import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.konan.target.HostManager
import java.time.Duration
import javax.inject.Inject

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

val sdkVersion = providers.gradleProperty("sdkCVersion")
val libraryVersion = providers.gradleProperty("libraryVersion")

val generateVersion = tasks.register("generateVersion") {
    group = "build"
    description = "Writes the version of the C library and of the iCalendar library into the sources."
    val output = layout.buildDirectory.dir("generated/version/nativeMain/kotlin")
    val sdk = sdkVersion
    val library = libraryVersion
    inputs.property("sdk", sdk)
    inputs.property("library", library)
    outputs.dir(output)
    doLast {
        val file = output.get().file("io/github/calendrum/tooling/sdkc/Version.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package io.github.calendrum.tooling.sdkc\n\n" +
                "internal const val SDK_VERSION = \"${sdk.get()}\"\n\n" +
                "internal const val LIBRARY_VERSION = \"${library.get()}\"\n",
        )
    }
}

val linkDirectory = layout.projectDirectory.dir("link")

kotlin {
    explicitApi()
    compilerOptions {
        allWarningsAsErrors = true
    }

    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.sharedLib(listOf(NativeBuildType.RELEASE)) {
            baseName = "calendrum"
            // -s on Linux: the compiler names its static symbols by source paths, which hold the home folder of whoever built this. On
            // macOS the export list already removes them, and on Windows the shared script of the archives does.
            // Only the four functions go out. On Linux the symbols of the runtime would otherwise bind across two Kotlin/Native
            // libraries in one process.
            when (targetName) {
                // The default install name is the path of the build directory, which would go into every program linked to it.
                "macosArm64" -> linkerOpts(
                    "-Wl,-exported_symbols_list,${linkDirectory.file("calendrum.exp").asFile.path}",
                    "-Wl,-install_name,@rpath/libcalendrum.dylib",
                )

                // The import library is not written by default, and the toolchain of the host cannot run dlltool.
                "mingwX64" -> linkerOpts(
                    linkDirectory.file("calendrum.def").asFile.path,
                    "-Wl,--out-implib,${outputDirectory.resolve("libcalendrum.dll.a").path}",
                )

                // The compiler asks for libcrypt, libutil and others that nothing here uses, and a wheel has to name them.
                else -> linkerOpts(
                    "-Wl,-s",
                    "-Wl,--version-script=${linkDirectory.file("calendrum.ver").asFile.path}",
                    "-Wl,-soname,libcalendrum.so",
                    "-Wl,--as-needed",
                )
            }
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        nativeMain {
            kotlin.srcDir(generateVersion)
            dependencies {
                implementation(project(":ops"))
            }
        }
    }
}

fun sharedLibrary(target: String) = kotlin.targets.withType<KotlinNativeTarget>().getByName(target)
    .binaries.getSharedLib(NativeBuildType.RELEASE)

val header = layout.projectDirectory.file("include/calendrum.h")

// The archives of every target. The same script packs the server and the command line.
extra["distArchiveBase"] = sdkVersion.map { "calendrum-sdk-c-$it" }
extra["distDescription"] = "the C library"
extra["distReadme"] = layout.projectDirectory.file("dist/README.txt")
extra["distLinkTasks"] = kotlin.targets.withType<KotlinNativeTarget>().associate {
    it.name to sharedLibrary(it.name).linkTaskProvider
}
extra["distBinaries"] = kotlin.targets.withType<KotlinNativeTarget>().associate {
    it.name to sharedLibrary(it.name).linkTaskProvider.flatMap { link -> link.outputFile }
}
extra["distExtraFiles"] = kotlin.targets.withType<KotlinNativeTarget>().associate { target ->
    val windows = target.name == "mingwX64"
    target.name to listOfNotNull(
        header,
        if (windows) linkDirectory.file("calendrum.def") else null,
        if (windows) sharedLibrary(target.name).outputDirectory.resolve("libcalendrum.dll.a") else null,
    )
}
apply(from = "../gradle/distribution.gradle.kts")

/** Runs a test program, shows what it prints and keeps it as the output, so that a run that passed is not repeated. */
abstract class RunCTest @Inject constructor(private val exec: ExecOperations) : DefaultTask() {
    @get:Input
    abstract val command: ListProperty<String>

    @get:Input
    abstract val variables: MapProperty<String, String>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val tested: ConfigurableFileCollection

    @get:OutputFile
    abstract val log: RegularFileProperty

    @TaskAction
    fun run() {
        val file = log.get().asFile
        file.parentFile.mkdirs()
        file.outputStream().use { stream ->
            val result = exec.exec {
                commandLine(command.get())
                environment(variables.get())
                standardOutput = stream
                errorOutput = stream
                isIgnoreExitValue = true
            }
            logger.lifecycle(file.readText())
            result.assertNormalExitValue()
        }
    }
}

// The real library, from C and from Python. A program of this kind runs only on the host it is built for.
if (HostManager.hostIsMac && System.getProperty("os.arch") == "aarch64") {
    val library = sharedLibrary("macosArm64")
    val libraryDirectory = library.outputDirectory
    val libraryFile = library.outputFile
    val testDirectory = layout.projectDirectory.dir("test")
    val includeDirectory = layout.projectDirectory.dir("include")
    val programDirectory = layout.buildDirectory.dir("ctest/bin")
    val logDirectory = layout.buildDirectory.dir("ctest/log")
    val casesDirectory = layout.settingsDirectory.dir("../conformance/ops")
    val cliBinary = project(":cli").layout.buildDirectory.file("bin/macosArm64/releaseExecutable/calendrum.kexe")

    fun compile(
        name: String,
        source: Provider<RegularFile>,
        compiler: String,
        standard: String,
        libraries: List<String>,
        extra: List<Any> = emptyList(),
    ) = tasks.register<Exec>("compileCTest${name.replaceFirstChar { it.uppercase() }}") {
        group = "build"
        description = "Compiles the C test $name against the release library."
        val output = programDirectory.map { it.file(name) }
        dependsOn(library.linkTaskProvider)
        extra.forEach { dependsOn(it) }
        inputs.file(source)
        inputs.dir(testDirectory)
        inputs.dir(includeDirectory)
        inputs.file(libraryFile)
        outputs.file(output)
        doFirst { output.get().asFile.parentFile.mkdirs() }
        val bin = programDirectory.get().asFile.path
        commandLine(
            listOf(
                compiler,
                "-std=$standard",
                "-Wall",
                "-Wextra",
                "-Werror",
                "-O1",
                "-I${includeDirectory.asFile.path}",
                "-I${testDirectory.asFile.path}",
                source.get().asFile.path,
                "-L$bin",
                "-L${libraryDirectory.path}",
                "-Wl,-rpath,$bin",
                "-Wl,-rpath,${libraryDirectory.path}",
            ) + libraries + listOf("-o", output.get().asFile.path),
        )
    }

    val compileInterpose = tasks.register<Exec>("compileCTestInterpose") {
        group = "build"
        description = "Compiles the library that makes memory run out for the tests."
        val output = programDirectory.map { it.file("libinterpose.dylib") }
        inputs.dir(testDirectory)
        outputs.file(output)
        doFirst { output.get().asFile.parentFile.mkdirs() }
        commandLine(
            "clang",
            "-std=c11",
            "-Wall",
            "-Wextra",
            "-Werror",
            "-O1",
            "-dynamiclib",
            "-I${testDirectory.asFile.path}",
            testDirectory.file("interpose.c").asFile.path,
            "-install_name",
            "@rpath/libinterpose.dylib",
            "-o",
            output.get().asFile.path,
        )
    }

    // The example of the guide is compiled from the guide itself: the C block with main.
    val guide = layout.settingsDirectory.file("../guides/c-api.md")
    val extractGuideExample = tasks.register("extractGuideExample") {
        group = "build"
        description = "Takes the example program out of guides/c-api.md."
        val output = layout.buildDirectory.file("ctest/guide_example.c")
        inputs.file(guide)
        outputs.file(output)
        doLast {
            val block = Regex("```c\\n(.*?)```", RegexOption.DOT_MATCHES_ALL).findAll(guide.asFile.readText())
                .map { it.groupValues[1] }
                .singleOrNull { "int main" in it }
            output.get().asFile.writeText(checkNotNull(block) { "guides/c-api.md needs exactly one C block with main" })
        }
    }

    val source = { name: String -> provider { testDirectory.file(name) } }
    val linked = listOf("-lcalendrum")
    val withInterpose = listOf("-linterpose", "-lcalendrum")
    val dynamic = emptyList<String>()
    val path = libraryFile.path
    val scratch = layout.buildDirectory.dir("ctest/scratch").get().asFile
    val cases = casesDirectory.asFile.canonicalPath

    // The name, the program and its arguments, in the order they run: they share the cores, and the limits in fork and fuzz are
    // about time, so that one at a time is what keeps them honest.
    class Program(val name: String, val compile: TaskProvider<Exec>, val arguments: List<String>)

    val programs = listOf(
        Program(
            "headerC",
            compile("headerC", source("header_check.c"), "clang", "c99", linked),
            emptyList(),
        ),
        Program(
            "headerCpp",
            compile("headerCpp", source("header_check.cpp"), "clang++", "c++11", linked),
            emptyList(),
        ),
        Program(
            "boundary",
            compile("boundary", source("boundary.c"), "clang", "c11", withInterpose, listOf(compileInterpose)),
            emptyList(),
        ),
        Program("threads", compile("threads", source("threads.c"), "clang", "c11", linked), emptyList()),
        Program("stack", compile("stack", source("stack.c"), "clang", "c11", linked), listOf(cases)),
        Program("fork", compile("fork", source("fork.c"), "clang", "c11", dynamic), listOf(path)),
        Program("unload", compile("unload", source("unload.c"), "clang", "c11", dynamic), listOf(path)),
        Program(
            "twoLoads",
            compile("twoLoads", source("two_loads.c"), "clang", "c11", dynamic),
            listOf(path, scratch.path),
        ),
        Program("leak", compile("leak", source("leak.c"), "clang", "c11", linked), emptyList()),
        Program(
            "processState",
            compile("processState", source("process_state.cpp"), "clang++", "c++17", dynamic),
            listOf(path),
        ),
        Program("loadCost", compile("loadCost", source("load_cost.c"), "clang", "c11", dynamic), listOf(path)),
        Program(
            "outOfMemory",
            compile("outOfMemory", source("out_of_memory.c"), "clang", "c11", withInterpose, listOf(compileInterpose)),
            emptyList(),
        ),
        Program(
            "fuzz",
            compile("fuzz", source("fuzz.c"), "clang", "c11", linked),
            listOf(cases, scratch.path, "60"),
        ),
        Program(
            "guideExample",
            compile(
                "guideExample",
                extractGuideExample.flatMap { task -> layout.buildDirectory.file("ctest/guide_example.c") },
                "clang",
                "c99",
                linked,
                listOf(extractGuideExample),
            ),
            emptyList(),
        ),
    )

    val pythonTests = listOf(
        tasks.register<RunCTest>("testExports") {
            group = "verification"
            description = "Compares the exports of the libraries of all targets with link/symbols.txt."
            val windows = sharedLibrary("mingwX64")
            val linuxX64 = sharedLibrary("linuxX64")
            val linuxArm64 = sharedLibrary("linuxArm64")
            dependsOn(
                library.linkTaskProvider,
                windows.linkTaskProvider,
                linuxX64.linkTaskProvider,
                linuxArm64.linkTaskProvider,
            )
            command.set(
                listOf(
                    "python3",
                    "-I",
                    testDirectory.file("check_exports.py").asFile.path,
                    linkDirectory.asFile.path,
                    libraryFile.path,
                    linuxX64.outputFile.path,
                    linuxArm64.outputFile.path,
                    windows.outputFile.path,
                ),
            )
            tested.from(libraryFile, linuxX64.outputFile, linuxArm64.outputFile, windows.outputFile, linkDirectory)
            tested.from(testDirectory.file("check_exports.py"))
            log.set(logDirectory.map { it.file("exports.txt") })
            timeout.set(Duration.ofMinutes(2))
        },
        tasks.register<RunCTest>("testContract") {
            group = "verification"
            description =
                "Runs the cases of the contract through the library from Python, and compares with the command line."
            dependsOn(library.linkTaskProvider, ":cli:linkReleaseExecutableMacosArm64")
            command.set(listOf("python3", "-I", testDirectory.file("test_contract.py").asFile.path))
            variables.put("CALENDRUM_LIBRARY", path)
            variables.put("CALENDRUM_CASES", cases)
            variables.put("CALENDRUM_CLI", cliBinary.get().asFile.path)
            tested.from(libraryFile, casesDirectory, cliBinary, testDirectory.file("test_contract.py"))
            log.set(logDirectory.map { it.file("contract.txt") })
            timeout.set(Duration.ofMinutes(5))
        },
    )

    val programRuns = programs.map { program ->
        tasks.register<RunCTest>("cTest${program.name.replaceFirstChar { it.uppercase() }}") {
            group = "verification"
            description = "Runs the C test ${program.name}."
            dependsOn(program.compile)
            val binary = programDirectory.map { it.file(program.name) }
            command.set(binary.map { listOf(it.asFile.path) + program.arguments })
            tested.from(binary, libraryFile, casesDirectory)
            log.set(logDirectory.map { it.file("${program.name}.txt") })
            timeout.set(Duration.ofMinutes(10))
            doFirst { scratch.mkdirs() }
        }
    }
    programRuns.zipWithNext { earlier, later -> later.configure { mustRunAfter(earlier) } }

    tasks.named("check") {
        dependsOn(programRuns)
        dependsOn(pythonTests)
    }
}

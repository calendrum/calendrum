import java.io.File
import java.time.Duration
import javax.inject.Inject

plugins {
    base
}

val sdkC = project(":sdk-c")
val sdkCBuild = sdkC.layout.buildDirectory
val notice = layout.settingsDirectory.file("../NOTICE")
val license = layout.settingsDirectory.file("../LICENSE")
val cases = layout.settingsDirectory.dir("../conformance/ops")
val guide = layout.settingsDirectory.file("../guides/dotnet.md")
val sources = layout.projectDirectory.dir("Calendrum")
val testSources = layout.projectDirectory.dir("Calendrum.Tests")
val measureSources = layout.projectDirectory.dir("Calendrum.Measure")
val checks = layout.projectDirectory.dir("checks")
val shared =
    listOf("Directory.Build.props", "global.json", "nuget.config", ".editorconfig").map {
        layout.projectDirectory.file(it)
    }

val targets =
    listOf(
        "macosArm64" to "libcalendrum.dylib",
        "linuxX64" to "libcalendrum.so",
        "linuxArm64" to "libcalendrum.so",
        "mingwX64" to "calendrum.dll",
    )

fun capitalized(name: String) = name.replaceFirstChar { it.uppercase() }

// -Pdotnet.root or DOTNET_ROOT, then the private folder of the guide, then the PATH. A missing SDK fails where it is
// needed, with what to do.
val dotnetRoot: Provider<String> =
    providers
        .gradleProperty("dotnet.root")
        .orElse(providers.environmentVariable("DOTNET_ROOT"))
        .orElse(providers.systemProperty("user.home").map { "$it/.dotnet" })

/** Runs the .NET SDK or a program built with it. What the SDK writes stays in its private folder, if it has one. */
abstract class RunDotnet
    @Inject
    constructor(
        private val exec: ExecOperations,
    ) : DefaultTask() {
        @get:Input
        abstract val root: Property<String>

        @get:Input
        abstract val arguments: ListProperty<String>

        @get:Input
        @get:Optional
        abstract val program: Property<String>

        @get:Input
        abstract val variables: MapProperty<String, String>

        @get:Internal
        abstract val workingFolder: DirectoryProperty

        @get:InputFiles
        @get:PathSensitive(PathSensitivity.RELATIVE)
        abstract val tested: ConfigurableFileCollection

        @get:OutputFile
        abstract val log: RegularFileProperty

        @TaskAction
        fun run() {
            val sdk = File(root.get(), "dotnet")
            val fromPath = if (sdk.canExecute()) null else "dotnet"
            val file = log.get().asFile
            file.parentFile.mkdirs()
            val home = File(root.get(), "home")
            val result =
                file.outputStream().use { stream ->
                    exec.exec {
                        commandLine(listOf(program.orNull ?: fromPath ?: sdk.path) + arguments.get())
                        workingFolder.orNull?.let { workingDir = it.asFile }
                        environment("DOTNET_CLI_TELEMETRY_OPTOUT", "1")
                        environment("DOTNET_NOLOGO", "1")
                        environment("DOTNET_SKIP_FIRST_TIME_EXPERIENCE", "1")
                        environment("DOTNET_GENERATE_ASPNET_CERTIFICATE", "false")
                        environment("DOTNET_CLI_UI_LANGUAGE", "en")
                        // No servers that outlive the build: a night of measurements may start right after it.
                        environment("MSBUILDDISABLENODEREUSE", "1")
                        environment("UseSharedCompilation", "false")
                        if (fromPath == null) {
                            environment("DOTNET_ROOT", root.get())
                            home.mkdirs()
                            environment("HOME", home.path)
                        }
                        environment(variables.get())
                        standardOutput = stream
                        errorOutput = stream
                        isIgnoreExitValue = true
                    }
                }
            logger.lifecycle(file.readText())
            result.assertNormalExitValue()
        }
    }

val logDirectory = layout.buildDirectory.dir("test-logs")
val distDirectory = layout.buildDirectory.dir("dist")

val checkGenerated =
    tasks.register<Exec>("checkGenerated") {
        group = "verification"
        description = "Checks that the generated files of the package are what generate.py writes."
        val script = layout.projectDirectory.file("generate.py")
        commandLine("python3", "-I", "-B", script.asFile.path, "--check")
        inputs.file(script)
        inputs.dir(cases.dir("schema"))
        inputs.file(layout.settingsDirectory.file("gradle.properties"))
        inputs.dir(sources)
    }

val findSdk =
    tasks.register("findDotnetSdk") {
        group = "verification"
        description = "Fails with what to do when there is no .NET SDK here."
        val root = dotnetRoot
        doLast {
            val here = File(root.get(), "dotnet").canExecute()
            val folders = (System.getenv("PATH") ?: "").split(File.pathSeparator)
            val onPath = folders.any { File(it, "dotnet").canExecute() }
            if (!here && !onPath) {
                throw GradleException(
                    "The .NET SDK 10 is not in ${root.get()} and not on the PATH. Install it, give its folder with " +
                        "-Pdotnet.root, or leave this module out with -x :sdk-net:check.",
                )
            }
        }
    }

val dist =
    tasks.register<RunDotnet>("dist") {
        group = "distribution"
        description = "Builds the package with the library of every target into build/dist."
        dependsOn(checkGenerated, findSdk)
        targets.forEach { (name, _) ->
            dependsOn(":sdk-c:linkReleaseShared${capitalized(name)}", ":sdk-c:writeLicenses${capitalized(name)}")
        }
        root.set(dotnetRoot)
        workingFolder.set(layout.projectDirectory)
        val out = distDirectory.get().asFile
        arguments.set(listOf("pack", "Calendrum/Calendrum.csproj", "-c", "Release", "--nologo", "-o", out.path))
        tested.from(sources, shared, notice, license)
        targets.forEach { (name, file) ->
            tested.from(
                sdkCBuild.file("bin/$name/releaseShared/$file"),
                sdkCBuild.file("licenses/$name/THIRD-PARTY-LICENSES.txt"),
            )
        }
        log.set(logDirectory.map { it.file("dist.txt") })
        outputs.dir(distDirectory)
        doFirst { out.deleteRecursively() }
        timeout.set(Duration.ofMinutes(5))
    }

val checkFormat =
    tasks.register<RunDotnet>("checkFormat") {
        group = "verification"
        description = "Checks the style of the C# sources."
        dependsOn(checkGenerated, findSdk)
        root.set(dotnetRoot)
        workingFolder.set(layout.projectDirectory)
        arguments.set(listOf("format", "Calendrum.slnx", "--verify-no-changes", "--verbosity", "minimal"))
        tested.from(sources, testSources, measureSources, shared)
        log.set(logDirectory.map { it.file("format.txt") })
        timeout.set(Duration.ofMinutes(5))
    }

// The real library runs only on the host it is built for.
if (System.getProperty("os.name") == "Mac OS X" && System.getProperty("os.arch") == "aarch64") {
    val buildTests =
        tasks.register<RunDotnet>("buildTests") {
            group = "verification"
            description = "Builds the tests of the package."
            dependsOn(checkGenerated, findSdk, ":sdk-c:linkReleaseSharedMacosArm64")
            root.set(dotnetRoot)
            workingFolder.set(layout.projectDirectory)
            arguments.set(listOf("build", "Calendrum.Tests/Calendrum.Tests.csproj", "-c", "Release", "--nologo"))
            tested.from(sources, testSources, shared, sdkCBuild.file("bin/macosArm64/releaseShared/libcalendrum.dylib"))
            log.set(logDirectory.map { it.file("build-tests.txt") })
            timeout.set(Duration.ofMinutes(5))
        }

    val testPackage =
        tasks.register<RunDotnet>("testNet") {
            group = "verification"
            description = "Runs the tests of the package on the real library."
            dependsOn(buildTests)
            root.set(dotnetRoot)
            val binary = layout.buildDirectory.file("artifacts/bin/Calendrum.Tests/release/Calendrum.Tests")
            program.set(binary.map { it.asFile.path })
            variables.put("CALENDRUM_CASES", cases.asFile.canonicalPath)
            variables.put(
                "CALENDRUM_API",
                layout.projectDirectory
                    .file("Calendrum/api/Calendrum.api.txt")
                    .asFile.path,
            )
            tested.from(sources, testSources, cases, sdkCBuild.file("bin/macosArm64/releaseShared/libcalendrum.dylib"))
            log.set(logDirectory.map { it.file("tests.txt") })
            timeout.set(Duration.ofMinutes(15))
            mustRunAfter(":sdk-c:check")
        }

    // The oldest .NET the package is for, without installing it: the program carries its runtime, from a pack.
    val buildTestsOldest =
        tasks.register<RunDotnet>("buildTestsOldest") {
            group = "verification"
            description = "Builds the tests of the package on .NET 8."
            dependsOn(checkGenerated, findSdk, ":sdk-c:linkReleaseSharedMacosArm64")
            root.set(dotnetRoot)
            workingFolder.set(layout.projectDirectory)
            val base =
                layout.buildDirectory
                    .dir("oldest")
                    .get()
                    .asFile
            arguments.set(
                listOf(
                    "publish",
                    "Calendrum.Tests/Calendrum.Tests.csproj",
                    "-c",
                    "Release",
                    "--nologo",
                    "-p:TestFramework=net8.0",
                    "-r",
                    "osx-arm64",
                    "--self-contained",
                    "true",
                    "-p:ArtifactsPath=${base.resolve("artifacts").path}",
                    "-o",
                    base.resolve("program").path,
                ),
            )
            tested.from(sources, testSources, shared, sdkCBuild.file("bin/macosArm64/releaseShared/libcalendrum.dylib"))
            log.set(logDirectory.map { it.file("build-tests-oldest.txt") })
            timeout.set(Duration.ofMinutes(5))
            mustRunAfter(testPackage)
        }

    val testOldest =
        tasks.register<RunDotnet>("testNetOldest") {
            group = "verification"
            description = "Runs the tests of the package on .NET 8."
            dependsOn(buildTestsOldest)
            root.set(dotnetRoot)
            program.set(layout.buildDirectory.file("oldest/program/Calendrum.Tests").map { it.asFile.path })
            variables.put("CALENDRUM_CASES", cases.asFile.canonicalPath)
            variables.put(
                "CALENDRUM_API",
                layout.projectDirectory
                    .file("Calendrum/api/Calendrum.api.txt")
                    .asFile.path,
            )
            tested.from(sources, testSources, cases, sdkCBuild.file("bin/macosArm64/releaseShared/libcalendrum.dylib"))
            log.set(logDirectory.map { it.file("tests-oldest.txt") })
            timeout.set(Duration.ofMinutes(15))
        }

    val checkPackage =
        tasks.register<RunDotnet>("checkPackage") {
            group = "verification"
            description = "Reads the package, installs it in a clean project and runs the blocks of the guide."
            dependsOn(dist)
            root.set(dotnetRoot)
            val checker = checks.file("package_check.py")
            program.set("python3")
            arguments.set(
                listOf(
                    "-I",
                    "-B",
                    checker.asFile.path,
                    distDirectory.get().asFile.path,
                    guide.asFile.canonicalPath,
                    layout.buildDirectory
                        .dir("package-check")
                        .get()
                        .asFile.path,
                    File(dotnetRoot.get(), "dotnet").path,
                ),
            )
            tested.from(distDirectory, checks, guide)
            log.set(logDirectory.map { it.file("package.txt") })
            timeout.set(Duration.ofMinutes(10))
            mustRunAfter(testOldest)
        }

    tasks.register<RunDotnet>("checkPublishing") {
        group = "verification"
        description = "Publishes under Native AOT and as a single file; asks nuget.org for 340 MB."
        dependsOn(dist)
        root.set(dotnetRoot)
        program.set("python3")
        arguments.set(
            listOf(
                "-I",
                "-B",
                checks.file("package_check.py").asFile.path,
                distDirectory.get().asFile.path,
                guide.asFile.canonicalPath,
                layout.buildDirectory
                    .dir("publishing-check")
                    .get()
                    .asFile.path,
                File(dotnetRoot.get(), "dotnet").path,
                "PublishingTest",
            ),
        )
        variables.put("CALENDRUM_CHECK_PUBLISHING", "1")
        tested.from(distDirectory, checks, guide)
        log.set(logDirectory.map { it.file("publishing.txt") })
        timeout.set(Duration.ofMinutes(20))
    }

    tasks.named("check") {
        dependsOn(checkGenerated, checkFormat, testPackage, testOldest, checkPackage)
    }
} else {
    tasks.named("check") {
        dependsOn(checkGenerated, checkFormat, dist)
    }
}

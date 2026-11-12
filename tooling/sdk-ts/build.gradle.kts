import java.time.Duration
import javax.inject.Inject

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

val sdkVersion = providers.gradleProperty("sdkTsVersion")
val libraryVersion = providers.gradleProperty("libraryVersion")

val generateVersion = tasks.register("generateVersion") {
    group = "build"
    description = "Writes the version of the package and of the iCalendar library into the sources."
    val output = layout.buildDirectory.dir("generated/version/jsMain/kotlin")
    val sdk = sdkVersion
    val library = libraryVersion
    inputs.property("sdk", sdk)
    inputs.property("library", library)
    outputs.dir(output)
    doLast {
        val file = output.get().file("io/github/calendrum/tooling/sdkts/Version.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package io.github.calendrum.tooling.sdkts\n\n" +
                "internal const val SDK_VERSION = \"${sdk.get()}\"\n\n" +
                "internal const val LIBRARY_VERSION = \"${library.get()}\"\n",
        )
    }
}

kotlin {
    explicitApi()
    compilerOptions {
        allWarningsAsErrors = true
    }

    js {
        // No TypeScript definitions from the compiler: the public types come from the schemas, without $metadata$.
        outputModuleName = "calendrum-core"
        nodejs()
        useEsModules()
        binaries.library()
    }

    sourceSets {
        jsMain {
            kotlin.srcDir(generateVersion)
            dependencies {
                implementation(project(":ops"))
            }
        }
    }
}

val packageSources = layout.projectDirectory.dir("package")
val testSources = layout.projectDirectory.dir("tests")
val cases = layout.settingsDirectory.dir("../conformance/ops")
val guide = layout.settingsDirectory.file("../guides/typescript.md")
val notice = layout.settingsDirectory.file("../NOTICE")
val license = layout.settingsDirectory.file("../LICENSE")
val generator = layout.projectDirectory.file("generate.mjs")

extra["licensesConfiguration"] = "jsRuntimeClasspath"
apply(from = "../gradle/third-party-licenses.gradle.kts")

/** Runs a Node program, shows what it prints and keeps it as the output, so that a run that passed is not repeated. */
abstract class RunNode @Inject constructor(private val exec: ExecOperations) : DefaultTask() {
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

val logDirectory = layout.buildDirectory.dir("test-logs")

val checkGenerated = tasks.register<RunNode>("checkGenerated") {
    group = "verification"
    description = "Checks that the generated files of the package are what generate.mjs writes."
    command.set(listOf("node", generator.asFile.path, "--check"))
    tested.from(generator, packageSources, cases.dir("schema"), layout.settingsDirectory.file("gradle.properties"))
    log.set(logDirectory.map { it.file("generated.txt") })
    timeout.set(Duration.ofMinutes(1))
}

// The package as it is installed: the hand-written modules at the top and the output of the compiler under lib/.
val assemblePackage = tasks.register<Sync>("assemblePackage") {
    group = "build"
    description = "Puts the package together in build/package."
    from(packageSources)
    from(notice, license)
    from(tasks.named("writeThirdPartyLicenses"))
    from(tasks.named("jsNodeProductionLibraryDistribution")) {
        include("*.mjs")
        // The compiler writes this empty module, and nothing imports it.
        exclude("kotlin_org_jetbrains_kotlin_kotlin_dom_api_compat.mjs")
        into("lib")
    }
    into(layout.buildDirectory.dir("package"))
}

val packageDirectory = layout.buildDirectory.dir("package")

val dist = tasks.register<Tar>("dist") {
    group = "distribution"
    description = "Packs the package as npm does, into build/dist."
    dependsOn(checkGenerated)
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    archiveFileName.set(sdkVersion.map { "calendrum-$it.tgz" })
    compression = Compression.GZIP
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    from(assemblePackage) {
        into("package")
        filePermissions { unix("644") }
    }
}

val testPackage = tasks.register<RunNode>("testPackage") {
    group = "verification"
    description = "Runs the tests of the package on the Node of the path."
    dependsOn(assemblePackage)
    command.set(
        testSources.asFile.listFiles().orEmpty()
            .filter { it.name.endsWith(".test.mjs") }
            .map { it.path }
            .sorted()
            .let { files -> listOf("node", "--test", "--test-concurrency=1", "--test-reporter=spec") + files },
    )
    variables.put("CALENDRUM_PACKAGE", packageDirectory.get().asFile.path)
    variables.put("CALENDRUM_CASES", cases.asFile.canonicalPath)
    variables.put("CALENDRUM_GUIDE", guide.asFile.canonicalPath)
    tested.from(packageDirectory, testSources, cases, guide, generator)
    tested.from(layout.settingsDirectory.file("gradle.properties"))
    log.set(logDirectory.map { it.file("package.txt") })
    timeout.set(Duration.ofMinutes(15))
    mustRunAfter(":sdk-c:check", ":sdk-py:check")
}

val testTarball = tasks.register<RunNode>("testTarball") {
    group = "verification"
    description = "Installs the tarball into a clean directory without the network and uses it."
    val checker = testSources.file("tarball-check.mjs")
    command.set(dist.flatMap { it.archiveFile }.map { listOf("node", checker.asFile.path, it.asFile.path) })
    tested.from(dist, checker)
    log.set(logDirectory.map { it.file("tarball.txt") })
    timeout.set(Duration.ofMinutes(5))
    mustRunAfter(testPackage)
}

// TypeScript is not a dependency of this project: the check runs where a compiler is at hand, and says so otherwise.
val typescript = providers.gradleProperty("sdkts.typescript")

tasks.named("check") {
    dependsOn(checkGenerated, testPackage, testTarball)
}

if (typescript.isPresent) {
    val checkTypes = tasks.register<RunNode>("checkTypes") {
        group = "verification"
        description = "Checks the public types and the guide with the TypeScript compiler of -Psdkts.typescript."
        dependsOn(assemblePackage)
        val checker = testSources.file("types-check.mjs")
        command.set(typescript.map { listOf("node", checker.asFile.path, it, packageDirectory.get().asFile.path) })
        variables.put("CALENDRUM_GUIDE", guide.asFile.canonicalPath)
        tested.from(packageDirectory, testSources, guide)
        log.set(logDirectory.map { it.file("types.txt") })
        timeout.set(Duration.ofMinutes(5))
        mustRunAfter(testTarball)
    }
    tasks.named("check") { dependsOn(checkTypes) }
}

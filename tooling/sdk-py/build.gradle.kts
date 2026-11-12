import java.time.Duration
import javax.inject.Inject

plugins {
    base
}

val sdkVersion = providers.gradleProperty("sdkPyVersion")
val sdkCBuild = project(":sdk-c").layout.buildDirectory
val notice = layout.settingsDirectory.file("../NOTICE")
val license = layout.settingsDirectory.file("../LICENSE")
val cases = layout.settingsDirectory.dir("../conformance/ops")
val guide = layout.settingsDirectory.file("../guides/python.md")
val packageSources = layout.projectDirectory.dir("calendrum")
val testSources = layout.projectDirectory.dir("tests")

class Target(val name: String, val library: String) {
    val capitalized = name.replaceFirstChar { it.uppercase() }
    val link = ":sdk-c:linkReleaseShared$capitalized"
    val licensesTask = ":sdk-c:writeLicenses$capitalized"
}

val targets = listOf(
    Target("macosArm64", "libcalendrum.dylib"),
    Target("linuxX64", "libcalendrum.so"),
    Target("linuxArm64", "libcalendrum.so"),
    Target("mingwX64", "calendrum.dll"),
)

fun libraryOf(target: Target) = sdkCBuild.file("bin/${target.name}/releaseShared/${target.library}")

fun licensesOf(target: Target) = sdkCBuild.file("licenses/${target.name}/THIRD-PARTY-LICENSES.txt")

/** Runs a Python program, shows what it prints and keeps it as the output, so that a run that passed is not repeated. */
abstract class RunPython @Inject constructor(private val exec: ExecOperations) : DefaultTask() {
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

val checkGenerated = tasks.register<RunPython>("checkGenerated") {
    group = "verification"
    description = "Checks that the generated files of the package are what generate.py writes."
    val script = layout.projectDirectory.file("generate.py")
    command.set(listOf("python3", "-I", "-B", script.asFile.path, "--check"))
    tested.from(script, packageSources, cases.dir("schema"), layout.settingsDirectory.file("gradle.properties"))
    log.set(logDirectory.map { it.file("generated.txt") })
    timeout.set(Duration.ofMinutes(1))
}

val wheelDirectory = layout.buildDirectory.dir("dist")
val buildScript = layout.projectDirectory.file("build_wheels.py")

fun wheelArguments() = listOf("--version", sdkVersion.get()) +
    listOf("--license", license.asFile.path, "--notice", notice.asFile.path) +
    targets.flatMap { target ->
        listOf("--target", target.name, libraryOf(target).get().asFile.path, licensesOf(target).get().asFile.path)
    }

val dist = tasks.register<Exec>("dist") {
    group = "distribution"
    description = "Builds the wheel of every target into build/dist."
    dependsOn(checkGenerated)
    targets.forEach {
        dependsOn(it.link, it.licensesTask)
        inputs.file(libraryOf(it))
        inputs.file(licensesOf(it))
    }
    inputs.file(buildScript)
    inputs.file(license)
    inputs.file(notice)
    inputs.dir(packageSources)
    inputs.property("version", sdkVersion)
    outputs.dir(wheelDirectory)
    val out = wheelDirectory.get().asFile
    doFirst { out.deleteRecursively() }
    commandLine(listOf("python3", "-I", "-B", buildScript.asFile.path) + wheelArguments() + listOf("--out", out.path))
}

// The real library runs only on the host it is built for.
if (System.getProperty("os.name") == "Mac OS X" && System.getProperty("os.arch") == "aarch64") {
    val host = targets.first { it.name == "macosArm64" }

    // A copy of the package with the library next to it, as a wheel has it. A package in the source tree has no library.
    val testPackage = tasks.register<Sync>("testPackage") {
        group = "build"
        description = "Puts the package and the native library of this host together for the tests."
        dependsOn(host.link)
        from(packageSources) {
            include("*.py", "py.typed")
            into("calendrum")
        }
        from(libraryOf(host)) { into("calendrum") }
        into(layout.buildDirectory.dir("test-package"))
    }

    fun testOn(name: String, python: String, version: String) = tasks.register<RunPython>(name) {
        group = "verification"
        description = "Runs the tests of the package on Python $version."
        dependsOn(testPackage)
        val runner = testSources.file("run_tests.py")
        command.set(listOf(python, "-I", "-B", runner.asFile.path, version))
        variables.put("CALENDRUM_PACKAGE", layout.buildDirectory.dir("test-package").get().asFile.path)
        variables.put("CALENDRUM_CASES", cases.asFile.canonicalPath)
        variables.put("CALENDRUM_GUIDE", guide.asFile.canonicalPath)
        tested.from(layout.buildDirectory.dir("test-package"), testSources, cases, guide)
        tested.from(layout.projectDirectory.file("generate.py"), layout.settingsDirectory.file("gradle.properties"))
        log.set(logDirectory.map { it.file("$name.txt") })
        timeout.set(Duration.ofMinutes(15))
        mustRunAfter(":sdk-c:check")
    }

    val latest = testOn("testPythonLatest", "python3", "3.10+")
    val oldest = testOn("testPythonOldest", "/usr/bin/python3", "3.9")
    oldest.configure { mustRunAfter(latest) }

    val testWheels = tasks.register<RunPython>("testWheels") {
        group = "verification"
        description = "Reads the wheels, asks pip about their tags and installs the one of this host into a clean venv."
        dependsOn(dist)
        val checker = testSources.file("wheels_check.py")
        command.set(
            listOf(
                "python3",
                "-I",
                "-B",
                checker.asFile.path,
                wheelDirectory.get().asFile.path,
                "python3",
                "/usr/bin/python3",
            ),
        )
        val arguments = wheelArguments()
        variables.put(
            "CALENDRUM_WHEEL_ARGUMENTS",
            arguments.joinToString(",", "[", "]") { "\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}\"" },
        )
        tested.from(wheelDirectory, checker)
        log.set(logDirectory.map { it.file("wheels.txt") })
        timeout.set(Duration.ofMinutes(10))
        mustRunAfter(oldest)
    }

    tasks.named("check") {
        dependsOn(checkGenerated, latest, oldest, testWheels)
    }
}

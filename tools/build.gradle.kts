plugins {
    kotlin("jvm")
    id("calendrum.lint")
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

dependencies {
    implementation(project(":calendrum-core"))
    implementation(project(":calendrum-tzdata"))
    testImplementation(kotlin("test"))
}

// The tests read the sample calendars from `tools/samples`; they are an input of the task.
tasks.test {
    useJUnit()
    systemProperty("calendrum.samples", layout.projectDirectory.dir("samples").asFile.absolutePath)
    inputs.dir(layout.projectDirectory.dir("samples")).withPathSensitivity(PathSensitivity.RELATIVE)
}

// Runs the CLI from the repository root, so relative paths in `--args` resolve from there.
tasks.register<JavaExec>("anonymize") {
    group = "tools"
    description = "Anonymizes a calendar or a directory of calendars: --args=\"<input> <output> " +
        "[--key-file <file>] [--shift-weeks <n>]\"; the key may also come from CALENDRUM_ANON_KEY."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("io.github.calendrum.tools.AnonymizeKt")
    workingDir = layout.settingsDirectory.asFile
}

// Runs the generator; arguments: an output directory and --base <date>.
tasks.register<JavaExec>("referenceFeed") {
    group = "tools"
    description = "Writes the reference feed for manual acceptance in calendar clients to build/reference-feed: " +
        "--args=\"[<output directory>] [--base <yyyy-mm-dd>]\", the base defaults to the next Monday."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("io.github.calendrum.tools.ReferenceFeedKt")
    workingDir = layout.projectDirectory.asFile
}

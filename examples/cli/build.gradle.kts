plugins {
    kotlin("jvm")
    application
}

// The Calendrum version is VERSION_NAME of the root gradle.properties, resolved from mavenLocal (only the io.github.calendrum
// group comes from there).
val calendrumVersion = providers.fileContents(layout.projectDirectory.file("../../gradle.properties")).asText
    .map { text -> text.lines().first { it.startsWith("VERSION_NAME=") }.substringAfter('=') }

dependencies {
    implementation(calendrumVersion.map { "io.github.calendrum:calendrum-core:$it" })
    implementation(calendrumVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "io.github.calendrum.examples.cli.MainKt"
}

tasks.test {
    systemProperty("examples.sample", layout.projectDirectory.file("../shared/sample.ics").asFile.absolutePath)
}

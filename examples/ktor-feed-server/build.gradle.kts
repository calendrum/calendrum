plugins {
    kotlin("jvm")
    application
}

val catalog = file("../../gradle/libs.versions.toml").readLines()
val ktorVersion = catalog.first { it.startsWith("ktor = ") }.substringAfter('"').substringBefore('"')

// The Calendrum version is VERSION_NAME of the root gradle.properties, resolved from mavenLocal (only the io.github.calendrum
// group comes from there).
val calendrumVersion = providers.fileContents(layout.projectDirectory.file("../../gradle.properties")).asText
    .map { text -> text.lines().first { it.startsWith("VERSION_NAME=") }.substringAfter('=') }

dependencies {
    implementation(calendrumVersion.map { "io.github.calendrum:calendrum-ktor:$it" })
    implementation(calendrumVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "io.github.calendrum.examples.feed.MainKt"
}

plugins {
    kotlin("jvm")
}

val calendrumVersion = providers.gradleProperty("calendrumVersion").get()

repositories {
    // Only Calendrum comes from ~/.m2; everything else must come from Maven Central, as it would for a real consumer.
    mavenLocal {
        content {
            includeGroup("io.github.calendrum")
        }
    }
    mavenCentral()
}

dependencies {
    implementation(platform("io.github.calendrum:calendrum-bom:$calendrumVersion"))

    // No version: it comes from the BOM. A plain kotlin("jvm") project also checks that Gradle module metadata selects
    // the JVM variant.
    implementation("io.github.calendrum:calendrum-core")
    implementation("io.github.calendrum:calendrum-jcal")
    implementation("io.github.calendrum:calendrum-kotlinx-io")
    implementation("io.github.calendrum:calendrum-okio")
    implementation("io.github.calendrum:calendrum-ktor")
    implementation("io.github.calendrum:calendrum-ical4j-interop")

    // kotlin-test of the consumer's Kotlin version; with no test framework configured, KGP picks kotlin-test-junit
    // (JUnit 4).
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:3.0.0")
    testImplementation("io.ktor:ktor-client-content-negotiation:3.0.0")
}

// The test compares Calendrum.VERSION with the version run.sh passed in. Generated source works the same on every target;
// system properties would not.
val generateExpectedVersion = tasks.register("generateExpectedVersion") {
    val outputDir = layout.buildDirectory.dir("generated/compat/kotlin")
    inputs.property("calendrumVersion", calendrumVersion)
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get().file("io/github/calendrum/compat/ExpectedVersion.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package io.github.calendrum.compat\n\ninternal const val EXPECTED_CALENDRUM_VERSION: String = \"$calendrumVersion\"\n",
        )
    }
}

kotlin {
    sourceSets.named("test") {
        kotlin.srcDir(generateExpectedVersion)
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

// The Calendrum version is VERSION_NAME of the root gradle.properties, resolved from mavenLocal (only the io.github.calendrum
// group comes from there).
val calendrumVersion = providers.fileContents(
    rootProject.layout.projectDirectory.file("../../gradle.properties"),
).asText
    .map { text -> text.lines().first { it.startsWith("VERSION_NAME=") }.substringAfter('=') }

android {
    namespace = "io.github.calendrum.examples.android"
    compileSdk = 36
    defaultConfig {
        applicationId = "io.github.calendrum.examples.android"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    sourceSets {
        named("main") {
            // The app reads examples/shared/sample.ics from its assets, so every example shows the same feed.
            assets.directories += rootProject.layout.projectDirectory.dir("../shared").asFile.path
        }
    }
    testOptions {
        unitTests.all {
            it.systemProperty(
                "examples.sample",
                rootProject.layout.projectDirectory.file("../shared/sample.ics").asFile.absolutePath,
            )
        }
    }
}

dependencies {
    implementation(calendrumVersion.map { "io.github.calendrum:calendrum-core:$it" })
    implementation(calendrumVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.activity.compose)
    testImplementation(libs.junit)
}

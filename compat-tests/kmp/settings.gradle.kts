// The Kotlin plugin version comes from -PkotlinVersion, so one template serves every Kotlin version of the matrix.
// A consumer project template, a standalone Gradle build that is not part of the root build. run.sh copies it to
// compat-tests/build/cases/<case>/ and passes kotlinVersion, calendrumVersion and compatTargets as Gradle properties.

pluginManagement {
    val kotlinVersion = providers.gradleProperty("kotlinVersion").get()
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("multiplatform") version kotlinVersion
    }
}

rootProject.name = "calendrum-compat-kmp"

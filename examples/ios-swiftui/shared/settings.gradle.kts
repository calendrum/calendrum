// The Kotlin version is read as text from the catalog: plugin versions are needed before any catalog is available, and
// an example must build with the same Kotlin as the library.
pluginManagement {
    val catalog = file("../../../gradle/libs.versions.toml").readLines()
    val kotlinVersion = catalog.first { it.startsWith("kotlin = ") }.substringAfter('"').substringBefore('"')
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("multiplatform") version kotlinVersion
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal {
            content {
                includeGroup("io.github.calendrum")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "calendrum-example-ios-shared"

// The Kotlin and AGP versions are read as text from the catalogs: plugin versions are needed before any catalog is
// available, and an example must build with the same Kotlin as the library.
pluginManagement {
    fun version(path: String, key: String): String =
        file(path).readLines().first { it.startsWith("$key = ") }.substringAfter('"').substringBefore('"')
    val kotlinVersion = version("../../gradle/libs.versions.toml", "kotlin")
    val agpVersion = version("gradle/libs.versions.toml", "agp")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version agpVersion
        id("org.jetbrains.kotlin.android") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal {
            content {
                includeGroup("io.github.calendrum")
            }
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "calendrum-example-android-compose"

include(":app")

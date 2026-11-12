pluginManagement {
    val catalog = file("../../gradle/libs.versions.toml").readLines()
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
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "calendrum-sdk-c"

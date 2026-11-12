// A standalone build, like examples/ and compat-tests/: it uses the published library from mavenLocal and has its own
// Kotlin version, which the root build (frozen on 2.3.x for its minimum consumers) cannot read in kotlin-sdk's klibs.
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
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

rootProject.name = "calendrum-tooling"

include(":cli", ":mcp", ":ops", ":platform", ":sdk-c", ":sdk-net", ":sdk-py", ":sdk-ts")

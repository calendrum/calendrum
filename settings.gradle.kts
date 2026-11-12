pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "calendrum"

include(
    ":calendrum-core",
    ":calendrum-tzdata",
    ":calendrum-jcal",
    ":calendrum-kotlinx-io",
    ":calendrum-okio",
    ":calendrum-ktor",
    ":calendrum-ical4j-interop",
    ":calendrum-bom",
    ":benchmarks",
    ":conformance",
    ":fuzz",
    ":tools",
)

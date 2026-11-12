plugins {
    id("calendrum.kmp-library")
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("io.github.calendrum.InternalICalApi")
    }
    sourceSets.commonMain {
        dependencies {
            api(project(":calendrum-core"))
            api(libs.kotlinx.serialization.json)
        }
    }
}

plugins {
    id("calendrum.kmp-library")
}

kotlin {
    sourceSets.commonMain {
        dependencies {
            api(project(":calendrum-core"))
            api(libs.okio)
        }
    }
}

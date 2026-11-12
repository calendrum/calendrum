plugins {
    id("size-budget.app")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(budget.calendrum.core)
    }
}

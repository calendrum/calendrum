plugins {
    base
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.spotless)
}

val ktlintVersion = libs.versions.ktlint.get()

// In the root project, over the sources of all modules, as the style is the same and the modules need nothing else from it.
spotless {
    kotlin {
        target("*/src/**/*.kt")
        ktlint(ktlintVersion)
    }
    kotlinGradle {
        target("*.gradle.kts", "*/*.gradle.kts")
        ktlint(ktlintVersion)
    }
}

tasks.register("lint") {
    group = "verification"
    description = "Checks code style with ktlint."
    dependsOn("spotlessCheck")
}

tasks.register("format") {
    group = "formatting"
    description = "Formats code with ktlint."
    dependsOn("spotlessApply")
}

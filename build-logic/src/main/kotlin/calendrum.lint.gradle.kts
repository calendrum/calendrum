// ktlint (code style from .editorconfig) through Spotless. `check` runs `spotlessCheck`. Entry points: `./gradlew lint`
// and `./gradlew format`.

plugins {
    id("com.diffplug.spotless")
}

val ktlintVersion = the<VersionCatalogsExtension>().named("libs").findVersion("ktlint").get().requiredVersion

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint(ktlintVersion)
    }
    kotlinGradle {
        target("*.gradle.kts")
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

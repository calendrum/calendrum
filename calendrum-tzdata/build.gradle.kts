import io.github.calendrum.buildlogic.tz.TzDataSources

plugins {
    id("calendrum.kmp-library")
}

val generateTzData = tasks.register<TzDataSources>("generateTzData") {
    description = "Compiles every zone of the pinned tzdb into the packed tables of calendrum-tzdata."
    tzdbDirectory.set(layout.settingsDirectory.dir("third_party/tzdb"))
    outputDirectory.set(layout.buildDirectory.dir("generated/calendrum/tzdata/kotlin"))
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("io.github.calendrum.InternalICalApi")
    }
    sourceSets.commonMain {
        kotlin.srcDir(generateTzData)
        dependencies {
            api(project(":calendrum-core"))
        }
    }
}

val tzdb = layout.settingsDirectory.dir("third_party/tzdb")

tasks.withType<Test>().matching { it.name.startsWith("jvmTest") }.configureEach {
    systemProperty("calendrum.tzdb", tzdb.asFile.absolutePath)
    inputs.dir(tzdb).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("tzdb")
}

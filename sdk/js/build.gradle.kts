plugins {
    kotlin("multiplatform")
}

val calendrumVersion = providers.fileContents(layout.projectDirectory.file("../../gradle.properties")).asText
    .map { text -> text.lines().first { it.startsWith("VERSION_NAME=") }.substringAfter('=') }

kotlin {
    js {
        outputModuleName = "calendrum"
        nodejs()
        useEsModules()
        binaries.library()
        generateTypeScriptDefinitions()
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
        jsMain.dependencies {
            implementation(calendrumVersion.map { "io.github.calendrum:calendrum-core:$it" })
            implementation(calendrumVersion.map { "io.github.calendrum:calendrum-jcal:$it" })
            implementation(calendrumVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
        }
        jsTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

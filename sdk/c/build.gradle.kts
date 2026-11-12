import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform")
}

val calendrumVersion = providers.fileContents(layout.projectDirectory.file("../../gradle.properties")).asText
    .map { text -> text.lines().first { it.startsWith("VERSION_NAME=") }.substringAfter('=') }

// Writes the library version into a Kotlin constant for `ical_version`.
val generateBuildInfo = tasks.register("generateBuildInfo") {
    val version = calendrumVersion
    val output = layout.buildDirectory.dir("generated/buildInfo")
    inputs.property("version", version)
    outputs.dir(output)
    doLast {
        val file = output.get().file("io/github/calendrum/sdk/c/BuildInfo.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package io.github.calendrum.sdk.c\n\ninternal const val LIBRARY_VERSION = \"${version.get()}\"\n",
        )
    }
}

kotlin {
    macosArm64 {
        val main = compilations.getByName("main")

        // A Kotlin/Native executable that calls the facade functions directly, without the C boundary, as the baseline
        // of the benchmarks; it is associated with main to see its internals.
        val bench = compilations.create("bench") {
            associateWith(main)
        }
        binaries.executable("bench", listOf(NativeBuildType.RELEASE)) {
            compilation = bench
            entryPoint = "io.github.calendrum.sdk.c.bench.main"
        }
    }
    linuxX64()
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.sharedLib {
            baseName = "calendrum"
        }
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalNativeApi")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
        nativeMain {
            kotlin.srcDir(generateBuildInfo)
            dependencies {
                implementation(calendrumVersion.map { "io.github.calendrum:calendrum-core:$it" })
                implementation(calendrumVersion.map { "io.github.calendrum:calendrum-jcal:$it" })
                implementation(calendrumVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}

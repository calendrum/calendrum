import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
}

val calendrumVersion = providers.gradleProperty("calendrumVersion").get()
val knownTargets = listOf("jvm", "js", "wasmJs", "linuxX64")

// Targets come from -PcompatTargets so that CI can build linuxX64 (Kotlin/Native) in the native workflow and everything
// else in the compat workflow.
val compatTargets = providers.gradleProperty("compatTargets").get()
    .split(',')
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .toSet()
require(compatTargets.isNotEmpty() && knownTargets.containsAll(compatTargets)) {
    "compatTargets=$compatTargets, expected a non-empty subset of $knownTargets"
}

repositories {
    // Only Calendrum comes from ~/.m2; everything else must come from Maven Central, as it would for a real consumer.
    mavenLocal {
        content {
            includeGroup("io.github.calendrum")
        }
    }
    mavenCentral()
}

// The test compares Calendrum.VERSION with the version run.sh passed in. Generated source works the same on every target;
// system properties would not.
val generateExpectedVersion = tasks.register("generateExpectedVersion") {
    val outputDir = layout.buildDirectory.dir("generated/compat/kotlin")
    inputs.property("calendrumVersion", calendrumVersion)
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get().file("io/github/calendrum/compat/ExpectedVersion.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package io.github.calendrum.compat\n\ninternal const val EXPECTED_CALENDRUM_VERSION: String = \"$calendrumVersion\"\n",
        )
    }
}

kotlin {
    if ("jvm" in compatTargets) {
        jvm()
    }
    if ("js" in compatTargets) {
        js { nodejs() }
    }
    if ("wasmJs" in compatTargets) {
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs { nodejs() }
    }
    if ("linuxX64" in compatTargets) {
        linuxX64()
    }

    sourceSets {
        if ("jvm" in compatTargets) {
            jvmTest.dependencies {
                implementation("io.ktor:ktor-server-test-host:3.0.0")
                implementation("io.ktor:ktor-client-content-negotiation:3.0.0")
            }
        }
        if ("js" in compatTargets) {
            jsTest.dependencies {
                implementation("io.ktor:ktor-server-test-host:3.0.0")
                implementation("io.ktor:ktor-client-content-negotiation:3.0.0")
            }
        }
        commonMain.dependencies {
            implementation("io.github.calendrum:calendrum-core:$calendrumVersion")
            implementation("io.github.calendrum:calendrum-jcal:$calendrumVersion")
            implementation("io.github.calendrum:calendrum-kotlinx-io:$calendrumVersion")
            implementation("io.github.calendrum:calendrum-okio:$calendrumVersion")
            implementation("io.github.calendrum:calendrum-ktor:$calendrumVersion")
        }
        commonTest {
            kotlin.srcDir(generateExpectedVersion)
            dependencies {
                // No version: kotlin-test follows the consumer's Kotlin, as in a real project.
                implementation(kotlin("test"))
            }
        }
    }
}

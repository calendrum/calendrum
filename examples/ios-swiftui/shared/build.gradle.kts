import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
}

// The Calendrum version is VERSION_NAME of the root gradle.properties, resolved from mavenLocal (only the io.github.calendrum
// group comes from there).
val calendrumVersion = providers.fileContents(layout.projectDirectory.file("../../../gradle.properties")).asText
    .map { text -> text.lines().first { it.startsWith("VERSION_NAME=") }.substringAfter('=') }

// region swift-gradle
kotlin {
    // A static framework for iosArm64 and iosSimulatorArm64 in one XCFramework; the binaryTarget of Package.swift
    // points at its release build.
    val xcframework = XCFramework("CalendrumSchedule")
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "CalendrumSchedule"
            isStatic = true
            xcframework.add(this)
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(calendrumVersion.map { "io.github.calendrum:calendrum-core:$it" })
            implementation(calendrumVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
        }
    }
}
// endregion

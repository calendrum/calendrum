import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

val cliVersion = providers.gradleProperty("cliVersion")
val libraryVersion = providers.gradleProperty("libraryVersion")
val casesDirectory = layout.settingsDirectory.dir("../conformance/ops")

val generateVersion = tasks.register("generateVersion") {
    group = "build"
    description = "Writes the version of the command line and of the library into the sources."
    val output = layout.buildDirectory.dir("generated/version/commonMain/kotlin")
    val version = cliVersion
    val library = libraryVersion
    inputs.property("version", version)
    inputs.property("library", library)
    outputs.dir(output)
    doLast {
        val file = output.get().file("io/github/calendrum/tooling/cli/Version.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package io.github.calendrum.tooling.cli\n\n" +
                "internal const val CLI_VERSION = \"${version.get()}\"\n\n" +
                "internal const val LIBRARY_VERSION = \"${library.get()}\"\n",
        )
    }
}

kotlin {
    explicitApi()
    compilerOptions {
        allWarningsAsErrors = true
    }

    jvm()
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.executable(listOf(NativeBuildType.RELEASE)) {
            baseName = "calendrum"
            // The compiler names its static symbols by source paths, which hold the home folder of whoever built this. The linker of
            // the Windows target does not obey this, so the shared script of the archives removes them there.
            if (targetName.startsWith("linux")) {
                linkerOpts("-Wl,-s")
            }
            entryPoint = "io.github.calendrum.tooling.cli.main"
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            kotlin.srcDir(generateVersion)
            dependencies {
                implementation(project(":ops"))
                implementation(project(":platform"))
                implementation(libs.kotlinx.io.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmTest {
            // The check of SchemaTest in :ops, so that what the command line prints meets the same reading of the schemas.
            kotlin.srcDir("../ops/src/schemaCheck/kotlin")
            dependencies {
                implementation(project.dependencies.platform(libs.junit.bom))
                implementation(libs.junit.jupiter)
                runtimeOnly(libs.junit.platform.launcher)
            }
        }
    }
}

// The real binary, run as a process. It runs only on the host it is built for.
val runsNativeBinary = HostManager.hostIsMac && System.getProperty("os.arch") == "aarch64"
val nativeBinary = kotlin.targets.withType<KotlinNativeTarget>().named("macosArm64").map {
    it.binaries.getExecutable(NativeBuildType.RELEASE)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    maxHeapSize = "2g"
    inputs.dir(casesDirectory).withPathSensitivity(PathSensitivity.RELATIVE)
    systemProperty("calendrum.cases", casesDirectory.asFile.canonicalPath)
    systemProperty("calendrum.version", cliVersion.get())
    if (runsNativeBinary) {
        dependsOn(nativeBinary.map { it.linkTaskProvider })
        systemProperty("calendrum.cli.binary", nativeBinary.get().outputFile.absolutePath)
    }
}

// The archives of every target: binary, LICENSE, NOTICE, licenses, README. The same script packs the server.
extra["distArchiveBase"] = cliVersion.map { "calendrum-$it" }
extra["distDescription"] = "the command line"
extra["distReadme"] = layout.projectDirectory.file("dist/README.txt")
extra["distLinkTasks"] = kotlin.targets.withType<KotlinNativeTarget>().associate {
    it.name to it.binaries.getExecutable(NativeBuildType.RELEASE).linkTaskProvider
}
extra["distBinaries"] = kotlin.targets.withType<KotlinNativeTarget>().associate {
    it.name to it.binaries.getExecutable(NativeBuildType.RELEASE).linkTaskProvider.flatMap { link -> link.outputFile }
}
apply(from = "../gradle/distribution.gradle.kts")

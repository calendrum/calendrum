import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

val mcpVersion = providers.gradleProperty("mcpVersion")
val schemaDirectory = layout.settingsDirectory.dir("../conformance/ops/schema")
val guideDirectory = layout.settingsDirectory.dir("../guides")
val casesDirectory = layout.settingsDirectory.dir("../conformance/ops")

// The contract and the server cannot differ: the schemas and the guides are the files of the repository, built in.
val generateEmbeddedFiles = tasks.register("generateEmbeddedFiles") {
    group = "build"
    description = "Embeds the schemas of the contract, the guides and the version into the server."
    val output = layout.buildDirectory.dir("generated/embedded/commonMain/kotlin")
    val version = mcpVersion
    val schemas = schemaDirectory
    val guides = guideDirectory
    inputs.dir(schemas).withPathSensitivity(PathSensitivity.RELATIVE)
    inputs.dir(guides).withPathSensitivity(PathSensitivity.RELATIVE)
    inputs.property("version", version)
    outputs.dir(output)
    doLast {
        // A class file holds a string of at most 65535 bytes, and a character takes up to three of them.
        val chunkSize = 20000
        val literal = { text: String ->
            buildString {
                append('"')
                for (char in text) {
                    when {
                        char == '\\' -> append("\\\\")
                        char == '"' -> append("\\\"")
                        char == '$' -> append("\\$")
                        char == '\n' -> append("\\n")
                        char == '\r' -> append("\\r")
                        char == '\t' -> append("\\t")
                        char.code < 0x20 -> append("\\u").append(char.code.toString(16).padStart(4, '0'))
                        else -> append(char)
                    }
                }
                append('"')
            }
        }
        val function = { name: String, files: List<Pair<String, File>> ->
            buildString {
                append("internal fun $name(name: String): String? = when (name) {\n")
                for ((key, file) in files) {
                    val chunks = file.readText().chunked(chunkSize).joinToString(", ") { literal(it) }
                    append("    ${literal(key)} -> listOf($chunks).joinToString(\"\")\n\n")
                }
                append("    else -> null\n}\n")
            }
        }
        val listed = { directory: Directory, suffix: String ->
            directory.asFile.listFiles().orEmpty()
                .filter { it.name.endsWith(suffix) }
                .sortedBy { it.name }
                .map { it.name.removeSuffix(suffix) to it }
        }
        val guideFiles = listed(guides, ".md")
        val text = buildString {
            append("package io.github.calendrum.tooling.mcp\n\n")
            append("internal const val MCP_VERSION = ${literal(version.get())}\n\n")
            append("internal val GUIDE_NAMES: List<String> = listOf(")
            append(guideFiles.joinToString(", ") { literal(it.first) })
            append(")\n\n")
            append(function("schemaFile", listed(schemas, ".schema.json")))
            append("\n")
            append(function("guideFile", guideFiles))
        }
        val file = output.get().file("io/github/calendrum/tooling/mcp/EmbeddedFiles.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(text)
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
            baseName = "calendrum-mcp"
            // The compiler names its static symbols by source paths, which hold the home folder of whoever built this. The linker of
            // the Windows target does not obey this, so the shared script of the archives removes them there.
            if (targetName.startsWith("linux")) {
                linkerOpts("-Wl,-s")
            }
            entryPoint = "io.github.calendrum.tooling.mcp.main"
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            kotlin.srcDir(generateEmbeddedFiles)
            dependencies {
                implementation(project(":ops"))
                implementation(project(":platform"))
                implementation(libs.mcp.server)
                implementation(libs.kotlin.logging)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.io.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmTest {
            // The check of SchemaTest in :ops, so that the output of the server meets the same reading of the schemas.
            kotlin.srcDir("../ops/src/schemaCheck/kotlin")
            dependencies {
                implementation(project.dependencies.platform(libs.junit.bom))
                implementation(libs.junit.jupiter)
                implementation(libs.jazzer.junit)
                implementation(libs.mcp.client)
                runtimeOnly(libs.junit.platform.launcher)
            }
        }
    }
}

// The real binary under a real client. It runs only on the host it is built for.
val runsNativeBinary = HostManager.hostIsMac && System.getProperty("os.arch") == "aarch64"
val nativeBinary = kotlin.targets.withType<KotlinNativeTarget>().named("macosArm64").map {
    it.binaries.getExecutable(NativeBuildType.RELEASE)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    maxHeapSize = "2g"
    inputs.dir(casesDirectory).withPathSensitivity(PathSensitivity.RELATIVE)
    inputs.dir(guideDirectory).withPathSensitivity(PathSensitivity.RELATIVE)
    jvmArgs("-XX:+EnableDynamicAgentLoading", "-XX:-OmitStackTraceInFastThrow")
    systemProperty("calendrum.cases", casesDirectory.asFile.canonicalPath)
    systemProperty("calendrum.guides", guideDirectory.asFile.canonicalPath)
    systemProperty("jazzer.instrument", "io.github.calendrum.**")
    systemProperty("junit.jupiter.execution.timeout.mode", "disabled")
    systemProperty("junit.jupiter.execution.timeout.testtemplate.method.default", "10 s")
    if (runsNativeBinary) {
        dependsOn(nativeBinary.map { it.linkTaskProvider })
        systemProperty("calendrum.mcp.binary", nativeBinary.get().outputFile.absolutePath)
    }
}

tasks.named<Test>("jvmTest") {
    // Regression mode always, even if the developer exported JAZZER_FUZZ.
    environment("JAZZER_FUZZ", "0")
}

tasks.register<Test>("fuzzMcp") {
    group = "verification"
    description = "Fuzzes the arguments of tools and the checks of paths for -Pfuzz.duration (default 20m)."
    val test = tasks.named<Test>("jvmTest").get()
    testClassesDirs = test.testClassesDirs
    classpath = test.classpath
    filter.includeTestsMatching("io.github.calendrum.tooling.mcp.McpFuzzTest")
    environment("JAZZER_FUZZ", "1")
    val duration = providers.gradleProperty("fuzz.duration").orElse("20m")
    jvmArgumentProviders.add(CommandLineArgumentProvider { listOf("-Djazzer.max_duration=${duration.get()}") })
    testLogging.showStandardStreams = true
    outputs.upToDateWhen { false }
    outputs.cacheIf { false }
}

// The archives of every target: binary, LICENSE, NOTICE, licenses, README. The same script packs the command line.
extra["distArchiveBase"] = mcpVersion.map { "calendrum-mcp-$it" }
extra["distDescription"] = "the server"
extra["distReadme"] = layout.projectDirectory.file("dist/README.txt")
extra["distLinkTasks"] = kotlin.targets.withType<KotlinNativeTarget>().associate {
    it.name to it.binaries.getExecutable(NativeBuildType.RELEASE).linkTaskProvider
}
extra["distBinaries"] = kotlin.targets.withType<KotlinNativeTarget>().associate {
    it.name to it.binaries.getExecutable(NativeBuildType.RELEASE).linkTaskProvider.flatMap { link -> link.outputFile }
}
apply(from = "../gradle/distribution.gradle.kts")

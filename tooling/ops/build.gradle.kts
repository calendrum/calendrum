import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeHostTest

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

val libraryVersion = providers.gradleProperty("libraryVersion")

// The meanings of the problem codes for `explain`, from the table of the public guide, so that the two cannot differ.
val generateProblemCodes = tasks.register("generateProblemCodes") {
    group = "build"
    description = "Generates the table of problem codes from guides/diagnostics.md."
    val guide = layout.settingsDirectory.file("../guides/diagnostics.md")
    val output = layout.buildDirectory.dir("generated/problemCodes/commonMain/kotlin")
    inputs.file(guide)
    outputs.dir(output)
    doLast {
        val row = Regex("""^\| `(ICAL-[A-Z][0-9]{3})` \| (\w+) \| `ProblemCode\.(\w+)` \| (.*) \|$""")
        // The guide is Markdown and a response is plain text: a link becomes its own text, and backticks go.
        val link = Regex("""\[([^\]]*)]\([^)]*\)""")
        val rows = guide.asFile.readLines().mapNotNull { row.find(it)?.groupValues }.map { values ->
            values.map { it.replace(link, "$1").replace("`", "") }
        }
        check(rows.size > 100) { "guides/diagnostics.md has only ${rows.size} problem codes: did its tables change?" }
        val text = StringBuilder("package io.github.calendrum.tooling.ops\n\n")
        text.append("internal val PROBLEM_CODES: List<List<String>> = listOf(\n")
        for (values in rows) {
            text.append("    listOf(")
            text.append(
                values.drop(1).joinToString { value ->
                    '"' + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("$", "\\$") + '"'
                },
            )
            text.append("),\n")
        }
        text.append(")\n")
        val file = output.get().file("io/github/calendrum/tooling/ops/ProblemCodes.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(text.toString())
    }
}

// Where the tests find the cases: an absolute path in a generated constant works the same on the JVM, Native and Node.
val generateCasesPath = tasks.register("generateCasesPath") {
    group = "build"
    description = "Generates the path of conformance/ops for the tests."
    val cases = layout.settingsDirectory.dir("../conformance/ops").asFile.canonicalPath
    val output = layout.buildDirectory.dir("generated/casesPath/commonTest/kotlin")
    inputs.property("cases", cases)
    outputs.dir(output)
    doLast {
        val file = output.get().file("io/github/calendrum/tooling/ops/CasesPath.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            "package io.github.calendrum.tooling.ops\n\ninternal const val CASES_PATH = \"$cases\"\n",
        )
    }
}

kotlin {
    explicitApi()
    compilerOptions {
        allWarningsAsErrors = true
    }

    jvm()
    js {
        nodejs()
    }
    macosArm64 {
        val main = compilations.getByName("main")

        // The measurement of a response in the release mode, as the C facade spike did it.
        val bench = compilations.create("bench") {
            associateWith(main)
        }
        binaries.executable("bench", listOf(NativeBuildType.RELEASE)) {
            compilation = bench
            entryPoint = "io.github.calendrum.tooling.ops.bench.main"
        }
    }
    linuxX64()
    linuxArm64()
    mingwX64()

    sourceSets {
        commonMain {
            kotlin.srcDir(generateProblemCodes)
            dependencies {
                implementation(libraryVersion.map { "io.github.calendrum:calendrum-core:$it" })
                implementation(libraryVersion.map { "io.github.calendrum:calendrum-jcal:$it" })
                implementation(libraryVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
            }
        }
        commonTest {
            kotlin.srcDir(generateCasesPath)
            kotlin.srcDir("src/schemaCheck/kotlin")
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.io.core)
            }
        }
        jvmTest.dependencies {
            implementation(project.dependencies.platform(libs.junit.bom))
            implementation(libs.junit.jupiter)
            implementation(libs.jazzer.junit)
            runtimeOnly(libs.junit.platform.launcher)
        }
    }
}

val casesDirectory = layout.settingsDirectory.dir("../conformance/ops")

tasks.withType<KotlinJsTest>().configureEach {
    inputs.dir(casesDirectory).withPathSensitivity(PathSensitivity.RELATIVE)
    useMocha { timeout = "120s" }
}

tasks.withType<KotlinNativeHostTest>().configureEach {
    inputs.dir(casesDirectory).withPathSensitivity(PathSensitivity.RELATIVE)
}

// Jazzer instruments only class directories on the classpath unless told otherwise, and the library arrives as jars. The
// JUnit default timeout of a test template is what Jazzer turns into the libFuzzer -timeout.
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    maxHeapSize = "2g"
    inputs.dir(casesDirectory).withPathSensitivity(PathSensitivity.RELATIVE)
    jvmArgs("-XX:+EnableDynamicAgentLoading", "-XX:-OmitStackTraceInFastThrow")
    systemProperty("jazzer.instrument", "io.github.calendrum.**")
    systemProperty("junit.jupiter.execution.timeout.mode", "disabled")
    systemProperty("junit.jupiter.execution.timeout.testtemplate.method.default", "10 s")
}

tasks.register<Test>("fuzzOps") {
    group = "verification"
    description = "Fuzzes the operations for -Pfuzz.duration (default 20m)."
    val test = tasks.named<Test>("jvmTest").get()
    testClassesDirs = test.testClassesDirs
    classpath = test.classpath
    filter.includeTestsMatching("io.github.calendrum.tooling.ops.OpsFuzzTest")
    environment("JAZZER_FUZZ", "1")
    val duration = providers.gradleProperty("fuzz.duration").orElse("20m")
    jvmArgumentProviders.add(CommandLineArgumentProvider { listOf("-Djazzer.max_duration=${duration.get()}") })
    testLogging.showStandardStreams = true
    outputs.upToDateWhen { false }
    outputs.cacheIf { false }
}

tasks.named<Test>("jvmTest") {
    // Regression mode always, even if the developer exported JAZZER_FUZZ.
    environment("JAZZER_FUZZ", "0")
    // The update of the expected responses, reviewed by hand before they stay.
    systemProperty("calendrum.ops.update", providers.gradleProperty("ops.update").getOrElse("false"))
}

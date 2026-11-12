import io.github.calendrum.buildlogic.ConformanceSources
import io.github.calendrum.buildlogic.TargetSet
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

// `kotlin("multiplatform")` directly, not `calendrum.kmp-library`: this module is never published and has no ABI dump, but
// still runs on every platform the host builds tests for, like `calendrum-core`, unlike `benchmarks`, `fuzz` and `tools`,
// which are JVM- or host-only.
plugins {
    kotlin("multiplatform")
    id("calendrum.lint")
}

val targetSet = TargetSet.of(providers)

val generateConformanceSources = tasks.register<ConformanceSources>("generateConformanceSources") {
    group = "verification"
    description = "Embeds the conformance cases and schemas into Kotlin test sources."
    casesDirectory = layout.projectDirectory.dir("ical")
    schemaDirectory = layout.projectDirectory.dir("schema")
    packageName = "io.github.calendrum.conformance"
    outputDirectory = layout.buildDirectory.dir("generated/conformance/commonTest/kotlin")
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }

    jvm()
    if (targetSet.declaredIn("web")) {
        js { nodejs() }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs { nodejs() }
        @OptIn(ExperimentalWasmDsl::class)
        wasmWasi { nodejs() }
    }
    if (targetSet.declaredIn("tier1", "apple")) {
        macosArm64()
        iosSimulatorArm64()
    }
    if (targetSet.declaredIn("tier1")) {
        linuxX64()
    }
    if (targetSet.declaredIn("windows")) {
        mingwX64()
    }

    sourceSets {
        commonTest {
            kotlin.srcDir(generateConformanceSources)
            dependencies {
                implementation(project(":calendrum-core"))
                implementation(project(":calendrum-jcal"))
                implementation(project(":calendrum-kotlinx-io"))
                implementation(project(":calendrum-okio"))
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.withType<KotlinJsTest>().matching { !it.name.contains("wasm") }.configureEach {
    useMocha { timeout = "60s" }
}

val corpus = layout.settingsDirectory.dir("corpus")

// The corpus directory reaches `RealCorpusTest` as the `calendrum.corpus` system property and is an input of the task, so
// a changed corpus file reruns it. corpus/real/ is the maintainer's and is not in the repository; the test also looks for
// pieces of it in the data that is, so those directories are inputs too.
tasks.named<Test>("jvmTest") {
    systemProperty("calendrum.corpus", corpus.asFile.absolutePath)
    inputs.files(fileTree(corpus)).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("corpus")
    inputs.files(
        layout.settingsDirectory.files(
            "fuzz/src/test/resources",
            "benchmarks/src/jvmTest/resources",
            "calendrum-core/src/commonTest",
            "tools/samples",
        ).asFileTree,
    ).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("publicData")
}

// JVM only: it calls into the parser directly and writes the case files as a side effect, which only makes sense once,
// from one platform.
tasks.register<JavaExec>("updateExpected") {
    group = "verification"
    description =
        "Rewrites the expected files of every conformance case and of the real corpus from the current parser."
    val compilation = kotlin.jvm().compilations.getByName("test")
    classpath(compilation.output.allOutputs, compilation.runtimeDependencyFiles)
    mainClass.set("io.github.calendrum.conformance.UpdateExpectedKt")
    args(layout.projectDirectory.dir("ical").asFile.absolutePath, corpus.asFile.absolutePath)
    dependsOn("jvmTestClasses")
}

// The unpublished benchmarks module: its own targets, kotlinx-benchmark profiles quick and full, JMH with `-prof gc`,
// the environment record and the summary.

import io.github.calendrum.buildlogic.BenchmarkEnvironment
import io.github.calendrum.buildlogic.BenchmarkProfile
import io.github.calendrum.buildlogic.BenchmarkRunLock
import io.github.calendrum.buildlogic.BenchmarkSummary
import io.github.calendrum.buildlogic.TargetSet
import kotlinx.benchmark.gradle.JsBenchmarkTarget
import kotlinx.benchmark.gradle.JsBenchmarksExecutor
import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec
import org.jetbrains.kotlin.konan.target.HostManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("multiplatform")
    kotlin("plugin.allopen")
    id("org.jetbrains.kotlinx.benchmark")
    id("calendrum.lint")
}

val suitePackage = "io.github.calendrum.benchmarks."
val corpusSuites = listOf(
    "corpusScan",
    "reader",
    "baseline",
    "charset",
    "parse",
    "cursor",
    "write",
    "writer",
    "stream",
    "lexerTree",
    "fingerprints",
    "values",
    "parseTyped",
    "patch",
    "selective",
    "schema",
    "occurrences",
    "validate",
    "fixups",
    "feedPublisher",
    "adapters",
)
val competitorSuites = listOf("baseline", "schema", "rrule", "occurrences", "tz")
val competitors = listOf("ical4j", "biweekly")

val targetSet = TargetSet.of(providers)

val catalog = the<VersionCatalogsExtension>().named("libs")

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }

    jvm()
    if (targetSet.declaredIn("web")) {
        js { nodejs() }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs { nodejs() }
    }
    if (targetSet.declaredIn("tier1", "apple")) {
        macosArm64()
    }
    if (targetSet.declaredIn("tier1")) {
        linuxX64()
    }
    if (targetSet.declaredIn("windows")) {
        mingwX64()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(catalog.findLibrary("kotlinx-benchmark-runtime").get())
        }
    }
}

kotlin.targets.withType<KotlinNativeTarget>().configureEach {
    // A release executable next to the kotlinx-benchmark one: the launcher starts it once per measurement. The entry
    // point is in a package, so it does not clash with the generated one.
    binaries.executable("coldStart", listOf(NativeBuildType.RELEASE)) {
        entryPoint = "io.github.calendrum.benchmarks.coldStart.main"
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

// Only the Native target of the current host is registered with kotlinx-benchmark; the other Native targets only
// compile.
val hostNativeTargets = kotlin.targets.withType<KotlinNativeTarget>()
    .filter { it.konanTarget == HostManager.host }
    .map { it.name }
val benchmarkInclude = providers.gradleProperty("bench.include").orNull
val benchmarkCorpora = providers.gradleProperty("bench.corpus").orNull
    ?.split(',')
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    .orEmpty()

// Whether a run will touch a suite that needs corpora or competitors is only knowable from the same include-regex
// matching that kotlinx-benchmark and JMH already use: for each suite name, either the raw pattern string contains that
// name (cheap, catches package prefixes like `baselineParse` when the filter is written as `baseline\w*`) or the regex
// matches a representative fully qualified benchmark name `io.github.calendrum.benchmarks.<suite>.`, built by appending a
// literal dot. A filter that only names the exact suite (`baseline` alone, without a `.` or `\w*` after it) fails this
// second check on `baselineParse` and the others, which is why `-Pbench.include` values use `baseline\w*`, not
// `baseline`. No filter means all suites run, so it defaults to true; an invalid regex also defaults to true rather
// than silently skipping corpus generation.
fun selects(suites: List<String>): Boolean {
    val pattern = benchmarkInclude ?: return true
    val regex = runCatching { Regex(pattern) }.getOrNull() ?: return true
    return suites.any { suite -> suite in pattern || regex.containsMatchIn("$suitePackage$suite") }
}

val usesCorpora = selects(corpusSuites)

// corpus/real/ stays with the maintainer (.gitignore): without it the benchmarks of real files (`Real*Benchmark`, JVM)
// are left out instead of failing in their setup.
val hasRealCorpus = layout.settingsDirectory.dir("corpus/real").asFile.isDirectory
val realBenchmarks = """\.Real\w*Benchmark\."""
val usesCompetitors = selects(competitorSuites)
val forkJvmArguments = listOf(
    "-Xms2g",
    "-Xmx2g",
    "-XX:+UseG1GC",
    "-Duser.timezone=UTC",
    "-Duser.language=en",
    "-Duser.country=US",
    "-Dfile.encoding=UTF-8",
)

fun jmhOptions(profile: BenchmarkProfile): List<String> = listOf(
    "-wi", "${profile.warmups}",
    "-w", "${profile.iterationSeconds}s",
    "-i", "${profile.iterations}",
    "-r", "${profile.iterationSeconds}s",
    "-f", "${profile.jvmForks}",
    "-prof", "gc",
    "-foe", "true",
) + if (benchmarkCorpora.isEmpty()) emptyList() else listOf("-p", "corpus=${benchmarkCorpora.joinToString(",")}")

benchmark {
    // The default profile would run JMH with its own defaults (5 forks, 10 s iterations).
    configurations.removeIf { it.name == "main" }
    BenchmarkProfile.all.forEach { profile ->
        configurations.register(profile.name) {
            warmups = profile.warmups

            // Native, JS and Wasm have no forks, so they run forks x iterations measurements. The JVM task ignores
            // these values and gets JMH options instead.
            iterations = profile.nonJvmIterations
            iterationTime = profile.iterationSeconds.toLong()
            iterationTimeUnit = "s"
            reportFormat = "json"
            benchmarkInclude?.let { include(it) }
            if (!hasRealCorpus) exclude(realBenchmarks)
            if (benchmarkCorpora.isNotEmpty()) param("corpus", *benchmarkCorpora.toTypedArray())
        }
    }
    targets {
        kotlin.targets.names.filter { it == "jvm" || it == "js" || it == "wasmJs" }.forEach { name ->
            register(name) {
                if (this is JsBenchmarkTarget) {
                    // Like the Wasm runner, the built-in executor honours warm-ups and iterations of the profile;
                    // benchmark.js interprets them differently.
                    jsBenchmarksExecutor = JsBenchmarksExecutor.BuiltIn
                }
            }
        }
        hostNativeTargets.forEach { register(it) }
    }
}

val reportTime: LocalDateTime = LocalDateTime.now()

// kotlinx-benchmark names the run directory after this time (ISO date-time with ':' replaced by '.'); setting it here
// lets environment.json land next to its reports.
extensions.extraProperties["reportTime"] = reportTime
val runDirectoryName = reportTime.format(DateTimeFormatter.ISO_DATE_TIME).replace(":", ".")
val reportsRoot = layout.buildDirectory.dir("reports/benchmarks")

// With the configuration cache Gradle runs tasks of one project in parallel; one slot keeps platforms from being
// measured at the same time.
val runLock = gradle.sharedServices.registerIfAbsent("benchmarkRunLock", BenchmarkRunLock::class) {
    maxParallelUsages = 1
}

tasks.withType<NodeJsExec>().configureEach {
    environment("TZ", "UTC")

    // `parse.ParseBenchmark.treeReadAll` on `huge` (a 100 MB tree plus every decoded value) crashes Node's default
    // old-space limit (`JavaScript heap out of memory`, exit code 134). Every JS and Wasm benchmark run gets the larger
    // heap, not just this one suite, since `NodeJsExec` has no per-task include filter to target it more narrowly.
    nodeArgs = (nodeArgs + "--max-old-space-size=4096").toMutableList()
}

// Mocha's default 2 s timeout is too short for the `university` case of `CorpusStructuralTest` on Node. The `wasmJs`
// test tasks are also `KotlinJsTest` instances but run their own `KotlinWasmNode` framework, not Mocha: configuring
// `useMocha` on them only prints a harmless but noisy warning, hence the name filter. The `personal` cases still take
// up to a second and a half each on Node, so the margin stays.
tasks.withType<KotlinJsTest>().matching { !it.name.contains("wasmJs") }.configureEach {
    useMocha { timeout = "60s" }
}

afterEvaluate {
    val javaLauncher = the<JavaToolchainService>().launcherFor(the<JavaPluginExtension>().toolchain)
    val jmhVersion = benchmark.targets.withType<JvmBenchmarkTarget>().single().jmhVersion
    val nodeVersions = buildMap {
        extensions.findByType<NodeJsEnvSpec>()?.let { put("js", it.version) }
        extensions.findByType<WasmNodeJsEnvSpec>()?.let { put("wasmJs", it.version) }
    }

    BenchmarkProfile.all.forEach { profile ->
        val runDirectory = reportsRoot.map { it.dir("${profile.name}/$runDirectoryName") }
        fun isProfileTask(name: String) = name == "${profile.name}Benchmark" || name.endsWith(profile.taskSuffix)

        val environment = tasks.register<BenchmarkEnvironment>("${profile.name}BenchmarkEnvironment") {
            group = "benchmark"
            description = "Clears the run directory of the ${profile.name} profile and writes environment.json."
            this.profile = profile.name
            this.runDirectory = runDirectory
            include = benchmarkInclude
            tools.put("kotlin", getKotlinPluginVersion())
            tools.put("kotlinxBenchmark", catalog.findVersion("kotlinx-benchmark").get().requiredVersion)
            tools.put("jmh", jmhVersion)
            jmhArguments = jmhOptions(profile)
            jmhJvmArguments = forkJvmArguments
            nonJvmSettings.put("warmups", profile.warmups)
            nonJvmSettings.put("iterations", profile.nonJvmIterations)
            nonJvmSettings.put("iterationTime", "${profile.iterationSeconds} s")
            nodeVersions.forEach { (target, version) -> this.nodeVersions.put(target, version) }
            nativeTargets = hostNativeTargets
            this.javaLauncher = javaLauncher

            // Only depend on `generateCorpora`, and only point `BenchmarkEnvironment` at its output, when this module
            // actually defines that task and the run will use it; a future benchmarks-only module without a corpus
            // generator must not break here.
            if (usesCorpora) {
                tasks.findByName("generateCorpora")?.let { generateCorpora ->
                    dependsOn(generateCorpora)
                    corporaDirectory.set(layout.buildDirectory.dir("corpora"))
                }
            }
            if (usesCompetitors) {
                competitors.forEach { name ->
                    catalog.findVersion(name).ifPresent { competitorVersions.put(name, it.requiredVersion) }
                }
            }
            competitorSettingsDirectory.set(layout.projectDirectory.dir("src/jvmMain/resources"))
        }

        val summary = tasks.register<BenchmarkSummary>("${profile.name}BenchmarkSummary") {
            group = "benchmark"
            description = "Summarizes the latest run of the ${profile.name} profile into summary.md."
            profileReports = reportsRoot.map { it.dir(profile.name) }
            summaryFile = reportsRoot.map { it.file("summary.md") }
            mustRunAfter(tasks.named { isProfileTask(it) })
        }

        tasks.named { isProfileTask(it) }.configureEach {
            dependsOn(environment)
            finalizedBy(summary)

            // Measure only after every benchmark binary is built, so compilation does not run next to a measurement.
            mustRunAfter("assembleBenchmarks")
            usesService(runLock)
        }

        // kotlinx-benchmark 0.5.0 cannot enable JMH profilers, so the JVM task runs org.openjdk.jmh.Main from the same
        // classpath with `-prof gc`. Forks get the same heap, GC, time zone and locale.
        tasks.named<JavaExec>("jvm${profile.taskSuffix}") {
            mainClass = "org.openjdk.jmh.Main"
            jvmArgs(forkJvmArguments.filter { it.startsWith("-D") })
            environment("TZ", "UTC")
            val reportFile = runDirectory.get().file("jvm.json").asFile.absolutePath
            setArgs(
                listOfNotNull(benchmarkInclude) + (if (hasRealCorpus) emptyList() else listOf("-e", realBenchmarks)) +
                    jmhOptions(profile) +
                    listOf("-rf", "json", "-rff", reportFile, "-jvmArgs", forkJvmArguments.joinToString(" ")),
            )
        }
    }
    val coldStartExecutable = kotlin.targets.withType<KotlinNativeTarget>()
        .firstOrNull { it.konanTarget == HostManager.host }
        ?.binaries
        ?.getExecutable("coldStart", NativeBuildType.RELEASE)
    tasks.register<JavaExec>("coldStartBenchmark") {
        group = "benchmark"
        description =
            "Measures the first parse of the invite in a new process on the JVM and on the host Native target."
        val compilation = kotlin.jvm().compilations.getByName("main")
        classpath(compilation.output.allOutputs, compilation.runtimeDependencyFiles)
        mainClass = "io.github.calendrum.benchmarks.coldStart.ColdStartRunnerKt"
        environment("TZ", "UTC")
        dependsOn("jvmMainClasses")
        usesService(runLock)
        coldStartExecutable?.let { dependsOn(it.linkTaskProvider) }
        val rounds = providers.gradleProperty("coldstart.rounds").orElse("30").get()
        val directory = reportsRoot.get().dir("cold-start/$runDirectoryName").asFile.absolutePath
        setArgs(
            listOf("--directory", directory, "--rounds", rounds) +
                (coldStartExecutable?.let { listOf("--native", it.outputFile.absolutePath) } ?: emptyList()),
        )
    }
}

package io.github.calendrum.buildlogic

import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Locale
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10

// Turns the latest run of a profile into summary.md in the history format of the benchmark documentation. The LAX
// parser accepts the bare NaN that kotlinx-benchmark writes; date checks are off so startedAt stays a string. The suite
// is the package after io.github.calendrum.benchmarks, camelCase turned into kebab-case.
abstract class BenchmarkSummary : DefaultTask() {
    @get:Internal
    abstract val profileReports: DirectoryProperty

    @get:Internal
    abstract val summaryFile: RegularFileProperty

    @TaskAction
    fun summarize() {
        val reports = profileReports.get().asFile
        val run = reports.listFiles().orEmpty()
            .filter { it.resolve(BenchmarkEnvironment.ENVIRONMENT_FILE).isFile }
            .maxByOrNull { it.name }
            ?: throw GradleException("No benchmark run with ${BenchmarkEnvironment.ENVIRONMENT_FILE} in $reports")
        val environment = parse(run.resolve(BenchmarkEnvironment.ENVIRONMENT_FILE)).asMap()
        val corpusSizes = environment["corpora"].asMap()["files"].asList()
            .associate { file -> file.asMap()["name"].toString().removeSuffix(".ics") to file.asMap()["sizeBytes"] }
        val rows = run.listFiles().orEmpty()
            .filter { it.extension == "json" && it.name != BenchmarkEnvironment.ENVIRONMENT_FILE }
            .flatMap { file -> parse(file).asList().map { row(file.nameWithoutExtension, it.asMap(), corpusSizes) } }
            .sortedWith(compareBy<Row> { it.suite }.thenBy { it.benchmark }.thenBy { platformOrder(it.platform) })
        val summary = buildString {
            header(environment)
            appendLine()
            appendLine("| Suite | Benchmark | Platform | Mode | Score ± error | Unit | Bytes/op (JVM) | Input size |")
            appendLine("|---|---|---|---|---:|---|---:|---:|")
            rows.forEach { row ->
                appendLine(
                    "| ${row.suite} | ${row.benchmark} | ${row.platform} | ${row.mode} | ${row.score} | " +
                        "${row.unit} | ${row.bytesPerOperation} | ${row.inputSizeBytes} |",
                )
            }
            if (rows.isEmpty()) appendLine("| — | no results | — | — | — | — | — | — |")
        }
        val output = summaryFile.get().asFile
        output.parentFile.mkdirs()
        output.writeText(summary)
        logger.lifecycle("Benchmark summary (${rows.size} results from ${run.name}): ${output.absolutePath}")
    }

    private fun StringBuilder.header(environment: Map<*, *>) {
        val started = environment["startedAt"]?.toString().orEmpty()
        val date = started.replace('T', ' ').removeSuffix("Z").substringBeforeLast(':') + " UTC"
        appendLine("### $date · `${environment["profile"]}` · commit `${environment["commit"]}`")
        appendLine()
        appendLine("- **Machine:** ${machine(environment["machine"].asMap())}; ${cpu(environment["cpu"].asMap())}")
        appendLine("- **OS:** ${os(environment["os"].asMap())}")
        val jdk = environment["jdk"].asMap()
        val jmh = environment["jmh"].asMap()
        appendLine(
            "- **JVM:** ${jdk["vendor"]} ${jdk["version"]}; JMH ${jmh["version"]} " +
                "`${jmh["arguments"].asList().joinToString(" ")}`; " +
                "fork flags `${jmh["jvmArguments"].asList().joinToString(" ")}`",
        )
        val nonJvm = environment["nonJvm"].asMap()
        val node = environment["node"].asMap().entries
            .sortedBy { platformOrder(it.key.toString()) }
            .joinToString { "${it.value} (${it.key})" }
            .ifEmpty { "—" }
        val native = environment["native"].asMap()["targets"].asList().joinToString().ifEmpty { "—" }
        appendLine(
            "- **Kotlin/Native, JS, Wasm:** warm-up ${nonJvm["warmups"]} × ${nonJvm["iterationTime"]}, " +
                "${nonJvm["iterations"]} × ${nonJvm["iterationTime"]}; Node.js $node; Kotlin/Native $native",
        )
        appendLine(
            "- **Versions:** Kotlin ${environment["kotlin"]}, kotlinx-benchmark ${environment["kotlinxBenchmark"]}; " +
                "corpora: ${corpora(environment["corpora"].asMap())}; " +
                "competitors: ${competitors(environment["competitors"].asMap())}",
        )
        appendLine("- **Filter:** ${environment["include"]?.let { "`$it`" } ?: "all suites"}")
    }

    private fun machine(machine: Map<*, *>): String = when (machine["type"]) {
        "github-actions" -> "GitHub Actions (${machine["runnerEnvironment"]}, ${machine["image"]})"
        else -> machine["type"].toString()
    }

    private fun cpu(cpu: Map<*, *>): String = buildList {
        add(cpu["model"].toString())
        val split = listOfNotNull(cpu["performanceCores"], cpu["efficiencyCores"])
        val splitText = if (split.size == 2) " (${split[0]}P + ${split[1]}E)" else ""
        add("${cpu["physicalCores"]} cores$splitText, ${cpu["logicalCores"]} threads")
        cpu["maxFrequencyMHz"]?.takeIf { it != UNKNOWN }?.let { add("max $it MHz") }
        cpu["currentFrequencyMHz"]?.let { add("current $it MHz") }
        add("turbo boost ${cpu["turboBoost"]}")
    }.joinToString(", ")

    private fun os(os: Map<*, *>): String = buildString {
        append("${os["distribution"] ?: os["name"]} ${os["version"]} ${os["arch"]}")
        append(", kernel ${os["kernel"]}")
    }

    private fun corpora(corpora: Map<*, *>): String {
        val files = corpora["files"].asList()
        if (files.isEmpty()) return "none"
        return "generator ${corpora["generator"]}, seed ${corpora["seed"]}, ${files.size} files"
    }

    private fun competitors(competitors: Map<*, *>): String = competitors.entries
        .filter { it.value != null }
        .joinToString { "${it.key} ${it.value.asMap()["version"]}" }
        .ifEmpty { "none" }

    private fun row(platform: String, result: Map<*, *>, corpusSizes: Map<String, Any?>): Row {
        val name = result["benchmark"].toString()
        val paramsMap = result["params"].asMap()
        val params = paramsMap.entries.joinToString { "${it.key}=${it.value}" }
        val primary = result["primaryMetric"].asMap()
        val allocation = result["secondaryMetrics"].asMap()["gc.alloc.rate.norm"].asMap()
        val inputSize = corpusSizes[paramsMap["corpus"]?.toString()]
        return Row(
            suite = suite(name),
            benchmark = name.split('.').takeLast(2).joinToString(".") + if (params.isEmpty()) "" else " ($params)",
            platform = platform,
            mode = result["mode"].toString(),
            score = measurement(primary),
            unit = primary["scoreUnit"].toString().replace("/sec", "/s"),
            bytesPerOperation = if (allocation.isEmpty()) "—" else measurement(allocation),
            inputSizeBytes = inputSize?.toString() ?: "—",
        )
    }

    private fun suite(name: String): String {
        val segment = name.removePrefix(SUITE_PACKAGE).takeIf { it != name }?.substringBefore('.') ?: return "—"
        return segment.replace(Regex("([a-z0-9])([A-Z])"), "$1-$2").lowercase(Locale.ROOT)
    }

    private fun measurement(metric: Map<*, *>): String {
        val score = number(metric["score"])
        val error = number(metric["scoreError"])
        if (score.isNaN()) return "n/a"
        val reference = if (error.isFinite() && error > 0) error else abs(score)
        val decimals = if (reference > 0) (2 - floor(log10(reference)).toInt()).coerceIn(0, 3) else 0
        val scoreText = String.format(Locale.ROOT, "%.${decimals}f", score)
        val errorText = if (error.isFinite()) String.format(Locale.ROOT, "%.${decimals}f", error) else "n/a"
        return "$scoreText ± $errorText"
    }

    private fun number(value: Any?): Double = when (value) {
        is Number -> value.toDouble()
        else -> value?.toString()?.toDoubleOrNull() ?: Double.NaN
    }

    private fun platformOrder(platform: String): String = when (platform) {
        "jvm" -> "0"
        "js" -> "1"
        "wasmJs" -> "2"
        else -> "3$platform"
    }

    private fun parse(file: File): Any? = JsonSlurper().setType(JsonParserType.LAX).setCheckDates(false).parse(file)

    private fun Any?.asMap(): Map<*, *> = this as? Map<*, *> ?: emptyMap<Any, Any>()

    private fun Any?.asList(): List<*> = this as? List<*> ?: emptyList<Any>()

    private class Row(
        val suite: String,
        val benchmark: String,
        val platform: String,
        val mode: String,
        val score: String,
        val unit: String,
        val bytesPerOperation: String,
        val inputSizeBytes: String,
    )

    private companion object {
        const val SUITE_PACKAGE = "io.github.calendrum.benchmarks."
        const val UNKNOWN = "unknown"
    }
}

package io.github.calendrum.benchmarks.coldStart

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import java.io.File
import java.time.Instant
import java.util.concurrent.TimeUnit

internal class ColdStartSamples(val platform: String, val variant: String) {
    val firstParse: MutableList<Long> = ArrayList()
    val wall: MutableList<Long> = ArrayList()
}

internal class ColdStartSummary(val min: Double, val q1: Double, val median: Double, val q3: Double, val max: Double)

internal fun summarize(nanos: List<Long>): ColdStartSummary {
    require(nanos.isNotEmpty()) { "No samples" }
    val sorted = nanos.sorted().map { it / NANOS_PER_MILLI }
    return ColdStartSummary(
        sorted.first(),
        quantile(sorted, 0.25),
        quantile(sorted, 0.5),
        quantile(sorted, 0.75),
        sorted.last(),
    )
}

private fun quantile(sorted: List<Double>, fraction: Double): Double {
    val position = (sorted.size - 1) * fraction
    val lower = position.toInt()
    val upper = minOf(lower + 1, sorted.size - 1)
    return sorted[lower] + (sorted[upper] - sorted[lower]) * (position - lower)
}

internal class ColdStartLauncher(
    private val platform: String,
    private val command: List<String>,
    val variants: List<String>,
    private val input: File,
) {
    fun run(variant: String): Pair<Long, Long> {
        val builder = ProcessBuilder(command + listOf(variant, input.absolutePath))
        builder.environment()["TZ"] = "UTC"
        builder.redirectError(ProcessBuilder.Redirect.INHERIT)
        val started = System.nanoTime()
        val process = builder.start()
        val output = process.inputStream.readBytes().decodeToString().trim()
        check(process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS)) { "$platform $variant: timed out" }
        val wall = System.nanoTime() - started
        check(process.exitValue() == 0) { "$platform $variant: exit code ${process.exitValue()}" }
        val firstParse =
            output.lines().last().toLongOrNull() ?: error("$platform $variant: unexpected output '$output'")
        return firstParse to wall
    }

    val name: String get() = platform
}

internal fun jvmLauncher(input: File, variants: List<String>): ColdStartLauncher {
    val java = File(System.getProperty("java.home"), "bin/java").absolutePath
    val command = listOf(java) + JVM_FLAGS + listOf(
        "-cp",
        System.getProperty("java.class.path"),
        "io.github.calendrum.benchmarks.coldStart.ColdStartChildKt",
    )
    return ColdStartLauncher("jvm", command, variants, input)
}

internal fun nativeLauncher(executable: File, input: File): ColdStartLauncher {
    require(executable.isFile) { "No Native executable at $executable" }
    return ColdStartLauncher("native", listOf(executable.absolutePath), ColdStartVariant.ours, input)
}

// Variants start in a different order each round, so the position in the round does not always fall on the same
// variant; the first rounds are dropped (file cache, dynamic linker).
internal fun measure(
    launchers: List<ColdStartLauncher>,
    rounds: Int,
    discard: Int,
    progress: (String) -> Unit = {},
): List<ColdStartSamples> {
    val samples = launchers.flatMap { launcher ->
        launcher.variants.map { ColdStartSamples(launcher.name, it) }
    }
    for (round in 0 until rounds + discard) {
        for (launcher in launchers) {
            val order = launcher.variants.indices.map { launcher.variants[(it + round) % launcher.variants.size] }
            for (variant in order) {
                val (firstParse, wall) = launcher.run(variant)
                if (round >= discard) {
                    val target = samples.single { it.platform == launcher.name && it.variant == variant }
                    target.firstParse += firstParse
                    target.wall += wall
                }
            }
        }
        progress("round ${round + 1} of ${rounds + discard}")
    }
    return samples
}

internal fun table(samples: List<ColdStartSamples>): String = buildString {
    appendLine(
        "| Platform | Variant | First parse, ms (median) | Quartiles | Min..max | Process wall, ms (median) | Quartiles | Runs |",
    )
    appendLine("|---|---|---:|---|---|---:|---|---:|")
    for (sample in samples) {
        val first = summarize(sample.firstParse)
        val wall = summarize(sample.wall)
        appendLine(
            "| ${sample.platform} | ${sample.variant} | ${format(
                first.median,
            )} | ${format(first.q1)}…${format(first.q3)} | " +
                "${format(
                    first.min,
                )}…${format(first.max)} | ${format(wall.median)} | ${format(wall.q1)}…${format(wall.q3)} | " +
                "${sample.firstParse.size} |",
        )
    }
}

private fun format(value: Double): String = "%.2f".format(java.util.Locale.ROOT, value)

internal fun json(
    samples: List<ColdStartSamples>,
    rounds: Int,
    discard: Int,
    environment: Map<String, String>,
): String = buildString {
    appendLine("{")
    appendLine("  \"schemaVersion\": 1,")
    appendLine("  \"startedAt\": \"${Instant.now()}\",")
    appendLine("  \"rounds\": $rounds,")
    appendLine("  \"discardedRounds\": $discard,")
    appendLine("  \"environment\": {")
    appendLine(
        environment.entries.joinToString(",\n") {
            "    \"${it.key}\": \"${it.value.replace("\"", "\\\"")}\""
        },
    )
    appendLine("  },")
    appendLine("  \"results\": [")
    appendLine(
        samples.joinToString(",\n") { sample ->
            "    {\"platform\": \"${sample.platform}\", \"variant\": \"${sample.variant}\", " +
                "\"firstParseNanos\": ${sample.firstParse}, \"wallNanos\": ${sample.wall}}"
        },
    )
    appendLine("  ]")
    appendLine("}")
}

private fun command(vararg arguments: String): String = try {
    val process = ProcessBuilder(*arguments).redirectErrorStream(true).start()
    val text = process.inputStream.readBytes().decodeToString().trim()
    process.waitFor()
    text
} catch (e: java.io.IOException) {
    "unknown"
}

// The power state is written before and after the run: a run on battery is not comparable.
private fun environment(): Map<String, String> = linkedMapOf(
    "java" to "${System.getProperty("java.vendor")} ${System.getProperty("java.version")}",
    "jvmFlags" to JVM_FLAGS.joinToString(" "),
    "os" to "${System.getProperty("os.name")} ${System.getProperty("os.version")} ${System.getProperty("os.arch")}",
    "cpu" to command("sysctl", "-n", "machdep.cpu.brand_string"),
    "power" to command("pmset", "-g", "batt").lines().take(2).joinToString(" | "),
)

fun main(args: Array<String>) {
    val options = args.toList().chunked(2).associate { (key, value) -> key to value }
    val directory = File(options["--directory"] ?: error("--directory is required"))
    val rounds = options["--rounds"]?.toInt() ?: DEFAULT_ROUNDS
    val discard = options["--discard"]?.toInt() ?: 1
    val nativeExecutable = options["--native"]?.takeIf { it.isNotEmpty() }?.let(::File)
    directory.mkdirs()
    val input = File(directory, "invite.ics")
    input.writeBytes(corpusBytes(Corpus.INVITE))

    val launchers = buildList {
        add(jvmLauncher(input, ColdStartVariant.ours + ColdStartVariant.rivals))
        nativeExecutable?.let { add(nativeLauncher(it, input)) }
    }
    val powerBefore = environment().getValue("power")
    val samples = measure(launchers, rounds, discard) { println(it) }
    val report = environment() + mapOf("powerBefore" to powerBefore)
    File(directory, "cold-start.json").writeText(json(samples, rounds, discard, report))
    val text = table(samples)
    File(directory, "cold-start.md").writeText(text)
    println(text)
}

private const val NANOS_PER_MILLI: Double = 1_000_000.0
private const val DEFAULT_ROUNDS: Int = 30
private const val PROCESS_TIMEOUT_SECONDS: Long = 120

private val JVM_FLAGS: List<String> = listOf(
    "-Xms2g",
    "-Xmx2g",
    "-XX:+UseG1GC",
    "-Duser.timezone=UTC",
    "-Duser.language=en",
    "-Duser.country=US",
    "-Dfile.encoding=UTF-8",
)

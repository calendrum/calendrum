package io.github.calendrum.benchmarks.corpus

import java.io.File

// Entry point for the `:benchmarks:generateCorpora` JavaExec task: writes every corpus to
// `<outputDirectory>/<name>.ics`, `SHA256SUMS` and `generator.properties` (version and seed, read back by
// `BenchmarkEnvironment` for `environment.json`).
fun main(args: Array<String>) {
    require(args.isNotEmpty()) { "Usage: GenerateCorpora <outputDirectory>" }
    val outputDirectory = File(args[0])
    outputDirectory.deleteRecursively()
    outputDirectory.mkdirs()

    val sums = LinkedHashMap<String, String>()
    for (name in Corpus.names) {
        val started = System.nanoTime()
        val file = File(outputDirectory, "$name.ics")
        val (size, sha256) = writeCorpusToFile(name, file)
        sums[file.name] = sha256
        val seconds = (System.nanoTime() - started) / 1_000_000_000.0
        println("$name: ${file.name}, $size bytes, sha256 $sha256, ${"%.2f".format(seconds)} s")
    }

    val sumsFile = File(outputDirectory, "SHA256SUMS")
    sumsFile.writeText(
        sums.entries.sortedBy { it.key }.joinToString(separator = "\n", postfix = "\n") { (name, sha256) ->
            "$sha256  $name"
        },
    )
    println("Wrote ${sumsFile.absolutePath}")

    val metadataFile = File(outputDirectory, "generator.properties")
    metadataFile.writeText("version=$CORPUS_GENERATOR_VERSION\nseed=$CORPUS_SEED\n")
}

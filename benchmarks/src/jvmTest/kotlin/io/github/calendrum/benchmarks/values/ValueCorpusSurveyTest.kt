@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.values

import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.value.ValueCodecs
import io.github.calendrum.value.ValueProblems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.TreeMap
import kotlin.io.path.name
import kotlin.test.Test
import kotlin.test.assertTrue

// Decodes every value of a known type in the real corpus, the benchmark corpora and the conformance cases, leniently
// and strictly, and writes the counts by problem code to `build/reports/value-corpus-survey.md`; it fails only on an
// exception.
class ValueCorpusSurveyTest {
    @Test
    fun everyValueOfAKnownTypeDecodesLeniently() {
        val sources = LinkedHashMap<String, List<Pair<String, ByteArray>>>()
        if (RealCorpus.isPresent()) sources["real"] = files(locate("corpus/real")) { it.name.endsWith(".ics") }
        sources["synthetic"] = Corpus.names.map { it to corpusBytes(it) }
        sources["conformance"] = files(locate("conformance/ical")) { it.name == "input.ics" }
        val report =
            StringBuilder("| Source | Values | Decoded | Lenient problems | Strict rejected |\n|---|--:|--:|---|--:|\n")
        val byType = TreeMap<String, Int>()
        val rejectedValues = ArrayList<String>()
        for ((source, inputs) in sources) {
            assertTrue(inputs.isNotEmpty(), source)
            var count = 0
            var decoded = 0
            var rejected = 0
            val problems = TreeMap<String, Int>()
            val lenient = ValueCodecs(false, false, ValueProblems { code, _ -> problems.merge(code.id, 1, Int::plus) })
            val strict = ValueCodecs(true, false, null)
            for ((file, bytes) in inputs) {
                val values = corpusValues(bytes, null)
                for (index in 0 until values.size) {
                    count += 1
                    if (values.decode(lenient, index) != null) {
                        decoded += 1
                    } else {
                        rejectedValues += "$source $file ${values.names[index]}"
                    }
                    if (values.decode(strict, index) == null) rejected += 1
                    val type = values.types[index]
                    val name = if (type == GEO_TYPE) "FLOAT (GEO)" else TYPE_NAMES[type]
                    byType.merge("$source $name", 1, Int::plus)
                }
            }
            val summary = if (problems.isEmpty()) "—" else problems.entries.joinToString { "${it.key} × ${it.value}" }
            report.append("| $source (${inputs.size} files) | $count | $decoded | $summary | $rejected |\n")
        }
        report.append("\nRejected leniently: ${rejectedValues.joinToString().ifEmpty { "none" }}\n")
        report.append("\n| Source and type | Values |\n|---|--:|\n")
        for ((key, value) in byType) report.append("| $key | $value |\n")
        println(report)
        val output = Paths.get(System.getProperty("user.dir")).resolve("build/reports/value-corpus-survey.md")
        Files.createDirectories(output.parent)
        Files.write(output, report.toString().toByteArray())
    }

    private fun files(root: Path, accept: (Path) -> Boolean): List<Pair<String, ByteArray>> =
        Files.walk(root).use { paths ->
            paths.filter { Files.isRegularFile(it) && accept(it) }.sorted().toList()
                .map { root.relativize(it).toString() to Files.readAllBytes(it) }
        }

    private fun locate(relative: String): Path {
        var directory: Path? = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        while (directory != null) {
            val candidate = directory.resolve(relative)
            if (Files.isDirectory(candidate)) return candidate
            directory = directory.parent
        }
        throw IllegalStateException("No $relative above ${System.getProperty("user.dir")}")
    }
}

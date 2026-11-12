package io.github.calendrum.benchmarks.parseTyped

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.parser
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.TreeMap
import kotlin.io.path.name
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ModelCorpusSurveyTest {
    @Test
    fun everyCorpusReadsThroughTheModelWithoutExceptions() {
        val sources = LinkedHashMap<String, List<Pair<String, ByteArray>>>()
        if (RealCorpus.isPresent()) sources["real"] = files(locate("corpus/real")) { it.name.endsWith(".ics") }
        sources["synthetic"] = Corpus.names.map { it to corpusBytes(it) }
        sources["conformance"] = files(locate("conformance/ical")) { it.name == "input.ics" }
        val eager = ICal.parser { decodeEagerly = true }
        val report = StringBuilder("| Source | Files | Components | Values read | Value problems (eager, lenient) |\n")
        report.append("|---|--:|--:|--:|---|\n")
        for ((source, inputs) in sources) {
            assertTrue(inputs.isNotEmpty(), source)
            val problems = TreeMap<String, Int>()
            var components = 0
            var values = 0
            for ((file, bytes) in inputs) {
                val result = eager.parse(bytes)
                for (problem in result.problems) {
                    if (problem.code.id.startsWith("ICAL-V")) problems.merge(problem.code.id, 1, Int::plus)
                }
                val reader = ModelReader()
                val read = reader.read(result)
                assertEquals(read, ModelReader().read(ICal.parse(bytes)), "$source $file: eager and lazy differ")
                components += reader.components
                values += read
            }
            val summary = if (problems.isEmpty()) "—" else problems.entries.joinToString { "${it.key} × ${it.value}" }
            report.append("| $source | ${inputs.size} | $components | $values | $summary |\n")
        }
        println(report)
        val output = Paths.get(System.getProperty("user.dir")).resolve("build/reports/model-corpus-survey.md")
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

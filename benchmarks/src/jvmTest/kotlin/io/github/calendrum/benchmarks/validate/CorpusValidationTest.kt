package io.github.calendrum.benchmarks.validate

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.model.calendars
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.Validator
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.test.Test
import kotlin.test.assertTrue

class CorpusValidationTest {
    @Test
    fun everyCorpusGivesAReport() {
        val inputs = LinkedHashMap<String, ByteArray>()
        for (corpus in listOf("invite", "invite-batch", "personal", "university")) inputs[corpus] = corpusBytes(corpus)
        if (RealCorpus.isPresent()) {
            Files.walk(locate("corpus/real")).use { paths ->
                paths.filter {
                    it.toString().endsWith(".ics")
                }.forEach { inputs["real/${it.name}"] = Files.readAllBytes(it) }
            }
        }
        Files.list(locate("conformance/ical")).use { cases ->
            cases.filter { it.isDirectory() }.forEach {
                inputs["conformance/${it.name}"] =
                    Files.readAllBytes(it.resolve("input.ics"))
            }
        }
        var calendars = 0
        val codes = sortedMapOf<String, Int>()
        for ((name, bytes) in inputs) {
            for (calendar in ICal.parse(bytes).calendars) {
                calendars++
                val method = calendar.method
                val rules = if (method == null) RuleSet.Rfc5545 else RuleSet.itip(method)
                for (issue in Validator.validate(calendar, rules).issues) {
                    codes[issue.code.id] =
                        (codes[issue.code.id] ?: 0) + 1
                }
            }
        }
        println("validated $calendars calendars from ${inputs.size} inputs: $codes")
        assertTrue(calendars > 1000)
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

package io.github.calendrum.syntax

import io.github.calendrum.ProblemCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Accuracy through the whole `decodeInput` path: correct means the decoded text equals the original. Texts of 40
// characters or more must be at least 99% correct, shorter ones either correct or reported with low confidence
// (ICAL-S028). All-capital and all-small variants are a stress test with a looser bound; the numbers are printed for
// the report.
class CharsetHeuristicTest {
    private class Tally(val name: String) {
        var total = 0
        var correct = 0
        var lowConfidence = 0
        var confidentWrong = 0
        val failures = mutableListOf<String>()

        fun add(text: String, correct: Boolean, low: Boolean, result: String) {
            total++
            if (correct) this.correct++
            if (low) lowConfidence++
            if (!correct && !low) confidentWrong++
            if (!correct) failures += "$result: $text"
        }

        val accuracy: Double get() = if (total == 0) 1.0 else correct.toDouble() / total

        override fun toString(): String =
            "$name: $correct/$total correct (${percent(accuracy)}), low confidence $lowConfidence, " +
                "confident and wrong $confidentWrong"

        private fun percent(value: Double): String = "${(value * 1000).toInt() / 10.0}%"
    }

    private fun koi8Safe(text: String): String = text.replace("№", "N").replace("«", "\"").replace("»", "\"")
        .replace("–", "-").replace("—", "-").replace("…", "...")

    private fun measure(
        label: String,
        texts: List<String>,
        charsets: List<String>,
        variant: (String) -> String,
    ): List<Tally> {
        val tallies = mutableListOf<Tally>()
        for (charset in charsets) {
            val long = Tally("$label $charset, 40+ chars")
            val short = Tally("$label $charset, under 40 chars")
            for (original in texts) {
                val text = variant(if (charset == "KOI8-R") koi8Safe(original) else original)
                val bytes = TestCharsets.encode(text, charset)
                if (bytes.all { it >= 0 }) continue
                val (decoded, problems) = decode(bytes, null)
                val correct = decoded.text() == text
                val low = problems.any { it.code == ProblemCode.LowCharsetConfidence }
                (if (text.length >= LONG_TEXT) long else short)
                    .add(text, correct, low, "${decoded.charset} ${decoded.confidence}%")
            }
            tallies += long
            tallies += short
        }
        return tallies
    }

    private fun all(variant: (String) -> String, label: String): List<Tally> =
        measure("$label russian", russianTexts, listOf("windows-1251", "KOI8-R"), variant) +
            measure("$label western", westernTexts, listOf("windows-1252"), variant) +
            measure("$label russian check", russianCheckTexts, listOf("windows-1251", "KOI8-R"), variant) +
            measure("$label western check", westernCheckTexts, listOf("windows-1252"), variant)

    @Test
    fun heuristicIsAccurateOnRealisticTexts() {
        val tallies = all({ it }, "as written")
        tallies.forEach { println("charset heuristic: $it") }
        for (tally in tallies) {
            if (tally.name.endsWith("40+ chars")) {
                assertTrue(tally.accuracy >= REQUIRED_ACCURACY, "$tally\n${tally.failures.joinToString("\n")}")
            } else {
                assertEquals(0, tally.confidentWrong, "$tally\n${tally.failures.joinToString("\n")}")
            }
        }
    }

    @Test
    fun heuristicOnAllCapitalAndAllSmallLetters() {
        val tallies = all(String::uppercase, "upper case") + all(String::lowercase, "lower case")
        tallies.forEach { println("charset heuristic: $it") }
        for (tally in tallies) {
            if (tally.name.endsWith("40+ chars")) {
                assertTrue(tally.accuracy >= STRESS_ACCURACY, "$tally\n${tally.failures.joinToString("\n")}")
            }
        }
    }

    @Test
    fun benchmarkCorpusTextsAreDetected() {
        val text = russianTexts.joinToString("\r\n") { "DESCRIPTION:$it" }
        for (charset in listOf("windows-1251", "KOI8-R")) {
            val prepared = if (charset == "KOI8-R") koi8Safe(text) else text
            val (decoded, problems) = decode(TestCharsets.encode(prepared, charset), null)
            assertEquals(charset, decoded.charset)
            assertEquals(FULL_CONFIDENCE, decoded.confidence)
            assertEquals(listOf("ICAL-S025"), problems.map { it.id })
            assertEquals(prepared, decoded.text())
        }
    }

    private companion object {
        const val LONG_TEXT = 40
        const val REQUIRED_ACCURACY = 0.99
        const val STRESS_ACCURACY = 0.9
    }
}

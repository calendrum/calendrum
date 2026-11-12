package io.github.calendrum.examples.cli

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainTest {
    private val sample = System.getProperty("examples.sample")

    private fun call(vararg args: String): Pair<Int, String> {
        val out = StringBuilder()
        return run(args.toList(), out) to out.toString()
    }

    @Test
    fun everyCommandWorksOnTheSample() {
        val (summaryCode, summary) = call("summary", sample)
        assertEquals(0, summaryCode)
        assertTrue(summary.startsWith("calendar Seminar\n"), summary)
        val (_, occurrences) = call("occurrences", sample, "2026-09-01T00:00:00Z", "2026-10-01T00:00:00Z")
        val lines = occurrences.lines().filter { it.isNotEmpty() }
        assertEquals(4, lines.size, occurrences)
        assertEquals("2026-09-07T08:00:00Z 2026-09-07T09:30:00Z Distributed systems seminar", lines[0])
        assertTrue(lines[1].endsWith("(moved)"), lines[1])
        val (validCode, validation) = call("validate", sample)
        assertEquals(0, validCode, validation)
        val (_, anonymized) = call("anonymize", sample)
        assertTrue(anonymized.startsWith("BEGIN:VCALENDAR"), anonymized)
        assertTrue("Distributed" !in anonymized)
        assertEquals(2, call("unknown", sample).first)
        assertEquals(2, call().first)
    }
}

package io.github.calendrum.benchmarks.baseline

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BaselineEquivalenceTest {
    @Test
    fun invite() {
        assertEquivalent(Corpus.INVITE)
    }

    @Test
    fun inviteBatch() {
        assertEquivalent(Corpus.INVITE_BATCH)
    }

    @Test
    fun personal() {
        assertEquivalent(Corpus.PERSONAL)
    }

    @Test
    fun university() {
        assertEquivalent(Corpus.UNIVERSITY)
    }

    @Test
    fun splitsInviteBatchIntoSeparateCalendars() {
        val inputs = baselineInputs(Corpus.INVITE_BATCH)
        assertEquals(1000, inputs.size)
        assertEquals(corpusBytes(Corpus.INVITE_BATCH).size, inputs.sumOf { it.size })
        assertTrue(inputs.all { it.decodeToString().startsWith("BEGIN:VCALENDAR\r\n") })
    }

    private fun assertEquivalent(corpus: String) {
        val result = corpusEquivalence(corpus)
        assertEquals(emptyList(), result.problems(), "$corpus:\n${equivalenceTable(listOf(result))}")
        result.reports.forEach { assertEquals(expectedOccurrenceTotals[corpus], it.occurrences?.values?.sum()) }
    }
}

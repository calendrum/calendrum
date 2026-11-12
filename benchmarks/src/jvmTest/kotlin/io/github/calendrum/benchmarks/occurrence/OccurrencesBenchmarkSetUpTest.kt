package io.github.calendrum.benchmarks.occurrence

import io.github.calendrum.benchmarks.occurrences.Ical4jOccurrencesBenchmark
import io.github.calendrum.benchmarks.occurrences.OccurrencesBenchmark
import kotlin.test.Test

class OccurrencesBenchmarkSetUpTest {
    @Test
    fun oursAndIcal4jAgreeOnBothCorpora() {
        for (corpus in listOf("personal", "university")) {
            OccurrencesBenchmark().apply { this.corpus = corpus }.setUp()
            Ical4jOccurrencesBenchmark().apply { this.corpus = corpus }.setUp()
        }
    }
}

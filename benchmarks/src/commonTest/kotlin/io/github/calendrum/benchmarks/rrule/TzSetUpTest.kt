package io.github.calendrum.benchmarks.rrule

import io.github.calendrum.benchmarks.occurrences.OccurrencesBenchmark
import io.github.calendrum.benchmarks.tz.TzBenchmark
import kotlin.test.Test

class TzSetUpTest {
    @Test
    fun bothSourcesAgreeWithTheTable() {
        for (source in listOf("vtimezone", "tzdata")) TzBenchmark().apply { this.source = source }.setUp()
    }

    @Test
    fun theOccurrenceSetUpCounts() {
        OccurrencesBenchmark().apply { corpus = "personal" }.setUp()
    }
}

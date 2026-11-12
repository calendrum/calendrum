package io.github.calendrum.benchmarks.occurrences

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

object OccurrenceWindow {
    val from: Instant = Instant.parse("2026-01-01T00:00:00Z")
    val to: Instant = Instant.parse("2027-01-01T00:00:00Z")
    val options: OccurrenceOptions = OccurrenceOptions.builder().floatingZone(TimeZone.UTC).build()
    val expected: Map<String, Int> = mapOf(Corpus.PERSONAL to 9_337, Corpus.UNIVERSITY to 175_200)
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class OccurrencesBenchmark {
    @Param("personal", "university")
    var corpus: String = Corpus.UNIVERSITY

    private var calendar: VCalendar? = null

    @Setup
    fun setUp() {
        val parsed = ICal.parse(corpusBytes(corpus)).calendars.single()
        calendar = parsed
        val count = window(parsed)
        val expected = OccurrenceWindow.expected.getValue(corpus)
        check(count == expected) { "$corpus: $count occurrences" }
    }

    @Benchmark
    fun window(blackhole: Blackhole) {
        blackhole.consume(window(calendar!!))
    }

    private fun window(calendar: VCalendar): Int =
        calendar.occurrences(OccurrenceWindow.from, OccurrenceWindow.to, OccurrenceWindow.options).count()
}

package io.github.calendrum.benchmarks.occurrences

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
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
import kotlin.time.Instant

// The occurrences of a semester of the real university schedule (the window of `RealScheduleOccurrencesTest`, whose
// golden file lists them), with the time zones of the file; JVM-only like `RealParseBenchmark`, its parameter is `file`.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class RealOccurrencesBenchmark {
    @Param(RealCorpus.UNIVERSITY)
    var file: String = RealCorpus.UNIVERSITY

    private var calendar: VCalendar? = null

    @Setup
    fun setUp() {
        val parsed = ICal.parse(RealCorpus.bytes(file)).calendars.single()
        calendar = parsed
        val count = window(parsed)
        check(count == SEMESTER_OCCURRENCES) { "$file: $count occurrences" }
    }

    @Benchmark
    fun window(blackhole: Blackhole) {
        blackhole.consume(window(calendar!!))
    }

    private fun window(calendar: VCalendar): Int = calendar.occurrences(FROM, TO).count()

    private companion object {
        val FROM: Instant = Instant.parse("2025-09-01T00:00:00Z")
        val TO: Instant = Instant.parse("2026-07-01T00:00:00Z")
        const val SEMESTER_OCCURRENCES = 488
    }
}

package io.github.calendrum.benchmarks.freebusy

import io.github.calendrum.ICal
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.FreeBusyCalc
import io.github.calendrum.occurrence.OccurrenceOptions
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

// One event whose occurrences overlap their neighbours: the merge has to join all of them into a single interval, which is
// what used to cost a pass over all the intervals for each boundary.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class FreeBusyBenchmark {
    @Param("1000", "10000", "100000")
    var occurrences: String = "1000"

    private val from = Instant.parse("2026-01-01T00:00:00Z")
    private val to = Instant.parse("2027-01-01T00:00:00Z")
    private val options = OccurrenceOptions.builder().floatingZone(TimeZone.UTC).build()
    private var calendars: List<VCalendar> = emptyList()

    @Setup
    fun setUp() {
        calendars = ICal.parse(
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//bench//EN\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n" +
                "DTSTAMP:20260101T000000Z\r\nDTSTART:20260101T000000Z\r\nDURATION:PT2M\r\n" +
                "RRULE:FREQ=MINUTELY;COUNT=$occurrences\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
        ).calendars
        val busy = FreeBusyCalc.busy(calendars, from, to, options)
        check(busy.size == 1) { "$occurrences occurrences: ${busy.size} intervals" }
    }

    @Benchmark
    fun busy(blackhole: Blackhole) {
        blackhole.consume(FreeBusyCalc.busy(calendars, from, to, options))
    }
}

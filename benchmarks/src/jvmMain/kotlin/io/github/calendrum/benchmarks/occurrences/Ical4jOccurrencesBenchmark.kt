package io.github.calendrum.benchmarks.occurrences

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
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
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Period
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.RecurrenceId
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.Temporal

object Ical4jOccurrences {
    private val window: Period<Temporal> =
        Period(ZonedDateTime.parse("2026-01-01T00:00:00Z"), ZonedDateTime.parse("2027-01-01T00:00:00Z"))

    fun parse(bytes: ByteArray): Calendar = CalendarBuilder().build(ByteArrayInputStream(bytes))

    fun count(calendar: Calendar): Int {
        val events = calendar.getComponents<VEvent>("VEVENT")
        val overridden = HashMap<String, MutableSet<Long>>()
        var total = 0
        for (event in events) {
            val recurrenceId = event.getProperty<RecurrenceId<Temporal>>("RECURRENCE-ID")
            if (!recurrenceId.isPresent) continue
            val uid = event.getProperty<Property>("UID").orElseThrow().value
            overridden.getOrPut(uid) { HashSet() } += epoch(recurrenceId.get().date)
            if (cancelled(event)) continue
            total += event.calculateRecurrenceSet<Temporal>(window).count { epoch(it.start) < END }
        }
        for (event in events) {
            if (event.getProperty<Property>("RECURRENCE-ID").isPresent || cancelled(event)) continue
            val uid = event.getProperty<Property>("UID").orElseThrow().value
            val skipped = overridden[uid].orEmpty()
            for (period in event.calculateRecurrenceSet<Temporal>(window)) {
                val start = epoch(period.start)
                if (start >= END || start in skipped) continue
                total++
            }
        }
        return total
    }

    private fun cancelled(event: VEvent): Boolean =
        event.getProperty<Property>("STATUS").map { it.value == "CANCELLED" }.orElse(false)

    private fun epoch(value: Temporal): Long = when (value) {
        is ZonedDateTime -> value.toEpochSecond()
        is OffsetDateTime -> value.toEpochSecond()
        is LocalDate -> value.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
        is LocalDateTime -> value.toEpochSecond(ZoneOffset.UTC)
        is java.time.Instant -> value.epochSecond
        else -> error("Unexpected temporal ${value::class}")
    }

    private val END: Long = ZonedDateTime.parse("2027-01-01T00:00:00Z").toEpochSecond()

    val expected: Map<String, Int> = mapOf(Corpus.PERSONAL to 9_337, Corpus.UNIVERSITY to 175_200)
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class Ical4jOccurrencesBenchmark {
    @Param("personal", "university")
    var corpus: String = Corpus.UNIVERSITY

    private var calendar: Calendar? = null

    @Setup
    fun setUp() {
        val parsed = Ical4jOccurrences.parse(corpusBytes(corpus))
        calendar = parsed
        val count = Ical4jOccurrences.count(parsed)
        val expected = Ical4jOccurrences.expected.getValue(corpus)
        check(count == expected) { "$corpus: ical4j gave $count occurrences" }
    }

    @Benchmark
    fun window(blackhole: Blackhole) {
        blackhole.consume(Ical4jOccurrences.count(calendar!!))
    }
}

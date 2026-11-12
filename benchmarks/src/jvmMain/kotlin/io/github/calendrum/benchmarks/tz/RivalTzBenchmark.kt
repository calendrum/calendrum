package io.github.calendrum.benchmarks.tz

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.component.VTimeZone
import java.io.StringReader
import java.time.ZoneId
import java.time.zone.ZoneRules
import net.fortuna.ical4j.model.TimeZone as Ical4jTimeZone

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class Ical4jTzBenchmark {
    private var zones: List<Ical4jTimeZone> = emptyList()

    @Setup
    fun setUp() {
        zones = TzSamples.zones.map { id ->
            val calendar = CalendarBuilder().build(StringReader(TzSamples.vtimezoneText(id)))
            Ical4jTimeZone(calendar.getComponent<VTimeZone>("VTIMEZONE").orElseThrow())
        }
        val rules = ZoneId.of("Asia/Tokyo").rules
        check(zones[3].getOffset(0) == rules.getOffset(java.time.Instant.EPOCH).totalSeconds * 1000) {
            "ical4j Tokyo offset"
        }
    }

    @Benchmark
    fun offsetAt(blackhole: Blackhole) {
        var total = 0L
        for (zone in zones) {
            for (seconds in TzSamples.instants) total += zone.getOffset(seconds * 1000)
        }
        blackhole.consume(total)
    }
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class JavaTimeTzBenchmark {
    private var rules: List<ZoneRules> = emptyList()
    private var ids: List<ZoneId> = emptyList()
    private var locals: List<java.time.LocalDateTime> = emptyList()

    @Setup
    fun setUp() {
        ids = TzSamples.zones.map { ZoneId.of(it) }
        rules = ids.map { it.rules }
        locals = TzSamples.locals.map { java.time.LocalDateTime.parse(it.toString()) }
    }

    @Benchmark
    fun offsetAt(blackhole: Blackhole) {
        var total = 0
        for (zone in rules) {
            for (seconds in TzSamples.instants) {
                total +=
                    zone.getOffset(java.time.Instant.ofEpochSecond(seconds)).totalSeconds
            }
        }
        blackhole.consume(total)
    }

    @Benchmark
    fun toInstant(blackhole: Blackhole) {
        var total = 0L
        for (zone in ids) {
            for (local in locals) total += local.atZone(zone).toEpochSecond()
        }
        blackhole.consume(total)
    }
}

package io.github.calendrum.benchmarks.tz

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tzdata.TzData
import io.github.calendrum.tzdata.VTimeZoneMode
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
import kotlinx.datetime.LocalDateTime
import kotlin.time.Instant

object TzSamples {
    const val COUNT: Int = 4096
    val zones: List<String> = listOf("Europe/Berlin", "America/New_York", "Australia/Lord_Howe", "Asia/Tokyo")
    val instants: LongArray = LongArray(COUNT).also { values ->
        var state = 0x5EEDL
        val from = Instant.parse("1970-01-01T00:00:00Z").epochSeconds
        val span = Instant.parse("2038-01-01T00:00:00Z").epochSeconds - from
        for (index in values.indices) {
            state = state * 6364136223846793005L + 1442695040888963407L
            values[index] = from + (state ushr 1) % span
        }
    }
    val locals: Array<LocalDateTime> = Array(COUNT) { index ->
        val seconds = instants[index]
        val day = seconds / 86_400
        val time = (seconds % 86_400).toInt()
        val date = kotlinx.datetime.LocalDate.fromEpochDays(day.toInt())
        LocalDateTime(date.year, date.month, date.day, if (index % 8 == 0) 2 else time / 3600, time / 60 % 60, 0)
    }

    fun vtimezoneText(id: String): String = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\n" +
        ICal.writer().encodeToString(TzData.vtimezone(id, VTimeZoneMode.Full)!!.component) +
        "END:VCALENDAR\r\n"
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class TzBenchmark {
    @Param("vtimezone", "tzdata")
    var source: String = "vtimezone"

    private var zones: Array<ICalTimeZone> = emptyArray()

    @Setup
    fun setUp() {
        zones = Array(TzSamples.zones.size) { index ->
            val id = TzSamples.zones[index]
            if (source == "tzdata") {
                TzData.zone(id)!!
            } else {
                val calendar = ICal.parse(TzSamples.vtimezoneText(id)).calendars.single()
                TimeZoneResolvers.vtimezone(calendar).resolve(id, ResolveContext.Empty)!!
            }
        }
        for ((index, zone) in zones.withIndex()) {
            val reference = TzData.zone(TzSamples.zones[index])!!
            for (seconds in TzSamples.instants) {
                val instant = Instant.fromEpochSeconds(seconds)
                check(zone.offsetAt(instant) == reference.offsetAt(instant)) { "$source ${zone.id} at $instant" }
            }
        }
    }

    @Benchmark
    fun offsetAt(blackhole: Blackhole) {
        var total = 0
        for (zone in zones) {
            for (seconds in TzSamples.instants) total += zone.offsetAt(Instant.fromEpochSeconds(seconds)).totalSeconds
        }
        blackhole.consume(total)
    }

    @Benchmark
    fun toInstant(blackhole: Blackhole) {
        var total = 0L
        for (zone in zones) {
            for (local in TzSamples.locals) total += zone.toInstant(local).epochSeconds
        }
        blackhole.consume(total)
    }
}

@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.occurrence

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.FbType
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Clock
import kotlin.time.Instant

class FreeBusyCalcTest {
    private fun calendar(vararg lines: String): VCalendar = ICal.parse(
        (listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN") + lines.toList() + "END:VCALENDAR")
            .joinToString("") { "$it\r\n" },
    ).calendars.single()

    private fun event(uid: String, start: String, end: String, vararg extra: String): Array<String> = arrayOf(
        "BEGIN:VEVENT",
        "UID:$uid",
        "DTSTAMP:20260901T000000Z",
        "DTSTART:$start",
        "DTEND:$end",
        *extra,
        "END:VEVENT",
    )

    private val work = calendar(
        *event("a", "20261005T090000Z", "20261005T100000Z"),
        *event("b", "20261005T093000Z", "20261005T103000Z", "STATUS:TENTATIVE"),
        *event("c", "20261005T120000Z", "20261005T130000Z", "TRANSP:TRANSPARENT"),
        *event("d", "20261005T140000Z", "20261005T150000Z", "STATUS:CANCELLED"),
        *event("e", "20261005T160000Z", "20261005T170000Z", "RRULE:FREQ=DAILY;COUNT=2"),
    )
    private val personal = calendar(
        *event("p", "20261005T095000Z", "20261005T094000Z"),
        "BEGIN:VFREEBUSY",
        "UID:fb",
        "DTSTAMP:20260901T000000Z",
        "FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:20261005T180000Z/PT1H,20261005T183000Z/20261005T200000Z",
        "FREEBUSY;FBTYPE=FREE:20261005T060000Z/PT1H",
        "FREEBUSY:20261005T230000Z/P1D",
        "END:VFREEBUSY",
    )
    private val from = Instant.parse("2026-10-05T00:00:00Z")
    private val to = Instant.parse("2026-10-06T00:00:00Z")

    @Test
    fun busyIntervalsAreMergedByStrength() {
        assertEquals(
            listOf(
                "09:00..10:00 BUSY",
                "10:00..10:30 BUSY-TENTATIVE",
                "16:00..17:00 BUSY",
                "18:00..20:00 BUSY-UNAVAILABLE",
                "23:00..00:00 BUSY",
            ),
            FreeBusyCalc.busy(listOf(work, personal), from, to, OccurrenceOptions.Default).map {
                "${it.start.toString().substring(11, 16)}..${it.end.toString().substring(11, 16)} ${it.type.value}"
            },
        )
        assertFailsWith<IllegalArgumentException> { BusyInterval(to, from, FbType.Busy) }
        assertEquals(BusyInterval(from, to, FbType.Busy), BusyInterval(from, to, FbType.Busy))
        assertEquals("BusyInterval($from..$to BUSY)", BusyInterval(from, to, FbType.Busy).toString())
    }

    @Test
    fun conflictsAndVFreeBusy() {
        val conflicts = FreeBusyCalc.conflicts(listOf(work, personal), from, to, OccurrenceOptions.Default)
        assertEquals(
            listOf("a/b 09:30..10:00"),
            conflicts.map {
                "${it.first.component.property("UID")?.value}/${it.second.component.property("UID")?.value} " +
                    "${it.start.toString().substring(11, 16)}..${it.end.toString().substring(11, 16)}"
            },
        )
        val busy = FreeBusyCalc.busy(listOf(work), from, to, OccurrenceOptions.Default)
        val defaults = BuildDefaults(
            object : Clock {
                override fun now(): Instant = Instant.parse("2026-10-01T08:00:00Z")
            },
        ) { "fb-1" }
        val vfreebusy = FreeBusyCalc.toVFreeBusy(busy, from, to, defaults)
        assertEquals("fb-1", vfreebusy.uid)
        assertEquals(
            listOf("BUSY 2", "BUSY-TENTATIVE 1"),
            vfreebusy.freeBusy.map { "${it.type.value} ${it.periods.size}" },
        )
        assertEquals(
            "FREEBUSY:20261005T090000Z/20261005T100000Z,20261005T160000Z/20261005T170000Z",
            vfreebusy.component.properties("FREEBUSY").first().let { "FREEBUSY:${it.value}" },
        )
        assertEquals("2026-10-05T00:00:00Z", vfreebusy.start.toString())
    }

    // The algorithm before the sweep: plainly right and quadratic. The reference for what the sweep has to give.
    private fun mergedByDefinition(intervals: List<BusyInterval>): List<BusyInterval> {
        val points = intervals.flatMap { listOf(it.start, it.end) }.distinct().sorted()
        val result = ArrayList<BusyInterval>()
        for (index in 0 until points.size - 1) {
            val start = points[index]
            val end = points[index + 1]
            val type = intervals.filter { it.start <= start && it.end >= end }.maxByOrNull { rankOf(it.type) }?.type
                ?: continue
            val last = result.lastOrNull()
            if (last != null && last.end == start && last.type == type) {
                result[result.size - 1] = BusyInterval(last.start, end, type)
            } else {
                result += BusyInterval(start, end, type)
            }
        }
        return result
    }

    private fun rankOf(type: FbType): Int = when (type) {
        FbType.Busy -> 3
        FbType.BusyUnavailable -> 2
        FbType.BusyTentative -> 1
        else -> 0
    }

    @Test
    fun theSweepGivesWhatTheDefinitionGives() {
        val types = listOf(
            FbType.Busy,
            FbType.BusyUnavailable,
            FbType.BusyTentative,
            FbType.Free,
            FbType.of("X-FIRST"),
            FbType.of("X-SECOND"),
        )
        val random = Random(20261106)
        repeat(3000) { round ->
            val available = if (round % 3 == 0) types.drop(3) else types
            val grid = 1 + random.nextInt(40)
            val intervals = List(1 + random.nextInt(60)) {
                val start = random.nextInt(grid)
                val length = 1 + random.nextInt(if (round % 2 == 0) 3 else grid)
                BusyInterval(moment(start), moment(start + length), available[random.nextInt(available.size)])
            }
            assertEquals(mergedByDefinition(intervals), merge(intervals), "round $round: $intervals")
        }
    }

    @Test
    fun theFirstIntervalWinsAmongTypesOfTheSameRank() {
        val first = BusyInterval(moment(0), moment(4), FbType.of("X-FIRST"))
        val second = BusyInterval(moment(0), moment(2), FbType.of("X-SECOND"))
        assertEquals(listOf(first), merge(listOf(first, second)))
        assertEquals(
            listOf(
                BusyInterval(moment(0), moment(2), FbType.of("X-SECOND")),
                BusyInterval(moment(2), moment(4), FbType.of("X-FIRST")),
            ),
            merge(listOf(second, first)),
        )
    }

    @Test
    fun adjacentIntervalsOfOneTypeJoin() {
        val intervals = List(1000) { BusyInterval(moment(it), moment(it + 1), FbType.Busy) }
        assertEquals(listOf(BusyInterval(moment(0), moment(1000), FbType.Busy)), merge(intervals.shuffled(Random(1))))
        assertEquals(emptyList(), merge(emptyList()))
    }

    private fun moment(step: Int): Instant = Instant.fromEpochSeconds(step * 900L)
}

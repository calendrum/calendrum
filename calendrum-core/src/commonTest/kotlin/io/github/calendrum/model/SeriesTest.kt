package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class SeriesTest {
    private fun calendar(vararg lines: String): VCalendar = ICal.parse(
        (listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN") + lines.toList() + "END:VCALENDAR")
            .joinToString("") { "$it\r\n" },
    ).calendars.single()

    private val weekly = calendar(
        "BEGIN:VEVENT", "UID:w", "DTSTAMP:20260901T000000Z", "DTSTART:20261005T100000Z", "DTEND:20261005T110000Z",
        "SUMMARY:Sync", "RRULE:FREQ=WEEKLY;COUNT=6", "EXDATE:20261019T100000Z", "END:VEVENT",
        "BEGIN:VEVENT", "UID:w", "DTSTAMP:20260901T000000Z", "RECURRENCE-ID:20261026T100000Z",
        "DTSTART:20261026T120000Z", "DTEND:20261026T130000Z", "SUMMARY:Sync (late)", "END:VEVENT",
        "BEGIN:VEVENT", "UID:other", "DTSTAMP:20260901T000000Z", "DTSTART:20261007T090000Z", "SUMMARY:Other",
        "END:VEVENT",
    )

    private fun at(text: String): ICalDateTime = ICalDateTime.Utc(Instant.parse(text))

    private fun schedule(calendar: VCalendar): List<String> = calendar.occurrences(
        Instant.parse("2026-10-01T00:00:00Z"),
        Instant.parse("2027-01-01T00:00:00Z"),
        OccurrenceOptions.Default,
    ).filter { it.component.property("UID")?.value != "other" }
        .map { "${it.start.toString().substring(5, 16)} ${it.component.property("SUMMARY")?.value}" }.toList()

    @Test
    fun theFixtureSchedule() {
        assertEquals(
            listOf(
                "10-05T10:00 Sync",
                "10-12T10:00 Sync",
                "10-26T12:00 Sync (late)",
                "11-02T10:00 Sync",
                "11-09T10:00 Sync",
            ),
            schedule(weekly),
        )
        val series = assertNotNull(Series.find(weekly, "w"))
        assertEquals(1, series.overrides.size)
        assertEquals("w", series.master?.uid)
        assertNull(Series.find(weekly, "none"))
        assertTrue(series.toString().startsWith("SeriesView("))
    }

    @Test
    fun editThisAndDeleteThis() {
        val series = Series.find(weekly, "w")!!
        val moved = Series.editThis(series, at("2026-10-12T10:00:00Z")) {
            start = at("2026-10-13T10:00:00Z")
            end = at("2026-10-13T11:00:00Z")
            summary = "Sync (Tuesday)"
        }
        assertEquals(
            listOf(
                "10-05T10:00 Sync",
                "10-13T10:00 Sync (Tuesday)",
                "10-26T12:00 Sync (late)",
                "11-02T10:00 Sync",
                "11-09T10:00 Sync",
            ),
            schedule(moved),
        )
        val again = Series.editThis(Series.find(moved, "w")!!, at("2026-10-26T10:00:00Z")) { summary = "Late" }
        assertEquals("10-26T12:00 Late", schedule(again)[2])
        assertEquals(4, again.events.size)
        val deleted = Series.deleteThis(Series.find(again, "w")!!, at("2026-10-26T10:00:00Z"))
        assertEquals(
            listOf("10-05T10:00 Sync", "10-13T10:00 Sync (Tuesday)", "11-02T10:00 Sync", "11-09T10:00 Sync"),
            schedule(deleted),
        )
        val orphan = calendar(
            "BEGIN:VEVENT",
            "UID:o",
            "DTSTAMP:20260901T000000Z",
            "RECURRENCE-ID:20261026T100000Z",
            "DTSTART:20261026T120000Z",
            "END:VEVENT",
        )
        assertEquals(0, Series.deleteThis(Series.find(orphan, "o")!!, at("2026-10-26T10:00:00Z")).events.size)
        assertFailsWith<IllegalArgumentException> {
            Series.editThis(Series.find(orphan, "o")!!, at("2026-10-27T10:00:00Z")) { summary = "x" }
        }
    }

    @Test
    fun editThisAndFutureSplitsTheSeries() {
        val series = Series.find(weekly, "w")!!
        val split = Series.editThisAndFuture(series, at("2026-10-26T10:00:00Z")) {
            start = at("2026-10-26T15:00:00Z")
            end = at("2026-10-26T16:00:00Z")
            summary = "Sync (afternoon)"
        }
        assertEquals(
            listOf(
                "10-05T10:00 Sync",
                "10-12T10:00 Sync",
                "10-26T12:00 Sync (late)",
                "11-02T15:00 Sync (afternoon)",
                "11-09T15:00 Sync (afternoon)",
            ),
            schedule(split),
        )
        val old = split.events.first { it.uid == "w" && it.recurrenceId == null }
        assertEquals(3, old.rrule?.count)
        assertEquals(listOf("2026-10-19T10:00:00Z"), old.exdates.map { it.toString() })
        val tail = split.events.first { it.uid != "w" && it.uid != "other" && it.recurrenceId == null }
        assertEquals(3, tail.rrule?.count)
        assertEquals(listOf("w"), tail.relatedTo.map { it.uid })
        assertEquals(Canonical.uidV5("w", "2026-10-26T10:00:00Z"), tail.uid)
        val movedOverride = split.events.single { it.recurrenceId != null }
        assertEquals(tail.uid, movedOverride.uid)
        assertEquals("2026-10-26T15:00:00Z", movedOverride.recurrenceId.toString())

        val ranged = Series.editThisAndFuture(series, at("2026-11-02T10:00:00Z"), ThisAndFuture.Range) {
            summary = "Sync (new room)"
        }
        val override = ranged.events.single { it.recurrenceId.toString() == "2026-11-02T10:00:00Z" }
        assertEquals(RecurrenceRange.ThisAndFuture, override.component[Properties.RECURRENCE_ID]?.range)
        assertEquals("11-09T10:00 Sync (new room)", schedule(ranged).last())

        val fromStart = Series.editThisAndFuture(series, at("2026-10-05T10:00:00Z")) { summary = "All" }
        assertEquals(listOf("w", "w", "other"), fromStart.events.map { it.uid })
    }

    @Test
    fun editAllShiftsOverridesAndExceptions() {
        val series = Series.find(weekly, "w")!!
        val later = Series.editAll(series) {
            start = at("2026-10-05T11:00:00Z")
            end = at("2026-10-05T12:00:00Z")
        }
        assertEquals(
            listOf(
                "10-05T11:00 Sync",
                "10-12T11:00 Sync",
                "10-26T12:00 Sync (late)",
                "11-02T11:00 Sync",
                "11-09T11:00 Sync",
            ),
            schedule(later),
        )
        val retitled = Series.editAll(series) { summary = "Weekly" }
        assertEquals("10-05T10:00 Weekly", schedule(retitled).first())
    }

    @Test
    fun deleteThisAndFuture() {
        val series = Series.find(weekly, "w")!!
        val cut = Series.deleteThisAndFuture(series, at("2026-10-26T10:00:00Z"))
        assertEquals(listOf("10-05T10:00 Sync", "10-12T10:00 Sync"), schedule(cut))
        assertEquals(2, cut.events.size)
        val all = Series.deleteThisAndFuture(series, at("2026-10-05T10:00:00Z"))
        assertEquals(listOf("other"), all.events.map { it.uid })
        val dated = calendar(
            "BEGIN:VEVENT",
            "UID:d",
            "DTSTAMP:20260901T000000Z",
            "DTSTART;VALUE=DATE:20261005",
            "RRULE:FREQ=DAILY",
            "RDATE;VALUE=DATE:20261001,20261201",
            "END:VEVENT",
        )
        val stopped = Series.deleteThisAndFuture(
            Series.find(dated, "d")!!,
            ICalDateTime.Date(kotlinx.datetime.LocalDate(2026, 10, 8)),
        )
        val master = stopped.events.single()
        assertEquals(3, master.rrule?.count)
        assertEquals(listOf("2026-10-01"), master.rdates.map { it.toString() })
    }

    @Test
    fun splitAndMerge() {
        val parts = Series.splitByUid(
            calendar(
                "X-WR-CALNAME:Team", "BEGIN:VTIMEZONE", "TZID:Z", "BEGIN:STANDARD", "DTSTART:19700101T000000",
                "TZOFFSETFROM:+0100", "TZOFFSETTO:+0100", "END:STANDARD", "END:VTIMEZONE",
                "BEGIN:VEVENT", "UID:a", "DTSTAMP:20260901T000000Z", "DTSTART;TZID=Z:20261005T100000", "END:VEVENT",
                "BEGIN:VEVENT", "UID:b", "DTSTAMP:20260901T000000Z", "DTSTART:20261005T100000Z", "END:VEVENT",
                "BEGIN:VEVENT", "UID:a", "DTSTAMP:20260901T000000Z", "RECURRENCE-ID;TZID=Z:20261005T100000",
                "DTSTART;TZID=Z:20261005T110000", "END:VEVENT",
            ),
        )
        assertEquals(listOf(listOf("a", "a"), listOf("b")), parts.map { part -> part.events.map { it.uid } })
        assertEquals(listOf(1, 0), parts.map { it.timeZones.size })
        assertEquals("Team", parts[1].component.property("X-WR-CALNAME")?.value)
        val newer = calendar(
            "BEGIN:VEVENT",
            "UID:b",
            "DTSTAMP:20260902T000000Z",
            "SEQUENCE:1",
            "DTSTART:20261006T100000Z",
            "END:VEVENT",
        )
        val merged = Series.merge(parts[0], parts[1], newer, parts[1])
        assertEquals(listOf("a", "a", "b"), merged.events.map { it.uid })
        assertEquals(1, merged.events.last().sequence)
        assertEquals(1, merged.timeZones.size)
        assertFailsWith<IllegalArgumentException> { Series.merge() }
    }
}

package io.github.calendrum.guides

import io.github.calendrum.ICal
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.Trigger
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class GettingStartedGuide {
    @Test
    fun readACalendar() {
        val text = calendarText
        // region getting-started-read
        val result = ICal.parse(text) // never throws on bad data
        val calendar = result.calendars.single() // a typed VCalendar
        val summaries = calendar.events.map { it.summary } // ["Weekly sync", "Holiday"]
        val start = calendar.events.first().start // 2026-10-01T10:00[Europe/Berlin]
        val problems = result.problems.map { it.code.id } // [] for a clean file
        // endregion
        assertEquals(listOf("Weekly sync", "Holiday"), summaries)
        assertEquals("2026-10-01T10:00[Europe/Berlin]", start.toString())
        assertEquals(emptyList(), problems)
    }

    @Test
    fun writeACalendar() {
        // region getting-started-write
        val defaults = BuildDefaults(Clock.System) { newUid() } // UID and DTSTAMP come from you, not from globals
        val calendar = VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = defaults) {
            event {
                summary = "Weekly sync"
                start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
                duration = ICalDuration.ofHours(1)
                rrule(Frequency.WEEKLY) {
                    byDay(DayOfWeek.THURSDAY)
                    count(4)
                }
                attendee("mailto:jane@example.com") { cn = "Jane" }
                alarm { display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Soon") }
            }
        }
        val ics: String = ICal.writer().encodeToString(calendar.component)
        // endregion
        assertEquals(true, ics.startsWith("BEGIN:VCALENDAR\r\nPRODID:-//Example//Planner 1.0//EN\r\nVERSION:2.0\r\n"))
        assertEquals(true, "RRULE:FREQ=WEEKLY;COUNT=4;BYDAY=TH\r\n" in ics)
        assertEquals(true, "ATTENDEE;CN=Jane:mailto:jane@example.com\r\n" in ics)
    }

    @Test
    fun computeOccurrences() {
        val calendar = ICal.parse(calendarText).calendars.single()
        // region getting-started-occurrences
        val options = OccurrenceOptions.builder()
            .zones(TimeZoneResolvers.default(calendar)) // the VTIMEZONE of the file; no system time zone is used
            .build()
        val from = Instant.parse("2026-10-01T00:00:00Z")
        val to = Instant.parse("2026-10-20T00:00:00Z")
        val starts = calendar.occurrences(from, to, options)
            .filter { it.component.property("SUMMARY")?.value == "Weekly sync" }
            .map { it.start }
            // [2026-10-01T08:00:00Z, 2026-10-08T08:00:00Z, 2026-10-15T08:00:00Z]
            .toList()
        // endregion
        assertEquals(
            listOf("2026-10-01T08:00:00Z", "2026-10-08T08:00:00Z", "2026-10-15T08:00:00Z"),
            starts.map { it.toString() },
        )
    }
}

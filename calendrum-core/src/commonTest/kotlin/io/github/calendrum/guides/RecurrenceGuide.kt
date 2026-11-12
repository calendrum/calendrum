@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.guides

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.FreeBusyCalc
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.OccurrenceSet
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.recur.RecurrenceLimits
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.StartMismatch
import io.github.calendrum.recur.expand
import io.github.calendrum.recur.recurrenceSet
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class RecurrenceGuide {
    private val calendar: VCalendar = ICal.parse(calendarText).calendars.single()

    @Test
    fun recurrenceSets() {
        val event = calendar.events.first()
        val printed = ArrayList<String>()
        // region recurrence-set
        val set = event.recurrenceSet ?: return // null without DTSTART
        val from = ICalDateTime.Floating(LocalDateTime(2026, 10, 6, 0, 0))
        val to = ICalDateTime.Floating(LocalDateTime(2026, 10, 20, 0, 0))
        for (start in set.between(from, to)) printed += start.toString() // [from, to) in local time; EXDATE wins
        val all = event.rrule!!.expand(event.start!!).take(10).toList() // lazy: endless rules are fine
        // endregion
        assertEquals(listOf("2026-10-08T10:00[Europe/Berlin]", "2026-10-15T10:00[Europe/Berlin]"), printed)
        assertEquals(4, all.size)
    }

    @Test
    fun recurrenceOptions() {
        val set = calendar.events.first().recurrenceSet!!
        val reported = ArrayList<String>()
        // region recurrence-options
        val options = RecurrenceOptions.builder()
            // DTSTART the rule does not produce: the RFC counts it by default
            .startMismatch(StartMismatch.Exclude)
            // limits end the sequence, they never throw
            .limits(RecurrenceLimits.builder().maxOccurrences(10_000).build())
            // ICAL-R001 limit reached, ICAL-R003 DTSTART not in the rule…
            .problemListener { reported += it.code.id }
            .build()
        val total = Sequence { set.iterator(options) }.count()
        // endregion
        assertEquals(4, total)
        assertEquals(emptyList(), reported)
    }

    private val options: OccurrenceOptions = OccurrenceOptions.builder()
        .zones(TimeZoneResolvers.default(calendar))
        .floatingZone(TimeZone.UTC)
        .build()

    @Test
    fun occurrences() {
        val printed = ArrayList<String>()
        // region occurrences-window
        val from = Instant.parse("2026-10-01T00:00:00Z")
        val to = Instant.parse("2026-10-10T00:00:00Z")
        for (occurrence in calendar.occurrences(from, to, options)) { // RFC 4791 time-range semantics, [from, to)
            val title = occurrence.component.property("SUMMARY")?.value
            printed += "$title ${occurrence.start}..${occurrence.end} allDay=${occurrence.isAllDay}"
            occurrence.alarms.forEach { printed += "  alarm at ${it.at}" } // REPEAT is expanded, with a limit
        }
        // endregion
        assertEquals(
            listOf(
                "Weekly sync 2026-10-01T08:00:00Z..2026-10-01T09:00:00Z allDay=false",
                "  alarm at 2026-10-01T07:45:00Z",
                "Holiday 2026-10-02T00:00:00Z..2026-10-03T00:00:00Z allDay=true",
                "Weekly sync 2026-10-08T08:00:00Z..2026-10-08T09:00:00Z allDay=false",
                "  alarm at 2026-10-08T07:45:00Z",
            ),
            printed,
        )
    }

    @Test
    fun cursors() {
        val now = Instant.parse("2026-10-05T00:00:00Z")
        // region occurrences-cursor
        // next(), after(instant), at(instant), page(size, after)
        val cursor = calendar.occurrences(options)
        val next = cursor.after(now) // the first occurrence that starts after the instant
        val page = calendar.occurrences(options).page(size = 2, after = null) // for paging through a long series
        // endregion
        assertEquals(Instant.parse("2026-10-08T08:00:00Z"), next?.start)
        assertEquals(2, page.size)
    }

    @Test
    fun occurrenceSets() {
        val teaching = calendar
        val holidays = ICal.parse(
            icsLines(
                "BEGIN:VCALENDAR",
                "BEGIN:VEVENT",
                "UID:h",
                "DTSTART:20261008T080000Z",
                "DTEND:20261008T090000Z",
                "SUMMARY:Closed",
                "END:VEVENT",
                "END:VCALENDAR",
            ),
        ).calendars.single()
        val from = Instant.parse("2026-10-01T00:00:00Z")
        val to = Instant.parse("2026-10-20T00:00:00Z")

        // region occurrences-set
        fun setOf(calendar: VCalendar) = OccurrenceSet.of(calendar.occurrences(from, to, options).toList())
        val lessons = setOf(teaching).minus(setOf(holidays)) // union, intersect and minus on occurrences
        // endregion
        assertEquals(3, lessons.occurrences.size) // the 8th is gone: meetings of the 1st and 15th, and the holiday
    }

    @Test
    fun freeBusy() {
        val from = Instant.parse("2026-10-01T00:00:00Z")
        val to = Instant.parse("2026-10-20T00:00:00Z")
        val defaults = BuildDefaults(fixedClock) { newUid() }
        // region occurrences-freebusy
        val busy = FreeBusyCalc.busy(listOf(calendar), from, to, options) // merged, sorted intervals
        val conflicts = FreeBusyCalc.conflicts(listOf(calendar, calendar), from, to, options) // overlapping pairs
        val published = FreeBusyCalc.toVFreeBusy(busy, from, to, defaults) // a VFREEBUSY for publishing
        // endregion
        assertEquals(4, busy.size) // three meetings and the all-day holiday
        assertTrue(conflicts.isNotEmpty())
        assertEquals(1, published.freeBusy.size)
    }
}

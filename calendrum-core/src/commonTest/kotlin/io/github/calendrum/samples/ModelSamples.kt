@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.samples

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.ICalPatch
import io.github.calendrum.model.Parameters
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.Properties
import io.github.calendrum.model.Trigger
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.model.decode
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

private val sampleClock = object : Clock {
    override fun now(): Instant = Instant.parse("2026-10-01T08:00:00Z")
}

private const val SCHEDULE = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\n" +
    "BEGIN:VEVENT\r\nUID:42@example.com\r\nDTSTAMP:20260901T000000Z\r\nDTSTART;TZID=Europe/Berlin:20261001T100000\r\n" +
    "SUMMARY:Weekly sync\r\nATTENDEE;CN=Jane;PARTSTAT=NEEDS-ACTION:mailto:jane@example.com\r\n" +
    "BEGIN:VALARM\r\nACTION:DISPLAY\r\nTRIGGER:-PT15M\r\nDESCRIPTION:Soon\r\nEND:VALARM\r\nEND:VEVENT\r\n" +
    "BEGIN:VEVENT\r\nUID:43@example.com\r\nDTSTAMP:20260901T000000Z\r\nDTSTART:20261002\r\nEND:VEVENT\r\n" +
    "END:VCALENDAR\r\n"

fun readTypedModel() {
    val calendar = ICal.parse(SCHEDULE).calendars.single()

    val event = calendar.events.first()
    val start = event.start
    assertIs<ICalDateTime.Zoned>(start)
    assertEquals("Europe/Berlin", start.tzid)
    assertEquals(PartStat.NeedsAction, event.attendees.single().partStat)
    assertEquals(1, event.alarms.size)
    assertEquals(null, calendar.events.last().summary)
    assertIs<Decoded.Missing>(calendar.events.last().component.decode(Properties.DTEND))
}

fun buildCalendar() {
    var counter = 0
    val defaults = BuildDefaults(sampleClock) { "uid-${++counter}@example.com" }

    val calendar = VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = defaults) {
        event {
            summary = "Weekly sync"
            start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
            duration = ICalDuration.ofHours(1)
            rrule(Frequency.WEEKLY) {
                byDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
                count(10)
            }
            attendee("mailto:alice@example.com") {
                cn = "Alice"
                rsvp = true
            }
            alarm { display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Soon") }
        }
    }

    val event = calendar.events.single()
    assertEquals("uid-1@example.com", event.uid)
    assertEquals("Alice", event.attendees.single().cn)
}

fun editCalendar() {
    val calendar = ICal.parse(SCHEDULE).calendars.single()
    val event = calendar.events.first()

    val accepted = calendar.edit {
        events {
            replace(
                event,
                event.edit {
                    sequence = (sequence ?: 0) + 1
                    attendees { replace("mailto:jane@example.com") { partStat = PartStat.Accepted } }
                },
            )
        }
    }

    assertEquals(PartStat.Accepted, accepted.events.first().attendees.single().partStat)
    assertEquals(1, accepted.events.first().sequence)
    assertEquals(PartStat.NeedsAction, calendar.events.first().attendees.single().partStat)
}

fun attendeeBuilder() {
    val attendee = Attendee.builder("mailto:bob@example.com").cn("Bob").partStat(PartStat.Tentative).build()

    assertEquals("Bob", attendee.cn)
}

fun patchFile() {
    val original = SCHEDULE.encodeToByteArray()

    val result = ICalPatch(original) {
        component(uid = "42@example.com", recurrenceId = null) {
            attendee("mailto:jane@example.com") { set(Parameters.PARTSTAT, PartStat.Accepted) }
            set(Properties.DTSTAMP, ICalDateTime.Utc(Instant.parse("2026-10-01T08:00:00Z")))
        }
    }.apply()

    assertEquals(emptyList(), result.unmatched)
    assertEquals(2, result.changes.size)
    val text = result.bytes.decodeToString()
    assertTrue(text.contains("PARTSTAT=ACCEPTED"))
    assertTrue(text.contains("SUMMARY:Weekly sync\r\n"))
}

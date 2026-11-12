package io.github.calendrum.guides

import kotlin.time.Clock
import kotlin.time.Instant

internal fun icsLines(vararg lines: String): String = lines.joinToString("") { "$it\r\n" }

/** The calendar the guides read: a weekly meeting in Europe/Berlin with its own VTIMEZONE, and an all-day holiday. */
internal val calendarText: String = icsLines(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Guides//EN",
    "BEGIN:VTIMEZONE",
    "TZID:Europe/Berlin",
    "BEGIN:STANDARD",
    "DTSTART:19701025T030000",
    "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU",
    "TZOFFSETFROM:+0200",
    "TZOFFSETTO:+0100",
    "END:STANDARD",
    "BEGIN:DAYLIGHT",
    "DTSTART:19700329T020000",
    "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU",
    "TZOFFSETFROM:+0100",
    "TZOFFSETTO:+0200",
    "END:DAYLIGHT",
    "END:VTIMEZONE",
    "BEGIN:VEVENT",
    "UID:42@example.com",
    "DTSTAMP:20260901T000000Z",
    "DTSTART;TZID=Europe/Berlin:20261001T100000",
    "DTEND;TZID=Europe/Berlin:20261001T110000",
    "SUMMARY:Weekly sync",
    "LOCATION:Room 101",
    "RRULE:FREQ=WEEKLY;BYDAY=TH;COUNT=4",
    "ATTENDEE;CN=Jane;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:mailto:jane@example.com",
    "ATTENDEE;CN=Bob;PARTSTAT=ACCEPTED:mailto:bob@example.com",
    "BEGIN:VALARM",
    "ACTION:DISPLAY",
    "TRIGGER:-PT15M",
    "DESCRIPTION:Soon",
    "END:VALARM",
    "END:VEVENT",
    "BEGIN:VEVENT",
    "UID:43@example.com",
    "DTSTAMP:20260901T000000Z",
    "DTSTART;VALUE=DATE:20261002",
    "SUMMARY:Holiday",
    "END:VEVENT",
    "END:VCALENDAR",
)

internal val calendarBytes: ByteArray = calendarText.encodeToByteArray()

internal val fixedClock = object : Clock {
    override fun now(): Instant = Instant.parse("2026-10-01T08:00:00Z")
}

private var uidCounter = 0

/** A UID source for the examples; real code uses a UUID. */
internal fun newUid(): String = "uid-${++uidCounter}@example.com"

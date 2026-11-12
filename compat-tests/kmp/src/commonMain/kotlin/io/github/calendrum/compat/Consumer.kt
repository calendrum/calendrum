package io.github.calendrum.compat

import io.github.calendrum.Calendrum
import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.value.ICalDateTime

// Consumer code in main sources that reads a public API of calendrum.
fun calendrumVersion(): String = Calendrum.VERSION

// Calls the first real public API, `ICal.parse`, on a short embedded calendar, so the matrix checks that consumers of
// every Kotlin version can compile against the parser, the tree and the diagnostics, not only read a constant.
fun eventSummaries(): List<String> = ICal.parse(INVITATION).components
    .flatMap { calendar -> calendar.components("VEVENT") }
    .map { event -> event.property("SUMMARY")?.value.orEmpty() }

fun invitation(): String = INVITATION

fun eventStarts(): List<String> = ICal.parse(INVITATION).calendars.single().events.map { event ->
    val start = event.start
    if (start is ICalDateTime.Utc) start.instant.toString() else "?"
}

fun attendeeStatuses(): List<String> =
    ICal.parse(INVITATION).calendars.single().events.flatMap { event -> event.attendees.map { it.partStat.value } }

// Parses the embedded calendar and writes it back with `ICal.writer().encodeToString`; the test expects the same text
// byte for byte. The quoted parameter of X-WR-CALNAME is not canonical, so only byte-stable writing gives it back.
fun rewrittenInvitation(): String = ICal.writer().encodeToString(ICal.parse(INVITATION).components)

private val INVITATION = listOf(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Calendrum//Compat test//EN",
    "X-WR-CALNAME;x-lang=\"en\":Team",
    "BEGIN:VEVENT",
    "UID:1@example.com",
    "DTSTART:20260901T090000Z",
    "SUMMARY:Planning",
    "ATTENDEE;PARTSTAT=ACCEPTED:mailto:jane@example.com",
    "END:VEVENT",
    "BEGIN:VEVENT",
    "UID:2@example.com",
    "DTSTART:20260902T090000Z",
    "SUMMARY:Retro",
    "END:VEVENT",
    "END:VCALENDAR",
).joinToString(separator = "\r\n", postfix = "\r\n")

package io.github.calendrum.sizebudget

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import kotlin.time.Instant

// Parses a small invitation with `ICal.parse`, replaces the SUMMARY of its event through the builders (read, edit,
// write), writes the calendar with `ICal.writer().encodeToString` and reads the new SUMMARY back, so the budget
// measures the parser, the tree, the builders, the writer and the diagnostics. Builders instead of the DSL: a DSL
// lambda is a class of the test app itself on Native and would count as contribution. The app-baseline returns a
// literal of the same length.
fun usage(): String {
    val calendar = ICal.parse(INVITATION).components.first()
    val event = calendar.components("VEVENT").first()
    val edited = ICalComponent.builder(event.name)
    for (property in event.properties) {
        if (property.name != "SUMMARY") edited.property(property)
    }
    edited.property("SUMMARY", "Budget agreed")
    val updated = ICalComponent.builder(calendar.name)
    for (property in calendar.properties) updated.property(property)
    updated.component(edited.build())
    val written = ICal.writer().encodeToString(updated.build())
    val summary = ICal.parse(written).components
        .firstOrNull()
        ?.components("VEVENT")
        ?.firstOrNull()
        ?.property("SUMMARY")
        ?.value
        .orEmpty()
    val invitation = ICal.parse(INVITATION).calendars.first().events.first()
    val start = invitation.start
    val hour = if (start is ICalDateTime.Utc) start.instant.epochSeconds / 3600 % 24 else -1
    val attendees = invitation.attendees.joinToString { "${it.cn}:${it.partStat}" }
    val series = ICal.parse(INVITATION).calendars.first()
    val week = series.occurrences(Instant.fromEpochSeconds(WEEK_START), Instant.fromEpochSeconds(WEEK_END)).count()
    return "$summary $hour $attendees $week"
}

private const val WEEK_START = 1_788_220_800L
private const val WEEK_END = 1_790_640_000L

// A compile-time constant: building the text at run time (`listOf(...).joinToString`) added code of the test app itself
// to the contribution.
private const val INVITATION =
    "BEGIN:VCALENDAR\r\n" +
        "VERSION:2.0\r\n" +
        "PRODID:-//Calendrum//Size budget//EN\r\n" +
        "METHOD:REQUEST\r\n" +
        "BEGIN:VEVENT\r\n" +
        "UID:size-budget@example.com\r\n" +
        "DTSTAMP:20260901T080000Z\r\n" +
        "DTSTART:20260901T090000Z\r\n" +
        "DTEND:20260901T100000Z\r\n" +
        "SUMMARY:Budget review\r\n" +
        "RRULE:FREQ=WEEKLY;COUNT=4\r\n" +
        "ORGANIZER;CN=Alice:mailto:alice@example.com\r\n" +
        "ATTENDEE;CN=Bob;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION:mailto:bob@example.com\r\n" +
        "END:VEVENT\r\n" +
        "END:VCALENDAR\r\n"

package io.github.calendrum.samples

import io.github.calendrum.ICal
import io.github.calendrum.jcal.ICalSerializers
import io.github.calendrum.jcal.JCal
import io.github.calendrum.model.calendars
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val EVENT = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\nBEGIN:VEVENT\r\n" +
    "UID:42@example.com\r\nDTSTAMP:20260901T000000Z\r\nDTSTART;TZID=Europe/Berlin:20261001T100000\r\n" +
    "SUMMARY:Weekly sync\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

fun jcalRoundTrip() {
    val components = ICal.parse(EVENT).components

    val json = JCal.encodeToString(components)
    val read = JCal.decodeFromString(json)

    assertTrue(json.startsWith("[[\"vcalendar\""))
    assertEquals(emptyList(), read.problems)
    assertEquals(components, read.components)
}

fun jcalProblemsInsteadOfExceptions() {
    val read = JCal.decodeFromString("{ not json")

    assertEquals("ICAL-J001", read.problems.single().code.id)
    assertEquals(emptyList(), read.components)
}

fun jcalSerializersForKotlinxSerialization() {
    val start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")

    val text = Json.encodeToString(ICalSerializers.dateTime, start)

    assertEquals("\"2026-10-01T10:00:00[Europe/Berlin]\"", text)
    assertEquals(start, Json.decodeFromString(ICalSerializers.dateTime, text))
}

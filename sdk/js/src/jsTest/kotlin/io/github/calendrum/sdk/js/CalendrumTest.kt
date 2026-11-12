package io.github.calendrum.sdk.js

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CalendrumTest {
    private val sample = listOf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Calendrum//SDK//EN",
        "BEGIN:VEVENT",
        "UID:daily@example.com",
        "DTSTAMP:20260901T080000Z",
        "DTSTART;TZID=Europe/Berlin:20260907T100000",
        "DURATION:PT1H",
        "RRULE:FREQ=DAILY;COUNT=3",
        "SUMMARY:Daily",
        "END:VEVENT",
        "END:VCALENDAR",
    ).joinToString("\r\n", postfix = "\r\n")

    @Test
    fun parseReturnsJCal() {
        val output = parse(sample)
        assertTrue(output.jcal.startsWith("[[\"vcalendar\""), output.jcal)
        assertEquals(0, output.problems.size)
        assertEquals(1, abiVersion())
    }

    @Test
    fun occurrencesUseTzData() {
        val found = occurrences(sample, "2026-09-01T00:00:00Z", "2026-10-01T00:00:00Z", "Europe/Berlin")
        assertEquals(3, found.size)
        assertEquals("2026-09-07T08:00:00Z", found[0].start)
        assertEquals("2026-09-07T10:00", found[0].localStart)
    }

    @Test
    fun badInstantThrows() {
        assertFailsWith<IllegalArgumentException> { occurrences(sample, "x", "y", null) }
    }
}

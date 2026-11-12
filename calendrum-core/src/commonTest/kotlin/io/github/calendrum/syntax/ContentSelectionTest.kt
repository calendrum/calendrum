package io.github.calendrum.syntax

import io.github.calendrum.syntax.ContentReader.Companion.BEGIN
import io.github.calendrum.syntax.ContentReader.Companion.END
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ContentSelectionTest {
    private val meeting = crlf(
        "BEGIN:VCALENDAR",
        "PRODID:-//RDU Software//NONSGML HandCal//EN",
        "VERSION:2.0",
        "X-WR-CALNAME:Work",
        "BEGIN:VTIMEZONE",
        "TZID:America/New_York",
        "BEGIN:STANDARD",
        "DTSTART:19981025T020000",
        "TZOFFSETFROM:-0400",
        "TZOFFSETTO:-0500",
        "TZNAME:EST",
        "END:STANDARD",
        "BEGIN:DAYLIGHT",
        "DTSTART:19990404T020000",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "TZNAME:EDT",
        "END:DAYLIGHT",
        "END:VTIMEZONE",
        "BEGIN:VEVENT",
        "DTSTAMP:19980309T231000Z",
        "UID:guid-1.example.com",
        "ORGANIZER:mailto:mrbig@example.com",
        "ATTENDEE;RSVP=TRUE;ROLE=REQ-PARTICIPANT;CUTYPE=GROUP:",
        " mailto:employee-A@example.com",
        "DESCRIPTION:Project XYZ Review Meeting",
        "X-MICROSOFT-CDO-BUSYSTATUS:BUSY",
        "SUMMARY:XYZ Project Review",
        "DTSTART;TZID=America/New_York:19980312T083000",
        "DTEND;TZID=America/New_York:19980312T093000",
        "LOCATION:1CP Conference Room 4350",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER:-PT15M",
        "END:VALARM",
        "BEGIN:X-VENDOR",
        "X-DATA:1",
        "END:X-VENDOR",
        "END:VEVENT",
        "BEGIN:VTODO",
        "UID:todo",
        "END:VTODO",
        "END:VCALENDAR",
    )

    private val malformed = listOf(
        crlf("END:VEVENT", "PRODID:x", "BEGIN:VCALENDAR", "BEGIN:VEVENT", "BEGIN:VALARM", "END:VEVENT", "UID:1"),
        crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "END:VTODO", "SUMMARY:s", "END:VEVENT", "END:VCALENDAR", "X:1"),
        crlf("BEGIN:", "BEGIN:X_BAD", "UID:1", "END:x_bad", "END:", "BEGIN;A=1:VEVENT", "summary:x", "END:VEVENT"),
        crlf("BEG", " IN:VCALENDAR", "SUM", " MARY:folded name", "DTSTART", "\t:1", "E", " ND:VCALENDAR"),
        crlf("begin:vcalendar", "begin:vtimezone", "tzid:x", "begin:standard", "end:vcalendar"),
        crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "BEGIN:VALARM", "BEGIN:VLOCATION", "NAME:n", "END:VALARM"),
        crlf("BEGIN:VCALENDAR", "no colon here", "", "UID:é", "SUMMARY;CN=\"open:x", "END:VCALENDAR") + "BEGIN:VEVENT",
        crlf("BEGIN:X-" + "A".repeat(300), "X-" + "Y".repeat(40) + ":v", "END:x-" + "a".repeat(300)),
        "BEGIN:VCALENDAR\nUID:1\rBEGIN:VEVENT\n\tSUMMARY:x\r\nEND:VEVENT\nEND:VCALENDAR",
    )

    private val inputs: List<ByteArray> = listOf(meeting.encodeToByteArray()) +
        malformed.map { it.encodeToByteArray() } +
        listOf(
            "BEGIN:VCALENDAR\r\nDESCRIPTION:".encodeToByteArray() +
                bytesOf(0xC3, 0x28, 0xFF) + "\r\nUID:1\r\nEND:VCALENDAR\r\n",
        )

    @Test
    fun presetsMatchTheFilteredFullStream() {
        for (input in inputs) {
            for (mask in Mask.fixed) assertSelectionMatchesFilter(input, mask)
        }
    }

    @Test
    fun randomMasksMatchTheFilteredFullStream() {
        val random = TestRandom(27)
        for (input in inputs) {
            repeat(40) { assertSelectionMatchesFilter(input, Mask.random(random)) }
        }
    }

    @Test
    fun randomInputsWithRandomMasksMatchTheFilteredFullStream() {
        val random = TestRandom(5546)
        repeat(1_500) {
            val input = randomStructuredInput(random)
            val limits = limits(maxDepth = 1 + random.nextInt(5), maxComponents = 1 + random.nextInt(40))
            assertSelectionMatchesFilter(input, Mask.random(random), limits)
        }
    }

    @Test
    fun schedulePresetKeepsOnlyWhatItNeeds() {
        val result = read(meeting, Mask.schedule.selection())
        val components = result.events.filter { it.kind == BEGIN }.map { it.componentName }
        assertEquals(listOf("VCALENDAR", "VTIMEZONE", "STANDARD", "DAYLIGHT", "VEVENT"), components)
        val properties = result.events.mapNotNull { it.property?.name }
        assertEquals(
            listOf(
                "TZID", "DTSTART", "TZOFFSETFROM", "TZOFFSETTO", "DTSTART", "TZOFFSETFROM", "TZOFFSETTO", "UID",
                "DESCRIPTION", "SUMMARY", "DTSTART", "DTEND", "LOCATION",
            ),
            properties,
        )
        assertEquals(3, result.reader.skippedComponents)
        assertEquals(13, result.reader.skippedLines)
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun calendarIsAlwaysAContainer() {
        val result = read(meeting, Mask.nothing.selection())
        assertEquals(listOf(BEGIN, END), result.kinds)
        assertEquals(listOf("VCALENDAR", "VCALENDAR"), result.events.map { it.componentName })
        assertEquals(meeting.length, result.events.last().componentEnd)
    }

    @Test
    fun unknownFlagsCoverUnknownAndXNames() {
        val result = read(meeting, Mask.onlyUnknown.selection())
        assertEquals(listOf("VCALENDAR", "VCALENDAR"), result.names())
        val inEvent = read(meeting, Mask(setOf("VEVENT"), emptySet(), true, true).selection())
        assertEquals(
            listOf(
                "VCALENDAR",
                "VEVENT",
                "X-MICROSOFT-CDO-BUSYSTATUS",
                "X-VENDOR",
                "X-DATA",
                "X-VENDOR",
                "VEVENT",
                "VCALENDAR",
            ),
            inEvent.names(),
        )
    }

    @Test
    fun skippedLinesAreNotDiagnosed() {
        val bytes = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "SUMMARY;CN=\"open:x", "UID:1").encodeToByteArray() +
            "DESCRIPTION:bad ".encodeToByteArray() + bytesOf(0xC3, 0x28) + crlf("", "END:VEVENT", "END:VCALENDAR")
        val full = read(bytes)
        assertTrue("ICAL-S006" in full.codes && "ICAL-S009" in full.codes, full.codes.toString())
        val masked = read(bytes, Mask(setOf("VEVENT"), setOf("UID"), false, false).selection())
        assertEquals(emptyList(), masked.problems)
        assertEquals(listOf("UID"), masked.events.mapNotNull { it.property?.name })
        assertEquals(2, masked.reader.skippedLines)
    }

    @Test
    fun skippedComponentsAreNotDiagnosedInside() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VTODO",
            "SUMMARY;CN=\"open:x",
            "no colon",
            "END:VTODO",
            "END:VCALENDAR",
        )
        val full = read(input)
        assertEquals(listOf("ICAL-S009", "ICAL-S007"), full.codes)
        val masked = read(input, Mask.schedule.selection())
        assertEquals(emptyList(), masked.problems)
        assertEquals(1, masked.reader.skippedComponents)
        assertEquals(2, masked.reader.skippedLines)
    }

    @Test
    fun fullSelectionSkipsNothing() {
        val result = read(meeting)
        assertEquals(0, result.reader.skippedLines)
        assertEquals(0, result.reader.skippedComponents)
    }

    @Test
    fun builderAcceptsOnlyKnownNamesOfTheRightKind() {
        assertFailsWith<IllegalArgumentException> { ContentSelection.Builder().components("X-VENDOR") }
        assertFailsWith<IllegalArgumentException> { ContentSelection.Builder().components("SUMMARY") }
        assertFailsWith<IllegalArgumentException> { ContentSelection.Builder().properties("VEVENT") }
        assertFailsWith<IllegalArgumentException> { ContentSelection.Builder().properties("CN") }
        assertFailsWith<IllegalArgumentException> { ContentSelection.Builder().properties("BEGIN") }
        assertFailsWith<IllegalArgumentException> { ContentSelection.Builder().properties("END") }
        val selection = ContentSelection.Builder().components("vevent").properties("tzid", "Summary").build()
        assertTrue(selection.includesComponent(KnownNames.lookup("VEVENT")))
        assertTrue(selection.includesProperty(KnownNames.lookup("TZID")))
        assertTrue(!selection.includesProperty(KnownNames.lookup("CN")))
        assertTrue(!selection.includesProperty(KnownNames.UNKNOWN))
        assertTrue(!selection.allProperties)
        assertTrue(ContentSelection.Full.allProperties)
    }

    @Test
    fun allPropertiesMeansNoLexerSkipping() {
        val builder = ContentSelection.Builder().components("VEVENT").unknownProperties(true)
        builder.properties(*Mask.propertyNames.toTypedArray())
        val selection = builder.build()
        assertTrue(selection.allProperties)
        assertSelectionMatchesFilter(
            meeting.encodeToByteArray(),
            Mask(setOf("VEVENT"), Mask.propertyNames.toSet(), false, true),
        )
    }
}

@file:OptIn(InternalICalApi::class)

package io.github.calendrum.syntax

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource
import kotlin.time.TimeSource

class SelectionTest {
    private val input = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Example//Selection//EN",
        "X-WR-TIMEZONE:Europe/Berlin",
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Berlin",
        "BEGIN:STANDARD",
        "DTSTART:19701025T030000",
        "TZOFFSETFROM:+0200",
        "TZOFFSETTO:+0100",
        "END:STANDARD",
        "END:VTIMEZONE",
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Paris",
        "BEGIN:STANDARD",
        "DTSTART:19701025T030000",
        "TZOFFSETFROM:+0200",
        "TZOFFSETTO:+0100",
        "END:STANDARD",
        "END:VTIMEZONE",
        "BEGIN:VTIMEZONE",
        "TZID:Asia/Tokyo",
        "BEGIN:STANDARD",
        "DTSTART:19700101T000000",
        "TZOFFSETFROM:+0900",
        "TZOFFSETTO:+0900",
        "END:STANDARD",
        "END:VTIMEZONE",
        "BEGIN:VEVENT",
        "UID:1",
        "DTSTAMP:20260901T080000Z",
        "DTSTART;TZID=Europe/Berlin:20261001T100000",
        "RRULE:FREQ=WEEKLY;COUNT=3",
        "SUMMARY:Lecture",
        "x-acme-room:101",
        "X-OTHER:x",
        "ATTENDEE;CN=A:mailto:a@example.com",
        "ORGANIZER:mailto:o@example.com",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER:-PT15M",
        "DESCRIPTION:Soon",
        "END:VALARM",
        "END:VEVENT",
        "BEGIN:VEVENT",
        "UID:1",
        "RECURRENCE-ID;TZID=Europe/Berlin:20261008T100000",
        "DTSTART:20261008T110000Z",
        "EXDATE;TZID=Europe/Paris:20261015T100000",
        "TRANSP:TRANSPARENT",
        "END:VEVENT",
        "BEGIN:VTODO",
        "UID:3",
        "SUMMARY:Task",
        "BEGIN:VALARM",
        "ACTION:AUDIO",
        "TRIGGER:-PT5M",
        "END:VALARM",
        "END:VTODO",
        "BEGIN:VFREEBUSY",
        "UID:4",
        "FREEBUSY:20261001T100000Z/PT1H",
        "END:VFREEBUSY",
        "BEGIN:X-CUSTOM",
        "UID:5",
        "X-OTHER:y",
        "END:X-CUSTOM",
        "END:VCALENDAR",
    )

    private val full = ICal.parse(input)

    private fun select(selection: Selection): ParseResult {
        val result = ICal.parser { select(selection) }.parse(input)
        assertEquals(filterSelection(full.components, selection), result.components, selection.toString())
        return result
    }

    private fun shape(component: ICalComponent): String = buildString {
        append(component.name).append('[')
        append(component.properties.joinToString(",") { it.name })
        for (child in component.components) append(' ').append(shape(child))
        append(']')
    }

    @Test
    fun theProposalSelectionReadsOnlyWhatItNames() {
        val parser = ICal.parser {
            select {
                components("VEVENT")
                properties(
                    Properties.UID,
                    Properties.DTSTART,
                    Properties.DTEND,
                    Properties.SUMMARY,
                    Properties.ATTENDEE,
                )
                properties("X-ACME-ROOM")
                need(Feature.Occurrences)
                timeZones = TimeZoneSelection.Referenced
            }
        }
        val result = parser.parse(input)
        assertTrue(result.isPartial)
        assertEquals(
            "VCALENDAR[" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VEVENT[UID,DTSTART,RRULE,SUMMARY,X-ACME-ROOM,ATTENDEE]" +
                " VEVENT[UID,RECURRENCE-ID,DTSTART,EXDATE]]",
            shape(result.components.single()),
        )
        assertEquals(
            listOf("Europe/Berlin", "Europe/Paris"),
            result.calendars.single().timeZones.map { it.tzid },
        )
        assertEquals(filterSelection(full.components, parser.options.selection), result.components)
        assertFalse(full.isPartial)
    }

    @Test
    fun presetsKeepTheirParts() {
        assertEquals(
            "VCALENDAR[X-WR-TIMEZONE" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VEVENT[UID,DTSTART,RRULE,SUMMARY,ATTENDEE,ORGANIZER]" +
                " VEVENT[UID,RECURRENCE-ID,DTSTART,EXDATE]]",
            shape(select(Selection.Schedule).components.single()),
        )
        assertEquals(
            "VCALENDAR[" +
                " VEVENT[UID,DTSTAMP VALARM[]]" +
                " VEVENT[UID,RECURRENCE-ID]" +
                " VTODO[UID VALARM[]]" +
                " VFREEBUSY[UID]" +
                " X-CUSTOM[UID]]",
            shape(select(Selection.SyncMetadata).components.single()),
        )
        assertEquals(
            "VCALENDAR[" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VEVENT[UID,DTSTART,RRULE]" +
                " VEVENT[UID,RECURRENCE-ID,DTSTART,EXDATE,TRANSP]" +
                " VFREEBUSY[UID,FREEBUSY]]",
            shape(select(Selection.FreeBusy).components.single()),
        )
        val everything = select(Selection.Full)
        assertFalse(everything.isPartial)
        assertEquals(full.components, everything.components)
        assertEquals(TimeZoneSelection.Referenced, Selection.Schedule.timeZones)
        assertEquals(TimeZoneSelection.None, Selection.SyncMetadata.timeZones)
        assertEquals("Selection(preset=Schedule)", Selection.Schedule.toString())
    }

    @Test
    fun featuresBringWhatTheyNeed() {
        val alarms = select(Selection.builder().components("VTODO").properties("UID").need(Feature.Alarms).build())
        assertEquals(
            "VCALENDAR[" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VTIMEZONE[TZID STANDARD[DTSTART,TZOFFSETFROM,TZOFFSETTO]]" +
                " VTODO[UID VALARM[ACTION,TRIGGER]]]",
            shape(alarms.components.single()),
        )
        val people = select(
            Selection.builder().components(
                "VEVENT",
            ).need(Feature.Participants).timeZones(TimeZoneSelection.None).build(),
        )
        assertEquals("VCALENDAR[ VEVENT[ATTENDEE,ORGANIZER] VEVENT[]]", shape(people.components.single()))
        val occurrences = Selection.builder().components("VEVENT").need(Feature.Occurrences)
            .timeZones(TimeZoneSelection.None).build()
        assertEquals(TimeZoneSelection.Referenced, occurrences.timeZones)
        assertEquals(2, select(occurrences).calendars.single().timeZones.size)
        assertEquals("Occurrences", Feature.Occurrences.toString())
        assertEquals("Alarms Participants", "${Feature.Alarms} ${Feature.Participants}")
    }

    @Test
    fun unknownNamesAreMatchedExactly() {
        val result = select(
            Selection.builder().components("x-custom", "VEVENT").properties("X-ACME-ROOM", "x-other")
                .timeZones(TimeZoneSelection.None).build(),
        )
        assertEquals(
            "VCALENDAR[ VEVENT[X-ACME-ROOM,X-OTHER] VEVENT[] X-CUSTOM[X-OTHER]]",
            shape(result.components.single()),
        )
        val noUnknown = select(Selection.builder().components("VTODO").properties("SUMMARY").build())
        assertEquals(4, noUnknown.components.single().components.size)
    }

    @Test
    fun unknownNamesAreSkippedWithoutBeingRead() {
        val text = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "UID:1",
            "x-microsoft-cdo-busystatus:BUSY",
            "X-MICROSOFT-CDO-BUSYSTATUS-AND-A-NAME-LONGER-THAN-ANY-KNOWN-ONE:1",
            "X-ACME:2",
            "X-ACM:3",
            "X-ACME-ROOMS:4",
            "x-acme-room;X=1:5",
            "X-ACME-ROOM",
            " :6",
            "X-ACME-R",
            " OOM:7",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        for (names in listOf(
            arrayOf("X-ACME-ROOM"),
            arrayOf("X-MICROSOFT-CDO-BUSYSTATUS"),
            arrayOf("X-MICROSOFT-CDO-BUSYSTATUS-AND-A-NAME-LONGER-THAN-ANY-KNOWN-ONE", "x-acm"),
        )) {
            val selection = Selection.builder().components("VEVENT").properties(*names).build()
            val result = ICal.parser { select(selection) }.parse(text)
            val filtered = filterSelection(ICal.parse(text).components, selection)
            assertEquals(filtered, result.components, names.toList().toString())
            val expected = when (names[0]) {
                "X-ACME-ROOM" -> listOf("5", "6", "7")
                "X-MICROSOFT-CDO-BUSYSTATUS" -> listOf("BUSY")
                else -> listOf("1", "3")
            }
            assertEquals(expected, result.components.single().components.single().properties.map { it.value })
        }
    }

    @Test
    fun zonesWithoutTzidAreNotKeptByReference() {
        val text = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VTIMEZONE",
            "BEGIN:STANDARD",
            "DTSTART:19700101T000000",
            "TZOFFSETTO:+0000",
            "END:STANDARD",
            "END:VTIMEZONE",
            "BEGIN:VTIMEZONE",
            "TZID:A",
            "END:VTIMEZONE",
            "BEGIN:VEVENT",
            "DTSTART;TZID=A:20260101T000000",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val result = ICal.parser { select(Selection.Schedule) }.parse(text)
        assertEquals(listOf("VTIMEZONE", "VEVENT"), result.components.single().components.map { it.name })
        assertEquals("A", result.components.single().components[0].property("TZID")?.value)
    }

    @Test
    fun wrongNamesAreRejected() {
        assertFailsWith<IllegalArgumentException> { Selection.builder().components("VTIMEZONE") }
        assertFailsWith<IllegalArgumentException> { Selection.builder().components("standard") }
        assertFailsWith<IllegalArgumentException> { Selection.builder().components("SUMMARY") }
        assertFailsWith<IllegalArgumentException> { Selection.builder().components("bad name") }
        assertFailsWith<IllegalArgumentException> { Selection.builder().properties("BEGIN") }
        assertFailsWith<IllegalArgumentException> { Selection.builder().properties("VEVENT") }
        assertFailsWith<IllegalArgumentException> { Selection.builder().properties("") }
        val builder = Selection.builder().components("VEVENT").properties("UID")
        assertEquals("Selection.Builder(components=1, properties=1)", builder.toString())
        assertEquals("Selection(components=1, properties=1, timeZones=All)", builder.build().toString())
    }

    @Test
    fun aPartialTreeIsWrittenOnlyWhenAllowed() {
        val parser = ICal.parser { select(Selection.Schedule) }
        val result = parser.parse(input)
        val message = assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(result.components) }.message
        assertEquals("A component from a selective parse is written only with WriteOptions.allowPartialWrite", message)
        val calendar = result.calendars.single()
        assertFailsWith<IllegalStateException> { ICal.writer().encodeToByteArray(calendar.component) }
        assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(calendar.events.first().component) }
        val edited = calendar.edit { events { for (event in items) replace(event, event.edit { summary = "x" }) } }
        assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(edited.component) }
        val mixed = VCalendar.builder().prodId("p").event(calendar.events.first()).build()
        assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(mixed.component) }
        val stream = parser.components(input.encodeToByteArray())
        val streamed = stream.next()
        assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(streamed) }
        val reader = parser.reader(input)
        while (reader.next() != ContentEventType.Begin || reader.name != "VEVENT") continue
        val read = reader.readComponent()
        assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(read) }
        val allowed = ICal.writer(WriteOptions.builder().allowPartialWrite(true).build())
        val written = allowed.encodeToString(result.components)
        assertEquals(result.components.single(), ICal.parse(written).components.single())
        assertTrue(allowed.toString().contains("allowPartialWrite=true"))
        assertEquals(input, ICal.writer().encodeToString(full.components))
    }

    @Test
    fun referencedTimeZonesAreKeptByTheTreeOnly() {
        val parser = ICal.parser { select(Selection.Schedule) }
        val streamed = parser.components(input.encodeToByteArray()).asSequence().toList()
        assertEquals(3, streamed.count { it.name == "VTIMEZONE" })
        val reader = parser.reader(input)
        var zones = 0
        while (true) {
            if ((reader.next() ?: break) == ContentEventType.Begin && reader.name == "VTIMEZONE") zones += 1
        }
        assertEquals(3, zones)
        val noZones = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:1", "END:VEVENT", "END:VCALENDAR")
        assertEquals(ICal.parse(noZones).components, parser.parse(noZones).components)
    }

    @Test
    fun optionsCarryTheSelection() {
        assertSame(Selection.Full, ParseOptions.Default.selection)
        val options = ParseOptions.builder().select(Selection.SyncMetadata).build()
        assertSame(Selection.SyncMetadata, options.selection)
        assertTrue(options.toString().endsWith("selection=Selection(preset=SyncMetadata), registry=default)"))
        assertTrue(ParseOptions.Default.toString().endsWith("selection=full, registry=default)"))
    }

    @Test
    fun untrustedInputsMeetTightLimits() {
        val untrusted = ParseOptions.Untrusted
        assertSame(untrusted, ParseOptions.Untrusted)
        assertEquals(ParseMode.Lenient, untrusted.mode)
        assertEquals(64 * 1024, untrusted.limits.maxLineOctets)
        assertEquals(1_000_000, untrusted.limits.maxLines)
        assertEquals(8, untrusted.limits.maxDepth)
        assertEquals(100_000, untrusted.limits.maxComponents)
        assertEquals(1_000, untrusted.limits.maxProblems)
        assertEquals(2.seconds, untrusted.limits.maxTime)
        val parser = ICal.parser(untrusted)
        val deep = buildString {
            repeat(12) { append("BEGIN:X-LEVEL\r\n") }
            repeat(12) { append("END:X-LEVEL\r\n") }
        }
        val nested = parser.parse(deep)
        assertEquals(1, nested.problemCount(ProblemCode.TooDeep))
        var depth = 0
        var node: ICalComponent? = nested.components.single()
        while (node != null) {
            depth += 1
            node = node.components.firstOrNull()
        }
        assertEquals(8, depth)
        val long = crlf("BEGIN:VCALENDAR", "X-BIG:" + "a".repeat(70_000), "END:VCALENDAR")
        val skipped = parser.parse(long)
        assertEquals(1, skipped.problemCount(ProblemCode.LineTooLong))
        assertEquals(0, skipped.components.single().properties.size)
        val many = buildString {
            append("BEGIN:VCALENDAR\r\n")
            repeat(100_001) { append("BEGIN:VEVENT\r\nEND:VEVENT\r\n") }
            append("END:VCALENDAR\r\n")
        }
        val truncated = parser.parse(many)
        assertTrue(truncated.isTruncated)
        assertEquals(1, truncated.problemCount(ProblemCode.TooManyComponents))
        val clock = object : TimeSource.WithComparableMarks {
            private val source = TestTimeSource()

            override fun markNow(): ComparableTimeMark {
                val mark = source.markNow()
                source += 1.seconds
                return mark
            }
        }
        val slow = ICal.parser(ParseOptions.builder().limits(untrusted.limits).timeSource(clock).build()).parse(many)
        assertEquals(1, slow.problemCount(ProblemCode.TimeBudgetExceeded))
        assertTrue(slow.components.single().components.size < 100_000)
    }
}

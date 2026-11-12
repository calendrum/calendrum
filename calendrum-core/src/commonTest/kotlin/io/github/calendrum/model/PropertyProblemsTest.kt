@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class PropertyProblemsTest {
    @Test
    fun dateTimeWithoutTimeIsReadAsADate() {
        val date = assertLenient(lenient(Properties.DTSTART, "DTSTART:20260901"), "ICAL-V008@2:17")
        assertIs<ICalDateTime.Date>(date)
        assertMalformed(strict(Properties.DTSTART, "DTSTART:20260901"), "ICAL-V008@2:17", "ICAL-V001@2:17")
    }

    @Test
    fun valueProblemsPointInsideTheValue() {
        assertMalformed(lenient(Properties.DTSTART, "DTSTART:20261301T100000Z"), "ICAL-V002@2:13")
        assertMalformed(lenient(Properties.DURATION, "DURATION:PT1X"), "ICAL-V001@2:13")
        assertLenient(lenient(Properties.DURATION, "DURATION:P90M"), "ICAL-V009@2:13")
        assertMalformed(lenient(Properties.RRULE, "RRULE:COUNT=2"), "ICAL-V003@2:7")
        assertLenient(lenient(Properties.SUMMARY, "SUMMARY:a\\qb"), "ICAL-V023@2:10")
        assertMalformed(strict(Properties.SUMMARY, "SUMMARY:a\\qb"), "ICAL-V023@2:10", "ICAL-V001@2:10")
        assertLenient(lenient(Properties.DTSTART, "DTSTART;TZID=Europe/Berlin:20260901T100000Z"), "ICAL-V024@2:43")
    }

    @Test
    fun positionsCountUtf8BytesAndFolding() {
        assertLenient(lenient(Properties.SUMMARY, "SUMMARY:Привет\\q"), "ICAL-V023@2:21")
        assertLenient(lenient(Properties.SUMMARY, "SUMMARY:😀x\\q"), "ICAL-V023@2:14")
        assertLenient(lenient(Properties.DESCRIPTION, "DESCRIPTION:abc\r\n de\\qf"), "ICAL-V023@3:4")
        assertLenient(lenient(Properties.DESCRIPTION, "DESCRIPTION:ab\r\n\tc\r\n d\\q"), "ICAL-V023@4:3")
        assertLenient(lenient(Properties.DESCRIPTION, "DESCRIPTION:\r\n \\q"), "ICAL-V023@3:2")
        val folded = lenient(Properties.SUMMARY, "SUMMARY:Пр\r\n ивет\\q")
        assertEquals(listOf("ICAL-V023@3:10"), folded.codes())
        val problem = assertIs<Decoded.Value<String>>(folded).problems.single()
        assertEquals(problem.span.startOffset, problem.span.endOffset)
        assertEquals(14L + 8 + 4 + 2 + 1 + 8, problem.span.startOffset)
    }

    @Test
    fun positionsAreTheSameWithoutTheSource() {
        val lines = listOf(
            "SUMMARY:Пр\r\n ивет\\q",
            "DTSTART;TZID=Europe/Berlin:20260901T100000Z",
            "ATTENDEE;RSVP=MAYBE:mailto:a@example.com",
        )
        for (line in lines) {
            val retained = propertyOf(line)
            val copied = propertyOf(line, retainSource = false)
            for (key in listOf<PropertyKey<*>>(Properties.SUMMARY, Properties.DTSTART, Properties.ATTENDEE)) {
                if (key.name != retained.name) continue
                val left = key.codec.decode(retained, DecodeContext.Lenient)
                val right = key.codec.decode(copied, DecodeContext.Lenient)
                assertEquals(spans(left), spans(right), line)
            }
        }
    }

    private fun spans(decoded: Decoded<*>): List<String> = when (decoded) {
        is Decoded.Value -> decoded.problems.map { "${it.code.id}@${it.span}" }
        is Decoded.Malformed -> listOf("${decoded.problem.code.id}@${decoded.problem.span}")
        Decoded.Missing -> emptyList()
    }

    @Test
    fun problemsOfLaterLinesHaveTheirOwnPositions() {
        val text = "BEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260901T100000\r\nEND:VEVENT\r\n"
        val event = ICal.parse(text).components.single()
        val decoded = event.decode(Properties.DTSTAMP)
        assertEquals(listOf("ICAL-V027@3:9"), decoded.codes())
        val problem = assertIs<Decoded.Value<ICalDateTime>>(decoded).problems.single()
        assertEquals(29L, problem.span.startOffset)
        assertSame(ProblemCode.NotUtc, problem.code)
        assertEquals("ICAL-V027 Warning: value is not in UTC at 3:9 (offset 29)", problem.toString())
    }

    @Test
    fun utcIsRequiredWhereTheRfcSaysSo() {
        assertLenient(lenient(Properties.DTSTAMP, "DTSTAMP:20260901T100000"), "ICAL-V027@2:9")
        assertMalformed(strict(Properties.DTSTAMP, "DTSTAMP:20260901T100000"), "ICAL-V027@2:9", "ICAL-V001@2:9")
        assertLenient(lenient(Properties.COMPLETED, "COMPLETED:20260901T100000", "VTODO"), "ICAL-V027@2:11")
        assertLenient(lenient(Properties.CREATED, "CREATED;TZID=X:20260901T100000Z"), "ICAL-V028@2:14")
        assertLenient(
            lenient(Properties.FREEBUSY, "FREEBUSY:19970308T160000/PT8H30M", "VFREEBUSY"),
            "ICAL-V027@2:10",
        )
        assertLenient(
            lenient(Properties.TRIGGER, "TRIGGER;VALUE=DATE-TIME:19980101T050000", "VALARM"),
            "ICAL-V027@2:25",
        )
    }

    @Test
    fun triggerDateTimeWithoutValueTypeIsAccepted() {
        val trigger = assertLenient(lenient(Properties.TRIGGER, "TRIGGER:19980101T050000Z", "VALARM"), "ICAL-V026@2:9")
        assertIs<Trigger.Absolute>(trigger)
        assertMalformed(
            strict(Properties.TRIGGER, "TRIGGER:19980101T050000Z", "VALARM"),
            "ICAL-V026@2:9",
            "ICAL-V001@2:9",
        )
        val related = lenient(Properties.TRIGGER, "TRIGGER;RELATED=END;VALUE=DATE-TIME:19980101T050000Z", "VALARM")
        assertLenient(related, "ICAL-V028@2:17")
        assertMalformed(lenient(Properties.TRIGGER, "TRIGGER:-PT15X", "VALARM"), "ICAL-V001@2:14")
    }

    @Test
    fun valueTypesThePropertyDoesNotTake() {
        val text = assertLenient(lenient(Properties.DTSTART, "DTSTART;VALUE=TEXT:20260901T100000Z"), "ICAL-V025@2:15")
        assertIs<ICalDateTime.Utc>(text)
        assertMalformed(
            strict(Properties.DTSTART, "DTSTART;VALUE=TEXT:20260901T100000Z"),
            "ICAL-V025@2:15",
            "ICAL-V001@2:15",
        )
        assertLenient(lenient(Properties.SUMMARY, "SUMMARY;VALUE=URI:x"), "ICAL-V025@2:15")
        assertLenient(lenient(Properties.SUMMARY, "SUMMARY;VALUE=:x"), "ICAL-V025@2:15")
        assertLenient(lenient(Properties.DTSTART, "DTSTART;VALUE=date:20260901"))
        val dateWithZone = lenient(Properties.DTSTART, "DTSTART;VALUE=DATE;TZID=Europe/Berlin:20260901")
        assertIs<ICalDateTime.Date>(assertLenient(dateWithZone, "ICAL-V028@2:25"))
        assertMalformed(
            strict(Properties.EXDATE, "EXDATE;VALUE=DATE;TZID=Europe/Berlin:20260901"),
            "ICAL-V028@2:24",
            "ICAL-V001@2:24",
        )
    }

    @Test
    fun undeclaredValueTypes() {
        val binary = assertLenient(lenient(Properties.ATTACH, "ATTACH;ENCODING=BASE64:QUJD"), "ICAL-V026@2:24")
        assertEquals("ABC", assertIs<Attach.Binary>(binary).bytes().decodeToString())
        val encoding = assertLenient(lenient(Properties.ATTACH, "ATTACH;VALUE=BINARY:QUJD"), "ICAL-V026@2:21")
        assertIs<Attach.Binary>(encoding)
        assertMalformed(strict(Properties.ATTACH, "ATTACH;VALUE=BINARY:QUJD"), "ICAL-V026@2:21", "ICAL-V001@2:21")
        val sound = assertLenient(lenient(Properties.ATTACH, "ATTACH;VALUE=URI:Chord"), "ICAL-V021@2:18")
        assertEquals("Chord", assertIs<Attach.Uri>(sound).uri)
        assertLenient(lenient(Properties.ATTACH, "ATTACH;ENCODING=BASE64;VALUE=BINARY:QUI"), "ICAL-V015@2:40")
        val periods = assertLenient(lenient(Properties.RDATE, "RDATE:19960403T020000Z/PT2H"), "ICAL-V026@2:7")
        assertEquals(1, periods.periods.size)
        assertMalformed(strict(Properties.RDATE, "RDATE:19960403T020000Z/PT2H"), "ICAL-V026@2:7", "ICAL-V001@2:7")
    }

    @Test
    fun listsKeepTheGoodItemsInLenientMode() {
        val dates = lenient(Properties.EXDATE, "EXDATE:20260901T100000Z,20261301T100000Z,20260903T100000Z")
        assertEquals(2, assertLenient(dates, "ICAL-V002@2:29").dates.size)
        assertMalformed(
            strict(Properties.EXDATE, "EXDATE:20260901T100000Z,20261301T100000Z,20260903T100000Z"),
            "ICAL-V002@2:29",
        )
    }

    @Test
    fun structuredValues() {
        assertMalformed(lenient(Properties.GEO, "GEO:37.386013"), "ICAL-V001@2:14")
        assertMalformed(lenient(Properties.GEO, "GEO:91;0"), "ICAL-V002@2:5")
        assertMalformed(lenient(Properties.GEO, "GEO:1;181"), "ICAL-V002@2:7")
        assertMalformed(lenient(Properties.GEO, "GEO:1;x"), "ICAL-V001@2:7")
        assertMalformed(lenient(Properties.GEO, "GEO:1e3;2"), "ICAL-V001@2:6")
        assertMalformed(lenient(Properties.REQUEST_STATUS, "REQUEST-STATUS:2;Success"), "ICAL-V001@2:16")
        assertMalformed(lenient(Properties.REQUEST_STATUS, "REQUEST-STATUS:2.0"), "ICAL-V001@2:19")
        assertMalformed(lenient(Properties.REQUEST_STATUS, "REQUEST-STATUS:2.0.0.0;x"), "ICAL-V001@2:16")
        assertLenient(
            lenient(Properties.REQUEST_STATUS, "REQUEST-STATUS:2.0;a\\x;b\\y"),
            "ICAL-V023@2:21",
            "ICAL-V023@2:25",
        )
    }

    @Test
    fun badParametersDoNotLoseTheProperty() {
        val attendee =
            assertLenient(lenient(Properties.ATTENDEE, "ATTENDEE;RSVP=MAYBE:mailto:a@example.com"), "ICAL-V001@2:15")
        assertEquals(false, attendee.rsvp)
        assertMalformed(strict(Properties.ATTENDEE, "ATTENDEE;RSVP=MAYBE:mailto:a@example.com"), "ICAL-V001@2:15")
        val empty =
            assertLenient(
                lenient(Properties.ATTENDEE, "ATTENDEE;ROLE=;PARTSTAT=:mailto:a@example.com"),
                "ICAL-V001@2:15",
                "ICAL-V001@2:25",
            )
        assertSame(Role.ReqParticipant, empty.role)
        assertSame(PartStat.NeedsAction, empty.partStat)
        assertEquals(true, value(Properties.ATTENDEE, "ATTENDEE;RSVP=true:mailto:a@example.com").rsvp)
        val bare = assertLenient(lenient(Properties.ATTENDEE, "ATTENDEE:jane@example.com"), "ICAL-V021@2:10")
        assertEquals("jane@example.com", bare.address)
        assertMalformed(lenient(Properties.ATTENDEE, "ATTENDEE:"), "ICAL-V001@2:10")
    }

    @Test
    fun unknownParametersAreKept() {
        val attendee = value(Properties.ATTENDEE, "ATTENDEE;X-FOO=bar;CN=Jane;X-NUM=1,2:mailto:jane@example.com")
        assertEquals(listOf("X-FOO", "CN", "X-NUM"), attendee.parameters.map { it.name })
        assertEquals(listOf("1", "2"), attendee.parameters[2].values)
        val encoded = encoded(Properties.ATTENDEE, attendee)
        assertEquals(propertyOf("ATTENDEE;X-FOO=bar;CN=Jane;X-NUM=1,2:mailto:jane@example.com"), encoded)
        val location = value(Properties.LOCATION, "LOCATION;X-APPLE-ID=7;LANGUAGE=de:Raum")
        assertEquals("X-APPLE-ID", location.parameters[0].name)
        assertEquals("de", location.language)
    }

    @Test
    fun builtPropertiesHaveNoSourcePosition() {
        val property = ICalProperty.of("DTSTAMP", "20260901T100000")
        val decoded = Properties.DTSTAMP.codec.decode(property, DecodeContext.Lenient)
        val problem = assertIs<Decoded.Value<ICalDateTime>>(decoded).problems.single()
        assertEquals(0, problem.span.line)
        assertEquals(0, problem.span.column)
        assertEquals(0L, problem.span.startOffset)
        val text = ICalProperty.of("SUMMARY", "ab\\q")
        val escape = assertIs<Decoded.Value<String>>(Properties.SUMMARY.codec.decode(text, DecodeContext.Lenient))
        assertEquals(2L, escape.problems.single().span.startOffset)
        val parameter = ICalProperty.builder(
            "ATTENDEE",
        ).parameter("RSVP", "maybe").value("mailto:a@example.com").build()
        val attendee = Properties.ATTENDEE.codec.decode(parameter, DecodeContext.Lenient)
        assertEquals(listOf("ICAL-V001@0:0"), attendee.codes())
    }

    @Test
    fun interceptedLinesPointAtTheirStart() {
        val parser = ICal.parser { intercept { line -> if (line.name == "X-START") line.rename("DTSTART") } }
        val event = parser.parse("BEGIN:VEVENT\r\nX-START:20260901\r\nEND:VEVENT\r\n").components.single()
        assertEquals(listOf("ICAL-V008@2:1"), event.decode(Properties.DTSTART).codes())
    }
}

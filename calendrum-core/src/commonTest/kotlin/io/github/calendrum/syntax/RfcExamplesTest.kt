package io.github.calendrum.syntax

import kotlin.test.Test
import kotlin.test.assertEquals

class RfcExamplesTest {
    @Test
    fun rfc5545GroupMeeting() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "PRODID:-//RDU Software//NONSGML HandCal//EN",
            "VERSION:2.0",
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
            "CATEGORIES:MEETING",
            "CLASS:PUBLIC",
            "CREATED:19980309T130000Z",
            "SUMMARY:XYZ Project Review",
            "DTSTART;TZID=America/New_York:19980312T083000",
            "DTEND;TZID=America/New_York:19980312T093000",
            "LOCATION:1CP Conference Room 4350",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val result = lex(input)
        assertEquals(emptyList(), result.problems)
        assertEquals(33, result.lines.size)
        val attendee = result.lines.single { it.name == "ATTENDEE" }
        assertEquals(
            listOf(
                Param("RSVP", listOf("TRUE")),
                Param("ROLE", listOf("REQ-PARTICIPANT")),
                Param("CUTYPE", listOf("GROUP")),
            ),
            attendee.parameters,
        )
        assertEquals("mailto:employee-A@example.com", attendee.value)
        assertEquals(23, attendee.line)
        assertEquals(24, attendee.endLine)
        val dtStart = result.lines.last { it.name == "DTSTART" }
        assertEquals(listOf(Param("TZID", listOf("America/New_York"))), dtStart.parameters)
        assertEquals("19980312T083000", dtStart.value)
    }

    @Test
    fun rfc5545ConferenceEvent() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "PRODID:-//xyz Corp//NONSGML PDA Calendar Version 1.0//EN",
            "VERSION:2.0",
            "BEGIN:VEVENT",
            "DTSTAMP:19960704T120000Z",
            "UID:uid1@example.com",
            "ORGANIZER:mailto:jsmith@example.com",
            "DTSTART:19960918T143000Z",
            "DTEND:19960920T220000Z",
            "STATUS:CONFIRMED",
            "CATEGORIES:CONFERENCE",
            "SUMMARY:Networld+Interop Conference",
            "DESCRIPTION:Networld+Interop Conference",
            "  and Exhibit\\nAtlanta World Congress Center\\n",
            " Atlanta\\, Georgia",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val result = lex(input)
        assertEquals(emptyList(), result.problems)
        assertEquals(15, result.lines.size)
        assertEquals(
            "Networld+Interop Conference and Exhibit\\nAtlanta World Congress Center\\nAtlanta\\, Georgia",
            result.lines.single { it.name == "DESCRIPTION" }.value,
        )
    }

    @Test
    fun rfc5545TodoWithFoldedAttachment() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//ABC Corporation//NONSGML My Product//EN",
            "BEGIN:VTODO",
            "DTSTAMP:19980130T134500Z",
            "SEQUENCE:2",
            "UID:uid4@example.com",
            "ORGANIZER:mailto:unclesam@example.com",
            "ATTENDEE;PARTSTAT=ACCEPTED:mailto:jqpublic@example.com",
            "DUE:19980415T000000",
            "STATUS:NEEDS-ACTION",
            "SUMMARY:Submit Income Taxes",
            "BEGIN:VALARM",
            "ACTION:AUDIO",
            "TRIGGER:19980403T120000Z",
            "ATTACH;FMTTYPE=audio/basic:http://example.com/pub/audio-",
            " files/ssbanner.aud",
            "REPEAT:4",
            "DURATION:PT1H",
            "END:VALARM",
            "END:VTODO",
            "END:VCALENDAR",
        )
        val result = lex(input)
        assertEquals(emptyList(), result.problems)
        assertEquals(21, result.lines.size)
        val attach = result.lines.single { it.name == "ATTACH" }
        assertEquals(listOf(Param("FMTTYPE", listOf("audio/basic"))), attach.parameters)
        assertEquals("http://example.com/pub/audio-files/ssbanner.aud", attach.value)
    }

    @Test
    fun rfc6868NewlinesInFoldedQuotedValue() {
        val input = crlf(
            "GEO;X-ADDRESS=\"Pittsburgh Pirates^n115 Federal St^nPitts",
            " burgh, PA 15212\":40.446816;-80.00566",
        )
        val result = lex(input)
        val line = result.single()
        assertEquals(
            listOf(Param("X-ADDRESS", listOf("Pittsburgh Pirates\n115 Federal St\nPittsburgh, PA 15212"))),
            line.parameters,
        )
        assertEquals("40.446816;-80.00566", line.value)
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun rfc6868EscapedQuotes() {
        val line = lex("ATTENDEE;CN=\"George Herman ^'Babe^' Ruth\":mailto:babe@example.com\r\n").single()
        assertEquals(listOf(Param("CN", listOf("George Herman \"Babe\" Ruth"))), line.parameters)
    }

    @Test
    fun rfc6868EscapesInUnquotedValues() {
        val line = lex("X;A=one^ntwo;B=a^^b;C=^'q^':v").single()
        assertEquals(
            listOf(Param("A", listOf("one\ntwo")), Param("B", listOf("a^b")), Param("C", listOf("\"q\""))),
            line.parameters,
        )
    }

    @Test
    fun rfc6868UnknownEscapesStayAsIs() {
        val result = lex("X;A=^x^N^;B=\"^\":v")
        assertEquals(listOf(Param("A", listOf("^x^N^")), Param("B", listOf("^"))), result.single().parameters)
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun rfc6868EscapesAreDecodedLeftToRight() {
        val line = lex("X;A=^^n;B=^^^n:v").single()
        assertEquals(listOf(Param("A", listOf("^n")), Param("B", listOf("^\n"))), line.parameters)
    }

    @Test
    fun rfc6868DoesNotApplyToPropertyValues() {
        val line = lex("X;A=^n:^n^'").single()
        assertEquals(listOf(Param("A", listOf("\n"))), line.parameters)
        assertEquals("^n^'", line.value)
    }
}

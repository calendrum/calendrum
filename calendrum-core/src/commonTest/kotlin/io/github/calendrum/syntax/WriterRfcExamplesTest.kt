package io.github.calendrum.syntax

import kotlin.test.Test
import kotlin.test.assertEquals

class WriterRfcExamplesTest {
    @Test
    fun rfc5545ExamplesAreWrittenBackUpToFolding() {
        for ((name, example) in rfc5545Examples) {
            val source = lex(example)
            assertEquals(emptyList(), source.problems, name)
            val written = write { for (line in source.lines) lexed(line) }
            assertEquals(unfold(example), written.unfolded(), name)
            val reread = lex(written.bytes)
            assertEquals(emptyList(), reread.problems, name)
            assertEquals(source.lines.map { it.content() }, reread.lines.map { it.content() }, name)
        }
    }

    @Test
    fun rfc5545FreeBusyIsWrittenExactly() {
        val written = write {
            begin("VCALENDAR")
            line("VERSION", "2.0")
            line("PRODID", "-//RDU Software//NONSGML HandCal//EN")
            begin("VFREEBUSY")
            line("ORGANIZER", "mailto:jsmith@example.com")
            line("DTSTART", "19980313T141711Z")
            line("DTEND", "19980410T141711Z")
            line("FREEBUSY", "19980314T233000Z/19980315T003000Z")
            line("FREEBUSY", "19980316T153000Z/19980316T163000Z")
            line("FREEBUSY", "19980318T030000Z/19980318T040000Z")
            line("URL", "http://www.example.com/calendar/busytime/jsmith.ifb")
            end("VFREEBUSY")
            end("VCALENDAR")
        }
        assertEquals(RFC5545_FREE_BUSY, written.text)
    }

    @Test
    fun rfc5545ConferenceDescriptionFoldsAt75Octets() {
        val value = "Networld+Interop Conference and Exhibit\\nAtlanta World Congress Center\\nAtlanta\\, Georgia"
        val written = write { line("DESCRIPTION", value) }
        assertEquals(listOf(75, 27), written.lengths)
        assertEquals(
            "DESCRIPTION:Networld+Interop Conference and Exhibit\\nAtlanta World Congress\r\n" +
                "  Center\\nAtlanta\\, Georgia\r\n",
            written.text,
        )
    }

    @Test
    fun rfc6868ICalendarExampleIsNotQuoted() {
        val written = write {
            line("ATTENDEE", "mailto:babe@example.com", "CN" to listOf("George Herman \"Babe\" Ruth"))
        }
        assertEquals("ATTENDEE;CN=George Herman ^'Babe^' Ruth:mailto:babe@example.com\r\n", written.text)
    }

    @Test
    fun rfc6868VCardExampleIsQuoted() {
        val address = "Pittsburgh Pirates\n115 Federal St\nPittsburgh, PA 15212"
        val written = write { line("GEO", "geo:40.446816,-80.00566", "X-ADDRESS" to listOf(address)) }
        assertEquals(
            "GEO;X-ADDRESS=\"Pittsburgh Pirates^n115 Federal St^nPittsburgh, PA 15212\":geo:40.446816,-80.00566\r\n",
            written.unfolded(),
        )
        val line = lex(written.bytes).single()
        assertEquals(listOf(Param("X-ADDRESS", listOf(address))), line.parameters)
        assertEquals("geo:40.446816,-80.00566", line.value)
    }

    private fun Lexed.content(): Triple<String, List<Param>, String> = Triple(name, parameters, value)

    internal companion object {
        val RFC5545_CONFERENCE: String = crlf(
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

        val RFC5545_GROUP_MEETING: String = crlf(
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

        val RFC5545_MIME_MEETING: String = crlf(
            "BEGIN:VCALENDAR",
            "METHOD:xyz",
            "VERSION:2.0",
            "PRODID:-//ABC Corporation//NONSGML My Product//EN",
            "BEGIN:VEVENT",
            "DTSTAMP:19970324T120000Z",
            "SEQUENCE:0",
            "UID:uid3@example.com",
            "ORGANIZER:mailto:jdoe@example.com",
            "ATTENDEE;RSVP=TRUE:mailto:jsmith@example.com",
            "DTSTART:19970324T123000Z",
            "DTEND:19970324T210000Z",
            "CATEGORIES:MEETING,PROJECT",
            "CLASS:PUBLIC",
            "SUMMARY:Calendaring Interoperability Planning Meeting",
            "DESCRIPTION:Discuss how we can test c&s interoperability\\n",
            " using iCalendar and other IETF standards.",
            "LOCATION:LDB Lobby",
            "ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/",
            " conf/bkgrnd.ps",
            "END:VEVENT",
            "END:VCALENDAR",
        )

        val RFC5545_TODO: String = crlf(
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

        val RFC5545_JOURNAL: String = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//ABC Corporation//NONSGML My Product//EN",
            "BEGIN:VJOURNAL",
            "DTSTAMP:19970324T120000Z",
            "UID:uid5@example.com",
            "ORGANIZER:mailto:jsmith@example.com",
            "STATUS:DRAFT",
            "CLASS:PUBLIC",
            "CATEGORIES:Project Report,XYZ,Weekly Meeting",
            "DESCRIPTION:Project xyz Review Meeting Minutes\\n",
            " Agenda\\n1. Review of project version 1.0 requirements.\\n2.",
            "  Definition",
            " of project processes.\\n3. Review of project schedule.\\n",
            " Participants: John Smith\\, Jane Doe\\, Jim Dandy\\n-It was",
            "  decided that the requirements need to be signed off by",
            "  product marketing.\\n-Project processes were accepted.\\n",
            " -Project schedule needs to account for scheduled holidays",
            "  and employee vacation time. Check with HR for specific",
            "  dates.\\n-New schedule will be distributed by Friday.\\n-",
            " Next weeks meeting is cancelled. No meeting until 3/23.",
            "END:VJOURNAL",
            "END:VCALENDAR",
        )

        val RFC5545_FREE_BUSY: String = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//RDU Software//NONSGML HandCal//EN",
            "BEGIN:VFREEBUSY",
            "ORGANIZER:mailto:jsmith@example.com",
            "DTSTART:19980313T141711Z",
            "DTEND:19980410T141711Z",
            "FREEBUSY:19980314T233000Z/19980315T003000Z",
            "FREEBUSY:19980316T153000Z/19980316T163000Z",
            "FREEBUSY:19980318T030000Z/19980318T040000Z",
            "URL:http://www.example.com/calendar/busytime/jsmith.ifb",
            "END:VFREEBUSY",
            "END:VCALENDAR",
        )

        val rfc5545Examples: Map<String, String> = mapOf(
            "conference" to RFC5545_CONFERENCE,
            "group meeting" to RFC5545_GROUP_MEETING,
            "MIME meeting" to RFC5545_MIME_MEETING,
            "to-do" to RFC5545_TODO,
            "journal" to RFC5545_JOURNAL,
            "free/busy" to RFC5545_FREE_BUSY,
        )
    }
}

@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fixup

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.validate.Validator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FixupsTest {
    private fun text(vararg lines: String): String = lines.joinToString("") { "$it\r\n" }

    private fun parse(text: String, fixup: Fixup? = null): VCalendar {
        val options = ParseOptions.builder()
        fixup?.interceptors()?.forEach { options.intercept(it) }
        return ICal.parser(options.build()).parse(text).calendars.single()
    }

    private fun written(calendar: VCalendar): List<String> =
        ICal.writer().encodeToString(calendar.component).split("\r\n").filter { it.isNotEmpty() }

    private fun fixed(fixup: Fixup, vararg lines: String): Pair<FixupResult, List<String>> {
        val result = fixup.apply(parse(text(*lines), fixup))
        return result to written(result.calendar)
    }

    @Test
    fun windowsTimeZoneIds() {
        val (result, lines) = fixed(
            Fixups.windowsTzids,
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN",
            "BEGIN:VTIMEZONE", "TZID:W. Europe Standard Time", "BEGIN:STANDARD", "DTSTART:16010101T030000",
            "TZOFFSETFROM:+0200", "TZOFFSETTO:+0100", "END:STANDARD", "END:VTIMEZONE",
            "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART;TZID=W. Europe Standard Time:20261005T100000",
            "DTEND;TZID=\"(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna\":20261005T110000",
            "END:VEVENT", "END:VCALENDAR",
        )
        assertTrue("TZID:Europe/Berlin" in lines, lines.toString())
        assertTrue("DTSTART;TZID=Europe/Berlin:20261005T100000" in lines, lines.toString())
        assertTrue("DTEND;TZID=Europe/Berlin:20261005T110000" in lines, lines.toString())
        assertEquals(
            listOf(
                "windows-tzids VCALENDAR[0]/VTIMEZONE[0]/TZID[0]",
                "windows-tzids VCALENDAR[0]/VEVENT[0]/DTSTART[0]",
                "windows-tzids VCALENDAR[0]/VEVENT[0]/DTEND[0]",
            ),
            result.changes.map { it.toString() },
        )
        assertTrue(Validator.validate(result.calendar).isValid)
        val again = Fixups.windowsTzids.apply(result.calendar)
        assertFalse(again.isChanged)
        assertSame(result.calendar, again.calendar)
    }

    @Test
    fun xWrProperties() {
        val google = text(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//Google Inc//Google Calendar 70.9054//EN",
            "X-WR-CALNAME:Team", "X-WR-CALDESC:Shared calendar", "X-WR-TIMEZONE:Europe/Moscow",
            "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART:20261005T100000", "DTEND:20261005T110000",
            "EXDATE:20261012T100000,20261019T100000", "RRULE:FREQ=WEEKLY", "END:VEVENT",
            "BEGIN:VEVENT", "UID:2", "DTSTAMP:20260101T000000Z", "DTSTART:20261006T070000Z", "END:VEVENT",
            "BEGIN:VEVENT", "UID:3", "DTSTAMP:20260101T000000Z", "DTSTART;VALUE=DATE:20261007",
            "RRULE:FREQ=DAILY;UNTIL=20261010T235959Z", "X-GOOGLE-CONFERENCE:https://meet.google.com/abc-defg-hij",
            "END:VEVENT", "END:VCALENDAR",
        )
        val result = Fixups.google.apply(parse(google))
        val lines = written(result.calendar)
        assertTrue("DTSTART;TZID=Europe/Moscow:20261005T100000" in lines, lines.toString())
        assertTrue("EXDATE;TZID=Europe/Moscow:20261012T100000,20261019T100000" in lines, lines.toString())
        assertTrue("DTSTART:20261006T070000Z" in lines)
        assertTrue("DTSTART;VALUE=DATE:20261007" in lines)
        assertTrue("RRULE:FREQ=DAILY;UNTIL=20261010" in lines, lines.toString())
        assertTrue("CONFERENCE;VALUE=URI;FEATURE=VIDEO:https://meet.google.com/abc-defg-hij" in lines, lines.toString())
        assertTrue("NAME:Team" in lines && "DESCRIPTION:Shared calendar" in lines)
        assertEquals(
            listOf("x-wr-timezone", "x-wr-calname", "google"),
            result.changes.map { it.fixup }.distinct(),
        )
        assertFalse(Fixups.google.apply(result.calendar).isChanged)
        val unknownZone = parse(google.replace("X-WR-TIMEZONE:Europe/Moscow", "X-WR-TIMEZONE:Mars/Olympus"))
        assertFalse(Fixups.xWrTimezone.apply(unknownZone).isChanged)
    }

    @Test
    fun outlookAndNotes() {
        val (outlook, lines) = fixed(
            Fixups.outlook,
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN",
            "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "X-MICROSOFT-CDO-ALLDAYEVENT:TRUE",
            "DTSTART;TZID=W. Europe Standard Time:20261005T000000",
            "DTEND;TZID=W. Europe Standard Time:20261006T000000",
            "ORGANIZER;CN=Boss:MAILTO:boss@example.com", "ATTENDEE:MAILTO:b@example.com", "END:VEVENT", "END:VCALENDAR",
        )
        assertTrue("DTSTART;VALUE=DATE:20261005" in lines, lines.toString())
        assertTrue("DTEND;VALUE=DATE:20261006" in lines, lines.toString())
        assertTrue("ORGANIZER;CN=Boss:mailto:boss@example.com" in lines && "ATTENDEE:mailto:b@example.com" in lines)
        assertTrue(outlook.isChanged)
        assertTrue(outlook.toString().startsWith("FixupResult("))

        val notes = text(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//Lotus Development Corporation//NONSGML Notes 8.5//EN",
            "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART:20261005T100000Z", "DTEND:",
            "RRULE:", "SUMMARY:", "END:VEVENT", "END:VCALENDAR",
        )
        val parsed = parse(notes, Fixups.lotusNotes)
        assertEquals(KEPT, parsed.events.single().component.properties.map { it.name })
        val plain = parse(notes)
        val tree = Fixups.lotusNotes.apply(plain)
        assertEquals(KEPT, tree.calendar.events.single().component.properties.map { it.name })
        assertEquals(2, tree.changes.size)
    }

    @Test
    fun vCalendar10() {
        val source = text(
            "BEGIN:VCALENDAR", "VERSION:1.0", "PRODID:-//Nokia//Calendar//EN", "TZ:+03:00",
            "BEGIN:VEVENT", "UID:1", "DCREATED:20260101T000000",
            "SUMMARY;ENCODING=QUOTED-PRINTABLE;CHARSET=UTF-8:" +
                "=D0=92=D1=81=D1=82=D1=80=D0=B5=D1=87=D0=B0, =D0=BE=D1=84=D0=B8=D1=81",
            "DESCRIPTION;ENCODING=QUOTED-PRINTABLE;CHARSET=ISO-8859-1:Caf=E9=0D=0Aline 2",
            "DTSTART:20261005T100000", "DTEND:20261005T110000", "RRULE:W1 MO WE #4",
            "ATTENDEE;ROLE=ATTENDEE;STATUS=NEEDS ACTION;RSVP=YES;EXPECT=REQUIRE:John <john@example.com>",
            "AALARM:20261005T094500;PT5M;2;", "DALARM:20261005T095000;;;Meeting soon", "STATUS:NEEDS ACTION",
            "END:VEVENT", "BEGIN:VTODO", "UID:2", "RRULE:MP1 1+ MO 1- FR #0", "END:VTODO",
            "BEGIN:VTODO", "UID:3", "RRULE:MD2 1 LD 20271231T000000", "END:VTODO",
            "BEGIN:VTODO", "UID:4", "RRULE:YM1 6 7", "END:VTODO", "END:VCALENDAR",
        )
        val result = Fixups.vcalendar10.apply(parse(source, Fixups.vcalendar10))
        val lines = written(result.calendar)
        assertTrue("VERSION:2.0" in lines, lines.toString())
        assertTrue("SUMMARY:Встреча\\, офис" in lines, lines.toString())
        assertTrue("DESCRIPTION:Café\\nline 2" in lines, lines.toString())
        assertTrue("CREATED:20260101T000000" in lines)
        assertTrue("RRULE:FREQ=WEEKLY;BYDAY=MO,WE;COUNT=4" in lines, lines.toString())
        assertTrue("RRULE:FREQ=MONTHLY;BYDAY=1MO,-1FR" in lines, lines.toString())
        assertTrue("RRULE:FREQ=MONTHLY;INTERVAL=2;BYMONTHDAY=1,-1;UNTIL=20271231T000000" in lines, lines.toString())
        assertTrue("RRULE:FREQ=YEARLY;BYMONTH=6,7;COUNT=2" in lines, lines.toString())
        val attendee = result.calendar.events.single().attendees.single()
        assertEquals("mailto:john@example.com", attendee.address)
        assertEquals("NEEDS-ACTION", attendee.partStat.value)
        assertEquals("REQ-PARTICIPANT", attendee.role.value)
        assertTrue(attendee.rsvp)
        assertTrue("STATUS:NEEDS-ACTION" in lines)
        val alarms = result.calendar.events.single().alarms
        assertEquals(listOf("AUDIO", "DISPLAY"), alarms.map { it.component.property("ACTION")?.value })
        assertEquals("-PT15M", alarms[0].component.property("TRIGGER")?.value)
        assertEquals("PT5M", alarms[0].component.property("DURATION")?.value)
        assertEquals("Meeting soon", alarms[1].component.property("DESCRIPTION")?.value)
        assertEquals("-PT10M", alarms[1].component.property("TRIGGER")?.value)
        assertFalse(Fixups.vcalendar10.apply(result.calendar).isChanged)
        assertEquals(null, vcalRule("FREQ=DAILY"))
        assertEquals("FREQ=DAILY;COUNT=2", vcalRule("D1"))
        assertEquals(null, vcalRule("D1 0800 #3"))
        assertEquals(null, vcalRule("X2 #3"))
        assertEquals("FREQ=YEARLY;BYYEARDAY=1,100", vcalRule("YD1 1 100 #0"))
    }

    @Test
    fun byProdIdAndCompose() {
        val outlook = text(
            "BEGIN:VCALENDAR", "PRODID:-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN", "VERSION:2.0",
            "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART;TZID=Eastern Standard Time:20261005T100000",
            "END:VEVENT", "END:VCALENDAR",
        )
        val chosen = Fixups.byProdId().apply(parse(outlook))
        assertTrue(written(chosen.calendar).contains("DTSTART;TZID=America/New_York:20261005T100000"))
        val vcal = text(
            "BEGIN:VCALENDAR",
            "VERSION:1.0",
            "BEGIN:VEVENT",
            "SUMMARY;ENCODING=QUOTED-PRINTABLE:a=3Db",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val parsed = parse(vcal, Fixups.byProdId())
        assertEquals("a=b", parsed.events.single().summary)
        val none = parse(text("BEGIN:VCALENDAR", "PRODID:-//t//EN", "VERSION:2.0", "END:VCALENDAR"))
        assertFalse(Fixups.byProdId().apply(none).isChanged)
        val both = Fixups.compose(Fixups.windowsTzids, Fixups.xWrCalendarName)
        assertEquals("windows-tzids+x-wr-calname", both.id)
        assertEquals("Fixup(windows-tzids+x-wr-calname)", both.toString())
        assertEquals(0, both.interceptors().size)
        assertEquals(1, Fixups.lotusNotes.interceptors().size)
        assertEquals(Fixups.google, choose("-//Google Inc//Google Calendar//EN", "2.0"))
        assertEquals(Fixups.lotusNotes, choose("-//IBM//Domino 9//EN", "2.0"))
        assertEquals(null, choose(null, null))
    }

    // Found by fuzzing: vcalendar10 writes VERSION:2.0, after which the
    // PRODID alone chose outlook, so applying byProdId twice changed the calendar twice.
    @Test
    fun aVCalendarFromOutlookIsFixedInOnePass() {
        val vcs = parse(
            text(
                "BEGIN:VCALENDAR",
                "VERSION:1.0",
                "PRODID:-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN",
                "BEGIN:VTIMEZONE",
                "TZID:W. Europe Standard Time",
                "END:VTIMEZONE",
                "BEGIN:VEVENT",
                "DTSTART;TZID=W. Europe Standard Time:20261005T100000",
                "SUMMARY;ENCODING=QUOTED-PRINTABLE:a=3Db",
                "END:VEVENT",
                "END:VCALENDAR",
            ),
            Fixups.byProdId(),
        )
        val first = Fixups.byProdId().apply(vcs)
        assertEquals(setOf("vcalendar10", "windows-tzids"), first.changes.map { it.fixup }.toSet())
        assertEquals("2.0", first.calendar.version)
        val event = first.calendar.events.single()
        assertEquals("Europe/Berlin", event.component.property("DTSTART")?.parameter("TZID")?.value)
        assertEquals("a=b", event.summary)
        assertFalse(Fixups.byProdId().apply(first.calendar).isChanged)
        assertEquals("vcalendar10+windows-tzids+outlook", choose("-//Microsoft Corporation//Outlook//EN", "1.0")?.id)
        assertEquals(Fixups.vcalendar10, choose("-//t//EN", "1.0"))
    }

    // Found by fuzzing: parsing keeps a name that is not a token, a control character and a
    // parameter without a value; a fixup leaves such a line or property as it was, so it neither throws nor leaves a
    // node no writer accepts, and the result writes and reads back.
    @Test
    fun whatCannotBeRebuiltIsLeftAsItWas() {
        val windows = fixed(
            Fixups.windowsTzids,
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN", "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z",
            "DTSTART;TZID=W. Europe Standard Time:20261005T100000",
            "X-B\u000bD;TZID=W. Europe Standard Time:20261005T100000",
            "X-NO-VALUE;TZID=W. Europe Standard Time;X-P:v",
            "END:VEVENT", "END:VCALENDAR",
        ).second
        assertTrue("DTSTART;TZID=Europe/Berlin:20261005T100000" in windows, windows.toString())
        assertTrue("X-B\u000bD;TZID=W. Europe Standard Time:20261005T100000" in windows, windows.toString())
        assertTrue("X-NO-VALUE;TZID=W. Europe Standard Time;X-P:v" in windows, windows.toString())
        val vcal = fixed(
            Fixups.vcalendar10,
            "BEGIN:VCALENDAR", "VERSION:1.0", "BEGIN:VEVENT",
            "S~UMMARY;ENCODING=QUOTED-PRINTABLE;CHARSET=UTF-8:=D0=92",
            "SUMMARY;ENCODING=QUOTED-PRINTABLE:a=01b",
            "DESCRIPTION;ENCODING=QUOTED-PRINTABLE:a=3Db",
            "ATTENDEE;STATUS=ACCEPTED:jo\u0001hn@example.com",
            "AALARM:20261005T094500Z;PT5M;2;\u0007",
            "END:VEVENT", "END:VCALENDAR",
        ).second
        assertTrue("S~UMMARY;ENCODING=QUOTED-PRINTABLE;CHARSET=UTF-8:=D0=92" in vcal, vcal.toString())
        assertTrue("SUMMARY;ENCODING=QUOTED-PRINTABLE:a=01b" in vcal, vcal.toString())
        assertTrue("DESCRIPTION:a=b" in vcal, vcal.toString())
        assertTrue("ATTENDEE;STATUS=ACCEPTED:jo\u0001hn@example.com" in vcal, vcal.toString())
        assertTrue(vcal.none { it.startsWith("AALARM") || it == "BEGIN:VALARM" }, vcal.toString())
        val names = fixed(
            Fixups.compose(Fixups.xWrCalendarName, Fixups.google),
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//Google Inc//Google Calendar 70.9054//EN",
            "X-WR-CALNAME:a\u0001b", "X-WR-CALDESC:c\u0002d", "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z",
            "DTSTART:20261005T100000Z", "X-GOOGLE-CONFERENCE:https://meet.example.com/\u0003", "END:VEVENT",
            "END:VCALENDAR",
        ).second
        assertTrue(names.none { it.startsWith("NAME") || it.startsWith("DESCRIPTION") }, names.toString())
        assertTrue(names.none { it.startsWith("CONFERENCE") }, names.toString())
    }

    private companion object {
        val KEPT = listOf("UID", "DTSTAMP", "DTSTART", "SUMMARY")
    }
}

package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class WrappersTest {
    private fun calendar(vararg lines: String): VCalendar =
        ICal.parse(lines.joinToString("\r\n", postfix = "\r\n")).calendars.single()

    private fun utc(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int = 0): ICalDateTime =
        ICalDateTime.Utc(LocalDateTime(year, month, day, hour, minute, second).toInstant(TimeZone.UTC))

    @Test
    fun simpleObject() {
        val calendar = calendar(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//hacksw/handcal//NONSGML v1.0//EN",
            "BEGIN:VEVENT",
            "UID:19970610T172345Z-AF23B2@example.com",
            "DTSTAMP:19970610T172345Z",
            "DTSTART:19970714T170000Z",
            "DTEND:19970715T040000Z",
            "SUMMARY:Bastille Day Party",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        assertEquals("2.0", calendar.version)
        assertEquals("-//hacksw/handcal//NONSGML v1.0//EN", calendar.prodId)
        assertNull(calendar.method)
        assertNull(calendar.calScale)
        val event = calendar.events.single()
        assertEquals("19970610T172345Z-AF23B2@example.com", event.uid)
        assertEquals(utc(1997, 6, 10, 17, 23, 45), event.stamp)
        assertEquals(utc(1997, 7, 14, 17, 0), event.start)
        assertEquals(utc(1997, 7, 15, 4, 0), event.end)
        assertEquals("Bastille Day Party", event.summary)
        assertEquals(emptyList(), calendar.todos)
        assertEquals(emptyList(), calendar.journals)
        assertEquals(emptyList(), calendar.freeBusy)
        assertEquals(emptyList(), calendar.timeZones)
        assertSame(calendar.events, calendar.events)
        assertSame(event.component, calendar.component.components.single())
    }

    @Test
    fun groupMeetingWithATimeZone() {
        val calendar = calendar(
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
        val zone = calendar.timeZones.single()
        assertEquals("America/New_York", zone.tzid)
        assertNull(zone.tzUrl)
        assertNull(zone.lastModified)
        val (standard, daylight) = zone.observances
        assertIs<VTimeZone.Standard>(standard)
        assertIs<VTimeZone.Daylight>(daylight)
        assertEquals(UtcOffset(hours = -4), standard.offsetFrom)
        assertEquals(UtcOffset(hours = -5), standard.offsetTo)
        assertEquals(listOf("EST"), standard.names)
        assertEquals(ICalDateTime.Floating(LocalDateTime(1999, 4, 4, 2, 0, 0)), daylight.start)
        assertEquals(listOf("EDT"), daylight.names)
        assertNull(daylight.rrule)
        assertEquals(emptyList(), daylight.rdates)
        assertEquals(emptyList(), daylight.comments)
        assertNotEquals<Any>(standard, daylight)
        val event = calendar.events.single()
        assertEquals("mailto:mrbig@example.com", event.organizer?.address)
        val attendee = event.attendees.single()
        assertEquals("mailto:employee-A@example.com", attendee.address)
        assertTrue(attendee.rsvp)
        assertSame(CuType.Group, attendee.cuType)
        assertSame(Classification.Public, event.classification)
        assertEquals(listOf("MEETING"), event.categories)
        assertEquals(utc(1998, 3, 9, 13, 0), event.created)
        val start = assertIs<ICalDateTime.Zoned>(event.start)
        assertEquals("America/New_York", start.tzid)
        assertEquals(LocalDateTime(1998, 3, 12, 8, 30, 0), start.dateTime)
        assertEquals("1CP Conference Room 4350", event.location)
        assertEquals("Project XYZ Review Meeting", event.description)
        assertEquals("VTimeZone(properties=1, components=2)", zone.toString())
        assertEquals("VTimeZone.Standard(properties=4, components=0)", standard.toString())
    }

    @Test
    fun todoWithAnAlarm() {
        val calendar = calendar(
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
        val todo = calendar.todos.single()
        assertEquals(2, todo.sequence)
        assertSame(TodoStatus.NeedsAction, todo.status)
        assertEquals(ICalDateTime.Floating(LocalDateTime(1998, 4, 15, 0, 0, 0)), todo.due)
        assertSame(PartStat.Accepted, todo.attendees.single().partStat)
        val alarm = todo.alarms.single()
        assertSame(AlarmAction.Audio, alarm.action)
        assertEquals(utc(1998, 4, 3, 12, 0), assertIs<Trigger.Absolute>(alarm.trigger).dateTime)
        assertEquals(4, alarm.repeat)
        assertEquals(ICalDuration.ofHours(1), alarm.duration)
        val sound = assertIs<Attach.Uri>(alarm.attachments.single())
        assertEquals("http://example.com/pub/audio-files/ssbanner.aud", sound.uri)
        assertEquals("audio/basic", sound.formatType)
        assertNull(alarm.description)
        assertNull(alarm.summary)
        assertEquals(emptyList(), alarm.attendees)
        assertSame(todo.alarms, todo.alarms)
        assertEquals(
            listOf("ICAL-V026@15:9"),
            todo.component.components.single().decode(Properties.TRIGGER).codes(),
        )
    }

    @Test
    fun journalWithManyDescriptions() {
        val calendar = calendar(
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
            " Agenda\\n1. Review of project version 1.0 requirements.",
            "DESCRIPTION:Second note",
            "END:VJOURNAL",
            "END:VCALENDAR",
        )
        val journal = calendar.journals.single()
        assertSame(JournalStatus.Draft, journal.status)
        assertEquals(listOf("Project Report", "XYZ", "Weekly Meeting"), journal.categories)
        assertEquals(2, journal.descriptions.size)
        assertTrue(journal.descriptions[0].startsWith("Project xyz Review Meeting Minutes\nAgenda\n1."))
        assertEquals("Second note", journal.descriptions[1])
        assertEquals("mailto:jsmith@example.com", journal.organizer?.address)
    }

    @Test
    fun freeBusy() {
        val calendar = calendar(
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
        val busy = calendar.freeBusy.single()
        assertEquals(3, busy.freeBusy.size)
        assertSame(FbType.Busy, busy.freeBusy[0].type)
        assertEquals(utc(1998, 3, 16, 16, 30), busy.freeBusy[1].periods.single().end)
        assertEquals("http://www.example.com/calendar/busytime/jsmith.ifb", busy.url)
        assertEquals(utc(1998, 3, 13, 14, 17, 11), busy.start)
        assertEquals(utc(1998, 4, 10, 14, 17, 11), busy.end)
        assertNull(busy.uid)
        assertNull(busy.contact)
        assertEquals(emptyList(), busy.attendees)
    }

    @Test
    fun listsAreFlattenedAndMultiplePropertiesKept() {
        val calendar = calendar(
            "BEGIN:VCALENDAR",
            "METHOD:REQUEST",
            "CALSCALE:GREGORIAN",
            "BEGIN:VEVENT",
            "UID:1",
            "CATEGORIES:A,B",
            "CATEGORIES:C",
            "RESOURCES:EASEL",
            "RESOURCES:VCR,PROJECTOR",
            "EXDATE:20260902T100000Z,20260903T100000Z",
            "EXDATE:20260905T100000Z",
            "RDATE:20260910T100000Z",
            "RDATE;VALUE=PERIOD:20260911T100000Z/PT1H",
            "RRULE:FREQ=DAILY;COUNT=10",
            "RECURRENCE-ID;RANGE=THISANDFUTURE:20260901T100000Z",
            "COMMENT:one",
            "COMMENT:two",
            "CONTACT:Jim",
            "RELATED-TO;RELTYPE=SIBLING:2",
            "REQUEST-STATUS:2.0;Success",
            "GEO:1.5;2.5",
            "PRIORITY:1",
            "TRANSP:TRANSPARENT",
            "STATUS:CANCELLED",
            "URL:http://example.com",
            "LAST-MODIFIED:20260901T000000Z",
            "DURATION:PT1H",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        assertSame(Method.Request, calendar.method)
        assertEquals("GREGORIAN", calendar.calScale)
        val event = calendar.events.single()
        assertEquals(listOf("A", "B", "C"), event.categories)
        assertEquals(listOf("EASEL", "VCR", "PROJECTOR"), event.resources)
        assertEquals(3, event.exdates.size)
        assertEquals(listOf(utc(2026, 9, 10, 10, 0), utc(2026, 9, 11, 10, 0)), event.rdates)
        assertEquals(10, event.rrule?.count)
        assertEquals(utc(2026, 9, 1, 10, 0), event.recurrenceId)
        assertSame(RecurrenceRange.ThisAndFuture, event.component[Properties.RECURRENCE_ID]?.range)
        assertEquals(listOf("one", "two"), event.comments)
        assertEquals(listOf("Jim"), event.contacts)
        assertSame(RelType.Sibling, event.relatedTo.single().relType)
        assertEquals("2.0", event.requestStatuses.single().code)
        assertEquals(1.5, event.geo?.latitude)
        assertEquals(1, event.priority)
        assertSame(Transparency.Transparent, event.transparency)
        assertSame(EventStatus.Cancelled, event.status)
        assertEquals("http://example.com", event.url)
        assertEquals(utc(2026, 9, 1, 0, 0), event.lastModified)
        assertEquals(ICalDuration.ofHours(1), event.duration)
        assertEquals(emptyList(), event.attachments)
        assertEquals(emptyList(), event.alarms)
        assertEquals("VEvent(properties=23, components=0)", event.toString())
        assertEquals("VCalendar(properties=2, components=1)", calendar.toString())
    }

    @Test
    fun malformedValuesAreNullAndMissingListsEmpty() {
        val event = calendar(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "DTSTART:2026-13-01",
            "SEQUENCE:x",
            "ATTENDEE:mailto:a@example.com",
            "ATTENDEE:",
            "END:VEVENT",
            "END:VCALENDAR",
        ).events.single()
        assertNull(event.start)
        assertNull(event.sequence)
        assertNull(event.uid)
        assertEquals(1, event.attendees.size)
        assertEquals(emptyList(), event.exdates)
        assertEquals(emptyList(), event.rdates)
        assertEquals(emptyList(), event.categories)
        assertIs<Decoded.Malformed>(event.component.decode(Properties.DTSTART))
    }

    @Test
    fun calendarsAreTopLevelVcalendarsOnlyAndCached() {
        val result = ICal.parse(
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nEND:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:x\r\nEND:VEVENT\r\n" +
                "BEGIN:VCALENDAR\r\nVERSION:3.0\r\nEND:VCALENDAR\r\n",
        )
        assertEquals(listOf("2.0", "3.0"), result.calendars.map { it.version })
        assertSame(result.calendars, result.calendars)
        assertEquals(emptyList(), ICal.parse("BEGIN:VEVENT\r\nEND:VEVENT\r\n").calendars)
        val again = ICal.parse("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nEND:VCALENDAR\r\n").calendars.single()
        assertEquals(result.calendars[0], again)
        assertEquals(result.calendars[0].hashCode(), again.hashCode())
        assertNotEquals(result.calendars[0], result.calendars[1])
    }

    @Test
    fun wrappersCompareByComponent() {
        val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\n" +
            "BEGIN:VALARM\r\nACTION:DISPLAY\r\nEND:VALARM\r\nEND:VEVENT\r\n" +
            "BEGIN:VTODO\r\nUID:2\r\nEND:VTODO\r\nBEGIN:VJOURNAL\r\nUID:3\r\nEND:VJOURNAL\r\n" +
            "BEGIN:VFREEBUSY\r\nUID:4\r\nEND:VFREEBUSY\r\nEND:VCALENDAR\r\n"
        val left = ICal.parse(text).calendars.single()
        val right = ICal.parse(text).calendars.single()
        assertEquals(left.events.single(), right.events.single())
        assertEquals(left.events.single().alarms.single(), right.events.single().alarms.single())
        assertEquals(left.todos.single(), right.todos.single())
        assertEquals(left.journals.single(), right.journals.single())
        assertEquals(left.freeBusy.single(), right.freeBusy.single())
        assertEquals(left.todos.single().hashCode(), right.todos.single().hashCode())
        assertEquals("VAlarm(properties=1, components=0)", left.events.single().alarms.single().toString())
        assertEquals("VTodo(properties=1, components=0)", left.todos.single().toString())
        assertEquals("VJournal(properties=1, components=0)", left.journals.single().toString())
        assertEquals("VFreeBusy(properties=1, components=0)", left.freeBusy.single().toString())
        assertEquals(left.events.single().alarms.single().hashCode(), right.events.single().alarms.single().hashCode())
        assertEquals(left.journals.single().hashCode(), right.journals.single().hashCode())
        assertEquals(left.freeBusy.single().hashCode(), right.freeBusy.single().hashCode())
    }
}

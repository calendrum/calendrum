package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.syntax.WriterRfcExamplesTest
import io.github.calendrum.syntax.crlf
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class EditingTest {
    private val input = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Example//Edit//EN",
        "BEGIN:VEVENT",
        "uid:42@example.com",
        "DTSTAMP:20260901T080000Z",
        "DTSTART;TZID=Europe/Moscow:20261001T100000",
        "SUMMARY:A long summary that is folded by its writer because it does not fit",
        "  into one line",
        "ATTENDEE;X-VENDOR=\"a;b\";PARTSTAT=NEEDS-ACTION;CN=\"Me\":mailto:me@example.com",
        "ATTENDEE;CN=Other:mailto:other@example.com",
        "COMMENT:first",
        "X-ACME-TAG:blue",
        "COMMENT:second",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER:-PT15M",
        "DESCRIPTION:Soon",
        "END:VALARM",
        "END:VEVENT",
        "BEGIN:VTODO",
        "UID:7@example.com",
        "DTSTAMP:20260901T080000Z",
        "END:VTODO",
        "END:VCALENDAR",
    )

    private fun parse(text: String = input): VCalendar = ICal.parse(text).calendars.single()

    @Test
    fun anEmptyEditKeepsTheSameNodes() {
        val calendar = parse()
        val event = calendar.events.single()
        assertSame(event.component, event.edit {}.component)
        assertSame(calendar.component, calendar.edit {}.component)
        assertSame(calendar.component, calendar.edit { events { replace(event, event) } }.component)
        assertEquals(input, ICal.writer().encodeToString(calendar.edit {}.component))
    }

    @Test
    fun theProposalEditMovesTheEvent() {
        val calendar = parse()
        val event = calendar.events.single()
        val moved = event.edit {
            start = (start as ICalDateTime.Zoned).let {
                ICalDateTime.Zoned(LocalDateTime(it.dateTime.date.plus(1, DateTimeUnit.DAY), it.dateTime.time), it.tzid)
            }
            sequence = (sequence ?: 0) + 1
            attendees { replace("MAILTO:ME@example.com") { partStat = PartStat.Accepted } }
            remove(Properties.COMMENT)
            remove("x-acme-tag")
        }
        val updated = calendar.edit { events { replace(old = event, new = moved) } }
        val expected = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//Example//Edit//EN",
            "BEGIN:VEVENT",
            "uid:42@example.com",
            "DTSTAMP:20260901T080000Z",
            "DTSTART;TZID=Europe/Moscow:20261002T100000",
            "SUMMARY:A long summary that is folded by its writer because it does not fit",
            "  into one line",
            "ATTENDEE;X-VENDOR=\"a;b\";PARTSTAT=ACCEPTED;CN=Me:mailto:me@example.com",
            "ATTENDEE;CN=Other:mailto:other@example.com",
            "SEQUENCE:1",
            "BEGIN:VALARM",
            "ACTION:DISPLAY",
            "TRIGGER:-PT15M",
            "DESCRIPTION:Soon",
            "END:VALARM",
            "END:VEVENT",
            "BEGIN:VTODO",
            "UID:7@example.com",
            "DTSTAMP:20260901T080000Z",
            "END:VTODO",
            "END:VCALENDAR",
        )
        assertEquals(expected, ICal.writer().encodeToString(updated.component))
        val before = event.component.properties
        val after = moved.component.properties
        assertSame(before[0], after[0])
        assertSame(before[1], after[1])
        assertNotSame(before[2], after[2])
        assertSame(before[3], after[3])
        assertSame(before[5], after[5])
        assertSame(event.component.components.single(), moved.component.components.single())
        assertSame(calendar.component.properties[0], updated.component.properties[0])
        assertSame(calendar.todos.single().component, updated.todos.single().component)
        assertEquals(updated.component, ICal.parse(ICal.writer().encodeToString(updated.component)).components.single())
        assertEquals(PartStat.NeedsAction, event.attendees.first().partStat)
    }

    @Test
    fun newPropertiesGoAfterTheLastPropertyAndReplacedOnesStayInPlace() {
        val event = parse().events.single()
        val edited = event.edit {
            summary = "Short"
            location = "Room 1"
            comment("third")
            set(Properties.PRIORITY, 1)
            set(Properties.PRIORITY, 2)
        }
        assertEquals(
            listOf(
                "UID", "DTSTAMP", "DTSTART", "SUMMARY", "ATTENDEE", "ATTENDEE", "COMMENT", "X-ACME-TAG", "COMMENT",
                "LOCATION", "COMMENT", "PRIORITY",
            ),
            edited.component.properties.map { it.name },
        )
        assertEquals(2, edited.priority)
        assertEquals("Short", edited.summary)
        val written = ICal.writer().encodeToString(edited.component)
        assertTrue(written.contains("SUMMARY:Short\r\nATTENDEE;X-VENDOR"))
        assertTrue(written.contains("PRIORITY:2\r\nBEGIN:VALARM"))
        val duplicated = event.edit {
            property(Properties.SUMMARY, "Second")
            summary = "Only"
        }
        assertEquals(listOf("Only"), duplicated.component.properties("SUMMARY").map { it.value })
        assertEquals(event.component.properties.size, duplicated.component.properties.size)
    }

    @Test
    fun attendeesAreAddedReplacedAndRemovedByAddress() {
        val event = parse().events.single()
        val edited = event.edit {
            attendees {
                assertEquals(2, items.size)
                assertTrue(remove("mailto:OTHER@example.com"))
                assertFalse(remove("mailto:nobody@example.com"))
                assertFalse(replace("mailto:nobody@example.com") { partStat = PartStat.Declined })
                add("mailto:new@example.com") {
                    cn = "New"
                    parameter("X-SOURCE", "test")
                }
            }
        }
        val attendees = edited.attendees
        assertEquals(listOf("mailto:me@example.com", "mailto:new@example.com"), attendees.map { it.address })
        assertEquals("a;b", attendees[0].parameters.first { it.name == "X-VENDOR" }.value)
        assertEquals(listOf("CN", "X-SOURCE"), attendees[1].parameters.map { it.name })
        assertSame(event.component.properties("ATTENDEE")[0], edited.component.properties("ATTENDEE")[0])
    }

    @Test
    fun aParticipantBuilderKeepsUnknownParametersInOrder() {
        val attendee = parse().events.single().attendees.first()
        val changed = attendee.toBuilder().apply {
            assertEquals("Me", cn)
            assertEquals(PartStat.NeedsAction, partStat)
            assertEquals(Role.ReqParticipant, role)
            assertFalse(rsvp)
            rsvp = true
            delegatedTo = listOf("mailto:a@example.com", "mailto:b@example.com")
            cn = null
        }.build()
        assertEquals(listOf("X-VENDOR", "PARTSTAT", "RSVP", "DELEGATED-TO"), changed.parameters.map { it.name })
        assertEquals(listOf("mailto:a@example.com", "mailto:b@example.com"), changed.delegatedTo)
        assertTrue(changed.rsvp)
        val organizer = Organizer.builder("mailto:boss@example.com").cn("Boss").email("boss@example.com").build()
        assertEquals("Boss", organizer.cn)
        assertEquals(
            listOf("CN", "EMAIL"),
            organizer.toBuilder().sentBy("mailto:x@example.com").build().parameters
                .map { it.name }.dropLast(1),
        )
    }

    @Test
    fun childrenAreAddedReplacedAndRemoved() {
        val calendar = parse()
        val event = calendar.events.single()
        val alarm = event.alarms.single()
        val edited = event.edit {
            alarms {
                assertEquals(1, items.size)
                assertTrue(replace(alarm, alarm.edit { description = "Very soon" }))
                add(VAlarm.builder().action(AlarmAction.Audio).build())
            }
        }
        assertEquals(listOf("Very soon", null), edited.alarms.map { it.description })
        val withoutAlarms = edited.edit { alarms { for (item in items) remove(item) } }
        assertEquals(0, withoutAlarms.alarms.size)
        val emptied = calendar.edit {
            todos { assertTrue(remove(items.single())) }
            events { assertFalse(remove(withoutAlarms)) }
        }
        assertEquals(0, emptied.todos.size)
        assertEquals(1, emptied.events.size)
    }

    @Test
    fun propertiesAfterChildrenKeepTheirPlace() {
        val text = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "UID:1",
            "BEGIN:VALARM",
            "ACTION:DISPLAY",
            "END:VALARM",
            "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC",
            "BEGIN:VALARM",
            "ACTION:AUDIO",
            "END:VALARM",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val event = ICal.parse(text).calendars.single().events.single()
        val added = event.edit { summary = "S" }
        val written = ICal.writer().encodeToString(added.component)
        assertEquals(
            crlf(
                "BEGIN:VEVENT",
                "UID:1",
                "BEGIN:VALARM",
                "ACTION:DISPLAY",
                "END:VALARM",
                "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC",
                "SUMMARY:S",
                "BEGIN:VALARM",
                "ACTION:AUDIO",
                "END:VALARM",
                "END:VEVENT",
            ),
            written,
        )
        val removed = event.edit {
            remove("UID")
            alarms { add(VAlarm.builder().action(AlarmAction.Email).build()) }
        }
        assertEquals(
            crlf(
                "BEGIN:VEVENT",
                "BEGIN:VALARM",
                "ACTION:DISPLAY",
                "END:VALARM",
                "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC",
                "BEGIN:VALARM",
                "ACTION:AUDIO",
                "END:VALARM",
                "BEGIN:VALARM",
                "ACTION:EMAIL",
                "END:VALARM",
                "END:VEVENT",
            ),
            ICal.writer().encodeToString(removed.component),
        )
    }

    @Test
    fun everyRfcExampleSurvivesAnEditRoundTrip() {
        for ((name, example) in WriterRfcExamplesTest.rfc5545Examples) {
            val calendar = ICal.parse(example).calendars.single()
            val edited = calendar.edit {
                for (event in events.items) events.replace(event, event.edit { summary = "Edited $name" })
                for (todo in todos.items) todos.replace(todo, todo.edit { comment("checked") })
                for (journal in journals.items) journals.replace(journal, journal.edit { sequence = 1 })
                for (freeBusy in freeBusies.items) freeBusies.replace(freeBusy, freeBusy.edit { contact = "Desk" })
                for (zone in timeZones.items) {
                    timeZones.replace(
                        zone,
                        zone.edit {
                            observances { for (o in items) replace(o, o.edit { name("X") }) }
                        },
                    )
                }
            }
            val written = ICal.writer().encodeToString(edited.component)
            assertEquals(edited.component, ICal.parse(written).components.single(), name)
            val original = example.split("\r\n")
            val lines = written.split("\r\n")
            assertTrue(lines.containsAll(original.filter { !it.startsWith("SUMMARY:") }), name)
        }
    }
}

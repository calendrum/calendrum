package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.syntax.WriterRfcExamplesTest
import io.github.calendrum.syntax.crlf
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.Instant

class BuildingTest {
    private val clock = object : Clock {
        override fun now(): Instant = LocalDateTime(2026, 9, 1, 8, 0).toInstant(TimeZone.UTC)
    }

    private var generated = 0

    private val defaults = BuildDefaults(clock) { "generated-${++generated}@example.com" }

    private val canonical = ICal.writer(WriteOptions.builder().byteStable(false).build())

    private fun utc(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int = 0): ICalDateTime =
        ICalDateTime.Utc(LocalDateTime(year, month, day, hour, minute, second).toInstant(TimeZone.UTC))

    private fun assertBuilt(example: String, built: VCalendar) {
        val parsed = ICal.parse(example).components.single()
        assertEquals(parsed, built.component)
        assertEquals(canonical.encodeToString(parsed), canonical.encodeToString(built.component))
        assertEquals(built.component, ICal.parse(ICal.writer().encodeToString(built.component)).components.single())
    }

    @Test
    fun theProposalExampleBuilds() {
        val calendar = VCalendar(prodId = "-//Acme//Planner 1.0//EN", defaults = defaults) {
            event {
                uid = "42@acme.example"
                summary = "Планёрка"
                start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Moscow")
                duration = ICalDuration.ofHours(1)
                rrule(Frequency.WEEKLY) {
                    byDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
                    count(10)
                }
                attendee("mailto:alice@example.com") {
                    cn = "Alice"
                    partStat = PartStat.NeedsAction
                    rsvp = true
                }
                alarm {
                    display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Скоро планёрка")
                }
                property(Properties.CATEGORIES, Categories(listOf("work")))
                property("X-ACME-TAG", "blue")
            }
        }
        val expected = crlf(
            "BEGIN:VCALENDAR",
            "PRODID:-//Acme//Planner 1.0//EN",
            "VERSION:2.0",
            "BEGIN:VEVENT",
            "UID:42@acme.example",
            "SUMMARY:Планёрка",
            "DTSTART;TZID=Europe/Moscow:20261001T100000",
            "DURATION:PT1H",
            "RRULE:FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE",
            "ATTENDEE;CN=Alice;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:mailto:alice@example.com",
            "CATEGORIES:work",
            "X-ACME-TAG:blue",
            "DTSTAMP:20260901T080000Z",
            "BEGIN:VALARM",
            "ACTION:DISPLAY",
            "TRIGGER:-PT15M",
            "DESCRIPTION:Скоро планёрка",
            "END:VALARM",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        assertEquals(expected, ICal.writer().encodeToString(calendar.component))
        val event = calendar.events.single()
        assertEquals("Планёрка", event.summary)
        assertEquals(PartStat.NeedsAction, event.attendees.single().partStat)
        assertEquals(listOf("work"), event.categories)
        assertEquals(AlarmAction.Display, event.alarms.single().action)
        assertEquals(0, generated)
    }

    @Test
    fun theConferenceExampleBuilds() {
        val calendar = VCalendar("-//xyz Corp//NONSGML PDA Calendar Version 1.0//EN") {
            event {
                stamp = utc(1996, 7, 4, 12, 0)
                uid = "uid1@example.com"
                organizer("mailto:jsmith@example.com") {}
                start = utc(1996, 9, 18, 14, 30)
                end = utc(1996, 9, 20, 22, 0)
                status = EventStatus.Confirmed
                categories("CONFERENCE")
                summary = "Networld+Interop Conference"
                description = "Networld+Interop Conference and Exhibit\nAtlanta World Congress Center\nAtlanta, Georgia"
            }
        }
        assertBuilt(WriterRfcExamplesTest.RFC5545_CONFERENCE, calendar)
    }

    @Test
    fun theGroupMeetingExampleBuilds() {
        val newYork = "America/New_York"
        val calendar = VCalendar("-//RDU Software//NONSGML HandCal//EN") {
            timeZone {
                tzid = newYork
                standard {
                    start = ICalDateTime.Floating(LocalDateTime(1998, 10, 25, 2, 0))
                    offsetFrom = UtcOffset(hours = -4)
                    offsetTo = UtcOffset(hours = -5)
                    name("EST")
                }
                daylight {
                    start = ICalDateTime.Floating(LocalDateTime(1999, 4, 4, 2, 0))
                    offsetFrom = UtcOffset(hours = -5)
                    offsetTo = UtcOffset(hours = -4)
                    name("EDT")
                }
            }
            event {
                stamp = utc(1998, 3, 9, 23, 10)
                uid = "guid-1.example.com"
                organizer = Organizer.builder("mailto:mrbig@example.com").build()
                attendee("mailto:employee-A@example.com") {
                    rsvp = true
                    role = Role.ReqParticipant
                    cuType = CuType.Group
                }
                description = "Project XYZ Review Meeting"
                categories("MEETING")
                classification = Classification.Public
                created = utc(1998, 3, 9, 13, 0)
                summary = "XYZ Project Review"
                start = ICalDateTime.Zoned(LocalDateTime(1998, 3, 12, 8, 30), newYork)
                end = ICalDateTime.Zoned(LocalDateTime(1998, 3, 12, 9, 30), newYork)
                location = "1CP Conference Room 4350"
            }
        }
        assertBuilt(WriterRfcExamplesTest.RFC5545_GROUP_MEETING, calendar)
    }

    @Test
    fun theMimeMeetingExampleBuildsWithJavaStyleBuilders() {
        val event = VEvent.builder()
            .stamp(utc(1997, 3, 24, 12, 0))
            .sequence(0)
            .uid("uid3@example.com")
            .organizer(Organizer.builder("mailto:jdoe@example.com").build())
            .attendee(Attendee.builder("mailto:jsmith@example.com").rsvp(true).build())
            .start(utc(1997, 3, 24, 12, 30))
            .end(utc(1997, 3, 24, 21, 0))
            .categories("MEETING", "PROJECT")
            .classification(Classification.Public)
            .summary("Calendaring Interoperability Planning Meeting")
            .description("Discuss how we can test c&s interoperability\nusing iCalendar and other IETF standards.")
            .location("LDB Lobby")
            .attach(Attach.Uri("ftp://example.com/pub/conf/bkgrnd.ps", "application/postscript"))
            .build()
        val calendar = VCalendar.builder()
            .property("METHOD", "xyz")
            .version("2.0")
            .prodId("-//ABC Corporation//NONSGML My Product//EN")
            .event(event)
            .build()
        assertBuilt(WriterRfcExamplesTest.RFC5545_MIME_MEETING, calendar)
    }

    @Test
    fun theToDoExampleBuilds() {
        val calendar = VCalendar.builder().version("2.0").prodId("-//ABC Corporation//NONSGML My Product//EN").todo {
            stamp = utc(1998, 1, 30, 13, 45)
            sequence = 2
            uid = "uid4@example.com"
            organizer("mailto:unclesam@example.com") {}
            attendee("mailto:jqpublic@example.com") { partStat = PartStat.Accepted }
            due = ICalDateTime.Floating(LocalDateTime(1998, 4, 15, 0, 0))
            status = TodoStatus.NeedsAction
            summary = "Submit Income Taxes"
            alarm {
                action = AlarmAction.Audio
                property("TRIGGER", "19980403T120000Z")
                attach(Attach.Uri("http://example.com/pub/audio-files/ssbanner.aud", "audio/basic"))
                repeat = 4
                duration = ICalDuration.ofHours(1)
            }
        }.build()
        assertBuilt(WriterRfcExamplesTest.RFC5545_TODO, calendar)
    }

    @Test
    fun theJournalExampleBuilds() {
        val description = ICal.parse(WriterRfcExamplesTest.RFC5545_JOURNAL).calendars.single().journals.single()
            .descriptions.single()
        assertEquals(true, description.contains("2. Definitionof project processes."))
        val calendar = VCalendar.builder().version("2.0").prodId("-//ABC Corporation//NONSGML My Product//EN").journal {
            stamp = utc(1997, 3, 24, 12, 0)
            uid = "uid5@example.com"
            organizer("mailto:jsmith@example.com") {}
            status = JournalStatus.Draft
            classification = Classification.Public
            categories("Project Report", "XYZ", "Weekly Meeting")
            description(description)
        }.build()
        assertBuilt(WriterRfcExamplesTest.RFC5545_JOURNAL, calendar)
    }

    @Test
    fun theFreeBusyExampleGetsItsRequiredProperties() {
        val builder = VCalendar.builder(defaults).version("2.0").prodId("-//RDU Software//NONSGML HandCal//EN")
        builder.freeBusy {
            organizer("mailto:jsmith@example.com") {}
            start = utc(1998, 3, 13, 14, 17, 11)
            end = utc(1998, 4, 10, 14, 17, 11)
            freeBusy(ICalPeriod(utc(1998, 3, 14, 23, 30), utc(1998, 3, 15, 0, 30), null))
            freeBusy(ICalPeriod(utc(1998, 3, 16, 15, 30), utc(1998, 3, 16, 16, 30), null))
            freeBusy(ICalPeriod(utc(1998, 3, 18, 3, 0), utc(1998, 3, 18, 4, 0), null))
            url = "http://www.example.com/calendar/busytime/jsmith.ifb"
        }
        val parsed = ICal.parse(WriterRfcExamplesTest.RFC5545_FREE_BUSY).components.single()
        val freeBusy = parsed.components.single()
        val expected = ICalComponent.builder("VCALENDAR")
            .apply { for (property in parsed.properties) property(property) }
            .component(
                ICalComponent.builder("VFREEBUSY")
                    .apply { for (property in freeBusy.properties) property(property) }
                    .property("UID", "generated-1@example.com")
                    .property("DTSTAMP", "20260901T080000Z")
                    .build(),
            )
            .build()
        assertEquals(expected, builder.build().component)
    }

    @Test
    fun requiredPropertiesNeedValuesOrDefaults() {
        assertFailsWith<IllegalStateException> { VEvent.builder().summary("No UID").build() }
        assertFailsWith<IllegalStateException> { VTodo.builder().uid("1").build() }
        assertFailsWith<IllegalStateException> { VJournal.builder().stamp(utc(2026, 1, 1, 0, 0)).build() }
        assertFailsWith<IllegalStateException> { VFreeBusy.builder().build() }
        assertFailsWith<IllegalStateException> { VCalendar("-//x//y//EN") { event { summary = "No UID" } } }
        val event = VEvent.builder(defaults).summary("Defaults").build()
        assertEquals("generated-1@example.com", event.uid)
        assertEquals(utc(2026, 9, 1, 8, 0), event.stamp)
        assertEquals(listOf("SUMMARY", "UID", "DTSTAMP"), event.component.properties.map { it.name })
        val explicit = VEvent.builder().uid("1").stamp(utc(2026, 1, 1, 0, 0)).build()
        assertEquals(listOf("UID", "DTSTAMP"), explicit.component.properties.map { it.name })
        assertEquals(1, generated)
        assertNull(VAlarm.builder().build().action)
        assertEquals(0, VTimeZone.builder().build().component.properties.size)
    }

    @Test
    fun untilMustMatchTheStart() {
        val dateRule = Recur.builder(Frequency.DAILY).until(ICalDateTime.Date(LocalDate(2026, 12, 1)))
        val utcRule = Recur.builder(Frequency.DAILY).until(utc(2026, 12, 1, 0, 0)).build()
        val floatingRule = Recur.builder(Frequency.DAILY)
            .until(ICalDateTime.Floating(LocalDateTime(2026, 12, 1, 0, 0)))
            .build()
        val zonedStart = ICalDateTime.Zoned(LocalDateTime(2026, 9, 1, 10, 0), "Europe/Berlin")
        VEvent.builder(defaults).start(zonedStart).rrule(utcRule).build()
        VEvent.builder(defaults).start(utc(2026, 9, 1, 10, 0)).rrule(utcRule).build()
        VEvent.builder(
            defaults,
        ).start(ICalDateTime.Floating(LocalDateTime(2026, 9, 1, 10, 0))).rrule(floatingRule).build()
        VEvent.builder(defaults).start(ICalDateTime.Date(LocalDate(2026, 9, 1))).rrule(dateRule.build())
            .build()
        assertFailsWith<IllegalStateException> {
            VEvent.builder(defaults).start(zonedStart).rrule(floatingRule).build()
        }
        assertFailsWith<IllegalStateException> {
            VTodo.builder(defaults).start(zonedStart).rrule(dateRule.build()).build()
        }
        assertFailsWith<IllegalStateException> {
            VJournal.builder(defaults).start(utc(2026, 9, 1, 10, 0)).rrule(floatingRule).build()
        }
        VEvent.builder(defaults).rrule(floatingRule).build()
    }
}

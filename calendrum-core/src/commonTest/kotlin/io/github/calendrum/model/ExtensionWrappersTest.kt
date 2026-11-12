package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.time.Clock
import kotlin.time.Instant

class ExtensionWrappersTest {
    private fun calendar(body: String): VCalendar = ICal.parse(
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//t//EN\r\n" + body.trimIndent().replace("\n", "\r\n") +
            "\r\nEND:VCALENDAR\r\n",
    ).calendars.single()

    @Test
    fun rfc9073Example1() {
        val event = calendar(RFC9073_EXAMPLE_1).events.single()
        assertEquals("Beethoven Piano Sonatas", event.summary)
        assertEquals(listOf(ImageDisplay.Badge), event.images.single().display)
        assertEquals("http://example.com/images/concert.png", event.images.single().uri)
        assertEquals(2, event.participants.size)
        val sponsor = event.participants[0]
        assertEquals(ParticipantType.Sponsor, sponsor.participantType)
        assertEquals("dG9tQGZvb2Jhci5xlLmNvbQ", sponsor.uid)
        assertEquals("http://example.com/sponsor.vcf", sponsor.structuredData.single().text)
        assertEquals(ValueType.Uri, sponsor.structuredData.single().valueType)
        assertEquals(ParticipantType.of("PERFORMER:"), event.participants[1].participantType)
        assertNotEquals(ParticipantType.Performer, event.participants[1].participantType)
        assertEquals(listOf("The venue", "Parking for the venue"), event.locations.map { it.name })
        assertEquals("http://dir.example.com/venues/parking.vcf", event.locations[1].structuredData.single().text)
        assertEquals("123456-abcdef-87654321", event.locations[1].uid)
    }

    @Test
    fun rfc9073Example2() {
        val event = calendar(RFC9073_EXAMPLE_2).events.single()
        assertEquals(2, event.attendees.size)
        val participant = event.participants.single()
        assertEquals(listOf("At home"), participant.locationTexts)
        assertEquals("http://www.example.com/people/b.vcf", participant.structuredData.single().text)
        assertEquals(ParticipantType.of("ACTIVE:"), participant.participantType)
    }

    @Test
    fun rfc9073ComponentExamples() {
        val event = calendar(RFC9073_COMPONENTS).events.single()
        val speaker = event.participants.single()
        assertEquals(ParticipantType.Speaker, speaker.participantType)
        assertEquals("A participant", speaker.description)
        assertEquals(" em9lQGZvb2GFtcGxlLmNdrt", speaker.uid)
        val home = speaker.locations.single()
        assertEquals("My home location", home.name)
        assertEquals("http://dir.example.com/addresses/my-home.vcf", home.structuredData.single().text)
        val projector = event.resourceComponents.single()
        assertEquals("The projector", projector.name)
        assertEquals(ResourceType.Projector, projector.resourceType)
        assertEquals("456789-abcdef-98765432", projector.uid)
        assertEquals("The venue", event.locations.single().name)
    }

    @Test
    fun rfc9074AlarmExamples() {
        val snoozed = calendar(RFC9074_SNOOZE).events.single()
        val original = snoozed.alarms[0]
        val snooze = snoozed.alarms[1]
        assertEquals("8297C37D-BA2D-4476-91AE-C1EAA364F8E1", original.uid)
        assertEquals(ICalDateTime.Utc(Instant.parse("2021-03-02T15:15:14Z")), original.acknowledged)
        assertEquals(RelType.Snooze, snooze.relatedTo.single().relType)
        assertEquals(original.uid, snooze.relatedTo.single().uid)
        val proximity = calendar(
            "BEGIN:VEVENT\nUID:x\n" + RFC9074_PROXIMITY + "\nEND:VEVENT",
        ).events.single().alarms.single()
        assertEquals(Proximity.Depart, proximity.proximity)
        assertEquals("Office", proximity.locations.single().name)
        assertEquals("geo:40.443,-79.945;u=10", proximity.locations.single().url)
    }

    @Test
    fun calendarPropertiesOfRfc7986() {
        val calendar = calendar(
            """
            NAME;LANGUAGE=en:Holidays
            NAME;LANGUAGE=de:Feiertage
            DESCRIPTION:Public holidays
            UID:5FC53010-1267-4F8E-BC28-1D7AE55A7C99
            URL:https://example.com/holidays
            LAST-MODIFIED:20260901T100000Z
            CATEGORIES:holidays,public
            REFRESH-INTERVAL;VALUE=DURATION:P1W
            SOURCE;VALUE=URI:https://example.com/holidays.ics
            COLOR:turquoise
            IMAGE;VALUE=URI:https://example.com/logo.png
            """,
        )
        assertEquals(listOf("Holidays", "Feiertage"), calendar.names)
        assertEquals(listOf("Public holidays"), calendar.descriptions)
        assertEquals("5FC53010-1267-4F8E-BC28-1D7AE55A7C99", calendar.uid)
        assertEquals("https://example.com/holidays", calendar.url)
        assertEquals(ICalDateTime.Utc(Instant.parse("2026-09-01T10:00:00Z")), calendar.lastModified)
        assertEquals(listOf("holidays", "public"), calendar.categories)
        assertEquals(ICalDuration.ofWeeks(1), calendar.refreshInterval)
        assertEquals("https://example.com/holidays.ics", calendar.source)
        assertEquals("turquoise", calendar.color)
        assertEquals("https://example.com/logo.png", calendar.images.single().uri)
    }

    @Test
    fun buildersWriteTheNewPropertiesAndComponents() {
        val defaults = BuildDefaults(FixedClock, UidGenerator { "uid-${++uids}" })
        val built = VCalendar("-//t//t//EN", defaults) {
            color = "teal"
            refreshInterval = ICalDuration.ofDays(1)
            source = "https://example.com/feed.ics"
            name("Feed")
            description("All events")
            image(Image("https://example.com/logo.png"))
            categories("a", "b")
            event {
                summary = "Concert"
                color = "red"
                conference(Conference("https://meet.example/1", listOf(ConferenceFeature.Video), "Video"))
                image(Image("https://example.com/c.png", "image/png"))
                styledDescription(StyledDescription("<p>x</p>", ValueType.Text, "text/html"))
                structuredData(StructuredData("https://example.com/e.json", ValueType.Uri))
                link(Link("https://example.com/next", ValueType.Uri, "next"))
                concept("https://example.com/types/music")
                refId("tour-2026")
                participant {
                    participantType = ParticipantType.Performer
                    calendarAddress = "mailto:p@example.com"
                    locationText("On stage")
                    location { name = "Stage" }
                }
                location {
                    name = "Hall"
                    locationTypes = listOf("hall")
                    geo = Geo(55.75, 37.62)
                }
                resourceComponent {
                    name = "Piano"
                    resourceType = ResourceType.of("PIANO")
                }
                alarm {
                    display(Trigger.Relative(ICalDuration.ofMinutes(-15)), "Soon")
                    uid = "alarm-1"
                    acknowledged = ICalDateTime.Utc(FixedClock.now())
                    proximity = Proximity.Arrive
                    relatedTo(RelatedTo("alarm-0", RelType.Snooze))
                }
            }
        }
        val text = ICal.writer().encodeToString(built.component)
        val read = ICal.parse(text).calendars.single()
        assertEquals(built.component, read.component)
        assertEquals("teal", read.color)
        assertEquals(listOf("Feed"), read.names)
        val event = read.events.single()
        assertEquals("Video", event.conferences.single().label)
        assertEquals(listOf("tour-2026"), event.refIds)
        assertEquals(listOf("https://example.com/types/music"), event.concepts)
        assertEquals("next", event.links.single().linkRel)
        val performer = event.participants.single()
        assertEquals(ParticipantType.Performer, performer.participantType)
        assertEquals("uid-2", performer.uid)
        assertEquals(listOf("On stage"), performer.locationTexts)
        assertEquals("Stage", performer.locations.single().name)
        assertEquals(listOf("hall"), event.locations.single().locationTypes)
        assertEquals(ResourceType.of("piano"), event.resourceComponents.single().resourceType)
        val alarm = event.alarms.single()
        assertEquals(Proximity.Arrive, alarm.proximity)
        assertEquals(RelType.Snooze, alarm.relatedTo.single().relType)
        assertSame(event.component, event.edit { }.component)
        val edited = event.edit {
            color = null
            refId("second")
        }
        assertNull(edited.color)
        assertEquals(listOf("tour-2026", "second"), edited.refIds)
        assertFailsWith<IllegalStateException> { Participant.builder().uid("p").build() }
        assertFailsWith<IllegalStateException> { VLocation.builder().build() }
        assertEquals("v", VLocation.builder().uid("v").build().uid)
        assertEquals("r", VResource.builder(defaults).uid("r").build().uid)
    }

    private var uids = 0

    private object FixedClock : Clock {
        override fun now(): Instant = Instant.parse("2026-09-01T10:00:00Z")
    }

    private companion object {
        const val RFC9073_EXAMPLE_1 = """
            BEGIN:VEVENT
            CREATED:20200215T145739Z
            DESCRIPTION: Piano Sonata No 3\n
             Piano Sonata No 30
            DTSTAMP:20200215T145739Z
            DTSTART;TZID=America/New_York:20200315T150000Z
            DTEND;TZID=America/New_York:20200315T163000Z
            LAST-MODIFIED:20200216T145739Z
            SUMMARY:Beethoven Piano Sonatas
            UID:123456
            IMAGE;VALUE=URI;DISPLAY=BADGE;FMTTYPE=image/png:h
             ttp://example.com/images/concert.png
            BEGIN:PARTICIPANT
            PARTICIPANT-TYPE:SPONSOR
            UID:dG9tQGZvb2Jhci5xlLmNvbQ
            STRUCTURED-DATA;VALUE=URI:http://example.com/sponsor.vcf
            END:PARTICIPANT
            BEGIN:PARTICIPANT
            PARTICIPANT-TYPE:PERFORMER:
            UID:em9lQGZvb2GFtcGxlLmNvbQ
            STRUCTURED-DATA;VALUE=URI:http://www.example.com/people/johndoe.vcf
            END:PARTICIPANT
            BEGIN:VLOCATION
            UID:123456-abcdef-98765432
            NAME:The venue
            STRUCTURED-DATA;VALUE=URI:http://dir.example.com/venues/big-hall.vcf
            END:VLOCATION
            BEGIN:VLOCATION
            UID:123456-abcdef-87654321
            NAME:Parking for the venue
            STRUCTURED-DATA;VALUE=URI:http://dir.example.com/venues/parking.vcf
            END:VLOCATION
            END:VEVENT
        """

        const val RFC9073_EXAMPLE_2 = """
            BEGIN:VEVENT
            CREATED:20200215T145739Z
            DTSTAMP:20200215T145739Z
            DTSTART;TZID=America/New_York:20200315T150000Z
            DTEND;TZID=America/New_York:20200315T163000Z
            LAST-MODIFIED:20200216T145739Z
            SUMMARY:Conference planning
            UID:123456
            ORGANIZER:mailto:a@example.com
            ATTENDEE;PARTSTAT=ACCEPTED;CN=A:mailto:a@example.com
            ATTENDEE;RSVP=TRUE;CN=B:mailto:b@example.com
            BEGIN:PARTICIPANT
            PARTICIPANT-TYPE:ACTIVE:
            UID:v39lQGZvb2GFtcGxlLmNvbQ
            STRUCTURED-DATA;VALUE=URI:http://www.example.com/people/b.vcf
            LOCATION:At home
            END:PARTICIPANT
            END:VEVENT
        """

        const val RFC9073_COMPONENTS = """
            BEGIN:VEVENT
            UID:components
            BEGIN:PARTICIPANT
            UID: em9lQGZvb2GFtcGxlLmNdrt
            STRUCTURED-DATA;VALUE=URI;
             http://dir.example.com/vcard/contacts/my-card.vcf
            PARTICIPANT-TYPE:SPEAKER
            DESCRIPTION:A participant
            BEGIN:VLOCATION
            UID:123456-abcdef-98765432
            NAME:My home location
            STRUCTURED-DATA;VALUE=URI:
             http://dir.example.com/addresses/my-home.vcf
            END:VLOCATION
            END:PARTICIPANT
            BEGIN:VLOCATION
            UID:123456-abcdef-98765432
            NAME:The venue
            STRUCTURED-DATA;VALUE=URI:
             http://dir.example.com/venues/big-hall.vcf
            END:VLOCATION
            BEGIN:VRESOURCE
            UID:456789-abcdef-98765432
            NAME:The projector
            RESOURCE-TYPE:projector
            STRUCTURED-DATA;VALUE=URI:http://dir.example.com/projectors/3d.vcf
            END:VRESOURCE
            END:VEVENT
        """

        const val RFC9074_SNOOZE = """
            BEGIN:VEVENT
            CREATED:20210302T151004Z
            UID:AC67C078-CED3-4BF5-9726-832C3749F627
            DTSTAMP:20210302T151516Z
            DTSTART;TZID=America/New_York:20210302T103000
            DTEND;TZID=America/New_York:20210302T113000
            SUMMARY:Meeting
            BEGIN:VALARM
            UID:8297C37D-BA2D-4476-91AE-C1EAA364F8E1
            TRIGGER:-PT15M
            DESCRIPTION:Event reminder
            ACTION:DISPLAY
            ACKNOWLEDGED:20210302T151514Z
            END:VALARM
            BEGIN:VALARM
            UID:DE7B5C34-83FF-47FE-BE9E-FF41AE6DD097
            TRIGGER;VALUE=DATE-TIME:20210302T152000Z
            RELATED-TO;RELTYPE=SNOOZE:8297C37D-BA2D-4476-91AE-C1EAA364F8E1
            DESCRIPTION:Event reminder
            ACTION:DISPLAY
            END:VALARM
            END:VEVENT
        """

        const val RFC9074_PROXIMITY = """BEGIN:VALARM
UID:77D80D14-906B-4257-963F-85B1E734DBB6
ACTION:DISPLAY
TRIGGER;VALUE=DATE-TIME:19760401T005545Z
DESCRIPTION:Remember to buy milk
PROXIMITY:DEPART
BEGIN:VLOCATION
UID:123456-abcdef-98765432
NAME:Office
URL:geo:40.443,-79.945;u=10
END:VLOCATION
END:VALARM"""
    }
}

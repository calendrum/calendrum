package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.Instant

class ExtensionBuilderMembersTest {
    private val stamp = ICalDateTime.Utc(Instant.parse("2026-09-01T08:00:00Z"))
    private val image = Image("https://example.com/i.png", "image/png")
    private val conference = Conference("tel:+1-555-0100", listOf(ConferenceFeature.Phone), "Dial-in")
    private val styled = StyledDescription("<p>x</p>", ValueType.Text, "text/html")
    private val data = StructuredData("{}", ValueType.Text, "application/json", "https://schema.org/Event")
    private val link = Link("https://example.com/next", ValueType.Uri, "next")
    private val attach = Attach.Uri("https://example.com/a.pdf")
    private val status = RequestStatus("2.0", "Success")
    private var uids = 0
    private val defaults = BuildDefaults(
        object : Clock {
            override fun now(): Instant = Instant.parse("2026-09-01T08:00:00Z")
        },
        UidGenerator { "uid-${++uids}" },
    )

    private fun reparsed(component: ICalComponent): ICalComponent =
        ICal.parse(ICal.writer().encodeToString(component)).components.single()

    @Test
    fun calendarMembers() {
        val built = VCalendar.builder(defaults)
            .uid("uid-value")
            .url("https://example.com/url")
            .lastModified(stamp)
            .refreshInterval(ICalDuration.ofHours(2))
            .source("https://example.com/source")
            .color("color-value")
            .name("names-value")
            .description("descriptions-value")
            .categories("a", "b")
            .image(image)
            .build()
        val read = VCalendar(reparsed(built.component))
        assertEquals("uid-value", read.uid)
        assertEquals("https://example.com/url", read.url)
        assertEquals(stamp, read.lastModified)
        assertEquals(ICalDuration.ofHours(2), read.refreshInterval)
        assertEquals("https://example.com/source", read.source)
        assertEquals("color-value", read.color)
        assertEquals(listOf("names-value"), read.names)
        assertEquals(listOf("descriptions-value"), read.descriptions)
        assertEquals(listOf("a", "b"), read.categories)
        assertEquals(listOf(image), read.images)
        val builder = built.toBuilder()
        assertEquals("uid-value", builder.uid)
        builder.uid = null
        assertNull(builder.uid)
        assertEquals("https://example.com/url", builder.url)
        builder.url = null
        assertNull(builder.url)
        assertEquals(stamp, builder.lastModified)
        builder.lastModified = null
        assertNull(builder.lastModified)
        assertEquals(ICalDuration.ofHours(2), builder.refreshInterval)
        builder.refreshInterval = null
        assertNull(builder.refreshInterval)
        assertEquals("https://example.com/source", builder.source)
        builder.source = null
        assertNull(builder.source)
        assertEquals("color-value", builder.color)
        builder.color = null
        assertNull(builder.color)
        builder.build()
    }

    @Test
    fun eventMembers() {
        val built = VEvent.builder(defaults)
            .color("color-value")
            .image(image)
            .conference(conference)
            .styledDescription(styled)
            .structuredData(data)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .participant { participantType = ParticipantType.Active }
            .participant(Participant.builder(defaults).participantType(ParticipantType.Speaker).build())
            .participants { assertEquals(2, items.size) }
            .location { name = "place" }
            .location(VLocation.builder(defaults).name("second").build())
            .locations { assertEquals(2, items.size) }
            .resourceComponent { name = "thing" }
            .resourceComponent(VResource.builder(defaults).name("second").build())
            .resourceComponents { assertEquals(2, items.size) }
            .build()
        val read = VEvent(reparsed(built.component))
        assertEquals("color-value", read.color)
        assertEquals(listOf(image), read.images)
        assertEquals(listOf(conference), read.conferences)
        assertEquals(listOf(styled), read.styledDescriptions)
        assertEquals(listOf(data), read.structuredData)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(2, read.participants.size)
        assertEquals(2, read.locations.size)
        assertEquals(2, read.resourceComponents.size)
        val builder = built.toBuilder()
        assertEquals("color-value", builder.color)
        builder.color = null
        assertNull(builder.color)
        assertEquals(2, builder.participants.items.size)
        assertEquals(2, builder.locations.items.size)
        assertEquals(2, builder.resourceComponents.items.size)
        builder.build()
    }

    @Test
    fun todoMembers() {
        val built = VTodo.builder(defaults)
            .color("color-value")
            .image(image)
            .conference(conference)
            .styledDescription(styled)
            .structuredData(data)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .participant { participantType = ParticipantType.Active }
            .participant(Participant.builder(defaults).participantType(ParticipantType.Speaker).build())
            .participants { assertEquals(2, items.size) }
            .location { name = "place" }
            .location(VLocation.builder(defaults).name("second").build())
            .locations { assertEquals(2, items.size) }
            .resourceComponent { name = "thing" }
            .resourceComponent(VResource.builder(defaults).name("second").build())
            .resourceComponents { assertEquals(2, items.size) }
            .build()
        val read = VTodo(reparsed(built.component))
        assertEquals("color-value", read.color)
        assertEquals(listOf(image), read.images)
        assertEquals(listOf(conference), read.conferences)
        assertEquals(listOf(styled), read.styledDescriptions)
        assertEquals(listOf(data), read.structuredData)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(2, read.participants.size)
        assertEquals(2, read.locations.size)
        assertEquals(2, read.resourceComponents.size)
        val builder = built.toBuilder()
        assertEquals("color-value", builder.color)
        builder.color = null
        assertNull(builder.color)
        assertEquals(2, builder.participants.items.size)
        assertEquals(2, builder.locations.items.size)
        assertEquals(2, builder.resourceComponents.items.size)
        builder.build()
    }

    @Test
    fun journalMembers() {
        val built = VJournal.builder(defaults)
            .color("color-value")
            .image(image)
            .styledDescription(styled)
            .structuredData(data)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .participant { participantType = ParticipantType.Active }
            .participant(Participant.builder(defaults).participantType(ParticipantType.Speaker).build())
            .participants { assertEquals(2, items.size) }
            .location { name = "place" }
            .location(VLocation.builder(defaults).name("second").build())
            .locations { assertEquals(2, items.size) }
            .resourceComponent { name = "thing" }
            .resourceComponent(VResource.builder(defaults).name("second").build())
            .resourceComponents { assertEquals(2, items.size) }
            .build()
        val read = VJournal(reparsed(built.component))
        assertEquals("color-value", read.color)
        assertEquals(listOf(image), read.images)
        assertEquals(listOf(styled), read.styledDescriptions)
        assertEquals(listOf(data), read.structuredData)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(2, read.participants.size)
        assertEquals(2, read.locations.size)
        assertEquals(2, read.resourceComponents.size)
        val builder = built.toBuilder()
        assertEquals("color-value", builder.color)
        builder.color = null
        assertNull(builder.color)
        assertEquals(2, builder.participants.items.size)
        assertEquals(2, builder.locations.items.size)
        assertEquals(2, builder.resourceComponents.items.size)
        builder.build()
    }

    @Test
    fun freeBusyMembers() {
        val built = VFreeBusy.builder(defaults)
            .styledDescription(styled)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .relatedTo("p")
            .relatedTo(RelatedTo("q", RelType.Next))
            .participant { participantType = ParticipantType.Active }
            .participant(Participant.builder(defaults).participantType(ParticipantType.Speaker).build())
            .participants { assertEquals(2, items.size) }
            .location { name = "place" }
            .location(VLocation.builder(defaults).name("second").build())
            .locations { assertEquals(2, items.size) }
            .resourceComponent { name = "thing" }
            .resourceComponent(VResource.builder(defaults).name("second").build())
            .resourceComponents { assertEquals(2, items.size) }
            .build()
        val read = VFreeBusy(reparsed(built.component))
        assertEquals(listOf(styled), read.styledDescriptions)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(listOf("p", "q"), read.relatedTo.map { it.uid })
        assertEquals(2, read.participants.size)
        assertEquals(2, read.locations.size)
        assertEquals(2, read.resourceComponents.size)
        val builder = built.toBuilder()
        assertEquals(2, builder.participants.items.size)
        assertEquals(2, builder.locations.items.size)
        assertEquals(2, builder.resourceComponents.items.size)
        builder.build()
    }

    @Test
    fun alarmMembers() {
        val built = VAlarm.builder(defaults)
            .uid("uid-value")
            .acknowledged(stamp)
            .proximity(Proximity.Connect)
            .relatedTo("p")
            .relatedTo(RelatedTo("q", RelType.Next))
            .styledDescription(styled)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .location { name = "place" }
            .location(VLocation.builder(defaults).name("second").build())
            .locations { assertEquals(2, items.size) }
            .build()
        val read = VAlarm(reparsed(built.component))
        assertEquals("uid-value", read.uid)
        assertEquals(stamp, read.acknowledged)
        assertEquals(Proximity.Connect, read.proximity)
        assertEquals(listOf("p", "q"), read.relatedTo.map { it.uid })
        assertEquals(listOf(styled), read.styledDescriptions)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(2, read.locations.size)
        val builder = built.toBuilder()
        assertEquals("uid-value", builder.uid)
        builder.uid = null
        assertNull(builder.uid)
        assertEquals(stamp, builder.acknowledged)
        builder.acknowledged = null
        assertNull(builder.acknowledged)
        assertEquals(Proximity.Connect, builder.proximity)
        builder.proximity = null
        assertNull(builder.proximity)
        assertEquals(2, builder.locations.items.size)
        builder.build()
    }

    @Test
    fun timeZoneMembers() {
        val built = VTimeZone.builder(defaults)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .relatedTo("p")
            .relatedTo(RelatedTo("q", RelType.Next))
            .build()
        val read = VTimeZone(reparsed(built.component))
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(listOf("p", "q"), read.relatedTo.map { it.uid })
        val builder = built.toBuilder()

        builder.build()
    }

    @Test
    fun observanceMembers() {
        val built = VTimeZone.Daylight.builder()
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .relatedTo("p")
            .relatedTo(RelatedTo("q", RelType.Next))
            .build()
        val read = VTimeZone.Daylight(reparsed(built.component))
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(listOf("p", "q"), read.relatedTo.map { it.uid })
        val builder = built.toBuilder()

        builder.build()
    }

    @Test
    fun locationMembers() {
        val built = VLocation.builder(defaults)
            .uid("uid-value")
            .name("name-value")
            .description("description-value")
            .geo(Geo(1.5, 2.5))
            .url("https://example.com/url")
            .locationTypes(listOf("a", "b"))
            .structuredData(data)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .relatedTo("p")
            .relatedTo(RelatedTo("q", RelType.Next))
            .build()
        val read = VLocation(reparsed(built.component))
        assertEquals("uid-value", read.uid)
        assertEquals("name-value", read.name)
        assertEquals("description-value", read.description)
        assertEquals(Geo(1.5, 2.5), read.geo)
        assertEquals("https://example.com/url", read.url)
        assertEquals(listOf("a", "b"), read.locationTypes)
        assertEquals(listOf(data), read.structuredData)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(listOf("p", "q"), read.relatedTo.map { it.uid })
        val builder = built.toBuilder()
        assertEquals("uid-value", builder.uid)
        builder.uid = null
        assertNull(builder.uid)
        assertEquals("name-value", builder.name)
        builder.name = null
        assertNull(builder.name)
        assertEquals("description-value", builder.description)
        builder.description = null
        assertNull(builder.description)
        assertEquals(Geo(1.5, 2.5), builder.geo)
        builder.geo = null
        assertNull(builder.geo)
        assertEquals("https://example.com/url", builder.url)
        builder.url = null
        assertNull(builder.url)
        assertEquals(listOf("a", "b"), builder.locationTypes)
        builder.locationTypes = null
        builder.build()
    }

    @Test
    fun resourceMembers() {
        val built = VResource.builder(defaults)
            .uid("uid-value")
            .name("name-value")
            .description("description-value")
            .geo(Geo(1.5, 2.5))
            .resourceType(ResourceType.Room)
            .structuredData(data)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .relatedTo("p")
            .relatedTo(RelatedTo("q", RelType.Next))
            .build()
        val read = VResource(reparsed(built.component))
        assertEquals("uid-value", read.uid)
        assertEquals("name-value", read.name)
        assertEquals("description-value", read.description)
        assertEquals(Geo(1.5, 2.5), read.geo)
        assertEquals(ResourceType.Room, read.resourceType)
        assertEquals(listOf(data), read.structuredData)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(listOf("p", "q"), read.relatedTo.map { it.uid })
        val builder = built.toBuilder()
        assertEquals("uid-value", builder.uid)
        builder.uid = null
        assertNull(builder.uid)
        assertEquals("name-value", builder.name)
        builder.name = null
        assertNull(builder.name)
        assertEquals("description-value", builder.description)
        builder.description = null
        assertNull(builder.description)
        assertEquals(Geo(1.5, 2.5), builder.geo)
        builder.geo = null
        assertNull(builder.geo)
        assertEquals(ResourceType.Room, builder.resourceType)
        builder.resourceType = null
        assertNull(builder.resourceType)
        builder.build()
    }

    @Test
    fun participantMembers() {
        val built = Participant.builder(defaults)
            .participantType(ParticipantType.Contact)
            .uid("uid-value")
            .calendarAddress("mailto:p@example.com")
            .created(stamp)
            .description("description-value")
            .stamp(stamp)
            .geo(Geo(1.5, 2.5))
            .lastModified(stamp)
            .priority(3)
            .sequence(3)
            .status(EventStatus.Confirmed)
            .summary("summary-value")
            .url("https://example.com/url")
            .attach(attach)
            .categories("a", "b")
            .comment("comments-value")
            .contact("Desk")
            .locationText("Here")
            .requestStatus(status)
            .relatedTo("p")
            .relatedTo(RelatedTo("q", RelType.Next))
            .resources("R")
            .styledDescription(styled)
            .structuredData(data)
            .link(link)
            .concept("https://example.com/c")
            .refId("refIds-value")
            .location { name = "place" }
            .location(VLocation.builder(defaults).name("second").build())
            .locations { assertEquals(2, items.size) }
            .resourceComponent { name = "thing" }
            .resourceComponent(VResource.builder(defaults).name("second").build())
            .resourceComponents { assertEquals(2, items.size) }
            .build()
        val read = Participant(reparsed(built.component))
        assertEquals(ParticipantType.Contact, read.participantType)
        assertEquals("uid-value", read.uid)
        assertEquals("mailto:p@example.com", read.calendarAddress)
        assertEquals(stamp, read.created)
        assertEquals("description-value", read.description)
        assertEquals(stamp, read.stamp)
        assertEquals(Geo(1.5, 2.5), read.geo)
        assertEquals(stamp, read.lastModified)
        assertEquals(3, read.priority)
        assertEquals(3, read.sequence)
        assertEquals(EventStatus.Confirmed, read.status)
        assertEquals("summary-value", read.summary)
        assertEquals("https://example.com/url", read.url)
        assertEquals(listOf(attach), read.attachments)
        assertEquals(listOf("a", "b"), read.categories)
        assertEquals(listOf("comments-value"), read.comments)
        assertEquals(listOf("Desk"), read.contacts)
        assertEquals(listOf("Here"), read.locationTexts)
        assertEquals(listOf(status), read.requestStatuses)
        assertEquals(listOf("p", "q"), read.relatedTo.map { it.uid })
        assertEquals(listOf("R"), read.resources)
        assertEquals(listOf(styled), read.styledDescriptions)
        assertEquals(listOf(data), read.structuredData)
        assertEquals(listOf(link), read.links)
        assertEquals(listOf("https://example.com/c"), read.concepts)
        assertEquals(listOf("refIds-value"), read.refIds)
        assertEquals(2, read.locations.size)
        assertEquals(2, read.resourceComponents.size)
        val builder = built.toBuilder()
        assertEquals(ParticipantType.Contact, builder.participantType)
        builder.participantType = null
        assertNull(builder.participantType)
        assertEquals("uid-value", builder.uid)
        builder.uid = null
        assertNull(builder.uid)
        assertEquals("mailto:p@example.com", builder.calendarAddress)
        builder.calendarAddress = null
        assertNull(builder.calendarAddress)
        assertEquals(stamp, builder.created)
        builder.created = null
        assertNull(builder.created)
        assertEquals("description-value", builder.description)
        builder.description = null
        assertNull(builder.description)
        assertEquals(stamp, builder.stamp)
        builder.stamp = null
        assertNull(builder.stamp)
        assertEquals(Geo(1.5, 2.5), builder.geo)
        builder.geo = null
        assertNull(builder.geo)
        assertEquals(stamp, builder.lastModified)
        builder.lastModified = null
        assertNull(builder.lastModified)
        assertEquals(3, builder.priority)
        builder.priority = null
        assertNull(builder.priority)
        assertEquals(3, builder.sequence)
        builder.sequence = null
        assertNull(builder.sequence)
        assertEquals(EventStatus.Confirmed, builder.status)
        builder.status = null
        assertNull(builder.status)
        assertEquals("summary-value", builder.summary)
        builder.summary = null
        assertNull(builder.summary)
        assertEquals("https://example.com/url", builder.url)
        builder.url = null
        assertNull(builder.url)
        assertEquals(2, builder.locations.items.size)
        assertEquals(2, builder.resourceComponents.items.size)
        builder.build()
    }
}

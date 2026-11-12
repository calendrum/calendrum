package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class ExtensionPropertiesTest {
    private fun <T : Any> circular(key: PropertyKey<T>, value: T, component: String = "VEVENT"): T {
        val property = encoded(key, value)
        val again = assertLenient(key.codec.decode(property, DecodeContext.Strict))
        assertEquals(value, again, "$property in $component")
        return again
    }

    private fun <T : Any> example(key: PropertyKey<T>, line: String, component: String = "VEVENT"): T =
        circular(key, value(key, line, component), component)

    private fun <T : Any> folded(key: PropertyKey<T>, text: String, component: String = "VEVENT"): T {
        val result = ICal.parse(
            "BEGIN:$component\r\n" + text.trimIndent().replace("\n", "\r\n") + "\r\nEND:$component\r\n",
        )
        val property = result.components.single().properties.single()
        val decoded = assertLenient(key.codec.decode(property, DecodeContext.Lenient))
        return circular(key, decoded, component)
    }

    @Test
    fun rfc7986CalendarProperties() {
        assertEquals("Company Vacation Days", example(Properties.NAME, "NAME:Company Vacation Days", "VCALENDAR"))
        assertEquals(
            "5FC53010-1267-4F8E-BC28-1D7AE55A7C99",
            example(Properties.UID, "UID:5FC53010-1267-4F8E-BC28-1D7AE55A7C99", "VCALENDAR"),
        )
        val refresh = example(Properties.REFRESH_INTERVAL, "REFRESH-INTERVAL;VALUE=DURATION:P1W", "VCALENDAR")
        assertEquals(1, refresh.weeks)
        assertEquals("REFRESH-INTERVAL;VALUE=DURATION:P1W", lineOf(encoded(Properties.REFRESH_INTERVAL, refresh)))
        assertEquals(
            "https://example.com/holidays.ics",
            example(Properties.SOURCE, "SOURCE;VALUE=URI:https://example.com/holidays.ics", "VCALENDAR"),
        )
        assertEquals("turquoise", example(Properties.COLOR, "COLOR:turquoise"))
    }

    @Test
    fun rfc7986Images() {
        val badge = folded(
            Properties.IMAGE,
            """
            IMAGE;VALUE=URI;DISPLAY=BADGE;FMTTYPE=image/png:h
             ttp://example.com/images/party.png
            """,
        )
        assertEquals("http://example.com/images/party.png", badge.uri)
        assertEquals("image/png", badge.formatType)
        assertEquals(listOf(ImageDisplay.Badge), badge.display)
        assertFalse(badge.isBinary)
        val thumbnail = folded(
            Properties.IMAGE,
            """
            IMAGE;VALUE=URI;DISPLAY=BADGE,THUMBNAIL;FMTTYPE=image/png:https://exa
             mple.com/images/weather-cloudy.png
            """,
        )
        assertEquals(listOf(ImageDisplay.Badge, ImageDisplay.Thumbnail), thumbnail.display)
        val plain = value(Properties.IMAGE, "IMAGE:https://example.com/a.png")
        assertEquals(listOf(ImageDisplay.Badge), plain.display)
        val binary = example(Properties.IMAGE, "IMAGE;FMTTYPE=image/png;ENCODING=BASE64;VALUE=BINARY:QUJD")
        assertContentEquals(byteArrayOf(65, 66, 67), binary.bytes())
        assertNull(binary.uri)
        assertEquals(
            Image(byteArrayOf(65, 66, 67), "image/png"),
            circular(Properties.IMAGE, Image(byteArrayOf(65, 66, 67), "image/png")),
        )
        assertEquals("IMAGE;VALUE=URI:https://x.example", lineOf(encoded(Properties.IMAGE, Image("https://x.example"))))
    }

    @Test
    fun rfc7986Conferences() {
        val moderator = folded(
            Properties.CONFERENCE,
            """
            CONFERENCE;VALUE=URI;FEATURE=PHONE,MODERATOR;
             LABEL=Moderator dial-in:tel:+1-412-555-0123,,,654321
            """,
        )
        assertEquals("tel:+1-412-555-0123,,,654321", moderator.uri)
        assertEquals(listOf(ConferenceFeature.Phone, ConferenceFeature.Moderator), moderator.features)
        assertEquals("Moderator dial-in", moderator.label)
        val examples = listOf(
            "CONFERENCE;VALUE=URI;FEATURE=PHONE;\r\n LABEL=Attendee dial-in:tel:+1-412-555-0123,,,555123",
            "CONFERENCE;VALUE=URI;FEATURE=PHONE;\r\n LABEL=Attendee dial-in:tel:+1-888-555-0456,,,555123",
            "CONFERENCE;VALUE=URI;FEATURE=CHAT;\r\n LABEL=Chat room:xmpp:chat-123@conference.example.com",
            "CONFERENCE;VALUE=URI;FEATURE=AUDIO,VIDEO;\r\n LABEL=Attendee dial-in:https://chat.example.com/audio?id=123456",
            "CONFERENCE;VALUE=URI;FEATURE=AUDIO:rtsp://audio.example.com/\r\n event",
            "CONFERENCE;VALUE=URI;FEATURE=AUDIO,VIDEO:https://video-chat.exam\r\n ple.com/;group-id=1234",
        )
        for (text in examples) folded(Properties.CONFERENCE, text.replace("\r\n", "\n"))
        assertEquals(
            "rtsp://audio.example.com/event",
            folded(Properties.CONFERENCE, examples[4].replace("\r\n", "\n")).uri,
        )
        val built = Conference("https://meet.example/x", listOf(ConferenceFeature.Video), "Room")
        assertEquals(
            "CONFERENCE;VALUE=URI;FEATURE=VIDEO;LABEL=Room:https://meet.example/x",
            lineOf(encoded(Properties.CONFERENCE, built)),
        )
        circular(Properties.CONFERENCE, built)
        assertEquals(emptyList(), circular(Properties.CONFERENCE, Conference("tel:1")).features)
    }

    @Test
    fun rfc7986EmailParameter() {
        val attendee = value(
            Properties.ATTENDEE,
            "ATTENDEE;CN=Cyrus Daboo;EMAIL=cyrus@example.com:mailto:opaque-token-1234@example.com",
        )
        assertEquals("cyrus@example.com", attendee.email)
        val property = propertyOf("ATTENDEE;EMAIL=cyrus@example.com:mailto:x@example.com")
        assertEquals("cyrus@example.com", property[Parameters.EMAIL])
    }

    @Test
    fun rfc9073Properties() {
        assertEquals(
            ParticipantType.Speaker,
            example(Properties.PARTICIPANT_TYPE, "PARTICIPANT-TYPE:SPEAKER", "PARTICIPANT"),
        )
        assertEquals(ResourceType.Projector, example(Properties.RESOURCE_TYPE, "RESOURCE-TYPE:projector", "VRESOURCE"))
        assertEquals(
            listOf("hotel", "restaurant"),
            example(Properties.LOCATION_TYPE, "LOCATION-TYPE:hotel,restaurant", "VLOCATION"),
        )
        assertEquals(
            "mailto:b@example.com",
            example(Properties.CALENDAR_ADDRESS, "CALENDAR-ADDRESS:mailto:b@example.com", "PARTICIPANT"),
        )
        val uri = example(Properties.STYLED_DESCRIPTION, "STYLED-DESCRIPTION;VALUE=URI:http://example.org/desc001.html")
        assertEquals(ValueType.Uri, uri.valueType)
        assertEquals("http://example.org/desc001.html", uri.value)
        val derived = folded(
            Properties.STYLED_DESCRIPTION,
            """
            STYLED-DESCRIPTION;FMTTYPE=text/html;
             DERIVED=TRUE:<html>...</html>
            """,
        )
        assertTrue(derived.isDerived)
        assertEquals(ValueType.Text, derived.valueType)
        assertEquals("text/html", derived.formatType)
        assertEquals("<html>...</html>", derived.value)
        assertTrue(propertyOf("STYLED-DESCRIPTION;DERIVED=TRUE:x")[Parameters.DERIVED] == true)
        val built = StyledDescription("<b>Hi</b>", ValueType.Text, "text/html")
        assertEquals(
            "STYLED-DESCRIPTION;VALUE=TEXT;FMTTYPE=text/html:<b>Hi</b>",
            lineOf(encoded(Properties.STYLED_DESCRIPTION, built)),
        )
        assertFailsWith<IllegalArgumentException> { StyledDescription("x", ValueType.Binary) }
    }

    @Test
    fun rfc9073StructuredData() {
        val sports = folded(
            Properties.STRUCTURED_DATA,
            """
            STRUCTURED-DATA;FMTTYPE=application/ld+json;
             SCHEMA="https://schema.org/SportsEvent";
             VALUE=TEXT:{\n
               "@context": "http://schema.org"\,\n
               "@type": "SportsEvent"\,\n
               "homeTeam": "Pittsburgh Pirates"\,\n
               "awayTeam": "San Francisco Giants"\n
             }\n
            """,
        )
        assertEquals(ValueType.Text, sports.valueType)
        assertEquals("https://schema.org/SportsEvent", sports.schema)
        assertEquals("application/ld+json", sports.formatType)
        assertTrue(sports.text.orEmpty().startsWith("{\n  \"@context\": \"http://schema.org\",\n"))
        assertTrue(sports.text.orEmpty().endsWith("\"awayTeam\": \"San Francisco Giants\"\n}\n"))
        val flight = folded(Properties.STRUCTURED_DATA, FLIGHT)
        assertEquals(ValueType.Binary, flight.valueType)
        assertEquals("https://schema.org/FlightReservation", flight.schema)
        assertTrue(flight.bytes()!!.decodeToString().contains("\"reservationId\": \"RXJ34P\""))
        val venue = example(
            Properties.STRUCTURED_DATA,
            "STRUCTURED-DATA;VALUE=URI:http://dir.example.com/venues/big-hall.vcf",
            "VLOCATION",
        )
        assertEquals(ValueType.Uri, venue.valueType)
        circular(
            Properties.STRUCTURED_DATA,
            StructuredData(byteArrayOf(1, 2), "application/x-test", "https://schema.example/x"),
        )
        circular(
            Properties.STRUCTURED_DATA,
            StructuredData("{}", ValueType.Text, "application/json", "https://schema.example/y"),
        )
    }

    @Test
    fun rfc9074Properties() {
        val acknowledged = example(Properties.ACKNOWLEDGED, "ACKNOWLEDGED:20090604T084500Z", "VALARM")
        assertEquals(ICalDateTime.Utc(Instant.parse("2009-06-04T08:45:00Z")), acknowledged)
        assertMalformed(
            strict(Properties.ACKNOWLEDGED, "ACKNOWLEDGED:20090604T084500", "VALARM"),
            "ICAL-V027@2:14",
            "ICAL-V001@2:14",
        )
        assertEquals(Proximity.Depart, example(Properties.PROXIMITY, "PROXIMITY:DEPART", "VALARM"))
        val snooze = example(
            Properties.RELATED_TO,
            "RELATED-TO;RELTYPE=SNOOZE:8297C37D-BA2D-4476-91AE-C1EAA364F8E1",
            "VALARM",
        )
        assertEquals(RelType.Snooze, snooze.relType)
        assertEquals("8297C37D-BA2D-4476-91AE-C1EAA364F8E1", snooze.uid)
    }

    @Test
    fun rfc9253Properties() {
        assertEquals(
            "https://example.com/event-types/arts/music",
            example(Properties.CONCEPT, "CONCEPT:https://example.com/event-types/arts/music"),
        )
        assertEquals("itinerary-2014-11-17", example(Properties.REFID, "REFID:itinerary-2014-11-17"))
        val source = folded(
            Properties.LINK,
            """
            LINK;LINKREL=SOURCE;LABEL=Venue;VALUE=URI:
             https://example.com/events
            """,
        )
        assertEquals("https://example.com/events", source.value)
        assertEquals("SOURCE", source.linkRel)
        assertEquals("Venue", source.label)
        assertEquals(ValueType.Uri, source.valueType)
        val derived = folded(
            Properties.LINK,
            """
            LINK;LINKREL="https://example.com/linkrel/derivedFrom";
             VALUE=URI:
             https://example.com/tasks/01234567-abcd1234.ics
            """,
        )
        assertEquals("https://example.com/linkrel/derivedFrom", derived.linkRel)
        val xml = folded(
            Properties.LINK,
            """
            LINK;LINKREL="https://example.com/linkrel/costStructure";
             VALUE=XML-REFERENCE:
             https://example.com/xmlDocs/bidFramework.xml
             #xpointer(descendant::CostStruc/range-to(
             following::CostStrucEND[1]))
            """,
        )
        assertEquals(ValueType.XmlReference, xml.valueType)
        assertEquals(
            "https://example.com/xmlDocs/bidFramework.xml#xpointer(descendant::CostStruc/range-to(following::CostStrucEND[1]))",
            xml.value,
        )
        val uid = circular(Properties.LINK, Link("a\\b,c", ValueType.Uid, "next"))
        assertEquals("a\\b,c", uid.value)
        assertFailsWith<IllegalArgumentException> { Link("x", ValueType.Text, "next") }
    }

    @Test
    fun rfc9253RelatedTo() {
        assertEquals(
            "jsmith.part7.19960817T083000.xyzMail@example.com",
            example(Properties.RELATED_TO, "RELATED-TO:jsmith.part7.19960817T083000.xyzMail@example.com").uid,
        )
        val uri = folded(
            Properties.RELATED_TO,
            """
            RELATED-TO;VALUE=URI;RELTYPE=STARTTOFINISH:
             https://example.com/caldav/user/jb/cal/
             19960401-080045-4000F192713.ics
            """,
        )
        assertEquals("https://example.com/caldav/user/jb/cal/19960401-080045-4000F192713.ics", uri.uid)
        assertEquals(RelType.StartToFinish, uri.relType)
        val gap = example(Properties.RELATED_TO, "RELATED-TO;RELTYPE=FINISHTOSTART;GAP=P1D:task-b")
        assertEquals(ICalDuration.ofDays(1), gap.gap)
        assertEquals(RelType.FinishToStart, gap.relType)
        assertEquals(ICalDuration.ofDays(1), propertyOf("RELATED-TO;GAP=P1D:x")[Parameters.GAP])
        val built = RelatedTo("task-b", RelType.FinishToStart, ICalDuration.ofDays(-1))
        assertEquals("RELATED-TO;RELTYPE=FINISHTOSTART;GAP=-P1D:task-b", lineOf(encoded(Properties.RELATED_TO, built)))
        assertLenient(lenient(Properties.RELATED_TO, "RELATED-TO;GAP=soon:x"), "ICAL-V001@2:16")
        assertMalformed(strict(Properties.RELATED_TO, "RELATED-TO;GAP=soon:x"), "ICAL-V001@2:16")
        assertEquals(RelType.DependsOn, RelType.of("depends-on"))
    }

    @Test
    fun newParametersHaveKeys() {
        val property = propertyOf("X-TEST;ORDER=2;DISPLAY=GRAPHIC,X-FANCY;FEATURE=SCREEN;LABEL=Room;LINKREL=next:x")
        assertEquals(2, property[Parameters.ORDER])
        assertEquals(listOf(ImageDisplay.Graphic, ImageDisplay.of("x-fancy")), property[Parameters.DISPLAY])
        assertEquals(listOf(ConferenceFeature.Screen), property[Parameters.FEATURE])
        assertEquals("Room", property[Parameters.LABEL])
        assertEquals("next", property[Parameters.LINKREL])
        assertNull(propertyOf("X-TEST;ORDER=0:x")[Parameters.ORDER])
        assertNull(propertyOf("X-TEST;ORDER=a:x")[Parameters.ORDER])
        assertEquals("X-FANCY", ImageDisplay.of("x-fancy").value)
        assertEquals("ORDER=3", lineOf(Parameters.ORDER.codec.encode(3, EncodeContext("ORDER"))))
        assertFailsWith<IllegalArgumentException> { Parameters.ORDER.codec.encode(0, EncodeContext("ORDER")) }
    }

    private companion object {
        val FLIGHT = """
            STRUCTURED-DATA;FMTTYPE=application/ld+json;
             SCHEMA="https://schema.org/FlightReservation";
             ENCODING=BASE64;VALUE=BINARY:ICAgIDxzY3JpcHQgdHlwZT0iYXBwb
             GljYXRpb24vbGQranNvbiI+CiAgICB7CiAgICAgICJAY29
             udGV4dCI6ICJodHRwOi8vc2NoZW1hLm9yZyIsCiAgICAgICJAdHlwZSI
             6ICJGbGlnaHRSZXNlcnZhdGlvbiIsCiAgICAgICJyZXNlcnZhdGlvbkl
             kIjogIlJYSjM0UCIsCiAgICAgICJyZXNlcnZhdGlvblN0YXR1cyI6ICJ
             odHRwOi8vc2NoZW1hLm9yZy9SZXNlcnZhdGlvbkNvbmZpcm1lZCIsCiA
             gICAgICJwYXNzZW5nZXJQcmlvcml0eVN0YXR1cyI6ICJGYXN0IFRyYWN
             rIiwKICAgICAgInBhc3NlbmdlclNlcXVlbmNlTnVtYmVyIjogIkFCQzE
             yMyIsCiAgICAgICJzZWN1cml0eVNjcmVlbmluZyI6ICJUU0EgUHJlQ2h
             lY2siLAogICAgICAidW5kZXJOYW1lIjogewogICAgICAgICJAdHlwZSI
             6ICJQZXJzb24iLAogICAgICAgICJuYW1lIjogIkV2YSBHcmVlbiIKICA
             gICAgfSwKICAgICAgInJlc2VydmF0aW9uRm9yIjogewogICAgICAgICJ
             AdHlwZSI6ICJGbGlnaHQiLAogICAgICAgICJmbGlnaHROdW1iZXIiOiA
             iVUExMTAiLAogICAgICAgICJwcm92aWRlciI6IHsKICAgICAgICAgICJ
             AdHlwZSI6ICJBaXJsaW5lIiwKICAgICAgICAgICJuYW1lIjogIkNvbnR
             pbmVudGFsIiwKICAgICAgICAgICJpYXRhQ29kZSI6ICJDTyIsCiAgICA
             gICAgICAiYm9hcmRpbmdQb2xpY3kiOiAiaHR0cDovL3NjaGVtYS5vcmc
             vWm9uZUJvYXJkaW5nUG9saWN5IgogICAgICAgIH0sCiAgICAgICAgInN
             lbGxlciI6IHsKICAgICAgICAgICJAdHlwZSI6ICJBaXJsaW5lIiwKICA
             gICAgICAgICJuYW1lIjogIlVuaXRlZCIsCiAgICAgICAgICAiaWF0YUN
             vZGUiOiAiVUEiCiAgICAgICAgfSwKICAgICAgICAiZGVwYXJ0dXJlQWl
             ycG9ydCI6IHsKICAgICAgICAgICJAdHlwZSI6ICJBaXJwb3J0IiwKICA
             gICAgICAgICJuYW1lIjogIlNhbiBGcmFuY2lzY28gQWlycG9ydCIsCiA
             gICAgICAgICAiaWF0YUNvZGUiOiAiU0ZPIgogICAgICAgIH0sCiAgICA
             gICAgImRlcGFydHVyZVRpbWUiOiAiMjAxNy0wMy0wNFQyMDoxNTowMC0
             wODowMCIsCiAgICAgICAgImFycml2YWxBaXJwb3J0IjogewogICAgICA
             gICAgIkB0eXBlIjogIkFpcnBvcnQiLAogICAgICAgICAgIm5hbWUiOiA
             iSm9obiBGLiBLZW5uZWR5IEludGVybmF0aW9uYWwgQWlycG9ydCIsCiA
             gICAgICAgICAiaWF0YUNvZGUiOiAiSkZLIgogICAgICAgIH0sCiAgICA
             gICAgImFycml2YWxUaW1lIjogIjIwMTctMDMtMDVUMDY6MzA6MDAtMDU
             6MDAiCiAgICAgIH0KICAgIH0KICAgIDwvc2NyaXB0Pg==
        """
    }
}

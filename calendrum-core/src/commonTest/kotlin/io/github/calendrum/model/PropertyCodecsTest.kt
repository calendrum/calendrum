package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class PropertyCodecsTest {
    private fun reparse(property: ICalProperty, component: String): ICalProperty {
        val text = ICal.writer().encodeToString(ICalComponent.builder(component).property(property).build())
        return ICal.parse(text).components.single().properties.single()
    }

    private fun <T : Any> roundTrip(key: PropertyKey<T>, line: String, component: String = "VEVENT"): T {
        val decoded = value(key, line, component)
        val encoded = encoded(key, decoded)
        assertEquals(propertyOf(line, component), encoded, line)
        val again = key.codec.decode(reparse(encoded, component), DecodeContext.Strict)
        assertEquals(decoded, assertIs<Decoded.Value<T>>(again, line).value, line)
        assertEquals(decoded.hashCode(), again.value.hashCode(), line)
        return decoded
    }

    private fun utc(text: String): ICalDateTime {
        val local = LocalDateTime(
            text.substring(0, 4).toInt(),
            text.substring(4, 6).toInt(),
            text.substring(6, 8).toInt(),
            text.substring(9, 11).toInt(),
            text.substring(11, 13).toInt(),
            text.substring(13, 15).toInt(),
        )
        return ICalDateTime.Utc(local.toInstant(TimeZone.UTC))
    }

    @Test
    fun calendarProperties() {
        assertEquals("GREGORIAN", roundTrip(Properties.CALSCALE, "CALSCALE:GREGORIAN", "VCALENDAR"))
        assertSame(Method.Request, roundTrip(Properties.METHOD, "METHOD:REQUEST", "VCALENDAR"))
        assertEquals(
            "-//ABC Corporation//NONSGML My Product//EN",
            roundTrip(Properties.PRODID, "PRODID:-//ABC Corporation//NONSGML My Product//EN", "VCALENDAR"),
        )
        assertEquals("2.0", roundTrip(Properties.VERSION, "VERSION:2.0", "VCALENDAR"))
    }

    @Test
    fun descriptiveTexts() {
        val comment = "COMMENT:The meeting really needs to include both ourselves and the customer. We can't hold " +
            "this meeting without them. As a matter of fact\\, the venue for the meeting ought to be at their site. - - John"
        assertTrue(roundTrip(Properties.COMMENT, comment).contains("fact, the venue"))
        val description = "DESCRIPTION:Meeting to provide technical review for \"Phoenix\" design.\\nHappy Face " +
            "Conference Room. Phoenix design team MUST attend this meeting.\\nRSVP to team leader."
        assertEquals(3, roundTrip(Properties.DESCRIPTION, description).lines().size)
        assertEquals("Department Party", roundTrip(Properties.SUMMARY, "SUMMARY:Department Party"))
        assertEquals(
            "19960401T080045Z-4000F192713-0052@example.com",
            roundTrip(Properties.UID, "UID:19960401T080045Z-4000F192713-0052@example.com"),
        )
        assertEquals(
            "http://example.com/pub/calendars/jsmith/mytime.ics",
            roundTrip(Properties.URL, "URL:http://example.com/pub/calendars/jsmith/mytime.ics"),
        )
    }

    @Test
    fun locationsAndContacts() {
        val plain = roundTrip(Properties.LOCATION, "LOCATION:Conference Room - F123\\, Bldg. 002")
        assertEquals("Conference Room - F123, Bldg. 002", plain.text)
        assertNull(plain.altRep)
        val alternative = roundTrip(
            Properties.LOCATION,
            "LOCATION;ALTREP=\"http://xyzcorp.com/conf-rooms/f123.vcf\":Conference Room - F123\\, Bldg. 002",
        )
        assertEquals("http://xyzcorp.com/conf-rooms/f123.vcf", alternative.altRep)
        val french = roundTrip(Properties.LOCATION, "LOCATION;LANGUAGE=fr:Salle de conférence")
        assertEquals("fr", french.language)
        val contact = roundTrip(
            Properties.CONTACT,
            "CONTACT;ALTREP=\"CID:part3.msg970930T083000SILVER@example.com\":Jim Dolittle\\, ABC Industries\\, " +
                "+1-919-555-1234",
        )
        assertEquals("CID:part3.msg970930T083000SILVER@example.com", contact.altRep)
        assertEquals("Jim Dolittle, ABC Industries, +1-919-555-1234", contact.text)
        assertEquals("Location(parameters=1)", alternative.toString())
    }

    @Test
    fun listsOfTexts() {
        assertEquals(
            listOf("APPOINTMENT", "EDUCATION"),
            roundTrip(Properties.CATEGORIES, "CATEGORIES:APPOINTMENT,EDUCATION").values,
        )
        assertEquals(listOf("MEETING"), roundTrip(Properties.CATEGORIES, "CATEGORIES:MEETING").values)
        assertEquals(
            listOf("EASEL", "PROJECTOR", "VCR"),
            roundTrip(Properties.RESOURCES, "RESOURCES:EASEL,PROJECTOR,VCR").values,
        )
        val french = roundTrip(Properties.RESOURCES, "RESOURCES;LANGUAGE=fr:Nettoyeur haute pression")
        assertEquals("fr", french.language)
        assertEquals("Categories(values=2)", value(Properties.CATEGORIES, "CATEGORIES:A\\,B,C").toString())
        assertEquals(listOf("A,B", "C"), value(Properties.CATEGORIES, "CATEGORIES:A\\,B,C").values)
    }

    @Test
    fun enumerations() {
        assertSame(Classification.Public, roundTrip(Properties.CLASS, "CLASS:PUBLIC"))
        assertEquals("X-SECRET", roundTrip(Properties.CLASS, "CLASS:X-SECRET").value)
        assertSame(EventStatus.Tentative, roundTrip(Properties.EVENT_STATUS, "STATUS:TENTATIVE"))
        assertSame(TodoStatus.NeedsAction, roundTrip(Properties.TODO_STATUS, "STATUS:NEEDS-ACTION", "VTODO"))
        assertSame(JournalStatus.Draft, roundTrip(Properties.JOURNAL_STATUS, "STATUS:DRAFT", "VJOURNAL"))
        assertSame(Transparency.Transparent, roundTrip(Properties.TRANSP, "TRANSP:TRANSPARENT"))
        assertSame(Transparency.Opaque, roundTrip(Properties.TRANSP, "TRANSP:OPAQUE"))
        assertSame(AlarmAction.Audio, roundTrip(Properties.ACTION, "ACTION:AUDIO", "VALARM"))
        assertSame(AlarmAction.Display, value(Properties.ACTION, "ACTION:display", "VALARM"))
        assertMalformed(lenient(Properties.CLASS, "CLASS:"), "ICAL-V001@2:7")
    }

    @Test
    fun numbersAndGeo() {
        assertEquals(39, roundTrip(Properties.PERCENT_COMPLETE, "PERCENT-COMPLETE:39", "VTODO"))
        assertEquals(1, roundTrip(Properties.PRIORITY, "PRIORITY:1"))
        assertEquals(4, roundTrip(Properties.REPEAT, "REPEAT:4", "VALARM"))
        assertEquals(2, roundTrip(Properties.SEQUENCE, "SEQUENCE:2"))
        val geo = roundTrip(Properties.GEO, "GEO:37.386013;-122.082932")
        assertEquals(37.386013, geo.latitude)
        assertEquals(-122.082932, geo.longitude)
        assertEquals("Geo", geo.toString())
        assertMalformed(lenient(Properties.PRIORITY, "PRIORITY:high"), "ICAL-V001@2:10")
    }

    @Test
    fun dateTimes() {
        assertEquals(utc("19960401T150000"), roundTrip(Properties.COMPLETED, "COMPLETED:19960401T150000Z", "VTODO"))
        assertEquals(utc("19960401T150000"), roundTrip(Properties.DTEND, "DTEND:19960401T150000Z"))
        assertEquals(ICalDateTime.Date(LocalDate(1998, 7, 4)), roundTrip(Properties.DTEND, "DTEND;VALUE=DATE:19980704"))
        assertEquals(utc("19980430T000000"), roundTrip(Properties.DUE, "DUE:19980430T000000Z", "VTODO"))
        assertEquals(utc("19980118T073000"), roundTrip(Properties.DTSTART, "DTSTART:19980118T073000Z"))
        val zoned = roundTrip(Properties.DTSTART, "DTSTART;TZID=America/New_York:19980119T020000")
        assertEquals(ICalDateTime.Zoned(LocalDateTime(1998, 1, 19, 2, 0, 0), "America/New_York"), zoned)
        val floating = roundTrip(Properties.DTSTART, "DTSTART:19980118T230000")
        assertEquals(ICalDateTime.Floating(LocalDateTime(1998, 1, 18, 23, 0, 0)), floating)
        assertEquals(utc("19980118T230000"), roundTrip(Properties.CREATED, "CREATED:19980118T230000Z"))
        assertEquals(utc("19971210T080000"), roundTrip(Properties.DTSTAMP, "DTSTAMP:19971210T080000Z"))
        assertEquals(utc("19960817T133000"), roundTrip(Properties.LAST_MODIFIED, "LAST-MODIFIED:19960817T133000Z"))
        val explicit = value(Properties.DTSTART, "DTSTART;VALUE=DATE-TIME:19980118T073000Z")
        assertEquals(utc("19980118T073000"), explicit)
    }

    @Test
    fun durationsRulesAndOffsets() {
        assertEquals("PT1H0M0S", roundTrip(Properties.DURATION, "DURATION:PT1H0M0S").toString())
        assertEquals(ICalDuration.ofMinutes(15), roundTrip(Properties.DURATION, "DURATION:PT15M", "VALARM"))
        val rule = roundTrip(Properties.RRULE, "RRULE:FREQ=DAILY;COUNT=10")
        assertEquals(Frequency.DAILY, rule.frequency)
        assertEquals(10, rule.count)
        assertEquals(UtcOffset(hours = -5), roundTrip(Properties.TZOFFSETFROM, "TZOFFSETFROM:-0500", "STANDARD"))
        assertEquals(
            UtcOffset(hours = 13, minutes = 45),
            roundTrip(Properties.TZOFFSETFROM, "TZOFFSETFROM:+1345", "STANDARD"),
        )
        assertEquals(UtcOffset(hours = -4), roundTrip(Properties.TZOFFSETTO, "TZOFFSETTO:-0400", "DAYLIGHT"))
        assertEquals(
            UtcOffset(hours = 2, minutes = 30, seconds = 17),
            value(Properties.TZOFFSETTO, "TZOFFSETTO:+023017", "STANDARD"),
        )
        assertMalformed(lenient(Properties.TZOFFSETTO, "TZOFFSETTO:+1900", "STANDARD"), "ICAL-V002@2:12")
    }

    @Test
    fun timeZoneTexts() {
        assertEquals("America/New_York", roundTrip(Properties.TZID, "TZID:America/New_York", "VTIMEZONE"))
        assertEquals(
            "/example.org/America/New_York",
            roundTrip(Properties.TZID, "TZID:/example.org/America/New_York", "VTIMEZONE"),
        )
        assertEquals("EST", roundTrip(Properties.TZNAME, "TZNAME:EST", "STANDARD"))
        assertEquals("HNE", value(Properties.TZNAME, "TZNAME;LANGUAGE=fr-CA:HNE", "STANDARD"))
        assertEquals(ICalProperty.of("TZNAME", "HNE"), encoded(Properties.TZNAME, "HNE"))
        assertEquals(
            "http://timezones.example.org/tz/America-Los_Angeles.ics",
            roundTrip(Properties.TZURL, "TZURL:http://timezones.example.org/tz/America-Los_Angeles.ics", "VTIMEZONE"),
        )
    }

    @Test
    fun attendeesAndOrganizers() {
        val member =
            roundTrip(
                Properties.ATTENDEE,
                "ATTENDEE;MEMBER=\"mailto:DEV-GROUP@example.com\":mailto:joecool@example.com",
            )
        assertEquals("mailto:joecool@example.com", member.address)
        assertEquals(listOf("mailto:DEV-GROUP@example.com"), member.member)
        assertSame(Role.ReqParticipant, member.role)
        assertSame(PartStat.NeedsAction, member.partStat)
        assertSame(CuType.Individual, member.cuType)
        assertEquals(false, member.rsvp)
        assertNull(member.cn)
        val delegated = roundTrip(
            Properties.ATTENDEE,
            "ATTENDEE;ROLE=REQ-PARTICIPANT;DELEGATED-FROM=\"mailto:bob@example.com\";PARTSTAT=ACCEPTED;" +
                "CN=Jane Doe:mailto:jdoe@example.com",
        )
        assertEquals(listOf("mailto:bob@example.com"), delegated.delegatedFrom)
        assertSame(PartStat.Accepted, delegated.partStat)
        assertEquals("Jane Doe", delegated.cn)
        val full = roundTrip(
            Properties.ATTENDEE,
            "ATTENDEE;CUTYPE=GROUP;ROLE=CHAIR;PARTSTAT=TENTATIVE;RSVP=TRUE;DELEGATED-TO=\"mailto:a@example.com\"," +
                "\"mailto:b@example.com\";SENT-BY=\"mailto:sray@example.com\";DIR=\"ldap://example.com:6666/" +
                "o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\";LANGUAGE=de;EMAIL=jim@example.com;X-VENDOR=1:" +
                "mailto:jimdo@example.com",
        )
        assertSame(CuType.Group, full.cuType)
        assertSame(Role.Chair, full.role)
        assertSame(PartStat.Tentative, full.partStat)
        assertEquals(true, full.rsvp)
        assertEquals(listOf("mailto:a@example.com", "mailto:b@example.com"), full.delegatedTo)
        assertEquals("mailto:sray@example.com", full.sentBy)
        assertEquals("ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)", full.dir)
        assertEquals("de", full.language)
        assertEquals("jim@example.com", full.email)
        assertEquals("X-VENDOR", full.parameters.last().name)
        assertEquals("Attendee(parameters=10)", full.toString())
        val organizer = roundTrip(Properties.ORGANIZER, "ORGANIZER;CN=John Smith:mailto:jsmith@example.com")
        assertEquals("John Smith", organizer.cn)
        val sentBy =
            roundTrip(
                Properties.ORGANIZER,
                "ORGANIZER;SENT-BY=\"mailto:jane_doe@example.com\":mailto:jsmith@example.com",
            )
        assertEquals("mailto:jane_doe@example.com", sentBy.sentBy)
        val directory = roundTrip(
            Properties.ORGANIZER,
            "ORGANIZER;CN=JohnSmith;DIR=\"ldap://example.com:6666/o=DC%20Associates,c=US???(cn=John%20Smith)\";" +
                "LANGUAGE=en;EMAIL=js@example.com:mailto:jsmith@example.com",
        )
        assertEquals("ldap://example.com:6666/o=DC%20Associates,c=US???(cn=John%20Smith)", directory.dir)
        assertEquals("en", directory.language)
        assertEquals("js@example.com", directory.email)
        assertNull(directory.sentBy)
        assertEquals("Organizer(parameters=4)", directory.toString())
    }

    @Test
    fun attachments() {
        val cid = roundTrip(Properties.ATTACH, "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com")
        assertEquals("CID:jsmith.part3.960817T083000.xyzMail@example.com", assertIs<Attach.Uri>(cid).uri)
        val typed =
            roundTrip(
                Properties.ATTACH,
                "ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/reports/r-960812.ps",
            )
        assertEquals("application/postscript", typed.formatType)
        val binary = roundTrip(
            Properties.ATTACH,
            "ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=",
        )
        val content = assertIs<Attach.Binary>(binary)
        assertEquals("The quick brown fox jumps over the lazy dog.", content.bytes().decodeToString())
        assertEquals(44, content.size)
        content.bytes()[0] = 0
        assertContentEquals("The".encodeToByteArray(), content.bytes().copyOf(3))
        assertEquals("text/plain", content.formatType)
        assertEquals("Attach.Binary(size=44)", content.toString())
        assertEquals("Attach.Uri(parameters=0)", cid.toString())
    }

    @Test
    fun relationsAndRecurrenceIds() {
        val parent = roundTrip(Properties.RELATED_TO, "RELATED-TO:jsmith.part7.19960817T083000.xyzMail@example.com")
        assertSame(RelType.Parent, parent.relType)
        assertEquals("jsmith.part7.19960817T083000.xyzMail@example.com", parent.uid)
        assertSame(RelType.Child, roundTrip(Properties.RELATED_TO, "RELATED-TO;RELTYPE=CHILD:a@example.com").relType)
        val date = roundTrip(Properties.RECURRENCE_ID, "RECURRENCE-ID;VALUE=DATE:19960401")
        assertEquals(ICalDateTime.Date(LocalDate(1996, 4, 1)), date.value)
        assertNull(date.range)
        val future = roundTrip(Properties.RECURRENCE_ID, "RECURRENCE-ID;RANGE=THISANDFUTURE:19960120T120000Z")
        assertSame(RecurrenceRange.ThisAndFuture, future.range)
        assertEquals(utc("19960120T120000"), future.value)
        assertEquals("RelatedTo(CHILD)", value(Properties.RELATED_TO, "RELATED-TO;RELTYPE=CHILD:a").toString())
    }

    @Test
    fun recurrenceDates() {
        val exdates = roundTrip(Properties.EXDATE, "EXDATE:19960402T010000Z,19960403T010000Z,19960404T010000Z")
        assertEquals(listOf(utc("19960402T010000"), utc("19960403T010000"), utc("19960404T010000")), exdates.dates)
        val exdateDates = roundTrip(Properties.EXDATE, "EXDATE;VALUE=DATE:19960402,19960403")
        assertEquals(ICalDateTime.Date(LocalDate(1996, 4, 3)), exdateDates.dates[1])
        val zoned = roundTrip(Properties.EXDATE, "EXDATE;TZID=Europe/Moscow:20261026T100000")
        assertEquals("Europe/Moscow", assertIs<ICalDateTime.Zoned>(zoned.dates.single()).tzid)
        assertEquals(listOf(utc("19970714T123000")), roundTrip(Properties.RDATE, "RDATE:19970714T123000Z").dates)
        val zonedRdate = roundTrip(Properties.RDATE, "RDATE;TZID=America/New_York:19970714T083000")
        assertEquals("America/New_York", assertIs<ICalDateTime.Zoned>(zonedRdate.dates.single()).tzid)
        val periods = roundTrip(
            Properties.RDATE,
            "RDATE;VALUE=PERIOD:19960403T020000Z/19960403T040000Z,19960404T010000Z/PT3H",
        )
        assertEquals(emptyList(), periods.dates)
        assertEquals(2, periods.periods.size)
        assertEquals("PT3H", periods.periods[1].duration.toString())
        val dates = roundTrip(
            Properties.RDATE,
            "RDATE;VALUE=DATE:19970101,19970120,19970217,19970421,19970526,19970704,19970901,19971014,19971128," +
                "19971129,19971225",
        )
        assertEquals(11, dates.dates.size)
        assertEquals("RDate(dates=11, periods=0)", dates.toString())
        assertEquals("ExDate(dates=3)", exdates.toString())
    }

    @Test
    fun freeBusy() {
        val single =
            roundTrip(Properties.FREEBUSY, "FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M", "VFREEBUSY")
        assertSame(FbType.BusyUnavailable, single.type)
        val free = roundTrip(
            Properties.FREEBUSY,
            "FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H,19970308T230000Z/19970309T000000Z",
            "VFREEBUSY",
        )
        assertSame(FbType.Free, free.type)
        assertEquals(3, free.periods.size)
        assertEquals(utc("19970309T000000"), free.periods[2].end)
        assertSame(FbType.Busy, roundTrip(Properties.FREEBUSY, "FREEBUSY:19970308T160000Z/PT1H", "VFREEBUSY").type)
        assertEquals(
            "FreeBusy(BUSY, periods=1)",
            value(Properties.FREEBUSY, "FREEBUSY:19970308T160000Z/PT1H", "VFREEBUSY").toString(),
        )
    }

    @Test
    fun triggers() {
        val before = assertIs<Trigger.Relative>(roundTrip(Properties.TRIGGER, "TRIGGER:-PT15M", "VALARM"))
        assertEquals(ICalDuration.ofMinutes(-15), before.duration)
        assertSame(Related.Start, before.related)
        val after = assertIs<Trigger.Relative>(roundTrip(Properties.TRIGGER, "TRIGGER;RELATED=END:PT5M", "VALARM"))
        assertSame(Related.End, after.related)
        val absolute = roundTrip(Properties.TRIGGER, "TRIGGER;VALUE=DATE-TIME:19980101T050000Z", "VALARM")
        assertEquals(utc("19980101T050000"), assertIs<Trigger.Absolute>(absolute).dateTime)
        assertEquals("Trigger.Relative(-PT15M, START)", before.toString())
        assertEquals("Trigger.Absolute(1998-01-01T05:00:00Z)", absolute.toString())
        assertEquals(1, absolute.parameters.size)
    }

    @Test
    fun requestStatuses() {
        val success = roundTrip(Properties.REQUEST_STATUS, "REQUEST-STATUS:2.0;Success")
        assertEquals("2.0", success.code)
        assertEquals("Success", success.description)
        assertNull(success.data)
        val invalid =
            roundTrip(Properties.REQUEST_STATUS, "REQUEST-STATUS:3.1;Invalid property value;DTSTART:96-Apr-01")
        assertEquals("DTSTART:96-Apr-01", invalid.data)
        val escaped = roundTrip(
            Properties.REQUEST_STATUS,
            "REQUEST-STATUS:2.8; Success\\, repeating event ignored. Scheduled as a single event.;RRULE:FREQ=WEEKLY\\;INTERVAL=2",
        )
        assertEquals(" Success, repeating event ignored. Scheduled as a single event.", escaped.description)
        assertEquals("RRULE:FREQ=WEEKLY;INTERVAL=2", escaped.data)
        val conflict = roundTrip(Properties.REQUEST_STATUS, "REQUEST-STATUS:4.1;Event conflict.  Date-time is busy.")
        assertEquals("4.1", conflict.code)
        val attendee =
            value(
                Properties.REQUEST_STATUS,
                "REQUEST-STATUS;LANGUAGE=en:3.7;Invalid calendar user;ATTENDEE:mailto:jsmith@example.com",
            )
        assertEquals("en", attendee.language)
        assertEquals("ATTENDEE:mailto:jsmith@example.com", attendee.data)
        assertEquals("RequestStatus(3.7)", attendee.toString())
        assertEquals("1.2.3", value(Properties.REQUEST_STATUS, "REQUEST-STATUS:1.2.3;Ok").code)
    }

    @Test
    fun decodedValuesAreCachedPerCodecAndContext() {
        val property = propertyOf("DTSTART:20260901T100000Z")
        val first = property.decodeWith(Properties.DTSTART.codec, DecodeContext.Lenient)
        assertSame(first, property.decodeWith(Properties.DTSTART.codec, DecodeContext.Lenient))
        val strict = property.decodeWith(Properties.DTSTART.codec, DecodeContext.Strict)
        assertTrue(strict !== first)
        assertSame(strict, property.decodeWith(Properties.DTSTART.codec, DecodeContext.Strict))
        assertEquals("20260901T100000Z", property.value)
        val text = property.decodeWith(Properties.SUMMARY.codec, DecodeContext.Lenient)
        assertEquals("20260901T100000Z", assertIs<Decoded.Value<String>>(text).value)
        assertEquals("20260901T100000Z", property.value)
        assertEquals(propertyOf("DTSTART:20260901T100000Z"), property)
    }
}

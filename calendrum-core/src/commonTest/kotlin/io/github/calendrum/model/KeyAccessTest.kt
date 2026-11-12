@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class KeyAccessTest {
    private val event: ICalComponent = ICal.parse(
        "BEGIN:VEVENT\r\n" +
            "UID:42@example.com\r\n" +
            "DTSTART:20261301T100000Z\r\n" +
            "dtend;tzid=Europe/Berlin:20260901T120000\r\n" +
            "ATTENDEE;CN=Jane;PARTSTAT=ACCEPTED;RSVP=TRUE;DELEGATED-FROM=\"mailto:b@example.com\":" +
            "mailto:jane@example.com\r\n" +
            "ATTENDEE:\r\n" +
            "ATTENDEE;ROLE=CHAIR:mailto:bob@example.com\r\n" +
            "X-ACME-COLOR:#ff8800\r\n" +
            "X-ACME-COLOR:blue\r\n" +
            "END:VEVENT\r\n",
    ).components.single()

    @Test
    fun getReturnsTheFirstGoodValueOrNull() {
        assertEquals("42@example.com", event[Properties.UID])
        assertNull(event[Properties.DTSTART])
        assertNull(event[Properties.SUMMARY])
        val end = assertIs<ICalDateTime.Zoned>(event[Properties.DTEND])
        assertEquals("Europe/Berlin", end.tzid)
    }

    @Test
    fun decodeExplainsTheResult() {
        assertSame(Decoded.Missing, event.decode(Properties.SUMMARY))
        val start = assertIs<Decoded.Malformed>(event.decode(Properties.DTSTART))
        assertSame(ProblemCode.OutOfRange, start.problem.code)
        assertEquals(3, start.problem.span.line)
        assertEquals(13, start.problem.span.column)
        assertEquals("Malformed(ICAL-V002)", start.toString())
        val uid = assertIs<Decoded.Value<String>>(event.decode(Properties.UID))
        assertEquals("Value(problems=0)", uid.toString())
        assertEquals("Missing", Decoded.Missing.toString())
        assertSame(event.decode(Properties.UID), event.decode(Properties.UID))
    }

    @Test
    fun allSkipsMalformedProperties() {
        val attendees = event.all(Properties.ATTENDEE)
        assertEquals(listOf("mailto:jane@example.com", "mailto:bob@example.com"), attendees.map { it.address })
        assertSame(PartStat.Accepted, attendees[0].partStat)
        assertEquals(true, attendees[0].rsvp)
        assertSame(Role.Chair, attendees[1].role)
        assertEquals(emptyList(), event.all(Properties.CATEGORIES))
        assertEquals(listOf("42@example.com"), event.all(Properties.UID))
    }

    @Test
    fun parametersByKey() {
        val attendee = event.properties("ATTENDEE").first()
        assertEquals("Jane", attendee[Parameters.CN])
        assertSame(PartStat.Accepted, attendee[Parameters.PARTSTAT])
        assertEquals(true, attendee[Parameters.RSVP])
        assertEquals(listOf("mailto:b@example.com"), attendee[Parameters.DELEGATED_FROM])
        assertNull(attendee[Parameters.ROLE])
        assertNull(attendee[Parameters.LANGUAGE])
        val end = event.property("DTEND")!!
        assertEquals("Europe/Berlin", end[Parameters.TZID])
        val typed = ICal.parse(
            "BEGIN:X\r\nP;VALUE=date;RSVP=maybe;ROLE=;FBTYPE=busy:x\r\nEND:X\r\n",
        ).components.single()
        val property = typed.properties.single()
        assertSame(ValueType.Date, property[Parameters.VALUE])
        assertNull(property[Parameters.RSVP])
        assertNull(property[Parameters.ROLE])
        assertSame(FbType.Busy, property[Parameters.FBTYPE])
        val malformed = Parameters.RSVP.codec.decode(property, property.parameter("RSVP")!!, DecodeContext.Lenient)
        assertEquals(listOf("ICAL-V001@2:19"), malformed.codes())
    }

    @Test
    fun everyParameterKeyDecodesItsValue() {
        val line = "P;ALTREP=\"http://a\";CN=Jane;CUTYPE=ROOM;DELEGATED-FROM=\"mailto:a\",\"mailto:b\";" +
            "DELEGATED-TO=\"mailto:c\";DIR=\"ldap://d\";ENCODING=BASE64;FMTTYPE=text/plain;FBTYPE=FREE;LANGUAGE=de;" +
            "MEMBER=\"mailto:g\";PARTSTAT=DECLINED;RANGE=THISANDFUTURE;RELATED=END;RELTYPE=SIBLING;" +
            "ROLE=OPT-PARTICIPANT;" +
            "RSVP=FALSE;SENT-BY=\"mailto:s\";TZID=Europe/Paris;VALUE=PERIOD;EMAIL=e@example.com:x"
        val property = ICal.parse("BEGIN:X\r\n$line\r\nEND:X\r\n").components.single().properties.single()
        assertEquals("http://a", property[Parameters.ALTREP])
        assertEquals("Jane", property[Parameters.CN])
        assertSame(CuType.Room, property[Parameters.CUTYPE])
        assertEquals(listOf("mailto:a", "mailto:b"), property[Parameters.DELEGATED_FROM])
        assertEquals(listOf("mailto:c"), property[Parameters.DELEGATED_TO])
        assertEquals("ldap://d", property[Parameters.DIR])
        assertSame(Encoding.Base64, property[Parameters.ENCODING])
        assertEquals("text/plain", property[Parameters.FMTTYPE])
        assertSame(FbType.Free, property[Parameters.FBTYPE])
        assertEquals("de", property[Parameters.LANGUAGE])
        assertEquals(listOf("mailto:g"), property[Parameters.MEMBER])
        assertSame(PartStat.Declined, property[Parameters.PARTSTAT])
        assertSame(RecurrenceRange.ThisAndFuture, property[Parameters.RANGE])
        assertSame(Related.End, property[Parameters.RELATED])
        assertSame(RelType.Sibling, property[Parameters.RELTYPE])
        assertSame(Role.OptParticipant, property[Parameters.ROLE])
        assertEquals(false, property[Parameters.RSVP])
        assertEquals("mailto:s", property[Parameters.SENT_BY])
        assertEquals("Europe/Paris", property[Parameters.TZID])
        assertSame(ValueType.Period, property[Parameters.VALUE])
        assertEquals("e@example.com", property[Parameters.EMAIL])
    }

    @Test
    fun parameterCodecsEncode() {
        assertEquals(ICalParameter.of("RSVP", "TRUE"), Parameters.RSVP.codec.encode(true, EncodeContext("RSVP")))
        assertEquals(ICalParameter.of("RSVP", "FALSE"), Parameters.RSVP.codec.encode(false, EncodeContext("RSVP")))
        assertEquals(
            ICalParameter.of("MEMBER", "mailto:a", "mailto:b"),
            Parameters.MEMBER.codec.encode(listOf("mailto:a", "mailto:b"), EncodeContext("MEMBER")),
        )
        assertEquals(
            ICalParameter.of("PARTSTAT", "ACCEPTED"),
            Parameters.PARTSTAT.codec.encode(PartStat.Accepted, EncodeContext("PARTSTAT")),
        )
        assertEquals(ICalParameter.of("CN", "Jane"), Parameters.CN.codec.encode("Jane", EncodeContext("CN")))
        assertEquals("EncodeContext(CN)", EncodeContext("CN").toString())
    }

    @Test
    fun ownKeysExtendTheModel() {
        val color = PropertyKey("x-acme-color", HexColorCodec)
        assertEquals("X-ACME-COLOR", color.name)
        assertEquals("PropertyKey(X-ACME-COLOR)", color.toString())
        assertEquals(0xFF8800, event[color])
        assertEquals(listOf(0xFF8800), event.all(color))
        val values = event.properties("X-ACME-COLOR")
        val bad = HexColorCodec.decode(values[1], DecodeContext.Lenient)
        assertEquals(listOf("ICAL-V001@9:14"), bad.codes())
        assertEquals(
            ICalProperty.of("X-ACME-COLOR", "#00ff00"),
            HexColorCodec.encode(0x00FF00, EncodeContext(color.name)),
        )
        assertEquals("ParameterKey(X-SHADE)", ParameterKey("x-shade", Parameters.CN.codec).toString())
        assertFailsWith<IllegalArgumentException> { PropertyKey("BEGIN", HexColorCodec) }
        assertFailsWith<IllegalArgumentException> { PropertyKey("X ACME", HexColorCodec) }
        assertFailsWith<IllegalArgumentException> { ParameterKey("", Parameters.CN.codec) }
    }

    @Test
    fun contextsAreSharedAndKnowTheirMode() {
        assertTrue(DecodeContext.Strict.isStrict)
        assertTrue(!DecodeContext.Lenient.isStrict)
        assertEquals("DecodeContext(strict)", DecodeContext.Strict.toString())
        assertEquals("DecodeContext(lenient)", DecodeContext.Lenient.toString())
        val property = event.property("UID")!!
        val problem = DecodeContext.Lenient.problem(property, ProblemCode.InvalidValue, 2)
        assertEquals(2, problem.span.line)
        assertEquals(7, problem.span.column)
        val first = event.properties("ATTENDEE").first()
        val parameter = DecodeContext.Lenient.problem(first, first.parameter("PARTSTAT")!!, ProblemCode.InvalidValue)
        assertEquals(5, parameter.span.line)
        assertEquals(27, parameter.span.column)
    }

    @Test
    fun keyObjectsAreReachableAndDistinct() {
        assertEquals("STATUS", Properties.EVENT_STATUS.name)
        assertEquals("STATUS", Properties.TODO_STATUS.name)
        assertEquals("STATUS", Properties.JOURNAL_STATUS.name)
        assertNotEquals<Any>(Properties.EVENT_STATUS, Properties.TODO_STATUS)
        assertEquals("LAST-MODIFIED", Properties.LAST_MODIFIED.name)
        assertEquals("SENT-BY", Parameters.SENT_BY.name)
        assertSame(Properties.DTSTART, standardKey("DTSTART", "VEVENT"))
        assertSame(Properties.TODO_STATUS, standardKey("STATUS", "VTODO"))
        assertSame(Properties.JOURNAL_STATUS, standardKey("STATUS", "VJOURNAL"))
        assertSame(Properties.EVENT_STATUS, standardKey("STATUS", "VEVENT"))
        assertNull(standardKey("X-FOO", "VEVENT"))
    }

    private object HexColorCodec : PropertyCodec<Int> {
        override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Int> {
            val text = property.value
            if (text.length != 7 || text[0] != '#') {
                return Decoded.Malformed(context.problem(property, ProblemCode.InvalidValue, 0))
            }
            return Decoded.Value(text.substring(1).toInt(16), emptyList())
        }

        override fun encode(value: Int, context: EncodeContext): ICalProperty =
            ICalProperty.of(context.name, "#" + value.toString(16).padStart(6, '0'))
    }
}

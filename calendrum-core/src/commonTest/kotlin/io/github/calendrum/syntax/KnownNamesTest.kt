package io.github.calendrum.syntax

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class KnownNamesTest {
    private fun ids(kind: Int): List<Int> = (0 until KnownNames.count).filter { KnownNames.kinds(it) and kind != 0 }

    @Test
    fun everyNameIsFoundInAnyCase() {
        for (id in 0 until KnownNames.count) {
            val name = KnownNames.name(id)
            assertEquals(id, KnownNames.lookup(name), name)
            assertEquals(id, KnownNames.lookup(name.lowercase()), name)
            val mixed = name.mapIndexed { index, char -> if (index % 2 == 0) char.lowercaseChar() else char }
            assertEquals(id, KnownNames.lookup(mixed.joinToString("")), name)
        }
    }

    @Test
    fun namesAreUniqueCanonicalAndShort() {
        val names = (0 until KnownNames.count).map { KnownNames.name(it) }
        assertEquals(names.size, names.toSet().size)
        for (name in names) {
            assertTrue(name.all { it in 'A'..'Z' || it == '-' }, name)
        }
        assertEquals(names.maxOf { it.length }, KnownNames.maxLength)
        assertEquals(19, KnownNames.maxLength)
        assertTrue(KnownNames.longestProbe <= 2, "longest probe ${KnownNames.longestProbe}")
    }

    @Test
    fun tableCoversTheStandards() {
        assertEquals(70, ids(KnownNames.PROPERTY).size)
        assertEquals(14, ids(KnownNames.COMPONENT).size)
        assertEquals(32, ids(KnownNames.PARAMETER).size)
        assertEquals(115, KnownNames.count)
        assertEquals("BEGIN", KnownNames.name(KnownNames.BEGIN))
        assertEquals("END", KnownNames.name(KnownNames.END))
        assertEquals("VCALENDAR", KnownNames.name(KnownNames.VCALENDAR))
        assertEquals(KnownNames.PROPERTY or KnownNames.PARAMETER, KnownNames.kinds(KnownNames.lookup("TZID")))
        assertEquals(KnownNames.COMPONENT, KnownNames.kinds(KnownNames.lookup("VAVAILABILITY")))
        assertEquals(KnownNames.PARAMETER, KnownNames.kinds(KnownNames.lookup("SCHEDULE-FORCE-SEND")))
        assertEquals(KnownNames.PROPERTY, KnownNames.kinds(KnownNames.lookup("X-WR-TIMEZONE")))
    }

    @Test
    fun otherNamesAreUnknown() {
        val unknown = listOf(
            "", "X", "X-FOO", "SUMMAR", "SUMMARYX", "SUMMARY-", "-SUMMARY", "DT_START", "RECURRENCEMID",
            "DTSTART ", "VEVENTS", "X-WR-TIMEZONES", "SCHEDULE-FORCE-SENDX", "ÉVÉNEMENT", "A".repeat(200),
        )
        for (name in unknown) assertEquals(KnownNames.UNKNOWN, KnownNames.lookup(name), name)
    }

    @Test
    fun caseFoldingAppliesToLettersOnly() {
        val bytes = "RECURRENCE-ID".encodeToByteArray()
        bytes[10] = (bytes[10] + 0x20).toByte()
        assertEquals(KnownNames.UNKNOWN, KnownNames.find(bytes, 0, bytes.size))
        val shifted = "dtstart".encodeToByteArray().map { (it - 0x40).toByte() }.toByteArray()
        assertEquals(KnownNames.UNKNOWN, KnownNames.find(shifted, 0, shifted.size))
    }

    @Test
    fun lookupWorksInsideLargerArrays() {
        val bytes = "xxSummaryyy".encodeToByteArray()
        assertEquals(KnownNames.lookup("SUMMARY"), KnownNames.find(bytes, 2, 9))
        assertEquals(KnownNames.UNKNOWN, KnownNames.find(bytes, 2, 10))
    }

    @Test
    fun knownNamesReturnTheSameCanonicalString() {
        val input = crlf("summary;cn=a:1", "SUMMARY;CN=b:2", "Summary;Cn=c:3")
        val lexer = ContentLexer(input.encodeToByteArray(), ParseLimits.Default) { }
        val names = mutableListOf<String>()
        val parameters = mutableListOf<String>()
        while (true) {
            val line = lexer.next() ?: break
            names += line.name()
            parameters += line.parameterName(0)
        }
        assertEquals(listOf("SUMMARY", "SUMMARY", "SUMMARY"), names)
        for (name in names) assertSame(KnownNames.name(KnownNames.lookup("SUMMARY")), name)
        for (name in parameters) assertSame(KnownNames.name(KnownNames.lookup("CN")), name)
    }

    @Test
    fun idsAndKindsAreFrozen() {
        val names = expectedNames.split(' ')
        assertEquals(names.size, KnownNames.count)
        assertEquals(expectedKinds.length, KnownNames.count)
        for (id in names.indices) {
            assertEquals(names[id], KnownNames.name(id))
            assertEquals(expectedKinds[id] - '0', KnownNames.kinds(id), names[id])
            assertEquals(id, KnownNames.lookup(names[id]), names[id])
        }
    }

    private val expectedNames: String =
        "BEGIN END CALSCALE METHOD PRODID VERSION ATTACH CATEGORIES CLASS COMMENT DESCRIPTION GEO LOCATION " +
            "PERCENT-COMPLETE PRIORITY RESOURCES STATUS SUMMARY COMPLETED DTEND DUE DTSTART DURATION FREEBUSY " +
            "TRANSP TZID TZNAME TZOFFSETFROM TZOFFSETTO TZURL ATTENDEE CONTACT ORGANIZER RECURRENCE-ID RELATED-TO " +
            "URL UID EXDATE RDATE RRULE ACTION REPEAT TRIGGER CREATED DTSTAMP LAST-MODIFIED SEQUENCE " +
            "REQUEST-STATUS EXRULE NAME REFRESH-INTERVAL SOURCE COLOR IMAGE CONFERENCE BUSYTYPE LOCATION-TYPE " +
            "PARTICIPANT-TYPE RESOURCE-TYPE CALENDAR-ADDRESS STYLED-DESCRIPTION STRUCTURED-DATA ACKNOWLEDGED " +
            "PROXIMITY CONCEPT LINK REFID X-WR-CALNAME X-WR-CALDESC X-WR-TIMEZONE VCALENDAR VEVENT VTODO VJOURNAL " +
            "VFREEBUSY VTIMEZONE STANDARD DAYLIGHT VALARM VAVAILABILITY AVAILABLE PARTICIPANT VLOCATION VRESOURCE " +
            "ALTREP CN CUTYPE DELEGATED-FROM DELEGATED-TO DIR ENCODING FMTTYPE FBTYPE LANGUAGE MEMBER PARTSTAT " +
            "RANGE RELATED RELTYPE ROLE RSVP SENT-BY VALUE DISPLAY EMAIL FEATURE LABEL SCHEDULE-AGENT " +
            "SCHEDULE-FORCE-SEND SCHEDULE-STATUS ORDER SCHEMA DERIVED LINKREL GAP"

    private val expectedKinds: String =
        "222222222222222222222222262222222222222222222222222222222222" +
            "2222222222111111111111114444444444444444444444444444444"
}

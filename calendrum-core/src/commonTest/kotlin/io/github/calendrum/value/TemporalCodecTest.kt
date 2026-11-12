package io.github.calendrum.value

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TemporalCodecTest {
    private fun date(value: String) = lenient { decodeDate(value, 0, value.length) }

    private fun strictDate(value: String) = strict { decodeDate(value, 0, value.length) }

    private fun dt(value: String, tzid: String? = null) = lenient { decodeDateTime(value, 0, value.length, tzid) }

    private fun strictDt(value: String, tzid: String? = null) = strict { decodeDateTime(value, 0, value.length, tzid) }

    private fun time(value: String) = lenient { decodeTime(value, 0, value.length) }

    private fun strictTime(value: String) = strict { decodeTime(value, 0, value.length) }

    private fun offset(value: String) = lenient { decodeUtcOffset(value, 0, value.length) }

    private fun strictOffset(value: String) = strict { decodeUtcOffset(value, 0, value.length) }

    @Test
    fun rfcDateExamples() {
        assertAccepted("19970714", strictDate("19970714"), ::encodeDateTime)
        val list = strict { decodeDateList("19970101,19970120,19970217", 0, 26) }
        assertAccepted("19970101,19970120,19970217", list, ::encodeDateTimeList)
    }

    @Test
    fun rfcDateTimeExamples() {
        val floating = strictDt("19980118T230000")
        assertAccepted("19980118T230000", floating, ::encodeDateTime)
        assertEquals(FORM_FLOATING, floating.result.form)
        val utc = strictDt("19980119T070000Z")
        assertAccepted("19980119T070000Z", utc, ::encodeDateTime)
        assertTrue(utc.result.isUtc)
        val zoned = strictDt("19980119T020000", "America/New_York")
        assertAccepted("19980119T020000", zoned, ::encodeDateTime)
        assertEquals(FORM_ZONED, zoned.result.form)
        assertEquals("America/New_York", zoned.result.tzid)
        assertAccepted("19970714T173000Z", strictDt("19970714T173000Z"), ::encodeDateTime)
        assertAccepted("19970901T130000Z", strictDt("19970901T130000Z"), ::encodeDateTime)
        assertAccepted("19980118T073000Z", strictDt("19980118T073000Z"), ::encodeDateTime)
    }

    @Test
    fun rfcTimeAndOffsetExamples() {
        assertAccepted("230000", strictTime("230000"), ::encodeTime)
        assertAccepted("070000Z", strictTime("070000Z"), ::encodeTime)
        assertAccepted("083000", strictTime("083000"), ::encodeTime)
        assertAccepted("-0500", strictOffset("-0500"), ::encodeUtcOffset)
        assertAccepted("+0100", strictOffset("+0100"), ::encodeUtcOffset)
        assertAccepted("+1345", strictOffset("+1345"), ::encodeUtcOffset)
        assertAccepted("-0400", strictOffset("-0400"), ::encodeUtcOffset)
        assertEquals(-18000, strictOffset("-0500").value)
    }

    @Test
    fun offsetsWithSecondsKeepTheirSeconds() {
        assertAccepted("+023017", strictOffset("+023017"), ::encodeUtcOffset)
        assertEquals(9017, strictOffset("+023017").value)
        assertAccepted("+0230", strictOffset("+023000"), ::encodeUtcOffset)
    }

    @Test
    fun lowercaseDesignatorsAreValidAbnf() {
        assertAccepted("20260901T100000Z", strictDt("20260901t100000z"), ::encodeDateTime)
        assertAccepted("100000Z", strictTime("100000z"), ::encodeTime)
    }

    @Test
    fun extendedIsoForm() {
        assertAccepted("20260901", date("2026-09-01"), ::encodeDateTime, "ICAL-V005@4")
        assertRejected(strictDate("2026-09-01"), "ICAL-V005@4", "$V001@4")
        assertAccepted("20260901T100000Z", dt("2026-09-01T10:00:00Z"), ::encodeDateTime, "ICAL-V005@4")
        assertRejected(strictDt("2026-09-01T10:00:00Z"), "ICAL-V005@4", "$V001@4")
        assertAccepted("20260901T100000", dt("20260901T10:00:00"), ::encodeDateTime, "ICAL-V005@11")
        assertAccepted("100000", time("10:00:00"), ::encodeTime, "ICAL-V005@2")
        assertRejected(strictTime("10:00:00"), "ICAL-V005@2", "$V001@2")
        assertRejected(dt("2026-09-01T100000Z"), "ICAL-V005@4", "$V001@13")
        assertRejected(dt("2026-0901"), "ICAL-V005@4", "$V001@7")
        assertRejected(dt("20260901T10:0000"), "ICAL-V005@11", "$V001@14")
    }

    @Test
    fun fractionalSeconds() {
        assertAccepted("20260901T100000Z", dt("20260901T100000.000Z"), ::encodeDateTime, "ICAL-V006@15")
        assertRejected(strictDt("20260901T100000.000Z"), "ICAL-V006@15", "$V001@15")
        assertAccepted(
            "20260901T100000Z",
            dt("2026-09-01T10:00:00.123Z"),
            ::encodeDateTime,
            "ICAL-V005@4",
            "ICAL-V006@19",
        )
        assertAccepted("100000", time("100000,5"), ::encodeTime, "ICAL-V006@6")
        assertRejected(dt("20260901T100000.Z"), "$V001@16")
    }

    @Test
    fun dateWithTime() {
        assertAccepted("20260901", date("20260901T100000"), ::encodeDateTime, "ICAL-V007@8")
        assertRejected(strictDate("20260901T100000"), "ICAL-V007@8", "$V001@8")
        assertRejected(date("20260901T1000"), "ICAL-V007@8", "$V001@13")
    }

    @Test
    fun dateAsDateTime() {
        val decoded = dt("20260901")
        assertAccepted("20260901", decoded, ::encodeDateTime, "ICAL-V008@8")
        assertTrue(decoded.result.isDate)
        assertRejected(strictDt("20260901"), "ICAL-V008@8", "$V001@8")
    }

    @Test
    fun tzidWithUtc() {
        val decoded = dt("20260901T100000Z", "Europe/Moscow")
        assertAccepted("20260901T100000Z", decoded, ::encodeDateTime, "ICAL-V024@15")
        assertTrue(decoded.result.isUtc)
        assertEquals(null, decoded.result.tzid)
        assertRejected(strictDt("20260901T100000Z", "Europe/Moscow"), "ICAL-V024@15", "$V001@15")
    }

    @Test
    fun whitespace() {
        assertAccepted("20260901T100000Z", dt(" 20260901T100000Z "), ::encodeDateTime, "ICAL-V013@0")
        assertAccepted("20260901", date("20260901\t"), ::encodeDateTime, "ICAL-V013@8")
        assertRejected(strictDt(" 20260901T100000Z"), "ICAL-V013@0", "$V001@0")
        assertRejected(dt("   "), "ICAL-V013@0", "$V001@3")
    }

    @Test
    fun leapSecond() {
        assertAccepted("20161231T235959Z", dt("20161231T235960Z"), ::encodeDateTime, "ICAL-V004@13")
        assertAccepted("20161231T235959Z", strictDt("20161231T235960Z"), ::encodeDateTime, "ICAL-V004@13")
        val next = run(false, true) { decodeDateTime("20161231T235960Z", 0, 16, null) }
        assertAccepted("20170101T000000Z", next, ::encodeDateTime, "ICAL-V004@13")
        val inMonth = run(false, true) { decodeDateTime("20160630T235960", 0, 15, null) }
        assertAccepted("20160701T000000", inMonth, ::encodeDateTime, "ICAL-V004@13")
        val minute = run(false, true) { decodeDateTime("20160630T125960", 0, 15, null) }
        assertAccepted("20160630T130000", minute, ::encodeDateTime, "ICAL-V004@13")
        val plain = run(false, true) { decodeDateTime("20160630T120060", 0, 15, null) }
        assertAccepted("20160630T120100", plain, ::encodeDateTime, "ICAL-V004@13")
        val lastYear = run(false, true) { decodeDateTime("99991231T235960Z", 0, 16, null) }
        assertAccepted("99991231T235959Z", lastYear, ::encodeDateTime, "ICAL-V004@13")
        assertAccepted("235959Z", time("235960Z"), ::encodeTime, "ICAL-V004@4")
        assertAccepted("235959", run(false, true) { decodeTime("235960", 0, 6) }, ::encodeTime, "ICAL-V004@4")
        assertAccepted("130000", run(false, true) { decodeTime("125960", 0, 6) }, ::encodeTime, "ICAL-V004@4")
        assertAccepted("120100", run(false, true) { decodeTime("120060", 0, 6) }, ::encodeTime, "ICAL-V004@4")
        assertRejected(dt("20161231T235961Z"), "$V002@13")
    }

    @Test
    fun boundaryDates() {
        assertAccepted("16010101T000000", strictDt("16010101T000000"), ::encodeDateTime)
        assertEquals(-134_774L, dateTime("16010101T000000").epochDay())
        assertEquals(-11_644_473_600L, dateTime("16010101T000000Z").epochSecond())
        assertAccepted("99991231T235959Z", strictDt("99991231T235959Z"), ::encodeDateTime)
        assertEquals(2_932_896L, dateTime("99991231").epochDay())
        assertEquals(253_402_300_799L, dateTime("99991231T235959Z").epochSecond())
        assertEquals(0L, dateTime("19700101T000000Z").epochSecond())
        assertEquals(-1L, dateTime("19691231T235959Z").epochSecond())
        assertEquals(2_147_483_648L, dateTime("20380119T031408Z").epochSecond())
        assertEquals(-719_528L, dateTime("00000101").epochDay())
        assertAccepted("00000101", strictDate("00000101"), ::encodeDateTime)
        assertAccepted("20240229", strictDate("20240229"), ::encodeDateTime)
        assertAccepted("20000229", strictDate("20000229"), ::encodeDateTime)
        assertRejected(strictDate("20230229"), "$V002@6")
        assertRejected(strictDate("19000229"), "$V002@6")
        assertAccepted("20260131", strictDate("20260131"), ::encodeDateTime)
        assertRejected(strictDate("20260431"), "$V002@6")
        assertRejected(strictDate("20261301"), "$V002@4")
        assertRejected(strictDate("20260001"), "$V002@4")
        assertRejected(strictDate("20260100"), "$V002@6")
    }

    @Test
    fun malformedDates() {
        assertRejected(strictDate(""), "$V001@0")
        assertRejected(strictDate("2026"), "$V001@4")
        assertRejected(strictDate("202609"), "$V001@6")
        assertRejected(strictDate("2026090"), "$V001@7")
        assertRejected(strictDate("2026O901"), "$V001@4")
        assertRejected(strictDt("20260901X100000"), "$V001@8")
        assertRejected(strictDt("20260901T1000"), "$V001@13")
        assertRejected(strictDt("20260901T10000"), "$V001@14")
        assertRejected(strictDt("20260901T1a0000"), "$V001@10")
        assertRejected(strictDt("20260901T100000ZZ"), "$V001@16")
        assertRejected(strictDt("20260901T240000"), "$V002@9")
        assertRejected(strictDt("20260901T106000"), "$V002@11")
        assertRejected(strictDt("20260901T100000+0300"), "$V001@15")
    }

    @Test
    fun malformedTimes() {
        assertRejected(strictTime(""), "$V001@0")
        assertRejected(strictTime("10"), "$V001@2")
        assertRejected(strictTime("1000"), "$V001@4")
        assertRejected(strictTime("240000"), "$V002@0")
        assertRejected(time("10:00"), "ICAL-V005@2", "$V001@5")
        assertRejected(time("10:0000"), "ICAL-V005@2", "$V001@5")
    }

    @Test
    fun utcOffsetHeuristics() {
        assertAccepted("+0300", offset("+03:00"), ::encodeUtcOffset, "ICAL-V011@3")
        assertRejected(strictOffset("+03:00"), "ICAL-V011@3", "$V001@3")
        assertAccepted("+023017", offset("+02:30:17"), ::encodeUtcOffset, "ICAL-V011@3")
        assertAccepted("+0000", offset("-0000"), ::encodeUtcOffset, "ICAL-V012@0")
        assertAccepted("+0000", offset("-000000"), ::encodeUtcOffset, "ICAL-V012@0")
        assertRejected(strictOffset("-0000"), "ICAL-V012@0", "$V001@0")
        assertAccepted("+0000", strictOffset("+0000"), ::encodeUtcOffset)
        assertAccepted("-0100", offset(" -0100"), ::encodeUtcOffset, "ICAL-V013@0")
    }

    @Test
    fun malformedOffsets() {
        assertRejected(strictOffset(""), "$V001@0")
        assertRejected(strictOffset("0300"), "$V001@0")
        assertRejected(strictOffset("+3"), "$V001@2")
        assertRejected(strictOffset("+03"), "$V001@3")
        assertRejected(strictOffset("+030"), "$V001@4")
        assertRejected(strictOffset("+03000"), "$V001@6")
        assertRejected(strictOffset("+0300000"), "$V001@7")
        assertRejected(strictOffset("+2400"), "$V002@1")
        assertRejected(strictOffset("+0060"), "$V002@3")
        assertRejected(strictOffset("+000060"), "$V002@5")
        assertRejected(offset("+03:0000"), "ICAL-V011@3", "$V001@6")
    }

    @Test
    fun dateTimeListsSkipBadItemsOnlyWhenLenient() {
        val value = "20260901T100000Z,,2026-09-02T10:00:00Z,bad, 20260903T100000Z"
        val decoded = lenient { decodeDateTimeList(value, 0, value.length, null) }
        assertAccepted(
            "20260901T100000Z,20260902T100000Z,20260903T100000Z",
            decoded,
            ::encodeDateTimeList,
            "ICAL-V016@17",
            "ICAL-V005@22",
            "$V001@39",
            "ICAL-V013@43",
        )
        assertRejected(strict { decodeDateTimeList(value, 0, value.length, null) }, "ICAL-V016@17", "$V001@17")
        assertRejected(strict { decodeDateTimeList("20260901T100000Z,bad", 0, 20, null) }, "$V001@17")
    }

    @Test
    fun zonedListsAndRanges() {
        val value = "xx19980119T020000,19980120T020000yy"
        val decoded = strict { decodeDateTimeList(value, 2, value.length - 2, "America/New_York") }
        assertAccepted("19980119T020000,19980120T020000", decoded, ::encodeDateTimeList)
        assertTrue(decoded.result.all { it.tzid == "America/New_York" })
        assertRejected(strict { decodeDateTimeList(value, 2, value.length, null) }, "$V001@31")
    }

    @Test
    fun valueObjects() {
        val first = dateTime("20260901T100000", "A")
        assertEquals(first, dateTime("20260901T100000", "A"))
        assertEquals(first.hashCode(), dateTime("20260901T100000", "A").hashCode())
        assertTrue(first != dateTime("20260901T100000", "B"))
        assertTrue(first != dateTime("20260901T100000"))
        assertTrue(first.toString().indexOf("2026") < 0)
        val time = assertAcceptedTime("100000Z")
        assertEquals(time, assertAcceptedTime("100000Z"))
        assertEquals(time.hashCode(), assertAcceptedTime("100000Z").hashCode())
        assertTrue(time != assertAcceptedTime("100000"))
        assertTrue(time.toString().indexOf("10") < 0)
    }

    private fun assertAcceptedTime(value: String): TimeValue = strictTime(value).result
}

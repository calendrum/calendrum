package io.github.calendrum.value

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RecurCodecTest {
    private fun recur(value: String) = lenient { decodeRecur(value, 0, value.length) }

    private fun strictRecur(value: String) = strict { decodeRecur(value, 0, value.length) }

    @Test
    fun everyRfcExampleDecodesStrictlyAndRoundTrips() {
        for (rule in RFC_RULES) {
            val decoded = strictRecur(rule)
            assertEquals(emptyList(), decoded.problems, rule)
            val encoded = encodeRecur(decoded.result)
            assertEquals(decoded.result, strictRecur(encoded).result, rule)
            assertEquals(encoded, encodeRecur(strictRecur(encoded).result), rule)
            assertEquals(decoded.result.hashCode(), strictRecur(encoded).result.hashCode(), rule)
        }
    }

    @Test
    fun encoderWritesFreqFirstAndThenTheFixedOrder() {
        val rule = "X-NAME=a=b;WKST=SU;BYSETPOS=-1;BYMONTH=6;BYWEEKNO=20;BYYEARDAY=-1;BYMONTHDAY=1;BYDAY=-1SU,MO;" +
            "BYHOUR=9;BYMINUTE=30;BYSECOND=0;INTERVAL=1;UNTIL=20261231;FREQ=YEARLY;X-OTHER=;skip=omit;rscale=gregorian"
        assertEquals(
            "FREQ=YEARLY;UNTIL=20261231;INTERVAL=1;BYSECOND=0;BYMINUTE=30;BYHOUR=9;BYDAY=-1SU,MO;BYMONTHDAY=1;" +
                "BYYEARDAY=-1;BYWEEKNO=20;BYMONTH=6;BYSETPOS=-1;WKST=SU;RSCALE=GREGORIAN;SKIP=OMIT;X-NAME=a=b;X-OTHER=",
            encodeRecur(recur(rule).result),
        )
        assertEquals(listOf("ICAL-V020@0", "ICAL-V020@163", "ICAL-V019@79"), recur(rule).problems)
    }

    @Test
    fun partsAreDecoded() {
        val rule = strictRecur("freq=monthly;count=10;interval=2;byday=1su,-1SU,+2mo;bymonth=1,2;wkst=mo").result
        assertEquals(FREQ_MONTHLY, rule.freq)
        assertEquals(10, rule.count)
        assertEquals(2, rule.interval)
        assertContentEquals(intArrayOf(weekdayEntry(1, 6), weekdayEntry(-1, 6), weekdayEntry(2, 0)), rule.byDay)
        assertEquals(-1, ordinalOf(rule.byDay!![1]))
        assertEquals(6, weekdayOf(rule.byDay[1]))
        assertContentEquals(intArrayOf(monthEntry(1, false), monthEntry(2, false)), rule.byMonth)
        assertEquals(0, rule.weekStart)
        assertNull(rule.until)
        assertEquals(ABSENT, rule.skip)
        assertEquals("RecurValue", rule.toString())
        val leap = strictRecur("RSCALE=HEBREW;FREQ=YEARLY;BYMONTH=5L,13;BYMONTHDAY=8;SKIP=FORWARD").result
        assertEquals(5, monthOf(leap.byMonth!![0]))
        assertTrue(isLeapMonth(leap.byMonth[0]))
        assertEquals(13, monthOf(leap.byMonth[1]))
        assertEquals(SKIP_FORWARD, leap.skip)
        assertEquals("HEBREW", leap.rscale)
        val until = strictRecur("FREQ=DAILY;UNTIL=99991231T235959Z").result.until!!
        assertEquals(9999, until.year)
        assertTrue(until.isUtc)
        assertTrue(strictRecur("FREQ=DAILY;UNTIL=16010101").result.until!!.isDate)
    }

    @Test
    fun countAndUntilTogether() {
        val rule = "FREQ=DAILY;COUNT=3;UNTIL=20261231T000000Z"
        assertAccepted("FREQ=DAILY;UNTIL=20261231T000000Z;COUNT=3", recur(rule), ::encodeRecur, "ICAL-V017@19")
        assertRejected(strictRecur(rule), "ICAL-V017@19", "$V001@19")
    }

    @Test
    fun repeatedParts() {
        val rule = "FREQ=DAILY;COUNT=3;COUNT=5"
        assertAccepted("FREQ=DAILY;COUNT=5", recur(rule), ::encodeRecur, "ICAL-V018@19")
        assertRejected(strictRecur(rule), "ICAL-V018@19", "$V001@19")
        val unknown = "FREQ=DAILY;X-A=1;X-A=2"
        assertAccepted(unknown, recur(unknown), ::encodeRecur, "ICAL-V020@11", "ICAL-V020@17")
    }

    @Test
    fun partsNotAllowedForTheFrequency() {
        val cases = mapOf(
            "FREQ=MONTHLY;BYWEEKNO=1" to 13,
            "FREQ=WEEKLY;BYYEARDAY=1" to 12,
            "FREQ=MONTHLY;BYYEARDAY=1" to 13,
            "FREQ=DAILY;BYYEARDAY=1" to 11,
            "FREQ=WEEKLY;BYMONTHDAY=1" to 12,
            "FREQ=WEEKLY;BYDAY=1MO" to 12,
            "FREQ=YEARLY;BYDAY=1MO;BYWEEKNO=1" to 12,
            "FREQ=MONTHLY;BYSETPOS=1" to 13,
            "FREQ=MONTHLY;SKIP=OMIT" to 13,
            "FREQ=YEARLY;BYMONTH=5L" to 12,
        )
        for ((rule, offset) in cases) {
            assertAccepted(rule, recur(rule), ::encodeRecur, "ICAL-V019@$offset")
            assertRejected(strictRecur(rule), "ICAL-V019@$offset", "$V001@$offset")
        }
        for (rule in listOf("FREQ=YEARLY;BYDAY=20MO", "FREQ=MONTHLY;BYDAY=MO;BYSETPOS=1", "FREQ=WEEKLY;BYDAY=MO")) {
            assertEquals(emptyList(), strictRecur(rule).problems, rule)
        }
    }

    @Test
    fun emptyPartsAndItems() {
        assertAccepted(
            "FREQ=DAILY;COUNT=2",
            recur("FREQ=DAILY;;COUNT=2;"),
            ::encodeRecur,
            "ICAL-V016@11",
            "ICAL-V016@20",
        )
        assertRejected(strictRecur("FREQ=DAILY;"), "ICAL-V016@11", "$V001@11")
        assertAccepted(
            "FREQ=WEEKLY;BYDAY=MO,TU",
            recur("FREQ=WEEKLY;BYDAY=MO,,TU,"),
            ::encodeRecur,
            "ICAL-V016@21",
            "ICAL-V016@25",
        )
        assertAccepted("FREQ=DAILY;BYHOUR=9", recur("FREQ=DAILY;BYHOUR=,9"), ::encodeRecur, "ICAL-V016@18")
        assertRejected(strictRecur("FREQ=WEEKLY;BYDAY=MO,"), "ICAL-V016@21", "$V001@21")
        assertRejected(strictRecur("FREQ=DAILY;BYHOUR=9,"), "ICAL-V016@20", "$V001@20")
        assertRejected(recur("FREQ=WEEKLY;BYDAY=,"), "ICAL-V016@18", "ICAL-V016@19", "$V001@18")
        assertRejected(recur("FREQ=DAILY;BYHOUR=,"), "ICAL-V016@18", "ICAL-V016@19", "$V001@18")
        assertRejected(recur(""), "ICAL-V003@0")
        assertRejected(recur(";"), "ICAL-V016@0", "ICAL-V016@1", "ICAL-V003@0")
    }

    @Test
    fun blanksAroundAnUnknownPartValue() {
        val rule = "FREQ=DAILY;X-A= b ;COUNT=1"
        assertAccepted("FREQ=DAILY;COUNT=1;X-A=b", recur(rule), ::encodeRecur, "ICAL-V020@11", "ICAL-V013@15")
        assertRejected(strictRecur(rule), "ICAL-V020@11", "ICAL-V013@15", "$V001@15")
    }

    @Test
    fun whitespaceAroundTheRule() {
        assertAccepted("FREQ=DAILY", recur(" FREQ=DAILY "), ::encodeRecur, "ICAL-V013@0")
        assertRejected(strictRecur("FREQ=DAILY "), "ICAL-V013@10", "$V001@10")
    }

    @Test
    fun malformedRules() {
        val invalid = mapOf(
            "COUNT=1" to null,
            "FREQ" to 4,
            "FREQ=" to 5,
            "FREQ=FORTNIGHTLY" to 5,
            "FREQ=DAILY;COUNT=x" to 17,
            "FREQ=DAILY;COUNT=" to 17,
            "FREQ=DAILY;COUNT=-1" to 17,
            "FREQ=DAILY;INTERVAL=1.5" to 21,
            "FREQ=DAILY;UNTIL=2026" to 21,
            "FREQ=DAILY;UNTIL=20260901T100000+0300" to 32,
            "FREQ=DAILY;BYHOUR=9a" to 19,
            "FREQ=DAILY;BYHOUR=-9" to 18,
            "FREQ=DAILY;BYHOUR=" to 18,
            "FREQ=MONTHLY;BYMONTHDAY=+" to 25,
            "FREQ=WEEKLY;BYDAY=XX" to 18,
            "FREQ=WEEKLY;BYDAY=+MO" to 19,
            "FREQ=WEEKLY;BYDAY=1M" to 19,
            "FREQ=YEARLY;BYMONTH=5X" to 21,
            "FREQ=YEARLY;BYMONTH=5LL" to 21,
            "FREQ=WEEKLY;WKST=XX" to 17,
            "FREQ=YEARLY;RSCALE=" to 19,
            "FREQ=YEARLY;RSCALE=a b" to 19,
            "FREQ=YEARLY;RSCALE=G;SKIP=NEVER" to 26,
            "FREQ=DAILY;X_A=1" to 11,
            "FREQ=DAILY;=1" to 11,
        )
        for ((rule, offset) in invalid) {
            val expected = if (offset == null) arrayOf("ICAL-V003@0") else arrayOf("$V001@$offset")
            assertRejected(recur(rule), *expected)
        }
        val outOfRange = mapOf(
            "FREQ=DAILY;INTERVAL=0" to 20,
            "FREQ=DAILY;COUNT=2147483648" to 17,
            "FREQ=DAILY;UNTIL=20260230" to 23,
            "FREQ=MINUTELY;BYSECOND=61" to 23,
            "FREQ=HOURLY;BYMINUTE=60" to 21,
            "FREQ=DAILY;BYHOUR=24" to 18,
            "FREQ=MONTHLY;BYMONTHDAY=0" to 24,
            "FREQ=MONTHLY;BYMONTHDAY=-32" to 24,
            "FREQ=YEARLY;BYYEARDAY=367" to 22,
            "FREQ=YEARLY;BYWEEKNO=-54" to 21,
            "FREQ=YEARLY;BYMONTH=0" to 20,
            "FREQ=YEARLY;BYMONTH=14" to 20,
            "FREQ=YEARLY;BYMONTH=13" to 12,
            "FREQ=MONTHLY;BYSETPOS=367" to 22,
            "FREQ=MONTHLY;BYDAY=54MO" to 19,
            "FREQ=MONTHLY;BYDAY=0MO" to 19,
        )
        for ((rule, offset) in outOfRange) {
            assertRejected(recur(rule), "$V002@$offset")
        }
    }

    @Test
    fun leapSecondInUntil() {
        val rule = "FREQ=DAILY;UNTIL=20161231T235960Z"
        assertAccepted("FREQ=DAILY;UNTIL=20161231T235959Z", strictRecur(rule), ::encodeRecur, "ICAL-V004@30")
        assertAccepted("FREQ=DAILY;UNTIL=20260901", recur("FREQ=DAILY;UNTIL=2026-09-01"), ::encodeRecur, "ICAL-V005@21")
    }

    @Test
    fun valueRangeInsideALongerText() {
        val text = "RRULE:FREQ=DAILY;COUNT=x"
        assertRejected(strict { decodeRecur(text, 6, text.length) }, "$V001@17")
    }

    internal companion object {
        val RFC_RULES = listOf(
            "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU;BYHOUR=8,9;BYMINUTE=30",
            "FREQ=DAILY;COUNT=10",
            "FREQ=DAILY;UNTIL=19971224T000000Z",
            "FREQ=DAILY;INTERVAL=2",
            "FREQ=DAILY;INTERVAL=10;COUNT=5",
            "FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA",
            "FREQ=DAILY;UNTIL=20000131T140000Z;BYMONTH=1",
            "FREQ=WEEKLY;COUNT=10",
            "FREQ=WEEKLY;UNTIL=19971224T000000Z",
            "FREQ=WEEKLY;INTERVAL=2;WKST=SU",
            "FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH",
            "FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH",
            "FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR",
            "FREQ=WEEKLY;INTERVAL=2;COUNT=8;WKST=SU;BYDAY=TU,TH",
            "FREQ=MONTHLY;COUNT=10;BYDAY=1FR",
            "FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR",
            "FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU",
            "FREQ=MONTHLY;COUNT=6;BYDAY=-2MO",
            "FREQ=MONTHLY;BYMONTHDAY=-3",
            "FREQ=MONTHLY;COUNT=10;BYMONTHDAY=2,15",
            "FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1",
            "FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,15",
            "FREQ=MONTHLY;INTERVAL=2;BYDAY=TU",
            "FREQ=YEARLY;COUNT=10;BYMONTH=6,7",
            "FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3",
            "FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200",
            "FREQ=YEARLY;BYDAY=20MO",
            "FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO",
            "FREQ=YEARLY;BYMONTH=3;BYDAY=TH",
            "FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8",
            "FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13",
            "FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13",
            "FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,5,6,7,8",
            "FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3",
            "FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-2",
            "FREQ=HOURLY;INTERVAL=3;UNTIL=19970902T170000Z",
            "FREQ=MINUTELY;INTERVAL=15;COUNT=6",
            "FREQ=MINUTELY;INTERVAL=90;COUNT=4",
            "FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40",
            "FREQ=MINUTELY;INTERVAL=20;BYHOUR=9,10,11,12,13,14,15,16",
            "FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=MO",
            "FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=SU",
            "FREQ=MONTHLY;BYMONTHDAY=15,30;COUNT=5",
            "FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=19980404T070000Z",
            "FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=19730429T070000Z",
            "FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10",
            "FREQ=YEARLY;BYDAY=2SU;BYMONTH=3",
            "FREQ=SECONDLY;BYSECOND=60",
            "FREQ=YEARLY;BYMONTH=1;BYMONTHDAY=1;UNTIL=19700101",
            "RSCALE=CHINESE;FREQ=YEARLY",
            "RSCALE=GREGORIAN;FREQ=YEARLY;SKIP=FORWARD",
            "RSCALE=CHINESE;FREQ=MONTHLY;COUNT=4",
            "RSCALE=HEBREW;FREQ=YEARLY;BYMONTH=5L;BYMONTHDAY=8;SKIP=FORWARD",
            "RSCALE=GREGORIAN;FREQ=MONTHLY;COUNT=4;SKIP=BACKWARD",
            "RSCALE=GREGORIAN;FREQ=YEARLY;COUNT=5;SKIP=OMIT;BYMONTH=2;BYMONTHDAY=29",
        )
    }
}

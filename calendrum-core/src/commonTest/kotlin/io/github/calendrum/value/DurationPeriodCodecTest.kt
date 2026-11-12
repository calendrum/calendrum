package io.github.calendrum.value

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DurationPeriodCodecTest {
    private fun duration(value: String) = lenient { decodeDuration(value, 0, value.length) }

    private fun strictDuration(value: String) = strict { decodeDuration(value, 0, value.length) }

    private fun period(value: String, tzid: String? = null) = lenient { decodePeriod(value, 0, value.length, tzid) }

    private fun strictPeriod(value: String, tzid: String? = null) =
        strict { decodePeriod(value, 0, value.length, tzid) }

    private fun periods(value: String) = lenient { decodePeriodList(value, 0, value.length, null) }

    private fun strictPeriods(value: String) = strict { decodePeriodList(value, 0, value.length, null) }

    @Test
    fun rfcDurationExamples() {
        for (value in listOf("P15DT5H0M20S", "P7W", "PT1H0M0S", "PT15M", "-PT15M", "-PT30M", "PT5M", "PT1H", "-P2D")) {
            assertAccepted(value, strictDuration(value), ::encodeDuration)
        }
        assertAccepted("PT10M", strictDuration("+PT10M"), ::encodeDuration)
        assertAccepted("P1DT2H3M4S", strictDuration("p1dt2h3m4s"), ::encodeDuration)
    }

    @Test
    fun sourceFormIsKept() {
        val week = strictDuration("P1W").result
        val days = strictDuration("P7D").result
        assertTrue(week != days)
        assertEquals(week.nominalDays(), days.nominalDays())
        val minutes = strictDuration("PT60M").result
        val hour = strictDuration("PT1H").result
        assertTrue(minutes != hour)
        assertEquals(minutes.exactSeconds(), hour.exactSeconds())
        assertEquals("PT60M", encodeDuration(minutes))
        assertEquals(-900L, strictDuration("-PT15M").result.exactSeconds())
        assertEquals(-14L, strictDuration("-P2W").result.nominalDays())
        assertEquals(0L, strictDuration("-P2W").result.exactSeconds())
        val nominalAndExact = strictDuration("P1DT1H").result
        assertEquals(1L, nominalAndExact.nominalDays())
        assertEquals(3600L, nominalAndExact.exactSeconds())
        assertTrue(strictDuration("PT0S").result.isZero)
        assertTrue(strictDuration("P0D").result != strictDuration("PT0S").result)
        assertEquals(strictDuration("P1D").result, strictDuration("P1D").result)
        assertEquals(strictDuration("P1D").result.hashCode(), strictDuration("P1D").result.hashCode())
        assertEquals("DurationValue(units=2)", strictDuration("P1D").result.toString())
    }

    @Test
    fun timeUnitsWithoutDesignator() {
        assertAccepted("PT90M", duration("P90M"), ::encodeDuration, "ICAL-V009@3")
        assertRejected(strictDuration("P90M"), "ICAL-V009@3", "$V001@3")
        assertAccepted("PT1H30M", duration("P1H30M"), ::encodeDuration, "ICAL-V009@2")
        assertAccepted("P1DT2H", duration("P1D2H"), ::encodeDuration, "ICAL-V009@4")
        assertRejected(duration("P1HT30M"), "ICAL-V009@2", "$V001@3")
        assertRejected(duration("P1W2H"), "$V001@4")
    }

    @Test
    fun skippedMinutes() {
        assertAccepted("PT1H0M30S", duration("PT1H30S"), ::encodeDuration, "ICAL-V010@6")
        assertRejected(strictDuration("PT1H30S"), "ICAL-V010@6", "$V001@6")
    }

    @Test
    fun durationWhitespace() {
        assertAccepted("PT15M", duration(" PT15M"), ::encodeDuration, "ICAL-V013@0")
        assertRejected(strictDuration("PT15M "), "ICAL-V013@5", "$V001@5")
    }

    @Test
    fun malformedDurations() {
        val cases = mapOf(
            "" to 0,
            "15M" to 0,
            "-" to 1,
            "P" to 1,
            "PT" to 1,
            "P1DT" to 3,
            "P1" to 2,
            "PX" to 1,
            "P1Y" to 2,
            "P1W1D" to 4,
            "P1D1D" to 4,
            "PT1D" to 3,
            "PT1M1H" to 5,
            "PT1M1M" to 5,
            "PTT1H" to 2,
            "P1WT1H" to 3,
            "P1.5D" to 2,
            "--P1D" to 1,
        )
        for ((value, offset) in cases) {
            assertRejected(strictDuration(value), "$V001@$offset")
        }
        assertRejected(strictDuration("P2147483648D"), "$V002@1")
        assertAccepted("P2147483647D", strictDuration("P2147483647D"), ::encodeDuration)
    }

    @Test
    fun rfcPeriodExamples() {
        val explicit = strictPeriod("19970101T180000Z/19970102T070000Z")
        assertAccepted("19970101T180000Z/19970102T070000Z", explicit, ::encodePeriod)
        assertAccepted("19970101T180000Z/PT5H30M", strictPeriod("19970101T180000Z/PT5H30M"), ::encodePeriod)
        val busy = "19970308T160000Z/PT3H,19970308T200000Z/PT1H"
        assertAccepted(busy, strictPeriods(busy), ::encodePeriodList)
        val single = "19970308T230000Z/19970309T000000Z"
        assertAccepted(single, strictPeriods(single), ::encodePeriodList)
        assertAccepted("19970308T160000Z/PT8H30M", strictPeriods("19970308T160000Z/PT8H30M"), ::encodePeriodList)
        val zoned = strictPeriod("19970101T180000/PT1H", "Europe/Moscow").result
        assertEquals("Europe/Moscow", zoned.start.tzid)
        assertEquals(zoned, strictPeriod("19970101T180000/PT1H", "Europe/Moscow").result)
        assertEquals(zoned.hashCode(), strictPeriod("19970101T180000/PT1H", "Europe/Moscow").result.hashCode())
        assertTrue(zoned.toString().indexOf("1997") < 0)
    }

    @Test
    fun periodThatDoesNotEndAfterItsStart() {
        val reversed = "19970102T070000Z/19970101T180000Z"
        assertAccepted(reversed, period(reversed), ::encodePeriod, "ICAL-V022@17")
        assertRejected(strictPeriod(reversed), "ICAL-V022@17", "$V001@17")
        assertAccepted("19970101T180000Z/-PT1H", period("19970101T180000Z/-PT1H"), ::encodePeriod, "ICAL-V022@17")
        assertRejected(strictPeriod("19970101T180000Z/PT0S"), "ICAL-V022@17", "$V001@17")
        val reversedFields = listOf(
            "19980101T000000Z/19970101T000000Z",
            "19970201T000000Z/19970101T000000Z",
            "19970102T000000Z/19970101T000000Z",
            "19970101T010000Z/19970101T000000Z",
            "19970101T000100Z/19970101T000000Z",
            "19970101T000001Z/19970101T000000Z",
            "19970101T000000Z/19970101T000000Z",
        )
        for (value in reversedFields) {
            assertRejected(strictPeriod(value), "ICAL-V022@17", "$V001@17")
        }
        val mixed = "19970102T070000Z/19970101T180000"
        assertAccepted(mixed, strictPeriod(mixed), ::encodePeriod)
    }

    @Test
    fun malformedPeriods() {
        assertRejected(strictPeriod("19970101T180000Z"), "$V001@16")
        assertRejected(strictPeriod("19970101/19970102"), "$V001@8")
        assertRejected(strictPeriod("19970101T180000Z/"), "$V001@17")
        assertRejected(strictPeriod("19970101T180000Z/19970102"), "$V001@25")
        assertRejected(strictPeriod("19970101T180000Z/P"), "$V001@18")
        assertAccepted("19970101T180000Z/PT90M", period("19970101T180000Z/P90M"), ::encodePeriod, "ICAL-V009@20")
    }

    @Test
    fun periodListsSkipBadItemsOnlyWhenLenient() {
        val value = "19970308T160000Z/PT3H,,19970308T200000Z,19970308T210000Z/PT1H"
        assertAccepted(
            "19970308T160000Z/PT3H,19970308T210000Z/PT1H",
            periods(value),
            ::encodePeriodList,
            "ICAL-V016@22",
            "$V001@39",
        )
        assertRejected(strictPeriods(value), "ICAL-V016@22", "$V001@22")
        assertRejected(strictPeriods("19970308T160000Z/PT3H,x"), "$V001@23")
        assertAccepted("19970308T160000Z/PT3H", periods(" 19970308T160000Z/PT3H"), ::encodePeriodList, "ICAL-V013@0")
    }
}

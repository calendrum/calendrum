package io.github.calendrum.anonymize

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateShifterTest {
    @Test
    fun shiftsDatesAndDateTimesByWholeWeeks() {
        assertEquals("20240307", shifted("20240229", 1))
        assertEquals("20250101T090000", shifted("20241225T090000", 1))
        assertEquals("20241225T235960Z", shifted("20250101T235960Z", -1))
        assertEquals("20000301", shifted("20000223", 1))
        assertEquals("21000301", shifted("21000222", 1))
        assertEquals("00000108", shifted("00000101", 1))
        assertEquals("20261231", shifted("20261231", 0))
        assertEquals("20370101", shifted("20261231", 522))
    }

    @Test
    fun rejectsWhatIsNotADateOrDateTime() {
        for (value in listOf(
            "2024022", "202402290", "20230229", "20241301", "20240100", "20240132", "20240101T240000",
            "20240101T006000", "20240101T000061", "20240101t090000", "20240101T090000z", "2024-01-01", "",
            "20240101T0900", "tomorrow", "20240101T090000ZZ",
        )) {
            val bytes = value.encodeToByteArray()
            assertFalse(DateShifter(1).token(bytes, 0, bytes.size), value)
            assertEquals(value, bytes.decodeToString(), value)
        }
    }

    @Test
    fun keepsTheYearWithinFourDigits() {
        val late = "99991230".encodeToByteArray()
        assertFalse(DateShifter(1).token(late, 0, late.size))
        val early = "00000102".encodeToByteArray()
        assertFalse(DateShifter(-1).token(early, 0, early.size))
    }

    @Test
    fun keepsTheDayOfTheWeek() {
        var day = 0
        while (day < 3000) {
            val date = civilDate(day)
            for (weeks in listOf(-53, -1, 1, 7, 52, 1000)) {
                val bytes = date.encodeToByteArray()
                assertTrue(DateShifter(weeks).token(bytes, 0, bytes.size), date)
                assertEquals(weekday(date), weekday(bytes.decodeToString()), "$date + $weeks weeks")
                assertEquals(weeks * 7L, daysBetween(date, bytes.decodeToString()), "$date + $weeks weeks")
            }
            day += 37
        }
    }

    @Test
    fun shiftsListsAndPeriods() {
        assertEquals("20260914T090000,20260921T090000" to 0, list("20260907T090000,20260914T090000", 1))
        assertEquals("20260914T090000Z/20260914T100000Z" to 0, list("20260907T090000Z/20260907T100000Z", 1))
        assertEquals("20260914T090000Z/PT1H" to 0, list("20260907T090000Z/PT1H", 1))
        assertEquals("20260914,bad,20260915" to 1, list("20260907,bad,20260908", 1))
    }

    @Test
    fun shiftsOnlyTheUntilOfARule() {
        val rule = "FREQ=WEEKLY;BYDAY=MO,WE;until=20261221T205959Z;COUNT=20150101".encodeToByteArray()
        assertEquals(0, DateShifter(2).rule(rule, 0, rule.size))
        assertEquals("FREQ=WEEKLY;BYDAY=MO,WE;until=20270104T205959Z;COUNT=20150101", rule.decodeToString())
        val broken = "FREQ=DAILY;UNTIL=soon".encodeToByteArray()
        assertEquals(1, DateShifter(2).rule(broken, 0, broken.size))
    }

    @Test
    fun flagsRulesWhoseMeaningAShiftMayChange() {
        for (rule in listOf(
            "FREQ=MONTHLY;BYDAY=2MO",
            "FREQ=YEARLY",
            "FREQ=WEEKLY;BYMONTH=3",
            "FREQ=DAILY;BYSETPOS=1",
        )) {
            val bytes = rule.encodeToByteArray()
            assertTrue(ruleMeaningMayChange(bytes, 0, bytes.size), rule)
        }
        for (rule in listOf("FREQ=WEEKLY;BYDAY=MO;UNTIL=20261221T205959Z", "FREQ=DAILY;COUNT=5")) {
            val bytes = rule.encodeToByteArray()
            assertFalse(ruleMeaningMayChange(bytes, 0, bytes.size), rule)
        }
    }

    private fun shifted(value: String, weeks: Int): String {
        val bytes = value.encodeToByteArray()
        assertTrue(DateShifter(weeks).token(bytes, 0, bytes.size), value)
        return bytes.decodeToString()
    }

    private fun list(value: String, weeks: Int): Pair<String, Int> {
        val bytes = value.encodeToByteArray()
        val unrecognized = DateShifter(weeks).list(bytes, 0, bytes.size)
        return bytes.decodeToString() to unrecognized
    }
}

internal fun daysFromCivil(year: Int, month: Int, day: Int): Long {
    val y = (if (month <= 2) year - 1 else year).toLong()
    val era = y.floorDiv(400L)
    val yearOfEra = y - era * 400
    val dayOfYear = (153 * (if (month > 2) month - 3 else month + 9) + 2) / 5 + day - 1
    return era * 146097 + yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear - 719468
}

internal fun daysOf(date: String): Long =
    daysFromCivil(date.substring(0, 4).toInt(), date.substring(4, 6).toInt(), date.substring(6, 8).toInt())

internal fun weekday(date: String): Int = (daysOf(date) + 3).mod(7)

private fun daysBetween(from: String, to: String): Long = daysOf(to) - daysOf(from)

private fun civilDate(offset: Int): String {
    var year = 1996
    var month = 1
    var day = 1
    repeat(offset) {
        day++
        val length = when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        if (day > length) {
            day = 1
            month++
            if (month > 12) {
                month = 1
                year++
            }
        }
    }
    return year.toString().padStart(4, '0') + month.toString().padStart(2, '0') + day.toString().padStart(2, '0')
}

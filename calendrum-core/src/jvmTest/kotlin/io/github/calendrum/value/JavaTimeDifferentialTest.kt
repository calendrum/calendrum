package io.github.calendrum.value

import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaTimeDifferentialTest {
    private val decoder = ValueDecoder(true, false, null)

    @Test
    fun datesAgreeWithLocalDate() {
        val years = (0..9999 step 37) + listOf(1600, 1601, 1900, 1969, 1970, 2000, 2024, 2038, 2100, 9999)
        var checked = 0
        for (year in years) {
            for (month in 0..13) {
                for (day in 0..32) {
                    val text = pad(year, 4) + pad(month, 2) + pad(day, 2)
                    val ours = decoder.decodeDate(text, 0, text.length)
                    val theirs = try {
                        LocalDate.of(year, month, day)
                    } catch (_: DateTimeException) {
                        null
                    }
                    assertEquals(theirs != null, ours != null, text)
                    if (ours != null && theirs != null) assertEquals(theirs.toEpochDay(), ours.epochDay(), text)
                    checked += 1
                }
            }
        }
        assertEquals(years.size * 14 * 33, checked)
    }

    @Test
    fun timesAgreeWithLocalTimeExceptTheLeapSecond() {
        for (hour in 0..25) {
            for (minute in 0..61) {
                for (second in 0..61) {
                    val text = pad(hour, 2) + pad(minute, 2) + pad(second, 2) + "Z"
                    val ours = decoder.decodeTime(text, 0, text.length)
                    val theirs = try {
                        LocalTime.of(hour, minute, second)
                    } catch (_: DateTimeException) {
                        null
                    }
                    if (second == 60 && hour < 24 && minute < 60) {
                        assertEquals(59, ours!!.second, text)
                    } else {
                        assertEquals(theirs != null, ours != null, text)
                    }
                }
            }
        }
    }

    @Test
    fun epochSecondsAgreeWithLocalDateTime() {
        val random = Random(1601)
        repeat(100_000) {
            val epochDay = random.nextLong(
                LocalDate.of(0, 1, 1).toEpochDay(),
                LocalDate.of(9999, 12, 31).toEpochDay() + 1,
            )
            val time = LocalDateTime.of(
                LocalDate.ofEpochDay(epochDay),
                LocalTime.ofSecondOfDay(random.nextLong(86_400)),
            )
            val text = pad(time.year, 4) + pad(time.monthValue, 2) + pad(time.dayOfMonth, 2) + "T" +
                pad(time.hour, 2) + pad(time.minute, 2) + pad(time.second, 2) + "Z"
            val ours = decoder.decodeDateTime(text, 0, text.length, null)!!
            assertEquals(time.toEpochSecond(ZoneOffset.UTC), ours.epochSecond(), text)
            assertEquals(text, encodeDateTime(ours))
        }
    }

    private fun pad(number: Int, width: Int): String = number.toString().padStart(width, '0')
}

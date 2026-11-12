package io.github.calendrum.benchmarks.recur

import biweekly.Biweekly
import biweekly.util.ICalDate
import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.StartMismatch
import io.github.calendrum.recur.expand
import io.github.calendrum.value.ICalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import kotlin.random.Random
import net.fortuna.ical4j.model.Recur as Ical4jRecur

internal class RivalRule(val rule: String, val start: String, val dateOnly: Boolean) {
    val startTime: LocalDateTime
        get() = if (dateOnly) {
            LocalDate.parse(start, DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay()
        } else {
            LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"))
        }

    val end: LocalDateTime
        get() = when (rule.substringAfter("FREQ=").substringBefore(';')) {
            "SECONDLY" -> startTime.plusDays(1)
            "MINUTELY" -> startTime.plusDays(20)
            "HOURLY" -> startTime.plusYears(1)
            else -> startTime.plusYears(3)
        }

    override fun toString(): String = "$rule from $start"
}

internal object RecurrenceRivals {
    private val basic = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
    private val basicDate = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val exclude = RecurrenceOptions.builder().startMismatch(StartMismatch.Exclude).build()

    fun ours(sample: RivalRule): List<String> {
        val event = event(sample)
        val start = event.start!!
        val end = endOf(sample)
        return event.rrule!!.expand(start, start, end, exclude).map { text(it) }.toList()
    }

    fun oracle(sample: RivalRule): List<String> =
        RecurrenceOracle.expand(sample.rule, sample.startTime, sample.dateOnly, sample.end).map {
            if (sample.dateOnly) it.toLocalDate().toString() else it.toString()
        }

    fun ical4j(sample: RivalRule): List<String> {
        val end = sample.end
        return if (sample.dateOnly) {
            val start = LocalDate.parse(sample.start, basicDate)
            Ical4jRecur<LocalDate>(sample.rule).getDates(start, start, end.toLocalDate())
                .filter { it < end.toLocalDate() }
                .map { it.toString() }
        } else {
            val start = LocalDateTime.parse(sample.start, basic)
            Ical4jRecur<LocalDateTime>(sample.rule).getDates(start, start, end)
                .filter { it < end }
                .map { it.toString() }
        }
    }

    fun biweekly(sample: RivalRule): List<String> {
        val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nBEGIN:VEVENT\r\nUID:x\r\n" +
            (if (sample.dateOnly) "DTSTART;VALUE=DATE:${sample.start}\r\n" else "DTSTART:${sample.start}\r\n") +
            "RRULE:${sample.rule}\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val event = Biweekly.parse(text).first().events.single()
        val zone = if (sample.dateOnly) TimeZone.getDefault() else TimeZone.getTimeZone("UTC")
        val offset = zone.toZoneId()
        val startLocal = if (sample.dateOnly) {
            LocalDate.parse(sample.start, basicDate).atStartOfDay()
        } else {
            LocalDateTime.parse(sample.start, basic)
        }
        val end = sample.end
        val startDate = ICalDate(java.util.Date.from(startLocal.atZone(offset).toInstant()), !sample.dateOnly)
        val iterator = event.recurrenceRule.value.getDateIterator(startDate, zone)
        val result = ArrayList<String>()
        while (iterator.hasNext() && result.size < LIMIT) {
            val local = LocalDateTime.ofInstant(iterator.next().toInstant(), offset)
            if (!local.isBefore(end)) break
            result += if (sample.dateOnly) local.toLocalDate().toString() else local.toString()
        }
        return result
    }

    private fun event(sample: RivalRule) = ICal.parse(
        "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:x\r\n" +
            (if (sample.dateOnly) "DTSTART;VALUE=DATE:${sample.start}\r\n" else "DTSTART:${sample.start}\r\n") +
            "RRULE:${sample.rule}\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
    ).calendars.single().events.single()

    private fun endOf(sample: RivalRule): ICalDateTime = if (sample.dateOnly) {
        ICalDateTime.Date(kotlinx.datetime.LocalDate.parse(sample.end.toLocalDate().toString()))
    } else {
        ICalDateTime.Floating(kotlinx.datetime.LocalDateTime.parse(sample.end.toString()))
    }

    private fun text(value: ICalDateTime): String = when (value) {
        is ICalDateTime.Date -> value.date.toString()
        is ICalDateTime.Floating -> LocalDateTime.parse(value.dateTime.toString()).toString()
        else -> value.toString()
    }

    fun rules(seed: Long, count: Int): List<RivalRule> {
        val random = Random(seed)
        return List(count) { rule(random) }
    }

    private fun rule(random: Random): RivalRule {
        val freqs = listOf("SECONDLY", "MINUTELY", "HOURLY", "DAILY", "WEEKLY", "MONTHLY", "YEARLY")
        val freq = freqs[listOf(0, 1, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6)[random.nextInt(20)]]
        val dateOnly = freq != "SECONDLY" && freq != "MINUTELY" && freq != "HOURLY" && random.nextInt(5) == 0
        val date = LocalDate.of(1995 + random.nextInt(35), 1 + random.nextInt(12), 1 + random.nextInt(28))
        val start = if (dateOnly) {
            date.format(basicDate)
        } else {
            date.atTime(random.nextInt(24), random.nextInt(4) * 15).format(basic)
        }
        val parts = ArrayList<String>()
        parts += "FREQ=$freq"
        if (random.nextInt(3) == 0) parts += "INTERVAL=${1 + random.nextInt(4)}"
        when (random.nextInt(4)) {
            0 -> parts += "COUNT=${1 + random.nextInt(40)}"

            1 -> parts += "UNTIL=" + date.plusDays(random.nextLong(1500)).let {
                if (dateOnly) it.format(basicDate) else it.atTime(random.nextInt(24), 0).format(basic)
            }
        }
        val subDaily = freq == "SECONDLY" || freq == "MINUTELY" || freq == "HOURLY"
        if (random.nextInt(3) == 0) parts += "BYMONTH=" + list(random, 1, 12, 3, false)
        val weekNo = freq == "YEARLY" && random.nextInt(6) == 0
        if (weekNo) parts += "BYWEEKNO=" + list(random, 1, 53, 2, true)
        if ((freq == "YEARLY" || subDaily) &&
            random.nextInt(6) == 0
        ) {
            parts += "BYYEARDAY=" + list(random, 1, 366, 3, true)
        }
        if (freq != "WEEKLY" && random.nextInt(3) == 0) parts += "BYMONTHDAY=" + list(random, 1, 31, 3, true)
        if (random.nextInt(2) == 0) {
            val numbered = freq == "MONTHLY" || (freq == "YEARLY" && !weekNo)
            parts += "BYDAY=" + (0 until 1 + random.nextInt(3)).joinToString(",") {
                val day = listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")[random.nextInt(7)]
                if (numbered &&
                    random.nextInt(3) == 0
                ) {
                    (if (random.nextBoolean()) "-" else "") + (1 + random.nextInt(4)) + day
                } else {
                    day
                }
            }
        }
        if (!dateOnly && random.nextInt(5) == 0) parts += "BYHOUR=" + list(random, 0, 23, 3, false)
        if (!dateOnly && random.nextInt(5) == 0) parts += "BYMINUTE=" + list(random, 0, 59, 2, false)
        if (parts.size > 2 && random.nextInt(5) == 0) parts += "BYSETPOS=" + list(random, 1, 5, 2, true)
        if (random.nextInt(5) == 0) parts += "WKST=" + listOf("MO", "SU", "SA")[random.nextInt(3)]
        return RivalRule(parts.joinToString(";"), start, dateOnly)
    }

    private fun list(random: Random, low: Int, high: Int, max: Int, signed: Boolean): String =
        (0 until 1 + random.nextInt(max)).map {
            val value = low + random.nextInt(high - low + 1)
            if (signed && random.nextInt(4) == 0) -value else value
        }.distinct().joinToString(",")

    const val LIMIT = 5000
}

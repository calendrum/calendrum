package io.github.calendrum.recur

import io.github.calendrum.model.Decoded
import io.github.calendrum.model.Properties
import io.github.calendrum.model.decode
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlin.random.Random

internal class RandomRule(val text: String, val rule: Recur, val start: ICalDateTime)

internal class RandomRules(seed: Long) {
    private val random = Random(seed)

    fun next(): RandomRule {
        while (true) {
            val text = ruleText()
            val start = start()
            val component = ICalComponent("VEVENT") { property("RRULE", text) }
            val decoded = component.decode(Properties.RRULE)
            if (decoded is Decoded.Value) return RandomRule(text, decoded.value, start)
        }
    }

    private fun start(): ICalDateTime {
        val date = LocalDate(1990 + random.nextInt(45), 1 + random.nextInt(12), 1 + random.nextInt(28))
        if (random.nextInt(6) == 0) return ICalDateTime.Date(date)
        val time = LocalDateTime(
            date.year,
            date.month.number,
            date.day,
            random.nextInt(24),
            random.nextInt(60),
            random.nextInt(4) * 15,
        )
        return ICalDateTime.Floating(time)
    }

    private fun ruleText(): String {
        val freq = FREQUENCIES[weightedFrequency()]
        val parts = ArrayList<String>()
        parts += "FREQ=$freq"
        if (random.nextInt(3) == 0) parts += "INTERVAL=${1 + random.nextInt(if (random.nextBoolean()) 4 else 40)}"
        when (random.nextInt(4)) {
            0 -> parts += "COUNT=${1 + random.nextInt(60)}"

            1 ->
                parts +=
                    "UNTIL=${2000 + random.nextInt(
                        40,
                    )}${pad(1 + random.nextInt(12))}${pad(1 + random.nextInt(28))}T120000"
        }
        val subDaily = freq == "HOURLY" || freq == "MINUTELY" || freq == "SECONDLY"
        if (random.nextInt(3) == 0) parts += "BYMONTH=" + list(1, 12, 3, false)
        if (freq == "YEARLY" && random.nextInt(5) == 0) parts += "BYWEEKNO=" + list(1, 53, 3, true)
        if ((freq == "YEARLY" || subDaily) && random.nextInt(5) == 0) parts += "BYYEARDAY=" + list(1, 366, 3, true)
        if (freq != "WEEKLY" && random.nextInt(3) == 0) parts += "BYMONTHDAY=" + list(1, 31, 4, true)
        if (random.nextInt(2) ==
            0
        ) {
            parts +=
                "BYDAY=" + days(freq == "MONTHLY" || (freq == "YEARLY" && !parts.any { it.startsWith("BYWEEKNO") }))
        }
        if (random.nextInt(4) == 0) parts += "BYHOUR=" + list(0, 23, 3, false)
        if (random.nextInt(4) == 0) parts += "BYMINUTE=" + list(0, 59, 3, false)
        if (random.nextInt(6) == 0) parts += "BYSECOND=" + list(0, 59, 2, false)
        if (parts.size > 2 && random.nextInt(4) == 0) parts += "BYSETPOS=" + list(1, 10, 2, true)
        if (random.nextInt(4) == 0) parts += "WKST=" + WEEKDAYS[random.nextInt(7)]
        return parts.joinToString(";")
    }

    private fun weightedFrequency(): Int = when (random.nextInt(20)) {
        0 -> 0
        1 -> 1
        2, 3 -> 2
        in 4..7 -> 3
        in 8..11 -> 4
        in 12..15 -> 5
        else -> 6
    }

    private fun list(low: Int, high: Int, max: Int, signed: Boolean): String {
        val size = 1 + random.nextInt(max)
        return (0 until size).joinToString(",") {
            val value = low + random.nextInt(high - low + 1)
            if (signed && random.nextInt(4) == 0) "-$value" else value.toString()
        }
    }

    private fun days(numbered: Boolean): String {
        val size = 1 + random.nextInt(4)
        return (0 until size).joinToString(",") {
            val day = WEEKDAYS[random.nextInt(7)]
            if (numbered && random.nextInt(3) == 0) {
                val ordinal = 1 + random.nextInt(5)
                (if (random.nextBoolean()) "-" else "") + ordinal + day
            } else {
                day
            }
        }
    }

    private fun pad(value: Int): String = if (value < 10) "0$value" else value.toString()

    override fun toString(): String = "RandomRules"
}

private val FREQUENCIES = listOf("SECONDLY", "MINUTELY", "HOURLY", "DAILY", "WEEKLY", "MONTHLY", "YEARLY")
private val WEEKDAYS = listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")

internal fun checkInvariants(sample: RandomRule, occurrences: List<ICalDateTime>): String? {
    val rule = sample.rule
    val keys = occurrences.map { it.localKey() }
    for (index in 1 until keys.size) {
        if (keys[index] <= keys[index - 1]) return "not increasing at $index"
    }
    val count = rule.count
    if (count != null && occurrences.size > count) return "more than COUNT"
    if (occurrences.any { it::class != sample.start::class }) return "form differs from DTSTART"
    val until = rule.until
    val first = if (occurrences.firstOrNull() == sample.start) 1 else 0
    if (until != null) {
        val bound = if (until is ICalDateTime.Date && sample.start !is ICalDateTime.Date) {
            until.localKey() + 86_399
        } else {
            until.localKey()
        }
        if (keys.drop(first).any { it > bound }) return "after UNTIL"
    }
    for (value in occurrences.drop(first)) {
        val dateTime = when (value) {
            is ICalDateTime.Floating -> value.dateTime
            is ICalDateTime.Date -> LocalDateTime(value.date, kotlinx.datetime.LocalTime(0, 0))
            else -> return "unexpected form"
        }
        val date = dateTime.date
        if (rule.byMonth.isNotEmpty() && rule.byMonth.none { it.month == date.month.number }) return "BYMONTH $value"
        if (rule.byMonthDay.isNotEmpty()) {
            val length = monthLength(date.year, date.month.number)
            if (rule.byMonthDay.none { it == date.day || it == date.day - length - 1 }) return "BYMONTHDAY $value"
        }
        if (rule.byDay.isNotEmpty() && rule.byDay.none { it.dayOfWeek == date.dayOfWeek }) return "BYDAY $value"
        if (rule.byYearDay.isNotEmpty()) {
            val yearLength = if (monthLength(date.year, 2) == 29) 366 else 365
            if (rule.byYearDay.none {
                    it == date.dayOfYear || it == date.dayOfYear - yearLength - 1
                }
            ) {
                return "BYYEARDAY $value"
            }
        }
        if (value is ICalDateTime.Floating) {
            if (rule.byHour.isNotEmpty() && dateTime.hour !in rule.byHour) return "BYHOUR $value"
            if (rule.byMinute.isNotEmpty() && dateTime.minute !in rule.byMinute) return "BYMINUTE $value"
            if (rule.bySecond.isNotEmpty() && dateTime.second !in rule.bySecond) return "BYSECOND $value"
        }
    }
    return null
}

private fun monthLength(year: Int, month: Int): Int = when (month) {
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    4, 6, 9, 11 -> 30
    else -> 31
}

internal fun DayOfWeek.mondayIndex(): Int = isoDayNumber - 1

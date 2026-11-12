package io.github.calendrum.benchmarks.recur

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

internal class OracleRule(text: String) {
    private val parts = text.split(';').associate { it.substringBefore('=') to it.substringAfter('=') }
    val freq: String = parts.getValue("FREQ")
    val interval: Int = parts["INTERVAL"]?.toInt() ?: 1
    val count: Int? = parts["COUNT"]?.toInt()
    val until: String? = parts["UNTIL"]
    val byMonth: List<Int>? = ints("BYMONTH")
    val byWeekNo: List<Int>? = ints("BYWEEKNO")
    val byYearDay: List<Int>? = ints("BYYEARDAY")
    val byMonthDay: List<Int>? = ints("BYMONTHDAY")
    val byDay: List<Pair<Int, DayOfWeek>>? = parts["BYDAY"]?.split(',')?.map {
        val day = DAYS.getValue(it.takeLast(2))
        val ordinal = it.dropLast(2)
        (if (ordinal.isEmpty()) 0 else ordinal.toInt()) to day
    }
    val byHour: List<Int>? = ints("BYHOUR")
    val byMinute: List<Int>? = ints("BYMINUTE")
    val bySecond: List<Int>? = ints("BYSECOND")
    val bySetPos: List<Int>? = ints("BYSETPOS")
    val weekStart: DayOfWeek = parts["WKST"]?.let { DAYS.getValue(it) } ?: DayOfWeek.MONDAY

    private fun ints(name: String): List<Int>? = parts[name]?.split(',')?.map { it.toInt() }

    override fun toString(): String = "OracleRule($freq)"

    companion object {
        val DAYS = mapOf(
            "MO" to DayOfWeek.MONDAY,
            "TU" to DayOfWeek.TUESDAY,
            "WE" to DayOfWeek.WEDNESDAY,
            "TH" to DayOfWeek.THURSDAY,
            "FR" to DayOfWeek.FRIDAY,
            "SA" to DayOfWeek.SATURDAY,
            "SU" to DayOfWeek.SUNDAY,
        )
    }
}

// A deliberately naive RFC 5545 §3.3.10 expansion on java.time: every day of every period, filters by definition, week
// numbers from `WeekFields.of(WKST, 4)`. It shares no code with the engine and judges the differential test against
// ical4j and biweekly.
internal object RecurrenceOracle {
    fun expand(text: String, start: LocalDateTime, dateOnly: Boolean, end: LocalDateTime): List<LocalDateTime> {
        val rule = OracleRule(text)
        val until = rule.until?.let {
            if (it.length == 8) {
                LocalDate.parse(it, java.time.format.DateTimeFormatter.BASIC_ISO_DATE).atTime(LocalTime.MAX)
            } else {
                LocalDateTime.parse(
                    it.removeSuffix("Z"),
                    java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"),
                )
            }
        }
        val result = ArrayList<LocalDateTime>()
        var emitted = 0
        var period = 0L
        while (true) {
            val periodStart = periodStart(rule, start, period)
            if (!periodStart.isBefore(end) || periodStart.year > 9999) break
            period++
            val candidates = candidates(rule, start, dateOnly, periodStart).sorted().distinct()
            val selected = rule.bySetPos?.let { positions ->
                positions.mapNotNull { position ->
                    val index = if (position > 0) position - 1 else candidates.size + position
                    candidates.getOrNull(index)
                }.sorted().distinct()
            } ?: candidates
            for (candidate in selected) {
                if (candidate.isBefore(start)) continue
                if (until != null && candidate.isAfter(until)) return result
                if (rule.count != null && emitted >= rule.count) return result
                if (!candidate.isBefore(end)) return result
                emitted++
                result += candidate
            }
        }
        return result
    }

    private fun periodStart(rule: OracleRule, start: LocalDateTime, period: Long): LocalDateTime {
        val step = period * rule.interval
        return when (rule.freq) {
            "YEARLY" -> LocalDate.of(start.year, 1, 1).plusYears(step).atStartOfDay()

            "MONTHLY" -> LocalDate.of(start.year, start.monthValue, 1).plusMonths(step).atStartOfDay()

            "WEEKLY" -> start.toLocalDate().with(
                TemporalAdjusters.previousOrSame(rule.weekStart),
            ).plusWeeks(step).atStartOfDay()

            "DAILY" -> start.toLocalDate().plusDays(step).atStartOfDay()

            "HOURLY" -> start.truncatedTo(ChronoUnit.HOURS).plusHours(step)

            "MINUTELY" -> start.truncatedTo(ChronoUnit.MINUTES).plusMinutes(step)

            else -> start.plusSeconds(step)
        }
    }

    private fun candidates(
        rule: OracleRule,
        start: LocalDateTime,
        dateOnly: Boolean,
        periodStart: LocalDateTime,
    ): List<LocalDateTime> {
        val days: List<LocalDate> = when (rule.freq) {
            "YEARLY" -> (0 until periodStart.toLocalDate().lengthOfYear()).map {
                periodStart.toLocalDate().plusDays(it.toLong())
            }

            "MONTHLY" -> (0 until periodStart.toLocalDate().lengthOfMonth()).map {
                periodStart.toLocalDate().plusDays(it.toLong())
            }

            "WEEKLY" -> (0 until 7).map { periodStart.toLocalDate().plusDays(it.toLong()) }

            else -> listOf(periodStart.toLocalDate())
        }
        val noDayParts =
            rule.byWeekNo == null && rule.byYearDay == null && rule.byMonthDay == null && rule.byDay == null
        var months = rule.byMonth
        var monthDays = rule.byMonthDay
        var weekdays: List<Pair<Int, DayOfWeek>>? = rule.byDay
        when (rule.freq) {
            "YEARLY" -> when {
                noDayParts -> {
                    if (months == null) months = listOf(start.monthValue)
                    monthDays = listOf(start.dayOfMonth)
                }

                rule.byDay == null && rule.byYearDay == null && rule.byWeekNo != null && rule.byMonthDay == null ->
                    weekdays = listOf(0 to start.dayOfWeek)

                rule.byDay == null && rule.byYearDay == null && rule.byWeekNo == null && months == null ->
                    months = listOf(start.monthValue)
            }

            "MONTHLY" -> if (noDayParts) monthDays = listOf(start.dayOfMonth)

            "WEEKLY" -> if (noDayParts) weekdays = listOf(0 to start.dayOfWeek)
        }
        val fields = WeekFields.of(rule.weekStart, 4)
        val kept = days.filter { day ->
            if (months != null && day.monthValue !in months) return@filter false
            if (rule.byWeekNo != null) {
                val week = day.get(fields.weekOfWeekBasedYear())
                val weeks = day.range(fields.weekOfWeekBasedYear()).maximum.toInt()
                if (rule.byWeekNo.none { it == week || it == week - weeks - 1 }) return@filter false
            }
            if (rule.byYearDay != null) {
                val length = day.lengthOfYear()
                if (rule.byYearDay.none { it == day.dayOfYear || it == day.dayOfYear - length - 1 }) return@filter false
            }
            if (monthDays != null) {
                val length = day.lengthOfMonth()
                if (monthDays.none { it == day.dayOfMonth || it == day.dayOfMonth - length - 1 }) return@filter false
            }
            if (weekdays != null &&
                weekdays.none { (ordinal, weekday) -> matches(rule, months, day, ordinal, weekday) }
            ) {
                return@filter false
            }
            true
        }
        val hours = when {
            dateOnly -> listOf(0)
            rule.byHour != null -> rule.byHour
            rule.freq in DAILY_OR_LONGER -> listOf(start.hour)
            else -> (0..23).toList()
        }
        val minutes = when {
            dateOnly -> listOf(0)
            rule.byMinute != null -> rule.byMinute
            rule.freq in DAILY_OR_LONGER || rule.freq == "HOURLY" -> listOf(start.minute)
            else -> (0..59).toList()
        }
        val seconds = when {
            dateOnly -> listOf(0)
            rule.bySecond != null -> rule.bySecond.filter { it < 60 }
            rule.freq != "SECONDLY" -> listOf(start.second)
            else -> (0..59).toList()
        }
        return kept.flatMap { day ->
            when (rule.freq) {
                "HOURLY" -> if (periodStart.hour in hours) {
                    minutes.flatMap { minute -> seconds.map { day.atTime(periodStart.hour, minute, it) } }
                } else {
                    emptyList()
                }

                "MINUTELY" -> if (periodStart.hour in hours && periodStart.minute in minutes) {
                    seconds.map { day.atTime(periodStart.hour, periodStart.minute, it) }
                } else {
                    emptyList()
                }

                "SECONDLY" -> if (periodStart.hour in hours && periodStart.minute in minutes &&
                    periodStart.second in seconds
                ) {
                    listOf(periodStart)
                } else {
                    emptyList()
                }

                else -> hours.flatMap { hour ->
                    minutes.flatMap { minute -> seconds.map { day.atTime(hour, minute, it) } }
                }
            }
        }
    }

    private fun matches(
        rule: OracleRule,
        months: List<Int>?,
        day: LocalDate,
        ordinal: Int,
        weekday: DayOfWeek,
    ): Boolean {
        if (day.dayOfWeek != weekday) return false
        if (ordinal == 0 || rule.freq !in MONTHLY_OR_LONGER) return true
        val inYear = rule.freq == "YEARLY" && rule.byMonth == null
        val from = if (inYear) day.withDayOfYear(1) else day.withDayOfMonth(1)
        val to = if (inYear) day.withDayOfYear(day.lengthOfYear()) else day.withDayOfMonth(day.lengthOfMonth())
        val all = generateSequence(from.with(TemporalAdjusters.nextOrSame(weekday))) { it.plusWeeks(1) }
            .takeWhile { !it.isAfter(to) }
            .toList()
        val index = if (ordinal > 0) ordinal - 1 else all.size + ordinal
        return all.getOrNull(index) == day && (months == null || day.monthValue in months)
    }

    private val DAILY_OR_LONGER = setOf("DAILY", "WEEKLY", "MONTHLY", "YEARLY")
    private val MONTHLY_OR_LONGER = setOf("MONTHLY", "YEARLY")
}

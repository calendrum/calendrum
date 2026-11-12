package io.github.calendrum.recur

import io.github.calendrum.ICal
import io.github.calendrum.Problem
import io.github.calendrum.ProblemListener
import io.github.calendrum.model.calendars
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.number

internal fun newYorkOffset(local: LocalDateTime): UtcOffset {
    val year = local.year
    val dstStart: LocalDateTime
    val dstEnd: LocalDateTime
    if (year >= 2007) {
        dstStart = LocalDateTime(nthSunday(year, 3, 2), kotlinx.datetime.LocalTime(2, 0))
        dstEnd = LocalDateTime(nthSunday(year, 11, 1), kotlinx.datetime.LocalTime(2, 0))
    } else {
        dstStart = LocalDateTime(nthSunday(year, 4, 1), kotlinx.datetime.LocalTime(2, 0))
        dstEnd = LocalDateTime(lastSunday(year, 10), kotlinx.datetime.LocalTime(2, 0))
    }
    val daylight = local >= dstStart && local < dstEnd
    return UtcOffset(hours = if (daylight) -4 else -5)
}

private fun nthSunday(year: Int, month: Int, n: Int): LocalDate {
    var date = LocalDate(year, month, 1)
    while (date.dayOfWeek != DayOfWeek.SUNDAY) date = LocalDate.fromEpochDays(date.toEpochDays() + 1)
    return LocalDate.fromEpochDays(date.toEpochDays() + (n - 1) * 7)
}

private fun lastSunday(year: Int, month: Int): LocalDate {
    var date = LocalDate(year, month, if (month == 2) 28 else 30).let {
        if (month == 10) LocalDate(year, month, 31) else it
    }
    while (date.dayOfWeek != DayOfWeek.SUNDAY) date = LocalDate.fromEpochDays(date.toEpochDays() - 1)
    return date
}

internal class ProblemSink : ProblemListener {
    val problems = ArrayList<Problem>()

    val codes: List<String> get() = problems.map { it.code.id }

    override fun onProblem(problem: Problem) {
        problems += problem
    }
}

internal fun event(vararg lines: String): io.github.calendrum.model.VEvent {
    val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:x\r\n" + lines.joinToString("") { "$it\r\n" } +
        "END:VEVENT\r\nEND:VCALENDAR\r\n"
    return ICal.parse(text).calendars.single().events.single()
}

internal fun local(value: ICalDateTime): String = when (value) {
    is ICalDateTime.Date -> value.date.toString()
    is ICalDateTime.Floating -> value.dateTime.toString()
    is ICalDateTime.Zoned -> value.dateTime.toString()
    is ICalDateTime.Utc -> value.instant.toString()
}

internal fun day(year: Int, month: Int, date: Int, time: String = "09:00"): String =
    LocalDate(year, month, date).let { "${it.year}-${pad(it.month.number)}-${pad(it.day)}T$time" }

private fun pad(value: Int): String = if (value < 10) "0$value" else value.toString()

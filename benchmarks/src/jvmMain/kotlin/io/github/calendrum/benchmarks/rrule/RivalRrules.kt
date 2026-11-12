package io.github.calendrum.benchmarks.rrule

import biweekly.Biweekly
import biweekly.util.ICalDate
import biweekly.util.Recurrence
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.TimeZone
import net.fortuna.ical4j.model.Recur as Ical4jRecur

object RivalRrules {
    private val start: LocalDateTime = LocalDateTime.of(2026, 1, 5, 9, 0)
    private val end: LocalDateTime = start.plusYears(10)
    private val utc: TimeZone = TimeZone.getTimeZone("UTC")
    private val endMillis: Long = end.toInstant(ZoneOffset.UTC).toEpochMilli()
    private val startDate: ICalDate = ICalDate(java.util.Date.from(start.toInstant(ZoneOffset.UTC)), true)

    fun ical4j(set: String): List<Ical4jRecur<LocalDateTime>> = RruleRules.texts(set).map { Ical4jRecur(it) }

    fun ical4jCount(rule: Ical4jRecur<LocalDateTime>): Int = rule.getDates(start, start, end).count { it < end }

    fun biweekly(set: String): List<Recurrence> = RruleRules.texts(set).map { text ->
        val calendar = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nBEGIN:VEVENT\r\nUID:x\r\nDTSTART:${RruleRules.START}\r\n" +
            "RRULE:$text\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        Biweekly.parse(calendar).first().events.single().recurrenceRule.value
    }

    fun biweeklyCount(rule: Recurrence): Int {
        val iterator = rule.getDateIterator(startDate, utc)
        var count = 0
        while (iterator.hasNext()) {
            if (iterator.next().time >= endMillis) break
            count++
        }
        return count
    }

    fun ical4jTotal(set: String): Int = ical4j(set).sumOf { ical4jCount(it) }

    fun biweeklyTotal(set: String): Int = biweekly(set).sumOf { biweeklyCount(it) }

    val expectedIcal4jTotals: Map<String, Int> = mapOf(RruleRules.SIMPLE to 5_348, RruleRules.COMPLEX to 22_842)
    val expectedBiweeklyTotals: Map<String, Int> = mapOf(RruleRules.SIMPLE to 5_348, RruleRules.COMPLEX to 22_842)
}

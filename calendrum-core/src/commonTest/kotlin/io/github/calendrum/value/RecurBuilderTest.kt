package io.github.calendrum.value

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RecurBuilderTest {
    private val decoder = ValueDecoder(false, false, null)

    private fun decoded(rule: String): Recur = Recur(decoder.decodeRecur(rule, 0, rule.length)!!)

    private fun rebuilt(rule: String, decoded: Recur): Recur {
        val builder = Recur.builder(decoded.frequency)
        decoded.until?.let { builder.until(it) }
        decoded.count?.let { builder.count(it) }
        if (rule.contains("INTERVAL=")) builder.interval(decoded.interval)
        if (decoded.bySecond.isNotEmpty()) builder.bySecond(*decoded.bySecond.toIntArray())
        if (decoded.byMinute.isNotEmpty()) builder.byMinute(*decoded.byMinute.toIntArray())
        if (decoded.byHour.isNotEmpty()) builder.byHour(*decoded.byHour.toIntArray())
        if (decoded.byDay.isNotEmpty()) builder.byDay(*decoded.byDay.toTypedArray())
        if (decoded.byMonthDay.isNotEmpty()) builder.byMonthDay(*decoded.byMonthDay.toIntArray())
        if (decoded.byYearDay.isNotEmpty()) builder.byYearDay(*decoded.byYearDay.toIntArray())
        if (decoded.byWeekNo.isNotEmpty()) builder.byWeekNo(*decoded.byWeekNo.toIntArray())
        if (decoded.byMonth.isNotEmpty()) builder.byMonth(*decoded.byMonth.map { it.month }.toIntArray())
        if (decoded.bySetPos.isNotEmpty()) builder.bySetPos(*decoded.bySetPos.toIntArray())
        decoded.weekStart?.let { builder.weekStart(it) }
        return builder.build()
    }

    @Test
    fun everyRfc5545ExampleRuleBuilds() {
        val rules = RecurCodecTest.RFC_RULES.filter { !it.contains("RSCALE") }
        assertEquals(49, rules.size)
        for (rule in rules) {
            val decoded = decoded(rule)
            val built = rebuilt(rule, decoded)
            assertEquals(decoded, built, rule)
            assertEquals(decoded.toString(), built.toString(), rule)
        }
    }

    @Test
    fun theProposalRuleBuilds() {
        val until = ICalDateTime.Utc(kotlin.time.Instant.fromEpochSeconds(1_798_761_600))
        val rule = Recur.builder(Frequency.MONTHLY).byDay(WeekdayNum(-1, DayOfWeek.FRIDAY)).until(until).build()
        assertEquals("FREQ=MONTHLY;UNTIL=20270101T000000Z;BYDAY=-1FR", rule.toString())
        val weekly = Recur.builder(Frequency.WEEKLY).byDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).count(10).build()
        assertEquals("FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE", weekly.toString())
        val replaced = Recur.builder(Frequency.DAILY).byHour(1).byHour(9, 17).interval(2).interval(3).build()
        assertEquals("FREQ=DAILY;INTERVAL=3;BYHOUR=9,17", replaced.toString())
    }

    @Test
    fun countAndUntilAreExclusive() {
        val until = ICalDateTime.Floating(LocalDateTime(2026, 12, 1, 0, 0))
        assertFailsWith<IllegalStateException> { Recur.builder(Frequency.DAILY).count(3).until(until) }
        assertFailsWith<IllegalStateException> { Recur.builder(Frequency.DAILY).until(until).count(3) }
        assertFailsWith<IllegalArgumentException> {
            Recur.builder(Frequency.DAILY).until(ICalDateTime.Zoned(LocalDateTime(2026, 12, 1, 0, 0), "Europe/Paris"))
        }
    }

    @Test
    fun partsThatTheFrequencyDoesNotAllowAreRejected() {
        val numbered = WeekdayNum(1, DayOfWeek.MONDAY)
        for (frequency in Frequency.entries) {
            if (frequency != Frequency.YEARLY) {
                assertFailsWith<IllegalArgumentException>(frequency.name) { Recur.builder(frequency).byWeekNo(20) }
            }
            if (frequency == Frequency.DAILY || frequency == Frequency.WEEKLY || frequency == Frequency.MONTHLY) {
                assertFailsWith<IllegalArgumentException>(frequency.name) { Recur.builder(frequency).byYearDay(1) }
            }
            if (frequency != Frequency.MONTHLY && frequency != Frequency.YEARLY) {
                assertFailsWith<IllegalArgumentException>(frequency.name) { Recur.builder(frequency).byDay(numbered) }
            }
        }
        assertFailsWith<IllegalArgumentException> { Recur.builder(Frequency.WEEKLY).byMonthDay(1) }
        assertFailsWith<IllegalStateException> { Recur.builder(Frequency.MONTHLY).bySetPos(1).build() }
        assertFailsWith<IllegalStateException> {
            Recur.builder(Frequency.YEARLY).byWeekNo(20).byDay(WeekdayNum(1, DayOfWeek.MONDAY)).build()
        }
        Recur.builder(Frequency.YEARLY).byWeekNo(20).byDay(DayOfWeek.MONDAY).build()
        Recur.builder(Frequency.MONTHLY).byDay(DayOfWeek.MONDAY).bySetPos(-1).build()
    }

    @Test
    fun valuesOutsideTheirRangesAreRejected() {
        val yearly = { Recur.builder(Frequency.YEARLY) }
        val cases = listOf<() -> Unit>(
            { yearly().bySecond(61) },
            { yearly().bySecond(-1) },
            { yearly().byMinute(60) },
            { yearly().byHour(24) },
            { yearly().byMonthDay(0) },
            { yearly().byMonthDay(32) },
            { yearly().byMonthDay(-32) },
            { yearly().byYearDay(0) },
            { yearly().byYearDay(367) },
            { yearly().byWeekNo(54) },
            { yearly().byWeekNo(0) },
            { yearly().byMonth(0) },
            { yearly().byMonth(13) },
            { yearly().bySetPos(0) },
            { yearly().bySetPos(-367) },
            { yearly().byDay(WeekdayNum(54, DayOfWeek.MONDAY)) },
            { yearly().byDay(WeekdayNum(0, DayOfWeek.MONDAY)) },
            { yearly().interval(0) },
            { yearly().count(0) },
            { yearly().byHour() },
            { yearly().byDay(*emptyArray<DayOfWeek>()) },
        )
        for ((index, case) in cases.withIndex()) {
            assertFailsWith<IllegalArgumentException>("case $index") { case() }
        }
        val edges = yearly().bySecond(0, 60).byMinute(59).byHour(0, 23).byMonthDay(-31, 31).byYearDay(-366, 366)
            .byWeekNo(-53, 53).byMonth(1, 12).bySetPos(-366, 366).byDay(DayOfWeek.SUNDAY).build()
        assertEquals(
            "FREQ=YEARLY;BYSECOND=0,60;BYMINUTE=59;BYHOUR=0,23;BYDAY=SU;BYMONTHDAY=-31,31;BYYEARDAY=-366,366;" +
                "BYWEEKNO=-53,53;BYMONTH=1,12;BYSETPOS=-366,366",
            edges.toString(),
        )
    }
}

@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.value

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ArrayView
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName

/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
public enum class Frequency {
    /** Repeats every second (or every `INTERVAL` seconds). */
    SECONDLY,

    /** Repeats every minute. */
    MINUTELY,

    /** Repeats every hour. */
    HOURLY,

    /** Repeats every day. */
    DAILY,

    /** Repeats every week. */
    WEEKLY,

    /** Repeats every month. */
    MONTHLY,

    /** Repeats every year. */
    YEARLY,
}

/**
 * The `SKIP` rule part of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529#section-4.1.1): what to do with an
 * occurrence that falls on a date that does not exist, such as the 31st in a month with 30 days.
 */
public enum class Skip {
    /** Drops the occurrence. */
    OMIT,

    /** Moves it to the last valid date before it. */
    BACKWARD,

    /** Moves it to the first valid date after it. */
    FORWARD,
}

/**
 * An entry of `BYDAY`: a day of the week, optionally preceded by a number, as in `MO`, `2TU` or `-1FR`.
 *
 * The number selects the n-th such day in the month or year (`-1` is the last), and is allowed only with `MONTHLY` and
 * `YEARLY`.
 *
 * @property ordinal the number from 1 to 53 or from -53 to -1, or `null` for every such weekday.
 * @property dayOfWeek the day of the week.
 */
public class WeekdayNum(public val ordinal: Int?, public val dayOfWeek: DayOfWeek) {
    /** Returns `true` when [other] has the same number and day of week. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is WeekdayNum && ordinal == other.ordinal && dayOfWeek == other.dayOfWeek)

    /** Returns a hash code over the number and the day of week. */
    override fun hashCode(): Int = (ordinal ?: 0) * 31 + dayOfWeek.hashCode()

    /** Returns the entry as written in a rule, such as `MO` or `-1FR`. */
    override fun toString(): String = (ordinal?.toString() ?: "") + weekdayName(dayOfWeek.isoDayNumber - 1)
}

/**
 * An entry of `BYMONTH`: a month number, which with `RSCALE` of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529) can
 * be a leap month, written `5L`.
 *
 * @property month the month number, 1 to 12 in the Gregorian calendar.
 * @property isLeap whether this is a leap month; always false in the Gregorian calendar.
 */
public class MonthNum(public val month: Int, public val isLeap: Boolean) {
    /** Returns `true` when [other] has the same month and leap flag. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is MonthNum && month == other.month && isLeap == other.isLeap)

    /** Returns a hash code over the month and the leap flag. */
    override fun hashCode(): Int = month * 2 + (if (isLeap) 1 else 0)

    /** Returns the entry as written in a rule, such as `5` or `5L`. */
    override fun toString(): String = if (isLeap) "${month}L" else month.toString()
}

/**
 * A rule part the library does not know, such as an `X-` part or a part registered after RFC 7529, kept so that the
 * rule is written back unchanged.
 *
 * @property name the part name as written, in upper case.
 * @property value the part value as written.
 */
public class RecurPart(public val name: String, public val value: String) {
    /** Returns `true` when [other] has the same name and value. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is RecurPart && name == other.name && value == other.value)

    /** Returns a hash code over the name and the value. */
    override fun hashCode(): Int = name.hashCode() * 31 + value.hashCode()

    /** Returns the name only, never the value. */
    override fun toString(): String = "RecurPart($name)"
}

/**
 * A recurrence rule: the value of `RRULE` and `EXRULE` ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)), together with `RSCALE` and `SKIP` of [RFC
 * 7529](https://www.rfc-editor.org/rfc/rfc7529).
 *
 * Read one from a property with the typed model, or build one with [builder], which checks the combinations the
 * standard forbids. Rules read from input are accepted leniently (see `ICAL-V017` to `ICAL-V020`), so a rule read from
 * a file may hold combinations the builder would reject. A `Recur` is immutable; expanding it into dates is done by
 * `expand` in the recurrence package.
 */
public class Recur internal constructor(internal val value: RecurValue) {
    /** The `FREQ` of the rule. */
    public val frequency: Frequency get() = frequencyOf(value.freq)

    /** The `UNTIL` of the rule: a date, a local date-time or a UTC date-time, or `null` when the rule has none. */
    public val until: ICalDateTime? = value.until?.toPublic()

    /** The `COUNT`, or `null` when the rule has none. */
    public val count: Int? get() = if (value.count == ABSENT) null else value.count

    /** The `INTERVAL`, 1 when the rule has none. */
    public val interval: Int get() = if (value.interval == ABSENT) 1 else value.interval

    /** The `BYSECOND` values, 0 to 60; empty when the part is absent. */
    public val bySecond: List<Int> = numbers(value.bySecond)

    /** The `BYMINUTE` values, 0 to 59; empty when the part is absent. */
    public val byMinute: List<Int> = numbers(value.byMinute)

    /** The `BYHOUR` values, 0 to 23; empty when the part is absent. */
    public val byHour: List<Int> = numbers(value.byHour)

    /** The `BYDAY` entries; empty when the part is absent. */
    public val byDay: List<WeekdayNum> = weekdays(value.byDay)

    /** The `BYMONTHDAY` values, 1 to 31 or -31 to -1; empty when the part is absent. */
    public val byMonthDay: List<Int> = numbers(value.byMonthDay)

    /** The `BYYEARDAY` values, 1 to 366 or -366 to -1; empty when the part is absent. */
    public val byYearDay: List<Int> = numbers(value.byYearDay)

    /** The `BYWEEKNO` values, 1 to 53 or -53 to -1; empty when the part is absent. */
    public val byWeekNo: List<Int> = numbers(value.byWeekNo)

    /** The `BYMONTH` entries; empty when the part is absent. */
    public val byMonth: List<MonthNum> = months(value.byMonth)

    /** The `BYSETPOS` values, 1 to 366 or -366 to -1; empty when the part is absent. */
    public val bySetPos: List<Int> = numbers(value.bySetPos)

    /** The `WKST`, or `null` when the rule has none (Monday is then the default of the standard). */
    public val weekStart: DayOfWeek? get() = if (value.weekStart == ABSENT) null else DayOfWeek(value.weekStart + 1)

    /**
     * The `RSCALE` calendar name of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529), or `null` for the Gregorian
     * calendar.
     */
    public val rscale: String? get() = value.rscale

    /** The `SKIP` rule part, or `null` when the rule has none. */
    public val skip: Skip? get() = if (value.skip == ABSENT) null else skipOf(value.skip)

    /** The parts the library does not know, in the order they were written; empty for none. */
    public val unknownParts: List<RecurPart> = parts(value.extensions)

    /** Returns `true` when [other] is a rule with the same parts in the same form. */
    override fun equals(other: Any?): Boolean = this === other || (other is Recur && value == other.value)

    /** Returns a hash code over all parts. */
    override fun hashCode(): Int = value.hashCode()

    /**
     * Returns the rule in iCalendar text form, such as `FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE`; unknown parts are left out.
     */
    override fun toString(): String {
        val known = value
        return encodeRecur(
            RecurValue(
                known.freq,
                known.until,
                known.count,
                known.interval,
                known.bySecond,
                known.byMinute,
                known.byHour,
                known.byDay,
                known.byMonthDay,
                known.byYearDay,
                known.byWeekNo,
                known.byMonth,
                known.bySetPos,
                known.weekStart,
                known.rscale,
                known.skip,
                null,
            ),
        )
    }

    // RFC 5545 3.3.10: a rule part that the frequency does not allow or a value out of range is rejected at once;
    // combinations that depend on the order of calls (BYSETPOS alone, a numbered BYDAY with BYWEEKNO) are checked in
    // `build()`.

    /**
     * Builds a [Recur] that is correct by construction: the combinations the standard forbids are rejected as they are
     * added. Not thread-safe; obtain one with [Recur.builder].
     */
    @ICalDsl
    public class Builder internal constructor(private val frequency: Frequency) {
        private var until: ICalDateTime? = null
        private var count = ABSENT
        private var interval = ABSENT
        private var bySecond: IntArray? = null
        private var byMinute: IntArray? = null
        private var byHour: IntArray? = null
        private var byDay: IntArray? = null
        private var byMonthDay: IntArray? = null
        private var byYearDay: IntArray? = null
        private var byWeekNo: IntArray? = null
        private var byMonth: IntArray? = null
        private var bySetPos: IntArray? = null
        private var weekStart = ABSENT

        /**
         * Sets `UNTIL`, which is a date, a local date-time or a UTC date-time and must suit the type of `DTSTART`.
         *
         * @throws IllegalStateException if `COUNT` is already set.
         * @throws IllegalArgumentException if [value] is a zoned date-time or its year is outside 0000 to 9999.
         */
        public fun until(value: ICalDateTime): Builder = apply {
            check(count == ABSENT) { "COUNT and UNTIL are exclusive" }
            require(value !is ICalDateTime.Zoned) { "UNTIL is a date, a UTC date-time or a local date-time" }
            value.toValue()
            until = value
        }

        /**
         * Sets `COUNT`, the number of occurrences.
         *
         * @throws IllegalStateException if `UNTIL` is already set.
         * @throws IllegalArgumentException if [value] is less than 1.
         */
        public fun count(value: Int): Builder = apply {
            check(until == null) { "COUNT and UNTIL are exclusive" }
            require(value >= 1) { "COUNT is positive" }
            count = value
        }

        /**
         * Sets `INTERVAL`, the step in units of the frequency.
         *
         * @throws IllegalArgumentException if [value] is less than 1.
         */
        public fun interval(value: Int): Builder = apply {
            require(value >= 1) { "INTERVAL is positive" }
            interval = value
        }

        /**
         * Sets `BYSECOND` to [values], each 0 to 60.
         *
         * @throws IllegalArgumentException if there is no value or one is out of range.
         */
        public fun bySecond(vararg values: Int): Builder = apply { bySecond = checked(values, 0, 60, false) }

        /**
         * Sets `BYMINUTE` to [values], each 0 to 59.
         *
         * @throws IllegalArgumentException if there is no value or one is out of range.
         */
        public fun byMinute(vararg values: Int): Builder = apply { byMinute = checked(values, 0, 59, false) }

        /**
         * Sets `BYHOUR` to [values], each 0 to 23.
         *
         * @throws IllegalArgumentException if there is no value or one is out of range.
         */
        public fun byHour(vararg values: Int): Builder = apply { byHour = checked(values, 0, 23, false) }

        /**
         * Sets `BYDAY` to the given days, every such weekday.
         *
         * @throws IllegalArgumentException if no day is given.
         */
        public fun byDay(@ObjCName("daysOfWeek") vararg days: DayOfWeek): Builder = apply {
            require(days.isNotEmpty()) { "BYDAY needs a day" }
            byDay = IntArray(days.size) { weekdayEntry(0, days[it].isoDayNumber - 1) }
        }

        /**
         * Sets `BYDAY` to the given entries, which may carry a number such as the second Tuesday.
         *
         * @throws IllegalArgumentException if no entry is given, a number is used with a frequency other than `MONTHLY`
         *   or `YEARLY`, or a number is 0 or outside -53 to 53.
         */
        public fun byDay(vararg days: WeekdayNum): Builder = apply {
            require(days.isNotEmpty()) { "BYDAY needs a day" }
            val entries = IntArray(days.size)
            for (index in days.indices) {
                val ordinal = days[index].ordinal
                if (ordinal != null) {
                    require(frequency == Frequency.MONTHLY || frequency == Frequency.YEARLY) {
                        "BYDAY with a number needs FREQ=MONTHLY or FREQ=YEARLY"
                    }
                    require(ordinal != 0 && ordinal in -53..53) { "BYDAY number is within 1..53" }
                }
                entries[index] = weekdayEntry(ordinal ?: 0, days[index].dayOfWeek.isoDayNumber - 1)
            }
            byDay = entries
        }

        /**
         * Sets `BYMONTHDAY` to [values], each 1 to 31 or -31 to -1.
         *
         * @throws IllegalArgumentException if the frequency is `WEEKLY`, there is no value or one is out of range.
         */
        public fun byMonthDay(vararg values: Int): Builder = apply {
            require(frequency != Frequency.WEEKLY) { "BYMONTHDAY is not allowed with FREQ=WEEKLY" }
            byMonthDay = checked(values, 1, 31, true)
        }

        /**
         * Sets `BYYEARDAY` to [values], each 1 to 366 or -366 to -1.
         *
         * @throws IllegalArgumentException if the frequency is `DAILY`, `WEEKLY` or `MONTHLY`, there is no value or one
         *   is out of range.
         */
        public fun byYearDay(vararg values: Int): Builder = apply {
            require(frequency != Frequency.DAILY && frequency != Frequency.WEEKLY && frequency != Frequency.MONTHLY) {
                "BYYEARDAY is not allowed with FREQ=DAILY, WEEKLY or MONTHLY"
            }
            byYearDay = checked(values, 1, 366, true)
        }

        /**
         * Sets `BYWEEKNO` to [values], each 1 to 53 or -53 to -1.
         *
         * @throws IllegalArgumentException if the frequency is not `YEARLY`, there is no value or one is out of range.
         */
        public fun byWeekNo(vararg values: Int): Builder = apply {
            require(frequency == Frequency.YEARLY) { "BYWEEKNO needs FREQ=YEARLY" }
            byWeekNo = checked(values, 1, 53, true)
        }

        /**
         * Sets `BYMONTH` to [values], each 1 to 12.
         *
         * @throws IllegalArgumentException if there is no value or one is out of range.
         */
        public fun byMonth(vararg values: Int): Builder = apply {
            val months = checked(values, 1, 12, false)
            byMonth = IntArray(months.size) { monthEntry(months[it], false) }
        }

        /**
         * Sets `BYSETPOS` to [values], each 1 to 366 or -366 to -1. The rule also needs another `BYxxx` part.
         *
         * @throws IllegalArgumentException if there is no value or one is out of range.
         */
        public fun bySetPos(vararg values: Int): Builder = apply { bySetPos = checked(values, 1, 366, true) }

        /** Sets `WKST`, the day on which the week starts. */
        public fun weekStart(day: DayOfWeek): Builder = apply { weekStart = day.isoDayNumber - 1 }

        /**
         * Creates the rule.
         *
         * @throws IllegalStateException if `BYSETPOS` is used without another `BYxxx` part, or `BYWEEKNO` is combined
         *   with a numbered `BYDAY`.
         */
        public fun build(): Recur {
            check(bySetPos == null || hasOtherParts()) { "BYSETPOS needs another BYxxx rule part" }
            check(byWeekNo == null || !hasNumberedDay()) { "BYDAY with a number is not allowed with BYWEEKNO" }
            return Recur(
                RecurValue(
                    frequency.ordinal,
                    until?.toValue(),
                    count,
                    interval,
                    bySecond,
                    byMinute,
                    byHour,
                    byDay,
                    byMonthDay,
                    byYearDay,
                    byWeekNo,
                    byMonth,
                    bySetPos,
                    weekStart,
                    null,
                    ABSENT,
                    null,
                ),
            )
        }

        private fun hasOtherParts(): Boolean =
            bySecond != null || byMinute != null || byHour != null || byDay != null ||
                byMonthDay != null || byYearDay != null || byWeekNo != null || byMonth != null

        private fun hasNumberedDay(): Boolean {
            val days = byDay ?: return false
            for (entry in days) {
                if (ordinalOf(entry) != 0) return true
            }
            return false
        }

        /** Returns the frequency of the rule being built. */
        override fun toString(): String = "Recur.Builder($frequency)"
    }

    /** Creates rule builders. */
    public companion object {
        /** Returns a builder for a rule that repeats with the given [frequency]. */
        @JvmStatic
        public fun builder(frequency: Frequency): Builder = Builder(frequency)
    }
}

private fun checked(values: IntArray, low: Int, high: Int, signed: Boolean): IntArray {
    require(values.isNotEmpty()) { "A BYxxx rule part needs a value" }
    for (value in values) {
        val magnitude = if (signed && value < 0) -value else value
        require(magnitude in low..high) { "A BYxxx value is out of range" }
    }
    return values.copyOf()
}

private fun frequencyOf(index: Int): Frequency = when (index) {
    FREQ_SECONDLY -> Frequency.SECONDLY
    FREQ_MINUTELY -> Frequency.MINUTELY
    FREQ_HOURLY -> Frequency.HOURLY
    FREQ_DAILY -> Frequency.DAILY
    FREQ_WEEKLY -> Frequency.WEEKLY
    FREQ_MONTHLY -> Frequency.MONTHLY
    else -> Frequency.YEARLY
}

private fun skipOf(index: Int): Skip = when (index) {
    SKIP_OMIT -> Skip.OMIT
    SKIP_BACKWARD -> Skip.BACKWARD
    else -> Skip.FORWARD
}

private fun numbers(values: IntArray?): List<Int> {
    if (values == null) return emptyList()
    return ArrayView(Array(values.size) { values[it] })
}

private fun weekdays(values: IntArray?): List<WeekdayNum> {
    if (values == null) return emptyList()
    return ArrayView(
        Array(values.size) {
            val ordinal = ordinalOf(values[it])
            WeekdayNum(if (ordinal == 0) null else ordinal, DayOfWeek(weekdayOf(values[it]) + 1))
        },
    )
}

private fun months(values: IntArray?): List<MonthNum> {
    if (values == null) return emptyList()
    return ArrayView(Array(values.size) { MonthNum(monthOf(values[it]), isLeapMonth(values[it])) })
}

private fun parts(values: List<String>?): List<RecurPart> {
    if (values == null) return emptyList()
    return ArrayView(Array(values.size / 2) { RecurPart(values[it * 2], values[it * 2 + 1]) })
}

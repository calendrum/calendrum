package io.github.calendrum.value

import kotlinx.datetime.DateTimePeriod
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * A `DURATION` value: a signed amount of weeks, days, hours, minutes and seconds ([RFC 5545
 * §3.3.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.6)), such as `PT1H30M` or `-P2D`.
 *
 * Days and weeks are nominal: their length depends on the time zone and the date (a day can have 23 or 25 hours), so
 * they cannot become an exact `kotlin.time.Duration`. The value keeps which units it was written with, so reading and writing it
 * again gives the same text. Immutable. Equality compares the sign, the amounts and those units.
 */
public class ICalDuration internal constructor(internal val value: DurationValue) {
    /** Whether the duration is negative, written with a leading `-`. */
    public val isNegative: Boolean get() = value.isNegative

    /** The number of weeks, never negative; the sign is in [isNegative]. The standard allows weeks only alone. */
    public val weeks: Int get() = value.weeks

    /** The number of days, never negative. */
    public val days: Int get() = value.days

    /** The number of hours, never negative. */
    public val hours: Int get() = value.hours

    /** The number of minutes, never negative. */
    public val minutes: Int get() = value.minutes

    /** The number of seconds, never negative. */
    public val seconds: Int get() = value.seconds

    /**
     * Returns the duration as a kotlinx-datetime period with the sign applied to every unit; weeks become days (7
     * each).
     */
    public fun toDateTimePeriod(): DateTimePeriod {
        val sign = if (value.isNegative) -1 else 1
        return DateTimePeriod(
            days = sign * (value.weeks * 7 + value.days),
            hours = sign * value.hours,
            minutes = sign * value.minutes,
            seconds = sign * value.seconds,
        )
    }

    /**
     * Returns the exact length, or `null` when the duration has weeks or days, whose length depends on the time zone.
     */
    @JvmName("toDurationOrNull")
    public fun toDurationOrNull(): Duration? {
        if (value.weeks != 0 || value.days != 0) return null
        val exact = value.hours.hours + value.minutes.minutes + value.seconds.seconds
        return if (value.isNegative) -exact else exact
    }

    /** Returns `true` when [other] is a duration with the same sign, amounts and written units. */
    override fun equals(other: Any?): Boolean = this === other || (other is ICalDuration && value == other.value)

    /** Returns a hash code over the sign, the amounts and the written units. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the duration in iCalendar text form, such as `PT1H30M`. */
    override fun toString(): String = encodeDuration(value)

    /** Creates durations by hand. */
    public companion object {
        /**
         * Creates the duration that equals [duration] in hours, minutes and seconds.
         *
         * @throws IllegalArgumentException if [duration] is infinite, has a fraction of a second, or is longer than
         *   `Int.MAX_VALUE` hours.
         */
        // No `@JvmName("ofDuration")`: from Java it would take the raw `Long` inside `kotlin.time.Duration`, an encoding of
        // nanoseconds or milliseconds, not a count Java can supply; Java uses `ofSeconds`… or `ICalDurations`.
        @JvmStatic
        public fun of(duration: Duration): ICalDuration {
            require(duration.isFinite()) { "Infinite duration" }
            val negative = duration.isNegative()
            val magnitude = duration.absoluteValue
            val total = magnitude.inWholeSeconds
            require(magnitude == total.seconds) { "An iCalendar duration has whole seconds only" }
            val hours = total / 3600
            require(hours <= Int.MAX_VALUE) { "Duration too long for iCalendar" }
            val minutes = (total / 60 % 60).toInt()
            val seconds = (total % 60).toInt()
            var units = 0
            if (hours != 0L) units = units or UNIT_HOURS
            if (minutes != 0) units = units or UNIT_MINUTES
            if (seconds != 0 || units == 0) units = units or UNIT_SECONDS
            if (hours != 0L && seconds != 0) units = units or UNIT_MINUTES
            return ICalDuration(DurationValue(negative, 0, 0, hours.toInt(), minutes, seconds, units))
        }

        /**
         * Creates a duration of [weeks] weeks; negative values give a negative duration.
         *
         * @throws IllegalArgumentException if [weeks] is `Int.MIN_VALUE`.
         */
        @JvmStatic
        public fun ofWeeks(weeks: Int): ICalDuration = single(weeks, UNIT_WEEKS)

        /**
         * Creates a duration of [days] days; negative values give a negative duration.
         *
         * @throws IllegalArgumentException if [days] is `Int.MIN_VALUE`.
         */
        @JvmStatic
        public fun ofDays(days: Int): ICalDuration = single(days, UNIT_DAYS)

        /**
         * Creates a duration of [hours] hours; negative values give a negative duration.
         *
         * @throws IllegalArgumentException if [hours] is `Int.MIN_VALUE`.
         */
        @JvmStatic
        public fun ofHours(hours: Int): ICalDuration = single(hours, UNIT_HOURS)

        /**
         * Creates a duration of [minutes] minutes; negative values give a negative duration.
         *
         * @throws IllegalArgumentException if [minutes] is `Int.MIN_VALUE`.
         */
        @JvmStatic
        public fun ofMinutes(minutes: Int): ICalDuration = single(minutes, UNIT_MINUTES)

        /**
         * Creates a duration of [seconds] seconds; negative values give a negative duration.
         *
         * @throws IllegalArgumentException if [seconds] is `Int.MIN_VALUE`.
         */
        @JvmStatic
        public fun ofSeconds(seconds: Int): ICalDuration = single(seconds, UNIT_SECONDS)

        private fun single(amount: Int, unit: Int): ICalDuration {
            require(amount != Int.MIN_VALUE) { "Duration too long for iCalendar" }
            val magnitude = if (amount < 0) -amount else amount
            return ICalDuration(
                DurationValue(
                    amount < 0,
                    if (unit == UNIT_WEEKS) magnitude else 0,
                    if (unit == UNIT_DAYS) magnitude else 0,
                    if (unit == UNIT_HOURS) magnitude else 0,
                    if (unit == UNIT_MINUTES) magnitude else 0,
                    if (unit == UNIT_SECONDS) magnitude else 0,
                    unit,
                ),
            )
        }
    }
}

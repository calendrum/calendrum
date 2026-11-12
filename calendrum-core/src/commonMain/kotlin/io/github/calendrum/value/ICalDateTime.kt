package io.github.calendrum.value

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import kotlin.concurrent.Volatile
import kotlin.time.Instant

/**
 * A `DATE` or `DATE-TIME` value in one of the four forms the standard allows ([RFC 5545
 * §3.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.4),
 * [§3.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.5)): [Date], [Floating], [Utc] and [Zoned].
 *
 * The set of forms is closed by the standard, so a `when` over it is exhaustive. None of them is an instant by itself
 * except [Utc]; to get one from the others a time zone is needed (see `toInstant` with a resolver). The library never
 * uses the system time zone. All forms are immutable and compare by content, so [Floating] and [Zoned] values with the
 * same fields are equal even if they would be different instants. Years outside 0000 to 9999 cannot be written to
 * iCalendar text.
 */
public sealed interface ICalDateTime {
    // The date and time are kept as fields; `LocalDate` and `LocalDateTime` are created on the first read of
    // `date`/`dateTime` and cached. Any reference to a kotlinx-datetime class with `@Serializable(with = ...)` pulls
    // its serializer, and through `serialize` -> `toString()` the whole formatting machinery, into a Kotlin/Native or
    // Wasm binary. `equals`, `hashCode` and `toString` work on the fields; `toString` prints what
    // kotlinx-datetime prints.
    /**
     * A calendar date without a time of day: `VALUE=DATE`, such as `20261001`. A date-only start usually means an
     * all-day event.
     */
    public class Date private constructor(
        internal val year: Int,
        internal val month: Int,
        internal val day: Int,
        @Volatile private var cached: LocalDate?,
    ) : ICalDateTime {
        /** Creates the value of [date]. */
        public constructor(date: LocalDate) : this(date.year, date.month.number, date.day, date)

        internal constructor(year: Int, month: Int, day: Int) : this(year, month, day, null)

        /** The calendar date. */
        public val date: LocalDate
            get() = cached ?: LocalDate(year, month, day).also { cached = it }

        /** Returns `true` when [other] is a date with the same year, month and day. */
        override fun equals(other: Any?): Boolean =
            this === other || (other is Date && year == other.year && month == other.month && day == other.day)

        /** Returns a hash code over year, month and day. */
        override fun hashCode(): Int = civilHash(year, month, day, 0, 0)

        /** Returns the date in extended ISO 8601 form, such as `2026-10-01`; this is not the iCalendar text form. */
        override fun toString(): String = isoDateTime(year, month, day, NO_TIME, 0)
    }

    /**
     * A local date and time that is not tied to a time zone: `20261001T100000`. It means "10:00 wherever the reader
     * is".
     */
    public class Floating private constructor(
        internal val year: Int,
        internal val month: Int,
        internal val day: Int,
        internal val secondOfDay: Int,
        internal val nanosecond: Int,
        @Volatile private var cached: LocalDateTime?,
    ) : ICalDateTime {
        /** Creates the value of [dateTime]. */
        public constructor(dateTime: LocalDateTime) : this(
            dateTime.year,
            dateTime.month.number,
            dateTime.day,
            secondOfDay(dateTime),
            dateTime.nanosecond,
            dateTime,
        )

        internal constructor(year: Int, month: Int, day: Int, secondOfDay: Int) :
            this(year, month, day, secondOfDay, 0, null)

        internal constructor(year: Int, month: Int, day: Int, secondOfDay: Int, nanosecond: Int) :
            this(year, month, day, secondOfDay, nanosecond, null)

        /** The local date and time. */
        public val dateTime: LocalDateTime
            get() = cached ?: localDateTime(year, month, day, secondOfDay, nanosecond).also { cached = it }

        /** Returns `true` when [other] is a floating value with the same date and time of day. */
        override fun equals(other: Any?): Boolean = this === other ||
            (
                other is Floating && year == other.year && month == other.month && day == other.day &&
                    secondOfDay == other.secondOfDay && nanosecond == other.nanosecond
                )

        /** Returns a hash code over date and time of day. */
        override fun hashCode(): Int = civilHash(year, month, day, secondOfDay, nanosecond)

        /**
         * Returns the date and time in extended ISO 8601 form, such as `2026-10-01T10:00`; this is not the iCalendar
         * text form.
         */
        override fun toString(): String = isoDateTime(year, month, day, secondOfDay, nanosecond)
    }

    /**
     * A date and time in UTC, written with a trailing `Z`: `20261001T070000Z`. The only form that is an instant by
     * itself.
     *
     * @property instant the moment.
     */
    public class Utc(public val instant: Instant) : ICalDateTime {
        /** Returns `true` when [other] is a UTC value of the same instant. */
        override fun equals(other: Any?): Boolean = this === other || (other is Utc && instant == other.instant)

        /** Returns the hash code of the instant. */
        override fun hashCode(): Int = instant.hashCode()

        /** Returns the instant in ISO 8601 form, such as `2026-10-01T07:00:00Z`. */
        override fun toString(): String = instant.toString()
    }

    /**
     * A local date and time in the time zone named by the `TZID` parameter:
     * `DTSTART;TZID=Europe/Berlin:20261001T100000`.
     *
     * The name is kept as written; resolving it to rules needs a time zone resolver, since it may name a `VTIMEZONE` of
     * the file, an IANA zone or a vendor alias.
     *
     * @property tzid the `TZID` as written, never a resolved zone.
     */
    public class Zoned private constructor(
        internal val year: Int,
        internal val month: Int,
        internal val day: Int,
        internal val secondOfDay: Int,
        internal val nanosecond: Int,
        public val tzid: String,
        @Volatile private var cached: LocalDateTime?,
    ) : ICalDateTime {
        /** Creates the value of [dateTime] in the zone named [tzid]. */
        public constructor(dateTime: LocalDateTime, tzid: String) : this(
            dateTime.year,
            dateTime.month.number,
            dateTime.day,
            secondOfDay(dateTime),
            dateTime.nanosecond,
            tzid,
            dateTime,
        )

        internal constructor(year: Int, month: Int, day: Int, secondOfDay: Int, tzid: String) :
            this(year, month, day, secondOfDay, 0, tzid, null)

        internal constructor(year: Int, month: Int, day: Int, secondOfDay: Int, nanosecond: Int, tzid: String) :
            this(year, month, day, secondOfDay, nanosecond, tzid, null)

        /** The local date and time in the named zone. */
        public val dateTime: LocalDateTime
            get() = cached ?: localDateTime(year, month, day, secondOfDay, nanosecond).also { cached = it }

        /** Returns `true` when [other] is a zoned value with the same date, time of day and `TZID` text. */
        override fun equals(other: Any?): Boolean = this === other ||
            (
                other is Zoned && year == other.year && month == other.month && day == other.day &&
                    secondOfDay == other.secondOfDay && nanosecond == other.nanosecond && tzid == other.tzid
                )

        /** Returns a hash code over date, time of day and `TZID`. */
        override fun hashCode(): Int = civilHash(year, month, day, secondOfDay, nanosecond) * 31 + tzid.hashCode()

        /**
         * Returns the date and time followed by the zone name in brackets, such as `2026-10-01T10:00[Europe/Berlin]`.
         */
        override fun toString(): String = isoDateTime(year, month, day, secondOfDay, nanosecond) + "[" + tzid + "]"
    }
}

private const val NO_TIME = -1

private fun secondOfDay(dateTime: LocalDateTime): Int = dateTime.hour * 3600 + dateTime.minute * 60 + dateTime.second

private fun localDateTime(year: Int, month: Int, day: Int, secondOfDay: Int, nanosecond: Int): LocalDateTime =
    LocalDateTime(year, month, day, secondOfDay / 3600, secondOfDay / 60 % 60, secondOfDay % 60, nanosecond)

private fun civilHash(year: Int, month: Int, day: Int, secondOfDay: Int, nanosecond: Int): Int =
    (((year * 13 + month) * 32 + day) * 86_400 + secondOfDay) * 31 + nanosecond

// ISO 8601 as kotlinx-datetime writes it: a year outside 0000-9999 gets its sign (`+10000`, `-0001`), seconds only when
// they or the fraction are not zero, the fraction in groups of three digits.
private fun isoDateTime(year: Int, month: Int, day: Int, secondOfDay: Int, nanosecond: Int): String {
    val out = StringBuilder(29)
    if (year < 0) out.append('-')
    val magnitude = if (year < 0) -year else year
    if (magnitude > MAX_YEAR) {
        if (year > 0) out.append('+')
        out.append(magnitude)
    } else {
        appendDigits(out, magnitude, 4)
    }
    out.append('-')
    appendDigits(out, month, 2)
    out.append('-')
    appendDigits(out, day, 2)
    if (secondOfDay == NO_TIME) return out.toString()
    out.append('T')
    appendDigits(out, secondOfDay / 3600, 2)
    out.append(':')
    appendDigits(out, secondOfDay / 60 % 60, 2)
    val second = secondOfDay % 60
    if (second == 0 && nanosecond == 0) return out.toString()
    out.append(':')
    appendDigits(out, second, 2)
    if (nanosecond == 0) return out.toString()
    out.append('.')
    when {
        nanosecond % 1_000_000 == 0 -> appendDigits(out, nanosecond / 1_000_000, 3)
        nanosecond % 1_000 == 0 -> appendDigits(out, nanosecond / 1_000, 6)
        else -> appendDigits(out, nanosecond, 9)
    }
    return out.toString()
}

internal fun DateTimeValue.toPublic(): ICalDateTime = when (form) {
    FORM_DATE -> ICalDateTime.Date(year, month, day)
    FORM_FLOATING -> ICalDateTime.Floating(year, month, day, hour * 3600 + minute * 60 + second)
    FORM_UTC -> ICalDateTime.Utc(Instant.fromEpochSeconds(epochSecond()))
    else -> ICalDateTime.Zoned(year, month, day, hour * 3600 + minute * 60 + second, tzid!!)
}

internal fun ICalDateTime.toValue(): DateTimeValue = when (this) {
    is ICalDateTime.Date -> localValue(year, month, day, 0, FORM_DATE, null)
    is ICalDateTime.Floating -> localValue(year, month, day, secondOfDay, FORM_FLOATING, null)
    is ICalDateTime.Zoned -> localValue(year, month, day, secondOfDay, FORM_ZONED, tzid)
    is ICalDateTime.Utc -> utcValue(instant.epochSeconds)
}

internal fun ICalDateTime.tzidOrNull(): String? = if (this is ICalDateTime.Zoned) tzid else null

private fun localValue(year: Int, month: Int, day: Int, secondOfDay: Int, form: Int, tzid: String?): DateTimeValue {
    checkYear(year)
    return DateTimeValue(year, month, day, secondOfDay / 3600, secondOfDay / 60 % 60, secondOfDay % 60, form, tzid)
}

internal fun utcValue(epochSecond: Long): DateTimeValue {
    val days = floorDiv(epochSecond, SECONDS_PER_DAY)
    val secondOfDay = (epochSecond - days * SECONDS_PER_DAY).toInt()
    val shifted = days + EPOCH_SHIFT
    val era = floorDiv(shifted, DAYS_PER_ERA)
    val dayOfEra = shifted - era * DAYS_PER_ERA
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val shiftedMonth = (5 * dayOfYear + 2) / 153
    val day = (dayOfYear - (153 * shiftedMonth + 2) / 5 + 1).toInt()
    val month = (if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9).toInt()
    val year = yearOfEra + era * 400 + (if (month <= 2) 1 else 0)
    require(year in 0..MAX_YEAR) { "Year out of the iCalendar range 0000..9999" }
    return DateTimeValue(
        year.toInt(),
        month,
        day,
        secondOfDay / 3600,
        secondOfDay / 60 % 60,
        secondOfDay % 60,
        FORM_UTC,
        null,
    )
}

private fun checkYear(year: Int) {
    require(year in 0..MAX_YEAR) { "Year out of the iCalendar range 0000..9999" }
}

private fun floorDiv(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    return if ((value % divisor != 0L) && ((value < 0) != (divisor < 0))) quotient - 1 else quotient
}

private const val EPOCH_SHIFT = 719_468L
private const val DAYS_PER_ERA = 146_097L

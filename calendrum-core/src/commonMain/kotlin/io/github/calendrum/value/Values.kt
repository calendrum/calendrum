package io.github.calendrum.value

internal const val FORM_DATE = 0
internal const val FORM_FLOATING = 1
internal const val FORM_UTC = 2
internal const val FORM_ZONED = 3

internal class DateTimeValue(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val form: Int,
    val tzid: String?,
) {
    val isDate: Boolean get() = form == FORM_DATE

    val isUtc: Boolean get() = form == FORM_UTC

    fun epochDay(): Long = epochDay(year, month, day)

    fun epochSecond(): Long = epochDay() * SECONDS_PER_DAY + hour * 3600L + minute * 60L + second

    internal fun compareLocal(other: DateTimeValue): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month != other.month) return month.compareTo(other.month)
        if (day != other.day) return day.compareTo(other.day)
        if (hour != other.hour) return hour.compareTo(other.hour)
        if (minute != other.minute) return minute.compareTo(other.minute)
        return second.compareTo(other.second)
    }

    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is DateTimeValue && year == other.year && month == other.month && day == other.day &&
                hour == other.hour && minute == other.minute && second == other.second && form == other.form &&
                tzid == other.tzid
            )

    override fun hashCode(): Int {
        var result = ((year * 13 + month) * 32 + day) * 24 + hour
        result = ((result * 60 + minute) * 61 + second) * 4 + form
        return result * 31 + (tzid?.hashCode() ?: 0)
    }

    override fun toString(): String = "DateTimeValue(form=$form)"
}

internal class TimeValue(val hour: Int, val minute: Int, val second: Int, val isUtc: Boolean) {
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is TimeValue && hour == other.hour && minute == other.minute && second == other.second &&
                isUtc == other.isUtc
            )

    override fun hashCode(): Int = ((hour * 60 + minute) * 61 + second) * 2 + (if (isUtc) 1 else 0)

    override fun toString(): String = "TimeValue(utc=$isUtc)"
}

internal const val UNIT_WEEKS = 1
internal const val UNIT_DAYS = 2
internal const val UNIT_HOURS = 4
internal const val UNIT_MINUTES = 8
internal const val UNIT_SECONDS = 16
internal const val TIME_UNITS = UNIT_HOURS or UNIT_MINUTES or UNIT_SECONDS

// `units` are the units that were written; equality includes them, so `P1W` != `P7D` and `PT60M` != `PT1H`.
internal class DurationValue(
    val isNegative: Boolean,
    val weeks: Int,
    val days: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val units: Int,
) {
    fun nominalDays(): Long {
        val days = weeks * 7L + days
        return if (isNegative) -days else days
    }

    fun exactSeconds(): Long {
        val seconds = hours * 3600L + minutes * 60L + seconds
        return if (isNegative) -seconds else seconds
    }

    val isZero: Boolean get() = weeks == 0 && days == 0 && hours == 0 && minutes == 0 && seconds == 0

    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is DurationValue && isNegative == other.isNegative && weeks == other.weeks &&
                days == other.days && hours == other.hours && minutes == other.minutes &&
                seconds == other.seconds && units == other.units
            )

    override fun hashCode(): Int {
        var result = if (isNegative) 1 else 0
        result = result * 31 + weeks
        result = result * 31 + days
        result = result * 31 + hours
        result = result * 31 + minutes
        result = result * 31 + seconds
        return result * 32 + units
    }

    override fun toString(): String = "DurationValue(units=$units)"
}

internal class PeriodValue(val start: DateTimeValue, val end: DateTimeValue?, val duration: DurationValue?) {
    override fun equals(other: Any?): Boolean = this === other ||
        (other is PeriodValue && start == other.start && end == other.end && duration == other.duration)

    override fun hashCode(): Int = (start.hashCode() * 31 + end.hashCode()) * 31 + duration.hashCode()

    override fun toString(): String = "PeriodValue(explicitEnd=${end != null})"
}

internal class BinaryValue(val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean =
        this === other || (other is BinaryValue && bytes.contentEquals(other.bytes))

    override fun hashCode(): Int = bytes.contentHashCode()

    override fun toString(): String = "BinaryValue(size=${bytes.size})"
}

// Keeps the decimal text without a leading `+`: no exponent, no loss when written back, the same on every platform;
// `toDouble()` on demand.
internal class FloatValue(val text: String) {
    fun toDouble(): Double = decimalToDouble(text)

    override fun equals(other: Any?): Boolean = this === other || (other is FloatValue && text == other.text)

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = "FloatValue"
}

internal const val SECONDS_PER_DAY = 86_400L

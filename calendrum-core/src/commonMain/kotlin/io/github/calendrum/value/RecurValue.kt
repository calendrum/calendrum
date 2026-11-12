package io.github.calendrum.value

internal const val FREQ_SECONDLY = 0
internal const val FREQ_MINUTELY = 1
internal const val FREQ_HOURLY = 2
internal const val FREQ_DAILY = 3
internal const val FREQ_WEEKLY = 4
internal const val FREQ_MONTHLY = 5
internal const val FREQ_YEARLY = 6

internal const val SKIP_OMIT = 0
internal const val SKIP_BACKWARD = 1
internal const val SKIP_FORWARD = 2

// Marks a missing `COUNT`, `INTERVAL`, `WKST` or `SKIP`. `extensions` of `RecurValue` holds unknown parts as name,
// value pairs in their order.
internal const val ABSENT = -1

internal const val FREQUENCY_COUNT = 7
internal const val WEEKDAY_COUNT = 7
internal const val SKIP_COUNT = 3

// Same rule as `rulePartName` in `RecurDecoding.kt`: a `when` over literals, no top-level array.
internal fun frequencyName(index: Int): String = when (index) {
    FREQ_SECONDLY -> "SECONDLY"
    FREQ_MINUTELY -> "MINUTELY"
    FREQ_HOURLY -> "HOURLY"
    FREQ_DAILY -> "DAILY"
    FREQ_WEEKLY -> "WEEKLY"
    FREQ_MONTHLY -> "MONTHLY"
    else -> "YEARLY"
}

internal fun weekdayName(index: Int): String = when (index) {
    0 -> "MO"
    1 -> "TU"
    2 -> "WE"
    3 -> "TH"
    4 -> "FR"
    5 -> "SA"
    else -> "SU"
}

internal fun skipName(index: Int): String = when (index) {
    SKIP_OMIT -> "OMIT"
    SKIP_BACKWARD -> "BACKWARD"
    else -> "FORWARD"
}

// `BYDAY` entries pack the ordinal and the weekday (0 = MO … 6 = SU) as `ordinal shl 3 or weekday`; `BYMONTH` entries
// pack the month and the RFC 7529 leap flag as `month shl 1 or leap`.
internal fun weekdayEntry(ordinal: Int, weekday: Int): Int = (ordinal shl 3) or weekday

internal fun weekdayOf(entry: Int): Int = entry and 7

internal fun ordinalOf(entry: Int): Int = entry shr 3

internal fun monthEntry(month: Int, leap: Boolean): Int = (month shl 1) or (if (leap) 1 else 0)

internal fun monthOf(entry: Int): Int = entry shr 1

internal fun isLeapMonth(entry: Int): Boolean = entry and 1 != 0

internal class RecurValue(
    val freq: Int,
    val until: DateTimeValue?,
    val count: Int,
    val interval: Int,
    val bySecond: IntArray?,
    val byMinute: IntArray?,
    val byHour: IntArray?,
    val byDay: IntArray?,
    val byMonthDay: IntArray?,
    val byYearDay: IntArray?,
    val byWeekNo: IntArray?,
    val byMonth: IntArray?,
    val bySetPos: IntArray?,
    val weekStart: Int,
    val rscale: String?,
    val skip: Int,
    val extensions: List<String>?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecurValue) return false
        return freq == other.freq && until == other.until && count == other.count && interval == other.interval &&
            bySecond.contentEquals(other.bySecond) && byMinute.contentEquals(other.byMinute) &&
            byHour.contentEquals(other.byHour) && byDay.contentEquals(other.byDay) &&
            byMonthDay.contentEquals(other.byMonthDay) && byYearDay.contentEquals(other.byYearDay) &&
            byWeekNo.contentEquals(other.byWeekNo) && byMonth.contentEquals(other.byMonth) &&
            bySetPos.contentEquals(other.bySetPos) && weekStart == other.weekStart && rscale == other.rscale &&
            skip == other.skip && extensions == other.extensions
    }

    override fun hashCode(): Int {
        var result = freq
        result = result * 31 + until.hashCode()
        result = result * 31 + count
        result = result * 31 + interval
        result = result * 31 + bySecond.contentHashCode()
        result = result * 31 + byMinute.contentHashCode()
        result = result * 31 + byHour.contentHashCode()
        result = result * 31 + byDay.contentHashCode()
        result = result * 31 + byMonthDay.contentHashCode()
        result = result * 31 + byYearDay.contentHashCode()
        result = result * 31 + byWeekNo.contentHashCode()
        result = result * 31 + byMonth.contentHashCode()
        result = result * 31 + bySetPos.contentHashCode()
        result = result * 31 + weekStart
        result = result * 31 + rscale.hashCode()
        result = result * 31 + skip
        return result * 31 + extensions.hashCode()
    }

    override fun toString(): String = "RecurValue"
}

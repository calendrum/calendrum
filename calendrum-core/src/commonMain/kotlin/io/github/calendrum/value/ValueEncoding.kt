package io.github.calendrum.value

import io.github.calendrum.syntax.escapeText
import io.github.calendrum.syntax.escapeTextList

internal fun encodeBoolean(value: Boolean): String = if (value) "TRUE" else "FALSE"

internal fun encodeInteger(value: Int): String = value.toString()

internal fun encodeFloat(value: FloatValue): String = value.text

internal fun encodeText(value: String): String = escapeText(value)

internal fun encodeTextList(values: List<String>): String = escapeTextList(values)

internal fun encodeBinary(value: BinaryValue): String {
    val bytes = value.bytes
    val out = StringBuilder((bytes.size + 2) / 3 * 4)
    var index = 0
    while (index + 2 < bytes.size) {
        val chunk = (byte(bytes, index) shl 16) or (byte(bytes, index + 1) shl 8) or byte(bytes, index + 2)
        out.append(BASE64[chunk shr 18]).append(BASE64[(chunk shr 12) and 63])
        out.append(BASE64[(chunk shr 6) and 63]).append(BASE64[chunk and 63])
        index += 3
    }
    val rest = bytes.size - index
    if (rest > 0) {
        val chunk = (byte(bytes, index) shl 16) or (if (rest == 2) byte(bytes, index + 1) shl 8 else 0)
        out.append(BASE64[chunk shr 18]).append(BASE64[(chunk shr 12) and 63])
        out.append(if (rest == 2) BASE64[(chunk shr 6) and 63] else '=').append('=')
    }
    return out.toString()
}

private fun byte(bytes: ByteArray, index: Int): Int = bytes[index].toInt() and 0xFF

internal fun encodeDateTime(value: DateTimeValue): String {
    val out = StringBuilder(DATE_TIME_LENGTH)
    appendDateTime(out, value)
    return out.toString()
}

internal fun encodeDateTimeList(values: List<DateTimeValue>): String {
    val out = StringBuilder(values.size * (DATE_TIME_LENGTH + 1))
    for (index in values.indices) {
        if (index > 0) out.append(',')
        appendDateTime(out, values[index])
    }
    return out.toString()
}

internal fun appendDateTime(out: StringBuilder, value: DateTimeValue) {
    appendDigits(out, value.year, 4)
    appendDigits(out, value.month, 2)
    appendDigits(out, value.day, 2)
    if (value.isDate) return
    out.append('T')
    appendClock(out, value.hour, value.minute, value.second, value.isUtc)
}

internal fun encodeTime(value: TimeValue): String {
    val out = StringBuilder(TIME_LENGTH)
    appendClock(out, value.hour, value.minute, value.second, value.isUtc)
    return out.toString()
}

private fun appendClock(out: StringBuilder, hour: Int, minute: Int, second: Int, utc: Boolean) {
    appendDigits(out, hour, 2)
    appendDigits(out, minute, 2)
    appendDigits(out, second, 2)
    if (utc) out.append('Z')
}

internal fun appendDigits(out: StringBuilder, number: Int, width: Int) {
    var divisor = 1
    for (step in 1 until width) divisor *= 10
    var rest = number
    while (divisor > 0) {
        out.append('0' + rest / divisor)
        rest %= divisor
        divisor /= 10
    }
}

internal fun encodeUtcOffset(seconds: Int): String {
    val out = StringBuilder(UTC_OFFSET_LENGTH)
    out.append(if (seconds < 0) '-' else '+')
    val magnitude = if (seconds < 0) -seconds else seconds
    appendDigits(out, magnitude / 3600, 2)
    appendDigits(out, magnitude / 60 % 60, 2)
    if (magnitude % 60 != 0) appendDigits(out, magnitude % 60, 2)
    return out.toString()
}

internal fun encodeDuration(value: DurationValue): String {
    val out = StringBuilder()
    appendDuration(out, value)
    return out.toString()
}

private fun appendDuration(out: StringBuilder, value: DurationValue) {
    if (value.isNegative) out.append('-')
    out.append('P')
    val units = value.units
    if (units and UNIT_WEEKS != 0) out.append(value.weeks).append('W')
    if (units and UNIT_DAYS != 0) out.append(value.days).append('D')
    if (units and TIME_UNITS == 0) return
    out.append('T')
    if (units and UNIT_HOURS != 0) out.append(value.hours).append('H')
    if (units and UNIT_MINUTES != 0) out.append(value.minutes).append('M')
    if (units and UNIT_SECONDS != 0) out.append(value.seconds).append('S')
}

internal fun encodePeriod(value: PeriodValue): String {
    val out = StringBuilder()
    appendPeriod(out, value)
    return out.toString()
}

internal fun encodePeriodList(values: List<PeriodValue>): String {
    val out = StringBuilder()
    for (index in values.indices) {
        if (index > 0) out.append(',')
        appendPeriod(out, values[index])
    }
    return out.toString()
}

private fun appendPeriod(out: StringBuilder, value: PeriodValue) {
    appendDateTime(out, value.start)
    out.append('/')
    val end = value.end
    if (end != null) appendDateTime(out, end) else appendDuration(out, value.duration!!)
}

internal fun encodeRecur(value: RecurValue): String {
    val out = StringBuilder()
    out.append("FREQ=")
    out.append(frequencyName(value.freq))
    val until = value.until
    if (until != null) {
        out.append(";UNTIL=")
        appendDateTime(out, until)
    }
    if (value.count != ABSENT) out.append(";COUNT=").append(value.count)
    if (value.interval != ABSENT) out.append(";INTERVAL=").append(value.interval)
    appendNumbers(out, "BYSECOND", value.bySecond)
    appendNumbers(out, "BYMINUTE", value.byMinute)
    appendNumbers(out, "BYHOUR", value.byHour)
    val byDay = value.byDay
    if (byDay != null) {
        out.append(";BYDAY=")
        for (index in byDay.indices) {
            if (index > 0) out.append(',')
            val ordinal = ordinalOf(byDay[index])
            if (ordinal != 0) out.append(ordinal)
            out.append(weekdayName(weekdayOf(byDay[index])))
        }
    }
    appendNumbers(out, "BYMONTHDAY", value.byMonthDay)
    appendNumbers(out, "BYYEARDAY", value.byYearDay)
    appendNumbers(out, "BYWEEKNO", value.byWeekNo)
    val byMonth = value.byMonth
    if (byMonth != null) {
        out.append(";BYMONTH=")
        for (index in byMonth.indices) {
            if (index > 0) out.append(',')
            out.append(monthOf(byMonth[index]))
            if (isLeapMonth(byMonth[index])) out.append('L')
        }
    }
    appendNumbers(out, "BYSETPOS", value.bySetPos)
    if (value.weekStart != ABSENT) {
        out.append(";WKST=")
        out.append(weekdayName(value.weekStart))
    }
    if (value.rscale != null) out.append(";RSCALE=").append(value.rscale)
    if (value.skip != ABSENT) {
        out.append(";SKIP=")
        out.append(skipName(value.skip))
    }
    val extensions = value.extensions
    if (extensions != null) {
        var index = 0
        while (index < extensions.size) {
            out.append(';').append(extensions[index]).append('=').append(extensions[index + 1])
            index += 2
        }
    }
    return out.toString()
}

private fun appendNumbers(out: StringBuilder, name: String, numbers: IntArray?) {
    if (numbers == null) return
    out.append(';').append(name).append('=')
    for (index in numbers.indices) {
        if (index > 0) out.append(',')
        out.append(numbers[index])
    }
}

private const val BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
private const val DATE_TIME_LENGTH = 16
private const val TIME_LENGTH = 7
private const val UTC_OFFSET_LENGTH = 7

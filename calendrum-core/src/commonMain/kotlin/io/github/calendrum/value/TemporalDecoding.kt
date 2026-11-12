package io.github.calendrum.value

import io.github.calendrum.ProblemCode

// What the value may be: `DATE` (a time is dropped with V007), `DATE-TIME` (a missing time makes a DATE with V008), a
// `PERIOD` bound (a DATE-TIME only), `UNTIL` (DATE or DATE-TIME, no TZID).
internal const val KIND_DATE = 0
internal const val KIND_DATE_TIME = 1
internal const val KIND_PERIOD_BOUND = 2
internal const val KIND_UNTIL = 3

internal fun ValueDecoder.decodeDate(value: CharSequence, from: Int, to: Int): DateTimeValue? =
    decodeTemporal(value, from, to, from, null, KIND_DATE)

internal fun ValueDecoder.decodeDateTime(value: CharSequence, from: Int, to: Int, tzid: String?): DateTimeValue? =
    decodeTemporal(value, from, to, from, tzid, KIND_DATE_TIME)

internal fun ValueDecoder.decodeTemporal(
    value: CharSequence,
    from: Int,
    to: Int,
    base: Int,
    tzid: String?,
    kind: Int,
): DateTimeValue? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, base)) return null
    return parseTemporal(value, start, end, base, tzid, kind)
}

private fun ValueDecoder.parseTemporal(
    value: CharSequence,
    start: Int,
    end: Int,
    base: Int,
    tzid: String?,
    kind: Int,
): DateTimeValue? {
    var index = start
    var extended = false
    val year = yearAt(value, index, end)
    if (year < 0) return invalid(firstNonDigit(value, index, end), base)
    index += 4
    if (index < end && value[index] == '-') {
        if (!accept(ProblemCode.ExtendedIsoForm, index, base)) return null
        extended = true
        index += 1
    }
    val monthIndex = index
    val month = twoDigits(value, index, end)
    if (month < 0) return invalid(firstNonDigit(value, index, end), base)
    index += 2
    if (extended) {
        if (index >= end || value[index] != '-') return invalid(index, base)
        index += 1
    }
    val dayIndex = index
    val day = twoDigits(value, index, end)
    if (day < 0) return invalid(firstNonDigit(value, index, end), base)
    index += 2
    if (month !in 1..12) return outOfRange(monthIndex, base)
    if (day < 1 || day > daysInMonth(year, month)) return outOfRange(dayIndex, base)
    if (index == end) {
        if (kind == KIND_PERIOD_BOUND) return invalid(index, base)
        if (kind == KIND_DATE_TIME && !accept(ProblemCode.DateAsDateTime, index, base)) return null
        return DateTimeValue(year, month, day, 0, 0, 0, FORM_DATE, null)
    }
    val designator = value[index]
    if (designator != 'T' && designator != 't') return invalid(index, base)
    if (kind == KIND_DATE && !accept(ProblemCode.DateWithTime, index, base)) return null
    val time = parseTime(value, index + 1, end, base, extended)
    if (time < 0) return null
    if (kind == KIND_DATE) return DateTimeValue(year, month, day, 0, 0, 0, FORM_DATE, null)
    val utc = timeIsUtc(time)
    if (utc && tzid != null && !accept(ProblemCode.TzidWithUtc, end - 1, base)) return null
    val form = if (utc) {
        FORM_UTC
    } else if (tzid != null) {
        FORM_ZONED
    } else {
        FORM_FLOATING
    }
    val zone = if (form == FORM_ZONED) tzid else null
    val hour = timeHour(time)
    val minute = timeMinute(time)
    if (timeSecond(time) != 60) return DateTimeValue(year, month, day, hour, minute, timeSecond(time), form, zone)
    if (!leapSecondToNextMinute) return DateTimeValue(year, month, day, hour, minute, 59, form, zone)
    return nextMinute(year, month, day, hour, minute, form, zone)
}

// The "next minute" leap second policy: carry through hour, day, month and year; at 9999-12-31 the carry is impossible
// and the second becomes 59.
private fun nextMinute(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
    form: Int,
    zone: String?,
): DateTimeValue {
    var nextMinute = minute + 1
    var nextHour = hour
    var nextDay = day
    var nextMonth = month
    var nextYear = year
    if (nextMinute == 60) {
        nextMinute = 0
        nextHour += 1
    }
    if (nextHour == 24) {
        nextHour = 0
        nextDay += 1
    }
    if (nextDay > daysInMonth(nextYear, nextMonth)) {
        nextDay = 1
        nextMonth += 1
    }
    if (nextMonth == 13) {
        nextMonth = 1
        nextYear += 1
    }
    if (nextYear > MAX_YEAR) return DateTimeValue(year, month, day, hour, minute, 59, form, zone)
    return DateTimeValue(nextYear, nextMonth, nextDay, nextHour, nextMinute, 0, form, zone)
}

internal fun ValueDecoder.decodeTime(value: CharSequence, from: Int, to: Int): TimeValue? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    val time = parseTime(value, start, end, from, false)
    if (time < 0) return null
    val hour = timeHour(time)
    val minute = timeMinute(time)
    val utc = timeIsUtc(time)
    if (timeSecond(time) != 60) return TimeValue(hour, minute, timeSecond(time), utc)
    if (!leapSecondToNextMinute || (hour == 23 && minute == 59)) return TimeValue(hour, minute, 59, utc)
    return if (minute == 59) TimeValue(hour + 1, 0, 0, utc) else TimeValue(hour, minute + 1, 0, utc)
}

// Returns the time packed into one `Int` (`((hour * 64 + minute) * 64 + second) * 2 + utc`) or -1 after a reported
// problem, so that a DATE-TIME allocates only its result.
private fun ValueDecoder.parseTime(value: CharSequence, start: Int, end: Int, base: Int, extendedDate: Boolean): Int {
    var index = start
    var extended = extendedDate
    val hour = twoDigits(value, index, end)
    if (hour < 0) return invalidTime(firstNonDigit(value, index, end), base)
    index += 2
    if (index < end && value[index] == ':') {
        if (!extended && !accept(ProblemCode.ExtendedIsoForm, index, base)) return -1
        extended = true
        index += 1
    } else if (extended) {
        return invalidTime(index, base)
    }
    val minuteIndex = index
    val minute = twoDigits(value, index, end)
    if (minute < 0) return invalidTime(firstNonDigit(value, index, end), base)
    index += 2
    if (extended) {
        if (index >= end || value[index] != ':') return invalidTime(index, base)
        index += 1
    }
    val secondIndex = index
    val second = twoDigits(value, index, end)
    if (second < 0) return invalidTime(firstNonDigit(value, index, end), base)
    index += 2
    if (index < end && (value[index] == '.' || value[index] == ',')) {
        val fractionEnd = digitsEnd(value, index + 1, end)
        if (fractionEnd == index + 1) return invalidTime(index + 1, base)
        if (!accept(ProblemCode.FractionalSeconds, index, base)) return -1
        index = fractionEnd
    }
    var utc = false
    if (index < end && (value[index] == 'Z' || value[index] == 'z')) {
        utc = true
        index += 1
    }
    if (index != end) return invalidTime(index, base)
    if (hour > 23) return timeOutOfRange(start, base)
    if (minute > 59) return timeOutOfRange(minuteIndex, base)
    if (second > 60) return timeOutOfRange(secondIndex, base)
    if (second == 60) report(ProblemCode.LeapSecond, secondIndex, base)
    return ((hour * 64 + minute) * 64 + second) * 2 + (if (utc) 1 else 0)
}

private fun ValueDecoder.invalidTime(index: Int, base: Int): Int {
    invalid(index, base)
    return -1
}

private fun ValueDecoder.timeOutOfRange(index: Int, base: Int): Int {
    outOfRange(index, base)
    return -1
}

private fun timeIsUtc(time: Int): Boolean = time and 1 != 0

private fun timeSecond(time: Int): Int = (time shr 1) and 63

private fun timeMinute(time: Int): Int = (time shr 7) and 63

private fun timeHour(time: Int): Int = time shr 13

private fun yearAt(value: CharSequence, index: Int, to: Int): Int {
    val high = twoDigits(value, index, to)
    if (high < 0) return -1
    val low = twoDigits(value, index + 2, to)
    if (low < 0) return -1
    return high * 100 + low
}

private fun firstNonDigit(value: CharSequence, from: Int, to: Int): Int = digitsEnd(value, from, to)

internal fun ValueDecoder.decodeUtcOffset(value: CharSequence, from: Int, to: Int): Int? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    if (start == end) return invalid(start, from)
    val sign = value[start]
    if (sign != '+' && sign != '-') return invalid(start, from)
    var index = start + 1
    val hours = twoDigits(value, index, end)
    if (hours < 0) return invalid(firstNonDigit(value, index, end), from)
    index += 2
    var colon = false
    if (index < end && value[index] == ':') {
        if (!accept(ProblemCode.UtcOffsetWithColon, index, from)) return null
        colon = true
        index += 1
    }
    val minuteIndex = index
    val minutes = twoDigits(value, index, end)
    if (minutes < 0) return invalid(firstNonDigit(value, index, end), from)
    index += 2
    var seconds = 0
    var secondIndex = index
    if (index < end) {
        if (colon) {
            if (value[index] != ':') return invalid(index, from)
            index += 1
            secondIndex = index
        }
        seconds = twoDigits(value, index, end)
        if (seconds < 0) return invalid(firstNonDigit(value, index, end), from)
        index += 2
        if (index != end) return invalid(index, from)
    }
    if (hours > 23) return outOfRange(start + 1, from)
    if (minutes > 59) return outOfRange(minuteIndex, from)
    if (seconds > 59) return outOfRange(secondIndex, from)
    val total = hours * 3600 + minutes * 60 + seconds
    if (sign == '+') return total
    if (total == 0 && !accept(ProblemCode.NegativeZeroOffset, start, from)) return null
    return -total
}

internal fun ValueDecoder.decodeDateList(value: CharSequence, from: Int, to: Int): List<DateTimeValue>? =
    decodeTemporalList(value, from, to, null, KIND_DATE)

internal fun ValueDecoder.decodeDateTimeList(
    value: CharSequence,
    from: Int,
    to: Int,
    tzid: String?,
): List<DateTimeValue>? = decodeTemporalList(value, from, to, tzid, KIND_DATE_TIME)

// Lenient mode skips a bad item after its Error and keeps the others; strict mode rejects the list.
private fun ValueDecoder.decodeTemporalList(
    value: CharSequence,
    from: Int,
    to: Int,
    tzid: String?,
    kind: Int,
): List<DateTimeValue>? {
    val items = ArrayList<DateTimeValue>()
    var index = from
    while (true) {
        val end = listItemEnd(value, index, to)
        if (blankStart(value, index, end) == end) {
            if (!accept(ProblemCode.EmptyItem, index, from)) return null
        } else {
            val item = decodeTemporal(value, index, end, from, tzid, kind)
            if (item != null) {
                items.add(item)
            } else if (isStrict) {
                return null
            }
        }
        if (end == to) return items
        index = end + 1
    }
}

internal fun listItemEnd(value: CharSequence, from: Int, to: Int): Int {
    var index = from
    while (index < to && value[index] != ',') index += 1
    return index
}

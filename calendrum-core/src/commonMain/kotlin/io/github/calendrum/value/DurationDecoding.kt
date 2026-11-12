package io.github.calendrum.value

import io.github.calendrum.ProblemCode

internal fun ValueDecoder.decodeDuration(value: CharSequence, from: Int, to: Int): DurationValue? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    return parseDuration(value, start, end, from)
}

// Units are a bit mask in RFC order (W, D, H, M, S); a time unit must have a higher bit than every time unit before it.
// Time units before `T` switch to the time part once (V009); seconds after hours without minutes add zero minutes
// (V010), so that the written form is valid RFC.
private fun ValueDecoder.parseDuration(value: CharSequence, start: Int, end: Int, base: Int): DurationValue? {
    var index = start
    val negative = index < end && value[index] == '-'
    if (index < end && (negative || value[index] == '+')) index += 1
    if (index >= end || upper(value[index]) != 'P') return invalid(index, base)
    index += 1
    if (index == end) return invalid(index, base)
    var units = 0
    var inTime = false
    var weeks = 0
    var days = 0
    var hours = 0
    var minutes = 0
    var seconds = 0
    while (index < end) {
        val char = upper(value[index])
        if (char == 'T') {
            if (inTime || units and UNIT_WEEKS != 0 || index + 1 == end) return invalid(index, base)
            inTime = true
            index += 1
            continue
        }
        val numberEnd = digitsEnd(value, index, end)
        if (numberEnd == index || numberEnd == end) return invalid(numberEnd, base)
        val number = naturalNumber(value, index, numberEnd)
        if (number < 0) return outOfRange(index, base)
        val unitIndex = numberEnd
        val unit = upper(value[unitIndex])
        when (unit) {
            'W' -> {
                if (inTime || units != 0) return invalid(unitIndex, base)
                weeks = number
                units = UNIT_WEEKS
            }

            'D' -> {
                if (inTime || units != 0) return invalid(unitIndex, base)
                days = number
                units = UNIT_DAYS
            }

            'H', 'M', 'S' -> {
                if (units and UNIT_WEEKS != 0) return invalid(unitIndex, base)
                if (!inTime) {
                    if (!accept(ProblemCode.DurationWithoutTimeDesignator, unitIndex, base)) return null
                    inTime = true
                }
                val bit = if (unit == 'H') {
                    UNIT_HOURS
                } else if (unit == 'M') {
                    UNIT_MINUTES
                } else {
                    UNIT_SECONDS
                }
                if (units and TIME_UNITS >= bit) return invalid(unitIndex, base)
                if (bit == UNIT_SECONDS && units and UNIT_HOURS != 0 && units and UNIT_MINUTES == 0) {
                    if (!accept(ProblemCode.DurationSkipsMinutes, unitIndex, base)) return null
                    units = units or UNIT_MINUTES
                }
                units = units or bit
                if (bit ==
                    UNIT_HOURS
                ) {
                    hours = number
                } else if (bit == UNIT_MINUTES) {
                    minutes = number
                } else {
                    seconds = number
                }
            }

            else -> return invalid(unitIndex, base)
        }
        index = unitIndex + 1
    }
    return DurationValue(negative, weeks, days, hours, minutes, seconds, units)
}

internal fun ValueDecoder.decodePeriod(value: CharSequence, from: Int, to: Int, tzid: String?): PeriodValue? =
    parsePeriod(value, from, to, from, tzid)

// The order check compares local fields only when both bounds have the same form; mixed forms need a time zone and are
// checked at the property level.
private fun ValueDecoder.parsePeriod(value: CharSequence, from: Int, to: Int, base: Int, tzid: String?): PeriodValue? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, base)) return null
    var slash = start
    while (slash < end && value[slash] != '/') slash += 1
    if (slash == end) return invalid(end, base)
    val periodStart = decodeTemporal(value, start, slash, base, tzid, KIND_PERIOD_BOUND) ?: return null
    val endStart = slash + 1
    val first = if (endStart < end) upper(value[endStart]) else ' '
    if (first == 'P' || first == '+' || first == '-') {
        val duration = parseDuration(value, endStart, end, base) ?: return null
        if ((duration.isNegative || duration.isZero) && !accept(ProblemCode.PeriodNotPositive, endStart, base)) {
            return null
        }
        return PeriodValue(periodStart, null, duration)
    }
    val periodEnd = decodeTemporal(value, endStart, end, base, tzid, KIND_PERIOD_BOUND) ?: return null
    if (periodEnd.form == periodStart.form &&
        periodEnd.compareLocal(periodStart) <= 0 &&
        !accept(ProblemCode.PeriodNotPositive, endStart, base)
    ) {
        return null
    }
    return PeriodValue(periodStart, periodEnd, null)
}

// Lenient mode skips a bad item after its Error and keeps the others; strict mode rejects the list.
internal fun ValueDecoder.decodePeriodList(
    value: CharSequence,
    from: Int,
    to: Int,
    tzid: String?,
): List<PeriodValue>? {
    val items = ArrayList<PeriodValue>()
    var index = from
    while (true) {
        val end = listItemEnd(value, index, to)
        if (blankStart(value, index, end) == end) {
            if (!accept(ProblemCode.EmptyItem, index, from)) return null
        } else {
            val item = parsePeriod(value, index, end, from, tzid)
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

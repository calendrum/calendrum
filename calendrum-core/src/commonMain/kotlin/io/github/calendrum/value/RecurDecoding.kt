package io.github.calendrum.value

import io.github.calendrum.ProblemCode

// Names come from `when` over string literals, not from top-level arrays: a top-level `val` with an initializer makes
// Kotlin/Native register a thread-local record in the module initializer that dead code elimination keeps (+40 bytes of
// iOS code in `size-budget` even when the package is unreachable), and a lookup in one packed string scanned with
// `indexOf` slowed `RECUR` decoding down by up to 20 % on Kotlin/Native. `indexOfName` is inline, so the name functions
// are inlined as well.
internal fun rulePartName(index: Int): String = when (index) {
    PART_FREQ -> "FREQ"
    PART_UNTIL -> "UNTIL"
    PART_COUNT -> "COUNT"
    PART_INTERVAL -> "INTERVAL"
    PART_BYSECOND -> "BYSECOND"
    PART_BYMINUTE -> "BYMINUTE"
    PART_BYHOUR -> "BYHOUR"
    PART_BYDAY -> "BYDAY"
    PART_BYMONTHDAY -> "BYMONTHDAY"
    PART_BYYEARDAY -> "BYYEARDAY"
    PART_BYWEEKNO -> "BYWEEKNO"
    PART_BYMONTH -> "BYMONTH"
    PART_BYSETPOS -> "BYSETPOS"
    PART_WKST -> "WKST"
    PART_RSCALE -> "RSCALE"
    else -> "SKIP"
}

// Indexes of the rule parts (`rulePartName`); `seen` has one bit per part. `BY_PARTS` covers `BYSECOND`…`BYSETPOS`.
private const val PART_FREQ = 0
private const val PART_UNTIL = 1
private const val PART_COUNT = 2
private const val PART_INTERVAL = 3
private const val PART_BYSECOND = 4
private const val PART_BYMINUTE = 5
private const val PART_BYHOUR = 6
private const val PART_BYDAY = 7
private const val PART_BYMONTHDAY = 8
private const val PART_BYYEARDAY = 9
private const val PART_BYWEEKNO = 10
private const val PART_BYMONTH = 11
private const val PART_BYSETPOS = 12
private const val PART_WKST = 13
private const val PART_RSCALE = 14
private const val PART_SKIP = 15
private const val PART_COUNT_ALL = 16

private const val BY_PARTS = 0x1FF0
private const val GREGORIAN_MONTHS = 12
private const val MAX_MONTHS = 13

// Rule parts are read in any order and without regard to case. A repeated part keeps the last value. Checks that need
// the whole rule (no FREQ, month 13 without RSCALE, COUNT with UNTIL, parts not allowed for the frequency) run after
// the loop; their offsets come from `partStart`, which scans the rule again only on that rare path.
internal fun ValueDecoder.decodeRecur(value: CharSequence, from: Int, to: Int): RecurValue? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    if (start == end) {
        report(ProblemCode.RecurWithoutFreq, start, from)
        return null
    }
    var seen = 0
    var freq = ABSENT
    var until: DateTimeValue? = null
    var count = ABSENT
    var interval = ABSENT
    var bySecond: IntArray? = null
    var byMinute: IntArray? = null
    var byHour: IntArray? = null
    var byDay: IntArray? = null
    var byMonthDay: IntArray? = null
    var byYearDay: IntArray? = null
    var byWeekNo: IntArray? = null
    var byMonth: IntArray? = null
    var bySetPos: IntArray? = null
    var weekStart = ABSENT
    var rscale: String? = null
    var skip = ABSENT
    var extensions: ArrayList<String>? = null
    var index = start
    while (true) {
        val partEnd = indexOf(value, ';', index, end)
        if (partEnd == index) {
            if (!accept(ProblemCode.EmptyItem, index, from)) return null
        } else {
            val equals = indexOf(value, '=', index, partEnd)
            if (equals == partEnd) return invalid(partEnd, from)
            val part = indexOfName(value, index, equals, PART_COUNT_ALL, ::rulePartName)
            val valueStart = equals + 1
            if (part < 0) {
                if (!isToken(value, index, equals)) return invalid(index, from)
                report(ProblemCode.UnknownRulePart, index, from)
                val list = extensions ?: ArrayList<String>().also { extensions = it }
                list.add(slice(value, index, equals).uppercase())
                val extensionStart = blankStart(value, valueStart, partEnd)
                val extensionEnd = blankEnd(value, extensionStart, partEnd)
                if (!acceptBlanks(valueStart, partEnd, extensionStart, extensionEnd, from)) return null
                list.add(slice(value, extensionStart, extensionEnd))
            } else {
                if (seen and (1 shl part) != 0 && !accept(ProblemCode.RepeatedRulePart, index, from)) {
                    return null
                }
                seen = seen or (1 shl part)
                when (part) {
                    PART_FREQ -> {
                        freq = indexOfName(value, valueStart, partEnd, FREQUENCY_COUNT, ::frequencyName)
                        if (freq < 0) return invalid(valueStart, from)
                    }

                    PART_UNTIL ->
                        until =
                            decodeTemporal(value, valueStart, partEnd, from, null, KIND_UNTIL) ?: return null

                    PART_COUNT -> {
                        count = ruleNumber(value, valueStart, partEnd, from, 0)
                        if (count < 0) return null
                    }

                    PART_INTERVAL -> {
                        interval = ruleNumber(value, valueStart, partEnd, from, 1)
                        if (interval < 0) return null
                    }

                    PART_BYSECOND ->
                        bySecond =
                            numberList(value, valueStart, partEnd, from, 0, 60, false) ?: return null

                    PART_BYMINUTE ->
                        byMinute =
                            numberList(value, valueStart, partEnd, from, 0, 59, false) ?: return null

                    PART_BYHOUR -> byHour = numberList(value, valueStart, partEnd, from, 0, 23, false) ?: return null

                    PART_BYDAY -> byDay = weekdayList(value, valueStart, partEnd, from) ?: return null

                    PART_BYMONTHDAY ->
                        byMonthDay =
                            numberList(value, valueStart, partEnd, from, 1, 31, true) ?: return null

                    PART_BYYEARDAY ->
                        byYearDay =
                            numberList(value, valueStart, partEnd, from, 1, 366, true) ?: return null

                    PART_BYWEEKNO -> byWeekNo = numberList(value, valueStart, partEnd, from, 1, 53, true) ?: return null

                    PART_BYMONTH ->
                        byMonth =
                            numberList(value, valueStart, partEnd, from, 1, MAX_MONTHS, false) ?: return null

                    PART_BYSETPOS ->
                        bySetPos =
                            numberList(value, valueStart, partEnd, from, 1, 366, true) ?: return null

                    PART_WKST -> {
                        weekStart = indexOfName(value, valueStart, partEnd, WEEKDAY_COUNT, ::weekdayName)
                        if (weekStart < 0) return invalid(valueStart, from)
                    }

                    PART_RSCALE -> {
                        if (!isToken(value, valueStart, partEnd)) return invalid(valueStart, from)
                        rscale = slice(value, valueStart, partEnd).uppercase()
                    }

                    else -> {
                        skip = indexOfName(value, valueStart, partEnd, SKIP_COUNT, ::skipName)
                        if (skip < 0) return invalid(valueStart, from)
                    }
                }
            }
        }
        if (partEnd == end) break
        index = partEnd + 1
    }
    if (freq < 0) {
        report(ProblemCode.RecurWithoutFreq, start, from)
        return null
    }
    if (byMonth != null && rscale == null) {
        for (entry in byMonth) {
            if (monthOf(entry) > GREGORIAN_MONTHS) return outOfRange(partStart(value, start, end, PART_BYMONTH), from)
        }
    }
    if (count >= 0 && until != null) {
        val second = maxOf(partStart(value, start, end, PART_COUNT), partStart(value, start, end, PART_UNTIL))
        if (!accept(ProblemCode.CountAndUntil, second, from)) return null
    }
    val notAllowed = notAllowedPart(freq, seen, byDay, byMonth, rscale)
    if (notAllowed >= 0 &&
        !accept(ProblemCode.RulePartNotAllowed, partStart(value, start, end, notAllowed), from)
    ) {
        return null
    }
    return RecurValue(
        freq,
        until,
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
        rscale,
        skip,
        extensions,
    )
}

private fun notAllowedPart(freq: Int, seen: Int, byDay: IntArray?, byMonth: IntArray?, rscale: String?): Int {
    if (seen and (1 shl PART_BYWEEKNO) != 0 && freq != FREQ_YEARLY) return PART_BYWEEKNO
    if (seen and (1 shl PART_BYYEARDAY) != 0 && freq in FREQ_DAILY..FREQ_MONTHLY) return PART_BYYEARDAY
    if (seen and (1 shl PART_BYMONTHDAY) != 0 && freq == FREQ_WEEKLY) return PART_BYMONTHDAY
    if (byDay != null) {
        val ordinalAllowed = freq == FREQ_MONTHLY || (freq == FREQ_YEARLY && seen and (1 shl PART_BYWEEKNO) == 0)
        if (!ordinalAllowed) {
            for (entry in byDay) {
                if (ordinalOf(entry) != 0) return PART_BYDAY
            }
        }
    }
    if (seen and (1 shl PART_BYSETPOS) != 0 && seen and BY_PARTS and (1 shl PART_BYSETPOS).inv() == 0) {
        return PART_BYSETPOS
    }
    if (rscale == null) {
        if (seen and (1 shl PART_SKIP) != 0) return PART_SKIP
        if (byMonth != null) {
            for (entry in byMonth) {
                if (isLeapMonth(entry)) return PART_BYMONTH
            }
        }
    }
    return -1
}

private fun partStart(value: CharSequence, start: Int, end: Int, part: Int): Int {
    var found = start
    var index = start
    while (index < end) {
        val partEnd = indexOf(value, ';', index, end)
        val equals = indexOf(value, '=', index, partEnd)
        if (indexOfName(value, index, equals, PART_COUNT_ALL, ::rulePartName) == part) found = index
        index = partEnd + 1
    }
    return found
}

private fun ValueDecoder.ruleNumber(value: CharSequence, from: Int, to: Int, base: Int, min: Int): Int {
    if (from == to || digitsEnd(value, from, to) != to) {
        invalid(digitsEnd(value, from, to), base)
        return -1
    }
    val number = naturalNumber(value, from, to)
    if (number < min) {
        outOfRange(from, base)
        return -1
    }
    return number
}

private fun ValueDecoder.numberList(
    value: CharSequence,
    from: Int,
    to: Int,
    base: Int,
    min: Int,
    max: Int,
    signed: Boolean,
): IntArray? {
    val size = countItems(value, from, to, base)
    if (size <= 0) return if (size == 0) invalid(from, base) else null
    val result = IntArray(size)
    var out = 0
    var index = from
    while (index <= to) {
        val end = listItemEnd(value, index, to)
        if (end > index) {
            var digits = index
            val negative = signed && value[digits] == '-'
            if (negative || (signed && value[digits] == '+')) digits += 1
            val digitsEnd = digitsEnd(value, digits, end)
            val leap = !signed && max == MAX_MONTHS && digitsEnd + 1 == end && upper(value[digitsEnd]) == 'L'
            if (digitsEnd == digits || (digitsEnd != end && !leap)) return invalid(digitsEnd, base)
            val number = naturalNumber(value, digits, digitsEnd)
            if (number < min || number > max) return outOfRange(index, base)
            result[out] = if (max == MAX_MONTHS) {
                monthEntry(number, leap)
            } else if (negative) {
                -number
            } else {
                number
            }
            out += 1
        }
        index = end + 1
    }
    return result
}

private fun ValueDecoder.weekdayList(value: CharSequence, from: Int, to: Int, base: Int): IntArray? {
    val size = countItems(value, from, to, base)
    if (size <= 0) return if (size == 0) invalid(from, base) else null
    val result = IntArray(size)
    var out = 0
    var index = from
    while (index <= to) {
        val end = listItemEnd(value, index, to)
        if (end > index) {
            var digits = index
            val negative = value[digits] == '-'
            val sign = negative || value[digits] == '+'
            if (sign) digits += 1
            val digitsEnd = digitsEnd(value, digits, end)
            if (sign && digitsEnd == digits) return invalid(digits, base)
            val weekday = indexOfName(value, digitsEnd, end, WEEKDAY_COUNT, ::weekdayName)
            if (weekday < 0) return invalid(digitsEnd, base)
            var ordinal = 0
            if (digitsEnd > digits) {
                ordinal = naturalNumber(value, digits, digitsEnd)
                if (ordinal < 1 || ordinal > 53) return outOfRange(index, base)
            }
            result[out] = weekdayEntry(if (negative) -ordinal else ordinal, weekday)
            out += 1
        }
        index = end + 1
    }
    return result
}

// Reports empty items and returns the number of non-empty ones (or -1 when strict mode rejects an empty item), so the
// result array has its exact size.
private fun ValueDecoder.countItems(value: CharSequence, from: Int, to: Int, base: Int): Int {
    var count = 0
    var index = from
    while (index <= to) {
        val end = listItemEnd(value, index, to)
        if (end > index) {
            count += 1
        } else if (from < to && !accept(ProblemCode.EmptyItem, index, base)) {
            return -1
        }
        index = end + 1
    }
    return count
}

private fun indexOf(value: CharSequence, char: Char, from: Int, to: Int): Int {
    var index = from
    while (index < to && value[index] != char) index += 1
    return index
}

private fun isToken(value: CharSequence, from: Int, to: Int): Boolean {
    if (from == to) return false
    for (index in from until to) {
        val char = value[index]
        if (!(char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9' || char == '-')) return false
    }
    return true
}

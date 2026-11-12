package io.github.calendrum.anonymize

// Strict DATE (YYYYMMDD) and DATE-TIME (…THHMMSS[Z], second 60 allowed) with upper-case T and Z; the date moves by
// whole weeks through days since 1970 (the civil calendar algorithms of H. Hinnant), the time stays. A year outside
// 0000..9999 leaves the value unchanged. `list` handles comma-separated values and periods (the end of a period is
// shifted, a duration is not); `rule` shifts only UNTIL.
internal class DateShifter(weeks: Int) {
    private val days = weeks.toLong() * DAYS_PER_WEEK

    fun list(bytes: ByteArray, from: Int, to: Int): Int {
        var unrecognized = 0
        var start = from
        for (index in from..to) {
            if (index < to && bytes[index] != COMMA) continue
            unrecognized += item(bytes, start, index)
            start = index + 1
        }
        return unrecognized
    }

    fun rule(bytes: ByteArray, from: Int, to: Int): Int {
        var unrecognized = 0
        var start = from
        for (index in from..to) {
            if (index < to && bytes[index] != SEMICOLON) continue
            val equals = indexOf(bytes, start, index, EQUALS)
            if (equals >= 0 && equalsIgnoreCase(bytes, start, equals, UNTIL) && !token(bytes, equals + 1, index)) {
                unrecognized++
            }
            start = index + 1
        }
        return unrecognized
    }

    fun token(bytes: ByteArray, from: Int, to: Int): Boolean {
        val length = to - from
        if (length != DATE_LENGTH && length != DATE_TIME_LENGTH && length != UTC_LENGTH) return false
        val year = number(bytes, from, 4)
        val month = number(bytes, from + 4, 2)
        val day = number(bytes, from + 6, 2)
        if (year < 0 || month !in 1..MONTHS || day < 1 || day > daysInMonth(year, month)) return false
        if (length > DATE_LENGTH) {
            if (bytes[from + DATE_LENGTH] != UPPER_T) return false
            val hour = number(bytes, from + DATE_LENGTH + 1, 2)
            val minute = number(bytes, from + DATE_LENGTH + 3, 2)
            val second = number(bytes, from + DATE_LENGTH + 5, 2)
            if (hour !in 0..MAX_HOUR || minute !in 0..MAX_MINUTE || second !in 0..MAX_SECOND) return false
            if (length == UTC_LENGTH && bytes[to - 1] != UPPER_Z) return false
        }
        val civil = civil(epochDay(year, month, day) + days)
        if (civil < 0) return false
        put(bytes, from, 4, (civil ushr 9).toInt())
        put(bytes, from + 4, 2, (civil ushr 5).toInt() and 0xF)
        put(bytes, from + 6, 2, civil.toInt() and 0x1F)
        return true
    }

    private fun item(bytes: ByteArray, from: Int, to: Int): Int {
        val slash = indexOf(bytes, from, to, SLASH)
        if (slash < 0) return if (token(bytes, from, to)) 0 else 1
        var unrecognized = if (token(bytes, from, slash)) 0 else 1
        val end = slash + 1
        if (end < to && bytes[end] != UPPER_P && bytes[end] != PLUS && bytes[end] != MINUS && !token(bytes, end, to)) {
            unrecognized++
        }
        return unrecognized
    }

    private fun number(bytes: ByteArray, from: Int, digits: Int): Int {
        var value = 0
        for (index in from until from + digits) {
            val digit = bytes[index] - DIGIT_ZERO
            if (digit !in 0..9) return -1
            value = value * 10 + digit
        }
        return value
    }

    private fun put(bytes: ByteArray, from: Int, digits: Int, value: Int) {
        var rest = value
        for (index in from + digits - 1 downTo from) {
            bytes[index] = (DIGIT_ZERO + rest % 10).toByte()
            rest /= 10
        }
    }

    private companion object {
        private const val DAYS_PER_WEEK = 7L
        private const val DATE_LENGTH = 8
        private const val DATE_TIME_LENGTH = 15
        private const val UTC_LENGTH = 16
        private const val MONTHS = 12
        private const val MAX_HOUR = 23
        private const val MAX_MINUTE = 59
        private const val MAX_SECOND = 60
        private const val DIGIT_ZERO = 0x30
        private const val COMMA: Byte = 0x2C
        private const val PLUS: Byte = 0x2B
        private const val MINUS: Byte = 0x2D
        private const val SLASH: Byte = 0x2F
        private const val SEMICOLON: Byte = 0x3B
        private const val EQUALS: Byte = 0x3D
        private const val UPPER_P: Byte = 0x50
        private const val UPPER_T: Byte = 0x54
        private const val UPPER_Z: Byte = 0x5A
        private const val UNTIL = "UNTIL"
    }
}

internal fun ruleMeaningMayChange(bytes: ByteArray, from: Int, to: Int): Boolean {
    var start = from
    for (index in from..to) {
        if (index < to && bytes[index] != RULE_SEPARATOR) continue
        val equals = indexOf(bytes, start, index, RULE_EQUALS)
        if (equals >= 0) {
            for (part in CALENDAR_PARTS) {
                if (equalsIgnoreCase(bytes, start, equals, part)) return true
            }
            if (equalsIgnoreCase(bytes, start, equals, "FREQ")) {
                if (equalsIgnoreCase(bytes, equals + 1, index, "MONTHLY")) return true
                if (equalsIgnoreCase(bytes, equals + 1, index, "YEARLY")) return true
            }
        }
        start = index + 1
    }
    return false
}

internal fun indexOf(bytes: ByteArray, from: Int, to: Int, byte: Byte): Int {
    for (index in from until to) {
        if (bytes[index] == byte) return index
    }
    return -1
}

internal fun equalsIgnoreCase(bytes: ByteArray, from: Int, to: Int, ascii: String): Boolean {
    if (to - from != ascii.length) return false
    for (offset in ascii.indices) {
        var byte = bytes[from + offset].toInt()
        if (byte in LOWER_A..LOWER_Z) byte -= CASE_OFFSET
        if (byte != ascii[offset].code) return false
    }
    return true
}

private fun daysInMonth(year: Int, month: Int): Int = when (month) {
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    4, 6, 9, 11 -> 30
    else -> 31
}

private fun epochDay(year: Int, month: Int, day: Int): Long {
    val y = (if (month <= 2) year - 1 else year).toLong()
    val era = y.floorDiv(DAYS_ERA_YEARS)
    val yearOfEra = y - era * DAYS_ERA_YEARS
    val shiftedMonth = if (month > 2) month - 3 else month + 9
    val dayOfYear = (153 * shiftedMonth + 2) / 5 + day - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    return era * DAYS_PER_ERA + dayOfEra - EPOCH_SHIFT
}

private fun civil(epochDay: Long): Long {
    val z = epochDay + EPOCH_SHIFT
    val era = z.floorDiv(DAYS_PER_ERA)
    val dayOfEra = z - era * DAYS_PER_ERA
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val shiftedMonth = (5 * dayOfYear + 2) / 153
    val day = dayOfYear - (153 * shiftedMonth + 2) / 5 + 1
    val month = if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9
    val year = yearOfEra + era * DAYS_ERA_YEARS + if (month <= 2) 1 else 0
    if (year !in 0..MAX_YEAR) return -1L
    return (year shl 9) or (month shl 5) or day
}

private const val DAYS_ERA_YEARS = 400L
private const val MAX_YEAR = 9999L
private const val DAYS_PER_ERA = 146097L
private const val EPOCH_SHIFT = 719468L
private const val LOWER_A = 0x61
private const val LOWER_Z = 0x7A
private const val CASE_OFFSET = 0x20
private const val RULE_SEPARATOR: Byte = 0x3B
private const val RULE_EQUALS: Byte = 0x3D
private val CALENDAR_PARTS = arrayOf("BYMONTH", "BYMONTHDAY", "BYYEARDAY", "BYWEEKNO", "BYSETPOS")

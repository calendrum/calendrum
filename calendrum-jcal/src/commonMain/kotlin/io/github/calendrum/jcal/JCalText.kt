package io.github.calendrum.jcal

import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonUnquotedLiteral

internal fun asciiLower(text: String): String {
    var index = 0
    while (index < text.length && text[index] !in 'A'..'Z') index++
    if (index == text.length) return text
    val out = StringBuilder(text.length).append(text, 0, index)
    while (index < text.length) {
        val char = text[index++]
        out.append(if (char in 'A'..'Z') char + 32 else char)
    }
    return out.toString()
}

internal fun asciiUpper(text: String): String {
    var index = 0
    while (index < text.length && text[index] !in 'a'..'z') index++
    if (index == text.length) return text
    val out = StringBuilder(text.length).append(text, 0, index)
    while (index < text.length) {
        val char = text[index++]
        out.append(if (char in 'a'..'z') char - 32 else char)
    }
    return out.toString()
}

internal fun jcalType(icalType: String): String? = when (val upper = asciiUpper(icalType)) {
    "BINARY", "BOOLEAN", "CAL-ADDRESS", "DATE", "DATE-TIME", "DURATION", "FLOAT", "INTEGER", "PERIOD", "RECUR", "TEXT",
    "TIME", "URI", "UTC-OFFSET", "UID", "XML-REFERENCE",
    -> asciiLower(upper)

    else -> null
}

// From the ISO text of the value, without kotlinx-datetime objects; jCal has whole seconds.
internal fun jcalDateTime(value: ICalDateTime): String = when (value) {
    is ICalDateTime.Date -> value.toString()
    is ICalDateTime.Floating -> local(value.toString())
    is ICalDateTime.Zoned -> local(value.toString().substringBefore('['))
    is ICalDateTime.Utc -> utc(value.instant.epochSeconds)
}

private fun local(iso: String): String = if (iso.length >= 19) iso.substring(0, 19) else iso.substring(0, 16) + ":00"

internal fun jcalPeriod(value: ICalPeriod): String {
    val end = value.end
    return jcalDateTime(value.start) + "/" + (if (end != null) jcalDateTime(end) else value.duration.toString())
}

internal fun jcalOffset(totalSeconds: Int): String {
    val sign = if (totalSeconds < 0) "-" else "+"
    val magnitude = if (totalSeconds < 0) -totalSeconds else totalSeconds
    val seconds = magnitude % 60
    return sign + two(magnitude / 3600) + ":" + two(magnitude / 60 % 60) +
        (if (seconds == 0) "" else ":" + two(seconds))
}

private fun utc(epochSecond: Long): String {
    val days = floorDiv(epochSecond, SECONDS_PER_DAY)
    val secondOfDay = (epochSecond - days * SECONDS_PER_DAY).toInt()
    val shifted = days + 719_468L
    val era = floorDiv(shifted, 146_097L)
    val dayOfEra = shifted - era * 146_097L
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val shiftedMonth = (5 * dayOfYear + 2) / 153
    val date = (dayOfYear - (153 * shiftedMonth + 2) / 5 + 1).toInt()
    val month = (if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9).toInt()
    val year = (yearOfEra + era * 400 + (if (month <= 2) 1 else 0)).toInt()
    return day(year, month, date) + 'T' + clock(secondOfDay) + 'Z'
}

private fun floorDiv(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    return if (value % divisor != 0L && (value < 0) != (divisor < 0)) quotient - 1 else quotient
}

private fun day(year: Int, month: Int, day: Int): String {
    val out = StringBuilder(10)
    if (year < 0) out.append('-')
    val magnitude = if (year < 0) -year else year
    val digits = magnitude.toString()
    repeat(4 - digits.length) { out.append('0') }
    return out.append(digits).append('-').append(two(month)).append('-').append(two(day)).toString()
}

private fun clock(secondOfDay: Int): String =
    two(secondOfDay / 3600) + ":" + two(secondOfDay / 60 % 60) + ":" + two(secondOfDay % 60)

private fun two(value: Int): String = if (value < 10) "0$value" else value.toString()

// A FLOAT keeps its digits as an unquoted JSON literal (no rounding through Double); the plus sign and leading zeros,
// which JSON numbers do not allow, are dropped.
@OptIn(ExperimentalSerializationApi::class)
internal fun jsonDecimal(text: String, from: Int, to: Int): JsonPrimitive? {
    var start = from
    var end = to
    while (start < end && text[start] == ' ') start++
    while (end > start && text[end - 1] == ' ') end--
    if (start == end) return null
    val negative = text[start] == '-'
    if (text[start] == '+' || negative) start++
    var integerEnd = start
    while (integerEnd < end && text[integerEnd] in '0'..'9') integerEnd++
    if (integerEnd == start) return null
    var fractionEnd = integerEnd
    if (fractionEnd < end && text[fractionEnd] == '.') {
        fractionEnd++
        val digits = fractionEnd
        while (fractionEnd < end && text[fractionEnd] in '0'..'9') fractionEnd++
        if (fractionEnd == digits) return null
    }
    if (fractionEnd != end) return null
    // A reader of JSON keeps a number as a double: more digits than that holds would change in silence, and Python refuses a
    // long integer outright. Such a value stays in the file as text (`unknown`), as an integer above 32 bits does.
    if (significantDigits(text, start, fractionEnd) > MAX_DECIMAL_DIGITS) return null
    // Below the smallest normal double the digits left are fewer, and far enough below a reader gets 0: `0.` with 400 zeros
    // and a 1 has one significant digit. Only the fraction can be that small, as a long integer has too many digits.
    // A value that is not zero is checked by its digits, as far enough below the double is 0 itself.
    val zero = (start until fractionEnd).all { text[it] == '0' || text[it] == '.' }
    if (!zero && text.substring(start, fractionEnd).toDouble() < MIN_NORMAL_DOUBLE) return null
    var first = start
    while (first < integerEnd - 1 && text[first] == '0') first++
    val out = StringBuilder(end - first + 1)
    if (negative) out.append('-')
    out.append(text, first, end)
    return JsonUnquotedLiteral(out.toString())
}

internal fun parseInteger(text: String, from: Int, to: Int): Int? {
    var index = from
    if (index == to) return null
    val negative = text[index] == '-'
    if (negative || text[index] == '+') index++
    if (index == to) return null
    var value = 0L
    while (index < to) {
        val char = text[index++]
        if (char !in '0'..'9') return null
        value = value * 10 + (char - '0')
        if (value > Int.MAX_VALUE.toLong() + 1) return null
    }
    val signed = if (negative) -value else value
    return if (signed in Int.MIN_VALUE..Int.MAX_VALUE) signed.toInt() else null
}

internal fun icalTimeToJcal(text: String, from: Int, to: Int): String? {
    val utc = to - from == 7 && (text[to - 1] == 'Z' || text[to - 1] == 'z')
    if (to - from != 6 && !utc) return null
    for (index in from until from + 6) if (text[index] !in '0'..'9') return null
    return text.substring(from, from + 2) + ":" + text.substring(from + 2, from + 4) + ":" +
        text.substring(from + 4, from + 6) + (if (utc) "Z" else "")
}

internal fun jcalDateToIcal(text: String): String? {
    if (text.length != 10 || text[4] != '-' || text[7] != '-') return null
    if (!digits(text, 0, 4) || !digits(text, 5, 7) || !digits(text, 8, 10)) return null
    return text.substring(0, 4) + text.substring(5, 7) + text.substring(8, 10)
}

internal fun jcalDateTimeToIcal(text: String): String? {
    val utc = text.length == 20 && text[19] == 'Z'
    if (text.length != 19 && !utc) return null
    if (text[10] != 'T') return null
    val date = jcalDateToIcal(text.substring(0, 10)) ?: return null
    val time = jcalTimeToIcal(text.substring(11)) ?: return null
    return date + "T" + time
}

internal fun jcalTimeToIcal(text: String): String? {
    val utc = text.length == 9 && text[8] == 'Z'
    if (text.length != 8 && !utc) return null
    if (text[2] != ':' || text[5] != ':') return null
    if (!digits(text, 0, 2) || !digits(text, 3, 5) || !digits(text, 6, 8)) return null
    return text.substring(0, 2) + text.substring(3, 5) + text.substring(6, 8) + (if (utc) "Z" else "")
}

internal fun jcalOffsetToIcal(text: String): String? {
    if (text.length != 6 && text.length != 9) return null
    if (text[0] != '+' && text[0] != '-') return null
    if (text[3] != ':' || !digits(text, 1, 3) || !digits(text, 4, 6)) return null
    if (text.length == 9 && (text[6] != ':' || !digits(text, 7, 9))) return null
    return text[0] + text.substring(1, 3) + text.substring(4, 6) + (if (text.length == 9) text.substring(7, 9) else "")
}

internal fun jcalPeriodToIcal(text: String): String? {
    val slash = text.indexOf('/')
    if (slash < 0) return null
    val start = jcalDateTimeToIcal(text.substring(0, slash)) ?: return null
    val rest = text.substring(slash + 1)
    val end = if (isDuration(rest)) rest else jcalDateTimeToIcal(rest) ?: return null
    return "$start/$end"
}

internal fun isDuration(text: String): Boolean {
    var index = 0
    if (index < text.length && (text[index] == '+' || text[index] == '-')) index++
    if (index >= text.length || text[index] != 'P' || index + 1 >= text.length) return false
    for (position in index + 1 until text.length) {
        val char = text[position]
        if (char !in '0'..'9' && char != 'W' && char != 'D' && char != 'T' && char != 'H' && char != 'M' &&
            char != 'S'
        ) {
            return false
        }
    }
    return true
}

private fun digits(text: String, from: Int, to: Int): Boolean {
    for (index in from until to) if (text[index] !in '0'..'9') return false
    return true
}

internal fun escapeText(text: String): String {
    val out = StringBuilder(text.length + 8)
    var index = 0
    while (index < text.length) {
        when (val char = text[index]) {
            '\\' -> out.append("\\\\")

            ';' -> out.append("\\;")

            ',' -> out.append("\\,")

            '\n' -> out.append("\\n")

            '\r' -> {
                out.append("\\n")
                if (index + 1 < text.length && text[index + 1] == '\n') index++
            }

            else -> out.append(char)
        }
        index++
    }
    return out.toString()
}

internal fun base64(bytes: ByteArray): String {
    val out = StringBuilder((bytes.size + 2) / 3 * 4)
    var index = 0
    while (index < bytes.size) {
        val first = bytes[index].toInt() and 0xFF
        val second = if (index + 1 < bytes.size) bytes[index + 1].toInt() and 0xFF else 0
        val third = if (index + 2 < bytes.size) bytes[index + 2].toInt() and 0xFF else 0
        out.append(BASE64[first shr 2])
        out.append(BASE64[(first and 3 shl 4) or (second shr 4)])
        out.append(if (index + 1 < bytes.size) BASE64[(second and 15 shl 2) or (third shr 6)] else '=')
        out.append(if (index + 2 < bytes.size) BASE64[third and 63] else '=')
        index += 3
    }
    return out.toString()
}

private const val BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
private const val SECONDS_PER_DAY = 86_400L

private fun significantDigits(text: String, from: Int, to: Int): Int {
    var count = 0
    for (index in from until to) {
        val char = text[index]
        if (char != '.' && (count > 0 || char != '0')) count++
    }
    return count
}

// Every decimal number of up to 15 significant digits comes back the same from a double; with more digits, not every one does.
private const val MAX_DECIMAL_DIGITS = 15
private const val MIN_NORMAL_DOUBLE = 2.2250738585072014E-308

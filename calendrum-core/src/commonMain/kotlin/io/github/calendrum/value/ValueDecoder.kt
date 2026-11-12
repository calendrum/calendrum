package io.github.calendrum.value

import io.github.calendrum.ProblemCode

// Decodes values of RFC 5545 §3.3. Offsets of problems are relative to `from` of the decoded range (to the start of the
// whole value for list items, rule parts and period bounds). `accept` reports a lenient heuristic and, in strict mode,
// `ICAL-V001` at the same offset, and returns false then. A decoder returns null only after an Error.
internal class ValueDecoder(val isStrict: Boolean, val leapSecondToNextMinute: Boolean, val problems: ValueProblems?) {
    fun report(code: ProblemCode, index: Int, base: Int) {
        problems?.report(code, index - base)
    }

    fun accept(code: ProblemCode, index: Int, base: Int): Boolean {
        problems?.report(code, index - base)
        if (!isStrict) return true
        problems?.report(ProblemCode.InvalidValue, index - base)
        return false
    }

    fun invalid(index: Int, base: Int): Nothing? {
        problems?.report(ProblemCode.InvalidValue, index - base)
        return null
    }

    fun outOfRange(index: Int, base: Int): Nothing? {
        problems?.report(ProblemCode.OutOfRange, index - base)
        return null
    }

    fun acceptBlanks(from: Int, to: Int, start: Int, end: Int, base: Int): Boolean =
        (start == from && end == to) || accept(ProblemCode.Whitespace, if (start != from) from else end, base)
}

internal fun isBlank(char: Char): Boolean = char == ' ' || char == '\t'

internal fun blankStart(value: CharSequence, from: Int, to: Int): Int {
    var index = from
    while (index < to && isBlank(value[index])) index += 1
    return index
}

internal fun blankEnd(value: CharSequence, from: Int, to: Int): Int {
    var index = to
    while (index > from && isBlank(value[index - 1])) index -= 1
    return index
}

internal fun digitAt(value: CharSequence, index: Int, to: Int): Int {
    if (index >= to) return -1
    val char = value[index]
    return if (char in '0'..'9') char - '0' else -1
}

internal fun twoDigits(value: CharSequence, index: Int, to: Int): Int {
    val high = digitAt(value, index, to)
    if (high < 0) return -1
    val low = digitAt(value, index + 1, to)
    if (low < 0) return -1
    return high * 10 + low
}

internal fun digitsEnd(value: CharSequence, from: Int, to: Int): Int {
    var index = from
    while (index < to && value[index] in '0'..'9') index += 1
    return index
}

// Returns -1 when the number exceeds `Int.MAX_VALUE`.
internal fun naturalNumber(value: CharSequence, from: Int, to: Int): Int {
    var result = 0
    for (index in from until to) {
        val digit = value[index] - '0'
        if (result > (Int.MAX_VALUE - digit) / 10) return -1
        result = result * 10 + digit
    }
    return result
}

internal fun upper(char: Char): Char = if (char in 'a'..'z') char - ('a' - 'A') else char

internal fun matchesIgnoreCase(value: CharSequence, from: Int, to: Int, name: String): Boolean {
    if (to - from != name.length) return false
    for (index in name.indices) {
        if (upper(value[from + index]) != name[index]) return false
    }
    return true
}

// Linear search over the names given by `name`, compared without regard to case; inline so that the name functions are
// inlined as well.
internal inline fun indexOfName(value: CharSequence, from: Int, to: Int, count: Int, name: (Int) -> String): Int {
    for (index in 0 until count) {
        if (matchesIgnoreCase(value, from, to, name(index))) return index
    }
    return -1
}

internal fun slice(value: CharSequence, from: Int, to: Int): String =
    if (value is String) value.substring(from, to) else value.subSequence(from, to).toString()

internal fun ValueDecoder.decodeBoolean(value: CharSequence, from: Int, to: Int): Boolean? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    if (matchesIgnoreCase(value, start, end, "TRUE")) return true
    if (matchesIgnoreCase(value, start, end, "FALSE")) return false
    return invalid(start, from)
}

// The magnitude is accumulated in a `Double` (exact up to 2^53): no `Long` per value on JS and no overflow check per
// digit; -2147483648 is the only value whose magnitude exceeds `Int.MAX_VALUE`.
internal fun ValueDecoder.decodeInteger(value: CharSequence, from: Int, to: Int): Int? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    var index = start
    val negative = index < end && value[index] == '-'
    if (index < end && (value[index] == '+' || negative)) index += 1
    if (index == end || digitsEnd(value, index, end) != end) return invalid(if (index == end) end else index, from)
    var magnitude = 0.0
    while (index < end) {
        magnitude = magnitude * 10 + (value[index] - '0')
        if (magnitude > INT_MAGNITUDE_LIMIT) return outOfRange(start, from)
        index += 1
    }
    val signed = if (negative) -magnitude else magnitude
    if (signed > Int.MAX_VALUE.toDouble()) return outOfRange(start, from)
    return signed.toInt()
}

internal fun ValueDecoder.decodeFloat(value: CharSequence, from: Int, to: Int): FloatValue? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    var index = start
    val sign = index < end && (value[index] == '+' || value[index] == '-')
    if (sign) index += 1
    val integerEnd = digitsEnd(value, index, end)
    if (integerEnd == index) return invalid(index, from)
    index = integerEnd
    if (index < end && value[index] == '.') {
        val fractionEnd = digitsEnd(value, index + 1, end)
        if (fractionEnd == index + 1) return invalid(index + 1, from)
        index = fractionEnd
    }
    if (index != end) return invalid(index, from)
    val textStart = if (sign && value[start] == '+') start + 1 else start
    return FloatValue(slice(value, textStart, end))
}

internal fun ValueDecoder.decodeUri(value: CharSequence, from: Int, to: Int): String? {
    val start = blankStart(value, from, to)
    val end = blankEnd(value, start, to)
    if (!acceptBlanks(from, to, start, end, from)) return null
    if (start == end) return invalid(start, from)
    if (!hasScheme(value, start, end) && !accept(ProblemCode.UriWithoutScheme, start, from)) return null
    return slice(value, start, end)
}

private fun hasScheme(value: CharSequence, from: Int, to: Int): Boolean {
    val first = value[from]
    if (!(first in 'a'..'z' || first in 'A'..'Z')) return false
    for (index in from + 1 until to) {
        val char = value[index]
        if (char == ':') return true
        if (!(char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9' || char == '+' || char == '-' || char == '.')) {
            return false
        }
    }
    return false
}

internal fun calAddressKey(value: CharSequence): String {
    val start = if (value.length >= MAILTO.length && matchesIgnoreCase(value, 0, MAILTO.length, MAILTO)) {
        MAILTO.length
    } else {
        0
    }
    return slice(value, start, value.length).lowercase()
}

// First pass: validate and count significant characters, padding and whitespace; second pass: decode into an array of
// the exact size. Non-zero trailing bits are accepted (RFC 4648 §3.5 allows decoders to accept them).
internal fun ValueDecoder.decodeBinary(value: CharSequence, from: Int, to: Int): BinaryValue? {
    var significant = 0
    var padding = 0
    var firstBlank = -1
    for (index in from until to) {
        val char = value[index]
        if (base64Digit(char) >= 0) {
            if (padding > 0) return invalid(index, from)
            significant += 1
            continue
        }
        if (char == '=') {
            padding += 1
            if (padding > 2) return invalid(index, from)
            continue
        }
        if (isBlank(char) || char == '\r' || char == '\n') {
            if (firstBlank < 0) firstBlank = index
            continue
        }
        return invalid(index, from)
    }
    val remainder = significant % 4
    if (remainder == 1 || (padding > 0 && remainder + padding != 4)) return invalid(to, from)
    if (firstBlank >= 0 && !accept(ProblemCode.Base64Whitespace, firstBlank, from)) return null
    if (padding == 0 && remainder != 0 && !accept(ProblemCode.Base64Padding, to, from)) return null
    val bytes = ByteArray(significant / 4 * 3 + (if (remainder == 0) 0 else remainder - 1))
    var buffer = 0
    var bits = 0
    var out = 0
    for (index in from until to) {
        val digit = base64Digit(value[index])
        if (digit < 0) continue
        buffer = (buffer shl 6) or digit
        bits += 6
        if (bits >= 8) {
            bits -= 8
            bytes[out] = (buffer shr bits).toByte()
            out += 1
            buffer = buffer and ((1 shl bits) - 1)
        }
    }
    return BinaryValue(bytes)
}

private fun base64Digit(char: Char): Int = when (char) {
    in 'A'..'Z' -> char - 'A'
    in 'a'..'z' -> char - 'a' + 26
    in '0'..'9' -> char - '0' + 52
    '+' -> 62
    '/' -> 63
    else -> -1
}

internal fun ValueDecoder.decodeText(value: CharSequence, from: Int, to: Int): String? {
    val out = StringBuilder(to - from)
    val end = unescapeItem(value, from, to, from, out, false)
    return if (end < 0) null else out.toString()
}

internal fun ValueDecoder.decodeTextList(value: CharSequence, from: Int, to: Int): List<String>? {
    val items = ArrayList<String>()
    val out = StringBuilder()
    var index = from
    while (true) {
        out.setLength(0)
        val end = unescapeItem(value, index, to, from, out, true)
        if (end < 0) return null
        items.add(out.toString())
        if (end == to) return items
        index = end + 1
    }
}

// Returns the end of the item (an unescaped comma in a list, or `to`), or -1 when strict mode rejects an unknown
// escape.
private fun ValueDecoder.unescapeItem(
    value: CharSequence,
    from: Int,
    to: Int,
    base: Int,
    out: StringBuilder,
    list: Boolean,
): Int {
    var index = from
    while (index < to) {
        val char = value[index]
        if (char == ',' && list) return index
        if (char != '\\') {
            out.append(char)
            index += 1
            continue
        }
        val next = if (index + 1 < to) value[index + 1] else '\u0000'
        when (next) {
            '\\', ';', ',' -> out.append(next)

            'n', 'N' -> out.append('\n')

            else -> {
                if (!accept(ProblemCode.UnknownEscape, index, base)) return -1
                out.append('\\')
                if (index + 1 == to) return to
                out.append(next)
            }
        }
        index += 2
    }
    return to
}

private const val MAILTO = "MAILTO:"
private const val INT_MAGNITUDE_LIMIT = 2_147_483_648.0

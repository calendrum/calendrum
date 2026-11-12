package io.github.calendrum.value

// Correctly rounded decimal to `Double` without `String.toDouble()`, which on Kotlin/Native links the Regex engine
// through `HexStringParser`. Up to 15 significant digits and a decimal exponent within 22 both the integer and
// the power of ten are exact doubles, so one multiplication or division rounds correctly (Clinger's fast path).
// Otherwise the value is a fraction of big integers scaled so that the quotient has 54-55 bits; the quotient is rounded
// to 53 bits half to even with the remainder as a sticky bit, clamped to the subnormal exponent.
internal fun decimalToDouble(text: String): Double {
    val negative = text.isNotEmpty() && text[0] == '-'
    val digits = ByteArray(text.length)
    var count = 0
    var exponent = 0
    var fraction = false
    for (index in (if (negative) 1 else 0) until text.length) {
        val char = text[index]
        if (char == '.') {
            fraction = true
            continue
        }
        if (fraction) exponent -= 1
        if (count > 0 || char != '0') {
            digits[count] = (char - '0').toByte()
            count += 1
        }
    }
    while (count > 0 && digits[count - 1].toInt() == 0) {
        count -= 1
        exponent += 1
    }
    val magnitude = when {
        count == 0 -> 0.0
        count <= FAST_DIGITS && exponent >= -FAST_EXPONENT && exponent <= FAST_EXPONENT -> fast(digits, count, exponent)
        else -> exact(digits, count, exponent)
    }
    return if (negative) -magnitude else magnitude
}

private fun fast(digits: ByteArray, count: Int, exponent: Int): Double {
    var value = 0L
    for (index in 0 until count) value = value * 10 + digits[index]
    var power = 1.0
    val steps = if (exponent < 0) -exponent else exponent
    for (step in 0 until steps) power *= 10.0
    return if (exponent < 0) value.toDouble() / power else value.toDouble() * power
}

private fun exact(digits: ByteArray, digitCount: Int, digitExponent: Int): Double {
    var count = digitCount
    var exponent = digitExponent
    if (count + exponent >= MAX_DECIMAL_MAGNITUDE) return Double.POSITIVE_INFINITY
    if (count + exponent <= MIN_DECIMAL_MAGNITUDE) return 0.0
    if (count > MAX_DIGITS) {
        exponent += count - MAX_DIGITS
        count = MAX_DIGITS
        digits[count - 1] = 1
    }
    val numerator = BigNat(count / 9 + 2)
    var index = 0
    while (index < count) {
        val end = minOf(index + 9, count)
        var chunk = 0
        var factor = 1
        for (position in index until end) {
            chunk = chunk * 10 + digits[position]
            factor *= 10
        }
        numerator.multiplyAdd(factor, chunk)
        index = end
    }
    val denominator = BigNat(1)
    denominator.multiplyAdd(1, 1)
    if (exponent >= 0) numerator.multiplyPowerOfTen(exponent) else denominator.multiplyPowerOfTen(-exponent)
    val shift = numerator.bitLength() - denominator.bitLength() - QUOTIENT_BITS
    val dividend = if (shift < 0) numerator.shiftedLeft(-shift) else numerator
    val divisor = if (shift > 0) denominator.shiftedLeft(shift) else denominator
    val step = divisor.shiftedLeft(QUOTIENT_BITS + 1)
    var quotient = 0L
    for (bit in QUOTIENT_BITS + 1 downTo 0) {
        if (dividend.compareTo(step) >= 0) {
            dividend.subtract(step)
            quotient = quotient or (1L shl bit)
        }
        step.shiftRightOne()
    }
    val sticky = !dividend.isZero()
    val quotientBits = 64 - quotient.countLeadingZeroBits()
    var binaryExponent = shift + quotientBits - MANTISSA_BITS
    if (binaryExponent < MIN_BINARY_EXPONENT) binaryExponent = MIN_BINARY_EXPONENT
    val drop = binaryExponent - shift
    if (drop >= 64) return 0.0
    var mantissa = quotient ushr drop
    if (drop > 0) {
        val rest = quotient and ((1L shl drop) - 1)
        val half = 1L shl (drop - 1)
        if (rest > half || (rest == half && (sticky || mantissa and 1L == 1L))) mantissa += 1
    }
    if (mantissa == 1L shl MANTISSA_BITS) {
        mantissa = mantissa ushr 1
        binaryExponent += 1
    }
    if (mantissa < IMPLICIT_BIT) return Double.fromBits(mantissa)
    val biased = binaryExponent + EXPONENT_BIAS
    if (biased >= MAX_BIASED_EXPONENT) return Double.POSITIVE_INFINITY
    return Double.fromBits((biased.toLong() shl 52) or (mantissa - IMPLICIT_BIT))
}

private class BigNat(capacity: Int) {
    private var words = IntArray(maxOf(capacity, 1))
    private var size = 0

    fun multiplyAdd(factor: Int, addend: Int) {
        var carry = addend.toLong()
        for (index in 0 until size) {
            val product = (words[index].toLong() and WORD) * factor + carry
            words[index] = product.toInt()
            carry = product ushr 32
        }
        if (carry != 0L) append(carry.toInt())
    }

    fun multiplyPowerOfTen(power: Int) {
        var rest = power
        while (rest >= 9) {
            multiplyAdd(BILLION, 0)
            rest -= 9
        }
        var factor = 1
        for (step in 0 until rest) factor *= 10
        if (factor != 1) multiplyAdd(factor, 0)
    }

    fun bitLength(): Int = if (size == 0) 0 else (size - 1) * 32 + 32 - words[size - 1].countLeadingZeroBits()

    fun isZero(): Boolean = size == 0

    fun shiftedLeft(bits: Int): BigNat {
        val wordShift = bits / 32
        val bitShift = bits % 32
        val result = BigNat(size + wordShift + 1)
        result.size = size + wordShift + 1
        for (index in 0 until size) {
            val word = words[index].toLong() and WORD
            val shifted = word shl bitShift
            val target = index + wordShift
            result.words[target] = result.words[target] or shifted.toInt()
            result.words[target + 1] = (shifted ushr 32).toInt()
        }
        result.trim()
        return result
    }

    fun shiftRightOne() {
        for (index in 0 until size) {
            val high = if (index + 1 < size) words[index + 1] shl 31 else 0
            words[index] = (words[index] ushr 1) or high
        }
        trim()
    }

    fun compareTo(other: BigNat): Int {
        if (size != other.size) return if (size < other.size) -1 else 1
        for (index in size - 1 downTo 0) {
            val left = words[index].toLong() and WORD
            val right = other.words[index].toLong() and WORD
            if (left != right) return if (left < right) -1 else 1
        }
        return 0
    }

    fun subtract(other: BigNat) {
        var borrow = 0L
        for (index in 0 until size) {
            val right = if (index < other.size) other.words[index].toLong() and WORD else 0L
            val difference = (words[index].toLong() and WORD) - right - borrow
            words[index] = difference.toInt()
            borrow = if (difference < 0) 1L else 0L
        }
        trim()
    }

    private fun append(word: Int) {
        if (size == words.size) words = words.copyOf(size * 2)
        words[size] = word
        size += 1
    }

    private fun trim() {
        while (size > 0 && words[size - 1] == 0) size -= 1
    }
}

private const val FAST_DIGITS = 15
private const val FAST_EXPONENT = 22

// A midpoint between two adjacent doubles has at most 767 significant decimal digits, so 799 true digits and one
// non-zero digit in place of the rest round like the whole input; this bounds the work on very long input.
private const val MAX_DIGITS = 800
private const val MAX_DECIMAL_MAGNITUDE = 310
private const val MIN_DECIMAL_MAGNITUDE = -324
private const val QUOTIENT_BITS = 54
private const val MANTISSA_BITS = 53
private const val IMPLICIT_BIT = 1L shl 52
private const val MIN_BINARY_EXPONENT = -1074
private const val EXPONENT_BIAS = 1075
private const val MAX_BIASED_EXPONENT = 2047
private const val BILLION = 1_000_000_000
private const val WORD = 0xFFFF_FFFFL

// Plain decimal notation of a double for `FLOAT` (GEO): the shortest digits that the platform prints, without an
// exponent and trailing zeros, so `1.0E-5` and `1e-5` both become `0.00001`.
internal fun decimalText(value: Double): String {
    require(!value.isNaN() && !value.isInfinite()) { "A FLOAT value is finite" }
    if (value == 0.0) return "0"
    val raw = value.toString()
    val digits = StringBuilder(raw.length)
    var point = -1
    var exponent = 0
    var index = if (raw[0] == '-') 1 else 0
    while (index < raw.length) {
        val char = raw[index]
        if (char == 'e' || char == 'E') {
            exponent = raw.substring(index + 1).toInt()
            break
        }
        if (char == '.') point = digits.length else digits.append(char)
        index += 1
    }
    if (point < 0) point = digits.length
    point += exponent
    var first = 0
    while (first < digits.length - 1 && digits[first] == '0') {
        first += 1
        point -= 1
    }
    var last = digits.length
    while (last > first + 1 && digits[last - 1] == '0' && last - first > point) last -= 1
    val out = StringBuilder(last - first + 4)
    if (value < 0) out.append('-')
    when {
        point <= 0 -> {
            out.append("0.")
            for (step in 0 until -point) out.append('0')
            out.append(digits, first, last)
        }

        point >= last - first -> {
            out.append(digits, first, last)
            for (step in 0 until point - (last - first)) out.append('0')
        }

        else -> {
            out.append(digits, first, first + point)
            out.append('.')
            out.append(digits, first + point, last)
        }
    }
    return out.toString()
}

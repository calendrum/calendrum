package io.github.calendrum.syntax

internal class Transcoded(val bytes: ByteArray, val end: Int)

// Two passes: the first counts the UTF-8 length, the second writes, so the output is allocated once at its exact size.
// Bytes `prefixStart until from` are copied unchanged to `outStart`, and the decoded rest follows them: the lexer uses
// `outStart == prefixStart` to keep the indices of the ASCII prefix, eager decoding uses 0. An output larger than the
// largest array is a contract error, not a diagnostic: such an input cannot be held in memory twice anyway.
internal fun transcode(
    charset: InputCharset,
    input: ByteArray,
    prefixStart: Int,
    from: Int,
    to: Int,
    outStart: Int,
): Transcoded {
    val decodedStart = outStart + (from - prefixStart)
    val size = decodedStart.toLong() + decodeToUtf8(charset, input, from, to, null, 0)
    require(size <= MAX_DECODED_SIZE) { "Decoded input of $size bytes exceeds the maximum array size" }
    val out = ByteArray(size.toInt())
    input.copyInto(out, outStart, prefixStart, from)
    val end = decodeToUtf8(charset, input, from, to, out, decodedStart)
    return Transcoded(out, end)
}

internal fun decodeToUtf8(
    charset: InputCharset,
    input: ByteArray,
    from: Int,
    to: Int,
    out: ByteArray?,
    start: Int,
): Int = when (charset) {
    InputCharset.Utf16Le -> decodeUtf16(input, from, to, bigEndian = false, out, start)
    InputCharset.Utf16Be -> decodeUtf16(input, from, to, bigEndian = true, out, start)
    InputCharset.Utf32Le -> decodeUtf32(input, from, to, bigEndian = false, out, start)
    InputCharset.Utf32Be -> decodeUtf32(input, from, to, bigEndian = true, out, start)
    else -> decodeSingleByte(input, from, to, SingleByteCharsets.table(charset), out, start)
}

internal fun decodableEnd(charset: InputCharset, input: ByteArray, from: Int, to: Int): Int = when (charset) {
    InputCharset.Utf16Le, InputCharset.Utf16Be -> {
        var cut = from + ((to - from) and -2)
        if (cut - from >= 2) {
            val first = input[cut - 2].toInt() and 0xFF
            val second = input[cut - 1].toInt() and 0xFF
            val unit = if (charset == InputCharset.Utf16Be) (first shl 8) or second else (second shl 8) or first
            if (unit >= LEAD_SURROGATE_FIRST && unit < TRAIL_SURROGATE_FIRST) cut -= 2
        }
        cut
    }

    InputCharset.Utf32Le, InputCharset.Utf32Be -> from + ((to - from) and -4)

    else -> to
}

private fun decodeSingleByte(input: ByteArray, from: Int, to: Int, table: CharArray, out: ByteArray?, start: Int): Int {
    var index = start
    if (out == null) {
        for (i in from until to) {
            val byte = input[i].toInt()
            index += if (byte >= 0) 1 else utf8Length(table[byte + HIGH_BYTES].code)
        }
        return index
    }
    for (i in from until to) {
        val byte = input[i].toInt()
        if (byte >= 0) {
            out[index++] = byte.toByte()
        } else {
            index = putUtf8(out, index, table[byte + HIGH_BYTES].code)
        }
    }
    return index
}

// The WHATWG UTF-16 decoder: a lead surrogate waits for a trail surrogate; any other unit after it gives one U+FFFD and
// is then decoded on its own; a lone trail surrogate is U+FFFD; a pending lead surrogate or an odd last byte at the end
// gives one U+FFFD together. The JDK differs: it replaces a lead surrogate and the unit after it with one U+FFFD,
// losing that unit.
private fun decodeUtf16(input: ByteArray, from: Int, to: Int, bigEndian: Boolean, out: ByteArray?, start: Int): Int {
    var index = start
    var i = from
    var lead = -1
    while (i + 1 < to) {
        val first = input[i].toInt() and 0xFF
        val second = input[i + 1].toInt() and 0xFF
        val unit = if (bigEndian) (first shl 8) or second else (second shl 8) or first
        i += 2
        if (lead >= 0) {
            val previous = lead
            lead = -1
            if (isTrailSurrogate(unit)) {
                val codePoint =
                    SUPPLEMENTARY_BASE + ((previous - LEAD_SURROGATE_FIRST) shl 10) + (unit - TRAIL_SURROGATE_FIRST)
                index = emit(out, index, codePoint)
                continue
            }
            index = emit(out, index, REPLACEMENT.code)
        }
        when {
            unit < LEAD_SURROGATE_FIRST || unit > TRAIL_SURROGATE_LAST -> index = emit(out, index, unit)
            unit < TRAIL_SURROGATE_FIRST -> lead = unit
            else -> index = emit(out, index, REPLACEMENT.code)
        }
    }
    if (lead >= 0 || i < to) index = emit(out, index, REPLACEMENT.code)
    return index
}

// Values above U+10FFFF, surrogate code points and a cut last unit become U+FFFD. The JDK passes surrogate code points
// through as unpaired UTF-16 surrogates, which have no UTF-8 form.
private fun decodeUtf32(input: ByteArray, from: Int, to: Int, bigEndian: Boolean, out: ByteArray?, start: Int): Int {
    var index = start
    var i = from
    while (i + 3 < to) {
        val b0 = input[i].toInt() and 0xFF
        val b1 = input[i + 1].toInt() and 0xFF
        val b2 = input[i + 2].toInt() and 0xFF
        val b3 = input[i + 3].toInt() and 0xFF
        val value = if (bigEndian) {
            (b0 shl 24) or (b1 shl 16) or (b2 shl 8) or b3
        } else {
            (b3 shl 24) or (b2 shl 16) or (b1 shl 8) or b0
        }
        i += 4
        val scalar = value in 0..MAX_CODE_POINT && (value < LEAD_SURROGATE_FIRST || value > TRAIL_SURROGATE_LAST)
        index = emit(out, index, if (scalar) value else REPLACEMENT.code)
    }
    if (i < to) index = emit(out, index, REPLACEMENT.code)
    return index
}

private fun isTrailSurrogate(unit: Int): Boolean = unit >= TRAIL_SURROGATE_FIRST && unit <= TRAIL_SURROGATE_LAST

private fun emit(out: ByteArray?, index: Int, codePoint: Int): Int =
    if (out == null) index + utf8Length(codePoint) else putUtf8(out, index, codePoint)

internal fun utf8Length(codePoint: Int): Int = when {
    codePoint < 0x80 -> 1
    codePoint < 0x800 -> 2
    codePoint < SUPPLEMENTARY_BASE -> 3
    else -> 4
}

internal fun putUtf8(out: ByteArray, index: Int, codePoint: Int): Int {
    var i = index
    when {
        codePoint < 0x80 -> out[i++] = codePoint.toByte()

        codePoint < 0x800 -> {
            out[i++] = (0xC0 or (codePoint shr 6)).toByte()
            out[i++] = (0x80 or (codePoint and 0x3F)).toByte()
        }

        codePoint < SUPPLEMENTARY_BASE -> {
            out[i++] = (0xE0 or (codePoint shr 12)).toByte()
            out[i++] = (0x80 or ((codePoint shr 6) and 0x3F)).toByte()
            out[i++] = (0x80 or (codePoint and 0x3F)).toByte()
        }

        else -> {
            out[i++] = (0xF0 or (codePoint shr 18)).toByte()
            out[i++] = (0x80 or ((codePoint shr 12) and 0x3F)).toByte()
            out[i++] = (0x80 or ((codePoint shr 6) and 0x3F)).toByte()
            out[i++] = (0x80 or (codePoint and 0x3F)).toByte()
        }
    }
    return i
}

private const val LEAD_SURROGATE_FIRST = 0xD800
private const val TRAIL_SURROGATE_FIRST = 0xDC00
private const val TRAIL_SURROGATE_LAST = 0xDFFF
private const val SUPPLEMENTARY_BASE = 0x10000
private const val MAX_CODE_POINT = 0x10FFFF
private const val MAX_DECODED_SIZE = Int.MAX_VALUE - 8L

package io.github.calendrum.syntax

internal const val UTF8_CONTINUATION_LOW: Int = 0x80
internal const val UTF8_CONTINUATION_HIGH: Int = 0xBF

private const val REPLACEMENT_CHARACTER: Char = '�'

// Valid lead bytes of UTF-8 (Unicode Table 3-7), packed as the number of continuation bytes and the allowed range of
// the first continuation byte; 0 means the byte can never start a sequence.
internal fun utf8LeadInfo(lead: Int): Int = when (lead) {
    in 0xC2..0xDF -> packLead(1, UTF8_CONTINUATION_LOW, UTF8_CONTINUATION_HIGH)
    0xE0 -> packLead(2, 0xA0, UTF8_CONTINUATION_HIGH)
    in 0xE1..0xEC, 0xEE, 0xEF -> packLead(2, UTF8_CONTINUATION_LOW, UTF8_CONTINUATION_HIGH)
    0xED -> packLead(2, UTF8_CONTINUATION_LOW, 0x9F)
    0xF0 -> packLead(3, 0x90, UTF8_CONTINUATION_HIGH)
    in 0xF1..0xF3 -> packLead(3, UTF8_CONTINUATION_LOW, UTF8_CONTINUATION_HIGH)
    0xF4 -> packLead(3, UTF8_CONTINUATION_LOW, 0x8F)
    else -> 0
}

internal fun utf8LeadNeed(info: Int): Int = info ushr 16

internal fun utf8LeadLow(info: Int): Int = (info ushr 8) and 0xFF

internal fun utf8LeadHigh(info: Int): Int = info and 0xFF

private fun packLead(need: Int, low: Int, high: Int): Int = (need shl 16) or (low shl 8) or high

// Locals only, no state: the lead byte table of `utf8LeadInfo`, the range of the first continuation byte, then
// `10xxxxxx` for the others. Returns -1 unless a complete, well-formed sequence starts at `index` and ends before
// `limit`.
internal fun utf8CompleteSequenceEnd(bytes: ByteArray, index: Int, limit: Int): Int {
    val info = utf8LeadInfo(bytes[index].toInt() and 0xFF)
    val end = index + utf8LeadNeed(info) + 1
    if (info == 0 || end > limit) return -1
    val second = bytes[index + 1].toInt() and 0xFF
    if (second < utf8LeadLow(info) || second > utf8LeadHigh(info)) return -1
    for (k in index + 2 until end) {
        if (bytes[k].toInt() and UTF8_CONTINUATION_MASK != UTF8_CONTINUATION_LOW) return -1
    }
    return end
}

private const val UTF8_CONTINUATION_MASK = 0xC0

// Signed `Byte` bounds: continuation bytes are below `UTF8_LEAD_FIRST` (0xC0), two-byte leads are `UTF8_TWO_BYTE_LEAD`
// (0xC2) up to and excluding `UTF8_THREE_BYTE_LEAD` (0xE0).
internal const val UTF8_LEAD_FIRST: Byte = 0xC0.toByte()
internal const val UTF8_TWO_BYTE_LEAD: Byte = 0xC2.toByte()
internal const val UTF8_THREE_BYTE_LEAD: Byte = 0xE0.toByte()

// Valid input goes through the platform decoder (`decodeValidUtf8`: the stdlib, except on wasmJs). Input that the lexer
// marked invalid goes through `decodeUtf8Replacing`, so the replacement is the same on every platform.
internal fun decodeUtf8(bytes: ByteArray, start: Int, end: Int, valid: Boolean): String =
    if (valid) decodeValidUtf8(bytes, start, end) else decodeUtf8Replacing(bytes, start, end)

internal expect fun decodeValidUtf8(bytes: ByteArray, start: Int, end: Int): String

// Replaces each maximal subpart of an ill-formed sequence with one U+FFFD, as the Unicode standard recommends and
// WHATWG requires.
internal fun decodeUtf8Replacing(bytes: ByteArray, start: Int, end: Int): String {
    val chars = CharArray(end - start)
    return chars.concatToString(0, decodeUtf8Into(bytes, start, end, chars))
}

// The same decoding into a caller's buffer, which needs at least one char per input byte; returns the number of chars.
// The cursor decodes values into its reusable buffer with it.
internal fun decodeUtf8Into(bytes: ByteArray, start: Int, end: Int, chars: CharArray): Int {
    var count = 0
    var i = start
    while (i < end) {
        val lead = bytes[i].toInt() and 0xFF
        i++
        if (lead < UTF8_CONTINUATION_LOW) {
            chars[count++] = lead.toChar()
            continue
        }
        val info = utf8LeadInfo(lead)
        if (info == 0) {
            chars[count++] = REPLACEMENT_CHARACTER
            continue
        }
        var need = utf8LeadNeed(info)
        var low = utf8LeadLow(info)
        var high = utf8LeadHigh(info)
        var codePoint = lead and (0x7F ushr (need + 1))
        while (need > 0 && i < end) {
            val next = bytes[i].toInt() and 0xFF
            if (next < low || next > high) break
            codePoint = (codePoint shl 6) or (next and 0x3F)
            low = UTF8_CONTINUATION_LOW
            high = UTF8_CONTINUATION_HIGH
            need--
            i++
        }
        count = putDecoded(chars, count, if (need > 0) REPLACEMENT_CODE_POINT else codePoint)
    }
    return count
}

// Writes a code point as one char or a surrogate pair; used by the decoder and by the `Appendable` output of the
// writer.
internal fun putDecoded(chars: CharArray, at: Int, codePoint: Int): Int {
    if (codePoint < SUPPLEMENTARY_BASE) {
        chars[at] = codePoint.toChar()
        return at + 1
    }
    val offset = codePoint - SUPPLEMENTARY_BASE
    chars[at] = (HIGH_SURROGATE_BASE + (offset ushr 10)).toChar()
    chars[at + 1] = (LOW_SURROGATE_BASE + (offset and 0x3FF)).toChar()
    return at + 2
}

internal const val REPLACEMENT_CODE_POINT: Int = 0xFFFD

private const val SUPPLEMENTARY_BASE = 0x10000
private const val HIGH_SURROGATE_BASE = 0xD800
private const val LOW_SURROGATE_BASE = 0xDC00

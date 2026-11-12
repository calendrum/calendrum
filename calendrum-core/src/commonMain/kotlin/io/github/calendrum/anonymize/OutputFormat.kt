package io.github.calendrum.anonymize

import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.InputCharset
import io.github.calendrum.syntax.ParseResult
import io.github.calendrum.syntax.SingleByteCharsets
import io.github.calendrum.syntax.charsetForLabel

// Mirrors the input: its charset (single-byte tables of the decoder read backwards, U+FFFD back to the first undefined
// byte), a byte order mark (always for UTF-16/32, which is only detected by one), the most frequent line ending with
// one bare LF (last) or CR (first) when the input also had those, and no line ending at the end when the input had
// none. The writer writes only CRLF, so every CR of its output starts a CRLF pair.
internal class OutputFormat(
    val charset: InputCharset,
    val byteOrderMark: Boolean,
    private val lineEnding: Int,
    private val lastLf: Boolean,
    private val firstCr: Boolean,
    private val finalLineEnding: Boolean,
) {
    val lineEndings: String
        get() = buildString {
            append(NAMES[lineEnding])
            if (lastLf) append(", one LF")
            if (firstCr) append(", one CR")
            if (!finalLineEnding) append(", none at the end")
        }

    fun encode(utf8: ByteArray): ByteArray? {
        val text = convertLineEndings(utf8)
        val body = when {
            charset == InputCharset.Utf8 -> text
            charset.isSingleByte -> singleByte(text, SingleByteCharsets.table(charset))
            else -> wide(text)
        } ?: return null
        if (!byteOrderMark) return body
        val mark = if (charset == InputCharset.Utf8) BYTE_ORDER_MARK else wide(BYTE_ORDER_MARK)!!
        return mark + body
    }

    private fun convertLineEndings(utf8: ByteArray): ByteArray {
        var ends = 0
        for (byte in utf8) {
            if (byte == LF) ends++
        }
        val lastKept = if (finalLineEnding) ends else ends - 1
        val out = ByteArray(utf8.size)
        var at = 0
        var seen = 0
        var index = 0
        while (index < utf8.size) {
            val byte = utf8[index]
            if (byte != CR) {
                out[at++] = byte
                index++
                continue
            }
            seen++
            index += 2
            if (seen > lastKept) continue
            val preferred = when {
                seen == lastKept && lastLf -> LINE_LF
                seen == 1 && firstCr -> LINE_CR
                else -> lineEnding
            }
            val style = if (preferred == LINE_LF && at > 0 && out[at - 1] == CR) LINE_CRLF else preferred
            if (style != LINE_LF) out[at++] = CR
            if (style != LINE_CR) out[at++] = LF
        }
        return out.copyOf(at)
    }

    private fun singleByte(utf8: ByteArray, table: CharArray): ByteArray? {
        val out = ByteArray(utf8.size)
        var at = 0
        var index = 0
        while (index < utf8.size) {
            val unit = utf8Unit(utf8, index, utf8.size)
            if (unit and INVALID != 0) return null
            val length = unit and UNIT_LENGTH
            val codePoint = decode(utf8, index, length)
            index += length
            if (codePoint < ASCII_END) {
                out[at++] = codePoint.toByte()
                continue
            }
            if (codePoint >= SUPPLEMENTARY_BASE) return null
            val found = table.indexOf(codePoint.toChar())
            if (found < 0) return null
            out[at++] = (found + ASCII_END).toByte()
        }
        return out.copyOf(at)
    }

    private fun wide(utf8: ByteArray): ByteArray? {
        val wide32 = charset == InputCharset.Utf32Le || charset == InputCharset.Utf32Be || charset == InputCharset.Utf32
        val out = ByteArray(utf8.size * UTF32_UNIT)
        var at = 0
        var index = 0
        while (index < utf8.size) {
            val unit = utf8Unit(utf8, index, utf8.size)
            if (unit and INVALID != 0) return null
            val length = unit and UNIT_LENGTH
            val codePoint = decode(utf8, index, length)
            index += length
            at = when {
                wide32 -> put(out, at, codePoint, UTF32_UNIT)

                codePoint < SUPPLEMENTARY_BASE -> put(out, at, codePoint, UTF16_UNIT)

                else -> {
                    val offset = codePoint - SUPPLEMENTARY_BASE
                    val high = put(out, at, HIGH_SURROGATE + (offset ushr SURROGATE_BITS), UTF16_UNIT)
                    put(out, high, LOW_SURROGATE + (offset and SURROGATE_MASK), UTF16_UNIT)
                }
            }
        }
        return out.copyOf(at)
    }

    private fun put(out: ByteArray, at: Int, value: Int, unit: Int): Int {
        val bigEndian = isBigEndian(charset)
        for (index in 0 until unit) {
            val shift = (if (bigEndian) unit - 1 - index else index) * Byte.SIZE_BITS
            out[at + index] = (value ushr shift).toByte()
        }
        return at + unit
    }

    companion object {
        private const val LINE_CRLF = 0
        private const val LINE_LF = 1
        private const val LINE_CR = 2
        private const val CR: Byte = 0x0D
        private const val LF: Byte = 0x0A
        private const val ASCII_END = 0x80
        private const val SUPPLEMENTARY_BASE = 0x10000
        private const val HIGH_SURROGATE = 0xD800
        private const val LOW_SURROGATE = 0xDC00
        private const val SURROGATE_BITS = 10
        private const val SURROGATE_MASK = 0x3FF
        private const val UTF16_UNIT = 2
        private const val UTF32_UNIT = 4
        private val NAMES = arrayOf("CRLF", "LF", "CR")
        private val BYTE_ORDER_MARK = byteArrayOf(-17, -69, -65)

        fun of(input: ByteArray, result: ParseResult): OutputFormat {
            val charset = charsetForLabel(result.charset) ?: InputCharset.Utf8
            val unit = when {
                charset == InputCharset.Utf8 || charset.isSingleByte -> 1
                charset == InputCharset.Utf32Le || charset == InputCharset.Utf32Be || charset == InputCharset.Utf32 -> 4
                else -> 2
            }
            val bigEndian = isBigEndian(charset)
            var crlf = 0
            var lf = 0
            var cr = 0
            var last = -1
            var index = if (unit == 1) 0 else unit
            while (index + unit <= input.size) {
                val value = unitAt(input, index, unit, bigEndian)
                if (value == CR.toInt() && index + 2 * unit <= input.size &&
                    unitAt(input, index + unit, unit, bigEndian) == LF.toInt()
                ) {
                    crlf++
                    index += unit
                    last = LF.toInt()
                } else {
                    if (value == CR.toInt()) cr++
                    if (value == LF.toInt()) lf++
                    last = value
                }
                index += unit
            }
            val style = when {
                lf > crlf && lf >= cr -> LINE_LF
                cr > crlf && cr > lf -> LINE_CR
                else -> LINE_CRLF
            }
            val byteOrderMark = if (charset == InputCharset.Utf8) {
                result.problemCount(ProblemCode.Utf8Bom) > 0
            } else {
                !charset.isSingleByte
            }
            return OutputFormat(
                charset,
                byteOrderMark,
                style,
                lastLf = lf > 0 && style != LINE_LF,
                firstCr = cr > 0 && style != LINE_CR,
                finalLineEnding = last == CR.toInt() || last == LF.toInt(),
            )
        }

        private fun isBigEndian(charset: InputCharset): Boolean = charset == InputCharset.Utf16Be ||
            charset == InputCharset.Utf32Be || charset == InputCharset.Utf16 || charset == InputCharset.Utf32

        private fun unitAt(input: ByteArray, index: Int, unit: Int, bigEndian: Boolean): Int {
            var value = 0
            for (offset in 0 until unit) {
                val byte = input[index + offset].toInt() and 0xFF
                value = if (bigEndian) {
                    (value shl Byte.SIZE_BITS) or byte
                } else {
                    value or (byte shl (offset * Byte.SIZE_BITS))
                }
            }
            return value
        }
    }
}

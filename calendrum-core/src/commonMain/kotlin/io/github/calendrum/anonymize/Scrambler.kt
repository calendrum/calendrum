package io.github.calendrum.anonymize

import io.github.calendrum.syntax.putUtf8
import io.github.calendrum.syntax.utf8LeadHigh
import io.github.calendrum.syntax.utf8LeadInfo
import io.github.calendrum.syntax.utf8LeadLow
import io.github.calendrum.syntax.utf8LeadNeed
import io.github.calendrum.syntax.utf8Length

internal class Scrambler(private val pseudonyms: Pseudonyms, private val forbidden: ForbiddenGrams) {
    private val letters = Letters()
    private val encoded = ByteArray(MAX_UTF8)

    var invalidSequences: Int = 0
        private set

    // Plain TEXT is cut into items at unescaped `,` and `;` and each item gets its own random stream from its bytes, so
    // equal list items (CATEGORIES) get equal replacements; an HTML value is one item.
    fun text(bytes: ByteArray, from: Int, to: Int, html: Boolean) {
        if (html) {
            val markup = htmlMarkup(bytes, from, to)
            scramble(bytes, from, to, pseudonyms.stream(Pseudonyms.TAG_TEXT, bytes, from, to), TEXT, markup)
            return
        }
        var start = from
        var index = from
        while (index < to) {
            val byte = bytes[index]
            if (byte == BACKSLASH && index + 1 < to) {
                index += 2
                continue
            }
            if (byte == COMMA || byte == SEMICOLON) {
                segment(bytes, start, index)
                start = index + 1
            }
            index++
        }
        segment(bytes, start, to)
    }

    fun parameter(bytes: ByteArray, from: Int, to: Int) {
        if (from == to) return
        scramble(bytes, from, to, pseudonyms.stream(Pseudonyms.TAG_TEXT, bytes, from, to), CARET, null)
    }

    fun binary(bytes: ByteArray, from: Int, to: Int) {
        if (from == to) return
        val random = pseudonyms.stream(Pseudonyms.TAG_BINARY, bytes, from, to)
        for (index in from until to) {
            if (BASE64.indexOf((bytes[index].toInt() and 0xFF).toChar()) >= 0) {
                bytes[index] = BASE64[random.nextByte() and BASE64_MASK].code.toByte()
            }
        }
    }

    private fun segment(bytes: ByteArray, from: Int, to: Int) {
        if (from == to) return
        scramble(bytes, from, to, pseudonyms.stream(Pseudonyms.TAG_TEXT, bytes, from, to), TEXT, null)
    }

    // One code point at a time: escapes (`\n`, `\N`, `\,`, `\;`, `\` everywhere, RFC 6868 carets in parameters) stay,
    // an `n` or `N` of an escape counts as a letter of the next word for the 4-gram check, a letter or digit gets a
    // replacement of the same class and UTF-8 length that does not complete a forbidden 4-gram, emoji and other
    // supplementary code points get a replacement too, everything else stays.
    private fun scramble(
        bytes: ByteArray,
        from: Int,
        to: Int,
        random: RandomStream,
        escapes: Int,
        markup: BooleanArray?,
    ) {
        var run = 0
        var first = 0
        var second = 0
        var third = 0
        var index = from
        while (index < to) {
            if (markup != null && markup[index - from]) {
                run = 0
                index++
                continue
            }
            val byte = bytes[index]
            if (byte == BACKSLASH || (escapes == CARET && byte == CARET_BYTE)) {
                val next: Byte = if (index + 1 < to) bytes[index + 1] else 0
                val escaped = if (byte == BACKSLASH) next in TEXT_ESCAPES else next in CARET_ESCAPES
                if (escaped && (next == LOWER_N || next == UPPER_N)) {
                    first = second
                    second = third
                    third = LOWER_N.toInt()
                    run = 1
                    index += 2
                    continue
                }
                run = 0
                index += if (escaped) 2 else 1
                continue
            }
            val unit = utf8Unit(bytes, index, to)
            val length = unit and UNIT_LENGTH
            if (unit and INVALID != 0) {
                writeInvalid(bytes, index, length)
                invalidSequences++
                run = 0
                index += length
                continue
            }
            val codePoint = decode(bytes, index, length)
            if (!isWordCodePoint(codePoint)) {
                if (codePoint >= SUPPLEMENTARY_BASE) put(bytes, index, length, letters.replacement(codePoint, random))
                run = 0
                index += length
                continue
            }
            var replacement = letters.replacement(codePoint, random)
            if (run >= GRAM - 1) {
                var attempt = 1
                while (attempt < MAX_ATTEMPTS && forbidden.contains(first, second, third, lower(replacement))) {
                    replacement = letters.replacement(codePoint, random)
                    attempt++
                }
            }
            put(bytes, index, length, replacement)
            first = second
            second = third
            third = lower(replacement)
            run++
            index += length
        }
    }

    private fun put(bytes: ByteArray, index: Int, length: Int, codePoint: Int) {
        if (utf8Length(codePoint) != length) return
        putUtf8(encoded, 0, codePoint)
        encoded.copyInto(bytes, index, 0, length)
    }

    private fun lower(codePoint: Int): Int = codePoint.toChar().lowercaseChar().code

    // An invalid maximal subpart of 1, 2 or 3 bytes is replaced with FF, E1 80 or F1 80 80: invalid subparts of the
    // same length. The byte after the subpart is never a continuation byte (it was not one in the source, and
    // replacements keep the class of every byte), so the number of U+FFFD, the position of ICAL-S006 and the charset
    // decision stay.
    private fun writeInvalid(bytes: ByteArray, index: Int, length: Int) {
        when (length) {
            1 -> bytes[index] = INVALID_BYTE

            2 -> {
                bytes[index] = TRUNCATED_THREE
                bytes[index + 1] = CONTINUATION
            }

            else -> {
                bytes[index] = TRUNCATED_FOUR
                bytes[index + 1] = CONTINUATION
                bytes[index + 2] = CONTINUATION
            }
        }
    }

    // Marks what stays in an HTML value: `<`, tag names, attribute names, `>`, `<!--`, `-->`, named entities (also with
    // the `\;` that Outlook writes) and `&#`/`&#x` with the `;` of numeric entities. Attribute values, comments and
    // text are replaced.
    private fun htmlMarkup(bytes: ByteArray, from: Int, to: Int): BooleanArray {
        val markup = BooleanArray(to - from)
        var index = from
        while (index < to) {
            val byte = bytes[index]
            index = when {
                byte == LESS && index + 1 < to && startsTag(bytes[index + 1]) -> tag(bytes, index, to, markup, from)
                byte == AMPERSAND -> entity(bytes, index, to, markup, from)
                else -> index + 1
            }
        }
        return markup
    }

    private fun tag(bytes: ByteArray, start: Int, to: Int, markup: BooleanArray, base: Int): Int {
        if (startsWith(bytes, start, to, COMMENT_OPEN)) {
            mark(markup, base, start, start + COMMENT_OPEN.length)
            var index = start + COMMENT_OPEN.length
            while (index < to && !startsWith(bytes, index, to, COMMENT_CLOSE)) index++
            val end = minOf(index + COMMENT_CLOSE.length, to)
            mark(markup, base, index, end)
            return end
        }
        var index = start + 1
        mark(markup, base, start, index)
        while (index < to && isTagNameByte(bytes[index])) {
            mark(markup, base, index, index + 1)
            index++
        }
        while (index < to) {
            val byte = bytes[index]
            when {
                byte == GREATER -> {
                    mark(markup, base, index, index + 1)
                    return index + 1
                }

                isSpace(byte) || byte == SLASH || byte == QUESTION -> index++

                byte == BACKSLASH && index + 1 < to && bytes[index + 1] in TEXT_ESCAPES -> index += 2

                else -> index = attribute(bytes, index, to, markup, base)
            }
        }
        return index
    }

    private fun attribute(bytes: ByteArray, start: Int, to: Int, markup: BooleanArray, base: Int): Int {
        var index = start
        while (index < to && !isSpace(bytes[index]) && bytes[index] !in ATTRIBUTE_END) index++
        if (index == start) {
            mark(markup, base, index, index + 1)
            return index + 1
        }
        mark(markup, base, start, index)
        var after = index
        while (after < to && isSpace(bytes[after])) after++
        if (after >= to || bytes[after] != EQUALS) return index
        after++
        while (after < to && isSpace(bytes[after])) after++
        if (after < to && (bytes[after] == DQUOTE || bytes[after] == APOSTROPHE)) {
            val quote = bytes[after]
            after++
            while (after < to && bytes[after] != quote) after++
            return minOf(after + 1, to)
        }
        while (after < to && !isSpace(bytes[after]) && bytes[after] != GREATER) after++
        return after
    }

    private fun entity(bytes: ByteArray, start: Int, to: Int, markup: BooleanArray, base: Int): Int {
        var index = start + 1
        val numeric = index < to && bytes[index] == HASH
        if (numeric) {
            index++
            if (index < to && (bytes[index] == LOWER_X || bytes[index] == UPPER_X)) index++
        }
        val nameStart = index
        while (index < to && index - nameStart < MAX_ENTITY && isAsciiAlphanumeric(bytes[index])) index++
        if (index == nameStart || index >= to) return start + 1
        val end = when {
            bytes[index] == SEMICOLON -> index + 1
            bytes[index] == BACKSLASH && index + 1 < to && bytes[index + 1] == SEMICOLON -> index + 2
            else -> return start + 1
        }
        if (numeric) {
            mark(markup, base, start, nameStart)
            mark(markup, base, index, end)
        } else {
            mark(markup, base, start, end)
        }
        return end
    }

    private fun mark(markup: BooleanArray, base: Int, from: Int, to: Int) {
        markup.fill(true, from - base, to - base)
    }

    private fun startsWith(bytes: ByteArray, index: Int, to: Int, prefix: String): Boolean {
        if (to - index < prefix.length) return false
        for (offset in prefix.indices) {
            if (bytes[index + offset].toInt() != prefix[offset].code) return false
        }
        return true
    }

    private fun startsTag(byte: Byte): Boolean =
        isAsciiLetter(byte) || byte == SLASH || byte == EXCLAMATION || byte == QUESTION

    private fun isTagNameByte(byte: Byte): Boolean =
        isAsciiAlphanumeric(byte) || byte == HYPHEN || byte == COLON || byte == UNDERSCORE || byte == SLASH ||
            byte == EXCLAMATION || byte == QUESTION

    private fun isAsciiLetter(byte: Byte): Boolean = byte in UPPER_A..UPPER_Z || byte in LOWER_A..LOWER_Z

    private fun isAsciiAlphanumeric(byte: Byte): Boolean = isAsciiLetter(byte) || byte in DIGIT_ZERO..DIGIT_NINE

    private fun isSpace(byte: Byte): Boolean = byte == SPACE || byte == HTAB

    private companion object {
        private const val TEXT = 0
        private const val CARET = 1
        private const val GRAM = 4
        private const val MAX_ATTEMPTS = 16
        private const val MAX_UTF8 = 4
        private const val MAX_ENTITY = 32
        private const val SUPPLEMENTARY_BASE = 0x10000
        private const val BASE64_MASK = 63
        private const val BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        private const val COMMENT_OPEN = "<!--"
        private const val COMMENT_CLOSE = "-->"

        private const val HTAB: Byte = 0x09
        private const val SPACE: Byte = 0x20
        private const val EXCLAMATION: Byte = 0x21
        private const val DQUOTE: Byte = 0x22
        private const val HASH: Byte = 0x23
        private const val AMPERSAND: Byte = 0x26
        private const val APOSTROPHE: Byte = 0x27
        private const val COMMA: Byte = 0x2C
        private const val HYPHEN: Byte = 0x2D
        private const val SLASH: Byte = 0x2F
        private const val DIGIT_ZERO: Byte = 0x30
        private const val DIGIT_NINE: Byte = 0x39
        private const val COLON: Byte = 0x3A
        private const val SEMICOLON: Byte = 0x3B
        private const val LESS: Byte = 0x3C
        private const val EQUALS: Byte = 0x3D
        private const val GREATER: Byte = 0x3E
        private const val QUESTION: Byte = 0x3F
        private const val UPPER_A: Byte = 0x41
        private const val UPPER_N: Byte = 0x4E
        private const val UPPER_X: Byte = 0x58
        private const val UPPER_Z: Byte = 0x5A
        private const val BACKSLASH: Byte = 0x5C
        private const val CARET_BYTE: Byte = 0x5E
        private const val UNDERSCORE: Byte = 0x5F
        private const val LOWER_A: Byte = 0x61
        private const val LOWER_N: Byte = 0x6E
        private const val LOWER_X: Byte = 0x78
        private const val LOWER_Z: Byte = 0x7A
        private const val INVALID_BYTE: Byte = -1
        private const val TRUNCATED_THREE: Byte = -31
        private const val TRUNCATED_FOUR: Byte = -15
        private const val CONTINUATION: Byte = -128

        private val TEXT_ESCAPES = byteArrayOf(LOWER_N, UPPER_N, COMMA, SEMICOLON, BACKSLASH)
        private val CARET_ESCAPES = byteArrayOf(LOWER_N, CARET_BYTE, APOSTROPHE)
        private val ATTRIBUTE_END = byteArrayOf(EQUALS, GREATER, SLASH, BACKSLASH)
    }
}

internal const val INVALID: Int = 0x100
internal const val UNIT_LENGTH: Int = 0xFF

internal fun utf8Unit(bytes: ByteArray, index: Int, end: Int): Int {
    val lead = bytes[index].toInt() and 0xFF
    if (lead < 0x80) return 1
    val info = utf8LeadInfo(lead)
    if (info == 0) return 1 or INVALID
    var need = utf8LeadNeed(info)
    var low = utf8LeadLow(info)
    var high = utf8LeadHigh(info)
    var at = index + 1
    while (need > 0 && at < end) {
        val next = bytes[at].toInt() and 0xFF
        if (next < low || next > high) break
        low = CONTINUATION_LOW
        high = CONTINUATION_HIGH
        need--
        at++
    }
    return if (need > 0) (at - index) or INVALID else at - index
}

internal fun decode(bytes: ByteArray, index: Int, length: Int): Int {
    val lead = bytes[index].toInt() and 0xFF
    if (length == 1) return lead
    var codePoint = lead and (0x7F ushr length)
    for (offset in 1 until length) codePoint = (codePoint shl 6) or (bytes[index + offset].toInt() and 0x3F)
    return codePoint
}

private const val CONTINUATION_LOW = 0x80
private const val CONTINUATION_HIGH = 0xBF

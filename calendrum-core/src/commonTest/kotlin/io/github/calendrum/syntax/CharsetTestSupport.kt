package io.github.calendrum.syntax

import io.github.calendrum.Problem

// Test encoders written from the structure of the encodings, not copied from the decoder tables: windows-1251 letters
// are contiguous from 0xC0 and 0x80..0xBF is a list; KOI8-R letters follow the order юабцдефгхийклмнопярстужвьызшэщчъ,
// lower case from 0xC0 and upper case from 0xE0; windows-1252 is Latin-1 except 0x80..0x9F. U+FFFF marks undefined
// bytes and never matches.
internal object TestCharsets {
    private const val CP1251_HIGH: String =
        "ЂЃ‚ѓ„…†‡€‰Љ‹ЊЌЋЏ" +
            "ђ‘’“”•–—\uFFFF™љ›њќћџ" +
            "\u00A0ЎўЈ¤Ґ¦§Ё©Є«¬\u00AD®Ї" +
            "°±Ііґµ¶·ё№є»јЅѕї"
    private const val KOI8_LETTERS: String = "юабцдефгхийклмнопярстужвьызшэщчъ"
    private const val CP1252_C1: String = "€\uFFFF‚ƒ„…†‡ˆ‰Š‹Œ\uFFFFŽ\uFFFF\uFFFF‘’“”•–—˜™š›œ\uFFFFžŸ"

    val singleByte: List<String> = listOf("windows-1251", "KOI8-R", "windows-1252", "ISO-8859-1")

    val wide: List<String> = listOf("UTF-16LE", "UTF-16BE", "UTF-32LE", "UTF-32BE")

    fun encode(text: String, charset: String): ByteArray = when (charset) {
        "UTF-8" -> text.encodeToByteArray()
        "UTF-16LE", "UTF-16BE" -> utf16(text, charset == "UTF-16BE")
        "UTF-32LE", "UTF-32BE" -> utf32(text, charset == "UTF-32BE")
        else -> ByteArray(text.length) { byte(text[it], charset) }
    }

    fun bom(charset: String): ByteArray = when (charset) {
        "UTF-8" -> bytesOf(0xEF, 0xBB, 0xBF)
        "UTF-16LE" -> bytesOf(0xFF, 0xFE)
        "UTF-16BE" -> bytesOf(0xFE, 0xFF)
        "UTF-32LE" -> bytesOf(0xFF, 0xFE, 0x00, 0x00)
        "UTF-32BE" -> bytesOf(0x00, 0x00, 0xFE, 0xFF)
        else -> error("no byte order mark for $charset")
    }

    fun canEncode(text: String, charset: String): Boolean = text.all { byteOrNull(it, charset) != null }

    private fun byte(char: Char, charset: String): Byte =
        byteOrNull(char, charset) ?: error("'$char' (U+${char.code.toString(16)}) is not in $charset")

    private fun byteOrNull(char: Char, charset: String): Byte? {
        val code = char.code
        if (code < 0x80) return code.toByte()
        if (char == '\uFFFF') return null
        val high = when (charset) {
            "windows-1251" -> when (char) {
                in 'А'..'я' -> 0xC0 + (char - 'А')
                else -> CP1251_HIGH.indexOf(char).let { if (it < 0) -1 else 0x80 + it }
            }

            "KOI8-R" -> when {
                char == 'ё' -> 0xA3
                char == 'Ё' -> 0xB3
                char == '\u00A0' -> 0x9A
                char == '°' -> 0x9C
                char in KOI8_LETTERS -> 0xC0 + KOI8_LETTERS.indexOf(char)
                char.lowercaseChar() in KOI8_LETTERS -> 0xE0 + KOI8_LETTERS.indexOf(char.lowercaseChar())
                else -> -1
            }

            "windows-1252" -> when (code) {
                in 0xA0..0xFF -> code
                else -> CP1252_C1.indexOf(char).let { if (it < 0) -1 else 0x80 + it }
            }

            "ISO-8859-1" -> if (code <= 0xFF) code else -1

            else -> -1
        }
        return if (high < 0) null else high.toByte()
    }

    private fun utf16(text: String, bigEndian: Boolean): ByteArray {
        val out = ByteArray(text.length * 2)
        for ((index, char) in text.withIndex()) {
            val high = (char.code shr 8).toByte()
            val low = char.code.toByte()
            out[2 * index] = if (bigEndian) high else low
            out[2 * index + 1] = if (bigEndian) low else high
        }
        return out
    }

    private fun utf32(text: String, bigEndian: Boolean): ByteArray {
        val points = ArrayList<Int>()
        var i = 0
        while (i < text.length) {
            val char = text[i]
            if (char.isHighSurrogate() && i + 1 < text.length && text[i + 1].isLowSurrogate()) {
                points += 0x10000 + ((char.code - 0xD800) shl 10) + (text[i + 1].code - 0xDC00)
                i += 2
            } else {
                points += char.code
                i++
            }
        }
        return wideUnits(points, bigEndian)
    }

    fun wideUnits(points: List<Int>, bigEndian: Boolean): ByteArray {
        val out = ByteArray(points.size * 4)
        for ((index, point) in points.withIndex()) {
            for (k in 0 until 4) {
                val byte = (point ushr (8 * k)).toByte()
                out[4 * index + if (bigEndian) 3 - k else k] = byte
            }
        }
        return out
    }
}

internal class CharsetRead(
    val events: List<Event>,
    val problems: List<Problem>,
    val lexerCharset: InputCharset,
    val source: Int,
    val confidence: Int,
) {
    val codes: List<String> get() = problems.map { it.id }
}

internal fun readWithCharset(
    input: ByteArray,
    charset: String?,
    selection: ContentSelection = ContentSelection.Full,
    limits: ParseLimits = ParseLimits.Default,
): CharsetRead {
    val problems = mutableListOf<Problem>()
    val reader = ContentReader(input, 0, input.size, selection, limits, { problems += it }, true, charset)
    val events = mutableListOf<Event>()
    var calls = 0
    while (true) {
        calls++
        check(calls <= 4 * input.size + 8) { "reader does not terminate" }
        if (reader.next() == ContentReader.END_OF_INPUT) break
        events += reader.snapshot()
    }
    return CharsetRead(events, problems, reader.decodedCharset, reader.charsetSource, reader.charsetConfidence)
}

internal fun decode(input: ByteArray, charset: String?): Pair<DecodedInput, List<Problem>> {
    val problems = mutableListOf<Problem>()
    val decoded = decodeInput(input, charset) { problems += it }
    return decoded to problems
}

internal fun DecodedInput.text(): String = bytes.decodeToString(offset, offset + length)

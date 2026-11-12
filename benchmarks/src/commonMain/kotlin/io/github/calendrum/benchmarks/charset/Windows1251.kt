package io.github.calendrum.benchmarks.charset

// An encoder written independently of the decoder tables: А..я are contiguous from 0xC0, Ё and ё are 0xA8 and 0xB8,
// plus the few symbols the corpus uses; everything else (emoji, Japanese, Latin letters with diacritics) becomes '?', a
// surrogate pair one '?'.
internal object Windows1251 {
    private const val SYMBOLS: String = "\u2014\u2013\u00AB\u00BB\u2116\u2026\u201C\u201D\u00A0"
    private val symbolBytes: IntArray = intArrayOf(0x97, 0x96, 0xAB, 0xBB, 0xB9, 0x85, 0x93, 0x94, 0xA0)

    fun encode(text: String): ByteArray {
        val lossy = lossy(text)
        return ByteArray(lossy.length) { byteOf(lossy[it]).toByte() }
    }

    fun lossy(text: String): String {
        val out = StringBuilder(text.length)
        var index = 0
        while (index < text.length) {
            val char = text[index]
            if (char.isHighSurrogate() && index + 1 < text.length && text[index + 1].isLowSurrogate()) {
                out.append(UNMAPPABLE)
                index += 2
                continue
            }
            out.append(if (byteOf(char) < 0) UNMAPPABLE else char)
            index++
        }
        return out.toString()
    }

    private fun byteOf(char: Char): Int = when {
        char.code < ASCII_LIMIT -> char.code
        char in CYRILLIC_FIRST..CYRILLIC_LAST -> CYRILLIC_BYTE + (char - CYRILLIC_FIRST)
        char == CAPITAL_IO -> CAPITAL_IO_BYTE
        char == SMALL_IO -> SMALL_IO_BYTE
        else -> SYMBOLS.indexOf(char).let { if (it < 0) -1 else symbolBytes[it] }
    }

    private const val ASCII_LIMIT = 0x80
    private const val CYRILLIC_FIRST = 'А'
    private const val CYRILLIC_LAST = 'я'
    private const val CYRILLIC_BYTE = 0xC0
    private const val CAPITAL_IO = 'Ё'
    private const val SMALL_IO = 'ё'
    private const val CAPITAL_IO_BYTE = 0xA8
    private const val SMALL_IO_BYTE = 0xB8
    private const val UNMAPPABLE = '?'
}

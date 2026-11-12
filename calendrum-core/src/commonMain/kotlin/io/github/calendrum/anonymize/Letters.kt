package io.github.calendrum.anonymize

// Replacement classes. Latin and Cyrillic letters are drawn from strings where letters repeat by frequency (English;
// the Russian national corpus), so the charset heuristic sees realistic text; accented Latin letters become Latin-1
// letters and Cyrillic ones Russian letters, which windows-1252, windows-1251 and KOI8-R can encode. Han, Hangul,
// Hiragana and Katakana get any letter of their whole range, so a replacement does not tell which neighbourhood the
// source character came from. Other letters get a letter of the same case from the unbroken run of such letters around
// the source inside its 128-code-point block: runs of different scripts are separated by marks or punctuation, and
// 0x80, 0x800 and 0x10000 are multiples of 128, so the UTF-8 length stays. U+00AA, U+00B5 and U+00BA stay: the micro
// sign is in windows-1251, the Latin-1 letters are not.
internal class Letters {
    private val runs = HashMap<Int, IntArray>()

    fun replacement(codePoint: Int, random: RandomStream): Int = when {
        codePoint < ASCII_END -> ascii(codePoint, random)

        codePoint < SUPPLEMENTARY_BASE -> basic(codePoint, random)

        codePoint in EMOJI_FIRST..EMOJI_LAST -> EMOTICONS_FIRST + random.nextInt(EMOTICONS_COUNT)

        codePoint in HAN_EXTENSION_FIRST..HAN_EXTENSION_LAST -> range(
            HAN_EXTENSION_B_FIRST,
            HAN_EXTENSION_B_LAST,
            random,
        )

        else -> (codePoint and WINDOW_MASK) + random.nextInt(WINDOW_SIZE)
    }

    private fun ascii(codePoint: Int, random: RandomStream): Int = when (codePoint.toChar()) {
        in '0'..'9' -> '0'.code + random.nextInt(DIGITS)
        in 'a'..'z' -> pick(LATIN_LOWER, random)
        in 'A'..'Z' -> pick(LATIN_UPPER, random)
        else -> codePoint
    }

    private fun basic(codePoint: Int, random: RandomStream): Int {
        val char = codePoint.toChar()
        if (char.isDigit()) return codePoint - char.digitToInt() + random.nextInt(DIGITS)
        if (!char.isLetter() || codePoint == FEMININE_ORDINAL || codePoint == MICRO || codePoint == MASCULINE_ORDINAL) {
            return codePoint
        }
        val upper = char.isUpperCase()
        return when (codePoint) {
            in LATIN_FIRST..LATIN_LAST -> pick(if (upper) LATIN1_UPPER else LATIN1_LOWER, random)
            in CYRILLIC_FIRST..CYRILLIC_LAST -> pick(if (upper) CYRILLIC_UPPER else CYRILLIC_LOWER, random)
            in GREEK_FIRST..GREEK_LAST -> pick(if (upper) GREEK_UPPER else GREEK_LOWER, random)
            in HAN_FIRST..HAN_LAST -> range(HAN_BASIC_FIRST, HAN_LAST, random)
            in HANGUL_FIRST..HANGUL_LAST -> range(HANGUL_FIRST, HANGUL_LAST, random)
            in HIRAGANA_FIRST..HIRAGANA_LAST -> range(HIRAGANA_FIRST, HIRAGANA_LAST, random)
            in KATAKANA_FIRST..KATAKANA_LAST -> range(KATAKANA_FIRST, KATAKANA_LAST, random)
            in KANA_BLOCK_FIRST..KANA_BLOCK_LAST -> codePoint
            else -> fromRun(codePoint, category(char), random)
        }
    }

    private fun fromRun(codePoint: Int, category: Int, random: RandomStream): Int {
        val base = codePoint and WINDOW_MASK
        var first = codePoint
        while (first > base && isLetter(first - 1, category)) first--
        val candidates = runs.getOrPut((first shl 2) or category) {
            var last = codePoint
            while (last < base + WINDOW_SIZE - 1 && isLetter(last + 1, category)) last++
            IntArray(last - first + 1) { first + it }
        }
        return candidates[random.nextInt(candidates.size)]
    }

    private fun isLetter(codePoint: Int, category: Int): Boolean {
        val char = codePoint.toChar()
        return char.isLetter() && category(char) == category
    }

    private fun range(first: Int, last: Int, random: RandomStream): Int = first + random.nextInt(last - first + 1)

    private fun category(char: Char): Int = when {
        char.isUpperCase() -> UPPER
        char.isLowerCase() -> LOWER
        else -> OTHER
    }

    private fun pick(alphabet: String, random: RandomStream): Int = alphabet[random.nextInt(alphabet.length)].code

    private companion object {
        private const val ASCII_END = 0x80
        private const val SUPPLEMENTARY_BASE = 0x10000
        private const val DIGITS = 10
        private const val WINDOW_SIZE = 128
        private const val WINDOW_MASK = -WINDOW_SIZE
        private const val EMOJI_FIRST = 0x1F000
        private const val EMOJI_LAST = 0x1FAFF
        private const val EMOTICONS_FIRST = 0x1F600
        private const val EMOTICONS_COUNT = 80
        private const val FEMININE_ORDINAL = 0xAA
        private const val MICRO = 0xB5
        private const val MASCULINE_ORDINAL = 0xBA
        private const val LATIN_FIRST = 0xC0
        private const val LATIN_LAST = 0x24F
        private const val GREEK_FIRST = 0x370
        private const val GREEK_LAST = 0x3FF
        private const val CYRILLIC_FIRST = 0x400
        private const val CYRILLIC_LAST = 0x52F
        private const val KANA_BLOCK_FIRST = 0x3040
        private const val HIRAGANA_FIRST = 0x3041
        private const val HIRAGANA_LAST = 0x3096
        private const val KATAKANA_FIRST = 0x30A1
        private const val KATAKANA_LAST = 0x30FA
        private const val KANA_BLOCK_LAST = 0x30FF
        private const val HAN_FIRST = 0x3400
        private const val HAN_BASIC_FIRST = 0x4E00
        private const val HAN_LAST = 0x9FFF
        private const val HANGUL_FIRST = 0xAC00
        private const val HANGUL_LAST = 0xD7A3
        private const val HAN_EXTENSION_FIRST = 0x20000
        private const val HAN_EXTENSION_LAST = 0x3134F
        private const val HAN_EXTENSION_B_FIRST = 0x20000
        private const val HAN_EXTENSION_B_LAST = 0x2A6DF
        private const val UPPER = 1
        private const val LOWER = 2
        private const val OTHER = 0

        private const val LATIN_LOWER =
            "eeeeeeeeeeeeettttttttttaaaaaaaaaooooooooiiiiiiinnnnnnnsssssshhhhhhrrrrrrddddllllcccuuummwwffggyyppbvkjxqz"
        private const val LATIN_UPPER =
            "EEEEEEEEEEEEETTTTTTTTTTAAAAAAAAAOOOOOOOOIIIIIIINNNNNNNSSSSSSHHHHHHRRRRRRDDDDLLLLCCCUUUMMWWFFGGYYPPBVKJXQZ"
        private const val LATIN1_LOWER = "éééééééèèèàààçôööüüääñíóúáâêîûëïøåæßãõýÿðþìòù"
        private const val LATIN1_UPPER = "ÉÉÉÉÈÈÀÀÇÔÖÖÜÜÄÄÑÍÓÚÁÂÊÎÛËÏØÅÆÃÕÝÐÞÌÒÙ"
        private const val CYRILLIC_LOWER =
            "ооооооооооооееееееееееааааааааааиииииииинннннннттттттттссссссссррррррввввввллллллкккккммммммдддддппппп" +
                "ууууяяяыыыьььгггзззббббчччййххжжшюцщэфъё"
        private const val CYRILLIC_UPPER =
            "ООООООООООООЕЕЕЕЕЕЕЕЕЕААААААААААИИИИИИИИНННННННТТТТТТТТССССССССРРРРРРВВВВВВЛЛЛЛЛЛКККККММММММДДДДДППППП" +
                "УУУУЯЯЯЫЫЫЬЬЬГГГЗЗЗББББЧЧЧЙЙХХЖЖШЮЦЩЭФЪЁ"
        private const val GREEK_LOWER = "αβγδεζηθικλμνξοπρστυφχψω"
        private const val GREEK_UPPER = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ"
    }
}

internal fun isWordCodePoint(codePoint: Int): Boolean = codePoint < 0x10000 && codePoint.toChar().isLetterOrDigit()

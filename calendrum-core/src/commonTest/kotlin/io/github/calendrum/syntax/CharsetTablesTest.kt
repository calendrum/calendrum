package io.github.calendrum.syntax

import kotlin.math.log2
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals

class CharsetTablesTest {
    private val russianLetters = "оеаинтсрвлкмдпуяыьгзбчйхжшюцщэфъё"
    private val russianVowels = "аеёиоуыэюя"
    private val russianSigns = "ьъ"
    private val latinCommon = "éèàçôêâüöäßñáíóúëïîûùåøæãõ"
    private val latinMedium = "ìòÿœšžð"
    private val symbols =
        " «»–—…‘’“”„‚•€№°±" +
            "·§©®™×÷¤¦µ¶†‡‰‹›¢" +
            "£¥¡¿ªº¹²³¼½¾¨¯´¸ˆ" +
            "˜­"
    private val russianFrequencies = intArrayOf(
        1097, 845, 801, 735, 670, 626, 547, 473, 454, 440, 349, 321, 298, 281, 262, 201, 190,
        174, 170, 165, 159, 144, 121, 97, 94, 73, 64, 48, 36, 32, 26, 4, 4,
    )

    private fun kindOf(char: Char): Int = when {
        char in symbols -> 1
        char.isLetter() && char.isUpperCase() -> 2
        char.isLetter() && char.isLowerCase() -> 3
        else -> 0
    }

    private fun phoneticOf(lower: Char): Int {
        val position = russianLetters.indexOf(lower)
        return when {
            position < 0 -> 4
            lower in russianVowels -> 0
            lower in russianSigns -> 2
            lower == 'й' -> 3
            else -> 1
        }
    }

    private fun cyrillicCostOf(lower: Char): Int {
        val position = russianLetters.indexOf(lower)
        return if (position < 0) 110 else (-log2(russianFrequencies[position] / 10_000.0) * 10).roundToInt()
    }

    private fun latinCostOf(lower: Char): Int = when (lower) {
        in latinCommon -> 40
        in latinMedium -> 65
        else -> 90
    }

    @Test
    fun packedHeuristicTablesEqualTheirDefinition() {
        assertEquals(
            listOf(InputCharset.Windows1251, InputCharset.Windows1252, InputCharset.Koi8R),
            SingleByteHeuristic.candidates.toList(),
        )
        for ((index, charset) in SingleByteHeuristic.candidates.withIndex()) {
            val table = SingleByteCharsets.table(charset)
            val cyrillic = charset != InputCharset.Windows1252
            for (byte in 0 until HIGH_BYTES) {
                val char = table[byte]
                val kind = kindOf(char)
                var phonetic = 4
                var cost = 0
                if (kind == 2 || kind == 3) {
                    val lower = char.lowercaseChar()
                    if (cyrillic) {
                        phonetic = phoneticOf(lower)
                        cost = cyrillicCostOf(lower)
                    } else {
                        cost = latinCostOf(lower)
                    }
                }
                val where = "$charset byte ${byte + HIGH_BYTES} ($char)"
                assertEquals(kind, SingleByteHeuristic.kinds[index][byte].toInt(), "kind of $where")
                assertEquals(phonetic, SingleByteHeuristic.phonetic[index][byte].toInt(), "sound of $where")
                assertEquals(cost, SingleByteHeuristic.costs[index][byte], "cost of $where")
            }
        }
    }
}

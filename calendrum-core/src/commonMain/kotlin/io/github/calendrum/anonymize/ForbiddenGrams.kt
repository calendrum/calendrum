package io.github.calendrum.anonymize

// The first four letters or digits (in lower case) of every word of four or more characters in a replaced value. A word
// can only reappear in the output if its first four characters do, so forbidding them in generated text and hex tokens
// is enough. Keys are four 16-bit chars in a Long; open addressing with Fibonacci hashing.
internal class ForbiddenGrams {
    private var table = LongArray(INITIAL_CAPACITY)
    private var size = 0

    val isEmpty: Boolean get() = size == 0

    fun addWords(text: String, escapes: Boolean) {
        var run = 0
        var first = 0
        var second = 0
        var third = 0
        var index = 0
        while (index < text.length) {
            val char = text[index]
            if (escapes && char == '\\' && index + 1 < text.length && text[index + 1] in ESCAPED) {
                run = 0
                index += 2
                continue
            }
            if (!char.isLetterOrDigit()) {
                run = 0
                index++
                continue
            }
            val lower = char.lowercaseChar().code
            when (run) {
                0 -> first = lower
                1 -> second = lower
                2 -> third = lower
                3 -> add(key(first, second, third, lower))
            }
            run++
            index++
        }
    }

    fun contains(first: Int, second: Int, third: Int, fourth: Int): Boolean {
        if (size == 0) return false
        val key = key(first, second, third, fourth)
        var slot = slot(key)
        while (true) {
            val stored = table[slot]
            if (stored == EMPTY) return false
            if (stored == key) return true
            slot = (slot + 1) and (table.size - 1)
        }
    }

    fun occursIn(token: String): Boolean {
        if (size == 0) return false
        for (index in 0..token.length - GRAM) {
            val a = token[index]
            val b = token[index + 1]
            val c = token[index + 2]
            val d = token[index + 3]
            if (!a.isLetterOrDigit() || !b.isLetterOrDigit() || !c.isLetterOrDigit() || !d.isLetterOrDigit()) continue
            val found = contains(
                a.lowercaseChar().code,
                b.lowercaseChar().code,
                c.lowercaseChar().code,
                d.lowercaseChar().code,
            )
            if (found) return true
        }
        return false
    }

    private fun add(key: Long) {
        if ((size + 1) * 2 > table.size) grow()
        var slot = slot(key)
        while (true) {
            val stored = table[slot]
            if (stored == key) return
            if (stored == EMPTY) {
                table[slot] = key
                size++
                return
            }
            slot = (slot + 1) and (table.size - 1)
        }
    }

    private fun grow() {
        val old = table
        table = LongArray(old.size * 2)
        size = 0
        for (key in old) {
            if (key != EMPTY) add(key)
        }
    }

    private fun slot(key: Long): Int = ((key * MIX) ushr (Long.SIZE_BITS - bits())).toInt()

    private fun bits(): Int = Int.SIZE_BITS - 1 - table.size.countLeadingZeroBits()

    private companion object {
        private const val INITIAL_CAPACITY = 64
        private const val GRAM = 4
        private const val EMPTY = 0L
        private const val MIX = -0x61c8864680b583ebL
        private const val ESCAPED = "nN,;\\"

        fun key(first: Int, second: Int, third: Int, fourth: Int): Long =
            (first.toLong() shl 48) or (second.toLong() shl 32) or (third.toLong() shl 16) or fourth.toLong()
    }
}

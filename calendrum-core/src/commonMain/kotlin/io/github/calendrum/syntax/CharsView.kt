package io.github.calendrum.syntax

// A `CharSequence` over a reusable char array: the value view of the cursor and the chunk that the writer appends to an
// `Appendable`. `subSequence` and `toString` copy.
internal class CharsView : CharSequence {
    var chars: CharArray = EMPTY_CHARS
        private set
    private var count = 0

    override val length: Int get() = count

    fun reset(chars: CharArray, count: Int) {
        this.chars = chars
        this.count = count
    }

    override fun get(index: Int): Char {
        if (index !in 0 until count) throw IndexOutOfBoundsException("index $index, length $count")
        return chars[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        if (startIndex < 0 || startIndex > endIndex || endIndex > count) {
            throw IndexOutOfBoundsException("range $startIndex..$endIndex, length $count")
        }
        return chars.concatToString(startIndex, endIndex)
    }

    override fun toString(): String = chars.concatToString(0, count)
}

private val EMPTY_CHARS = CharArray(0)

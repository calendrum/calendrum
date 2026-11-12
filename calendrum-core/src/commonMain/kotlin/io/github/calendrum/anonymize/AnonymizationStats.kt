package io.github.calendrum.anonymize

internal class AnonymizationStats {
    val properties: MutableMap<String, IntArray> = HashMap()
    val parameters: MutableMap<String, IntArray> = HashMap()
    val unrecognizedDates: MutableMap<String, Int> = HashMap()
    val notTokens: MutableMap<String, Int> = HashMap()
    var rulesMayChange: Int = 0
    var editedInPlace: Int = 0
    var rebuilt: Int = 0
    private var starts = LongArray(INITIAL_SPANS)
    private var ends = LongArray(INITIAL_SPANS)
    private var spanCount = 0
    private var sorted = true

    fun property(name: String, outcome: Int) {
        properties.getOrPut(name) { IntArray(OUTCOMES) }[outcome]++
    }

    fun parameter(name: String, outcome: Int) {
        parameters.getOrPut(name) { IntArray(OUTCOMES) }[outcome]++
    }

    fun unrecognizedDate(name: String, count: Int) {
        unrecognizedDates[name] = (unrecognizedDates[name] ?: 0) + count
    }

    fun notToken(name: String) {
        notTokens[name] = (notTokens[name] ?: 0) + 1
    }

    fun changed(start: Long, end: Long) {
        if (spanCount == starts.size) {
            starts = starts.copyOf(spanCount * 2)
            ends = ends.copyOf(spanCount * 2)
        }
        starts[spanCount] = start
        ends[spanCount] = end
        spanCount++
        sorted = false
    }

    // Spans of changed lines never overlap, so sorting their starts and ends separately keeps the pairs.
    fun isInsideChanged(offset: Long): Boolean {
        if (!sorted) {
            starts.sort(0, spanCount)
            ends.sort(0, spanCount)
            sorted = true
        }
        var low = 0
        var high = spanCount - 1
        var found = -1
        while (low <= high) {
            val middle = (low + high) ushr 1
            if (starts[middle] <= offset) {
                found = middle
                low = middle + 1
            } else {
                high = middle - 1
            }
        }
        return found >= 0 && offset <= ends[found]
    }

    companion object {
        const val REPLACED: Int = 0
        const val SHIFTED: Int = 1
        const val KEPT: Int = 2
        private const val OUTCOMES = 3
        private const val INITIAL_SPANS = 64
    }
}

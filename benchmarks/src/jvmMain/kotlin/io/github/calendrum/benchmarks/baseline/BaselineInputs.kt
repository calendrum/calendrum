package io.github.calendrum.benchmarks.baseline

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import java.time.Instant

internal val baselineCorpora: List<String> = Corpus.names

internal val rruleCorpora: List<String> = listOf(Corpus.PERSONAL, Corpus.UNIVERSITY)

internal val expectedOccurrenceTotals: Map<String, Int> = mapOf(Corpus.PERSONAL to 8_614, Corpus.UNIVERSITY to 175_200)

internal val rruleWindowStart: Instant = Instant.parse("2026-01-01T00:00:00Z")

internal val rruleWindowEnd: Instant = Instant.parse("2036-01-01T00:00:00Z")

internal fun interface ValueSink {
    fun consume(value: Any?)
}

internal fun baselineInputs(corpus: String): List<ByteArray> {
    val bytes = corpusBytes(corpus)
    return if (corpus == Corpus.INVITE_BATCH) splitCalendars(bytes) else listOf(bytes)
}

internal fun splitCalendars(bytes: ByteArray): List<ByteArray> {
    val marker = "END:VCALENDAR\r\n".encodeToByteArray()
    val parts = ArrayList<ByteArray>()
    var start = 0
    var index = 0
    while (index <= bytes.size - marker.size) {
        val lineStart = index == 0 || bytes[index - 1] == '\n'.code.toByte()
        if (lineStart && bytes.startsWith(marker, index)) {
            val end = index + marker.size
            parts += bytes.copyOfRange(start, end)
            start = end
            index = end
        } else {
            index++
        }
    }
    check(start == bytes.size) { "Trailing bytes after the last END:VCALENDAR" }
    return parts
}

private fun ByteArray.startsWith(prefix: ByteArray, offset: Int): Boolean {
    for (i in prefix.indices) {
        if (this[offset + i] != prefix[i]) return false
    }
    return true
}

internal fun requireRruleCorpus(corpus: String) {
    require(corpus in rruleCorpora) { "baseline-rrule runs on ${rruleCorpora.joinToString()}, not on $corpus" }
}

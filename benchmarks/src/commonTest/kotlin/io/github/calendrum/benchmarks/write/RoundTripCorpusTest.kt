package io.github.calendrum.benchmarks.write

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.parser
import io.github.calendrum.syntax.WriteOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

// The exit criterion of the syntax layer: every corpus written byte-stably and canonically is the corpus byte for byte,
// also from a tree without the source; the text output is the corpus as a string; with `timeZonesFirst` the moved
// corpus is written back as the original.
class RoundTripCorpusTest {
    @Test
    fun invite() {
        assertRoundTrip(Corpus.INVITE)
    }

    @Test
    fun personal() {
        assertRoundTrip(Corpus.PERSONAL)
    }

    @Test
    fun timeZonesMoveToTheFront() {
        assertTimeZonesMoveToTheFront(Corpus.PERSONAL)
    }
}

internal fun assertRoundTrip(corpus: String) {
    val bytes = corpusBytes(corpus)
    val tree = ICal.parse(bytes).components
    assertNull(firstDifference(bytes, ICal.writer().encodeToByteArray(tree)), "$corpus: byte-stable")
    assertNull(firstDifference(bytes, canonicalWriter.encodeToByteArray(tree)), "$corpus: canonical")
    val text = ICal.writer().encodeToString(tree)
    assertTrue(text == bytes.decodeToString(), "$corpus: byte-stable text")
    val copied = ICal.parser { retainSource = false }.parse(bytes).components
    assertNull(firstDifference(bytes, ICal.writer().encodeToByteArray(copied)), "$corpus: without the source")
}

internal fun assertTimeZonesMoveToTheFront(corpus: String) {
    val bytes = corpusBytes(corpus)
    for (calendar in ICal.parse(bytes).components) {
        val names = calendar.components.map { it.name }
        assertEquals(names.sortedBy { it != TIME_ZONE }, names, "$corpus: time zones come first in the corpus")
    }
    val moved = moveTimeZonesLast(bytes)
    assertEquals(bytes.size, moved.size, corpus)
    val tree = ICal.parse(moved).components
    assertTrue(tree.all { it.components.last().name == TIME_ZONE }, "$corpus: time zones are last")
    assertNull(firstDifference(moved, ICal.writer().encodeToByteArray(tree)), corpus)
    for (byteStable in listOf(true, false)) {
        val writer = ICal.writer(WriteOptions.builder().byteStable(byteStable).timeZonesFirst(true).build())
        assertNull(firstDifference(bytes, writer.encodeToByteArray(tree)), "$corpus, byteStable=$byteStable")
    }
}

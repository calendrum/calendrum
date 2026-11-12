@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.reader

import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.SplitMix64
import io.github.calendrum.benchmarks.corpus.corpusBytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal fun assertMatchesReference(corpus: String) {
    val readout = readCorpus(corpusBytes(corpus))
    val expected = expectedCounts.getValue(corpus)
    assertEquals(emptyList(), readout.problemsAboveInfo(), "$corpus: problems above Info")
    assertEquals(expected.logicalLines, readout.counts.logicalLines, "$corpus: logical lines")
    assertEquals(expected.properties, readout.counts.properties, "$corpus: properties")
    assertEquals(expected.components, readout.counts.components, "$corpus: components")
    assertEquals(expected.selectedProperties, readout.counts.selectedProperties, "$corpus: properties by name")
    assertTrue(readout.balanced, "$corpus: events must be balanced without implicit END")
    assertEquals(0, readout.unknownComponents, "$corpus: unknown component names")
    assertEquals(emptySet(), readout.otherComponents, "$corpus: components outside the reference")
    assertEquals(0, readout.unknownCountedProperties, "$corpus: counted properties must have known ids")
}

// Random masks draw from the names seen in the corpus plus a few absent ones, with a fixed SplitMix64 seed per corpus.
internal fun assertSelectionInvariant(corpus: String, seed: Long) {
    val bytes = corpusBytes(corpus)
    val seen = knownNamesIn(bytes)
    val components = seen.components + listOf("VTODO", "VJOURNAL")
    val properties = seen.properties + listOf("COMMENT", "X-WR-TIMEZONE")
    val random = SplitMix64(seed)
    val masks = listOf(Mask.schedule, Mask.syncMetadata) + List(3) { Mask.random(random, components, properties) }
    for (mask in masks) {
        val compared = assertSelectionMatchesFilter(bytes, mask)
        assertTrue(compared > 2, "$corpus: $mask compared only $compared events")
    }
}

class ReaderCorpusTest {
    @Test
    fun invite() {
        assertMatchesReference(Corpus.INVITE)
    }

    @Test
    fun personal() {
        assertMatchesReference(Corpus.PERSONAL)
    }

    @Test
    fun selectionOnPersonal() {
        assertSelectionInvariant(Corpus.PERSONAL, seed = 1)
    }
}

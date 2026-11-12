package io.github.calendrum.benchmarks.cursor

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ICalComponent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

// The cursor sees the reference counts and `readComponent` gives the tree of `parse`.
class CursorCorpusTest {
    @Test
    fun invite() {
        assertCursorMatches(Corpus.INVITE)
    }

    @Test
    fun personal() {
        assertCursorMatches(Corpus.PERSONAL)
    }
}

internal fun assertCursorMatches(corpus: String) {
    val bytes = corpusBytes(corpus)
    assertNull(cursorCounts(ICal.parser(), bytes).difference(expectedCounts.getValue(corpus)), corpus)
    val reader = ICal.parser().reader(bytes)
    val read = ArrayList<ICalComponent>()
    while (true) {
        val type = reader.next() ?: break
        assertEquals(ContentEventType.Begin, type, corpus)
        read += reader.readComponent()
    }
    val tree = ICal.parse(bytes).components
    assertEquals(tree, read, corpus)
    assertEquals(tree.map { it.span }, read.map { it.span }, corpus)
}

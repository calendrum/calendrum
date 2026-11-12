@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.writer

import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.syntax.ByteLineWriter
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

// The writer round trip of the benchmark setup on the corpora that every platform handles in a test; `huge` is checked
// by the benchmark setup itself.
class WriterCorpusTest {
    @Test
    fun invite() {
        assertWrittenBack(Corpus.INVITE)
    }

    @Test
    fun personal() {
        assertWrittenBack(Corpus.PERSONAL)
    }
}

internal fun assertWrittenBack(corpus: String) {
    val bytes = corpusBytes(corpus)
    val lines = CorpusLines.read(bytes)
    assertEquals(expectedCounts.getValue(corpus).logicalLines, lines.size, corpus)
    val writer = ByteLineWriter()
    lines.writeTo(writer)
    val written = writer.toByteArray()
    assertNull(lines.firstDifference(written), corpus)
    assertContentEquals(bytes, written, corpus)
    writer.reset()
    lines.writeTo(writer)
    assertContentEquals(written, writer.toByteArray(), corpus)
}

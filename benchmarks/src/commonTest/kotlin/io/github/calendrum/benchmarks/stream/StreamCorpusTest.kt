package io.github.calendrum.benchmarks.stream

import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ICalComponent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StreamCorpusTest {
    @Test
    fun invite() {
        assertStreamMatches(Corpus.INVITE, (1..16).toList() + LEXER_BLOCK_BYTES)
    }

    @Test
    fun personal() {
        assertStreamMatches(Corpus.PERSONAL, listOf(13, 4093, LEXER_BLOCK_BYTES))
    }
}

internal const val LEXER_BLOCK_BYTES: Int = 64 * 1024

internal class Chunks(private val bytes: ByteArray, private val chunk: Int) : ByteInput {
    private var position = 0

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (position == bytes.size) return -1
        val count = minOf(length, chunk, bytes.size - position)
        bytes.copyInto(buffer, offset, position, position + count)
        position += count
        return count
    }
}

internal fun assertStreamMatches(corpus: String, chunks: List<Int>) {
    val bytes = corpusBytes(corpus)
    val parser = ICal.parser()
    val tree = parser.parse(bytes)
    for (chunk in chunks) {
        val where = "$corpus in chunks of $chunk"
        val streamed = parser.parse(Chunks(bytes, chunk))
        assertEquals(tree.components, streamed.components, where)
        for (index in tree.components.indices) {
            assertSameSpans(
                tree.components[index],
                streamed.components[index],
                where,
            )
        }
        assertEquals(tree.problems.map { it.toString() }, streamed.problems.map { it.toString() }, where)
        assertEquals(tree.charset, streamed.charset, where)

        val fromArray = parser.components(bytes)
        val fromInput = parser.components(Chunks(bytes, chunk))
        while (fromArray.hasNext()) {
            assertTrue(fromInput.hasNext(), where)
            val expected = fromArray.next()
            val actual = fromInput.next()
            assertEquals(expected, actual, where)
            assertSameSpans(expected, actual, where)
            assertEquals(fromArray.calendarIndex, fromInput.calendarIndex, where)
            assertEquals(fromArray.calendarProperties, fromInput.calendarProperties, where)
        }
        assertFalse(fromInput.hasNext(), where)

        val cursor = parser.reader(bytes)
        val streamCursor = parser.reader(Chunks(bytes, chunk))
        while (true) {
            val type = cursor.next()
            assertEquals(type, streamCursor.next(), where)
            if (type == null) break
            assertEquals(cursor.span(), streamCursor.span(), where)
            assertEquals(cursor.name, streamCursor.name, where)
        }
    }
}

internal fun assertSameSpans(expected: ICalComponent, actual: ICalComponent, where: String) {
    val stack = ArrayList<Pair<ICalComponent, ICalComponent>>()
    stack += expected to actual
    while (stack.isNotEmpty()) {
        val (left, right) = stack.removeAt(stack.lastIndex)
        assertEquals(left.span, right.span, where)
        assertEquals(left.properties.map { it.span }, right.properties.map { it.span }, where)
        for (index in left.components.indices) stack += left.components[index] to right.components[index]
    }
}

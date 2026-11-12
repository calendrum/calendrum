package io.github.calendrum.benchmarks.adapters

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ParseResult
import kotlinx.io.buffered
import okio.buffer
import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.calendrum.kotlinxio.components as kotlinxComponents
import io.github.calendrum.kotlinxio.parse as kotlinxParse
import io.github.calendrum.okio.components as okioComponents
import io.github.calendrum.okio.parse as okioParse

// A source that hands out 1, 7, 13, 4093 or 64 KiB bytes per read must give the same components, problems, charset and
// component stream as the ByteArray, and a sink the same bytes as encodeToByteArray.
class AdaptersCorpusTest {
    @Test
    fun invite() {
        assertAdaptersMatch(Corpus.INVITE, listOf(1, 7, 4096))
    }

    @Test
    fun personal() {
        assertAdaptersMatch(Corpus.PERSONAL, listOf(13, 4093, 64 * 1024))
    }

    private fun assertAdaptersMatch(corpus: String, chunks: List<Int>) {
        val bytes = corpusBytes(corpus)
        val parser = ICal.parser()
        val tree = parser.parse(bytes)
        val stream = parser.components(bytes).asSequence().toList()
        for (chunk in chunks) {
            val where = "$corpus in chunks of $chunk"
            assertSame(tree, parser.kotlinxParse(kotlinxSource(bytes, chunk)), "$where, kotlinx-io")
            assertSame(tree, parser.okioParse(okioSource(bytes, chunk)), "$where, Okio")
            assertEquals(stream, parser.kotlinxComponents(kotlinxSource(bytes, chunk)).asSequence().toList(), where)
            assertEquals(stream, parser.okioComponents(okioSource(bytes, chunk)).asSequence().toList(), where)
        }
    }

    private fun assertSame(expected: ParseResult, actual: ParseResult, where: String) {
        assertEquals(expected.components, actual.components, where)
        assertEquals(expected.problems.map { it.toString() }, actual.problems.map { it.toString() }, where)
        assertEquals(expected.charset, actual.charset, where)
    }

    private fun kotlinxSource(bytes: ByteArray, chunk: Int): kotlinx.io.Source = KotlinxChunks(bytes, chunk).buffered()

    private fun okioSource(bytes: ByteArray, chunk: Int): okio.BufferedSource = OkioChunks(bytes, chunk).buffer()

    private class KotlinxChunks(private val bytes: ByteArray, private val chunk: Int) : kotlinx.io.RawSource {
        private var position = 0

        override fun readAtMostTo(sink: kotlinx.io.Buffer, byteCount: Long): Long {
            if (position == bytes.size) return -1
            val count = minOf(chunk.toLong(), byteCount, (bytes.size - position).toLong()).toInt()
            sink.write(bytes, position, position + count)
            position += count
            return count.toLong()
        }

        override fun close() = Unit
    }

    private class OkioChunks(private val bytes: ByteArray, private val chunk: Int) : okio.Source {
        private var position = 0

        override fun read(sink: okio.Buffer, byteCount: Long): Long {
            if (position == bytes.size) return -1
            val count = minOf(chunk.toLong(), byteCount, (bytes.size - position).toLong()).toInt()
            sink.write(bytes, position, count)
            position += count
            return count.toLong()
        }

        override fun timeout(): okio.Timeout = okio.Timeout.NONE

        override fun close() = Unit
    }
}

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.syntax.ParseResult
import kotlinx.io.RawSource
import kotlinx.io.buffered
import okio.buffer
import io.github.calendrum.kotlinxio.components as kotlinxComponents
import io.github.calendrum.kotlinxio.parse as kotlinxParse
import io.github.calendrum.okio.components as okioComponents
import io.github.calendrum.okio.parse as okioParse
import kotlinx.io.Buffer as KotlinxBuffer
import okio.Buffer as OkioBuffer

// The kotlinx-io and Okio adapters against a ByteArray: a Buffer (parsed as an array) and a source that is not a
// Buffer (read block by block, in reads of the size the first byte picks) give the same tree and the same problems,
// and the component streams the same components.
class AdaptersFuzzTest {
    @FuzzTest
    fun adapters(input: ByteArray) {
        if (input.isEmpty()) return
        val chunk = 1 + (input[0].toInt() and 0xFF)
        val bytes = input.copyOfRange(1, input.size)
        val parser = ICal.parser()
        val expected = shape(parser.parse(bytes))
        val kotlinxBuffer = KotlinxBuffer().apply { write(bytes) }
        invariant(shape(parser.kotlinxParse(kotlinxBuffer)) == expected) { "kotlinx-io Buffer differs" }
        invariant(shape(parser.kotlinxParse(kotlinxSource(bytes, chunk).buffered())) == expected) {
            "kotlinx-io source"
        }
        invariant(shape(parser.okioParse(OkioBuffer().write(bytes))) == expected) { "Okio Buffer differs" }
        invariant(shape(parser.okioParse(okioSource(bytes, chunk).buffer())) == expected) { "Okio source differs" }
        val components = parser.components(bytes).asSequence().toList()
        val fromKotlinx = parser.kotlinxComponents(kotlinxSource(bytes, chunk).buffered()).asSequence().toList()
        val fromOkio = parser.okioComponents(OkioBuffer().write(bytes)).asSequence().toList()
        invariant(fromKotlinx == components) { "kotlinx-io component stream differs" }
        invariant(fromOkio == components) { "Okio component stream differs" }
    }

    private fun shape(result: ParseResult): Pair<Any, Any> =
        result.components to result.problems.map { "${it.code.id} ${it.span}" }

    private fun kotlinxSource(bytes: ByteArray, chunk: Int): RawSource = object : RawSource {
        private var at = 0

        override fun readAtMostTo(sink: KotlinxBuffer, byteCount: Long): Long {
            if (at == bytes.size) return -1
            val count = minOf(chunk.toLong(), byteCount, (bytes.size - at).toLong()).toInt()
            sink.write(bytes, at, at + count)
            at += count
            return count.toLong()
        }

        override fun close() = Unit
    }

    private fun okioSource(bytes: ByteArray, chunk: Int): okio.Source = object : okio.Source {
        private var at = 0

        override fun read(sink: OkioBuffer, byteCount: Long): Long {
            if (at == bytes.size) return -1
            val count = minOf(chunk.toLong(), byteCount, (bytes.size - at).toLong()).toInt()
            sink.write(bytes, at, count)
            at += count
            return count.toLong()
        }

        override fun timeout(): okio.Timeout = okio.Timeout.NONE

        override fun close() = Unit
    }
}

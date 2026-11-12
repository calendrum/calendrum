package io.github.calendrum.okio

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ICalReader
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.ParseResult
import okio.Buffer
import okio.BufferedSource
import okio.Source
import okio.Timeout
import okio.buffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

// A source that hands out 1, 7, 13, 4093 or 64 KiB bytes per read must give the same components, problems, charset and
// component stream as the ByteArray, and a sink the same bytes as encodeToByteArray.
class OkioEquivalenceTest {
    private val inputs: List<ByteArray> = listOf(
        crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//t//EN",
            "BEGIN:VEVENT",
            "UID:1@example.com",
            "SUMMARY:Лекция по матанализу — ауд. 101 😀",
            "DESCRIPTION:A long description that is folded across several physical lines so that the ",
            " fold crosses block boundaries in the stream",
            "ATTENDEE;CN=\"Doe, Jane\":mailto:jane@example.com",
            "END:VEVENT",
            "BEGIN:VTODO",
            "UID:2@example.com",
            "END:VTODO",
            "END:VCALENDAR",
        ).encodeToByteArray(),
        "".encodeToByteArray(),
        "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\n".encodeToByteArray(),
        "BEGIN:VCALENDAR\r\nEND:VCALENDAR\r\nBEGIN:VCALENDAR\r\nX:1\r\nEND:VCALENDAR\r\n".encodeToByteArray(),
        "BEGIN:VCALENDAR\rX;P=\"unclosed:v\r\n NO COLON\nEND:VCALENDAR".encodeToByteArray(),
        byteArrayOf(0x42, 0x3A, 0xC3.toByte(), 0x0D, 0x0A, 0x20, 0xA9.toByte(), 0x0D, 0x0A, 0xFF.toByte()),
        byteArrayOf(0xFF.toByte(), 0xFE.toByte(), 0x42, 0x00, 0x3A, 0x00, 0x31, 0x00, 0x0D, 0x00, 0x0A, 0x00),
    )

    private val chunks = listOf(1, 7, 4096)

    @Test
    fun parsingASourceEqualsParsingTheBytes() {
        for ((index, bytes) in inputs.withIndex()) {
            for (options in listOf(ParseOptions.Default, ParseOptions.builder().retainSource(false).build())) {
                val parser = ICal.parser(options)
                val expected = parser.parse(bytes)
                for (chunk in chunks) {
                    assertSameResult(expected, parser.parse(source(bytes, chunk)), "input #$index, chunk $chunk")
                }
            }
        }
    }

    // A Buffer of up to 16 MiB is copied into an array, a larger one is read block by block (the copy
    // would hold its bytes twice); either way the result is that of the bytes.
    @Test
    fun buffersOfAnySizeEqualParsingTheBytes() {
        val event = "BEGIN:VEVENT\r\nUID:x\r\nSUMMARY:" + "s".repeat(60) + "\r\nEND:VEVENT\r\n"
        val large = "BEGIN:VCALENDAR\r\n" + event.repeat(LARGE / event.length + 1) + "END:VCALENDAR\r\n"
        for (bytes in inputs + large.encodeToByteArray()) {
            val expected = ICal.parser().parse(bytes)
            assertSameResult(
                expected,
                ICal.parser().parse(
                    Buffer().apply {
                        write(bytes)
                    },
                ),
                "a buffer of ${bytes.size}",
            )
            val stream = ICal.parser().components(Buffer().apply { write(bytes) })
            assertEquals(ICal.parser().components(bytes).asSequence().count(), stream.asSequence().count())
        }
        assertTrue(large.length > LARGE)
    }

    @Test
    fun strictModeFailsAtTheSameProblem() {
        val parser = ICal.parser(ParseOptions.builder().mode(ParseMode.Strict).build())
        for ((index, bytes) in inputs.withIndex()) {
            val expected = runCatching { parser.parse(bytes) }.exceptionOrNull() as? ICalParseException ?: continue
            for (chunk in chunks) {
                val actual = assertFailsWith<ICalParseException>("input #$index") { parser.parse(source(bytes, chunk)) }
                assertEquals(describe(expected.problems), describe(actual.problems), "input #$index, chunk $chunk")
            }
        }
    }

    @Test
    fun theCursorAndTheComponentStreamSeeTheSameEvents() {
        val parser = ICal.parser()
        for ((index, bytes) in inputs.withIndex()) {
            val expected = events(parser.reader(bytes))
            val components = parser.components(bytes).asSequence().toList()
            for (chunk in chunks) {
                assertEquals(expected, events(parser.reader(source(bytes, chunk))), "input #$index, chunk $chunk")
                assertEquals(components, parser.components(source(bytes, chunk)).asSequence().toList(), "input #$index")
            }
        }
    }

    @Test
    fun writingToASinkEqualsWritingBytes() {
        val writer = ICal.writer()
        for (bytes in inputs) {
            val components = ICal.parse(bytes).components
            val buffer = Buffer()
            writer.write(components, buffer)
            assertTrue(writer.encodeToByteArray(components).contentEquals(buffer.readByteArray()))
            for (component in components) {
                writer.write(component, buffer)
                assertTrue(writer.encodeToByteArray(component).contentEquals(buffer.readByteArray()))
            }
        }
        assertTrue(Buffer().asByteInput().toString().startsWith("ByteInput("))
        assertTrue(Buffer().asByteOutput().toString().startsWith("ByteOutput("))
    }

    private fun events(reader: ICalReader): List<String> {
        val out = ArrayList<String>()
        while (true) {
            val event = reader.next() ?: break
            val value = if (event == ContentEventType.Property) reader.valueString() else ""
            out += "$event ${reader.name} $value ${reader.span().startOffset} ${reader.depth}"
        }
        return out
    }

    private fun assertSameResult(expected: ParseResult, actual: ParseResult, what: String) {
        assertEquals(expected.components, actual.components, what)
        assertEquals(describe(expected.problems), describe(actual.problems), what)
        assertEquals(expected.charset, actual.charset, what)
        assertEquals(expected.isTruncated, actual.isTruncated, what)
    }

    private fun describe(problems: List<io.github.calendrum.Problem>): List<String> =
        problems.map { "${it.code.id} ${it.span.startOffset} ${it.span.line}:${it.span.column}" }

    private fun source(bytes: ByteArray, chunk: Int): BufferedSource = ChunkedSource(bytes, chunk).buffer()

    private class ChunkedSource(private val bytes: ByteArray, private val chunk: Int) : Source {
        private var position = 0

        override fun read(sink: Buffer, byteCount: Long): Long {
            if (position == bytes.size) return -1
            val count = minOf(chunk.toLong(), byteCount, (bytes.size - position).toLong()).toInt()
            sink.write(bytes, position, count)
            position += count
            return count.toLong()
        }

        override fun timeout(): Timeout = Timeout.NONE

        override fun close() = Unit
    }

    private fun crlf(vararg lines: String): String = lines.joinToString("") { "$it\r\n" }

    private companion object {
        const val LARGE = 16 * 1024 * 1024
    }
}

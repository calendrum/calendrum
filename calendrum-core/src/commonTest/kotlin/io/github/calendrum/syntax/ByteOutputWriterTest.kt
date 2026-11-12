package io.github.calendrum.syntax

import io.github.calendrum.ByteOutput
import io.github.calendrum.ICal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ByteOutputWriterTest {
    private class Collected : ByteOutput {
        private var bytes = ByteArray(0)
        var writes: Int = 0
            private set

        override fun write(buffer: ByteArray, offset: Int, length: Int) {
            writes++
            bytes += buffer.copyOfRange(offset, offset + length)
        }

        fun toByteArray(): ByteArray = bytes
    }

    private val canonical = ICal.writer(WriteOptions.builder().byteStable(false).build())

    private val large: ByteArray = buildString {
        append("BEGIN:VCALENDAR\r\nPRODID:big\r\n")
        repeat(2_000) {
            append("BEGIN:VEVENT\r\nUID:").append(it).append("\r\nsummary;x-p=\"a:b\":Встреча ").append(it)
            append("\r\n folded tail\r\nEND:VEVENT\r\n")
        }
        append("END:VCALENDAR\r\n")
    }.encodeToByteArray()

    @Test
    fun byteOutputReceivesTheSameBytesInBlocks() {
        val tree = ICal.parse(large).components
        for (writer in listOf(ICal.writer(), canonical)) {
            val single = Collected()
            writer.write(tree.single(), single)
            assertTrue(writer.encodeToByteArray(tree.single()).contentEquals(single.toByteArray()))
            assertTrue(single.writes > 1)
            val list = Collected()
            writer.write(tree + tree, list)
            assertTrue(writer.encodeToByteArray(tree + tree).contentEquals(list.toByteArray()))
        }
        val small = Collected()
        ICal.writer().write(ICal.parse("BEGIN:X\r\nY:1\r\nEND:X\r\n").components.single(), small)
        assertEquals(1, small.writes)
        val empty = Collected()
        ICal.writer().write(emptyList(), empty)
        assertEquals(0, empty.writes)
    }

    @Test
    fun linesBeforeAFailureAreWritten() {
        val tree = ICal.parse("BEGIN:X\r\nA:1\r\nB:bad\u0001value\r\nEND:X\r\n").components.single()
        val out = Collected()
        assertFailsWith<IllegalArgumentException> { canonical.write(tree, out) }
        assertEquals("BEGIN:X\r\nA:1\r\n", out.toByteArray().decodeToString())
        val stable = Collected()
        ICal.writer().write(tree, stable)
        assertEquals("BEGIN:X\r\nA:1\r\nB:bad\u0001value\r\nEND:X\r\n", stable.toByteArray().decodeToString())
    }
}

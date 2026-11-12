package io.github.calendrum.syntax

import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

internal class Written(val bytes: ByteArray, val text: String) {
    val physicalLines: List<ByteArray> by lazy { physicalLinesOf(bytes) }

    val lengths: List<Int> get() = physicalLines.map { it.size }

    fun unfolded(): String = unfold(text)
}

// Runs the same calls on both outputs (`ByteLineWriter` and `AppendableLineWriter`) and checks the invariants of every
// writer test: the UTF-8 of the `Appendable` output equals the byte output; only CRLF line ends; at most 75 octets per
// physical line; every physical line is valid UTF-8 on its own; every fold was needed (the first character of the next
// line would not have fit).
internal fun write(block: ContentLineWriter.() -> Unit): Written {
    val bytesWriter = ByteLineWriter()
    bytesWriter.block()
    val text = StringBuilder()
    AppendableLineWriter(text).block()
    val bytes = bytesWriter.toByteArray()
    assertEquals(bytes.size, bytesWriter.size)
    assertContentEquals(bytes, text.toString().encodeToByteArray(), "UTF-8 of the Appendable output != byte output")
    val written = Written(bytes, text.toString())
    assertFoldedCorrectly(written)
    return written
}

internal fun ContentLineWriter.line(name: String, value: String, vararg parameters: Pair<String, List<String>>) {
    property(name)
    for ((parameter, values) in parameters) {
        parameter(parameter)
        for (parameterValue in values) parameterValue(parameterValue)
    }
    value(value)
}

internal fun ContentLineWriter.lexed(line: Lexed) {
    property(line.name)
    for (parameter in line.parameters) {
        parameter(parameter.name)
        for (value in parameter.values) parameterValue(value)
    }
    value(line.value)
}

internal fun physicalLinesOf(bytes: ByteArray): List<ByteArray> {
    val lines = mutableListOf<ByteArray>()
    var start = 0
    var index = 0
    while (index < bytes.size) {
        val byte = bytes[index]
        if (byte == CR) {
            assertTrue(index + 1 < bytes.size && bytes[index + 1] == LF, "bare CR at byte $index")
            lines += bytes.copyOfRange(start, index)
            index += 2
            start = index
        } else {
            assertTrue(byte != LF, "bare LF at byte $index")
            index++
        }
    }
    assertEquals(bytes.size, start, "output must end with CRLF")
    return lines
}

internal fun assertFoldedCorrectly(written: Written) {
    val lines = written.physicalLines
    for ((number, line) in lines.withIndex()) {
        assertTrue(line.size <= 75, "physical line #$number has ${line.size} octets")
        assertTrue(line.isNotEmpty(), "physical line #$number is empty")
        try {
            line.decodeToString(throwOnInvalidSequence = true)
        } catch (e: CharacterCodingException) {
            fail("physical line #$number is not valid UTF-8 on its own: $e")
        }
        val next = lines.getOrNull(number + 1) ?: continue
        if (next[0] == SPACE && next.size > 1) {
            val nextCodePoint = utf8Length(next[1])
            assertTrue(line.size + nextCodePoint > 75, "physical line #$number could hold the next character")
        }
    }
}

internal fun utf8Length(lead: Byte): Int {
    val value = lead.toInt() and 0xFF
    return when {
        value < 0x80 -> 1
        value < 0xE0 -> 2
        value < 0xF0 -> 3
        else -> 4
    }
}

internal fun unfold(text: String): String = text.replace("\r\n ", "")

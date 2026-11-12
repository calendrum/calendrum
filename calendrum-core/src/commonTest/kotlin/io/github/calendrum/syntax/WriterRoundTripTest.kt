package io.github.calendrum.syntax

import io.github.calendrum.Severity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Random lines from fixed SplitMix64 seeds, split into batches that stay under the 2 s Mocha timeout of `jsNodeTest`.
// Expected parameter values follow RFC 6868: CRLF and CR are written as `^n`, which reads back as LF.
class WriterRoundTripTest {
    private class Line(val name: String, val parameters: List<Param>, val value: String)

    @Test
    fun randomLinesSurviveTheLexer1() {
        assertRandomLinesSurvive(5545)
    }

    @Test
    fun randomLinesSurviveTheLexer2() {
        assertRandomLinesSurvive(5546)
    }

    @Test
    fun randomLinesSurviveTheLexer3() {
        assertRandomLinesSurvive(5547)
    }

    @Test
    fun randomLinesSurviveTheLexer4() {
        assertRandomLinesSurvive(5548)
    }

    @Test
    fun randomLinesSurviveTheLexer5() {
        assertRandomLinesSurvive(5549)
    }

    @Test
    fun randomLinesSurviveTheLexer6() {
        assertRandomLinesSurvive(5550)
    }

    @Test
    fun componentLinesSurviveTheLexer() {
        val random = TestRandom(5551)
        val names = List(500) { randomName(random) }
        val written = write {
            for (name in names) {
                begin(name)
                end(name)
            }
        }
        val result = lex(written.bytes)
        assertEquals(emptyList(), result.problems)
        val expected = names.flatMap { listOf("BEGIN" to it.uppercase(), "END" to it.uppercase()) }
        assertEquals(expected, result.lines.map { it.name to it.value })
    }

    private fun assertRandomLinesSurvive(seed: Long) {
        val random = TestRandom(seed)
        val batch = List(LINES) { randomLine(random) }
        val written = write { for (line in batch) writeLine(line) }
        val result = lex(written.bytes)
        assertEquals(emptyList(), result.problems.filter { it.severity != Severity.Info })
        assertEquals(batch.size, result.lines.size)
        for ((index, line) in batch.withIndex()) {
            val lexed = result.lines[index]
            assertEquals(line.name.uppercase(), lexed.name, "name of line #$index")
            assertEquals(expectedParameters(line.parameters), lexed.parameters, "parameters of line #$index")
            assertEquals(line.value, lexed.value, "value of line #$index")
        }
        assertTrue(written.bytes.size > 100_000, "only ${written.bytes.size} octets written")
    }

    private fun ContentLineWriter.writeLine(line: Line) {
        property(line.name)
        for (parameter in line.parameters) {
            parameter(parameter.name)
            for (value in parameter.values) parameterValue(value)
        }
        value(line.value)
    }

    private fun expectedParameters(parameters: List<Param>): List<Param> = parameters.map { parameter ->
        Param(parameter.name.uppercase(), parameter.values.map { it.replace("\r\n", "\n").replace('\r', '\n') })
    }

    private fun randomLine(random: TestRandom): Line {
        val parameters = List(random.nextInt(6)) {
            Param(randomName(random), List(1 + random.nextInt(3)) { randomText(random, PARAMETER_PIECES, 12) })
        }
        val length = when (random.nextInt(10)) {
            0 -> 400 + random.nextInt(1_600)
            1, 2 -> 40 + random.nextInt(160)
            else -> random.nextInt(40)
        }
        return Line(randomName(random), parameters, randomText(random, VALUE_PIECES, length))
    }

    private fun randomName(random: TestRandom): String {
        val name = when (random.nextInt(4)) {
            0 -> KNOWN_NAMES[random.nextInt(KNOWN_NAMES.size)]
            1 -> "X-" + token(random, 1 + random.nextInt(4)) + "-" + token(random, 1 + random.nextInt(12))
            2 -> token(random, 60 + random.nextInt(60))
            else -> token(random, 1 + random.nextInt(20))
        }
        val chars = name.toCharArray()
        for (index in chars.indices) {
            if (random.nextInt(3) == 0) chars[index] = chars[index].lowercaseChar()
        }
        return chars.concatToString()
    }

    private fun token(random: TestRandom, length: Int): String {
        val chars = CharArray(length) { NAME_CHARS[random.nextInt(NAME_CHARS.length)] }
        return chars.concatToString()
    }

    private fun randomText(random: TestRandom, pieces: List<String>, count: Int): String {
        val text = StringBuilder()
        repeat(count) { text.append(pieces[random.nextInt(pieces.size)]) }
        return text.toString()
    }

    private companion object {
        const val LINES = 500
        const val NAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-"

        val KNOWN_NAMES = listOf("SUMMARY", "DTSTART", "ATTENDEE", "X-WR-CALNAME", "TZID", "CN", "VEVENT", "RRULE")

        val COMMON_PIECES = listOf(
            "a", "Z", "7", " ", " ", "word", "\t", ":", ";", ",", "=", "'", "^", "^n", "\\", "\\n", "/", "@",
            "é", "ж", "Ё", "€", "中", "文", "😀", "𝄞", "🎉", " ", "\u0085",
        )

        val PARAMETER_PIECES = COMMON_PIECES + listOf("\"", "\"\"", "\n", "\r", "\r\n", "^\"")

        val VALUE_PIECES = COMMON_PIECES + listOf("\"", "mailto:x@example.com", "Долгое описание, ", "長い説明。")
    }
}

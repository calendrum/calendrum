package io.github.calendrum.syntax

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LexerRobustnessTest {
    private class Random64(private var state: Long) {
        fun nextLong(): Long {
            state += -0x61c8864680b583ebL
            var z = state
            z = (z xor (z ushr 30)) * -0x40a7b892e31b1a47L
            z = (z xor (z ushr 27)) * -0x6b2fb644ecceee15L
            return z xor (z ushr 31)
        }

        fun nextInt(bound: Int): Int = ((nextLong() ushr 33) % bound).toInt()
    }

    private val alphabet = bytesOf(
        0x0D, 0x0A, 0x0D, 0x0A, 0x20, 0x09, 0x3A, 0x3A, 0x3B, 0x3B, 0x3D, 0x2C, 0x22, 0x22, 0x5E, 0x6E, 0x27,
        0x41, 0x42, 0x2D, 0x31, 0x61, 0x5F, 0x40, 0xC3, 0xA9, 0xE2, 0x82, 0xAC, 0xF0, 0x9F, 0x80, 0xBF,
        0xC0, 0xED, 0xF4, 0xFF, 0xFE, 0xEF, 0xBB, 0x00,
    )

    private fun randomInput(random: Random64): ByteArray {
        val size = random.nextInt(300)
        return ByteArray(size) {
            if (random.nextInt(4) == 0) random.nextInt(256).toByte() else alphabet[random.nextInt(alphabet.size)]
        }
    }

    @Test
    fun randomInputsNeitherThrowNorHang() {
        val random = Random64(5545)
        val limitOptions = listOf(
            ParseLimits.Default,
            limits(maxLineOctets = 16, maxLines = 1_000, maxProblems = 1_000),
            limits(maxLineOctets = 1, maxLines = 5, maxProblems = 2),
        )
        var totalLines = 0
        repeat(4_000) { iteration ->
            val input = randomInput(random)
            val limits = limitOptions[iteration % limitOptions.size]
            val problems = mutableListOf<Problem>()
            val lexer = ContentLexer(input, limits) { problems += it }
            var calls = 0
            var previousStart = -1
            while (true) {
                calls++
                assertTrue(calls <= input.size + 2, "too many lines for input #$iteration")
                val line = lexer.next() ?: break
                totalLines++
                assertTrue(line.startOffset > previousStart, "offsets must grow in input #$iteration")
                previousStart = line.startOffset
                assertTrue(line.startOffset in 0..line.endOffset && line.endOffset <= lexer.decodedSize)
                assertTrue(line.line >= 1 && line.column >= 1 && line.endLine >= line.line)
                assertTrue(line.end - line.start <= limits.maxLineOctets)
                line.name()
                line.rawValue()
                for (p in 0 until line.parameterCount) {
                    line.parameterName(p)
                    for (v in 0 until line.parameterValueCount(p)) line.parameterValue(p, v)
                }
            }
            assertEquals(null, lexer.next())
            for (problem in problems) {
                assertTrue(problem.offset in 0..lexer.decodedSize, "problem offset in input #$iteration")
                assertTrue(problem.line >= 1 && problem.column >= 1)
            }
            assertTrue(
                problems.count {
                    it.code.oncePerInput.not() && !it.code.bypassesLimit
                } <= limits.maxProblems + 1,
            )
        }
        assertTrue(totalLines > 1_000)
    }

    @Test
    fun problemsDoNotContainPropertyValues() {
        val input = "SUMMARY:SECRET-1\n".encodeToByteArray() +
            "X_BAD:SECRET-2\r\n" +
            "ATTENDEE;CN=\"SECRET-3:mailto:SECRET-4\r\n" +
            "NOCOLON SECRET-5\r\n" +
            "A;B;C=x\"SECRET-6\":SECRET-7" + bytesOf(0xFF) + "\r\n" +
            "\r\n" +
            "D:" + "SECRET-8".repeat(8) + "\r\n"
        val problems = mutableListOf<Problem>()
        val limits = limits(maxLineOctets = 50, maxLines = 100, maxProblems = 100)
        val lexer = ContentLexer(input, limits) { problems += it }
        val lines = mutableListOf<String>()
        while (true) lines += (lexer.next() ?: break).toString()
        val codes = problems.map { it.code }.toSet()
        assertTrue(
            codes.containsAll(
                listOf(
                    ProblemCode.BareLf,
                    ProblemCode.InvalidName,
                    ProblemCode.UnclosedQuote,
                    ProblemCode.MissingColon,
                    ProblemCode.MalformedParameter,
                    ProblemCode.InvalidUtf8,
                    ProblemCode.BlankLine,
                    ProblemCode.LineTooLong,
                ),
            ),
            "codes: $codes",
        )
        for (text in problems.map { it.toString() } + problems.map { it.message } + lines) {
            assertFalse("SECRET" in text, text)
        }
    }
}

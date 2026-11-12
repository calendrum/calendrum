package io.github.calendrum.syntax

import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LexerPositionAndLimitTest {
    @Test
    fun positionsOfLinesAndProblems() {
        val input = "BEGIN:VCALENDAR\n" + "SUMMARY:ab\r\n cd\r\n" + "X;a@b=1:v\r\n" + "Y;p\r\n @=1:v\r\n"
        val result = lex(input)
        val lines = result.lines
        assertEquals(listOf(0, 16, 33, 44), lines.map { it.startOffset })
        assertEquals(listOf(15, 31, 42, 55), lines.map { it.endOffset })
        assertEquals(listOf(1, 2, 4, 5), lines.map { it.line })
        assertEquals(listOf(1, 3, 4, 6), lines.map { it.endLine })
        assertEquals(listOf(1, 1, 1, 1), lines.map { it.column })
        assertEquals(listOf(false, true, false, true), lines.map { it.folded })

        val bareLf = result.problem(ProblemCode.BareLf)
        assertEquals(listOf(15, 1, 16), listOf(bareLf.offset, bareLf.line, bareLf.column))
        val names = result.problems.filter { it.code == ProblemCode.InvalidName }
        assertEquals(listOf(36, 50), names.map { it.offset })
        assertEquals(listOf(4, 6), names.map { it.line })
        assertEquals(listOf(4, 2), names.map { it.column })
    }

    @Test
    fun columnsCountBytesNotCharacters() {
        val result = lex("SUMMARY;CN=Пётр;X_Y=1:v\r\n")
        val problem = result.problem(ProblemCode.InvalidName)
        assertEquals(21, problem.offset)
        assertEquals(22, problem.column)
    }

    @Test
    fun longLineFoldedEvery75Octets() {
        val parameters = (1..6).joinToString(separator = "") { ";P$it=a,b,c,d,e" }
        val value = "значение ".repeat(300)
        val logical = "X-LONG$parameters;Q=bad\"quote:$value".encodeToByteArray()
        val quote = logical.indexOf(DQUOTE)
        var folded = ByteArray(0)
        var quoteOffset = -1
        var quoteLine = 0
        var quoteColumn = 0
        var index = 0
        var physicalLine = 1
        while (index < logical.size) {
            val prefix = if (index == 0) 0 else 1
            val chunk = minOf(75 - prefix, logical.size - index)
            if (quote in index until index + chunk) {
                quoteOffset = folded.size + prefix + (quote - index)
                quoteLine = physicalLine
                quoteColumn = prefix + (quote - index) + 1
            }
            folded += (if (prefix == 1) " " else "").encodeToByteArray() + logical.copyOfRange(index, index + chunk) +
                "\r\n".encodeToByteArray()
            index += chunk
            physicalLine++
        }
        val result = lex(folded)
        val line = result.single()
        assertEquals("X-LONG", line.name)
        assertEquals(7, line.parameters.size)
        assertEquals(List(6) { Param("P${it + 1}", listOf("a", "b", "c", "d", "e")) }, line.parameters.take(6))
        assertEquals(Param("Q", listOf("bad\"quote")), line.parameters[6])
        assertEquals(value, line.value)
        assertEquals(physicalLine - 1, line.endLine)
        val problem = result.problem(ProblemCode.MalformedParameter)
        assertEquals(listOf(quoteOffset, quoteLine, quoteColumn), listOf(problem.offset, problem.line, problem.column))
        assertTrue(quoteLine > 1)
    }

    @Test
    fun lineAtTheLengthLimitIsAccepted() {
        val limits = limits(maxLineOctets = 10, maxLines = 100, maxProblems = 100)
        val result = lex("A:12345678\r\nC:1234\r\n 5678\r\n", limits)
        assertEquals(listOf("12345678", "12345678"), result.lines.map { it.value })
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun linesOverTheLengthLimitAreSkipped() {
        val limits = limits(maxLineOctets = 10, maxLines = 100, maxProblems = 100)
        val result = lex("B:123456789\r\nC:1234\r\n 56789\r\n\t0\r\nD:1\r\n", limits)
        assertEquals(listOf("D"), result.lines.map { it.name })
        val problems = result.problems.filter { it.code == ProblemCode.LineTooLong }
        assertEquals(listOf(0, 13), problems.map { it.offset })
        assertEquals(listOf(1, 2), problems.map { it.line })
        assertEquals(Severity.Error, problems[0].severity)
        assertEquals(5, result.lines[0].line)
    }

    @Test
    fun manyBlankLinesGiveOneProblem() {
        val input = "A:1\r\n" + "\r\n".repeat(1_000_000) + "B:2\r\n"
        val result = lex(input)
        assertEquals(listOf("A", "B"), result.lines.map { it.name })
        assertEquals(listOf("ICAL-S004"), result.codes)
        assertEquals(1_000_000, result.lexer.problemCount(ProblemCode.BlankLine))
        assertEquals(1_000_002, result.lexer.logicalLineCount)
    }

    @Test
    fun lineLimitStopsWithPartialResult() {
        val limits = limits(maxLineOctets = 100, maxLines = 1000, maxProblems = 100)
        val input = "A:1\r\n" + "\r\n".repeat(1_000_000) + "B:2\r\n"
        val result = lex(input, limits)
        assertEquals(listOf("A"), result.lines.map { it.name })
        assertEquals(listOf("ICAL-S004", "ICAL-S014"), result.codes)
        val problem = result.problem(ProblemCode.TooManyLines)
        assertEquals(5 + 999 * 2, problem.offset)
        assertEquals(1001, problem.line)
        assertEquals(1000, result.lexer.logicalLineCount)
        assertEquals(999, result.lexer.problemCount(ProblemCode.BlankLine))
    }

    @Test
    fun lineLimitCountsContentLines() {
        val limits = limits(maxLineOctets = 100, maxLines = 2, maxProblems = 0)
        val result = lex(crlf("A:1", "B:2", "C:3"), limits)
        assertEquals(listOf("A", "B"), result.lines.map { it.name })
        assertEquals(listOf("ICAL-S014"), result.codes)
    }

    @Test
    fun problemLimitStopsReportingButKeepsCounting() {
        val limits = limits(maxLineOctets = 100, maxLines = 100, maxProblems = 3)
        val input = (1..10).joinToString(separator = "") { "X_$it:v\r\n" }
        val result = lex(input, limits)
        assertEquals(10, result.lines.size)
        assertEquals(listOf("ICAL-S008", "ICAL-S008", "ICAL-S008", "ICAL-S013"), result.codes)
        assertEquals(10, result.lexer.problemCount(ProblemCode.InvalidName))
        assertEquals(1, result.lexer.problemCount(ProblemCode.TooManyProblems))
        assertEquals(4, result.problems[3].line)
    }

    @Test
    fun lexerReturnsNullAfterTheEnd() {
        val lexer = ContentLexer("A:1".encodeToByteArray(), ParseLimits.Default) { }
        assertTrue(lexer.next() != null)
        assertEquals(null, lexer.next())
        assertEquals(null, lexer.next())
    }

    @Test
    fun invalidArgumentsAreRejected() {
        assertFailsWith<IllegalArgumentException> { limits(maxLineOctets = 0, maxLines = 1, maxProblems = 0) }
        assertFailsWith<IllegalArgumentException> { limits(maxLineOctets = 1, maxLines = 0, maxProblems = 0) }
        assertFailsWith<IllegalArgumentException> { limits(maxLineOctets = 1, maxLines = 1, maxProblems = -1) }
        assertFailsWith<IllegalArgumentException> { limits(maxDepth = 0) }
        assertFailsWith<IllegalArgumentException> { limits(maxComponents = 0) }
        assertFailsWith<IllegalArgumentException> { ContentLexer(ByteArray(4), 3, 2, ParseLimits.Default) { } }
        assertFailsWith<IllegalArgumentException> { ContentLexer(ByteArray(4), -1, 2, ParseLimits.Default) { } }
    }

    @Test
    fun parameterIndexesAreChecked() {
        val line = ContentLexer("X;A=1:v".encodeToByteArray(), ParseLimits.Default) { }.next()!!
        assertFailsWith<IndexOutOfBoundsException> { line.parameterName(1) }
        assertFailsWith<IndexOutOfBoundsException> { line.parameterValue(0, 1) }
        assertFalse(line.isFolded)
    }
}

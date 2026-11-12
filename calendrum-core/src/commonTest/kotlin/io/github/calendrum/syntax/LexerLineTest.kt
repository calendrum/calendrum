package io.github.calendrum.syntax

import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LexerLineTest {
    @Test
    fun crlfLinesProduceNoProblems() {
        val result = lex(crlf("BEGIN:VCALENDAR", "VERSION:2.0", "END:VCALENDAR"))
        assertEquals(listOf("BEGIN", "VERSION", "END"), result.lines.map { it.name })
        assertEquals(listOf("VCALENDAR", "2.0", "VCALENDAR"), result.lines.map { it.value })
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun lastLineWithoutLineEndingIsAccepted() {
        val result = lex("A:1\r\nB:2")
        assertEquals(listOf("1", "2"), result.lines.map { it.value })
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun emptyInputHasNoLines() {
        val result = lex("")
        assertEquals(emptyList(), result.lines)
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun bareLfIsReportedOnceWithCount() {
        val result = lex("A:1\nB:2\nC:3\n")
        assertEquals(listOf("A", "B", "C"), result.lines.map { it.name })
        assertEquals(listOf("ICAL-S003"), result.codes)
        val problem = result.problem(ProblemCode.BareLf)
        assertEquals(3, problem.offset)
        assertEquals(1, problem.line)
        assertEquals(4, problem.column)
        assertEquals(3, result.lexer.problemCount(ProblemCode.BareLf))
    }

    @Test
    fun bareCrIsReportedOnceWithCount() {
        val result = lex("A:1\rB:2\rC:3")
        assertEquals(listOf("1", "2", "3"), result.lines.map { it.value })
        assertEquals(listOf("ICAL-S002"), result.codes)
        assertEquals(2, result.lexer.problemCount(ProblemCode.BareCr))
        assertEquals(listOf(1, 2, 3), result.lines.map { it.line })
    }

    @Test
    fun mixedLineEndingsInOneInput() {
        val result = lex("A:1\r\nB:2\nC:3\rD:4\r\nE:5\n")
        assertEquals(listOf("A", "B", "C", "D", "E"), result.lines.map { it.name })
        assertEquals(listOf(1, 2, 3, 4, 5), result.lines.map { it.line })
        assertEquals(listOf("ICAL-S003", "ICAL-S002"), result.codes)
        assertEquals(2, result.lexer.problemCount(ProblemCode.BareLf))
        assertEquals(1, result.lexer.problemCount(ProblemCode.BareCr))
    }

    @Test
    fun crBeforeCrlfIsBareCrFollowedByBlankLine() {
        val result = lex("A:1\r\r\nB:2\r\n")
        assertEquals(listOf("A", "B"), result.lines.map { it.name })
        assertEquals(listOf("ICAL-S002", "ICAL-S004"), result.codes)
        assertEquals(3, result.lines[1].line)
    }

    @Test
    fun rfc5545FoldingExample() {
        val input = crlf(
            "DESCRIPTION:This is a lo",
            " ng description",
            "  that exists on a long line.",
        )
        val line = lex(input).single()
        assertEquals("This is a long description that exists on a long line.", line.value)
        assertTrue(line.folded)
    }

    @Test
    fun foldWithHorizontalTab() {
        val line = lex("SUMMARY:ab\r\n\tcd\r\n").single()
        assertEquals("abcd", line.value)
    }

    @Test
    fun onlyOneFoldCharacterIsRemoved() {
        val line = lex("SUMMARY:ab\r\n  cd\r\n\t\tef\r\n").single()
        assertEquals("ab cd\tef", line.value)
    }

    @Test
    fun foldAfterBareLf() {
        val result = lex("SUMMARY:ab\n cd\nX:1\n")
        assertEquals(listOf("abcd", "1"), result.lines.map { it.value })
        assertEquals(listOf("ICAL-S003"), result.codes)
        assertEquals(3, result.lexer.problemCount(ProblemCode.BareLf))
    }

    @Test
    fun foldAfterBareCr() {
        val result = lex("SUMMARY:ab\r cd\rX:1")
        assertEquals(listOf("abcd", "1"), result.lines.map { it.value })
        assertEquals(listOf("ICAL-S002"), result.codes)
    }

    @Test
    fun foldCanSplitNameAndParameters() {
        val line = lex("ATT\r\n ENDEE;RS\r\n VP=TRUE:mailto:a@\r\n example.com\r\n").single()
        assertEquals("ATTENDEE", line.name)
        assertEquals(listOf(Param("RSVP", listOf("TRUE"))), line.parameters)
        assertEquals("mailto:a@example.com", line.value)
    }

    @Test
    fun blankLinesAreSkippedAndReportedOnce() {
        val result = lex("A:1\r\n\r\n\r\nB:2\r\n\r\n")
        assertEquals(listOf("A", "B"), result.lines.map { it.name })
        assertEquals(listOf("ICAL-S004"), result.codes)
        val problem = result.problem(ProblemCode.BlankLine)
        assertEquals(5, problem.offset)
        assertEquals(2, problem.line)
        assertEquals(1, problem.column)
        assertEquals(3, result.lexer.problemCount(ProblemCode.BlankLine))
    }

    @Test
    fun blankLineFollowedByFoldBecomesContent() {
        val result = lex("A:1\r\n\r\n B:2\r\n")
        assertEquals(listOf("A", "B"), result.lines.map { it.name })
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun continuationAtInputStartStartsTheFirstLine() {
        val result = lex(" BEGIN:VCALENDAR\r\n  X:1\r\nEND:VCALENDAR\r\n")
        assertEquals(listOf("BEGIN", "END"), result.lines.map { it.name })
        assertEquals("VCALENDAR X:1", result.lines[0].value)
        assertEquals(listOf("ICAL-S005"), result.codes)
        val problem = result.problem(ProblemCode.ContinuationAtStart)
        assertEquals(0, problem.offset)
        assertEquals(1, problem.line)
        assertEquals(1, problem.column)
        assertEquals(1, result.lines[0].startOffset)
        assertEquals(2, result.lines[0].column)
    }

    @Test
    fun continuationAtStartAfterBom() {
        val result = lex(bytesOf(0xEF, 0xBB, 0xBF) + "\tA:1\r\n")
        assertEquals("A", result.single().name)
        assertEquals(listOf("ICAL-S001", "ICAL-S005"), result.codes)
        assertEquals(3, result.problem(ProblemCode.ContinuationAtStart).offset)
        assertEquals(4, result.lines[0].startOffset)
        assertEquals(5, result.lines[0].column)
    }

    @Test
    fun utf8BomIsSkippedWithInfo() {
        val result = lex(bytesOf(0xEF, 0xBB, 0xBF) + crlf("BEGIN:VCALENDAR", "END:VCALENDAR"))
        assertEquals(listOf("BEGIN", "END"), result.lines.map { it.name })
        val problem = result.problem(ProblemCode.Utf8Bom)
        assertEquals(Severity.Info, problem.severity)
        assertEquals(0, problem.offset)
        assertEquals(3, result.lines[0].startOffset)
        assertEquals(4, result.lines[0].column)
    }

    @Test
    fun utf16LittleEndianBomIsTranscoded() {
        val result = lex(bytesOf(0xFF, 0xFE, 0x42, 0x00, 0x3A, 0x00, 0x31, 0x00, 0x0D, 0x00, 0x0A, 0x00))
        assertEquals(listOf("B"), result.lines.map { it.name })
        assertEquals("1", result.single().value)
        assertEquals(listOf("ICAL-S024"), result.codes)
        assertEquals(Severity.Info, result.problems[0].severity)
        assertEquals(0, result.single().startOffset)
        assertEquals(1, result.single().column)
    }

    @Test
    fun utf16BigEndianBomIsTranscoded() {
        val result = lex(bytesOf(0xFE, 0xFF, 0x00, 0x42, 0x00, 0x3A, 0x00, 0x31))
        assertEquals("1", result.single().value)
        assertEquals(listOf("ICAL-S024"), result.codes)
    }

    @Test
    fun utf32BigEndianBomIsTranscoded() {
        val result = lex(bytesOf(0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x42, 0x00, 0x00, 0x00, 0x3A))
        assertEquals("B", result.single().name)
        assertEquals(listOf("ICAL-S024"), result.codes)
    }

    @Test
    fun utf7SignatureStopsWithOneProblem() {
        val result = lex("+/v8-BEGIN:VCALENDAR\r\n".encodeToByteArray())
        assertEquals(emptyList(), result.lines)
        assertEquals(listOf("ICAL-S011"), result.codes)
        assertEquals(Severity.Error, result.problems[0].severity)
    }

    @Test
    fun lexesOnlyTheGivenRange() {
        val bytes = "garbage\r\nA:1\r\nB:2\r\ntail".encodeToByteArray()
        val result = lex(bytes, offset = 9, length = 10)
        assertEquals(listOf("A", "B"), result.lines.map { it.name })
        assertEquals(listOf(0, 5), result.lines.map { it.startOffset })
        assertEquals(emptyList(), result.problems)
    }
}

package io.github.calendrum.syntax

import io.github.calendrum.ProblemCode
import kotlin.test.Test
import kotlin.test.assertEquals

class LexerUtf8Test {
    private fun assertFoldRestores(text: String) {
        val line = "SUMMARY:$text"
        val bytes = line.encodeToByteArray()
        for (split in "SUMMARY:".length + 1 until bytes.size) {
            val folded = line.insertAtByte(split, "\r\n ")
            val result = lex(folded)
            assertEquals(text, result.single().value, "split at byte $split")
            assertEquals(emptyList(), result.problems, "split at byte $split")
        }
    }

    @Test
    fun foldInsideTwoByteSequences() {
        assertFoldRestores("Привет, мир")
    }

    @Test
    fun foldInsideThreeByteSequences() {
        assertFoldRestores("会議室の予約")
    }

    @Test
    fun foldInsideFourByteSequences() {
        assertFoldRestores("📅🎉")
    }

    @Test
    fun severalFoldsInsideOneCharacter() {
        val bytes = "SUMMARY:".encodeToByteArray() + bytesOf(0xF0, 0x9F) + "\r\n " + bytesOf(0x93) + "\n\t" +
            bytesOf(0x85) + "\r\n"
        val result = lex(bytes)
        assertEquals("📅", result.single().value)
        assertEquals(listOf("ICAL-S003"), result.codes)
    }

    @Test
    fun foldInsideNameAndParameterValue() {
        val bytes = "X-ИМЯ;CN=Пётр:ok".encodeToByteArray()
        val folded = bytes.copyOfRange(0, 3) + "\r\n " + bytes.copyOfRange(3, 15) + "\r\n\t" +
            bytes.copyOfRange(15, bytes.size)
        val result = lex(folded)
        val line = result.single()
        assertEquals("X-ИМЯ", line.name)
        assertEquals(listOf(Param("CN", listOf("Пётр"))), line.parameters)
        assertEquals("ok", line.value)
        assertEquals(listOf("ICAL-S008"), result.codes)
    }

    @Test
    fun truncatedSequenceAtLineEnd() {
        val bytes = "A:x".encodeToByteArray() + bytesOf(0xD0) + "\r\nB:y\r\n"
        val result = lex(bytes)
        assertEquals(listOf("x�", "y"), result.lines.map { it.value })
        val problem = result.problem(ProblemCode.InvalidUtf8)
        assertEquals(3, problem.offset)
        assertEquals(1, problem.line)
        assertEquals(4, problem.column)
    }

    @Test
    fun truncatedSequenceAtEndOfInput() {
        val result = lex("A:x".encodeToByteArray() + bytesOf(0xE2, 0x82))
        assertEquals("x�", result.single().value)
        assertEquals(listOf("ICAL-S006"), result.codes)
    }

    @Test
    fun truncatedSequenceFollowedByAscii() {
        val result = lex("A:".encodeToByteArray() + bytesOf(0xE2, 0x82) + "z")
        assertEquals("�z", result.single().value)
    }

    @Test
    fun strayContinuationByte() {
        val result = lex("A:a".encodeToByteArray() + bytesOf(0x80) + "b\r\n")
        assertEquals("a�b", result.single().value)
        assertEquals(3, result.problem(ProblemCode.InvalidUtf8).offset)
    }

    @Test
    fun overlongEncodings() {
        val result = lex(
            "A:".encodeToByteArray() + bytesOf(0xC0, 0xAF) + "|" + bytesOf(0xE0, 0x80, 0xAF) + "|" +
                bytesOf(0xF0, 0x80, 0x80, 0xAF),
        )
        assertEquals("��|���|����", result.single().value)
        assertEquals(listOf("ICAL-S006"), result.codes)
        assertEquals(2, result.problems[0].offset)
    }

    @Test
    fun surrogatesAndOutOfRangeCodePoints() {
        val result = lex("A:".encodeToByteArray() + bytesOf(0xED, 0xA0, 0x80) + "|" + bytesOf(0xF4, 0x90, 0x80, 0x80))
        assertEquals("���|����", result.single().value)
    }

    @Test
    fun invalidBytesInFoldedLineReportFirstPosition() {
        val bytes = "A:ok\r\n x".encodeToByteArray() + bytesOf(0xFF) + "y\r\n"
        val result = lex(bytes)
        assertEquals("okx�y", result.single().value)
        val problem = result.problem(ProblemCode.InvalidUtf8)
        assertEquals(8, problem.offset)
        assertEquals(2, problem.line)
        assertEquals(3, problem.column)
    }

    @Test
    fun oneProblemPerLineWithInvalidBytes() {
        val bytes = "A:".encodeToByteArray() + bytesOf(0xFF, 0xFE, 0x80) + "\r\nB:" + bytesOf(0xC3) + "\r\n"
        val result = lex(bytes)
        assertEquals(listOf("ICAL-S006", "ICAL-S006"), result.codes)
        assertEquals(listOf("���", "�"), result.lines.map { it.value })
    }

    @Test
    fun invalidBytesInParameterValuesAreReplaced() {
        val bytes = "X;CN=".encodeToByteArray() + bytesOf(0xC3) + "^n:v"
        val line = lex(bytes).single()
        assertEquals(listOf(Param("CN", listOf("�\n"))), line.parameters)
    }

    @Test
    fun replacementDecoderMatchesStdlibOnValidInput() {
        val text = "ASCII, кириллица, 会議, 📅 и é"
        val bytes = text.encodeToByteArray()
        assertEquals(text, decodeUtf8Replacing(bytes, 0, bytes.size))
    }
}

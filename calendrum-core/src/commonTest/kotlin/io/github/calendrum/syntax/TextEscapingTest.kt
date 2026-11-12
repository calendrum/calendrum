package io.github.calendrum.syntax

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame

class TextEscapingTest {
    @Test
    fun escapesBackslashSemicolonCommaAndLineBreaks() {
        assertEquals("a\\\\b\\;c\\,d\\ne\\nf\\ng", escapeText("a\\b;c,d\ne\r\nf\rg"))
        assertEquals("\\n\\n\\n\\n", escapeText("\r\r\n\n\r"))
        assertEquals("\\\\n", escapeText("\\n"))
        assertEquals("\\,\\;\\\\", escapeText(",;\\"))
    }

    @Test
    fun colonAndOtherCharactersAreNotEscaped() {
        val value = "Time: 10:00 \"quoted\" ^n\ttab é中😀"
        assertSame(value, escapeText(value))
        val empty = ""
        assertSame(empty, escapeText(empty))
    }

    @Test
    fun escapesIntoAnAppendable() {
        val out = StringBuilder("SUMMARY:")
        escapeText("a,b;c\\d\r\ne", out)
        assertEquals("SUMMARY:a\\,b\\;c\\\\d\\ne", out.toString())
    }

    @Test
    fun listSeparatorsAreNotEscaped() {
        assertEquals("MEETING,a\\,b,c\\;d\\\\,e\\nf", escapeTextList(listOf("MEETING", "a,b", "c;d\\", "e\nf")))
        assertEquals("", escapeTextList(emptyList()))
        assertEquals(",", escapeTextList(listOf("", "")))
        val out = StringBuilder("CATEGORIES:")
        escapeTextList(listOf("Project Report", "XYZ", "Weekly Meeting"), out)
        assertEquals("CATEGORIES:Project Report,XYZ,Weekly Meeting", out.toString())
    }

    @Test
    fun unescapesKnownSequences() {
        assertEquals("a\\b;c,d\ne\nf", unescapeText("a\\\\b\\;c\\,d\\ne\\Nf"))
        assertEquals(
            "Participants: John Smith, Jane Doe, Jim Dandy\n-It was",
            unescapeText("Participants: John Smith\\, Jane Doe\\, Jim Dandy\\n-It was"),
        )
    }

    @Test
    fun ical4jBugCapitalNIsALineBreak() {
        assertEquals("a\nb", unescapeText("a\\Nb"))
    }

    @Test
    fun ical4jBugEscapedBackslashBeforeN() {
        assertEquals("C:\\dir\\\nnext", unescapeText("C:\\\\dir\\\\\\nnext"))
        assertEquals("C:\\\\dir\\\\\\nnext line", escapeText("C:\\dir\\\nnext line"))
        assertEquals("C:\\dir\\\nnext line", unescapeText(escapeText("C:\\dir\\\nnext line")))
    }

    @Test
    fun escapedBackslashFollowedByNIsNotALineBreak() {
        assertEquals("a\\nb", unescapeText("a\\\\nb"))
        assertEquals("\\N", unescapeText("\\\\N"))
        assertEquals("\\\n", unescapeText("\\\\\\n"))
    }

    @Test
    fun unknownSequencesStayAsTheyAre() {
        assertEquals("\\x\\:\\t\\\"\\ \\😀", unescapeText("\\x\\:\\t\\\"\\ \\😀"))
        assertEquals("a\\:b;c", unescapeText("a\\:b\\;c"))
    }

    @Test
    fun trailingBackslashStays() {
        assertEquals("end\\", unescapeText("end\\"))
        assertEquals("\\", unescapeText("\\"))
        assertEquals("a\\\\", unescapeText("a\\\\\\"))
        assertEquals("a;\\", unescapeText("a\\;\\"))
    }

    @Test
    fun textWithoutEscapesIsReturnedAsIs() {
        val value = "Plain text: no escapes, only a comma"
        assertSame(value, unescapeText(value))
    }

    // Rule: unescaping the escaped text gives the original with CRLF and lone CR turned into LF; escaping that result
    // again gives the same escaped text.
    @Test
    fun unescapedTextEqualsTheOriginalWithLineBreaksAsLf() {
        val random = TestRandom(6868)
        repeat(5_000) {
            val text = randomText(random)
            val escaped = escapeText(text)
            assertFalse(escaped.contains('\r') || escaped.contains('\n'), "escaped text keeps a line break")
            assertEquals(normalizedLineBreaks(text), unescapeText(escaped))
            assertEquals(escaped, escapeText(unescapeText(escaped)))
        }
    }

    @Test
    fun escapedTextSurvivesTheWriterAndTheLexer() {
        val random = TestRandom(6869)
        val texts = List(1_000) { randomText(random) }
        val written = write { for (text in texts) line("DESCRIPTION", escapeText(text)) }
        val lines = lex(written.bytes).lines
        assertEquals(texts.map { normalizedLineBreaks(it) }, lines.map { unescapeText(it.value) })
    }

    private fun normalizedLineBreaks(text: String): String = text.replace("\r\n", "\n").replace('\r', '\n')

    private fun randomText(random: TestRandom): String {
        val text = StringBuilder()
        repeat(random.nextInt(40)) { text.append(PIECES[random.nextInt(PIECES.size)]) }
        return text.toString()
    }

    private companion object {
        val PIECES = listOf(
            "\\", "\\", ";", ",", ":", "\n", "\r", "\r\n", "n", "N", "\\n", "\\N", "\\\\", "x", " ", "word",
            "é", "ж", "中", "😀", "\"", "^",
        )
    }
}

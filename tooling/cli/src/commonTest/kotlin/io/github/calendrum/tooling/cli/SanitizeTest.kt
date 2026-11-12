package io.github.calendrum.tooling.cli

import kotlin.test.Test
import kotlin.test.assertEquals

class SanitizeTest {
    @Test
    fun anEscapeSequenceBecomesVisible() {
        assertEquals("Stand\\x1B]0;pwn\\x07up", sanitize("Stand\u001B]0;pwn\u0007up"))
    }

    @Test
    fun theSingleCharacterCsiAndDelAreEscapedToo() {
        assertEquals("a\\x9Bb\\x7Fc\\x00d\\x85e", sanitize("a\u009Bb\u007Fc\u0000d\u0085e"))
    }

    @Test
    fun everyControlCharacterGoesInALine() {
        assertEquals("a\\x0Ab\\x0Dc\\x09d", sanitize("a\nb\rc\td"))
        for (code in 0..0x1F) {
            val text = sanitize(code.toChar().toString())
            assertEquals(4, text.length, "U+${code.toString(16)}")
            assertEquals("\\x", text.substring(0, 2))
        }
        for (code in 0x7F..0x9F) assertEquals("\\x" + code.toString(16).uppercase(), sanitize(code.toChar().toString()))
    }

    @Test
    fun aCalendarOnATerminalKeepsItsLineBreaksOnly() {
        assertEquals("a\r\nb\tc\\x1B[2Jd", sanitize("a\r\nb\tc\u001B[2Jd", keepLineBreaks = true))
        assertEquals("\\x9B", sanitize("\u009B", keepLineBreaks = true))
    }

    @Test
    fun aSurrogatePairIsACharacterAndStays() {
        val text = "grin \uD83D\uDE00 and \uD835\uDC9C"
        assertEquals(text, sanitize(text))
    }

    @Test
    fun aHalfOfAPairIsEscapedAndItsNeighboursAreNotHurt() {
        assertEquals("a\\uD83Db", sanitize("a\uD83Db"))
        assertEquals("a\\uDE00b", sanitize("a\uDE00b"))
        assertEquals("\\uDE00\uD83D\uDE00\\uD83D", sanitize("\uDE00\uD83D\uDE00\uD83D"))
        assertEquals("x\\uD83D", sanitize("x\uD83D"))
    }

    @Test
    fun textThatSteersTheReadingDirectionIsEscaped() {
        assertEquals("a\\u202Eb", sanitize("a\u202Eb"))
        assertEquals("a\\u2028b\\u2029", sanitize("a\u2028b\u2029"))
        assertEquals("\\u2066x\\u2069", sanitize("\u2066x\u2069"))
    }

    @Test
    fun ordinaryTextIsReturnedAsItIs() {
        val text = "Встреча команды, 会議, café"
        assertEquals(text, sanitize(text))
        assertEquals("", sanitize(""))
    }

    @Test
    fun jsonForATerminalHasTheSameValue() {
        val json = "{\"summary\":\"a\u009Bb\u007Fc\u202Ed\"}"
        assertEquals("{\"summary\":\"a\\u009bb\\u007fc\\u202ed\"}", escapeJsonForTerminal(json))
        val plain = "{\"summary\":\"Standup\"}"
        assertEquals(plain, escapeJsonForTerminal(plain))
    }
}

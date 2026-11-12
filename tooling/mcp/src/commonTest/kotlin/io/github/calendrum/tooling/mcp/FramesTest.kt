package io.github.calendrum.tooling.mcp

import kotlinx.io.Buffer
import kotlinx.io.writeString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FramesTest {
    private fun Frames.lines(text: String): List<String> {
        val chunk = Buffer()
        chunk.writeString(text)
        val lines = ArrayList<String>()
        while (true) {
            when (val frame = next(chunk) ?: break) {
                is Frame.Line -> lines += frame.text
                Frame.TooLong -> lines += "<too long>"
            }
        }
        return lines
    }

    @Test
    fun aLineCanComeInPieces() {
        val frames = Frames(100)
        assertEquals(emptyList(), frames.lines("ab"))
        assertEquals(emptyList(), frames.lines("c"))
        assertEquals(listOf("abcd", "e"), frames.lines("d\ne\nf"))
        assertEquals(listOf("f"), frames.lines("\n"))
    }

    @Test
    fun aCarriageReturnBeforeTheNewlineIsNotPartOfTheLine() {
        assertEquals(listOf("a", "b\rc", ""), Frames(100).lines("a\r\nb\rc\n\n"))
    }

    @Test
    fun aLineAboveTheLimitIsThrownAwayAndTheNextOneIsRead() {
        val frames = Frames(4)
        assertEquals(emptyList(), frames.lines("abcde"))
        assertEquals(emptyList(), frames.lines("fgh"))
        assertEquals(listOf("<too long>", "abcd"), frames.lines("i\nabcd\n"))
    }

    @Test
    fun nothingIsReadFromAnEmptyChunk() {
        assertNull(Frames(4).next(Buffer()))
        val chunk = Buffer()
        chunk.writeString("\n")
        assertIs<Frame.Line>(Frames(4).next(chunk))
    }

    @Test
    fun theDepthCountsOutsideStrings() {
        assertTrue(isWithinDepth("[[{}]]", 3))
        assertFalse(isWithinDepth("[[{}]]", 2))
        assertTrue(isWithinDepth("""["[[[[", "\"[[["]""", 1))
        assertFalse(isWithinDepth("[".repeat(100), 72))
        assertTrue(isWithinDepth("not json", 0))
    }
}

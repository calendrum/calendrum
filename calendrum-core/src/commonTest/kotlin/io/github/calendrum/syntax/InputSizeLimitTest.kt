package io.github.calendrum.syntax

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// `ParseLimits.maxInputOctets`: reading stops before the first logical line that does not end, with the
// byte after its line ending, within the limit; a byte array and a stream in any blocks stop at the same place.
class InputSizeLimitTest {
    private val text = "BEGIN:VCALENDAR\r\n" +
        "BEGIN:VEVENT\r\nUID:1\r\nSUMMARY:first\r\nEND:VEVENT\r\n" +
        "BEGIN:VEVENT\r\nUID:2\r\nDESCRIPTION:a folded\r\n  line\r\nEND:VEVENT\r\n" +
        "END:VCALENDAR\r\n"
    private val input = text.encodeToByteArray()

    private fun parser(limit: Long, mode: ParseMode = ParseMode.Lenient): ICalParser = ICal.parser(
        ParseOptions.builder().mode(mode).limits(ParseLimits.builder().maxInputOctets(limit).build()).build(),
    )

    // The array result, checked to be the result of every stream as well.
    private fun parse(bytes: ByteArray, limit: Long): ParseResult {
        val result = parser(limit).parse(bytes)
        val expected = renderResult(result)
        for (blockSize in listOf(1, 2, 7, 13, DEFAULT_BLOCK_SIZE)) {
            val stream = ChunkedInput(bytes, chunkPattern(blockSize))
            assertEquals(
                expected,
                renderParse {
                    parser(limit).parse(stream, blockSize, false)
                },
                "blocks of $blockSize",
            )
        }
        return result
    }

    @Test
    fun anInputWithinTheLimitIsReadWhole() {
        for (limit in listOf(input.size.toLong(), input.size + 1L, Long.MAX_VALUE)) {
            val result = parse(input, limit)
            assertFalse(result.isTruncated)
            assertEquals(emptyList(), result.problems)
            assertEquals(2, result.components.single().components.size)
        }
    }

    @Test
    fun readingStopsBeforeTheLineThatCrossesTheLimit() {
        val folded = text.indexOf("DESCRIPTION")
        for (limit in listOf(folded + 5, folded + 23, text.indexOf("END:VEVENT", folded))) {
            val result = parse(input, limit.toLong())
            assertTrue(result.isTruncated)
            val problem = result.problems.single()
            assertEquals(ProblemCode.InputTooLarge, problem.code)
            assertEquals("ICAL-S030", problem.code.id)
            assertEquals(Severity.Error, problem.severity)
            assertEquals(folded.toLong(), problem.span.startOffset, "limit $limit")
            assertEquals(8, problem.span.line)
            assertEquals(1, problem.span.column)
            val second = result.components.single().components[1]
            assertEquals(listOf("UID"), second.properties.map { it.name }, "limit $limit")
            assertEquals(0, result.problemCount(ProblemCode.UnclosedComponents))
        }
    }

    @Test
    fun aLineEndingAtTheLimitWaitsForTheByteAfterIt() {
        val next = text.indexOf("UID:2")
        val cut = parse(input, next.toLong())
        assertEquals((next - "BEGIN:VEVENT\r\n".length).toLong(), cut.problems.single().span.startOffset)
        assertEquals(1, cut.components.single().components.size)
        val kept = parse(input, next + 1L)
        assertEquals(next.toLong(), kept.problems.single().span.startOffset)
        assertEquals(2, kept.components.single().components.size)
    }

    @Test
    fun aLimitInsideTheFirstLineReadsNothing() {
        for (limit in listOf(1L, 16L, 17L)) {
            val result = parse(input, limit)
            assertEquals(emptyList(), result.components)
            assertEquals(1, result.problems.single().span.line)
            assertTrue(result.isTruncated)
        }
    }

    @Test
    fun otherEncodingsStopAtTheSamePlaceInArraysAndStreams() {
        val utf16 =
            byteArrayOf(0xFF.toByte(), 0xFE.toByte()) + text.flatMap { listOf(it.code.toByte(), 0) }.toByteArray()
        for (limit in 1L until utf16.size) {
            val result = parse(utf16, limit)
            assertTrue(result.isTruncated, "limit $limit")
            assertEquals(1, result.problemCount(ProblemCode.InputTooLarge))
        }
        val cyrillic = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nSUMMARY:Встреча\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val windows1251 = cyrillic.map { if (it.code < 0x80) it.code.toByte() else (it.code - 0x350).toByte() }
            .toByteArray()
        // Every limit, so that the charset is decided on a sample that the cut shortens in both.
        for (limit in 1L until windows1251.size) {
            val result = parse(windows1251, limit)
            assertTrue(result.isTruncated, "limit $limit")
        }
    }

    @Test
    fun strictModeThrows() {
        val failure = assertFailsWith<ICalParseException> { parser(50, ParseMode.Strict).parse(input) }
        assertEquals(ProblemCode.InputTooLarge, failure.problems.last().code)
    }

    @Test
    fun theLimitIsPositiveAndUntrustedHasOne() {
        assertFailsWith<IllegalArgumentException> { ParseLimits.builder().maxInputOctets(0).build() }
        assertEquals(Long.MAX_VALUE, ParseLimits.Default.maxInputOctets)
        assertTrue("maxInputOctets=none" in ParseLimits.Default.toString())
        assertTrue("maxInputOctets=10" in ParseLimits.builder().maxInputOctets(10).build().toString())
        assertEquals(64L * 1024 * 1024, ParseOptions.Untrusted.limits.maxInputOctets)
        assertEquals(Long.MAX_VALUE, ParseLimits.Default.withMaxDepth(3).maxInputOctets)
    }
}

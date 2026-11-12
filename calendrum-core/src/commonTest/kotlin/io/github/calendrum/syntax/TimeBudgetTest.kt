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
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource
import kotlin.time.TimeSource

class TimeBudgetTest {
    private class SteppingTimeSource(private val step: Duration) : TimeSource.WithComparableMarks {
        private val source = TestTimeSource()
        var marks: Int = 0
            private set

        override fun markNow(): ComparableTimeMark {
            marks++
            val mark = source.markNow()
            source += step
            return mark
        }
    }

    private val input: ByteArray = buildString {
        append("BEGIN:VCALENDAR\r\n")
        repeat(3_000) { append("BEGIN:VEVENT\r\nUID:").append(it).append("\r\nEND:VEVENT\r\n") }
        append("END:VCALENDAR\r\n")
    }.encodeToByteArray()

    private fun options(
        clock: SteppingTimeSource,
        mode: ParseMode = ParseMode.Lenient,
        maxTime: Duration = 10.milliseconds,
    ): ParseOptions = ParseOptions.builder()
        .mode(mode)
        .limits(ParseLimits.builder().maxTime(maxTime).build())
        .timeSource(clock)
        .build()

    @Test
    fun exceededBudgetTruncatesTheInput() {
        val clock = SteppingTimeSource(4.milliseconds)
        val result = ICal.parser(options(clock)).parse(input)
        assertEquals(4, clock.marks)
        assertTrue(result.isTruncated)
        val problem = result.problems.single()
        assertEquals(ProblemCode.TimeBudgetExceeded, problem.code)
        assertEquals(Severity.Error, problem.severity)
        assertEquals("ICAL-S029", problem.code.id)
        assertEquals(2049, problem.span.line)
        assertEquals(1, problem.span.column)
        assertEquals(1, result.problemCount(ProblemCode.TimeBudgetExceeded))
        val calendar = result.components.single()
        assertEquals(683, calendar.components.size)
        val offset = input.decodeToString().lineSequence().take(2048).sumOf { it.length + 2 }
        assertEquals(offset.toLong(), problem.span.startOffset)
    }

    @Test
    fun budgetIsCheckedOncePerThousandLinesWithoutCostOtherwise() {
        val generous = SteppingTimeSource(1.milliseconds)
        val result = ICal.parser(options(generous, maxTime = 1.seconds)).parse(input)
        assertFalse(result.isTruncated)
        assertEquals(1 + (9_002 + 1_023) / 1_024, generous.marks)
        val unused = SteppingTimeSource(1.milliseconds)
        ICal.parser(ParseOptions.builder().timeSource(unused).build()).parse(input)
        assertEquals(0, unused.marks)
    }

    @Test
    fun streamsStopAtTheSamePlace() {
        val expected = renderParse { ICal.parser(options(SteppingTimeSource(4.milliseconds))).parse(input) }
        for (blockSize in listOf(1, 13, DEFAULT_BLOCK_SIZE)) {
            val parser = ICal.parser(options(SteppingTimeSource(4.milliseconds)))
            val chunks = intArrayOf(blockSize)
            assertEquals(expected, renderParse { parser.parse(ChunkedInput(input, chunks), blockSize, false) })
        }
        val stream = ICal.parser(options(SteppingTimeSource(4.milliseconds))).components(input)
        assertEquals(683, stream.asSequence().count())
        assertEquals(listOf(ProblemCode.TimeBudgetExceeded), stream.problems.map { it.code })
        val reader = ICal.parser(options(SteppingTimeSource(4.milliseconds))).reader(input)
        var events = 0
        while (reader.next() != null) events++
        assertEquals(1 + 682 * 3 + 2 + 1, events)
    }

    @Test
    fun strictModeThrows() {
        val parser = ICal.parser(options(SteppingTimeSource(4.milliseconds), ParseMode.Strict))
        val failure = assertFailsWith<ICalParseException> { parser.parse(input) }
        assertEquals(ProblemCode.TimeBudgetExceeded, failure.problems.last().code)
    }

    @Test
    fun maxTimeMustBePositive() {
        assertFailsWith<IllegalArgumentException> { ParseLimits.builder().maxTime(Duration.ZERO).build() }
        assertFailsWith<IllegalArgumentException> { ParseLimits.builder().maxTime((-1).seconds).build() }
        assertEquals(null, ParseLimits.Default.maxTime)
        assertEquals(2.seconds, ParseLimits.builder().maxTime(2.seconds).build().maxTime)
        assertTrue("maxTime=2000ms" in ParseLimits.builder().maxTime(2.seconds).build().toString())
        assertTrue("maxTime=none" in ParseLimits.Default.toString())
        assertEquals(TimeSource.Monotonic, ParseOptions.Default.timeSource)
    }
}

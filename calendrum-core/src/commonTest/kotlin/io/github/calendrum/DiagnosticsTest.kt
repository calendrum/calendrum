package io.github.calendrum

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

// The constants of `ProblemCode` against the registry of the diagnostics, in order: id, severity, `of()`, the index,
// the message and the two flags of every code against a copy of the table that was published before the constants came
// from packed data (`expectedMessages`, `expectedFlags`).
class DiagnosticsTest {
    private val registry = listOf(
        ProblemCode.Utf8Bom to Severity.Info,
        ProblemCode.BareCr to Severity.Warning,
        ProblemCode.BareLf to Severity.Warning,
        ProblemCode.BlankLine to Severity.Warning,
        ProblemCode.ContinuationAtStart to Severity.Warning,
        ProblemCode.InvalidUtf8 to Severity.Warning,
        ProblemCode.MissingColon to Severity.Error,
        ProblemCode.InvalidName to Severity.Warning,
        ProblemCode.UnclosedQuote to Severity.Warning,
        ProblemCode.LineTooLong to Severity.Error,
        ProblemCode.UnsupportedEncoding to Severity.Error,
        ProblemCode.MalformedParameter to Severity.Warning,
        ProblemCode.TooManyProblems to Severity.Warning,
        ProblemCode.TooManyLines to Severity.Error,
        ProblemCode.EndWithoutBegin to Severity.Warning,
        ProblemCode.EndClosesNested to Severity.Warning,
        ProblemCode.UnmatchedEnd to Severity.Warning,
        ProblemCode.UnclosedComponents to Severity.Warning,
        ProblemCode.InvalidComponentName to Severity.Warning,
        ProblemCode.ComponentParameters to Severity.Warning,
        ProblemCode.PropertyOutsideComponent to Severity.Warning,
        ProblemCode.TooDeep to Severity.Error,
        ProblemCode.TooManyComponents to Severity.Error,
        ProblemCode.TranscodedInput to Severity.Info,
        ProblemCode.DetectedCharset to Severity.Warning,
        ProblemCode.UnknownCharsetHint to Severity.Warning,
        ProblemCode.CharsetHintContradictsData to Severity.Warning,
        ProblemCode.LowCharsetConfidence to Severity.Warning,
        ProblemCode.TimeBudgetExceeded to Severity.Error,
        ProblemCode.InputTooLarge to Severity.Error,
    )

    @Test
    fun constantsCoverTheRegistryInOrder() {
        for ((index, entry) in registry.withIndex()) {
            val (code, severity) = entry
            val id = "ICAL-S" + (index + 1).toString().padStart(3, '0')
            assertEquals(id, code.id)
            assertEquals(severity, code.severity, id)
            assertEquals(id, code.toString())
            assertSame(code, ProblemCode.of(id))
            assertSame(code, ProblemCode.of(id.lowercase()))
        }
        assertEquals(registry.size, ProblemCode.SYNTAX_COUNT)
        assertEquals(registry.map { it.first }, ProblemCode.known.take(ProblemCode.SYNTAX_COUNT))
    }

    @Test
    fun unknownCodesAreOpen() {
        val future = ProblemCode.of("ical-t010")
        assertEquals("ICAL-T010", future.id)
        assertEquals(Severity.Warning, future.severity)
        assertEquals(future, ProblemCode.of("ICAL-T010"))
        assertEquals(future.hashCode(), ProblemCode.of("ICAL-T010").hashCode())
        assertNotEquals(future, ProblemCode.BareLf)
        assertFailsWith<IllegalArgumentException> { ProblemCode.of("") }
    }

    @Test
    fun spansCompareByValue() {
        val span = SourceSpan(3, 10, 2, 4)
        assertEquals(span, SourceSpan(3, 10, 2, 4))
        assertEquals(span.hashCode(), SourceSpan(3, 10, 2, 4).hashCode())
        assertNotEquals(span, SourceSpan(3, 11, 2, 4))
        assertEquals("2:4 (offsets 3..10)", span.toString())
    }

    @Test
    fun problemsDescribeThemselvesWithoutData() {
        val problem = Problem(ProblemCode.BareLf, SourceSpan(12, 12, 3, 7), null)
        assertEquals(Severity.Warning, problem.severity)
        assertEquals("bare LF used as a line ending", problem.message)
        assertEquals("ICAL-S003 Warning: bare LF used as a line ending at 3:7 (offset 12)", problem.toString())
        val detailed = Problem(ProblemCode.TranscodedInput, SourceSpan(0, 0, 1, 1), "UTF-16LE, byte order mark")
        assertEquals("input decoded to UTF-8: UTF-16LE, byte order mark", detailed.message)
    }

    @Test
    fun parseExceptionKeepsACopyOfTheProblems() {
        val warning = Problem(ProblemCode.BareLf, SourceSpan(5, 5, 1, 6), null)
        val error = Problem(ProblemCode.MissingColon, SourceSpan(6, 6, 2, 1), null)
        val problems = mutableListOf(warning, error)
        val exception = ICalParseException(problems)
        problems.clear()
        assertEquals(listOf(warning, error), exception.problems)
        assertEquals(error.toString(), exception.message)
        assertTrue(exception.message!!.startsWith("ICAL-S007"))
        assertFalse(ICalParseException(emptyList()).message.isNullOrEmpty())
    }

    @Test
    fun constantsKeepTheirIndexMessageAndFlags() {
        assertEquals(expectedMessages.size, ProblemCode.SYNTAX_COUNT)
        assertEquals(expectedFlags.length, ProblemCode.SYNTAX_COUNT)
        for ((index, code) in ProblemCode.known.take(ProblemCode.SYNTAX_COUNT).withIndex()) {
            assertEquals(index, code.index, code.id)
            assertEquals(expectedMessages[index], code.message, code.id)
            assertEquals(expectedFlags[index] == '1', code.oncePerInput, code.id)
            assertEquals(expectedFlags[index] == '2', code.bypassesLimit, code.id)
        }
        val future = ProblemCode.of("ICAL-S031")
        assertEquals(-1, future.index)
        assertEquals("ICAL-S031", future.message)
        assertFalse(future.oncePerInput)
        assertFalse(future.bypassesLimit)
    }

    private val expectedFlags = "011100000020020000000022222222"

    private val expectedMessages = listOf(
        "UTF-8 byte order mark skipped",
        "bare CR used as a line ending",
        "bare LF used as a line ending",
        "blank line skipped",
        "continuation line without a previous line",
        "invalid UTF-8 sequence replaced with U+FFFD",
        "no ':' after the property name and parameters, line skipped",
        "invalid or empty property or parameter name",
        "unclosed quoted parameter value",
        "logical line exceeds the length limit, line skipped",
        "unsupported encoding, input not read",
        "malformed parameter",
        "problem limit reached, further problems are only counted",
        "logical line limit reached, input truncated",
        "END without an open component, ignored",
        "END closes nested components implicitly",
        "END does not match any open component, ignored",
        "input ended with open components, closed implicitly",
        "empty or invalid component name in BEGIN or END",
        "parameters on BEGIN or END ignored",
        "property outside any component",
        "component nesting exceeds the depth limit, subtree skipped",
        "component limit reached, input truncated",
        "input decoded to UTF-8",
        "input encoding detected by a heuristic",
        "unknown charset hint ignored",
        "charset hint contradicts the data, the data decides",
        "low confidence in the detected encoding",
        "time budget exceeded, input truncated",
        "input size limit reached, input truncated",
    )
}

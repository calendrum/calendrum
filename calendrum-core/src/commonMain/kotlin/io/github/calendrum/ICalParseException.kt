package io.github.calendrum

import io.github.calendrum.syntax.ArrayView

/**
 * Thrown by a parser in strict mode when the input has a problem of severity [Severity.Error].
 *
 * The parser stops at the first error, so [problems] ends with it. In lenient mode parsing never throws on data.
 *
 * The message is the text of the last error in [problems]. It never contains property values.
 */
public class ICalParseException(problems: List<Problem>) : Exception(parseExceptionMessage(problems)) {
    /** The problems found up to and including the error that stopped the parse, in the order they were found. */
    public val problems: List<Problem> = ArrayView(problems.toTypedArray())
}

private fun parseExceptionMessage(problems: List<Problem>): String =
    problems.lastOrNull { it.severity == Severity.Error }?.toString() ?: "iCalendar input rejected"

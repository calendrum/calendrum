@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * A problem found while reading, decoding or checking iCalendar data: a stable [code], a [severity], a position and a
 * short message.
 *
 * Problems are reported instead of thrown wherever the data can be read at all. The text never contains property or
 * parameter values, so a problem can be logged without leaking user data. See [ProblemCode] for every code.
 */
public class Problem internal constructor(
    /** The stable code of the problem, such as `ICAL-S003`. */
    public val code: ProblemCode,
    /**
     * Where in the input the problem was found; a point, so [SourceSpan.startOffset] equals [SourceSpan.endOffset].
     *
     * Nodes built by hand or edited by an interceptor have no position in the original input; see [SourceSpan].
     */
    public val span: SourceSpan,
    internal val detail: String?,
) {
    /** The severity of [code]. */
    public val severity: Severity get() = code.severity

    /**
     * A short human-readable text: the fixed message of the code, followed by an explanation after a colon when the
     * code has one (for example the name of a detected charset). It never contains data from the input.
     */
    public val message: String get() = if (detail == null) code.message else "${code.message}: $detail"

    /**
     * Returns the code, severity, message and position, such as `ICAL-S003 Warning: bare LF used as a line ending at
     * 3:7 (offset 12)`. It never contains values from the input.
     */
    override fun toString(): String =
        "${code.id} ${code.severity}: $message at ${span.line}:${span.column} (offset ${span.startOffset})"
}

// Problems for sibling modules (jCal) without a position in an iCalendar text: the span is empty, the detail names the
// place.

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun problemOf(code: ProblemCode, detail: String?): Problem = Problem(code, SourceSpan(0L, 0L, 0, 0), detail)

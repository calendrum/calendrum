package io.github.calendrum.syntax

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import kotlin.concurrent.Volatile

// `problems` are what the listener got; `counts` are the counters of the lexer, so `problemCount` includes problems
// beyond the limit. Truncated means the line or component limit stopped the parse (ICAL-S014, ICAL-S023).
/**
 * The outcome of parsing: the tree, the problems found and facts about the input.
 *
 * A result is immutable. [components] are the top-level components, usually a single `VCALENDAR`. Reading values from
 * the tree is lazy and cached in the tree; use `calendars` of the typed model for wrappers with typed accessors.
 */
public class ParseResult internal constructor(
    componentArray: Array<ICalComponent>,
    problemArray: Array<Problem>,
    private val counts: IntArray,
    private val valueCounts: IntArray?,
    /** The canonical name of the encoding the input was read in, such as `UTF-8` or `windows-1251`. */
    public val charset: String,
    /**
     * How sure the library is of [charset], in percent: 100 when the encoding is certain (a byte order mark, UTF-8, a
     * confirmed hint) and lower for a guess by the heuristic.
     */
    public val charsetConfidence: Int,
    /**
     * Whether the tree is a selection of the input (see [ParseOptions.selection]). A partial tree cannot be written
     * back unless `WriteOptions.allowPartialWrite` is set.
     */
    public val isPartial: Boolean,
) {
    /** The top-level components in input order. A property outside any component is not part of the tree. */
    public val components: List<ICalComponent> = ArrayView(componentArray)

    /**
     * The reported problems, in the order they were found. There are at most [ParseLimits.maxProblems] of them; see
     * [problemCount] for the real number.
     */
    public val problems: List<Problem> = ArrayView(problemArray)

    /**
     * Whether parsing stopped before the end of the input because of a limit on lines, components or the input size
     * or of the time budget (`ICAL-S014`, `ICAL-S023`, `ICAL-S030`, `ICAL-S029`). Then [components] is a partial
     * result.
     */
    public val isTruncated: Boolean
        get() = counts[ProblemCode.TooManyLines.index] > 0 ||
            counts[ProblemCode.TooManyComponents.index] > 0 ||
            counts[ProblemCode.TimeBudgetExceeded.index] > 0 ||
            counts[ProblemCode.InputTooLarge.index] > 0

    @Volatile
    internal var calendarsCache: Any? = null

    /**
     * Returns how many problems with [code] were found, including those beyond the reporting limit that are only
     * counted.
     *
     * @param code the code to count.
     */
    public fun problemCount(code: ProblemCode): Int {
        val index = code.index
        if (index < 0) return 0
        if (index < counts.size) return counts[index]
        val values = valueCounts ?: return 0
        val value = index - ProblemCode.SYNTAX_COUNT
        return if (value < values.size) values[value] else 0
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe(
        "ParseResult",
        "components", components.size,
        "problems", problems.size,
        "truncated", isTruncated,
        "partial", isPartial,
        "charset", charset,
    )
}

package io.github.calendrum.syntax

import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

// The constructor is internal, not private: a private one called from `Builder` or the companion would get a public
// synthetic accessor with `DefaultConstructorMarker` in the JVM ABI (Java 8 bytecode has no nestmates).
/**
 * Upper bounds on what a parser accepts, so that hostile or broken input cannot use unlimited memory or time.
 *
 * When a bound is hit, the parser reports a problem and either skips the offending part or stops with a truncated
 * result; see the documentation of each property. Build limits with [builder]; [Default] is generous and fits files you
 * trust, while [ParseOptions.Untrusted] uses much tighter ones. A value of this class is immutable.
 *
 * @property maxLineOctets the longest logical line, after unfolding, in bytes; a longer line is skipped (`ICAL-S010`).
 * @property maxLines the most logical lines; reading stops there (`ICAL-S014`).
 * @property maxProblems the most problems that are reported; later ones are only counted (`ICAL-S013`).
 * @property maxDepth the deepest nesting of components; deeper subtrees are skipped (`ICAL-S022`).
 * @property maxComponents the most components; reading stops there (`ICAL-S023`).
 * @property maxInputOctets the most bytes of input, as given before any transcoding, or [Long.MAX_VALUE] for no limit
 *   (the default). Reading stops before the first logical line that does not end within that many bytes, the byte that
 *   follows its line ending included (`ICAL-S030`), and the result is truncated. A byte array is checked once before it
 *   is read; a stream counts the bytes of each block it reads and reads at most one block past the limit.
 */
public class ParseLimits internal constructor(
    public val maxLineOctets: Int,
    public val maxLines: Int,
    public val maxProblems: Int,
    public val maxDepth: Int,
    public val maxComponents: Int,
    internal val maxTimeNanos: Long,
    public val maxInputOctets: Long,
) {
    /**
     * The time budget of one parse, measured by [ParseOptions.timeSource], or `null` for none.
     *
     * When the budget runs out the parser stops at a line boundary with `ICAL-S029` and a truncated result. A cursor
     * and a component stream count the time of the code between events, too.
     */
    @get:JvmName("getMaxTime")
    public val maxTime: Duration? get() = if (maxTimeNanos < 0) null else maxTimeNanos.nanoseconds

    init {
        require(maxLineOctets > 0) { "maxLineOctets must be positive" }
        require(maxLines > 0) { "maxLines must be positive" }
        require(maxProblems >= 0) { "maxProblems must not be negative" }
        require(maxDepth > 0) { "maxDepth must be positive" }
        require(maxComponents > 0) { "maxComponents must be positive" }
        require(maxTimeNanos == NO_TIME_LIMIT || maxTimeNanos > 0) { "maxTime must be positive" }
        require(maxInputOctets > 0) { "maxInputOctets must be positive" }
    }

    // A copy with another depth, for `parseComponent`.
    internal fun withMaxDepth(depth: Int): ParseLimits =
        ParseLimits(maxLineOctets, maxLines, maxProblems, depth, maxComponents, maxTimeNanos, maxInputOctets)

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe(
        "ParseLimits",
        "maxLineOctets", maxLineOctets,
        "maxLines", maxLines,
        "maxProblems", maxProblems,
        "maxDepth", maxDepth,
        "maxComponents", maxComponents,
        "maxTime", if (maxTimeNanos < 0) "none" else "${maxTimeNanos / NANOS_PER_MILLI}ms",
        "maxInputOctets", if (maxInputOctets == NO_INPUT_LIMIT) "none" else maxInputOctets,
    )

    // A `var` for Kotlin DSL assignment and a fluent function of the same name for Java; `build()` validates.

    /**
     * Collects [ParseLimits] values. The properties and the functions of the same name do the same; the functions
     * chain.
     */
    public class Builder internal constructor() {
        /** The longest logical line in bytes; positive. The default is 8 MiB. */
        public var maxLineOctets: Int = DEFAULT_MAX_LINE_OCTETS

        /** The most logical lines; positive. The default is 10,000,000. */
        public var maxLines: Int = DEFAULT_MAX_LINES

        /** The most reported problems; zero or more. The default is 1,000. */
        public var maxProblems: Int = DEFAULT_MAX_PROBLEMS

        /** The deepest component nesting; positive. The default is 16. */
        public var maxDepth: Int = DEFAULT_MAX_DEPTH

        /** The most components; positive. The default is 1,000,000. */
        public var maxComponents: Int = DEFAULT_MAX_COMPONENTS

        /** The most bytes of input; positive. The default, [Long.MAX_VALUE], means no limit. */
        public var maxInputOctets: Long = NO_INPUT_LIMIT

        /** The time budget of one parse, or `null` for none (the default); positive. */
        @get:JvmName("getMaxTime")
        @set:JvmName("setMaxTime")
        public var maxTime: Duration? = null

        /** Sets [maxLineOctets] to [value]. */
        public fun maxLineOctets(value: Int): Builder {
            maxLineOctets = value
            return this
        }

        /** Sets [maxLines] to [value]. */
        public fun maxLines(value: Int): Builder {
            maxLines = value
            return this
        }

        /** Sets [maxProblems] to [value]. */
        public fun maxProblems(value: Int): Builder {
            maxProblems = value
            return this
        }

        /** Sets [maxDepth] to [value]. */
        public fun maxDepth(value: Int): Builder {
            maxDepth = value
            return this
        }

        /** Sets [maxComponents] to [value]. */
        public fun maxComponents(value: Int): Builder {
            maxComponents = value
            return this
        }

        /** Sets [maxInputOctets] to [value]. */
        public fun maxInputOctets(value: Long): Builder {
            maxInputOctets = value
            return this
        }

        /** Sets [maxTime] to [value]; `null` removes the budget. */
        @JvmName("maxTime")
        public fun maxTime(value: Duration?): Builder {
            maxTime = value
            return this
        }

        /**
         * Creates the limits.
         *
         * @throws IllegalArgumentException if a bound is not positive (or, for [maxProblems], negative), or the time is
         *   not positive.
         */
        public fun build(): ParseLimits {
            val time = maxTime
            require(time == null || time.isPositive()) { "maxTime must be positive" }
            val nanos = time?.inWholeNanoseconds ?: NO_TIME_LIMIT
            return ParseLimits(maxLineOctets, maxLines, maxProblems, maxDepth, maxComponents, nanos, maxInputOctets)
        }
    }

    /** The default limits and a builder. */
    public companion object {
        /**
         * Generous limits for files you trust: lines up to 8 MiB, 10 million lines, 1,000 reported problems, nesting
         * depth 16, one million components, no limit on the input size and no time budget.
         */
        @JvmField
        public val Default: ParseLimits = ParseLimits(
            DEFAULT_MAX_LINE_OCTETS,
            DEFAULT_MAX_LINES,
            DEFAULT_MAX_PROBLEMS,
            DEFAULT_MAX_DEPTH,
            DEFAULT_MAX_COMPONENTS,
            NO_TIME_LIMIT,
            NO_INPUT_LIMIT,
        )

        /** Returns a new builder that starts from the default limits. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

// 8 MiB per logical line is enough for inline base64 `ATTACH;VALUE=BINARY` attachments (Outlook exports them this way)
// and caps the fold buffer. 10 million logical lines is five times the 100 MB benchmark feed (about 2 million). 1000
// reported problems is more than a person reads; the rest are counted. Depth 16: real files nest at most 4 levels
// (VCALENDAR, VEVENT, VALARM, VLOCATION from RFC 9074); the limit only has to stop hostile nesting. One million
// components is five times the 100 MB feed (about 185 thousand).
private const val DEFAULT_MAX_LINE_OCTETS = 8 * 1024 * 1024
private const val DEFAULT_MAX_LINES = 10_000_000
private const val DEFAULT_MAX_PROBLEMS = 1_000
private const val DEFAULT_MAX_DEPTH = 16
private const val DEFAULT_MAX_COMPONENTS = 1_000_000
private const val NANOS_PER_MILLI = 1_000_000L

internal const val NO_TIME_LIMIT: Long = -1L

internal const val NO_INPUT_LIMIT: Long = Long.MAX_VALUE

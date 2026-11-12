package io.github.calendrum.recur

import io.github.calendrum.ProblemListener
import io.github.calendrum.syntax.describe
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * What to do when `DTSTART` is not one of the dates its own rule produces, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined (`ICAL-R003`).
 */
public enum class StartMismatch {
    /**
     * Follows the standard literally: `DTSTART` is the first occurrence and counts toward `COUNT`. This is the default.
     */
    Rfc,

    /** `DTSTART` is not an occurrence unless the rule produces it. */
    Exclude,

    /** `DTSTART` is an occurrence but does not count toward `COUNT`. */
    Include,
}

/**
 * Bounds on the work of an expansion, so that a rule such as `FREQ=SECONDLY` or an impossible one cannot run forever.
 *
 * When a bound is reached the sequence simply ends and a problem is reported; nothing is thrown. Immutable.
 *
 * @property maxOccurrences the most dates one expansion produces (`ICAL-R001` when it is reached); a rule with `COUNT`,
 *   which cannot jump to the start of a range, also passes at most this many dates before it.
 * @property maxIterationsWithoutMatch the most consecutive periods without a single date before the expansion gives up
 *   (`ICAL-R002`).
 */
public class RecurrenceLimits private constructor(
    public val maxOccurrences: Int,
    public val maxIterationsWithoutMatch: Int,
) {
    /** Returns the two bounds. */
    override fun toString(): String = describe(
        "RecurrenceLimits",
        "maxOccurrences",
        maxOccurrences,
        "maxIterationsWithoutMatch",
        maxIterationsWithoutMatch,
    )

    /**
     * Collects [RecurrenceLimits] values. The properties and the functions of the same name do the same. Not
     * thread-safe.
     */
    public class Builder internal constructor() {
        /** The most dates one expansion produces; positive. The default is 1,000,000. */
        public var maxOccurrences: Int = DEFAULT_MAX_OCCURRENCES

        /** The most consecutive empty periods; positive. The default is 10,000. */
        public var maxIterationsWithoutMatch: Int = DEFAULT_MAX_ITERATIONS

        /** Sets [maxOccurrences] to [value]. */
        public fun maxOccurrences(value: Int): Builder {
            maxOccurrences = value
            return this
        }

        /** Sets [maxIterationsWithoutMatch] to [value]. */
        public fun maxIterationsWithoutMatch(value: Int): Builder {
            maxIterationsWithoutMatch = value
            return this
        }

        /**
         * Creates the limits.
         *
         * @throws IllegalArgumentException if a bound is not positive.
         */
        public fun build(): RecurrenceLimits {
            require(maxOccurrences > 0) { "maxOccurrences must be positive" }
            require(maxIterationsWithoutMatch > 0) { "maxIterationsWithoutMatch must be positive" }
            return RecurrenceLimits(maxOccurrences, maxIterationsWithoutMatch)
        }

        /** Returns the name of the builder. */
        override fun toString(): String = "RecurrenceLimits.Builder"
    }

    /** The default limits and a builder. */
    public companion object {
        /** 1,000,000 dates and 10,000 empty periods in a row. */
        @JvmField
        public val Default: RecurrenceLimits = RecurrenceLimits(DEFAULT_MAX_OCCURRENCES, DEFAULT_MAX_ITERATIONS)

        /** Returns a builder that starts from the default limits. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

/**
 * Settings of an expansion: limits, the treatment of a `DTSTART` outside the rule, a problem listener and the offset of
 * a UTC `UNTIL`.
 *
 * Immutable and thread-safe. Problems are reported once per expansion and per code, with no limit or counter.
 *
 * @property limits the bounds on the work of one expansion.
 * @property startMismatch what to do with a `DTSTART` that the rule does not produce.
 * @property problemListener receives the problems (`ICAL-R001` to `ICAL-R009`), or `null`.
 * @property untilOffset gives the UTC offset in force at a local time, to compare a UTC `UNTIL` with a zoned `DTSTART`
 *   exactly; `null` compares with the local time of `UNTIL` instead (`ICAL-R006`).
 */
public class RecurrenceOptions private constructor(
    public val limits: RecurrenceLimits,
    public val startMismatch: StartMismatch,
    public val problemListener: ProblemListener?,
    public val untilOffset: ((LocalDateTime) -> UtcOffset)?,
    internal val localOffset: LocalOffset?,
) {
    /** Returns the settings; the listener and the offset function only as set or none. */
    override fun toString(): String = describe(
        "RecurrenceOptions",
        "limits",
        limits,
        "startMismatch",
        startMismatch,
        "problemListener",
        if (problemListener == null) "none" else "set",
        "untilOffset",
        if (untilOffset == null) "none" else "set",
    )

    /**
     * Collects [RecurrenceOptions] values. The properties and the functions of the same name do the same. Not
     * thread-safe.
     */
    public class Builder internal constructor() {
        /** The bounds on one expansion; [RecurrenceLimits.Default] by default. */
        public var limits: RecurrenceLimits = RecurrenceLimits.Default

        /** What to do with a `DTSTART` outside the rule; [StartMismatch.Rfc] by default. */
        public var startMismatch: StartMismatch = StartMismatch.Rfc

        /** Receives the problems; `null` (the default) for none. */
        public var problemListener: ProblemListener? = null

        /** The UTC offset in force at a local time, for a UTC `UNTIL` against a zoned `DTSTART`; `null` by default. */
        public var untilOffset: ((LocalDateTime) -> UtcOffset)? = null

        /** Sets [limits] to [value]. */
        public fun limits(value: RecurrenceLimits): Builder {
            limits = value
            return this
        }

        /** Sets [startMismatch] to [value]. */
        public fun startMismatch(value: StartMismatch): Builder {
            startMismatch = value
            return this
        }

        /** Sets [problemListener] to [value]. */
        public fun problemListener(value: ProblemListener?): Builder {
            problemListener = value
            return this
        }

        /** Sets [untilOffset] to [value]. */
        public fun untilOffset(value: ((LocalDateTime) -> UtcOffset)?): Builder {
            untilOffset = value
            return this
        }

        /** Creates the options. */
        public fun build(): RecurrenceOptions = RecurrenceOptions(
            limits,
            startMismatch,
            problemListener,
            untilOffset,
            untilOffset?.let(::PublicLocalOffset),
        )

        /** Returns the name of the builder. */
        override fun toString(): String = "RecurrenceOptions.Builder"
    }

    /** The default options and a builder. */
    public companion object {
        /** Default limits, [StartMismatch.Rfc], no listener and no offset function. */
        @JvmField
        public val Default: RecurrenceOptions =
            RecurrenceOptions(RecurrenceLimits.Default, StartMismatch.Rfc, null, null, null)

        /** Returns a builder that starts from the default options. */
        @JvmStatic
        public fun builder(): Builder = Builder()

        internal fun create(
            limits: RecurrenceLimits,
            startMismatch: StartMismatch,
            problemListener: ProblemListener?,
            untilOffset: ((LocalDateTime) -> UtcOffset)?,
            localOffset: LocalOffset?,
        ): RecurrenceOptions = RecurrenceOptions(limits, startMismatch, problemListener, untilOffset, localOffset)
    }
}

// The UNTIL offset in seconds of local time. The public `untilOffset` takes kotlinx-datetime types; it is wrapped
// (`PublicLocalOffset`) only by `Builder.build()`, and the engines build their options with `create`, so the default
// path never makes a LocalDateTime or a UtcOffset.
internal fun interface LocalOffset {
    fun offsetSeconds(localSecond: Long): Int
}

internal class PublicLocalOffset(private val function: (LocalDateTime) -> UtcOffset) : LocalOffset {
    override fun offsetSeconds(localSecond: Long): Int {
        val day = floorDiv(localSecond, SECONDS_PER_DAY_LONG).toInt()
        return function(localDateTime(day, (localSecond - day * SECONDS_PER_DAY_LONG).toInt())).totalSeconds
    }

    override fun toString(): String = "PublicLocalOffset"
}

private const val DEFAULT_MAX_OCCURRENCES = 1_000_000
private const val DEFAULT_MAX_ITERATIONS = 10_000

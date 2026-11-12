package io.github.calendrum.syntax

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICalDsl
import io.github.calendrum.ProblemListener
import io.github.calendrum.model.EagerDecoding
import io.github.calendrum.model.ICalRegistry
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

// Immutable options; the internal constructor has the same reason as that of `ParseLimits`.
/**
 * Settings of a parser: mode, limits, problem listener, retention of the source, charset hint, interceptors, selection
 * and registry.
 *
 * Build options with [builder] or with the `ICal.parser { ... }` DSL. The class is immutable and can be shared between
 * threads; [Default] is lenient with the default limits, and [Untrusted] is meant for files from strangers.
 *
 * @property mode lenient or strict reading.
 * @property limits the bounds on size, depth and time.
 * @property problemListener receives problems as they are found, or `null`.
 * @property retainSource whether the tree keeps references to the input, which saves copying; see
 *   [Builder.retainSource].
 * @property charset the charset hint for byte input, or `null`.
 * @property interceptors the factories of the line interceptors, applied in order.
 */
public class ParseOptions internal constructor(
    public val mode: ParseMode,
    public val limits: ParseLimits,
    public val problemListener: ProblemListener?,
    public val retainSource: Boolean,
    public val charset: String?,
    @property:ExperimentalICalApi
    public val interceptors: List<() -> LineInterceptor>,
    internal val clock: TimeSource.WithComparableMarks?,
    internal val budget: TimeBudget?,
    internal val valueHook: ValueHook?,
    private val chosen: Selection?,
    private val chosenRegistry: ICalRegistry?,
) {
    /**
     * The registry that tells eager decoding and strict mode what every property is; [ICalRegistry.Default] unless one
     * was set.
     */
    public val registry: ICalRegistry get() = chosenRegistry ?: ICalRegistry.Default

    /** Which components and properties are read; [Selection.Full] unless a selection was set. */
    public val selection: Selection get() = chosen ?: Selection.Full

    internal val contentSelection: ContentSelection get() = chosen?.content ?: ContentSelection.Full

    internal val referencedTimeZones: Boolean get() = chosen?.timeZones == TimeZoneSelection.Referenced

    internal val partial: Boolean get() = chosen != null && !chosen.isFull

    /** The clock used for the time budget; the monotonic clock unless one was set. */
    public val timeSource: TimeSource.WithComparableMarks get() = clock ?: TimeSource.Monotonic

    /**
     * Whether values are decoded while parsing, so that value problems appear in the result. Always true in strict
     * mode.
     */
    public val decodeEagerly: Boolean get() = valueHook != null

    /** Returns a short description for diagnostics; its format is not a contract. */
    @OptIn(ExperimentalICalApi::class)
    override fun toString(): String = describe(
        "ParseOptions",
        "mode", mode,
        "limits", limits,
        "problemListener", if (problemListener == null) "none" else "set",
        "retainSource", retainSource,
        "charset", charset,
        "timeSource", clock ?: "monotonic",
        "decodeEagerly", decodeEagerly,
        "interceptors", interceptors.size,
        "selection", chosen ?: "full",
        "registry", chosenRegistry ?: "default",
    )

    /**
     * Collects [ParseOptions] values. The properties and the functions of the same name do the same; the functions
     * chain. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor() {
        /** Lenient (the default) or strict reading. */
        public var mode: ParseMode = ParseMode.Lenient

        /** Decode every value while parsing and report its problems; false by default. Strict mode implies it. */
        public var decodeEagerly: Boolean = false

        /** The registry for eager decoding and strict mode; `null` selects [ICalRegistry.Default]. */
        public var registry: ICalRegistry? = null

        /** The bounds on the input; [ParseLimits.Default] by default. */
        public var limits: ParseLimits = ParseLimits.Default

        /** Receives each reported problem as it is found; `null` (the default) for none. */
        public var problemListener: ProblemListener? = null

        /**
         * Whether the tree keeps references to the input bytes (true, the default) or copies the lines it needs.
         * Retaining is faster and allocates less, but holds the whole input as long as any node lives; copying lets a
         * small selection outlive a big input.
         */
        public var retainSource: Boolean = true

        /**
         * A hint for the encoding of byte input: a charset name or a whole `Content-Type` value such as `text/calendar;
         * charset=windows-1251`. The hint is checked against the data; a contradiction is reported (`ICAL-S027`) and
         * the data decides. `null` (the default) means no hint.
         */
        public var charset: String? = null

        /**
         * The clock used for [ParseLimits.maxTime]; the monotonic clock by default. Set a test clock to make the budget
         * deterministic.
         */
        public var timeSource: TimeSource.WithComparableMarks = TimeSource.Monotonic

        /** Which components and properties to read; `null` (the default) reads everything. */
        public var selection: Selection? = null
        private val factories = ArrayList<() -> LineInterceptor>()

        /** Reads only what [selection] names. The rest is skipped in the lexer; the result is partial. */
        public fun select(selection: Selection): Builder {
            this.selection = selection
            return this
        }

        /** Reads only what the selection built by [block] names. */
        public fun select(block: Selection.Builder.() -> Unit): Builder =
            select(Selection.builder().apply(block).build())

        /** Sets [registry]; `null` selects the default registry. */
        public fun registry(registry: ICalRegistry?): Builder {
            this.registry = registry
            return this
        }

        /** Sets [mode]. */
        public fun mode(mode: ParseMode): Builder {
            this.mode = mode
            return this
        }

        /** Sets [limits]. */
        public fun limits(limits: ParseLimits): Builder {
            this.limits = limits
            return this
        }

        /** Sets the problem listener; `null` removes it. */
        public fun problemListener(listener: ProblemListener?): Builder {
            problemListener = listener
            return this
        }

        /** Sets [retainSource]. */
        public fun retainSource(retain: Boolean): Builder {
            retainSource = retain
            return this
        }

        /** Sets the charset hint; `null` removes it. */
        public fun charset(charset: String?): Builder {
            this.charset = charset
            return this
        }

        /** Sets the clock for the time budget. */
        public fun timeSource(source: TimeSource.WithComparableMarks): Builder {
            timeSource = source
            return this
        }

        /** Sets [decodeEagerly]. */
        public fun decodeEagerly(eager: Boolean): Builder {
            decodeEagerly = eager
            return this
        }

        /**
         * Adds an interceptor created by [factory] for every parse, so that a stateful interceptor gets a fresh
         * instance each time. Interceptors run in the order they were added.
         */
        @ExperimentalICalApi
        public fun intercept(factory: () -> LineInterceptor): Builder {
            factories += factory
            return this
        }

        // Registers a factory that returns the same instance, for stateless interceptors and the `intercept { line -> …
        // }` DSL; a lambda without a parameter resolves to `intercept(factory)`.

        /** Adds a stateless [interceptor]; the same instance serves every parse and must be thread-safe. */
        @ExperimentalICalApi
        public fun intercept(interceptor: LineInterceptor): Builder {
            factories += { interceptor }
            return this
        }

        /** Creates the options. */
        public fun build(): ParseOptions {
            val nanos = limits.maxTimeNanos
            val budget = if (nanos < 0) null else ClockBudget(timeSource, nanos)
            val interceptors = if (factories.isEmpty()) emptyList() else ArrayView(factories.toTypedArray())
            val values = if (decodeEagerly || mode == ParseMode.Strict) EagerDecoding(registry) else null
            return ParseOptions(
                mode,
                limits,
                problemListener,
                retainSource,
                charset,
                interceptors,
                timeSource,
                budget,
                values,
                selection,
                registry,
            )
        }
    }

    /** The shared defaults and a builder. */
    public companion object {
        /** Lenient mode, the default limits, a retained source and no listener, hint, interceptors or selection. */
        @JvmField
        public val Default: ParseOptions =
            ParseOptions(
                ParseMode.Lenient,
                ParseLimits.Default,
                null,
                true,
                null,
                emptyList(),
                null,
                null,
                null,
                null,
                null,
            )

        @Volatile
        private var untrusted: ParseOptions? = null

        // A getter with a cached instance, not a field: a field of the companion would be built together with
        // `Default`, and its time budget would bring kotlin.time into every application.

        /**
         * Options for files from strangers: lenient, but with nesting depth 8, lines up to 64 KiB, 1,000,000 lines,
         * 100,000 components, input up to 64 MiB and a time budget of 2 seconds.
         */
        @JvmStatic
        public val Untrusted: ParseOptions
            get() = untrusted ?: builder().limits(untrustedLimits()).build().also { untrusted = it }

        /** Returns a builder that starts from the defaults. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

private fun untrustedLimits(): ParseLimits = ParseLimits.builder()
    .maxLineOctets(UNTRUSTED_LINE_OCTETS)
    .maxLines(UNTRUSTED_LINES)
    .maxDepth(UNTRUSTED_DEPTH)
    .maxComponents(UNTRUSTED_COMPONENTS)
    .maxProblems(UNTRUSTED_PROBLEMS)
    .maxTime(UNTRUSTED_SECONDS.seconds)
    .maxInputOctets(UNTRUSTED_INPUT_OCTETS)
    .build()

private const val UNTRUSTED_LINE_OCTETS = 64 * 1024
private const val UNTRUSTED_LINES = 1_000_000
private const val UNTRUSTED_DEPTH = 8
private const val UNTRUSTED_COMPONENTS = 100_000
private const val UNTRUSTED_PROBLEMS = 1_000
private const val UNTRUSTED_SECONDS = 2

// 64 MiB: an untrusted upload or response this large is no calendar a person keeps; the line and component limits
// alone let 1,000,000 lines of 64 KiB through.
private const val UNTRUSTED_INPUT_OCTETS = 64L * 1024 * 1024

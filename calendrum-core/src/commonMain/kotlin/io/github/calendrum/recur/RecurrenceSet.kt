package io.github.calendrum.recur

import io.github.calendrum.ICalDsl
import io.github.calendrum.ProblemCode
import io.github.calendrum.SourceSpan
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.model.RecurCodec
import io.github.calendrum.model.decodeWith
import io.github.calendrum.model.frozenCopy
import io.github.calendrum.model.get
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.describe
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * All the dates a component repeats on: its `DTSTART`, the dates its rules produce and its `RDATE`s, less its
 * exceptions ([RFC 5545 §3.8.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5)).
 *
 * Get one from a component with `recurrenceSet` or [of], or build one with [builder]. [iterator] and [between] produce
 * the dates lazily, in order, in local time: `DTSTART` first (subject to `StartMismatch`), then the union of the
 * `RRULE`s and `RDATE`s with duplicates dropped, minus the dates of `EXDATE` and `EXRULE`, which always win. An
 * `EXDATE` that is a `DATE` removes every occurrence on that day. Dates of another form than `DTSTART` are compared by
 * their local time (`ICAL-R009`). An `RDATE` period contributes its start. Limits end the sequence, they never throw.
 * Immutable.
 *
 * @property start the `DTSTART`.
 * @property rules the `RRULE`s.
 * @property exceptionRules the `EXRULE`s, which [RFC 5545](https://www.rfc-editor.org/rfc/rfc5545) deprecates but old
 *   data still has.
 * @property dates the single dates of `RDATE`.
 * @property periods the periods of `RDATE`; each contributes its start.
 * @property exceptions the `EXDATE` dates.
 */
public class RecurrenceSet private constructor(
    public val start: ICalDateTime,
    public val rules: List<Recur>,
    public val exceptionRules: List<Recur>,
    public val dates: List<ICalDateTime>,
    public val periods: List<ICalPeriod>,
    public val exceptions: List<ICalDateTime>,
    internal val startSpan: SourceSpan,
    internal val ruleSpans: Array<SourceSpan>?,
    internal val exceptionRuleSpans: Array<SourceSpan>?,
) {
    /**
     * Whether every rule ends (has `COUNT` or `UNTIL`), so that iterating the set terminates without hitting a limit. A
     * set without rules is finite.
     */
    public val isFinite: Boolean
        get() {
            for (rule in rules) {
                if (rule.count == null && rule.until == null) return false
            }
            return true
        }

    /**
     * Returns an iterator over all the dates of the set, in order. It may be endless; see [isFinite].
     *
     * @param options limits, the handling of a `DTSTART` that no rule produces, and the problem listener.
     */
    @JvmOverloads
    public fun iterator(options: RecurrenceOptions = RecurrenceOptions.Default): Iterator<ICalDateTime> =
        SetIterator(this, options, Long.MIN_VALUE, Long.MAX_VALUE)

    /**
     * Returns the dates from [from] up to, excluding, [to], in order; the range is compared by local time.
     *
     * @param from the first moment of the range.
     * @param to the end of the range, excluded.
     * @param options limits, the handling of a `DTSTART` that no rule produces, and the problem listener.
     */
    @JvmOverloads
    public fun between(
        from: ICalDateTime,
        to: ICalDateTime,
        options: RecurrenceOptions = RecurrenceOptions.Default,
    ): Sequence<ICalDateTime> {
        val fromKey = from.localKey()
        val toKey = to.localKey()
        return Sequence { SetIterator(this, options, fromKey, toKey) }
    }

    /**
     * Returns `true` when [other] is a set with equal start, rules, exception rules, dates, periods and exceptions.
     */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is RecurrenceSet && start == other.start && rules == other.rules &&
                exceptionRules == other.exceptionRules && dates == other.dates && periods == other.periods &&
                exceptions == other.exceptions
            )

    /** Returns a hash code over all parts. */
    override fun hashCode(): Int {
        var result = start.hashCode()
        result = result * 31 + rules.hashCode()
        result = result * 31 + exceptionRules.hashCode()
        result = result * 31 + dates.hashCode()
        result = result * 31 + periods.hashCode()
        return result * 31 + exceptions.hashCode()
    }

    /** Returns the number of rules, dates, periods and exceptions; never a date. */
    override fun toString(): String = describe(
        "RecurrenceSet",
        "rules",
        rules.size,
        "exceptionRules",
        exceptionRules.size,
        "dates",
        dates.size,
        "periods",
        periods.size,
        "exceptions",
        exceptions.size,
    )

    /** Collects the parts of a [RecurrenceSet] by hand. Not thread-safe. */
    @ICalDsl
    public class Builder internal constructor(private val start: ICalDateTime) {
        private val rules = ArrayList<Recur>()
        private val dates = ArrayList<ICalDateTime>()
        private val periods = ArrayList<ICalPeriod>()
        private val exceptions = ArrayList<ICalDateTime>()

        /** Adds an `RRULE`. */
        public fun rule(value: Recur): Builder = apply { rules += value }

        /** Adds a single date, as `RDATE` does. */
        public fun date(value: ICalDateTime): Builder = apply { dates += value }

        /** Adds a period, as `RDATE` does; it contributes its start. */
        public fun period(value: ICalPeriod): Builder = apply { periods += value }

        /** Adds an excluded date, as `EXDATE` does. */
        public fun exception(value: ICalDateTime): Builder = apply { exceptions += value }

        /** Creates the set. */
        public fun build(): RecurrenceSet = RecurrenceSet(
            start,
            frozenCopy(rules),
            emptyList(),
            frozenCopy(dates),
            frozenCopy(periods),
            frozenCopy(exceptions),
            NO_SPAN,
            null,
            null,
        )

        /** Returns the name of the builder. */
        override fun toString(): String = "RecurrenceSet.Builder"
    }

    /** Creates sets. */
    public companion object {
        /**
         * Reads the set of a component (`VEVENT`, `VTODO`, `VJOURNAL` or an observance) from its `DTSTART`, `RRULE`,
         * `EXRULE`, `RDATE` and `EXDATE`.
         *
         * Properties that do not decode are left out; their problems are not reported here. Returns `null` when the
         * component has no `DTSTART` or it does not decode.
         *
         * @param component the component to read.
         */
        @JvmStatic
        public fun of(component: ICalComponent): RecurrenceSet? {
            val startProperty = component.property("DTSTART") ?: return null
            val start = component[Properties.DTSTART] ?: return null
            val rules = ArrayList<Recur>()
            val ruleSpans = ArrayList<SourceSpan>()
            val exceptionRules = ArrayList<Recur>()
            val exceptionRuleSpans = ArrayList<SourceSpan>()
            val dates = ArrayList<ICalDateTime>()
            val periods = ArrayList<ICalPeriod>()
            val exceptions = ArrayList<ICalDateTime>()
            val exceptionRuleKey = PropertyKey("EXRULE", RecurCodec())
            for (property in component.propertyNodes) {
                when (property.name) {
                    "RRULE" -> {
                        val decoded = property.decodeWith(Properties.RRULE.codec, DecodeContext.Lenient)
                        if (decoded is Decoded.Value) {
                            rules += decoded.value
                            ruleSpans += property.span ?: NO_SPAN
                        }
                    }

                    "EXRULE" -> {
                        val decoded = property.decodeWith(exceptionRuleKey.codec, DecodeContext.Lenient)
                        if (decoded is Decoded.Value) {
                            exceptionRules += decoded.value
                            exceptionRuleSpans += property.span ?: NO_SPAN
                        }
                    }

                    "RDATE" -> {
                        val decoded = property.decodeWith(Properties.RDATE.codec, DecodeContext.Lenient)
                        if (decoded is Decoded.Value) {
                            dates += decoded.value.dates
                            periods += decoded.value.periods
                        }
                    }

                    "EXDATE" -> {
                        val decoded = property.decodeWith(Properties.EXDATE.codec, DecodeContext.Lenient)
                        if (decoded is Decoded.Value) exceptions += decoded.value.dates
                    }
                }
            }
            return RecurrenceSet(
                start,
                frozenCopy(rules),
                frozenCopy(exceptionRules),
                frozenCopy(dates),
                frozenCopy(periods),
                frozenCopy(exceptions),
                startProperty.span ?: NO_SPAN,
                ruleSpans.toTypedArray(),
                exceptionRuleSpans.toTypedArray(),
            )
        }

        /** Returns a builder for a set that starts at [start]. */
        @JvmStatic
        public fun builder(start: ICalDateTime): Builder = Builder(start)
    }
}

// A linear k-way merge of DTSTART, the rules and the sorted RDATE values by local time, duplicates dropped; EXDATE are
// hash sets of local keys and of days (an EXDATE date excludes the whole day of a date-time set), EXRULE iterators move
// along with the merge.
internal class SetIterator(
    private val set: RecurrenceSet,
    options: RecurrenceOptions,
    from: Long,
    private val to: Long,
) : Iterator<ICalDateTime> {
    private val start = set.start
    private val startKey = start.localKey()
    private val dateStart = start is ICalDateTime.Date
    private val problems = RecurrenceProblems(options.problemListener, set.startSpan)
    private val maxOccurrences = options.limits.maxOccurrences
    private val rules: Array<RuleIterator>
    private val ruleHeads: BooleanArray
    private val exceptionRules: Array<RuleIterator>
    private val exceptionHeads: BooleanArray
    private val dateKeys: LongArray
    private val dateValues: Array<ICalDateTime>
    private var dateIndex = 0
    private var startPending: Boolean
    private val excludedKeys = HashSet<Long>()
    private val excludedDays = HashSet<Int>()
    private var emitted = 0
    private var lookahead: ICalDateTime? = null
    private var done = false

    init {
        val ruleList = set.rules
        rules = Array(ruleList.size) {
            val span = set.ruleSpans?.getOrNull(it) ?: set.startSpan
            RuleIterator(
                ruleList[it].value,
                start,
                options,
                RecurrenceProblems(options.problemListener, span),
                from,
                to,
            )
        }
        ruleHeads = BooleanArray(rules.size) { rules[it].advance() }
        val exclusionOptions = RecurrenceOptions.create(
            options.limits,
            StartMismatch.Exclude,
            null,
            options.untilOffset,
            options.localOffset,
        )
        val exceptionList = set.exceptionRules
        exceptionRules = Array(exceptionList.size) {
            val span = set.exceptionRuleSpans?.getOrNull(it) ?: set.startSpan
            RuleIterator(
                exceptionList[it].value,
                start,
                exclusionOptions,
                RecurrenceProblems(null, span),
                from,
                Long.MAX_VALUE,
            )
        }
        exceptionHeads = BooleanArray(exceptionRules.size) { exceptionRules[it].advance() }
        startPending = (options.startMismatch != StartMismatch.Exclude || rules.isEmpty()) &&
            startKey >= from && startKey < to
        val candidates = ArrayList<ICalDateTime>(set.dates.size + set.periods.size)
        for (value in set.dates) candidates += normalized(value)
        for (period in set.periods) candidates += normalized(period.start)
        val keyed = candidates.map { it.localKey() to it }.filter { it.first >= from && it.first < to }
            .sortedBy { it.first }
        dateKeys = LongArray(keyed.size) { keyed[it].first }
        dateValues = Array(keyed.size) { keyed[it].second }
        for (exception in set.exceptions) {
            val value = normalized(exception)
            if (value is ICalDateTime.Date && !dateStart) {
                excludedDays += value.localPoint().day
            } else {
                val point = value.localPoint()
                excludedKeys += if (dateStart) localKey(point.day, 0) else point.key
            }
        }
    }

    private fun normalized(value: ICalDateTime): ICalDateTime {
        val sameForm = when (start) {
            is ICalDateTime.Utc -> value is ICalDateTime.Utc || value is ICalDateTime.Date

            is ICalDateTime.Zoned -> (value is ICalDateTime.Zoned && value.tzid == start.tzid) ||
                value is ICalDateTime.Date

            else -> value !is ICalDateTime.Utc && value !is ICalDateTime.Zoned
        }
        if (!sameForm) problems.report(ProblemCode.FormMismatch)
        return value
    }

    override fun hasNext(): Boolean {
        if (lookahead != null) return true
        if (done) return false
        lookahead = compute()
        if (lookahead == null) done = true
        return lookahead != null
    }

    override fun next(): ICalDateTime {
        if (!hasNext()) throw NoSuchElementException()
        val value = lookahead!!
        lookahead = null
        return value
    }

    private fun compute(): ICalDateTime? {
        while (true) {
            var best = Long.MAX_VALUE
            var found = false
            if (startPending) {
                best = startKey
                found = true
            }
            for (index in rules.indices) {
                if (ruleHeads[index] && (!found || rules[index].key < best)) {
                    best = rules[index].key
                    found = true
                }
            }
            if (dateIndex < dateKeys.size && (!found || dateKeys[dateIndex] < best)) {
                best = dateKeys[dateIndex]
                found = true
            }
            if (!found) return null
            var value: ICalDateTime? = null
            if (startPending && startKey == best) {
                value = start
                startPending = false
            }
            for (index in rules.indices) {
                while (ruleHeads[index] && rules[index].key == best) {
                    if (value == null) value = rules[index].current()
                    ruleHeads[index] = rules[index].advance()
                }
            }
            while (dateIndex < dateKeys.size && dateKeys[dateIndex] == best) {
                if (value == null) value = dateValues[dateIndex]
                dateIndex++
            }
            if (excluded(best)) continue
            if (emitted >= maxOccurrences) {
                problems.report(ProblemCode.RecurrenceLimitReached)
                return null
            }
            emitted++
            return value
        }
    }

    private fun excluded(key: Long): Boolean {
        if (excludedKeys.contains(key)) return true
        if (excludedDays.isNotEmpty() && excludedDays.contains(floorDiv(key, SECONDS_PER_DAY_LONG).toInt())) {
            return true
        }
        for (index in exceptionRules.indices) {
            while (exceptionHeads[index] && exceptionRules[index].key < key) {
                exceptionHeads[index] = exceptionRules[index].advance()
            }
            if (exceptionHeads[index] && exceptionRules[index].key == key) return true
        }
        return false
    }

    override fun toString(): String = "RecurrenceSet.iterator"
}

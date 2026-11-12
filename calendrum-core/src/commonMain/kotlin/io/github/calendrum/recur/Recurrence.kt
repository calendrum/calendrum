@file:JvmName("Recurrence")

package io.github.calendrum.recur

import io.github.calendrum.model.VEvent
import io.github.calendrum.model.VJournal
import io.github.calendrum.model.VTimeZone
import io.github.calendrum.model.VTodo
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Expands this rule lazily into the dates it produces, starting at [start], in local time.
 *
 * The sequence is lazy and can be endless (a rule without `COUNT` or `UNTIL`); take what you need with `take` or
 * `takeWhile`. Each call to `iterator()` starts over. The dates have the form of [start]: dates stay dates, UTC stays
 * UTC and a zoned [start] gives zoned dates with the same `TZID`. No time zone rules are applied: the dates are
 * wall-clock times, and a time that a daylight saving gap skips is a matter for the occurrence layer. Expansion never
 * throws: the limits of [options] end the sequence, and problems (`ICAL-R001` to `ICAL-R009`) go to its problem
 * listener, each at most once per expansion.
 *
 * @param start the `DTSTART` of the series, which is also the first date when the rule produces it.
 * @param options limits, the handling of a `DTSTART` that the rule does not produce, and the problem listener.
 */
@JvmOverloads
public fun Recur.expand(
    start: ICalDateTime,
    options: RecurrenceOptions = RecurrenceOptions.Default,
): Sequence<ICalDateTime> = expand(start, null, null, options)

/**
 * Expands this rule lazily like the shorter form, but only the dates in the range from [from] up to, excluding, [to].
 *
 * The skipped dates before [from] still count for `COUNT`. A `null` bound leaves that side open.
 *
 * @param start the `DTSTART` of the series.
 * @param from the first date to produce, or `null` for no lower bound.
 * @param to the end of the range, excluded, or `null` for no upper bound.
 * @param options limits, the handling of a `DTSTART` that the rule does not produce, and the problem listener.
 */
public fun Recur.expand(
    start: ICalDateTime,
    from: ICalDateTime?,
    to: ICalDateTime?,
    options: RecurrenceOptions,
): Sequence<ICalDateTime> {
    val rule = value
    val fromKey = from?.localKey() ?: Long.MIN_VALUE
    val toKey = to?.localKey() ?: Long.MAX_VALUE
    return Sequence {
        RuleIterator(rule, start, options, RecurrenceProblems(options.problemListener, NO_SPAN), fromKey, toKey)
    }
}

/**
 * The recurrence set of this event: `DTSTART` with its `RRULE`, `EXRULE`, `RDATE` and `EXDATE`, or `null` when the
 * event has no usable `DTSTART`.
 */
public val VEvent.recurrenceSet: RecurrenceSet? get() = RecurrenceSet.of(component)

/**
 * The recurrence set of this to-do: `DTSTART` with its `RRULE`, `EXRULE`, `RDATE` and `EXDATE`, or `null` when the
 * to-do has no usable `DTSTART`.
 */
public val VTodo.recurrenceSet: RecurrenceSet? get() = RecurrenceSet.of(component)

/**
 * The recurrence set of this journal entry: `DTSTART` with its `RRULE`, `EXRULE`, `RDATE` and `EXDATE`, or `null` when
 * it has no usable `DTSTART`.
 */
public val VJournal.recurrenceSet: RecurrenceSet? get() = RecurrenceSet.of(component)

/**
 * The recurrence set of this time zone observance (`STANDARD` or `DAYLIGHT`): its `DTSTART` with `RRULE` and `RDATE`,
 * or `null` without a usable `DTSTART`.
 */
public val VTimeZone.Observance.recurrenceSet: RecurrenceSet? get() = RecurrenceSet.of(component)

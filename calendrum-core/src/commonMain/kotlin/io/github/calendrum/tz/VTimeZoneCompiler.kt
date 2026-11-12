package io.github.calendrum.tz

import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.model.OffsetSecondsCodec
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.model.VTimeZone
import io.github.calendrum.model.get
import io.github.calendrum.recur.LocalOffset
import io.github.calendrum.recur.RecurrenceLimits
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.RecurrenceSet
import io.github.calendrum.recur.SECONDS_PER_DAY_LONG
import io.github.calendrum.recur.StartMismatch
import io.github.calendrum.recur.dayOf
import io.github.calendrum.recur.localKey
import io.github.calendrum.value.ICalDateTime

internal class CompiledObservance(val from: Int, val to: Int, val set: RecurrenceSet, val options: RecurrenceOptions)

// Every observance is a recurrence set (DTSTART, RRULE, RDATE) in local time; its UNTIL in UTC is compared with the
// observance's own TZOFFSETFROM, and every onset becomes an instant with that offset. Before the first onset the zone
// has the TZOFFSETFROM of the earliest observance. An observance of 1601 with a rule (Outlook, Exchange) starts its
// rules that year: its DTSTART is not an onset. All observances together give at most MAX_TRANSITIONS onsets up
// to the horizon: thousands of endless observances (each up to its own limit) would otherwise fill the memory, so such
// a zone is not compiled (ICAL-Z003) and the next resolvers look for its TZID.
internal fun compileVTimeZone(zone: VTimeZone, context: ResolveContext): TransitionRules? {
    val tzid = zone.tzid
    val observances = ArrayList<CompiledObservance>()
    for (observance in zone.observances) {
        val to = observance.component[OFFSET_TO] ?: continue
        val from = observance.component[OFFSET_FROM] ?: to
        val set = RecurrenceSet.of(observance.component) ?: continue
        val windowsEpoch = set.rules.isNotEmpty() && startYear(set.start) <= WINDOWS_EPOCH_YEAR
        val options = RecurrenceOptions.create(
            OBSERVANCE_LIMITS,
            if (windowsEpoch) StartMismatch.Exclude else StartMismatch.Rfc,
            observanceListener(context, windowsEpoch),
            null,
            LocalOffset { from },
        )
        observances += CompiledObservance(from, to, set, options)
    }
    if (tzid == null || observances.isEmpty()) {
        context.report(ProblemCode.InvalidVTimeZone, zone.component.span)
        return null
    }
    var horizon = INITIAL_HORIZON
    for (observance in observances) {
        val year = startYear(observance.set.start)
        if (year + 1 > horizon) horizon = year + 1
    }
    val onsets = ArrayList<LongArray>()
    for (observance in observances) {
        if (!collect(observance, Long.MIN_VALUE, yearStart(horizon + 1), onsets)) {
            context.report(ProblemCode.InvalidVTimeZone, zone.component.span)
            return null
        }
    }
    onsets.sortBy { it[0] }
    val initial = if (onsets.isEmpty()) observances[0].from else earliestFrom(observances, onsets[0][0])
    val times = LongArray(onsets.size)
    val offsets = IntArray(onsets.size)
    var size = 0
    var previous = initial
    for (onset in onsets) {
        val offset = onset[1].toInt()
        if (offset == previous) continue
        if (size > 0 && onset[0] == times[size - 1]) {
            offsets[size - 1] = offset
            previous = offset
            continue
        }
        times[size] = onset[0]
        offsets[size] = offset
        previous = offset
        size++
    }
    val open = observances.filter { !it.set.isFinite }
    val source = if (open.isEmpty()) {
        null
    } else {
        TransitionSource { fromYear, toYear, _, sink ->
            val extended = ArrayList<LongArray>()
            for (observance in open) {
                if (!collect(observance, yearStart(fromYear), yearStart(toYear + 1), extended)) break
            }
            extended.sortBy { it[0] }
            for (onset in extended) sink.add(onset[0], onset[1].toInt())
        }
    }
    return TransitionRules(initial, times, offsets, size, if (source == null) MAX_YEAR else horizon, source)
}

// Adds the onsets in [fromLocal, toLocal) to [out]; `false` when [out] would grow beyond MAX_TRANSITIONS.
private fun collect(
    observance: CompiledObservance,
    fromLocal: Long,
    toLocal: Long,
    out: MutableList<LongArray>,
): Boolean {
    val set = observance.set
    val values = if (fromLocal == Long.MIN_VALUE) {
        Sequence { set.iterator(observance.options) }.takeWhile { it.localKey() < toLocal }
    } else {
        set.between(local(fromLocal), local(toLocal), observance.options)
    }
    for (value in values) {
        if (out.size >= MAX_TRANSITIONS) return false
        val epoch = if (value is ICalDateTime.Utc) value.instant.epochSeconds else value.localKey() - observance.from
        out += longArrayOf(epoch, observance.to.toLong())
    }
    return true
}

private fun earliestFrom(observances: List<CompiledObservance>, epoch: Long): Int {
    for (observance in observances) {
        val first = Sequence { observance.set.iterator(observance.options) }.firstOrNull() ?: continue
        val at = if (first is ICalDateTime.Utc) first.instant.epochSeconds else first.localKey() - observance.from
        if (at == epoch) return observance.from
    }
    return observances[0].from
}

private fun startYear(start: ICalDateTime): Int = when (start) {
    is ICalDateTime.Date -> start.year
    is ICalDateTime.Floating -> start.year
    is ICalDateTime.Zoned -> start.year
    is ICalDateTime.Utc -> yearOf(start.instant.epochSeconds)
}

private fun yearStart(year: Int): Long = dayOf(year, 1, 1) * SECONDS_PER_DAY_LONG

private fun local(key: Long): ICalDateTime {
    val day = io.github.calendrum.recur.floorDiv(key, SECONDS_PER_DAY_LONG).toInt()
    val second = (key - day * SECONDS_PER_DAY_LONG).toInt()
    val civil = io.github.calendrum.recur.civilOf(day)
    return ICalDateTime.Floating(civil shr 9, (civil shr 5) and 15, civil and 31, second)
}

private fun observanceListener(context: ResolveContext, windowsEpoch: Boolean): ProblemListener? {
    val listener = context.problemListener ?: return null
    if (!windowsEpoch) return listener
    return ProblemListener { if (it.code != ProblemCode.StartNotInRule) listener.onProblem(it) }
}

internal fun ResolveContext.report(code: ProblemCode, span: io.github.calendrum.SourceSpan?) {
    val listener = problemListener ?: return
    listener.onProblem(io.github.calendrum.Problem(code, span ?: io.github.calendrum.recur.NO_SPAN, null))
}

private val OFFSET_FROM: PropertyKey<Int> get() = PropertyKey("TZOFFSETFROM", OffsetSecondsCodec())

private val OFFSET_TO: PropertyKey<Int> get() = PropertyKey("TZOFFSETTO", OffsetSecondsCodec())

private val OBSERVANCE_LIMITS: RecurrenceLimits
    get() = RecurrenceLimits.builder().maxOccurrences(200_000).maxIterationsWithoutMatch(2_000).build()

private const val INITIAL_HORIZON = 2040
internal const val MAX_TRANSITIONS = 200_000
private const val WINDOWS_EPOCH_YEAR = 1601
private const val MAX_YEAR = 9999

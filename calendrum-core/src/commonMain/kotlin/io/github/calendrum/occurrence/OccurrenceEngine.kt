package io.github.calendrum.occurrence

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.SortedStringMap
import io.github.calendrum.SourceSpan
import io.github.calendrum.groupsOf
import io.github.calendrum.model.EventStatus
import io.github.calendrum.model.JournalStatus
import io.github.calendrum.model.Properties
import io.github.calendrum.model.RecurrenceRange
import io.github.calendrum.model.TodoStatus
import io.github.calendrum.model.VAlarm
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.get
import io.github.calendrum.recur.LocalOffset
import io.github.calendrum.recur.NO_SPAN
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.RecurrenceSet
import io.github.calendrum.recur.dayOf
import io.github.calendrum.recur.localKey
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.tz.FileZones
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.ZoneRules
import io.github.calendrum.tz.instantOfLocal
import io.github.calendrum.tz.localSecondOf
import io.github.calendrum.tz.report
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.time.Instant

internal class OccurrenceEngine(calendar: VCalendar?, val options: OccurrenceOptions) {
    private val file: FileZones? = if (options.zoneSource == null && calendar != null) FileZones(calendar) else null
    val context: ResolveContext = ResolveContext(calendar, options.problemListener)

    // A TZID comes from the input, so the cache is a tree and not a hash table (InputKeys.kt). An occurrence asks for its zone
    // again and again, mostly the one it asked for last, so that one is kept apart: no lookup in the tree, no comparison of
    // strings when it is the same object.
    private val zoneCache = SortedStringMap<CachedZone>()
    private var lastTzid: String? = null
    private var lastZone: ZoneRules? = null

    private class CachedZone(val rules: ZoneRules?)

    // For a series without a zone of its own, which needs no offsets: one object for the whole engine and not one for each
    // series, as this is the path of every expansion. It is here and not lower, because `group` reads it.
    val recurrenceWithoutZone: RecurrenceOptions = options.recurrence.let { recurrence ->
        if (recurrence.problemListener != null || options.problemListener == null) {
            recurrence
        } else {
            RecurrenceOptions.create(
                recurrence.limits,
                recurrence.startMismatch,
                options.problemListener,
                recurrence.untilOffset,
                recurrence.localOffset,
            )
        }
    }
    val series: List<Series> = if (calendar == null) emptyList() else group(calendar)
    val maxDuration: Long = series.maxOfOrNull { it.maxDuration } ?: 0L

    fun stream(seek: Instant?): Iterator<Occurrence> = MergedIterator(series.map { SeriesIterator(it, seek) })

    fun zone(tzid: String): ZoneRules? {
        val last = lastTzid
        if (last != null && (tzid === last || tzid == last)) return lastZone
        val cached = zoneCache[tzid]
        val rules = if (cached != null) cached.rules else resolve(tzid).also { zoneCache[tzid] = CachedZone(it) }
        lastTzid = tzid
        lastZone = rules
        return rules
    }

    private fun resolve(tzid: String): ZoneRules? {
        val zones = options.zoneSource ?: return if (file != null) file.resolve(tzid, context) else unknownZone()
        return zones.resolve(tzid, context)
    }

    private fun unknownZone(): ZoneRules? {
        context.report(ProblemCode.UnknownTimeZone, null)
        return null
    }

    fun instantOf(value: ICalDateTime): Instant? = when (value) {
        is ICalDateTime.Utc -> value.instant
        is ICalDateTime.Zoned -> instantOfLocal(localSecondOf(value), value.nanosecond, zone(value.tzid))
        is ICalDateTime.Floating -> instantOfLocal(localSecondOf(value), value.nanosecond, null)
        is ICalDateTime.Date -> instantOfLocal(localSecondOf(value), 0, null)
    }

    fun instantOfLocal(localSecond: Long, nanosecond: Int, zone: ZoneRules?): Instant? =
        (zone ?: options.floatingRules)?.instantOfLocal(localSecond, nanosecond)

    fun localOf(instant: Instant, frame: ICalDateTime): Long? {
        val epoch = instant.epochSeconds
        val offset = when (frame) {
            is ICalDateTime.Utc -> 0
            is ICalDateTime.Zoned -> (zone(frame.tzid) ?: options.floatingRules)?.offsetSeconds(epoch)
            else -> options.floatingRules?.offsetSeconds(epoch)
        } ?: return null
        return epoch + offset
    }

    fun report(code: ProblemCode, span: SourceSpan?) {
        options.problemListener?.onProblem(Problem(code, span ?: NO_SPAN, null))
    }

    // Components of one UID form a series; the UIDs are grouped by sorting, not hashing (see InputKeys.kt).
    private fun group(calendar: VCalendar): List<Series> {
        val keyed = ArrayList<ICalComponent>()
        val keys = ArrayList<String>()
        val result = ArrayList<Series>()
        for (component in calendar.component.componentNodes) {
            val name = component.name
            if (name != "VEVENT" && name != "VTODO" && name != "VJOURNAL") continue
            val uid = component[Properties.UID]
            if (uid == null) {
                result +=
                    Series(
                        this,
                        if (component.property("RECURRENCE-ID") ==
                            null
                        ) {
                            component
                        } else {
                            null
                        },
                        listOf(component),
                    )
                continue
            }
            keyed += component
            keys += component.name + "\u0000" + uid
        }
        for (positions in groupsOf(keys.size) { keys[it] }) {
            val components = positions.map { keyed[it] }
            var master: ICalComponent? = null
            val extras = ArrayList<ICalComponent>()
            val overrides = ArrayList<ICalComponent>()
            for (component in components) {
                when {
                    component.property("RECURRENCE-ID") != null -> overrides += component
                    master == null -> master = component
                    else -> extras += component
                }
            }
            result += Series(this, master, overrides)
            for (extra in extras) result += Series(this, extra, emptyList())
        }
        return result
    }

    override fun toString(): String = "OccurrenceEngine(${series.size} series)"
}

internal class Timing(
    val startLocal: ICalDateTime?,
    val dueOnly: Boolean,
    val exact: Long,
    val days: Int,
    val nominal: ICalDuration?,
    val overlap: Int,
)

// DTEND and DUE give the exact duration of the master, DURATION adds its days and weeks in local time and the rest
// exactly, a DATE start without an end lasts one day, a DATE-TIME start without an end is a point (RFC 5545 §3.8.5.3,
// RFC 4791 §9.9).
internal fun timingOf(component: ICalComponent, engine: OccurrenceEngine): Timing {
    val start = component[Properties.DTSTART]
    return when (component.name) {
        "VTODO" -> {
            val due = component[Properties.DUE]
            val duration = component[Properties.DURATION]
            when {
                start == null && due != null -> Timing(due, true, 0, 0, null, OVERLAP_DUE_ONLY)
                start != null && due != null -> exactTiming(start, due, engine, OVERLAP_TODO_DUE)
                start != null && duration != null -> Timing(start, false, -1, 0, duration, OVERLAP_TODO_DURATION)
                else -> Timing(start, false, 0, 0, null, OVERLAP_POINT)
            }
        }

        "VJOURNAL" -> if (start is ICalDateTime.Date) {
            Timing(start, false, -1, 1, null, OVERLAP_SPAN)
        } else {
            Timing(start, false, 0, 0, null, OVERLAP_POINT)
        }

        else -> {
            val end = component[Properties.DTEND]
            val duration = component[Properties.DURATION]
            when {
                start != null && end != null -> exactTiming(start, end, engine, OVERLAP_SPAN)

                start != null && duration != null -> {
                    val zero = duration.weeks == 0 && duration.days == 0 && duration.hours == 0 &&
                        duration.minutes == 0 && duration.seconds == 0
                    Timing(start, false, -1, 0, duration, if (zero) OVERLAP_POINT else OVERLAP_SPAN)
                }

                start is ICalDateTime.Date -> Timing(start, false, -1, 1, null, OVERLAP_SPAN)

                else -> Timing(start, false, 0, 0, null, OVERLAP_POINT)
            }
        }
    }
}

private fun exactTiming(start: ICalDateTime, end: ICalDateTime, engine: OccurrenceEngine, overlap: Int): Timing {
    if (start is ICalDateTime.Date && end is ICalDateTime.Date) {
        val days = dayOf(end.year, end.month, end.day) - dayOf(start.year, start.month, start.day)
        return Timing(start, false, -1, maxOf(days, 0), null, overlap)
    }
    val from = engine.instantOf(start)
    val to = engine.instantOf(end)
    val delta = if (from == null || to == null) 0L else maxOf(0L, to.epochSeconds - from.epochSeconds)
    return Timing(start, false, delta, 0, null, if (delta == 0L && overlap == OVERLAP_SPAN) OVERLAP_POINT else overlap)
}

internal fun endOf(timing: Timing, start: Instant, local: ICalDateTime, engine: OccurrenceEngine): Instant {
    if (timing.dueOnly) return start
    if (timing.exact >= 0) return Instant.fromEpochSeconds(start.epochSeconds + timing.exact, start.nanosecondsOfSecond)
    val days = timing.days + (timing.nominal?.let { it.weeks * 7 + it.days } ?: 0)
    val seconds = timing.nominal?.let { it.hours * 3600L + it.minutes * 60L + it.seconds } ?: 0L
    val sign = if (timing.nominal?.isNegative == true) -1 else 1
    if (sign < 0) return start
    val dayEnd = if (days == 0) {
        start
    } else {
        val moved = localSecondOf(local) + days * 86_400L
        when (local) {
            is ICalDateTime.Utc -> null
            is ICalDateTime.Zoned -> engine.instantOfLocal(moved, local.nanosecond, engine.zone(local.tzid))
            is ICalDateTime.Floating -> engine.instantOfLocal(moved, local.nanosecond, null)
            is ICalDateTime.Date -> engine.instantOfLocal(moved, 0, null)
        } ?: Instant.fromEpochSeconds(start.epochSeconds + days * 86_400L, start.nanosecondsOfSecond)
    }
    return Instant.fromEpochSeconds(dayEnd.epochSeconds + seconds, dayEnd.nanosecondsOfSecond)
}

internal fun isCancelled(component: ICalComponent): Boolean = when (component.name) {
    "VTODO" -> component[Properties.TODO_STATUS] == TodoStatus.Cancelled
    "VJOURNAL" -> component[Properties.JOURNAL_STATUS] == JournalStatus.Cancelled
    else -> component[Properties.EVENT_STATUS] == EventStatus.Cancelled
}

internal class Override(
    val component: ICalComponent,
    val key: Long,
    val recurrenceId: ICalDateTime,
    val future: Boolean,
    val timing: Timing,
    val shift: Long,
)

internal class Series(val engine: OccurrenceEngine, val master: ICalComponent?, overrides: List<ICalComponent>) {
    val masterTiming: Timing? = master?.let { timingOf(it, engine) }
    val set: RecurrenceSet? = master?.let { RecurrenceSet.of(it) }
    val exact = HashMap<Long, Override>()
    val future = ArrayList<Override>()
    val singles = ArrayList<Occurrence>()
    val maxShift: Long
    val maxDuration: Long
    val periods = HashMap<Long, io.github.calendrum.value.ICalPeriod>()
    val recurrenceOptions: RecurrenceOptions = recurrenceOptionsFor(set?.start, engine)
    private val alarms = AlarmSource { alarmsOf(it, engine) }

    init {
        var shift = 0L
        var duration = 0L
        val parsed = ArrayList<Override>()
        for (component in overrides) {
            val recurrence = component[Properties.RECURRENCE_ID] ?: continue
            val key = engine.instantOf(recurrence.value)?.epochSeconds
            val timing = timingOf(component, engine)
            if (key == null) {
                engine.report(ProblemCode.FloatingWithoutZone, component.span)
                continue
            }
            val ownStart = timing.startLocal?.let { engine.instantOf(it) }?.epochSeconds ?: key
            val isFuture = engine.options.thisAndFuture && recurrence.range == RecurrenceRange.ThisAndFuture
            val override = Override(component, key, recurrence.value, isFuture, timing, ownStart - key)
            parsed += override
            shift = maxOf(shift, kotlin.math.abs(ownStart - key))
        }
        val matched = if (master == null) BooleanArray(0) else matched(parsed)
        for ((position, override) in parsed.withIndex()) {
            if (master == null || set == null) {
                single(override.component, override.timing, override.recurrenceId)?.let { singles += it }
                continue
            }
            exact[override.key] = override
            if (override.future) {
                future += override
                engine.report(ProblemCode.ThisAndFutureApplied, override.component.span)
            }
            if (!matched[position]) engine.report(ProblemCode.OverrideWithoutInstance, override.component.span)
            single(override.component, override.timing, override.recurrenceId)?.let { singles += it }
        }
        future.sortBy { it.key }
        if (master != null && set == null) {
            if (masterTiming?.startLocal == null) {
                engine.report(ProblemCode.OccurrenceWithoutStart, master.span)
            } else {
                single(master, masterTiming, null)?.let { singles += it }
            }
        }
        val currentSet = set
        if (currentSet != null) {
            for (period in currentSet.periods) {
                engine.instantOf(period.start)?.let { periods[it.epochSeconds] = period }
            }
        }
        for (occurrence in singles) {
            duration =
                maxOf(duration, occurrence.end.epochSeconds - occurrence.start.epochSeconds)
        }
        masterTiming?.let { timing ->
            duration = maxOf(duration, timing.exact, (timing.days + 1) * 86_400L)
            timing.nominal?.let {
                duration =
                    maxOf(
                        duration,
                        (it.weeks * 7L + it.days + 1) * 86_400L + it.hours * 3600L + it.minutes * 60L + it.seconds,
                    )
            }
        }
        for (period in periods.values) duration = maxOf(duration, periodLength(period))
        for (override in parsed) {
            duration = maxOf(duration, override.timing.exact, (override.timing.days + 1) * 86_400L)
        }
        singles.sortBy { it.start }
        maxShift = shift
        maxDuration = duration
    }

    private fun periodLength(period: io.github.calendrum.value.ICalPeriod): Long {
        val start = engine.instantOf(period.start) ?: return 0L
        val end = period.end?.let { engine.instantOf(it) }
        if (end != null) return maxOf(0L, end.epochSeconds - start.epochSeconds)
        val duration = period.duration ?: return 0L
        return (duration.weeks * 7L + duration.days + 1) * 86_400L + duration.hours * 3600L + duration.minutes * 60L +
            duration.seconds
    }

    // Which overrides fall on an instance of the master: one walk of the set over the span of the overrides, in local
    // order, instead of an expansion per override, which rebuilt the RDATE and EXDATE tables each time and walked a
    // COUNT rule from DTSTART (quadratic in overrides). If the walk reaches the occurrence limit, the overrides after
    // it get a window each, as before: a rule without COUNT jumps there.
    private fun matched(overrides: List<Override>): BooleanArray {
        val result = BooleanArray(overrides.size)
        val currentSet = set ?: return result
        val locals = LongArray(overrides.size)
        val known = ArrayList<Int>(overrides.size)
        for ((position, override) in overrides.withIndex()) {
            val local = engine.localOf(Instant.fromEpochSeconds(override.key), currentSet.start) ?: continue
            locals[position] = local
            known += position
        }
        if (known.isEmpty()) return result
        val order = known.sortedBy { locals[it] }
        var limited = false
        val options = RecurrenceOptions.create(
            recurrenceOptions.limits,
            recurrenceOptions.startMismatch,
            ProblemListener { if (it.code == ProblemCode.RecurrenceLimitReached) limited = true },
            recurrenceOptions.untilOffset,
            recurrenceOptions.localOffset,
        )
        val from = floating(locals[order.first()] - 1)
        val to = floating(locals[order.last()] + 2)
        val instances = currentSet.between(from, to, options).iterator()
        var cursor = 0
        while (cursor < order.size && instances.hasNext()) {
            val value = instances.next()
            val key = value.localKey()
            while (cursor < order.size && locals[order[cursor]] + 2 <= key) cursor++
            var probe = cursor
            var instant = Long.MIN_VALUE
            while (probe < order.size && locals[order[probe]] - 1 <= key) {
                if (instant == Long.MIN_VALUE) instant = engine.instantOf(value)?.epochSeconds ?: Long.MAX_VALUE
                val position = order[probe]
                if (instant == overrides[position].key) result[position] = true
                probe++
            }
        }
        if (limited) {
            for (rank in cursor until order.size) {
                val position = order[rank]
                if (!result[position]) result[position] = exists(overrides[position])
            }
        }
        return result
    }

    private fun exists(override: Override): Boolean {
        val currentSet = set ?: return false
        val local = engine.localOf(Instant.fromEpochSeconds(override.key), currentSet.start) ?: return false
        val from = floating(local - 1)
        val to = floating(local + 2)
        return currentSet.between(from, to, recurrenceOptions).any {
            engine.instantOf(it)?.epochSeconds == override.key
        }
    }

    fun single(component: ICalComponent, timing: Timing, recurrenceId: ICalDateTime?): Occurrence? {
        val local = timing.startLocal ?: run {
            engine.report(ProblemCode.OccurrenceWithoutStart, component.span)
            return null
        }
        val start = engine.instantOf(local) ?: run {
            engine.report(ProblemCode.FloatingWithoutZone, component.span)
            return null
        }
        val cancelled = isCancelled(component)
        if (cancelled && !engine.options.includeCancelled) return null
        return Occurrence(
            start,
            endOf(timing, start, local, engine),
            local,
            recurrenceId,
            component,
            cancelled,
            timing.overlap,
            alarms,
        )
    }

    fun instance(value: ICalDateTime): Occurrence? {
        val master = master ?: return null
        val timing = masterTiming ?: return null
        val start = engine.instantOf(value) ?: return null
        val key = start.epochSeconds
        if (exact.containsKey(key)) return null
        val range = rangeFor(key)
        val component = range?.component ?: master
        val cancelled = isCancelled(component)
        if (cancelled && !engine.options.includeCancelled) return null
        if (range != null) {
            val moved = Instant.fromEpochSeconds(key + range.shift, start.nanosecondsOfSecond)
            return Occurrence(
                moved,
                endOf(range.timing, moved, value, engine),
                value,
                value,
                component,
                cancelled,
                range.timing.overlap,
                alarms,
            )
        }
        val period = periods[key]
        val end = if (period != null) periodEnd(period, start, value) else endOf(timing, start, value, engine)
        val recurring = set?.let { it.rules.isNotEmpty() || it.dates.isNotEmpty() || it.periods.isNotEmpty() } == true
        return Occurrence(start, end, value, if (recurring) value else null, master, cancelled, timing.overlap, alarms)
    }

    private fun periodEnd(period: io.github.calendrum.value.ICalPeriod, start: Instant, value: ICalDateTime): Instant {
        period.end?.let { end -> engine.instantOf(end)?.let { return it } }
        val duration = period.duration ?: return start
        return endOf(Timing(value, false, -1, 0, duration, OVERLAP_SPAN), start, value, engine)
    }

    // The last THISANDFUTURE override at or before [key]; `future` is sorted by key.
    private fun rangeFor(key: Long): Override? {
        var low = 0
        var high = future.size - 1
        var found: Override? = null
        while (low <= high) {
            val middle = (low + high) ushr 1
            if (future[middle].key <= key) {
                found = future[middle]
                low = middle + 1
            } else {
                high = middle - 1
            }
        }
        return found
    }

    override fun toString(): String = "Series(${master?.name})"
}

private fun recurrenceOptionsFor(start: ICalDateTime?, engine: OccurrenceEngine): RecurrenceOptions {
    if (start !is ICalDateTime.Zoned) return engine.recurrenceWithoutZone
    val zone = engine.zone(start.tzid) ?: return engine.recurrenceWithoutZone
    val recurrence = engine.options.recurrence
    return RecurrenceOptions.create(
        recurrence.limits,
        recurrence.startMismatch,
        recurrence.problemListener ?: engine.options.problemListener,
        recurrence.untilOffset,
        LocalOffset { zone.offsetForLocal(it) },
    )
}

private fun floating(local: Long): ICalDateTime {
    val day = io.github.calendrum.recur.floorDiv(local, 86_400L)
    val second = (local - day * 86_400L).toInt()
    val civil = io.github.calendrum.recur.civilOf(day.toInt())
    return ICalDateTime.Floating(civil shr 9, (civil shr 5) and 15, civil and 31, second)
}

// A series yields instances in local order; an instant differs from its local key by at most 18 hours plus the largest
// THISANDFUTURE shift, so an occurrence leaves the heap once it is earlier than anything the series can still produce.
internal class SeriesIterator(private val series: Series, seek: Instant?) : Iterator<Occurrence> {
    private val heap = OccurrenceHeap()
    private var base: Iterator<ICalDateTime>? = null
    private var bound = Long.MIN_VALUE
    private val margin = MAX_OFFSET + series.maxShift + 1

    init {
        for (single in series.singles) heap.push(single)
        val set = series.set
        if (set != null && series.master != null) {
            val engine = series.engine
            val lower = seek?.let {
                engine.localOf(Instant.fromEpochSeconds(it.epochSeconds - series.maxDuration - margin), set.start)
            }
            base = if (lower == null) {
                set.iterator(series.recurrenceOptions)
            } else {
                set.between(floating(lower), FAR_FUTURE, series.recurrenceOptions).iterator()
            }
        }
    }

    override fun hasNext(): Boolean {
        fill()
        return heap.size > 0
    }

    override fun next(): Occurrence {
        if (!hasNext()) throw NoSuchElementException()
        return heap.pop()
    }

    private fun fill() {
        while (true) {
            val current = base ?: return
            if (heap.size > 0 && heap.peek().start.epochSeconds < bound) return
            if (!current.hasNext()) {
                base = null
                bound = Long.MAX_VALUE
                return
            }
            val value = current.next()
            val local = value.localKey()
            bound = local - margin
            series.instance(value)?.let { heap.push(it) }
        }
    }

    override fun toString(): String = "SeriesIterator"
}

internal class MergedIterator(sources: List<Iterator<Occurrence>>) : Iterator<Occurrence> {
    private val sources = sources.toTypedArray()
    private val heads = arrayOfNulls<Occurrence>(sources.size)
    private val order = IntArray(sources.size)
    private var size = 0

    init {
        for (index in this.sources.indices) {
            val source = this.sources[index]
            if (source.hasNext()) {
                heads[index] = source.next()
                order[size] = index
                up(size++)
            }
        }
    }

    override fun hasNext(): Boolean = size > 0

    override fun next(): Occurrence {
        if (size == 0) throw NoSuchElementException()
        val top = order[0]
        val value = heads[top]!!
        val source = sources[top]
        if (source.hasNext()) {
            heads[top] = source.next()
        } else {
            heads[top] = null
            size--
            order[0] = order[size]
        }
        if (size > 0) down(0)
        return value
    }

    private fun less(a: Int, b: Int): Boolean {
        val left = heads[order[a]]!!
        val right = heads[order[b]]!!
        return left.start < right.start || (left.start == right.start && left.end < right.end)
    }

    private fun up(position: Int) {
        var index = position
        while (index > 0) {
            val parent = (index - 1) / 2
            if (!less(index, parent)) break
            swap(index, parent)
            index = parent
        }
    }

    private fun down(position: Int) {
        var index = position
        while (true) {
            val left = index * 2 + 1
            val right = left + 1
            var smallest = index
            if (left < size && less(left, smallest)) smallest = left
            if (right < size && less(right, smallest)) smallest = right
            if (smallest == index) return
            swap(index, smallest)
            index = smallest
        }
    }

    private fun swap(a: Int, b: Int) {
        val value = order[a]
        order[a] = order[b]
        order[b] = value
    }

    override fun toString(): String = "MergedIterator(${sources.size})"
}

internal class OccurrenceHeap {
    private var items = arrayOfNulls<Occurrence>(16)
    var size: Int = 0
        private set

    fun push(value: Occurrence) {
        if (size == items.size) items = items.copyOf(size * 2)
        var index = size++
        items[index] = value
        while (index > 0) {
            val parent = (index - 1) / 2
            if (!less(items[index]!!, items[parent]!!)) break
            val swap = items[index]
            items[index] = items[parent]
            items[parent] = swap
            index = parent
        }
    }

    fun peek(): Occurrence = items[0]!!

    fun pop(): Occurrence {
        val top = items[0]!!
        size--
        items[0] = items[size]
        items[size] = null
        var index = 0
        while (true) {
            val left = index * 2 + 1
            val right = left + 1
            var smallest = index
            if (left < size && less(items[left]!!, items[smallest]!!)) smallest = left
            if (right < size && less(items[right]!!, items[smallest]!!)) smallest = right
            if (smallest == index) break
            val swap = items[index]
            items[index] = items[smallest]
            items[smallest] = swap
            index = smallest
        }
        return top
    }

    private fun less(a: Occurrence, b: Occurrence): Boolean = a.start < b.start ||
        (a.start == b.start && a.end < b.end)

    override fun toString(): String = "OccurrenceHeap($size)"
}

internal fun alarmsOf(occurrence: Occurrence, engine: OccurrenceEngine): List<AlarmTime> {
    val result = ArrayList<AlarmTime>()
    for (node in occurrence.component.componentNodes) {
        if (node.name != "VALARM") continue
        val alarm = VAlarm.of(node) ?: continue
        val trigger = alarm.trigger ?: continue
        val at = when (trigger) {
            is io.github.calendrum.model.Trigger.Absolute -> engine.instantOf(trigger.dateTime) ?: continue

            is io.github.calendrum.model.Trigger.Relative -> {
                val base = if (trigger.related ==
                    io.github.calendrum.model.Related.End
                ) {
                    occurrence.end
                } else {
                    occurrence.start
                }
                Instant.fromEpochSeconds(base.epochSeconds + signedSeconds(trigger.duration), base.nanosecondsOfSecond)
            }
        }
        result += AlarmTime(at, alarm, 0)
        val repeat = alarm.repeat ?: 0
        val step = alarm.duration?.let { signedSeconds(it) } ?: 0L
        if (repeat <= 0 || step <= 0L) continue
        val allowed = minOf(repeat, engine.options.maxAlarmRepetitions)
        if (allowed < repeat) engine.report(ProblemCode.AlarmRepeatLimit, node.span)
        for (index in 1..allowed) {
            result +=
                AlarmTime(Instant.fromEpochSeconds(at.epochSeconds + step * index), alarm, index)
        }
    }
    result.sortBy { it.at }
    return result
}

private fun signedSeconds(duration: ICalDuration): Long {
    val total =
        (duration.weeks * 7L + duration.days) * 86_400L + duration.hours * 3600L + duration.minutes * 60L +
            duration.seconds
    return if (duration.isNegative) -total else total
}

private val FAR_FUTURE: ICalDateTime get() = ICalDateTime.Floating(9999, 12, 31, 86_399)
private const val MAX_OFFSET = 18L * 3600L

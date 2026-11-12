package io.github.calendrum.occurrence

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.FbType
import io.github.calendrum.model.FreeBusy
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.VFreeBusy
import io.github.calendrum.model.get
import io.github.calendrum.syntax.describe
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod
import kotlin.jvm.JvmStatic
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

/**
 * A period during which someone is busy, in absolute time.
 *
 * @property start the start of the interval.
 * @property end the end of the interval, after [start].
 * @property type how busy: `BUSY`, `BUSY-TENTATIVE` or `BUSY-UNAVAILABLE`.
 * @throws IllegalArgumentException if [end] is not after [start].
 */
public class BusyInterval(public val start: Instant, public val end: Instant, public val type: FbType) {
    init {
        require(end > start) { "A busy interval must end after its start" }
    }

    /** Returns `true` when [other] has the same start, end and type. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is BusyInterval && start == other.start && end == other.end && type == other.type)

    /** Returns a hash code over the start, the end and the type. */
    override fun hashCode(): Int = (start.hashCode() * 31 + end.hashCode()) * 31 + type.hashCode()

    /** Returns the interval and its type, such as `BusyInterval(2026-10-01T07:00:00Z..2026-10-01T08:00:00Z BUSY)`. */
    override fun toString(): String = "BusyInterval($start..$end ${type.value})"
}

/**
 * Two occurrences that overlap in time.
 *
 * @property first the occurrence that started first.
 * @property second the occurrence that overlaps it.
 */
public class Conflict internal constructor(public val first: Occurrence, public val second: Occurrence) {
    /** The start of the overlap: the later of the two starts. */
    public val start: Instant get() = maxOf(first.start, second.start)

    /** The end of the overlap: the earlier of the two ends. */
    public val end: Instant get() = minOf(first.end, second.end)

    /** Returns the start and the end of the overlap. */
    override fun toString(): String = describe("Conflict", "start", start, "end", end)
}

/**
 * Computes busy time, conflicts and `VFREEBUSY` from calendars.
 *
 * Only events that block time count: not cancelled, not `TRANSP:TRANSPARENT` and with a duration. Tentative events are
 * `BUSY-TENTATIVE`.
 */
public object FreeBusyCalc {
    /**
     * Returns the busy intervals of [calendars] within the range from [from] up to, excluding, [to], merged and sorted.
     *
     * Events of every calendar are expanded with [options]. The `FREEBUSY` periods of their `VFREEBUSY` components are
     * added. Where intervals of different types overlap, the more binding type wins: busy, then unavailable, then
     * tentative.
     *
     * @param calendars the calendars to combine.
     * @param from the start of the range.
     * @param to the end of the range, excluded.
     * @param options zones, floating zone and the other occurrence options.
     */
    @JvmStatic
    public fun busy(
        calendars: List<VCalendar>,
        from: Instant,
        to: Instant,
        options: OccurrenceOptions,
    ): List<BusyInterval> {
        val raw = ArrayList<BusyInterval>()
        for (calendar in calendars) {
            for (occurrence in blocking(calendar, from, to, options)) {
                val start = maxOf(occurrence.start, from)
                val end = minOf(occurrence.end, to)
                if (end > start) raw += BusyInterval(start, end, typeOf(occurrence))
            }
            for (freeBusy in calendar.freeBusy) {
                for (value in freeBusy.freeBusy) {
                    if (value.type == FbType.Free) continue
                    for (period in value.periods) {
                        val bounds = boundsOf(period) ?: continue
                        val start = maxOf(bounds.first, from)
                        val end = minOf(bounds.second, to)
                        if (end > start) raw += BusyInterval(start, end, value.type)
                    }
                }
            }
        }
        return merge(raw)
    }

    /**
     * Returns the pairs of blocking occurrences of [calendars] that overlap within the range, in the order of the
     * second occurrence's start.
     *
     * The time and the memory are proportional to the number of conflicts: k occurrences that overlap each other give
     * k(k - 1)/2 pairs, so bound the range or the calendars when they come from strangers. The shape of the answer (a
     * limit on the number of pairs, or a lazy sequence) may still change in a minor release.
     *
     * @param calendars the calendars to combine.
     * @param from the start of the range.
     * @param to the end of the range, excluded.
     * @param options zones, floating zone and the other occurrence options.
     */
    @ExperimentalICalApi
    @JvmStatic
    public fun conflicts(
        calendars: List<VCalendar>,
        from: Instant,
        to: Instant,
        options: OccurrenceOptions,
    ): List<Conflict> {
        val occurrences = calendars.flatMap { blocking(it, from, to, options).toList() }.sortedBy { it.start }
        val result = ArrayList<Conflict>()
        val open = ArrayList<Occurrence>()
        for (occurrence in occurrences) {
            open.removeAll { it.end <= occurrence.start }
            for (other in open) result += Conflict(other, occurrence)
            open += occurrence
        }
        return result
    }

    /**
     * Creates a `VFREEBUSY` component for the range from [from] to [to] listing [busy] as `FREEBUSY` periods in UTC,
     * grouped by type.
     *
     * @param busy the busy intervals, merged before use.
     * @param from the start of the range.
     * @param to the end of the range.
     * @param defaults supplies `UID` and `DTSTAMP`.
     */
    @JvmStatic
    public fun toVFreeBusy(busy: List<BusyInterval>, from: Instant, to: Instant, defaults: BuildDefaults): VFreeBusy {
        val builder = VFreeBusy.builder(defaults)
            .start(ICalDateTime.Utc(from))
            .end(ICalDateTime.Utc(to))
        for ((type, intervals) in merge(busy).groupBy { it.type }) {
            val periods = intervals.map { ICalPeriod(ICalDateTime.Utc(it.start), ICalDateTime.Utc(it.end), null) }
            builder.freeBusy(FreeBusy(type, periods))
        }
        return builder.build()
    }

    /** Returns the name of the object. */
    override fun toString(): String = "FreeBusyCalc"
}

// A sequence, so that `busy` does not keep every occurrence while it makes the intervals of a large series.
private fun blocking(
    calendar: VCalendar,
    from: Instant,
    to: Instant,
    options: OccurrenceOptions,
): Sequence<Occurrence> = calendar.occurrences(from, to, options).filter { occurrence ->
    occurrence.component.name == "VEVENT" && !occurrence.isCancelled && occurrence.end > occurrence.start &&
        occurrence.component[Properties.TRANSP]?.value != "TRANSPARENT"
}

private fun typeOf(occurrence: Occurrence): FbType =
    if (occurrence.component[Properties.EVENT_STATUS]?.value == "TENTATIVE") FbType.BusyTentative else FbType.Busy

private fun boundsOf(period: ICalPeriod): Pair<Instant, Instant>? {
    val start = (period.start as? ICalDateTime.Utc)?.instant ?: return null
    val end = (period.end as? ICalDateTime.Utc)?.instant
        ?: period.duration?.let { duration ->
            if (duration.isNegative) return null
            val seconds = (duration.weeks * 7L + duration.days) * 86_400L + duration.hours * 3_600L +
                duration.minutes * 60L + duration.seconds
            start + seconds.seconds
        }
        ?: return null
    return if (end > start) start to end else null
}

private fun rank(type: FbType): Int = when (type) {
    FbType.Busy -> 3
    FbType.BusyUnavailable -> 2
    FbType.BusyTentative -> 1
    else -> 0
}

// A sweep over the boundaries: each segment between two of them takes the strongest type covering it (BUSY over
// BUSY-UNAVAILABLE over BUSY-TENTATIVE), then equal neighbours join. The counters make this O(n log n): a rule can give a
// million occurrences from a line of a file, and a pass over all of them for each boundary would take hours. A rank above
// zero has one type. Rank zero has FREE and the types of extensions, and there the first interval of the input wins, so
// it takes a heap of positions, where an interval that has ended stays until it reaches the top.
internal fun merge(intervals: List<BusyInterval>): List<BusyInterval> {
    if (intervals.isEmpty()) return emptyList()
    val starts = intervals.indices.sortedBy { intervals[it].start }
    val ends = intervals.sortedBy { it.end }
    val covering = IntArray(RANK_COUNT)
    val types = arrayOfNulls<FbType>(RANK_COUNT)
    val unranked = PositionHeap()
    val result = ArrayList<BusyInterval>()
    var nextStart = 0
    var nextEnd = 0
    var from: Instant? = null
    while (nextEnd < ends.size) {
        val point = if (nextStart < starts.size) {
            minOf(intervals[starts[nextStart]].start, ends[nextEnd].end)
        } else {
            ends[nextEnd].end
        }
        from?.let { start ->
            strongest(covering, types, unranked, intervals, start)?.let { result.join(start, point, it) }
        }
        while (nextEnd < ends.size && ends[nextEnd].end == point) {
            covering[rank(ends[nextEnd].type)]--
            nextEnd++
        }
        while (nextStart < starts.size && intervals[starts[nextStart]].start == point) {
            val position = starts[nextStart]
            val type = intervals[position].type
            val rank = rank(type)
            covering[rank]++
            types[rank] = type
            if (rank == 0) unranked.push(position)
            nextStart++
        }
        from = point
    }
    return result
}

private fun strongest(
    covering: IntArray,
    types: Array<FbType?>,
    unranked: PositionHeap,
    intervals: List<BusyInterval>,
    start: Instant,
): FbType? {
    for (rank in RANK_COUNT - 1 downTo 1) {
        if (covering[rank] > 0) return types[rank]
    }
    if (covering[0] == 0) return null
    while (intervals[unranked.first()].end <= start) unranked.removeFirst()
    return intervals[unranked.first()].type
}

private fun MutableList<BusyInterval>.join(start: Instant, end: Instant, type: FbType) {
    val last = lastOrNull()
    if (last != null && last.end == start && last.type == type) {
        this[size - 1] = BusyInterval(last.start, end, type)
    } else {
        this += BusyInterval(start, end, type)
    }
}

private class PositionHeap {
    private var items = IntArray(INITIAL_HEAP_SIZE)
    private var size = 0

    fun first(): Int = items[0]

    fun push(position: Int) {
        if (size == items.size) items = items.copyOf(size * 2)
        var at = size++
        while (at > 0) {
            val parent = (at - 1) / 2
            if (items[parent] <= position) break
            items[at] = items[parent]
            at = parent
        }
        items[at] = position
    }

    fun removeFirst() {
        val moved = items[--size]
        var at = 0
        while (true) {
            var child = at * 2 + 1
            if (child >= size) break
            if (child + 1 < size && items[child + 1] < items[child]) child++
            if (items[child] >= moved) break
            items[at] = items[child]
            at = child
        }
        items[at] = moved
    }
}

private const val RANK_COUNT = 4
private const val INITIAL_HEAP_SIZE = 16

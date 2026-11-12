@file:JvmName("Occurrences")

package io.github.calendrum.occurrence

import io.github.calendrum.model.VCalendar
import kotlinx.datetime.IllegalTimeZoneException
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.time.Instant

/**
 * Returns the occurrences of this calendar's events, to-dos and journal entries that intersect the time range from
 * [from] up to, excluding, [to], in the order of their start.
 *
 * Recurrence, overrides (`RECURRENCE-ID`, including `RANGE=THISANDFUTURE`), cancelled instances, durations and alarms
 * are applied; the rules are those of [RFC 4791 §9.9](https://www.rfc-editor.org/rfc/rfc4791#section-9.9) for time
 * range queries. The sequence is lazy. Time zones come from [options]: by default the `VTIMEZONE` components of this
 * calendar and their aliases. Floating times and dates need `floatingZone` in the options, otherwise their occurrences
 * are skipped (`ICAL-O004`). The system time zone is never used. From Java this overload is `Occurrences.between`.
 *
 * @param from the start of the range.
 * @param to the end of the range, excluded.
 * @param options zones, floating zone, recurrence options, alarms and the problem listener.
 * @throws IllegalArgumentException if [from] is not before [to].
 * @throws kotlinx.datetime.IllegalTimeZoneException when evaluated, if a zone is unknown and the resolver's policy is
 *   `Fail`.
 */
@JvmName("between")
@JvmOverloads
public fun VCalendar.occurrences(
    from: Instant,
    to: Instant,
    options: OccurrenceOptions = OccurrenceOptions.Default,
): Sequence<Occurrence> {
    require(from < to) { "from must be before to" }
    val calendar = this
    return Sequence { WindowIterator(OccurrenceEngine(calendar, options).stream(from), from, to) }
}

// The window [from, to) over the merged stream: stops at the first start after `to`, keeps what intersects (RFC 4791
// §9.9).
internal class WindowIterator(
    private val source: Iterator<Occurrence>,
    private val from: Instant,
    private val to: Instant,
) : Iterator<Occurrence> {
    private var next: Occurrence? = null
    private var done = false

    override fun hasNext(): Boolean {
        if (next != null) return true
        while (!done && source.hasNext()) {
            val candidate = source.next()
            if (candidate.start > to) break
            if (candidate.intersects(from, to)) {
                next = candidate
                return true
            }
        }
        done = true
        return false
    }

    override fun next(): Occurrence {
        if (!hasNext()) throw NoSuchElementException()
        val value = next!!
        next = null
        return value
    }

    override fun toString(): String = "WindowIterator"
}

/**
 * Returns a cursor over the occurrences of this calendar, to ask for the next one, the one after an instant, the ones
 * at an instant or a page. From Java this overload is `Occurrences.cursor`.
 *
 * @param options zones, floating zone, recurrence options, alarms and the problem listener.
 */
@JvmName("cursor")
@JvmOverloads
public fun VCalendar.occurrences(options: OccurrenceOptions = OccurrenceOptions.Default): OccurrenceCursor =
    OccurrenceCursor(OccurrenceEngine(this, options))

/**
 * A random-access view over the occurrences of a calendar, made by `calendar.occurrences(options)`.
 *
 * The cursor computes occurrences on demand and does not hold them. It is not thread-safe.
 */
public class OccurrenceCursor internal constructor(private val engine: OccurrenceEngine) {
    private var sequential: Iterator<Occurrence>? = null

    /**
     * Returns the next occurrence in the order of the start, or `null` when there are no more. Successive calls walk
     * the calendar from its beginning.
     *
     * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
     */
    @Throws(IllegalTimeZoneException::class)
    public fun next(): Occurrence? {
        val iterator = sequential ?: engine.stream(null).also { sequential = it }
        return if (iterator.hasNext()) iterator.next() else null
    }

    /**
     * Returns the first occurrence that starts after [instant], or `null`.
     *
     * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
     */
    @Throws(IllegalTimeZoneException::class)
    public fun after(instant: Instant): Occurrence? {
        val iterator = engine.stream(instant)
        while (iterator.hasNext()) {
            val occurrence = iterator.next()
            if (occurrence.start > instant) return occurrence
        }
        return null
    }

    /**
     * Returns the occurrences that are in progress at [instant]: those that start at or before it and end after it (a
     * point occurrence counts at its start).
     *
     * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
     */
    @Throws(IllegalTimeZoneException::class)
    public fun at(instant: Instant): List<Occurrence> {
        val result = ArrayList<Occurrence>()
        val iterator = engine.stream(instant)
        while (iterator.hasNext()) {
            val occurrence = iterator.next()
            if (occurrence.start > instant) break
            if (occurrence.contains(instant)) result += occurrence
        }
        return result
    }

    /**
     * Returns up to [size] occurrences that start after [after], or from the beginning when it is `null`; occurrences
     * with the same start as the last one are included, so a page never splits them.
     *
     * @param size how many occurrences to return, positive.
     * @param after the instant after which to start, or `null`.
     * @throws IllegalArgumentException if [size] is not positive.
     * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
     */
    @Throws(IllegalTimeZoneException::class)
    public fun page(size: Int, after: Instant?): List<Occurrence> {
        require(size > 0) { "size must be positive" }
        val result = ArrayList<Occurrence>()
        val iterator = engine.stream(after)
        while (iterator.hasNext()) {
            val occurrence = iterator.next()
            if (after != null && occurrence.start <= after) continue
            if (result.size >= size && occurrence.start != result.last().start) break
            result += occurrence
        }
        return result
    }

    /** Returns the name of the class. */
    override fun toString(): String = "OccurrenceCursor"
}

/**
 * A set of occurrences, ordered by start and end, that supports set algebra: union, intersection and difference by
 * overlap.
 *
 * Use it to combine schedules, for example the lessons minus the holidays. Immutable.
 *
 * @property occurrences the occurrences in order.
 */
public class OccurrenceSet private constructor(public val occurrences: List<Occurrence>) {
    /** Returns the occurrences of both sets, without duplicates. */
    public fun union(other: OccurrenceSet): OccurrenceSet {
        val all = LinkedHashSet<Occurrence>()
        all += occurrences
        all += other.occurrences
        return OccurrenceSet(sorted(all))
    }

    /** Returns the occurrences of this set that overlap an occurrence of [other] in time. */
    public fun intersect(other: OccurrenceSet): OccurrenceSet =
        OccurrenceSet(occurrences.filter { occurrence -> other.occurrences.any { overlaps(occurrence, it) } })

    /** Returns the occurrences of this set that overlap no occurrence of [other] in time. */
    public fun minus(other: OccurrenceSet): OccurrenceSet =
        OccurrenceSet(occurrences.filter { occurrence -> other.occurrences.none { overlaps(occurrence, it) } })

    /** Returns `true` when [other] holds equal occurrences in the same order. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is OccurrenceSet && occurrences == other.occurrences)

    /** Returns a hash code over the occurrences. */
    override fun hashCode(): Int = occurrences.hashCode()

    /** Returns the number of occurrences. */
    override fun toString(): String = "OccurrenceSet(${occurrences.size})"

    /** Creates sets. */
    public companion object {
        /** Creates a set of [occurrences], sorted by start and end. */
        @kotlin.jvm.JvmStatic
        public fun of(occurrences: Iterable<Occurrence>): OccurrenceSet = OccurrenceSet(sorted(occurrences.toList()))
    }
}

private fun sorted(values: Collection<Occurrence>): List<Occurrence> =
    values.sortedWith(compareBy<Occurrence>({ it.start }, { it.end }))

private fun overlaps(a: Occurrence, b: Occurrence): Boolean {
    val aPoint = a.end <= a.start
    val bPoint = b.end <= b.start
    return when {
        aPoint && bPoint -> a.start == b.start
        aPoint -> a.start >= b.start && a.start < b.end
        bPoint -> b.start >= a.start && b.start < a.end
        else -> a.start < b.end && b.start < a.end
    }
}

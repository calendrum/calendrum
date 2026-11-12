@file:JvmName("Events")

package io.github.calendrum.occurrence

import io.github.calendrum.model.Attendee
import io.github.calendrum.model.CuType
import io.github.calendrum.model.EventStatus
import io.github.calendrum.model.VEvent
import io.github.calendrum.tz.TimeZoneResolver
import io.github.calendrum.tz.toInstant
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.IllegalTimeZoneException
import kotlinx.datetime.TimeZone
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.time.Instant

/**
 * Returns the instant this event starts, or `null` when it has no start or the start cannot be placed without a zone.
 *
 * @param zones resolves the `TZID` of the start.
 * @param floatingZone the zone in which a floating start or a date is read, or `null` for none.
 * @throws kotlinx.datetime.IllegalTimeZoneException if the zone is unknown and the resolver's policy is `Fail`.
 */
@JvmOverloads
@Throws(IllegalTimeZoneException::class)
public fun VEvent.resolvedStart(zones: TimeZoneResolver, floatingZone: TimeZone? = null): Instant? =
    start?.toInstant(zones, floatingZone)

/**
 * Returns the instant this event ends, from `DTEND` or else from `DTSTART` plus `DURATION`, a day for a date, or the
 * start itself; `null` when the event has no start or it cannot be placed without a zone.
 *
 * @param zones resolves the `TZID` values.
 * @param floatingZone the zone in which floating times and dates are read, or `null` for none.
 * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
 */
@JvmOverloads
@Throws(IllegalTimeZoneException::class)
public fun VEvent.resolvedEnd(zones: TimeZoneResolver, floatingZone: TimeZone? = null): Instant? {
    val start = start ?: return null
    end?.let { return it.toInstant(zones, floatingZone) }
    val begin = start.toInstant(zones, floatingZone) ?: return null
    val options = OccurrenceOptions.builder().zones(zones).floatingZone(floatingZone).build()
    val engine = OccurrenceEngine(null, options)
    return endOf(timingOf(component, engine), begin, start, engine)
}

/** Whether the event has `STATUS:CANCELLED`. */
public val VEvent.isCancelled: Boolean get() = status == EventStatus.Cancelled

/** Whether the event starts on a date and not at a time of day. */
public val VEvent.isAllDay: Boolean get() = start is ICalDateTime.Date

/**
 * Returns the version of the event for deciding which copy is newer: its `SEQUENCE` (0 when absent) and its
 * `LAST-MODIFIED`, or `DTSTAMP` when that is missing, if it is in UTC.
 */
public fun VEvent.revision(): Revision = Revision(
    sequence ?: 0,
    (lastModified ?: stamp)?.let {
        it as? ICalDateTime.Utc
    }?.instant,
)

/**
 * Returns the attendees that are people, as opposed to groups, resources and rooms (`CUTYPE=INDIVIDUAL`, the default).
 */
public fun List<Attendee>.individuals(): List<Attendee> = filter { it.cuType == CuType.Individual }

/**
 * The version of a component by the rules of [RFC 5546 §2.1.5](https://www.rfc-editor.org/rfc/rfc5546#section-2.1.5):
 * the higher `SEQUENCE` wins, and with equal sequences the later timestamp.
 *
 * @property sequence the `SEQUENCE`, 0 when absent.
 * @property modified the `LAST-MODIFIED` or `DTSTAMP` in UTC, or `null` when there is none.
 */
public class Revision(public val sequence: Int, public val modified: Instant?) : Comparable<Revision> {
    /** Compares by [sequence] first, then by [modified]; a missing time is older than any time. */
    override fun compareTo(other: Revision): Int {
        if (sequence != other.sequence) return sequence.compareTo(other.sequence)
        val left = modified
        val right = other.modified
        return when {
            left == null && right == null -> 0
            left == null -> -1
            right == null -> 1
            else -> left.compareTo(right)
        }
    }

    /** Returns `true` when [other] has the same sequence and time. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Revision && sequence == other.sequence && modified == other.modified)

    /** Returns a hash code over the sequence and the time. */
    override fun hashCode(): Int = sequence * 31 + modified.hashCode()

    /** Returns the sequence and the time. */
    override fun toString(): String = "Revision($sequence, $modified)"
}

package io.github.calendrum.tz

import io.github.calendrum.ProblemListener
import io.github.calendrum.model.VCalendar
import kotlinx.datetime.IllegalTimeZoneException
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlin.time.Instant

/**
 * A time zone as the library uses it: an identifier and the rules that map instants to UTC offsets and back.
 *
 * Instances come from a [TimeZoneResolver]; they are not tied to the system time zone, which the library never uses on
 * its own. A zone is immutable and thread-safe.
 *
 * Users implement this interface for a resolver of their own. Its set of members is frozen for 1.0: a later release
 * does not add an abstract member that would break such implementations.
 */
public interface ICalTimeZone {
    /** The identifier the zone was requested with, usually the `TZID` of the file. */
    public val id: String

    /** Returns the UTC offset in force at [instant]. */
    public fun offsetAt(instant: Instant): UtcOffset

    /**
     * Returns the instant of the wall-clock time [local] in this zone.
     *
     * A time that the zone skips (a daylight saving gap) is read with the offset before the gap, so it lands after the
     * gap; an ambiguous time (an overlap) takes the first of its two instants, as [RFC 5545
     * §3.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.5) says.
     */
    public fun toInstant(local: LocalDateTime): Instant

    /** Returns the changes of offset from [from] up to, excluding, [to], in time order. */
    public fun transitions(from: Instant, to: Instant): List<ZoneTransition>
}

/**
 * A change of the UTC offset of a zone, such as the start or the end of daylight saving time.
 *
 * @property at the instant at which the new offset takes effect.
 * @property offsetBefore the offset just before [at].
 * @property offsetAfter the offset from [at] on.
 */
public class ZoneTransition(
    public val at: Instant,
    public val offsetBefore: UtcOffset,
    public val offsetAfter: UtcOffset,
) {
    /** Returns `true` when [other] is a transition at the same instant with the same offsets. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is ZoneTransition && at == other.at && offsetBefore == other.offsetBefore &&
                offsetAfter == other.offsetAfter
            )

    /** Returns a hash code over the instant and the offsets. */
    override fun hashCode(): Int = (at.hashCode() * 31 + offsetBefore.hashCode()) * 31 + offsetAfter.hashCode()

    /** Returns the instant and the offsets, such as `ZoneTransition(2026-03-29T01:00:00Z, +01:00 -> +02:00)`. */
    override fun toString(): String = "ZoneTransition($at, $offsetBefore -> $offsetAfter)"
}

/**
 * Finds the rules of a time zone by its `TZID`.
 *
 * Resolvers are combined in a chain with [TimeZoneResolvers.chain]. Implementations must be thread-safe and must not
 * use the system time zone.
 */
public fun interface TimeZoneResolver {
    /**
     * Returns the zone for [tzid], or `null` when this resolver does not know it, so that the next resolver of a chain
     * can try.
     *
     * @param tzid the value of a `TZID` parameter.
     * @param context the calendar being read and the problem listener.
     * @throws kotlinx.datetime.IllegalTimeZoneException if the identifier is unknown and the policy of the chain is
     *   [UnknownZonePolicy.Fail].
     */
    @Throws(IllegalTimeZoneException::class)
    public fun resolve(tzid: String, context: ResolveContext): ICalTimeZone?
}

/**
 * What a resolver may need besides the identifier: the calendar the `TZID` comes from and where to report problems
 * (`ICAL-Z001` to `ICAL-Z004`).
 *
 * @property calendar the calendar being read, or `null`.
 * @property problemListener receives the problems, or `null`.
 */
public class ResolveContext(public val calendar: VCalendar?, public val problemListener: ProblemListener?) {
    /** Returns the name of the class. */
    override fun toString(): String = "ResolveContext"

    /** Shared contexts. */
    public companion object {
        /** A context without a calendar and a listener. */
        @kotlin.jvm.JvmField
        public val Empty: ResolveContext = ResolveContext(null, null)
    }
}

/** What a chain of resolvers does with a `TZID` that none of them knows (`ICAL-Z001`). */
public enum class UnknownZonePolicy {
    /**
     * Resolves to `null`: the time is treated as floating, which needs a floating zone to become an instant. This is
     * the default of [TimeZoneResolvers.default].
     */
    Floating,

    /** Resolves to UTC. */
    Utc,

    /** Throws `IllegalTimeZoneException`. */
    Fail,
}

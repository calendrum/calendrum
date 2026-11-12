@file:JvmName("TimeZoneConversions")

package io.github.calendrum.tz

import io.github.calendrum.ProblemCode
import io.github.calendrum.SortedStringMap
import io.github.calendrum.model.VCalendar
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.IllegalTimeZoneException
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.time.Instant

/**
 * Ready-made [TimeZoneResolver]s and the chain that combines them.
 *
 * The library never asks the operating system for a time zone on its own: `default` resolves from the file and its
 * aliases only, and the IANA zones of the platform or of the `calendrum-tzdata` module join a chain when you add them. An
 * application that does not ask for them does not pay for the platform zone database.
 */
public object TimeZoneResolvers {
    /**
     * Returns a resolver that compiles the `VTIMEZONE` components of [calendar] and resolves a `TZID` to the rules they
     * describe.
     *
     * A `VTIMEZONE` that cannot be compiled is skipped with `ICAL-Z003`. Compilation happens once per calendar, on
     * first use.
     */
    @JvmStatic
    public fun vtimezone(calendar: VCalendar): TimeZoneResolver = VTimeZoneResolver(FileZones(calendar))

    /**
     * A resolver for IANA zone names such as `Europe/Berlin`, backed by the time zone database of the platform through
     * kotlinx-datetime.
     *
     * It does not know Windows names or aliases; combine it with [aliases]. Results can differ between platforms and
     * database versions; for the same answer everywhere use the resolver of `calendrum-tzdata`.
     */
    @get:JvmStatic
    public val iana: TimeZoneResolver
        get() = IanaResolver

    /**
     * A resolver for the names real-world calendars use instead of IANA identifiers: Windows and Outlook names, IANA
     * links and `/mozilla.org/` prefixes. It resolves them through the [iana] resolver and reports `ICAL-Z002`.
     */
    @get:JvmStatic
    public val aliases: TimeZoneResolver
        get() = AliasResolver(IanaResolver)

    /**
     * Combines [resolvers] into one that asks them in order and applies [unknown] when none of them knows the `TZID`.
     *
     * @param resolvers the resolvers, tried first to last.
     * @param unknown what to do for a `TZID` no resolver knows.
     */
    @JvmStatic
    public fun chain(vararg resolvers: TimeZoneResolver, unknown: UnknownZonePolicy): TimeZoneResolver =
        ChainResolver(resolvers.copyOf(), unknown)

    /**
     * Returns the default resolver: the `VTIMEZONE` components of [calendar], then the aliases of the zones in them,
     * and `null` (floating time) for any other `TZID`. Without a calendar it knows nothing.
     *
     * No IANA zone database is consulted, so the result does not depend on the platform. From Java call it as
     * `defaultFor`.
     */
    @JvmStatic
    @JvmName("defaultFor")
    public fun default(calendar: VCalendar?): TimeZoneResolver = if (calendar == null) {
        ChainResolver(emptyArray(), UnknownZonePolicy.Floating)
    } else {
        val file = VTimeZoneResolver(FileZones(calendar))
        ChainResolver(arrayOf(file, AliasResolver(file)), UnknownZonePolicy.Floating)
    }
}

/**
 * Returns the instant this value means, or `null` when it cannot be known without a time zone.
 *
 * A UTC value is its own instant. A zoned value is resolved by [zones]; if that gives no zone, [floatingZone] is used.
 * A floating value and a date are read in [floatingZone]. With no [floatingZone] they give `null`: the library never
 * assumes the system zone. The result keeps the nanoseconds of the value.
 *
 * @param zones resolves the `TZID` of a zoned value.
 * @param floatingZone the zone in which floating times and dates are read, or `null` for none.
 * @throws kotlinx.datetime.IllegalTimeZoneException if `TZID` is unknown and the resolver's policy is
 *   [UnknownZonePolicy.Fail].
 */
@JvmOverloads
@Throws(IllegalTimeZoneException::class)
public fun ICalDateTime.toInstant(zones: TimeZoneResolver, floatingZone: TimeZone? = null): Instant? =
    toInstant(zones, floatingZone, ResolveContext.Empty)

internal fun ICalDateTime.toInstant(
    zones: TimeZoneResolver,
    floatingZone: TimeZone?,
    context: ResolveContext,
): Instant? {
    val zone = if (this is ICalDateTime.Zoned) zones.resolve(tzid, context) else null
    return when (this) {
        is ICalDateTime.Utc -> instant

        is ICalDateTime.Zoned -> (zone?.rules() ?: floatingZone?.let(::KotlinxRules))
            ?.instantOfLocal(localSecondOf(this), nanosecond)

        is ICalDateTime.Floating -> floatingZone?.let(::KotlinxRules)?.instantOfLocal(localSecondOf(this), nanosecond)

        is ICalDateTime.Date -> floatingZone?.let(::KotlinxRules)?.instantOfLocal(localSecondOf(this), 0)
    }
}

// The default chain without public objects: VTIMEZONE of the file compiled to TransitionRules, aliases resolved against
// the same file, then ICAL-Z001. IANA is an explicit choice.
internal class FileZones(private val calendar: VCalendar) {
    @Volatile
    private var compiled: SortedStringMap<TransitionRules?>? = null

    fun rules(tzid: String, context: ResolveContext): TransitionRules? {
        val zones = compiled ?: compile(context).also { compiled = it }
        return zones[tzid]
    }

    fun resolve(tzid: String, context: ResolveContext): ZoneRules? {
        rules(tzid, context)?.let { return it }
        val alias = TimeZoneAliases.resolve(tzid) ?: return unknown(context)
        val target = rules(alias.ianaId, context) ?: return unknown(context)
        TimeZoneAliases.report(alias, context)
        return target
    }

    private fun unknown(context: ResolveContext): ZoneRules? {
        context.report(ProblemCode.UnknownTimeZone, null)
        return null
    }

    // TZIDs come from the input: sorted, not hashed (see InputKeys.kt).
    private fun compile(context: ResolveContext): SortedStringMap<TransitionRules?> {
        val result = SortedStringMap<TransitionRules?>()
        for (zone in calendar.timeZones) {
            val tzid = zone.tzid ?: continue
            if (result.containsKey(tzid)) continue
            result[tzid] = compileVTimeZone(zone, context)
        }
        return result
    }

    override fun toString(): String = "FileZones"
}

internal class VTimeZoneResolver(private val file: FileZones) : TimeZoneResolver {
    private val zones = SortedStringMap<TransitionZone>()

    override fun resolve(tzid: String, context: ResolveContext): ICalTimeZone? {
        val rules = file.rules(tzid, context) ?: return null
        return zones[tzid] ?: TransitionZone(tzid, rules).also { zones[tzid] = it }
    }

    override fun toString(): String = "TimeZoneResolvers.vtimezone"
}

internal object IanaResolver : TimeZoneResolver {
    override fun resolve(tzid: String, context: ResolveContext): ICalTimeZone? {
        if (tzid.isEmpty() || tzid[0] == '/' || tzid.indexOf(' ') >= 0) return null
        val zone = try {
            TimeZone.of(tzid)
        } catch (_: IllegalTimeZoneException) {
            return null
        } catch (_: IllegalArgumentException) {
            return null
        }
        return PlatformZone(tzid, zone)
    }

    override fun toString(): String = "TimeZoneResolvers.iana"
}

internal class PlatformZone(override val id: String, private val zone: TimeZone) :
    ICalTimeZone,
    ZoneRules {
    override fun offsetAt(instant: Instant): UtcOffset = zone.offsetAt(instant)

    override fun offsetSeconds(epochSecond: Long): Int =
        zone.offsetAt(Instant.fromEpochSeconds(epochSecond)).totalSeconds

    override fun toInstant(local: LocalDateTime): Instant = local.toInstant(zone)

    override fun offsetForLocal(localSecond: Long): Int =
        (localSecond - localDateTimeOf(localSecond).toInstant(zone).epochSeconds).toInt()

    // kotlinx-datetime has no transition list: offsets are sampled every six hours and each change is narrowed down to
    // the second.
    override fun transitions(from: Instant, to: Instant): List<ZoneTransition> {
        val result = ArrayList<ZoneTransition>()
        var start = from.epochSeconds
        val end = to.epochSeconds
        var before = zone.offsetAt(from)
        while (start < end) {
            val next = minOf(end, start + SCAN_STEP)
            val after = zone.offsetAt(Instant.fromEpochSeconds(next))
            if (after != before) {
                var low = start
                var high = next
                while (high - low > 1) {
                    val middle = low + (high - low) / 2
                    if (zone.offsetAt(Instant.fromEpochSeconds(middle)) == before) low = middle else high = middle
                }
                result +=
                    ZoneTransition(
                        Instant.fromEpochSeconds(high),
                        before,
                        zone.offsetAt(Instant.fromEpochSeconds(high)),
                    )
                before = zone.offsetAt(Instant.fromEpochSeconds(high))
                start = high
                continue
            }
            start = next
        }
        return result
    }

    override fun toString(): String = "ICalTimeZone($id)"

    private companion object {
        const val SCAN_STEP = 6L * 3600L
    }
}

internal class AliasResolver(private val target: TimeZoneResolver) : TimeZoneResolver {
    override fun resolve(tzid: String, context: ResolveContext): ICalTimeZone? {
        val alias = TimeZoneAliases.resolve(tzid) ?: return null
        val zone = target.resolve(alias.ianaId, context) ?: return null
        TimeZoneAliases.report(alias, context)
        return AliasZone(tzid, zone)
    }

    override fun toString(): String = "TimeZoneResolvers.aliases"
}

internal class AliasZone(override val id: String, private val target: ICalTimeZone) :
    ICalTimeZone,
    ZoneRules {
    private val rules = target.rules()

    override fun offsetAt(instant: Instant): UtcOffset = target.offsetAt(instant)

    override fun offsetSeconds(epochSecond: Long): Int = rules.offsetSeconds(epochSecond)

    override fun toInstant(local: LocalDateTime): Instant = target.toInstant(local)

    override fun offsetForLocal(localSecond: Long): Int = rules.offsetForLocal(localSecond)

    override fun transitions(from: Instant, to: Instant): List<ZoneTransition> = target.transitions(from, to)

    override fun toString(): String = "ICalTimeZone($id)"
}

internal class ChainResolver(
    private val resolvers: Array<out TimeZoneResolver>,
    private val unknown: UnknownZonePolicy,
) : TimeZoneResolver {
    override fun resolve(tzid: String, context: ResolveContext): ICalTimeZone? {
        for (resolver in resolvers) {
            val zone = resolver.resolve(tzid, context)
            if (zone != null) return zone
        }
        context.report(ProblemCode.UnknownTimeZone, null)
        return when (unknown) {
            UnknownZonePolicy.Floating -> null
            UnknownZonePolicy.Utc -> FixedZone(tzid, 0)
            UnknownZonePolicy.Fail -> throw IllegalTimeZoneException("Unknown time zone")
        }
    }

    override fun toString(): String = "TimeZoneResolvers.chain(${resolvers.size}, $unknown)"
}

internal class FixedZone(override val id: String, private val seconds: Int) :
    ICalTimeZone,
    ZoneRules {
    override fun offsetAt(instant: Instant): UtcOffset = UtcOffset(seconds = seconds)

    override fun offsetSeconds(epochSecond: Long): Int = seconds

    override fun toInstant(local: LocalDateTime): Instant =
        Instant.fromEpochSeconds(localSeconds(local) - seconds, local.nanosecond)

    override fun offsetForLocal(localSecond: Long): Int = seconds

    override fun transitions(from: Instant, to: Instant): List<ZoneTransition> = emptyList()

    override fun toString(): String = "ICalTimeZone($id)"
}

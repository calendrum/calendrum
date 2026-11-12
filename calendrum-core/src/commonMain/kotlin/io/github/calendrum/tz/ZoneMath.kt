package io.github.calendrum.tz

import io.github.calendrum.recur.SECONDS_PER_DAY_LONG
import io.github.calendrum.recur.civilOf
import io.github.calendrum.recur.dayOf
import io.github.calendrum.recur.floorDiv
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlin.time.Instant

// Our zones are recognized by the `ZoneRules` interface, never by their class. A custom ICalTimeZone is read through
// its `offsetAt` and `toInstant`, a kotlinx `TimeZone` through kotlinx-datetime; both wrappers are created only by
// `OccurrenceOptions.Builder.build()` and the public `toInstant`, outside the default path.
internal fun interface ZoneSource {
    fun resolve(tzid: String, context: ResolveContext): ZoneRules?
}

internal class ResolverSource(private val resolver: TimeZoneResolver) : ZoneSource {
    override fun resolve(tzid: String, context: ResolveContext): ZoneRules? = resolver.resolve(tzid, context)?.rules()

    override fun toString(): String = "ResolverSource($resolver)"
}

internal fun ICalTimeZone.rules(): ZoneRules = this as? ZoneRules ?: PublicRules(this)

internal class PublicRules(private val zone: ICalTimeZone) : ZoneRules {
    override fun offsetSeconds(epochSecond: Long): Int =
        zone.offsetAt(Instant.fromEpochSeconds(epochSecond)).totalSeconds

    override fun offsetForLocal(localSecond: Long): Int =
        (localSecond - zone.toInstant(localDateTimeOf(localSecond)).epochSeconds).toInt()

    override fun toString(): String = "PublicRules(${zone.id})"
}

internal class KotlinxRules(private val zone: TimeZone) : ZoneRules {
    override fun offsetSeconds(epochSecond: Long): Int =
        zone.offsetAt(Instant.fromEpochSeconds(epochSecond)).totalSeconds

    override fun offsetForLocal(localSecond: Long): Int =
        (localSecond - localDateTimeOf(localSecond).toInstant(zone).epochSeconds).toInt()

    override fun toString(): String = "KotlinxRules($zone)"
}

internal fun ZoneRules.instantOfLocal(localSecond: Long, nanosecond: Int): Instant =
    Instant.fromEpochSeconds(localSecond - offsetForLocal(localSecond), nanosecond)

internal fun localSecondOf(value: ICalDateTime): Long = when (value) {
    is ICalDateTime.Date -> dayOf(value.year, value.month, value.day) * SECONDS_PER_DAY_LONG
    is ICalDateTime.Floating -> dayOf(value.year, value.month, value.day) * SECONDS_PER_DAY_LONG + value.secondOfDay
    is ICalDateTime.Zoned -> dayOf(value.year, value.month, value.day) * SECONDS_PER_DAY_LONG + value.secondOfDay
    is ICalDateTime.Utc -> value.instant.epochSeconds
}

internal fun localDateTimeOf(localSecond: Long): LocalDateTime {
    val day = floorDiv(localSecond, SECONDS_PER_DAY_LONG)
    val second = (localSecond - day * SECONDS_PER_DAY_LONG).toInt()
    val civil = civilOf(day.toInt())
    return LocalDateTime(civil shr 9, (civil shr 5) and 15, civil and 31, second / 3600, second / 60 % 60, second % 60)
}

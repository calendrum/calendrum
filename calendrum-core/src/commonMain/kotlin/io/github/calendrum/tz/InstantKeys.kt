@file:JvmName("InstantKeys")

package io.github.calendrum.tz

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.EncodeContext
import io.github.calendrum.model.PropertyCodec
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.TimeZone
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.time.Instant

/**
 * Returns a key that reads the same property as an `Instant` instead of an [ICalDateTime], placing it in time with
 * [zones].
 *
 * Use it with schemas and typed reads: `Properties.DTSTART.instant(zones)`. A value that cannot be placed reads as
 * malformed, with `ICAL-Z001` for an unknown `TZID` or `ICAL-O004` for a floating time or a date without a
 * [floatingZone]. Writing the key produces a UTC value.
 *
 * @param zones resolves the `TZID` of zoned values.
 * @param floatingZone the zone in which floating times and dates are read, or `null` for none.
 */
@JvmOverloads
public fun PropertyKey<ICalDateTime>.instant(
    zones: TimeZoneResolver,
    floatingZone: TimeZone? = null,
): PropertyKey<Instant> = PropertyKey(name, InstantCodec(codec, zones, floatingZone))

@OptIn(ExperimentalICalApi::class)
internal class InstantCodec(
    private val base: PropertyCodec<ICalDateTime>,
    private val zones: TimeZoneResolver,
    private val floatingZone: TimeZone?,
) : PropertyCodec<Instant> {
    override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Instant> {
        val decoded = base.decode(property, context)
        if (decoded !is Decoded.Value) {
            @Suppress("UNCHECKED_CAST")
            return decoded as Decoded<Instant>
        }
        val instant = decoded.value.toInstant(zones, floatingZone)
        if (instant != null) return Decoded.Value(instant, decoded.problems)
        val zoned = decoded.value is ICalDateTime.Zoned
        val code = if (zoned) ProblemCode.UnknownTimeZone else ProblemCode.FloatingWithoutZone
        return Decoded.Malformed(context.problem(property, code, 0))
    }

    override fun encode(value: Instant, context: EncodeContext): ICalProperty =
        base.encode(ICalDateTime.Utc(value), context)

    override fun toString(): String = "InstantCodec"
}

@file:JsExport

package io.github.calendrum.sdk.js

import io.github.calendrum.ICal
import io.github.calendrum.jcal.JCal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tzdata.TzData
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

// The whole file is exported; helpers are internal so that they stay out of the TypeScript declarations.
// kotlinx-datetime `TimeZone.of` would need the js-joda time zone database on JS, so zones come from calendrum-tzdata.
class ParseOutput internal constructor(val jcal: String, val problems: Array<ProblemOutput>)

class ProblemOutput internal constructor(
    val code: String,
    val severity: String,
    val message: String,
    val line: Int,
    val column: Int,
)

class OccurrenceOutput internal constructor(
    val uid: String?,
    val summary: String?,
    val start: String,
    val end: String,
    val localStart: String?,
    val localEnd: String?,
)

fun abiVersion(): Int = 1

fun parse(text: String): ParseOutput {
    val result = ICal.parse(text)
    val problems = result.problems.map {
        ProblemOutput(it.code.id, it.severity.name, it.message, it.span.line, it.span.column)
    }
    return ParseOutput(JCal.encodeToString(result.components), problems.toTypedArray())
}

fun occurrences(text: String, from: String, to: String, zone: String?): Array<OccurrenceOutput> {
    val start = Instant.parse(from)
    val end = Instant.parse(to)
    val local = zone?.let(::zoneOf)
    val calendar = ICal.parse(text).calendars.firstOrNull() ?: return emptyArray()
    val zones = TimeZoneResolvers.chain(
        TimeZoneResolvers.vtimezone(calendar),
        TzData.resolver,
        unknown = UnknownZonePolicy.Utc,
    )
    val options = OccurrenceOptions.builder().zones(zones).build()
    return calendar.occurrences(start, end, options).map {
        OccurrenceOutput(
            uid = it.component.property("UID")?.value,
            summary = it.component.property("SUMMARY")?.value,
            start = it.start.toString(),
            end = it.end.toString(),
            localStart = local?.let { zone -> localOf(it.start, zone) },
            localEnd = local?.let { zone -> localOf(it.end, zone) },
        )
    }.toList().toTypedArray()
}

internal fun zoneOf(id: String): ICalTimeZone =
    requireNotNull(TzData.resolver.resolve(id, ResolveContext.Empty)) { "unknown time zone $id" }

internal fun localOf(instant: Instant, zone: ICalTimeZone): String =
    (instant + zone.offsetAt(instant).totalSeconds.seconds).toLocalDateTime(TimeZone.UTC).toString()

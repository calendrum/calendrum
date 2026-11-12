@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.tooling.ops

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.fixup.Fixups
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.model.get
import io.github.calendrum.occurrence.FreeBusyCalc
import io.github.calendrum.occurrence.Occurrence
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.recur.RecurrenceLimits
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.expand
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tz.toInstant
import io.github.calendrum.tzdata.TzData
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

internal fun expand(request: Request, run: Run) {
    val from = request.requiredInstant("from")
    val to = request.requiredInstant("to")
    if (from >= to) badArgument("The field from must be before to")
    val local = request.text("zone")?.let { zoneOf(it, run) }
    val limit = request.limit()
    val includeCancelled = request.boolean("include_cancelled") ?: false
    val calendars = run.parse(request).calendars
    val options = optionsOf(calendars, local, includeCancelled, run)
    val out = run.out
    out.beginObject()
    out.name("occurrences")
    out.beginArray()
    var count = 0
    calendars@ for (index in calendars.indices) {
        for (occurrence in calendars[index].occurrences(from, to, options)) {
            if (count == limit || !run.hasRoom()) {
                run.truncated = true
                break@calendars
            }
            occurrence(occurrence, index, local, out)
            count++
        }
    }
    out.endArray()
    out.field("count", count)
    run.writeProblems()
    out.endObject()
}

private fun occurrence(occurrence: Occurrence, calendarIndex: Int, local: ICalTimeZone?, out: JsonOut) {
    val component = occurrence.component
    out.beginObject()
    out.field("calendar_index", calendarIndex)
    out.field("component", component.name)
    out.optional("uid", component[Properties.UID])
    out.optional("summary", component[Properties.SUMMARY])
    out.field("start", occurrence.start)
    out.field("end", occurrence.end)
    out.field("all_day", occurrence.isAllDay)
    if (local != null) {
        localField("local_start", occurrence.start, local, out)
        localField("local_end", occurrence.end, local, out)
    }
    occurrence.recurrenceId?.let { writeDateTime("recurrence_id", it, out) }
    if (occurrence.isCancelled) out.field("cancelled", true)
    out.endObject()
}

private fun localField(name: String, instant: Instant, zone: ICalTimeZone, out: JsonOut) {
    out.name(name)
    out.localValue(instant.epochSeconds + zone.offsetAt(instant).totalSeconds) { instant.toString() }
}

private fun optionsOf(
    calendars: List<VCalendar>,
    local: ICalTimeZone?,
    includeCancelled: Boolean,
    run: Run,
    minDatesPerSeries: Int = run.limits.minDatesPerSeries,
): OccurrenceOptions {
    val zones = TimeZoneResolvers.chain(
        *calendars.map(TimeZoneResolvers::vtimezone).toTypedArray(),
        TzData.resolver,
        unknown = UnknownZonePolicy.Utc,
    )
    var series = 0
    for (calendar in calendars) {
        for (child in calendar.component.components) if (child.property("RRULE") != null) series++
    }
    // The dates before the window count for COUNT, so a far window costs a full expansion per series. The sum is what
    // matters: the bound per series falls with their number.
    val perSeries = (
        run.limits.maxExpansionWork / maxOf(
            series,
            1,
        )
        ).coerceIn(minDatesPerSeries, MAX_DATES_PER_SERIES)
    // UTC when the request names no zone, as the contract says. It is a zone of kotlinx-datetime that needs no database.
    val builder = OccurrenceOptions.builder()
    if (local == null) builder.floatingZone(TimeZone.UTC) else builder.floatingICalZone(local)
    return builder
        .zones(zones)
        .includeCancelled(includeCancelled)
        .recurrence(RecurrenceOptions.builder().limits(limitsOf(perSeries)).build())
        .problemListener(run.problems)
        .build()
}

private fun limitsOf(maxOccurrences: Int): RecurrenceLimits =
    RecurrenceLimits.builder().maxOccurrences(maxOccurrences).build()

internal fun rruleExpand(request: Request, run: Run) {
    val rule = recurOf(request.requiredText("rrule"), "rrule")
    val zone = request.text("zone")?.let { zoneOf(it, run).id }
    val start = dateTimeOf(request.requiredText("start"), zone, "start")
    val limit = request.limit()
    val options = RecurrenceOptions.builder()
        .limits(limitsOf(MAX_DATES_PER_SERIES))
        .problemListener(run.problems)
        .build()
    val out = run.out
    out.beginObject()
    out.name("dates")
    out.beginArray()
    var count = 0
    for (date in rule.expand(start, options)) {
        if (count == limit || !run.hasRoom()) {
            run.truncated = true
            break
        }
        out.beginObject()
        writeDateTime("date", date, out)
        date.toInstant(TzData.resolver)?.let { out.field("utc", it) }
        out.endObject()
        count++
    }
    out.endArray()
    out.field("count", count)
    run.writeProblems()
    out.endObject()
}

internal fun freeBusy(request: Request, run: Run) {
    val from = request.requiredInstant("from")
    val to = request.requiredInstant("to")
    if (from >= to) badArgument("The field from must be before to")
    val local = request.text("zone")?.let { zoneOf(it, run) }
    val withConflicts = request.boolean("conflicts") ?: true
    val limit = request.limit()
    val calendars = run.parse(request).calendars
    // The library keeps every interval of `busy` until it merges them, so the budget has no floor here: with one, a file of 15 MB
    // could hold 100 million intervals in memory. A series with more dates than its share makes the result truncated.
    val options = optionsOf(calendars, local, false, run, minDatesPerSeries = 1)
    val out = run.out
    out.beginObject()
    out.name("busy")
    out.beginArray()
    var written = 0
    for (interval in FreeBusyCalc.busy(calendars, from, to, options)) {
        if (written == limit || !run.hasRoom()) {
            run.truncated = true
            break
        }
        out.beginObject()
        out.field("start", interval.start)
        out.field("end", interval.end)
        out.field("type", interval.type.value)
        out.endObject()
        written++
    }
    out.endArray()
    out.name("conflicts")
    out.beginArray()
    if (withConflicts) {
        // conflicts() returns every pair at once, and k overlapping occurrences give k(k - 1)/2 of them.
        if (fewOccurrences(calendars, from, to, options, run.limits.maxConflictOccurrences)) {
            written = 0
            for (conflict in FreeBusyCalc.conflicts(calendars, from, to, options)) {
                if (written == limit || !run.hasRoom()) {
                    run.truncated = true
                    break
                }
                out.beginObject()
                out.field("start", conflict.start)
                out.field("end", conflict.end)
                out.name("first")
                conflictSide(conflict.first, out)
                out.name("second")
                conflictSide(conflict.second, out)
                out.endObject()
                written++
            }
        } else {
            run.truncated = true
        }
    }
    out.endArray()
    run.writeProblems()
    out.endObject()
}

private fun conflictSide(occurrence: Occurrence, out: JsonOut) {
    out.beginObject()
    out.optional("uid", occurrence.component[Properties.UID])
    out.optional("summary", occurrence.component[Properties.SUMMARY])
    out.field("start", occurrence.start)
    out.field("end", occurrence.end)
    out.endObject()
}

private fun fewOccurrences(
    calendars: List<VCalendar>,
    from: Instant,
    to: Instant,
    options: OccurrenceOptions,
    limit: Int,
): Boolean {
    var count = 0
    for (calendar in calendars) {
        for (occurrence in calendar.occurrences(from, to, options)) {
            if (++count > limit) return false
        }
    }
    return true
}

internal fun tzResolve(request: Request, run: Run) {
    val name = request.requiredText("name")
    val at = request.instant("at") ?: run.environment.clock.now()
    val zone = zoneOf(name, run)
    val offset = zone.offsetAt(at)
    val out = run.out
    out.beginObject()
    out.field("id", zone.id)
    out.field("alias", zone.id != name)
    out.field("at", at)
    out.field("offset_seconds", offset.totalSeconds)
    out.field("offset", offset.toString())
    out.field("tzdb_version", TzData.version)
    zone.transitions(at, at + TRANSITION_HORIZON).firstOrNull()?.let { transition ->
        out.name("next_transition")
        out.beginObject()
        out.field("at", transition.at)
        out.field("offset_seconds_after", transition.offsetAfter.totalSeconds)
        out.endObject()
    }
    run.writeProblems()
    out.endObject()
}

internal fun zoneOf(name: String, run: Run): ICalTimeZone {
    checkToken(name, "zone")
    val resolved = TzData.resolver.resolve(name, ResolveContext(null, run.problems))
        ?: throw OpsFailure(UNKNOWN_ZONE, "The time zone is not known")
    return TzData.zone(name) ?: TzData.zone(ianaNameOf(name)) ?: resolved
}

// The resolver names a zone as the request does, also an alias such as a Windows name. The fixup that rewrites a TZID is
// the way to the IANA name that the public API has.
private fun ianaNameOf(name: String): String {
    val start = ICalProperty.builder("DTSTART").parameter("TZID", name).value("20260101T000000").build()
    val calendar = VCalendar.of(ICalComponent("VCALENDAR") { component("VEVENT") { property(start) } }) ?: return name
    val fixed = Fixups.windowsTzids.apply(calendar).calendar.component.components.first()
    return fixed.property("DTSTART")?.parameter("TZID")?.values?.firstOrNull() ?: name
}

internal fun recurOf(text: String, field: String): Recur {
    checkToken(text, field)
    val holder = ICalComponent("VEVENT") { property("RRULE", text.removePrefix("RRULE:")) }
    return holder[Properties.RRULE] ?: badArgument("The field $field is not a recurrence rule")
}

/** A date, a moment with its offset, or a local time, which needs a zone: nothing is assumed about a time without one. */
internal fun dateTimeOf(text: String, zone: String?, field: String): ICalDateTime {
    checkToken(text, field)
    if (text.length == DATE_LENGTH) return ICalDateTime.Date(localDate(text, field))
    if (text.endsWith('Z') || hasOffset(text)) {
        if (zone != null) badArgument("The field $field has its own offset, so it takes no zone")
        val moment = try {
            Instant.parse(text)
        } catch (_: IllegalArgumentException) {
            badArgument("The field $field must be a moment in ISO 8601")
        }
        return ICalDateTime.Utc(moment)
    }
    if (zone == null) badArgument("The field $field is a local time, so it needs a zone")
    return ICalDateTime.Zoned(localDateTime(text, field), zone)
}

private fun hasOffset(text: String): Boolean {
    val sign = text.length - OFFSET_LENGTH
    return sign > 0 && (text[sign] == '+' || text[sign] == '-') && text[sign + 3] == ':'
}

internal fun writeDateTime(name: String, value: ICalDateTime, out: JsonOut) {
    out.name(name)
    when (value) {
        is ICalDateTime.Date -> out.value(value.date)

        is ICalDateTime.Floating -> out.value(value.dateTime)

        is ICalDateTime.Utc -> out.value(value.instant)

        is ICalDateTime.Zoned -> {
            out.value(value.dateTime)
            out.field("${name}_zone", value.tzid)
        }
    }
}

private const val OFFSET_LENGTH = 6
private const val DATE_LENGTH = 10
private const val MAX_DATES_PER_SERIES = 1_000_000
private val TRANSITION_HORIZON = 400.days

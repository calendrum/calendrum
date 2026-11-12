package io.github.calendrum.examples.android

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tzdata.TzData
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class ScheduleItem(val title: String, val day: String, val time: String, val location: String)

object Schedule {
    fun from(feed: ByteArray, from: Instant, to: Instant, zone: TimeZone): List<ScheduleItem> {
        val calendar = ICal.parse(feed).calendars.firstOrNull() ?: return emptyList()
        val zones = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TzData.resolver,
            unknown = UnknownZonePolicy.Utc,
        )
        val options = OccurrenceOptions.builder().zones(zones).build()
        return calendar.occurrences(from, to, options).map { occurrence ->
            val start = occurrence.start.toLocalDateTime(zone)
            val end = occurrence.end.toLocalDateTime(zone)
            ScheduleItem(
                title = occurrence.component.property("SUMMARY")?.value.orEmpty(),
                day = start.date.toString(),
                time = "${start.time}–${end.time}",
                location = occurrence.component.property("LOCATION")?.value.orEmpty(),
            )
        }.toList()
    }
}

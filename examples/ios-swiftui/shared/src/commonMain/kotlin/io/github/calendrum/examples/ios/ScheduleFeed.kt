package io.github.calendrum.examples.ios

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

// region swift-kotlin
class ScheduleEntry(val title: String, val day: String, val time: String, val location: String)

class ScheduleFeed(private val zoneId: String) {
    // `@Throws` makes Swift see a bad window or zone as a thrown error; the window is parsed first so that an empty
    // feed with a bad window still throws.
    @Throws(IllegalArgumentException::class)
    fun entries(feed: String, from: String, to: String): List<ScheduleEntry> {
        val zone = TimeZone.of(zoneId)
        val window = Instant.parse(from) to Instant.parse(to)
        val calendar = ICal.parse(feed).calendars.firstOrNull() ?: return emptyList()
        val zones = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TzData.resolver,
            unknown = UnknownZonePolicy.Utc,
        )
        val options = OccurrenceOptions.builder().zones(zones).build()
        return calendar.occurrences(window.first, window.second, options).map { occurrence ->
            val start = occurrence.start.toLocalDateTime(zone)
            val end = occurrence.end.toLocalDateTime(zone)
            ScheduleEntry(
                title = occurrence.component.property("SUMMARY")?.value.orEmpty(),
                day = start.date.toString(),
                time = "${start.time}–${end.time}",
                location = occurrence.component.property("LOCATION")?.value.orEmpty(),
            )
        }.toList()
    }
}
// endregion

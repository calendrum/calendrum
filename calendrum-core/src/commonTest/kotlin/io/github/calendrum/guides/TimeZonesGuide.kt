package io.github.calendrum.guides

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolver
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tz.toInstant
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class TimeZonesGuide {
    private val calendar = ICal.parse(calendarText).calendars.single()

    @Test
    fun theDefaultChain() {
        // region tz-default
        // VTIMEZONE of the file, then aliases; unknown TZID → floating
        val zones = TimeZoneResolvers.default(calendar)
        val start = calendar.events.first().start!! // 2026-10-01T10:00[Europe/Berlin]
        val instant: Instant? = start.toInstant(zones) // 2026-10-01T08:00:00Z
        // endregion
        assertEquals(Instant.parse("2026-10-01T08:00:00Z"), instant)
    }

    @Test
    fun floatingTimesNeedAnExplicitZone() {
        val zones = TimeZoneResolvers.default(calendar)
        // region tz-floating
        val breakfast = ICalDateTime.Floating(LocalDateTime(2026, 10, 1, 8, 0))
        val unknown = breakfast.toInstant(zones) // null: the library never guesses
        val inUtc = breakfast.toInstant(zones, TimeZone.UTC) // the zone is your decision, any kotlinx TimeZone
        // endregion
        assertEquals(null, unknown)
        assertEquals(Instant.parse("2026-10-01T08:00:00Z"), inUtc)
    }

    @Test
    fun gapsAndOverlaps() {
        val zones = TimeZoneResolvers.default(calendar)
        // region tz-gaps
        // 02:00 → 03:00 is skipped
        val inTheGap = ICalDateTime.Zoned(LocalDateTime(2026, 3, 29, 2, 30), "Europe/Berlin")
        // 02:00–03:00 happens twice
        val twice = ICalDateTime.Zoned(LocalDateTime(2026, 10, 25, 2, 30), "Europe/Berlin")
        // 2026-03-29T01:30:00Z: a gap moves forward by its length (03:30 CEST)
        val moved = inTheGap.toInstant(zones)
        // 2026-10-25T00:30:00Z: an overlap takes the first occurrence (CEST)
        val first = twice.toInstant(zones)
        // endregion
        assertEquals(Instant.parse("2026-03-29T01:30:00Z"), moved)
        assertEquals(Instant.parse("2026-10-25T00:30:00Z"), first)
    }

    @Test
    fun chains() {
        val zones: TimeZoneResolver = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TimeZoneResolvers.aliases,
            unknown = UnknownZonePolicy.Utc,
        )
        // region tz-chain
        val chain = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar), // the file first: it says what its author meant
            TimeZoneResolvers.iana, // the platform database, only when you ask for it
            TimeZoneResolvers.aliases, // Windows and Outlook names, /mozilla.org/ prefixes, old links
            unknown = UnknownZonePolicy.Floating, // or Utc, or Fail
        )
        val zone: ICalTimeZone? = chain.resolve("Europe/Berlin", ResolveContext.Empty)
        // endregion
        assertEquals("Europe/Berlin", zone?.id)
        assertEquals("Europe/Berlin", zones.resolve("Europe/Berlin", ResolveContext.Empty)?.id)
        val unknown = zones.resolve("Nowhere/Land", ResolveContext.Empty)
        assertEquals(0, unknown?.offsetAt(Instant.parse("2026-10-01T00:00:00Z"))?.totalSeconds)
    }

    @Test
    fun aRecurringInstanceInTheGapStillHappens() {
        val text = calendarText
            .replace("DTSTART;TZID=Europe/Berlin:20261001T100000", "DTSTART;TZID=Europe/Berlin:20260322T023000")
            .replace("DTEND;TZID=Europe/Berlin:20261001T110000", "DTEND;TZID=Europe/Berlin:20260322T033000")
            .replace("BYDAY=TH;COUNT=4", "BYDAY=SU;COUNT=2")
        val weekly = ICal.parse(text).calendars.single()
        val options = OccurrenceOptions.builder().zones(TimeZoneResolvers.default(weekly)).build()
        val from = Instant.parse("2026-03-01T00:00:00Z")
        val to = Instant.parse("2026-04-30T00:00:00Z")
        val starts = weekly.occurrences(from, to, options)
            .filter { it.component.property("SUMMARY")?.value == "Weekly sync" }
            .map { it.start.toString() }
            .toList()
        assertEquals(listOf("2026-03-22T01:30:00Z", "2026-03-29T01:30:00Z"), starts)
    }
}

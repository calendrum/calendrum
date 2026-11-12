package io.github.calendrum.tzdata.guides

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tz.toInstant
import io.github.calendrum.tzdata.TzData
import io.github.calendrum.tzdata.VTimeZoneMode
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class TzDataGuide {
    private val text =
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Guides//EN\r\nBEGIN:VEVENT\r\nUID:1@example.com\r\n" +
            "DTSTAMP:20260901T000000Z\r\nDTSTART;TZID=Europe/Berlin:20261001T100000\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

    @Test
    fun resolveWithTheEmbeddedDatabase() {
        val calendar = ICal.parse(text).calendars.single()
        // region tzdata-resolver
        val zones = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar), // a VTIMEZONE in the file still wins
            TzData.resolver, // the same answer on every platform
            unknown = UnknownZonePolicy.Utc,
        )
        val start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
        val instant: Instant? = start.toInstant(zones) // 2026-10-01T08:00:00Z
        val version = TzData.version // the tzdb release, for example "2026b"
        // endregion
        assertEquals(Instant.parse("2026-10-01T08:00:00Z"), instant)
        assertTrue(version.isNotEmpty())
    }

    @Test
    fun writeTheMissingTimeZones() {
        val calendar = ICal.parse(text).calendars.single()
        // region tzdata-write
        val writer = ICal.writer(WriteOptions.builder().addMissingTimeZones(TzData).timeZonesFirst(true).build())
        val ics = writer.encodeToString(calendar.component) // a VTIMEZONE for every TZID that has none
        val zone = TzData.vtimezone("Europe/Berlin", VTimeZoneMode.OutlookCompatible) // or build one yourself
        // endregion
        assertTrue("BEGIN:VTIMEZONE\r\nTZID:Europe/Berlin\r\n" in ics)
        assertEquals("Europe/Berlin", zone?.tzid)
        assertEquals(true, ics.indexOf("BEGIN:VTIMEZONE") < ics.indexOf("BEGIN:VEVENT"))
    }
}

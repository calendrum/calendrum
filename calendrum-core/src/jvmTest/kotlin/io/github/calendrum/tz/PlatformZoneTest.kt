package io.github.calendrum.tz

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Instant

class PlatformZoneTest {
    @Test
    fun theIanaResolverWrapsThePlatformDatabase() {
        val zone = assertNotNull(TimeZoneResolvers.iana.resolve("Europe/Berlin", ResolveContext.Empty))
        assertEquals("Europe/Berlin", zone.id)
        assertEquals(
            listOf("2026-03-29T01:00:00Z +01:00 -> +02:00", "2026-10-25T01:00:00Z +02:00 -> +01:00"),
            zone.transitions(Instant.parse("2026-01-01T00:00:00Z"), Instant.parse("2027-01-01T00:00:00Z"))
                .map { "${it.at} ${it.offsetBefore} -> ${it.offsetAfter}" },
        )
        assertEquals(Instant.parse("2026-03-29T01:30:00Z"), zone.toInstant(LocalDateTime(2026, 3, 29, 2, 30)))
        assertNull(TimeZoneResolvers.iana.resolve("W. Europe Standard Time", ResolveContext.Empty))
        assertNull(TimeZoneResolvers.iana.resolve("/mozilla.org/x/Europe/Berlin", ResolveContext.Empty))
        assertNull(TimeZoneResolvers.iana.resolve("Nowhere/Nothing", ResolveContext.Empty))
        val alias = assertNotNull(TimeZoneResolvers.aliases.resolve("W. Europe Standard Time", ResolveContext.Empty))
        assertEquals("W. Europe Standard Time", alias.id)
        assertEquals(
            1,
            alias.transitions(Instant.parse("2026-03-01T00:00:00Z"), Instant.parse("2026-04-01T00:00:00Z")).size,
        )
        assertEquals("+02:00", alias.offsetAt(Instant.parse("2026-07-01T00:00:00Z")).toString())
        assertEquals(Instant.parse("2026-07-01T10:00:00Z"), alias.toInstant(LocalDateTime(2026, 7, 1, 12, 0)))
        val fallback = TimeZoneResolvers.default(null)
        assertNull(fallback.resolve("America/New_York", ResolveContext.Empty))
        val explicit = TimeZoneResolvers.chain(
            TimeZoneResolvers.iana,
            TimeZoneResolvers.aliases,
            unknown = UnknownZonePolicy.Floating,
        )
        assertNotNull(explicit.resolve("America/New_York", ResolveContext.Empty))
        val utc = TimeZoneResolvers.chain(unknown = UnknownZonePolicy.Utc).resolve("x", ResolveContext.Empty)!!
        assertEquals(
            emptyList(),
            utc.transitions(Instant.parse("2026-01-01T00:00:00Z"), Instant.parse("2027-01-01T00:00:00Z")),
        )
        assertEquals(Instant.parse("2026-07-01T12:00:00Z"), utc.toInstant(LocalDateTime(2026, 7, 1, 12, 0)))
        assertEquals("ICalTimeZone(x)", utc.toString())
    }

    @Test
    fun ianaIsAnExplicitChoiceForOccurrences() {
        val text = listOf(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN",
            "BEGIN:VEVENT", "UID:n", "DTSTART;TZID=America/New_York:20260601T100000",
            "RRULE:FREQ=DAILY;COUNT=2", "END:VEVENT",
            "BEGIN:VEVENT", "UID:k", "DTSTART;TZID=Europe/Kiev:20260601T100000", "END:VEVENT",
            "END:VCALENDAR",
        ).joinToString("") { "$it\r\n" }
        val calendar = ICal.parse(text).calendars.single()
        val from = Instant.parse("2026-05-01T00:00:00Z")
        val to = Instant.parse("2026-07-01T00:00:00Z")
        assertEquals(emptyList(), calendar.occurrences(from, to).toList())
        val zones = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TimeZoneResolvers.iana,
            TimeZoneResolvers.aliases,
            unknown = UnknownZonePolicy.Floating,
        )
        val options = OccurrenceOptions.builder().zones(zones).build()
        assertEquals(
            listOf("2026-06-01T07:00:00Z", "2026-06-01T14:00:00Z", "2026-06-02T14:00:00Z"),
            calendar.occurrences(from, to, options).map { it.start.toString() }.toList(),
        )
    }
}

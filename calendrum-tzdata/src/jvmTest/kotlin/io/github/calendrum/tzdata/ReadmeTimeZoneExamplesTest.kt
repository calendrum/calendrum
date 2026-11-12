package io.github.calendrum.tzdata

import io.github.calendrum.ICal
import io.github.calendrum.model.Properties
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.OccurrenceSet
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tz.instant
import io.github.calendrum.tz.toInstant
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class ReadmeTimeZoneExamplesTest {
    private val calendar = ICal.parse(
        listOf(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN",
            "BEGIN:VEVENT", "UID:1", "DTSTART;TZID=Eastern Standard Time:20261005T090000", "DURATION:PT1H",
            "RRULE:FREQ=WEEKLY;COUNT=3", "SUMMARY:Lesson",
            "BEGIN:VALARM", "ACTION:DISPLAY", "TRIGGER:-PT10M", "END:VALARM", "END:VEVENT",
            "BEGIN:VEVENT", "UID:2", "DTSTART;VALUE=DATE:20261012", "SUMMARY:Holiday", "END:VEVENT",
            "END:VCALENDAR",
        ).joinToString("") { "$it\r\n" },
    ).calendars.single()
    private val from = Instant.parse("2026-10-01T00:00:00Z")
    private val to = Instant.parse("2026-11-01T00:00:00Z")
    private val now = Instant.parse("2026-10-06T00:00:00Z")
    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    @Test
    fun theTimeZoneSnippetOfTheReadmeCompilesAndWorks() {
        val event = calendar.events.first()
        val zones = TimeZoneResolvers.default(calendar)
        val withIana = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TimeZoneResolvers.iana,
            TimeZoneResolvers.aliases,
            unknown = UnknownZonePolicy.Floating,
        )
        val start: Instant? = event.start?.toInstant(withIana)
        assertEquals(Instant.parse("2026-10-05T13:00:00Z"), start)
        assertEquals(null, event.start?.toInstant(zones))
        val options = OccurrenceOptions.builder()
            .zones(
                TimeZoneResolvers.chain(
                    TimeZoneResolvers.vtimezone(calendar),
                    TzData.resolver,
                    unknown = UnknownZonePolicy.Utc,
                ),
            )
            .floatingZone(TimeZone.of("Europe/Berlin"))
            .build()
        for (occurrence in calendar.occurrences(from, to, options)) {
            println("${occurrence.start}..${occurrence.end} ${occurrence.component.property("SUMMARY")?.value}")
            occurrence.alarms.forEach { println("  alarm at ${it.at}") }
        }
        assertEquals("2026-10-05T13:00:00Z..2026-10-05T14:00:00Z Lesson", printed[0])
        assertEquals("  alarm at 2026-10-05T12:50:00Z", printed[1])
        assertEquals("2026-10-11T22:00:00Z..2026-10-12T22:00:00Z Holiday", printed[2])
        val cursor = calendar.occurrences(options)
        val next = cursor.after(now)
        assertEquals(Instant.parse("2026-10-11T22:00:00Z"), next?.start)
        val all = calendar.occurrences(from, to, options).toList()
        val teaching = all.filter { it.component.property("SUMMARY")?.value == "Lesson" }
        val holidays = all.filter { it.isAllDay }
        val lessons = OccurrenceSet.of(teaching).minus(OccurrenceSet.of(holidays))
        assertEquals(2, lessons.occurrences.size)
        val ics = ICal.writer(
            WriteOptions.builder().addMissingTimeZones(TzData).build(),
        ).encodeToString(calendar.component)
        assertTrue(ics.contains("BEGIN:VTIMEZONE\r\nTZID:Eastern Standard Time"))
        val begins = Properties.DTSTART.instant(zones)
        assertEquals("DTSTART", begins.name)
    }
}

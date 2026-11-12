package io.github.calendrum.tzdata

import io.github.calendrum.ICal
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tz.toInstant
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class TzDataTest {
    private fun offset(id: String, utc: String): String = TzData.zone(id)!!.offsetAt(Instant.parse(utc)).toString()

    @Test
    fun theTableKnowsEveryZone() {
        assertEquals("2026e", TzData.version)
        assertTrue(TzData.zoneIds.size > 590)
        assertTrue("Europe/Kyiv" in TzData.zoneIds && "Europe/Kiev" in TzData.zoneIds)
        assertNull(TzData.zone("Nowhere/Nothing"))
        assertEquals("+03:00", offset("Europe/Moscow", "2026-01-01T00:00:00Z"))
        assertEquals("-03:00", offset("America/Sao_Paulo", "2026-01-01T00:00:00Z"))
        assertEquals("+05:30", offset("Asia/Kolkata", "2026-01-01T00:00:00Z"))
        assertEquals("+05:45", offset("Asia/Kathmandu", "2026-01-01T00:00:00Z"))
        assertEquals("+02:00", offset("Europe/Berlin", "2160-07-01T00:00:00Z"))
    }

    @Test
    fun usDaylightTimeStartsOnTheSecondSunday() {
        assertEquals("-05:00", offset("America/New_York", "2025-03-09T06:59:59Z"))
        assertEquals("-04:00", offset("America/New_York", "2025-03-09T07:00:00Z"))
        assertEquals("-05:00", offset("America/New_York", "2025-03-02T12:00:00Z"))
        val zone = TzData.zone("America/New_York")!!
        assertEquals(Instant.parse("2025-03-09T07:30:00Z"), zone.toInstant(LocalDateTime(2025, 3, 9, 2, 30)))
        assertEquals(Instant.parse("2025-11-02T05:30:00Z"), zone.toInstant(LocalDateTime(2025, 11, 2, 1, 30)))
    }

    @Test
    fun lordHoweShiftsByThirtyMinutes() {
        assertEquals("+10:30", offset("Australia/Lord_Howe", "2026-07-01T00:00:00Z"))
        assertEquals("+11:00", offset("Australia/Lord_Howe", "2026-12-01T00:00:00Z"))
        val zone = TzData.zone("Australia/Lord_Howe")!!
        assertEquals(Instant.parse("2026-10-03T15:45:00Z"), zone.toInstant(LocalDateTime(2026, 10, 4, 2, 15)))
        assertEquals(Instant.parse("2026-04-04T14:45:00Z"), zone.toInstant(LocalDateTime(2026, 4, 5, 1, 45)))
        val transitions = zone.transitions(Instant.parse("2026-01-01T00:00:00Z"), Instant.parse("2027-01-01T00:00:00Z"))
        assertEquals(
            listOf("+11:00 -> +10:30", "+10:30 -> +11:00"),
            transitions.map {
                "${it.offsetBefore} -> ${it.offsetAfter}"
            },
        )
    }

    @Test
    fun theResolverAcceptsAliases() {
        val problems = ArrayList<Problem>()
        val context = ResolveContext(null, ProblemListener { problems += it })
        val zone = assertNotNull(TzData.resolver.resolve("Eastern Standard Time", context))
        assertEquals("Eastern Standard Time", zone.id)
        assertEquals("-04:00", zone.offsetAt(Instant.parse("2026-07-01T00:00:00Z")).toString())
        assertEquals(listOf(ProblemCode.TimeZoneAlias), problems.map { it.code })
        val chain = TimeZoneResolvers.chain(TzData.resolver, unknown = UnknownZonePolicy.Floating)
        val start = ICalDateTime.Zoned(LocalDateTime(2026, 7, 1, 9, 0), "/mozilla.org/20050126_1/Europe/Berlin")
        assertEquals(Instant.parse("2026-07-01T07:00:00Z"), start.toInstant(chain))
    }

    @Test
    fun writtenTimeZonesCompileToTheSameOffsets() {
        for (id in listOf(
            "Europe/Berlin",
            "America/New_York",
            "Australia/Lord_Howe",
            "Asia/Tokyo",
            "America/Nuuk",
            "Pacific/Easter",
        )) {
            for (mode in VTimeZoneMode.entries) {
                val written = TzData.vtimezone(id, mode)!!
                val text =
                    "BEGIN:VCALENDAR\r\nVERSION:2.0\r\n" + ICal.writer().encodeToString(written.component) +
                        "END:VCALENDAR\r\n"
                val calendar = ICal.parse(text).calendars.single()
                val compiled = TimeZoneResolvers.vtimezone(calendar).resolve(id, ResolveContext.Empty)!!
                val expected = TzData.zone(id)!!
                var at = Instant.parse("2027-01-01T00:00:00Z").epochSeconds
                while (at < Instant.parse("2037-12-31T00:00:00Z").epochSeconds) {
                    val instant = Instant.fromEpochSeconds(at)
                    assertEquals(expected.offsetAt(instant), compiled.offsetAt(instant), "$id $mode $instant")
                    at += 86_400 * 3
                }
            }
        }
        val outlook = TzData.vtimezone("Europe/Berlin", VTimeZoneMode.OutlookCompatible)!!
        assertEquals(2, outlook.observances.size)
        assertTrue(outlook.observances.all { it.start.toString().startsWith("1601-") })
    }

    @Test
    fun addedTimeZonesMatchTheTableOnCurrentYearsInEveryZone() {
        val irregular = ArrayList<String>()
        for (id in TzData.zoneIds.sorted()) {
            val expected = TzData.zone(id)!!
            val samples = currentSamples(expected)
            val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//x//EN\r\nBEGIN:VEVENT\r\nUID:1\r\n" +
                "DTSTART;TZID=$id:20260901T090000\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
            val written = ICal.writer(WriteOptions.builder().addMissingTimeZones(TzData).build())
                .encodeToString(ICal.parse(text).components)
            val calendar = ICal.parse(written).calendars.single()
            val compiled = TimeZoneResolvers.vtimezone(calendar).resolve(id, ResolveContext.Empty)!!
            for (at in samples) assertEquals(expected.offsetAt(at), compiled.offsetAt(at), "$id $at")
            val problems = ArrayList<Problem>()
            val outlook = TzData.vtimezone(
                id,
                VTimeZoneMode.OutlookCompatible,
                null,
                ProblemListener {
                    problems += it
                },
            )!!
            val outlookText = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\n" + ICal.writer().encodeToString(outlook.component) +
                "END:VCALENDAR\r\n"
            val outlookZone = TimeZoneResolvers.vtimezone(ICal.parse(outlookText).calendars.single())
                .resolve(id, ResolveContext.Empty)!!
            val exact = samples.all { expected.offsetAt(it) == outlookZone.offsetAt(it) }
            assertEquals(!exact, problems.isNotEmpty(), "$id: Outlook form exact = $exact, problems = $problems")
            assertTrue(problems.all { it.code == ProblemCode.IrregularTimeZone })
            val outlookWritten = calendar.timeZones.single().observances.all { it.start.toString().startsWith("1601-") }
            assertEquals(exact, outlookWritten, id)
            if (!exact) irregular += id
        }
        println("time zones written in the Full mode by addMissingTimeZones: $irregular")
        assertTrue(listOf("Africa/Cairo", "Africa/Casablanca", "Asia/Gaza").all { it in irregular }, "$irregular")
    }

    private fun currentSamples(zone: io.github.calendrum.tz.ICalTimeZone): List<Instant> {
        val start = Instant.parse("2026-01-01T00:00:00Z")
        val end = Instant.parse("2038-01-01T00:00:00Z")
        val result = ArrayList<Instant>()
        for (transition in zone.transitions(start, end)) {
            val at = transition.at.epochSeconds
            for (delta in -1L..1L) result += Instant.fromEpochSeconds(at + delta)
        }
        var at = start.epochSeconds
        while (at < end.epochSeconds) {
            result += Instant.fromEpochSeconds(at)
            at += 86_400 * 5
        }
        return result
    }

    @Test
    fun theWriterAddsMissingTimeZones() {
        val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//x//EN\r\nBEGIN:VEVENT\r\nUID:1\r\n" +
            "DTSTART;TZID=Europe/Berlin:20260901T090000\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val parsed = ICal.parse(text)
        val written = ICal.writer(
            WriteOptions.builder().addMissingTimeZones(TzData).build(),
        ).encodeToString(parsed.components)
        val calendar = ICal.parse(written).calendars.single()
        assertEquals(listOf("Europe/Berlin"), calendar.timeZones.map { it.tzid })
        assertTrue(
            written.contains("BEGIN:VEVENT\r\nUID:1\r\nDTSTART;TZID=Europe/Berlin:20260901T090000\r\nEND:VEVENT"),
        )
        val again = ICal.writer(
            WriteOptions.builder().addMissingTimeZones(TzData).build(),
        ).encodeToString(ICal.parse(written).components)
        assertEquals(written, again)
        assertEquals(text, ICal.writer().encodeToString(parsed.components))
    }
}

package io.github.calendrum.tz

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.IllegalTimeZoneException
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class TimeZoneTest {
    private val problems = ArrayList<Problem>()
    private val context = ResolveContext(null, ProblemListener { problems += it })

    private fun compiled(zone: VendorZone): ICalTimeZone {
        val calendar = calendarOf(zone.text)
        return TimeZoneResolvers.vtimezone(calendar).resolve(calendar.timeZones.single().tzid!!, context)!!
    }

    private fun offset(zone: ICalTimeZone, utc: String): String = zone.offsetAt(Instant.parse(utc)).toString()

    @Test
    fun usRulesWithSecondSunday() {
        for (vendor in listOf(GOOGLE_NEW_YORK, OUTLOOK_EASTERN, RFC_EASTERN)) {
            val zone = compiled(vendor)
            assertEquals("-05:00", offset(zone, "2025-03-09T06:59:59Z"), vendor.vendor)
            assertEquals("-04:00", offset(zone, "2025-03-09T07:00:00Z"), vendor.vendor)
            assertEquals("-05:00", offset(zone, "2025-03-02T12:00:00Z"), vendor.vendor)
            assertEquals("-04:00", offset(zone, "2025-11-02T05:59:59Z"), vendor.vendor)
            assertEquals("-05:00", offset(zone, "2025-11-02T06:00:00Z"), vendor.vendor)
            assertEquals(
                listOf("2026-03-08T07:00:00Z -05:00 -> -04:00", "2026-11-01T06:00:00Z -04:00 -> -05:00"),
                zone.transitions(Instant.parse("2026-01-01T00:00:00Z"), Instant.parse("2027-01-01T00:00:00Z"))
                    .map { "${it.at} ${it.offsetBefore} -> ${it.offsetAfter}" },
                vendor.vendor,
            )
        }
        val rfc = compiled(RFC_EASTERN)
        assertEquals("-04:00", offset(rfc, "1974-01-06T07:00:00Z"))
        assertEquals("-04:00", offset(rfc, "1975-02-23T07:00:00Z"))
        assertEquals("-05:00", offset(rfc, "1990-01-01T00:00:00Z"))
    }

    @Test
    fun gapsMoveForwardAndOverlapsTakeTheFirst() {
        val zone = compiled(APPLE_BERLIN)
        assertEquals(Instant.parse("2026-03-29T01:30:00Z"), zone.toInstant(LocalDateTime(2026, 3, 29, 2, 30)))
        assertEquals(Instant.parse("2026-03-29T01:00:00Z"), zone.toInstant(LocalDateTime(2026, 3, 29, 3, 0)))
        assertEquals(Instant.parse("2026-10-25T00:30:00Z"), zone.toInstant(LocalDateTime(2026, 10, 25, 2, 30)))
        assertEquals(Instant.parse("2026-10-25T02:00:00Z"), zone.toInstant(LocalDateTime(2026, 10, 25, 3, 0)))
        assertEquals(Instant.parse("2026-07-01T10:00:00Z"), zone.toInstant(LocalDateTime(2026, 7, 1, 12, 0)))
        assertEquals("+01:00", offset(zone, "2099-12-01T00:00:00Z"))
        assertEquals("+02:00", offset(zone, "2099-07-01T00:00:00Z"))
        assertEquals("+02:00", offset(zone, "3100-07-01T00:00:00Z"))
    }

    @Test
    fun lordHoweMovesByHalfAnHour() {
        val zone = compiled(LORD_HOWE)
        assertEquals("+10:30", offset(zone, "2026-07-01T00:00:00Z"))
        assertEquals("+11:00", offset(zone, "2026-12-01T00:00:00Z"))
        assertEquals(Instant.parse("2026-10-03T15:45:00Z"), zone.toInstant(LocalDateTime(2026, 10, 4, 2, 15)))
        assertEquals(Instant.parse("2026-04-04T14:45:00Z"), zone.toInstant(LocalDateTime(2026, 4, 5, 1, 45)))
        assertEquals(Instant.parse("2026-04-04T15:30:00Z"), zone.toInstant(LocalDateTime(2026, 4, 5, 2, 0)))
    }

    @Test
    fun zonesWithoutDaylightTime() {
        assertEquals("+09:00", offset(compiled(APPLE_TOKYO), "2026-06-01T00:00:00Z"))
        assertEquals("+03:00", offset(compiled(THUNDERBIRD_MOSCOW), "2026-06-01T00:00:00Z"))
        val utc = compiled(YANDEX_UTC)
        assertEquals("Z", offset(utc, "2026-06-01T00:00:00Z"))
        assertEquals(
            emptyList(),
            utc.transitions(Instant.parse("1600-01-01T00:00:00Z"), Instant.parse("2100-01-01T00:00:00Z")),
        )
        assertEquals("+01:00", offset(compiled(OUTLOOK_BERLIN), "2026-01-15T00:00:00Z"))
        assertTrue(problems.isEmpty(), problems.toString())
    }

    @Test
    fun theDefaultChainAndItsPolicies() {
        val calendar = calendarOf(OUTLOOK_BERLIN.text)
        val zones = TimeZoneResolvers.default(calendar)
        val local = LocalDateTime(2026, 7, 1, 12, 0)
        assertEquals(
            Instant.parse("2026-07-01T10:00:00Z"),
            ICalDateTime.Zoned(local, "W. Europe Standard Time").toInstant(zones),
        )
        assertEquals(
            Instant.parse("2026-07-01T10:00:00Z"),
            ICalDateTime.Utc(Instant.parse("2026-07-01T10:00:00Z")).toInstant(zones),
        )
        assertNull(ICalDateTime.Floating(local).toInstant(zones))
        assertEquals(
            Instant.parse("2026-07-01T12:00:00Z"),
            ICalDateTime.Floating(local).toInstant(zones, TimeZone.UTC),
        )
        assertEquals(
            Instant.parse("2026-07-01T00:00:00Z"),
            ICalDateTime.Date(LocalDate(2026, 7, 1)).toInstant(zones, TimeZone.UTC),
        )
        val sink = ArrayList<Problem>()
        val unknown = ResolveContext(null, ProblemListener { sink += it })
        assertNull(zones.resolve("Nowhere/Nothing", unknown))
        assertEquals(listOf(ProblemCode.UnknownTimeZone), sink.map { it.code })
        val utc = TimeZoneResolvers.chain(TimeZoneResolvers.vtimezone(calendar), unknown = UnknownZonePolicy.Utc)
        assertEquals(
            UtcOffset.ZERO,
            utc.resolve("Nowhere/Nothing", ResolveContext.Empty)!!.offsetAt(Instant.parse("2026-01-01T00:00:00Z")),
        )
        val fail = TimeZoneResolvers.chain(unknown = UnknownZonePolicy.Fail)
        assertFailsWith<IllegalTimeZoneException> { fail.resolve("Nowhere/Nothing", ResolveContext.Empty) }
    }

    @Test
    fun aliasesFindIanaZones() {
        val cases = mapOf(
            "W. Europe Standard Time" to "Europe/Berlin",
            "Russian Standard Time" to "Europe/Moscow",
            "/mozilla.org/20050126_1/Europe/Moscow" to "Europe/Moscow",
            "/citadel.org/20190914_1/America/New_York" to "America/New_York",
            "Europe/Kiev" to "Europe/Kyiv",
            "(UTC+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna" to "Europe/Berlin",
            "(GMT+01.00) Amsterdam / Berlin / Bern / Rome / Stockholm / Vienna" to "Europe/Berlin",
            "(UTC-05:00) Eastern Time (US & Canada)" to "America/New_York",
        )
        for ((tzid, expected) in cases) {
            val match = assertNotNull(TimeZoneAliases.resolve(tzid), tzid)
            assertEquals(expected, match.ianaId, tzid)
            assertEquals(false, match.guessed, tzid)
        }
        val guess = assertNotNull(TimeZoneAliases.resolve("(UTC+03:00) Moscow, St. Petersburg, Volgograd (old)"))
        assertEquals("Europe/Moscow", guess.ianaId)
        assertNull(TimeZoneAliases.resolve("Totally Unknown Zone"))
    }
}

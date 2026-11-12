package io.github.calendrum.tz

import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class VTimeZoneDifferentialTest {
    private val ranges = mapOf(
        APPLE_BERLIN to 1997..2037,
        APPLE_TOKYO to 1970..2037,
        OUTLOOK_BERLIN to 1997..2037,
        OUTLOOK_EASTERN to 2008..2037,
        GOOGLE_NEW_YORK to 2008..2037,
        THUNDERBIRD_MOSCOW to 2015..2037,
        LORD_HOWE to 2008..2037,
        RFC_EASTERN to 1968..2037,
        YANDEX_UTC to 1970..2037,
    )

    @Test
    fun vendorZonesMatchJavaTime() {
        for ((vendor, years) in ranges) {
            val calendar = calendarOf(vendor.text)
            val zone = TimeZoneResolvers.vtimezone(
                calendar,
            ).resolve(calendar.timeZones.single().tzid!!, ResolveContext.Empty)!!
            val rules = ZoneId.of(vendor.ianaId).rules
            var instant = LocalDateTime.of(years.first, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC)
            val end = LocalDateTime.of(years.last + 1, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC)
            while (instant < end) {
                val expected = rules.getOffset(java.time.Instant.ofEpochSecond(instant)).totalSeconds
                assertEquals(
                    expected,
                    zone.offsetAt(Instant.fromEpochSeconds(instant)).totalSeconds,
                    "${vendor.vendor} $instant",
                )
                instant += 6 * 3600
            }
            var local = LocalDateTime.of(years.first, 1, 2, 0, 0)
            val last = LocalDateTime.of(years.last, 12, 30, 0, 0)
            while (local < last) {
                val expected = local.atZone(ZoneId.of(vendor.ianaId)).toEpochSecond()
                assertEquals(
                    expected,
                    zone.toInstant(local.toKotlinLocalDateTime()).epochSeconds,
                    "${vendor.vendor} $local",
                )
                local = local.plusMinutes(30)
            }
        }
    }
}

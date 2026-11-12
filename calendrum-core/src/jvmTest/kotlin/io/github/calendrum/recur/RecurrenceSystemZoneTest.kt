package io.github.calendrum.recur

import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import java.util.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class RecurrenceSystemZoneTest {
    @Test
    fun aUtcYearlyRuleIgnoresTheDefaultTimeZone() {
        val saved = TimeZone.getDefault()
        val rule = Recur.builder(io.github.calendrum.value.Frequency.YEARLY).count(4).build()
        val start = ICalDateTime.Utc(Instant.parse("2026-12-31T23:30:00Z"))
        val expected = listOf(
            "2026-12-31T23:30:00Z",
            "2027-12-31T23:30:00Z",
            "2028-12-31T23:30:00Z",
            "2029-12-31T23:30:00Z",
        )
        try {
            for (zone in listOf("UTC", "Pacific/Kiritimati", "America/St_Johns", "Pacific/Pago_Pago", "Asia/Kolkata")) {
                TimeZone.setDefault(TimeZone.getTimeZone(zone))
                assertEquals(expected, rule.expand(start).map { it.toString() }.toList(), zone)
            }
        } finally {
            TimeZone.setDefault(saved)
        }
    }
}

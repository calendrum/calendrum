package io.github.calendrum.jcal

import io.github.calendrum.ICal
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Instant

class ICalSerializersTest {
    private fun <T> roundTrip(serializer: KSerializer<T>, value: T, json: String) {
        assertEquals(json, Json.encodeToString(serializer, value))
        assertEquals(value, Json.decodeFromString(serializer, json))
    }

    @Test
    fun dateTimesAreJcalStringsWithTheZoneInBrackets() {
        val serializer = ICalSerializers.dateTime
        roundTrip(serializer, ICalDateTime.Date(LocalDate(2026, 6, 1)), "\"2026-06-01\"")
        roundTrip(serializer, ICalDateTime.Floating(LocalDateTime(2026, 6, 1, 10, 0)), "\"2026-06-01T10:00:00\"")
        roundTrip(serializer, ICalDateTime.Utc(Instant.parse("2026-06-01T08:00:05Z")), "\"2026-06-01T08:00:05Z\"")
        roundTrip(
            serializer,
            ICalDateTime.Zoned(LocalDateTime(2026, 6, 1, 10, 0, 30), "Europe/Berlin"),
            "\"2026-06-01T10:00:30[Europe/Berlin]\"",
        )
        for (bad in listOf(
            "\"2026-6-1\"",
            "\"2026-06-01T10:00:00Z[Europe/Berlin]\"",
            "\"2026-06-01T10:00\"",
            "\"x[]\"",
        )) {
            assertFailsWith<SerializationException>(bad) { Json.decodeFromString(serializer, bad) }
        }
    }

    @Test
    fun durationsRulesAndPeriodsAreICalendarStrings() {
        roundTrip(ICalSerializers.duration, ICalDuration.ofMinutes(-90), "\"-PT90M\"")
        val rule = Recur.builder(io.github.calendrum.value.Frequency.WEEKLY).count(4).interval(2).build()
        roundTrip(ICalSerializers.recur, rule, "\"FREQ=WEEKLY;COUNT=4;INTERVAL=2\"")
        val start = ICalDateTime.Utc(Instant.parse("2026-06-01T08:00:00Z"))
        roundTrip(
            ICalSerializers.period,
            ICalPeriod(start, ICalDateTime.Utc(Instant.parse("2026-06-01T09:00:00Z")), null),
            "\"2026-06-01T08:00:00Z/2026-06-01T09:00:00Z\"",
        )
        roundTrip(
            ICalSerializers.period,
            ICalPeriod(start, null, ICalDuration.ofHours(1)),
            "\"2026-06-01T08:00:00Z/PT1H\"",
        )
        assertFailsWith<SerializationException> { Json.decodeFromString(ICalSerializers.duration, "\"soon\"") }
        assertFailsWith<SerializationException> { Json.decodeFromString(ICalSerializers.recur, "\"COUNT=4\"") }
        assertFailsWith<SerializationException> { Json.decodeFromString(ICalSerializers.period, "\"2026-06-01\"") }
        assertFailsWith<SerializationException> {
            Json.decodeFromString(ICalSerializers.period, "\"2026-06-01T08:00:00Z/soon\"")
        }
    }

    @Test
    fun componentsAreJcal() {
        val component = ICal.parse(
            "BEGIN:VEVENT\r\nUID:1\r\nDTSTART;VALUE=DATE:20260601\r\nEND:VEVENT\r\n",
        ).components.single()
        val json = """["vevent",[["uid",{},"text","1"],["dtstart",{},"date","2026-06-01"]],[]]"""
        roundTrip(ICalSerializers.component, component, json)
        roundTrip(ListSerializer(ICalSerializers.component), listOf(component, component), "[$json,$json]")
        assertFailsWith<SerializationException> { Json.decodeFromString(ICalSerializers.component, "[\"vevent\"]") }
        assertFailsWith<SerializationException> {
            Json.decodeFromString(ICalSerializers.component, """["vevent",[["uid",{},"text"]],[]]""")
        }
        assertEquals("ICalSerializers.component", ICalSerializers.component.toString())
        assertEquals("ICalSerializers", ICalSerializers.toString())
    }
}

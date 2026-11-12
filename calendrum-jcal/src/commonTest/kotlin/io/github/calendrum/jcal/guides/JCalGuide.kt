package io.github.calendrum.jcal.guides

import io.github.calendrum.ICal
import io.github.calendrum.jcal.ICalSerializers
import io.github.calendrum.jcal.JCal
import io.github.calendrum.jcal.JCalOptions
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.calendars
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JCalGuide {
    private val bytes = (
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Guides//EN\r\nBEGIN:VEVENT\r\nUID:1@example.com\r\n" +
            "DTSTART;TZID=Europe/Berlin:20260601T100000\r\nSUMMARY:Weekly sync\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        ).encodeToByteArray()

    @Test
    fun encodeAndDecode() {
        // region jcal-encode
        val result = ICal.parse(bytes)
        val json: String = JCal.encodeToString(result.components) // [["vcalendar", [...], [...]]]
        val element: JsonElement = JCal.encodeToJsonElement(result.components)
        val read = JCal.decodeFromString(json) // the same tree; malformed parts are skipped
        // ICAL-J001 not JSON … ICAL-J006 unknown type
        val problems = read.problems.map { it.code.id }
        // endregion
        assertTrue(json.startsWith("[[\"vcalendar\","))
        assertEquals(Json.parseToJsonElement(json), element)
        assertEquals(result.components, read.components)
        assertEquals(emptyList(), problems)
    }

    @Test
    fun problemsInsteadOfExceptions() {
        // region jcal-problems
        val read = JCal.decodeFromString("{\"not\": \"jCal\"}")
        val codes = read.problems.map { it.code.id } // a problem with a JSON path, not an exception
        // endregion
        assertTrue(read.components.isEmpty())
        assertTrue(codes.isNotEmpty() && codes.all { it.startsWith("ICAL-J") })
    }

    @Test
    fun registryOptions() {
        val components = ICal.parse(bytes).components
        // region jcal-options
        val registry = ICalRegistry.Default // or one with your own X- properties and their types
        val custom = JCal.encodeToString(components, JCalOptions.builder().registry(registry).build())
        // endregion
        assertEquals(JCal.encodeToString(components), custom)
    }

    @Test
    fun serializers() {
        val calendar = ICal.parse(bytes).calendars.single()
        val event = calendar.events.single()
        // region jcal-serializers
        // "2026-06-01T10:00:00[Europe/Berlin]"
        val start: String = Json.encodeToString(ICalSerializers.dateTime, event.start!!)
        val stored = Json.encodeToString(ListSerializer(ICalSerializers.component), calendar.component.components)
        // jCal of each component
        val events = Json.decodeFromString(ListSerializer(ICalSerializers.component), stored)
        // endregion
        assertEquals("\"2026-06-01T10:00:00[Europe/Berlin]\"", start)
        assertEquals(calendar.component.components, events)
    }
}

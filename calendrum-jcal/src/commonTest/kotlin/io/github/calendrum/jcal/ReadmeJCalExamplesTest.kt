package io.github.calendrum.jcal

import io.github.calendrum.ICal
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReadmeJCalExamplesTest {
    private val bytes = (
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nBEGIN:VEVENT\r\nUID:1\r\n" +
            "DTSTART;TZID=Europe/Berlin:20260601T100000\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        ).encodeToByteArray()
    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    @Test
    fun theJcalSnippetOfTheReadmeCompilesAndWorks() {
        val calendar = ICal.parse(bytes).calendars.single()
        val components: List<ICalComponent> = ICal.parse(bytes).components
        val registry = ICalRegistry.Default
        val event = calendar.events.single()
        val json: String = JCal.encodeToString(ICal.parse(bytes).components)
        val element: JsonElement = JCal.encodeToJsonElement(listOf(calendar.component))
        val read = JCal.decodeFromString(json)
        read.problems.forEach { println("${it.code.id} ${it.message}") }
        val custom = JCal.encodeToString(components, JCalOptions.builder().registry(registry).build())
        val start: String = Json.encodeToString(ICalSerializers.dateTime, event.start!!)
        val stored = Json.encodeToString(ListSerializer(ICalSerializers.component), calendar.component.components)
        val events = Json.decodeFromString(ListSerializer(ICalSerializers.component), stored)
        assertTrue(json.startsWith("[[\"vcalendar\","))
        assertEquals(Json.parseToJsonElement(json), element)
        assertEquals(components, read.components)
        assertEquals(emptyList(), printed)
        assertEquals(json, custom)
        assertEquals("\"2026-06-01T10:00:00[Europe/Berlin]\"", start)
        assertEquals(calendar.component.components, events)
    }
}

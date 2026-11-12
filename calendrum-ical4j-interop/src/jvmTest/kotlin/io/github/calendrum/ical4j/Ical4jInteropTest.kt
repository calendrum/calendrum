package io.github.calendrum.ical4j

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import net.fortuna.ical4j.data.CalendarBuilder
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Every VCALENDAR of the conformance inputs and the real corpus goes through ical4j and back; the known differences are
// the ways ical4j writes the same content. The real calendars (corpus/real/, cases named real-*) stay with the maintainer
// and may be absent; their values are not written here, so the only difference allowed for them is the order of the
// parts of an RRULE, which ical4j rewrites.
class Ical4jInteropTest {
    private val conformance = File(System.getProperty("calendrum.conformance") ?: "../conformance/ical")
    private val corpus = File(System.getProperty("calendrum.corpus") ?: "../corpus/real")

    private class Input(val name: String, val bytes: ByteArray, val private: Boolean)

    private fun inputs(): List<Input> {
        val cases = conformance.listFiles().orEmpty().filter { File(it, "input.ics").isFile }.sortedBy { it.name }
            .map { Input(it.name, File(it, "input.ics").readBytes(), it.name.startsWith("real-")) }
        val real = corpus.walkTopDown().filter { it.isFile && it.name.endsWith(".ics") }.sortedBy { it.path }
            .map { Input(it.name, it.readBytes(), private = true) }.toList()
        return cases + real
    }

    @Test
    fun calendarsGoThroughIcal4jAndBack() {
        val outcomes = LinkedHashMap<String, String>()
        val privateFailures = ArrayList<String>()
        var exact = 0
        var reordered = 0
        var total = 0
        for (input in inputs()) {
            for ((index, calendar) in ICal.parse(input.bytes).calendars.withIndex()) {
                total++
                val key = if (index == 0) input.name else "${input.name}#$index"
                val back = try {
                    calendar.toIcal4j().toCalendrum()
                } catch (e: Exception) {
                    if (input.private) {
                        privateFailures += "$key: ical4j rejects it"
                    } else {
                        outcomes[key] =
                            "ical4j rejects: ${e::class.simpleName}"
                    }
                    continue
                }
                when {
                    back.component == calendar.component -> exact++
                    input.private && sameButRuleOrder(calendar.component, back.component) -> reordered++
                    input.private -> privateFailures += "$key: differs beyond the order of RRULE parts"
                    else -> outcomes[key] = difference(calendar.component, back.component)
                }
            }
        }
        assertEquals(known, outcomes)
        assertEquals(emptyList(), privateFailures)
        // The public cases alone hold 19 calendars; the real ones, where present, add to them.
        assertTrue(total >= 19, "calendars $total")
        assertEquals(total - known.size - reordered, exact)
    }

    @Test
    fun anIcal4jCalendarComesBackFromCalendrum() {
        val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nBEGIN:VEVENT\r\nUID:1\r\n" +
            "DTSTAMP:20260901T080000Z\r\nDTSTART:20260901T090000Z\r\nSUMMARY:Planning\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val ical4j = CalendarBuilder().build(ByteArrayInputStream(text.encodeToByteArray()))
        val calendar = ical4j.toCalendrum()
        assertEquals("Planning", calendar.events.single().summary)
        assertEquals(ical4j, calendar.toIcal4j())
        assertEquals(ICal.parse(text).calendars.single().component, calendar.component)
    }

    private val known = mapOf(
        "fingerprints-keys-and-offsets" to "component VTIMEZONE -> VTODO",
        "rfc7265-example-2" to
            "DAYLIGHT: RRULE value FREQ=YEARLY;BYDAY=1SU;BYMONTH=4 -> FREQ=YEARLY;BYMONTH=4;BYDAY=1SU",
    )

    private fun sameButRuleOrder(expected: ICalComponent, actual: ICalComponent): Boolean {
        if (expected.name != actual.name || expected.properties.size != actual.properties.size) return false
        if (expected.components.size != actual.components.size) return false
        val properties = expected.properties.zip(actual.properties).all { (a, b) ->
            a == b || (
                a.name == "RRULE" && b.name == "RRULE" && a.parameters == b.parameters &&
                    a.value.split(';').toSet() == b.value.split(';').toSet()
                )
        }
        return properties && expected.components.zip(actual.components).all { (a, b) -> sameButRuleOrder(a, b) }
    }

    private fun difference(expected: ICalComponent, actual: ICalComponent): String {
        if (expected.name != actual.name) return "component ${expected.name} -> ${actual.name}"
        for ((index, property) in expected.properties.withIndex()) {
            val other = actual.properties.getOrNull(index) ?: return "${expected.name}: ${property.name} missing"
            if (property != other) {
                return if (property.name != other.name) {
                    "${expected.name}: ${property.name} -> ${other.name}"
                } else if (property.value != other.value) {
                    "${expected.name}: ${property.name} value ${property.value} -> ${other.value}"
                } else {
                    "${expected.name}: ${property.name} parameters ${property.parameters} -> ${other.parameters}"
                }
            }
        }
        if (actual.properties.size > expected.properties.size) {
            return "${expected.name}: extra ${actual.properties[expected.properties.size].name}"
        }
        for ((index, child) in expected.components.withIndex()) {
            val other = actual.components.getOrNull(index) ?: return "${child.name} missing"
            if (child != other) return difference(child, other)
        }
        return "${expected.name}: extra component"
    }
}

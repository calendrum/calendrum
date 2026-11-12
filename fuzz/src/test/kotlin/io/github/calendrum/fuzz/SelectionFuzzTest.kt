package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.Selection
import io.github.calendrum.syntax.TimeZoneSelection

// A selective parse against the full one: with events and three of their properties selected, the top level keeps the
// calendars and the events outside them, and every kept component is the one at the same place of the full tree with
// only those properties and, of its children, only the calendars and events: the lexer keeps a VCALENDAR at any depth,
// and a VEVENT inside a VEVENT is a selected component too. The first byte picks the three names among the usual ones,
// so that names the input does not have are selected too.
class SelectionFuzzTest {
    @FuzzTest
    fun selection(input: ByteArray) {
        if (input.isEmpty()) return
        val pick = input[0].toInt() and 0xFF
        val names = listOf(NAMES[pick % NAMES.size], NAMES[pick / 7 % NAMES.size], NAMES[pick / 49 % NAMES.size])
        val selection = Selection.builder().components("VEVENT").properties(*names.toTypedArray())
            .timeZones(TimeZoneSelection.None).build()
        val bytes = input.copyOfRange(1, input.size)
        val full = ICal.parse(bytes).components
        val selective = ICal.parser(ParseOptions.builder().apply { this.selection = selection }.build()).parse(bytes)
        val roots = full.filter { it.name in KEPT }
        invariant(selective.components.map { it.name } == roots.map { it.name }) { "the top-level components differ" }
        for ((index, root) in selective.components.withIndex()) check(roots[index], root, names, "${root.name} $index")
    }

    private fun check(expected: ICalComponent, actual: ICalComponent, names: List<String>, what: String) {
        val kept = expected.properties.filter { it.name in names }.map(::text)
        invariant(actual.properties.map(::text) == kept) { "$names: $what differs" }
        val children = expected.components.filter { it.name in KEPT }
        invariant(actual.components.map { it.name } == children.map { it.name }) {
            "$names: $what keeps ${actual.components.map { it.name }}, the full tree has ${children.map { it.name }}"
        }
        for ((position, child) in children.withIndex()) {
            check(child, actual.components[position], names, "$what/${child.name} $position")
        }
    }

    private fun text(property: io.github.calendrum.syntax.ICalProperty): String =
        property.name + property.parameters.joinToString("") { ";" + it.name + "=" + it.values } + ":" + property.value

    private companion object {
        val NAMES = listOf("UID", "DTSTART", "DTEND", "SUMMARY", "RRULE", "EXDATE", "X-A", "ATTENDEE", "LOCATION")
        val KEPT = setOf("VCALENDAR", "VEVENT")
    }
}

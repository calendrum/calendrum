package io.github.calendrum.conformance

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.jcal.JCal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.WriteOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Every case goes tree → jCal → tree: the second jCal equals the first, and the tree equals the parsed one except where
// jCal normalizes (escaping of TEXT, VALUE first among the parameters, VALUE written for a value read leniently in
// another type) — each such case is listed with its first difference. A tree that canonical writing rejects (a name
// that is not a token, a parameter without a value) is not written as jCal either, and reading reports no
// problems for what was written.
class JcalRoundTripTest {
    @Test
    fun everyCaseReadsBackFromJcal() {
        val failures = ArrayList<String>()
        var exact = 0
        var compared = 0
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject ?: continue
            val tree = try {
                ICal.parser(parseOptions(meta)).parse(input).components
            } catch (_: ICalParseException) {
                continue
            }
            val jcal = try {
                JCal.encodeToJsonElement(tree)
            } catch (_: IllegalArgumentException) {
                if (tree.all(::canonicallyWritable)) failures += "${case.directory}: jCal rejects a writable tree"
                continue
            }
            compared++
            val back = JCal.decodeFromString(jcal.toString())
            val problems = back.problems.map { it.code.id }.distinct()
            if (problems.isNotEmpty()) {
                failures += "${case.directory}: problems $problems"
                continue
            }
            val again = JCal.encodeToJsonElement(back.components)
            if (again != jcal) failures += "${case.directory}: jCal of the tree read back from jCal differs"
            if (back.components == tree) {
                exact++
                if (case.directory in normalized) failures += "${case.directory}: reads back exactly now"
            } else {
                val difference = firstDifference(tree, back.components)
                if (normalized[case.directory] != difference) failures += "${case.directory}: $difference"
            }
        }
        assertEquals(emptyList(), failures)
        assertTrue(compared > Suite.cases.size / 2, "compared $compared")
        assertTrue(exact > compared * 3 / 4, "exact $exact of $compared")
    }

    private fun canonicallyWritable(component: ICalComponent): Boolean = try {
        ICal.writer(WriteOptions.builder().byteStable(false).build()).encodeToString(component)
        true
    } catch (_: IllegalArgumentException) {
        false
    }

    private fun firstDifference(expected: List<ICalComponent>, actual: List<ICalComponent>): String {
        for ((index, component) in expected.withIndex()) {
            val other = actual.getOrNull(index) ?: return "component $index is missing"
            componentDifference(component, other)?.let { return it }
        }
        return "different component count"
    }

    private fun componentDifference(expected: ICalComponent, actual: ICalComponent): String? {
        if (expected.name != actual.name) return "component ${expected.name} became ${actual.name}"
        for ((index, property) in expected.properties.withIndex()) {
            val other = actual.properties.getOrNull(index) ?: return "${property.name} is missing"
            if (property != other) return propertyDifference(property, other)
        }
        for ((index, child) in expected.components.withIndex()) {
            val other = actual.components.getOrNull(index) ?: return "${child.name} is missing"
            componentDifference(child, other)?.let { return it }
        }
        return null
    }

    private fun propertyDifference(expected: ICalProperty, actual: ICalProperty): String = when {
        expected.name != actual.name -> "${expected.name} became ${actual.name}"

        expected.parameters.toSet() == actual.parameters.toSet() ->
            "${expected.name}: value ${expected.value} -> ${actual.value}"

        else -> "${expected.name}: parameters ${expected.parameters} -> ${actual.parameters}"
    }

    private val normalized = mapOf(
        "fingerprints-keys-and-offsets" to
            "SUMMARY: value A to-do without a UID, written in lower case and folded -> " +
            "A to-do without a UID\\, written in lower case and folded",
        "rfc5545-todo-example" to "TRIGGER: parameters [] -> [ICalParameter(name=VALUE, values=1)]",
        "rfc7265-example-1" to "DTSTART: parameters [] -> [ICalParameter(name=VALUE, values=1)]",
        "rfc7265-example-2" to "RDATE: value 20060102T150000/PT2H -> 20060102T150000/PT2H",
    )
}

@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.syntax.KnownNames
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegistrySpecTest {
    private val extensions = mapOf(
        "5545" to Rfc5545,
        "7986" to Rfc7986,
        "9073" to Rfc9073,
        "9074" to Rfc9074,
        "9253" to Rfc9253,
    )

    private fun component(column: String): String = if (column.startsWith("VALARM")) "VALARM" else column

    private fun cardinality(code: String): Cardinality = when (code) {
        SPEC_ONCE -> Cardinality.One
        SPEC_OPTIONAL, SPEC_OPTIONAL_SHOULD_ONCE -> Cardinality.Optional
        SPEC_REQUIRED_MANY -> Cardinality.OneOrMore
        else -> Cardinality.Many
    }

    private fun expected(row: SpecRow): Map<String, Cardinality> {
        val result = LinkedHashMap<String, String>()
        for (column in SpecTable.components.rows.flatMap { it.words("columns") }) {
            val code = row[column]
            if (code == SPEC_NOT_ALLOWED) continue
            val name = component(column)
            val previous = result[name]
            result[name] = if (previous == null || isMultiple(code)) code else previous
        }
        return result.mapValues { cardinality(it.value) }
    }

    @Test
    fun everyExtensionDefinesExactlyTheRowsOfItsRfc() {
        for ((rfc, extension) in extensions) {
            val registry = ICalRegistry.builder().install(extension).build()
            for (row in SpecTable.properties.rfc(rfc)) {
                for ((name, code) in expected(row)) {
                    val definition =
                        assertNotNull(registry.property(KnownNames.find(row.name), row.name, name), "$row $name")
                    assertEquals(row.name, definition.name, "$row")
                    assertEquals(code, definition.cardinality[name], "$row in $name")
                    assertEquals(ValueType.of(row["value"]), definition.defaultType, "$row default type")
                    assertEquals(row.words("types").map(ValueType::of).toSet(), definition.allowedTypes, "$row types")
                    assertEquals(row.words("parameters").toSet(), definition.parameters, "$row parameters")
                    assertEquals("RFC $rfc §${row["section"]}", definition.source, "$row source")
                }
            }
            for (row in SpecTable.parameters.rfc(rfc)) {
                val definition = assertNotNull(registry.parameter(row.name), "$row")
                assertTrue("RFC $rfc §${row["section"]}" in definition.source, "$row source")
            }
            for (row in SpecTable.components.rfc(rfc)) {
                val definition = assertNotNull(registry.component(row.name), "$row")
                assertTrue(definition.children.containsAll(row.words("children")), "$row children")
                assertTrue("RFC $rfc §${row["section"]}" in definition.source, "$row source")
            }
        }
    }

    @Test
    fun theDefaultRegistryMergesTheRowsOfAllRfcs() {
        val registry = ICalRegistry.Default
        for (row in SpecTable.properties.rows) {
            for ((name, code) in expected(row)) {
                val definition =
                    assertNotNull(registry.property(KnownNames.find(row.name), row.name, name), "$row $name")
                assertEquals(code, definition.cardinality[name], "$row in $name")
                assertTrue(definition.allowedTypes.containsAll(row.words("types").map(ValueType::of)), "$row types")
                assertTrue(definition.parameters.containsAll(row.words("parameters")), "$row parameters")
                assertTrue("RFC ${row["rfc"]} §${row["section"]}" in definition.source, "$row source")
            }
        }
        val children = SpecTable.components.rows.groupBy { it.name }.mapValues { (_, rows) ->
            rows.flatMap { it.words("children") }.toSet()
        }
        for ((name, expected) in children) assertEquals(expected, registry.component(name)?.children, name)
    }

    @Test
    fun theDefaultRegistryDefinesNothingBeyondTheTable() {
        val properties = SpecTable.properties.names().toSet()
        val parameters = SpecTable.parameters.names().toSet()
        val components = SpecTable.components.names().toSet()
        for (id in 0 until KnownNames.count) {
            val name = KnownNames.name(id)
            if (name in properties) assertNotNull(ICalRegistry.Default.property(name), name)
            if (name !in properties) assertNull(ICalRegistry.Default.property(name), name)
            if (name !in parameters) assertNull(ICalRegistry.Default.parameter(name), name)
            if (name !in components) assertNull(ICalRegistry.Default.component(name), name)
        }
    }
}

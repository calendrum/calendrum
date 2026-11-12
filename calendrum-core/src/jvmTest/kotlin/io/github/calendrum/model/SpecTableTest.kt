package io.github.calendrum.model

import io.github.calendrum.syntax.KnownNames
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpecTableTest {
    @Test
    fun propertiesAreTheRfc5545PropertiesInRfcOrder() {
        val rfc = (0 until KnownNames.count)
            .filter { KnownNames.kinds(it) and KnownNames.PROPERTY != 0 }
            .map { KnownNames.name(it) }
            .takeWhile { it != "EXRULE" }
            .filter { it != "BEGIN" && it != "END" }
        assertEquals(rfc, SpecTable.properties.rfc("5545").map { it.name })
        assertEquals(46, rfc.size)
    }

    @Test
    fun parametersAreTheRfc5545Parameters() {
        val rfc = (0 until KnownNames.count)
            .filter { KnownNames.kinds(it) and KnownNames.PARAMETER != 0 }
            .map { KnownNames.name(it) }
            .takeWhile { it != "DISPLAY" }
        assertEquals(rfc.sorted(), SpecTable.parameters.rfc("5545").map { it.name }.sorted())
        assertEquals(20, rfc.size)
    }

    @Test
    fun everyNameOfTheLaterRfcsIsAKnownName() {
        val later = SPEC_RFCS.drop(1)
        for ((table, kind) in listOf(
            SpecTable.properties to KnownNames.PROPERTY,
            SpecTable.parameters to KnownNames.PARAMETER,
            SpecTable.components to KnownNames.COMPONENT,
        )) {
            for (row in table.rows.filter { it["rfc"] in later }) {
                val id = KnownNames.find(row.name)
                assertTrue(id >= 0 && KnownNames.kinds(id) and kind != 0, "$row is a known name")
            }
        }
    }

    @Test
    fun rowsOfOneNameComeFromDifferentRfcsOrSections() {
        for (table in listOf(SpecTable.properties, SpecTable.parameters, SpecTable.components)) {
            val keys = table.rows.map { Triple(it.name, it["rfc"], it["section"]) }
            assertEquals(keys.distinct(), keys, table.toString())
            for (row in table.rows) assertTrue(row["rfc"] in SPEC_RFCS, "$row rfc")
        }
    }

    @Test
    fun componentsAreTheRfc5545Components() {
        val rfc = (0 until KnownNames.count)
            .filter { KnownNames.kinds(it) and KnownNames.COMPONENT != 0 }
            .map { KnownNames.name(it) }
            .takeWhile { it != "VAVAILABILITY" }
        assertEquals(rfc, SpecTable.components.rfc("5545").map { it.name })
    }

    @Test
    fun everyComponentColumnIsInThePropertyTable() {
        val columns = SpecTable.components.rows.flatMap { it.words("columns") }
        val header = SpecTable.properties.header
        assertEquals(columns, header.subList(header.indexOf("VCALENDAR"), header.indexOf("rfc")))
    }

    @Test
    fun propertyCellsUseKnownCodesAndTypes() {
        val parameters = SpecTable.parameters.names().toSet()
        for (row in SpecTable.properties.rows) {
            assertTrue(row["value"] in SPEC_VALUE_TYPES, "$row value")
            assertTrue(row["value"] in row.words("types"), "$row default type is allowed")
            assertTrue(row.words("types").all { it in SPEC_VALUE_TYPES }, "$row types")
            assertTrue(row["shape"] in setOf("single", "list", "structured"), "$row shape")
            assertTrue(row.words("parameters").all { it in parameters }, "$row parameters")
            assertTrue(row.words("types").size == 1 || "VALUE" in row.words("parameters"), "$row VALUE parameter")
            for (column in SpecTable.components.rows.flatMap { it.words("columns") }) {
                assertTrue(row[column] in SPEC_CARDINALITIES, "$row $column")
            }
            if (row["rfc"] == "5545") {
                assertTrue(row["section"].startsWith("3.7.") || row["section"].startsWith("3.8."), "$row section")
            } else {
                assertTrue(row["section"].isNotEmpty(), "$row section")
            }
        }
    }

    @Test
    fun everyPropertyBelongsToSomeComponent() {
        val columns = SpecTable.components.rows.flatMap { it.words("columns") }
        for (row in SpecTable.properties.rows) {
            assertTrue(columns.any { row[it] != SPEC_NOT_ALLOWED }, "$row is allowed nowhere")
        }
    }

    @Test
    fun parameterCellsAreConsistent() {
        for (row in SpecTable.parameters.rows) {
            val type = row["value"]
            assertTrue(type == "ENUM" || type in SPEC_VALUE_TYPES, "$row value")
            assertEquals(type == "ENUM", row.words("values").isNotEmpty(), "$row values")
            assertTrue(row["shape"] in setOf("single", "list"), "$row shape")
            assertTrue(row["quoted"] in setOf("", "yes"), "$row quoted")
            assertTrue(row["extensible"] in setOf("", "yes"), "$row extensible")
            val default = row["default"]
            assertTrue(default.isEmpty() || default in row.words("values") || type == "BOOLEAN", "$row default")
            assertTrue(row["rfc"] != "5545" || row["section"].startsWith("3.2."), "$row section")
        }
    }

    @Test
    fun componentTreeIsConsistent() {
        val names = SpecTable.components.names().toSet()
        fun all(name: String, column: String) = SpecTable.components.rows(name).flatMap { it.words(column) }
        for (row in SpecTable.components.rows) {
            for (parent in row.words("parents")) {
                assertTrue(parent in names && row.name in all(parent, "children"), "$row")
            }
            for (child in row.words("children")) {
                assertTrue(row.name in all(child, "parents"), "$row child $child")
            }
        }
    }

    @Test
    fun constraintsNameKnownThings() {
        val components = SpecTable.components.names().toSet()
        val properties = SpecTable.properties.names().toSet()
        val rules =
            setOf(
                "has-component",
                "exclusive",
                "required-without-method",
                "requires",
                "has-child",
                "action",
                "together",
                "same-type",
                "after",
                "date-duration",
                "until-type",
                "values",
                "range",
                "local-time",
            )
        for (row in SpecTable.constraints.rows) {
            assertTrue(row.name in components, "$row component")
            assertTrue(row["rule"] in rules, "$row rule")
            val names = row.words("names")
            when (row["rule"]) {
                "has-component" -> assertTrue(names.isEmpty(), "$row")

                "has-child" -> assertTrue(names.all { it in components }, "$row")

                "required-without-method" -> assertTrue(names.size == 1 && names[0] in properties, "$row")

                "action" -> assertEquals(componentColumns(row.name), names.map { "${row.name}-$it" }, "$row")

                "values" -> assertTrue(names.size >= 2 && names[0] in properties, "$row")

                "range" -> assertTrue(
                    names.size == 3 && names[0] in properties && names[1].toInt() < names[2].toInt(),
                    "$row",
                )

                "local-time" -> assertTrue(names.size == 1 && names[0] in properties, "$row")

                else -> assertTrue(names.size >= 2 && names.all { it in properties }, "$row")
            }
        }
    }

    @Test
    fun cardinalitiesFollowTheComponentGrammars() {
        assertEquals(
            mapOf(
                "CALSCALE" to "?", "METHOD" to "?", "PRODID" to "1", "VERSION" to "1", "NAME" to "*",
                "DESCRIPTION" to "*", "UID" to "?", "LAST-MODIFIED" to "?", "URL" to "?", "CATEGORIES" to "*",
                "REFRESH-INTERVAL" to "?", "SOURCE" to "?", "COLOR" to "?", "IMAGE" to "*",
            ).toSortedMap(),
            cardinalities("VCALENDAR").toSortedMap(),
        )
        val event = cardinalities("VEVENT")
        assertEquals("1", event["UID"])
        assertEquals("1", event["DTSTAMP"])
        assertEquals("?s", event["RRULE"])
        assertEquals("*", event["ATTENDEE"])
        assertEquals(null, event["DUE"])
        val alarm = cardinalities("VALARM")
        assertEquals("+", alarm["ATTENDEE"])
        assertEquals("*", alarm["ATTACH"])
        assertEquals("1", alarm["DESCRIPTION"])
        assertEquals(
            listOf("TZID", "TZURL", "LAST-MODIFIED", "CONCEPT", "LINK", "REFID", "RELATED-TO").sorted(),
            cardinalities("VTIMEZONE").keys.sorted(),
        )
        assertEquals("1", cardinalities("PARTICIPANT")["PARTICIPANT-TYPE"])
        assertEquals("1", cardinalities("VLOCATION")["UID"])
        assertEquals("?", alarm["ACKNOWLEDGED"])
        assertEquals(cardinalities("STANDARD"), cardinalities("DAYLIGHT"))
    }
}

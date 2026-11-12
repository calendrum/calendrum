package io.github.calendrum.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class PropertyKeysSpecTest {
    private val samples = mapOf(
        "BINARY" to "QUJD",
        "CAL-ADDRESS" to "mailto:a@example.com",
        "DATE" to "20260901",
        "DATE-TIME" to "20260901T100000Z",
        "DURATION" to "PT1H",
        "FLOAT" to "1.5;2.5",
        "INTEGER" to "1",
        "PERIOD" to "20260901T100000Z/PT1H",
        "RECUR" to "FREQ=DAILY",
        "TEXT" to "X",
        "URI" to "http://example.com",
        "UTC-OFFSET" to "+0100",
        "UID" to "uid-1",
        "XML-REFERENCE" to "http://example.com/a.xml",
    )

    private val declaredValue = setOf("REFRESH-INTERVAL")

    @Test
    fun everyPropertyOfTheTableHasAKeyInEveryComponentThatAllowsIt() {
        val columns = SpecTable.components.rows.flatMap { component ->
            component.words("columns").map {
                component.name to
                    it
            }
        }
        for (row in SpecTable.properties.rows) {
            for ((component, column) in columns) {
                if (row[column] == SPEC_NOT_ALLOWED) continue
                val key = assertNotNull(standardKey(row.name, component), "$row in $component")
                assertEquals(row.name, key.name, "$row in $component")
            }
        }
    }

    @Test
    fun everyKeyDecodesEveryValueTypeOfTheTable() {
        for (row in SpecTable.properties.rows) {
            val component = SpecTable.components.rows.first { c ->
                c.words("columns").any { row[it] != SPEC_NOT_ALLOWED }
            }
            val key = assertNotNull(standardKey(row.name, component.name))
            for (type in row.words("types")) {
                val text = when {
                    row.name == "REQUEST-STATUS" -> "2.0;Success"
                    type == "BINARY" -> "QUJD"
                    else -> samples.getValue(type)
                }
                val parameters = when {
                    type == row["value"] && row.name in declaredValue -> ";VALUE=$type"
                    type == row["value"] -> ""
                    type == "BINARY" -> ";VALUE=BINARY;ENCODING=BASE64"
                    else -> ";VALUE=$type"
                }
                val line = "${row.name}$parameters:$text"
                val decoded = key.codec.decode(propertyOf(line, component.name), DecodeContext.Strict)
                val value = assertIs<Decoded.Value<*>>(decoded, "$line: ${decoded.codes()}")
                assertEquals(emptyList(), value.problems.map(::describe), line)
                @Suppress("UNCHECKED_CAST")
                val encoded = (key as PropertyKey<Any>).codec.encode(value.value, EncodeContext(key.name))
                assertEquals(propertyOf(line, component.name), encoded, line)
            }
        }
    }

    @Test
    fun everyParameterOfTheTableHasAKey() {
        val keys = listOf(
            Parameters.ALTREP, Parameters.CN, Parameters.CUTYPE, Parameters.DELEGATED_FROM, Parameters.DELEGATED_TO,
            Parameters.DIR, Parameters.ENCODING, Parameters.FMTTYPE, Parameters.FBTYPE, Parameters.LANGUAGE,
            Parameters.MEMBER, Parameters.PARTSTAT, Parameters.RANGE, Parameters.RELATED, Parameters.RELTYPE,
            Parameters.ROLE, Parameters.RSVP, Parameters.SENT_BY, Parameters.TZID, Parameters.VALUE,
            Parameters.DISPLAY, Parameters.EMAIL, Parameters.FEATURE, Parameters.LABEL, Parameters.ORDER,
            Parameters.SCHEMA, Parameters.DERIVED, Parameters.LINKREL, Parameters.GAP,
        )
        assertEquals(SpecTable.parameters.names(), keys.map { it.name })
        for (row in SpecTable.parameters.rows) {
            val key = keys.first { it.name == row.name }
            assertEquals(key, ICalRegistry.Default.parameter(row.name)?.key, row.toString())
            val sample = row.words("values").firstOrNull() ?: when (row["value"]) {
                "BOOLEAN" -> "TRUE"
                "INTEGER" -> "1"
                "DURATION" -> "-PT1H"
                else -> "mailto:x"
            }
            val property = propertyOf("X-TEST;${row.name}=\"$sample\":x")
            val decoded = key.codec.decode(property, property.parameters.single(), DecodeContext.Strict)
            assertIs<Decoded.Value<*>>(decoded, row.toString())
        }
    }

    @Test
    fun enumerationsHaveTheValuesOfTheTable() {
        val families: Map<String, (String) -> Any> = mapOf(
            "CUTYPE" to CuType::of,
            "ENCODING" to Encoding::of,
            "FBTYPE" to FbType::of,
            "PARTSTAT" to PartStat::of,
            "RANGE" to RecurrenceRange::of,
            "RELATED" to Related::of,
            "RELTYPE" to RelType::of,
            "ROLE" to Role::of,
            "VALUE" to ValueType::of,
            "DISPLAY" to ImageDisplay::of,
            "FEATURE" to ConferenceFeature::of,
        )
        for (row in SpecTable.parameters.rows.filter { it["value"] == "ENUM" }) {
            val of = families.getValue(row.name)
            for (value in row.words("values")) assertEquals(value, of(value).toString(), row.toString())
        }
    }
}

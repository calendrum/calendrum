package io.github.calendrum.tooling.ops

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OpsCasesTest {
    @Test
    fun everyCaseGivesTheExpectedResponse() {
        val cases = allCases()
        assertTrue(cases.size >= Ops.operations.size * 3, "${cases.size} cases")
        for (case in cases) {
            val expected = assertNotNull(case.expected, case.id)
            val actual = Ops.call(case.operation, case.request, FIXED_ENVIRONMENT)
            assertEquals(
                comparable(Json.parseToJsonElement(expected), case.ignored),
                comparable(Json.parseToJsonElement(actual), case.ignored),
                case.id,
            )
        }
    }

    @Test
    fun aBuiltEventIsValid() {
        var built = 0
        for (case in allCases().filter { it.operation == "build_event" }) {
            val response = Json.parseToJsonElement(Ops.call(case.operation, case.request, FIXED_ENVIRONMENT)).jsonObject
            val result = response["result"]?.jsonObject ?: continue
            val ics = result.getValue("ics")
            val validated = Ops.call("validate", """{"ics":$ics}""", FIXED_ENVIRONMENT)
            val report = Json.parseToJsonElement(validated).jsonObject.getValue("result").jsonObject
            assertEquals(true, report.getValue("valid").jsonPrimitive.boolean, "${case.id}: $validated")
            built++
        }
        assertTrue(built >= 3)
    }

    @Test
    fun jcalComesBackAsTheSameCalendar() {
        val ics =
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\nBEGIN:VEVENT\r\nUID:a\r\nDTSTAMP:20260101T000000Z\r\n" +
                "DTSTART;TZID=Europe/Berlin:20260105T100000\r\nSUMMARY:a\\, b\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val there = Json.parseToJsonElement(
            Ops.call("convert", """{"ics":${JsonPrimitive(ics)}}""", FIXED_ENVIRONMENT),
        ).jsonObject
        val jcal = there.getValue("result").jsonObject.getValue("jcal")
        val back = Json.parseToJsonElement(Ops.call("convert", """{"jcal":$jcal}""", FIXED_ENVIRONMENT)).jsonObject
        assertEquals(ics, back.getValue("result").jsonObject.getValue("ics").jsonPrimitive.content)
    }

    @Test
    fun anAliasGivesTheIanaZone() {
        for ((name, id) in listOf("W. Europe Standard Time" to "Europe/Berlin", "Europe/Berlin" to "Europe/Berlin")) {
            val response = Ops.call("tz_resolve", """{"name":"$name","at":"2026-01-01T00:00:00Z"}""", FIXED_ENVIRONMENT)
            val result = Json.parseToJsonElement(response).jsonObject.getValue("result").jsonObject
            assertEquals(id, result.getValue("id").jsonPrimitive.content)
        }
    }

    @Test
    fun everyOperationHasThreeCases() {
        val counts = allCases().groupingBy { it.operation }.eachCount()
        for (operation in Ops.operations) {
            assertTrue((counts[operation] ?: 0) >= 3, "$operation has ${counts[operation]} cases")
        }
    }

    @Test
    fun theNamesOfRepairsInAResponseAreTheOnesOfARequest() {
        val ics = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN\r\n" +
            "X-WR-TIMEZONE:Europe/Berlin\r\nX-WR-CALNAME:Team\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n" +
            "DTSTAMP:20260101T000000Z\r\nDTSTART;TZID=W. Europe Standard Time:20260105T100000\r\n" +
            "DTEND:20260105T110000\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val requestable = setOf(
            "by_prod_id", "windows_tzids", "x_wr_timezone", "x_wr_calname", "outlook", "lotus_notes", "google",
            "vcalendar10", "lint",
        )
        var seen = 0
        for (names in listOf(
            "windows_tzids",
            "x_wr_timezone",
            "x_wr_calname",
            "outlook",
            "lotus_notes",
            "google",
            "lint",
            null,
        )) {
            val fixups = if (names == null) "" else ""","fixups":["$names"]"""
            val response = Json.parseToJsonElement(
                Ops.call("fix", """{"ics":${JsonPrimitive(ics)}$fixups}""", FIXED_ENVIRONMENT),
            )
            val changes = response.jsonObject.getValue("result").jsonObject.getValue("changes").jsonArray
            for (change in changes) {
                val name = change.jsonObject.getValue("fixup").jsonPrimitive.content
                assertTrue("-" !in name, name)
                assertTrue(name in requestable, name)
                seen++
            }
        }
        assertTrue(seen >= 4, "$seen changes")
    }

    @Test
    fun anInputWithNoCalendarIsNotValid() {
        for (text in listOf("", "plain text\n", "X-A:1\r\n")) {
            for (rules in listOf("lint", "rfc5545")) {
                val response = Ops.call(
                    "validate",
                    """{"ics":${JsonPrimitive(text)},"rules":"$rules"}""",
                    FIXED_ENVIRONMENT,
                )
                val result = Json.parseToJsonElement(response).jsonObject.getValue("result").jsonObject
                assertEquals(0, result.getValue("calendar_count").jsonPrimitive.content.toInt(), response)
                assertEquals(false, result.getValue("valid").jsonPrimitive.boolean, response)
            }
        }
    }
}

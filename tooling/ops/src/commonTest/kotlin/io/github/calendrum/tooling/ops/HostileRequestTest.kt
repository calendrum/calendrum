package io.github.calendrum.tooling.ops

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HostileRequestTest {
    private fun call(operation: String, request: String, environment: OpsEnvironment = FIXED_ENVIRONMENT) =
        Json.parseToJsonElement(Ops.call(operation, request, environment)).jsonObject

    private fun errorCode(operation: String, request: String, environment: OpsEnvironment = FIXED_ENVIRONMENT): String =
        call(operation, request, environment).getValue("error").jsonObject.getValue("code").jsonPrimitive.content

    private val event =
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\nBEGIN:VEVENT\r\nUID:a\r\nDTSTAMP:20260101T000000Z\r\n" +
            "DTSTART:20260105T100000Z\r\nDURATION:PT1H\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

    @Test
    fun aRequestThatIsNotAnObjectIsRefused() {
        for (request in listOf("", "not json", "{", "[]", "1", "null", "\"text\"", "{\"ics\":")) {
            assertEquals(BAD_ARGUMENT, errorCode("inspect", request), request)
        }
    }

    @Test
    fun fieldsOfTheWrongTypeAreRefused() {
        val ics = JsonPrimitive(event).toString()
        val requests = listOf(
            "inspect" to """{"ics":1}""",
            "inspect" to """{"ics":$ics,"limit":"1"}""",
            "inspect" to """{"ics":$ics,"limit":1.5}""",
            "inspect" to """{"ics":$ics,"include_jcal":"yes"}""",
            "inspect" to """{"ics":$ics,"ics_base64":"AA=="}""",
            "fix" to """{"ics":$ics,"fixups":"lint"}""",
            "fix" to """{"ics":$ics,"fixups":[1]}""",
            "expand" to """{"ics":$ics,"from":1,"to":2}""",
            "build_event" to """{"summary":"x","start":"2026-01-01T10:00:00Z","attendees":"a"}""",
            "build_event" to """{"summary":"x","start":"2026-01-01T10:00:00Z","attendees":[{}]}""",
            "convert" to """{"jcal":{}}""",
            "anonymize" to """{"ics":$ics,"shift_weeks":100000}""",
            "anonymize" to """{"ics":$ics,"shift_weeks":true}""",
        )
        for ((operation, request) in requests) {
            assertEquals(BAD_ARGUMENT, errorCode(operation, request), "$operation $request")
        }
    }

    @Test
    fun deepNestingIsRefusedBeforeTheParser() {
        val deep = "{\"a\":" + "[".repeat(1_000_000)
        assertEquals(BAD_ARGUMENT, errorCode("inspect", deep))
        val jcal = """{"jcal":${"[".repeat(200)}${"]".repeat(200)}}"""
        assertEquals(BAD_ARGUMENT, errorCode("convert", jcal))
        // A quote or a bracket inside a string is not structure.
        val text = """{"code":"${"[".repeat(1000)}"}"""
        assertEquals(UNKNOWN_CODE, errorCode("explain", text))
    }

    @Test
    fun jcalThatIsNotACalendarGivesProblems() {
        val response = call("convert", """{"jcal":[1,2,3]}""")
        val result = response.getValue("result").jsonObject
        assertEquals("", result.getValue("ics").jsonPrimitive.content)
        assertEquals(
            "ICAL-J002",
            result.getValue("problems").jsonArray[0].jsonObject.getValue("code").jsonPrimitive.content,
        )
    }

    @Test
    fun aLimitOutOfRangeIsRefused() {
        val ics = JsonPrimitive(event).toString()
        for (limit in listOf("0", "-1", "100001", "99999999999")) {
            assertEquals(BAD_ARGUMENT, errorCode("inspect", """{"ics":$ics,"limit":$limit}"""), limit)
        }
    }

    @Test
    fun aWindowOutOfRangeIsRefused() {
        val ics = JsonPrimitive(event).toString()
        val windows = listOf(
            """"from":"2027-01-01T00:00:00Z","to":"2026-01-01T00:00:00Z"""",
            """"from":"2026-01-01T00:00:00Z","to":"2026-01-01T00:00:00Z"""",
            """"from":"-100000-01-01T00:00:00Z","to":"2026-01-01T00:00:00Z"""",
            """"from":"2026-01-01T00:00:00Z","to":"+100000-01-01T00:00:00Z"""",
        )
        for (window in windows) assertEquals(BAD_ARGUMENT, errorCode("expand", """{"ics":$ics,$window}"""), window)
    }

    @Test
    fun aHugeWindowStopsAtTheLimit() {
        val ics = JsonPrimitive(rule("FREQ=SECONDLY")).toString()
        val response =
            call("expand", """{"ics":$ics,"from":"0001-01-01T00:00:00Z","to":"9999-01-01T00:00:00Z","limit":100}""")
        assertEquals(true, response.getValue("truncated").jsonPrimitive.content.toBoolean())
        assertEquals(100, response.getValue("result").jsonObject.getValue("count").jsonPrimitive.int)
    }

    @Test
    fun aRuleThatProducesNothingEnds() {
        val ics = JsonPrimitive(rule("FREQ=YEARLY;BYMONTH=2;BYMONTHDAY=30")).toString()
        val response = call("expand", """{"ics":$ics,"from":"2026-01-01T00:00:00Z","to":"9999-01-01T00:00:00Z"}""")
        // DTSTART itself, whatever the rule says.
        assertEquals(1, response.getValue("result").jsonObject.getValue("count").jsonPrimitive.int)
    }

    @Test
    fun manyLongSeriesBeforeAFarWindowHaveABound() {
        val text = StringBuilder("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\n")
        repeat(2000) { index ->
            text.append("BEGIN:VEVENT\r\nUID:$index\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20000101T000000Z\r\n")
            text.append("RRULE:FREQ=SECONDLY;COUNT=100000000\r\nEND:VEVENT\r\n")
        }
        text.append("END:VCALENDAR\r\n")
        val ics = JsonPrimitive(text.toString()).toString()
        val response = call("expand", """{"ics":$ics,"from":"9000-01-01T00:00:00Z","to":"9001-01-01T00:00:00Z"}""")
        assertEquals(true, response.getValue("truncated").jsonPrimitive.content.toBoolean(), "The limit cut the series")
        assertEquals(0, response.getValue("result").jsonObject.getValue("count").jsonPrimitive.int)
    }

    @Test
    fun overlappingOccurrencesDoNotGrowTheConflictsWithoutBound() {
        val text = StringBuilder("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\n")
        repeat(3000) { index ->
            text.append("BEGIN:VEVENT\r\nUID:$index\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\n")
            text.append("DURATION:PT1H\r\nEND:VEVENT\r\n")
        }
        text.append("END:VCALENDAR\r\n")
        val ics = JsonPrimitive(text.toString()).toString()
        val response = call("freebusy", """{"ics":$ics,"from":"2026-01-01T00:00:00Z","to":"2026-02-01T00:00:00Z"}""")
        assertEquals(true, response.getValue("truncated").jsonPrimitive.content.toBoolean())
        val result = response.getValue("result").jsonObject
        assertEquals(0, result.getValue("conflicts").jsonArray.size)
        assertEquals(1, result.getValue("busy").jsonArray.size)
    }

    @Test
    fun limitsOfTheEnvironmentHold() {
        val tiny = OpsLimits(
            maxRequestChars = 2000,
            maxInputBytes = 100,
            maxDepth = 4,
            defaultItems = 2,
            maxItems = 3,
            maxResponseBytes = 2000,
            maxProblems = 1,
            maxExpansionWork = 1000,
            maxConflictOccurrences = 1,
        )
        val environment = OpsEnvironment(FIXED_ENVIRONMENT.clock, FIXED_ENVIRONMENT.randomBytes, tiny)
        val ics = JsonPrimitive(event).toString()
        assertEquals(TOO_LARGE, errorCode("inspect", """{"ics":"${"x".repeat(3000)}"}""", environment))
        assertEquals(TOO_LARGE, errorCode("inspect", """{"ics":$ics}""", environment))
        val small = JsonPrimitive("BEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n").toString()
        assertEquals(BAD_ARGUMENT, errorCode("inspect", """{"ics":$small,"limit":4}""", environment))
        // A response above the limit that cannot be cut, unlike a list.
        val wide = OpsEnvironment(
            FIXED_ENVIRONMENT.clock,
            FIXED_ENVIRONMENT.randomBytes,
            OpsLimits(
                tiny.maxRequestChars * 1000, tiny.maxInputBytes * 1000, 8, 2, 3, 100, 1, 1000, 1,
            ),
        )
        assertEquals(TOO_LARGE, errorCode("fix", """{"ics":$ics}""", wide))
    }

    @Test
    fun aLineOverTheLimitIsReportedAsTruncated() {
        val long = "DESCRIPTION:" + "x".repeat(70_000)
        val ics = JsonPrimitive(event.replace("END:VEVENT", "$long\r\nEND:VEVENT")).toString()
        for ((operation, extra) in listOf("fix" to "", "convert" to "", "inspect" to "")) {
            val response = call(operation, """{"ics":$ics$extra}""")
            assertEquals(true, response.getValue("truncated").jsonPrimitive.content.toBoolean(), operation)
        }
    }

    @Test
    fun anUnknownOperationIsAnError() {
        assertEquals(UNKNOWN_OPERATION, errorCode("nothing", "{}"))
    }

    private fun rule(rule: String) = event.replace("DURATION:PT1H", "RRULE:$rule")

    private fun withLongLine(tail: String): String =
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n" +
            "DTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\n" +
            "DESCRIPTION:${"x".repeat(70_000)}$tail\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

    private fun JsonObject.isTruncated(): Boolean = getValue("truncated").jsonPrimitive.content == "true"

    private fun JsonObject.result(): JsonObject = getValue("result").jsonObject

    private fun JsonObject.problemCodes(): List<String> =
        result().getValue("problems").jsonArray.map { it.jsonObject.getValue("code").jsonPrimitive.content }

    @Test
    fun aChangeBeyondTheLimitsIsNotSilent() {
        val before = Json.encodeToString(withLongLine("A"))
        val after = Json.encodeToString(withLongLine("B"))
        val response = call("diff", """{"before":$before,"after":$after}""")
        assertTrue(response.isTruncated(), "no fingerprint sees that line, so the answer cannot be complete")
        assertTrue("ICAL-S010" in response.problemCodes())
    }

    @Test
    fun whatAnonymizeCouldNotReadIsNotSilent() {
        val response = call("anonymize", """{"ics":${Json.encodeToString(withLongLine("A"))}}""")
        assertTrue(response.isTruncated())
        assertTrue("ICAL-S010" in response.problemCodes())
        assertFalse("x".repeat(100) in response.result().getValue("ics").jsonPrimitive.content)
    }

    @Test
    fun aLineThatCannotBeReadMakesACalendarInvalid() {
        val response = call("validate", """{"ics":${Json.encodeToString(withLongLine("A"))}}""")
        assertEquals("false", response.result().getValue("valid").jsonPrimitive.content)
    }

    @Test
    fun freeBusyKeepsItsBudgetOnAFileOfManySeries() {
        val events = StringBuilder("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\n")
        repeat(6000) { index ->
            events.append("BEGIN:VEVENT\r\nUID:$index@example.com\r\nDTSTAMP:20260101T000000Z\r\n")
            events.append(
                "DTSTART:20260101T000000Z\r\nDURATION:PT1S\r\nRRULE:FREQ=SECONDLY;COUNT=2000\r\nEND:VEVENT\r\n",
            )
        }
        events.append("END:VCALENDAR\r\n")
        val limits = OpsLimits.Default.let {
            OpsLimits(
                it.maxRequestChars,
                it.maxInputBytes,
                it.maxDepth,
                it.defaultItems,
                it.maxItems,
                it.maxResponseBytes,
                it.maxProblems,
                60_000,
                it.maxConflictOccurrences,
            )
        }
        val environment = OpsEnvironment(FIXED_ENVIRONMENT.clock, FIXED_ENVIRONMENT.randomBytes, limits)
        val request = """{"ics":${Json.encodeToString(
            events.toString(),
        )},"from":"2026-01-01T00:00:00Z","to":"2027-01-01T00:00:00Z"}"""
        val response = call("freebusy", request, environment)
        assertTrue(response.isTruncated(), "60,000 dates among 6000 series: 10 each, and not the floor of 1000")
    }
}

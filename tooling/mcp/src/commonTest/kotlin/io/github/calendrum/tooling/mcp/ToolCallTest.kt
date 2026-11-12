package io.github.calendrum.tooling.mcp

import io.github.calendrum.tooling.platform.FileRead
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ToolCallTest {
    private val inspect = assertNotNull(ToolCatalog.spec("ics_inspect"))
    private val diff = assertNotNull(ToolCatalog.spec("ics_diff"))
    private val convert = assertNotNull(ToolCatalog.spec("ics_convert"))
    private val explain = assertNotNull(ToolCatalog.spec("ics_explain"))
    private val noFiles: (String) -> FileRead = { error("No file is expected") }

    private fun arguments(vararg pairs: Pair<String, String>) =
        JsonObject(pairs.associate { (name, value) -> name to JsonPrimitive(value) })

    private fun request(spec: ToolSpec, arguments: JsonObject, read: (String) -> FileRead = noFiles): JsonObject =
        Json.parseToJsonElement(requestOf(spec, arguments, read)).jsonObject

    @Test
    fun contentBecomesTheFieldOfTheContract() {
        val request = request(inspect, JsonObject(arguments("content" to "x") + ("limit" to JsonPrimitive(5))))
        assertEquals("x", request.getValue("ics").jsonPrimitive.content)
        assertEquals(setOf("ics", "limit"), request.keys)
    }

    @Test
    fun aFileGoesAsBase64() {
        val request = request(inspect, arguments("path" to "/a.ics")) { path ->
            assertEquals("/a.ics", path)
            FileRead.Bytes(byteArrayOf(1, 2, 3))
        }
        assertEquals("AQID", request.getValue("ics_base64").jsonPrimitive.content)
        assertEquals(setOf("ics_base64"), request.keys)
    }

    @Test
    fun bothCalendarsOfADiffCanBeFiles() {
        val request =
            request(diff, arguments("before_path" to "/a.ics", "after" to "text")) { FileRead.Bytes(byteArrayOf(1)) }
        assertEquals(setOf("before_base64", "after"), request.keys)
    }

    @Test
    fun oneCalendarHasOneSource() {
        val both =
            assertFailsWith<ToolFailure> {
                requestOf(inspect, arguments("content" to "x", "path" to "/a.ics"), noFiles)
            }
        assertEquals("OPS-BAD-ARGUMENT", both.code)
        val none = assertFailsWith<ToolFailure> { requestOf(inspect, JsonObject(emptyMap()), noFiles) }
        assertEquals("OPS-BAD-ARGUMENT", none.code)
        val other = assertFailsWith<ToolFailure> { requestOf(diff, arguments("before" to "x"), noFiles) }
        assertEquals("OPS-BAD-ARGUMENT", other.code)
    }

    @Test
    fun theNamesOfTheOperationDoNotGetAroundTheChecks() {
        val failure = assertFailsWith<ToolFailure> { requestOf(inspect, arguments("ics" to "x"), noFiles) }
        assertEquals("OPS-BAD-ARGUMENT", failure.code)
        val request = request(inspect, arguments("ics_base64" to "AA==", "content" to "x"))
        assertEquals(setOf("ics"), request.keys)
    }

    @Test
    fun nullIsAnAbsentField() {
        val arguments = JsonObject(arguments("content" to "x") + ("path" to JsonNull))
        assertEquals(setOf("ics"), request(inspect, arguments).keys)
    }

    @Test
    fun aFieldOfTheWrongTypeIsAnError() {
        val arguments = JsonObject(mapOf("path" to JsonPrimitive(1)))
        val failure = assertFailsWith<ToolFailure> { requestOf(inspect, arguments, noFiles) }
        assertEquals("OPS-BAD-ARGUMENT", failure.code)
    }

    @Test
    fun aJcalNeedsNoCalendar() {
        val arguments = JsonObject(mapOf("jcal" to JsonPrimitive("x")))
        assertEquals(setOf("jcal"), request(convert, arguments).keys)
        assertFailsWith<ToolFailure> { requestOf(convert, JsonObject(emptyMap()), noFiles) }
    }

    @Test
    fun aToolWithoutACalendarPassesItsFields() {
        val arguments = arguments("code" to "ICAL-S001", "content" to "x")
        assertEquals(arguments, request(explain, arguments))
    }

    @Test
    fun aDeniedFileHasOneAnswer() {
        val failure =
            assertFailsWith<ToolFailure> { requestOf(inspect, arguments("path" to "/secret"), { FileRead.Denied }) }
        assertEquals("MCP-PATH-DENIED", failure.code)
        assertEquals(PATH_DENIED_MESSAGE, failure.text)
        assertFalse("secret" in failure.text)
        val large =
            assertFailsWith<ToolFailure> { requestOf(inspect, arguments("path" to "/a.ics"), { FileRead.TooLarge }) }
        assertEquals("OPS-TOO-LARGE", large.code)
    }

    @Test
    fun whereFilesCannotBeCheckedNothingIsRead() {
        val failure = assertFailsWith<ToolFailure> { readFile("/a.ics", RootUris.WorkingDirectory, supported = false) }
        assertEquals("MCP-PATH-UNSUPPORTED", failure.code)
        assertEquals(PATH_UNSUPPORTED_MESSAGE, failure.text)
    }

    @Test
    fun noRootsAllowNothing() {
        assertEquals(FileRead.Denied, readFile(".", RootUris.None, supported = true))
        assertEquals(FileRead.Denied, readFile(".", RootUris.Listed(listOf("https://example.com/")), supported = true))
    }

    @Test
    fun successIsTheEnvelopeTwice() {
        val response = """{"ops_version":1,"ok":true,"result":{},"truncated":false}"""
        val result = resultOf(response)
        assertEquals(response, (result.content.single() as TextContent).text)
        assertEquals(Json.parseToJsonElement(response).jsonObject, result.structuredContent)
        assertEquals(false, result.isError)
    }

    @Test
    fun failureIsTheErrorOnly() {
        val result = resultOf("""{"ops_version":1,"ok":false,"error":{"code":"OPS-BAD-ARGUMENT","message":"m"}}""")
        val text = (result.content.single() as TextContent).text
        assertEquals("""{"code":"OPS-BAD-ARGUMENT","message":"m"}""", text)
        assertEquals(true, result.isError)
        assertNull(result.structuredContent)
    }

    @Test
    fun aFailureOfTheTransportHasTheSameForm() {
        val result = resultOf(ToolFailure("MCP-PATH-DENIED", "m"))
        assertTrue((result.content.single() as TextContent).text == """{"code":"MCP-PATH-DENIED","message":"m"}""")
        assertEquals(true, result.isError)
    }
}

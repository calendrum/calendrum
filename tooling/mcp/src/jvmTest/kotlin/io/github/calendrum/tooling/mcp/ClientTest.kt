package io.github.calendrum.tooling.mcp

import io.github.calendrum.tooling.ops.SchemaCheck
import io.github.oshai.kotlinlogging.DirectLoggerFactory
import io.github.oshai.kotlinlogging.KotlinLoggingConfiguration
import io.github.oshai.kotlinlogging.Level
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.ClientOptions
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.types.CallToolRequestParams
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.ClientCapabilities
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ListResourceTemplatesRequest
import io.modelcontextprotocol.kotlin.sdk.types.McpJson
import io.modelcontextprotocol.kotlin.sdk.types.ReadResourceRequest
import io.modelcontextprotocol.kotlin.sdk.types.ReadResourceRequestParams
import io.modelcontextprotocol.kotlin.sdk.types.Root
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import io.modelcontextprotocol.kotlin.sdk.types.TextResourceContents
import io.modelcontextprotocol.kotlin.sdk.types.Tool
import io.modelcontextprotocol.kotlin.sdk.types.ToolSchema
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

// The real binary under the stdio client of kotlin-sdk.
class ClientTest {
    private val casesDirectory = File(System.getProperty("calendrum.cases"))
    private val guidesDirectory = File(System.getProperty("calendrum.guides"))

    private fun withClient(directory: File, roots: List<Root>? = null, block: suspend (Client) -> Unit) {
        KotlinLoggingConfiguration.logStartupMessage = false
        KotlinLoggingConfiguration.loggerFactory = DirectLoggerFactory
        KotlinLoggingConfiguration.direct.logLevel = Level.OFF
        val errors = File.createTempFile("calendrum-mcp", ".stderr")
        val process = ProcessBuilder(binary()).directory(directory).redirectError(errors).start()
        try {
            runBlocking {
                val capabilities =
                    ClientCapabilities(roots = roots?.let { ClientCapabilities.Roots(listChanged = false) })
                val client = Client(Implementation("calendrum-test", "0"), ClientOptions(capabilities = capabilities))
                roots?.let(client::addRoots)
                client.connect(
                    StdioClientTransport(
                        process.inputStream.asSource().buffered(),
                        process.outputStream.asSink().buffered(),
                    ),
                )
                block(client)
                process.outputStream.close()
                assertTrue(process.waitFor(30, TimeUnit.SECONDS), "The server did not stop at the end of its input")
                assertEquals(0, process.exitValue())
                client.close()
            }
            assertEquals("", errors.readText(), "Standard error must stay empty")
        } finally {
            process.destroyForcibly()
            errors.delete()
        }
    }

    private suspend fun Client.call(name: String, arguments: JsonObject): CallToolResult =
        callTool(CallToolRequest(CallToolRequestParams(name = name, arguments = arguments)))

    private fun schemaOf(schema: ToolSchema?): JsonObject =
        McpJson.encodeToJsonElement(ToolSchema.serializer(), assertNotNull(schema)).jsonObject

    // No definition is known: a schema of a tool that still refers to another file fails here.
    private val checker = SchemaCheck { null }

    @Test
    fun theServerIntroducesItselfAndAnswers() = withClient(temporaryDirectory()) { client ->
        assertEquals("calendrum-mcp", client.serverVersion?.name)
        assertEquals("0.1.0", client.serverVersion?.version)
        assertTrue(assertNotNull(client.serverInstructions).contains("data"))
        client.ping()
        val tools = client.listTools().tools
        assertEquals(12, tools.size)
        for (tool in tools) {
            assertEquals(true, tool.annotations?.readOnlyHint, tool.name)
            assertNotNull(tool.outputSchema, tool.name)
        }
    }

    @Test
    fun theGuidesAreTheFilesOfTheRepository() = withClient(temporaryDirectory()) { client ->
        val files = guidesDirectory.listFiles { file ->
            file.extension == "md"
        }.orEmpty().associateBy { it.nameWithoutExtension }
        val resources = client.listResources().resources
        assertEquals(files.keys, resources.map { it.name }.toSet())
        for (resource in resources) {
            assertEquals("calendrum://guides/${resource.name}", resource.uri)
            assertEquals("text/markdown", resource.mimeType)
            val read = client.readResource(ReadResourceRequest(ReadResourceRequestParams(resource.uri)))
            val contents = assertIs<TextResourceContents>(read.contents.single())
            assertEquals(files.getValue(resource.name).readText(), contents.text, resource.name)
        }
    }

    @Test
    fun theDiagnosticsAreATemplateOverTheTableOfExplain() = withClient(temporaryDirectory()) { client ->
        val template = client.listResourceTemplates(ListResourceTemplatesRequest()).resourceTemplates.single()
        assertEquals("calendrum://diagnostics/{code}", template.uriTemplate)
        val read = client.readResource(
            ReadResourceRequest(ReadResourceRequestParams("calendrum://diagnostics/ICAL-S001")),
        )
        val contents = assertIs<TextResourceContents>(read.contents.single())
        val explained = client.call("ics_explain", JsonObject(mapOf("code" to JsonPrimitive("ICAL-S001"))))
        assertEquals(explained.structuredContent?.get("result"), Json.parseToJsonElement(contents.text))
    }

    @Test
    fun everyCaseOfTheContractPassesThroughItsTool() = withClient(temporaryDirectory()) { client ->
        val tools = client.listTools().tools.associateBy { it.name }
        val errors = ArrayList<String>()
        var compared = 0
        for (case in cases()) {
            val tool = tools.getValue("ics_${case.operation}")
            val request = Json.parseToJsonElement(case.request) as? JsonObject ?: continue
            val arguments = argumentsOf(tool, request)
            val result = client.call(tool.name, arguments)
            if (case.id in NEEDS_CLOCK_OR_RANDOM) {
                checkWithoutFixedEnvironment(client, case, result)
            } else {
                compare(case, result, errors)
                compared++
            }
            result.structuredContent?.let { content ->
                checker.check(content, schemaOf(tool.outputSchema), "${case.id} output", errors)
                checker.check(arguments, schemaOf(tool.inputSchema), "${case.id} input", errors)
                assertEquals(content, Json.parseToJsonElement(textOf(result)), case.id)
            }
        }
        assertEquals(emptyList(), errors)
        assertTrue(compared >= 50, "$compared cases")
    }

    private fun textOf(result: CallToolResult): String = assertIs<TextContent>(result.content.single()).text

    private fun compare(case: Case, result: CallToolResult, errors: MutableList<String>) {
        val expected = Json.parseToJsonElement(case.expected).jsonObject
        if (expected["ok"] == JsonPrimitive(true)) {
            if (result.isError == true) {
                errors += "${case.id}: ${textOf(result)}"
            } else if (comparable(expected, case.ignored) !=
                comparable(assertNotNull(result.structuredContent), case.ignored)
            ) {
                errors += "${case.id}: ${result.structuredContent}"
            }
        } else {
            assertEquals(true, result.isError, case.id)
            assertNull(result.structuredContent, case.id)
            val code = Json.parseToJsonElement(textOf(result)).jsonObject.getValue("code")
            assertEquals(expected.getValue("error").jsonObject.getValue("code"), code, case.id)
        }
    }

    private suspend fun checkWithoutFixedEnvironment(client: Client, case: Case, result: CallToolResult) {
        val content = assertNotNull(result.structuredContent, case.id)
        val output = content.getValue("result").jsonObject
        when (case.operation) {
            "build_event" -> {
                val validated = client.call("ics_validate", JsonObject(mapOf("content" to output.getValue("ics"))))
                val report = assertNotNull(validated.structuredContent).getValue("result").jsonObject
                assertEquals(JsonPrimitive(true), report["valid"], case.id)
            }

            "anonymize" -> assertTrue("Salary" !in output.getValue("ics").jsonPrimitive.content, case.id)

            "tz_resolve" -> assertEquals("Asia/Tokyo", output.getValue("id").jsonPrimitive.content)

            else -> error(case.id)
        }
    }

    // The fields of a tool have other names for a calendar, and its lists are shorter unless a call says otherwise.
    private fun argumentsOf(tool: Tool, request: JsonObject): JsonObject {
        val arguments = LinkedHashMap<String, JsonElement>()
        for ((name, value) in request) {
            when (name) {
                "ics" -> arguments["content"] = value
                "ics_base64" -> arguments["content_base64"] = value
                else -> arguments[name] = value
            }
        }
        if ("limit" in assertNotNull(tool.inputSchema.properties) && "limit" !in arguments) {
            arguments["limit"] = JsonPrimitive(1000)
        }
        return JsonObject(arguments)
    }

    private class Case(
        val id: String,
        val operation: String,
        val request: String,
        val expected: String,
        val ignored: Set<String>,
    )

    private fun cases(): List<Case> = casesDirectory.listFiles().orEmpty()
        .filter { File(it, "request.json").exists() }
        .sortedBy { it.name }
        .map { directory ->
            val meta = Json.parseToJsonElement(File(directory, "meta.json").readText()).jsonObject
            Case(
                id = directory.name,
                operation = meta.getValue("operation").jsonPrimitive.content,
                request = File(directory, "request.json").readText(),
                expected = File(directory, "expected.json").readText(),
                ignored = (meta["ignore_fields"] as? JsonArray).orEmpty().map { it.jsonPrimitive.content }.toSet(),
            )
        }

    private fun comparable(element: JsonElement, ignored: Set<String>): JsonElement = when (element) {
        is JsonObject -> JsonObject(
            element.filterKeys { it != "message" && it !in ignored }.mapValues { comparable(it.value, ignored) },
        )

        is JsonArray -> JsonArray(element.map { comparable(it, ignored) })

        else -> element
    }

    @Test
    fun theRootsOfTheClientOpenTheFilesInsideThem() {
        val root = temporaryDirectory()
        File(root, "inside.ics").writeText(SIMPLE_CALENDAR)
        val outside = temporaryDirectory()
        File(outside, "outside.ics").writeText(SIMPLE_CALENDAR)
        val roots = listOf(Root(uri = root.toURI().toString().replaceFirst("file:/", "file:///"), name = "test"))
        withClient(temporaryDirectory(), roots) { client ->
            val inside = client.call("ics_inspect", JsonObject(mapOf("path" to JsonPrimitive("$root/inside.ics"))))
            assertEquals(false, inside.isError, inside.toString())
            val denied = client.call("ics_inspect", JsonObject(mapOf("path" to JsonPrimitive("$outside/outside.ics"))))
            assertEquals(true, denied.isError)
            assertTrue("MCP-PATH-DENIED" in textOf(denied))
        }
        root.deleteRecursively()
        outside.deleteRecursively()
    }

    private companion object {
        // The binary has a real clock and real random bytes, and no flag for tests.
        val NEEDS_CLOCK_OR_RANDOM = setOf(
            "anonymize-shift",
            "anonymize-text-goes",
            "build-event-all-day-generated-uid",
            "tz-resolve-default-moment",
        )
    }
}

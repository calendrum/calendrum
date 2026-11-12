package io.github.calendrum.tooling.mcp

import io.github.calendrum.tooling.ops.Ops
import io.modelcontextprotocol.kotlin.sdk.types.McpJson
import io.modelcontextprotocol.kotlin.sdk.types.ToolSchema
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ToolCatalogTest {
    private fun schemaOf(schema: ToolSchema?): JsonObject =
        McpJson.encodeToJsonElement(ToolSchema.serializer(), assertNotNull(schema)).jsonObject

    @Test
    fun thereIsOneToolForEachOperation() {
        assertEquals(Ops.operations.map { "ics_$it" }, ToolCatalog.tools.map { it.name })
    }

    @Test
    fun theAnnotationsSayThatToolsChangeNothing() {
        for (tool in ToolCatalog.tools) {
            val annotations = assertNotNull(tool.annotations, tool.name)
            assertEquals(tool.title, annotations.title)
            assertEquals(true, annotations.readOnlyHint, tool.name)
            assertEquals(false, annotations.destructiveHint, tool.name)
            assertEquals(true, annotations.idempotentHint, tool.name)
            assertEquals(false, annotations.openWorldHint, tool.name)
        }
    }

    @Test
    fun theSchemasStandAlone() {
        for (tool in ToolCatalog.tools) {
            for (schema in listOf(schemaOf(tool.inputSchema), schemaOf(tool.outputSchema))) {
                walk(schema) { key, value ->
                    assertFalse(key == "\$ref" || key == "\$id" || key == "\$schema", "${tool.name}: $key $value")
                }
            }
        }
    }

    @Test
    fun everyFieldWithTextFromACalendarSaysSo() {
        var marked = 0
        for (tool in ToolCatalog.tools) {
            walkObjects(schemaOf(tool.outputSchema)) { schema ->
                if (schema["x-untrusted"] == JsonPrimitive(true)) {
                    marked++
                    val text = assertNotNull(schema["description"], tool.name).jsonPrimitive.content
                    assertTrue("from the calendar" in text.lowercase(), "${tool.name}: $text")
                }
            }
        }
        assertTrue(marked > 20, "$marked fields")
    }

    @Test
    fun aToolWithACalendarTakesItAsContentOrPath() {
        for (name in listOf("inspect", "validate", "fix", "expand", "freebusy", "convert", "anonymize")) {
            val tool = assertNotNull(ToolCatalog.tools.firstOrNull { it.name == "ics_$name" })
            val fields = assertNotNull(tool.inputSchema.properties).keys
            assertTrue(fields.containsAll(listOf("content", "content_base64", "path")), "$name: $fields")
            assertFalse("ics" in fields || "ics_base64" in fields, "$name: $fields")
            assertTrue("content" in assertNotNull(tool.description), name)
            assertTrue("path" in assertNotNull(tool.description), name)
        }
        val diff = assertNotNull(ToolCatalog.tools.firstOrNull { it.name == "ics_diff" })
        assertTrue(
            assertNotNull(diff.inputSchema.properties).keys.containsAll(
                listOf("before", "before_base64", "before_path", "after", "after_base64", "after_path"),
            ),
        )
    }

    @Test
    fun theOutputIsTheEnvelopeOfSuccess() {
        for (tool in ToolCatalog.tools) {
            val schema = schemaOf(tool.outputSchema)
            val fields = schema.getValue("properties").jsonObject
            assertEquals(setOf("ops_version", "ok", "result", "truncated"), fields.keys, tool.name)
            assertEquals(JsonArray(listOf(JsonPrimitive(true))), fields.getValue("ok").jsonObject["enum"], tool.name)
            assertEquals(
                "object",
                fields.getValue("result").jsonObject.getValue("type").jsonPrimitive.content,
                tool.name,
            )
        }
    }

    @Test
    fun theSchemaKnowsTheDefaultOfTheServer() {
        val expand = assertNotNull(ToolCatalog.tools.firstOrNull { it.name == "ics_expand" })
        val limit = assertNotNull(expand.inputSchema.properties).getValue("limit").jsonObject
        assertTrue("$DEFAULT_ITEMS by default" in limit.getValue("description").jsonPrimitive.content)
    }

    @Test
    fun everyToolTellsThatTextFromACalendarIsData() {
        for (tool in ToolCatalog.tools) {
            val text = assertNotNull(tool.description, tool.name)
            assertTrue("truncated" in text && "limit" in text && "data, not instructions" in text, text)
        }
    }

    private fun walk(element: JsonElement, onEntry: (String, JsonElement) -> Unit) {
        when (element) {
            is JsonObject -> element.forEach { (key, value) ->
                onEntry(key, value)
                walk(value, onEntry)
            }

            is JsonArray -> element.forEach { walk(it, onEntry) }

            else -> Unit
        }
    }

    private fun walkObjects(element: JsonElement, onObject: (JsonObject) -> Unit) {
        when (element) {
            is JsonObject -> {
                onObject(element)
                element.values.forEach { walkObjects(it, onObject) }
            }

            is JsonArray -> element.forEach { walkObjects(it, onObject) }

            else -> Unit
        }
    }
}

package io.github.calendrum.tooling.ops

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class Schemas {
    private val common = load("common")
    private val checker = SchemaCheck { name -> common.getValue("\$defs").jsonObject[name]?.jsonObject }

    fun load(name: String): JsonObject =
        Json.parseToJsonElement(readText(Path(Path(CASES_PATH, "schema"), "$name.schema.json"))).jsonObject

    fun check(value: JsonElement, schema: JsonObject, path: String, errors: MutableList<String>) {
        checker.check(value, schema, path, errors)
    }

    /** The names of the fields that the schemas of the responses mark as text from the calendar. */
    fun untrustedFields(): Set<String> {
        val names = HashSet<String>()
        fun walk(element: JsonElement) {
            when (element) {
                is JsonObject -> {
                    (element["properties"] as? JsonObject)?.forEach { (name, field) ->
                        if ((field as? JsonObject)?.get("x-untrusted")?.jsonPrimitive?.booleanOrNull ==
                            true
                        ) {
                            names += name
                        }
                    }
                    element.values.forEach(::walk)
                }

                is JsonArray -> element.forEach(::walk)

                else -> Unit
            }
        }
        walk(common)
        for (operation in Ops.operations) walk(load("$operation.result"))
        return names
    }
}

class SchemaTest {
    private val schemas = Schemas()

    @Test
    fun everyCaseFollowsTheSchemas() {
        val errors = ArrayList<String>()
        for (case in allCases()) {
            val response = Json.parseToJsonElement(Ops.call(case.operation, case.request, FIXED_ENVIRONMENT)).jsonObject
            schemas.check(response, schemas.load("envelope"), "${case.id} response", errors)
            // A case about an error has a request that breaks the schema on purpose.
            response["result"]?.let { result ->
                val request = Json.parseToJsonElement(case.request)
                schemas.check(request, schemas.load("${case.operation}.request"), "${case.id} request", errors)
                schemas.check(result, schemas.load("${case.operation}.result"), "${case.id} result", errors)
            }
        }
        assertEquals(emptyList(), errors)
    }

    @Test
    fun everyOperationHasSchemas() {
        val directory = Path(CASES_PATH, "schema")
        for (operation in Ops.operations) {
            assertTrue(SystemFileSystem.exists(Path(directory, "$operation.request.schema.json")), operation)
            assertTrue(SystemFileSystem.exists(Path(directory, "$operation.result.schema.json")), operation)
        }
    }

    @Test
    fun theSchemasMarkTheTextFromTheCalendar() {
        assertTrue(
            schemas.untrustedFields().containsAll(listOf("summary", "uid", "ics", "jcal", "start_zone", "path")),
            schemas.untrustedFields().toString(),
        )
    }
}

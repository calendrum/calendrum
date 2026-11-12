package io.github.calendrum.tooling.ops

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

// The part of JSON Schema that the schemas of the contract use. No library: a schema that the check does not understand
// fails it. The tests of :ops and of :mcp share this file, so that they read the schemas the same way.
internal class SchemaCheck(private val definition: (String) -> JsonObject?) {
    fun check(value: JsonElement, schema: JsonObject, path: String, errors: MutableList<String>) {
        for (key in schema.keys) {
            if (key !in KNOWN_KEYWORDS) errors += "$path: unknown keyword $key"
        }
        schema["\$ref"]?.let { ref ->
            val name = ref.jsonPrimitive.content.removePrefix("common.schema.json#/\$defs/")
            val target = definition(name)
            if (target == null) errors += "$path: unknown reference $name" else check(value, target, path, errors)
        }
        schema["type"]?.let { type ->
            val types = if (type is JsonArray) {
                type.map {
                    it.jsonPrimitive.content
                }
            } else {
                listOf(type.jsonPrimitive.content)
            }
            if (types.none { hasType(value, it) }) errors += "$path: not ${types.joinToString("|")}"
        }
        (schema["enum"] as? JsonArray)?.let { values ->
            if (value !in values) errors += "$path: not in the enum"
        }
        (schema["oneOf"] as? JsonArray)?.let { options ->
            val matches = options.count { option ->
                val inner = ArrayList<String>()
                check(value, option.jsonObject, path, inner)
                inner.isEmpty()
            }
            if (matches != 1) errors += "$path: matches $matches options of oneOf"
        }
        if (value is JsonObject) {
            val properties = schema["properties"]?.jsonObject.orEmpty()
            (schema["required"] as? JsonArray)?.forEach { name ->
                if (name.jsonPrimitive.content !in value) errors += "$path: no ${name.jsonPrimitive.content}"
            }
            val additional = schema["additionalProperties"]
            for ((name, field) in value) {
                val inner = properties[name]?.jsonObject
                if (inner != null) {
                    check(field, inner, "$path.$name", errors)
                } else if (additional is JsonPrimitive && additional.booleanOrNull == false) {
                    errors += "$path: unexpected field $name"
                }
            }
        }
        if (value is JsonArray) {
            schema["items"]?.jsonObject?.let { items ->
                value.forEachIndexed { index, item -> check(item, items, "$path[$index]", errors) }
            }
        }
    }

    private fun hasType(value: JsonElement, type: String): Boolean = when (type) {
        "object" -> value is JsonObject
        "array" -> value is JsonArray
        "null" -> value is JsonNull
        "string" -> value is JsonPrimitive && value !is JsonNull && value.isString
        "boolean" -> value is JsonPrimitive && !value.isString && value.booleanOrNull != null
        "integer" -> value is JsonPrimitive && !value.isString && (value.intOrNull != null || value.longOrNull != null)
        else -> false
    }

    private companion object {
        val KNOWN_KEYWORDS = setOf(
            "\$schema",
            "\$id",
            "\$defs",
            "\$ref",
            "title",
            "description",
            "type",
            "enum",
            "oneOf",
            "properties",
            "required",
            "additionalProperties",
            "items",
            "x-untrusted",
        )
    }
}

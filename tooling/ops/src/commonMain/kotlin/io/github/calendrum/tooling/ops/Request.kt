package io.github.calendrum.tooling.ops

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlin.io.encoding.Base64
import kotlin.time.Instant

// The fields of a request. An absent field and JSON null are the same; a field the operation does not know is ignored.
internal class Request private constructor(private val body: JsonObject, private val limits: OpsLimits) {
    fun element(name: String): JsonElement? = body[name]?.takeIf { it !is JsonNull }

    fun text(name: String): String? {
        val element = element(name) ?: return null
        if (element !is JsonPrimitive || !element.isString) badArgument("The field $name must be a string")
        return element.content
    }

    fun requiredText(name: String): String = text(name) ?: badArgument("The field $name is required")

    fun boolean(name: String): Boolean? {
        val element = element(name) ?: return null
        if (element !is JsonPrimitive || element.isString) badArgument("The field $name must be a boolean")
        return element.booleanOrNull ?: badArgument("The field $name must be a boolean")
    }

    fun int(name: String): Int? {
        val element = element(name) ?: return null
        if (element !is JsonPrimitive || element.isString) badArgument("The field $name must be an integer")
        return element.intOrNull ?: badArgument("The field $name must be an integer")
    }

    fun texts(name: String): List<String>? {
        val element = element(name) ?: return null
        if (element !is JsonArray) badArgument("The field $name must be an array of strings")
        return element.map {
            if (it !is JsonPrimitive || !it.isString) badArgument("The field $name must be an array of strings")
            it.content
        }
    }

    fun objects(name: String): List<Request>? {
        val element = element(name) ?: return null
        if (element !is JsonArray) badArgument("The field $name must be an array of objects")
        return element.map {
            if (it !is JsonObject) badArgument("The field $name must be an array of objects")
            Request(it, limits)
        }
    }

    fun limit(): Int {
        val limit = int("limit") ?: return limits.defaultItems
        if (limit < 1 || limit > limits.maxItems) badArgument("The field limit is out of range")
        return limit
    }

    fun instant(name: String): Instant? {
        val text = text(name) ?: return null
        val instant = try {
            Instant.parse(text)
        } catch (_: IllegalArgumentException) {
            badArgument("The field $name must be a moment in ISO 8601")
        }
        if (instant < FIRST_MOMENT || instant > LAST_MOMENT) badArgument("The field $name is out of range")
        return instant
    }

    fun requiredInstant(name: String): Instant = instant(name) ?: badArgument("The field $name is required")

    /** The calendar in the field [name] or, in another encoding, in the field `<name>_base64`. */
    fun input(name: String): ByteArray {
        val text = text(name)
        val encoded = text("${name}_base64")
        if (text != null && encoded != null) badArgument("Give only one of $name and ${name}_base64")
        val bytes = when {
            text != null -> text.encodeToByteArray()

            encoded != null -> try {
                Base64.decode(encoded)
            } catch (_: IllegalArgumentException) {
                badArgument("The field ${name}_base64 is not base64")
            }

            else -> badArgument("The field $name is required")
        }
        if (bytes.size > limits.maxInputBytes) throw OpsFailure(TOO_LARGE, "The calendar is larger than the limit")
        return bytes
    }

    companion object {
        private val FIRST_MOMENT = Instant.parse("0001-01-01T00:00:00Z")
        private val LAST_MOMENT = Instant.parse("9999-12-31T23:59:59Z")

        fun parse(json: String, limits: OpsLimits): Request {
            if (json.length >
                limits.maxRequestChars
            ) {
                throw OpsFailure(TOO_LARGE, "The request is larger than the limit")
            }
            checkDepth(json, limits.maxDepth)
            val element = try {
                Json.parseToJsonElement(json)
            } catch (_: SerializationException) {
                badArgument("The request is not valid JSON")
            }
            if (element !is JsonObject) badArgument("The request must be a JSON object")
            return Request(element, limits)
        }

        // Before the parser, so that the bound holds whatever the parser does with deep input.
        private fun checkDepth(json: String, maxDepth: Int) {
            var depth = 0
            var inString = false
            var index = 0
            while (index < json.length) {
                val char = json[index]
                if (inString) {
                    if (char == '\\') {
                        index++
                    } else if (char == '"') {
                        inString = false
                    }
                } else {
                    when (char) {
                        '"' -> inString = true

                        '{', '[' -> {
                            depth++
                            if (depth > maxDepth) badArgument("The request is nested too deep")
                        }

                        '}', ']' -> depth--
                    }
                }
                index++
            }
        }
    }
}

internal fun localDate(text: String, field: String): LocalDate = try {
    LocalDate.parse(text)
} catch (_: IllegalArgumentException) {
    badArgument("The field $field must be a date such as 2026-10-01")
}

internal fun localDateTime(text: String, field: String): LocalDateTime = try {
    LocalDateTime.parse(text)
} catch (_: IllegalArgumentException) {
    badArgument("The field $field must be a local time such as 2026-10-01T10:00:00")
}

/** Refuses control characters in a value that the writer does not escape, such as a `UID` or an address. */
internal fun checkToken(value: String, field: String): String {
    for (char in value) {
        if (char.code < 0x20 || char.code == 0x7F) badArgument("The field $field has a control character")
    }
    return value
}

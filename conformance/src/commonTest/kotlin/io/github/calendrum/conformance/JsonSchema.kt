package io.github.calendrum.conformance

internal class JsonSchema(private val root: JsonObject) {
    private val patterns = HashMap<String, Regex>()

    fun validate(value: Json): List<String> {
        val errors = ArrayList<String>()
        check(root, value, "$", errors)
        return errors
    }

    private fun check(schema: Json, value: Json, path: String, errors: MutableList<String>) {
        if (schema is JsonBoolean) {
            if (!schema.value) errors += "$path: no value is allowed here"
            return
        }
        require(schema is JsonObject) { "A schema must be an object or a boolean at $path" }
        for ((keyword, argument) in schema.members) {
            when (keyword) {
                "\$schema", "\$id", "title", "description", "\$defs" -> Unit

                "\$ref" -> check(resolve(argument), value, path, errors)

                "type" -> checkType(argument, value, path, errors)

                "enum" -> if (value !in (argument as JsonArray).items) {
                    errors += "$path: ${value.toCompactJson()} is not one of ${argument.toCompactJson()}"
                }

                "const" -> if (value != argument) {
                    errors += "$path: expected ${argument.toCompactJson()}, got ${value.toCompactJson()}"
                }

                "properties" -> checkProperties(argument as JsonObject, value, path, errors)

                "additionalProperties" -> checkAdditional(schema, argument, value, path, errors)

                "required" -> if (value is JsonObject) {
                    for (name in (argument as JsonArray).items) {
                        val key = (name as JsonString).value
                        if (value[key] == null) errors += "$path: missing required key \"$key\""
                    }
                }

                "minProperties" -> if (value is JsonObject && value.members.size < argument.integer()) {
                    errors += "$path: expected at least ${argument.integer()} keys"
                }

                // JSON Schema 2020-12 tuples: the first items are checked one by one, `items` applies to the rest only.
                "prefixItems" -> if (value is JsonArray) {
                    val prefix = (argument as JsonArray).items
                    for (index in 0 until minOf(prefix.size, value.items.size)) {
                        check(prefix[index], value.items[index], "$path[$index]", errors)
                    }
                }

                "items" -> if (value is JsonArray) {
                    val skip = (schema["prefixItems"] as? JsonArray)?.items?.size ?: 0
                    value.items.forEachIndexed { index, item ->
                        if (index >= skip) check(argument, item, "$path[$index]", errors)
                    }
                }

                "minItems" -> if (value is JsonArray && value.items.size < argument.integer()) {
                    errors += "$path: expected at least ${argument.integer()} items"
                }

                "uniqueItems" -> if (value is JsonArray && (argument as JsonBoolean).value) {
                    if (value.items.toSet().size != value.items.size) errors += "$path: items are not unique"
                }

                "minLength" -> if (value is JsonString && codePoints(value.value) < argument.integer()) {
                    errors += "$path: shorter than ${argument.integer()} characters"
                }

                "maxLength" -> if (value is JsonString && codePoints(value.value) > argument.integer()) {
                    errors += "$path: longer than ${argument.integer()} characters"
                }

                "pattern" -> if (value is JsonString) {
                    val pattern = (argument as JsonString).value
                    val regex = patterns.getOrPut(pattern) { Regex(pattern) }
                    if (!regex.containsMatchIn(value.value)) {
                        errors += "$path: ${value.toCompactJson()} does not match $pattern"
                    }
                }

                "minimum" -> if (value is JsonInteger && value.value < argument.integer()) {
                    errors += "$path: less than ${argument.integer()}"
                }

                "maximum" -> if (value is JsonInteger && value.value > argument.integer()) {
                    errors += "$path: greater than ${argument.integer()}"
                }

                "oneOf" -> checkOneOf(argument as JsonArray, value, path, errors)

                else -> throw IllegalArgumentException("Unsupported schema keyword \"$keyword\" at $path")
            }
        }
    }

    private fun checkType(argument: Json, value: Json, path: String, errors: MutableList<String>) {
        val types = when (argument) {
            is JsonString -> listOf(argument.value)
            is JsonArray -> argument.items.map { (it as JsonString).value }
            else -> throw IllegalArgumentException("Invalid type at $path")
        }
        val actual = when (value) {
            is JsonObject -> "object"
            is JsonArray -> "array"
            is JsonString -> "string"
            is JsonInteger -> "integer"
            is JsonDecimal -> "number"
            is JsonBoolean -> "boolean"
            JsonNull -> "null"
        }
        if (actual !in types && !(actual == "integer" && "number" in types)) {
            errors += "$path: expected ${types.joinToString(" or ")}, got $actual"
        }
    }

    private fun checkProperties(properties: JsonObject, value: Json, path: String, errors: MutableList<String>) {
        if (value !is JsonObject) return
        var previous = -1
        var previousKey = ""
        for ((key, member) in value.members) {
            val schema = properties[key] ?: continue
            check(schema, member, "$path.$key", errors)
            val index = properties.keys.indexOf(key)
            if (index < previous) errors += "$path: key \"$key\" must come before \"$previousKey\""
            if (index > previous) {
                previous = index
                previousKey = key
            }
        }
    }

    private fun checkAdditional(
        schema: JsonObject,
        argument: Json,
        value: Json,
        path: String,
        errors: MutableList<String>,
    ) {
        if (value !is JsonObject) return
        val known = (schema["properties"] as? JsonObject)?.keys.orEmpty()
        for ((key, member) in value.members) {
            if (key in known) continue
            if (argument == JsonBoolean(false)) {
                errors += "$path: unexpected key \"$key\""
            } else {
                check(argument, member, "$path.$key", errors)
            }
        }
    }

    private fun checkOneOf(options: JsonArray, value: Json, path: String, errors: MutableList<String>) {
        val results = options.items.map { option -> ArrayList<String>().also { check(option, value, path, it) } }
        val matches = results.count { it.isEmpty() }
        when {
            matches == 1 -> Unit

            matches > 1 -> errors += "$path: matches $matches alternatives of oneOf, expected exactly one"

            else -> {
                errors += "$path: matches none of the oneOf alternatives; the closest one says:"
                errors += results.minBy { it.size }
            }
        }
    }

    private fun resolve(reference: Json): Json {
        val path = (reference as JsonString).value
        require(path.startsWith(DEFS)) { "Only local references to $DEFS are supported: $path" }
        val definitions = root["\$defs"] as? JsonObject
        return definitions?.get(path.removePrefix(DEFS)) ?: throw IllegalArgumentException("Unknown reference $path")
    }

    private fun Json.integer(): Long = (this as JsonInteger).value

    private fun codePoints(text: String): Int {
        var count = 0
        var index = 0
        while (index < text.length) {
            val high = text[index].isHighSurrogate() && index + 1 < text.length && text[index + 1].isLowSurrogate()
            index += if (high) 2 else 1
            count++
        }
        return count
    }

    private companion object {
        const val DEFS = "#/\$defs/"
    }
}

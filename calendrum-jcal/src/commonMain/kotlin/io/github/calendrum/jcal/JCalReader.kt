package io.github.calendrum.jcal

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.problemOf
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

// jCal to the tree through the builders of the core: every problem is an ICAL-J code with the JSON path of the element;
// a malformed component or property is skipped, a malformed value is copied as is. VALUE goes first when the type
// differs from the default type of the registry; a property the registry does not know always gets it, except
// `unknown`.
// Components deeper than `maxDepth` are skipped with ICAL-J007 before their children are looked at, so the recursion is
// bounded whatever the JSON.
internal class JCalReader(
    private val registry: ICalRegistry,
    private val problems: MutableList<Problem>,
    private val maxDepth: Int,
) {
    fun components(root: JsonElement): List<ICalComponent> {
        if (root !is JsonArray) {
            report(ProblemCode.MalformedJCalComponent, "$")
            return emptyList()
        }
        val first = root.firstOrNull()
        if (first is JsonPrimitive && first.isString) return listOfNotNull(component(root, "$", 1))
        val result = ArrayList<ICalComponent>(root.size)
        for ((index, item) in root.withIndex()) component(item, "$[$index]", 1)?.let { result += it }
        return result
    }

    fun component(element: JsonElement, path: String, depth: Int): ICalComponent? {
        if (depth > maxDepth) {
            report(ProblemCode.JCalTooDeep, path)
            return null
        }
        val parts = element as? JsonArray
        val name = (parts?.getOrNull(0) as? JsonPrimitive)?.takeIf { it.isString }?.content
        val properties = parts?.getOrNull(1) as? JsonArray
        val children = parts?.getOrNull(2) as? JsonArray
        if (parts == null || parts.size != 3 || name == null || properties == null || children == null) {
            report(ProblemCode.MalformedJCalComponent, path)
            return null
        }
        val builder = try {
            ICalComponent.builder(asciiUpper(name))
        } catch (_: IllegalArgumentException) {
            report(ProblemCode.MalformedJCalComponent, path)
            return null
        }
        for ((index, item) in properties.withIndex()) property(item, "$path[1][$index]")?.let { builder.property(it) }
        for ((index, item) in children.withIndex()) {
            component(item, "$path[2][$index]", depth + 1)?.let { builder.component(it) }
        }
        return builder.build()
    }

    private fun property(element: JsonElement, path: String): ICalProperty? {
        val parts = element as? JsonArray
        val name = (parts?.getOrNull(0) as? JsonPrimitive)?.takeIf { it.isString }?.content
        val parameters = parts?.getOrNull(1) as? JsonObject
        val type = (parts?.getOrNull(2) as? JsonPrimitive)?.takeIf { it.isString }?.content
        if (parts == null || parts.size < 4 || name == null || parameters == null || type == null) {
            report(ProblemCode.MalformedJCalProperty, path)
            return null
        }
        val upperName = asciiUpper(name)
        val lowerType = asciiLower(type)
        val known = jcalType(lowerType) != null || lowerType == UNKNOWN
        val icalType = when {
            lowerType == UNKNOWN -> null

            known -> asciiUpper(lowerType)

            isName(type) -> {
                asciiUpper(type)
            }

            else -> {
                report(ProblemCode.UnknownJCalType, "$path[2]")
                null
            }
        }
        val value = value(if (known) lowerType else UNKNOWN, parts, path) ?: return null
        return try {
            val builder = ICalProperty.builder(upperName)
            if (icalType != null && icalType != defaultType(upperName)) builder.parameter(VALUE, icalType)
            for ((key, member) in parameters) {
                val values = parameterValues(member)
                if (values == null || asciiUpper(key) == VALUE) {
                    report(ProblemCode.MalformedJCalParameter, "$path[1].$key")
                    continue
                }
                builder.parameter(asciiUpper(key), *values.toTypedArray())
            }
            builder.value(value).build()
        } catch (_: IllegalArgumentException) {
            report(ProblemCode.MalformedJCalProperty, path)
            null
        }
    }

    private fun defaultType(name: String): String? = registry.property(name)?.defaultType?.value

    private fun parameterValues(member: JsonElement): List<String>? = when (member) {
        is JsonPrimitive -> if (member.isString) listOf(member.content) else null
        is JsonArray -> member.map { (it as? JsonPrimitive)?.takeIf { item -> item.isString }?.content ?: return null }
        else -> null
    }

    private fun value(type: String, parts: JsonArray, path: String): String? {
        val out = StringBuilder()
        for (index in 3 until parts.size) {
            val item = parts[index]
            val text = when {
                type == "recur" && item is JsonObject -> recur(item, "$path[$index]") ?: return null

                item is JsonArray -> structured(type, item, "$path[$index]") ?: return null

                item is JsonPrimitive && item !is JsonNull -> scalar(type, item, "$path[$index]")

                else -> {
                    report(ProblemCode.MalformedJCalProperty, "$path[$index]")
                    return null
                }
            }
            if (index > 3) out.append(',')
            out.append(text)
        }
        return out.toString()
    }

    private fun structured(type: String, items: JsonArray, path: String): String? {
        val out = StringBuilder()
        for ((index, item) in items.withIndex()) {
            if (item !is JsonPrimitive || item is JsonNull) {
                report(ProblemCode.MalformedJCalProperty, "$path[$index]")
                return null
            }
            if (index > 0) out.append(';')
            out.append(scalar(type, item, "$path[$index]"))
        }
        return out.toString()
    }

    private fun scalar(type: String, item: JsonPrimitive, path: String): String {
        val content = item.content
        val converted: String? = when (type) {
            "text" -> if (item.isString) escapeText(content) else null

            "date" -> if (item.isString) jcalDateToIcal(content) else null

            "date-time" -> if (item.isString) jcalDateTimeToIcal(content) else null

            "time" -> if (item.isString) jcalTimeToIcal(content) else null

            "utc-offset" -> if (item.isString) jcalOffsetToIcal(content) else null

            "period" -> if (item.isString) jcalPeriodToIcal(content) else null

            "duration" -> if (item.isString && isDuration(content)) content else null

            "integer" -> if (!item.isString) content.takeIf { parseInteger(it, 0, it.length) != null } else null

            "float" -> if (!item.isString) content.takeIf { jsonDecimal(it, 0, it.length) != null } else null

            "boolean" -> when {
                item.isString -> null
                content == "true" -> "TRUE"
                content == "false" -> "FALSE"
                else -> null
            }

            else -> if (item.isString) content else null
        }
        if (converted != null) return converted
        report(ProblemCode.JCalValueMismatch, path)
        return if (type == "text") escapeText(content) else content
    }

    private fun recur(members: JsonObject, path: String): String? {
        val out = StringBuilder()
        for ((key, member) in members) {
            val part = asciiUpper(key)
            val text = when (member) {
                is JsonPrimitive -> recurValue(part, member, path) ?: return null

                is JsonArray -> {
                    val values = StringBuilder()
                    for ((index, item) in member.withIndex()) {
                        if (item !is JsonPrimitive || item is JsonNull) {
                            report(ProblemCode.MalformedJCalProperty, "$path.$key[$index]")
                            return null
                        }
                        if (index > 0) values.append(',')
                        values.append(recurValue(part, item, "$path.$key[$index]") ?: return null)
                    }
                    values.toString()
                }

                else -> {
                    report(ProblemCode.MalformedJCalProperty, "$path.$key")
                    return null
                }
            }
            if (out.isNotEmpty()) out.append(';')
            out.append(part).append('=').append(text)
        }
        return out.toString()
    }

    private fun recurValue(part: String, item: JsonPrimitive, path: String): String? {
        if (item is JsonNull) {
            report(ProblemCode.MalformedJCalProperty, path)
            return null
        }
        val content = item.content
        if (part != "UNTIL") return content
        val converted = if (content.length == 10) jcalDateToIcal(content) else jcalDateTimeToIcal(content)
        if (converted != null) return converted
        report(ProblemCode.JCalValueMismatch, path)
        return content
    }

    private fun isName(text: String): Boolean {
        if (text.isEmpty()) return false
        for (char in text) {
            if (char !in 'a'..'z' && char !in 'A'..'Z' && char !in '0'..'9' && char != '-') return false
        }
        return true
    }

    private fun report(code: ProblemCode, path: String) {
        problems += problemOf(code, path)
    }

    override fun toString(): String = "JCalReader"
}

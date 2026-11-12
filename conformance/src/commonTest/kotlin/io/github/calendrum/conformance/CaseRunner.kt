package io.github.calendrum.conformance

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.syntax.ComponentFingerprint
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.ParseResult
import io.github.calendrum.syntax.Selection

internal class Outcome(val tree: Json, val diagnostics: Json) {
    fun file(name: String): Json = when (name) {
        Suite.TREE -> tree
        Suite.DIAGNOSTICS -> diagnostics
        else -> throw IllegalArgumentException("No outcome for $name")
    }

    companion object {
        val files: List<String> = listOf(Suite.TREE, Suite.DIAGNOSTICS)
    }
}

// The `ICalParseException` of the strict mode is the outcome of the case, not a test failure: it becomes `{"rejected":
// true}` plus the diagnostics collected up to and including the error, matching what a real caller of the strict mode
// gets.
internal fun runCase(input: ByteArray, meta: JsonObject): Outcome {
    val parser = ICal.parser(parseOptions(meta))
    return try {
        val result = parser.parse(input)
        Outcome(treeJson(result), diagnosticsJson(result.problems))
    } catch (e: ICalParseException) {
        Outcome(jsonObject("rejected" to JsonBoolean(true)), diagnosticsJson(e.problems))
    }
}

internal fun jcalOf(input: ByteArray, meta: JsonObject): Json {
    val result = try {
        ICal.parser(parseOptions(meta)).parse(input)
    } catch (e: ICalParseException) {
        throw JcalUndefined("the input is rejected in strict mode")
    }
    return try {
        jcal(result.components)
    } catch (_: IllegalArgumentException) {
        throw JcalUndefined("jCal is written by the rules of canonical writing, which reject the tree")
    }
}

internal fun parseOptions(meta: JsonObject, selection: Selection? = null): ParseOptions {
    val builder = ParseOptions.builder()
    if (selection != null) builder.select(selection)
    val options = meta["options"] as? JsonObject ?: return builder.build()
    when (val mode = (options["mode"] as? JsonString)?.value) {
        null -> Unit
        "lenient" -> builder.mode(ParseMode.Lenient)
        "strict" -> builder.mode(ParseMode.Strict)
        else -> throw IllegalArgumentException("Unknown mode $mode")
    }
    (options["charset"] as? JsonString)?.let { builder.charset(it.value) }
    val limits = options["limits"] as? JsonObject
    if (limits != null) {
        val limitsBuilder = ParseLimits.builder()
        limits.int("maxLineOctets")?.let { limitsBuilder.maxLineOctets(it) }
        limits.int("maxLines")?.let { limitsBuilder.maxLines(it) }
        limits.int("maxProblems")?.let { limitsBuilder.maxProblems(it) }
        limits.int("maxDepth")?.let { limitsBuilder.maxDepth(it) }
        limits.int("maxComponents")?.let { limitsBuilder.maxComponents(it) }
        builder.limits(limitsBuilder.build())
    }
    return builder.build()
}

private fun JsonObject.int(key: String): Int? = (this[key] as? JsonInteger)?.value?.toInt()

private fun treeJson(result: ParseResult): Json = jsonObject(
    "charset" to JsonString(result.charset),
    "components" to JsonArray(result.components.map(::componentJson)),
)

private fun componentJson(component: ICalComponent): Json = jsonObject(
    "name" to JsonString(component.name),
    "properties" to JsonArray(component.properties.map(::propertyJson)),
    "components" to JsonArray(component.components.map(::componentJson)),
)

private fun propertyJson(property: ICalProperty): Json = jsonObject(
    "name" to JsonString(property.name),
    "parameters" to JsonArray(property.parameters.map(::parameterJson)),
    "value" to JsonString(property.value),
)

private fun parameterJson(parameter: ICalParameter): Json = jsonObject(
    "name" to JsonString(parameter.name),
    "values" to JsonArray(parameter.values.map(::JsonString)),
)

internal fun diagnosticsJson(problems: List<Problem>): Json = JsonArray(
    problems.map { problem ->
        jsonObject(
            "code" to JsonString(problem.code.id),
            "severity" to JsonString(problem.severity.name.lowercase()),
            "line" to JsonInteger(problem.span.line.toLong()),
            "column" to JsonInteger(problem.span.column.toLong()),
            "offset" to JsonInteger(problem.span.startOffset),
        )
    },
)

// Fingerprints with the default options (`meta.json` options do not apply); the hash as 16 hex digits, since a JSON
// number cannot hold 64 bits.
internal fun fingerprintsJson(input: ByteArray): Json {
    val fingerprints = ICal.fingerprints(input)
    return jsonObject(
        "algorithmVersion" to JsonInteger(fingerprints.algorithmVersion.toLong()),
        "entries" to JsonArray(fingerprints.entries.map(::fingerprintJson)),
    )
}

private fun fingerprintJson(entry: ComponentFingerprint): Json {
    val key = entry.key
    return jsonObject(
        "calendarIndex" to JsonInteger(key.calendarIndex.toLong()),
        "name" to JsonString(key.name),
        "uid" to optional(key.uid),
        "recurrenceId" to optional(key.recurrenceId),
        "tzid" to optional(key.tzid),
        "ordinal" to JsonInteger(key.ordinal.toLong()),
        "hash" to JsonString(entry.hash.toULong().toString(HEX_RADIX).padStart(HASH_DIGITS, '0')),
        "startOffset" to JsonInteger(entry.startOffset),
        "endOffset" to JsonInteger(entry.endOffset),
    )
}

private fun optional(value: String?): Json = if (value == null) JsonNull else JsonString(value)

private const val HEX_RADIX = 16
private const val HASH_DIGITS = 16

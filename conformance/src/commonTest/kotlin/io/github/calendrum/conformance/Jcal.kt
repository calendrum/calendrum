package io.github.calendrum.conformance

import io.github.calendrum.jcal.JCal
import io.github.calendrum.syntax.ICalComponent
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.JsonArray as KotlinxArray
import kotlinx.serialization.json.JsonNull as KotlinxNull
import kotlinx.serialization.json.JsonObject as KotlinxObject

// The input has no jCal form: only when strict parsing rejects it. The case must say `jcal: false` with a reason in
// `meta.json`; `updateExpected` fails otherwise.
internal class JcalUndefined(message: String) : Exception(message)

// The jCal of a parse result is written by calendrum-jcal; the suite only turns the kotlinx JSON tree into its own
// model: a number with a fraction or an exponent stays text (`JsonDecimal`).
internal fun jcal(components: List<ICalComponent>): Json = json(JCal.encodeToJsonElement(components))

internal fun json(element: JsonElement): Json = when (element) {
    is KotlinxObject -> JsonObject(element.map { (key, value) -> key to json(value) })

    is KotlinxArray -> JsonArray(element.map(::json))

    is KotlinxNull -> JsonNull

    is JsonPrimitive -> when {
        element.isString -> JsonString(element.content)
        element.booleanOrNull != null -> JsonBoolean(element.booleanOrNull!!)
        element.content.any { it == '.' || it == 'e' || it == 'E' } -> JsonDecimal(element.content)
        else -> JsonInteger(element.content.toLong())
    }
}

package io.github.calendrum.jcal

import io.github.calendrum.model.Attach
import io.github.calendrum.model.Categories
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.ExDate
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.model.RDate
import io.github.calendrum.model.RequestStatus
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import kotlinx.datetime.UtcOffset
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal class JCalWriter(private val registry: ICalRegistry) {
    fun component(component: ICalComponent): JsonArray {
        if (!isName(component.name)) throw IllegalArgumentException("Cannot write a component with an invalid name")
        return JsonArray(
            listOf(
                JsonPrimitive(asciiLower(component.name)),
                JsonArray(component.properties.map { property(it) }),
                JsonArray(component.components.map { component(it) }),
            ),
        )
    }

    // The type is VALUE, else the default type of the registry; a value that its type cannot decode tries the other
    // allowed types of the registry (TRIGGER:19980403T120000Z without VALUE), then is written as `unknown` with the raw
    // text, which keeps the text but not a declared VALUE: reading `unknown` adds none (RFC 7265 §5). A DATE-TIME
    // property whose values are all dates is `date`. Parameters except VALUE, lower case; a repeated parameter becomes
    // one array.
    fun property(property: ICalProperty): JsonArray {
        checkWritable(property)
        val parameters = LinkedHashMap<String, JsonElement>()
        for (parameter in property.parameters) {
            if (parameter.name == VALUE) continue
            val name = asciiLower(parameter.name)
            val values = ArrayList<JsonPrimitive>()
            when (val existing = parameters[name]) {
                is JsonPrimitive -> values += existing
                is JsonArray -> for (item in existing) values += item as JsonPrimitive
                else -> Unit
            }
            parameter.values.mapTo(values, ::JsonPrimitive)
            parameters[name] = if (values.size == 1) values[0] else JsonArray(values)
        }
        var type = typeOf(property)
        var rendered = render(property, type)
        if (rendered === FALLBACK && property.parameter(VALUE) == null) {
            val allowed = registry.property(property.name)?.allowedTypes.orEmpty()
            for (candidate in allowed) {
                val other = jcalType(candidate.value) ?: continue
                if (other == type) continue
                val attempt = render(property, other)
                if (attempt !== FALLBACK) {
                    type = other
                    rendered = attempt
                    break
                }
            }
        }
        if (type == "date-time" && rendered.isNotEmpty() && rendered !== FALLBACK && rendered.all { isDateValue(it) }) {
            type = "date"
        }
        val items = ArrayList<JsonElement>(4 + rendered.size)
        items += JsonPrimitive(asciiLower(property.name))
        items += JsonObject(parameters)
        if (rendered === FALLBACK) {
            items += JsonPrimitive(UNKNOWN)
            items += JsonPrimitive(property.value)
        } else {
            items += JsonPrimitive(type)
            items.addAll(rendered)
        }
        return JsonArray(items)
    }

    private fun render(property: ICalProperty, type: String): List<JsonElement> = try {
        values(property, type)
    } catch (_: IllegalArgumentException) {
        FALLBACK
    }

    private fun isDateValue(element: JsonElement): Boolean = element is JsonPrimitive && element.content.length == 10

    // A declared type that jCal does not define (`VALUE=X-FOO`) is written as its name in lower case with the raw text,
    // which reading turns back into the VALUE parameter; `unknown` would lose it.
    private fun typeOf(property: ICalProperty): String {
        val declared = property.parameter(VALUE)?.value
        if (declared != null) return jcalType(declared) ?: asciiLower(declared)
        val definition = registry.property(property.name) ?: return UNKNOWN
        return jcalType(definition.defaultType.value) ?: UNKNOWN
    }

    // Values are decoded by the codecs of standard keys on a copy of the property with VALUE set to the type (EXDATE
    // for dates, RDATE for periods), so a type from VALUE or the registry decodes even for an X- property. CATEGORIES
    // and RESOURCES are lists, REQUEST-STATUS and GEO structured values (RFC 7265 §3.4).
    private fun values(property: ICalProperty, type: String): List<JsonElement> = when (type) {
        "text" -> text(property)
        "date", "date-time" -> decode(typed(property, type), Properties.EXDATE) { dates(it, type) }
        "period" -> decode(typed(property, type), Properties.RDATE) { periods(it) }
        "duration" -> decode(typed(property, type), Properties.DURATION) { listOf(JsonPrimitive(it.toString())) }
        "recur" -> decode(typed(property, type), Properties.RRULE) { listOf(recur(it, property.value)) }
        "utc-offset" -> decode(typed(property, type), Properties.TZOFFSETFROM) { listOf(JsonPrimitive(offset(it))) }
        "binary" -> decode(typed(property, type), Properties.ATTACH) { binary(it) }
        "integer" -> integer(property.value)
        "float" -> floats(property)
        "boolean" -> boolean(property.value)
        "time" -> times(property.value)
        else -> listOf(JsonPrimitive(property.value))
    }

    private fun text(property: ICalProperty): List<JsonElement> = when (property.name) {
        "CATEGORIES", "RESOURCES" -> decode(property, Properties.CATEGORIES) { categories ->
            categories.values.map(::JsonPrimitive)
        }

        "REQUEST-STATUS" -> decode(property, Properties.REQUEST_STATUS) { status ->
            listOf(JsonArray(listOfNotNull(status.code, status.description, status.data).map(::JsonPrimitive)))
        }

        else -> decode(property, Properties.DESCRIPTION) { listOf(JsonPrimitive(it)) }
    }

    private fun dates(value: ExDate, type: String): List<JsonElement> {
        if (value.dates.isEmpty()) return FALLBACK
        val dates = value.dates.count { it is ICalDateTime.Date }
        if (type == "date" && dates != value.dates.size) return FALLBACK
        if (type == "date-time" && dates != 0 && dates != value.dates.size) return FALLBACK
        return value.dates.map { JsonPrimitive(jcalDateTime(it)) }
    }

    private fun periods(value: RDate): List<JsonElement> {
        if (value.periods.isEmpty() || value.dates.isNotEmpty()) return FALLBACK
        return value.periods.map { JsonPrimitive(jcalPeriod(it)) }
    }

    private fun binary(value: Attach): List<JsonElement> =
        if (value is Attach.Binary) listOf(JsonPrimitive(base64(value.bytes()))) else FALLBACK

    private fun floats(property: ICalProperty): List<JsonElement> {
        val raw = property.value
        if (property.name == "GEO") {
            val separator = raw.indexOf(';')
            if (separator < 0) return FALLBACK
            val latitude = jsonDecimal(raw, 0, separator) ?: return FALLBACK
            val longitude = jsonDecimal(raw, separator + 1, raw.length) ?: return FALLBACK
            return listOf(JsonArray(listOf(latitude, longitude)))
        }
        val result = ArrayList<JsonElement>()
        var start = 0
        while (true) {
            val end = raw.indexOf(',', start).let { if (it < 0) raw.length else it }
            result += jsonDecimal(raw, start, end) ?: return FALLBACK
            if (end == raw.length) return result
            start = end + 1
        }
    }

    private fun integer(raw: String): List<JsonElement> {
        val result = ArrayList<JsonElement>()
        var start = 0
        while (true) {
            val end = raw.indexOf(',', start).let { if (it < 0) raw.length else it }
            val number = parseInteger(raw, start, end) ?: return FALLBACK
            result += JsonPrimitive(number)
            if (end == raw.length) return result
            start = end + 1
        }
    }

    private fun boolean(raw: String): List<JsonElement> = when {
        raw.trim().equals("TRUE", ignoreCase = true) -> listOf(JsonPrimitive(true))
        raw.trim().equals("FALSE", ignoreCase = true) -> listOf(JsonPrimitive(false))
        else -> FALLBACK
    }

    private fun times(raw: String): List<JsonElement> {
        val result = ArrayList<JsonElement>()
        var start = 0
        while (true) {
            val end = raw.indexOf(',', start).let { if (it < 0) raw.length else it }
            result += JsonPrimitive(icalTimeToJcal(raw, start, end) ?: return FALLBACK)
            if (end == raw.length) return result
            start = end + 1
        }
    }

    // Parts in the order of the file, names from the raw text and values from the decoded rule, so the rule reads back
    // as written; INTERVAL=1 stays when the file has it.
    private fun recur(value: Recur, raw: String): JsonObject {
        val members = LinkedHashMap<String, JsonElement>()
        var start = 0
        var unknown = 0
        while (start <= raw.length) {
            val end = raw.indexOf(';', start).let { if (it < 0) raw.length else it }
            val equals = raw.indexOf('=', start)
            val name = asciiUpper(raw.substring(start, if (equals in start until end) equals else end).trim())
            when (name) {
                "FREQ" -> members["freq"] = JsonPrimitive(value.frequency.name)

                "UNTIL" -> value.until?.let { members["until"] = JsonPrimitive(jcalDateTime(it)) }

                "COUNT" -> value.count?.let { members["count"] = JsonPrimitive(it) }

                "INTERVAL" -> members["interval"] = JsonPrimitive(value.interval)

                "BYSECOND" -> numbers(members, "bysecond", value.bySecond)

                "BYMINUTE" -> numbers(members, "byminute", value.byMinute)

                "BYHOUR" -> numbers(members, "byhour", value.byHour)

                "BYDAY" -> many(members, "byday", value.byDay.map { JsonPrimitive(it.toString()) })

                "BYMONTHDAY" -> numbers(members, "bymonthday", value.byMonthDay)

                "BYYEARDAY" -> numbers(members, "byyearday", value.byYearDay)

                "BYWEEKNO" -> numbers(members, "byweekno", value.byWeekNo)

                "BYMONTH" -> many(
                    members,
                    "bymonth",
                    value.byMonth.map { if (it.isLeap) JsonPrimitive(it.toString()) else JsonPrimitive(it.month) },
                )

                "BYSETPOS" -> numbers(members, "bysetpos", value.bySetPos)

                "WKST" -> value.weekStart?.let { members["wkst"] = JsonPrimitive(it.name.substring(0, 2)) }

                "RSCALE" -> value.rscale?.let { members["rscale"] = JsonPrimitive(it) }

                "SKIP" -> value.skip?.let { members["skip"] = JsonPrimitive(it.name) }

                "" -> Unit

                else -> {
                    val part = value.unknownParts.getOrNull(unknown++)
                    if (part != null) members[asciiLower(part.name)] = JsonPrimitive(part.value)
                }
            }
            start = end + 1
        }
        return JsonObject(members)
    }

    private fun numbers(members: MutableMap<String, JsonElement>, name: String, values: List<Int>) {
        many(members, name, values.map { JsonPrimitive(it) })
    }

    private fun many(members: MutableMap<String, JsonElement>, name: String, values: List<JsonElement>) {
        if (values.isEmpty()) return
        members[name] = if (values.size == 1) values[0] else JsonArray(values)
    }

    private fun typed(property: ICalProperty, type: String): ICalProperty {
        val wanted = asciiUpper(type)
        if (property.parameter(VALUE)?.value == wanted) return property
        val builder = ICalProperty.builder(property.name).parameter(VALUE, wanted)
        for (parameter in property.parameters) {
            if (parameter.name != VALUE) builder.parameter(parameter)
        }
        return builder.value(property.value).build()
    }

    private inline fun <T : Any> decode(
        property: ICalProperty,
        key: PropertyKey<T>,
        render: (T) -> List<JsonElement>,
    ): List<JsonElement> {
        val decoded = key.codec.decode(property, DecodeContext.Lenient)
        return if (decoded is Decoded.Value) render(decoded.value) else FALLBACK
    }

    override fun toString(): String = "JCalWriter"
}

// jCal is written by the rules of canonical writing: parsing keeps a name that is not a token, a
// control character other than HTAB and a parameter without a value, but reading cannot build a node with them,
// so writing them would lose them on the way back; so would a VALUE that is not one type name. The message
// names the property when its name is valid, never the text.
private fun checkWritable(property: ICalProperty) {
    val problem = when {
        !isName(property.name) -> "Invalid character in a property name"

        property.value.any(::isControl) -> "Control character in a property value"

        else -> property.parameters.firstNotNullOfOrNull { parameter ->
            when {
                !isName(parameter.name) -> "Invalid character in a parameter name"

                parameter.values.isEmpty() -> "A parameter needs at least one value"

                parameter.values.any { value -> value.any { isControl(it) && it != '\n' && it != '\r' } } ->
                    "Control character in a parameter value"

                // jCal holds the type as a name, so a VALUE that is not one type name has no form there.
                parameter.name == VALUE && (parameter.values.size != 1 || !isName(parameter.values[0])) ->
                    "The VALUE parameter is not a type name"

                else -> null
            }
        }
    } ?: return
    val name = if (isName(property.name)) " ${property.name}" else ""
    throw IllegalArgumentException("Cannot write property$name: $problem")
}

private fun isName(name: String): Boolean =
    name.isNotEmpty() && name.all { it == '-' || it in '0'..'9' || it in 'A'..'Z' || it in 'a'..'z' }

// A control character other than HTAB. A line break is one too, except in a parameter value, where RFC 6868 writes it
// as `^n`.
private fun isControl(char: Char): Boolean = (char < ' ' && char != '\t') || char == '\u007f'

internal val FALLBACK: List<JsonElement> = ArrayList(0)

internal const val VALUE = "VALUE"
internal const val UNKNOWN = "unknown"

internal fun offset(value: UtcOffset): String = jcalOffset(value.totalSeconds)

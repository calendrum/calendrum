package io.github.calendrum.jcal

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.problemOf
import io.github.calendrum.syntax.ICalComponent
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Converts component trees to and from jCal, the JSON form of iCalendar
 * ([RFC 7265](https://www.rfc-editor.org/rfc/rfc7265)).
 *
 * Values get the JSON types of their iCalendar types, taken from the registry of [JCalOptions]; a value that does not
 * decode as its type is written with the type `unknown` as it is, and a `VALUE` type that jCal does not define, such as
 * `X-FOO`, with that name in lower case. Writing follows canonical writing and throws on what it
 * rejects (a control character, a parameter without a value), so whatever is written reads back. Reading never throws
 * on data: what cannot be read is skipped and reported as a problem of the `ICAL-J` series in [JCalResult.problems].
 *
 * @sample io.github.calendrum.samples.jcalRoundTrip
 * @sample io.github.calendrum.samples.jcalProblemsInsteadOfExceptions
 */
public object JCal {
    /**
     * Writes [components] as a jCal JSON array (compact, with no whitespace) and returns the text.
     *
     * @param components the components to write, usually one `VCALENDAR`.
     * @param options the registry that tells the type of every property.
     * @throws IllegalArgumentException if a name, value or parameter cannot be written, as in canonical writing: a name
     *   that is not a token, a control character other than HTAB, or a parameter without a value; or if a `VALUE`
     *   parameter is not one type name. Parsing keeps them, but jCal could not read them back.
     * @sample io.github.calendrum.samples.jcalRoundTrip
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    public fun encodeToString(components: List<ICalComponent>, options: JCalOptions = JCalOptions.Default): String =
        encodeToJsonElement(components, options).toString()

    /**
     * Writes [components] as a jCal `JsonArray` for use with kotlinx-serialization-json.
     *
     * @param components the components to write, usually one `VCALENDAR`.
     * @param options the registry that tells the type of every property.
     * @throws IllegalArgumentException if a name, value or parameter cannot be written, as in [encodeToString].
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    public fun encodeToJsonElement(
        components: List<ICalComponent>,
        options: JCalOptions = JCalOptions.Default,
    ): JsonElement {
        val writer = JCalWriter(options.registry)
        return JsonArray(components.map { writer.component(it) })
    }

    /**
     * Reads jCal from [text]. Malformed parts are skipped and reported; input that is not JSON gives `ICAL-J001` and no
     * components, and JSON nested deeper than a jCal of `JCalOptions.maxDepth` components can be (twice the depth and
     * eight more arrays or objects) gives `ICAL-J007` and no components.
     *
     * @param text a JSON array of components, or a single component.
     * @param options the registry that tells the type of every property.
     */
    @JvmStatic
    @JvmOverloads
    public fun decodeFromString(text: String, options: JCalOptions = JCalOptions.Default): JCalResult {
        if (nestsDeeper(text, 2 * options.maxDepth + DEPTH_SLACK)) {
            return JCalResult(emptyList(), listOf(problemOf(ProblemCode.JCalTooDeep, "$")))
        }
        val element = try {
            Json.parseToJsonElement(text)
        } catch (_: SerializationException) {
            return JCalResult(emptyList(), listOf(problemOf(ProblemCode.InvalidJson, null)))
        }
        return decodeFromJsonElement(element, options)
    }

    /**
     * Reads jCal from an already parsed JSON [element], like [decodeFromString].
     *
     * @param element a JSON array of components, or a single component.
     * @param options the registry that tells the type of every property.
     */
    @JvmStatic
    @JvmOverloads
    public fun decodeFromJsonElement(element: JsonElement, options: JCalOptions = JCalOptions.Default): JCalResult {
        val problems = ArrayList<Problem>()
        val components = JCalReader(options.registry, problems, options.maxDepth).components(element)
        return JCalResult(components, problems)
    }

    /** Returns the name of the object. */
    override fun toString(): String = "JCal"
}

// kotlinx.serialization reads nested arrays by recursion (only objects switch to a heap-based reader past 200 levels),
// so 100,000 nested arrays overflow the stack before the reader sees a component. A component of depth d sits 2d arrays
// deep, its values at most four more: the scan rejects only what the reader would cut anyway.
private fun nestsDeeper(text: String, limit: Int): Boolean {
    var depth = 0
    var quoted = false
    var index = 0
    while (index < text.length) {
        val char = text[index++]
        if (quoted) {
            if (char == '\\') {
                index++
            } else if (char == '"') {
                quoted = false
            }
            continue
        }
        when (char) {
            '"' -> quoted = true
            '[', '{' -> if (++depth > limit) return true
            ']', '}' -> depth--
        }
    }
    return false
}

private const val DEPTH_SLACK = 8

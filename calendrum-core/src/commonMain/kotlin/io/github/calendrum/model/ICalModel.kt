@file:JvmName("ICalModel")

package io.github.calendrum.model

import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.ParseResult
import kotlin.jvm.JvmName

/**
 * The `VCALENDAR` components of this result as typed [VCalendar] views, in input order; computed once and cached in the
 * result.
 *
 * This is the entry to the typed model: `ICal.parse(bytes).calendars.single()`.
 */
public val ParseResult.calendars: List<VCalendar>
    get() {
        @Suppress("UNCHECKED_CAST")
        val cached = calendarsCache as List<VCalendar>?
        if (cached != null) return cached
        val list = calendarsOf(components)
        calendarsCache = list
        return list
    }

private fun calendarsOf(components: List<ICalComponent>): List<VCalendar> {
    var count = 0
    for (component in components) {
        if (component.nameId == KnownNames.VCALENDAR) count += 1
    }
    if (count == 0) return emptyList()
    val result = arrayOfNulls<VCalendar>(count)
    var index = 0
    for (component in components) {
        if (component.nameId == KnownNames.VCALENDAR) result[index++] = VCalendar(component)
    }
    @Suppress("UNCHECKED_CAST")
    return ArrayView(result as Array<VCalendar>)
}

/**
 * Returns the value of the first property of [key], or `null` when the property is missing or does not decode.
 *
 * Decoding happens on first access and is cached in the property. Use [decode] to learn why a value is `null`.
 *
 * @param key the typed key, such as `Properties.DTSTART`.
 */
public operator fun <T : Any> ICalComponent.get(key: PropertyKey<T>): T? = decode(key).valueOrNull()

/**
 * Returns the values of every property of [key], in source order; properties that do not decode are skipped.
 *
 * @param key the typed key, such as `Properties.ATTENDEE`.
 */
public fun <T : Any> ICalComponent.all(key: PropertyKey<T>): List<T> {
    var found: Array<Any?>? = null
    var count = 0
    for (property in propertyNodes) {
        if (!property.hasName(key.nameId, key.name)) continue
        val decoded = property.decodeWith(key.codec, DecodeContext.Lenient)
        if (decoded !is Decoded.Value) continue
        val array = found ?: arrayOfNulls<Any?>(propertyNodes.size).also { found = it }
        array[count++] = decoded.value
    }
    val array = found ?: return emptyList()
    @Suppress("UNCHECKED_CAST")
    return ArrayView(array.copyOf(count) as Array<T>)
}

/**
 * Decodes the first property of [key] and returns the outcome: the value with the problems that were repaired,
 * [Decoded.Missing], or [Decoded.Malformed] with the error.
 *
 * @param key the typed key, such as `Properties.DTSTART`.
 */
public fun <T : Any> ICalComponent.decode(key: PropertyKey<T>): Decoded<T> {
    for (property in propertyNodes) {
        if (property.hasName(key.nameId, key.name)) return property.decodeWith(key.codec, DecodeContext.Lenient)
    }
    return Decoded.Missing
}

/**
 * Decodes the first property named [name] into the type the default registry assigns to it; the text for an unknown
 * property.
 *
 * @param name the property name.
 */
public fun ICalComponent.decodeAny(name: String): Decoded<Any> = decodeAny(name, ICalRegistry.Default)

/**
 * Decodes the first property named [name] into the type that [registry] assigns to it; the text for a property it does
 * not know.
 *
 * @param name the property name.
 * @param registry what every property is.
 */
public fun ICalComponent.decodeAny(name: String, registry: ICalRegistry): Decoded<Any> {
    val property = property(name) ?: return Decoded.Missing
    val definition = registry.property(property.nameId, property.name, this.name)
        ?: return Decoded.Value(property.value, emptyList())
    @Suppress("UNCHECKED_CAST")
    return property.decodeWith(definition.key.codec as PropertyCodec<Any>, DecodeContext.Lenient)
}

/**
 * Returns the value of the parameter of [key], or `null` when the parameter is missing or does not decode.
 *
 * @param key the typed key, such as `Parameters.PARTSTAT`.
 */
public operator fun <T : Any> ICalProperty.get(key: ParameterKey<T>): T? {
    val parameter = parameter(key.name) ?: return null
    return key.codec.decode(this, parameter, DecodeContext.Lenient).valueOrNull()
}

internal fun <T : Any> ICalProperty.decodeWith(codec: PropertyCodec<T>, context: DecodeContext): Decoded<T> {
    @Suppress("UNCHECKED_CAST")
    val cached = cachedValue(codec, context) as Decoded<T>?
    if (cached != null) return cached
    val result = codec.decode(this, context)
    cacheValue(codec, context, result)
    return result
}

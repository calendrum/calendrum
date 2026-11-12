package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ICalWriter
import kotlin.jvm.JvmStatic

// Properties sorted by name and text, parameters by name, components by name and canonical text, written anew; values
// are kept as they are. uidV5 follows RFC 9562 §5.5; a namespace that is not a UUID becomes one in the DNS namespace
// first.
/** Canonical forms for comparing and hashing calendars, and name-based identifiers. */
public object Canonical {
    /**
     * Returns the canonical bytes of [calendar]: properties and parameters sorted, nothing else changed, written
     * canonically. Two calendars with the same content give the same bytes, whatever their order or spelling.
     *
     * @throws IllegalArgumentException if a property cannot be written canonically, as with [ICalWriter]: a control
     *   character other than HTAB in a value, or a parameter without a value (`ICAL-S012`). Parsing keeps both.
     */
    @JvmStatic
    public fun canonicalize(calendar: VCalendar): ByteArray =
        ICal.writer().encodeToByteArray(canonicalNode(calendar.component))

    /**
     * Returns a UUID version 5 text for [naturalKey] in [namespace]: the same inputs always give the same `UID`.
     *
     * @param namespace a UUID text, or any other text, which is mapped to a UUID in the DNS namespace.
     * @param naturalKey the key that identifies the item.
     */
    @JvmStatic
    public fun uidV5(namespace: String, naturalKey: String): String {
        val space = uuidBytes(namespace) ?: uuidV5Bytes(DNS_NAMESPACE, namespace.encodeToByteArray())
        return format(uuidV5Bytes(space, naturalKey.encodeToByteArray()))
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Canonical"
}

internal fun canonicalNode(component: ICalComponent, ignored: Set<String> = emptySet()): ICalComponent {
    val properties = component.properties.filter { it.name !in ignored }.map(::canonicalProperty)
        .sortedWith(compareBy<ICalProperty> { it.name }.thenBy { sortKey(it) })
    val children = component.components.map { canonicalNode(it, ignored) }
        .map { it to ICal.writer().encodeToString(it) }
        .sortedWith(compareBy<Pair<ICalComponent, String>> { it.first.name }.thenBy { it.second })
    val builder = ICalComponent.builder(component.name)
    for (property in properties) builder.property(property)
    for ((child, _) in children) builder.component(child)
    return builder.build()
}

private fun canonicalProperty(property: ICalProperty): ICalProperty {
    val builder = ICalProperty.builder(property.name)
    for (parameter in property.parameters.sortedBy { it.name }) {
        builder.parameter(ICalParameter.of(parameter.name, *parameter.values.toTypedArray()))
    }
    return builder.value(property.value).build()
}

private fun sortKey(property: ICalProperty): String = buildString {
    for (parameter in property.parameters) {
        append(parameter.name).append('=').append(parameter.values.joinToString(",")).append(';')
    }
    append(':').append(property.value)
}

private val DNS_NAMESPACE = uuidBytes("6ba7b810-9dad-11d1-80b4-00c04fd430c8")!!

private fun uuidV5Bytes(namespace: ByteArray, name: ByteArray): ByteArray {
    val hash = sha1(namespace + name)
    val bytes = hash.copyOf(16)
    bytes[6] = ((bytes[6].toInt() and 0x0F) or 0x50).toByte()
    bytes[8] = ((bytes[8].toInt() and 0x3F) or 0x80).toByte()
    return bytes
}

private fun uuidBytes(text: String): ByteArray? {
    if (text.length != 36) return null
    val bytes = ByteArray(16)
    var at = 0
    for (index in text.indices) {
        if (index == 8 || index == 13 || index == 18 || index == 23) {
            if (text[index] != '-') return null
            continue
        }
        val digit = text[index].digitToIntOrNull(16) ?: return null
        bytes[at / 2] = (bytes[at / 2].toInt() or (digit shl (if (at % 2 == 0) 4 else 0))).toByte()
        at += 1
    }
    return bytes
}

private fun format(bytes: ByteArray): String = buildString {
    for (index in bytes.indices) {
        if (index == 4 || index == 6 || index == 8 || index == 10) append('-')
        val value = bytes[index].toInt() and 0xFF
        append(HEX[value shr 4]).append(HEX[value and 15])
    }
}

private const val HEX = "0123456789abcdef"

@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.values

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.value.ValueCodecs

// Values of a corpus in one string with start and end offsets, so that the benchmark decodes ranges and measures
// neither parsing nor substrings.
internal class CorpusValues(
    val text: String,
    val types: IntArray,
    val tzids: Array<String?>,
    val starts: IntArray,
    val ends: IntArray,
    val names: Array<String>,
) {
    val size: Int get() = types.size

    fun decode(codecs: ValueCodecs, index: Int): Any? {
        val type = types[index]
        if (type != GEO_TYPE) return codecs.decode(type, text, starts[index], ends[index], tzids[index])
        var separator = starts[index]
        while (separator < ends[index] && text[separator] != ';') separator += 1
        if (separator == ends[index]) return codecs.decode(ValueCodecs.FLOAT, text, starts[index], separator, null)
        val latitude = codecs.decode(ValueCodecs.FLOAT, text, starts[index], separator, null) ?: return null
        val longitude = codecs.decode(ValueCodecs.FLOAT, text, separator + 1, ends[index], null) ?: return null
        return listOf(latitude, longitude)
    }
}

internal val BENCHMARK_PROPERTIES: Set<String> =
    setOf("DTSTART", "DTEND", "EXDATE", "RRULE", "DURATION", "TRIGGER", "ATTENDEE")

internal fun corpusValues(bytes: ByteArray, properties: Set<String>?): CorpusValues {
    val reader = ICal.parser().reader(bytes)
    val text = StringBuilder()
    val types = ArrayList<Int>()
    val tzids = ArrayList<String?>()
    val starts = ArrayList<Int>()
    val ends = ArrayList<Int>()
    val names = ArrayList<String>()
    while (true) {
        val event = reader.next() ?: break
        if (event != ContentEventType.Property) continue
        val name = reader.name
        if (properties != null && name !in properties) continue
        var valueParameter: String? = null
        var tzid: String? = null
        for (index in 0 until reader.parameterCount) {
            when (reader.parameterName(index)) {
                "VALUE" -> valueParameter = reader.parameterValues(index).firstOrNull()
                "TZID" -> tzid = reader.parameterValues(index).firstOrNull()
            }
        }
        val type = valueType(name, valueParameter)
        if (type == NO_TYPE) continue
        types += type
        tzids += tzid
        names += name
        starts += text.length
        text.append(reader.value)
        ends += text.length
    }
    return CorpusValues(
        text.toString(),
        types.toIntArray(),
        tzids.toTypedArray(),
        starts.toIntArray(),
        ends.toIntArray(),
        names.toTypedArray(),
    )
}

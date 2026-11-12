package io.github.calendrum.benchmarks.cursor

import io.github.calendrum.benchmarks.reader.CorpusCounts
import io.github.calendrum.benchmarks.reader.countedComponents
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ICalParser

// Properties, counted components and implicit ends seen by the cursor, compared with the reference of
// `reader/CorpusCounts.kt`.
internal class CursorCounts(val properties: Int, val components: Map<String, Int>, val implicitEnds: Int)

internal fun cursorCounts(parser: ICalParser, bytes: ByteArray): CursorCounts {
    val reader = parser.reader(bytes)
    var properties = 0
    var implicitEnds = 0
    val components = HashMap<String, Int>()
    while (true) {
        when (reader.next() ?: break) {
            ContentEventType.Begin -> components[reader.name] = (components[reader.name] ?: 0) + 1
            ContentEventType.Property -> properties++
            ContentEventType.End -> if (reader.isImplicitEnd) implicitEnds++
        }
    }
    return CursorCounts(properties, countedComponents.associateWith { components[it] ?: 0 }, implicitEnds)
}

internal fun CursorCounts.difference(expected: CorpusCounts): String? = when {
    properties != expected.properties -> "properties: $properties, expected ${expected.properties}"
    components != expected.components -> "components: $components, expected ${expected.components}"
    implicitEnds != 0 -> "$implicitEnds implicit ends"
    else -> null
}

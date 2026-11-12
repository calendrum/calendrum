@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.reader

import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.SplitMix64
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ContentSelection
import io.github.calendrum.syntax.ParseLimits
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class Mask(
    val components: Set<String>,
    val properties: Set<String>,
    val unknownComponents: Boolean,
    val unknownProperties: Boolean,
) {
    fun selection(): ContentSelection = ContentSelection.Builder()
        .components(*components.toTypedArray())
        .properties(*properties.toTypedArray())
        .unknownComponents(unknownComponents)
        .unknownProperties(unknownProperties)
        .build()

    fun wantsComponent(reader: ContentReader): Boolean {
        if (reader.componentId == ContentReader.UNKNOWN_NAME) return unknownComponents
        val name = reader.componentName()
        return name == "VCALENDAR" || name in components
    }

    fun wantsProperty(reader: ContentReader): Boolean {
        val line = reader.line
        if (line.nameId == ContentReader.UNKNOWN_NAME) return unknownProperties
        return line.name() in properties
    }

    override fun toString(): String =
        "Mask(components=$components, properties=$properties, unknown=$unknownComponents/$unknownProperties)"

    companion object {
        val schedule: Mask = Mask(scheduleComponents.toSet(), scheduleProperties.toSet(), false, false)
        val syncMetadata: Mask = Mask(syncMetadataComponents.toSet(), syncMetadataProperties.toSet(), false, false)

        fun random(random: SplitMix64, components: List<String>, properties: List<String>): Mask = Mask(
            components = components.filter { random.nextInt(2) == 0 }.toSet(),
            properties = properties.filter { random.nextInt(3) == 0 }.toSet(),
            unknownComponents = random.nextInt(2) == 0,
            unknownProperties = random.nextInt(2) == 0,
        )
    }
}

internal class KnownNamesSeen(val components: List<String>, val properties: List<String>)

internal fun knownNamesIn(bytes: ByteArray): KnownNamesSeen {
    val reader = ContentReader(bytes, ContentSelection.Full, ParseLimits.Default) { }
    val components = LinkedHashSet<String>()
    val properties = LinkedHashSet<String>()
    while (true) {
        when (reader.next()) {
            ContentReader.END_OF_INPUT -> break

            ContentReader.BEGIN -> if (reader.componentId !=
                ContentReader.UNKNOWN_NAME
            ) {
                components += reader.componentName()
            }

            ContentReader.PROPERTY -> if (reader.line.nameId !=
                ContentReader.UNKNOWN_NAME
            ) {
                properties += reader.line.name()
            }
        }
    }
    return KnownNamesSeen(components.toList(), properties.toList())
}

// The main invariant of selective parsing: the stream with a selection equals the Full stream filtered by the same
// selection. Both readers run in lockstep, so a 16 MB corpus is compared without keeping events in memory. The filter
// is written here from the rules, not taken from ContentSelection.
internal fun assertSelectionMatchesFilter(bytes: ByteArray, mask: Mask): Int {
    val full = ContentReader(bytes, ContentSelection.Full, ParseLimits.Default) { }
    val masked = ContentReader(bytes, mask.selection(), ParseLimits.Default) { }
    val visible = ArrayList<Boolean>()
    var compared = 0
    while (true) {
        val kind = full.next()
        val parentVisible = visible.isEmpty() || visible.last()
        val expected = when (kind) {
            ContentReader.END_OF_INPUT -> true
            ContentReader.BEGIN -> (parentVisible && mask.wantsComponent(full)).also { visible += it }
            ContentReader.END -> visible.removeAt(visible.lastIndex)
            else -> parentVisible && mask.wantsProperty(full)
        }
        if (!expected) continue
        assertEquals(kind, masked.next(), "event kind after $compared events with $mask")
        assertSameEvent(full, masked, compared, mask)
        compared++
        if (kind == ContentReader.END_OF_INPUT) break
    }
    return compared
}

private fun assertSameEvent(full: ContentReader, masked: ContentReader, index: Int, mask: Mask) {
    val where = "event #$index with $mask"
    if (full.kind == ContentReader.END_OF_INPUT) return
    assertEquals(full.depth, masked.depth, where)
    assertEquals(full.offset, masked.offset, where)
    assertEquals(full.lineNumber, masked.lineNumber, where)
    assertEquals(full.column, masked.column, where)
    if (full.kind == ContentReader.PROPERTY) {
        val expected = full.line
        val actual = masked.line
        assertEquals(expected.nameId, actual.nameId, where)
        assertEquals(expected.name(), actual.name(), where)
        assertEquals(expected.startOffset, actual.startOffset, where)
        assertEquals(expected.endOffset, actual.endOffset, where)
        assertEquals(expected.line, actual.line, where)
        assertEquals(expected.column, actual.column, where)
        assertEquals(expected.parameterCount, actual.parameterCount, where)
        for (p in 0 until expected.parameterCount) {
            assertEquals(expected.parameterNameId(p), actual.parameterNameId(p), where)
        }
        val length = expected.valueEnd - expected.valueStart
        assertEquals(length, actual.valueEnd - actual.valueStart, where)
        for (k in 0 until length) {
            if (expected.bytes[expected.valueStart + k] != actual.bytes[actual.valueStart + k]) {
                assertTrue(false, "value bytes differ at $k, $where")
            }
        }
    } else {
        assertEquals(full.componentId, masked.componentId, where)
        assertEquals(full.componentName(), masked.componentName(), where)
        assertEquals(full.isImplicit, masked.isImplicit, where)
        assertEquals(full.componentStartOffset, masked.componentStartOffset, where)
        assertEquals(full.componentEndOffset, masked.componentEndOffset, where)
    }
}

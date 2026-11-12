package io.github.calendrum.syntax

import io.github.calendrum.Problem
import kotlin.test.assertEquals

internal data class Event(
    val kind: Int,
    val depth: Int,
    val componentId: Int,
    val componentName: String,
    val implicit: Boolean,
    val componentStart: Int,
    val componentEnd: Int,
    val offset: Int,
    val line: Int,
    val column: Int,
    val nameId: Int,
    val parameterIds: List<Int>,
    val value: List<Byte>,
    val property: Lexed?,
)

internal class ReadResult(val events: List<Event>, val problems: List<Problem>, val reader: ContentReader) {
    val codes: List<String> get() = problems.map { it.code.id }

    val kinds: List<Int> get() = events.map { it.kind }

    fun names(): List<String> = events.map { it.property?.name ?: it.componentName }
}

internal fun ContentReader.snapshot(): Event {
    val property = kind == ContentReader.PROPERTY
    val line = line
    return Event(
        kind = kind,
        depth = depth,
        componentId = componentId,
        componentName = componentName(),
        implicit = isImplicit,
        componentStart = componentStartOffset.toInt(),
        componentEnd = componentEndOffset.toInt(),
        offset = offset,
        line = lineNumber,
        column = column,
        nameId = if (property) line.nameId else KnownNames.UNKNOWN,
        parameterIds = if (property) (0 until line.parameterCount).map { line.parameterNameId(it) } else emptyList(),
        value = if (property) line.bytes.copyOfRange(line.valueStart, line.valueEnd).toList() else emptyList(),
        property = if (property) line.snapshot() else null,
    )
}

internal fun read(
    input: ByteArray,
    selection: ContentSelection = ContentSelection.Full,
    limits: ParseLimits = ParseLimits.Default,
): ReadResult {
    val problems = mutableListOf<Problem>()
    val reader = ContentReader(input, selection, limits) { problems += it }
    val events = mutableListOf<Event>()
    var calls = 0
    while (true) {
        calls++
        check(calls <= 4 * input.size + 8) { "reader does not terminate" }
        if (reader.next() == ContentReader.END_OF_INPUT) break
        events += reader.snapshot()
    }
    return ReadResult(events, problems, reader)
}

internal fun read(text: String, selection: ContentSelection = ContentSelection.Full): ReadResult =
    read(text.encodeToByteArray(), selection)

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

    fun wantsComponent(id: Int): Boolean {
        if (id == KnownNames.VCALENDAR) return true
        if (id < 0 || KnownNames.kinds(id) and KnownNames.COMPONENT == 0) return unknownComponents
        return KnownNames.name(id) in components
    }

    fun wantsProperty(id: Int): Boolean {
        if (id < 0 || KnownNames.kinds(id) and KnownNames.PROPERTY == 0) return unknownProperties
        return KnownNames.name(id) in properties
    }

    override fun toString(): String =
        "Mask(components=$components, properties=$properties, unknown=$unknownComponents/$unknownProperties)"

    companion object {
        val componentNames: List<String> =
            (0 until KnownNames.count).filter { KnownNames.kinds(it) and KnownNames.COMPONENT != 0 }
                .map { KnownNames.name(it) }
        val propertyNames: List<String> =
            (0 until KnownNames.count)
                .filter { KnownNames.kinds(it) and KnownNames.PROPERTY != 0 }
                .filter { it != KnownNames.BEGIN && it != KnownNames.END }
                .map { KnownNames.name(it) }

        val schedule: Mask = Mask(
            components = setOf("VEVENT", "VTIMEZONE", "STANDARD", "DAYLIGHT"),
            properties = setOf(
                "UID", "DTSTART", "DTEND", "DURATION", "SUMMARY", "LOCATION", "DESCRIPTION", "RRULE", "RDATE",
                "EXDATE", "RECURRENCE-ID", "STATUS", "SEQUENCE", "LAST-MODIFIED", "TZID", "TZOFFSETFROM",
                "TZOFFSETTO",
            ),
            unknownComponents = false,
            unknownProperties = false,
        )

        val syncMetadata: Mask = Mask(
            components = setOf("VEVENT"),
            properties = setOf("UID", "SEQUENCE", "LAST-MODIFIED", "DTSTAMP", "RECURRENCE-ID"),
            unknownComponents = false,
            unknownProperties = false,
        )

        val nothing: Mask = Mask(emptySet(), emptySet(), unknownComponents = false, unknownProperties = false)

        val onlyUnknown: Mask = Mask(emptySet(), emptySet(), unknownComponents = true, unknownProperties = true)

        val fixed: List<Mask> = listOf(schedule, syncMetadata, nothing, onlyUnknown)

        fun random(random: TestRandom): Mask = Mask(
            components = componentNames.filter { random.nextInt(2) == 0 }.toSet(),
            properties = propertyNames.filter { random.nextInt(3) == 0 }.toSet(),
            unknownComponents = random.nextInt(2) == 0,
            unknownProperties = random.nextInt(2) == 0,
        )
    }
}

// Reference implementation of the selection rules for the invariant tests, independent of `ContentSelection`: a
// component is emitted when its parent is and it is VCALENDAR or selected; a property when its component is emitted and
// it is selected; unknown names, and known names of the wrong kind, follow the flags.
internal fun filter(full: List<Event>, mask: Mask): List<Event> {
    val visible = ArrayList<Boolean>()
    val result = ArrayList<Event>()
    for (event in full) {
        val parentVisible = visible.isEmpty() || visible.last()
        when (event.kind) {
            ContentReader.BEGIN -> {
                val shown = parentVisible && mask.wantsComponent(event.componentId)
                visible += shown
                if (shown) result += event
            }

            ContentReader.END -> if (visible.removeAt(visible.lastIndex)) result += event

            else -> if (parentVisible && mask.wantsProperty(event.nameId)) result += event
        }
    }
    return result
}

internal fun assertSelectionMatchesFilter(input: ByteArray, mask: Mask, limits: ParseLimits = ParseLimits.Default) {
    val full = read(input, ContentSelection.Full, limits)
    val masked = read(input, mask.selection(), limits)
    val expected = filter(full.events, mask)
    assertEquals(expected.size, masked.events.size, "event count with $mask")
    for (index in expected.indices) {
        assertEquals(expected[index], masked.events[index], "event #$index with $mask")
    }
}

internal class TestRandom(private var state: Long) {
    fun nextLong(): Long {
        state += -0x61c8864680b583ebL
        var z = state
        z = (z xor (z ushr 30)) * -0x40a7b892e31b1a47L
        z = (z xor (z ushr 27)) * -0x6b2fb644ecceee15L
        return z xor (z ushr 31)
    }

    fun nextInt(bound: Int): Int = ((nextLong() ushr 33) % bound).toInt()
}

@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.InternalICalApi
import io.github.calendrum.syntax.ContentLine
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ContentSelection
import io.github.calendrum.syntax.ParseLimits

class ReaderFuzzTest {
    // The same input is read twice with the same limits: with `ContentSelection.Full` and with the fuzzed selection.
    // Both streams are checked on their own, then the selected stream must equal the full one filtered by the same
    // selection. `detect` switches both reads to the constructor that detects the encoding (no hint), so the selection
    // invariant also covers the lazy decision: skipped lines must see the first non-ASCII byte too, or offsets and
    // decoded values would differ.
    @FuzzTest
    @DictionaryFile(resourcePath = "ics.dict")
    fun reader(data: FuzzedDataProvider) {
        val limits = data.consumeLimits()
        val mask = data.consumeMask()
        val detect = data.consumeBoolean()
        val input = data.consumeRemainingAsBytes()
        val decoded = decodedText(input, detect)
        val full = read(input, decoded, detect, ContentSelection.Full, limits)
        val selected = read(input, decoded, detect, mask.selection(), limits)
        val expected = mask.filter(full)
        invariant(expected.size == selected.size) { "${selected.size} events with $mask, expected ${expected.size}" }
        for (index in expected.indices) {
            invariant(expected[index] == selected[index]) {
                "event #$index with $mask: ${selected[index]}, expected ${expected[index]}"
            }
        }
    }

    // A stack of open BEGIN events checks the balance: every END (explicit or implicit) closes the top BEGIN with the
    // same id, name and start offset, and nothing stays open at the end. The call bound catches a loop that the
    // libFuzzer timeout would report later.
    private fun read(
        input: ByteArray,
        decoded: ByteArray,
        detect: Boolean,
        selection: ContentSelection,
        limits: ParseLimits,
    ): List<Event> {
        val problems = ProblemLog(decoded)
        val positions = Positions(decoded)
        val reader = if (detect) {
            ContentReader(input, null, selection, limits, problems)
        } else {
            ContentReader(input, selection, limits, problems)
        }
        val events = ArrayList<Event>()
        val open = ArrayList<Event>()
        var begins = 0
        var calls = 0
        while (true) {
            calls++
            invariant(calls <= 4 * decoded.size + 8) { "the reader does not terminate" }
            val kind = reader.next()
            if (kind == ContentReader.END_OF_INPUT) break
            val event = reader.snapshot()
            positions.check(event, event.offset, event.line, event.column)
            invariant(event.depth in 0..limits.maxDepth) { "depth of $event" }
            when (kind) {
                ContentReader.BEGIN -> {
                    begins++
                    invariant(begins <= limits.maxComponents) {
                        "$begins components with limit ${limits.maxComponents}"
                    }
                    invariant(event.depth == open.size + 1 && !event.implicit) { "BEGIN $event at depth ${open.size}" }
                    invariant(event.componentStart in 0 until decoded.size && event.componentEnd == -1) {
                        "BEGIN offsets of $event"
                    }
                    open += event
                }

                ContentReader.END -> {
                    val begin = open.removeLastOrNull() ?: throw AssertionError("END without BEGIN: $event")
                    invariant(event.depth == begin.depth && event.componentId == begin.componentId) {
                        "END $event closes $begin"
                    }
                    invariant(event.componentName == begin.componentName) { "END $event closes $begin" }
                    invariant(event.componentStart == begin.componentStart) { "END $event closes $begin" }
                    invariant(event.componentEnd > event.componentStart && event.componentEnd <= decoded.size) {
                        "END offsets of $event"
                    }
                }

                ContentReader.PROPERTY -> {
                    invariant(event.depth == open.size && event.property != null) { "PROPERTY $event at ${open.size}" }
                    val parent = open.lastOrNull()
                    invariant(event.componentId == (parent?.componentId ?: ContentReader.UNKNOWN_NAME)) {
                        "PROPERTY $event in $parent"
                    }
                    invariant(event.componentStart == -1 && event.componentEnd == -1) { "PROPERTY offsets of $event" }
                    checkLineRanges(reader.line)
                    positions.check(reader.line, reader.line.startOffset, reader.line.line, reader.line.column)
                }

                else -> invariant(false) { "unknown event kind $kind" }
            }
            events += event
        }
        invariant(open.isEmpty()) { "unbalanced: ${open.size} components left open" }
        invariant(reader.next() == ContentReader.END_OF_INPUT) { "next() after the end returned an event" }
        problems.check(limits)
        invariant(!(problems.has(UNCLOSED) && (problems.has(TOO_MANY_LINES) || problems.has(TOO_MANY_COMPONENTS)))) {
            "$UNCLOSED together with a truncation"
        }
        return events
    }

    private fun ContentReader.snapshot(): Event {
        val property = if (kind == ContentReader.PROPERTY) line.snapshot() else null
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
            property = property,
        )
    }

    private fun ContentLine.snapshot(): PropertyLine = PropertyLine(
        nameId = nameId,
        name = name(),
        parameters = (0 until parameterCount).map { parameter ->
            listOf(parameterNameId(parameter).toString(), parameterName(parameter)) +
                (0 until parameterValueCount(parameter)).map { parameterValue(parameter, it) }
        },
        value = bytes.copyOfRange(valueStart, valueEnd).toList(),
        rawValue = rawValue(),
        startOffset = startOffset,
        endOffset = endOffset,
        line = line,
        column = column,
    )

    private data class Event(
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
        val property: PropertyLine?,
    )

    private data class PropertyLine(
        val nameId: Int,
        val name: String,
        val parameters: List<List<String>>,
        val value: List<Byte>,
        val rawValue: String,
        val startOffset: Int,
        val endOffset: Int,
        val line: Int,
        val column: Int,
    )

    private class Mask(
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

        // Reference filter, as in the commonTest support: VCALENDAR is always shown, a component is shown only if its
        // parent is, and a name that is not a known component or property counts as unknown. Whether a known name is a
        // component or a property is asked from `ContentSelection.Builder`, which rejects names of the wrong kind, so
        // the fuzz module does not copy the table of known names; the lists below are only candidates for random masks.
        fun filter(events: List<Event>): List<Event> {
            val visible = ArrayList<Boolean>()
            val result = ArrayList<Event>()
            for (event in events) {
                val parentVisible = visible.isEmpty() || visible.last()
                when (event.kind) {
                    ContentReader.BEGIN -> {
                        val shown = parentVisible && wantsComponent(event.componentId, event.componentName)
                        visible += shown
                        if (shown) result += event
                    }

                    ContentReader.END -> if (visible.removeAt(visible.lastIndex)) result += event

                    else -> {
                        val property = event.property!!
                        if (parentVisible && wantsProperty(property.nameId, property.name)) result += event
                    }
                }
            }
            return result
        }

        private fun wantsComponent(id: Int, name: String): Boolean = when {
            id < 0 || !isKnownComponent(name) -> unknownComponents
            else -> name == VCALENDAR || name in components
        }

        private fun wantsProperty(id: Int, name: String): Boolean =
            if (id < 0 || !isKnownProperty(name)) unknownProperties else name in properties

        override fun toString(): String =
            "Mask(components=$components, properties=$properties, unknown=$unknownComponents/$unknownProperties)"
    }

    // Four fixed masks (schedule, sync metadata, nothing, only unknown names) or random bits for every known component
    // and property.
    private fun FuzzedDataProvider.consumeMask(): Mask {
        val preset = consumeInt(0, 5)
        if (preset < PRESETS.size) return PRESETS[preset]
        val componentBits = consumeLong()
        val propertyBits = longArrayOf(consumeLong(), consumeLong())
        return Mask(
            components = COMPONENTS.filterIndexed { index, _ -> componentBits ushr index and 1L != 0L }.toSet(),
            properties = PROPERTIES.filterIndexed { index, _ -> propertyBits[index / 64] ushr index and 1L != 0L }
                .toSet(),
            unknownComponents = consumeBoolean(),
            unknownProperties = consumeBoolean(),
        )
    }

    private companion object {
        const val VCALENDAR = "VCALENDAR"
        const val UNCLOSED = "ICAL-S018"
        const val TOO_MANY_LINES = "ICAL-S014"
        const val TOO_MANY_COMPONENTS = "ICAL-S023"

        val COMPONENTS = listOf(
            "VCALENDAR", "VEVENT", "VTODO", "VJOURNAL", "VFREEBUSY", "VTIMEZONE", "STANDARD", "DAYLIGHT", "VALARM",
            "VAVAILABILITY", "AVAILABLE", "PARTICIPANT", "VLOCATION", "VRESOURCE",
        )

        val PROPERTIES = listOf(
            "CALSCALE", "METHOD", "PRODID", "VERSION", "ATTACH", "CATEGORIES", "CLASS", "COMMENT", "DESCRIPTION",
            "GEO", "LOCATION", "PERCENT-COMPLETE", "PRIORITY", "RESOURCES", "STATUS", "SUMMARY", "COMPLETED",
            "DTEND", "DUE", "DTSTART", "DURATION", "FREEBUSY", "TRANSP", "TZID", "TZNAME", "TZOFFSETFROM",
            "TZOFFSETTO", "TZURL", "ATTENDEE", "CONTACT", "ORGANIZER", "RECURRENCE-ID", "RELATED-TO", "URL", "UID",
            "EXDATE", "RDATE", "RRULE", "ACTION", "REPEAT", "TRIGGER", "CREATED", "DTSTAMP", "LAST-MODIFIED",
            "SEQUENCE", "REQUEST-STATUS", "EXRULE", "NAME", "REFRESH-INTERVAL", "SOURCE", "COLOR", "IMAGE",
            "CONFERENCE", "BUSYTYPE", "LOCATION-TYPE", "PARTICIPANT-TYPE", "RESOURCE-TYPE", "CALENDAR-ADDRESS",
            "STYLED-DESCRIPTION", "STRUCTURED-DATA", "ACKNOWLEDGED", "PROXIMITY", "CONCEPT", "LINK", "REFID",
            "X-WR-CALNAME", "X-WR-CALDESC", "X-WR-TIMEZONE",
        )

        val PRESETS = listOf(
            Mask(
                components = setOf("VEVENT", "VTIMEZONE", "STANDARD", "DAYLIGHT"),
                properties = setOf("UID", "DTSTART", "DTEND", "SUMMARY", "RRULE", "TZID", "TZOFFSETTO"),
                unknownComponents = false,
                unknownProperties = false,
            ),
            Mask(
                components = setOf("VEVENT"),
                properties = setOf("UID", "SEQUENCE", "DTSTAMP"),
                unknownComponents = false,
                unknownProperties = false,
            ),
            Mask(emptySet(), emptySet(), unknownComponents = false, unknownProperties = false),
            Mask(emptySet(), emptySet(), unknownComponents = true, unknownProperties = true),
        )

        private val componentKinds = HashMap<String, Boolean>()
        private val propertyKinds = HashMap<String, Boolean>()

        fun isKnownComponent(name: String): Boolean = componentKinds.getOrPut(name) {
            runCatching { ContentSelection.Builder().components(name) }.isSuccess
        }

        fun isKnownProperty(name: String): Boolean = propertyKinds.getOrPut(name) {
            runCatching { ContentSelection.Builder().properties(name) }.isSuccess
        }
    }
}

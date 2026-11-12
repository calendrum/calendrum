package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalReader
import io.github.calendrum.syntax.WriteOptions

class TreeRoundTripFuzzTest {
    // Any bytes, parsed leniently; the tree is written byte-stably, canonically and with time zones first, and the
    // cursor reads the same input.
    @FuzzTest
    @DictionaryFile(resourcePath = "ics.dict")
    fun treeRoundTrip(input: ByteArray) {
        val result = ICal.parse(input)
        val tree = result.components
        val invalidUtf8 = result.problemCount(ProblemCode.InvalidUtf8) > 0
        checkByteStable(tree, invalidUtf8)
        checkCanonical(tree)
        checkTimeZonesFirst(tree)
        checkCursor(input, tree)
    }

    // Bytes and text must parse back into an equal tree. The bytes keep invalid UTF-8 as it was, and the charset
    // decision on such mixed input depends on the whole input: lines the parser dropped can change it. So a different
    // decision is allowed only when the input had ICAL-S006; the text is parsed as a string and never decided.
    private fun checkByteStable(tree: List<ICalComponent>, invalidUtf8: Boolean) {
        val bytes = BYTE_STABLE.encodeToByteArray(tree)
        checkLineEnds(bytes)
        val reparsed = ICal.parse(bytes)
        if (reparsed.charset == "UTF-8") {
            invariant(reparsed.components == tree) { "byte-stable bytes read back as a different tree" }
        } else {
            invariant(invalidUtf8) { "byte-stable output of valid UTF-8 read back as ${reparsed.charset}" }
        }
        val text = BYTE_STABLE.encodeToString(tree)
        invariant(ICal.parse(text).components == tree) { "byte-stable text read back as a different tree" }
    }

    // IllegalArgumentException exactly when the tree has something without a strict form (`canonicallyWritable`:
    // invalid names, a parameter without values, control characters). Otherwise the output is folded correctly, parses
    // without problems into an equal tree and is written the same again.
    private fun checkCanonical(tree: List<ICalComponent>) {
        val writable = canonicallyWritable(tree)
        val bytes = try {
            CANONICAL.encodeToByteArray(tree)
        } catch (e: IllegalArgumentException) {
            invariant(!writable) { "canonical writing rejected a writable tree: ${e.message}" }
            invariant(runCatching { CANONICAL.encodeToString(tree) }.isFailure) { "only the text output succeeded" }
            return
        }
        invariant(writable) { "canonical writing accepted a tree that cannot be written strictly" }
        invariant(bytes.contentEquals(CANONICAL.encodeToString(tree).encodeToByteArray())) {
            "canonical bytes differ from the UTF-8 of the text"
        }
        checkFolding(bytes)
        val reparsed = ICal.parse(bytes)
        invariant(reparsed.problems.isEmpty()) { "canonical output has problems: ${reparsed.problems}" }
        invariant(reparsed.charset == "UTF-8" && reparsed.components == tree) {
            "canonical output read back as a different tree"
        }
        invariant(CANONICAL.encodeToByteArray(reparsed.components).contentEquals(bytes)) {
            "canonical writing is not idempotent"
        }
    }

    private fun checkTimeZonesFirst(tree: List<ICalComponent>) {
        val written = ICal.parse(ZONES_FIRST.encodeToString(tree)).components.map { content(it, zonesFirst = false) }
        invariant(written == tree.map { content(it, zonesFirst = true) }) {
            "time zones first: not the tree with VTIMEZONE first inside every VCALENDAR"
        }
    }

    private fun content(component: ICalComponent, zonesFirst: Boolean): NodeShape {
        val children = if (zonesFirst && component.name == "VCALENDAR") {
            component.components.sortedBy { it.name != "VTIMEZONE" }
        } else {
            component.components
        }
        return NodeShape(
            component.name,
            null,
            component.properties.map { property ->
                PropertyShape(property.name, property.parameters.map { it.name to it.values }, property.value, null)
            },
            children.map { content(it, zonesFirst) },
        )
    }

    // The cursor gives the tree of `parse`: names, parameters, values (the view and `valueString`), spans and depths;
    // then `readComponent()` on every top-level Begin gives the same components.
    private fun checkCursor(input: ByteArray, tree: List<ICalComponent>) {
        val expected = tree.map { shape(it) }
        val events = cursorShapes(ICal.parser().reader(input))
        invariant(events == expected) { "cursor events differ from the tree" }
        val reader = ICal.parser().reader(input)
        val read = ArrayList<ICalComponent>()
        while (true) {
            val type = reader.next() ?: break
            invariant(type == ContentEventType.Begin && reader.depth == 1) { "top-level event $type" }
            read += reader.readComponent()
            invariant(reader.depth == 1 && reader.name == read.last().name) {
                "readComponent left the cursor elsewhere"
            }
        }
        invariant(read.map { shape(it) } == expected) { "readComponent built a different tree" }
    }

    private fun checkLineEnds(bytes: ByteArray) {
        for (index in bytes.indices) {
            val byte = bytes[index]
            if (byte == CR) invariant(index + 1 < bytes.size && bytes[index + 1] == LF) { "bare CR at $index" }
            if (byte == LF) invariant(index > 0 && bytes[index - 1] == CR) { "bare LF at $index" }
        }
        invariant(bytes.isEmpty() || (bytes.size >= 2 && bytes[bytes.size - 1] == LF)) { "no CRLF at the end" }
    }

    private fun checkFolding(bytes: ByteArray) {
        var start = 0
        while (start < bytes.size) {
            var end = start
            while (end < bytes.size && bytes[end] != CR) end++
            invariant(end + 1 < bytes.size && bytes[end + 1] == LF) { "physical line at $start does not end with CRLF" }
            invariant(end - start <= LIMIT) { "physical line at $start has ${end - start} octets" }
            val next = end + 2
            if (next < bytes.size && bytes[next] == SPACE) {
                invariant(next + 1 < bytes.size && bytes[next + 1].toInt() and 0xC0 != 0x80) {
                    "fold at $end splits a UTF-8 sequence"
                }
            }
            start = next
        }
    }

    private fun canonicallyWritable(tree: List<ICalComponent>): Boolean {
        val stack = ArrayList(tree)
        while (stack.isNotEmpty()) {
            val component = stack.removeAt(stack.lastIndex)
            if (!isName(component.name)) return false
            for (property in component.properties) {
                if (!isName(property.name) || !isPropertyValue(property.value)) return false
                for (parameter in property.parameters) {
                    if (!isName(parameter.name) || parameter.values.isEmpty()) return false
                    if (!parameter.values.all(::isParameterValue)) return false
                }
            }
            stack.addAll(component.components)
        }
        return true
    }

    private fun isName(value: String): Boolean =
        value.isNotEmpty() && value.all { it == '-' || it in '0'..'9' || it in 'A'..'Z' || it in 'a'..'z' }

    private fun isPropertyValue(value: String): Boolean = value.none { (it < ' ' && it != '\t') || it == '\u007F' }

    private fun isParameterValue(value: String): Boolean =
        value.none { it != '\r' && it != '\n' && ((it < ' ' && it != '\t') || it == '\u007F') }

    private data class PropertyShape(
        val name: String,
        val parameters: List<Pair<String, List<String>>>,
        val value: String,
        val span: SourceSpan?,
    )

    private data class NodeShape(
        val name: String,
        val start: Triple<Long, Int, Int>?,
        val properties: List<PropertyShape>,
        val children: List<NodeShape>,
    )

    private fun shape(component: ICalComponent): NodeShape = NodeShape(
        component.name,
        component.span?.let { Triple(it.startOffset, it.line, it.column) },
        component.properties.map { property ->
            val parameters = property.parameters.map { it.name to it.values }
            PropertyShape(property.name, parameters, property.value, property.span)
        },
        component.components.map { shape(it) },
    )

    private fun cursorShapes(reader: ICalReader): List<NodeShape> {
        class Open(val name: String, val start: Triple<Long, Int, Int>) {
            val properties = ArrayList<PropertyShape>()
            val children = ArrayList<NodeShape>()
        }
        val roots = ArrayList<NodeShape>()
        val stack = ArrayList<Open>()
        while (true) {
            when (reader.next() ?: break) {
                ContentEventType.Begin -> {
                    invariant(reader.depth == stack.size + 1 && !reader.isImplicitEnd) {
                        "Begin at depth ${reader.depth}"
                    }
                    invariant(reader.span().startOffset == reader.offset) { "span of Begin" }
                    stack += Open(reader.name, Triple(reader.offset, reader.line, reader.column))
                }

                ContentEventType.Property -> {
                    invariant(reader.depth == stack.size && stack.isNotEmpty()) { "Property at depth ${reader.depth}" }
                    val parameters = List(reader.parameterCount) { index ->
                        reader.parameterName(index) to reader.parameterValues(index)
                    }
                    val value = reader.value.toString()
                    invariant(value == reader.valueString()) { "value view differs from valueString()" }
                    stack.last().properties += PropertyShape(reader.name, parameters, value, reader.span())
                }

                ContentEventType.End -> {
                    val open = stack.removeLastOrNull() ?: throw AssertionError("End without Begin")
                    invariant(reader.name == open.name && reader.depth == stack.size + 1) { "End closes ${open.name}" }
                    if (reader.isImplicitEnd) {
                        invariant(reader.span().endOffset == reader.offset) { "span of an implicit End" }
                    }
                    val node = NodeShape(open.name, open.start, open.properties, open.children)
                    if (stack.isEmpty()) roots += node else stack.last().children += node
                }
            }
        }
        invariant(stack.isEmpty()) { "unbalanced cursor events" }
        return roots
    }

    private companion object {
        const val LIMIT = 75

        val BYTE_STABLE = ICal.writer()
        val CANONICAL = ICal.writer(WriteOptions.builder().byteStable(false).build())
        val ZONES_FIRST = ICal.writer(WriteOptions.builder().timeZonesFirst(true).build())
    }
}

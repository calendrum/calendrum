package io.github.calendrum.syntax

import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.ProblemListener
import io.github.calendrum.Severity

// Keeps the reported problems (`keep`: `parse` always, the cursor only in the strict mode, where the exception needs
// them), forwards each to the listener of the user and, in the strict mode, throws at the first `Error` with everything
// reported so far.
internal class ProblemCollector(
    private val listener: ProblemListener?,
    private val strict: Boolean,
    private val keep: Boolean,
) : ProblemListener {
    val problems: ArrayList<Problem> = ArrayList()

    override fun onProblem(problem: Problem) {
        if (keep) problems += problem
        listener?.onProblem(problem)
        if (strict && problem.severity == Severity.Error) throw ICalParseException(problems)
    }
}

// One pass over `ContentReader`. Properties and child components of the open components wait on two shared stacks;
// `frames[depth]` remembers where each open component's part begins. END moves that part into exact-size arrays (a
// shared empty array when there is nothing) and pushes the new component onto the part of its parent. Properties at
// depth 0 (ICAL-S021) are not in the tree. `marks` runs beside the component stack: how many properties of the parent
// came before each child; a component whose properties came after a child (`interleaved`) keeps them as
// `childPositions`, so writing keeps the source order. `readComponent()` builds only the subtree of the current BEGIN
// of the reader and stops at its END; `rootDepth` marks its level, whose parent frame is not part of the build.
internal class TreeBuilder(
    private val reader: ContentReader,
    retainSource: Boolean,
    inputSize: Int,
    perComponent: Boolean,
) {
    private val arena = if (retainSource) null else Arena(inputSize, perComponent)
    private var properties = arrayOfNulls<ICalProperty>(INITIAL_PROPERTIES)
    private var propertyTop = 0
    private var components = arrayOfNulls<ICalComponent>(INITIAL_COMPONENTS)
    private var marks = IntArray(INITIAL_COMPONENTS)
    private var componentTop = 0
    private var frames = arrayOfNulls<Frame>(INITIAL_FRAMES)
    private var rootDepth = 1

    fun build(): Array<ICalComponent> {
        rootDepth = 1
        while (true) {
            when (reader.next()) {
                ContentReader.END_OF_INPUT -> break
                ContentReader.BEGIN -> begin()
                ContentReader.PROPERTY -> if (reader.depth > 0) property()
                else -> end()
            }
        }
        val result = take(components, 0, componentTop, EMPTY_COMPONENTS)
        val values = reader.values
        if (values != null) {
            for (component in result) values.component(component, reader)
        }
        return result
    }

    fun readComponent(): ICalComponent {
        val depth = reader.depth
        rootDepth = depth
        propertyTop = 0
        componentTop = 0
        arena?.startComponent()
        begin()
        while (true) {
            when (reader.next()) {
                ContentReader.BEGIN -> begin()

                ContentReader.PROPERTY -> property()

                ContentReader.END -> {
                    end()
                    if (reader.depth == depth) break
                }

                else -> throw IllegalStateException("The input ended inside a component")
            }
        }
        val component = components[0]!!
        components[0] = null
        componentTop = 0
        reader.values?.component(component, reader)
        return component
    }

    // A component keeps the whole BEGIN line (its name is the value part of it) and the explicit END line as source
    // ranges for byte-stable writing; an implicit END has no line. `TEXT_END_FOLDED` marks a folded END line.
    private fun begin() {
        val depth = reader.depth
        if (depth >= frames.size) frames = frames.copyOf(maxOf(frames.size * 2, depth + 1))
        val frame = frames[depth] ?: Frame().also { frames[depth] = it }
        val line = reader.line
        var flags = if (line.validUtf8) 0 else TEXT_INVALID_UTF8
        var source = line.bytes
        var lineFrom = line.nameStart
        var from = line.valueStart
        var to = line.valueEnd
        if (line.isFolded) {
            flags = flags or TEXT_FOLDED
            source = reader.source
            val base = reader.sourceBase
            lineFrom = base + line.startOffset
            from = base + line.offsetAt(from)
            to = base + line.offsetAt(to)
        }
        val arena = arena
        if (arena != null) {
            val at = arena.place(source, lineFrom, to)
            source = arena.chunk
            from += at - lineFrom
            to += at - lineFrom
            lineFrom = at
        }
        frame.nameSource = source
        frame.beginStart = lineFrom
        frame.nameStart = from
        frame.nameEnd = to
        frame.nameId = reader.componentId
        frame.textFlags = flags
        frame.startOffset = reader.componentStartOffset
        frame.line = reader.lineNumber
        frame.column = reader.column
        frame.propertyBase = propertyTop
        frame.componentBase = componentTop
        frame.interleaved = false
    }

    private fun end() {
        val depth = reader.depth
        val frame = frames[depth]!!
        val propertyArray = take(properties, frame.propertyBase, propertyTop, EMPTY_PROPERTIES)
        val componentArray = take(components, frame.componentBase, componentTop, EMPTY_COMPONENTS)
        val positions = if (frame.interleaved) marks.copyOfRange(frame.componentBase, componentTop) else null
        propertyTop = frame.propertyBase
        componentTop = frame.componentBase
        var flags = if (reader.partial) frame.textFlags or TEXT_PARTIAL else frame.textFlags
        var endSource: ByteArray? = null
        var endStart = 0
        var endEnd = 0
        if (!reader.isImplicit) {
            val line = reader.line
            var source = line.bytes
            var from = line.nameStart
            var to = line.valueEnd
            if (line.isFolded) {
                flags = flags or TEXT_END_FOLDED
                source = reader.source
                from = reader.sourceBase + line.startOffset
                to = reader.sourceBase + line.offsetAt(to)
            }
            val arena = arena
            if (arena != null) {
                val at = arena.place(source, from, to)
                source = arena.chunk
                to = at + (to - from)
                from = at
            }
            endSource = source
            endStart = from
            endEnd = to
        }
        val component = ICalComponent(
            frame.nameSource,
            frame.beginStart,
            frame.nameStart,
            frame.nameEnd,
            frame.nameId,
            flags,
            endSource,
            endStart,
            endEnd,
            positions,
            propertyArray,
            componentArray,
            frame.startOffset,
            reader.componentEndOffset,
            frame.line,
            frame.column,
            null,
        )
        if (componentTop == components.size) {
            components = components.copyOf(componentTop * 2)
            marks = marks.copyOf(componentTop * 2)
        }
        val parent = if (depth > rootDepth) frames[depth - 1] else null
        marks[componentTop] = if (parent == null) 0 else frame.propertyBase - parent.propertyBase
        components[componentTop++] = component
    }

    fun readProperty(): ICalProperty {
        arena?.startComponent()
        val property = buildProperty()
        reader.values?.property(property, KnownNames.VCALENDAR, reader)
        return property
    }

    // A node keeps byte ranges, not strings. An unfolded line is a range of the input as it is; a folded line is kept
    // as its physical bytes with the folds, mapped back from the fold buffer with `offsetAt`, and is unfolded when
    // decoded. Its start is the `startOffset` of the logical line, not `offsetAt(0)`: a line may begin with an empty
    // segment (a blank line followed by a continuation line), which `offsetAt` would skip (a fuzzing finding). The
    // parameter data is one `IntArray` per property: the count, then per parameter the name range, the name id
    // (`UNRESOLVED_NAME` for unfolded lines: resolved on first access) and the value count, then per value its range
    // and flags.
    private fun property() {
        val frame = frames[reader.depth]!!
        if (componentTop > frame.componentBase) frame.interleaved = true
        val property = buildProperty()
        if (propertyTop == properties.size) properties = properties.copyOf(propertyTop * 2)
        properties[propertyTop++] = property
    }

    private fun buildProperty(): ICalProperty {
        val line = reader.line
        val edits = reader.interceptedLine
        if (edits != null && edits.isEdited) {
            val start = reader.originValue + line.startOffset
            return edits.toProperty(start, line.endOffset - line.startOffset, line.line, line.column)
        }
        val folded = line.isFolded
        val base = reader.sourceBase
        var flags = if (line.validUtf8) 0 else TEXT_INVALID_UTF8
        var source = line.bytes
        var start = line.nameStart
        var end = line.valueEnd
        if (folded) {
            flags = flags or TEXT_FOLDED
            source = reader.source
            start = base + line.startOffset
            end = base + line.offsetAt(end)
        }
        var delta = 0
        val arena = arena
        if (arena != null) {
            val at = arena.place(source, start, end)
            source = arena.chunk
            delta = at - start
        }
        val parameterCount = line.parameterCount
        var data: IntArray? = null
        if (parameterCount > 0) {
            var size = 1
            for (parameter in 0 until parameterCount) {
                size += PARAMETER_FIRST_VALUE + PARAMETER_VALUE_FIELDS * line.parameterValueCount(parameter)
            }
            val parameters = IntArray(size)
            parameters[0] = parameterCount
            var at = 1
            for (parameter in 0 until parameterCount) {
                val valueCount = line.parameterValueCount(parameter)
                parameters[at + PARAMETER_NAME_START] = position(line, line.parameterNameStart(parameter)) + delta
                parameters[at + PARAMETER_NAME_END] = position(line, line.parameterNameEnd(parameter)) + delta
                parameters[at + PARAMETER_NAME_ID] =
                    if (folded) line.parameterNameId(parameter) else line.storedParameterNameId(parameter)
                parameters[at + PARAMETER_VALUE_COUNT] = valueCount
                var value = at + PARAMETER_FIRST_VALUE
                for (index in 0 until valueCount) {
                    parameters[value] = position(line, line.parameterValueStart(parameter, index)) + delta
                    parameters[value + 1] = position(line, line.parameterValueEnd(parameter, index)) + delta
                    parameters[value + PARAMETER_VALUE_FLAGS] = line.parameterValueFlags(parameter, index)
                    value += PARAMETER_VALUE_FIELDS
                }
                at = value
            }
            data = parameters
        }
        return ICalProperty(
            source,
            start + delta,
            position(line, line.nameEnd) + delta,
            position(line, line.valueStart) + delta,
            end + delta,
            line.nameId,
            flags,
            data,
            reader.originValue - (base + delta),
            line.line,
            line.column,
            null,
            null,
            null,
        )
    }

    private fun position(line: ContentLine, index: Int): Int =
        if (line.isFolded) reader.sourceBase + line.offsetAt(index) else index

    private class Frame {
        var nameSource: ByteArray? = null
        var beginStart = 0
        var nameStart = 0
        var nameEnd = 0
        var nameId = KnownNames.UNKNOWN
        var textFlags = 0
        var startOffset = 0L
        var line = 0
        var column = 0
        var propertyBase = 0
        var componentBase = 0
        var interleaved = false
    }

    // `retainSource = false`: the physical bytes of every property line and of the BEGIN and END lines are copied into
    // chunks of 64 KiB (or the input size, if smaller), so the tree keeps no reference to the input; `offsetShift`
    // keeps the spans in input coordinates.
    private class Arena(inputSize: Int, private val perComponent: Boolean) {
        var chunk: ByteArray = ByteArray(0)
            private set
        private var used = 0
        private var nextSize = if (perComponent) FIRST_COMPONENT_CHUNK else minOf(ARENA_CHUNK, inputSize)

        fun startComponent() {
            if (!perComponent) return
            used = chunk.size
            nextSize = FIRST_COMPONENT_CHUNK
        }

        fun place(source: ByteArray, from: Int, to: Int): Int {
            val length = to - from
            if (length > chunk.size - used) {
                chunk = ByteArray(maxOf(length, nextSize))
                used = 0
                if (perComponent) nextSize = minOf(nextSize * 2, ARENA_CHUNK)
            }
            source.copyInto(chunk, used, from, to)
            val at = used
            used += length
            return at
        }
    }

    private companion object {
        private const val INITIAL_PROPERTIES = 64
        private const val INITIAL_COMPONENTS = 16
        private const val INITIAL_FRAMES = 8
        private const val ARENA_CHUNK = 64 * 1024
        private const val FIRST_COMPONENT_CHUNK = 512

        @Suppress("UNCHECKED_CAST")
        fun <T> take(stack: Array<T?>, from: Int, to: Int, empty: Array<T>): Array<T> =
            if (from == to) empty else stack.copyOfRange(from, to) as Array<T>
    }
}

// A string without surrogates is encoded by the standard library. An unpaired surrogate becomes the byte 0xFF, never
// valid in UTF-8, so the lexer reports ICAL-S006 at its position and decodes it as U+FFFD on every platform (the
// standard library replaces it with '?' on the JVM and U+FFFD elsewhere).
internal fun encodeText(text: String): ByteArray {
    var surrogates = false
    for (char in text) {
        if (char.isSurrogate()) {
            surrogates = true
            break
        }
    }
    if (!surrogates) return text.encodeToByteArray()
    var size = 0
    var index = 0
    while (index < text.length) {
        val code = text[index].code
        size += when {
            code < UTF8_TWO_BYTES -> 1

            code < UTF8_THREE_BYTES -> 2

            isSurrogatePair(text, index) -> {
                index++
                4
            }

            code in SURROGATE_FIRST..SURROGATE_LAST -> 1

            else -> 3
        }
        index++
    }
    val out = ByteArray(size)
    var at = 0
    index = 0
    while (index < text.length) {
        val code = text[index].code
        if (isSurrogatePair(text, index)) {
            val point = SUPPLEMENTARY_BASE + ((code - SURROGATE_FIRST) shl 10) + (text[index + 1].code - LOW_SURROGATE)
            at = putUtf8(out, at, point)
            index += 2
            continue
        }
        if (code in SURROGATE_FIRST..SURROGATE_LAST) {
            out[at++] = INVALID_BYTE
        } else {
            at = putUtf8(out, at, code)
        }
        index++
    }
    return out
}

private fun isSurrogatePair(text: String, index: Int): Boolean =
    text[index].isHighSurrogate() && index + 1 < text.length && text[index + 1].isLowSurrogate()

private const val UTF8_TWO_BYTES = 0x80
private const val UTF8_THREE_BYTES = 0x800
private const val SURROGATE_FIRST = 0xD800
private const val SURROGATE_LAST = 0xDFFF
private const val LOW_SURROGATE = 0xDC00
private const val SUPPLEMENTARY_BASE = 0x10000
private const val INVALID_BYTE: Byte = -1

package io.github.calendrum.syntax

import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.SourceSpan
import kotlin.test.assertEquals

internal class ChunkedInput(private val bytes: ByteArray, private val chunks: IntArray) : ByteInput {
    private var position = 0
    private var call = 0
    var reads: Int = 0
        private set

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        reads++
        if (position == bytes.size) return -1
        val chunk = chunks[call++ % chunks.size]
        if (chunk == 0) return 0
        val count = minOf(length, chunk, bytes.size - position)
        bytes.copyInto(buffer, offset, position, position + count)
        position += count
        return count
    }
}

internal val streamBlockSizes: List<Int> = (1..16).toList() + DEFAULT_BLOCK_SIZE

internal fun chunkPattern(blockSize: Int): IntArray = when (blockSize % 3) {
    0 -> intArrayOf(blockSize)
    1 -> IntArray(minOf(blockSize, MAX_CYCLE)) { it + 1 }
    else -> intArrayOf(0, blockSize, 1)
}

private const val MAX_CYCLE = 64

internal fun renderSpan(span: SourceSpan?): String =
    if (span == null) "-" else "${span.startOffset}..${span.endOffset}@${span.line}:${span.column}"

internal fun renderProblem(problem: Problem): String = "$problem ${renderSpan(problem.span)}"

internal fun StringBuilder.renderProperty(property: ICalProperty) {
    append(property.name)
    for (parameter in property.parameters) append(';').append(parameter.name).append('=').append(parameter.values)
    append(':').append(property.value).append(' ').append(renderSpan(property.span)).append('\n')
}

internal fun StringBuilder.renderComponent(component: ICalComponent) {
    val stack = ArrayList<Pair<ICalComponent, Int>>()
    stack += component to 0
    while (stack.isNotEmpty()) {
        val (node, depth) = stack.removeAt(stack.lastIndex)
        repeat(depth) { append("  ") }
        append("BEGIN ").append(node.name).append(' ').append(renderSpan(node.span)).append('\n')
        for (property in node.properties) {
            repeat(depth + 1) { append("  ") }
            renderProperty(property)
        }
        for (child in node.components.asReversed()) stack += child to depth + 1
    }
}

internal fun renderResult(result: ParseResult): String = buildString {
    for (component in result.components) renderComponent(component)
    for (problem in result.problems) append(renderProblem(problem)).append('\n')
    append("charset ").append(result.charset).append(' ').append(result.charsetConfidence)
    append(" truncated ").append(result.isTruncated).append('\n')
    for (code in ProblemCode.known) append(result.problemCount(code)).append(' ')
}

internal fun renderParse(block: () -> ParseResult): String = try {
    renderResult(block())
} catch (e: ICalParseException) {
    "failure ${e.message}\n" + e.problems.joinToString("\n") { renderProblem(it) }
}

internal fun renderEvents(reader: ICalReader, readComponents: Boolean): String = buildString {
    try {
        while (true) {
            val type = reader.next() ?: break
            append(type).append(' ').append(reader.depth).append(' ').append(reader.name).append(' ')
            append(reader.offset).append('@').append(reader.line).append(':').append(reader.column).append(' ')
            append(renderSpan(reader.span()))
            when (type) {
                ContentEventType.Property -> {
                    append(' ').append(reader.value).append(' ').append(reader.valueString())
                    for (index in 0 until reader.parameterCount) {
                        append(
                            ';',
                        ).append(reader.parameterName(index)).append('=').append(reader.parameterValues(index))
                    }
                }

                ContentEventType.End -> append(' ').append(reader.isImplicitEnd)

                ContentEventType.Begin -> if (readComponents && reader.depth == 2) {
                    append('\n')
                    renderComponent(reader.readComponent())
                }
            }
            append('\n')
        }
        append("end ").append(reader.next())
    } catch (e: ICalParseException) {
        append("failure ").append(e.message)
    }
}

internal fun renderComponents(stream: ComponentStream): String = buildString {
    append(stream.calendarIndex).append(' ').append(stream.calendarProperties.size).append('\n')
    try {
        for (component in stream) {
            append("calendar ").append(stream.calendarIndex).append('\n')
            for (property in stream.calendarProperties) renderProperty(property)
            renderComponent(component)
        }
        append("calendar ").append(stream.calendarIndex).append('\n')
        for (property in stream.calendarProperties) renderProperty(property)
        append("more ").append(stream.hasNext())
    } catch (e: ICalParseException) {
        append("failure ").append(e.message)
    }
    append('\n')
    for (problem in stream.problems) append(renderProblem(problem)).append('\n')
}

internal fun flatComponents(result: ParseResult): String = buildString {
    for (component in result.components) {
        if (component.name == "VCALENDAR") {
            for (child in component.components) renderComponent(child)
        } else {
            renderComponent(component)
        }
    }
}

internal fun streamedComponents(stream: ComponentStream): String = buildString {
    for (component in stream) renderComponent(component)
}

internal fun assertStreamEquivalent(
    input: ByteArray,
    options: ParseOptions = ParseOptions.Default,
    blockSizes: List<Int> = streamBlockSizes,
    what: String = "input",
) {
    val parser = ICal.parser(options)
    val parsed = renderParse { parser.parse(input) }
    val events = renderEvents(parser.reader(input), readComponents = false)
    val read = renderEvents(parser.reader(input), readComponents = true)
    val components = renderComponents(parser.components(input))
    if (!parsed.startsWith("failure")) {
        val tree = parser.parse(input)
        if (!tree.isTruncated) assertEquals(flatComponents(tree), streamedComponents(parser.components(input)), what)
    }
    for (blockSize in blockSizes) {
        val chunks = chunkPattern(blockSize)
        val where = "$what, block $blockSize, chunks ${chunks.toList()}"
        assertEquals(parsed, renderParse { parser.parse(ChunkedInput(input, chunks), blockSize, false) }, where)
        assertEquals(events, renderEvents(parser.reader(ChunkedInput(input, chunks), blockSize), false), where)
        assertEquals(read, renderEvents(parser.reader(ChunkedInput(input, chunks), blockSize), true), where)
        assertEquals(components, renderComponents(parser.components(ChunkedInput(input, chunks), blockSize)), where)
    }
}

package io.github.calendrum.conformance

import io.github.calendrum.ByteInput
import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ComponentStream
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ParseResult

internal class Chunks(private val bytes: ByteArray, private val chunk: Int) : ByteInput {
    private var position = 0

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (position == bytes.size) return -1
        val count = minOf(length, chunk, bytes.size - position)
        bytes.copyInto(buffer, offset, position, position + count)
        position += count
        return count
    }
}

internal fun describe(parse: () -> ParseResult): String = try {
    val result = parse()
    buildString {
        for (component in result.components) component(component)
        for (problem in result.problems) problem(problem)
        append(result.charset).append(' ').append(result.charsetConfidence).append(' ').append(result.isTruncated)
    }
} catch (e: ICalParseException) {
    buildString {
        append("rejected\n")
        for (problem in e.problems) problem(problem)
    }
}

internal fun describeComponents(stream: ComponentStream): String = buildString {
    try {
        for (component in stream) {
            append(stream.calendarIndex).append('\n')
            for (property in stream.calendarProperties) property(property)
            component(component)
        }
    } catch (e: ICalParseException) {
        append("rejected\n")
    }
    for (problem in stream.problems) problem(problem)
}

private fun StringBuilder.component(root: ICalComponent) {
    val stack = ArrayList<ICalComponent>()
    stack += root
    while (stack.isNotEmpty()) {
        val node = stack.removeAt(stack.lastIndex)
        append("BEGIN ").append(node.name).append(' ')
        span(node.span)
        for (property in node.properties) property(property)
        for (child in node.components.asReversed()) stack += child
        append('\n')
    }
}

private fun StringBuilder.property(property: ICalProperty) {
    append(property.name)
    for (parameter in property.parameters) append(';').append(parameter.name).append('=').append(parameter.values)
    append(':').append(property.value).append(' ')
    span(property.span)
}

private fun StringBuilder.problem(problem: Problem) {
    append(problem).append(' ')
    span(problem.span)
}

private fun StringBuilder.span(span: SourceSpan?) {
    if (span == null) {
        append("-\n")
    } else {
        append(span.startOffset).append("..").append(span.endOffset).append('@')
        append(span.line).append(':').append(span.column).append('\n')
    }
}

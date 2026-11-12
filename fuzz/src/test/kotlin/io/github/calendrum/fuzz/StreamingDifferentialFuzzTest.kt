package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ComponentStream
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ICalReader
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.ParseResult

class StreamingDifferentialFuzzTest {
    @FuzzTest
    @DictionaryFile(resourcePath = "ics.dict")
    fun streamingDifferential(data: FuzzedDataProvider) {
        val limits = data.consumeLimits()
        val hint = HINTS[data.consumeInt(0, HINTS.size - 1)]
        val mode = if (data.consumeInt(0, 3) == 0) ParseMode.Strict else ParseMode.Lenient
        val chunks = IntArray(data.consumeInt(1, 6)) { data.consumeInt(0, 80) }
        chunks[chunks.lastIndex] = maxOf(1, chunks.last())
        val input = data.consumeRemainingAsBytes()
        val parser = ICal.parser(ParseOptions.builder().limits(limits).charset(hint).mode(mode).build())

        val parsed = render { parser.parse(input) }
        invariant(
            parsed == render {
                parser.parse(Chunks(input, chunks))
            },
        ) { "parse differs, chunks ${chunks.toList()}" }
        invariant(events(parser.reader(input), false) == events(parser.reader(Chunks(input, chunks)), false)) {
            "cursor events differ, chunks ${chunks.toList()}"
        }
        invariant(events(parser.reader(input), true) == events(parser.reader(Chunks(input, chunks)), true)) {
            "readComponent differs, chunks ${chunks.toList()}"
        }
        val components = components(parser.components(input))
        invariant(components == components(parser.components(Chunks(input, chunks)))) {
            "component streams differ, chunks ${chunks.toList()}"
        }
        checkComponentsMatchTheTree(parser, input)
    }

    private fun checkComponentsMatchTheTree(parser: ICalParser, input: ByteArray) {
        val tree = try {
            parser.parse(input)
        } catch (e: ICalParseException) {
            return
        }
        if (tree.isTruncated) return
        val expected = StringBuilder()
        for (component in tree.components) {
            if (component.name == "VCALENDAR") {
                for (child in component.components) expected.component(child)
            } else {
                expected.component(component)
            }
        }
        val actual = StringBuilder()
        for (component in parser.components(input)) actual.component(component)
        invariant(expected.toString() == actual.toString()) { "components differ from the children of the tree" }
    }

    private class Chunks(private val bytes: ByteArray, private val chunks: IntArray) : ByteInput {
        private var position = 0
        private var call = 0

        override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
            if (position == bytes.size) return -1
            val count = minOf(length, chunks[call++ % chunks.size], bytes.size - position)
            bytes.copyInto(buffer, offset, position, position + count)
            position += count
            return count
        }
    }

    private fun render(parse: () -> ParseResult): String = try {
        val result = parse()
        buildString {
            for (component in result.components) component(component)
            for (problem in result.problems) problem(problem)
            append(result.charset).append(' ').append(result.charsetConfidence).append(' ').append(result.isTruncated)
            for (code in CODES) append(' ').append(result.problemCount(code))
        }
    } catch (e: ICalParseException) {
        buildString {
            append("failure ").append(e.message).append('\n')
            for (problem in e.problems) problem(problem)
        }
    }

    private fun events(reader: ICalReader, readComponents: Boolean): String = buildString {
        try {
            while (true) {
                val type = reader.next() ?: break
                append(type).append(' ').append(reader.depth).append(' ').append(reader.name).append(' ')
                append(reader.offset).append('@').append(reader.line).append(':').append(reader.column).append(' ')
                span(reader.span())
                when (type) {
                    ContentEventType.Property -> {
                        append(' ').append(reader.value).append(' ').append(reader.valueString())
                        for (index in 0 until reader.parameterCount) {
                            append(';').append(reader.parameterName(index)).append('=')
                            append(reader.parameterValues(index))
                        }
                    }

                    ContentEventType.End -> append(' ').append(reader.isImplicitEnd)

                    ContentEventType.Begin -> if (readComponents && reader.depth == 2) {
                        append('\n')
                        component(reader.readComponent())
                    }
                }
                append('\n')
            }
        } catch (e: ICalParseException) {
            append("failure ").append(e.message)
        }
    }

    private fun components(stream: ComponentStream): String = buildString {
        try {
            for (component in stream) {
                append("calendar ").append(stream.calendarIndex).append('\n')
                for (property in stream.calendarProperties) property(property)
                component(component)
            }
            append("calendar ").append(stream.calendarIndex).append('\n')
            for (property in stream.calendarProperties) property(property)
        } catch (e: ICalParseException) {
            append("failure ").append(e.message).append('\n')
        }
        for (problem in stream.problems) problem(problem)
    }

    private fun StringBuilder.component(root: ICalComponent) {
        val stack = ArrayList<Pair<ICalComponent, Int>>()
        stack += root to 0
        while (stack.isNotEmpty()) {
            val (node, depth) = stack.removeAt(stack.lastIndex)
            append(depth).append(" BEGIN ").append(node.name).append(' ')
            span(node.span)
            append('\n')
            for (property in node.properties) property(property)
            for (child in node.components.asReversed()) stack += child to depth + 1
        }
    }

    private fun StringBuilder.property(property: ICalProperty) {
        append(property.name)
        for (parameter in property.parameters) append(';').append(parameter.name).append('=').append(parameter.values)
        append(':').append(property.value).append(' ')
        span(property.span)
        append('\n')
    }

    private fun StringBuilder.problem(problem: Problem) {
        append(problem).append(' ')
        span(problem.span)
        append('\n')
    }

    private fun StringBuilder.span(span: SourceSpan?) {
        if (span == null) {
            append('-')
        } else {
            append(span.startOffset).append("..").append(span.endOffset).append('@')
            append(span.line).append(':').append(span.column)
        }
    }

    private companion object {
        val HINTS: List<String?> = listOf(null, null, "windows-1251", "UTF-16", "UTF-8", "KOI8-R", "UTF-32LE")
        val CODES: List<ProblemCode> = (1..29).map { ProblemCode.of("ICAL-S%03d".format(it)) }
    }
}

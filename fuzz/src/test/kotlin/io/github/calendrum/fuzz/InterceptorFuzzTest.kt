@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.InterceptedLine
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.WriteOptions

// Each `Editor` picks an edit from its seed and the content of the line (name, value, parameter count), so the same
// decisions can be replayed on a model of the plain tree (`Line.edited`). The write checks run only with the default
// limits: a tree parsed with other limits (a deeper `maxDepth`, say) does not read back the same with the defaults.
class InterceptorFuzzTest {
    @FuzzTest
    @DictionaryFile(resourcePath = "ics.dict")
    fun interceptor(data: FuzzedDataProvider) {
        val limits = data.consumeLimits()
        val hint = HINTS[data.consumeInt(0, HINTS.size - 1)]
        val seeds = IntArray(data.consumeInt(1, 3)) { data.consumeInt() }
        val input = data.consumeRemainingAsBytes()
        val plainOptions = ParseOptions.builder().limits(limits).charset(hint)
        val interceptedOptions = ParseOptions.builder().limits(limits).charset(hint)
        for (seed in seeds) interceptedOptions.intercept { Editor(seed) }
        val plain = ICal.parser(plainOptions.build())
        val intercepted = ICal.parser(interceptedOptions.build())

        val expected = plain.parse(input).components.map { model(it).edited(seeds) }
        val tree = intercepted.parse(input)
        val actual = tree.components.map(::model)
        invariant(expected == actual) { "the intercepted tree differs from the edited tree" }

        checkStreamsMatch(intercepted, input, tree.isTruncated)
        if (limits !== ParseLimits.Default) return
        checkWriting(tree.components, actual)
        checkWriteInterceptors(plain.parse(input).components, seeds, expected)
    }

    private fun checkStreamsMatch(parser: ICalParser, input: ByteArray, truncated: Boolean) {
        val tree = parser.parse(input).components
        val events = ArrayList<Line>()
        val reader = parser.reader(input)
        while (true) {
            val type = reader.next() ?: break
            if (type != ContentEventType.Property) continue
            val parameters = (0 until reader.parameterCount).map {
                reader.parameterName(it) to
                    reader.parameterValues(it)
            }
            invariant(reader.value.toString() == reader.valueString()) { "value and valueString differ" }
            events += Line(reader.name, parameters, reader.valueString())
        }
        val lines = ArrayList<Line>()
        for (component in tree) model(component).collect(lines)
        invariant(events.size == lines.size) { "the cursor sees ${events.size} lines, the tree ${lines.size}" }
        if (truncated) return
        val children = ArrayList<Node>()
        for (component in tree) {
            if (component.name ==
                "VCALENDAR"
            ) {
                component.components.mapTo(children, ::model)
            } else {
                children += model(component)
            }
        }
        val streamed = parser.components(input).asSequence().map(::model).toList()
        invariant(children == streamed) { "the component stream differs from the tree" }
    }

    private fun checkWriting(components: List<ICalComponent>, expected: List<Node>) {
        val text = try {
            ICal.writer().encodeToString(components)
        } catch (e: IllegalArgumentException) {
            return
        }
        val reparsed = ICal.parse(text).components.map(::model)
        invariant(reparsed == expected) { "the written intercepted tree does not parse back the same" }
    }

    private fun checkWriteInterceptors(components: List<ICalComponent>, seeds: IntArray, expected: List<Node>) {
        val options = WriteOptions.builder()
        for (seed in seeds) options.intercept { Editor(seed) }
        val text = try {
            ICal.writer(options.build()).encodeToString(components)
        } catch (e: IllegalArgumentException) {
            return
        }
        val reparsed = ICal.parse(text).components.map(::model)
        invariant(reparsed == expected) { "the tree written through interceptors does not parse back edited" }
    }

    private class Editor(private val seed: Int) : LineInterceptor {
        override fun intercept(line: InterceptedLine) {
            val parameters = (0 until line.parameterCount).map { line.parameterName(it) to line.parameterValues(it) }
            when (val edit = decide(seed, Line(line.name, parameters, line.value.toString()))) {
                DROP -> line.drop()
                RENAME -> line.rename(RENAMED)
                SET_VALUE -> line.setValue(EDITED_VALUE)
                SET_PARAMETER -> line.setParameter(FUZZ_PARAMETER, *FUZZ_VALUES)
                REMOVE_PARAMETER -> line.removeParameter(parameters.firstOrNull()?.first ?: FUZZ_PARAMETER)
                else -> check(edit == KEEP)
            }
        }
    }

    private class Line(val name: String, val parameters: List<Pair<String, List<String>>>, val value: String) {
        override fun equals(other: Any?): Boolean =
            other is Line && name == other.name && parameters == other.parameters && value == other.value

        override fun hashCode(): Int = (name.hashCode() * 31 + parameters.hashCode()) * 31 + value.hashCode()

        override fun toString(): String = "$name;$parameters"

        fun edited(seeds: IntArray): Line? {
            var line = this
            for (seed in seeds) {
                line = when (decide(seed, line)) {
                    DROP -> return null

                    RENAME -> Line(RENAMED, line.parameters, line.value)

                    SET_VALUE -> Line(line.name, line.parameters, EDITED_VALUE)

                    SET_PARAMETER -> Line(line.name, line.withParameter(), line.value)

                    REMOVE_PARAMETER -> {
                        val name = line.parameters.firstOrNull()?.first ?: FUZZ_PARAMETER
                        Line(line.name, line.parameters.filter { it.first != name }, line.value)
                    }

                    else -> line
                }
            }
            return line
        }

        private fun withParameter(): List<Pair<String, List<String>>> {
            val parameter = FUZZ_PARAMETER to FUZZ_VALUES.toList()
            val out = ArrayList<Pair<String, List<String>>>()
            var replaced = false
            for (item in parameters) {
                if (item.first != FUZZ_PARAMETER) {
                    out += item
                } else if (!replaced) {
                    out += parameter
                    replaced = true
                }
            }
            if (!replaced) out += parameter
            return out
        }
    }

    private class Node(val name: String, val lines: List<Line>, val children: List<Node>) {
        override fun equals(other: Any?): Boolean =
            other is Node && name == other.name && lines == other.lines && children == other.children

        override fun hashCode(): Int = (name.hashCode() * 31 + lines.hashCode()) * 31 + children.hashCode()

        override fun toString(): String = "$name(${lines.size}, ${children.size})"

        fun edited(seeds: IntArray): Node = Node(
            name,
            lines.mapNotNull {
                it.edited(seeds)
            },
            children.map { it.edited(seeds) },
        )

        fun collect(out: MutableList<Line>) {
            out += lines
            for (child in children) child.collect(out)
        }
    }

    private fun model(component: ICalComponent): Node = Node(
        component.name,
        component.properties.map { property ->
            Line(property.name, property.parameters.map { it.name to it.values }, property.value)
        },
        component.components.map(::model),
    )

    private companion object {
        val HINTS: List<String?> = listOf(null, null, "windows-1251", "UTF-16", "UTF-8")
        const val KEEP = 0
        const val DROP = 1
        const val RENAME = 2
        const val SET_VALUE = 3
        const val SET_PARAMETER = 4
        const val REMOVE_PARAMETER = 5
        const val KINDS = 8
        const val RENAMED = "X-RENAMED"
        const val EDITED_VALUE = "edited value"
        const val FUZZ_PARAMETER = "X-FUZZ"
        val FUZZ_VALUES = arrayOf("a", "b,c")

        fun decide(seed: Int, line: Line): Int {
            val hash = ((seed * 31 + line.name.hashCode()) * 31 + line.value.hashCode()) * 31 + line.parameters.size
            val kind = (hash ushr 1) % KINDS
            return if (kind > REMOVE_PARAMETER) KEEP else kind
        }
    }
}

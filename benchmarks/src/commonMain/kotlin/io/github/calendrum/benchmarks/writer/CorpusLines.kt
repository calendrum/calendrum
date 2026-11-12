@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.writer

import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.syntax.ContentLineWriter
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ContentSelection
import io.github.calendrum.syntax.ParseLimits

// The logical lines of a corpus in flat arrays: kind, name and raw value per line; parameters and their values
// addressed by end indices. `firstDifference` compares an output with them event by event, without building a second
// copy (100 MB for `huge`).
internal class CorpusLines private constructor(
    private val kinds: IntArray,
    private val names: Array<String>,
    private val values: Array<String>,
    private val parameterEnds: IntArray,
    private val parameterNames: Array<String>,
    private val valueEnds: IntArray,
    private val parameterValues: Array<String>,
) {
    val size: Int get() = kinds.size

    fun writeTo(writer: ContentLineWriter) {
        var parameter = 0
        var value = 0
        for (line in kinds.indices) {
            when (kinds[line]) {
                ContentReader.BEGIN -> writer.begin(names[line])

                ContentReader.END -> writer.end(names[line])

                else -> {
                    writer.property(names[line])
                    val parameterEnd = parameterEnds[line]
                    while (parameter < parameterEnd) {
                        writer.parameter(parameterNames[parameter])
                        val valueEnd = valueEnds[parameter]
                        while (value < valueEnd) writer.parameterValue(parameterValues[value++])
                        parameter++
                    }
                    writer.value(values[line])
                }
            }
        }
    }

    fun firstDifference(bytes: ByteArray): String? {
        val problems = ArrayList<Problem>()
        val reader = ContentReader(bytes, ContentSelection.Full, ParseLimits.Default) { problems += it }
        var line = 0
        var parameter = 0
        var value = 0
        while (true) {
            val kind = reader.next()
            if (kind == ContentReader.END_OF_INPUT) break
            if (line == size) return "more lines than the $size read"
            if (kind != kinds[line]) return "line #$line: kind $kind instead of ${kinds[line]}"
            if (kind != ContentReader.PROPERTY) {
                if (reader.componentName() != names[line]) return "line #$line: another component name"
                line++
                continue
            }
            val content = reader.line
            if (content.name() != names[line]) return "line #$line: another property name"
            if (content.rawValue() != values[line]) return "line #$line: another value"
            val parameterStart = parameter
            parameter = parameterEnds[line]
            if (content.parameterCount != parameter - parameterStart) return "line #$line: another parameter count"
            for (index in 0 until content.parameterCount) {
                val expected = parameterStart + index
                if (content.parameterName(index) != parameterNames[expected]) return "line #$line: parameter name"
                val valueStart = value
                value = valueEnds[expected]
                if (content.parameterValueCount(index) != value - valueStart) return "line #$line: value count"
                for (valueIndex in 0 until content.parameterValueCount(index)) {
                    val same = content.parameterValue(index, valueIndex) == parameterValues[valueStart + valueIndex]
                    if (!same) return "line #$line: parameter value"
                }
            }
            line++
        }
        if (line != size) return "$line lines instead of $size"
        return if (problems.isEmpty()) null else "problems $problems"
    }

    companion object {
        val EMPTY: CorpusLines = CorpusLines(
            IntArray(0),
            emptyArray(),
            emptyArray(),
            IntArray(0),
            emptyArray(),
            IntArray(0),
            emptyArray(),
        )

        fun read(bytes: ByteArray): CorpusLines {
            val problems = ArrayList<Problem>()
            val reader = ContentReader(bytes, ContentSelection.Full, ParseLimits.Default) { problems += it }
            val kinds = IntList()
            val names = ArrayList<String>()
            val values = ArrayList<String>()
            val parameterEnds = IntList()
            val parameterNames = ArrayList<String>()
            val valueEnds = IntList()
            val parameterValues = ArrayList<String>()
            while (true) {
                val kind = reader.next()
                if (kind == ContentReader.END_OF_INPUT) break
                kinds.add(kind)
                if (kind == ContentReader.PROPERTY) {
                    val line = reader.line
                    names += line.name()
                    values += line.rawValue()
                    for (parameter in 0 until line.parameterCount) {
                        parameterNames += line.parameterName(parameter)
                        for (value in 0 until line.parameterValueCount(parameter)) {
                            parameterValues += line.parameterValue(parameter, value)
                        }
                        valueEnds.add(parameterValues.size)
                    }
                } else {
                    names += reader.componentName()
                    values += ""
                }
                parameterEnds.add(parameterNames.size)
            }
            check(problems.isEmpty()) { "unexpected problems $problems" }
            return CorpusLines(
                kinds.toArray(),
                names.toTypedArray(),
                values.toTypedArray(),
                parameterEnds.toArray(),
                parameterNames.toTypedArray(),
                valueEnds.toArray(),
                parameterValues.toTypedArray(),
            )
        }
    }

    private class IntList {
        private var items = IntArray(1024)
        private var count = 0

        fun add(item: Int) {
            if (count == items.size) items = items.copyOf(count * 2)
            items[count++] = item
        }

        fun toArray(): IntArray = items.copyOf(count)
    }
}

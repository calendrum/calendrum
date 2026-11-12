package io.github.calendrum.conformance

import io.github.calendrum.ICal
import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ICalReader
import io.github.calendrum.syntax.WriteOptions

// The golden round-trip checks of a real calendar, shared by the conformance cases with source `anonymized-real` (every
// platform) and `RealCorpusTest` (JVM): the byte-stable output must equal the input with every bare CR or LF turned
// into CRLF and a UTF-8 byte order mark removed, since the tree keeps neither; a transcoded input cannot equal its
// UTF-8 output, so there it must read back as the same tree and write the same bytes again. The canonical output must
// read back as an equal tree without problems; the cursor's events, built into per-component text in input order, must
// equal the tree's, positions included.
internal fun roundTripFailures(input: ByteArray, parser: ICalParser): List<String> {
    val failures = ArrayList<String>()
    val result = parser.parse(input)
    val tree = result.components
    val byteStable = ICal.writer().encodeToByteArray(tree)
    if (result.charset == UTF_8) {
        firstDifference(withCrlf(withoutBom(input)), byteStable)?.let {
            failures += "byte-stable output differs from the input with CRLF line endings at byte $it"
        }
    } else {
        val reparsed = ICal.parse(byteStable)
        if (reparsed.charset != UTF_8 || reparsed.components != tree) {
            failures += "byte-stable output of the ${result.charset} input reads back as a different tree"
        }
        if (!ICal.writer().encodeToByteArray(reparsed.components).contentEquals(byteStable)) {
            failures += "byte-stable output of the ${result.charset} input is not stable on a second write"
        }
    }
    try {
        val reparsed = ICal.parse(canonicalWriter.encodeToByteArray(tree))
        if (reparsed.problems.isNotEmpty()) {
            failures += "canonical output has problems: ${reparsed.problems.map { it.code.id }.distinct()}"
        }
        if (reparsed.components != tree) failures += "canonical output reads back as a different tree"
    } catch (e: IllegalArgumentException) {
        failures += "canonical writing rejected the tree: ${e.message}"
    }
    val expected = tree.map(::treeNode)
    val actual = cursorNodes(parser.reader(input))
    if (actual != expected) {
        val index = expected.indices.firstOrNull { it >= actual.size || actual[it] != expected[it] } ?: expected.size
        failures += "cursor events differ from the tree at top-level component $index " +
            "(${actual.size} components from the cursor, ${expected.size} in the tree)"
    }
    return failures
}

internal fun firstDifference(expected: ByteArray, actual: ByteArray): Int? {
    val common = minOf(expected.size, actual.size)
    for (index in 0 until common) {
        if (expected[index] != actual[index]) return index
    }
    return if (expected.size == actual.size) null else common
}

private fun withoutBom(input: ByteArray): ByteArray {
    val bom = input.size >= BOM.size && BOM.indices.all { input[it] == BOM[it] }
    return if (bom) input.copyOfRange(BOM.size, input.size) else input
}

private fun withCrlf(input: ByteArray): ByteArray {
    var bare = 0
    for (index in input.indices) {
        if (isBareLineEnd(input, index)) bare++
    }
    if (bare == 0) return input
    val out = ByteArray(input.size + bare)
    var size = 0
    for (index in input.indices) {
        if (isBareLineEnd(input, index)) {
            out[size++] = CR
            out[size++] = LF
        } else {
            out[size++] = input[index]
        }
    }
    return out
}

private fun isBareLineEnd(input: ByteArray, index: Int): Boolean = when (input[index]) {
    CR -> index + 1 == input.size || input[index + 1] != LF
    LF -> index == 0 || input[index - 1] != CR
    else -> false
}

private fun treeNode(component: ICalComponent): String = buildString {
    append("BEGIN ").append(component.name).append(' ')
    start(component.span)
    for (property in component.properties) property(property.name, parameters(property), property.value, property.span)
    for (child in component.components) append(treeNode(child))
    append("END ").append(component.name).append('\n')
}

private fun parameters(property: ICalProperty): String =
    property.parameters.joinToString("") { ";${it.name}=${it.values}" }

private class OpenNode(val begin: String) {
    val properties = StringBuilder()
    val children = StringBuilder()
}

// Properties and child components are collected separately per open component, because the public tree exposes them as
// two lists even when the input interleaves them (Apple's `X-APPLE-*` after `VALARM`).
private fun cursorNodes(reader: ICalReader): List<String> {
    val roots = ArrayList<String>()
    val stack = ArrayList<OpenNode>()
    while (true) {
        when (reader.next() ?: break) {
            ContentEventType.Begin -> {
                val begin = buildString {
                    append("BEGIN ").append(reader.name).append(' ')
                    append(reader.offset).append('@').append(reader.line).append(':').append(reader.column).append('\n')
                }
                stack += OpenNode(begin)
            }

            ContentEventType.Property -> {
                val parameters = (0 until reader.parameterCount).joinToString("") {
                    ";${reader.parameterName(it)}=${reader.parameterValues(it)}"
                }
                stack.last().properties.property(reader.name, parameters, reader.valueString(), reader.span())
            }

            ContentEventType.End -> {
                val open = stack.removeAt(stack.lastIndex)
                val node = open.begin + open.properties + open.children + "END ${reader.name}\n"
                if (stack.isEmpty()) roots += node else stack.last().children.append(node)
            }
        }
    }
    return roots
}

private fun StringBuilder.property(name: String, parameters: String, value: String, span: SourceSpan?) {
    append(name).append(parameters).append(':').append(value).append(' ')
    if (span == null) {
        append("-\n")
    } else {
        append(span.startOffset).append("..").append(span.endOffset).append('@')
        append(span.line).append(':').append(span.column).append('\n')
    }
}

private fun StringBuilder.start(span: SourceSpan?) {
    if (span == null) {
        append("-\n")
    } else {
        append(span.startOffset).append('@').append(span.line).append(':').append(span.column).append('\n')
    }
}

private val canonicalWriter = ICal.writer(WriteOptions.builder().byteStable(false).build())

private val BOM = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())
private const val CR: Byte = 13
private const val LF: Byte = 10
private const val UTF_8 = "UTF-8"

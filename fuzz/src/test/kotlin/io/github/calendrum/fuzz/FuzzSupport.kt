@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.ProblemListener
import io.github.calendrum.syntax.ContentLine
import io.github.calendrum.syntax.DecodedInput
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.syntax.decodeInput

// Fails with AssertionError, never with IllegalStateException or IllegalArgumentException: the writer target treats
// those two as expected outcomes of the code under test.
internal fun invariant(condition: Boolean, message: () -> String) {
    if (!condition) throw AssertionError(message())
}

// Whether the canonical writer accepts [component]. Parsing keeps control characters and parameters without a value,
// which canonical writing rejects; the library transforms that write anew (Canonical, jCal) inherit that, so the
// targets hold them to their round trips only for trees that can be written.
internal fun canonicallyWritable(component: ICalComponent): Boolean = try {
    CANONICAL_WRITER.encodeToByteArray(component)
    true
} catch (_: IllegalArgumentException) {
    false
}

private val CANONICAL_WRITER = ICal.writer(WriteOptions.builder().byteStable(false).build())

// The first byte from the end picks the limits: an even byte (for instance the LF that ends an .ics seed) gives
// `ParseLimits.Default`, an odd one gives small limits read from the next bytes, so the fuzzer reaches every limit
// path.
internal fun FuzzedDataProvider.consumeLimits(): ParseLimits = if (!consumeBoolean()) {
    ParseLimits.Default
} else {
    ParseLimits.builder()
        .maxLineOctets(consumeInt(1, 300))
        .maxLines(consumeInt(1, 200))
        .maxProblems(consumeInt(0, 20))
        .maxDepth(consumeInt(1, 20))
        .maxComponents(consumeInt(1, 100))
        .build()
}

// Reference positions: the line is 1 plus the number of line endings (CRLF, lone CR, lone LF) before the offset, the
// column is the byte distance from the start of that physical line plus 1. The BOM belongs to the first line, and a
// problem inside a folded line has the position of its source bytes, so the same rule holds for lines, events and
// problems.
internal class Positions(private val input: ByteArray) {
    private val lineStarts: IntArray

    init {
        val starts = ArrayList<Int>()
        starts += 0
        var index = 0
        while (index < input.size) {
            val byte = input[index]
            index++
            if (byte == CR && index < input.size && input[index] == LF) index++
            if (byte == CR || byte == LF) starts += index
        }
        lineStarts = starts.toIntArray()
    }

    fun check(what: Any, offset: Int, line: Int, column: Int) {
        invariant(offset in 0..input.size) { "$what at offset $offset outside the input of ${input.size} bytes" }
        var low = 0
        var high = lineStarts.size - 1
        while (low < high) {
            val middle = (low + high + 1) ushr 1
            if (lineStarts[middle] <= offset) low = middle else high = middle - 1
        }
        val expectedColumn = offset - lineStarts[low] + 1
        invariant(line == low + 1 && column == expectedColumn) {
            "$what at offset $offset is $line:$column, expected ${low + 1}:$expectedColumn"
        }
    }
}

internal class ProblemLog(private val input: ByteArray) : ProblemListener {
    val problems = ArrayList<Problem>()

    override fun onProblem(problem: Problem) {
        problems += problem
    }

    fun has(code: String): Boolean = problems.any { it.code.id == code }

    // Problem rules: known codes only, positions inside the input; S002-S004 are reported once per input; S011, S014
    // and S023 bypass the problem limit; every other problem counts towards `maxProblems`, and S013 comes once, after
    // the limit is reached, and nothing counted comes after it.
    fun check(limits: ParseLimits) {
        val positions = Positions(input)
        for (problem in problems) {
            invariant(problem.code.id in CODES) { "unknown problem code $problem" }
            invariant(problem.span.endOffset == problem.span.startOffset) { "problem span is not a point: $problem" }
            positions.check(problem, problem.span.startOffset.toInt(), problem.span.line, problem.span.column)
        }
        val counts = problems.groupingBy { it.code.id }.eachCount()
        for (code in ONCE_PER_INPUT + BYPASSES_LIMIT + TOO_MANY_PROBLEMS) {
            invariant((counts[code] ?: 0) <= 1) { "$code reported ${counts[code]} times" }
        }
        val limited = problems.filter { it.code.id !in BYPASSES_LIMIT && it.code.id != TOO_MANY_PROBLEMS }
        invariant(limited.size <= limits.maxProblems) {
            "${limited.size} problems reported with maxProblems ${limits.maxProblems}"
        }
        val limitIndex = problems.indexOfFirst { it.code.id == TOO_MANY_PROBLEMS }
        if (limitIndex >= 0) {
            invariant(limited.size == limits.maxProblems) { "$TOO_MANY_PROBLEMS before the limit was reached" }
            invariant(problems.subList(limitIndex, problems.size).none { it in limited }) {
                "a limited problem reported after $TOO_MANY_PROBLEMS"
            }
        }
    }

    private companion object {
        val CODES = (1..29).map { "ICAL-S%03d".format(it) }.toSet()
        val ONCE_PER_INPUT = listOf("ICAL-S002", "ICAL-S003", "ICAL-S004")
        val BYPASSES_LIMIT = listOf("ICAL-S011", "ICAL-S014", "ICAL-S023") + (24..29).map { "ICAL-S%03d".format(it) }
        const val TOO_MANY_PROBLEMS = "ICAL-S013"
    }
}

// The text positions refer to. The UTF-8 constructors read the input as it is, except after a UTF-16/32 byte order
// mark, where they read its transcoding; the detecting constructors read what `decodeInput` without a hint returns.
// Lines and problems are checked against this text, not against the raw bytes.
internal fun decodedText(input: ByteArray, detect: Boolean): ByteArray {
    val decoded = decodeInput(input, null) { }
    val wideByteOrderMark = decoded.source == DecodedInput.SOURCE_BOM && decoded.charset?.startsWith("UTF-") == true &&
        decoded.charset != "UTF-8"
    if (!detect && !wideByteOrderMark) return input
    return decoded.bytes.copyOfRange(decoded.offset, decoded.offset + decoded.length)
}

// Reference unfolding of a physical span: every CR, LF or CRLF inside a logical line must be followed by one space or
// HTAB, and both are removed.
internal fun unfold(input: ByteArray, from: Int, to: Int): ByteArray {
    val out = ByteArray(to - from)
    var count = 0
    var i = from
    while (i < to) {
        val byte = input[i]
        if (byte != CR && byte != LF) {
            out[count++] = byte
            i++
            continue
        }
        i += if (byte == CR && i + 1 < to && input[i + 1] == LF) 2 else 1
        invariant(i < to && (input[i] == SPACE || input[i] == HTAB)) {
            "line break at ${i - 1} inside a logical line is not a fold"
        }
        i++
    }
    return out.copyOf(count)
}

// The name ends at the first ':' or ';', every parameter starts after its ';', values follow their name and each other,
// and everything lies before the value, which starts after ':'. A known name must equal its bytes ignoring ASCII case.
// All string accessors are called, so a bad range would also throw.
internal fun checkLineRanges(line: ContentLine) {
    val bytes = line.bytes
    val nameStart = line.nameStart
    val nameEnd = line.nameEnd
    val valueStart = line.valueStart
    val valueEnd = line.valueEnd
    invariant(nameStart in 0..nameEnd && nameEnd < valueStart && valueStart <= valueEnd && valueEnd <= bytes.size) {
        "line ranges: name $nameStart..$nameEnd, value $valueStart..$valueEnd, buffer ${bytes.size}"
    }
    invariant(bytes[valueStart - 1] == COLON) { "the value does not follow ':'" }
    invariant(bytes[nameEnd] == COLON || bytes[nameEnd] == SEMICOLON) { "the name does not end at ':' or ';'" }
    for (index in nameStart until nameEnd) {
        invariant(bytes[index] != COLON && bytes[index] != SEMICOLON) { "':' or ';' inside the name" }
    }
    var cursor = nameEnd
    for (parameter in 0 until line.parameterCount) {
        val start = line.parameterNameStart(parameter)
        val end = line.parameterNameEnd(parameter)
        invariant(start > cursor && bytes[start - 1] == SEMICOLON && start <= end && end < valueStart) {
            "parameter $parameter name $start..$end after $cursor, value starts at $valueStart"
        }
        cursor = end
        for (value in 0 until line.parameterValueCount(parameter)) {
            val from = line.parameterValueStart(parameter, value)
            val to = line.parameterValueEnd(parameter, value)
            invariant(from > cursor && from <= to && to < valueStart) {
                "parameter $parameter value $value at $from..$to after $cursor, value starts at $valueStart"
            }
            cursor = to
        }
        line.parameterName(parameter)
        val id = line.parameterNameId(parameter)
        if (id >= 0) checkKnownName(line.parameterName(parameter), bytes, start, end)
        for (value in 0 until line.parameterValueCount(parameter)) line.parameterValue(parameter, value)
    }
    invariant(cursor < valueStart) { "parameters overlap the value" }
    if (line.nameId >= 0) checkKnownName(line.name(), bytes, nameStart, nameEnd)
    line.name()
    line.rawValue()
}

private fun checkKnownName(name: String, bytes: ByteArray, from: Int, to: Int) {
    val raw = String(bytes, from, to - from, Charsets.ISO_8859_1)
    invariant(name.equals(raw, ignoreCase = true) && raw.all { it.code < 0x80 }) {
        "known name $name for raw bytes '$raw'"
    }
}

internal const val HTAB: Byte = 0x09
internal const val LF: Byte = 0x0A
internal const val CR: Byte = 0x0D
internal const val SPACE: Byte = 0x20
internal const val COLON: Byte = 0x3A
internal const val SEMICOLON: Byte = 0x3B

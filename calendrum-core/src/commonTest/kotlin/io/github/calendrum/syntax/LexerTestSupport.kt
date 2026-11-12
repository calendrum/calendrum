package io.github.calendrum.syntax

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import kotlin.test.assertEquals

internal data class Param(val name: String, val values: List<String>)

internal data class Lexed(
    val name: String,
    val parameters: List<Param>,
    val value: String,
    val startOffset: Int,
    val endOffset: Int,
    val line: Int,
    val column: Int,
    val endLine: Int,
    val folded: Boolean,
    val quoted: List<List<Boolean>>,
)

internal class LexResult(val lines: List<Lexed>, val problems: List<Problem>, val lexer: ContentLexer) {
    val codes: List<String> get() = problems.map { it.code.id }

    fun single(): Lexed {
        assertEquals(1, lines.size, "lines: $lines")
        return lines[0]
    }

    fun problem(code: ProblemCode): Problem = problems.single { it.code == code }
}

internal fun ContentLine.snapshot(): Lexed = Lexed(
    name = name(),
    parameters = (0 until parameterCount).map { p ->
        Param(parameterName(p), (0 until parameterValueCount(p)).map { v -> parameterValue(p, v) })
    },
    value = rawValue(),
    startOffset = startOffset,
    endOffset = endOffset,
    line = line,
    column = column,
    endLine = endLine,
    folded = isFolded,
    quoted = (0 until parameterCount).map { p ->
        (0 until parameterValueCount(p)).map { v -> isParameterValueQuoted(p, v) }
    },
)

internal fun lex(text: String, limits: ParseLimits = ParseLimits.Default): LexResult =
    lex(text.encodeToByteArray(), limits = limits)

internal fun lex(
    bytes: ByteArray,
    offset: Int = 0,
    length: Int = bytes.size - offset,
    limits: ParseLimits = ParseLimits.Default,
): LexResult {
    val problems = mutableListOf<Problem>()
    val lexer = ContentLexer(bytes, offset, length, limits) { problems += it }
    val lines = mutableListOf<Lexed>()
    while (true) {
        val line = lexer.next() ?: break
        lines += line.snapshot()
    }
    return LexResult(lines, problems, lexer)
}

internal fun crlf(vararg lines: String): String = lines.joinToString(separator = "\r\n", postfix = "\r\n")

internal fun bytesOf(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }

internal operator fun ByteArray.plus(text: String): ByteArray = this + text.encodeToByteArray()

internal fun String.insertAtByte(index: Int, insertion: String): ByteArray {
    val bytes = encodeToByteArray()
    return bytes.copyOfRange(0, index) + insertion.encodeToByteArray() + bytes.copyOfRange(index, bytes.size)
}

internal fun limits(
    maxLineOctets: Int = ParseLimits.Default.maxLineOctets,
    maxLines: Int = ParseLimits.Default.maxLines,
    maxProblems: Int = ParseLimits.Default.maxProblems,
    maxDepth: Int = ParseLimits.Default.maxDepth,
    maxComponents: Int = ParseLimits.Default.maxComponents,
): ParseLimits =
    ParseLimits(maxLineOctets, maxLines, maxProblems, maxDepth, maxComponents, NO_TIME_LIMIT, NO_INPUT_LIMIT)

internal val Problem.offset: Int get() = span.startOffset.toInt()

// Short names for the span fields the older tests used as fields of the internal `Problem`.
internal val Problem.line: Int get() = span.line

internal val Problem.column: Int get() = span.column

internal val Problem.id: String get() = code.id

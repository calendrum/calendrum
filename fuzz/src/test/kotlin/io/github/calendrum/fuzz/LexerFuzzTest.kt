@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.InternalICalApi
import io.github.calendrum.syntax.ContentLexer

class LexerFuzzTest {
    // Invariants of the `Lexer` target: no exception, termination, ordered and disjoint lines, the logical line equals
    // its unfolded physical span, ranges inside the line, the line and length limits, the problem rules.
    @FuzzTest
    @DictionaryFile(resourcePath = "ics.dict")
    fun lexer(data: FuzzedDataProvider) {
        val limits = data.consumeLimits()
        val raw = data.consumeRemainingAsBytes()
        val input = decodedText(raw, detect = false)
        val problems = ProblemLog(input)
        val positions = Positions(input)
        val lexer = ContentLexer(raw, limits, problems)
        var lines = 0
        var previousEnd = 0
        while (true) {
            val line = lexer.next() ?: break
            lines++
            invariant(lines <= limits.maxLines && lines <= input.size) { "$lines lines from ${input.size} bytes" }
            invariant(line.startOffset >= previousEnd && line.startOffset < line.endOffset) {
                "line at ${line.startOffset}..${line.endOffset} after $previousEnd"
            }
            invariant(line.endOffset <= input.size) { "line ends at ${line.endOffset}, input size ${input.size}" }
            positions.check(line, line.startOffset, line.line, line.column)
            invariant(line.valueEnd - line.nameStart <= limits.maxLineOctets) { "line longer than the limit" }
            checkLineRanges(line)
            val logical = unfold(input, line.startOffset, line.endOffset)
            invariant(logical.contentEquals(line.bytes.copyOfRange(line.nameStart, line.valueEnd))) {
                "logical line at ${line.startOffset} differs from its unfolded source"
            }
            previousEnd = line.endOffset
        }
        invariant(lexer.next() == null) { "next() after the end returned a line" }
        problems.check(limits)
    }
}

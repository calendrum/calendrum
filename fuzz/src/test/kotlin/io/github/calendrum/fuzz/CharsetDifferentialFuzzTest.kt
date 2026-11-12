@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.syntax.ContentLexer
import io.github.calendrum.syntax.ContentLine
import io.github.calendrum.syntax.DecodedInput
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.decodeInput
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction

class CharsetDifferentialFuzzTest {
    // Differential test of the input decoder. The last byte picks a hint from `HINTS`, the rest is the input.
    // `decodeInput` must not throw; UTF-8 must come back as the input itself; any other result must be valid UTF-8
    // equal to `java.nio.charset` decoding of the same bytes (after the byte order mark if the mark chose the encoding)
    // in the chosen encoding. Then the lazy path is checked against the eager one: a lexer with the same hint on the
    // raw input must return the same lines, positions and problems as a UTF-8 lexer on the decoded text plus the
    // problems of the decoder. A decoded text that itself starts with a signature (U+FEFF or `+/v8`, possible only
    // after UTF-16/32) is skipped there: the second lexer would read it as a byte order mark.
    @FuzzTest
    @DictionaryFile(resourcePath = "charset.dict")
    fun charsetDifferential(data: FuzzedDataProvider) {
        val hint = HINTS[data.consumeInt(0, HINTS.size - 1)]
        val input = data.consumeRemainingAsBytes()

        val decoderProblems = ArrayList<Problem>()
        val decoded = decodeInput(input, hint) { decoderProblems += it }
        val text = decoded.bytes.copyOfRange(decoded.offset, decoded.offset + decoded.length)
        invariant(decoded.offset >= 0 && decoded.length >= 0 && decoded.offset + decoded.length <= decoded.bytes.size) {
            "range ${decoded.offset}+${decoded.length} of ${decoded.bytes.size}"
        }
        val charset = decoded.charset
        if (charset == null) {
            invariant(decoded.length == 0 && decoderProblems.single().code.id == UNSUPPORTED) {
                "unsupported: $decoderProblems"
            }
        } else if (charset == "UTF-8") {
            invariant(decoded.bytes === input && decoded.offset == 0 && decoded.length == input.size) {
                "UTF-8 input was copied: $decoded"
            }
        } else {
            invariant(firstMalformedUtf8(text) < 0) { "decoded $charset is not valid UTF-8" }
            val body = if (decoded.source == DecodedInput.SOURCE_BOM) {
                input.copyOfRange(BOM_LENGTHS.getValue(charset), input.size)
            } else {
                input
            }
            val expected = reference(body, charset)
            val actual = String(text, Charsets.UTF_8)
            invariant(actual == expected) {
                "$charset (${decoded.source}) of ${input.hex()}: ${actual.hex()}, expected ${expected.hex()}"
            }
        }

        // An ISO-8859-1, latin1, US-ASCII or windows-1252 hint that the data does not contradict decodes as
        // windows-1252, as in WHATWG; the reference then decodes with the windows-1252 of the JDK, which differs from
        // its ISO-8859-1 in 0x80..0x9F and maps the five undefined bytes to U+FFFD, like our table.
        if (hint != null && hint.lowercase() in WINDOWS_1252_LABELS && decoded.source == DecodedInput.SOURCE_HINT) {
            invariant(charset == WINDOWS_1252) { "hint $hint decoded as $charset, expected $WINDOWS_1252" }
        }
        if (decoded.source == DecodedInput.SOURCE_HEURISTIC || decoded.source == DecodedInput.SOURCE_BOM) {
            invariant(decoded.confidence in 0..100) { "confidence ${decoded.confidence}" }
        } else {
            invariant(decoded.confidence == 100) { "confidence ${decoded.confidence} for source ${decoded.source}" }
        }

        if (charset != null && charset != "UTF-8" && startsWithSignature(text)) return
        val reference = ProblemLog(text)
        val eagerLines = lex(text, reference) { ContentLexer(text, ParseLimits.Default, it) }
        val lazyProblems = ProblemLog(text)
        val lazyLines = lex(text, lazyProblems) { ContentLexer(input, hint, ParseLimits.Default, it) }
        invariant(eagerLines == lazyLines) { "lines of ${input.hex()} with hint $hint differ" }
        lazyProblems.check(ParseLimits.Default)
        val expectedProblems = (decoderProblems + reference.problems).map { it.toString() }.sorted()
        val actualProblems = lazyProblems.problems.map { it.toString() }.sorted()
        invariant(expectedProblems == actualProblems) { "problems $actualProblems, expected $expectedProblems" }
    }

    private fun lex(text: ByteArray, problems: ProblemLog, create: (ProblemLog) -> ContentLexer): List<Line> {
        val positions = Positions(text)
        val lexer = create(problems)
        val lines = ArrayList<Line>()
        var calls = 0
        while (true) {
            calls++
            invariant(calls <= text.size + 2) { "the lexer does not terminate" }
            val line = lexer.next() ?: break
            positions.check(line, line.startOffset, line.line, line.column)
            checkLineRanges(line)
            lines += line.snapshot()
        }
        return lines
    }

    private fun ContentLine.snapshot(): Line = Line(
        name(),
        bytes.copyOfRange(nameStart, valueEnd).toList(),
        rawValue(),
        startOffset,
        endOffset,
        line,
        column,
    )

    private data class Line(
        val name: String,
        val bytes: List<Byte>,
        val value: String,
        val startOffset: Int,
        val endOffset: Int,
        val line: Int,
        val column: Int,
    )

    private fun reference(bytes: ByteArray, name: String): String {
        val charset = Charset.forName(name)
        return when (name) {
            "UTF-16LE", "UTF-16BE" -> referenceUtf16(bytes, charset)
            "UTF-32LE", "UTF-32BE" -> referenceUtf32(bytes, charset)
            else -> String(bytes, charset)
        }
    }

    // The strict JDK decoder with one correction: for a lead surrogate followed by a unit that is not a trail surrogate
    // the JDK reports a malformed input of 4 bytes and so loses that unit; Unicode (maximal subpart, §3.9) and WHATWG
    // replace only the surrogate, so the reference skips 2 bytes and decodes the unit again.
    private fun referenceUtf16(bytes: ByteArray, charset: Charset): String {
        val decoder = charset.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)
        val input = ByteBuffer.wrap(bytes)
        val out = CharBuffer.allocate(bytes.size + 2)
        while (true) {
            val result = decoder.decode(input, out, true)
            if (result.isUnderflow) break
            val length = if (result.length() == UNPAIRED_LEAD_WITH_NEXT_UNIT) 2 else result.length()
            out.put(REPLACEMENT)
            input.position(input.position() + length)
        }
        decoder.flush(out)
        return out.flip().toString()
    }

    // Unit by unit: the JDK passes surrogate code points through as unpaired surrogates, which are not Unicode scalar
    // values (§3.9, D90) and become U+FFFD here; a unit U+FEFF decodes to the empty string because the JDK strips a
    // byte order mark at the start of every call, so it is restored as the character.
    private fun referenceUtf32(bytes: ByteArray, charset: Charset): String {
        val out = StringBuilder()
        var index = 0
        while (index + 4 <= bytes.size) {
            val unit = String(bytes, index, 4, charset)
            out.append(
                when {
                    unit.isEmpty() -> BYTE_ORDER_MARK.toString()
                    unit.length == 1 && unit[0].isSurrogate() -> REPLACEMENT.toString()
                    else -> unit
                },
            )
            index += 4
        }
        if (index < bytes.size) out.append(REPLACEMENT)
        return out.toString()
    }

    private fun startsWithSignature(text: ByteArray): Boolean {
        fun at(index: Int): Int = if (index < text.size) text[index].toInt() and 0xFF else -1
        return (at(0) == 0xEF && at(1) == 0xBB && at(2) == 0xBF) ||
            (at(0) == '+'.code && at(1) == '/'.code && at(2) == 'v'.code && at(3) in UTF7_FOURTH_BYTES)
    }

    private fun firstMalformedUtf8(bytes: ByteArray): Int {
        val input = ByteBuffer.wrap(bytes)
        val result = Charsets.UTF_8.newDecoder().decode(input, CharBuffer.allocate(bytes.size), true)
        return if (result.isError) input.position() else -1
    }

    private fun ByteArray.hex(): String = joinToString(" ") { "%02X".format(it) }

    private fun String.hex(): String = map { "%04X".format(it.code) }.joinToString(" ")

    private companion object {
        const val UNSUPPORTED = "ICAL-S011"
        const val UNPAIRED_LEAD_WITH_NEXT_UNIT = 4
        const val REPLACEMENT = '\uFFFD'
        const val BYTE_ORDER_MARK = '\uFEFF'
        const val WINDOWS_1252 = "windows-1252"

        val WINDOWS_1252_LABELS = setOf("windows-1252", "cp1252", "iso-8859-1", "latin1", "us-ascii", "ascii")

        val UTF7_FOURTH_BYTES = listOf('8'.code, '9'.code, '+'.code, '/'.code)
        val BOM_LENGTHS = mapOf("UTF-16LE" to 2, "UTF-16BE" to 2, "UTF-32LE" to 4, "UTF-32BE" to 4)

        val HINTS = listOf(
            null, "UTF-8", "utf8", "UTF-16", "UTF-16LE", "utf-16be", "UTF-32", "UTF-32LE", "utf-32be",
            "windows-1251", "cp1251", "KOI8-R", "koi8", "windows-1252", "CP1252", "ISO-8859-1", "latin1",
            "US-ASCII", "ascii", "text/calendar; charset=windows-1251", "shift_jis", "",
        )
    }
}

@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.InternalICalApi
import io.github.calendrum.syntax.ContentLexer
import io.github.calendrum.syntax.ParseLimits
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.CharBuffer

class Utf8DifferentialFuzzTest {
    // Differential test of the UTF-8 handling of the lexer against the JDK. The input bytes are the value of one
    // property `X`; LF and CR in the input become folds (CRLF plus space or HTAB), so the lexer must also join
    // sequences split by a fold. Validity (ICAL-S006) must match the strict JDK decoder, and the problem must point at
    // the first malformed sequence.
    @FuzzTest
    @DictionaryFile(resourcePath = "utf8.dict")
    fun utf8Differential(input: ByteArray) {
        val value = ByteArrayOutputStream(input.size)
        val physical = ByteArrayOutputStream(input.size + 8)
        val physicalOffsets = IntArray(input.size + 1)
        physical.write(PREFIX)
        for (byte in input) {
            when (byte) {
                LF -> physical.write(FOLD_WITH_SPACE)

                CR -> physical.write(FOLD_WITH_TAB)

                else -> {
                    physicalOffsets[value.size()] = physical.size()
                    value.write(byte.toInt())
                    physical.write(byte.toInt())
                }
            }
        }
        physical.write(LINE_END)
        val bytes = value.toByteArray()
        val source = physical.toByteArray()

        val problems = ProblemLog(source)
        val lexer = ContentLexer(source, ParseLimits.Default, problems)
        val line = lexer.next() ?: throw AssertionError("no line")
        invariant(lexer.next() == null) { "more than one line" }
        invariant(line.name() == "X" && line.parameterCount == 0) { "line ${line.name()}" }
        invariant(line.bytes.copyOfRange(line.valueStart, line.valueEnd).contentEquals(bytes)) {
            "unfolded value differs"
        }
        invariant(problems.problems.all { it.code.id == INVALID_UTF8 }) { "problems: ${problems.problems}" }
        problems.check(ParseLimits.Default)

        val firstMalformed = firstMalformed(bytes)
        val reported = problems.problems.singleOrNull()
        invariant((reported == null) == (firstMalformed < 0)) {
            "validity: lexer ${reported == null}, JDK strict decoder ${firstMalformed < 0} for ${bytes.hex()}"
        }
        if (reported != null) {
            invariant(reported.span.startOffset.toInt() == physicalOffsets[firstMalformed]) {
                "$INVALID_UTF8 at ${reported.span.startOffset.toInt()}, first malformed byte at ${physicalOffsets[firstMalformed]}"
            }
        }
        val expected = decodeMaximalSubparts(bytes)
        val actual = line.rawValue()
        invariant(actual == expected) { "value of ${bytes.hex()}: ${actual.hex()}, expected ${expected.hex()}" }
    }

    // Expected value: the JDK decoding with one correction. For ED followed by A0..BF (an encoded surrogate) the JDK
    // replaces the whole 2- or 3-byte sequence with one U+FFFD. Unicode §3.9 (Table 3-7: after ED only 80..9F is valid)
    // makes ED a maximal subpart by itself, and the recommended practice replaces every maximal subpart, so ED and
    // A0..BF give two U+FFFD; the WHATWG Encoding Standard does the same. Everything before and after such a pair
    // decodes independently, because ED is never a continuation byte. Found by this target.
    private fun decodeMaximalSubparts(bytes: ByteArray): String {
        val out = StringBuilder(bytes.size)
        var from = 0
        var index = 0
        while (index + 1 < bytes.size) {
            if (bytes[index] == SURROGATE_LEAD && (bytes[index + 1].toInt() and 0xFF) in SURROGATE_SECOND) {
                out.append(String(bytes, from, index - from, Charsets.UTF_8)).append(REPLACEMENT).append(REPLACEMENT)
                index += 2
                from = index
            } else {
                index++
            }
        }
        return out.append(String(bytes, from, bytes.size - from, Charsets.UTF_8)).toString()
    }

    private fun firstMalformed(bytes: ByteArray): Int {
        val input = ByteBuffer.wrap(bytes)
        val result = Charsets.UTF_8.newDecoder().decode(input, CharBuffer.allocate(bytes.size), true)
        return if (result.isError) input.position() else -1
    }

    private fun ByteArray.hex(): String = joinToString(" ") { "%02X".format(it) }

    private fun String.hex(): String = map { "%04X".format(it.code) }.joinToString(" ")

    private companion object {
        const val INVALID_UTF8 = "ICAL-S006"
        const val SURROGATE_LEAD = 0xED.toByte()
        const val REPLACEMENT = '\uFFFD'
        val SURROGATE_SECOND = 0xA0..0xBF
        val PREFIX = "X:".toByteArray()
        val FOLD_WITH_SPACE = "\r\n ".toByteArray()
        val FOLD_WITH_TAB = "\r\n\t".toByteArray()
        val LINE_END = "\r\n".toByteArray()
    }
}

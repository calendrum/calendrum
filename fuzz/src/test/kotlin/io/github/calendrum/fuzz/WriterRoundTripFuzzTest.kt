@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Severity
import io.github.calendrum.syntax.AppendableLineWriter
import io.github.calendrum.syntax.ByteLineWriter
import io.github.calendrum.syntax.ContentLexer
import io.github.calendrum.syntax.ContentLineWriter
import io.github.calendrum.syntax.ParseLimits

class WriterRoundTripFuzzTest {
    // The input is a script: steps separated by NUL, the first character of a step is the call (B begin, E end, P
    // property, A parameter, a parameterValue, V value, R reset, S size and toByteArray of the byte writer, which the
    // Appendable writer does not have), the rest is its argument. U+E000, U+E001 and U+E002 stand for the unpaired
    // surrogates U+D800 and U+DC00 and for NUL, which UTF-8 input cannot carry. Every step runs on a byte writer and on
    // an Appendable writer; both must give the outcome the model predicts. After an IllegalArgumentException the
    // Appendable keeps the partial line, so the test cuts it back to the last complete line before comparing.
    @FuzzTest
    @DictionaryFile(resourcePath = "writer.dict")
    fun writerRoundTrip(input: ByteArray) {
        val bytes = ByteLineWriter()
        val text = StringBuilder()
        val chars = AppendableLineWriter(text)
        val model = WriterModel()
        var committedBytes = 0
        var committedChars = 0
        for (step in String(input, Charsets.UTF_8).split(OPERATION_SEPARATOR)) {
            if (step.isEmpty()) continue
            val operation = step[0]
            val argument = decodeEscapes(step.substring(1))
            val expected = model.apply(operation, argument) ?: continue
            val byteOutcome = run(bytes, operation, argument)
            val charOutcome = run(chars, operation, argument)
            invariant(byteOutcome == expected) { "bytes: $operation '$argument' gave $byteOutcome, expected $expected" }
            invariant(charOutcome == expected) {
                "Appendable: $operation '$argument' gave $charOutcome, expected $expected"
            }
            when {
                operation == RESET -> {
                    text.setLength(0)
                    committedBytes = 0
                    committedChars = 0
                }

                expected == Outcome.ARGUMENT -> text.setLength(committedChars)

                model.idle -> {
                    committedBytes = bytes.size
                    committedChars = text.length
                }
            }
        }
        val output = bytes.toByteArray()
        invariant(output.size == bytes.size) { "size ${bytes.size} for ${output.size} bytes" }
        invariant(output.contentEquals(text.toString().encodeToByteArray())) { "bytes and Appendable differ" }
        val lines = output.copyOf(committedBytes)
        checkFolding(lines)
        val problems = ProblemLog(lines)
        val lexer = ContentLexer(lines, ParseLimits.Default, problems)
        val read = ArrayList<ExpectedLine>()
        while (true) {
            val line = lexer.next() ?: break
            val parameters = (0 until line.parameterCount).map { parameter ->
                line.parameterName(parameter) to
                    (0 until line.parameterValueCount(parameter)).map { line.parameterValue(parameter, it) }
            }
            read += ExpectedLine(line.name(), parameters, line.rawValue())
        }
        invariant(problems.problems.all { it.severity == Severity.Info }) { "problems: ${problems.problems}" }
        invariant(read == model.lines) { "read back ${read.size} lines, wrote ${model.lines.size}" }
    }

    private fun run(writer: ContentLineWriter, operation: Char, argument: String): Outcome = try {
        when (operation) {
            BEGIN -> writer.begin(argument)

            END -> writer.end(argument)

            PROPERTY -> writer.property(argument)

            PARAMETER -> writer.parameter(argument)

            PARAMETER_VALUE -> writer.parameterValue(argument)

            VALUE -> writer.value(argument)

            RESET -> writer.reset()

            else -> if (writer is ByteLineWriter) {
                invariant(writer.toByteArray().size == writer.size) { "size differs from toByteArray()" }
            }
        }
        Outcome.OK
    } catch (e: IllegalStateException) {
        Outcome.STATE
    } catch (e: IllegalArgumentException) {
        Outcome.ARGUMENT
    }

    // Physical lines of at most 75 octets ending in CRLF; a continuation line never starts inside a UTF-8 sequence, and
    // the fold was needed: the first code point of the continuation would not have fit on the previous line.
    private fun checkFolding(output: ByteArray) {
        var start = 0
        while (start < output.size) {
            var end = start
            while (end < output.size && output[end] != CR && output[end] != LF) end++
            invariant(end + 1 < output.size && output[end] == CR && output[end + 1] == LF) {
                "physical line at $start does not end with CRLF"
            }
            invariant(end - start <= LIMIT) { "physical line at $start has ${end - start} octets" }
            val next = end + 2
            if (next < output.size && output[next] == SPACE) {
                invariant(next + 1 < output.size && output[next + 1] != CR) { "empty continuation line at $next" }
                val lead = output[next + 1].toInt() and 0xFF
                invariant(lead and 0xC0 != 0x80) { "fold at $end splits a UTF-8 sequence" }
                invariant(end - start + utf8Length(lead) > LIMIT) { "fold at $end is not greedy" }
            }
            start = next
        }
    }

    private fun utf8Length(lead: Int): Int = when {
        lead < 0x80 -> 1
        lead < 0xE0 -> 2
        lead < 0xF0 -> 3
        else -> 4
    }

    private fun decodeEscapes(value: String): String {
        if (value.none { it in ESCAPES }) return value
        return buildString(value.length) {
            for (char in value) append(ESCAPES[char] ?: char)
        }
    }

    private enum class Outcome { OK, STATE, ARGUMENT }

    private data class ExpectedLine(
        val name: String,
        val parameters: List<Pair<String, List<String>>>,
        val value: String,
    )

    // Expected behaviour of ContentLineWriter: a call out of order is IllegalStateException and changes nothing; an
    // invalid name or value is IllegalArgumentException and drops the unfinished line. Names: letters, digits and '-',
    // written in upper case. Parameter values: no control characters except HTAB, CR and LF (CR, LF and CRLF read back
    // as LF, RFC 6868). Property values: no control characters except HTAB. Unpaired surrogates are invalid in both.
    private class WriterModel {
        val lines = ArrayList<ExpectedLine>()
        private var state = IDLE
        private var name = ""
        private val parameters = ArrayList<Pair<String, MutableList<String>>>()

        val idle: Boolean get() = state == IDLE

        fun apply(operation: Char, argument: String): Outcome? = when (operation) {
            BEGIN, END -> when {
                state != IDLE -> Outcome.STATE

                !isName(argument) -> Outcome.ARGUMENT

                else -> {
                    lines += ExpectedLine(if (operation == BEGIN) "BEGIN" else "END", emptyList(), argument.uppercase())
                    Outcome.OK
                }
            }

            PROPERTY -> when {
                state != IDLE -> Outcome.STATE

                !isName(argument) -> Outcome.ARGUMENT

                else -> {
                    name = argument.uppercase()
                    parameters.clear()
                    state = NAME
                    Outcome.OK
                }
            }

            PARAMETER -> when {
                state != NAME && state != WITH_VALUES -> Outcome.STATE

                !isName(argument) -> discard()

                else -> {
                    parameters += argument.uppercase() to ArrayList()
                    state = WITHOUT_VALUES
                    Outcome.OK
                }
            }

            PARAMETER_VALUE -> when {
                state != WITHOUT_VALUES && state != WITH_VALUES -> Outcome.STATE

                !isParameterValue(argument) -> discard()

                else -> {
                    parameters.last().second += argument.replace("\r\n", "\n").replace('\r', '\n')
                    state = WITH_VALUES
                    Outcome.OK
                }
            }

            VALUE -> when {
                state != NAME && state != WITH_VALUES -> Outcome.STATE

                !isPropertyValue(argument) -> discard()

                else -> {
                    lines += ExpectedLine(name, parameters.map { it.first to it.second.toList() }, argument)
                    state = IDLE
                    Outcome.OK
                }
            }

            RESET -> {
                lines.clear()
                state = IDLE
                Outcome.OK
            }

            SIZE -> Outcome.OK

            else -> null
        }

        private fun discard(): Outcome {
            state = IDLE
            return Outcome.ARGUMENT
        }

        private fun isName(value: String): Boolean =
            value.isNotEmpty() && value.all { it == '-' || it in '0'..'9' || it in 'A'..'Z' || it in 'a'..'z' }

        private fun isParameterValue(value: String): Boolean = isWellFormed(value) &&
            value.none { it != '\r' && it != '\n' && ((it < ' ' && it != '\t') || it == '\u007F') }

        private fun isPropertyValue(value: String): Boolean =
            isWellFormed(value) && value.none { (it < ' ' && it != '\t') || it == '\u007F' }

        private fun isWellFormed(value: String): Boolean {
            var index = 0
            while (index < value.length) {
                val char = value[index]
                if (char.isHighSurrogate() && index + 1 < value.length && value[index + 1].isLowSurrogate()) {
                    index += 2
                } else if (char.isSurrogate()) {
                    return false
                } else {
                    index++
                }
            }
            return true
        }

        private companion object {
            const val IDLE = 0
            const val NAME = 1
            const val WITHOUT_VALUES = 2
            const val WITH_VALUES = 3
        }
    }

    private companion object {
        const val OPERATION_SEPARATOR = '\u0000'
        const val BEGIN = 'B'
        const val END = 'E'
        const val PROPERTY = 'P'
        const val PARAMETER = 'A'
        const val PARAMETER_VALUE = 'a'
        const val VALUE = 'V'
        const val RESET = 'R'
        const val SIZE = 'S'
        const val LIMIT = 75

        val ESCAPES = mapOf('' to '\uD800', '' to '\uDC00', '' to '\u0000')
    }
}

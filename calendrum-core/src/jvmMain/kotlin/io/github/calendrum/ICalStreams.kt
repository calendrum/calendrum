@file:JvmName("ICalStreams")

package io.github.calendrum

import io.github.calendrum.model.IcsSchema
import io.github.calendrum.syntax.ComponentStream
import io.github.calendrum.syntax.DEFAULT_BLOCK_SIZE
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ICalWriter
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseResult
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import java.time.Duration
import java.util.stream.Stream
import kotlin.streams.asStream
import kotlin.time.toKotlinDuration

/** Adapts this stream to a [ByteInput]. Reading is passed to the stream; the stream is never closed by the library. */
public fun InputStream.asByteInput(): ByteInput = InputStreamByteInput(this)

/**
 * Adapts this stream to a [ByteOutput]. Writing is passed to the stream; it is neither flushed nor closed by the
 * library.
 */
public fun OutputStream.asByteOutput(): ByteOutput = OutputStreamByteOutput(this)

/**
 * Parses everything [input] yields, reading it block by block; the stream is not closed. The result equals that of
 * parsing the same bytes as an array.
 *
 * @throws IOException if reading fails.
 * @throws ICalParseException in strict mode, at the first error.
 */
@Throws(IOException::class, ICalParseException::class)
public fun ICalParser.parse(input: InputStream): ParseResult = parse(input.asByteInput())

/**
 * Parses the text [input] yields as UTF-8; no charset detection happens. A lone surrogate is reported as `ICAL-S006`.
 * Positions count bytes of the UTF-8 form. The reader is not closed.
 *
 * @throws IOException if reading fails.
 * @throws ICalParseException in strict mode, at the first error.
 */
@Throws(IOException::class, ICalParseException::class)
public fun ICalParser.parse(input: Reader): ParseResult = parse(ReaderByteInput(input), DEFAULT_BLOCK_SIZE, text = true)

/**
 * Returns a stream of the children of every `VCALENDAR` that [input] yields, one component at a time; the stream is not
 * closed.
 *
 * @throws IOException if reading fails.
 */
@Throws(IOException::class)
public fun ICalParser.components(input: InputStream): ComponentStream = components(input.asByteInput())

/**
 * Writes [component] to [out] as UTF-8 bytes; [out] is not flushed or closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 */
@Throws(IOException::class)
public fun ICalWriter.write(component: ICalComponent, out: OutputStream) {
    write(component, out.asByteOutput())
}

/**
 * Writes all [components] to [out] as UTF-8 bytes; [out] is not flushed or closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 */
@Throws(IOException::class)
public fun ICalWriter.write(components: List<ICalComponent>, out: OutputStream) {
    write(components, out.asByteOutput())
}

/**
 * Decodes everything [input] yields into objects of [schema] as a Java stream, with bounded memory. The stream can be
 * consumed once.
 */
public fun <T : Any> ICalParser.decodeStream(input: ByteInput, schema: IcsSchema<T>): Stream<T> =
    decode(input, schema).asStream()

/**
 * Decodes everything [input] yields into objects of [schema] as a Java stream, with bounded memory; the stream is not
 * closed.
 */
public fun <T : Any> ICalParser.decodeStream(input: InputStream, schema: IcsSchema<T>): Stream<T> =
    decode(input.asByteInput(), schema).asStream()

// Java cannot create a `kotlin.time.Duration` (an inline class), so the budget takes a `java.time.Duration`
// here; Java calls it as `ICalStreams.maxTime(builder, duration)`.

/**
 * Sets the time budget from a `java.time.Duration`; `null` removes it. Helps Java callers, who cannot use
 * `kotlin.time.Duration`.
 */
public fun ParseLimits.Builder.maxTime(value: Duration?): ParseLimits.Builder = maxTime(value?.toKotlinDuration())

/**
 * Writes [component] as text to [out], which is not flushed or closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
@Throws(IOException::class)
public fun ICalWriter.write(component: ICalComponent, out: Writer) {
    write(component, out as Appendable)
}

/**
 * Writes all [components] as text to [out], which is not flushed or closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
@Throws(IOException::class)
public fun ICalWriter.write(components: List<ICalComponent>, out: Writer) {
    write(components, out as Appendable)
}

private class InputStreamByteInput(private val stream: InputStream) : ByteInput {
    override fun read(buffer: ByteArray, offset: Int, length: Int): Int = stream.read(buffer, offset, length)

    override fun toString(): String = "ByteInput($stream)"
}

private class OutputStreamByteOutput(private val stream: OutputStream) : ByteOutput {
    override fun write(buffer: ByteArray, offset: Int, length: Int) {
        stream.write(buffer, offset, length)
    }

    override fun toString(): String = "ByteOutput($stream)"
}

private class ReaderByteInput(private val reader: Reader) : ByteInput {
    private val chars = CharArray(CHUNK)
    private val bytes = ByteArray(CHUNK * MAX_UTF8_PER_CHAR)
    private var carry = 0
    private var position = 0
    private var count = 0
    private var ended = false

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (position == count && !refill()) return -1
        val size = minOf(length, count - position)
        bytes.copyInto(buffer, offset, position, position + size)
        position += size
        return size
    }

    private fun refill(): Boolean {
        position = 0
        count = 0
        while (count == 0) {
            if (ended) return false
            val read = reader.read(chars, carry, chars.size - carry)
            if (read < 0) {
                ended = true
                if (carry == 0) return false
                bytes[count++] = INVALID_BYTE
                carry = 0
                return true
            }
            val total = carry + read
            var limit = total
            if (limit > 0 && chars[limit - 1].isHighSurrogate()) limit--
            encode(limit)
            carry = total - limit
            if (carry > 0) chars[0] = chars[limit]
        }
        return true
    }

    private fun encode(limit: Int) {
        var index = 0
        while (index < limit) {
            val char = chars[index]
            val code = char.code
            if (char.isHighSurrogate() && index + 1 < limit && chars[index + 1].isLowSurrogate()) {
                val point = Character.toCodePoint(char, chars[index + 1])
                bytes[count++] = (0xF0 or (point shr 18)).toByte()
                bytes[count++] = (0x80 or ((point shr 12) and 0x3F)).toByte()
                bytes[count++] = (0x80 or ((point shr 6) and 0x3F)).toByte()
                bytes[count++] = (0x80 or (point and 0x3F)).toByte()
                index += 2
                continue
            }
            when {
                char.isSurrogate() -> bytes[count++] = INVALID_BYTE

                code < 0x80 -> bytes[count++] = code.toByte()

                code < 0x800 -> {
                    bytes[count++] = (0xC0 or (code shr 6)).toByte()
                    bytes[count++] = (0x80 or (code and 0x3F)).toByte()
                }

                else -> {
                    bytes[count++] = (0xE0 or (code shr 12)).toByte()
                    bytes[count++] = (0x80 or ((code shr 6) and 0x3F)).toByte()
                    bytes[count++] = (0x80 or (code and 0x3F)).toByte()
                }
            }
            index++
        }
    }

    override fun toString(): String = "ByteInput($reader)"

    private companion object {
        private const val CHUNK = 8 * 1024
        private const val MAX_UTF8_PER_CHAR = 3
        private const val INVALID_BYTE: Byte = -1
    }
}

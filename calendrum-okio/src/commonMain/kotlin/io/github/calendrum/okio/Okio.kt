@file:JvmName("ICalOkio")

package io.github.calendrum.okio

import io.github.calendrum.ByteInput
import io.github.calendrum.ByteOutput
import io.github.calendrum.ICalParseException
import io.github.calendrum.syntax.ComponentStream
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ICalReader
import io.github.calendrum.syntax.ICalWriter
import io.github.calendrum.syntax.ParseResult
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource
import okio.IOException
import kotlin.jvm.JvmName

// Thin adapter (as `KotlinxIo.kt`): reads go straight to `read` with the block the core asks for, writes to `write`;
// the sink is not flushed, as with the java.io adapters of the core.
/**
 * Adapts this Okio `BufferedSource` to a [ByteInput]; reading is passed to the source, which the library never closes.
 */
public fun BufferedSource.asByteInput(): ByteInput = SourceByteInput(this)

/**
 * Adapts this Okio `BufferedSink` to a [ByteOutput]; writing is passed to the sink, which the library neither flushes
 * nor closes.
 */
public fun BufferedSink.asByteOutput(): ByteOutput = SinkByteOutput(this)

/**
 * Parses everything [source] yields, reading it block by block. An Okio `Buffer` of up to 16 MiB, which already holds
 * all its bytes, is copied into one array and parsed as an array, which is faster; while it is copied the bytes are in
 * memory twice, so a larger buffer is read block by block. The result equals that of parsing the same bytes as an
 * array. The source is not closed.
 *
 * @throws IOException if reading fails.
 * @throws ICalParseException in strict mode, at the first error.
 * @sample io.github.calendrum.samples.parseAndWriteOkio
 */
@Throws(IOException::class, ICalParseException::class)
public fun ICalParser.parse(source: BufferedSource): ParseResult =
    if (source is Buffer && source.size <= MAX_ARRAY) parse(source.readByteArray()) else parse(source.asByteInput())

/**
 * Returns a pull cursor over everything [source] yields, without building a tree; the source is not closed.
 *
 * @throws IOException if reading fails.
 */
@Throws(IOException::class)
public fun ICalParser.reader(source: BufferedSource): ICalReader = reader(source.asByteInput())

/**
 * Returns a stream of the children of every `VCALENDAR` that [source] yields, one component at a time; the source is
 * not closed. An Okio `Buffer` of up to 16 MiB is copied into one array and streamed as an array, as in `parse`.
 *
 * @throws IOException if reading fails.
 */
@Throws(IOException::class)
public fun ICalParser.components(source: BufferedSource): ComponentStream = if (source is Buffer &&
    source.size <= MAX_ARRAY
) {
    components(source.readByteArray())
} else {
    components(source.asByteInput())
}

/**
 * Writes [component] to [sink] as UTF-8; the sink is neither flushed nor closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @sample io.github.calendrum.samples.parseAndWriteOkio
 */
@Throws(IOException::class)
public fun ICalWriter.write(component: ICalComponent, sink: BufferedSink) {
    write(component, sink.asByteOutput())
}

/**
 * Writes all [components] to [sink] as UTF-8; the sink is neither flushed nor closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 */
@Throws(IOException::class)
public fun ICalWriter.write(components: List<ICalComponent>, sink: BufferedSink) {
    write(components, sink.asByteOutput())
}

private class SourceByteInput(private val source: BufferedSource) : ByteInput {
    override fun read(buffer: ByteArray, offset: Int, length: Int): Int = source.read(buffer, offset, length)

    override fun toString(): String = "ByteInput($source)"
}

private class SinkByteOutput(private val sink: BufferedSink) : ByteOutput {
    override fun write(buffer: ByteArray, offset: Int, length: Int) {
        sink.write(buffer, offset, length)
    }

    override fun toString(): String = "ByteOutput($sink)"
}

// A Buffer holds its bytes already: copying them into one array costs less than the block-by-block path of the core
// (28–39 % slower than an array in the benchmarks). `readByteArray` fills the array before it releases the
// segments, so the copy doubles the memory for a moment; above 16 MiB the block path, whose window is a few blocks, is
// worth its time.
private const val MAX_ARRAY = 16L * 1024 * 1024

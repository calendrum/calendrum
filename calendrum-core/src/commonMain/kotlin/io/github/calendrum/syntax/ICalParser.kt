package io.github.calendrum.syntax

import io.github.calendrum.ByteInput
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICalParseException
import io.github.calendrum.ProblemListener
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.SchemaDecoder
import io.github.calendrum.model.SchemaResult
import io.github.calendrum.model.frozenCopy
import kotlin.Throws

// Cheap and stateless: every `parse` or `reader` creates its own reader, collector and tree builder. Byte input goes
// through the charset decoder with `options.charset` as the hint; a `String` is encoded to UTF-8 and read as text
// (`textInput`).
/**
 * Reads iCalendar data with a fixed set of [ParseOptions].
 *
 * Obtain a parser from `ICal.parser()` or `ICal.parser(options)`. A parser is immutable and thread-safe: it holds only
 * its options, and every call to `parse`, `reader` or `components` gets its own state. Per-parse state, such as a
 * stateful [LineInterceptor], is created from the factories in the options for each call.
 *
 * The same input gives the same tree and the same problems in every form: a `ByteArray`, a `String`, a
 * [io.github.calendrum.ByteInput] with any block size, or an `InputStream` on the JVM. Positions in problems are offsets in
 * bytes from the start of the input.
 *
 * In lenient mode (the default) a parser never throws on data. In strict mode it throws [ICalParseException] at the
 * first problem with severity [io.github.calendrum.Severity.Error]. Calls that return a [ParseResult] are `parse`, calls
 * that stream are `reader` and `components`.
 *
 * @property options the options this parser applies.
 */
public class ICalParser internal constructor(public val options: ParseOptions) {
    /**
     * Parses the whole [bytes] into a tree.
     *
     * The encoding is detected as described in [ParseOptions.charset]. The result keeps the problems found and, unless
     * the options say otherwise, references [bytes] instead of copying them, so do not change the array while the
     * result is in use.
     *
     * @param bytes the complete content.
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun parse(bytes: ByteArray): ParseResult = parse(bytes, 0, bytes.size)

    /**
     * Parses [length] bytes of [bytes] starting at [offset]. Offsets in positions are relative to [offset].
     *
     * @param bytes an array that holds the content.
     * @param offset the index of the first byte to parse.
     * @param length the number of bytes to parse.
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun parse(bytes: ByteArray, offset: Int, length: Int): ParseResult {
        val collector = collector(keep = true)
        return read(byteReader(bytes, offset, length, collector), collector, options.retainSource, length)
    }

    /**
     * Parses [text] into a tree.
     *
     * The text is encoded to UTF-8 first, so no charset detection happens, only a leading U+FEFF is reported as
     * `ICAL-S001`. Positions count bytes of the UTF-8 form.
     *
     * @param text the complete content.
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun parse(text: String): ParseResult {
        val bytes = encodeText(text)
        val collector = collector(keep = true)
        return read(textReader(bytes, collector), collector, options.retainSource, bytes.size)
    }

    /**
     * Parses everything [input] yields, reading it block by block.
     *
     * The tree does not keep the input, so it is always a copy of the lines it needs; the memory held while reading is
     * the tree plus a block. For a feed too large to hold as a tree use [components] instead. [input] is read to its
     * end and not closed.
     *
     * @param input the source of the content.
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun parse(input: ByteInput): ParseResult = parse(input, DEFAULT_BLOCK_SIZE, text = false)

    /**
     * Returns a pull cursor over [bytes] that reports events without building a tree.
     *
     * The cursor allocates nothing per event; see [ICalReader] for what each event offers.
     *
     * @param bytes the complete content.
     */
    public fun reader(bytes: ByteArray): ICalReader = reader(bytes, 0, bytes.size)

    /** Returns a pull cursor over [length] bytes of [bytes] starting at [offset]. */
    public fun reader(bytes: ByteArray, offset: Int, length: Int): ICalReader {
        val collector = collector(keep = strict)
        return ICalReader(byteReader(bytes, offset, length, collector), options.retainSource, length, false)
    }

    /** Returns a pull cursor over [text], encoded to UTF-8. */
    public fun reader(text: String): ICalReader {
        val bytes = encodeText(text)
        val collector = collector(keep = strict)
        return ICalReader(textReader(bytes, collector), options.retainSource, bytes.size, false)
    }

    /** Returns a pull cursor over everything [input] yields; the input is read block by block and not closed. */
    public fun reader(input: ByteInput): ICalReader = reader(input, DEFAULT_BLOCK_SIZE)

    /**
     * Returns a stream of the children of every `VCALENDAR` in [bytes], one component at a time.
     *
     * @param bytes the complete content.
     */
    public fun components(bytes: ByteArray): ComponentStream {
        val collector = collector(keep = true)
        return ComponentStream(byteReader(bytes, 0, bytes.size, collector), collector, options.retainSource, bytes.size)
    }

    /**
     * Returns a stream of the children of every `VCALENDAR` that [input] yields, one component at a time, with memory
     * bounded by a block and the longest component. The input is not closed.
     */
    public fun components(input: ByteInput): ComponentStream = components(input, DEFAULT_BLOCK_SIZE)

    // UTF-8 input: the byte range of the fingerprint in the input itself. Transcoded input: the whole input is decoded
    // again with `decodeInput` and no hint (the same decision as the fingerprint pass), then the range of the decoded
    // text. The lexer starts at the absolute offset and line of the component (`startAt`), and a child of a calendar
    // gets `maxDepth - 1`, so spans and the depth limit match the full parse.

    /**
     * Parses only the component that [fingerprint] points at, by its offsets, without reading the rest of [bytes].
     *
     * The result equals the corresponding subtree of a full parse. [bytes] must be the input the fingerprint was
     * computed from.
     *
     * @param bytes the content that was fingerprinted.
     * @param fingerprint an entry of [Fingerprints.entries] or of a [FingerprintDiff].
     * @throws IllegalArgumentException if the fingerprint does not fit [bytes].
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun parseComponent(bytes: ByteArray, fingerprint: ComponentFingerprint): ICalComponent {
        val source: ByteArray
        val size: Int
        if (fingerprint.charset == InputCharset.Utf8) {
            source = bytes
            size = bytes.size
        } else {
            val decoded = decodeInput(bytes, null, IGNORED_PROBLEMS)
            source = decoded.bytes
            size = decoded.offset + decoded.length
        }
        val start = fingerprint.startOffset
        val end = fingerprint.endOffset
        require(end <= size && start < end) { "The fingerprint does not fit this input" }
        val length = (end - start).toInt()
        val collector = collector(keep = true)
        val nested = fingerprint.key.calendarIndex >= 0 && options.limits.maxDepth > 1
        val limits = if (nested) options.limits.withMaxDepth(options.limits.maxDepth - 1) else options.limits
        val reader = ContentReader(source, start.toInt(), length, ContentSelection.Full, limits, collector)
        reader.startAt(start, fingerprint.line)
        val components = TreeBuilder(configured(reader), options.retainSource, length, false).build()
        require(components.isNotEmpty()) { "The fingerprint does not point at a component of this input" }
        return components[0]
    }

    /**
     * Decodes [bytes] straight into objects of [schema], without building a tree.
     *
     * Only the properties the schema names are read. Components that miss a required field are skipped; use
     * [decodeWithProblems] to see why.
     *
     * @param bytes the complete content.
     * @param schema describes the component and its fields.
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun <T : Any> decode(bytes: ByteArray, schema: IcsSchema<T>): List<T> =
        decodeWithProblems(bytes, schema).items

    /**
     * Decodes everything [input] yields into objects of [schema], one at a time, with bounded memory.
     *
     * The sequence can be iterated once and reads the input as it goes; a strict parser throws [ICalParseException]
     * from the iterator.
     */
    public fun <T : Any> decode(input: ByteInput, schema: IcsSchema<T>): Sequence<T> = Sequence {
        val collector = collector(keep = false)
        val reader = streamReader(input, DEFAULT_BLOCK_SIZE, false, collector, schema.compiled.selection.content)
        reader.values = null
        val decoder = SchemaDecoder(reader, schema, strict, STREAM_SIZE)
        generateSequence { decoder.next() }.iterator()
    }.constrainOnce()

    /**
     * Decodes [bytes] into objects of [schema] and also returns the problems and the number of skipped components.
     *
     * @param bytes the complete content.
     * @param schema describes the component and its fields.
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun <T : Any> decodeWithProblems(bytes: ByteArray, schema: IcsSchema<T>): SchemaResult<T> {
        val collector = collector(keep = true)
        val decoder = schemaDecoder(bytes, schema, collector)
        val items = ArrayList<T>()
        while (true) items += decoder.next() ?: break
        return SchemaResult(frozenCopy(items), ArrayView(collector.problems.toTypedArray()), decoder.skipped)
    }

    internal fun <T : Any> schemaDecoder(
        bytes: ByteArray,
        schema: IcsSchema<T>,
        collector: ProblemCollector,
    ): SchemaDecoder<T> {
        val reader = byteReader(bytes, 0, bytes.size, collector, schema.compiled.selection.content)
        reader.values = null
        return SchemaDecoder(reader, schema, strict, bytes.size)
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "ICalParser($options)"

    internal fun parse(input: ByteInput, blockSize: Int, text: Boolean): ParseResult {
        val collector = collector(keep = true)
        return read(streamReader(input, blockSize, text, collector), collector, false, STREAM_SIZE)
    }

    internal fun reader(input: ByteInput, blockSize: Int): ICalReader {
        val collector = collector(keep = strict)
        return ICalReader(streamReader(input, blockSize, false, collector), false, STREAM_SIZE, true)
    }

    internal fun components(input: ByteInput, blockSize: Int): ComponentStream {
        val collector = collector(keep = true)
        return ComponentStream(streamReader(input, blockSize, false, collector), collector, false, STREAM_SIZE)
    }

    private val strict: Boolean get() = options.mode == ParseMode.Strict

    private fun collector(keep: Boolean): ProblemCollector = ProblemCollector(options.problemListener, strict, keep)

    private fun byteReader(
        bytes: ByteArray,
        offset: Int,
        length: Int,
        collector: ProblemCollector,
        selection: ContentSelection = options.contentSelection,
    ): ContentReader {
        val reader = ContentReader(
            bytes,
            offset,
            length,
            selection,
            options.limits,
            collector,
            true,
            options.charset,
        )
        return configured(reader)
    }

    private fun textReader(bytes: ByteArray, collector: ProblemCollector): ContentReader {
        val reader = ContentReader(bytes, 0, bytes.size, options.contentSelection, options.limits, collector)
        reader.textInput = true
        return configured(reader)
    }

    private fun streamReader(
        input: ByteInput,
        blockSize: Int,
        text: Boolean,
        collector: ProblemCollector,
        selection: ContentSelection = options.contentSelection,
    ): ContentReader {
        val reader = ContentReader(
            input,
            blockSize,
            selection,
            options.limits,
            collector,
            !text,
            if (text) null else options.charset,
        )
        reader.textInput = text
        return configured(reader)
    }

    @OptIn(ExperimentalICalApi::class)
    private fun configured(reader: ContentReader): ContentReader {
        reader.strict = strict
        reader.budget = options.budget
        reader.values = options.valueHook
        val interceptors = options.interceptors
        if (interceptors.isNotEmpty()) reader.interceptors = InterceptorChain(interceptors)
        return reader
    }

    private fun read(
        reader: ContentReader,
        collector: ProblemCollector,
        retainSource: Boolean,
        size: Int,
    ): ParseResult {
        val built = TreeBuilder(reader, retainSource, size, false).build()
        val components = if (options.referencedTimeZones) pruneTimeZones(built) else built
        return ParseResult(
            components,
            collector.problems.toTypedArray(),
            reader.problemCounts(),
            reader.valueProblemCounts(),
            reader.decodedCharset.canonicalName,
            reader.charsetConfidence,
            options.partial,
        )
    }
}

private val IGNORED_PROBLEMS = ProblemListener { }

internal const val DEFAULT_BLOCK_SIZE: Int = 64 * 1024

private const val STREAM_SIZE = Int.MAX_VALUE

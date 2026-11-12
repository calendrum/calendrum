package io.github.calendrum

import io.github.calendrum.syntax.FingerprintOptions
import io.github.calendrum.syntax.FingerprintScanner
import io.github.calendrum.syntax.Fingerprints
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ICalWriter
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.ParseResult
import io.github.calendrum.syntax.WriteOptions
import kotlin.jvm.JvmStatic

/**
 * Entry point of the library: parses iCalendar text and bytes, writes component trees back, and fingerprints feeds.
 *
 * Every function uses shared, immutable default settings, so nothing here has global mutable state and all of it is
 * safe to call from any thread. Use [parser] or [writer] with options to change the behavior, or the `ICal.parser { ...
 * }` extension to build [ParseOptions] with a DSL.
 *
 * @sample io.github.calendrum.samples.parseText
 */
public object ICal {
    private val defaultParser: ICalParser = ICalParser(ParseOptions.Default)
    private val defaultWriter: ICalWriter = ICalWriter(WriteOptions.Default)

    /**
     * Parses iCalendar [text] with the default options: lenient mode, the default limits and a retained source.
     *
     * Lenient parsing never throws on bad data. Problems found in the input are collected in [ParseResult.problems];
     * the result is a lossless tree of top-level components, usually one `VCALENDAR` ([RFC 5545
     * §3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.4)). Values are not decoded here; they are decoded on
     * first access.
     *
     * Positions in problems count bytes of the UTF-8 form of [text]. Use [parser] with `ParseMode.Strict` to get an
     * [ICalParseException] on the first error instead.
     *
     * @param text the complete iCalendar content.
     * @return the parsed components with the problems found.
     * @sample io.github.calendrum.samples.parseText
     */
    @JvmStatic
    public fun parse(text: String): ParseResult = defaultParser.parse(text)

    /**
     * Parses iCalendar [bytes] with the default options: lenient mode, the default limits and a retained source.
     *
     * The encoding is detected from a byte order mark and from the data: UTF-8 is read as is, UTF-16 and UTF-32 are
     * transcoded, and windows-1251, KOI8-R and windows-1252 are recognized by a heuristic. What was decided is reported
     * in [ParseResult.charset] and by the problems `ICAL-S024` to `ICAL-S028`. Lenient parsing never throws on bad
     * data.
     *
     * @param bytes the complete iCalendar content.
     * @return the parsed components with the problems found.
     */
    @JvmStatic
    public fun parse(bytes: ByteArray): ParseResult = defaultParser.parse(bytes)

    /**
     * Returns the shared parser with the default options.
     *
     * The parser is immutable and thread-safe, so the same instance is returned on every call.
     */
    @JvmStatic
    public fun parser(): ICalParser = defaultParser

    /**
     * Creates a parser that applies [options].
     *
     * The parser is immutable and thread-safe; keep it and reuse it for many inputs.
     */
    @JvmStatic
    public fun parser(options: ParseOptions): ICalParser = ICalParser(options)

    /**
     * Fingerprints the components of the feed in [bytes] with the default [FingerprintOptions].
     *
     * One pass over the bytes finds the children of every `VCALENDAR` and hashes them without decoding values, so two
     * fingerprints of a feed show which components were added, removed or changed (see [Fingerprints.diff]). `DTSTAMP`
     * is ignored by default.
     *
     * @param bytes the complete iCalendar content.
     */
    @JvmStatic
    public fun fingerprints(bytes: ByteArray): Fingerprints = fingerprints(bytes, FingerprintOptions.Default)

    /**
     * Fingerprints the components of the feed in [bytes] with the given [options].
     *
     * @param bytes the complete iCalendar content.
     * @param options which properties take part in the hash.
     */
    @JvmStatic
    public fun fingerprints(bytes: ByteArray, options: FingerprintOptions): Fingerprints =
        FingerprintScanner(options).scan(bytes)

    /**
     * Returns the shared writer with the default options: byte-stable output in the order of the tree.
     *
     * The writer is immutable and thread-safe.
     */
    @JvmStatic
    public fun writer(): ICalWriter = defaultWriter

    /**
     * Creates a writer that applies [options].
     *
     * The writer is immutable and thread-safe.
     */
    @JvmStatic
    public fun writer(options: WriteOptions): ICalWriter = ICalWriter(options)
}

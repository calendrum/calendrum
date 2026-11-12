@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.syntax

import io.github.calendrum.ByteOutput
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.schemaCalendar
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

// Stateless: every call creates its own `ContentLineWriter` and `TreeWriter`. Byte output starts with a buffer of the
// summed source size of the parsed roots (1 KiB per hand-built one, plus slack for canonical lines), so a byte-stable
// tree is written without growing or copying the buffer.
/**
 * Writes component trees as iCalendar text.
 *
 * Get a writer from `ICal.writer()` or `ICal.writer(options)`. A writer is immutable and thread-safe. With the default
 * options it is byte-stable: a tree read from input is written as it was read, except that line ends become CRLF, and
 * an edited tree gives a minimal difference, because only lines that changed are rewritten. Nodes built by hand are
 * written canonically: lines folded at 75 octets, parameter values quoted when they contain a colon, semicolon or
 * comma, and `^`, `"` and line breaks in parameter values escaped as in [RFC
 * 6868](https://www.rfc-editor.org/rfc/rfc6868).
 *
 * Streams given to a writer are never closed. A component is written completely or the call throws; after a failure the
 * output can hold a partial component.
 */
public class ICalWriter internal constructor(private val options: WriteOptions) {
    /**
     * Writes [component] to [out]. Nothing is flushed or closed.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written, such as a control character in
     *   a value.
     * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun write(component: ICalComponent, @ObjCName("appendable") out: Appendable) {
        writer(AppendableLineWriter(out)).write(component)
    }

    /**
     * Writes all [components] to [out] one after another.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written.
     * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun write(components: List<ICalComponent>, @ObjCName("appendable") out: Appendable) {
        val writer = writer(AppendableLineWriter(out))
        for (component in components) writer.write(component)
    }

    /**
     * Writes [component] to [out] as UTF-8 bytes in blocks; [out] is not closed.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written. What was written before the
     *   error has been passed on to [out].
     * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun write(component: ICalComponent, out: ByteOutput) {
        val lines = ByteLineWriter(out)
        try {
            writer(lines).write(component)
        } catch (e: IllegalArgumentException) {
            lines.flush()
            throw e
        }
        lines.flush()
    }

    /**
     * Writes all [components] to [out] as UTF-8 bytes; [out] is not closed.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written.
     * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun write(components: List<ICalComponent>, out: ByteOutput) {
        val lines = ByteLineWriter(out)
        val writer = writer(lines)
        try {
            for (component in components) writer.write(component)
        } catch (e: IllegalArgumentException) {
            lines.flush()
            throw e
        }
        lines.flush()
    }

    /**
     * Encodes [items] as components of [schema] inside a new `VCALENDAR` set up by [calendar], and returns the bytes.
     *
     * @param items the objects to write, one component each.
     * @param schema describes the component and its fields.
     * @param calendar sets the properties of the surrounding calendar, such as `prodId`.
     * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
     *   defaults are given.
     * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
     */
    @Throws(IllegalArgumentException::class)
    public fun <T : Any> encode(
        items: List<T>,
        schema: IcsSchema<T>,
        @ObjCName("buildCalendar") calendar: VCalendar.Builder.() -> Unit,
    ): ByteArray = encodeToByteArray(schemaCalendar(items, schema, calendar, null))

    /**
     * Encodes [items] as components of [schema] inside a copy of [calendar] and returns the bytes.
     *
     * @param items the objects to write, one component each.
     * @param schema describes the component and its fields.
     * @param calendar the surrounding calendar; its properties are kept and the components are added.
     * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
     *   defaults are given.
     * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
     */
    @Throws(IllegalArgumentException::class)
    public fun <T : Any> encode(items: List<T>, schema: IcsSchema<T>, calendar: VCalendar): ByteArray =
        encodeToByteArray(schemaCalendar(items, schema, calendar, null))

    /**
     * Encodes [items] as components of [schema] inside a copy of [calendar] and returns the bytes, generating `UID` and
     * `DTSTAMP` of components that lack them with [defaults].
     *
     * @param items the objects to write, one component each.
     * @param schema describes the component and its fields.
     * @param calendar the surrounding calendar; its properties are kept and the components are added.
     * @param defaults the source of generated `UID` and `DTSTAMP` values.
     * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
     *   defaults are given.
     * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
     */
    @Throws(IllegalArgumentException::class)
    public fun <T : Any> encode(
        items: List<T>,
        schema: IcsSchema<T>,
        calendar: VCalendar,
        defaults: BuildDefaults,
    ): ByteArray = encodeToByteArray(schemaCalendar(items, schema, calendar, defaults))

    /**
     * Encodes [items] as components of [schema] inside a new `VCALENDAR` set up by [calendar], generating `UID` and
     * `DTSTAMP` of components that lack them with [defaults].
     *
     * @param items the objects to write, one component each.
     * @param schema describes the component and its fields.
     * @param calendar sets the properties of the surrounding calendar, such as `prodId`.
     * @param defaults the source of generated `UID` and `DTSTAMP` values.
     * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
     *   defaults are given.
     * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
     */
    @Throws(IllegalArgumentException::class)
    public fun <T : Any> encode(
        items: List<T>,
        schema: IcsSchema<T>,
        @ObjCName("buildCalendar") calendar: VCalendar.Builder.() -> Unit,
        defaults: BuildDefaults,
    ): ByteArray = encodeToByteArray(schemaCalendar(items, schema, calendar, defaults))

    /**
     * Writes [component] and returns the text.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written.
     * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun encodeToString(component: ICalComponent): String {
        val out = StringBuilder(capacity(component))
        write(component, out)
        return out.toString()
    }

    /**
     * Writes all [components] one after another and returns the text.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written.
     * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun encodeToString(components: List<ICalComponent>): String {
        val out = StringBuilder(capacity(components))
        write(components, out)
        return out.toString()
    }

    /**
     * Writes [component] and returns the UTF-8 bytes.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written.
     * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun encodeToByteArray(component: ICalComponent): ByteArray {
        val out = ByteLineWriter(capacity(component))
        writer(out).write(component)
        return out.takeBytes()
    }

    /**
     * Writes all [components] one after another and returns the UTF-8 bytes.
     *
     * @throws IllegalArgumentException if a name, value or parameter cannot be written.
     * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
     */
    @Throws(IllegalArgumentException::class)
    public fun encodeToByteArray(components: List<ICalComponent>): ByteArray {
        val out = ByteLineWriter(capacity(components))
        val writer = writer(out)
        for (component in components) writer.write(component)
        return out.takeBytes()
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "ICalWriter($options)"

    @OptIn(ExperimentalICalApi::class)
    private fun writer(out: ContentLineWriter): TreeWriter = TreeWriter(
        out,
        options.byteStable,
        options.timeZonesFirst,
        chain(options.interceptors),
        options.allowPartialWrite,
        options.missingTimeZones,
    )

    private fun chain(factories: List<() -> LineInterceptor>): InterceptorChain? =
        if (factories.isEmpty()) null else InterceptorChain(factories)

    private fun capacity(components: List<ICalComponent>): Int {
        var parsed = 0L
        var built = 0
        for (component in components) {
            val length = component.sourceLength
            if (length > 0L) parsed += length else built++
        }
        return capacity(parsed, built)
    }

    private fun capacity(component: ICalComponent): Int {
        val length = component.sourceLength
        return if (length > 0L) capacity(length, 0) else capacity(0L, 1)
    }

    private fun capacity(parsed: Long, built: Int): Int {
        var total = parsed + built.toLong() * BUILT_ESTIMATE
        if (!options.byteStable) total += CANONICAL_SLACK
        return minOf(maxOf(total, MIN_CAPACITY.toLong()), MAX_CAPACITY.toLong()).toInt()
    }

    private companion object {
        private const val BUILT_ESTIMATE = 1024
        private const val CANONICAL_SLACK = 256
        private const val MIN_CAPACITY = 256
        private const val MAX_CAPACITY = Int.MAX_VALUE - 8
    }
}

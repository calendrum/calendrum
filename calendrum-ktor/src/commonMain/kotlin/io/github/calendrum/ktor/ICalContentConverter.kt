@file:JvmName("ICalKtor")

package io.github.calendrum.ktor

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalWriter
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.ParseResult
import io.ktor.http.ContentType
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutgoingContent
import io.ktor.http.withCharset
import io.ktor.serialization.Configuration
import io.ktor.serialization.ContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.charsets.name
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import kotlin.jvm.JvmName

/** The `text/calendar` content type. */
public val TextCalendar: ContentType = ContentType("text", "calendar")

/**
 * A Ktor content converter for `text/calendar`, for the server and the client `ContentNegotiation`.
 *
 * It writes a [VCalendar], an `ICalComponent`, a `ParseResult` or a list of them, and reads a [VCalendar], a list of
 * calendars, an `ICalComponent`, a list of components or a `ParseResult`, depending on the requested type. When
 * reading, the charset of the content type is used as the charset hint of the parser if the options set none. Register
 * it with `iCalendar()`.
 *
 * @property parseOptions the options of the parser that reads bodies.
 * @property writer the writer that produces bodies.
 * @sample io.github.calendrum.samples.serveAnIcsFeed
 */
public class ICalContentConverter(public val parseOptions: ParseOptions, public val writer: ICalWriter) :
    ContentConverter {
    public constructor() : this(ParseOptions.Default, ICal.writer())

    /** Writes [value] as `text/calendar` with the charset UTF-8, or returns `null` when it is not a supported type. */
    override suspend fun serialize(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?,
    ): OutgoingContent? {
        val components = componentsOf(value) ?: return null
        return ByteArrayContent(writer.encodeToByteArray(components), contentType.withCharset(Charsets.UTF_8))
    }

    // The server passes the charset of the request, or UTF-8 when the Content-Type has none, so only another charset
    // becomes a hint of the parser; the Ktor 3.0 client always passes UTF-8, and the charset detection of the core
    // decides there. The parse options are copied with the hint, since ParseOptions has no toBuilder.

    // One byte more than `maxInputOctets` is enough for the parser to see that the body is too large: it then reads the
    // lines within the limit and reports ICAL-S030, and the rest of the body is never held in memory.

    /**
     * Reads the body as the type [typeInfo] asks for, or returns `null` for a type this converter does not produce.
     *
     * At most `ParseLimits.maxInputOctets` of [parseOptions] plus one bytes of the body are read; a larger body gives a
     * truncated result with `ICAL-S030`, not an exception.
     */
    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        val limit = parseOptions.limits.maxInputOctets
        val bytes = content.readRemaining(if (limit == Long.MAX_VALUE) limit else limit + 1).readByteArray()
        val result = ICal.parser(withHint(parseOptions, charset.name)).parse(bytes)
        val type = typeInfo.type
        val element = typeInfo.kotlinType?.arguments?.singleOrNull()?.type?.classifier
        return when {
            type == ParseResult::class -> result
            type == ICalComponent::class -> result.components.firstOrNull()
            type == VCalendar::class -> result.calendars.firstOrNull()
            type == List::class && element == VCalendar::class -> result.calendars
            type == List::class -> result.components
            else -> null
        }
    }

    /** Returns the name of the class. */
    override fun toString(): String = "ICalContentConverter"
}

/**
 * Registers an [ICalContentConverter] with the default options for `text/calendar`.
 * @sample io.github.calendrum.samples.serveAnIcsFeed
 */
public fun Configuration.iCalendar() {
    iCalendar(ICalContentConverter())
}

/** Registers [converter] for the content type [contentType]; `text/calendar` by default. */
public fun Configuration.iCalendar(converter: ICalContentConverter, contentType: ContentType = TextCalendar) {
    register(contentType, converter)
}

private fun componentsOf(value: Any?): List<ICalComponent>? = when (value) {
    is ICalComponent -> listOf(value)

    is VCalendar -> listOf(value.component)

    is ParseResult -> value.components

    is List<*> -> value.map {
        when (it) {
            is ICalComponent -> it
            is VCalendar -> it.component
            else -> return null
        }
    }

    else -> null
}

@OptIn(ExperimentalICalApi::class)
internal fun withHint(options: ParseOptions, charset: String): ParseOptions {
    if (options.charset != null || charset.equals("UTF-8", ignoreCase = true)) return options
    val builder = ParseOptions.builder()
        .mode(options.mode)
        .limits(options.limits)
        .registry(options.registry)
        .retainSource(options.retainSource)
        .decodeEagerly(options.decodeEagerly)
        .timeSource(options.timeSource)
        .select(options.selection)
        .charset(charset)
    options.problemListener?.let { builder.problemListener(it) }
    for (interceptor in options.interceptors) builder.intercept(interceptor)
    return builder.build()
}

@file:JvmName("IcsFeeds")

package io.github.calendrum.ktor

import io.github.calendrum.ICal
import io.github.calendrum.model.Canonical
import io.github.calendrum.model.VCalendar
import io.github.calendrum.syntax.ICalWriter
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.utils.io.charsets.Charsets
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * Settings of an ICS feed route: the `Cache-Control` header and the writer. Immutable.
 *
 * @property cacheControl the value of the `Cache-Control` header; `no-cache` by default, so clients revalidate with the
 *   `ETag`.
 * @property writer the writer that produces the body.
 */
public class IcsFeedOptions private constructor(public val cacheControl: String, public val writer: ICalWriter) {
    /** Returns the cache control. */
    override fun toString(): String = "IcsFeedOptions(cacheControl=$cacheControl)"

    /** Collects [IcsFeedOptions] values. Not thread-safe. */
    public class Builder internal constructor() {
        /** The value of the `Cache-Control` header; `no-cache` by default. */
        public var cacheControl: String = DEFAULT_CACHE_CONTROL

        /** The writer that produces the body; the default writer by default. */
        public var writer: ICalWriter = ICal.writer()

        /** Sets [cacheControl] to [value] and returns this builder. */
        public fun cacheControl(value: String): Builder = apply { cacheControl = value }

        /** Sets [writer] to [value] and returns this builder. */
        public fun writer(value: ICalWriter): Builder = apply { writer = value }

        /** Creates the options. */
        public fun build(): IcsFeedOptions = IcsFeedOptions(cacheControl, writer)

        /** Returns the name of the builder. */
        override fun toString(): String = "IcsFeedOptions.Builder"
    }

    /** The default options and a builder. */
    public companion object {
        /** `Cache-Control: no-cache` and the default writer. */
        @JvmField
        public val Default: IcsFeedOptions = IcsFeedOptions(DEFAULT_CACHE_CONTROL, ICal.writer())

        /** Returns a builder that starts from the default options. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

/**
 * Serves a calendar as an ICS feed at [path] with default options: each `GET` runs [feed], sends the calendar as
 * `text/calendar` with an `ETag`, and answers `304 Not Modified` when `If-None-Match` matches.
 *
 * The `ETag` is a weak tag over the canonical form of the calendar, so a feed whose content did not change keeps its
 * tag.
 *
 * @param path the route path.
 * @param feed produces the calendar for one request.
 * @sample io.github.calendrum.samples.serveAnIcsFeed
 */
public fun Route.icsFeed(path: String, feed: suspend ApplicationCall.() -> VCalendar): Route =
    icsFeed(path, IcsFeedOptions.Default, feed)

/**
 * Serves a calendar as an ICS feed at [path] with the given [options].
 *
 * @param path the route path.
 * @param options the cache header and the writer.
 * @param feed produces the calendar for one request.
 */
public fun Route.icsFeed(path: String, options: IcsFeedOptions, feed: suspend ApplicationCall.() -> VCalendar): Route =
    get(path) {
        val calendar = call.feed()
        val tag = entityTag(calendar)
        call.response.header(HttpHeaders.ETag, tag)
        call.response.header(HttpHeaders.CacheControl, options.cacheControl)
        if (matches(call.request.headers.getAll(HttpHeaders.IfNoneMatch), tag)) {
            call.respond(HttpStatusCode.NotModified)
        } else {
            call.respondBytes(
                options.writer.encodeToByteArray(calendar.component),
                TextCalendar.withCharset(Charsets.UTF_8),
            )
        }
    }

// A weak tag: FNV-1a 64 of the canonical form, so the order of properties and components does not change it; the
// component fingerprints would miss the properties of VCALENDAR.
internal fun entityTag(calendar: VCalendar): String {
    var hash = FNV_OFFSET
    for (byte in Canonical.canonicalize(calendar)) {
        hash = (hash xor (byte.toLong() and 0xFF)) * FNV_PRIME
    }
    val hex = StringBuilder(22).append("W/\"")
    for (shift in 60 downTo 0 step 4) hex.append(HEX[((hash ushr shift) and 0xF).toInt()])
    return hex.append('"').toString()
}

// If-None-Match is a list of tags or `*`; weak comparison, as RFC 9110 §13.1.2 asks for GET.
internal fun matches(headers: List<String>?, tag: String): Boolean {
    if (headers == null) return false
    val opaque = tag.removePrefix("W/")
    for (header in headers) {
        for (part in header.split(',')) {
            val candidate = part.trim()
            if (candidate == "*" || candidate.removePrefix("W/") == opaque) return true
        }
    }
    return false
}

private const val DEFAULT_CACHE_CONTROL = "no-cache"
private const val FNV_OFFSET = -0x340d631b7bdddcdbL
private const val FNV_PRIME = 0x100000001b3L
private const val HEX = "0123456789abcdef"

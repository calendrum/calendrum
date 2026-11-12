package io.github.calendrum.syntax

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICalDsl
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

// Immutable options with a builder, as `ParseOptions`: `byteStable` (default) writes parsed lines as they were read,
// `timeZonesFirst` puts VTIMEZONE first inside each VCALENDAR.
/**
 * Settings of a writer: byte stability, the order of time zones, interceptors, partial output and missing time zones.
 *
 * Build options with [builder]; [Default] writes byte-stable. The class is immutable and can be shared between threads.
 *
 * @property byteStable whether lines read from input are written exactly as they were.
 * @property timeZonesFirst whether `VTIMEZONE` components are written before the other children of a `VCALENDAR`.
 * @property interceptors the factories of the line interceptors, applied in order.
 * @property allowPartialWrite whether a tree produced by a selective parse may be written.
 * @property missingTimeZones where a `VTIMEZONE` for a `TZID` that the calendar does not define comes from, or `null`.
 */
public class WriteOptions internal constructor(
    public val byteStable: Boolean,
    public val timeZonesFirst: Boolean,
    @property:ExperimentalICalApi
    public val interceptors: List<() -> LineInterceptor>,
    public val allowPartialWrite: Boolean,
    public val missingTimeZones: VTimeZoneSource?,
) {
    /** Returns a short description for diagnostics; its format is not a contract. */
    @OptIn(ExperimentalICalApi::class)
    override fun toString(): String = describe(
        "WriteOptions",
        "byteStable",
        byteStable,
        "timeZonesFirst",
        timeZonesFirst,
        "interceptors",
        interceptors.size,
        "allowPartialWrite",
        allowPartialWrite,
        "missingTimeZones",
        if (missingTimeZones == null) "none" else "set",
    )

    /**
     * Collects [WriteOptions] values. The properties and the functions of the same name do the same; the functions
     * chain. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor() {
        /**
         * Write lines that came from input as they were (spelling, quoting, folding), so an unchanged tree comes back
         * byte for byte; true by default. When false every line is written canonically: folded at 75 octets, with
         * parameters quoted as needed. Line ends are always CRLF. Nodes built by hand or edited are always written
         * canonically.
         */
        public var byteStable: Boolean = true

        /** Write the `VTIMEZONE` children of a `VCALENDAR` before its other children; false by default. */
        public var timeZonesFirst: Boolean = false

        /**
         * Allow writing a tree from a selective parse, which lacks what was not selected; false by default, in which
         * case writing such a tree throws.
         */
        public var allowPartialWrite: Boolean = false

        /**
         * The source of a `VTIMEZONE` for every `TZID` used in a calendar but not defined in it; `null` (the default)
         * adds none.
         */
        public var missingTimeZones: VTimeZoneSource? = null
        private val factories = ArrayList<() -> LineInterceptor>()

        /** Sets [byteStable]. */
        public fun byteStable(byteStable: Boolean): Builder {
            this.byteStable = byteStable
            return this
        }

        /** Sets [timeZonesFirst]. */
        public fun timeZonesFirst(timeZonesFirst: Boolean): Builder {
            this.timeZonesFirst = timeZonesFirst
            return this
        }

        /** Sets [allowPartialWrite]. */
        public fun allowPartialWrite(allow: Boolean): Builder {
            allowPartialWrite = allow
            return this
        }

        /**
         * Sets [missingTimeZones]: when a calendar is written, a `VTIMEZONE` that [source] returns is added for each
         * `TZID` that the calendar uses but does not define, placed before the other children. `null` turns it off.
         */
        public fun addMissingTimeZones(source: VTimeZoneSource?): Builder {
            missingTimeZones = source
            return this
        }

        /**
         * Adds an interceptor created by [factory] for every write, so that a stateful interceptor gets a fresh
         * instance each time.
         */
        @ExperimentalICalApi
        public fun intercept(factory: () -> LineInterceptor): Builder {
            factories += factory
            return this
        }

        // Registers a factory that returns the same instance, for stateless interceptors and the `intercept { line -> …
        // }` DSL; a lambda without a parameter resolves to `intercept(factory)`.

        /** Adds a stateless [interceptor]; the same instance serves every write and must be thread-safe. */
        @ExperimentalICalApi
        public fun intercept(interceptor: LineInterceptor): Builder {
            factories += { interceptor }
            return this
        }

        /** Creates the options. */
        public fun build(): WriteOptions {
            val interceptors = if (factories.isEmpty()) emptyList() else ArrayView(factories.toTypedArray())
            return WriteOptions(byteStable, timeZonesFirst, interceptors, allowPartialWrite, missingTimeZones)
        }
    }

    /** The shared defaults and a builder. */
    public companion object {
        /** Byte-stable writing without interceptors, partial output or added time zones. */
        @JvmField
        public val Default: WriteOptions = WriteOptions(true, false, emptyList(), false, null)

        /** Returns a builder that starts from the defaults. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

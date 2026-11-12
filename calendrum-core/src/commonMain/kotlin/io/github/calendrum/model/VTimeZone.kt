package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlinx.datetime.UtcOffset
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

/**
 * A `VTIMEZONE` component: the rules that turn the local times of one time zone into UTC
 * ([RFC 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 *
 * The rules are a list of observances, each a `STANDARD` or `DAYLIGHT` component with an offset, a start and a
 * recurrence. Turning them into an offset for a moment is the job of a time zone resolver, which compiles them lazily.
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class VTimeZone internal constructor(public val component: ICalComponent) {
    @Volatile
    private var observanceList: List<Observance>? = null

    /**
     * The time zone identifier of the time zone (`TZID`,
     * [RFC 5545 §3.8.3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.1)), or `null` when it is missing or
     * does not decode.
     */
    public val tzid: String? get() = component[Properties.TZID]

    /**
     * The time of the last modification of the time zone (`LAST-MODIFIED`,
     * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
     * does not decode.
     */
    public val lastModified: ICalDateTime? get() = component[Properties.LAST_MODIFIED]

    /**
     * The URL of the time zone definition of the time zone (`TZURL`,
     * [RFC 5545 §3.8.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.5)), or `null` when it is missing or
     * does not decode.
     */
    public val tzUrl: String? get() = component[Properties.TZURL]

    /** The `STANDARD` and `DAYLIGHT` observances in source order. */
    public val observances: List<Observance>
        get() = observanceList ?: observances(component).also { observanceList = it }

    /**
     * The links of the time zone (`LINK` properties,
     * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
     * skipped.
     */
    public val links: List<Link> get() = component.all(Properties.LINK)

    /**
     * The concepts of the time zone (`CONCEPT` properties,
     * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

    /**
     * The reference identifiers of the time zone (`REFID` properties,
     * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
     * skipped.
     */
    public val refIds: List<String> get() = texts(component, Properties.REFID)

    /**
     * The relationships to other components of the time zone (`RELATED-TO` properties,
     * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

    /**
     * Returns a copy of this time zone changed by [block]. Properties and components that [block] does not touch are
     * the same objects as in this time zone.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): VTimeZone = toBuilder().apply(block).build()

    /** Returns a builder that starts from this time zone; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this time zone and takes generated `UID` and `DTSTAMP` values from [defaults]
     * where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a VTimeZone with an equal VTIMEZONE component. */
    override fun equals(other: Any?): Boolean = this === other || (other is VTimeZone && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("VTimeZone", component)

    /**
     * One set of rules of a time zone: a `STANDARD` or a `DAYLIGHT` component
     * ([RFC 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
     *
     * It says that from [start] the offset from UTC is [offsetTo] instead of [offsetFrom], and when that repeats (a
     * rule or dates). A view over a tree node like [VTimeZone]; the subclasses [Standard] and [Daylight] only tell
     * which kind it is.
     *
     * @property component the underlying tree node.
     */
    public sealed class Observance(public val component: ICalComponent) {
        /**
         * The local start (`DTSTART`) of the observance, in the offset before it; `null` when it is missing or does not
         * decode.
         */
        public val start: ICalDateTime? get() = component[Properties.DTSTART]

        /**
         * The UTC offset in force before the observance (`TZOFFSETFROM`,
         * [RFC 5545 §3.8.3.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.3)), or `null` when it is missing or
         * does not decode.
         */
        public val offsetFrom: UtcOffset? get() = component[Properties.TZOFFSETFROM]

        /**
         * The UTC offset the observance puts in force (`TZOFFSETTO`,
         * [RFC 5545 §3.8.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.4)), or `null` when it is missing or
         * does not decode.
         */
        public val offsetTo: UtcOffset? get() = component[Properties.TZOFFSETTO]

        /** The recurrence rule of the observance (`RRULE`), or `null` when it has none or it does not decode. */
        public val rrule: Recur? get() = component[Properties.RRULE]

        /** The comments (`COMMENT`) in source order. */
        public val comments: List<String> get() = texts(component, Properties.COMMENT)

        /** The additional start dates (`RDATE`) in source order; a period contributes its start. */
        public val rdates: List<ICalDateTime> get() = rdates(component)

        /**
         * The names of the observance (`TZNAME`,
         * [RFC 5545 §3.8.3.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.2)), such as `CEST`, in source
         * order.
         */
        public val names: List<String> get() = texts(component, Properties.TZNAME)

        /**
         * The links (`LINK`, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) of the observance in
         * source order.
         */
        public val links: List<Link> get() = component.all(Properties.LINK)

        /**
         * The concepts (`CONCEPT`, [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) of the
         * observance in source order.
         */
        public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

        /**
         * The reference identifiers (`REFID`, [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) of
         * the observance in source order.
         */
        public val refIds: List<String> get() = texts(component, Properties.REFID)

        /**
         * The relationships (`RELATED-TO`, [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5))
         * of the observance in source order.
         */
        public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

        /**
         * Returns a copy of this observance changed by [block]; untouched nodes are shared.
         *
         * @param block changes the builder.
         */
        public fun edit(block: Builder.() -> Unit): Observance = toBuilder().apply(block).build()

        /** Returns a builder that starts from this observance. */
        public fun toBuilder(): Builder = Builder(component.name, component, null)

        /**
         * Collects the properties of a `STANDARD` or `DAYLIGHT` observance. Get one from [Standard.builder],
         * [Daylight.builder] or [Observance.toBuilder]. Not thread-safe.
         */
        @ICalDsl
        public class Builder internal constructor(
            private val kind: String,
            original: ICalComponent?,
            defaults: BuildDefaults?,
        ) : ComponentBuilder<Builder>(kind, original, defaults) {
            /** The local start (`DTSTART`) of the observance being built; assigning `null` removes the property. */
            public var start: ICalDateTime?
                get() = read(Properties.DTSTART)
                set(value) {
                    write(Properties.DTSTART, value)
                }

            /** Sets [start] to [value] and returns this builder; `null` removes the property. */
            public fun start(value: ICalDateTime?): Builder = apply { start = value }

            /**
             * The offset before the observance being built (`TZOFFSETFROM`); assigning `null` removes the property.
             */
            public var offsetFrom: UtcOffset?
                get() = read(Properties.TZOFFSETFROM)
                set(value) {
                    write(Properties.TZOFFSETFROM, value)
                }

            /** Sets [offsetFrom] to [value] and returns this builder; `null` removes the property. */
            public fun offsetFrom(value: UtcOffset?): Builder = apply { offsetFrom = value }

            /**
             * The offset the observance being built puts in force (`TZOFFSETTO`); assigning `null` removes the
             * property.
             */
            public var offsetTo: UtcOffset?
                get() = read(Properties.TZOFFSETTO)
                set(value) {
                    write(Properties.TZOFFSETTO, value)
                }

            /** Sets [offsetTo] to [value] and returns this builder; `null` removes the property. */
            public fun offsetTo(value: UtcOffset?): Builder = apply { offsetTo = value }

            /** The recurrence rule of the observance being built (`RRULE`); assigning `null` removes the property. */
            public var rrule: Recur?
                get() = read(Properties.RRULE)
                set(value) {
                    write(Properties.RRULE, value)
                }

            /** Sets [rrule] to [value] and returns this builder; `null` removes the property. */
            public fun rrule(value: Recur?): Builder = apply { rrule = value }

            /** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
            public fun rrule(frequency: Frequency, block: Recur.Builder.() -> Unit): Builder =
                apply { rrule = Recur.builder(frequency).apply(block).build() }

            /** Adds a `COMMENT` property with the text [text]. */
            public fun comment(text: String): Builder = property(Properties.COMMENT, text)

            /** Adds an `RDATE` property with the given [dates]. */
            public fun rdate(vararg dates: ICalDateTime): Builder =
                property(Properties.RDATE, RDate(dates.asList(), emptyList()))

            /** Adds an `RDATE` property with the given [periods]. */
            public fun rdate(vararg periods: ICalPeriod): Builder =
                property(Properties.RDATE, RDate(emptyList(), periods.asList()))

            /** Adds a `TZNAME` property with the text [text]. */
            public fun name(text: String): Builder = property(Properties.TZNAME, text)

            /** Adds [value] as a `LINK` property. */
            public fun link(value: Link): Builder = property(Properties.LINK, value)

            /** Adds a `CONCEPT` property with the URI [uri]. */
            public fun concept(uri: String): Builder = property(Properties.CONCEPT, uri)

            /** Adds a `REFID` property with the text [text]. */
            public fun refId(text: String): Builder = property(Properties.REFID, text)

            /** Adds a `RELATED-TO` property naming the component with the given [uid]. */
            public fun relatedTo(uid: String): Builder = property(Properties.RELATED_TO, RelatedTo(uid))

            /** Adds [relatedTo] as a `RELATED-TO` property. */
            public fun relatedTo(relatedTo: RelatedTo): Builder = property(Properties.RELATED_TO, relatedTo)

            /** Creates the observance: a [Standard] or a [Daylight], as the builder was made for. */
            public fun build(): Observance {
                val node = buildNode()
                return if (kind == STANDARD) Standard(node) else Daylight(node)
            }
        }

        /** Returns `true` when [other] is an observance of the same kind with an equal component. */
        override fun equals(other: Any?): Boolean = this === other ||
            (other is Observance && (this is Standard) == (other is Standard) && component == other.component)

        /** Returns the hash code of the component. */
        override fun hashCode(): Int = component.hashCode()
    }

    /** An observance of standard time (`STANDARD`). */
    public class Standard internal constructor(component: ICalComponent) : Observance(component) {
        /** Returns the type and the number of properties and components; never a value. */
        override fun toString(): String = describeWrapper("VTimeZone.Standard", component)

        /** Creates `STANDARD` observances. */
        public companion object {
            /**
             * Wraps [component] as a [Standard], or returns `null` when it is not a `STANDARD` component.
             *
             * @param component the component to wrap.
             */
            @JvmStatic
            public fun of(component: ICalComponent): Standard? =
                if (named(component, STANDARD)) Standard(component) else null

            /** Returns an empty builder for a `STANDARD` observance. */
            @JvmStatic
            public fun builder(): Observance.Builder = Observance.Builder(STANDARD, null, null)
        }
    }

    /** An observance of daylight saving time (`DAYLIGHT`). */
    public class Daylight internal constructor(component: ICalComponent) : Observance(component) {
        /** Returns the type and the number of properties and components; never a value. */
        override fun toString(): String = describeWrapper("VTimeZone.Daylight", component)

        /** Creates `DAYLIGHT` observances. */
        public companion object {
            /**
             * Wraps [component] as a [Daylight], or returns `null` when it is not a `DAYLIGHT` component.
             *
             * @param component the component to wrap.
             */
            @JvmStatic
            public fun of(component: ICalComponent): Daylight? =
                if (named(component, DAYLIGHT)) Daylight(component) else null

            /** Returns an empty builder for a `DAYLIGHT` observance. */
            @JvmStatic
            public fun builder(): Observance.Builder = Observance.Builder(DAYLIGHT, null, null)
        }
    }

    /**
     * Collects the properties and nested components of a new or edited time zone, with a Kotlin DSL and chained calls.
     * Get one from [VTimeZone.builder] or [VTimeZone.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("VTIMEZONE", original, defaults) {
        /**
         * The time zone identifier of the time zone being built (`TZID`,
         * [RFC 5545 §3.8.3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.1)); assigning `null` removes the
         * property.
         */
        public var tzid: String?
            get() = read(Properties.TZID)
            set(value) {
                write(Properties.TZID, value)
            }

        /**
         * Sets [tzid] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun tzid(value: String?): Builder = apply { tzid = value }

        /**
         * The time of the last modification of the time zone being built (`LAST-MODIFIED`,
         * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
         * property.
         */
        public var lastModified: ICalDateTime?
            get() = read(Properties.LAST_MODIFIED)
            set(value) {
                write(Properties.LAST_MODIFIED, value)
            }

        /**
         * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun lastModified(value: ICalDateTime?): Builder = apply { lastModified = value }

        /**
         * The URL of the time zone definition of the time zone being built (`TZURL`,
         * [RFC 5545 §3.8.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.5)); assigning `null` removes the
         * property.
         */
        public var tzUrl: String?
            get() = read(Properties.TZURL)
            set(value) {
                write(Properties.TZURL, value)
            }

        /**
         * Sets [tzUrl] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun tzUrl(value: String?): Builder = apply { tzUrl = value }

        /** The observances of the time zone being built, as an editable list. */
        public val observances: ComponentList<Observance>
            get() = ComponentList(this, STANDARD, DAYLIGHT, ::observanceOf, Observance::component)

        /** Adds a `STANDARD` observance that [block] sets up. */
        public fun standard(block: Observance.Builder.() -> Unit): Builder =
            observance(Observance.Builder(STANDARD, null, defaults).apply(block).build())

        /** Adds a `DAYLIGHT` observance that [block] sets up. */
        public fun daylight(block: Observance.Builder.() -> Unit): Builder =
            observance(Observance.Builder(DAYLIGHT, null, defaults).apply(block).build())

        /** Adds [observance] to the time zone. */
        public fun observance(observance: Observance): Builder = apply { observances.add(observance) }

        /** Edits the observances in [block]: add, replace or remove them. */
        public fun observances(block: ComponentList<Observance>.() -> Unit): Builder = apply { observances.block() }

        /** Adds [value] as a `LINK` property. */
        public fun link(value: Link): Builder = property(Properties.LINK, value)

        /** Adds a `CONCEPT` property with the URI [uri]. */
        public fun concept(uri: String): Builder = property(Properties.CONCEPT, uri)

        /** Adds a `REFID` property with the text [text]. */
        public fun refId(text: String): Builder = property(Properties.REFID, text)

        /**
         * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
         * otherwise the given value.
         */
        public fun relatedTo(uid: String): Builder = property(Properties.RELATED_TO, RelatedTo(uid))

        /**
         * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
         * otherwise the given value.
         */
        public fun relatedTo(relatedTo: RelatedTo): Builder = property(Properties.RELATED_TO, relatedTo)

        /** Creates the time zone. */
        public fun build(): VTimeZone = VTimeZone(buildNode())
    }

    /** Creates VTimeZone values and builders. */
    public companion object {
        /**
         * Wraps [component] as a VTimeZone, or returns `null` when it is not a `VTIMEZONE` component. Nothing is copied
         * or validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): VTimeZone? =
            if (named(component, "VTIMEZONE")) VTimeZone(component) else null

        /** Returns an empty builder. */
        @JvmStatic
        public fun builder(): Builder = Builder(null, null)

        /**
         * Returns an empty builder that generates missing identity properties with [defaults].
         *
         * @param defaults the clock and the `UID` generator.
         */
        @JvmStatic
        public fun builder(defaults: BuildDefaults): Builder = Builder(null, defaults)
    }
}

private fun observanceOf(node: ICalComponent): VTimeZone.Observance =
    if (node.name == STANDARD) VTimeZone.Standard(node) else VTimeZone.Daylight(node)

private const val STANDARD = "STANDARD"
private const val DAYLIGHT = "DAYLIGHT"

private fun observances(component: ICalComponent): List<VTimeZone.Observance> {
    val standard = KnownNames.find("STANDARD")
    val daylight = KnownNames.find("DAYLIGHT")
    var count = 0
    for (node in component.componentNodes) {
        if (node.nameId == standard || node.nameId == daylight) count += 1
    }
    if (count == 0) return emptyList()
    val result = arrayOfNulls<VTimeZone.Observance>(count)
    var index = 0
    for (node in component.componentNodes) {
        if (node.nameId == standard) result[index++] = VTimeZone.Standard(node)
        if (node.nameId == daylight) result[index++] = VTimeZone.Daylight(node)
    }
    @Suppress("UNCHECKED_CAST")
    return ArrayView(result as Array<VTimeZone.Observance>)
}

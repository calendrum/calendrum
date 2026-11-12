@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName

/**
 * A `VLOCATION` component: a structured location of an event or to-do, with a name, a type and coordinates
 * ([RFC 9073 §7.2](https://www.rfc-editor.org/rfc/rfc9073#section-7.2)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class VLocation internal constructor(public val component: ICalComponent) {
    /**
     * The globally unique identifier of the location component (`UID`,
     * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
     * does not decode.
     */
    public val uid: String? get() = component[Properties.UID]

    /**
     * The name of the location component (`NAME`, [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)),
     * or `null` when it is missing or does not decode.
     */
    public val name: String? get() = component[Properties.NAME]

    /**
     * The description of the location component (`DESCRIPTION`,
     * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
     * does not decode.
     */
    @ObjCName("descriptionText")
    public val description: String? get() = component[Properties.DESCRIPTION]

    /**
     * The geographic position of the location component (`GEO`,
     * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
     * does not decode.
     */
    public val geo: Geo? get() = component[Properties.GEO]

    /**
     * The URL of the location component (`URL`,
     * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
     * does not decode.
     */
    public val url: String? get() = component[Properties.URL]

    /**
     * The types of the location component (`LOCATION-TYPE`,
     * [RFC 9073 §6.1](https://www.rfc-editor.org/rfc/rfc9073#section-6.1)), such as `hotel` or `airport`; empty when
     * the property is missing or does not decode.
     */
    public val locationTypes: List<String> get() = component[Properties.LOCATION_TYPE] ?: emptyList()

    /**
     * The structured data of the location component (`STRUCTURED-DATA` properties,
     * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
     * skipped.
     */
    public val structuredData: List<StructuredData> get() = component.all(Properties.STRUCTURED_DATA)

    /**
     * The links of the location component (`LINK` properties,
     * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
     * skipped.
     */
    public val links: List<Link> get() = component.all(Properties.LINK)

    /**
     * The concepts of the location component (`CONCEPT` properties,
     * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

    /**
     * The reference identifiers of the location component (`REFID` properties,
     * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
     * skipped.
     */
    public val refIds: List<String> get() = texts(component, Properties.REFID)

    /**
     * The relationships to other components of the location component (`RELATED-TO` properties,
     * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

    /**
     * Returns a copy of this location component changed by [block]. Properties and components that [block] does not
     * touch are the same objects as in this location component.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): VLocation = toBuilder().apply(block).build()

    /** Returns a builder that starts from this location component; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this location component and takes generated `UID` and `DTSTAMP` values from
     * [defaults] where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a VLocation with an equal VLOCATION component. */
    override fun equals(other: Any?): Boolean = this === other || (other is VLocation && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("VLocation", component)

    /**
     * Collects the properties and nested components of a new or edited location component, with a Kotlin DSL and
     * chained calls. Get one from [VLocation.builder] or [VLocation.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("VLOCATION", original, defaults) {
        /**
         * The globally unique identifier of the location component being built (`UID`,
         * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
         * property.
         */
        public var uid: String?
            get() = read(Properties.UID)
            set(value) {
                write(Properties.UID, value)
            }

        /**
         * Sets [uid] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun uid(value: String?): Builder = apply { uid = value }

        /**
         * The name of the location component being built (`NAME`,
         * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)); assigning `null` removes the property.
         */
        public var name: String?
            get() = read(Properties.NAME)
            set(value) {
                write(Properties.NAME, value)
            }

        /**
         * Sets [name] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun name(value: String?): Builder = apply { name = value }

        /**
         * The description of the location component being built (`DESCRIPTION`,
         * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)); assigning `null` removes the
         * property.
         */
        @ObjCName("descriptionText")
        public var description: String?
            get() = read(Properties.DESCRIPTION)
            set(value) {
                write(Properties.DESCRIPTION, value)
            }

        /**
         * Sets [description] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun description(value: String?): Builder = apply { description = value }

        /**
         * The geographic position of the location component being built (`GEO`,
         * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
         * property.
         */
        public var geo: Geo?
            get() = read(Properties.GEO)
            set(value) {
                write(Properties.GEO, value)
            }

        /**
         * Sets [geo] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun geo(value: Geo?): Builder = apply { geo = value }

        /**
         * The URL of the location component being built (`URL`,
         * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
         * property.
         */
        public var url: String?
            get() = read(Properties.URL)
            set(value) {
                write(Properties.URL, value)
            }

        /**
         * Sets [url] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun url(value: String?): Builder = apply { url = value }

        /**
         * The types of the location component being built (`LOCATION-TYPE`,
         * [RFC 9073 §6.1](https://www.rfc-editor.org/rfc/rfc9073#section-6.1)); assigning `null` removes the property.
         */
        public var locationTypes: List<String>?
            get() = read(Properties.LOCATION_TYPE)
            set(value) {
                write(Properties.LOCATION_TYPE, value)
            }

        /**
         * Sets [locationTypes] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun locationTypes(value: List<String>?): Builder = apply { locationTypes = value }

        /** Adds [value] as a `STRUCTURED-DATA` property. */
        public fun structuredData(value: StructuredData): Builder = property(Properties.STRUCTURED_DATA, value)

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

        /**
         * Creates the location component.
         *
         * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
         */
        public fun build(): VLocation {
            requireUid()
            return VLocation(buildNode())
        }
    }

    /** Creates VLocation values and builders. */
    public companion object {
        /**
         * Wraps [component] as a VLocation, or returns `null` when it is not a `VLOCATION` component. Nothing is copied
         * or validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): VLocation? =
            if (named(component, "VLOCATION")) VLocation(component) else null

        /**
         * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
         */
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

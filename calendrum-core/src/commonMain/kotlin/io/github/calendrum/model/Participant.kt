@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName

/**
 * A `PARTICIPANT` component: a person, group or resource taking part in an event or to-do, with its own properties
 * ([RFC 9073 §7.1](https://www.rfc-editor.org/rfc/rfc9073#section-7.1)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class Participant internal constructor(public val component: ICalComponent) {
    /**
     * The participant type of the participant component (`PARTICIPANT-TYPE`,
     * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)), or `null` when it is missing or does not
     * decode.
     */
    public val participantType: ParticipantType? get() = component[Properties.PARTICIPANT_TYPE]

    /**
     * The globally unique identifier of the participant component (`UID`,
     * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
     * does not decode.
     */
    public val uid: String? get() = component[Properties.UID]

    /**
     * The calendar address of the participant component (`CALENDAR-ADDRESS`,
     * [RFC 9073 §6.4](https://www.rfc-editor.org/rfc/rfc9073#section-6.4)), or `null` when it is missing or does not
     * decode.
     */
    public val calendarAddress: String? get() = component[Properties.CALENDAR_ADDRESS]

    /**
     * The creation time of the participant component (`CREATED`,
     * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)), or `null` when it is missing or
     * does not decode.
     */
    public val created: ICalDateTime? get() = component[Properties.CREATED]

    /**
     * The description of the participant component (`DESCRIPTION`,
     * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
     * does not decode.
     */
    @ObjCName("descriptionText")
    public val description: String? get() = component[Properties.DESCRIPTION]

    /**
     * The time stamp of the participant component (`DTSTAMP`,
     * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
     * does not decode.
     */
    public val stamp: ICalDateTime? get() = component[Properties.DTSTAMP]

    /**
     * The geographic position of the participant component (`GEO`,
     * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
     * does not decode.
     */
    public val geo: Geo? get() = component[Properties.GEO]

    /**
     * The time of the last modification of the participant component (`LAST-MODIFIED`,
     * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
     * does not decode.
     */
    public val lastModified: ICalDateTime? get() = component[Properties.LAST_MODIFIED]

    /**
     * The priority of the participant component (`PRIORITY`,
     * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
     * does not decode.
     */
    public val priority: Int? get() = component[Properties.PRIORITY]

    /**
     * The revision sequence number of the participant component (`SEQUENCE`,
     * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
     * does not decode.
     */
    public val sequence: Int? get() = component[Properties.SEQUENCE]

    /**
     * The status of the participant component (`STATUS`,
     * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
     * does not decode.
     */
    public val status: EventStatus? get() = component[Properties.EVENT_STATUS]

    /**
     * The summary, the short title of the participant component (`SUMMARY`,
     * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
     * does not decode.
     */
    public val summary: String? get() = component[Properties.SUMMARY]

    /**
     * The URL of the participant component (`URL`,
     * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
     * does not decode.
     */
    public val url: String? get() = component[Properties.URL]

    /**
     * The attachments of the participant component (`ATTACH` properties,
     * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attachments: List<Attach> get() = component.all(Properties.ATTACH)

    /**
     * The categories of the participant component (`CATEGORIES` properties,
     * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val categories: List<String> get() = categories(component)

    /**
     * The comments of the participant component (`COMMENT` properties,
     * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
     * decode are skipped.
     */
    public val comments: List<String> get() = texts(component, Properties.COMMENT)

    /**
     * The contacts of the participant component (`CONTACT` properties,
     * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)) in source order; ones that do not
     * decode are skipped.
     */
    public val contacts: List<String> get() = contacts(component)

    /**
     * The location texts of the participant component (`LOCATION` properties,
     * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)) in source order; ones that do not
     * decode are skipped.
     */
    public val locationTexts: List<String> get() = component.all(Properties.LOCATION).map { it.text }

    /**
     * The request statuses of the participant component (`REQUEST-STATUS` properties,
     * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
     * decode are skipped.
     */
    public val requestStatuses: List<RequestStatus> get() = component.all(Properties.REQUEST_STATUS)

    /**
     * The relationships to other components of the participant component (`RELATED-TO` properties,
     * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

    /**
     * The resources of the participant component (`RESOURCES` properties,
     * [RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)) from all properties in source
     * order; ones that do not decode are skipped.
     */
    public val resources: List<String> get() = resources(component)

    /**
     * The styled descriptions of the participant component (`STYLED-DESCRIPTION` properties,
     * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
     * skipped.
     */
    public val styledDescriptions: List<StyledDescription> get() = component.all(Properties.STYLED_DESCRIPTION)

    /**
     * The structured data of the participant component (`STRUCTURED-DATA` properties,
     * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
     * skipped.
     */
    public val structuredData: List<StructuredData> get() = component.all(Properties.STRUCTURED_DATA)

    /**
     * The links of the participant component (`LINK` properties,
     * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
     * skipped.
     */
    public val links: List<Link> get() = component.all(Properties.LINK)

    /**
     * The concepts of the participant component (`CONCEPT` properties,
     * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

    /**
     * The reference identifiers of the participant component (`REFID` properties,
     * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
     * skipped.
     */
    public val refIds: List<String> get() = texts(component, Properties.REFID)

    /** The locations nested in the participant component, in source order. */
    public val locations: List<VLocation> get() = children(component, "VLOCATION", ::VLocation)

    /** The resources nested in the participant component, in source order. */
    public val resourceComponents: List<VResource> get() = children(component, "VRESOURCE", ::VResource)

    /**
     * Returns a copy of this participant component changed by [block]. Properties and components that [block] does not
     * touch are the same objects as in this participant component.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): Participant = toBuilder().apply(block).build()

    /** Returns a builder that starts from this participant component; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this participant component and takes generated `UID` and `DTSTAMP` values from
     * [defaults] where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a Participant with an equal PARTICIPANT component. */
    override fun equals(other: Any?): Boolean = this === other || (other is Participant && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("Participant", component)

    /**
     * Collects the properties and nested components of a new or edited participant component, with a Kotlin DSL and
     * chained calls. Get one from [Participant.builder] or [Participant.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("PARTICIPANT", original, defaults) {
        /**
         * The participant type of the participant component being built (`PARTICIPANT-TYPE`,
         * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)); assigning `null` removes the property.
         */
        public var participantType: ParticipantType?
            get() = read(Properties.PARTICIPANT_TYPE)
            set(value) {
                write(Properties.PARTICIPANT_TYPE, value)
            }

        /**
         * Sets [participantType] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun participantType(value: ParticipantType?): Builder = apply { participantType = value }

        /**
         * The globally unique identifier of the participant component being built (`UID`,
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
         * The calendar address of the participant component being built (`CALENDAR-ADDRESS`,
         * [RFC 9073 §6.4](https://www.rfc-editor.org/rfc/rfc9073#section-6.4)); assigning `null` removes the property.
         */
        public var calendarAddress: String?
            get() = read(Properties.CALENDAR_ADDRESS)
            set(value) {
                write(Properties.CALENDAR_ADDRESS, value)
            }

        /**
         * Sets [calendarAddress] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun calendarAddress(value: String?): Builder = apply { calendarAddress = value }

        /**
         * The creation time of the participant component being built (`CREATED`,
         * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
         * property.
         */
        public var created: ICalDateTime?
            get() = read(Properties.CREATED)
            set(value) {
                write(Properties.CREATED, value)
            }

        /**
         * Sets [created] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun created(value: ICalDateTime?): Builder = apply { created = value }

        /**
         * The description of the participant component being built (`DESCRIPTION`,
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
         * The time stamp of the participant component being built (`DTSTAMP`,
         * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
         * property.
         */
        public var stamp: ICalDateTime?
            get() = read(Properties.DTSTAMP)
            set(value) {
                write(Properties.DTSTAMP, value)
            }

        /**
         * Sets [stamp] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun stamp(value: ICalDateTime?): Builder = apply { stamp = value }

        /**
         * The geographic position of the participant component being built (`GEO`,
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
         * The time of the last modification of the participant component being built (`LAST-MODIFIED`,
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
         * The priority of the participant component being built (`PRIORITY`,
         * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
         * property.
         */
        public var priority: Int?
            get() = read(Properties.PRIORITY)
            set(value) {
                write(Properties.PRIORITY, value)
            }

        /**
         * Sets [priority] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun priority(value: Int?): Builder = apply { priority = value }

        /**
         * The revision sequence number of the participant component being built (`SEQUENCE`,
         * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
         * property.
         */
        public var sequence: Int?
            get() = read(Properties.SEQUENCE)
            set(value) {
                write(Properties.SEQUENCE, value)
            }

        /**
         * Sets [sequence] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun sequence(value: Int?): Builder = apply { sequence = value }

        /**
         * The status of the participant component being built (`STATUS`,
         * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
         * property.
         */
        public var status: EventStatus?
            get() = read(Properties.EVENT_STATUS)
            set(value) {
                write(Properties.EVENT_STATUS, value)
            }

        /**
         * Sets [status] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun status(value: EventStatus?): Builder = apply { status = value }

        /**
         * The summary, the short title of the participant component being built (`SUMMARY`,
         * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)); assigning `null` removes the
         * property.
         */
        public var summary: String?
            get() = read(Properties.SUMMARY)
            set(value) {
                write(Properties.SUMMARY, value)
            }

        /**
         * Sets [summary] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun summary(value: String?): Builder = apply { summary = value }

        /**
         * The URL of the participant component being built (`URL`,
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

        /** Adds [value] as an `ATTACH` property. */
        public fun attach(value: Attach): Builder = property(Properties.ATTACH, value)

        /** Adds a `CATEGORIES` property with the given [values]. */
        public fun categories(vararg values: String): Builder =
            property(Properties.CATEGORIES, Categories(values.asList()))

        /** Adds a `COMMENT` property with the text [text]. */
        public fun comment(text: String): Builder = property(Properties.COMMENT, text)

        /** Adds a `CONTACT` property with the text [text]. */
        public fun contact(text: String): Builder = property(Properties.CONTACT, Contact(text))

        /** Adds a `LOCATION` property with the text [text]. */
        public fun locationText(text: String): Builder = property(Properties.LOCATION, Location(text))

        /** Adds [value] as a `REQUEST-STATUS` property. */
        public fun requestStatus(value: RequestStatus): Builder = property(Properties.REQUEST_STATUS, value)

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

        /** Adds a `RESOURCES` property with the given [values]. */
        public fun resources(vararg values: String): Builder =
            property(Properties.RESOURCES, Resources(values.asList()))

        /** Adds [value] as a `STYLED-DESCRIPTION` property. */
        public fun styledDescription(value: StyledDescription): Builder = property(Properties.STYLED_DESCRIPTION, value)

        /** Adds [value] as a `STRUCTURED-DATA` property. */
        public fun structuredData(value: StructuredData): Builder = property(Properties.STRUCTURED_DATA, value)

        /** Adds [value] as a `LINK` property. */
        public fun link(value: Link): Builder = property(Properties.LINK, value)

        /** Adds a `CONCEPT` property with the URI [uri]. */
        public fun concept(uri: String): Builder = property(Properties.CONCEPT, uri)

        /** Adds a `REFID` property with the text [text]. */
        public fun refId(text: String): Builder = property(Properties.REFID, text)

        /** The locations of the participant component being built, as an editable list. */
        public val locations: ComponentList<VLocation>
            get() = ComponentList(this, "VLOCATION", null, ::VLocation, VLocation::component)

        /**
         * Adds a location that [block] sets up.
         *
         * @param block sets up the new location.
         */
        public fun location(block: VLocation.Builder.() -> Unit): Builder =
            location(VLocation.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a location of the participant component. */
        public fun location(value: VLocation): Builder = apply { locations.add(value) }

        /** Edits the locations of the participant component in [block]: add, replace or remove them. */
        public fun locations(block: ComponentList<VLocation>.() -> Unit): Builder = apply { locations.block() }

        /** The resources of the participant component being built, as an editable list. */
        public val resourceComponents: ComponentList<VResource>
            get() = ComponentList(this, "VRESOURCE", null, ::VResource, VResource::component)

        /**
         * Adds a resource that [block] sets up.
         *
         * @param block sets up the new resource.
         */
        public fun resourceComponent(block: VResource.Builder.() -> Unit): Builder =
            resourceComponent(VResource.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a resource of the participant component. */
        public fun resourceComponent(value: VResource): Builder = apply { resourceComponents.add(value) }

        /** Edits the resources of the participant component in [block]: add, replace or remove them. */
        public fun resourceComponents(block: ComponentList<VResource>.() -> Unit): Builder = apply {
            resourceComponents.block()
        }

        /**
         * Creates the participant.
         *
         * @throws IllegalStateException if `UID` or `PARTICIPANT-TYPE` is missing and the builder has no defaults for
         *   the `UID`.
         */
        public fun build(): Participant {
            requireUid()
            requireParticipantType()
            return Participant(buildNode())
        }
    }

    /** Creates Participant values and builders. */
    public companion object {
        /**
         * Wraps [component] as a Participant, or returns `null` when it is not a `PARTICIPANT` component. Nothing is
         * copied or validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): Participant? =
            if (named(component, "PARTICIPANT")) Participant(component) else null

        /** Returns an empty builder. A `UID` and a participant type are required. */
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

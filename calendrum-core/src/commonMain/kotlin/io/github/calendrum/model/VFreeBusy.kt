package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod
import kotlin.jvm.JvmStatic

/**
 * A `VFREEBUSY` component: a request for or a report of free and busy time
 * ([RFC 5545 §3.6.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.4)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class VFreeBusy internal constructor(public val component: ICalComponent) {
    /**
     * The globally unique identifier of the free/busy component (`UID`,
     * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
     * does not decode.
     */
    public val uid: String? get() = component[Properties.UID]

    /**
     * The time stamp of the free/busy component (`DTSTAMP`,
     * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
     * does not decode.
     */
    public val stamp: ICalDateTime? get() = component[Properties.DTSTAMP]

    /**
     * The contact for the free/busy information (`CONTACT`,
     * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)), or `null` when it is missing or
     * does not decode.
     */
    public val contact: String? get() = component[Properties.CONTACT]?.text

    /**
     * The start of the free/busy component (`DTSTART`,
     * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
     * does not decode.
     */
    public val start: ICalDateTime? get() = component[Properties.DTSTART]

    /**
     * The end of the free/busy component (`DTEND`,
     * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)), or `null` when it is missing or
     * does not decode.
     */
    public val end: ICalDateTime? get() = component[Properties.DTEND]

    /**
     * The organizer of the free/busy component (`ORGANIZER`,
     * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
     * does not decode.
     */
    public val organizer: Organizer? get() = component[Properties.ORGANIZER]

    /**
     * The URL of the free/busy component (`URL`,
     * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
     * does not decode.
     */
    public val url: String? get() = component[Properties.URL]

    /**
     * The attendees of the free/busy component (`ATTENDEE` properties,
     * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attendees: List<Attendee> get() = component.all(Properties.ATTENDEE)

    /**
     * The comments of the free/busy component (`COMMENT` properties,
     * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
     * decode are skipped.
     */
    public val comments: List<String> get() = texts(component, Properties.COMMENT)

    /** The free/busy component nested in the free/busy component, in source order. */
    public val freeBusy: List<FreeBusy> get() = component.all(Properties.FREEBUSY)

    /**
     * The request statuses of the free/busy component (`REQUEST-STATUS` properties,
     * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
     * decode are skipped.
     */
    public val requestStatuses: List<RequestStatus> get() = component.all(Properties.REQUEST_STATUS)

    /**
     * The styled descriptions of the free/busy component (`STYLED-DESCRIPTION` properties,
     * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
     * skipped.
     */
    public val styledDescriptions: List<StyledDescription> get() = component.all(Properties.STYLED_DESCRIPTION)

    /**
     * The links of the free/busy component (`LINK` properties,
     * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
     * skipped.
     */
    public val links: List<Link> get() = component.all(Properties.LINK)

    /**
     * The concepts of the free/busy component (`CONCEPT` properties,
     * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

    /**
     * The reference identifiers of the free/busy component (`REFID` properties,
     * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
     * skipped.
     */
    public val refIds: List<String> get() = texts(component, Properties.REFID)

    /**
     * The relationships to other components of the free/busy component (`RELATED-TO` properties,
     * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

    /** The participants nested in the free/busy component, in source order. */
    public val participants: List<Participant> get() = children(component, "PARTICIPANT", ::Participant)

    /** The locations nested in the free/busy component, in source order. */
    public val locations: List<VLocation> get() = children(component, "VLOCATION", ::VLocation)

    /** The resources nested in the free/busy component, in source order. */
    public val resourceComponents: List<VResource> get() = children(component, "VRESOURCE", ::VResource)

    /**
     * Returns a copy of this free/busy component changed by [block]. Properties and components that [block] does not
     * touch are the same objects as in this free/busy component.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): VFreeBusy = toBuilder().apply(block).build()

    /** Returns a builder that starts from this free/busy component; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this free/busy component and takes generated `UID` and `DTSTAMP` values from
     * [defaults] where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a VFreeBusy with an equal VFREEBUSY component. */
    override fun equals(other: Any?): Boolean = this === other || (other is VFreeBusy && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("VFreeBusy", component)

    /**
     * Collects the properties and nested components of a new or edited free/busy component, with a Kotlin DSL and
     * chained calls. Get one from [VFreeBusy.builder] or [VFreeBusy.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("VFREEBUSY", original, defaults) {
        /**
         * The globally unique identifier of the free/busy component being built (`UID`,
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
         * The time stamp of the free/busy component being built (`DTSTAMP`,
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
         * The contact for the free/busy information being built (`CONTACT`,
         * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)); assigning `null` removes the
         * property.
         */
        public var contact: String?
            get() = read(Properties.CONTACT)?.text
            set(value) {
                write(Properties.CONTACT, value?.let(::Contact))
            }

        /**
         * Sets [contact] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun contact(value: String?): Builder = apply { contact = value }

        /**
         * The start of the free/busy component being built (`DTSTART`,
         * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
         * property.
         */
        public var start: ICalDateTime?
            get() = read(Properties.DTSTART)
            set(value) {
                write(Properties.DTSTART, value)
            }

        /**
         * Sets [start] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun start(value: ICalDateTime?): Builder = apply { start = value }

        /**
         * The end of the free/busy component being built (`DTEND`,
         * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)); assigning `null` removes the
         * property.
         */
        public var end: ICalDateTime?
            get() = read(Properties.DTEND)
            set(value) {
                write(Properties.DTEND, value)
            }

        /**
         * Sets [end] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun end(value: ICalDateTime?): Builder = apply { end = value }

        /**
         * The organizer of the free/busy component being built (`ORGANIZER`,
         * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
         * property.
         */
        public var organizer: Organizer?
            get() = read(Properties.ORGANIZER)
            set(value) {
                write(Properties.ORGANIZER, value)
            }

        /**
         * Sets [organizer] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun organizer(value: Organizer?): Builder = apply { organizer = value }

        /**
         * The URL of the free/busy component being built (`URL`,
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

        /** Sets the organizer to the address [address], set up by [block]. */
        public fun organizer(address: String, block: Organizer.Builder.() -> Unit): Builder =
            apply { organizer = Organizer.builder(address).apply(block).build() }

        /**
         * The attendees added so far (`ATTENDEE`,
         * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
         */
        public val attendees: List<Attendee> get() = readAll(Properties.ATTENDEE)

        /** Adds an `ATTENDEE` for the address [address], set up by [block]. */
        public fun attendee(address: String, block: Attendee.Builder.() -> Unit): Builder =
            attendee(Attendee.builder(address).apply(block).build())

        /** Adds [attendee] as an `ATTENDEE` property. */
        public fun attendee(attendee: Attendee): Builder = property(Properties.ATTENDEE, attendee)

        /** Edits the attendees in [block]: add, replace or remove them. */
        public fun attendees(block: AttendeeList.() -> Unit): Builder = apply { AttendeeList(this).block() }

        /** Adds a `COMMENT` property with the text [text]. */
        public fun comment(text: String): Builder = property(Properties.COMMENT, text)

        /** Adds [freeBusy] as a free/busy component of the free/busy component. */
        public fun freeBusy(freeBusy: FreeBusy): Builder = property(Properties.FREEBUSY, freeBusy)

        /** Adds [periods] as a free/busy component of the free/busy component. */
        public fun freeBusy(vararg periods: ICalPeriod): Builder =
            property(Properties.FREEBUSY, FreeBusy(periods.asList()))

        /** Adds [status] as a `REQUEST-STATUS` property. */
        public fun requestStatus(status: RequestStatus): Builder = property(Properties.REQUEST_STATUS, status)

        /** Adds [value] as a `STYLED-DESCRIPTION` property. */
        public fun styledDescription(value: StyledDescription): Builder = property(Properties.STYLED_DESCRIPTION, value)

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

        /** The participants of the free/busy component being built, as an editable list. */
        public val participants: ComponentList<Participant>
            get() = ComponentList(this, "PARTICIPANT", null, ::Participant, Participant::component)

        /**
         * Adds a participant that [block] sets up.
         *
         * @param block sets up the new participant.
         */
        public fun participant(block: Participant.Builder.() -> Unit): Builder =
            participant(Participant.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a participant of the free/busy component. */
        public fun participant(value: Participant): Builder = apply { participants.add(value) }

        /** Edits the participants of the free/busy component in [block]: add, replace or remove them. */
        public fun participants(block: ComponentList<Participant>.() -> Unit): Builder = apply { participants.block() }

        /** The locations of the free/busy component being built, as an editable list. */
        public val locations: ComponentList<VLocation>
            get() = ComponentList(this, "VLOCATION", null, ::VLocation, VLocation::component)

        /**
         * Adds a location that [block] sets up.
         *
         * @param block sets up the new location.
         */
        public fun location(block: VLocation.Builder.() -> Unit): Builder =
            location(VLocation.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a location of the free/busy component. */
        public fun location(value: VLocation): Builder = apply { locations.add(value) }

        /** Edits the locations of the free/busy component in [block]: add, replace or remove them. */
        public fun locations(block: ComponentList<VLocation>.() -> Unit): Builder = apply { locations.block() }

        /** The resources of the free/busy component being built, as an editable list. */
        public val resourceComponents: ComponentList<VResource>
            get() = ComponentList(this, "VRESOURCE", null, ::VResource, VResource::component)

        /**
         * Adds a resource that [block] sets up.
         *
         * @param block sets up the new resource.
         */
        public fun resourceComponent(block: VResource.Builder.() -> Unit): Builder =
            resourceComponent(VResource.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a resource of the free/busy component. */
        public fun resourceComponent(value: VResource): Builder = apply { resourceComponents.add(value) }

        /** Edits the resources of the free/busy component in [block]: add, replace or remove them. */
        public fun resourceComponents(block: ComponentList<VResource>.() -> Unit): Builder = apply {
            resourceComponents.block()
        }

        /**
         * Creates the free/busy component.
         *
         * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults.
         */
        public fun build(): VFreeBusy {
            requireIdentity()
            return VFreeBusy(buildNode())
        }
    }

    /** Creates VFreeBusy values and builders. */
    public companion object {
        /**
         * Wraps [component] as a VFreeBusy, or returns `null` when it is not a `VFREEBUSY` component. Nothing is copied
         * or validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): VFreeBusy? =
            if (named(component, "VFREEBUSY")) VFreeBusy(component) else null

        /**
         * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
         * generate them.
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

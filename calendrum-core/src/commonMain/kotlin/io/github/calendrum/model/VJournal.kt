package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlin.jvm.JvmStatic

/**
 * A `VJOURNAL` component: a note attached to a date
 * ([RFC 5545 §3.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.3)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class VJournal internal constructor(public val component: ICalComponent) {
    /**
     * The globally unique identifier of the journal entry (`UID`,
     * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
     * does not decode.
     */
    public val uid: String? get() = component[Properties.UID]

    /**
     * The time stamp of the journal entry (`DTSTAMP`,
     * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
     * does not decode.
     */
    public val stamp: ICalDateTime? get() = component[Properties.DTSTAMP]

    /**
     * The access classification of the journal entry (`CLASS`,
     * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)), or `null` when it is missing or
     * does not decode.
     */
    public val classification: Classification? get() = component[Properties.CLASS]

    /**
     * The creation time of the journal entry (`CREATED`,
     * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)), or `null` when it is missing or
     * does not decode.
     */
    public val created: ICalDateTime? get() = component[Properties.CREATED]

    /**
     * The start of the journal entry (`DTSTART`,
     * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
     * does not decode.
     */
    public val start: ICalDateTime? get() = component[Properties.DTSTART]

    /**
     * The time of the last modification of the journal entry (`LAST-MODIFIED`,
     * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
     * does not decode.
     */
    public val lastModified: ICalDateTime? get() = component[Properties.LAST_MODIFIED]

    /**
     * The organizer of the journal entry (`ORGANIZER`,
     * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
     * does not decode.
     */
    public val organizer: Organizer? get() = component[Properties.ORGANIZER]

    /**
     * The recurrence identifier of the journal entry (`RECURRENCE-ID`,
     * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
     * does not decode.
     */
    public val recurrenceId: ICalDateTime? get() = component[Properties.RECURRENCE_ID]?.value

    /**
     * The revision sequence number of the journal entry (`SEQUENCE`,
     * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
     * does not decode.
     */
    public val sequence: Int? get() = component[Properties.SEQUENCE]

    /**
     * The status of the journal entry (`STATUS`,
     * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
     * does not decode.
     */
    public val status: JournalStatus? get() = component[Properties.JOURNAL_STATUS]

    /**
     * The summary, the short title of the journal entry (`SUMMARY`,
     * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
     * does not decode.
     */
    public val summary: String? get() = component[Properties.SUMMARY]

    /**
     * The URL of the journal entry (`URL`,
     * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
     * does not decode.
     */
    public val url: String? get() = component[Properties.URL]

    /**
     * The recurrence rule of the journal entry (`RRULE`,
     * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
     * does not decode.
     */
    public val rrule: Recur? get() = component[Properties.RRULE]

    /**
     * The attachments of the journal entry (`ATTACH` properties,
     * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attachments: List<Attach> get() = component.all(Properties.ATTACH)

    /**
     * The attendees of the journal entry (`ATTENDEE` properties,
     * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attendees: List<Attendee> get() = component.all(Properties.ATTENDEE)

    /**
     * The categories of the journal entry (`CATEGORIES` properties,
     * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val categories: List<String> get() = categories(component)

    /**
     * The comments of the journal entry (`COMMENT` properties,
     * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
     * decode are skipped.
     */
    public val comments: List<String> get() = texts(component, Properties.COMMENT)

    /**
     * The contacts of the journal entry (`CONTACT` properties,
     * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)) in source order; ones that do not
     * decode are skipped.
     */
    public val contacts: List<String> get() = contacts(component)

    /**
     * The descriptions of the journal entry (`DESCRIPTION` properties,
     * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val descriptions: List<String> get() = texts(component, Properties.DESCRIPTION)

    /**
     * The excluded dates of the journal entry (`EXDATE` properties,
     * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val exdates: List<ICalDateTime> get() = exdates(component)

    /**
     * The relationships to other components of the journal entry (`RELATED-TO` properties,
     * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

    /**
     * The additional dates of the journal entry (`RDATE` properties,
     * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val rdates: List<ICalDateTime> get() = rdates(component)

    /**
     * The request statuses of the journal entry (`REQUEST-STATUS` properties,
     * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
     * decode are skipped.
     */
    public val requestStatuses: List<RequestStatus> get() = component.all(Properties.REQUEST_STATUS)

    /**
     * The color of the journal entry (`COLOR`, [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), or
     * `null` when it is missing or does not decode.
     */
    public val color: String? get() = component[Properties.COLOR]

    /**
     * The images of the journal entry (`IMAGE` properties,
     * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
     * are skipped.
     */
    public val images: List<Image> get() = component.all(Properties.IMAGE)

    /**
     * The styled descriptions of the journal entry (`STYLED-DESCRIPTION` properties,
     * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
     * skipped.
     */
    public val styledDescriptions: List<StyledDescription> get() = component.all(Properties.STYLED_DESCRIPTION)

    /**
     * The structured data of the journal entry (`STRUCTURED-DATA` properties,
     * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
     * skipped.
     */
    public val structuredData: List<StructuredData> get() = component.all(Properties.STRUCTURED_DATA)

    /**
     * The links of the journal entry (`LINK` properties,
     * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
     * skipped.
     */
    public val links: List<Link> get() = component.all(Properties.LINK)

    /**
     * The concepts of the journal entry (`CONCEPT` properties,
     * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

    /**
     * The reference identifiers of the journal entry (`REFID` properties,
     * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
     * skipped.
     */
    public val refIds: List<String> get() = texts(component, Properties.REFID)

    /** The participants nested in the journal entry, in source order. */
    public val participants: List<Participant> get() = children(component, "PARTICIPANT", ::Participant)

    /** The locations nested in the journal entry, in source order. */
    public val locations: List<VLocation> get() = children(component, "VLOCATION", ::VLocation)

    /** The resources nested in the journal entry, in source order. */
    public val resourceComponents: List<VResource> get() = children(component, "VRESOURCE", ::VResource)

    /**
     * Returns a copy of this journal entry changed by [block]. Properties and components that [block] does not touch
     * are the same objects as in this journal entry.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): VJournal = toBuilder().apply(block).build()

    /** Returns a builder that starts from this journal entry; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this journal entry and takes generated `UID` and `DTSTAMP` values from
     * [defaults] where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a VJournal with an equal VJOURNAL component. */
    override fun equals(other: Any?): Boolean = this === other || (other is VJournal && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("VJournal", component)

    /**
     * Collects the properties and nested components of a new or edited journal entry, with a Kotlin DSL and chained
     * calls. Get one from [VJournal.builder] or [VJournal.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("VJOURNAL", original, defaults) {
        /**
         * The globally unique identifier of the journal entry being built (`UID`,
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
         * The time stamp of the journal entry being built (`DTSTAMP`,
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
         * The access classification of the journal entry being built (`CLASS`,
         * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
         * property.
         */
        public var classification: Classification?
            get() = read(Properties.CLASS)
            set(value) {
                write(Properties.CLASS, value)
            }

        /**
         * Sets [classification] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun classification(value: Classification?): Builder = apply { classification = value }

        /**
         * The creation time of the journal entry being built (`CREATED`,
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
         * The start of the journal entry being built (`DTSTART`,
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
         * The time of the last modification of the journal entry being built (`LAST-MODIFIED`,
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
         * The organizer of the journal entry being built (`ORGANIZER`,
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
         * The recurrence identifier of the journal entry being built (`RECURRENCE-ID`,
         * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
         * property.
         */
        public var recurrenceId: ICalDateTime?
            get() = read(Properties.RECURRENCE_ID)?.value
            set(value) {
                write(Properties.RECURRENCE_ID, value?.let(::RecurrenceId))
            }

        /**
         * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun recurrenceId(value: ICalDateTime?): Builder = apply { recurrenceId = value }

        /**
         * The revision sequence number of the journal entry being built (`SEQUENCE`,
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
         * The status of the journal entry being built (`STATUS`,
         * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
         * property.
         */
        public var status: JournalStatus?
            get() = read(Properties.JOURNAL_STATUS)
            set(value) {
                write(Properties.JOURNAL_STATUS, value)
            }

        /**
         * Sets [status] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun status(value: JournalStatus?): Builder = apply { status = value }

        /**
         * The summary, the short title of the journal entry being built (`SUMMARY`,
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
         * The URL of the journal entry being built (`URL`,
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
         * The recurrence rule of the journal entry being built (`RRULE`,
         * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
         * property.
         */
        public var rrule: Recur?
            get() = read(Properties.RRULE)
            set(value) {
                write(Properties.RRULE, value)
            }

        /**
         * Sets [rrule] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun rrule(value: Recur?): Builder = apply { rrule = value }

        /** Sets the organizer to the address [address], set up by [block]. */
        public fun organizer(address: String, block: Organizer.Builder.() -> Unit): Builder =
            apply { organizer = Organizer.builder(address).apply(block).build() }

        /** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
        public fun rrule(frequency: Frequency, block: Recur.Builder.() -> Unit): Builder =
            apply { rrule = Recur.builder(frequency).apply(block).build() }

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

        /** Adds [attach] as an `ATTACH` property. */
        public fun attach(attach: Attach): Builder = property(Properties.ATTACH, attach)

        /** Adds a `CATEGORIES` property with the given [values]. */
        public fun categories(vararg values: String): Builder =
            property(Properties.CATEGORIES, Categories(values.asList()))

        /** Adds a `COMMENT` property with the text [text]. */
        public fun comment(text: String): Builder = property(Properties.COMMENT, text)

        /** Adds a `CONTACT` property with the text [text]. */
        public fun contact(text: String): Builder = property(Properties.CONTACT, Contact(text))

        /** Adds a `DESCRIPTION` property with the text [text]. */
        public fun description(text: String): Builder = property(Properties.DESCRIPTION, text)

        /** Adds an `EXDATE` property with the given [dates]. */
        public fun exdate(vararg dates: ICalDateTime): Builder = property(Properties.EXDATE, ExDate(dates.asList()))

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

        /** Adds an `RDATE` property with the given [dates]. */
        public fun rdate(vararg dates: ICalDateTime): Builder =
            property(Properties.RDATE, RDate(dates.asList(), emptyList()))

        /** Adds an `RDATE` property with the given [periods]. */
        public fun rdate(vararg periods: ICalPeriod): Builder =
            property(Properties.RDATE, RDate(emptyList(), periods.asList()))

        /** Adds [status] as a `REQUEST-STATUS` property. */
        public fun requestStatus(status: RequestStatus): Builder = property(Properties.REQUEST_STATUS, status)

        /**
         * The color of the journal entry being built (`COLOR`,
         * [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)); assigning `null` removes the property.
         */
        public var color: String?
            get() = read(Properties.COLOR)
            set(value) {
                write(Properties.COLOR, value)
            }

        /**
         * Sets [color] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun color(value: String?): Builder = apply { color = value }

        /** Adds [value] as an `IMAGE` property. */
        public fun image(value: Image): Builder = property(Properties.IMAGE, value)

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

        /** The participants of the journal entry being built, as an editable list. */
        public val participants: ComponentList<Participant>
            get() = ComponentList(this, "PARTICIPANT", null, ::Participant, Participant::component)

        /**
         * Adds a participant that [block] sets up.
         *
         * @param block sets up the new participant.
         */
        public fun participant(block: Participant.Builder.() -> Unit): Builder =
            participant(Participant.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a participant of the journal entry. */
        public fun participant(value: Participant): Builder = apply { participants.add(value) }

        /** Edits the participants of the journal entry in [block]: add, replace or remove them. */
        public fun participants(block: ComponentList<Participant>.() -> Unit): Builder = apply { participants.block() }

        /** The locations of the journal entry being built, as an editable list. */
        public val locations: ComponentList<VLocation>
            get() = ComponentList(this, "VLOCATION", null, ::VLocation, VLocation::component)

        /**
         * Adds a location that [block] sets up.
         *
         * @param block sets up the new location.
         */
        public fun location(block: VLocation.Builder.() -> Unit): Builder =
            location(VLocation.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a location of the journal entry. */
        public fun location(value: VLocation): Builder = apply { locations.add(value) }

        /** Edits the locations of the journal entry in [block]: add, replace or remove them. */
        public fun locations(block: ComponentList<VLocation>.() -> Unit): Builder = apply { locations.block() }

        /** The resources of the journal entry being built, as an editable list. */
        public val resourceComponents: ComponentList<VResource>
            get() = ComponentList(this, "VRESOURCE", null, ::VResource, VResource::component)

        /**
         * Adds a resource that [block] sets up.
         *
         * @param block sets up the new resource.
         */
        public fun resourceComponent(block: VResource.Builder.() -> Unit): Builder =
            resourceComponent(VResource.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a resource of the journal entry. */
        public fun resourceComponent(value: VResource): Builder = apply { resourceComponents.add(value) }

        /** Edits the resources of the journal entry in [block]: add, replace or remove them. */
        public fun resourceComponents(block: ComponentList<VResource>.() -> Unit): Builder = apply {
            resourceComponents.block()
        }

        /**
         * Creates the journal entry.
         *
         * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
         *   of the `RRULE` does not suit the type of `DTSTART`.
         */
        public fun build(): VJournal {
            requireIdentity()
            checkUntil()
            return VJournal(buildNode())
        }
    }

    /** Creates VJournal values and builders. */
    public companion object {
        /**
         * Wraps [component] as a VJournal, or returns `null` when it is not a `VJOURNAL` component. Nothing is copied
         * or validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): VJournal? =
            if (named(component, "VJOURNAL")) VJournal(component) else null

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

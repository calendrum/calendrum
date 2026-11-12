@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlin.concurrent.Volatile
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName

/**
 * A `VTODO` component: a task with an optional due date
 * ([RFC 5545 §3.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.2)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class VTodo internal constructor(public val component: ICalComponent) {
    @Volatile
    private var alarmList: List<VAlarm>? = null

    /**
     * The globally unique identifier of the to-do (`UID`,
     * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
     * does not decode.
     */
    public val uid: String? get() = component[Properties.UID]

    /**
     * The time stamp of the to-do (`DTSTAMP`,
     * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
     * does not decode.
     */
    public val stamp: ICalDateTime? get() = component[Properties.DTSTAMP]

    /**
     * The access classification of the to-do (`CLASS`,
     * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)), or `null` when it is missing or
     * does not decode.
     */
    public val classification: Classification? get() = component[Properties.CLASS]

    /**
     * The completion time of the to-do (`COMPLETED`,
     * [RFC 5545 §3.8.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.1)), or `null` when it is missing or
     * does not decode.
     */
    public val completed: ICalDateTime? get() = component[Properties.COMPLETED]

    /**
     * The creation time of the to-do (`CREATED`,
     * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)), or `null` when it is missing or
     * does not decode.
     */
    public val created: ICalDateTime? get() = component[Properties.CREATED]

    /**
     * The description of the to-do (`DESCRIPTION`,
     * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
     * does not decode.
     */
    @ObjCName("descriptionText")
    public val description: String? get() = component[Properties.DESCRIPTION]

    /**
     * The start of the to-do (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
     * or `null` when it is missing or does not decode.
     */
    public val start: ICalDateTime? get() = component[Properties.DTSTART]

    /**
     * The geographic position of the to-do (`GEO`,
     * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
     * does not decode.
     */
    public val geo: Geo? get() = component[Properties.GEO]

    /**
     * The time of the last modification of the to-do (`LAST-MODIFIED`,
     * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
     * does not decode.
     */
    public val lastModified: ICalDateTime? get() = component[Properties.LAST_MODIFIED]

    /** The location nested in the to-do, in source order. */
    public val location: String? get() = component[Properties.LOCATION]?.text

    /**
     * The organizer of the to-do (`ORGANIZER`,
     * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
     * does not decode.
     */
    public val organizer: Organizer? get() = component[Properties.ORGANIZER]

    /**
     * The percentage complete of the to-do (`PERCENT-COMPLETE`,
     * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)), or `null` when it is missing or
     * does not decode.
     */
    public val percentComplete: Int? get() = component[Properties.PERCENT_COMPLETE]

    /**
     * The priority of the to-do (`PRIORITY`,
     * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
     * does not decode.
     */
    public val priority: Int? get() = component[Properties.PRIORITY]

    /**
     * The recurrence identifier of the to-do (`RECURRENCE-ID`,
     * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
     * does not decode.
     */
    public val recurrenceId: ICalDateTime? get() = component[Properties.RECURRENCE_ID]?.value

    /**
     * The revision sequence number of the to-do (`SEQUENCE`,
     * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
     * does not decode.
     */
    public val sequence: Int? get() = component[Properties.SEQUENCE]

    /**
     * The status of the to-do (`STATUS`,
     * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
     * does not decode.
     */
    public val status: TodoStatus? get() = component[Properties.TODO_STATUS]

    /**
     * The summary, the short title of the to-do (`SUMMARY`,
     * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
     * does not decode.
     */
    public val summary: String? get() = component[Properties.SUMMARY]

    /**
     * The URL of the to-do (`URL`, [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or
     * `null` when it is missing or does not decode.
     */
    public val url: String? get() = component[Properties.URL]

    /**
     * The recurrence rule of the to-do (`RRULE`,
     * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
     * does not decode.
     */
    public val rrule: Recur? get() = component[Properties.RRULE]

    /**
     * The due time of the to-do (`DUE`, [RFC 5545 §3.8.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.3)),
     * or `null` when it is missing or does not decode.
     */
    public val due: ICalDateTime? get() = component[Properties.DUE]

    /**
     * The duration of the to-do (`DURATION`,
     * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
     * does not decode.
     */
    public val duration: ICalDuration? get() = component[Properties.DURATION]

    /**
     * The attachments of the to-do (`ATTACH` properties,
     * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attachments: List<Attach> get() = component.all(Properties.ATTACH)

    /**
     * The attendees of the to-do (`ATTENDEE` properties,
     * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attendees: List<Attendee> get() = component.all(Properties.ATTENDEE)

    /**
     * The categories of the to-do (`CATEGORIES` properties,
     * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val categories: List<String> get() = categories(component)

    /**
     * The comments of the to-do (`COMMENT` properties,
     * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
     * decode are skipped.
     */
    public val comments: List<String> get() = texts(component, Properties.COMMENT)

    /**
     * The contacts of the to-do (`CONTACT` properties,
     * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)) in source order; ones that do not
     * decode are skipped.
     */
    public val contacts: List<String> get() = contacts(component)

    /**
     * The excluded dates of the to-do (`EXDATE` properties,
     * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val exdates: List<ICalDateTime> get() = exdates(component)

    /**
     * The request statuses of the to-do (`REQUEST-STATUS` properties,
     * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
     * decode are skipped.
     */
    public val requestStatuses: List<RequestStatus> get() = component.all(Properties.REQUEST_STATUS)

    /**
     * The relationships to other components of the to-do (`RELATED-TO` properties,
     * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

    /**
     * The resources of the to-do (`RESOURCES` properties,
     * [RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)) from all properties in source
     * order; ones that do not decode are skipped.
     */
    public val resources: List<String> get() = resources(component)

    /**
     * The additional dates of the to-do (`RDATE` properties,
     * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val rdates: List<ICalDateTime> get() = rdates(component)

    /** The alarms nested in the to-do, in source order. */
    public val alarms: List<VAlarm>
        get() = alarmList ?: children(component, "VALARM", ::VAlarm).also { alarmList = it }

    /**
     * The color of the to-do (`COLOR`, [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), or `null`
     * when it is missing or does not decode.
     */
    public val color: String? get() = component[Properties.COLOR]

    /**
     * The images of the to-do (`IMAGE` properties,
     * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
     * are skipped.
     */
    public val images: List<Image> get() = component.all(Properties.IMAGE)

    /**
     * The conferences of the to-do (`CONFERENCE` properties,
     * [RFC 7986 §5.11](https://www.rfc-editor.org/rfc/rfc7986#section-5.11)) in source order; ones that do not decode
     * are skipped.
     */
    public val conferences: List<Conference> get() = component.all(Properties.CONFERENCE)

    /**
     * The styled descriptions of the to-do (`STYLED-DESCRIPTION` properties,
     * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
     * skipped.
     */
    public val styledDescriptions: List<StyledDescription> get() = component.all(Properties.STYLED_DESCRIPTION)

    /**
     * The structured data of the to-do (`STRUCTURED-DATA` properties,
     * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
     * skipped.
     */
    public val structuredData: List<StructuredData> get() = component.all(Properties.STRUCTURED_DATA)

    /**
     * The links of the to-do (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
     * in source order; ones that do not decode are skipped.
     */
    public val links: List<Link> get() = component.all(Properties.LINK)

    /**
     * The concepts of the to-do (`CONCEPT` properties,
     * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

    /**
     * The reference identifiers of the to-do (`REFID` properties,
     * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
     * skipped.
     */
    public val refIds: List<String> get() = texts(component, Properties.REFID)

    /** The participants nested in the to-do, in source order. */
    public val participants: List<Participant> get() = children(component, "PARTICIPANT", ::Participant)

    /** The locations nested in the to-do, in source order. */
    public val locations: List<VLocation> get() = children(component, "VLOCATION", ::VLocation)

    /** The resources nested in the to-do, in source order. */
    public val resourceComponents: List<VResource> get() = children(component, "VRESOURCE", ::VResource)

    /**
     * Returns a copy of this to-do changed by [block]. Properties and components that [block] does not touch are the
     * same objects as in this to-do.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): VTodo = toBuilder().apply(block).build()

    /** Returns a builder that starts from this to-do; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this to-do and takes generated `UID` and `DTSTAMP` values from [defaults]
     * where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a VTodo with an equal VTODO component. */
    override fun equals(other: Any?): Boolean = this === other || (other is VTodo && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("VTodo", component)

    /**
     * Collects the properties and nested components of a new or edited to-do, with a Kotlin DSL and chained calls. Get
     * one from [VTodo.builder] or [VTodo.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("VTODO", original, defaults) {
        /**
         * The globally unique identifier of the to-do being built (`UID`,
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
         * The time stamp of the to-do being built (`DTSTAMP`,
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
         * The access classification of the to-do being built (`CLASS`,
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
         * The completion time of the to-do being built (`COMPLETED`,
         * [RFC 5545 §3.8.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.1)); assigning `null` removes the
         * property.
         */
        public var completed: ICalDateTime?
            get() = read(Properties.COMPLETED)
            set(value) {
                write(Properties.COMPLETED, value)
            }

        /**
         * Sets [completed] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun completed(value: ICalDateTime?): Builder = apply { completed = value }

        /**
         * The creation time of the to-do being built (`CREATED`,
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
         * The description of the to-do being built (`DESCRIPTION`,
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
         * The start of the to-do being built (`DTSTART`,
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
         * The geographic position of the to-do being built (`GEO`,
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
         * The time of the last modification of the to-do being built (`LAST-MODIFIED`,
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
         * The location text of the to-do being built (`LOCATION`,
         * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
         * property.
         */
        public var location: String?
            get() = read(Properties.LOCATION)?.text
            set(value) {
                write(Properties.LOCATION, value?.let(::Location))
            }

        /**
         * Sets [location] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun location(@ObjCName("text") value: String?): Builder = apply { location = value }

        /**
         * The organizer of the to-do being built (`ORGANIZER`,
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
         * The percentage complete of the to-do being built (`PERCENT-COMPLETE`,
         * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)); assigning `null` removes the
         * property.
         */
        public var percentComplete: Int?
            get() = read(Properties.PERCENT_COMPLETE)
            set(value) {
                write(Properties.PERCENT_COMPLETE, value)
            }

        /**
         * Sets [percentComplete] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun percentComplete(value: Int?): Builder = apply { percentComplete = value }

        /**
         * The priority of the to-do being built (`PRIORITY`,
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
         * The recurrence identifier of the to-do being built (`RECURRENCE-ID`,
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
         * The revision sequence number of the to-do being built (`SEQUENCE`,
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
         * The status of the to-do being built (`STATUS`,
         * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
         * property.
         */
        public var status: TodoStatus?
            get() = read(Properties.TODO_STATUS)
            set(value) {
                write(Properties.TODO_STATUS, value)
            }

        /**
         * Sets [status] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun status(value: TodoStatus?): Builder = apply { status = value }

        /**
         * The summary, the short title of the to-do being built (`SUMMARY`,
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
         * The URL of the to-do being built (`URL`,
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
         * The recurrence rule of the to-do being built (`RRULE`,
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

        /**
         * The due time of the to-do being built (`DUE`,
         * [RFC 5545 §3.8.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.3)); assigning `null` removes the
         * property.
         */
        public var due: ICalDateTime?
            get() = read(Properties.DUE)
            set(value) {
                write(Properties.DUE, value)
            }

        /**
         * Sets [due] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun due(value: ICalDateTime?): Builder = apply { due = value }

        /**
         * The duration of the to-do being built (`DURATION`,
         * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)); assigning `null` removes the
         * property.
         */
        public var duration: ICalDuration?
            get() = read(Properties.DURATION)
            set(value) {
                write(Properties.DURATION, value)
            }

        /**
         * Sets [duration] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun duration(value: ICalDuration?): Builder = apply { duration = value }

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

        /** Adds an `EXDATE` property with the given [dates]. */
        public fun exdate(vararg dates: ICalDateTime): Builder = property(Properties.EXDATE, ExDate(dates.asList()))

        /** Adds [status] as a `REQUEST-STATUS` property. */
        public fun requestStatus(status: RequestStatus): Builder = property(Properties.REQUEST_STATUS, status)

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

        /** Adds an `RDATE` property with the given [dates]. */
        public fun rdate(vararg dates: ICalDateTime): Builder =
            property(Properties.RDATE, RDate(dates.asList(), emptyList()))

        /** Adds an `RDATE` property with the given [periods]. */
        public fun rdate(vararg periods: ICalPeriod): Builder =
            property(Properties.RDATE, RDate(emptyList(), periods.asList()))

        /** The alarms of the to-do being built, as an editable list. */
        public val alarms: ComponentList<VAlarm> get() = ComponentList(
            this,
            "VALARM",
            null,
            ::VAlarm,
            VAlarm::component,
        )

        /**
         * Adds a alarm that [block] sets up.
         *
         * @param block sets up the new alarm.
         */
        public fun alarm(block: VAlarm.Builder.() -> Unit): Builder =
            alarm(VAlarm.Builder(null, defaults).apply(block).build())

        /** Adds [alarm] as a alarm of the to-do. */
        public fun alarm(alarm: VAlarm): Builder = apply { alarms.add(alarm) }

        /** Edits the alarms of the to-do in [block]: add, replace or remove them. */
        public fun alarms(block: ComponentList<VAlarm>.() -> Unit): Builder = apply { alarms.block() }

        /**
         * The color of the to-do being built (`COLOR`,
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

        /** Adds [value] as a `CONFERENCE` property. */
        public fun conference(value: Conference): Builder = property(Properties.CONFERENCE, value)

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

        /** The participants of the to-do being built, as an editable list. */
        public val participants: ComponentList<Participant>
            get() = ComponentList(this, "PARTICIPANT", null, ::Participant, Participant::component)

        /**
         * Adds a participant that [block] sets up.
         *
         * @param block sets up the new participant.
         */
        public fun participant(block: Participant.Builder.() -> Unit): Builder =
            participant(Participant.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a participant of the to-do. */
        public fun participant(value: Participant): Builder = apply { participants.add(value) }

        /** Edits the participants of the to-do in [block]: add, replace or remove them. */
        public fun participants(block: ComponentList<Participant>.() -> Unit): Builder = apply { participants.block() }

        /** The locations of the to-do being built, as an editable list. */
        public val locations: ComponentList<VLocation>
            get() = ComponentList(this, "VLOCATION", null, ::VLocation, VLocation::component)

        /**
         * Adds a location that [block] sets up.
         *
         * @param block sets up the new location.
         */
        public fun location(block: VLocation.Builder.() -> Unit): Builder =
            location(VLocation.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a location of the to-do. */
        public fun location(value: VLocation): Builder = apply { locations.add(value) }

        /** Edits the locations of the to-do in [block]: add, replace or remove them. */
        public fun locations(block: ComponentList<VLocation>.() -> Unit): Builder = apply { locations.block() }

        /** The resources of the to-do being built, as an editable list. */
        public val resourceComponents: ComponentList<VResource>
            get() = ComponentList(this, "VRESOURCE", null, ::VResource, VResource::component)

        /**
         * Adds a resource that [block] sets up.
         *
         * @param block sets up the new resource.
         */
        public fun resourceComponent(block: VResource.Builder.() -> Unit): Builder =
            resourceComponent(VResource.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a resource of the to-do. */
        public fun resourceComponent(value: VResource): Builder = apply { resourceComponents.add(value) }

        /** Edits the resources of the to-do in [block]: add, replace or remove them. */
        public fun resourceComponents(block: ComponentList<VResource>.() -> Unit): Builder = apply {
            resourceComponents.block()
        }

        /**
         * Creates the to-do.
         *
         * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
         *   of the `RRULE` does not suit the type of `DTSTART`.
         */
        public fun build(): VTodo {
            requireIdentity()
            checkUntil()
            return VTodo(buildNode())
        }
    }

    /** Creates VTodo values and builders. */
    public companion object {
        /**
         * Wraps [component] as a VTodo, or returns `null` when it is not a `VTODO` component. Nothing is copied or
         * validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): VTodo? = if (named(component, "VTODO")) VTodo(component) else null

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

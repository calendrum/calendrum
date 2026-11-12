@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName

/**
 * A `VALARM` component: a reminder that fires relative to its parent event or to-do
 * ([RFC 5545 §3.6.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.6)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class VAlarm internal constructor(public val component: ICalComponent) {
    /**
     * The action of the alarm (`ACTION`, [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)),
     * or `null` when it is missing or does not decode.
     */
    public val action: AlarmAction? get() = component[Properties.ACTION]

    /**
     * The trigger of the alarm (`TRIGGER`,
     * [RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)), or `null` when it is missing or
     * does not decode.
     */
    public val trigger: Trigger? get() = component[Properties.TRIGGER]

    /**
     * The duration of the alarm (`DURATION`,
     * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
     * does not decode.
     */
    public val duration: ICalDuration? get() = component[Properties.DURATION]

    /**
     * The number of repetitions of the alarm (`REPEAT`,
     * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)), or `null` when it is missing or
     * does not decode.
     */
    public val repeat: Int? get() = component[Properties.REPEAT]

    /**
     * The description of the alarm (`DESCRIPTION`,
     * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
     * does not decode.
     */
    @ObjCName("descriptionText")
    public val description: String? get() = component[Properties.DESCRIPTION]

    /**
     * The summary, the short title of the alarm (`SUMMARY`,
     * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
     * does not decode.
     */
    public val summary: String? get() = component[Properties.SUMMARY]

    /**
     * The attendees of the alarm (`ATTENDEE` properties,
     * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attendees: List<Attendee> get() = component.all(Properties.ATTENDEE)

    /**
     * The attachments of the alarm (`ATTACH` properties,
     * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
     * decode are skipped.
     */
    public val attachments: List<Attach> get() = component.all(Properties.ATTACH)

    /**
     * The globally unique identifier of the alarm (`UID`,
     * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
     * does not decode.
     */
    public val uid: String? get() = component[Properties.UID]

    /**
     * The time the alarm was acknowledged of the alarm (`ACKNOWLEDGED`,
     * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)), or `null` when it is missing or does not
     * decode.
     */
    public val acknowledged: ICalDateTime? get() = component[Properties.ACKNOWLEDGED]

    /**
     * The proximity condition of the alarm (`PROXIMITY`,
     * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)), or `null` when it is missing or does not
     * decode.
     */
    public val proximity: Proximity? get() = component[Properties.PROXIMITY]

    /**
     * The relationships to other components of the alarm (`RELATED-TO` properties,
     * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val relatedTo: List<RelatedTo> get() = component.all(Properties.RELATED_TO)

    /**
     * The styled descriptions of the alarm (`STYLED-DESCRIPTION` properties,
     * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
     * skipped.
     */
    public val styledDescriptions: List<StyledDescription> get() = component.all(Properties.STYLED_DESCRIPTION)

    /**
     * The links of the alarm (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
     * in source order; ones that do not decode are skipped.
     */
    public val links: List<Link> get() = component.all(Properties.LINK)

    /**
     * The concepts of the alarm (`CONCEPT` properties,
     * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val concepts: List<String> get() = texts(component, Properties.CONCEPT)

    /**
     * The reference identifiers of the alarm (`REFID` properties,
     * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
     * skipped.
     */
    public val refIds: List<String> get() = texts(component, Properties.REFID)

    /** The locations nested in the alarm, in source order. */
    public val locations: List<VLocation> get() = children(component, "VLOCATION", ::VLocation)

    /**
     * Returns a copy of this alarm changed by [block]. Properties and components that [block] does not touch are the
     * same objects as in this alarm.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): VAlarm = toBuilder().apply(block).build()

    /** Returns a builder that starts from this alarm; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this alarm and takes generated `UID` and `DTSTAMP` values from [defaults]
     * where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a VAlarm with an equal VALARM component. */
    override fun equals(other: Any?): Boolean = this === other || (other is VAlarm && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("VAlarm", component)

    /**
     * Collects the properties and nested components of a new or edited alarm, with a Kotlin DSL and chained calls. Get
     * one from [VAlarm.builder] or [VAlarm.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("VALARM", original, defaults) {
        /**
         * The action of the alarm being built (`ACTION`,
         * [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)); assigning `null` removes the
         * property.
         */
        public var action: AlarmAction?
            get() = read(Properties.ACTION)
            set(value) {
                write(Properties.ACTION, value)
            }

        /**
         * Sets [action] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun action(value: AlarmAction?): Builder = apply { action = value }

        /**
         * The trigger of the alarm being built (`TRIGGER`,
         * [RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)); assigning `null` removes the
         * property.
         */
        public var trigger: Trigger?
            get() = read(Properties.TRIGGER)
            set(value) {
                write(Properties.TRIGGER, value)
            }

        /**
         * Sets [trigger] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun trigger(value: Trigger?): Builder = apply { trigger = value }

        /**
         * The duration of the alarm being built (`DURATION`,
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

        /**
         * The number of repetitions of the alarm being built (`REPEAT`,
         * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)); assigning `null` removes the
         * property.
         */
        public var repeat: Int?
            get() = read(Properties.REPEAT)
            set(value) {
                write(Properties.REPEAT, value)
            }

        /**
         * Sets [repeat] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun repeat(value: Int?): Builder = apply { repeat = value }

        /**
         * The description of the alarm being built (`DESCRIPTION`,
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
         * The summary, the short title of the alarm being built (`SUMMARY`,
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

        /**
         * Makes this a `DISPLAY` alarm: sets `ACTION`, [trigger] and the `DESCRIPTION` shown to the user.
         *
         * @param trigger when the alarm fires.
         * @param description the text to display.
         */
        public fun display(trigger: Trigger, description: String): Builder = apply {
            action = AlarmAction.Display
            this.trigger = trigger
            this.description = description
        }

        /**
         * Makes this an `AUDIO` alarm: sets `ACTION` and [trigger]; the platform chooses the sound unless you attach
         * one.
         *
         * @param trigger when the alarm fires.
         */
        public fun audio(trigger: Trigger): Builder = apply {
            action = AlarmAction.Audio
            this.trigger = trigger
        }

        /**
         * Makes this an `EMAIL` alarm: sets `ACTION`, [trigger], the `SUMMARY` that becomes the subject and the
         * `DESCRIPTION` that becomes the body.
         *
         * @param trigger when the alarm fires.
         * @param summary the subject of the mail.
         * @param description the body of the mail.
         */
        public fun email(trigger: Trigger, summary: String, description: String): Builder = apply {
            action = AlarmAction.Email
            this.trigger = trigger
            this.summary = summary
            this.description = description
        }

        /**
         * The globally unique identifier of the alarm being built (`UID`,
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
         * The time the alarm was acknowledged of the alarm being built (`ACKNOWLEDGED`,
         * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)); assigning `null` removes the property.
         */
        public var acknowledged: ICalDateTime?
            get() = read(Properties.ACKNOWLEDGED)
            set(value) {
                write(Properties.ACKNOWLEDGED, value)
            }

        /**
         * Sets [acknowledged] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun acknowledged(value: ICalDateTime?): Builder = apply { acknowledged = value }

        /**
         * The proximity condition of the alarm being built (`PROXIMITY`,
         * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)); assigning `null` removes the property.
         */
        public var proximity: Proximity?
            get() = read(Properties.PROXIMITY)
            set(value) {
                write(Properties.PROXIMITY, value)
            }

        /**
         * Sets [proximity] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun proximity(value: Proximity?): Builder = apply { proximity = value }

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

        /** Adds [value] as a `STYLED-DESCRIPTION` property. */
        public fun styledDescription(value: StyledDescription): Builder = property(Properties.STYLED_DESCRIPTION, value)

        /** Adds [value] as a `LINK` property. */
        public fun link(value: Link): Builder = property(Properties.LINK, value)

        /** Adds a `CONCEPT` property with the URI [uri]. */
        public fun concept(uri: String): Builder = property(Properties.CONCEPT, uri)

        /** Adds a `REFID` property with the text [text]. */
        public fun refId(text: String): Builder = property(Properties.REFID, text)

        /** The locations of the alarm being built, as an editable list. */
        public val locations: ComponentList<VLocation>
            get() = ComponentList(this, "VLOCATION", null, ::VLocation, VLocation::component)

        /**
         * Adds a location that [block] sets up.
         *
         * @param block sets up the new location.
         */
        public fun location(block: VLocation.Builder.() -> Unit): Builder =
            location(VLocation.Builder(null, defaults).apply(block).build())

        /** Adds [value] as a location of the alarm. */
        public fun location(value: VLocation): Builder = apply { locations.add(value) }

        /** Edits the locations of the alarm in [block]: add, replace or remove them. */
        public fun locations(block: ComponentList<VLocation>.() -> Unit): Builder = apply { locations.block() }

        /** Creates the alarm. */
        public fun build(): VAlarm = VAlarm(buildNode())
    }

    /** Creates VAlarm values and builders. */
    public companion object {
        /**
         * Wraps [component] as a VAlarm, or returns `null` when it is not a `VALARM` component. Nothing is copied or
         * validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): VAlarm? = if (named(component, "VALARM")) VAlarm(component) else null

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

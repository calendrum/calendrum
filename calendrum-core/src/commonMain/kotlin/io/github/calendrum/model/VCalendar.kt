@file:JvmName("VCalendars")

package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * A `VCALENDAR` component: the container of an iCalendar object, with its calendar properties and its events, to-dos,
 * journal entries, free/busy components and time zones
 * ([RFC 5545 §3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.4)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
public class VCalendar internal constructor(public val component: ICalComponent) {
    @Volatile
    private var eventList: List<VEvent>? = null

    @Volatile
    private var todoList: List<VTodo>? = null

    @Volatile
    private var journalList: List<VJournal>? = null

    @Volatile
    private var freeBusyList: List<VFreeBusy>? = null

    @Volatile
    private var timeZoneList: List<VTimeZone>? = null

    /**
     * The product identifier of the calendar (`PRODID`,
     * [RFC 5545 §3.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.3)), or `null` when it is missing or does
     * not decode.
     */
    public val prodId: String? get() = component[Properties.PRODID]

    /**
     * The iCalendar version of the calendar (`VERSION`,
     * [RFC 5545 §3.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.4)), or `null` when it is missing or does
     * not decode.
     */
    public val version: String? get() = component[Properties.VERSION]

    /**
     * The iTIP method of the calendar (`METHOD`,
     * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)), or `null` when it is missing or does
     * not decode.
     */
    public val method: Method? get() = component[Properties.METHOD]

    /**
     * The calendar scale of the calendar (`CALSCALE`,
     * [RFC 5545 §3.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.1)), or `null` when it is missing or does
     * not decode.
     */
    public val calScale: String? get() = component[Properties.CALSCALE]

    /** The events nested in the calendar, in source order. */
    public val events: List<VEvent>
        get() = eventList ?: children(component, "VEVENT", ::VEvent).also { eventList = it }

    /** The to-dos nested in the calendar, in source order. */
    public val todos: List<VTodo>
        get() = todoList ?: children(component, "VTODO", ::VTodo).also { todoList = it }

    /** The journal entries nested in the calendar, in source order. */
    public val journals: List<VJournal>
        get() = journalList ?: children(component, "VJOURNAL", ::VJournal).also { journalList = it }

    /** The free/busy component nested in the calendar, in source order. */
    public val freeBusy: List<VFreeBusy>
        get() = freeBusyList ?: children(component, "VFREEBUSY", ::VFreeBusy).also { freeBusyList = it }

    /** The time zones nested in the calendar, in source order. */
    public val timeZones: List<VTimeZone>
        get() = timeZoneList ?: children(component, "VTIMEZONE", ::VTimeZone).also { timeZoneList = it }

    /**
     * The globally unique identifier of the calendar (`UID`,
     * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
     * does not decode.
     */
    public val uid: String? get() = component[Properties.UID]

    /**
     * The URL of the calendar (`URL`, [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or
     * `null` when it is missing or does not decode.
     */
    public val url: String? get() = component[Properties.URL]

    /**
     * The time of the last modification of the calendar (`LAST-MODIFIED`,
     * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
     * does not decode.
     */
    public val lastModified: ICalDateTime? get() = component[Properties.LAST_MODIFIED]

    /**
     * The suggested refresh interval of the calendar (`REFRESH-INTERVAL`,
     * [RFC 7986 §5.7](https://www.rfc-editor.org/rfc/rfc7986#section-5.7)), or `null` when it is missing or does not
     * decode.
     */
    public val refreshInterval: ICalDuration? get() = component[Properties.REFRESH_INTERVAL]

    /**
     * The source URI of the calendar (`SOURCE`, [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)),
     * or `null` when it is missing or does not decode.
     */
    public val source: String? get() = component[Properties.SOURCE]

    /**
     * The color of the calendar (`COLOR`, [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), or
     * `null` when it is missing or does not decode.
     */
    public val color: String? get() = component[Properties.COLOR]

    /**
     * The names of the calendar (`NAME` properties,
     * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)) in source order; ones that do not decode are
     * skipped.
     */
    public val names: List<String> get() = texts(component, Properties.NAME)

    /**
     * The descriptions of the calendar (`DESCRIPTION` properties,
     * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)) in source order; ones that do not
     * decode are skipped.
     */
    public val descriptions: List<String> get() = texts(component, Properties.DESCRIPTION)

    /**
     * The categories of the calendar (`CATEGORIES` properties,
     * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
     * ones that do not decode are skipped.
     */
    public val categories: List<String> get() = categories(component)

    /**
     * The images of the calendar (`IMAGE` properties,
     * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
     * are skipped.
     */
    public val images: List<Image> get() = component.all(Properties.IMAGE)

    /**
     * Returns a copy of this calendar changed by [block]. Properties and components that [block] does not touch are the
     * same objects as in this calendar.
     *
     * @param block changes the builder.
     */
    public fun edit(block: Builder.() -> Unit): VCalendar = toBuilder().apply(block).build()

    /** Returns a builder that starts from this calendar; unchanged nodes are shared with it. */
    public fun toBuilder(): Builder = Builder(component, null)

    /**
     * Returns a builder that starts from this calendar and takes generated `UID` and `DTSTAMP` values from [defaults]
     * where it creates components.
     *
     * @param defaults the clock and the `UID` generator.
     */
    public fun toBuilder(defaults: BuildDefaults): Builder = Builder(component, defaults)

    /** Returns `true` when [other] is a VCalendar with an equal VCALENDAR component. */
    override fun equals(other: Any?): Boolean = this === other || (other is VCalendar && component == other.component)

    /** Returns the hash code of the component. */
    override fun hashCode(): Int = component.hashCode()

    /** Returns the type and the number of properties and components; never a value. */
    override fun toString(): String = describeWrapper("VCalendar", component)

    /**
     * Collects the properties and nested components of a new or edited calendar, with a Kotlin DSL and chained calls.
     * Get one from [VCalendar.builder] or [VCalendar.toBuilder]. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(original: ICalComponent?, defaults: BuildDefaults?) :
        ComponentBuilder<Builder>("VCALENDAR", original, defaults) {
        /**
         * The product identifier of the calendar being built (`PRODID`,
         * [RFC 5545 §3.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.3)); assigning `null` removes the
         * property.
         */
        public var prodId: String?
            get() = read(Properties.PRODID)
            set(value) {
                write(Properties.PRODID, value)
            }

        /**
         * Sets [prodId] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun prodId(value: String?): Builder = apply { prodId = value }

        /**
         * The iCalendar version of the calendar being built (`VERSION`,
         * [RFC 5545 §3.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.4)); assigning `null` removes the
         * property.
         */
        public var version: String?
            get() = read(Properties.VERSION)
            set(value) {
                write(Properties.VERSION, value)
            }

        /**
         * Sets [version] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun version(value: String?): Builder = apply { version = value }

        /**
         * The iTIP method of the calendar being built (`METHOD`,
         * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)); assigning `null` removes the
         * property.
         */
        public var method: Method?
            get() = read(Properties.METHOD)
            set(value) {
                write(Properties.METHOD, value)
            }

        /**
         * Sets [method] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun method(value: Method?): Builder = apply { method = value }

        /**
         * The calendar scale of the calendar being built (`CALSCALE`,
         * [RFC 5545 §3.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.1)); assigning `null` removes the
         * property.
         */
        public var calScale: String?
            get() = read(Properties.CALSCALE)
            set(value) {
                write(Properties.CALSCALE, value)
            }

        /**
         * Sets [calScale] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun calScale(value: String?): Builder = apply { calScale = value }

        /** The events of the calendar being built, as an editable list. */
        public val events: ComponentList<VEvent> get() = ComponentList(
            this,
            "VEVENT",
            null,
            ::VEvent,
            VEvent::component,
        )

        /**
         * Adds a event that [block] sets up.
         *
         * @param block sets up the new event.
         */
        public fun event(block: VEvent.Builder.() -> Unit): Builder =
            event(VEvent.Builder(null, defaults).apply(block).build())

        /** Adds [event] as a event of the calendar. */
        public fun event(event: VEvent): Builder = apply { events.add(event) }

        /** Edits the events of the calendar in [block]: add, replace or remove them. */
        public fun events(block: ComponentList<VEvent>.() -> Unit): Builder = apply { events.block() }

        /** The to-dos of the calendar being built, as an editable list. */
        public val todos: ComponentList<VTodo> get() = ComponentList(this, "VTODO", null, ::VTodo, VTodo::component)

        /**
         * Adds a to-do that [block] sets up.
         *
         * @param block sets up the new to-do.
         */
        public fun todo(block: VTodo.Builder.() -> Unit): Builder =
            todo(VTodo.Builder(null, defaults).apply(block).build())

        /** Adds [todo] as a to-do of the calendar. */
        public fun todo(todo: VTodo): Builder = apply { todos.add(todo) }

        /** Edits the to-dos of the calendar in [block]: add, replace or remove them. */
        public fun todos(block: ComponentList<VTodo>.() -> Unit): Builder = apply { todos.block() }

        /** The journal entries of the calendar being built, as an editable list. */
        public val journals: ComponentList<VJournal> get() = ComponentList(
            this,
            "VJOURNAL",
            null,
            ::VJournal,
            VJournal::component,
        )

        /**
         * Adds a journal entry that [block] sets up.
         *
         * @param block sets up the new journal entry.
         */
        public fun journal(block: VJournal.Builder.() -> Unit): Builder =
            journal(VJournal.Builder(null, defaults).apply(block).build())

        /** Adds [journal] as a journal entry of the calendar. */
        public fun journal(journal: VJournal): Builder = apply { journals.add(journal) }

        /** Edits the journal entries of the calendar in [block]: add, replace or remove them. */
        public fun journals(block: ComponentList<VJournal>.() -> Unit): Builder = apply { journals.block() }

        /** The time zones of the calendar being built, as an editable list. */
        public val timeZones: ComponentList<VTimeZone> get() = ComponentList(
            this,
            "VTIMEZONE",
            null,
            ::VTimeZone,
            VTimeZone::component,
        )

        /**
         * Adds a time zone that [block] sets up.
         *
         * @param block sets up the new time zone.
         */
        public fun timeZone(block: VTimeZone.Builder.() -> Unit): Builder =
            timeZone(VTimeZone.Builder(null, defaults).apply(block).build())

        /** Adds [timeZone] as a time zone of the calendar. */
        public fun timeZone(timeZone: VTimeZone): Builder = apply { timeZones.add(timeZone) }

        /** Edits the time zones of the calendar in [block]: add, replace or remove them. */
        public fun timeZones(block: ComponentList<VTimeZone>.() -> Unit): Builder = apply { timeZones.block() }

        /** The free/busy components of the calendar being built, as an editable list. */
        public val freeBusies: ComponentList<VFreeBusy> get() = ComponentList(
            this,
            "VFREEBUSY",
            null,
            ::VFreeBusy,
            VFreeBusy::component,
        )

        /**
         * Adds a free/busy component that [block] sets up.
         *
         * @param block sets up the new free/busy component.
         */
        public fun freeBusy(block: VFreeBusy.Builder.() -> Unit): Builder =
            freeBusy(VFreeBusy.Builder(null, defaults).apply(block).build())

        /** Adds [freeBusy] as a free/busy component of the calendar. */
        public fun freeBusy(freeBusy: VFreeBusy): Builder = apply { freeBusies.add(freeBusy) }

        /** Edits the free/busy components of the calendar in [block]: add, replace or remove them. */
        public fun freeBusies(block: ComponentList<VFreeBusy>.() -> Unit): Builder = apply { freeBusies.block() }

        /**
         * The globally unique identifier of the calendar being built (`UID`,
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
         * The URL of the calendar being built (`URL`,
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
         * The time of the last modification of the calendar being built (`LAST-MODIFIED`,
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
         * The suggested refresh interval of the calendar being built (`REFRESH-INTERVAL`,
         * [RFC 7986 §5.7](https://www.rfc-editor.org/rfc/rfc7986#section-5.7)); assigning `null` removes the property.
         */
        public var refreshInterval: ICalDuration?
            get() = read(Properties.REFRESH_INTERVAL)
            set(value) {
                write(Properties.REFRESH_INTERVAL, value)
            }

        /**
         * Sets [refreshInterval] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun refreshInterval(value: ICalDuration?): Builder = apply { refreshInterval = value }

        /**
         * The source URI of the calendar being built (`SOURCE`,
         * [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)); assigning `null` removes the property.
         */
        public var source: String?
            get() = read(Properties.SOURCE)
            set(value) {
                write(Properties.SOURCE, value)
            }

        /**
         * Sets [source] to [value] and returns this builder; `null` removes the property.
         *
         * @param value the new value, or `null`.
         */
        public fun source(value: String?): Builder = apply { source = value }

        /**
         * The color of the calendar being built (`COLOR`,
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

        /** Adds a `NAME` property with the text [text]. */
        public fun name(text: String): Builder = property(Properties.NAME, text)

        /** Adds a `DESCRIPTION` property with the text [text]. */
        public fun description(text: String): Builder = property(Properties.DESCRIPTION, text)

        /** Adds a `CATEGORIES` property with the given [values]. */
        public fun categories(vararg values: String): Builder =
            property(Properties.CATEGORIES, Categories(values.asList()))

        /** Adds [value] as an `IMAGE` property. */
        public fun image(value: Image): Builder = property(Properties.IMAGE, value)

        /** Creates the calendar. */
        public fun build(): VCalendar = VCalendar(buildNode())
    }

    /** Creates VCalendar values and builders. */
    public companion object {
        /**
         * Wraps [component] as a VCalendar, or returns `null` when it is not a `VCALENDAR` component. Nothing is copied
         * or validated.
         *
         * @param component the component to wrap.
         */
        @JvmStatic
        public fun of(component: ICalComponent): VCalendar? =
            if (named(component, "VCALENDAR")) VCalendar(component) else null

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

/**
 * Builds a calendar with the given `PRODID` and `VERSION:2.0` in a Kotlin DSL; the builder in [block] adds events,
 * to-dos and the rest.
 *
 * `UID` and `DTSTAMP` of every component must be set in [block], since this overload has no defaults.
 *
 * @param prodId the product identifier, such as `-//Example//Planner 1.0//EN`.
 * @param block adds the properties and components.
 * @throws IllegalStateException when a component is built without `UID` or `DTSTAMP`.
 * @sample io.github.calendrum.samples.buildCalendar
 */
public fun VCalendar(prodId: String, block: VCalendar.Builder.() -> Unit): VCalendar =
    VCalendar.builder().prodId(prodId).version(VERSION_2_0).apply(block).build()

/**
 * Builds a calendar like the shorter overload, but takes the `UID` and `DTSTAMP` of components that lack them from
 * [defaults].
 *
 * There is no global clock or `UID` generator: they are always passed in.
 *
 * @param prodId the product identifier, such as `-//Example//Planner 1.0//EN`.
 * @param defaults the clock and the generator of `UID` values.
 * @param block adds the properties and components.
 * @sample io.github.calendrum.samples.buildCalendar
 */
public fun VCalendar(prodId: String, defaults: BuildDefaults, block: VCalendar.Builder.() -> Unit): VCalendar =
    VCalendar.builder(defaults).prodId(prodId).version(VERSION_2_0).apply(block).build()

private const val VERSION_2_0 = "2.0"

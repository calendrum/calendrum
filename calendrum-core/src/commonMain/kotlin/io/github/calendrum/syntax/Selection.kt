@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.ICalDsl
import io.github.calendrum.InternalICalApi
import io.github.calendrum.SortedStringMap
import io.github.calendrum.model.PropertyKey
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import kotlin.native.HiddenFromObjC

/** Which `VTIMEZONE` components a selective parse keeps. */
public enum class TimeZoneSelection {
    /** Every `VTIMEZONE` of the input. */
    All,

    /** Only the `VTIMEZONE` components whose `TZID` is used by a parameter of a kept component. */
    Referenced,

    /** No `VTIMEZONE` at all. */
    None,
}

/**
 * A named group of properties and components that a selection needs for some purpose, so that it need not list them one
 * by one.
 */
public class Feature private constructor(private val name: String, internal val kind: Int) {
    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = name

    /** The features of [Selection.Builder.need]. */
    public companion object {
        /**
         * Everything needed to compute occurrences: `DTSTART`, `DTEND`, `DURATION`, `RRULE`, `RDATE`, `EXDATE` and
         * `RECURRENCE-ID`. Time zones are then at least [TimeZoneSelection.Referenced].
         */
        @JvmField
        public val Occurrences: Feature = Feature("Occurrences", OCCURRENCES)

        /** The `VALARM` components, whole. */
        @JvmField
        public val Alarms: Feature = Feature("Alarms", ALARMS)

        /** The `ATTENDEE` and `ORGANIZER` properties. */
        @JvmField
        public val Participants: Feature = Feature("Participants", PARTICIPANTS)
    }
}

/**
 * Tells the lexer which components and properties to read; everything else is skipped by bytes, without being parsed.
 *
 * A selection is the way to pay only for what you use: lines of unwanted properties and whole unwanted components are
 * skipped to the end of their logical line, so they cost almost nothing and produce no problems. `BEGIN` and `END`
 * lines are always read. The resulting tree is marked partial, and writing it throws unless
 * `WriteOptions.allowPartialWrite` is set. Use a preset ([Full], [Schedule], [SyncMetadata], [FreeBusy]) or [builder].
 *
 * @property timeZones which `VTIMEZONE` components are kept.
 */
public class Selection internal constructor(
    internal val content: ContentSelection,
    public val timeZones: TimeZoneSelection,
    private val label: String?,
    private val componentCount: Int,
    private val propertyCount: Int,
) {
    internal val isFull: Boolean get() = content.isFull

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = if (label != null) {
        describe("Selection", "preset", label)
    } else {
        describe("Selection", "components", componentCount, "properties", propertyCount, "timeZones", timeZones)
    }

    /**
     * Collects what a [Selection] reads. The properties and the functions of the same name do the same. Not
     * thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor() {
        private val components = LinkedHashSet<String>()
        private val properties = LinkedHashSet<String>()
        private val whole = LinkedHashSet<String>()
        private var everyComponent = false
        private var occurrences = false

        /** Which `VTIMEZONE` components to keep; [TimeZoneSelection.All] by default. */
        public var timeZones: TimeZoneSelection = TimeZoneSelection.All

        /**
         * Adds components by name. Names of components that the standard defines and unknown `X-` names are accepted;
         * time zone components are chosen with [timeZones] instead.
         *
         * @throws IllegalArgumentException if a name is invalid, is not a component name, or names a time zone
         *   component.
         */
        public fun components(vararg names: String): Builder {
            for (name in names) {
                val canonical = checkedName(name, "component name").uppercaseAscii()
                require(canonical != VTIMEZONE && canonical != STANDARD && canonical != DAYLIGHT) {
                    "Time zones are selected with timeZones"
                }
                val id = KnownNames.find(canonical)
                require(id < 0 || KnownNames.kinds(id) and KnownNames.COMPONENT != 0) {
                    "Not a component name: $canonical"
                }
                components += canonical
            }
            return this
        }

        /**
         * Adds properties by name, which are read in every selected component. Unknown names are matched exactly
         * (ignoring ASCII case).
         *
         * @throws IllegalArgumentException if a name is invalid, is not a property name, or is `BEGIN` or `END`.
         */
        public fun properties(vararg names: String): Builder {
            for (name in names) addProperty(checkedPropertyName(name).uppercaseAscii())
            return this
        }

        /** Adds the properties that the typed [keys] stand for. */
        public fun properties(vararg keys: PropertyKey<*>): Builder {
            for (key in keys) addProperty(key.name)
            return this
        }

        /** Adds what the [features] need. */
        public fun need(vararg features: Feature): Builder {
            for (feature in features) {
                when (feature.kind) {
                    OCCURRENCES -> {
                        properties += "DTSTART"
                        properties += "DTEND"
                        properties += "DURATION"
                        properties += "RRULE"
                        properties += "RDATE"
                        properties += "EXDATE"
                        properties += "RECURRENCE-ID"
                        occurrences = true
                    }

                    ALARMS -> {
                        components += VALARM
                        whole += VALARM
                    }

                    else -> {
                        properties += ATTENDEE
                        properties += ORGANIZER
                    }
                }
            }
            return this
        }

        /** Sets [timeZones]. */
        public fun timeZones(selection: TimeZoneSelection): Builder {
            timeZones = selection
            return this
        }

        /** Creates the selection. */
        public fun build(): Selection = build(null)

        internal fun everyComponent(): Builder {
            everyComponent = true
            return this
        }

        internal fun whole(name: String): Builder {
            components += name
            whole += name
            return this
        }

        internal fun build(label: String?): Selection {
            val zones = if (occurrences &&
                timeZones == TimeZoneSelection.None
            ) {
                TimeZoneSelection.Referenced
            } else {
                timeZones
            }
            val size = ContentSelection.maskSize()
            val componentMask = LongArray(size)
            val propertyMask = LongArray(size)
            val wholeMask = LongArray(size)
            val componentNames = ArrayList<String>()
            val propertyNames = ArrayList<String>()
            for (name in components) {
                val id = KnownNames.find(name)
                if (id < 0) componentNames += name else ContentSelection.set(componentMask, id)
            }
            if (everyComponent) {
                for (id in 0 until KnownNames.count) {
                    if (KnownNames.kinds(id) and KnownNames.COMPONENT != 0 && !isTimeZoneComponent(id)) {
                        ContentSelection.set(componentMask, id)
                    }
                }
            }
            if (zones != TimeZoneSelection.None) {
                ContentSelection.set(componentMask, KnownNames.VTIMEZONE)
                ContentSelection.set(componentMask, KnownNames.find(STANDARD))
                ContentSelection.set(componentMask, KnownNames.find(DAYLIGHT))
                ContentSelection.set(wholeMask, KnownNames.VTIMEZONE)
            }
            for (name in whole) ContentSelection.set(wholeMask, KnownNames.find(name))
            for (name in properties) {
                val id = KnownNames.find(name)
                if (id < 0 || KnownNames.kinds(id) and KnownNames.PROPERTY == 0) {
                    propertyNames += name
                } else {
                    ContentSelection.set(propertyMask, id)
                }
            }
            val content = ContentSelection.create(
                componentMask,
                propertyMask,
                everyComponent || componentNames.isNotEmpty(),
                propertyNames.isNotEmpty(),
                wholeMask,
                if (everyComponent || componentNames.isEmpty()) null else componentNames.toTypedArray(),
                if (propertyNames.isEmpty()) null else propertyNames.toTypedArray(),
            )
            return Selection(content, zones, label, components.size, properties.size)
        }

        private fun addProperty(name: String) {
            require(name != BEGIN && name != END) { "BEGIN and END are always read" }
            val id = KnownNames.find(name)
            require(id < 0 || KnownNames.kinds(id) and KnownNames.PROPERTY != 0) { "Not a property name: $name" }
            properties += name
        }

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String =
            describe("Selection.Builder", "components", components.size, "properties", properties.size)
    }

    /** The presets and the builder of selections. */
    public companion object {
        /** Reads everything. This is the default of a parser. */
        @JvmField
        public val Full: Selection = Selection(ContentSelection.Full, TimeZoneSelection.All, "Full", 0, 0)

        /**
         * A calendar view of events: `VEVENT` with `UID`, `SUMMARY`, `DESCRIPTION`, `LOCATION`, `STATUS`, `SEQUENCE`,
         * `CREATED`, `LAST-MODIFIED`, the occurrence properties and the participants, `X-WR-TIMEZONE` of the calendar,
         * and the time zones the events use.
         */
        @JvmField
        public val Schedule: Selection = Builder()
            .components(VEVENT)
            .properties("UID", "SUMMARY", "DESCRIPTION", "LOCATION", STATUS, "SEQUENCE", "CREATED", "LAST-MODIFIED")
            .properties("X-WR-TIMEZONE")
            .need(Feature.Occurrences, Feature.Participants)
            .timeZones(TimeZoneSelection.Referenced)
            .build("Schedule")

        /**
         * What a sync needs to tell versions apart: every component with `UID`, `SEQUENCE`, `LAST-MODIFIED`, `DTSTAMP`
         * and `RECURRENCE-ID`, and no time zones.
         */
        @JvmField
        public val SyncMetadata: Selection = Builder()
            .everyComponent()
            .properties("UID", "SEQUENCE", "LAST-MODIFIED", "DTSTAMP", "RECURRENCE-ID")
            .timeZones(TimeZoneSelection.None)
            .build("SyncMetadata")

        /**
         * What a free/busy view needs: `VFREEBUSY` whole, and `VEVENT` with `UID`, `TRANSP`, `STATUS` and the
         * occurrence properties, with the time zones they use.
         */
        @JvmField
        public val FreeBusy: Selection = Builder()
            .whole(VFREEBUSY)
            .components(VEVENT)
            .properties("UID", TRANSP, STATUS)
            .need(Feature.Occurrences)
            .timeZones(TimeZoneSelection.Referenced)
            .build("FreeBusy")

        /** Returns an empty builder. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

// The selection applied to a full tree, for tests: equal to the selective parse of the same input.

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun filterSelection(components: List<ICalComponent>, selection: Selection): List<ICalComponent> {
    val result = ArrayList<ICalComponent>()
    for (component in components) {
        val kept = keep(component, selection.content, false)
        if (kept != null) result += kept
    }
    if (selection.timeZones != TimeZoneSelection.Referenced) return result
    return pruneTimeZones(result.toTypedArray()).asList()
}

private fun keep(component: ICalComponent, content: ContentSelection, inWhole: Boolean): ICalComponent? {
    val id = component.nameId
    if (!inWhole && id != KnownNames.VCALENDAR && !content.wantsComponent(id) { component.name }) return null
    val whole = inWhole || content.isWhole(id)
    val properties = ArrayList<ICalProperty>()
    for (property in component.propertyNodes) {
        if (whole || content.wantsProperty(property.nameId) { property.name }) properties += property
    }
    val children = ArrayList<ICalComponent>()
    for (child in component.componentNodes) {
        val kept = keep(child, content, whole)
        if (kept != null) children += kept
    }
    return newComponent(component.name, properties.toTypedArray(), children.toTypedArray())
}

// `Referenced`: after the tree is built, a calendar keeps only the VTIMEZONE whose TZID appears in a TZID parameter of
// its other components. Streams and the cursor cannot look ahead and keep all.
internal fun pruneTimeZones(components: Array<ICalComponent>): Array<ICalComponent> {
    for (index in components.indices) {
        val calendar = components[index]
        if (calendar.nameId == KnownNames.VCALENDAR) components[index] = pruneCalendar(calendar)
    }
    return components
}

// Once every VTIMEZONE of the calendar is referenced, all of them are kept and the walk over the remaining components
// stops; a VTIMEZONE without TZID is never kept, so it turns the early return off. Coverage is a count of the defined
// TZIDs met so far, not `containsAll` after each component (quadratic with thousands of zones and referenced TZIDs).
// TZIDs come from the input: kept in trees, not hashed (see InputKeys.kt).
private fun pruneCalendar(calendar: ICalComponent): ICalComponent {
    val children = calendar.componentNodes
    var zones = 0
    for (child in children) if (child.nameId == KnownNames.VTIMEZONE) zones += 1
    if (zones == 0) return calendar
    val defined = SortedStringMap<Unit>()
    var everyZoneNamed = true
    for (child in children) {
        if (child.nameId != KnownNames.VTIMEZONE) continue
        val tzid = child.property(TZID)
        if (tzid == null) everyZoneNamed = false else defined[tzid.value] = Unit
    }
    val referenced = SortedStringMap<Unit>()
    val found = ArrayList<String>()
    var covered = 0
    for (child in children) {
        if (child.nameId == KnownNames.VTIMEZONE) continue
        found.clear()
        collectZones(child, found)
        for (tzid in found) {
            if (referenced.containsKey(tzid)) continue
            referenced[tzid] = Unit
            if (defined.containsKey(tzid)) covered++
        }
        if (everyZoneNamed && covered == defined.size) return calendar
    }
    var kept = 0
    val keep = BooleanArray(children.size)
    for (index in children.indices) {
        val child = children[index]
        keep[index] = child.nameId != KnownNames.VTIMEZONE ||
            child.property(TZID)?.value?.let { referenced.containsKey(it) } == true
        if (keep[index]) kept += 1
    }
    if (kept == children.size) return calendar
    val result = arrayOfNulls<ICalComponent>(kept)
    val positions = calendar.childPositions
    val newPositions = if (positions == null) null else IntArray(kept)
    var at = 0
    for (index in children.indices) {
        if (!keep[index]) continue
        if (newPositions != null) newPositions[at] = positions!![index]
        result[at++] = children[index]
    }
    @Suppress("UNCHECKED_CAST")
    return calendar.withChildren(result as Array<ICalComponent>, newPositions)
}

private fun collectZones(component: ICalComponent, referenced: MutableCollection<String>) {
    val tzid = KnownNames.find(TZID)
    var stack = arrayOfNulls<ICalComponent>(INITIAL_STACK)
    stack[0] = component
    var top = 1
    while (top > 0) {
        val node = stack[--top]!!
        for (property in node.propertyNodes) {
            if (property.parameterCount != 0) property.collectParameterValues(tzid, referenced)
        }
        val nested = node.componentNodes
        if (top + nested.size > stack.size) stack = stack.copyOf(maxOf(stack.size * 2, top + nested.size))
        for (child in nested) stack[top++] = child
    }
}

private fun String.uppercaseAscii(): String = canonicalName(this)

private fun isTimeZoneComponent(id: Int): Boolean =
    id == KnownNames.VTIMEZONE || id == KnownNames.find(STANDARD) || id == KnownNames.find(DAYLIGHT)

private const val OCCURRENCES = 0
private const val ALARMS = 1
private const val PARTICIPANTS = 2
private const val VTIMEZONE = "VTIMEZONE"
private const val STANDARD = "STANDARD"
private const val DAYLIGHT = "DAYLIGHT"
private const val VALARM = "VALARM"
private const val VEVENT = "VEVENT"
private const val VFREEBUSY = "VFREEBUSY"
private const val ATTENDEE = "ATTENDEE"
private const val ORGANIZER = "ORGANIZER"
private const val TRANSP = "TRANSP"
private const val STATUS = "STATUS"
private const val TZID = "TZID"
private const val BEGIN = "BEGIN"
private const val END = "END"
private const val INITIAL_STACK = 8

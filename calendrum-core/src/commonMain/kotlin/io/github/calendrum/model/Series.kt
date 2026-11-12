package io.github.calendrum.model

import io.github.calendrum.SortedStringMap
import io.github.calendrum.groupsOf
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.expand
import io.github.calendrum.recur.localKey
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.describe
import io.github.calendrum.value.ICalDateTime
import kotlin.jvm.JvmStatic

/**
 * A recurring event as stored in a calendar: the master with the `RRULE` and the overrides with a `RECURRENCE-ID`, all
 * sharing one `UID`.
 *
 * Get one with [Series.find] and pass it to the editing functions of [Series]. Immutable.
 *
 * @property calendar the calendar the series is in.
 * @property uid the `UID` the components share.
 * @property master the component with the recurrence rule, or `null` when only overrides are present.
 * @property overrides the components that override single instances, in calendar order.
 */
public class SeriesView internal constructor(
    public val calendar: VCalendar,
    public val uid: String,
    public val master: VEvent?,
    public val overrides: List<VEvent>,
) {
    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("SeriesView", "master", master != null, "overrides", overrides.size)
}

/** How to change "this and all following" instances of a series. */
public enum class ThisAndFuture {
    /**
     * Splits the series in two: the original ends before the instance and a new series with a new `UID`, related to the
     * old one with `RELATED-TO`, takes over from it. This is the widely supported way.
     */
    Split,

    /**
     * Keeps one series and adds an override with `RANGE=THISANDFUTURE`, which
     * [RFC 5545](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4) allows but many clients ignore.
     */
    Range,
}

/**
 * Operations on recurring events: splitting and merging calendars by `UID`, and changing a whole series, one instance
 * or "this and the following" instances, the way a calendar application does.
 *
 * All functions are pure: they return a new calendar and leave the one they are given as it is.
 */
public object Series {
    /**
     * Splits [calendar] into one calendar per `UID`, each with the time zones it needs. Components without a `UID` get
     * a calendar each.
     */
    @JvmStatic
    public fun splitByUid(calendar: VCalendar): List<VCalendar> {
        // UIDs and TZIDs come from the input: grouped by sorting and looked up in trees, not hashed (see InputKeys.kt).
        val children = calendar.component.components.filter { it.name != "VTIMEZONE" }
        val keys = children.mapIndexed { index, child -> child.uid()?.let { "\u0001$it" } ?: "\u0000$index" }
        val zones = ZoneIndex(calendar.component)
        val shell = withoutChildren(calendar.component)
        return groupsOf(children.size) { keys[it] }.map { positions ->
            VCalendar(calendarWith(calendar.component, positions.map { children[it] }, zones, shell))
        }
    }

    /**
     * Merges [calendars] into one: components are matched by name, `UID` and `RECURRENCE-ID`, and the one with the
     * newer revision (`SEQUENCE`, then `DTSTAMP`) wins; time zones are matched by `TZID`. The result takes the calendar
     * properties of the first.
     *
     * @param calendars the calendars to merge; at least one.
     * @throws IllegalArgumentException if no calendar is given.
     */
    @JvmStatic
    public fun merge(vararg calendars: VCalendar): VCalendar {
        require(calendars.isNotEmpty()) { "Nothing to merge" }
        val zones = ArrayList<ICalComponent>()
        val items = ArrayList<ICalComponent>()
        val zoneIndex = SortedStringMap<Int>()
        val itemIndex = SortedStringMap<Int>()
        for (calendar in calendars) {
            for (child in calendar.component.components) {
                if (child.name == "VTIMEZONE") {
                    val tzid = child.property("TZID")?.value ?: continue
                    if (!zoneIndex.containsKey(tzid)) {
                        zoneIndex[tzid] = zones.size
                        zones += child
                    }
                    continue
                }
                val uid = child.uid()
                if (uid == null) {
                    items += child
                    continue
                }
                val key = "${child.name}\u0000$uid\u0000${child.recurrenceId()}"
                val at = itemIndex[key]
                if (at == null) {
                    itemIndex[key] = items.size
                    items += child
                } else if (!isOlder(child, items[at])) {
                    items[at] = child
                }
            }
        }
        return VCalendar(calendars[0].component.edit { children(zones + items) })
    }

    /** Returns the series of events with the given [uid] in [calendar], or `null` when there is none. */
    @JvmStatic
    public fun find(calendar: VCalendar, uid: String): SeriesView? {
        val events = calendar.component.components.filter { it.name == "VEVENT" && it.uid() == uid }
        if (events.isEmpty()) return null
        val master = events.firstOrNull { it.recurrenceId() == null }
        return SeriesView(calendar, uid, master?.let(::VEvent), events.filter { it !== master }.map(::VEvent))
    }

    /**
     * Changes one instance of the series: edits its override, which is created from the master when it does not exist.
     *
     * @param series the series to change.
     * @param recurrenceId the start of the instance, as the master produces it.
     * @param edit changes the override.
     * @throws IllegalArgumentException if the series has no master and no such override.
     */
    @JvmStatic
    public fun editThis(series: SeriesView, recurrenceId: ICalDateTime, edit: VEvent.Builder.() -> Unit): VCalendar {
        val override = instanceOf(series, recurrenceId)
        val edited = VEvent(override ?: newOverride(series, recurrenceId)).edit(edit).component
        return replace(series, listOfNotNull(override), listOf(edited))
    }

    // The default split ends the old rule with COUNT (exact without time zones) and starts a new series at the
    // RECURRENCE-ID with a UID derived from the old one (UUIDv5), RELATED-TO the old UID; later dates and overrides
    // move to it. Editing from the first instance edits the whole series.

    /**
     * Changes the given instance and all following ones by splitting the series ([ThisAndFuture.Split]).
     *
     * @param series the series to change.
     * @param recurrenceId the start of the first instance to change.
     * @param edit changes the new series.
     * @throws IllegalArgumentException if the series has no master.
     */
    @JvmStatic
    public fun editThisAndFuture(
        series: SeriesView,
        recurrenceId: ICalDateTime,
        edit: VEvent.Builder.() -> Unit,
    ): VCalendar = editThisAndFuture(series, recurrenceId, ThisAndFuture.Split, edit)

    /**
     * Changes the given instance and all following ones in the way chosen by [mode].
     *
     * @param series the series to change.
     * @param recurrenceId the start of the first instance to change.
     * @param mode splits the series or adds an override with `RANGE=THISANDFUTURE`.
     * @param edit changes the instances.
     * @throws IllegalArgumentException if the series has no master.
     */
    @JvmStatic
    public fun editThisAndFuture(
        series: SeriesView,
        recurrenceId: ICalDateTime,
        mode: ThisAndFuture,
        edit: VEvent.Builder.() -> Unit,
    ): VCalendar {
        if (mode == ThisAndFuture.Range) {
            val override = instanceOf(series, recurrenceId)
            val ranged = (override ?: newOverride(series, recurrenceId)).edit {
                set(Properties.RECURRENCE_ID, RecurrenceId(recurrenceId, RecurrenceRange.ThisAndFuture))
            }
            return replace(series, listOfNotNull(override), listOf(VEvent(ranged).edit(edit).component))
        }
        val master = requireMaster(series)
        val before = instancesBefore(master, recurrenceId)
        if (before == 0) return editAll(series, edit)
        val newUid = Canonical.uidV5(series.uid, recurrenceId.toString())
        val head = truncated(master, recurrenceId)
        val tail = master.edit {
            set(Properties.UID, newUid)
            val start = master[Properties.DTSTART]
            set(Properties.DTSTART, recurrenceId)
            val end = master[Properties.DTEND]
            if (start != null && end != null) set(Properties.DTEND, shifted(end, start, recurrenceId))
            remove(Properties.SEQUENCE)
            val count = master[Properties.RRULE]?.count
            if (count != null) rewriteRule(this, master, count - (ruleInstancesBefore(master, recurrenceId) ?: 0))
            keepDates(this, master, recurrenceId, future = true)
            property(Properties.RELATED_TO, RelatedTo(series.uid))
        }
        val future = series.overrides.map { it.component }.filter { !isBefore(it.recurrenceId(), recurrenceId) }
        val moved = future.map { it.edit { set(Properties.UID, newUid) } }
        val (newMaster, newOverrides) = editedMaster(tail, moved, edit)
        return replace(series, listOf(master) + future, listOf(head, newMaster) + newOverrides)
    }

    /**
     * Changes every instance: edits the master, keeping the overrides.
     *
     * @param series the series to change.
     * @param edit changes the master.
     * @throws IllegalArgumentException if the series has no master.
     */
    @JvmStatic
    public fun editAll(series: SeriesView, edit: VEvent.Builder.() -> Unit): VCalendar {
        val master = requireMaster(series)
        val overrides = series.overrides.map { it.component }
        val (newMaster, newOverrides) = editedMaster(master, overrides, edit)
        return replace(series, listOf(master) + overrides, listOf(newMaster) + newOverrides)
    }

    /**
     * Removes one instance of the series: adds an `EXDATE` to the master and drops the override of that instance, if
     * any.
     *
     * @param series the series to change.
     * @param recurrenceId the start of the instance to remove.
     */
    @JvmStatic
    public fun deleteThis(series: SeriesView, recurrenceId: ICalDateTime): VCalendar {
        val override = instanceOf(series, recurrenceId)
        val master = series.master?.component ?: return replace(series, listOfNotNull(override), emptyList())
        val excluded = master.edit { property(Properties.EXDATE, ExDate(listOf(recurrenceId))) }
        return replace(series, listOfNotNull(master, override), listOf(excluded))
    }

    /**
     * Removes the given instance and all following ones: the rule of the master ends before it (`UNTIL`) and the later
     * overrides are dropped. If that leaves nothing, the whole series is removed.
     *
     * @param series the series to change.
     * @param recurrenceId the start of the first instance to remove.
     */
    @JvmStatic
    public fun deleteThisAndFuture(series: SeriesView, recurrenceId: ICalDateTime): VCalendar {
        val future = series.overrides.map { it.component }.filter { !isBefore(it.recurrenceId(), recurrenceId) }
        val master = series.master?.component ?: return replace(series, future, emptyList())
        val before = instancesBefore(master, recurrenceId)
        if (before == 0) return replace(series, listOf(master) + series.overrides.map { it.component }, emptyList())
        return replace(series, listOf(master) + future, listOf(truncated(master, recurrenceId)))
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Series"
}

private fun requireMaster(series: SeriesView): ICalComponent =
    series.master?.component ?: throw IllegalArgumentException("The series has no master")

private fun instanceOf(series: SeriesView, recurrenceId: ICalDateTime): ICalComponent? =
    series.overrides.firstOrNull { sameInstance(it.recurrenceId, recurrenceId) }?.component

private fun newOverride(series: SeriesView, recurrenceId: ICalDateTime): ICalComponent =
    overrideOf(requireMaster(series), recurrenceId)

private fun isBefore(value: ICalDateTime?, limit: ICalDateTime): Boolean =
    value != null && value.localKey() < limit.localKey()

private fun instancesBefore(master: ICalComponent, recurrenceId: ICalDateTime): Int {
    val start = master[Properties.DTSTART] ?: return 0
    val ruled = ruleInstancesBefore(master, recurrenceId) ?: if (isBefore(start, recurrenceId)) 1 else 0
    val dates = master[Properties.RDATE]?.dates.orEmpty().count { isBefore(it, recurrenceId) }
    return ruled + dates
}

private fun truncated(master: ICalComponent, recurrenceId: ICalDateTime): ICalComponent = master.edit {
    val count = ruleInstancesBefore(master, recurrenceId)
    if (count != null) {
        if (count == 0) remove(Properties.RRULE) else rewriteRule(this, master, count)
    }
    keepDates(this, master, recurrenceId, future = false)
}

private fun ruleInstancesBefore(master: ICalComponent, recurrenceId: ICalDateTime): Int? {
    val start = master[Properties.DTSTART] ?: return null
    return master[Properties.RRULE]?.expand(start, null, recurrenceId, RecurrenceOptions.Default)?.count()
}

private fun rewriteRule(builder: NodeBuilder, master: ICalComponent, count: Int) {
    val property = master.property("RRULE") ?: return
    val parts = property.value.split(';').filter { part ->
        val name = part.substringBefore('=').uppercase()
        name != "COUNT" && name != "UNTIL"
    }
    builder.remove(Properties.RRULE)
    builder.property(ICalProperty.of("RRULE", (parts + "COUNT=$count").joinToString(";")))
}

private fun keepDates(builder: NodeBuilder, master: ICalComponent, limit: ICalDateTime, future: Boolean) {
    for (name in listOf("RDATE", "EXDATE")) {
        val properties = master.properties(name)
        if (properties.isEmpty()) continue
        builder.remove(name)
        for (property in properties) {
            val kept = property.value.split(',').filter { text ->
                val date = singleDate(property, text) ?: return@filter true
                isBefore(date, limit) != future
            }
            if (kept.isNotEmpty()) builder.property(withValue(property, kept.joinToString(",")))
        }
    }
}

private fun withValue(property: ICalProperty, value: String): ICalProperty =
    ICalProperty.builder(property.name).apply { property.parameters.forEach { parameter(it) } }.value(value).build()

private fun singleDate(property: ICalProperty, text: String): ICalDateTime? {
    val single = ICalProperty.builder("RDATE").apply {
        property.parameters.forEach { parameter(it) }
    }.value(text).build()
    val component = ICalComponent.builder("VEVENT").property(single).build()
    return component[Properties.RDATE]?.let { it.dates.firstOrNull() ?: it.periods.firstOrNull()?.start }
}

// When the start of the master moves, the RECURRENCE-IDs of its overrides and its EXDATEs move by the same amount,
// unless the edit set EXDATEs itself.
private fun editedMaster(
    master: ICalComponent,
    overrides: List<ICalComponent>,
    edit: VEvent.Builder.() -> Unit,
): Pair<ICalComponent, List<ICalComponent>> {
    val edited = VEvent(master).edit(edit).component
    val before = master[Properties.DTSTART]
    val after = edited[Properties.DTSTART]
    if (before == null || after == null || before == after) return edited to overrides
    val moved = overrides.map { override ->
        val recurrenceId = override.recurrenceId() ?: return@map override
        val target = shifted(recurrenceId, before, after) ?: return@map override
        override.edit { set(Properties.RECURRENCE_ID, RecurrenceId(target)) }
    }
    val exdates = master.properties("EXDATE")
    val shiftedMaster = if (exdates.isNotEmpty() && exdates == edited.properties("EXDATE")) {
        edited.edit {
            remove(Properties.EXDATE)
            for (property in exdates) {
                val values = property.value.split(',').mapNotNull { singleDate(property, it) }
                    .mapNotNull { shifted(it, before, after) }
                if (values.isNotEmpty()) property(Properties.EXDATE, ExDate(values))
            }
        }
    } else {
        edited
    }
    return shiftedMaster to moved
}

private fun replace(series: SeriesView, old: List<ICalComponent>, new: List<ICalComponent>): VCalendar {
    val children = ArrayList(series.calendar.component.components)
    val first = children.indexOfFirst { child -> old.any { it === child } }.let { if (it < 0) children.size else it }
    val remaining = children.filter { child -> old.none { it === child } }
    val at = children.subList(0, first).count { child -> old.none { it === child } }
    val result = ArrayList(remaining)
    result.addAll(at, new)
    return VCalendar(series.calendar.component.edit { children(result) })
}

package io.github.calendrum.model

import io.github.calendrum.SortedStringMap
import io.github.calendrum.recur.SECONDS_PER_DAY_LONG
import io.github.calendrum.recur.civilOf
import io.github.calendrum.recur.dayOf
import io.github.calendrum.recur.floorDiv
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.tz.localSeconds
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.number

// A builder over any component for internal edits (iTIP, series), instance matching by RECURRENCE-ID and shifting a
// date by the distance between two others in the same form.
internal class NodeBuilder(name: String, original: ICalComponent?) :
    ComponentBuilder<NodeBuilder>(name, original, null) {
    fun keep(names: Set<String>): NodeBuilder = apply {
        val nodes = propertyNodes()
        var index = nodes.size - 1
        while (index >= 0) {
            if (nodes[index].name !in names) removeAt(index)
            index -= 1
        }
    }

    fun removeChildren(keep: (ICalComponent) -> Boolean = { false }): NodeBuilder = apply {
        replaceChildren(keep, emptyList())
    }

    fun children(components: List<ICalComponent>): NodeBuilder = apply {
        replaceChildren({ false }, components)
    }

    fun attendees(attendees: List<Attendee>): NodeBuilder = apply {
        remove(Properties.ATTENDEE)
        for (attendee in attendees) property(Properties.ATTENDEE, attendee)
    }

    fun build(): ICalComponent = buildNode()
}

internal fun ICalComponent.edit(block: NodeBuilder.() -> Unit): ICalComponent =
    NodeBuilder(name, this).apply(block).build()

internal fun ICalComponent.sequence(): Int = this[Properties.SEQUENCE] ?: 0

internal fun ICalComponent.uid(): String? = this[Properties.UID]

internal fun ICalComponent.recurrenceId(): ICalDateTime? = this[Properties.RECURRENCE_ID]?.value

internal fun ICalComponent.stamp(): ICalDateTime? = this[Properties.DTSTAMP]

// RECURRENCE-IDs match when equal, as the same UTC instant, or as the same local time of floating and zoned values; UTC
// against a TZID needs zones and does not match.
internal fun sameInstance(left: ICalDateTime?, right: ICalDateTime?): Boolean = when {
    left == null || right == null -> left == null && right == null
    left == right -> true
    left is ICalDateTime.Utc && right is ICalDateTime.Utc -> left.instant == right.instant
    left is ICalDateTime.Date || right is ICalDateTime.Date -> false
    left is ICalDateTime.Utc || right is ICalDateTime.Utc -> false
    else -> localOf(left) == localOf(right)
}

private fun localOf(value: ICalDateTime): Long? = when (value) {
    is ICalDateTime.Floating -> localSeconds(value.dateTime)
    is ICalDateTime.Zoned -> localSeconds(value.dateTime)
    else -> null
}

internal fun shifted(value: ICalDateTime, from: ICalDateTime, to: ICalDateTime): ICalDateTime? = when {
    value is ICalDateTime.Utc && from is ICalDateTime.Utc && to is ICalDateTime.Utc ->
        ICalDateTime.Utc(value.instant + (to.instant - from.instant))

    value is ICalDateTime.Date && from is ICalDateTime.Date && to is ICalDateTime.Date -> {
        val day = epochDay(value) + epochDay(to) - epochDay(from)
        val civil = civilOf(day)
        ICalDateTime.Date(civil shr 9, (civil shr 5) and 15, civil and 31)
    }

    value is ICalDateTime.Utc || value is ICalDateTime.Date -> null

    else -> {
        val start = localOf(from)
        val target = localOf(to)
        val local = localOf(value)
        if (start == null || target == null || local == null) {
            null
        } else {
            val seconds = local + target - start
            val day = floorDiv(seconds, SECONDS_PER_DAY_LONG).toInt()
            val second = (seconds - day * SECONDS_PER_DAY_LONG).toInt()
            val civil = civilOf(day)
            val year = civil shr 9
            val month = (civil shr 5) and 15
            when (value) {
                is ICalDateTime.Zoned -> ICalDateTime.Zoned(year, month, civil and 31, second, value.tzid)
                else -> ICalDateTime.Floating(year, month, civil and 31, second)
            }
        }
    }
}

private fun epochDay(value: ICalDateTime.Date): Int = dayOf(value.date.year, value.date.month.number, value.date.day)

// An instance of the master: no recurrence properties, DTSTART is the RECURRENCE-ID and DTEND or DUE move with it.
internal fun overrideOf(master: ICalComponent, recurrenceId: ICalDateTime): ICalComponent = master.edit {
    val start = master[Properties.DTSTART]
    val end = master[Properties.DTEND]
    val due = master[Properties.DUE]
    remove(Properties.RRULE)
    remove(Properties.RDATE)
    remove(Properties.EXDATE)
    remove("EXRULE")
    set(Properties.DTSTART, if (start != null) recurrenceId else null)
    if (start != null && end != null) set(Properties.DTEND, shifted(end, start, recurrenceId))
    if (start != null && due != null) set(Properties.DUE, shifted(due, start, recurrenceId))
    set(Properties.RECURRENCE_ID, RecurrenceId(recurrenceId))
}

internal fun isOlder(incoming: ICalComponent, stored: ICalComponent): Boolean {
    if (incoming.sequence() != stored.sequence()) return incoming.sequence() < stored.sequence()
    val left = incoming.stamp() as? ICalDateTime.Utc ?: return false
    val right = stored.stamp() as? ICalDateTime.Utc ?: return false
    return left.instant < right.instant
}

// [shell] is the calendar whose properties the result takes; splitting a calendar passes it without its children, so
// that each part does not copy all of them first.
internal fun calendarWith(
    source: ICalComponent,
    components: List<ICalComponent>,
    zones: ZoneIndex = ZoneIndex(source),
    shell: ICalComponent = source,
): ICalComponent = shell.edit { children(zones.usedBy(components) + components) }

// [calendar] with its properties and without its children.
internal fun withoutChildren(calendar: ICalComponent): ICalComponent =
    calendar.withChildren(emptyArray(), calendar.childPositions?.let { IntArray(0) })

// The VTIMEZONEs of a calendar by TZID, for picking those that some components use: the TZIDs of the components are
// looked up here instead of every zone being looked for in every component, which was quadratic with thousands of
// zones and components. TZIDs come from the input, so the table is a tree (see InputKeys.kt).
internal class ZoneIndex(calendar: ICalComponent?) {
    private val zones = calendar?.components("VTIMEZONE").orEmpty()
    private val positions = SortedStringMap<ArrayList<Int>>()

    init {
        for ((index, zone) in zones.withIndex()) {
            val tzid = zone.property("TZID")?.value ?: continue
            val list = positions[tzid] ?: ArrayList<Int>().also { positions[tzid] = it }
            list += index
        }
    }

    // Every zone whose TZID a property of [components] or of their subcomponents names, in the order of the calendar.
    fun usedBy(components: List<ICalComponent>): List<ICalComponent> {
        if (zones.isEmpty()) return emptyList()
        val seen = SortedStringMap<Unit>()
        val found = ArrayList<Int>()
        val pending = ArrayList<ICalComponent>(components)
        while (pending.isNotEmpty()) {
            val component = pending.removeAt(pending.size - 1)
            for (property in component.properties) {
                val tzid = property.parameter("TZID")?.value ?: continue
                if (seen.containsKey(tzid)) continue
                seen[tzid] = Unit
                positions[tzid]?.let { found.addAll(it) }
            }
            pending.addAll(component.components)
        }
        found.sort()
        return found.map { zones[it] }
    }

    override fun toString(): String = "ZoneIndex(${positions.size})"
}

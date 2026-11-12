package io.github.calendrum.itip

import io.github.calendrum.SortedStringMap
import io.github.calendrum.model.recurrenceId
import io.github.calendrum.model.sameInstance
import io.github.calendrum.model.uid
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime

// The children of a store while a message is applied. They are a linked list, so that an override goes in
// right after its master and a series is replaced or removed where it stands; the members of each series are kept by
// UID in an AVL tree (UIDs come from the input, see InputKeys.kt), each list in the order of the store. Finding a
// series costs O(log n) instead of a pass over the store, so a message of k items takes O((n + k) log n) plus the
// members of the series it touches, and the order of the result is the one that index arithmetic on a list gave, for
// series whose members are not adjacent too.
internal class SeriesStore(children: List<ICalComponent>) {
    class Slot(var component: ICalComponent) {
        var previous: Slot? = null
        var next: Slot? = null
    }

    private var first: Slot? = null
    private var last: Slot? = null
    private val series = SortedStringMap<ArrayList<Slot>>()

    init {
        for (child in children) append(child)
    }

    fun members(uid: String): List<Slot> = series[uid].orEmpty()

    fun master(uid: String): Slot? = members(uid).firstOrNull { it.component.recurrenceId() == null }

    fun instance(uid: String, recurrenceId: ICalDateTime): Slot? =
        members(uid).firstOrNull { sameInstance(it.component.recurrenceId(), recurrenceId) }

    // The replacement has the UID of the slot.
    fun replace(slot: Slot, component: ICalComponent) {
        slot.component = component
    }

    fun append(component: ICalComponent) {
        val slot = Slot(component)
        link(slot, null)
        val uid = seriesUid(component) ?: return
        val members = series[uid] ?: ArrayList<Slot>().also { series[uid] = it }
        members += slot
    }

    // [component] is a member of the series of [master] and goes right after it, so it follows the master among the
    // members as well.
    fun insertAfter(master: Slot, component: ICalComponent) {
        val slot = Slot(component)
        link(slot, master.next)
        val members = series[seriesUid(master.component)!!]!!
        members.add(members.indexOf(master) + 1, slot)
    }

    fun remove(slot: Slot) {
        unlink(slot)
        series[seriesUid(slot.component)!!]!!.remove(slot)
    }

    // The members of [uid] are replaced by [components], all of that UID, where its first member stood, or at the end.
    fun replaceSeries(uid: String, components: List<ICalComponent>) {
        val old = members(uid)
        val at = old.firstOrNull()
        val slots = ArrayList<Slot>(components.size)
        for (component in components) slots += Slot(component).also { link(it, at) }
        for (slot in old) unlink(slot)
        series[uid] = slots
    }

    fun toList(): ArrayList<ICalComponent> {
        val result = ArrayList<ICalComponent>()
        var slot = first
        while (slot != null) {
            result += slot.component
            slot = slot.next
        }
        return result
    }

    // Links [slot] before [before], or at the end when it is null.
    private fun link(slot: Slot, before: Slot?) {
        val previous = if (before == null) last else before.previous
        slot.previous = previous
        slot.next = before
        if (previous == null) first = slot else previous.next = slot
        if (before == null) last = slot else before.previous = slot
    }

    private fun unlink(slot: Slot) {
        val previous = slot.previous
        val next = slot.next
        if (previous == null) first = next else previous.next = next
        if (next == null) last = previous else next.previous = previous
    }

    private fun seriesUid(component: ICalComponent): String? =
        if (component.name in SCHEDULED) component.uid() else null

    override fun toString(): String = "SeriesStore(${series.size} series)"
}

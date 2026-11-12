package io.github.calendrum.syntax

/**
 * Supplies a `VTIMEZONE` component for a time zone identifier, so that a written calendar can be self-contained ([RFC
 * 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 *
 * Used by `WriteOptions.Builder.addMissingTimeZones`. The `calendrum-tzdata` module provides one for every IANA zone.
 */
public fun interface VTimeZoneSource {
    /**
     * Returns the `VTIMEZONE` for [tzid], or `null` when this source does not know the zone.
     *
     * @param tzid the value of a `TZID` parameter.
     */
    public fun vtimezone(tzid: String): ICalComponent?
}

internal fun withMissingTimeZones(calendar: ICalComponent, source: VTimeZoneSource): ICalComponent {
    if (calendar.nameId != KnownNames.VCALENDAR) return calendar
    val children = calendar.componentNodes
    val existing = HashSet<String>()
    for (child in children) {
        if (child.nameId == KnownNames.VTIMEZONE) child.property("TZID")?.let { existing += it.value }
    }
    val used = LinkedHashSet<String>()
    val pending = ArrayList<ICalComponent>()
    for (child in children) if (child.nameId != KnownNames.VTIMEZONE) pending += child
    while (pending.isNotEmpty()) {
        val component = pending.removeAt(pending.size - 1)
        for (property in component.propertyNodes) {
            val tzid = property.parameter("TZID")?.value ?: continue
            if (tzid !in existing) used += tzid
        }
        for (child in component.componentNodes) pending += child
    }
    if (used.isEmpty()) return calendar
    val added = ArrayList<ICalComponent>()
    for (tzid in used) source.vtimezone(tzid)?.let { added += it }
    if (added.isEmpty()) return calendar
    val all = Array(added.size + children.size) { if (it < added.size) added[it] else children[it - added.size] }
    val positions = calendar.childPositions?.let { old ->
        val first = if (old.isEmpty()) calendar.propertyNodes.size else old[0]
        IntArray(all.size) { if (it < added.size) first else old[it - added.size] }
    }
    return calendar.withChildren(all, positions)
}

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.SortedStringMap
import io.github.calendrum.syntax.FingerprintOptions
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import kotlin.time.Clock

// Components are compared by fingerprints of their canonical form without DTSTAMP, LAST-MODIFIED and SEQUENCE; an
// unchanged component is the previously published one, so the feed keeps its bytes.
/**
 * Keeps the scheduling properties of a published feed right between two versions of it.
 *
 * For every component that is new or whose content changed since the previous version, [publish] sets `DTSTAMP` and
 * `LAST-MODIFIED` to the time of the clock and raises `SEQUENCE`; unchanged components keep their old values, so
 * clients do not see changes that did not happen.
 *
 * @property clock gives the time for the new `DTSTAMP`.
 */
@ExperimentalICalApi
public class FeedPublisher(public val clock: Clock) {
    /**
     * Returns [current] with the properties updated as described, comparing it with [previous].
     *
     * @param previous the version published last time, or `null` for the first one.
     * @param current the version to publish.
     * @throws IllegalArgumentException if [previous] or [current] cannot be written canonically, as with
     *   [Canonical.canonicalize].
     */
    public fun publish(previous: VCalendar?, current: VCalendar): VCalendar {
        val now = ICalDateTime.Utc(clock.now())
        // Keys come from the input: kept in trees, not hashed (see InputKeys.kt).
        val old = SortedStringMap<ICalComponent>()
        previous?.component?.components?.forEach { child -> keyOf(child)?.let { old[it] = child } }
        val touched = changedKeys(previous, current)
        var edited = false
        val children = current.component.components.map { child ->
            val key = keyOf(child) ?: return@map child
            val before = old[key]
            val result = when {
                before == null || touched.containsKey(key) -> child.edit {
                    set(Properties.DTSTAMP, now)
                    set(Properties.LAST_MODIFIED, now)
                    val sequence = if (before ==
                        null
                    ) {
                        child[Properties.SEQUENCE]
                    } else {
                        maxOf(before.sequence() + 1, child.sequence())
                    }
                    set(Properties.SEQUENCE, sequence)
                }

                else -> before
            }
            if (result != child) edited = true
            result
        }
        return if (edited) VCalendar(current.component.edit { children(children) }) else current
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "FeedPublisher($clock)"
}

private fun changedKeys(previous: VCalendar?, current: VCalendar): SortedStringMap<Unit> {
    val keys = SortedStringMap<Unit>()
    if (previous == null) return keys
    val options = FingerprintOptions.builder().ignoredProperties(VOLATILE).build()
    val before = ICal.fingerprints(Canonical.canonicalize(previous), options)
    val after = ICal.fingerprints(Canonical.canonicalize(current), options)
    val diff = after.diff(before)
    for (entry in diff.changed + diff.added) {
        keys["${entry.key.name}\u0000${entry.key.uid}\u0000${entry.key.recurrenceId}"] = Unit
    }
    return keys
}

private fun keyOf(component: ICalComponent): String? {
    val uid = component.property("UID")?.value ?: return null
    return "${component.name}\u0000$uid\u0000${component.property("RECURRENCE-ID")?.value}"
}

private val VOLATILE = setOf("DTSTAMP", "LAST-MODIFIED", "SEQUENCE")

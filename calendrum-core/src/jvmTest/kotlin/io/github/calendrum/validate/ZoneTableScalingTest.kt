package io.github.calendrum.validate

import io.github.calendrum.SortedStringMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

// A guard against quadratic time zone tables: the zones of the file, the cache of the occurrences and the
// VTIMEZONE resolver kept their TZIDs in a sorted array, and each TZID in descending order shifted all the others, so
// this input took 11 s on one table. The tree takes well under a tenth of a second; the bound is far from both. The
// callers meet 2^17 such TZIDs in `HostileInputTest.tzidsInDescendingAndShuffledOrder`: a calendar of 400,000 zones or
// events does not fit in the heap of the tests.
class ZoneTableScalingTest {
    @Test
    fun tzidsInDescendingOrderAreNotQuadratic() {
        val tzids = List(TZIDS) { "Zone/" + it.toString().padStart(7, '0') }.asReversed()
        val table = SortedStringMap<Int>()
        val time = measureTime {
            for ((index, tzid) in tzids.withIndex()) if (!table.containsKey(tzid)) table[tzid] = index
        }
        assertTrue(time < BOUND, "the table took $time")
        assertEquals(TZIDS, table.size)
        assertEquals(TZIDS - 1, table["Zone/0000000"])
    }

    private companion object {
        const val TZIDS = 400_000
        val BOUND = 3.seconds
    }
}

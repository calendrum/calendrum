package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.model.Canonical
import io.github.calendrum.model.calendars

// The canonical form of any calendar is a fixed point: canonicalizing what it reads back to gives the same bytes. It
// throws IllegalArgumentException only for a calendar that the canonical writer rejects as well.
class CanonicalFuzzTest {
    @FuzzTest
    fun canonical(input: ByteArray) {
        for (calendar in ICal.parse(input).calendars) {
            val first = try {
                Canonical.canonicalize(calendar)
            } catch (e: IllegalArgumentException) {
                invariant(!canonicallyWritable(calendar.component)) { "canonicalize throws on a writable calendar: $e" }
                continue
            }
            val again = ICal.parse(first).calendars
            invariant(again.size == 1) { "the canonical form reads back as ${again.size} calendars" }
            invariant(Canonical.canonicalize(again[0]).contentEquals(first)) {
                "the canonical form is not a fixed point"
            }
        }
    }
}

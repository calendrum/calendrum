package io.github.calendrum.benchmarks.write

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.cursor.assertCursorMatches
import kotlin.test.Test

class HugeRoundTripTest {
    @Test
    fun huge() {
        assertRoundTrip(Corpus.HUGE)
    }

    @Test
    fun hugeCursor() {
        assertCursorMatches(Corpus.HUGE)
    }
}

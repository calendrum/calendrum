package io.github.calendrum.benchmarks.reader

import io.github.calendrum.benchmarks.corpus.Corpus
import kotlin.test.Test

// `huge` is JVM-only, like `HugeStructuralTest`: 100 MB in memory.
class HugeReaderTest {
    @Test
    fun huge() {
        assertMatchesReference(Corpus.HUGE)
    }
}

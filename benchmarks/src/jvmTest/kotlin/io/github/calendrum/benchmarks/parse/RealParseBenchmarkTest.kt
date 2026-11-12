package io.github.calendrum.benchmarks.parse

import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.assumeRealCorpus
import kotlin.test.Test

class RealParseBenchmarkTest {
    @Test
    fun university() {
        assumeRealCorpus()
        val benchmark = RealParseBenchmark()
        benchmark.file = RealCorpus.UNIVERSITY
        benchmark.setUp()
    }
}

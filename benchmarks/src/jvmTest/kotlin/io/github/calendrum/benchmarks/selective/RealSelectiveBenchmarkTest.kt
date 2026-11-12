package io.github.calendrum.benchmarks.selective

import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.assumeRealCorpus
import kotlin.test.Test

class RealSelectiveBenchmarkTest {
    @Test
    fun university() {
        assumeRealCorpus()
        val benchmark = RealSelectiveBenchmark()
        benchmark.file = RealCorpus.UNIVERSITY
        benchmark.setUp()
    }
}

package io.github.calendrum.benchmarks.values

import io.github.calendrum.benchmarks.corpus.Corpus
import kotlin.test.Test

class ValuesCorpusTest {
    @Test
    fun invite() {
        setUp(Corpus.INVITE)
    }

    @Test
    fun personal() {
        setUp(Corpus.PERSONAL)
    }

    private fun setUp(corpus: String) {
        val benchmark = ValuesBenchmark()
        benchmark.corpus = corpus
        benchmark.setUp()
    }
}

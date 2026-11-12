package io.github.calendrum.benchmarks.adapters

import io.github.calendrum.benchmarks.corpus.Corpus
import kotlin.test.Test

class AdaptersSetUpTest {
    @Test
    fun theSetUpChecksPassOnTheSmallCorpora() {
        for (corpus in listOf(Corpus.INVITE, Corpus.PERSONAL)) {
            val benchmark = AdaptersBenchmark()
            benchmark.corpus = corpus
            benchmark.setUp()
        }
    }
}

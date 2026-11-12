package io.github.calendrum.benchmarks.parseTyped

import io.github.calendrum.benchmarks.corpus.Corpus
import kotlin.test.Test

class ParseTypedCorpusTest {
    @Test
    fun invite() {
        setUpParseTyped(Corpus.INVITE)
    }

    @Test
    fun personal() {
        setUpParseTyped(Corpus.PERSONAL)
    }
}

internal fun setUpParseTyped(corpus: String) {
    val benchmark = ParseTypedBenchmark()
    benchmark.corpus = corpus
    benchmark.setUp()
}

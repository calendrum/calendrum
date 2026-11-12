package io.github.calendrum.benchmarks.schema

import io.github.calendrum.benchmarks.corpus.Corpus
import kotlin.test.Test

class SchemaBenchmarkSetUpTest {
    @Test
    fun everySetUpPasses() {
        for (corpus in listOf(Corpus.PERSONAL, Corpus.UNIVERSITY)) {
            SchemaBenchmark().apply { this.corpus = corpus }.setUp()
            Ical4jSchemaBenchmark().apply { this.corpus = corpus }.setUp()
            BiweeklySchemaBenchmark().apply { this.corpus = corpus }.setUp()
        }
    }
}

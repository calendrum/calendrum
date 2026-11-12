package io.github.calendrum.benchmarks.schema

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import kotlin.test.Test
import kotlin.test.assertTrue

class SchemaCorpusTest {
    @Test
    fun personal() {
        assertTrue(setUpSchema(Corpus.PERSONAL, corpusBytes(Corpus.PERSONAL)).isNotEmpty())
    }

    @Test
    fun invite() {
        assertTrue(setUpSchema(Corpus.INVITE, corpusBytes(Corpus.INVITE)).isNotEmpty())
    }
}

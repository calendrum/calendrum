package io.github.calendrum.benchmarks.corpus

import kotlin.test.Test
import kotlin.test.assertEquals

private class Expected(val bytes: Long, val fnv1a64: String)

private val EXPECTED: Map<String, Expected> = mapOf(
    Corpus.INVITE to Expected(2844, "5982c9c2e33662ce"),
    Corpus.INVITE_BATCH to Expected(3369948, "d23d98b5839675cf"),
    Corpus.PERSONAL to Expected(3971107, "b5c20bedc3187a32"),
    Corpus.UNIVERSITY to Expected(15785428, "ec349806937c42aa"),
)

class CorpusDeterminismTest {
    @Test
    fun invite() = assertDeterministic(Corpus.INVITE)

    @Test
    fun inviteBatch() = assertDeterministic(Corpus.INVITE_BATCH)

    @Test
    fun personal() = assertDeterministic(Corpus.PERSONAL)

    @Test
    fun university() = assertDeterministic(Corpus.UNIVERSITY)

    private fun assertDeterministic(name: String) {
        val expected = EXPECTED.getValue(name)
        val hash = Fnv1a64Sink()
        val counting = ByteCountingSink()
        Corpus.write(name, TeeSink(hash, counting))
        assertEquals(expected.bytes, counting.count, "$name byte length changed; bump CORPUS_GENERATOR_VERSION")
        assertEquals(expected.fnv1a64, hash.hexDigest(), "$name FNV-1a64 changed; bump CORPUS_GENERATOR_VERSION")
    }
}

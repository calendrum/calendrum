package io.github.calendrum.benchmarks.coldStart

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ColdStartSetUpTest {
    @Test
    fun everyVariantOfTheLibraryParsesTheInvite() {
        val input = corpusBytes(Corpus.INVITE)
        for (variant in ColdStartVariant.ours) {
            assertTrue(firstParse(variant, input) >= 0, variant)
        }
    }

    @Test
    fun anUnknownVariantFails() {
        assertFailsWith<IllegalStateException> { firstParse("unknown", corpusBytes(Corpus.INVITE)) }
    }
}

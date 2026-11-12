package io.github.calendrum.benchmarks.fuzz

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FuzzSeedTest {
    @Test
    fun inviteSeedsMatchTheGenerator() {
        val inputs = Paths.get(System.getProperty("calendrum.fuzzInputs"))
        val seeds = inviteSeeds(inputs)
        assertTrue(seeds.isNotEmpty(), "no $INVITE_SEED under $inputs")
        val invite = corpusBytes(Corpus.INVITE)
        val stale = seeds.filter {
            !Files.readAllBytes(it).contentEquals(invite)
        }.map { inputs.relativize(it).toString() }
        assertEquals(emptyList(), stale, "stale fuzz seeds; run ./gradlew :benchmarks:updateFuzzSeeds")
    }
}

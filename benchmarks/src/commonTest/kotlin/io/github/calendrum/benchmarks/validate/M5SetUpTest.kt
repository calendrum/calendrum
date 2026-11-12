package io.github.calendrum.benchmarks.validate

import io.github.calendrum.benchmarks.feedPublisher.FeedPublisherBenchmark
import io.github.calendrum.benchmarks.fixups.FixupsBenchmark
import kotlin.test.Test

class M5SetUpTest {
    @Test
    fun validationSetUp() {
        for (corpus in listOf("invite", "personal")) ValidateBenchmark().apply { this.corpus = corpus }.setUp()
    }

    @Test
    fun fixupsSetUp() {
        for (fixup in listOf("byProdId", "outlook", "google")) {
            FixupsBenchmark().apply {
                corpus = "invite-batch"
                this.fixup = fixup
            }.setUp()
        }
    }

    @Test
    fun feedPublisherSetUp() {
        FeedPublisherBenchmark().apply { corpus = "personal" }.setUp()
    }
}

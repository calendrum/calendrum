package io.github.calendrum.benchmarks.coldStart

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColdStartRunnerTest {
    @Test
    fun rivalsAndTheLibraryAgreeWithTheGeneratorOnTheInvite() {
        val input = corpusBytes(Corpus.INVITE)
        for (variant in ColdStartVariant.ours + ColdStartVariant.rivals) {
            assertTrue(firstParseWithRival(variant, input) >= 0, variant)
        }
    }

    @Test
    fun summaryUsesLinearQuantiles() {
        val summary = summarize(listOf(4_000_000L, 1_000_000L, 3_000_000L, 2_000_000L, 5_000_000L))
        assertEquals(1.0, summary.min)
        assertEquals(2.0, summary.q1)
        assertEquals(3.0, summary.median)
        assertEquals(4.0, summary.q3)
        assertEquals(5.0, summary.max)
    }

    @Test
    fun aNewProcessReportsTheFirstParseForEveryVariantAndRotatesTheOrder() {
        val directory = Files.createTempDirectory("cold-start").toFile()
        try {
            val input = directory.resolve("invite.ics")
            input.writeBytes(corpusBytes(Corpus.INVITE))
            val variants = listOf(ColdStartVariant.FLOOR, ColdStartVariant.ICS_TREE, ColdStartVariant.ICAL4J)
            val samples = measure(listOf(jvmLauncher(input, variants)), rounds = 2, discard = 1)
            assertEquals(variants, samples.map { it.variant })
            for (sample in samples) {
                assertEquals(2, sample.firstParse.size, sample.variant)
                assertTrue(sample.firstParse.all { it > 0 }, sample.variant)
                assertTrue(sample.wall.zip(sample.firstParse).all { (wall, parse) -> wall > parse }, sample.variant)
            }
            assertTrue(table(samples).lines().size > variants.size)
            assertTrue(json(samples, 2, 1, mapOf("power" to "AC")).contains("\"variant\": \"ical4j\""))
        } finally {
            directory.deleteRecursively()
        }
    }
}

package io.github.calendrum.benchmarks.corpus

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CorpusChecksumTest {
    @Test
    fun matchesBaseline() {
        val baselineFile = File("corpora.sha256")
        assertTrue(baselineFile.isFile, "Expected ${baselineFile.absolutePath} to exist")
        val expected = baselineFile.readLines()
            .filter { it.isNotBlank() }
            .associate { line ->
                val hash = line.substringBefore(' ')
                val name = line.substringAfterLast(' ').removeSuffix(".ics")
                name to hash
            }
        assertEquals(Corpus.names.toSet(), expected.keys, "corpora.sha256 must list exactly Corpus.names")
        for (name in Corpus.names) {
            val sha256 = Sha256Sink()
            Corpus.write(name, sha256)
            assertEquals(
                expected.getValue(name),
                sha256.hexDigest(),
                "$name SHA-256 changed; bump CORPUS_GENERATOR_VERSION and update corpora.sha256",
            )
        }
    }
}

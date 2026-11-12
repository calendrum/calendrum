package io.github.calendrum.benchmarks.selective

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.assumeRealCorpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import kotlin.test.Test

// The `invite-batch` and `university` cases of the common corpus tests, JVM only. The debug test binaries of
// Kotlin/Native and JS spent most of their time there (the selection invariant on `university` alone took 25 s on
// macosArm64); `invite` and `personal` keep every check on every platform, and `CorpusDeterminismTest` keeps all four
// corpora there, so the corpora stay byte-identical across platforms.
class LargeSelectionCorpusTest {
    @Test
    fun university() {
        setUpSelective(Corpus.UNIVERSITY)
        assertSelectionsMatch(corpusBytes(Corpus.UNIVERSITY), seed = 3, random = 4)
    }

    @Test
    fun inviteBatch() {
        assertSelectionsMatch(corpusBytes(Corpus.INVITE_BATCH), seed = 4)
    }

    @Test
    fun realUniversitySchedule() {
        assumeRealCorpus()
        assertSelectionsMatch(RealCorpus.bytes(RealCorpus.UNIVERSITY), seed = 5)
    }
}

package io.github.calendrum.benchmarks.patch

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.assumeRealCorpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import kotlin.test.Test
import kotlin.test.assertContentEquals

// The `invite-batch` and `university` cases of the common corpus tests, JVM only. The debug test binaries of
// Kotlin/Native and JS spent most of their time there (the selection invariant on `university` alone took 25 s on
// macosArm64); `invite` and `personal` keep every check on every platform, and `CorpusDeterminismTest` keeps all four
// corpora there, so the corpora stay byte-identical across platforms.
class LargePatchCorpusTest {
    @Test
    fun inviteBatch() {
        setUpPatch(Corpus.INVITE_BATCH)
    }

    @Test
    fun university() {
        setUpPatch(Corpus.UNIVERSITY)
    }

    @Test
    fun universityEditRoundTrip() {
        checkEditRoundTrip(corpusBytes(Corpus.UNIVERSITY))
    }

    @Test
    fun realUniversityEditRoundTrip() {
        assumeRealCorpus()
        checkEditRoundTrip(RealCorpus.bytes(RealCorpus.UNIVERSITY))
    }

    @Test
    fun realUniversityScheduleChangesOneLine() {
        assumeRealCorpus()
        val bytes = RealCorpus.bytes(RealCorpus.UNIVERSITY)
        val target = patchTarget(bytes)
        val result = patch(bytes, target)
        checkOneLine(bytes, target, result)
        assertContentEquals(editAndWrite(bytes, target), result.bytes)
    }
}

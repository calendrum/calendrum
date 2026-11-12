package io.github.calendrum.benchmarks.fingerprints

import io.github.calendrum.benchmarks.corpus.Corpus
import kotlin.test.Test

// The `invite-batch` and `university` cases of the common corpus tests, JVM only. The debug test binaries of
// Kotlin/Native and JS spent most of their time there (the selection invariant on `university` alone took 25 s on
// macosArm64); `invite` and `personal` keep every check on every platform, and `CorpusDeterminismTest` keeps all four
// corpora there, so the corpora stay byte-identical across platforms.
class LargeFingerprintCorpusTest {
    @Test
    fun inviteBatch() {
        assertFingerprintsMatchTheTree(Corpus.INVITE_BATCH)
        assertEmptyInterceptorsChangeNothing(Corpus.INVITE_BATCH)
    }

    @Test
    fun university() {
        assertFingerprintsMatchTheTree(Corpus.UNIVERSITY)
        assertEmptyInterceptorsChangeNothing(Corpus.UNIVERSITY)
    }

    @Test
    fun huge() {
        assertFingerprintsMatchTheTree(Corpus.HUGE)
    }
}

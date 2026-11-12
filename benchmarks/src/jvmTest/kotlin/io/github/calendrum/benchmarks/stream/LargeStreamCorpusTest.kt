package io.github.calendrum.benchmarks.stream

import io.github.calendrum.benchmarks.corpus.Corpus
import kotlin.test.Test

// The `invite-batch` and `university` cases of the common corpus tests, JVM only. The debug test binaries of
// Kotlin/Native and JS spent most of their time there (the selection invariant on `university` alone took 25 s on
// macosArm64); `invite` and `personal` keep every check on every platform, and `CorpusDeterminismTest` keeps all four
// corpora there, so the corpora stay byte-identical across platforms.
class LargeStreamCorpusTest {
    @Test
    fun inviteBatch() {
        assertStreamMatches(Corpus.INVITE_BATCH, listOf(1, 7, 16, LEXER_BLOCK_BYTES))
    }

    @Test
    fun university() {
        assertStreamMatches(Corpus.UNIVERSITY, listOf(16, LEXER_BLOCK_BYTES))
    }
}

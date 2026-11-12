// The structural checks of `huge` live in `jvmTest`: it is the one corpus scoped to JVM-only for size and memory
// reasons, unlike the byte-length and FNV-1a64 determinism check in `CorpusDeterminismTest` (commonTest), which covers
// `invite`, `invite-batch`, `personal` and `university` on every host platform. The `invite-batch` and `university`
// structural checks are JVM-only too (`LargeCorpusStructuralTest`).

package io.github.calendrum.benchmarks.corpus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HugeStructuralTest {
    @Test
    fun wellFormedAndUniqueUids() {
        val bytes = corpusBytes(Corpus.HUGE)

        val folding = analyzeFolding(bytes)
        assertTrue(folding.violations.isEmpty(), "folding violations: ${folding.violations}")
        assertTrue(!folding.hasBareCrOrLf, "bare CR or LF found")

        val logicalLines = unfoldLines(bytes)
        val balance = checkBalance(logicalLines)
        for ((component, delta) in balance) {
            assertEquals(0, delta, "unbalanced BEGIN/END for $component")
        }

        val uids = logicalLines.filter { it.startsWith("UID:") }
        assertEquals(uids.size, uids.toSet().size, "duplicate UID in huge corpus")
    }
}

@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.benchmarks.fingerprints

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.Fingerprints
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class FingerprintDiffBenchmark {
    private var next: ByteArray = ByteArray(0)
    private var previous: Fingerprints = ICal.fingerprints(ByteArray(0))
    private var current: Fingerprints = previous
    private var stored: String = ""

    @Setup
    fun setUp() {
        val bytes = corpusBytes(Corpus.UNIVERSITY)
        val edited = editEveryHundredthEvent(bytes)
        next = edited.bytes
        previous = ICal.fingerprints(bytes)
        current = ICal.fingerprints(next)
        stored = previous.encodeToString()
        val diff = current.diff(previous)
        check(diff.changed.size == edited.changed && diff.added.isEmpty() && diff.removed.isEmpty()) {
            "university: expected ${edited.changed} changed events, got $diff"
        }
        check(diff.unchangedCount == previous.entries.size - edited.changed) { "university: unchanged $diff" }
    }

    @Benchmark
    fun diff(blackhole: Blackhole) {
        blackhole.consume(current.diff(previous))
    }

    @Benchmark
    fun decodeAndDiff(blackhole: Blackhole) {
        blackhole.consume(current.diff(Fingerprints.decodeFromString(stored)))
    }

    @Benchmark
    fun fingerprintAndDiff(blackhole: Blackhole) {
        blackhole.consume(ICal.fingerprints(next).diff(previous))
    }
}

internal class EditedFeed(val bytes: ByteArray, val changed: Int)

// The second version of `university`: a line `X-EDITED:1` after every hundredth `BEGIN:VEVENT`, so exactly those events
// change and none is added or removed.
internal fun editEveryHundredthEvent(bytes: ByteArray): EditedFeed {
    val begin = "BEGIN:VEVENT\r\n"
    val text = bytes.decodeToString()
    val out = StringBuilder(text.length + text.length / EDIT_EVERY)
    var events = 0
    var changed = 0
    var from = 0
    while (true) {
        val at = text.indexOf(begin, from)
        if (at < 0) break
        out.append(text, from, at + begin.length)
        if (events++ % EDIT_EVERY == 0) {
            out.append("X-EDITED:1\r\n")
            changed++
        }
        from = at + begin.length
    }
    out.append(text, from, text.length)
    return EditedFeed(out.toString().encodeToByteArray(), changed)
}

private const val EDIT_EVERY = 100

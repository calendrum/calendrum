package io.github.calendrum.benchmarks.fingerprints

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class FingerprintsBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        val expected = expectedEntries(bytes)
        val entries = ICal.fingerprints(bytes).entries.size
        check(entries == expected) { "$corpus: expected $expected fingerprints, got $entries" }
    }

    @Benchmark
    fun fingerprints(blackhole: Blackhole) {
        blackhole.consume(ICal.fingerprints(bytes))
    }
}

internal fun expectedEntries(bytes: ByteArray): Int {
    var count = 0
    for (top in ICal.parse(bytes).components) count += if (top.name == "VCALENDAR") top.components.size else 1
    return count
}

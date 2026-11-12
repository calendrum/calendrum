package io.github.calendrum.benchmarks.baselineParse

import io.github.calendrum.benchmarks.baseline.BiweeklyBaseline
import io.github.calendrum.benchmarks.baseline.baselineInputs
import io.github.calendrum.benchmarks.baseline.requireGeneratorCounts
import io.github.calendrum.benchmarks.corpus.Corpus
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
class BiweeklyParseBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private var inputs: List<ByteArray> = emptyList()

    @Setup
    fun setUp() {
        inputs = baselineInputs(corpus)
        requireGeneratorCounts(
            corpus,
            BiweeklyBaseline.NAME,
            BiweeklyBaseline.counts(inputs.map(BiweeklyBaseline::parse)),
        )
    }

    @Benchmark
    fun parse(blackhole: Blackhole) {
        for (input in inputs) {
            blackhole.consume(BiweeklyBaseline.parse(input))
        }
    }
}

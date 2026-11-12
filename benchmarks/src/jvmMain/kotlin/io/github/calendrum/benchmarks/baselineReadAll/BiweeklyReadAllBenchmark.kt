package io.github.calendrum.benchmarks.baselineReadAll

import io.github.calendrum.benchmarks.baseline.BiweeklyBaseline
import io.github.calendrum.benchmarks.baseline.ValueSink
import io.github.calendrum.benchmarks.baseline.baselineInputs
import io.github.calendrum.benchmarks.baseline.generatorCounts
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
class BiweeklyReadAllBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private var inputs: List<ByteArray> = emptyList()

    @Setup
    fun setUp() {
        inputs = baselineInputs(corpus)
        val calendars = inputs.map(BiweeklyBaseline::parse)
        requireGeneratorCounts(corpus, BiweeklyBaseline.NAME, BiweeklyBaseline.counts(calendars))
        val values = calendars.sumOf { BiweeklyBaseline.readAll(it, ValueSink {}) }
        check(values == generatorCounts(corpus).properties) { "$corpus: read $values property values" }
    }

    @Benchmark
    fun readAll(blackhole: Blackhole) {
        val sink = ValueSink { blackhole.consume(it) }
        var values = 0
        for (input in inputs) {
            values += BiweeklyBaseline.readAll(BiweeklyBaseline.parse(input), sink)
        }
        blackhole.consume(values)
    }
}

package io.github.calendrum.benchmarks.schema

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
class Ical4jSchemaBenchmark {
    @Param("personal", "university", "huge")
    var corpus: String = Corpus.PERSONAL

    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        requireSameLessons(corpus, setUpSchema(corpus, bytes), Ical4jLessons.read(bytes))
    }

    @Benchmark
    fun lessons(blackhole: Blackhole) {
        blackhole.consume(Ical4jLessons.read(bytes))
    }
}

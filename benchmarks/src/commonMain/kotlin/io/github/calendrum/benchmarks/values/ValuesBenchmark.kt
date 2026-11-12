@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.values

import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.value.ValueCodecs
import io.github.calendrum.value.ValueProblems
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
class ValuesBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val codecs = ValueCodecs(false, false, null)
    private var values: CorpusValues =
        CorpusValues("", IntArray(0), emptyArray(), IntArray(0), IntArray(0), emptyArray())

    // Every value must decode without a problem: the synthetic corpora contain RFC forms only.
    @Setup
    fun setUp() {
        values = corpusValues(corpusBytes(corpus), BENCHMARK_PROPERTIES)
        check(values.size > 0) { "$corpus: no values" }
        var problems = 0
        val checking = ValueCodecs(false, false, ValueProblems { _, _ -> problems += 1 })
        for (index in 0 until values.size) {
            checkNotNull(values.decode(checking, index)) { "$corpus: value $index of ${values.names[index]}" }
        }
        check(problems == 0) { "$corpus: $problems value problems" }
    }

    @Benchmark
    fun decode(blackhole: Blackhole) {
        val values = values
        for (index in 0 until values.size) {
            blackhole.consume(
                codecs.decode(
                    values.types[index],
                    values.text,
                    values.starts[index],
                    values.ends[index],
                    values.tzids[index],
                ),
            )
        }
    }
}

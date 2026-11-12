package io.github.calendrum.benchmarks.parseTyped

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalParser
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
class ParseTypedBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val parser: ICalParser = ICal.parser()
    private val eager: ICalParser = ICal.parser { decodeEagerly = true }
    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        val result = eager.parse(bytes)
        val problems = result.problems.filter { it.severity != Severity.Info }
        check(problems.isEmpty()) { "$corpus: unexpected problems $problems" }
        val reader = ModelReader()
        val values = reader.read(parser.parse(bytes))
        check(values > 0) { "$corpus: nothing read" }
        val expected = expectedCounts.getValue(corpus).components.values.sum()
        check(reader.components >= expected) { "$corpus: read ${reader.components} components, expected $expected" }
        check(readModel(eager.parse(bytes)) == values) { "$corpus: eager and lazy reads differ" }
    }

    @Benchmark
    fun readAll(blackhole: Blackhole) {
        blackhole.consume(readModel(parser.parse(bytes)))
    }

    @Benchmark
    fun eagerReadAll(blackhole: Blackhole) {
        blackhole.consume(readModel(eager.parse(bytes)))
    }
}

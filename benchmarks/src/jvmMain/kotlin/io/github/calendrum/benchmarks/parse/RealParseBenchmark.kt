package io.github.calendrum.benchmarks.parse

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.benchmarks.corpus.RealCorpus
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

// A JVM-only class next to `ParseBenchmark` because the real file is read from disk; its parameter is `file`, not
// `corpus`, so `-Pbench.corpus` (JMH `-p corpus=`) neither changes it nor hands `ParseBenchmark` a name the generator
// does not know.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class RealParseBenchmark {
    @Param(RealCorpus.UNIVERSITY)
    var file: String = RealCorpus.UNIVERSITY

    private val parser: ICalParser = ICal.parser()
    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = RealCorpus.bytes(file)
        val result = parser.parse(bytes)
        val problems = result.problems.filter { it.severity != Severity.Info }
        check(problems.isEmpty()) { "$file: unexpected problems $problems" }
        val expected = RealCorpus.events(file)
        val events = countComponents(result, EVENT)
        check(events == expected) { "$file: expected $expected $EVENT, got $events" }
        check(readAll(result) > 0) { "$file: nothing to read" }
    }

    @Benchmark
    fun tree(blackhole: Blackhole) {
        blackhole.consume(parser.parse(bytes))
    }

    @Benchmark
    fun treeReadAll(blackhole: Blackhole) {
        blackhole.consume(readAll(parser.parse(bytes)))
    }

    private companion object {
        const val EVENT = "VEVENT"
    }
}

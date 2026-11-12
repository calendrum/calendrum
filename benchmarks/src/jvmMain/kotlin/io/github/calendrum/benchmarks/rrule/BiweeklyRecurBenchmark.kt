package io.github.calendrum.benchmarks.rrule

import biweekly.util.Recurrence
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
class BiweeklyRecurBenchmark {
    @Param("simple", "complex")
    var rules: String = RruleRules.SIMPLE

    private var parsed: List<Recurrence> = emptyList()

    @Setup
    fun setUp() {
        parsed = RivalRrules.biweekly(rules)
        val total = parsed.sumOf { RivalRrules.biweeklyCount(it) }
        check(total == RivalRrules.expectedBiweeklyTotals.getValue(rules)) {
            "$rules: biweekly expanded $total occurrences"
        }
    }

    @Benchmark
    fun expand(blackhole: Blackhole) {
        var total = 0
        for (rule in parsed) total += RivalRrules.biweeklyCount(rule)
        blackhole.consume(total)
    }
}

package io.github.calendrum.benchmarks.rrule

import io.github.calendrum.recur.expand
import io.github.calendrum.value.Recur
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
class RruleBenchmark {
    @Param("simple", "complex")
    var rules: String = RruleRules.SIMPLE

    private var parsed: List<Recur> = emptyList()

    @Setup
    fun setUp() {
        parsed = RruleRules.rules(rules)
        val total = expandAll()
        check(total == RruleRules.expectedTotals.getValue(rules)) { "$rules: expanded $total occurrences" }
    }

    @Benchmark
    fun expand(blackhole: Blackhole) {
        blackhole.consume(expandAll())
    }

    private fun expandAll(): Int {
        var total = 0
        for (rule in parsed) {
            for (ignored in rule.expand(RruleRules.start, RruleRules.start, RruleRules.end, RruleRules.options)) {
                total++
            }
        }
        return total
    }
}

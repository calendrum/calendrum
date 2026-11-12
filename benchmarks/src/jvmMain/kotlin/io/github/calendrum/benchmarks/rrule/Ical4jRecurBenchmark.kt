package io.github.calendrum.benchmarks.rrule

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
import java.time.LocalDateTime
import net.fortuna.ical4j.model.Recur as Ical4jRecur

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class Ical4jRecurBenchmark {
    @Param("simple", "complex")
    var rules: String = RruleRules.SIMPLE

    private var parsed: List<Ical4jRecur<LocalDateTime>> = emptyList()

    @Setup
    fun setUp() {
        parsed = RivalRrules.ical4j(rules)
        val total = parsed.sumOf { RivalRrules.ical4jCount(it) }
        check(total == RivalRrules.expectedIcal4jTotals.getValue(rules)) {
            "$rules: ical4j expanded $total occurrences"
        }
    }

    @Benchmark
    fun expand(blackhole: Blackhole) {
        var total = 0
        for (rule in parsed) total += RivalRrules.ical4jCount(rule)
        blackhole.consume(total)
    }
}

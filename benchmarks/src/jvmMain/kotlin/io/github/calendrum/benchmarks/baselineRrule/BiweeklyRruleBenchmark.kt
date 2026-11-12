package io.github.calendrum.benchmarks.baselineRrule

import biweekly.component.VEvent
import biweekly.io.TimezoneInfo
import io.github.calendrum.benchmarks.baseline.BiweeklyBaseline
import io.github.calendrum.benchmarks.baseline.baselineInputs
import io.github.calendrum.benchmarks.baseline.expectedOccurrenceTotals
import io.github.calendrum.benchmarks.baseline.requireRruleCorpus
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
class BiweeklyRruleBenchmark {
    @Param("personal", "university")
    var corpus: String = Corpus.PERSONAL

    private var timezoneInfo: TimezoneInfo = TimezoneInfo()
    private var masters: List<VEvent> = emptyList()

    @Setup
    fun setUp() {
        requireRruleCorpus(corpus)
        val calendar = BiweeklyBaseline.parse(baselineInputs(corpus).single())
        timezoneInfo = calendar.timezoneInfo
        masters = BiweeklyBaseline.recurringMasters(calendar)
        val total = masters.sumOf { BiweeklyBaseline.occurrences(timezoneInfo, it) }
        check(total == expectedOccurrenceTotals.getValue(corpus)) { "$corpus: expanded $total occurrences" }
    }

    @Benchmark
    fun rrule(blackhole: Blackhole) {
        var total = 0
        for (master in masters) {
            total += BiweeklyBaseline.occurrences(timezoneInfo, master)
        }
        blackhole.consume(total)
    }
}

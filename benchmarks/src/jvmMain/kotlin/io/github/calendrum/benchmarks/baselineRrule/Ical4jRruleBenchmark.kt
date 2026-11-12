package io.github.calendrum.benchmarks.baselineRrule

import io.github.calendrum.benchmarks.baseline.Ical4jBaseline
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
import net.fortuna.ical4j.model.component.VEvent

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class Ical4jRruleBenchmark {
    @Param("personal", "university")
    var corpus: String = Corpus.PERSONAL

    private var masters: List<VEvent> = emptyList()

    @Setup
    fun setUp() {
        requireRruleCorpus(corpus)
        val calendar = Ical4jBaseline.parse(baselineInputs(corpus).single())
        masters = Ical4jBaseline.recurringMasters(calendar)
        val total = masters.sumOf { Ical4jBaseline.occurrences(it).size }
        check(total == expectedOccurrenceTotals.getValue(corpus)) { "$corpus: expanded $total occurrences" }
    }

    @Benchmark
    fun rrule(blackhole: Blackhole) {
        var total = 0
        for (master in masters) {
            val occurrences = Ical4jBaseline.occurrences(master)
            total += occurrences.size
            blackhole.consume(occurrences)
        }
        blackhole.consume(total)
    }
}

package io.github.calendrum.benchmarks.baselineWrite

import biweekly.ICalendar
import io.github.calendrum.benchmarks.baseline.BiweeklyBaseline
import io.github.calendrum.benchmarks.baseline.baselineInputs
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
import java.io.ByteArrayOutputStream

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class BiweeklyWriteBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private var calendars: List<ICalendar> = emptyList()
    private val output: ByteArrayOutputStream = ByteArrayOutputStream()

    @Setup
    fun setUp() {
        calendars = baselineInputs(corpus).map(BiweeklyBaseline::parse)
        requireGeneratorCounts(corpus, BiweeklyBaseline.NAME, BiweeklyBaseline.counts(calendars))
    }

    @Benchmark
    fun write(blackhole: Blackhole) {
        output.reset()
        for (calendar in calendars) {
            BiweeklyBaseline.write(calendar, output)
        }
        blackhole.consume(output.size())
    }
}

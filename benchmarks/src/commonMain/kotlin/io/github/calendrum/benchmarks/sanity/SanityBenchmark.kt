package io.github.calendrum.benchmarks.sanity

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

// Checks the pipeline code -> runner -> JSON report on every platform; it measures nothing useful. `setUp` verifies the
// logical line count once per platform.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class SanityBenchmark {
    private var ics: String = ""

    @Setup
    fun setUp() {
        ics = RFC5545_GROUP_MEETING
        val lines = countLogicalLines(ics.encodeToByteArray())
        check(lines == RFC5545_GROUP_MEETING_LOGICAL_LINES) {
            "Expected $RFC5545_GROUP_MEETING_LOGICAL_LINES logical lines, got $lines"
        }
    }

    @Benchmark
    fun encodeAndCountLines(blackhole: Blackhole) {
        blackhole.consume(countLogicalLines(ics.encodeToByteArray()))
    }
}

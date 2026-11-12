@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.selective

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.Selection
import io.github.calendrum.syntax.filterSelection
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
class RealSelectiveBenchmark {
    @Param(RealCorpus.UNIVERSITY)
    var file: String = RealCorpus.UNIVERSITY

    private val full: ICalParser = ICal.parser()
    private val schedule: ICalParser = ICal.parser { select(Selection.Schedule) }
    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = RealCorpus.bytes(file)
        val tree = full.parse(bytes)
        val selected = schedule.parse(bytes)
        check(selected.isPartial) { "$file: the schedule is not partial" }
        check(nodes(selected) in 1 until nodes(tree)) { "$file: the schedule read nothing or all" }
        check(selected.components == filterSelection(tree.components, Selection.Schedule)) {
            "$file: the schedule differs from the filtered full tree"
        }
    }

    @Benchmark
    fun full(blackhole: Blackhole) {
        blackhole.consume(full.parse(bytes))
    }

    @Benchmark
    fun schedule(blackhole: Blackhole) {
        blackhole.consume(schedule.parse(bytes))
    }
}

@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.selective

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ParseResult
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
class SelectiveBenchmark {
    @Param("personal", "university", "huge")
    var corpus: String = Corpus.PERSONAL

    private val full: ICalParser = ICal.parser()
    private val schedule: ICalParser = ICal.parser { select(Selection.Schedule) }
    private val syncMetadata: ICalParser = ICal.parser { select(Selection.SyncMetadata) }
    private val freeBusy: ICalParser = ICal.parser { select(Selection.FreeBusy) }
    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        val tree = full.parse(bytes)
        for (parser in listOf(schedule, syncMetadata, freeBusy)) {
            val selected = parser.parse(bytes)
            check(selected.isPartial) { "$corpus: ${parser.options.selection} is not partial" }
            check(nodes(selected) in 1 until nodes(tree)) { "$corpus: ${parser.options.selection} read nothing or all" }
            if (corpus != Corpus.HUGE) {
                check(selected.components == filterSelection(tree.components, parser.options.selection)) {
                    "$corpus: ${parser.options.selection} differs from the filtered full tree"
                }
            }
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

    @Benchmark
    fun syncMetadata(blackhole: Blackhole) {
        blackhole.consume(syncMetadata.parse(bytes))
    }

    @Benchmark
    fun freeBusy(blackhole: Blackhole) {
        blackhole.consume(freeBusy.parse(bytes))
    }
}

internal fun nodes(result: ParseResult): Int {
    var count = 0
    val stack = ArrayList<ICalComponent>(result.components)
    while (stack.isNotEmpty()) {
        val component = stack.removeAt(stack.size - 1)
        count += 1 + component.properties.size
        stack.addAll(component.components)
    }
    return count
}

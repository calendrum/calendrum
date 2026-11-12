package io.github.calendrum.benchmarks.parse

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ParseResult
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

// The public parse into a tree. `tree` leaves the values alone: nodes keep byte ranges, so it measures the tree itself.
// `treeReadAll` reads the value of every property and the values of every parameter, which decodes and caches them:
// closer to what ical4j and biweekly do while parsing, but they also convert values to their types. `@Setup` checks
// that there are no problems above `Info` and the number of VEVENT against the reference.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class ParseBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val parser: ICalParser = ICal.parser()
    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        val result = parser.parse(bytes)
        val problems = result.problems.filter { it.severity != Severity.Info }
        check(problems.isEmpty()) { "$corpus: unexpected problems $problems" }
        val expected = expectedCounts.getValue(corpus).components.getValue(EVENT)
        val events = countComponents(result, EVENT)
        check(events == expected) { "$corpus: expected $expected $EVENT, got $events" }
        check(readAll(result) > 0) { "$corpus: nothing to read" }
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

internal fun countComponents(result: ParseResult, name: String): Int {
    var count = 0
    walk(result) { component ->
        if (component.name == name) count++
    }
    return count
}

internal fun readAll(result: ParseResult): Int {
    var total = 0
    walk(result) { component ->
        for (property in component.properties) {
            total += property.value.length
            for (parameter in property.parameters) {
                for (value in parameter.values) total += value.length
            }
        }
    }
    return total
}

// An explicit stack instead of recursion, like the tree itself.
private inline fun walk(result: ParseResult, visit: (ICalComponent) -> Unit) {
    val stack = ArrayList<ICalComponent>(result.components)
    while (stack.isNotEmpty()) {
        val component = stack.removeAt(stack.lastIndex)
        visit(component)
        stack.addAll(component.components)
    }
}

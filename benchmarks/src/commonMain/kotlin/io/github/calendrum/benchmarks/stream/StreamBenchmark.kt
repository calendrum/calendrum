package io.github.calendrum.benchmarks.stream

import io.github.calendrum.ICal
import io.github.calendrum.asByteInput
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ICalParser
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
class StreamBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val parser: ICalParser = ICal.parser()
    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        val expected = expectedComponents(parser, bytes)
        check(expected > 0) { "$corpus: no components" }
        check(countInput() == expected && countArray() == expected) {
            "$corpus: components ${countInput()} from ByteInput, ${countArray()} from ByteArray, expected $expected"
        }
        check(eventsInput() == eventsArray()) { "$corpus: cursor events differ" }
    }

    @Benchmark
    fun components(blackhole: Blackhole) {
        blackhole.consume(countInput())
    }

    @Benchmark
    fun componentsArray(blackhole: Blackhole) {
        blackhole.consume(countArray())
    }

    @Benchmark
    fun cursorInput(blackhole: Blackhole) {
        blackhole.consume(eventsInput())
    }

    @Benchmark
    fun cursorArray(blackhole: Blackhole) {
        blackhole.consume(eventsArray())
    }

    private fun countInput(): Int {
        var total = 0
        for (component in parser.components(bytes.asByteInput())) total += component.properties.size + 1
        return total
    }

    private fun countArray(): Int {
        var total = 0
        for (component in parser.components(bytes)) total += component.properties.size + 1
        return total
    }

    private fun eventsInput(): Int {
        val reader = parser.reader(bytes.asByteInput())
        var total = 0
        while (true) {
            val type = reader.next() ?: break
            total += type.ordinal + reader.depth
        }
        return total
    }

    private fun eventsArray(): Int {
        val reader = parser.reader(bytes)
        var total = 0
        while (true) {
            val type = reader.next() ?: break
            total += type.ordinal + reader.depth
        }
        return total
    }
}

internal fun expectedComponents(parser: ICalParser, bytes: ByteArray): Int {
    var total = 0
    for (top in parser.parse(bytes).components) {
        val children = if (top.name == "VCALENDAR") top.components else listOf(top)
        for (component in children) total += component.properties.size + 1
    }
    return total
}

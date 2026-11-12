package io.github.calendrum.benchmarks.cursor

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.syntax.ContentEventType
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

// `ICalReader` over every corpus: `events` touches only the event type and depth, `values` decodes every value into the
// reusable view, `valueStrings` makes a string of every value. `@Setup` checks the properties and components against
// the reference and that both value paths see the same characters.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class CursorBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val parser: ICalParser = ICal.parser()
    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        val difference = cursorCounts(parser, bytes).difference(expectedCounts.getValue(corpus))
        check(difference == null) { "$corpus: $difference" }
        val strings = readValueStrings()
        check(strings > 0 && strings == readValues()) { "$corpus: values ${readValues()}, strings $strings" }
    }

    @Benchmark
    fun events(blackhole: Blackhole) {
        blackhole.consume(readEvents())
    }

    @Benchmark
    fun values(blackhole: Blackhole) {
        blackhole.consume(readValues())
    }

    @Benchmark
    fun valueStrings(blackhole: Blackhole) {
        blackhole.consume(readValueStrings())
    }

    private fun readEvents(): Int {
        val reader = parser.reader(bytes)
        var total = 0
        while (true) {
            val type = reader.next() ?: break
            total += type.ordinal + reader.depth
        }
        return total
    }

    private fun readValues(): Int {
        val reader = parser.reader(bytes)
        var total = 0
        while (true) {
            val type = reader.next() ?: break
            if (type == ContentEventType.Property) total += reader.value.length
        }
        return total
    }

    private fun readValueStrings(): Int {
        val reader = parser.reader(bytes)
        var total = 0
        while (true) {
            val type = reader.next() ?: break
            if (type == ContentEventType.Property) total += reader.valueString().length
        }
        return total
    }
}

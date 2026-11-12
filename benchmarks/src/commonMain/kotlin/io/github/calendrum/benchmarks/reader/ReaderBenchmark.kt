@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.reader

import io.github.calendrum.InternalICalApi
import io.github.calendrum.ProblemListener
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ContentLexer
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ContentSelection
import io.github.calendrum.syntax.ParseLimits
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

// The `reader` suite: the lexer alone, then the structural reader with the Full, Schedule and SyncMetadata selections
// over every corpus. No value strings are created: each operation folds ids, depths and value lengths into an Int for
// the Blackhole.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class ReaderBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private var bytes: ByteArray = ByteArray(0)
    private var problems: Int = 0
    private val sink: ProblemListener = ProblemListener { problems++ }
    private val schedule: ContentSelection = selectionOf(scheduleComponents, scheduleProperties)
    private val syncMetadata: ContentSelection = selectionOf(syncMetadataComponents, syncMetadataProperties)

    // Equivalence check before measuring: no problems, and the line and property counts of the reference table.
    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        val expected = expectedCounts.getValue(corpus)
        val readout = readCorpus(bytes)
        check(readout.problems.isEmpty()) { "$corpus: unexpected problems ${readout.problems}" }
        check(
            readout.counts.logicalLines == expected.logicalLines && readout.counts.properties == expected.properties,
        ) {
            "$corpus: expected ${expected.describe()}, got ${readout.counts.describe()}"
        }
        check(lex(bytes) == expected.logicalLines) { "$corpus: lexer line count differs" }
        problems = 0
    }

    @Benchmark
    fun lex(blackhole: Blackhole) {
        blackhole.consume(lex(bytes))
    }

    @Benchmark
    fun readFull(blackhole: Blackhole) {
        blackhole.consume(read(ContentSelection.Full))
    }

    @Benchmark
    fun readSchedule(blackhole: Blackhole) {
        blackhole.consume(read(schedule))
    }

    @Benchmark
    fun readSyncMetadata(blackhole: Blackhole) {
        blackhole.consume(read(syncMetadata))
    }

    // Returns the line count; the sum of name ids only takes part in a comparison that is never true, so the JIT cannot
    // drop the lookups.
    private fun lex(input: ByteArray): Int {
        val lexer = ContentLexer(input, ParseLimits.Default, sink)
        var lines = 0
        var ids = 0
        while (true) {
            val line = lexer.next() ?: break
            lines++
            ids += line.nameId
        }
        return if (ids == Int.MIN_VALUE) -lines else lines
    }

    private fun read(selection: ContentSelection): Int {
        val reader = ContentReader(bytes, selection, ParseLimits.Default, sink)
        var result = 0
        while (true) {
            when (reader.next()) {
                ContentReader.END_OF_INPUT -> return result

                ContentReader.PROPERTY -> {
                    val line = reader.line
                    result += line.nameId + line.valueEnd - line.valueStart + line.parameterCount
                }

                else -> result += reader.componentId + reader.depth
            }
        }
    }
}

@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.writer

import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ByteLineWriter
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

// The `writer` suite: every logical line of a corpus, read beforehand into plain strings, is written into the reused
// byte buffer of ContentLineWriter. The Blackhole gets the output size.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class WriterBenchmark {
    @Param("personal", "university", "huge")
    var corpus: String = Corpus.PERSONAL

    private var lines: CorpusLines = CorpusLines.EMPTY
    private val writer: ByteLineWriter = ByteLineWriter()

    // Equivalence check before measuring: the output, read back, gives the same lines without problems, and it is byte
    // for byte the generated corpus, so the output MB/s is the corpus size times operations per second.
    @Setup
    fun setUp() {
        val bytes = corpusBytes(corpus)
        lines = CorpusLines.read(bytes)
        writeAll()
        val written = writer.toByteArray()
        val difference = lines.firstDifference(written)
        check(difference == null) { "$corpus: written lines read back differently: $difference" }
        check(written.contentEquals(bytes)) { "$corpus: written bytes differ from the corpus" }
    }

    @Benchmark
    fun write(blackhole: Blackhole) {
        blackhole.consume(writeAll())
    }

    private fun writeAll(): Int {
        writer.reset()
        lines.writeTo(writer)
        return writer.size
    }
}

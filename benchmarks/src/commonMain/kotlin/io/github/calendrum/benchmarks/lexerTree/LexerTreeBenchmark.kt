@file:OptIn(ExperimentalICalApi::class, InternalICalApi::class)

package io.github.calendrum.benchmarks.lexerTree

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.ProblemListener
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ContentSelection
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseOptions
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

// `tree`/`readFull` without and with an empty interceptor in one run, so the pairs compare with each other (BENCHMARKS
// §3). The setup compares the trees except on `huge`, where two trees of 100 MB do not fit the JS heap.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class LexerTreeBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val empty: LineInterceptor = LineInterceptor { }
    private val plain: ICalParser = ICal.parser()
    private val intercepted: ICalParser = ICal.parser(ParseOptions.builder().intercept(empty).build())
    private var bytes: ByteArray = ByteArray(0)
    private var problems: Int = 0
    private val sink: ProblemListener = ProblemListener { problems++ }

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        if (corpus != Corpus.HUGE) {
            check(plain.parse(bytes).components == intercepted.parse(bytes).components) {
                "$corpus: the empty interceptor changed the tree"
            }
        }
        val lines = read(null)
        check(lines == read(empty)) { "$corpus: the empty interceptor changed the event stream" }
        check(problems == 0) { "$corpus: unexpected problems" }
    }

    @Benchmark
    fun tree(blackhole: Blackhole) {
        blackhole.consume(plain.parse(bytes))
    }

    @Benchmark
    fun treeIntercepted(blackhole: Blackhole) {
        blackhole.consume(intercepted.parse(bytes))
    }

    @Benchmark
    fun readFull(blackhole: Blackhole) {
        blackhole.consume(read(null))
    }

    @Benchmark
    fun readFullIntercepted(blackhole: Blackhole) {
        blackhole.consume(read(empty))
    }

    private fun read(interceptor: LineInterceptor?): Int {
        val reader = ContentReader(bytes, ContentSelection.Full, ParseLimits.Default, sink)
        if (interceptor != null) reader.intercept(interceptor)
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

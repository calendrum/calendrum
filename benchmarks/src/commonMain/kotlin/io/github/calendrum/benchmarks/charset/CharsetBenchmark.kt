@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.charset

import io.github.calendrum.InternalICalApi
import io.github.calendrum.ProblemListener
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ContentLexer
import io.github.calendrum.syntax.DecodedInput
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.decodeInput
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

// The `charset` suite: the corpus re-encoded to windows-1251 in `@Setup`, decoded and lexed with detection and with a
// hint, and the UTF-8 corpus through the detecting constructor. `@Setup` checks the decoded text byte by byte against
// the corpus with the same replacements and the line count against UTF-8.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class CharsetBenchmark {
    @Param("personal")
    var corpus: String = Corpus.PERSONAL

    private var utf8: ByteArray = ByteArray(0)
    private var windows1251: ByteArray = ByteArray(0)
    private val sink: ProblemListener = ProblemListener { }

    @Setup
    fun setUp() {
        utf8 = corpusBytes(corpus)
        val text = utf8.decodeToString()
        windows1251 = Windows1251.encode(text)
        val expected = Windows1251.lossy(text).encodeToByteArray()
        val detected = decodeInput(windows1251, null) { }
        check(detected.charset == WINDOWS_1251 && detected.source == DecodedInput.SOURCE_HEURISTIC) {
            "$corpus: detected $detected"
        }
        check(detected.decodedEquals(expected)) { "$corpus: detected text differs from the corpus" }
        check(decodeInput(windows1251, WINDOWS_1251) { }.decodedEquals(expected)) { "$corpus: hinted text differs" }
        val lines = lex(ContentLexer(utf8, ParseLimits.Default, sink))
        check(lex(ContentLexer(windows1251, null, ParseLimits.Default, sink)) == lines) { "$corpus: detected lines" }
        check(lex(ContentLexer(windows1251, WINDOWS_1251, ParseLimits.Default, sink)) == lines) {
            "$corpus: hinted lines"
        }
        check(lex(ContentLexer(utf8, null, ParseLimits.Default, sink)) == lines) { "$corpus: UTF-8 lines" }
    }

    @Benchmark
    fun lexDetected(blackhole: Blackhole) {
        blackhole.consume(lex(ContentLexer(windows1251, null, ParseLimits.Default, sink)))
    }

    @Benchmark
    fun lexHinted(blackhole: Blackhole) {
        blackhole.consume(lex(ContentLexer(windows1251, WINDOWS_1251, ParseLimits.Default, sink)))
    }

    @Benchmark
    fun lexUtf8Detected(blackhole: Blackhole) {
        blackhole.consume(lex(ContentLexer(utf8, null, ParseLimits.Default, sink)))
    }

    private fun lex(lexer: ContentLexer): Int {
        var lines = 0
        var ids = 0
        while (true) {
            val line = lexer.next() ?: break
            lines++
            ids += line.nameId
        }
        return if (ids == Int.MIN_VALUE) -lines else lines
    }

    private fun DecodedInput.decodedEquals(expected: ByteArray): Boolean =
        length == expected.size && bytes.copyOfRange(offset, offset + length).contentEquals(expected)

    private companion object {
        const val WINDOWS_1251 = "windows-1251"
    }
}

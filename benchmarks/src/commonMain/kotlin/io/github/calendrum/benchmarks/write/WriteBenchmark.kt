package io.github.calendrum.benchmarks.write

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalWriter
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

// `encodeToByteArray` of the parsed tree of a corpus: `byteStable` copies the source lines, `canonical` writes every
// line again from the decoded strings. `@Setup` checks that both outputs equal the corpus, which also leaves every
// value decoded and cached in the tree, so `canonical` measures writing, not decoding.
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class WriteBenchmark {
    @Param("invite", "invite-batch", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val writer: ICalWriter = ICal.writer()
    private var components: List<ICalComponent> = emptyList()

    @Setup
    fun setUp() {
        val bytes = corpusBytes(corpus)
        components = ICal.parse(bytes).components
        val stable = firstDifference(bytes, writer.encodeToByteArray(components))
        check(stable == null) { "$corpus: byte-stable output: $stable" }
        val canonical = firstDifference(bytes, canonicalWriter.encodeToByteArray(components))
        check(canonical == null) { "$corpus: canonical output: $canonical" }
    }

    @Benchmark
    fun byteStable(blackhole: Blackhole) {
        blackhole.consume(writer.encodeToByteArray(components))
    }

    @Benchmark
    fun canonical(blackhole: Blackhole) {
        blackhole.consume(canonicalWriter.encodeToByteArray(components))
    }
}

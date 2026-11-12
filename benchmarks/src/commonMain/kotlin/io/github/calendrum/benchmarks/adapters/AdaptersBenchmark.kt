package io.github.calendrum.benchmarks.adapters

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParser
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
import io.github.calendrum.kotlinxio.components as kotlinxComponents
import io.github.calendrum.kotlinxio.parse as kotlinxParse
import io.github.calendrum.kotlinxio.write as kotlinxWrite
import io.github.calendrum.okio.components as okioComponents
import io.github.calendrum.okio.parse as okioParse
import io.github.calendrum.okio.write as okioWrite
import kotlinx.io.Buffer as KotlinxBuffer
import okio.Buffer as OkioBuffer

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class AdaptersBenchmark {
    @Param("invite", "personal", "university", "huge")
    var corpus: String = Corpus.INVITE

    private val parser: ICalParser = ICal.parser()
    private val writer: ICalWriter = ICal.writer()
    private var bytes: ByteArray = ByteArray(0)
    private var tree: List<ICalComponent> = emptyList()

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        tree = parser.parse(bytes).components
        val expected = parseArray()
        check(expected > 0) { "$corpus: no properties" }
        check(parseKotlinxIo() == expected && parseOkio() == expected) {
            "$corpus: kotlinx-io ${parseKotlinxIo()}, Okio ${parseOkio()}, ByteArray $expected properties"
        }
        val components = componentsArray()
        check(componentsKotlinxIo() == components && componentsOkio() == components) {
            "$corpus: component streams differ"
        }
        val written = writer.encodeToByteArray(tree).size.toLong()
        check(writeKotlinxIo() == written && writeOkio() == written) { "$corpus: written sizes differ" }
    }

    @Benchmark
    fun parseBytes(blackhole: Blackhole) {
        blackhole.consume(parseArray())
    }

    @Benchmark
    fun parseKotlinxSource(blackhole: Blackhole) {
        blackhole.consume(parseKotlinxIo())
    }

    @Benchmark
    fun parseOkioSource(blackhole: Blackhole) {
        blackhole.consume(parseOkio())
    }

    @Benchmark
    fun componentsKotlinxSource(blackhole: Blackhole) {
        blackhole.consume(componentsKotlinxIo())
    }

    @Benchmark
    fun componentsOkioSource(blackhole: Blackhole) {
        blackhole.consume(componentsOkio())
    }

    @Benchmark
    fun writeBytes(blackhole: Blackhole) {
        blackhole.consume(writer.encodeToByteArray(tree).size)
    }

    @Benchmark
    fun writeKotlinxSink(blackhole: Blackhole) {
        blackhole.consume(writeKotlinxIo())
    }

    @Benchmark
    fun writeOkioSink(blackhole: Blackhole) {
        blackhole.consume(writeOkio())
    }

    private fun parseArray(): Int = properties(parser.parse(bytes).components)

    private fun parseKotlinxIo(): Int = properties(
        parser.kotlinxParse(
            KotlinxBuffer().apply {
                write(bytes)
            },
        ).components,
    )

    private fun parseOkio(): Int = properties(parser.okioParse(OkioBuffer().write(bytes)).components)

    private fun componentsArray(): Int {
        var total = 0
        for (component in parser.components(bytes)) total += component.properties.size + 1
        return total
    }

    private fun componentsKotlinxIo(): Int {
        var total = 0
        for (component in parser.kotlinxComponents(KotlinxBuffer().apply { write(bytes) })) {
            total += component.properties.size + 1
        }
        return total
    }

    private fun componentsOkio(): Int {
        var total = 0
        for (component in parser.okioComponents(OkioBuffer().write(bytes))) total += component.properties.size + 1
        return total
    }

    private fun writeKotlinxIo(): Long {
        val sink = KotlinxBuffer()
        writer.kotlinxWrite(tree, sink)
        return sink.size
    }

    private fun writeOkio(): Long {
        val sink = OkioBuffer()
        writer.okioWrite(tree, sink)
        return sink.size
    }

    private fun properties(components: List<ICalComponent>): Int {
        var total = 0
        for (component in components) total += component.properties.size + properties(component.components)
        return total
    }
}

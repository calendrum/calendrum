package io.github.calendrum.benchmarks.schema

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
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
class SchemaBenchmark {
    @Param("personal", "university", "huge")
    var corpus: String = Corpus.PERSONAL

    private var bytes: ByteArray = ByteArray(0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        setUpSchema(corpus, bytes)
    }

    @Benchmark
    fun schema(blackhole: Blackhole) {
        blackhole.consume(ICal.parser().decode(bytes, LessonSchema))
    }

    @Benchmark
    fun wrappers(blackhole: Blackhole) {
        blackhole.consume(lessonsFromWrappers(bytes))
    }
}

internal fun setUpSchema(corpus: String, bytes: ByteArray): List<Lesson> {
    val lessons = ICal.parser().decode(bytes, LessonSchema)
    check(lessons.isNotEmpty()) { "$corpus: the schema read no lessons" }
    if (corpus != Corpus.HUGE) {
        check(lessons == lessonsFromWrappers(bytes)) { "$corpus: the schema differs from the wrappers" }
    }
    return lessons
}

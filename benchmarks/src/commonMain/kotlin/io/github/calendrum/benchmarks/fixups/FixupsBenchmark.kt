@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.benchmarks.fixups

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.fixup.Fixup
import io.github.calendrum.fixup.Fixups
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalParser
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

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class FixupsBenchmark {
    @Param("invite-batch", "personal")
    var corpus: String = Corpus.PERSONAL

    @Param("byProdId", "outlook", "google")
    var fixup: String = "byProdId"

    private var bytes: ByteArray = ByteArray(0)
    private var chosen: Fixup = Fixups.byProdId()
    private var plain: ICalParser = ICal.parser()
    private var fixing: ICalParser = ICal.parser()

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        chosen = when (fixup) {
            "outlook" -> Fixups.outlook
            "google" -> Fixups.google
            else -> Fixups.byProdId()
        }
        val options = ParseOptions.builder()
        for (factory in chosen.interceptors()) options.intercept(factory)
        fixing = ICal.parser(options.build())
        val calendars = fixing.parse(bytes).calendars
        check(calendars.isNotEmpty()) { "$corpus has no calendars" }
        for (calendar in calendars) {
            val first = chosen.apply(calendar)
            check(!chosen.apply(first.calendar).isChanged) { "$fixup is not idempotent on $corpus" }
        }
    }

    @Benchmark
    fun parse(blackhole: Blackhole) {
        blackhole.consume(plain.parse(bytes).calendars)
    }

    @Benchmark
    fun parseAndFix(blackhole: Blackhole) {
        for (calendar in fixing.parse(bytes).calendars) blackhole.consume(chosen.apply(calendar))
    }
}

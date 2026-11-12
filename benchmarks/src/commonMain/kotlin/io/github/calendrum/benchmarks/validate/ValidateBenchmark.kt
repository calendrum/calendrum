package io.github.calendrum.benchmarks.validate

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.Validator
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
class ValidateBenchmark {
    @Param("invite", "personal", "university")
    var corpus: String = Corpus.UNIVERSITY

    private var calendars: List<VCalendar> = emptyList()

    @Setup
    fun setUp() {
        calendars = ICal.parse(corpusBytes(corpus)).calendars
        check(calendars.isNotEmpty()) { "$corpus has no calendars" }
        val rules = RuleSet.Rfc5545
        val issues = calendars.sumOf { Validator.validate(it, rules).issues.size }
        val lint = calendars.sumOf { Linter.lint(it).issues.size }
        check(lint >= issues) { "$corpus: lint found $lint, validation $issues" }
    }

    @Benchmark
    fun validate(blackhole: Blackhole) {
        for (calendar in calendars) blackhole.consume(Validator.validate(calendar, RuleSet.Rfc5545))
    }

    @Benchmark
    fun lint(blackhole: Blackhole) {
        for (calendar in calendars) blackhole.consume(Linter.lint(calendar))
    }
}

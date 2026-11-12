@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.benchmarks.feedPublisher

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.model.Canonical
import io.github.calendrum.model.FeedPublisher
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
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
import kotlin.time.Clock
import kotlin.time.Instant

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class FeedPublisherBenchmark {
    @Param("personal", "university")
    var corpus: String = Corpus.PERSONAL

    private val publisher = FeedPublisher(
        object : Clock {
            override fun now(): Instant = Instant.parse("2026-10-01T08:00:00Z")
        },
    )
    private var previous: VCalendar? = null
    private var current: VCalendar? = null
    private var changed: VCalendar? = null

    @Setup
    fun setUp() {
        val calendar = ICal.parse(corpusBytes(corpus)).calendars.single()
        val published = publisher.publish(null, calendar)
        previous = published
        current = calendar
        val first = calendar.events.first()
        changed = calendar.toBuilder().apply { events.replace(first, first.edit { summary = "Changed" }) }.build()
        val same = publisher.publish(published, calendar)
        check(ICal.writer().encodeToString(same.component) == ICal.writer().encodeToString(published.component)) {
            "$corpus: an unchanged feed changed"
        }
        val next = publisher.publish(published, changed!!)
        check(next.events.first().sequence == (first.sequence ?: 0) + 1) { "$corpus: SEQUENCE did not grow" }
    }

    @Benchmark
    fun unchanged(blackhole: Blackhole) {
        blackhole.consume(publisher.publish(previous, current!!))
    }

    @Benchmark
    fun oneChanged(blackhole: Blackhole) {
        blackhole.consume(publisher.publish(previous, changed!!))
    }

    @Benchmark
    fun canonicalize(blackhole: Blackhole) {
        blackhole.consume(Canonical.canonicalize(current!!))
    }
}

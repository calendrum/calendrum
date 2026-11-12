package io.github.calendrum.recur

import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.fail

class MillionRulesStressTest {
    @Test
    fun aMillionRandomRulesExpandWithoutHanging() {
        val rules = RandomRules(System.getProperty("calendrum.stress.seed")?.toLong() ?: 20261017L)
        val total = System.getProperty("calendrum.stress.rules")?.toInt() ?: 1_000_000
        val options = RecurrenceOptions.builder()
            .limits(RecurrenceLimits.builder().maxOccurrences(500).build())
            .build()
        val from = ICalDateTime.Floating(LocalDateTime(2031, 1, 1, 0, 0))
        val to = ICalDateTime.Floating(LocalDateTime(2032, 1, 1, 0, 0))
        var slowest = 0L
        var slowestRule = ""
        var occurrences = 0L
        val started = System.nanoTime()
        repeat(total) {
            val sample = rules.next()
            val before = System.nanoTime()
            val values = sample.rule.expand(sample.start, options).take(TAKE).toList()
            occurrences += values.size + sample.rule.expand(sample.start, from, to, options).count()
            val broken = checkInvariants(sample, values)
            if (broken != null) fail("${sample.text} from ${sample.start}: $broken")
            val spent = System.nanoTime() - before
            if (spent > slowest) {
                slowest = spent
                slowestRule = "${sample.text} from ${sample.start}"
            }
        }
        val seconds = (System.nanoTime() - started) / 1e9
        println("rules: $total, occurrences: $occurrences, time: ${"%.1f".format(seconds)} s")
        println("slowest rule: ${slowest / 1_000_000} ms — $slowestRule")
        if (slowest > MAX_NANOS) fail("A rule took ${slowest / 1_000_000} ms: $slowestRule")
    }

    private companion object {
        const val TAKE = 200
        const val MAX_NANOS = 2_000_000_000L
    }
}

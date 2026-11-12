package io.github.calendrum.benchmarks.rrule

import io.github.calendrum.recur.expand
import kotlin.test.Test
import kotlin.test.assertEquals

class RruleBenchmarkSetUpTest {
    @Test
    fun everySetUpAgreesOnTheTotals() {
        for (set in listOf(RruleRules.SIMPLE, RruleRules.COMPLEX)) {
            RruleBenchmark().apply { rules = set }.setUp()
            Ical4jRecurBenchmark().apply { rules = set }.setUp()
            BiweeklyRecurBenchmark().apply { rules = set }.setUp()
            val ours = RruleRules.rules(set).map {
                it.expand(RruleRules.start, RruleRules.start, RruleRules.end, RruleRules.options).count()
            }
            assertEquals(ours, RivalRrules.ical4j(set).map { RivalRrules.ical4jCount(it) }, set)
            assertEquals(ours, RivalRrules.biweekly(set).map { RivalRrules.biweeklyCount(it) }, set)
        }
    }
}

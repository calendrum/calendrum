package io.github.calendrum.benchmarks.rrule

import kotlin.test.Test

class RruleSetUpTest {
    @Test
    fun theSetUpExpandsBothRuleSets() {
        for (set in listOf(RruleRules.SIMPLE, RruleRules.COMPLEX)) RruleBenchmark().apply { rules = set }.setUp()
    }
}

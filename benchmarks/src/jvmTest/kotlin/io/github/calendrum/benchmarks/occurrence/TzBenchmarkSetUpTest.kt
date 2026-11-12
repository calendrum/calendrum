package io.github.calendrum.benchmarks.occurrence

import io.github.calendrum.benchmarks.tz.Ical4jTzBenchmark
import io.github.calendrum.benchmarks.tz.JavaTimeTzBenchmark
import kotlin.test.Test

class TzBenchmarkSetUpTest {
    @Test
    fun rivalSetUps() {
        Ical4jTzBenchmark().setUp()
        JavaTimeTzBenchmark().setUp()
    }
}

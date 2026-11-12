package io.github.calendrum.benchmarks.coldStart

import io.github.calendrum.benchmarks.baseline.BiweeklyBaseline
import io.github.calendrum.benchmarks.baseline.Ical4jBaseline
import io.github.calendrum.benchmarks.baseline.ValueSink
import io.github.calendrum.benchmarks.baseline.generatorCounts
import io.github.calendrum.benchmarks.baseline.requireGeneratorCounts
import io.github.calendrum.benchmarks.corpus.Corpus

// An object, not a lambda: the first lambda of a JVM starts the invokedynamic machinery, and the competitors' own code
// starts it inside the timed call; a lambda created before the timer would move that cost out of their measurement.
private object DiscardingSink : ValueSink {
    override fun consume(value: Any?) {
    }
}

internal fun firstParseWithRival(variant: String, input: ByteArray): Long {
    warmClock()
    val sink = DiscardingSink
    return when (variant) {
        ColdStartVariant.ICAL4J -> {
            val result = timed { Ical4jBaseline.parse(input) }
            requireGeneratorCounts(Corpus.INVITE, Ical4jBaseline.NAME, Ical4jBaseline.counts(listOf(result.value)))
            result.nanos
        }

        ColdStartVariant.ICAL4J_READ -> {
            val result = timed { Ical4jBaseline.readAll(Ical4jBaseline.parse(input), sink) }
            checkValues(Ical4jBaseline.NAME, result.value)
            result.nanos
        }

        ColdStartVariant.BIWEEKLY -> {
            val result = timed { BiweeklyBaseline.parse(input) }
            requireGeneratorCounts(Corpus.INVITE, BiweeklyBaseline.NAME, BiweeklyBaseline.counts(listOf(result.value)))
            result.nanos
        }

        ColdStartVariant.BIWEEKLY_READ -> {
            val result = timed { BiweeklyBaseline.readAll(BiweeklyBaseline.parse(input), sink) }
            checkValues(BiweeklyBaseline.NAME, result.value)
            result.nanos
        }

        else -> firstParse(variant, input)
    }
}

private fun checkValues(library: String, values: Int) {
    val expected = generatorCounts(Corpus.INVITE).properties
    check(values == expected) { "$library read $values property values, expected $expected" }
}

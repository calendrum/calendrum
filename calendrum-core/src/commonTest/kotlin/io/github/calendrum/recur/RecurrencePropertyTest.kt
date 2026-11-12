package io.github.calendrum.recur

import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail

class RecurrencePropertyTest {
    @Test
    fun randomRulesKeepTheInvariants() {
        val rules = RandomRules(20261017)
        repeat(RULES) {
            val sample = rules.next()
            val sink = ProblemSink()
            val options = RecurrenceOptions.builder().problemListener(sink).build()
            val occurrences = sample.rule.expand(sample.start, options).take(TAKE).toList()
            val broken = checkInvariants(sample, occurrences)
            if (broken != null) fail("${sample.text} from ${sample.start}: $broken")
        }
    }

    @Test
    fun aWindowIsASliceOfTheWholeSequence() {
        val rules = RandomRules(42)
        val random = Random(7)
        repeat(RULES) {
            val sample = rules.next()
            val sink = ProblemSink()
            val options = RecurrenceOptions.builder().problemListener(sink).build()
            val start = (sample.start as? ICalDateTime.Floating)?.dateTime
                ?: LocalDateTime((sample.start as ICalDateTime.Date).date, kotlinx.datetime.LocalTime(0, 0))
            val fromYear = start.year + random.nextInt(6)
            val from = ICalDateTime.Floating(
                LocalDateTime(fromYear, 1 + random.nextInt(12), 1 + random.nextInt(28), random.nextInt(24), 0),
            )
            val to = ICalDateTime.Floating(
                LocalDateTime(fromYear + 1 + random.nextInt(2), 1 + random.nextInt(12), 1, 0, 0),
            )
            val fromKey = from.localKey()
            val toKey = to.localKey()
            val prefix = sample.rule.expand(sample.start, options)
                .takeWhile { it.localKey() < toKey }
                .take(WINDOW_LIMIT)
                .toList()
            val whole = prefix.filter { it.localKey() >= fromKey }
            if (prefix.size == WINDOW_LIMIT ||
                sink.problems.any { it.code.id == "ICAL-R001" || it.code.id == "ICAL-R002" }
            ) {
                return@repeat
            }
            val window = sample.rule.expand(sample.start, from, to, RecurrenceOptions.Default).toList()
            assertEquals(whole, window, "${sample.text} from ${sample.start} in [$from, $to)")
        }
    }

    @Test
    fun setsAreOrderedWithoutDuplicates() {
        val rules = RandomRules(9)
        repeat(RULES / 4) {
            val first = rules.next()
            val second = rules.next()
            if (first.start::class != second.start::class) return@repeat
            val set = RecurrenceSet.builder(first.start)
                .rule(first.rule)
                .rule(second.rule)
                .date(second.start)
                .exception(second.start)
                .build()
            val keys = Sequence { set.iterator() }.take(TAKE).map { it.localKey() }.toList()
            for (index in 1 until keys.size) {
                if (keys[index] <= keys[index - 1]) fail("${first.text} + ${second.text}: not increasing at $index")
            }
            assertNull(keys.firstOrNull { it == second.start.localKey() && second.start != first.start })
        }
    }

    private companion object {
        const val RULES = 1500
        const val TAKE = 120
        const val WINDOW_LIMIT = 1500
    }
}

package io.github.calendrum.benchmarks.coldStart

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.parseTyped.readModel
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.syntax.ParseResult
import kotlin.time.TimeSource

internal object ColdStartVariant {
    const val FLOOR: String = "floor"
    const val ICS_TREE: String = "ics-tree"
    const val ICS_READ: String = "ics-read"
    const val ICAL4J: String = "ical4j"
    const val ICAL4J_READ: String = "ical4j-read"
    const val BIWEEKLY: String = "biweekly"
    const val BIWEEKLY_READ: String = "biweekly-read"

    val ours: List<String> = listOf(FLOOR, ICS_TREE, ICS_READ)
    val rivals: List<String> = listOf(ICAL4J, ICAL4J_READ, BIWEEKLY, BIWEEKLY_READ)
}

internal class Timed<T>(val value: T, val nanos: Long)

// The first reading of the monotonic clock loads its classes; it happens before the timed call, the same for every
// variant, so that the loading is not counted in the first parse.
internal fun warmClock() {
    TimeSource.Monotonic.markNow().elapsedNow()
}

internal inline fun <T> timed(block: () -> T): Timed<T> {
    val mark = TimeSource.Monotonic.markNow()
    val value = block()
    return Timed(value, mark.elapsedNow().inWholeNanoseconds)
}

// Only the call is timed (the parser or the builder is created inside it); the check against the generator counts runs
// after the timer stops.
internal fun firstParse(variant: String, input: ByteArray): Long {
    warmClock()
    return when (variant) {
        ColdStartVariant.FLOOR -> {
            val result = timed { input.size }
            check(result.value > 0) { "Empty input" }
            result.nanos
        }

        ColdStartVariant.ICS_TREE -> {
            val result = timed { ICal.parser().parse(input) }
            checkInvite(result.value)
            result.nanos
        }

        ColdStartVariant.ICS_READ -> {
            val result = timed { readModel(ICal.parser().parse(input)) }
            check(result.value > 0) { "Nothing was read" }
            result.nanos
        }

        else -> error("Unknown cold-start variant: $variant")
    }
}

internal fun checkInvite(result: ParseResult) {
    val problems = result.problems.filter { it.severity != Severity.Info }
    check(problems.isEmpty()) { "Unexpected problems $problems" }
    var properties = 0
    val stack = ArrayList(result.components)
    while (stack.isNotEmpty()) {
        val component = stack.removeAt(stack.lastIndex)
        properties += component.properties.size
        stack.addAll(component.components)
    }
    val expected = expectedCounts.getValue(Corpus.INVITE).properties
    check(properties == expected) { "Expected $expected properties, got $properties" }
}

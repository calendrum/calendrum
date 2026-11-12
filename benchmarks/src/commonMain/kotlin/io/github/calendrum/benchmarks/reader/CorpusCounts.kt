@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.reader

import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.Severity
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ContentSelection
import io.github.calendrum.syntax.ParseLimits

internal class CorpusCounts(
    val logicalLines: Int,
    val properties: Int,
    val components: Map<String, Int>,
    val selectedProperties: Map<String, Int>,
)

internal val countedComponents: List<String> =
    listOf("VCALENDAR", "VTIMEZONE", "STANDARD", "DAYLIGHT", "VEVENT", "VALARM")

internal val countedProperties: List<String> = listOf("RRULE", "RECURRENCE-ID", "ATTENDEE")

private fun counts(lines: Int, properties: Int, components: List<Int>, selected: List<Int>): CorpusCounts =
    CorpusCounts(lines, properties, countedComponents.zip(components).toMap(), countedProperties.zip(selected).toMap())

// Independent reference counted by a Python validator straight from the corpus files, not through the generator; RRULE
// includes the rules inside VTIMEZONE.
internal val expectedCounts: Map<String, CorpusCounts> = mapOf(
    Corpus.INVITE to counts(45, 35, listOf(1, 1, 1, 0, 1, 1), listOf(0, 0, 6)),
    Corpus.INVITE_BATCH to
        counts(52_219, 40_791, listOf(1_000, 1_000, 1_000, 714, 1_000, 1_000), listOf(1_428, 0, 7_507)),
    Corpus.PERSONAL to counts(77_462, 64_354, listOf(1, 7, 7, 5, 5_000, 1_534), listOf(678, 0, 0)),
    Corpus.UNIVERSITY to counts(382_158, 322_702, listOf(1, 7, 7, 5, 29_708, 0), listOf(12_010, 17_708, 14_462)),
    Corpus.HUGE to counts(2_035_988, 1_665_788, listOf(1, 7, 7, 5, 185_080, 0), listOf(10, 0, 92_540)),
)

internal class CorpusReadout(
    val counts: CorpusCounts,
    val problems: List<Problem>,
    val unknownComponents: Int,
    val otherComponents: Set<String>,
    val unknownCountedProperties: Int,
    val maxDepth: Int,
    val balanced: Boolean,
)

internal fun readCorpus(bytes: ByteArray, selection: ContentSelection = ContentSelection.Full): CorpusReadout {
    val problems = ArrayList<Problem>()
    val reader = ContentReader(bytes, selection, ParseLimits.Default) { problems += it }
    val components = HashMap<String, Int>()
    val selected = HashMap<String, Int>()
    var lines = 0
    var properties = 0
    var unknownComponents = 0
    var unknownProperties = 0
    var depth = 0
    var maxDepth = 0
    var balanced = true
    while (true) {
        when (reader.next()) {
            ContentReader.END_OF_INPUT -> break

            ContentReader.BEGIN -> {
                lines++
                depth++
                if (reader.depth != depth) balanced = false
                if (depth > maxDepth) maxDepth = depth
                if (reader.componentId == ContentReader.UNKNOWN_NAME) unknownComponents++
                val name = reader.componentName()
                components[name] = (components[name] ?: 0) + 1
            }

            ContentReader.END -> {
                lines++
                if (reader.isImplicit || reader.depth != depth) balanced = false
                depth--
            }

            else -> {
                lines++
                properties++
                val line = reader.line
                if (line.nameId != ContentReader.UNKNOWN_NAME) {
                    val name = line.name()
                    if (name in countedProperties) selected[name] = (selected[name] ?: 0) + 1
                } else if (line.name() in countedProperties) {
                    unknownProperties++
                }
            }
        }
    }
    val counted = CorpusCounts(
        logicalLines = lines,
        properties = properties,
        components = countedComponents.associateWith { components[it] ?: 0 },
        selectedProperties = countedProperties.associateWith { selected[it] ?: 0 },
    )
    return CorpusReadout(
        counts = counted,
        problems = problems,
        unknownComponents = unknownComponents,
        otherComponents = components.keys - countedComponents.toSet(),
        unknownCountedProperties = unknownProperties,
        maxDepth = maxDepth,
        balanced = balanced && depth == 0,
    )
}

internal fun CorpusCounts.describe(): String =
    "lines=$logicalLines properties=$properties components=$components selected=$selectedProperties"

internal fun CorpusReadout.problemsAboveInfo(): List<Problem> = problems.filter { it.severity != Severity.Info }

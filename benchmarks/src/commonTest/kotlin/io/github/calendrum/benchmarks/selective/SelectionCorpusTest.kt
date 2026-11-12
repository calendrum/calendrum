@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.selective

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.SplitMix64
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.parser
import io.github.calendrum.syntax.Feature
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.Selection
import io.github.calendrum.syntax.TimeZoneSelection
import io.github.calendrum.syntax.filterSelection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SelectionCorpusTest {
    @Test
    fun personal() {
        setUpSelective(Corpus.PERSONAL)
        assertSelectionsMatch(corpusBytes(Corpus.PERSONAL), seed = 1)
    }

    @Test
    fun invite() {
        assertSelectionsMatch(corpusBytes(Corpus.INVITE), seed = 2)
    }
}

internal fun setUpSelective(corpus: String) {
    val benchmark = SelectiveBenchmark()
    benchmark.corpus = corpus
    benchmark.setUp()
}

internal fun assertSelectionsMatch(bytes: ByteArray, seed: Long, random: Int = 8) {
    val full = ICal.parse(bytes)
    val components = LinkedHashSet<String>()
    val properties = LinkedHashSet<String>()
    val stack = ArrayList<ICalComponent>(full.components)
    while (stack.isNotEmpty()) {
        val component = stack.removeAt(stack.size - 1)
        components += component.name
        for (property in component.properties) properties += property.name
        stack.addAll(component.components)
    }
    components -= setOf("VCALENDAR", "VTIMEZONE", "STANDARD", "DAYLIGHT")
    val generator = SplitMix64(seed)
    val selections = ArrayList<Selection>()
    selections += listOf(Selection.Full, Selection.Schedule, Selection.SyncMetadata, Selection.FreeBusy)
    repeat(random) {
        val builder = Selection.builder()
        builder.components(*components.filter { generator.nextInt(2) == 0 }.toTypedArray())
        builder.properties(*properties.filter { generator.nextInt(3) == 0 }.toTypedArray())
        if (generator.nextInt(3) == 0) builder.need(Feature.Occurrences)
        if (generator.nextInt(3) == 0) builder.need(Feature.Alarms)
        if (generator.nextInt(3) == 0) builder.need(Feature.Participants)
        builder.timeZones(TimeZoneSelection.entries[generator.nextInt(3)])
        selections += builder.build()
    }
    for (selection in selections) {
        val selected = ICal.parser { select(selection) }.parse(bytes)
        assertEquals(selection != Selection.Full, selected.isPartial)
        assertTrue(filterSelection(full.components, selection) == selected.components, "$selection")
    }
}

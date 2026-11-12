package io.github.calendrum.benchmarks.parse

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.benchmarks.reader.countedComponents
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalComponent
import kotlin.test.Test
import kotlin.test.assertEquals

// The tree of each corpus against the reference (properties, components by name), and the tree without the source
// (`retainSource = false`) against the ordinary one, values included.
class ParseCorpusTest {
    @Test
    fun invite() {
        assertTreeMatchesReference(Corpus.INVITE)
    }

    @Test
    fun personal() {
        assertTreeMatchesReference(Corpus.PERSONAL)
    }
}

internal fun assertTreeMatchesReference(corpus: String) {
    val bytes = corpusBytes(corpus)
    val result = ICal.parse(bytes)
    val expected = expectedCounts.getValue(corpus)
    assertEquals(emptyList(), result.problems, corpus)
    val components = HashMap<String, Int>()
    var properties = 0
    val stack = ArrayList<ICalComponent>(result.components)
    while (stack.isNotEmpty()) {
        val component = stack.removeAt(stack.lastIndex)
        components[component.name] = (components[component.name] ?: 0) + 1
        properties += component.properties.size
        stack.addAll(component.components)
    }
    assertEquals(expected.properties, properties, "$corpus: properties")
    assertEquals(expected.components, countedComponents.associateWith { components[it] ?: 0 }, corpus)
    val copied = ICal.parser { retainSource = false }.parse(bytes)
    assertEquals(result.components, copied.components, "$corpus: tree without the source")
    assertEquals(readAll(result), readAll(copied), "$corpus: values")
}

@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.benchmarks.fingerprints

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.parser
import io.github.calendrum.syntax.Fingerprints
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.WriteOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FingerprintCorpusTest {
    @Test
    fun invite() {
        assertFingerprintsMatchTheTree(Corpus.INVITE)
        assertEmptyInterceptorsChangeNothing(Corpus.INVITE)
    }

    @Test
    fun personal() {
        assertFingerprintsMatchTheTree(Corpus.PERSONAL)
        assertEmptyInterceptorsChangeNothing(Corpus.PERSONAL)
    }
}

internal fun assertFingerprintsMatchTheTree(corpus: String) {
    val bytes = corpusBytes(corpus)
    val fingerprints = ICal.fingerprints(bytes)
    val children = ArrayList<ICalComponent>()
    for (top in ICal.parse(bytes).components) {
        if (top.name == "VCALENDAR") children.addAll(top.components) else children += top
    }
    val entries = fingerprints.entries
    assertEquals(children.size, entries.size, "$corpus: entries")
    val parser = ICal.parser()
    for (index in children.indices) {
        val child = children[index]
        val entry = entries[index]
        assertEquals(child.name, entry.key.name, "$corpus: name of entry $index")
        assertEquals(child.span!!.startOffset, entry.startOffset, "$corpus: start of entry $index")
        assertEquals(child.span!!.endOffset, entry.endOffset, "$corpus: end of entry $index")
        val parsed = parser.parseComponent(bytes, entry)
        assertEquals(child, parsed, "$corpus: parseComponent of entry $index")
        assertEquals(child.span!!.line, parsed.span!!.line, "$corpus: line of entry $index")
    }
    val decoded = Fingerprints.decodeFromString(fingerprints.encodeToString())
    assertEquals(fingerprints, decoded, "$corpus: encodeToString round trip")
    val diff = fingerprints.diff(decoded)
    assertEquals(entries.size, diff.unchangedCount, corpus)
    assertTrue(diff.added.isEmpty() && diff.removed.isEmpty() && diff.changed.isEmpty(), corpus)
}

internal fun assertEmptyInterceptorsChangeNothing(corpus: String) {
    val bytes = corpusBytes(corpus)
    val plain = ICal.parse(bytes).components
    val intercepted = ICal.parser { intercept { _ -> } }.parse(bytes).components
    assertEquals(plain, intercepted, "$corpus: tree with an empty interceptor")
    val writer = ICal.writer(WriteOptions.builder().intercept { _ -> }.build())
    assertTrue(bytes.contentEquals(writer.encodeToByteArray(intercepted)), "$corpus: written with an empty interceptor")
}

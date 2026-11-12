package io.github.calendrum.benchmarks.patch

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.model.calendars
import kotlin.test.Test

class PatchCorpusTest {
    @Test
    fun invite() {
        setUpPatch(Corpus.INVITE)
    }
}

internal fun setUpPatch(corpus: String) {
    val benchmark = PatchBenchmark()
    benchmark.corpus = corpus
    benchmark.setUp()
}

class EditRoundTripCorpusTest {
    @Test
    fun personal() {
        checkEditRoundTrip(corpusBytes(Corpus.PERSONAL))
    }
}

internal fun checkEditRoundTrip(bytes: ByteArray) {
    val result = ICal.parse(bytes)
    var edits = 0
    val components = result.components.map { component ->
        if (component.name != "VCALENDAR") return@map component
        val calendar = result.calendars.first { it.component === component }
        calendar.edit {
            for (event in events.items) {
                edits += 1
                events.replace(event, event.edit { sequence = (sequence ?: 0) + 1 })
            }
        }.component
    }
    check(edits > 0) { "No event to edit" }
    val written = ICal.writer().encodeToByteArray(components)
    check(ICal.parse(written).components == components) { "The edited tree did not survive writing" }
    val original = bytes.decodeToString().split("\r\n").filter { !it.startsWith("SEQUENCE:") }
    val lines = written.decodeToString().split("\r\n").toSet()
    check(original.all { it in lines }) { "A line that was not edited changed" }
}

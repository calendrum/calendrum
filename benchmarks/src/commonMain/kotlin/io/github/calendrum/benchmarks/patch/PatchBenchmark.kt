@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.benchmarks.patch

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.model.ICalPatch
import io.github.calendrum.model.Parameters
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.PatchResult
import io.github.calendrum.model.VEvent
import io.github.calendrum.model.calendars
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(BenchmarkTimeUnit.SECONDS)
class PatchBenchmark {
    @Param("invite", "invite-batch", "university")
    var corpus: String = Corpus.INVITE

    private var bytes: ByteArray = ByteArray(0)
    private var target: PatchTarget = PatchTarget("", null, "", PartStat.Tentative, 0, 0)

    @Setup
    fun setUp() {
        bytes = corpusBytes(corpus)
        target = patchTarget(bytes)
        checkOneLine(bytes, target, patch(bytes, target))
        check(editAndWrite(bytes, target).contentEquals(patch(bytes, target).bytes)) {
            "$corpus: the patch differs from an edit written byte-stable"
        }
    }

    @Benchmark
    fun patchOne(blackhole: Blackhole) {
        blackhole.consume(patch(bytes, target).bytes)
    }

    @Benchmark
    fun editOne(blackhole: Blackhole) {
        blackhole.consume(editAndWrite(bytes, target))
    }
}

class PatchTarget(
    val uid: String,
    val recurrenceId: String?,
    val address: String,
    val partStat: PartStat,
    val lineStart: Int,
    val lineEnd: Int,
)

fun patchTarget(bytes: ByteArray): PatchTarget {
    val parser = ICal.parser()
    for (entry in ICal.fingerprints(bytes).entries) {
        val uid = entry.key.uid ?: continue
        if (entry.key.name != "VEVENT") continue
        val component = parser.parseComponent(bytes, entry)
        val attendee = component.properties("ATTENDEE").firstOrNull() ?: continue
        if (component.properties("ATTENDEE").count { it.value.equals(attendee.value, ignoreCase = true) } > 1) continue
        val span = attendee.span!!
        val current = attendee.parameter("PARTSTAT")?.value
        val partStat = if (current.equals("TENTATIVE", ignoreCase = true)) PartStat.Declined else PartStat.Tentative
        return PatchTarget(
            uid,
            entry.key.recurrenceId,
            attendee.value,
            partStat,
            span.startOffset.toInt(),
            span.endOffset.toInt(),
        )
    }
    throw IllegalStateException("No event with an attendee")
}

fun patch(bytes: ByteArray, target: PatchTarget): PatchResult = ICalPatch(bytes) {
    component(target.uid, target.recurrenceId) {
        attendee(target.address) { set(Parameters.PARTSTAT, target.partStat) }
    }
}.apply()

fun editAndWrite(bytes: ByteArray, target: PatchTarget): ByteArray {
    val result = ICal.parse(bytes)
    val calendar = result.calendars.first { calendar -> calendar.events.any { matches(it, target) } }
    val event = calendar.events.first { matches(it, target) }
    val edited = calendar.edit {
        events {
            replace(event, event.edit { attendees { replace(target.address) { partStat = target.partStat } } })
        }
    }
    val components = result.components.map { if (it === calendar.component) edited.component else it }
    return ICal.writer().encodeToByteArray(components)
}

private fun matches(event: VEvent, target: PatchTarget): Boolean =
    event.uid == target.uid && event.component.property("RECURRENCE-ID")?.value == target.recurrenceId

fun checkOneLine(original: ByteArray, target: PatchTarget, result: PatchResult) {
    check(result.unmatched.isEmpty()) { "The patch did not find its target" }
    check(result.changes.size == 1) { "The patch changed ${result.changes.size} lines" }
    val patched = result.bytes
    var prefix = 0
    while (prefix < original.size && prefix < patched.size && original[prefix] == patched[prefix]) prefix += 1
    var suffix = 0
    while (suffix < original.size - prefix && suffix < patched.size - prefix &&
        original[original.size - 1 - suffix] == patched[patched.size - 1 - suffix]
    ) {
        suffix += 1
    }
    check(prefix >= target.lineStart) { "Bytes before the patched line changed" }
    check(original.size - suffix <= target.lineEnd) { "Bytes after the patched line changed" }
}

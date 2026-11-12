package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.recur.RecurrenceLimits
import io.github.calendrum.recur.RecurrenceOptions
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

// Occurrences and alarms with small limits: every occurrence of the window intersects it, they come by start time,
// an alarm repeats at most `maxAlarmRepetitions` times, and the cursor finds after `from` the first occurrence of the
// window that starts later.
// The first byte picks the window.
class OccurrencesFuzzTest {
    @FuzzTest
    fun occurrences(input: ByteArray) {
        if (input.isEmpty()) return
        val from = Instant.fromEpochSeconds(START + (input[0].toInt() and 0xFF) * 86_400L * 61)
        val to = Instant.fromEpochSeconds(from.epochSeconds + 400 * 86_400L)
        val limits = RecurrenceLimits.builder().maxOccurrences(LIMIT).maxIterationsWithoutMatch(500).build()
        val options = OccurrenceOptions.builder()
            .recurrence(RecurrenceOptions.builder().limits(limits).build())
            .floatingZone(TimeZone.UTC)
            .maxAlarmRepetitions(REPEATS)
            .build()
        for (calendar in ICal.parse(input.copyOfRange(1, input.size)).calendars) {
            val window = calendar.occurrences(from, to, options).take(TAKE).toList()
            for (index in window.indices) {
                val occurrence = window[index]
                invariant(occurrence.start < to && occurrence.end >= from) {
                    "an occurrence outside the window: $occurrence"
                }
                invariant(index == 0 || occurrence.start >= window[index - 1].start) { "not ordered at $index" }
                val alarms = occurrence.component.components.count { it.name == "VALARM" }
                invariant(occurrence.alarms.size <= alarms * (REPEATS + 1)) { "too many alarms: $occurrence" }
            }
            // The window also holds occurrences that started at or before `from` and are in progress; `after` returns
            // only those that start later, and the first of them is in the window when it starts before `to`.
            val later = window.firstOrNull { it.start > from } ?: continue
            val first = calendar.occurrences(options).after(from)
            invariant(first?.start == later.start) {
                "the cursor finds ${first?.start} after $from, not ${later.start}"
            }
        }
    }

    private companion object {
        const val START = 1_577_836_800L // 2020
        const val LIMIT = 2_000
        const val REPEATS = 5
        const val TAKE = 5_000
    }
}

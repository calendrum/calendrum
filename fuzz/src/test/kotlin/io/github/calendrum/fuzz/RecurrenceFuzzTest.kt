package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.Properties
import io.github.calendrum.model.decode
import io.github.calendrum.recur.RecurrenceLimits
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.StartMismatch
import io.github.calendrum.recur.expand
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.Instant

class RecurrenceFuzzTest {
    @FuzzTest
    fun recurrence(input: ByteArray) {
        if (input.size < 4) return
        val text = String(input, 4, input.size - 4, Charsets.UTF_8)
        // The builder takes no control character, DEL included; such a rule is not one to expand.
        if (text.any { it < ' ' || it == '\u007f' }) return
        val component = ICalComponent("VEVENT") { property("RRULE", text) }
        val rule = (component.decode(Properties.RRULE) as? Decoded.Value)?.value ?: return
        val days = ((input[1].toInt() and 0xFF) shl 8 or (input[2].toInt() and 0xFF)) - 20_000
        val date = LocalDate.fromEpochDays(days)
        val second = (input[3].toInt() and 0xFF) * 337 % 86_400
        val time = LocalDateTime(date.year, date.month, date.day, second / 3600, second / 60 % 60, second % 60)
        val start = when (input[0].toInt() and 3) {
            0 -> ICalDateTime.Date(date)
            1 -> ICalDateTime.Floating(time)
            2 -> ICalDateTime.Zoned(time, "Europe/Berlin")
            else -> ICalDateTime.Utc(Instant.fromEpochSeconds(days * 86_400L + second))
        }
        val policy = StartMismatch.entries[(input[0].toInt() shr 2 and 3) % 3]
        val options = RecurrenceOptions.builder()
            .startMismatch(policy)
            .limits(RecurrenceLimits.builder().maxOccurrences(LIMIT).maxIterationsWithoutMatch(IDLE).build())
            .build()
        val all = rule.expand(start, options).toList()
        invariant(all.size <= LIMIT) { "'$text' gave more than the limit" }
        invariant(all.all { it::class == start::class }) { "'$text' changed the form of DTSTART" }
        val keys = all.map(::key)
        for (index in 1 until keys.size) invariant(keys[index] > keys[index - 1]) { "'$text' not increasing at $index" }
        // With Include a DTSTART that the rule does not produce is an occurrence beyond COUNT.
        val count = rule.count?.let { if (policy == StartMismatch.Include) it + 1 else it }
        invariant(count == null || all.size <= count) { "'$text' exceeds COUNT" }
        if (all.size < LIMIT && all.size > 2) {
            val from = all[all.size / 2]
            val to = all.last()
            val window = rule.expand(start, from, to, options).toList()
            invariant(window == all.subList(all.size / 2, all.size - 1)) { "'$text' window differs from the slice" }
        }
    }

    private fun key(value: ICalDateTime): Long = when (value) {
        is ICalDateTime.Date -> value.date.toEpochDays() * 86_400L
        is ICalDateTime.Floating -> value.dateTime.date.toEpochDays() * 86_400L + value.dateTime.time.toSecondOfDay()
        is ICalDateTime.Zoned -> value.dateTime.date.toEpochDays() * 86_400L + value.dateTime.time.toSecondOfDay()
        is ICalDateTime.Utc -> value.instant.epochSeconds
    }

    private companion object {
        const val LIMIT = 300
        const val IDLE = 2_000
    }
}

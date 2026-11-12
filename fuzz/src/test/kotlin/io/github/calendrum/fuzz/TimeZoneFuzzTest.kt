package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

// The VTIMEZONE compiler and the offset lookup on any calendar: a zone either resolves or not, an offset stays within
// ±18 hours, a local time maps back to an instant within a day of where it came from, and transitions are increasing
// and inside their range. The first byte picks the sample instants.
class TimeZoneFuzzTest {
    @FuzzTest
    fun timeZone(input: ByteArray) {
        if (input.isEmpty()) return
        // At least 37 days and a second, so 1900–2100 is under 2,000 samples a zone (a step of 1 s was 6·10⁹).
        val step = 1L + (1 + (input[0].toInt() and 0xFF)) * 86_400L * 37
        for (calendar in ICal.parse(input.copyOfRange(1, input.size)).calendars) {
            val zones = TimeZoneResolvers.vtimezone(calendar)
            for (tzid in calendar.timeZones.mapNotNull { it.tzid }.distinct().take(8)) {
                val zone = zones.resolve(tzid, ResolveContext.Empty) ?: continue
                var seconds = FIRST
                while (seconds < LAST) {
                    val instant = Instant.fromEpochSeconds(seconds)
                    val offset = zone.offsetAt(instant).totalSeconds
                    invariant(offset in -MAX_OFFSET..MAX_OFFSET) { "$tzid: offset $offset at $instant" }
                    val local = Instant.fromEpochSeconds(seconds + offset).toLocalDateTime(TimeZone.UTC)
                    val back = zone.toInstant(local)
                    invariant(kotlin.math.abs(back.epochSeconds - seconds) <= DAY) { "$tzid: $local maps to $back" }
                    seconds += step
                }
                val transitions = zone.transitions(Instant.fromEpochSeconds(FIRST), Instant.fromEpochSeconds(LAST))
                for (index in transitions.indices) {
                    val at = transitions[index].at.epochSeconds
                    invariant(at in FIRST until LAST) { "$tzid: transition at $at outside the range" }
                    invariant(index == 0 || at > transitions[index - 1].at.epochSeconds) { "$tzid: not increasing" }
                }
            }
        }
    }

    private companion object {
        const val FIRST = -2_208_988_800L // 1900
        const val LAST = 4_102_444_800L // 2100
        const val MAX_OFFSET = 18 * 3600
        const val DAY = 86_400L
    }
}

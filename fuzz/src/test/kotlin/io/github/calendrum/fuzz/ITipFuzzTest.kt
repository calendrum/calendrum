@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.itip.IMip
import io.github.calendrum.itip.ITip
import io.github.calendrum.model.Series
import io.github.calendrum.model.calendars

// iTIP and series on any pair of calendars: the input before the first zero byte is the store, the rest the message.
// Applying never throws; its result is written (byte-stable, like its inputs) and reads back as one calendar, and when
// both calendars can be written canonically, so can the result (a reply that cannot be rebuilt is ignored);
// the iMIP checks and the series helpers accept whatever came in.
class ITipFuzzTest {
    @FuzzTest
    fun iTip(input: ByteArray) {
        val split = input.indexOf(0).let { if (it < 0) input.size / 2 else it }
        val stores = ICal.parse(input.copyOfRange(0, split)).calendars
        val messages = ICal.parse(input.copyOfRange(minOf(split + 1, input.size), input.size)).calendars
        for (message in messages) {
            IMip.checkSender(message, "mailto:a@example.com")
            IMip.untrustedContent(message)
            for (store in stores) {
                val result = ITip.apply(store, message)
                val text = ICal.writer().encodeToString(result.calendar.component)
                invariant(ICal.parse(text).calendars.size == 1) { "the result of ${result.outcome} does not read back" }
                if (!canonicallyWritable(store.component) || !canonicallyWritable(message.component)) continue
                invariant(canonicallyWritable(result.calendar.component)) {
                    "the result of ${result.outcome} cannot be written canonically"
                }
            }
        }
        for (store in stores) {
            val parts = Series.splitByUid(store)
            if (parts.isNotEmpty()) Series.merge(*parts.toTypedArray())
            val uid = store.events.firstNotNullOfOrNull { it.uid } ?: continue
            invariant(Series.find(store, uid) != null) { "the series $uid is not found" }
        }
    }
}

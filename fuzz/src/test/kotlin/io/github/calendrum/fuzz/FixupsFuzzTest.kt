@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.fixup.Fixups
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ParseOptions

class FixupsFuzzTest {
    @FuzzTest
    fun fixups(input: ByteArray) {
        if (input.isEmpty()) return
        val fixup = FIXUPS[(input[0].toInt() and 0xFF) % FIXUPS.size]
        val options = ParseOptions.builder()
        for (factory in fixup.interceptors()) options.intercept(factory)
        val bytes = input.copyOfRange(1, input.size)
        for (calendar in ICal.parser(options.build()).parse(bytes).calendars) {
            val first = fixup.apply(calendar)
            val second = fixup.apply(first.calendar)
            invariant(!second.isChanged) { "${fixup.id} is not idempotent: ${second.changes}" }
            val text = ICal.writer().encodeToString(first.calendar.component)
            val again = ICal.parse(text).calendars
            invariant(again.size == 1) { "${fixup.id} output does not read back as one calendar" }
            val report = Linter.lint(first.calendar)
            Linter.fix(first.calendar, report)
        }
    }

    private companion object {
        val FIXUPS = listOf(
            Fixups.windowsTzids,
            Fixups.xWrTimezone,
            Fixups.xWrCalendarName,
            Fixups.outlook,
            Fixups.lotusNotes,
            Fixups.google,
            Fixups.vcalendar10,
            Fixups.byProdId(),
        )
    }
}

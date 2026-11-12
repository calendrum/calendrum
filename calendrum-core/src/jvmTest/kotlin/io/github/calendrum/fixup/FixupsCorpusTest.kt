@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fixup

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.validate.Validator
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FixupsCorpusTest {
    private val root = File(System.getProperty("calendrum.spec")).parentFile

    private val all = listOf(
        Fixups.windowsTzids,
        Fixups.xWrTimezone,
        Fixups.xWrCalendarName,
        Fixups.outlook,
        Fixups.lotusNotes,
        Fixups.google,
        Fixups.vcalendar10,
        Fixups.byProdId(),
    )

    @Test
    fun everyFixupIsIdempotentAndHarmlessOnRealFiles() {
        val inputs = File(root, "corpus/real").walkTopDown().filter { it.name.endsWith(".ics") }.toList() +
            File(root, "conformance/ical").listFiles().orEmpty().map { File(it, "input.ics") }.filter { it.isFile }
        assertTrue(inputs.size > 50, "${inputs.size}")
        val changed = sortedMapOf<String, Int>()
        for (file in inputs) {
            val bytes = file.readBytes()
            for (fixup in all) {
                val options = ParseOptions.builder()
                fixup.interceptors().forEach { options.intercept(it) }
                for (calendar in ICal.parser(options.build()).parse(bytes).calendars) {
                    val first = fixup.apply(calendar)
                    if (first.isChanged) changed[fixup.id] = (changed[fixup.id] ?: 0) + 1
                    val second = fixup.apply(first.calendar)
                    assertEquals(
                        emptyList(),
                        second.changes.map {
                            it.toString()
                        },
                        "${file.parentFile.name} ${fixup.id}",
                    )
                    val before = Validator.validate(calendar).issues.count { it.severity == Severity.Error }
                    val after = Validator.validate(first.calendar).issues.count { it.severity == Severity.Error }
                    assertTrue(after <= before, "${file.parentFile.name} ${fixup.id}: $before -> $after")
                    val lint = Linter.lint(first.calendar)
                    Linter.fix(first.calendar, lint)
                }
            }
        }
        println("fixups changed: $changed")
    }
}

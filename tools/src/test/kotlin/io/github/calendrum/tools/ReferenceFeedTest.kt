package io.github.calendrum.tools

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.calendars
import io.github.calendrum.validate.Validator
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReferenceFeedTest {
    private val feed = ReferenceFeed(LocalDate(2026, 10, 19))

    @Test
    fun theFeedIsValidAndStable() {
        val bytes = feed.bytes()
        assertContentEquals(bytes, ReferenceFeed(LocalDate(2026, 10, 19)).bytes())
        val calendar = ICal.parse(bytes).calendars.single()
        val report = Validator.validate(calendar)
        assertEquals(
            emptyList(),
            report.issues.filter {
                it.severity != Severity.Info
            }.map { "${it.code.id} ${it.path}" },
        )
        val lint = Linter.lint(calendar).issues.filter { it.severity == Severity.Error }
        assertTrue(lint.isEmpty(), lint.map { "${it.code.id} ${it.path}" }.toString())
        val cases = (calendar.events.mapNotNull { it.summary } + calendar.todos.mapNotNull { it.summary })
            .map { it.substringBefore(' ') }.toSortedSet()
        assertEquals((1..19).map { it.toString().padStart(2, '0') }.toSortedSet(), cases)
        assertEquals(
            listOf(
                "America/New_York",
                "Asia/Kolkata",
                "Australia/Lord_Howe",
                "Europe/Berlin",
                "Europe/London",
                "Europe/Moscow",
            ),
            calendar.timeZones.mapNotNull { it.tzid }.sorted(),
        )
        val long = bytes.decodeToString().lines().filter { it.encodeToByteArray().size > 76 }
        assertTrue(long.isEmpty(), long.toString())
    }

    @Test
    fun expectedOccurrences() {
        val expected = feed.expected()
        assertTrue(
            expected.contains("| 01 | 2026-10-19T09:00:00Z | 2026-10-19T09:00:00Z | 2026-10-19T10:00:00Z |"),
            expected,
        )
        assertTrue(expected.contains("| 02 | 2026-10-19T10:00[Europe/Moscow] | 2026-10-19T07:00:00Z |"), expected)
        assertTrue(expected.contains("| 09 | 2026-10-27T10:00[Europe/Berlin] | 2026-10-27T09:00:00Z |"), expected)
        assertTrue(!expected.contains("| 13 |"), expected)
        assertEquals(5, expected.lines().count { it.startsWith("| 11 |") })
        assertEquals(LocalDate(2026, 10, 19), nextMonday(LocalDate(2026, 10, 17)))
        assertEquals(LocalDate(2026, 10, 26), nextMonday(LocalDate(2026, 10, 19)))
    }
}

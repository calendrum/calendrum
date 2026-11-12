package io.github.calendrum.lint

import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.validate.RuleSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinterTest {
    private fun calendar(vararg lines: String): VCalendar = ICal.parse(
        (listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN") + lines.toList() + "END:VCALENDAR")
            .joinToString("") { "$it\r\n" },
    ).calendars.single()

    private val messy = calendar(
        "X-WR-CALNAME:Team", "X-WR-TIMEZONE:Europe/Berlin",
        "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART:20261005T100000", "DTEND:20261005T110000",
        "DURATION:PT1H", "PRIORITY:12", "ORGANIZER:MAILTO:a@example.com", "URL:", "END:VEVENT",
        "BEGIN:VEVENT", "UID:2", "DTSTAMP:20260101T000000Z", "DTSTART;VALUE=DATE:20261007",
        "RRULE:FREQ=DAILY;UNTIL=20261010T235959Z", "END:VEVENT",
        "BEGIN:VTIMEZONE", "TZID:W. Europe Standard Time", "BEGIN:STANDARD", "DTSTART:16010101T030000",
        "TZOFFSETFROM:+0200", "TZOFFSETTO:+0100", "END:STANDARD", "END:VTIMEZONE",
        "BEGIN:VEVENT", "UID:3", "DTSTAMP:20260101T000000Z", "DTSTART;TZID=W. Europe Standard Time:20261008T100000",
        "DTEND:20261008T100000Z", "END:VEVENT",
    )

    private fun codes(report: LintReport): List<String> = report.issues.map { "${it.code.id} ${it.path}" }.sorted()

    @Test
    fun lintFindsInteropAndRfcProblems() {
        val report = Linter.lint(messy)
        assertEquals(
            listOf(
                "ICAL-L008 VCALENDAR[0]/VEVENT[0]/DURATION[0]",
                "ICAL-L010 VCALENDAR[0]/VEVENT[1]/RRULE[0]",
                "ICAL-L014 VCALENDAR[0]/VEVENT[0]/PRIORITY[0]",
                "ICAL-L016 VCALENDAR[0]/VEVENT[0]/URL[0]",
                "ICAL-L021 VCALENDAR[0]/X-WR-TIMEZONE[0]",
                "ICAL-L022 VCALENDAR[0]/VTIMEZONE[0]/TZID[0]",
                "ICAL-L023 VCALENDAR[0]/X-WR-CALNAME[0]",
                "ICAL-L024 VCALENDAR[0]/VEVENT[0]/ORGANIZER[0]",
                "ICAL-L025 VCALENDAR[0]/VEVENT[0]/URL[0]",
            ),
            codes(report),
        )
        assertEquals(8, report.fixable.size)
        assertTrue(report.toString().startsWith("LintReport("))
        val quiet = Linter.lint(
            messy,
            LintRules.builder().disable(ProblemCode.EmptyValue).disable(ProblemCode.MalformedValue).build(),
        )
        assertEquals(7, quiet.issues.size)
        val again = LintRules.Default.toBuilder().enable(ProblemCode.EmptyValue).validation(RuleSet.Rfc5545).build()
        assertEquals(9, Linter.lint(messy, again).issues.size)
        assertTrue(
            Linter.lint(
                calendar("BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART:20261005T100000Z", "END:VEVENT"),
            ).isClean,
        )
    }

    @Test
    fun fixRemovesWhatItCan() {
        val report = Linter.lint(messy)
        val fixed = Linter.fix(messy, report)
        assertEquals(
            listOf("ICAL-L016 VCALENDAR[0]/VEVENT[0]/URL[0]"),
            codes(Linter.lint(messy)).filter {
                "L016" in it
            },
        )
        assertTrue(Linter.lint(fixed).isClean, codes(Linter.lint(fixed)).toString())
        val event = fixed.events[0]
        assertEquals(9, event.priority)
        assertEquals(null, event.duration)
        assertEquals("mailto:a@example.com", event.organizer?.address)
        assertEquals("Europe/Berlin", (event.start as io.github.calendrum.value.ICalDateTime.Zoned).tzid)
        assertEquals(listOf("Team"), fixed.names)
        assertEquals("Europe/Berlin", fixed.timeZones.single().tzid)
        assertEquals(fixed, Linter.fix(fixed, Linter.lint(fixed)))
    }

    // Found by fuzzing: parsing keeps a control character, but no property can be built with one, so lint
    // threw while it tried the `mailto:` rewrite. Such a property is not rewritten; a clean one still is.
    @Test
    fun aValueWithAControlCharacterIsNotRewritten() {
        val input = calendar(
            "BEGIN:VEVENT",
            "UID:1",
            "DTSTAMP:20260101T000000Z",
            "DTSTART:20261005T100000Z",
            "ORGANIZER:Mailto:a\u0001@example.com",
            "ATTENDEE:MAILTO:b@example.com",
            "END:VEVENT",
        )
        val report = Linter.lint(input)
        val fixed = Linter.fix(input, report).events[0]
        assertEquals("Mailto:a\u0001@example.com", fixed.component.property("ORGANIZER")?.value)
        assertEquals("mailto:b@example.com", fixed.component.property("ATTENDEE")?.value)
        assertEquals(listOf("ICAL-L024 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]"), codes(report).filter { "L024" in it })
    }
}

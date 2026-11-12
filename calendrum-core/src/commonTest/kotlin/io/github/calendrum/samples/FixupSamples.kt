package io.github.calendrum.samples

import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.fixup.Fixups
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.calendars
import io.github.calendrum.value.ICalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val GOOGLE_FEED =
    "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Google Inc//Google Calendar 70.9054//EN\r\n" +
        "X-WR-TIMEZONE:Europe/Berlin\r\nBEGIN:VEVENT\r\nUID:42@example.com\r\nDTSTAMP:20260901T000000Z\r\n" +
        "DTSTART:20261005T100000\r\nDTEND:20261005T110000\r\nSUMMARY:Weekly sync\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

fun fixVendorCalendar() {
    val calendar = ICal.parse(GOOGLE_FEED).calendars.single()

    val result = Fixups.xWrTimezone.apply(calendar)

    assertTrue(result.isChanged)
    val start = result.calendar.events.single().start as ICalDateTime.Zoned
    assertEquals("Europe/Berlin", start.tzid)
}

fun lintAndFix() {
    val calendar = ICal.parse(GOOGLE_FEED).calendars.single()

    val report = Linter.lint(calendar)

    assertFalse(report.isClean)
    assertTrue(report.issues.any { it.code == ProblemCode.VendorTimeZone })
    val fixed = Linter.fix(calendar, report)
    assertTrue(Linter.lint(fixed).issues.none { it.code == ProblemCode.VendorTimeZone })
}

package io.github.calendrum.samples

import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.Cardinality
import io.github.calendrum.model.calendars
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.Validator
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private const val INVALID_EVENT = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\n" +
    "BEGIN:VEVENT\r\nUID:42@example.com\r\nDTSTAMP:20261001T000000Z\r\nDTSTART:20261005T100000Z\r\n" +
    "DTEND:20261005T090000Z\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

fun validateCalendar() {
    val calendar = ICal.parse(INVALID_EVENT).calendars.single()

    val report = Validator.validate(calendar)

    assertFalse(report.isValid)
    val issue = report.issues(ProblemCode.EndBeforeStart).single()
    assertEquals("VCALENDAR[0]/VEVENT[0]/DTEND[0]", issue.path.toString())
}

fun validateWithHouseRules() {
    val calendar = ICal.parse(INVALID_EVENT).calendars.single()
    val house = RuleSet.builder()
        .cardinality("VEVENT", "SUMMARY", Cardinality.One)
        .forbid("VEVENT", "ATTACH")
        .build()

    val report = Validator.validate(calendar, house)

    assertTrue(report.issues(ProblemCode.MissingProperty).any { it.path.toString().endsWith("SUMMARY[0]") })
}

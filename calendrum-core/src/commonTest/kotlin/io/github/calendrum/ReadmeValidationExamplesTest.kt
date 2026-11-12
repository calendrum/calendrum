package io.github.calendrum

import io.github.calendrum.model.Cardinality
import io.github.calendrum.model.Method
import io.github.calendrum.model.calendars
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.Validator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReadmeValidationExamplesTest {
    private val calendar = ICal.parse(
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260101T000000Z\r\n" +
            "DTSTART:20261005T100000Z\r\nDTEND:20261005T090000Z\r\nATTACH:https://example.com/a.pdf\r\nEND:VEVENT\r\n" +
            "END:VCALENDAR\r\n",
    ).calendars.single()
    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    @Test
    fun theValidationSnippetOfTheReadmeCompilesAndWorks() {
        val report = Validator.validate(calendar)
        if (!report.isValid) report.issues.forEach { println("${it.code.id} ${it.severity} ${it.path}") }
        val invitation = Validator.validate(calendar, RuleSet.itip(Method.Request))
        val house = RuleSet.builder()
            .cardinality("VEVENT", "SUMMARY", Cardinality.One)
            .forbid("VEVENT", "ATTACH")
            .build()
        val strict = Validator.validate(calendar, house)
        assertEquals(listOf("ICAL-L011 Error VCALENDAR[0]/VEVENT[0]/DTEND[0]"), printed)
        assertTrue(invitation.issues(ProblemCode.MethodMismatch).isNotEmpty())
        assertFalse(strict.isValid)
        assertEquals(3, strict.issues.size)
    }
}

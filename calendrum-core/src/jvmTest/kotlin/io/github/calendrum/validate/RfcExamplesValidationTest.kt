package io.github.calendrum.validate

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.model.Method
import io.github.calendrum.model.calendars
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class RfcExamplesValidationTest {
    private val root = File(System.getProperty("calendrum.spec")).parentFile

    @Test
    fun rfc5545ExamplesAreValid() {
        val inputs = File(root, "conformance/ical").listFiles().orEmpty()
            .filter { it.name.startsWith("rfc5545-") }
            .map { File(it, "input.ics") }
        val found = inputs.flatMap { file ->
            ICal.parse(file.readBytes()).calendars.flatMap { calendar ->
                Validator.validate(calendar).issues.filter { it.severity == Severity.Error }
                    .map { "${file.parentFile.name}: ${it.code.id} ${it.path}" }
            }
        }
        assertEquals(7, inputs.size)
        assertEquals(
            listOf(
                "rfc5545-freebusy-example: ICAL-L001 VCALENDAR[0]/VFREEBUSY[0]/UID[0]",
                "rfc5545-freebusy-example: ICAL-L001 VCALENDAR[0]/VFREEBUSY[0]/DTSTAMP[0]",
            ),
            found.sorted().reversed(),
        )
    }

    @Test
    fun rfc5546ExamplesAreValidForTheirMethod() {
        val files = File(
            javaClass.getResource("/io/github/calendrum/validate/rfc5546")!!.toURI(),
        ).listFiles().orEmpty().sorted()
        val found = files.flatMap { file ->
            val calendar = ICal.parse(file.readBytes()).calendars.single()
            Validator.validate(calendar, RuleSet.itip(calendar.method!!)).issues
                .filter { it.severity == Severity.Error }
                .map { "${file.name}: ${it.code.id} ${it.path}" }
        }
        assertEquals(52, files.size)
        assertEquals(RFC5546_EXAMPLE_ERRORS.sorted(), found.sorted())
    }

    private companion object {
        val RFC5546_EXAMPLE_ERRORS = listOf(
            "4-1-4-1-publish.ics: ICAL-L011 VCALENDAR[0]/VEVENT[0]/DTEND[0]",
            "4-2-1-1-request.ics: ICAL-L016 VCALENDAR[0]/VEVENT[0]/DTEND[0]",
            "4-2-6-1-reply.ics: ICAL-L002 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]",
            "4-2-7-1-reply.ics: ICAL-L002 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]",
            "4-3-1-1-publish.ics: ICAL-L001 VCALENDAR[0]/VFREEBUSY[0]/UID[0]",
            "4-4-8-4-request.ics: ICAL-L011 VCALENDAR[0]/VEVENT[1]/DTEND[0]",
            "4-4-8-4-request.ics: ICAL-L001 VCALENDAR[0]/VEVENT[1]/ORGANIZER[0]",
            "4-5-7-2-1-reply.ics: ICAL-L001 VCALENDAR[0]/VTODO[0]/ORGANIZER[0]",
            "4-7-1-1-refresh.ics: ICAL-L002 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]",
        )
    }
}

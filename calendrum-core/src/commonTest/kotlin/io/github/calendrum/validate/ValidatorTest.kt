package io.github.calendrum.validate

import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import io.github.calendrum.model.Cardinality
import io.github.calendrum.model.Method
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidatorTest {
    private fun calendar(vararg lines: String, head: List<String> = HEAD): VCalendar =
        ICal.parse((head + lines.toList() + "END:VCALENDAR").joinToString("") { "$it\r\n" }).calendars.single()

    private fun event(vararg lines: String): VCalendar =
        calendar("BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART:20260601T100000Z", *lines, "END:VEVENT")

    private fun issues(calendar: VCalendar, rules: RuleSet = RuleSet.Rfc5545): List<String> =
        Validator.validate(calendar, rules).issues.map { "${it.code.id} ${it.path}" }

    @Test
    fun aMinimalCalendarIsValid() {
        val report = Validator.validate(event("SUMMARY:x"))
        assertTrue(report.isValid, report.issues.toString())
        assertEquals(emptyList(), report.issues)
    }

    @Test
    fun cardinalities() {
        assertEquals(
            listOf("ICAL-L001 VCALENDAR[0]/PRODID[0]"),
            issues(
                calendar(
                    "BEGIN:VEVENT",
                    "UID:1",
                    "DTSTAMP:20260101T000000Z",
                    "DTSTART:20260601T100000Z",
                    "END:VEVENT",
                    head = listOf("BEGIN:VCALENDAR", "VERSION:2.0"),
                ),
            ),
        )
        assertEquals(
            listOf("ICAL-L001 VCALENDAR[0]/VEVENT[0]/UID[0]"),
            issues(calendar("BEGIN:VEVENT", "DTSTAMP:20260101T000000Z", "DTSTART:20260601T100000Z", "END:VEVENT")),
        )
        assertEquals(listOf("ICAL-L002 VCALENDAR[0]/VEVENT[0]/SUMMARY[1]"), issues(event("SUMMARY:a", "SUMMARY:b")))
        assertEquals(
            listOf("ICAL-L003 VCALENDAR[0]/VEVENT[0]/RRULE[1]"),
            issues(event("RRULE:FREQ=DAILY", "RRULE:FREQ=WEEKLY")),
        )
        assertEquals(listOf("ICAL-L004 VCALENDAR[0]/VEVENT[0]/DUE[0]"), issues(event("DUE:20260602T100000Z")))
        assertEquals(listOf("ICAL-L005 VCALENDAR[0]/VEVENT[0]/FOO[0]"), issues(event("FOO:bar", "X-FOO:bar")))
        val unknown = Validator.validate(event("FOO:bar"))
        assertTrue(unknown.isValid)
        assertEquals(Severity.Info, unknown.issues.single().severity)
    }

    @Test
    fun components() {
        assertEquals(listOf("ICAL-L007 VCALENDAR[0]"), issues(calendar()))
        assertEquals(
            listOf("ICAL-L006 VCALENDAR[0]/VEVENT[0]/VTODO[0]"),
            issues(event("BEGIN:VTODO", "UID:2", "DTSTAMP:20260101T000000Z", "END:VTODO")),
        )
        assertEquals(
            listOf("ICAL-L007 VCALENDAR[0]/VTIMEZONE[0]/STANDARD[0]"),
            issues(calendar("BEGIN:VTIMEZONE", "TZID:X", "END:VTIMEZONE")),
        )
        assertEquals(emptyList(), issues(event("BEGIN:X-THING", "X:1", "END:X-THING")))
    }

    @Test
    fun constraintsOfRfc5545() {
        assertEquals(
            listOf("ICAL-L008 VCALENDAR[0]/VEVENT[0]/DURATION[0]"),
            issues(event("DTEND:20260601T110000Z", "DURATION:PT1H")),
        )
        assertEquals(
            listOf("ICAL-L001 VCALENDAR[0]/VEVENT[0]/DTSTART[0]", "ICAL-L009 VCALENDAR[0]/VEVENT[0]/RRULE[0]"),
            issues(calendar("BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "RRULE:FREQ=DAILY", "END:VEVENT")),
        )
        assertEquals(listOf("ICAL-L010 VCALENDAR[0]/VEVENT[0]/DTEND[0]"), issues(event("DTEND;VALUE=DATE:20260602")))
        assertEquals(listOf("ICAL-L011 VCALENDAR[0]/VEVENT[0]/DTEND[0]"), issues(event("DTEND:20260601T100000Z")))
        assertEquals(
            listOf("ICAL-L012 VCALENDAR[0]/VEVENT[0]/DURATION[0]"),
            issues(
                calendar(
                    "BEGIN:VEVENT",
                    "UID:1",
                    "DTSTAMP:20260101T000000Z",
                    "DTSTART;VALUE=DATE:20260601",
                    "DURATION:PT1H",
                    "END:VEVENT",
                ),
            ),
        )
        assertEquals(
            listOf("ICAL-L010 VCALENDAR[0]/VEVENT[0]/RRULE[0]"),
            issues(event("RRULE:FREQ=DAILY;UNTIL=20260610T100000")),
        )
        assertEquals(listOf("ICAL-L013 VCALENDAR[0]/VEVENT[0]/STATUS[0]"), issues(event("STATUS:DONE")))
        assertEquals(listOf("ICAL-L014 VCALENDAR[0]/VEVENT[0]/PRIORITY[0]"), issues(event("PRIORITY:10")))
        assertEquals(listOf("ICAL-L016 VCALENDAR[0]/VEVENT[0]/GEO[0]"), issues(event("GEO:north")))
        assertEquals(
            listOf("ICAL-L013 VCALENDAR[0]/VERSION[0]"),
            issues(
                calendar(
                    "BEGIN:VJOURNAL",
                    "UID:1",
                    "DTSTAMP:20260101T000000Z",
                    "END:VJOURNAL",
                    head = listOf("BEGIN:VCALENDAR", "PRODID:-//t//EN", "VERSION:3.0"),
                ),
            ),
        )
        val todo = calendar(
            "BEGIN:VTODO",
            "UID:1",
            "DTSTAMP:20260101T000000Z",
            "DURATION:PT1H",
            "PERCENT-COMPLETE:120",
            "END:VTODO",
        )
        assertEquals(
            listOf(
                "ICAL-L009 VCALENDAR[0]/VTODO[0]/DURATION[0]",
                "ICAL-L014 VCALENDAR[0]/VTODO[0]/PERCENT-COMPLETE[0]",
            ),
            issues(todo).sorted(),
        )
    }

    @Test
    fun alarmsAndTimeZones() {
        assertEquals(
            listOf("ICAL-L001 VCALENDAR[0]/VEVENT[0]/VALARM[0]/DESCRIPTION[0]"),
            issues(event("BEGIN:VALARM", "ACTION:DISPLAY", "TRIGGER:-PT5M", "END:VALARM")),
        )
        assertEquals(
            listOf("ICAL-L009 VCALENDAR[0]/VEVENT[0]/VALARM[0]/REPEAT[0]"),
            issues(event("BEGIN:VALARM", "ACTION:AUDIO", "TRIGGER:-PT5M", "REPEAT:2", "END:VALARM")),
        )
        val unknown = Validator.validate(event("BEGIN:VALARM", "ACTION:X-SMS", "TRIGGER:-PT5M", "END:VALARM"))
        assertEquals(listOf(ProblemCode.UnknownAlarmAction), unknown.issues.map { it.code })
        assertTrue(unknown.isValid)
        assertEquals(
            listOf("ICAL-L017 VCALENDAR[0]/VEVENT[0]/DTSTART[0]@TZID"),
            issues(
                calendar(
                    "BEGIN:VEVENT",
                    "UID:1",
                    "DTSTAMP:20260101T000000Z",
                    "DTSTART;TZID=Europe/Berlin:20260601T100000",
                    "END:VEVENT",
                ),
            ),
        )
        assertEquals(
            listOf("ICAL-L015 VCALENDAR[0]/VTIMEZONE[0]/STANDARD[0]/DTSTART[0]"),
            issues(
                calendar(
                    "BEGIN:VTIMEZONE",
                    "TZID:X",
                    "BEGIN:STANDARD",
                    "DTSTART:19700101T000000Z",
                    "TZOFFSETFROM:+0100",
                    "TZOFFSETTO:+0100",
                    "END:STANDARD",
                    "END:VTIMEZONE",
                ),
            ),
        )
    }

    @Test
    fun itipRuleSets() {
        val request = calendar(
            "BEGIN:VEVENT", "UID:1", "DTSTAMP:20260101T000000Z", "DTSTART:20260601T100000Z", "SUMMARY:x",
            "ORGANIZER:mailto:o@example.com", "ATTENDEE:mailto:a@example.com", "END:VEVENT",
            head = listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN", "METHOD:REQUEST"),
        )
        assertTrue(Validator.validate(request, RuleSet.itip(Method.Request)).isValid)
        assertEquals(
            listOf("ICAL-L004 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]", "ICAL-L018 VCALENDAR[0]/METHOD[0]"),
            issues(request, RuleSet.itip(Method.Publish)).sorted(),
        )
        val decline = issues(request, RuleSet.itip(Method.DeclineCounter))
        assertTrue("ICAL-L018 VCALENDAR[0]/METHOD[0]" in decline, decline.toString())
        assertTrue("ICAL-L001 VCALENDAR[0]/VEVENT[0]/SEQUENCE[0]" in decline, decline.toString())
        val journal =
            calendar(
                "BEGIN:VJOURNAL",
                "UID:1",
                "DTSTAMP:20260101T000000Z",
                "END:VJOURNAL",
                head = listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN", "METHOD:REFRESH"),
            )
        assertEquals(listOf("ICAL-L020 VCALENDAR[0]/VJOURNAL[0]"), issues(journal, RuleSet.itip(Method.Refresh)))
        assertEquals(Method.Refresh, RuleSet.itip(Method.Refresh).method)
    }

    @Test
    fun builtRuleSets() {
        val rules = RuleSet.builder()
            .cardinality("VEVENT", "SUMMARY", Cardinality.One)
            .forbid("VEVENT", "LOCATION")
            .rule { component, path ->
                if (component.name == "VEVENT" && component.property("CATEGORIES") == null) {
                    listOf(ValidationIssue(ProblemCode.of("X-CORP-1"), Severity.Warning, path, component.span))
                } else {
                    emptyList()
                }
            }
            .build()
        val report = Validator.validate(event("LOCATION:Room"), rules)
        assertEquals(
            listOf(
                "ICAL-L001 VCALENDAR[0]/VEVENT[0]/SUMMARY[0]",
                "ICAL-L004 VCALENDAR[0]/VEVENT[0]/LOCATION[0]",
                "X-CORP-1 VCALENDAR[0]/VEVENT[0]",
            ),
            report.issues.map { "${it.code.id} ${it.path}" }.sorted(),
        )
        assertFalse(report.isValid)
        assertEquals(1, report.issues(ProblemCode.MissingProperty).size)
        assertEquals(
            "VCALENDAR[0]/VEVENT[0]/ATTENDEE[1]@PARTSTAT",
            ElementPath.Root.child(
                "VCALENDAR",
                0,
            ).child("VEVENT", 0).child("ATTENDEE", 1).parameter("PARTSTAT").toString(),
        )
    }

    private companion object {
        val HEAD = listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN")
    }

    // RFC 5545 §3.1 forbids CONTROL characters in a content line, HTAB aside. Parsing keeps them; the validator says so,
    // with the path and without the text.
    @Test
    fun controlCharacters() {
        for (char in listOf('\u0000', '\u0007', '\u001B', '\u007F', '\u0001')) {
            val report = Validator.validate(event("SUMMARY:Pl${char}ain", "DESCRIPTION:fine"))
            assertFalse(report.isValid, "U+${char.code.toString(16)}")
            assertEquals(
                listOf("ICAL-L026 VCALENDAR[0]/VEVENT[0]/SUMMARY[0]"),
                issues(event("SUMMARY:Pl${char}ain", "DESCRIPTION:fine")),
            )
            assertEquals(Severity.Error, report.issues.single().severity)
        }
        assertEquals(
            listOf("ICAL-L026 VCALENDAR[0]/VEVENT[0]/X-NOTE[1]", "ICAL-L026 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]"),
            issues(
                event("X-NOTE:fine", "X-NOTE:b\u0002d", "ATTENDEE;CN=Al\u0003ice:mailto:a@example.com"),
            ).sorted().reversed(),
        )
        assertEquals(
            listOf("ICAL-L026 VCALENDAR[0]/X-CAL[0]"),
            issues(
                calendar(
                    "X-CAL:\u0004",
                    "BEGIN:VEVENT",
                    "UID:1",
                    "DTSTAMP:20260101T000000Z",
                    "DTSTART:20260601T100000Z",
                    "END:VEVENT",
                ),
            ),
        )
        val message = Validator.validate(event("SUMMARY:SECRET\u0007")).issues.single().toString()
        assertFalse("SECRET" in message, message)
    }

    @Test
    fun whatIsNotAControlCharacter() {
        assertTrue(Validator.validate(event("SUMMARY:a\tb", "DESCRIPTION:Лекция 😀 é")).isValid)
        // A folded line has CR, LF and a space inside its range, and none of them counts.
        val folded = ICal.parse(
            (
                HEAD +
                    listOf(
                        "BEGIN:VEVENT",
                        "UID:1",
                        "DTSTAMP:20260101T000000Z",
                        "DTSTART:20260601T100000Z",
                        "SUMMARY:a long",
                        " folded\tline",
                        "END:VEVENT",
                        "END:VCALENDAR",
                    )
                )
                .joinToString("") { "$it\r\n" },
        ).calendars.single()
        assertTrue(Validator.validate(folded).isValid, Validator.validate(folded).issues.toString())
        // RFC 6868: ^n in a parameter value decodes to a line break, which the standard allows.
        assertTrue(Validator.validate(event("ATTENDEE;CN=\"A^nB\":mailto:a@example.com")).isValid)
        // A calendar built by hand cannot hold one at all: the builders refuse it.
        assertTrue(
            Validator.validate(VCalendar.builder().build()).issues.none {
                it.code == ProblemCode.ControlCharacter
            },
        )
    }

    @Test
    fun manyLinesWithControlCharactersCostOneSort() {
        val lines = Array(20_000) { "X-N:\u0001$it" }
        assertEquals(20_000, Validator.validate(event(*lines)).issues.count { it.code == ProblemCode.ControlCharacter })
    }
}

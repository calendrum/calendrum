package io.github.calendrum.itip

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.model.Method
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.Validator
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class ITipRfcExamplesTest {
    private val clock = object : Clock {
        override fun now(): Instant = Instant.parse("2026-10-01T08:00:00Z")
    }
    private val examples: List<Pair<String, VCalendar>> = File(
        javaClass.getResource("/io/github/calendrum/validate/rfc5546")!!.toURI(),
    ).listFiles().orEmpty().sorted().map { it.name to ICal.parse(it.readBytes()).calendars.single() }

    @Test
    fun everyAttendeeOfEveryRequestRepliesAndTheOrganizerApplies() {
        val errors = ArrayList<String>()
        var replies = 0
        for ((name, request) in examples) {
            if (request.method != Method.Request) continue
            val component = (request.events.map { it.component } + request.todos.map { it.component }).firstOrNull()
                ?: continue
            val organizer = component.property("ORGANIZER")?.value
            for (attendee in component.properties("ATTENDEE").map { it.value }.distinct()) {
                if (attendee == organizer || !attendee.startsWith("mailto:")) continue
                val reply = ITip.reply(request, attendee, PartStat.Accepted, clock)
                replies++
                Validator.validate(reply, RuleSet.itip(Method.Reply)).issues
                    .filter { it.severity == Severity.Error }
                    .forEach { errors += "$name $attendee: ${it.code.id} ${it.path}" }
                val applied = ITip.apply(request, reply)
                assertEquals(ITipOutcome.Applied, applied.outcome, name)
                val back = applied.calendar.component.components.first { it.name == component.name }
                    .properties("ATTENDEE").first { it.value == attendee }
                assertEquals("ACCEPTED", back.parameter("PARTSTAT")?.value, "$name $attendee")
                assertEquals(SenderCheck.Matches, IMip.checkSender(reply, attendee), name)
            }
        }
        assertTrue(replies >= 40, "$replies")
        assertEquals(
            listOf(
                "4-2-1-1-request.ics mailto:b@example.com: ICAL-L016 VCALENDAR[0]/VEVENT[0]/DTEND[0]",
                "4-2-1-1-request.ics mailto:c@example.com: ICAL-L016 VCALENDAR[0]/VEVENT[0]/DTEND[0]",
                "4-2-1-1-request.ics mailto:d@example.com: ICAL-L016 VCALENDAR[0]/VEVENT[0]/DTEND[0]",
                "4-2-1-1-request.ics mailto:e@example.com: ICAL-L016 VCALENDAR[0]/VEVENT[0]/DTEND[0]",
            ),
            errors.filter { it.startsWith("4-2-1-1") },
        )
        assertEquals(emptyList(), errors.filterNot { it.startsWith("4-2-1-1") || it.startsWith("4-4-8-4") })
    }

    @Test
    fun everyExampleAppliesToAnEmptyAndToItsOwnCalendar() {
        val outcomes = sortedMapOf<String, Int>()
        for ((name, message) in examples) {
            val empty = VCalendar.builder().prodId("-//t//EN").version("2.0").build()
            val first = ITip.apply(empty, message)
            if (message.freeBusy.isNotEmpty()) assertEquals(ITipOutcome.Ignored, first.outcome, name)
            val again = ITip.apply(first.calendar, message)
            val key = "${message.method!!.value} ${first.outcome}"
            outcomes[key] = (outcomes[key] ?: 0) + 1
            if (first.outcome == ITipOutcome.Applied) {
                assertTrue(again.outcome == ITipOutcome.Applied || again.outcome == ITipOutcome.Outdated, name)
            }
            val sender = message.component.components.firstOrNull { it.property("ORGANIZER") != null }
                ?.property("ORGANIZER")?.value
            if (sender != null && message.method!!.value in ORGANIZER_METHODS) {
                assertTrue(IMip.checkSender(message, sender) != SenderCheck.Mismatch, name)
            }
        }
        assertEquals(ALL_METHODS, outcomes.keys.map { it.substringBefore(' ') }.toSet())
        assertEquals(
            "{ADD NotFound=2, CANCEL NotFound=5, COUNTER ResponseRequired=2, DECLINECOUNTER Ignored=1, " +
                "PUBLISH Applied=5, PUBLISH Ignored=1, REFRESH ResponseRequired=2, REPLY Ignored=1, " +
                "REPLY NotFound=9, REQUEST Applied=23, REQUEST Ignored=1}",
            outcomes.toString(),
        )
    }

    private companion object {
        val ORGANIZER_METHODS = setOf("PUBLISH", "REQUEST", "ADD", "CANCEL", "DECLINECOUNTER")
        val ALL_METHODS = setOf("PUBLISH", "REQUEST", "REPLY", "ADD", "CANCEL", "REFRESH", "COUNTER", "DECLINECOUNTER")
    }
}

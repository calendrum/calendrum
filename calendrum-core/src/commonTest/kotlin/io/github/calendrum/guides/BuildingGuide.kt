@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.guides

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.FeedPublisher
import io.github.calendrum.model.ICalPatch
import io.github.calendrum.model.Parameters
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.Properties
import io.github.calendrum.model.Series
import io.github.calendrum.model.Trigger
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class BuildingGuide {
    @Test
    fun buildWithTheDsl() {
        // region building-dsl
        val defaults = BuildDefaults(Clock.System) { newUid() } // a clock and a UID source, nothing global
        val calendar = VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = defaults) {
            event {
                summary = "Weekly sync"
                start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
                duration = ICalDuration.ofHours(1)
                // COUNT xor UNTIL; BYxxx and ranges are checked
                rrule(Frequency.WEEKLY) {
                    byDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
                    count(10)
                }
                attendee("mailto:alice@example.com") {
                    cn = "Alice"
                    rsvp = true
                }
                alarm { display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Soon") }
                property("X-ACME-TAG", "blue")
            }
            todo {
                summary = "Book a room"
                due = ICalDateTime.Utc(Instant.parse("2026-09-30T12:00:00Z"))
            }
        }
        val ics = ICal.writer().encodeToString(calendar.component)
        // endregion
        assertTrue("RRULE:FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE\r\n" in ics)
        assertTrue(calendar.events.single().uid.orEmpty().startsWith("uid-"))
        assertEquals("Book a room", calendar.todos.single().summary)
    }

    @Test
    fun impossibleRulesAreRefused() {
        // region building-checks
        val failure = assertFailsWith<IllegalArgumentException> {
            VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = BuildDefaults(Clock.System) { newUid() }) {
                event {
                    start = ICalDateTime.Date(LocalDate(2026, 10, 1))
                    rrule(Frequency.DAILY) { byWeekNo(1) } // BYWEEKNO is only for YEARLY
                }
            }
        }
        // endregion
        assertTrue(failure.message.orEmpty().isNotEmpty())
    }

    @Test
    fun handBuiltTrees() {
        // region building-tree
        val event = ICalComponent("VEVENT") {
            property("UID", "42@example.com")
            property(ICalProperty.builder("ATTENDEE").parameter("CN", "Jane").value("mailto:jane@example.com").build())
            component("VALARM") { property("ACTION", "DISPLAY") }
        }
        val ics = ICal.writer().encodeToString(event) // canonical: folded at 75 octets, escaped
        // endregion
        assertEquals(
            "BEGIN:VEVENT\r\nUID:42@example.com\r\nATTENDEE;CN=Jane:mailto:jane@example.com\r\nBEGIN:VALARM\r\n" +
                "ACTION:DISPLAY\r\nEND:VALARM\r\nEND:VEVENT\r\n",
            ics,
        )
    }

    @Test
    fun edits() {
        val calendar = ICal.parse(calendarText).calendars.single()
        // region building-edit
        val event = calendar.events.first()
        val accepted = calendar.edit {
            // a new calendar; the old one is untouched
            events {
                replace(
                    event,
                    event.edit {
                        sequence = (sequence ?: 0) + 1
                        // other parameters stay
                        attendees { replace("mailto:jane@example.com") { partStat = PartStat.Accepted } }
                        remove("LOCATION")
                    },
                )
            }
        }
        val untouched = accepted.events.last().component === calendar.events.last().component // shared, not copied
        // endregion
        assertEquals(PartStat.Accepted, accepted.events.first().attendees.first().partStat)
        assertEquals(PartStat.NeedsAction, calendar.events.first().attendees.first().partStat)
        assertEquals(1, accepted.events.first().sequence)
        assertEquals(null, accepted.events.first().location)
        assertTrue(untouched)
    }

    @Test
    fun patchesAFile() {
        val original = calendarBytes
        // region building-patch
        val result = ICalPatch(original) {
            component(uid = "42@example.com", recurrenceId = null) {
                attendee("mailto:jane@example.com") { set(Parameters.PARTSTAT, PartStat.Accepted) }
                set(Properties.DTSTAMP, ICalDateTime.Utc(Clock.System.now()))
            }
        }.apply()
        val patched: ByteArray = result.bytes // every other byte is as it was
        val missed = result.unmatched // selectors that found nothing
        val log = result.changes.map { "${it.kind} ${it.property} at line ${it.line}" } // never the values
        // endregion
        assertEquals(emptyList(), missed)
        assertEquals(2, log.size)
        assertTrue("PARTSTAT=ACCEPTED" in patched.decodeToString())
        assertTrue("SUMMARY:Weekly sync\r\n" in patched.decodeToString())
    }

    @Test
    fun series() {
        val calendar = ICal.parse(calendarText).calendars.single()
        // region building-series
        val series = Series.find(calendar, "42@example.com")!!
        val second = ICalDateTime.Zoned(LocalDateTime(2026, 10, 8, 10, 0), "Europe/Berlin")
        val moved = Series.editThis(series, second) {
            // one instance: an override is created
            start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 8, 14, 0), "Europe/Berlin")
        }
        val cancelled = Series.deleteThis(series, second) // an EXDATE on the master
        val renamed = Series.editAll(series) { summary = "Team sync" } // the whole series
        // endregion
        assertEquals(2, moved.events.count { it.uid == "42@example.com" })
        assertEquals(1, cancelled.events.first().exdates.size)
        assertEquals("Team sync", renamed.events.first().summary)
    }

    @Test
    fun feedPublishing() {
        val previous = ICal.parse(calendarText).calendars.single()
        val current = previous.edit {
            events { replace(previous.events.first(), previous.events.first().edit { summary = "Daily sync" }) }
        }
        // region building-publish
        val publisher = FeedPublisher(Clock.System)
        // new DTSTAMP and a higher SEQUENCE for what changed, nothing else
        val next = publisher.publish(previous, current)
        // endregion
        assertEquals(1, next.events.first().sequence)
        assertEquals(previous.events.last().stamp, next.events.last().stamp)
    }

    @Test
    fun claimsOfTheGuide() {
        assertFailsWith<IllegalStateException> {
            VCalendar(prodId = "-//Example//Planner 1.0//EN") { event { summary = "No identity" } }
        }
        val long = ICalComponent("VEVENT") { property("DESCRIPTION", "word ".repeat(40)) }
        val lines = ICal.writer().encodeToString(long).split("\r\n")
        assertTrue(lines.all { it.encodeToByteArray().size <= 75 })
    }
}

@file:JvmName("Ical4jInterop")

package io.github.calendrum.ical4j

import io.github.calendrum.ICal
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.data.ParserException
import net.fortuna.ical4j.model.Calendar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

// Both directions go through text: Calendrum writes byte-stable text and parses what ical4j writes;
// `CalendarOutputter(false)` skips the validation of ical4j, and the global `CompatibilityHints` stay untouched.
/**
 * Converts an ical4j `Calendar` to a [VCalendar], by writing it as iCalendar text and parsing that. Properties and
 * parameters survive as text, so the result is as faithful as ical4j's own output.
 *
 * @throws IOException if ical4j fails to write the calendar.
 */
@Throws(IOException::class)
public fun Calendar.toCalendrum(): VCalendar {
    val out = ByteArrayOutputStream()
    CalendarOutputter(false).output(this, out)
    return ICal.parse(out.toByteArray()).calendars.single()
}

/**
 * Converts this calendar to an ical4j `Calendar`, by writing it as iCalendar text and building that with ical4j's
 * `CalendarBuilder`.
 *
 * @throws IOException if ical4j fails to read the text.
 * @throws ParserException if ical4j rejects the calendar.
 */
@Throws(IOException::class, ParserException::class)
public fun VCalendar.toIcal4j(): Calendar =
    CalendarBuilder().build(ByteArrayInputStream(ICal.writer().encodeToByteArray(component)))

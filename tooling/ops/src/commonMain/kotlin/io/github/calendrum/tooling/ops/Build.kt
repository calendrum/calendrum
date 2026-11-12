package io.github.calendrum.tooling.ops

import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.Organizer
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.model.get
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.tzdata.TzData
import io.github.calendrum.validate.Validator
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration

internal fun buildEvent(request: Request, run: Run) {
    val allDay = request.boolean("all_day") ?: false
    val zoneName = request.text("zone")
    if (allDay && zoneName != null) badArgument("An all-day event takes no zone")
    val zone = zoneName?.let { zoneOf(it, run).id }
    val summary = request.requiredText("summary")
    val start = eventTime(request.requiredText("start"), zone, allDay, "start")
    val end = request.text("end")?.let { eventTime(it, zone, allDay, "end") }
    val duration = request.text("duration")?.let(::durationOf)
    if (end != null && duration != null) badArgument("Give only one of end and duration")
    val rule = request.text("rrule")?.let { recurOf(it, "rrule") }
    val description = request.text("description")
    val location = request.text("location")
    val givenUid = request.text("uid")?.let { checkToken(it, "uid") }
    val stamp = request.instant("dtstamp")?.let { ICalDateTime.Utc(it) }
    val organizer = request.text("organizer")?.let { Organizer.builder(addressOf(it, "organizer")).build() }
    val attendees = request.objects("attendees").orEmpty().map { attendee ->
        Attendee(
            addressOf(attendee.requiredText("address"), "attendees"),
            attendee.text("name"),
            attendee.boolean("rsvp"),
        )
    }
    val environment = run.environment
    val defaults = BuildDefaults(environment.clock) { newUid(environment) }
    val writer = ICal.writer(WriteOptions.builder().addMissingTimeZones(TzData).timeZonesFirst(true).build())
    val ics = try {
        val calendar = VCalendar(PRODUCT, defaults) {
            event {
                if (givenUid != null) this.uid = givenUid
                if (stamp != null) this.stamp = stamp
                this.summary = summary
                this.start = start
                if (end != null) this.end = end
                if (duration != null) this.duration = duration
                if (rule != null) this.rrule = rule
                if (description != null) this.description = description
                if (location != null) this.location = location
                if (organizer != null) this.organizer = organizer
                for (attendee in attendees) {
                    attendee(attendee.address) {
                        if (attendee.name != null) cn = attendee.name
                        if (attendee.rsvp != null) rsvp = attendee.rsvp
                    }
                }
            }
        }
        writer.encodeToString(calendar.component)
    } catch (_: IllegalArgumentException) {
        badArgument("The fields do not make a valid event")
    } catch (_: IllegalStateException) {
        // The builder of an event says so about what does not fit together, such as UNTIL of another type than the start.
        badArgument("The fields do not make a valid event")
    }
    // The file that goes out is what must be valid, with its VTIMEZONE.
    val written = ICal.parse(ics).calendars.single()
    if (Validator.validate(written).issues.any { it.severity == Severity.Error }) {
        badArgument("The fields do not make a valid event")
    }
    val out = run.out
    out.beginObject()
    out.field("uid", written.events.single().uid.orEmpty())
    out.field("ics", ics)
    run.writeProblems()
    out.endObject()
}

private class Attendee(val address: String, val name: String?, val rsvp: Boolean?)

private fun eventTime(text: String, zone: String?, allDay: Boolean, field: String): ICalDateTime {
    if (allDay) {
        checkToken(text, field)
        return ICalDateTime.Date(localDate(text, field))
    }
    val value = dateTimeOf(text, zone, field)
    if (value is ICalDateTime.Date) badArgument("The field $field is a date, so the event needs all_day")
    return value
}

private fun durationOf(text: String): ICalDuration {
    checkToken(text, "duration")
    val holder = ICalComponent("VEVENT") { property("DURATION", text) }
    return holder[Properties.DURATION] ?: badArgument("The field duration is not a duration such as PT1H")
}

// A person writes an address without a scheme; RFC 5545 §3.3.3 wants a URI.
private fun addressOf(text: String, field: String): String {
    checkToken(text, field)
    return if (text.contains(':')) text else "mailto:$text"
}

private fun newUid(environment: OpsEnvironment): String {
    val text = StringBuilder(UID_LENGTH)
    for (byte in environment.randomBytes(UID_BYTES)) {
        val code = byte.toInt() and 0xFF
        text.append(HEX[code shr 4]).append(HEX[code and 0xF])
    }
    return text.append("@calendrum").toString()
}

private const val PRODUCT = "-//Calendrum//Tooling//EN"
private const val UID_BYTES = 16
private const val UID_LENGTH = 42
private const val HEX = "0123456789abcdef"

package io.github.calendrum.benchmarks.parseTyped

import io.github.calendrum.model.Attach
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.Organizer
import io.github.calendrum.model.RequestStatus
import io.github.calendrum.model.VAlarm
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.VEvent
import io.github.calendrum.model.VFreeBusy
import io.github.calendrum.model.VJournal
import io.github.calendrum.model.VTimeZone
import io.github.calendrum.model.VTodo
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ParseResult

internal class ModelReader {
    var values: Int = 0
        private set

    var components: Int = 0
        private set

    fun read(result: ParseResult): Int {
        for (calendar in result.calendars) read(calendar)
        return values
    }

    fun read(calendar: VCalendar) {
        components += 1
        value(calendar.prodId)
        value(calendar.version)
        value(calendar.method)
        value(calendar.calScale)
        for (event in calendar.events) read(event)
        for (todo in calendar.todos) read(todo)
        for (journal in calendar.journals) read(journal)
        for (busy in calendar.freeBusy) read(busy)
        for (zone in calendar.timeZones) read(zone)
    }

    private fun read(event: VEvent) {
        components += 1
        value(event.uid)
        value(event.stamp)
        value(event.start)
        value(event.classification)
        value(event.created)
        value(event.description)
        value(event.geo?.latitude)
        value(event.lastModified)
        value(event.location)
        organizer(event.organizer)
        value(event.priority)
        value(event.sequence)
        value(event.status)
        value(event.summary)
        value(event.transparency)
        value(event.url)
        value(event.recurrenceId)
        value(event.rrule?.frequency)
        value(event.end)
        value(event.duration)
        attachments(event.attachments)
        attendees(event.attendees)
        list(event.categories)
        list(event.comments)
        list(event.contacts)
        list(event.exdates)
        statuses(event.requestStatuses)
        for (relation in event.relatedTo) value(relation.relType)
        list(event.resources)
        list(event.rdates)
        for (alarm in event.alarms) read(alarm)
    }

    private fun read(todo: VTodo) {
        components += 1
        value(todo.uid)
        value(todo.stamp)
        value(todo.classification)
        value(todo.completed)
        value(todo.created)
        value(todo.description)
        value(todo.start)
        value(todo.geo?.longitude)
        value(todo.lastModified)
        value(todo.location)
        organizer(todo.organizer)
        value(todo.percentComplete)
        value(todo.priority)
        value(todo.recurrenceId)
        value(todo.sequence)
        value(todo.status)
        value(todo.summary)
        value(todo.url)
        value(todo.rrule?.frequency)
        value(todo.due)
        value(todo.duration)
        attachments(todo.attachments)
        attendees(todo.attendees)
        list(todo.categories)
        list(todo.comments)
        list(todo.contacts)
        list(todo.exdates)
        statuses(todo.requestStatuses)
        for (relation in todo.relatedTo) value(relation.relType)
        list(todo.resources)
        list(todo.rdates)
        for (alarm in todo.alarms) read(alarm)
    }

    private fun read(journal: VJournal) {
        components += 1
        value(journal.uid)
        value(journal.stamp)
        value(journal.classification)
        value(journal.created)
        value(journal.start)
        value(journal.lastModified)
        organizer(journal.organizer)
        value(journal.recurrenceId)
        value(journal.sequence)
        value(journal.status)
        value(journal.summary)
        value(journal.url)
        value(journal.rrule?.frequency)
        attachments(journal.attachments)
        attendees(journal.attendees)
        list(journal.categories)
        list(journal.comments)
        list(journal.contacts)
        list(journal.descriptions)
        list(journal.exdates)
        for (relation in journal.relatedTo) value(relation.relType)
        list(journal.rdates)
        statuses(journal.requestStatuses)
    }

    private fun read(busy: VFreeBusy) {
        components += 1
        value(busy.uid)
        value(busy.stamp)
        value(busy.contact)
        value(busy.start)
        value(busy.end)
        organizer(busy.organizer)
        value(busy.url)
        attendees(busy.attendees)
        list(busy.comments)
        for (item in busy.freeBusy) list(item.periods)
        statuses(busy.requestStatuses)
    }

    private fun read(zone: VTimeZone) {
        components += 1
        value(zone.tzid)
        value(zone.lastModified)
        value(zone.tzUrl)
        for (observance in zone.observances) {
            components += 1
            value(observance.start)
            value(observance.offsetFrom)
            value(observance.offsetTo)
            value(observance.rrule?.frequency)
            list(observance.comments)
            list(observance.rdates)
            list(observance.names)
        }
    }

    private fun read(alarm: VAlarm) {
        components += 1
        value(alarm.action)
        value(alarm.trigger)
        value(alarm.duration)
        value(alarm.repeat)
        value(alarm.description)
        value(alarm.summary)
        attendees(alarm.attendees)
        attachments(alarm.attachments)
    }

    private fun organizer(organizer: Organizer?) {
        if (organizer == null) return
        values += 1
        value(organizer.cn)
    }

    private fun attendees(attendees: List<Attendee>) {
        for (attendee in attendees) {
            values += 1
            value(attendee.cn)
            value(attendee.partStat)
            value(attendee.role)
        }
    }

    private fun attachments(attachments: List<Attach>) {
        for (attach in attachments) value(attach.formatType ?: attach)
    }

    private fun statuses(statuses: List<RequestStatus>) {
        for (status in statuses) value(status.code)
    }

    private fun list(items: List<Any>) {
        values += items.size
    }

    private fun value(value: Any?) {
        if (value != null) values += 1
    }
}

internal fun readModel(result: ParseResult): Int = ModelReader().read(result)

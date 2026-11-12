package io.github.calendrum.benchmarks.baseline

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.DaylightSavingsTime
import biweekly.component.ICalComponent
import biweekly.component.RawComponent
import biweekly.component.StandardTime
import biweekly.component.VEvent
import biweekly.io.TimezoneInfo
import biweekly.property.Attendee
import biweekly.property.ICalProperty
import biweekly.property.ListProperty
import biweekly.property.Organizer
import biweekly.property.RawProperty
import biweekly.property.Trigger
import biweekly.property.ValuedProperty
import biweekly.property.Version
import java.io.ByteArrayInputStream
import java.io.OutputStream
import java.util.Date
import java.util.TimeZone

internal object BiweeklyBaseline {
    const val NAME: String = "biweekly"

    private val utc: TimeZone = TimeZone.getTimeZone("UTC")
    private val windowStart: Date = Date.from(rruleWindowStart)
    private val windowEnd: Date = Date.from(rruleWindowEnd)

    fun parse(input: ByteArray): ICalendar {
        val calendars = Biweekly.parse(ByteArrayInputStream(input)).all()
        check(calendars.size == 1) { "Expected one VCALENDAR, got ${calendars.size}" }
        return calendars[0]
    }

    fun readAll(calendar: ICalendar, sink: ValueSink): Int {
        sink.consume(calendar.version)
        var values = versionProperties(calendar) + readProperties(calendar.properties.values(), sink)
        for (assignment in calendar.timezoneInfo.timezones) {
            assignment.component?.let { values += readComponent(it, sink) }
        }
        for (component in calendar.components.values()) {
            values += readComponent(component, sink)
        }
        return values
    }

    fun write(calendar: ICalendar, output: OutputStream) {
        Biweekly.write(calendar).go(output)
    }

    fun recurringMasters(calendar: ICalendar): List<VEvent> =
        calendar.events.filter { it.recurrenceRule != null && it.recurrenceId == null }

    fun occurrences(timezoneInfo: TimezoneInfo, event: VEvent): Int {
        val timezone = timezoneInfo.getTimezone(event.dateStart)?.timeZone ?: utc
        val iterator = event.getDateIterator(timezone)
        iterator.advanceTo(windowStart)
        var count = 0
        while (iterator.hasNext()) {
            if (!iterator.next().before(windowEnd)) break
            count++
        }
        return count
    }

    fun uid(event: VEvent): String = event.uid.value

    fun counts(calendars: List<ICalendar>): ModelCounts {
        val counter = ModelCounter()
        for (calendar in calendars) {
            counter.component("VCALENDAR")
            counter.properties(versionProperties(calendar) + calendar.properties.size())
            for (assignment in calendar.timezoneInfo.timezones) {
                assignment.component?.let { count(it, counter) }
            }
            for (component in calendar.components.values()) {
                count(component, counter)
            }
        }
        return counter.result()
    }

    private fun versionProperties(calendar: ICalendar): Int = if (calendar.version != null) 1 else 0

    private fun count(component: ICalComponent, counter: ModelCounter) {
        counter.component(componentName(component))
        counter.properties(component.properties.size())
        for (child in component.components.values()) {
            count(child, counter)
        }
    }

    private fun componentName(component: ICalComponent): String = when (component) {
        is StandardTime -> "STANDARD"
        is DaylightSavingsTime -> "DAYLIGHT"
        is RawComponent -> component.name
        else -> component.javaClass.simpleName.uppercase()
    }

    private fun readComponent(component: ICalComponent, sink: ValueSink): Int {
        var values = readProperties(component.properties.values(), sink)
        for (child in component.components.values()) {
            values += readComponent(child, sink)
        }
        return values
    }

    private fun readProperties(properties: List<ICalProperty>, sink: ValueSink): Int {
        for (property in properties) {
            readValue(property, sink)
            for (parameter in property.parameters) {
                for (value in parameter.value) {
                    sink.consume(value)
                }
            }
        }
        return properties.size
    }

    private fun readValue(property: ICalProperty, sink: ValueSink) {
        when (property) {
            is Attendee -> {
                sink.consume(property.email ?: property.uri)
                sink.consume(property.commonName)
                sink.consume(property.role)
                sink.consume(property.participationStatus)
                sink.consume(property.rsvp)
            }

            is Organizer -> {
                sink.consume(property.email ?: property.uri)
                sink.consume(property.commonName)
            }

            is Trigger -> sink.consume(property.duration ?: property.date)

            is ListProperty<*> -> sink.consume(property.values)

            is ValuedProperty<*> -> sink.consume(property.value)

            is Version -> sink.consume(property.maxVersion)

            is RawProperty -> sink.consume(property.value)

            else -> sink.consume(property)
        }
    }
}

package io.github.calendrum.benchmarks.baseline

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.ComponentContainer
import net.fortuna.ical4j.model.Period
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Attendee
import net.fortuna.ical4j.model.property.Categories
import net.fortuna.ical4j.model.property.DateListProperty
import net.fortuna.ical4j.model.property.DateProperty
import net.fortuna.ical4j.model.property.Organizer
import net.fortuna.ical4j.model.property.RRule
import net.fortuna.ical4j.model.property.Sequence
import net.fortuna.ical4j.model.property.Trigger
import net.fortuna.ical4j.model.property.TzOffsetFrom
import net.fortuna.ical4j.model.property.TzOffsetTo
import net.fortuna.ical4j.model.property.Version
import net.fortuna.ical4j.util.Configurator
import java.io.ByteArrayInputStream
import java.io.OutputStream
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.Temporal

internal object Ical4jBaseline {
    const val NAME: String = "ical4j"

    val settings: Map<String, String> = mapOf(
        "net.fortuna.ical4j.timezone.update.enabled" to "false",
        "net.fortuna.ical4j.timezone.cache.impl" to "net.fortuna.ical4j.util.MapTimeZoneCache",
        "net.fortuna.ical4j.timezone.id.pool.size" to "10000",
    )

    init {
        for ((key, value) in settings) {
            val actual = Configurator.getProperty(key).orElse(null)
            check(actual == value) { "ical4j.properties is not on the classpath: $key=$actual, expected $value" }
        }
    }

    private val window: Period<ZonedDateTime> = Period(
        ZonedDateTime.ofInstant(rruleWindowStart, ZoneOffset.UTC),
        ZonedDateTime.ofInstant(rruleWindowEnd, ZoneOffset.UTC),
    )

    fun parse(input: ByteArray): Calendar = CalendarBuilder().build(ByteArrayInputStream(input))

    fun readAll(calendar: Calendar, sink: ValueSink): Int {
        var values = readProperties(calendar.propertyList.all, sink)
        for (component in calendar.componentList.all) {
            values += readComponent(component, sink)
        }
        return values
    }

    fun write(calendar: Calendar, output: OutputStream) {
        CalendarOutputter(false).output(calendar, output)
    }

    fun recurringMasters(calendar: Calendar): List<VEvent> = calendar.getComponents<VEvent>(Component.VEVENT)
        .filter { it.getProperty<RRule<*>>(Property.RRULE).isPresent }
        .filter { !it.getProperty<Property>(Property.RECURRENCE_ID).isPresent }

    fun occurrences(event: VEvent): Set<Period<Temporal>> = event.calculateRecurrenceSet(window)

    fun uid(event: VEvent): String = event.getProperty<Property>(Property.UID).orElseThrow().value

    fun counts(calendars: List<Calendar>): ModelCounts {
        val counter = ModelCounter()
        for (calendar in calendars) {
            counter.component(Calendar.VCALENDAR)
            counter.properties(calendar.propertyList.all.size)
            for (component in calendar.componentList.all) {
                count(component, counter)
            }
        }
        return counter.result()
    }

    private fun count(component: Component, counter: ModelCounter) {
        counter.component(component.name)
        counter.properties(component.propertyList.all.size)
        if (component is ComponentContainer<*>) {
            for (child in component.componentList.all) {
                count(child, counter)
            }
        }
    }

    private fun readComponent(component: Component, sink: ValueSink): Int {
        var values = readProperties(component.propertyList.all, sink)
        if (component is ComponentContainer<*>) {
            for (child in component.componentList.all) {
                values += readComponent(child, sink)
            }
        }
        return values
    }

    private fun readProperties(properties: List<Property>, sink: ValueSink): Int {
        for (property in properties) {
            sink.consume(primaryValue(property))
            for (parameter in property.parameterList.all) {
                sink.consume(parameter.value)
            }
        }
        return properties.size
    }

    private fun primaryValue(property: Property): Any? = when (property) {
        is Trigger -> property.duration ?: property.date
        is DateProperty<*> -> property.date
        is DateListProperty<*> -> property.dates
        is RRule<*> -> property.recur
        is Attendee -> property.calAddress
        is Organizer -> property.calAddress
        is Sequence -> property.sequenceNo
        is TzOffsetFrom -> property.offset
        is TzOffsetTo -> property.offset
        is Categories -> property.categories
        is Version -> property.maxVersion
        else -> property.value
    }
}

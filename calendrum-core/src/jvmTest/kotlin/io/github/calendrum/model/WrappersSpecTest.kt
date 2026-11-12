package io.github.calendrum.model

import io.github.calendrum.syntax.ICalComponent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WrappersSpecTest {
    private val accessors: Map<String, Map<String, (ICalComponent) -> Any?>> = mapOf(
        "VCALENDAR" to mapOf(
            "CALSCALE" to { c -> VCalendar(c).calScale },
            "METHOD" to { c -> VCalendar(c).method },
            "PRODID" to { c -> VCalendar(c).prodId },
            "VERSION" to { c -> VCalendar(c).version },
            "NAME" to { c -> VCalendar(c).names },
            "DESCRIPTION" to { c -> VCalendar(c).descriptions },
            "UID" to { c -> VCalendar(c).uid },
            "LAST-MODIFIED" to { c -> VCalendar(c).lastModified },
            "URL" to { c -> VCalendar(c).url },
            "CATEGORIES" to { c -> VCalendar(c).categories },
            "REFRESH-INTERVAL" to { c -> VCalendar(c).refreshInterval },
            "SOURCE" to { c -> VCalendar(c).source },
            "COLOR" to { c -> VCalendar(c).color },
            "IMAGE" to { c -> VCalendar(c).images },
        ),
        "VEVENT" to mapOf(
            "ATTACH" to { c -> VEvent(c).attachments },
            "CATEGORIES" to { c -> VEvent(c).categories },
            "CLASS" to { c -> VEvent(c).classification },
            "COMMENT" to { c -> VEvent(c).comments },
            "DESCRIPTION" to { c -> VEvent(c).description },
            "GEO" to { c -> VEvent(c).geo },
            "LOCATION" to { c -> VEvent(c).location },
            "PRIORITY" to { c -> VEvent(c).priority },
            "RESOURCES" to { c -> VEvent(c).resources },
            "STATUS" to { c -> VEvent(c).status },
            "SUMMARY" to { c -> VEvent(c).summary },
            "DTEND" to { c -> VEvent(c).end },
            "DTSTART" to { c -> VEvent(c).start },
            "DURATION" to { c -> VEvent(c).duration },
            "TRANSP" to { c -> VEvent(c).transparency },
            "ATTENDEE" to { c -> VEvent(c).attendees },
            "CONTACT" to { c -> VEvent(c).contacts },
            "ORGANIZER" to { c -> VEvent(c).organizer },
            "RECURRENCE-ID" to { c -> VEvent(c).recurrenceId },
            "RELATED-TO" to { c -> VEvent(c).relatedTo },
            "URL" to { c -> VEvent(c).url },
            "UID" to { c -> VEvent(c).uid },
            "EXDATE" to { c -> VEvent(c).exdates },
            "RDATE" to { c -> VEvent(c).rdates },
            "RRULE" to { c -> VEvent(c).rrule },
            "CREATED" to { c -> VEvent(c).created },
            "DTSTAMP" to { c -> VEvent(c).stamp },
            "LAST-MODIFIED" to { c -> VEvent(c).lastModified },
            "SEQUENCE" to { c -> VEvent(c).sequence },
            "REQUEST-STATUS" to { c -> VEvent(c).requestStatuses },
            "COLOR" to { c -> VEvent(c).color },
            "IMAGE" to { c -> VEvent(c).images },
            "CONFERENCE" to { c -> VEvent(c).conferences },
            "STYLED-DESCRIPTION" to { c -> VEvent(c).styledDescriptions },
            "STRUCTURED-DATA" to { c -> VEvent(c).structuredData },
            "LINK" to { c -> VEvent(c).links },
            "CONCEPT" to { c -> VEvent(c).concepts },
            "REFID" to { c -> VEvent(c).refIds },
        ),
        "VTODO" to mapOf(
            "ATTACH" to { c -> VTodo(c).attachments },
            "CATEGORIES" to { c -> VTodo(c).categories },
            "CLASS" to { c -> VTodo(c).classification },
            "COMMENT" to { c -> VTodo(c).comments },
            "DESCRIPTION" to { c -> VTodo(c).description },
            "GEO" to { c -> VTodo(c).geo },
            "LOCATION" to { c -> VTodo(c).location },
            "PERCENT-COMPLETE" to { c -> VTodo(c).percentComplete },
            "PRIORITY" to { c -> VTodo(c).priority },
            "RESOURCES" to { c -> VTodo(c).resources },
            "STATUS" to { c -> VTodo(c).status },
            "SUMMARY" to { c -> VTodo(c).summary },
            "COMPLETED" to { c -> VTodo(c).completed },
            "DUE" to { c -> VTodo(c).due },
            "DTSTART" to { c -> VTodo(c).start },
            "DURATION" to { c -> VTodo(c).duration },
            "ATTENDEE" to { c -> VTodo(c).attendees },
            "CONTACT" to { c -> VTodo(c).contacts },
            "ORGANIZER" to { c -> VTodo(c).organizer },
            "RECURRENCE-ID" to { c -> VTodo(c).recurrenceId },
            "RELATED-TO" to { c -> VTodo(c).relatedTo },
            "URL" to { c -> VTodo(c).url },
            "UID" to { c -> VTodo(c).uid },
            "EXDATE" to { c -> VTodo(c).exdates },
            "RDATE" to { c -> VTodo(c).rdates },
            "RRULE" to { c -> VTodo(c).rrule },
            "CREATED" to { c -> VTodo(c).created },
            "DTSTAMP" to { c -> VTodo(c).stamp },
            "LAST-MODIFIED" to { c -> VTodo(c).lastModified },
            "SEQUENCE" to { c -> VTodo(c).sequence },
            "REQUEST-STATUS" to { c -> VTodo(c).requestStatuses },
            "COLOR" to { c -> VTodo(c).color },
            "IMAGE" to { c -> VTodo(c).images },
            "CONFERENCE" to { c -> VTodo(c).conferences },
            "STYLED-DESCRIPTION" to { c -> VTodo(c).styledDescriptions },
            "STRUCTURED-DATA" to { c -> VTodo(c).structuredData },
            "LINK" to { c -> VTodo(c).links },
            "CONCEPT" to { c -> VTodo(c).concepts },
            "REFID" to { c -> VTodo(c).refIds },
        ),
        "VJOURNAL" to mapOf(
            "ATTACH" to { c -> VJournal(c).attachments },
            "CATEGORIES" to { c -> VJournal(c).categories },
            "CLASS" to { c -> VJournal(c).classification },
            "COMMENT" to { c -> VJournal(c).comments },
            "DESCRIPTION" to { c -> VJournal(c).descriptions },
            "STATUS" to { c -> VJournal(c).status },
            "SUMMARY" to { c -> VJournal(c).summary },
            "DTSTART" to { c -> VJournal(c).start },
            "ATTENDEE" to { c -> VJournal(c).attendees },
            "CONTACT" to { c -> VJournal(c).contacts },
            "ORGANIZER" to { c -> VJournal(c).organizer },
            "RECURRENCE-ID" to { c -> VJournal(c).recurrenceId },
            "RELATED-TO" to { c -> VJournal(c).relatedTo },
            "URL" to { c -> VJournal(c).url },
            "UID" to { c -> VJournal(c).uid },
            "EXDATE" to { c -> VJournal(c).exdates },
            "RDATE" to { c -> VJournal(c).rdates },
            "RRULE" to { c -> VJournal(c).rrule },
            "CREATED" to { c -> VJournal(c).created },
            "DTSTAMP" to { c -> VJournal(c).stamp },
            "LAST-MODIFIED" to { c -> VJournal(c).lastModified },
            "SEQUENCE" to { c -> VJournal(c).sequence },
            "REQUEST-STATUS" to { c -> VJournal(c).requestStatuses },
            "COLOR" to { c -> VJournal(c).color },
            "IMAGE" to { c -> VJournal(c).images },
            "STYLED-DESCRIPTION" to { c -> VJournal(c).styledDescriptions },
            "STRUCTURED-DATA" to { c -> VJournal(c).structuredData },
            "LINK" to { c -> VJournal(c).links },
            "CONCEPT" to { c -> VJournal(c).concepts },
            "REFID" to { c -> VJournal(c).refIds },
        ),
        "VFREEBUSY" to mapOf(
            "COMMENT" to { c -> VFreeBusy(c).comments },
            "DTEND" to { c -> VFreeBusy(c).end },
            "DTSTART" to { c -> VFreeBusy(c).start },
            "FREEBUSY" to { c -> VFreeBusy(c).freeBusy },
            "ATTENDEE" to { c -> VFreeBusy(c).attendees },
            "CONTACT" to { c -> VFreeBusy(c).contact },
            "ORGANIZER" to { c -> VFreeBusy(c).organizer },
            "URL" to { c -> VFreeBusy(c).url },
            "UID" to { c -> VFreeBusy(c).uid },
            "DTSTAMP" to { c -> VFreeBusy(c).stamp },
            "REQUEST-STATUS" to { c -> VFreeBusy(c).requestStatuses },
            "STYLED-DESCRIPTION" to { c -> VFreeBusy(c).styledDescriptions },
            "RELATED-TO" to { c -> VFreeBusy(c).relatedTo },
            "LINK" to { c -> VFreeBusy(c).links },
            "CONCEPT" to { c -> VFreeBusy(c).concepts },
            "REFID" to { c -> VFreeBusy(c).refIds },
        ),
        "VTIMEZONE" to mapOf(
            "TZID" to { c -> VTimeZone(c).tzid },
            "TZURL" to { c -> VTimeZone(c).tzUrl },
            "LAST-MODIFIED" to { c -> VTimeZone(c).lastModified },
            "RELATED-TO" to { c -> VTimeZone(c).relatedTo },
            "LINK" to { c -> VTimeZone(c).links },
            "CONCEPT" to { c -> VTimeZone(c).concepts },
            "REFID" to { c -> VTimeZone(c).refIds },
        ),
        "STANDARD" to observance { VTimeZone.Standard(it) },
        "DAYLIGHT" to observance { VTimeZone.Daylight(it) },
        "VALARM" to mapOf(
            "ATTACH" to { c -> VAlarm(c).attachments },
            "DESCRIPTION" to { c -> VAlarm(c).description },
            "SUMMARY" to { c -> VAlarm(c).summary },
            "DURATION" to { c -> VAlarm(c).duration },
            "ATTENDEE" to { c -> VAlarm(c).attendees },
            "ACTION" to { c -> VAlarm(c).action },
            "REPEAT" to { c -> VAlarm(c).repeat },
            "TRIGGER" to { c -> VAlarm(c).trigger },
            "UID" to { c -> VAlarm(c).uid },
            "RELATED-TO" to { c -> VAlarm(c).relatedTo },
            "ACKNOWLEDGED" to { c -> VAlarm(c).acknowledged },
            "PROXIMITY" to { c -> VAlarm(c).proximity },
            "STYLED-DESCRIPTION" to { c -> VAlarm(c).styledDescriptions },
            "LINK" to { c -> VAlarm(c).links },
            "CONCEPT" to { c -> VAlarm(c).concepts },
            "REFID" to { c -> VAlarm(c).refIds },
        ),
        "PARTICIPANT" to mapOf(
            "PARTICIPANT-TYPE" to { c -> Participant(c).participantType },
            "UID" to { c -> Participant(c).uid },
            "CALENDAR-ADDRESS" to { c -> Participant(c).calendarAddress },
            "CREATED" to { c -> Participant(c).created },
            "DESCRIPTION" to { c -> Participant(c).description },
            "DTSTAMP" to { c -> Participant(c).stamp },
            "GEO" to { c -> Participant(c).geo },
            "LAST-MODIFIED" to { c -> Participant(c).lastModified },
            "PRIORITY" to { c -> Participant(c).priority },
            "SEQUENCE" to { c -> Participant(c).sequence },
            "STATUS" to { c -> Participant(c).status },
            "SUMMARY" to { c -> Participant(c).summary },
            "URL" to { c -> Participant(c).url },
            "ATTACH" to { c -> Participant(c).attachments },
            "CATEGORIES" to { c -> Participant(c).categories },
            "COMMENT" to { c -> Participant(c).comments },
            "CONTACT" to { c -> Participant(c).contacts },
            "LOCATION" to { c -> Participant(c).locationTexts },
            "REQUEST-STATUS" to { c -> Participant(c).requestStatuses },
            "RELATED-TO" to { c -> Participant(c).relatedTo },
            "RESOURCES" to { c -> Participant(c).resources },
            "STYLED-DESCRIPTION" to { c -> Participant(c).styledDescriptions },
            "STRUCTURED-DATA" to { c -> Participant(c).structuredData },
            "LINK" to { c -> Participant(c).links },
            "CONCEPT" to { c -> Participant(c).concepts },
            "REFID" to { c -> Participant(c).refIds },
        ),
        "VLOCATION" to mapOf(
            "UID" to { c -> VLocation(c).uid },
            "NAME" to { c -> VLocation(c).name },
            "DESCRIPTION" to { c -> VLocation(c).description },
            "GEO" to { c -> VLocation(c).geo },
            "URL" to { c -> VLocation(c).url },
            "LOCATION-TYPE" to { c -> VLocation(c).locationTypes },
            "STRUCTURED-DATA" to { c -> VLocation(c).structuredData },
            "RELATED-TO" to { c -> VLocation(c).relatedTo },
            "LINK" to { c -> VLocation(c).links },
            "CONCEPT" to { c -> VLocation(c).concepts },
            "REFID" to { c -> VLocation(c).refIds },
        ),
        "VRESOURCE" to mapOf(
            "UID" to { c -> VResource(c).uid },
            "NAME" to { c -> VResource(c).name },
            "DESCRIPTION" to { c -> VResource(c).description },
            "GEO" to { c -> VResource(c).geo },
            "RESOURCE-TYPE" to { c -> VResource(c).resourceType },
            "STRUCTURED-DATA" to { c -> VResource(c).structuredData },
            "RELATED-TO" to { c -> VResource(c).relatedTo },
            "LINK" to { c -> VResource(c).links },
            "CONCEPT" to { c -> VResource(c).concepts },
            "REFID" to { c -> VResource(c).refIds },
        ),
    )

    private fun observance(wrap: (ICalComponent) -> VTimeZone.Observance): Map<String, (ICalComponent) -> Any?> = mapOf(
        "COMMENT" to { c -> wrap(c).comments },
        "DTSTART" to { c -> wrap(c).start },
        "TZNAME" to { c -> wrap(c).names },
        "TZOFFSETFROM" to { c -> wrap(c).offsetFrom },
        "TZOFFSETTO" to { c -> wrap(c).offsetTo },
        "RDATE" to { c -> wrap(c).rdates },
        "RRULE" to { c -> wrap(c).rrule },
        "RELATED-TO" to { c -> wrap(c).relatedTo },
        "LINK" to { c -> wrap(c).links },
        "CONCEPT" to { c -> wrap(c).concepts },
        "REFID" to { c -> wrap(c).refIds },
    )

    private val samples = mapOf(
        "BINARY" to "QUJD",
        "CAL-ADDRESS" to "mailto:a@example.com",
        "DATE-TIME" to "20260901T100000Z",
        "DURATION" to "PT1H",
        "FLOAT" to "1.5;2.5",
        "INTEGER" to "1",
        "PERIOD" to "20260901T100000Z/PT1H",
        "RECUR" to "FREQ=DAILY",
        "TEXT" to "X",
        "URI" to "http://example.com",
        "UTC-OFFSET" to "+0100",
    )

    @Test
    fun everyWrapperHasAnAccessorForEveryPropertyOfItsComponent() {
        assertEquals(SpecTable.components.names().toSet(), accessors.keys)
        for (component in SpecTable.components.names()) {
            assertEquals(cardinalities(component).keys, accessors.getValue(component).keys, component)
        }
    }

    @Test
    fun accessorsAreListsExactlyWhereTheComponentAllowsMany() {
        for (component in SpecTable.components.names()) {
            for ((property, cardinality) in cardinalities(component)) {
                val row = SpecTable.properties.row(property)
                val value = if (property == "REQUEST-STATUS") "2.0;Success" else samples.getValue(row["value"])
                val node = ICalComponent.builder(component)
                    .property(property, value)
                    .property(property, value)
                    .build()
                val result = accessors.getValue(component).getValue(property)(node)
                val many = isMultiple(cardinality)
                val valueList = !many && row["shape"] == "list"
                assertEquals(many || valueList, result is List<*>, "$component $property $cardinality")
                if (valueList) {
                    assertTrue((result as List<*>).isNotEmpty(), "$component $property reads the first property")
                } else if (many) {
                    assertTrue((result as List<*>).size >= 2, "$component $property reads every property")
                } else {
                    assertTrue(result != null, "$component $property reads the first property")
                }
            }
        }
    }
}

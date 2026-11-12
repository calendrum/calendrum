package io.github.calendrum.itip

import io.github.calendrum.model.Attendee
import io.github.calendrum.model.Method
import io.github.calendrum.model.NodeBuilder
import io.github.calendrum.model.Properties
import io.github.calendrum.model.ZoneIndex
import io.github.calendrum.model.get
import io.github.calendrum.model.sameAddress
import io.github.calendrum.syntax.ICalComponent

internal const val DEFAULT_PRODID = "-//Calendrum//iTIP//EN"

internal val SCHEDULED = setOf("VEVENT", "VTODO", "VJOURNAL", "VFREEBUSY")

internal fun calendarOf(source: ICalComponent?, method: Method, components: List<ICalComponent>): ICalComponent {
    val builder = NodeBuilder("VCALENDAR", source)
    if (source == null) {
        builder.property("PRODID", DEFAULT_PRODID)
        builder.property("VERSION", "2.0")
    } else if (source.property("PRODID") == null) {
        builder.property("PRODID", DEFAULT_PRODID)
    }
    if (source?.property("VERSION") == null && source != null) builder.property("VERSION", "2.0")
    builder.set(Properties.METHOD, method)
    return builder.children(ZoneIndex(source).usedBy(components) + components).build()
}

internal fun ICalComponent.attendeeList(): List<Attendee> = properties("ATTENDEE").mapNotNull { property ->
    ICalComponent.builder(name).property(property).build()[Properties.ATTENDEE]
}

internal fun address(value: String): String =
    if (value.length > MAILTO.length && value.regionMatches(0, MAILTO, 0, MAILTO.length, ignoreCase = true)) {
        value.substring(MAILTO.length)
    } else {
        value
    }

internal fun sameParty(left: String, right: String): Boolean = sameAddress(address(left), address(right))

private const val MAILTO = "mailto:"

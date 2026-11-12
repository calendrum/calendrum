package io.github.calendrum.compat

import io.github.calendrum.ICal
import io.github.calendrum.jcal.ICalSerializers
import io.github.calendrum.jcal.JCal
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

fun jcalOfInvitation(): String = JCal.encodeToString(ICal.parse(invitation()).components)

fun summariesReadFromJcal(): List<String> = JCal.decodeFromString(jcalOfInvitation()).components
    .flatMap { calendar -> calendar.components("VEVENT") }
    .map { event -> event.property("SUMMARY")?.value.orEmpty() }

fun eventsThroughSerializers(): Int {
    val events = ICal.parse(invitation()).components.single().components("VEVENT")
    val serializer = ListSerializer(ICalSerializers.component)
    return Json.decodeFromString(serializer, Json.encodeToString(serializer, events)).size
}

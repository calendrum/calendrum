package io.github.calendrum.compat

import io.github.calendrum.ICal
import io.github.calendrum.ktor.ICalContentConverter
import io.github.calendrum.ktor.icsFeed
import io.github.calendrum.model.calendars
import io.ktor.server.routing.Route

fun ktorConverter(): ICalContentConverter = ICalContentConverter()

fun Route.invitationFeed() {
    icsFeed("/feed.ics") { ICal.parse(invitation()).calendars.single() }
}

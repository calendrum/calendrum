package io.github.calendrum.examples.feed

import io.github.calendrum.ICal
import io.github.calendrum.ktor.IcsFeedOptions
import io.github.calendrum.ktor.iCalendar
import io.github.calendrum.ktor.icsFeed
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.VEvent
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.tzdata.TzData
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.datetime.LocalDateTime

fun main() {
    embeddedServer(CIO, port = 8080) { module() }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) { iCalendar() }
    val options = IcsFeedOptions.builder()
        .cacheControl("max-age=300")
        .writer(ICal.writer(WriteOptions.builder().addMissingTimeZones(TzData).build()))
        .build()
    routing {
        icsFeed("/team/{team}.ics", options) { teamCalendar(parameters["team"].orEmpty()) }
        post("/echo") { call.respond(call.receive<VCalendar>()) }
    }
}

fun teamCalendar(team: String): VCalendar {
    val standup = VEvent.builder()
        .uid("standup-$team@example.com")
        .stamp(ICalDateTime.Utc(kotlin.time.Instant.parse("2026-09-01T08:00:00Z")))
        .start(ICalDateTime.Zoned(LocalDateTime(2026, 9, 7, 9, 30), "Europe/Berlin"))
        .summary("Stand-up of $team")
        .rrule(Recur.builder(Frequency.DAILY).count(20).build())
        .build()
    return VCalendar.builder().prodId("-//Calendrum//Feed example//EN").event(standup).build()
}

package io.github.calendrum.samples

import io.github.calendrum.ICal
import io.github.calendrum.ktor.iCalendar
import io.github.calendrum.ktor.icsFeed
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation

private fun loadCalendar(team: String?): VCalendar = ICal.parse(
    "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\nX-WR-CALNAME:$team\r\nEND:VCALENDAR\r\n",
).calendars.single()

fun serveAnIcsFeed() = testApplication {
    install(ContentNegotiation) { iCalendar() }
    routing {
        icsFeed("/calendar.ics") { loadCalendar(parameters["team"]) }
    }
    val client = createClient { install(ClientNegotiation) { iCalendar() } }

    val first = client.get("/calendar.ics?team=core")
    val calendar: VCalendar = first.body()
    val again = client.get("/calendar.ics?team=core") {
        header(HttpHeaders.IfNoneMatch, first.headers[HttpHeaders.ETag])
    }

    assertEquals("core", calendar.component.property("X-WR-CALNAME")?.value)
    assertEquals(HttpStatusCode.NotModified, again.status)
}

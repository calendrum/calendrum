package io.github.calendrum.ktor.guides

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
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation

class KtorGuide {
    private fun loadCalendar(team: String?): VCalendar = ICal.parse(
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Guides//EN\r\nX-WR-CALNAME:$team\r\nEND:VCALENDAR\r\n",
    ).calendars.single()

    @Test
    fun serverAndClient() = testApplication {
        // region ktor-server
        install(ContentNegotiation) { iCalendar() } // text/calendar for receive() and respond()
        routing {
            // ETag, Cache-Control: no-cache, 304 on If-None-Match
            icsFeed("/calendar.ics") { loadCalendar(parameters["team"]) }
            post("/import") {
                val calendar = call.receive<VCalendar>()
                call.respond(calendar)
            }
        }
        // endregion
        // region ktor-client
        val client = createClient { install(ClientNegotiation) { iCalendar() } }
        val calendar: VCalendar = client.get("/calendar.ics?team=core").body() // the charset is detected
        // endregion
        assertEquals("core", calendar.component.property("X-WR-CALNAME")?.value)
        val first = client.get("/calendar.ics?team=core")
        val tag = first.headers[HttpHeaders.ETag]!!
        val again = client.get("/calendar.ics?team=core") { header(HttpHeaders.IfNoneMatch, tag) }
        assertEquals(HttpStatusCode.NotModified, again.status)
    }
}

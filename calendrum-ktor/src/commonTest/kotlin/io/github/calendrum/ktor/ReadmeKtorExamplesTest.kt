package io.github.calendrum.ktor

import io.github.calendrum.ICal
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation

class ReadmeKtorExamplesTest {
    private fun loadCalendar(team: String?): VCalendar = ICal.parse(
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nX-WR-CALNAME:$team\r\nEND:VCALENDAR\r\n",
    ).calendars.single()

    @Test
    fun theKtorSnippetOfTheReadmeCompilesAndWorks() = testApplication {
        install(ContentNegotiation) { iCalendar() }
        routing {
            icsFeed("/calendar.ics") { loadCalendar(parameters["team"]) }
            post("/import") {
                val calendar = call.receive<VCalendar>()
                call.respond(calendar)
            }
        }
        val client = createClient { install(ClientNegotiation) { iCalendar() } }
        val url = "/calendar.ics?team=core"
        val calendar: VCalendar = client.get(url).body()
        assertEquals("core", calendar.component.property("X-WR-CALNAME")?.value)
    }
}

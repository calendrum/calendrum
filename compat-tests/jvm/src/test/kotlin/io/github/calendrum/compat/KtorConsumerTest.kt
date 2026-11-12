package io.github.calendrum.compat

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
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation

class KtorConsumerTest {
    @Test
    fun servesAndReadsAFeed() = testApplication {
        routing { icsFeed("/feed.ics") { ICal.parse(invitation()).calendars.single() } }
        val client = createClient { install(ClientNegotiation) { iCalendar() } }
        val response = client.get("/feed.ics")
        assertEquals(listOf("Planning", "Retro"), response.body<VCalendar>().events.map { it.summary })
        val tag = response.headers[HttpHeaders.ETag].orEmpty()
        assertEquals(
            HttpStatusCode.NotModified,
            client.get("/feed.ics") {
                header(HttpHeaders.IfNoneMatch, tag)
            }.status,
        )
    }
}

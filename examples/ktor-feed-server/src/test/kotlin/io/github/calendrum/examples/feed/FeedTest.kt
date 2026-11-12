package io.github.calendrum.examples.feed

import io.github.calendrum.ktor.iCalendar
import io.github.calendrum.model.VCalendar
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation

class FeedTest {
    @Test
    fun theFeedServesATeamCalendarWithItsTimeZone() = testApplication {
        application { module() }
        val client = createClient { install(ClientNegotiation) { iCalendar() } }
        val response = client.get("/team/core.ics")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("max-age=300", response.headers[HttpHeaders.CacheControl])
        val calendar = response.body<VCalendar>()
        assertEquals("Stand-up of core", calendar.events.single().summary)
        assertTrue(calendar.timeZones.any { it.tzid == "Europe/Berlin" })
        val tag = response.headers[HttpHeaders.ETag].orEmpty()
        assertEquals(
            HttpStatusCode.NotModified,
            client.get("/team/core.ics") {
                header(HttpHeaders.IfNoneMatch, tag)
            }.status,
        )
    }
}

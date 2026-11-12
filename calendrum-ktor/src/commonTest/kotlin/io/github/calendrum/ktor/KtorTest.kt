@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.ktor

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.InterceptedLine
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.ParseResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.exhausted
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerNegotiation

// MockEngine runs inside testApplication: its block is a coroutine on every platform, so the tests need no
// kotlinx-coroutines-test.
class KtorTest {
    private val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nBEGIN:VEVENT\r\nUID:1\r\n" +
        "SUMMARY:Planning\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
    private val calendar: VCalendar = ICal.parse(text).calendars.single()

    @Test
    fun theConverterReadsAndWritesOnTheServerAndTheClient() = testApplication {
        install(ServerNegotiation) { iCalendar() }
        routing {
            post("/calendar") { call.respond(call.receive<VCalendar>()) }
            post("/component") { call.respond(call.receive<ICalComponent>()) }
            post("/list") { call.respond(call.receive<List<ICalComponent>>()) }
            post("/calendars") { call.respond(call.receive<List<VCalendar>>()) }
            post("/result") { call.respond(call.receive<ParseResult>().components.size.toString()) }
        }
        val client = createClient { install(ClientNegotiation) { iCalendar() } }
        val echoed = client.post("/calendar") {
            contentType(TextCalendar)
            setBody(calendar)
        }
        assertEquals("text/calendar; charset=UTF-8", echoed.headers[HttpHeaders.ContentType])
        assertEquals(calendar.component, echoed.body<VCalendar>().component)
        val component = client.post("/component") {
            contentType(TextCalendar)
            setBody(calendar.component)
        }.body<ICalComponent>()
        assertEquals(calendar.component, component)
        val list = client.post("/list") {
            contentType(TextCalendar)
            setBody(listOf(calendar.component, calendar.component))
        }.body<List<ICalComponent>>()
        assertEquals(listOf(calendar.component, calendar.component), list)
        val calendars = client.post("/calendars") {
            contentType(TextCalendar)
            setBody(listOf(calendar))
        }.body<List<VCalendar>>()
        assertEquals(listOf(calendar.component), calendars.map { it.component })
        val count = client.post("/result") {
            contentType(TextCalendar)
            setBody(ICal.parse(text + text))
        }.bodyAsText()
        assertEquals("2", count)
    }

    @Test
    fun theClientUsesTheCharsetOfTheResponse() = testApplication {
        val cyrillic = "Лекция"
        val body = ("BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nSUMMARY:").encodeToByteArray() + windows1251(cyrillic) +
            "\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        val engine = MockEngine { request ->
            assertEquals("/feed.ics", request.url.encodedPath)
            respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "text/calendar; charset=windows-1251"))
        }
        val client = HttpClient(engine) { install(ClientNegotiation) { iCalendar() } }
        val read = client.get("https://example.com/feed.ics").body<ParseResult>()
        assertEquals("windows-1251", read.charset)
        assertEquals(cyrillic, read.calendars.single().events.single().summary)
        val plain =
            HttpClient(
                MockEngine {
                    respond(text, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "text/calendar"))
                },
            ) {
                install(ClientNegotiation) { iCalendar() }
            }
        assertEquals(calendar.component, plain.get("https://example.com/").body<VCalendar>().component)
    }

    @Test
    fun aNonUtf8CharsetIsAHint() = testApplication {
        val problems = ArrayList<String>()
        var intercepted = 0
        val options = ParseOptions.builder()
            .problemListener { problems += it.code.id }
            .intercept { InterceptedCounter { intercepted++ } }
            .build()
        val converter = ICalContentConverter(options, ICal.writer())
        val latin = "BEGIN:VCALENDAR\r\nX:caf".encodeToByteArray() + byteArrayOf(0xE9.toByte()) +
            "\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        val hinted = converter.deserialize(
            Charsets.ISO_8859_1,
            typeInfo<ParseResult>(),
            ByteReadChannel(latin),
        ) as ParseResult
        assertEquals("café", hinted.components.single().property("X")?.value)
        assertTrue(intercepted > 0)
        assertEquals(hinted.problems.map { it.code.id }, problems)
        assertEquals(null, withHint(options, "UTF-8").charset)
        assertEquals("ISO-8859-1", withHint(options, "ISO-8859-1").charset)
        val fixed = ParseOptions.builder().charset("windows-1251").build()
        assertEquals("windows-1251", withHint(fixed, "ISO-8859-1").charset)
        val custom = IcsFeedOptions.builder().writer(ICal.writer()).build()
        assertEquals("no-cache", custom.cacheControl)
    }

    private class InterceptedCounter(private val count: () -> Unit) : LineInterceptor {
        override fun intercept(line: InterceptedLine) {
            count()
        }
    }

    @Test
    fun theConverterIgnoresOtherTypes() = testApplication {
        val converter = ICalContentConverter()
        assertNull(converter.deserialize(Charsets.UTF_8, typeInfo<String>(), ByteReadChannel(text.encodeToByteArray())))
        assertNull(converter.serialize(TextCalendar, Charsets.UTF_8, typeInfo<String>(), "text"))
        assertNull(converter.serialize(TextCalendar, Charsets.UTF_8, typeInfo<List<String>>(), listOf("a")))
        assertEquals("ICalContentConverter", converter.toString())
    }

    // The converter reads one byte more than the limit, and the parser reports the rest as cut.
    @Test
    fun aBodyOverTheInputLimitIsTruncatedNotThrown() = testApplication {
        val limits = ParseLimits.builder().maxInputOctets(text.length.toLong()).build()
        val converter = ICalContentConverter(ParseOptions.builder().limits(limits).build(), ICal.writer())
        val body = ByteReadChannel((text + "X-PADDING:" + "x".repeat(100_000) + "\r\n").encodeToByteArray())
        val result = converter.deserialize(Charsets.UTF_8, typeInfo<ParseResult>(), body) as ParseResult
        assertTrue(result.isTruncated)
        assertEquals(listOf(ProblemCode.InputTooLarge), result.problems.map { it.code })
        assertEquals(1, result.calendars.single().events.size)
        assertTrue(!body.exhausted(), "the body was read beyond the limit")
        val exact = ByteReadChannel(text.encodeToByteArray())
        val whole = converter.deserialize(Charsets.UTF_8, typeInfo<ParseResult>(), exact) as ParseResult
        assertEquals(emptyList(), whole.problems)
    }

    @Test
    fun aFeedHasAnEtagAndAnswersNotModified() = testApplication {
        var current = calendar
        routing {
            icsFeed("/feed.ics") { current }
            icsFeed("/cached.ics", IcsFeedOptions.builder().cacheControl("max-age=300").build()) { current }
        }
        val first = client.get("/feed.ics")
        assertEquals(HttpStatusCode.OK, first.status)
        assertEquals("text/calendar; charset=UTF-8", first.headers[HttpHeaders.ContentType])
        assertEquals("no-cache", first.headers[HttpHeaders.CacheControl])
        assertTrue(ICal.writer().encodeToByteArray(calendar.component).contentEquals(first.readRawBytes()))
        val tag = first.headers[HttpHeaders.ETag]!!
        assertTrue(tag.startsWith("W/\"") && tag.length == 20, tag)
        val again = client.get("/feed.ics") { header(HttpHeaders.IfNoneMatch, tag) }
        assertEquals(HttpStatusCode.NotModified, again.status)
        assertEquals(tag, again.headers[HttpHeaders.ETag])
        assertEquals(0, again.readRawBytes().size)
        assertEquals(
            HttpStatusCode.NotModified,
            client.get("/feed.ics") { header(HttpHeaders.IfNoneMatch, "\"other\", ${tag.removePrefix("W/")}") }.status,
        )
        assertEquals(
            HttpStatusCode.NotModified,
            client.get("/feed.ics") {
                header(HttpHeaders.IfNoneMatch, "*")
            }.status,
        )
        assertEquals(HttpStatusCode.OK, client.get("/feed.ics") { header(HttpHeaders.IfNoneMatch, "\"other\"") }.status)
        val reordered = text.replace("UID:1\r\nSUMMARY:Planning", "SUMMARY:Planning\r\nUID:1")
        current = ICal.parse(reordered).calendars.single()
        assertEquals(tag, client.get("/feed.ics").headers[HttpHeaders.ETag])
        current = ICal.parse(text.replace("Planning", "Retro")).calendars.single()
        val changed = client.get("/feed.ics") { header(HttpHeaders.IfNoneMatch, tag) }
        assertEquals(HttpStatusCode.OK, changed.status)
        assertTrue(changed.headers[HttpHeaders.ETag] != tag)
        assertEquals("max-age=300", client.get("/cached.ics").headers[HttpHeaders.CacheControl])
        assertEquals("IcsFeedOptions(cacheControl=no-cache)", IcsFeedOptions.Default.toString())
        assertEquals("IcsFeedOptions.Builder", IcsFeedOptions.builder().toString())
    }

    private fun windows1251(text: String): ByteArray = ByteArray(text.length) { index ->
        val char = text[index]
        if (char in 'А'..'я') (char.code - 'А'.code + 0xC0).toByte() else char.code.toByte()
    }
}

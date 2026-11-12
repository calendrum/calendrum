@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum

import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tz.toInstant
import kotlinx.datetime.IllegalTimeZoneException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class SwiftInteropTest {
    private val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTART;TZID=Nowhere:20260601T100000\r\n" +
        "END:VEVENT\r\nEND:VCALENDAR\r\n"

    @Test
    fun theThrowingIteratorMethodsMatchTheIterator() {
        val stream = ICal.parser().components(text.encodeToByteArray())
        assertTrue(stream.hasNextOrThrow())
        assertEquals("VEVENT", stream.nextOrThrow().name)
        assertFalse(stream.hasNextOrThrow())
        assertFailsWith<NoSuchElementException> { stream.nextOrThrow() }
        val strict = ICal.parser(ParseOptions.builder().mode(ParseMode.Strict).build())
        val broken = strict.components("BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n".encodeToByteArray())
        assertFailsWith<ICalParseException> { broken.hasNextOrThrow() }
        assertFailsWith<ICalParseException> { broken.nextOrThrow() }
    }

    @Test
    fun theFailPolicyIsTheExceptionSwiftSees() {
        val start = ICal.parse(text).calendars.single().events.single().start!!
        val fail = TimeZoneResolvers.chain(unknown = UnknownZonePolicy.Fail)
        assertFailsWith<IllegalTimeZoneException> { fail.resolve("Nowhere", ResolveContext.Empty) }
        assertFailsWith<IllegalTimeZoneException> { start.toInstant(fail) }
    }

    @Test
    fun aSchemaEncodesIntoAPreparedCalendar() {
        class Item(val uid: String)
        val schema = object : IcsSchema<Item>("VEVENT") {
            private val uid = field(Properties.UID, Item::uid)

            override fun create(values: Values): Item = Item(values[uid])
        }
        val clock = object : Clock {
            override fun now(): Instant = Instant.parse("2026-09-01T08:00:00Z")
        }
        val header = VCalendar.builder().prodId("-//t//EN").build()
        val bytes = ICal.writer().encode(listOf(Item("a"), Item("b")), schema, header, BuildDefaults(clock) { "x" })
        val calendar = ICal.parse(bytes).calendars.single()
        assertEquals("-//t//EN", calendar.prodId)
        assertEquals(listOf("a", "b"), calendar.events.map { it.uid })
        assertFailsWith<IllegalStateException> { ICal.writer().encode(listOf(Item("a")), schema, header) }
    }
}

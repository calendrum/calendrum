@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.syntax.WriteOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class CanonicalTest {
    private fun lines(vararg lines: String): VCalendar =
        ICal.parse(lines.joinToString("") { "$it\r\n" }).calendars.single()

    private fun hex(bytes: ByteArray): String =
        bytes.joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }

    @Test
    fun uuidV5MatchesRfc9562() {
        assertEquals(
            "2ed6657d-e927-568b-95e1-2665a8aea6a2",
            Canonical.uidV5(DNS, "www.example.com"),
        )
        assertEquals(
            Canonical.uidV5(DNS, "www.example.com"),
            Canonical.uidV5("6BA7B810-9DAD-11D1-80B4-00C04FD430C8", "www.example.com"),
        )
        val derived = Canonical.uidV5("example.com", "lesson/42")
        val space = Canonical.uidV5(DNS, "example.com")
        assertEquals(Canonical.uidV5(space, "lesson/42"), derived)
        assertEquals('5', derived[14])
        assertTrue(derived[19] in "89ab")
        assertNotEquals(derived, Canonical.uidV5("example.com", "lesson/43"))
        var counter = 0
        val uids = UidGenerator.v5("example.com") { "lesson/${++counter}" }
        assertEquals(Canonical.uidV5("example.com", "lesson/1"), uids.next())
        assertEquals(Canonical.uidV5("example.com", "lesson/2"), uids.next())
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", hex(sha1("abc".encodeToByteArray())))
        assertEquals(
            "84983e441c3bd26ebaae4aa1f95129e5e54670f1",
            hex(sha1("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".encodeToByteArray())),
        )
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", hex(sha1(ByteArray(0))))
        assertEquals("34aa973cd4c4daa4f61eeb2bdbad27316534016f", hex(sha1(ByteArray(1_000_000) { 'a'.code.toByte() })))
    }

    @Test
    fun canonicalFormIgnoresOrderAndFolding() {
        val first = lines(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN", "BEGIN:VEVENT", "UID:1",
            "SUMMARY:A very long summary that is folded", "  over two lines",
            "ATTENDEE;RSVP=TRUE;CN=Bob:mailto:b@example.com", "DTSTART:20261005T100000Z", "END:VEVENT",
            "BEGIN:VEVENT", "UID:2", "DTSTART:20261006T100000Z", "END:VEVENT", "END:VCALENDAR",
        )
        val second = lines(
            "BEGIN:VCALENDAR", "PRODID:-//t//EN", "VERSION:2.0", "BEGIN:VEVENT", "UID:2", "DTSTART:20261006T100000Z",
            "END:VEVENT", "BEGIN:VEVENT", "DTSTART:20261005T100000Z", "attendee;cn=Bob;rsvp=TRUE:mailto:b@example.com",
            "UID:1", "SUMMARY:A very long summary that is folded over two lines", "END:VEVENT", "END:VCALENDAR",
        )
        assertEquals(Canonical.canonicalize(first).decodeToString(), Canonical.canonicalize(second).decodeToString())
        val text = Canonical.canonicalize(first).decodeToString()
        val head = "BEGIN:VCALENDAR\r\nPRODID:-//t//EN\r\nVERSION:2.0\r\nBEGIN:VEVENT\r\nATTENDEE;CN=Bob;RSVP=TRUE:"
        assertTrue(text.startsWith(head), text)
        assertNotEquals(text, Canonical.canonicalize(first.edit { prodId = "-//x//EN" }).decodeToString())
    }

    @Test
    fun feedPublisherKeepsStableFieldsAndBumpsChangedOnes() {
        var now = Instant.parse("2026-10-01T08:00:00Z")
        val publisher = FeedPublisher(
            object : Clock {
                override fun now(): Instant = now
            },
        )
        val feed = lines(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN",
            "BEGIN:VEVENT", "UID:1", "DTSTAMP:20200101T000000Z", "DTSTART:20261005T100000Z", "SUMMARY:A", "END:VEVENT",
            "BEGIN:VEVENT", "UID:2", "DTSTAMP:20200101T000000Z", "DTSTART:20261006T100000Z", "SUMMARY:B", "END:VEVENT",
            "END:VCALENDAR",
        )
        val first = publisher.publish(null, feed)
        assertEquals(listOf(STAMP, STAMP), first.events.map { it.lastModified.toString() })
        assertEquals(listOf(null, null), first.events.map { it.sequence })
        now = Instant.parse("2026-10-02T08:00:00Z")
        val regenerated = feed.edit { }
        val same = publisher.publish(first, regenerated)
        assertEquals(ICal.writer().encodeToString(first.component), ICal.writer().encodeToString(same.component))
        now = Instant.parse("2026-10-03T08:00:00Z")
        val changed = feed.toBuilder().apply {
            val second = events.items[1]
            events.replace(second, second.edit { summary = "B2" })
        }.build()
        val next = publisher.publish(same, changed)
        assertEquals(listOf(null, 1), next.events.map { it.sequence })
        assertEquals(
            listOf("2026-10-01T08:00:00Z", "2026-10-03T08:00:00Z"),
            next.events.map { it.lastModified.toString() },
        )
        assertEquals("2026-10-03T08:00:00Z", next.events[1].stamp.toString())
        val added = changed.toBuilder().event(
            lines("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:3", "DTSTART:20261007T100000Z", "END:VEVENT", "END:VCALENDAR")
                .events.single(),
        ).build()
        val withNew = publisher.publish(next, added)
        assertEquals(listOf(null, 1, null), withNew.events.map { it.sequence })
        assertEquals("2026-10-03T08:00:00Z", withNew.events[2].stamp.toString())
        assertTrue(publisher.toString().startsWith("FeedPublisher("))
    }

    // Found by fuzzing: parsing keeps what canonical writing rejects, and canonicalize throws the
    // documented IllegalArgumentException for it, like ICalWriter.
    @Test
    fun whatTheCanonicalWriterRejectsCannotBeCanonicalized() {
        val head = arrayOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN", "BEGIN:VEVENT", "UID:1")
        val control = lines(*head, "SUMMARY:a\u0001b", "END:VEVENT", "END:VCALENDAR")
        val noValue = lines(*head, "X-P;A:v", "END:VEVENT", "END:VCALENDAR")
        for (calendar in listOf(control, noValue)) {
            assertFailsWith<IllegalArgumentException> { CANONICAL.encodeToByteArray(calendar.component) }
            assertFailsWith<IllegalArgumentException> { Canonical.canonicalize(calendar) }
        }
    }

    private companion object {
        val CANONICAL = ICal.writer(WriteOptions.builder().byteStable(false).build())
        const val DNS = "6ba7b810-9dad-11d1-80b4-00c04fd430c8"
        const val STAMP = "2026-10-01T08:00:00Z"
    }
}

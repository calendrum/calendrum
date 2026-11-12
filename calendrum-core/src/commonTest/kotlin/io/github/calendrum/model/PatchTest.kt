@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.syntax.crlf
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class PatchTest {
    private val invitation = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Example//Patch//EN",
        "METHOD:REQUEST",
        "BEGIN:VEVENT",
        "UID:42@example.com",
        "DTSTAMP:20260901T080000Z",
        "DTSTART:20261001T100000Z",
        "SUMMARY:Planning with a summary long enough to be folded by the writer of",
        "  the organizer",
        "attendee;x-vendor=keep;Partstat=NEEDS-ACTION;CN=\"Me\":mailto:me@example.com",
        "ATTENDEE;PARTSTAT=NEEDS-ACTION;CN=Other:mailto:other@example.com",
        "COMMENT:one",
        "COMMENT:two",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER:-PT15M",
        "DESCRIPTION:Soon",
        "END:VALARM",
        "END:VEVENT",
        "BEGIN:VEVENT",
        "UID:42@example.com",
        "RECURRENCE-ID:20261008T100000Z",
        "DTSTAMP:20260901T080000Z",
        "DTSTART:20261008T120000Z",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    private val now = ICalDateTime.Utc(LocalDateTime(2026, 9, 2, 9, 30).toInstant(TimeZone.UTC))

    private fun lines(text: String): List<String> = text.split("\r\n")

    @Test
    fun theProposalPatchChangesOnlyTheTouchedLines() {
        val result = ICalPatch(invitation.encodeToByteArray()) {
            component(uid = "42@example.com", recurrenceId = null) {
                attendee("mailto:me@example.com") { set(Parameters.PARTSTAT, PartStat.Accepted) }
                set(Properties.DTSTAMP, now)
            }
        }.apply()
        val expected = invitation
            .replace("DTSTAMP:20260901T080000Z\r\nDTSTART:20261001", "DTSTAMP:20260902T093000Z\r\nDTSTART:20261001")
            .replace(
                "attendee;x-vendor=keep;Partstat=NEEDS-ACTION;CN=\"Me\":mailto:me@example.com",
                "ATTENDEE;X-VENDOR=keep;PARTSTAT=ACCEPTED;CN=Me:mailto:me@example.com",
            )
        assertEquals(expected, result.bytes.decodeToString())
        assertEquals(emptyList(), result.unmatched)
        assertEquals(
            listOf(PatchChange.Kind.REPLACED to 7, PatchChange.Kind.REPLACED to 11),
            result.changes.map { it.kind to it.line },
        )
        assertEquals(listOf("DTSTAMP", "ATTENDEE"), result.changes.map { it.property })
        val changed = lines(result.bytes.decodeToString()).zip(lines(invitation)).count { it.first != it.second }
        assertEquals(2, changed)
    }

    @Test
    fun selectorsThatFindNothingAreReported() {
        val bytes = invitation.encodeToByteArray()
        val result = ICalPatch(bytes) {
            component("missing@example.com", null) { set(Properties.SUMMARY, "x") }
            component("42@example.com", "20261008T100000Z") {
                attendee("mailto:nobody@example.com") { set("X-A", "1") }
                property("LOCATION") { set("X-B", "2") }
            }
        }.apply()
        assertSame(bytes, result.bytes)
        assertEquals(
            listOf(
                PatchSelector("missing@example.com", null, null, null),
                PatchSelector("42@example.com", "20261008T100000Z", "ATTENDEE", "mailto:nobody@example.com"),
                PatchSelector("42@example.com", "20261008T100000Z", "LOCATION", null),
            ),
            result.unmatched,
        )
        assertEquals(emptyList(), result.changes)
    }

    @Test
    fun theOverrideIsSelectedByItsRecurrenceId() {
        val result = ICalPatch(invitation.encodeToByteArray()) {
            component("42@example.com", "20261008T100000Z") {
                set(Properties.SUMMARY, "Moved")
                set(Properties.SEQUENCE, 2)
            }
        }.apply()
        val text = result.bytes.decodeToString()
        assertEquals(
            invitation.replace(
                "DTSTART:20261008T120000Z\r\nEND:VEVENT",
                "DTSTART:20261008T120000Z\r\nSUMMARY:Moved\r\nSEQUENCE:2\r\nEND:VEVENT",
            ),
            text,
        )
        assertEquals(listOf(PatchChange.Kind.INSERTED, PatchChange.Kind.INSERTED), result.changes.map { it.kind })
        assertEquals(listOf(26, 27), result.changes.map { it.line })
    }

    @Test
    fun newPropertiesGoBeforeTheChildrenAndRemovedLinesDisappear() {
        val result = ICalPatch(invitation.encodeToByteArray()) {
            component("42@example.com", null) {
                remove(Properties.COMMENT)
                set(Properties.SEQUENCE, 1)
                set(Properties.SUMMARY, "Short")
                remove("x-unknown")
            }
        }.apply()
        val expected = invitation
            .replace("COMMENT:one\r\nCOMMENT:two\r\nBEGIN:VALARM", "SEQUENCE:1\r\nBEGIN:VALARM")
            .replace(
                "SUMMARY:Planning with a summary long enough to be folded by the writer of\r\n  the organizer",
                "SUMMARY:Short",
            )
        assertEquals(expected, result.bytes.decodeToString())
        assertEquals(
            listOf(
                PatchChange.Kind.REPLACED,
                PatchChange.Kind.REMOVED,
                PatchChange.Kind.REMOVED,
                PatchChange.Kind.INSERTED,
            ),
            result.changes.map { it.kind },
        )
    }

    @Test
    fun aLongValueIsFoldedWithTheLineEndingsOfTheInput() {
        val text = invitation.replace("\r\n", "\n")
        val summary = "S".repeat(100)
        val result = ICalPatch(text.encodeToByteArray()) {
            component("42@example.com", null) { set(Properties.SUMMARY, summary) }
        }.apply()
        val patched = result.bytes.decodeToString()
        assertTrue(!patched.contains('\r'))
        assertEquals(
            text.replace(
                "SUMMARY:Planning with a summary long enough to be folded by the writer of\n  the organizer",
                "SUMMARY:" + "S".repeat(67) + "\n " + "S".repeat(33),
            ),
            patched,
        )
        assertEquals(summary, ICal.parse(patched).calendars.single().events.first().summary)
    }

    @Test
    fun aTranscodedInputIsPatchedInUtf8() {
        val text = invitation.replace("SUMMARY:Planning", "SUMMARY:Планёрка")
        val bytes = windows1251(text)
        val result = ICalPatch(bytes) {
            component("42@example.com", null) { attendee("mailto:other@example.com") { remove(Parameters.CN) } }
        }.apply()
        val expected = text.replace(
            "ATTENDEE;PARTSTAT=NEEDS-ACTION;CN=Other:mailto:other@example.com",
            "ATTENDEE;PARTSTAT=NEEDS-ACTION:mailto:other@example.com",
        )
        assertContentEquals(expected.encodeToByteArray(), result.bytes)
        assertEquals(listOf(12), result.changes.map { it.line })
    }

    @Test
    fun aPatchIsTheSameAsAnEditOfTheTree() {
        val bytes = invitation.encodeToByteArray()
        val patched = ICalPatch(bytes) {
            component("42@example.com", null) {
                attendee("mailto:other@example.com") {
                    set(Parameters.PARTSTAT, PartStat.Declined)
                    set("X-NOTE", "a,b", "c")
                }
                set(Properties.DTSTAMP, now)
                remove(Properties.COMMENT)
                set(Properties.LOCATION, Location("Room"))
            }
        }.apply()
        val calendar = ICal.parse(bytes).calendars.single()
        val master = calendar.events.first()
        val edited = calendar.edit {
            events {
                replace(
                    master,
                    master.edit {
                        attendees {
                            replace("mailto:other@example.com") {
                                partStat = PartStat.Declined
                                parameter("X-NOTE", "a,b", "c")
                            }
                        }
                        stamp = now
                        remove(Properties.COMMENT)
                        location = "Room"
                    },
                )
            }
        }
        assertEquals(edited.component, ICal.parse(patched.bytes).components.single())
        val withLanguage = ICalPatch(bytes) {
            component("42@example.com", null) { property(Properties.SUMMARY) { set(Parameters.LANGUAGE, "en") } }
        }.apply()
        val summary = ICal.parse(withLanguage.bytes).calendars.single().events.first().component.property("SUMMARY")!!
        assertEquals(listOf("LANGUAGE"), summary.parameters.map { it.name })
        assertEquals(master.summary, summary.value)
    }

    @Test
    fun anInputWithoutAFinalLineBreakKeepsItsShape() {
        val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nSUMMARY:x"
        val result = ICalPatch(text.encodeToByteArray()) {
            component("1", null) {
                set(Properties.SEQUENCE, 1)
                set(Properties.PRIORITY, 2)
            }
        }.apply()
        assertEquals(text + "\r\nSEQUENCE:1\r\nPRIORITY:2", result.bytes.decodeToString())
        assertEquals(listOf(4, 5), result.changes.map { it.line })
        val bare = "BEGIN:VCALENDAR\rBEGIN:VEVENT\rUID:1\rEND:VEVENT\rEND:VCALENDAR\r"
        val patched = ICalPatch(bare.encodeToByteArray()) {
            component("1", null) { set(Properties.SUMMARY, "S".repeat(80)) }
        }.apply()
        assertEquals(
            bare.replace("UID:1\r", "UID:1\rSUMMARY:" + "S".repeat(67) + "\r " + "S".repeat(13) + "\r"),
            patched.bytes.decodeToString(),
        )
    }

    private fun windows1251(text: String): ByteArray {
        val out = ByteArray(text.length)
        for ((index, char) in text.withIndex()) {
            out[index] = when (char) {
                in 'А'..'я' -> (char.code - 'А'.code + 0xC0).toByte()
                'ё' -> 0xB8.toByte()
                else -> char.code.toByte()
            }
        }
        return out
    }
}

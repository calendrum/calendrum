package io.github.calendrum.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class EnumerationsTest {
    private class Family(val of: (String) -> Any, val known: List<Any>)

    private val families = listOf(
        Family(
            PartStat::of,
            listOf(
                PartStat.NeedsAction,
                PartStat.Accepted,
                PartStat.Declined,
                PartStat.Tentative,
                PartStat.Delegated,
                PartStat.Completed,
                PartStat.InProcess,
            ),
        ),
        Family(Role::of, listOf(Role.Chair, Role.ReqParticipant, Role.OptParticipant, Role.NonParticipant)),
        Family(CuType::of, listOf(CuType.Individual, CuType.Group, CuType.Resource, CuType.Room, CuType.Unknown)),
        Family(RelType::of, listOf(RelType.Parent, RelType.Child, RelType.Sibling)),
        Family(FbType::of, listOf(FbType.Free, FbType.Busy, FbType.BusyUnavailable, FbType.BusyTentative)),
        Family(
            ValueType::of,
            listOf(
                ValueType.Binary, ValueType.Boolean, ValueType.CalAddress, ValueType.Date, ValueType.DateTime,
                ValueType.Duration, ValueType.Float, ValueType.Integer, ValueType.Period, ValueType.Recur,
                ValueType.Text, ValueType.Time, ValueType.Uri, ValueType.UtcOffset,
            ),
        ),
        Family(Encoding::of, listOf(Encoding.EightBit, Encoding.Base64)),
        Family(Related::of, listOf(Related.Start, Related.End)),
        Family(RecurrenceRange::of, listOf(RecurrenceRange.ThisAndFuture)),
        Family(EventStatus::of, listOf(EventStatus.Tentative, EventStatus.Confirmed, EventStatus.Cancelled)),
        Family(
            TodoStatus::of,
            listOf(TodoStatus.NeedsAction, TodoStatus.Completed, TodoStatus.InProcess, TodoStatus.Cancelled),
        ),
        Family(JournalStatus::of, listOf(JournalStatus.Draft, JournalStatus.Final, JournalStatus.Cancelled)),
        Family(Classification::of, listOf(Classification.Public, Classification.Private, Classification.Confidential)),
        Family(Transparency::of, listOf(Transparency.Opaque, Transparency.Transparent)),
        Family(AlarmAction::of, listOf(AlarmAction.Audio, AlarmAction.Display, AlarmAction.Email)),
        Family(
            Method::of,
            listOf(
                Method.Publish,
                Method.Request,
                Method.Reply,
                Method.Add,
                Method.Cancel,
                Method.Refresh,
                Method.Counter,
                Method.DeclineCounter,
            ),
        ),
    )

    @Test
    fun knownValuesAreTheConstantsWithoutRegardToCase() {
        for (family in families) {
            for (constant in family.known) {
                val text = constant.toString()
                assertSame(constant, family.of(text), text)
                assertSame(constant, family.of(text.lowercase()), text)
                assertEquals(text, text.uppercase(), "canonical $text")
            }
        }
    }

    @Test
    fun unknownValuesAreKeptAndCompareByValue() {
        for (family in families) {
            val custom = family.of("x-custom")
            assertEquals("X-CUSTOM", custom.toString())
            assertEquals(custom, family.of("X-Custom"))
            assertEquals(custom.hashCode(), family.of("X-CUSTOM").hashCode())
            assertNotSame(custom, family.of("X-CUSTOM"))
            assertNotEquals(custom, family.known.first())
            assertFailsWith<IllegalArgumentException> { family.of("") }
        }
    }

    @Test
    fun typesStayApartEvenWithTheSameText() {
        assertEquals("CANCELLED", EventStatus.Cancelled.value)
        assertNotEquals<Any>(EventStatus.Cancelled, TodoStatus.Cancelled)
        assertNotEquals<Any>(PartStat.Tentative, EventStatus.Tentative)
        assertEquals("8BIT", Encoding.EightBit.value)
        assertEquals("DECLINECOUNTER", Method.DeclineCounter.value)
        assertEquals("THISANDFUTURE", RecurrenceRange.ThisAndFuture.toString())
    }
}

package io.github.calendrum.compat

import kotlin.test.Test
import kotlin.test.assertEquals

// Runs on every target of the case: a passing test proves that the consumer compiled, linked and loaded calendrum.
class ConsumerTest {
    @Test
    fun readsTheLibraryVersion() {
        assertEquals(EXPECTED_CALENDRUM_VERSION, calendrumVersion())
    }

    @Test
    fun writesTheInvitationBackByteForByte() {
        assertEquals(invitation(), rewrittenInvitation())
    }

    @Test
    fun parsesAnInvitation() {
        assertEquals(listOf("Planning", "Retro"), eventSummaries())
    }

    @Test
    fun readsTheTypedModel() {
        assertEquals(listOf("2026-09-01T09:00:00Z", "2026-09-02T09:00:00Z"), eventStarts())
        assertEquals(listOf("ACCEPTED"), attendeeStatuses())
    }
}

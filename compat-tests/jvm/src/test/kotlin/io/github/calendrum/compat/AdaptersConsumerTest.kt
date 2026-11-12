package io.github.calendrum.compat

import kotlin.test.Test
import kotlin.test.assertEquals

class AdaptersConsumerTest {
    @Test
    fun readsAndWritesThroughKotlinxIoAndOkio() {
        assertEquals(listOf("Planning", "Retro"), summariesThroughKotlinxIo())
        assertEquals(listOf("Planning", "Retro"), summariesThroughOkio())
        assertEquals(listOf(invitation(), invitation()), invitationThroughSinks())
    }
}

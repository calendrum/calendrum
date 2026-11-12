package io.github.calendrum.compat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JCalConsumerTest {
    @Test
    fun writesAndReadsJcal() {
        assertTrue(jcalOfInvitation().startsWith("[[\"vcalendar\","))
        assertEquals(listOf("Planning", "Retro"), summariesReadFromJcal())
        assertEquals(2, eventsThroughSerializers())
    }
}

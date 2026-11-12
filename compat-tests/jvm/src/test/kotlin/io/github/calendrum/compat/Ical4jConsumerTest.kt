package io.github.calendrum.compat

import io.github.calendrum.ICal
import io.github.calendrum.ical4j.toCalendrum
import io.github.calendrum.ical4j.toIcal4j
import io.github.calendrum.model.calendars
import kotlin.test.Test
import kotlin.test.assertEquals

class Ical4jConsumerTest {
    @Test
    fun goesThroughIcal4jAndBack() {
        val calendar = ICal.parse(invitation()).calendars.single()
        val back = calendar.toIcal4j().toCalendrum()
        assertEquals(calendar.component, back.component)
    }
}

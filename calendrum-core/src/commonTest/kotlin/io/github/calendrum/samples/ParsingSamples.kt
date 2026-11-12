package io.github.calendrum.samples

import io.github.calendrum.ICal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun parseText() {
    val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\n" +
        "BEGIN:VEVENT\r\nUID:42@example.com\r\nSUMMARY:Weekly sync\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

    val result = ICal.parse(text)

    assertTrue(result.problems.isEmpty())
    val calendar = result.components.single()
    assertEquals("VCALENDAR", calendar.name)
    assertEquals("VEVENT", calendar.components.single().name)
}

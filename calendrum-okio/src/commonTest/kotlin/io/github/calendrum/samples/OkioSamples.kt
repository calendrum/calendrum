package io.github.calendrum.samples

import io.github.calendrum.ICal
import io.github.calendrum.okio.parse
import io.github.calendrum.okio.write
import okio.Buffer
import kotlin.test.assertEquals

private const val CALENDAR = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\n" +
    "BEGIN:VEVENT\r\nUID:42@example.com\r\nSUMMARY:Weekly sync\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

fun parseAndWriteOkio() {
    val source = Buffer().write(CALENDAR.encodeToByteArray())

    val result = ICal.parser().parse(source)

    assertEquals("VEVENT", result.components.single().components.single().name)
    val sink = Buffer()
    ICal.writer().write(result.components, sink)
    assertEquals(CALENDAR, sink.readUtf8())
}

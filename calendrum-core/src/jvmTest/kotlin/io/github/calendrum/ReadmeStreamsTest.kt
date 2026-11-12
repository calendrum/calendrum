package io.github.calendrum

import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ParseLimits
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ReadmeStreamsTest {
    private val bytes = listOf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "X-WR-TIMEZONE:Europe/Moscow",
        "BEGIN:VEVENT",
        "UID:1@example.com",
        "SUMMARY:Лекция",
        "END:VEVENT",
        "BEGIN:VEVENT",
        "UID:2@example.com",
        "END:VEVENT",
        "END:VCALENDAR",
    ).joinToString("") { "$it\r\n" }.encodeToByteArray()

    @Test
    fun aLargeFeedFromAFileIsIndexedComponentByComponent() {
        val file = File.createTempFile("readme", ".ics")
        val copy = File.createTempFile("readme", ".copy.ics")
        try {
            file.writeBytes(bytes)
            val indexed = mutableListOf<String>()
            fun index(component: ICalComponent, calendarProperties: List<ICalProperty>) {
                indexed += "${component.property("UID")?.value}:${calendarProperties.size}"
            }
            FileInputStream(file).use { input ->
                val events = ICal.parser { limits = ParseLimits.builder().maxTime(2.seconds).build() }.components(input)
                for (component in events) {
                    if (component.name == "VEVENT") index(component, events.calendarProperties)
                }
                assertTrue(events.problems.isEmpty())
            }
            assertEquals(listOf("1@example.com:2", "2@example.com:2"), indexed)
            val result = ICal.parse(bytes)
            FileOutputStream(copy).use { ICal.writer().write(result.components, it) }
            assertTrue(bytes.contentEquals(copy.readBytes()))
        } finally {
            file.delete()
            copy.delete()
        }
    }
}

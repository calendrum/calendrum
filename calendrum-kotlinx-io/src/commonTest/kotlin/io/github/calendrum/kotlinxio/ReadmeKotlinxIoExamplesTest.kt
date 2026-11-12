package io.github.calendrum.kotlinxio

import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadmeKotlinxIoExamplesTest {
    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    private fun buffer(text: String): Buffer = Buffer().apply { writeString(text) }

    @Test
    fun theStreamSnippetOfTheReadmeCompilesAndWorks() {
        val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val source = buffer(text)
        val sink = Buffer()
        val result = ICal.parser().parse(source)
        for (event in ICal.parser().components(buffer(text))) println(event.property("UID")?.value)
        val cursor = ICal.parser().reader(buffer(text))
        ICal.writer().write(result.components, sink)
        val input: ByteInput = buffer(text).asByteInput()
        assertEquals(listOf("1"), printed)
        assertEquals(text, sink.readString())
        cursor.next()
        assertEquals("VCALENDAR", cursor.name)
        assertEquals(ICal.parser().parse(text).components, ICal.parser().parse(input).components)
    }
}

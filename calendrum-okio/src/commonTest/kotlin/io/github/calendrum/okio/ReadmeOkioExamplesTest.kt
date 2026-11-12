package io.github.calendrum.okio

import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadmeOkioExamplesTest {
    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    @Test
    fun theStreamSnippetOfTheReadmeCompilesAndWorks() {
        val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val source = Buffer().writeUtf8(text)
        val sink = Buffer()
        val result = ICal.parser().parse(source)
        for (event in ICal.parser().components(Buffer().writeUtf8(text))) println(event.property("UID")?.value)
        val cursor = ICal.parser().reader(Buffer().writeUtf8(text))
        ICal.writer().write(result.components, sink)
        val input: ByteInput = Buffer().writeUtf8(text).asByteInput()
        assertEquals(listOf("1"), printed)
        assertEquals(text, sink.readUtf8())
        cursor.next()
        assertEquals("VCALENDAR", cursor.name)
        assertEquals(ICal.parser().parse(text).components, ICal.parser().parse(input).components)
    }
}

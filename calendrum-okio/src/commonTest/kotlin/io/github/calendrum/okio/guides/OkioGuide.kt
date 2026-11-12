package io.github.calendrum.okio.guides

import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import io.github.calendrum.okio.asByteInput
import io.github.calendrum.okio.components
import io.github.calendrum.okio.parse
import io.github.calendrum.okio.reader
import io.github.calendrum.okio.write
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class OkioGuide {
    private val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

    private fun buffer(): Buffer = Buffer().writeUtf8(text)

    @Test
    fun streams() {
        val source = buffer()
        val sink = Buffer()
        val uids = ArrayList<String>()
        // region okio-streams
        val result = ICal.parser().parse(source) // okio.BufferedSource
        // one component at a time
        for (event in ICal.parser().components(buffer())) uids += event.property("UID")?.value.orEmpty()
        val cursor = ICal.parser().reader(buffer()) // the event cursor
        // okio.BufferedSink; flushing is yours
        ICal.writer().write(result.components, sink)
        val input: ByteInput = buffer().asByteInput() // for any API that takes ByteInput
        // endregion
        assertEquals(listOf("1"), uids)
        assertEquals(text, sink.readUtf8())
        cursor.next()
        assertEquals("VCALENDAR", cursor.name)
        assertEquals(ICal.parser().parse(text).components, ICal.parser().parse(input).components)
    }
}

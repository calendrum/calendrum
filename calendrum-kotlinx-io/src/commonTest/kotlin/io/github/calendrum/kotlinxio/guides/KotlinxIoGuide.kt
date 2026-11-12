package io.github.calendrum.kotlinxio.guides

import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import io.github.calendrum.kotlinxio.asByteInput
import io.github.calendrum.kotlinxio.components
import io.github.calendrum.kotlinxio.parse
import io.github.calendrum.kotlinxio.reader
import io.github.calendrum.kotlinxio.write
import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinxIoGuide {
    private val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

    private fun buffer(): Buffer = Buffer().apply { writeString(text) }

    @Test
    fun streams() {
        val source = buffer()
        val sink = Buffer()
        val uids = ArrayList<String>()
        // region kotlinx-io-streams
        val result = ICal.parser().parse(source) // kotlinx.io.Source
        // one component at a time
        for (event in ICal.parser().components(buffer())) uids += event.property("UID")?.value.orEmpty()
        val cursor = ICal.parser().reader(buffer()) // the event cursor
        ICal.writer().write(result.components, sink) // kotlinx.io.Sink; flushing is yours
        val input: ByteInput = buffer().asByteInput() // for any API that takes ByteInput
        // endregion
        assertEquals(listOf("1"), uids)
        assertEquals(text, sink.readString())
        cursor.next()
        assertEquals("VCALENDAR", cursor.name)
        assertEquals(ICal.parser().parse(text).components, ICal.parser().parse(input).components)
    }
}

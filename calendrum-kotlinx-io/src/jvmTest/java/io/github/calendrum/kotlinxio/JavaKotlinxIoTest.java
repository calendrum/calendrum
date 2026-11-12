package io.github.calendrum.kotlinxio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import io.github.calendrum.ICal;
import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ParseResult;
import java.nio.charset.StandardCharsets;
import java.util.List;
import kotlinx.io.Buffer;
import kotlinx.io.SourcesKt;
import org.junit.Test;

public class JavaKotlinxIoTest {
    private static final byte[] CALENDAR = ("BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nEND:VEVENT\r\n"
        + "BEGIN:VEVENT\r\nUID:2\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n").getBytes(StandardCharsets.UTF_8);

    private static Buffer source() {
        Buffer buffer = new Buffer();
        buffer.write(CALENDAR, 0, CALENDAR.length);
        return buffer;
    }

    @Test
    public void adaptersFromJava() throws Exception {
        ParseResult result = ICalKotlinxIo.parse(ICal.parser(), source());
        List<ICalComponent> components = result.getComponents();
        assertEquals(ICal.parse(CALENDAR).getComponents(), components);
        ComponentStream stream = ICalKotlinxIo.components(ICal.parser(), source());
        assertEquals("1", stream.next().property("UID").getValue());
        assertNotNull(ICalKotlinxIo.reader(ICal.parser(), source()).next());
        Buffer sink = new Buffer();
        ICalKotlinxIo.write(ICal.writer(), components, sink);
        assertArrayEquals(CALENDAR, SourcesKt.readByteArray(sink, (int) sink.getSize()));
        ICalKotlinxIo.write(ICal.writer(), components.get(0), sink);
        assertArrayEquals(ICal.writer().encodeToByteArray(components.get(0)), SourcesKt.readByteArray(sink, (int) sink.getSize()));
    }
}

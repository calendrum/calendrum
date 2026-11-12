package io.github.calendrum.okio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import io.github.calendrum.ICal;
import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ParseResult;
import java.nio.charset.StandardCharsets;
import java.util.List;
import okio.Buffer;
import org.junit.Test;

public class JavaOkioTest {
    private static final byte[] CALENDAR = ("BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nEND:VEVENT\r\n"
        + "BEGIN:VEVENT\r\nUID:2\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n").getBytes(StandardCharsets.UTF_8);

    @Test
    public void adaptersFromJava() throws Exception {
        ParseResult result = ICalOkio.parse(ICal.parser(), new Buffer().write(CALENDAR));
        List<ICalComponent> components = result.getComponents();
        assertEquals(ICal.parse(CALENDAR).getComponents(), components);
        ComponentStream stream = ICalOkio.components(ICal.parser(), new Buffer().write(CALENDAR));
        assertEquals("1", stream.next().property("UID").getValue());
        assertNotNull(ICalOkio.reader(ICal.parser(), new Buffer().write(CALENDAR)).next());
        Buffer sink = new Buffer();
        ICalOkio.write(ICal.writer(), components, sink);
        assertArrayEquals(CALENDAR, sink.readByteArray());
        ICalOkio.write(ICal.writer(), components.get(0), sink);
        assertArrayEquals(ICal.writer().encodeToByteArray(components.get(0)), sink.readByteArray());
    }
}

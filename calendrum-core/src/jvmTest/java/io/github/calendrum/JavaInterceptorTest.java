package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ICalParser;
import io.github.calendrum.syntax.InterceptedLine;
import io.github.calendrum.syntax.LineInterceptor;
import io.github.calendrum.syntax.ParseOptions;
import io.github.calendrum.syntax.WriteOptions;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class JavaInterceptorTest {
    private static final String FEED = "BEGIN:VCALENDAR\r\n"
        + "X-WR-TIMEZONE:Europe/Moscow\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:1@example.com\r\n"
        + "X-VENDOR-ROOM:Room 101\r\n"
        + "DTSTART:20260101T100000\r\n"
        + "DESCRIPTION:Long\r\n"
        + "END:VEVENT\r\n"
        + "END:VCALENDAR\r\n";

    private static final class FloatingTime implements LineInterceptor {
        private String zone;

        @Override
        public void intercept(InterceptedLine line) {
            if (line.getName().equals("X-WR-TIMEZONE")) zone = line.getValue().toString();
            if (zone != null && line.getName().equals("DTSTART") && line.getParameterCount() == 0) {
                line.setParameter("TZID", zone);
            }
        }
    }

    @Test
    public void interceptorsAreRegisteredFromJava() throws ICalParseException {
        ParseOptions options = ParseOptions.builder()
            .intercept(line -> {
                if (line.getName().equals("X-VENDOR-ROOM")) line.rename("LOCATION");
            })
            .intercept(line -> {
                if (line.getName().equals("DESCRIPTION")) line.drop();
            })
            .intercept(FloatingTime::new)
            .build();
        ICalParser parser = ICal.parser(options);
        ICalComponent event = parser.parse(FEED).getComponents().get(0).getComponents().get(0);
        assertEquals("Room 101", event.property("LOCATION").getValue());
        assertNull(event.property("DESCRIPTION"));
        assertEquals("Europe/Moscow", event.property("DTSTART").parameter("TZID").getValue());
        assertEquals(3, options.getInterceptors().size());

        ComponentStream stream = parser.components(FEED.getBytes(StandardCharsets.UTF_8));
        assertEquals(event, stream.next());
    }

    @Test
    public void writeInterceptorsAreRegisteredFromJava() throws ICalParseException {
        List<String> seen = new ArrayList<>();
        WriteOptions options = WriteOptions.builder()
            .intercept(() -> line -> seen.add(line.getDepth() + " " + line.getComponentName() + " " + line.getName()))
            .intercept(line -> {
                if (line.getName().startsWith("X-")) line.drop();
            })
            .build();
        String written = ICal.writer(options).encodeToString(ICal.parse(FEED).getComponents());
        assertEquals(FEED.replace("X-WR-TIMEZONE:Europe/Moscow\r\n", "").replace("X-VENDOR-ROOM:Room 101\r\n", ""), written);
        assertEquals("2 VEVENT DESCRIPTION", seen.get(seen.size() - 1));
    }

    @Test
    public void strictEditsThrow() throws ICalParseException {
        ParseOptions options = ParseOptions.builder().intercept(line -> line.rename("not a name")).build();
        try {
            ICal.parser(options).parse(FEED);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid character in a property name at index 3", expected.getMessage());
        }
    }
}

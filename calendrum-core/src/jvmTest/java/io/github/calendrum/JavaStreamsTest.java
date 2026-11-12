package io.github.calendrum;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.ContentEventType;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ICalParser;
import io.github.calendrum.syntax.ICalReader;
import io.github.calendrum.syntax.ICalWriter;
import io.github.calendrum.syntax.ParseLimits;
import io.github.calendrum.syntax.ParseMode;
import io.github.calendrum.syntax.ParseOptions;
import io.github.calendrum.syntax.ParseResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.time.TimeSource;
import org.junit.Test;

public class JavaStreamsTest {
    private static final String FEED = "BEGIN:VCALENDAR\r\n"
        + "PRODID:-//ics-mp//Java streams//EN\r\n"
        + "X-WR-TIMEZONE:Europe/Moscow\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:1@example.com\r\n"
        + "SUMMARY:Лекция 😀\r\n"
        + "END:VEVENT\r\n"
        + "BEGIN:VTODO\r\n"
        + "UID:2@example.com\r\n"
        + "END:VTODO\r\n"
        + "END:VCALENDAR\r\n";

    private static final byte[] BYTES = FEED.getBytes(StandardCharsets.UTF_8);

    private static final class Closing extends FilterInputStream {
        boolean closed;

        Closing(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            closed = true;
            super.close();
        }
    }

    @Test
    public void parsesInputStreamsAndReaders() throws IOException, ICalParseException {
        ICalParser parser = ICal.parser();
        Closing stream = new Closing(new ByteArrayInputStream(BYTES));
        ParseResult fromStream = ICalStreams.parse(parser, stream);
        assertFalse(stream.closed);
        ParseResult fromBytes = ICal.parse(BYTES);
        assertEquals(fromBytes.getComponents(), fromStream.getComponents());
        assertEquals(
            fromBytes.getComponents().get(0).getComponents().get(0).getSpan(),
            fromStream.getComponents().get(0).getComponents().get(0).getSpan());

        ParseResult fromReader = ICalStreams.parse(parser, new StringReader(FEED));
        assertEquals(ICal.parse(FEED).getComponents(), fromReader.getComponents());
        assertEquals("UTF-8", fromReader.getCharset());
        assertEquals(
            ICal.parse(FEED).getComponents().get(0).getProperties().get(1).getSpan(),
            fromReader.getComponents().get(0).getProperties().get(1).getSpan());
    }

    @Test
    public void readerInputMatchesStringInputWithBrokenSurrogates() throws IOException, ICalParseException {
        String text = "BEGIN:X\r\nA:\uD83D\r\nB:\uDE00x😀\r\nEND:X\r\n\uD83D";
        ParseResult expected = ICal.parse(text);
        ParseResult actual = ICalStreams.parse(ICal.parser(), new StringReader(text));
        assertEquals(expected.getComponents(), actual.getComponents());
        assertEquals(expected.getProblems().toString(), actual.getProblems().toString());
    }

    @Test
    public void readerChunksKeepSurrogatePairs() throws IOException, ICalParseException {
        StringBuilder text = new StringBuilder("BEGIN:X\r\nA:");
        while (text.length() < 8191) {
            text.append('a');
        }
        text.append("\uD83D\uDE00\r\nB:");
        while (text.length() < 2 * 8192 - 1) {
            text.append('b');
        }
        text.append("\uD83D\r\nEND:X\r\n\uD83D");
        String input = text.toString();
        ParseResult expected = ICal.parse(input);
        ParseResult actual = ICalStreams.parse(ICal.parser(), new StringReader(input));
        assertEquals(expected.getComponents(), actual.getComponents());
        assertEquals(expected.getProblems().toString(), actual.getProblems().toString());
    }

    @Test
    public void streamsComponentsFromAnInputStream() throws IOException, ICalParseException {
        ComponentStream stream = ICalStreams.components(ICal.parser(), new ByteArrayInputStream(BYTES));
        List<String> names = new ArrayList<>();
        while (stream.hasNext()) {
            ICalComponent component = stream.next();
            names.add(component.getName());
            assertEquals(0, stream.getCalendarIndex());
            assertEquals("Europe/Moscow", stream.getCalendarProperties().get(1).getValue());
        }
        assertEquals(Arrays.asList("VEVENT", "VTODO"), names);
        assertTrue(stream.getProblems().isEmpty());

        ComponentStream fromBytes = ICal.parser().components(BYTES);
        assertEquals("VEVENT", fromBytes.next().getName());
        ComponentStream fromInput = ICal.parser().components(ByteInputs.asByteInput(BYTES));
        assertEquals("VEVENT", fromInput.next().getName());
    }

    @Test
    public void cursorReadsAByteInput() throws ICalParseException {
        ICalReader reader = ICal.parser().reader(ICalStreams.asByteInput(new ByteArrayInputStream(BYTES)));
        int properties = 0;
        ContentEventType type;
        while ((type = reader.next()) != null) {
            if (type == ContentEventType.Property) {
                properties++;
            }
        }
        assertEquals(5, properties);
        assertNull(reader.next());
    }

    @Test
    public void writesToOutputStreamsAndWriters() throws IOException, ICalParseException {
        ICalWriter writer = ICal.writer();
        ICalComponent calendar = ICal.parse(BYTES).getComponents().get(0);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ICalStreams.write(writer, calendar, bytes);
        assertArrayEquals(BYTES, bytes.toByteArray());

        ByteArrayOutputStream list = new ByteArrayOutputStream();
        ICalStreams.write(writer, Arrays.asList(calendar, calendar), list);
        assertEquals(2 * BYTES.length, list.size());

        StringWriter text = new StringWriter();
        ICalStreams.write(writer, calendar, text);
        assertEquals(FEED, text.toString());
        StringWriter texts = new StringWriter();
        ICalStreams.write(writer, Arrays.asList(calendar, calendar), texts);
        assertEquals(FEED + FEED, texts.toString());

        ByteArrayOutputStream adapted = new ByteArrayOutputStream();
        writer.write(calendar, ICalStreams.asByteOutput(adapted));
        assertArrayEquals(BYTES, adapted.toByteArray());
    }

    @Test
    public void ioExceptionsReachTheCaller() throws ICalParseException {
        InputStream failing = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("disk gone");
            }
        };
        try {
            ICalStreams.parse(ICal.parser(), failing);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("disk gone", e.getMessage());
        }
        OutputStream full = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("no space");
            }
        };
        try {
            ICalStreams.write(ICal.writer(), ICal.parse(BYTES).getComponents().get(0), full);
            fail("IOException expected");
        } catch (IOException e) {
            assertEquals("no space", e.getMessage());
        }
    }

    @Test
    public void strictStreamsThrowParseExceptions() throws IOException, ICalParseException {
        ParseOptions options = ParseOptions.builder().mode(ParseMode.Strict).build();
        byte[] broken = "BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n".getBytes(StandardCharsets.UTF_8);
        try {
            ICalStreams.parse(ICal.parser(options), new ByteArrayInputStream(broken));
            fail("ICalParseException expected");
        } catch (ICalParseException e) {
            assertEquals("ICAL-S007", e.getProblems().get(0).getCode().getId());
        }
        ComponentStream stream = ICalStreams.components(ICal.parser(options), new ByteArrayInputStream(broken));
        try {
            stream.hasNext();
            fail("ICalParseException expected");
        } catch (Exception e) {
            assertTrue(e instanceof ICalParseException);
            assertEquals("ICAL-S007", ((ICalParseException) e).getProblems().get(0).getCode().getId());
        }
    }

    @Test
    public void timeBudgetOptionsAreReachableFromJava() {
        ParseOptions options = ParseOptions.builder().timeSource(TimeSource.Monotonic.INSTANCE).build();
        assertSame(TimeSource.Monotonic.INSTANCE, options.getTimeSource());
        assertNull(ParseLimits.Default.getMaxTime());
        assertNull(ParseLimits.builder().maxTime(null).build().getMaxTime());
        assertEquals("ICAL-S029", ProblemCode.TimeBudgetExceeded.getId());
    }
}

package io.github.calendrum;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import io.github.calendrum.syntax.ContentEventType;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ICalParameter;
import io.github.calendrum.syntax.ICalParser;
import io.github.calendrum.syntax.ICalProperty;
import io.github.calendrum.syntax.ICalReader;
import io.github.calendrum.syntax.ICalWriter;
import io.github.calendrum.syntax.ParseLimits;
import io.github.calendrum.syntax.ParseMode;
import io.github.calendrum.syntax.ParseOptions;
import io.github.calendrum.syntax.ParseResult;
import io.github.calendrum.syntax.WriteOptions;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import org.junit.Test;

// The key scenarios from Java: static `ICal` calls, the strict mode with a checked `ICalParseException` (`@Throws`),
// builders, a tree walk, `problemCount`.
public class JavaApiTest {
    private static final String INVITE = "BEGIN:VCALENDAR\r\n"
        + "VERSION:2.0\r\n"
        + "PRODID:-//ics-mp//Java test//EN\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:42@example.com\r\n"
        + "SUMMARY:Kickoff\r\n"
        + "ATTENDEE;CN=Jane;ROLE=CHAIR:mailto:jane@example.com\r\n"
        + "BEGIN:VALARM\r\n"
        + "ACTION:DISPLAY\r\n"
        + "END:VALARM\r\n"
        + "END:VEVENT\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:43@example.com\r\n"
        + "SUMMARY:Review\r\n"
        + "END:VEVENT\r\n"
        + "END:VCALENDAR\r\n";

    @Test
    public void parsesBytesAndWalksTheTree() {
        ParseResult result = ICal.parse(INVITE.getBytes(StandardCharsets.UTF_8));
        List<String> summaries = new ArrayList<>();
        for (ICalComponent calendar : result.getComponents()) {
            for (ICalComponent event : calendar.components("VEVENT")) {
                ICalProperty summary = event.property("SUMMARY");
                summaries.add(summary == null ? null : summary.getValue());
            }
        }
        assertEquals(Arrays.asList("Kickoff", "Review"), summaries);
        assertTrue(result.getProblems().isEmpty());
        assertEquals(0, result.problemCount(ProblemCode.BareLf));
        assertFalse(result.isTruncated());
        assertEquals("UTF-8", result.getCharset());
        assertEquals(100, result.getCharsetConfidence());

        int properties = 0;
        int components = 0;
        Deque<ICalComponent> stack = new ArrayDeque<>(result.getComponents());
        while (!stack.isEmpty()) {
            ICalComponent component = stack.pop();
            components++;
            properties += component.getProperties().size();
            for (ICalComponent child : component.getComponents()) {
                stack.push(child);
            }
        }
        assertEquals(4, components);
        assertEquals(8, properties);

        ICalProperty attendee = result.getComponents().get(0).getComponents().get(0).property("attendee");
        assertEquals("Jane", attendee.parameter("CN").getValue());
        assertEquals(Arrays.asList("CHAIR"), attendee.parameter("ROLE").getValues());
        assertEquals(7, attendee.getSpan().getLine());
        assertEquals(1, attendee.getSpan().getColumn());
    }

    @Test
    public void strictModeThrowsParseException() {
        ICalParser strict = ICal.parser(ParseOptions.builder().mode(ParseMode.Strict).build());
        try {
            strict.parse("BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n");
            fail("Strict mode accepted a line without ':'");
        } catch (ICalParseException e) {
            List<Problem> problems = e.getProblems();
            Problem last = problems.get(problems.size() - 1);
            assertEquals(ProblemCode.MissingColon, last.getCode());
            assertEquals(Severity.Error, last.getSeverity());
            assertEquals(2, last.getSpan().getLine());
        }
    }

    @Test
    public void optionsAndListenersFromJava() throws ICalParseException {
        List<Problem> seen = new ArrayList<>();
        ParseOptions options = ParseOptions.builder()
            .limits(ParseLimits.builder().maxProblems(1).maxDepth(8).build())
            .problemListener(seen::add)
            .retainSource(false)
            .charset("windows-1251")
            .build();
        ParseResult result = ICal.parser(options).parse(INVITE.replace("\r\n", "\n").replace("SUMMARY:Review\n", "BROKEN\n"));
        assertEquals(seen, result.getProblems());
        assertEquals(Arrays.asList(ProblemCode.BareLf, ProblemCode.TooManyProblems), codes(result.getProblems()));
        assertEquals(16, result.problemCount(ProblemCode.of("ICAL-S003")));
        assertEquals(1, result.problemCount(ProblemCode.MissingColon));
        assertEquals("UTF-8", result.getCharset());
        assertSame(ParseLimits.Default, ParseOptions.Default.getLimits());
        assertSame(ParseOptions.Default, ICal.parser().getOptions());
    }

    @Test
    public void buildersMatchParsedTrees() {
        ICalComponent parsed = ICal.parse(INVITE).getComponents().get(0).getComponents().get(0);
        ICalComponent built = ICalComponent.builder("VEVENT")
            .property(ICalProperty.of("UID", "42@example.com"))
            .property("SUMMARY", "Kickoff")
            .property(ICalProperty.builder("ATTENDEE")
                .parameter("CN", "Jane")
                .parameter(ICalParameter.of("ROLE", "CHAIR"))
                .value("mailto:jane@example.com")
                .build())
            .component(ICalComponent.builder("VALARM").property("ACTION", "DISPLAY").build())
            .build();
        assertEquals(parsed, built);
        assertEquals(parsed.hashCode(), built.hashCode());
        assertNull(built.getSpan());
        try {
            ICalProperty.of("BAD NAME", "value");
            fail("An invalid name was accepted");
        } catch (IllegalArgumentException expected) {
            assertFalse(expected.getMessage().contains("BAD"));
        }
    }

    @Test
    public void cursorWalksEventsWithoutATree() throws ICalParseException {
        ICalReader reader = ICal.parser().reader(INVITE.getBytes(StandardCharsets.UTF_8));
        List<String> summaries = new ArrayList<>();
        int begins = 0;
        int ends = 0;
        ContentEventType type;
        while ((type = reader.next()) != null) {
            switch (type) {
                case Begin:
                    begins++;
                    break;
                case Property:
                    if (reader.getName().equals("SUMMARY")) {
                        summaries.add(reader.getValue().toString());
                        assertEquals(reader.getValue().toString(), reader.valueString());
                    }
                    if (reader.getName().equals("ATTENDEE")) {
                        assertEquals(2, reader.getParameterCount());
                        assertEquals("CN", reader.parameterName(0));
                        assertEquals(Arrays.asList("Jane"), reader.parameterValues(0));
                        assertEquals(7, reader.getLine());
                        assertEquals(7, reader.span().getLine());
                    }
                    break;
                case End:
                    ends++;
                    assertFalse(reader.isImplicitEnd());
                    break;
                default:
                    fail("Unknown event " + type);
            }
        }
        assertEquals(Arrays.asList("Kickoff", "Review"), summaries);
        assertEquals(4, begins);
        assertEquals(4, ends);
        assertNull(reader.next());

        ICalReader events = ICal.parser().reader(INVITE);
        events.next();
        events.next();
        events.next();
        events.next();
        ICalComponent event = events.readComponent();
        assertEquals(ICal.parse(INVITE).getComponents().get(0).getComponents().get(0), event);
        assertEquals("VEVENT", events.getName());
        assertEquals(2, events.getDepth());
        assertEquals(ContentEventType.Begin, events.next());
        try {
            events.getValue();
            fail("A value outside a property was returned");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("Property"));
        }
    }

    @Test
    public void writerReproducesParsedCalendars() {
        byte[] bytes = INVITE.replace("SUMMARY:Review", "summary;x-a=\"1\":Review").getBytes(StandardCharsets.UTF_8);
        ParseResult result = ICal.parse(bytes);
        assertArrayEquals(bytes, ICal.writer().encodeToByteArray(result.getComponents()));
        ICalComponent calendar = result.getComponents().get(0);
        assertEquals(new String(bytes, StandardCharsets.UTF_8), ICal.writer().encodeToString(calendar));

        ICalWriter canonical = ICal.writer(WriteOptions.builder().byteStable(false).timeZonesFirst(true).build());
        String normalized = canonical.encodeToString(result.getComponents());
        assertTrue(normalized.contains("SUMMARY;X-A=1:Review\r\n"));
        StringBuilder out = new StringBuilder();
        canonical.write(calendar, out);
        assertEquals(normalized, out.toString());

        ICalComponent event = ICalComponent.builder("VEVENT").property(ICalProperty.of("UID", "42@example.com")).build();
        assertEquals("BEGIN:VEVENT\r\nUID:42@example.com\r\nEND:VEVENT\r\n", ICal.writer().encodeToString(event));
        assertTrue(WriteOptions.Default.getByteStable());
        assertFalse(WriteOptions.Default.getTimeZonesFirst());
        try {
            ICalProperty.of("DESCRIPTION", "SECRET\u0001");
            fail("A control character was accepted");
        } catch (IllegalArgumentException expected) {
            assertFalse(expected.getMessage().contains("SECRET"));
        }
    }

    private static List<ProblemCode> codes(List<Problem> problems) {
        List<ProblemCode> codes = new ArrayList<>();
        for (Problem problem : problems) {
            codes.add(problem.getCode());
        }
        return codes;
    }
}

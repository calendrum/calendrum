package io.github.calendrum;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import io.github.calendrum.model.Attendee;
import io.github.calendrum.model.BuildDefaults;
import io.github.calendrum.model.Decoded;
import io.github.calendrum.model.ICalRegistry;
import io.github.calendrum.model.IcsSchema;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.Parameters;
import io.github.calendrum.model.Properties;
import io.github.calendrum.model.Rfc5545;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.model.VendorProperties;
import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.Feature;
import io.github.calendrum.syntax.FingerprintDiff;
import io.github.calendrum.syntax.Fingerprints;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ICalParser;
import io.github.calendrum.syntax.ICalProperty;
import io.github.calendrum.syntax.InterceptedLine;
import io.github.calendrum.syntax.LineInterceptor;
import io.github.calendrum.syntax.ParseLimits;
import io.github.calendrum.syntax.ParseMode;
import io.github.calendrum.syntax.ParseOptions;
import io.github.calendrum.syntax.ParseResult;
import io.github.calendrum.syntax.Selection;
import io.github.calendrum.value.Frequency;
import io.github.calendrum.value.ICalDateTime;
import io.github.calendrum.value.ICalDurations;
import io.github.calendrum.value.Recur;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import kotlin.time.Clock;
import io.github.calendrum.model.JavaClocks;
import io.github.calendrum.value.ICalDateTimes;
import kotlin.time.jdk8.InstantConversionsJDK8Kt;
import kotlinx.datetime.DayOfWeek;
import org.junit.Test;

public class JavaReadmeExamplesTest {
    private static final String TEXT = "BEGIN:VCALENDAR\r\n"
        + "X-WR-TIMEZONE:Europe/Moscow\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:42@example.com\r\n"
        + "X-VENDOR-ROOM:Room 101\r\n"
        + "SUMMARY:Weekly sync\r\n"
        + "END:VEVENT\r\n"
        + "END:VCALENDAR\r\n";

    private static final class FloatingTimeFix implements LineInterceptor {
        @Override
        public void intercept(InterceptedLine line) {
            if (line.getName().equals("DTSTART")) line.setParameter("TZID", "Europe/Moscow");
        }
    }

    @Test
    public void theJavaSnippetOfTheReadmeCompilesAndWorks() throws IOException, ICalParseException {
        byte[] bytes = TEXT.getBytes(StandardCharsets.UTF_8);
        byte[] feed = bytes;
        String stored = ICal.fingerprints(bytes).encodeToString();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream collected = new ByteArrayOutputStream();
        OutputStream outputStream = collected;

        ParseResult result = ICal.parse(bytes);
        ICalParser strict = ICal.parser(ParseOptions.builder().mode(ParseMode.Strict).build());
        try {
            strict.parse("BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n".getBytes(StandardCharsets.UTF_8));
            fail();
        } catch (ICalParseException e) {
            assertTrue(!e.getProblems().isEmpty());
        }
        ICalComponent event = ICalComponent.builder("VEVENT").property(ICalProperty.of("UID", "42@example.com")).build();
        String ics = ICal.writer().encodeToString(event);
        assertEquals("BEGIN:VEVENT\r\nUID:42@example.com\r\nEND:VEVENT\r\n", ics);
        byte[] same = ICal.writer().encodeToByteArray(result.getComponents());
        assertArrayEquals(bytes, same);
        ComponentStream stream = ICalStreams.components(ICal.parser(), inputStream);
        assertEquals("VEVENT", stream.next().getName());
        ICalStreams.write(ICal.writer(), result.getComponents(), outputStream);
        assertArrayEquals(bytes, collected.toByteArray());
        ParseLimits limits = ICalStreams.maxTime(ParseLimits.builder(), Duration.ofSeconds(2)).build();
        assertNotNull(limits);
        ParseOptions fixer = ParseOptions.builder()
            .intercept(line -> { if (line.getName().equals("X-VENDOR-ROOM")) line.rename("LOCATION"); })
            .intercept(FloatingTimeFix::new)
            .build();
        ICalComponent fixed = ICal.parser(fixer).parse(bytes).getComponents().get(0).getComponents().get(0);
        assertNull(fixed.property("X-VENDOR-ROOM"));
        assertEquals("Room 101", fixed.property("LOCATION").getValue());
        FingerprintDiff delta = ICal.fingerprints(feed).diff(Fingerprints.decodeFromString(stored));
        assertEquals(1, delta.getUnchangedCount());
        assertTrue(delta.getChanged().isEmpty());
        byte[] edited = TEXT.replace("Weekly", "Daily").getBytes(StandardCharsets.UTF_8);
        FingerprintDiff changedDelta = ICal.fingerprints(edited).diff(Fingerprints.decodeFromString(stored));
        ICalComponent changed = ICal.parser().parseComponent(edited, changedDelta.getChanged().get(0));
        assertEquals("Daily sync", changed.property("SUMMARY").getValue());
    }

    @Test
    public void theJavaModelSnippetOfTheReadmeCompilesAndWorks() {
        ParseResult result = ICal.parse(TEXT.replace("UID:42@example.com\r\n", "UID:42@example.com\r\nDTSTART:20261001T070000Z\r\n"));
        VCalendar calendar = ICalModel.getCalendars(result).get(0);
        ICalDateTime start = calendar.getEvents().get(0).getStart();
        Instant seen = null;
        if (start instanceof ICalDateTime.Utc) {
            java.time.Instant instant = ICalDateTimes.toJavaInstant((ICalDateTime.Utc) start);
            seen = instant;
        }
        Decoded<ICalDateTime> reason = ICalModel.decode(calendar.getEvents().get(0).getComponent(), Properties.getDTSTART());
        assertEquals(Instant.parse("2026-10-01T07:00:00Z"), seen);
        assertTrue(reason instanceof Decoded.Value);
    }

    private static String newUid() {
        return "java-1@example.com";
    }

    @Test
    public void theJavaBuildingSnippetOfTheReadmeCompilesAndWorks() {
        Clock clock = () -> InstantConversionsJDK8Kt.toKotlinInstant(Instant.parse("2026-09-01T08:00:00Z"));
        VEvent event = VEvent.builder(new BuildDefaults(clock, () -> newUid()))
            .summary("Weekly sync")
            .rrule(Recur.builder(Frequency.WEEKLY).byDay(DayOfWeek.MONDAY).count(10).build())
            .attendee(Attendee.builder("mailto:alice@example.com").cn("Alice").build())
            .build();
        VEvent moved = event.toBuilder().sequence(1).build();
        assertEquals("java-1@example.com", moved.getUid());
        assertEquals(Integer.valueOf(1), moved.getSequence());
        assertEquals("FREQ=WEEKLY;COUNT=10;BYDAY=MO", String.valueOf(moved.getRrule()));
    }

    @Test
    public void theJavaSelectionSnippetOfTheReadmeCompilesAndWorks() throws ICalParseException {
        byte[] bytes = TEXT.getBytes(StandardCharsets.UTF_8);
        Selection lessons = Selection.builder().components("VEVENT").need(Feature.Occurrences).build();
        ParseResult partial = ICal.parser(ParseOptions.builder().select(lessons).build()).parse(bytes);
        ParseResult upload = ICal.parser(ParseOptions.getUntrusted()).parse(bytes);
        assertTrue(partial.isPartial());
        assertEquals(upload.getComponents().size(), partial.getComponents().size());
    }

    public static final class Meeting {
        private final String uid;

        public Meeting(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }
    }

    public static final class MeetingSchema extends IcsSchema<Meeting> {
        private final Field<String> uid = field(Properties.getUID(), Meeting::getUid);

        public MeetingSchema() {
            super("VEVENT");
        }

        @Override
        public Meeting create(Values values) {
            return new Meeting(values.get(uid));
        }
    }

    @Test
    public void theJavaExtensionSnippetOfTheReadmeCompilesAndWorks() throws ICalParseException {
        byte[] bytes = ("BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:42@example.com\r\nDURATION:PT1H\r\n"
            + "ATTENDEE;CN=Ann:mailto:ann@example.com\r\nX-MICROSOFT-CDO-BUSYSTATUS:BUSY\r\nEND:VEVENT\r\n"
            + "END:VCALENDAR\r\n").getBytes(StandardCharsets.UTF_8);
        ComponentStream stream = ICal.parser().components(bytes);
        ICalRegistry registry = ICalRegistry.builder().install(Rfc5545.INSTANCE).install(VendorProperties.INSTANCE).build();
        ParseResult vendor = ICal.parser(ParseOptions.builder().registry(registry).decodeEagerly(true).build()).parse(bytes);
        VEvent streamed = VEvent.of(stream.next());
        Duration length = ICalDurations.toJavaDuration(streamed.getDuration());
        String cn = ICalModel.get(streamed.getComponent().property("ATTENDEE"), Parameters.getCN());
        List<Meeting> meetings = ICal.parser().decode(bytes, new MeetingSchema());
        assertTrue(vendor.getProblems().isEmpty());
        assertEquals(Duration.ofHours(1), length);
        assertEquals("Ann", cn);
        assertEquals("42@example.com", meetings.get(0).getUid());
        java.util.stream.Stream<Meeting> streamedMeetings =
            ICalStreams.decodeStream(ICal.parser(), new java.io.ByteArrayInputStream(bytes), new MeetingSchema());
        assertEquals(1, streamedMeetings.count());
    }

    @Test
    public void theJavaRecurrenceSnippetOfTheReadmeCompilesAndWorks() {
        VEvent event = VEvent.builder(JavaClocks.buildDefaults(java.time.Clock.systemUTC(), () -> newUid()))
            .start(new ICalDateTime.Floating(new kotlinx.datetime.LocalDateTime(2026, 10, 5, 10, 0, 0, 0)))
            .rrule(Recur.builder(Frequency.WEEKLY).byDay(DayOfWeek.MONDAY).count(10).build())
            .build();
        java.util.Iterator<ICalDateTime> dates =
            io.github.calendrum.recur.Recurrence.getRecurrenceSet(event).iterator(io.github.calendrum.recur.RecurrenceOptions.Default);
        kotlin.sequences.Sequence<ICalDateTime> next =
            io.github.calendrum.recur.Recurrence.expand(event.getRrule(), event.getStart());
        int count = 0;
        while (dates.hasNext()) {
            dates.next();
            count++;
        }
        assertEquals(10, count);
        assertEquals("2026-10-05T10:00", next.iterator().next().toString());
    }

    @Test
    public void theJavaValidationSnippetOfTheReadmeCompilesAndWorks() {
        VCalendar calendar = ICalModel.getCalendars(ICal.parse("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\n"
            + "METHOD:REQUEST\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20261005T100000Z\r\n"
            + "SUMMARY:x\r\nORGANIZER:mailto:o@example.com\r\nATTENDEE:mailto:a@example.com\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n")).get(0);
        boolean valid = io.github.calendrum.validate.Validator.validate(calendar,
            io.github.calendrum.validate.RuleSet.itip(io.github.calendrum.model.Method.Request)).isValid();
        assertTrue(valid);
        java.time.Clock clock = java.time.Clock.systemUTC();
        VCalendar request = io.github.calendrum.itip.ITip.request(calendar.getEvents().get(0),
            calendar.getEvents().get(0).getOrganizer(), calendar.getEvents().get(0).getAttendees(), JavaClocks.toKotlinClock(clock));
        VCalendar reply = io.github.calendrum.itip.ITip.reply(request, "mailto:a@example.com",
            io.github.calendrum.model.PartStat.Accepted, JavaClocks.toKotlinClock(clock));
        assertEquals(1, reply.getEvents().size());
    }
}

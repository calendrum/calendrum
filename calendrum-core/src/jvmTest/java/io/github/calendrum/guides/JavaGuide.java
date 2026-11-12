package io.github.calendrum.guides;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import io.github.calendrum.ICal;
import io.github.calendrum.ICalParseException;
import io.github.calendrum.ICalStreams;
import io.github.calendrum.model.Attendee;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.IcsSchema;
import io.github.calendrum.model.JavaClocks;
import io.github.calendrum.model.Parameters;
import io.github.calendrum.model.PartStat;
import io.github.calendrum.model.Properties;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.occurrence.Occurrence;
import io.github.calendrum.occurrence.OccurrenceOptions;
import io.github.calendrum.occurrence.Occurrences;
import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ParseLimits;
import io.github.calendrum.syntax.ParseMode;
import io.github.calendrum.syntax.ParseOptions;
import io.github.calendrum.syntax.ParseResult;
import io.github.calendrum.tz.TimeZoneResolver;
import io.github.calendrum.tz.TimeZoneResolvers;
import io.github.calendrum.value.Frequency;
import io.github.calendrum.value.ICalDateTime;
import io.github.calendrum.value.ICalDateTimes;
import io.github.calendrum.value.ICalDurations;
import io.github.calendrum.value.Recur;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kotlinx.datetime.DayOfWeek;
import org.junit.Test;

public class JavaGuide {
    private static final String TEXT = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Guides//EN\r\n"
        + "BEGIN:VTIMEZONE\r\nTZID:Europe/Berlin\r\n"
        + "BEGIN:STANDARD\r\nDTSTART:19701025T030000\r\nRRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU\r\n"
        + "TZOFFSETFROM:+0200\r\nTZOFFSETTO:+0100\r\nEND:STANDARD\r\n"
        + "BEGIN:DAYLIGHT\r\nDTSTART:19700329T020000\r\nRRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU\r\n"
        + "TZOFFSETFROM:+0100\r\nTZOFFSETTO:+0200\r\nEND:DAYLIGHT\r\nEND:VTIMEZONE\r\n"
        + "BEGIN:VEVENT\r\nUID:42@example.com\r\nDTSTAMP:20260901T000000Z\r\n"
        + "DTSTART;TZID=Europe/Berlin:20261001T100000\r\nDURATION:PT1H\r\nSUMMARY:Weekly sync\r\n"
        + "RRULE:FREQ=WEEKLY;BYDAY=TH;COUNT=4\r\nATTENDEE;CN=Jane:mailto:jane@example.com\r\n"
        + "END:VEVENT\r\nEND:VCALENDAR\r\n";

    private static final byte[] BYTES = TEXT.getBytes(StandardCharsets.UTF_8);

    private static String newUid() {
        return "java-1@example.com";
    }

    @Test
    public void parseAndStrictMode() throws ICalParseException {
        byte[] bytes = BYTES;
        byte[] broken = "BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n".getBytes(StandardCharsets.UTF_8);
        List<String> codes = new ArrayList<>();
        // region java-parse
        ParseResult result = ICal.parse(bytes);                              // lenient: problems, never an exception
        ICalComponent calendar = result.getComponents().get(0);
        String summary = calendar.components("VEVENT").get(0).property("SUMMARY").getValue();   // "Weekly sync"

        ParseOptions options = ParseOptions.builder().mode(ParseMode.Strict).build();
        try {
            ICal.parser(options).parse(broken);
        } catch (ICalParseException e) {                                     // checked, so Java makes you handle it
            e.getProblems().forEach(problem -> codes.add(problem.getCode().getId()));
        }
        // endregion
        assertEquals("Weekly sync", summary);
        assertEquals(Arrays.asList("ICAL-S007"), codes);
    }

    @Test
    public void streamsAndInterceptors() throws IOException, ICalParseException {
        InputStream inputStream = new ByteArrayInputStream(BYTES);
        ByteArrayOutputStream collected = new ByteArrayOutputStream();
        OutputStream outputStream = collected;
        List<String> names = new ArrayList<>();
        // region java-streams
        ComponentStream stream = ICalStreams.components(ICal.parser(), inputStream);   // throws IOException
        while (stream.hasNext()) names.add(stream.next().getName());
        ParseLimits limits = ICalStreams.maxTime(ParseLimits.builder(), Duration.ofSeconds(2)).build();
        ICalStreams.write(ICal.writer(), ICal.parse(BYTES).getComponents(), outputStream);   // never closed by us
        ParseOptions fixer = ParseOptions.builder()
            .limits(limits)
            .intercept(line -> { if (line.getName().equals("SUMMARY")) line.rename("DESCRIPTION"); })
            .build();
        // endregion
        assertEquals(Arrays.asList("VTIMEZONE", "VEVENT"), names);
        assertEquals(TEXT, new String(collected.toByteArray(), StandardCharsets.UTF_8));
        ICalComponent event = ICal.parser(fixer).parse(BYTES).getComponents().get(0).components("VEVENT").get(0);
        assertEquals("Weekly sync", event.property("DESCRIPTION").getValue());
        assertNull(event.property("SUMMARY"));
    }

    @Test
    public void theTypedModel() {
        ParseResult result = ICal.parse(BYTES);
        // region java-model
        VCalendar calendar = ICalModel.getCalendars(result).get(0);
        VEvent event = calendar.getEvents().get(0);
        ICalDateTime start = event.getStart();
        Instant instant = null;
        if (start instanceof ICalDateTime.Utc) {
            instant = ICalDateTimes.toJavaInstant((ICalDateTime.Utc) start);   // kotlin.time.Instant ↔ java.time.Instant
        }
        Duration length = ICalDurations.toJavaDuration(event.getDuration());   // null if it has days or weeks
        String cn = ICalModel.get(event.getComponent().property("ATTENDEE"), Parameters.getCN());   // "Jane"
        // endregion
        assertNull(instant);
        assertEquals(Duration.ofHours(1), length);
        assertEquals("Jane", cn);
    }

    @Test
    public void buildersAndEdits() {
        // region java-build
        VEvent event = VEvent.builder(JavaClocks.buildDefaults(Clock.systemUTC(), () -> newUid()))
            .summary("Weekly sync")
            .rrule(Recur.builder(Frequency.WEEKLY).byDay(DayOfWeek.MONDAY).count(10).build())
            .attendee(Attendee.builder("mailto:alice@example.com").cn("Alice").partStat(PartStat.Accepted).build())
            .build();
        VEvent moved = event.toBuilder().sequence(1).build();                 // edits return a copy
        // endregion
        assertEquals("java-1@example.com", moved.getUid());
        assertEquals(Integer.valueOf(1), moved.getSequence());
        assertNull(event.getSequence());
    }

    // region java-schema
    public static final class Meeting {
        private final String uid;
        private final String title;

        public Meeting(String uid, String title) {
            this.uid = uid;
            this.title = title;
        }

        public String getUid() {
            return uid;
        }

        public String getTitle() {
            return title;
        }
    }

    public static final class MeetingSchema extends IcsSchema<Meeting> {
        private final Field<String> uid = field(Properties.getUID(), Meeting::getUid);
        private final Field<String> title = field(Properties.getSUMMARY(), Meeting::getTitle);

        public MeetingSchema() {
            super("VEVENT");
        }

        @Override
        public Meeting create(Values values) {
            return new Meeting(values.get(uid), values.get(title));
        }
    }
    // endregion

    @Test
    public void schemas() throws IOException, ICalParseException {
        // region java-schema-use
        List<Meeting> meetings = ICal.parser().decode(BYTES, new MeetingSchema());       // no tree is built
        Stream<Meeting> streamed = ICalStreams.decodeStream(ICal.parser(), new ByteArrayInputStream(BYTES), new MeetingSchema());
        // endregion
        assertEquals("Weekly sync", meetings.get(0).getTitle());
        assertEquals(Arrays.asList("42@example.com"), streamed.map(Meeting::getUid).collect(Collectors.toList()));
    }

    @Test
    public void timesAndOccurrences() {
        VCalendar calendar = ICalModel.getCalendars(ICal.parse(BYTES)).get(0);
        // region java-occurrences
        TimeZoneResolver zones = TimeZoneResolvers.defaultFor(calendar);        // "default" is a Java keyword
        OccurrenceOptions options = OccurrenceOptions.builder().zones(zones).build();
        Iterator<Occurrence> week = Occurrences.between(calendar,
            ICalDateTimes.toKotlinInstant(Instant.parse("2026-10-01T00:00:00Z")),
            ICalDateTimes.toKotlinInstant(Instant.parse("2026-10-20T00:00:00Z")), options).iterator();
        List<Instant> starts = new ArrayList<>();
        while (week.hasNext()) starts.add(ICalDateTimes.toJavaInstant(week.next().getStart()));
        // endregion
        assertEquals(Arrays.asList(Instant.parse("2026-10-01T08:00:00Z"), Instant.parse("2026-10-08T08:00:00Z"),
            Instant.parse("2026-10-15T08:00:00Z")), starts);
        assertTrue(starts.size() == 3);
        if (starts.isEmpty()) fail();
    }
}

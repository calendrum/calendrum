package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.BuildDefaults;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.JavaClocks;
import io.github.calendrum.model.ICalPatch;
import io.github.calendrum.model.IcsSchema;
import io.github.calendrum.model.PatchResult;
import io.github.calendrum.model.Properties;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ParseResult;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import kotlin.Unit;
import org.junit.Test;

public class JavaScenariosTest {
    private static final String TEXT = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\n"
        + "BEGIN:VEVENT\r\nUID:1@example.com\r\nDTSTAMP:20260901T080000Z\r\nDTSTART:20260901T090000Z\r\n"
        + "SUMMARY:Planning\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n";

    public static final class Meeting {
        private final String uid;
        private final String summary;

        public Meeting(String uid, String summary) {
            this.uid = uid;
            this.summary = summary;
        }

        public String getUid() {
            return uid;
        }

        public String getSummary() {
            return summary;
        }
    }

    public static final class MeetingSchema extends IcsSchema<Meeting> {
        private final Field<String> uid = field(Properties.getUID(), Meeting::getUid);
        private final Field<String> summary = field(Properties.getSUMMARY(), Meeting::getSummary);

        public MeetingSchema() {
            super("VEVENT");
        }

        @Override
        public Meeting create(Values values) {
            return new Meeting(values.get(uid), values.get(summary));
        }
    }

    @Test
    public void aSchemaEncodesIntoACalendarBuiltInJava() throws ICalParseException {
        VCalendar header = VCalendar.builder().prodId("-//Java//Scenario//EN").build();
        List<Meeting> meetings = Arrays.asList(new Meeting("a@example.com", "One"), new Meeting("b@example.com", "Two"));
        BuildDefaults defaults = JavaClocks.buildDefaults(
            Clock.fixed(Instant.parse("2026-09-01T08:00:00Z"), ZoneOffset.UTC),
            () -> "generated@example.com");
        byte[] bytes = ICal.writer().encode(meetings, new MeetingSchema(), header, defaults);
        VCalendar calendar = ICalModel.getCalendars(ICal.parse(bytes)).get(0);
        assertEquals("-//Java//Scenario//EN", calendar.getProdId());
        assertEquals(2, calendar.getEvents().size());
        List<Meeting> read = ICal.parser().decode(bytes, new MeetingSchema());
        assertEquals("Two", read.get(1).getSummary());
    }

    @Test
    public void aPatchFromJava() {
        byte[] original = TEXT.getBytes(StandardCharsets.UTF_8);
        PatchResult result = new ICalPatch(original, builder -> {
            builder.component("1@example.com", null, patch -> {
                patch.set(Properties.getSUMMARY(), "Retro");
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        }).apply();
        String patched = new String(result.getBytes(), StandardCharsets.UTF_8);
        assertEquals(TEXT.replace("Planning", "Retro"), patched);
        assertTrue(result.getUnmatched().isEmpty());
    }

    @Test
    public void aComponentStreamThrowsFromJavaToo() throws ICalParseException {
        ComponentStream stream = ICal.parser().components(TEXT.getBytes(StandardCharsets.UTF_8));
        assertTrue(stream.hasNextOrThrow());
        ICalComponent event = stream.nextOrThrow();
        assertEquals("VEVENT", event.getName());
        ParseResult result = ICal.parse(TEXT);
        assertEquals(1, result.getComponents().size());
    }
}

package io.github.calendrum;

import static org.junit.Assert.assertEquals;

import io.github.calendrum.model.BuildDefaults;
import io.github.calendrum.model.JavaClocks;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VCalendars;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.tz.TimeZoneConversions;
import io.github.calendrum.tz.TimeZoneResolvers;
import io.github.calendrum.value.ICalDateTime;
import io.github.calendrum.value.ICalDateTimes;
import io.github.calendrum.value.ICalDuration;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import kotlin.Unit;
import org.junit.Test;

public class JavaBridgesTest {
    private static final String CALENDAR = "BEGIN:VCALENDAR\r\n"
        + "BEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260901T080000Z\r\nSUMMARY:Standup\r\nEND:VEVENT\r\n"
        + "BEGIN:VEVENT\r\nUID:2\r\nDTSTAMP:20260902T080000Z\r\nEND:VEVENT\r\n"
        + "BEGIN:VEVENT\r\nSUMMARY:No uid\r\nEND:VEVENT\r\n"
        + "END:VCALENDAR\r\n";

    @Test
    public void instantsCrossBetweenJavaAndKotlin() {
        Instant java = Instant.parse("2026-09-01T08:00:00.123Z");
        ICalDateTime.Utc utc = ICalDateTimes.toICalDateTime(java);
        assertEquals("2026-09-01T08:00:00.123Z", utc.toString());
        assertEquals(java, ICalDateTimes.toJavaInstant(utc));
        kotlin.time.Instant kotlinInstant = ICalDateTimes.toKotlinInstant(java);
        assertEquals(utc.getInstant(), kotlinInstant);
        assertEquals(java, ICalDateTimes.toJavaInstant(kotlinInstant));
        assertEquals(utc, new ICalDateTime.Utc(kotlinInstant));
    }

    @Test
    public void buildDefaultsTakeAJavaClock() {
        Clock clock = Clock.fixed(Instant.parse("2026-09-01T08:00:00Z"), ZoneOffset.UTC);
        BuildDefaults defaults = JavaClocks.buildDefaults(clock, () -> "generated@example.com");
        VEvent event = VEvent.builder(defaults).summary("x").build();
        assertEquals("generated@example.com", event.getUid());
        assertEquals(ICalDateTimes.toICalDateTime(Instant.parse("2026-09-01T08:00:00Z")), event.getStamp());
        assertEquals(Instant.parse("2026-09-01T08:00:00Z"),
            ICalDateTimes.toJavaInstant(JavaClocks.toKotlinClock(clock).now()));
    }

    @Test
    public void schemaDecodingStreamsToJava() {
        JavaSchemaTest.MeetingSchema schema = new JavaSchemaTest.MeetingSchema();
        byte[] bytes = CALENDAR.getBytes(StandardCharsets.UTF_8);
        List<String> uids = ICalStreams.decodeStream(ICal.parser(), new ByteArrayInputStream(bytes), schema)
            .map(JavaSchemaTest.Meeting::getUid)
            .collect(Collectors.toList());
        assertEquals(java.util.Arrays.asList("1", "2"), uids);
        Iterator<JavaSchemaTest.Meeting> iterator =
            ICalStreams.decodeStream(ICal.parser(), ByteInputs.asByteInput(bytes), schema).iterator();
        assertEquals("Standup", iterator.next().getSummary());
        assertEquals("2", iterator.next().getUid());
        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void fileFacadesHaveJavaNames() {
        ICalDateTime.Utc utc = ICalDateTimes.toICalDateTime(Instant.parse("2026-09-01T08:00:00Z"));
        assertEquals(utc.getInstant(), TimeZoneConversions.toInstant(utc, TimeZoneResolvers.defaultFor(null)));
        VCalendar calendar = VCalendars.VCalendar("-//Example//Java//EN", builder -> Unit.INSTANCE);
        assertEquals("-//Example//Java//EN", calendar.getProdId());
        assertEquals("1h 30m", ICalDuration.ofMinutes(90).toDurationOrNull().toString());   // a kotlin.time.Duration
        assertEquals(null, ICalDuration.ofDays(1).toDurationOrNull());
    }
}

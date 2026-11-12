package io.github.calendrum.ical4j;

import static org.junit.Assert.assertEquals;

import io.github.calendrum.model.VCalendar;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import org.junit.Test;

public class JavaIcal4jInteropTest {
    private static final String TEXT = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nBEGIN:VEVENT\r\nUID:1\r\n"
        + "DTSTAMP:20260901T080000Z\r\nSUMMARY:Planning\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n";

    @Test
    public void interopFromJava() throws Exception {
        Calendar ical4j = new CalendarBuilder().build(new ByteArrayInputStream(TEXT.getBytes(StandardCharsets.UTF_8)));
        VCalendar calendar = Ical4jInterop.toCalendrum(ical4j);
        assertEquals("Planning", calendar.getEvents().get(0).getSummary());
        assertEquals(ical4j, Ical4jInterop.toIcal4j(calendar));
    }
}

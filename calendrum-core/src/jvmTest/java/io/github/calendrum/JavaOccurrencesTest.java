package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.occurrence.Events;
import io.github.calendrum.occurrence.Occurrence;
import io.github.calendrum.occurrence.OccurrenceCursor;
import io.github.calendrum.occurrence.OccurrenceOptions;
import io.github.calendrum.occurrence.Occurrences;
import io.github.calendrum.tz.ICalTimeZone;
import io.github.calendrum.tz.ResolveContext;
import io.github.calendrum.tz.TimeZoneResolver;
import io.github.calendrum.tz.TimeZoneResolvers;
import io.github.calendrum.value.ICalDateTimes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

public class JavaOccurrencesTest {
    private static final String CALENDAR = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\n"
        + "BEGIN:VTIMEZONE\r\nTZID:W. Europe Standard Time\r\n"
        + "BEGIN:STANDARD\r\nDTSTART:16010101T030000\r\nTZOFFSETFROM:+0200\r\nTZOFFSETTO:+0100\r\n"
        + "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10\r\nEND:STANDARD\r\n"
        + "BEGIN:DAYLIGHT\r\nDTSTART:16010101T020000\r\nTZOFFSETFROM:+0100\r\nTZOFFSETTO:+0200\r\n"
        + "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3\r\nEND:DAYLIGHT\r\nEND:VTIMEZONE\r\n"
        + "BEGIN:VEVENT\r\nUID:1\r\nDTSTART;TZID=W. Europe Standard Time:20261026T090000\r\nDURATION:PT1H\r\n"
        + "RRULE:FREQ=DAILY;COUNT=3\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n";

    @Test
    public void occurrencesFromJava() {
        VCalendar calendar = ICalModel.getCalendars(ICal.parse(CALENDAR)).get(0);
        TimeZoneResolver zones = TimeZoneResolvers.defaultFor(calendar);
        ICalTimeZone zone = zones.resolve("W. Europe Standard Time", ResolveContext.Empty);
        assertEquals(3600, zone.offsetAt(ICalDateTimes.toKotlinInstant(Instant.parse("2026-12-01T00:00:00Z"))).getTotalSeconds());
        OccurrenceOptions options = OccurrenceOptions.builder().zones(zones).build();
        List<Instant> starts = new ArrayList<>();
        Iterator<Occurrence> iterator = Occurrences.between(calendar,
            ICalDateTimes.toKotlinInstant(Instant.parse("2026-10-01T00:00:00Z")),
            ICalDateTimes.toKotlinInstant(Instant.parse("2026-11-01T00:00:00Z")), options).iterator();
        while (iterator.hasNext()) starts.add(ICalDateTimes.toJavaInstant(iterator.next().getStart()));
        assertEquals(Instant.parse("2026-10-26T08:00:00Z"), starts.get(0));
        assertEquals(3, starts.size());
        OccurrenceCursor cursor = Occurrences.cursor(calendar, OccurrenceOptions.Default);
        assertEquals(starts.get(0), ICalDateTimes.toJavaInstant(cursor.next().getStart()));
        VEvent event = calendar.getEvents().get(0);
        assertTrue(!Events.isAllDay(event));
        assertEquals(Instant.parse("2026-10-26T09:00:00Z"), ICalDateTimes.toJavaInstant(Events.resolvedEnd(event, zones)));
    }
}

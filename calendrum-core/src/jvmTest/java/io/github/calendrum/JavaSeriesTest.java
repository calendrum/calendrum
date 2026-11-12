package io.github.calendrum;

import static org.junit.Assert.assertEquals;

import io.github.calendrum.model.Canonical;
import io.github.calendrum.model.FeedPublisher;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.JavaClocks;
import io.github.calendrum.model.Series;
import io.github.calendrum.model.SeriesView;
import io.github.calendrum.model.UidGenerator;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.occurrence.BusyInterval;
import io.github.calendrum.occurrence.FreeBusyCalc;
import io.github.calendrum.occurrence.OccurrenceOptions;
import io.github.calendrum.value.ICalDateTimes;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import kotlin.Unit;
import org.junit.Test;

public class JavaSeriesTest {
    @Test
    public void seriesFromJava() {
        VCalendar calendar = ICalModel.getCalendars(ICal.parse("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\n"
            + "BEGIN:VEVENT\r\nUID:w\r\nDTSTAMP:20260901T000000Z\r\nDTSTART:20261005T100000Z\r\nDTEND:20261005T110000Z\r\n"
            + "RRULE:FREQ=WEEKLY;COUNT=3\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n")).get(0);
        SeriesView series = Series.find(calendar, "w");
        VCalendar edited = Series.editThis(series, ICalDateTimes.toICalDateTime(Instant.parse("2026-10-12T10:00:00Z")),
            builder -> {
                builder.summary("Moved");
                return Unit.INSTANCE;
            });
        assertEquals(2, edited.getEvents().size());
        assertEquals("2ed6657d-e927-568b-95e1-2665a8aea6a2",
            Canonical.uidV5("6ba7b810-9dad-11d1-80b4-00c04fd430c8", "www.example.com"));
        assertEquals(Canonical.uidV5("example.com", "k"), UidGenerator.v5("example.com", () -> "k").next());
        FeedPublisher publisher = new FeedPublisher(
            JavaClocks.toKotlinClock(java.time.Clock.fixed(Instant.parse("2026-10-01T08:00:00Z"), ZoneOffset.UTC)));
        VCalendar published = publisher.publish(null, edited);
        assertEquals("2026-10-01T08:00:00Z", published.getEvents().get(0).getLastModified().toString());
        List<BusyInterval> busy = FreeBusyCalc.busy(Collections.singletonList(calendar),
            ICalDateTimes.toKotlinInstant(Instant.parse("2026-10-01T00:00:00Z")),
            ICalDateTimes.toKotlinInstant(Instant.parse("2026-11-01T00:00:00Z")), OccurrenceOptions.Default);
        assertEquals(3, busy.size());
        assertEquals(new String(Canonical.canonicalize(calendar), java.nio.charset.StandardCharsets.UTF_8),
            new String(Canonical.canonicalize(Series.merge(calendar)), java.nio.charset.StandardCharsets.UTF_8));
    }
}

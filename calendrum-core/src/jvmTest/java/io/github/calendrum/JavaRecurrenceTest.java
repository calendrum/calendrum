package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.recur.Recurrence;
import io.github.calendrum.recur.RecurrenceLimits;
import io.github.calendrum.recur.RecurrenceOptions;
import io.github.calendrum.recur.RecurrenceSet;
import io.github.calendrum.recur.StartMismatch;
import io.github.calendrum.value.Frequency;
import io.github.calendrum.value.ICalDateTime;
import io.github.calendrum.value.Recur;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlinx.datetime.LocalDateTime;
import kotlinx.datetime.UtcOffset;
import org.junit.Test;

public class JavaRecurrenceTest {
    private static final String CALENDAR = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:x\r\n"
        + "DTSTART;TZID=Europe/Berlin:20260901T090000\r\n"
        + "RRULE:FREQ=DAILY;UNTIL=20260903T070000Z\r\n"
        + "EXDATE;TZID=Europe/Berlin:20260902T090000\r\n"
        + "END:VEVENT\r\nEND:VCALENDAR\r\n";

    @Test
    public void aRuleExpandsFromJava() {
        Recur rule = Recur.builder(Frequency.WEEKLY).count(3).build();
        ICalDateTime start = new ICalDateTime.Floating(new LocalDateTime(2026, 9, 1, 9, 0, 0, 0));
        List<String> values = new ArrayList<>();
        for (Iterator<ICalDateTime> it = Recurrence.expand(rule, start).iterator(); it.hasNext(); ) {
            values.add(it.next().toString());
        }
        assertEquals(3, values.size());
        assertEquals("2026-09-15T09:00", values.get(2));
        List<Problem> problems = new ArrayList<>();
        RecurrenceOptions options = RecurrenceOptions.builder()
            .startMismatch(StartMismatch.Exclude)
            .limits(RecurrenceLimits.builder().maxOccurrences(2).build())
            .problemListener(problems::add)
            .build();
        Iterator<ICalDateTime> limited = Recurrence.expand(rule, start, null, null, options).iterator();
        limited.next();
        limited.next();
        assertFalse(limited.hasNext());
        assertEquals(ProblemCode.RecurrenceLimitReached, problems.get(0).getCode());
    }

    @Test
    public void aRecurrenceSetIteratesFromJava() throws ICalParseException {
        VEvent event = ICalModel.getCalendars(ICal.parse(CALENDAR)).get(0).getEvents().get(0);
        RecurrenceSet set = Recurrence.getRecurrenceSet(event);
        assertTrue(set.isFinite());
        RecurrenceOptions berlin = RecurrenceOptions.builder()
            .untilOffset(local -> UtcOffset.Companion.parse("+02:00", UtcOffset.Formats.INSTANCE.getISO()))
            .build();
        List<String> values = new ArrayList<>();
        Iterator<ICalDateTime> iterator = set.iterator(berlin);
        while (iterator.hasNext()) values.add(iterator.next().toString());
        assertEquals(2, values.size());
        assertEquals("2026-09-03T09:00[Europe/Berlin]", values.get(1));
        assertEquals(set, RecurrenceSet.of(event.getComponent()));
    }
}

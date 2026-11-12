package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import io.github.calendrum.model.AlarmAction;
import io.github.calendrum.model.Attendee;
import io.github.calendrum.model.BuildDefaults;
import io.github.calendrum.model.Categories;
import io.github.calendrum.model.CuType;
import io.github.calendrum.model.Organizer;
import io.github.calendrum.model.PartStat;
import io.github.calendrum.model.Properties;
import io.github.calendrum.model.Trigger;
import io.github.calendrum.model.VAlarm;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.model.VTimeZone;
import io.github.calendrum.value.Frequency;
import io.github.calendrum.value.ICalDateTime;
import io.github.calendrum.value.ICalDuration;
import io.github.calendrum.value.Recur;
import io.github.calendrum.value.WeekdayNum;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import kotlin.time.Clock;
import kotlin.time.jdk8.InstantConversionsJDK8Kt;
import kotlinx.datetime.DayOfWeek;
import kotlinx.datetime.LocalDateTime;
import kotlinx.datetime.ConvertersKt;
import org.junit.Test;

public class JavaBuildersTest {
    private static ICalDateTime utc(String text) {
        return new ICalDateTime.Utc(InstantConversionsJDK8Kt.toKotlinInstant(Instant.parse(text)));
    }

    @Test
    public void anEventIsBuiltWithFluentBuilders() {
        Recur rule = Recur.builder(Frequency.MONTHLY)
            .byDay(new WeekdayNum(-1, DayOfWeek.FRIDAY))
            .until(utc("2027-01-01T00:00:00Z"))
            .build();
        VEvent event = VEvent.builder()
            .uid("42@acme.example")
            .stamp(utc("2026-09-01T08:00:00Z"))
            .summary("Planning")
            .start(new ICalDateTime.Zoned(new LocalDateTime(2026, 10, 1, 10, 0, 0, 0), "Europe/Moscow"))
            .duration(ICalDuration.ofHours(1))
            .rrule(rule)
            .organizer(Organizer.builder("mailto:boss@example.com").cn("Boss").build())
            .attendee(Attendee.builder("mailto:alice@example.com").cn("Alice").partStat(PartStat.NeedsAction)
                .rsvp(true).cuType(CuType.Individual).build())
            .alarm(VAlarm.builder().display(new Trigger.Relative(ICalDuration.ofMinutes(-15)), "Soon").build())
            .property(Properties.getCATEGORIES(), new Categories(Arrays.asList("work", "planning")))
            .property("X-ACME-TAG", "blue")
            .build();
        VCalendar calendar = VCalendar.builder().prodId("-//Acme//Planner 1.0//EN").version("2.0").event(event).build();
        String expected = "BEGIN:VCALENDAR\r\n"
            + "PRODID:-//Acme//Planner 1.0//EN\r\n"
            + "VERSION:2.0\r\n"
            + "BEGIN:VEVENT\r\n"
            + "UID:42@acme.example\r\n"
            + "DTSTAMP:20260901T080000Z\r\n"
            + "SUMMARY:Planning\r\n"
            + "DTSTART;TZID=Europe/Moscow:20261001T100000\r\n"
            + "DURATION:PT1H\r\n"
            + "RRULE:FREQ=MONTHLY;UNTIL=20270101T000000Z;BYDAY=-1FR\r\n"
            + "ORGANIZER;CN=Boss:mailto:boss@example.com\r\n"
            + "ATTENDEE;CN=Alice;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CUTYPE=INDIVIDUAL:mailto:\r\n"
            + " alice@example.com\r\n"
            + "CATEGORIES:work,planning\r\n"
            + "X-ACME-TAG:blue\r\n"
            + "BEGIN:VALARM\r\n"
            + "ACTION:DISPLAY\r\n"
            + "TRIGGER:-PT15M\r\n"
            + "DESCRIPTION:Soon\r\n"
            + "END:VALARM\r\n"
            + "END:VEVENT\r\n"
            + "END:VCALENDAR\r\n";
        assertEquals(expected, ICal.writer().encodeToString(calendar.getComponent()));
        assertEquals(rule, calendar.getEvents().get(0).getRrule());
    }

    @Test
    public void editsGoThroughToBuilder() {
        VCalendar calendar = VCalendar.builder()
            .prodId("-//x//y//EN")
            .event(VEvent.builder().uid("1").stamp(utc("2026-09-01T08:00:00Z")).summary("Before").build())
            .timeZone(VTimeZone.builder()
                .tzid("Europe/Berlin")
                .observance(VTimeZone.Standard.builder()
                    .start(new ICalDateTime.Floating(new LocalDateTime(1996, 10, 27, 3, 0, 0, 0)))
                    .offsetFrom(ConvertersKt.toKotlinUtcOffset(ZoneOffset.ofHours(2)))
                    .offsetTo(ConvertersKt.toKotlinUtcOffset(ZoneOffset.ofHours(1)))
                    .name("CET")
                    .build())
                .build())
            .build();
        VEvent event = calendar.getEvents().get(0);
        VEvent edited = event.toBuilder().summary("After").sequence(1).build();
        assertEquals("After", edited.getSummary());
        assertSame(event.getComponent().getProperties().get(0), edited.getComponent().getProperties().get(0));
        VCalendar.Builder builder = calendar.toBuilder();
        builder.getEvents().replace(event, edited);
        VCalendar result = builder.build();
        assertNotSame(calendar.getComponent(), result.getComponent());
        assertSame(calendar.getTimeZones().get(0).getComponent(), result.getTimeZones().get(0).getComponent());
        assertEquals(1, (int) result.getEvents().get(0).getSequence());
        assertEquals("CET", result.getTimeZones().get(0).getObservances().get(0).getNames().get(0));
    }

    @Test
    public void requiredPropertiesComeFromDefaults() {
        assertThrows(IllegalStateException.class, () -> VEvent.builder().summary("x").build());
        Clock clock = () -> InstantConversionsJDK8Kt.toKotlinInstant(Instant.parse("2026-09-01T08:00:00Z"));
        BuildDefaults defaults = new BuildDefaults(clock, () -> "generated@example.com");
        VEvent event = VEvent.builder(defaults).summary("x").build();
        assertEquals("generated@example.com", event.getUid());
        assertEquals(utc("2026-09-01T08:00:00Z"), event.getStamp());
        VAlarm alarm = VAlarm.builder().action(AlarmAction.Audio).build();
        assertEquals(AlarmAction.Audio, alarm.getAction());
        assertThrows(IllegalStateException.class, () -> Recur.builder(Frequency.DAILY).count(1).until(
            utc("2026-09-01T08:00:00Z")));
        assertThrows(IllegalArgumentException.class, () -> Recur.builder(Frequency.WEEKLY).byWeekNo(1));
    }
}

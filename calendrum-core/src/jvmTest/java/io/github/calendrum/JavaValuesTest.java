package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.value.Frequency;
import io.github.calendrum.value.ICalDateTime;
import io.github.calendrum.value.ICalDuration;
import io.github.calendrum.value.ICalPeriod;
import io.github.calendrum.value.MonthNum;
import io.github.calendrum.value.RecurPart;
import io.github.calendrum.value.Skip;
import io.github.calendrum.value.WeekdayNum;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kotlin.time.jdk8.InstantConversionsJDK8Kt;
import kotlinx.datetime.ConvertersKt;
import kotlinx.datetime.DateTimePeriod;
import kotlinx.datetime.DayOfWeek;
import org.junit.Test;

public class JavaValuesTest {
    @Test
    public void dateTimeFormsAreFinalClassesOfOneInterface() {
        ICalDateTime date = new ICalDateTime.Date(ConvertersKt.toKotlinLocalDate(LocalDate.of(2026, 9, 1)));
        ICalDateTime floating =
            new ICalDateTime.Floating(ConvertersKt.toKotlinLocalDateTime(LocalDateTime.of(2026, 9, 1, 10, 0)));
        ICalDateTime zoned = new ICalDateTime.Zoned(
            ConvertersKt.toKotlinLocalDateTime(LocalDateTime.of(2026, 9, 1, 10, 0)), "Europe/Berlin");
        ICalDateTime utc =
            new ICalDateTime.Utc(InstantConversionsJDK8Kt.toKotlinInstant(Instant.parse("2026-09-01T08:00:00Z")));
        assertTrue(date instanceof ICalDateTime.Date);
        assertEquals(LocalDate.of(2026, 9, 1), ConvertersKt.toJavaLocalDate(((ICalDateTime.Date) date).getDate()));
        assertEquals(
            LocalDateTime.of(2026, 9, 1, 10, 0),
            ConvertersKt.toJavaLocalDateTime(((ICalDateTime.Floating) floating).getDateTime()));
        assertEquals("Europe/Berlin", ((ICalDateTime.Zoned) zoned).getTzid());
        Instant instant = InstantConversionsJDK8Kt.toJavaInstant(((ICalDateTime.Utc) utc).getInstant());
        assertEquals(Instant.parse("2026-09-01T08:00:00Z"), instant);
        assertEquals("2026-09-01T08:00:00Z", utc.toString());
        assertNotEquals(floating, zoned);
        assertEquals(
            utc, new ICalDateTime.Utc(InstantConversionsJDK8Kt.toKotlinInstant(Instant.ofEpochSecond(1788249600L))));
    }

    @Test
    public void durationsKeepTheirFormAndConvert() {
        ICalDuration week = ICalDuration.ofWeeks(1);
        ICalDuration sevenDays = ICalDuration.ofDays(7);
        assertNotEquals(week, sevenDays);
        assertEquals(week.toDateTimePeriod(), sevenDays.toDateTimePeriod());
        ICalDuration reminder = ICalDuration.ofMinutes(-15);
        assertTrue(reminder.isNegative());
        assertEquals(15, reminder.getMinutes());
        assertEquals("-PT15M", reminder.toString());
        DateTimePeriod period = reminder.toDateTimePeriod();
        assertEquals(-15, period.getMinutes());
        assertEquals(0, ICalDuration.ofHours(2).getMinutes());
        assertEquals("PT45S", ICalDuration.ofSeconds(45).toString());
    }

    @Test
    public void periodsAndRecurrencePieces() {
        ICalDateTime start =
            new ICalDateTime.Utc(InstantConversionsJDK8Kt.toKotlinInstant(Instant.parse("1997-01-01T18:00:00Z")));
        ICalPeriod period = new ICalPeriod(start, null, ICalDuration.ofHours(5));
        assertNull(period.getEnd());
        assertEquals("PT5H", period.getDuration().toString());
        WeekdayNum lastFriday = new WeekdayNum(-1, DayOfWeek.FRIDAY);
        assertEquals("-1FR", lastFriday.toString());
        assertNull(new WeekdayNum(null, DayOfWeek.MONDAY).getOrdinal());
        MonthNum leap = new MonthNum(5, true);
        assertTrue(leap.isLeap());
        assertFalse(new MonthNum(5, false).isLeap());
        assertEquals("X-NAME", new RecurPart("X-NAME", "1").getName());
        assertEquals(Frequency.WEEKLY, Frequency.valueOf("WEEKLY"));
        assertEquals(Skip.OMIT, Skip.values()[0]);
    }
}

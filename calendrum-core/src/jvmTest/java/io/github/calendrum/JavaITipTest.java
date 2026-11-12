package io.github.calendrum;

import static org.junit.Assert.assertEquals;

import io.github.calendrum.itip.IMip;
import io.github.calendrum.itip.ITip;
import io.github.calendrum.itip.ITipOutcome;
import io.github.calendrum.itip.ITipResult;
import io.github.calendrum.itip.SenderCheck;
import io.github.calendrum.model.Attendee;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.JavaClocks;
import io.github.calendrum.model.Method;
import io.github.calendrum.model.Organizer;
import io.github.calendrum.model.PartStat;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VEvent;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
import kotlin.time.Clock;
import org.junit.Test;

public class JavaITipTest {
    @Test
    public void inviteAndReplyFromJava() {
        Clock clock = JavaClocks.toKotlinClock(java.time.Clock.fixed(Instant.parse("2026-10-01T08:00:00Z"), ZoneOffset.UTC));
        VEvent event = ICalModel.getCalendars(ICal.parse("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\n"
            + "BEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20261005T100000Z\r\nSUMMARY:x\r\n"
            + "END:VEVENT\r\nEND:VCALENDAR\r\n")).get(0).getEvents().get(0);
        Organizer organizer = Organizer.builder("mailto:a@example.com").build();
        Attendee bob = Attendee.builder("mailto:b@example.com").rsvp(true).build();
        VCalendar request = ITip.request(event, organizer, Collections.singletonList(bob), clock);
        VCalendar reply = ITip.reply(request, "mailto:b@example.com", PartStat.Accepted, clock);
        VCalendar store = VCalendar.builder().prodId("-//a//EN").version("2.0").event(request.getEvents().get(0)).build();
        ITipResult result = ITip.apply(store, reply);
        assertEquals(ITipOutcome.Applied, result.getOutcome());
        assertEquals(PartStat.Accepted, result.getCalendar().getEvents().get(0).getAttendees().get(0).getPartStat());
        assertEquals("text/calendar; method=REPLY; charset=UTF-8", IMip.contentType(Method.Reply));
        assertEquals(SenderCheck.Matches, IMip.checkSender(reply, "b@example.com"));
        assertEquals(0, IMip.untrustedContent(reply).size());
    }
}

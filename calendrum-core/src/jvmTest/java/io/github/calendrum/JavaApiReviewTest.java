package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.Parameters;
import io.github.calendrum.model.Participant;
import io.github.calendrum.model.PartStat;
import io.github.calendrum.model.VAlarm;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.model.VFreeBusy;
import io.github.calendrum.model.VJournal;
import io.github.calendrum.model.VLocation;
import io.github.calendrum.model.VResource;
import io.github.calendrum.model.VTimeZone;
import io.github.calendrum.model.VTodo;
import io.github.calendrum.syntax.ComponentStream;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ICalProperty;
import io.github.calendrum.value.ICalDuration;
import io.github.calendrum.value.ICalDurations;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class JavaApiReviewTest {
    private static final String CALENDAR = "BEGIN:VCALENDAR\r\n"
        + "BEGIN:VTIMEZONE\r\nTZID:X\r\nBEGIN:STANDARD\r\nDTSTART:19700101T000000\r\nTZOFFSETFROM:+0100\r\n"
        + "TZOFFSETTO:+0100\r\nEND:STANDARD\r\nBEGIN:DAYLIGHT\r\nDTSTART:19700301T000000\r\nTZOFFSETFROM:+0100\r\n"
        + "TZOFFSETTO:+0200\r\nEND:DAYLIGHT\r\nEND:VTIMEZONE\r\n"
        + "BEGIN:VEVENT\r\nUID:e\r\nDURATION:PT1H30M\r\nATTENDEE;PARTSTAT=ACCEPTED;CN=Ann:mailto:ann@example.com\r\n"
        + "BEGIN:VALARM\r\nACTION:DISPLAY\r\nTRIGGER:-PT15M\r\nEND:VALARM\r\n"
        + "BEGIN:PARTICIPANT\r\nUID:p\r\nPARTICIPANT-TYPE:SPEAKER\r\nEND:PARTICIPANT\r\n"
        + "BEGIN:VLOCATION\r\nUID:l\r\nEND:VLOCATION\r\nBEGIN:VRESOURCE\r\nUID:r\r\nEND:VRESOURCE\r\nEND:VEVENT\r\n"
        + "BEGIN:VTODO\r\nUID:t\r\nEND:VTODO\r\nBEGIN:VJOURNAL\r\nUID:j\r\nEND:VJOURNAL\r\n"
        + "BEGIN:VFREEBUSY\r\nUID:f\r\nEND:VFREEBUSY\r\n"
        + "END:VCALENDAR\r\n";

    @Test
    public void wrappersAreMadeFromStreamedComponents() throws Exception {
        ComponentStream stream = ICal.parser().components(CALENDAR.getBytes(StandardCharsets.UTF_8));
        List<String> kinds = new ArrayList<>();
        while (stream.hasNext()) {
            ICalComponent component = stream.next();
            VEvent event = VEvent.of(component);
            if (event != null) {
                kinds.add("event " + event.getUid());
                assertNull(VTodo.of(component));
                assertEquals("l", VLocation.of(event.getLocations().get(0).getComponent()).getUid());
                assertEquals("r", VResource.of(event.getComponent().getComponents().get(3)).getUid());
                assertEquals("p", Participant.of(event.getComponent().getComponents().get(1)).getUid());
                assertEquals("DISPLAY", VAlarm.of(event.getComponent().getComponents().get(0)).getAction().getValue());
                assertNull(VAlarm.of(component));
            }
            VTimeZone zone = VTimeZone.of(component);
            if (zone != null) {
                kinds.add("zone " + zone.getTzid());
                assertTrue(VTimeZone.Standard.of(component.getComponents().get(0)) != null);
                assertNull(VTimeZone.Standard.of(component.getComponents().get(1)));
                assertTrue(VTimeZone.Daylight.of(component.getComponents().get(1)) != null);
            }
            if (VTodo.of(component) != null) kinds.add("todo");
            if (VJournal.of(component) != null) kinds.add("journal");
            if (VFreeBusy.of(component) != null) kinds.add("free-busy");
        }
        assertEquals(5, kinds.size());
        assertEquals("zone X", kinds.get(0));
        assertEquals("event e", kinds.get(1));
        VCalendar calendar = VCalendar.of(ICal.parse(CALENDAR).getComponents().get(0));
        assertEquals(1, calendar.getEvents().size());
        assertNull(VCalendar.of(calendar.getEvents().get(0).getComponent()));
    }

    @Test
    public void durationsBridgeToJavaTime() throws Exception {
        VEvent event = ICalModel.getCalendars(ICal.parse(CALENDAR)).get(0).getEvents().get(0);
        ICalDuration duration = event.getDuration();
        assertEquals(Duration.ofMinutes(90), ICalDurations.toJavaDuration(duration));
        assertNull(ICalDurations.toJavaDuration(ICalDuration.ofDays(1)));
        ICalDuration back = ICalDurations.toICalDuration(Duration.ofMinutes(-90));
        assertEquals("-PT1H30M", back.toString());
        assertEquals(duration, ICalDurations.toICalDuration(Duration.ofMinutes(90)));
    }

    @Test
    public void parametersAreReadWithGettersLikeProperties() throws Exception {
        VEvent event = ICalModel.getCalendars(ICal.parse(CALENDAR)).get(0).getEvents().get(0);
        ICalProperty attendee = event.getComponent().property("ATTENDEE");
        assertEquals("Ann", ICalModel.get(attendee, Parameters.getCN()));
        assertSame(PartStat.Accepted, ICalModel.get(attendee, Parameters.getPARTSTAT()));
        assertSame(Parameters.getCN(), Parameters.getCN());
        assertEquals("FEATURE", Parameters.getFEATURE().getName());
    }
}

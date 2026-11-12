package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.BusyStatus;
import io.github.calendrum.model.Cardinality;
import io.github.calendrum.model.Conference;
import io.github.calendrum.model.ConferenceFeature;
import io.github.calendrum.model.Decoded;
import io.github.calendrum.model.ICalExtension;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.ICalRegistry;
import io.github.calendrum.model.Properties;
import io.github.calendrum.model.PropertyDefinition;
import io.github.calendrum.model.PropertyKey;
import io.github.calendrum.model.Rfc5545;
import io.github.calendrum.model.Rfc7986;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.model.VEvent;
import io.github.calendrum.model.VLocation;
import io.github.calendrum.model.ValueType;
import io.github.calendrum.model.VendorProperties;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ParseOptions;
import io.github.calendrum.syntax.ParseResult;
import java.util.Collections;
import org.junit.Test;

public class JavaRegistryTest {
    private static final String CALENDAR = "BEGIN:VCALENDAR\r\n"
        + "COLOR:teal\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:1\r\n"
        + "CONFERENCE;VALUE=URI;FEATURE=VIDEO:https://meet.example/1\r\n"
        + "X-MICROSOFT-CDO-BUSYSTATUS:BUSY\r\n"
        + "BEGIN:VLOCATION\r\n"
        + "UID:l\r\n"
        + "NAME:Hall\r\n"
        + "END:VLOCATION\r\n"
        + "END:VEVENT\r\n"
        + "END:VCALENDAR\r\n";

    @Test
    public void aRegistryIsBuiltFromExtensions() throws Exception {
        PropertyKey<String> room = new PropertyKey<>("X-ACME-ROOM", Properties.getSUMMARY().getCodec());
        ICalExtension acme = registry -> registry.property(new PropertyDefinition(
            room,
            ValueType.Text,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.singletonMap("VEVENT", Cardinality.Optional),
            "Acme"));
        ICalRegistry registry = ICalRegistry.builder()
            .install(Rfc5545.INSTANCE)
            .install(Rfc7986.INSTANCE)
            .install(VendorProperties.INSTANCE)
            .install(acme)
            .build();
        assertSame(room, registry.property("x-acme-room").getKey());
        assertSame(Properties.getCOLOR(), ICalRegistry.getDefault().property("COLOR").getKey());
        assertNull(ICalRegistry.getDefault().property("X-ACME-ROOM"));

        ParseResult result = ICal.parser(ParseOptions.builder().registry(registry).decodeEagerly(true).build())
            .parse(CALENDAR);
        assertTrue(result.getProblems().isEmpty());
        ICalComponent event = result.getComponents().get(0).getComponents().get(0);
        Decoded<Object> status = ICalModel.decodeAny(event, "X-MICROSOFT-CDO-BUSYSTATUS", registry);
        assertEquals(BusyStatus.Busy, ((Decoded.Value<Object>) status).getValue());
        assertEquals(BusyStatus.Busy, ICalModel.get(event, VendorProperties.getMICROSOFT_CDO_BUSYSTATUS()));
    }

    @Test
    public void wrappersExposeTheNewRfcs() throws Exception {
        VCalendar calendar = ICalModel.getCalendars(ICal.parse(CALENDAR)).get(0);
        assertEquals("teal", calendar.getColor());
        VEvent event = calendar.getEvents().get(0);
        Conference conference = event.getConferences().get(0);
        assertEquals(Collections.singletonList(ConferenceFeature.Video), conference.getFeatures());
        VLocation hall = event.getLocations().get(0);
        assertEquals("Hall", hall.getName());
        VEvent built = VEvent.builder()
            .uid("2")
            .stamp(ICalModel.get(ICal.parse("BEGIN:X\r\nDTSTAMP:20260901T100000Z\r\nEND:X\r\n").getComponents().get(0),
                Properties.getDTSTAMP()))
            .color("red")
            .conference(new Conference("tel:1"))
            .location(VLocation.builder().uid("l2").name("Room").build())
            .refId("trip")
            .build();
        assertEquals("red", built.getColor());
        assertEquals("Room", built.getLocations().get(0).getName());
        assertEquals(Collections.singletonList("trip"), built.getRefIds());
    }
}

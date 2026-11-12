package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.Attendee;
import io.github.calendrum.model.DecodeContext;
import io.github.calendrum.model.Decoded;
import io.github.calendrum.model.EncodeContext;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.Parameters;
import io.github.calendrum.model.PartStat;
import io.github.calendrum.model.Properties;
import io.github.calendrum.model.PropertyCodec;
import io.github.calendrum.model.PropertyKey;
import io.github.calendrum.model.Role;
import io.github.calendrum.model.Trigger;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ICalProperty;
import io.github.calendrum.value.ICalDateTime;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import kotlin.time.jdk8.InstantConversionsJDK8Kt;
import org.junit.Test;

public class JavaModelTest {
    private static final String EVENT = "BEGIN:VEVENT\r\n"
        + "UID:42@example.com\r\n"
        + "DTSTART:20260901T080000Z\r\n"
        + "DTEND:20261301T090000Z\r\n"
        + "ATTENDEE;CN=Jane;PARTSTAT=ACCEPTED:mailto:jane@example.com\r\n"
        + "ATTENDEE;ROLE=CHAIR:mailto:bob@example.com\r\n"
        + "BEGIN:VALARM\r\n"
        + "ACTION:DISPLAY\r\n"
        + "TRIGGER:-PT15M\r\n"
        + "END:VALARM\r\n"
        + "END:VEVENT\r\n";

    private final ICalComponent event = ICal.parse(EVENT).getComponents().get(0);

    @Test
    public void propertiesByKeyAreStaticCalls() {
        assertEquals("42@example.com", ICalModel.get(event, Properties.getUID()));
        ICalDateTime start = ICalModel.get(event, Properties.getDTSTART());
        assertTrue(start instanceof ICalDateTime.Utc);
        Instant instant = InstantConversionsJDK8Kt.toJavaInstant(((ICalDateTime.Utc) start).getInstant());
        assertEquals(Instant.parse("2026-09-01T08:00:00Z"), instant);
        assertNull(ICalModel.get(event, Properties.getDTEND()));
        assertNull(ICalModel.get(event, Properties.getSUMMARY()));
    }

    @Test
    public void decodeTellsWhy() {
        Decoded<ICalDateTime> end = ICalModel.decode(event, Properties.getDTEND());
        assertTrue(end instanceof Decoded.Malformed);
        assertSame(ProblemCode.OutOfRange, ((Decoded.Malformed) end).getProblem().getCode());
        assertSame(Decoded.Missing.INSTANCE, ICalModel.decode(event, Properties.getSUMMARY()));
        Decoded<String> uid = ICalModel.decode(event, Properties.getUID());
        assertTrue(uid instanceof Decoded.Value);
        assertEquals(Collections.emptyList(), ((Decoded.Value<String>) uid).getProblems());
    }

    @Test
    public void listsAndParameters() {
        List<Attendee> attendees = ICalModel.all(event, Properties.getATTENDEE());
        assertEquals(2, attendees.size());
        assertSame(PartStat.Accepted, attendees.get(0).getPartStat());
        assertEquals("Jane", attendees.get(0).getCn());
        assertSame(Role.Chair, attendees.get(1).getRole());
        ICalProperty first = event.properties("ATTENDEE").get(0);
        assertEquals("Jane", ICalModel.get(first, Parameters.getCN()));
        assertSame(PartStat.Accepted, ICalModel.get(first, Parameters.getPARTSTAT()));
        assertNull(ICalModel.get(first, Parameters.getRSVP()));
        ICalComponent alarm = event.components("VALARM").get(0);
        Trigger trigger = ICalModel.get(alarm, Properties.getTRIGGER());
        assertTrue(trigger instanceof Trigger.Relative);
        assertEquals("-PT15M", ((Trigger.Relative) trigger).getDuration().toString());
    }

    @Test
    public void ownCodecsFromJava() {
        PropertyKey<String> shout = new PropertyKey<>("X-SHOUT", new PropertyCodec<String>() {
            @Override
            public Decoded<String> decode(ICalProperty property, DecodeContext context) {
                return new Decoded.Value<>(property.getValue().toUpperCase(), Collections.emptyList());
            }

            @Override
            public ICalProperty encode(String value, EncodeContext context) {
                return ICalProperty.of(context.getName(), value);
            }
        });
        ICalComponent component = ICalComponent.builder("VEVENT").property("X-SHOUT", "hello").build();
        assertEquals("HELLO", ICalModel.get(component, shout));
        assertEquals(DecodeContext.Lenient, DecodeContext.Lenient);
        assertTrue(DecodeContext.Strict.isStrict());
    }
}

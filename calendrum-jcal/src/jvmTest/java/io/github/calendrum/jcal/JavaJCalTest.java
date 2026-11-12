package io.github.calendrum.jcal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.ICal;
import io.github.calendrum.ProblemCode;
import io.github.calendrum.model.ICalRegistry;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.value.ICalDateTime;
import java.util.List;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import org.junit.Test;

public class JavaJCalTest {
    @Test
    public void jcalFromJava() {
        List<ICalComponent> components =
            ICal.parse("BEGIN:VEVENT\r\nUID:1\r\nDTSTART:20260601T080000Z\r\nEND:VEVENT\r\n").getComponents();
        String json = JCal.encodeToString(components);
        assertEquals("[[\"vevent\",[[\"uid\",{},\"text\",\"1\"],"
            + "[\"dtstart\",{},\"date-time\",\"2026-06-01T08:00:00Z\"]],[]]]", json);
        JsonElement element = JCal.encodeToJsonElement(components, JCalOptions.Default);
        JCalResult read = JCal.decodeFromString(json);
        assertEquals(components, read.getComponents());
        assertEquals(components, JCal.decodeFromJsonElement(element).getComponents());
        JCalOptions options = JCalOptions.builder().registry(ICalRegistry.getDefault()).build();
        assertEquals(json, JCal.encodeToString(components, options));
        assertEquals(ProblemCode.InvalidJson, JCal.decodeFromString("{", options).getProblems().get(0).getCode());
        ICalDateTime start = Json.Default.decodeFromString(ICalSerializers.getDateTime(), "\"2026-06-01T08:00:00Z\"");
        assertTrue(start instanceof ICalDateTime.Utc);
        assertEquals("\"PT1H\"", Json.Default.encodeToString(ICalSerializers.getDuration(),
            Json.Default.decodeFromString(ICalSerializers.getDuration(), "\"PT1H\"")));
    }
}

package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.Properties;
import io.github.calendrum.syntax.Feature;
import io.github.calendrum.syntax.ICalParser;
import io.github.calendrum.syntax.ParseOptions;
import io.github.calendrum.syntax.ParseResult;
import io.github.calendrum.syntax.Selection;
import io.github.calendrum.syntax.TimeZoneSelection;
import io.github.calendrum.syntax.WriteOptions;
import org.junit.Test;

public class JavaSelectionTest {
    private static final String TEXT = "BEGIN:VCALENDAR\r\n"
        + "VERSION:2.0\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:1\r\n"
        + "DTSTART:20261001T100000Z\r\n"
        + "SUMMARY:Lecture\r\n"
        + "X-ACME-ROOM:101\r\n"
        + "END:VEVENT\r\n"
        + "BEGIN:VTODO\r\n"
        + "UID:2\r\n"
        + "END:VTODO\r\n"
        + "END:VCALENDAR\r\n";

    @Test
    public void selectionsAreBuiltAndUsedThroughBuilders() throws ICalParseException {
        Selection selection = Selection.builder()
            .components("VEVENT")
            .properties(Properties.getUID(), Properties.getSUMMARY())
            .properties("X-ACME-ROOM")
            .need(Feature.Occurrences)
            .timeZones(TimeZoneSelection.Referenced)
            .build();
        ICalParser parser = ICal.parser(ParseOptions.builder().select(selection).build());
        ParseResult result = parser.parse(TEXT);
        assertTrue(result.isPartial());
        assertEquals(1, result.getComponents().get(0).getComponents().size());
        assertEquals(4, result.getComponents().get(0).getComponents().get(0).getProperties().size());
        assertThrows(IllegalStateException.class, () -> ICal.writer().encodeToString(result.getComponents()));
        String written = ICal.writer(WriteOptions.builder().allowPartialWrite(true).build())
            .encodeToString(result.getComponents());
        assertFalse(written.contains("VTODO"));
        assertSame(Selection.Schedule, ICal.parser(ParseOptions.builder().select(Selection.Schedule).build())
            .getOptions().getSelection());
        assertEquals(TimeZoneSelection.None, Selection.SyncMetadata.getTimeZones());
    }

    @Test
    public void untrustedOptionsAreAStaticGetter() throws ICalParseException {
        ParseOptions untrusted = ParseOptions.getUntrusted();
        assertEquals(8, untrusted.getLimits().getMaxDepth());
        assertEquals(64 * 1024, untrusted.getLimits().getMaxLineOctets());
        assertEquals(100_000, untrusted.getLimits().getMaxComponents());
        assertFalse(ICal.parser(untrusted).parse(TEXT).isPartial());
    }
}

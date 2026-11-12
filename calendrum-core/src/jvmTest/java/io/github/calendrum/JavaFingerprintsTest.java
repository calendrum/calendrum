package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.syntax.ComponentFingerprint;
import io.github.calendrum.syntax.ComponentKey;
import io.github.calendrum.syntax.FingerprintDiff;
import io.github.calendrum.syntax.FingerprintOptions;
import io.github.calendrum.syntax.Fingerprints;
import io.github.calendrum.syntax.ICalComponent;
import io.github.calendrum.syntax.ParseLimits;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import org.junit.Test;

public class JavaFingerprintsTest {
    private static final String FEED = "BEGIN:VCALENDAR\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:1@example.com\r\n"
        + "DTSTAMP:20260101T000000Z\r\n"
        + "SUMMARY:Lecture\r\n"
        + "END:VEVENT\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:2@example.com\r\n"
        + "SUMMARY:Seminar\r\n"
        + "END:VEVENT\r\n"
        + "END:VCALENDAR\r\n";

    private static byte[] bytes(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    @Test
    public void fingerprintsDiffAndIndexFromJava() throws ICalParseException {
        Fingerprints before = ICal.fingerprints(bytes(FEED));
        String stored = before.encodeToString();
        byte[] now = bytes(FEED.replace("SUMMARY:Lecture", "SUMMARY:Moved").replace("20260101T000000Z", "20260202T000000Z"));
        Fingerprints after = ICal.fingerprints(now);
        FingerprintDiff diff = after.diff(Fingerprints.decodeFromString(stored));
        assertEquals(1, diff.getChanged().size());
        assertEquals(1, diff.getUnchangedCount());
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getRemoved().isEmpty());

        ComponentFingerprint changed = diff.getChanged().get(0);
        ComponentKey key = changed.getKey();
        assertEquals("1@example.com", key.getUid());
        assertNull(key.getRecurrenceId());
        assertEquals(0, key.getCalendarIndex());
        assertEquals(-1, key.getOrdinal());
        ICalComponent event = ICal.parser().parseComponent(now, changed);
        assertEquals("Moved", event.property("SUMMARY").getValue());
        assertEquals(1, after.getAlgorithmVersion());
        assertEquals(changed.getStartOffset(), event.getSpan().getStartOffset());
    }

    @Test
    public void optionsFromJava() {
        FingerprintOptions options = FingerprintOptions.builder()
            .ignoredProperties(Collections.singleton("summary"))
            .build();
        assertEquals(Collections.singleton("SUMMARY"), options.getIgnoredProperties());
        byte[] now = bytes(FEED.replace("SUMMARY:Lecture", "SUMMARY:Moved"));
        FingerprintDiff diff = ICal.fingerprints(now, options).diff(ICal.fingerprints(bytes(FEED), options));
        assertEquals(2, diff.getUnchangedCount());
        assertEquals(Collections.singleton("DTSTAMP"), FingerprintOptions.Default.getIgnoredProperties());
    }

    @Test
    public void timeBudgetFromJavaDuration() {
        ParseLimits limits = ICalStreams.maxTime(ParseLimits.builder(), Duration.ofMillis(1500)).build();
        assertTrue(limits.toString().contains("maxTime=1500ms"));
        assertNull(ICalStreams.maxTime(ParseLimits.builder(), null).build().getMaxTime());
        try {
            ICalStreams.maxTime(ParseLimits.builder(), Duration.ZERO).build();
            throw new AssertionError("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("maxTime must be positive", expected.getMessage());
        }
    }
}

package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import io.github.calendrum.model.IcsSchema;
import io.github.calendrum.model.Properties;
import io.github.calendrum.model.SchemaResult;
import io.github.calendrum.value.ICalDateTime;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import kotlin.Unit;
import org.junit.Test;

public class JavaSchemaTest {
    private static final String CALENDAR = "BEGIN:VCALENDAR\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:1\r\n"
        + "DTSTAMP:20260901T080000Z\r\n"
        + "SUMMARY:Standup\r\n"
        + "END:VEVENT\r\n"
        + "BEGIN:VEVENT\r\n"
        + "UID:2\r\n"
        + "DTSTAMP:20260901T080000Z\r\n"
        + "END:VEVENT\r\n"
        + "BEGIN:VEVENT\r\n"
        + "SUMMARY:No uid\r\n"
        + "END:VEVENT\r\n"
        + "END:VCALENDAR\r\n";

    public static final class Meeting {
        private final String uid;
        private final ICalDateTime stamp;
        private final String summary;

        public Meeting(String uid, ICalDateTime stamp, String summary) {
            this.uid = uid;
            this.stamp = stamp;
            this.summary = summary;
        }

        public String getUid() {
            return uid;
        }

        public ICalDateTime getStamp() {
            return stamp;
        }

        public String getSummary() {
            return summary;
        }
    }

    public static final class MeetingSchema extends IcsSchema<Meeting> {
        private final Field<String> uid = field(Properties.getUID(), Meeting::getUid);
        private final Field<ICalDateTime> stamp = field(Properties.getDTSTAMP(), Meeting::getStamp);
        private final Field<String> summary = optionalField(Properties.getSUMMARY(), Meeting::getSummary);

        public MeetingSchema() {
            super("VEVENT");
        }

        @Override
        public Meeting create(Values values) {
            return new Meeting(values.get(uid), values.get(stamp), values.get(summary));
        }
    }

    @Test
    public void aJavaSchemaReadsAndWrites() throws Exception {
        MeetingSchema schema = new MeetingSchema();
        byte[] bytes = CALENDAR.getBytes(StandardCharsets.UTF_8);
        SchemaResult<Meeting> result = ICal.parser().decodeWithProblems(bytes, schema);
        assertEquals(1, result.getSkipped());
        List<Meeting> meetings = result.getItems();
        assertEquals(Arrays.asList("1", "2"), Arrays.asList(meetings.get(0).getUid(), meetings.get(1).getUid()));
        assertEquals("Standup", meetings.get(0).getSummary());
        assertNull(meetings.get(1).getSummary());
        byte[] feed = ICal.writer().encode(meetings, schema, calendar -> {
            calendar.setProdId("-//Java//Schema//EN");
            return Unit.INSTANCE;
        });
        List<Meeting> again = ICal.parser().decode(feed, schema);
        assertEquals(2, again.size());
        assertEquals("Standup", again.get(0).getSummary());
        assertEquals(meetings.get(1).getStamp(), again.get(1).getStamp());
    }
}

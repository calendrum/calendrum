package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.model.Cardinality;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.Method;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.validate.ElementPath;
import io.github.calendrum.validate.RuleSet;
import io.github.calendrum.validate.ValidationReport;
import io.github.calendrum.validate.Validator;
import java.util.Collections;
import org.junit.Test;

public class JavaValidationTest {
    private static final String CALENDAR = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nMETHOD:REQUEST\r\n"
        + "BEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260601T100000Z\r\nSUMMARY:x\r\n"
        + "ORGANIZER:mailto:o@example.com\r\nATTENDEE:mailto:a@example.com\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n";

    @Test
    public void validateFromJava() {
        VCalendar calendar = ICalModel.getCalendars(ICal.parse(CALENDAR)).get(0);
        assertTrue(Validator.validate(calendar, RuleSet.getRfc5545()).isValid());
        assertTrue(Validator.validate(calendar, RuleSet.itip(Method.Request)).isValid());
        ValidationReport publish = Validator.validate(calendar, RuleSet.itip(Method.Publish));
        assertFalse(publish.isValid());
        assertEquals(1, publish.issues(ProblemCode.MethodMismatch).size());
        RuleSet strict = RuleSet.builder()
            .cardinality("VEVENT", "LOCATION", Cardinality.One)
            .rule((component, path) -> Collections.emptyList())
            .build();
        ValidationReport report = Validator.validate(calendar, strict);
        assertEquals("VCALENDAR[0]/VEVENT[0]/LOCATION[0]", report.getIssues().get(0).getPath().toString());
        assertEquals("VCALENDAR[0]@X", ElementPath.Root.child("VCALENDAR", 0).parameter("X").toString());
    }
}

package io.github.calendrum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.github.calendrum.fixup.Fixup;
import io.github.calendrum.fixup.FixupResult;
import io.github.calendrum.fixup.Fixups;
import io.github.calendrum.lint.LintReport;
import io.github.calendrum.lint.LintRules;
import io.github.calendrum.lint.Linter;
import io.github.calendrum.model.ICalModel;
import io.github.calendrum.model.VCalendar;
import io.github.calendrum.syntax.ParseOptions;
import org.junit.Test;

public class JavaFixupsTest {
    private static final String OUTLOOK = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\n"
        + "PRODID:-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN\r\nBEGIN:VEVENT\r\nUID:1\r\n"
        + "DTSTAMP:20260101T000000Z\r\nDTSTART;TZID=Eastern Standard Time:20261005T100000\r\n"
        + "ORGANIZER:MAILTO:a@example.com\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n";

    @Test
    public void fixupsAndLinterFromJava() throws ICalParseException {
        Fixup fixup = Fixups.compose(Fixups.byProdId(), Fixups.xWrCalendarName);
        ParseOptions.Builder options = ParseOptions.builder();
        fixup.interceptors().forEach(options::intercept);
        VCalendar calendar = ICalModel.getCalendars(ICal.parser(options.build()).parse(OUTLOOK)).get(0);
        LintReport report = Linter.lint(calendar);
        assertEquals(2, report.getFixable().size());
        FixupResult result = fixup.apply(calendar);
        assertTrue(result.isChanged());
        assertEquals("mailto:a@example.com", result.getCalendar().getEvents().get(0).getOrganizer().getAddress());
        LintReport after = Linter.lint(Linter.fix(calendar, report), LintRules.Default);
        assertEquals(1, after.getIssues().size());
        assertEquals(ProblemCode.MissingTimeZone, after.getIssues().get(0).getCode());
        assertEquals(1, Linter.lint(result.getCalendar()).getIssues().size());
        assertEquals("outlook", Fixups.outlook.getId().substring(Fixups.outlook.getId().length() - 7));
    }
}

package io.github.calendrum.tools

import io.github.calendrum.ICal
import io.github.calendrum.model.Canonical
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.tzdata.TzData
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.Clock

fun main(args: Array<String>) {
    val base = args.indexOf("--base").takeIf { it >= 0 }?.let { LocalDate.parse(args[it + 1]) }
        ?: nextMonday(Clock.System.now().toLocalDateTime(TimeZone.UTC).date)
    val output = Paths.get(args.firstOrNull { !it.startsWith("--") && it != base.toString() } ?: "build/reference-feed")
    val feed = ReferenceFeed(base)
    Files.createDirectories(output)
    Files.write(output.resolve("calendrum-reference.ics"), feed.bytes())
    Files.write(output.resolve("expected.md"), feed.expected().toByteArray())
    println("reference feed for the week of $base written to $output")
}

internal fun nextMonday(today: LocalDate): LocalDate {
    var day = today.plus(DatePeriod(days = 1))
    while (day.dayOfWeek != DayOfWeek.MONDAY) day = day.plus(DatePeriod(days = 1))
    return day
}

// The reference feed for manual acceptance in calendar applications: 19 numbered cases relative to a base Monday, UIDs derived
// with UUIDv5, VTIMEZONEs from calendrum-tzdata, written folded; expected.md lists the expected occurrences of the first
// ten weeks.
internal class ReferenceFeed(private val base: LocalDate) {
    private fun day(offset: Int): String {
        val date = base.plus(DatePeriod(days = offset))
        return "${date.year}".padStart(4, '0') + "${date.month.ordinal + 1}".padStart(2, '0') +
            "${date.day}".padStart(2, '0')
    }

    private fun at(offset: Int, time: String): String = "${day(offset)}T$time"

    private fun uid(key: String): String = Canonical.uidV5("reference.calendrum.example", key)

    private fun event(key: String, summary: String, vararg lines: String): List<String> =
        listOf("BEGIN:VEVENT", "UID:${uid(key)}", "DTSTAMP:${day(0)}T000000Z", "SUMMARY:$summary") + lines.toList() +
            "END:VEVENT"

    fun text(): String {
        val lines = ArrayList<String>()
        lines += listOf(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//Calendrum//Reference feed//EN", "CALSCALE:GREGORIAN",
            "NAME:Calendrum reference", "X-WR-CALNAME:Calendrum reference",
            "DESCRIPTION:Cases every client must show correctly",
            "COLOR:teal", "REFRESH-INTERVAL;VALUE=DURATION:PT12H", "X-PUBLISHED-TTL:PT12H",
        )
        lines += event(
            "utc",
            "01 UTC meeting 09:00-10:00 UTC",
            "DTSTART:${at(0, "090000")}Z",
            "DTEND:${at(0, "100000")}Z",
        )
        lines += event(
            "moscow",
            "02 Moscow 10:00-11:00 (UTC+3, no DST)",
            "DTSTART;TZID=Europe/Moscow:${at(0, "100000")}",
            "DTEND;TZID=Europe/Moscow:${at(0, "110000")}",
        )
        lines += event(
            "new-york",
            "03 New York 09:00-09:30 (DST zone)",
            "DTSTART;TZID=America/New_York:${at(1, "090000")}",
            "DURATION:PT30M",
        )
        lines += event(
            "kolkata",
            "04 Kolkata 14:30 (UTC+5:30)",
            "DTSTART;TZID=Asia/Kolkata:${at(1, "143000")}",
            "DTEND;TZID=Asia/Kolkata:${at(1, "153000")}",
        )
        lines += event(
            "lord-howe",
            "05 Lord Howe 08:00 (30-minute DST)",
            "DTSTART;TZID=Australia/Lord_Howe:${at(2, "080000")}",
            "DTEND;TZID=Australia/Lord_Howe:${at(2, "090000")}",
        )
        lines += event("all-day", "06 All day, one day", "DTSTART;VALUE=DATE:${day(2)}", "DTEND;VALUE=DATE:${day(3)}")
        lines += event(
            "multi-day",
            "07 All day, three days",
            "DTSTART;VALUE=DATE:${day(3)}",
            "DTEND;VALUE=DATE:${day(6)}",
            "TRANSP:TRANSPARENT",
        )
        lines += event(
            "floating",
            "08 Floating 12:00 in the viewer's zone",
            "DTSTART:${at(3, "120000")}",
            "DURATION:PT1H",
        )
        lines += event(
            "weekly",
            "09 Weekly Mon+Wed 08:00 Berlin, 6 times, 2nd Wed removed, 2nd Mon moved",
            "DTSTART;TZID=Europe/Berlin:${at(0, "080000")}",
            "DTEND;TZID=Europe/Berlin:${at(0, "083000")}",
            "RRULE:FREQ=WEEKLY;BYDAY=MO,WE;COUNT=6",
            "EXDATE;TZID=Europe/Berlin:${at(9, "080000")}",
        )
        lines += listOf(
            "BEGIN:VEVENT",
            "UID:${uid("weekly")}",
            "DTSTAMP:${day(0)}T000000Z",
            "RECURRENCE-ID;TZID=Europe/Berlin:${at(7, "080000")}",
            "SUMMARY:09 Weekly (moved to Tuesday 10:00)",
            "DTSTART;TZID=Europe/Berlin:${at(8, "100000")}",
            "DTEND;TZID=Europe/Berlin:${at(8, "103000")}",
            "END:VEVENT",
        )
        lines += event(
            "monthly",
            "10 Last Friday of the month 16:00 London, 3 times",
            "DTSTART;TZID=Europe/London:${lastFriday()}T160000",
            "DURATION:PT1H",
            "RRULE:FREQ=MONTHLY;BYDAY=-1FR;COUNT=3",
        )
        lines += event(
            "daily",
            "11 Daily 07:00 UTC until day 4",
            "DTSTART:${at(0, "070000")}Z",
            "DURATION:PT15M",
            "RRULE:FREQ=DAILY;UNTIL=${at(4, "070000")}Z",
        )
        lines += event("yearly", "12 Yearly, all day", "DTSTART;VALUE=DATE:${day(4)}", "RRULE:FREQ=YEARLY;COUNT=5")
        lines += event(
            "cancelled",
            "13 Cancelled meeting (should be hidden or struck out)",
            "DTSTART:${at(4, "100000")}Z",
            "DURATION:PT1H",
            "STATUS:CANCELLED",
        )
        lines += event(
            "tentative",
            "14 Tentative meeting",
            "DTSTART:${at(4, "130000")}Z",
            "DURATION:PT1H",
            "STATUS:TENTATIVE",
        )
        lines += event(
            "alarms", "15 Alarms 15 minutes before and at start",
            "DTSTART;TZID=Europe/Berlin:${at(1, "150000")}", "DURATION:PT1H",
            "BEGIN:VALARM", "ACTION:DISPLAY", "DESCRIPTION:Reference alarm", "TRIGGER:-PT15M", "END:VALARM",
            "BEGIN:VALARM", "ACTION:DISPLAY", "DESCRIPTION:Starts now", "TRIGGER;RELATED=START:PT0S", "END:VALARM",
        )
        lines += event(
            "text", "16 Текст: кириллица, emoji 📅, запятая\\, точка с запятой\\; и обратная черта \\\\",
            "DTSTART:${at(2, "110000")}Z", "DURATION:PT1H",
            "DESCRIPTION:Line one\\nLine two with a long text that the writer has to fold into several physical " +
                "lines without breaking multi-byte characters: «ёлка», 東京, 🎄.",
            "LOCATION:Москва\\, ул. Тверская\\, 1", "GEO:55.757;37.613", "CATEGORIES:Reference,Тест",
            "URL:https://example.com/reference", "COLOR:crimson",
        )
        lines += event(
            "people", "17 Meeting with an organizer and attendees", "DTSTART:${at(3, "140000")}Z", "DURATION:PT45M",
            "ORGANIZER;CN=\"Alice Example\":mailto:alice@example.com",
            "ATTENDEE;CN=\"Bob, Jr.\";ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED:mailto:bob@example.com",
            "ATTENDEE;CN=Иван Петров;ROLE=OPT-PARTICIPANT;PARTSTAT=TENTATIVE:mailto:ivan@example.com",
            "ATTENDEE;CUTYPE=ROOM;CN=Room 1:mailto:room1@example.com",
            "CONFERENCE;VALUE=URI;FEATURE=VIDEO;LABEL=Video call:https://example.com/call",
        )
        lines += event(
            "dst",
            "18 Overnight 23:00-05:00 New York",
            "DTSTART;TZID=America/New_York:${at(5, "230000")}",
            "DTEND;TZID=America/New_York:${at(6, "050000")}",
        )
        lines += listOf(
            "BEGIN:VTODO",
            "UID:${uid("todo")}",
            "DTSTAMP:${day(0)}T000000Z",
            "SUMMARY:19 To-do due on day 5",
            "DUE;VALUE=DATE:${day(5)}",
            "PRIORITY:1",
            "STATUS:NEEDS-ACTION",
            "END:VTODO",
        )
        lines += "END:VCALENDAR"
        return lines.joinToString("") { "$it\r\n" }
    }

    private fun lastFriday(): String {
        var date = LocalDate(base.year, base.month, 1).plus(DatePeriod(months = 1)).plus(DatePeriod(days = -1))
        while (date.dayOfWeek != DayOfWeek.FRIDAY) date = date.plus(DatePeriod(days = -1))
        if (date < base) {
            date = LocalDate(base.year, base.month, 1).plus(DatePeriod(months = 2)).plus(DatePeriod(days = -1))
            while (date.dayOfWeek != DayOfWeek.FRIDAY) date = date.plus(DatePeriod(days = -1))
        }
        return "${date.year}".padStart(4, '0') + "${date.month.ordinal + 1}".padStart(2, '0') +
            "${date.day}".padStart(2, '0')
    }

    fun calendar(): VCalendar = ICal.parse(text()).calendars.single()

    fun bytes(): ByteArray = ICal.writer(WriteOptions.builder().byteStable(false).addMissingTimeZones(TzData).build())
        .encodeToByteArray(calendar().component)

    fun expected(): String {
        val calendar = ICal.parse(bytes()).calendars.single()
        val from = base.atStartOfDayIn(TimeZone.UTC)
        val to = base.plus(DatePeriod(days = 70)).atStartOfDayIn(TimeZone.UTC)
        val options = OccurrenceOptions.builder().zones(TzData.resolver).floatingZone(TimeZone.UTC).build()
        val rows = calendar.occurrences(from, to, options).map { occurrence ->
            val summary = occurrence.component.property("SUMMARY")?.value.orEmpty().substringBefore(' ')
            "| $summary | ${occurrence.startLocal} | ${occurrence.start} | ${occurrence.end} |"
        }.toList()
        return buildString {
            append("# Calendrum reference feed, week of $base\n\n")
            append("Occurrences in the first ten weeks (floating times shown in UTC). ")
            append("Cancelled ones are not listed.\n\n")
            append("| Case | Local start | Start (UTC) | End (UTC) |\n|---|---|---|---|\n")
            for (row in rows) append(row).append('\n')
        }
    }
}

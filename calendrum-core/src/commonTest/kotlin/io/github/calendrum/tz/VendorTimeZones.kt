package io.github.calendrum.tz

import io.github.calendrum.ICal
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars

internal class VendorZone(val vendor: String, val ianaId: String, val text: String)

internal fun calendarOf(vararg zones: String): VCalendar =
    ICal.parse("BEGIN:VCALENDAR\r\nVERSION:2.0\r\n" + zones.joinToString("") + "END:VCALENDAR\r\n").calendars.single()

private fun zone(vararg lines: String): String = lines.joinToString("") { "$it\r\n" }

internal val APPLE_BERLIN = VendorZone(
    "apple",
    "Europe/Berlin",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Berlin",
        "BEGIN:DAYLIGHT",
        "TZOFFSETFROM:+0100",
        "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU",
        "DTSTART:19810329T020000",
        "TZNAME:MESZ",
        "TZOFFSETTO:+0200",
        "END:DAYLIGHT",
        "BEGIN:STANDARD",
        "TZOFFSETFROM:+0200",
        "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU",
        "DTSTART:19961027T030000",
        "TZNAME:MEZ",
        "TZOFFSETTO:+0100",
        "END:STANDARD",
        "END:VTIMEZONE",
    ),
)

internal val APPLE_TOKYO = VendorZone(
    "apple",
    "Asia/Tokyo",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:Asia/Tokyo",
        "BEGIN:STANDARD",
        "TZOFFSETFROM:+0900",
        "RRULE:FREQ=YEARLY;BYMONTH=1",
        "DTSTART:19700101T000000",
        "TZNAME:JST",
        "TZOFFSETTO:+0900",
        "END:STANDARD",
        "END:VTIMEZONE",
    ),
)

internal val OUTLOOK_BERLIN = VendorZone(
    "outlook",
    "Europe/Berlin",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:W. Europe Standard Time",
        "BEGIN:STANDARD",
        "DTSTART:16010101T030000",
        "TZOFFSETFROM:+0200",
        "TZOFFSETTO:+0100",
        "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=-1SU;BYMONTH=10",
        "END:STANDARD",
        "BEGIN:DAYLIGHT",
        "DTSTART:16010101T020000",
        "TZOFFSETFROM:+0100",
        "TZOFFSETTO:+0200",
        "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=-1SU;BYMONTH=3",
        "END:DAYLIGHT",
        "END:VTIMEZONE",
    ),
)

internal val OUTLOOK_EASTERN = VendorZone(
    "outlook",
    "America/New_York",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:Eastern Standard Time",
        "BEGIN:STANDARD",
        "DTSTART:16010101T020000",
        "TZOFFSETFROM:-0400",
        "TZOFFSETTO:-0500",
        "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=1SU;BYMONTH=11",
        "END:STANDARD",
        "BEGIN:DAYLIGHT",
        "DTSTART:16010101T020000",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=2SU;BYMONTH=3",
        "END:DAYLIGHT",
        "END:VTIMEZONE",
    ),
)

internal val GOOGLE_NEW_YORK = VendorZone(
    "google",
    "America/New_York",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:America/New_York",
        "X-LIC-LOCATION:America/New_York",
        "BEGIN:DAYLIGHT",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "TZNAME:EDT",
        "DTSTART:19700308T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU",
        "END:DAYLIGHT",
        "BEGIN:STANDARD",
        "TZOFFSETFROM:-0400",
        "TZOFFSETTO:-0500",
        "TZNAME:EST",
        "DTSTART:19701101T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU",
        "END:STANDARD",
        "END:VTIMEZONE",
    ),
)

internal val THUNDERBIRD_MOSCOW = VendorZone(
    "thunderbird",
    "Europe/Moscow",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:/mozilla.org/20050126_1/Europe/Moscow",
        "X-LIC-LOCATION:Europe/Moscow",
        "BEGIN:STANDARD",
        "TZOFFSETFROM:+0300",
        "TZOFFSETTO:+0300",
        "TZNAME:MSK",
        "DTSTART:19700101T000000",
        "END:STANDARD",
        "END:VTIMEZONE",
    ),
)

internal val LORD_HOWE = VendorZone(
    "libical",
    "Australia/Lord_Howe",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:Australia/Lord_Howe",
        "BEGIN:STANDARD",
        "TZOFFSETFROM:+1100",
        "TZOFFSETTO:+1030",
        "TZNAME:+1030",
        "DTSTART:19700405T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU",
        "END:STANDARD",
        "BEGIN:DAYLIGHT",
        "TZOFFSETFROM:+1030",
        "TZOFFSETTO:+1100",
        "TZNAME:+11",
        "DTSTART:19701004T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=1SU",
        "END:DAYLIGHT",
        "END:VTIMEZONE",
    ),
)

internal val RFC_EASTERN = VendorZone(
    "rfc5545",
    "America/New_York",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:America/New_York",
        "LAST-MODIFIED:20050809T050000Z",
        "BEGIN:DAYLIGHT",
        "DTSTART:19670430T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "TZNAME:EDT",
        "END:DAYLIGHT",
        "BEGIN:STANDARD",
        "DTSTART:19671029T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z",
        "TZOFFSETFROM:-0400",
        "TZOFFSETTO:-0500",
        "TZNAME:EST",
        "END:STANDARD",
        "BEGIN:DAYLIGHT",
        "DTSTART:19740106T020000",
        "RDATE:19750223T020000",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "TZNAME:EDT",
        "END:DAYLIGHT",
        "BEGIN:DAYLIGHT",
        "DTSTART:19760425T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "TZNAME:EDT",
        "END:DAYLIGHT",
        "BEGIN:DAYLIGHT",
        "DTSTART:19870405T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "TZNAME:EDT",
        "END:DAYLIGHT",
        "BEGIN:DAYLIGHT",
        "DTSTART:20070311T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU",
        "TZOFFSETFROM:-0500",
        "TZOFFSETTO:-0400",
        "TZNAME:EDT",
        "END:DAYLIGHT",
        "BEGIN:STANDARD",
        "DTSTART:20071104T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU",
        "TZOFFSETFROM:-0400",
        "TZOFFSETTO:-0500",
        "TZNAME:EST",
        "END:STANDARD",
        "END:VTIMEZONE",
    ),
)

internal val YANDEX_UTC = VendorZone(
    "yandex",
    "Etc/UTC",
    zone(
        "BEGIN:VTIMEZONE",
        "TZID:Etc/UTC",
        "X-LIC-LOCATION:Etc/UTC",
        "BEGIN:STANDARD",
        "TZOFFSETFROM:+0000",
        "TZOFFSETTO:+0000",
        "TZNAME:UTC",
        "DTSTART:16010101T000000",
        "RDATE:16010101T000000",
        "END:STANDARD",
        "END:VTIMEZONE",
    ),
)

internal val VENDOR_ZONES: List<VendorZone> = listOf(
    APPLE_BERLIN,
    APPLE_TOKYO,
    OUTLOOK_BERLIN,
    OUTLOOK_EASTERN,
    GOOGLE_NEW_YORK,
    THUNDERBIRD_MOSCOW,
    LORD_HOWE,
    RFC_EASTERN,
    YANDEX_UTC,
)

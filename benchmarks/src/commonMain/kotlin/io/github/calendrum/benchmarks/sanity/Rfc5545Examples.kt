package io.github.calendrum.benchmarks.sanity

internal const val RFC5545_GROUP_MEETING_LOGICAL_LINES: Int = 33

// The group-scheduled meeting example from RFC 5545 section 4 (https://www.rfc-editor.org/rfc/rfc5545#section-4), with
// CRLF line endings. Code components of RFCs are licensed by the IETF Trust under the Simplified BSD License (IETF
// Trust Legal Provisions); Copyright (c) 2009 IETF Trust and the persons identified as the document authors.
internal val RFC5545_GROUP_MEETING: String = listOf(
    "BEGIN:VCALENDAR",
    "PRODID:-//RDU Software//NONSGML HandCal//EN",
    "VERSION:2.0",
    "BEGIN:VTIMEZONE",
    "TZID:America/New_York",
    "BEGIN:STANDARD",
    "DTSTART:19981025T020000",
    "TZOFFSETFROM:-0400",
    "TZOFFSETTO:-0500",
    "TZNAME:EST",
    "END:STANDARD",
    "BEGIN:DAYLIGHT",
    "DTSTART:19990404T020000",
    "TZOFFSETFROM:-0500",
    "TZOFFSETTO:-0400",
    "TZNAME:EDT",
    "END:DAYLIGHT",
    "END:VTIMEZONE",
    "BEGIN:VEVENT",
    "DTSTAMP:19980309T231000Z",
    "UID:guid-1.example.com",
    "ORGANIZER:mailto:mrbig@example.com",
    "ATTENDEE;RSVP=TRUE;ROLE=REQ-PARTICIPANT;CUTYPE=GROUP:",
    " mailto:employee-A@example.com",
    "DESCRIPTION:Project XYZ Review Meeting",
    "CATEGORIES:MEETING",
    "CLASS:PUBLIC",
    "CREATED:19980309T130000Z",
    "SUMMARY:XYZ Project Review",
    "DTSTART;TZID=America/New_York:19980312T083000",
    "DTEND;TZID=America/New_York:19980312T093000",
    "LOCATION:1CP Conference Room 4350",
    "END:VEVENT",
    "END:VCALENDAR",
).joinToString(separator = "\r\n", postfix = "\r\n")

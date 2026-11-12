@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.reader

import io.github.calendrum.InternalICalApi
import io.github.calendrum.syntax.ContentSelection

internal val scheduleComponents: List<String> = listOf("VEVENT", "VTIMEZONE", "STANDARD", "DAYLIGHT")

// The Schedule preset of selective parsing, with the SyncMetadata preset below it.
internal val scheduleProperties: List<String> = listOf(
    "UID", "DTSTART", "DTEND", "DURATION", "SUMMARY", "LOCATION", "DESCRIPTION", "RRULE", "RDATE", "EXDATE",
    "RECURRENCE-ID", "STATUS", "SEQUENCE", "LAST-MODIFIED", "TZID", "TZOFFSETFROM", "TZOFFSETTO",
)

internal val syncMetadataComponents: List<String> = listOf("VEVENT")

internal val syncMetadataProperties: List<String> =
    listOf("UID", "SEQUENCE", "LAST-MODIFIED", "DTSTAMP", "RECURRENCE-ID")

internal fun selectionOf(components: Collection<String>, properties: Collection<String>): ContentSelection =
    ContentSelection.Builder()
        .components(*components.toTypedArray())
        .properties(*properties.toTypedArray())
        .build()

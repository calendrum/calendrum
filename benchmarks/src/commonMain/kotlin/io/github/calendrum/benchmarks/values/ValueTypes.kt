@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.values

import io.github.calendrum.InternalICalApi
import io.github.calendrum.value.ValueCodecs

internal const val NO_TYPE: Int = -1
internal const val GEO_TYPE: Int = -2

// The value type of a property: the VALUE parameter first, then the default type of RFC 5545 and RFC 7986; EXDATE and
// RDATE are lists. GEO is two FLOAT values separated by ';' (`GEO_TYPE`).
internal fun valueType(property: String, valueParameter: String?): Int {
    val list = property == "EXDATE" || property == "RDATE"
    if (valueParameter != null) {
        return when (valueParameter.uppercase()) {
            "DATE" -> if (list) ValueCodecs.DATE_LIST else ValueCodecs.DATE
            "DATE-TIME" -> if (list) ValueCodecs.DATE_TIME_LIST else ValueCodecs.DATE_TIME
            "PERIOD" -> ValueCodecs.PERIOD_LIST
            "DURATION" -> ValueCodecs.DURATION
            "URI", "CAL-ADDRESS" -> ValueCodecs.URI
            "BINARY" -> ValueCodecs.BINARY
            "BOOLEAN" -> ValueCodecs.BOOLEAN
            "INTEGER" -> ValueCodecs.INTEGER
            "FLOAT" -> ValueCodecs.FLOAT
            "TIME" -> ValueCodecs.TIME
            "UTC-OFFSET" -> ValueCodecs.UTC_OFFSET
            "RECUR" -> ValueCodecs.RECUR
            "TEXT" -> if (property in TEXT_LISTS) ValueCodecs.TEXT_LIST else ValueCodecs.TEXT
            else -> NO_TYPE
        }
    }
    return DEFAULT_TYPES[property] ?: NO_TYPE
}

private val TEXT_LISTS = setOf("CATEGORIES", "RESOURCES")

private val DEFAULT_TYPES: Map<String, Int> = buildMap {
    for (name in listOf("DTSTART", "DTEND", "DUE", "RECURRENCE-ID")) put(name, ValueCodecs.DATE_TIME)
    for (name in listOf("COMPLETED", "CREATED", "DTSTAMP", "LAST-MODIFIED", "ACKNOWLEDGED")) {
        put(name, ValueCodecs.DATE_TIME)
    }
    put("EXDATE", ValueCodecs.DATE_TIME_LIST)
    put("RDATE", ValueCodecs.DATE_TIME_LIST)
    for (name in listOf("DURATION", "TRIGGER", "REFRESH-INTERVAL")) put(name, ValueCodecs.DURATION)
    put("RRULE", ValueCodecs.RECUR)
    put("EXRULE", ValueCodecs.RECUR)
    for (name in listOf("ATTENDEE", "ORGANIZER", "URL", "TZURL", "SOURCE", "CONFERENCE", "IMAGE", "ATTACH")) {
        put(name, ValueCodecs.URI)
    }
    for (name in listOf("PRIORITY", "PERCENT-COMPLETE", "REPEAT", "SEQUENCE")) put(name, ValueCodecs.INTEGER)
    put("TZOFFSETFROM", ValueCodecs.UTC_OFFSET)
    put("TZOFFSETTO", ValueCodecs.UTC_OFFSET)
    put("FREEBUSY", ValueCodecs.PERIOD_LIST)
    put("CATEGORIES", ValueCodecs.TEXT_LIST)
    put("RESOURCES", ValueCodecs.TEXT_LIST)
    for (name in listOf("SUMMARY", "DESCRIPTION", "LOCATION", "COMMENT", "CONTACT", "TZNAME", "NAME")) {
        put(name, ValueCodecs.TEXT)
    }
    put("GEO", GEO_TYPE)
}

internal val TYPE_NAMES: List<String> = listOf(
    "BINARY",
    "BOOLEAN",
    "URI",
    "DATE",
    "DATE-TIME",
    "DURATION",
    "FLOAT",
    "INTEGER",
    "PERIOD",
    "RECUR",
    "TEXT",
    "TEXT list",
    "TIME",
    "UTC-OFFSET",
    "DATE list",
    "DATE-TIME list",
    "PERIOD list",
)

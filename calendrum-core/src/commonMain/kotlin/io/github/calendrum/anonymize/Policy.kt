package io.github.calendrum.anonymize

internal enum class ValueRule {
    Keep,
    Zone,
    Date,
    Recurrence,
    Text,
    Html,
    RequestStatus,
    Address,
    Uid,
    Url,
    Binary,
    Geo,
}

internal enum class ParameterRule {
    Keep,
    Zone,
    Text,
    Address,
    Url,
}

internal object Policy {
    // Kept only when StructuralValues accepts the value; anything else in these places is text.
    val structuralProperties: Set<String> = setOf(
        "VERSION", "PRODID", "CALSCALE", "METHOD", "STATUS", "CLASS", "TRANSP", "SEQUENCE", "PRIORITY", "DURATION",
        "REPEAT", "ACTION", "PERCENT-COMPLETE", "REFRESH-INTERVAL", "COLOR", "BUSYTYPE", "PROXIMITY",
        "LOCATION-TYPE", "PARTICIPANT-TYPE", "RESOURCE-TYPE", "TZNAME", "TZOFFSETFROM", "TZOFFSETTO",
    )

    val dateProperties: Set<String> = setOf(
        "DTSTART", "DTEND", "DUE", "DTSTAMP", "CREATED", "LAST-MODIFIED", "RECURRENCE-ID", "RDATE", "EXDATE",
        "COMPLETED", "FREEBUSY", "ACKNOWLEDGED", "TZUNTIL",
    )

    // A known zone stays, any other name gets one pseudonym wherever it occurs, so references still match.
    private val zoneProperties = setOf("TZID", "TZID-ALIAS-OF", "X-WR-TIMEZONE", "X-LIC-LOCATION")

    val allowedExtensions: Set<String> = setOf(
        "X-MICROSOFT-CDO-BUSYSTATUS",
        "X-MICROSOFT-CDO-INTENDEDSTATUS",
        "X-MICROSOFT-CDO-ALLDAYEVENT",
        "X-MICROSOFT-CDO-IMPORTANCE",
        "X-MICROSOFT-CDO-INSTTYPE",
        "X-MICROSOFT-CDO-APPT-SEQUENCE",
        "X-MICROSOFT-DISALLOW-COUNTER",
        "X-MICROSOFT-DONOTFORWARDMEETING",
        "X-MICROSOFT-ISRESPONSEREQUESTED",
        "X-MICROSOFT-REQUESTEDATTENDANCEMODE",
        "X-MS-OLK-FORCEINSPECTOROPEN",
        "X-MS-OLK-CONFTYPE",
        "X-MS-OLK-AUTOFILLLOCATION",
        "X-MS-OLK-ALLOWEXTERNCHECK",
        "X-MS-OLK-AUTOSTARTCHECK",
        "X-PUBLISHED-TTL",
        "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR",
        "X-APPLE-DEFAULT-ALARM",
        "X-APPLE-LOCAL-DEFAULT-ALARM",
        "X-APPLE-EWS-BUSYSTATUS",
        "X-APPLE-CALENDAR-COLOR",
        "X-CALENDARSERVER-ACCESS",
        "X-MOZ-GENERATION",
        "X-MOZ-RECEIVED-SEQUENCE",
        "X-MOZ-SEND-INVITATIONS",
        "X-MOZ-SEND-INVITATIONS-UNDISCLOSED",
        "X-MOZ-FAKED-MASTER",
    )

    val allowedDateExtensions: Set<String> = setOf(
        "X-MICROSOFT-CDO-OWNER-CRITICAL-CHANGE",
        "X-MICROSOFT-CDO-ATTENDEE-CRITICAL-CHANGE",
        "X-MOZ-LASTACK",
        "X-MOZ-SNOOZE-TIME",
        "X-MOZ-RECEIVED-DTSTAMP",
    )

    // Kept only when StructuralValues accepts the value.
    val structuralParameters: Set<String> = setOf(
        "VALUE", "RANGE", "RELATED", "ROLE", "PARTSTAT", "RSVP", "CUTYPE", "FBTYPE", "ENCODING", "FMTTYPE",
        "RELTYPE", "LANGUAGE", "DISPLAY", "FEATURE", "DERIVED", "ORDER", "GAP", "LINKREL", "SCHEDULE-AGENT",
        "SCHEDULE-STATUS", "SCHEDULE-FORCE-SEND", "SIZE",
    )

    private val addressParameters = setOf("SENT-BY", "DELEGATED-TO", "DELEGATED-FROM", "MEMBER", "EMAIL")
    private val urlParameters = setOf("ALTREP", "DIR", "SCHEMA")
    private val urlProperties = setOf("URL", "CONFERENCE", "TZURL", "SOURCE", "CONCEPT", "LINK")
    private val addressProperties = setOf("ORGANIZER", "ATTENDEE", "CALENDAR-ADDRESS")

    fun propertyRule(
        name: String,
        valueType: String?,
        encoding: String?,
        html: Boolean,
        inTimeZone: Boolean,
    ): ValueRule {
        // Inside VTIMEZONE the same rules hold: its dates and rules are checked and not shifted, and a COMMENT or an
        // X- property there is text like anywhere else.
        if (name in zoneProperties) return ValueRule.Zone
        val binary = valueType == "BINARY" || encoding == "BASE64"
        return when (name) {
            in structuralProperties -> ValueRule.Keep

            in dateProperties -> ValueRule.Date

            "RRULE", "EXRULE" -> ValueRule.Recurrence

            "TRIGGER" -> if (valueType == "DATE-TIME") ValueRule.Date else ValueRule.Keep

            "UID", "RELATED-TO", "X-WR-ALARMUID" -> ValueRule.Uid

            in addressProperties -> ValueRule.Address

            in urlProperties -> ValueRule.Url

            "ATTACH", "IMAGE" -> if (binary) ValueRule.Binary else ValueRule.Url

            "GEO" -> ValueRule.Geo

            "REQUEST-STATUS" -> ValueRule.RequestStatus

            in allowedExtensions -> ValueRule.Keep

            in allowedDateExtensions -> ValueRule.Date

            else -> when {
                binary -> ValueRule.Binary
                valueType == "URI" -> ValueRule.Url
                valueType == "CAL-ADDRESS" -> ValueRule.Address
                html -> ValueRule.Html
                else -> ValueRule.Text
            }
        }
    }

    fun parameterRule(name: String): ParameterRule = when {
        name == "TZID" -> ParameterRule.Zone
        name in structuralParameters -> ParameterRule.Keep
        name in addressParameters -> ParameterRule.Address
        name in urlParameters -> ParameterRule.Url
        else -> ParameterRule.Text
    }

    // Letters, digits, `-` and `_`, at most 64 characters: nothing that can name a host, a path or an address of a
    // person, so a URI without a scheme is kept only when it names a thing of the client.
    fun isBareName(value: String): Boolean {
        if (value.isEmpty() || value.length > MAX_TOKEN) return false
        for (char in value) {
            val allowed = char in 'A'..'Z' || char in 'a'..'z' || char in '0'..'9' || char == '-' || char == '_'
            if (!allowed) return false
        }
        return true
    }

    fun isMailto(value: String): Boolean = value.regionMatches(0, MAILTO, 0, MAILTO.length, ignoreCase = true)

    const val MAILTO: String = "mailto:"
    private const val MAX_TOKEN = 64
}

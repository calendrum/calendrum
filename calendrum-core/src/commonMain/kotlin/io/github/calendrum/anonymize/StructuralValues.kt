package io.github.calendrum.anonymize

import io.github.calendrum.tz.TimeZoneAliases

// What may stay in an anonymized calendar because it says nothing about a person: the values of the standard's own
// lists, numbers, durations, offsets, dates and rules, known zones and known producers. A value in a "structural" place
// that is not one of these is free text that happens to sit there, such as `CLASS:Zorgwin oncology visit`, and the
// session replaces it as text. The lists are explicit, so that a review can read what passes; a value of an
// extension (`X-…`) does not.
internal object StructuralValues {
    fun property(name: String, value: String): Boolean = when (name) {
        "VERSION" -> isVersion(value)
        "PRODID" -> isKnownProducer(value)
        "CALSCALE" -> value.equals("GREGORIAN", ignoreCase = true)
        "METHOD" -> upper(value) in METHODS
        "STATUS" -> upper(value) in STATUSES
        "CLASS" -> upper(value) in CLASSES
        "TRANSP" -> upper(value) in TRANSPARENCIES
        "ACTION" -> upper(value) in ACTIONS
        "BUSYTYPE" -> upper(value) in BUSY_TYPES
        "PROXIMITY" -> upper(value) in PROXIMITIES
        "PARTICIPANT-TYPE" -> upper(value) in PARTICIPANT_TYPES
        "RESOURCE-TYPE" -> upper(value) in RESOURCE_TYPES
        "LOCATION-TYPE" -> items(value).all { it.lowercase() in LOCATION_TYPES }
        "SEQUENCE", "PRIORITY", "REPEAT", "PERCENT-COMPLETE" -> isInteger(value)
        "DURATION", "REFRESH-INTERVAL", "TRIGGER" -> isDuration(value)
        "TZOFFSETFROM", "TZOFFSETTO" -> isUtcOffset(value)
        "TZNAME" -> isZoneAbbreviation(value)
        "COLOR" -> isColor(value)
        else -> false
    }

    fun parameter(name: String, value: String): Boolean = when (name) {
        "VALUE" -> upper(value) in VALUE_TYPES
        "ENCODING" -> upper(value) in ENCODINGS
        "RANGE" -> upper(value) in RANGES
        "RELATED" -> upper(value) in RELATED
        "ROLE" -> upper(value) in ROLES
        "PARTSTAT" -> upper(value) in PARTICIPATION
        "CUTYPE" -> upper(value) in USER_TYPES
        "FBTYPE" -> upper(value) in FREE_BUSY_TYPES
        "RELTYPE" -> upper(value) in RELATIONSHIPS
        "DISPLAY" -> upper(value) in DISPLAYS
        "FEATURE" -> upper(value) in FEATURES
        "SCHEDULE-AGENT" -> upper(value) in SCHEDULE_AGENTS
        "SCHEDULE-FORCE-SEND" -> upper(value) in FORCE_SEND
        "RSVP", "DERIVED" -> upper(value) in BOOLEANS
        "ORDER", "SIZE" -> isInteger(value)
        "GAP" -> isDuration(value)
        "FMTTYPE" -> isMediaType(value)
        "LANGUAGE" -> isLanguageTag(value)
        "LINKREL" -> value.lowercase() in LINK_RELATIONS
        "SCHEDULE-STATUS" -> items(value).all(::isStatusCode)
        else -> false
    }

    // The value of an allowed vendor property: a flag, a number, a duration, a date, a colour or one of the values the
    // vendors document for these properties (`BUSY`, `OOF`, `AUTOMATIC`). Any other word is text, in upper case too.
    fun extension(value: String): Boolean = upper(value) in BOOLEANS || isInteger(value) || isDuration(value) ||
        isDateList(value) || isHexColor(value) || upper(value) in VENDOR_TOKENS

    // An exact name of the zone database or of Windows. A match guessed from a city in free text is not one: the text
    // around the city would stay.
    fun knownZone(value: String): Boolean = TimeZoneAliases.resolve(value)?.guessed == false

    fun isDateList(value: String): Boolean = value.isNotEmpty() && items(value).all(::isDateOrPeriod)

    // A rule of RFC 5545 §3.3.10 and RFC 7529, part by part. Any other part, or a value outside the grammar of its
    // part, is text, as an `X-NOTE=` in an RRULE can say anything.
    fun isRule(value: String): Boolean {
        val body = if (value.startsWith("RRULE:", ignoreCase = true)) value.substring(RRULE_PREFIX) else value
        if (body.isEmpty()) return false
        for (part in body.split(';')) {
            val equals = part.indexOf('=')
            if (equals <= 0) return false
            val partValue = part.substring(equals + 1)
            val ok = when (upper(part.substring(0, equals))) {
                "FREQ" -> upper(partValue) in FREQUENCIES

                "UNTIL" -> isDate(partValue)

                "COUNT", "INTERVAL" -> isInteger(partValue)

                "BYSECOND", "BYMINUTE", "BYHOUR", "BYMONTHDAY", "BYYEARDAY", "BYWEEKNO", "BYSETPOS" ->
                    items(partValue).all(::isInteger)

                "BYMONTH" -> items(partValue).all { isInteger(it.removeSuffix("L").removeSuffix("l")) }

                "BYDAY" -> items(partValue).all(::isWeekdayNumber)

                "WKST" -> upper(partValue) in WEEKDAYS

                "RSCALE" -> partValue.lowercase() in CALENDARS

                "SKIP" -> upper(partValue) in SKIPS

                else -> false
            }
            if (!ok) return false
        }
        return true
    }

    private fun upper(value: String): String = value.trim().uppercase()

    private fun items(value: String): List<String> = value.split(',')

    private fun isInteger(value: String): Boolean {
        val digits = if (value.startsWith('+') || value.startsWith('-')) value.substring(1) else value
        return digits.length in 1..MAX_DIGITS && digits.all { it in '0'..'9' }
    }

    // [+|-]P followed by weeks, or days and a time part (RFC 5545 §3.3.6).
    private fun isDuration(value: String): Boolean {
        val body = if (value.startsWith('+') || value.startsWith('-')) value.substring(1) else value
        if (body.length < 3 || body.length > MAX_TOKEN || (body[0] != 'P' && body[0] != 'p')) return false
        var digits = 0
        var time = false
        for (index in 1 until body.length) {
            val char = body[index].uppercaseChar()
            when {
                char in '0'..'9' -> digits++
                char == 'T' && !time && digits == 0 -> time = true
                (char == 'W' || char == 'D') && !time && digits > 0 -> digits = 0
                (char == 'H' || char == 'M' || char == 'S') && time && digits > 0 -> digits = 0
                else -> return false
            }
        }
        return digits == 0
    }

    private fun isUtcOffset(value: String): Boolean =
        (value.length == OFFSET_LENGTH || value.length == OFFSET_SECONDS_LENGTH) &&
            (value[0] == '+' || value[0] == '-') && value.substring(1).all { it in '0'..'9' }

    // An abbreviation such as CET, EEST or MSK, which the zone database writes in upper case and in at most five
    // letters, or a numeric one such as +03, -0330 or GMT+3. A longer word or one in mixed case is a name and text.
    private fun isZoneAbbreviation(value: String): Boolean {
        if (value.length in 2..ABBREVIATION && value.all { it in 'A'..'Z' }) return true
        val numeric = when {
            value.startsWith("GMT", ignoreCase = true) || value.startsWith("UTC", ignoreCase = true) ->
                value.substring(3)

            else -> value
        }
        if (numeric.isEmpty() || (numeric[0] != '+' && numeric[0] != '-')) return false
        val digits = numeric.substring(1).replace(":", "")
        return digits.length in 1..4 && digits.all { it in '0'..'9' }
    }

    private fun isColor(value: String): Boolean = value.lowercase() in CSS_COLORS || isHexColor(value)

    private fun isHexColor(value: String): Boolean = value.length in HEX_COLOR_LENGTHS && value[0] == '#' &&
        value.substring(1).all { it in '0'..'9' || it in 'a'..'f' || it in 'A'..'F' }

    private fun isVersion(value: String): Boolean = value.isNotEmpty() && value.length <= MAX_TOKEN &&
        value.split(';').all { part ->
            part.isNotEmpty() && part.split('.').all { it.isNotEmpty() && it.all { c -> c in '0'..'9' } }
        }

    // YYYYMMDD or YYYYMMDDTHHMMSS[Z]: the form, and not the calendar, which the shifter checks.
    private fun isDate(value: String): Boolean {
        if (value.length != DATE_LENGTH && value.length != DATE_TIME_LENGTH && value.length != UTC_LENGTH) return false
        for (index in value.indices) {
            val char = value[index]
            val ok = when {
                index == DATE_LENGTH -> char == 'T'
                index == UTC_LENGTH - 1 -> char == 'Z'
                else -> char in '0'..'9'
            }
            if (!ok) return false
        }
        return true
    }

    private fun isDateOrPeriod(value: String): Boolean {
        val slash = value.indexOf('/')
        if (slash < 0) return isDate(value)
        val end = value.substring(slash + 1)
        return isDate(value.substring(0, slash)) && (isDate(end) || isDuration(end))
    }

    private fun isWeekdayNumber(value: String): Boolean {
        if (value.length < 2) return false
        val day = upper(value.substring(value.length - 2))
        val number = value.substring(0, value.length - 2)
        return day in WEEKDAYS && (number.isEmpty() || isInteger(number))
    }

    private fun isStatusCode(value: String): Boolean {
        val parts = value.split('.')
        return parts.size in 2..3 && parts.all { it.length in 1..2 && it.all { c -> c in '0'..'9' } }
    }

    // A media type of the list: a subtype is open (`vnd.<vendor>.<anything>`), so one that is not listed is text.
    private fun isMediaType(value: String): Boolean = value.lowercase() in MEDIA_TYPES

    // A tag of BCP 47 of a language, a script of the list and a region (`de-CH`, `zh-Hant-TW`, `es-419`). A variant or
    // an extension subtag can be any word of up to eight characters (`de-zorgwin` is well formed), so it makes the
    // value text.
    private fun isLanguageTag(value: String): Boolean {
        val parts = value.split('-')
        if (parts.size > LANGUAGE_PARTS || !isLetters(parts[0], 2..3)) return false
        var next = 1
        if (next < parts.size && parts[next].length == SCRIPT_LENGTH) {
            if (parts[next].lowercase() !in SCRIPTS) return false
            next++
        }
        if (next < parts.size) {
            val region = parts[next]
            val digits = region.length == REGION_DIGITS && region.all { it in '0'..'9' }
            if (!isLetters(region, 2..2) && !digits) return false
            next++
        }
        return next == parts.size
    }

    private fun isLetters(value: String, lengths: IntRange): Boolean =
        value.length in lengths && value.all { it in 'a'..'z' || it in 'A'..'Z' }

    // The producers whose PRODID the fixups read (`Fixups.byProdId`) and the common clients and servers: their names
    // are public, and a corpus needs them to tell which repairs a file needs. The value stays when it names one of them
    // and each of its other words is a word of a product name (`Inc`, `Calendar`, `NONSGML`); a number or a word of one
    // or two characters (`EN`, `V1`, `OS`) says nothing. Any other word, or a character outside printable ASCII, makes
    // it text: `-//Zorgwin Clinic//Teams planner//EN` names a clinic, and a match inside a word (`zoom` in `Zoomorph`)
    // is no producer.
    private fun isKnownProducer(value: String): Boolean {
        if (value.length > MAX_PRODUCER) return false
        var known = false
        var start = -1
        for (index in 0..value.length) {
            val char = if (index < value.length) value[index] else ' '
            if (char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9') {
                if (start < 0) start = index
                continue
            }
            if (char < ' ' || char > '~') return false
            if (start >= 0 && index - start > 2) {
                val word = value.substring(start, index).lowercase()
                when {
                    word.all { it in '0'..'9' } -> Unit
                    word in PRODUCERS -> known = true
                    word !in PRODUCT_WORDS -> return false
                }
            }
            start = -1
        }
        return known
    }

    private const val RRULE_PREFIX = 6
    private const val MAX_PRODUCER = 200
    private const val MAX_DIGITS = 9
    private const val MAX_TOKEN = 64
    private const val ABBREVIATION = 5
    private const val OFFSET_LENGTH = 5
    private const val OFFSET_SECONDS_LENGTH = 7
    private const val LANGUAGE_PARTS = 3
    private const val SCRIPT_LENGTH = 4
    private const val REGION_DIGITS = 3
    private const val DATE_LENGTH = 8
    private const val DATE_TIME_LENGTH = 15
    private const val UTC_LENGTH = 16
    private val HEX_COLOR_LENGTHS = setOf(4, 5, 7, 9)

    private val METHODS = setOf("PUBLISH", "REQUEST", "REPLY", "ADD", "CANCEL", "REFRESH", "COUNTER", "DECLINECOUNTER")
    private val STATUSES = setOf(
        "TENTATIVE",
        "CONFIRMED",
        "CANCELLED",
        "NEEDS-ACTION",
        "COMPLETED",
        "IN-PROCESS",
        "DRAFT",
        "FINAL",
    )
    private val CLASSES = setOf("PUBLIC", "PRIVATE", "CONFIDENTIAL")
    private val TRANSPARENCIES = setOf("OPAQUE", "TRANSPARENT")
    private val ACTIONS = setOf("AUDIO", "DISPLAY", "EMAIL", "PROCEDURE", "URI", "NONE")
    private val BUSY_TYPES = setOf("BUSY", "BUSY-UNAVAILABLE", "BUSY-TENTATIVE")
    private val PROXIMITIES = setOf("ARRIVE", "DEPART", "CONNECT", "DISCONNECT")
    private val PARTICIPANT_TYPES = setOf(
        "ACTIVE", "INACTIVE", "SPONSOR", "CONTACT", "BOOKING-CONTACT", "EMERGENCY-CONTACT", "PUBLICITY-CONTACT",
        "PLANNER-CONTACT", "PERFORMER", "SPEAKER",
    )
    private val RESOURCE_TYPES = setOf("ROOM", "PROJECTOR", "REMOTE-CONFERENCE-AUDIO", "REMOTE-CONFERENCE-VIDEO")

    // RFC 4589, the location types that RFC 9073 refers to.
    private val LOCATION_TYPES = setOf(
        "aircraft", "airport", "arena", "automobile", "bank", "bar", "bicycle", "bus", "bus-station", "cafe",
        "classroom", "club", "construction", "convention-center", "government", "hospital", "hotel", "industrial",
        "library", "motorcycle", "office", "other", "outdoors", "parking", "place-of-worship", "prison", "public",
        "public-transport", "residence", "restaurant", "school", "shopping-area", "stadium", "store", "street",
        "theater", "train", "train-station", "truck", "underway", "unknown", "warehouse", "water", "watercraft",
    )
    private val VALUE_TYPES = setOf(
        "BINARY", "BOOLEAN", "CAL-ADDRESS", "DATE", "DATE-TIME", "DURATION", "FLOAT", "INTEGER", "PERIOD", "RECUR",
        "TEXT", "TIME", "URI", "UTC-OFFSET", "XML-REFERENCE", "UID",
    )
    private val ENCODINGS = setOf("8BIT", "BASE64", "QUOTED-PRINTABLE")
    private val RANGES = setOf("THISANDFUTURE", "THISANDPRIOR")
    private val RELATED = setOf("START", "END")
    private val ROLES = setOf("CHAIR", "REQ-PARTICIPANT", "OPT-PARTICIPANT", "NON-PARTICIPANT")
    private val PARTICIPATION = setOf(
        "NEEDS-ACTION",
        "ACCEPTED",
        "DECLINED",
        "TENTATIVE",
        "DELEGATED",
        "COMPLETED",
        "IN-PROCESS",
    )
    private val USER_TYPES = setOf("INDIVIDUAL", "GROUP", "RESOURCE", "ROOM", "UNKNOWN")
    private val FREE_BUSY_TYPES = setOf("FREE", "BUSY", "BUSY-UNAVAILABLE", "BUSY-TENTATIVE")
    private val RELATIONSHIPS = setOf(
        "PARENT", "CHILD", "SIBLING", "SNOOZE", "FINISHTOSTART", "FINISHTOFINISH", "STARTTOFINISH", "STARTTOSTART",
        "FIRST", "NEXT", "DEPENDS-ON", "REFID", "CONCEPT",
    )
    private val DISPLAYS = setOf("BADGE", "GRAPHIC", "FULLSIZE", "THUMBNAIL")
    private val FEATURES = setOf("AUDIO", "CHAT", "FEED", "MODERATOR", "PHONE", "SCREEN", "VIDEO")
    private val SCHEDULE_AGENTS = setOf("SERVER", "CLIENT", "NONE")
    private val FORCE_SEND = setOf("REQUEST", "REPLY")
    private val BOOLEANS = setOf("TRUE", "FALSE")
    private val FREQUENCIES = setOf("SECONDLY", "MINUTELY", "HOURLY", "DAILY", "WEEKLY", "MONTHLY", "YEARLY")
    private val WEEKDAYS = setOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")
    private val SKIPS = setOf("OMIT", "BACKWARD", "FORWARD")
    private val CALENDARS = setOf(
        "gregorian", "chinese", "coptic", "dangi", "ethioaa", "ethiopic", "hebrew", "indian", "islamic",
        "islamic-civil", "islamic-rgsa", "islamic-tbla", "islamic-umalqura", "iso8601", "japanese", "persian", "roc",
        "buddhist",
    )
    private val MEDIA_TYPES = setOf(
        "text/plain", "text/html", "text/calendar", "text/csv", "text/xml", "text/markdown", "text/rtf", "text/vcard",
        "text/x-vcard", "text/x-vcalendar", "application/pdf", "application/json", "application/xml", "application/zip",
        "application/octet-stream", "application/binary", "application/rtf", "application/msword", "application/ics",
        "application/calendar+json", "application/calendar+xml", "application/vnd.ms-excel",
        "application/vnd.ms-powerpoint", "application/vnd.ms-outlook",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "application/vnd.oasis.opendocument.text", "application/vnd.oasis.opendocument.spreadsheet",
        "application/vnd.oasis.opendocument.presentation", "application/vnd.apple.pages",
        "application/vnd.apple.numbers", "application/vnd.apple.keynote", "application/vnd.google-apps.document",
        "application/vnd.google-apps.spreadsheet", "application/vnd.google-apps.presentation",
        "application/vnd.google-apps.file", "image/png", "image/jpeg", "image/jpg", "image/gif", "image/webp",
        "image/svg+xml", "image/bmp", "image/tiff", "image/heic", "audio/basic", "audio/mpeg", "audio/mp4", "audio/wav",
        "audio/x-wav", "audio/ogg", "audio/aac", "video/mp4", "video/mpeg", "video/quicktime", "video/webm",
        "message/rfc822",
    )

    // ISO 15924, the scripts that language tags of calendars use.
    private val SCRIPTS = setOf(
        "latn", "cyrl", "grek", "armn", "geor", "hebr", "arab", "syrc", "thaa", "deva", "beng", "guru", "gujr", "orya",
        "taml", "telu", "knda", "mlym", "sinh", "thai", "laoo", "tibt", "mymr", "khmr", "ethi", "mong", "hans", "hant",
        "hani", "jpan", "hira", "kana", "kore", "hang",
    )

    // The values that Microsoft, Apple and the calendar server document for the allowed vendor properties.
    private val VENDOR_TOKENS = setOf(
        "FREE", "TENTATIVE", "BUSY", "OOF", "WORKINGELSEWHERE", "DEFAULT", "INPERSON", "ONLINE", "AUTOMATIC", "ENABLED",
        "DISABLED", "PUBLIC", "PRIVATE", "CONFIDENTIAL", "RESTRICTED", "NONE",
    )
    private val LINK_RELATIONS = setOf(
        "alternate", "appendix", "author", "bookmark", "canonical", "chapter", "contents", "copyright", "describedby",
        "describes", "edit", "enclosure", "glossary", "help", "icon", "index", "latest-version", "license", "next",
        "prev", "preview", "related", "replies", "search", "section", "self", "start", "subsection", "up", "via",
        "working-copy", "working-copy-of",
    )
    private val PRODUCERS = setOf(
        "microsoft", "outlook", "exchange", "office", "office365", "google", "apple", "icloud", "macos", "ios",
        "mozilla", "thunderbird", "lightning", "yahoo", "yandex", "lotus", "domino", "notes", "zimbra", "nextcloud",
        "owncloud", "sabre", "fastmail", "zoho", "proton", "kerio", "kopano", "egroupware", "horde", "davmail",
        "radicale", "baikal", "evolution", "ximian", "korganizer", "kde", "gnome", "calendrum", "ical4j", "biweekly",
        "libical", "facebook", "meetup", "eventbrite", "calendly", "zoom", "webex", "cisco", "teams", "samsung",
        "huawei", "xiaomi", "android",
    )

    // The other words of the PRODID of a known producer: forms of a company, of a product, of a platform.
    private val PRODUCT_WORDS = setOf(
        "inc", "corp", "corporation", "company", "llc", "ltd", "gmbh", "org", "com", "net", "www", "github", "calendar",
        "calendars", "ical", "icalendar", "vcalendar", "ics", "caldav", "webdav", "server", "client", "web", "mobile",
        "app", "desktop", "environment", "event", "events", "export", "exporter", "feed", "nonsgml", "mimedir",
        "vobject", "version", "build", "beta", "release", "online", "live", "mail", "suite", "groupware",
        "collaboration", "connector", "sync", "video", "communications", "meetings", "workspace", "windows", "linux",
        "computer", "mac", "iphone", "ipad", "ipados", "watchos", "systems", "software", "technologies", "plus", "pro",
        "for", "the", "and", "cloud", "service", "services", "platform", "studio",
    )

    // The colour names of CSS3, which RFC 7986 §5.9 refers to.
    private val CSS_COLORS = setOf(
        "aliceblue", "antiquewhite", "aqua", "aquamarine", "azure", "beige", "bisque", "black", "blanchedalmond",
        "blue", "blueviolet", "brown", "burlywood", "cadetblue", "chartreuse", "chocolate", "coral", "cornflowerblue",
        "cornsilk", "crimson", "cyan", "darkblue", "darkcyan", "darkgoldenrod", "darkgray", "darkgreen", "darkgrey",
        "darkkhaki", "darkmagenta", "darkolivegreen", "darkorange", "darkorchid", "darkred", "darksalmon",
        "darkseagreen", "darkslateblue", "darkslategray", "darkslategrey", "darkturquoise", "darkviolet", "deeppink",
        "deepskyblue", "dimgray", "dimgrey", "dodgerblue", "firebrick", "floralwhite", "forestgreen", "fuchsia",
        "gainsboro", "ghostwhite", "gold", "goldenrod", "gray", "green", "greenyellow", "grey", "honeydew", "hotpink",
        "indianred", "indigo", "ivory", "khaki", "lavender", "lavenderblush", "lawngreen", "lemonchiffon", "lightblue",
        "lightcoral", "lightcyan", "lightgoldenrodyellow", "lightgray", "lightgreen", "lightgrey", "lightpink",
        "lightsalmon", "lightseagreen", "lightskyblue", "lightslategray", "lightslategrey", "lightsteelblue",
        "lightyellow", "lime", "limegreen", "linen", "magenta", "maroon", "mediumaquamarine", "mediumblue",
        "mediumorchid", "mediumpurple", "mediumseagreen", "mediumslateblue", "mediumspringgreen", "mediumturquoise",
        "mediumvioletred", "midnightblue", "mintcream", "mistyrose", "moccasin", "navajowhite", "navy", "oldlace",
        "olive", "olivedrab", "orange", "orangered", "orchid", "palegoldenrod", "palegreen", "paleturquoise",
        "palevioletred", "papayawhip", "peachpuff", "peru", "pink", "plum", "powderblue", "purple", "rebeccapurple",
        "red", "rosybrown", "royalblue", "saddlebrown", "salmon", "sandybrown", "seagreen", "seashell", "sienna",
        "silver", "skyblue", "slateblue", "slategray", "slategrey", "snow", "springgreen", "steelblue", "tan", "teal",
        "thistle", "tomato", "turquoise", "violet", "wheat", "white", "whitesmoke", "yellow", "yellowgreen",
    )
}

package io.github.calendrum.syntax

internal object KnownNames {
    const val UNKNOWN: Int = -1

    const val COMPONENT: Int = 1
    const val PROPERTY: Int = 2
    const val PARAMETER: Int = 4

    const val BEGIN: Int = 0
    const val END: Int = 1
    const val VCALENDAR: Int = 70
    const val VTIMEZONE: Int = 75

    private const val TABLE_SIZE: Int = 1024
    private const val TABLE_MASK: Int = TABLE_SIZE - 1

    private const val PROPERTIES: String =
        "BEGIN END " +
            "CALSCALE METHOD PRODID VERSION " +
            "ATTACH CATEGORIES CLASS COMMENT DESCRIPTION GEO LOCATION PERCENT-COMPLETE PRIORITY RESOURCES " +
            "STATUS SUMMARY " +
            "COMPLETED DTEND DUE DTSTART DURATION FREEBUSY TRANSP " +
            "TZID TZNAME TZOFFSETFROM TZOFFSETTO TZURL " +
            "ATTENDEE CONTACT ORGANIZER RECURRENCE-ID RELATED-TO URL UID " +
            "EXDATE RDATE RRULE " +
            "ACTION REPEAT TRIGGER " +
            "CREATED DTSTAMP LAST-MODIFIED SEQUENCE " +
            "REQUEST-STATUS " +
            "EXRULE " +
            "NAME REFRESH-INTERVAL SOURCE COLOR IMAGE CONFERENCE " +
            "BUSYTYPE " +
            "LOCATION-TYPE PARTICIPANT-TYPE RESOURCE-TYPE CALENDAR-ADDRESS STYLED-DESCRIPTION STRUCTURED-DATA " +
            "ACKNOWLEDGED PROXIMITY " +
            "CONCEPT LINK REFID " +
            "X-WR-CALNAME X-WR-CALDESC X-WR-TIMEZONE"

    private const val COMPONENTS: String =
        "VCALENDAR VEVENT VTODO VJOURNAL VFREEBUSY VTIMEZONE STANDARD DAYLIGHT VALARM " +
            "VAVAILABILITY AVAILABLE " +
            "PARTICIPANT VLOCATION VRESOURCE"

    private const val PARAMETERS: String =
        "ALTREP CN CUTYPE DELEGATED-FROM DELEGATED-TO DIR ENCODING FMTTYPE FBTYPE LANGUAGE MEMBER PARTSTAT RANGE " +
            "RELATED RELTYPE ROLE RSVP SENT-BY TZID VALUE " +
            "DISPLAY EMAIL FEATURE LABEL " +
            "SCHEDULE-AGENT SCHEDULE-FORCE-SEND SCHEDULE-STATUS " +
            "ORDER SCHEMA DERIVED " +
            "LINKREL GAP"

    val count: Int
    val maxLength: Int
    val longestProbe: Int

    private val names: Array<String>
    private val kinds: IntArray
    private val packed: ByteArray
    private val packedLower: ByteArray
    private val starts: IntArray
    private val table: IntArray

    init {
        val groups = arrayOf(PROPERTIES, COMPONENTS, PARAMETERS)
        val groupKinds = intArrayOf(PROPERTY, COMPONENT, PARAMETER)
        val capacity = (PROPERTIES.length + COMPONENTS.length + PARAMETERS.length) / 2 + groups.size
        val found = arrayOfNulls<String>(capacity)
        val foundKinds = IntArray(capacity)
        var added = 0
        for (group in groups.indices) {
            val text = groups[group]
            var start = 0
            while (start < text.length) {
                var end = start
                while (end < text.length && text[end] != ' ') end++
                val name = text.substring(start, end)
                var id = 0
                while (id < added && found[id] != name) id++
                if (id == added) found[added++] = name
                foundKinds[id] = foundKinds[id] or groupKinds[group]
                start = end + 1
            }
        }
        count = added
        names = Array(count) { found[it]!! }
        kinds = foundKinds.copyOf(count)
        starts = IntArray(count + 1)
        var total = 0
        var longest = 0
        for (id in 0 until count) {
            starts[id] = total
            total += names[id].length
            if (names[id].length > longest) longest = names[id].length
        }
        starts[count] = total
        maxLength = longest
        packed = ByteArray(total)
        packedLower = ByteArray(total)
        for (id in 0 until count) {
            val name = names[id]
            for (k in 0 until name.length) {
                val code = name[k].code
                packed[starts[id] + k] = code.toByte()
                packedLower[starts[id] + k] = (if (code in UPPER_A..UPPER_Z) code + CASE_OFFSET else code).toByte()
            }
        }
        table = IntArray(TABLE_SIZE) { UNKNOWN }
        var probes = 0
        for (id in 0 until count) {
            var slot = slotOf(packed, starts[id], starts[id + 1] - starts[id])
            var probe = 1
            while (table[slot] != UNKNOWN) {
                slot = (slot + 1) and TABLE_MASK
                probe++
            }
            table[slot] = id
            if (probe > probes) probes = probe
        }
        longestProbe = probes
        check(names[BEGIN] == "BEGIN" && names[END] == "END" && names[VCALENDAR] == "VCALENDAR")
    }

    fun find(bytes: ByteArray, from: Int, to: Int): Int {
        val length = to - from
        if (length <= 0 || length > maxLength) return UNKNOWN
        var slot = slotOf(bytes, from, length)
        while (true) {
            val id = table[slot]
            if (id == UNKNOWN || matches(id, bytes, from, length)) return id
            slot = (slot + 1) and TABLE_MASK
        }
    }

    fun find(name: CharSequence): Int {
        val length = name.length
        if (length <= 0 || length > maxLength) return UNKNOWN
        for (index in 0 until length) {
            if (name[index].code >= ASCII_LIMIT) return UNKNOWN
        }
        var slot = slotOf(length, name[0].code, name[length ushr 1].code, name[length - 1].code)
        while (true) {
            val id = table[slot]
            if (id == UNKNOWN || matches(id, name)) return id
            slot = (slot + 1) and TABLE_MASK
        }
    }

    fun lookup(name: String): Int = find(name)

    fun name(id: Int): String = names[id]

    fun kinds(id: Int): Int = kinds[id]

    private fun slotOf(bytes: ByteArray, from: Int, length: Int): Int =
        slotOf(length, bytes[from].toInt(), bytes[from + (length ushr 1)].toInt(), bytes[from + length - 1].toInt())

    private fun slotOf(length: Int, firstCode: Int, middleCode: Int, lastCode: Int): Int {
        val first = firstCode and CASE_MASK
        val middle = middleCode and CASE_MASK
        val last = lastCode and CASE_MASK
        val hash = (length * MIX_LENGTH) xor (first * MIX_FIRST) xor (middle * MIX_MIDDLE) xor (last * MIX_LAST)
        return (hash xor (hash ushr 16)) and TABLE_MASK
    }

    private fun matches(id: Int, name: CharSequence): Boolean {
        val start = starts[id]
        val length = name.length
        if (starts[id + 1] - start != length) return false
        for (k in 0 until length) {
            val code = name[k].code
            if (code != packed[start + k].toInt() && code != packedLower[start + k].toInt()) return false
        }
        return true
    }

    fun isName(id: Int, bytes: ByteArray, from: Int, to: Int): Boolean = matches(id, bytes, from, to - from)

    private fun matches(id: Int, bytes: ByteArray, from: Int, length: Int): Boolean {
        val start = starts[id]
        if (starts[id + 1] - start != length) return false
        val upper = packed
        val lower = packedLower
        for (k in 0 until length) {
            val byte = bytes[from + k]
            if (byte != upper[start + k] && byte != lower[start + k]) return false
        }
        return true
    }

    private const val UPPER_A: Int = 0x41
    private const val UPPER_Z: Int = 0x5A
    private const val CASE_OFFSET: Int = 0x20
    private const val CASE_MASK: Int = 0xDF
    private const val ASCII_LIMIT: Int = 0x80
    private const val MIX_LENGTH: Int = -0x61c88647
    private const val MIX_FIRST: Int = -0x7a143595
    private const val MIX_MIDDLE: Int = -0x3d4d51cb
    private const val MIX_LAST: Int = 0x27d4eb2f
}

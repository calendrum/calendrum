package io.github.calendrum.fixup

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.model.edit
import io.github.calendrum.recur.dayOf
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.InterceptedLine
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.controlIndex
import io.github.calendrum.syntax.isName
import io.github.calendrum.syntax.isWritableParameterValue
import io.github.calendrum.validate.ElementPath

// An edited line is written anew, so a line that canonical writing would refuse (a name that is not a token, a control
// character, a parameter without a value: parsing keeps them) is left alone and written byte for byte, and so
// is a quoted-printable value that decodes to a control character; `setValue` would throw on it.
@OptIn(ExperimentalICalApi::class)
internal object VCalendar10Lines : LineInterceptor {
    override fun intercept(line: InterceptedLine) {
        if (!isWritable(line)) return
        if (line.name == "DCREATED") line.rename("CREATED")
        val encoding = parameter(line, "ENCODING")
        if (encoding != null && encoding.equals("QUOTED-PRINTABLE", ignoreCase = true)) {
            val bytes = quotedPrintable(line.value)
            val charset = parameter(line, "CHARSET")
            val text = if (charset != null && isLatin(charset)) latin(bytes) else bytes.decodeToString()
            val value = if (line.name in TEXT_PROPERTIES) escapeText(text) else text
            if (controlIndex(value) >= 0) return
            line.setValue(value)
            line.removeParameter("ENCODING")
            line.removeParameter("CHARSET")
        } else if (parameter(line, "CHARSET") != null) {
            line.removeParameter("CHARSET")
        }
        when (line.name) {
            "ATTENDEE", "ORGANIZER" -> attendee(line)
            "STATUS" -> if (isNeedsAction(line.value.toString())) line.setValue("NEEDS-ACTION")
        }
    }

    private fun attendee(line: InterceptedLine) {
        val value = line.value.toString().trim()
        val address = value.substringAfter('<', value).substringBefore('>').trim()
        if (!address.contains(':') && address.contains('@')) line.setValue("mailto:$address")
        parameter(line, "STATUS")?.let { status ->
            line.removeParameter("STATUS")
            line.setParameter("PARTSTAT", if (isNeedsAction(status)) "NEEDS-ACTION" else status.uppercase())
        }
        parameter(line, "RSVP")?.let { rsvp ->
            when (rsvp.uppercase()) {
                "YES" -> line.setParameter("RSVP", "TRUE")
                "NO" -> line.setParameter("RSVP", "FALSE")
            }
        }
        val expect = parameter(line, "EXPECT")?.uppercase()
        val role = parameter(line, "ROLE")?.uppercase()
        if (expect != null) line.removeParameter("EXPECT")
        val mapped = when {
            role == "OWNER" || role == "ORGANIZER" -> "CHAIR"
            expect == "REQUEST" -> "OPT-PARTICIPANT"
            expect == "FYI" -> "NON-PARTICIPANT"
            role == "ATTENDEE" || role == "DELEGATE" -> "REQ-PARTICIPANT"
            expect == "REQUIRE" || expect == "IMMEDIATE" -> "REQ-PARTICIPANT"
            else -> null
        }
        if (mapped != null) line.setParameter("ROLE", mapped)
    }

    override fun toString(): String = "VCalendar10Lines"
}

private fun isWritable(line: InterceptedLine): Boolean {
    if (!isName(line.name) || controlIndex(line.value.toString()) >= 0) return false
    for (index in 0 until line.parameterCount) {
        val values = line.parameterValues(index)
        if (!isName(line.parameterName(index)) || values.isEmpty() ||
            !values.all(::isWritableParameterValue)
        ) {
            return false
        }
    }
    return true
}

private fun parameter(line: InterceptedLine, name: String): String? {
    for (index in 0 until line.parameterCount) {
        if (line.parameterName(index) == name) return line.parameterValues(index).firstOrNull()
    }
    return null
}

private fun isLatin(charset: String): Boolean {
    val name = charset.uppercase()
    return name == "ISO-8859-1" || name == "LATIN1" || name == "WINDOWS-1252" || name == "US-ASCII"
}

private fun isNeedsAction(value: String): Boolean = value.equals("NEEDS ACTION", ignoreCase = true)

private fun latin(bytes: ByteArray): String = buildString {
    for (byte in bytes) append((byte.toInt() and 0xFF).toChar())
}

internal fun quotedPrintable(text: CharSequence): ByteArray {
    val out = ArrayList<Byte>(text.length)
    var index = 0
    while (index < text.length) {
        val char = text[index]
        if (char == '=' && index + 2 < text.length) {
            val high = text[index + 1].digitToIntOrNull(16)
            val low = text[index + 2].digitToIntOrNull(16)
            if (high != null && low != null) {
                out += (high * 16 + low).toByte()
                index += 3
                continue
            }
        }
        if (char == '=' && index == text.length - 1) break
        val encoded = char.toString().encodeToByteArray()
        for (byte in encoded) out += byte
        index += 1
    }
    return out.toByteArray()
}

private fun escapeText(text: String): String = buildString {
    var index = 0
    while (index < text.length) {
        val char = text[index]
        when {
            char == '\\' -> append("\\\\")

            char == ';' -> append("\\;")

            char == ',' -> append("\\,")

            char == '\r' && index + 1 < text.length && text[index + 1] == '\n' -> {
                append("\\n")
                index += 1
            }

            char == '\n' || char == '\r' -> append("\\n")

            else -> append(char)
        }
        index += 1
    }
}

internal fun vcalendar10(root: ICalComponent, changes: Changes): ICalComponent {
    if (root.property("VERSION")?.value?.trim() != "1.0") return root
    val versioned = root.mapProperties(ROOT_PATH, changes) { property ->
        if (property.name == "VERSION") property.copy(value = "2.0") else property
    }
    return rewrite(versioned, ROOT_PATH) { component, path ->
        if (component.name == "VCALENDAR") return@rewrite component
        val rules = component.mapProperties(path, changes) { property ->
            when (property.name) {
                "RRULE", "EXRULE" -> vcalRule(property.value)?.let { property.copy(value = it) } ?: property
                else -> property
            }
        }
        alarms(rules, path, changes)
    }
}

private fun alarms(component: ICalComponent, path: ElementPath, changes: Changes): ICalComponent {
    val old = component.properties.filter { it.name == "AALARM" || it.name == "DALARM" }
    if (old.isEmpty()) return component
    val start = component.property("DTSTART")?.value
    val alarms = old.mapNotNull { property -> alarmOf(property, start) }
    val removed = component.mapProperties(path, changes) { property ->
        if (property.name == "AALARM" || property.name == "DALARM") null else property
    }
    return removed.edit { for (alarm in alarms) addChild(alarm) }
}

// AALARM and DALARM become VALARMs: an absolute UTC trigger, or a trigger relative to a floating DTSTART; snooze and
// repeat become DURATION and REPEAT.
private fun alarmOf(property: ICalProperty, start: String?): ICalComponent? {
    if (controlIndex(property.value) >= 0) return null
    val parts = property.value.split(';')
    val runTime = parts.getOrNull(0)?.trim().orEmpty()
    if (runTime.isEmpty()) return null
    val builder = ICalComponent.builder("VALARM")
    val audio = property.name == "AALARM"
    builder.property("ACTION", if (audio) "AUDIO" else "DISPLAY")
    when {
        runTime.endsWith("Z") ->
            builder.property(ICalProperty.builder("TRIGGER").parameter("VALUE", "DATE-TIME").value(runTime).build())

        start != null && !start.endsWith("Z") -> {
            val seconds = seconds(runTime) ?: return null
            val reference = seconds(start) ?: return null
            builder.property("TRIGGER", duration(seconds - reference))
        }

        else -> return null
    }
    val snooze = parts.getOrNull(1)?.trim().orEmpty()
    val repeat = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: 0
    if (snooze.isNotEmpty() && repeat > 0) {
        builder.property("DURATION", snooze)
        builder.property("REPEAT", repeat.toString())
    }
    val text = parts.getOrNull(3)?.trim().orEmpty()
    if (!audio) builder.property("DESCRIPTION", text.ifEmpty { "Reminder" })
    return builder.build()
}

private fun seconds(value: String): Long? {
    if (value.length < 15 || value[8] != 'T') return null
    val year = value.substring(0, 4).toIntOrNull() ?: return null
    val month = value.substring(4, 6).toIntOrNull() ?: return null
    val day = value.substring(6, 8).toIntOrNull() ?: return null
    val hour = value.substring(9, 11).toIntOrNull() ?: return null
    val minute = value.substring(11, 13).toIntOrNull() ?: return null
    val second = value.substring(13, 15).toIntOrNull() ?: return null
    return dayOf(year, month, day) * 86_400L + hour * 3_600L + minute * 60L + second
}

private fun duration(total: Long): String {
    val sign = if (total < 0) "-" else ""
    var rest = if (total < 0) -total else total
    val days = rest / 86_400
    rest %= 86_400
    val text = buildString {
        append(sign).append('P')
        if (days > 0) append(days).append('D')
        if (rest > 0 || days == 0L) {
            append('T')
            val hours = rest / 3_600
            val minutes = rest / 60 % 60
            val seconds = rest % 60
            if (hours > 0) append(hours).append('H')
            if (minutes > 0) append(minutes).append('M')
            if (seconds > 0 || (hours == 0L && minutes == 0L)) append(seconds).append('S')
        }
    }
    return text
}

// vCalendar 1.0 rules: D, W, MP, MD, YM, YD with an interval, their lists and an end (#n, #0 forever, or a date; #2
// when absent). Anything else stays as it is.
internal fun vcalRule(text: String): String? {
    val tokens = text.trim().split(' ').filter { it.isNotEmpty() }
    if (tokens.isEmpty() || tokens[0].contains('=')) return null
    val head = tokens[0].uppercase()
    val kind = head.takeWhile { it.isLetter() }
    val interval = head.drop(kind.length).toIntOrNull() ?: return null
    val frequency = when (kind) {
        "D" -> "DAILY"
        "W" -> "WEEKLY"
        "MP", "MD" -> "MONTHLY"
        "YM", "YD" -> "YEARLY"
        else -> return null
    }
    var end = "#2"
    val modifiers = ArrayList<String>()
    for (token in tokens.drop(1)) {
        val isEnd = token.startsWith("#") || (token.length >= 8 && token[0].isDigit() && token.contains('T'))
        if (isEnd) end = token else modifiers += token
    }
    val parts = arrayListOf("FREQ=$frequency")
    if (interval != 1) parts += "INTERVAL=$interval"
    if (modifiers.isNotEmpty()) {
        parts += when (kind) {
            "D" -> return null
            "W" -> "BYDAY=" + (weekdays(modifiers) ?: return null)
            "MP" -> "BYDAY=" + (positions(modifiers) ?: return null)
            "MD" -> "BYMONTHDAY=" + (numbers(modifiers, allowLast = true) ?: return null)
            "YM" -> "BYMONTH=" + (numbers(modifiers, allowLast = false) ?: return null)
            else -> "BYYEARDAY=" + (numbers(modifiers, allowLast = false) ?: return null)
        }
    }
    when {
        end.startsWith("#") -> {
            val count = end.drop(1).toIntOrNull() ?: return null
            if (count > 0) parts += "COUNT=$count"
        }

        else -> parts += "UNTIL=$end"
    }
    return parts.joinToString(";")
}

private fun weekdays(tokens: List<String>): String? {
    val days = tokens.map { it.uppercase() }
    return if (days.all { it in WEEKDAYS }) days.joinToString(",") else null
}

private fun positions(tokens: List<String>): String? {
    val out = ArrayList<String>()
    var pending: Int? = null
    for (token in tokens.map { it.uppercase() }) {
        when {
            token in WEEKDAYS -> out += (pending?.toString() ?: "") + token

            token.length == 2 && token[0].isDigit() && (token[1] == '+' || token[1] == '-') ->
                pending = if (token[1] == '-') -token[0].digitToInt() else token[0].digitToInt()

            else -> return null
        }
    }
    return if (out.isEmpty()) null else out.joinToString(",")
}

private fun numbers(tokens: List<String>, allowLast: Boolean): String? {
    val out = ArrayList<String>()
    for (token in tokens.map { it.uppercase() }) {
        out += when {
            allowLast && token == "LD" -> "-1"
            token.endsWith("-") -> "-" + (token.dropLast(1).toIntOrNull() ?: return null)
            token.endsWith("+") -> (token.dropLast(1).toIntOrNull() ?: return null).toString()
            else -> (token.toIntOrNull() ?: return null).toString()
        }
    }
    return if (out.isEmpty()) null else out.joinToString(",")
}

private val WEEKDAYS = setOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")

package io.github.calendrum.tools

import io.github.calendrum.ICal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal val SAMPLE_DIRECTORY: Path = Paths.get(System.getProperty("calendrum.samples"))

internal val SAMPLES: List<Path> = Files.list(SAMPLE_DIRECTORY).use { stream ->
    stream.filter { it.fileName.toString().endsWith(".ics") }.sorted().toList()
}

internal fun sample(name: String): Path = SAMPLE_DIRECTORY.resolve(name)

// The same leak specification as the corpus test, for the inline samples (all platforms) and `tools/samples` (JVM).
// HTML values count the text and attribute values, not tag or attribute names or named entities.
internal fun sensitiveWords(source: ByteArray, output: ByteArray? = null): Set<String> {
    val words = HashSet<String>()
    val kept = StringBuilder(FIXED_WORDS.joinToString("\n"))
    if (output != null) {
        visit(ICal.parse(output).components) { name, value, sensitive, _ ->
            kept.append('\n').append(name)
            if (!sensitive) kept.append('\n').append(value)
        }
    }
    visit(ICal.parse(source).components) { name, value, sensitive, html ->
        kept.append('\n').append(name)
        when {
            !sensitive -> kept.append('\n').append(value)

            html -> {
                words += words(htmlText(unescape(value)))
                kept.append('\n').append(htmlMarkup(unescape(value)))
            }

            else -> words += words(unescape(value))
        }
    }
    val keptText = kept.toString().lowercase()
    return words.filterTo(HashSet()) { it.length >= MIN_WORD && it.lowercase() !in keptText }
}

internal fun sensitiveValues(source: ByteArray): Set<String> {
    val values = HashSet<String>()
    visit(ICal.parse(source).components) { name, value, sensitive, _ ->
        if (sensitive && name in IDENTIFIERS && value.length >= MIN_WORD) {
            values += value.lowercase().removePrefix("mailto:")
        }
    }
    return values
}

private fun visit(roots: List<ICalComponent>, visitor: (String, String, Boolean, Boolean) -> Unit) {
    val stack = ArrayList(roots.map { it to (it.name == "VTIMEZONE") })
    while (stack.isNotEmpty()) {
        val (component, inTimeZone) = stack.removeAt(stack.lastIndex)
        visitor(component.name, "", false, false)
        for (property in component.properties) {
            val html = property.parameter("FMTTYPE")?.value.equals("text/html", ignoreCase = true)
            val sensitive = if (inTimeZone) property.name == "TZURL" else property.name !in PRESERVED_PROPERTIES
            visitor(property.name, property.value, sensitive && !isNamedUri(property), html)
            for (parameter in property.parameters) {
                val sensitiveParameter = !inTimeZone && parameter.name !in PRESERVED_PARAMETERS
                for (value in parameter.values) {
                    val namedUri = parameter.name in URI_PARAMETERS && isName(value)
                    visitor(parameter.name, value, sensitiveParameter && !namedUri, false)
                }
            }
        }
        for (child in component.components) stack += child to (inTimeZone || child.name == "VTIMEZONE")
    }
}

// A URI value without a scheme that is a bare name (the alarm sound `ATTACH;VALUE=URI:Chord` of Apple) is kept by
// design and is not sensitive. The corpus test has no such values.
private fun isNamedUri(property: ICalProperty): Boolean {
    val uri = property.parameter("VALUE")?.value.equals("URI", ignoreCase = true) ||
        (property.name in URI_PROPERTIES && property.parameter("ENCODING") == null)
    return uri && isName(property.value)
}

private fun isName(value: String): Boolean =
    value.isNotEmpty() && value.all { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' || it == '-' || it == '_' }

private fun words(text: String): List<String> = text.split(Regex("[^\\p{L}\\p{Nd}]+")).filter { it.isNotEmpty() }

private fun unescape(value: String): String = Regex("\\\\([nN,;\\\\])").replace(value) {
    val next = it.groupValues[1]
    if (next == "n" || next == "N") "\n" else next
}

private fun htmlText(html: String): String {
    val withoutEntities = Regex("&[A-Za-z][A-Za-z0-9]*;").replace(html, " ")
    return Regex("<[^>]*>").replace(withoutEntities) { tag ->
        Regex("=\\s*(\"[^\"]*\"|'[^']*'|[^\\s>]+)").findAll(tag.value).joinToString(" ") { it.groupValues[1] }
    }
}

private fun htmlMarkup(html: String): String {
    val parts = ArrayList<String>()
    Regex("&([A-Za-z][A-Za-z0-9]*);").findAll(html).forEach { parts += it.groupValues[1] }
    Regex("<[^>]*>").findAll(html).forEach { tag ->
        parts += Regex("=\\s*(\"[^\"]*\"|'[^']*'|[^\\s>]+)").replace(tag.value, " ")
    }
    return parts.joinToString(" ")
}

private const val MIN_WORD = 4

private val FIXED_WORDS = listOf("mailto", "example.invalid", "https")

private val IDENTIFIERS = setOf(
    "ORGANIZER", "ATTENDEE", "UID", "RELATED-TO", "URL", "CONFERENCE", "IMAGE", "SOURCE", "EMAIL", "SENT-BY",
    "DELEGATED-TO", "DELEGATED-FROM", "MEMBER", "ALTREP", "DIR",
)

private val URI_PROPERTIES = setOf("ATTACH", "IMAGE", "URL", "CONFERENCE", "SOURCE", "TZURL")

private val URI_PARAMETERS = setOf("ALTREP", "DIR", "SCHEMA")

private val PRESERVED_PROPERTIES = setOf(
    "VERSION", "PRODID", "CALSCALE", "METHOD", "STATUS", "CLASS", "TRANSP", "SEQUENCE", "PRIORITY", "DTSTART", "DTEND",
    "DUE", "DURATION", "DTSTAMP", "CREATED", "LAST-MODIFIED", "RECURRENCE-ID", "RRULE", "RDATE", "EXDATE", "TRIGGER",
    "REPEAT", "ACTION", "TZID", "TZNAME", "TZOFFSETFROM", "TZOFFSETTO", "PERCENT-COMPLETE", "COMPLETED", "FREEBUSY",
    "X-WR-TIMEZONE", "X-LIC-LOCATION", "X-MICROSOFT-CDO-BUSYSTATUS", "X-MICROSOFT-CDO-INTENDEDSTATUS",
    "X-MICROSOFT-CDO-ALLDAYEVENT", "X-MICROSOFT-CDO-IMPORTANCE", "X-MICROSOFT-CDO-INSTTYPE",
    "X-MICROSOFT-CDO-APPT-SEQUENCE", "X-MICROSOFT-DISALLOW-COUNTER", "X-MICROSOFT-DONOTFORWARDMEETING",
    "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR", "X-APPLE-CALENDAR-COLOR", "X-APPLE-DEFAULT-ALARM",
)

private val PRESERVED_PARAMETERS = setOf(
    "VALUE", "TZID", "RANGE", "RELATED", "ROLE", "PARTSTAT", "RSVP", "CUTYPE", "FBTYPE", "ENCODING", "FMTTYPE",
    "RELTYPE", "LANGUAGE", "FEATURE", "SIZE",
)

@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.anonymize.Anonymizer
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty

class AnonymizerFuzzTest {
    // Any input: the anonymizer never throws, the tree keeps its shape and reads back from its text, the output reads
    // back as the anonymized tree when its charset is detected like the input's (another decision is possible when
    // letters change), a property in a canonical form (`canonical`) stays (dates outside VTIMEZONE move only with a
    // shift), the word `zorgwin` leaves every structural place (what sits in a structural place is kept only
    // when it is a structural value, and no list of those holds the word), UID and RELATED-TO are replaced, the summary
    // is printable ASCII. The key check is skipped when the input already contains the key: Jazzer learns the needle of
    // `String.contains` and inserts it into component names, which are kept by design. Input and output are compared
    // unfolded (`unfolded`): Jazzer also breaks the inserted key with a fold, and the writer joins the key again on the
    // END line, which is not a leak.
    @FuzzTest
    @DictionaryFile(resourcePath = "anonymizer.dict")
    fun anonymizer(data: FuzzedDataProvider) {
        val weeks = data.consumeInt(-2, 2)
        val input = data.consumeRemainingAsBytes()
        val source = ICal.parse(input)
        val result = Anonymizer(KEY, weeks).anonymize(input)
        val tree = result.components
        invariant(shape(tree) == shape(source.components)) { "the anonymized tree has another shape" }
        val text = ICal.writer().encodeToString(tree)
        invariant(ICal.parse(text).components == tree) { "the anonymized tree does not read back from its text" }
        val output = ICal.parse(result.bytes)
        if (output.charset == source.charset) {
            invariant(output.components == tree) { "the output reads back as another tree in ${output.charset}" }
        }
        checkProperties(source.components, tree, weeks)
        invariant(result.summary.all { it == '\n' || it in ' '..'~' }) { "the summary has a character from a value" }
        if (!unfolded(input).contains(KEY_HEX, ignoreCase = true)) {
            invariant(!unfolded(result.bytes).contains(KEY_HEX, ignoreCase = true)) {
                "the key is in the output"
            }
        }
    }

    private fun unfolded(bytes: ByteArray): String = bytes.decodeToString()
        .replace("\r\n ", "").replace("\r\n\t", "").replace("\n ", "").replace("\n\t", "")
        .replace("\r ", "").replace("\r\t", "")

    private fun checkProperties(source: List<ICalComponent>, tree: List<ICalComponent>, weeks: Int) {
        val before = properties(source)
        val after = properties(tree)
        for (index in before.indices) {
            val (property, inTimeZone) = before[index]
            val written = after[index].first
            if (canonical(property, inTimeZone)) {
                invariant(written == property || (!inTimeZone && weeks != 0 && property.name in DATES)) {
                    "the structural property ${property.name} changed"
                }
            }
            if (property.name in WATCHED) {
                invariant(!hasCanary(property.value) || !hasCanary(written.value)) {
                    "free text stays in the value of ${property.name}"
                }
            }
            for ((at, parameter) in property.parameters.withIndex()) {
                if (parameter.name !in WATCHED_PARAMETERS) continue
                invariant(parameter.values.none(::hasCanary) || written.parameters[at].values.none(::hasCanary)) {
                    "free text stays in the parameter ${parameter.name} of ${property.name}"
                }
            }
            if (property.name == "UID" || property.name == "RELATED-TO") {
                invariant(written.value.endsWith("@example.invalid")) { "${property.name} is not replaced" }
            }
        }
    }

    private fun hasCanary(value: String): Boolean = value.contains(CANARY, ignoreCase = true)

    // A small form of each structural value that the anonymizer must keep: the oracle does not repeat its lists.
    private fun canonical(property: ICalProperty, inTimeZone: Boolean): Boolean {
        val value = property.value
        val ok = when (property.name) {
            "VERSION" -> value == "2.0"
            "CALSCALE" -> value == "GREGORIAN"
            "METHOD" -> value in METHODS
            "STATUS" -> value in STATUSES
            "SEQUENCE" -> value.length in 1..9 && value.all { it in '0'..'9' }
            "DURATION" -> DURATION.matches(value)
            "TZOFFSETFROM", "TZOFFSETTO" -> inTimeZone && OFFSET.matches(value)
            "TZID" -> value in ZONES
            "RRULE" -> RULE.matches(value)
            in DATES -> DATE.matches(value)
            else -> false
        }
        return ok && property.parameters.all { parameter ->
            parameter.values.size == 1 && when (parameter.name) {
                "TZID" -> parameter.value in ZONES
                "VALUE" -> parameter.value == "DATE" || parameter.value == "DATE-TIME"
                "RANGE" -> parameter.value == "THISANDFUTURE"
                else -> false
            }
        }
    }

    private fun properties(roots: List<ICalComponent>): List<Pair<ICalProperty, Boolean>> {
        val out = ArrayList<Pair<ICalProperty, Boolean>>()
        val stack = ArrayList(roots.asReversed().map { it to (it.name == "VTIMEZONE") })
        while (stack.isNotEmpty()) {
            val (component, inTimeZone) = stack.removeAt(stack.lastIndex)
            for (property in component.properties) out += property to inTimeZone
            for (child in component.components.asReversed()) {
                stack += child to (inTimeZone || child.name == "VTIMEZONE")
            }
        }
        return out
    }

    private fun shape(roots: List<ICalComponent>): List<String> {
        val out = ArrayList<String>()
        val stack = ArrayList(roots.asReversed())
        while (stack.isNotEmpty()) {
            val component = stack.removeAt(stack.lastIndex)
            out += "BEGIN ${component.name} ${component.components.size}"
            for (property in component.properties) {
                out += property.name + property.parameters.joinToString("") { ";${it.name}=${it.values.size}" }
            }
            stack.addAll(component.components.asReversed())
        }
        return out
    }

    private companion object {
        val KEY = ByteArray(32) { (it * 7 + 3).toByte() }
        val KEY_HEX = KEY.joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }
        const val CANARY = "zorgwin"
        val DATES = setOf("DTSTART", "DTEND", "DTSTAMP", "CREATED", "LAST-MODIFIED", "RECURRENCE-ID", "RRULE", "EXDATE")
        val WATCHED = DATES + setOf(
            "VERSION", "PRODID", "CALSCALE", "METHOD", "STATUS", "CLASS", "TRANSP", "SEQUENCE", "PRIORITY", "DURATION",
            "TRIGGER", "ACTION", "REPEAT", "COLOR", "DUE", "RDATE", "TZID", "TZNAME", "TZOFFSETFROM", "TZOFFSETTO",
            "TZUNTIL", "TZID-ALIAS-OF", "X-WR-TIMEZONE", "X-LIC-LOCATION", "X-MICROSOFT-CDO-BUSYSTATUS",
        )
        val WATCHED_PARAMETERS = setOf(
            "TZID", "VALUE", "RANGE", "RELATED", "ROLE", "PARTSTAT", "CUTYPE", "FBTYPE", "RELTYPE", "LANGUAGE", "RSVP",
            "SCHEDULE-STATUS", "SCHEDULE-AGENT", "FMTTYPE", "ENCODING",
        )
        val METHODS = setOf("PUBLISH", "REQUEST", "REPLY", "CANCEL")
        val STATUSES = setOf("TENTATIVE", "CONFIRMED", "CANCELLED", "NEEDS-ACTION", "COMPLETED")
        val ZONES = setOf("Europe/Berlin", "Europe/Moscow", "America/New_York", "UTC")
        val DATE = Regex("[0-9]{8}(T[0-9]{6}Z?)?")
        val DURATION = Regex("-?P([0-9]{1,4}W|[0-9]{1,4}D|T[0-9]{1,4}[HMS])")
        val OFFSET = Regex("[+-][0-9]{4}")
        val RULE =
            Regex("FREQ=(DAILY|WEEKLY|MONTHLY|YEARLY)(;COUNT=[0-9]{1,3})?(;BYDAY=(-1)?(MO|SU))?(;BYMONTH=[0-9]{1,2})?")
    }
}

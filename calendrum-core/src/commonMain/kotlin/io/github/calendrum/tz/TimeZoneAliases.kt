@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.tz

import io.github.calendrum.InternalICalApi
import io.github.calendrum.ProblemCode
import kotlin.concurrent.Volatile
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class AliasMatch internal constructor(public val ianaId: String, public val guessed: Boolean) {
    override fun toString(): String = "AliasMatch($ianaId, guessed=$guessed)"
}

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public object TimeZoneAliases {
    @Volatile
    private var tables: AliasTables? = null

    public val tzdbVersion: String get() = ALIAS_TZDB_VERSION

    public fun resolve(tzid: String): AliasMatch? {
        val data = tables ?: AliasTables().also { tables = it }
        var text = tzid.trim()
        if (text.length >= 2 && text[0] == '"' &&
            text[text.length - 1] == '"'
        ) {
            text = text.substring(1, text.length - 1)
        }
        // The longest real names (Windows display names, Mozilla paths) have about 60 characters; a TZID up to a whole
        // line long would cost a substring per path segment and a search per city over all of it.
        if (text.isEmpty() || text.length > MAX_ALIAS_LENGTH) return null
        val lower = asciiLowercase(text)
        data.links[lower]?.let { return AliasMatch(it, false) }
        data.windows[lower]?.let { return AliasMatch(it, false) }
        if (text.indexOf('/') >= 0) {
            val path = segments(lower)
            var start = 0
            while (start < path.length) {
                val candidate = if (start == 0) path else path.substring(start)
                data.zones[candidate]?.let { return AliasMatch(it, false) }
                data.links[candidate]?.let { return AliasMatch(it, false) }
                val slash = path.indexOf('/', start)
                if (slash < 0) break
                start = slash + 1
            }
        }
        val display = normalizeDisplayName(text)
        data.display[display]?.let { return AliasMatch(it, false) }
        val words = StringBuilder(display.length + 2).append(' ')
        for (char in display) words.append(if (char == ',' || char == '.') ' ' else char)
        val spaced = words.append(' ').toString()
        var best: String? = null
        var bestAt = Int.MAX_VALUE
        var bestLength = 0
        val cities = data.cities
        var index = 0
        while (index < cities.size) {
            val city = cities[index]
            val at = spaced.indexOf(city)
            if (at < 0) {
                index += 2
                continue
            }
            if (at < bestAt || (at == bestAt && city.length > bestLength)) {
                best = cities[index + 1]
                bestAt = at
                bestLength = city.length
            }
            index += 2
        }
        return best?.let { AliasMatch(it, true) }
    }

    public fun report(match: AliasMatch, context: ResolveContext) {
        context.report(if (match.guessed) ProblemCode.TimeZoneGuessed else ProblemCode.TimeZoneAlias, null)
    }

    override fun toString(): String = "TimeZoneAliases"
}

// Generated tables from third_party (links, Windows ids, Windows display names from the comments of CLDR
// windowsZones.xml, zone names). A TZID with a vendor prefix loses leading segments until the rest is an IANA name; a
// display name is normalized (no offset prefix, '/' and ',' alike); the last resort is the first city of the name,
// reported as a guess. The tables are compact: names sorted and front-coded (one character of the alphabet is the
// length shared with the previous name), targets of links, Windows ids and display names are two-character indexes into
// the names, ' Standard Time' is '*'; matching lowercases ASCII only.
internal class AliasTables {
    val zones = HashMap<String, String>()
    val links = HashMap<String, String>()
    val windows = HashMap<String, String>()
    val display = HashMap<String, String>()
    val cities = ArrayList<String>()

    init {
        val names = names(aliasNames())
        val targets = aliasLinks()
        for (index in 0 until ALIAS_ZONE_COUNT) {
            val zone = names[index]
            zones[asciiLowercase(zone)] = zone
            if (!zone.startsWith("Etc/")) city(zone, zone)
        }
        for (index in ALIAS_ZONE_COUNT until names.size) {
            val link = names[index]
            val target = names[code(targets, (index - ALIAS_ZONE_COUNT) * 2)]
            links[asciiLowercase(link)] = target
            if (!link.startsWith("Etc/")) city(link, target)
        }
        entries(aliasWindows(), names, windows, true)
        entries(aliasDisplay(), names, display, false)
    }

    private fun city(name: String, zone: String) {
        val out = StringBuilder(name.length + 2).append(' ')
        for (index in name.lastIndexOf('/') + 1 until name.length) {
            val char = name[index]
            out.append(if (char == '_') ' ' else asciiLower(char))
        }
        if (out.length > 3) {
            cities += out.append(' ').toString()
            cities += zone
        }
    }

    private fun names(text: String): Array<String> {
        var count = 1
        for (char in text) if (char == '\n') count++
        val result = arrayOfNulls<String>(count)
        var previous = ""
        var start = 0
        for (index in 0 until count) {
            var end = text.indexOf('\n', start)
            if (end < 0) end = text.length
            val shared = ALPHABET.indexOf(text[start])
            val name = previous.substring(0, shared) + text.substring(start + 1, end)
            result[index] = name
            previous = name
            start = end + 1
        }
        @Suppress("UNCHECKED_CAST")
        return result as Array<String>
    }

    private fun entries(text: String, names: Array<String>, into: HashMap<String, String>, windows: Boolean) {
        var start = 0
        while (start < text.length) {
            val end = text.indexOf('\n', start)
            var key = text.substring(start, end - 2)
            if (windows && key.endsWith('*')) key = key.substring(0, key.length - 1) + " Standard Time"
            into[asciiLowercase(key)] = names[code(text, end - 2)]
            start = end + 1
        }
    }

    private fun code(text: String, at: Int): Int =
        ALPHABET.indexOf(text[at]) * ALPHABET.length + ALPHABET.indexOf(text[at + 1])

    override fun toString(): String = "AliasTables"
}

private const val MAX_ALIAS_LENGTH = 256
private const val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"

private fun segments(path: String): String {
    val out = StringBuilder(path.length)
    var index = 0
    while (index < path.length) {
        if (path[index] == '/') {
            index++
            continue
        }
        val end = path.indexOf('/', index).let { if (it < 0) path.length else it }
        if (out.isNotEmpty()) out.append('/')
        out.append(path, index, end)
        index = end
    }
    return out.toString()
}

internal fun asciiLower(char: Char): Char = if (char in 'A'..'Z') char + 32 else char

internal fun asciiLowercase(text: String): String {
    var index = 0
    while (index < text.length && text[index] !in 'A'..'Z') index++
    if (index == text.length) return text
    val out = StringBuilder(text.length).append(text, 0, index)
    while (index < text.length) out.append(asciiLower(text[index++]))
    return out.toString()
}

internal fun normalizeDisplayName(name: String): String {
    var text = asciiLowercase(name.trim())
    if (text.startsWith("(")) text = text.substring(text.indexOf(')') + 1)
    val out = StringBuilder()
    var pendingComma = false
    var pendingSpace = false
    for (char in text) {
        when {
            char == ',' || char == '/' -> pendingComma = true

            char == ' ' || char == '\t' -> pendingSpace = true

            else -> {
                if (pendingComma && out.isNotEmpty()) {
                    out.append(',')
                } else if (pendingSpace && out.isNotEmpty()) {
                    out.append(' ')
                }
                pendingComma = false
                pendingSpace = false
                out.append(char)
            }
        }
    }
    return out.toString()
}

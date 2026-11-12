@file:OptIn(InternalICalApi::class)

package io.github.calendrum.tz

import io.github.calendrum.InternalICalApi
import java.io.File
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Builds the alias tables straight from third_party with the code that read the plain tables before the compact format
// and compares both on every name and on random city phrases.
class AliasTablesOracleTest {
    private val directory = File(System.getProperty("calendrum.thirdParty") ?: "../third_party")

    @Test
    fun compactTablesResolveLikeTheSourceTables() {
        val oracle = Oracle(directory)
        val inputs = LinkedHashSet<String>()
        for (zone in oracle.zones.values) {
            inputs += zone
            inputs += zone.uppercase()
            inputs += "/mozilla.org/20050126_1/$zone"
            inputs += "/citadel.org/20190914_1/$zone/"
            inputs += "\"$zone\""
            inputs += " ${zone.substringAfterLast('/').replace('_', ' ')} "
        }
        for (link in oracle.linkNames) {
            inputs += link
            inputs += link.lowercase()
            inputs += "//x//$link"
        }
        inputs += oracle.windowsNames
        inputs += oracle.windowsNames.map { it.lowercase() }
        inputs += oracle.displayNames
        inputs += oracle.displayNames.map { "(UTC+01:00) $it" }
        val random = Random(94)
        val words = oracle.cityWords
        repeat(3_000) {
            val count = 1 + random.nextInt(4)
            inputs +=
                List(count) {
                    words[random.nextInt(words.size)]
                }.joinToString(if (random.nextBoolean()) ", " else " / ")
        }
        inputs += listOf("", "\"\"", "/", "///", "Totally Unknown Zone", "(UTC)", "(", "Etc/GMT+5", "GMT", "utc")
        var resolved = 0
        for (input in inputs) {
            val expected = oracle.resolve(input)
            val actual = TimeZoneAliases.resolve(input)
            assertEquals(expected?.first, actual?.ianaId, input)
            assertEquals(expected?.second, actual?.guessed, input)
            if (actual != null) resolved++
        }
        assertTrue(resolved > inputs.size / 2, "resolved $resolved of ${inputs.size}")
        assertEquals(oracle.version, TimeZoneAliases.tzdbVersion)
    }

    private class Oracle(directory: File) {
        val zones = LinkedHashMap<String, String>()
        val links = LinkedHashMap<String, String>()
        val windows = LinkedHashMap<String, String>()
        val display = LinkedHashMap<String, String>()
        val cities = ArrayList<Pair<String, String>>()
        val linkNames = ArrayList<String>()
        val windowsNames = ArrayList<String>()
        val displayNames = ArrayList<String>()
        val version = File(directory, "tzdb/version").readText().trim()

        val cityWords: List<String> get() = cities.map { it.first }.distinct()

        init {
            val zoneNames = ArrayList<String>()
            val rawLinks = HashMap<String, String>()
            for (file in listOf(
                "africa",
                "antarctica",
                "asia",
                "australasia",
                "europe",
                "northamerica",
                "southamerica",
                "etcetera",
                "backward",
            )) {
                for (raw in File(directory, "tzdb/$file").readLines()) {
                    val line = raw.substringBefore('#')
                    if (line.isBlank() || line[0].isWhitespace()) continue
                    val fields = line.trim().split(Regex("\\s+"))
                    when (fields[0]) {
                        "Zone" -> zoneNames += fields[1]
                        "Link" -> rawLinks[fields[2]] = fields[1]
                    }
                }
            }
            for (zone in zoneNames.sorted()) {
                zones[zone.lowercase()] = zone
                if (zone.startsWith("Etc/")) continue
                val city = zone.substringAfterLast('/').replace('_', ' ').lowercase()
                if (city.length > 2) cities += city to zone
            }
            for (link in rawLinks.keys.sorted()) {
                var target = rawLinks.getValue(link)
                repeat(8) { target = rawLinks[target] ?: target }
                links[link.lowercase()] = target
                linkNames += link
                if (link.startsWith("Etc/")) continue
                val city = link.substringAfterLast('/').replace('_', ' ').lowercase()
                if (city.length > 2) cities += city to target
            }
            var comment: String? = null
            val windowsPairs = ArrayList<Pair<String, String>>()
            val displayPairs = ArrayList<Pair<String, String>>()
            for (raw in File(directory, "cldr/windowsZones.xml").readLines()) {
                val line = raw.trim()
                if (line.startsWith("<!--") && line.contains("(UTC")) {
                    comment = line.removePrefix("<!--").removeSuffix("-->").trim()
                    continue
                }
                if (!line.startsWith("<mapZone") || !line.contains("territory=\"001\"")) continue
                val other = line.substringAfter("other=\"").substringBefore('"')
                val type = line.substringAfter("type=\"").substringBefore('"').substringBefore(' ')
                windowsPairs += other to type
                comment?.let { displayPairs += normalize(it) to type }
                comment = null
            }
            for ((name, type) in windowsPairs.distinct()) {
                windows[name.lowercase()] = type
                windowsNames += name
            }
            for ((name, type) in displayPairs.distinct()) {
                display[name.lowercase()] = type
                displayNames += name
            }
        }

        fun resolve(tzid: String): Pair<String, Boolean>? {
            val text = tzid.trim().removeSurrounding("\"")
            if (text.isEmpty()) return null
            val lower = text.lowercase()
            links[lower]?.let { return it to false }
            windows[lower]?.let { return it to false }
            if (text[0] == '/' || text.contains('/')) {
                val parts = text.split('/').filter { it.isNotEmpty() }
                for (first in parts.indices) {
                    val candidate = parts.subList(first, parts.size).joinToString("/").lowercase()
                    zones[candidate]?.let { return it to false }
                    links[candidate]?.let { return it to false }
                }
            }
            val normalized = normalize(text)
            display[normalized]?.let { return it to false }
            val words = " " + normalized.replace(',', ' ').replace('.', ' ') + " "
            var best: String? = null
            var bestAt = Int.MAX_VALUE
            var bestLength = 0
            for ((city, zone) in cities) {
                val at = words.indexOf(" $city ")
                if (at < 0) continue
                if (at < bestAt || (at == bestAt && city.length > bestLength)) {
                    best = zone
                    bestAt = at
                    bestLength = city.length
                }
            }
            return best?.let { it to true }
        }

        private fun normalize(name: String): String {
            var text = name.trim().lowercase()
            if (text.startsWith("(")) text = text.substringAfter(')')
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
    }
}

package io.github.calendrum.buildlogic.tz

import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters

enum class TimeDefinition { WALL, STANDARD, UTC }

class DaySpec(val month: Int, val day: Int, val dayOfWeek: DayOfWeek?, val mode: Int) {
    fun date(year: Int): LocalDate {
        val length = LocalDate.of(year, month, 1).lengthOfMonth()
        return when (mode) {
            MODE_LAST -> LocalDate.of(year, month, length).with(TemporalAdjusters.previousOrSame(dayOfWeek!!))
            MODE_AFTER -> LocalDate.of(year, month, day).with(TemporalAdjusters.nextOrSame(dayOfWeek!!))
            MODE_BEFORE -> LocalDate.of(year, month, day).with(TemporalAdjusters.previousOrSame(dayOfWeek!!))
            else -> LocalDate.of(year, month, day)
        }
    }

    companion object {
        const val MODE_EXACT = 0
        const val MODE_AFTER = 1
        const val MODE_BEFORE = 2
        const val MODE_LAST = 3
    }
}

class TzRule(
    val name: String,
    val from: Int,
    val to: Int,
    val day: DaySpec,
    val time: Int,
    val definition: TimeDefinition,
    val save: Int,
    val letter: String,
)

class ZoneLine(
    val standardOffset: Int,
    val rules: String?,
    val fixedSave: Int?,
    val format: String,
    val until: LocalDateTime?,
    val untilDefinition: TimeDefinition,
)

class TzSource(
    val zones: Map<String, List<ZoneLine>>,
    val rules: Map<String, List<TzRule>>,
    val links: Map<String, String>,
)

class Transition(
    val epochSecond: Long,
    val offsetBefore: Int,
    val offsetAfter: Int,
    val save: Int,
    val abbreviation: String,
)

class TailRule(
    val day: DaySpec,
    val time: Int,
    val definition: TimeDefinition,
    val standardOffset: Int,
    val offsetBefore: Int,
    val offsetAfter: Int,
    val save: Int,
    val abbreviation: String,
)

class CompiledZone(
    val name: String,
    val initialOffset: Int,
    val initialSave: Int,
    val initialAbbreviation: String,
    val transitions: List<Transition>,
    val tailStartYear: Int,
    val tail: List<TailRule>,
)

object Tzdb {
    val zoneFiles: List<String> = listOf(
        "africa",
        "antarctica",
        "asia",
        "australasia",
        "europe",
        "northamerica",
        "southamerica",
        "etcetera",
        "backward",
    )

    fun read(directory: File): TzSource {
        val zones = LinkedHashMap<String, MutableList<ZoneLine>>()
        val rules = LinkedHashMap<String, MutableList<TzRule>>()
        val links = LinkedHashMap<String, String>()
        for (fileName in zoneFiles) {
            var current: MutableList<ZoneLine>? = null
            for (raw in File(directory, fileName).readLines()) {
                val line = raw.substringBefore('#')
                if (line.isBlank()) continue
                val fields = line.trim().split(Regex("\\s+"))
                if (line[0].isWhitespace()) {
                    val zone = current ?: error("Continuation without a zone: $raw")
                    zone += zoneLine(fields, 0)
                    if (zone.last().until == null) current = null
                    continue
                }
                when (fields[0]) {
                    "Rule" -> rules.getOrPut(fields[1]) { ArrayList() } += rule(fields)

                    "Zone" -> {
                        val zone = ArrayList<ZoneLine>()
                        zones[fields[1]] = zone
                        zone += zoneLine(fields, 2)
                        current = if (zone.last().until == null) null else zone
                    }

                    "Link" -> links[fields[2]] = fields[1]

                    else -> error("Unknown line: $raw")
                }
            }
        }
        return TzSource(zones, rules, links)
    }

    private fun rule(fields: List<String>): TzRule {
        val from = if (fields[2] == "min") -9999 else fields[2].toInt()
        val to = when (fields[3]) {
            "only" -> from
            "max" -> Int.MAX_VALUE
            else -> fields[3].toInt()
        }
        val month = month(fields[5])
        val (time, definition) = time(fields[7])
        return TzRule(
            fields[1],
            from,
            to,
            day(month, fields[6]),
            time,
            definition,
            duration(fields[8].trimEnd('s', 'd')),
            if (fields[9] == "-") "" else fields[9],
        )
    }

    private fun zoneLine(fields: List<String>, first: Int): ZoneLine {
        val standard = duration(fields[first])
        val rules = fields[first + 1]
        var named: String? = null
        var fixed: Int? = null
        when {
            rules == "-" -> fixed = 0
            rules[0].isDigit() || rules[0] == '-' -> fixed = duration(rules)
            else -> named = rules
        }
        val format = fields[first + 2]
        val untilFields = fields.drop(first + 3)
        if (untilFields.isEmpty()) return ZoneLine(standard, named, fixed, format, null, TimeDefinition.WALL)
        val year = untilFields[0].toInt()
        val month = if (untilFields.size > 1) month(untilFields[1]) else 1
        val date = if (untilFields.size > 2) day(month, untilFields[2]).date(year) else LocalDate.of(year, month, 1)
        val (time, definition) = if (untilFields.size > 3) time(untilFields[3]) else 0 to TimeDefinition.WALL
        return ZoneLine(standard, named, fixed, format, date.atStartOfDay().plusSeconds(time.toLong()), definition)
    }

    private fun month(text: String): Int {
        val names = listOf("jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec")
        return names.indexOfFirst { text.lowercase().startsWith(it) } + 1
    }

    private fun weekday(text: String): DayOfWeek {
        val names = listOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")
        return DayOfWeek.of(names.indexOfFirst { text.lowercase().startsWith(it) } + 1)
    }

    private fun day(month: Int, text: String): DaySpec = when {
        text.startsWith("last") -> DaySpec(month, 0, weekday(text.substring(4)), DaySpec.MODE_LAST)
        text.contains(">=") -> DaySpec(month, text.substringAfter(">=").toInt(), weekday(text), DaySpec.MODE_AFTER)
        text.contains("<=") -> DaySpec(month, text.substringAfter("<=").toInt(), weekday(text), DaySpec.MODE_BEFORE)
        else -> DaySpec(month, text.toInt(), null, DaySpec.MODE_EXACT)
    }

    private fun time(text: String): Pair<Int, TimeDefinition> {
        val definition = when (text.last()) {
            's' -> TimeDefinition.STANDARD
            'u', 'g', 'z' -> TimeDefinition.UTC
            else -> TimeDefinition.WALL
        }
        val value = if (text.last().isLetter()) text.dropLast(1) else text
        return duration(value) to definition
    }

    fun duration(text: String): Int {
        if (text == "-" || text.isEmpty()) return 0
        val negative = text.startsWith("-")
        val parts = text.removePrefix("-").split(':')
        var seconds = parts[0].toInt() * 3600
        if (parts.size > 1) seconds += parts[1].toInt() * 60
        if (parts.size > 2) seconds += parts[2].substringBefore('.').toInt()
        return if (negative) -seconds else seconds
    }

    // Follows the windows of java.time's ZoneRulesBuilder: the savings in effect at the start of a zone line come from
    // the last rule transition before it, computed with the previous line's standard offset; a rule adds a transition
    // only when the offset changes; rules of a line in effect forever become the tail.
    fun compile(source: TzSource, name: String, explicitUntilYear: Int): CompiledZone {
        val lines = source.zones.getValue(name)
        val first = lines[0]
        val transitions = ArrayList<Transition>()
        var standard = first.standardOffset
        var savings = first.fixedSave ?: 0
        val initialOffset = standard + savings
        val initialRules = first.rules?.let { source.rules.getValue(it) }.orEmpty()
        val initialAbbreviation = abbreviation(first, savings, initialRules.firstOrNull { it.save == 0 }?.letter ?: "")
        var windowStart = Long.MIN_VALUE
        var windowOffset = initialOffset
        var tail: List<TailRule> = emptyList()
        var tailStartYear = Int.MAX_VALUE
        for ((index, line) in lines.withIndex()) {
            val last = index == lines.size - 1
            val startYear = if (windowStart == Long.MIN_VALUE) -9999 else yearOf(windowStart, windowOffset)
            val endYear = line.until?.year ?: maxOf(explicitUntilYear, startYear)
            val expanded = ArrayList<Pair<TzRule, Int>>()
            val ruleLines = line.rules?.let { source.rules.getValue(it) }.orEmpty()
            for (rule in ruleLines) {
                val to = if (rule.to == Int.MAX_VALUE) endYear + 1 else rule.to
                val from = if (rule.from == -9999) startYear - 1 else rule.from
                for (year in from..to) expanded += rule to year
            }
            expanded.sortWith(compareBy({ it.second }, { it.first.day.date(it.second) }, { it.first.time }))
            var effective = line.fixedSave
            var letter = ""
            if (effective == null) {
                effective = 0
                for ((rule, year) in expanded) {
                    if (epoch(rule, year, standard, savings) > windowStart) break
                    effective = rule.save
                    letter = rule.letter
                }
            }
            standard = line.standardOffset
            val wall = standard + effective
            if (windowOffset != wall) {
                transitions +=
                    Transition(windowStart, windowOffset, wall, effective, abbreviation(line, effective, letter))
            }
            savings = effective
            for ((rule, year) in expanded) {
                val at = epoch(rule, year, standard, savings)
                val before = standard + savings
                val after = standard + rule.save
                if (at >= windowStart && (line.until == null || at < endEpoch(line, savings)) && before != after) {
                    transitions += Transition(at, before, after, rule.save, abbreviation(line, rule.save, rule.letter))
                    savings = rule.save
                }
            }
            if (last) {
                val lastRules = ruleLines.filter {
                    it.to == Int.MAX_VALUE
                }.sortedWith(compareBy({ it.day.date(2001) }, { it.time }))
                if (lastRules.isNotEmpty()) {
                    tailStartYear = endYear + 2
                    var save = savings
                    tail = lastRules.map { rule ->
                        TailRule(
                            rule.day,
                            rule.time,
                            rule.definition,
                            standard,
                            standard + save,
                            standard + rule.save,
                            rule.save,
                            abbreviation(line, rule.save, rule.letter),
                        ).also { save = rule.save }
                    }
                }
            }
            windowOffset = standard + savings
            if (line.until != null) windowStart = endEpoch(line, savings)
        }
        return CompiledZone(
            name,
            initialOffset,
            first.fixedSave ?: 0,
            initialAbbreviation,
            transitions,
            tailStartYear,
            tail,
        )
    }

    private fun yearOf(epochSecond: Long, offset: Int): Int =
        LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.ofTotalSeconds(offset)).year

    private fun epoch(rule: TzRule, year: Int, standard: Int, savingsBefore: Int): Long {
        val local = rule.day.date(year).atStartOfDay().plusSeconds(rule.time.toLong())
        val wallBefore = standard + savingsBefore
        val base = local.toEpochSecond(ZoneOffset.UTC)
        return when (rule.definition) {
            TimeDefinition.UTC -> base
            TimeDefinition.STANDARD -> base - standard
            TimeDefinition.WALL -> base - wallBefore
        }
    }

    private fun endEpoch(line: ZoneLine, savings: Int): Long {
        val base = line.until!!.toEpochSecond(ZoneOffset.UTC)
        return when (line.untilDefinition) {
            TimeDefinition.UTC -> base
            TimeDefinition.STANDARD -> base - line.standardOffset
            TimeDefinition.WALL -> base - line.standardOffset - savings
        }
    }

    fun abbreviation(line: ZoneLine, save: Int, letter: String): String {
        val format = line.format
        return when {
            format.contains("%s") -> format.replace("%s", letter)
            format.contains("%z") -> format.replace("%z", numeric(line.standardOffset + save))
            format.contains('/') -> if (save != 0) format.substringAfter('/') else format.substringBefore('/')
            else -> format
        }
    }

    private fun numeric(offset: Int): String {
        val sign = if (offset < 0) "-" else "+"
        val absolute = kotlin.math.abs(offset)
        val hours = absolute / 3600
        val minutes = absolute / 60 % 60
        val seconds = absolute % 60
        val out = StringBuilder(sign).append(hours.toString().padStart(2, '0'))
        if (minutes != 0 || seconds != 0) out.append(minutes.toString().padStart(2, '0'))
        if (seconds != 0) out.append(seconds.toString().padStart(2, '0'))
        return out.toString()
    }
}

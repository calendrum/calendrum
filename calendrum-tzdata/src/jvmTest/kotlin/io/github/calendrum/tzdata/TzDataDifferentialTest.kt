package io.github.calendrum.tzdata

import io.github.calendrum.ICal
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import java.io.File
import java.time.ZoneId
import java.time.zone.ZoneRulesProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class TzDataDifferentialTest {
    private val start = java.time.Instant.parse("1970-01-01T00:00:00Z")
    private val end = java.time.Instant.parse("2038-01-01T00:00:00Z")

    private fun samples(id: String): List<Long> {
        val rules = ZoneId.of(id).rules
        val result = ArrayList<Long>()
        var transition = rules.nextTransition(start.minusSeconds(1))
        while (transition != null && transition.instant < end) {
            val at = transition.instant.epochSecond
            result += at - 1
            result += at
            result += at + 1
            transition = rules.nextTransition(transition.instant)
        }
        var at = start.epochSecond
        while (at < end.epochSecond) {
            result += at
            at += 15 * 86_400
        }
        return result
    }

    private fun differs(id: String, zone: ICalTimeZone, from: Long = start.epochSecond): Long? {
        val rules = ZoneId.of(id).rules
        for (at in samples(id)) {
            if (at < from) continue
            val expected = rules.getOffset(java.time.Instant.ofEpochSecond(at)).totalSeconds
            if (zone.offsetAt(Instant.fromEpochSeconds(at)).totalSeconds != expected) return at
        }
        return null
    }

    private fun compiled(calendar: VCalendar, id: String): ICalTimeZone =
        TimeZoneResolvers.vtimezone(calendar).resolve(id, ResolveContext.Empty)!!

    private fun calendar(id: String, mode: VTimeZoneMode): VCalendar {
        val zone = TzData.vtimezone(id, mode)!!
        val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//test//EN\r\n" +
            ICal.writer().encodeToString(zone.component) + "END:VCALENDAR\r\n"
        return ICal.parse(text).calendars.single()
    }

    @Test
    fun everyZoneMatchesJavaTime() {
        val jdkVersion = ZoneRulesProvider.getVersions("UTC").lastKey()
        val allowed = changedSince(jdkVersion)
        val ids = TzData.zoneIds.filter { it in ZoneId.getAvailableZoneIds() }.sorted()
        assertTrue(ids.size > 550, "only ${ids.size} zones in common with java.time")
        val table = ArrayList<String>()
        val full = ArrayList<String>()
        val outlook = ArrayList<String>()
        val notRepresentable = ArrayList<String>()
        for (id in ids) {
            val tzdata = TzData.zone(id)!!
            differs(id, tzdata)?.let { table += "$id@$it" }
            differs(id, compiled(calendar(id, VTimeZoneMode.Full), id))?.let { full += "$id@$it" }
            if (outlookRepresentable(id)) {
                differs(id, compiled(calendar(id, VTimeZoneMode.OutlookCompatible), id), CURRENT_RULES_FROM)
                    ?.let { outlook += "$id@$it" }
            } else {
                notRepresentable += id
            }
        }
        println("tzdata ${TzData.version} against java.time $jdkVersion: ${ids.size} zones")
        println("table differs: $table")
        println("full VTIMEZONE differs: $full")
        println("outlook VTIMEZONE differs: $outlook")
        println("current rules not representable in Outlook mode: $notRepresentable")
        val unexplained = (table + full + outlook).map { it.substringBefore('@') }.toSet().filter { !allowed(it) }
        assertEquals(
            emptyList(),
            unexplained,
            "zones that differ from java.time without a tzdb change since $jdkVersion",
        )
    }

    private companion object {
        val CURRENT_RULES_FROM: Long = java.time.Instant.parse("2027-01-01T00:00:00Z").epochSecond
    }

    private fun outlookRepresentable(id: String): Boolean {
        val record = TzZones.record(id)!!
        val from = CURRENT_RULES_FROM
        if (record.tail.isEmpty()) return record.times.none { it >= from }
        if (!record.tail.all { isExactRule(it) }) return false
        val generated = HashSet<Long>()
        for (year in 2027 until record.tailStartYear) {
            for (rule in record.tail) generated += transitionEpoch(rule, year)
        }
        return generated.containsAll(record.times.filter { it >= from })
    }

    private fun changedSince(version: String): (String) -> Boolean {
        if (version == TzData.version) return { false }
        val directory = File(System.getProperty("calendrum.tzdb"))
        val news = File(directory, "NEWS").readText()
        val start = news.indexOf("Release ${TzData.version} ")
        val stop = news.indexOf("Release $version ")
        val text = if (start >= 0 && stop > start) news.substring(start, stop) else ""
        val links = HashMap<String, String>()
        for (file in listOf(
            "africa",
            "antarctica",
            "asia",
            "australasia",
            "europe",
            "northamerica",
            "southamerica",
            "backward",
        )) {
            for (line in File(directory, file).readLines()) {
                val fields = line.substringBefore('#').trim().split(Regex("\\s+"))
                if (fields.size >= 3 && fields[0] == "Link") links[fields[2]] = fields[1]
            }
        }
        val countries = File(directory, "iso3166.tab").readLines().filter { !it.startsWith("#") && it.contains('\t') }
            .associate { it.substringBefore('\t') to it.substringAfter('\t') }
        val zoneCountries = HashMap<String, String>()
        val zoneComments = HashMap<String, String>()
        for (line in File(directory, "zone.tab").readLines()) {
            if (line.startsWith("#")) continue
            val fields = line.split('\t')
            if (fields.size >= 3) zoneCountries[fields[2]] = countries[fields[0]] ?: ""
            if (fields.size >= 4) zoneComments[fields[2]] = fields[3].substringAfterLast(" - ").substringBefore(" (")
        }
        return { id ->
            var canonical = id
            repeat(4) { canonical = links[canonical] ?: canonical }
            val names = listOf(
                id,
                canonical,
                id.substringAfterLast('/').replace('_', ' '),
                canonical.substringAfterLast('/').replace('_', ' '),
                zoneCountries[canonical] ?: "",
                zoneComments[canonical] ?: "",
            )
            names.any { it.isNotEmpty() && text.contains(it) }
        }
    }
}

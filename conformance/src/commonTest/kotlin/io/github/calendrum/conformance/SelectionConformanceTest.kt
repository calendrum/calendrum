@file:OptIn(InternalICalApi::class)

package io.github.calendrum.conformance

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.InternalICalApi
import io.github.calendrum.syntax.Feature
import io.github.calendrum.syntax.Selection
import io.github.calendrum.syntax.TimeZoneSelection
import io.github.calendrum.syntax.filterSelection
import kotlin.test.Test
import kotlin.test.assertTrue

class SelectionConformanceTest {
    private val selections = listOf(
        Selection.Schedule,
        Selection.SyncMetadata,
        Selection.FreeBusy,
        Selection.builder().components("VEVENT", "VTODO").properties("UID", "SUMMARY", "X-WR-CALNAME")
            .need(Feature.Alarms).build(),
        Selection.builder().components("X-UNKNOWN", "VJOURNAL").properties("X-NAME", "x-vendor", "DESCRIPTION")
            .need(Feature.Participants).timeZones(TimeZoneSelection.None).build(),
        Selection.builder().need(Feature.Occurrences).timeZones(TimeZoneSelection.Referenced).build(),
    )

    @Test
    fun aSelectionEqualsTheFullTreeFilteredByIt() {
        val failures = ArrayList<String>()
        var checked = 0
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject ?: continue
            val full = try {
                ICal.parser(parseOptions(meta)).parse(input)
            } catch (e: ICalParseException) {
                continue
            }
            for (selection in selections) {
                val selected = try {
                    ICal.parser(parseOptions(meta, selection)).parse(input)
                } catch (e: ICalParseException) {
                    failures += "${case.directory}: $selection throws ${e.problems.last().code.id}"
                    continue
                }
                if (!selected.isPartial) failures += "${case.directory}: $selection is not partial"
                if (selected.components != filterSelection(full.components, selection)) {
                    failures += "${case.directory}: $selection differs from the filtered full tree"
                }
                checked += 1
            }
        }
        assertTrue(checked >= Suite.cases.size, "Only $checked selections were checked")
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }
}

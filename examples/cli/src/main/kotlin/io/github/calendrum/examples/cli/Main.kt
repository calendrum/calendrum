@file:OptIn(InternalICalApi::class)

package io.github.calendrum.examples.cli

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.anonymize.Anonymizer
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tzdata.TzData
import io.github.calendrum.validate.Validator
import java.io.File
import kotlin.system.exitProcess
import kotlin.time.Instant

fun main(args: Array<String>) {
    val out = StringBuilder()
    val code = run(args.toList(), out)
    print(out)
    if (code != 0) exitProcess(code)
}

fun run(args: List<String>, out: StringBuilder): Int {
    val command = args.getOrNull(0)
    val file = args.getOrNull(1)?.let(::File)
    if (command == null || file == null || !file.isFile) {
        out.append(USAGE)
        return 2
    }
    val bytes = file.readBytes()
    when (command) {
        "summary" -> summary(bytes, out)

        "occurrences" -> occurrences(bytes, args.getOrNull(2), args.getOrNull(3), out)

        "validate" -> return validate(bytes, out)

        "anonymize" -> out.append(Anonymizer(ANONYMIZER_KEY, 3).anonymize(bytes).bytes.decodeToString())

        else -> {
            out.append(USAGE)
            return 2
        }
    }
    return 0
}

private fun summary(bytes: ByteArray, out: StringBuilder) {
    val result = ICal.parse(bytes)
    for (calendar in result.calendars) {
        out.append("calendar ").append(calendar.component.property("X-WR-CALNAME")?.value ?: "-").append('\n')
        for (event in calendar.events) {
            out.append("  ").append(event.uid).append(' ').append(event.summary.orEmpty()).append('\n')
        }
    }
    for (problem in result.problems) out.append("problem ").append(problem.code.id).append('\n')
}

private fun occurrences(bytes: ByteArray, from: String?, to: String?, out: StringBuilder) {
    val calendar = ICal.parse(bytes).calendars.first()
    val zones = TimeZoneResolvers.chain(
        TimeZoneResolvers.vtimezone(calendar),
        TzData.resolver,
        unknown = UnknownZonePolicy.Utc,
    )
    val options = OccurrenceOptions.builder().zones(zones).build()
    val start = Instant.parse(from ?: "2026-09-01T00:00:00Z")
    val end = Instant.parse(to ?: "2026-10-01T00:00:00Z")
    for (occurrence in calendar.occurrences(start, end, options)) {
        val summary = occurrence.component.property("SUMMARY")?.value.orEmpty()
        out.append(occurrence.start).append(' ').append(occurrence.end).append(' ').append(summary).append('\n')
    }
}

private fun validate(bytes: ByteArray, out: StringBuilder): Int {
    var errors = 0
    for (calendar in ICal.parse(bytes).calendars) {
        val report = Validator.validate(calendar)
        for (issue in report.issues) {
            out.append(issue.code.id).append(' ').append(issue.severity).append(' ').append(issue.path).append('\n')
        }
        if (!report.isValid) errors++
    }
    out.append(if (errors == 0) "valid\n" else "invalid\n")
    return if (errors == 0) 0 else 1
}

private val ANONYMIZER_KEY = "calendrum example key".encodeToByteArray()

private const val USAGE = "usage: cli summary|occurrences|validate|anonymize <file.ics> [from to]\n"

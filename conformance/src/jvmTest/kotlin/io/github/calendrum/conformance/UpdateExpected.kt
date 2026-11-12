package io.github.calendrum.conformance

import io.github.calendrum.ICal
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    require(args.size == 2) { "Usage: UpdateExpected <conformance/ical directory> <corpus directory>" }
    val directories = File(args[0]).listFiles().orEmpty()
        .filter { it.isDirectory && !it.name.startsWith(".") }
        .sortedBy { it.name }
    val errors = ArrayList<String>()
    var updated = 0
    for (directory in directories) {
        val input = File(directory, Suite.INPUT)
        val metaFile = File(directory, Suite.META)
        if (!input.isFile || !metaFile.isFile) {
            errors += "${directory.name}: needs ${Suite.INPUT} and ${Suite.META}"
            continue
        }
        val json = readJson(metaFile.readBytes())
        val meta = json.value as? JsonObject
        val problems = json.problems + meta?.let { Suite.schemaFor(Suite.META).validate(it) }.orEmpty()
        if (meta == null || problems.isNotEmpty()) {
            errors += problems.ifEmpty { listOf("not a JSON object") }.map { "${directory.name}/${Suite.META}: $it" }
            continue
        }
        val inputBytes = input.readBytes()
        val outcome = runCase(inputBytes, meta)
        val expected = Outcome.files.associateWith { outcome.file(it) }.toMutableMap()
        if (hasFingerprintsTag(meta)) expected[Suite.FINGERPRINTS] = fingerprintsJson(inputBytes)
        try {
            expected[Suite.JCAL] = jcalOf(inputBytes, meta)
        } catch (e: JcalUndefined) {
            val stale = File(directory, Suite.JCAL)
            if (stale.isFile && stale.delete()) println("deleted: ${directory.name}/${Suite.JCAL}")
            if (!jcalUndefined(meta)) {
                errors += "${directory.name}: no jCal (${e.message}); add jcal: false and jcalReason to ${Suite.META}"
            }
        }
        for ((name, json) in expected) {
            if (write(File(directory, name), json)) {
                println("updated: ${directory.name}/$name")
                updated++
            }
        }
    }
    val corpus = File(args[1])
    val calendars = RealCorpus.calendars(corpus)
    for (calendar in calendars) {
        val target = RealCorpus.expectedProblems(calendar)
        if (write(target, RealCorpus.problemsJson(ICal.parse(calendar.readBytes())))) {
            println("updated: ${target.relativeTo(corpus).invariantSeparatorsPath}")
            updated++
        }
    }
    println("${directories.size} cases, ${calendars.size} real corpus files, $updated files updated")
    if (errors.isNotEmpty()) {
        System.err.println(errors.joinToString("\n"))
        exitProcess(1)
    }
}

private fun write(file: File, json: Json): Boolean {
    val bytes = json.toCanonicalJson().encodeToByteArray()
    if (file.isFile && file.readBytes().contentEquals(bytes)) return false
    file.writeBytes(bytes)
    return true
}

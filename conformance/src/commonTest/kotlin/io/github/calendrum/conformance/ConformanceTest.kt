package io.github.calendrum.conformance

import io.github.calendrum.ICal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ConformanceTest {
    @Test
    fun schemasAreCanonicalJson() {
        assertEquals(Suite.schemaFiles.values.sorted(), Suite.schemaDocuments.map { it.name }.sorted())
        val failures = Suite.schemaDocuments.flatMap { document ->
            readJson(document.bytes).problems.map { "${document.name}: $it" }
        }
        report("schemas are not canonical JSON", failures)
    }

    @Test
    fun caseDirectoriesAreComplete() {
        assertTrue(Suite.cases.size >= MIN_CASES, "The suite has only ${Suite.cases.size} cases")
        val failures = Suite.cases.flatMap { case -> layoutProblems(case).map { "${case.directory}: $it" } }
        report("case directories are incomplete", failures)
    }

    @Test
    fun filesFollowTheirSchemas() {
        val failures = ArrayList<String>()
        val ids = HashMap<String, String>()
        for (case in Suite.cases) {
            for (name in Suite.schemaFiles.keys) {
                val file = case.file(name) ?: continue
                val json = readJson(file.bytes)
                failures += json.problems.map { "${case.directory}/$name: $it" }
                val value = json.value ?: continue
                failures += Suite.schemaFor(name).validate(value).map { "${case.directory}/$name: $it" }
            }
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject ?: continue
            val id = (meta["id"] as? JsonString)?.value ?: continue
            if (id !=
                case.directory
            ) {
                failures += "${case.directory}/${Suite.META}: id \"$id\" differs from the directory"
            }
            ids.put(id, case.directory)?.let {
                failures += "${case.directory}/${Suite.META}: id \"$id\" is used by $it"
            }
        }
        report("files do not follow their schemas", failures)
    }

    @Test
    fun parserMatchesEveryCase() {
        val failures = ArrayList<String>()
        var failed = 0
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT) ?: continue
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject
            if (meta == null || Suite.schemaFor(Suite.META).validate(meta).isNotEmpty()) {
                failures += "${case.directory}: cannot run, ${Suite.META} is missing or invalid"
                failed++
                continue
            }
            val outcome = runCase(input.bytes, meta)
            var matches = true
            for (name in Outcome.files) {
                val actual = outcome.file(name)
                val expected = case.file(name)
                if (expected == null) {
                    failures += "${case.directory}/$name: missing, the parser gives:\n${actual.toCanonicalJson()}"
                    matches = false
                    continue
                }
                val expectedText = expected.bytes.decodeToString()
                val actualText = actual.toCanonicalJson()
                if (expectedText == actualText) continue
                matches = false
                failures += "${case.directory}/$name:"
                failures += difference(expectedText, actualText, actual).map { "  $it" }
            }
            if (!matches) failed++
        }
        if (failures.isNotEmpty()) {
            fail(
                "$failed of ${Suite.cases.size} conformance cases do not match the parser:\n" +
                    failures.joinToString("\n") + "\n" +
                    "Fix the parser, or, only if the new output is correct, run ${Suite.UPDATE_COMMAND} " +
                    "and review the changes (conformance/README.md).",
            )
        }
    }

    @Test
    fun jcalMatchesEveryCase() {
        val failures = ArrayList<String>()
        var compared = 0
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject ?: continue
            val expected = case.file(Suite.JCAL)
            val actual = try {
                jcalOf(input, meta)
            } catch (e: JcalUndefined) {
                if (expected != null) failures += "${case.directory}: no jCal, ${e.message}"
                continue
            }
            if (expected == null) {
                if (jcalUndefined(meta)) {
                    failures +=
                        "${case.directory}: ${Suite.META} says jcal: false, but jCal is defined"
                }
                continue
            }
            val expectedText = expected.bytes.decodeToString()
            val actualText = actual.toCanonicalJson()
            if (expectedText != actualText) {
                failures += "${case.directory}/${Suite.JCAL}:"
                failures += difference(expectedText, actualText, actual).map { "  $it" }
            }
            compared += 1
        }
        assertTrue(compared > Suite.cases.size / 2, "Only $compared cases have jCal")
        report("jCal differs from the expected files", failures)
    }

    @Test
    fun streamsMatchEveryCase() {
        val failures = ArrayList<String>()
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject ?: continue
            val parser = ICal.parser(parseOptions(meta))
            val expected = describe { parser.parse(input) }
            val components = describeComponents(parser.components(input))
            for (chunk in CHUNKS) {
                if (describe { parser.parse(Chunks(input, chunk)) } != expected) {
                    failures += "${case.directory}: parse differs in chunks of $chunk"
                }
                if (describeComponents(parser.components(Chunks(input, chunk))) != components) {
                    failures += "${case.directory}: components differ in chunks of $chunk"
                }
            }
        }
        report("streams differ from the byte array", failures)
    }

    @Test
    fun fingerprintsMatchTaggedCases() {
        val failures = ArrayList<String>()
        var tagged = 0
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val expected = case.file(Suite.FINGERPRINTS) ?: continue
            tagged++
            val actual = fingerprintsJson(input)
            val expectedText = expected.bytes.decodeToString()
            val actualText = actual.toCanonicalJson()
            if (expectedText == actualText) continue
            failures += "${case.directory}/${Suite.FINGERPRINTS}:"
            failures += difference(expectedText, actualText, actual).map { "  $it" }
        }
        assertTrue(tagged >= MIN_FINGERPRINT_CASES, "Only $tagged cases have ${Suite.FINGERPRINTS}")
        report("fingerprints differ (run ${Suite.UPDATE_COMMAND} only if the change is intended)", failures)
    }

    @Test
    fun parseComponentMatchesTheTree() {
        val failures = ArrayList<String>()
        val parser = ICal.parser()
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val result = parser.parse(input)
            if (result.isTruncated) continue
            val children = result.components.flatMap { if (it.name == "VCALENDAR") it.components else listOf(it) }
            val entries = ICal.fingerprints(input).entries
            if (entries.size != children.size) {
                failures += "${case.directory}: ${entries.size} fingerprints for ${children.size} components"
                continue
            }
            for (index in entries.indices) {
                if (parser.parseComponent(input, entries[index]) != children[index]) {
                    failures += "${case.directory}: parseComponent of entry $index differs from the tree"
                }
            }
        }
        report("parseComponent differs from the tree", failures)
    }

    // Cases cut from real calendars stay with the maintainer: the `real-` prefix keeps them out of the repository
    // (.gitignore), so a checkout without them simply has none to run.
    @Test
    fun anonymizedRealCasesRoundTrip() {
        val failures = ArrayList<String>()
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject ?: continue
            val real = (meta["source"] as? JsonString)?.value == ANONYMIZED_REAL
            if (real != case.directory.startsWith(PRIVATE_PREFIX)) {
                failures +=
                    "${case.directory}: a case from a real calendar, and only such a case, is named $PRIVATE_PREFIX*"
            }
            if (!real) continue
            failures += roundTripFailures(input, ICal.parser(parseOptions(meta))).map { "${case.directory}: $it" }
        }
        report("anonymized real cases do not round-trip or are not private", failures)
    }

    @Test
    fun everySyntaxCodeHasACase() {
        val seen = HashSet<String>()
        for (case in Suite.cases) {
            val diagnostics = case.file(Suite.DIAGNOSTICS)?.let { readJson(it.bytes).value } as? JsonArray ?: continue
            for (item in diagnostics.items) {
                ((item as? JsonObject)?.get("code") as? JsonString)?.let { seen += it.value }
            }
        }
        val missing = (1..SYNTAX_CODES).map {
            "ICAL-S" + it.toString().padStart(CODE_DIGITS, '0')
        }.filter { it !in seen }
        assertEquals(emptyList(), missing, "Syntax codes without a conformance case")
    }

    private fun difference(expectedText: String, actualText: String, actual: Json): List<String> {
        val expected = try {
            parseJson(expectedText)
        } catch (e: JsonSyntaxException) {
            return listOf("expected file is not valid JSON (${e.message}); ${textDifference(expectedText, actualText)}")
        }
        return jsonDifferences(expected, actual).ifEmpty { listOf(textDifference(expectedText, actualText)) }
    }

    private fun report(title: String, failures: List<String>) {
        if (failures.isNotEmpty()) fail("Conformance suite: $title:\n" + failures.joinToString("\n"))
    }

    private companion object {
        const val MIN_CASES = 50
        const val MIN_FINGERPRINT_CASES = 5
        const val ANONYMIZED_REAL = "anonymized-real"
        const val PRIVATE_PREFIX = "real-"
        val CHUNKS: List<Int> = (1..16).toList() + 64 * 1024
        const val SYNTAX_CODES = 28
        const val CODE_DIGITS = 3
    }
}

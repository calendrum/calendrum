package io.github.calendrum.tooling.cli

import io.github.calendrum.tooling.ops.SchemaCheck
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// The cases of conformance/ops through the flags of the command line: where the request can be said with flags, the output
// of --json is the expected response, by the rules of the README of the contract.
class CasesTest {
    private val casesDirectory = File(System.getProperty("calendrum.cases"))
    private val directory = temporaryDirectory()
    private val common = load("common")
    private val checker = SchemaCheck { name -> common.getValue("\$defs").jsonObject[name]?.jsonObject }

    private class Case(
        val id: String,
        val operation: String,
        val request: JsonObject,
        val expected: JsonObject,
        val ignored: Set<String>,
    )

    private fun load(name: String): JsonObject =
        Json.parseToJsonElement(File(File(casesDirectory, "schema"), "$name.schema.json").readText()).jsonObject

    private fun cases(): List<Case> = casesDirectory.listFiles().orEmpty()
        .filter { File(it, "request.json").exists() }
        .sortedBy { it.name }
        .map { dir ->
            val meta = Json.parseToJsonElement(File(dir, "meta.json").readText()).jsonObject
            Case(
                dir.name,
                meta.getValue("operation").jsonPrimitive.content,
                Json.parseToJsonElement(File(dir, "request.json").readText()).jsonObject,
                Json.parseToJsonElement(File(dir, "expected.json").readText()).jsonObject,
                (meta["ignore_fields"] as? JsonArray).orEmpty().map { it.jsonPrimitive.content }.toSet(),
            )
        }

    private fun text(request: JsonObject, name: String): String? = (request[name] as? JsonPrimitive)?.takeIf {
        it.isString
    }?.content

    private fun file(name: String, content: String): String = File(directory, name).also { it.writeText(content) }.path

    /** The arguments that say this request, or `null` if the command line cannot say it. */
    private fun argumentsOf(case: Case): List<String>? {
        val request = case.request
        val arguments = ArrayList<String>()
        fun option(flag: String, field: String) {
            request[field]?.let { arguments += listOf(flag, (it as JsonPrimitive).content) }
        }
        when (case.operation) {
            "validate" -> {
                arguments += listOf("lint", file("${case.id}.ics", text(request, "ics") ?: return null))
                // The contract checks RFC 5545 alone by default, and the command lints by default.
                arguments += listOf("--rules", text(request, "rules") ?: "rfc5545")
                option("--limit", "limit")
            }

            "fix" -> {
                val fixups = request["fixups"]?.jsonArray
                val ics = file("${case.id}.ics", text(request, "ics") ?: return null)
                when {
                    fixups == null -> arguments += listOf("fix", ics)

                    fixups.isEmpty() -> arguments += listOf("format", ics)

                    else ->
                        arguments +=
                            listOf("fix", ics, "--fixups", fixups.joinToString(",") { it.jsonPrimitive.content })
                }
            }

            "convert" -> {
                val ics = text(request, "ics")
                val jcal = request["jcal"]
                when {
                    ics != null && jcal == null -> arguments += listOf("convert", file("${case.id}.ics", ics))

                    ics == null && jcal != null ->
                        arguments +=
                            listOf("convert", file("${case.id}.json", jcal.toString()))

                    else -> return null
                }
            }

            "expand" -> {
                arguments += listOf("expand", file("${case.id}.ics", text(request, "ics") ?: return null))
                option("--from", "from")
                option("--to", "to")
                option("--zone", "zone")
                option("--limit", "limit")
                if (request["include_cancelled"]?.jsonPrimitive?.booleanOrNull ==
                    true
                ) {
                    arguments += "--include-cancelled"
                }
            }

            "diff" -> {
                val before = text(request, "before") ?: return null
                val after = text(request, "after") ?: return null
                arguments += listOf("diff", file("${case.id}.before.ics", before), file("${case.id}.after.ics", after))
                option("--limit", "limit")
            }

            "anonymize" -> {
                arguments += listOf("anonymize", file("${case.id}.ics", text(request, "ics") ?: return null))
                option("--shift-weeks", "shift_weeks")
            }

            "tz_resolve" -> {
                arguments += listOf("tz", text(request, "name") ?: return null)
                option("--at", "at")
            }

            "explain" -> arguments += listOf("explain", text(request, "code") ?: return null)

            else -> return null
        }
        return arguments + "--json"
    }

    private fun comparable(element: JsonElement, ignored: Set<String>): JsonElement = when (element) {
        is JsonObject -> JsonObject(
            element.filterKeys { it != "message" && it !in ignored }.mapValues { comparable(it.value, ignored) },
        )

        is JsonArray -> JsonArray(element.map { comparable(it, ignored) })

        else -> element
    }

    // The rules of the exit codes, applied to the expected response: that is the point, the code follows the answer.
    private fun expectedExit(case: Case): Int {
        val expected = case.expected
        if (expected["ok"]?.jsonPrimitive?.booleanOrNull != true) return 2
        val answer = Answer(expected.toString(), expected)
        val command = when (case.operation) {
            "validate" -> "lint"
            "tz_resolve" -> "tz"
            else -> case.operation
        }
        val name = if (case.operation == "fix" &&
            case.request["fixups"]?.jsonArray?.isEmpty() == true
        ) {
            "format"
        } else {
            command
        }
        if (hasNoCalendar(name, answer)) return 2
        if (hasFindings(command, answer, "warning")) return 1
        val lossy = command in LOSSY_COMMANDS && losesData(answer)
        return if (lossy || answer.truncated) 1 else 0
    }

    @Test
    fun everyCaseThatTheFlagsCanSayGivesTheExpectedJson() {
        val errors = ArrayList<String>()
        var compared = 0
        val covered = HashSet<String>()
        for (case in cases()) {
            val arguments = argumentsOf(case) ?: continue
            covered += case.operation
            val ran = cli(*arguments.toTypedArray())
            val answer = Json.parseToJsonElement(ran.text.trimEnd()).jsonObject
            assertTrue(ran.text.endsWith("\n") && ran.text.count { it == '\n' } == 1, "${case.id}: one line of JSON")
            if (case.id in NEEDS_CLOCK_OR_RANDOM) {
                checkByMeaning(case, answer)
            } else if (comparable(case.expected, case.ignored) != comparable(answer, case.ignored)) {
                errors += "${case.id}: $answer"
            } else {
                compared++
            }
            if (answer["ok"]?.jsonPrimitive?.booleanOrNull == true) {
                checker.check(answer, load("envelope"), "${case.id} envelope", errors)
                checker.check(answer.getValue("result"), load("${case.operation}.result"), "${case.id} result", errors)
            }
            assertEquals(expectedExit(case), ran.exit, "${case.id}: exit code, stderr ${ran.errors}")
            if (ran.exit == 2) assertTrue(ran.errors.startsWith("calendrum: "), "${case.id}: ${ran.errors}")
            if (ran.exit == 1 && case.expected["truncated"]?.jsonPrimitive?.booleanOrNull == true) {
                assertTrue("The output is incomplete" in ran.errors, "${case.id}: ${ran.errors}")
            }
        }
        assertEquals(emptyList(), errors)
        assertEquals(
            setOf("validate", "fix", "convert", "expand", "diff", "anonymize", "tz_resolve", "explain"),
            covered,
        )
        assertTrue(compared >= 30, "$compared cases")
    }

    private fun checkByMeaning(case: Case, answer: JsonObject) {
        val result = assertNotNull(answer["result"], case.id).jsonObject
        when (case.operation) {
            "anonymize" -> {
                val ics = result.getValue("ics").jsonPrimitive.content
                assertTrue("Salary" !in ics && "Alice" !in ics && "boss@example.com" !in ics, case.id)
                assertEquals(case.expected.getValue("result").jsonObject["shift_weeks"], result["shift_weeks"], case.id)
            }

            "tz_resolve" -> assertEquals("Asia/Tokyo", result.getValue("id").jsonPrimitive.contentOrNull, case.id)

            else -> error(case.id)
        }
    }

    private companion object {
        // The binary has a real clock and real random bytes, and no flag for tests.
        val NEEDS_CLOCK_OR_RANDOM = setOf("anonymize-shift", "anonymize-text-goes", "tz-resolve-default-moment")
    }
}

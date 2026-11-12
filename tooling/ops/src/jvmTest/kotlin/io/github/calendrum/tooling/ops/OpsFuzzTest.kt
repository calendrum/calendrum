package io.github.calendrum.tooling.ops

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

class OpsFuzzTest {
    // Whatever the input, a call returns one JSON object with `ok`. The operation comes from the input. A request is
    // either free text, which reaches the JSON checks, or a well-formed one with a calendar, which reaches the operation.
    @FuzzTest
    @DictionaryFile(resourcePath = "ops.dict")
    fun everyResponseIsJson(data: FuzzedDataProvider) {
        val operation = Ops.operations[data.consumeInt(0, Ops.operations.size - 1)]
        val mode = data.consumeInt(0, 2)
        val request = when (mode) {
            0 -> data.consumeRemainingAsString()

            1 -> {
                val zone = JsonPrimitive(data.consumeString(40))
                val limit = data.consumeInt(-1, 300)
                val text = JsonPrimitive(data.consumeRemainingAsString())
                """{"ics":$text,"before":$text,"after":$text,"from":"2026-01-01T00:00:00Z",""" +
                    """"to":"2027-01-01T00:00:00Z","zone":$zone,"limit":$limit,"name":$zone,"code":$zone,""" +
                    """"rrule":$text,"start":$zone,"summary":$text}"""
            }

            else -> {
                val first = JsonPrimitive(data.consumeString(60))
                val second = JsonPrimitive(data.consumeString(60))
                val text = JsonPrimitive(data.consumeRemainingAsString())
                """{"summary":$text,"start":$first,"end":$second,"duration":$second,"zone":$second,"rrule":$first,""" +
                    """"uid":$first,"organizer":$second,"attendees":[{"address":$first,"name":$text}],"jcal":$text}"""
            }
        }
        // A bug comes out as its own exception, which is what a finding needs.
        val response = Ops.unguarded(operation, request, ENVIRONMENT)
        val parsed = Json.parseToJsonElement(response).jsonObject
        check("ok" in parsed && "ops_version" in parsed) { "No envelope" }
    }

    private companion object {
        val ENVIRONMENT = OpsEnvironment(
            FIXED_ENVIRONMENT.clock,
            FIXED_ENVIRONMENT.randomBytes,
            OpsLimits(
                maxRequestChars = 1024 * 1024,
                maxInputBytes = 1024 * 1024,
                maxDepth = 64,
                defaultItems = 200,
                maxItems = 1000,
                maxResponseBytes = 1024L * 1024 * 4,
                maxProblems = 100,
                maxExpansionWork = 200_000,
                maxConflictOccurrences = 200,
            ),
        )
    }
}

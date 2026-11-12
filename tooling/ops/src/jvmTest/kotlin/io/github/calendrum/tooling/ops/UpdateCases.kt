package io.github.calendrum.tooling.ops

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.test.Test

class UpdateCases {
    // Writes the expected responses of the cases, for review before they stay: `-Pops.update=true --tests '*UpdateCases'`.
    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun update() {
        if (System.getProperty("calendrum.ops.update") != "true") return
        val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }
        for (case in allCases()) {
            val response = Ops.call(case.operation, case.request, FIXED_ENVIRONMENT)
            val text = json.encodeToString(Json.parseToJsonElement(response))
            File(CASES_PATH, "${case.id}/expected.json").writeText(text + "\n")
        }
    }
}

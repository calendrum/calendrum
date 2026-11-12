package io.github.calendrum.conformance

internal class EmbeddedFile(val name: String, val bytes: ByteArray)

internal class EmbeddedCase(val directory: String, val files: List<EmbeddedFile>) {
    fun file(name: String): EmbeddedFile? = files.firstOrNull { it.name == name }
}

internal fun bytes(vararg chunks: String): ByteArray {
    var size = 0
    for (chunk in chunks) size += chunk.length
    val out = ByteArray(size)
    var index = 0
    for (chunk in chunks) {
        for (char in chunk) out[index++] = char.code.toByte()
    }
    return out
}

internal object Suite {
    const val INPUT = "input.ics"
    const val TREE = "expected.tree.json"
    const val DIAGNOSTICS = "diagnostics.json"
    const val FINGERPRINTS = "expected.fingerprints.json"
    const val JCAL = "expected.jcal.json"
    const val META = "meta.json"
    const val META_SCHEMA = "meta.schema.json"
    const val TREE_SCHEMA = "expected.tree.schema.json"
    const val DIAGNOSTICS_SCHEMA = "diagnostics.schema.json"
    const val FINGERPRINTS_SCHEMA = "expected.fingerprints.schema.json"
    const val JCAL_SCHEMA = "expected.jcal.schema.json"
    const val FINGERPRINTS_TAG = "fingerprints"
    const val UPDATE_COMMAND = "./gradlew :conformance:updateExpected"

    val caseFiles: List<String> = listOf(DIAGNOSTICS, TREE, INPUT, META)

    val schemaFiles: Map<String, String> = mapOf(
        TREE to TREE_SCHEMA,
        DIAGNOSTICS to DIAGNOSTICS_SCHEMA,
        META to META_SCHEMA,
        FINGERPRINTS to FINGERPRINTS_SCHEMA,
        JCAL to JCAL_SCHEMA,
    )

    val cases: List<EmbeddedCase> by lazy { embeddedCases() }

    val schemaDocuments: List<EmbeddedFile> by lazy { embeddedSchemas() }

    val schemas: Map<String, JsonSchema> by lazy {
        schemaDocuments.associate { it.name to JsonSchema(readJson(it.bytes).value as JsonObject) }
    }

    fun schemaFor(file: String): JsonSchema = schemas.getValue(schemaFiles.getValue(file))
}

internal class JsonFile(val value: Json?, val problems: List<String>)

// A file is expected to already be in canonical form (conformance/README.md): re-serializing the parsed value and
// comparing to the original bytes both validates the JSON and catches formatting drift in one pass.
internal fun readJson(bytes: ByteArray): JsonFile {
    val text = try {
        bytes.decodeToString(throwOnInvalidSequence = true)
    } catch (e: CharacterCodingException) {
        return JsonFile(null, listOf("not valid UTF-8"))
    }
    if (text.startsWith('\uFEFF')) return JsonFile(null, listOf("starts with a byte order mark"))
    val value = try {
        parseJson(text)
    } catch (e: JsonSyntaxException) {
        return JsonFile(null, listOf("not valid JSON: ${e.message}"))
    }
    val canonical = value.toCanonicalJson()
    val problems = if (canonical == text) {
        emptyList()
    } else {
        listOf("not in the canonical form (${textDifference(canonical, text)})")
    }
    return JsonFile(value, problems)
}

private val caseId = Regex("^[a-z0-9]+(-[a-z0-9]+)*$")

internal fun layoutProblems(case: EmbeddedCase): List<String> {
    val problems = ArrayList<String>()
    if (!caseId.matches(case.directory)) problems += "directory name is not kebab-case"
    for (name in Suite.caseFiles) {
        if (case.file(name) == null) problems += "missing $name"
    }
    for (file in case.files) {
        if (file.name !in Suite.caseFiles &&
            file.name != Suite.FINGERPRINTS &&
            file.name != Suite.JCAL
        ) {
            problems += "unexpected entry ${file.name}"
        }
    }
    val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject
    val tagged = meta != null && hasFingerprintsTag(meta)
    val present = case.file(Suite.FINGERPRINTS) != null
    if (tagged && !present) problems += "missing ${Suite.FINGERPRINTS} for the ${Suite.FINGERPRINTS_TAG} tag"
    if (!tagged && present) problems += "${Suite.FINGERPRINTS} without the ${Suite.FINGERPRINTS_TAG} tag"
    val undefined = meta != null && jcalUndefined(meta)
    val reason = meta?.get("jcalReason") != null
    val jcal = case.file(Suite.JCAL) != null
    if (undefined && jcal) problems += "${Suite.JCAL} although ${Suite.META} says jcal: false"
    if (!undefined && !jcal) problems += "missing ${Suite.JCAL} (or jcal: false with a jcalReason in ${Suite.META})"
    if (undefined != reason) problems += "jcal: false and jcalReason come together in ${Suite.META}"
    return problems
}

// A case has `expected.jcal.json` unless `meta.json` says `jcal: false`; the layout check enforces both directions and
// requires `jcalReason` with the flag.
internal fun jcalUndefined(meta: JsonObject): Boolean = meta["jcal"] == JsonBoolean(false)

// A case tagged `fingerprints` must have `expected.fingerprints.json` and only such a case may have it; the layout
// check enforces both directions.
internal fun hasFingerprintsTag(meta: JsonObject): Boolean =
    (meta["tags"] as? JsonArray)?.items?.any { (it as? JsonString)?.value == Suite.FINGERPRINTS_TAG } == true

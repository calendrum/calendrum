package io.github.calendrum.conformance

internal fun jsonDifferences(expected: Json, actual: Json, limit: Int = DIFF_LIMIT): List<String> {
    val out = ArrayList<String>()
    compare(expected, actual, "$", out, limit)
    return out
}

internal fun textDifference(expected: String, actual: String): String {
    val expectedLines = expected.split('\n')
    val actualLines = actual.split('\n')
    for (index in 0 until maxOf(expectedLines.size, actualLines.size)) {
        val left = expectedLines.getOrNull(index)
        val right = actualLines.getOrNull(index)
        if (left != right) {
            return "line ${index + 1}: expected ${quoteLine(left)}, actual ${quoteLine(right)}"
        }
    }
    return "no difference"
}

private fun compare(expected: Json, actual: Json, path: String, out: MutableList<String>, limit: Int) {
    if (out.size >= limit) return
    when {
        expected is JsonObject && actual is JsonObject -> compareObjects(expected, actual, path, out, limit)
        expected is JsonArray && actual is JsonArray -> compareArrays(expected, actual, path, out, limit)
        expected != actual -> out += "$path: expected ${shorten(expected)}, actual ${shorten(actual)}"
    }
}

private fun compareObjects(
    expected: JsonObject,
    actual: JsonObject,
    path: String,
    out: MutableList<String>,
    limit: Int,
) {
    for ((key, value) in expected.members) {
        val other = actual[key]
        if (other == null) {
            if (out.size < limit) out += "$path.$key: missing, expected ${shorten(value)}"
        } else {
            compare(value, other, "$path.$key", out, limit)
        }
    }
    for ((key, value) in actual.members) {
        if (expected[key] == null && out.size < limit) out += "$path.$key: unexpected, actual ${shorten(value)}"
    }
    val common = expected.keys.filter { actual[it] != null }
    if (common != actual.keys.filter { expected[it] != null } && out.size < limit) {
        out += "$path: keys in a different order: expected $common, actual ${actual.keys}"
    }
}

private fun compareArrays(expected: JsonArray, actual: JsonArray, path: String, out: MutableList<String>, limit: Int) {
    val common = minOf(expected.items.size, actual.items.size)
    for (index in 0 until common) compare(expected.items[index], actual.items[index], "$path[$index]", out, limit)
    if (expected.items.size != actual.items.size && out.size < limit) {
        out += "$path: expected ${expected.items.size} items, actual ${actual.items.size}"
    }
    for (index in common until expected.items.size) {
        if (out.size < limit) out += "$path[$index]: missing, expected ${shorten(expected.items[index])}"
    }
    for (index in common until actual.items.size) {
        if (out.size < limit) out += "$path[$index]: unexpected, actual ${shorten(actual.items[index])}"
    }
}

private fun shorten(value: Json): String {
    val text = value.toCompactJson()
    return if (text.length <= SHORT_LIMIT) text else text.take(SHORT_LIMIT) + "..."
}

private fun quoteLine(line: String?): String = if (line == null) "end of file" else "`$line`"

private const val DIFF_LIMIT = 20
private const val SHORT_LIMIT = 200

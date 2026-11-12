package io.github.calendrum.tooling.mcp

import io.github.calendrum.tooling.platform.FileRead
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.io.encoding.Base64

// The sentences here are fixed, and no path or value of a request goes into them. The same answer to a file that does
// not exist and to a file outside the roots keeps a caller from looking around the disk.
internal const val PATH_DENIED_MESSAGE =
    "The file cannot be read: it does not exist, it is outside the allowed roots, or it is not a calendar file"
internal const val PATH_UNSUPPORTED_MESSAGE =
    "The field path is not available on this platform: give the calendar as content"

/** A call that does not reach the operation. [text] is a fixed sentence. */
internal class ToolFailure(val code: String, val text: String) : Exception(text)

/**
 * The request of an operation from the arguments of a tool. A file is read here, and its bytes go as `<field>_base64`:
 * the operations know nothing about files.
 *
 * @param read gives the file at a path. It can throw [ToolFailure].
 * @throws ToolFailure if the arguments do not say which calendar to take.
 */
internal fun requestOf(spec: ToolSpec, arguments: JsonObject, read: (String) -> FileRead): String {
    val request = LinkedHashMap<String, JsonElement>(arguments)
    for (calendar in spec.calendars) {
        // These names belong to the operation. A tool has other ones, and a value here would go around the checks
        // below.
        request.remove(calendar.operationField)
        request.remove(calendar.operationBase64Field)
        request.remove(calendar.argument)
        request.remove(calendar.base64Argument)
        request.remove(calendar.pathArgument)
    }
    for (calendar in spec.calendars) {
        val text = textField(arguments, calendar.argument)
        val encoded = textField(arguments, calendar.base64Argument)
        val path = textField(arguments, calendar.pathArgument)
        val given = listOfNotNull(text, encoded, path).size
        if (given > 1) throw badArgument("${namesOf(calendar)}: give only one of them")
        when {
            text != null -> request[calendar.operationField] = JsonPrimitive(text)

            encoded != null -> request[calendar.operationBase64Field] = JsonPrimitive(encoded)

            path != null -> request[calendar.operationBase64Field] = JsonPrimitive(encodedFile(read(path)))

            // A jCal is the other input of `convert`.
            spec.operation != "convert" || arguments["jcal"].isAbsent() -> {
                throw badArgument("${namesOf(calendar)}: give one of them")
            }
        }
    }
    return JsonObject(request).toString()
}

private fun namesOf(calendar: CalendarArgument): String =
    "${calendar.argument}, ${calendar.base64Argument} and ${calendar.pathArgument}"

private fun JsonElement?.isAbsent(): Boolean = this == null || this is JsonNull

private fun textField(arguments: JsonObject, name: String): String? {
    val element = arguments[name]
    if (element.isAbsent()) return null
    if (element !is JsonPrimitive || !element.isString) throw badArgument("The field $name must be a string")
    return element.content
}

private fun badArgument(text: String) = ToolFailure("OPS-BAD-ARGUMENT", text)

private fun encodedFile(read: FileRead): String = when (read) {
    is FileRead.Bytes -> Base64.encode(read.bytes)
    FileRead.Denied -> throw ToolFailure("MCP-PATH-DENIED", PATH_DENIED_MESSAGE)
    FileRead.TooLarge -> throw ToolFailure("OPS-TOO-LARGE", "The calendar is larger than the limit")
}

/** A response of the contract as the result of a tool: on success the whole envelope, on failure only the error. */
internal fun resultOf(response: String): CallToolResult {
    val envelope = Json.parseToJsonElement(response).jsonObject
    val error = envelope["error"]
    return if (error == null) {
        // Twice on purpose: MCP asks for the text as well, for clients that do not read structured content.
        CallToolResult(content = listOf(TextContent(response)), isError = false, structuredContent = envelope)
    } else {
        CallToolResult(content = listOf(TextContent(error.toString())), isError = true)
    }
}

/** The failure of a call as the result of a tool, in the form of an error of the contract. */
internal fun resultOf(failure: ToolFailure): CallToolResult {
    val error = JsonObject(mapOf("code" to JsonPrimitive(failure.code), "message" to JsonPrimitive(failure.text)))
    return CallToolResult(content = listOf(TextContent(error.toString())), isError = true)
}

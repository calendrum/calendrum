package io.github.calendrum.tooling.mcp

import io.github.calendrum.tooling.ops.Ops
import io.modelcontextprotocol.kotlin.sdk.types.ListResourceTemplatesResult
import io.modelcontextprotocol.kotlin.sdk.types.ListResourcesResult
import io.modelcontextprotocol.kotlin.sdk.types.McpException
import io.modelcontextprotocol.kotlin.sdk.types.RPCError
import io.modelcontextprotocol.kotlin.sdk.types.ReadResourceResult
import io.modelcontextprotocol.kotlin.sdk.types.Resource
import io.modelcontextprotocol.kotlin.sdk.types.ResourceTemplate
import io.modelcontextprotocol.kotlin.sdk.types.TextResourceContents
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

/** The public guides, as they are in the repository, and the table of problem codes that `explain` uses. */
internal object Resources {
    private const val GUIDES = "calendrum://guides/"
    private const val DIAGNOSTICS = "calendrum://diagnostics/"

    fun list(): ListResourcesResult = ListResourcesResult(
        GUIDE_NAMES.map { name ->
            Resource(
                uri = GUIDES + name,
                name = name,
                description = "A guide to the library and its tools.",
                mimeType = "text/markdown",
            )
        },
    )

    fun templates(): ListResourceTemplatesResult = ListResourceTemplatesResult(
        listOf(
            ResourceTemplate(
                uriTemplate = DIAGNOSTICS + "{code}",
                name = "diagnostics",
                description = "The severity, the constant and the meaning of a problem code such as ICAL-S003.",
                mimeType = "application/json",
            ),
        ),
    )

    /** @throws McpException if there is no such resource. The answer does not quote the request. */
    fun read(uri: String): ReadResourceResult {
        val contents = when {
            uri.startsWith(GUIDES) -> guideFile(uri.removePrefix(GUIDES))?.let {
                TextResourceContents(text = it, uri = uri, mimeType = "text/markdown")
            }

            uri.startsWith(DIAGNOSTICS) -> diagnostic(uri.removePrefix(DIAGNOSTICS))?.let {
                TextResourceContents(text = it, uri = uri, mimeType = "application/json")
            }

            else -> null
        }
        contents ?: throw McpException(RPCError.ErrorCode.RESOURCE_NOT_FOUND, "There is no such resource")
        return ReadResourceResult(listOf(contents))
    }

    private fun diagnostic(code: String): String? {
        val request = JsonObject(mapOf("code" to JsonPrimitive(code))).toString()
        val envelope = Json.parseToJsonElement(Ops.call("explain", request)).jsonObject
        return envelope["result"]?.toString()
    }
}

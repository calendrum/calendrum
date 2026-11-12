package io.github.calendrum.tooling.mcp

import io.modelcontextprotocol.kotlin.sdk.shared.AbstractTransport
import io.modelcontextprotocol.kotlin.sdk.shared.TransportSendOptions
import io.modelcontextprotocol.kotlin.sdk.shared.serializeMessage
import io.modelcontextprotocol.kotlin.sdk.types.JSONRPCEmptyMessage
import io.modelcontextprotocol.kotlin.sdk.types.JSONRPCMessage
import io.modelcontextprotocol.kotlin.sdk.types.McpException
import io.modelcontextprotocol.kotlin.sdk.types.McpJson
import io.modelcontextprotocol.kotlin.sdk.types.RPCError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.buffered
import kotlinx.io.indexOf
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlin.coroutines.CoroutineContext

/**
 * JSON-RPC over standard input and output: one message on a line.
 *
 * `StdioServerTransport` of kotlin-sdk does not fit. It answers nothing to a line that is not JSON-RPC, so a client
 * waits for its timeout, and it closes the connection on a line above 16 MiB, a size it does not let change, while a
 * calendar in a request can reach 64 MiB. Here both get an error and the server goes on. Replies quote nothing of what
 * came in.
 *
 * @param context runs the reading and the writing and needs two threads, so that a read waiting for input does not
 *   hold up a write.
 */
internal class StdioTransport(
    private val input: RawSource,
    output: RawSink,
    context: CoroutineContext,
    private val maxMessageBytes: Long,
) : AbstractTransport() {
    private val sink = output.buffered()
    private val outgoing = Channel<String>(Channel.BUFFERED)
    private val scope = CoroutineScope(SupervisorJob() + context)
    private var writer: Job? = null

    override suspend fun start() {
        writer = scope.launch { writeAll() }
        scope.launch { readAll() }
    }

    override suspend fun send(message: JSONRPCMessage, options: TransportSendOptions?) {
        enqueue(serializeMessage(message))
    }

    override suspend fun close() {
        outgoing.close()
        writer?.join()
    }

    private suspend fun enqueue(line: String) {
        try {
            outgoing.send(line)
        } catch (cause: ClosedSendChannelException) {
            throw McpException(RPCError.ErrorCode.CONNECTION_CLOSED, "The connection is closed", cause = cause)
        }
    }

    private suspend fun writeAll() {
        try {
            for (line in outgoing) {
                sink.writeString(line)
                sink.flush()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Standard output ends the process by itself: this is for other sinks.
        } finally {
            invokeOnCloseCallback()
        }
    }

    private suspend fun readAll() {
        val frames = Frames(maxMessageBytes)
        val chunk = Buffer()
        try {
            while (input.readAtMostTo(chunk, READ_SIZE) >= 0) {
                while (true) {
                    val frame = frames.next(chunk) ?: break
                    handle(frame)
                }
            }
        } finally {
            outgoing.close()
        }
    }

    private suspend fun handle(frame: Frame) {
        when (frame) {
            is Frame.TooLong -> replyWithoutId(
                RPCError.ErrorCode.INVALID_REQUEST,
                "The message is larger than the limit",
            )

            is Frame.Line -> {
                if (frame.text.isBlank()) return
                val message = decode(frame.text) ?: return
                try {
                    _onMessage(message)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    // A message that cannot be processed must not take the following ones with it.
                }
            }
        }
    }

    private suspend fun decode(line: String): JSONRPCMessage? {
        // Encoding and decoding a message are recursive, and a deep one would overflow the stack: a crash, not an
        // exception.
        if (!isWithinDepth(line, MAX_DEPTH)) {
            replyWithoutId(RPCError.ErrorCode.INVALID_REQUEST, "The message is nested too deep")
            return null
        }
        val element = try {
            Json.parseToJsonElement(line)
        } catch (_: SerializationException) {
            replyWithoutId(RPCError.ErrorCode.PARSE_ERROR, "The message is not valid JSON")
            return null
        }
        val message = try {
            McpJson.decodeFromJsonElement(JSONRPCMessage.serializer(), withoutForeignRoots(element))
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            null
        }
        if (message != null && message !is JSONRPCEmptyMessage) return message
        // A broken answer to a request of the server gets no reply. Whoever waits for that answer times out.
        if (element is JsonObject && ("result" in element || "error" in element)) return null
        replyTo(element, RPCError.ErrorCode.INVALID_REQUEST, "The message is not a valid JSON-RPC message")
        return null
    }

    private suspend fun replyTo(element: JsonElement, code: Int, text: String) {
        val id = (element as? JsonObject)?.get("id") as? JsonPrimitive
        if (id != null &&
            (id.isString || id.longOrNull != null)
        ) {
            enqueue(errorLine(id.toString(), code, text))
        } else {
            replyWithoutId(code, text)
        }
    }

    // JSON-RPC asks for an id of null where the id cannot be read, and the serializer of the SDK leaves a null out.
    private suspend fun replyWithoutId(code: Int, text: String) {
        enqueue(errorLine("null", code, text))
    }

    private fun errorLine(id: String, code: Int, text: String): String =
        """{"jsonrpc":"2.0","id":$id,"error":{"code":$code,"message":${JsonPrimitive(text)}}}""" + "\n"

    private companion object {
        const val READ_SIZE = 65536L

        // 64 for the request of an operation (OpsLimits.maxDepth) and the levels around the arguments of a tool.
        const val MAX_DEPTH = 72
    }
}

internal sealed class Frame {
    class Line(val text: String) : Frame()

    object TooLong : Frame()
}

/** Cuts a stream of bytes into lines. A line above the limit is thrown away as it comes, not kept in memory. */
internal class Frames(private val maxBytes: Long) {
    private val line = Buffer()
    private var tooLong = false

    /** The next line that [chunk] completes, or `null` when it has been used up. */
    fun next(chunk: Buffer): Frame? {
        if (chunk.exhausted()) return null
        val end = chunk.indexOf(NEWLINE)
        val size = if (end < 0) chunk.size else end
        if (tooLong || line.size + size > maxBytes) {
            tooLong = true
            line.clear()
            chunk.skip(size)
        } else {
            line.write(chunk, size)
        }
        if (end < 0) return null
        chunk.skip(1)
        if (tooLong) {
            tooLong = false
            return Frame.TooLong
        }
        return Frame.Line(line.readString().removeSuffix("\r"))
    }

    private companion object {
        const val NEWLINE = '\n'.code.toByte()
    }
}

// `Root` of the SDK refuses a URI without file://, and that fails the whole list: the good roots beside it would be
// lost.
internal fun withoutForeignRoots(element: JsonElement): JsonElement {
    val message = element as? JsonObject ?: return element
    val result = message["result"] as? JsonObject ?: return element
    val roots = result["roots"] as? JsonArray ?: return element
    val kept = roots.filter { root ->
        val uri = (root as? JsonObject)?.get("uri") as? JsonPrimitive
        uri != null && uri.isString && uri.content.startsWith("file://")
    }
    if (kept.size == roots.size) return element
    return JsonObject(message + ("result" to JsonObject(result + ("roots" to JsonArray(kept)))))
}

/** Whether the arrays and objects of [json] are nested at most [maxDepth] levels. It need not be valid JSON. */
internal fun isWithinDepth(json: String, maxDepth: Int): Boolean {
    var depth = 0
    var inString = false
    var index = 0
    while (index < json.length) {
        val char = json[index]
        if (inString) {
            if (char == '\\') {
                index++
            } else if (char == '"') {
                inString = false
            }
        } else {
            when (char) {
                '"' -> inString = true

                '{', '[' -> {
                    depth++
                    if (depth > maxDepth) return false
                }

                '}', ']' -> depth--
            }
        }
        index++
    }
    return true
}

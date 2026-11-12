@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)

package io.github.calendrum.tooling.sdkc

import io.github.calendrum.tooling.ops.Ops
import io.github.calendrum.tooling.ops.OpsEnvironment
import io.github.calendrum.tooling.ops.OpsLimits
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.set
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.CName

// The ABI of include/calendrum.h. Every target is 64-bit, so size_t is ULong on all of them.

private const val ABI_VERSION = 1

private const val OK = 0
private const val BAD_ARGUMENT = 1
private const val INTERNAL = 2

// Longer than the name of any operation: a longer string is not one, and it is not worth copying to find that out.
private const val LONGEST_OPERATION = 64

/** Returns the version of the ABI. It allocates nothing, so there is nothing to catch. */
@CName("calendrum_abi_version")
public fun abiVersion(): Int = ABI_VERSION

/** Writes the versions as JSON to [outJson] and its length, without the final zero, to [outLen]. */
@CName("calendrum_version")
public fun version(outJson: CPointer<CPointerVar<UByteVar>>?, outLen: CPointer<ULongVar>?): Int =
    answering(outJson, outLen) {
        """{"abi":$ABI_VERSION,"contract":${Ops.VERSION},"library":"$LIBRARY_VERSION","sdk":"$SDK_VERSION"}"""
    }

/**
 * Runs one operation of the contract. The answer is what [Ops.call] returns, in UTF-8 with a final zero that [outLen] does
 * not count; the reason of a failed operation is in it, and the code says only that it was written.
 */
@CName("calendrum_call")
public fun call(
    operation: CPointer<ByteVar>?,
    request: CPointer<UByteVar>?,
    requestLen: ULong,
    outResponse: CPointer<CPointerVar<UByteVar>>?,
    outLen: CPointer<ULongVar>?,
): Int = answering(outResponse, outLen) {
    if (operation == null || (request == null && requestLen != 0uL)) {
        null
    } else {
        respond(operation, request, requestLen)
    }
}

/** Frees what the other functions returned. It allocates nothing, so there is nothing to catch. */
@CName("calendrum_free")
public fun free(data: CPointer<UByteVar>?) {
    platform.posix.free(data)
}

// A body that gives null refuses its arguments. Nothing thrown may cross into C, and the text of an exception can quote the
// input, so a failure is only a number.
private inline fun answering(
    out: CPointer<CPointerVar<UByteVar>>?,
    outLen: CPointer<ULongVar>?,
    body: () -> String?,
): Int {
    out?.pointed?.value = null
    outLen?.pointed?.value = 0uL
    if (out == null || outLen == null) return BAD_ARGUMENT
    return try {
        val text = body() ?: return BAD_ARGUMENT
        write(text, out, outLen)
    } catch (_: Throwable) {
        INTERNAL
    }
}

private fun write(text: String, out: CPointer<CPointerVar<UByteVar>>, outLen: CPointer<ULongVar>): Int {
    val bytes = text.encodeToByteArray()
    val memory = platform.posix.malloc((bytes.size + 1).convert())?.reinterpret<UByteVar>() ?: return INTERNAL
    if (bytes.isNotEmpty()) {
        bytes.usePinned { platform.posix.memcpy(memory, it.addressOf(0), bytes.size.convert()) }
    }
    memory[bytes.size] = 0u
    out.pointed.value = memory
    outLen.pointed.value = bytes.size.toULong()
    return OK
}

private fun respond(operation: CPointer<ByteVar>, request: CPointer<UByteVar>?, length: ULong): String {
    // The envelopes of a request that never reaches the operation come from Ops as well, so that they cannot differ.
    val name = operationName(operation)
    if (isTooLarge(request, length)) return Ops.call(name, "{}", REFUSING_ALL)
    // Not UTF-8 is not JSON, and the empty request makes Ops say so itself.
    val text = request?.let { decodedOrNull(it.reinterpret(), length.toInt()) }.orEmpty()
    return Ops.call(name, text)
}

private fun decodedOrNull(bytes: CPointer<ByteVar>, length: Int): String? = try {
    bytes.readBytes(length).decodeToString(0, length, throwOnInvalidSequence = true)
} catch (_: CharacterCodingException) {
    null
}

// A name that is not UTF-8 or is too long gives the empty name, and there is no such operation.
private fun operationName(operation: CPointer<ByteVar>): String {
    var length = 0
    while (length <= LONGEST_OPERATION && operation[length] != 0.toByte()) length++
    if (length > LONGEST_OPERATION) return ""
    return decodedOrNull(operation, length).orEmpty()
}

// The limit counts UTF-16 units, and a unit takes one to three bytes of UTF-8. Up to the limit in bytes a request is
// certainly within it, above three times the limit certainly not, and only in between the bytes are read: not copied.
private fun isTooLarge(request: CPointer<UByteVar>?, length: ULong): Boolean {
    val limit = OpsLimits.Default.maxRequestChars.toULong()
    if (length <= limit) return false
    if (length > limit * 3uL) return true
    val bytes = request ?: return false
    var units = 0uL
    var index = 0L
    while (index < length.toLong()) {
        val byte = bytes[index].toInt()
        if (byte and 0xC0 != 0x80) units += if (byte >= 0xF0) 2uL else 1uL
        index++
    }
    return units > limit
}

private val REFUSING_ALL: OpsEnvironment = OpsLimits.Default.let { limits ->
    OpsEnvironment(
        OpsEnvironment.Default.clock,
        OpsEnvironment.Default.randomBytes,
        OpsLimits(
            maxRequestChars = 0,
            maxInputBytes = limits.maxInputBytes,
            maxDepth = limits.maxDepth,
            defaultItems = limits.defaultItems,
            maxItems = limits.maxItems,
            maxResponseBytes = limits.maxResponseBytes,
            maxProblems = limits.maxProblems,
            maxExpansionWork = limits.maxExpansionWork,
            maxConflictOccurrences = limits.maxConflictOccurrences,
        ),
    )
}

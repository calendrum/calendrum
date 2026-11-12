@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.cinterop.wcstr
import platform.posix.getpid
import platform.windows.CREATE_NEW
import platform.windows.CloseHandle
import platform.windows.CreateFileW
import platform.windows.DWORDVar
import platform.windows.DeleteFileW
import platform.windows.FILE_ATTRIBUTE_DIRECTORY
import platform.windows.FILE_ATTRIBUTE_NORMAL
import platform.windows.FILE_SHARE_DELETE
import platform.windows.FILE_SHARE_READ
import platform.windows.FILE_SHARE_WRITE
import platform.windows.FILE_TYPE_DISK
import platform.windows.FlushFileBuffers
import platform.windows.GENERIC_READ
import platform.windows.GENERIC_WRITE
import platform.windows.GetFileAttributesW
import platform.windows.GetFileSizeEx
import platform.windows.GetFileType
import platform.windows.GetLastError
import platform.windows.INVALID_FILE_ATTRIBUTES
import platform.windows.INVALID_HANDLE_VALUE
import platform.windows.LARGE_INTEGER
import platform.windows.MOVEFILE_REPLACE_EXISTING
import platform.windows.MOVEFILE_WRITE_THROUGH
import platform.windows.MoveFileExW
import platform.windows.OPEN_EXISTING
import platform.windows.ReadFile
import platform.windows.WriteFile

// None of this has run: the development host is not Windows, and the code is checked by linking only. The paths go to the
// system in UTF-16 (wcstr), so that a name that is not ASCII is the name the person typed.
public actual fun readUserFile(path: String, maxBytes: Int): UserFileRead {
    if (path.isEmpty()) return UserFileRead.Failed("No such file or directory")
    val attributes = GetFileAttributesW(path)
    if (attributes != INVALID_FILE_ATTRIBUTES && (attributes and FILE_ATTRIBUTE_DIRECTORY.convert()) != 0u) {
        return UserFileRead.Failed("Is a directory")
    }
    val handle = CreateFileW(
        path,
        GENERIC_READ.convert(),
        (FILE_SHARE_READ or FILE_SHARE_WRITE or FILE_SHARE_DELETE).convert(),
        null,
        OPEN_EXISTING.convert(),
        FILE_ATTRIBUTE_NORMAL.convert(),
        null,
    )
    if (handle == INVALID_HANDLE_VALUE) return UserFileRead.Failed(reasonOfError(GetLastError()))
    try {
        // A pipe, a console or a device would make ReadFile wait.
        if (GetFileType(handle) != FILE_TYPE_DISK.convert<UInt>()) return UserFileRead.Failed("Not a regular file")
        val size = memScoped {
            val value = alloc<LARGE_INTEGER>()
            if (GetFileSizeEx(handle, value.ptr) == 0) return UserFileRead.Failed(reasonOfError(GetLastError()))
            value.QuadPart
        }
        if (size > maxBytes) return UserFileRead.TooLarge
        var bytes = ByteArray(maxOf(size.toInt(), MIN_BUFFER).coerceAtMost(maxBytes))
        var filled = 0
        while (true) {
            if (filled == bytes.size) {
                if (filled >= maxBytes) {
                    return if (readChunk(handle, ByteArray(1), 0, 1) >
                        0
                    ) {
                        UserFileRead.TooLarge
                    } else {
                        UserFileRead.Bytes(bytes)
                    }
                }
                bytes = bytes.copyOf(minOf(maxBytes.toLong(), bytes.size * 2L).toInt())
            }
            val count = readChunk(handle, bytes, filled, bytes.size - filled)
            if (count < 0) return UserFileRead.Failed("The file cannot be read")
            if (count == 0) return UserFileRead.Bytes(bytes.copyOf(filled))
            filled += count
        }
    } finally {
        CloseHandle(handle)
    }
}

private fun readChunk(handle: kotlinx.cinterop.COpaquePointer?, bytes: ByteArray, offset: Int, size: Int): Int =
    memScoped {
        val read = alloc<DWORDVar>()
        val ok = bytes.usePinned { ReadFile(handle, it.addressOf(offset), size.convert(), read.ptr, null) }
        if (ok == 0) -1 else read.value.toInt()
    }

public actual fun writeFileAtomically(path: String, bytes: ByteArray, replace: Boolean): FileWrite {
    if (path.isEmpty()) return FileWrite.Failed("No such file or directory")
    val directory = directoryOf(path)
    val separator = if (path.length > directory.length &&
        (path[directory.length] == '\\' || path[directory.length] == '/')
    ) {
        path[directory.length].toString()
    } else {
        "\\"
    }
    val stem = temporaryStem(path)
    val process = getpid()
    var temporary: String? = null
    var handle: kotlinx.cinterop.COpaquePointer? = null
    for (attempt in 0 until TEMPORARY_ATTEMPTS) {
        val name = (if (directory.endsWith("\\") || directory.endsWith("/")) directory else directory + separator) +
            ".$stem.calendrum-$process-$attempt.tmp"
        // CREATE_NEW: a name that is taken is never opened.
        val opened =
            CreateFileW(
                name,
                GENERIC_WRITE.convert(),
                0u,
                null,
                CREATE_NEW.convert(),
                FILE_ATTRIBUTE_NORMAL.convert(),
                null,
            )
        if (opened != INVALID_HANDLE_VALUE) {
            temporary = name
            handle = opened
            break
        }
        val code = GetLastError()
        if (code != ERROR_FILE_EXISTS && code != ERROR_ALREADY_EXISTS) return FileWrite.Failed(reasonOfError(code))
    }
    if (temporary == null || handle == null) return FileWrite.Failed("The file cannot be written")
    var done = false
    try {
        var offset = 0
        while (offset < bytes.size) {
            val written = memScoped {
                val count = alloc<DWORDVar>()
                val ok = bytes.usePinned {
                    WriteFile(handle, it.addressOf(offset), (bytes.size - offset).convert(), count.ptr, null)
                }
                if (ok == 0) -1 else count.value.toInt()
            }
            if (written <= 0) return FileWrite.Failed(reasonOfError(GetLastError()))
            offset += written
        }
        if (FlushFileBuffers(handle) == 0) return FileWrite.Failed(reasonOfError(GetLastError()))
        val closed = CloseHandle(handle)
        handle = null
        if (closed == 0) return FileWrite.Failed(reasonOfError(GetLastError()))
        // Without MOVEFILE_REPLACE_EXISTING the move fails if the name is taken, and it does that in one step.
        val flags = if (replace) MOVEFILE_REPLACE_EXISTING or MOVEFILE_WRITE_THROUGH else MOVEFILE_WRITE_THROUGH
        if (MoveFileExW(temporary, path, flags.convert()) == 0) {
            val code = GetLastError()
            return if (!replace && (code == ERROR_FILE_EXISTS || code == ERROR_ALREADY_EXISTS)) {
                FileWrite.Exists
            } else {
                FileWrite.Failed(reasonOfError(code))
            }
        }
        done = true
        return FileWrite.Done
    } finally {
        if (handle != null) CloseHandle(handle)
        if (!done) DeleteFileW(temporary)
    }
}

private fun reasonOfError(code: UInt): String = when (code) {
    ERROR_FILE_NOT_FOUND, ERROR_PATH_NOT_FOUND -> "No such file or directory"
    ERROR_ACCESS_DENIED -> "Permission denied"
    ERROR_SHARING_VIOLATION -> "The file is in use"
    ERROR_FILE_EXISTS, ERROR_ALREADY_EXISTS -> "File exists"
    ERROR_DISK_FULL -> "No space left on device"
    else -> "Windows error $code"
}

private const val ERROR_FILE_NOT_FOUND = 2u
private const val ERROR_PATH_NOT_FOUND = 3u
private const val ERROR_ACCESS_DENIED = 5u
private const val ERROR_SHARING_VIOLATION = 32u
private const val ERROR_FILE_EXISTS = 80u
private const val ERROR_DISK_FULL = 112u
private const val ERROR_ALREADY_EXISTS = 183u
private const val MIN_BUFFER = 4096
private const val TEMPORARY_ATTEMPTS = 100

@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.EEXIST
import platform.posix.EINTR
import platform.posix.ENOENT
import platform.posix.O_CLOEXEC
import platform.posix.O_CREAT
import platform.posix.O_EXCL
import platform.posix.O_NONBLOCK
import platform.posix.O_RDONLY
import platform.posix.O_WRONLY
import platform.posix.S_IFDIR
import platform.posix.S_IFMT
import platform.posix.S_IFREG
import platform.posix.close
import platform.posix.errno
import platform.posix.fchmod
import platform.posix.fstat
import platform.posix.fsync
import platform.posix.getpid
import platform.posix.link
import platform.posix.lstat
import platform.posix.open
import platform.posix.rename
import platform.posix.stat
import platform.posix.strerror
import platform.posix.unlink
import platform.posix.write

internal fun reasonOf(code: Int): String = strerror(code)?.toKString() ?: "Error $code"

public actual fun readUserFile(path: String, maxBytes: Int): UserFileRead {
    if (path.isEmpty()) return UserFileRead.Failed(reasonOf(ENOENT))
    var fd: Int
    while (true) {
        // O_NONBLOCK keeps open() from waiting for the writer of a FIFO; on a regular file it changes nothing. A link is
        // followed: this is what the person asked for.
        fd = open(path, O_RDONLY or O_NONBLOCK or O_CLOEXEC)
        if (fd >= 0 || errno != EINTR) break
    }
    if (fd < 0) return UserFileRead.Failed(reasonOf(errno))
    try {
        var kind = 0
        var size = 0L
        memScoped {
            val status = alloc<stat>()
            if (fstat(fd, status.ptr) != 0) return UserFileRead.Failed(reasonOf(errno))
            kind = status.st_mode.toInt() and S_IFMT
            size = status.st_size
        }
        if (kind == S_IFDIR) return UserFileRead.Failed("Is a directory")
        if (kind != S_IFREG) return UserFileRead.Failed("Not a regular file")
        if (size > maxBytes) return UserFileRead.TooLarge
        return when (val read = readAll(fd, size.toInt(), maxBytes)) {
            is FileRead.Bytes -> UserFileRead.Bytes(read.bytes)
            FileRead.TooLarge -> UserFileRead.TooLarge
            FileRead.Denied -> UserFileRead.Failed("The file cannot be read")
        }
    } finally {
        close(fd)
    }
}

public actual fun writeFileAtomically(path: String, bytes: ByteArray, replace: Boolean): FileWrite {
    // A link is followed, so that the file it points at is the one replaced, and not the link.
    val target = if (replace) realPathOf(path) ?: path else path
    val directory = directoryOf(target)
    val mode = if (replace) permissionsOf(target) else null
    val temporary = createTemporary(directory, temporaryStem(target), mode)
        ?: return FileWrite.Failed(reasonOf(errno))
    var done = false
    try {
        if (!writeAll(temporary.fd, bytes)) return FileWrite.Failed(reasonOf(errno))
        if (mode != null) fchmod(temporary.fd, mode.convert())
        if (fsync(temporary.fd) != 0) return FileWrite.Failed(reasonOf(errno))
        val closed = close(temporary.fd)
        val closeError = errno
        temporary.fd = -1
        if (closed != 0) return FileWrite.Failed(reasonOf(closeError))
        val moved = if (replace) moveOver(temporary.name, target) else moveIfAbsent(temporary.name, target)
        if (moved == FileWrite.Done) {
            done = true
            syncDirectory(directory)
        }
        return moved
    } finally {
        if (temporary.fd >= 0) close(temporary.fd)
        if (!done) unlink(temporary.name)
    }
}

internal class Temporary(val name: String, var fd: Int)

// O_EXCL: a name that is taken is never opened, so that nobody can plant a link there and have us write through it. The
// file is created with the permissions the result is to have, so that a private calendar is never readable by others
// while it is written; the mask of the process can only narrow them. fchmod later gives back what the mask took.
internal fun createTemporary(directory: String, stem: String, permissions: Int?): Temporary? {
    val separator = if (directory.endsWith("/")) "" else "/"
    val process = getpid()
    for (attempt in 0 until TEMPORARY_ATTEMPTS) {
        val name = "$directory$separator.$stem.calendrum-$process-$attempt.tmp"
        val fd = open(name, O_WRONLY or O_CREAT or O_EXCL or O_CLOEXEC, permissions ?: DEFAULT_MODE)
        if (fd >= 0) return Temporary(name, fd)
        if (errno != EEXIST && errno != EINTR) return null
    }
    return null
}

private fun permissionsOf(path: String): Int? = memScoped {
    val status = alloc<stat>()
    if (stat(path, status.ptr) != 0) return null
    if ((status.st_mode.toInt() and S_IFMT) != S_IFREG) return null
    status.st_mode.toInt() and PERMISSION_BITS
}

private fun writeAll(fd: Int, bytes: ByteArray): Boolean {
    var offset = 0
    bytes.usePinned {
        while (offset < bytes.size) {
            val count = write(fd, it.addressOf(offset), (bytes.size - offset).convert()).toInt()
            when {
                count > 0 -> offset += count
                count < 0 && errno == EINTR -> continue
                else -> return false
            }
        }
    }
    return true
}

private fun moveOver(temporary: String, target: String): FileWrite =
    if (rename(temporary, target) == 0) FileWrite.Done else FileWrite.Failed(reasonOf(errno))

// link() fails if the name is taken, also by a link that points nowhere, and does it in one step, so that two commands
// writing the same name cannot both win. A file system without hard links gets a check and a rename instead.
private fun moveIfAbsent(temporary: String, target: String): FileWrite {
    if (link(temporary, target) == 0) {
        unlink(temporary)
        return FileWrite.Done
    }
    if (errno == EEXIST) return FileWrite.Exists
    val exists = memScoped { lstat(target, alloc<stat>().ptr) == 0 }
    if (exists) return FileWrite.Exists
    return moveOver(temporary, target)
}

// The rename is in the directory, and it is not on the disk until the directory is. A failure here is not reported: the
// file is in place, and some systems refuse to sync a directory.
private fun syncDirectory(directory: String) {
    val fd = open(directory, O_RDONLY or O_CLOEXEC)
    if (fd < 0) return
    fsync(fd)
    close(fd)
}

private const val TEMPORARY_ATTEMPTS = 100

// The mask of the process takes what it must from this.
private const val DEFAULT_MODE = 0x1B6
private const val PERMISSION_BITS = 0xFFF

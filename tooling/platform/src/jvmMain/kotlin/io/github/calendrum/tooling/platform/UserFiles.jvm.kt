package io.github.calendrum.tooling.platform

import java.io.IOException
import java.nio.channels.FileChannel
import java.nio.file.AccessDeniedException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.BasicFileAttributes

// The JVM target exists for tests and fuzzing, as for readRegularFile: the type is checked before the file is opened, as
// there is no O_NONBLOCK to keep a FIFO from being waited on.
public actual fun readUserFile(path: String, maxBytes: Int): UserFileRead {
    val file = try {
        Paths.get(path)
    } catch (_: InvalidPathException) {
        return UserFileRead.Failed("No such file or directory")
    }
    return try {
        val attributes = Files.readAttributes(file, BasicFileAttributes::class.java)
        when {
            attributes.isDirectory -> UserFileRead.Failed("Is a directory")

            !attributes.isRegularFile -> UserFileRead.Failed("Not a regular file")

            attributes.size() > maxBytes -> UserFileRead.TooLarge

            else -> Files.newInputStream(file).use { stream ->
                val bytes = stream.readNBytes(maxBytes)
                if (bytes.size == maxBytes && stream.read() >= 0) UserFileRead.TooLarge else UserFileRead.Bytes(bytes)
            }
        }
    } catch (_: NoSuchFileException) {
        UserFileRead.Failed("No such file or directory")
    } catch (_: AccessDeniedException) {
        UserFileRead.Failed("Permission denied")
    } catch (_: IOException) {
        UserFileRead.Failed("The file cannot be read")
    }
}

public actual fun writeFileAtomically(path: String, bytes: ByteArray, replace: Boolean): FileWrite {
    val requested = try {
        Paths.get(path)
    } catch (_: InvalidPathException) {
        return FileWrite.Failed("Invalid argument")
    }
    val target = if (replace && Files.exists(requested)) requested.toRealPath() else requested.toAbsolutePath()
    val directory = target.parent ?: return FileWrite.Failed("No such file or directory")
    var temporary: Path? = null
    return try {
        val created = Files.createTempFile(directory, ".${temporaryStem(target.toString())}.calendrum-", ".tmp")
        temporary = created
        FileChannel.open(created, StandardOpenOption.WRITE).use { channel ->
            channel.write(java.nio.ByteBuffer.wrap(bytes))
            channel.force(true)
        }
        if (replace) {
            if (Files.isRegularFile(target)) {
                Files.setPosixFilePermissions(created, Files.getPosixFilePermissions(target))
            }
            Files.move(created, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
        } else {
            Files.createLink(target, created)
            Files.delete(created)
        }
        temporary = null
        FileWrite.Done
    } catch (_: FileAlreadyExistsException) {
        FileWrite.Exists
    } catch (_: AccessDeniedException) {
        FileWrite.Failed("Permission denied")
    } catch (_: NoSuchFileException) {
        FileWrite.Failed("No such file or directory")
    } catch (_: IOException) {
        FileWrite.Failed("The file cannot be written")
    } catch (_: UnsupportedOperationException) {
        FileWrite.Failed("The file cannot be written")
    } finally {
        temporary?.let { runCatching { Files.deleteIfExists(it) } }
    }
}

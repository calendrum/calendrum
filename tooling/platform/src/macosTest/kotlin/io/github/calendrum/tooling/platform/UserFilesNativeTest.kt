@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.write
import platform.posix.SIGPIPE
import platform.posix.SIG_IGN
import platform.posix.S_IFLNK
import platform.posix.S_IFMT
import platform.posix.chmod
import platform.posix.close
import platform.posix.fstat
import platform.posix.lstat
import platform.posix.mkdtemp
import platform.posix.mkfifo
import platform.posix.pipe
import platform.posix.read
import platform.posix.rmdir
import platform.posix.signal
import platform.posix.stat
import platform.posix.symlink
import platform.posix.unlink
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserFilesNativeTest {
    private val root: String = memScoped {
        val template = "/tmp/calendrum-native-XXXXXX".encodeToByteArray().copyOf(32)
        template.usePinned { mkdtemp(it.addressOf(0))!!.toKString() }
    }
    private val made = ArrayList<String>()

    @AfterTest
    fun clean() {
        // chmod first: a test leaves a directory that cannot be entered.
        chmod(root, 0x1C0.convert())
        for (name in made.reversed()) {
            unlink(name)
            rmdir(name)
        }
        rmdir(root)
    }

    private fun create(name: String, bytes: ByteArray): String {
        val path = "$root/$name"
        check(writeFileAtomically(path, bytes, replace = true) == FileWrite.Done)
        made += path
        return path
    }

    private fun contents(path: String): ByteArray = assertIs<UserFileRead.Bytes>(readUserFile(path, 1 shl 20)).bytes

    private fun permissions(path: String): Int = memScoped {
        val status = alloc<stat>()
        check(platform.posix.stat(path, status.ptr) == 0)
        status.st_mode.toInt() and 0xFFF
    }

    @Test
    fun aSymbolicLinkIsFollowedWhenAPersonNamedIt() {
        val target = create("real.ics", byteArrayOf(7))
        val link = "$root/link.ics"
        check(symlink(target, link) == 0)
        made += link
        assertContentEquals(byteArrayOf(7), contents(link))
    }

    @Test
    fun aFifoIsRefusedAtOnceAndDoesNotWait() {
        val fifo = "$root/fifo"
        check(mkfifo(fifo, 0x1B0.convert()) == 0)
        made += fifo
        assertEquals("Not a regular file", assertIs<UserFileRead.Failed>(readUserFile(fifo, 10)).reason)
    }

    @Test
    fun aDevicesIsRefused() {
        assertIs<UserFileRead.Failed>(readUserFile("/dev/null", 10))
        assertIs<UserFileRead.Failed>(readUserFile("/dev/zero", 10))
    }

    @Test
    fun anUnreadableFileSaysSo() {
        val path = create("secret.ics", byteArrayOf(1))
        check(chmod(path, 0.convert()) == 0)
        if (platform.posix.geteuid() != 0u) {
            assertEquals("Permission denied", assertIs<UserFileRead.Failed>(readUserFile(path, 10)).reason)
        }
        chmod(path, 0x1A4.convert())
    }

    @Test
    fun replacingThroughALinkReplacesTheFileAndKeepsTheLink() {
        val target = create("real.ics", byteArrayOf(1))
        val link = "$root/link.ics"
        check(symlink(target, link) == 0)
        made += link
        assertEquals(FileWrite.Done, writeFileAtomically(link, byteArrayOf(2, 3), replace = true))
        assertContentEquals(byteArrayOf(2, 3), contents(target))
        assertContentEquals(byteArrayOf(2, 3), contents(link))
        val kind = memScoped {
            val status = alloc<stat>()
            check(lstat(link, status.ptr) == 0)
            status.st_mode.toInt() and S_IFMT
        }
        assertEquals(S_IFLNK, kind)
    }

    @Test
    fun theNewFileKeepsThePermissionsOfTheOldOne() {
        val path = create("private.ics", byteArrayOf(1))
        check(chmod(path, 0x180.convert()) == 0)
        assertEquals(FileWrite.Done, writeFileAtomically(path, byteArrayOf(2), replace = true))
        assertEquals(0x180, permissions(path))
    }

    @Test
    fun aDanglingLinkIsNotOverwrittenWithoutReplace() {
        val link = "$root/dangling.ics"
        check(symlink("$root/nowhere", link) == 0)
        made += link
        assertEquals(FileWrite.Exists, writeFileAtomically(link, byteArrayOf(1), replace = false))
        assertIs<UserFileRead.Failed>(readUserFile("$root/nowhere", 10))
    }

    @Test
    fun aDirectoryWithoutWriteAccessLeavesTheOriginalAndNoTemporaryFile() {
        if (platform.posix.geteuid() == 0u) return
        val path = create("keep.ics", byteArrayOf(1, 2))
        check(chmod(root, 0x140.convert()) == 0)
        val result = writeFileAtomically(path, byteArrayOf(9), replace = true)
        assertEquals("Permission denied", assertIs<FileWrite.Failed>(result).reason)
        check(chmod(root, 0x1C0.convert()) == 0)
        assertContentEquals(byteArrayOf(1, 2), contents(path))
        assertTrue(
            memScoped {
                val names = ArrayList<String>()
                val handle = platform.posix.opendir(root)!!
                while (true) {
                    val entry = platform.posix.readdir(handle) ?: break
                    names += entry.pointed.d_name.toKString()
                }
                platform.posix.closedir(handle)
                names.none { it.endsWith(".tmp") }
            },
        )
    }

    @Test
    fun aClosedPipeIsAnErrorOfTheCheckedOutput() {
        signal(SIGPIPE, SIG_IGN)
        val (input, output) = memScoped {
            val fds = allocArray<IntVar>(2)
            check(pipe(fds) == 0)
            fds[0] to fds[1]
        }
        close(input)
        val source = Buffer()
        source.write(byteArrayOf(1, 2, 3))
        val failure = assertFailsWith<IOException> { checkedSink(output).write(source, 3) }
        assertEquals("Broken pipe", failure.message)
        close(output)
    }

    @Test
    fun theCheckedOutputWritesEverythingToAnOpenPipe() {
        val (input, output) = memScoped {
            val fds = allocArray<IntVar>(2)
            check(pipe(fds) == 0)
            fds[0] to fds[1]
        }
        val source = Buffer()
        source.write(byteArrayOf(1, 2, 3))
        checkedSink(output).write(source, 3)
        val received = ByteArray(3)
        assertEquals(3, received.usePinned { read(input, it.addressOf(0), 3.convert()) })
        assertContentEquals(byteArrayOf(1, 2, 3), received)
        close(input)
        close(output)
    }

    @Test
    fun theTemporaryFileIsCreatedWithThePermissionsOfTheResult() {
        // Not a race: the file is looked at as it is made, before a byte is written, so a private calendar is never
        // readable by others for even a moment.
        val temporary = assertNotNull(createTemporary(root, "private.ics", 0x180))
        made += temporary.name
        val mode = memScoped {
            val status = alloc<stat>()
            check(fstat(temporary.fd, status.ptr) == 0)
            status.st_mode.toInt() and 0xFFF
        }
        close(temporary.fd)
        assertEquals(0x180, mode)
    }

    @Test
    fun aBackslashIsAPartOfANameOnPosix() {
        val path = "$root/back\\slash.ics"
        assertEquals(FileWrite.Done, writeFileAtomically(path, byteArrayOf(4, 2), replace = false))
        made += path
        assertContentEquals(byteArrayOf(4, 2), contents(path))
        assertEquals(FileWrite.Exists, writeFileAtomically(path, byteArrayOf(1), replace = false))
        assertEquals(FileWrite.Done, writeFileAtomically(path, byteArrayOf(5), replace = true))
        assertEquals("back\\slash.ics", temporaryStem(path))
        assertEquals(root, directoryOf(path))
    }
}

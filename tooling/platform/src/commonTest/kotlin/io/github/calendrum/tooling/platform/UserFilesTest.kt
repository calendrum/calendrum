package io.github.calendrum.tooling.platform

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.files.SystemTemporaryDirectory
import kotlinx.io.readByteArray
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UserFilesTest {
    private val directory = Path(SystemTemporaryDirectory, "calendrum-platform-${Random.nextLong().toULong()}")

    init {
        SystemFileSystem.createDirectories(directory)
    }

    @AfterTest
    fun clean() {
        for (path in SystemFileSystem.list(directory)) SystemFileSystem.delete(path)
        SystemFileSystem.delete(directory)
    }

    private fun path(name: String): String = Path(directory, name).toString()

    private fun write(name: String, bytes: ByteArray) {
        SystemFileSystem.sink(Path(directory, name)).buffered().use { it.write(bytes) }
    }

    private fun read(name: String): ByteArray = SystemFileSystem.source(Path(directory, name)).buffered().use {
        it.readByteArray()
    }

    private fun names(): Set<String> = SystemFileSystem.list(directory).map { it.name }.toSet()

    @Test
    fun aFileIsRead() {
        write("a.ics", byteArrayOf(1, 2, 3))
        assertContentEquals(byteArrayOf(1, 2, 3), assertIs<UserFileRead.Bytes>(readUserFile(path("a.ics"), 10)).bytes)
        assertContentEquals(byteArrayOf(1, 2, 3), assertIs<UserFileRead.Bytes>(readUserFile(path("a.ics"), 3)).bytes)
    }

    @Test
    fun theExtensionDoesNotMatter() {
        write("noextension", byteArrayOf(9))
        assertIs<UserFileRead.Bytes>(readUserFile(path("noextension"), 10))
    }

    @Test
    fun anEmptyFileIsRead() {
        write("empty", byteArrayOf())
        assertEquals(0, assertIs<UserFileRead.Bytes>(readUserFile(path("empty"), 10)).bytes.size)
    }

    @Test
    fun aFileAboveTheLimitIsTooLarge() {
        write("big", ByteArray(11))
        assertEquals(UserFileRead.TooLarge, readUserFile(path("big"), 10))
    }

    @Test
    fun aMissingFileAndADirectoryAreRefusedWithAReason() {
        val missing = assertIs<UserFileRead.Failed>(readUserFile(path("missing.ics"), 10))
        assertTrue(missing.reason.isNotEmpty())
        assertTrue(path("missing.ics") !in missing.reason)
        val directoryRead = assertIs<UserFileRead.Failed>(readUserFile(directory.toString(), 10))
        assertEquals("Is a directory", directoryRead.reason)
        assertIs<UserFileRead.Failed>(readUserFile("", 10))
    }

    @Test
    fun aNewFileIsWrittenAndNoTemporaryFileStaysBehind() {
        assertEquals(FileWrite.Done, writeFileAtomically(path("out.ics"), byteArrayOf(5, 6), replace = false))
        assertContentEquals(byteArrayOf(5, 6), read("out.ics"))
        assertEquals(setOf("out.ics"), names())
    }

    @Test
    fun anExistingFileIsNotReplacedUnlessAskedTo() {
        write("out.ics", byteArrayOf(1))
        assertEquals(FileWrite.Exists, writeFileAtomically(path("out.ics"), byteArrayOf(2), replace = false))
        assertContentEquals(byteArrayOf(1), read("out.ics"))
        assertEquals(setOf("out.ics"), names())
        assertEquals(FileWrite.Done, writeFileAtomically(path("out.ics"), byteArrayOf(3, 4), replace = true))
        assertContentEquals(byteArrayOf(3, 4), read("out.ics"))
        assertEquals(setOf("out.ics"), names())
    }

    @Test
    fun aLargeFileIsWrittenWhole() {
        val bytes = ByteArray(3 * 1024 * 1024) { (it % 251).toByte() }
        assertEquals(FileWrite.Done, writeFileAtomically(path("large"), bytes, replace = true))
        assertContentEquals(bytes, read("large"))
    }

    @Test
    fun aMissingDirectoryFailsWithAReason() {
        val result = writeFileAtomically(path("missing/out.ics"), byteArrayOf(1), replace = true)
        assertTrue(assertIs<FileWrite.Failed>(result).reason.isNotEmpty())
        assertEquals(emptySet(), names())
    }

    @Test
    fun aLongNameStillGetsATemporaryFile() {
        val name = "x".repeat(200) + ".ics"
        assertEquals(FileWrite.Done, writeFileAtomically(path(name), byteArrayOf(1), replace = true))
        assertEquals(setOf(name), names())
    }
}

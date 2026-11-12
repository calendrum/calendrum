package io.github.calendrum.tooling.platform

import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertSame

// The same rules as the native code, which is tested through the binary of the server in :mcp.
class FileAccessJvmTest {
    private val top = Files.createTempDirectory("calendrum-platform").toFile().canonicalFile
    private val root = File(top, "root").also { it.mkdirs() }
    private val outside = File(top, "outside").also { it.mkdirs() }

    @AfterTest
    fun removeTheFiles() {
        top.deleteRecursively()
    }

    private fun read(path: String, maxBytes: Int = 1024): FileRead =
        FileAccess.readInside(path, listOf(root.path), FileAccess.CALENDAR_EXTENSIONS, maxBytes)

    @Test
    fun aCalendarInsideTheRootIsRead() {
        File(root, "a.ics").writeText("abc")
        File(root, "B.ICS").writeText("abc")
        assertContentEquals("abc".toByteArray(), assertIs<FileRead.Bytes>(read("$root/a.ics")).bytes)
        assertIs<FileRead.Bytes>(read("$root/B.ICS"))
        assertIs<FileRead.Bytes>(read("$top/root/../root/a.ics"))
    }

    @Test
    fun everythingElseIsDenied() {
        File(root, "a.txt").writeText("abc")
        File(root, "d.ics").mkdirs()
        File(outside, "o.ics").writeText("abc")
        Files.createSymbolicLink(File(root, "out.ics").toPath(), File(outside, "o.ics").toPath())
        Files.createSymbolicLink(File(root, "txt.ics").toPath(), File(root, "a.txt").toPath())
        for (path in listOf(
            "$root/none.ics",
            "$root/a.txt",
            "$root/d.ics",
            "$outside/o.ics",
            "$root/../outside/o.ics",
            "$root/out.ics",
            "$root/txt.ics",
            "",
            "$root/a.ics\u0000",
        )) {
            assertSame(FileRead.Denied, read(path), path)
        }
    }

    @Test
    fun aFifoIsDeniedWithoutWaiting() {
        val fifo = File(root, "f.ics")
        assertEquals(0, ProcessBuilder("mkfifo", fifo.path).start().waitFor())
        assertSame(FileRead.Denied, read(fifo.path))
    }

    @Test
    fun aFileAtTheLimitIsReadAndAboveItIsNot() {
        File(root, "at.ics").writeBytes(ByteArray(1024))
        assertEquals(1024, assertIs<FileRead.Bytes>(read("$root/at.ics")).bytes.size)
        RandomAccessFile(File(root, "above.ics"), "rw").use { it.setLength(1025) }
        assertSame(FileRead.TooLarge, read("$root/above.ics"))
    }

    @Test
    fun theWorkingDirectoryIsReal() {
        assertEquals(File(".").canonicalPath, assertNotNull(FileAccess.workingDirectory()))
    }
}

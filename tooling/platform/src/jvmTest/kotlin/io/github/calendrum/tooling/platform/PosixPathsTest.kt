package io.github.calendrum.tooling.platform

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PosixPathsTest {
    @Test
    fun aBackslashIsAPartOfANameWhereTheSeparatorIsASlash() {
        if (File.separatorChar != '/') return
        val directory = Files.createTempDirectory("calendrum-paths").toFile().canonicalFile
        val path = "${directory.path}/back\\slash.ics"
        assertEquals(FileWrite.Done, writeFileAtomically(path, byteArrayOf(4, 2), replace = false))
        assertTrue(File(path).exists())
        assertEquals("back\\slash.ics", temporaryStem(path))
        assertEquals(directory.path, directoryOf(path))
        File(path).delete()
        directory.delete()
    }
}

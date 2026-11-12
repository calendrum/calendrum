package io.github.calendrum.tooling.platform

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PathRulesTest {
    @Test
    fun insideGoesByComponents() {
        assertTrue(isInside("/a/b/c.ics", "/a/b"))
        assertTrue(isInside("/a/b", "/a/b"))
        assertTrue(isInside("/a/b/c.ics", "/a/b/"))
        assertTrue(isInside("/a/b/c.ics", "/"))
        assertTrue(isInside("/", "/"))
        assertFalse(isInside("/a/bc", "/a/b"))
        assertFalse(isInside("/a/bc/d.ics", "/a/b"))
        assertFalse(isInside("/a", "/a/b"))
        assertFalse(isInside("/x/a/b", "/a/b"))
    }

    @Test
    fun theExtensionIsOfTheLastComponentInAnyCase() {
        val extensions = FileAccess.CALENDAR_EXTENSIONS
        for (name in listOf("/a/x.ics", "/a/x.ICS", "/a/x.Ical", "/a/x.ifb", "/a/x.VCS", "/a/x.y.ics")) {
            assertTrue(hasExtension(name, extensions), name)
        }
        for (name in listOf(
            "/a/x",
            "/a/x.txt",
            "/a.ics/x",
            "/a/x.ics.txt",
            "/a/ics",
            "/a/x.ics/",
            "/a/x.icſ",
            "/a/x.ıcs",
        )) {
            assertFalse(hasExtension(name, extensions), name)
        }
    }

    @Test
    fun fileUrisGiveTheirPath() {
        assertEquals("/a/b", pathOfFileUri("file:///a/b"))
        assertEquals("/a/b c", pathOfFileUri("file:///a/b%20c"))
        assertEquals("/a/b", pathOfFileUri("FILE://LOCALHOST/a/b"))
        assertEquals("/", pathOfFileUri("file:///"))
        assertEquals("/папка", pathOfFileUri("file:///%D0%BF%D0%B0%D0%BF%D0%BA%D0%B0"))
        assertEquals("/папка", pathOfFileUri("file:///папка"))
    }

    @Test
    fun anythingElseIsNotARoot() {
        for (uri in listOf(
            "",
            "file:",
            "file://",
            "file:/a",
            "file:a/b",
            "/a/b",
            "https://example.com/a",
            "file://example.com/a",
            "file:///a?b",
            "file:///a#b",
            "file:///a%",
            "file:///a%2",
            "file:///a%zz",
            "file:///a%00b",
            "file:///a%0Ab",
            "file:///a%FF",
            "fıle:///a",
        )) {
            assertNull(pathOfFileUri(uri), uri)
        }
    }

    @Test
    fun aPathWithNulDoesNotResolve() {
        assertNull(FileAccess.realPath("/a\u0000b"))
        assertNull(FileAccess.realPath(""))
    }
}

package io.github.calendrum

import kotlin.test.Test
import kotlin.test.assertTrue

class CalendrumTest {
    @Test
    fun versionFollowsSemanticVersioning() {
        val semanticVersion = Regex("""\d+\.\d+\.\d+(-[0-9A-Za-z.-]+)?""")
        assertTrue(semanticVersion.matches(Calendrum.VERSION), "Not a semantic version: ${Calendrum.VERSION}")
    }
}

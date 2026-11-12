package io.github.calendrum.tooling.ops

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProblemCodesTest {
    // The table comes from a Markdown guide, and a response is read by programs and by people who do not render it.
    @Test
    fun noRowHasTheMarkupOfTheGuide() {
        assertTrue(PROBLEM_CODES.size > 100, "${PROBLEM_CODES.size} codes")
        for (row in PROBLEM_CODES) {
            for (value in row) {
                assertFalse("](" in value, value)
                assertFalse('`' in value, value)
            }
        }
    }

    @Test
    fun aLinkBecomesItsOwnText() {
        val meaning = PROBLEM_CODES.single { it[0] == "ICAL-V012" }[3]
        assertTrue("RFC 5545 \u00a73.3.14" in meaning, meaning)
        assertFalse("http" in meaning, meaning)
    }
}

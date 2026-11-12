package io.github.calendrum.samples

import kotlin.test.Test

class SamplesTest {
    @Test
    fun parsing() {
        parseText()
    }

    @Test
    fun model() {
        readTypedModel()
        buildCalendar()
        editCalendar()
        attendeeBuilder()
        patchFile()
    }

    @Test
    fun schemaAndRegistry() {
        decodeIntoClasses()
        encodeFromClasses()
        ownPropertyAndRegistry()
    }

    @Test
    fun fixups() {
        fixVendorCalendar()
        lintAndFix()
    }

    @Test
    fun itip() {
        invitationAndReply()
        reviseAnEvent()
    }

    @Test
    fun validation() {
        validateCalendar()
        validateWithHouseRules()
    }
}

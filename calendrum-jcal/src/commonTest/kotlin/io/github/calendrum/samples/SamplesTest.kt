package io.github.calendrum.samples

import kotlin.test.Test

class SamplesTest {
    @Test
    fun jcal() {
        jcalRoundTrip()
        jcalProblemsInsteadOfExceptions()
        jcalSerializersForKotlinxSerialization()
    }
}

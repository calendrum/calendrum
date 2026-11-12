@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.schema

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.RealCorpus
import io.github.calendrum.benchmarks.corpus.assumeRealCorpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.UidGenerator
import io.github.calendrum.model.schemaPropertyNodes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

// The `invite-batch` and `university` cases of the common corpus tests, JVM only. The debug test binaries of
// Kotlin/Native and JS spent most of their time there (the selection invariant on `university` alone took 25 s on
// macosArm64); `invite` and `personal` keep every check on every platform, and `CorpusDeterminismTest` keeps all four
// corpora there, so the corpora stay byte-identical across platforms.
class LargeSchemaCorpusTest {
    private val defaults = BuildDefaults(
        object : Clock {
            override fun now(): Instant = Instant.parse("2026-09-01T08:00:00Z")
        },
        UidGenerator { error("Every lesson has a UID") },
    )

    private fun check(bytes: ByteArray, expectedLessons: Int) {
        val lessons = ICal.parser().decode(bytes, LessonSchema)
        assertEquals(expectedLessons, lessons.size)
        assertEquals(lessonsFromWrappers(bytes), lessons)
        assertEquals(0, schemaPropertyNodes(ICal.parser(), bytes, LessonSchema))
        val feed = ICal.writer().encode(lessons, LessonSchema, { prodId = "-//ics-mp//Schema test//EN" }, defaults)
        assertEquals(lessons, ICal.parser().decode(feed, LessonSchema))
    }

    @Test
    fun university() {
        val bytes = corpusBytes(Corpus.UNIVERSITY)
        check(bytes, lessonsFromWrappers(bytes).size)
    }

    @Test
    fun realUniversitySchedule() {
        assumeRealCorpus()
        check(RealCorpus.bytes(RealCorpus.UNIVERSITY), 273)
    }

    @Test
    fun hugeHasNoNodes() {
        val bytes = corpusBytes(Corpus.HUGE)
        assertEquals(0, schemaPropertyNodes(ICal.parser(), bytes, LessonSchema))
    }
}

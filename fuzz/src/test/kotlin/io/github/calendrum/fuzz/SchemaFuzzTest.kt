@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.asByteInput
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.Location
import io.github.calendrum.model.Properties
import io.github.calendrum.model.UidGenerator
import io.github.calendrum.value.ICalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

// `IcsSchema` on any input: decoding never throws, the list, the stream and the version with problems give the same
// items, items and skipped components together are at most the events of the full tree, and the items write and read
// back to the same items; writing them throws only for an input that cannot be written canonically.
class SchemaFuzzTest {
    @FuzzTest
    fun schema(input: ByteArray) {
        val parser = ICal.parser()
        val items = parser.decode(input, LessonSchema)
        val result = parser.decodeWithProblems(input, LessonSchema)
        invariant(result.items == items) { "decodeWithProblems differs from decode" }
        invariant(parser.decode(input.asByteInput(), LessonSchema).toList() == items) { "the stream differs" }
        val tree = ICal.parse(input).components
        val events = tree.sumOf(::events)
        invariant(items.size + result.skipped <= events) { "${items.size} + ${result.skipped} of $events events" }
        if (items.isEmpty()) return
        // The items are written anew, so what canonical writing rejects (a control character that parsing kept in a
        // TZID or a text) throws; the input itself is then not canonically writable either.
        val written = try {
            ICal.writer().encode(items, LessonSchema, { prodId = "-//ics-mp//fuzz//EN" }, DEFAULTS)
        } catch (_: IllegalArgumentException) {
            invariant(!tree.all(::canonicallyWritable)) {
                "the items of a canonically writable input cannot be written"
            }
            return
        }
        invariant(parser.decode(written, LessonSchema) == items) { "the items do not read back" }
    }

    // Every VEVENT of the tree: the schema decodes the component wherever it is nested.
    private fun events(component: io.github.calendrum.syntax.ICalComponent): Int =
        (if (component.name == "VEVENT") 1 else 0) + component.components.sumOf(::events)

    private companion object {
        val DEFAULTS = BuildDefaults(
            object : Clock {
                override fun now(): Instant = Instant.fromEpochSeconds(1_767_225_600L)
            },
            UidGenerator { "fuzz@example.com" },
        )
    }

    private class Lesson(
        val uid: String,
        val start: ICalDateTime,
        val summary: String,
        val room: String?,
        val teachers: List<String>,
    ) {
        override fun equals(other: Any?): Boolean = other is Lesson && uid == other.uid && start == other.start &&
            summary == other.summary && room == other.room && teachers == other.teachers

        override fun hashCode(): Int = uid.hashCode()
    }

    private object LessonSchema : IcsSchema<Lesson>("VEVENT") {
        private val uid = field(Properties.UID, Lesson::uid)
        private val start = field(Properties.DTSTART, Lesson::start)
        private val summary = field(Properties.SUMMARY, Lesson::summary)
        private val room = optionalField(Properties.LOCATION, { it.room?.let(::Location) }, Location::text)
        private val teachers = listField(
            Properties.ATTENDEE,
            Lesson::teachers,
            decode = { it.address },
            encode = { Attendee.builder(it).build() },
        )

        override fun create(values: Values) =
            Lesson(values[uid], values[start], values[summary], values[room], values[teachers])
    }
}

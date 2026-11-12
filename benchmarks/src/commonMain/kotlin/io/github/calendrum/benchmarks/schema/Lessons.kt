@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.benchmarks.schema

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.Location
import io.github.calendrum.model.Properties
import io.github.calendrum.model.calendars
import io.github.calendrum.value.ICalDateTime

data class Lesson(
    val uid: String,
    val start: ICalDateTime,
    val end: ICalDateTime?,
    val summary: String?,
    val location: String?,
    val description: String?,
    val sequence: Int?,
    val teachers: List<String>,
)

object LessonSchema : IcsSchema<Lesson>("VEVENT") {
    private val uid = field(Properties.UID, Lesson::uid)
    private val start = field(Properties.DTSTART, Lesson::start)
    private val end = optionalField(Properties.DTEND, Lesson::end)
    private val summary = optionalField(Properties.SUMMARY, Lesson::summary)
    private val location = optionalField(Properties.LOCATION, { it.location?.let(::Location) }, Location::text)
    private val description = optionalField(Properties.DESCRIPTION, Lesson::description)
    private val sequence = optionalField(Properties.SEQUENCE, Lesson::sequence)
    private val teachers = listField(
        Properties.ATTENDEE,
        Lesson::teachers,
        decode = { it.address },
        encode = { Attendee.builder(it).build() },
    )

    override fun create(values: Values): Lesson = Lesson(
        values[uid],
        values[start],
        values[end],
        values[summary],
        values[location],
        values[description],
        values[sequence],
        values[teachers],
    )
}

fun lessonsFromWrappers(bytes: ByteArray): List<Lesson> {
    val result = ArrayList<Lesson>()
    for (calendar in ICal.parse(bytes).calendars) {
        for (event in calendar.events) {
            val uid = event.uid ?: continue
            val start = event.start ?: continue
            result += Lesson(
                uid,
                start,
                event.end,
                event.summary,
                event.location,
                event.description,
                event.sequence,
                event.attendees.map { it.address },
            )
        }
    }
    return result
}

package io.github.calendrum.benchmarks.schema

import biweekly.ICalendar
import biweekly.property.ICalProperty
import biweekly.util.ICalDate
import io.github.calendrum.benchmarks.baseline.BiweeklyBaseline
import io.github.calendrum.benchmarks.baseline.Ical4jBaseline
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.Parameter
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.parameter.TzId
import net.fortuna.ical4j.model.property.Attendee
import net.fortuna.ical4j.model.property.DateProperty
import net.fortuna.ical4j.model.property.Sequence
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.temporal.Temporal
import kotlin.time.toKotlinInstant

internal object Ical4jLessons {
    fun read(input: ByteArray): List<Lesson> = lessons(Ical4jBaseline.parse(input))

    fun lessons(calendar: Calendar): List<Lesson> {
        val result = ArrayList<Lesson>()
        for (event in calendar.getComponents<VEvent>(Component.VEVENT)) {
            val uid = text(event, Property.UID) ?: continue
            val start = date(event.getProperty<DateProperty<Temporal>>(Property.DTSTART).orElse(null)) ?: continue
            result += Lesson(
                uid,
                start,
                date(event.getProperty<DateProperty<Temporal>>(Property.DTEND).orElse(null)),
                text(event, Property.SUMMARY),
                text(event, Property.LOCATION),
                text(event, Property.DESCRIPTION),
                event.getProperty<Sequence>(Property.SEQUENCE).orElse(null)?.sequenceNo,
                event.getProperties<Attendee>(Property.ATTENDEE).map { it.calAddress.toString() },
            )
        }
        return result
    }

    private fun text(event: VEvent, name: String): String? = event.getProperty<Property>(name).orElse(null)?.value

    private fun date(property: DateProperty<Temporal>?): ICalDateTime? {
        if (property == null) return null
        return when (val value = property.date) {
            is java.time.LocalDate -> ICalDateTime.Date(value.toKotlinLocalDate())

            is java.time.LocalDateTime -> ICalDateTime.Floating(value.toKotlinLocalDateTime())

            is Instant -> ICalDateTime.Utc(value.toKotlinInstant())

            is OffsetDateTime -> ICalDateTime.Utc(value.toInstant().toKotlinInstant())

            is ZonedDateTime -> {
                val tzid = property.getParameter<TzId>(Parameter.TZID).orElse(null)?.value
                if (tzid == null) {
                    ICalDateTime.Utc(value.toInstant().toKotlinInstant())
                } else {
                    ICalDateTime.Zoned(value.toLocalDateTime().toKotlinLocalDateTime(), tzid)
                }
            }

            else -> null
        }
    }
}

internal object BiweeklyLessons {
    fun read(input: ByteArray): List<Lesson> = lessons(BiweeklyBaseline.parse(input))

    fun lessons(calendar: ICalendar): List<Lesson> {
        val result = ArrayList<Lesson>()
        for (event in calendar.events) {
            val uid = event.uid?.value ?: continue
            val start = date(calendar, event.dateStart, event.dateStart?.value) ?: continue
            result += Lesson(
                uid,
                start,
                date(calendar, event.dateEnd, event.dateEnd?.value),
                event.summary?.value,
                event.location?.value,
                event.description?.value,
                event.sequence?.value,
                event.attendees.map { it.uri ?: "mailto:" + it.email },
            )
        }
        return result
    }

    private fun date(calendar: ICalendar, property: ICalProperty?, value: ICalDate?): ICalDateTime? {
        if (property == null || value == null) return null
        val raw = value.rawComponents
        if (!value.hasTime()) return ICalDateTime.Date(LocalDate(raw.year, raw.month, raw.date))
        val local = LocalDateTime(raw.year, raw.month, raw.date, raw.hour, raw.minute, raw.second)
        if (raw.isUtc) return ICalDateTime.Utc(local.toInstant(TimeZone.UTC))
        val assignment = calendar.timezoneInfo.getTimezone(property)
            ?: return ICalDateTime.Floating(local)
        val tzid = assignment.globalId ?: assignment.component?.timezoneId?.value ?: return ICalDateTime.Floating(local)
        return ICalDateTime.Zoned(local, tzid)
    }
}

internal fun requireSameLessons(corpus: String, expected: List<Lesson>, actual: List<Lesson>) {
    check(actual.size == expected.size) { "$corpus: ${actual.size} lessons, expected ${expected.size}" }
    for (index in expected.indices) {
        val left = expected[index]
        val right = actual[index]
        check(left.uid == right.uid && left.summary == right.summary && left.sequence == right.sequence) {
            "$corpus: lesson $index differs"
        }
        check(left.teachers.size == right.teachers.size) { "$corpus: lesson $index has other teachers" }
    }
}

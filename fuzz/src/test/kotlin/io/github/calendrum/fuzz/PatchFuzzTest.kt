@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.AttendeeList
import io.github.calendrum.model.ComponentBuilder
import io.github.calendrum.model.ComponentPatch
import io.github.calendrum.model.ICalPatch
import io.github.calendrum.model.Location
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VEvent
import io.github.calendrum.model.VFreeBusy
import io.github.calendrum.model.VJournal
import io.github.calendrum.model.VTodo
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ParseResult

class PatchFuzzTest {
    @FuzzTest
    @DictionaryFile(resourcePath = "ics.dict")
    fun patch(data: FuzzedDataProvider) {
        val pick = data.consumeInt(0, 64)
        val operations = List(data.consumeInt(1, 4)) { Operation(data.consumeInt(0, 5), data.consumeInt(0, 99)) }
        val input = data.consumeRemainingAsBytes()
        val entries = ICal.fingerprints(input).entries.filter { it.key.uid != null }
        if (entries.isEmpty()) return
        val key = entries[pick % entries.size].key
        val uid = key.uid!!
        val recurrenceId = key.recurrenceId
        val result = try {
            ICalPatch(input) { component(uid, recurrenceId) { for (operation in operations) operation.patch(this) } }
                .apply()
        } catch (e: IllegalArgumentException) {
            return
        }
        invariant(result.unmatched.none { it.property == null }) { "the selector of a fingerprint is unmatched" }
        val parsed = ICal.parse(input)
        val selected = entries.filter { it.key.uid == uid && it.key.recurrenceId == recurrenceId }
        if (!isClean(parsed) || selected.any { it.key.name !in EDITABLE || it.key.calendarIndex < 0 }) return
        val edited = try {
            edit(parsed, uid, recurrenceId, operations)
        } catch (e: IllegalArgumentException) {
            return
        }
        invariant(ICal.parse(result.bytes).components == edited) { "the patched input differs from the edited tree" }
        if (ICal.writer().encodeToByteArray(parsed.components).contentEquals(input)) {
            invariant(ICal.writer().encodeToByteArray(edited).contentEquals(result.bytes)) {
                "the patch differs from the byte-stable writing of the edit"
            }
        }
    }

    private fun isClean(result: ParseResult): Boolean =
        result.problems.isEmpty() && !result.isTruncated && result.charset == "UTF-8" &&
            result.components.all { it.name == "VCALENDAR" }

    private fun edit(parsed: ParseResult, uid: String, recurrenceId: String?, operations: List<Operation>) =
        parsed.calendars.map { calendar ->
            calendar.edit {
                for (event in events.items) {
                    if (selected(event.component, uid, recurrenceId)) {
                        events.replace(event, event.edit { for (operation in operations) operation.edit(this) })
                    }
                }
                for (todo in todos.items) {
                    if (selected(todo.component, uid, recurrenceId)) {
                        todos.replace(todo, todo.edit { for (operation in operations) operation.edit(this) })
                    }
                }
                for (journal in journals.items) {
                    if (selected(journal.component, uid, recurrenceId)) {
                        journals.replace(journal, journal.edit { for (operation in operations) operation.edit(this) })
                    }
                }
                for (freeBusy in freeBusies.items) {
                    if (selected(freeBusy.component, uid, recurrenceId)) {
                        freeBusies.replace(
                            freeBusy,
                            freeBusy.edit {
                                for (operation in operations) operation.edit(this)
                            },
                        )
                    }
                }
            }.component
        }

    private fun selected(component: ICalComponent, uid: String, recurrenceId: String?): Boolean =
        component.property("UID")?.value == uid && component.property("RECURRENCE-ID")?.value == recurrenceId

    private class Operation(private val kind: Int, private val number: Int) {
        private val address: String get() = ADDRESSES[number % ADDRESSES.size]

        fun patch(patch: ComponentPatch) {
            when (kind) {
                0 -> patch.set(Properties.SUMMARY, "Summary $number")
                1 -> patch.set(Properties.SEQUENCE, number)
                2 -> patch.remove(Properties.COMMENT)
                3 -> patch.attendee(address) { set("X-FUZZ", "$number") }
                4 -> patch.remove("X-$number")
                else -> patch.set(Properties.LOCATION, Location("Room $number"))
            }
        }

        fun edit(builder: ComponentBuilder<*>) {
            when (kind) {
                0 -> builder.set(Properties.SUMMARY, "Summary $number")
                1 -> builder.set(Properties.SEQUENCE, number)
                2 -> builder.remove(Properties.COMMENT)
                3 -> attendees(builder) { replace(address) { parameter("X-FUZZ", "$number") } }
                4 -> builder.remove("X-$number")
                else -> builder.set(Properties.LOCATION, Location("Room $number"))
            }
        }

        private fun attendees(builder: ComponentBuilder<*>, edit: AttendeeList.() -> Unit) {
            when (builder) {
                is VEvent.Builder -> builder.attendees(edit)
                is VTodo.Builder -> builder.attendees(edit)
                is VJournal.Builder -> builder.attendees(edit)
                is VFreeBusy.Builder -> builder.attendees(edit)
            }
        }
    }

    private companion object {
        private val EDITABLE = setOf("VEVENT", "VTODO", "VJOURNAL", "VFREEBUSY")
        private val ADDRESSES = listOf("mailto:a@example.com", "mailto:me@example.com", "mailto:bob@example.com")
    }
}

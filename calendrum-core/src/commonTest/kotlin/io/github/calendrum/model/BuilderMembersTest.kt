@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import io.github.calendrum.value.decimalText
import io.github.calendrum.value.decimalToDouble
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BuilderMembersTest {
    private val stamp = utc(2026, 9, 1, 8, 0)
    private val start = utc(2026, 9, 2, 10, 0)
    private val end = utc(2026, 9, 2, 11, 0)
    private val organizer = Organizer.builder("mailto:boss@example.com").cn("Boss").build()
    private val attendee = Attendee.builder("mailto:alice@example.com").cn("Alice").build()
    private val attach = Attach.Uri("https://example.com/agenda.pdf", "application/pdf")
    private val rule = Recur.builder(Frequency.DAILY).count(3).build()
    private val status = RequestStatus("2.0", "Success")
    private val period = ICalPeriod(start, end, null)

    private fun utc(year: Int, month: Int, day: Int, hour: Int, minute: Int): ICalDateTime =
        ICalDateTime.Utc(LocalDateTime(year, month, day, hour, minute).toInstant(TimeZone.UTC))

    private fun reparsed(component: ICalComponent) =
        ICal.parse(ICal.writer().encodeToString(component)).components.single()

    @Test
    fun everyEventMemberWritesWhatTheWrapperReads() {
        val event = VEvent.builder()
            .uid("1").stamp(stamp).start(start).classification(Classification.Private).created(stamp)
            .description("D").geo(Geo(37.5, -122.25)).lastModified(stamp).location("L").organizer(organizer)
            .priority(1).sequence(2).status(EventStatus.Tentative).summary("S").transparency(Transparency.Transparent)
            .url("https://example.com").recurrenceId(start).rrule(rule).end(end).duration(null)
            .attendee(attendee).attach(attach).categories("a", "b").comment("c").contact("Desk")
            .exdate(start).requestStatus(status).relatedTo("parent").relatedTo(RelatedTo("child", RelType.Child))
            .resources("Projector").rdate(end).rdate(period)
            .alarm(VAlarm.builder().audio(Trigger.Relative(ICalDuration.ofMinutes(-5))).build())
            .build()
        val read = VEvent(reparsed(event.component))
        assertEquals("1", read.uid)
        assertEquals(stamp, read.stamp)
        assertEquals(start, read.start)
        assertEquals(Classification.Private, read.classification)
        assertEquals(stamp, read.created)
        assertEquals("D", read.description)
        assertEquals(37.5, read.geo?.latitude)
        assertEquals(-122.25, read.geo?.longitude)
        assertEquals(stamp, read.lastModified)
        assertEquals("L", read.location)
        assertEquals("Boss", read.organizer?.cn)
        assertEquals(1, read.priority)
        assertEquals(2, read.sequence)
        assertEquals(EventStatus.Tentative, read.status)
        assertEquals("S", read.summary)
        assertEquals(Transparency.Transparent, read.transparency)
        assertEquals("https://example.com", read.url)
        assertEquals(start, read.recurrenceId)
        assertEquals(rule, read.rrule)
        assertEquals(end, read.end)
        assertNull(read.duration)
        assertEquals(listOf(attendee), read.attendees)
        assertEquals(listOf<Attach>(attach), read.attachments)
        assertEquals(listOf("a", "b"), read.categories)
        assertEquals(listOf("c"), read.comments)
        assertEquals(listOf("Desk"), read.contacts)
        assertEquals(listOf(start), read.exdates)
        assertEquals(listOf(status), read.requestStatuses)
        assertEquals(listOf("parent", "child"), read.relatedTo.map { it.uid })
        assertEquals(listOf(RelType.Parent, RelType.Child), read.relatedTo.map { it.relType })
        assertEquals(listOf("Projector"), read.resources)
        assertEquals(listOf(end, start), read.rdates)
        assertEquals(AlarmAction.Audio, read.alarms.single().action)
        val builder = event.toBuilder()
        assertEquals("S", builder.summary)
        assertEquals(1, builder.attendees.size)
        assertEquals(1, builder.alarms.items.size)
        builder.duration = ICalDuration.ofHours(1)
        builder.end = null
        assertEquals(ICalDuration.ofHours(1), builder.build().duration)
    }

    @Test
    fun everyToDoMemberWritesWhatTheWrapperReads() {
        val todo = VTodo.builder()
            .uid("2").stamp(stamp).classification(Classification.Public).completed(end).created(stamp)
            .description("D").start(start).geo(Geo(0.0, 0.0)).lastModified(stamp).location("L")
            .organizer("mailto:boss@example.com") { sentBy = "mailto:assistant@example.com" }
            .percentComplete(50).priority(9).recurrenceId(start).sequence(1).status(TodoStatus.InProcess)
            .summary("S").url("https://example.com").rrule(Frequency.WEEKLY) { interval(2) }
            .due(end).duration(null).attendee("mailto:bob@example.com") { role = Role.Chair }
            .attach(attach).categories("x").comment("c").contact("Desk").exdate(start).requestStatus(status)
            .relatedTo("parent").resources("r").rdate(end)
            .alarm { display(Trigger.Absolute(start), "Due") }
            .alarms { assertEquals(1, items.size) }
            .build()
        val read = VTodo(reparsed(todo.component))
        assertEquals("2", read.uid)
        assertEquals(stamp, read.stamp)
        assertEquals(Classification.Public, read.classification)
        assertEquals(end, read.completed)
        assertEquals(stamp, read.created)
        assertEquals("D", read.description)
        assertEquals(start, read.start)
        assertEquals(0.0, read.geo?.latitude)
        assertEquals(stamp, read.lastModified)
        assertEquals("L", read.location)
        assertEquals("mailto:assistant@example.com", read.organizer?.sentBy)
        assertEquals(50, read.percentComplete)
        assertEquals(9, read.priority)
        assertEquals(start, read.recurrenceId)
        assertEquals(1, read.sequence)
        assertEquals(TodoStatus.InProcess, read.status)
        assertEquals("S", read.summary)
        assertEquals("https://example.com", read.url)
        assertEquals(2, read.rrule?.interval)
        assertEquals(end, read.due)
        assertNull(read.duration)
        assertEquals(Role.Chair, read.attendees.single().role)
        assertEquals(1, read.attachments.size)
        assertEquals(listOf("x"), read.categories)
        assertEquals(listOf("c"), read.comments)
        assertEquals(listOf("Desk"), read.contacts)
        assertEquals(listOf(start), read.exdates)
        assertEquals(1, read.requestStatuses.size)
        assertEquals(1, read.relatedTo.size)
        assertEquals(listOf("r"), read.resources)
        assertEquals(listOf(end), read.rdates)
        assertEquals(start, (read.alarms.single().trigger as Trigger.Absolute).dateTime)
        val builder = todo.toBuilder()
        assertEquals("S", builder.summary)
        assertEquals(1, builder.attendees.size)
        builder.attendees { remove("mailto:bob@example.com") }
        assertEquals(0, builder.build().attendees.size)
    }

    @Test
    fun everyJournalMemberWritesWhatTheWrapperReads() {
        val journal = VJournal.builder()
            .uid("3").stamp(stamp).classification(Classification.Confidential).created(stamp).start(start)
            .lastModified(stamp).organizer(organizer).recurrenceId(start).sequence(4).status(JournalStatus.Final)
            .summary("S").url("https://example.com").rrule(rule).organizer("mailto:other@example.com") {}
            .attendee(attendee).attendee("mailto:bob@example.com") {}.attach(attach).categories("x").comment("c")
            .contact("Desk").description("one").description("two").exdate(start).relatedTo("p").rdate(end)
            .requestStatus(status)
            .attendees { assertEquals(2, items.size) }
            .build()
        val read = VJournal(reparsed(journal.component))
        assertEquals("3", read.uid)
        assertEquals(stamp, read.stamp)
        assertEquals(Classification.Confidential, read.classification)
        assertEquals(stamp, read.created)
        assertEquals(start, read.start)
        assertEquals(stamp, read.lastModified)
        assertEquals("mailto:other@example.com", read.organizer?.address)
        assertEquals(start, read.recurrenceId)
        assertEquals(4, read.sequence)
        assertEquals(JournalStatus.Final, read.status)
        assertEquals("S", read.summary)
        assertEquals("https://example.com", read.url)
        assertEquals(rule, read.rrule)
        assertEquals(2, read.attendees.size)
        assertEquals(1, read.attachments.size)
        assertEquals(listOf("x"), read.categories)
        assertEquals(listOf("c"), read.comments)
        assertEquals(listOf("Desk"), read.contacts)
        assertEquals(listOf("one", "two"), read.descriptions)
        assertEquals(listOf(start), read.exdates)
        assertEquals(1, read.relatedTo.size)
        assertEquals(listOf(end), read.rdates)
        assertEquals(1, read.requestStatuses.size)
        assertEquals(2, journal.toBuilder().attendees.size)
    }

    @Test
    fun everyFreeBusyMemberWritesWhatTheWrapperReads() {
        val freeBusy = VFreeBusy.builder()
            .uid("4").stamp(stamp).contact("Desk").start(start).end(end).organizer(organizer)
            .url("https://example.com").attendee(attendee).comment("c").freeBusy(period)
            .freeBusy(FreeBusy(FbType.BusyTentative, listOf(period))).requestStatus(status)
            .organizer("mailto:x@example.com") { cn = "X" }
            .attendee("mailto:y@example.com") {}
            .attendees { assertEquals(2, items.size) }
            .build()
        val read = VFreeBusy(reparsed(freeBusy.component))
        assertEquals("4", read.uid)
        assertEquals(stamp, read.stamp)
        assertEquals("Desk", read.contact)
        assertEquals(start, read.start)
        assertEquals(end, read.end)
        assertEquals("X", read.organizer?.cn)
        assertEquals("https://example.com", read.url)
        assertEquals(2, read.attendees.size)
        assertEquals(listOf("c"), read.comments)
        assertEquals(listOf(FbType.Busy, FbType.BusyTentative), read.freeBusy.map { it.type })
        assertEquals(1, read.requestStatuses.size)
        assertEquals("Desk", freeBusy.toBuilder().contact)
    }

    @Test
    fun everyAlarmTimeZoneAndCalendarMemberWritesWhatTheWrapperReads() {
        val alarm = VAlarm.builder()
            .email(Trigger.Relative(ICalDuration.ofMinutes(-30), Related.End), "Subject", "Body")
            .duration(ICalDuration.ofMinutes(5)).repeat(2).attendee(attendee).attendee("mailto:b@example.com") {}
            .attach(attach).attendees { assertEquals(2, items.size) }
            .build()
        val readAlarm = VAlarm(reparsed(alarm.component))
        assertEquals(AlarmAction.Email, readAlarm.action)
        assertEquals(Related.End, (readAlarm.trigger as Trigger.Relative).related)
        assertEquals(ICalDuration.ofMinutes(5), readAlarm.duration)
        assertEquals(2, readAlarm.repeat)
        assertEquals("Body", readAlarm.description)
        assertEquals("Subject", readAlarm.summary)
        assertEquals(2, readAlarm.attendees.size)
        assertEquals(1, readAlarm.attachments.size)
        assertEquals("Subject", alarm.toBuilder().summary)
        val zone = VTimeZone.builder()
            .tzid("Europe/Berlin").lastModified(stamp).tzUrl("https://example.com/tz")
            .daylight {
                start = ICalDateTime.Floating(LocalDateTime(1996, 3, 31, 2, 0))
                offsetFrom = UtcOffset(hours = 1)
                offsetTo = UtcOffset(hours = 2)
                rrule(Frequency.YEARLY) { byMonth(3) }
                comment("c")
                rdate(ICalDateTime.Floating(LocalDateTime(1997, 3, 30, 2, 0)))
                name("CEST")
            }
            .observance(VTimeZone.Standard.builder().offsetTo(UtcOffset(hours = 1)).build())
            .build()
        val readZone = VTimeZone(reparsed(zone.component))
        assertEquals("Europe/Berlin", readZone.tzid)
        assertEquals(stamp, readZone.lastModified)
        assertEquals("https://example.com/tz", readZone.tzUrl)
        val daylight = readZone.observances[0]
        assertTrue(daylight is VTimeZone.Daylight)
        assertEquals(UtcOffset(hours = 1), daylight.offsetFrom)
        assertEquals(UtcOffset(hours = 2), daylight.offsetTo)
        assertEquals(listOf(3), daylight.rrule?.byMonth?.map { it.month })
        assertEquals(listOf("c"), daylight.comments)
        assertEquals(1, daylight.rdates.size)
        assertEquals(listOf("CEST"), daylight.names)
        assertTrue(readZone.observances[1] is VTimeZone.Standard)
        val edited = zone.edit { observances { assertTrue(remove(items[1])) } }
        assertEquals(1, edited.observances.size)
        assertEquals(listOf("CEST", "X"), edited.observances.single().edit { name("X") }.names)
        val calendar = VCalendar.builder().prodId("p").version("2.0").method(Method.Publish).calScale("GREGORIAN")
            .timeZone(zone).journal(VJournal.builder().uid("j").stamp(stamp).build())
            .freeBusy(VFreeBusy.builder().uid("f").stamp(stamp).build())
            .todo(VTodo.builder().uid("t").stamp(stamp).build())
            .timeZone { tzid = "UTC" }
            .build()
        val readCalendar = VCalendar(reparsed(calendar.component))
        assertEquals(Method.Publish, readCalendar.method)
        assertEquals("GREGORIAN", readCalendar.calScale)
        assertEquals(2, readCalendar.timeZones.size)
        assertEquals(1, readCalendar.journals.size)
        assertEquals(1, readCalendar.freeBusy.size)
        assertEquals(1, readCalendar.todos.size)
        val changed = calendar.edit {
            journals { assertTrue(remove(items.single())) }
            freeBusies { assertTrue(remove(items.single())) }
            timeZones { assertTrue(remove(items.last())) }
            todos { add(VTodo.builder().uid("t2").stamp(stamp).build()) }
            assertEquals("p", prodId)
        }
        assertEquals(0, changed.journals.size)
        assertEquals(0, changed.freeBusy.size)
        assertEquals(1, changed.timeZones.size)
        assertEquals(2, changed.todos.size)
        assertFailsWith<IllegalArgumentException> {
            calendar.edit { events { add(VEvent(changed.todos[0].component)) } }
        }
    }

    @Test
    fun participantBuildersCoverEveryParameter() {
        val built = Attendee.builder("mailto:a@example.com")
            .address("mailto:b@example.com").cn("B").role(Role.OptParticipant).partStat(PartStat.Delegated)
            .rsvp(false).cuType(CuType.Resource).delegatedTo("mailto:c@example.com")
            .delegatedFrom("mailto:d@example.com").member("mailto:group@example.com").sentBy("mailto:s@example.com")
            .dir("https://example.com/dir").language("en").email("b@example.com").parameter("X-A", "1", "2")
            .removeParameter("x-a").build()
        assertEquals("mailto:b@example.com", built.address)
        assertEquals("B", built.cn)
        assertEquals(Role.OptParticipant, built.role)
        assertEquals(PartStat.Delegated, built.partStat)
        assertEquals(CuType.Resource, built.cuType)
        assertEquals(listOf("mailto:c@example.com"), built.delegatedTo)
        assertEquals(listOf("mailto:d@example.com"), built.delegatedFrom)
        assertEquals(listOf("mailto:group@example.com"), built.member)
        assertEquals("mailto:s@example.com", built.sentBy)
        assertEquals("https://example.com/dir", built.dir)
        assertEquals("en", built.language)
        assertEquals("b@example.com", built.email)
        val builder = built.toBuilder()
        assertEquals(listOf("mailto:c@example.com"), builder.delegatedTo)
        assertEquals(listOf("mailto:d@example.com"), builder.delegatedFrom)
        assertEquals(listOf("mailto:group@example.com"), builder.member)
        assertEquals("mailto:s@example.com", builder.sentBy)
        assertEquals("https://example.com/dir", builder.dir)
        assertEquals("en", builder.language)
        assertEquals("b@example.com", builder.email)
        assertEquals(CuType.Resource, builder.cuType)
        assertEquals("Attendee.Builder(parameters=12)", builder.toString())
        assertFailsWith<IllegalArgumentException> { Attendee.builder("").build() }
        val organizer = Organizer.builder("mailto:o@example.com").address("mailto:p@example.com").cn("P")
            .sentBy("mailto:s@example.com").dir("https://example.com/dir").language("de").email("p@example.com")
            .parameter("X-B", "1").removeParameter("X-B").build()
        assertEquals("mailto:p@example.com", organizer.address)
        assertEquals("P", organizer.cn)
        assertEquals("mailto:s@example.com", organizer.sentBy)
        assertEquals("https://example.com/dir", organizer.dir)
        assertEquals("de", organizer.language)
        assertEquals("p@example.com", organizer.email)
        val organizerBuilder = organizer.toBuilder()
        assertEquals("P", organizerBuilder.cn)
        assertEquals("mailto:s@example.com", organizerBuilder.sentBy)
        assertEquals("https://example.com/dir", organizerBuilder.dir)
        assertEquals("de", organizerBuilder.language)
        assertEquals("p@example.com", organizerBuilder.email)
        assertEquals("Organizer.Builder(parameters=5)", organizerBuilder.toString())
        assertFailsWith<IllegalArgumentException> { Organizer.builder("").build() }
    }

    @Test
    fun valuesAreBuiltWithTheirParameters() {
        val date = ICalDateTime.Date(LocalDate(2026, 9, 1))
        val zoned = ICalDateTime.Zoned(LocalDateTime(2026, 9, 1, 10, 0), "Europe/Berlin")
        assertEquals(listOf("VALUE"), RecurrenceId(date).parameters.map { it.name })
        assertEquals(
            listOf("TZID", "RANGE"),
            RecurrenceId(zoned, RecurrenceRange.ThisAndFuture).parameters.map {
                it.name
            },
        )
        assertEquals(listOf("RANGE"), RecurrenceId(start, RecurrenceRange.ThisAndFuture).parameters.map { it.name })
        assertEquals(emptyList(), RecurrenceId(start).parameters)
        assertEquals(listOf("VALUE"), ExDate(listOf(date, date)).parameters.map { it.name })
        assertEquals(listOf("TZID"), ExDate(listOf(zoned)).parameters.map { it.name })
        assertFailsWith<IllegalArgumentException> { ExDate(emptyList()) }
        assertFailsWith<IllegalArgumentException> { ExDate(listOf(date, start)) }
        assertFailsWith<IllegalArgumentException> {
            ExDate(listOf(zoned, ICalDateTime.Zoned(LocalDateTime(2026, 9, 1, 10, 0), "Europe/Paris")))
        }
        val floating = ICalDateTime.Floating(LocalDateTime(2026, 9, 1, 10, 0))
        assertEquals(emptyList(), ExDate(listOf(floating, floating)).parameters)
        assertEquals(
            listOf("VALUE", "TZID"),
            RDate(emptyList(), listOf(ICalPeriod(zoned, null, ICalDuration.ofHours(1)))).parameters.map { it.name },
        )
        assertEquals(listOf("VALUE"), RDate(emptyList(), listOf(period)).parameters.map { it.name })
        assertFailsWith<IllegalArgumentException> { RDate(emptyList(), emptyList()) }
        assertFailsWith<IllegalArgumentException> { RDate(listOf(start), listOf(period)) }
        assertFailsWith<IllegalArgumentException> {
            RDate(emptyList(), listOf(ICalPeriod(date, null, ICalDuration.ofDays(1))))
        }
        assertFailsWith<IllegalArgumentException> {
            FreeBusy(listOf(ICalPeriod(floating, null, ICalDuration.ofHours(1))))
        }
        assertFailsWith<IllegalArgumentException> { FreeBusy(listOf(ICalPeriod(start, floating, null))) }
        assertFailsWith<IllegalArgumentException> { FreeBusy(emptyList()) }
        assertFailsWith<IllegalArgumentException> { Trigger.Absolute(floating) }
        assertEquals(
            listOf("RELATED"),
            Trigger.Relative(ICalDuration.ofMinutes(1), Related.Start).parameters.map {
                it.name
            },
        )
        assertEquals(listOf("VALUE"), Trigger.Absolute(start).parameters.map { it.name })
        assertFailsWith<IllegalArgumentException> { RequestStatus("x", "y") }
        assertFailsWith<IllegalArgumentException> { RequestStatus("1.2.3.4", "y") }
        assertEquals("data", RequestStatus("3.1", "Invalid", "data").data)
        assertFailsWith<IllegalArgumentException> { Categories(emptyList()) }
        assertFailsWith<IllegalArgumentException> { Resources(emptyList()) }
        assertEquals("Room", Location("Room").text)
        assertEquals("Desk", Contact("Desk").text)
        assertEquals(RelType.Sibling, RelatedTo("x", RelType.Sibling).relType)
        val binary = Attach.Binary(byteArrayOf(1, 2, 3), "application/octet-stream")
        assertEquals(listOf("FMTTYPE", "ENCODING", "VALUE"), binary.parameters.map { it.name })
        assertEquals(listOf("ENCODING", "VALUE"), Attach.Binary(byteArrayOf(4)).parameters.map { it.name })
        assertEquals(emptyList(), Attach.Uri("https://example.com").parameters)
        val event = VEvent.builder().uid("1").stamp(stamp).attach(binary).build()
        assertContentEquals(
            byteArrayOf(1, 2, 3),
            (VEvent(reparsed(event.component)).attachments.single() as Attach.Binary).bytes(),
        )
        assertFailsWith<IllegalArgumentException> { Geo(90.5, 0.0) }
        assertFailsWith<IllegalArgumentException> { Geo(0.0, -180.5) }
        assertNotEquals(Geo(1.0, 2.0), Geo(1.0, 2.5))
        assertEquals(Geo(1.0, 2.0), Geo(1.0, 2.0))
    }

    @Test
    fun doublesAreWrittenAsPlainDecimals() {
        val cases = mapOf(
            0.0 to "0",
            -0.0 to "0",
            37.386013 to "37.386013",
            -122.082932 to "-122.082932",
            180.0 to "180",
            100.0 to "100",
            0.5 to "0.5",
            0.00001 to "0.00001",
            1.0E-7 to "0.0000001",
            1.2345E7 to "12345000",
            -1.5E-10 to "-0.00000000015",
        )
        for ((value, text) in cases) {
            assertEquals(text, decimalText(value), "$value")
            assertEquals(value + 0.0, decimalToDouble(text) + 0.0, "$value")
        }
        assertFailsWith<IllegalArgumentException> { decimalText(Double.NaN) }
        assertFailsWith<IllegalArgumentException> { decimalText(Double.POSITIVE_INFINITY) }
    }

    @Test
    fun patchResultsDescribeThemselvesWithoutValues() {
        val change = PatchChange(PatchChange.Kind.INSERTED, "SUMMARY", 10, 2)
        assertEquals(change, PatchChange(PatchChange.Kind.INSERTED, "SUMMARY", 10, 2))
        assertEquals(change.hashCode(), PatchChange(PatchChange.Kind.INSERTED, "SUMMARY", 10, 2).hashCode())
        assertNotEquals(change, PatchChange(PatchChange.Kind.REMOVED, "SUMMARY", 10, 2))
        assertEquals("PatchChange(kind=INSERTED, property=SUMMARY, line=2)", change.toString())
        val selector = PatchSelector("uid", null, "ATTENDEE", "mailto:a@example.com")
        assertEquals(selector.hashCode(), PatchSelector("uid", null, "ATTENDEE", "mailto:a@example.com").hashCode())
        assertEquals("PatchSelector(property=ATTENDEE)", selector.toString())
        val patch = ICalPatch(ByteArray(0)) { component("uid", null) { remove("X-A") } }
        assertEquals("ICalPatch(components=1)", patch.toString())
        val result = patch.apply()
        assertEquals("PatchResult(bytes=0, unmatched=1, changes=0)", result.toString())
        ICalPatch(ByteArray(0)) {
            assertEquals("ICalPatch.Builder(components=0)", toString())
            component("uid", null) {
                assertEquals("ComponentPatch(operations=0)", toString())
                property("SUMMARY") { assertEquals("PropertyPatch(parameters=0)", toString()) }
                property(Properties.SUMMARY) { remove(Parameters.LANGUAGE).remove("x-b") }
            }
        }
        assertFailsWith<IllegalArgumentException> {
            ICalPatch(ByteArray(0)) { component("uid", null) { property("SUMMARY") { set("X-A") } } }
        }
    }
}

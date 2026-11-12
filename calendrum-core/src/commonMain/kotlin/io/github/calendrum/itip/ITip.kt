@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.itip

import io.github.calendrum.SortedStringMap
import io.github.calendrum.groupsOf
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.EncodeContext
import io.github.calendrum.model.ExDate
import io.github.calendrum.model.Method
import io.github.calendrum.model.NodeBuilder
import io.github.calendrum.model.Organizer
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.Properties
import io.github.calendrum.model.RDate
import io.github.calendrum.model.RecurrenceId
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.VEvent
import io.github.calendrum.model.VTodo
import io.github.calendrum.model.edit
import io.github.calendrum.model.get
import io.github.calendrum.model.isOlder
import io.github.calendrum.model.overrideOf
import io.github.calendrum.model.recurrenceId
import io.github.calendrum.model.sameInstance
import io.github.calendrum.model.sequence
import io.github.calendrum.model.stamp
import io.github.calendrum.model.uid
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.describe
import io.github.calendrum.syntax.isWritable
import io.github.calendrum.value.ICalDateTime
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName
import kotlin.time.Clock

/** What [ITip.apply] did with an incoming message. */
public enum class ITipOutcome {
    /** The message changed the calendar; [ITipResult.calendar] is the new one. */
    Applied,

    /**
     * The message is older than what the calendar already has (a lower `SEQUENCE`, or the same one with an earlier
     * `DTSTAMP`) and was not applied.
     */
    Outdated,

    /** The message refers to a component the calendar does not have, so there was nothing to change. */
    NotFound,

    /**
     * The message is a `REFRESH` or a `COUNTER`: it asks the organizer for an answer and changes nothing by itself.
     */
    ResponseRequired,

    /**
     * The message was not applicable: it has no `METHOD`, no schedulable component, no `UID`, or a method that does not
     * change a calendar; or it is a `REPLY` whose attendee cannot be rebuilt, because the values it brings have a
     * control character or a parameter without a value (parsing keeps them, no writer accepts them).
     */
    Ignored,
}

/**
 * The outcome of applying an iTIP message to a calendar.
 *
 * @property calendar the calendar after applying the message; the same calendar when the outcome is not
 *   [ITipOutcome.Applied].
 * @property outcome what happened.
 * @property method the `METHOD` of the message, or `null` when it had none.
 */
public class ITipResult internal constructor(
    public val calendar: VCalendar,
    public val outcome: ITipOutcome,
    public val method: Method?,
) {
    /** Returns the outcome and the method. */
    override fun toString(): String = describe("ITipResult", "outcome", outcome, "method", method?.value ?: "none")
}

/**
 * Builds and applies iTIP messages ([RFC 5546](https://www.rfc-editor.org/rfc/rfc5546)), the scheduling messages of
 * invitations, replies and cancellations.
 *
 * Everything here is a pure function: the clock is a parameter, `DTSTAMP` and `SEQUENCE` follow RFC 5546, and nothing
 * is added to a calendar, sent or opened on its own. A message is a [VCalendar] with a `METHOD`; transport (iMIP mail,
 * CalDAV scheduling) is up to the caller, see [IMip] for the mail checks. Messages built here keep only the properties
 * the method needs.
 *
 * @sample io.github.calendrum.samples.invitationAndReply
 * @sample io.github.calendrum.samples.reviseAnEvent
 */
public object ITip {
    /**
     * Builds a `PUBLISH` message ([RFC 5546 §3.2.1](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.1)) from
     * [calendar]: its events, to-dos and journal entries with a new `DTSTAMP`, without `ATTENDEE` and `REQUEST-STATUS`.
     *
     * Time zones used by the components are kept; the others are dropped.
     *
     * @param calendar the calendar to publish.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun publish(calendar: VCalendar, clock: Clock): VCalendar {
        val stamp = ICalDateTime.Utc(clock.now())
        val components = calendar.component.components.filter { it.name != "VTIMEZONE" }.map { child ->
            if (child.name !in SCHEDULED) {
                child
            } else {
                child.edit {
                    set(Properties.DTSTAMP, stamp)
                    remove(Properties.ATTENDEE)
                    remove(Properties.REQUEST_STATUS)
                    priority(child)
                }
            }
        }
        return VCalendar(calendarOf(calendar.component, Method.Publish, components))
    }

    /**
     * Builds a `REQUEST` message ([RFC 5546 §3.2.2](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.2)) inviting
     * [attendees] to [event].
     *
     * The message carries the event with [organizer] set, the attendees, a `DTSTAMP` from [clock] and the event's
     * `SEQUENCE` (0 if it has none). Use [revise] first when the event was changed, so the `SEQUENCE` is right.
     *
     * @param event the event to send.
     * @param organizer who organizes it.
     * @param attendees who is invited.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun request(event: VEvent, organizer: Organizer, attendees: List<Attendee>, clock: Clock): VCalendar =
        requested(event.component, organizer, attendees, clock)

    /**
     * Builds a `REQUEST` message ([RFC 5546 §3.4.2](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.2)) assigning
     * [todo] to [attendees].
     *
     * @param todo the to-do to send.
     * @param organizer who organizes it.
     * @param attendees who is asked.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun request(todo: VTodo, organizer: Organizer, attendees: List<Attendee>, clock: Clock): VCalendar =
        requested(todo.component, organizer, attendees, clock)

    /**
     * Returns [current] with the `SEQUENCE` that RFC 5546 requires: [previous] plus one when `DTSTART`, `DTEND`,
     * `DURATION`, `RRULE`, `RDATE`, `EXDATE`, `EXRULE` or `STATUS` changed, and otherwise the old one. A `SEQUENCE`
     * that the caller already raised is kept.
     *
     * @param previous the event as the attendees last saw it.
     * @param current the edited event.
     */
    @JvmStatic
    public fun revise(previous: VEvent, current: VEvent): VEvent =
        VEvent(revised(previous.component, current.component))

    /**
     * Returns [current] with the `SEQUENCE` that RFC 5546 requires: [previous] plus one when `DTSTART`, `DUE`,
     * `DURATION`, `RRULE`, `RDATE`, `EXDATE`, `EXRULE` or `STATUS` changed, and otherwise the old one.
     *
     * @param previous the to-do as the attendees last saw it.
     * @param current the edited to-do.
     */
    @JvmStatic
    @ObjCName("reviseTodo")
    public fun revise(previous: VTodo, current: VTodo): VTodo = VTodo(revised(previous.component, current.component))

    /**
     * Builds a `REPLY` message ([RFC 5546 §3.2.3](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.3)) in which [me]
     * answers [request] with [partStat].
     *
     * Each event or to-do of the request is answered with the properties a reply needs and the attendee [me] with the
     * new `PARTSTAT` and without `RSVP`. If [me] is not among the attendees, a new one is added.
     *
     * @param request the invitation being answered.
     * @param me the address of the replying attendee, with or without `mailto:`.
     * @param partStat the answer.
     * @param clock supplies the `DTSTAMP`.
     * @throws IllegalArgumentException if [request] has no event or to-do.
     */
    @JvmStatic
    public fun reply(request: VCalendar, me: String, partStat: PartStat, clock: Clock): VCalendar {
        val stamp = ICalDateTime.Utc(clock.now())
        val components = scheduled(request.component).map { component ->
            val mine = component.attendeeList().firstOrNull { sameParty(it.address, me) }
            val attendee = (
                mine?.toBuilder() ?: Attendee.builder(
                    me,
                )
                ).partStat(partStat).removeParameter("RSVP").build()
            component.edit {
                keep(REPLY_PROPERTIES)
                attendees(listOf(attendee))
                set(Properties.DTSTAMP, stamp)
                removeChildren()
            }
        }
        require(components.isNotEmpty()) { "The request has no VEVENT or VTODO" }
        return VCalendar(calendarOf(request.component, Method.Reply, components))
    }

    /**
     * Builds a `CANCEL` message ([RFC 5546 §3.2.5](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.5)) for the whole
     * [event]: `STATUS:CANCELLED` and `SEQUENCE` plus one.
     *
     * @param event the event to cancel.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun cancel(event: VEvent, clock: Clock): VCalendar = cancelled(event.component, null, clock)

    /**
     * Builds a `CANCEL` message for one instance of a recurring [event], identified by [recurrenceId].
     *
     * @param event the recurring event.
     * @param recurrenceId the instance to cancel.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun cancel(event: VEvent, recurrenceId: ICalDateTime, clock: Clock): VCalendar =
        cancelled(event.component, recurrenceId, clock)

    /**
     * Builds a `CANCEL` message ([RFC 5546 §3.4.5](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.5)) for the whole
     * [todo].
     *
     * @param todo the to-do to cancel.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun cancel(todo: VTodo, clock: Clock): VCalendar = cancelled(todo.component, null, clock)

    /**
     * Builds a `CANCEL` message for one instance of a recurring [todo], identified by [recurrenceId].
     *
     * @param todo the recurring to-do.
     * @param recurrenceId the instance to cancel.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun cancel(todo: VTodo, recurrenceId: ICalDateTime, clock: Clock): VCalendar =
        cancelled(todo.component, recurrenceId, clock)

    /**
     * Builds an `ADD` message ([RFC 5546 §3.2.4](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.4)) that adds
     * [instance] to the recurring [series].
     *
     * @param series the recurring event; its `UID` is used and its `SEQUENCE` raised by one.
     * @param instance the new occurrence, without recurrence properties.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun add(series: VEvent, instance: VEvent, clock: Clock): VCalendar =
        added(series.component, instance.component, clock)

    /**
     * Builds an `ADD` message ([RFC 5546 §3.4.4](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.4)) that adds
     * [instance] to the recurring [series].
     *
     * @param series the recurring to-do; its `UID` is used and its `SEQUENCE` raised by one.
     * @param instance the new occurrence, without recurrence properties.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    @ObjCName("addTodo")
    public fun add(series: VTodo, instance: VTodo, clock: Clock): VCalendar =
        added(series.component, instance.component, clock)

    /**
     * Builds a `REFRESH` message ([RFC 5546 §3.2.6](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.6)) in which
     * [me] asks the organizer for the latest version of [event].
     *
     * @param event the event as the attendee has it.
     * @param me the address of the attendee asking.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun refresh(event: VEvent, me: String, clock: Clock): VCalendar = refreshed(event.component, me, clock)

    /**
     * Builds a `REFRESH` message ([RFC 5546 §3.4.6](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.6)) in which
     * [me] asks the organizer for the latest version of [todo].
     *
     * @param todo the to-do as the attendee has it.
     * @param me the address of the attendee asking.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun refresh(todo: VTodo, me: String, clock: Clock): VCalendar = refreshed(todo.component, me, clock)

    /**
     * Builds a `COUNTER` message ([RFC 5546 §3.2.7](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.7)) in which
     * [me] proposes [proposal] instead of the event invited to.
     *
     * @param proposal the event with the changes the attendee proposes.
     * @param me the address of the attendee proposing.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    public fun counter(proposal: VEvent, me: String, clock: Clock): VCalendar = countered(proposal.component, me, clock)

    /**
     * Builds a `COUNTER` message ([RFC 5546 §3.4.7](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.7)) in which
     * [me] proposes [proposal] instead of the to-do assigned.
     *
     * @param proposal the to-do with the changes the attendee proposes.
     * @param me the address of the attendee proposing.
     * @param clock supplies the `DTSTAMP`.
     */
    @JvmStatic
    @ObjCName("counterTodo")
    public fun counter(proposal: VTodo, me: String, clock: Clock): VCalendar = countered(proposal.component, me, clock)

    /**
     * Builds a `DECLINECOUNTER` message ([RFC 5546 §3.2.8](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.8)) in
     * which the organizer refuses a counter proposal.
     *
     * @param counter the `COUNTER` message being refused.
     * @param clock supplies the `DTSTAMP`.
     * @throws IllegalArgumentException if [counter] has no event or to-do.
     */
    @JvmStatic
    public fun declineCounter(counter: VCalendar, clock: Clock): VCalendar {
        val stamp = ICalDateTime.Utc(clock.now())
        val components = scheduled(counter.component).map { component ->
            component.edit {
                keep(DECLINE_PROPERTIES)
                set(Properties.SEQUENCE, component.sequence())
                set(Properties.DTSTAMP, stamp)
                removeChildren()
            }
        }
        require(components.isNotEmpty()) { "The counter has no VEVENT or VTODO" }
        return VCalendar(calendarOf(counter.component, Method.DeclineCounter, components))
    }

    // A REQUEST or PUBLISH of a master replaces the whole series of its UID (stored alarms stay when the message has
    // none); of an instance, only that override. A smaller SEQUENCE, or an equal one with an earlier DTSTAMP, is
    // outdated. CANCEL marks a series cancelled or adds an EXDATE for an instance, ADD adds an RDATE and an override,
    // REPLY sets the PARTSTAT of the attendee and creates the override of an instance it answers.

    /**
     * Applies an incoming iTIP message to [store] and returns the result.
     *
     * `PUBLISH` and `REQUEST` add or replace the series (a request keeps the alarms the user set on the stored copy);
     * `CANCEL` marks it cancelled or removes an instance; `ADD` adds an instance; `REPLY` updates the `PARTSTAT` of the
     * replying attendee; a `REPLY` whose `PARTSTAT`, `DELEGATED-TO` or `DELEGATED-FROM` (or, for an attendee the store
     * does not have, any of its parameters or its address) has a control character or a parameter without a value is
     * [ITipOutcome.Ignored], so a store that writes canonically still does after any message. A message older than the
     * stored copy by `SEQUENCE` and `DTSTAMP` is [ITipOutcome.Outdated] and changes nothing. `REFRESH` and `COUNTER`
     * need an answer and change nothing ([ITipOutcome.ResponseRequired]). Time zones of an applied message are added to
     * the store. [store] itself is never modified.
     *
     * @param store the calendar that holds the user's events.
     * @param incoming the message to apply.
     * @sample io.github.calendrum.samples.invitationAndReply
     */
    @JvmStatic
    public fun apply(store: VCalendar, incoming: VCalendar): ITipResult {
        val method = incoming.method
        val items = scheduled(incoming.component)
        if (method == null || items.isEmpty()) return ITipResult(store, ITipOutcome.Ignored, method)
        val series = SeriesStore(store.component.components)
        val outcomes = ArrayList<ITipOutcome>()
        when (method.value) {
            "PUBLISH", "REQUEST" -> for (group in byUid(items)) outcomes += requestApplied(series, group)
            "CANCEL" -> for (item in items) outcomes += cancelApplied(series, item)
            "ADD" -> for (item in items) outcomes += addApplied(series, item)
            "REPLY" -> for (item in items) outcomes += replyApplied(series, item)
            "REFRESH", "COUNTER" -> outcomes += ITipOutcome.ResponseRequired
            else -> outcomes += ITipOutcome.Ignored
        }
        val outcome = OUTCOME_ORDER.firstOrNull { it in outcomes } ?: ITipOutcome.Ignored
        val calendar = if (outcome == ITipOutcome.Applied) {
            val children = series.toList()
            if (method.value in ZONED_METHODS) addZones(children, incoming.component)
            VCalendar(store.component.edit { children(children) })
        } else {
            store
        }
        return ITipResult(calendar, outcome, method)
    }

    /** Returns the name of the object. */
    override fun toString(): String = "ITip"
}

private fun requested(
    component: ICalComponent,
    organizer: Organizer,
    attendees: List<Attendee>,
    clock: Clock,
): VCalendar {
    val message = component.edit {
        set(Properties.ORGANIZER, organizer)
        attendees(attendees)
        set(Properties.DTSTAMP, ICalDateTime.Utc(clock.now()))
        set(Properties.SEQUENCE, component.sequence())
        remove(Properties.REQUEST_STATUS)
        priority(component)
    }
    return VCalendar(calendarOf(null, Method.Request, listOf(message)))
}

// SEQUENCE grows by one when a property listed by RFC 5545 §3.8.7.4 changed, unless the caller already raised it.
private fun revised(previous: ICalComponent, current: ICalComponent): ICalComponent {
    val sequence = when {
        current.sequence() > previous.sequence() -> current.sequence()
        SIGNIFICANT.any { previous.properties(it) != current.properties(it) } -> previous.sequence() + 1
        else -> previous.sequence()
    }
    return if (current[Properties.SEQUENCE] ==
        sequence
    ) {
        current
    } else {
        current.edit { set(Properties.SEQUENCE, sequence) }
    }
}

private fun cancelled(component: ICalComponent, recurrenceId: ICalDateTime?, clock: Clock): VCalendar {
    val source = if (recurrenceId == null) component else overrideOf(component, recurrenceId)
    val message = source.edit {
        keep(CANCEL_PROPERTIES)
        set(Properties.SEQUENCE, component.sequence() + 1)
        set(Properties.DTSTAMP, ICalDateTime.Utc(clock.now()))
        remove("STATUS")
        property("STATUS", CANCELLED)
        removeChildren()
    }
    return VCalendar(calendarOf(null, Method.Cancel, listOf(message)))
}

private fun added(series: ICalComponent, instance: ICalComponent, clock: Clock): VCalendar {
    val message = instance.edit {
        for (name in RECURRENCE_PROPERTIES) remove(name)
        remove(Properties.RECURRENCE_ID)
        remove(Properties.REQUEST_STATUS)
        set(Properties.UID, series.uid())
        set(Properties.SEQUENCE, series.sequence() + 1)
        set(Properties.DTSTAMP, ICalDateTime.Utc(clock.now()))
        for (name in INHERITED) {
            if (instance.property(name) == null) series.properties(name).forEach { property(it) }
        }
        priority(instance)
    }
    return VCalendar(calendarOf(null, Method.Add, listOf(message)))
}

private fun refreshed(component: ICalComponent, me: String, clock: Clock): VCalendar {
    val mine = component.attendeeList().firstOrNull { sameParty(it.address, me) } ?: Attendee.builder(me).build()
    val message = component.edit {
        keep(if (component.name == "VTODO") REFRESH_TODO_PROPERTIES else REFRESH_PROPERTIES)
        attendees(listOf(mine))
        set(Properties.DTSTAMP, ICalDateTime.Utc(clock.now()))
        removeChildren()
    }
    return VCalendar(calendarOf(null, Method.Refresh, listOf(message)))
}

private fun countered(proposal: ICalComponent, me: String, clock: Clock): VCalendar {
    val attendees = proposal.attendeeList()
    val message = proposal.edit {
        if (attendees.none { sameParty(it.address, me) }) property(Properties.ATTENDEE, Attendee.builder(me).build())
        set(Properties.SEQUENCE, proposal.sequence())
        set(Properties.DTSTAMP, ICalDateTime.Utc(clock.now()))
        priority(proposal)
    }
    return VCalendar(calendarOf(null, Method.Counter, listOf(message)))
}

private fun NodeBuilder.priority(component: ICalComponent) {
    if (component.name == "VTODO" && component.property("PRIORITY") == null) set(Properties.PRIORITY, 0)
}

private fun scheduled(calendar: ICalComponent): List<ICalComponent> =
    calendar.components.filter { it.name == "VEVENT" || it.name == "VTODO" || it.name == "VJOURNAL" }

// The items grouped by UID in the order of their first item, as `groupBy` gives, but by sorting: UIDs come from the
// input (see InputKeys.kt). Items without a UID form one group, which is ignored.
private fun byUid(items: List<ICalComponent>): List<List<ICalComponent>> {
    val keys = items.map { item -> item.uid()?.let { "\u0001$it" } ?: "\u0000" }
    return groupsOf(items.size) { keys[it] }.map { positions -> positions.map { items[it] } }
}

private fun requestApplied(series: SeriesStore, group: List<ICalComponent>): ITipOutcome {
    val uid = group.first().uid() ?: return ITipOutcome.Ignored
    val incomingMaster = group.firstOrNull { it.recurrenceId() == null }
    if (incomingMaster != null) {
        val stored = series.master(uid)
        if (stored != null && isOlder(incomingMaster, stored.component)) return ITipOutcome.Outdated
        val alarms = stored?.component?.components("VALARM").orEmpty()
        val replacement = group.map { item ->
            if (alarms.isEmpty() || item.components("VALARM").isNotEmpty()) {
                item
            } else {
                item.edit { for (alarm in alarms) addChild(alarm) }
            }
        }
        series.replaceSeries(uid, replacement)
        return ITipOutcome.Applied
    }
    var outcome = ITipOutcome.Ignored
    for (item in group) {
        val recurrenceId = item.recurrenceId() ?: continue
        val stored = series.instance(uid, recurrenceId)
        val master = series.master(uid)
        val reference = stored ?: master
        if (reference != null && item.sequence() < reference.component.sequence()) {
            if (outcome == ITipOutcome.Ignored) outcome = ITipOutcome.Outdated
            continue
        }
        if (stored != null && isOlder(item, stored.component)) {
            if (outcome == ITipOutcome.Ignored) outcome = ITipOutcome.Outdated
            continue
        }
        when {
            stored != null -> series.replace(stored, item)
            master != null -> series.insertAfter(master, item)
            else -> series.append(item)
        }
        outcome = ITipOutcome.Applied
    }
    return outcome
}

private fun cancelApplied(series: SeriesStore, item: ICalComponent): ITipOutcome {
    val uid = item.uid() ?: return ITipOutcome.Ignored
    val existing = series.members(uid)
    if (existing.isEmpty()) return ITipOutcome.NotFound
    val master = series.master(uid)
    val recurrenceId = item.recurrenceId()
    if (recurrenceId == null) {
        if (master != null && item.sequence() < master.component.sequence()) return ITipOutcome.Outdated
        for (slot in existing) {
            series.replace(
                slot,
                slot.component.edit {
                    remove("STATUS")
                    property("STATUS", CANCELLED)
                    if (slot === master) set(Properties.SEQUENCE, item.sequence())
                },
            )
        }
        return ITipOutcome.Applied
    }
    val stored = series.instance(uid, recurrenceId)
    val reference = stored ?: master ?: return ITipOutcome.NotFound
    if (item.sequence() < reference.component.sequence()) return ITipOutcome.Outdated
    if (master != null) {
        val component = master.component
        series.replace(
            master,
            component.edit {
                property(Properties.EXDATE, ExDate(listOf(recurrenceId)))
                set(Properties.SEQUENCE, maxOf(component.sequence(), item.sequence()))
            },
        )
        if (stored != null) series.remove(stored)
    } else {
        series.replace(
            reference,
            reference.component.edit {
                remove("STATUS")
                property("STATUS", CANCELLED)
                set(Properties.SEQUENCE, item.sequence())
            },
        )
    }
    return ITipOutcome.Applied
}

private fun addApplied(series: SeriesStore, item: ICalComponent): ITipOutcome {
    val uid = item.uid() ?: return ITipOutcome.Ignored
    val start = item[Properties.DTSTART] ?: return ITipOutcome.Ignored
    val master = series.master(uid) ?: return ITipOutcome.NotFound
    val component = master.component
    if (item.sequence() < component.sequence()) return ITipOutcome.Outdated
    series.replace(
        master,
        component.edit {
            property(Properties.RDATE, RDate(listOf(start), emptyList()))
            set(Properties.SEQUENCE, item.sequence())
        },
    )
    val instance = item.edit { set(Properties.RECURRENCE_ID, RecurrenceId(start)) }
    val stored = series.instance(uid, start)
    if (stored != null) series.replace(stored, instance) else series.insertAfter(master, instance)
    return ITipOutcome.Applied
}

private fun replyApplied(series: SeriesStore, item: ICalComponent): ITipOutcome {
    val uid = item.uid() ?: return ITipOutcome.Ignored
    val replier = item.attendeeList().firstOrNull() ?: return ITipOutcome.Ignored
    val recurrenceId = item.recurrenceId()
    val master = series.master(uid)
    val stored = if (recurrenceId == null) master else series.instance(uid, recurrenceId)
    val target = when {
        stored != null -> stored.component
        recurrenceId != null && master != null -> overrideOf(master.component, recurrenceId)
        else -> return ITipOutcome.NotFound
    }
    if (item.sequence() < target.sequence()) return ITipOutcome.Outdated
    val replied = replied(target, replier) ?: return ITipOutcome.Ignored
    if (stored != null) series.replace(stored, replied) else series.insertAfter(master!!, replied)
    return ITipOutcome.Applied
}

// The stored ATTENDEE of the replier gets the PARTSTAT and the delegation of the reply; a component without one gets
// the replier. Parsing keeps a control character or a parameter without a value, the builder and canonical writing
// refuse them, and a rebuilt node has no source bytes for byte-stable writing: such a reply is not applied
// rather than leaving a component that no writer accepts.
private fun replied(target: ICalComponent, replier: Attendee): ICalComponent? {
    val nodes = target.properties
    for (position in nodes.indices) {
        val node = nodes[position]
        if (node.name != "ATTENDEE") continue
        val stored = ICalComponent.builder(target.name).property(node).build()[Properties.ATTENDEE] ?: continue
        if (!sameParty(stored.address, replier.address)) continue
        val rebuilt = rebuilt {
            val merged = stored.toBuilder().partStat(replier.partStat)
            if (replier.delegatedTo.isNotEmpty()) merged.delegatedTo = replier.delegatedTo
            if (replier.delegatedFrom.isNotEmpty()) merged.delegatedFrom = replier.delegatedFrom
            merged.build()
        } ?: return null
        return target.edit { replaceAt(position, rebuilt) }
    }
    val rebuilt = rebuilt { replier } ?: return null
    return target.edit { property(rebuilt) }
}

private inline fun rebuilt(attendee: () -> Attendee): ICalProperty? = try {
    Properties.ATTENDEE.codec.encode(attendee(), EncodeContext("ATTENDEE")).takeIf(::isWritable)
} catch (_: IllegalArgumentException) {
    null
}

// TZIDs come from the input: kept in a tree, not hashed (see InputKeys.kt).
private fun addZones(children: MutableList<ICalComponent>, incoming: ICalComponent) {
    val known = SortedStringMap<Unit>()
    for (child in children) if (child.name == "VTIMEZONE") child.property("TZID")?.value?.let { known[it] = Unit }
    val added = ArrayList<ICalComponent>()
    for (zone in incoming.components("VTIMEZONE")) {
        val tzid = zone.property("TZID")?.value ?: continue
        if (known.containsKey(tzid)) continue
        known[tzid] = Unit
        added += zone
    }
    children.addAll(children.indexOfLast { it.name == "VTIMEZONE" } + 1, added)
}

private const val CANCELLED = "CANCELLED"

private val OUTCOME_ORDER = listOf(
    ITipOutcome.Applied,
    ITipOutcome.ResponseRequired,
    ITipOutcome.Outdated,
    ITipOutcome.NotFound,
    ITipOutcome.Ignored,
)

private val ZONED_METHODS = setOf("PUBLISH", "REQUEST", "ADD")

private val SIGNIFICANT = listOf("DTSTART", "DTEND", "DURATION", "DUE", "RRULE", "RDATE", "EXDATE", "EXRULE", "STATUS")

private val RECURRENCE_PROPERTIES = listOf("RRULE", "RDATE", "EXDATE", "EXRULE")

private val INHERITED = listOf("ORGANIZER", "SUMMARY", "ATTENDEE")

private val REPLY_PROPERTIES = setOf(
    "UID", "SEQUENCE", "RECURRENCE-ID", "DTSTART", "DTEND", "DURATION", "DUE", "SUMMARY", "ORGANIZER", "ATTENDEE",
    "DTSTAMP",
)

private val CANCEL_PROPERTIES = setOf(
    "UID", "SEQUENCE", "RECURRENCE-ID", "DTSTART", "DTEND", "DURATION", "DUE", "SUMMARY", "ORGANIZER", "ATTENDEE",
    "STATUS", "DTSTAMP",
)

private val REFRESH_PROPERTIES = setOf("UID", "RECURRENCE-ID", "ORGANIZER", "ATTENDEE", "DTSTAMP")

private val REFRESH_TODO_PROPERTIES = setOf("UID", "RECURRENCE-ID", "ATTENDEE", "DTSTAMP")

private val DECLINE_PROPERTIES = setOf("UID", "SEQUENCE", "RECURRENCE-ID", "ORGANIZER", "ATTENDEE", "DTSTAMP")

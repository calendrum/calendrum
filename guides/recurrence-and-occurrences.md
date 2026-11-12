# Recurrence and occurrences

Two layers answer "when does this happen":

- **Recurrence** (`io.github.calendrum.recur`) expands an `RRULE` and the `RDATE` and `EXDATE` around it into dates *in local
  time*, lazily and without a time zone.
- **Occurrences** (`io.github.calendrum.occurrence`) turn components into instants: they apply the time zones, the overrides
  (`RECURRENCE-ID`), cancellations, durations and alarms, and answer a time-range query with the semantics of CalDAV
  ([RFC 4791 §9.9](https://www.rfc-editor.org/rfc/rfc4791#section-9.9)).

Fragments use `calendar`, a `VCalendar` with a weekly meeting on Thursdays (four times) in Europe/Berlin, an alarm 15 minutes
before it, and an all-day holiday (the example file of [Getting started](getting-started.md#read-a-calendar)).

## Recurrence rules

Every `RRULE` of RFC 5545 expands, including all `BYxxx` parts in any combination, `WKST`, `BYSETPOS`, and the `SKIP` of
RFC 7529 for `RSCALE=GREGORIAN`. A rule with another `RSCALE` is not expanded and reports `ICAL-R004`. `recurrenceSet`
collects `DTSTART`, `RRULE`, `RDATE`, `EXDATE` and the old `EXRULE` of a component; `between(from, to)` yields the dates of
`[from, to)` in local time. An `EXDATE` always wins over what a rule or an `RDATE` produced, a date in `EXDATE` excludes the
whole day of a date-time set, and duplicates are dropped.

<!-- sample: recurrence-set -->
```kotlin
val set = event.recurrenceSet ?: return // null without DTSTART
val from = ICalDateTime.Floating(LocalDateTime(2026, 10, 6, 0, 0))
val to = ICalDateTime.Floating(LocalDateTime(2026, 10, 20, 0, 0))
for (start in set.between(from, to)) printed += start.toString() // [from, to) in local time; EXDATE wins
val all = event.rrule!!.expand(event.start!!).take(10).toList() // lazy: endless rules are fine
```

(`event` is the first event of the calendar and `printed` a mutable list of strings.) Sequences are lazy, so a rule without an
end is fine as long as you stop taking from it.

## Limits and mismatches

Hostile or silly rules (`FREQ=SECONDLY` over centuries, `BYMONTH=2;BYMONTHDAY=30`) are bounded by `RecurrenceLimits`. When a
limit is reached the sequence ends and a problem is reported: nothing is thrown. `StartMismatch` decides what happens when
`DTSTART` is not one of the dates its own rule produces, for which RFC 5545 §3.8.5.3 leaves the result undefined: by default it
is the first occurrence and counts toward `COUNT` (`Rfc`); `Exclude` drops it unless the rule produces it; `Include` keeps it
without counting it.

<!-- sample: recurrence-options -->
```kotlin
val options = RecurrenceOptions.builder()
    // DTSTART the rule does not produce: the RFC counts it by default
    .startMismatch(StartMismatch.Exclude)
    // limits end the sequence, they never throw
    .limits(RecurrenceLimits.builder().maxOccurrences(10_000).build())
    // ICAL-R001 limit reached, ICAL-R003 DTSTART not in the rule…
    .problemListener { reported += it.code.id }
    .build()
val total = Sequence { set.iterator(options) }.count()
```

(`set` is the recurrence set from the previous fragment and `reported` a list of strings.) `untilOffset` is a function that
gives the UTC offset of a local time, used to compare a UTC `UNTIL` with the local dates of a zoned rule; the occurrence engine
sets it for you.

## Occurrences of a calendar

`calendar.occurrences(from, to, options)` is a lazy sequence of `Occurrence` in start order. An occurrence knows its
`start` and `end` as instants, `startLocal` as written, the `recurrenceId` of an instance, the component it comes from (the
override for an overridden instance) and its alarms. The end follows RFC 5545 §3.8.2.2: `DTEND` or `DUE`, the start plus
`DURATION`, a day for a date, or the start itself.

The options say where time zones come from and where floating times happen. Nothing is taken from the system: a `DATE` or a
floating time without `floatingZone` or `floatingICalZone` has no instant and is skipped with `ICAL-O004`.

<!-- sample: occurrences-window -->
```kotlin
val from = Instant.parse("2026-10-01T00:00:00Z")
val to = Instant.parse("2026-10-10T00:00:00Z")
for (occurrence in calendar.occurrences(from, to, options)) { // RFC 4791 time-range semantics, [from, to)
    val title = occurrence.component.property("SUMMARY")?.value
    printed += "$title ${occurrence.start}..${occurrence.end} allDay=${occurrence.isAllDay}"
    occurrence.alarms.forEach { printed += "  alarm at ${it.at}" } // REPEAT is expanded, with a limit
}
```

Types: each `alarms` entry is an `AlarmTime` with the instant `at`, the `alarm` (`VAlarm`) and the `repetition`, 0 for the first
firing and 1 and up for the `REPEAT` repetitions. To decide which of two copies of an event is newer, `event.revision()` gives a
`Revision` (`sequence`, `modified`) that compares by the rules of RFC 5546.

`options` here is an `OccurrenceOptions` built with `zones(TimeZoneResolvers.default(calendar))` and
`floatingZone(TimeZone.UTC)`; see [Time zones](time-zones.md) for the choices. More of what the options hold:

- `recurrence` – the `RecurrenceOptions` above;
- `includeCancelled` – cancelled instances (`STATUS:CANCELLED`) are left out by default;
- `thisAndFuture` – on by default: overrides with `RANGE=THISANDFUTURE` are applied to the later instances on a best-effort
  basis (`ICAL-O002`);
- `maxAlarmRepetitions` – the cap on the repetitions of an alarm;
- `problemListener` – receives the `ICAL-O` and `ICAL-R` problems.

Overrides replace the instance they name; one that matches no instance is kept as a single occurrence and reported
(`ICAL-O001`).

## Cursors and paging

When you need the next occurrence rather than a window, `occurrences(options)` gives an `OccurrenceCursor`: `next()`,
`after(instant)`, `at(instant)` and `page(size, after)`. It computes only as far as it is asked.

<!-- sample: occurrences-cursor -->
```kotlin
// next(), after(instant), at(instant), page(size, after)
val cursor = calendar.occurrences(options)
val next = cursor.after(now) // the first occurrence that starts after the instant
val page = calendar.occurrences(options).page(size = 2, after = null) // for paging through a long series
```

## Combining occurrences

`OccurrenceSet` holds a sorted list of occurrences and supports `union`, `intersect` and `minus`, for questions like "my lessons
minus the holidays".

<!-- sample: occurrences-set -->
```kotlin
fun setOf(calendar: VCalendar) = OccurrenceSet.of(calendar.occurrences(from, to, options).toList())
val lessons = setOf(teaching).minus(setOf(holidays)) // union, intersect and minus on occurrences
```

## Free/busy and conflicts

`FreeBusyCalc` merges the busy time of several calendars. Events that are cancelled, transparent or of zero length do not
block; where intervals of different types overlap the more binding one wins (busy, then unavailable, then tentative).
Existing `VFREEBUSY` components are added. `conflicts` is not frozen for 1.0 and is marked `@ExperimentalICalApi`: opt in with
`@OptIn(ExperimentalICalApi::class)` (see [Compatibility](compatibility.md#api-stability)). `busy` and `toVFreeBusy` need nothing.

<!-- sample: occurrences-freebusy -->
```kotlin
val busy = FreeBusyCalc.busy(listOf(calendar), from, to, options) // merged, sorted intervals
val conflicts = FreeBusyCalc.conflicts(listOf(calendar, calendar), from, to, options) // overlapping pairs
val published = FreeBusyCalc.toVFreeBusy(busy, from, to, defaults) // a VFREEBUSY for publishing
```

Types: `busy` returns `BusyInterval`s (`start`, `end` and the `FBTYPE` as `type`); `conflicts` returns `Conflict`s, each a pair of
occurrences (`first`, `second`) with the overlap as `start` and `end`. Every overlapping pair is listed, so the time and memory of
`conflicts` grow with the number of conflicts: k events at the same time make k(k − 1)/2 of them. A limit on the number of pairs
or a lazy sequence may replace the list in a minor release; that is why the function is not frozen.

## Next

- [Time zones](time-zones.md) for the resolvers these functions take.
- Recurring series edits (`Series`) in [Building and editing](building-and-editing.md#recurring-series).

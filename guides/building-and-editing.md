# Building and editing

Everything in the model is immutable. You create calendars with a Kotlin DSL (or builders, from Java), and you change one by
asking for a modified copy that shares every node the change did not touch. A third tool, `ICalPatch`, changes a file
without parsing the rest of it.

## Build with the DSL

`VCalendar(prodId, defaults) { ... }` builds a calendar, `event { }`, `todo { }`, `journal { }`, `timeZone { }` add components
to it, and the nested builders (`rrule`, `attendee`, `alarm`) build their parts. Properties the DSL does not know are added
with `property(name, value)`.

There is no global clock and no global UID generator. `UID` and `DTSTAMP` of a new component come from values you set or from
`BuildDefaults(clock) { newUid() }`; leaving both out is an error, which is how a missing `UID` is found in your code rather
than in a client.

<!-- sample: building-dsl -->
```kotlin
val defaults = BuildDefaults(Clock.System) { newUid() } // a clock and a UID source, nothing global
val calendar = VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = defaults) {
    event {
        summary = "Weekly sync"
        start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
        duration = ICalDuration.ofHours(1)
        // COUNT xor UNTIL; BYxxx and ranges are checked
        rrule(Frequency.WEEKLY) {
            byDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            count(10)
        }
        attendee("mailto:alice@example.com") {
            cn = "Alice"
            rsvp = true
        }
        alarm { display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Soon") }
        property("X-ACME-TAG", "blue")
    }
    todo {
        summary = "Book a room"
        due = ICalDateTime.Utc(Instant.parse("2026-09-30T12:00:00Z"))
    }
}
val ics = ICal.writer().encodeToString(calendar.component)
```

(`newUid()` is any function that returns a unique string.)

The recurrence rule builder is correct by construction: a part that the frequency does not allow, a value out of range, or
`COUNT` together with `UNTIL` is refused when you write it, and combinations that depend on the order of calls are checked in
`build()`. `UNTIL` must have the type of `DTSTART`: a date for a date, a local date-time for a floating start, UTC otherwise.

<!-- sample: building-checks -->
```kotlin
val failure = assertFailsWith<IllegalArgumentException> {
    VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = BuildDefaults(Clock.System) { newUid() }) {
        event {
            start = ICalDateTime.Date(LocalDate(2026, 10, 1))
            rrule(Frequency.DAILY) { byWeekNo(1) } // BYWEEKNO is only for YEARLY
        }
    }
}
```

(The fragment uses `assertFailsWith` of `kotlin.test` for the check.)

Building does not validate a calendar against the whole standard; that is [`Validator`](validation-itip-fixups.md#validation).

## Build trees by hand

The syntax layer has its own small DSL for components that need no typed support, such as unknown ones. Nodes built by hand are
written canonically.

<!-- sample: building-tree -->
```kotlin
val event = ICalComponent("VEVENT") {
    property("UID", "42@example.com")
    property(ICalProperty.builder("ATTENDEE").parameter("CN", "Jane").value("mailto:jane@example.com").build())
    component("VALARM") { property("ACTION", "DISPLAY") }
}
val ics = ICal.writer().encodeToString(event) // canonical: folded at 75 octets, escaped
```

## Edit

`edit { }` on a calendar or on any wrapper returns a new object. Unchanged properties and components are the same objects as
before, so an edit costs what it touches, and byte-stable writing keeps the untouched lines exactly as they were. A replaced
property stays at its place; a new one goes after the last property. A parameter you do not mention, including unknown ones,
stays.

<!-- sample: building-edit -->
```kotlin
val event = calendar.events.first()
val accepted = calendar.edit {
    // a new calendar; the old one is untouched
    events {
        replace(
            event,
            event.edit {
                sequence = (sequence ?: 0) + 1
                // other parameters stay
                attendees { replace("mailto:jane@example.com") { partStat = PartStat.Accepted } }
                remove("LOCATION")
            },
        )
    }
}
val untouched = accepted.events.last().component === calendar.events.last().component // shared, not copied
```

Editing a parsed component does not validate it; building one from scratch needs a `UID` and a `DTSTAMP`.

## Patch a file

`ICalPatch` is for programs that must change a few properties of a file and leave everything else alone, such as a client that
answers an invitation and uploads the file to a CalDAV server whose `ETag` should not change for the parts you did not touch.
It finds each component by `UID` and `RECURRENCE-ID` through [fingerprints](parsing.md#fingerprints), so the rest of the file is
never parsed. Changed lines are written canonically, with the line ending of the file; every other byte stays. `ICalPatch`
is not frozen for 1.0 and is marked `@ExperimentalICalApi`: opt in with `@OptIn(ExperimentalICalApi::class)` (see
[Compatibility](compatibility.md#api-stability)).

<!-- sample: building-patch -->
```kotlin
val result = ICalPatch(original) {
    component(uid = "42@example.com", recurrenceId = null) {
        attendee("mailto:jane@example.com") { set(Parameters.PARTSTAT, PartStat.Accepted) }
        set(Properties.DTSTAMP, ICalDateTime.Utc(Clock.System.now()))
    }
}.apply()
val patched: ByteArray = result.bytes // every other byte is as it was
val missed = result.unmatched // selectors that found nothing
val log = result.changes.map { "${it.kind} ${it.property} at line ${it.line}" } // never the values
```

`original` is the file as bytes. On a component the operations are `set` and `remove` for properties, and `attendee(address)`
or `property(key) { ... }` to `set` or `remove` the parameters of one property. The rules of `edit` apply: `set` replaces the
first property of the name and removes the rest, `remove` removes every one, and a new property goes after the last one.

Types: `apply()` returns a `PatchResult` with the `bytes`, the `unmatched` selectors (`PatchSelector`: `uid`, `recurrenceId`,
`property`, `address`) and the `changes` (`PatchChange`: `kind`, `property`, byte `offset` and `line`).

## Recurring series

A recurring event is a master with an `RRULE` and, optionally, overrides that share its `UID` and carry a `RECURRENCE-ID`.
`Series` changes one instance, "this and the following" instances, or the whole series, the way a calendar application does,
and always returns a new calendar:

<!-- sample: building-series -->
```kotlin
val series = Series.find(calendar, "42@example.com")!!
val second = ICalDateTime.Zoned(LocalDateTime(2026, 10, 8, 10, 0), "Europe/Berlin")
val moved = Series.editThis(series, second) {
    // one instance: an override is created
    start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 8, 14, 0), "Europe/Berlin")
}
val cancelled = Series.deleteThis(series, second) // an EXDATE on the master
val renamed = Series.editAll(series) { summary = "Team sync" } // the whole series
```

Types: `Series.find` returns a `SeriesView` with the `calendar`, the `uid`, the `master` event and its `overrides`.

`editThisAndFuture` splits the series by default: the old rule ends before the instance and a new series with a derived
`UID` (a UUIDv5 of the old one) and `RELATED-TO` takes over; `ThisAndFuture.Range` writes a `RANGE=THISANDFUTURE` override
instead, which the RFC allows but many clients ignore. When the start of the master moves, the `RECURRENCE-ID`s of its
overrides and its `EXDATE`s move by the same amount.

## Publish a feed

A feed that is generated again every hour should change only where the data changed. `FeedPublisher` compares the new version
with the previous one by canonical form, sets `DTSTAMP`, `LAST-MODIFIED` and a higher `SEQUENCE` on the components that
changed and keeps the old values on the others, so clients see no phantom updates and the unchanged part of the feed keeps
its bytes. `Canonical.canonicalize` gives the canonical bytes of a calendar (sorted properties, parameters and components), and
`Canonical.uidV5` derives a stable `UID` from a namespace and a natural key. `FeedPublisher` is not frozen for 1.0
(`@ExperimentalICalApi`); `Canonical` is.

<!-- sample: building-publish -->
```kotlin
val publisher = FeedPublisher(Clock.System)
// new DTSTAMP and a higher SEQUENCE for what changed, nothing else
val next = publisher.publish(previous, current)
```

## Next

- Reading straight into your own classes and writing a feed from them: [Selective parsing and
  schemas](selective-parsing-and-schema.md).
- Answering invitations (`ITip`), repairing feeds: [Validation, iTIP and fixups](validation-itip-fixups.md).
- Builders and static factories for Java: [Java](java.md).

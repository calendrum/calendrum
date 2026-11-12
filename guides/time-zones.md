# Time zones

A calendar file says "10:00 in Europe/Berlin" and leaves the rest to the reader. The reader needs the rules of that zone,
and the rules change: governments move the clocks, and a file written in 2015 describes a zone differently from the database
of 2026. `calendrum` therefore never takes a zone from the system and never guesses. Everything that turns a local time into an
instant takes a `TimeZoneResolver` that you choose, and a time without a resolvable zone stays what it is until you give it a
zone on purpose.

## The four forms of a time

`ICalDateTime` has exactly the forms of RFC 5545:

| Form | Example | Instant |
|---|---|---|
| `Utc` | `20261001T080000Z` | its own |
| `Zoned` | `DTSTART;TZID=Europe/Berlin:20261001T100000` | needs the rules of the `TZID` |
| `Floating` | `20261001T100000` | needs a zone chosen by the application ("wherever the user is") |
| `Date` | `VALUE=DATE:20261001` | needs a zone, for the start of the day |

`toInstant(zones, floatingZone)` returns `null` when the answer cannot be known: a floating time or a date with no
`floatingZone`, or a `Zoned` time whose `TZID` no resolver knows and the policy says "floating".

## The default chain

`TimeZoneResolvers.default(calendar)` reads the `VTIMEZONE` components of the file, then tries aliases of the zones in them,
and treats any other `TZID` as floating. It consults no zone database, so the result does not depend on the platform.

<!-- sample: tz-default -->
```kotlin
// VTIMEZONE of the file, then aliases; unknown TZID → floating
val zones = TimeZoneResolvers.default(calendar)
val start = calendar.events.first().start!! // 2026-10-01T10:00[Europe/Berlin]
val instant: Instant? = start.toInstant(zones) // 2026-10-01T08:00:00Z
```

A file whose `VTIMEZONE` is missing, as many feeds are, needs a database. Adding one is an explicit choice, so that an
application that does not need it does not carry it.

## Choose your own chain

A chain asks its resolvers in order and applies a policy to a `TZID` that none of them knows (`ICAL-Z001`):
`UnknownZonePolicy.Floating` (the time becomes floating), `Utc` or `Fail` (an exception).

<!-- sample: tz-chain -->
```kotlin
val chain = TimeZoneResolvers.chain(
    TimeZoneResolvers.vtimezone(calendar), // the file first: it says what its author meant
    TimeZoneResolvers.iana, // the platform database, only when you ask for it
    TimeZoneResolvers.aliases, // Windows and Outlook names, /mozilla.org/ prefixes, old links
    unknown = UnknownZonePolicy.Floating, // or Utc, or Fail
)
val zone: ICalTimeZone? = chain.resolve("Europe/Berlin", ResolveContext.Empty)
```

| Resolver | Source |
|---|---|
| `vtimezone(calendar)` | the `VTIMEZONE` components of the calendar, compiled on first use; a broken one is skipped with `ICAL-Z003` |
| `iana` | the platform database through kotlinx-datetime; not the same on every platform and database version |
| `aliases` | names real calendars use instead of IANA ids: Windows and Outlook names ("W. Europe Standard Time"), IANA links, `/mozilla.org/` prefixes, vendor prefixes; found through `iana` and reported as `ICAL-Z002`; a last-resort guess from a city in the name is `ICAL-Z004` |
| `TzData.resolver` | the `calendrum-tzdata` module: the IANA database packed into the library, the same answer on every platform |
| your own | a `TimeZoneResolver` is a one-method interface returning an `ICalTimeZone?` |

An `ICalTimeZone` gives the offset at an instant, the instant of a local time, and the transitions in a range.

## Floating times and dates

<!-- sample: tz-floating -->
```kotlin
val breakfast = ICalDateTime.Floating(LocalDateTime(2026, 10, 1, 8, 0))
val unknown = breakfast.toInstant(zones) // null: the library never guesses
val inUtc = breakfast.toInstant(zones, TimeZone.UTC) // the zone is your decision, any kotlinx TimeZone
```

A `kotlinx.datetime.TimeZone` such as `TimeZone.of("Europe/Berlin")` also works where the platform has its zone data (a default
JS, Wasm or WASI program has none: there use `TimeZone.UTC` or a fixed offset, or an `ICalTimeZone` from `calendrum-tzdata` for the
zoned times). `OccurrenceOptions.floatingZone` is the same decision for [occurrences](recurrence-and-occurrences.md): where all-day and
floating events happen for this user. Without it they are skipped (`ICAL-O004`). `OccurrenceOptions.floatingICalZone` takes an
`ICalTimeZone` instead, such as one from `TzData.resolver`, and so it works for any zone on JS, Wasm and WASI too.

## Gaps and overlaps

When the clocks move forward, some local times do not exist; when they move back, some happen twice. `calendrum` follows RFC 5545
§3.3.5: a time in a gap moves forward by the length of the gap, and a time in an overlap is the first occurrence.

<!-- sample: tz-gaps -->
```kotlin
// 02:00 → 03:00 is skipped
val inTheGap = ICalDateTime.Zoned(LocalDateTime(2026, 3, 29, 2, 30), "Europe/Berlin")
// 02:00–03:00 happens twice
val twice = ICalDateTime.Zoned(LocalDateTime(2026, 10, 25, 2, 30), "Europe/Berlin")
// 2026-03-29T01:30:00Z: a gap moves forward by its length (03:30 CEST)
val moved = inTheGap.toInstant(zones)
// 2026-10-25T00:30:00Z: an overlap takes the first occurrence (CEST)
val first = twice.toInstant(zones)
```

Recurrence expansion works in local time and applies the same rule to each instance, so a weekly 02:30 meeting that falls
into the gap still happens, at 03:30.

## How a `VTIMEZONE` is read

Each observance (`STANDARD`, `DAYLIGHT`) is a recurrence set in local time with its `TZOFFSETFROM` and `TZOFFSETTO`; its
`UNTIL` in UTC is compared with its own `TZOFFSETFROM`. Before the first onset the zone has the offset of the earliest
observance. Exchange and Outlook write an observance of the year 1601 with a rule; its `DTSTART` is not an onset, and the rules start
that year.

## The `calendrum-tzdata` module

`calendrum-tzdata` packs the IANA database (the release is listed in `NOTICE` and in `TzData.version`) into the library as compact
tables decoded on first use. It gives the same answers on every platform, which the platform database cannot promise, and it
produces `VTIMEZONE` components for writing.

<!-- sample: tzdata-resolver -->
```kotlin
val zones = TimeZoneResolvers.chain(
    TimeZoneResolvers.vtimezone(calendar), // a VTIMEZONE in the file still wins
    TzData.resolver, // the same answer on every platform
    unknown = UnknownZonePolicy.Utc,
)
val start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
val instant: Instant? = start.toInstant(zones) // 2026-10-01T08:00:00Z
val version = TzData.version // the tzdb release, for example "2026b"
```

## Writing time zones

A calendar that uses `TZID=Europe/Berlin` without a `VTIMEZONE` is not self-contained: a reader that does not know the zone
cannot place the time. `WriteOptions.addMissingTimeZones(source)` adds a `VTIMEZONE` for every `TZID` the calendar uses and does not define,
from any `VTimeZoneSource`; `TzData` is one. `TzData.vtimezone(tzid, mode)` builds a component by hand.

<!-- sample: tzdata-write -->
```kotlin
val writer = ICal.writer(WriteOptions.builder().addMissingTimeZones(TzData).timeZonesFirst(true).build())
val ics = writer.encodeToString(calendar.component) // a VTIMEZONE for every TZID that has none
val zone = TzData.vtimezone("Europe/Berlin", VTimeZoneMode.OutlookCompatible) // or build one yourself
```

Two modes:

- `OutlookCompatible` writes one `RRULE` per observance, the shape Outlook and Exchange expect for the current rules. Where
  the current rules cannot be written that way, the result approximates them and `ICAL-Z005` says so (pass a problem listener
  to know).
- `Full` lists every offset change, with the history as `RDATE`s and rules for the future; it is exact and longer, and a
  `from` instant leaves out earlier history.

The plain `TzData.vtimezone(tzid)` (the `VTimeZoneSource` method) picks the Outlook form when the zone allows it and the full form
otherwise.

## Next

- [Recurrence and occurrences](recurrence-and-occurrences.md) use these resolvers.
- `ICAL-Z` codes: [Diagnostics](diagnostics.md#ical-z-time-zones).

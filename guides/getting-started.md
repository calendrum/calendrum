# Getting started

`calendrum` reads, writes and computes iCalendar data ([RFC 5545](https://www.rfc-editor.org/rfc/rfc5545)) on every Kotlin
target: JVM and Android, Apple platforms, Linux, Windows, JS and Wasm. The core has no reflection, no classpath resources
and no global mutable state, so it behaves the same everywhere.

> **Status.** The library is in early development and nothing is published yet; every API may still change. Until the
> first release, build it from source (`./gradlew publishToMavenLocal`) and add `mavenLocal()` to your repositories.

Every Kotlin, Java and Swift fragment in these guides is the text of a function that the build compiles and the tests run.
Imports are left out to keep the fragments short; the types live in these packages:

| Package | What is there |
|---|---|
| `io.github.calendrum` | `ICal` (the entry point), problems and their codes, `ByteInput` and `ByteOutput` |
| `io.github.calendrum.syntax` | the syntax tree, `ICalParser`, `ICalWriter`, options, the event cursor, selections, interceptors, fingerprints |
| `io.github.calendrum.model` | typed wrappers (`VCalendar`, `VEvent`…), keys, builders, `edit`, `ICalPatch`, schemas, the registry |
| `io.github.calendrum.value` | decoded values: `ICalDateTime`, `ICalDuration`, `ICalPeriod`, `Recur` |
| `io.github.calendrum.recur` | recurrence rules and sets |
| `io.github.calendrum.tz` | time zone resolvers and the `VTIMEZONE` compiler |
| `io.github.calendrum.occurrence` | occurrences, cursors, free/busy |
| `io.github.calendrum.validate`, `itip`, `fixup`, `lint` | validation, iTIP and iMIP, repairs of known producers, the linter |

## Add the dependency

```kts
// build.gradle.kts of a Kotlin Multiplatform module
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.calendrum:calendrum-core:0.1.0-SNAPSHOT")
        }
    }
}
```

The core depends on the Kotlin standard library and `kotlinx-datetime` only. The optional modules are separate artifacts of the
same group; [Integrations](integrations.md) describes them and [Compatibility](compatibility.md) lists the platforms and the
minimum Kotlin versions. To keep the versions of several modules in step, import `calendrum-bom`.

## Read a calendar

`ICal.parse` accepts bytes, text or a stream and returns a `ParseResult`. In the default lenient mode it never throws on
data: what is wrong is reported as a problem with a stable code and a position.

<!-- sample: getting-started-read -->
```kotlin
val result = ICal.parse(text) // never throws on bad data
val calendar = result.calendars.single() // a typed VCalendar
val summaries = calendar.events.map { it.summary } // ["Weekly sync", "Holiday"]
val start = calendar.events.first().start // 2026-10-01T10:00[Europe/Berlin]
val problems = result.problems.map { it.code.id } // [] for a clean file
```

`result.components` is the lossless syntax tree; `calendars` wraps its `VCALENDAR` components in typed classes whose values
are decoded on first access. [Parsing](parsing.md) covers the tree, problems, charsets and streams, and [The typed
model](model.md) the wrappers.

## Write a calendar

The Kotlin DSL builds a calendar. There is no global clock and no global UID generator: `UID` and `DTSTAMP` come from values
you set or from a `BuildDefaults` that you pass.

<!-- sample: getting-started-write -->
```kotlin
val defaults = BuildDefaults(Clock.System) { newUid() } // UID and DTSTAMP come from you, not from globals
val calendar = VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = defaults) {
    event {
        summary = "Weekly sync"
        start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
        duration = ICalDuration.ofHours(1)
        rrule(Frequency.WEEKLY) {
            byDay(DayOfWeek.THURSDAY)
            count(4)
        }
        attendee("mailto:jane@example.com") { cn = "Jane" }
        alarm { display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Soon") }
    }
}
val ics: String = ICal.writer().encodeToString(calendar.component)
```

`newUid()` is any function that returns a unique string, for example a UUID. See [Building and
editing](building-and-editing.md) for edits, patches and the rest of the builders.

## Compute occurrences

Occurrences turn a recurring event into concrete instants. A time zone is never taken from the system: you pass a resolver,
and the default one reads the `VTIMEZONE` components of the file.

<!-- sample: getting-started-occurrences -->
```kotlin
val options = OccurrenceOptions.builder()
    .zones(TimeZoneResolvers.default(calendar)) // the VTIMEZONE of the file; no system time zone is used
    .build()
val from = Instant.parse("2026-10-01T00:00:00Z")
val to = Instant.parse("2026-10-20T00:00:00Z")
val starts = calendar.occurrences(from, to, options)
    .filter { it.component.property("SUMMARY")?.value == "Weekly sync" }
    .map { it.start }
    // [2026-10-01T08:00:00Z, 2026-10-08T08:00:00Z, 2026-10-15T08:00:00Z]
    .toList()
```

[Recurrence and occurrences](recurrence-and-occurrences.md) and [Time zones](time-zones.md) go further.

## Where to go next

- Reading large or untrusted files, charsets and the problem model: [Parsing](parsing.md).
- Reading only what you need, or straight into your own classes: [Selective parsing and schemas](selective-parsing-and-schema.md).
- Checking and repairing calendars from real producers: [Validation, iTIP and fixups](validation-itip-fixups.md).
- Using the library from Java or Swift: [Java](java.md) and [Swift](swift.md).
- What a problem code means: [Diagnostics](diagnostics.md).
- Moving from ical4j: [MIGRATION](../MIGRATION.md).

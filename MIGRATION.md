# Migrating from ical4j

This guide maps the common tasks of [ical4j](https://github.com/ical4j/ical4j) 4.x to Calendrum and lists the differences
that change results. Calendrum does not port ical4j code; the names below are only for orientation.

The [guides](guides/getting-started.md) describe the Calendrum side of each task in detail; [Java](guides/java.md) explains the
names from Java.

For a gradual move on the JVM, the optional module `calendrum-ical4j-interop` (Java 11, as ical4j 4 needs) converts between
the two models:

```kotlin
val calendar: VCalendar = ical4jCalendar.toCalendrum()           // net.fortuna.ical4j.model.Calendar → VCalendar
val back: net.fortuna.ical4j.model.Calendar = calendar.toIcal4j()
```

Both directions go through text (Calendrum writes byte-stable text, ical4j parses it, and the other way round), so what one
library keeps the other keeps too. A calendar that goes through ical4j and back comes back as the same tree, except that
ical4j prints the parts of a `RRULE` in its own order and `VTIMEZONE` components before the others.

## Tasks

| Task | ical4j 4.x | Calendrum |
|---|---|---|
| Parse a stream | `new CalendarBuilder().build(inputStream)` | `ICal.parser().parse(inputStream)` (JVM), `ICal.parse(bytes)` everywhere; Okio and kotlinx-io: `calendrum-okio`, `calendrum-kotlinx-io` |
| Lenient or strict parsing | `CompatibilityHints.setHintEnabled(KEY_RELAXED_PARSING, true)` (global) | lenient is the default, problems are values (`ParseResult.problems`); `ParseOptions.builder().mode(ParseMode.Strict)` per parser |
| Many calendars, one at a time | read everything | `ICal.parser().components(input)` streams components |
| Components | `calendar.getComponents(Component.VEVENT)` | `calendar.events` (typed) or `component.components("VEVENT")` (tree) |
| A property | `event.getProperty(Property.SUMMARY)` (`Optional`) | `event.summary`, or `component[Properties.SUMMARY]` for any key |
| Date and time | `DtStart<T extends Temporal>` | `ICalDateTime`: `Date`, `Floating`, `Zoned` (with `tzid`), `Utc` — a closed `sealed` type |
| An instant | `dtStart.getDate()` and the JVM zone rules | `start.toInstant(zones)` with an explicit `TimeZoneResolver`; floating and all-day times need an explicit zone |
| Time zones | `TimeZoneRegistryFactory`, the `ical4j~<uuid>` zones of `ZoneRulesProvider` | `TimeZoneResolvers.default(calendar)` (the file's `VTIMEZONE` and aliases), `TimeZoneResolvers.iana`, `TzData.resolver` (`calendrum-tzdata`) |
| Write | `new CalendarOutputter().output(calendar, out)` | `ICal.writer().write(components, out)`; byte-stable for parsed trees |
| Build an event | `new VEvent(start, end, summary)` and mutation | `VEvent.builder(defaults).summary(…).start(…).build()`, `event.toBuilder()` — the model is immutable |
| Recurrences | `component.calculateRecurrenceSet(period)`, `Recur.getDates(…)` | `RecurrenceSet.of(component).between(from, to)`, `Recur.expand(start)` (lazy) |
| Occurrences with overrides | `ComponentGroup` | `calendar.occurrences(from, to, options)` with overrides, cancellations, durations and alarms |
| Validation | `calendar.validate()` (throws `ValidationException`) | `Validator.validate(calendar, RuleSet.Rfc5545)` returns a `ValidationReport` |
| iTIP | `UserAgent`, iTIP transformers | `ITip.request`, `reply`, `cancel`, `counter`… with an explicit `Clock`; `ITip.apply` |
| Vendor quirks | global `CompatibilityHints` | explicit `Fixups` (`outlook`, `lotusNotes`, `google`, `windowsTzids`, `byProdId()`) |
| Split and merge | `Calendars.split`, `Calendars.merge` | `Series.splitByUid`, `Series.merge` |
| `Content-Type` with `method` | `Calendars.getContentType` | `IMip.contentType(method)` |
| JSON | — | jCal (RFC 7265): `calendrum-jcal` |

## What changes

- **No JVM default time zone.** ical4j resolves some floating values through the JVM zone; Calendrum never uses the system
  zone. Pass the zone of the user (`OccurrenceOptions.floatingZone`) where a floating or all-day time must become an instant.
- **IANA zones are opt-in.** By default Calendrum resolves a `TZID` by the `VTIMEZONE` components of the file and by aliases
  (Windows names, Outlook display names, old links); add `TimeZoneResolvers.iana` or `TzData.resolver` to the chain when a
  file uses an IANA `TZID` without a `VTIMEZONE`.
- **Problems are values, not exceptions.** A malformed line, value or `TZID` is reported with a code (`ICAL-S…`, `ICAL-V…`,
  `ICAL-Z…`) and the rest is read; validation never throws.
- **Text escapes.** Calendrum reads `\N` as a line break and decodes `C:\\dir\\\nnext` as `C:\dir\` and a line break, as
  RFC 5545 §3.3.11 says; ical4j 4.3.0 keeps `\N` and misreads an escaped backslash before `n`.
- **Recurrence rules.** Calendrum follows RFC 5545 §3.3.10 literally (it matches an independent reference expansion on thousands
  of random rules); ical4j differs in a few corner cases (for example `BYDAY` duplicates and some `BYSETPOS` and `BYWEEKNO`
  combinations), so a migrated series may gain or lose an instance there.
- **`VTIMEZONE` rules.** Calendrum applies every observance of a `VTIMEZONE`, including `RDATE`; ical4j can miss the last
  observance of some zones (for example a Moscow zone with `DTSTART` and `RDATE` in 2014) and is then an hour off.
- **Immutable model.** Change a component with its builder (`toBuilder()`) or `ICalPatch`; nothing is shared or mutated.
- **No reflection or `ServiceLoader`.** Extensions are installed into an explicit `ICalRegistry`.

# Changelog

All notable changes to Calendrum are documented in this file. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and versions follow
[Semantic Versioning](https://semver.org/spec/v2.0.0.html). Before 1.0, a minor release may break compatibility;
every breaking change is listed here with a migration path.

## Unreleased

Nothing is published yet.

### Added

- Real calendars stay private: `.gitignore` keeps out `corpus/real/` and every file named `real-*` or `seed-real-*`
  (conformance cases, fuzz inputs and golden files cut from them). A checkout without them builds and passes, with their
  tests skipped and the benchmarks of real files left out; with them, `RealCorpusTest.nothingFromTheCorpusIsPublic` fails
  if a file that goes into the repository holds a piece of them. The ical4j interoperability test no longer writes values of
  a real calendar: for those, the only difference it allows is the order of RRULE parts.
- Calendrum is licensed under the Apache License, Version 2.0. `LICENSE` is in the root and, next to `NOTICE`, in
  `META-INF/` of every jar; every POM, the BOM's too, names the license by its SPDX identifier `Apache-2.0`, which is the
  only place for it in a klib.
- `ICAL-L026` (`ProblemCode.ControlCharacter`, Error): the validator and the linter report a content line with a control
  character other than horizontal tab, in its name, a parameter or its value, which RFC 5545 §3.1 forbids. Before, such a
  calendar was valid, although canonical writing, jCal and the builders refuse it. The check reads the bytes of each line and
  decodes nothing; the issue has the path of the property and never quotes it.
- `FingerprintOptions.limits` and `FingerprintOptions.problemListener`: the limits of reading and the problems beyond them. What lies
  beyond a limit takes no part in any hash, so that a change there is not seen, and a listener now tells about it.
- `OccurrenceOptions.floatingICalZone`: the zone of floating times and dates as an `ICalTimeZone`, such as one from
  `TzData.resolver`. Unlike `floatingZone` it needs no time zone database of the platform, so floating events can be read in any
  zone on JS, Wasm and WASI too. Setting both is an `IllegalArgumentException` from `build()`.
- `@ExperimentalICalApi` (API review, item 6): an opt-in marker (`WARNING` level) for public API that is not frozen for 1.0
  and may change in a minor release without a deprecation cycle. It marks the entry points of the registry
  (`ICalRegistry.builder()`, `ICalRegistry.Builder`, `ICalExtension.contribute`), byte-level patches (`ICalPatch`), line
  interceptors (`LineInterceptor.intercept`, `intercept` and `interceptors` of `ParseOptions` and `WriteOptions`), the text
  format of fingerprints (`Fingerprints.encodeToString`, `decodeFromString`), `FeedPublisher` and
  `IMip.untrustedContent`; implementing `PropertyCodec`, `ParameterCodec` or `Fixup` and extending `IcsSchema` need the
  opt-in too (`@SubclassOptInRequired`). Code that uses them adds `@OptIn(ExperimentalICalApi::class)`; nothing else
  changes.
- `ParseLimits.maxInputOctets` (no limit by default, 64 MiB in `ParseOptions.Untrusted`) and the code `ICAL-S030`
  (`ProblemCode.InputTooLarge`): reading stops before the first logical line that does not end within that many bytes of
  input, and the result is truncated. A byte array is checked once; a stream counts the bytes of each block it reads. The
  Ktor `ICalContentConverter` reads at most one byte more than the limit, so a larger body is a truncated result, not an
  exception and not a body held in memory.
- `JCalOptions.maxDepth` (16 by default, like `ParseLimits.maxDepth`) and the code `ICAL-J007` (`ProblemCode.JCalTooDeep`):
  jCal components nested deeper are skipped, and `JCal.decodeFromString` rejects JSON nested deeper than such a jCal can be
  before it is parsed.
- Guides in `guides/` (English): getting started, parsing, the typed model, building and editing, selective parsing and
  schemas, recurrence and occurrences, time zones, validation, iTIP and fixups, integrations, Java, Swift, compatibility,
  diagnostics (every problem code) and performance. Every code fragment is the text of a compiled source, and a test
  keeps the two equal. The README links to the guides.
- Documentation build: Dokka 2.1.0 generates HTML for every published module (`./gradlew dokkaGenerate`), and a `checkKDoc`
  task, part of `check`, reports public declarations without KDoc. Public API of `calendrum-core` gets KDoc, with
  compiled and tested `@sample` examples; no code and no ABI change.
- Public syntax API in `calendrum-core`, the first public API of the library, in the packages `io.github.calendrum`
  and `io.github.calendrum.syntax`: parsing iCalendar into a lossless tree with diagnostics, a streaming cursor,
  component-by-component streaming of large feeds, byte-stable writing, line interceptors and component fingerprints.
  Values are not interpreted yet (the typed model comes next).
  - Parsing. `ICal` (`parse(String)`, `parse(ByteArray)`, `parser()`, `parser(ParseOptions)` and the `ICal.parser { … }`
    DSL); `ICalParser` (`parse` of a `ByteArray`, a range of it, a `String` or a `ByteInput`); `ParseOptions` with its
    `Builder` (mode, limits, problem listener, `retainSource`, charset hint, time source, interceptors); `ParseMode`
    (`Lenient`, `Strict`); `ParseLimits` with its `Builder`; `ParseResult` (components, problems, `problemCount`,
    `isTruncated`, `charset`, `charsetConfidence`).
  - The lossless tree: `ICalComponent`, `ICalProperty` and `ICalParameter` with builders and the `ICalComponent(name) { … }`
    DSL. The tree is immutable and compact: nodes keep byte ranges of the input and decode names and values on first access;
    `retainSource = false` copies the lines instead of holding the input. Names are canonical upper case, known names are
    shared strings. Every node read from input has a `SourceSpan` (offset, line and column). `equals` and `hashCode`
    compare content without recursion, and `toString` never shows values.
  - Diagnostics shared by every layer: `Problem`, `ProblemCode` (an open enumeration with a constant for every code from
    `ICAL-S001` to `ICAL-S029` and `of(id)` for the rest), `Severity`, `ProblemListener`, `ICalParseException` and
    `SourceSpan`; the `@ICalDsl` DSL marker. Lenient parsing never throws on data; Strict throws `ICalParseException` at the
    first problem of severity `Error`. Problems stream to the listener as they are found; `problemCount` also counts
    problems beyond the reporting limit. Values never appear in messages.
  - Limits and budgets: `ParseLimits` bounds the length of a line, the number of lines, components, nesting depth and
    reported problems, and, with `maxTime` (and `ParseOptions.timeSource`, by default `TimeSource.Monotonic`), the time of
    parsing; when the budget runs out, parsing stops with `ICAL-S029` (`TimeBudgetExceeded`, Error) and a partial,
    truncated result; Strict throws. The parser is iterative, so deep nesting cannot overflow the stack.
  - Encodings. Byte input is decoded by the charset decoder: a UTF-8 BOM is skipped; UTF-16 and UTF-32 are recognized by a
    byte order mark, by a hint, or by their zero bytes when there is no mark (`ICAL-S025`); windows-1251, KOI8-R and
    windows-1252 come from a hint (`ParseOptions.charset`, a charset name or a whole `Content-Type` value) or from a
    heuristic with a confidence; one broken sequence in a UTF-8 file keeps it UTF-8. UTF-8 input is read as is, with no copy.
    The diagnostics `ICAL-S024` to `ICAL-S028` explain what was decided. A `String` is read as text.
  - Streaming cursor. `ICalReader` from `ICalParser.reader(bytes)`, `reader(bytes, offset, length)`, `reader(text)` and
    `reader(ByteInput)`: a pull cursor over `Begin`, `Property` and `End` events (`ContentEventType`) with the name, depth,
    implicit ends, parameters, the value as a `CharSequence` view valid until the next event (`valueString()` for a copy),
    positions and spans, and `readComponent()` that builds the subtree of the current `Begin` with the same builder as
    `parse`. It allocates nothing per event; charset detection, limits and the strict mode are those of `parse`.
  - Streaming input and output. `ByteInput` and `ByteOutput`, minimal blocking byte streams (`fun interface`s with the
    `InputStream`/`OutputStream` contract), and `ByteArray.asByteInput()`. The lexer reads a stream in 64 KiB blocks through a
    sliding window, so the cursor and `ComponentStream` use memory bounded by a block and the longest line (a line over
    `maxLineOctets` is skipped without keeping it), not by the input; offsets count from the start of the stream as `Long`. A
    stream gives the same events, tree, problems and spans as a byte array with the same bytes, at any block size.
    `ComponentStream` (`ICalParser.components(ByteArray)` and `components(ByteInput)`) is a single-pass
    `Iterator<ICalComponent>` (and `asSequence()`) over the children of each `VCALENDAR` — and over top-level components that
    are not calendars — with `calendarIndex`, the `calendarProperties` read so far and the `problems` so far. A 100 MB feed
    streams through it in a 128 MiB JVM heap.
  - JVM adapters in `ICalStreams`: `InputStream.asByteInput()`, `OutputStream.asByteOutput()`, `ICalParser.parse(InputStream)`,
    `parse(Reader)` (text, like `parse(String)`), `components(InputStream)`, `ICalWriter.write(…, OutputStream)` and
    `write(…, Writer)`, declared with `@Throws(IOException::class)` for Java, and `ParseLimits.Builder.maxTime(java.time.Duration)`.
    Streams are never closed by the library.
  - Writing. `ICalWriter` from `ICal.writer()` and `ICal.writer(WriteOptions)`: `write` to an `Appendable` or a `ByteOutput`,
    `encodeToString` and `encodeToByteArray` of one component or a list. `WriteOptions` with its `Builder`: `byteStable`
    (default) writes every line of a parsed tree exactly as it was read — spelling, parameters, quotes and folding, with CRLF
    line endings — so a well-formed calendar round-trips byte for byte; hand-built nodes, and everything when
    `byteStable = false`, are written canonically (upper-case names, quotes only where needed, RFC 6868, 75-octet folding).
    The order of properties and child components of parsed nodes is kept. `timeZonesFirst` writes the properties, then every
    `VTIMEZONE`, then the other components of each `VCALENDAR`.
  - Line interceptors. `LineInterceptor` (a `fun interface`) and `InterceptedLine`: a chain of interceptors sees every
    property line inside a component in input order, with its `depth` and `componentName`, reads its `name`, `value` (a
    `CharSequence` view) and parameters, and can `rename` it, `setValue`, `setParameter`, `removeParameter` or `drop` it.
    Names and values are checked like in the builders (`IllegalArgumentException`). `BEGIN`/`END` lines are not seen; after
    `drop()` later interceptors do not see the line. `ParseOptions.Builder.intercept(factory)` and
    `WriteOptions.Builder.intercept(factory)` take a factory `() -> LineInterceptor`, so every `parse`, `reader`,
    `components` and every write gets its own instances and parsers stay thread-safe; `intercept(interceptor)` (and
    `intercept { line -> … }` in the DSL) shares one stateless instance. The tree, the cursor, `readComponent`,
    `ComponentStream` and streamed input see the same edited lines. Without interceptors parsing costs one null check per
    property line; with an interceptor nothing is allocated per line until it edits. An edited line is written canonically,
    every other line stays byte for byte; its node keeps the span of the original line.
  - Fingerprints. `ICal.fingerprints(bytes)` and `fingerprints(bytes, FingerprintOptions)` hash every child of every
    `VCALENDAR` (and every top-level component outside one) with xxHash64 over its canonical lines in one pass over the
    input, without decoding values. Refolding, line endings and the case of names do not change a hash; `DTSTAMP` is ignored
    by default (`FingerprintOptions.ignoredProperties`). `Fingerprints` (`entries`, `algorithmVersion`, `diff`,
    `encodeToString`/`decodeFromString` in a compact, versioned text format), `ComponentFingerprint` (`key`, `hash`,
    `startOffset`, `endOffset`), `ComponentKey` (`calendarIndex`, `name`, then `uid` and `recurrenceId`, `tzid` for
    `VTIMEZONE`, or an `ordinal` among components without one) and `FingerprintDiff` (`added`, `removed`, `changed`,
    `unchangedCount`; duplicate keys are matched in order). `ICalParser.parseComponent(bytes, fingerprint)` parses one
    component by its offsets and gives the same subtree, with the same spans, as the full parse. `toString` shows no values.
  - Round trips byte for byte on the RFC 5545 examples and on every benchmark corpus and the real corpus, in both writing
    modes; on the JVM byte-stable writing runs at 1.6–2.6 GB/s against 40–185 MB/s of ical4j and biweekly writing their own models,
    and parsing into the tree at 0.8–1.0 GB/s against 8–41 MB/s.
- `calendrum-core` and `calendrum-bom` (version alignment) artifacts, with their JVM and klib ABI dumps.

- Public values in `io.github.calendrum.value`, on `kotlinx-datetime` 0.7.0, now an `api` dependency of
  `calendrum-core`: `ICalDateTime` with exactly four forms (`Date` with a `LocalDate`, `Floating` with a `LocalDateTime`, `Utc`
  with a `kotlin.time.Instant`, `Zoned` with a `LocalDateTime` and its `TZID`), `ICalDuration` that keeps its written form
  (`P1W` is not `P7D`, `PT60M` is not `PT1H`) and converts explicitly with `toDateTimePeriod()` and `toDurationOrNull()`
  (only without days and weeks, which are nominal), with factories `of(Duration)`, `ofWeeks`, `ofDays`, `ofHours`,
  `ofMinutes` and `ofSeconds`; `ICalPeriod` with an end or a duration; `Recur`, the value of `RRULE`, with every rule part of
  RFC 5545 and RFC 7529 (`Frequency`, `WeekdayNum`, `MonthNum`, `Skip`) and the unknown parts in order (`RecurPart`). Values
  compare by content; `toString` shows dates, times and rules, but never the values of unknown rule parts.

- Typed keys in `io.github.calendrum.model`: `PropertyKey` and `ParameterKey` with their codecs (`PropertyCodec`,
  `ParameterCodec`, `DecodeContext`, `EncodeContext`), `Properties` with a key for every RFC 5545 property (`STATUS` has one key
  per component type: `EVENT_STATUS`, `TODO_STATUS`, `JOURNAL_STATUS`) and `Parameters` with every RFC 5545 parameter and `EMAIL`.
  Read them from the tree with `component[key]` (`null` when the property is missing or malformed), `component.all(key)`,
  `component.decode(key)` (`Decoded.Value` with the problems of a lenient read, `Decoded.Missing` or `Decoded.Malformed` with the
  problem) and `property[parameterKey]`; from Java they are static methods of `ICalModel`. Decoded values are cached in the
  property node, without making the tree any larger. Own keys need no registration: `PropertyKey("X-ACME-COLOR", codec)`.
- Domain types for properties with parameters: `Attendee` and `Organizer` (with the RFC defaults of `ROLE`, `PARTSTAT`, `RSVP` and
  `CUTYPE`), `Trigger` (`Relative` or `Absolute`), `Attach` (`Uri` or `Binary`), `Geo`, `RequestStatus`, `FreeBusy`,
  `RecurrenceId`, `ExDate`, `RDate`, `Categories`, `Resources`, `Location`, `Contact` and `RelatedTo`; each keeps every parameter
  of the property in order, unknown ones too.
- Open enumerations for RFC values: `PartStat`, `Role`, `CuType`, `RelType`, `FbType`, `ValueType`, `Encoding`, `Related`,
  `RecurrenceRange`, `EventStatus`, `TodoStatus`, `JournalStatus`, `Classification`, `Transparency`, `AlarmAction` and `Method`: constants for the
  known values, `of(value)` for any value, compared by value without regard to case.
- Value diagnostics are public: `ProblemCode` has a constant for every code from `ICAL-V001` to `ICAL-V028`, and a value problem
  points into the value in the input (folded lines and UTF-8 included). New codes: `ICAL-V025` (`VALUE` names a type the property
  does not take), `ICAL-V026` (a value of a non-default type without its `VALUE` or `ENCODING`, such as the
  `TRIGGER:19980101T050000Z` of the RFC 5545 examples), `ICAL-V027` (a value that must be in UTC is not) and `ICAL-V028` (a
  parameter that does not apply, such as `TZID` on a `DATE`).

- Read-only wrappers over the tree: `ParseResult.calendars`, `VCalendar` (`events`, `todos`, `journals`, `freeBusy`,
  `timeZones`), `VEvent`, `VTodo`, `VJournal`, `VFreeBusy`, `VTimeZone` (with its `Standard` and `Daylight` observances) and
  `VAlarm`, with an accessor for every RFC 5545 property of the component (`start`, `end`, `due`, `attendees`, `alarms`…;
  repeatable properties are lists). Wrappers copy nothing: `component` is the node of the tree.
- `ParseOptions.decodeEagerly` (and `Builder.decodeEagerly`): decode every known property while parsing and report the value
  problems in `ParseResult.problems` and the problem listener. Strict mode now decodes eagerly too and throws on the first
  value error.

- Building and editing the typed model (`io.github.calendrum.model`):
  - A DSL, `VCalendar(prodId, defaults) { event { … }; todo { … }; journal { … }; timeZone { … }; freeBusy { … } }` with
    `alarm { … }`, `attendee(address) { … }`, `organizer(address) { … }`, `rrule(frequency) { … }`, `property(key, value)` and
    `property(name, value)`, and Java builders with the same names: `VCalendar.builder()`, `VEvent.builder()`, …,
    `VTimeZone.Standard.builder()`, `Attendee.builder(address)`, `Organizer.builder(address)`. Every typed accessor of a
    wrapper has a property and a fluent method in its builder; repeated properties are added, attendees and child
    components are replaced or removed through `attendees { … }`, `events { … }`, `alarms { … }` and the like.
  - `BuildDefaults(clock, uids)` and `UidGenerator`: a new event, to-do, journal or free/busy component gets its required
    `UID` and `DTSTAMP` from explicit values or from these defaults, never from a global clock; without them `build()`
    throws `IllegalStateException`.
  - `Recur.builder(frequency)`, an RRULE builder that is correct by construction: `COUNT` and `UNTIL` are exclusive, rule
    parts that the frequency does not allow and values out of range are rejected, `BYSETPOS` needs another rule part; the
    type of `UNTIL` is checked against `DTSTART` when the component is built.
  - `edit { … }` and `toBuilder()` on every wrapper: a new immutable copy where unchanged properties and components are the
    same objects, so byte-stable writing keeps their lines as they were; a replaced property stays in place, a new one
    goes after the last property, and an edited `ATTENDEE` keeps its unknown parameters.
  - `ICalPatch`: rewrite only the touched lines of a file. Components are found by `UID` and `RECURRENCE-ID` through
    fingerprints, only they are parsed; properties and their parameters are set or removed with the same rules as
    `edit`; every other byte stays as it was, including line endings. `PatchResult` gives the bytes, the selectors that
    found nothing and the changed lines (kind, property, offset, line — never values).
  - Public constructors for values that go into properties: `Trigger.Relative`, `Trigger.Absolute`, `RecurrenceId`,
    `ExDate`, `RDate`, `FreeBusy`, `Location`, `Contact`, `Categories`, `Resources`, `RelatedTo`, `RequestStatus`,
    `Attach.Uri`, `Attach.Binary` and `Geo`; the parameters that the value needs (`VALUE`, `TZID`, `FMTTYPE`…) are derived.

- Selective parsing (`io.github.calendrum.syntax`): `ICal.parser { select { components(…); properties(…); need(…);
  timeZones = … } }`, `Selection` with the presets `Full`, `Schedule`, `SyncMetadata` and `FreeBusy` and a builder, `Feature`
  (`Occurrences`, `Alarms`, `Participants`) that brings the properties and components a task needs, and `TimeZoneSelection`
  (`All`, `Referenced` — only the `VTIMEZONE` components that the selected properties refer to — and `None`). What a selection
  does not name is skipped by the lexer; time zones, alarms of `need(Alarms)` and free/busy components of the preset are kept
  whole; unknown names are matched exactly. `ParseResult.isPartial` marks the result, and writing a partial tree — the tree,
  wrappers, streamed components — throws `IllegalStateException` unless `WriteOptions.allowPartialWrite` is set, so a
  server never loses what was not read. Java: `ParseOptions.builder().select(selection)`.
- `ParseOptions.Untrusted`: tight limits for files from strangers (lines up to 64 KiB, 1 million lines, depth 8, 100 000
  components, 1 000 problems) and a 2-second time budget; from Java, `ParseOptions.getUntrusted()`.
- A registry of definitions (`io.github.calendrum.model`): `ICalRegistry` with its `Builder` and `Default`,
  `ICalExtension`, `PropertyDefinition` (key, default and allowed value types, parameters, cardinality per component,
  source), `ParameterDefinition`, `ComponentDefinition` and `Cardinality`. The extensions `Rfc5545`, `Rfc7986`, `Rfc9073`,
  `Rfc9074` and `Rfc9253` are all in `ICalRegistry.Default`; extensions add to the definitions of earlier ones.
  `ParseOptions.registry` tells eager decoding and Strict mode which properties to decode, your own X-properties
  included, and `ICalComponent.decodeAny(name)` decodes a property by its definition. Java: `ICalRegistry.getDefault()`,
  `Rfc7986.INSTANCE`.
- RFC 7986, 9073, 9074 and 9253 in the typed model: property keys `NAME`, `REFRESH_INTERVAL`, `SOURCE`, `COLOR`, `IMAGE`,
  `CONFERENCE`, `LOCATION_TYPE`, `PARTICIPANT_TYPE`, `RESOURCE_TYPE`, `CALENDAR_ADDRESS`, `STYLED_DESCRIPTION`,
  `STRUCTURED_DATA`, `ACKNOWLEDGED`, `PROXIMITY`, `CONCEPT`, `LINK` and `REFID`; parameter keys `DISPLAY`, `FEATURE`,
  `LABEL`, `ORDER`, `SCHEMA`, `DERIVED`, `LINKREL` and `GAP`; the types `Image`, `Conference`, `StyledDescription`,
  `StructuredData` and `Link`; the open enumerations `ImageDisplay`, `ConferenceFeature`, `ParticipantType`, `ResourceType`
  and `Proximity`; the `RELTYPE` values of RFC 9074 and 9253 and the value types `UID` and `XML-REFERENCE`;
  `RelatedTo.gap` and `RELATED-TO` with `VALUE=URI` or `UID`. The wrappers `VLocation`, `VResource` and `Participant` with
  builders, and new accessors and builder members in the other wrappers: `VCalendar.names`, `color`, `refreshInterval`,
  `source`, `images`…; `VEvent.conferences`, `locations`, `participants`, `resourceComponents`, `links`…;
  `VAlarm.uid`, `acknowledged`, `proximity`, `relatedTo`, `locations`.
- `VendorProperties`: keys and an extension for 20 frequent vendor X-properties of Microsoft, Apple, Google, Mozilla,
  libical and Yandex (`X-MICROSOFT-CDO-BUSYSTATUS` as the open enumeration `BusyStatus`, `X-WR-CALNAME`,
  `X-PUBLISHED-TTL`, `X-APPLE-STRUCTURED-LOCATION`, `X-ALT-DESC`…); not in `ICalRegistry.Default`.
- Schemas: `IcsSchema<T>` with `field`, `optionalField`, `listField`, `childField` and `create(values)`
  maps components to plain classes of the application without annotations, reflection or code generation.
  `ICalParser.decode(bytes, schema)`, `decode(input: ByteInput, schema)` (a `Sequence`) and `decodeWithProblems` (a
  `SchemaResult` with the items, the problems and the number of skipped components) read only the properties of the
  schema and build no tree; `ICalWriter.encode(items, schema, calendar[, defaults])` writes a feed through the builders.
  A component without a required field is skipped with `ICAL-M001` (`ProblemCode.MissingRequiredField`), and Strict mode
  throws.
- Java: `ICalDurations.toJavaDuration` and `toICalDuration` bridge `ICalDuration` and `java.time.Duration`;
  `VEvent.of(component)` and `of` of every other wrapper wrap a component from a stream or a cursor (`null` for a
  component of another name).
- Recurrence in the package `io.github.calendrum.recur`: `Recur.expand(start[, from, to], options)` expands any
  RRULE of RFC 5545 lazily in local time — every `FREQ`, `INTERVAL`, `COUNT`, `UNTIL`, `BYxxx`, `BYSETPOS` and `WKST`, the
  expand and limit table of §3.3.10, ISO weeks with `WKST` — and `SKIP=OMIT|BACKWARD|FORWARD` of RFC 7529 with
  `RSCALE=GREGORIAN`; other calendars are not expanded. `RecurrenceSet` (`of(component)`, `builder(start)`, `iterator`,
  `between`, `isFinite`) merges DTSTART, RRULE and RDATE without duplicates and removes EXDATE and the old EXRULE;
  `recurrenceSet` of `VEvent`, `VTodo`, `VJournal` and time zone observances. `RecurrenceOptions` (limits, the
  `StartMismatch` policy for a DTSTART that is not an instance of its rule, a problem listener and the `untilOffset` of a
  UTC `UNTIL` with a zoned DTSTART) and `RecurrenceLimits` (1 000 000 occurrences and 10 000 periods without a match by
  default): a limit ends the sequence and is reported, it never hangs or throws. Problem codes `ICAL-R001` to `ICAL-R009`.
  From Java: `Recurrence.expand(recur, start)` and `Recurrence.getRecurrenceSet(event)`.
- Time zones in the package `io.github.calendrum.tz`: `ICalTimeZone` (`offsetAt`, `toInstant` with the gap and
  overlap rules of RFC 5545 §3.3.5, `transitions`), `ZoneTransition`, `TimeZoneResolver` with `ResolveContext`, and
  `TimeZoneResolvers`: `vtimezone(calendar)` compiles the `VTIMEZONE` components of a calendar with the recurrence engine
  (an observance's UTC `UNTIL` is compared with its own `TZOFFSETFROM`, Outlook's `16010101` start is understood), `iana`
  uses the platform database through kotlinx-datetime, `aliases` maps Windows ids and display names, `/mozilla.org/`
  prefixes and old IANA links, `chain(…, unknown)` with `UnknownZonePolicy` (`Floating`, `Utc`, `Fail`), and
  `default(calendar)` (from Java, `defaultFor`). `ICalDateTime.toInstant(zones, floatingZone)` never uses the system time
  zone: floating and all-day values need an explicit zone. `PropertyKey<ICalDateTime>.instant(zones)` reads a schema field
  as an `Instant`. `WriteOptions.addMissingTimeZones(source)` with `VTimeZoneSource` adds a `VTIMEZONE` for every used `TZID`.
  Problem codes `ICAL-Z001` to `ICAL-Z005`. Alias tables come from the IANA tzdb 2026e and CLDR 48 files pinned in
  `third_party/` (see `NOTICE`).
- The new artifact `calendrum-tzdata`: every zone of the pinned IANA tzdb (`TzData.version`, `zoneIds`, `zone(tzid)`, `resolver`)
  in a compact table that gives the same results on every platform, and `TzData.vtimezone(tzid, mode)` writes a
  `VTIMEZONE` with the full history since 1970 (`VTimeZoneMode.Full`) or the current rules only
  (`VTimeZoneMode.OutlookCompatible`); `TzData` is a `VTimeZoneSource`. Every zone agrees with `java.time` from 1970 to 2037,
  apart from the changes of tzdb releases newer than the JDK's. As a `VTimeZoneSource`, `TzData` writes the Outlook form only
  for zones whose current rules it expresses exactly from 2026 to 2037, and the full history for the others (15 of 591 zones
  in tzdb 2026e: Egypt, Morocco, Palestine, and the Canadian provinces that stop changing clocks in 2026);
  `TzData.vtimezone(tzid, mode, from, problemListener)` reports `ICAL-Z005` (`ProblemCode.IrregularTimeZone`) when the
  Outlook form is only an approximation.
- Occurrences in the package `io.github.calendrum.occurrence`: `VCalendar.occurrences(from, to, options)` and the
  cursor `VCalendar.occurrences(options)` (`next`, `after`, `at`, `page`) group components by `UID`, apply `RECURRENCE-ID`
  overrides (`RANGE=THISANDFUTURE` best effort), cancellations and exceptions, compute durations from `DTEND`, `DURATION`
  and `DUE`, intersect with the range as RFC 4791 §9.9 does for events, to-dos and journals, and expand alarms (`AlarmTime`,
  `REPEAT` with a limit). `OccurrenceOptions`, `OccurrenceSet` (`union`, `intersect`, `minus`), and the helpers
  `resolvedStart`, `resolvedEnd`, `isCancelled`, `isAllDay`, `revision()` (`Revision`) and `individuals()`. Problem codes
  `ICAL-O001` to `ICAL-O005`. From Java: `Occurrences.between(calendar, from, to, options)`, `Occurrences.cursor`, `Events.*`.
- Java bridges in the JVM artifact: `ICalDateTimes.toJavaInstant` and `toICalDateTime` convert between `ICalDateTime.Utc`
  and `java.time.Instant`, `ICalDateTimes.toKotlinInstant` and `toJavaInstant` between `kotlin.time.Instant` and
  `java.time.Instant`; `JavaClocks.buildDefaults(java.time.Clock, uids)` and `JavaClocks.toKotlinClock` take a
  `java.time.Clock`; `ICalStreams.decodeStream(parser, input, schema)` decodes a schema from an `InputStream` or a
  `ByteInput` into a `java.util.stream.Stream`.
- Validation in the package `io.github.calendrum.validate`: `Validator.validate(calendar, rules)` returns a
  `ValidationReport` (`issues`, `isValid`, `issues(code)`) of `ValidationIssue`s with a code, a severity, an `ElementPath`
  (`VCALENDAR[0]/VEVENT[3]/ATTENDEE[1]@PARTSTAT`) and the source span. `RuleSet.Rfc5545` checks cardinalities, nesting,
  exclusive and dependent properties, value types, the order of start and end, allowed values and ranges, values that do not
  decode and TZIDs without a VTIMEZONE; `RuleSet.itip(method)` adds the tables of RFC 5546 §3 for `PUBLISH`, `REQUEST`,
  `REPLY`, `ADD`, `CANCEL`, `REFRESH`, `COUNTER` and `DECLINECOUNTER`; `RuleSet.builder()` changes cardinalities and adds
  custom `ValidationRule`s. The rules are data generated from `spec/`. Problem codes `ICAL-L001` to `ICAL-L020`.
- iTIP and iMIP in the package `io.github.calendrum.itip`: `ITip.publish`, `request`, `reply`, `cancel`, `add`,
  `refresh`, `counter` and `declineCounter` build the messages of RFC 5546 with `DTSTAMP` from a `kotlin.time.Clock` and only
  the properties the method allows; `ITip.revise(previous, current)` increments `SEQUENCE` after a significant change;
  `ITip.apply(store, incoming)` applies a message to a calendar and returns an `ITipResult` (`Applied`, `Outdated`,
  `NotFound`, `ResponseRequired`, `Ignored`). `IMip.contentType(method)`, `IMip.checkSender(message, from)` (`SenderCheck`)
  and `IMip.untrustedContent(calendar)` (links, HTML and attachments to show, never to fetch). Nothing is applied on its own.
- Series and feeds: `Series.find`, `splitByUid`, `merge`, `editThis`, `editThisAndFuture` (splits the series
  by default, `ThisAndFuture.Range` writes `RANGE=THISANDFUTURE` instead), `editAll`, `deleteThis` and `deleteThisAndFuture`
  over a `SeriesView`; `Canonical.canonicalize` (a stable form for hashes and ETags) and `Canonical.uidV5` (UUID version 5,
  RFC 9562), `UidGenerator.v5`; `FeedPublisher(clock).publish(previous, current)` sets `SEQUENCE`, `LAST-MODIFIED` and
  `DTSTAMP` only on the components that changed. Free and busy time in `io.github.calendrum.occurrence`: `FreeBusyCalc.busy`,
  `conflicts` and `toVFreeBusy` with `BusyInterval` and `Conflict`.
- Fixups in `io.github.calendrum.fixup`: the `Fixup` interface (line interceptors for parsing and a tree pass
  returning a `FixupResult` with a `FixupChange` per edited property) and `Fixups.windowsTzids`, `xWrTimezone`,
  `xWrCalendarName`, `outlook`, `lotusNotes`, `google`, `vcalendar10` (vCalendar 1.0 with QUOTED-PRINTABLE, its rules and
  alarms), `byProdId()` and `compose(…)`. Every fixup is explicit and idempotent.
- A linter in `io.github.calendrum.lint`: `Linter.lint(calendar, LintRules)` adds interoperability rules (`ICAL-L021` to
  `ICAL-L025`: `X-WR-TIMEZONE` with floating times, non-IANA TZIDs, `X-WR-CALNAME` without `NAME`, `MAILTO:` in upper case,
  empty values) to the validation rules, and `Linter.fix(calendar, report)` fixes what `LintReport.fixable` lists.
- The module `calendrum-jcal`: jCal, the JSON form of iCalendar (RFC 7265), over kotlinx-serialization-json 1.6.2.
  `JCal.encodeToString` and `encodeToJsonElement` write any list of components; value types come from `VALUE` or from the
  registry (`JCalOptions.registry`), and a value its type cannot read is written as `unknown`, so writing never loses data.
  `JCal.decodeFromString` and `decodeFromJsonElement` return a `JCalResult` with the tree and problems `ICAL-J001` to
  `ICAL-J006` instead of throwing. `ICalSerializers` holds hand-written `KSerializer`s for `ICalDateTime`, `ICalDuration`,
  `Recur`, `ICalPeriod` (strings) and `ICalComponent` (jCal, JSON only); no compiler plugin is needed by us or by users.
  The module is in the BOM.
- The modules `calendrum-kotlinx-io` (kotlinx-io 0.3.1) and `calendrum-okio` (Okio 3.7.0, every target of the core except Android
  Native, which Okio does not publish): `Source.asByteInput()` and `Sink.asByteOutput()` (`BufferedSource` and
  `BufferedSink` for Okio), and `ICalParser.parse`, `reader` and `components` of a source and `ICalWriter.write` to a sink, with
  the same results as a `ByteArray`. Writing does not flush the sink. From Java: `ICalKotlinxIo` and `ICalOkio`.
- The module `calendrum-ktor` (Ktor 3.0.0, every target of the core except WASI): `ICalContentConverter` and
  `iCalendar()` for the server and the client `ContentNegotiation` read and write `text/calendar` as `ICalComponent`,
  `VCalendar`, lists of them or a `ParseResult`; on the server a non-UTF-8 `charset` of the request is a charset hint (the
  Ktor client passes UTF-8 to converters, so there the core detects the charset);
  `Route.icsFeed(path) { calendar }` serves a feed with a weak `ETag` of its canonical form, `Cache-Control` (`no-cache` by
  default, `IcsFeedOptions`) and `304 Not Modified` for a matching `If-None-Match`.

### Changed

- Occurrences of a calendar with many series are back at their speed before the protection against keys with one hash:
  components of one `UID` are grouped by a hash of the key with a radix sort and not by sorting the keys as text, and the
  engine keeps the zone it looked up last apart from its tree of zones. On the `personal` corpus the year's occurrences are
  16 percent faster on Wasm, 6 percent on the JVM and 5 percent on Kotlin/Native than before this change. The grouping of iTIP
  and of `Series` uses the same code.
- `Fingerprints.diff` orders the keys of both runs by a hash with a radix sort and matches them in one merge: comparing two
  versions of a 30,000-component feed is 3.8 times faster on the JVM (768 comparisons per second, against 202), 5.0 times on
  Kotlin/Native, 3.5 times on Wasm and 1.4 times on JS. No hash table is keyed by strings of the input, so keys made to share a
  hash still cost comparisons and nothing worse.
- The library is now called Calendrum: the group is `io.github.calendrum`, the artifacts `calendrum-*`, the root package
  `io.github.calendrum`, the plugins and properties of the build `calendrum.*`. `IcsMp.VERSION` is now `Calendrum.VERSION`,
  `toIcsMp()` is `toCalendrum()`, the variable of the key of the anonymizer is `CALENDRUM_ANON_KEY`, and the default `PRODID` of iTIP is
  `-//Calendrum//iTIP//EN`. Nothing was released under the old name, so there is no bridge to it.
- **Breaking** (JVM, API review, item 1): three file facades get names for Java. `TimeZoneResolversKt.toInstant` is
  `TimeZoneConversions.toInstant`, `VCalendarKt.VCalendar` is `VCalendars.VCalendar`, `ByteInputKt.asByteInput` is
  `ByteInputs.asByteInput`. Kotlin sources and klibs are not affected; Java callers change the class name, and Kotlin code
  compiled against an earlier build is recompiled.
- **Breaking** (Swift and Objective-C, API review, items 20 and 21): names that Swift could not use as they were. The
  `description` of `VEvent`, `VTodo`, `VAlarm`, `VLocation`, `VResource`, `Participant`, `RequestStatus` and of their builders
  is `descriptionText` (it was `description_`, because `NSObject` has `description`). Overloads no longer end in `_`:
  `ITip.reviseTodo`, `addTodo` and `counterTodo` (to-dos), `location(text:)` (builders), `byDay(daysOfWeek:)`,
  `write(component:appendable:)` and `write(components:appendable:)`, `encode(items:schema:buildCalendar:)` with or without
  `defaults:`, and `encodeProperty(value:context:)` and `encodeParameter(value:context:)` of the codecs. Kotlin and Java names
  are unchanged.
- **Breaking** (JVM, API review, item 17): `ICalDuration.toDurationOrNull()` has its plain JVM name instead of
  `toDurationOrNull-FghU774`, so Java can call it (it returns a `kotlin.time.Duration`). Kotlin code compiled against an earlier
  build is recompiled. `ICalDuration.of(Duration)` keeps its JVM name: from Java it would take the internal encoding of a
  `kotlin.time.Duration`; Java uses `ofSeconds` and the other factories or `ICalDurations`.
- **Breaking** (source, API review, item 26): `FreeBusyCalc.conflicts` is marked `@ExperimentalICalApi`, so calling it needs
  `@OptIn(ExperimentalICalApi::class)`; code that builds with warnings as errors stops compiling until it opts in. It lists every
  pair of overlapping occurrences, so its time and memory grow with the square of the number of simultaneous events; a limit
  or a lazy sequence may replace the list in a minor release. Binary compatibility and the behaviour do not change. `busy` and
  `toVFreeBusy` stay frozen for 1.0.
- `ICalTimeZone` documents that users implement it and that its members are frozen for 1.0; `UntrustedKind` documents that
  the list may grow, so a `when` over it keeps an `else` branch.
- `JCal.encodeToString`, `JCal.encodeToJsonElement` and `ICalSerializers.component` throw `IllegalArgumentException` on what
  canonical writing rejects: a name that is not a token, a control character other than HTAB, or a parameter without a
  value. They used to write it, and reading the jCal back reported `ICAL-J002` or `ICAL-J003` and skipped it; a parameter
  without a value was written as `""` and read back with one value. Whatever is written as jCal now reads back. The two
  `JCal` functions are `@Throws` for Swift.
- `ICAL-Z003` also means a `VTIMEZONE` with more than 200,000 transitions up to its last start: such a zone is not compiled
  and the next resolver of the chain looks for its `TZID`. Extending the transitions of a zone towards 9999 stops at
  200,000 as well; later times keep the last offset.
- `parse` and `components` of `calendrum-kotlinx-io` and `calendrum-okio` copy a `Buffer` of up to 16 MiB, which holds all its bytes
  already, into one array and parse it as an array; a larger `Buffer` (the copy holds its bytes twice for a moment) and
  other sources are still read block by block. Parsing a `Buffer` is 30 to 56 % faster on the
  JVM, 40 to 87 % on JS, 32 to 39 % on Wasm and 29 to 36 % on Kotlin/Native, and allocates up to 6 times less.
- Faster byte-stable writing: a folded line without bare CR or LF is copied as bytes. Writing the benchmark corpora is 13 to
  68 % faster on every platform (an invitation on the JVM: 518,000 to 811,000 writes per second).
- Faster reading of UTF-8 text on wasmJs: valid UTF-8 is decoded through a `CharArray` instead of the generic decoder of the
  standard library; reading a tree with every value decoded is 16 to 46 % faster. Other platforms are unchanged.
- Faster time zones on JS: the offsets of a zone are looked up by `Double` keys, since every `Long` operation of Kotlin/JS is
  a call. On JS, `offsetAt` is 43 to 45 % faster and converting a local time to an instant 88 % faster; the conversion is
  also 9 % faster on Kotlin/Native and 13 to 18 % on Wasm (one `Long` multiplication instead of several).
- `RecurrenceLimits.maxOccurrences` also bounds the dates a rule with `COUNT` passes before the start of a range
  (`RecurrenceSet.between`, occurrences in a window), with `ICAL-R001`: without `COUNT` the range is reached by arithmetic.
- Selective parsing is faster. A selection with exact unknown property names (the `Schedule` preset names `X-WR-TIMEZONE`) now skips
  the other unknown lines without decoding their names, a kept line no longer looks its name up twice, and
  `TimeZoneSelection.Referenced` stops walking the tree once every `VTIMEZONE` is referenced and compares parameter names without a
  table lookup. On the JVM, `Schedule` is 11–28 % faster on the benchmark corpora and `FreeBusy` 13–24 % faster; `Schedule` now
  matches `Full` on a personal calendar and is still 4–6 % slower on a university timetable where it keeps almost every line.
- An empty line interceptor costs less: on the JVM it now stays within 2 % of parsing without interceptors (it was up to 3.8 %).
- Control characters other than HTAB are no longer accepted in property values (RFC 5545 `VALUE-CHAR`): building a
  property with one throws `IllegalArgumentException`, and so does canonical writing of a parsed property that has one
  (the message names the property, never the value). Byte-stable writing still writes parsed lines as they were read.
- Charset labels `iso-8859-1`, `latin1`, `us-ascii` and their aliases now mean windows-1252, as in the WHATWG Encoding
  standard: servers send windows-1252 under the ISO-8859-1 label. Undefined windows-1252 bytes still decode to U+FFFD.
- `ICAL-S011` now means an encoding that is not supported at all (UTF-7, UTF-1, UTF-EBCDIC, SCSU, BOCU-1, GB18030, EBCDIC);
  UTF-16 and UTF-32 are transcoded instead of rejected.
- Smaller binary size: what the parse, edit and write sample adds to an application, before and after (bytes): iOS framework
  360 160 to 261 256 (code and data sections 301 914 to 212 622), wasmJs `.wasm` 84 618 to 56 424 (gzip 37 041 to 24 791), JS
  106 962 to 94 595 (gzip 37 324 to 31 369), JVM with R8 90 237 to 80 755 and 72 to 66 classes, Android dex 62 972 to
  55 952. The public API and the behaviour are unchanged, except that the `charset` keyword of a charset hint such as
  `text/calendar; charset=utf-8` is now matched by ASCII case folding.
- Faster on Kotlin/Native: the lexer, parsing and charset decoding run 40 to 60 % faster on `macosArm64` (the name byte
  test no longer creates a range object), and writing 8 to 17 % faster; JVM speed is unchanged within noise.
- Faster parsing on Kotlin/Native and JS: on `macosArm64` the lexer runs 4.1 to 4.2 times faster and the structural
  reader 3.5 to 3.6 times (a loop over the input boxed an `Int` for every byte), parsing into the tree 3.2 times,
  component streaming and the cursor 2.4 to 3.7 times, fingerprints 2.6 to 2.7 times, and canonical writing 24 % faster
  (writing text boxed an `Int` per character). On JS the lexer is 19 to 36 % faster and the reader 24 to 27 % (complete
  UTF-8 sequences are checked in place, known names are not validated twice, no `Long` is written per line), and
  fingerprints are 4.7 to 5.0 times faster: xxHash64 runs on pairs of `Int` there, since JS emulates `Long` with
  objects. Wasm is 14 to 31 % faster, the JVM 14 to 32 %. Medians of three interleaved `quick` pairs on the `personal`
  and `university` corpora, Apple M5. The public API, the ABI and the behaviour are unchanged; fingerprints have the
  same values.
- Charset detection looks at most 64 KiB past the first non-ASCII byte, and a single-byte charset hint is checked at the
  first non-ASCII byte when that byte is not in the first 64 KiB, so that a stream and a byte array decide the same way.

- Java reads the property keys through static getters, `Properties.getUID()`, instead of fields: a key and its codec are
  now created on first use, so an application carries only the codecs of the properties it reads. Kotlin code is
  unchanged (`Properties.UID`).
- Much smaller binary size of the typed model: what the size sample (parse, edit, write, read `start` and `attendees`) adds
  to an iOS framework drops from 1 716 820 to 350 982 bytes of code and data sections. `ICalDateTime` creates `LocalDate`
  and `LocalDateTime` only when `date` or `dateTime` is read, and the `GEO` decoder no longer uses `String.toDouble()`,
  so kotlinx-datetime's formatting and kotlinx-serialization stay out of applications that do not use them. Results are
  unchanged: `GEO` coordinates are the nearest `Double`, and `toString` of date-times prints what kotlinx-datetime prints.
- Java reads the parameter keys through static getters, `Parameters.getCN()`, as it reads the property keys; Kotlin code is
  unchanged (`Parameters.CN`).
- `ParseOptions.toString()` names the registry.
- Breaking: the default time zones no longer include the platform IANA database. `TimeZoneResolvers.default(calendar)`,
  `OccurrenceOptions.Default` and the validator resolve a `TZID` by the `VTIMEZONE` of the file, then by aliases (Windows
  names, Outlook display names, old links, vendor prefixes) resolved against the same `VTIMEZONE` components, then by the
  unknown-zone policy; `TimeZoneResolvers.default(null)` resolves nothing. To keep IANA zones, ask for them:
  `TimeZoneResolvers.chain(TimeZoneResolvers.vtimezone(calendar), TimeZoneResolvers.iana, TimeZoneResolvers.aliases,
  unknown = UnknownZonePolicy.Floating)`, or `TzData.resolver` of `calendrum-tzdata`. RFC 5545 requires a `VTIMEZONE` for
  every `TZID` of a file anyway.
- Much smaller binary size of occurrences: what the size sample (parse, edit, write and four weeks of occurrences with the
  default options) adds to an application drops from 2 075 082 to 689 274 bytes of iOS code and data sections, from
  575 496 to 253 818 bytes of JS, from 426 306 to 260 672 bytes of `.wasm`, from 181 452 to 151 248 bytes of Android dex
  and from 291 086 to 251 739 bytes on the JVM with R8. Occurrences, recurrences and the `VTIMEZONE` compiler work in
  seconds without kotlinx-datetime objects, so kotlinx-datetime's zone database, formatting and kotlinx-serialization stay
  out of applications that do not use them. Results are unchanged; a custom `ICalTimeZone` is still read through its
  `offsetAt` and `toInstant`, and a `floatingZone` through kotlinx-datetime.
- Time zone aliases are matched with ASCII case folding.
- The module `calendrum-ical4j-interop` (JVM only, Java 11, ical4j 4.0.0): `Calendar.toCalendrum()` and
  `VCalendar.toIcal4j()` (`Ical4jInterop` from Java) convert through text for a gradual move from ical4j; `MIGRATION.md` maps the
  tasks of ical4j to Calendrum and lists what changes.
- Swift sees the exceptions that depend on data as `throws` (`@Throws`): `ComponentStream.hasNextOrThrow()` and
  `nextOrThrow()` (new; the `Iterator` methods cannot declare exceptions), `ICalWriter` writing and encoding (a parsed value
  that canonical writing rejects), `Fingerprints.decodeFromString`, `TimeZoneResolver.resolve`, `ICalDateTime.toInstant`,
  `resolvedStart`, `resolvedEnd` and the methods of `OccurrenceCursor` (the `Fail` policy for unknown zones). Builders still
  throw `IllegalArgumentException` for invalid names and values without declaring it.
- `ICalWriter.encode(items, schema, calendar)` and `encode(items, schema, calendar, defaults)` take a prepared `VCalendar`,
  so Java encodes through a schema without a Kotlin lambda.

### Fixed

- `FreeBusyCalc.busy` and `FreeBusyCalc.toVFreeBusy` no longer take time quadratic in the number of intervals. A rule of one line
  could produce enough occurrences to run for hours; the merge is now `O(n log n)` with the same result.
- `OccurrenceOptions.problemListener` now hears the problems of the expansion, up to `ICAL-R009`, for a series that starts in UTC,
  on a date, at a floating time or in an unknown zone. It used to hear them only for a series in a known zone, so that reaching
  `ICAL-R001` could cut a list without a signal. `RecurrenceOptions.problemListener` still comes first when both are set.
- jCal no longer writes a `FLOAT` or `GEO` number with more than 15 significant digits as a JSON number. A reader keeps a number as a double, so that
  such a value changed in silence, and Python refused a long one outright. It is written as `unknown` text, as an `INTEGER` above 32 bits is.
  The same holds for a number that is not zero and below the smallest normal double (`0.` with 400 zeros and a `1` has one
  significant digit, and a reader got `0`).
- The anonymizer no longer throws `IllegalStateException` on a line with a stray quote before a parameter it replaces with a link
  or an address (`X;=";SCHEMA=a/b:v`). Such a value is scrambled in place, and the output reads back as the same tree.
- A logical line that starts with a blank line followed by a continuation line now starts at the blank line in the tree, as in
  the lexer and the cursor: its span and its source bytes include the first, empty segment.
- Several empty continuation lines inside a multi-byte UTF-8 character no longer make valid UTF-8 look invalid.
- `Validator.validate` and `Linter.lint` are linear in the size of the calendar: the index of a component or property
  among its namesakes was found by scanning the earlier siblings, so a calendar of 24,000 events was validated 80 times
  slower than one of 1,500 (13 MB/s), and 300,000 overrides of one series took minutes. The paths and the issues are the
  same.
- `Linter.lint`, `Linter.fix` and the fixups no longer throw `IllegalArgumentException` on a parsed property whose value
  has a control character: a property that cannot be rebuilt is left as it was (found by fuzzing). The KDoc of
  `Canonical.canonicalize` and `FeedPublisher.publish` now says that they throw it, as canonical writing does, for a
  control character or a parameter without a value.
- `ITip.apply` no longer throws `IllegalArgumentException` on a `REPLY` whose attendee cannot be rebuilt: a control
  character in its `PARTSTAT`, `DELEGATED-TO`, `DELEGATED-FROM` or address, or a parameter without a value. Such a reply is
  `ITipOutcome.Ignored` and the store is unchanged, so a store that is written canonically still is after any message. The
  override of an instance that a reply answers is no longer added when the reply is outdated (found by fuzzing).
- The fixups and `Linter.fix` leave as it was a property whose name is not a token or that has a parameter without a value
  or with a control character, as they already did for a value with a control character: rebuilding it threw
  `IllegalArgumentException` (found by fuzzing) or left a node that no writer accepts. The `vcalendar10` line interceptor
  no longer throws on a quoted-printable value that decodes to a control character and leaves such lines as they are;
  `xWrCalendarName`, the Google conference link and vCalendar 1.0 alarms skip values with a control character.
- jCal keeps a `VALUE` type that jCal does not define, such as `VALUE=X-FOO`: it is written as the type `x-foo` and reads
  back as `VALUE=X-FOO`; it used to become `unknown`, which lost it, and the second round typed the value by the registry
  (found by fuzzing). A `VALUE` that is not one type name now throws `IllegalArgumentException` on writing.
- `Fixups.byProdId()` fixes a vCalendar 1.0 file from Outlook, Lotus Notes or Google in one pass: it applies the
  vCalendar 1.0 repair and then that of the producer. It used to apply only the former, which writes `VERSION:2.0`, so
  applying it again picked the producer and changed the calendar a second time (found by fuzzing).

### Security

Hostile inputs that ran for minutes, exhausted the memory or the stack (each one is a test in `HostileInputTest`):

- A `VTIMEZONE` of thousands of endless observances no longer runs out of memory (each observance was bounded, the zone was
  not).
- jCal nested 100,000 levels deep no longer overflows the stack (in kotlinx.serialization, which reads arrays by recursion,
  and in the reader).
- Validation, lint and occurrences of thousands of names, `UID`s, `TZID`s or paths with one `hashCode` are no longer quadratic:
  there the library does not key hash tables by strings from the input (on JS and Kotlin/Native 32,768 such `TZID`s took minutes).
  The time zones of occurrences and of the `VTIMEZONE` resolver are kept in a balanced tree, so `TZID`s in any order cost
  O(log n) each (200,000 `TZID`s in descending order took 8 s with sorted insertion).
- `BYSETPOS`, `BYYEARDAY`, `BYWEEKNO` and numbered `BYDAY` with a value repeated a million times no longer cost the whole list
  per period of the expansion.
- Occurrences of a series with thousands of overrides, `EXDATE`s and `THISANDFUTURE` overrides are linear: the overrides are
  checked against the instances in one pass instead of one expansion each.
- `TimeZoneSelection.Referenced` (the `Schedule` and `FreeBusy` presets) with thousands of zones and referenced `TZID`s is linear.
- A `TZID` thousands of characters long is no longer looked up as an alias (the lookup was quadratic in its length).
- A body of any size no longer makes the Ktor converter read it whole into memory when `maxInputOctets` is set (it is in
  `ParseOptions.Untrusted`).
- `ITip.apply` finds a series by an index of the `UID`s of the store instead of a pass over the store for every item of the
  message: a message of thousands of items against a store of tens of thousands of series is no longer quadratic. The
  order of the components of the store is the same as before.
- Fingerprints and their `diff`, the `Referenced` time zone selection, `Series.splitByUid` and `Series.merge`,
  `FeedPublisher.publish`, the fixups and the time zones that `ITip.apply` adds no longer key hash tables by strings from the
  input, so thousands of `UID`s, `TZID`s or component names with one `hashCode` are not quadratic on JS and Kotlin/Native.
- `Series.splitByUid` of a calendar with tens of thousands of `UID`s is linear: every part used to copy all children of the
  calendar first (32,768 series took 26 s). Editing the children of a component that has a property after its children
  (as some exports write `X-WR-CALNAME` last) no longer copies their positions once per child.

The anonymizer (`anonymize` of the CLI, the MCP server and the SDKs, and `:tools:anonymize`):

- Free text in a place of structure no longer passes through. A value there stays only when it is a value of structure:
  a value of the standard's own lists, a number, a duration, an offset, a date or a period, a rule whose every part is one
  of RFC 5545 or RFC 7529, a colour, a media type or a language tag of the list. `CLASS:` or `STATUS:` with a sentence,
  `ROLE=` or `LANGUAGE=` with words, `RRULE` with an `X-` part and `DTSTART:next monday` are replaced like text and counted
  in the summary. Allowed vendor properties keep only flags, numbers, durations, dates, colours and the values the vendors
  document (`BUSY`, `OOF`), not any word in upper case.
- A `VTIMEZONE` is no longer kept whole: its `COMMENT`, `X-` properties and names are replaced like anywhere else, and its
  dates still do not move. A time zone that is not in the zone database or in the list of Windows gets a pseudonym
  `Zone-<hex>`, the same one in its `VTIMEZONE`, in every `TZID=`, in `TZID-ALIAS-OF`, `X-WR-TIMEZONE` and `X-LIC-LOCATION`.
- A `PRODID` stays only when it names a known calendar program and its other words are words of a product name (`Inc`,
  `Calendar`, `NONSGML`): it used to stay whenever such a name was anywhere in it, also inside another word.

### Internal

Not public API and not published; kept here so that the history of the project is complete.

- `checkPrivateReferences`, part of `check`: no file that goes into the repository may point at the maintainers' private
  design documents, decisions or tasks. Code comments that pointed at them now give the reason in words. `AGENTS.md` is
  public, in English, with the rules for coding agents; local instructions of the maintainers go into the ignored
  `AGENTS.local.md`.
- The API shared between the modules (`@InternalICalApi`) is hidden from Objective-C and Swift (`@HiddenFromObjC`); the
  header of `calendrum-core` is about 500 lines shorter.
- The `docs` job of `ci.yml` builds the HTML API reference (`./gradlew dokkaGenerate`) and keeps it as the `dokka-html` artifact
  for seven days.
- `./gradlew syncGuides` copies the compiled source regions into the code blocks of `guides/*.md`, and
  `./gradlew syncDiagnosticsGuide` the KDoc of the `ProblemCode` constants into the tables of `guides/diagnostics.md`; run them
  after `format`.
- Hostile-input tests (`HostileInputTest`, every platform) and a JVM guard against quadratic validation
  (`ValidationScalingTest`); the benchmark `RealOccurrencesBenchmark` measures a semester of the real university schedule.

- IDE sync no longer fails on sources jars: Android Studio asks Gradle for the `-sources.jar` of every dependency and target, and
  `gradle/verification-metadata.xml` had no checksums for them, so the import failed with `Dependency verification failed`.
  `*-sources.jar` is now in `<trusted-artifacts>`, the Gradle sources archive `gradle-9.3.1-src.zip` and the Kotlin stdlib 2.0.20 metadata, which the import also
  resolves, have their own checksum entries, and the Groovy that ships inside the Gradle API (`org.apache.groovy`) is trusted; checksums of every other artifact (jars, klibs, `.pom`, `.module`, toolchains)
  are still required.
- Benchmarks: the `cold-start` suite (`:benchmarks:coldStartBenchmark`): the first parse of the invite in a new process on
  the JVM and on `macosArm64`, against ical4j and biweekly, with the time of the call and the lifetime of the process.

- Build infrastructure:
  - Gradle 9.3.1 wrapper and a version catalog. Artifacts are built with Kotlin 2.3.21:
    we build with the oldest supported Kotlin line, 2.3.
  - Convention plugins in `build-logic/`:
    - `calendrum.kmp-library`: targets by support tier, explicit API mode, language and API version 2.3,
      warnings as errors, JVM bytecode for Java 8, and the ABI validation built into the Kotlin Gradle plugin;
    - `calendrum.lint`: ktlint through Spotless with the official Kotlin coding conventions, `lint` and `format` tasks;
    - `calendrum.publishing`: `maven-publish` to the local Maven repository.
  - Test coverage with Kover 0.9.9 in the `calendrum.kmp-library` convention plugin, applied to every published KMP
    module (`calendrum-core` for now): `koverHtmlReport`, `koverXmlReport` and `koverLog` for a per-project and an
    aggregated root report, JVM only (`commonMain` and `jvmMain` through `jvmTest`). No coverage threshold yet;
    `ci.yml` uploads the report as the `kover-report` artifact on every run.
  - The `calendrum.targets` Gradle property selects the targets to build: `all`, `tier1`, `web`, `apple`, `windows` or `jvm`.
  - GitHub Actions workflows, manual runs only for now: Linux checks, Kotlin/Native, macOS, Windows,
    a full pre-release run and publishing to GitHub Packages.
  - Kotlin stdlib dependency: 2.3.0 for the JVM; JS, Wasm and Native depend on the stdlib of the compiler (2.3.21).
  - Consumer compatibility checks in `compat-tests/`: real consumer projects built against the locally published
    artifacts with Kotlin 2.2 (JVM), 2.3, 2.4 and the current beta (JVM, JS, Wasm, linuxX64),
    and the manual `compat.yml` workflow; linuxX64 consumers run in `native.yml`. The consumers call `ICal.parse` and write
    the parsed calendar back.
  - Size budget in `size-budget/`: the same small consumer app is built with and without Calendrum, and the difference
    (JVM jar after R8, JS bundle, wasmJs `.wasm` and loader, raw and gzipped; Android release APK dex after R8;
    iOS dynamic framework binary) is checked against an accepted baseline; growth of more than 5% and more than 1 KB
    fails the check. Platforms that the host cannot build are skipped and keep their baseline. The manual `size.yml`
    workflow measures JVM, JS, Wasm and Android; `macos.yml` measures iOS. The app parses, edits and writes a calendar.
  - Minimum Kotlin for consumers: 2.2 on the JVM and Android, 2.3.0 on Kotlin/Native, 2.3.20 on JS and Wasm.
  - Benchmarks module `benchmarks/` on kotlinx-benchmark 0.5.0 (not published): JVM (JMH with the `-prof gc` allocation
    profiler), JS, Wasm and the Kotlin/Native target of the host, `quick` and `full` profiles, a suite filter,
    an `environment.json` record of the machine and tools next to every run, and a Markdown summary in the format
    of the results history. The `sanity` suite checks the pipeline on every platform. The manual `benchmarks.yml`
    workflow runs a profile on Linux (JVM, JS, Wasm, linuxX64).
  - Deterministic synthetic corpus generator for benchmarks (`io.github.calendrum.benchmarks.corpus`): a portable
    SplitMix64 PRNG and a small RFC 5545 writer (75-octet folding, CRLF, TEXT escaping) independent of the library
    writer, producing `invite`, `invite-batch`, `personal`, `university` and `huge` (up to ~100 MB, streamed)
    with non-ASCII content, VTIMEZONE for 7 IANA zones, RRULE/RECURRENCE-ID/EXDATE. `:benchmarks:generateCorpora`
    writes the corpora and a `SHA256SUMS` next to a checked-in baseline, `benchmarks/corpora.sha256`. New
    `corpus-scan` suite and commonTest/jvmTest coverage (byte-length and FNV-1a64 determinism, structural
    invariants, SHA-256 against the baseline) run on every platform of the host; `environment.json` now records the
    generator version, seed and corpus checksums when a run uses them.
  - Rivals baseline: ical4j 4.3.0 and biweekly 0.6.8, JVM-only and only in
    `benchmarks`'s `jvmMain` (not a dependency of any published module), measured with the same corpora as our own
    suites at equal levels of work (`baselineParse`, `baselineReadAll`, `baselineWrite`, `baselineRrule`). No network
    during measurement (ical4j's timezone updater is disabled by `ical4j.properties`; both libraries build zones from
    each corpus's own `VTIMEZONE`). A new `baselineEquivalence` task and `BaselineEquivalenceTest` compare component,
    property and RRULE-occurrence counts across the generator, ical4j and biweekly on every corpus.
  - Supply chain: Gradle dependency verification with SHA-256 checksums of every dependency, plugin, `.pom` and
    `.module` in `gradle/verification-metadata.xml` (root build and `build-logic`) and
    `size-budget/gradle/verification-metadata.xml`; a changed checksum or an unlisted artifact fails the build.
    Artifacts that only resolve on Linux and Windows (Kotlin/Native, Node.js, Binaryen, aapt2) are listed with their
    source. `compat-tests` are left out: they use many Kotlin and Gradle versions.
  - Reproducible builds: archives without timestamps, in a fixed order and with the same file permissions, and
    Kotlin/Native, JS and Wasm klibs without absolute source paths (`-Xklib-relative-path-base`). Two clean
    `publishToMavenLocal` runs, runs on JDK 21 and 25, and builds in different directories give byte-identical files.
  - The `calendrum.targets` selection, Spotless and Kover are configured once: one `TargetSet` class in `build-logic` reads
    `calendrum.targets` for the library and benchmark conventions and the conformance module; the root lint no longer walks
    `build`, `.gradle`, `.kotlin` and `bin` directories of `compat-tests` and `size-budget` (about 1.4 s down to 0.3 s on a
    warm daemon); the root Kover report lists the published modules explicitly instead of configuring subprojects, a step
    towards Gradle project isolation.
  - The benchmark corpus tests run `invite-batch` and `university` on the JVM only (Kotlin/Native, JS and Wasm keep `invite`,
    `personal` and the cross-platform determinism check of all four corpora), so `macosArm64Test` of the module takes about
    18 s instead of 100 s. A JVM test checks that every `seed-invite.ics` fuzz input still equals the generated `invite`
    corpus, and `:benchmarks:updateFuzzSeeds` rewrites stale copies.
  - JS tests of the core run with a 60-second Mocha timeout (as in the benchmarks module): the default 2 seconds failed a test that takes
    1-2 seconds when Gradle links the Kotlin/Native test binaries in parallel.
  - Repository: `.gitattributes` and `.editorconfig` keep the bytes of `*.ics` files, fuzz inputs and conformance cases
    as they are (no line ending, charset or whitespace normalization); `bin/` (IDE output) is ignored.
  - Contributor documentation: `CONTRIBUTING.md` (building and checking, code style, API proposal first, Definition of
    Done, and how to update the dependency checksums), issue forms and a pull request template. The README and the template
    do not refer to the maintainers' private documents.
- Syntax layer internals (`@InternalICalApi` or `internal`):
  - A byte-level lexer for content lines; a structural reader with recovery and limits, known-name interning and selective
    parsing in the lexer, shared between Calendrum modules through the `@InternalICalApi` opt-in marker.
  - A content line writer core with 75-octet folding that never splits a code point, parameter quoting only where needed and
    RFC 6868 encoding, writing UTF-8 bytes or to an `Appendable` with identical results, plus one-pass TEXT escaping and
    unescaping. `ContentLineWriter` is an abstract base with two final outputs, `ByteLineWriter` (the former no-argument
    constructor, `size`, `toByteArray`) and `AppendableLineWriter` (the former `Appendable` constructor); an `Appendable` writer
    has no `size` or `toByteArray` any more. The `@InternalICalApi` diagnostics (`Problem`, `Severity`, `ProblemSink`) were
    replaced by the public `io.github.calendrum` types.
  - Input decoding for non-UTF-8 calendars: new lexer and reader constructors take an optional charset hint with IANA, WHATWG
    and JDK aliases; positions of a transcoded input are in the decoded UTF-8 text with the original line numbers. `decodeInput`
    is shared through `@InternalICalApi`. The tables and constants of the syntax layer (known names, problem codes, charset
    heuristic and signatures, content selection) are built from packed data by plain loops, `Byte` range tests no longer create
    `IntRange` objects on Kotlin/Native, the `toString` of nodes and options share one helper, and xxHash64 runs on pairs of `Int`
    on JS through `expect`/`actual`.
  - A calendar anonymizer for the test corpus and for bug reports, in `io.github.calendrum.anonymize` behind `@InternalICalApi`, and a
    command line tool in the new unpublished `tools` module:
    `./gradlew :tools:anonymize --args="<input> <output> [--key <hex>] [--shift-weeks <n>]"` for a file or a directory.
    The structure stays: components, the names and order of properties and parameters, dates, recurrence rules, time
    zones, structural parameters and a short list of vendor X- properties; unchanged lines are written byte for byte.
    Texts are replaced character by character with a character of the same class and UTF-8 length, so escaping and the
    vendor's folding stay; addresses, UIDs and links become deterministic `example.invalid` values that keep the relations
    between events. Replacements come from a secret key through HMAC-SHA-256; dates can be shifted by whole weeks. The
    output keeps the charset, byte order mark and line endings of the input; the tool prints a summary without values and
    checks that the output reads back with the same tree shape and problem codes. The key can also come from a file
    (`--key-file <file>`) or from the `CALENDRUM_ANON_KEY` environment variable, which keep it out of the shell history and the
    process list; `--key` and `--key-file` together are an error. A URI value without a scheme that is a bare name, such as
    Apple's alarm sound `ATTACH;VALUE=URI:Chord`, is kept as is; a host, a path or an address without a scheme is replaced.
    A leak test runs on every benchmark corpus and on sample calendars, and `:tools:test` runs in `ci.yml`.
    A property line that starts with a blank line and a continuation line keeps that blank segment when the anonymizer rebuilds it (the
    anonymizer used to fail its own read-back check on such a line).
- Value layer internals (`internal`): decoders and canonical encoders for every RFC 5545 value type in
  `io.github.calendrum.value` — `BINARY`, `BOOLEAN`, `CAL-ADDRESS` and `URI`, `DATE`, `DATE-TIME` (floating, UTC and with a
  `TZID`), `DURATION` (the written form is kept: `P1W` is not `P7D`), `FLOAT`, `INTEGER`, `PERIOD`, `RECUR` (with `RSCALE`
  and `SKIP` from RFC 7529; unknown parts are kept in order; `FREQ` is written first), `TEXT` lists, `TIME`, `UTC-OFFSET`, and
  `DATE`, `DATE-TIME` and `PERIOD` lists. Hand-written parsers work on a range of a `CharSequence` without regular
  expressions or intermediate strings; years 0000–9999 and the epoch are in `Long`. A strict mode rejects anything outside the
  RFC grammar; the lenient default accepts known real-world forms (extended ISO 8601 dates, fractional seconds, `P90M`,
  `+03:00`, `-0000`, base64 without padding, empty list items, `COUNT` with `UNTIL` and others) with new `ICAL-V001`…`ICAL-V024`
  diagnostics; a leap second `60` becomes `59` by default. Not public yet: the typed model comes with the next API
  proposal. A `ValueCodecs` facade behind `@InternalICalApi` serves the `benchmarks` and `fuzz` modules, and the value package
  is excluded from the ABI dumps until then.
- The consumer compatibility projects and the size budget app read the typed model too (`calendars`, `event.start`,
  `event.attendees`); a JVM consumer on Kotlin 2.2 needs `@OptIn(ExperimentalTime::class)` where it reads a `kotlin.time.Instant`.
  The `parse-typed` benchmark suite reads every field of the model, lazily and eagerly, and a survey reads the real corpus, the
  synthetic corpora and the conformance cases through every accessor.
- Specification tables in `spec/`: every RFC 5545 property, parameter and component with its value types,
  parameters, cardinality in each component (the §3.6 grammars) and section, and the rules beyond cardinality (`DTEND`
  or `DURATION`, `RRULE` needs `DTSTART`). They are the RFC coverage matrix; JVM tests check the table against itself and
  against the code. A spike generated Kotlin tables from them in `build-logic`; the generated code compiled to the same
  bytes as hand-written code, so keys and wrappers are written by hand and the generator was removed.
- Tests and test data:
  - Value codecs: tests of every value example of RFC 5545 §3.3 and §3.8 and of RFC 7529, round-trip
    properties on random inputs, a differential test of dates and times against `java.time`, a differential table of
    `RECUR` against ical4j and biweekly, a survey that decodes every value of a known type in the real corpus, the
    benchmark corpora and the conformance cases, a twelfth fuzz target (`ValueCodecs`) and the `values` benchmark suite.
  - Line coverage of the JVM tests must stay at or above 95 % (`koverVerify` in `check`).
  - Coverage-guided fuzzing of the syntax layer with Jazzer 0.30.0 on the JVM in the `fuzz` module (not published): eleven
    targets (`Lexer`, `Reader`, `WriterRoundTrip`, `Text`, `Utf8Differential`, `CharsetDifferential`, `TreeRoundTrip`,
    `Anonymizer`, `StreamingDifferential`, `Interceptor`, `Fingerprint`), their seed corpora and libFuzzer dictionaries.
    `:fuzz:test` replays the stored inputs as part of `check` and `ci.yml`; `:fuzz:fuzz` fuzzes one target (`-Pfuzz.target`)
    or every target in turn for `-Pfuzz.duration`; the manual `fuzz.yml` workflow fuzzes every target and uploads findings.
  - A language-neutral conformance suite in the new unpublished `conformance` module: 67 cases in
    `conformance/ical/<case-id>/`, each an `input.ics` plus the expected lossless syntax tree (`expected.tree.json`), the
    expected diagnostics (`diagnostics.json`, code/severity/position only, no message text), metadata (`meta.json`) and, for
    cases tagged `fingerprints`, the expected fingerprints (`expected.fingerprints.json`); JSON Schema documents describe the
    format for the SDKs that will run the same suite in other languages. The cases cover every RFC 5545 §4 example byte for
    byte, the RFC 5545 §3.1 folding example, both RFC 6868 examples, folding across multi-byte UTF-8 characters, every
    line-ending and charset-detection combination, and at least one case per syntax diagnostic from `ICAL-S001` to
    `ICAL-S028`. The Kotlin runner embeds every case into the test sources at build time (a new `generateConformanceSources`
    Gradle task in `build-logic`) and runs identically on every platform this host builds tests for;
    `:conformance:updateExpected` regenerates the expected files from the current parser, for review, never to silence a
    failing case.
  - A real corpus in `corpus/real/<source>/`: anonymized calendar exports with a manifest (`<name>.meta.json`) and the
    expected parse outcome (`<name>.expected-problems.json`) each, described in `corpus/README.md`. Golden tests on the JVM
    (`RealCorpusTest` in `conformance`) check that each file parses leniently with the expected problems and charset, that
    the byte-stable writer reproduces it byte for byte, that the canonical output reads back as an equal tree and that the
    cursor gives the same tree. Small fragments become conformance cases (`real-*`, source `anonymized-real`), on which the
    suite runs the same round-trip checks on every platform, and fuzz seeds. The corpus and everything cut from it stay with
    the maintainer and out of the repository; without them these tests are skipped.
  - Tests of the README examples (`ReadmeExamplesTest`, `ReadmeStreamsTest`, `JavaReadmeExamplesTest`): the code shown in the
    README compiles and does what the README says. Java tests cover the key scenarios of the public API; every core test
    input, every conformance case and the benchmark corpora read the same through streams with blocks of 1 to 16 bytes and
    64 KiB.
- Benchmarks (not published): suites `reader`, `parse` (and `RealParseBenchmark` on the real corpus), `cursor`, `write`,
  `writer`, `charset`, `stream`, `lexer-tree` (`tree` and `readFull` with and without an empty interceptor) and
  `fingerprints` (`fingerprints` on every corpus, `diff` of two versions of `university`) on every platform, and
  `:benchmarks:hugeStreamTest`, which streams the `huge` corpus in a 128 MiB heap and prints the peak. The first comparison
  with ical4j and biweekly is recorded in the maintainers' results history.
- Building and editing: the `patch` benchmark suite (`ICalPatch` of one `PARTSTAT` against parse, edit and
  byte-stable writing on `invite`, `invite-batch` and `university`; code and `@Setup` checks only), tests of patching one
  `PARTSTAT` in the synthetic and the real university schedules (exactly one logical line changes) and of edit round trips
  on the corpora, and a thirteenth fuzz target, `Patch`, that checks a patch against an edit of the parsed tree.
- Selective parsing: the `selective` benchmark suite (presets against a full parse), and the invariant "a
  selection equals the full tree filtered by it" on every conformance case, the synthetic corpora and the real one.
- Registry and new RFCs: `spec/` has rows of RFC 7986, 9073, 9074 and 9253 (one row per property and RFC)
  and the columns `PARTICIPANT`, `VLOCATION` and `VRESOURCE`; `RegistrySpecTest` checks every `Rfc*` extension against
  the rows of its RFC. `CodecRun` decodes from a node or from the reader's current line, so that schemas build no nodes.
- Schemas: the `schema` benchmark suite (`IcsSchema` against the wrappers and against ical4j and biweekly
  with a hand-written mapping of their model to the same class), and corpus tests on `university`, `huge` and the real
  corpus: the same values as the wrappers, no `ICalProperty` node, writing and reading back gives the same list.
- API review: a snapshot of the Objective-C header of the iOS framework, `calendrum-core/api/calendrum-core.h`,
  checked by `checkObjcHeader` in `check` on macOS and written by `updateObjcHeader`.
- `fuzz.yml` runs the 13 fuzz targets in a matrix of three groups, each within 210 minutes at 30 minutes per target;
  `-Pfuzz.target` takes a comma-separated list.
- jCal expectations in the conformance suite (RFC 7265): every case now carries the expected jCal form of its calendars
  (`expected.jcal.json`, 66 cases) or says in `meta.json` why jCal cannot represent it (`"jcal": false` with
  `jcalReason`: a strict-mode rejection, a malformed value of a known property, a parameter without a value); a JSON Schema
  (`expected.jcal.schema.json`) describes the format. The jCal serializer lives in the test code of `conformance` (typed
  values through the property keys and the public value types, `unknown` for unrecognized properties) and runs on every
  platform; `:conformance:updateExpected` writes the jCal files too. Two new cases are the examples of RFC 7265 Appendix B:
  the first matches the RFC exactly, the second differs only where the RFC contradicts its own iCalendar input (property
  order, `BYDAY`, the second event's `DTSTART` and `SUMMARY`), which a test pins down.
- Recurrence: an independent naive expansion on `java.time` judges a differential test of 3 000 random
  rules against ical4j 4.3.0 and biweekly 0.6.8 (Calendrum agrees with it on every rule), a million random rules in the
  separate task `recurrenceStressTest`, the fuzz target `Recurrence` (in the `model` group of `fuzz.yml`), and the benchmark
  suite `rrule`.
- Time zones and occurrences: the IANA tzdb and CLDR `windowsZones.xml` pinned in `third_party/` with
  `third_party/update.sh` and the manual workflow `tzdata-update.yml` (its schedule stays commented out); the tables are
  generated by `build-logic`; `NOTICE` goes into every jar; `jvmTestKiritimati` and `jvmTestStJohns` run the JVM tests
  with other default time zones in `check` and `ci.yml`; an architecture test forbids implicit time zones and clocks; a
  golden file of a semester of the real schedule; the benchmark suites `tz` and `occurrences`.
- Validation: `spec/itip.csv` with the presence tables of RFC 5546 §3, new rule kinds in
  `spec/constraints.csv`, the task `generateValidationRules`, the 52 examples of RFC 5546 §4 as test data, and a test that
  validates every corpus without exceptions.
- Fixups: the fuzz target `Fixups` (in the `tree` group of `fuzz.yml`) and a test that runs every fixup on
  the real corpus and the conformance inputs.
- Acceptance: `./gradlew :tools:referenceFeed` writes a reference feed of 19 cases (time zones, all-day and
  floating events, recurrences with exceptions and overrides, cancellations, alarms, non-ASCII text, attendees, a to-do) with
  the expected occurrences, for manual checks in Google, Apple, Outlook, Yandex and Thunderbird; the benchmark suites
  `validate`, `fixups` and `feedPublisher`.
- Examples: `examples/` holds four separate builds against the local Maven repository: a CLI (summary,
  occurrences, validation, anonymization), a Ktor server with an ICS feed, an Android app with Jetpack Compose and an iOS
  SwiftUI package on an XCFramework built from Kotlin; `examples/run.sh` builds and tests them, and the `examples.yml`
  workflow (manual runs only) runs them on Linux and macOS.
- SDK spike: `sdk/c` is a Kotlin/Native shared library with a small C API (`include/calendrum.h`: parse to jCal,
  occurrences in a window as JSON, version), a Python `ctypes` wrapper with `unittest` tests and benchmarks; `sdk/js` is an
  `@JsExport` facade with a TypeScript example. Neither is published.

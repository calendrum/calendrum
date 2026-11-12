# Calendrum

iCalendar (RFC 5545) for Kotlin Multiplatform.

> **Status: early development.** The syntax layer works: parsing into a lossless tree with diagnostics, a streaming
> cursor, component-by-component streaming of large feeds, byte-stable writing, line interceptors and component
> fingerprints. The typed model reads, builds and edits values, keys and components; recurrence rules expand, time
> zones resolve and occurrences are computed. Validation, iTIP, repairs of known producers, jCal and the stream and Ktor
> adapters work too; only CalDAV below is a plan.
> Nothing is published yet and every API may still change. The repository is private for now.

`calendrum` is a Kotlin Multiplatform library that reads, writes and computes iCalendar data
([RFC 5545](https://www.rfc-editor.org/rfc/rfc5545)). The same code runs on JVM/Android, iOS/macOS, Linux, Windows, JS and Wasm.

**iCalendar comes first.** CalDAV ([RFC 4791](https://www.rfc-editor.org/rfc/rfc4791)) is a separate, optional track for
automated sync, planned after the iCalendar 1.0 release and driven by demand.

## Why a new library

[ical4j](https://github.com/ical4j/ical4j) is the reference implementation on the JVM, but it cannot move to
Kotlin Multiplatform. It finds its factories through `ServiceLoader` and reflection, loads configuration and time zone
data from classpath resources, and keeps global mutable settings. None of that exists on Kotlin/Native, JS or Wasm.

This project is a new design, not a port. It learns from ical4j and copies none of its code.

## Goals

- **No reflection anywhere.** There is no `ServiceLoader`, no classpath resources and no global mutable state.
  The library behaves the same on every target and needs no R8/ProGuard keep rules or GraalVM reflection config.
- **Correct on real-world data.** It covers RFC 5545 and its extensions. Time zones come from embedded `VTIMEZONE` or IANA,
  and `RRULE` expansion is complete, including DST gaps and overlaps. Lenient parsing reports problems instead of crashing.
- **Fast.** The parser works on bytes as a stream, decodes values lazily and allocates little.
  It is benchmarked against ical4j and biweekly.
- **Pay only for what you use.** Choose which components and properties to parse — the rest is skipped in the lexer —
  or map `.ics` straight into your own plain classes through a small schema — no annotations, no compiler plugin, no generated code;
  the schema itself is the selection.
  Fingerprints show which components of a feed changed without parsing them.
- **Pleasant and stable API.** The model is immutable, with typed accessors and a Kotlin DSL, and it is idiomatic from Java and Swift.
  Explicit API mode, ABI dumps and semantic versioning keep it stable.
- **Plays well with others.** The core depends only on the Kotlin stdlib and `kotlinx-datetime`.
  Optional adapter modules add kotlinx-io, Okio, kotlinx-serialization and Ktor support.

## A taste of the API

Part of the API is not frozen for 1.0 and may change in a minor release. It is marked `@ExperimentalICalApi`, and using it needs
`@OptIn(ExperimentalICalApi::class)` (the fragments below leave the opt-in out): line interceptors (`intercept`), `ICalPatch`,
the registry builder (`ICalRegistry.builder()`), the text format of fingerprints (`Fingerprints.encodeToString` and
`decodeFromString`), `FeedPublisher`, `FreeBusyCalc.conflicts` and `IMip.untrustedContent`. The list and what the marker promises
are in [Compatibility](guides/compatibility.md#api-stability). The Java fragments are not affected: `javac` does not check the
marker ([Java](guides/java.md)).

**Works today** — parsing into a lossless syntax tree, a streaming cursor, component-by-component streaming of large
feeds, writing, line interceptors and component fingerprints (`calendrum-core`, packages `io.github.calendrum` and
`io.github.calendrum.syntax`):

```kotlin
// Parse leniently: problems in the data are reported, never thrown
val result = ICal.parse(bytes)                       // or ICal.parse(text)
for (calendar in result.components) {                // top-level components, usually one VCALENDAR
    for (event in calendar.components("VEVENT")) {
        val attendee = event.property("ATTENDEE")
        println("${event.property("SUMMARY")?.value} with ${attendee?.parameter("CN")?.value}")
    }
}

// Every problem has a stable code, a severity and a position; values never appear in messages
result.problems.forEach { println("${it.code.id} at ${it.span.line}:${it.span.column}: ${it.message}") }
println(result.problemCount(ProblemCode.BareLf))     // counts include problems beyond the reporting limit
println("${result.charset} (${result.charsetConfidence}%)")  // UTF-8, or a legacy encoding detected or hinted

// Strict mode throws on the first error; options come from a DSL or, in Java, from a builder
val strict = ICal.parser {
    mode = ParseMode.Strict
    charset = "text/calendar; charset=windows-1251"  // a hint for byte input, checked against the data
    problemListener = ProblemListener { println(it) }
}
try {
    strict.parse(bytes)
} catch (e: ICalParseException) {
    println(e.problems.last())
}

// Trees are immutable, compared by content (without recursion) and can be built by hand
val event = ICalComponent("VEVENT") {
    property("UID", "42@example.com")
    property(ICalProperty.builder("ATTENDEE").parameter("CN", "Jane").value("mailto:jane@example.com").build())
    component("VALARM") { property("ACTION", "DISPLAY") }
}
check(event == ICal.parse(sameEventText).components.single())

// Writing is byte-stable: lines read from a file are written exactly as they were (spelling, quotes, folding; line
// endings become CRLF), so an unchanged calendar comes back byte for byte and an edited one gives a minimal diff
val same: ByteArray = ICal.writer().encodeToByteArray(result.components)   // == bytes for a well-formed file
val ics: String = ICal.writer().encodeToString(event)                       // hand-built nodes are written canonically
val normalized = ICal.writer(WriteOptions.builder().byteStable(false).timeZonesFirst(true).build())
    .encodeToString(result.components)                                    // canonical lines, VTIMEZONE first

// The cursor streams events without building a tree and allocates nothing per event; values are views
val reader = ICal.parser().reader(bytes)
while (true) {
    when (reader.next() ?: break) {
        ContentEventType.Begin -> if (reader.name == "VEVENT") events += reader.readComponent()  // or build a subtree
        ContentEventType.Property -> if (reader.name == "X-WR-TIMEZONE") index(reader.value, reader.offset)
        ContentEventType.End -> Unit
    }
}

// A large feed from a stream: one component at a time, memory bounded by a block and the longest line
FileInputStream(file).use { input ->                  // JVM; ByteInput and ByteArray work on every platform
    val events = ICal.parser { limits = ParseLimits.builder().maxTime(2.seconds).build() }.components(input)
    for (component in events) {                       // children of each VCALENDAR: VTIMEZONE, VEVENT, VTODO…
        if (component.name == "VEVENT") index(component, events.calendarProperties)   // X-WR-TIMEZONE and friends
    }
    events.problems.forEach(::println)                // ICAL-S029 if the time budget ran out
}
FileOutputStream(copy).use { ICal.writer().write(result.components, it) }   // or any ByteOutput; we never close it

// Interceptors fix a feed while it is read — no second pass over the tree, nothing allocated per line until an edit;
// edited lines are written canonically, the rest stay byte for byte
val fixer = ICal.parser {
    intercept { line -> if (line.name == "X-VENDOR-ROOM") line.rename("LOCATION") }
    intercept { line -> if (line.name == "DESCRIPTION" && line.value.length > 10_000) line.drop() }
    intercept { FloatingTimeFix() }                   // a factory: a stateful interceptor gets a fresh instance per parse
}
val publisher = ICal.writer(WriteOptions.builder().intercept { line -> if (line.name.startsWith("X-")) line.drop() }.build())

// Fingerprints tell which components of a feed changed in one pass over the bytes, without parsing values
val now = ICal.fingerprints(feed)                     // one per child of each VCALENDAR; DTSTAMP is ignored by default
val delta = now.diff(Fingerprints.decodeFromString(stored))          // added, removed, changed, unchangedCount
val changed = delta.changed.map { ICal.parser().parseComponent(feed, it) }   // by offsets: the same subtree as parse
store(now.encodeToString())                           // a compact, versioned text
```

**Works today** — the typed model reads the tree (`io.github.calendrum.model` and `io.github.calendrum.value`, on
`kotlinx-datetime`): wrappers for every RFC 5545 component, typed keys for every property and parameter, values that are
decoded on first access and cached in the tree:

```kotlin
val calendar = ICal.parse(bytes).calendars.single()
for (event in calendar.events) {
    when (val start = event.start ?: continue) {        // ICalDateTime: four forms, closed by RFC 5545
        is ICalDateTime.Date -> println("all day ${start.date}")
        is ICalDateTime.Utc -> println("at ${start.instant}")
        is ICalDateTime.Zoned -> println("at ${start.dateTime} ${start.tzid}")   // to an instant with a resolver later
        is ICalDateTime.Floating -> println("at ${start.dateTime} local time")
    }
    event.attendees.filter { it.partStat == PartStat.NeedsAction }.forEach { println(it.address) }
    for (alarm in event.alarms) println("${alarm.action} ${alarm.trigger}")
}

// A missing or broken value is null; decode tells which, with the problem and its position
val event = calendar.events.first()
when (val decoded = event.component.decode(Properties.DTSTART)) {
    is Decoded.Value -> decoded.problems.forEach(::println)          // read leniently, e.g. ICAL-V008
    is Decoded.Malformed -> println(decoded.problem)                 // ICAL-V002 Error: ... at 7:13 (offset 120)
    Decoded.Missing -> println("no DTSTART")
}
val cn: String? = event.component.properties("ATTENDEE").first()[Parameters.CN]
val color = PropertyKey("X-ACME-COLOR", HexColor)   // own keys need no registration; HexColor is a PropertyCodec<Int>

// Eager decoding reports every value problem while parsing; Strict mode does it always and throws on the first error
val eager = ICal.parser { decodeEagerly = true }.parse(bytes)
eager.problems.filter { it.code.id.startsWith("ICAL-V") }.forEach(::println)
```

**Works today** — building and editing (`io.github.calendrum.model`): a DSL and Java builders for every component, an RRULE
builder that is correct by construction, `edit {}` that returns a new immutable copy sharing every unchanged node, and
`ICalPatch` (opt-in, see above), which rewrites only the touched lines of a file:

```kotlin
// UID and DTSTAMP come from explicit values or from BuildDefaults — there is no global clock or generator
val defaults = BuildDefaults(Clock.System) { newUid() }
val calendar = VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = defaults) {
    event {
        summary = "Weekly sync"
        start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
        duration = ICalDuration.ofHours(1)
        rrule(Frequency.WEEKLY) {                       // COUNT xor UNTIL, BYxxx allowed for FREQ and ranges are checked
            byDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            count(10)
        }
        attendee("mailto:alice@example.com") { cn = "Alice"; rsvp = true }
        alarm { display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Soon") }
        property("X-ACME-TAG", "blue")
    }
}
val ics = ICal.writer().encodeToString(calendar.component)

// Edits return a new calendar; unchanged properties and components are the same objects, and byte-stable writing
// keeps their lines as they were; a replaced property stays in place, a new one goes after the last property
val event = calendar.events.single()
val accepted = calendar.edit {
    events {
        replace(event, event.edit {
            sequence = (sequence ?: 0) + 1
            attendees { replace("mailto:alice@example.com") { partStat = PartStat.Accepted } }  // unknown parameters stay
            remove("X-ACME-TAG")
        })
    }
}

// Patch a file without parsing the rest of it: components are found by UID and RECURRENCE-ID through fingerprints,
// changed lines are written canonically, every other byte stays as it was (ETags of CalDAV servers are spared)
val result = ICalPatch(original) {
    component(uid = "42@example.com", recurrenceId = null) {
        attendee("mailto:me@example.com") { set(Parameters.PARTSTAT, PartStat.Accepted) }
        set(Properties.DTSTAMP, ICalDateTime.Utc(Clock.System.now()))
    }
}.apply()
upload(result.bytes)                                    // result.unmatched: selectors that found nothing
result.changes.forEach { println("${it.kind} ${it.property} at line ${it.line}") }   // never the values
```

**Works today** — selective parsing (`io.github.calendrum.syntax`): the lexer skips what a selection does not name, a
partial result cannot be written back by accident, and a profile with tight limits reads files from strangers:

```kotlin
val lessons = ICal.parser {
    select {
        components("VEVENT")                            // VTODO, VALARM… are skipped right in the lexer
        properties(Properties.UID, Properties.SUMMARY, Properties.LOCATION)
        properties("X-ACME-ROOM")                       // unknown names are matched exactly
        need(Feature.Occurrences)                       // DTSTART, DTEND, DURATION, RRULE, RDATE, EXDATE, RECURRENCE-ID
        timeZones = TimeZoneSelection.Referenced        // only the VTIMEZONE components the events refer to
    }
}.parse(bytes)
val changes = ICal.parser { select(Selection.SyncMetadata) }.parse(bytes)   // presets: Schedule, SyncMetadata, FreeBusy
check(lessons.isPartial)                                // writing it throws unless allowPartialWrite is set:
val preview = ICal.writer(WriteOptions.builder().allowPartialWrite(true).build()).encodeToString(lessons.components)
val upload = ICal.parser(ParseOptions.Untrusted).parse(bytes)   // depth 8, lines up to 64 KiB, 100k components, 2 s
```

**Works today** — RFC 7986, 9073, 9074 and 9253 are read like RFC 5545, and a registry (`ICalRegistry`) tells eager
decoding, Strict mode and `decodeAny` what every property is, your own X-properties included; frequent vendor
X-properties are an extension you install. `ICalRegistry.builder()` is marked `@ExperimentalICalApi`; `ICalRegistry.Default`
and the ready-made extensions need no opt-in:

```kotlin
val calendar = ICal.parse(bytes).calendars.single()
println("${calendar.names} ${calendar.color} ${calendar.refreshInterval}")       // NAME, COLOR, REFRESH-INTERVAL
for (event in calendar.events) {
    event.conferences.forEach { println("${it.label}: ${it.uri} ${it.features}") }        // CONFERENCE
    event.locations.forEach { println("${it.name} ${it.structuredData.firstOrNull()?.text}") }  // VLOCATION
    event.alarms.filter { it.acknowledged != null }.forEach { println("dismissed ${it.uid}") }  // RFC 9074
}
val registry = ICalRegistry.builder()
    .install(Rfc5545).install(Rfc7986).install(VendorProperties)                // ICalRegistry.Default has all five RFCs
    .install { it.property(PropertyDefinition(color, ValueType.Text, emptySet(), emptySet(), emptyMap(), "Acme")) }
    .build()
val checked = ICal.parser { this.registry = registry; decodeEagerly = true }.parse(bytes)  // X-ACME-COLOR is checked too
val busy = checked.components.single().components.first().decodeAny("X-MICROSOFT-CDO-BUSYSTATUS", registry)
```

**Works today** — schemas (`IcsSchema`): describe your own plain class once and decode a feed straight into it — the lexer
reads only the properties of the schema and no tree is built — or write a feed from a list of them:

```kotlin
data class Lesson(val uid: String, val start: ICalDateTime, val summary: String, val room: String?, val teachers: List<String>)

object LessonSchema : IcsSchema<Lesson>("VEVENT") {
    private val uid = field(Properties.UID, Lesson::uid)
    private val start = field(Properties.DTSTART, Lesson::start)
    private val summary = field(Properties.SUMMARY, Lesson::summary)
    private val room = optionalField(Properties.LOCATION, { it.room?.let(::Location) }, Location::text)
    private val teachers = listField(
        Properties.ATTENDEE, Lesson::teachers, decode = { it.address }, encode = { Attendee.builder(it).build() },
    )

    override fun create(values: Values) =
        Lesson(values[uid], values[start], values[summary], values[room], values[teachers])
}

val lessons: List<Lesson> = ICal.parser().decode(bytes, LessonSchema)   // or decode(input: ByteInput) → Sequence<Lesson>
val result = ICal.parser().decodeWithProblems(bytes, LessonSchema)     // items, problems, skipped (no UID? ICAL-M001)
val feed: ByteArray = ICal.writer().encode(lessons, LessonSchema, { prodId = "-//Uni//Schedule//EN" }, defaults)
```

**Works today** — recurrence (`io.github.calendrum.recur`): every RRULE of RFC 5545 and the `SKIP` of RFC 7529, expanded lazily
in local time (time zones come next), and the recurrence set of a component with RDATE, EXDATE and old EXRULE:

```kotlin
val event = calendar.events.first()
val set = event.recurrenceSet ?: return                  // null without DTSTART
for (start in set.between(from, to)) println(start)     // [from, to) in local time; EXDATE wins, duplicates are dropped
val next = event.rrule!!.expand(event.start!!).take(10).toList()   // lazy: endless rules are fine
val options = RecurrenceOptions.builder()
    .startMismatch(StartMismatch.Exclude)                // DTSTART the rule does not produce: RFC counts it by default
    .limits(RecurrenceLimits.builder().maxOccurrences(10_000).build())  // limits end the sequence, they never throw
    .problemListener { println(it) }                     // ICAL-R001 limit reached, ICAL-R003 DTSTART not in the rule…
    .build()
val total = Sequence { set.iterator(options) }.count()
```

**Works today** — time zones and occurrences (`io.github.calendrum.tz`, `io.github.calendrum.occurrence`, and the optional
`calendrum-tzdata`): by default `VTIMEZONE` of the file, then Windows and Outlook names, `/mozilla.org/` prefixes and old links
resolved against it; IANA zones of the platform (kotlinx-datetime) or of `calendrum-tzdata` only when you add them to the chain,
so an app that does not ask for them does not pay for the platform zone database; occurrences with overrides, cancellations,
durations and alarms; never the system time zone:

```kotlin
val zones = TimeZoneResolvers.default(calendar)                 // VTIMEZONE → aliases; unknown TZID → floating
val withIana = TimeZoneResolvers.chain(                          // IANA of the platform is an explicit choice
    TimeZoneResolvers.vtimezone(calendar), TimeZoneResolvers.iana, TimeZoneResolvers.aliases,
    unknown = UnknownZonePolicy.Floating,
)
val start: Instant? = event.start?.toInstant(withIana)           // a floating or all-day time needs an explicit zone
val options = OccurrenceOptions.builder()
    .zones(TimeZoneResolvers.chain(TimeZoneResolvers.vtimezone(calendar), TzData.resolver, unknown = UnknownZonePolicy.Utc))
    .floatingZone(TimeZone.of("Europe/Berlin"))                   // where all-day and floating events happen for this user
    .build()
for (occurrence in calendar.occurrences(from, to, options)) {    // RFC 4791 time-range semantics, [from, to)
    println("${occurrence.start}..${occurrence.end} ${occurrence.component.property("SUMMARY")?.value}")
    occurrence.alarms.forEach { println("  alarm at ${it.at}") }  // REPEAT is expanded, with a limit
}
val cursor = calendar.occurrences(options)                      // next(), after(instant), at(instant), page(size, after)
val next = cursor.after(now)
val lessons = OccurrenceSet.of(teaching).minus(OccurrenceSet.of(holidays))
val ics = ICal.writer(WriteOptions.builder().addMissingTimeZones(TzData).build()).encodeToString(calendar.component)
val begins = Properties.DTSTART.instant(zones)                  // a schema field read as an Instant
```

**Works today** — validation (`io.github.calendrum.validate`): RFC 5545 rules and the iTIP tables of RFC 5546 for each method,
as data; a report with paths, never an exception:

```kotlin
val report = Validator.validate(calendar)                        // RuleSet.Rfc5545
if (!report.isValid) report.issues.forEach { println("${it.code.id} ${it.severity} ${it.path}") }
val invitation = Validator.validate(calendar, RuleSet.itip(Method.Request))   // METHOD and the RFC 5546 §3.2.2 table
val house = RuleSet.builder()
    .cardinality("VEVENT", "SUMMARY", Cardinality.One)                       // our feed requires a title
    .forbid("VEVENT", "ATTACH")
    .build()
val strict = Validator.validate(calendar, house)
```

**Works today** — iTIP and iMIP helpers (`io.github.calendrum.itip`): pure functions with an explicit clock, `SEQUENCE` and
`DTSTAMP` by RFC 5546; nothing is added to a calendar or opened on its own (`IMip.untrustedContent` is marked
`@ExperimentalICalApi`, see above):

```kotlin
val request = ITip.request(event, organizer, listOf(bob), clock)            // METHOD:REQUEST, DTSTAMP from the clock
val reply = ITip.reply(request, "mailto:bob@example.com", PartStat.Accepted, clock)
if (IMip.checkSender(reply, smtpFrom) == SenderCheck.Matches) {            // FROM against ATTENDEE (or ORGANIZER) and SENT-BY
    val result = ITip.apply(organizerCalendar, reply)                      // Applied, Outdated, NotFound…
    save(result.calendar)
}
val moved = ITip.revise(previous, edited)                                    // SEQUENCE + 1 when DTSTART, RRULE… changed
val links = IMip.untrustedContent(request)                                   // URLs, HTML, attachments: show, never fetch
val contentType = IMip.contentType(Method.Request)                           // text/calendar; method=REQUEST; charset=UTF-8
```

**Works today** — jCal (`calendrum-jcal`, RFC 7265): the JSON form of any tree, value types from the registry, reading
with `ICAL-J` problems instead of exceptions; hand-written serializers for kotlinx-serialization, no compiler plugin:

```kotlin
val json: String = JCal.encodeToString(ICal.parse(bytes).components)    // [["vcalendar", [...], [...]]]
val element: JsonElement = JCal.encodeToJsonElement(listOf(calendar.component))
val read = JCal.decodeFromString(json)                                // the same tree; malformed parts are skipped
read.problems.forEach { println("${it.code.id} ${it.message}") }       // ICAL-J001 not JSON … ICAL-J006 unknown type
val custom = JCal.encodeToString(components, JCalOptions.builder().registry(registry).build())   // types of X- properties
val start: String = Json.encodeToString(ICalSerializers.dateTime, event.start!!)   // "2026-06-01T10:00:00[Europe/Berlin]"
val events = Json.decodeFromString(ListSerializer(ICalSerializers.component), stored)   // jCal of each component
```

**Works today** — streams of kotlinx-io (`calendrum-kotlinx-io`) and Okio (`calendrum-okio`): thin adapters over `ByteInput` and
`ByteOutput`, the same results as a `ByteArray`:

```kotlin
val result = ICal.parser().parse(source)                    // kotlinx.io.Source or okio.BufferedSource
for (event in ICal.parser().components(source)) println(event.property("UID")?.value)   // one component at a time
val cursor = ICal.parser().reader(source)                   // the event cursor
ICal.writer().write(result.components, sink)                // kotlinx.io.Sink or okio.BufferedSink; flushing is yours
val input: ByteInput = source.asByteInput()                 // for any API that takes ByteInput
```

**Works today** — Ktor (`calendrum-ktor`): `text/calendar` for the server and the client, and an ICS feed with `ETag` and
`304 Not Modified`:

```kotlin
install(ContentNegotiation) { iCalendar() }                  // server or client ContentNegotiation
routing {
    icsFeed("/calendar.ics") { loadCalendar(parameters["team"]) }   // ETag, Cache-Control: no-cache, 304 on If-None-Match
    post("/import") { val calendar = call.receive<VCalendar>(); call.respond(calendar) }
}
val calendar: VCalendar = client.get(url).body()               // the charset is detected (Ktor's client passes UTF-8)
```

From Java the same API is plain static calls and builders:

```java
ParseResult result = ICal.parse(bytes);
ICalParser strict = ICal.parser(ParseOptions.builder().mode(ParseMode.Strict).build());
try { strict.parse(bytes); } catch (ICalParseException e) { e.getProblems(); }
ICalComponent event = ICalComponent.builder("VEVENT").property(ICalProperty.of("UID", "42@example.com")).build();
String ics = ICal.writer().encodeToString(event);
byte[] same = ICal.writer().encodeToByteArray(result.getComponents());
ComponentStream stream = ICalStreams.components(ICal.parser(), inputStream);   // throws IOException
ICalStreams.write(ICal.writer(), result.getComponents(), outputStream);
ParseLimits limits = ICalStreams.maxTime(ParseLimits.builder(), Duration.ofSeconds(2)).build();
ParseOptions fixer = ParseOptions.builder()
    .intercept(line -> { if (line.getName().equals("X-VENDOR-ROOM")) line.rename("LOCATION"); })
    .intercept(FloatingTimeFix::new)                                            // a factory
    .build();
FingerprintDiff delta = ICal.fingerprints(feed).diff(Fingerprints.decodeFromString(stored));
ICalComponent changed = ICal.parser().parseComponent(feed, delta.getChanged().get(0));
VCalendar calendar = ICalModel.getCalendars(result).get(0);
ICalDateTime start = calendar.getEvents().get(0).getStart();
if (start instanceof ICalDateTime.Utc) {
    java.time.Instant instant = ICalDateTimes.toJavaInstant((ICalDateTime.Utc) start);
}
Decoded<ICalDateTime> reason = ICalModel.decode(calendar.getEvents().get(0).getComponent(), Properties.getDTSTART());
VEvent event = VEvent.builder(JavaClocks.buildDefaults(java.time.Clock.systemUTC(), () -> newUid()))
    .summary("Weekly sync")
    .rrule(Recur.builder(Frequency.WEEKLY).byDay(DayOfWeek.MONDAY).count(10).build())
    .attendee(Attendee.builder("mailto:alice@example.com").cn("Alice").build())
    .build();
VEvent moved = event.toBuilder().sequence(1).build();                           // edits from Java
Selection lessons = Selection.builder().components("VEVENT").need(Feature.Occurrences).build();
ParseResult partial = ICal.parser(ParseOptions.builder().select(lessons).build()).parse(bytes);
ParseResult upload = ICal.parser(ParseOptions.getUntrusted()).parse(bytes);
ICalRegistry registry = ICalRegistry.builder().install(Rfc5545.INSTANCE).install(VendorProperties.INSTANCE).build();
ParseResult vendor = ICal.parser(ParseOptions.builder().registry(registry).decodeEagerly(true).build()).parse(bytes);
VEvent streamed = VEvent.of(stream.next());                                       // null for another component
Duration length = ICalDurations.toJavaDuration(streamed.getDuration());           // null if it has days or weeks
String cn = ICalModel.get(streamed.getComponent().property("ATTENDEE"), Parameters.getCN());
List<Meeting> meetings = ICal.parser().decode(bytes, new MeetingSchema());         // MeetingSchema extends IcsSchema<Meeting>
Stream<Meeting> streamed = ICalStreams.decodeStream(ICal.parser(), inputStream, new MeetingSchema());
Iterator<ICalDateTime> dates = Recurrence.getRecurrenceSet(event).iterator(RecurrenceOptions.Default);
Sequence<ICalDateTime> next = Recurrence.expand(event.getRrule(), event.getStart());   // next.iterator()
TimeZoneResolver zones = TimeZoneResolvers.defaultFor(calendar);                 // "default" is a Java keyword
String json = JCal.encodeToString(result.getComponents());                      // calendrum-jcal
ParseResult fromOkio = ICalOkio.parse(ICal.parser(), okioSource);                // calendrum-okio; ICalKotlinxIo for kotlinx-io
List<ICalComponent> read = JCal.decodeFromString(json).getComponents();
Iterator<Occurrence> week = Occurrences.between(calendar, from, to, OccurrenceOptions.Default).iterator();
boolean valid = Validator.validate(calendar, RuleSet.itip(Method.Request)).isValid();
VCalendar reply = ITip.reply(request, "mailto:bob@example.com", PartStat.Accepted, JavaClocks.toKotlinClock(clock));
Instant end = ICalDateTimes.toJavaInstant(Events.resolvedEnd(event, zones));     // kotlin.time.Instant ↔ java.time.Instant
```

**Planned** — CalDAV (sketches, names may change):

```kotlin
// Later, optional track B — CalDAV: discovery, queries, incremental sync
val client = CalDavClient(KtorTransport(httpClient), Credentials.basic(user, appPassword))
val account = client.discover("user@example.com")
val delta = client.sync(client.calendars(account).first(), lastSyncToken)
```

## Planned modules

Maven coordinates are `io.github.calendrum:<artifact>`. Kotlin packages live under `io.github.calendrum`,
because a package name cannot contain a hyphen.

| Artifact | Purpose | Extra dependencies |
|---|---|---|
| `calendrum-core` | parser/writer, model, values, time zones, recurrence, validation, iTIP | `kotlinx-datetime` |
| `calendrum-tzdata` | the IANA time zone database (pinned release, see `NOTICE`): every zone, `VTIMEZONE` output in two modes, the same results on every platform — works today | — |
| `calendrum-jcal` | jCal (RFC 7265) and hand-written serializers for kotlinx-serialization — works today | `kotlinx-serialization-json` 1.6.2 |
| `calendrum-kotlinx-io`, `calendrum-okio` | streaming adapters — works today (Okio has no Android Native targets) | kotlinx-io 0.3.1 / Okio 3.7.0 |
| `calendrum-ktor` | `text/calendar` content negotiation for Ktor client/server and an ICS feed route — works today (no WASI) | Ktor 3.0.0 |
| `calendrum-caldav-core` *(track B, later)* | sans-I/O WebDAV/CalDAV: requests, responses, discovery and sync state machines | — |
| `calendrum-caldav-client` *(track B, later)* | suspend API, sync and ICS feed subscriptions over a pluggable `HttpTransport` | `kotlinx-coroutines-core` |
| `calendrum-caldav-ktor` *(track B, later)* | Ktor transport and authentication | Ktor client |
| `calendrum-ical4j-interop` | JVM only: `Calendar.toCalendrum()` and `VCalendar.toIcal4j()` for a gradual move from ical4j — works today | ical4j 4 |
| `calendrum-bom` | version alignment | — |

## Examples

[`examples/`](examples) has small applications that use Calendrum as a dependency: a command-line tool, a Ktor server with an
ICS feed, an Android app with Jetpack Compose and an iOS app view in SwiftUI on an XCFramework. `examples/run.sh` builds
and tests them against the local Maven repository.

## Documentation

The guides in [`guides/`](guides) are written for users of the library. Every Kotlin, Java and Swift fragment in them is the
text of code that the build compiles (and, for the tests, runs).

- [Getting started](guides/getting-started.md) — dependency, the first read, write and occurrences
- [Parsing](guides/parsing.md) — modes, problems, charsets, limits, streams, the cursor, writing, interceptors, fingerprints
- [The typed model](guides/model.md) — wrappers, values, keys, decoding results, extensions and the registry
- [Building and editing](guides/building-and-editing.md) — the DSL, `edit`, `ICalPatch`, series, feed publishing
- [Selective parsing and schemas](guides/selective-parsing-and-schema.md) — selections, partial results, mapping into your classes
- [Recurrence and occurrences](guides/recurrence-and-occurrences.md) — rules, sets, windows, cursors, free/busy
- [Time zones](guides/time-zones.md) — resolvers, no implicit zone, gaps and overlaps, `calendrum-tzdata`
- [Validation, iTIP and fixups](guides/validation-itip-fixups.md) — validation, scheduling messages, repairs of known producers, the linter
- [Integrations](guides/integrations.md) — jCal, kotlinx-serialization, kotlinx-io, Okio, Ktor, ical4j
- [Java](guides/java.md) and [Swift](guides/swift.md) — using the library from other languages
- [Compatibility](guides/compatibility.md) — Kotlin and Java versions, targets, dependencies, API stability
- [Diagnostics](guides/diagnostics.md) — every problem code
- [Performance](guides/performance.md) — measured throughput, with the caveats
- [MCP server](guides/mcp.md) — the operations of the library for AI assistants, and the boundary around files
- [Command line](guides/cli.md) — `calendrum`: lint, fix, convert, expand, diff and anonymize calendar files, in scripts and CI
- [C API](guides/c-api.md) — `libcalendrum`: every operation through one C function, and what its runtime means inside your process
- [Python](guides/python.md) — the package `calendrum`: every operation as a function, with frozen results, and the rules for `fork`
- [TypeScript and JavaScript](guides/typescript.md) — the package `calendrum`: typed functions, a worker that can stop a call, and floating times
- [.NET](guides/dotnet.md) — the package `Calendrum`: every operation as a method, immutable results, and what two garbage collectors mean in one process

Reference documentation (KDoc) of every public declaration is built with Dokka: `./gradlew dokkaGenerate` writes HTML for all
modules to `build/dokka/html`.

Also:

- [CONTRIBUTING](CONTRIBUTING.md) — building, tests, lint, ABI checks, code style, what a pull request needs, issues and triage
- [RELEASING](RELEASING.md) — how a version is released and which versions are supported
- [CHANGELOG](CHANGELOG.md) — what changed in each release
- [MIGRATION](MIGRATION.md) — moving from ical4j: tasks side by side, what changes, `calendrum-ical4j-interop`

The design documents (roadmap, architecture, standards coverage, benchmarks, decision log) are in Russian and are kept
by the maintainers outside the repository for now.

## License

Calendrum is licensed under the [Apache License, Version 2.0](LICENSE). The data it carries from third parties —
the IANA time zone database and Unicode CLDR — and their notices are listed in [NOTICE](NOTICE).

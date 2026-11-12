# Integrations

The core depends on the Kotlin standard library and `kotlinx-datetime` only. Everything else is an optional module of the
same group (`io.github.calendrum`) that adds one dependency and a thin layer on top of the core. Each module has the same
version; import `calendrum-bom` to keep them aligned.

| Artifact | Adds | Platforms |
|---|---|---|
| `calendrum-jcal` | jCal (RFC 7265) and serializers for kotlinx-serialization (1.6.2) | all |
| `calendrum-kotlinx-io` | streams of kotlinx-io (0.3.1) | all |
| `calendrum-okio` | streams of Okio (3.7.0) | all except Android Native |
| `calendrum-ktor` | `text/calendar` for Ktor client and server, an ICS feed route (Ktor 3.0.0) | all except WASI |
| `calendrum-tzdata` | the IANA time zone database, see [Time zones](time-zones.md) | all |
| `calendrum-ical4j-interop` | conversions to and from ical4j 4 | JVM only |

## jCal

`calendrum-jcal` writes a tree as jCal, the JSON form of iCalendar (RFC 7265), and reads it back. Value types come from the
registry, so `DTSTART` is a `date-time`, `RRULE` a `recur` object and a property the registry does not know is `unknown`
text. Writing never loses data: a value that does not decode in its type is written with the raw text. Like canonical
writing, it throws `IllegalArgumentException` on what parsing keeps but cannot be built again (a control character, a
parameter without a value), since jCal could not read it back.

<!-- sample: jcal-encode -->
```kotlin
val result = ICal.parse(bytes)
val json: String = JCal.encodeToString(result.components) // [["vcalendar", [...], [...]]]
val element: JsonElement = JCal.encodeToJsonElement(result.components)
val read = JCal.decodeFromString(json) // the same tree; malformed parts are skipped
// ICAL-J001 not JSON … ICAL-J006 unknown type
val problems = read.problems.map { it.code.id }
```

Reading follows the principle of the core: problems instead of exceptions. Every problem is an `ICAL-J` code with the JSON path
of the element; a malformed component or property is skipped, a malformed value is copied as it is.

<!-- sample: jcal-problems -->
```kotlin
val read = JCal.decodeFromString("{\"not\": \"jCal\"}")
val codes = read.problems.map { it.code.id } // a problem with a JSON path, not an exception
```

`JCalOptions` takes a registry, which decides the type of your own `X-` properties:

<!-- sample: jcal-options -->
```kotlin
val registry = ICalRegistry.Default // or one with your own X- properties and their types
val custom = JCal.encodeToString(components, JCalOptions.builder().registry(registry).build())
```

### kotlinx-serialization

`ICalSerializers` holds hand-written serializers (no compiler plugin, no reflection) for the value classes and for components,
so that your own `@Serializable` classes can embed them. Values are strings that any format can hold: dates and date-times as in
jCal, a zoned time with the `TZID` in brackets, durations and rules as in iCalendar, a period as `start/end` or
`start/duration`. A component is encoded as jCal and needs the JSON format.

<!-- sample: jcal-serializers -->
```kotlin
// "2026-06-01T10:00:00[Europe/Berlin]"
val start: String = Json.encodeToString(ICalSerializers.dateTime, event.start!!)
val stored = Json.encodeToString(ListSerializer(ICalSerializers.component), calendar.component.components)
// jCal of each component
val events = Json.decodeFromString(ListSerializer(ICalSerializers.component), stored)
```

## kotlinx-io and Okio

These modules are thin adapters over `ByteInput` and `ByteOutput`: reads go straight to the source with the block size the
core asks for, writes go to the sink, and a sink is never flushed or closed for you. Streams give the same results as a
`ByteArray`, whatever block sizes the source hands out. The two modules have the same API in different packages
(`io.github.calendrum.kotlinxio` and `io.github.calendrum.okio`).

<!-- sample: kotlinx-io-streams -->
```kotlin
val result = ICal.parser().parse(source) // kotlinx.io.Source
// one component at a time
for (event in ICal.parser().components(buffer())) uids += event.property("UID")?.value.orEmpty()
val cursor = ICal.parser().reader(buffer()) // the event cursor
ICal.writer().write(result.components, sink) // kotlinx.io.Sink; flushing is yours
val input: ByteInput = buffer().asByteInput() // for any API that takes ByteInput
```

The Okio version is the same with `okio.BufferedSource` and `okio.BufferedSink`:

<!-- sample: okio-streams -->
```kotlin
val result = ICal.parser().parse(source) // okio.BufferedSource
// one component at a time
for (event in ICal.parser().components(buffer())) uids += event.property("UID")?.value.orEmpty()
val cursor = ICal.parser().reader(buffer()) // the event cursor
// okio.BufferedSink; flushing is yours
ICal.writer().write(result.components, sink)
val input: ByteInput = buffer().asByteInput() // for any API that takes ByteInput
```

(`source` and `sink` are a source and a sink of the library, and `buffer()` makes a fresh source of the same bytes. `uids` is a
list of strings.) The functions are extensions in the module's package: import `parse`, `components`, `reader`, `write` and
`asByteInput` from it.

## Ktor

`calendrum-ktor` registers a content converter for `text/calendar`, for the server and for the client. It writes a `VCalendar`, an
`ICalComponent`, a `ParseResult` or a list of them, as UTF-8, and reads the types you ask for. When reading it uses the
charset of the request as the hint of the parser, except for UTF-8 (which is what Ktor passes by default): the charset
detection of the core decides there.

`icsFeed(path) { calendar }` serves a calendar at a path. Each `GET` runs your function, sends `text/calendar` with a weak
`ETag` over the canonical form of the calendar (the order of properties does not change it), `Cache-Control: no-cache`, and
answers `304 Not Modified` when `If-None-Match` matches. `IcsFeedOptions` sets the cache header and the writer.

<!-- sample: ktor-server -->
```kotlin
install(ContentNegotiation) { iCalendar() } // text/calendar for receive() and respond()
routing {
    // ETag, Cache-Control: no-cache, 304 on If-None-Match
    icsFeed("/calendar.ics") { loadCalendar(parameters["team"]) }
    post("/import") {
        val calendar = call.receive<VCalendar>()
        call.respond(calendar)
    }
}
```

On the client, install the same converter into the client's `ContentNegotiation`:

<!-- sample: ktor-client -->
```kotlin
val client = createClient { install(ClientNegotiation) { iCalendar() } }
val calendar: VCalendar = client.get("/calendar.ics?team=core").body() // the charset is detected
```

(`ContentNegotiation` is the server plugin, `ClientNegotiation` the client plugin imported under another name; `loadCalendar`
is your function.)

## ical4j

`calendrum-ical4j-interop` helps with a gradual move from ical4j on the JVM. It needs ical4j 4 and Java 11. Both directions go
through text: Calendrum writes byte-stable text and parses what ical4j writes, so properties and parameters survive as text, and
the result is as faithful as ical4j's own output. ical4j's validation is skipped and its global settings are not touched.

<!-- sample: ical4j-both -->
```kotlin
val ours = oldCalendar.toCalendrum() // ical4j Calendar → VCalendar, as faithful as ical4j's own output
val summary = ours.events.single().summary // "Weekly sync"
val theirs = ICal.parse(text).calendars.single().toIcal4j() // VCalendar → ical4j Calendar
```

See [MIGRATION](../MIGRATION.md) for the old calls next to the new ones.

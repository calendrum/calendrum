# Parsing

The syntax layer (`io.github.calendrum` and `io.github.calendrum.syntax`) turns bytes, text or a stream into a lossless tree of
components, properties and parameters, and tells you what is wrong with the data. It does not interpret values: dates,
durations and rules are decoded later, by the [typed model](model.md), when you ask for them.

The fragments below use these values: `text` is the content of an `.ics` file as a `String`, `bytes` the same as a
`ByteArray`. The `ICal.parser { ... }` DSL is the extension function `io.github.calendrum.parser`.

## Entry points

| You have | You call | You get |
|---|---|---|
| `String`, `ByteArray` | `ICal.parse(...)` or `ICal.parser(options).parse(...)` | `ParseResult`, the whole tree |
| `ByteInput` (a file, a socket, a response body) | `parser.parse(input)` | the same, read in blocks |
| a big feed | `parser.components(input)` | `ComponentStream`: one child component at a time |
| any input, no tree | `parser.reader(input)` | `ICalReader`, an event cursor |
| bytes of a feed and a stored fingerprint | `ICal.fingerprints(bytes)` | which components changed |

`ICal.parse(x)` is `ICal.parser().parse(x)` with the default options. `ICalParser` and `ParseOptions` are immutable and can be
shared between threads. `ByteInput` and `ByteOutput` are tiny interfaces of the core; on the JVM `ICalStreams` adapts
`InputStream`, `OutputStream`, `Reader` and `Writer`, and the kotlinx-io and Okio modules adapt their streams
([Integrations](integrations.md)).

## The tree

`ParseResult.components` holds the top-level components, usually one `VCALENDAR`. Components, properties and parameters are
immutable and compare by content. Values are `String`s as they were written (unescaped, unfolded); nothing is decoded yet.

<!-- sample: parsing-tree -->
```kotlin
val result = ICal.parse(text)
val calendar = result.components.single() // the VCALENDAR; the tree keeps the file as it was
val event = calendar.components("VEVENT").first()
val summary = event.property("SUMMARY")?.value // "Weekly sync"
val cn = event.properties("ATTENDEE").map { it.parameter("CN")?.value } // ["Jane", "Bob"]
val sameContent = event == ICal.parse(text).components.single().components("VEVENT").first() // true
```

Names are compared without regard to case, as the RFC says. Every node parsed from input knows where it came from
(`span`), and the tree remembers the spelling of each line, which is what makes [byte-stable writing](#writing)
possible. Unclosed components are closed at the end of the input or at the next `END`, and the tree says so with a problem
(`ICAL-S016`, `ICAL-S018`), not with an exception.

## Problems

Every deviation from the format is a `Problem`: a stable code (`ICAL-S003`), a severity, a position and a short message that
never contains your data. [Diagnostics](diagnostics.md) lists all codes.

<!-- sample: parsing-problems -->
```kotlin
val result = ICal.parse("BEGIN:VCALENDAR\nVERSION:2.0\nBROKEN\nEND:VCALENDAR\n")
for (problem in result.problems) {
    val at = "${problem.span.line}:${problem.span.column}"
    println("${problem.code.id} ${problem.severity} at $at: ${problem.message}")
}
val bareLf = result.problemCount(ProblemCode.BareLf) // counts include cases beyond the reporting limit
val worst = result.problems.maxOf { it.severity } // Severity.Error (ICAL-S007)
```

- `result.problems` is limited (1000 by default); `problemCount(code)` counts every case.
- Some codes are reported once per input: a file with a million LF line endings gives one `ICAL-S003` and a count of a million.
- `problemListener` on the options receives problems while the input is read, which is the way to log them from a stream.
- A position is in bytes: `offset` from the start of the input (of the stream, for streams), `line` and `column` from 1.
  For input in another charset the position counts bytes of the decoded UTF-8.

## Lenient and strict

The default mode, `ParseMode.Lenient`, never throws on data: it repairs what it can and keeps going. `ParseMode.Strict` stops at
the first problem with the `Error` severity and throws `ICalParseException`, which carries the problems up to and including
it. Warnings do not stop a strict parse. The listener is called before the exception is thrown.

`reported` is a mutable list of strings.

<!-- sample: parsing-strict -->
```kotlin
val strict = ICal.parser {
    mode = ParseMode.Strict
    problemListener = ProblemListener { reported += it.code.id } // also called before the exception
}
val message = try {
    strict.parse("BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n")
    null
} catch (e: ICalParseException) {
    e.problems.last().code.id // the first Error: "ICAL-S007"
}
```

Strict mode also decodes every value and reports value problems (`ICAL-V…`); in lenient mode you can ask for the same with
`decodeEagerly = true` ([The typed model](model.md)).

## Charsets

RFC 5545 requires UTF-8, but real files come in Windows-1251, KOI8-R, Windows-1252 or UTF-16. The parser handles byte input
like this:

1. A byte order mark decides first: UTF-8 is skipped (`ICAL-S001`), UTF-16 and UTF-32 are transcoded (`ICAL-S024`).
2. A hint from `ParseOptions.charset` (a charset name or a whole `Content-Type` value) is checked against the data. If the
   data contradict it, the data win and `ICAL-S027` says so; an unknown hint is `ICAL-S026`.
3. Otherwise the input is read as UTF-8. The decision is made at the first non-ASCII byte, so plain ASCII files pay nothing.
4. If the bytes are not UTF-8, a heuristic picks among Windows-1251, KOI8-R and Windows-1252 and reports its confidence;
   below 90 % you get `ICAL-S028`.

`legacy` is a file whose text is in Windows-1251.

<!-- sample: parsing-charset -->
```kotlin
val hinted = ICal.parser { charset = "text/calendar; charset=windows-1251" }.parse(legacy) // a hint, checked
val detected = ICal.parse(legacy) // no hint: detected from the bytes
val event = hinted.components.single().components("VEVENT").single()
val summary = event.property("SUMMARY")?.value // "Привет"
val charset = detected.charset // "windows-1251"
```

`ParseResult.charset` and `charsetConfidence` tell what was decided. The result is always valid UTF-8 text, and positions refer
to it. Parsing a `String` involves no decoding at all.

## Limits and untrusted input

`ParseLimits` bounds line length, the number of lines, components and problems, the nesting depth, the size of the input
and the time. The defaults suit files you trust; for files from strangers use `ParseOptions.Untrusted` (depth 8, lines up
to 64 KiB, 100,000 components, 64 MiB of input, 2 seconds) or your own limits. A limit that is hit ends the parse with a
problem (`ICAL-S014`, `ICAL-S023`, `ICAL-S030`, `ICAL-S029`) and a result with `isTruncated == true`; it never throws in
lenient mode.

<!-- sample: parsing-limits -->
```kotlin
val limits = ParseLimits.builder()
    .maxLineOctets(64 * 1024)
    .maxComponents(100_000)
    .maxTime(2.seconds) // a budget for hostile input; ICAL-S029 when it runs out
    .build()
val result = ICal.parser { this.limits = limits }.parse(bytes)
// depth 8, 64 KiB lines, 100k components, 2 s
val untrusted = ICal.parser(ParseOptions.Untrusted).parse(bytes)
```

## Streams and large feeds

`parse` builds the whole tree in memory. For a feed of hundreds of megabytes, `components` yields one child of each
`VCALENDAR` at a time (a `VEVENT`, a `VTIMEZONE`…), and memory is bounded by the block size and the longest line. The properties
of the calendar itself are available as `calendarProperties` once the stream has read them.

`seen` is a mutable list of strings.

<!-- sample: parsing-stream -->
```kotlin
val events = ICal.parser().components(bytes.asByteInput()) // any ByteInput: a file, a socket, a response body
for (component in events) { // children of each VCALENDAR, one at a time
    if (component.name == "VEVENT") seen += component.property("UID")?.value ?: continue
}
val calendarProperties = events.calendarProperties // VERSION, PRODID… of the calendar being read
val problems = events.problems // what was found on the way
```

A stream with any block sizes gives the same components, problems and positions as the same bytes in an array.

## The event cursor

`reader` returns an `ICalReader` that walks the file as events (`Begin`, `Property`, `End`) without building a tree and
without allocating per event. Names and values are views that are valid until the next call to `next()`. Call
`readComponent()` at a `Begin` to build just that subtree.

`offsets` is a mutable list of `Long`.

<!-- sample: parsing-cursor -->
```kotlin
val reader = ICal.parser().reader(bytes)
var uids = 0
while (true) {
    when (reader.next() ?: break) {
        // where the event starts
        ContentEventType.Begin -> if (reader.name == "VEVENT") offsets += reader.offset

        // reader.value is a view
        ContentEventType.Property -> if (reader.name == "UID") uids += 1

        ContentEventType.End -> Unit
    }
}
```

## Writing

`ICal.writer()` writes trees back. By default writing is byte-stable: every line that was read comes back as it was
(spelling, quoting, folding), only bare CR or LF line endings become CRLF. An unchanged file therefore comes back byte for
byte, and an edited one produces a minimal diff. Nodes you built by hand are written canonically: folded at 75 octets, with
the escaping the RFC requires. `WriteOptions` turns the stability off, moves `VTIMEZONE` components first, refuses or allows
partial trees from [selective parsing](selective-parsing-and-schema.md#partial-results), adds missing time zones
([Time zones](time-zones.md)) and attaches interceptors.

<!-- sample: parsing-write -->
```kotlin
val result = ICal.parse(bytes)
val same: ByteArray = ICal.writer().encodeToByteArray(result.components) // the file, byte for byte
val normalized = ICal.writer(WriteOptions.builder().byteStable(false).timeZonesFirst(true).build())
    .encodeToString(result.components) // canonical lines, VTIMEZONE first
```

`write(components, output)` takes any `ByteOutput`; the library never closes or flushes what you give it.

## Interceptors

A `LineInterceptor` sees each logical line before it reaches the tree and may rename, drop or change it. Nothing is allocated
per line until an edit happens, and edited lines are written canonically while the rest stay byte for byte. Use them to repair
a known producer's output while reading, or to strip private data while writing. A stateful interceptor is registered as a
factory so that every parse gets a fresh instance.

Interceptors are not frozen for 1.0: `intercept`, `LineInterceptor.intercept` and the `interceptors` of the options are marked
`@ExperimentalICalApi`, so code that registers or implements an interceptor opts in with `@OptIn(ExperimentalICalApi::class)`
(see [Compatibility](compatibility.md#api-stability)).

`feed` is a calendar text; `FloatingTimeFix` is a `LineInterceptor` class of your own that adds `TZID=Europe/Berlin` to a floating `DTSTART`.

<!-- sample: parsing-interceptors -->
```kotlin
val fixer = ICal.parser {
    intercept { line -> if (line.name == "X-VENDOR-ROOM") line.rename("LOCATION") }
    intercept { line -> if (line.name == "DESCRIPTION" && line.value.length > 10_000) line.drop() }
    // a factory: a stateful interceptor gets a fresh instance per parse
    intercept { FloatingTimeFix() }
}
val event = fixer.parse(feed).components.single().components("VEVENT").single()
val location = event.property("LOCATION")?.value // "Room 101"
val zone = event.property("DTSTART")?.parameter("TZID")?.value // "Europe/Berlin"
```

The ready-made repairs for common producers are in [`Fixups`](validation-itip-fixups.md#fixups).

## Fingerprints

For a feed you poll, parsing everything to learn what changed is wasteful. `ICal.fingerprints` hashes each child component of
each calendar in one pass over the bytes, without parsing values, and ignores `DTSTAMP` by default. Store the compact text
form, compare it with the next poll, and parse only the components that changed.

`feed` is the new content of the feed as bytes and `stored` is the text that `encodeToString()` returned last time.

<!-- sample: parsing-fingerprints -->
```kotlin
val now = ICal.fingerprints(feed) // one pass over the bytes; DTSTAMP is ignored by default
val delta = now.diff(Fingerprints.decodeFromString(stored)) // added, removed, changed, unchangedCount
val changed = delta.changed.map { ICal.parser().parseComponent(feed, it) } // the same subtree as a full parse
val toStore = now.encodeToString() // a compact, versioned text
```

Types: `diff` returns a `FingerprintDiff` (`added`, `removed`, `changed` and `unchangedCount`); each entry is a
`ComponentFingerprint` with its `key` (`ComponentKey`: calendar index, component name, `UID`, `RECURRENCE-ID`, `TZID` and the
position among equal keys), the `hash` and the byte offsets that `parseComponent` reads.

`parseComponent` reads by offsets, so the subtree is the same as in a full parse, with the same positions. The text form is
versioned but not frozen for 1.0: `encodeToString` and `decodeFromString` are `@ExperimentalICalApi`, and text stored by one
release may need fingerprinting the feed again after an upgrade.

## Next

- Typed access to the tree: [The typed model](model.md).
- Reading only part of a file: [Selective parsing and schemas](selective-parsing-and-schema.md).
- Code by code: [Diagnostics](diagnostics.md).

# Selective parsing and schemas

A calendar app rarely needs every property of every component. Two features let you pay only for what you use:

- a **selection** tells the lexer which components and properties to read, and the rest is skipped by bytes;
- a **schema** describes your own class once and decodes the file straight into it, with no tree.

Both leave the unselected bytes alone: no parameter parsing, no UTF-8 check, no line-length check and therefore no problems
about them. `BEGIN` and `END` lines are always read.

The fragments use `bytes`, the content of an `.ics` file as a `ByteArray`.

## Selections

`ICal.parser { select { ... } }` takes a `Selection` built from component names, property names or typed keys, and `need(...)`
for groups of properties that belong together. Unknown names are matched exactly (ignoring case), so your own `X-` properties
can be selected too.

<!-- sample: selective-select -->
```kotlin
val lessons = ICal.parser {
    select {
        components("VEVENT") // VTODO, VALARM… are skipped right in the lexer
        properties(Properties.UID, Properties.SUMMARY, Properties.LOCATION)
        properties("X-ACME-ROOM") // unknown names are matched exactly
        // DTSTART, DTEND, DURATION, RRULE, RDATE, EXDATE, RECURRENCE-ID
        need(Feature.Occurrences)
        timeZones = TimeZoneSelection.Referenced // only the VTIMEZONE components the events refer to
    }
}.parse(bytes)
// presets: Schedule, SyncMetadata, FreeBusy
val changes = ICal.parser { select(Selection.SyncMetadata) }.parse(bytes)
```

| Piece | Meaning |
|---|---|
| `components(...)` | the components to read; their children are read only if they are selected too (`VALARM` inside `VEVENT`) |
| `properties(...)` | properties to read in every selected component |
| `need(Feature.Occurrences)` | everything needed to compute occurrences: `DTSTART`, `DTEND`, `DURATION`, `RRULE`, `RDATE`, `EXDATE`, `RECURRENCE-ID`, with the time zones they use |
| `need(Feature.Alarms)`, `need(Feature.Participants)` | `VALARM` whole; `ATTENDEE` and `ORGANIZER` |
| `timeZones` | `All` (default), `Referenced` (only the `VTIMEZONE` whose `TZID` a kept component uses) or `None` |

The presets are `Selection.Full` (the default), `Schedule` (events with the properties a calendar view shows and the time
zones they use), `SyncMetadata` (every component with `UID`, `SEQUENCE`, `LAST-MODIFIED`, `DTSTAMP`, `RECURRENCE-ID` and no
time zones) and `FreeBusy`.

`TimeZoneSelection.Referenced` needs the whole calendar before it can decide, so it applies to `parse`; streams and the cursor
keep all time zones.

## Partial results

A tree read with a selection is marked partial (`ParseResult.isPartial`). Writing it back would silently drop everything you
did not select, so the writer refuses with an `IllegalStateException` unless you allow it.

<!-- sample: selective-partial -->
```kotlin
val partial = lessons.isPartial // true: this is not the whole file
val failure = assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(lessons.components) }
val preview = ICal.writer(WriteOptions.builder().allowPartialWrite(true).build())
    .encodeToString(lessons.components) // only when you really mean it
```

To change a file you only partly read, use [`ICalPatch`](building-and-editing.md#patch-a-file), which leaves every other byte alone.

## Files from strangers

`ParseOptions.Untrusted` combines tight [limits](parsing.md#limits-and-untrusted-input) with lenient reading, and it composes with
a selection: select the few properties you need, and a hostile file costs little.

## Schemas

A schema extends `IcsSchema<T>` for one component name and declares the fields of your class: a required field, an optional
one, a list of repeated properties, or child components. There are no annotations, no compiler plugin and no generated code. The
schema doubles as the selection: the lexer reads only the properties the schema names. Schemas are not frozen for 1.0:
extending `IcsSchema` needs `@OptIn(ExperimentalICalApi::class)` (see [Compatibility](compatibility.md#api-stability)), while decoding and encoding with a schema need
nothing.

<!-- sample: schema-define -->
```kotlin
data class Lesson(
    val uid: String,
    val start: ICalDateTime,
    val summary: String,
    val room: String?,
    val teachers: List<String>,
)

object LessonSchema : IcsSchema<Lesson>("VEVENT") {
    private val uid = field(Properties.UID, Lesson::uid)
    private val start = field(Properties.DTSTART, Lesson::start)
    private val summary = field(Properties.SUMMARY, Lesson::summary)
    private val room = optionalField(Properties.LOCATION, { it.room?.let(::Location) }, Location::text)
    private val teachers = listField(
        Properties.ATTENDEE,
        Lesson::teachers,
        decode = { it.address },
        encode = { Attendee.builder(it).build() },
    )

    override fun create(values: Values) =
        Lesson(values[uid], values[start], values[summary], values[room], values[teachers])
}
```

Each field takes a typed key (`Properties.DTSTART` and so on) and a getter that the encoder uses; a field can convert the
decoded value into your own type (`decode`) and back (`encode`). Fields are declared before the schema is used.

Decoding:

<!-- sample: schema-decode -->
```kotlin
val lessons: List<Lesson> = ICal.parser().decode(bytes, LessonSchema) // no tree is built
val result = ICal.parser().decodeWithProblems(bytes, LessonSchema) // items, problems, skipped
val streamed: Sequence<Lesson> = ICal.parser().decode(bytes.asByteInput(), LessonSchema) // bounded memory
```

- A component whose required field is missing or broken is skipped; `decodeWithProblems` returns `skipped` and the problem
  (`ICAL-M001`).
- Optional fields are `null` when the property is missing or does not decode; value problems are reported as usual.
- The `ByteInput` overload returns a sequence that reads as you iterate, once, with memory bounded by the block size.
- In strict mode the first error throws `ICalParseException`.
- The `Values` object is reused between components: read what you need inside `create` and do not keep it.

Encoding goes the other way, through the component builders: UID and `DTSTAMP` come from the field values or from
`BuildDefaults`, exactly as when you build a calendar.

<!-- sample: schema-encode -->
```kotlin
val defaults = BuildDefaults(fixedClock) { newUid() } // for items that have no UID of their own
val feed: ByteArray = ICal.writer().encode(lessons, LessonSchema, { prodId = "-//Uni//Schedule//EN" }, defaults)
```

`fixedClock` is any `kotlin.time.Clock`. A required field that is `null` in an item, or a value that cannot be written, is an
`IllegalArgumentException`. A schema can nest: `childField(otherSchema, getter)` collects the child components, such as the
alarms of an event.

## Next

- Occurrences and recurrence for the events you decoded: [Recurrence and occurrences](recurrence-and-occurrences.md).
- The syntax of streams: [Parsing](parsing.md).

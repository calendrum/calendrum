# Java

The library is written in Kotlin and designed to be called from Java 8 and later, on the JVM and on Android. There are no
`data class`es, no `kotlin.Result` and no enums for open sets in the public API, every set of options has a builder, and the
Kotlin-only conveniences (DSLs, extension functions, default arguments) have a Java-friendly twin.

The fragments on this page are in a Java source file that the build compiles with the JDK 8 API and runs in the test suite.

## How Kotlin shows up in Java

| In Kotlin | In Java |
|---|---|
| `result.problems` | `result.getProblems()` |
| `ParseOptions.Untrusted`, `ParseOptions.Default` | `ParseOptions.getUntrusted()`, `ParseOptions.Default` |
| `Properties.DTSTART`, `Parameters.CN` | `Properties.getDTSTART()`, `Parameters.getCN()` |
| `PartStat.Accepted`, `Frequency.WEEKLY` | `PartStat.Accepted`, `Frequency.WEEKLY` (static fields) |
| `ICal.parse(bytes)` | `ICal.parse(bytes)` |
| `object Rfc5545` | `Rfc5545.INSTANCE` |
| `calendar.occurrences(from, to, options)` | `Occurrences.between(calendar, from, to, options)` |
| `TimeZoneResolvers.default(calendar)` | `TimeZoneResolvers.defaultFor(calendar)` (`default` is a Java keyword) |
| `result.calendars` (an extension) | `ICalModel.getCalendars(result)` |
| `ICal.parser { mode = ... }` (a DSL) | `ICal.parser(ParseOptions.builder().mode(...).build())` |
| `VCalendar(prodId) { event { ... } }` (a DSL) | `VCalendar.builder()...`, `VEvent.builder(defaults)...` |

Extension functions of Kotlin live in static utility classes whose names say what they are for: `ICalModel`, `ICalStreams`,
`Recurrence`, `Occurrences`, `Events`, `TimeZoneConversions` (`toInstant`), `ByteInputs` (`asByteInput`), `VCalendars` (the
DSL), `ICalDateTimes`, `ICalDurations` and `JavaClocks`. Kotlin's default arguments give you the shorter overloads; the DSLs
are Kotlin-only, and every builder they use is available as an ordinary builder.

The API uses `kotlinx-datetime` (`LocalDateTime`, `DayOfWeek`, `TimeZone`) and `kotlin.time.Instant` and `Clock`, so these
libraries come with it. `ICalDateTimes` converts between `kotlin.time.Instant` and `java.time.Instant`, `ICalDurations`
between `ICalDuration` and `java.time.Duration` (or `null` when the duration has days or weeks, which are not exact), and
`JavaClocks` turns a `java.time.Clock` into the clock the library takes. Because Java cannot create a `kotlin.time.Duration`,
the time budget of a parser is set with `ICalStreams.maxTime(builder, java.time.Duration)`.

### Bridges for Java

The JVM-only classes that turn Java types into the ones the library takes, and back:

| Class | What it bridges |
|---|---|
| `ICalStreams` | `InputStream`, `OutputStream`, `Reader` and `Writer` for parsing and writing, `decodeStream` to a `java.util.stream.Stream`, `maxTime(builder, java.time.Duration)` |
| `ICalDateTimes` | `java.time.Instant` ↔ `kotlin.time.Instant` and `ICalDateTime.Utc` |
| `ICalDurations` | `java.time.Duration` ↔ `ICalDuration` (`null` when the duration has days or weeks) |
| `JavaClocks` | `java.time.Clock` → the `kotlin.time.Clock` the library takes, and `buildDefaults(clock, uids)` |

The other static classes (`ICalModel`, `Occurrences`, `Recurrence`, `Events`, `TimeZoneConversions`, `ByteInputs`, `VCalendars`) are
the Kotlin extension functions under a Java name; they exist on every platform. A `Sequence` (occurrences, recurrence sets) is
read with `.iterator()`; there is no `Stream` twin for it.

## Parsing and strict mode

`ICalParseException`, thrown by strict parsing and by the stream functions, is a checked exception (Kotlin's `@Throws`), so
the compiler makes sure you handle it. Lenient parsing never throws on data.

<!-- sample: java-parse -->
```java
ParseResult result = ICal.parse(bytes);                              // lenient: problems, never an exception
ICalComponent calendar = result.getComponents().get(0);
String summary = calendar.components("VEVENT").get(0).property("SUMMARY").getValue();   // "Weekly sync"

ParseOptions options = ParseOptions.builder().mode(ParseMode.Strict).build();
try {
    ICal.parser(options).parse(broken);
} catch (ICalParseException e) {                                     // checked, so Java makes you handle it
    e.getProblems().forEach(problem -> codes.add(problem.getCode().getId()));
}
```

(`bytes` and `broken` are `byte[]`, `codes` a `List<String>`.)

## Streams, limits and interceptors

On the JVM, `ICalStreams` adapts `java.io`: `components(parser, inputStream)`, `write(writer, components, outputStream)`,
`Reader` and `Writer` variants, and `decodeStream` for schemas. The streams are never closed for you. Interceptors are lambdas
or factories.

<!-- sample: java-streams -->
```java
ComponentStream stream = ICalStreams.components(ICal.parser(), inputStream);   // throws IOException
while (stream.hasNext()) names.add(stream.next().getName());
ParseLimits limits = ICalStreams.maxTime(ParseLimits.builder(), Duration.ofSeconds(2)).build();
ICalStreams.write(ICal.writer(), ICal.parse(BYTES).getComponents(), outputStream);   // never closed by us
ParseOptions fixer = ParseOptions.builder()
    .limits(limits)
    .intercept(line -> { if (line.getName().equals("SUMMARY")) line.rename("DESCRIPTION"); })
    .build();
```

A stateful interceptor is registered as a factory with `.intercept(MyInterceptor::new)`, so that every parse gets a new instance.

## The typed model

<!-- sample: java-model -->
```java
VCalendar calendar = ICalModel.getCalendars(result).get(0);
VEvent event = calendar.getEvents().get(0);
ICalDateTime start = event.getStart();
Instant instant = null;
if (start instanceof ICalDateTime.Utc) {
    instant = ICalDateTimes.toJavaInstant((ICalDateTime.Utc) start);   // kotlin.time.Instant ↔ java.time.Instant
}
Duration length = ICalDurations.toJavaDuration(event.getDuration());   // null if it has days or weeks
String cn = ICalModel.get(event.getComponent().property("ATTENDEE"), Parameters.getCN());   // "Jane"
```

`ICalDateTime` is a sealed hierarchy of `Date`, `Floating`, `Utc` and `Zoned`; check with `instanceof`. A missing or broken
value is `null`; `ICalModel.decode(component, key)` returns the `Decoded` object that says why, as in
[The typed model](model.md#missing-broken-or-read-with-a-warning).

## Builders and edits

Every wrapper has a `builder(...)` and a `toBuilder()`: the second is the Java form of `edit { }` and returns a modified copy,
sharing every node that did not change. `BuildDefaults` takes a clock and a UID generator; `JavaClocks.buildDefaults` accepts a
`java.time.Clock`.

<!-- sample: java-build -->
```java
VEvent event = VEvent.builder(JavaClocks.buildDefaults(Clock.systemUTC(), () -> newUid()))
    .summary("Weekly sync")
    .rrule(Recur.builder(Frequency.WEEKLY).byDay(DayOfWeek.MONDAY).count(10).build())
    .attendee(Attendee.builder("mailto:alice@example.com").cn("Alice").partStat(PartStat.Accepted).build())
    .build();
VEvent moved = event.toBuilder().sequence(1).build();                 // edits return a copy
```

## Schemas

A schema is a subclass of `IcsSchema<T>`; fields are declared as fields of the subclass and read in `create`. The constructor
passes the component name.

<!-- sample: java-schema -->
```java
public static final class Meeting {
    private final String uid;
    private final String title;

    public Meeting(String uid, String title) {
        this.uid = uid;
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }
}

public static final class MeetingSchema extends IcsSchema<Meeting> {
    private final Field<String> uid = field(Properties.getUID(), Meeting::getUid);
    private final Field<String> title = field(Properties.getSUMMARY(), Meeting::getTitle);

    public MeetingSchema() {
        super("VEVENT");
    }

    @Override
    public Meeting create(Values values) {
        return new Meeting(values.get(uid), values.get(title));
    }
}
```

<!-- sample: java-schema-use -->
```java
List<Meeting> meetings = ICal.parser().decode(BYTES, new MeetingSchema());       // no tree is built
Stream<Meeting> streamed = ICalStreams.decodeStream(ICal.parser(), new ByteArrayInputStream(BYTES), new MeetingSchema());
```

## Time zones and occurrences

<!-- sample: java-occurrences -->
```java
TimeZoneResolver zones = TimeZoneResolvers.defaultFor(calendar);        // "default" is a Java keyword
OccurrenceOptions options = OccurrenceOptions.builder().zones(zones).build();
Iterator<Occurrence> week = Occurrences.between(calendar,
    ICalDateTimes.toKotlinInstant(Instant.parse("2026-10-01T00:00:00Z")),
    ICalDateTimes.toKotlinInstant(Instant.parse("2026-10-20T00:00:00Z")), options).iterator();
List<Instant> starts = new ArrayList<>();
while (week.hasNext()) starts.add(ICalDateTimes.toJavaInstant(week.next().getStart()));
```

`Occurrences.between` returns a Kotlin `Sequence`; call `.iterator()` on it. `Occurrences.cursor(calendar, options)` gives the cursor
with `next()`, `after(instant)`, `at(instant)` and `page(size, after)`. Recurrence rules are expanded with
`Recurrence.getRecurrenceSet(event)` and `Recurrence.expand(rule, start)`.

## Compiling against the library

- `kotlin.time.Duration` is a value class and cannot be created from Java. `ICalDuration` has Java factories (`ofSeconds`, `ofHours`
  and so on) and the properties `getWeeks()`, `getDays()`…; `toDurationOrNull()` returns a boxed `kotlin.time.Duration`, and
  `ICalDurations.toJavaDuration` a `java.time.Duration`. The time budget of a parser is set with `ICalStreams.maxTime`.
- The kotlinx-datetime classes carry annotations of kotlinx-serialization that are not on your classpath. `javac` with
  `-Xlint:all -Werror` stops at `Cannot find annotation method 'with()'`; compile with `-Xlint:-classfile`, or add
  `kotlinx-serialization-core` as `compileOnly`.
- Every method whose exception depends on data declares it (`@Throws`), so checked exceptions are real: `ICalParseException`,
  `IOException` for streams, `IllegalTimeZoneException` for a resolver with the `Fail` policy. Violations of a precondition (an
  invalid name in a builder) are `IllegalArgumentException`.

## API that is not frozen

A few parts of the API may still change in a minor release before they are frozen: interceptors, `ICalPatch`, schemas
(`IcsSchema`), your own codecs and registry extensions, the text form of fingerprints, `FeedPublisher`, your own `Fixup`,
`FreeBusyCalc.conflicts` and `IMip.untrustedContent`. Kotlin marks them `@ExperimentalICalApi` and asks for an opt-in; `javac` does not check the marker, so
from Java the promise is the same but nothing warns you. The list is in [Compatibility](compatibility.md#api-stability).

## Android

The JVM artifact is the Android artifact. kotlinx-datetime builds on `java.time`, so an app needs `minSdk` 26 or core library
desugaring. There is no reflection, hence no R8 or ProGuard rules to add. See [Compatibility](compatibility.md).

## Next

The Kotlin guides ([Parsing](parsing.md), [The typed model](model.md), [Building and editing](building-and-editing.md)) describe
the same objects; the Java names follow the table above.

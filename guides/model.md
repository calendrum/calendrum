# The typed model

The tree from [Parsing](parsing.md) holds text. The typed model (`io.github.calendrum.model` and `io.github.calendrum.value`) reads
that text as RFC 5545 defines it: wrappers for every component, typed keys for every property and parameter, and values that
are decoded on first access and cached in the tree. The wrappers are thin views; they add no copy of the data and the tree
stays the single source of truth.

## Wrappers

`ParseResult.calendars` wraps the `VCALENDAR` components of a result in `VCalendar`. A calendar has `events`, `todos`,
`journals`, `freeBusy` and `timeZones`; an event has `alarms`, `attendees`, `organizer`, `rrule`, `exdates` and the other
properties of RFC 5545 §3.6.1, plus the properties and sub-components of RFC 7986, 9073, 9074 and 9253 (`conferences`,
`locations`, `participants`…). `VTodo`, `VJournal`, `VFreeBusy`, `VTimeZone` and `VAlarm` work the same way.

Every wrapper exposes the tree node it wraps as `component`, so you can always drop down to the syntax layer and write
back exactly what you read.

<!-- sample: model-read -->
```kotlin
val calendar = ICal.parse(text).calendars.single()
for (event in calendar.events) {
    when (val start = event.start ?: continue) { // ICalDateTime: four forms, closed by RFC 5545
        is ICalDateTime.Date -> printed += "all day ${start.date}"

        is ICalDateTime.Utc -> printed += "at ${start.instant}"

        // an instant needs a resolver
        is ICalDateTime.Zoned -> printed += "at ${start.dateTime} ${start.tzid}"

        is ICalDateTime.Floating -> printed += "at ${start.dateTime} local time"
    }
    event.attendees.filter { it.partStat == PartStat.NeedsAction }.forEach { printed += it.address }
    for (alarm in event.alarms) printed += "${alarm.action} ${alarm.trigger}"
}
```

(`printed` is a mutable list of strings in this fragment.)

## Values

| Class | RFC 5545 type | Notes |
|---|---|---|
| `ICalDateTime` | DATE, DATE-TIME | `Date`, `Floating`, `Utc` and `Zoned`: the four forms the RFC allows, nothing else |
| `ICalDuration` | DURATION | keeps the units that were written: `P1W` is not `P7D` and `PT60M` is not `PT1H` |
| `ICalPeriod` | PERIOD | start and end, or start and duration |
| `Recur` | RECUR | the parts of an `RRULE`, the `SKIP` of RFC 7529, unknown parts in order |
| `Attendee`, `Organizer`, `Trigger`, `Geo`… | CAL-ADDRESS, TRIGGER, FLOAT lists | classes of their own, with the parameters that belong to them |

`ICalDateTime.Zoned` carries the date and time and the `TZID`; turning it into an instant needs a time zone resolver, because
the library never reads the system time zone ([Time zones](time-zones.md)). Floating times and dates stay what they are.

## Missing, broken, or read with a warning

An accessor returns `null` when the property is missing *or* its value cannot be read. When you need to tell which, ask for
the `Decoded` result: `Value` (possibly with warnings), `Malformed` (with the error and its position) or `Missing`.

<!-- sample: model-decode -->
```kotlin
val start: ICalDateTime? = event.start // null: missing or broken
val why = when (val decoded = event.component.decode(Properties.DTSTART)) {
    is Decoded.Value -> "read, ${decoded.problems.size} warnings"

    // lenient reading reports ICAL-V…
    is Decoded.Malformed -> decoded.problem.code.id

    // "ICAL-V002", with its position
    Decoded.Missing -> "no DTSTART"
}
```

In lenient mode a value that the RFC forbids but that is easy to read (a `DATE-TIME` without a time, the offset `-0000`,
leap second `60`) is accepted with a warning; in strict mode the same input is an error. The `ICAL-V` codes are listed in
[Diagnostics](diagnostics.md).

## Keys and parameters

Every standard property is a `PropertyKey<T>` in `Properties`, every standard parameter a `ParameterKey<T>` in `Parameters`.
`component[key]` decodes and caches; `property[key]` reads a parameter. A key ties a name to a codec, so your own properties
need no registration: write a `PropertyCodec<T>`, make a key, and read.

<!-- sample: model-keys -->
```kotlin
// a typed parameter key
val cn: String? = attendeeEvent.component.properties("ATTENDEE").first()[Parameters.CN]
val color = PropertyKey("X-ACME-COLOR", HexColor) // your own key needs no registration; HexColor is yours
val rgb: Int? = event.component[color] // 0xFF8800
```

A codec implements `decode(property, context)` and `encode(value, context)`; to report a bad value return
`Decoded.Malformed(context.problem(property, ProblemCode.InvalidValue, offset))`. Your own codecs and the registry below are
the extension points that are not frozen for 1.0: implementing `PropertyCodec` or `ParameterCodec`, `ICalRegistry.builder()` and
`ICalExtension.contribute` need `@OptIn(ExperimentalICalApi::class)` (see [Compatibility](compatibility.md#api-stability)). The ready-made keys and codecs need nothing.

Types of the values: structured values decode to classes of their own, such as `Conference`
(`CONFERENCE`), `Image` (`IMAGE`), `StructuredData` (`STRUCTURED-DATA`) and `RequestStatus` (`REQUEST-STATUS`), read through
the wrappers (`event.conferences`, `event.images`, `event.structuredData`, `event.requestStatuses`).

## Open enumerations

Parameter values such as `PARTSTAT`, `ROLE` or `CUTYPE`, and property values such as `STATUS` or `CLASS`, are open
enumerations: the standard tokens are constants, and any other token is kept as it is, never rejected, because the RFC lets
producers define their own (`X-` tokens and IANA registrations). Compare with `==` and read the token with `value`.

<!-- sample: model-open-enum -->
```kotlin
// PartStat.Accepted: tokens are matched without regard to case
val known = PartStat.of("accepted")
val vendor = PartStat.of("X-MAYBE-LATER") // unknown tokens are kept, not rejected
val written = vendor.value // "X-MAYBE-LATER"
```

The same holds for every open enumeration: `Role`, `CuType`, `RelType`, `FbType`, `EventStatus`, `Classification` and the rest are
classes with constants for the standard tokens and `of(value)` for any other.

## Reporting value problems up front

By default values are decoded when you read them, so a broken `DTSTART` shows up only when you ask for it. Parse with
`decodeEagerly = true` to decode every property of the registry while parsing and get all value problems in
`ParseResult.problems`; strict mode does the same and throws on the first error.

<!-- sample: model-eager -->
```kotlin
val result = ICal.parser { decodeEagerly = true }.parse(text) // Strict mode does the same and throws
val valueProblems = result.problems.filter { it.code.id.startsWith("ICAL-V") } // [ICAL-V008]
```

## Extensions and the registry

RFC 7986 (`NAME`, `COLOR`, `CONFERENCE`, `IMAGE`…), RFC 9073 (`VLOCATION`, `PARTICIPANT`, `VRESOURCE`), RFC 9074 (alarm
`UID`, `ACKNOWLEDGED`, `PROXIMITY`) and RFC 9253 (`LINK`, `CONCEPT`, `REFID`) are read like RFC 5545:

<!-- sample: model-extensions -->
```kotlin
val calendar = ICal.parse(extended).calendars.single()
// NAME, COLOR, REFRESH-INTERVAL
printed += "${calendar.names} ${calendar.color} ${calendar.refreshInterval}"
for (event in calendar.events) {
    event.conferences.forEach { printed += "${it.label}: ${it.uri} ${it.features}" } // CONFERENCE (RFC 7986)
    event.locations.forEach { printed += "${it.name}" } // VLOCATION (RFC 9073)
    event.alarms.filter { it.acknowledged != null }.forEach { printed += "dismissed ${it.uid}" } // RFC 9074
}
```

An `ICalRegistry` is the list of what every property is: its value types, parameters and where it may appear. Eager decoding,
strict mode, `decodeAny` and validation consult it. `ICalRegistry.Default` knows the five RFCs. Build your own to add your
`X-` properties, or to install `VendorProperties`, an extension with the frequent X-properties of Microsoft, Apple, Google, Mozilla, libical and Yandex
that is deliberately not part of the default registry. Vendors change their behaviour and there is no standard to follow.

<!-- sample: model-registry -->
```kotlin
val registry = ICalRegistry.builder()
    .install(Rfc5545).install(Rfc7986).install(VendorProperties) // ICalRegistry.Default has all five RFCs
    .install {
        it.property(PropertyDefinition(color, ValueType.Text, emptySet(), emptySet(), emptyMap(), "Acme"))
    }
    .build()
val checked = ICal.parser {
    this.registry = registry
    decodeEagerly = true
}.parse(extended)
val busy = checked.components.single().components.first().decodeAny("X-MICROSOFT-CDO-BUSYSTATUS", registry)
```

(`color` is the key from the previous section and `extended` a calendar text with an `X-ACME-COLOR` and an
`X-MICROSOFT-CDO-BUSYSTATUS` property.)

Types: `registry.property(name)`, `parameter(name)` and `component(name)` return a `PropertyDefinition` (its `defaultType` and
`allowedTypes` as `ValueType`s, its `parameters` and a `Cardinality` per component), a `ParameterDefinition` or a
`ComponentDefinition` (its allowed `children`), or `null` for a name the registry does not know.

## Next

[Building and editing](building-and-editing.md) shows how to create and change what you have read, and [Selective parsing
and schemas](selective-parsing-and-schema.md) how to read straight into your own classes.

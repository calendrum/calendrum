# Swift

`calendrum` reaches Swift through the Kotlin/Native export to Objective-C, as every Kotlin Multiplatform library does. Two
kinds of apps use it:

- **a Kotlin Multiplatform app with a SwiftUI front end** depends on `calendrum-core` (and optionally `calendrum-tzdata`) in its shared
  module and builds one framework; the types of Calendrum are then visible in Swift when the shared API exposes them;
- **a Swift app** wraps the shared module in a small Kotlin façade (as below) and consumes it as an XCFramework.

The example in [`examples/ios-swiftui`](../examples/ios-swiftui) is the second kind, and the fragments on this page are taken from
it. Kotlin code is compiled by the build of the example, Swift code by Xcode: run `examples/run.sh ios-swiftui` on a Mac with an
iPhone simulator to build and test it. (These two fragments are not part of the Gradle `check` of the library, which has no Apple
simulator test for Swift code; the Swift-facing rules of the API are tested there by Kotlin tests, see the end of this page.)

## The shared module

A Gradle module with the Apple targets builds a static framework and an XCFramework:

<!-- sample: swift-gradle -->
```kts
kotlin {
    // A static framework for iosArm64 and iosSimulatorArm64 in one XCFramework; the binaryTarget of Package.swift
    // points at its release build.
    val xcframework = XCFramework("CalendrumSchedule")
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "CalendrumSchedule"
            isStatic = true
            xcframework.add(this)
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(calendrumVersion.map { "io.github.calendrum:calendrum-core:$it" })
            implementation(calendrumVersion.map { "io.github.calendrum:calendrum-tzdata:$it" })
        }
    }
}
```

`calendrumVersion` is the library version. The Swift package points a `binaryTarget` at the XCFramework.

## A façade for the Swift side

Keep the Swift-facing surface small and simple: classes, strings, lists. The façade below parses a feed and returns the
occurrences of a window as plain objects. `@Throws` turns an exception of Kotlin into a thrown error in Swift; without it an
exception crashes the process.

<!-- sample: swift-kotlin -->
```kotlin
class ScheduleEntry(val title: String, val day: String, val time: String, val location: String)

class ScheduleFeed(private val zoneId: String) {
    // `@Throws` makes Swift see a bad window or zone as a thrown error; the window is parsed first so that an empty
    // feed with a bad window still throws.
    @Throws(IllegalArgumentException::class)
    fun entries(feed: String, from: String, to: String): List<ScheduleEntry> {
        val zone = TimeZone.of(zoneId)
        val window = Instant.parse(from) to Instant.parse(to)
        val calendar = ICal.parse(feed).calendars.firstOrNull() ?: return emptyList()
        val zones = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TzData.resolver,
            unknown = UnknownZonePolicy.Utc,
        )
        val options = OccurrenceOptions.builder().zones(zones).build()
        return calendar.occurrences(window.first, window.second, options).map { occurrence ->
            val start = occurrence.start.toLocalDateTime(zone)
            val end = occurrence.end.toLocalDateTime(zone)
            ScheduleEntry(
                title = occurrence.component.property("SUMMARY")?.value.orEmpty(),
                day = start.date.toString(),
                time = "${start.time}–${end.time}",
                location = occurrence.component.property("LOCATION")?.value.orEmpty(),
            )
        }.toList()
    }
}
```

## The Swift side

<!-- sample: swift-model -->
```swift
public enum ScheduleModel {
    public static func rows(feed: String, from: String, to: String, zone: String) throws -> [ScheduleRow] {
        let entries = try ScheduleFeed(zoneId: zone).entries(feed: feed, from: from, to: to)
        return entries.enumerated().map { index, entry in
            ScheduleRow(id: index, title: entry.title, day: entry.day, time: entry.time, location: entry.location)
        }
    }
}
```

A Kotlin `List` is an `NSArray`-bridged `[ScheduleEntry]`, a property is a Swift property, and the `@Throws` function is a
`throws` function that you call with `try`. A bad instant makes it throw:

<!-- sample: swift-throws -->
```swift
func testBadInstantThrows() {
    XCTAssertThrowsError(try ScheduleModel.rows(feed: "", from: "x", to: "y", zone: "Europe/Berlin"))
}
```

## Using the types of Calendrum directly

When your shared module exports the library (`export(...)` in the framework configuration), Swift sees the types of the
library with these rules:

- **Names.** `ICal` is `ICal.shared`; Kotlin `object`s are accessed through `shared` and companions through `companion`:
  `PartStat.companion.Accepted`. The Kotlin names are kept (`swift_name` attributes in the generated header), so
  `parse(text:)`, `parse(bytes:)` and `fingerprints(bytes:)` read like the Kotlin API. Kotlin's `ByteArray` is `KotlinByteArray`.
- **Renamed for Swift.** `NSObject` already has `description`, so the `description` of `VEvent`, `VTodo`, `VAlarm`, `VLocation`,
  `VResource`, `Participant`, `RequestStatus` and of their builders is `descriptionText` in Swift (`event.descriptionText`).
  Overloads that Swift could not tell apart have their own labels or names, never a trailing `_`: `ITip.reviseTodo`, `addTodo`
  and `counterTodo` for to-dos, `location(text:)` for the `LOCATION` text of a builder (`location(value:)` adds a `VLOCATION`),
  `byDay(daysOfWeek:)` beside `byDay(days:)` of a recurrence builder, `write(component:appendable:)` beside
  `write(component:out:)` of `ICalWriter`, `encode(items:schema:buildCalendar:)` for the overload that builds the calendar, and
  `encodeProperty(value:context:)` and `encodeParameter(value:context:)` of a codec. The Kotlin names are unchanged. The library
  API that is internal to its modules is not exported to Objective-C at all.
- **Exceptions.** Everything that can throw because of the data is annotated with `@Throws`: strict parsing (`ICalParser`,
  `ICalReader`), `ComponentStream.hasNextOrThrow()` and `nextOrThrow()` (the methods of `Iterator` cannot declare exceptions, so
  Swift gets the twins), `ICalWriter`, `Fingerprints.decodeFromString`, `TimeZoneResolver.resolve`, `toInstant`, the occurrence
  cursor and the stream adapters. Violations of a precondition, such as an invalid name given to a builder, are not annotated:
  check the input first or choose a policy that does not throw (`UnknownZonePolicy.Floating` or `Utc`).
- **Defaults and builders.** Objective-C has no default arguments, so the API offers overloads and builders (`ParseOptions.builder()`).
- **Enumerations.** Kotlin `enum`s are classes with singletons (`ParseMode.strict`). The open enumerations of the RFC (`PartStat`,
  `ProblemCode`, `ValueType`) are classes with constants and `of(value:)`; they stay open on purpose.
- **Sealed types.** `ICalDateTime` and its four forms are classes; test the form with `as?` (`ICalDateTimeUtc`, `ICalDateTimeZoned`,
  `ICalDateTimeFloating`, `ICalDateTimeDate`). Swift export of Kotlin, which would turn them into enums, is still experimental;
  SKIE is not used by the library, and an app that adds it gets the enum forms for the library types it exports.

A snapshot test of the generated Objective-C header (`checkObjcHeader`, part of `check` on a Mac) catches accidental changes of
all of this.

## Next

[Compatibility](compatibility.md) lists the Apple targets and the Kotlin versions the framework needs.

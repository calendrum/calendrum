# Examples

Each example is a separate Gradle build that uses Calendrum from the local Maven repository, as an application would.
`examples/run.sh` publishes Calendrum to `mavenLocal` and builds and tests all of them; `--skip-publish` reuses what is there,
and names select examples (`examples/run.sh --skip-publish cli`).

| Example | What it shows | Needs |
|---|---|---|
| [`cli`](cli) | a command-line tool: `summary`, `occurrences`, `validate` and `anonymize` an `.ics` file | JDK 21 |
| [`ktor-feed-server`](ktor-feed-server) | a Ktor server with an ICS feed (`icsFeed`, `ETag`, `Cache-Control`) and `VCalendar` bodies | JDK 21 |
| [`android-compose`](android-compose) | an Android app that lists the occurrences of a feed with Jetpack Compose | Android SDK |
| [`ios-swiftui`](ios-swiftui) | an XCFramework built from Kotlin and a SwiftPM package with a SwiftUI view of the same feed | macOS, Xcode |

All examples read [`shared/sample.ics`](shared/sample.ics): a weekly seminar in `Europe/Berlin` with one moved instance.

Run the CLI by hand:

```bash
./gradlew -p examples/cli run --args="occurrences $PWD/examples/shared/sample.ics 2026-09-01T00:00:00Z 2026-10-01T00:00:00Z"
```

Start the feed server on port 8080 and open `http://localhost:8080/team/core.ics`:

```bash
./gradlew -p examples/ktor-feed-server run
```

The iOS example needs the XCFramework before Xcode opens the package:

```bash
./gradlew -p examples/ios-swiftui/shared assembleCalendrumScheduleReleaseXCFramework
```

Then add the package in `examples/ios-swiftui` to an app and show `ScheduleView(rows: try ScheduleModel.rows(...))`.

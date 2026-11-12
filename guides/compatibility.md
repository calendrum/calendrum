# Compatibility

What `calendrum` runs on, with which versions of Kotlin and Java, and what each module needs. The Kotlin and Java requirements are
checked by building small consumer projects against the freshly built artifacts (`compat-tests/run.sh`); this page lists what
those projects cover.

## Kotlin versions of the consumer

The artifacts are built with the *minimum* Kotlin version they promise (Kotlin 2.3 language and API level), the way large
libraries do: a klib built with a newer compiler would not be readable by an older one, while your compiler generates the machine
code for Native, JS and Wasm itself.

| Your platform | Minimum Kotlin | Notes |
|---|---|---|
| JVM and Android | **2.2** | see "Kotlin 2.2 and `kotlin.time.Instant`" below |
| Kotlin/Native (Apple, Linux, Windows, Android Native) | **2.3.0** | |
| Kotlin/JS and Kotlin/Wasm | **2.3.20** | the stdlib that comes with the library is the version of the build compiler, and the JS and Wasm compilers refuse an older compiler than their stdlib; 2.3.0 and 2.3.10 fail |

Cases that the consumer check builds and tests:

| Case | Kotlin | Gradle | Targets |
|---|---|---|---|
| `jvm-2.2` | 2.2.0 | 8.14.5 | JVM, with the BOM |
| `jvm-2.3` | 2.3.0 | 9.0.0 | JVM, with the BOM |
| `kmp-2.3` | 2.3.0 | 9.0.0 | JVM, `linuxX64` |
| `web-2.3.20` | 2.3.20 | 9.3.1 | JS, Wasm |
| `kmp-2.4` | 2.4.20 | 9.3.1 | JVM, JS, Wasm, `linuxX64` |
| `kmp-2.5-beta` | 2.5.0-Beta1 | 9.3.1 | JVM, JS, Wasm, `linuxX64` |

The window moves with the Kotlin releases: the minimum rises only in a minor release of the library, announced in advance and
recorded in the changelog.

### Kotlin 2.2 and `kotlin.time.Instant`

`ICalDateTime.Utc.instant` is a `kotlin.time.Instant`, which the standard library of Kotlin 2.3 made stable. A JVM consumer on
Kotlin 2.2 uses it with `@OptIn(kotlin.time.ExperimentalTime::class)`; everything else in the API needs no opt-in. The
public API has no other experimental declaration of the standard library and no experimental compiler feature.

## Targets

| Tier | Promise | Targets |
|---|---|---|
| 1 | all modules; tested; bugs come first | `jvm` (server, desktop and Android), `iosArm64`, `iosSimulatorArm64`, `macosArm64`, `linuxX64` |
| 2 | the core; tested | `js` (Node and browser), `wasmJs`, `linuxArm64`, `mingwX64`, `iosX64` |
| 3 | compiled only; as is | `wasmWasi`, `watchosArm64`, `watchosDeviceArm64`, `watchosSimulatorArm64`, `tvosArm64`, `tvosSimulatorArm64`, `tvosX64`, `macosX64`, `androidNativeArm32/Arm64/X86/X64` |

`macosX64` and `tvosX64` are deprecated by Kotlin and are kept as long as Kotlin builds them and the kotlinx libraries publish
them; they will be removed in a minor release with a note in the changelog. `watchosArm32`, `watchosX64` and `linuxArm32Hfp` are
not offered.

Tests run on the host where they can: JVM, JS, Wasm, `linuxX64`, `macosArm64`, `iosSimulatorArm64` and the others that the
host can execute. On a Mac without a tvOS or watchOS simulator runtime the tvOS and watchOS tests cannot run, only compile.

### Modules and targets

| Module | Targets |
|---|---|
| `calendrum-core`, `calendrum-tzdata`, `calendrum-jcal`, `calendrum-kotlinx-io` | all targets above |
| `calendrum-okio` | all except Android Native (Okio does not publish it) |
| `calendrum-ktor` | all except `wasmWasi` (Ktor does not publish it) |
| `calendrum-ical4j-interop` | JVM only |
| `calendrum-bom` | not a library: aligns the versions of the others |

## Java and Android

| | |
|---|---|
| Bytecode and JDK API | **Java 8** (`jvmTarget` 1.8, compiled against the Java 8 API), so any JVM from 8 on can load the library |
| `calendrum-ical4j-interop` | Java 11 (ical4j 4 requires it) |
| Android | the JVM artifact; `minSdk` 26, or core library desugaring below that, because kotlinx-datetime builds on `java.time` |
| Shrinking | no reflection, no `ServiceLoader` and no classpath resources: no R8 or ProGuard rules are needed |
| Building from source | JDK 17 or newer |

From Java, see [Java](java.md) for naming and the one compiler flag you may need.

## Dependencies

| Module | Depends on | Minimum version |
|---|---|---|
| `calendrum-core` | Kotlin standard library, `kotlinx-datetime` | datetime 0.7.0 (the first with `kotlin.time.Instant`) |
| `calendrum-tzdata` | `calendrum-core` | |
| `calendrum-jcal` | `calendrum-core`, `kotlinx-serialization-json` | 1.6.2 |
| `calendrum-kotlinx-io` | `calendrum-core`, `kotlinx-io-core` | 0.3.1 |
| `calendrum-okio` | `calendrum-core`, Okio | 3.7.0 |
| `calendrum-ktor` | `calendrum-core`, Ktor | 3.0.0 |
| `calendrum-ical4j-interop` | `calendrum-core`, ical4j | 4.0.0 |

Each dependency is the lowest version that supports every target of the module, so an application that has a newer one gets the
newer one. The core has no logging library: diagnostics go through callbacks. kotlinx-datetime brings `kotlinx-serialization-core`
on non-JVM targets and `@js-joda/core` on JS and Wasm.

## Platform notes

- **Time zones.** The core never uses the system time zone. JS and WASI have no system time zone database, and kotlinx-datetime
  needs separate data there; add `calendrum-tzdata` to get the same answers on every platform ([Time zones](time-zones.md)).
- **Swift.** The API is exported to Objective-C with `@Throws` on everything that can fail on data
  ([Swift](swift.md)); the generated header is kept as a snapshot and checked.
- **JS.** Usable from Kotlin/JS today; a TypeScript-facing façade exists as a spike and is not a supported artifact.

## API stability

- Explicit API mode in every published module; the ABI of the JVM and of the klibs is dumped into the repository and checked, so
  a change of the public API cannot pass unnoticed.
- No `data class` in the public API (adding a field would break `copy` and `componentN`), no `kotlin.Result`, and open
  enumerations for the sets that RFCs let producers extend. `sealed` only for sets that the standard closes (the four forms of
  `ICalDateTime`).
- New options go into option objects and builders, not into new default parameters, which would break binary compatibility.
- Semantic versioning. Before 1.0 a minor release may break the API; each break is described in the changelog with how to
  migrate. A deprecation goes through warning, error and hidden, one minor release each, with a `ReplaceWith`.
- `@ExperimentalICalApi` marks the public API that is not frozen for 1.0 and may change in a minor release, with a changelog
  entry but without a deprecation cycle; using it needs `@OptIn(ExperimentalICalApi::class)`. It covers the extension points
  of the registry (`ICalRegistry.builder()`, `ICalExtension`, your own `PropertyCodec` and `ParameterCodec`), schemas (extending
  `IcsSchema`), `ICalPatch`, line interceptors, the text form of `Fingerprints` (`encodeToString`, `decodeFromString`),
  `FeedPublisher`, your own `Fixup`, `FreeBusyCalc.conflicts` and `IMip.untrustedContent`. Everything else in the public API is
  frozen for 1.0.
- `@InternalICalApi` marks what the modules share between them; it is not a contract.

## Support policy

How long a version of the library is maintained once 1.0 is out. Releases come in a train, a minor version about every 6 to 8
weeks ([how a release is made](../RELEASING.md)).

| Version | What it gets |
|---|---|
| the latest minor version (`1.4.x`) | bug fixes and security fixes, as patch releases |
| the previous minor version (`1.3.x`) | security fixes, until 6 months after the day the next minor version came out |
| older minor versions | nothing: upgrade |

- **A new major version does not extend this.** When 2.0 comes out, the 1.x line gets security fixes only, for 6 months.
- **Deprecation** is the one in "API stability" above: warning, error and hidden, at least one minor release for each step.
  Public API that is marked `@ExperimentalICalApi` is outside it.
- **The minimum Kotlin of your project** (the tables at the top of this page) rises only in a minor version, never in a patch
  version, and the change is announced in advance and has a line in [CHANGELOG](../CHANGELOG.md).
- **Time zone data.** `calendrum-tzdata` gets a patch release within a week of a new release of the IANA time zone database.
- Before 1.0, and while the repository is private, this describes the plan and is not a commitment to outside users. Where to
  report a security problem will be published with the first public release.

## What is checked

The checks run when you start them by hand (`./gradlew check`, `compat-tests/run.sh`, `size-budget/run.sh`):

- tests of the common code on the JVM, JS, Wasm and Native hosts, the conformance suite and the benchmark corpora;
- the ABI dumps and the Objective-C header snapshot;
- the consumer projects of the table above;
- a size budget for what an application pays for the parts of the library it uses (JVM with R8, Android, JS, Wasm, iOS);
- the JVM tests again under other default time zones, and Java tests of the API.

## Next

[Getting started](getting-started.md) and [Integrations](integrations.md).

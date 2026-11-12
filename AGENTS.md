# AGENTS.md — working on Calendrum

Instructions for any coding agent (Codex, Claude Code and others) that changes this repository, and a short version of
[CONTRIBUTING.md](CONTRIBUTING.md) for people. Read it whole before you start. If a file named `AGENTS.local.md` lies
next to this one, read it too: it holds local instructions of the maintainers and is never committed.

## The project

- `calendrum` is a Kotlin Multiplatform library for iCalendar (RFC 5545): parsing, writing, the typed model,
  recurrence, time zones, validation, iTIP and jCal. One codebase for JVM/Android, Apple, Linux, Windows, JS and Wasm.
  No reflection, no global mutable state; the core depends only on the Kotlin standard library and kotlinx-datetime.
- Published modules: `calendrum-core`, `calendrum-tzdata`, `calendrum-jcal`, `calendrum-kotlinx-io`, `calendrum-okio`,
  `calendrum-ktor`, `calendrum-ical4j-interop`, `calendrum-bom`. Not published: `benchmarks`, `conformance`, `fuzz`,
  `tools`, `compat-tests`, `size-budget`, `examples`. `tooling/` is a separate Gradle build with the command line, the
  MCP server and the SDKs for C, Python, TypeScript and .NET.
- Nothing is published yet and every API may still change. License: Apache-2.0.

## Environment

- JDK 21 as the default `java`. Gradle and Kotlin come through `./gradlew`; the Kotlin/Native toolchain is downloaded on
  first use.
- Apple targets need macOS with Xcode. Without the tvOS and watchOS simulator runtimes, leave their tests out with
  `-x tvosSimulatorArm64Test -x watchosSimulatorArm64Test`.
- `:sdk-net` of `tooling/` needs the .NET SDK 10 (`-Pdotnet.root`, `DOTNET_ROOT`, `~/.dotnet` or `PATH`).
- The build needs the network: Maven Central, the Gradle Plugin Portal, `download.jetbrains.com` (Kotlin/Native),
  `nodejs.org` and `registry.yarnpkg.com` (JS and Wasm tests).

## Commands

The property `calendrum.targets` picks the targets: `all` (the default), `tier1`, `web`, `apple`, `windows`, `jvm`.

```bash
./gradlew check -Pcalendrum.targets=jvm      # the fast loop: lint, ABI (JVM), JVM tests, private references
./gradlew check                              # every target the host can build; ABI of the JVM and of the klibs
./gradlew :calendrum-core:allTests           # the tests of every target the host can run
./gradlew lint                               # ktlint through Spotless
./gradlew format                             # fix the style
./gradlew checkKotlinAbi                     # check the ABI dumps
./gradlew updateKotlinAbi                    # update them after an INTENDED change of the public API
./gradlew publishToMavenLocal                # every target into ~/.m2
./gradlew kotlinUpgradeYarnLock kotlinWasmUpgradeYarnLock   # when the build stops at the yarn.lock check
compat-tests/run.sh                          # consumer compatibility: publishes to mavenLocal and builds the matrix
size-budget/run.sh                           # size budget: publishes to mavenLocal, compares with baseline.json
./gradlew :benchmarks:quickBenchmark         # benchmarks, quick profile, every platform of the host
./gradlew :benchmarks:jvmQuickBenchmark -Pbench.include='\.sanity\.'   # one platform, one suite
./gradlew -p tooling check                   # the command line, the MCP server and the SDKs
```

- Add `--console=plain` and keep the output when you report a result.
- The klib dump is checked and updated only with `calendrum.targets=all`.
- `check` runs neither `compat-tests` nor `size-budget`. Run `compat-tests/run.sh` when a change touches the published
  artifacts, their dependencies or the compiler flags; the oldest consumer Kotlin is 2.2 for JVM and Android, 2.3.0 for
  Native and 2.3.20 for JS and Wasm. Run `size-budget/run.sh` when a change touches the code of `calendrum-core` or its
  dependencies; `--update-baseline` only with the reason for the growth.
- After a change of `.editorconfig`, run `./gradlew --stop`: the daemon keeps the old ktlint settings.
- The build cache and the configuration cache are on. A change of the build is checked twice in a row: the second run
  must reuse the configuration cache.

## Hard rules

**Versions.** Kotlin 2.3.21 and Gradle 9.3.1; do not raise them. `languageVersion` and `apiVersion` 2.3, `explicitApi()`
strict, warnings are errors, JVM bytecode and API Java 8. No experimental stdlib API and no `-X` feature in the public
API; the one exception is `@ObjCName` and `@HiddenFromObjC`, opted in inside the library: they change only the
Objective-C header. Adding or removing a target is decided by the maintainers first.

**Dependencies.** The core depends on the standard library and kotlinx-datetime and on nothing else. A new dependency
needs the maintainers' approval, a check that its metadata and stdlib are not newer than Kotlin 2.3, and its cost in
size; take the oldest version that does the job. SHA-256 sums of every dependency are in
`gradle/verification-metadata.xml` and `size-budget/gradle/verification-metadata.xml`: update them with the commands
of CONTRIBUTING ("Dependencies") and read the new entries. Never turn the verification off or add `<trust>`, except for
`*-sources.jar` and the group `org.apache.groovy`. Versions live only in `gradle/libs.versions.toml`. Test and benchmark
dependencies (ical4j, biweekly, Jazzer) never reach a published artifact.

**Architecture.**
- No reflection: no `ServiceLoader`, `Class.forName`, `kotlin-reflect` or classpath resources.
- No global mutable state; defaults are immutable `val`s.
- Never the system time zone implicitly.
- No regular expressions on hot paths. No DI frameworks.
- Public API: no `data class`, no `kotlin.Result`, no `enum` for values that RFCs let grow (open enumerations instead),
  `sealed` only for sets closed by the standard. New options go into option objects, not into default parameters.
- Code goes into `commonMain`; platform code only at the edges.

**KDoc and comments.** Every public declaration of a published module has an English KDoc: the first sentence says what
it does, then the RFC sections, `@throws` where it throws and `@sample` from compilable sample functions. An
`@InternalICalApi` declaration has one line: "Internal API, not a contract". A comment inside code only where *why* is
not obvious — an algorithm, an RFC, a bug of another implementation, a limit of a platform — and in its own words:
`checkPrivateReferences` fails on references to documents that are not in the repository. In build files and YAML,
comments only for steps that are not obvious.

**Style.** ktlint `intellij_idea` (the official Kotlin conventions), lines of at most 120 characters, trailing commas in
declarations, no `*` imports, 4 spaces (YAML 2), LF, UTF-8, a final newline. Before you finish: `./gradlew format`, then
`lint`. Everything in the repository is in English.

**Gradle.** Shared logic goes into the convention plugins of `build-logic/src/main/kotlin/` (`calendrum.kmp-library`,
`calendrum.lint`, `calendrum.publishing`, `calendrum.benchmarks`), not into copies per module. A new published module:
`include` in `settings.gradle.kts`, `plugins { id("calendrum.kmp-library") }`, its ABI dump (`updateKotlinAbi`), a line
in `calendrum-bom` and a `kover(project(":<module>"))` line in the root `build.gradle.kts`. Everything works with the
configuration cache: providers, not values read at configuration time.

**CI** (`.github/workflows/`). Every workflow starts only by hand (`workflow_dispatch`, plus `workflow_call`); the
commented triggers stay commented until the maintainers turn them on. A new workflow copies the job of `ci.yml`:
checkout with `persist-credentials: false`, `./.github/actions/setup-build`, Gradle tasks by name with `--continue`, the
reports uploaded on failure for 7 days. Actions are pinned by full SHA with the version in a comment;
`permissions: contents: read` by default; `timeout-minutes` on every job; `concurrency` with a literal prefix
(`<name>-${{ github.ref }}`). Run actionlint if you have it.

**Real calendars.** A real calendar, even anonymized, and anything cut from it stay with whoever exported them:
`corpus/real/` and every file or directory named `real-*` or `seed-real-*` are kept out by `.gitignore`. A fuzz input
grown from such a file, or a golden file derived from it, is named `real-*` too. See
[corpus/README.md](corpus/README.md).

## Definition of done

- `./gradlew check` passes: at least with `-Pcalendrum.targets=jvm`, with every target when the change touches the
  build, the targets or other platforms.
- New behaviour has tests in `commonTest`, so that they run on every platform.
- An intended change of the public API comes with updated ABI dumps (and the Objective-C header, `updateObjcHeader`).
- Every new or changed public declaration has KDoc.
- `CHANGELOG.md` has a line under `Unreleased`, in the style of the others.
- A change that users see is reflected in `guides/` and the README.
- A hot path that changed has a benchmark before and after; a slowdown of more than 10 % is explained.
- `compat-tests` and `size-budget` ran when the rules above ask for them.

## Never

- Publish anywhere but `mavenLocal`; create repositories, organizations or tokens.
- Turn on automatic CI runs or change the `on:` triggers.
- Raise Kotlin, Gradle, plugins or libraries without the maintainers.
- Change global settings (`~/.gradle/gradle.properties`, `~/.m2/settings.xml` and the like).
- Delete files or refactor widely beyond the task.
- Commit a real calendar or anything cut from it.

## Where things are

```
AGENTS.md, CONTRIBUTING.md       how to work here
build-logic/src/main/kotlin/     convention plugins and build tasks
gradle/libs.versions.toml        versions
calendrum-core/                  the core, package io.github.calendrum; api/ holds the ABI dumps
calendrum-bom/                   the BOM
conformance/                     language-neutral test cases (ical/, ops/) and their runner
corpus/                          the layout of real calendars kept locally (real/ is never committed)
fuzz/                            fuzz targets and the inputs they found
compat-tests/                    consumer projects (separate builds), the case matrix, run.sh
size-budget/                     the size budget: a separate build, baseline.json, run.sh
benchmarks/                      kotlinx-benchmark suites, not published
guides/                          the user guides; their code samples compile in the tests
tooling/                         the command line, the MCP server and the SDKs (a separate build)
.github/workflows/, actions/     CI, started by hand
kotlin-js-store/                 yarn.lock of the JS and Wasm tests (committed)
gradle.properties                GROUP, VERSION_NAME, calendrum.targets
```

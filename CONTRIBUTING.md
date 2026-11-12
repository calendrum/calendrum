# Contributing to Calendrum

Calendrum is in early development and the repository is private. This guide covers building and checking the code, the
code style and what a pull request needs before it is merged. Coding agents read [AGENTS.md](AGENTS.md), which sums up
the same rules.

## Where the work is

- Work is tracked in GitHub issues. Use the issue forms: **Bug**, **Feature request** and **API proposal**; how requests are
  decided is in "Issues and triage" below. How versions are released is in [RELEASING.md](RELEASING.md).
- The maintainers keep the roadmap and pick what comes next. Before you start on anything larger than a bug fix,
  ask in an issue, so that the work fits the current milestone.

## Issues and triage

Open an issue with one of the forms. Every request is answered with a decision: once the repository is public, the first
answer comes within a week, even when it is "no, and here is why".

**How a request is decided.** The maintainers go through these questions in order:

1. Does the library break the RFC, or fail on a real producer's data? Then it is a bug, or a fixup for that producer.
2. Can it be solved with what exists: keys, the registry, the transport, selective parsing? Then the answer is a recipe in the
   guides, or a note that the community can build it on the extension point; the core stays as it is.
3. Do many need it: several independent requests, or the same thing in two or more popular libraries? Then it is a candidate.
4. Does it need outside data or someone else's API that goes out of date? Then it is a separate module, and only with an owner
   named for it.
5. Does it grow the public API of the core? Then only through an [API proposal](#api-proposal-first).

**A refusal always has an explanation and a label.** A request is not dropped silently. It is closed with one of
`wontfix: out-of-scope`, `wontfix: narrow` or `wontfix: app-level` and a few lines on why. A refusal is not forever: at each
release the maintainers look again at whether an audience has appeared.

**Labels.**

| Label | Meaning | Set by |
|---|---|---|
| `type:bug` | a bug (the Bug form) | the form |
| `type:api` | an API proposal (the API proposal form) | the form |
| `type:feature` | a feature request (the Feature request form) | the form |
| `wontfix: out-of-scope` | not what this library is for: another product, or a layer above or beside it | maintainers |
| `wontfix: narrow` | too few people need it for it to belong in the library | maintainers |
| `wontfix: app-level` | the meaning is the application's (what counts as a "reschedule", how a rule is worded, which time suits everybody) | maintainers |
| `ci:native` | on a pull request: also run Kotlin/Native tests on Linux and compile the Apple klibs | the author of a pull request that touches those platforms |
| `ci:macos` | on a pull request: also run the Apple tests | the same |
| `ci:windows` | on a pull request: also run the `mingwX64` tests | the same |
| `good first issue` | small, clearly described and has a pointer to the code | maintainers |

The `ci:` labels take effect when CI runs on pull requests (see the pull request template). `good first issue` and the
one-week answer start when the project becomes public.

## Building and checking

You need JDK 17 or newer (CI uses 21). Gradle and Kotlin come with the wrapper, so nothing else has to be installed.
The first build downloads dependencies from Maven Central and the Gradle Plugin Portal, the Kotlin/Native toolchains
from `download.jetbrains.com`, and Node.js for the JS and Wasm tests.

```bash
./gradlew check -Pcalendrum.targets=jvm      # quick loop: lint, ABI dump (JVM) and JVM tests
./gradlew check                           # every target this host can test, both ABI dumps, lint
./gradlew lint                            # ktlint through Spotless
./gradlew format                          # fixes what ktlint can fix
./gradlew updateKotlinAbi                 # after an intended change of the public API
```

The `calendrum.targets` property selects the Kotlin targets: `all` (default), `tier1`, `web`, `apple`, `windows` or `jvm`.
Set it on the command line or once in `~/.gradle/gradle.properties`. The klib ABI dump is checked and updated only with
`all`. On a Mac without tvOS and watchOS simulator runtimes, skip their tests:

```bash
./gradlew check -x tvosSimulatorArm64Test -x watchosSimulatorArm64Test
```

`check` does not run the consumer compatibility builds and the size budget. Run them when you change published
artifacts, their dependencies, compiler flags or the code of `calendrum-core`:

```bash
compat-tests/run.sh                       # consumer projects on the oldest and newest supported Kotlin
size-budget/run.sh                        # how much calendrum adds to an app, against size-budget/baseline.json
```

CI workflows in `.github/workflows/` only run when started by hand for now. Before you ask for a review, run the same
checks locally.

## Dependencies

Gradle checks the SHA-256 of every artifact it downloads (libraries, plugins, their `.pom` and `.module` files, and the
Node.js, Yarn, Binaryen and Kotlin/Native archives) against `gradle/verification-metadata.xml`. The `size-budget` build has
its own `size-budget/gradle/verification-metadata.xml`. The check runs in every build, so an artifact that is not listed or
has another checksum fails it.

When a change adds a dependency, a plugin or a tool, or raises the version of one (Kotlin, a Gradle plugin, a library),
update both files with these two commands, the first one first, because `size-budget` takes the core from the local
Maven repository:

```bash
./gradlew --no-configuration-cache --refresh-dependencies --rerun-tasks --write-verification-metadata sha256 check -x tvosSimulatorArm64Test -x watchosSimulatorArm64Test publishToMavenLocal koverHtmlReport koverXmlReport koverLog :benchmarks:quickBenchmark -Pbench.include='\.sanity\.'
ANDROID_HOME="$HOME/Library/Android/sdk" ./gradlew --no-configuration-cache --refresh-dependencies --rerun-tasks --write-verification-metadata sha256 -p size-budget sizeReport
```

- Review the new entries as you review an ABI dump: the group, name and version are the ones you raised, and `origin` of
  every entry that Gradle wrote is `Generated by Gradle`. Entries written by hand keep their `origin`, which names the
  source of the checksum; do not delete them.
- Artifacts that resolve only on Linux and Windows (Kotlin/Native, Node.js and Binaryen archives) are entered by hand.
  When a raise changes their versions, add the matching entries with the SHA-256 from the official source
  (Maven Central, nodejs.org, the Binaryen release).
- `Dependency verification failed ... This can indicate that a dependency has been compromised` means that an artifact that
  is already listed has another checksum. That is an alarm, not a reason to regenerate the file: tell the maintainers.
- Never switch the verification off and never add `<trust>` entries.

## Code style

- The official Kotlin coding conventions, enforced by ktlint (`ktlint_code_style = intellij_idea` in `.editorconfig`):
  lines up to 120 characters, trailing commas in declarations, no wildcard imports, 4 spaces, LF, UTF-8 and a final
  newline. Run `./gradlew format`, then `./gradlew lint`.
- Identifiers, strings in code, Gradle task descriptions, `README.md` and `CHANGELOG.md` are in English.
- Every public declaration of a published module has KDoc in English: the first sentence says what it does, then links to
  the RFC sections, `@throws` where it throws and `@sample` from a compiled sample function. Declarations marked
  `@InternalICalApi` get one line: "Internal API, not a contract". `./gradlew check` fails on a public declaration without
  KDoc. A comment inside code explains *why* (an algorithm, an RFC section, a bug of another implementation, a platform
  limit), not *what* the code does.
- `calendrum-core` depends only on the Kotlin standard library and kotlinx-datetime. No reflection, no global mutable
  state, no implicit system time zone, no regular expressions on hot paths. Shared code lives in `commonMain`;
  platform code stays at the edges.
- In the public API: no `data class`, no `kotlin.Result`, no `enum` for values that the standard lets grow (use open
  enumerations), `sealed` only for sets the standard closes. New options go into settings objects, not into new
  default parameters. Explicit API mode is on: every public declaration states its visibility and type.
- Kotlin, Gradle, plugin and library versions live in `gradle/libs.versions.toml` and are not raised in passing.

## API proposal first

Any new or changed public API starts with an **API proposal** issue: the motivation, a Kotlin sketch, how it looks
from Java and Swift, and the alternatives. Code is written after the proposal is agreed. A pull request that changes
the ABI dump without an agreed proposal is not merged.

## Definition of Done

A pull request that touches the public API:

- tests in `commonTest`, passing in CI;
- the ABI dump updated with `./gradlew updateKotlinAbi`, and its diff reviewed;
- an entry in `CHANGELOG.md` under `Unreleased`;
- a benchmark before and after if a hot path changed (`./gradlew :benchmarks:quickBenchmark` with a suite filter);
  a slowdown of more than 10 % needs an explanation;
- a new dependency only with the maintainers' approval.

Any change:

- tests for new behaviour in `commonTest`, so that they run on every platform;
- `./gradlew check` passes (at least with `-Pcalendrum.targets=jvm`; with `all` when the build, the targets or other
  platforms are affected);
- a dependency, a plugin or a tool that the change adds or raises: `verification-metadata.xml` updated with the commands
  from the Dependencies section, and the `origin` of the new entries checked;
- the pull request description says what the change adds to building, testing, benchmarks or CI, so that the
  maintainers' developer guide can follow.

The pull request template carries the same checklist.

## License of contributions

Calendrum is licensed under the [Apache License, Version 2.0](LICENSE). A contribution submitted for inclusion is under
the same license, as section 5 of the license says, unless you state otherwise in writing. Do not add code or test files
that you may not license this way; data from third parties needs its source and license in [NOTICE](NOTICE).

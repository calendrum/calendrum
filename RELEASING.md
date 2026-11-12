# Releasing Calendrum

How a version of Calendrum is released, who does it and which versions are supported. The repository is private and
nothing is published to Maven Central yet, so some steps below describe the current state (GitHub Packages of this
repository) and say where that changes.

## Release train

- A minor version is released about every 6 to 8 weeks.
- Versions follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html). Before 1.0 a minor release may break the API;
  from 1.0 it may not, apart from what is marked `@ExperimentalICalApi` (see
  [Compatibility](guides/compatibility.md#api-stability)). Every change that matters to a user has a line in
  [CHANGELOG.md](CHANGELOG.md).
- A minor version goes out as a release candidate when one is needed (a pre-release version such as `1.2.0-rc01`; the
  workflow accepts any SemVer pre-release), then as the final version.
- Patch versions (`x.y.z`) carry fixes, as described in "Support policy" below.
- All published modules and the BOM share one version number.

## Who releases

A maintainer with the right to run the release workflow. The aim is at least two such maintainers.
Publishing is done only from CI (the `Publish` workflow), never from a developer machine: a local
`./gradlew publishToMavenLocal` is for trying things out and is never signed or released.

## Where it is published

- Now: GitHub Packages of this repository (`https://maven.pkg.github.com/<owner>/<repo>`), by `publish.yml`. Reading
  from GitHub Packages needs a token with the `read:packages` scope.
- Maven Central: TBD. The POM already names the license (Apache-2.0); signing, the rest of the POM (developers, SCM) and
  the staging steps of Central are decided when the repository is public.

CI workflows are started by hand for now, so every step below that says "run a workflow" is a manual start.

## Checklist before a release

Run on the commit that will be released.

1. **Tests and checks on every target.** `./gradlew check` with `calendrum.targets=all` (the default). It includes lint, both
   ABI dumps, the Objective-C header check, KDoc, the conformance suite and the regression run of the fuzz targets
   (`:fuzz:test`: every input ever found by fuzzing). On a Mac without tvOS and watchOS simulator runtimes:
   `./gradlew check -x tvosSimulatorArm64Test -x watchosSimulatorArm64Test`.
2. **Consumers.** `compat-tests/run.sh`: consumer projects on the oldest and newest supported Kotlin, Gradle and targets.
3. **Examples.** `examples/run.sh`: the command-line tool, the Ktor server, the Android and the iOS example against the
   freshly published artifacts.
4. **Size budget.** `size-budget/run.sh`: what the library adds to an application, against `size-budget/baseline.json`. A
   growth that is accepted on purpose updates the baseline with `--update-baseline` and is explained in the pull request.
5. **Time zone data.** Check that `calendrum-tzdata` has the latest IANA tz database: run `tzdata-update.yml` (or
   `third_party/update.sh --tzdb latest --cldr latest` locally). If there is a newer release, the data update is merged
   before the release.
6. **CI on the release commit.** Run `full.yml` on the commit: it starts every CI workflow, with macOS and Windows after the
   Linux ones pass.
7. **Reference feed in real clients** (minor releases). `./gradlew :tools:referenceFeed` writes `calendrum-reference.ics` and
   `expected.md` to `tools/build/reference-feed/`. Import the file and subscribe to it in Google Calendar, Apple Calendar
   (iOS and macOS), Outlook (web and desktop), Yandex Calendar and Thunderbird, and compare what each client shows with
   `expected.md`. The results table is kept by the maintainers; a difference between a client and the expected result
   becomes a lint rule or a fixup, or a line in the release notes.
8. **Benchmarks** (minor releases). `benchmarks/campaign.sh jvm` and `benchmarks/campaign.sh native-web`, one night each, on a quiet
   machine on mains power (the script waits for both and repeats a step that ran on battery). Compare the results with the last
   recorded run: a slowdown of more than 10 percent is explained in the release notes, and the numbers of
   [Performance](guides/performance.md) are updated.

## Steps

1. Move the entries of `Unreleased` in [CHANGELOG.md](CHANGELOG.md) under the new version number and today's date and leave an
   empty `Unreleased` above it. Breaking changes carry the migration.
2. Set `VERSION_NAME` in `gradle.properties` to the release version (without `-SNAPSHOT`) and commit both changes.
3. Go through the checklist above on that commit.
4. Run the `Publish` workflow (`publish.yml`, Run workflow) on the commit with the same version number as `VERSION_NAME`;
   the workflow takes the version from its input and does not compare it with `gradle.properties`. It refuses a version
   that is not SemVer or ends in `-SNAPSHOT`, and one whose tag `v<version>` already exists. It builds every target on
   Linux, checks the ABI dumps and publishes every module. A separate job then tags the built commit `v<version>`. A
   published version cannot be replaced in GitHub Packages: a faulty release is fixed by the next patch version.
5. After the release, set `VERSION_NAME` to the next `-SNAPSHOT` version and commit it.

## Support policy

The text for users is in [Compatibility](guides/compatibility.md#support-policy). In short, after 1.0:

- the latest minor version gets bug fixes and security fixes;
- the previous minor version gets security fixes until 6 months after the next minor version came out;
- a new major version does not extend that: after 2.0 the 1.x line gets security fixes only, for 6 months.

Fixes are released as patch versions. Before 1.0, and while the repository is private, the policy is a description and not a
commitment to outside users.

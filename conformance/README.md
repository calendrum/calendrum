# Conformance suite

A language-neutral set of test cases for Calendrum's syntax layer: an iCalendar input and, for each, the expected
lossless syntax tree and the expected diagnostics. This suite is what any SDK on top of the C facade will run
against its own port; the Kotlin runner here is the reference implementation. It is not published as a separate
archive yet.

The suite covers the syntax layer — names, parameters, raw values, structure and diagnostics — and typed values in jCal
form (`expected.jcal.json`, RFC 7265). The operations of the tools and SDKs have their own cases in `conformance/ops/`.

## Layout

```
conformance/
  ical/<case-id>/
    input.ics              the input, exactly as its bytes are (CRLF, LF, BOM, windows-1251, …)
    expected.tree.json      the expected lossless syntax tree, or {"rejected": true} for a strict-mode rejection
    diagnostics.json         the expected problems, in the order they are reported
    meta.json                id, description, tags, RFC references, source and license
    expected.fingerprints.json  only in cases tagged "fingerprints": the expected component fingerprints
    expected.jcal.json       the expected jCal form of the parse result, unless meta.json says "jcal": false
  schema/
    expected.tree.schema.json
    diagnostics.schema.json
    meta.schema.json
    expected.fingerprints.schema.json
    expected.jcal.schema.json
  build.gradle.kts           the `:conformance` module (not published)
```

`<case-id>` is a stable, kebab-case name equal to the directory name and to `meta.json`'s `id`. Once a case ships,
its id does not change; a case that is no longer needed is deleted, not renamed.

## File formats

### `input.ics`

The raw bytes of the input, exactly as a real file would have them: line endings (CRLF, bare LF, bare CR, mixed),
a byte order mark, a non-UTF-8 encoding, a fold in the middle of a multi-byte character, truncation — whatever the
case is about. Not re-encoded, not "cleaned up" by an editor.

### `expected.tree.json`

The result of parsing `input.ics` with the options from `meta.json`, as a neutral, lossless view of the syntax tree.
Two shapes, by the schema's `oneOf`:

- **A successful (or lenient) parse:**
  ```json
  {
    "charset": "UTF-8",
    "components": [
      {
        "name": "VCALENDAR",
        "properties": [
          { "name": "VERSION", "parameters": [], "value": "2.0" }
        ],
        "components": []
      }
    ]
  }
  ```
  `charset` is one of the encodings the library reads: `UTF-8`, `UTF-16LE`, `UTF-16BE`, `UTF-32LE`, `UTF-32BE`,
  `windows-1251`, `KOI8-R`, `windows-1252`. `components` lists the top-level components in input order; each
  component has its `name` (canonical: ASCII letters upper-cased), its `properties` in input order, and its nested
  `components` in input order. Each property has its `name`, its `parameters` (a list of `{name, values}` in input
  order — `values` is always a list, even for a single value), and its `value`: the raw value after unfolding and
  charset decoding, **not** unescaped (a `TEXT` value keeps its `\n`, `\,` and `\\` sequences literally). A property
  reported as "outside any component" (`ICAL-S021`) is not written to the tree at all — wrap it in a component if
  the case needs to keep it, or accept that it is diagnostics-only.
- **A strict-mode rejection:** `{"rejected": true}`. Strict mode stops at the first `Error`-level problem and
  throws; there is no partial tree to compare, only `diagnostics.json`, which lists the problems up to and
  including that error.

### `diagnostics.json`

The expected problems, in the exact order `ParseResult.problems` gives them:

```json
[
  { "code": "ICAL-S003", "severity": "warning", "line": 2, "column": 1, "offset": 8 }
]
```

`code` and `severity` come from the registry of problem codes in `guides/diagnostics.md`. `line` and `column` are
1-based; `offset` is 0-based, in UTF-8 bytes from the start of the input — or, for a transcoded input, from the start
of the decoded UTF-8 text, the same text that `line` and `column` count in. **Messages are never compared**: they
may change. Problems beyond the configured limit (`ICAL-S013`) are not individually listed, matching what the
listener actually receives.

### `meta.json`

```json
{
  "id": "bare-lf-line-ending",
  "description": "The whole input uses bare LF line endings instead of CRLF.",
  "tags": ["line-endings", "robustness"],
  "rfc": ["RFC 5545 §3.1"],
  "source": "synthetic",
  "license": "CC0-1.0"
}
```

- `id` — must equal the directory name.
- `description` — one short English sentence: what the case checks.
- `tags` — from a fixed set (see `schema/meta.schema.json`'s `tags` enum): `rfc5545-example`, `rfc6868-example`, `rfc7265-example`,
  `folding`, `line-endings`, `bom`, `charset`, `utf8`, `parameters`, `values`, `names`, `structure`, `robustness`,
  `limits`, `strict`, `encoding-signature`, `case-insensitivity`, `edge-case`, `fingerprints`. Adding a new tag means
  adding it to the schema's enum in the same change. `rfc7265-example` marks the RFC 7265 jCal examples. A case tagged `fingerprints` must have
  `expected.fingerprints.json`, and only such a case may have it.
- `rfc` — the sections the case is based on (`"RFC 5545 §3.1"`), or an empty list for a case with no direct RFC
  basis.
- `source` — `rfc` (bytes taken from an RFC example), `synthetic` (written for this suite) or `anonymized-real`
  (derived from a real calendar through the Calendrum anonymizer, see `corpus/README.md`). An `anonymized-real` case, and
  only such a case, is named `real-*`: it stays with whoever made it and out of the repository (`.gitignore`).
- `license` — `IETF-Trust-BSD` for an input built from RFC text (the IETF Trust Legal Provisions' Simplified BSD
  License for code components applies to `input.ics` in that case), `private` for an `anonymized-real` case, `CC0-1.0`
  for everything else in this suite.
- `options` (optional) — parse options that differ from the library's defaults, used verbatim to build the
  `ParseOptions` the runner parses the case with:
  ```json
  {
    "mode": "strict",
    "charset": "windows-1251",
    "limits": { "maxLineOctets": 20, "maxLines": 4, "maxProblems": 3, "maxDepth": 1, "maxComponents": 2 }
  }
  ```
  `charset` is a charset label or a `Content-Type`-style value, exactly as `ParseOptions.charset` accepts it. Every
  `limits` field is optional; omitted ones keep `ParseLimits.Default`.

### `expected.fingerprints.json`

Only in cases tagged `fingerprints`. The component fingerprints of `input.ics` (`ICal.fingerprints` with the default
options: `DTSTAMP` ignored, default limits, no charset hint; `meta.json`'s `options` do not apply), in input order:

```json
{
  "algorithmVersion": 1,
  "entries": [
    {
      "calendarIndex": 0,
      "name": "VEVENT",
      "uid": "lecture@example.com",
      "recurrenceId": "20260112T100000",
      "tzid": null,
      "ordinal": -1,
      "hash": "09cbc6bfe54b3488",
      "startOffset": 433,
      "endOffset": 629
    }
  ]
}
```

- **Which components.** Every child of every top-level `VCALENDAR` (a nested `VCALENDAR` included) and every top-level
  component that is not a `VCALENDAR` — the components a component stream yields. Properties of `VCALENDAR` itself
  belong to no fingerprint.
- **Key.** `calendarIndex` is the index of the top-level `VCALENDAR`, from 0, or -1 outside any calendar; `name` is the
  canonical component name. Then exactly one of: a `VTIMEZONE` with a `TZID` is keyed by `tzid`; any other component
  with a `UID` is keyed by `uid` and `recurrenceId` (the raw values of its own first `UID` and `RECURRENCE-ID`
  lines, without parameters; `recurrenceId` is null without one); anything else is keyed by `ordinal`, its index
  among the components of the same name without such a key in the same calendar, from 0. A keyed component has
  `ordinal` -1; the unused fields are null.
- **Hash** (algorithm version 1): xxHash64 with seed 0, written as 16 lowercase hex digits, of the canonical text of the
  component's subtree — its logical lines in order, each followed by CRLF:
  - a `BEGIN` line is `BEGIN:` and the component name with ASCII letters upper-cased (parameters on `BEGIN` dropped);
  - a property line is the property name upper-cased, then the rest of the line after the name exactly as it is after
    unfolding — except parameter names, which are upper-cased too; lines of ignored properties (`DTSTAMP` by default)
    are left out at any depth;
  - an `END` line is `END:` and the same name as its `BEGIN`, also for a component closed implicitly.

  So refolding, line endings, a byte order mark, the case of names and how a component is closed do not change the
  hash; values, parameters and their order, quoting and the order of lines do. A transcoded input is hashed after
  decoding to UTF-8.
- **Offsets.** `startOffset` and `endOffset` are those of the component's span in the tree: from the start of its
  `BEGIN` line to the end of its `END` line including the line ending (or to where it was closed implicitly), in UTF-8
  bytes from the start of the input, or of the decoded text for a transcoded input.

`ConformanceTest.fingerprintsMatchTaggedCases` compares these files; `ConformanceTest.parseComponentMatchesTheTree`
checks on every case that parsing one component by its fingerprint gives the same subtree as the full parse. An SDK
that exposes fingerprints should do both.

### `expected.jcal.json`

The parse result (with the options from `meta.json`) in jCal form, RFC 7265: an array of the top-level components in
input order, each `[name, properties, components]` with the name in lower case:

```json
[
  [
    "vcalendar",
    [
      [
        "prodid",
        {},
        "text",
        "-//Example Corp.//Example Client//EN"
      ]
    ],
    [
      [
        "vevent",
        [
          [
            "dtstart",
            {
              "tzid": "US/Eastern"
            },
            "date-time",
            "2006-01-02T12:00:00"
          ]
        ],
        []
      ]
    ]
  ]
]
```

- **A property** is `[name, parameters, type, value…]`. `name` and parameter names are lower-cased; a parameter with one
  value is a string, with several an array of strings; `VALUE` is never a parameter — its meaning is the `type`.
- **The type** is the property's value type: the type named by `VALUE`, or the property's default type from the
  registry (RFC 5545 and the RFCs the default registry installs). A value the default type cannot read takes the next type
  the property allows (`TRIGGER:19980403T120000Z` is `date-time`), and a `date-time` property whose values are all dates is
  `date`. A property the registry does not know and has no `VALUE` is `unknown`, with its raw value.
- **Values** follow RFC 7265 §3.5: `TEXT` is unescaped, a list (`CATEGORIES`, `RESOURCES`, `EXDATE`, `RDATE`, `FREEBUSY`) is
  one value per element; `date` is `YYYY-MM-DD`, `date-time` is `YYYY-MM-DDThh:mm:ss` with `Z` for UTC (`TZID` stays a
  parameter), `utc-offset` is `±hh:mm[:ss]`, `period` is `start/end` or `start/duration`; `integer` and `float` are JSON
  numbers (a `float` keeps its digits), `boolean` a JSON boolean; `recur` is an object of the rule parts in lower case in the
  order of the input (a part with one value is a scalar, with several an array); `GEO` is `float` with the array
  `[latitude, longitude]`, `REQUEST-STATUS` is `text` with the array `[code, description, data]`. Text keeps its case
  (`METHOD:xyz` stays `xyz`).
- **Nothing is lost.** A value its type cannot read is `unknown` with the raw value, a `VALUE` type that jCal does not
  define (`X-FOO`) is the type `x-foo` with the raw value, and a repeated parameter is one array of all its values.
- **No jCal.** A strict-mode rejection has no tree, and a tree that canonical writing rejects (a name that is not a token,
  a control character, a parameter without a value) is not written as jCal either, since reading could not build it
  again: `meta.json` then says `"jcal": false` with a `"jcalReason"`, and the case has no `expected.jcal.json`.

`ConformanceTest.jcalMatchesEveryCase` compares these files on every platform; `Rfc7265Test` checks the two examples of
RFC 7265 Appendix B (cases `rfc7265-example-1` and `rfc7265-example-2`) against the jCal printed in the RFC: example 1 is
the same, example 2 differs only where the RFC's jCal contradicts its own iCalendar (the order of `VERSION` and `PRODID`,
`BYDAY=-1SU`, the `DTSTART` and the `SUMMARY` of the second event); both printed examples read back to the same jCal.
`JcalRoundTripTest` reads every case back from its jCal and compares the trees.

## Schemas

`schema/*.schema.json` are ordinary JSON Schema (2020-12) documents. `expected.tree.schema.json` describes the two
tree shapes above; `diagnostics.schema.json` describes the problem list; `meta.schema.json` describes the
metadata, including the tag enum and the option shapes; `expected.fingerprints.schema.json` describes the
fingerprints; `expected.jcal.schema.json` describes the jCal form. Every SDK's own runner should validate against the
same schemas before comparing output, so a
malformed case fails loudly instead of silently passing.

The Kotlin runner ships its own minimal JSON reader, writer and JSON Schema validator
(`src/commonTest/kotlin/io/github/calendrum/conformance/Json*.kt`) rather than a library, to keep `:conformance` free
of a JSON dependency in `commonMain`-adjacent code.

All JSON files in a case, and the schema files themselves, are stored in **canonical form**: UTF-8, no byte
order mark, two-space indentation, `\n` line endings, one blank final newline, object keys in the order the schema
declares them, and strings escaped per RFC 8259 (`\"`, `\\`, `\n`, `\r`, `\t`, and `\uXXXX` for other control
characters — everything else is written literally, including non-ASCII characters). This keeps diffs small and
reviewable. `expected.tree.json`, `diagnostics.json` and `expected.fingerprints.json` are always written in this form
by `updateExpected` (below); if you add a `meta.json` or a schema file by hand, match the existing files' formatting.

## Adding a case

1. Pick a stable, descriptive, kebab-case id and create `conformance/ical/<id>/`.
2. Write `input.ics` with the exact bytes the case needs (a hex editor or a short script, not a text editor that
   might rewrite line endings or add a byte order mark).
3. Write `meta.json` by hand (id, description, tags, `rfc`, `source`, `license`, and `options` if the case needs
   non-default parse options).
4. Run `./gradlew :conformance:updateExpected` to generate `expected.tree.json`, `diagnostics.json` and
   `expected.jcal.json` (and `expected.fingerprints.json` if the case is tagged `fingerprints`) from the current parser.
   If the input has no jCal form, the task says why: add `"jcal": false` and that reason as `"jcalReason"` to `meta.json`.
5. **Read the generated files.** `updateExpected` writes whatever the parser currently produces — it does not know
   whether that is correct. Check the tree and the diagnostics against what RFC 5545/6868 and
   `guides/diagnostics.md` actually say the case should do. If the output looks wrong, that is a parser bug: fix the
   parser (with a `commonTest` regression test) and re-run `updateExpected`, never hand-edit the expected files to
   match a bug.
6. Run `./gradlew :conformance:jvmTest` (or `check`) to confirm the suite is green, including
   `everySyntaxCodeHasACase` and the schema/layout checks.

## Running the suite

```bash
./gradlew :conformance:jvmTest              # this module's tests on the JVM
./gradlew :conformance:allTests             # every platform this host can run
./gradlew check                             # the whole repository, conformance included
./gradlew :conformance:updateExpected       # regenerate the expected files (JVM only)
```

`updateExpected` also regenerates the expected files of the real corpus (`corpus/README.md`), whose golden tests,
`RealCorpusTest`, run in this module's `jvmTest`.

`updateExpected` **always** needs a human to review its diff before it is committed — never run it just to make a
failing test pass without understanding why the parser's output changed.

Cases are embedded into the test sources at build time by the `generateConformanceSources` Gradle task (a
generated Kotlin file per ~100 cases, to stay well under the JVM's 64 KiB per-method bytecode limit), so the
suite runs on every platform the host can build test binaries for — no file reads at test time, and therefore no
difference in behavior between platforms.

## What each syntax code needs

Every code from `ICAL-S001` to `ICAL-S028` (`guides/diagnostics.md`) has at least one case that triggers it;
`ConformanceTest.everySyntaxCodeHasACase` enforces this. All 28 are reachable through ordinary input or a small
limit override in `meta.json`'s `options`. The one exception is `ICAL-S029` (time budget exceeded): it depends on a
clock, and `options` deliberately has no clock or time limit, so no case can trigger it deterministically.

`ConformanceTest.anonymizedRealCasesRoundTrip` checks that a case is named `real-*` exactly when its `source` is
`anonymized-real`, and for every such case that is present (none in a public checkout) checks that
writing its tree byte-stably reproduces `input.ics` (with CRLF line endings), that the canonical output reads back as an
equal tree without problems, and that the cursor's events give the same tree with the same positions. An SDK that writes
iCalendar should do the same.

`ConformanceTest.streamsMatchEveryCase` also reads every case as a stream, in chunks of 1 to 16 bytes and 64 KiB,
and requires the same tree, problems and positions as the byte array. An SDK that reads streams should do the same.

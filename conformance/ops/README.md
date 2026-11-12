# The operations contract

The same twelve operations stand behind every transport of the project: the MCP server, the command line and the SDKs for
other languages. An operation takes one JSON object and returns one JSON object. This directory is the definition of that
contract, independent of any language: the schemas say what the fields are, and the cases say what a response must be. A new
SDK runs these cases against its own port or its binding, and the Kotlin implementation in `tooling/ops` is the reference.

## Layout

```
conformance/ops/
  schema/
    envelope.schema.json             every response
    common.schema.json               shared definitions
    <operation>.request.schema.json  the fields of a request
    <operation>.result.schema.json   the fields of `result`
  <operation>-<case>/
    request.json    the request
    expected.json   the response, compared as JSON and not as text
    meta.json       id, operation, description, source, license, and the fields to ignore
```

`<operation>` is written with a hyphen in the directory name (`rrule-expand-…`) and with an underscore in `meta.json`
(`rrule_expand`).

## How a case is checked

1. Run the operation named in `meta.json` on `request.json` with the fixed environment below.
2. In the response and in `expected.json` drop every field called `message`, and the fields listed in `ignore_fields` of
   `meta.json`. Messages are for people and may change; `tzdb_version` follows the time zone data.
3. Compare the two as JSON values: the order of the fields of an object does not matter, the order of an array does.
4. Check the response against the schemas, and also the request when the case succeeds: a case about an error may break them on
   purpose.

**The fixed environment:** the clock says `2026-01-01T00:00:00Z`; a request for `n` random bytes gets the bytes `0, 1, …, n-1`,
which also fixes the anonymization key and a generated `UID`; the limits are the defaults below.

The schemas add one keyword to JSON Schema, `x-untrusted`, explained in the next section. A checker needs only the
keywords the schemas use: `$ref`, `type`, `enum`, `oneOf`, `properties`, `required`, `additionalProperties`, `items`.

## Text from the calendar is data

A calendar comes from a stranger. Its `SUMMARY`, `DESCRIPTION`, names of properties and zones can say anything, including
sentences written for a program that reads them, such as "ignore your instructions". A response keeps such text apart:

- It appears **only in the fields marked `"x-untrusted": true`** in the schemas of the results, and in the whole of the `ics`
  and `jcal` values. Show it to a person or pass it to a model only as data.
- It never appears in `error.message`, in `problems[].message`, in `issues[].message` or in any other field. These are fixed
  sentences in plain text, without any markup. An error does not quote the request either.

| Marked fields | Where |
|---|---|
| `summary`, `uid`, `start`, `start_zone`, `end`, `end_zone`, `rrule`, `status` | events of `inspect` |
| `prod_id`, `version`, `method`, `names`, `time_zones` | calendars of `inspect` |
| `path` | `validate`, `fix`: names of components and properties, which a file can choose |
| `component`, `uid`, `summary`, `recurrence_id`, `recurrence_id_zone` | occurrences of `expand` |
| `uid`, `summary`, `type` | `freebusy` |
| `name`, `uid`, `recurrence_id`, `tzid` | `diff` |
| `ics`, `jcal` | `inspect`, `fix`, `convert`, `build_event`, `anonymize` |

`build_event` and `rrule_expand` write what the request gave them, so the same care applies to the request.

## The envelope

```json
{"ops_version": 1, "ok": true, "result": {}, "truncated": false}
{"ops_version": 1, "ok": false, "error": {"code": "OPS-BAD-ARGUMENT", "message": "The field from is required"}}
```

`truncated: true` means the response does not hold everything: a limit cut a list, conflicts were not computed, or the input
was not read whole (`ICAL-S010`, `S013`, `S014`, `S022`, `S023`, `S029`, `S030`, `R001`). A problem of a calendar is not an error of the
operation: the operation succeeds and lists it in `problems` with its `ICAL-…` code.

| Error | When |
|---|---|
| `OPS-BAD-ARGUMENT` | The request is not valid JSON or not an object, a field has the wrong type or value, or the fields do not make a valid event. |
| `OPS-UNKNOWN-OPERATION` | There is no such operation. |
| `OPS-UNKNOWN-ZONE` | A zone of the request is not in the time zone data. |
| `OPS-UNKNOWN-CODE` | `explain` does not know the code. |
| `OPS-TOO-LARGE` | The request or the calendar is above the limit, or a response that is not a list is. |
| `OPS-INTERNAL` | A bug. No operation should ever return it. |

## The operations

| Operation | Takes | Returns |
|---|---|---|
| `inspect` | `ics`; `include_jcal`, `limit` | the charset, calendars with counts, zones and events, the problems of parsing |
| `validate` | `ics`; `rules` (`rfc5545`, `lint`), `limit` | `valid`, `calendar_count` (`valid` is false when it is 0 or when a problem of parsing is an error), issues with code, severity and path; `fixable` under `lint` |
| `fix` | `ics`; `fixups` | the repaired `ics`, `changed`, the list of changes (the repair, spelled as in `fixups`, and the path, never a value) |
| `expand` | `ics`, `from`, `to`; `zone`, `include_cancelled`, `limit` | occurrences, in the order of each calendar |
| `rrule_expand` | `rrule`, `start`; `zone`, `limit` | dates in the form of the start, and the moments of those that have one |
| `freebusy` | `ics`, `from`, `to`; `zone`, `conflicts`, `limit` | merged busy intervals and pairs of overlapping occurrences |
| `build_event` | `summary`, `start`; `end` or `duration`, `zone` or `all_day`, `description`, `location`, `rrule`, `organizer`, `attendees`, `uid`, `dtstamp` | a valid `ics` with its `VTIMEZONE`, and the `uid` |
| `diff` | `before`, `after`; `limit` | added, removed and changed components, by fingerprint; `DTSTAMP` does not count. What lies beyond the limits has no fingerprint: the response is then truncated |
| `convert` | `ics` or `jcal` (an array of calendars, or one calendar) | the other form; `jcal` in a result is always an array of calendars |
| `anonymize` | `ics`; `shift_weeks` | `ics` without text and addresses; a random key for each call that never leaves it. What lies beyond the limits is not in the output: the response is then truncated |
| `tz_resolve` | `name`; `at` | the IANA identifier, the offset, the next change of the offset |
| `explain` | `code` | the severity, the constant and the meaning of a problem code |

Wherever a calendar is taken, `<field>_base64` can replace `<field>` (`ics_base64`, `before_base64`, `after_base64`) for bytes in another
encoding. The operation does not read files or the network: that is up to the transport.

**Zones are explicit.** The time zone of the machine is never used. Without `zone`, the moments of a response are in UTC, nothing local is
written, and floating times are read in UTC. With `zone`, floating times are read in that zone, from the data of the library, so every
platform gives the same answer. A local time in a request needs a zone: nothing is assumed.

## Limits

| Limit | Value |
|---|---|
| A request | 128 MiB of characters, nesting up to 64 |
| A calendar | 64 MiB, and the rest of the limits for files from strangers: lines up to 64 KiB, 1,000,000 lines, 100,000 components, depth 8, 2 seconds of parsing |
| A list | `limit`, 1000 by default, 100,000 at most. A transport may lower the default (the MCP server has 100), not the bound |
| A response | 16 MiB |
| Problems | 1000 listed. Past that the parser stops reporting them, `problem_count` stops with it, and the response is truncated |
| Expansion | A budget of 5,000,000 dates is divided among the series of a call, and dates before the window count, as they do for `COUNT`. Each series gets its share, but at least 1000 and at most 1,000,000, so a file with 99,999 series can reach about 100 million dates: seconds on the JVM and Native. On Node a date costs more, so the TypeScript package lowers that floor to 50: there the budget holds, and more than 50,000 series can come out truncated. `freebusy` keeps all its intervals in memory until it merges them, so for it the budget holds everywhere, without a floor |
| Conflicts | not computed above 2000 occurrences in the window: their number grows with its square |

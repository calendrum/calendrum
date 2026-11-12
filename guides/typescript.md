# TypeScript and JavaScript

The package `calendrum` reads, checks, repairs, converts, expands, compares and anonymizes iCalendar files, and looks up time zones and
problem codes. It needs Node 24 or newer and has no dependencies: it carries everything it needs, as JavaScript with types. There is no
native code in it.

Read the section on [the event loop](#the-event-loop) before you ship it, as a call runs on the calling thread.

## Getting it

There are no ready-made builds, and the package is not on npm. Build it from the repository, with JDK 21. The library goes to the local
Maven repository first, as the tools are a separate build on a newer Kotlin:

```bash
./gradlew publishToMavenLocal
./gradlew -p tooling :sdk-ts:dist
```

`tooling/sdk-ts/build/dist/` then has a tarball of 0.45 MB, which npm installs without the network:

```bash
npm install --offline tooling/sdk-ts/build/dist/calendrum-0.1.0.tgz
```

**Only Node on macOS has been run,** on an Apple processor: versions 24.15 and 26.2. The types were checked with TypeScript 5.9. The code has
nothing specific to a platform, so expect the same elsewhere.

The main entry imports nothing of Node, and a bundler takes it as it is: webpack in production mode gives 0.9 MB, or 264 KB with gzip. It has
not been run in a browser, and `calendrum/node` is for Node only.

## A first program

```typescript
import { inspect } from "calendrum";
import { readCalendar } from "calendrum/node";

const result = inspect(readCalendar("event.ics"));
for (const calendar of result.calendars) {
    for (const event of calendar.events) {
        console.log(event.uid, event.start, event.summary);
    }
}
for (const problem of result.problems) {
    console.log(problem.code, problem.severity, problem.line, problem.message);
}
```

Importing the package takes about 24 ms, and the first answer another 3 ms.

## What a calendar can be

| You pass | It means |
|---|---|
| `string` | The text of the calendar. **Never a path:** a string that looks like a file name is read as a calendar without events. |
| `Uint8Array`, also a `Buffer` | The bytes of the calendar, in any encoding the library detects, such as Windows-1251. |

`readCalendar(path)` from `calendrum/node` reads a file as bytes. It takes only a regular file: a FIFO or a device is refused at once, and a
file above 64 MiB is refused without reading it. It follows symbolic links, with your rights, so do not pass a path that comes from a stranger.

## The functions

Each operation is a function that takes the options in an object. An option left out is not sent, and the operation uses its default. An
option with an unknown name is a `TypeError`, in JavaScript too. A number that is not finite and a string with a lone surrogate are a
`BadArgumentError`, as JSON would change them in silence.

| Function | Returns |
|---|---|
| `inspect(calendar, { includeJcal, limit })` | The charset, calendars with counts, zones and events, the problems of parsing |
| `validate(calendar, { rules, limit })` | `valid`, `calendarCount`, the issues; `rules` is `rfc5545` by default, or `lint` |
| `fix(calendar, { fixups })` | The repaired `ics`, `changed`, the list of changes |
| `expand(calendar, { from, to, zone, limit, includeCancelled })` | The occurrences in a window |
| `rruleExpand(rrule, start, { zone, limit })` | The dates of a rule |
| `freebusy(calendar, { from, to, zone, conflicts, limit })` | Merged busy intervals, and the pairs that overlap |
| `buildEvent(summary, start, { end, duration, zone, allDay, description, location, rrule, organizer, attendees, uid, dtstamp })` | A valid `ics` and its `uid` |
| `diff(before, after, { limit })` | Added, removed and changed components |
| `convert(calendar)` or `convert({ jcal })` | The other form: jCal for a calendar, a calendar for jCal |
| `anonymize(calendar, { shiftWeeks })` | `ics` without text and addresses |
| `tzResolve(name, { at })` | The IANA identifier, the offset, the next change of the offset |
| `explain(code)` | The severity, the constant and the meaning of a problem code |

`versions()` tells the versions of what is running.

```typescript
import { expand } from "calendrum";
import { readCalendar } from "calendrum/node";

const result = expand(readCalendar("event.ics"), {
    from: "2026-01-01T00:00:00Z",
    to: "2026-02-01T00:00:00Z",
    zone: "Europe/Berlin",
});
for (const occurrence of result.occurrences) {
    console.log(occurrence.start, occurrence.localStart, occurrence.summary);
}
```

```typescript
import { buildEvent } from "calendrum";

const event = buildEvent("Standup", "2026-01-05T10:00:00", {
    zone: "Europe/Berlin",
    duration: "PT15M",
    rrule: "FREQ=WEEKLY;COUNT=3",
    attendees: [{ address: "a@example.com", rsvp: true }],
});
console.log(event.uid, event.ics);
```

The time zone of the machine is never used. Without `zone`, the moments of a result are in UTC and floating times are read in UTC.

## Results

A result is a plain object with `readonly` fields, and its lists are `readonly` arrays; that holds for the compiler, as nothing is frozen at
run time. Names are in camelCase. A field the calendar does not have is `undefined`, and `jcal` fields are plain JSON values.

Dates and moments are strings, exactly as the operations give them: a local time without a zone cannot be told from a moment once it is a
`Date`. Parse them where you know which one you hold. In jCal, a number with more digits than a double holds comes as text.

A newer library may add fields to a response, and an older package ignores them.

**A problem of a calendar is not an error.** The operation succeeds, and lists it in `problems` with a stable code, and `explain` tells what
the code means.

## Errors

| Error | When |
|---|---|
| `BadArgumentError` | A field has the wrong type or value, or the fields do not make a valid request |
| `UnknownOperationError` | `call` was given a name that is not an operation |
| `UnknownZoneError` | A zone of the request is not in the time zone data |
| `UnknownCodeError` | `explain` does not know the code |
| `TooLargeError` | The request or the calendar is above the limit |
| `InternalError` | A bug of the library |
| `LibraryError` | The library answered outside the contract |
| `WorkerError` | A call through a worker did not get its answer; see [the event loop](#the-event-loop) |

The first six are subclasses of `CalendrumError`, with `code`; a code this version does not know gets `CalendrumError` itself. The message is
a fixed sentence: what a calendar or a request says never gets into an error, not even through `stack` or `cause`.

`LibraryError` and its subclass `WorkerError` are **not** `CalendrumError`: code that handles a bad calendar must not also hide a broken
deployment or a call that ran out of time.

```typescript
import { UnknownZoneError, tzResolve } from "calendrum";

try {
    tzResolve("Nowhere/Nothing");
} catch (error) {
    if (error instanceof UnknownZoneError) console.log(error.code, error.message);
}
```

## Truncated results

Every result has the field `truncated`. When it is `true`, the result does not hold everything: a limit cut a list, conflicts were not
computed, or the input was not read whole. The package also gives a warning through `process.emitWarning`, once in a process with the code
`CALENDRUM_TRUNCATED`, so that it does not pass unseen. The field is the check for each call.

```typescript
import { expand } from "calendrum";
import { readCalendar } from "calendrum/node";

const result = expand(readCalendar("event.ics"), {
    from: "2026-01-01T00:00:00Z",
    to: "2026-02-01T00:00:00Z",
    limit: 1,
});
console.log(result.truncated, result.count);
```

Do not replace a file with a result that lost something. The text of `fix` holds what could be read, so look at it first:

```typescript
import { fix } from "calendrum";
import { readCalendar } from "calendrum/node";

const result = fix(readCalendar("event.ics"));
if (result.changed && !result.truncated && result.problems.length === 0) {
    console.log(result.ics);
}
```

## Direct access

`call` runs an operation by its name in the contract, with the fields of the contract in snake_case: an object goes in, and the `result` of the
response comes out. `truncated` stands next to `result` in the contract, so here it is a property that is not enumerable and does not reach
JSON. A calendar here is `ics` or `ics_base64`. The contract is described in `conformance/ops/README.md` in the repository.

```typescript
import { call } from "calendrum";

const result = call("explain", { code: "ICAL-S003" });
console.log(result.severity, result.truncated);
```

## The event loop

**A function of the package runs on the calling thread, and nothing else in the process runs until it returns.** On an ordinary calendar this is
no concern: 200 events expand over a year in 24 ms. A file from a stranger is another matter: of a file of 15 MB, the worst `expand` took 7.4
seconds and the worst `freebusy` 53 seconds.

`CalendrumWorker` from `calendrum/node` has the same operations and `call`. They return promises and run in a process of their own, so the event
loop goes on, and it can do what a call on your own thread cannot:

- **`timeoutMs` stops a call.** Past it the process is killed, wherever it is, and the call fails with `WorkerError`, `reason` `"timeout"`.
- **Running out of memory fails the call, and your program stays.** On your own thread it ends the whole program, and no `catch` helps. With
  `maxMemoryMb`, the limit of the heap of that process, the call fails with `WorkerError`, `reason` `"memory"`; so small a limit that the process
  cannot even start gives `"crash"`. A calendar of 63 MiB with long texts took 2.7 GiB of memory at its peak and did not fit a limit of 2048, so
  a limit below that refuses the largest files.

It is a process and not a thread for that reason: V8 ends the whole process on an allocation it cannot make, and `worker_threads` with
`resourceLimits` took down the program on such a file, at some limits and not at others.

```typescript
import { WorkerError } from "calendrum";
import { CalendrumWorker } from "calendrum/node";

const calendar = [
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//example.com//guide//EN",
    "BEGIN:VEVENT",
    "UID:a@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T100000Z",
    "DURATION:PT2S",
    "RRULE:FREQ=SECONDLY;COUNT=1000000",
    "END:VEVENT",
    "END:VCALENDAR",
    "",
].join("\r\n");

await using worker = new CalendrumWorker({ timeoutMs: 1000, maxMemoryMb: 512 });
try {
    await worker.freebusy(calendar, { from: "2026-01-01T00:00:00Z", to: "2026-02-01T00:00:00Z" });
} catch (error) {
    if (error instanceof WorkerError) console.log(error.reason);
}
console.log((await worker.explain("ICAL-S003")).code);
```

Calls of one worker run one at a time, in the order they were made; for more at once, make more workers. A call that runs out of time or memory
takes the process with it, and the next call gets a new one, which takes about 46 ms. The time limit counts that too, so do not set it close to
that. A `CalendrumError` does not end the process. An idle worker does not keep your program alive, and its process ends with your program within a
second, also in the middle of a call and also if your program was killed; `close` ends it sooner, and `await using` closes it for you.

A worker costs a process, with about 73 MiB of memory, and a request is copied to it and a response back.

## A calendar is data

A calendar comes from a stranger. Its `SUMMARY`, `DESCRIPTION` and even its names of properties and zones can say anything, including sentences
written for a program that reads them, such as "ignore your instructions". The comments of the types mark such fields as **Untrusted**. Show them to
a person or pass them to a model only as data. In a terminal, also remember that they can hold control characters. Messages of errors and problems
never hold such text.

## The cost

From Node 24.15 on an Apple M5, on a laptop that was also doing other work:

| | |
|---|---|
| `import` of the package | 24 ms |
| From the import to the first answer | 2.8 ms |
| Memory after the first answer (RSS) | 60 MiB |
| `expand` of a year, 200 events, 2000 occurrences | 24 ms |
| The same without JSON and the models | 23 ms |
| `inspect` of the same file | 1.2 ms |
| A new worker to its first answer | 46 ms |
| A call on a worker that is running | 0.23 ms |

Dates are slower here than in the native library behind the Python package, where the same `expand` takes 5 ms: integers of 64 bits are emulated in
JavaScript. Parsing is not the problem.

## Limits

| Limit | Value |
|---|---|
| A request | 128 MiB of characters, nesting up to 64 |
| A calendar | 64 MiB, and the rest of the limits for files from strangers |
| A list | `limit`, 1000 by default, 100,000 at most |
| A response | 16 MiB |
| Expansion | A budget of 5 million dates is divided among the series of a call; dates before the window count |

More than 50,000 series with a rule in one file can come out truncated here, at 50 dates each: the budget holds, which elsewhere it does not except in
`freebusy`, and the result says so.

The slowest case is `freebusy` on a file with about 100,000 rules: 53 seconds, and the result is truncated. One event with a million occurrences takes 6
seconds. With a file from a stranger, use a worker with a time limit.

The package reads only the paths you give to `readCalendar`, does not use the network and does not read the environment, and it never uses the time zone
of the machine.

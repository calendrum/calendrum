# Python

The package `calendrum` reads, checks, repairs, converts, expands, compares and anonymizes iCalendar files, and looks up time zones and
problem codes. It needs Python 3.9 or newer and has no dependencies: a wheel carries the [C library](c-api.md), and the package calls it
through `ctypes`.

That library is a Kotlin/Native binary with a runtime of its own, and two things follow from it. Read the sections on
[`fork`](#fork) and [out of memory](#out-of-memory) before you ship it.

## Getting it

There are no ready-made builds, and the package is not on PyPI. Build the wheels from the repository, with JDK 21. The library goes to
the local Maven repository first, as the tools are a separate build on a newer Kotlin:

```bash
./gradlew publishToMavenLocal
./gradlew -p tooling :sdk-py:dist
```

`tooling/sdk-py/build/dist/` then has a wheel for each target. Install the one for your platform:

```bash
python3 -m pip install --no-index tooling/sdk-py/build/dist/calendrum-0.1.0-py3-none-macosx_12_0_arm64.whl
```

| Wheel | Needs | Size |
|---|---|---|
| `py3-none-macosx_12_0_arm64` | macOS 12 or newer | 1.6 MB |
| `py3-none-manylinux_2_17_x86_64` | glibc 2.17 or newer; not musl | 1.5 MB |
| `py3-none-manylinux_2_17_aarch64` | glibc 2.17 or newer; not musl | 1.3 MB |
| `py3-none-win_amd64` | Windows on x64 | 1.5 MB |

**Only the macOS wheel has been run**, on Python 3.9 and 3.14, and every number and every behavior in this guide comes from it. The
others are built on macOS: their contents have been read, pip has agreed with their tags, and that is all that is known of them.
Nothing is signed or notarized.

## A first program

```python
from pathlib import Path

import calendrum

result = calendrum.inspect(Path("event.ics"))
for calendar in result.calendars:
    for event in calendar.events:
        print(event.uid, event.start, event.summary)
for problem in result.problems:
    print(problem.code, problem.severity, problem.line, problem.message)
```

Importing the package loads nothing native. The library loads at the first call, which takes about 3 ms.

## What a calendar can be

| You pass | It means |
|---|---|
| `str` | The text of the calendar. **Never a path:** a string that looks like a file name is read as a calendar without events. |
| `bytes` | The bytes of the calendar, in any encoding the library detects, such as Windows-1251. |
| `pathlib.Path` or another `os.PathLike` | A file that this package reads. Only a regular file: a FIFO or a device is refused at once, and a file above 64 MiB is refused without reading it. |

The package reads a path that you give it, with your rights, and follows symbolic links. Do not pass a path that comes from a stranger:
give the bytes that you have checked instead.

## The functions

Each operation is a function, and everything after the first arguments is named. An argument left out is not sent, and the operation
uses its default.

| Function | Returns |
|---|---|
| `inspect(calendar, *, include_jcal, limit)` | The charset, calendars with counts, zones and events, the problems of parsing |
| `validate(calendar, *, rules, limit)` | `valid`, `calendar_count`, the issues; `rules` is `rfc5545` by default, or `lint` |
| `fix(calendar, *, fixups)` | The repaired `ics`, `changed`, the list of changes |
| `expand(calendar, *, from_, to, zone, limit, include_cancelled)` | The occurrences in a window |
| `rrule_expand(rrule, start, *, zone, limit)` | The dates of a rule |
| `freebusy(calendar, *, from_, to, zone, conflicts, limit)` | Merged busy intervals, and the pairs that overlap |
| `build_event(summary, start, *, end, duration, zone, all_day, description, location, rrule, organizer, attendees, uid, dtstamp)` | A valid `ics` and its `uid` |
| `diff(before, after, *, limit)` | Added, removed and changed components |
| `convert(calendar=None, *, jcal=None)` | The other form: jCal for a calendar, a calendar for jCal |
| `anonymize(calendar, *, shift_weeks)` | `ics` without text and addresses |
| `tz_resolve(name, *, at)` | The IANA identifier, the offset, the next change of the offset |
| `explain(code)` | The severity, the constant and the meaning of a problem code |

`versions()` tells the versions of the package and of the library.

```python
from pathlib import Path

import calendrum

result = calendrum.expand(
    Path("event.ics"),
    from_="2026-01-01T00:00:00Z",
    to="2026-02-01T00:00:00Z",
    zone="Europe/Berlin",
)
for occurrence in result.occurrences:
    print(occurrence.start, occurrence.local_start, occurrence.summary)
```

The time zone of the machine is never used. Without `zone`, the moments of a result are in UTC and floating times are read in UTC.

## Results

A result is a frozen `dataclass`, and so is everything inside it. Lists are tuples. The only values that are not frozen are `jcal`
fields, which are plain JSON values. A field the calendar does not have is `None`. Types are in the package, which is marked with
`py.typed`.

Dates and moments are strings, exactly as the operations give them: a local time without a zone cannot be told from a moment once it
is a `datetime`, and `datetime.fromisoformat` does not read `Z` before Python 3.11. Parse them where you know which one you hold.

A newer library may add fields to a response, and an older package ignores them.

**A problem of a calendar is not an error.** The operation succeeds, and lists it in `problems` with a stable code, and `explain` tells
what the code means.

## Errors

| Error | When |
|---|---|
| `BadArgumentError` | A field has the wrong type or value, or the fields do not make a valid request |
| `UnknownOperationError` | `call` was given a name that is not an operation |
| `UnknownZoneError` | A zone of the request is not in the time zone data |
| `UnknownCodeError` | `explain` does not know the code |
| `TooLargeError` | The request or the calendar is above the limit |
| `InternalError` | A bug of the library |
| `LibraryError` | The native library did not load, or failed outside any operation |
| `ForkedProcessError` | This is a child of `fork`, and the parent has called the library; see [`fork`](#fork) |

The first six are subclasses of `CalendrumError`, with `code` and `message`; a code this version does not know gets `CalendrumError`
itself. The message is a fixed sentence: what a calendar or a request says never gets into an exception, not even through `repr`.

`LibraryError` and its subclass `ForkedProcessError` are **not** `CalendrumError`: code that handles a bad calendar must not also hide a
broken deployment.

```python
import calendrum

try:
    calendrum.tz_resolve("Nowhere/Nothing")
except calendrum.UnknownZoneError as error:
    print(error.code, error.message)
```

A path that cannot be read gives the usual `OSError`, such as `FileNotFoundError`, with the path in it.

## Truncated results

Every result has the field `truncated`. When it is `True`, the result does not hold everything: a limit cut a list, conflicts were not
computed, or the input was not read whole. The package also gives a `calendrum.TruncatedWarning`, so that it does not pass unseen.

```python
import warnings
from pathlib import Path

import calendrum

with warnings.catch_warnings():
    warnings.simplefilter("ignore", calendrum.TruncatedWarning)
    result = calendrum.expand(
        Path("event.ics"),
        from_="2026-01-01T00:00:00Z",
        to="2026-02-01T00:00:00Z",
        limit=1,
    )
print(result.truncated, result.count)
```

Python shows a warning once for each line of code. To stop on any truncated result, use
`warnings.simplefilter("error", calendrum.TruncatedWarning)`, which raises it, and the result is not returned.

Do not replace a file with a result that lost something. The text of `fix` holds what could be read, so look at it first:

```python
from pathlib import Path

import calendrum

path = Path("event.ics")
result = calendrum.fix(path)
if result.changed and not result.truncated and not result.problems:
    path.write_bytes(result.ics.encode("utf-8"))
```

## Direct access

`call` runs an operation by its name, with the fields of the contract: a `dict` goes in, and the `result` of the response comes out as a
`dict`. `truncated` stands next to `result` in the contract, so here it is an attribute. A calendar here is `ics` or `ics_base64`, and
nothing reads a path. The contract is described in `conformance/ops/README.md` in the repository.

```python
import calendrum

result = calendrum.call("explain", {"code": "ICAL-S003"})
print(result["severity"], result.truncated)
```

## `fork`

**After the first call to the library in a process, a child of `fork()` cannot call it.** In a child the native call would hang forever,
at its first garbage collection, as the threads of the runtime do not survive a fork. So the package remembers the process of the first
call, does not make the native call in a child, and raises `ForkedProcessError`. One call in the parent is enough, even `versions()`.

| In the parent, before the workers start | How the workers start | In the workers |
|---|---|---|
| Nothing but `import calendrum` | `fork` | Works |
| Any call | `fork`, also through `ProcessPoolExecutor` | `ForkedProcessError`; the parent goes on working |
| Any call | `spawn`, `forkserver` | Works |
| Any call | `os.fork()`, and the child calls nothing and ends | Works |
| Other threads in the middle of calls | `fork` | `ForkedProcessError`; those threads go on working |

These were run on macOS, with real `multiprocessing`. From Python 3.12, `os.fork()` in such a process also gives a `DeprecationWarning`,
as Python sees the threads of the runtime. On Linux `multiprocessing` used `fork` by default up to Python 3.13.

What this means in practice:

- Do not call the library at import, or at the startup of a program that forks later. Importing it is fine.
- With a pool of workers, ask for `spawn` or `forkserver`, or call the library only in the workers. Keep calls under `if __name__ == "__main__":`:
  a `forkserver` imports the main module, and a call outside that guard runs in the server, whose children are then refused.
- `gunicorn` forks its workers. The rule is the same: without `--preload` the workers load your application themselves, and with `--preload`
  your application must not call the library while it starts. This follows from the table; `gunicorn` itself was not run.

```python
import multiprocessing
from pathlib import Path

import calendrum


def count_events(path):
    return calendrum.inspect(path).calendars[0].event_count


if __name__ == "__main__":
    print(calendrum.versions().package)
    with multiprocessing.get_context("spawn").Pool(2) as pool:
        print(pool.map(count_events, [Path("event.ics")]))
```

## Threads and cancelling

Call any function from any thread. `ctypes` releases the lock of the interpreter during a native call, so calls run at the same time, and
8 threads get the same answers as one.

**Keep a pool of threads, and do not start one for each call.** A thread that has called the library and ends leaves 300 to 450 KiB behind until
the next garbage collection, and short calls may not ask for one at all: 5000 threads with one short call each reached 2.3 GiB. `ThreadingHTTPServer`
does exactly this; use a `ThreadPoolExecutor` instead.

**A call cannot be cancelled.** `KeyboardInterrupt` arrives after the call ends. To put a time limit on a call on a file from a
stranger, run it in another process and end that process, as under [out of memory](#out-of-memory).

## Out of memory

**When memory runs out inside the native library, the whole process ends with `SIGABRT`.** No `except` can catch it, and `MemoryError` does
not come. A calendar of 64 MiB, the largest allowed, takes about 1 GiB at its peak. On a server that takes files from strangers, check the
size before the call, or do such work in a process of its own, where the loss of the process is the loss of one call:

```python
import multiprocessing
from pathlib import Path

import calendrum


def work(connection, data):
    connection.send(calendrum.validate(data).valid)


def validate_with_limit(data, seconds):
    """The answer, or None if the call took too long or its process died."""
    context = multiprocessing.get_context("spawn")
    receiver, sender = context.Pipe(duplex=False)
    process = context.Process(target=work, args=(sender, data))
    process.start()
    sender.close()
    try:
        return receiver.recv() if receiver.poll(seconds) else None
    except EOFError:
        return None
    finally:
        process.kill()
        process.join()


if __name__ == "__main__":
    print(validate_with_limit(Path("event.ics").read_bytes(), 60))
```

This is also the way to put a time limit on a call. A `ProcessPoolExecutor` is not: `future.result(timeout=...)` stops waiting, and the
worker goes on, and the pool waits for it when it closes.

A library that has loaded stays loaded until the process ends: there is no way to unload it.

## A calendar is data

A calendar comes from a stranger. Its `SUMMARY`, `DESCRIPTION` and even its names of properties and zones can say anything, including
sentences written for a program that reads them, such as "ignore your instructions". The docstrings of the models mark such fields as
**Untrusted**. Show them to a person or pass them to a model only as data. In a terminal, also remember that they can hold control
characters. Messages of errors and problems never hold such text.

## The cost

From the macOS wheel, on Python 3.14 (3.9 in brackets), on a laptop that was also doing other work:

| | |
|---|---|
| `import calendrum` | 14 ms (35 ms), of which most is `dataclasses`: importing it and building the models |
| From the import to the first answer | 3.3 ms (4.1 ms) |
| Memory after the first answer (peak RSS) | 26 MiB (24 MiB) |
| `expand` of a year, 200 events, 2000 occurrences, through `expand()` | 8.4 ms (9.2 ms) |
| The same through `call()` | 7.3 ms (7.4 ms) |
| The same through the library alone | 5.2 ms (5.3 ms) |

The difference is JSON and the models. A million occurrences do not fit a result: the largest list is 100,000.

## Limits

| Limit | Value |
|---|---|
| A request | 128 MiB of characters, nesting up to 64 |
| A calendar | 64 MiB, and the rest of the limits for files from strangers |
| A list | `limit`, 1000 by default, 100,000 at most |
| A response | 16 MiB |

The slowest case is `freebusy` on a file with about 100,000 rules: 10 seconds and 2.8 GiB of memory at its peak, and the result is truncated.
One event with a million occurrences takes less than a second. With a file from a stranger, use another process.

The package reads only the paths that you give it, and the library does not use the network or files, does not read the environment and
never uses the time zone of the machine.

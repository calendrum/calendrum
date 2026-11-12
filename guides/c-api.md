# C API

`libcalendrum` is a shared library with a C interface to the operations on iCalendar files: it reads a calendar, checks it,
repairs it, converts it to and from jCal, lists its occurrences, compares two calendars, strips the text from one, and looks up
time zones and problem codes. It is the base for bindings in other languages. There are four functions, and one of them runs every
operation: a JSON request goes in and a JSON response comes out, as `conformance/ops/README.md` in the repository defines them.

The library is a Kotlin/Native binary and carries its runtime, with a garbage collector and threads of its own. Most of this guide
is about what that means inside your process. Read the sections on [`fork`](#fork), [the stack](#the-stack) and
[out of memory](#out-of-memory) before you ship it.

## Getting it

There are no ready-made builds yet. Build it from the repository, with JDK 21. The library goes to the local Maven repository
first, as the tools are a separate build on a newer Kotlin:

```bash
./gradlew publishToMavenLocal
./gradlew -p tooling :sdk-c:dist
```

`tooling/sdk-c/build/dist/` then has an archive for each target: `calendrum-sdk-c-<version>-macosArm64.tar.gz`, `-linuxX64.tar.gz`,
`-linuxArm64.tar.gz` and `-mingwX64.zip`. Each holds the library, `calendrum.h`, `NOTICE`, the licenses of the libraries inside and
a short `README.txt`. The Windows archive also has the import library `libcalendrum.dll.a` and `calendrum.def`. Nothing is signed or
notarized.

| Target | File | Needs |
|---|---|---|
| `macosArm64` | `libcalendrum.dylib` | macOS 12 or newer |
| `linuxX64` | `libcalendrum.so` | glibc 2.14 or newer; not musl |
| `linuxArm64` | `libcalendrum.so` | glibc 2.17 or newer; not musl |
| `mingwX64` | `calendrum.dll` | `KERNEL32`, `ADVAPI32`, `bcrypt`, `msvcrt` |

**Only the macOS library has been run**, and every number and every behavior in this guide comes from it. The Linux and Windows
libraries are built on macOS: they link, their exports and dependencies have been read with tools, and that is all that is known of
them. The runtime is the same code on all of them, so expect the same behavior, and test it on your platform.

To get a `.lib` for MSVC, from a Visual Studio prompt (not tried yet):

```text
lib /def:calendrum.def /machine:x64 /out:calendrum.lib
```

## A first program

```c
#include <stdio.h>
#include <string.h>

#include "calendrum.h"

int main(void) {
    static const char request[] =
        "{\"ics\":\"BEGIN:VCALENDAR\\r\\nVERSION:2.0\\r\\nPRODID:-//Example//EN\\r\\n"
        "BEGIN:VEVENT\\r\\nUID:a@example.com\\r\\nDTSTAMP:20260101T000000Z\\r\\n"
        "DTSTART:20260105T100000Z\\r\\nSUMMARY:Standup\\r\\nEND:VEVENT\\r\\nEND:VCALENDAR\\r\\n\"}";
    uint8_t *response = NULL;
    size_t length = 0;

    if (calendrum_abi_version() != CALENDRUM_ABI_VERSION) {
        fprintf(stderr, "this library has another ABI\n");
        return 1;
    }
    int32_t code = calendrum_call("inspect", (const uint8_t *) request, strlen(request), &response, &length);
    if (code != CALENDRUM_OK) {
        fprintf(stderr, "calendrum_call failed with %d\n", code);
        return 1;
    }
    /* A failed operation is here too, with "ok": false: a program reads the response as JSON. */
    printf("%s\n", (const char *) response);
    calendrum_free(response);
    return 0;
}
```

```bash
cc -std=c99 -I. inspect.c -L. -lcalendrum -Wl,-rpath,. -o inspect
./inspect
```

## The functions

```c
int32_t calendrum_abi_version(void);
int32_t calendrum_version(uint8_t **out_json, size_t *out_len);
int32_t calendrum_call(const char *operation,
                       const uint8_t *request, size_t request_len,
                       uint8_t **out_response, size_t *out_len);
void calendrum_free(uint8_t *data);
```

**`calendrum_abi_version`** returns the number of the ABI, now `1`, which is also `CALENDRUM_ABI_VERSION` in the header. Functions
are only added; a change to an existing one would be a new major number.

**`calendrum_version`** writes the versions:

```json
{"abi":1,"contract":1,"library":"0.1.0-SNAPSHOT","sdk":"0.1.0"}
```

**`calendrum_call`** runs one operation. The request does not need a final zero and may contain one, as only `request_len` bytes
are read. A response ends with a zero for the convenience of C, and `*out_len` does not count it. The operations are `inspect`,
`validate`, `fix`, `expand`, `rrule_expand`, `freebusy`, `build_event`, `diff`, `convert`, `anonymize`, `tz_resolve` and `explain`.

| Code | Macro | Means |
|---|---|---|
| `0` | `CALENDRUM_OK` | The response is written. An operation that failed is here too, with `"ok": false`. |
| `1` | `CALENDRUM_BAD_ARGUMENT` | A required pointer is null, or `request` is null with a length that is not zero. |
| `2` | `CALENDRUM_INTERNAL_ERROR` | An internal failure, such as no memory for the response. |

With any code but `0`, `*out_response` is null and `*out_len` is `0`, for those of the two pointers that are not null themselves.
The reason an operation failed is only in the response:

```json
{"ops_version":1,"ok":false,"error":{"code":"OPS-UNKNOWN-OPERATION","message":"There is no such operation"}}
```

A request that is not valid UTF-8 is answered with `OPS-BAD-ARGUMENT`, a name that is not valid UTF-8 with
`OPS-UNKNOWN-OPERATION`, and a request above the limit with `OPS-TOO-LARGE` before it is copied. A `null` request of length `0` is an
empty request, which is not valid JSON. The message of an error is a fixed sentence and never quotes your input.

A response holds what a calendar says, and a calendar comes from a stranger. The fields that hold such text are marked in the
schemas of the contract: show them to a person or pass them to a model only as data.

## Memory

A response comes from the `malloc` of the library, and only `calendrum_free` releases it. `calendrum_free(NULL)` is allowed. On
Windows the library and your program can have different C runtimes, so `free` there is a heap corruption. A buffer can be released on
any thread, not only the one that got it.

## Threads

Call any function from any thread, at the same time, with no setup and no registration. 8 threads with 1000 calls each give the
same bytes as one thread. The throughput of `inspect` on a file of
200 events was 1.8 times higher with 2 threads, 2.9 with 4, and 3.4 with 8, on 4 performance cores and 6 efficiency cores.

A thread that has called the library and ends leaves 300 to 450 KiB behind until the next garbage collection, and calls that allocate
little may not ask for one at all. A thread for every call adds up: 6000 of them reached 1.5 GiB, and thousands of them each second stayed
around 3 GiB with pauses of up to 5 seconds. Keep a pool, where 100,000 calls keep memory flat.

A call cannot be cancelled. To put a time limit on a call on a file from a stranger, run it in another process.

## `fork`

**After the first call to any function of the library, including `calendrum_abi_version`, do not call it in a child of `fork()`.** The
call hangs forever. The runtime keeps a thread for garbage collection, a child does not inherit threads, and the first thread that
has to wait for a collection in the child waits for good. A short call can succeed first, so a test with one call passes and
production hangs.

| In the parent, before `fork` | In the child | Result |
|---|---|---|
| The library is not loaded | loads it and calls | works |
| The library is loaded, nothing called | calls | works |
| Any function called, even only the version | calls many times | **hangs at the first collection** |
| Any function called | one short call | passes, by luck |
| Any function called | calls nothing, then `exit`, `_exit` or `exec` | works |
| Other threads in the middle of calls | calls many times | **hangs** |

The parent keeps working in all of these. There is no setting of the runtime that helps: another collector hangs the same way, and
turning collection off gives a child that works and a parent whose memory never comes back (more than 1 GiB after 3000 calls).

What this means in practice:

- Do not call the library at import, or at startup, of a program that forks later. Loading it is fine.
- A pool of workers that forks: call the library only in the workers, or start them so that they do not fork: `multiprocessing` with
  `spawn` or `forkserver` rather than `fork`, `gunicorn` without `--preload`.
- A binding can notice the mistake and fail with a clear message: remember the process id at the first call, and refuse when it has
  changed.

## The stack

No operation needs much of it. Input that tries to go deep, 2 million nested `BEGIN`, a jCal at the limit of 64 levels, and JSON
nested far above the limit, works on a thread with a stack of **64 KiB**, also as the first call of the process. All the cases of
the contract do too. At 32 KiB a deep jCal crashes the process. Keep at least 256 KiB, to leave room for the code that calls the
library.

## Signals

The runtime installs no signal handlers: every signal keeps the disposition your process gave it, `SIGSEGV`, `SIGBUS` and `SIGPIPE`
included, so Python and the JVM are safe. It does not touch the signal mask or the locale.

The first call replaces the C++ terminate handler with `std::set_terminate`, for the whole process. A C++ program with a handler of
its own should set it after that call. The runtime prints to the standard error only when it dies, as in the next section.

## Out of memory

**When memory runs out inside the runtime, the process ends with `SIGABRT`,** and a line on the standard error says how many bytes it
could not get. This cannot be caught, by the library or by you: the runtime does not throw an exception for it. Only a failed
`malloc` of the response itself comes back, as code `2`.

So give the process more than the input needs. A request of 64 MiB, the largest calendar, takes about 1 GiB at its peak, as it
goes through JSON, sometimes also base64. Check the size of untrusted input before the call, or put such calls in a process of their
own.

## Unloading

`dlclose` returns 0 and does nothing: **the library stays in the process**, and the next `dlopen` gets it at the same address, in
the same state. This is safe, also with threads that called the library and end after `dlclose`. Do not count on memory or threads
coming back: load it once and keep it.

## Two copies in one process

Two copies of the file under different paths, as two packages that each bring their own have it, work side by side, each with a runtime
of its own: two more threads, its own memory. Only the four functions are exported, so the copies cannot take symbols from each other.
On macOS the Objective-C runtime prints a warning to the standard error for each class they share, on loading the second one; the
calls worked regardless. A buffer goes back to `calendrum_free` of the copy that returned it.

## The cost of loading

From the macOS library, median of 20 processes:

| | |
|---|---|
| `dlopen` to the first response | 2.8 ms |
| Memory (RSS): before, loaded, after the first response | 1.6, 10.0, 11.5 MiB |
| After 20 `expand` of 200 events | 22 MiB |
| Threads: before, loaded, after the first call | 1, 1, 3 |

Loading starts no threads. The first call of any function starts the runtime, with its 2 threads.

## Limits

| Limit | Value |
|---|---|
| A request | 128 MiB of characters, nesting up to 64 |
| A calendar | 64 MiB, and the rest of the limits for files from strangers |
| A list | `limit`, 1000 by default, 100,000 at most |
| A response | 16 MiB |

The contract has the complete table. The slowest case is `freebusy` on a file with about 100,000 rules: 10 seconds and 2.8 GiB of memory
at its peak, and the result is truncated. One event with a million occurrences takes less than a second. Bytes of a calendar go through
JSON, as text or as base64, which adds a third to the size of the second.

The library does not use the network or files, does not read the environment, and never uses the time zone of the machine.

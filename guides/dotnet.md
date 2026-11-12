# .NET

The package `Calendrum` reads, checks, repairs, converts, expands, compares and anonymizes iCalendar files, and looks up time zones and
problem codes. It needs .NET 8 or newer and has no dependencies: it carries the [C library](c-api.md) for each platform, and calls it
through `LibraryImport`. It uses no reflection, so it works with trimming and Native AOT.

That library is a Kotlin/Native binary with a runtime of its own, so a process has two garbage collectors. Read
[Two collectors](#two-collectors) and [Out of memory](#out-of-memory) before you ship it.

## Getting it

There are no ready-made builds, and the package is not on NuGet. Build it from the repository, with JDK 21 and the .NET 10 SDK. The
library goes to the local Maven repository first, as the tools are a separate build on a newer Kotlin:

```bash
./gradlew publishToMavenLocal
./gradlew -p tooling :sdk-net:dist
```

The build looks for the SDK in `-Pdotnet.root`, then in `DOTNET_ROOT`, then in `~/.dotnet`, then on the `PATH`.
`tooling/sdk-net/build/dist/` then has `Calendrum.0.1.0.nupkg`, of 6.2 MB, with the library of every target. Use it from a local folder:

```bash
dotnet nuget add source --name local "$PWD/tooling/sdk-net/build/dist"
dotnet add package Calendrum --version 0.1.0
```

| Runtime identifier | File | Needs |
|---|---|---|
| `osx-arm64` | `libcalendrum.dylib` | macOS 12 or newer |
| `linux-x64` | `libcalendrum.so` | glibc 2.14 or newer; not musl |
| `linux-arm64` | `libcalendrum.so` | glibc 2.17 or newer; not musl |
| `win-x64` | `libcalendrum.dll` | Windows on x64 |

**Only the macOS library has been run**, on .NET 8 and .NET 10, and every number and every behavior in this guide comes from it. The other
libraries are built on macOS: their headers have been read, and that is all that is known of them. Nothing is signed or notarized.

## A first program

```csharp
using System;
using Calendrum;

InspectResult result = ICal.Inspect(CalendarInput.FromFile("event.ics"));
foreach (InspectCalendar calendar in result.Calendars)
{
    foreach (InspectEvent item in calendar.Events)
    {
        Console.WriteLine($"{item.Uid} {item.Start} {item.Summary}");
    }
}
foreach (Problem problem in result.Problems)
{
    Console.WriteLine($"{problem.Code} {problem.Severity} {problem.Line} {problem.Message}");
}
```

Nothing native loads before the first call, which then takes about 20 ms, most of it the JIT.

## What a calendar can be

Every operation that reads a calendar takes a `CalendarInput`, and these convert to it by themselves:

| You pass | It means |
|---|---|
| `string` | The text of the calendar. **Never a path:** a string that looks like a file name is read as a calendar without events. |
| `byte[]` or `ReadOnlyMemory<byte>` | The bytes of the calendar, in any encoding the library detects, such as Windows-1251. |
| `CalendarInput.FromFile(path)` | A file that the package reads at that moment. A file above 64 MiB is refused without reading it. |

`FromFile` follows symbolic links and reads with your rights. Do not pass a path that comes from a stranger: give the bytes that you have
checked instead. .NET cannot open a path without waiting on a FIFO, nor ask what a path is, so `FromFile` opens nothing of size 0: a FIFO, a
device and an empty file all give a `BadArgumentException`. A path swapped for a FIFO between that check and the opening would still wait.

## The operations

Each operation is a static method of `ICal`, with what it cannot go without as its arguments, and the rest in an object of options. A
property left out is not sent, and the operation uses its default.

| Method | Options | Returns |
|---|---|---|
| `Inspect(calendar, options)` | `IncludeJcal`, `Limit` | The charset, calendars with counts, zones and events, the problems of parsing |
| `Validate(calendar, options)` | `Rules`, `Limit` | `Valid`, `CalendarCount`, the issues; `Rules` is `rfc5545` by default, or `lint` |
| `Fix(calendar, options)` | `Fixups` | The repaired `Ics`, `Changed`, the list of changes |
| `Expand(calendar, from, to, options)` | `Zone`, `Limit`, `IncludeCancelled` | The occurrences in a window |
| `RruleExpand(rrule, start, options)` | `Zone`, `Limit` | The dates of a rule |
| `Freebusy(calendar, from, to, options)` | `Zone`, `Conflicts`, `Limit` | Merged busy intervals, and the pairs that overlap |
| `BuildEvent(summary, start, options)` | `End`, `Duration`, `Zone`, `AllDay`, `Description`, `Location`, `Rrule`, `Organizer`, `Attendees`, `Uid`, `Dtstamp` | A valid `Ics` and its `Uid` |
| `Diff(before, after, options)` | `Limit` | Added, removed and changed components |
| `ConvertToJcal(calendar)` | | `Jcal` |
| `ConvertFromJcal(jcal)` | | `Ics` |
| `Anonymize(calendar, options)` | `ShiftWeeks` | `Ics` without text and addresses |
| `TzResolve(name, options)` | `At` | The IANA identifier, the offset, the next change of the offset |
| `Explain(code)` | | The severity, the constant and the meaning of a problem code |

`GetVersions()` tells the versions of the package and of the library.

```csharp
using System;
using Calendrum;

ExpandResult result = ICal.Expand(
    CalendarInput.FromFile("event.ics"),
    "2026-01-01T00:00:00Z",
    "2026-02-01T00:00:00Z",
    new ExpandOptions { Zone = "Europe/Berlin" });
foreach (Occurrence occurrence in result.Occurrences)
{
    Console.WriteLine($"{occurrence.Start} {occurrence.LocalStart} {occurrence.Summary}");
}
```

The time zone of the machine is never used. Without `Zone`, the moments of a result are in UTC and floating times are read in UTC.

## Results

A result is a sealed class whose properties cannot be changed, and so is everything inside it. A field the calendar does not have is `null`,
and nullable annotations say which ones can be. `Jcal` is a `JsonElement`, as jCal is a tree that no class says anything about.

Dates and moments are strings, exactly as the operations give them: a local time without a zone cannot be told from a moment once it is a
`DateTimeOffset`. Parse them where you know which one you hold. In jCal, a number with more digits than a double holds comes as text.

A newer library may add fields to a response, and an older package ignores them.

**A problem of a calendar is not an error.** The operation succeeds, and lists it in `Problems` with a stable code, and `Explain` tells
what the code means.

Results do not override `ToString`, so logging a result does not write what a calendar says into your log.

## Errors

| Exception | When |
|---|---|
| `BadArgumentException` | A field has the wrong type or value, the fields do not make a valid request, or a string holds half of a surrogate pair |
| `UnknownOperationException` | `Call` was given a name that is not an operation |
| `UnknownZoneException` | A zone of the request is not in the time zone data |
| `UnknownCodeException` | `Explain` does not know the code |
| `TooLargeException` | The request or the calendar is above the limit |
| `InternalLibraryException` | A bug of the library |
| `LibraryException` | The native library did not load, or failed outside any operation |
| `ArgumentNullException` | An argument is `null` |

The first six are subclasses of `CalendrumException`, with `Code` and `Message`; a code this version does not know gets `CalendrumException`
itself. The message is a fixed sentence: what a calendar or a request says never gets into an exception, and nothing is wrapped inside one.

`LibraryException` is **not** a `CalendrumException`: code that handles a bad calendar must not also hide a broken deployment.

```csharp
using System;
using Calendrum;

try
{
    ICal.TzResolve("Nowhere/Nothing");
}
catch (UnknownZoneException error)
{
    Console.WriteLine($"{error.Code} {error.Message}");
}
```

A path that cannot be read gives the usual `FileNotFoundException`, `IOException` or `UnauthorizedAccessException`, with the path in it.

## Truncated results

Every result has the property `Truncated`. When it is `true`, the result does not hold everything: a limit cut a list, conflicts were not
computed, or the input was not read whole. Nothing else tells you, as .NET has no warnings for a library to give at run time, so look at it,
or ask for an exception with `EnsureComplete`:

```csharp
using System;
using Calendrum;

ExpandResult result = ICal.Expand(
    CalendarInput.FromFile("event.ics"),
    "2026-01-01T00:00:00Z",
    "2026-02-01T00:00:00Z",
    new ExpandOptions { Limit = 1 });
Console.WriteLine($"{result.Truncated} {result.Count}");

try
{
    result.EnsureComplete();
}
catch (TruncatedResultException error)
{
    Console.WriteLine(error.Message);
}
```

Do not replace a file with a result that lost something. The text of `Fix` holds what could be read, so look at it first:

```csharp
using System.IO;
using System.Linq;
using Calendrum;

FixResult result = ICal.Fix(CalendarInput.FromFile("event.ics"));
if (result.Changed && !result.Truncated && !result.Problems.Any())
{
    File.WriteAllText("event.ics", result.Ics);
}
```

## Direct access

`Call` runs an operation by its name, with the fields of the contract: a `JsonObject` goes in, and the `result` of the response comes out as a
`JsonObject`, next to `Truncated`. A calendar here is `ics` or `ics_base64`, and nothing reads a path. The contract is described in
`conformance/ops/README.md` in the repository.

```csharp
using System;
using System.Text.Json.Nodes;
using Calendrum;

CallResult result = ICal.Call("explain", new JsonObject { ["code"] = "ICAL-S003" });
Console.WriteLine($"{result.Result["severity"]} {result.Truncated}");
```

Reading a response into a tree of more than 85 KB goes through the large object heap, with a collection of every generation now and
then. For a loop over large answers, use the methods with classes.

## Threads and cancelling

Call any method from any thread, with no setup. 8 threads get the same answers as one. A call blocks its thread and holds no lock of the
CLR, so a garbage collection of the CLR does not wait for it. There are no `async` methods, as there is nothing to wait for but this
processor: to keep a server's threads free, use `Task.Run`.

```csharp
using System;
using System.Threading.Tasks;
using Calendrum;

ExpandResult result = await Task.Run(() => ICal.Expand(
    CalendarInput.FromFile("event.ics"),
    "2026-01-01T00:00:00Z",
    "2026-02-01T00:00:00Z"));
Console.WriteLine(result.Count);
```

**Keep to the thread pool, and do not start a thread for each call.** A thread that has called the library and ends leaves 300 to 450 KiB
behind until the next collection of the library, and short calls may not ask for one at all: 6000 threads with one call each reached 1.6 GiB, and the same calls on the thread pool stayed at 64 MiB.

**A call cannot be cancelled**, and `CancellationToken` could not stop it. To put a time limit on a call on a file from a stranger, run it in
another process and end that process, as under [Out of memory](#out-of-memory).

`Process.Start` is safe at any time: its child runs another program at once. Calling `fork()` yourself through interop is not: a child of
`fork` hangs in the library, at its first collection.

## Two collectors

A process with this package has two garbage collectors that do not know each other: the one of the CLR for your objects, and the one of the
library for the objects of a call. They do not wait for each other. Compacting collections of the CLR at full speed with 6 threads in calls
gave the same answers, with no hang, also with the server collector.

Memory with 8 threads in `Expand` on a year of 200 events, 96,000 calls, at the end of each of 6 steps:

| Collector of the CLR | RSS of the process | Managed heap |
|---|---|---|
| Workstation, the default | 67 to 91 MiB | 15 to 22 MiB |
| Server | 167 to 200 MiB | 35 to 121 MiB |
| Workstation with `GCHeapHardLimit` of 64 MiB | 67 to 75 MiB | 15 to 18 MiB |

Three things follow from it:

- **The CLR does not see the memory of the library.** `GC.GetTotalMemory` and `GCMemoryInfo` count only the managed part. Watch the RSS of the
  process, and leave room in the limit of a container.
- **`GCHeapHardLimit` does not bind the library.** It limits the managed heap only, and the memory of the library stays outside it.
- **Nothing here needs tuning.** The buffers of the library are released before a method returns, so `GC.AddMemoryPressure` has nothing to
  report. The server collector costs more memory, as it does anywhere, and gives no more speed in this loop.

The library starts two threads of its own at the first call, and they stay. An `AssemblyLoadContext` that is unloaded leaves them, and the
library itself: **a library that has loaded stays loaded until the process ends.**

## Out of memory

**When memory runs out inside the native library, the whole process ends with `SIGABRT`.** No `catch` can catch it, and
`OutOfMemoryException` does not come. A calendar of 58 million characters took 2.4 GiB at its peak, in a test that held it as a string. On a server that takes files from strangers, check the size before the call, or do such work
in a process of its own, where the loss of the process is the loss of one call. This is also the way to put a time limit on a call.

The worker is a small program of its own, which reads a calendar from its input:

```csharp
using System;
using System.IO;
using Calendrum;

using var calendar = new MemoryStream();
Console.OpenStandardInput().CopyTo(calendar);
Console.Write(ICal.Validate(calendar.ToArray()).Valid);
```

And the program that uses it gives it the bytes, and ends it when the time is up:

```csharp
using System;
using System.Diagnostics;
using System.IO;
using System.Threading.Tasks;

// The answer of the worker, or null if it took too long or died.
static string? RunWithLimit(string worker, byte[] calendar, TimeSpan limit)
{
    var start = new ProcessStartInfo(worker) { RedirectStandardInput = true, RedirectStandardOutput = true };
    using Process process = Process.Start(start)!;
    try
    {
        Task writing = Task.Run(() =>
        {
            process.StandardInput.BaseStream.Write(calendar);
            process.StandardInput.Close();
        });
        Task<string> answer = process.StandardOutput.ReadToEndAsync();
        if (!Task.WhenAll(writing, answer).Wait(limit))
        {
            return null;
        }
        process.WaitForExit();
        return process.ExitCode == 0 ? answer.Result : null;
    }
    catch (AggregateException)
    {
        return null;
    }
    finally
    {
        if (!process.HasExited)
        {
            process.Kill(entireProcessTree: true);
        }
    }
}

// Here cat stands in for the worker: put the path of yours.
Console.WriteLine(RunWithLimit("/bin/cat", File.ReadAllBytes("event.ics"), TimeSpan.FromSeconds(60)) is not null);
```

The stack is not a concern: all the cases of the contract and input that tries to go deep work on a thread of 256 KiB.

## Publishing

`dotnet publish` with a runtime identifier takes only the library of that platform. Without one, all four go along, under `runtimes/`.
`PublishSingleFile` leaves the library next to the program, as with any native file, unless you ask for
`IncludeNativeLibrariesForSelfExtract`, which unpacks it at the first start. All of these were run.

**Native AOT** works without any setting, and without a warning: a program with every operation came to 2.8 MB, next to the library of 5.6 MB.
The library cannot be linked into the program, so it has to stay next to it.

The package looks for the library in the folders the runtime knows for your application, in the folder of its own assembly and under
`runtimes/<rid>/native` there. A plugin in an `AssemblyLoadContext` of its own looks in its own folders first. A file of the same name in the
current folder or on the `PATH` is not picked up, in a plugin either. An application that keeps the library elsewhere calls
`NativeLibrary.SetDllImportResolver(typeof(ICal).Assembly, …)` before the first call, and the package then asks it.

## A calendar is data

A calendar comes from a stranger. Its `SUMMARY`, `DESCRIPTION` and even its names of properties and zones can say anything, including
sentences written for a program that reads them, such as "ignore your instructions". The comments of the results mark such fields as
**Untrusted**. Show them to a person or pass them to a model only as data. In a terminal, also remember that they can hold control
characters. Messages of errors and problems never hold such text.

## The cost

From the macOS library, on .NET 10, on a laptop that was also doing other work:

| | |
|---|---|
| The first call of a process | about 21 ms |
| Memory after it (RSS), and threads | 48 MiB, 10 |
| `Expand` of a year, 200 events, 2000 occurrences, after warming up | 6.3 ms |
| The same through `Call` | 6.2 ms |
| The same through the library alone | 5.4 ms |

The difference is JSON and the classes. A million occurrences do not fit a result: the largest list is 100,000.

## Limits

| Limit | Value |
|---|---|
| A request | 128 MiB of characters, nesting up to 64 |
| A calendar | 64 MiB, and the rest of the limits for files from strangers |
| A list | `Limit`, 1000 by default, 100,000 at most |
| A response | 16 MiB |

The slowest case is `Freebusy` on a file with about 100,000 rules: 10 seconds and 2.8 GiB of memory at its peak, and the result is truncated.
One event with a million occurrences takes less than a second. With a file from a stranger, use another process.

The package reads only the paths that you give it, and the library does not use the network or files, does not read the environment and
never uses the time zone of the machine.

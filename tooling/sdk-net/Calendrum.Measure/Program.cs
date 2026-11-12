using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text.Json.Nodes;
using System.Threading;
using Calendrum;

// Measures the package. Usage: Calendrum.Measure <feed.ics>, where the feed comes from sdk/c/bench/make_feed.py. Each mode is a process
// of its own, started by hand or by the script around it, so the numbers of one do not disturb another.
string mode = args[0];
string feed = File.ReadAllText(args[1]);
const string From = "2026-01-01T00:00:00Z";
const string To = "2027-01-01T00:00:00Z";
var window = new ExpandOptions { Zone = "Europe/Berlin", Limit = 100000 };

static double Mib(long bytes) => bytes / (1024.0 * 1024.0);

static double Resident() => Mib(Process.GetCurrentProcess().WorkingSet64);

void Breakdown()
{
    var writer = new Calendrum.Internal.RequestWriter();
    writer.Calendar("ics", feed).Text("from", From).Text("to", To).Text("zone", window.Zone).Number("limit", window.Limit);
    byte[] request = writer.Finish().ToArray();
    writer.Dispose();
    byte[] operation = "expand\0"u8.ToArray();
    byte[] response;
    using (Calendrum.Internal.NativeBuffer buffer = Calendrum.Internal.Native.Call(operation, request))
    {
        response = buffer.Span.ToArray();
    }
    Console.WriteLine($"request {request.Length} B, response {response.Length} B");
    double Timed(string name, Action action)
    {
        for (int warm = 0; warm < 3000; warm++)
        {
            action();
            if (name == "library")
            {
                break;
            }
        }
        Thread.Sleep(300);
        int gen0 = GC.CollectionCount(0), gen2 = GC.CollectionCount(2);
        long allocated = GC.GetAllocatedBytesForCurrentThread();
        var clock = Stopwatch.StartNew();
        const int Rounds = 300;
        for (int round = 0; round < Rounds; round++)
        {
            action();
        }
        double each = clock.Elapsed.TotalMilliseconds / Rounds;
        Console.WriteLine($"{name}: {each:F3} ms, allocated {Mib(GC.GetAllocatedBytesForCurrentThread() - allocated) / Rounds * 1024:F0} KiB, gen0 {GC.CollectionCount(0) - gen0}, gen2 {GC.CollectionCount(2) - gen2}");
        return each;
    }
    Timed("library", () =>
    {
        using Calendrum.Internal.NativeBuffer buffer = Calendrum.Internal.Native.Call(operation, request);
    });
    Timed("write the request", () =>
    {
        using var w = new Calendrum.Internal.RequestWriter();
        w.Calendar("ics", feed).Text("from", From).Text("to", To).Text("zone", window.Zone).Number("limit", window.Limit);
        _ = w.Finish();
    });
    Timed("read the response into classes", () => _ = ICal.Decode(response, Calendrum.Internal.CalendrumJson.Default.EnvelopeExpandResult));
    Timed("read the response into a tree", () => _ = ICal.DecodeCall(response));
    Timed("Expand, whole", () => _ = ICal.Expand(feed, From, To, window));
}

void Calls()
{
    var request = new JsonObject { ["ics"] = feed, ["from"] = From, ["to"] = To, ["zone"] = "Europe/Berlin", ["limit"] = 100000 };
    int count = ICal.Expand(feed, From, To, window).Count;
    for (int warm = 0; warm < 300; warm++)
    {
        _ = ICal.Expand(feed, From, To, window);
    }
    var clock = Stopwatch.StartNew();
    for (int round = 0; round < 200; round++)
    {
        _ = ICal.Expand(feed, From, To, window);
    }
    double typed = clock.Elapsed.TotalMilliseconds / 200;
    clock.Restart();
    for (int round = 0; round < 200; round++)
    {
        _ = ICal.Call("expand", (JsonObject)request.DeepClone());
    }
    double call = clock.Elapsed.TotalMilliseconds / 200;
    Console.WriteLine($"occurrences {count}; Expand with the classes {typed:F2} ms, Call with a tree {call:F2} ms; RSS {Resident():F1} MiB");
}

void First()
{
    var clock = Stopwatch.StartNew();
    _ = ICal.Explain("ICAL-S003");
    double first = clock.Elapsed.TotalMilliseconds;
    clock.Restart();
    _ = ICal.Explain("ICAL-S003");
    Console.WriteLine($"first answer {first:F2} ms, second {clock.Elapsed.TotalMilliseconds:F3} ms; RSS {Resident():F1} MiB; threads {Process.GetCurrentProcess().Threads.Count}");
}

void Memory()
{
    int threads = args.Length > 2 ? int.Parse(args[2], System.Globalization.CultureInfo.InvariantCulture) : 1;
    int calls = args.Length > 3 ? int.Parse(args[3], System.Globalization.CultureInfo.InvariantCulture) : 20000;
    Console.WriteLine($"server GC {System.Runtime.GCSettings.IsServerGC}; {threads} threads; {calls} calls in each step");
    double start = Resident();
    long peak = 0;
    for (int step = 1; step <= 6; step++)
    {
        var clock = Stopwatch.StartNew();
        var workers = Enumerable.Range(0, threads).Select(index => new Thread(() =>
        {
            for (int round = 0; round < calls / threads; round++)
            {
                _ = ICal.Expand(feed, From, To, window);
            }
        })).ToArray();
        foreach (Thread worker in workers)
        {
            worker.Start();
        }
        foreach (Thread worker in workers)
        {
            worker.Join();
        }
        GCMemoryInfo info = GC.GetGCMemoryInfo();
        peak = Math.Max(peak, Process.GetCurrentProcess().WorkingSet64);
        Console.WriteLine($"step {step}: RSS {Resident():F0} MiB (start {start:F0}), managed heap {Mib(GC.GetTotalMemory(false)):F0} MiB, committed {Mib(info.TotalCommittedBytes):F0} MiB, gen0 {GC.CollectionCount(0)} gen2 {GC.CollectionCount(2)}, pause {GC.GetTotalPauseDuration().TotalMilliseconds:F0} ms, {clock.Elapsed.TotalSeconds:F1} s");
    }
    Console.WriteLine($"largest RSS at the end of a step {Mib(peak):F0} MiB, limit of the CLR {Mib(GC.GetGCMemoryInfo().TotalAvailableMemoryBytes):F0} MiB");
}

void ThreadPerCall()
{
    int total = int.Parse(args[2], System.Globalization.CultureInfo.InvariantCulture);
    Console.WriteLine($"RSS at the start {Resident():F0} MiB");
    var clock = Stopwatch.StartNew();
    for (int index = 1; index <= total; index++)
    {
        var thread = new Thread(() => _ = ICal.Explain("ICAL-S003"));
        thread.Start();
        thread.Join();
        if (index % (total / 4) == 0)
        {
            Console.WriteLine($"{index} threads, one call each: RSS {Resident():F0} MiB, {clock.Elapsed.TotalSeconds:F1} s");
        }
    }
    clock.Restart();
    var pool = new System.Threading.Tasks.Task[total];
    for (int index = 0; index < total; index++)
    {
        pool[index] = System.Threading.Tasks.Task.Run(() => _ = ICal.Explain("ICAL-S003"));
    }
    System.Threading.Tasks.Task.WaitAll(pool);
    Console.WriteLine($"the same on the thread pool: RSS {Resident():F0} MiB, {clock.Elapsed.TotalSeconds:F1} s");
}

void Large()
{
    // One large answer through both collectors: the managed heap holds the text and the classes, and the runtime of the library its own.
    int copies = args.Length > 2 ? int.Parse(args[2], System.Globalization.CultureInfo.InvariantCulture) : 100;
    var big = string.Concat(Enumerable.Repeat(feed.Replace("END:VCALENDAR\r\n", "", StringComparison.Ordinal).Replace("BEGIN:VCALENDAR\r\n", "", StringComparison.Ordinal), copies));
    string whole = "BEGIN:VCALENDAR\r\n" + big + "END:VCALENDAR\r\n";
    Console.WriteLine($"calendar {Mib(whole.Length):F0} MiB of characters, RSS {Resident():F0} MiB");
    var clock = Stopwatch.StartNew();
    int count = ICal.Expand(whole, From, To, window).Count;
    Console.WriteLine($"{count} occurrences, {clock.Elapsed.TotalSeconds:F1} s, RSS {Resident():F0} MiB, managed {Mib(GC.GetTotalMemory(false)):F0} MiB");
}

switch (mode)
{
    case "first":
        First();
        break;
    case "calls":
        Calls();
        break;
    case "breakdown":
        Breakdown();
        break;
    case "memory":
        Memory();
        break;
    case "large":
        Large();
        break;
    case "threadpercall":
        ThreadPerCall();
        break;
    default:
        Console.WriteLine("modes: first, calls, memory [threads] [calls], large");
        break;
}

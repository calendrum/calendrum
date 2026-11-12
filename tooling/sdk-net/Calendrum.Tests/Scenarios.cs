using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.Loader;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Threading;

namespace Calendrum.Tests;

/// <summary>
/// What runs in a child process, as it can hang, crash or end the process. Each is chosen by name, and only the ones whose name says so
/// start a further process, which is either a system tool or this program with another scenario that starts nothing.
/// </summary>
internal static class Scenarios
{
    internal static int Run(string name, string[] arguments)
    {
        switch (name)
        {
            case "fifo":
                return Fifo(arguments[0]);
            case "nothing_before_the_first_call":
                return NothingBeforeTheFirstCall();
            case "first_call_race":
                return FirstCallRace();
            case "exit_with_threads_in_calls":
                return WithThreadsInCalls(exit: true);
            case "return_with_threads_in_calls":
                return WithThreadsInCalls(exit: false);
            case "start_processes_after_calls":
                return StartProcessesAfterCalls();
            case "two_collectors":
                return TwoCollectors();
            case "memory_plateau":
                return MemoryPlateau();
            case "small_stack":
                return OnSmallStack(AllCases);
            case "deep_input":
                return OnSmallStack(DeepInput);
            case "unload_context":
                return UnloadContext();
            case "copy_without_library":
                return CopyWithout();
            case "copy_with_garbage_library":
                return CopyWithGarbage();
            case "copy_with_stub_library":
                return CopyWithStub(arguments[0]);
            case "planted_in_current_folder":
                return PlantedInCurrentFolder();
            case "plugin_with_runtimes_folder":
                return PluginWithRuntimesFolder();
            case "plugin_without_library":
                return PluginWithoutLibrary();
            case "plugin_child":
                return PluginChild(arguments[0], arguments[1]);
            case "own_resolver":
                return OwnResolver();
            case "expect_library_error":
                return ExpectLibraryError(arguments[0]);
            case "answer_once":
                _ = ICal.Explain("ICAL-S003");
                return 0;
            default:
                Console.WriteLine($"No scenario {name}");
                return 2;
        }
    }

    private static int Fail(string text)
    {
        Console.WriteLine(text);
        return 1;
    }

    private static int Fifo(string directory)
    {
        string path = Path.Combine(directory, "pipe.ics");
        using (Process maker = Process.Start("/usr/bin/mkfifo", [path]))
        {
            maker.WaitForExit();
            if (maker.ExitCode != 0)
            {
                return Fail("mkfifo failed");
            }
        }
        string link = Path.Combine(directory, "link.ics");
        File.CreateSymbolicLink(link, path);
        foreach (string target in new[] { path, link })
        {
            try
            {
                CalendarInput.FromFile(target);
                return Fail($"{target} was read");
            }
            catch (BadArgumentException)
            {
            }
        }
        return 0;
    }

    private static bool LoadedByDyld()
    {
        // NativeLibrary would load it; a handle from RTLD_NOLOAD says only whether it is there.
        const int RtldNoLoad = 0x10;
        const int RtldLazy = 1;
        IntPtr handle = dlopen(Path.Combine(AppContext.BaseDirectory, "libcalendrum.dylib"), RtldNoLoad | RtldLazy);
        return handle != IntPtr.Zero;
    }

    [System.Runtime.InteropServices.DllImport("libSystem.dylib", CharSet = System.Runtime.InteropServices.CharSet.Ansi)]
    private static extern IntPtr dlopen(string path, int flags);

    private static int NothingBeforeTheFirstCall()
    {
        // Types, an input and a refused argument: none of it reaches the library.
        CalendarInput input = Support.Ics;
        _ = new ExpandOptions { Limit = 1 };
        _ = typeof(ICal).GetMethods();
        try
        {
            ICal.Inspect((CalendarInput)null!);
        }
        catch (ArgumentNullException)
        {
        }
        if (LoadedByDyld())
        {
            return Fail("loaded before the first call");
        }
        _ = ICal.Inspect(input);
        return LoadedByDyld() ? 0 : Fail("not loaded by the first call, so this check says nothing");
    }

    private static int FirstCallRace()
    {
        using var start = new Barrel(16);
        var answers = new string[16];
        Thread[] threads = Enumerable.Range(0, 16).Select(index => new Thread(() =>
        {
            start.Wait();
            answers[index] = ICal.GetVersions().Library + ICal.Explain("ICAL-S003").Code;
        })).ToArray();
        foreach (Thread thread in threads)
        {
            thread.Start();
        }
        foreach (Thread thread in threads)
        {
            thread.Join();
        }
        return answers.Distinct().Count() == 1 ? 0 : Fail("the answers differ");
    }

    private sealed class Barrel(int count) : IDisposable
    {
        private readonly Barrier _barrier = new(count);

        internal void Wait() => _barrier.SignalAndWait();

        public void Dispose() => _barrier.Dispose();
    }

    private static string Feed(int events)
    {
        var text = new System.Text.StringBuilder("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//test//EN\r\n");
        for (int index = 0; index < events; index++)
        {
            text.Append("BEGIN:VEVENT\r\nUID:").Append(index).Append("@example.com\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nDTEND:20260105T110000Z\r\nSUMMARY:Lecture ").Append(index).Append("\r\nRRULE:FREQ=WEEKLY;COUNT=20\r\nEND:VEVENT\r\n");
        }
        return text.Append("END:VCALENDAR\r\n").ToString();
    }

    private static int WithThreadsInCalls(bool exit)
    {
        string feed = Feed(2000);
        for (int index = 0; index < 8; index++)
        {
            new Thread(() =>
            {
                while (true)
                {
                    _ = ICal.Expand(feed, Support.From, "2027-01-01T00:00:00Z", new ExpandOptions { Limit = 100000 });
                }
            })
            { IsBackground = true }.Start();
        }
        Thread.Sleep(1500);
        if (exit)
        {
            Environment.Exit(0);
        }
        return 0;
    }

    private static int StartProcessesAfterCalls()
    {
        _ = ICal.Expand(Support.Ics, Support.From, Support.To);
        string self = Environment.ProcessPath!;
        for (int round = 0; round < 5; round++)
        {
            using Process tool = Process.Start("/usr/bin/true")!;
            if (!tool.WaitForExit(20000) || tool.ExitCode != 0)
            {
                return Fail("a system tool did not end");
            }
        }
        // This program again, as a process that calls the library itself. What it does is the plain scenario, which starts nothing.
        using Process child = Process.Start(self, ["--scenario", "answer_once"]);
        if (!child.WaitForExit(30000))
        {
            child.Kill(entireProcessTree: true);
            return Fail("the child did not end");
        }
        _ = ICal.Explain("ICAL-S003");
        return child.ExitCode == 0 ? 0 : Fail($"the child ended with {child.ExitCode}");
    }

    private static int TwoCollectors()
    {
        string feed = Feed(200);
        string expected = ICal.Expand(feed, Support.From, "2027-01-01T00:00:00Z", new ExpandOptions { Limit = 100000 }).Count.ToString(System.Globalization.CultureInfo.InvariantCulture);
        var stop = new ManualResetEventSlim();
        var failures = 0;
        var calls = 0;
        Thread[] callers = Enumerable.Range(0, 6).Select(_ => new Thread(() =>
        {
            while (!stop.IsSet)
            {
                ExpandResult result = ICal.Expand(feed, Support.From, "2027-01-01T00:00:00Z", new ExpandOptions { Limit = 100000 });
                if (result.Count.ToString(System.Globalization.CultureInfo.InvariantCulture) != expected)
                {
                    Interlocked.Increment(ref failures);
                }
                Interlocked.Increment(ref calls);
            }
        })).ToArray();
        // Collections of the CLR at full speed, with compaction, while the callers are inside the library and its own collector runs.
        var collector = new Thread(() =>
        {
            while (!stop.IsSet)
            {
                GC.Collect(2, GCCollectionMode.Forced, blocking: true, compacting: true);
                _ = new byte[1024 * 1024];
            }
        });
        foreach (Thread thread in callers)
        {
            thread.Start();
        }
        collector.Start();
        Thread.Sleep(10000);
        stop.Set();
        foreach (Thread thread in callers)
        {
            thread.Join();
        }
        collector.Join();
        Console.WriteLine($"{calls} calls, {GC.CollectionCount(2)} collections of generation 2");
        return failures == 0 && calls > 0 && GC.CollectionCount(2) > 10 ? 0 : Fail($"failures {failures}, calls {calls}");
    }

    private static double ResidentMiB() => Process.GetCurrentProcess().WorkingSet64 / (1024.0 * 1024.0);

    private static int MemoryPlateau()
    {
        string feed = Feed(20);
        void Round(int count)
        {
            for (int index = 0; index < count; index++)
            {
                _ = ICal.Expand(feed, Support.From, "2027-01-01T00:00:00Z", new ExpandOptions { Limit = 100000 });
            }
        }
        Round(5000);
        double first = ResidentMiB();
        Round(15000);
        double second = ResidentMiB();
        Round(15000);
        double third = ResidentMiB();
        Console.WriteLine($"RSS: {first:F1}, {second:F1}, {third:F1} MiB");
        // Room for the collectors to breathe, but not for a leak: 15000 calls that leak a response each would add far more than this.
        return third < first + 60 && third < second + 40 ? 0 : Fail($"RSS {first:F1}, {second:F1}, {third:F1} MiB");
    }

    private static int OnSmallStack(Func<int> work)
    {
        int code = 3;
        var thread = new Thread(() => code = work(), 256 * 1024);
        thread.Start();
        thread.Join();
        return code;
    }

    private static int AllCases()
    {
        foreach (ContractCase contractCase in Support.AllCases())
        {
            try
            {
                _ = ICal.Call(contractCase.Operation, contractCase.Request);
            }
            catch (CalendrumException)
            {
            }
        }
        return 0;
    }

    private static int DeepInput()
    {
        string begins = string.Concat(Enumerable.Repeat("BEGIN:X\r\n", 2_000_000));
        InspectResult deep = ICal.Inspect("BEGIN:VCALENDAR\r\n" + begins);
        if (deep.ProblemCount == 0)
        {
            return Fail("deep input gave no problem");
        }
        var nested = new System.Text.StringBuilder();
        for (int level = 0; level < 60; level++)
        {
            nested.Append('[');
        }
        nested.Append(new string(']', 60));
        try
        {
            _ = ICal.Call("convert", new JsonObject { ["jcal"] = JsonNode.Parse(nested.ToString(), documentOptions: new JsonDocumentOptions { MaxDepth = 256 }) });
        }
        catch (BadArgumentException)
        {
        }
        return 0;
    }

    private static int UnloadContext()
    {
        string path = typeof(ICal).Assembly.Location;
        for (int round = 0; round < 3; round++)
        {
            var context = new AssemblyLoadContext($"round {round}", isCollectible: true);
            Assembly assembly = context.LoadFromAssemblyPath(path);
            object? answer = assembly.GetType("Calendrum.ICal")!.GetMethod("GetVersions")!.Invoke(null, null);
            if (answer is null)
            {
                return Fail("no answer");
            }
            context.Unload();
            GC.Collect();
            GC.WaitForPendingFinalizers();
        }
        _ = ICal.Explain("ICAL-S003");
        return 0;
    }

    private static string CopyProgram(string target)
    {
        Directory.CreateDirectory(target);
        foreach (string file in Directory.GetFiles(AppContext.BaseDirectory))
        {
            if (Path.GetFileName(file) != "libcalendrum.dylib")
            {
                File.Copy(file, Path.Combine(target, Path.GetFileName(file)));
            }
        }
        return Path.Combine(target, Path.GetFileName(Environment.ProcessPath!));
    }

    private static int RunCopy(string program, string scenario, string? currentDirectory = null, string expected = "did not load", string? extra = null)
    {
        string[] arguments = extra is null ? ["--scenario", scenario, expected] : ["--scenario", scenario, expected, extra];
        var start = new ProcessStartInfo(program, arguments) { RedirectStandardOutput = true, RedirectStandardError = true };
        if (currentDirectory is not null)
        {
            start.WorkingDirectory = currentDirectory;
        }
        using Process process = Process.Start(start)!;
        var errors = process.StandardError.ReadToEndAsync();
        string output = process.StandardOutput.ReadToEnd();
        if (!process.WaitForExit(40000))
        {
            process.Kill(entireProcessTree: true);
            return Fail("the copy did not end");
        }
        Console.WriteLine(output + errors.Result);
        return process.ExitCode;
    }

    private static int CopyWithout()
    {
        using var folder = new Support.TemporaryFolder();
        return RunCopy(CopyProgram(Path.Combine(folder.Path, "copy")), "expect_library_error");
    }

    private static int CopyWithGarbage()
    {
        using var folder = new Support.TemporaryFolder();
        string program = CopyProgram(Path.Combine(folder.Path, "copy"));
        File.WriteAllBytes(Path.Combine(Path.GetDirectoryName(program)!, "libcalendrum.dylib"), new byte[4096]);
        return RunCopy(program, "expect_library_error");
    }

    private static int CopyWithStub(string kind)
    {
        using var folder = new Support.TemporaryFolder();
        string program = CopyProgram(Path.Combine(folder.Path, "copy"));
        string source = Path.Combine(folder.Path, "stub.c");
        // A stub of another ABI has all four functions and answers well, so that only the check of the ABI can refuse it.
        File.WriteAllText(source, kind == "abi"
            ? """
              #include <stddef.h>
              #include <stdint.h>
              static uint8_t answer[] = "{\"ok\":true,\"result\":{\"code\":\"X\",\"severity\":\"error\",\"constant\":\"Y\",\"meaning\":\"Z\"}}";
              static uint8_t versions[] = "{\"abi\":2,\"contract\":1,\"library\":\"x\",\"sdk\":\"y\"}";
              int32_t calendrum_abi_version(void) { return 2; }
              int32_t calendrum_version(uint8_t **out, size_t *length) { *out = versions; *length = sizeof(versions) - 1; return 0; }
              int32_t calendrum_call(const char *name, const uint8_t *request, size_t size, uint8_t **out, size_t *length) { *out = answer; *length = sizeof(answer) - 1; return 0; }
              void calendrum_free(uint8_t *data) {}
              """
            : "int calendrum_abi_version(void) { return 1; }\n");
        using Process compiler = Process.Start("/usr/bin/cc", ["-shared", "-o", Path.Combine(Path.GetDirectoryName(program)!, "libcalendrum.dylib"), source]);
        compiler.WaitForExit();
        return compiler.ExitCode != 0 ? Fail("cc failed") : RunCopy(program, "expect_library_error", null, kind == "abi" ? "has ABI 2" : "did not load");
    }

    private static int PlantedInCurrentFolder()
    {
        using var folder = new Support.TemporaryFolder();
        string program = CopyProgram(Path.Combine(folder.Path, "copy"));
        string current = Path.Combine(folder.Path, "current");
        Directory.CreateDirectory(current);
        // The real file, so that a search that looks in the current folder would succeed and fail the test by answering.
        File.Copy(Path.Combine(AppContext.BaseDirectory, "libcalendrum.dylib"), Path.Combine(current, "libcalendrum.dylib"));
        return RunCopy(program, "expect_library_error", current);
    }

    // A plugin in a load context of its own, built without a runtime identifier: the library is under runtimes/<rid>/native, and the
    // program runs from a copy without it. The current folder holds garbage of the same name, which fails the load if the search reaches it.
    private static int PluginWithRuntimesFolder()
    {
        using var folder = new Support.TemporaryFolder();
        string program = CopyProgram(Path.Combine(folder.Path, "copy"));
        string plugin = Path.Combine(folder.Path, "plugin");
        string native = Path.Combine(plugin, "runtimes", "osx-arm64", "native");
        Directory.CreateDirectory(native);
        File.Copy(typeof(ICal).Assembly.Location, Path.Combine(plugin, "Calendrum.dll"));
        File.Copy(Path.Combine(AppContext.BaseDirectory, "libcalendrum.dylib"), Path.Combine(native, "libcalendrum.dylib"));
        string current = Path.Combine(folder.Path, "current");
        Directory.CreateDirectory(current);
        File.WriteAllBytes(Path.Combine(current, "libcalendrum.dylib"), new byte[4096]);
        return RunCopy(program, "plugin_child", current, "answers", plugin);
    }

    // A plugin without the library, and the real one in the current folder: an answer means the search found the planted file. The
    // program runs from a copy without the library, so that its own folder does not have it either.
    private static int PluginWithoutLibrary()
    {
        using var folder = new Support.TemporaryFolder();
        string program = CopyProgram(Path.Combine(folder.Path, "copy"));
        string plugin = Path.Combine(folder.Path, "plugin");
        Directory.CreateDirectory(plugin);
        File.Copy(typeof(ICal).Assembly.Location, Path.Combine(plugin, "Calendrum.dll"));
        string current = Path.Combine(folder.Path, "current");
        Directory.CreateDirectory(current);
        File.Copy(Path.Combine(AppContext.BaseDirectory, "libcalendrum.dylib"), Path.Combine(current, "libcalendrum.dylib"));
        return RunCopy(program, "plugin_child", current, "did not load", plugin);
    }

    // Starts nothing: loads the package from the folder into a new load context and asks it for the versions.
    private static int PluginChild(string expected, string plugin)
    {
        var context = new AssemblyLoadContext("plugin", isCollectible: false);
        Assembly assembly = context.LoadFromAssemblyPath(Path.Combine(plugin, "Calendrum.dll"));
        if (AssemblyLoadContext.GetLoadContext(assembly) != context)
        {
            return Fail("the package did not load into the plugin context");
        }
        try
        {
            object? versions = assembly.GetType("Calendrum.ICal")!.GetMethod("GetVersions")!.Invoke(null, null);
            return expected == "answers" && versions is not null ? 0 : Fail("it answered");
        }
        catch (TargetInvocationException error) when (error.InnerException is { } inner && inner.GetType().Name == "LibraryException")
        {
            return expected != "answers" && inner.Message.Contains(expected, StringComparison.Ordinal) ? 0 : Fail($"{inner.GetType().Name}: {inner.Message}");
        }
    }

    private static int OwnResolver()
    {
        var asked = 0;
        System.Runtime.InteropServices.NativeLibrary.SetDllImportResolver(typeof(ICal).Assembly, (name, assembly, searchPath) =>
        {
            Interlocked.Increment(ref asked);
            return System.Runtime.InteropServices.NativeLibrary.Load(Path.Combine(AppContext.BaseDirectory, "libcalendrum.dylib"));
        });
        _ = ICal.Explain("ICAL-S003");
        return asked > 0 ? 0 : Fail("the resolver of the application was not asked");
    }

    private static int ExpectLibraryError(string expected)
    {
        foreach (Action action in new Action[] { () => ICal.Explain("ICAL-S003"), () => ICal.GetVersions(), () => ICal.Call("explain", new JsonObject { ["code"] = "ICAL-S003" }), () => ICal.Inspect(Support.Ics) })
        {
            try
            {
                action();
                return Fail("it answered");
            }
            catch (LibraryException error)
            {
                if (!error.Message.Contains(expected, StringComparison.Ordinal))
                {
                    return Fail($"Not what was expected: {error.Message}");
                }
            }
        }
        return 0;
    }
}

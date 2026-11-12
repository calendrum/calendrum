using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading;

namespace Calendrum.Tests;

/// <summary>The library inside a CLR: threads, two collectors, the end of a process, and what happens when it cannot load.</summary>
internal static class ProcessTests
{
    [Test]
    private static void EightThreadsGetTheSameAnswersAsOne()
    {
        string expected = JsonSerializer.Serialize(ICal.Expand(Support.Ics, Support.From, Support.To), Internal.CalendrumJson.Default.ExpandResult);
        var failures = new List<string>();
        Thread[] threads = Enumerable.Range(0, 8).Select(_ => new Thread(() =>
        {
            for (int round = 0; round < 1000; round++)
            {
                string answer = JsonSerializer.Serialize(ICal.Expand(Support.Ics, Support.From, Support.To), Internal.CalendrumJson.Default.ExpandResult);
                if (answer != expected)
                {
                    lock (failures)
                    {
                        failures.Add(answer);
                    }
                }
            }
        })).ToArray();
        foreach (Thread thread in threads)
        {
            thread.Start();
        }
        foreach (Thread thread in threads)
        {
            thread.Join();
        }
        Check.Equal(0, failures.Count, "answers that differ:");
    }

    [Test]
    private static void TheFirstCallLoadsTheLibraryAndNothingBeforeIt() => Support.RunOk("nothing_before_the_first_call");

    [Test]
    private static void ThreadsRaceToTheFirstCall() => Support.RunOk("first_call_race");

    [Test]
    private static void TheProcessEndsWithThreadsInCalls() => Support.RunOk("exit_with_threads_in_calls", limitSeconds: 60);

    [Test]
    private static void TheProcessReturnsFromMainWithThreadsInCalls() => Support.RunOk("return_with_threads_in_calls", limitSeconds: 60);

    [Test]
    private static void ChildProcessesAfterTheLibraryWorks() => Support.RunOk("start_processes_after_calls", limitSeconds: 60);

    [Test]
    private static void TwoCollectorsAtWork() => Support.RunOk("two_collectors", limitSeconds: 120);

    [Test]
    private static void TwoCollectorsAtWorkWithTheServerCollector() =>
        Support.RunOk("two_collectors", limitSeconds: 120, variables: new() { ["DOTNET_gcServer"] = "1", ["DOTNET_GCHeapCount"] = "4" });

    [Test]
    private static void MemoryReachesAPlateau() => Support.RunOk("memory_plateau", limitSeconds: 300);

    [Test]
    private static void TheContractOnASmallStack() => Support.RunOk("small_stack", limitSeconds: 120);

    [Test]
    private static void InputThatTriesToGoDeepOnASmallStack() => Support.RunOk("deep_input", limitSeconds: 120);

    [Test]
    private static void AnAssemblyContextThatIsUnloaded() => Support.RunOk("unload_context", limitSeconds: 60);

    [Test]
    private static void WithoutTheLibrary() => Support.RunOk("copy_without_library", limitSeconds: 60);

    [Test]
    private static void ALibraryThatDoesNotLoad() => Support.RunOk("copy_with_garbage_library", limitSeconds: 60);

    [Test]
    private static void ALibraryOfAnotherAbi() => Support.RunOk("copy_with_stub_library", ["abi"], limitSeconds: 60);

    [Test]
    private static void ALibraryWithoutItsFunctions() => Support.RunOk("copy_with_stub_library", ["functions"], limitSeconds: 60);

    [Test]
    private static void AnApplicationThatSetsItsOwnResolver() => Support.RunOk("own_resolver", limitSeconds: 60);

    [Test]
    private static void AFileOfTheSameNameInTheCurrentFolderIsNotPickedUp() => Support.RunOk("planted_in_current_folder", limitSeconds: 60);

    [Test]
    private static void APluginFindsTheLibraryUnderItsRuntimesFolder() => Support.RunOk("plugin_with_runtimes_folder", limitSeconds: 60);

    [Test]
    private static void APluginWithoutTheLibraryDoesNotTakeOneFromTheCurrentFolder() => Support.RunOk("plugin_without_library", limitSeconds: 60);
}

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;

namespace Calendrum.Tests;

/// <summary>A test: a public static method without arguments. The base library has no framework, and nothing else may come in.</summary>
[AttributeUsage(AttributeTargets.Method)]
internal sealed class TestAttribute : Attribute
{
}

internal sealed class AssertionFailed(string message) : Exception(message);

internal static class Check
{
    internal static void True(bool condition, string text = "expected true")
    {
        if (!condition)
        {
            throw new AssertionFailed(text);
        }
    }

    internal static void False(bool condition, string text = "expected false") => True(!condition, text);

    internal static void Equal<T>(T expected, T actual, string text = "")
    {
        if (!EqualityComparer<T>.Default.Equals(expected, actual))
        {
            throw new AssertionFailed($"{text} expected <{expected}>, got <{actual}>");
        }
    }

    internal static void Contains(string part, string text, string note = "")
    {
        if (!text.Contains(part, StringComparison.Ordinal))
        {
            throw new AssertionFailed($"{note} <{part}> is not in <{Short(text)}>");
        }
    }

    internal static void NotContains(string part, string text, string note = "")
    {
        if (text.Contains(part, StringComparison.Ordinal))
        {
            throw new AssertionFailed($"{note} <{part}> is in <{Short(text)}>");
        }
    }

    internal static T Throws<T>(Action action, string text = "")
        where T : Exception
    {
        try
        {
            action();
        }
        catch (Exception error)
        {
            if (error.GetType() == typeof(T))
            {
                return (T)error;
            }
            throw new AssertionFailed($"{text} expected {typeof(T).Name}, got {error.GetType().Name}: {Short(error.Message)}");
        }
        throw new AssertionFailed($"{text} expected {typeof(T).Name}, nothing was thrown");
    }

    private static string Short(string text) => text.Length <= 400 ? text : text[..400] + "...";
}

internal static class Runner
{
    internal static int RunAll(IReadOnlyCollection<string> only)
    {
        List<MethodInfo> tests = typeof(Runner).Assembly.GetTypes()
            .SelectMany(type => type.GetMethods(BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic))
            .Where(method => method.GetCustomAttribute<TestAttribute>() is not null)
            .Where(method => only.Count == 0 || only.Any(name => $"{method.DeclaringType!.Name}.{method.Name}".Contains(name, StringComparison.Ordinal)))
            .OrderBy(method => method.DeclaringType!.Name, StringComparer.Ordinal)
            .ThenBy(method => method.Name, StringComparer.Ordinal)
            .ToList();
        int failed = 0;
        var clock = Stopwatch.StartNew();
        foreach (MethodInfo test in tests)
        {
            string name = $"{test.DeclaringType!.Name}.{test.Name}";
            var one = Stopwatch.StartNew();
            try
            {
                test.Invoke(null, null);
                Console.WriteLine($"ok   {name} ({one.ElapsedMilliseconds} ms)");
            }
            catch (TargetInvocationException error)
            {
                failed++;
                Console.WriteLine($"FAIL {name}\n     {error.InnerException!.GetType().Name}: {error.InnerException.Message}\n{error.InnerException.StackTrace}");
            }
        }
        Console.WriteLine($"{tests.Count} tests, {failed} failed, {clock.Elapsed.TotalSeconds:F1} s");
        if (tests.Count == 0)
        {
            Console.WriteLine("Nothing ran.");
            return 1;
        }
        return failed == 0 ? 0 : 1;
    }
}

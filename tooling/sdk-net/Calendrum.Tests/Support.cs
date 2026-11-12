using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Text.Json.Nodes;

namespace Calendrum.Tests;

internal sealed record ContractCase(string Id, string Operation, JsonObject Request, JsonObject Expected, HashSet<string> Ignored);

internal static class Support
{
    internal const string Ics =
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//test//EN\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n"
        + "DTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nDTEND:20260105T110000Z\r\nSUMMARY:Plain\r\n"
        + "RRULE:FREQ=DAILY;COUNT=3\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n";

    internal const string From = "2026-01-01T00:00:00Z";
    internal const string To = "2026-02-01T00:00:00Z";

    internal static readonly string[] Operations =
    [
        "inspect", "validate", "fix", "expand", "rrule_expand", "freebusy", "build_event", "diff", "convert", "anonymize", "tz_resolve",
        "explain",
    ];

    internal static string CasesDirectory => Variable("CALENDRUM_CASES");

    internal static string SchemasDirectory => Path.Combine(CasesDirectory, "schema");

    private static string Variable(string name) =>
        Environment.GetEnvironmentVariable(name) ?? throw new InvalidOperationException($"The variable {name} is not set");

    internal static JsonObject LoadObject(string path) =>
        JsonNode.Parse(File.ReadAllText(path), documentOptions: new JsonDocumentOptions { MaxDepth = 256 })!.AsObject();

    private static List<ContractCase>? s_cases;

    internal static IReadOnlyList<ContractCase> AllCases()
    {
        if (s_cases is not null)
        {
            return s_cases;
        }
        var found = new List<ContractCase>();
        foreach (string directory in Directory.GetDirectories(CasesDirectory).OrderBy(name => name, StringComparer.Ordinal))
        {
            string request = Path.Combine(directory, "request.json");
            if (!File.Exists(request))
            {
                continue;
            }
            JsonObject meta = LoadObject(Path.Combine(directory, "meta.json"));
            var ignored = new HashSet<string>();
            if (meta["ignore_fields"] is JsonArray fields)
            {
                ignored.UnionWith(fields.Select(field => field!.GetValue<string>()));
            }
            found.Add(new ContractCase(
                Path.GetFileName(directory),
                meta["operation"]!.GetValue<string>(),
                LoadObject(request),
                LoadObject(Path.Combine(directory, "expected.json")),
                ignored));
        }
        return s_cases = found;
    }

    /// <summary>The rules of conformance/ops/README.md: messages and the fields a case lists do not count.</summary>
    internal static JsonNode? Comparable(JsonNode? value, HashSet<string> ignored)
    {
        switch (value)
        {
            case JsonObject map:
                var result = new JsonObject();
                foreach (var pair in map)
                {
                    if (pair.Key != "message" && !ignored.Contains(pair.Key))
                    {
                        result[pair.Key] = Comparable(pair.Value, ignored);
                    }
                }
                return result;
            case JsonArray list:
                return new JsonArray(list.Select(item => Comparable(item, ignored)).ToArray());
            default:
                return value?.DeepClone();
        }
    }

    /// <summary>Compares two JSON values by what they mean: the order of the fields does not count, and 1 and 1.0 are one number.</summary>
    internal static bool SameJson(JsonNode? one, JsonNode? other)
    {
        switch (one, other)
        {
            case (null, null):
                return true;
            case (JsonObject first, JsonObject second):
                return first.Count == second.Count && first.All(pair => second.ContainsKey(pair.Key) && SameJson(pair.Value, second[pair.Key]));
            case (JsonArray first, JsonArray second):
                return first.Count == second.Count && first.Zip(second).All(pair => SameJson(pair.First, pair.Second));
            case (JsonValue first, JsonValue second):
                if (first.GetValueKind() != second.GetValueKind())
                {
                    return false;
                }
                return first.GetValueKind() == JsonValueKind.Number
                    ? first.GetValue<double>().Equals(second.GetValue<double>())
                    : first.ToJsonString() == second.ToJsonString();
            default:
                return false;
        }
    }

    /// <summary>Runs a scenario in a process of its own, so that nothing it started can outlive the limit.</summary>
    internal static (int Code, string Output) RunScenario(string name, string[]? arguments = null, int limitSeconds = 120, Dictionary<string, string>? variables = null, string? program = null)
    {
        var start = new ProcessStartInfo(program ?? Environment.ProcessPath!)
        {
            RedirectStandardOutput = true,
            RedirectStandardError = true,
            RedirectStandardInput = true,
            UseShellExecute = false,
        };
        start.ArgumentList.Add("--scenario");
        start.ArgumentList.Add(name);
        foreach (string argument in arguments ?? [])
        {
            start.ArgumentList.Add(argument);
        }
        foreach (var pair in variables ?? [])
        {
            start.Environment[pair.Key] = pair.Value;
        }
        using Process process = Process.Start(start)!;
        process.StandardInput.Close();
        var output = process.StandardOutput.ReadToEndAsync();
        var errors = process.StandardError.ReadToEndAsync();
        if (!process.WaitForExit(limitSeconds * 1000))
        {
            process.Kill(entireProcessTree: true);
            process.WaitForExit();
            throw new AssertionFailed($"{name} did not finish in {limitSeconds} s:\n{output.Result}\n{errors.Result}");
        }
        return (process.ExitCode, output.Result + errors.Result);
    }

    internal static void RunOk(string name, string[]? arguments = null, int limitSeconds = 120, Dictionary<string, string>? variables = null)
    {
        (int code, string output) = RunScenario(name, arguments, limitSeconds, variables);
        Check.Equal(0, code, $"{name}:\n{output}\n");
    }

    /// <summary>A folder that is removed afterwards, and that the tests may fill with anything.</summary>
    internal sealed class TemporaryFolder : IDisposable
    {
        internal TemporaryFolder()
        {
            Path = Directory.CreateTempSubdirectory("calendrum-test-").FullName;
        }

        internal string Path { get; }

        public void Dispose() => Directory.Delete(Path, recursive: true);
    }
}

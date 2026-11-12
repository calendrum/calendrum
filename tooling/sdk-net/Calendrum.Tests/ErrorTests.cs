using System;
using System.Collections.Generic;
using System.Text;
using System.Text.Json;
using System.Text.Json.Nodes;
using Calendrum.Internal;

namespace Calendrum.Tests;

/// <summary>Each code has its class, and no error carries the input.</summary>
internal static class ErrorTests
{
    private const string Marker = "ZZMARKZZ";

    private static string Carried(Exception error)
    {
        var parts = new StringBuilder(error.ToString());
        for (Exception? seen = error; seen is not null; seen = seen.InnerException)
        {
            parts.Append('\n').Append(seen.Message).Append('\n').Append(seen.Source);
            foreach (System.Collections.DictionaryEntry entry in seen.Data)
            {
                parts.Append('\n').Append(entry.Key).Append('=').Append(entry.Value);
            }
        }
        return parts.ToString();
    }

    [Test]
    private static void EveryCodeHasItsClass()
    {
        foreach ((string code, Type wanted) in new (string, Type)[]
        {
            ("OPS-BAD-ARGUMENT", typeof(BadArgumentException)),
            ("OPS-UNKNOWN-OPERATION", typeof(UnknownOperationException)),
            ("OPS-UNKNOWN-ZONE", typeof(UnknownZoneException)),
            ("OPS-UNKNOWN-CODE", typeof(UnknownCodeException)),
            ("OPS-TOO-LARGE", typeof(TooLargeException)),
            ("OPS-INTERNAL", typeof(InternalLibraryException)),
        })
        {
            CalendrumException error = ErrorCodes.ToException(code, "text");
            Check.Equal(wanted, error.GetType());
            Check.Equal(code, error.Code);
            Check.Equal("text", error.Message);
        }
    }

    [Test]
    private static void ACodeThisVersionDoesNotKnowGetsTheBaseClass() =>
        Check.Equal(typeof(CalendrumException), ErrorCodes.ToException("OPS-NEW", "text").GetType());

    [Test]
    private static void TheRealLibraryGivesTheseClasses()
    {
        Check.Throws<BadArgumentException>(() => ICal.Expand(Support.Ics, "x", "y"));
        Check.Throws<UnknownOperationException>(() => ICal.Call("nope", []));
        Check.Throws<UnknownZoneException>(() => ICal.TzResolve("Nowhere/Nothing"));
        Check.Throws<UnknownCodeException>(() => ICal.Explain("ICAL-NOPE"));
        Check.Throws<TooLargeException>(() => ICal.Inspect(new byte[(64 * 1024 * 1024) + 1]));
    }

    [Test]
    private static void ALibraryErrorIsNotAnErrorOfAnOperation()
    {
        Check.False(typeof(CalendrumException).IsAssignableFrom(typeof(LibraryException)));
        Check.False(typeof(CalendrumException).IsAssignableFrom(typeof(TruncatedResultException)));
    }

    [Test]
    private static void TheInputIsNotInAnyError()
    {
        var attempts = new Dictionary<string, Action>
        {
            ["rule"] = () => ICal.RruleExpand(Marker, "2026-01-01T00:00:00Z"),
            ["start"] = () => ICal.RruleExpand("FREQ=DAILY", Marker),
            ["zone"] = () => ICal.TzResolve(Marker),
            ["code"] = () => ICal.Explain(Marker),
            ["operation"] = () => ICal.Call(Marker, []),
            ["window"] = () => ICal.Expand(Support.Ics, Marker, Marker),
            ["rules"] = () => ICal.Validate(Support.Ics, new ValidateOptions { Rules = Marker }),
            ["fixup"] = () => ICal.Fix(Support.Ics, new FixOptions { Fixups = [Marker] }),
            ["jcal"] = () => ICal.Call("convert", new JsonObject { ["jcal"] = Marker }),
            ["lone surrogate"] = () => ICal.Inspect($"\ud800{Marker}"),
            ["lone surrogate after a pair"] = () => ICal.Inspect($"\ud83d\ude00\udc00{Marker}"),
            ["surrogate in a tree"] = () => ICal.Call("inspect", new JsonObject { ["ics"] = $"\ud800{Marker}" }),
            ["surrogate as a letter"] = () => ICal.Call("inspect", new JsonObject { ["ics"] = JsonValue.Create('\ud800'), [Marker] = 1 }),
            ["surrogate in a key"] = () => ICal.Call("inspect", new JsonObject { [$"\ud800{Marker}"] = 1 }),
            ["surrogate in the operation"] = () => ICal.Call($"\ud800{Marker}", []),
            ["escaped surrogate in a parsed tree"] = () => ICal.Call("inspect", JsonNode.Parse($"{{\"ics\":\"\\ud800{Marker}\"}}")!.AsObject()),
            ["escaped surrogate in a parsed key"] = () => ICal.Call("inspect", JsonNode.Parse($"{{\"\\ud800{Marker}\":1}}")!.AsObject()),
            ["escaped surrogate in jCal"] = () => ICal.ConvertFromJcal(JsonDocument.Parse($"[\"\\ud800{Marker}\"]").RootElement),
            ["escaped surrogate in a jCal key"] = () => ICal.ConvertFromJcal(JsonDocument.Parse($"[{{\"\\udc00{Marker}\":1}}]").RootElement),
            ["zero in the operation"] = () => ICal.Call($"explain\0{Marker}", new JsonObject { ["code"] = "ICAL-S003" }),
            ["event"] = () => ICal.BuildEvent(Marker, Marker),
            ["field of a request"] = () => ICal.Call("explain", new JsonObject { ["code"] = 5, [Marker] = Marker }),
            ["a key next to what cannot be JSON"] = () => ICal.Call("inspect", new JsonObject { [Marker] = JsonValue.Create(double.NaN) }),
        };
        foreach (var pair in attempts)
        {
            try
            {
                pair.Value();
            }
            catch (CalendrumException error)
            {
                Check.NotContains(Marker, Carried(error), pair.Key);
                continue;
            }
            throw new AssertionFailed($"{pair.Key}: nothing was thrown");
        }
    }

    [Test]
    private static void HalfOfASurrogatePairIsRefusedAndAWholePairIsNot()
    {
        Check.Throws<BadArgumentException>(() => ICal.Inspect("\ud800"));
        Check.Throws<BadArgumentException>(() => ICal.Inspect("\udc00x"));
        Check.Throws<BadArgumentException>(() => ICal.TzResolve("x\ud83d"));
        Check.Throws<BadArgumentException>(() => ICal.BuildEvent("\ud800", "2026-01-01T00:00:00Z"));
        Check.Throws<BadArgumentException>(() => ICal.Fix(Support.Ics, new FixOptions { Fixups = ["\ud800"] }));
        Check.Throws<BadArgumentException>(() => ICal.BuildEvent("x", "2026-01-01T00:00:00Z", new BuildEventOptions { Attendees = [new Attendee { Address = "\ud800" }] }));
        foreach (Action parsed in new Action[]
        {
            () => ICal.Call("inspect", JsonNode.Parse("{\"ics\":\"\\ud800\"}")!.AsObject()),
            () => ICal.Call("inspect", JsonNode.Parse("{\"\\ud800\":1}")!.AsObject()),
            () => ICal.ConvertFromJcal(JsonDocument.Parse("[\"vcalendar\",[[\"x-a\",{},\"text\",\"\\ud800\"]],[]]").RootElement),
        })
        {
            var error = Check.Throws<BadArgumentException>(parsed);
            Check.Contains("surrogate", error.Message, "a parsed tree");
        }
        string summary = ICal.Inspect(Support.Ics.Replace("Plain", "Emoji \ud83d\ude00\ud83d\ude00", StringComparison.Ordinal)).Calendars[0].Events[0].Summary!;
        Check.Equal("Emoji \ud83d\ude00\ud83d\ude00", summary);
    }

    [Test]
    private static void ARequestNestedTooDeeplyIsABadArgument()
    {
        foreach (int depth in new[] { 100, 900, 5000, 200000 })
        {
            JsonNode nested = new JsonArray();
            for (int level = 0; level < depth; level++)
            {
                nested = new JsonArray(nested);
            }
            Check.Throws<BadArgumentException>(() => ICal.Call("convert", new JsonObject { ["jcal"] = nested }), $"depth {depth}:");
        }
    }

    [Test]
    private static void AZeroInTheNameIsNotAnotherOperation() =>
        Check.Throws<UnknownOperationException>(() => ICal.Call("explain\0inspect", new JsonObject { ["code"] = "ICAL-S003" }));

    [Test]
    private static void ANullArgumentIsTheErrorOfTheLanguage()
    {
        Check.Throws<ArgumentNullException>(() => ICal.Inspect((CalendarInput)null!));
        Check.Throws<ArgumentNullException>(() => ICal.Inspect((string)null!));
        Check.Throws<ArgumentNullException>(() => ICal.Inspect((byte[])null!));
        Check.Throws<ArgumentNullException>(() => ICal.Expand(Support.Ics, null!, "x"));
        Check.Throws<ArgumentNullException>(() => ICal.Call(null!, []));
        Check.Throws<ArgumentNullException>(() => ICal.Call("explain", null!));
        Check.Throws<ArgumentNullException>(() => ICal.Fix(Support.Ics, new FixOptions { Fixups = [null!] }));
    }

    [Test]
    private static void ResponsesThatDoNotFollowTheContract()
    {
        foreach ((string name, byte[] raw) in new (string, byte[])[]
        {
            ("not JSON", Encoding.UTF8.GetBytes($"not json {Marker}")),
            ("not UTF-8", [0xFF, 0xFE, .. Encoding.UTF8.GetBytes(Marker)]),
            ("empty", []),
            ("an array", Encoding.UTF8.GetBytes($"[\"{Marker}\"]")),
            ("no result", Encoding.UTF8.GetBytes($"{{\"ok\":true,\"{Marker}\":1}}")),
            ("a result of the wrong type", Encoding.UTF8.GetBytes($"{{\"ok\":true,\"result\":\"{Marker}\"}}")),
            ("no error", Encoding.UTF8.GetBytes($"{{\"ok\":false,\"{Marker}\":1}}")),
            ("no ok", Encoding.UTF8.GetBytes($"{{\"{Marker}\":1}}")),
            ("a result without its fields", Encoding.UTF8.GetBytes($"{{\"ok\":true,\"result\":{{\"{Marker}\":1}}}}")),
            ("an error without its code", Encoding.UTF8.GetBytes($"{{\"ok\":false,\"error\":{{\"message\":\"{Marker}\"}}}}")),
        })
        {
            LibraryException error = Check.Throws<LibraryException>(() => ICal.Decode(raw, CalendrumJson.Default.EnvelopeExplainResult), $"{name}:");
            Check.NotContains(Marker, Carried(error), name);
            Check.True(error.InnerException is null, $"{name}: nothing inside the exception");
        }
    }

    [Test]
    private static void ResponsesThatDoNotFollowTheContractThroughCall()
    {
        foreach ((string name, byte[] raw) in new (string, byte[])[]
        {
            ("not JSON", Encoding.UTF8.GetBytes($"not json {Marker}")),
            ("not UTF-8", [0xFF, 0xFE, .. Encoding.UTF8.GetBytes(Marker)]),
            ("an array", Encoding.UTF8.GetBytes($"[\"{Marker}\"]")),
            ("no result", Encoding.UTF8.GetBytes($"{{\"ok\":true,\"{Marker}\":1}}")),
            ("a result of the wrong type", Encoding.UTF8.GetBytes($"{{\"ok\":true,\"result\":\"{Marker}\"}}")),
            ("no error", Encoding.UTF8.GetBytes($"{{\"ok\":false,\"{Marker}\":1}}")),
        })
        {
            LibraryException error = Check.Throws<LibraryException>(() => ICal.DecodeCall(raw), $"{name}:");
            Check.NotContains(Marker, Carried(error), name);
            Check.True(error.InnerException is null, $"{name}: nothing inside the exception");
        }
    }

    [Test]
    private static void ATruncatedResultIsVisibleAndCanBeRefused()
    {
        ExpandResult result = ICal.Expand(Support.Ics, Support.From, Support.To, new ExpandOptions { Limit = 1 });
        Check.True(result.Truncated);
        Check.Equal(1, result.Count);
        TruncatedResultException error = Check.Throws<TruncatedResultException>(() => result.EnsureComplete());
        Check.NotContains("Plain", Carried(error));

        ExpandResult whole = ICal.Expand(Support.Ics, Support.From, Support.To);
        Check.False(whole.Truncated);
        Check.True(ReferenceEquals(whole, whole.EnsureComplete()));
        Check.Equal(3, whole.Count);

        CallResult call = ICal.Call("expand", new JsonObject { ["ics"] = Support.Ics, ["from"] = Support.From, ["to"] = Support.To, ["limit"] = 1 });
        Check.True(call.Truncated);
        Check.False(call.Result.ContainsKey("truncated"), "it belongs to the envelope");
        Check.Throws<TruncatedResultException>(() => call.EnsureComplete());
    }
}

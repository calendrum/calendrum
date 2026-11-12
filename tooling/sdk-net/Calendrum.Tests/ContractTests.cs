using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.Json.Serialization.Metadata;
using System.Text.RegularExpressions;
using Calendrum.Internal;

namespace Calendrum.Tests;

/// <summary>
/// All the cases of the contract through the real library, by the rules of conformance/ops/README.md. A case goes through the method of
/// its operation where its arguments can say the request, and always through <see cref="ICal.Call"/> as well. The library has a real
/// clock and real random bytes, so the cases that depend on them are checked by what they mean.
/// </summary>
internal static partial class ContractTests
{
    private static readonly HashSet<string> s_clockOrRandom =
        ["anonymize-shift", "anonymize-text-goes", "build-event-all-day-generated-uid", "tz-resolve-default-moment"];

    private sealed record Answer(bool Ok, string? Code, JsonNode? Result, bool Truncated, Type? ErrorClass);

    private static Answer Catching(Func<(JsonNode? Result, bool Truncated)> action)
    {
        try
        {
            (JsonNode? result, bool truncated) = action();
            return new Answer(true, null, result, truncated, null);
        }
        catch (CalendrumException error)
        {
            return new Answer(false, error.Code, null, false, error.GetType());
        }
    }

    private static (JsonNode? Result, bool Truncated) Plain<T>(T result, JsonTypeInfo<T> info)
        where T : IOperationResult => (JsonSerializer.SerializeToNode(result, info), result.Truncated);

    private static bool TryCalendar(JsonObject request, string field, out CalendarInput? input)
    {
        input = null;
        if (request[field] is JsonValue text && text.TryGetValue(out string? value))
        {
            input = value;
        }
        else if (request[$"{field}_base64"] is JsonValue encoded && encoded.TryGetValue(out string? base64))
        {
            input = Convert.FromBase64String(base64);
        }
        return input is not null;
    }

    private static string? Text(JsonObject request, string field) => request[field]?.GetValue<string>();

    private static int? Number(JsonObject request, string field) => request[field]?.GetValue<int>();

    private static bool? Flag(JsonObject request, string field) => request[field]?.GetValue<bool>();

    /// <summary>Calls the method of the operation, or gives null where its signature cannot say the request.</summary>
    private static Answer? RunTyped(ContractCase contractCase)
    {
        JsonObject r = contractCase.Request;
        CalendrumJson json = CalendrumJson.Default;
        CalendarInput? calendar;
        switch (contractCase.Operation)
        {
            case "inspect":
                if (!TryCalendar(r, "ics", out calendar))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.Inspect(calendar!, new InspectOptions { IncludeJcal = Flag(r, "include_jcal"), Limit = Number(r, "limit") }), json.InspectResult));
            case "validate":
                if (!TryCalendar(r, "ics", out calendar))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.Validate(calendar!, new ValidateOptions { Rules = Text(r, "rules"), Limit = Number(r, "limit") }), json.ValidateResult));
            case "fix":
                if (!TryCalendar(r, "ics", out calendar))
                {
                    return null;
                }
                string[]? fixups = (r["fixups"] as JsonArray)?.Select(item => item!.GetValue<string>()).ToArray();
                return Catching(() => Plain(ICal.Fix(calendar!, new FixOptions { Fixups = fixups }), json.FixResult));
            case "expand":
                if (!TryCalendar(r, "ics", out calendar) || !r.ContainsKey("from") || !r.ContainsKey("to"))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.Expand(calendar!, Text(r, "from")!, Text(r, "to")!, new ExpandOptions { Zone = Text(r, "zone"), Limit = Number(r, "limit"), IncludeCancelled = Flag(r, "include_cancelled") }), json.ExpandResult));
            case "rrule_expand":
                if (!r.ContainsKey("rrule") || !r.ContainsKey("start"))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.RruleExpand(Text(r, "rrule")!, Text(r, "start")!, new RruleExpandOptions { Zone = Text(r, "zone"), Limit = Number(r, "limit") }), json.RruleExpandResult));
            case "freebusy":
                if (!TryCalendar(r, "ics", out calendar) || !r.ContainsKey("from") || !r.ContainsKey("to"))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.Freebusy(calendar!, Text(r, "from")!, Text(r, "to")!, new FreebusyOptions { Zone = Text(r, "zone"), Conflicts = Flag(r, "conflicts"), Limit = Number(r, "limit") }), json.FreebusyResult));
            case "build_event":
                if (!r.ContainsKey("summary") || !r.ContainsKey("start"))
                {
                    return null;
                }
                Attendee[]? people = (r["attendees"] as JsonArray)?.Select(item => new Attendee
                {
                    Address = item!["address"]!.GetValue<string>(),
                    Name = item["name"]?.GetValue<string>(),
                    Rsvp = item["rsvp"]?.GetValue<bool>(),
                }).ToArray();
                return Catching(() => Plain(
                    ICal.BuildEvent(Text(r, "summary")!, Text(r, "start")!, new BuildEventOptions
                    {
                        End = Text(r, "end"),
                        Duration = Text(r, "duration"),
                        Zone = Text(r, "zone"),
                        AllDay = Flag(r, "all_day"),
                        Description = Text(r, "description"),
                        Location = Text(r, "location"),
                        Rrule = Text(r, "rrule"),
                        Organizer = Text(r, "organizer"),
                        Attendees = people,
                        Uid = Text(r, "uid"),
                        Dtstamp = Text(r, "dtstamp"),
                    }),
                    json.BuildEventResult));
            case "diff":
                if (!TryCalendar(r, "before", out CalendarInput? before) || !TryCalendar(r, "after", out CalendarInput? after))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.Diff(before!, after!, new DiffOptions { Limit = Number(r, "limit") }), json.DiffResult));
            case "convert":
                bool hasJcal = r.ContainsKey("jcal");
                bool hasCalendar = TryCalendar(r, "ics", out calendar);
                if (hasJcal == hasCalendar)
                {
                    return null;
                }
                if (hasCalendar)
                {
                    return Catching(() => Plain(ICal.ConvertToJcal(calendar!), json.ConvertResult));
                }
                JsonElement element = JsonDocument.Parse(r["jcal"]!.ToJsonString(), new JsonDocumentOptions { MaxDepth = 256 }).RootElement;
                return Catching(() => Plain(ICal.ConvertFromJcal(element), json.ConvertResult));
            case "anonymize":
                if (!TryCalendar(r, "ics", out calendar))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.Anonymize(calendar!, new AnonymizeOptions { ShiftWeeks = Number(r, "shift_weeks") }), json.AnonymizeResult));
            case "tz_resolve":
                if (!r.ContainsKey("name"))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.TzResolve(Text(r, "name")!, new TzResolveOptions { At = Text(r, "at") }), json.TzResolveResult));
            case "explain":
                if (!r.ContainsKey("code"))
                {
                    return null;
                }
                return Catching(() => Plain(ICal.Explain(Text(r, "code")!), json.ExplainResult));
            default:
                throw new AssertionFailed($"No method for {contractCase.Operation}");
        }
    }

    private static Answer RunCall(ContractCase contractCase) => Catching(() =>
    {
        CallResult result = ICal.Call(contractCase.Operation, contractCase.Request.DeepClone().AsObject());
        return (result.Result, result.Truncated);
    });

    private static void Compare(ContractCase contractCase, Answer answer)
    {
        JsonObject expected = contractCase.Expected;
        bool wantedOk = expected["ok"]!.GetValue<bool>();
        Check.Equal(wantedOk, answer.Ok, $"{contractCase.Id}: ok, code {answer.Code};");
        if (!wantedOk)
        {
            Check.Equal(expected["error"]!["code"]!.GetValue<string>(), answer.Code, $"{contractCase.Id}:");
            return;
        }
        Check.Equal(expected["truncated"]?.GetValue<bool>() ?? false, answer.Truncated, $"{contractCase.Id}: truncated;");
        if (s_clockOrRandom.Contains(contractCase.Id))
        {
            CheckByMeaning(contractCase, answer.Result!.AsObject());
            return;
        }
        JsonNode? one = Support.Comparable(expected["result"], contractCase.Ignored);
        JsonNode? other = Support.Comparable(answer.Result, contractCase.Ignored);
        Check.True(Support.SameJson(one, other), $"{contractCase.Id}:\nexpected {one?.ToJsonString()}\ngot      {other?.ToJsonString()}");
    }

    private static IEnumerable<string> Shape(string ics) => ics.Split("\r\n").Select(line => line.Split(':')[0].Split(';')[0]);

    private static void CheckByMeaning(ContractCase contractCase, JsonObject result)
    {
        JsonObject expected = contractCase.Expected["result"]!.AsObject();
        switch (contractCase.Operation)
        {
            case "anonymize":
                string text = result["ics"]!.GetValue<string>();
                foreach (string secret in new[] { "Salary", "Alice", "boss@example.com" })
                {
                    Check.NotContains(secret, text, contractCase.Id);
                }
                Check.Equal(expected["shift_weeks"]!.GetValue<int>(), result["shift_weeks"]!.GetValue<int>());
                Check.True(Shape(expected["ics"]!.GetValue<string>()).SequenceEqual(Shape(text)), contractCase.Id);
                break;
            case "build_event":
                string uid = result["uid"]!.GetValue<string>();
                string ics = result["ics"]!.GetValue<string>();
                Check.True(GeneratedUid().IsMatch(uid), uid);
                string stamp = Stamp().Match(ics).Groups[1].Value;
                var moment = DateTimeOffset.ParseExact(stamp, "yyyyMMdd'T'HHmmss", null, System.Globalization.DateTimeStyles.AssumeUniversal);
                Check.True(Math.Abs((DateTimeOffset.UtcNow - moment).TotalSeconds) < 120, stamp);
                Check.Equal(expected["ics"]!.GetValue<string>(), ics.Replace(uid, expected["uid"]!.GetValue<string>()).Replace(stamp, "20260101T000000"));
                Check.True(ICal.Validate(ics).Valid, contractCase.Id);
                break;
            case "tz_resolve":
                Check.Equal(expected["id"]!.GetValue<string>(), result["id"]!.GetValue<string>());
                Check.Equal(expected["offset_seconds"]!.GetValue<int>(), result["offset_seconds"]!.GetValue<int>());
                break;
            default:
                throw new AssertionFailed(contractCase.Id);
        }
    }

    [GeneratedRegex("^[0-9a-f]{32}@calendrum$")]
    private static partial Regex GeneratedUid();

    [GeneratedRegex(@"DTSTAMP:(\d{8}T\d{6})Z")]
    private static partial Regex Stamp();

    [Test]
    private static void EveryCaseThroughItsMethod()
    {
        var onlyCall = new List<string>();
        var through = 0;
        foreach (ContractCase contractCase in Support.AllCases())
        {
            Answer? answer = RunTyped(contractCase);
            if (answer is null)
            {
                onlyCall.Add(contractCase.Id);
                continue;
            }
            Compare(contractCase, answer);
            through++;
        }
        Console.WriteLine($"     contract: {Support.AllCases().Count} cases; {through} through the methods, {s_clockOrRandom.Count} of them by meaning; only through Call: {string.Join(", ", onlyCall)}");
        Check.Equal(60, Support.AllCases().Count, "the number of cases;");
        Check.Equal("convert-both,diff-no-after,explain-missing,inspect-no-input", string.Join(",", onlyCall.Order(StringComparer.Ordinal)));
    }

    [Test]
    private static void EveryCaseThroughCall()
    {
        foreach (ContractCase contractCase in Support.AllCases())
        {
            Compare(contractCase, RunCall(contractCase));
        }
    }

    [Test]
    private static void AnErrorComesWithItsClass()
    {
        var classes = new Dictionary<string, Type>
        {
            ["OPS-BAD-ARGUMENT"] = typeof(BadArgumentException),
            ["OPS-UNKNOWN-ZONE"] = typeof(UnknownZoneException),
            ["OPS-UNKNOWN-CODE"] = typeof(UnknownCodeException),
        };
        var seen = new HashSet<string>();
        foreach (ContractCase contractCase in Support.AllCases().Where(item => !item.Expected["ok"]!.GetValue<bool>()))
        {
            string code = contractCase.Expected["error"]!["code"]!.GetValue<string>();
            Check.Equal(classes[code], RunCall(contractCase).ErrorClass, contractCase.Id);
            Answer? typed = RunTyped(contractCase);
            if (typed is not null)
            {
                Check.Equal(classes[code], typed.ErrorClass, contractCase.Id);
            }
            seen.Add(code);
        }
        Check.True(seen.SetEquals(classes.Keys));
    }

    [Test]
    private static void TheMethodsTakeEveryFieldOfTheRequests()
    {
        var methods = new Dictionary<string, (string Method, Type? Options, string[] Positional)>
        {
            ["inspect"] = ("Inspect", typeof(InspectOptions), ["calendar"]),
            ["validate"] = ("Validate", typeof(ValidateOptions), ["calendar"]),
            ["fix"] = ("Fix", typeof(FixOptions), ["calendar"]),
            ["expand"] = ("Expand", typeof(ExpandOptions), ["calendar", "from", "to"]),
            ["rrule_expand"] = ("RruleExpand", typeof(RruleExpandOptions), ["rrule", "start"]),
            ["freebusy"] = ("Freebusy", typeof(FreebusyOptions), ["calendar", "from", "to"]),
            ["build_event"] = ("BuildEvent", typeof(BuildEventOptions), ["summary", "start"]),
            ["diff"] = ("Diff", typeof(DiffOptions), ["before", "after"]),
            ["anonymize"] = ("Anonymize", typeof(AnonymizeOptions), ["calendar"]),
            ["tz_resolve"] = ("TzResolve", typeof(TzResolveOptions), ["name"]),
            ["explain"] = ("Explain", null, ["code"]),
        };
        foreach (var pair in methods)
        {
            JsonObject schema = Support.LoadObject($"{Support.SchemasDirectory}/{pair.Key}.request.schema.json");
            HashSet<string> wanted = schema["properties"]!.AsObject().Select(item => item.Key).Where(name => !name.EndsWith("_base64", StringComparison.Ordinal)).ToHashSet();
            if (wanted.Remove("ics"))
            {
                wanted.Add("calendar");
            }
            MethodInfo method = typeof(ICal).GetMethod(pair.Value.Method)!;
            HashSet<string> parameters = method.GetParameters().Select(parameter => parameter.Name!).Where(name => name != "options").ToHashSet();
            Check.True(parameters.SetEquals(pair.Value.Positional), pair.Key);
            if (pair.Value.Options is { } options)
            {
                parameters.UnionWith(options.GetProperties().Select(property => Snake(property.Name)));
            }
            Check.True(wanted.SetEquals(parameters), $"{pair.Key}: schema [{string.Join(",", wanted.Order())}], method [{string.Join(",", parameters.Order())}]");
        }
    }

    internal static string Snake(string name) => string.Concat(name.Select((letter, index) => char.IsUpper(letter) ? (index > 0 ? "_" : "") + char.ToLowerInvariant(letter) : letter.ToString()));
}

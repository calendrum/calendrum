using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text.Json.Nodes;
using System.Text.Json.Serialization;

namespace Calendrum.Tests;

/// <summary>The classes of the results against the schemas, and what a caller of them can and cannot do.</summary>
internal static class ModelTests
{
    private static readonly Dictionary<string, Type> s_results = new()
    {
        ["inspect"] = typeof(InspectResult),
        ["validate"] = typeof(ValidateResult),
        ["fix"] = typeof(FixResult),
        ["expand"] = typeof(ExpandResult),
        ["rrule_expand"] = typeof(RruleExpandResult),
        ["freebusy"] = typeof(FreebusyResult),
        ["build_event"] = typeof(BuildEventResult),
        ["diff"] = typeof(DiffResult),
        ["convert"] = typeof(ConvertResult),
        ["anonymize"] = typeof(AnonymizeResult),
        ["tz_resolve"] = typeof(TzResolveResult),
        ["explain"] = typeof(ExplainResult),
    };

    private static JsonObject Dereference(JsonObject schema)
    {
        if (schema["$ref"] is JsonValue reference)
        {
            string name = reference.GetValue<string>().Split('/')[^1];
            return Support.LoadObject($"{Support.SchemasDirectory}/common.schema.json")["$defs"]![name]!.AsObject();
        }
        return schema;
    }

    private static void Walk(Type type, JsonObject schema, string path, HashSet<Type> seen)
    {
        seen.Add(type);
        Dictionary<string, PropertyInfo> properties = type.GetProperties()
            .Where(property => property.GetCustomAttribute<JsonPropertyNameAttribute>() is not null)
            .ToDictionary(property => property.GetCustomAttribute<JsonPropertyNameAttribute>()!.Name);
        JsonObject fields = schema["properties"]!.AsObject();
        Check.True(properties.Keys.ToHashSet().SetEquals(fields.Select(field => field.Key)), $"{path}: the fields differ");
        HashSet<string> required = (schema["required"] as JsonArray)?.Select(item => item!.GetValue<string>()).ToHashSet() ?? [];
        var nullability = new NullabilityInfoContext();
        foreach (var pair in fields)
        {
            PropertyInfo property = properties[pair.Key];
            JsonObject field = Dereference(pair.Value!.AsObject());
            bool isNullable = nullability.Create(property).ReadState == NullabilityState.Nullable;
            Check.Equal(!required.Contains(pair.Key), isNullable, $"{path}.{pair.Key}: nullable;");
            Check.False(property.SetMethod is { IsPublic: true } && !property.SetMethod.ReturnParameter!.GetRequiredCustomModifiers().Any(), $"{path}.{pair.Key} can be set");
            Type inner = Nullable.GetUnderlyingType(property.PropertyType) ?? property.PropertyType;
            switch (field["type"]!.GetValue<string>())
            {
                case "object":
                    Walk(inner, field, $"{path}.{pair.Key}", seen);
                    break;
                case "array" when field["items"] is JsonObject items:
                    JsonObject item = Dereference(items);
                    if (item["type"]!.GetValue<string>() == "object")
                    {
                        Walk(inner.GetGenericArguments()[0], item, $"{path}.{pair.Key}[]", seen);
                    }
                    break;
            }
            if (field["x-untrusted"]?.GetValue<bool>() == true)
            {
                Check.True(inner == typeof(string) || inner == typeof(System.Text.Json.JsonElement) || inner.IsGenericType, $"{path}.{pair.Key}");
            }
        }
    }

    [Test]
    private static void EveryFieldOfEverySchemaHasItsProperty()
    {
        var seen = new HashSet<Type>();
        foreach (var pair in s_results)
        {
            Walk(pair.Value, Support.LoadObject($"{Support.SchemasDirectory}/{pair.Key}.result.schema.json"), pair.Key, seen);
        }
        HashSet<Type> generated = typeof(ICal).Assembly.GetExportedTypes()
            .Where(type => type.GetProperties().Any(property => property.GetCustomAttribute<JsonPropertyNameAttribute>() is not null))
            .ToHashSet();
        Check.True(generated.SetEquals(seen), "a class of the results is not reached from the schemas");
    }

    [Test]
    private static void EveryOperationResultSaysWhetherItIsTruncated()
    {
        foreach (Type type in s_results.Values)
        {
            Check.True(typeof(IOperationResult).IsAssignableFrom(type), type.Name);
            Check.False(type.GetProperty("Truncated")!.SetMethod!.IsPublic, $"{type.Name}: a caller could change it");
        }
    }

    [Test]
    private static void AResultDoesNotPrintWhatACalendarSays()
    {
        // A log line of a result stays free of the text of a stranger, as nothing here overrides ToString.
        ExpandResult result = ICal.Expand(Support.Ics, Support.From, Support.To);
        Check.NotContains("Plain", result.ToString()!);
        Check.NotContains("Plain", result.Occurrences[0].ToString()!);
        Check.NotContains("Plain", ICal.Inspect(Support.Ics).Calendars[0].Events[0].ToString()!);
        Check.Equal("Plain", result.Occurrences[0].Summary);
    }

    [Test]
    private static void EverythingPublicIsDocumented()
    {
        // The build fails on a public member without a comment (CS1591), so this looks at the file that it writes.
        string path = System.IO.Path.ChangeExtension(typeof(ICal).Assembly.Location, ".xml");
        string text = System.IO.File.ReadAllText(path);
        Check.Contains("M:Calendrum.ICal.Expand", text);
        Check.Contains("P:Calendrum.Occurrence.Summary", text);
        Check.Contains("Untrusted: text from the calendar", text);
    }

    [Test]
    private static void JcalFarDeeperThanTheLibraryCanGiveIsStillRead()
    {
        string deep = new string('[', 200) + new string(']', 200);
        byte[] response = System.Text.Encoding.UTF8.GetBytes($$$"""{"ok":true,"result":{"jcal":{{{deep}}},"problems":[],"problem_count":0}}""");
        ConvertResult typed = ICal.Decode(response, Calendrum.Internal.CalendrumJson.Default.EnvelopeConvertResult);
        Check.Equal(deep, typed.Jcal!.Value.GetRawText());
        Check.True(ICal.DecodeCall(response).Result["jcal"] is JsonArray);
    }

    [Test]
    private static void TheListsOfAResultCannotBeChanged()
    {
        // A line without a colon: the reader skips it with ICAL-S007.
        ValidateResult result = ICal.Validate(Support.Ics.Replace("DTSTART", "BROKEN\r\nDTSTART", StringComparison.Ordinal));
        Check.True(result.Problems.Count > 0, "the input must have a problem");
        Check.False(result.Problems is List<Problem>, "a List<T> can be changed by a cast");
        var view = (ICollection<Problem>)result.Problems;
        Check.True(view.IsReadOnly);
        Check.Throws<NotSupportedException>(() => view.Add(result.Problems[0]));
        var given = new List<Problem> { result.Problems[0] };
        var copy = new ValidateResult { Issues = [], Valid = false, CalendarCount = 1, Problems = given, ProblemCount = 1 };
        given.Clear();
        Check.Equal(1, copy.Problems.Count);
        Check.Throws<ArgumentNullException>(() => _ = new ValidateResult { Issues = [], Valid = false, CalendarCount = 0, Problems = null!, ProblemCount = 0 });
    }

    [Test]
    private static void AnUnknownFieldOfALaterContractIsIgnored()
    {
        byte[] response = System.Text.Encoding.UTF8.GetBytes("""{"ok":true,"later":1,"result":{"code":"X","severity":"X","constant":"X","meaning":"X","new_field":[1]},"truncated":true}""");
        ExplainResult? result = null;
        try
        {
            result = ICal.Decode(response, Calendrum.Internal.CalendrumJson.Default.EnvelopeExplainResult);
        }
        catch (LibraryException)
        {
            throw new AssertionFailed("A field the package does not know must not stop it. If the schema of explain changed, fix this response.");
        }
        Check.True(result.Truncated);
    }
}

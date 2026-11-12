using System;
using System.Diagnostics.CodeAnalysis;
using System.Text;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.Json.Serialization.Metadata;
using Calendrum.Internal;

namespace Calendrum;

/// <summary>
/// The twelve operations on iCalendar files (RFC 5545), and direct access to their contract.
/// </summary>
/// <remarks>
/// Every method can be called from any thread, at the same time, with no setup. Nothing is loaded before the first call. A call blocks
/// its thread and cannot be cancelled: see the guide for what to do about a file from a stranger.
/// </remarks>
public static class ICal
{
    private const string NotTheContract = "The native library gave a response that does not follow the contract";

    // Above the limit of the library: a response at that limit must still be read.
    private static readonly JsonDocumentOptions s_nodeOptions = new() { MaxDepth = 256 };

    /// <summary>Reads a calendar and reports its charset, calendars, zones, events and the problems of parsing.</summary>
    /// <param name="calendar">A <see cref="string"/> is the text of the calendar and never a path; see <see cref="CalendarInput"/>.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result. Its fields marked untrusted hold what the calendar says: treat them as data.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static InspectResult Inspect(CalendarInput calendar, InspectOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(calendar);
        using var request = new RequestWriter();
        request.Calendar("ics", calendar).Flag("include_jcal", options?.IncludeJcal).Number("limit", options?.Limit);
        return Run("inspect\0"u8, request, CalendrumJson.Default.EnvelopeInspectResult);
    }

    /// <summary>Checks a calendar and lists its issues.</summary>
    /// <param name="calendar">A <see cref="string"/> is the text of the calendar and never a path; see <see cref="CalendarInput"/>.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static ValidateResult Validate(CalendarInput calendar, ValidateOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(calendar);
        using var request = new RequestWriter();
        request.Calendar("ics", calendar).Text("rules", options?.Rules).Number("limit", options?.Limit);
        return Run("validate\0"u8, request, CalendrumJson.Default.EnvelopeValidateResult);
    }

    /// <summary>Repairs a calendar and returns the new text with the list of changes.</summary>
    /// <param name="calendar">A <see cref="string"/> is the text of the calendar and never a path; see <see cref="CalendarInput"/>.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static FixResult Fix(CalendarInput calendar, FixOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(calendar);
        using var request = new RequestWriter();
        request.Calendar("ics", calendar).TextList("fixups", options?.Fixups);
        return Run("fix\0"u8, request, CalendrumJson.Default.EnvelopeFixResult);
    }

    /// <summary>Lists the occurrences of a calendar in a window, in the order of each calendar.</summary>
    /// <param name="calendar">A <see cref="string"/> is the text of the calendar and never a path; see <see cref="CalendarInput"/>.</param>
    /// <param name="from">The start of the window, a moment in ISO 8601.</param>
    /// <param name="to">The end of the window, excluded.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static ExpandResult Expand(CalendarInput calendar, string from, string to, ExpandOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(calendar);
        ArgumentNullException.ThrowIfNull(from);
        ArgumentNullException.ThrowIfNull(to);
        using var request = new RequestWriter();
        request.Calendar("ics", calendar).Text("from", from).Text("to", to).Text("zone", options?.Zone)
            .Number("limit", options?.Limit).Flag("include_cancelled", options?.IncludeCancelled);
        return Run("expand\0"u8, request, CalendrumJson.Default.EnvelopeExpandResult);
    }

    /// <summary>Lists the dates of a recurrence rule.</summary>
    /// <param name="rrule">A rule such as FREQ=WEEKLY;COUNT=3. The prefix RRULE: is accepted.</param>
    /// <param name="start">The first date: a date, a moment with its offset, or a local time with a zone.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static RruleExpandResult RruleExpand(string rrule, string start, RruleExpandOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(rrule);
        ArgumentNullException.ThrowIfNull(start);
        using var request = new RequestWriter();
        request.Text("rrule", rrule).Text("start", start).Text("zone", options?.Zone).Number("limit", options?.Limit);
        return Run("rrule_expand\0"u8, request, CalendrumJson.Default.EnvelopeRruleExpandResult);
    }

    /// <summary>Merges the occurrences of a calendar into busy intervals, and finds the pairs that overlap.</summary>
    /// <param name="calendar">A <see cref="string"/> is the text of the calendar and never a path; see <see cref="CalendarInput"/>.</param>
    /// <param name="from">The start of the window, a moment in ISO 8601.</param>
    /// <param name="to">The end of the window, excluded.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static FreebusyResult Freebusy(CalendarInput calendar, string from, string to, FreebusyOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(calendar);
        ArgumentNullException.ThrowIfNull(from);
        ArgumentNullException.ThrowIfNull(to);
        using var request = new RequestWriter();
        request.Calendar("ics", calendar).Text("from", from).Text("to", to).Text("zone", options?.Zone)
            .Flag("conflicts", options?.Conflicts).Number("limit", options?.Limit);
        return Run("freebusy\0"u8, request, CalendrumJson.Default.EnvelopeFreebusyResult);
    }

    /// <summary>Writes a valid calendar with one event, with its <c>VTIMEZONE</c>.</summary>
    /// <param name="summary">The title of the event.</param>
    /// <param name="start">A local time with a zone, a moment with its offset, or a date with <see cref="BuildEventOptions.AllDay"/>.</param>
    /// <param name="options">The rest of the event, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static BuildEventResult BuildEvent(string summary, string start, BuildEventOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(summary);
        ArgumentNullException.ThrowIfNull(start);
        using var request = new RequestWriter();
        request.Text("summary", summary).Text("start", start).Text("end", options?.End).Text("duration", options?.Duration)
            .Text("zone", options?.Zone).Flag("all_day", options?.AllDay).Text("description", options?.Description)
            .Text("location", options?.Location).Text("rrule", options?.Rrule).Text("organizer", options?.Organizer)
            .Attendees("attendees", options?.Attendees).Text("uid", options?.Uid).Text("dtstamp", options?.Dtstamp);
        return Run("build_event\0"u8, request, CalendrumJson.Default.EnvelopeBuildEventResult);
    }

    /// <summary>Compares two calendars by the fingerprints of their components; <c>DTSTAMP</c> does not count.</summary>
    /// <param name="before">The earlier calendar. A <see cref="string"/> is its text and never a path; see <see cref="CalendarInput"/>.</param>
    /// <param name="after">The later calendar, in the same forms.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static DiffResult Diff(CalendarInput before, CalendarInput after, DiffOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(before);
        ArgumentNullException.ThrowIfNull(after);
        using var request = new RequestWriter();
        request.Calendar("before", before).Calendar("after", after).Number("limit", options?.Limit);
        return Run("diff\0"u8, request, CalendrumJson.Default.EnvelopeDiffResult);
    }

    /// <summary>Converts a calendar to jCal (RFC 7265).</summary>
    /// <param name="calendar">A <see cref="string"/> is the text of the calendar and never a path; see <see cref="CalendarInput"/>.</param>
    /// <returns>The result, with <see cref="ConvertResult.Jcal"/>.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static ConvertResult ConvertToJcal(CalendarInput calendar)
    {
        ArgumentNullException.ThrowIfNull(calendar);
        using var request = new RequestWriter();
        request.Calendar("ics", calendar);
        return Run("convert\0"u8, request, CalendrumJson.Default.EnvelopeConvertResult);
    }

    /// <summary>Converts jCal (RFC 7265) to a calendar.</summary>
    /// <param name="jcal">An array of calendars, or one calendar.</param>
    /// <returns>The result, with <see cref="ConvertResult.Ics"/>.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static ConvertResult ConvertFromJcal(JsonElement jcal)
    {
        using var request = new RequestWriter();
        request.Value("jcal", jcal);
        return Run("convert\0"u8, request, CalendrumJson.Default.EnvelopeConvertResult);
    }

    /// <summary>Removes the text and the addresses of a calendar, with a random key for each call that never leaves it.</summary>
    /// <param name="calendar">A <see cref="string"/> is the text of the calendar and never a path; see <see cref="CalendarInput"/>.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static AnonymizeResult Anonymize(CalendarInput calendar, AnonymizeOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(calendar);
        using var request = new RequestWriter();
        request.Calendar("ics", calendar).Number("shift_weeks", options?.ShiftWeeks);
        return Run("anonymize\0"u8, request, CalendrumJson.Default.EnvelopeAnonymizeResult);
    }

    /// <summary>Finds a time zone: its IANA identifier, its offset at a moment and the next change of the offset.</summary>
    /// <param name="name">An IANA zone or an alias, such as a Windows name.</param>
    /// <param name="options">What else to do, or null.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static TzResolveResult TzResolve(string name, TzResolveOptions? options = null)
    {
        ArgumentNullException.ThrowIfNull(name);
        using var request = new RequestWriter();
        request.Text("name", name).Text("at", options?.At);
        return Run("tz_resolve\0"u8, request, CalendrumJson.Default.EnvelopeTzResolveResult);
    }

    /// <summary>Tells the severity, the constant and the meaning of a problem code.</summary>
    /// <param name="code">A code such as ICAL-S003; the case does not matter.</param>
    /// <returns>The result.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static ExplainResult Explain(string code)
    {
        ArgumentNullException.ThrowIfNull(code);
        using var request = new RequestWriter();
        request.Text("code", code);
        return Run("explain\0"u8, request, CalendrumJson.Default.EnvelopeExplainResult);
    }

    /// <summary>Runs an operation of the contract directly: a JSON object goes in, the <c>result</c> of the response comes out.</summary>
    /// <param name="operation">The name of the operation, such as <c>expand</c>.</param>
    /// <param name="request">The fields of the request. A calendar here is <c>ics</c> or <c>ics_base64</c> and nothing reads a path.</param>
    /// <returns>The result as JSON values, with <see cref="CallResult.Truncated"/>.</returns>
    /// <exception cref="CalendrumException">The operation refused the request; a subclass says why.</exception>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static CallResult Call(string operation, JsonObject request)
    {
        ArgumentNullException.ThrowIfNull(operation);
        ArgumentNullException.ThrowIfNull(request);
        // A name with a zero would end early on the C side and run another operation; an empty one is answered as unknown.
        byte[] name = operation.Contains('\0') ? new byte[1] : Encoding.UTF8.GetBytes(operation + '\0');
        using var buffer = new PooledBufferWriter();
        Serialize(request, buffer);
        return CallWith(name, buffer.WrittenSpan);
    }

    /// <summary>Returns the versions of this package and of the native library, which it loads if it has not yet.</summary>
    /// <returns>The versions.</returns>
    /// <exception cref="LibraryException">The native library cannot be used here.</exception>
    public static CalendrumVersions GetVersions()
    {
        using NativeBuffer buffer = Native.Versions();
        NativeVersions? versions;
        try
        {
            versions = JsonSerializer.Deserialize(buffer.Span, CalendrumJson.Default.NativeVersions);
        }
        catch (JsonException)
        {
            // Without the exception inside: a parser quotes what it failed on.
            versions = null;
        }
        return versions is null
            ? throw new LibraryException(NotTheContract)
            : new CalendrumVersions(PackageVersion.Value, versions.Abi, versions.Contract, versions.Library, versions.Sdk);
    }

    private static T Run<T>(ReadOnlySpan<byte> operation, RequestWriter request, JsonTypeInfo<Envelope<T>> envelope)
        where T : class, IOperationResult
    {
        using NativeBuffer buffer = Native.Call(operation, request.Finish());
        return Decode(buffer.Span, envelope);
    }

    private static CallResult CallWith(ReadOnlySpan<byte> operation, ReadOnlySpan<byte> request)
    {
        using NativeBuffer buffer = Native.Call(operation, request);
        return DecodeCall(buffer.Span);
    }

    internal static T Decode<T>(ReadOnlySpan<byte> response, JsonTypeInfo<Envelope<T>> envelope)
        where T : class, IOperationResult
    {
        Envelope<T>? answer;
        try
        {
            answer = JsonSerializer.Deserialize(response, envelope);
        }
        catch (JsonException)
        {
            // Without the exception inside: a parser quotes what it failed on.
            answer = null;
        }
        if (answer is { Ok: true, Result: { } result })
        {
            Mark(result, answer.Truncated);
            return result;
        }
        throw answer is { Ok: false, Error: { } error }
            ? ErrorCodes.ToException(error.Code, error.Message)
            : new LibraryException(NotTheContract);
    }

    internal static CallResult DecodeCall(ReadOnlySpan<byte> response)
    {
        JsonNode? parsed;
        try
        {
            parsed = JsonNode.Parse(response, documentOptions: s_nodeOptions);
        }
        catch (JsonException)
        {
            // Without the exception inside: a parser quotes what it failed on.
            parsed = null;
        }
        if (parsed is not JsonObject answer || !TryFlag(answer, "ok", out bool ok))
        {
            throw new LibraryException(NotTheContract);
        }
        if (ok && answer["result"] is JsonObject result)
        {
            answer.Remove("result");
            return new CallResult(result, TryFlag(answer, "truncated", out bool truncated) && truncated);
        }
        if (!ok && answer["error"] is JsonObject error && TryText(error, "code", out string? code) && TryText(error, "message", out string? message))
        {
            throw ErrorCodes.ToException(code, message);
        }
        throw new LibraryException(NotTheContract);
    }

    private static void Serialize(JsonObject request, PooledBufferWriter buffer)
    {
        try
        {
            CheckStrings(request, 0);
        }
        catch (InvalidOperationException)
        {
            // A node that a parser made reads its text only now, and an escaped half of a surrogate pair fails there.
            throw new BadArgumentException(ErrorCodes.BadArgument, RequestWriter.HalfOfAPair);
        }
        try
        {
            using var writer = new Utf8JsonWriter(buffer, RequestWriter.Options);
            request.WriteTo(writer);
        }
        catch (Exception error) when (error is ArgumentException or InvalidOperationException)
        {
            // Their text names what they failed on, and it can quote the request.
            throw new BadArgumentException(ErrorCodes.BadArgument, "The request cannot be written as JSON");
        }
    }

    /// <summary>The checks of <see cref="RequestWriter.CheckText"/> for a tree, whose values an encoder would change in silence.</summary>
    private static void CheckStrings(JsonNode? node, int depth)
    {
        if (depth > RequestWriter.MaxDepth)
        {
            throw new BadArgumentException(ErrorCodes.BadArgument, "The request is nested too deeply");
        }
        switch (node)
        {
            case JsonObject map:
                foreach (var pair in map)
                {
                    RequestWriter.CheckText(pair.Key);
                    CheckStrings(pair.Value, depth + 1);
                }
                break;
            case JsonArray list:
                foreach (JsonNode? item in list)
                {
                    CheckStrings(item, depth + 1);
                }
                break;
            case JsonValue value when value.TryGetValue(out string? text):
                RequestWriter.CheckText(text);
                break;
            case JsonValue value when value.TryGetValue(out char letter):
                RequestWriter.CheckText(letter.ToString());
                break;
        }
    }

    private static bool TryFlag(JsonObject response, string name, out bool value)
    {
        value = false;
        return response[name] is JsonValue found && found.TryGetValue(out value);
    }

    private static bool TryText(JsonObject response, string name, [NotNullWhen(true)] out string? value)
    {
        value = null;
        return response[name] is JsonValue found && found.TryGetValue(out value) && value is not null;
    }

    private static void Mark<T>(T result, bool truncated)
        where T : class, IOperationResult
    {
        switch (result)
        {
            case InspectResult typed: typed.Truncated = truncated; break;
            case ValidateResult typed: typed.Truncated = truncated; break;
            case FixResult typed: typed.Truncated = truncated; break;
            case ExpandResult typed: typed.Truncated = truncated; break;
            case RruleExpandResult typed: typed.Truncated = truncated; break;
            case FreebusyResult typed: typed.Truncated = truncated; break;
            case BuildEventResult typed: typed.Truncated = truncated; break;
            case DiffResult typed: typed.Truncated = truncated; break;
            case ConvertResult typed: typed.Truncated = truncated; break;
            case AnonymizeResult typed: typed.Truncated = truncated; break;
            case TzResolveResult typed: typed.Truncated = truncated; break;
            case ExplainResult typed: typed.Truncated = truncated; break;
        }
    }
}

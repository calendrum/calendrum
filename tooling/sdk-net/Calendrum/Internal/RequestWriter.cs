using System;
using System.Collections.Generic;
using System.Text.Encodings.Web;
using System.Text.Json;

namespace Calendrum.Internal;

/// <summary>Writes the JSON object of a request. A field that was not given is not written.</summary>
internal sealed class RequestWriter : IDisposable
{
    // The limits of the contract. A larger input is refused here, without copying it. The JSON writer would fail above its own limit
    // for a value, which is close to the one for a request.
    internal const int RequestLimit = 128 * 1024 * 1024;
    internal const int CalendarLimit = 64 * 1024 * 1024;

    // Not for a page: these bytes go to the library only. The usual encoder writes every letter outside ASCII as six bytes.
    internal static readonly JsonWriterOptions Options = new() { Encoder = JavaScriptEncoder.UnsafeRelaxedJsonEscaping };

    private readonly PooledBufferWriter _buffer = new();
    private readonly Utf8JsonWriter _writer;

    internal RequestWriter()
    {
        _writer = new Utf8JsonWriter(_buffer, Options);
        _writer.WriteStartObject();
    }

    internal RequestWriter Text(string name, string? value)
    {
        if (value is not null)
        {
            CheckText(value);
            _writer.WriteString(name, value);
        }
        return this;
    }

    internal RequestWriter Number(string name, int? value)
    {
        if (value is not null)
        {
            _writer.WriteNumber(name, value.Value);
        }
        return this;
    }

    internal RequestWriter Flag(string name, bool? value)
    {
        if (value is not null)
        {
            _writer.WriteBoolean(name, value.Value);
        }
        return this;
    }

    internal RequestWriter TextList(string name, IReadOnlyList<string>? values)
    {
        if (values is not null)
        {
            _writer.WriteStartArray(name);
            foreach (string value in values)
            {
                ArgumentNullException.ThrowIfNull(value, name);
                CheckText(value);
                _writer.WriteStringValue(value);
            }
            _writer.WriteEndArray();
        }
        return this;
    }

    internal RequestWriter Attendees(string name, IReadOnlyList<Attendee>? people)
    {
        if (people is not null)
        {
            _writer.WriteStartArray(name);
            foreach (Attendee person in people)
            {
                ArgumentNullException.ThrowIfNull(person, name);
                _writer.WriteStartObject();
                Text("address", person.Address);
                Text("name", person.Name);
                Flag("rsvp", person.Rsvp);
                _writer.WriteEndObject();
            }
            _writer.WriteEndArray();
        }
        return this;
    }

    /// <summary>A calendar: text goes as the field itself, and bytes as the same field with the suffix <c>_base64</c>.</summary>
    internal RequestWriter Calendar(string name, CalendarInput calendar)
    {
        if (calendar.AsText is { } text)
        {
            CheckText(text);
            _writer.WriteString(name, text);
        }
        else
        {
            if (calendar.AsBytes.Length > CalendarLimit)
            {
                throw new TooLargeException(ErrorCodes.TooLarge, "The calendar is larger than the limit");
            }
            _writer.WriteBase64String(name + "_base64", calendar.AsBytes.Span);
        }
        return this;
    }

    /// <summary>
    /// A whole JSON value that the caller made. A parser takes an escaped half of a surrogate pair (<c>"\ud800"</c>) and fails only
    /// when the text is read, so every string and name is read and checked before anything is written.
    /// </summary>
    internal RequestWriter Value(string name, JsonElement value)
    {
        if (value.ValueKind == JsonValueKind.Undefined)
        {
            throw new BadArgumentException(ErrorCodes.BadArgument, "The value of a field is not JSON");
        }
        try
        {
            CheckElement(value, 0);
        }
        catch (InvalidOperationException)
        {
            throw new BadArgumentException(ErrorCodes.BadArgument, HalfOfAPair);
        }
        _writer.WritePropertyName(name);
        try
        {
            value.WriteTo(_writer);
        }
        catch (InvalidOperationException)
        {
            // Nesting past the limit of the writer, which is far above the one of the library.
            throw new BadArgumentException(ErrorCodes.BadArgument, "The request is nested too deeply");
        }
        return this;
    }

    internal ReadOnlySpan<byte> Finish()
    {
        _writer.WriteEndObject();
        _writer.Flush();
        return _buffer.WrittenSpan;
    }

    public void Dispose()
    {
        _writer.Dispose();
        _buffer.Dispose();
    }

    internal const string HalfOfAPair = "A string holds half of a surrogate pair";

    // The limit of nesting that the checks of a tree from the caller follow.
    internal const int MaxDepth = 1000;

    private static void CheckElement(JsonElement element, int depth)
    {
        if (depth > MaxDepth)
        {
            throw new BadArgumentException(ErrorCodes.BadArgument, "The request is nested too deeply");
        }
        switch (element.ValueKind)
        {
            case JsonValueKind.Object:
                foreach (JsonProperty property in element.EnumerateObject())
                {
                    CheckText(property.Name);
                    CheckElement(property.Value, depth + 1);
                }
                break;
            case JsonValueKind.Array:
                foreach (JsonElement item in element.EnumerateArray())
                {
                    CheckElement(item, depth + 1);
                }
                break;
            case JsonValueKind.String:
                CheckText(element.GetString()!);
                break;
        }
    }

    /// <summary>
    /// JSON cannot hold half of a surrogate pair, and an encoder would replace it in silence, so that the library reads another text.
    /// </summary>
    internal static void CheckText(string text)
    {
        if (text.Length > RequestLimit)
        {
            throw new TooLargeException(ErrorCodes.TooLarge, "The request is larger than the limit");
        }
        ReadOnlySpan<char> rest = text;
        while (true)
        {
            int index = rest.IndexOfAnyInRange('\uD800', '\uDFFF');
            if (index < 0)
            {
                return;
            }
            if (index + 1 >= rest.Length || !char.IsHighSurrogate(rest[index]) || !char.IsLowSurrogate(rest[index + 1]))
            {
                throw new BadArgumentException(ErrorCodes.BadArgument, HalfOfAPair);
            }
            rest = rest[(index + 2)..];
        }
    }
}

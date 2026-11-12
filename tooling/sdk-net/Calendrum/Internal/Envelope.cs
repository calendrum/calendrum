using System.Text.Json.Serialization;

namespace Calendrum.Internal;

/// <summary>Every response of the contract. Read in one pass, so a result is not parsed twice.</summary>
internal sealed class Envelope<T>
    where T : class
{
    [JsonPropertyName("ok")]
    public bool? Ok { get; init; }

    [JsonPropertyName("result")]
    public T? Result { get; init; }

    [JsonPropertyName("truncated")]
    public bool Truncated { get; init; }

    [JsonPropertyName("error")]
    public EnvelopeError? Error { get; init; }
}

internal sealed class EnvelopeError
{
    [JsonPropertyName("code")]
    public required string Code { get; init; }

    [JsonPropertyName("message")]
    public required string Message { get; init; }
}

internal sealed class NativeVersions
{
    [JsonPropertyName("abi")]
    public required int Abi { get; init; }

    [JsonPropertyName("contract")]
    public required int Contract { get; init; }

    [JsonPropertyName("library")]
    public required string Library { get; init; }

    [JsonPropertyName("sdk")]
    public required string Sdk { get; init; }
}

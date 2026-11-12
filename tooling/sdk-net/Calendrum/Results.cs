using System;
using System.Text.Json.Nodes;

namespace Calendrum;

/// <summary>What every result of an operation says about itself.</summary>
public interface IOperationResult
{
    /// <summary>Whether the result does not hold everything: a limit cut a list, or the input was not read whole.</summary>
    bool Truncated { get; }
}

/// <summary>Helps with what every result has.</summary>
public static class OperationResultExtensions
{
    /// <summary>Returns the result, or fails when it does not hold everything: for code that has no use for a part of an answer.</summary>
    /// <typeparam name="T">The type of the result.</typeparam>
    /// <param name="result">The result.</param>
    /// <returns>The same result.</returns>
    /// <exception cref="ArgumentNullException"><paramref name="result"/> is null.</exception>
    /// <exception cref="TruncatedResultException">The result does not hold everything.</exception>
    public static T EnsureComplete<T>(this T result)
        where T : IOperationResult
    {
        ArgumentNullException.ThrowIfNull(result);
        return result.Truncated ? throw new TruncatedResultException() : result;
    }
}

/// <summary>The <c>result</c> of an operation as JSON values, from <see cref="ICal.Call"/>.</summary>
public sealed class CallResult : IOperationResult
{
    internal CallResult(JsonObject result, bool truncated)
    {
        Result = result;
        Truncated = truncated;
    }

    /// <summary>The fields of the result. Those the contract marks as untrusted hold what the calendar says.</summary>
    public JsonObject Result { get; }

    /// <inheritdoc/>
    public bool Truncated { get; }
}

/// <summary>The versions of what is running.</summary>
public sealed class CalendrumVersions
{
    internal CalendrumVersions(string package, int abi, int contract, string library, string sdk)
    {
        Package = package;
        Abi = abi;
        Contract = contract;
        Library = library;
        Sdk = sdk;
    }

    /// <summary>This .NET package.</summary>
    public string Package { get; }

    /// <summary>The C interface of the native library.</summary>
    public int Abi { get; }

    /// <summary>The operations contract.</summary>
    public int Contract { get; }

    /// <summary>The iCalendar library inside.</summary>
    public string Library { get; }

    /// <summary>The native library.</summary>
    public string Sdk { get; }
}

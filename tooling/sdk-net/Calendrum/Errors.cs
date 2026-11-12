using System;

namespace Calendrum;

/// <summary>
/// An operation refused the request. A problem of a calendar is not an error: the operation succeeds and lists it in
/// <c>Problems</c>.
/// </summary>
/// <remarks>The message comes from the contract or from this package, and never quotes the request or the calendar.</remarks>
public class CalendrumException : Exception
{
    /// <summary>Creates the error of a code.</summary>
    /// <param name="code">A stable code of the contract, such as <c>OPS-BAD-ARGUMENT</c>.</param>
    /// <param name="message">A fixed sentence.</param>
    public CalendrumException(string code, string message)
        : base(message)
    {
        Code = code;
    }

    /// <summary>A stable code of the contract, such as <c>OPS-BAD-ARGUMENT</c>.</summary>
    public string Code { get; }
}

/// <summary>A field has the wrong type or value, or the fields do not make a valid request.</summary>
public sealed class BadArgumentException : CalendrumException
{
    /// <inheritdoc cref="CalendrumException(string, string)"/>
    public BadArgumentException(string code, string message)
        : base(code, message)
    {
    }
}

/// <summary>There is no such operation.</summary>
public sealed class UnknownOperationException : CalendrumException
{
    /// <inheritdoc cref="CalendrumException(string, string)"/>
    public UnknownOperationException(string code, string message)
        : base(code, message)
    {
    }
}

/// <summary>A zone of the request is not in the time zone data.</summary>
public sealed class UnknownZoneException : CalendrumException
{
    /// <inheritdoc cref="CalendrumException(string, string)"/>
    public UnknownZoneException(string code, string message)
        : base(code, message)
    {
    }
}

/// <summary><see cref="ICal.Explain"/> does not know the code.</summary>
public sealed class UnknownCodeException : CalendrumException
{
    /// <inheritdoc cref="CalendrumException(string, string)"/>
    public UnknownCodeException(string code, string message)
        : base(code, message)
    {
    }
}

/// <summary>The request or the calendar is above the limit, or a response that is not a list is.</summary>
public sealed class TooLargeException : CalendrumException
{
    /// <inheritdoc cref="CalendrumException(string, string)"/>
    public TooLargeException(string code, string message)
        : base(code, message)
    {
    }
}

/// <summary>A bug of the library. No operation should ever give it.</summary>
public sealed class InternalLibraryException : CalendrumException
{
    /// <inheritdoc cref="CalendrumException(string, string)"/>
    public InternalLibraryException(string code, string message)
        : base(code, message)
    {
    }
}

/// <summary>
/// The native library cannot be used here: it did not load, or it failed outside any operation.
/// </summary>
/// <remarks>
/// This is not a <see cref="CalendrumException"/>: code that handles a bad calendar with a <c>catch</c> of that class must not also
/// hide a broken deployment.
/// </remarks>
public sealed class LibraryException : Exception
{
    internal LibraryException(string message, int? nativeCode = null)
        : base(message)
    {
        NativeCode = nativeCode;
    }

    internal LibraryException(string message, Exception inner)
        : base(message, inner)
    {
    }

    /// <summary>The code the library returned, or <see langword="null"/> when it did not get that far.</summary>
    public int? NativeCode { get; }
}

/// <summary>
/// A result does not hold everything, and the caller asked to be told so with
/// <see cref="OperationResultExtensions.EnsureComplete{T}"/>.
/// </summary>
public sealed class TruncatedResultException : Exception
{
    internal TruncatedResultException()
        : base("The result does not hold everything: a limit cut a list, or the input was not read whole")
    {
    }
}

internal static class ErrorCodes
{
    internal const string BadArgument = "OPS-BAD-ARGUMENT";
    internal const string UnknownOperation = "OPS-UNKNOWN-OPERATION";
    internal const string UnknownZone = "OPS-UNKNOWN-ZONE";
    internal const string UnknownCode = "OPS-UNKNOWN-CODE";
    internal const string TooLarge = "OPS-TOO-LARGE";
    internal const string Internal = "OPS-INTERNAL";

    /// <summary>A code this version does not know gets the base class, as the contract can grow.</summary>
    internal static CalendrumException ToException(string code, string message) => code switch
    {
        BadArgument => new BadArgumentException(code, message),
        UnknownOperation => new UnknownOperationException(code, message),
        UnknownZone => new UnknownZoneException(code, message),
        UnknownCode => new UnknownCodeException(code, message),
        TooLarge => new TooLargeException(code, message),
        Internal => new InternalLibraryException(code, message),
        _ => new CalendrumException(code, message),
    };
}

using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace Calendrum.Internal;

/// <summary>The lists of a result: a copy behind a read-only view, so that neither a cast nor the list given to <c>init</c> changes them.</summary>
internal static class Frozen
{
    internal static IReadOnlyList<T> List<T>(IReadOnlyList<T> value)
    {
        ArgumentNullException.ThrowIfNull(value);
        if (value.Count == 0)
        {
            return ReadOnlyCollection<T>.Empty;
        }
        var copy = new T[value.Count];
        for (int index = 0; index < copy.Length; index++)
        {
            copy[index] = value[index];
        }
        return new ReadOnlyCollection<T>(copy);
    }
}

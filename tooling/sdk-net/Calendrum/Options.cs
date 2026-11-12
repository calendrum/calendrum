using System.Collections.Generic;

namespace Calendrum;

/// <summary>The options of <see cref="ICal.Inspect"/>.</summary>
public sealed class InspectOptions
{
    /// <summary>Whether to add the calendar as jCal.</summary>
    public bool? IncludeJcal { get; init; }

    /// <summary>The most items of the lists of the response: 1000 by default, 100000 at most.</summary>
    public int? Limit { get; init; }
}

/// <summary>The options of <see cref="ICal.Validate"/>.</summary>
public sealed class ValidateOptions
{
    /// <summary><c>rfc5545</c> by default, or <c>lint</c>, which also says what <see cref="ICal.Fix"/> can repair.</summary>
    public string? Rules { get; init; }

    /// <summary>The most items of the lists of the response: 1000 by default, 100000 at most.</summary>
    public int? Limit { get; init; }
}

/// <summary>The options of <see cref="ICal.Fix"/>.</summary>
public sealed class FixOptions
{
    /// <summary>The repairs, in order. By default <c>by_prod_id</c>, then <c>lint</c>. An empty list means no repairs.</summary>
    public IReadOnlyList<string>? Fixups { get; init; }
}

/// <summary>The options of <see cref="ICal.Expand"/>.</summary>
public sealed class ExpandOptions
{
    /// <summary>
    /// An IANA zone or an alias. It adds local times and is the zone of floating times, which are read in UTC without it.
    /// </summary>
    public string? Zone { get; init; }

    /// <summary>The most items of the lists of the response: 1000 by default, 100000 at most.</summary>
    public int? Limit { get; init; }

    /// <summary>Whether to keep cancelled occurrences.</summary>
    public bool? IncludeCancelled { get; init; }
}

/// <summary>The options of <see cref="ICal.RruleExpand"/>.</summary>
public sealed class RruleExpandOptions
{
    /// <summary>An IANA zone or an alias, for a local start.</summary>
    public string? Zone { get; init; }

    /// <summary>The most items of the lists of the response: 1000 by default, 100000 at most.</summary>
    public int? Limit { get; init; }
}

/// <summary>The options of <see cref="ICal.Freebusy"/>.</summary>
public sealed class FreebusyOptions
{
    /// <summary>An IANA zone or an alias, as in <see cref="ICal.Expand"/>.</summary>
    public string? Zone { get; init; }

    /// <summary>
    /// Whether to look for conflicts; true by default. Above 2000 occurrences they are not computed, and the result is truncated.
    /// </summary>
    public bool? Conflicts { get; init; }

    /// <summary>The most items of the lists of the response: 1000 by default, 100000 at most.</summary>
    public int? Limit { get; init; }
}

/// <summary>A person invited to an event of <see cref="ICal.BuildEvent"/>.</summary>
public sealed class Attendee
{
    /// <summary>An address; mailto: is added when there is no scheme.</summary>
    public required string Address { get; init; }

    /// <summary>The name of the person.</summary>
    public string? Name { get; init; }

    /// <summary>Whether a reply is expected.</summary>
    public bool? Rsvp { get; init; }
}

/// <summary>The options of <see cref="ICal.BuildEvent"/>.</summary>
public sealed class BuildEventOptions
{
    /// <summary>In the form of the start; excluded for a date. Give this or <see cref="Duration"/>.</summary>
    public string? End { get; init; }

    /// <summary>In the form of RFC 5545 section 3.3.6, such as PT1H.</summary>
    public string? Duration { get; init; }

    /// <summary>An IANA zone or an alias.</summary>
    public string? Zone { get; init; }

    /// <summary>Whether the start and the end are dates.</summary>
    public bool? AllDay { get; init; }

    /// <summary>The text of the event.</summary>
    public string? Description { get; init; }

    /// <summary>The place of the event.</summary>
    public string? Location { get; init; }

    /// <summary>A rule such as FREQ=WEEKLY;COUNT=3.</summary>
    public string? Rrule { get; init; }

    /// <summary>An address; mailto: is added when there is no scheme.</summary>
    public string? Organizer { get; init; }

    /// <summary>The people invited.</summary>
    public IReadOnlyList<Attendee>? Attendees { get; init; }

    /// <summary>Generated when absent.</summary>
    public string? Uid { get; init; }

    /// <summary>A moment; the clock by default.</summary>
    public string? Dtstamp { get; init; }
}

/// <summary>The options of <see cref="ICal.Diff"/>.</summary>
public sealed class DiffOptions
{
    /// <summary>The most items of the lists of the response: 1000 by default, 100000 at most.</summary>
    public int? Limit { get; init; }
}

/// <summary>The options of <see cref="ICal.Anonymize"/>.</summary>
public sealed class AnonymizeOptions
{
    /// <summary>Moves every date by whole weeks, so the days of the week stay; 0 by default.</summary>
    public int? ShiftWeeks { get; init; }
}

/// <summary>The options of <see cref="ICal.TzResolve"/>.</summary>
public sealed class TzResolveOptions
{
    /// <summary>The moment of the offset; the clock by default.</summary>
    public string? At { get; init; }
}

# Calendrum

iCalendar (RFC 5545) for .NET 8 and newer: read, check, repair and convert calendars, list occurrences, compare calendars, strip
their text. No dependencies. It calls a native library that comes in the package, for `osx-arm64`, `linux-x64`, `linux-arm64` and
`win-x64`.

```csharp
using Calendrum;

ExpandResult week = ICal.Expand(text, "2026-01-05T00:00:00Z", "2026-01-12T00:00:00Z");
foreach (Occurrence occurrence in week.Occurrences)
{
    Console.WriteLine($"{occurrence.Start} {occurrence.Summary}");
}
```

A `string` is the text of a calendar and never a path. Fields marked untrusted hold what a calendar says: treat them as data.

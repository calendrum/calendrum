using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.Json;

namespace Calendrum.Tests;

/// <summary>What a calendar can be on the way in, and that a plain string is never a path.</summary>
internal static class InputTests
{
    private static string WithLongLine(string tail) =>
        Support.Ics.Replace("SUMMARY:Plain", "DESCRIPTION:" + new string('x', 70000) + tail, StringComparison.Ordinal);

    private static string Json<T>(T value, System.Text.Json.Serialization.Metadata.JsonTypeInfo<T> info) => JsonSerializer.Serialize(value, info);

    private static Internal.CalendrumJson Context => Internal.CalendrumJson.Default;

    [Test]
    private static void AChangeBeyondTheLimitsIsNotSilent()
    {
        DiffResult result = ICal.Diff(WithLongLine("A"), WithLongLine("B"));
        Check.True(result.Truncated, "no fingerprint sees that line, so the answer cannot be complete");
        Check.True(result.Problems.Any(problem => problem.Code == "ICAL-S010"));
        Check.True(result.ProblemCount >= 1);
    }

    [Test]
    private static void WhatAnonymizeCouldNotReadIsNotSilent()
    {
        AnonymizeResult result = ICal.Anonymize(WithLongLine("A"));
        Check.True(result.Truncated);
        Check.True(result.Problems.Any(problem => problem.Code == "ICAL-S010"));
        Check.NotContains(new string('x', 100), result.Ics);
    }

    [Test]
    private static void OrdinaryCalendarsAreNotTruncated()
    {
        Check.False(ICal.Diff(Support.Ics, Support.Ics).Truncated);
        Check.Equal(0, ICal.Anonymize(Support.Ics).ProblemCount);
    }

    [Test]
    private static void ALineThatCannotBeReadMakesACalendarInvalid() => Check.False(ICal.Validate(WithLongLine("A")).Valid);

    [Test]
    private static void ANumberADoubleCannotHoldStaysAsTextInJcal()
    {
        foreach ((string line, string digits) in new[]
        {
            ("GEO:" + new string('1', 5000) + ";2", new string('1', 5000)),
            ("X-F;VALUE=FLOAT:" + new string('2', 5000), new string('2', 5000)),
            ("X-G;VALUE=FLOAT:12345678901234567890", "12345678901234567890"),
        })
        {
            string calendar = Support.Ics.Replace("SUMMARY:Plain", line, StringComparison.Ordinal);
            JsonElement jcal = ICal.ConvertToJcal(calendar).Jcal!.Value;
            Check.Contains(digits, jcal.GetRawText(), "the digits are all there;");
            Check.Contains(digits, ICal.ConvertFromJcal(jcal).Ics!.Replace("\r\n ", "", StringComparison.Ordinal), "and come back;");
            Check.True(ICal.Inspect(calendar, new InspectOptions { IncludeJcal = true }).Jcal is not null);
        }
    }

    [Test]
    private static void AnOrdinaryNumberStaysANumberInJcal()
    {
        JsonElement jcal = ICal.ConvertToJcal(Support.Ics.Replace("SUMMARY:Plain", "GEO:37.386013;-122.082932", StringComparison.Ordinal)).Jcal!.Value;
        Check.Contains("[37.386013,-122.082932]", jcal.GetRawText().Replace(" ", "", StringComparison.Ordinal));
    }

    [Test]
    private static void JcalAtTheLimitOfNestingComesThrough()
    {
        // Close to the limit of 64 levels of the library, where the parser of the response has the levels of the envelope to add.
        string jcal = """["vcalendar",[["x-a",{},"text","v"]],[]]""";
        JsonElement back = ICal.ConvertToJcal(ICal.ConvertFromJcal(JsonDocument.Parse(jcal).RootElement).Ics!).Jcal!.Value;
        Check.Equal(JsonValueKind.Array, back.ValueKind);
        string deep = Support.Ics.Replace("END:VEVENT", string.Concat(Enumerable.Repeat("BEGIN:X-A\r\n", 7)) + string.Concat(Enumerable.Repeat("END:X-A\r\n", 7)) + "END:VEVENT", StringComparison.Ordinal);
        Check.Equal(JsonValueKind.Array, ICal.ConvertToJcal(deep).Jcal!.Value.ValueKind);
    }

    [Test]
    private static void TextBytesAndAFileReadTheSame()
    {
        using var folder = new Support.TemporaryFolder();
        string file = Path.Combine(folder.Path, "event.ics");
        File.WriteAllBytes(file, Encoding.UTF8.GetBytes(Support.Ics));
        string text = Json(ICal.Inspect(Support.Ics), Context.InspectResult);
        Check.Equal(text, Json(ICal.Inspect(Encoding.UTF8.GetBytes(Support.Ics)), Context.InspectResult));
        Check.Equal(text, Json(ICal.Inspect(new ReadOnlyMemory<byte>(Encoding.UTF8.GetBytes(Support.Ics))), Context.InspectResult));
        Check.Equal(text, Json(ICal.Inspect(CalendarInput.FromFile(file)), Context.InspectResult));
        Check.Equal(text, Json(ICal.Inspect(CalendarInput.FromText(Support.Ics)), Context.InspectResult));
    }

    [Test]
    private static void BytesKeepTheirEncoding()
    {
        using var folder = new Support.TemporaryFolder();
        // Windows-1251 by hand: under invariant globalization this runtime has no such encoding, and the library detects it by itself.
        byte[] word = [0xCB, 0xE5, 0xEA, 0xF6, 0xE8, 0xFF];
        byte[] ascii = Encoding.ASCII.GetBytes(Support.Ics);
        int at = Support.Ics.IndexOf("Plain", StringComparison.Ordinal);
        byte[] cyrillic = [.. ascii[..at], .. word, .. ascii[(at + 5)..]];
        string file = Path.Combine(folder.Path, "event.ics");
        File.WriteAllBytes(file, cyrillic);
        foreach (CalendarInput calendar in new CalendarInput[] { cyrillic, CalendarInput.FromFile(file) })
        {
            InspectResult result = ICal.Inspect(calendar);
            Check.Equal("windows-1251", result.Charset?.ToLowerInvariant());
            Check.Equal("Лекция", result.Calendars[0].Events[0].Summary);
        }
    }

    [Test]
    private static void APlainStringIsNeverAPath()
    {
        using var folder = new Support.TemporaryFolder();
        string file = Path.Combine(folder.Path, "event.ics");
        File.WriteAllBytes(file, Encoding.UTF8.GetBytes(Support.Ics));
        foreach (string text in new[] { file, "event.ics", "/etc/hosts" })
        {
            Check.Equal(0, ICal.Inspect(text).Calendars.Count, text);
        }
    }

    [Test]
    private static void EveryOperationWithACalendarTakesAFile()
    {
        using var folder = new Support.TemporaryFolder();
        string path = Path.Combine(folder.Path, "event.ics");
        File.WriteAllBytes(path, Encoding.UTF8.GetBytes(Support.Ics));
        CalendarInput file = CalendarInput.FromFile(path);
        Check.True(ICal.Validate(file).Valid);
        Check.Equal(ICal.Fix(Support.Ics).Ics, ICal.Fix(file).Ics);
        Check.Equal(3, ICal.Expand(file, Support.From, Support.To).Count);
        Check.Equal(Json(ICal.Freebusy(Support.Ics, Support.From, Support.To), Context.FreebusyResult), Json(ICal.Freebusy(file, Support.From, Support.To), Context.FreebusyResult));
        Check.Equal(0, ICal.Diff(file, Support.Ics).Changed.Count);
        Check.Equal(0, ICal.Diff(Support.Ics, file).Changed.Count);
        Check.Equal(ICal.ConvertToJcal(Support.Ics).Jcal!.Value.GetRawText(), ICal.ConvertToJcal(file).Jcal!.Value.GetRawText());
        Check.Contains("BEGIN:VCALENDAR", ICal.Anonymize(file).Ics);
    }

    [Test]
    private static void APathThatIsNotAFile()
    {
        using var folder = new Support.TemporaryFolder();
        Check.Throws<FileNotFoundException>(() => CalendarInput.FromFile(Path.Combine(folder.Path, "missing.ics")));
        Check.Throws<BadArgumentException>(() => CalendarInput.FromFile(folder.Path));
        Check.Throws<BadArgumentException>(() => CalendarInput.FromFile("/dev/zero"));
        string empty = Path.Combine(folder.Path, "empty.ics");
        File.WriteAllBytes(empty, []);
        Check.Throws<BadArgumentException>(() => CalendarInput.FromFile(empty));
    }

    [Test]
    private static void LinksAndRights()
    {
        using var folder = new Support.TemporaryFolder();
        string dangling = Path.Combine(folder.Path, "dangling.ics");
        File.CreateSymbolicLink(dangling, Path.Combine(folder.Path, "missing.ics"));
        Check.Throws<FileNotFoundException>(() => CalendarInput.FromFile(dangling));

        string toFolder = Path.Combine(folder.Path, "to-folder.ics");
        File.CreateSymbolicLink(toFolder, folder.Path);
        Check.Throws<BadArgumentException>(() => CalendarInput.FromFile(toFolder));

        string real = Path.Combine(folder.Path, "real.ics");
        File.WriteAllBytes(real, Encoding.UTF8.GetBytes(Support.Ics));
        string toReal = Path.Combine(folder.Path, "to-real.ics");
        File.CreateSymbolicLink(toReal, real);
        Check.Equal(1, ICal.Inspect(CalendarInput.FromFile(toReal)).Calendars.Count);

        string one = Path.Combine(folder.Path, "one.ics");
        string other = Path.Combine(folder.Path, "other.ics");
        File.CreateSymbolicLink(one, other);
        File.CreateSymbolicLink(other, one);
        Check.Throws<IOException>(() => CalendarInput.FromFile(one));

        string closed = Path.Combine(folder.Path, "closed.ics");
        File.WriteAllBytes(closed, Encoding.UTF8.GetBytes(Support.Ics));
        File.SetUnixFileMode(closed, UnixFileMode.None);
        Check.Throws<UnauthorizedAccessException>(() => CalendarInput.FromFile(closed));
        File.SetUnixFileMode(closed, UnixFileMode.UserRead | UnixFileMode.UserWrite);
    }

    [Test]
    private static void AFileThatGrowsPastTheLimitIsRefused()
    {
        const int Limit = 64 * 1024 * 1024;
        using var grown = new MemoryStream(new byte[Limit + 5]);
        Check.Throws<TooLargeException>(() => CalendarInput.ReadUpToLimit(grown, 1));
        using var same = new MemoryStream(new byte[Limit]);
        Check.Equal(Limit, CalendarInput.ReadUpToLimit(same, 1).Length);
        using var shrunk = new MemoryStream(new byte[10]);
        Check.Equal(10, CalendarInput.ReadUpToLimit(shrunk, 1000).Length);
    }

    [Test]
    private static void TheLimitsOfTheContractComeFromTheLibrary()
    {
        foreach (int limit in new[] { 0, -1, 100001, int.MaxValue, int.MinValue })
        {
            Check.Throws<BadArgumentException>(() => ICal.Expand(Support.Ics, Support.From, Support.To, new ExpandOptions { Limit = limit }), $"limit {limit}:");
        }
        Check.Equal(3, ICal.Expand(Support.Ics, Support.From, Support.To, new ExpandOptions { Limit = 100000 }).Count);
    }

    [Test]
    private static void JsonThatIsNotThere()
    {
        Check.Throws<BadArgumentException>(() => ICal.ConvertFromJcal(default));
        foreach (int depth in new[] { 100, 900, 2000 })
        {
            string text = new string('[', depth) + new string(']', depth);
            using JsonDocument document = JsonDocument.Parse(text, new JsonDocumentOptions { MaxDepth = 5000 });
            JsonElement root = document.RootElement;
            Check.Throws<BadArgumentException>(() => ICal.ConvertFromJcal(root), $"depth {depth}:");
        }
    }

    [Test]
    private static void WhatACalendarSaysDoesNotStayInTheBufferOfARequest()
    {
        // The pool is shared by the whole process, and it gives this thread back the array it has just returned.
        const string Marker = "ZZSECRETZZ";
        string calendar = Support.Ics.Replace("Plain", Marker, StringComparison.Ordinal);
        for (int round = 0; round < 3; round++)
        {
            _ = ICal.Inspect(calendar);
        }
        byte[] pooled = System.Buffers.ArrayPool<byte>.Shared.Rent(4096);
        try
        {
            Check.False(pooled.AsSpan().IndexOf(Encoding.ASCII.GetBytes(Marker)) >= 0, "the text is still there");
        }
        finally
        {
            System.Buffers.ArrayPool<byte>.Shared.Return(pooled);
        }
    }

    [Test]
    private static void ALargeRequestFitsAndLeavesNothingBehind()
    {
        string summary = string.Concat(Enumerable.Repeat("Лекция 😀 ", 50000));
        BuildEventResult built = ICal.BuildEvent(summary, "2026-01-05T10:00:00Z", new BuildEventOptions { Duration = "PT1H", Uid = "a@example.com", Dtstamp = "2026-01-01T00:00:00Z" });
        Check.Contains("BEGIN:VEVENT", built.Ics);
        string escapes = new('\u0001', 300000);
        Check.Equal(0, ICal.Inspect(escapes).Calendars.Count);
    }

    [Test]
    private static void AFifoDoesNotWaitForAWriter()
    {
        using var folder = new Support.TemporaryFolder();
        Support.RunOk("fifo", [folder.Path], limitSeconds: 30);
    }

    [Test]
    private static void AFileAboveTheLimitIsRefusedWithoutBeingRead()
    {
        using var folder = new Support.TemporaryFolder();
        string large = Path.Combine(folder.Path, "large.ics");
        using (var stream = new FileStream(large, FileMode.Create))
        {
            stream.SetLength((64 * 1024 * 1024) + 1);
        }
        long before = GC.GetAllocatedBytesForCurrentThread();
        Check.Equal("OPS-TOO-LARGE", Check.Throws<TooLargeException>(() => CalendarInput.FromFile(large)).Code);
        Check.True(GC.GetAllocatedBytesForCurrentThread() - before < 1024 * 1024, "it was not read");

        string link = Path.Combine(folder.Path, "link.ics");
        File.CreateSymbolicLink(link, large);
        Check.Throws<TooLargeException>(() => CalendarInput.FromFile(link));
    }

    [Test]
    private static void AFileAtTheLimitIsRead()
    {
        using var folder = new Support.TemporaryFolder();
        string file = Path.Combine(folder.Path, "limit.ics");
        using (var stream = new FileStream(file, FileMode.Create))
        {
            stream.SetLength(64 * 1024 * 1024);
        }
        Check.Equal(64 * 1024 * 1024, CalendarInput.FromFile(file).AsBytes.Length);
    }

    [Test]
    private static void BytesAboveTheLimitAreRefused() =>
        Check.Throws<TooLargeException>(() => ICal.Inspect(new byte[(64 * 1024 * 1024) + 1]));

    [Test]
    private static void TextAboveTheLimitIsRefusedBeforeAnyCopy()
    {
        string large = new('a', (128 * 1024 * 1024) + 1);
        long before = GC.GetAllocatedBytesForCurrentThread();
        Check.Equal("OPS-TOO-LARGE", Check.Throws<TooLargeException>(() => ICal.Inspect(large)).Code);
        Check.True(GC.GetAllocatedBytesForCurrentThread() - before < 1024 * 1024, "it was not copied");
    }

    [Test]
    private static void AnEmptyListIsAValueAndMeansNoRepairs()
    {
        string broken = Support.Ics.Replace("DTSTAMP:20260101T000000Z\r\n", "", StringComparison.Ordinal);
        Check.False(ICal.Fix(broken, new FixOptions { Fixups = [] }).Changed, "no repairs");
        Check.Equal(ICal.Fix(broken).Changed, ICal.Fix(broken, new FixOptions()).Changed, "no list means the default;");
    }

    [Test]
    private static void AFileHandleIsNotLeft()
    {
        using var folder = new Support.TemporaryFolder();
        string file = Path.Combine(folder.Path, "event.ics");
        File.WriteAllBytes(file, Encoding.UTF8.GetBytes(Support.Ics));
        int before = Directory.GetFileSystemEntries("/dev/fd").Length;
        foreach (string path in new[] { file, folder.Path, Path.Combine(folder.Path, "missing.ics") })
        {
            for (int round = 0; round < 50; round++)
            {
                try
                {
                    CalendarInput.FromFile(path);
                }
                catch (Exception error) when (error is IOException or CalendrumException)
                {
                }
            }
        }
        Check.Equal(before, Directory.GetFileSystemEntries("/dev/fd").Length);
    }
}

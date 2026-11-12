using System;
using System.IO;
using Calendrum.Internal;

namespace Calendrum;

/// <summary>
/// A calendar to read: text or bytes. A <see cref="string"/> converts to this as the text of the calendar, and is never taken for a
/// path. A file needs <see cref="FromFile"/>.
/// </summary>
public sealed class CalendarInput
{
    private CalendarInput(string? text, ReadOnlyMemory<byte> bytes)
    {
        AsText = text;
        AsBytes = bytes;
    }

    internal string? AsText { get; }

    internal ReadOnlyMemory<byte> AsBytes { get; }

    /// <summary>A calendar as text.</summary>
    /// <param name="text">The text of the calendar, and not a path.</param>
    /// <exception cref="ArgumentNullException"><paramref name="text"/> is null.</exception>
    public static CalendarInput FromText(string text)
    {
        ArgumentNullException.ThrowIfNull(text);
        return new CalendarInput(text, default);
    }

    /// <summary>A calendar as bytes, in any encoding the library detects. The bytes are not copied until the call.</summary>
    /// <param name="bytes">The bytes of the calendar.</param>
    public static CalendarInput FromBytes(ReadOnlyMemory<byte> bytes) => new(null, bytes);

    /// <summary>
    /// Reads a file now. A file larger than the limit of 64 MiB is refused without reading it. Symbolic links are followed, so do not
    /// pass a path that comes from a stranger.
    /// </summary>
    /// <param name="path">The path of a regular file.</param>
    /// <exception cref="ArgumentNullException"><paramref name="path"/> is null.</exception>
    /// <exception cref="ArgumentException"><paramref name="path"/> is empty or holds a character that no path can hold.</exception>
    /// <exception cref="BadArgumentException">The path is not a regular file, or the file is empty.</exception>
    /// <exception cref="TooLargeException">The file is larger than the limit.</exception>
    /// <exception cref="FileNotFoundException">There is no such file.</exception>
    /// <exception cref="IOException">The file could not be read, or a folder on the way does not exist.</exception>
    /// <exception cref="UnauthorizedAccessException">The file may not be read.</exception>
    public static CalendarInput FromFile(string path)
    {
        ArgumentNullException.ThrowIfNull(path);
        return new CalendarInput(null, ReadFile(path));
    }

    /// <summary>The text of a calendar, and not a path; see <see cref="FromText"/>.</summary>
    /// <param name="text">The text of the calendar.</param>
    public static implicit operator CalendarInput(string text) => FromText(text);

    /// <summary>The bytes of a calendar; see <see cref="FromBytes"/>.</summary>
    /// <param name="bytes">The bytes of the calendar.</param>
    public static implicit operator CalendarInput(byte[] bytes)
    {
        ArgumentNullException.ThrowIfNull(bytes);
        return FromBytes(bytes);
    }

    /// <summary>The bytes of a calendar; see <see cref="FromBytes"/>.</summary>
    /// <param name="bytes">The bytes of the calendar.</param>
    public static implicit operator CalendarInput(ReadOnlyMemory<byte> bytes) => FromBytes(bytes);

    private static byte[] ReadFile(string path)
    {
        // Opening a FIFO for reading waits for a writer forever, and .NET has no way to open it without waiting or to ask what a path is.
        // But the size of what is not a regular file is 0, so nothing of size 0 is opened. An empty file holds no calendar anyway.
        var link = new FileInfo(path);
        FileInfo info = link.ResolveLinkTarget(returnFinalTarget: true) as FileInfo ?? link;
        if (Directory.Exists(info.FullName))
        {
            throw new BadArgumentException(ErrorCodes.BadArgument, "The path is not a regular file");
        }
        if (!info.Exists)
        {
            throw new FileNotFoundException("There is no such file", path);
        }
        if (info.Length == 0)
        {
            throw new BadArgumentException(ErrorCodes.BadArgument, "The file is empty, or the path is not a regular file");
        }
        if (info.Length > RequestWriter.CalendarLimit)
        {
            throw new TooLargeException(ErrorCodes.TooLarge, "The calendar is larger than the limit");
        }

        using var stream = new FileStream(info.FullName, FileMode.Open, FileAccess.Read, FileShare.ReadWrite | FileShare.Delete, bufferSize: 0);
        if (!stream.CanSeek)
        {
            throw new BadArgumentException(ErrorCodes.BadArgument, "The path is not a regular file");
        }
        return ReadUpToLimit(stream, info.Length);
    }

    /// <summary>Reads to the limit and not to the size that was seen: a file can grow while it is read.</summary>
    internal static byte[] ReadUpToLimit(Stream stream, long expected)
    {
        var buffer = new byte[Math.Min(expected + 1, RequestWriter.CalendarLimit + 1L)];
        int total = 0;
        while (true)
        {
            int read = stream.Read(buffer, total, buffer.Length - total);
            if (read == 0)
            {
                return total == buffer.Length ? buffer : buffer.AsSpan(0, total).ToArray();
            }
            total += read;
            if (total > RequestWriter.CalendarLimit)
            {
                throw new TooLargeException(ErrorCodes.TooLarge, "The calendar is larger than the limit");
            }
            if (total == buffer.Length)
            {
                Array.Resize(ref buffer, Math.Min(buffer.Length * 2, RequestWriter.CalendarLimit + 1));
            }
        }
    }
}

package io.github.calendrum.tooling.platform

/** What came of reading a file that a person named on the command line. */
public sealed class UserFileRead {
    /** The bytes of the file. */
    public class Bytes(public val bytes: ByteArray) : UserFileRead()

    /**
     * The file could not be read.
     *
     * @property reason a short sentence from the system, such as "No such file or directory", fit to show to the person
     *   who named the file. It never holds the name.
     */
    public class Failed(public val reason: String) : UserFileRead()

    /** A regular file above the limit. */
    public object TooLarge : UserFileRead()
}

/** What came of writing a file. */
public sealed class FileWrite {
    /** The file is in place. */
    public object Done : FileWrite()

    /** A file was already there, and it was not to be replaced. Nothing was changed. */
    public object Exists : FileWrite()

    /**
     * The file was not written, and a file that was there is as it was.
     *
     * @property reason a short sentence from the system, such as "Permission denied".
     */
    public class Failed(public val reason: String) : FileWrite()
}

/**
 * Reads the regular file at [path], at most [maxBytes] of it.
 *
 * This does what a person asks for, so there are no roots and no extensions, and a symbolic link is followed. It still
 * never waits: a FIFO or a device is [UserFileRead.Failed], and so is a directory. Do not use it for a path that comes from
 * somebody else: [FileAccess.readInside] is the function for that.
 *
 * On Windows the path is passed to the system in UTF-16.
 */
public expect fun readUserFile(path: String, maxBytes: Int): UserFileRead

/**
 * Writes [bytes] to [path] so that a reader sees the old file or the new one, never half of it, and so that a failure
 * leaves the old file as it was.
 *
 * The bytes go to a temporary file in the same directory, are flushed to the disk, and then take the place of [path]: by
 * `rename` on POSIX, by `MoveFileExW` with `MOVEFILE_WRITE_THROUGH` on Windows. The temporary file is removed on failure.
 *
 * @param replace whether a file at [path] may be replaced. If it may not, and one is there, the answer is
 *   [FileWrite.Exists], also if it appeared during the call. If it may, a symbolic link at [path] is followed, and the new
 *   file keeps the permissions of the old one.
 */
public expect fun writeFileAtomically(path: String, bytes: ByteArray, replace: Boolean): FileWrite

/** The characters that separate the components of a path on this platform: only `/` on POSIX, where `\` is a name. */
internal expect val PATH_SEPARATORS: CharArray

/** The directory part of [path] as the system writes it, `.` if there is none. */
internal fun directoryOf(path: String): String {
    val cut = path.lastIndexOfAny(PATH_SEPARATORS)
    return when {
        cut < 0 -> "."
        cut == 0 -> path.substring(0, 1)
        else -> path.substring(0, cut)
    }
}

/** The last component of [path], cut to a length that leaves room for the rest of a temporary name. */
internal fun temporaryStem(path: String): String {
    val name = path.substring(path.lastIndexOfAny(PATH_SEPARATORS) + 1)
    return if (name.length > MAX_STEM) name.substring(0, MAX_STEM) else name
}

private const val MAX_STEM = 64

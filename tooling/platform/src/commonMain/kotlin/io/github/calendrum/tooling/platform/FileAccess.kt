package io.github.calendrum.tooling.platform

/** What came of reading a file. */
public sealed class FileRead {
    /** The bytes of the file. */
    public class Bytes(public val bytes: ByteArray) : FileRead()

    /** The file does not exist, is not allowed, or is not a regular file. They cannot be told apart, on purpose. */
    public object Denied : FileRead()

    /** A regular file above the limit. */
    public object TooLarge : FileRead()
}

/**
 * Reads files on behalf of somebody who is not trusted with the whole disk.
 *
 * Text from a calendar can talk a model into asking for another file, so the boundary is here and not in the model. A
 * path is resolved first and judged afterwards, by what it really points at.
 */
public object FileAccess {
    /** The extensions of calendar files. */
    public val CALENDAR_EXTENSIONS: Set<String> = setOf(".ics", ".ical", ".ifb", ".vcs")

    /** Whether this platform can check paths. Where it cannot, nothing is read. */
    public val supported: Boolean get() = FILES_SUPPORTED

    /** The real path of the working directory, or `null` if it has none. */
    public fun workingDirectory(): String? = realPath(".")

    /** The path with symbolic links and `..` resolved, or `null` if it does not exist or is not valid UTF-8. */
    public fun realPath(path: String): String? {
        if (path.isEmpty() || '\u0000' in path) return null
        return realPathOf(path)
    }

    /**
     * Reads the file at [path] if it is a regular file inside one of [realRoots] with one of [extensions].
     *
     * @param path an absolute path, or one relative to the working directory.
     * @param realRoots real paths, as [realPath] gives them.
     * @param extensions in lower case, with the dot. They are looked for in the real path, so that `x.ics` linking
     *   to `id_rsa` does not pass.
     * @param maxBytes the largest file. No more than this is read, even of a file that is growing.
     */
    public fun readInside(path: String, realRoots: List<String>, extensions: Set<String>, maxBytes: Int): FileRead {
        val real = realPath(path) ?: return FileRead.Denied
        if (realRoots.none { isInside(real, it) }) return FileRead.Denied
        if (!hasExtension(real, extensions)) return FileRead.Denied
        return readRegularFile(real, maxBytes)
    }
}

/**
 * Reads a regular file that is not a symbolic link, at most [maxBytes] of it, without ever waiting: a FIFO or a device
 * is [FileRead.Denied].
 */
public expect fun readRegularFile(path: String, maxBytes: Int): FileRead

internal expect val FILES_SUPPORTED: Boolean

internal expect fun realPathOf(path: String): String?

/** Whether the real path [path] is [root] or lies under it, by components: `/a/bc` is not inside `/a/b`. */
public fun isInside(path: String, root: String): Boolean {
    if (path == root) return true
    val prefix = root.trimEnd('/')
    return path.length > prefix.length && path[prefix.length] == '/' && path.startsWith(prefix)
}

/**
 * Whether the last component of [path] ends with one of [extensions], in any case.
 *
 * Only ASCII letters fold. The case folding of the standard library would take `.icſ` for `.ics`, as `ſ` has the
 * upper case `S`.
 */
public fun hasExtension(path: String, extensions: Set<String>): Boolean {
    val name = path.substringAfterLast('/').lowerCaseAscii()
    return extensions.any { name.endsWith(it) }
}

/**
 * The path of a `file://` URI of the root of a client, with percent escapes decoded, or `null` for anything else.
 *
 * Only `file:///path` and `file://localhost/path` count. A query, a fragment, a bad escape, a control character or
 * bytes that are not UTF-8 make the URI unreadable, and an unreadable root does not exist.
 */
public fun pathOfFileUri(uri: String): String? {
    if (uri.length < FILE_SCHEME.length) return null
    if (uri.substring(0, FILE_SCHEME.length).lowerCaseAscii() != FILE_SCHEME) return null
    val rest = uri.substring(FILE_SCHEME.length)
    val slash = rest.indexOf('/')
    if (slash < 0) return null
    val authority = rest.substring(0, slash).lowerCaseAscii()
    if (authority.isNotEmpty() && authority != "localhost") return null
    val encoded = rest.substring(slash)
    if ('?' in encoded || '#' in encoded) return null
    val bytes = decodePercents(encoded) ?: return null
    if (bytes.any { it.toInt() in 0 until 0x20 }) return null
    return try {
        bytes.decodeToString(throwOnInvalidSequence = true)
    } catch (_: CharacterCodingException) {
        null
    }
}

private const val FILE_SCHEME = "file://"

private fun decodePercents(text: String): ByteArray? {
    val raw = text.encodeToByteArray()
    val out = ByteArray(raw.size)
    var size = 0
    var index = 0
    while (index < raw.size) {
        val byte = raw[index]
        if (byte == '%'.code.toByte()) {
            if (index + 2 >= raw.size) return null
            val high = hexDigit(raw[index + 1])
            val low = hexDigit(raw[index + 2])
            if (high < 0 || low < 0) return null
            out[size++] = (high * 16 + low).toByte()
            index += 3
        } else {
            out[size++] = byte
            index++
        }
    }
    return out.copyOf(size)
}

private fun hexDigit(byte: Byte): Int = when (val char = byte.toInt().toChar()) {
    in '0'..'9' -> char - '0'
    in 'a'..'f' -> char - 'a' + 10
    in 'A'..'F' -> char - 'A' + 10
    else -> -1
}

private fun String.lowerCaseAscii(): String {
    if (none { it in 'A'..'Z' }) return this
    return buildString(length) {
        for (char in this@lowerCaseAscii) append(if (char in 'A'..'Z') char + ('a' - 'A') else char)
    }
}

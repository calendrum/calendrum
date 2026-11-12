package io.github.calendrum.syntax

// Encodings the input decoder knows, by canonical name (the one `java.nio.charset` also accepts). `Utf16` and `Utf32`
// without a byte order exist only as hints; the data picks the order.
internal enum class InputCharset(val canonicalName: String) {
    Utf8("UTF-8"),
    Utf16Le("UTF-16LE"),
    Utf16Be("UTF-16BE"),
    Utf32Le("UTF-32LE"),
    Utf32Be("UTF-32BE"),
    Windows1251("windows-1251"),
    Koi8R("KOI8-R"),
    Windows1252("windows-1252"),
    Utf16("UTF-16"),
    Utf32("UTF-32"),
    ;

    val isSingleByte: Boolean get() = ordinal in Windows1251.ordinal..Windows1252.ordinal

    val isWide: Boolean get() = !isSingleByte && this != Utf8
}

// Hint labels, normalized (lower case, no '-', '_', '.', ':', spaces or quotes), one group per `InputCharset` entry in
// declaration order, groups separated by '|'. Sources: the IANA charset registry, the WHATWG Encoding Standard and the
// aliases of `java.nio.charset`, only for the encodings we decode. `iso-10646-ucs-2` means big-endian to the JDK and
// little-endian to WHATWG, so it is the unmarked UTF-16 here. The ISO-8859-1 and US-ASCII labels are in the
// windows-1252 group, as in WHATWG: servers send windows-1252 under them.
private const val LABELS: String =
    "utf8 unicode11utf8 unicode20utf8 xunicode20utf8|" +
        "utf16le xutf16le unicodelittleunmarked|" +
        "utf16be xutf16be unicodebigunmarked|" +
        "utf32le xutf32le|" +
        "utf32be xutf32be|" +
        "windows1251 cp1251 xcp1251 cswindows1251 ansi1251 win1251 cp5347|" +
        "koi8r koi8 koi cskoi8r|" +
        "windows1252 cp1252 xcp1252 cswindows1252 ansi1252 win1252 ibm1252 cp5348 " +
        "iso88591 88591 iso885911987 isoir100 latin1 l1 ibm819 cp819 csisolatin1 " +
        "usascii ascii us isoir6 ansix341968 ansix341986 iso646irv1991 iso646us ibm367 cp367 csascii ascii7 646|" +
        "utf16 unicode ucs2 iso10646ucs2 csunicode|" +
        "utf32 ucs4 iso10646ucs4"

private const val MAX_LABEL_LENGTH = 40

// Accepts a bare label or a whole Content-Type value, from which the `charset` parameter is taken. Any other character
// than ASCII letters, digits and the ignored punctuation makes the label unknown.
internal fun charsetForLabel(label: String): InputCharset? {
    val value = charsetParameter(label)
    val key = CharArray(MAX_LABEL_LENGTH)
    var length = 0
    for (char in value) {
        val normalized = when (char) {
            in 'a'..'z', in '0'..'9' -> char
            in 'A'..'Z' -> char + ('a' - 'A')
            '-', '_', '.', ':', ' ', '\t', '"', '\'' -> continue
            else -> return null
        }
        if (length == MAX_LABEL_LENGTH) return null
        key[length++] = normalized
    }
    if (length == 0) return null
    var group = 0
    var start = 0
    while (start < LABELS.length) {
        var end = start
        while (end < LABELS.length && LABELS[end] != ' ' && LABELS[end] != '|') end++
        if (end - start == length && matches(start, key, length)) return InputCharset.entries[group]
        if (end < LABELS.length && LABELS[end] == '|') group++
        start = end + 1
    }
    return null
}

private fun matches(start: Int, key: CharArray, length: Int): Boolean {
    for (k in 0 until length) {
        if (LABELS[start + k] != key[k]) return false
    }
    return true
}

private fun charsetParameter(label: String): String {
    if (label.indexOf('=') < 0) return label
    var index = 0
    while (true) {
        index = indexOfCharset(label, index)
        if (index < 0) return label
        var i = index + CHARSET_PARAMETER_LENGTH
        while (i < label.length && (label[i] == ' ' || label[i] == '\t')) i++
        if (i < label.length && label[i] == '=') {
            val end = label.indexOf(';', i + 1)
            return label.substring(i + 1, if (end < 0) label.length else end)
        }
        index = i
    }
}

private const val CHARSET_PARAMETER = "charset"
private const val CHARSET_PARAMETER_LENGTH = 7
private const val ASCII_CASE_BIT = 0x20

// The `charset` keyword is found by ASCII case folding (`or 0x20` on the characters, which cannot turn a non-letter
// into a letter). `indexOf(..., ignoreCase = true)` folds by Unicode, which brings the case-mapping functions and
// tables of the standard library into the binary; the only difference is that U+017F (long s) no longer counts as `s`
// in the keyword.
private fun indexOfCharset(label: String, from: Int): Int {
    for (index in from..label.length - CHARSET_PARAMETER_LENGTH) {
        if (matchesCharset(label, index)) return index
    }
    return -1
}

private fun matchesCharset(label: String, index: Int): Boolean {
    for (k in 0 until CHARSET_PARAMETER_LENGTH) {
        if ((label[index + k].code or ASCII_CASE_BIT) != CHARSET_PARAMETER[k].code) return false
    }
    return true
}

// Decoding tables as data: 128 characters for bytes 0x80..0xFF per encoding, built on first use of the object. The
// strings were generated from `java.nio.charset` (JDK 21) and equal the Unicode mapping files; bytes undefined in the
// encoding (0x98 in windows-1251; 0x81, 0x8D, 0x8F, 0x90, 0x9D in windows-1252) decode to U+FFFD, as in the JDK, while
// WHATWG maps them to C1 controls.
internal object SingleByteCharsets {
    private const val WINDOWS_1251: String =
        "\u0402\u0403\u201A\u0453\u201E\u2026\u2020\u2021\u20AC\u2030\u0409\u2039\u040A\u040C\u040B\u040F" +
            "\u0452\u2018\u2019\u201C\u201D\u2022\u2013\u2014\uFFFD\u2122\u0459\u203A\u045A\u045C\u045B\u045F" +
            "\u00A0\u040E\u045E\u0408\u00A4\u0490\u00A6\u00A7\u0401\u00A9\u0404\u00AB\u00AC\u00AD\u00AE\u0407" +
            "\u00B0\u00B1\u0406\u0456\u0491\u00B5\u00B6\u00B7\u0451\u2116\u0454\u00BB\u0458\u0405\u0455\u0457" +
            "\u0410\u0411\u0412\u0413\u0414\u0415\u0416\u0417\u0418\u0419\u041A\u041B\u041C\u041D\u041E\u041F" +
            "\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042A\u042B\u042C\u042D\u042E\u042F" +
            "\u0430\u0431\u0432\u0433\u0434\u0435\u0436\u0437\u0438\u0439\u043A\u043B\u043C\u043D\u043E\u043F" +
            "\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044A\u044B\u044C\u044D\u044E\u044F"

    private const val KOI8_R: String =
        "\u2500\u2502\u250C\u2510\u2514\u2518\u251C\u2524\u252C\u2534\u253C\u2580\u2584\u2588\u258C\u2590" +
            "\u2591\u2592\u2593\u2320\u25A0\u2219\u221A\u2248\u2264\u2265\u00A0\u2321\u00B0\u00B2\u00B7\u00F7" +
            "\u2550\u2551\u2552\u0451\u2553\u2554\u2555\u2556\u2557\u2558\u2559\u255A\u255B\u255C\u255D\u255E" +
            "\u255F\u2560\u2561\u0401\u2562\u2563\u2564\u2565\u2566\u2567\u2568\u2569\u256A\u256B\u256C\u00A9" +
            "\u044E\u0430\u0431\u0446\u0434\u0435\u0444\u0433\u0445\u0438\u0439\u043A\u043B\u043C\u043D\u043E" +
            "\u043F\u044F\u0440\u0441\u0442\u0443\u0436\u0432\u044C\u044B\u0437\u0448\u044D\u0449\u0447\u044A" +
            "\u042E\u0410\u0411\u0426\u0414\u0415\u0424\u0413\u0425\u0418\u0419\u041A\u041B\u041C\u041D\u041E" +
            "\u041F\u042F\u0420\u0421\u0422\u0423\u0416\u0412\u042C\u042B\u0417\u0428\u042D\u0429\u0427\u042A"

    private const val WINDOWS_1252: String =
        "\u20AC\uFFFD\u201A\u0192\u201E\u2026\u2020\u2021\u02C6\u2030\u0160\u2039\u0152\uFFFD\u017D\uFFFD" +
            "\uFFFD\u2018\u2019\u201C\u201D\u2022\u2013\u2014\u02DC\u2122\u0161\u203A\u0153\uFFFD\u017E\u0178" +
            "\u00A0\u00A1\u00A2\u00A3\u00A4\u00A5\u00A6\u00A7\u00A8\u00A9\u00AA\u00AB\u00AC\u00AD\u00AE\u00AF" +
            "\u00B0\u00B1\u00B2\u00B3\u00B4\u00B5\u00B6\u00B7\u00B8\u00B9\u00BA\u00BB\u00BC\u00BD\u00BE\u00BF" +
            "\u00C0\u00C1\u00C2\u00C3\u00C4\u00C5\u00C6\u00C7\u00C8\u00C9\u00CA\u00CB\u00CC\u00CD\u00CE\u00CF" +
            "\u00D0\u00D1\u00D2\u00D3\u00D4\u00D5\u00D6\u00D7\u00D8\u00D9\u00DA\u00DB\u00DC\u00DD\u00DE\u00DF" +
            "\u00E0\u00E1\u00E2\u00E3\u00E4\u00E5\u00E6\u00E7\u00E8\u00E9\u00EA\u00EB\u00EC\u00ED\u00EE\u00EF" +
            "\u00F0\u00F1\u00F2\u00F3\u00F4\u00F5\u00F6\u00F7\u00F8\u00F9\u00FA\u00FB\u00FC\u00FD\u00FE\u00FF"

    private val windows1251: CharArray = WINDOWS_1251.toCharArray()
    private val koi8R: CharArray = KOI8_R.toCharArray()
    private val windows1252: CharArray = WINDOWS_1252.toCharArray()

    fun table(charset: InputCharset): CharArray = when (charset) {
        InputCharset.Windows1251 -> windows1251
        InputCharset.Koi8R -> koi8R
        InputCharset.Windows1252 -> windows1252
        else -> throw IllegalArgumentException("${charset.canonicalName} is not a single-byte encoding")
    }
}

internal const val HIGH_BYTES: Int = 128
internal const val REPLACEMENT: Char = '\uFFFD'

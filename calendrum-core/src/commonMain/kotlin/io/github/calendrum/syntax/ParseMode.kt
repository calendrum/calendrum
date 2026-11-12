package io.github.calendrum.syntax

/** How a parser reacts to problems in the input. */
public enum class ParseMode {
    /** Reads whatever can be read. Problems are reported and repaired, never thrown. This is the default. */
    Lenient,

    /**
     * Throws [io.github.calendrum.ICalParseException] at the first problem of severity [io.github.calendrum.Severity.Error].
     * Warnings do not stop it. Strict mode also decodes every value while parsing, so value errors are caught too.
     */
    Strict,
}

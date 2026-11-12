package io.github.calendrum

/** How serious a [Problem] is. */
public enum class Severity {
    /** Nothing is wrong with the data; the problem only explains a decision, such as a skipped byte order mark. */
    Info,

    /**
     * The data deviates from the standard but was read in a defined way, usually the way real-world producers meant it.
     */
    Warning,

    /**
     * The data could not be read as meant: a line, a value or a whole component was skipped. A strict parser throws
     * [ICalParseException] on the first one.
     */
    Error,
}

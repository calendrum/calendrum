package io.github.calendrum.syntax

// Closed by us, not by an RFC, so an enum (as `ParseMode`).
/** The kind of event an [ICalReader] reports. */
public enum class ContentEventType {
    /** A `BEGIN` line: a component starts. */
    Begin,

    /** A property of the current component. */
    Property,

    /** An `END` line: the component ends, possibly implicitly. */
    End,
}

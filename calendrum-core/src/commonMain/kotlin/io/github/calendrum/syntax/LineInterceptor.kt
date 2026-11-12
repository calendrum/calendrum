package io.github.calendrum.syntax

import io.github.calendrum.ExperimentalICalApi

// A `fun interface` that edits the line in place: no object is returned, so an interceptor that changes nothing
// allocates nothing.
/**
 * Looks at each property line while it is read or written, and may change or drop it.
 *
 * An interceptor fixes a feed on the fly: nothing is allocated per line until an edit is made, and lines it leaves
 * alone stay byte for byte as they were. Register it with `ParseOptions.Builder.intercept` or
 * `WriteOptions.Builder.intercept`. Interceptors run in the order they were added; each sees the line as the previous
 * one left it. An edited line is written canonically.
 */
public fun interface LineInterceptor {
    /**
     * Called once for every property line.
     *
     * The [line] is valid only during this call; do not keep it. The function must not throw for data reasons.
     */
    @ExperimentalICalApi
    public fun intercept(line: InterceptedLine)
}

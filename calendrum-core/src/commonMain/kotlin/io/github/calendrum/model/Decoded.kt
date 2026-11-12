package io.github.calendrum.model

import io.github.calendrum.Problem
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.describe

/**
 * The result of decoding a property or parameter: a value, nothing, or a malformed one.
 *
 * Reading with `component[key]` gives only the value or `null`; `component.decode(key)` tells which of the three it was
 * and why.
 */
public sealed interface Decoded<out T : Any> {
    /**
     * A value was decoded, possibly with problems that were repaired (warnings and infos).
     *
     * @property value the decoded value.
     * @property problems the repairs made while decoding; empty when the value was clean.
     */
    public class Value<T : Any>(public val value: T, problems: List<Problem>) : Decoded<T> {
        public val problems: List<Problem> = if (problems.isEmpty()) emptyList() else ArrayView(problems.toTypedArray())

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("Value", "problems", problems.size)
    }

    /** The component has no such property. */
    public object Missing : Decoded<Nothing> {
        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = "Missing"
    }

    // `warnings` (internal) are the warnings reported before the error; not public (the public result has only `problem`), used by
    // eager decoding to report every problem of the value in order.

    /**
     * The property exists but could not be decoded.
     *
     * @property problem the error that explains why; never shows the value.
     */
    public class Malformed(public val problem: Problem) : Decoded<Nothing> {
        internal var warnings: List<Problem> = emptyList()

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = "Malformed(${problem.code.id})"
    }
}

internal fun <T : Any> Decoded<T>.valueOrNull(): T? = if (this is Decoded.Value) value else null

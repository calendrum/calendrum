@file:JvmName("ICalParserDsl")

package io.github.calendrum

import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ParseOptions
import kotlin.jvm.JvmName

/**
 * Creates a parser from options written in a DSL block.
 *
 * ```
 * val strict = ICal.parser {
 *     mode = ParseMode.Strict
 *     limits = ParseLimits.builder().maxLines(100_000).build()
 * }
 * ```
 *
 * @param configure sets the properties of [ParseOptions.Builder].
 */
public inline fun ICal.parser(configure: ParseOptions.Builder.() -> Unit): ICalParser =
    parser(ParseOptions.builder().apply(configure).build())

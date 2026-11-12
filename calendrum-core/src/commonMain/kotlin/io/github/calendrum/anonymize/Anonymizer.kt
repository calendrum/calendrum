@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.anonymize

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ParseOptions
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// Internal entry point of the calendar anonymizer, used by the `tools` CLI and the `Anonymizer` fuzz target. Parses the
// input like `ICal.parse`, builds a tree of the same shape, writes it with the byte-stable writer, gives the output the
// charset, byte order mark and line endings of the input, and checks the result. The key is copied and never shown, not
// even by `toString`.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class Anonymizer(
    key: ByteArray,
    private val shiftWeeks: Int,
    private val options: ParseOptions = ParseOptions.Default,
) {
    private val key = key.copyOf()

    init {
        require(key.size >= MIN_KEY_BYTES) { "The key needs at least $MIN_KEY_BYTES bytes" }
    }

    public fun anonymize(input: ByteArray): AnonymizedCalendar {
        val source = ICal.parser(options).parse(input)
        val session = AnonymizationSession(key, shiftWeeks)
        val tree = session.anonymize(source.components)
        val written = ICal.writer().encodeToByteArray(tree)
        val format = OutputFormat.of(input, source)
        val encoded = format.encode(written)
        val output = encoded ?: written
        val reparsed = ICal.parse(output)
        val verification = Verification(source, tree, reparsed, session.stats)
        val report = AnonymizationReport(
            session,
            shiftWeeks,
            format,
            encoded != null,
            input.size,
            output.size,
            source,
            reparsed,
            verification,
        )
        return AnonymizedCalendar(
            output,
            tree,
            verification.passed,
            report.render(),
            source.problems,
            source.isTruncated,
        )
    }

    override fun toString(): String = "Anonymizer(shiftWeeks=$shiftWeeks)"

    private companion object {
        private const val MIN_KEY_BYTES = 16
    }
}

// `bytes` is the output in the charset of the input; `components` is the anonymized tree (what the output reads back as
// when its charset is detected like the input's); `summary` names properties, parameters and problem codes but never
// shows a value.

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class AnonymizedCalendar internal constructor(
    public val bytes: ByteArray,
    public val components: List<ICalComponent>,
    public val isVerified: Boolean,
    public val summary: String,
    public val problems: List<Problem>,
    public val isTruncated: Boolean,
) {
    override fun toString(): String =
        "AnonymizedCalendar(bytes=${bytes.size}, components=${components.size}, verified=$isVerified)"
}

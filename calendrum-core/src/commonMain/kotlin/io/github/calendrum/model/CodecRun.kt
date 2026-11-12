package io.github.calendrum.model

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ValueDecoder
import io.github.calendrum.value.ValueProblems

// One decode of one property: collects the value problems with their spans, applies the strict-mode rule (a lenient
// heuristic is a warning, in strict mode followed by `ICAL-V001`) and turns the result into `Decoded`. `shift` moves
// the offsets of a decoder call on a part of the value (`GEO`, `REQUEST-STATUS`) to the start of the whole value.
//
// It reads the value, the parameters and the positions of problems through four abstract members: `NodeRun` from a tree
// node (the lazy accessors, no extra allocation), `LineRun` from the current logical line of the reader (schemas, which
// build no nodes). Positions of a line are mapped through the segments of the unfolded line, so a problem gets the same
// offset, line and column as in the tree.
internal abstract class CodecRun(val context: DecodeContext) : ValueProblems {
    private var problems: ArrayList<Problem>? = null
    private var error: Problem? = null
    var shift: Int = 0

    val strict: Boolean get() = context.isStrict

    val decoder: ValueDecoder = ValueDecoder(context.isStrict, false, this)

    abstract val value: String

    abstract val parameterList: List<ICalParameter>

    abstract fun parameter(name: String): ICalParameter?

    abstract fun valueSpan(offset: Int): SourceSpan

    abstract fun parameterSpan(parameter: ICalParameter): SourceSpan

    override fun report(code: ProblemCode, offset: Int) {
        add(Problem(code, valueSpan(offset + shift), null))
    }

    fun reportParameter(code: ProblemCode, parameter: ICalParameter) {
        add(Problem(code, parameterSpan(parameter), null))
    }

    fun accept(code: ProblemCode, offset: Int): Boolean {
        report(code, offset)
        if (!strict) return true
        report(ProblemCode.InvalidValue, offset)
        return false
    }

    fun acceptParameter(code: ProblemCode, parameter: ICalParameter): Boolean {
        reportParameter(code, parameter)
        if (!strict) return true
        reportParameter(ProblemCode.InvalidValue, parameter)
        return false
    }

    fun <T : Any> finish(value: T?): Decoded<T> {
        val error = error
        if (value == null || (strict && error != null)) {
            val problem = error ?: Problem(ProblemCode.InvalidValue, valueSpan(0), null)
            val malformed = Decoded.Malformed(problem)
            val all = problems
            if (all != null) malformed.warnings = all
            return malformed
        }
        return Decoded.Value(value, problems ?: emptyList())
    }

    private fun add(problem: Problem) {
        val list = problems ?: ArrayList<Problem>(2).also { problems = it }
        list.add(problem)
        if (error == null && problem.severity == Severity.Error) error = problem
    }

    fun text(name: String): String? = parameter(name)?.value

    fun texts(name: String): List<String> = parameter(name)?.values ?: emptyList()

    inline fun <E : Any> token(name: String, default: E, of: (String) -> E): E {
        val parameter = parameter(name) ?: return default
        val value = parameter.value
        if (value.isEmpty()) {
            reportParameter(ProblemCode.InvalidValue, parameter)
            return default
        }
        return of(value)
    }

    inline fun <E : Any> optionalToken(name: String, of: (String) -> E): E? {
        val parameter = parameter(name) ?: return null
        val value = parameter.value
        if (value.isEmpty()) {
            reportParameter(ProblemCode.InvalidValue, parameter)
            return null
        }
        return of(value)
    }

    fun flag(name: String): Boolean {
        val parameter = parameter(name) ?: return false
        return when (enumToken(parameter.value.ifEmpty { "-" })) {
            "TRUE" -> true

            "FALSE" -> false

            else -> {
                reportParameter(ProblemCode.InvalidValue, parameter)
                false
            }
        }
    }

    // The type from `VALUE=` as a bit of the allowed set, not a `ValueType` object: decoding a property does not create
    // the constants of the `ValueType` companion. `REJECTED` - not allowed and refused in strict mode; `NOT_DECLARED` -
    // no `VALUE` parameter.
    fun valueType(allowed: Int, default: Int): Int {
        val parameter = parameter(VALUE) ?: return default
        val value = parameter.value
        val type = if (value.isEmpty()) 0 else typeBit(enumToken(value))
        if (type and allowed != 0) return type
        if (!acceptParameter(ProblemCode.ValueTypeNotAllowed, parameter)) return REJECTED
        return default
    }

    fun declared(): Int {
        val value = parameter(VALUE)?.value ?: return NOT_DECLARED
        return if (value.isEmpty()) NOT_DECLARED else typeBit(enumToken(value))
    }
}

internal class NodeRun(private val property: ICalProperty, context: DecodeContext) : CodecRun(context) {
    override val value: String get() = property.value

    override val parameterList: List<ICalParameter> get() = property.parameters

    override fun parameter(name: String): ICalParameter? = property.parameter(name)

    override fun valueSpan(offset: Int): SourceSpan = property.valueSpan(offset)

    override fun parameterSpan(parameter: ICalParameter): SourceSpan = property.parameterSpan(parameter, 0)
}

internal const val VALUE = "VALUE"

internal const val TYPE_BINARY = 1
internal const val TYPE_DATE = 2
internal const val TYPE_DATE_TIME = 4
internal const val TYPE_DURATION = 8
internal const val TYPE_PERIOD = 16
internal const val TYPE_TEXT = 32
internal const val TYPE_URI = 64
internal const val TYPE_CAL_ADDRESS = 128
internal const val TYPE_INTEGER = 256
internal const val TYPE_FLOAT = 512
internal const val TYPE_RECUR = 1024
internal const val TYPE_UTC_OFFSET = 2048
internal const val TYPE_UID = 4096
internal const val TYPE_XML_REFERENCE = 8192
internal const val TYPE_BOOLEAN = 16384

internal const val REJECTED = -1
internal const val NOT_DECLARED = -2

internal fun typeBit(token: String): Int = when (token) {
    "BINARY" -> TYPE_BINARY
    "DATE" -> TYPE_DATE
    "DATE-TIME" -> TYPE_DATE_TIME
    "DURATION" -> TYPE_DURATION
    "PERIOD" -> TYPE_PERIOD
    "TEXT" -> TYPE_TEXT
    "URI" -> TYPE_URI
    "CAL-ADDRESS" -> TYPE_CAL_ADDRESS
    "INTEGER" -> TYPE_INTEGER
    "FLOAT" -> TYPE_FLOAT
    "RECUR" -> TYPE_RECUR
    "UTC-OFFSET" -> TYPE_UTC_OFFSET
    "UID" -> TYPE_UID
    "XML-REFERENCE" -> TYPE_XML_REFERENCE
    "BOOLEAN" -> TYPE_BOOLEAN
    else -> 0
}

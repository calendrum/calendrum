@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.checkedName
import io.github.calendrum.syntax.checkedPropertyName
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmField
import kotlin.native.ObjCName

/**
 * Names a property and knows how to read and write its value as a [T]: the typed handle used as
 * `component[Properties.DTSTART]`.
 *
 * Keys for the standard properties are in [Properties]; make your own for an `X-` property with a codec, no
 * registration needed. Immutable.
 *
 * @property codec decodes the property and encodes a value into a property.
 */
public class PropertyKey<T : Any>(name: String, public val codec: PropertyCodec<T>) {
    /** The canonical property name in upper case. */
    public val name: String = checkedPropertyName(name)

    internal val nameId: Int = KnownNames.find(this.name)

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "PropertyKey($name)"
}

/**
 * Names a parameter and knows how to read and write its value as a [T]: the typed handle used as
 * `property[Parameters.PARTSTAT]`.
 *
 * Keys for the standard parameters are in [Parameters]. Immutable.
 *
 * @property codec decodes the parameter and encodes a value into a parameter.
 */
public class ParameterKey<T : Any>(name: String, public val codec: ParameterCodec<T>) {
    /** The canonical parameter name in upper case. */
    public val name: String = checkedName(name, "parameter name")

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "ParameterKey($name)"
}

/**
 * Reads a property into a value of type [T] and writes a value back; the plug-in point for your own properties.
 *
 * Implementations must be stateless and thread-safe, report trouble as [Decoded.Malformed] instead of throwing, and not
 * put values into problems.
 */
@SubclassOptInRequired(ExperimentalICalApi::class)
public interface PropertyCodec<T : Any> {
    /**
     * Decodes [property], with problems and strictness as [context] says.
     *
     * @param property the property to read.
     * @param context lenient or strict decoding, and a factory for problems with positions.
     */
    public fun decode(property: ICalProperty, context: DecodeContext): Decoded<T>

    /**
     * Encodes [value] into a property named by [context].
     *
     * @param value the value to write.
     * @param context carries the property name.
     */
    @ObjCName("encodeProperty")
    public fun encode(value: T, context: EncodeContext): ICalProperty
}

/**
 * Reads a parameter into a value of type [T] and writes a value back; the plug-in point for your own parameters.
 *
 * Implementations must be stateless and thread-safe.
 */
@SubclassOptInRequired(ExperimentalICalApi::class)
public interface ParameterCodec<T : Any> {
    /**
     * Decodes [parameter] of [property], with problems and strictness as [context] says.
     *
     * @param property the property the parameter belongs to.
     * @param parameter the parameter to read.
     * @param context lenient or strict decoding, and a factory for problems with positions.
     */
    public fun decode(property: ICalProperty, parameter: ICalParameter, context: DecodeContext): Decoded<T>

    /**
     * Encodes [value] into a parameter named by [context].
     *
     * @param value the value to write.
     * @param context carries the parameter name.
     */
    @ObjCName("encodeParameter")
    public fun encode(value: T, context: EncodeContext): ICalParameter
}

/**
 * How a codec decodes: leniently or strictly, and the factory for problems at the right position.
 *
 * @property isStrict whether a value that is only acceptable by repair is an error.
 */
public class DecodeContext internal constructor(public val isStrict: Boolean) {
    /** Creates a problem with [code] positioned [valueOffset] characters into the value of [property]. */
    public fun problem(property: ICalProperty, code: ProblemCode, valueOffset: Int): Problem =
        Problem(code, property.valueSpan(valueOffset), null)

    /** Creates a problem with [code] positioned at the value of [parameter] of [property]. */
    public fun problem(property: ICalProperty, parameter: ICalParameter, code: ProblemCode): Problem =
        Problem(code, property.parameterSpan(parameter, 0), null)

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = if (isStrict) "DecodeContext(strict)" else "DecodeContext(lenient)"

    /** The two contexts. */
    public companion object {
        /** Reads known quirks of real-world data with a warning and a repair. */
        @JvmField
        public val Lenient: DecodeContext = DecodeContext(false)

        /** Turns every repair into an error: the value is rejected. */
        @JvmField
        public val Strict: DecodeContext = DecodeContext(true)
    }
}

/**
 * What a codec needs to write: the name of the property or parameter.
 *
 * @property name the name to write.
 */
public class EncodeContext internal constructor(public val name: String) {
    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "EncodeContext($name)"
}

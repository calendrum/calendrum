@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.value

import io.github.calendrum.InternalICalApi
import io.github.calendrum.ProblemCode
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun interface ValueProblems {
    public fun report(code: ProblemCode, offset: Int)
}

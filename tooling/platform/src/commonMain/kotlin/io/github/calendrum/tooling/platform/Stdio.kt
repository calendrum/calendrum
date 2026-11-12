package io.github.calendrum.tooling.platform

import kotlinx.io.RawSink
import kotlinx.io.RawSource

/**
 * The standard input of the process. kotlinx-io has none on Native.
 *
 * A read returns as soon as some bytes arrive, an interrupted read is repeated, and a read that fails is the end of the
 * input.
 */
public expect fun standardInput(): RawSource

/**
 * The standard output of the process. kotlinx-io has none on Native.
 *
 * Interrupted and short writes are repeated until everything is written. Nobody is left to read the output once it is
 * closed, so a write that fails ends the process with the code 0 and prints nothing.
 */
public expect fun standardOutput(): RawSink

/**
 * The standard output of a command that must not lose what it prints.
 *
 * Unlike [standardOutput] a write that fails throws [kotlinx.io.IOException], also when the reader has gone: the caller
 * answers with an error code and nobody gets a signal.
 */
public expect fun checkedStandardOutput(): RawSink

/** The standard error of the process. A write that fails is dropped: there is nobody left to tell. */
public expect fun standardError(): RawSink

/** Whether the standard output is a terminal, where text can steer the screen. */
public expect fun standardOutputIsTerminal(): Boolean

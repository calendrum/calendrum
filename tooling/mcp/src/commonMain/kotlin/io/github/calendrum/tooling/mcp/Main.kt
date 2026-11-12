package io.github.calendrum.tooling.mcp

import io.github.calendrum.tooling.platform.standardInput
import io.github.calendrum.tooling.platform.standardOutput
import kotlin.system.exitProcess

/** The entry point of the server. */
public fun main() {
    serve(standardInput(), standardOutput())
    // An operation that is still running cannot be interrupted, and its answer has nowhere to go.
    exitProcess(0)
}

package io.github.calendrum.tooling.cli

import io.github.calendrum.tooling.platform.commandLineArguments
import kotlin.system.exitProcess

/** The entry point of the command line. */
public fun main(args: Array<String>) {
    exitProcess(runCli(commandLineArguments(args), SystemConsole))
}

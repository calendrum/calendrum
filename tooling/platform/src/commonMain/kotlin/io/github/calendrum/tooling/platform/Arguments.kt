package io.github.calendrum.tooling.platform

/**
 * The arguments of the command line, after the name of the program.
 *
 * Where the runtime hands them over right, they are [fallback]. On Windows the C runtime gives them in the code page of the
 * system and not in Unicode, which would spoil a name that is not ASCII, so they are read again from the command line in
 * UTF-16. That code has been compiled and has not been run.
 *
 * @param fallback the arguments as `main` got them.
 */
public expect fun commandLineArguments(fallback: Array<String>): List<String>

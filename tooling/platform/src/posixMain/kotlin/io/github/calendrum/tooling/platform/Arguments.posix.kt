package io.github.calendrum.tooling.platform

public actual fun commandLineArguments(fallback: Array<String>): List<String> = fallback.toList()

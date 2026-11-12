package io.github.calendrum.model

import io.github.calendrum.syntax.canonicalName

internal fun enumToken(value: String): String {
    require(value.isNotEmpty()) { "Empty value" }
    return canonicalName(value)
}

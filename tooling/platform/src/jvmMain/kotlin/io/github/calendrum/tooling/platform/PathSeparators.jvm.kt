package io.github.calendrum.tooling.platform

import java.io.File

internal actual val PATH_SEPARATORS: CharArray =
    if (File.separatorChar == '\\') charArrayOf('/', '\\') else charArrayOf('/')

@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKStringFromUtf16
import kotlinx.cinterop.value
import platform.windows.CommandLineToArgvW
import platform.windows.GetCommandLineW
import platform.windows.LocalFree

// Not run: the development host is not Windows. If the call fails, the arguments of main are used.
public actual fun commandLineArguments(fallback: Array<String>): List<String> = memScoped {
    val count = alloc<IntVar>()
    val argv = CommandLineToArgvW(GetCommandLineW(), count.ptr) ?: return fallback.toList()
    try {
        // The first one is the name of the program.
        (1 until count.value).map { index -> argv[index]?.toKStringFromUtf16() ?: return fallback.toList() }
    } finally {
        LocalFree(argv)
    }
}

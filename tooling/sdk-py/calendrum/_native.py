"""The boundary with the native library: loading it, the four functions, and the refusal to call it after `fork`.

Importing this module loads nothing, not even `ctypes`: a program that forks after the import must be safe, as `gunicorn --preload`
and `multiprocessing` do.
"""

from __future__ import annotations

import os
import sys
import threading
import types
from typing import Any, Optional

from ._errors import ForkedProcessError, LibraryError

_ABI_VERSION = 1

_FORKED = (
    "This process is a child of fork() and its parent has already called the library: the native call would hang forever. "
    "Start the workers with spawn or forkserver, or call the library only in the workers and never before they fork."
)

_lock = threading.Lock()
_library: Optional["_Library"] = None

# What the refusal after `fork` depends on: the process that began to load the library, and whether this one is a child of it. It
# belongs to the process and not to this module, as the runtime does: after a reload, or from a second copy of the package, the
# native call would hang just the same. Written before anything of the library runs, so that a fork at any later moment finds it.
_state = sys.modules.setdefault("calendrum._process_state", types.SimpleNamespace(owner_pid=None, forked=False))


def _library_file() -> str:
    if sys.platform == "darwin":
        name = "libcalendrum.dylib"
    elif sys.platform == "win32":
        name = "calendrum.dll"
    else:
        name = "libcalendrum.so"
    return os.path.join(os.path.dirname(os.path.abspath(__file__)), name)


class _Library:
    def __init__(self, path: str) -> None:
        import ctypes

        self._byref = ctypes.byref
        self._string_at = ctypes.string_at
        self._size = ctypes.c_size_t
        self._bytes = ctypes.POINTER(ctypes.c_uint8)
        native = ctypes.CDLL(path)

        self.abi_version = native.calendrum_abi_version
        self.abi_version.restype = ctypes.c_int32
        self.abi_version.argtypes = []

        self._version = native.calendrum_version
        self._version.restype = ctypes.c_int32
        self._version.argtypes = [ctypes.POINTER(self._bytes), ctypes.POINTER(ctypes.c_size_t)]

        self._call = native.calendrum_call
        self._call.restype = ctypes.c_int32
        self._call.argtypes = [
            ctypes.c_char_p,
            ctypes.c_char_p,
            ctypes.c_size_t,
            ctypes.POINTER(self._bytes),
            ctypes.POINTER(ctypes.c_size_t),
        ]

        self._free = native.calendrum_free
        self._free.restype = None
        self._free.argtypes = [self._bytes]

    def _take(self, code: int, data: Any, size: Any) -> bytes:
        if code != 0:
            raise LibraryError(f"The native library failed with code {code}", code)
        try:
            return self._string_at(data, size.value)
        finally:
            self._free(data)

    def version(self) -> bytes:
        data, size = self._bytes(), self._size()
        return self._take(self._version(self._byref(data), self._byref(size)), data, size)

    def call(self, operation: bytes, request: bytes) -> bytes:
        data, size = self._bytes(), self._size()
        code = self._call(operation, request, len(request), self._byref(data), self._byref(size))
        return self._take(code, data, size)


def _load() -> _Library:
    global _library
    with _lock:
        if _library is not None:
            return _library
        path = _library_file()
        if not os.path.isfile(path):
            raise LibraryError("The native library is not in the package: install the wheel for this platform")
        # Before `dlopen`: a thread that forks while this one is inside it leaves a child that would crash or hang in the runtime.
        earlier_owner = _state.owner_pid
        _state.owner_pid = os.getpid()
        try:
            library = _Library(path)
        except (OSError, AttributeError) as error:
            _state.owner_pid = earlier_owner
            raise LibraryError(f"The native library did not load: {error}") from None
        version = library.abi_version()
        if version != _ABI_VERSION:
            raise LibraryError(f"The native library has ABI {version}, and this package needs {_ABI_VERSION}")
        _library = library
        return library


def _for_this_process() -> _Library:
    # This comes before any lock: a child gets its error even if a lock was held at the moment of the fork.
    if _state.forked or (_state.owner_pid is not None and _state.owner_pid != os.getpid()):
        raise ForkedProcessError(_FORKED)
    return _library if _library is not None else _load()


def call(operation: bytes, request: bytes) -> bytes:
    """Runs one operation and returns the bytes of its response."""
    return _for_this_process().call(operation, request)


def versions() -> bytes:
    """Returns the bytes of the JSON object with the versions of the library."""
    return _for_this_process().version()


def _after_fork_in_child() -> None:
    # The lock may be held by a thread that the child does not have. The flag also covers a child whose process id happens to be
    # the one of a parent that has ended.
    global _lock
    _lock = threading.Lock()
    if _state.owner_pid is not None:
        _state.forked = True


if hasattr(os, "register_at_fork"):
    os.register_at_fork(after_in_child=_after_fork_in_child)

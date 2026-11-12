import ctypes
import json
import os
import sys
from pathlib import Path

ICAL_OK = 0
ICAL_INVALID_ARGUMENT = 1
ICAL_INTERNAL_ERROR = 2


class CalendrumError(Exception):
    def __init__(self, code, message):
        super().__init__(message)
        self.code = code


def _default_library():
    root = Path(__file__).resolve().parent.parent / "build" / "bin"
    if sys.platform == "darwin":
        return root / "macosArm64" / "releaseShared" / "libcalendrum.dylib"
    return root / "linuxX64" / "releaseShared" / "libcalendrum.so"


_out = ctypes.POINTER(ctypes.c_uint8)


class Calendrum:
    def __init__(self, path=None):
        self._lib = ctypes.CDLL(str(path or os.environ.get("CALENDRUM_LIBRARY") or _default_library()))
        lib = self._lib
        lib.ical_abi_version.restype = ctypes.c_int32
        lib.ical_abi_version.argtypes = []
        lib.ical_version.restype = ctypes.c_int32
        lib.ical_version.argtypes = [ctypes.POINTER(_out), ctypes.POINTER(ctypes.c_size_t)]
        lib.ical_parse.restype = ctypes.c_int32
        lib.ical_parse.argtypes = [
            ctypes.c_char_p, ctypes.c_size_t,
            ctypes.POINTER(_out), ctypes.POINTER(ctypes.c_size_t),
            ctypes.POINTER(_out), ctypes.POINTER(ctypes.c_size_t),
        ]
        lib.ical_occurrences.restype = ctypes.c_int32
        lib.ical_occurrences.argtypes = [
            ctypes.c_char_p, ctypes.c_size_t,
            ctypes.c_char_p, ctypes.c_char_p, ctypes.c_char_p,
            ctypes.POINTER(_out), ctypes.POINTER(ctypes.c_size_t),
        ]
        lib.ical_free.restype = None
        lib.ical_free.argtypes = [_out]

    def abi_version(self):
        return self._lib.ical_abi_version()

    def version(self):
        data, size = _out(), ctypes.c_size_t()
        code = self._lib.ical_version(ctypes.byref(data), ctypes.byref(size))
        return self._result(code, data, size)

    def parse(self, ics):
        jcal, jcal_len = _out(), ctypes.c_size_t()
        problems, problems_len = _out(), ctypes.c_size_t()
        code = self._lib.ical_parse(
            ics, len(ics),
            ctypes.byref(jcal), ctypes.byref(jcal_len),
            ctypes.byref(problems), ctypes.byref(problems_len),
        )
        found = self._take(problems, problems_len)
        return self._result(code, jcal, jcal_len), [] if found is None else json.loads(found)

    def occurrences(self, ics, start, end, zone=None):
        data, size = _out(), ctypes.c_size_t()
        code = self._lib.ical_occurrences(
            ics, len(ics),
            start.encode(), end.encode(), None if zone is None else zone.encode(),
            ctypes.byref(data), ctypes.byref(size),
        )
        return self._result(code, data, size)

    def _take(self, data, size):
        if not data:
            return None
        try:
            return ctypes.string_at(data, size.value).decode("utf-8")
        finally:
            self._lib.ical_free(data)

    def _result(self, code, data, size):
        text = self._take(data, size)
        value = None if text is None else json.loads(text)
        if code != ICAL_OK:
            message = value.get("error") if isinstance(value, dict) else None
            raise CalendrumError(code, message or "Calendrum error %d" % code)
        return value

"""Calendrum: reads, checks, repairs, converts, expands, compares and anonymizes iCalendar files (RFC 5545).

Importing this package loads nothing native: the library loads at the first call. Once a process has called it, a child of `fork`
cannot, and gets `ForkedProcessError`.

A result holds what a calendar says, and a calendar comes from a stranger: the docstrings of the models mark such fields as untrusted.
"""

from . import _models
from ._errors import (
    BadArgumentError,
    CalendrumError,
    ForkedProcessError,
    InternalError,
    LibraryError,
    TooLargeError,
    TruncatedWarning,
    UnknownCodeError,
    UnknownOperationError,
    UnknownZoneError,
)
from ._models import *  # noqa: F401,F403
from ._operations import (
    CallResult,
    Versions,
    anonymize,
    build_event,
    call,
    convert,
    diff,
    expand,
    explain,
    fix,
    freebusy,
    inspect,
    rrule_expand,
    tz_resolve,
    validate,
    versions,
)
from ._version import __version__

__all__ = [
    "BadArgumentError",
    "CalendrumError",
    "CallResult",
    "ForkedProcessError",
    "InternalError",
    "LibraryError",
    "TooLargeError",
    "TruncatedWarning",
    "UnknownCodeError",
    "UnknownOperationError",
    "UnknownZoneError",
    "Versions",
    "__version__",
    "anonymize",
    "build_event",
    "call",
    "convert",
    "diff",
    "expand",
    "explain",
    "fix",
    "freebusy",
    "inspect",
    "rrule_expand",
    "tz_resolve",
    "validate",
    "versions",
]
__all__ += _models.__all__

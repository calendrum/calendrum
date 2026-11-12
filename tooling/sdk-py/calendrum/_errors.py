"""The errors of the package. Their text comes from the contract or from this package, never from the input."""

from __future__ import annotations

from typing import Dict, Optional, Type


class CalendrumError(Exception):
    """An operation refused the request.

    A problem of a calendar is not an error: the operation succeeds and lists it in `problems`.

    Attributes:
        code: A stable code of the contract, such as OPS-BAD-ARGUMENT.
        message: A fixed sentence. It never quotes the request or the calendar.
    """

    def __init__(self, code: str, message: str) -> None:
        super().__init__(code, message)
        self.code = code
        self.message = message

    def __str__(self) -> str:
        return f"{self.code}: {self.message}"


class BadArgumentError(CalendrumError):
    """A field has the wrong type or value, or the fields do not make a valid request."""


class UnknownOperationError(CalendrumError):
    """There is no such operation."""


class UnknownZoneError(CalendrumError):
    """A zone of the request is not in the time zone data."""


class UnknownCodeError(CalendrumError):
    """`explain` does not know the code."""


class TooLargeError(CalendrumError):
    """The request or the calendar is above the limit, or a response that is not a list is."""


class InternalError(CalendrumError):
    """A bug of the library. No operation should ever give it."""


class LibraryError(Exception):
    """The native library cannot be used here: it did not load, or it failed outside any operation.

    This is not a `CalendrumError`: code that handles a bad calendar with `except CalendrumError` must not also hide a broken
    deployment.

    Attributes:
        message: What happened.
        code: The code the library returned, or None when it did not get that far.
    """

    def __init__(self, message: str, code: Optional[int] = None) -> None:
        super().__init__(message, code)
        self.message = message
        self.code = code

    def __str__(self) -> str:
        return self.message


class ForkedProcessError(LibraryError):
    """This process is a child of `fork`, and its parent has already called the library.

    The native call would hang forever, so it is not made.
    """


class TruncatedWarning(UserWarning):
    """A result does not hold everything: a limit cut a list, or the input was not read whole."""


_BY_CODE: Dict[str, Type[CalendrumError]] = {
    "OPS-BAD-ARGUMENT": BadArgumentError,
    "OPS-UNKNOWN-OPERATION": UnknownOperationError,
    "OPS-UNKNOWN-ZONE": UnknownZoneError,
    "OPS-UNKNOWN-CODE": UnknownCodeError,
    "OPS-TOO-LARGE": TooLargeError,
    "OPS-INTERNAL": InternalError,
}


def error_for(code: str, message: str) -> CalendrumError:
    """Returns the error of a code; a code this version does not know gets the base class, as the contract can grow."""
    return _BY_CODE.get(code, CalendrumError)(code, message)

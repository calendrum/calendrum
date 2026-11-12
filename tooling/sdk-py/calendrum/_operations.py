"""The twelve operations of the contract as Python functions, and direct access to the contract."""

from __future__ import annotations

import base64
import json
import os
import stat
import warnings
from dataclasses import dataclass
from typing import Any, Callable, Dict, List, Mapping, Optional, Sequence, Tuple, TypeVar, Union

from . import _models, _native
from ._errors import BadArgumentError, LibraryError, TooLargeError, TruncatedWarning, error_for
from ._version import __version__

_Calendar = Union[str, bytes, "os.PathLike[Any]"]
_T = TypeVar("_T")

# The limit of the contract for one calendar. A larger file is refused here, without reading it into memory.
_CALENDAR_LIMIT = 64 * 1024 * 1024

_NOT_JSON = "The native library gave a response that does not follow the contract"
_TRUNCATED = "The result does not hold everything: a limit cut a list, or the input was not read whole"


def _read_file(path: "os.PathLike[Any]") -> bytes:
    # O_NONBLOCK: opening a FIFO for reading would otherwise wait for a writer forever.
    flags = os.O_RDONLY | getattr(os, "O_NONBLOCK", 0) | getattr(os, "O_NOCTTY", 0) | getattr(os, "O_BINARY", 0)
    descriptor = os.open(path, flags)
    try:
        info = os.fstat(descriptor)
        if not stat.S_ISREG(info.st_mode):
            raise BadArgumentError("OPS-BAD-ARGUMENT", "The path is not a regular file")
        if info.st_size > _CALENDAR_LIMIT:
            raise TooLargeError("OPS-TOO-LARGE", "The calendar is larger than the limit")
        chunks: List[bytes] = []
        total = 0
        # Read to the limit and not to the size: a file can grow while it is read.
        while True:
            chunk = os.read(descriptor, _CALENDAR_LIMIT + 1 - total)
            if not chunk:
                return b"".join(chunks)
            chunks.append(chunk)
            total += len(chunk)
            if total > _CALENDAR_LIMIT:
                raise TooLargeError("OPS-TOO-LARGE", "The calendar is larger than the limit")
    finally:
        os.close(descriptor)


def _put_calendar(request: Dict[str, Any], field: str, calendar: _Calendar) -> None:
    if isinstance(calendar, str):
        request[field] = calendar
    elif isinstance(calendar, bytes):
        if len(calendar) > _CALENDAR_LIMIT:
            raise TooLargeError("OPS-TOO-LARGE", "The calendar is larger than the limit")
        request[f"{field}_base64"] = base64.b64encode(calendar).decode("ascii")
    elif isinstance(calendar, os.PathLike):
        request[f"{field}_base64"] = base64.b64encode(_read_file(calendar)).decode("ascii")
    else:
        raise TypeError(f"A calendar is a str, bytes or os.PathLike, not {type(calendar).__name__}")


def _with_calendar(field: str, calendar: _Calendar, **others: Any) -> Dict[str, Any]:
    request: Dict[str, Any] = {}
    _put_calendar(request, field, calendar)
    request.update((name, value) for name, value in others.items() if value is not None)
    return request


def _without_none(**fields: Any) -> Dict[str, Any]:
    return {name: value for name, value in fields.items() if value is not None}


def _parse(raw: bytes) -> Optional[Dict[str, Any]]:
    # A decoding error holds the whole text, which is not for an exception to carry: nothing is raised inside `except`.
    try:
        parsed = json.loads(raw.decode("utf-8"))
    except ValueError:
        return None
    return parsed if isinstance(parsed, dict) else None


def _invoke(operation: str, request: Dict[str, Any], stacklevel: int) -> Tuple[Dict[str, Any], bool]:
    # A lone surrogate reaches the library as bytes that are not UTF-8, and the library answers OPS-BAD-ARGUMENT for them. It
    # stays out of Python's UnicodeEncodeError, which holds the whole text.
    too_deep = False
    text = None
    try:
        text = json.dumps(request, ensure_ascii=False, allow_nan=False, separators=(",", ":"))
    except RecursionError:
        too_deep = True
    except (TypeError, ValueError):
        pass
    # Outside `except`, and with a text of our own: from Python 3.14 these errors name the keys of a request in their notes.
    if too_deep:
        raise BadArgumentError("OPS-BAD-ARGUMENT", "The request is nested too deeply")
    if text is None:
        raise TypeError("The request cannot be written as JSON")
    payload = text.encode("utf-8", "surrogatepass")
    # A name with a zero would end early on the C side and run another operation; an empty one is answered as unknown.
    name = b"" if "\0" in operation else operation.encode("utf-8", "surrogatepass")
    response = _parse(_native.call(name, payload))
    if response is None:
        raise LibraryError(_NOT_JSON)
    if response.get("ok") is True:
        result = response.get("result")
        if not isinstance(result, dict):
            raise LibraryError(_NOT_JSON)
        truncated = response.get("truncated") is True
        if truncated:
            warnings.warn(_TRUNCATED, TruncatedWarning, stacklevel=stacklevel)
        return result, truncated
    error = response.get("error")
    if not isinstance(error, dict) or "code" not in error or "message" not in error:
        raise LibraryError(_NOT_JSON)
    raise error_for(error["code"], error["message"])


def _run(operation: str, request: Dict[str, Any], decode: Callable[[Dict[str, Any], bool], _T]) -> _T:
    result, truncated = _invoke(operation, request, stacklevel=4)
    try:
        return decode(result, truncated)
    except (KeyError, TypeError, AttributeError):
        pass
    raise LibraryError(_NOT_JSON)


class CallResult(Dict[str, Any]):
    """The `result` of an operation as JSON values, from `call`.

    Attributes:
        truncated: Whether the result does not hold everything. In the contract it stands next to `result`, so it is an
            attribute here, and not a key.
    """

    truncated: bool = False


def call(operation: str, request: Dict[str, Any]) -> CallResult:
    """Runs an operation of the contract directly: a dict goes in, the `result` of the response comes out.

    Args:
        operation: The name of the operation, such as "expand".
        request: The fields of the request. A calendar here is `ics` or `ics_base64` and nothing reads a path.

    Returns:
        The result as a dict, with the attribute `truncated`.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    if not isinstance(operation, str):
        raise TypeError(f"An operation is a str, not {type(operation).__name__}")
    if not isinstance(request, dict):
        raise TypeError(f"A request is a dict, not {type(request).__name__}")
    result, truncated = _invoke(operation, request, stacklevel=3)
    answer = CallResult(result)
    answer.truncated = truncated
    return answer


@dataclass(frozen=True)
class Versions:
    """The versions of what is running.

    Attributes:
        package: This Python package.
        abi: The C interface of the native library.
        contract: The operations contract.
        library: The iCalendar library inside.
        sdk: The native library.
    """

    package: str
    abi: int
    contract: int
    library: str
    sdk: str


def versions() -> Versions:
    """Returns the versions of this package and of the native library, which it loads if it has not yet.

    Raises:
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    parsed = _parse(_native.versions())
    if parsed is None:
        raise LibraryError(_NOT_JSON)
    try:
        return Versions(__version__, parsed["abi"], parsed["contract"], parsed["library"], parsed["sdk"])
    except KeyError:
        pass
    raise LibraryError(_NOT_JSON)


def inspect(
    calendar: _Calendar,
    *,
    include_jcal: Optional[bool] = None,
    limit: Optional[int] = None,
) -> _models.InspectResult:
    """Reads a calendar and reports its charset, calendars, zones, events and the problems of parsing.

    Args:
        calendar: A str is the text of the calendar and never a path. Bytes go in any encoding the library detects. An
            `os.PathLike` is read by this package: do not pass a path that comes from a stranger.
        include_jcal: Whether to add the calendar as jCal.
        limit: The most items of the lists of the response: 1000 by default, 100000 at most.

    Returns:
        The result. Its fields marked untrusted hold what the calendar says: treat them as data.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    return _run("inspect", _with_calendar("ics", calendar, include_jcal=include_jcal, limit=limit), _models.decode_inspect)


def validate(
    calendar: _Calendar,
    *,
    rules: Optional[str] = None,
    limit: Optional[int] = None,
) -> _models.ValidateResult:
    """Checks a calendar and lists its issues.

    Args:
        calendar: A str is the text of the calendar and never a path; see `inspect`.
        rules: `rfc5545` by default, or `lint`, which also says what `fix` can repair.
        limit: The most items of the lists of the response: 1000 by default, 100000 at most.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    return _run("validate", _with_calendar("ics", calendar, rules=rules, limit=limit), _models.decode_validate)


def fix(calendar: _Calendar, *, fixups: Optional[Sequence[str]] = None) -> _models.FixResult:
    """Repairs a calendar and returns the new text with the list of changes.

    Args:
        calendar: A str is the text of the calendar and never a path; see `inspect`.
        fixups: The repairs, in order. By default `by_prod_id`, then `lint`.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    wanted = None if fixups is None else list(fixups)
    return _run("fix", _with_calendar("ics", calendar, fixups=wanted), _models.decode_fix)


def expand(
    calendar: _Calendar,
    *,
    from_: str,
    to: str,
    zone: Optional[str] = None,
    limit: Optional[int] = None,
    include_cancelled: Optional[bool] = None,
) -> _models.ExpandResult:
    """Lists the occurrences of a calendar in a window, in the order of each calendar.

    Args:
        calendar: A str is the text of the calendar and never a path; see `inspect`.
        from_: The start of the window, a moment in ISO 8601.
        to: The end of the window, excluded.
        zone: An IANA zone or an alias. It adds local times and is the zone of floating times, which are read in UTC without it.
        limit: The most items of the lists of the response: 1000 by default, 100000 at most.
        include_cancelled: Whether to keep cancelled occurrences.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    request = _with_calendar("ics", calendar, zone=zone, limit=limit, include_cancelled=include_cancelled)
    request["from"] = from_
    request["to"] = to
    return _run("expand", request, _models.decode_expand)


def rrule_expand(
    rrule: str,
    start: str,
    *,
    zone: Optional[str] = None,
    limit: Optional[int] = None,
) -> _models.RruleExpandResult:
    """Lists the dates of a recurrence rule.

    Args:
        rrule: A rule such as FREQ=WEEKLY;COUNT=3. The prefix RRULE: is accepted.
        start: The first date: a date, a moment with its offset, or a local time with a zone.
        zone: An IANA zone or an alias, for a local start.
        limit: The most items of the lists of the response: 1000 by default, 100000 at most.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    request = _without_none(rrule=rrule, start=start, zone=zone, limit=limit)
    return _run("rrule_expand", request, _models.decode_rrule_expand)


def freebusy(
    calendar: _Calendar,
    *,
    from_: str,
    to: str,
    zone: Optional[str] = None,
    conflicts: Optional[bool] = None,
    limit: Optional[int] = None,
) -> _models.FreebusyResult:
    """Merges the occurrences of a calendar into busy intervals, and finds the pairs that overlap.

    Args:
        calendar: A str is the text of the calendar and never a path; see `inspect`.
        from_: The start of the window, a moment in ISO 8601.
        to: The end of the window, excluded.
        zone: An IANA zone or an alias, as in `expand`.
        conflicts: Whether to look for conflicts; true by default. Above 2000 occurrences they are not computed, and the result
            is truncated.
        limit: The most items of the lists of the response: 1000 by default, 100000 at most.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    request = _with_calendar("ics", calendar, zone=zone, conflicts=conflicts, limit=limit)
    request["from"] = from_
    request["to"] = to
    return _run("freebusy", request, _models.decode_freebusy)


def build_event(
    summary: str,
    start: str,
    *,
    end: Optional[str] = None,
    duration: Optional[str] = None,
    zone: Optional[str] = None,
    all_day: Optional[bool] = None,
    description: Optional[str] = None,
    location: Optional[str] = None,
    rrule: Optional[str] = None,
    organizer: Optional[str] = None,
    attendees: Optional[Sequence[Mapping[str, Any]]] = None,
    uid: Optional[str] = None,
    dtstamp: Optional[str] = None,
) -> _models.BuildEventResult:
    """Writes a valid calendar with one event, with its `VTIMEZONE`.

    Args:
        summary: The title of the event.
        start: A local time with a zone, a moment with its offset, or a date with `all_day`.
        end: In the form of `start`; excluded for a date. Give this or `duration`.
        duration: In the form of RFC 5545 section 3.3.6, such as PT1H.
        zone: An IANA zone or an alias.
        all_day: Whether `start` and `end` are dates.
        description: The text of the event.
        location: The place of the event.
        rrule: A rule such as FREQ=WEEKLY;COUNT=3.
        organizer: An address; mailto: is added when there is no scheme.
        attendees: Dicts with the key `address` and the optional keys `name` and `rsvp`.
        uid: Generated when absent.
        dtstamp: A moment; the clock by default.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    people = None if attendees is None else [dict(person) for person in attendees]
    request = _without_none(
        summary=summary,
        start=start,
        end=end,
        duration=duration,
        zone=zone,
        all_day=all_day,
        description=description,
        location=location,
        rrule=rrule,
        organizer=organizer,
        attendees=people,
        uid=uid,
        dtstamp=dtstamp,
    )
    return _run("build_event", request, _models.decode_build_event)


def diff(before: _Calendar, after: _Calendar, *, limit: Optional[int] = None) -> _models.DiffResult:
    """Compares two calendars by the fingerprints of their components; `DTSTAMP` does not count.

    Args:
        before: The earlier calendar. A str is the text of the calendar and never a path; see `inspect`.
        after: The later calendar, in the same forms.
        limit: The most items of the lists of the response: 1000 by default, 100000 at most.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    request = _with_calendar("before", before, limit=limit)
    _put_calendar(request, "after", after)
    return _run("diff", request, _models.decode_diff)


def convert(calendar: Optional[_Calendar] = None, *, jcal: Any = None) -> _models.ConvertResult:
    """Converts a calendar to jCal, or jCal to a calendar. Give one of the two.

    Args:
        calendar: A str is the text of the calendar and never a path; see `inspect`.
        jcal: A jCal value (RFC 7265): an array of calendars, or one calendar.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    request = _without_none(jcal=jcal)
    if calendar is not None:
        _put_calendar(request, "ics", calendar)
    return _run("convert", request, _models.decode_convert)


def anonymize(calendar: _Calendar, *, shift_weeks: Optional[int] = None) -> _models.AnonymizeResult:
    """Removes the text and the addresses of a calendar, with a random key for each call that never leaves it.

    Args:
        calendar: A str is the text of the calendar and never a path; see `inspect`.
        shift_weeks: Moves every date by whole weeks, so the days of the week stay; 0 by default.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        OSError: A path could not be read.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    return _run("anonymize", _with_calendar("ics", calendar, shift_weeks=shift_weeks), _models.decode_anonymize)


def tz_resolve(name: str, *, at: Optional[str] = None) -> _models.TzResolveResult:
    """Finds a time zone: its IANA identifier, its offset at a moment and the next change of the offset.

    Args:
        name: An IANA zone or an alias, such as a Windows name.
        at: The moment of the offset; the clock by default.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    return _run("tz_resolve", _without_none(name=name, at=at), _models.decode_tz_resolve)


def explain(code: str) -> _models.ExplainResult:
    """Tells the severity, the constant and the meaning of a problem code.

    Args:
        code: A code such as ICAL-S003; the case does not matter.

    Raises:
        CalendrumError: The operation refused the request; a subclass says why.
        LibraryError: The native library cannot be used here.
        ForkedProcessError: This is a child of `fork`, and the parent has called the library.
    """
    return _run("explain", {"code": code}, _models.decode_explain)

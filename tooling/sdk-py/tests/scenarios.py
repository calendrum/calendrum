"""Scenarios that need a process of their own, because what counts is what this process has done before.

Rules, from an accident with a test that started itself: a child never reaches `main`, as the name of a scenario is read only under
the `__main__` guard; every wait on a child has a limit; and the test that starts a scenario kills its whole session past a limit.

Usage: python3 -I -B scenarios.py <scenario> [arguments]. A scenario that fails exits with 1 and prints why.
"""
import os
import sys
import warnings

sys.path.insert(0, os.environ["CALENDRUM_PACKAGE"])

import calendrum  # noqa: E402
from calendrum import _native  # noqa: E402

LIMIT = 90
ICS = ("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//test//EN\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n"
       "DTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nDTEND:20260105T110000Z\r\nSUMMARY:Plain\r\n"
       "RRULE:FREQ=DAILY;COUNT=3\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n")
FROM, TO = "2026-01-01T00:00:00Z", "2026-02-01T00:00:00Z"


def check(condition, text):
    if not condition:
        print(f"FAILED: {text}", flush=True)
        os._exit(1)
    print(f"ok: {text}", flush=True)


def expand_once():
    return calendrum.expand(ICS, from_=FROM, to=TO).count


# ------------------------------------------------------------------------------------------------- what a child does


def child_calls(connection, count):
    try:
        for _ in range(count):
            if expand_once() != 3:
                connection.send("wrong answer")
                return
        connection.send("finished")
    except calendrum.ForkedProcessError as error:
        connection.send(f"forked: {error}")
    except BaseException as error:
        connection.send(f"other: {type(error).__name__}: {error}")


class _Trap:
    def __getattr__(self, name):
        raise AssertionError(f"the native function {name} was reached")


def child_never_reaches_native(connection):
    """Every way in has to fail before anything native, including the version of the ABI."""
    _native._library = _Trap()
    _native._Library = None
    outcomes = []
    for attempt in (expand_once, calendrum.versions, lambda: calendrum.explain("ICAL-S003"), lambda: calendrum.call("explain", {})):
        try:
            attempt()
            outcomes.append("answered")
        except calendrum.ForkedProcessError:
            outcomes.append("forked")
        except BaseException as error:
            outcomes.append(f"{type(error).__name__}: {error}")
    connection.send(outcomes)


def child_then_grandchild(connection):
    import multiprocessing

    try:
        first = expand_once()
        grandchild = outcome_of(multiprocessing.get_context("fork"), child_calls, 1)
        connection.send((first, grandchild, expand_once()))
    except BaseException as error:
        connection.send(f"other: {type(error).__name__}: {error}")


def pool_task():
    return expand_once()


# ------------------------------------------------------------------------------------------------- what a parent does


def outcome_of(context, target, *arguments):
    receiver, sender = context.Pipe(duplex=False)
    process = context.Process(target=target, args=(sender, *arguments))
    process.start()
    sender.close()
    try:
        if not receiver.poll(LIMIT):
            return "HANG"
        try:
            return receiver.recv()
        except EOFError:
            return "died"
    finally:
        process.join(5)
        if process.is_alive():
            process.kill()
            process.join(5)


def wait_for(pid):
    """The exit status of a child of os.fork, or None past the limit, with the child killed."""
    import signal
    import time

    end = time.monotonic() + LIMIT
    while time.monotonic() < end:
        done, status = os.waitpid(pid, os.WNOHANG)
        if done:
            return status
        time.sleep(0.02)
    os.kill(pid, signal.SIGKILL)
    os.waitpid(pid, 0)
    return None


def parent_keeps_working(count=3000):
    for _ in range(count):
        if expand_once() != 3:
            check(False, "the parent got a wrong answer")
    check(True, f"the parent made {count} more calls")


def fork_parent_idle():
    import multiprocessing

    check(_native._library is None and _native._state.owner_pid is None, "the import called nothing")
    outcome = outcome_of(multiprocessing.get_context("fork"), child_calls, 5000)
    check(outcome == "finished", f"a child of a parent that called nothing makes 5000 calls: {outcome}")
    parent_keeps_working(100)


def fork_parent_called():
    import multiprocessing

    expand_once()
    context = multiprocessing.get_context("fork")
    outcome = outcome_of(context, child_calls, 5000)
    check(outcome.startswith("forked: "), f"the child gets the error and does not hang: {outcome[:60]}")
    check("spawn" in outcome and "forkserver" in outcome, "the text says what to do")
    outcomes = outcome_of(context, child_never_reaches_native)
    check(outcomes == ["forked"] * 4, f"nothing native runs in the child: {outcomes}")
    parent_keeps_working()


def parent_called_with(method):
    import multiprocessing

    expand_once()
    outcome = outcome_of(multiprocessing.get_context(method), child_calls, 5000)
    check(outcome == "finished", f"{method}: the child makes 5000 calls: {outcome}")
    parent_keeps_working(100)


def forkserver_parent_called():
    parent_called_with("forkserver")


def spawn_parent_called():
    parent_called_with("spawn")


def forkserver_with_preload():
    import multiprocessing

    # The import loads nothing, so that a server that has imported the package can fork its workers.
    context = multiprocessing.get_context("forkserver")
    context.set_forkserver_preload(["calendrum"])
    expand_once()
    outcome = outcome_of(context, child_calls, 5000)
    check(outcome == "finished", f"a forkserver that preloads the package: {outcome}")


def pool_with(method):
    import concurrent.futures
    import multiprocessing

    expand_once()
    pool = concurrent.futures.ProcessPoolExecutor(2, mp_context=multiprocessing.get_context(method))
    return pool, pool.submit(pool_task)


def pool_fork():
    import concurrent.futures

    pool, future = pool_with("fork")
    try:
        future.result(LIMIT)
        check(False, "a pool that forks after a call has to fail")
    except concurrent.futures.TimeoutError:
        check(False, "the pool hangs")
    except calendrum.ForkedProcessError as error:
        check("forkserver" in str(error), "the error comes back through the pool with its text")
    pool.shutdown(wait=True)
    parent_keeps_working(100)


def pool_spawn():
    pool, future = pool_with("spawn")
    check(future.result(LIMIT) == 3, "a pool that spawns works")
    pool.shutdown(wait=True)


def os_fork_exit():
    # From Python 3.12 this warns once the runtime has started its threads, which is right, and the guide says so.
    warnings.simplefilter("ignore", DeprecationWarning)
    expand_once()
    pid = os.fork()
    if pid == 0:
        os._exit(7)
    status = wait_for(pid)
    check(status is not None and os.WEXITSTATUS(status) == 7, "a child that calls nothing and ends with _exit")
    parent_keeps_working(100)


def os_fork_sys_exit():
    warnings.simplefilter("ignore", DeprecationWarning)
    expand_once()
    pid = os.fork()
    if pid == 0:
        # The whole shutdown of the interpreter and of the C library, in a child without the threads of the runtime.
        sys.exit(9)
    status = wait_for(pid)
    check(status is not None and os.WEXITSTATUS(status) == 9, "a child that calls nothing and ends with exit")
    parent_keeps_working(100)


def threads_in_calls():
    import multiprocessing
    import threading

    # Python warns about any fork with threads, and that is the point here.
    warnings.simplefilter("ignore", DeprecationWarning)
    stop = threading.Event()
    wrong = []

    def work():
        while not stop.is_set():
            if expand_once() != 3:
                wrong.append(1)

    threads = [threading.Thread(target=work) for _ in range(8)]
    for thread in threads:
        thread.start()
    try:
        context = multiprocessing.get_context("fork")
        for attempt in range(5):
            outcome = outcome_of(context, child_calls, 5000)
            check(outcome.startswith("forked: "), f"fork {attempt} among 8 threads in calls: {outcome[:60]}")
    finally:
        stop.set()
    for thread in threads:
        thread.join(LIMIT)
    check(not wrong and not any(thread.is_alive() for thread in threads), "the threads of the parent went on and ended")
    parent_keeps_working(100)


def fork_while_loading():
    import multiprocessing

    # A thread holding this lock does not exist in the child, and nothing native has been called yet.
    _native._lock.acquire()
    outcome = outcome_of(multiprocessing.get_context("fork"), child_calls, 200)
    _native._lock.release()
    check(outcome == "finished", f"a child forked during the first load does not wait for the lock: {outcome}")
    parent_keeps_working(100)


def fork_during_the_first_call():
    import multiprocessing
    import threading
    import time

    # The window of the first load, held open: a thread is inside the first call, in front of `dlopen`. No child crashes here,
    # as a child that is refused never reaches the runtime.
    original = _native._Library
    entered = threading.Event()

    def slow(path):
        entered.set()
        time.sleep(1)
        return original(path)

    _native._Library = slow
    thread = threading.Thread(target=expand_once)
    thread.start()
    check(entered.wait(LIMIT), "a thread is in the first call")
    outcome = outcome_of(multiprocessing.get_context("fork"), child_calls, 3000)
    check(outcome.startswith("forked: "), f"a child forked during the first call is refused: {outcome[:60]}")
    thread.join(LIMIT)
    parent_keeps_working(100)


def failed_load_does_not_refuse_a_child():
    import multiprocessing

    original = _native._Library

    def failing(path):
        raise OSError("no such library")

    _native._Library = failing
    try:
        expand_once()
        check(False, "the load has to fail")
    except calendrum.LibraryError:
        pass
    _native._Library = original
    check(_native._state.owner_pid is None, "a load that failed claims nothing")
    outcome = outcome_of(multiprocessing.get_context("fork"), child_calls, 200)
    check(outcome == "finished", f"a child of a parent whose load failed works: {outcome}")


def reload_keeps_the_guard():
    import importlib
    import multiprocessing

    expand_once()
    importlib.reload(_native)
    check(expand_once() == 3, "the parent works after a reload")
    outcome = outcome_of(multiprocessing.get_context("fork"), child_calls, 3000)
    check(outcome.startswith("forked: "), f"a child is refused after a reload: {outcome[:60]}")


def child_of_a_second_copy(connection, directory):
    sys.path.remove(os.environ["CALENDRUM_PACKAGE"])
    for name in [name for name in sys.modules if name.startswith("calendrum.") and name != "calendrum._process_state"] + ["calendrum"]:
        del sys.modules[name]
    sys.path.insert(0, directory)
    import calendrum as other

    assert os.path.dirname(other.__file__).startswith(directory)
    try:
        for _ in range(3000):
            other.explain("ICAL-S003")
        connection.send("finished")
    except other.ForkedProcessError as error:
        connection.send(f"forked: {error}")
    except BaseException as error:
        connection.send(f"other: {type(error).__name__}: {error}")


def second_copy_is_refused_too(directory):
    import multiprocessing

    expand_once()
    outcome = outcome_of(multiprocessing.get_context("fork"), child_of_a_second_copy, directory)
    check(outcome.startswith("forked: "), f"a child that calls another copy of the package is refused: {outcome[:60]}")


def missing_symbol():
    import ctypes

    class Empty:
        def __getattr__(self, name):
            raise AttributeError(name)

    original = ctypes.CDLL
    ctypes.CDLL = lambda path: Empty()
    try:
        calendrum.explain("ICAL-S003")
        check(False, "a library without its functions has to fail")
    except calendrum.LibraryError as error:
        check("did not load" in str(error), f"it says so: {error}")
    finally:
        ctypes.CDLL = original


def grandchild():
    import multiprocessing

    outcome = outcome_of(multiprocessing.get_context("fork"), child_then_grandchild)
    check(not isinstance(outcome, str), f"the child answered: {outcome}")
    first, below, last = outcome
    check(first == 3 and last == 3, "the first process to call keeps working")
    check(below.startswith("forked: "), f"its own child is refused: {below[:60]}")


def first_call_race():
    import threading

    made = []
    original = _native._Library

    def counted(path):
        made.append(path)
        return original(path)

    _native._Library = counted
    barrier = threading.Barrier(8)
    answers = []

    def work():
        barrier.wait()
        answers.append(expand_once())

    threads = [threading.Thread(target=work) for _ in range(8)]
    for thread in threads:
        thread.start()
    for thread in threads:
        thread.join(LIMIT)
    check(answers == [3] * 8, "8 threads in the first call get their answers")
    check(len(made) == 1, f"the library loaded once: {len(made)}")


def exit_with_threads_in_calls():
    import threading
    import time

    def work():
        while True:
            expand_once()

    for _ in range(4):
        threading.Thread(target=work, daemon=True).start()
    time.sleep(0.05)
    print("ok: the main thread ends while 4 daemon threads are in calls", flush=True)
    sys.exit(0)


def interrupt_arrives_after_the_call():
    import signal
    import threading
    import time

    # Above 2000 occurrences the conflicts are not computed, and that is not the point here.
    warnings.simplefilter("ignore", calendrum.TruncatedWarning)
    calendar = ICS.replace("DTEND:20260105T110000Z", "DURATION:PT2S").replace("FREQ=DAILY;COUNT=3", "FREQ=SECONDLY;COUNT=1000000")
    # A process started in the background inherits SIGINT as ignored, and Python then raises nothing for it.
    signal.signal(signal.SIGINT, signal.default_int_handler)
    timing = {}
    for name in ("first", "second"):
        start = time.perf_counter()
        calendrum.freebusy(calendar, from_=FROM, to=TO)
        timing[name] = time.perf_counter() - start
    main = threading.get_ident()
    threading.Timer(0.05, lambda: signal.pthread_kill(main, signal.SIGINT)).start()
    start = time.perf_counter()
    try:
        calendrum.freebusy(calendar, from_=FROM, to=TO)
        check(False, "the interrupt has to come")
    except KeyboardInterrupt:
        elapsed = time.perf_counter() - start
    check(elapsed > min(timing.values()) * 0.5, f"it comes after the native call: {elapsed:.2f} s; a whole call takes {timing['second']:.2f} s")
    check(expand_once() == 3, "the library works after it")


def import_clean():
    check("ctypes" not in sys.modules, "the import of the package does not import ctypes")
    check(_native._library is None and _native._state.owner_pid is None, "the import called nothing")
    import ctypes

    def images():
        if sys.platform == "darwin":
            process = ctypes.CDLL(None)
            process._dyld_get_image_name.restype = ctypes.c_char_p
            process._dyld_get_image_name.argtypes = [ctypes.c_uint32]
            return [process._dyld_get_image_name(index).decode() for index in range(process._dyld_image_count())]
        with open("/proc/self/maps", encoding="utf-8", errors="replace") as file:
            return file.read().splitlines()

    check(not [image for image in images() if "calendrum" in image.rsplit("/", 1)[-1]], "no image of the library after the import")
    check(calendrum.explain("ICAL-S003").code == "ICAL-S003", "the first call answers")
    check([image for image in images() if "libcalendrum" in image], "the image of the library is there after the first call")


def rss_plateau():
    import resource

    def peak():
        value = resource.getrusage(resource.RUSAGE_SELF).ru_maxrss
        return value if sys.platform == "darwin" else value * 1024

    def calls(count):
        for index in range(count):
            if index % 2:
                expand_once()
            else:
                calendrum.inspect(ICS)

    calls(20000)
    early = peak()
    calls(80000)
    late = peak()
    mib = 1024 * 1024
    check(late - early < 16 * mib, f"100,000 calls reach a plateau: {early / mib:.1f} MiB at 20,000, {late / mib:.1f} MiB at the end")


def fifo(directory):
    import pathlib

    path = pathlib.Path(directory) / "pipe.ics"
    os.mkfifo(path)
    try:
        calendrum.inspect(path)
        check(False, "a FIFO has to be refused")
    except calendrum.BadArgumentError as error:
        check("regular file" in str(error), "a FIFO without a writer is refused at once")


def no_library(directory):
    sys.path.remove(os.environ["CALENDRUM_PACKAGE"])
    for name in [name for name in sys.modules if name.startswith("calendrum")]:
        del sys.modules[name]
    sys.path.insert(0, directory)
    import calendrum as other

    check(os.path.dirname(other.__file__).startswith(directory), "the copy without a library is the one imported")
    try:
        other.explain("ICAL-S003")
        check(False, "a package without its library has to fail")
    except other.LibraryError as error:
        check("wheel" in str(error) and error.code is None, f"it says so: {error}")


def broken_library(directory):
    sys.path.remove(os.environ["CALENDRUM_PACKAGE"])
    for name in [name for name in sys.modules if name.startswith("calendrum")]:
        del sys.modules[name]
    sys.path.insert(0, directory)
    import calendrum as other

    try:
        other.explain("ICAL-S003")
        check(False, "a broken library has to fail")
    except other.LibraryError as error:
        check("did not load" in str(error), "it says so")


def abi_mismatch():
    class Other:
        def __init__(self, path):
            pass

        def abi_version(self):
            return 2

    _native._Library = Other
    try:
        calendrum.explain("ICAL-S003")
        check(False, "another ABI has to fail")
    except calendrum.LibraryError as error:
        check("ABI 2" in str(error), f"it says so: {error}")
    check(_native._library is None, "the library was not kept")


SCENARIOS = {function.__name__: function for function in (
    fork_parent_idle, fork_parent_called, forkserver_parent_called, spawn_parent_called, pool_fork, pool_spawn, os_fork_exit,
    os_fork_sys_exit, threads_in_calls, fork_while_loading, grandchild, first_call_race, import_clean, rss_plateau, fifo, no_library,
    broken_library, abi_mismatch, forkserver_with_preload, exit_with_threads_in_calls, interrupt_arrives_after_the_call,
    fork_during_the_first_call, failed_load_does_not_refuse_a_child, reload_keeps_the_guard, second_copy_is_refused_too, missing_symbol,
)}

if __name__ == "__main__":
    SCENARIOS[sys.argv[1]](*sys.argv[2:])

"""Threads, and what needs a process of its own: the import, `fork`, memory over many calls, a library that is missing.

Each scenario is in scenarios.py, with the rules that keep a mistake in such a test from starting processes without end.
"""
import os
import shutil
import tempfile
import threading
import unittest
import warnings

import support

import calendrum
from calendrum import _native


class ThreadsTest(unittest.TestCase):
    def test_8_threads_get_the_same_answers_as_one(self):
        wanted = {
            "expand": calendrum.expand(support.ICS, **support.WINDOW),
            "inspect": calendrum.inspect(support.ICS),
            "validate": calendrum.validate(support.ICS, rules="lint"),
            "convert": calendrum.convert(support.ICS),
        }
        wrong = []

        def work():
            for _ in range(200):
                if calendrum.expand(support.ICS, **support.WINDOW) != wanted["expand"]:
                    wrong.append("expand")
                if calendrum.inspect(support.ICS) != wanted["inspect"]:
                    wrong.append("inspect")
                if calendrum.validate(support.ICS, rules="lint") != wanted["validate"]:
                    wrong.append("validate")
                if calendrum.convert(support.ICS).jcal != wanted["convert"].jcal:
                    wrong.append("convert")

        threads = [threading.Thread(target=work) for _ in range(8)]
        for thread in threads:
            thread.start()
        for thread in threads:
            thread.join(120)
        self.assertFalse(any(thread.is_alive() for thread in threads))
        self.assertEqual([], wrong)

    def test_a_native_call_lets_python_run(self):
        events = "".join(
            f"BEGIN:VEVENT\r\nUID:{index}@example.com\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nEND:VEVENT\r\n"
            for index in range(50000)
        )
        request = ('{"ics":"BEGIN:VCALENDAR\\r\\nVERSION:2.0\\r\\nPRODID:x\\r\\n' + events.replace("\r\n", "\\r\\n")
                   + 'END:VCALENDAR\\r\\n"}').encode("ascii")
        ticks = [0]
        stop = threading.Event()

        def tick():
            while not stop.is_set():
                ticks[0] += 1

        thread = threading.Thread(target=tick)
        thread.start()
        try:
            before = ticks[0]
            response = _native.call(b"inspect", request)
            during = ticks[0] - before
        finally:
            stop.set()
            thread.join(30)
        self.assertIn(b'"ok":true', response)
        self.assertGreater(during, 1000, "a thread of Python made no progress while the library ran")


class ProcessesTest(unittest.TestCase):
    def run_ok(self, name, *arguments, limit=300):
        code, output = support.run_scenario(name, *arguments, limit=limit)
        self.assertEqual(0, code, output)
        print(f"\n{name}:\n{output.rstrip()}")

    def test_the_import_loads_nothing(self):
        self.run_ok("import_clean", limit=60)

    def test_fork_parent_did_not_call_child_makes_5000_calls(self):
        self.run_ok("fork_parent_idle")

    def test_fork_parent_called_child_is_refused_and_parent_works(self):
        self.run_ok("fork_parent_called")

    def test_forkserver_parent_called(self):
        self.run_ok("forkserver_parent_called")

    def test_spawn_parent_called(self):
        self.run_ok("spawn_parent_called")

    def test_forkserver_that_preloads_the_package(self):
        self.run_ok("forkserver_with_preload")

    def test_the_process_ends_with_threads_in_calls(self):
        for _ in range(10):
            self.run_ok("exit_with_threads_in_calls", limit=60)

    def test_an_interrupt_arrives_after_the_call(self):
        self.run_ok("interrupt_arrives_after_the_call", limit=120)

    def test_pool_that_forks_is_refused(self):
        self.run_ok("pool_fork")

    def test_pool_that_spawns_works(self):
        self.run_ok("pool_spawn")

    def test_os_fork_and_exit_at_once(self):
        self.run_ok("os_fork_exit")

    def test_os_fork_and_a_whole_shutdown(self):
        self.run_ok("os_fork_sys_exit")

    def test_fork_with_8_threads_in_calls(self):
        self.run_ok("threads_in_calls")

    def test_fork_during_the_first_load(self):
        self.run_ok("fork_while_loading")

    def test_the_child_of_a_child(self):
        self.run_ok("grandchild")

    def test_threads_race_to_the_first_call(self):
        self.run_ok("first_call_race")

    def test_fork_during_the_first_call(self):
        self.run_ok("fork_during_the_first_call")

    def test_a_failed_load_does_not_refuse_a_child(self):
        self.run_ok("failed_load_does_not_refuse_a_child")

    def test_a_reload_keeps_the_guard(self):
        self.run_ok("reload_keeps_the_guard")

    def test_a_second_copy_is_refused_too(self):
        directory = self.package_copy()
        shutil.copy(_native._library_file().replace(os.path.dirname(_native.__file__), os.path.join(support.PACKAGE, "calendrum")), os.path.join(directory, "calendrum"))
        self.run_ok("second_copy_is_refused_too", directory)

    def test_a_library_without_its_functions(self):
        self.run_ok("missing_symbol", limit=60)

    def test_memory_reaches_a_plateau(self):
        self.run_ok("rss_plateau")

    def test_another_abi(self):
        self.run_ok("abi_mismatch", limit=60)

    def package_copy(self):
        directory = tempfile.mkdtemp()
        self.addCleanup(shutil.rmtree, directory)
        target = os.path.join(directory, "calendrum")
        os.mkdir(target)
        source = os.path.join(support.PACKAGE, "calendrum")
        for name in os.listdir(source):
            if name.endswith(".py"):
                shutil.copy(os.path.join(source, name), target)
        return directory

    def test_a_package_without_its_library(self):
        self.run_ok("no_library", self.package_copy(), limit=60)

    def test_a_library_that_does_not_load(self):
        directory = self.package_copy()
        with open(os.path.join(directory, "calendrum", _native._library_file().rsplit(os.sep, 1)[-1]), "wb") as file:
            file.write(b"this is not a library")
        self.run_ok("broken_library", directory, limit=60)


class WarningsAreOffTest(unittest.TestCase):
    def test_ordinary_calls_warn_about_nothing(self):
        with warnings.catch_warnings(record=True) as caught:
            warnings.simplefilter("always")
            calendrum.validate(support.ICS)
            calendrum.fix(support.ICS)
        self.assertEqual([], caught)


if __name__ == "__main__":
    unittest.main()

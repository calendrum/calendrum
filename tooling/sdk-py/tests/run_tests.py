"""Runs the tests of the package on this Python.

Usage: python3 -I -B run_tests.py [<major.minor> | <major.minor>+]

With a version, a different interpreter fails with the reason: a test on two Pythons must not quietly become a test on one.
"""
import os
import sys
import unittest

HERE = os.path.dirname(os.path.abspath(__file__))


def main():
    print(f"Python {sys.version.split()[0]}: {sys.executable}", flush=True)
    if len(sys.argv) > 1:
        wanted = sys.argv[1]
        lowest = tuple(int(part) for part in wanted.rstrip("+").split("."))
        found = sys.version_info[:2]
        if found < lowest or (not wanted.endswith("+") and found != lowest):
            raise SystemExit(f"These tests have to run on Python {wanted}, and this is {found[0]}.{found[1]}: {sys.executable}")
    sys.path.insert(0, HERE)
    suite = unittest.defaultTestLoader.discover(HERE, pattern="test_*.py", top_level_dir=HERE)
    result = unittest.TextTestRunner(verbosity=1, stream=sys.stdout).run(suite)
    if not result.wasSuccessful() or result.testsRun == 0:
        raise SystemExit(1)


if __name__ == "__main__":
    main()

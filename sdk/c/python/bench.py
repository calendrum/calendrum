import ctypes
import sys
import threading
import time

from calendrum import Calendrum


# The same operations as the Kotlin baseline: the empty call, parse to jCal and occurrences of a year as JSON, then
# parsing in 1, 2, 4 and 8 threads; every thread uses its own output buffers.
def timed(rounds, body):
    start = time.perf_counter()
    for _ in range(rounds):
        body()
    return (time.perf_counter() - start) / rounds


def main():
    data = open(sys.argv[1], "rb").read()
    rounds = int(sys.argv[2])
    lib = Calendrum()
    raw = lib._lib
    window = ("2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z", "Europe/Berlin")
    timed(rounds // 10 + 1, lambda: lib.parse(data))
    print("python ical_abi_version %.1f ns" % (timed(1000000, raw.ical_abi_version) * 1e9))
    def parse_raw():
        jcal, jcal_len = ctypes.POINTER(ctypes.c_uint8)(), ctypes.c_size_t()
        problems, problems_len = ctypes.POINTER(ctypes.c_uint8)(), ctypes.c_size_t()
        raw.ical_parse(data, len(data), ctypes.byref(jcal), ctypes.byref(jcal_len),
                       ctypes.byref(problems), ctypes.byref(problems_len))
        raw.ical_free(jcal)
        raw.ical_free(problems)

    print("python ical_parse (ctypes only) %.1f us" % (timed(rounds, parse_raw) * 1e6))
    print("python parse (with json.loads) %.1f us" % (timed(rounds, lambda: lib.parse(data)) * 1e6))
    print("python occurrences (with json.loads) %.1f us" % (timed(rounds, lambda: lib.occurrences(data, *window)) * 1e6))
    for count in (1, 2, 4, 8):
        threads = [threading.Thread(target=lambda: [parse_raw() for _ in range(rounds)]) for _ in range(count)]
        start = time.perf_counter()
        for thread in threads:
            thread.start()
        for thread in threads:
            thread.join()
        print("python threads %d: %.0f parses/s" % (count, count * rounds / (time.perf_counter() - start)))


if __name__ == "__main__":
    main()

libcalendrum: a C library for iCalendar files. It reads, checks, repairs, converts, expands, compares and anonymizes them, and
looks up time zones and problem codes. One function runs every operation: a JSON request goes in, a JSON response comes out.

    calendrum.h                the interface: four functions, with the rules for memory and threads in its comments
    libcalendrum.dylib / .so   the library; on Windows calendrum.dll
    libcalendrum.dll.a         Windows only: the import library for MinGW
    calendrum.def              Windows only: the exports, to make a .lib for MSVC with lib /def:calendrum.def /machine:x64

Release a response with calendrum_free and nothing else. Do not call the library in a child of fork() once the parent has called
it, not even for the version: the child hangs at its first garbage collection. Unloading does nothing: the library stays in the
process. An out-of-memory inside the runtime ends the process.

It does not use the network or files, does not read the environment and never uses the time zone of the machine.

Calendrum is licensed under the Apache License, Version 2.0, in LICENSE. NOTICE lists the data in this library and
THIRD-PARTY-LICENSES.txt the libraries.

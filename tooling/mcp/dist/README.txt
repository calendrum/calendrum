calendrum-mcp: a Model Context Protocol server for iCalendar files, over standard input and output.

Connect it to Claude Code:

    claude mcp add calendrum -- /full/path/calendrum-mcp

Any other MCP client takes the same command in its configuration:

    {"mcpServers": {"calendrum": {"command": "/full/path/calendrum-mcp"}}}

It has no options and it does not use the network. It reads a file only when a tool is given a path, and only a calendar
file inside the roots of the client or, for a client without roots, in the working directory of the server. On Windows the
path field is not available in this version: give the calendar as content.

Text from a calendar in the results is data from whoever wrote the calendar, not instructions.

Calendrum is licensed under the Apache License, Version 2.0, in LICENSE. NOTICE lists the data in this binary and
THIRD-PARTY-LICENSES.txt the libraries.

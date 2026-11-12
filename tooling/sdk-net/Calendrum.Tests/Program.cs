using System;
using System.Linq;

namespace Calendrum.Tests;

internal static class Program
{
    private static int Main(string[] arguments)
    {
        // First of all: a child never reaches the code that starts children.
        if (arguments.Length > 0 && arguments[0] == "--scenario")
        {
            return Scenarios.Run(arguments[1], arguments.Skip(2).ToArray());
        }
        return Runner.RunAll(arguments);
    }
}

using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.IO;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Runtime.Loader;
using System.Threading;

[assembly: DefaultDllImportSearchPaths(DllImportSearchPath.AssemblyDirectory)]

namespace Calendrum.Internal;

/// <summary>A response that the native library owns. The bytes are read in place, and only its own function releases them.</summary>
internal readonly unsafe ref struct NativeBuffer
{
    private readonly byte* _data;
    private readonly int _length;

    internal NativeBuffer(byte* data, int length)
    {
        _data = data;
        _length = length;
    }

    internal ReadOnlySpan<byte> Span => new(_data, _length);

    public void Dispose() => Native.Free(_data);
}

/// <summary>The boundary with the native library: the four functions of its C interface. Nothing loads it before the first call.</summary>
internal static unsafe partial class Native
{
    // Not "calendrum": on Windows that file and the assembly Calendrum.dll would be one name in a published folder.
    private const string Library = "libcalendrum";
    private const int AbiVersion = 1;

    private static bool s_ready;

    // Before any of the functions below can be reached.
    static Native()
    {
        try
        {
            NativeLibrary.SetDllImportResolver(typeof(Native).Assembly, Resolve);
        }
        catch (InvalidOperationException)
        {
            // The application has set one for this assembly before the first call. It knows where it keeps the file.
        }
    }

    /// <summary>
    /// Looks only in the folders this application names. The usual search ends in a load by the bare name, and on macOS that also looks in
    /// the current folder, whatever the attribute above says on .NET 8: a file planted there would run inside this process. That is why a
    /// plugin is not left to the search either, even when its load context would be asked next: a context that does not know the file
    /// answers nothing, and the search runs. An application that keeps the file elsewhere sets its own resolver before the first call.
    /// </summary>
    private static nint Resolve(string name, Assembly assembly, DllImportSearchPath? searchPath)
    {
        if (name != Library)
        {
            return 0;
        }
        string file = Library + (OperatingSystem.IsWindows() ? ".dll" : OperatingSystem.IsMacOS() ? ".dylib" : ".so");
        foreach (string folder in Folders(assembly))
        {
            string path = Path.Combine(folder, file);
            if (File.Exists(path))
            {
                // Load and not TryLoad: when the file is there and does not load, the reason is what the caller needs to see.
                return NativeLibrary.Load(path);
            }
        }
        throw new DllNotFoundException($"There is no {file} in the folders of this application");
    }

    /// <summary>
    /// The folders of the runtime for this application, its base folder, and the folder of this assembly with the layout of the package
    /// under it (<c>runtimes/&lt;rid&gt;/native</c>), which a plugin built without a runtime identifier keeps. A plugin looks in its own
    /// folders first: the ones of the application can hold another version.
    /// </summary>
    [UnconditionalSuppressMessage("SingleFile", "IL3000", Justification = "Empty in a single file or under Native AOT, which the folder before it covers.")]
    private static List<string> Folders(Assembly assembly)
    {
        var own = new List<string>();
        if (assembly.Location.Length > 0)
        {
            string folder = Path.GetDirectoryName(assembly.Location)!;
            own.Add(folder);
            foreach (string rid in RuntimeIdentifiers())
            {
                own.Add(Path.Combine(folder, "runtimes", rid, "native"));
            }
        }
        var application = new List<string>();
        if (AppContext.GetData("NATIVE_DLL_SEARCH_DIRECTORIES") is string known)
        {
            application.AddRange(known.Split(Path.PathSeparator, StringSplitOptions.RemoveEmptyEntries));
        }
        application.Add(AppContext.BaseDirectory);
        bool plugin = AssemblyLoadContext.GetLoadContext(assembly) != AssemblyLoadContext.Default;
        return plugin ? [.. own, .. application] : [.. application, .. own];
    }

    // The identifier of this runtime, which a distribution can make specific (fedora.40-x64), and the portable one of the package.
    private static List<string> RuntimeIdentifiers()
    {
        string system = OperatingSystem.IsWindows() ? "win" : OperatingSystem.IsMacOS() ? "osx" : "linux";
        string architecture = RuntimeInformation.ProcessArchitecture.ToString().ToLowerInvariant();
        string portable = system + "-" + architecture;
        return RuntimeInformation.RuntimeIdentifier == portable ? [portable] : [RuntimeInformation.RuntimeIdentifier, portable];
    }

    [LibraryImport(Library, EntryPoint = "calendrum_abi_version")]
    private static partial int NativeAbiVersion();

    [LibraryImport(Library, EntryPoint = "calendrum_version")]
    private static partial int NativeVersion(byte** outJson, nuint* outLength);

    [LibraryImport(Library, EntryPoint = "calendrum_call")]
    private static partial int NativeCall(byte* operation, byte* request, nuint requestLength, byte** outResponse, nuint* outLength);

    [LibraryImport(Library, EntryPoint = "calendrum_free")]
    private static partial void NativeFree(byte* data);

    /// <summary>Runs one operation. The name ends with a zero.</summary>
    internal static NativeBuffer Call(ReadOnlySpan<byte> operation, ReadOnlySpan<byte> request)
    {
        EnsureReady();
        byte* data = null;
        nuint length = 0;
        int code;
        try
        {
            fixed (byte* name = operation)
            fixed (byte* body = request)
            {
                code = NativeCall(name, body, (nuint)request.Length, &data, &length);
            }
        }
        catch (EntryPointNotFoundException error)
        {
            throw NotUsable(error);
        }
        return Take(code, data, length);
    }

    /// <summary>Gives the JSON object with the versions of the native library.</summary>
    internal static NativeBuffer Versions()
    {
        EnsureReady();
        byte* data = null;
        nuint length = 0;
        int code;
        try
        {
            code = NativeVersion(&data, &length);
        }
        catch (EntryPointNotFoundException error)
        {
            throw NotUsable(error);
        }
        return Take(code, data, length);
    }

    internal static void Free(byte* data) => NativeFree(data);

    private static NativeBuffer Take(int code, byte* data, nuint length)
    {
        if (code != 0)
        {
            throw new LibraryException($"The native library failed with code {code}", code);
        }
        if (length > int.MaxValue)
        {
            NativeFree(data);
            throw new LibraryException("The response of the native library is larger than a .NET span can hold");
        }
        return new NativeBuffer(data, (int)length);
    }

    private static void EnsureReady()
    {
        if (Volatile.Read(ref s_ready))
        {
            return;
        }
        int version;
        try
        {
            version = NativeAbiVersion();
        }
        catch (Exception error) when (error is DllNotFoundException or EntryPointNotFoundException or BadImageFormatException)
        {
            throw NotUsable(error);
        }
        if (version != AbiVersion)
        {
            throw new LibraryException($"The native library has ABI {version}, and this package needs {AbiVersion}");
        }
        Volatile.Write(ref s_ready, true);
    }

    private static LibraryException NotUsable(Exception error) => new(
        "The native library did not load. The package has it for osx-arm64, linux-x64, linux-arm64 and win-x64; "
        + "this process runs on " + RuntimeInformation.RuntimeIdentifier + ".",
        error);
}

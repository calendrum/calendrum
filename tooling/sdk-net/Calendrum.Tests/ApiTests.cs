using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;

namespace Calendrum.Tests;

/// <summary>
/// The public API against a dump in the repository, as the ABI dumps of the Kotlin modules: a change to it has to be made on purpose. With
/// CALENDRUM_API_UPDATE=1 the test writes the dump.
/// </summary>
internal static class ApiTests
{
    private static string Name(Type type)
    {
        if (type.IsGenericParameter)
        {
            return type.Name;
        }
        Type? inner = Nullable.GetUnderlyingType(type);
        if (inner is not null)
        {
            return Name(inner) + "?";
        }
        if (type.IsGenericType)
        {
            string plain = type.Name[..type.Name.IndexOf('`', StringComparison.Ordinal)];
            return $"{plain}<{string.Join(", ", type.GetGenericArguments().Select(Name))}>";
        }
        return type.IsArray ? Name(type.GetElementType()!) + "[]" : type.Name;
    }

    private static string Parameters(MethodBase method) => string.Join(", ", method.GetParameters().Select(parameter =>
        Name(parameter.ParameterType) + " " + parameter.Name + (parameter.HasDefaultValue ? " = null" : "")));

    private static IEnumerable<string> Members(Type type)
    {
        const BindingFlags Declared = BindingFlags.Public | BindingFlags.Instance | BindingFlags.Static | BindingFlags.DeclaredOnly;
        foreach (ConstructorInfo constructor in type.GetConstructors(Declared))
        {
            yield return $"  new({Parameters(constructor)})";
        }
        foreach (PropertyInfo property in type.GetProperties(Declared))
        {
            string required = property.GetCustomAttributes().Any(attribute => attribute.GetType().Name == "RequiredMemberAttribute") ? "required " : "";
            MethodInfo? setter = property.SetMethod;
            string access = setter is { IsPublic: true } ? (setter.ReturnParameter.GetRequiredCustomModifiers().Length > 0 ? "get; init;" : "get; set;") : "get;";
            var nullability = new NullabilityInfoContext().Create(property);
            string mark = nullability.ReadState == NullabilityState.Nullable && !property.PropertyType.IsValueType ? "?" : "";
            yield return $"  {required}{Name(property.PropertyType)}{mark} {property.Name} {{ {access} }}";
        }
        foreach (MethodInfo method in type.GetMethods(Declared).Where(method => !method.IsSpecialName || method.Name.StartsWith("op_", StringComparison.Ordinal)))
        {
            string generic = method.IsGenericMethod ? $"<{string.Join(", ", method.GetGenericArguments().Select(Name))}>" : "";
            yield return $"  {(method.IsStatic ? "static " : "")}{Name(method.ReturnType)} {method.Name}{generic}({Parameters(method)})";
        }
    }

    internal static string Dump()
    {
        var text = new StringBuilder();
        foreach (Type type in typeof(ICal).Assembly.GetExportedTypes().OrderBy(type => type.FullName, StringComparer.Ordinal))
        {
            string kind = type.IsInterface ? "interface" : type.IsAbstract && type.IsSealed ? "static class" : type.IsSealed ? "sealed class" : "class";
            string parent = type.BaseType is { } baseType && baseType != typeof(object) ? " : " + Name(baseType) : "";
            string interfaces = string.Concat(type.GetInterfaces().Select(item => ", " + Name(item)).Order(StringComparer.Ordinal));
            text.Append(kind).Append(' ').Append(type.FullName).Append(parent).Append(interfaces).Append('\n');
            foreach (string member in Members(type).Order(StringComparer.Ordinal))
            {
                text.Append(member).Append('\n');
            }
        }
        return text.ToString();
    }

    [Test]
    private static void ThePublicApiIsTheDump()
    {
        string path = Environment.GetEnvironmentVariable("CALENDRUM_API") ?? throw new InvalidOperationException("The variable CALENDRUM_API is not set");
        string now = Dump();
        if (Environment.GetEnvironmentVariable("CALENDRUM_API_UPDATE") == "1")
        {
            Directory.CreateDirectory(Path.GetDirectoryName(path)!);
            File.WriteAllText(path, now);
        }
        Check.True(File.Exists(path), $"No dump at {path}: run the tests with CALENDRUM_API_UPDATE=1");
        Check.Equal(File.ReadAllText(path), now, "The public API differs from the dump; if that is on purpose, run the tests with CALENDRUM_API_UPDATE=1.");
    }
}

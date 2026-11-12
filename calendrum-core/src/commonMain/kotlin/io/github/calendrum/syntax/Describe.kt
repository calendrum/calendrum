package io.github.calendrum.syntax

// The text of `toString` of the nodes and options: `Type(name=value, ...)` from the pairs of a name and a value. The
// values are appended as objects, so the text is the same as the string template it replaced. A string template costs a
// few inlined appends in the code of every `toString` (0.7 to 2 KB each on iOS); this function is the one copy of them
// (the net saving is 1 KB on iOS). `toString` is not on a hot path.
internal fun describe(type: String, vararg fields: Any?): String {
    val out = StringBuilder(type).append('(')
    var index = 0
    while (index < fields.size) {
        if (index > 0) out.append(", ")
        out.append(fields[index]).append('=').append(fields[index + 1])
        index += 2
    }
    return out.append(')').toString()
}

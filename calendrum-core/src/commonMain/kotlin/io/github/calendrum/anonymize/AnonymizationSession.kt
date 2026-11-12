package io.github.calendrum.anonymize

import io.github.calendrum.ProblemListener
import io.github.calendrum.syntax.ByteLineWriter
import io.github.calendrum.syntax.ContentLexer
import io.github.calendrum.syntax.ContentLine
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ContentSelection
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.NO_INPUT_LIMIT
import io.github.calendrum.syntax.NO_TIME_LIMIT
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.TreeBuilder

// State of one run. Two passes: `collectWords` gathers the forbidden 4-grams from every value that will be replaced
// (the whole file must be known before the first replacement), then `rebuild` walks every root depth-first with an
// explicit stack and builds new nodes bottom-up. A node without changes is reused, so its lines stay byte for byte.
internal class AnonymizationSession(key: ByteArray, shiftWeeks: Int) {
    private val forbidden = ForbiddenGrams()
    private val pseudonyms = Pseudonyms(key, forbidden)
    private val scrambler = Scrambler(pseudonyms, forbidden)
    private val shifter = if (shiftWeeks == 0) null else DateShifter(shiftWeeks)
    private val raw = ByteLineWriter()
    val stats: AnonymizationStats = AnonymizationStats()

    val invalidSequences: Int get() = scrambler.invalidSequences

    fun anonymize(roots: List<ICalComponent>): List<ICalComponent> {
        collectWords(roots)
        return roots.map { rebuild(it) }
    }

    private fun collectWords(roots: List<ICalComponent>) {
        val components = ArrayList<ICalComponent>()
        val zones = ArrayList<Boolean>()
        for (root in roots.asReversed()) {
            components += root
            zones += root.nameId == KnownNames.VTIMEZONE
        }
        while (components.isNotEmpty()) {
            val component = components.removeAt(components.lastIndex)
            val inTimeZone = zones.removeAt(zones.lastIndex)
            for (property in component.propertyNodes) collectWords(property, inTimeZone)
            val children = component.componentNodes
            for (index in children.indices.reversed()) {
                components += children[index]
                zones += inTimeZone || children[index].nameId == KnownNames.VTIMEZONE
            }
        }
    }

    private fun collectWords(property: ICalProperty, inTimeZone: Boolean) {
        when (valueRule(property, inTimeZone)) {
            ValueRule.Keep, ValueRule.Date, ValueRule.Recurrence, ValueRule.Zone -> Unit

            ValueRule.Text, ValueRule.Html, ValueRule.RequestStatus -> forbidden.addWords(
                property.value,
                escapes = true,
            )

            else -> forbidden.addWords(property.value, escapes = false)
        }
        for (parameter in property.parameterNodes()) {
            for (value in parameter.values) {
                val rule = parameterValueRule(parameter.name, value)
                if (rule != ParameterRule.Keep && rule != ParameterRule.Zone) forbidden.addWords(value, escapes = true)
            }
        }
    }

    private fun rebuild(root: ICalComponent): ICalComponent {
        val stack = ArrayList<Frame>()
        stack += Frame(root, root.nameId == KnownNames.VTIMEZONE)
        while (true) {
            val frame = stack[stack.lastIndex]
            val children = frame.component.componentNodes
            if (frame.next < children.size) {
                val child = children[frame.next]
                stack += Frame(child, frame.inTimeZone || child.nameId == KnownNames.VTIMEZONE)
                continue
            }
            stack.removeAt(stack.lastIndex)
            val built = finish(frame)
            if (stack.isEmpty()) return built
            val parent = stack[stack.lastIndex]
            parent.children[parent.next++] = built
        }
    }

    private fun finish(frame: Frame): ICalComponent {
        val component = frame.component
        val properties = component.propertyNodes
        var changed = false
        val edited = Array(properties.size) { index ->
            val property = properties[index]
            val replacement = edit(property, frame.inTimeZone)
            if (replacement != null) changed = true
            replacement ?: property
        }
        val children = component.componentNodes
        val built = Array(children.size) { index ->
            val child = frame.children[index]!!
            if (child !== children[index]) changed = true
            child
        }
        return if (changed) component.withContent(edited, built) else component
    }

    // A value stays only when it is what its place says: a value of the standard's lists, a number, a duration, a date,
    // a rule, a known zone or producer (StructuralValues). Anything else is text, wherever it sits, also inside
    // VTIMEZONE. A text value that starts with `mailto:` is an address. A URI value that is a bare name (no
    // scheme: letters, digits, `-`, `_`, such as the alarm sound `ATTACH;VALUE=URI:Chord` of Apple) stays; a host, a
    // path or an address without a scheme is still a link.
    private fun valueRule(property: ICalProperty, inTimeZone: Boolean): ValueRule {
        val name = property.name
        val value = property.value
        val valueType = property.parameter(VALUE)?.value?.uppercase()
        val encoding = property.parameter(ENCODING)?.value?.uppercase()
        val html = property.parameter(FMTTYPE)?.value.equals(TEXT_HTML, ignoreCase = true)
        val rule = Policy.propertyRule(name, valueType, encoding, html, inTimeZone)
        return when {
            rule == ValueRule.Url && Policy.isBareName(value) -> ValueRule.Keep

            rule == ValueRule.Zone -> if (StructuralValues.knownZone(value)) ValueRule.Keep else ValueRule.Zone

            rule == ValueRule.Keep && name in Policy.allowedExtensions ->
                if (StructuralValues.extension(value)) ValueRule.Keep else ValueRule.Text

            rule == ValueRule.Keep -> if (StructuralValues.property(name, value)) ValueRule.Keep else ValueRule.Text

            rule == ValueRule.Date && !StructuralValues.isDateList(value) -> ValueRule.Text

            rule == ValueRule.Recurrence && !StructuralValues.isRule(value) -> ValueRule.Text

            (rule == ValueRule.Text || rule == ValueRule.Html) && Policy.isMailto(value) -> ValueRule.Address

            else -> rule
        }
    }

    // One value of a parameter: a structural value stays only when it is one, a TZID that is not a known zone gets its
    // pseudonym, a link that is a bare name stays, a text value that starts with `mailto:` is an address.
    private fun parameterValueRule(name: String, value: String): ParameterRule = when (
        val rule = Policy.parameterRule(name)
    ) {
        ParameterRule.Keep -> if (StructuralValues.parameter(name, value)) ParameterRule.Keep else ParameterRule.Text
        ParameterRule.Zone -> if (StructuralValues.knownZone(value)) ParameterRule.Keep else ParameterRule.Zone
        ParameterRule.Url -> if (Policy.isBareName(value)) ParameterRule.Keep else ParameterRule.Url
        ParameterRule.Text -> if (Policy.isMailto(value)) ParameterRule.Address else ParameterRule.Text
        else -> rule
    }

    // A place whose value stays when it is structural: a value there that turns out to be text is counted in the
    // report.
    private fun isStructuralPlace(name: String): Boolean = name in Policy.structuralProperties ||
        name in Policy.dateProperties || name in Policy.allowedExtensions || name in Policy.allowedDateExtensions ||
        name == "RRULE" || name == "EXRULE" || name == "TRIGGER"

    // Works on the physical line as the writer would write it: the line is lexed again after a one-line prefix, so a
    // name that starts with EF BB BF is not taken for a byte order mark and the line is never the first line of an
    // input. Replacements of the same length go into a copy of the logical line and are written back through
    // `offsetAt`, which keeps the folding, quoting and case of the vendor; a replacement of another length makes the
    // line be spliced together from its own bytes and folded at 75 octets when the source line was folded. The bytes
    // before the name stay in front of a spliced line (`lead`): a logical line that starts with a blank line and a
    // continuation line has a blank segment there, and without it the rebuilt line would begin with a space or a tab
    // and read as a continuation of the previous line.
    private fun edit(property: ICalProperty, inTimeZone: Boolean): ICalProperty? {
        val name = property.name
        val rule = valueRule(property, inTimeZone)
        if (rule == ValueRule.Text && isStructuralPlace(name)) stats.notToken(name)
        val parameters = property.parameterNodes()
        var editsParameters = false
        for (parameter in parameters) {
            if (parameter.values.any { parameterValueRule(parameter.name, it) != ParameterRule.Keep }) {
                editsParameters = true
            }
        }
        // The dates of a zone are its history of offsets: they are checked, never moved.
        val shift = if (inTimeZone) null else shifter
        val editsValue = when (rule) {
            ValueRule.Keep -> false
            ValueRule.Date, ValueRule.Recurrence -> shift != null
            else -> true
        }
        if (!editsValue && !editsParameters) {
            stats.property(name, AnonymizationStats.KEPT)
            for (parameter in parameters) stats.parameter(parameter.name, AnonymizationStats.KEPT)
            return null
        }
        val lexed = lex(property)
        val line = lexed.line
        val start = line.nameStart
        val logical = line.bytes.copyOfRange(start, line.valueEnd)
        val work = logical.copyOf()
        val replacements = ArrayList<Replacement>()
        check(line.parameterCount == parameters.size) { "The line of $name has a different number of parameters" }
        for (index in parameters.indices) {
            editParameter(line, index, parameters[index], start, work, replacements)
        }
        val valueStart = line.valueStart - start
        val valueReplaced = editValue(property, rule, shift, work, valueStart, work.size, replacements)
        val valueChanged = valueReplaced || !rangeEquals(work, logical, valueStart, work.size)
        val outcome = when {
            !valueChanged -> AnonymizationStats.KEPT
            rule == ValueRule.Date || rule == ValueRule.Recurrence -> AnonymizationStats.SHIFTED
            else -> AnonymizationStats.REPLACED
        }
        stats.property(name, outcome)
        if (replacements.isEmpty() && work.contentEquals(logical)) return null
        property.span?.let { stats.changed(it.startOffset, it.endOffset) }
        return nodeOrNull(physicalLine(lexed, logical, work, replacements), property)
            ?: withoutNewQuotes(property, lexed, logical, work, replacements)
    }

    // A replaced parameter value can bring quotes with it, and those pair up with a stray quote earlier in the line,
    // such as `X;=";SCHEMA=x/y:v`: the line then reads as other parameters. Such a value is scrambled in place instead,
    // which leaves every delimiter of the line where it was. Still not the original text, whatever happens to this
    // line.
    private fun withoutNewQuotes(
        property: ICalProperty,
        lexed: Lexed,
        logical: ByteArray,
        work: ByteArray,
        replacements: List<Replacement>,
    ): ICalProperty {
        for (replacement in replacements) {
            if (replacement.inParameter) scrambler.parameter(work, replacement.from, replacement.to)
        }
        val physical = physicalLine(lexed, logical, work, replacements.filterNot { it.inParameter })
        return checkNotNull(nodeOrNull(physical, property)) {
            "The anonymized line of a property does not read back as the same property"
        }
    }

    private fun physicalLine(
        lexed: Lexed,
        logical: ByteArray,
        work: ByteArray,
        replacements: List<Replacement>,
    ): ByteArray {
        if (replacements.isEmpty()) {
            stats.editedInPlace++
            return inPlace(lexed, logical, work)
        }
        stats.rebuilt++
        val line = lexed.line
        val rebuilt = splice(work, replacements)
        val lead = lexed.input.copyOfRange(LEX_PREFIX.size, line.offsetAt(line.nameStart))
        return lead + if (line.isFolded) fold(rebuilt) else rebuilt
    }

    // A parameter value that becomes an address, a link or a zone pseudonym needs quotes when it was not quoted and has
    // a colon (`mailto:`); a literal value (ICAL-S009, ICAL-S012) is replaced with its quote, which is why those
    // problems can disappear inside replaced values. Each value of a parameter is judged on its own.
    private fun editParameter(
        line: ContentLine,
        index: Int,
        parameter: ICalParameter,
        start: Int,
        work: ByteArray,
        replacements: MutableList<Replacement>,
    ) {
        val values = parameter.values
        var changed = false
        for (value in 0 until line.parameterValueCount(index)) {
            val decoded = values[value]
            val effective = parameterValueRule(parameter.name, decoded)
            if (effective == ParameterRule.Keep) continue
            changed = true
            val from = line.parameterValueStart(index, value) - start
            val to = line.parameterValueEnd(index, value) - start
            val text = when (effective) {
                ParameterRule.Address -> address(decoded)
                ParameterRule.Url -> url(decoded)
                ParameterRule.Zone -> zone(decoded)
                else -> null
            }
            if (text == null) {
                scrambler.parameter(work, from, to)
                continue
            }
            val quoted = line.isParameterValueQuoted(index, value)
            val needsQuotes = !quoted && text.any { it == ':' || it == ';' || it == ',' }
            replacements += Replacement(from, to, (if (needsQuotes) "\"$text\"" else text).encodeToByteArray(), true)
        }
        stats.parameter(parameter.name, if (changed) AnonymizationStats.REPLACED else AnonymizationStats.KEPT)
    }

    private fun editValue(
        property: ICalProperty,
        rule: ValueRule,
        shifter: DateShifter?,
        work: ByteArray,
        from: Int,
        to: Int,
        replacements: MutableList<Replacement>,
    ): Boolean {
        val replacement = when (rule) {
            ValueRule.Keep -> null

            ValueRule.Date -> {
                val unrecognized = shifter?.list(work, from, to) ?: 0
                if (unrecognized > 0) stats.unrecognizedDate(property.name, unrecognized)
                null
            }

            ValueRule.Recurrence -> {
                val shifter = shifter
                if (shifter != null) {
                    val unrecognized = shifter.rule(work, from, to)
                    if (unrecognized > 0) stats.unrecognizedDate(property.name, unrecognized)
                    if (ruleMeaningMayChange(work, from, to)) stats.rulesMayChange++
                }
                null
            }

            ValueRule.Text, ValueRule.Html -> {
                scrambler.text(work, from, to, html = rule == ValueRule.Html)
                null
            }

            ValueRule.RequestStatus -> {
                var index = from
                while (index < to && work[index] != SEMICOLON) index += if (work[index] == BACKSLASH) 2 else 1
                if (index < to) scrambler.text(work, index + 1, to, html = false)
                null
            }

            ValueRule.Binary -> {
                scrambler.binary(work, from, to)
                null
            }

            ValueRule.Address -> address(property.value)

            ValueRule.Uid -> uid(property.value)

            ValueRule.Zone -> zone(property.value)

            ValueRule.Url -> url(property.value)

            ValueRule.Geo -> GEO
        }
        if (replacement == null) return false
        replacements += Replacement(from, to, replacement.encodeToByteArray(), false)
        return true
    }

    // Addresses are compared without `mailto:` and in lower case, so `MAILTO:A@B` and `mailto:a@b` get one fake
    // address; the original spelling of the scheme is kept. An http(s) value is a link; any other scheme keeps its
    // name.
    private fun address(value: String): String {
        val mailto = Policy.isMailto(value)
        val bare = if (mailto) value.substring(Policy.MAILTO.length) else value
        val normalized = bare.lowercase()
        val scheme = if (mailto) null else scheme(normalized)
        if (scheme == HTTP || scheme == HTTPS) return url(value)
        val token = pseudonyms.token(Pseudonyms.TAG_ADDRESS, normalized.encodeToByteArray())
        return when {
            mailto -> "${value.substring(0, Policy.MAILTO.length)}u-$token@$DOMAIN"
            scheme != null -> "${value.substring(0, scheme.length)}:u-$token"
            '@' in bare -> "u-$token@$DOMAIN"
            else -> "u-$token"
        }
    }

    private fun uid(value: String): String =
        "${pseudonyms.token(Pseudonyms.TAG_UID, value.encodeToByteArray())}@$DOMAIN"

    // One pseudonym for one zone name in the whole calendar, so the TZID of a VTIMEZONE and every TZID= that refers to
    // it still match.
    private fun zone(value: String): String = "Zone-${pseudonyms.token(Pseudonyms.TAG_ZONE, value.encodeToByteArray())}"

    private fun url(value: String): String =
        "https://$DOMAIN/${pseudonyms.token(Pseudonyms.TAG_URL, value.encodeToByteArray())}"

    private fun scheme(value: String): String? {
        val colon = value.indexOf(':')
        if (colon < 2) return null
        for (index in 0 until colon) {
            val char = value[index]
            val allowed = char in 'a'..'z' ||
                (index > 0 && (char in '0'..'9' || char == '+' || char == '-' || char == '.'))
            if (!allowed) return null
        }
        return value.substring(0, colon)
    }

    private fun lex(property: ICalProperty): Lexed {
        raw.reset()
        property.writeSource(raw)
        val source = raw.toByteArray()
        val input = ByteArray(LEX_PREFIX.size + source.size)
        LEX_PREFIX.copyInto(input)
        source.copyInto(input, LEX_PREFIX.size)
        val lexer = ContentLexer(input, 0, input.size, LIMITS, IGNORE)
        checkNotNull(lexer.next()) { "The prefix line did not read" }
        val line = checkNotNull(lexer.next()) { "The line of ${property.name} did not read back" }
        return Lexed(line, input)
    }

    private fun inPlace(lexed: Lexed, logical: ByteArray, work: ByteArray): ByteArray {
        val line = lexed.line
        val physical = lexed.input.copyOfRange(LEX_PREFIX.size, lexed.input.size - LINE_END)
        for (index in logical.indices) {
            if (work[index] != logical[index]) {
                physical[line.offsetAt(line.nameStart + index) - LEX_PREFIX.size] = work[index]
            }
        }
        return physical
    }

    private fun splice(work: ByteArray, replacements: List<Replacement>): ByteArray {
        val sorted = replacements.sortedBy { it.from }
        var size = work.size
        for (replacement in sorted) size += replacement.bytes.size - (replacement.to - replacement.from)
        val out = ByteArray(size)
        var at = 0
        var index = 0
        for (replacement in sorted) {
            work.copyInto(out, at, index, replacement.from)
            at += replacement.from - index
            replacement.bytes.copyInto(out, at)
            at += replacement.bytes.size
            index = replacement.to
        }
        work.copyInto(out, at, index, work.size)
        return out
    }

    private fun fold(logical: ByteArray): ByteArray {
        val out = ByteArray(logical.size + (logical.size / (FOLD_OCTETS - MAX_UNIT) + 1) * FOLD.size)
        var at = 0
        var octets = 0
        var index = 0
        while (index < logical.size) {
            val length = utf8Unit(logical, index, logical.size) and UNIT_LENGTH
            if (octets + length > FOLD_OCTETS) {
                FOLD.copyInto(out, at)
                at += FOLD.size
                octets = 1
            }
            logical.copyInto(out, at, index, index + length)
            at += length
            octets += length
            index += length
        }
        return out.copyOf(at)
    }

    // The node is built by parsing `BEGIN:X`, the line and `END:X` with the tree builder, without charset detection, so
    // the anonymized tree is exactly what a reparse of the output gives. The result must be one property with the same
    // names and value counts, otherwise it is null. `detached` drops the span that points into this small document.
    private fun nodeOrNull(physical: ByteArray, original: ICalProperty): ICalProperty? {
        val document = ByteArray(NODE_PREFIX.size + physical.size + NODE_SUFFIX.size)
        NODE_PREFIX.copyInto(document)
        physical.copyInto(document, NODE_PREFIX.size)
        NODE_SUFFIX.copyInto(document, NODE_PREFIX.size + physical.size)
        val reader = ContentReader(document, 0, document.size, ContentSelection.Full, LIMITS, IGNORE)
        val component = TreeBuilder(reader, true, document.size, false).build().singleOrNull()
        val property = component?.propertyNodes?.singleOrNull()
        if (property == null || component.componentNodes.isNotEmpty() || !sameNames(property, original)) return null
        return property.detached()
    }

    private fun sameNames(property: ICalProperty, original: ICalProperty): Boolean {
        if (property.name != original.name) return false
        val parameters = property.parameterNodes()
        val originals = original.parameterNodes()
        if (parameters.size != originals.size) return false
        for (index in parameters.indices) {
            if (parameters[index].name != originals[index].name) return false
            if (parameters[index].values.size != originals[index].values.size) return false
        }
        return true
    }

    private fun rangeEquals(left: ByteArray, right: ByteArray, from: Int, to: Int): Boolean {
        for (index in from until to) {
            if (left[index] != right[index]) return false
        }
        return true
    }

    private class Frame(val component: ICalComponent, val inTimeZone: Boolean) {
        val children: Array<ICalComponent?> = arrayOfNulls(component.componentNodes.size)
        var next: Int = 0
    }

    private class Lexed(val line: ContentLine, val input: ByteArray)

    private class Replacement(val from: Int, val to: Int, val bytes: ByteArray, val inParameter: Boolean)

    private companion object {
        private const val VALUE = "VALUE"
        private const val ENCODING = "ENCODING"
        private const val FMTTYPE = "FMTTYPE"
        private const val TEXT_HTML = "text/html"
        private const val HTTP = "http"
        private const val HTTPS = "https"
        private const val DOMAIN = "example.invalid"
        private const val GEO = "0.0;0.0"
        private const val FOLD_OCTETS = 75
        private const val MAX_UNIT = 4
        private const val LINE_END = 2
        private const val SEMICOLON: Byte = 0x3B
        private const val BACKSLASH: Byte = 0x5C
        private val FOLD = "\r\n ".encodeToByteArray()
        private val LEX_PREFIX = "X:\r\n".encodeToByteArray()
        private val NODE_PREFIX = "BEGIN:X\r\n".encodeToByteArray()
        private val NODE_SUFFIX = "\r\nEND:X\r\n".encodeToByteArray()
        private val LIMITS = ParseLimits(Int.MAX_VALUE, 8, 0, 4, 4, NO_TIME_LIMIT, NO_INPUT_LIMIT)
        private val IGNORE = ProblemListener { }
    }
}

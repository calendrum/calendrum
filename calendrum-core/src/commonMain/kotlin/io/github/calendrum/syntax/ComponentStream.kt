package io.github.calendrum.syntax

import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem

/**
 * An iterator over the children of every `VCALENDAR` of a large feed, one component at a time.
 *
 * Get one with `ICal.parser().components(...)`. Each element is a complete `VEVENT`, `VTODO`, `VTIMEZONE` or other
 * child of a calendar, with everything inside it; memory is bounded by one block of input and the longest component.
 * While iterating, [calendarProperties] shows the properties of the calendar the last element came from, such as
 * `X-WR-TIMEZONE`. Problems are collected in [problems]; if the time budget runs out the stream ends and `ICAL-S029` is
 * among them.
 *
 * In strict mode [hasNext] and [next] throw [ICalParseException] at the first error, and keep throwing. A stream is not
 * thread-safe.
 */
public class ComponentStream internal constructor(
    private val reader: ContentReader,
    private val collector: ProblemCollector,
    retainSource: Boolean,
    inputSize: Int,
) : Iterator<ICalComponent> {
    private val builder = TreeBuilder(reader, retainSource, inputSize, true)
    private var pending: ICalComponent? = null
    private var pendingCalendar: Calendar? = null
    private var current: Calendar? = null
    private var open: Calendar? = null
    private var calendars = 0
    private var finished = false
    private var failure: ICalParseException? = null

    /**
     * The 0-based index of the `VCALENDAR` the last returned component belongs to, or -1 before the first or for a
     * component outside any calendar.
     */
    public val calendarIndex: Int get() = current?.index ?: -1

    /**
     * The properties of the `VCALENDAR` of the last returned component that were read so far: those before it in the
     * input.
     */
    public val calendarProperties: List<ICalProperty> get() = current?.properties() ?: ArrayView(EMPTY_PROPERTIES)

    /** The problems found so far, in the order they were found; it grows as the stream is consumed. */
    public val problems: List<Problem> get() = ArrayView(collector.problems.toTypedArray())

    /**
     * Returns whether another component follows, reading ahead as far as needed.
     *
     * @throws ICalParseException in strict mode, at the first error; later calls rethrow it.
     */
    override fun hasNext(): Boolean {
        failure?.let { throw it }
        if (pending != null) return true
        if (finished) return false
        try {
            advance()
        } catch (e: ICalParseException) {
            failure = e
            finished = true
            pending = null
            throw e
        }
        return pending != null
    }

    /**
     * Returns the next component and moves on.
     *
     * @throws kotlin.NoSuchElementException if the stream has ended.
     * @throws ICalParseException in strict mode, at the first error.
     */
    override fun next(): ICalComponent {
        if (!hasNext()) throw NoSuchElementException("The component stream has ended")
        val component = pending!!
        pending = null
        current = pendingCalendar
        return component
    }

    // The same as `hasNext` and `next` (`nextOrThrow`), with `@Throws`: the methods of Iterator cannot declare
    // exceptions, and Swift would crash on a strict-mode problem.

    /**
     * Same as [hasNext]; it exists so that Java callers see the checked [ICalParseException].
     *
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun hasNextOrThrow(): Boolean = hasNext()

    /**
     * Same as [next]; it exists so that Java callers see the checked [ICalParseException].
     *
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun nextOrThrow(): ICalComponent = next()

    /** Returns the remaining components as a sequence that can be iterated only once. */
    public fun asSequence(): Sequence<ICalComponent> = Sequence { this }.constrainOnce()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "ComponentStream(calendarIndex=$calendarIndex, finished=$finished)"

    private fun advance() {
        while (true) {
            when (reader.next()) {
                ContentReader.END_OF_INPUT -> {
                    finished = true
                    return
                }

                ContentReader.BEGIN -> {
                    val calendar = open
                    if (reader.depth == 1 && reader.componentId == KnownNames.VCALENDAR) {
                        open = Calendar(calendars++)
                        continue
                    }
                    pendingCalendar = if (reader.depth == 1) null else calendar
                    pending = builder.readComponent()
                    return
                }

                ContentReader.PROPERTY -> if (reader.depth == 1) open?.add(builder.readProperty())

                else -> if (reader.depth == 1) open = null
            }
        }
    }

    private class Calendar(val index: Int) {
        private var items = arrayOfNulls<ICalProperty>(INITIAL_PROPERTIES)
        private var count = 0
        private var view: List<ICalProperty>? = null

        fun add(property: ICalProperty) {
            if (count == items.size) items = items.copyOf(count * 2)
            items[count++] = property
            view = null
        }

        fun properties(): List<ICalProperty> {
            val cached = view
            if (cached != null) return cached
            @Suppress("UNCHECKED_CAST")
            val snapshot = ArrayView(items.copyOf(count) as Array<ICalProperty>)
            view = snapshot
            return snapshot
        }
    }

    private companion object {
        private const val INITIAL_PROPERTIES = 8
    }
}

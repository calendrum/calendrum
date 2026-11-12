package io.github.calendrum.fixup

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.model.VCalendar
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.describe
import io.github.calendrum.validate.ElementPath

/**
 * A repair for a known quirk of a calendar producer, applied after reading (to a [VCalendar]) or while reading (as line
 * interceptors).
 *
 * The repairs are opt-in and never run on their own: pick one from [Fixups] or build your own. A fix does not change
 * the calendar it is given; it returns a new one and lists what it changed.
 */
@SubclassOptInRequired(ExperimentalICalApi::class)
public interface Fixup {
    /** A short stable name of the fix, such as `windows-tzids`. */
    public val id: String

    /**
     * Returns factories of line interceptors that make the same repair while the feed is read, to be passed to
     * `ParseOptions.Builder.intercept`. The list is empty for a fix that needs the whole tree.
     */
    public fun interceptors(): List<() -> LineInterceptor>

    /**
     * Applies the fix to [calendar] and returns the repaired calendar with a list of the changes. When nothing needed
     * repair the same calendar is returned and [FixupResult.isChanged] is false.
     */
    public fun apply(calendar: VCalendar): FixupResult
}

/**
 * One change made by a fix.
 *
 * @property fixup the [Fixup.id] of the fix that made it.
 * @property path the element that was changed or added.
 */
public class FixupChange internal constructor(public val fixup: String, public val path: ElementPath) {
    /** Returns the fix id and the path. */
    override fun toString(): String = "$fixup $path"
}

/**
 * The result of applying a [Fixup].
 *
 * @property calendar the repaired calendar.
 * @property changes what was changed, in the order it was done; never the values.
 */
public class FixupResult internal constructor(public val calendar: VCalendar, public val changes: List<FixupChange>) {
    /** Whether the fix changed anything. */
    public val isChanged: Boolean get() = changes.isNotEmpty()

    /** Returns the number of changes. */
    override fun toString(): String = describe("FixupResult", "changes", changes.size)
}

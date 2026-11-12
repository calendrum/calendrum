package io.github.calendrum

/**
 * Receives problems as a reader or an expander finds them.
 *
 * The listener is called on the thread that does the work, in the order the problems are found. It must not throw: an
 * exception thrown by a listener propagates out of the call that was running.
 */
public fun interface ProblemListener {
    /** Called once for every reported problem. */
    public fun onProblem(problem: Problem)
}

package io.github.calendrum

/** Internal API, not a contract: shared between Calendrum modules and free to change or disappear in any release. */
@RequiresOptIn(
    message =
    "Internal Calendrum API shared between Calendrum modules. It is not a contract for users and may change " +
        "or disappear in any release without deprecation.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.TYPEALIAS,
)
public annotation class InternalICalApi

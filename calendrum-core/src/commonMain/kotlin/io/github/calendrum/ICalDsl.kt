package io.github.calendrum

/**
 * Marks the receivers of the builder DSLs, so that an inner block cannot call members of an outer builder by accident.
 */
@DslMarker
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
public annotation class ICalDsl

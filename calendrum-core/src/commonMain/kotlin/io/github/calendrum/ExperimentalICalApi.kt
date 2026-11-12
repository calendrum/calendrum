package io.github.calendrum

/**
 * Marks public API that is not frozen for 1.0: it works and is tested, but its shape may change in a minor release.
 *
 * Marked are the extension points of the registry (your own [io.github.calendrum.model.ICalExtension],
 * [io.github.calendrum.model.PropertyCodec] and [io.github.calendrum.model.ParameterCodec], [io.github.calendrum.model.ICalRegistry.Builder]),
 * schemas ([io.github.calendrum.model.IcsSchema]), byte-level patches ([io.github.calendrum.model.ICalPatch]), line interceptors
 * ([io.github.calendrum.syntax.LineInterceptor]), the text format of [io.github.calendrum.syntax.Fingerprints], the feed publisher
 * ([io.github.calendrum.model.FeedPublisher]), your own [io.github.calendrum.fixup.Fixup] and the kinds of
 * [io.github.calendrum.itip.IMip.untrustedContent]. Using them needs `@OptIn(ExperimentalICalApi::class)`; implementing a
 * marked interface or extending a marked class needs it too.
 *
 * What is not promised: source and binary compatibility between minor releases (a member may be renamed, added to an
 * interface you implement or removed, with a line in the changelog but without a deprecation cycle), and the stability of
 * the fingerprint text across releases. What is promised: the same behaviour and the same tests as the rest of the
 * library, and a changelog entry for every change. The rest of the public API follows the deprecation policy of 1.0.
 */
@MustBeDocumented
@RequiresOptIn(
    message = "This Calendrum API is not frozen for 1.0 and may change in a minor release.",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.TYPEALIAS,
)
public annotation class ExperimentalICalApi

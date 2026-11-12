#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class CalendrumCoreAlarmAction, CalendrumCoreAlarmActionCompanion, CalendrumCoreAlarmTime, CalendrumCoreAttendee, CalendrumCoreAttendeeBuilder, CalendrumCoreAttendeeCompanion, CalendrumCoreAttendeeList, CalendrumCoreBuildDefaults, CalendrumCoreBusyInterval, CalendrumCoreBusyStatus, CalendrumCoreBusyStatusCompanion, CalendrumCoreCalendrum, CalendrumCoreCanonical, CalendrumCoreCardinality, CalendrumCoreCategories, CalendrumCoreClassification, CalendrumCoreClassificationCompanion, CalendrumCoreComponentBuilder<B>, CalendrumCoreComponentDefinition, CalendrumCoreComponentFingerprint, CalendrumCoreComponentKey, CalendrumCoreComponentList<T>, CalendrumCoreComponentPatch, CalendrumCoreComponentStream, CalendrumCoreConference, CalendrumCoreConferenceFeature, CalendrumCoreConferenceFeatureCompanion, CalendrumCoreConflict, CalendrumCoreContact, CalendrumCoreContentEventType, CalendrumCoreCuType, CalendrumCoreCuTypeCompanion, CalendrumCoreDecodeContext, CalendrumCoreDecodeContextCompanion, CalendrumCoreDecodedMissing, CalendrumCoreElementPath, CalendrumCoreElementPathCompanion, CalendrumCoreElementPathSegment, CalendrumCoreEncodeContext, CalendrumCoreEncoding, CalendrumCoreEncodingCompanion, CalendrumCoreEventStatus, CalendrumCoreEventStatusCompanion, CalendrumCoreExDate, CalendrumCoreFbType, CalendrumCoreFbTypeCompanion, CalendrumCoreFeature, CalendrumCoreFeatureCompanion, CalendrumCoreFingerprintDiff, CalendrumCoreFingerprintOptions, CalendrumCoreFingerprintOptionsBuilder, CalendrumCoreFingerprintOptionsCompanion, CalendrumCoreFingerprints, CalendrumCoreFingerprintsCompanion, CalendrumCoreFixupChange, CalendrumCoreFixupResult, CalendrumCoreFixups, CalendrumCoreFreeBusy, CalendrumCoreFreeBusyCalc, CalendrumCoreFrequency, CalendrumCoreGeo, CalendrumCoreICal, CalendrumCoreICalComponent, CalendrumCoreICalComponentBuilder, CalendrumCoreICalComponentCompanion, CalendrumCoreICalDuration, CalendrumCoreICalDurationCompanion, CalendrumCoreICalParameter, CalendrumCoreICalParameterCompanion, CalendrumCoreICalParser, CalendrumCoreICalPatchBuilder, CalendrumCoreICalPeriod, CalendrumCoreICalProperty, CalendrumCoreICalPropertyBuilder, CalendrumCoreICalPropertyCompanion, CalendrumCoreICalReader, CalendrumCoreICalRegistry, CalendrumCoreICalRegistryBuilder, CalendrumCoreICalRegistryCompanion, CalendrumCoreICalWriter, CalendrumCoreIMip, CalendrumCoreITip, CalendrumCoreITipOutcome, CalendrumCoreITipResult, CalendrumCoreIcsSchema<T>, CalendrumCoreIcsSchemaField<R>, CalendrumCoreIcsSchemaValues, CalendrumCoreImage, CalendrumCoreImageDisplay, CalendrumCoreImageDisplayCompanion, CalendrumCoreInterceptedLine, CalendrumCoreJournalStatus, CalendrumCoreJournalStatusCompanion, CalendrumCoreKotlinArray<T>, CalendrumCoreKotlinByteArray, CalendrumCoreKotlinByteIterator, CalendrumCoreKotlinEnum<E>, CalendrumCoreKotlinEnumCompanion, CalendrumCoreKotlinException, CalendrumCoreKotlinIllegalArgumentException, CalendrumCoreKotlinInstant, CalendrumCoreKotlinInstantCompanion, CalendrumCoreKotlinIntArray, CalendrumCoreKotlinIntIterator, CalendrumCoreKotlinNothing, CalendrumCoreKotlinRuntimeException, CalendrumCoreKotlinThrowable, CalendrumCoreKotlinx_datetimeDateTimePeriod, CalendrumCoreKotlinx_datetimeDateTimePeriodCompanion, CalendrumCoreKotlinx_datetimeDayOfWeek, CalendrumCoreKotlinx_datetimeDayOfWeekNames, CalendrumCoreKotlinx_datetimeDayOfWeekNamesCompanion, CalendrumCoreKotlinx_datetimeFixedOffsetTimeZone, CalendrumCoreKotlinx_datetimeFixedOffsetTimeZoneCompanion, CalendrumCoreKotlinx_datetimeLocalDate, CalendrumCoreKotlinx_datetimeLocalDateCompanion, CalendrumCoreKotlinx_datetimeLocalDateProgression, CalendrumCoreKotlinx_datetimeLocalDateProgressionCompanion, CalendrumCoreKotlinx_datetimeLocalDateRange, CalendrumCoreKotlinx_datetimeLocalDateRangeCompanion, CalendrumCoreKotlinx_datetimeLocalDateTime, CalendrumCoreKotlinx_datetimeLocalDateTimeCompanion, CalendrumCoreKotlinx_datetimeLocalTime, CalendrumCoreKotlinx_datetimeLocalTimeCompanion, CalendrumCoreKotlinx_datetimeMonth, CalendrumCoreKotlinx_datetimeMonthNames, CalendrumCoreKotlinx_datetimeMonthNamesCompanion, CalendrumCoreKotlinx_datetimeOverloadMarker, CalendrumCoreKotlinx_datetimePadding, CalendrumCoreKotlinx_datetimeTimeZone, CalendrumCoreKotlinx_datetimeTimeZoneCompanion, CalendrumCoreKotlinx_datetimeUtcOffset, CalendrumCoreKotlinx_datetimeUtcOffsetCompanion, CalendrumCoreKotlinx_serialization_coreSerialKind, CalendrumCoreKotlinx_serialization_coreSerializersModule, CalendrumCoreLink, CalendrumCoreLintReport, CalendrumCoreLintRules, CalendrumCoreLintRulesBuilder, CalendrumCoreLintRulesCompanion, CalendrumCoreLinter, CalendrumCoreLocation, CalendrumCoreMethod, CalendrumCoreMethodCompanion, CalendrumCoreMonthNum, CalendrumCoreOccurrence, CalendrumCoreOccurrenceCursor, CalendrumCoreOccurrenceOptions, CalendrumCoreOccurrenceOptionsBuilder, CalendrumCoreOccurrenceOptionsCompanion, CalendrumCoreOccurrenceSet, CalendrumCoreOccurrenceSetCompanion, CalendrumCoreOrganizer, CalendrumCoreOrganizerBuilder, CalendrumCoreOrganizerCompanion, CalendrumCoreParameterDefinition, CalendrumCoreParameterKey<T>, CalendrumCoreParameters, CalendrumCoreParseLimits, CalendrumCoreParseLimitsBuilder, CalendrumCoreParseLimitsCompanion, CalendrumCoreParseMode, CalendrumCoreParseOptions, CalendrumCoreParseOptionsBuilder, CalendrumCoreParseOptionsCompanion, CalendrumCoreParseResult, CalendrumCorePartStat, CalendrumCorePartStatCompanion, CalendrumCoreParticipant, CalendrumCoreParticipantBuilder, CalendrumCoreParticipantCompanion, CalendrumCoreParticipantType, CalendrumCoreParticipantTypeCompanion, CalendrumCorePatchChange, CalendrumCorePatchChangeKind, CalendrumCorePatchResult, CalendrumCorePatchSelector, CalendrumCoreProblem, CalendrumCoreProblemCode, CalendrumCoreProblemCodeCompanion, CalendrumCoreProperties, CalendrumCorePropertyDefinition, CalendrumCorePropertyKey<T>, CalendrumCorePropertyPatch, CalendrumCoreProximity, CalendrumCoreProximityCompanion, CalendrumCoreRDate, CalendrumCoreRecur, CalendrumCoreRecurBuilder, CalendrumCoreRecurCompanion, CalendrumCoreRecurPart, CalendrumCoreRecurrenceId, CalendrumCoreRecurrenceLimits, CalendrumCoreRecurrenceLimitsBuilder, CalendrumCoreRecurrenceLimitsCompanion, CalendrumCoreRecurrenceOptions, CalendrumCoreRecurrenceOptionsBuilder, CalendrumCoreRecurrenceOptionsCompanion, CalendrumCoreRecurrenceRange, CalendrumCoreRecurrenceRangeCompanion, CalendrumCoreRecurrenceSet, CalendrumCoreRecurrenceSetBuilder, CalendrumCoreRecurrenceSetCompanion, CalendrumCoreRelType, CalendrumCoreRelTypeCompanion, CalendrumCoreRelated, CalendrumCoreRelatedCompanion, CalendrumCoreRelatedTo, CalendrumCoreRequestStatus, CalendrumCoreResolveContext, CalendrumCoreResolveContextCompanion, CalendrumCoreResourceType, CalendrumCoreResourceTypeCompanion, CalendrumCoreResources, CalendrumCoreRevision, CalendrumCoreRfc5545, CalendrumCoreRfc7986, CalendrumCoreRfc9073, CalendrumCoreRfc9074, CalendrumCoreRfc9253, CalendrumCoreRole, CalendrumCoreRoleCompanion, CalendrumCoreRuleSet, CalendrumCoreRuleSetBuilder, CalendrumCoreRuleSetCompanion, CalendrumCoreSchemaResult<T>, CalendrumCoreSelection, CalendrumCoreSelectionBuilder, CalendrumCoreSelectionCompanion, CalendrumCoreSenderCheck, CalendrumCoreSeries, CalendrumCoreSeriesView, CalendrumCoreSeverity, CalendrumCoreSkip, CalendrumCoreSourceSpan, CalendrumCoreStartMismatch, CalendrumCoreStructuredData, CalendrumCoreStyledDescription, CalendrumCoreThisAndFuture, CalendrumCoreTimeZoneResolvers, CalendrumCoreTimeZoneSelection, CalendrumCoreTodoStatus, CalendrumCoreTodoStatusCompanion, CalendrumCoreTransparency, CalendrumCoreTransparencyCompanion, CalendrumCoreUidGeneratorCompanion, CalendrumCoreUnknownZonePolicy, CalendrumCoreUntrustedContent, CalendrumCoreUntrustedKind, CalendrumCoreVAlarm, CalendrumCoreVAlarmBuilder, CalendrumCoreVAlarmCompanion, CalendrumCoreVCalendar, CalendrumCoreVCalendarBuilder, CalendrumCoreVCalendarCompanion, CalendrumCoreVEvent, CalendrumCoreVEventBuilder, CalendrumCoreVEventCompanion, CalendrumCoreVFreeBusy, CalendrumCoreVFreeBusyBuilder, CalendrumCoreVFreeBusyCompanion, CalendrumCoreVJournal, CalendrumCoreVJournalBuilder, CalendrumCoreVJournalCompanion, CalendrumCoreVLocation, CalendrumCoreVLocationBuilder, CalendrumCoreVLocationCompanion, CalendrumCoreVResource, CalendrumCoreVResourceBuilder, CalendrumCoreVResourceCompanion, CalendrumCoreVTimeZone, CalendrumCoreVTimeZoneBuilder, CalendrumCoreVTimeZoneCompanion, CalendrumCoreVTimeZoneDaylight, CalendrumCoreVTimeZoneDaylightCompanion, CalendrumCoreVTimeZoneObservance, CalendrumCoreVTimeZoneObservanceBuilder, CalendrumCoreVTimeZoneStandard, CalendrumCoreVTimeZoneStandardCompanion, CalendrumCoreVTodo, CalendrumCoreVTodoBuilder, CalendrumCoreVTodoCompanion, CalendrumCoreValidationIssue, CalendrumCoreValidationReport, CalendrumCoreValidator, CalendrumCoreValueType, CalendrumCoreValueTypeCompanion, CalendrumCoreVendorProperties, CalendrumCoreWeekdayNum, CalendrumCoreWriteOptions, CalendrumCoreWriteOptionsBuilder, CalendrumCoreWriteOptionsCompanion, CalendrumCoreZoneTransition;

@protocol CalendrumCoreAttach, CalendrumCoreByteInput, CalendrumCoreByteOutput, CalendrumCoreDecoded, CalendrumCoreFixup, CalendrumCoreICalDateTime, CalendrumCoreICalExtension, CalendrumCoreICalTimeZone, CalendrumCoreKotlinAnnotation, CalendrumCoreKotlinAppendable, CalendrumCoreKotlinClock, CalendrumCoreKotlinClosedRange, CalendrumCoreKotlinCollection, CalendrumCoreKotlinComparable, CalendrumCoreKotlinIterable, CalendrumCoreKotlinIterator, CalendrumCoreKotlinKAnnotatedElement, CalendrumCoreKotlinKClass, CalendrumCoreKotlinKClassifier, CalendrumCoreKotlinKDeclarationContainer, CalendrumCoreKotlinOpenEndRange, CalendrumCoreKotlinSequence, CalendrumCoreKotlinTimeMark, CalendrumCoreKotlinTimeSource, CalendrumCoreKotlinTimeSourceWithComparableMarks, CalendrumCoreKotlinx_datetimeDateTimeFormat, CalendrumCoreKotlinx_datetimeDateTimeFormatBuilder, CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithDate, CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithDateTime, CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithTime, CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset, CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithYearMonth, CalendrumCoreKotlinx_serialization_coreCompositeDecoder, CalendrumCoreKotlinx_serialization_coreCompositeEncoder, CalendrumCoreKotlinx_serialization_coreDecoder, CalendrumCoreKotlinx_serialization_coreDeserializationStrategy, CalendrumCoreKotlinx_serialization_coreEncoder, CalendrumCoreKotlinx_serialization_coreKSerializer, CalendrumCoreKotlinx_serialization_coreSerialDescriptor, CalendrumCoreKotlinx_serialization_coreSerializationStrategy, CalendrumCoreKotlinx_serialization_coreSerializersModuleCollector, CalendrumCoreLineInterceptor, CalendrumCoreParameterCodec, CalendrumCoreProblemListener, CalendrumCorePropertyCodec, CalendrumCoreTimeZoneResolver, CalendrumCoreTrigger, CalendrumCoreUidGenerator, CalendrumCoreVTimeZoneSource, CalendrumCoreValidationRule;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface CalendrumCoreBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface CalendrumCoreBase (CalendrumCoreBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface CalendrumCoreMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface CalendrumCoreMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorCalendrumCoreKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface CalendrumCoreNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end

__attribute__((swift_name("KotlinByte")))
@interface CalendrumCoreByte : CalendrumCoreNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface CalendrumCoreUByte : CalendrumCoreNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface CalendrumCoreShort : CalendrumCoreNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface CalendrumCoreUShort : CalendrumCoreNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface CalendrumCoreInt : CalendrumCoreNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface CalendrumCoreUInt : CalendrumCoreNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface CalendrumCoreLong : CalendrumCoreNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface CalendrumCoreULong : CalendrumCoreNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface CalendrumCoreFloat : CalendrumCoreNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface CalendrumCoreDouble : CalendrumCoreNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface CalendrumCoreBoolean : CalendrumCoreNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end


/**
 * A pull source of bytes, the platform-independent counterpart of an input stream.
 *
 * Adapters for `java.io.InputStream`, kotlinx-io `Source` and Okio `BufferedSource` live in the optional modules. The
 * parser never closes an input; closing is the caller's job.
 */
__attribute__((swift_name("ByteInput")))
@protocol CalendrumCoreByteInput
@required

/**
 * Reads up to [length] bytes into [buffer] starting at [offset].
 *
 * @return the number of bytes read, at least 1, or -1 at the end of the input.
 */
- (int32_t)readBuffer:(CalendrumCoreKotlinByteArray *)buffer offset:(int32_t)offset length:(int32_t)length __attribute__((swift_name("read(buffer:offset:length:)")));
@end


/**
 * A push sink of bytes, the platform-independent counterpart of an output stream.
 *
 * The writer never flushes or closes an output; both are the caller's job.
 */
__attribute__((swift_name("ByteOutput")))
@protocol CalendrumCoreByteOutput
@required

/** Writes [length] bytes of [buffer] starting at [offset]. */
- (void)writeBuffer:(CalendrumCoreKotlinByteArray *)buffer offset:(int32_t)offset length:(int32_t)length __attribute__((swift_name("write(buffer:offset:length:)")));
@end


/** Information about the library itself. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum")))
@interface CalendrumCoreCalendrum : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Information about the library itself. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)calendrum __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreCalendrum *shared __attribute__((swift_name("shared")));

/** The version of the library, taken from the build. */
@property (readonly) NSString *VERSION __attribute__((swift_name("VERSION")));
@end


/**
 * Entry point of the library: parses iCalendar text and bytes, writes component trees back, and fingerprints feeds.
 *
 * Every function uses shared, immutable default settings, so nothing here has global mutable state and all of it is
 * safe to call from any thread. Use [parser] or [writer] with options to change the behavior, or the `ICal.parser { ...
 * }` extension to build [ParseOptions] with a DSL.
 *
 * @sample io.github.calendrum.samples.parseText
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICal")))
@interface CalendrumCoreICal : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Entry point of the library: parses iCalendar text and bytes, writes component trees back, and fingerprints feeds.
 *
 * Every function uses shared, immutable default settings, so nothing here has global mutable state and all of it is
 * safe to call from any thread. Use [parser] or [writer] with options to change the behavior, or the `ICal.parser { ...
 * }` extension to build [ParseOptions] with a DSL.
 *
 * @sample io.github.calendrum.samples.parseText
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)iCal __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreICal *shared __attribute__((swift_name("shared")));

/**
 * Fingerprints the components of the feed in [bytes] with the default [FingerprintOptions].
 *
 * One pass over the bytes finds the children of every `VCALENDAR` and hashes them without decoding values, so two
 * fingerprints of a feed show which components were added, removed or changed (see [Fingerprints.diff]). `DTSTAMP`
 * is ignored by default.
 *
 * @param bytes the complete iCalendar content.
 */
- (CalendrumCoreFingerprints *)fingerprintsBytes:(CalendrumCoreKotlinByteArray *)bytes __attribute__((swift_name("fingerprints(bytes:)")));

/**
 * Fingerprints the components of the feed in [bytes] with the given [options].
 *
 * @param bytes the complete iCalendar content.
 * @param options which properties take part in the hash.
 */
- (CalendrumCoreFingerprints *)fingerprintsBytes:(CalendrumCoreKotlinByteArray *)bytes options:(CalendrumCoreFingerprintOptions *)options __attribute__((swift_name("fingerprints(bytes:options:)")));

/**
 * Parses iCalendar [bytes] with the default options: lenient mode, the default limits and a retained source.
 *
 * The encoding is detected from a byte order mark and from the data: UTF-8 is read as is, UTF-16 and UTF-32 are
 * transcoded, and windows-1251, KOI8-R and windows-1252 are recognized by a heuristic. What was decided is reported
 * in [ParseResult.charset] and by the problems `ICAL-S024` to `ICAL-S028`. Lenient parsing never throws on bad
 * data.
 *
 * @param bytes the complete iCalendar content.
 * @return the parsed components with the problems found.
 */
- (CalendrumCoreParseResult *)parseBytes:(CalendrumCoreKotlinByteArray *)bytes __attribute__((swift_name("parse(bytes:)")));

/**
 * Parses iCalendar [text] with the default options: lenient mode, the default limits and a retained source.
 *
 * Lenient parsing never throws on bad data. Problems found in the input are collected in [ParseResult.problems];
 * the result is a lossless tree of top-level components, usually one `VCALENDAR` ([RFC 5545
 * §3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.4)). Values are not decoded here; they are decoded on
 * first access.
 *
 * Positions in problems count bytes of the UTF-8 form of [text]. Use [parser] with `ParseMode.Strict` to get an
 * [ICalParseException] on the first error instead.
 *
 * @param text the complete iCalendar content.
 * @return the parsed components with the problems found.
 * @sample io.github.calendrum.samples.parseText
 */
- (CalendrumCoreParseResult *)parseText:(NSString *)text __attribute__((swift_name("parse(text:)")));

/**
 * Returns the shared parser with the default options.
 *
 * The parser is immutable and thread-safe, so the same instance is returned on every call.
 */
- (CalendrumCoreICalParser *)parser __attribute__((swift_name("parser()")));

/**
 * Creates a parser that applies [options].
 *
 * The parser is immutable and thread-safe; keep it and reuse it for many inputs.
 */
- (CalendrumCoreICalParser *)parserOptions:(CalendrumCoreParseOptions *)options __attribute__((swift_name("parser(options:)")));

/**
 * Returns the shared writer with the default options: byte-stable output in the order of the tree.
 *
 * The writer is immutable and thread-safe.
 */
- (CalendrumCoreICalWriter *)writer __attribute__((swift_name("writer()")));

/**
 * Creates a writer that applies [options].
 *
 * The writer is immutable and thread-safe.
 */
- (CalendrumCoreICalWriter *)writerOptions:(CalendrumCoreWriteOptions *)options __attribute__((swift_name("writer(options:)")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface CalendrumCoreKotlinThrowable : CalendrumCoreBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (CalendrumCoreKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface CalendrumCoreKotlinException : CalendrumCoreKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * Thrown by a parser in strict mode when the input has a problem of severity [Severity.Error].
 *
 * The parser stops at the first error, so [problems] ends with it. In lenient mode parsing never throws on data.
 *
 * The message is the text of the last error in [problems]. It never contains property values.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalParseException")))
@interface CalendrumCoreICalParseException : CalendrumCoreKotlinException
- (instancetype)initWithProblems:(NSArray<CalendrumCoreProblem *> *)problems __attribute__((swift_name("init(problems:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));

/** The problems found up to and including the error that stopped the parse, in the order they were found. */
@property (readonly) NSArray<CalendrumCoreProblem *> *problems __attribute__((swift_name("problems")));
@end


/**
 * A problem found while reading, decoding or checking iCalendar data: a stable [code], a [severity], a position and a
 * short message.
 *
 * Problems are reported instead of thrown wherever the data can be read at all. The text never contains property or
 * parameter values, so a problem can be logged without leaking user data. See [ProblemCode] for every code.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Problem")))
@interface CalendrumCoreProblem : CalendrumCoreBase

/**
 * Returns the code, severity, message and position, such as `ICAL-S003 Warning: bare LF used as a line ending at
 * 3:7 (offset 12)`. It never contains values from the input.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The stable code of the problem, such as `ICAL-S003`. */
@property (readonly) CalendrumCoreProblemCode *code __attribute__((swift_name("code")));

/**
 * A short human-readable text: the fixed message of the code, followed by an explanation after a colon when the
 * code has one (for example the name of a detected charset). It never contains data from the input.
 */
@property (readonly) NSString *message __attribute__((swift_name("message")));

/** The severity of [code]. */
@property (readonly) CalendrumCoreSeverity *severity __attribute__((swift_name("severity")));

/**
 * Where in the input the problem was found; a point, so [SourceSpan.startOffset] equals [SourceSpan.endOffset].
 *
 * Nodes built by hand or edited by an interceptor have no position in the original input; see [SourceSpan].
 */
@property (readonly) CalendrumCoreSourceSpan *span __attribute__((swift_name("span")));
@end


/**
 * The stable identifier of a kind of [Problem], such as `ICAL-S003`, with its severity and fixed message.
 *
 * This is an open enumeration: every code the library emits is a constant of the companion, [of] returns the constant
 * for an identifier, and an identifier the library does not know yields a new code with severity [Severity.Warning].
 * Codes are equal when their [id] is equal. A code never changes its meaning and is never reused.
 *
 * The series are `ICAL-S` (syntax and encoding), `ICAL-V` (values), `ICAL-M` (model), `ICAL-R` (recurrence), `ICAL-Z`
 * (time zones), `ICAL-O` (occurrences), `ICAL-L` (validation and lint) and `ICAL-J` (jCal).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProblemCode")))
@interface CalendrumCoreProblemCode : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreProblemCodeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a code with the same [id]. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the [id]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the [id], such as `ICAL-S003`. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The identifier of the code, for example `ICAL-S003`. */
@property (readonly) NSString *id __attribute__((swift_name("id")));

/** How serious the problems with this code are. */
@property (readonly) CalendrumCoreSeverity *severity __attribute__((swift_name("severity")));
@end


/** The constants for every code of the library, and a lookup by identifier. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ProblemCode.Companion")))
@interface CalendrumCoreProblemCodeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants for every code of the library, and a lookup by identifier. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreProblemCodeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the code with the given [id], ignoring case.
 *
 * For an identifier the library knows this is the constant; for any other a new code is created with the
 * upper-case identifier and the severity [Severity.Warning].
 *
 * @throws IllegalArgumentException if [id] is empty.
 */
- (CalendrumCoreProblemCode *)ofId:(NSString *)id __attribute__((swift_name("of(id:)")));

/**
 * `ICAL-O003` (Warning): the `REPEAT` of an alarm is beyond the limit of alarm repetitions. The extra
 * repetitions are dropped.
 */
@property (readonly) CalendrumCoreProblemCode *AlarmRepeatLimit __attribute__((swift_name("AlarmRepeatLimit")));

/**
 * `ICAL-L022` (Warning): a `TZID` is not an IANA zone but a Windows name, a display name, a link or a vendor
 * prefix. Found by the linter, once per `TZID`.
 */
@property (readonly) CalendrumCoreProblemCode *AliasTimeZoneId __attribute__((swift_name("AliasTimeZoneId")));

/**
 * `ICAL-S002` (Warning): a bare CR is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumCoreProblemCode *BareCr __attribute__((swift_name("BareCr")));

/**
 * `ICAL-S003` (Warning): a bare LF is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumCoreProblemCode *BareLf __attribute__((swift_name("BareLf")));

/** `ICAL-V015` (Warning): a `BINARY` value without base64 padding. The padding is added. */
@property (readonly) CalendrumCoreProblemCode *Base64Padding __attribute__((swift_name("Base64Padding")));

/** `ICAL-V014` (Warning): whitespace inside a `BINARY` value. It is skipped. */
@property (readonly) CalendrumCoreProblemCode *Base64Whitespace __attribute__((swift_name("Base64Whitespace")));

/**
 * `ICAL-S004` (Warning): a blank line, which the format does not allow. The line is skipped. Reported once per
 * input.
 */
@property (readonly) CalendrumCoreProblemCode *BlankLine __attribute__((swift_name("BlankLine")));

/**
 * `ICAL-S027` (Warning): the charset hint contradicts the data, for example a byte order mark of another
 * encoding or clear UTF-8 data under a single-byte hint. The data decides.
 */
@property (readonly) CalendrumCoreProblemCode *CharsetHintContradictsData __attribute__((swift_name("CharsetHintContradictsData")));

/**
 * `ICAL-L006` (Error): a component inside a parent that does not allow it, or that the iTIP method forbids.
 */
@property (readonly) CalendrumCoreProblemCode *ComponentNotAllowed __attribute__((swift_name("ComponentNotAllowed")));

/** `ICAL-S020` (Warning): `BEGIN` or `END` has parameters. They are ignored. */
@property (readonly) CalendrumCoreProblemCode *ComponentParameters __attribute__((swift_name("ComponentParameters")));

/**
 * `ICAL-S005` (Warning): the input starts with a continuation line, a space or a tab in the first byte. That
 * one character is removed and the line starts the first logical line.
 */
@property (readonly) CalendrumCoreProblemCode *ContinuationAtStart __attribute__((swift_name("ContinuationAtStart")));

/**
 * `ICAL-L026` (Error): a content line holds a control character other than horizontal tab, in its name, a
 * parameter or its value, which [RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1) forbids.
 * Parsing keeps the line as it is and byte-stable writing writes it back, but canonical writing, jCal and the
 * builders refuse it. Found by the validator and the linter; the explanation never quotes the line.
 */
@property (readonly) CalendrumCoreProblemCode *ControlCharacter __attribute__((swift_name("ControlCharacter")));

/**
 * `ICAL-V017` (Warning): a recurrence rule has both `COUNT` and `UNTIL`, which the standard forbids. Both are
 * kept; the position is that of the second.
 */
@property (readonly) CalendrumCoreProblemCode *CountAndUntil __attribute__((swift_name("CountAndUntil")));

/**
 * `ICAL-V008` (Warning): a `DATE-TIME` value has no time, as in `DTSTART:20260901` without `VALUE=DATE`. It is
 * read as a `DATE`.
 */
@property (readonly) CalendrumCoreProblemCode *DateAsDateTime __attribute__((swift_name("DateAsDateTime")));

/** `ICAL-V007` (Warning): a `DATE` value has a time. The time is dropped. */
@property (readonly) CalendrumCoreProblemCode *DateWithTime __attribute__((swift_name("DateWithTime")));

/** `ICAL-L012` (Error): a `DURATION` with hours, minutes or seconds beside a `DTSTART` of type `DATE`. */
@property (readonly) CalendrumCoreProblemCode *DateWithTimeDuration __attribute__((swift_name("DateWithTimeDuration")));

/**
 * `ICAL-S025` (Warning): the encoding was guessed from the data, by a heuristic or by the placement of zero
 * bytes, and the input is transcoded from there on. The explanation names the encoding and the confidence in
 * percent.
 */
@property (readonly) CalendrumCoreProblemCode *DetectedCharset __attribute__((swift_name("DetectedCharset")));

/**
 * `ICAL-V010` (Warning): a `DURATION` has hours and seconds but no minutes, such as `PT1H30S`. The missing `0M`
 * is added.
 */
@property (readonly) CalendrumCoreProblemCode *DurationSkipsMinutes __attribute__((swift_name("DurationSkipsMinutes")));

/**
 * `ICAL-V009` (Warning): a `DURATION` has time units without the `T` designator, such as `P90M`. It is read as
 * `PT90M`, since the standard has no months in durations.
 */
@property (readonly) CalendrumCoreProblemCode *DurationWithoutTimeDesignator __attribute__((swift_name("DurationWithoutTimeDesignator")));

/**
 * `ICAL-V016` (Warning): an empty list item, as in `a,,b`, or an empty rule part, as in `FREQ=DAILY;`. It is
 * skipped.
 */
@property (readonly) CalendrumCoreProblemCode *EmptyItem __attribute__((swift_name("EmptyItem")));

/**
 * `ICAL-L025` (Warning): an empty value of a non-text property, such as `DTEND:` or `RRULE:`. Found by the
 * linter.
 */
@property (readonly) CalendrumCoreProblemCode *EmptyValue __attribute__((swift_name("EmptyValue")));

/**
 * `ICAL-L011` (Error): `DTEND` or `DUE` is not later than `DTSTART`. Not checked when a zone does not resolve.
 */
@property (readonly) CalendrumCoreProblemCode *EndBeforeStart __attribute__((swift_name("EndBeforeStart")));

/**
 * `ICAL-S016` (Warning): an `END` closes a component that is open deeper in the stack than the top. The
 * components in between are closed implicitly, then the matching one. One problem per `END` line.
 */
@property (readonly) CalendrumCoreProblemCode *EndClosesNested __attribute__((swift_name("EndClosesNested")));

/** `ICAL-S015` (Warning): an `END` line with no open component. The line is ignored. */
@property (readonly) CalendrumCoreProblemCode *EndWithoutBegin __attribute__((swift_name("EndWithoutBegin")));

/** `ICAL-L008` (Error): properties that exclude each other occur together, such as `DTEND` and `DURATION`. */
@property (readonly) CalendrumCoreProblemCode *ExclusiveProperties __attribute__((swift_name("ExclusiveProperties")));

/**
 * `ICAL-V005` (Warning): a date or time is written in the extended ISO 8601 form, with separators. It is read
 * as the basic form; in strict mode it is an error.
 */
@property (readonly) CalendrumCoreProblemCode *ExtendedIsoForm __attribute__((swift_name("ExtendedIsoForm")));

/**
 * `ICAL-O004` (Warning): a floating time, a `DATE` or an unresolved `TZID` was met while no floating zone is
 * set. The occurrence is skipped.
 */
@property (readonly) CalendrumCoreProblemCode *FloatingWithoutZone __attribute__((swift_name("FloatingWithoutZone")));

/**
 * `ICAL-R009` (Info): an `RDATE` or `EXDATE` is in another form than `DTSTART`, such as UTC against a time
 * zone. They are compared by local time.
 */
@property (readonly) CalendrumCoreProblemCode *FormMismatch __attribute__((swift_name("FormMismatch")));

/**
 * `ICAL-V006` (Warning): a time has fractions of a second, as written by JavaScript generators. The fraction is
 * dropped.
 */
@property (readonly) CalendrumCoreProblemCode *FractionalSeconds __attribute__((swift_name("FractionalSeconds")));

/**
 * `ICAL-S030` (Error): the input is longer than [io.github.calendrum.syntax.ParseLimits.maxInputOctets]. Reading
 * stops before the first logical line that does not end within that many bytes, and the result is truncated.
 * Always reported.
 */
@property (readonly) CalendrumCoreProblemCode *InputTooLarge __attribute__((swift_name("InputTooLarge")));

/**
 * `ICAL-S019` (Warning): the name in `BEGIN` or `END` is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is, and the component is opened and closed by it, ignoring ASCII
 * case.
 */
@property (readonly) CalendrumCoreProblemCode *InvalidComponentName __attribute__((swift_name("InvalidComponentName")));

/** `ICAL-J001` (Error): the input is not JSON. Nothing is read. */
@property (readonly) CalendrumCoreProblemCode *InvalidJson __attribute__((swift_name("InvalidJson")));

/**
 * `ICAL-S008` (Warning): a property or parameter name is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is.
 */
@property (readonly) CalendrumCoreProblemCode *InvalidName __attribute__((swift_name("InvalidName")));

/**
 * `ICAL-S006` (Warning): a logical line contains an invalid UTF-8 sequence. Each maximal invalid subsequence is
 * replaced with U+FFFD; the line is not skipped. One problem per line.
 */
@property (readonly) CalendrumCoreProblemCode *InvalidUtf8 __attribute__((swift_name("InvalidUtf8")));

/**
 * `ICAL-Z003` (Warning): a `VTIMEZONE` has no `TZID`, no observance with `DTSTART` and `TZOFFSETTO`, or more than
 * 200,000 transitions up to its last start (a hostile zone of endless observances). It is skipped, and the next
 * resolver of the chain looks for the `TZID`.
 */
@property (readonly) CalendrumCoreProblemCode *InvalidVTimeZone __attribute__((swift_name("InvalidVTimeZone")));

/**
 * `ICAL-V001` (Error): a value does not conform to the grammar of its type; no value is decoded. In strict mode
 * it follows the warning code of a lenient repair, with the same position.
 */
@property (readonly) CalendrumCoreProblemCode *InvalidValue __attribute__((swift_name("InvalidValue")));

/**
 * `ICAL-Z005` (Warning): the current rules of the zone cannot be written as one `RRULE` per observance, so the
 * generated `VTIMEZONE` only approximates them. The full mode gives an exact one.
 */
@property (readonly) CalendrumCoreProblemCode *IrregularTimeZone __attribute__((swift_name("IrregularTimeZone")));

/**
 * `ICAL-J007` (Error): jCal components are nested deeper than `JCalOptions.maxDepth` (16 by default). The
 * deeper component is skipped with everything inside it.
 */
@property (readonly) CalendrumCoreProblemCode *JCalTooDeep __attribute__((swift_name("JCalTooDeep")));

/**
 * `ICAL-J005` (Warning): a value is not in the form of its type, such as a `date-time` that is not
 * `YYYY-MM-DDTHH:MM:SS`. The value is written as it is.
 */
@property (readonly) CalendrumCoreProblemCode *JCalValueMismatch __attribute__((swift_name("JCalValueMismatch")));

/**
 * `ICAL-V004` (Warning): a time has the second 60, which the standard allows. The decoder replaces it by 59 or
 * by the next minute, depending on its policy.
 */
@property (readonly) CalendrumCoreProblemCode *LeapSecond __attribute__((swift_name("LeapSecond")));

/** `ICAL-R008` (Info): `BYSECOND=60` has no meaning in local time. The value is ignored. */
@property (readonly) CalendrumCoreProblemCode *LeapSecondIgnored __attribute__((swift_name("LeapSecondIgnored")));

/**
 * `ICAL-S010` (Error): a logical line is longer than [io.github.calendrum.syntax.ParseLimits.maxLineOctets]. The
 * whole line is skipped and the bytes beyond the limit are not copied.
 */
@property (readonly) CalendrumCoreProblemCode *LineTooLong __attribute__((swift_name("LineTooLong")));

/**
 * `ICAL-S028` (Warning): the heuristic is less than 90% sure of the encoding, typically for a short text of
 * capital abbreviations where windows-1251 and KOI8-R look alike. The input is read in the chosen encoding; the
 * application may want to ask the user or pass a hint.
 */
@property (readonly) CalendrumCoreProblemCode *LowCharsetConfidence __attribute__((swift_name("LowCharsetConfidence")));

/**
 * `ICAL-J002` (Error): an element is not a jCal component: not an array of a name, an array of properties and
 * an array of components, or the name is not valid. The component is skipped with its children.
 */
@property (readonly) CalendrumCoreProblemCode *MalformedJCalComponent __attribute__((swift_name("MalformedJCalComponent")));

/**
 * `ICAL-J004` (Warning): a parameter value is not a string or an array of strings, or the `value` parameter
 * appears in the parameter object. The parameter is skipped.
 */
@property (readonly) CalendrumCoreProblemCode *MalformedJCalParameter __attribute__((swift_name("MalformedJCalParameter")));

/**
 * `ICAL-J003` (Error): a property is not an array of a name, an object of parameters, a type and at least one
 * value, or a value has the wrong JSON type. The property is skipped.
 */
@property (readonly) CalendrumCoreProblemCode *MalformedJCalProperty __attribute__((swift_name("MalformedJCalProperty")));

/**
 * `ICAL-S012` (Warning): a parameter is malformed: it has no `=`, a quote inside an unquoted value, or text
 * after a closing quote. A parameter without `=` gets no values; otherwise the value is taken literally up to
 * the next delimiter.
 */
@property (readonly) CalendrumCoreProblemCode *MalformedParameter __attribute__((swift_name("MalformedParameter")));

/**
 * `ICAL-L016` (Error): a value does not decode as its type; the details are an `ICAL-V` problem from reading.
 */
@property (readonly) CalendrumCoreProblemCode *MalformedValue __attribute__((swift_name("MalformedValue")));

/**
 * `ICAL-L018` (Error): the calendar has no `METHOD`, or one that differs from the method of the iTIP rule set
 * in use.
 */
@property (readonly) CalendrumCoreProblemCode *MethodMismatch __attribute__((swift_name("MethodMismatch")));

/**
 * `ICAL-L020` (Error): the iTIP method is not defined for the component, such as `REFRESH` for a `VJOURNAL`.
 */
@property (readonly) CalendrumCoreProblemCode *MethodNotDefined __attribute__((swift_name("MethodNotDefined")));

/**
 * `ICAL-S007` (Error): there is no colon outside quotes after the property name and its parameters. The line is
 * skipped.
 */
@property (readonly) CalendrumCoreProblemCode *MissingColon __attribute__((swift_name("MissingColon")));

/**
 * `ICAL-L007` (Error): a required nested component is missing, such as a calendar without components or a
 * `VTIMEZONE` without `STANDARD` or `DAYLIGHT`.
 */
@property (readonly) CalendrumCoreProblemCode *MissingComponent __attribute__((swift_name("MissingComponent")));

/**
 * `ICAL-L009` (Error): a property lacks the one it requires, such as `RRULE` without `DTSTART` or `REPEAT`
 * without `DURATION`.
 */
@property (readonly) CalendrumCoreProblemCode *MissingDependency __attribute__((swift_name("MissingDependency")));

/** `ICAL-L001` (Error): a required property is missing. */
@property (readonly) CalendrumCoreProblemCode *MissingProperty __attribute__((swift_name("MissingProperty")));

/**
 * `ICAL-M001` (Error): a component of a schema lacks a required property, or the first such property is broken.
 * The component is skipped. The explanation names the property, never its value.
 */
@property (readonly) CalendrumCoreProblemCode *MissingRequiredField __attribute__((swift_name("MissingRequiredField")));

/**
 * `ICAL-L017` (Error): a `TZID` has no `VTIMEZONE` with that identifier in the calendar ([RFC 5545
 * §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)). Reported once per `TZID`.
 */
@property (readonly) CalendrumCoreProblemCode *MissingTimeZone __attribute__((swift_name("MissingTimeZone")));

/**
 * `ICAL-V012` (Warning): the offset `-0000`, which [RFC 5545
 * §3.3.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.14) forbids. It is read as `+0000`.
 */
@property (readonly) CalendrumCoreProblemCode *NegativeZeroOffset __attribute__((swift_name("NegativeZeroOffset")));

/**
 * `ICAL-L015` (Error): the `DTSTART` of a `VTIMEZONE` observance is not a local time ([RFC 5545
 * §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 */
@property (readonly) CalendrumCoreProblemCode *NotLocalTime __attribute__((swift_name("NotLocalTime")));

/**
 * `ICAL-V027` (Warning): a value that must be in UTC is not, as in `DTSTAMP`, `CREATED`, `LAST-MODIFIED` and an
 * absolute `TRIGGER`. It is kept as it is.
 */
@property (readonly) CalendrumCoreProblemCode *NotUtc __attribute__((swift_name("NotUtc")));

/**
 * `ICAL-O005` (Warning): a component has no `DTSTART` (and a to-do no `DUE` either), so it has no occurrence.
 */
@property (readonly) CalendrumCoreProblemCode *OccurrenceWithoutStart __attribute__((swift_name("OccurrenceWithoutStart")));

/**
 * `ICAL-V002` (Error): a field is out of range, such as a month, a day, an hour, a UTC offset, an integer
 * beyond 32 bits, or a `BYxxx` part of a recurrence rule. No value is decoded.
 */
@property (readonly) CalendrumCoreProblemCode *OutOfRange __attribute__((swift_name("OutOfRange")));

/**
 * `ICAL-O001` (Warning): the `RECURRENCE-ID` of an override matches no instance of the master. The override is
 * kept as a single occurrence.
 */
@property (readonly) CalendrumCoreProblemCode *OverrideWithoutInstance __attribute__((swift_name("OverrideWithoutInstance")));

/**
 * `ICAL-V028` (Warning): a parameter does not apply to the value, such as `TZID` on a `DATE` or `RELATED` on an
 * absolute `TRIGGER`. It is not applied.
 */
@property (readonly) CalendrumCoreProblemCode *ParameterNotAllowed __attribute__((swift_name("ParameterNotAllowed")));

/**
 * `ICAL-V022` (Warning): a `PERIOD` ends no later than it starts, or has a duration that is not positive. It is
 * kept.
 */
@property (readonly) CalendrumCoreProblemCode *PeriodNotPositive __attribute__((swift_name("PeriodNotPositive")));

/**
 * `ICAL-L004` (Error): a known property is in a component where it does not belong, or that the iTIP method
 * forbids.
 */
@property (readonly) CalendrumCoreProblemCode *PropertyNotAllowed __attribute__((swift_name("PropertyNotAllowed")));

/**
 * `ICAL-S021` (Warning): a property outside any component, before the first `BEGIN` or between calendars. The
 * property is not part of the tree, but a cursor reports it at depth 0.
 */
@property (readonly) CalendrumCoreProblemCode *PropertyOutsideComponent __attribute__((swift_name("PropertyOutsideComponent")));

/**
 * `ICAL-V003` (Error): a recurrence rule has no `FREQ` part ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)). No value is decoded.
 */
@property (readonly) CalendrumCoreProblemCode *RecurWithoutFreq __attribute__((swift_name("RecurWithoutFreq")));

/**
 * `ICAL-R001` (Warning): the number of occurrences reached the limit while the sequence was not over, or a rule
 * with `COUNT` passed as many dates before the start of a range. The sequence ends.
 */
@property (readonly) CalendrumCoreProblemCode *RecurrenceLimitReached __attribute__((swift_name("RecurrenceLimitReached")));

/**
 * `ICAL-R002` (Warning): too many periods in a row produced no occurrence, as in
 * `FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30`. The sequence ends.
 */
@property (readonly) CalendrumCoreProblemCode *RecurrenceNoMatch __attribute__((swift_name("RecurrenceNoMatch")));

/**
 * `ICAL-L003` (Warning): a property occurs more than once where the standard says it SHOULD NOT, such as
 * `RRULE`.
 */
@property (readonly) CalendrumCoreProblemCode *RepeatedProperty __attribute__((swift_name("RepeatedProperty")));

/** `ICAL-V018` (Warning): a part of a recurrence rule is repeated. The last one is kept. */
@property (readonly) CalendrumCoreProblemCode *RepeatedRulePart __attribute__((swift_name("RepeatedRulePart")));

/** `ICAL-R004` (Warning): the `RSCALE` of the rule is not `GREGORIAN`. The rule is not expanded. */
@property (readonly) CalendrumCoreProblemCode *RscaleNotSupported __attribute__((swift_name("RscaleNotSupported")));

/**
 * `ICAL-V019` (Warning): a rule part is not allowed in this rule, such as `BYWEEKNO` without `YEARLY`, or
 * `BYSETPOS` without another `BYxxx` part. The part is kept.
 */
@property (readonly) CalendrumCoreProblemCode *RulePartNotAllowed __attribute__((swift_name("RulePartNotAllowed")));

/**
 * `ICAL-R003` (Warning): `DTSTART` is not an instance of its own rule, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined. What happens
 * is up to the `startMismatch` option of the recurrence options.
 */
@property (readonly) CalendrumCoreProblemCode *StartNotInRule __attribute__((swift_name("StartNotInRule")));

/**
 * `ICAL-O002` (Info): an override with `RANGE=THISANDFUTURE` was applied to the later instances, on a
 * best-effort basis.
 */
@property (readonly) CalendrumCoreProblemCode *ThisAndFutureApplied __attribute__((swift_name("ThisAndFutureApplied")));

/**
 * `ICAL-S029` (Error): the time budget [io.github.calendrum.syntax.ParseLimits.maxTime] ran out. Reading stops at a
 * line boundary and the result is truncated. Always reported.
 */
@property (readonly) CalendrumCoreProblemCode *TimeBudgetExceeded __attribute__((swift_name("TimeBudgetExceeded")));

/**
 * `ICAL-R007` (Warning): the rule has a time part or a frequency shorter than a day while `DTSTART` is a
 * `DATE`. The time parts are ignored.
 */
@property (readonly) CalendrumCoreProblemCode *TimePartWithDate __attribute__((swift_name("TimePartWithDate")));

/**
 * `ICAL-Z002` (Info): the `TZID` was found through an alias, such as an IANA link, a Windows name or a
 * `/mozilla.org/` prefix.
 */
@property (readonly) CalendrumCoreProblemCode *TimeZoneAlias __attribute__((swift_name("TimeZoneAlias")));

/**
 * `ICAL-Z004` (Warning): the `TZID` was found only by a guess from a city in its name. The zone of the first
 * city mentioned is used.
 */
@property (readonly) CalendrumCoreProblemCode *TimeZoneGuessed __attribute__((swift_name("TimeZoneGuessed")));

/**
 * `ICAL-S022` (Error): components are nested deeper than [io.github.calendrum.syntax.ParseLimits.maxDepth]. The
 * subtree is skipped up to its `END`, with one problem per subtree.
 */
@property (readonly) CalendrumCoreProblemCode *TooDeep __attribute__((swift_name("TooDeep")));

/**
 * `ICAL-S023` (Error): the limit on components was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxComponents]). Open components are closed implicitly, reading stops
 * and the result is truncated. Always reported.
 */
@property (readonly) CalendrumCoreProblemCode *TooManyComponents __attribute__((swift_name("TooManyComponents")));

/**
 * `ICAL-S014` (Error): the limit on logical lines was reached ([io.github.calendrum.syntax.ParseLimits.maxLines]).
 * Reading stops and the result is truncated. Always reported, even beyond the problem limit.
 */
@property (readonly) CalendrumCoreProblemCode *TooManyLines __attribute__((swift_name("TooManyLines")));

/**
 * `ICAL-S013` (Warning): the limit on reported problems was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxProblems]). Further problems are only counted; see
 * [io.github.calendrum.syntax.ParseResult.problemCount].
 */
@property (readonly) CalendrumCoreProblemCode *TooManyProblems __attribute__((swift_name("TooManyProblems")));

/** `ICAL-L002` (Error): a property occurs more often than allowed. */
@property (readonly) CalendrumCoreProblemCode *TooManyProperties __attribute__((swift_name("TooManyProperties")));

/**
 * `ICAL-S024` (Info): the input was transcoded to UTF-8 from the encoding named by a byte order mark or by a
 * charset hint. The explanation names the encoding and where it came from. Positions refer to the decoded text.
 */
@property (readonly) CalendrumCoreProblemCode *TranscodedInput __attribute__((swift_name("TranscodedInput")));

/**
 * `ICAL-V024` (Warning): a `TZID` parameter on a UTC `DATE-TIME`, which the standard forbids. The parameter is
 * not applied and the time is UTC.
 */
@property (readonly) CalendrumCoreProblemCode *TzidWithUtc __attribute__((swift_name("TzidWithUtc")));

/**
 * `ICAL-S018` (Warning): the input ended while components were open, as in a truncated feed. All of them are
 * closed implicitly. Not reported when reading was stopped by a limit.
 */
@property (readonly) CalendrumCoreProblemCode *UnclosedComponents __attribute__((swift_name("UnclosedComponents")));

/**
 * `ICAL-S009` (Warning): a quoted parameter value is not closed before the end of the line. The value is taken
 * literally up to the next comma, semicolon or colon, and parsing goes on.
 */
@property (readonly) CalendrumCoreProblemCode *UnclosedQuote __attribute__((swift_name("UnclosedQuote")));

/**
 * `ICAL-L019` (Info): the `ACTION` of an alarm is not `AUDIO`, `DISPLAY` or `EMAIL`; such an alarm is to be
 * ignored.
 */
@property (readonly) CalendrumCoreProblemCode *UnknownAlarmAction __attribute__((swift_name("UnknownAlarmAction")));

/**
 * `ICAL-S026` (Warning): the charset hint is not recognized, either an unknown name or an unsupported encoding.
 * The hint is ignored and the data decides.
 */
@property (readonly) CalendrumCoreProblemCode *UnknownCharsetHint __attribute__((swift_name("UnknownCharsetHint")));

/**
 * `ICAL-V023` (Warning): a backslash in a `TEXT` value is followed by a character that is not an escape of [RFC
 * 5545 §3.3.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.11), or ends the value. Both characters are
 * kept.
 */
@property (readonly) CalendrumCoreProblemCode *UnknownEscape __attribute__((swift_name("UnknownEscape")));

/**
 * `ICAL-J006` (Warning): the type of a property is not a type of RFC 5545 or RFC 7265. No `VALUE` parameter is
 * written and the value is kept as it is.
 */
@property (readonly) CalendrumCoreProblemCode *UnknownJCalType __attribute__((swift_name("UnknownJCalType")));

/**
 * `ICAL-L005` (Info): a property known neither to the registry nor to the rule tables, and not an `X-`
 * property.
 */
@property (readonly) CalendrumCoreProblemCode *UnknownProperty __attribute__((swift_name("UnknownProperty")));

/**
 * `ICAL-V020` (Warning): an unknown part in a recurrence rule, such as an `X-` name or a future registered
 * part. It is kept, in order, and written after the known ones.
 */
@property (readonly) CalendrumCoreProblemCode *UnknownRulePart __attribute__((swift_name("UnknownRulePart")));

/**
 * `ICAL-Z001` (Warning): no resolver of the chain knows the `TZID`. The unknown-zone policy decides: floating
 * time, UTC or an exception.
 */
@property (readonly) CalendrumCoreProblemCode *UnknownTimeZone __attribute__((swift_name("UnknownTimeZone")));

/** `ICAL-S017` (Warning): an `END` names a component that is not open. The line is ignored. */
@property (readonly) CalendrumCoreProblemCode *UnmatchedEnd __attribute__((swift_name("UnmatchedEnd")));

/**
 * `ICAL-S011` (Error): the input is in an encoding the library does not read at all, such as UTF-7, UTF-EBCDIC,
 * SCSU, BOCU-1 or GB18030, recognized by its signature. Nothing is parsed. The explanation names the encoding.
 */
@property (readonly) CalendrumCoreProblemCode *UnsupportedEncoding __attribute__((swift_name("UnsupportedEncoding")));

/**
 * `ICAL-R005` (Warning): the type of `UNTIL` does not match `DTSTART`, such as a `DATE` against a `DATE-TIME`
 * or UTC against local. The two are compared in the closest sensible way.
 */
@property (readonly) CalendrumCoreProblemCode *UntilTypeMismatch __attribute__((swift_name("UntilTypeMismatch")));

/**
 * `ICAL-R006` (Info): `UNTIL` is in UTC while `DTSTART` has a time zone and no `untilOffset` is set, so `UNTIL`
 * is compared with local time.
 */
@property (readonly) CalendrumCoreProblemCode *UntilWithoutZone __attribute__((swift_name("UntilWithoutZone")));

/**
 * `ICAL-L024` (Info): an `ORGANIZER` or `ATTENDEE` with the scheme in upper case, `MAILTO:`. Found by the
 * linter.
 */
@property (readonly) CalendrumCoreProblemCode *UppercaseMailto __attribute__((swift_name("UppercaseMailto")));

/**
 * `ICAL-V021` (Warning): a `URI` or `CAL-ADDRESS` without a scheme, such as an address without `mailto:`. It is
 * kept as it is.
 */
@property (readonly) CalendrumCoreProblemCode *UriWithoutScheme __attribute__((swift_name("UriWithoutScheme")));

/** `ICAL-V011` (Warning): a `UTC-OFFSET` is written with a colon, such as `+03:00`. It is read without it. */
@property (readonly) CalendrumCoreProblemCode *UtcOffsetWithColon __attribute__((swift_name("UtcOffsetWithColon")));

/** `ICAL-S001` (Info): the input starts with a UTF-8 byte order mark. The three bytes are skipped. */
@property (readonly) CalendrumCoreProblemCode *Utf8Bom __attribute__((swift_name("Utf8Bom")));

/**
 * `ICAL-L013` (Error): a value outside the list of the standard, such as a component `STATUS`, `VERSION` or
 * `TRANSP`.
 */
@property (readonly) CalendrumCoreProblemCode *ValueNotAllowed __attribute__((swift_name("ValueNotAllowed")));

/**
 * `ICAL-L014` (Error): a number outside its range, such as `PRIORITY` outside 0 to 9 or `PERCENT-COMPLETE`
 * outside 0 to 100.
 */
@property (readonly) CalendrumCoreProblemCode *ValueOutOfRange __attribute__((swift_name("ValueOutOfRange")));

/**
 * `ICAL-L010` (Error): the form of a value differs from that of a related one, such as a `DATE` `DTEND` with a
 * `DATE-TIME` `DTSTART`, or a non-UTC `UNTIL` with a zoned `DTSTART`.
 */
@property (readonly) CalendrumCoreProblemCode *ValueTypeMismatch __attribute__((swift_name("ValueTypeMismatch")));

/**
 * `ICAL-V025` (Warning): the `VALUE` parameter names a type this property does not take, or is empty. The
 * default type of the property is read.
 */
@property (readonly) CalendrumCoreProblemCode *ValueTypeNotAllowed __attribute__((swift_name("ValueTypeNotAllowed")));

/**
 * `ICAL-V026` (Warning): a value of a type other than the default is not declared by `VALUE` or `ENCODING`, as
 * in `TRIGGER:19980101T050000Z`. It is read as the value of that type.
 */
@property (readonly) CalendrumCoreProblemCode *ValueTypeNotDeclared __attribute__((swift_name("ValueTypeNotDeclared")));

/** `ICAL-L023` (Info): `X-WR-CALNAME` without the standard `NAME` of RFC 7986. Found by the linter. */
@property (readonly) CalendrumCoreProblemCode *VendorCalendarName __attribute__((swift_name("VendorCalendarName")));

/**
 * `ICAL-L021` (Warning): `X-WR-TIMEZONE` beside floating times, which clients understand in different ways.
 * Found by the linter.
 */
@property (readonly) CalendrumCoreProblemCode *VendorTimeZone __attribute__((swift_name("VendorTimeZone")));

/**
 * `ICAL-V013` (Warning): spaces or tabs around a value or a list item. They are dropped. Not reported for
 * `TEXT` and `BINARY`.
 */
@property (readonly) CalendrumCoreProblemCode *Whitespace __attribute__((swift_name("Whitespace")));
@end


/**
 * Receives problems as a reader or an expander finds them.
 *
 * The listener is called on the thread that does the work, in the order the problems are found. It must not throw: an
 * exception thrown by a listener propagates out of the call that was running.
 */
__attribute__((swift_name("ProblemListener")))
@protocol CalendrumCoreProblemListener
@required

/** Called once for every reported problem. */
- (void)onProblemProblem:(CalendrumCoreProblem *)problem __attribute__((swift_name("onProblem(problem:)")));
@end

__attribute__((swift_name("KotlinComparable")))
@protocol CalendrumCoreKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface CalendrumCoreKotlinEnum<E> : CalendrumCoreBase <CalendrumCoreKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end


/** How serious a [Problem] is. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Severity")))
@interface CalendrumCoreSeverity : CalendrumCoreKotlinEnum<CalendrumCoreSeverity *>
+ (instancetype)alloc __attribute__((unavailable));

/** How serious a [Problem] is. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreSeverity *info __attribute__((swift_name("info")));
@property (class, readonly) CalendrumCoreSeverity *warning __attribute__((swift_name("warning")));
@property (class, readonly) CalendrumCoreSeverity *error __attribute__((swift_name("error")));
+ (CalendrumCoreKotlinArray<CalendrumCoreSeverity *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreSeverity *> *entries __attribute__((swift_name("entries")));
@end


/**
 * A position in the input: byte offsets from its start, a line and a column.
 *
 * Offsets count bytes of the input, from 0; for input that was transcoded they count bytes of the decoded UTF-8 text.
 * The line and the column both start at 1, and the column is counted in bytes from the start of the physical line.
 * Nodes built by hand have no input: their span is all zeros, and a value of a hand-built property has a column of 0.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SourceSpan")))
@interface CalendrumCoreSourceSpan : CalendrumCoreBase

/** Returns `true` when [other] is a span with the same offsets, line and column. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the line, the column and the offsets, such as `3:7 (offsets 12..12)`. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The 1-based column in bytes on that line, or 0 when there is no input. */
@property (readonly) int32_t column __attribute__((swift_name("column")));

/** The offset just after the last byte of the span; equal to [startOffset] for a point. */
@property (readonly) int64_t endOffset __attribute__((swift_name("endOffset")));

/** The 1-based number of the physical line where the span starts, or 0 when there is no input. */
@property (readonly) int32_t line __attribute__((swift_name("line")));

/** The offset of the first byte of the span. */
@property (readonly) int64_t startOffset __attribute__((swift_name("startOffset")));
@end


/**
 * A repair for a known quirk of a calendar producer, applied after reading (to a [VCalendar]) or while reading (as line
 * interceptors).
 *
 * The repairs are opt-in and never run on their own: pick one from [Fixups] or build your own. A fix does not change
 * the calendar it is given; it returns a new one and lists what it changed.
 *
 * @note annotations
 *   kotlin.SubclassOptInRequired(markerClass=[NormalClass(value=io/github/calendrum/ExperimentalICalApi)])
*/
__attribute__((swift_name("Fixup")))
@protocol CalendrumCoreFixup
@required

/**
 * Applies the fix to [calendar] and returns the repaired calendar with a list of the changes. When nothing needed
 * repair the same calendar is returned and [FixupResult.isChanged] is false.
 */
- (CalendrumCoreFixupResult *)applyCalendar:(CalendrumCoreVCalendar *)calendar __attribute__((swift_name("apply(calendar:)")));

/**
 * Returns factories of line interceptors that make the same repair while the feed is read, to be passed to
 * `ParseOptions.Builder.intercept`. The list is empty for a fix that needs the whole tree.
 */
- (NSArray<id<CalendrumCoreLineInterceptor> (^)(void)> *)interceptors __attribute__((swift_name("interceptors()")));

/** A short stable name of the fix, such as `windows-tzids`. */
@property (readonly) NSString *id __attribute__((swift_name("id")));
@end


/**
 * One change made by a fix.
 *
 * @property fixup the [Fixup.id] of the fix that made it.
 * @property path the element that was changed or added.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FixupChange")))
@interface CalendrumCoreFixupChange : CalendrumCoreBase

/** Returns the fix id and the path. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *fixup __attribute__((swift_name("fixup")));
@property (readonly) CalendrumCoreElementPath *path __attribute__((swift_name("path")));
@end


/**
 * The result of applying a [Fixup].
 *
 * @property calendar the repaired calendar.
 * @property changes what was changed, in the order it was done; never the values.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FixupResult")))
@interface CalendrumCoreFixupResult : CalendrumCoreBase

/** Returns the number of changes. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreVCalendar *calendar __attribute__((swift_name("calendar")));
@property (readonly) NSArray<CalendrumCoreFixupChange *> *changes __attribute__((swift_name("changes")));

/** Whether the fix changed anything. */
@property (readonly) BOOL isChanged __attribute__((swift_name("isChanged")));
@end


/**
 * The built-in fixes for calendars from well-known producers, and a way to combine them.
 *
 * Each fix repairs one thing real feeds get wrong and reports what it changed. They are also the repairs behind the
 * lint codes `ICAL-L021` to `ICAL-L025`.
 *
 * @sample io.github.calendrum.samples.fixVendorCalendar
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Fixups")))
@interface CalendrumCoreFixups : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The built-in fixes for calendars from well-known producers, and a way to combine them.
 *
 * Each fix repairs one thing real feeds get wrong and reports what it changed. They are also the repairs behind the
 * lint codes `ICAL-L021` to `ICAL-L025`.
 *
 * @sample io.github.calendrum.samples.fixVendorCalendar
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)fixups __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreFixups *shared __attribute__((swift_name("shared")));

/**
 * Returns a fix that picks the right built-in repair from the `PRODID` (and `VERSION:1.0`) of the calendar:
 * Outlook, Lotus Notes or Google. A vCalendar 1.0 file gets the vCalendar 1.0 repair and then that of its producer,
 * so that applying the fix again changes nothing. A calendar from another producer is left as it is.
 */
- (id<CalendrumCoreFixup>)byProdId __attribute__((swift_name("byProdId()")));

/** Combines [fixups] into one that applies them in order. Its changes are those of all of them. */
- (id<CalendrumCoreFixup>)composeFixups:(CalendrumCoreKotlinArray<id<CalendrumCoreFixup>> *)fixups __attribute__((swift_name("compose(fixups:)")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Repairs for Google Calendar feeds: `X-WR-TIMEZONE` ([xWrTimezone]), `X-WR-CALNAME` ([xWrCalendarName]), a `UNTIL`
 * with a time on a rule of an all-day event, and a `CONFERENCE` made from `X-GOOGLE-CONFERENCE`.
 */
@property (readonly) id<CalendrumCoreFixup> google __attribute__((swift_name("google")));

/**
 * Repairs for Lotus Notes and Domino feeds: Windows time zone names and empty values of non-text properties such as
 * `DTEND:`.
 */
@property (readonly) id<CalendrumCoreFixup> lotusNotes __attribute__((swift_name("lotusNotes")));

/**
 * Repairs for Microsoft Outlook and Exchange feeds: Windows time zone names ([windowsTzids]), all-day events
 * written as midnight date-times (`X-MICROSOFT-CDO-ALLDAYEVENT`) and upper-case `MAILTO:`.
 */
@property (readonly) id<CalendrumCoreFixup> outlook __attribute__((swift_name("outlook")));

/**
 * Repairs for vCalendar 1.0 (`VERSION:1.0`) files: `DCREATED` as `CREATED`, quoted-printable text decoded,
 * vCard-style attendee addresses and `STATUS`, `RSVP`, `EXPECT` and `ROLE` parameters mapped to those of iCalendar.
 */
@property (readonly) id<CalendrumCoreFixup> vcalendar10 __attribute__((swift_name("vcalendar10")));

/**
 * Renames `TZID`s that are Windows or Outlook names, IANA links or `/mozilla.org/` prefixes to the IANA identifier,
 * in `VTIMEZONE` components and in parameters, and merges `VTIMEZONE` duplicates that result. Guessed names are
 * left alone.
 */
@property (readonly) id<CalendrumCoreFixup> windowsTzids __attribute__((swift_name("windowsTzids")));

/**
 * Adds `NAME` and `DESCRIPTION` of RFC 7986 from `X-WR-CALNAME` and `X-WR-CALDESC` when the standard properties are
 * missing.
 */
@property (readonly) id<CalendrumCoreFixup> xWrCalendarName __attribute__((swift_name("xWrCalendarName")));

/**
 * Gives the floating `DTSTART`, `DTEND`, `DUE`, `RECURRENCE-ID`, `RDATE` and `EXDATE` values of components the
 * `TZID` named by the non-standard `X-WR-TIMEZONE` of the calendar, as Google Calendar and Apple Calendar mean it.
 */
@property (readonly) id<CalendrumCoreFixup> xWrTimezone __attribute__((swift_name("xWrTimezone")));
@end


/**
 * Helpers for iTIP messages carried by e-mail ([RFC 6047](https://www.rfc-editor.org/rfc/rfc6047)): the content type,
 * the check of the sender, and a list of the content a client should not trust.
 *
 * All functions are pure.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IMip")))
@interface CalendrumCoreIMip : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Helpers for iTIP messages carried by e-mail ([RFC 6047](https://www.rfc-editor.org/rfc/rfc6047)): the content type,
 * the check of the sender, and a list of the content a client should not trust.
 *
 * All functions are pure.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)iMip __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreIMip *shared __attribute__((swift_name("shared")));

/**
 * Compares the sender of the mail, [from], with the party the message says it comes from.
 *
 * For messages from the organizer (`PUBLISH`, `REQUEST`, `ADD`, `CANCEL`, `DECLINECOUNTER`) the `ORGANIZER` is
 * compared; for messages from an attendee (`REPLY`, `REFRESH`, `COUNTER`) the `ATTENDEE`s. A `SENT-BY` address
 * counts as [SenderCheck.SentBy]. If a message has several components the worst result wins.
 *
 * @param message the iTIP message.
 * @param from the address in the `From` header, with or without `mailto:`.
 * @sample io.github.calendrum.samples.invitationAndReply
 */
- (CalendrumCoreSenderCheck *)checkSenderMessage:(CalendrumCoreVCalendar *)message from:(NSString *)from __attribute__((swift_name("checkSender(message:from:)")));

/**
 * Returns the `Content-Type` header value for a message with the given [method], such as `text/calendar;
 * method=REQUEST; charset=UTF-8`.
 *
 * @param method the `METHOD` of the message.
 */
- (NSString *)contentTypeMethod:(CalendrumCoreMethod *)method __attribute__((swift_name("contentType(method:)")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Lists the links, markup and inline attachments in [calendar] that a client should show but never fetch or render
 * blindly.
 *
 * @param calendar the message or calendar to scan.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (NSArray<CalendrumCoreUntrustedContent *> *)untrustedContentCalendar:(CalendrumCoreVCalendar *)calendar __attribute__((swift_name("untrustedContent(calendar:)")));
@end


/**
 * Builds and applies iTIP messages ([RFC 5546](https://www.rfc-editor.org/rfc/rfc5546)), the scheduling messages of
 * invitations, replies and cancellations.
 *
 * Everything here is a pure function: the clock is a parameter, `DTSTAMP` and `SEQUENCE` follow RFC 5546, and nothing
 * is added to a calendar, sent or opened on its own. A message is a [VCalendar] with a `METHOD`; transport (iMIP mail,
 * CalDAV scheduling) is up to the caller, see [IMip] for the mail checks. Messages built here keep only the properties
 * the method needs.
 *
 * @sample io.github.calendrum.samples.invitationAndReply
 * @sample io.github.calendrum.samples.reviseAnEvent
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ITip")))
@interface CalendrumCoreITip : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Builds and applies iTIP messages ([RFC 5546](https://www.rfc-editor.org/rfc/rfc5546)), the scheduling messages of
 * invitations, replies and cancellations.
 *
 * Everything here is a pure function: the clock is a parameter, `DTSTAMP` and `SEQUENCE` follow RFC 5546, and nothing
 * is added to a calendar, sent or opened on its own. A message is a [VCalendar] with a `METHOD`; transport (iMIP mail,
 * CalDAV scheduling) is up to the caller, see [IMip] for the mail checks. Messages built here keep only the properties
 * the method needs.
 *
 * @sample io.github.calendrum.samples.invitationAndReply
 * @sample io.github.calendrum.samples.reviseAnEvent
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)iTip __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreITip *shared __attribute__((swift_name("shared")));

/**
 * Builds an `ADD` message ([RFC 5546 §3.2.4](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.4)) that adds
 * [instance] to the recurring [series].
 *
 * @param series the recurring event; its `UID` is used and its `SEQUENCE` raised by one.
 * @param instance the new occurrence, without recurrence properties.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)addSeries:(CalendrumCoreVEvent *)series instance:(CalendrumCoreVEvent *)instance clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("add(series:instance:clock:)")));

/**
 * Builds an `ADD` message ([RFC 5546 §3.4.4](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.4)) that adds
 * [instance] to the recurring [series].
 *
 * @param series the recurring to-do; its `UID` is used and its `SEQUENCE` raised by one.
 * @param instance the new occurrence, without recurrence properties.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)addTodoSeries:(CalendrumCoreVTodo *)series instance:(CalendrumCoreVTodo *)instance clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("addTodo(series:instance:clock:)")));

/**
 * Applies an incoming iTIP message to [store] and returns the result.
 *
 * `PUBLISH` and `REQUEST` add or replace the series (a request keeps the alarms the user set on the stored copy);
 * `CANCEL` marks it cancelled or removes an instance; `ADD` adds an instance; `REPLY` updates the `PARTSTAT` of the
 * replying attendee; a `REPLY` whose `PARTSTAT`, `DELEGATED-TO` or `DELEGATED-FROM` (or, for an attendee the store
 * does not have, any of its parameters or its address) has a control character or a parameter without a value is
 * [ITipOutcome.Ignored], so a store that writes canonically still does after any message. A message older than the
 * stored copy by `SEQUENCE` and `DTSTAMP` is [ITipOutcome.Outdated] and changes nothing. `REFRESH` and `COUNTER`
 * need an answer and change nothing ([ITipOutcome.ResponseRequired]). Time zones of an applied message are added to
 * the store. [store] itself is never modified.
 *
 * @param store the calendar that holds the user's events.
 * @param incoming the message to apply.
 * @sample io.github.calendrum.samples.invitationAndReply
 */
- (CalendrumCoreITipResult *)applyStore:(CalendrumCoreVCalendar *)store incoming:(CalendrumCoreVCalendar *)incoming __attribute__((swift_name("apply(store:incoming:)")));

/**
 * Builds a `CANCEL` message ([RFC 5546 §3.2.5](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.5)) for the whole
 * [event]: `STATUS:CANCELLED` and `SEQUENCE` plus one.
 *
 * @param event the event to cancel.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)cancelEvent:(CalendrumCoreVEvent *)event clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("cancel(event:clock:)")));

/**
 * Builds a `CANCEL` message ([RFC 5546 §3.4.5](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.5)) for the whole
 * [todo].
 *
 * @param todo the to-do to cancel.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)cancelTodo:(CalendrumCoreVTodo *)todo clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("cancel(todo:clock:)")));

/**
 * Builds a `CANCEL` message for one instance of a recurring [event], identified by [recurrenceId].
 *
 * @param event the recurring event.
 * @param recurrenceId the instance to cancel.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)cancelEvent:(CalendrumCoreVEvent *)event recurrenceId:(id<CalendrumCoreICalDateTime>)recurrenceId clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("cancel(event:recurrenceId:clock:)")));

/**
 * Builds a `CANCEL` message for one instance of a recurring [todo], identified by [recurrenceId].
 *
 * @param todo the recurring to-do.
 * @param recurrenceId the instance to cancel.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)cancelTodo:(CalendrumCoreVTodo *)todo recurrenceId:(id<CalendrumCoreICalDateTime>)recurrenceId clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("cancel(todo:recurrenceId:clock:)")));

/**
 * Builds a `COUNTER` message ([RFC 5546 §3.2.7](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.7)) in which
 * [me] proposes [proposal] instead of the event invited to.
 *
 * @param proposal the event with the changes the attendee proposes.
 * @param me the address of the attendee proposing.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)counterProposal:(CalendrumCoreVEvent *)proposal me:(NSString *)me clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("counter(proposal:me:clock:)")));

/**
 * Builds a `COUNTER` message ([RFC 5546 §3.4.7](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.7)) in which
 * [me] proposes [proposal] instead of the to-do assigned.
 *
 * @param proposal the to-do with the changes the attendee proposes.
 * @param me the address of the attendee proposing.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)counterTodoProposal:(CalendrumCoreVTodo *)proposal me:(NSString *)me clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("counterTodo(proposal:me:clock:)")));

/**
 * Builds a `DECLINECOUNTER` message ([RFC 5546 §3.2.8](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.8)) in
 * which the organizer refuses a counter proposal.
 *
 * @param counter the `COUNTER` message being refused.
 * @param clock supplies the `DTSTAMP`.
 * @throws IllegalArgumentException if [counter] has no event or to-do.
 */
- (CalendrumCoreVCalendar *)declineCounterCounter:(CalendrumCoreVCalendar *)counter clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("declineCounter(counter:clock:)")));

/**
 * Builds a `PUBLISH` message ([RFC 5546 §3.2.1](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.1)) from
 * [calendar]: its events, to-dos and journal entries with a new `DTSTAMP`, without `ATTENDEE` and `REQUEST-STATUS`.
 *
 * Time zones used by the components are kept; the others are dropped.
 *
 * @param calendar the calendar to publish.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)publishCalendar:(CalendrumCoreVCalendar *)calendar clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("publish(calendar:clock:)")));

/**
 * Builds a `REFRESH` message ([RFC 5546 §3.2.6](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.6)) in which
 * [me] asks the organizer for the latest version of [event].
 *
 * @param event the event as the attendee has it.
 * @param me the address of the attendee asking.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)refreshEvent:(CalendrumCoreVEvent *)event me:(NSString *)me clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("refresh(event:me:clock:)")));

/**
 * Builds a `REFRESH` message ([RFC 5546 §3.4.6](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.6)) in which
 * [me] asks the organizer for the latest version of [todo].
 *
 * @param todo the to-do as the attendee has it.
 * @param me the address of the attendee asking.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)refreshTodo:(CalendrumCoreVTodo *)todo me:(NSString *)me clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("refresh(todo:me:clock:)")));

/**
 * Builds a `REPLY` message ([RFC 5546 §3.2.3](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.3)) in which [me]
 * answers [request] with [partStat].
 *
 * Each event or to-do of the request is answered with the properties a reply needs and the attendee [me] with the
 * new `PARTSTAT` and without `RSVP`. If [me] is not among the attendees, a new one is added.
 *
 * @param request the invitation being answered.
 * @param me the address of the replying attendee, with or without `mailto:`.
 * @param partStat the answer.
 * @param clock supplies the `DTSTAMP`.
 * @throws IllegalArgumentException if [request] has no event or to-do.
 */
- (CalendrumCoreVCalendar *)replyRequest:(CalendrumCoreVCalendar *)request me:(NSString *)me partStat:(CalendrumCorePartStat *)partStat clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("reply(request:me:partStat:clock:)")));

/**
 * Builds a `REQUEST` message ([RFC 5546 §3.2.2](https://www.rfc-editor.org/rfc/rfc5546#section-3.2.2)) inviting
 * [attendees] to [event].
 *
 * The message carries the event with [organizer] set, the attendees, a `DTSTAMP` from [clock] and the event's
 * `SEQUENCE` (0 if it has none). Use [revise] first when the event was changed, so the `SEQUENCE` is right.
 *
 * @param event the event to send.
 * @param organizer who organizes it.
 * @param attendees who is invited.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)requestEvent:(CalendrumCoreVEvent *)event organizer:(CalendrumCoreOrganizer *)organizer attendees:(NSArray<CalendrumCoreAttendee *> *)attendees clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("request(event:organizer:attendees:clock:)")));

/**
 * Builds a `REQUEST` message ([RFC 5546 §3.4.2](https://www.rfc-editor.org/rfc/rfc5546#section-3.4.2)) assigning
 * [todo] to [attendees].
 *
 * @param todo the to-do to send.
 * @param organizer who organizes it.
 * @param attendees who is asked.
 * @param clock supplies the `DTSTAMP`.
 */
- (CalendrumCoreVCalendar *)requestTodo:(CalendrumCoreVTodo *)todo organizer:(CalendrumCoreOrganizer *)organizer attendees:(NSArray<CalendrumCoreAttendee *> *)attendees clock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("request(todo:organizer:attendees:clock:)")));

/**
 * Returns [current] with the `SEQUENCE` that RFC 5546 requires: [previous] plus one when `DTSTART`, `DTEND`,
 * `DURATION`, `RRULE`, `RDATE`, `EXDATE`, `EXRULE` or `STATUS` changed, and otherwise the old one. A `SEQUENCE`
 * that the caller already raised is kept.
 *
 * @param previous the event as the attendees last saw it.
 * @param current the edited event.
 */
- (CalendrumCoreVEvent *)revisePrevious:(CalendrumCoreVEvent *)previous current:(CalendrumCoreVEvent *)current __attribute__((swift_name("revise(previous:current:)")));

/**
 * Returns [current] with the `SEQUENCE` that RFC 5546 requires: [previous] plus one when `DTSTART`, `DUE`,
 * `DURATION`, `RRULE`, `RDATE`, `EXDATE`, `EXRULE` or `STATUS` changed, and otherwise the old one.
 *
 * @param previous the to-do as the attendees last saw it.
 * @param current the edited to-do.
 */
- (CalendrumCoreVTodo *)reviseTodoPrevious:(CalendrumCoreVTodo *)previous current:(CalendrumCoreVTodo *)current __attribute__((swift_name("reviseTodo(previous:current:)")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** What [ITip.apply] did with an incoming message. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ITipOutcome")))
@interface CalendrumCoreITipOutcome : CalendrumCoreKotlinEnum<CalendrumCoreITipOutcome *>
+ (instancetype)alloc __attribute__((unavailable));

/** What [ITip.apply] did with an incoming message. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreITipOutcome *applied __attribute__((swift_name("applied")));
@property (class, readonly) CalendrumCoreITipOutcome *outdated __attribute__((swift_name("outdated")));
@property (class, readonly) CalendrumCoreITipOutcome *notfound __attribute__((swift_name("notfound")));
@property (class, readonly) CalendrumCoreITipOutcome *responserequired __attribute__((swift_name("responserequired")));
@property (class, readonly) CalendrumCoreITipOutcome *ignored __attribute__((swift_name("ignored")));
+ (CalendrumCoreKotlinArray<CalendrumCoreITipOutcome *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreITipOutcome *> *entries __attribute__((swift_name("entries")));
@end


/**
 * The outcome of applying an iTIP message to a calendar.
 *
 * @property calendar the calendar after applying the message; the same calendar when the outcome is not
 *   [ITipOutcome.Applied].
 * @property outcome what happened.
 * @property method the `METHOD` of the message, or `null` when it had none.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ITipResult")))
@interface CalendrumCoreITipResult : CalendrumCoreBase

/** Returns the outcome and the method. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreVCalendar *calendar __attribute__((swift_name("calendar")));
@property (readonly) CalendrumCoreMethod * _Nullable method __attribute__((swift_name("method")));
@property (readonly) CalendrumCoreITipOutcome *outcome __attribute__((swift_name("outcome")));
@end


/**
 * The result of comparing the mail sender with the party a message claims to come from ([RFC 6047
 * §3](https://www.rfc-editor.org/rfc/rfc6047#section-3)).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SenderCheck")))
@interface CalendrumCoreSenderCheck : CalendrumCoreKotlinEnum<CalendrumCoreSenderCheck *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The result of comparing the mail sender with the party a message claims to come from ([RFC 6047
 * §3](https://www.rfc-editor.org/rfc/rfc6047#section-3)).
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreSenderCheck *matches __attribute__((swift_name("matches")));
@property (class, readonly) CalendrumCoreSenderCheck *sentby __attribute__((swift_name("sentby")));
@property (class, readonly) CalendrumCoreSenderCheck *mismatch __attribute__((swift_name("mismatch")));
@property (class, readonly) CalendrumCoreSenderCheck *unknown __attribute__((swift_name("unknown")));
+ (CalendrumCoreKotlinArray<CalendrumCoreSenderCheck *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreSenderCheck *> *entries __attribute__((swift_name("entries")));
@end


/**
 * One piece of untrusted content found in a calendar by [IMip.untrustedContent].
 *
 * @property path where it is in the calendar.
 * @property kind what sort of content it is.
 * @property property the property that holds it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UntrustedContent")))
@interface CalendrumCoreUntrustedContent : CalendrumCoreBase

/** Returns the path and the kind; never the content. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreUntrustedKind *kind __attribute__((swift_name("kind")));
@property (readonly) CalendrumCoreElementPath *path __attribute__((swift_name("path")));
@property (readonly) CalendrumCoreICalProperty *property __attribute__((swift_name("property")));
@end


/**
 * The kind of content in a calendar that a client must not fetch or render without care.
 *
 * The list may grow in a minor release as new kinds of links or markup appear, so a `when` over it keeps an `else`
 * branch; it is reached through [IMip.untrustedContent], which is not frozen for 1.0.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UntrustedKind")))
@interface CalendrumCoreUntrustedKind : CalendrumCoreKotlinEnum<CalendrumCoreUntrustedKind *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The kind of content in a calendar that a client must not fetch or render without care.
 *
 * The list may grow in a minor release as new kinds of links or markup appear, so a `when` over it keeps an `else`
 * branch; it is reached through [IMip.untrustedContent], which is not frozen for 1.0.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreUntrustedKind *link __attribute__((swift_name("link")));
@property (class, readonly) CalendrumCoreUntrustedKind *html __attribute__((swift_name("html")));
@property (class, readonly) CalendrumCoreUntrustedKind *attachment __attribute__((swift_name("attachment")));
+ (CalendrumCoreKotlinArray<CalendrumCoreUntrustedKind *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreUntrustedKind *> *entries __attribute__((swift_name("entries")));
@end


/**
 * The result of linting a calendar.
 *
 * @property issues all issues, from validation (`ICAL-L001` to `ICAL-L020`) and from the interoperability checks
 *   (`ICAL-L021` to `ICAL-L025`).
 * @property fixable the issues that [Linter.fix] can repair.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LintReport")))
@interface CalendrumCoreLintReport : CalendrumCoreBase

/** Returns the number of issues and of fixable issues. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumCoreValidationIssue *> *fixable __attribute__((swift_name("fixable")));

/** Whether there are no issues at all, of any severity. */
@property (readonly) BOOL isClean __attribute__((swift_name("isClean")));
@property (readonly) NSArray<CalendrumCoreValidationIssue *> *issues __attribute__((swift_name("issues")));
@end


/**
 * The settings of the linter: the validation rules it builds on and the codes it leaves out.
 *
 * Immutable. Use [Default] for RFC 5545 rules and all checks on, or [builder].
 *
 * @property validation the validation rules that run before the interoperability checks.
 * @property disabled the codes whose issues are dropped from the report.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LintRules")))
@interface CalendrumCoreLintRules : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreLintRulesCompanion *companion __attribute__((swift_name("companion")));

/** Returns a builder that starts from these rules. */
- (CalendrumCoreLintRulesBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns the number of disabled codes. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSSet<CalendrumCoreProblemCode *> *disabled __attribute__((swift_name("disabled")));
@property (readonly) CalendrumCoreRuleSet *validation __attribute__((swift_name("validation")));
@end


/** Changes a copy of [LintRules]. Not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LintRules.Builder")))
@interface CalendrumCoreLintRulesBuilder : CalendrumCoreBase

/** Creates the rules. */
- (CalendrumCoreLintRules *)build __attribute__((swift_name("build()")));

/** Leaves the issues with [code] out of the report. */
- (CalendrumCoreLintRulesBuilder *)disableCode:(CalendrumCoreProblemCode *)code __attribute__((swift_name("disable(code:)")));

/** Puts [code] back, if it was disabled. */
- (CalendrumCoreLintRulesBuilder *)enableCode:(CalendrumCoreProblemCode *)code __attribute__((swift_name("enable(code:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Sets the validation rules to [rules]. */
- (CalendrumCoreLintRulesBuilder *)validationRules:(CalendrumCoreRuleSet *)rules __attribute__((swift_name("validation(rules:)")));
@end


/** The default rules and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LintRules.Companion")))
@interface CalendrumCoreLintRulesCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default rules and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreLintRulesCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the default rules. */
- (CalendrumCoreLintRulesBuilder *)builder __attribute__((swift_name("builder()")));

/** RFC 5545 validation rules with every check on. */
@property (readonly) CalendrumCoreLintRules *Default __attribute__((swift_name("Default")));
@end


/**
 * Finds problems that make a calendar fail in real-world clients, in addition to the plain rule checks of the
 * validator.
 *
 * It runs the validator and then checks interoperability: `X-WR-TIMEZONE` beside floating times, non-IANA `TZID`s,
 * `X-WR-CALNAME` without `NAME`, an upper-case `MAILTO:` and empty values of non-text properties. Many of the issues
 * can be repaired automatically with [fix]. Linting does not change the calendar.
 *
 * @sample io.github.calendrum.samples.lintAndFix
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Linter")))
@interface CalendrumCoreLinter : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Finds problems that make a calendar fail in real-world clients, in addition to the plain rule checks of the
 * validator.
 *
 * It runs the validator and then checks interoperability: `X-WR-TIMEZONE` beside floating times, non-IANA `TZID`s,
 * `X-WR-CALNAME` without `NAME`, an upper-case `MAILTO:` and empty values of non-text properties. Many of the issues
 * can be repaired automatically with [fix]. Linting does not change the calendar.
 *
 * @sample io.github.calendrum.samples.lintAndFix
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)linter __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreLinter *shared __attribute__((swift_name("shared")));

/**
 * Repairs the fixable issues of [report] in [calendar] and returns the new calendar.
 *
 * The repairs are the ones listed by `LintReport.fixable`: empty values, non-IANA `TZID`s, `X-WR-TIMEZONE`,
 * `X-WR-CALNAME`, an upper-case `MAILTO:`, an extra `DURATION` beside `DTEND`, an `UNTIL` of the wrong type, and a
 * value out of range such as `PRIORITY`. Other issues are left for the caller.
 *
 * @param calendar the calendar that was linted.
 * @param report the report of that calendar.
 */
- (CalendrumCoreVCalendar *)fixCalendar:(CalendrumCoreVCalendar *)calendar report:(CalendrumCoreLintReport *)report __attribute__((swift_name("fix(calendar:report:)")));

/**
 * Lints [calendar] with the default rules.
 *
 * @param calendar the calendar to check.
 */
- (CalendrumCoreLintReport *)lintCalendar:(CalendrumCoreVCalendar *)calendar __attribute__((swift_name("lint(calendar:)")));

/**
 * Lints [calendar] with the given [rules].
 *
 * @param calendar the calendar to check.
 * @param rules the validation rules and the codes to leave out.
 */
- (CalendrumCoreLintReport *)lintCalendar:(CalendrumCoreVCalendar *)calendar rules:(CalendrumCoreLintRules *)rules __attribute__((swift_name("lint(calendar:rules:)")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * What an alarm does when it fires ([RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1),
 * `ACTION`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AlarmAction")))
@interface CalendrumCoreAlarmAction : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreAlarmActionCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a AlarmAction with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of AlarmAction and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AlarmAction.Companion")))
@interface CalendrumCoreAlarmActionCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of AlarmAction and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreAlarmActionCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new AlarmAction holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreAlarmAction *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Plays a sound. */
@property (readonly) CalendrumCoreAlarmAction *Audio __attribute__((swift_name("Audio")));

/** `DISPLAY`. Shows a message. */
@property (readonly) CalendrumCoreAlarmAction *Display __attribute__((swift_name("Display")));

/** `EMAIL`. Sends an e-mail. */
@property (readonly) CalendrumCoreAlarmAction *Email __attribute__((swift_name("Email")));
@end


/**
 * The value of an `ATTACH` property: a document by reference ([Uri]) or inline ([Binary])
 * ([RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)).
 */
__attribute__((swift_name("Attach")))
@protocol CalendrumCoreAttach
@required

/** The media type of the attachment (`FMTTYPE`), or `null`. */
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/**
 * An attachment whose content is inline, base64 encoded in the text form.
 *
 * Build one with `Attach.Binary(bytes)`; the bytes are copied.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AttachBinary")))
@interface CalendrumCoreAttachBinary : CalendrumCoreBase <CalendrumCoreAttach>
- (instancetype)initWithBytes:(CalendrumCoreKotlinByteArray *)bytes __attribute__((swift_name("init(bytes:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumCoreKotlinByteArray *)bytes formatType:(NSString *)formatType __attribute__((swift_name("init(bytes:formatType:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the content. */
- (CalendrumCoreKotlinByteArray *)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a Binary equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));

/** The length of the content in bytes. */
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end


/**
 * An attachment given by a URI.
 *
 * Build one with `Attach.Uri("https://example.com/agenda.pdf")`.
 *
 * @property uri the location of the document.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AttachUri")))
@interface CalendrumCoreAttachUri : CalendrumCoreBase <CalendrumCoreAttach>
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri formatType:(NSString *)formatType __attribute__((swift_name("init(uri:formatType:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Uri equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *uri __attribute__((swift_name("uri")));
@end


/**
 * The value of an `ATTENDEE` property: a calendar user taking part, with the parameters that describe their role and
 * answer ([RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 *
 * The typed fields are views of the parameters; [parameters] keeps all of them, unknown ones included, so a value
 * written back is not changed. Build one with [builder], change one with [toBuilder]. Immutable.
 *
 * @property address the calendar address, usually `mailto:jane@example.com`.
 * @property parameters all parameters as written.
 * @property cn the common name (`CN`), or `null`.
 * @property role the role (`ROLE`); [Role.ReqParticipant] when absent.
 * @property partStat the participation status (`PARTSTAT`); [PartStat.NeedsAction] when absent.
 * @property rsvp whether a reply is requested (`RSVP`); false when absent.
 * @property cuType the calendar user type (`CUTYPE`); [CuType.Individual] when absent.
 * @property delegatedTo the addresses the attendance was delegated to (`DELEGATED-TO`).
 * @property delegatedFrom the addresses that delegated their attendance to this user (`DELEGATED-FROM`).
 * @property member the groups the user is a member of (`MEMBER`).
 * @property sentBy the address that acts on behalf of the user (`SENT-BY`), or `null`.
 * @property dir the directory entry URI (`DIR`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property email the e-mail address of [RFC 7986](https://www.rfc-editor.org/rfc/rfc7986#section-6.2) (`EMAIL`), or
 *   `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Attendee")))
@interface CalendrumCoreAttendee : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreAttendeeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Attendee equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this attendee, parameters included. */
- (CalendrumCoreAttendeeBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) CalendrumCoreCuType *cuType __attribute__((swift_name("cuType")));
@property (readonly) NSArray<NSString *> *delegatedFrom __attribute__((swift_name("delegatedFrom")));
@property (readonly) NSArray<NSString *> *delegatedTo __attribute__((swift_name("delegatedTo")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<NSString *> *member __attribute__((swift_name("member")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumCorePartStat *partStat __attribute__((swift_name("partStat")));
@property (readonly) CalendrumCoreRole *role __attribute__((swift_name("role")));
@property (readonly) BOOL rsvp __attribute__((swift_name("rsvp")));
@property (readonly) NSString * _Nullable sentBy __attribute__((swift_name("sentBy")));
@end


/**
 * Collects the address and the parameters of an [Attendee]. The properties and the functions of the same name do
 * the same; the functions chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Attendee.Builder")))
@interface CalendrumCoreAttendeeBuilder : CalendrumCoreBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the attendee.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumCoreAttendee *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreAttendeeBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [cuType] to [value] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)cuTypeValue:(CalendrumCoreCuType *)value __attribute__((swift_name("cuType(value:)")));

/** Sets [delegatedFrom] to [values] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)delegatedFromValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedFrom(values:)")));

/** Sets [delegatedTo] to [values] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)delegatedToValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedTo(values:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreAttendeeBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreAttendeeBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreAttendeeBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/** Sets [member] to [values] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)memberValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("member(values:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumCoreAttendeeBuilder *)parameterName:(NSString *)name values:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Sets [partStat] to [value] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)partStatValue:(CalendrumCorePartStat *)value __attribute__((swift_name("partStat(value:)")));

/** Removes the parameter [name]. */
- (CalendrumCoreAttendeeBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [role] to [value] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)roleValue:(CalendrumCoreRole *)value __attribute__((swift_name("role(value:)")));

/** Sets [rsvp] to [value] and returns this builder. */
- (CalendrumCoreAttendeeBuilder *)rsvpValue:(BOOL)value __attribute__((swift_name("rsvp(value:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreAttendeeBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The calendar address. */
@property NSString *address __attribute__((swift_name("address")));

/** The common name (`CN`); assigning `null` removes the parameter. */
@property NSString * _Nullable cn __attribute__((swift_name("cn")));

/** The calendar user type (`CUTYPE`); [CuType.Individual] when absent. */
@property CalendrumCoreCuType *cuType __attribute__((swift_name("cuType")));

/**
 * The addresses that delegated their attendance to this user (`DELEGATED-FROM`); an empty list removes the
 * parameter.
 */
@property NSArray<NSString *> *delegatedFrom __attribute__((swift_name("delegatedFrom")));

/** The addresses the attendance was delegated to (`DELEGATED-TO`); an empty list removes the parameter. */
@property NSArray<NSString *> *delegatedTo __attribute__((swift_name("delegatedTo")));

/** The directory entry URI (`DIR`); assigning `null` removes the parameter. */
@property NSString * _Nullable dir __attribute__((swift_name("dir")));

/** The `EMAIL` address of RFC 7986; assigning `null` removes the parameter. */
@property NSString * _Nullable email __attribute__((swift_name("email")));

/** The `LANGUAGE`; assigning `null` removes the parameter. */
@property NSString * _Nullable language __attribute__((swift_name("language")));

/** The groups the user is a member of (`MEMBER`); an empty list removes the parameter. */
@property NSArray<NSString *> *member __attribute__((swift_name("member")));

/** The participation status (`PARTSTAT`); [PartStat.NeedsAction] when absent. */
@property CalendrumCorePartStat *partStat __attribute__((swift_name("partStat")));

/** The role (`ROLE`); [Role.ReqParticipant] when absent. */
@property CalendrumCoreRole *role __attribute__((swift_name("role")));

/** Whether a reply is requested (`RSVP`); false when absent. */
@property BOOL rsvp __attribute__((swift_name("rsvp")));

/** The address that acts on behalf of the user (`SENT-BY`); assigning `null` removes the parameter. */
@property NSString * _Nullable sentBy __attribute__((swift_name("sentBy")));
@end


/** Creates builders for [Attendee]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Attendee.Companion")))
@interface CalendrumCoreAttendeeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Attendee]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreAttendeeCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an attendee with the given calendar [address]. */
- (CalendrumCoreAttendeeBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
@end


/**
 * An editable view of the `ATTENDEE` properties of a builder, addressed by calendar address (compared ignoring ASCII
 * case).
 *
 * @property items the attendees now in the list.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AttendeeList")))
@interface CalendrumCoreAttendeeList : CalendrumCoreBase

/** Adds [attendee] after the existing ones. */
- (void)addAttendee:(CalendrumCoreAttendee *)attendee __attribute__((swift_name("add(attendee:)")));

/**
 * Adds an attendee with the address [address], set up by [block].
 *
 * @param address the calendar address of the new attendee.
 * @param block sets up the attendee.
 */
- (void)addAddress:(NSString *)address block:(void (^)(CalendrumCoreAttendeeBuilder *))block __attribute__((swift_name("add(address:block:)")));

/**
 * Removes every attendee with the address [address]. Returns `true` when there was one.
 *
 * @param address the calendar address, compared ignoring ASCII case.
 */
- (BOOL)removeAddress:(NSString *)address __attribute__((swift_name("remove(address:)")));

/**
 * Changes the attendee with the address [address] by [block]; parameters it does not touch stay. Returns `false`
 * when there is no such attendee.
 *
 * @param address the calendar address to look for, compared ignoring ASCII case.
 * @param block changes the attendee.
 */
- (BOOL)replaceAddress:(NSString *)address block:(void (^)(CalendrumCoreAttendeeBuilder *))block __attribute__((swift_name("replace(address:block:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumCoreAttendee *> *items __attribute__((swift_name("items")));
@end


/**
 * What a builder uses when a required property is missing: a clock for `DTSTAMP` and a generator for `UID`.
 *
 * The library never reads the system clock or a global generator by itself.
 *
 * @property clock gives the time for `DTSTAMP`.
 * @property uids makes the `UID`s.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BuildDefaults")))
@interface CalendrumCoreBuildDefaults : CalendrumCoreBase
- (instancetype)initWithClock:(id<CalendrumCoreKotlinClock>)clock uids:(id<CalendrumCoreUidGenerator>)uids __attribute__((swift_name("init(clock:uids:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumCoreKotlinClock> clock __attribute__((swift_name("clock")));
@property (readonly) id<CalendrumCoreUidGenerator> uids __attribute__((swift_name("uids")));
@end


/**
 * The busy status in the Microsoft extension `X-MICROSOFT-CDO-BUSYSTATUS` (a de-facto property of Outlook and Exchange,
 * not a standard).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BusyStatus")))
@interface CalendrumCoreBusyStatus : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreBusyStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a BusyStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of BusyStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BusyStatus.Companion")))
@interface CalendrumCoreBusyStatusCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of BusyStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreBusyStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new BusyStatus holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreBusyStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BUSY`. Busy. */
@property (readonly) CalendrumCoreBusyStatus *Busy __attribute__((swift_name("Busy")));

/** `FREE`. Free. */
@property (readonly) CalendrumCoreBusyStatus *Free __attribute__((swift_name("Free")));

/** `OOF`. Out of office. */
@property (readonly) CalendrumCoreBusyStatus *Oof __attribute__((swift_name("Oof")));

/** `TENTATIVE`. Tentatively busy. */
@property (readonly) CalendrumCoreBusyStatus *Tentative __attribute__((swift_name("Tentative")));

/** `WORKINGELSEWHERE`. Working elsewhere. */
@property (readonly) CalendrumCoreBusyStatus *WorkingElsewhere __attribute__((swift_name("WorkingElsewhere")));
@end


/** Canonical forms for comparing and hashing calendars, and name-based identifiers. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Canonical")))
@interface CalendrumCoreCanonical : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Canonical forms for comparing and hashing calendars, and name-based identifiers. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)canonical __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreCanonical *shared __attribute__((swift_name("shared")));

/**
 * Returns the canonical bytes of [calendar]: properties and parameters sorted, nothing else changed, written
 * canonically. Two calendars with the same content give the same bytes, whatever their order or spelling.
 *
 * @throws IllegalArgumentException if a property cannot be written canonically, as with [ICalWriter]: a control
 *   character other than HTAB in a value, or a parameter without a value (`ICAL-S012`). Parsing keeps both.
 */
- (CalendrumCoreKotlinByteArray *)canonicalizeCalendar:(CalendrumCoreVCalendar *)calendar __attribute__((swift_name("canonicalize(calendar:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Returns a UUID version 5 text for [naturalKey] in [namespace]: the same inputs always give the same `UID`.
 *
 * @param namespace a UUID text, or any other text, which is mapped to a UUID in the DNS namespace.
 * @param naturalKey the key that identifies the item.
 */
- (NSString *)uidV5Namespace:(NSString *)namespace_ naturalKey:(NSString *)naturalKey __attribute__((swift_name("uidV5(namespace:naturalKey:)")));
@end


/** How many times a property may occur in a component. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Cardinality")))
@interface CalendrumCoreCardinality : CalendrumCoreKotlinEnum<CalendrumCoreCardinality *>
+ (instancetype)alloc __attribute__((unavailable));

/** How many times a property may occur in a component. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreCardinality *one __attribute__((swift_name("one")));
@property (class, readonly) CalendrumCoreCardinality *optional __attribute__((swift_name("optional")));
@property (class, readonly) CalendrumCoreCardinality *many __attribute__((swift_name("many")));
@property (class, readonly) CalendrumCoreCardinality *oneormore __attribute__((swift_name("oneormore")));
+ (CalendrumCoreKotlinArray<CalendrumCoreCardinality *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreCardinality *> *entries __attribute__((swift_name("entries")));
@end


/**
 * The value of a `CATEGORIES` property: one or more category names
 * ([RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)).
 *
 * Build one with `Categories(listOf("Work", "Weekly"))`; the list must not be empty. Immutable.
 *
 * @property values the category names, unescaped.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Categories")))
@interface CalendrumCoreCategories : CalendrumCoreBase
- (instancetype)initWithValues:(NSArray<NSString *> *)values __attribute__((swift_name("init(values:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Categories equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<NSString *> *values __attribute__((swift_name("values")));
@end


/**
 * The access classification of a component
 * ([RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3), `CLASS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Classification")))
@interface CalendrumCoreClassification : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreClassificationCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Classification with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Classification and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Classification.Companion")))
@interface CalendrumCoreClassificationCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Classification and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreClassificationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Classification holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreClassification *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CONFIDENTIAL`. The details are shown only to a limited set of people. */
@property (readonly) CalendrumCoreClassification *Confidential __attribute__((swift_name("Confidential")));

/** `PRIVATE`. Only the owner may see the details. */
@property (readonly) CalendrumCoreClassification *Private __attribute__((swift_name("Private")));

/** `PUBLIC`. Anyone may see the details; the default. */
@property (readonly) CalendrumCoreClassification *Public __attribute__((swift_name("Public")));
@end


/**
 * The common part of the builders of all components: reading and writing properties by key or by name.
 *
 * The typed builders ([VEvent.Builder] and the others) add the accessors of their component. A builder starts empty or
 * from an existing component, shares the nodes it does not touch with it, and is not thread-safe.
 */
__attribute__((swift_name("ComponentBuilder")))
@interface CalendrumCoreComponentBuilder<B> : CalendrumCoreBase

/** Returns the value of the first property of [key], or `null`. */
- (id _Nullable)getKey:(CalendrumCorePropertyKey<id> *)key __attribute__((swift_name("get(key:)")));

/**
 * Adds [property] as it is.
 *
 * @return this builder.
 */
- (B)propertyProperty:(CalendrumCoreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Adds a property of [key] with [value] after the existing ones.
 *
 * @return this builder.
 */
- (B)propertyKey:(CalendrumCorePropertyKey<id> *)key value:(id)value __attribute__((swift_name("property(key:value:)")));

/**
 * Adds a property with the given [name] and [value] as text.
 *
 * @return this builder.
 * @throws IllegalArgumentException if the name or the value is invalid.
 */
- (B)propertyName:(NSString *)name value:(NSString *)value __attribute__((swift_name("property(name:value:)")));

/**
 * Removes every property of [key].
 *
 * @return this builder.
 */
- (B)removeKey:(CalendrumCorePropertyKey<id> *)key __attribute__((swift_name("remove(key:)")));

/**
 * Removes every property named [name].
 *
 * @return this builder.
 */
- (B)removeName:(NSString *)name __attribute__((swift_name("remove(name:)")));

/**
 * Sets the property of [key] to [value], replacing the first one in place and dropping later ones; `null` removes
 * the property.
 *
 * @return this builder.
 */
- (B)setKey:(CalendrumCorePropertyKey<id> *)key value:(id _Nullable)value __attribute__((swift_name("set(key:value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * What the registry knows about a component. Immutable.
 *
 * @property source the standard or vendor that defines it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComponentDefinition")))
@interface CalendrumCoreComponentDefinition : CalendrumCoreBase
- (instancetype)initWithName:(NSString *)name children:(NSSet<NSString *> *)children source:(NSString *)source __attribute__((swift_name("init(name:children:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The names of the components that may be nested in it. */
@property (readonly) NSSet<NSString *> *children __attribute__((swift_name("children")));

/** The canonical name of the component. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@end


/**
 * An editable view of the nested components of one kind in a builder, such as the alarms of an event.
 *
 * @property items the components now in the list.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComponentList")))
@interface CalendrumCoreComponentList<T> : CalendrumCoreBase

/**
 * Adds [item] after the existing components.
 *
 * @throws IllegalArgumentException if [item] is not of the kind this list holds.
 */
- (void)addItem:(T)item __attribute__((swift_name("add(item:)")));

/**
 * Removes [item], found by identity or else by equality.
 *
 * @return `true` when it was found.
 */
- (BOOL)removeItem:(T)item __attribute__((swift_name("remove(item:)")));

/**
 * Replaces [old] with [new] in place, found by identity or else by equality.
 *
 * @return `true` when [old] was found.
 * @throws IllegalArgumentException if [new] is not of the kind this list holds.
 */
- (BOOL)replaceOld:(T)old new:(T)new_ __attribute__((swift_name("replace(old:new:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<T> *items __attribute__((swift_name("items")));
@end


/**
 * The changes to one component of an [ICalPatch].
 *
 * Setting replaces the first property of that name (and drops later ones) or adds the property after the last one;
 * removing drops all properties of that name.
 *
 * @property uid the `UID` of the component.
 * @property recurrenceId the `RECURRENCE-ID` of the component, or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComponentPatch")))
@interface CalendrumCoreComponentPatch : CalendrumCoreBase

/**
 * Changes the parameters of the `ATTENDEE` with the address [address], as [block] says. Nothing happens, and the
 * selector is reported as unmatched, when there is no such attendee.
 */
- (CalendrumCoreComponentPatch *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumCorePropertyPatch *))block __attribute__((swift_name("attendee(address:block:)")));

/** Changes the parameters of the first property of [key], as [block] says. */
- (CalendrumCoreComponentPatch *)propertyKey:(CalendrumCorePropertyKey<id> *)key block:(void (^)(CalendrumCorePropertyPatch *))block __attribute__((swift_name("property(key:block:)")));

/** Changes the parameters of the first property named [name], as [block] says. */
- (CalendrumCoreComponentPatch *)propertyName:(NSString *)name block:(void (^)(CalendrumCorePropertyPatch *))block __attribute__((swift_name("property(name:block:)")));

/** Removes every property of [key]. */
- (CalendrumCoreComponentPatch *)removeKey:(CalendrumCorePropertyKey<id> *)key __attribute__((swift_name("remove(key:)")));

/** Removes every property named [name]. */
- (CalendrumCoreComponentPatch *)removeName:(NSString *)name __attribute__((swift_name("remove(name:)")));

/** Sets the property of [key] to [value]. */
- (CalendrumCoreComponentPatch *)setKey:(CalendrumCorePropertyKey<id> *)key value:(id)value __attribute__((swift_name("set(key:value:)")));

/**
 * Sets the property [name] to the text [value], written as is.
 *
 * @throws IllegalArgumentException if [name] or [value] is invalid.
 */
- (CalendrumCoreComponentPatch *)setName:(NSString *)name value:(NSString *)value __attribute__((swift_name("set(name:value:)")));

/** Returns the number of operations. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));
@property (readonly) NSString *uid __attribute__((swift_name("uid")));
@end


/**
 * The value of a `CONFERENCE` property: a way to join an online or phone meeting
 * ([RFC 7986 §5.11](https://www.rfc-editor.org/rfc/rfc7986#section-5.11)).
 *
 * Build one with `Conference("https://meet.example.com/abc")`. Immutable.
 *
 * @property uri the address to join, such as an https or tel URI.
 * @property features what the conference offers (`FEATURE`).
 * @property label a human-readable label (`LABEL`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Conference")))
@interface CalendrumCoreConference : CalendrumCoreBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri features:(NSArray<CalendrumCoreConferenceFeature *> *)features label:(NSString * _Nullable)label __attribute__((swift_name("init(uri:features:label:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Conference equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumCoreConferenceFeature *> *features __attribute__((swift_name("features")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *uri __attribute__((swift_name("uri")));
@end


/**
 * A feature of a conference ([RFC 7986 §6.3](https://www.rfc-editor.org/rfc/rfc7986#section-6.3), `FEATURE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConferenceFeature")))
@interface CalendrumCoreConferenceFeature : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreConferenceFeatureCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ConferenceFeature with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of ConferenceFeature and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ConferenceFeature.Companion")))
@interface CalendrumCoreConferenceFeatureCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ConferenceFeature and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreConferenceFeatureCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ConferenceFeature holding the upper-cased token
 * when it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreConferenceFeature *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Audio conferencing. */
@property (readonly) CalendrumCoreConferenceFeature *Audio __attribute__((swift_name("Audio")));

/** `CHAT`. Chat or instant messaging. */
@property (readonly) CalendrumCoreConferenceFeature *Chat __attribute__((swift_name("Chat")));

/** `FEED`. A blog or atom feed. */
@property (readonly) CalendrumCoreConferenceFeature *Feed __attribute__((swift_name("Feed")));

/** `MODERATOR`. Moderator dial-in. */
@property (readonly) CalendrumCoreConferenceFeature *Moderator __attribute__((swift_name("Moderator")));

/** `PHONE`. Phone conferencing. */
@property (readonly) CalendrumCoreConferenceFeature *Phone __attribute__((swift_name("Phone")));

/** `SCREEN`. Screen sharing. */
@property (readonly) CalendrumCoreConferenceFeature *Screen __attribute__((swift_name("Screen")));

/** `VIDEO`. Video conferencing. */
@property (readonly) CalendrumCoreConferenceFeature *Video __attribute__((swift_name("Video")));
@end


/**
 * The value of a `CONTACT` property: contact information as text
 * ([RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)).
 *
 * Build one with `Contact("Jane, +1 555 0100")`. Immutable.
 *
 * @property text the contact text, unescaped.
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Contact")))
@interface CalendrumCoreContact : CalendrumCoreBase
- (instancetype)initWithText:(NSString *)text __attribute__((swift_name("init(text:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Contact equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *text __attribute__((swift_name("text")));
@end


/**
 * The type of a calendar user ([RFC 5545 §3.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.3), `CUTYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CuType")))
@interface CalendrumCoreCuType : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreCuTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a CuType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of CuType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CuType.Companion")))
@interface CalendrumCoreCuTypeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of CuType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreCuTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new CuType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreCuType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `GROUP`. A group of people. */
@property (readonly) CalendrumCoreCuType *Group __attribute__((swift_name("Group")));

/** `INDIVIDUAL`. A person; the default. */
@property (readonly) CalendrumCoreCuType *Individual __attribute__((swift_name("Individual")));

/** `RESOURCE`. A resource other than a room, such as a projector. */
@property (readonly) CalendrumCoreCuType *Resource __attribute__((swift_name("Resource")));

/** `ROOM`. A room. */
@property (readonly) CalendrumCoreCuType *Room __attribute__((swift_name("Room")));

/** `UNKNOWN`. The type is not known. */
@property (readonly) CalendrumCoreCuType *Unknown __attribute__((swift_name("Unknown")));
@end


/**
 * How a codec decodes: leniently or strictly, and the factory for problems at the right position.
 *
 * @property isStrict whether a value that is only acceptable by repair is an error.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DecodeContext")))
@interface CalendrumCoreDecodeContext : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreDecodeContextCompanion *companion __attribute__((swift_name("companion")));

/** Creates a problem with [code] positioned [valueOffset] characters into the value of [property]. */
- (CalendrumCoreProblem *)problemProperty:(CalendrumCoreICalProperty *)property code:(CalendrumCoreProblemCode *)code valueOffset:(int32_t)valueOffset __attribute__((swift_name("problem(property:code:valueOffset:)")));

/** Creates a problem with [code] positioned at the value of [parameter] of [property]. */
- (CalendrumCoreProblem *)problemProperty:(CalendrumCoreICalProperty *)property parameter:(CalendrumCoreICalParameter *)parameter code:(CalendrumCoreProblemCode *)code __attribute__((swift_name("problem(property:parameter:code:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL isStrict __attribute__((swift_name("isStrict")));
@end


/** The two contexts. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DecodeContext.Companion")))
@interface CalendrumCoreDecodeContextCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The two contexts. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreDecodeContextCompanion *shared __attribute__((swift_name("shared")));

/** Reads known quirks of real-world data with a warning and a repair. */
@property (readonly) CalendrumCoreDecodeContext *Lenient __attribute__((swift_name("Lenient")));

/** Turns every repair into an error: the value is rejected. */
@property (readonly) CalendrumCoreDecodeContext *Strict __attribute__((swift_name("Strict")));
@end


/**
 * The result of decoding a property or parameter: a value, nothing, or a malformed one.
 *
 * Reading with `component[key]` gives only the value or `null`; `component.decode(key)` tells which of the three it was
 * and why.
 */
__attribute__((swift_name("Decoded")))
@protocol CalendrumCoreDecoded
@required
@end


/**
 * The property exists but could not be decoded.
 *
 * @property problem the error that explains why; never shows the value.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DecodedMalformed")))
@interface CalendrumCoreDecodedMalformed : CalendrumCoreBase <CalendrumCoreDecoded>
- (instancetype)initWithProblem:(CalendrumCoreProblem *)problem __attribute__((swift_name("init(problem:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreProblem *problem __attribute__((swift_name("problem")));
@end


/** The component has no such property. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DecodedMissing")))
@interface CalendrumCoreDecodedMissing : CalendrumCoreBase <CalendrumCoreDecoded>
+ (instancetype)alloc __attribute__((unavailable));

/** The component has no such property. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)missing __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreDecodedMissing *shared __attribute__((swift_name("shared")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * A value was decoded, possibly with problems that were repaired (warnings and infos).
 *
 * @property value the decoded value.
 * @property problems the repairs made while decoding; empty when the value was clean.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DecodedValue")))
@interface CalendrumCoreDecodedValue<T> : CalendrumCoreBase <CalendrumCoreDecoded>
- (instancetype)initWithValue:(T)value problems:(NSArray<CalendrumCoreProblem *> *)problems __attribute__((swift_name("init(value:problems:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumCoreProblem *> *problems __attribute__((swift_name("problems")));
@property (readonly) T value __attribute__((swift_name("value")));
@end


/**
 * What a codec needs to write: the name of the property or parameter.
 *
 * @property name the name to write.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("EncodeContext")))
@interface CalendrumCoreEncodeContext : CalendrumCoreBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/**
 * How an inline value is encoded ([RFC 5545 §3.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.7), `ENCODING`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Encoding")))
@interface CalendrumCoreEncoding : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreEncodingCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Encoding with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Encoding and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Encoding.Companion")))
@interface CalendrumCoreEncodingCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Encoding and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreEncodingCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Encoding holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreEncoding *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BASE64`. The value is base64 encoded binary data. */
@property (readonly) CalendrumCoreEncoding *Base64 __attribute__((swift_name("Base64")));

/** `8BIT`. The text is written as is; the default. */
@property (readonly) CalendrumCoreEncoding *EightBit __attribute__((swift_name("EightBit")));
@end


/**
 * The status of an event ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11), `STATUS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("EventStatus")))
@interface CalendrumCoreEventStatus : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreEventStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a EventStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of EventStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("EventStatus.Companion")))
@interface CalendrumCoreEventStatusCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of EventStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreEventStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new EventStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreEventStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The event is cancelled. */
@property (readonly) CalendrumCoreEventStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `CONFIRMED`. The event is confirmed. */
@property (readonly) CalendrumCoreEventStatus *Confirmed __attribute__((swift_name("Confirmed")));

/** `TENTATIVE`. The event is tentative. */
@property (readonly) CalendrumCoreEventStatus *Tentative __attribute__((swift_name("Tentative")));
@end


/**
 * The value of an `EXDATE` property: dates excluded from a recurrence set
 * ([RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)).
 *
 * @property dates the excluded dates, all of the same form.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ExDate")))
@interface CalendrumCoreExDate : CalendrumCoreBase
- (instancetype)initWithDates:(NSArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("init(dates:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a ExDate equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *dates __attribute__((swift_name("dates")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/**
 * The free/busy type of a period ([RFC 5545 §3.2.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.9), `FBTYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FbType")))
@interface CalendrumCoreFbType : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreFbTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a FbType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of FbType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FbType.Companion")))
@interface CalendrumCoreFbTypeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of FbType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreFbTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new FbType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreFbType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BUSY`. The time is busy; the default. */
@property (readonly) CalendrumCoreFbType *Busy __attribute__((swift_name("Busy")));

/** `BUSY-TENTATIVE`. The time is tentatively busy. */
@property (readonly) CalendrumCoreFbType *BusyTentative __attribute__((swift_name("BusyTentative")));

/** `BUSY-UNAVAILABLE`. The time is busy and unavailable. */
@property (readonly) CalendrumCoreFbType *BusyUnavailable __attribute__((swift_name("BusyUnavailable")));

/** `FREE`. The time is free. */
@property (readonly) CalendrumCoreFbType *Free __attribute__((swift_name("Free")));
@end


/**
 * Keeps the scheduling properties of a published feed right between two versions of it.
 *
 * For every component that is new or whose content changed since the previous version, [publish] sets `DTSTAMP` and
 * `LAST-MODIFIED` to the time of the clock and raises `SEQUENCE`; unchanged components keep their old values, so
 * clients do not see changes that did not happen.
 *
 * @property clock gives the time for the new `DTSTAMP`.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FeedPublisher")))
@interface CalendrumCoreFeedPublisher : CalendrumCoreBase
- (instancetype)initWithClock:(id<CalendrumCoreKotlinClock>)clock __attribute__((swift_name("init(clock:)"))) __attribute__((objc_designated_initializer));

/**
 * Returns [current] with the properties updated as described, comparing it with [previous].
 *
 * @param previous the version published last time, or `null` for the first one.
 * @param current the version to publish.
 * @throws IllegalArgumentException if [previous] or [current] cannot be written canonically, as with
 *   [Canonical.canonicalize].
 */
- (CalendrumCoreVCalendar *)publishPrevious:(CalendrumCoreVCalendar * _Nullable)previous current:(CalendrumCoreVCalendar *)current __attribute__((swift_name("publish(previous:current:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumCoreKotlinClock> clock __attribute__((swift_name("clock")));
@end


/**
 * The value of a `FREEBUSY` property: periods of one free/busy type
 * ([RFC 5545 §3.8.2.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.6)).
 *
 * @property type the free/busy type; [FbType.Busy] by default.
 * @property periods the periods, in UTC.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FreeBusy")))
@interface CalendrumCoreFreeBusy : CalendrumCoreBase
- (instancetype)initWithPeriods:(NSArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("init(periods:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithType:(CalendrumCoreFbType *)type periods:(NSArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("init(type:periods:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a FreeBusy equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<CalendrumCoreICalPeriod *> *periods __attribute__((swift_name("periods")));
@property (readonly) CalendrumCoreFbType *type __attribute__((swift_name("type")));
@end


/**
 * The value of a `GEO` property: a latitude and a longitude
 * ([RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)).
 *
 * Build one with `Geo(52.52, 13.405)`. Equality compares the decimal text, so a value written back is not changed by
 * rounding.
 *
 * @property latitude the latitude in degrees, from -90 to 90.
 * @property longitude the longitude in degrees, from -180 to 180.
 * @property parameters all parameters of the property as written, including unknown ones.
 * @throws IllegalArgumentException when constructed with a coordinate out of range.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Geo")))
@interface CalendrumCoreGeo : CalendrumCoreBase
- (instancetype)initWithLatitude:(double)latitude longitude:(double)longitude __attribute__((swift_name("init(latitude:longitude:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Geo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double latitude __attribute__((swift_name("latitude")));
@property (readonly) double longitude __attribute__((swift_name("longitude")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/** A set of definitions that can be installed into a registry, such as a standard or a vendor's properties. */
__attribute__((swift_name("ICalExtension")))
@protocol CalendrumCoreICalExtension
@required

/** Adds this extension's definitions to [registry].
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (void)contributeRegistry:(CalendrumCoreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));
@end


/**
 * Changes a few properties of the components of an `.ics` file and leaves every other byte as it was.
 *
 * A patch finds each component by its `UID` and `RECURRENCE-ID` through fingerprints, so the rest of the file is never
 * parsed. Only the lines that change are rewritten (canonically); the others, including the line ends, stay byte for
 * byte. That keeps the checksum-like identity a CalDAV server derives from the file stable for everything you did not
 * touch. The patch itself is a description of operations; nothing happens until [apply].
 *
 * @param original the bytes of the file to patch.
 * @param block lists the components to change.
 * @sample io.github.calendrum.samples.patchFile
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalPatch")))
@interface CalendrumCoreICalPatch : CalendrumCoreBase
- (instancetype)initWithOriginal:(CalendrumCoreKotlinByteArray *)original block:(void (^)(CalendrumCoreICalPatchBuilder *))block __attribute__((swift_name("init(original:block:)"))) __attribute__((objc_designated_initializer));

/**
 * Runs the patch and returns the new bytes, the selectors that found nothing and the lines that changed.
 *
 * When there is nothing to change the result holds the original array. The original is never modified.
 */
- (CalendrumCorePatchResult *)apply __attribute__((swift_name("apply()")));

/** Returns the number of components to change. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** Collects the components to change. Not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalPatch.Builder")))
@interface CalendrumCoreICalPatchBuilder : CalendrumCoreBase

/**
 * Adds a component to change, found by its [uid] and [recurrenceId], with the operations that [block] lists.
 *
 * @param uid the `UID` of the component.
 * @param recurrenceId the `RECURRENCE-ID` text of an override, or `null` for the master or a single component.
 * @param block lists the changes.
 */
- (CalendrumCoreICalPatchBuilder *)componentUid:(NSString *)uid recurrenceId:(NSString * _Nullable)recurrenceId block:(void (^)(CalendrumCoreComponentPatch *))block __attribute__((swift_name("component(uid:recurrenceId:block:)")));

/** Returns the number of components to change. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The table of what every property, parameter and component is: its value type, allowed types, parameters, cardinality
 * and where it comes from.
 *
 * The registry drives eager decoding, strict mode and `decodeAny`, and tells the validator what a known name is.
 * [Default] holds RFC 5545, RFC 7986, RFC 9073, RFC 9074 and RFC 9253. Add your own `X-` properties, or the vendor
 * properties of [VendorProperties], by building a registry with [builder]. A registry is immutable and has no global
 * state: pass it to the parser through `ParseOptions`.
 *
 * @property extensions the extensions installed in this registry, in order.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalRegistry")))
@interface CalendrumCoreICalRegistry : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreICalRegistryCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the definition of the component [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumCoreComponentDefinition * _Nullable)componentName:(NSString *)name __attribute__((swift_name("component(name:)")));

/**
 * Returns the definition of the parameter [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumCoreParameterDefinition * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/**
 * Returns the definition of the property [name], ignoring ASCII case, or `null` when the registry does not know it.
 * When several components define the property differently, the first definition is returned.
 */
- (CalendrumCorePropertyDefinition * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<CalendrumCoreICalExtension>> *extensions __attribute__((swift_name("extensions")));
@end


/** Collects definitions and extensions into an [ICalRegistry]. Not thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalRegistry.Builder")))
@interface CalendrumCoreICalRegistryBuilder : CalendrumCoreBase

/** Creates the registry. */
- (CalendrumCoreICalRegistry *)build __attribute__((swift_name("build()")));

/** Adds a component definition; the children of a definition for the same name are merged. */
- (CalendrumCoreICalRegistryBuilder *)componentDefinition:(CalendrumCoreComponentDefinition *)definition __attribute__((swift_name("component(definition:)")));

/**
 * Installs [extension] once: a second install of the same extension does nothing. The extension adds its
 * definitions to this builder.
 */
- (CalendrumCoreICalRegistryBuilder *)installExtension:(id<CalendrumCoreICalExtension>)extension __attribute__((swift_name("install(extension:)")));

/** Adds a parameter definition, replacing a definition of another key for the same name. */
- (CalendrumCoreICalRegistryBuilder *)parameterDefinition:(CalendrumCoreParameterDefinition *)definition __attribute__((swift_name("parameter(definition:)")));

/**
 * Adds a property definition. A definition for a key that is already defined is merged into it; one for the
 * same name in other components is kept beside it.
 */
- (CalendrumCoreICalRegistryBuilder *)propertyDefinition:(CalendrumCorePropertyDefinition *)definition __attribute__((swift_name("property(definition:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** The default registry and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalRegistry.Companion")))
@interface CalendrumCoreICalRegistryCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default registry and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreICalRegistryCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder; install [Rfc5545] and the other extensions you need.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumCoreICalRegistryBuilder *)builder __attribute__((swift_name("builder()")));

/** The registry with RFC 5545, RFC 7986, RFC 9073, RFC 9074 and RFC 9253; created on first use. */
@property (readonly) CalendrumCoreICalRegistry *Default __attribute__((swift_name("Default")));
@end


/**
 * Describes how a component maps to your own class, so a feed can be decoded straight into objects, or written from
 * them, with no annotations, no generated code and no reflection.
 *
 * Subclass it as an `object`, declare the fields with [field], [optionalField], [listField] and [childField], and build
 * the object in [create]. The fields are also the selection: the lexer reads only the properties of the schema, and no
 * tree is built. Decode with `ICal.parser().decode(bytes, schema)` or `decodeWithProblems`; write with
 * `ICal.writer().encode(items, schema, ...)`. A component that lacks a required field is skipped with `ICAL-M001`.
 *
 * @param component the name of the component the schema describes, such as `VEVENT`.
 * @sample io.github.calendrum.samples.decodeIntoClasses
 * @sample io.github.calendrum.samples.encodeFromClasses
 *
 * @note annotations
 *   kotlin.SubclassOptInRequired(markerClass=[NormalClass(value=io/github/calendrum/ExperimentalICalApi)])
*/
__attribute__((swift_name("IcsSchema")))
@interface CalendrumCoreIcsSchema<T> : CalendrumCoreBase
- (instancetype)initWithComponent:(NSString *)component __attribute__((swift_name("init(component:)"))) __attribute__((objc_designated_initializer));

/**
 * Declares a field that collects the child components of [schema], such as the alarms of an event.
 *
 * @param schema the schema of the child component.
 * @param get reads the children from an object when writing.
 * @throws IllegalArgumentException if [schema] is this schema.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumCoreIcsSchemaField<NSArray<id> *> *)childFieldSchema:(CalendrumCoreIcsSchema<id> *)schema get:(NSArray<id> *(^)(T))get __attribute__((swift_name("childField(schema:get:)")));

/**
 * Builds the object from the decoded [values]; read each declared field with `values[field]`.
 *
 * @param values the decoded values of one component.
 */
- (T)createValues:(CalendrumCoreIcsSchemaValues *)values __attribute__((swift_name("create(values:)")));

/**
 * Declares a required field that holds the value of the property [key] as it is decoded; [get] reads it back when
 * writing.
 *
 * @param key the property.
 * @param get reads the value from an object when writing.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumCoreIcsSchemaField<id> *)fieldKey:(CalendrumCorePropertyKey<id> *)key get:(id (^)(T))get __attribute__((swift_name("field(key:get:)")));

/**
 * Declares a required field whose value is converted from the decoded property value by [decode].
 *
 * @param key the property.
 * @param get reads the value from an object when writing; it returns the property value.
 * @param decode converts the decoded value into the type of the field.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumCoreIcsSchemaField<id> *)fieldKey:(CalendrumCorePropertyKey<id> *)key get:(id (^)(T))get decode:(id (^)(id))decode __attribute__((swift_name("field(key:get:decode:)")));

/**
 * Declares a field that collects every property [key] into a list; empty when there is none.
 *
 * @param key the property.
 * @param get reads the list from an object when writing; each element becomes a property.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumCoreIcsSchemaField<NSArray<id> *> *)listFieldKey:(CalendrumCorePropertyKey<id> *)key get:(NSArray<id> *(^)(T))get __attribute__((swift_name("listField(key:get:)")));

/**
 * Declares a list field whose elements are converted: [decode] when reading and [encode] when writing.
 *
 * @param key the property.
 * @param get reads the list from an object when writing.
 * @param decode converts each decoded property value into an element.
 * @param encode converts each element back into a property value.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumCoreIcsSchemaField<NSArray<id> *> *)listFieldKey:(CalendrumCorePropertyKey<id> *)key get:(NSArray<id> *(^)(T))get decode:(id _Nullable (^)(id))decode encode:(id (^)(id _Nullable))encode __attribute__((swift_name("listField(key:get:decode:encode:)")));

/**
 * Declares an optional field: `null` when the property is missing or does not decode.
 *
 * @param key the property.
 * @param get reads the value from an object when writing; `null` writes nothing.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumCoreIcsSchemaField<id> *)optionalFieldKey:(CalendrumCorePropertyKey<id> *)key get:(id _Nullable (^)(T))get __attribute__((swift_name("optionalField(key:get:)")));

/**
 * Declares an optional field whose value is converted from the decoded property value by [decode].
 *
 * @param key the property.
 * @param get reads the value from an object when writing; `null` writes nothing.
 * @param decode converts the decoded value into the type of the field.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumCoreIcsSchemaField<id> *)optionalFieldKey:(CalendrumCorePropertyKey<id> *)key get:(id _Nullable (^)(T))get decode:(id (^)(id))decode __attribute__((swift_name("optionalField(key:get:decode:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** A declared field of a schema, to read its value from [Values] in [IcsSchema.create]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IcsSchemaField")))
@interface CalendrumCoreIcsSchemaField<R> : CalendrumCoreBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** The decoded values of one component, handed to [IcsSchema.create]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IcsSchemaValues")))
@interface CalendrumCoreIcsSchemaValues : CalendrumCoreBase

/**
 * Returns the value of [field]: a malformed or missing optional value as `null`, a list field as a list (empty
 * if there is no property).
 *
 * @param field a field of the schema that decodes these values.
 * @throws IllegalArgumentException if [field] belongs to another schema.
 */
- (id _Nullable)getField:(CalendrumCoreIcsSchemaField<id> *)field __attribute__((swift_name("get(field:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The value of an `IMAGE` property: a picture of the component, by URI or inline
 * ([RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)).
 *
 * Build one with `Image("https://example.com/logo.png")` or from bytes and a media type. Immutable.
 *
 * @property uri the location of the image, or `null` for an inline image.
 * @property formatType the media type (`FMTTYPE`), or `null`.
 * @property display how the image is meant to be shown (`DISPLAY`); [ImageDisplay.Badge] by default.
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Image")))
@interface CalendrumCoreImage : CalendrumCoreBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumCoreKotlinByteArray *)bytes formatType:(NSString *)formatType __attribute__((swift_name("init(bytes:formatType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri formatType:(NSString *)formatType __attribute__((swift_name("init(uri:formatType:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the inline content, or `null` when the image is given by a URI. */
- (CalendrumCoreKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a Image equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSArray<CalendrumCoreImageDisplay *> *display __attribute__((swift_name("display")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the image is inline (base64 encoded in the text form) and not given by a URI. */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable uri __attribute__((swift_name("uri")));
@end


/**
 * How an image is meant to be shown ([RFC 7986 §6.1](https://www.rfc-editor.org/rfc/rfc7986#section-6.1), `DISPLAY`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImageDisplay")))
@interface CalendrumCoreImageDisplay : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreImageDisplayCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ImageDisplay with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of ImageDisplay and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImageDisplay.Companion")))
@interface CalendrumCoreImageDisplayCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ImageDisplay and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreImageDisplayCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ImageDisplay holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreImageDisplay *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BADGE`. A small image shown next to the title. */
@property (readonly) CalendrumCoreImageDisplay *Badge __attribute__((swift_name("Badge")));

/** `FULLSIZE`. A full-size version of an image. */
@property (readonly) CalendrumCoreImageDisplay *Fullsize __attribute__((swift_name("Fullsize")));

/** `GRAPHIC`. An image shown as a graphic with the content. */
@property (readonly) CalendrumCoreImageDisplay *Graphic __attribute__((swift_name("Graphic")));

/** `THUMBNAIL`. A reduced-size version of an image. */
@property (readonly) CalendrumCoreImageDisplay *Thumbnail __attribute__((swift_name("Thumbnail")));
@end


/**
 * The status of a journal entry ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11),
 * `STATUS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JournalStatus")))
@interface CalendrumCoreJournalStatus : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreJournalStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a JournalStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of JournalStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JournalStatus.Companion")))
@interface CalendrumCoreJournalStatusCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of JournalStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreJournalStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new JournalStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreJournalStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The entry is cancelled. */
@property (readonly) CalendrumCoreJournalStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `DRAFT`. The entry is a draft. */
@property (readonly) CalendrumCoreJournalStatus *Draft __attribute__((swift_name("Draft")));

/** `FINAL`. The entry is final. */
@property (readonly) CalendrumCoreJournalStatus *Final __attribute__((swift_name("Final")));
@end


/**
 * The value of a `LINK` property: a typed link to another resource
 * ([RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)).
 *
 * @property value the URI, `UID` or XML reference.
 * @property valueType [ValueType.Uri], [ValueType.Uid] or [ValueType.XmlReference].
 * @property linkRel the relation type (`LINKREL`), usually a URI or IANA link relation name, or `null`.
 * @property formatType the media type (`FMTTYPE`), or `null`.
 * @property label a human-readable label (`LABEL`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Link")))
@interface CalendrumCoreLink : CalendrumCoreBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumCoreValueType *)valueType linkRel:(NSString *)linkRel __attribute__((swift_name("init(value:valueType:linkRel:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Link equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSString * _Nullable linkRel __attribute__((swift_name("linkRel")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumCoreValueType *valueType __attribute__((swift_name("valueType")));
@end


/**
 * The value of a `LOCATION` property: the place as text
 * ([RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)).
 *
 * Build one with `Location("Room 101")`. Immutable.
 *
 * @property text the location text, unescaped.
 * @property altRep the `ALTREP` parameter, a URI with a richer description of the place, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones, so that writing keeps them.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Location")))
@interface CalendrumCoreLocation : CalendrumCoreBase
- (instancetype)initWithText:(NSString *)text __attribute__((swift_name("init(text:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Location equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *text __attribute__((swift_name("text")));
@end


/**
 * The iTIP method of a scheduling message ([RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2),
 * `METHOD`, with the semantics of [RFC 5546](https://www.rfc-editor.org/rfc/rfc5546)).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Method")))
@interface CalendrumCoreMethod : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreMethodCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Method with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Method and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Method.Companion")))
@interface CalendrumCoreMethodCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Method and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreMethodCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Method holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreMethod *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ADD`. The organizer adds instances to an existing series. */
@property (readonly) CalendrumCoreMethod *Add __attribute__((swift_name("Add")));

/** `CANCEL`. The organizer cancels a component or some of its instances. */
@property (readonly) CalendrumCoreMethod *Cancel __attribute__((swift_name("Cancel")));

/** `COUNTER`. An attendee proposes a change. */
@property (readonly) CalendrumCoreMethod *Counter __attribute__((swift_name("Counter")));

/** `DECLINECOUNTER`. The organizer declines a proposed change. */
@property (readonly) CalendrumCoreMethod *DeclineCounter __attribute__((swift_name("DeclineCounter")));

/** `PUBLISH`. Publishes a component to anyone, without expecting an answer. */
@property (readonly) CalendrumCoreMethod *Publish __attribute__((swift_name("Publish")));

/** `REFRESH`. An attendee asks for the latest version. */
@property (readonly) CalendrumCoreMethod *Refresh __attribute__((swift_name("Refresh")));

/** `REPLY`. An attendee answers a request. */
@property (readonly) CalendrumCoreMethod *Reply __attribute__((swift_name("Reply")));

/** `REQUEST`. Asks for attendance, or updates a component sent before. */
@property (readonly) CalendrumCoreMethod *Request __attribute__((swift_name("Request")));
@end


/**
 * The value of an `ORGANIZER` property: the calendar user who organizes the event
 * ([RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)).
 *
 * [parameters] keeps all parameters, unknown ones included. Build one with [builder], change one with [toBuilder].
 * Immutable.
 *
 * @property address the calendar address, usually `mailto:alice@example.com`.
 * @property parameters all parameters as written.
 * @property cn the common name (`CN`), or `null`.
 * @property sentBy the address that acts on behalf of the organizer (`SENT-BY`), or `null`.
 * @property dir the directory entry URI (`DIR`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property email the e-mail address of RFC 7986 (`EMAIL`), or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Organizer")))
@interface CalendrumCoreOrganizer : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreOrganizerCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Organizer equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this organizer, parameters included. */
- (CalendrumCoreOrganizerBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable sentBy __attribute__((swift_name("sentBy")));
@end


/**
 * Collects the address and the parameters of an [Organizer]. The properties and the functions of the same name do
 * the same; the functions chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Organizer.Builder")))
@interface CalendrumCoreOrganizerBuilder : CalendrumCoreBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumCoreOrganizerBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the organizer.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumCoreOrganizer *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreOrganizerBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreOrganizerBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreOrganizerBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreOrganizerBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumCoreOrganizerBuilder *)parameterName:(NSString *)name values:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Removes the parameter [name]. */
- (CalendrumCoreOrganizerBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumCoreOrganizerBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The calendar address. */
@property NSString *address __attribute__((swift_name("address")));

/** The common name (`CN`); assigning `null` removes the parameter. */
@property NSString * _Nullable cn __attribute__((swift_name("cn")));

/** The directory entry URI (`DIR`); assigning `null` removes the parameter. */
@property NSString * _Nullable dir __attribute__((swift_name("dir")));

/** The `EMAIL` address of RFC 7986; assigning `null` removes the parameter. */
@property NSString * _Nullable email __attribute__((swift_name("email")));

/** The `LANGUAGE`; assigning `null` removes the parameter. */
@property NSString * _Nullable language __attribute__((swift_name("language")));

/** The address that acts on behalf of the user (`SENT-BY`); assigning `null` removes the parameter. */
@property NSString * _Nullable sentBy __attribute__((swift_name("sentBy")));
@end


/** Creates builders for [Organizer]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Organizer.Companion")))
@interface CalendrumCoreOrganizerCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Organizer]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreOrganizerCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an organizer with the given calendar [address]. */
- (CalendrumCoreOrganizerBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
@end


/**
 * Reads a parameter into a value of type [T] and writes a value back; the plug-in point for your own parameters.
 *
 * Implementations must be stateless and thread-safe.
 *
 * @note annotations
 *   kotlin.SubclassOptInRequired(markerClass=[NormalClass(value=io/github/calendrum/ExperimentalICalApi)])
*/
__attribute__((swift_name("ParameterCodec")))
@protocol CalendrumCoreParameterCodec
@required

/**
 * Decodes [parameter] of [property], with problems and strictness as [context] says.
 *
 * @param property the property the parameter belongs to.
 * @param parameter the parameter to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumCoreDecoded>)decodeProperty:(CalendrumCoreICalProperty *)property parameter:(CalendrumCoreICalParameter *)parameter context:(CalendrumCoreDecodeContext *)context __attribute__((swift_name("decode(property:parameter:context:)")));

/**
 * Encodes [value] into a parameter named by [context].
 *
 * @param value the value to write.
 * @param context carries the parameter name.
 */
- (CalendrumCoreICalParameter *)encodeParameterValue:(id)value context:(CalendrumCoreEncodeContext *)context __attribute__((swift_name("encodeParameter(value:context:)")));
@end


/**
 * What the registry knows about a parameter. Immutable.
 *
 * @property key the typed key that decodes the parameter.
 * @property source the standard or vendor that defines it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParameterDefinition")))
@interface CalendrumCoreParameterDefinition : CalendrumCoreBase
- (instancetype)initWithKey:(CalendrumCoreParameterKey<id> *)key source:(NSString *)source __attribute__((swift_name("init(key:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreParameterKey<id> *key __attribute__((swift_name("key")));

/** The canonical name of the parameter. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@end


/**
 * Names a parameter and knows how to read and write its value as a [T]: the typed handle used as
 * `property[Parameters.PARTSTAT]`.
 *
 * Keys for the standard parameters are in [Parameters]. Immutable.
 *
 * @property codec decodes the parameter and encodes a value into a parameter.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParameterKey")))
@interface CalendrumCoreParameterKey<T> : CalendrumCoreBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumCoreParameterCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumCoreParameterCodec> codec __attribute__((swift_name("codec")));

/** The canonical parameter name in upper case. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/**
 * Typed keys for the standard parameters, to read with `property[Parameters.PARTSTAT]`.
 *
 * The keys are created on first use and shared. For an unknown parameter use `property.parameter(name)` of the syntax
 * layer.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Parameters")))
@interface CalendrumCoreParameters : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Typed keys for the standard parameters, to read with `property[Parameters.PARTSTAT]`.
 *
 * The keys are created on first use and shared. For an unknown parameter use `property.parameter(name)` of the syntax
 * layer.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)parameters __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreParameters *shared __attribute__((swift_name("shared")));

/**
 * The key of the `ALTREP` parameter ([RFC 5545 §3.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.1)): the
 * alternate text representation, a URI such as an HTML version of a text, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *ALTREP __attribute__((swift_name("ALTREP")));

/**
 * The key of the `CN` parameter ([RFC 5545 §3.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.2)): the
 * common (display) name of a calendar user, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *CN __attribute__((swift_name("CN")));

/**
 * The key of the `CUTYPE` parameter ([RFC 5545 §3.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.3)): the
 * type of a calendar user: individual, group, resource, room or unknown, read as a [CuType].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreCuType *> *CUTYPE __attribute__((swift_name("CUTYPE")));

/**
 * The key of the `DELEGATED-FROM` parameter
 * ([RFC 5545 §3.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.4)): the calendar addresses that delegated
 * their attendance to this user, read as a list of texts.
 */
@property (readonly) CalendrumCoreParameterKey<NSArray<NSString *> *> *DELEGATED_FROM __attribute__((swift_name("DELEGATED_FROM")));

/**
 * The key of the `DELEGATED-TO` parameter
 * ([RFC 5545 §3.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.5)): the calendar addresses this user
 * delegated their attendance to, read as a list of texts.
 */
@property (readonly) CalendrumCoreParameterKey<NSArray<NSString *> *> *DELEGATED_TO __attribute__((swift_name("DELEGATED_TO")));

/**
 * The key of the `DERIVED` parameter ([RFC 9073 §5.3](https://www.rfc-editor.org/rfc/rfc9073#section-5.3)): whether
 * a value is derived from other data and not authoritative, read as a flag.
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreBoolean *> *DERIVED __attribute__((swift_name("DERIVED")));

/**
 * The key of the `DIR` parameter ([RFC 5545 §3.2.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.6)): a URI
 * of a directory entry about the calendar user, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *DIR __attribute__((swift_name("DIR")));

/**
 * The key of the `DISPLAY` parameter ([RFC 7986 §6.1](https://www.rfc-editor.org/rfc/rfc7986#section-6.1)): how an
 * image is meant to be shown, read as a [List<ImageDisplay>].
 */
@property (readonly) CalendrumCoreParameterKey<NSArray<CalendrumCoreImageDisplay *> *> *DISPLAY __attribute__((swift_name("DISPLAY")));

/**
 * The key of the `EMAIL` parameter ([RFC 7986 §6.2](https://www.rfc-editor.org/rfc/rfc7986#section-6.2)): an e-mail
 * address of the calendar user, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *EMAIL __attribute__((swift_name("EMAIL")));

/**
 * The key of the `ENCODING` parameter ([RFC 5545 §3.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.7)):
 * how an inline value is encoded (`8BIT` or `BASE64`), read as a [Encoding].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreEncoding *> *ENCODING __attribute__((swift_name("ENCODING")));

/**
 * The key of the `FBTYPE` parameter ([RFC 5545 §3.2.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.9)): the
 * free/busy type of a `FREEBUSY` value, read as a [FbType].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreFbType *> *FBTYPE __attribute__((swift_name("FBTYPE")));

/**
 * The key of the `FEATURE` parameter ([RFC 7986 §6.3](https://www.rfc-editor.org/rfc/rfc7986#section-6.3)): the
 * features of a conference, read as a [List<ConferenceFeature>].
 */
@property (readonly) CalendrumCoreParameterKey<NSArray<CalendrumCoreConferenceFeature *> *> *FEATURE __attribute__((swift_name("FEATURE")));

/**
 * The key of the `FMTTYPE` parameter ([RFC 5545 §3.2.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.8)): the
 * media type of an attachment or image, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *FMTTYPE __attribute__((swift_name("FMTTYPE")));

/**
 * The key of the `GAP` parameter ([RFC 9253 §6.2](https://www.rfc-editor.org/rfc/rfc9253#section-6.2)): the gap
 * between a related component's end and this one's start, read as a [ICalDuration].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreICalDuration *> *GAP __attribute__((swift_name("GAP")));

/**
 * The key of the `LABEL` parameter ([RFC 7986 §6.4](https://www.rfc-editor.org/rfc/rfc7986#section-6.4)): a
 * human-readable label for a URI value, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *LABEL __attribute__((swift_name("LABEL")));

/**
 * The key of the `LANGUAGE` parameter ([RFC 5545 §3.2.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.10)):
 * the language of a text value, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *LANGUAGE __attribute__((swift_name("LANGUAGE")));

/**
 * The key of the `LINKREL` parameter ([RFC 9253 §6.1](https://www.rfc-editor.org/rfc/rfc9253#section-6.1)): the
 * relation type of a link, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *LINKREL __attribute__((swift_name("LINKREL")));

/**
 * The key of the `MEMBER` parameter ([RFC 5545 §3.2.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.11)):
 * the groups the calendar user belongs to, read as a list of texts.
 */
@property (readonly) CalendrumCoreParameterKey<NSArray<NSString *> *> *MEMBER __attribute__((swift_name("MEMBER")));

/**
 * The key of the `ORDER` parameter ([RFC 9073 §5.1](https://www.rfc-editor.org/rfc/rfc9073#section-5.1)): the order
 * of an item among several of the same property, read as an integer.
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreInt *> *ORDER __attribute__((swift_name("ORDER")));

/**
 * The key of the `PARTSTAT` parameter ([RFC 5545 §3.2.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.12)):
 * the participation status of a calendar user, read as a [PartStat].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCorePartStat *> *PARTSTAT __attribute__((swift_name("PARTSTAT")));

/**
 * The key of the `RANGE` parameter ([RFC 5545 §3.2.13](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.13)): the
 * range of instances a recurrence override applies to, read as a [RecurrenceRange].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreRecurrenceRange *> *RANGE __attribute__((swift_name("RANGE")));

/**
 * The key of the `RELATED` parameter ([RFC 5545 §3.2.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.14)):
 * which end of the parent component a relative trigger is measured from, read as a [Related].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreRelated *> *RELATED __attribute__((swift_name("RELATED")));

/**
 * The key of the `RELTYPE` parameter ([RFC 5545 §3.2.15](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.15)):
 * the type of a relationship, read as a [RelType].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreRelType *> *RELTYPE __attribute__((swift_name("RELTYPE")));

/**
 * The key of the `ROLE` parameter ([RFC 5545 §3.2.16](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.16)): the
 * role of a calendar user in the event, read as a [Role].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreRole *> *ROLE __attribute__((swift_name("ROLE")));

/**
 * The key of the `RSVP` parameter ([RFC 5545 §3.2.17](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.17)):
 * whether a reply is requested, read as a flag.
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreBoolean *> *RSVP __attribute__((swift_name("RSVP")));

/**
 * The key of the `SCHEMA` parameter ([RFC 9073 §5.2](https://www.rfc-editor.org/rfc/rfc9073#section-5.2)): the
 * schema URI of structured data, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *SCHEMA __attribute__((swift_name("SCHEMA")));

/**
 * The key of the `SENT-BY` parameter ([RFC 5545 §3.2.18](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.18)):
 * the calendar user who acts on behalf of the user, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *SENT_BY __attribute__((swift_name("SENT_BY")));

/**
 * The key of the `TZID` parameter ([RFC 5545 §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)): the
 * time zone identifier of a local date-time, read as text.
 */
@property (readonly) CalendrumCoreParameterKey<NSString *> *TZID __attribute__((swift_name("TZID")));

/**
 * The key of the `VALUE` parameter ([RFC 5545 §3.2.20](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.20)): the
 * value type of a property, when it is not the default, read as a [ValueType].
 */
@property (readonly) CalendrumCoreParameterKey<CalendrumCoreValueType *> *VALUE __attribute__((swift_name("VALUE")));
@end


/**
 * The participation status of a calendar user
 * ([RFC 5545 §3.2.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.12), `PARTSTAT`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PartStat")))
@interface CalendrumCorePartStat : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCorePartStatCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a PartStat with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of PartStat and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PartStat.Companion")))
@interface CalendrumCorePartStatCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of PartStat and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCorePartStatCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new PartStat holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCorePartStat *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACCEPTED`. The user will attend. */
@property (readonly) CalendrumCorePartStat *Accepted __attribute__((swift_name("Accepted")));

/** `COMPLETED`. The user completed the task (to-dos). */
@property (readonly) CalendrumCorePartStat *Completed __attribute__((swift_name("Completed")));

/** `DECLINED`. The user will not attend. */
@property (readonly) CalendrumCorePartStat *Declined __attribute__((swift_name("Declined")));

/** `DELEGATED`. The user delegated attendance to someone else. */
@property (readonly) CalendrumCorePartStat *Delegated __attribute__((swift_name("Delegated")));

/** `IN-PROCESS`. The user is working on the task (to-dos). */
@property (readonly) CalendrumCorePartStat *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The user has not answered yet; the default. */
@property (readonly) CalendrumCorePartStat *NeedsAction __attribute__((swift_name("NeedsAction")));

/** `TENTATIVE`. The user may attend. */
@property (readonly) CalendrumCorePartStat *Tentative __attribute__((swift_name("Tentative")));
@end


/**
 * A `PARTICIPANT` component: a person, group or resource taking part in an event or to-do, with its own properties
 * ([RFC 9073 §7.1](https://www.rfc-editor.org/rfc/rfc9073#section-7.1)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Participant")))
@interface CalendrumCoreParticipant : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreParticipantCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this participant component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this participant component.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreParticipant *)editBlock:(void (^)(CalendrumCoreParticipantBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a Participant with an equal PARTICIPANT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this participant component; unchanged nodes are shared with it. */
- (CalendrumCoreParticipantBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this participant component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreParticipantBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the participant component (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The calendar address of the participant component (`CALENDAR-ADDRESS`,
 * [RFC 9073 §6.4](https://www.rfc-editor.org/rfc/rfc9073#section-6.4)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) NSString * _Nullable calendarAddress __attribute__((swift_name("calendarAddress")));

/**
 * The categories of the participant component (`CATEGORIES` properties,
 * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *categories __attribute__((swift_name("categories")));

/**
 * The comments of the participant component (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the participant component (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The contacts of the participant component (`CONTACT` properties,
 * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *contacts __attribute__((swift_name("contacts")));

/**
 * The creation time of the participant component (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The description of the participant component (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The geographic position of the participant component (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the participant component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/**
 * The location texts of the participant component (`LOCATION` properties,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *locationTexts __attribute__((swift_name("locationTexts")));

/** The locations nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumCoreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The reference identifiers of the participant component (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the participant component (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the participant component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The resources of the participant component (`RESOURCES` properties,
 * [RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)) from all properties in source
 * order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *resources __attribute__((swift_name("resources")));

/**
 * The revision sequence number of the participant component (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the participant component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the participant component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

/**
 * The summary, the short title of the participant component (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The globally unique identifier of the participant component (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the participant component (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable url __attribute__((swift_name("url")));
@end


/**
 * Collects the properties and nested components of a new or edited participant component, with a Kotlin DSL and
 * chained calls. Get one from [Participant.builder] or [Participant.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Participant.Builder")))
@interface CalendrumCoreParticipantBuilder : CalendrumCoreComponentBuilder<CalendrumCoreParticipantBuilder *>

/** Adds [value] as an `ATTACH` property. */
- (CalendrumCoreParticipantBuilder *)attachValue:(id<CalendrumCoreAttach>)value __attribute__((swift_name("attach(value:)")));

/**
 * Creates the participant.
 *
 * @throws IllegalStateException if `UID` or `PARTICIPANT-TYPE` is missing and the builder has no defaults for
 *   the `UID`.
 */
- (CalendrumCoreParticipant *)build __attribute__((swift_name("build()")));

/**
 * Sets [calendarAddress] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)calendarAddressValue:(NSString * _Nullable)value __attribute__((swift_name("calendarAddress(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumCoreParticipantBuilder *)categoriesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumCoreParticipantBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreParticipantBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumCoreParticipantBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)createdValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)geoValue:(CalendrumCoreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)lastModifiedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreParticipantBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the participant component. */
- (CalendrumCoreParticipantBuilder *)locationValue:(CalendrumCoreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumCoreParticipantBuilder *)locationBlock:(void (^)(CalendrumCoreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Adds a `LOCATION` property with the text [text]. */
- (CalendrumCoreParticipantBuilder *)locationTextText:(NSString *)text __attribute__((swift_name("locationText(text:)")));

/** Edits the locations of the participant component in [block]: add, replace or remove them. */
- (CalendrumCoreParticipantBuilder *)locationsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [participantType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)participantTypeValue:(CalendrumCoreParticipantType * _Nullable)value __attribute__((swift_name("participantType(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)priorityValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreParticipantBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreParticipantBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreParticipantBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `REQUEST-STATUS` property. */
- (CalendrumCoreParticipantBuilder *)requestStatusValue:(CalendrumCoreRequestStatus *)value __attribute__((swift_name("requestStatus(value:)")));

/** Adds [value] as a resource of the participant component. */
- (CalendrumCoreParticipantBuilder *)resourceComponentValue:(CalendrumCoreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumCoreParticipantBuilder *)resourceComponentBlock:(void (^)(CalendrumCoreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the participant component in [block]: add, replace or remove them. */
- (CalendrumCoreParticipantBuilder *)resourceComponentsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumCoreParticipantBuilder *)resourcesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)sequenceValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)stampValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)statusValue:(CalendrumCoreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumCoreParticipantBuilder *)structuredDataValue:(CalendrumCoreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumCoreParticipantBuilder *)styledDescriptionValue:(CalendrumCoreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreParticipantBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The calendar address of the participant component being built (`CALENDAR-ADDRESS`,
 * [RFC 9073 §6.4](https://www.rfc-editor.org/rfc/rfc9073#section-6.4)); assigning `null` removes the property.
 */
@property NSString * _Nullable calendarAddress __attribute__((swift_name("calendarAddress")));

/**
 * The creation time of the participant component being built (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The description of the participant component being built (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The geographic position of the participant component being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the participant component being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component being built (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)); assigning `null` removes the property.
 */
@property CalendrumCoreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable priority __attribute__((swift_name("priority")));

/** The resources of the participant component being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The revision sequence number of the participant component being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The summary, the short title of the participant component being built (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The globally unique identifier of the participant component being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the participant component being built (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable url __attribute__((swift_name("url")));
@end


/** Creates Participant values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Participant.Companion")))
@interface CalendrumCoreParticipantCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates Participant values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreParticipantCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. A `UID` and a participant type are required. */
- (CalendrumCoreParticipantBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreParticipantBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a Participant, or returns `null` when it is not a `PARTICIPANT` component. Nothing is
 * copied or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreParticipant * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * The type of a participant ([RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2), `PARTICIPANT-TYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParticipantType")))
@interface CalendrumCoreParticipantType : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreParticipantTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ParticipantType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of ParticipantType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParticipantType.Companion")))
@interface CalendrumCoreParticipantTypeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ParticipantType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreParticipantTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ParticipantType holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreParticipantType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACTIVE`. An active participant. */
@property (readonly) CalendrumCoreParticipantType *Active __attribute__((swift_name("Active")));

/** `BOOKING-CONTACT`. A contact for bookings or reservations. */
@property (readonly) CalendrumCoreParticipantType *BookingContact __attribute__((swift_name("BookingContact")));

/** `CONTACT`. A contact for information. */
@property (readonly) CalendrumCoreParticipantType *Contact __attribute__((swift_name("Contact")));

/** `EMERGENCY-CONTACT`. A contact in an emergency. */
@property (readonly) CalendrumCoreParticipantType *EmergencyContact __attribute__((swift_name("EmergencyContact")));

/** `INACTIVE`. An inactive participant. */
@property (readonly) CalendrumCoreParticipantType *Inactive __attribute__((swift_name("Inactive")));

/** `PERFORMER`. A performer, such as a musician. */
@property (readonly) CalendrumCoreParticipantType *Performer __attribute__((swift_name("Performer")));

/** `PLANNER-CONTACT`. A contact for the planning of the activity. */
@property (readonly) CalendrumCoreParticipantType *PlannerContact __attribute__((swift_name("PlannerContact")));

/** `PUBLICITY-CONTACT`. A contact for publicity. */
@property (readonly) CalendrumCoreParticipantType *PublicityContact __attribute__((swift_name("PublicityContact")));

/** `SPEAKER`. A speaker. */
@property (readonly) CalendrumCoreParticipantType *Speaker __attribute__((swift_name("Speaker")));

/** `SPONSOR`. A sponsor of the activity. */
@property (readonly) CalendrumCoreParticipantType *Sponsor __attribute__((swift_name("Sponsor")));
@end


/**
 * One line that a patch changed.
 *
 * @property kind what happened to the line.
 * @property property the name of the property on the line; never its value.
 * @property offset the offset in bytes of the line in the original.
 * @property line the 1-based line number in the original, or in the new file for an inserted line.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PatchChange")))
@interface CalendrumCorePatchChange : CalendrumCoreBase

/** Returns `true` when [other] has the same kind, property, offset and line. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over all fields. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the kind, the property name and the line; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCorePatchChangeKind *kind __attribute__((swift_name("kind")));
@property (readonly) int32_t line __attribute__((swift_name("line")));
@property (readonly) int64_t offset __attribute__((swift_name("offset")));
@property (readonly) NSString *property __attribute__((swift_name("property")));
@end


/** What a patch did to a line. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PatchChange.Kind")))
@interface CalendrumCorePatchChangeKind : CalendrumCoreKotlinEnum<CalendrumCorePatchChangeKind *>
+ (instancetype)alloc __attribute__((unavailable));

/** What a patch did to a line. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCorePatchChangeKind *replaced __attribute__((swift_name("replaced")));
@property (class, readonly) CalendrumCorePatchChangeKind *inserted __attribute__((swift_name("inserted")));
@property (class, readonly) CalendrumCorePatchChangeKind *removed __attribute__((swift_name("removed")));
+ (CalendrumCoreKotlinArray<CalendrumCorePatchChangeKind *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCorePatchChangeKind *> *entries __attribute__((swift_name("entries")));
@end


/**
 * The outcome of applying an [ICalPatch].
 *
 * @property bytes the patched file; the original array when nothing changed.
 * @property unmatched the selectors that found no component, property or attendee.
 * @property changes the lines that changed, in file order; they never contain values.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PatchResult")))
@interface CalendrumCorePatchResult : CalendrumCoreBase

/** Returns the size of the result and the numbers of unmatched selectors and changes. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinByteArray *bytes __attribute__((swift_name("bytes")));
@property (readonly) NSArray<CalendrumCorePatchChange *> *changes __attribute__((swift_name("changes")));
@property (readonly) NSArray<CalendrumCorePatchSelector *> *unmatched __attribute__((swift_name("unmatched")));
@end


/**
 * Tells which component, property or attendee a patch operation could not find.
 *
 * @property uid the `UID` of the component.
 * @property recurrenceId the `RECURRENCE-ID` of the component, or `null`.
 * @property property the property name, or `null` when the component itself was not found.
 * @property address the attendee address, or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PatchSelector")))
@interface CalendrumCorePatchSelector : CalendrumCoreBase

/** Returns `true` when [other] has the same uid, recurrence id, property and address. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over all fields. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the property name only, never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable property __attribute__((swift_name("property")));
@property (readonly) NSString * _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));
@property (readonly) NSString *uid __attribute__((swift_name("uid")));
@end


/**
 * Typed keys for the standard properties, to read with `component[Properties.DTSTART]`, write with a builder, or use in
 * a schema.
 *
 * The keys are created on first use and shared; there is no registration. For an `X-` property make your own
 * [PropertyKey]. Three keys read `STATUS`, one for each component that has it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Properties")))
@interface CalendrumCoreProperties : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Typed keys for the standard properties, to read with `component[Properties.DTSTART]`, write with a builder, or use in
 * a schema.
 *
 * The keys are created on first use and shared; there is no registration. For an `X-` property make your own
 * [PropertyKey]. Three keys read `STATUS`, one for each component that has it.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)properties __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreProperties *shared __attribute__((swift_name("shared")));

/**
 * The key of the `ACKNOWLEDGED` property ([RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)),
 * read as a UTC date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *ACKNOWLEDGED __attribute__((swift_name("ACKNOWLEDGED")));

/**
 * The key of the `ACTION` property ([RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)),
 * read as a [AlarmAction].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreAlarmAction *> *ACTION __attribute__((swift_name("ACTION")));

/**
 * The key of the `ATTACH` property ([RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)),
 * read as a [Attach].
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreAttach>> *ATTACH __attribute__((swift_name("ATTACH")));

/**
 * The key of the `ATTENDEE` property ([RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)),
 * read as a [Attendee].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreAttendee *> *ATTENDEE __attribute__((swift_name("ATTENDEE")));

/**
 * The key of the `CALENDAR-ADDRESS` property ([RFC 9073 §6.4](https://www.rfc-editor.org/rfc/rfc9073#section-6.4)),
 * read as a calendar address.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *CALENDAR_ADDRESS __attribute__((swift_name("CALENDAR_ADDRESS")));

/**
 * The key of the `CALSCALE` property ([RFC 5545 §3.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.1)),
 * read as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *CALSCALE __attribute__((swift_name("CALSCALE")));

/**
 * The key of the `CATEGORIES` property
 * ([RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)), read as a [Categories].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreCategories *> *CATEGORIES __attribute__((swift_name("CATEGORIES")));

/**
 * The key of the `CLASS` property ([RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)),
 * read as a [Classification].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreClassification *> *CLASS __attribute__((swift_name("CLASS")));

/**
 * The key of the `COLOR` property ([RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), read as
 * text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *COLOR __attribute__((swift_name("COLOR")));

/**
 * The key of the `COMMENT` property ([RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)),
 * read as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *COMMENT __attribute__((swift_name("COMMENT")));

/**
 * The key of the `COMPLETED` property
 * ([RFC 5545 §3.8.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.1)), read as a UTC date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *COMPLETED __attribute__((swift_name("COMPLETED")));

/**
 * The key of the `CONCEPT` property ([RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)), read as
 * a URI.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *CONCEPT __attribute__((swift_name("CONCEPT")));

/**
 * The key of the `CONFERENCE` property ([RFC 7986 §5.11](https://www.rfc-editor.org/rfc/rfc7986#section-5.11)),
 * read as a [Conference].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreConference *> *CONFERENCE __attribute__((swift_name("CONFERENCE")));

/**
 * The key of the `CONTACT` property ([RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)),
 * read as a [Contact].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreContact *> *CONTACT __attribute__((swift_name("CONTACT")));

/**
 * The key of the `CREATED` property ([RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)),
 * read as a UTC date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *CREATED __attribute__((swift_name("CREATED")));

/**
 * The key of the `DESCRIPTION` property
 * ([RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), read as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *DESCRIPTION __attribute__((swift_name("DESCRIPTION")));

/**
 * The key of the `DTEND` property ([RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)),
 * read as a date or date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *DTEND __attribute__((swift_name("DTEND")));

/**
 * The key of the `DTSTAMP` property ([RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)),
 * read as a UTC date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *DTSTAMP __attribute__((swift_name("DTSTAMP")));

/**
 * The key of the `DTSTART` property ([RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * read as a date or date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *DTSTART __attribute__((swift_name("DTSTART")));

/**
 * The key of the `DUE` property ([RFC 5545 §3.8.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.3)), read
 * as a date or date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *DUE __attribute__((swift_name("DUE")));

/**
 * The key of the `DURATION` property ([RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)),
 * read as a [ICalDuration].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreICalDuration *> *DURATION __attribute__((swift_name("DURATION")));

/**
 * The key of the `STATUS` property of an event
 * ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), read as a [EventStatus]; the
 * property has a different set of values in each component.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreEventStatus *> *EVENT_STATUS __attribute__((swift_name("EVENT_STATUS")));

/**
 * The key of the `EXDATE` property ([RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)),
 * read as a [ExDate].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreExDate *> *EXDATE __attribute__((swift_name("EXDATE")));

/**
 * The key of the `FREEBUSY` property ([RFC 5545 §3.8.2.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.6)),
 * read as a [FreeBusy].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreFreeBusy *> *FREEBUSY __attribute__((swift_name("FREEBUSY")));

/**
 * The key of the `GEO` property ([RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), read
 * as a [Geo].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreGeo *> *GEO __attribute__((swift_name("GEO")));

/**
 * The key of the `IMAGE` property ([RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)), read as
 * a [Image].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreImage *> *IMAGE __attribute__((swift_name("IMAGE")));

/**
 * The key of the `STATUS` property of a journal entry
 * ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), read as a [JournalStatus]; the
 * property has a different set of values in each component.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreJournalStatus *> *JOURNAL_STATUS __attribute__((swift_name("JOURNAL_STATUS")));

/**
 * The key of the `LAST-MODIFIED` property
 * ([RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), read as a UTC date-time.
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *LAST_MODIFIED __attribute__((swift_name("LAST_MODIFIED")));

/**
 * The key of the `LINK` property ([RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)), read as a
 * [Link].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreLink *> *LINK __attribute__((swift_name("LINK")));

/**
 * The key of the `LOCATION` property ([RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)),
 * read as a [Location].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreLocation *> *LOCATION __attribute__((swift_name("LOCATION")));

/**
 * The key of the `LOCATION-TYPE` property ([RFC 9073 §6.1](https://www.rfc-editor.org/rfc/rfc9073#section-6.1)),
 * read as a list of texts.
 */
@property (readonly) CalendrumCorePropertyKey<NSArray<NSString *> *> *LOCATION_TYPE __attribute__((swift_name("LOCATION_TYPE")));

/**
 * The key of the `METHOD` property ([RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)), read
 * as a [Method].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreMethod *> *METHOD __attribute__((swift_name("METHOD")));

/**
 * The key of the `NAME` property ([RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)), read as
 * text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *NAME __attribute__((swift_name("NAME")));

/**
 * The key of the `ORGANIZER` property
 * ([RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), read as a [Organizer].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreOrganizer *> *ORGANIZER __attribute__((swift_name("ORGANIZER")));

/**
 * The key of the `PARTICIPANT-TYPE` property ([RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)),
 * read as a [ParticipantType].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreParticipantType *> *PARTICIPANT_TYPE __attribute__((swift_name("PARTICIPANT_TYPE")));

/**
 * The key of the `PERCENT-COMPLETE` property
 * ([RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)), read as an integer.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreInt *> *PERCENT_COMPLETE __attribute__((swift_name("PERCENT_COMPLETE")));

/**
 * The key of the `PRIORITY` property ([RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)),
 * read as an integer.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreInt *> *PRIORITY __attribute__((swift_name("PRIORITY")));

/**
 * The key of the `PRODID` property ([RFC 5545 §3.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.3)), read
 * as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *PRODID __attribute__((swift_name("PRODID")));

/**
 * The key of the `PROXIMITY` property ([RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)), read
 * as a [Proximity].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreProximity *> *PROXIMITY __attribute__((swift_name("PROXIMITY")));

/**
 * The key of the `RDATE` property ([RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)),
 * read as a [RDate].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreRDate *> *RDATE __attribute__((swift_name("RDATE")));

/**
 * The key of the `RECURRENCE-ID` property
 * ([RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), read as a [RecurrenceId].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreRecurrenceId *> *RECURRENCE_ID __attribute__((swift_name("RECURRENCE_ID")));

/**
 * The key of the `REFID` property ([RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)), read as
 * text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *REFID __attribute__((swift_name("REFID")));

/**
 * The key of the `REFRESH-INTERVAL` property ([RFC 7986 §5.7](https://www.rfc-editor.org/rfc/rfc7986#section-5.7)),
 * read as a [ICalDuration].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreICalDuration *> *REFRESH_INTERVAL __attribute__((swift_name("REFRESH_INTERVAL")));

/**
 * The key of the `RELATED-TO` property
 * ([RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)), read as a [RelatedTo].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreRelatedTo *> *RELATED_TO __attribute__((swift_name("RELATED_TO")));

/**
 * The key of the `REPEAT` property ([RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)),
 * read as an integer.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreInt *> *REPEAT __attribute__((swift_name("REPEAT")));

/**
 * The key of the `REQUEST-STATUS` property
 * ([RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)), read as a [RequestStatus].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreRequestStatus *> *REQUEST_STATUS __attribute__((swift_name("REQUEST_STATUS")));

/**
 * The key of the `RESOURCES` property
 * ([RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)), read as a [Resources].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreResources *> *RESOURCES __attribute__((swift_name("RESOURCES")));

/**
 * The key of the `RESOURCE-TYPE` property ([RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)),
 * read as a [ResourceType].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreResourceType *> *RESOURCE_TYPE __attribute__((swift_name("RESOURCE_TYPE")));

/**
 * The key of the `RRULE` property ([RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)),
 * read as a [Recur].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreRecur *> *RRULE __attribute__((swift_name("RRULE")));

/**
 * The key of the `SEQUENCE` property ([RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)),
 * read as an integer.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreInt *> *SEQUENCE __attribute__((swift_name("SEQUENCE")));

/**
 * The key of the `SOURCE` property ([RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)), read as a
 * URI.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *SOURCE __attribute__((swift_name("SOURCE")));

/**
 * The key of the `STRUCTURED-DATA` property ([RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)),
 * read as a [StructuredData].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreStructuredData *> *STRUCTURED_DATA __attribute__((swift_name("STRUCTURED_DATA")));

/**
 * The key of the `STYLED-DESCRIPTION` property
 * ([RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)), read as a [StyledDescription].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreStyledDescription *> *STYLED_DESCRIPTION __attribute__((swift_name("STYLED_DESCRIPTION")));

/**
 * The key of the `SUMMARY` property
 * ([RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), read as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *SUMMARY __attribute__((swift_name("SUMMARY")));

/**
 * The key of the `STATUS` property of a to-do
 * ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), read as a [TodoStatus]; the
 * property has a different set of values in each component.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreTodoStatus *> *TODO_STATUS __attribute__((swift_name("TODO_STATUS")));

/**
 * The key of the `TRANSP` property ([RFC 5545 §3.8.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.7)),
 * read as a [Transparency].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreTransparency *> *TRANSP __attribute__((swift_name("TRANSP")));

/**
 * The key of the `TRIGGER` property ([RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)),
 * read as a [Trigger].
 */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreTrigger>> *TRIGGER __attribute__((swift_name("TRIGGER")));

/**
 * The key of the `TZID` property ([RFC 5545 §3.8.3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.1)),
 * read as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *TZID __attribute__((swift_name("TZID")));

/**
 * The key of the `TZNAME` property ([RFC 5545 §3.8.3.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.2)),
 * read as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *TZNAME __attribute__((swift_name("TZNAME")));

/**
 * The key of the `TZOFFSETFROM` property
 * ([RFC 5545 §3.8.3.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.3)), read as a [UtcOffset].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreKotlinx_datetimeUtcOffset *> *TZOFFSETFROM __attribute__((swift_name("TZOFFSETFROM")));

/**
 * The key of the `TZOFFSETTO` property
 * ([RFC 5545 §3.8.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.4)), read as a [UtcOffset].
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreKotlinx_datetimeUtcOffset *> *TZOFFSETTO __attribute__((swift_name("TZOFFSETTO")));

/**
 * The key of the `TZURL` property ([RFC 5545 §3.8.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.5)),
 * read as a URI.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *TZURL __attribute__((swift_name("TZURL")));

/**
 * The key of the `UID` property ([RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), read
 * as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *UID __attribute__((swift_name("UID")));

/**
 * The key of the `URL` property ([RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), read
 * as a URI.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *URL __attribute__((swift_name("URL")));

/**
 * The key of the `VERSION` property ([RFC 5545 §3.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.4)), read
 * as text.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *VERSION __attribute__((swift_name("VERSION")));
@end


/**
 * Reads a property into a value of type [T] and writes a value back; the plug-in point for your own properties.
 *
 * Implementations must be stateless and thread-safe, report trouble as [Decoded.Malformed] instead of throwing, and not
 * put values into problems.
 *
 * @note annotations
 *   kotlin.SubclassOptInRequired(markerClass=[NormalClass(value=io/github/calendrum/ExperimentalICalApi)])
*/
__attribute__((swift_name("PropertyCodec")))
@protocol CalendrumCorePropertyCodec
@required

/**
 * Decodes [property], with problems and strictness as [context] says.
 *
 * @param property the property to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumCoreDecoded>)decodeProperty:(CalendrumCoreICalProperty *)property context:(CalendrumCoreDecodeContext *)context __attribute__((swift_name("decode(property:context:)")));

/**
 * Encodes [value] into a property named by [context].
 *
 * @param value the value to write.
 * @param context carries the property name.
 */
- (CalendrumCoreICalProperty *)encodePropertyValue:(id)value context:(CalendrumCoreEncodeContext *)context __attribute__((swift_name("encodeProperty(value:context:)")));
@end


/**
 * What the registry knows about a property: its key, value types, allowed parameters and how often it occurs in each
 * component. Immutable.
 *
 * @property key the typed key that decodes the property.
 * @property defaultType the value type used when no `VALUE` parameter says otherwise.
 * @property source the standard or vendor that defines it, such as `RFC 5545 §3.8.2.4`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PropertyDefinition")))
@interface CalendrumCorePropertyDefinition : CalendrumCoreBase
- (instancetype)initWithKey:(CalendrumCorePropertyKey<id> *)key defaultType:(CalendrumCoreValueType *)defaultType allowedTypes:(NSSet<CalendrumCoreValueType *> *)allowedTypes parameters:(NSSet<NSString *> *)parameters cardinality:(NSDictionary<NSString *, CalendrumCoreCardinality *> *)cardinality source:(NSString *)source __attribute__((swift_name("init(key:defaultType:allowedTypes:parameters:cardinality:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The value types the property may have, including the default one. */
@property (readonly) NSSet<CalendrumCoreValueType *> *allowedTypes __attribute__((swift_name("allowedTypes")));

/** How often the property occurs in each component, by component name. */
@property (readonly) NSDictionary<NSString *, CalendrumCoreCardinality *> *cardinality __attribute__((swift_name("cardinality")));
@property (readonly) CalendrumCoreValueType *defaultType __attribute__((swift_name("defaultType")));
@property (readonly) CalendrumCorePropertyKey<id> *key __attribute__((swift_name("key")));

/** The canonical name of the property. */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The names of the parameters that apply to the property. */
@property (readonly) NSSet<NSString *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@end


/**
 * Names a property and knows how to read and write its value as a [T]: the typed handle used as
 * `component[Properties.DTSTART]`.
 *
 * Keys for the standard properties are in [Properties]; make your own for an `X-` property with a codec, no
 * registration needed. Immutable.
 *
 * @property codec decodes the property and encodes a value into a property.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PropertyKey")))
@interface CalendrumCorePropertyKey<T> : CalendrumCoreBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumCorePropertyCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumCorePropertyCodec> codec __attribute__((swift_name("codec")));

/** The canonical property name in upper case. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/** The changes to the parameters of one property. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PropertyPatch")))
@interface CalendrumCorePropertyPatch : CalendrumCoreBase

/** Removes the parameter of [key]. */
- (CalendrumCorePropertyPatch *)removeKey:(CalendrumCoreParameterKey<id> *)key __attribute__((swift_name("remove(key:)")));

/** Removes the parameter named [name]. */
- (CalendrumCorePropertyPatch *)removeName:(NSString *)name __attribute__((swift_name("remove(name:)")));

/** Sets the parameter of [key] to [value], replacing the parameter of that name. */
- (CalendrumCorePropertyPatch *)setKey:(CalendrumCoreParameterKey<id> *)key value:(id)value __attribute__((swift_name("set(key:value:)")));

/**
 * Sets the parameter [name] to [values], replacing the parameter of that name.
 *
 * @throws IllegalArgumentException if no value is given or the name is invalid.
 */
- (CalendrumCorePropertyPatch *)setName:(NSString *)name values:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("set(name:values:)")));

/** Returns the number of parameter changes. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The proximity condition that fires an alarm ([RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1),
 * `PROXIMITY`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Proximity")))
@interface CalendrumCoreProximity : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreProximityCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Proximity with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Proximity and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Proximity.Companion")))
@interface CalendrumCoreProximityCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Proximity and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreProximityCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Proximity holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreProximity *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ARRIVE`. Fires when the user arrives at the location. */
@property (readonly) CalendrumCoreProximity *Arrive __attribute__((swift_name("Arrive")));

/** `CONNECT`. Fires when the user's device connects, for example to a car. */
@property (readonly) CalendrumCoreProximity *Connect __attribute__((swift_name("Connect")));

/** `DEPART`. Fires when the user leaves the location. */
@property (readonly) CalendrumCoreProximity *Depart __attribute__((swift_name("Depart")));

/** `DISCONNECT`. Fires when the user's device disconnects. */
@property (readonly) CalendrumCoreProximity *Disconnect __attribute__((swift_name("Disconnect")));
@end


/**
 * The value of an `RDATE` property: dates or periods added to a recurrence set
 * ([RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)).
 *
 * A property holds either dates or periods.
 *
 * @property dates the added dates; empty when the value is a list of periods.
 * @property periods the added periods; empty when the value is a list of dates.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RDate")))
@interface CalendrumCoreRDate : CalendrumCoreBase
- (instancetype)initWithDates:(NSArray<id<CalendrumCoreICalDateTime>> *)dates periods:(NSArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("init(dates:periods:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a RDate equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *dates __attribute__((swift_name("dates")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<CalendrumCoreICalPeriod *> *periods __attribute__((swift_name("periods")));
@end


/**
 * The value of a `RECURRENCE-ID` property: which instance of a recurring series a component overrides
 * ([RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)).
 *
 * @property value the start of the instance that is overridden, in the form of the series' `DTSTART`.
 * @property range `THISANDFUTURE` when the override applies to all later instances too, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceId")))
@interface CalendrumCoreRecurrenceId : CalendrumCoreBase
- (instancetype)initWithValue:(id<CalendrumCoreICalDateTime>)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(id<CalendrumCoreICalDateTime>)value range:(CalendrumCoreRecurrenceRange *)range __attribute__((swift_name("init(value:range:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a RecurrenceId equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumCoreRecurrenceRange * _Nullable range __attribute__((swift_name("range")));
@property (readonly) id<CalendrumCoreICalDateTime> value __attribute__((swift_name("value")));
@end


/**
 * The range of instances a recurrence override applies to
 * ([RFC 5545 §3.2.13](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.13), `RANGE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceRange")))
@interface CalendrumCoreRecurrenceRange : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRecurrenceRangeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a RecurrenceRange with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of RecurrenceRange and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceRange.Companion")))
@interface CalendrumCoreRecurrenceRangeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of RecurrenceRange and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRecurrenceRangeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new RecurrenceRange holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreRecurrenceRange *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `THISANDFUTURE`. The override applies to this instance and all later ones. */
@property (readonly) CalendrumCoreRecurrenceRange *ThisAndFuture __attribute__((swift_name("ThisAndFuture")));
@end


/**
 * The type of a relationship between components
 * ([RFC 5545 §3.2.15](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.15), `RELTYPE`, with the values of
 * [RFC 9253](https://www.rfc-editor.org/rfc/rfc9253) and [RFC 9074](https://www.rfc-editor.org/rfc/rfc9074)).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RelType")))
@interface CalendrumCoreRelType : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRelTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a RelType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of RelType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RelType.Companion")))
@interface CalendrumCoreRelTypeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of RelType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRelTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new RelType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreRelType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHILD`. The related component is a child of this one. */
@property (readonly) CalendrumCoreRelType *Child __attribute__((swift_name("Child")));

/** `CONCEPT`. The related value is a `CONCEPT` URI, not a `UID` (RFC 9253). */
@property (readonly) CalendrumCoreRelType *Concept __attribute__((swift_name("Concept")));

/** `DEPENDS-ON`. This component depends on the related one (RFC 9253). */
@property (readonly) CalendrumCoreRelType *DependsOn __attribute__((swift_name("DependsOn")));

/** `FINISHTOFINISH`. A finish-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumCoreRelType *FinishToFinish __attribute__((swift_name("FinishToFinish")));

/** `FINISHTOSTART`. A finish-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumCoreRelType *FinishToStart __attribute__((swift_name("FinishToStart")));

/** `FIRST`. The related component is the first of a series of components (RFC 9253). */
@property (readonly) CalendrumCoreRelType *First __attribute__((swift_name("First")));

/** `NEXT`. The related component is the next of a series of components (RFC 9253). */
@property (readonly) CalendrumCoreRelType *Next __attribute__((swift_name("Next")));

/** `PARENT`. The related component is the parent of this one; the default. */
@property (readonly) CalendrumCoreRelType *Parent __attribute__((swift_name("Parent")));

/** `REFID`. The related value is a `REFID` reference identifier, not a `UID` (RFC 9253). */
@property (readonly) CalendrumCoreRelType *RefId __attribute__((swift_name("RefId")));

/** `SIBLING`. The related component is a sibling of this one. */
@property (readonly) CalendrumCoreRelType *Sibling __attribute__((swift_name("Sibling")));

/** `SNOOZE`. This alarm is a snooze of the related alarm (RFC 9074). */
@property (readonly) CalendrumCoreRelType *Snooze __attribute__((swift_name("Snooze")));

/** `STARTTOFINISH`. A start-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumCoreRelType *StartToFinish __attribute__((swift_name("StartToFinish")));

/** `STARTTOSTART`. A start-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumCoreRelType *StartToStart __attribute__((swift_name("StartToStart")));
@end


/**
 * Which end of the parent component a relative alarm trigger is measured from
 * ([RFC 5545 §3.2.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.14), `RELATED`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Related")))
@interface CalendrumCoreRelated : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRelatedCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Related with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Related and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Related.Companion")))
@interface CalendrumCoreRelatedCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Related and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRelatedCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Related holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreRelated *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `END`. The end (or due time) of the parent. */
@property (readonly) CalendrumCoreRelated *End __attribute__((swift_name("End")));

/** `START`. The start of the parent; the default. */
@property (readonly) CalendrumCoreRelated *Start __attribute__((swift_name("Start")));
@end


/**
 * The value of a `RELATED-TO` property: a relationship to another component, by its `UID`
 * ([RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5), with the relation types of
 * [RFC 9253](https://www.rfc-editor.org/rfc/rfc9253)).
 *
 * Build one with `RelatedTo(uid)` (a parent) or with a [RelType] and, for `DEPENDS-ON`, a gap. Immutable.
 *
 * @property uid the `UID` (or, for `VALUE=URI`, the URI) of the related component.
 * @property relType the relation, [RelType.Parent] by default.
 * @property gap the `GAP` of RFC 9253 between the components, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RelatedTo")))
@interface CalendrumCoreRelatedTo : CalendrumCoreBase
- (instancetype)initWithUid:(NSString *)uid __attribute__((swift_name("init(uid:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumCoreRelType *)relType __attribute__((swift_name("init(uid:relType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumCoreRelType *)relType gap:(CalendrumCoreICalDuration *)gap __attribute__((swift_name("init(uid:relType:gap:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a RelatedTo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreICalDuration * _Nullable gap __attribute__((swift_name("gap")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumCoreRelType *relType __attribute__((swift_name("relType")));
@property (readonly) NSString *uid __attribute__((swift_name("uid")));
@end


/**
 * The value of a `REQUEST-STATUS` property: the status code of a scheduling request, a description and optional data
 * ([RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)), such as `2.0;Success`.
 *
 * @property code the status code: digits separated by dots, such as `2.0`.
 * @property description the human-readable description, unescaped.
 * @property data extra data, such as the offending property, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RequestStatus")))
@interface CalendrumCoreRequestStatus : CalendrumCoreBase
- (instancetype)initWithCode:(NSString *)code description:(NSString *)description __attribute__((swift_name("init(code:description:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCode:(NSString *)code description:(NSString *)description data:(NSString *)data __attribute__((swift_name("init(code:description:data:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a RequestStatus equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *code __attribute__((swift_name("code")));
@property (readonly) NSString * _Nullable data __attribute__((swift_name("data")));
@property (readonly) NSString *descriptionText __attribute__((swift_name("descriptionText")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/**
 * The type of a resource ([RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3), `RESOURCE-TYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResourceType")))
@interface CalendrumCoreResourceType : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreResourceTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ResourceType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of ResourceType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResourceType.Companion")))
@interface CalendrumCoreResourceTypeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ResourceType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreResourceTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ResourceType holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreResourceType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `PROJECTOR`. A projector. */
@property (readonly) CalendrumCoreResourceType *Projector __attribute__((swift_name("Projector")));

/** `REMOTE-CONFERENCE-AUDIO`. A remote conference audio channel. */
@property (readonly) CalendrumCoreResourceType *RemoteConferenceAudio __attribute__((swift_name("RemoteConferenceAudio")));

/** `REMOTE-CONFERENCE-VIDEO`. A remote conference video channel. */
@property (readonly) CalendrumCoreResourceType *RemoteConferenceVideo __attribute__((swift_name("RemoteConferenceVideo")));

/** `ROOM`. A room. */
@property (readonly) CalendrumCoreResourceType *Room __attribute__((swift_name("Room")));
@end


/**
 * The value of a `RESOURCES` property: one or more resources needed for an activity, as text
 * ([RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)).
 *
 * Build one with `Resources(listOf("Projector"))`; the list must not be empty. Immutable.
 *
 * @property values the resource names, unescaped.
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Resources")))
@interface CalendrumCoreResources : CalendrumCoreBase
- (instancetype)initWithValues:(NSArray<NSString *> *)values __attribute__((swift_name("init(values:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Resources equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<NSString *> *values __attribute__((swift_name("values")));
@end


/**
 * The definitions of [RFC 5545](https://www.rfc-editor.org/rfc/rfc5545): every standard property, parameter and
 * component, with value types, allowed parameters and cardinality per component.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Rfc5545")))
@interface CalendrumCoreRfc5545 : CalendrumCoreBase <CalendrumCoreICalExtension>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The definitions of [RFC 5545](https://www.rfc-editor.org/rfc/rfc5545): every standard property, parameter and
 * component, with value types, allowed parameters and cardinality per component.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)rfc5545 __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRfc5545 *shared __attribute__((swift_name("shared")));

/** Adds the definitions of this standard to [registry]. */
- (void)contributeRegistry:(CalendrumCoreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The definitions of [RFC 7986](https://www.rfc-editor.org/rfc/rfc7986): `NAME`, `REFRESH-INTERVAL`, `SOURCE`, `COLOR`,
 * `IMAGE`, `CONFERENCE` and the calendar-level `UID`, `URL`, `DESCRIPTION` and `CATEGORIES`, with the parameters
 * `DISPLAY`, `EMAIL`, `FEATURE` and `LABEL`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Rfc7986")))
@interface CalendrumCoreRfc7986 : CalendrumCoreBase <CalendrumCoreICalExtension>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The definitions of [RFC 7986](https://www.rfc-editor.org/rfc/rfc7986): `NAME`, `REFRESH-INTERVAL`, `SOURCE`, `COLOR`,
 * `IMAGE`, `CONFERENCE` and the calendar-level `UID`, `URL`, `DESCRIPTION` and `CATEGORIES`, with the parameters
 * `DISPLAY`, `EMAIL`, `FEATURE` and `LABEL`.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)rfc7986 __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRfc7986 *shared __attribute__((swift_name("shared")));

/** Adds the definitions of this standard to [registry]. */
- (void)contributeRegistry:(CalendrumCoreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The definitions of [RFC 9073](https://www.rfc-editor.org/rfc/rfc9073): the components `PARTICIPANT`, `VLOCATION` and
 * `VRESOURCE`, the properties `LOCATION-TYPE`, `PARTICIPANT-TYPE`, `RESOURCE-TYPE`, `CALENDAR-ADDRESS`,
 * `STYLED-DESCRIPTION` and `STRUCTURED-DATA`, and the parameters `ORDER`, `SCHEMA` and `DERIVED`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Rfc9073")))
@interface CalendrumCoreRfc9073 : CalendrumCoreBase <CalendrumCoreICalExtension>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The definitions of [RFC 9073](https://www.rfc-editor.org/rfc/rfc9073): the components `PARTICIPANT`, `VLOCATION` and
 * `VRESOURCE`, the properties `LOCATION-TYPE`, `PARTICIPANT-TYPE`, `RESOURCE-TYPE`, `CALENDAR-ADDRESS`,
 * `STYLED-DESCRIPTION` and `STRUCTURED-DATA`, and the parameters `ORDER`, `SCHEMA` and `DERIVED`.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)rfc9073 __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRfc9073 *shared __attribute__((swift_name("shared")));

/** Adds the definitions of this standard to [registry]. */
- (void)contributeRegistry:(CalendrumCoreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The definitions of [RFC 9074](https://www.rfc-editor.org/rfc/rfc9074): the alarm properties `ACKNOWLEDGED` and
 * `PROXIMITY` and the use of `UID` and `RELATED-TO` in `VALARM`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Rfc9074")))
@interface CalendrumCoreRfc9074 : CalendrumCoreBase <CalendrumCoreICalExtension>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The definitions of [RFC 9074](https://www.rfc-editor.org/rfc/rfc9074): the alarm properties `ACKNOWLEDGED` and
 * `PROXIMITY` and the use of `UID` and `RELATED-TO` in `VALARM`.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)rfc9074 __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRfc9074 *shared __attribute__((swift_name("shared")));

/** Adds the definitions of this standard to [registry]. */
- (void)contributeRegistry:(CalendrumCoreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The definitions of [RFC 9253](https://www.rfc-editor.org/rfc/rfc9253): the properties `LINK`, `CONCEPT` and `REFID`,
 * the parameters `LINKREL` and `GAP`, and the new relation types.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Rfc9253")))
@interface CalendrumCoreRfc9253 : CalendrumCoreBase <CalendrumCoreICalExtension>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The definitions of [RFC 9253](https://www.rfc-editor.org/rfc/rfc9253): the properties `LINK`, `CONCEPT` and `REFID`,
 * the parameters `LINKREL` and `GAP`, and the new relation types.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)rfc9253 __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRfc9253 *shared __attribute__((swift_name("shared")));

/** Adds the definitions of this standard to [registry]. */
- (void)contributeRegistry:(CalendrumCoreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The role of a calendar user in an event ([RFC 5545 §3.2.16](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.16),
 * `ROLE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Role")))
@interface CalendrumCoreRole : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRoleCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Role with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Role and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Role.Companion")))
@interface CalendrumCoreRoleCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Role and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRoleCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Role holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreRole *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHAIR`. The user chairs the event. */
@property (readonly) CalendrumCoreRole *Chair __attribute__((swift_name("Chair")));

/** `NON-PARTICIPANT`. Informed only; does not take part. */
@property (readonly) CalendrumCoreRole *NonParticipant __attribute__((swift_name("NonParticipant")));

/** `OPT-PARTICIPANT`. An optional participant. */
@property (readonly) CalendrumCoreRole *OptParticipant __attribute__((swift_name("OptParticipant")));

/** `REQ-PARTICIPANT`. A required participant; the default. */
@property (readonly) CalendrumCoreRole *ReqParticipant __attribute__((swift_name("ReqParticipant")));
@end


/**
 * The result of decoding into a schema.
 *
 * @property items the objects decoded, in input order.
 * @property problems the problems found, with values never shown.
 * @property skipped the number of components left out because a required field was missing or broken.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SchemaResult")))
@interface CalendrumCoreSchemaResult<T> : CalendrumCoreBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<T> *items __attribute__((swift_name("items")));
@property (readonly) NSArray<CalendrumCoreProblem *> *problems __attribute__((swift_name("problems")));
@property (readonly) int32_t skipped __attribute__((swift_name("skipped")));
@end


/**
 * Operations on recurring events: splitting and merging calendars by `UID`, and changing a whole series, one instance
 * or "this and the following" instances, the way a calendar application does.
 *
 * All functions are pure: they return a new calendar and leave the one they are given as it is.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Series")))
@interface CalendrumCoreSeries : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Operations on recurring events: splitting and merging calendars by `UID`, and changing a whole series, one instance
 * or "this and the following" instances, the way a calendar application does.
 *
 * All functions are pure: they return a new calendar and leave the one they are given as it is.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)series __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreSeries *shared __attribute__((swift_name("shared")));

/**
 * Removes one instance of the series: adds an `EXDATE` to the master and drops the override of that instance, if
 * any.
 *
 * @param series the series to change.
 * @param recurrenceId the start of the instance to remove.
 */
- (CalendrumCoreVCalendar *)deleteThisSeries:(CalendrumCoreSeriesView *)series recurrenceId:(id<CalendrumCoreICalDateTime>)recurrenceId __attribute__((swift_name("deleteThis(series:recurrenceId:)")));

/**
 * Removes the given instance and all following ones: the rule of the master ends before it (`UNTIL`) and the later
 * overrides are dropped. If that leaves nothing, the whole series is removed.
 *
 * @param series the series to change.
 * @param recurrenceId the start of the first instance to remove.
 */
- (CalendrumCoreVCalendar *)deleteThisAndFutureSeries:(CalendrumCoreSeriesView *)series recurrenceId:(id<CalendrumCoreICalDateTime>)recurrenceId __attribute__((swift_name("deleteThisAndFuture(series:recurrenceId:)")));

/**
 * Changes every instance: edits the master, keeping the overrides.
 *
 * @param series the series to change.
 * @param edit changes the master.
 * @throws IllegalArgumentException if the series has no master.
 */
- (CalendrumCoreVCalendar *)editAllSeries:(CalendrumCoreSeriesView *)series edit:(void (^)(CalendrumCoreVEventBuilder *))edit __attribute__((swift_name("editAll(series:edit:)")));

/**
 * Changes one instance of the series: edits its override, which is created from the master when it does not exist.
 *
 * @param series the series to change.
 * @param recurrenceId the start of the instance, as the master produces it.
 * @param edit changes the override.
 * @throws IllegalArgumentException if the series has no master and no such override.
 */
- (CalendrumCoreVCalendar *)editThisSeries:(CalendrumCoreSeriesView *)series recurrenceId:(id<CalendrumCoreICalDateTime>)recurrenceId edit:(void (^)(CalendrumCoreVEventBuilder *))edit __attribute__((swift_name("editThis(series:recurrenceId:edit:)")));

/**
 * Changes the given instance and all following ones by splitting the series ([ThisAndFuture.Split]).
 *
 * @param series the series to change.
 * @param recurrenceId the start of the first instance to change.
 * @param edit changes the new series.
 * @throws IllegalArgumentException if the series has no master.
 */
- (CalendrumCoreVCalendar *)editThisAndFutureSeries:(CalendrumCoreSeriesView *)series recurrenceId:(id<CalendrumCoreICalDateTime>)recurrenceId edit:(void (^)(CalendrumCoreVEventBuilder *))edit __attribute__((swift_name("editThisAndFuture(series:recurrenceId:edit:)")));

/**
 * Changes the given instance and all following ones in the way chosen by [mode].
 *
 * @param series the series to change.
 * @param recurrenceId the start of the first instance to change.
 * @param mode splits the series or adds an override with `RANGE=THISANDFUTURE`.
 * @param edit changes the instances.
 * @throws IllegalArgumentException if the series has no master.
 */
- (CalendrumCoreVCalendar *)editThisAndFutureSeries:(CalendrumCoreSeriesView *)series recurrenceId:(id<CalendrumCoreICalDateTime>)recurrenceId mode:(CalendrumCoreThisAndFuture *)mode edit:(void (^)(CalendrumCoreVEventBuilder *))edit __attribute__((swift_name("editThisAndFuture(series:recurrenceId:mode:edit:)")));

/** Returns the series of events with the given [uid] in [calendar], or `null` when there is none. */
- (CalendrumCoreSeriesView * _Nullable)findCalendar:(CalendrumCoreVCalendar *)calendar uid:(NSString *)uid __attribute__((swift_name("find(calendar:uid:)")));

/**
 * Merges [calendars] into one: components are matched by name, `UID` and `RECURRENCE-ID`, and the one with the
 * newer revision (`SEQUENCE`, then `DTSTAMP`) wins; time zones are matched by `TZID`. The result takes the calendar
 * properties of the first.
 *
 * @param calendars the calendars to merge; at least one.
 * @throws IllegalArgumentException if no calendar is given.
 */
- (CalendrumCoreVCalendar *)mergeCalendars:(CalendrumCoreKotlinArray<CalendrumCoreVCalendar *> *)calendars __attribute__((swift_name("merge(calendars:)")));

/**
 * Splits [calendar] into one calendar per `UID`, each with the time zones it needs. Components without a `UID` get
 * a calendar each.
 */
- (NSArray<CalendrumCoreVCalendar *> *)splitByUidCalendar:(CalendrumCoreVCalendar *)calendar __attribute__((swift_name("splitByUid(calendar:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * A recurring event as stored in a calendar: the master with the `RRULE` and the overrides with a `RECURRENCE-ID`, all
 * sharing one `UID`.
 *
 * Get one with [Series.find] and pass it to the editing functions of [Series]. Immutable.
 *
 * @property calendar the calendar the series is in.
 * @property uid the `UID` the components share.
 * @property master the component with the recurrence rule, or `null` when only overrides are present.
 * @property overrides the components that override single instances, in calendar order.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SeriesView")))
@interface CalendrumCoreSeriesView : CalendrumCoreBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreVCalendar *calendar __attribute__((swift_name("calendar")));
@property (readonly) CalendrumCoreVEvent * _Nullable master __attribute__((swift_name("master")));
@property (readonly) NSArray<CalendrumCoreVEvent *> *overrides __attribute__((swift_name("overrides")));
@property (readonly) NSString *uid __attribute__((swift_name("uid")));
@end


/**
 * The value of a `STRUCTURED-DATA` property: machine-readable data about the component, inline or by URI
 * ([RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)).
 *
 * @property valueType [ValueType.Text], [ValueType.Uri] or [ValueType.Binary].
 * @property text the text or the URI, or `null` for binary data.
 * @property formatType the media type (`FMTTYPE`), or `null`.
 * @property schema the schema URI (`SCHEMA`), or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StructuredData")))
@interface CalendrumCoreStructuredData : CalendrumCoreBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumCoreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumCoreKotlinByteArray *)bytes formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(bytes:formatType:schema:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumCoreValueType *)valueType formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(value:valueType:formatType:schema:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the binary content, or `null` when the data is text or a URI. */
- (CalendrumCoreKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a StructuredData equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the data is binary (base64 encoded in the text form). */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable schema __attribute__((swift_name("schema")));
@property (readonly) NSString * _Nullable text __attribute__((swift_name("text")));
@property (readonly) CalendrumCoreValueType *valueType __attribute__((swift_name("valueType")));
@end


/**
 * The value of a `STYLED-DESCRIPTION` property: a description in a rich format, such as HTML, as text or by URI
 * ([RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)).
 *
 * The markup is untrusted input: sanitize it before showing it. Immutable.
 *
 * @property value the markup or the URI.
 * @property valueType [ValueType.Text] or [ValueType.Uri].
 * @property formatType the media type (`FMTTYPE`), such as `text/html`, or `null`.
 * @property isDerived whether the text is derived from the plain `DESCRIPTION` (`DERIVED`).
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StyledDescription")))
@interface CalendrumCoreStyledDescription : CalendrumCoreBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumCoreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumCoreValueType *)valueType formatType:(NSString *)formatType __attribute__((swift_name("init(value:valueType:formatType:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a StyledDescription equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));
@property (readonly) BOOL isDerived __attribute__((swift_name("isDerived")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumCoreValueType *valueType __attribute__((swift_name("valueType")));
@end


/** How to change "this and all following" instances of a series. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ThisAndFuture")))
@interface CalendrumCoreThisAndFuture : CalendrumCoreKotlinEnum<CalendrumCoreThisAndFuture *>
+ (instancetype)alloc __attribute__((unavailable));

/** How to change "this and all following" instances of a series. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreThisAndFuture *split __attribute__((swift_name("split")));
@property (class, readonly) CalendrumCoreThisAndFuture *range __attribute__((swift_name("range")));
+ (CalendrumCoreKotlinArray<CalendrumCoreThisAndFuture *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreThisAndFuture *> *entries __attribute__((swift_name("entries")));
@end


/**
 * The status of a to-do ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11), `STATUS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TodoStatus")))
@interface CalendrumCoreTodoStatus : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreTodoStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a TodoStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of TodoStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TodoStatus.Companion")))
@interface CalendrumCoreTodoStatusCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of TodoStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreTodoStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new TodoStatus holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreTodoStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The to-do is cancelled. */
@property (readonly) CalendrumCoreTodoStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `COMPLETED`. The to-do is completed. */
@property (readonly) CalendrumCoreTodoStatus *Completed __attribute__((swift_name("Completed")));

/** `IN-PROCESS`. The to-do is in progress. */
@property (readonly) CalendrumCoreTodoStatus *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The to-do needs action. */
@property (readonly) CalendrumCoreTodoStatus *NeedsAction __attribute__((swift_name("NeedsAction")));
@end


/**
 * Whether an event blocks time in a free/busy search
 * ([RFC 5545 §3.8.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.7), `TRANSP`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Transparency")))
@interface CalendrumCoreTransparency : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreTransparencyCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Transparency with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Transparency and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Transparency.Companion")))
@interface CalendrumCoreTransparencyCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Transparency and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreTransparencyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Transparency holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreTransparency *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `OPAQUE`. The event blocks time; the default. */
@property (readonly) CalendrumCoreTransparency *Opaque __attribute__((swift_name("Opaque")));

/** `TRANSPARENT`. The event does not block time. */
@property (readonly) CalendrumCoreTransparency *Transparent __attribute__((swift_name("Transparent")));
@end


/**
 * The value of a `TRIGGER` property: when an alarm fires
 * ([RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)).
 *
 * Either a time relative to the start or end of the parent component ([Relative]) or an absolute UTC time ([Absolute]).
 * Immutable.
 */
__attribute__((swift_name("Trigger")))
@protocol CalendrumCoreTrigger
@required

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/**
 * An alarm that fires at one moment in UTC.
 *
 * Build one with `Trigger.Absolute(ICalDateTime.Utc(...))`.
 *
 * @property dateTime the moment; always UTC.
 * @throws IllegalArgumentException when constructed from a value that is not UTC.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TriggerAbsolute")))
@interface CalendrumCoreTriggerAbsolute : CalendrumCoreBase <CalendrumCoreTrigger>
- (instancetype)initWithDateTime:(id<CalendrumCoreICalDateTime>)dateTime __attribute__((swift_name("init(dateTime:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Absolute equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumCoreICalDateTime> dateTime __attribute__((swift_name("dateTime")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/**
 * An alarm that fires a [duration] before (negative) or after (positive) the start or end of its parent.
 *
 * Build one with `Trigger.Relative(ICalDuration.ofMinutes(-15))`.
 *
 * @property duration the offset from the related end.
 * @property related which end the offset is measured from; [Related.Start] by default.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TriggerRelative")))
@interface CalendrumCoreTriggerRelative : CalendrumCoreBase <CalendrumCoreTrigger>
- (instancetype)initWithDuration:(CalendrumCoreICalDuration *)duration __attribute__((swift_name("init(duration:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithDuration:(CalendrumCoreICalDuration *)duration related:(CalendrumCoreRelated *)related __attribute__((swift_name("init(duration:related:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Relative equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreICalDuration *duration __attribute__((swift_name("duration")));
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumCoreRelated *related __attribute__((swift_name("related")));
@end


/** Makes the `UID` values of new components. There is no global generator: you pass one in [BuildDefaults]. */
__attribute__((swift_name("UidGenerator")))
@protocol CalendrumCoreUidGenerator
@required

/** Returns a new `UID`; it must be globally unique. */
- (NSString *)next __attribute__((swift_name("next()")));
@end


/** Ready-made generators. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UidGeneratorCompanion")))
@interface CalendrumCoreUidGeneratorCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Ready-made generators. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreUidGeneratorCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a generator of reproducible, name-based UUID version 5 `UID`s: the same [namespace] and natural key
 * always give the same `UID`, which keeps a regenerated feed stable.
 *
 * @param namespace a UUID text, or any other text, which is mapped to a UUID in the DNS namespace.
 * @param naturalKey gives the key that identifies the item; called once per `UID`.
 */
- (id<CalendrumCoreUidGenerator>)v5Namespace:(NSString *)namespace_ naturalKey:(NSString *(^)(void))naturalKey __attribute__((swift_name("v5(namespace:naturalKey:)")));
@end


/**
 * A `VALARM` component: a reminder that fires relative to its parent event or to-do
 * ([RFC 5545 §3.6.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.6)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VAlarm")))
@interface CalendrumCoreVAlarm : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVAlarmCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this alarm changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this alarm.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVAlarm *)editBlock:(void (^)(CalendrumCoreVAlarmBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VAlarm with an equal VALARM component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this alarm; unchanged nodes are shared with it. */
- (CalendrumCoreVAlarmBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this alarm and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVAlarmBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The time the alarm was acknowledged of the alarm (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm (`ACTION`, [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) CalendrumCoreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attachments of the alarm (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the alarm (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the alarm (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The description of the alarm (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The duration of the alarm (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The links of the alarm (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the alarm, in source order. */
@property (readonly) NSArray<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumCoreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

/**
 * The reference identifiers of the alarm (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the alarm (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The number of repetitions of the alarm (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable repeat __attribute__((swift_name("repeat")));

/**
 * The styled descriptions of the alarm (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

/**
 * The summary, the short title of the alarm (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The trigger of the alarm (`TRIGGER`,
 * [RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

/**
 * The globally unique identifier of the alarm (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/**
 * Collects the properties and nested components of a new or edited alarm, with a Kotlin DSL and chained calls. Get
 * one from [VAlarm.builder] or [VAlarm.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VAlarm.Builder")))
@interface CalendrumCoreVAlarmBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVAlarmBuilder *>

/**
 * Sets [acknowledged] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)acknowledgedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("acknowledged(value:)")));

/**
 * Sets [action] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)actionValue:(CalendrumCoreAlarmAction * _Nullable)value __attribute__((swift_name("action(value:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumCoreVAlarmBuilder *)attachAttach:(id<CalendrumCoreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumCoreVAlarmBuilder *)attendeeAttendee:(CalendrumCoreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumCoreVAlarmBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumCoreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumCoreVAlarmBuilder *)attendeesBlock:(void (^)(CalendrumCoreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Makes this an `AUDIO` alarm: sets `ACTION` and [trigger]; the platform chooses the sound unless you attach
 * one.
 *
 * @param trigger when the alarm fires.
 */
- (CalendrumCoreVAlarmBuilder *)audioTrigger:(id<CalendrumCoreTrigger>)trigger __attribute__((swift_name("audio(trigger:)")));

/** Creates the alarm. */
- (CalendrumCoreVAlarm *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVAlarmBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Makes this a `DISPLAY` alarm: sets `ACTION`, [trigger] and the `DESCRIPTION` shown to the user.
 *
 * @param trigger when the alarm fires.
 * @param description the text to display.
 */
- (CalendrumCoreVAlarmBuilder *)displayTrigger:(id<CalendrumCoreTrigger>)trigger description:(NSString *)description __attribute__((swift_name("display(trigger:description:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)durationValue:(CalendrumCoreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Makes this an `EMAIL` alarm: sets `ACTION`, [trigger], the `SUMMARY` that becomes the subject and the
 * `DESCRIPTION` that becomes the body.
 *
 * @param trigger when the alarm fires.
 * @param summary the subject of the mail.
 * @param description the body of the mail.
 */
- (CalendrumCoreVAlarmBuilder *)emailTrigger:(id<CalendrumCoreTrigger>)trigger summary:(NSString *)summary description:(NSString *)description __attribute__((swift_name("email(trigger:summary:description:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVAlarmBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the alarm. */
- (CalendrumCoreVAlarmBuilder *)locationValue:(CalendrumCoreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumCoreVAlarmBuilder *)locationBlock:(void (^)(CalendrumCoreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the alarm in [block]: add, replace or remove them. */
- (CalendrumCoreVAlarmBuilder *)locationsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [proximity] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)proximityValue:(CalendrumCoreProximity * _Nullable)value __attribute__((swift_name("proximity(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVAlarmBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVAlarmBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVAlarmBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [repeat] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)repeatValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("repeat(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumCoreVAlarmBuilder *)styledDescriptionValue:(CalendrumCoreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [trigger] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)triggerValue:(id<CalendrumCoreTrigger> _Nullable)value __attribute__((swift_name("trigger(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVAlarmBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * The time the alarm was acknowledged of the alarm being built (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)); assigning `null` removes the property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm being built (`ACTION`,
 * [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The description of the alarm being built (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The duration of the alarm being built (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/** The locations of the alarm being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm being built (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)); assigning `null` removes the property.
 */
@property CalendrumCoreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

/**
 * The number of repetitions of the alarm being built (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable repeat __attribute__((swift_name("repeat")));

/**
 * The summary, the short title of the alarm being built (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The trigger of the alarm being built (`TRIGGER`,
 * [RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

/**
 * The globally unique identifier of the alarm being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/** Creates VAlarm values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VAlarm.Companion")))
@interface CalendrumCoreVAlarmCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VAlarm values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVAlarmCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumCoreVAlarmBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVAlarmBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VAlarm, or returns `null` when it is not a `VALARM` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVAlarm * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VCALENDAR` component: the container of an iCalendar object, with its calendar properties and its events, to-dos,
 * journal entries, free/busy components and time zones
 * ([RFC 5545 §3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.4)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VCalendar")))
@interface CalendrumCoreVCalendar : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVCalendarCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this calendar changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this calendar.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVCalendar *)editBlock:(void (^)(CalendrumCoreVCalendarBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VCalendar with an equal VCALENDAR component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this calendar; unchanged nodes are shared with it. */
- (CalendrumCoreVCalendarBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this calendar and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVCalendarBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The calendar scale of the calendar (`CALSCALE`,
 * [RFC 5545 §3.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.1)), or `null` when it is missing or does
 * not decode.
 */
@property (readonly) NSString * _Nullable calScale __attribute__((swift_name("calScale")));

/**
 * The categories of the calendar (`CATEGORIES` properties,
 * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *categories __attribute__((swift_name("categories")));

/**
 * The color of the calendar (`COLOR`, [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable color __attribute__((swift_name("color")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The descriptions of the calendar (`DESCRIPTION` properties,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *descriptions __attribute__((swift_name("descriptions")));

/** The events nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumCoreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy component nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumCoreVFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The images of the calendar (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumCoreImage *> *images __attribute__((swift_name("images")));

/** The journal entries nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumCoreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)), or `null` when it is missing or does
 * not decode.
 */
@property (readonly) CalendrumCoreMethod * _Nullable method __attribute__((swift_name("method")));

/**
 * The names of the calendar (`NAME` properties,
 * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));

/**
 * The product identifier of the calendar (`PRODID`,
 * [RFC 5545 §3.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.3)), or `null` when it is missing or does
 * not decode.
 */
@property (readonly) NSString * _Nullable prodId __attribute__((swift_name("prodId")));

/**
 * The suggested refresh interval of the calendar (`REFRESH-INTERVAL`,
 * [RFC 7986 §5.7](https://www.rfc-editor.org/rfc/rfc7986#section-5.7)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumCoreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar (`SOURCE`, [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumCoreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumCoreVTodo *> *todos __attribute__((swift_name("todos")));

/**
 * The globally unique identifier of the calendar (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the calendar (`URL`, [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable url __attribute__((swift_name("url")));

/**
 * The iCalendar version of the calendar (`VERSION`,
 * [RFC 5545 §3.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.4)), or `null` when it is missing or does
 * not decode.
 */
@property (readonly) NSString * _Nullable version __attribute__((swift_name("version")));
@end


/**
 * Collects the properties and nested components of a new or edited calendar, with a Kotlin DSL and chained calls.
 * Get one from [VCalendar.builder] or [VCalendar.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VCalendar.Builder")))
@interface CalendrumCoreVCalendarBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVCalendarBuilder *>

/** Creates the calendar. */
- (CalendrumCoreVCalendar *)build __attribute__((swift_name("build()")));

/**
 * Sets [calScale] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)calScaleValue:(NSString * _Nullable)value __attribute__((swift_name("calScale(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumCoreVCalendarBuilder *)categoriesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumCoreVCalendarBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds [event] as a event of the calendar. */
- (CalendrumCoreVCalendarBuilder *)eventEvent:(CalendrumCoreVEvent *)event __attribute__((swift_name("event(event:)")));

/**
 * Adds a event that [block] sets up.
 *
 * @param block sets up the new event.
 */
- (CalendrumCoreVCalendarBuilder *)eventBlock:(void (^)(CalendrumCoreVEventBuilder *))block __attribute__((swift_name("event(block:)")));

/** Edits the events of the calendar in [block]: add, replace or remove them. */
- (CalendrumCoreVCalendarBuilder *)eventsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVEvent *> *))block __attribute__((swift_name("events(block:)")));

/** Edits the free/busy components of the calendar in [block]: add, replace or remove them. */
- (CalendrumCoreVCalendarBuilder *)freeBusiesBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVFreeBusy *> *))block __attribute__((swift_name("freeBusies(block:)")));

/** Adds [freeBusy] as a free/busy component of the calendar. */
- (CalendrumCoreVCalendarBuilder *)freeBusyFreeBusy:(CalendrumCoreVFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/**
 * Adds a free/busy component that [block] sets up.
 *
 * @param block sets up the new free/busy component.
 */
- (CalendrumCoreVCalendarBuilder *)freeBusyBlock:(void (^)(CalendrumCoreVFreeBusyBuilder *))block __attribute__((swift_name("freeBusy(block:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumCoreVCalendarBuilder *)imageValue:(CalendrumCoreImage *)value __attribute__((swift_name("image(value:)")));

/** Adds [journal] as a journal entry of the calendar. */
- (CalendrumCoreVCalendarBuilder *)journalJournal:(CalendrumCoreVJournal *)journal __attribute__((swift_name("journal(journal:)")));

/**
 * Adds a journal entry that [block] sets up.
 *
 * @param block sets up the new journal entry.
 */
- (CalendrumCoreVCalendarBuilder *)journalBlock:(void (^)(CalendrumCoreVJournalBuilder *))block __attribute__((swift_name("journal(block:)")));

/** Edits the journal entries of the calendar in [block]: add, replace or remove them. */
- (CalendrumCoreVCalendarBuilder *)journalsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVJournal *> *))block __attribute__((swift_name("journals(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)lastModifiedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/**
 * Sets [method] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)methodValue:(CalendrumCoreMethod * _Nullable)value __attribute__((swift_name("method(value:)")));

/** Adds a `NAME` property with the text [text]. */
- (CalendrumCoreVCalendarBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/**
 * Sets [prodId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)prodIdValue:(NSString * _Nullable)value __attribute__((swift_name("prodId(value:)")));

/**
 * Sets [refreshInterval] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)refreshIntervalValue:(CalendrumCoreICalDuration * _Nullable)value __attribute__((swift_name("refreshInterval(value:)")));

/**
 * Sets [source] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)sourceValue:(NSString * _Nullable)value __attribute__((swift_name("source(value:)")));

/** Adds [timeZone] as a time zone of the calendar. */
- (CalendrumCoreVCalendarBuilder *)timeZoneTimeZone:(CalendrumCoreVTimeZone *)timeZone __attribute__((swift_name("timeZone(timeZone:)")));

/**
 * Adds a time zone that [block] sets up.
 *
 * @param block sets up the new time zone.
 */
- (CalendrumCoreVCalendarBuilder *)timeZoneBlock:(void (^)(CalendrumCoreVTimeZoneBuilder *))block __attribute__((swift_name("timeZone(block:)")));

/** Edits the time zones of the calendar in [block]: add, replace or remove them. */
- (CalendrumCoreVCalendarBuilder *)timeZonesBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVTimeZone *> *))block __attribute__((swift_name("timeZones(block:)")));

/** Adds [todo] as a to-do of the calendar. */
- (CalendrumCoreVCalendarBuilder *)todoTodo:(CalendrumCoreVTodo *)todo __attribute__((swift_name("todo(todo:)")));

/**
 * Adds a to-do that [block] sets up.
 *
 * @param block sets up the new to-do.
 */
- (CalendrumCoreVCalendarBuilder *)todoBlock:(void (^)(CalendrumCoreVTodoBuilder *))block __attribute__((swift_name("todo(block:)")));

/** Edits the to-dos of the calendar in [block]: add, replace or remove them. */
- (CalendrumCoreVCalendarBuilder *)todosBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVTodo *> *))block __attribute__((swift_name("todos(block:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * Sets [version] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVCalendarBuilder *)versionValue:(NSString * _Nullable)value __attribute__((swift_name("version(value:)")));

/**
 * The calendar scale of the calendar being built (`CALSCALE`,
 * [RFC 5545 §3.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.1)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable calScale __attribute__((swift_name("calScale")));

/**
 * The color of the calendar being built (`COLOR`,
 * [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)); assigning `null` removes the property.
 */
@property NSString * _Nullable color __attribute__((swift_name("color")));

/** The events of the calendar being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy components of the calendar being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVFreeBusy *> *freeBusies __attribute__((swift_name("freeBusies")));

/** The journal entries of the calendar being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar being built (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreMethod * _Nullable method __attribute__((swift_name("method")));

/**
 * The product identifier of the calendar being built (`PRODID`,
 * [RFC 5545 §3.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.3)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable prodId __attribute__((swift_name("prodId")));

/**
 * The suggested refresh interval of the calendar being built (`REFRESH-INTERVAL`,
 * [RFC 7986 §5.7](https://www.rfc-editor.org/rfc/rfc7986#section-5.7)); assigning `null` removes the property.
 */
@property CalendrumCoreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar being built (`SOURCE`,
 * [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)); assigning `null` removes the property.
 */
@property NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones of the calendar being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos of the calendar being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVTodo *> *todos __attribute__((swift_name("todos")));

/**
 * The globally unique identifier of the calendar being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the calendar being built (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable url __attribute__((swift_name("url")));

/**
 * The iCalendar version of the calendar being built (`VERSION`,
 * [RFC 5545 §3.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.4)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable version __attribute__((swift_name("version")));
@end


/** Creates VCalendar values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VCalendar.Companion")))
@interface CalendrumCoreVCalendarCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VCalendar values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVCalendarCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumCoreVCalendarBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVCalendarBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VCalendar, or returns `null` when it is not a `VCALENDAR` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVCalendar * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VEVENT` component: something that happens at a time, such as a meeting
 * ([RFC 5545 §3.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.1)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VEvent")))
@interface CalendrumCoreVEvent : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVEventCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this event changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this event.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVEvent *)editBlock:(void (^)(CalendrumCoreVEventBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VEvent with an equal VEVENT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this event; unchanged nodes are shared with it. */
- (CalendrumCoreVEventBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this event and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVEventBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the event, in source order. */
@property (readonly) NSArray<CalendrumCoreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the event (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the event (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The categories of the event (`CATEGORIES` properties,
 * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *categories __attribute__((swift_name("categories")));

/**
 * The access classification of the event (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreClassification * _Nullable classification __attribute__((swift_name("classification")));

/**
 * The color of the event (`COLOR`, [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), or `null`
 * when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable color __attribute__((swift_name("color")));

/**
 * The comments of the event (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the event (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The conferences of the event (`CONFERENCE` properties,
 * [RFC 7986 §5.11](https://www.rfc-editor.org/rfc/rfc7986#section-5.11)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumCoreConference *> *conferences __attribute__((swift_name("conferences")));

/**
 * The contacts of the event (`CONTACT` properties,
 * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *contacts __attribute__((swift_name("contacts")));

/**
 * The creation time of the event (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The description of the event (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The duration of the event (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event (`DTEND`, [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The excluded dates of the event (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the event (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the event (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumCoreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the event (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the event (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/**
 * The location text of the event (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the event, in source order. */
@property (readonly) NSArray<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the event, in source order. */
@property (readonly) NSArray<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the event (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the event (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/**
 * The reference identifiers of the event (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the event (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the event (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the event, in source order. */
@property (readonly) NSArray<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The resources of the event (`RESOURCES` properties,
 * [RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)) from all properties in source
 * order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *resources __attribute__((swift_name("resources")));

/**
 * The recurrence rule of the event (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the event (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the event (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

/**
 * The summary, the short title of the event (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The time transparency, whether it blocks time of the event (`TRANSP`,
 * [RFC 5545 §3.8.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

/**
 * The globally unique identifier of the event (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the event (`URL`, [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable url __attribute__((swift_name("url")));
@end


/**
 * Collects the properties and nested components of a new or edited event, with a Kotlin DSL and chained calls. Get
 * one from [VEvent.builder] or [VEvent.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VEvent.Builder")))
@interface CalendrumCoreVEventBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVEventBuilder *>

/** Adds [alarm] as a alarm of the event. */
- (CalendrumCoreVEventBuilder *)alarmAlarm:(CalendrumCoreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumCoreVEventBuilder *)alarmBlock:(void (^)(CalendrumCoreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the event in [block]: add, replace or remove them. */
- (CalendrumCoreVEventBuilder *)alarmsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumCoreVEventBuilder *)attachAttach:(id<CalendrumCoreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumCoreVEventBuilder *)attendeeAttendee:(CalendrumCoreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumCoreVEventBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumCoreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumCoreVEventBuilder *)attendeesBlock:(void (^)(CalendrumCoreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the event.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults to generate
 *   them.
 * @throws IllegalStateException if `UNTIL` of the rule does not suit the type of `DTSTART`.
 */
- (CalendrumCoreVEvent *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumCoreVEventBuilder *)categoriesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)classificationValue:(CalendrumCoreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumCoreVEventBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVEventBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumCoreVEventBuilder *)conferenceValue:(CalendrumCoreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumCoreVEventBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)createdValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)durationValue:(CalendrumCoreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)endValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumCoreVEventBuilder *)exdateDates:(CalendrumCoreKotlinArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)geoValue:(CalendrumCoreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumCoreVEventBuilder *)imageValue:(CalendrumCoreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)lastModifiedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVEventBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the event. */
- (CalendrumCoreVEventBuilder *)locationValue:(CalendrumCoreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumCoreVEventBuilder *)locationBlock:(void (^)(CalendrumCoreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the event in [block]: add, replace or remove them. */
- (CalendrumCoreVEventBuilder *)locationsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)organizerValue:(CalendrumCoreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumCoreVEventBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumCoreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the event. */
- (CalendrumCoreVEventBuilder *)participantValue:(CalendrumCoreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumCoreVEventBuilder *)participantBlock:(void (^)(CalendrumCoreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the event in [block]: add, replace or remove them. */
- (CalendrumCoreVEventBuilder *)participantsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)priorityValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumCoreVEventBuilder *)rdateDates:(CalendrumCoreKotlinArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumCoreVEventBuilder *)rdatePeriods:(CalendrumCoreKotlinArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)recurrenceIdValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVEventBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVEventBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVEventBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumCoreVEventBuilder *)requestStatusStatus:(CalendrumCoreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the event. */
- (CalendrumCoreVEventBuilder *)resourceComponentValue:(CalendrumCoreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumCoreVEventBuilder *)resourceComponentBlock:(void (^)(CalendrumCoreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the event in [block]: add, replace or remove them. */
- (CalendrumCoreVEventBuilder *)resourceComponentsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumCoreVEventBuilder *)resourcesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)rruleValue:(CalendrumCoreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumCoreVEventBuilder *)rruleFrequency:(CalendrumCoreFrequency *)frequency block:(void (^)(CalendrumCoreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)sequenceValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)stampValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)startValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)statusValue:(CalendrumCoreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumCoreVEventBuilder *)structuredDataValue:(CalendrumCoreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumCoreVEventBuilder *)styledDescriptionValue:(CalendrumCoreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [transparency] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)transparencyValue:(CalendrumCoreTransparency * _Nullable)value __attribute__((swift_name("transparency(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVEventBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the event being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the event being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreClassification * _Nullable classification __attribute__((swift_name("classification")));

/**
 * The color of the event being built (`COLOR`,
 * [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)); assigning `null` removes the property.
 */
@property NSString * _Nullable color __attribute__((swift_name("color")));

/**
 * The creation time of the event being built (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The description of the event being built (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The duration of the event being built (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event being built (`DTEND`,
 * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The geographic position of the event being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the event being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the event being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the event being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the event being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the event being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the event being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the event being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The summary, the short title of the event being built (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The time transparency, whether it blocks time of the event being built (`TRANSP`,
 * [RFC 5545 §3.8.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.7)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

/**
 * The globally unique identifier of the event being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the event being built (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable url __attribute__((swift_name("url")));
@end


/** Creates VEvent values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VEvent.Companion")))
@interface CalendrumCoreVEventCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VEvent values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVEventCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumCoreVEventBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVEventBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VEvent, or returns `null` when it is not a `VEVENT` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVEvent * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VFREEBUSY` component: a request for or a report of free and busy time
 * ([RFC 5545 §3.6.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.4)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VFreeBusy")))
@interface CalendrumCoreVFreeBusy : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVFreeBusyCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this free/busy component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this free/busy component.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVFreeBusy *)editBlock:(void (^)(CalendrumCoreVFreeBusyBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VFreeBusy with an equal VFREEBUSY component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this free/busy component; unchanged nodes are shared with it. */
- (CalendrumCoreVFreeBusyBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this free/busy component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVFreeBusyBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attendees of the free/busy component (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The comments of the free/busy component (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the free/busy component (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The contact for the free/busy information (`CONTACT`,
 * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable contact __attribute__((swift_name("contact")));

/**
 * The end of the free/busy component (`DTEND`,
 * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The free/busy component nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumCoreFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The links of the free/busy component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The reference identifiers of the free/busy component (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the free/busy component (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the free/busy component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The styled descriptions of the free/busy component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

/**
 * The globally unique identifier of the free/busy component (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the free/busy component (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable url __attribute__((swift_name("url")));
@end


/**
 * Collects the properties and nested components of a new or edited free/busy component, with a Kotlin DSL and
 * chained calls. Get one from [VFreeBusy.builder] or [VFreeBusy.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VFreeBusy.Builder")))
@interface CalendrumCoreVFreeBusyBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVFreeBusyBuilder *>

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumCoreVFreeBusyBuilder *)attendeeAttendee:(CalendrumCoreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumCoreVFreeBusyBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumCoreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumCoreVFreeBusyBuilder *)attendeesBlock:(void (^)(CalendrumCoreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the free/busy component.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults.
 */
- (CalendrumCoreVFreeBusy *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumCoreVFreeBusyBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVFreeBusyBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [contact] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVFreeBusyBuilder *)contactValue:(NSString * _Nullable)value __attribute__((swift_name("contact(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVFreeBusyBuilder *)endValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds [freeBusy] as a free/busy component of the free/busy component. */
- (CalendrumCoreVFreeBusyBuilder *)freeBusyFreeBusy:(CalendrumCoreFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/** Adds [periods] as a free/busy component of the free/busy component. */
- (CalendrumCoreVFreeBusyBuilder *)freeBusyPeriods:(CalendrumCoreKotlinArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("freeBusy(periods:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVFreeBusyBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the free/busy component. */
- (CalendrumCoreVFreeBusyBuilder *)locationValue:(CalendrumCoreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumCoreVFreeBusyBuilder *)locationBlock:(void (^)(CalendrumCoreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumCoreVFreeBusyBuilder *)locationsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVFreeBusyBuilder *)organizerValue:(CalendrumCoreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumCoreVFreeBusyBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumCoreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the free/busy component. */
- (CalendrumCoreVFreeBusyBuilder *)participantValue:(CalendrumCoreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumCoreVFreeBusyBuilder *)participantBlock:(void (^)(CalendrumCoreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumCoreVFreeBusyBuilder *)participantsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVFreeBusyBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVFreeBusyBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVFreeBusyBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumCoreVFreeBusyBuilder *)requestStatusStatus:(CalendrumCoreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the free/busy component. */
- (CalendrumCoreVFreeBusyBuilder *)resourceComponentValue:(CalendrumCoreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumCoreVFreeBusyBuilder *)resourceComponentBlock:(void (^)(CalendrumCoreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumCoreVFreeBusyBuilder *)resourceComponentsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVFreeBusyBuilder *)stampValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVFreeBusyBuilder *)startValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumCoreVFreeBusyBuilder *)styledDescriptionValue:(CalendrumCoreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVFreeBusyBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVFreeBusyBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The contact for the free/busy information being built (`CONTACT`,
 * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable contact __attribute__((swift_name("contact")));

/**
 * The end of the free/busy component being built (`DTEND`,
 * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The locations of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/** The resources of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The globally unique identifier of the free/busy component being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the free/busy component being built (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable url __attribute__((swift_name("url")));
@end


/** Creates VFreeBusy values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VFreeBusy.Companion")))
@interface CalendrumCoreVFreeBusyCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VFreeBusy values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVFreeBusyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumCoreVFreeBusyBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVFreeBusyBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VFreeBusy, or returns `null` when it is not a `VFREEBUSY` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVFreeBusy * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VJOURNAL` component: a note attached to a date
 * ([RFC 5545 §3.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.3)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VJournal")))
@interface CalendrumCoreVJournal : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVJournalCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this journal entry changed by [block]. Properties and components that [block] does not touch
 * are the same objects as in this journal entry.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVJournal *)editBlock:(void (^)(CalendrumCoreVJournalBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VJournal with an equal VJOURNAL component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this journal entry; unchanged nodes are shared with it. */
- (CalendrumCoreVJournalBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this journal entry and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVJournalBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the journal entry (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the journal entry (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The categories of the journal entry (`CATEGORIES` properties,
 * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *categories __attribute__((swift_name("categories")));

/**
 * The access classification of the journal entry (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreClassification * _Nullable classification __attribute__((swift_name("classification")));

/**
 * The color of the journal entry (`COLOR`, [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable color __attribute__((swift_name("color")));

/**
 * The comments of the journal entry (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the journal entry (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The contacts of the journal entry (`CONTACT` properties,
 * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *contacts __attribute__((swift_name("contacts")));

/**
 * The creation time of the journal entry (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The descriptions of the journal entry (`DESCRIPTION` properties,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *descriptions __attribute__((swift_name("descriptions")));

/**
 * The excluded dates of the journal entry (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The images of the journal entry (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumCoreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the journal entry (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the journal entry (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The additional dates of the journal entry (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the journal entry (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/**
 * The reference identifiers of the journal entry (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the journal entry (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the journal entry (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreJournalStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the journal entry (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the journal entry (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

/**
 * The summary, the short title of the journal entry (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The globally unique identifier of the journal entry (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the journal entry (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable url __attribute__((swift_name("url")));
@end


/**
 * Collects the properties and nested components of a new or edited journal entry, with a Kotlin DSL and chained
 * calls. Get one from [VJournal.builder] or [VJournal.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VJournal.Builder")))
@interface CalendrumCoreVJournalBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVJournalBuilder *>

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumCoreVJournalBuilder *)attachAttach:(id<CalendrumCoreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumCoreVJournalBuilder *)attendeeAttendee:(CalendrumCoreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumCoreVJournalBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumCoreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumCoreVJournalBuilder *)attendeesBlock:(void (^)(CalendrumCoreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the journal entry.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumCoreVJournal *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumCoreVJournalBuilder *)categoriesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)classificationValue:(CalendrumCoreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumCoreVJournalBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVJournalBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumCoreVJournalBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)createdValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumCoreVJournalBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumCoreVJournalBuilder *)exdateDates:(CalendrumCoreKotlinArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumCoreVJournalBuilder *)imageValue:(CalendrumCoreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)lastModifiedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVJournalBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the journal entry. */
- (CalendrumCoreVJournalBuilder *)locationValue:(CalendrumCoreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumCoreVJournalBuilder *)locationBlock:(void (^)(CalendrumCoreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the journal entry in [block]: add, replace or remove them. */
- (CalendrumCoreVJournalBuilder *)locationsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)organizerValue:(CalendrumCoreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumCoreVJournalBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumCoreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the journal entry. */
- (CalendrumCoreVJournalBuilder *)participantValue:(CalendrumCoreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumCoreVJournalBuilder *)participantBlock:(void (^)(CalendrumCoreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the journal entry in [block]: add, replace or remove them. */
- (CalendrumCoreVJournalBuilder *)participantsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumCoreVJournalBuilder *)rdateDates:(CalendrumCoreKotlinArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumCoreVJournalBuilder *)rdatePeriods:(CalendrumCoreKotlinArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)recurrenceIdValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVJournalBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVJournalBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVJournalBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumCoreVJournalBuilder *)requestStatusStatus:(CalendrumCoreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the journal entry. */
- (CalendrumCoreVJournalBuilder *)resourceComponentValue:(CalendrumCoreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumCoreVJournalBuilder *)resourceComponentBlock:(void (^)(CalendrumCoreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the journal entry in [block]: add, replace or remove them. */
- (CalendrumCoreVJournalBuilder *)resourceComponentsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)rruleValue:(CalendrumCoreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumCoreVJournalBuilder *)rruleFrequency:(CalendrumCoreFrequency *)frequency block:(void (^)(CalendrumCoreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)sequenceValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)stampValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)startValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)statusValue:(CalendrumCoreJournalStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumCoreVJournalBuilder *)structuredDataValue:(CalendrumCoreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumCoreVJournalBuilder *)styledDescriptionValue:(CalendrumCoreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVJournalBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the journal entry being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreClassification * _Nullable classification __attribute__((swift_name("classification")));

/**
 * The color of the journal entry being built (`COLOR`,
 * [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)); assigning `null` removes the property.
 */
@property NSString * _Nullable color __attribute__((swift_name("color")));

/**
 * The creation time of the journal entry being built (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The time of the last modification of the journal entry being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the journal entry being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the journal entry being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The recurrence identifier of the journal entry being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the journal entry being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreJournalStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The summary, the short title of the journal entry being built (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The globally unique identifier of the journal entry being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the journal entry being built (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable url __attribute__((swift_name("url")));
@end


/** Creates VJournal values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VJournal.Companion")))
@interface CalendrumCoreVJournalCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VJournal values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVJournalCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumCoreVJournalBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVJournalBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VJournal, or returns `null` when it is not a `VJOURNAL` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVJournal * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VLOCATION` component: a structured location of an event or to-do, with a name, a type and coordinates
 * ([RFC 9073 §7.2](https://www.rfc-editor.org/rfc/rfc9073#section-7.2)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VLocation")))
@interface CalendrumCoreVLocation : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVLocationCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this location component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this location component.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVLocation *)editBlock:(void (^)(CalendrumCoreVLocationBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VLocation with an equal VLOCATION component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this location component; unchanged nodes are shared with it. */
- (CalendrumCoreVLocationBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this location component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVLocationBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the location component (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The description of the location component (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The geographic position of the location component (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the location component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/**
 * The types of the location component (`LOCATION-TYPE`,
 * [RFC 9073 §6.1](https://www.rfc-editor.org/rfc/rfc9073#section-6.1)), such as `hotel` or `airport`; empty when
 * the property is missing or does not decode.
 */
@property (readonly) NSArray<NSString *> *locationTypes __attribute__((swift_name("locationTypes")));

/**
 * The name of the location component (`NAME`, [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable name __attribute__((swift_name("name")));

/**
 * The reference identifiers of the location component (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the location component (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The structured data of the location component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The globally unique identifier of the location component (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the location component (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable url __attribute__((swift_name("url")));
@end


/**
 * Collects the properties and nested components of a new or edited location component, with a Kotlin DSL and
 * chained calls. Get one from [VLocation.builder] or [VLocation.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VLocation.Builder")))
@interface CalendrumCoreVLocationBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVLocationBuilder *>

/**
 * Creates the location component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumCoreVLocation *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVLocationBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVLocationBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVLocationBuilder *)geoValue:(CalendrumCoreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVLocationBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [locationTypes] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVLocationBuilder *)locationTypesValue:(NSArray<NSString *> * _Nullable)value __attribute__((swift_name("locationTypes(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVLocationBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVLocationBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVLocationBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVLocationBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumCoreVLocationBuilder *)structuredDataValue:(CalendrumCoreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVLocationBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVLocationBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The description of the location component being built (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The geographic position of the location component being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The types of the location component being built (`LOCATION-TYPE`,
 * [RFC 9073 §6.1](https://www.rfc-editor.org/rfc/rfc9073#section-6.1)); assigning `null` removes the property.
 */
@property NSArray<NSString *> * _Nullable locationTypes __attribute__((swift_name("locationTypes")));

/**
 * The name of the location component being built (`NAME`,
 * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)); assigning `null` removes the property.
 */
@property NSString * _Nullable name __attribute__((swift_name("name")));

/**
 * The globally unique identifier of the location component being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the location component being built (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable url __attribute__((swift_name("url")));
@end


/** Creates VLocation values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VLocation.Companion")))
@interface CalendrumCoreVLocationCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VLocation values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVLocationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumCoreVLocationBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVLocationBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VLocation, or returns `null` when it is not a `VLOCATION` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVLocation * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VRESOURCE` component: a resource needed for an event or to-do, such as a room or a projector
 * ([RFC 9073 §7.3](https://www.rfc-editor.org/rfc/rfc9073#section-7.3)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VResource")))
@interface CalendrumCoreVResource : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVResourceCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this resource component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this resource component.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVResource *)editBlock:(void (^)(CalendrumCoreVResourceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VResource with an equal VRESOURCE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this resource component; unchanged nodes are shared with it. */
- (CalendrumCoreVResourceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this resource component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVResourceBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the resource component (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The description of the resource component (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The geographic position of the resource component (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the resource component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/**
 * The name of the resource component (`NAME`, [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable name __attribute__((swift_name("name")));

/**
 * The reference identifiers of the resource component (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the resource component (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The resource type of the resource component (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumCoreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

/**
 * The structured data of the resource component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The globally unique identifier of the resource component (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/**
 * Collects the properties and nested components of a new or edited resource component, with a Kotlin DSL and
 * chained calls. Get one from [VResource.builder] or [VResource.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VResource.Builder")))
@interface CalendrumCoreVResourceBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVResourceBuilder *>

/**
 * Creates the resource component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumCoreVResource *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVResourceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVResourceBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVResourceBuilder *)geoValue:(CalendrumCoreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVResourceBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVResourceBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVResourceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVResourceBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVResourceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [resourceType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVResourceBuilder *)resourceTypeValue:(CalendrumCoreResourceType * _Nullable)value __attribute__((swift_name("resourceType(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumCoreVResourceBuilder *)structuredDataValue:(CalendrumCoreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVResourceBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * The description of the resource component being built (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The geographic position of the resource component being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The name of the resource component being built (`NAME`,
 * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)); assigning `null` removes the property.
 */
@property NSString * _Nullable name __attribute__((swift_name("name")));

/**
 * The resource type of the resource component being built (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)); assigning `null` removes the property.
 */
@property CalendrumCoreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

/**
 * The globally unique identifier of the resource component being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/** Creates VResource values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VResource.Companion")))
@interface CalendrumCoreVResourceCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VResource values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVResourceCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumCoreVResourceBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVResourceBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VResource, or returns `null` when it is not a `VRESOURCE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVResource * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VTIMEZONE` component: the rules that turn the local times of one time zone into UTC
 * ([RFC 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 *
 * The rules are a list of observances, each a `STANDARD` or `DAYLIGHT` component with an offset, a start and a
 * recurrence. Turning them into an offset for a moment is the job of a time zone resolver, which compiles them lazily.
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone")))
@interface CalendrumCoreVTimeZone : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVTimeZoneCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this time zone changed by [block]. Properties and components that [block] does not touch are
 * the same objects as in this time zone.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVTimeZone *)editBlock:(void (^)(CalendrumCoreVTimeZoneBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTimeZone with an equal VTIMEZONE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this time zone; unchanged nodes are shared with it. */
- (CalendrumCoreVTimeZoneBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this time zone and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVTimeZoneBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the time zone (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The time of the last modification of the time zone (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the time zone (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/** The `STANDARD` and `DAYLIGHT` observances in source order. */
@property (readonly) NSArray<CalendrumCoreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

/**
 * The reference identifiers of the time zone (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the time zone (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The URL of the time zone definition of the time zone (`TZURL`,
 * [RFC 5545 §3.8.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable tzUrl __attribute__((swift_name("tzUrl")));

/**
 * The time zone identifier of the time zone (`TZID`,
 * [RFC 5545 §3.8.3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.1)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable tzid __attribute__((swift_name("tzid")));
@end


/**
 * Collects the properties and nested components of a new or edited time zone, with a Kotlin DSL and chained calls.
 * Get one from [VTimeZone.builder] or [VTimeZone.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone.Builder")))
@interface CalendrumCoreVTimeZoneBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVTimeZoneBuilder *>

/** Creates the time zone. */
- (CalendrumCoreVTimeZone *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVTimeZoneBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `DAYLIGHT` observance that [block] sets up. */
- (CalendrumCoreVTimeZoneBuilder *)daylightBlock:(void (^)(CalendrumCoreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("daylight(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTimeZoneBuilder *)lastModifiedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVTimeZoneBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [observance] to the time zone. */
- (CalendrumCoreVTimeZoneBuilder *)observanceObservance:(CalendrumCoreVTimeZoneObservance *)observance __attribute__((swift_name("observance(observance:)")));

/** Edits the observances in [block]: add, replace or remove them. */
- (CalendrumCoreVTimeZoneBuilder *)observancesBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVTimeZoneObservance *> *))block __attribute__((swift_name("observances(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVTimeZoneBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVTimeZoneBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVTimeZoneBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds a `STANDARD` observance that [block] sets up. */
- (CalendrumCoreVTimeZoneBuilder *)standardBlock:(void (^)(CalendrumCoreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("standard(block:)")));

/**
 * Sets [tzUrl] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTimeZoneBuilder *)tzUrlValue:(NSString * _Nullable)value __attribute__((swift_name("tzUrl(value:)")));

/**
 * Sets [tzid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTimeZoneBuilder *)tzidValue:(NSString * _Nullable)value __attribute__((swift_name("tzid(value:)")));

/**
 * The time of the last modification of the time zone being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The observances of the time zone being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

/**
 * The URL of the time zone definition of the time zone being built (`TZURL`,
 * [RFC 5545 §3.8.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.5)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable tzUrl __attribute__((swift_name("tzUrl")));

/**
 * The time zone identifier of the time zone being built (`TZID`,
 * [RFC 5545 §3.8.3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.1)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable tzid __attribute__((swift_name("tzid")));
@end


/** Creates VTimeZone values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone.Companion")))
@interface CalendrumCoreVTimeZoneCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTimeZone values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVTimeZoneCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumCoreVTimeZoneBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVTimeZoneBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTimeZone, or returns `null` when it is not a `VTIMEZONE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVTimeZone * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * One set of rules of a time zone: a `STANDARD` or a `DAYLIGHT` component
 * ([RFC 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 *
 * It says that from [start] the offset from UTC is [offsetTo] instead of [offsetFrom], and when that repeats (a
 * rule or dates). A view over a tree node like [VTimeZone]; the subclasses [Standard] and [Daylight] only tell
 * which kind it is.
 *
 * @property component the underlying tree node.
 */
__attribute__((swift_name("VTimeZone.Observance")))
@interface CalendrumCoreVTimeZoneObservance : CalendrumCoreBase

/**
 * Returns a copy of this observance changed by [block]; untouched nodes are shared.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVTimeZoneObservance *)editBlock:(void (^)(CalendrumCoreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is an observance of the same kind with an equal component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this observance. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** The comments (`COMMENT`) in source order. */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts (`CONCEPT`, [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) of the
 * observance in source order.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The links (`LINK`, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) of the observance in
 * source order.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/**
 * The names of the observance (`TZNAME`,
 * [RFC 5545 §3.8.3.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.2)), such as `CEST`, in source
 * order.
 */
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));

/**
 * The UTC offset in force before the observance (`TZOFFSETFROM`,
 * [RFC 5545 §3.8.3.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The UTC offset the observance puts in force (`TZOFFSETTO`,
 * [RFC 5545 §3.8.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The additional start dates (`RDATE`) in source order; a period contributes its start. */
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The reference identifiers (`REFID`, [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) of
 * the observance in source order.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships (`RELATED-TO`, [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5))
 * of the observance in source order.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/** The recurrence rule of the observance (`RRULE`), or `null` when it has none or it does not decode. */
@property (readonly) CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The local start (`DTSTART`) of the observance, in the offset before it; `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));
@end


/** An observance of daylight saving time (`DAYLIGHT`). */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone.Daylight")))
@interface CalendrumCoreVTimeZoneDaylight : CalendrumCoreVTimeZoneObservance
@property (class, readonly, getter=companion) CalendrumCoreVTimeZoneDaylightCompanion *companion __attribute__((swift_name("companion")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** Creates `DAYLIGHT` observances. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone.DaylightCompanion")))
@interface CalendrumCoreVTimeZoneDaylightCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates `DAYLIGHT` observances. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVTimeZoneDaylightCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder for a `DAYLIGHT` observance. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Wraps [component] as a [Daylight], or returns `null` when it is not a `DAYLIGHT` component.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVTimeZoneDaylight * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * Collects the properties of a `STANDARD` or `DAYLIGHT` observance. Get one from [Standard.builder],
 * [Daylight.builder] or [Observance.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone.ObservanceBuilder")))
@interface CalendrumCoreVTimeZoneObservanceBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVTimeZoneObservanceBuilder *>

/** Creates the observance: a [Standard] or a [Daylight], as the builder was made for. */
- (CalendrumCoreVTimeZoneObservance *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds a `TZNAME` property with the text [text]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/** Sets [offsetFrom] to [value] and returns this builder; `null` removes the property. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)offsetFromValue:(CalendrumCoreKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetFrom(value:)")));

/** Sets [offsetTo] to [value] and returns this builder; `null` removes the property. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)offsetToValue:(CalendrumCoreKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetTo(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)rdateDates:(CalendrumCoreKotlinArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)rdatePeriods:(CalendrumCoreKotlinArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/** Adds [relatedTo] as a `RELATED-TO` property. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/** Adds a `RELATED-TO` property naming the component with the given [uid]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Sets [rrule] to [value] and returns this builder; `null` removes the property. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)rruleValue:(CalendrumCoreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)rruleFrequency:(CalendrumCoreFrequency *)frequency block:(void (^)(CalendrumCoreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/** Sets [start] to [value] and returns this builder; `null` removes the property. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)startValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * The offset before the observance being built (`TZOFFSETFROM`); assigning `null` removes the property.
 */
@property CalendrumCoreKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The offset the observance being built puts in force (`TZOFFSETTO`); assigning `null` removes the
 * property.
 */
@property CalendrumCoreKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The recurrence rule of the observance being built (`RRULE`); assigning `null` removes the property. */
@property CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/** The local start (`DTSTART`) of the observance being built; assigning `null` removes the property. */
@property id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));
@end


/** An observance of standard time (`STANDARD`). */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone.Standard")))
@interface CalendrumCoreVTimeZoneStandard : CalendrumCoreVTimeZoneObservance
@property (class, readonly, getter=companion) CalendrumCoreVTimeZoneStandardCompanion *companion __attribute__((swift_name("companion")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** Creates `STANDARD` observances. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZone.StandardCompanion")))
@interface CalendrumCoreVTimeZoneStandardCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates `STANDARD` observances. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVTimeZoneStandardCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder for a `STANDARD` observance. */
- (CalendrumCoreVTimeZoneObservanceBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Wraps [component] as a [Standard], or returns `null` when it is not a `STANDARD` component.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVTimeZoneStandard * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * A `VTODO` component: a task with an optional due date
 * ([RFC 5545 §3.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.2)).
 *
 * A typed view over an [ICalComponent]: every accessor decodes its property on first use, caches the result in the tree
 * and gives `null` (or an empty list) for a missing or broken value; read `component.decode(key)` to learn why. The
 * view is immutable and shares the tree. To change it use [edit] or [toBuilder], which return new copies that share
 * every node they do not touch.
 *
 * @property component the underlying tree node.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTodo")))
@interface CalendrumCoreVTodo : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreVTodoCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this to-do changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this to-do.
 *
 * @param block changes the builder.
 */
- (CalendrumCoreVTodo *)editBlock:(void (^)(CalendrumCoreVTodoBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTodo with an equal VTODO component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this to-do; unchanged nodes are shared with it. */
- (CalendrumCoreVTodoBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this to-do and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVTodoBuilder *)toBuilderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumCoreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the to-do (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the to-do (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The categories of the to-do (`CATEGORIES` properties,
 * [RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *categories __attribute__((swift_name("categories")));

/**
 * The access classification of the to-do (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreClassification * _Nullable classification __attribute__((swift_name("classification")));

/**
 * The color of the to-do (`COLOR`, [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), or `null`
 * when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable color __attribute__((swift_name("color")));

/**
 * The comments of the to-do (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));

/**
 * The completion time of the to-do (`COMPLETED`,
 * [RFC 5545 §3.8.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.1)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts of the to-do (`CONCEPT` properties,
 * [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The conferences of the to-do (`CONFERENCE` properties,
 * [RFC 7986 §5.11](https://www.rfc-editor.org/rfc/rfc7986#section-5.11)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumCoreConference *> *conferences __attribute__((swift_name("conferences")));

/**
 * The contacts of the to-do (`CONTACT` properties,
 * [RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *contacts __attribute__((swift_name("contacts")));

/**
 * The creation time of the to-do (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The description of the to-do (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The due time of the to-do (`DUE`, [RFC 5545 §3.8.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.3)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The excluded dates of the to-do (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the to-do (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the to-do (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumCoreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the to-do (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the to-do (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreLink *> *links __attribute__((swift_name("links")));

/** The location nested in the to-do, in source order. */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the to-do (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the to-do (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/**
 * The reference identifiers of the to-do (`REFID` properties,
 * [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships to other components of the to-do (`RELATED-TO` properties,
 * [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the to-do (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumCoreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The resources of the to-do (`RESOURCES` properties,
 * [RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)) from all properties in source
 * order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<NSString *> *resources __attribute__((swift_name("resources")));

/**
 * The recurrence rule of the to-do (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumCoreTodoStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the to-do (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the to-do (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumCoreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

/**
 * The summary, the short title of the to-do (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The globally unique identifier of the to-do (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the to-do (`URL`, [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable url __attribute__((swift_name("url")));
@end


/**
 * Collects the properties and nested components of a new or edited to-do, with a Kotlin DSL and chained calls. Get
 * one from [VTodo.builder] or [VTodo.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTodo.Builder")))
@interface CalendrumCoreVTodoBuilder : CalendrumCoreComponentBuilder<CalendrumCoreVTodoBuilder *>

/** Adds [alarm] as a alarm of the to-do. */
- (CalendrumCoreVTodoBuilder *)alarmAlarm:(CalendrumCoreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumCoreVTodoBuilder *)alarmBlock:(void (^)(CalendrumCoreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the to-do in [block]: add, replace or remove them. */
- (CalendrumCoreVTodoBuilder *)alarmsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumCoreVTodoBuilder *)attachAttach:(id<CalendrumCoreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumCoreVTodoBuilder *)attendeeAttendee:(CalendrumCoreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumCoreVTodoBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumCoreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumCoreVTodoBuilder *)attendeesBlock:(void (^)(CalendrumCoreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the to-do.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumCoreVTodo *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumCoreVTodoBuilder *)categoriesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)classificationValue:(CalendrumCoreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumCoreVTodoBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/**
 * Sets [completed] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)completedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("completed(value:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumCoreVTodoBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumCoreVTodoBuilder *)conferenceValue:(CalendrumCoreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumCoreVTodoBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)createdValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [due] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)dueValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("due(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)durationValue:(CalendrumCoreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumCoreVTodoBuilder *)exdateDates:(CalendrumCoreKotlinArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)geoValue:(CalendrumCoreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumCoreVTodoBuilder *)imageValue:(CalendrumCoreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)lastModifiedValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumCoreVTodoBuilder *)linkValue:(CalendrumCoreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the to-do. */
- (CalendrumCoreVTodoBuilder *)locationValue:(CalendrumCoreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumCoreVTodoBuilder *)locationBlock:(void (^)(CalendrumCoreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the to-do in [block]: add, replace or remove them. */
- (CalendrumCoreVTodoBuilder *)locationsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)organizerValue:(CalendrumCoreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumCoreVTodoBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumCoreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the to-do. */
- (CalendrumCoreVTodoBuilder *)participantValue:(CalendrumCoreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumCoreVTodoBuilder *)participantBlock:(void (^)(CalendrumCoreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the to-do in [block]: add, replace or remove them. */
- (CalendrumCoreVTodoBuilder *)participantsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [percentComplete] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)percentCompleteValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("percentComplete(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)priorityValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumCoreVTodoBuilder *)rdateDates:(CalendrumCoreKotlinArray<id<CalendrumCoreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumCoreVTodoBuilder *)rdatePeriods:(CalendrumCoreKotlinArray<CalendrumCoreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)recurrenceIdValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumCoreVTodoBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVTodoBuilder *)relatedToRelatedTo:(CalendrumCoreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumCoreVTodoBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumCoreVTodoBuilder *)requestStatusStatus:(CalendrumCoreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the to-do. */
- (CalendrumCoreVTodoBuilder *)resourceComponentValue:(CalendrumCoreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumCoreVTodoBuilder *)resourceComponentBlock:(void (^)(CalendrumCoreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the to-do in [block]: add, replace or remove them. */
- (CalendrumCoreVTodoBuilder *)resourceComponentsBlock:(void (^)(CalendrumCoreComponentList<CalendrumCoreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumCoreVTodoBuilder *)resourcesValues:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)rruleValue:(CalendrumCoreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumCoreVTodoBuilder *)rruleFrequency:(CalendrumCoreFrequency *)frequency block:(void (^)(CalendrumCoreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)sequenceValue:(CalendrumCoreInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)stampValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)startValue:(id<CalendrumCoreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)statusValue:(CalendrumCoreTodoStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumCoreVTodoBuilder *)structuredDataValue:(CalendrumCoreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumCoreVTodoBuilder *)styledDescriptionValue:(CalendrumCoreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumCoreVTodoBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the to-do being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumCoreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the to-do being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreClassification * _Nullable classification __attribute__((swift_name("classification")));

/**
 * The color of the to-do being built (`COLOR`,
 * [RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)); assigning `null` removes the property.
 */
@property NSString * _Nullable color __attribute__((swift_name("color")));

/**
 * The completion time of the to-do being built (`COMPLETED`,
 * [RFC 5545 §3.8.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));

/**
 * The creation time of the to-do being built (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The description of the to-do being built (`DESCRIPTION`,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable descriptionText __attribute__((swift_name("descriptionText")));

/**
 * The due time of the to-do being built (`DUE`,
 * [RFC 5545 §3.8.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do being built (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The geographic position of the to-do being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the to-do being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the to-do being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the to-do being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the to-do being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do being built (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the to-do being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the to-do being built, as an editable list. */
@property (readonly) CalendrumCoreComponentList<CalendrumCoreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the to-do being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumCoreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumCoreTodoStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The summary, the short title of the to-do being built (`SUMMARY`,
 * [RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable summary __attribute__((swift_name("summary")));

/**
 * The globally unique identifier of the to-do being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));

/**
 * The URL of the to-do being built (`URL`,
 * [RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable url __attribute__((swift_name("url")));
@end


/** Creates VTodo values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTodo.Companion")))
@interface CalendrumCoreVTodoCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTodo values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVTodoCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumCoreVTodoBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumCoreVTodoBuilder *)builderDefaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTodo, or returns `null` when it is not a `VTODO` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumCoreVTodo * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * The data type of a property value ([RFC 5545 §3.2.20](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.20),
 * `VALUE`, and [RFC 5545 §3.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.3)).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ValueType")))
@interface CalendrumCoreValueType : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreValueTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ValueType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of ValueType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ValueType.Companion")))
@interface CalendrumCoreValueTypeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ValueType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreValueTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ValueType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumCoreValueType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BINARY`. Base64 encoded binary data. */
@property (readonly) CalendrumCoreValueType *Binary __attribute__((swift_name("Binary")));

/** `BOOLEAN`. `TRUE` or `FALSE`. */
@property (readonly) CalendrumCoreValueType *Boolean __attribute__((swift_name("Boolean")));

/** `CAL-ADDRESS`. A calendar user address. */
@property (readonly) CalendrumCoreValueType *CalAddress __attribute__((swift_name("CalAddress")));

/** `DATE`. A calendar date. */
@property (readonly) CalendrumCoreValueType *Date __attribute__((swift_name("Date")));

/** `DATE-TIME`. A date with a time of day. */
@property (readonly) CalendrumCoreValueType *DateTime __attribute__((swift_name("DateTime")));

/** `DURATION`. A length of time. */
@property (readonly) CalendrumCoreValueType *Duration __attribute__((swift_name("Duration")));

/** `FLOAT`. A decimal number. */
@property (readonly) CalendrumCoreValueType *Float __attribute__((swift_name("Float")));

/** `INTEGER`. A signed 32-bit integer. */
@property (readonly) CalendrumCoreValueType *Integer __attribute__((swift_name("Integer")));

/** `PERIOD`. A precise period of time. */
@property (readonly) CalendrumCoreValueType *Period __attribute__((swift_name("Period")));

/** `RECUR`. A recurrence rule. */
@property (readonly) CalendrumCoreValueType *Recur __attribute__((swift_name("Recur")));

/** `TEXT`. Human-readable text. */
@property (readonly) CalendrumCoreValueType *Text __attribute__((swift_name("Text")));

/** `TIME`. A time of day. */
@property (readonly) CalendrumCoreValueType *Time __attribute__((swift_name("Time")));

/** `UID`. A `UID` reference (RFC 9253). */
@property (readonly) CalendrumCoreValueType *Uid __attribute__((swift_name("Uid")));

/** `URI`. A uniform resource identifier. */
@property (readonly) CalendrumCoreValueType *Uri __attribute__((swift_name("Uri")));

/** `UTC-OFFSET`. An offset from UTC. */
@property (readonly) CalendrumCoreValueType *UtcOffset __attribute__((swift_name("UtcOffset")));

/** `XML-REFERENCE`. A reference to an XML element (RFC 9073). */
@property (readonly) CalendrumCoreValueType *XmlReference __attribute__((swift_name("XmlReference")));
@end


/**
 * The definitions of frequent vendor extensions (`X-` properties) of Microsoft, Apple, Google, Mozilla, libical and
 * Yandex, so that eager decoding and strict mode know them. They are not part of the default registry: install them
 * with `ICalRegistry.builder().install(Rfc5545).install(VendorProperties)`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VendorProperties")))
@interface CalendrumCoreVendorProperties : CalendrumCoreBase <CalendrumCoreICalExtension>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The definitions of frequent vendor extensions (`X-` properties) of Microsoft, Apple, Google, Mozilla, libical and
 * Yandex, so that eager decoding and strict mode know them. They are not part of the default registry: install them
 * with `ICalRegistry.builder().install(Rfc5545).install(VendorProperties)`.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)vendorProperties __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreVendorProperties *shared __attribute__((swift_name("shared")));

/** Adds the definitions of the vendor properties to [registry]. */
- (void)contributeRegistry:(CalendrumCoreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The key of `X-ALT-DESC`: an alternative description, usually HTML, written by Outlook. Untrusted markup. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreStyledDescription *> *ALT_DESC __attribute__((swift_name("ALT_DESC")));

/** The key of `X-APPLE-CALENDAR-COLOR`: the color of the calendar in Apple Calendar. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *APPLE_CALENDAR_COLOR __attribute__((swift_name("APPLE_CALENDAR_COLOR")));

/** The key of `X-APPLE-DEFAULT-ALARM`: whether an alarm is the default one of Apple Calendar. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreBoolean *> *APPLE_DEFAULT_ALARM __attribute__((swift_name("APPLE_DEFAULT_ALARM")));

/** The key of `X-APPLE-STRUCTURED-LOCATION`: a `geo:` URI with the position of the location. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *APPLE_STRUCTURED_LOCATION __attribute__((swift_name("APPLE_STRUCTURED_LOCATION")));

/** The key of `X-APPLE-TRAVEL-ADVISORY-BEHAVIOR`: whether Apple Calendar adds travel time. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *APPLE_TRAVEL_ADVISORY_BEHAVIOR __attribute__((swift_name("APPLE_TRAVEL_ADVISORY_BEHAVIOR")));

/** The key of `X-GOOGLE-CONFERENCE`: the link to a Google Meet conference. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *GOOGLE_CONFERENCE __attribute__((swift_name("GOOGLE_CONFERENCE")));

/** The key of `X-LIC-LOCATION`: the name of the zone, written by libical in a `VTIMEZONE`. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *LIC_LOCATION __attribute__((swift_name("LIC_LOCATION")));

/** The key of `X-MICROSOFT-CDO-ALLDAYEVENT`: whether Outlook treats the event as an all-day event. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreBoolean *> *MICROSOFT_CDO_ALLDAYEVENT __attribute__((swift_name("MICROSOFT_CDO_ALLDAYEVENT")));

/** The key of `X-MICROSOFT-CDO-BUSYSTATUS`: how Outlook shows the time of the event. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreBusyStatus *> *MICROSOFT_CDO_BUSYSTATUS __attribute__((swift_name("MICROSOFT_CDO_BUSYSTATUS")));

/** The key of `X-MICROSOFT-CDO-IMPORTANCE`: the importance of the event, as an integer. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreInt *> *MICROSOFT_CDO_IMPORTANCE __attribute__((swift_name("MICROSOFT_CDO_IMPORTANCE")));

/** The key of `X-MICROSOFT-CDO-INTENDEDSTATUS`: the busy status the organizer intends for the attendees. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreBusyStatus *> *MICROSOFT_CDO_INTENDEDSTATUS __attribute__((swift_name("MICROSOFT_CDO_INTENDEDSTATUS")));

/** The key of `X-MICROSOFT-CDO-OWNERAPPTID`: the identifier of the appointment in the owner's mailbox. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *MICROSOFT_CDO_OWNERAPPTID __attribute__((swift_name("MICROSOFT_CDO_OWNERAPPTID")));

/** The key of `X-MICROSOFT-DISALLOW-COUNTER`: whether attendees may not propose a new time. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreBoolean *> *MICROSOFT_DISALLOW_COUNTER __attribute__((swift_name("MICROSOFT_DISALLOW_COUNTER")));

/** The key of `X-MOZ-GENERATION`: the revision counter of a Mozilla Thunderbird item. */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreInt *> *MOZ_GENERATION __attribute__((swift_name("MOZ_GENERATION")));

/** The key of `X-MOZ-LASTACK`: the time Thunderbird last acknowledged the item's alarms. */
@property (readonly) CalendrumCorePropertyKey<id<CalendrumCoreICalDateTime>> *MOZ_LASTACK __attribute__((swift_name("MOZ_LASTACK")));

/**
 * The key of `X-PUBLISHED-TTL`: how often a subscriber should refresh; the standard form is `REFRESH-INTERVAL`.
 */
@property (readonly) CalendrumCorePropertyKey<CalendrumCoreICalDuration *> *PUBLISHED_TTL __attribute__((swift_name("PUBLISHED_TTL")));

/**
 * The key of `X-WR-CALDESC`: the calendar description, written by Apple and Google; the standard form is
 * `DESCRIPTION`.
 */
@property (readonly) CalendrumCorePropertyKey<NSString *> *WR_CALDESC __attribute__((swift_name("WR_CALDESC")));

/** The key of `X-WR-CALNAME`: the calendar name, written by Apple and Google; the standard form is `NAME`. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *WR_CALNAME __attribute__((swift_name("WR_CALNAME")));

/** The key of `X-WR-TIMEZONE`: the default time zone of the calendar, written by Apple and Google. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *WR_TIMEZONE __attribute__((swift_name("WR_TIMEZONE")));

/** The key of `X-YANDEX-VISIBILITY`: the visibility of an event in Yandex Calendar. */
@property (readonly) CalendrumCorePropertyKey<NSString *> *YANDEX_VISIBILITY __attribute__((swift_name("YANDEX_VISIBILITY")));
@end


/**
 * The moment an alarm of an occurrence fires.
 *
 * @property at the instant of the alarm.
 * @property alarm the `VALARM` it comes from.
 * @property repetition 0 for the first firing, 1 and up for the `REPEAT` repetitions.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AlarmTime")))
@interface CalendrumCoreAlarmTime : CalendrumCoreBase
- (instancetype)initWithAt:(CalendrumCoreKotlinInstant *)at alarm:(CalendrumCoreVAlarm *)alarm repetition:(int32_t)repetition __attribute__((swift_name("init(at:alarm:repetition:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same time, alarm and repetition number. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the time, the alarm and the repetition. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the instant and the repetition number. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreVAlarm *alarm __attribute__((swift_name("alarm")));
@property (readonly) CalendrumCoreKotlinInstant *at __attribute__((swift_name("at")));
@property (readonly) int32_t repetition __attribute__((swift_name("repetition")));
@end


/**
 * A period during which someone is busy, in absolute time.
 *
 * @property start the start of the interval.
 * @property end the end of the interval, after [start].
 * @property type how busy: `BUSY`, `BUSY-TENTATIVE` or `BUSY-UNAVAILABLE`.
 * @throws IllegalArgumentException if [end] is not after [start].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BusyInterval")))
@interface CalendrumCoreBusyInterval : CalendrumCoreBase
- (instancetype)initWithStart:(CalendrumCoreKotlinInstant *)start end:(CalendrumCoreKotlinInstant *)end type:(CalendrumCoreFbType *)type __attribute__((swift_name("init(start:end:type:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same start, end and type. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the start, the end and the type. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the interval and its type, such as `BusyInterval(2026-10-01T07:00:00Z..2026-10-01T08:00:00Z BUSY)`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinInstant *end __attribute__((swift_name("end")));
@property (readonly) CalendrumCoreKotlinInstant *start __attribute__((swift_name("start")));
@property (readonly) CalendrumCoreFbType *type __attribute__((swift_name("type")));
@end


/**
 * Two occurrences that overlap in time.
 *
 * @property first the occurrence that started first.
 * @property second the occurrence that overlaps it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Conflict")))
@interface CalendrumCoreConflict : CalendrumCoreBase

/** Returns the start and the end of the overlap. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The end of the overlap: the earlier of the two ends. */
@property (readonly) CalendrumCoreKotlinInstant *end __attribute__((swift_name("end")));
@property (readonly) CalendrumCoreOccurrence *first __attribute__((swift_name("first")));
@property (readonly) CalendrumCoreOccurrence *second __attribute__((swift_name("second")));

/** The start of the overlap: the later of the two starts. */
@property (readonly) CalendrumCoreKotlinInstant *start __attribute__((swift_name("start")));
@end


/**
 * Computes busy time, conflicts and `VFREEBUSY` from calendars.
 *
 * Only events that block time count: not cancelled, not `TRANSP:TRANSPARENT` and with a duration. Tentative events are
 * `BUSY-TENTATIVE`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FreeBusyCalc")))
@interface CalendrumCoreFreeBusyCalc : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Computes busy time, conflicts and `VFREEBUSY` from calendars.
 *
 * Only events that block time count: not cancelled, not `TRANSP:TRANSPARENT` and with a duration. Tentative events are
 * `BUSY-TENTATIVE`.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)freeBusyCalc __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreFreeBusyCalc *shared __attribute__((swift_name("shared")));

/**
 * Returns the busy intervals of [calendars] within the range from [from] up to, excluding, [to], merged and sorted.
 *
 * Events of every calendar are expanded with [options]. The `FREEBUSY` periods of their `VFREEBUSY` components are
 * added. Where intervals of different types overlap, the more binding type wins: busy, then unavailable, then
 * tentative.
 *
 * @param calendars the calendars to combine.
 * @param from the start of the range.
 * @param to the end of the range, excluded.
 * @param options zones, floating zone and the other occurrence options.
 */
- (NSArray<CalendrumCoreBusyInterval *> *)busyCalendars:(NSArray<CalendrumCoreVCalendar *> *)calendars from:(CalendrumCoreKotlinInstant *)from to:(CalendrumCoreKotlinInstant *)to options:(CalendrumCoreOccurrenceOptions *)options __attribute__((swift_name("busy(calendars:from:to:options:)")));

/**
 * Returns the pairs of blocking occurrences of [calendars] that overlap within the range, in the order of the
 * second occurrence's start.
 *
 * The time and the memory are proportional to the number of conflicts: k occurrences that overlap each other give
 * k(k - 1)/2 pairs, so bound the range or the calendars when they come from strangers. The shape of the answer (a
 * limit on the number of pairs, or a lazy sequence) may still change in a minor release.
 *
 * @param calendars the calendars to combine.
 * @param from the start of the range.
 * @param to the end of the range, excluded.
 * @param options zones, floating zone and the other occurrence options.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (NSArray<CalendrumCoreConflict *> *)conflictsCalendars:(NSArray<CalendrumCoreVCalendar *> *)calendars from:(CalendrumCoreKotlinInstant *)from to:(CalendrumCoreKotlinInstant *)to options:(CalendrumCoreOccurrenceOptions *)options __attribute__((swift_name("conflicts(calendars:from:to:options:)")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Creates a `VFREEBUSY` component for the range from [from] to [to] listing [busy] as `FREEBUSY` periods in UTC,
 * grouped by type.
 *
 * @param busy the busy intervals, merged before use.
 * @param from the start of the range.
 * @param to the end of the range.
 * @param defaults supplies `UID` and `DTSTAMP`.
 */
- (CalendrumCoreVFreeBusy *)toVFreeBusyBusy:(NSArray<CalendrumCoreBusyInterval *> *)busy from:(CalendrumCoreKotlinInstant *)from to:(CalendrumCoreKotlinInstant *)to defaults:(CalendrumCoreBuildDefaults *)defaults __attribute__((swift_name("toVFreeBusy(busy:from:to:defaults:)")));
@end


/**
 * One instance of a component in time: a single event, one instance of a recurring event or an override, with absolute
 * start and end.
 *
 * Both ends are instants, computed with the time zones of the options. For a component without an end the end is
 * derived by the rules of [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2): `DTEND`,
 * `DTSTART` plus `DURATION`, a day for a date, or the start itself. Immutable.
 *
 * @property start the instant the occurrence starts.
 * @property end the instant it ends; equal to [start] for a point in time.
 * @property startLocal the start as the component states it, in its own form.
 * @property recurrenceId the `RECURRENCE-ID` of an instance of a recurring series, or `null` for a single component.
 * @property component the component this occurrence comes from; for an override, the override.
 * @property isCancelled whether the instance is cancelled (`STATUS:CANCELLED`); only shown when the options include
 *   cancelled ones.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Occurrence")))
@interface CalendrumCoreOccurrence : CalendrumCoreBase

/** Returns `true` when [other] has the same start, end, component, recurrence id and cancellation flag. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the start, the end and the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the component name and the start and end instants. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The alarms of this occurrence with the absolute time of each, repetitions included up to
 * [OccurrenceOptions.maxAlarmRepetitions]; computed on first use.
 */
@property (readonly) NSArray<CalendrumCoreAlarmTime *> *alarms __attribute__((swift_name("alarms")));
@property (readonly) CalendrumCoreICalComponent *component __attribute__((swift_name("component")));
@property (readonly) CalendrumCoreKotlinInstant *end __attribute__((swift_name("end")));

/** Whether the occurrence starts on a date and not at a time of day. */
@property (readonly) BOOL isAllDay __attribute__((swift_name("isAllDay")));
@property (readonly) BOOL isCancelled __attribute__((swift_name("isCancelled")));
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));
@property (readonly) CalendrumCoreKotlinInstant *start __attribute__((swift_name("start")));
@property (readonly) id<CalendrumCoreICalDateTime> startLocal __attribute__((swift_name("startLocal")));
@end


/**
 * A random-access view over the occurrences of a calendar, made by `calendar.occurrences(options)`.
 *
 * The cursor computes occurrences on demand and does not hold them. It is not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OccurrenceCursor")))
@interface CalendrumCoreOccurrenceCursor : CalendrumCoreBase

/**
 * Returns the first occurrence that starts after [instant], or `null`.
 *
 * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreOccurrence * _Nullable)afterInstant:(CalendrumCoreKotlinInstant *)instant error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("after(instant:)"))) __attribute__((swift_error(nonnull_error)));

/**
 * Returns the occurrences that are in progress at [instant]: those that start at or before it and end after it (a
 * point occurrence counts at its start).
 *
 * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSArray<CalendrumCoreOccurrence *> * _Nullable)atInstant:(CalendrumCoreKotlinInstant *)instant error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("at(instant:)")));

/**
 * Returns the next occurrence in the order of the start, or `null` when there are no more. Successive calls walk
 * the calendar from its beginning.
 *
 * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreOccurrence * _Nullable)nextAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("next()"))) __attribute__((swift_error(nonnull_error)));

/**
 * Returns up to [size] occurrences that start after [after], or from the beginning when it is `null`; occurrences
 * with the same start as the last one are included, so a page never splits them.
 *
 * @param size how many occurrences to return, positive.
 * @param after the instant after which to start, or `null`.
 * @throws IllegalArgumentException if [size] is not positive.
 * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSArray<CalendrumCoreOccurrence *> * _Nullable)pageSize:(int32_t)size after:(CalendrumCoreKotlinInstant * _Nullable)after error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("page(size:after:)")));

/** Returns the name of the class. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * Settings of occurrence computation: time zones, the zone of floating times, recurrence options, cancelled instances,
 * `THISANDFUTURE` overrides, the alarm repetition limit and a problem listener.
 *
 * Immutable and thread-safe. The problems that can be reported are `ICAL-O001` to `ICAL-O005`, `ICAL-Z001` to
 * `ICAL-Z004` and `ICAL-R001` to `ICAL-R009`.
 *
 * @property zones resolves `TZID` values, or `null` for the default: the `VTIMEZONE` components of the calendar and
 *   their aliases.
 * @property floatingZone the zone in which floating times and dates are read, or `null` for none (their occurrences are
 *   then skipped unless [floatingICalZone] is set).
 * @property floatingICalZone the same as a zone of this library, or `null`. It needs no time zone database of the platform,
 *   which Kotlin/JS does not have: a zone from `TzData.resolver` works everywhere. At most one of the two is set.
 * @property recurrence options of the expansion of recurrence rules.
 * @property includeCancelled whether cancelled instances are returned (with `isCancelled` set); false by default.
 * @property thisAndFuture whether `RANGE=THISANDFUTURE` overrides apply to later instances; true by default.
 * @property maxAlarmRepetitions the most repetitions of one alarm that are returned; 100 by default.
 * @property problemListener receives the problems, or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OccurrenceOptions")))
@interface CalendrumCoreOccurrenceOptions : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreOccurrenceOptionsCompanion *companion __attribute__((swift_name("companion")));

/** Returns the settings, with the zone resolver and the floating zone by description. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumCoreICalTimeZone> _Nullable floatingICalZone __attribute__((swift_name("floatingICalZone")));
@property (readonly) CalendrumCoreKotlinx_datetimeTimeZone * _Nullable floatingZone __attribute__((swift_name("floatingZone")));
@property (readonly) BOOL includeCancelled __attribute__((swift_name("includeCancelled")));
@property (readonly) int32_t maxAlarmRepetitions __attribute__((swift_name("maxAlarmRepetitions")));
@property (readonly) id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));
@property (readonly) CalendrumCoreRecurrenceOptions *recurrence __attribute__((swift_name("recurrence")));
@property (readonly) BOOL thisAndFuture __attribute__((swift_name("thisAndFuture")));
@property (readonly) id<CalendrumCoreTimeZoneResolver> _Nullable zones __attribute__((swift_name("zones")));
@end


/**
 * Collects [OccurrenceOptions] values. The properties and the functions of the same name do the same. Not
 * thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OccurrenceOptions.Builder")))
@interface CalendrumCoreOccurrenceOptionsBuilder : CalendrumCoreBase

/**
 * Creates the options.
 *
 * @throws IllegalArgumentException if [maxAlarmRepetitions] is negative, or if both [floatingZone] and
 *   [floatingICalZone] are set.
 */
- (CalendrumCoreOccurrenceOptions *)build __attribute__((swift_name("build()")));

/** Sets [floatingICalZone] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)floatingICalZoneValue:(id<CalendrumCoreICalTimeZone> _Nullable)value __attribute__((swift_name("floatingICalZone(value:)")));

/** Sets [floatingZone] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)floatingZoneValue:(CalendrumCoreKotlinx_datetimeTimeZone * _Nullable)value __attribute__((swift_name("floatingZone(value:)")));

/** Sets [includeCancelled] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)includeCancelledValue:(BOOL)value __attribute__((swift_name("includeCancelled(value:)")));

/** Sets [maxAlarmRepetitions] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)maxAlarmRepetitionsValue:(int32_t)value __attribute__((swift_name("maxAlarmRepetitions(value:)")));

/** Sets [problemListener] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)problemListenerValue:(id<CalendrumCoreProblemListener> _Nullable)value __attribute__((swift_name("problemListener(value:)")));

/** Sets [recurrence] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)recurrenceValue:(CalendrumCoreRecurrenceOptions *)value __attribute__((swift_name("recurrence(value:)")));

/** Sets [thisAndFuture] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)thisAndFutureValue:(BOOL)value __attribute__((swift_name("thisAndFuture(value:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Sets [zones] to [value]. */
- (CalendrumCoreOccurrenceOptionsBuilder *)zonesValue:(id<CalendrumCoreTimeZoneResolver> _Nullable)value __attribute__((swift_name("zones(value:)")));

/**
 * The zone for floating times and dates as a zone of this library, such as one from `TzData.resolver`; `null` by
 * default. Unlike [floatingZone] it needs no time zone database of the platform.
 */
@property id<CalendrumCoreICalTimeZone> _Nullable floatingICalZone __attribute__((swift_name("floatingICalZone")));

/** The zone for floating times and dates, such as the user's zone; `null` by default. */
@property CalendrumCoreKotlinx_datetimeTimeZone * _Nullable floatingZone __attribute__((swift_name("floatingZone")));

/** Return cancelled instances too; false by default. */
@property BOOL includeCancelled __attribute__((swift_name("includeCancelled")));

/** The most repetitions of one alarm; zero or more, 100 by default. */
@property int32_t maxAlarmRepetitions __attribute__((swift_name("maxAlarmRepetitions")));

/** Receives the problems; `null` by default. */
@property id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/** Options for expanding recurrence rules; the default options by default. */
@property CalendrumCoreRecurrenceOptions *recurrence __attribute__((swift_name("recurrence")));

/** Apply `RANGE=THISANDFUTURE` overrides to later instances; true by default. */
@property BOOL thisAndFuture __attribute__((swift_name("thisAndFuture")));

/**
 * Resolves `TZID` values; `null` (the default) means the `VTIMEZONE` components of the calendar and their
 * aliases.
 */
@property id<CalendrumCoreTimeZoneResolver> _Nullable zones __attribute__((swift_name("zones")));
@end


/** The default options and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OccurrenceOptions.Companion")))
@interface CalendrumCoreOccurrenceOptionsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default options and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreOccurrenceOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the default options. */
- (CalendrumCoreOccurrenceOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * No zone resolver and no floating zone, default recurrence options, cancelled instances left out,
 * `THISANDFUTURE` applied and at most 100 alarm repetitions.
 */
@property (readonly) CalendrumCoreOccurrenceOptions *Default __attribute__((swift_name("Default")));
@end


/**
 * A set of occurrences, ordered by start and end, that supports set algebra: union, intersection and difference by
 * overlap.
 *
 * Use it to combine schedules, for example the lessons minus the holidays. Immutable.
 *
 * @property occurrences the occurrences in order.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OccurrenceSet")))
@interface CalendrumCoreOccurrenceSet : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreOccurrenceSetCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] holds equal occurrences in the same order. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the occurrences. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the occurrences of this set that overlap an occurrence of [other] in time. */
- (CalendrumCoreOccurrenceSet *)intersectOther:(CalendrumCoreOccurrenceSet *)other __attribute__((swift_name("intersect(other:)")));

/** Returns the occurrences of this set that overlap no occurrence of [other] in time. */
- (CalendrumCoreOccurrenceSet *)minusOther:(CalendrumCoreOccurrenceSet *)other __attribute__((swift_name("minus(other:)")));

/** Returns the number of occurrences. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Returns the occurrences of both sets, without duplicates. */
- (CalendrumCoreOccurrenceSet *)unionOther:(CalendrumCoreOccurrenceSet *)other __attribute__((swift_name("union(other:)")));
@property (readonly) NSArray<CalendrumCoreOccurrence *> *occurrences __attribute__((swift_name("occurrences")));
@end


/** Creates sets. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OccurrenceSet.Companion")))
@interface CalendrumCoreOccurrenceSetCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates sets. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreOccurrenceSetCompanion *shared __attribute__((swift_name("shared")));

/** Creates a set of [occurrences], sorted by start and end. */
- (CalendrumCoreOccurrenceSet *)ofOccurrences:(id)occurrences __attribute__((swift_name("of(occurrences:)")));
@end


/**
 * The version of a component by the rules of [RFC 5546 §2.1.5](https://www.rfc-editor.org/rfc/rfc5546#section-2.1.5):
 * the higher `SEQUENCE` wins, and with equal sequences the later timestamp.
 *
 * @property sequence the `SEQUENCE`, 0 when absent.
 * @property modified the `LAST-MODIFIED` or `DTSTAMP` in UTC, or `null` when there is none.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Revision")))
@interface CalendrumCoreRevision : CalendrumCoreBase <CalendrumCoreKotlinComparable>
- (instancetype)initWithSequence:(int32_t)sequence modified:(CalendrumCoreKotlinInstant * _Nullable)modified __attribute__((swift_name("init(sequence:modified:)"))) __attribute__((objc_designated_initializer));

/** Compares by [sequence] first, then by [modified]; a missing time is older than any time. */
- (int32_t)compareToOther:(CalendrumCoreRevision *)other __attribute__((swift_name("compareTo(other:)")));

/** Returns `true` when [other] has the same sequence and time. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the sequence and the time. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the sequence and the time. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinInstant * _Nullable modified __attribute__((swift_name("modified")));
@property (readonly) int32_t sequence __attribute__((swift_name("sequence")));
@end


/**
 * Bounds on the work of an expansion, so that a rule such as `FREQ=SECONDLY` or an impossible one cannot run forever.
 *
 * When a bound is reached the sequence simply ends and a problem is reported; nothing is thrown. Immutable.
 *
 * @property maxOccurrences the most dates one expansion produces (`ICAL-R001` when it is reached); a rule with `COUNT`,
 *   which cannot jump to the start of a range, also passes at most this many dates before it.
 * @property maxIterationsWithoutMatch the most consecutive periods without a single date before the expansion gives up
 *   (`ICAL-R002`).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceLimits")))
@interface CalendrumCoreRecurrenceLimits : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRecurrenceLimitsCompanion *companion __attribute__((swift_name("companion")));

/** Returns the two bounds. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t maxIterationsWithoutMatch __attribute__((swift_name("maxIterationsWithoutMatch")));
@property (readonly) int32_t maxOccurrences __attribute__((swift_name("maxOccurrences")));
@end


/**
 * Collects [RecurrenceLimits] values. The properties and the functions of the same name do the same. Not
 * thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceLimits.Builder")))
@interface CalendrumCoreRecurrenceLimitsBuilder : CalendrumCoreBase

/**
 * Creates the limits.
 *
 * @throws IllegalArgumentException if a bound is not positive.
 */
- (CalendrumCoreRecurrenceLimits *)build __attribute__((swift_name("build()")));

/** Sets [maxIterationsWithoutMatch] to [value]. */
- (CalendrumCoreRecurrenceLimitsBuilder *)maxIterationsWithoutMatchValue:(int32_t)value __attribute__((swift_name("maxIterationsWithoutMatch(value:)")));

/** Sets [maxOccurrences] to [value]. */
- (CalendrumCoreRecurrenceLimitsBuilder *)maxOccurrencesValue:(int32_t)value __attribute__((swift_name("maxOccurrences(value:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The most consecutive empty periods; positive. The default is 10,000. */
@property int32_t maxIterationsWithoutMatch __attribute__((swift_name("maxIterationsWithoutMatch")));

/** The most dates one expansion produces; positive. The default is 1,000,000. */
@property int32_t maxOccurrences __attribute__((swift_name("maxOccurrences")));
@end


/** The default limits and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceLimits.Companion")))
@interface CalendrumCoreRecurrenceLimitsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default limits and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRecurrenceLimitsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the default limits. */
- (CalendrumCoreRecurrenceLimitsBuilder *)builder __attribute__((swift_name("builder()")));

/** 1,000,000 dates and 10,000 empty periods in a row. */
@property (readonly) CalendrumCoreRecurrenceLimits *Default __attribute__((swift_name("Default")));
@end


/**
 * Settings of an expansion: limits, the treatment of a `DTSTART` outside the rule, a problem listener and the offset of
 * a UTC `UNTIL`.
 *
 * Immutable and thread-safe. Problems are reported once per expansion and per code, with no limit or counter.
 *
 * @property limits the bounds on the work of one expansion.
 * @property startMismatch what to do with a `DTSTART` that the rule does not produce.
 * @property problemListener receives the problems (`ICAL-R001` to `ICAL-R009`), or `null`.
 * @property untilOffset gives the UTC offset in force at a local time, to compare a UTC `UNTIL` with a zoned `DTSTART`
 *   exactly; `null` compares with the local time of `UNTIL` instead (`ICAL-R006`).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceOptions")))
@interface CalendrumCoreRecurrenceOptions : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRecurrenceOptionsCompanion *companion __attribute__((swift_name("companion")));

/** Returns the settings; the listener and the offset function only as set or none. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreRecurrenceLimits *limits __attribute__((swift_name("limits")));
@property (readonly) id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));
@property (readonly) CalendrumCoreStartMismatch *startMismatch __attribute__((swift_name("startMismatch")));
@property (readonly) CalendrumCoreKotlinx_datetimeUtcOffset *(^ _Nullable untilOffset)(CalendrumCoreKotlinx_datetimeLocalDateTime *) __attribute__((swift_name("untilOffset")));
@end


/**
 * Collects [RecurrenceOptions] values. The properties and the functions of the same name do the same. Not
 * thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceOptions.Builder")))
@interface CalendrumCoreRecurrenceOptionsBuilder : CalendrumCoreBase

/** Creates the options. */
- (CalendrumCoreRecurrenceOptions *)build __attribute__((swift_name("build()")));

/** Sets [limits] to [value]. */
- (CalendrumCoreRecurrenceOptionsBuilder *)limitsValue:(CalendrumCoreRecurrenceLimits *)value __attribute__((swift_name("limits(value:)")));

/** Sets [problemListener] to [value]. */
- (CalendrumCoreRecurrenceOptionsBuilder *)problemListenerValue:(id<CalendrumCoreProblemListener> _Nullable)value __attribute__((swift_name("problemListener(value:)")));

/** Sets [startMismatch] to [value]. */
- (CalendrumCoreRecurrenceOptionsBuilder *)startMismatchValue:(CalendrumCoreStartMismatch *)value __attribute__((swift_name("startMismatch(value:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Sets [untilOffset] to [value]. */
- (CalendrumCoreRecurrenceOptionsBuilder *)untilOffsetValue:(CalendrumCoreKotlinx_datetimeUtcOffset *(^ _Nullable)(CalendrumCoreKotlinx_datetimeLocalDateTime *))value __attribute__((swift_name("untilOffset(value:)")));

/** The bounds on one expansion; [RecurrenceLimits.Default] by default. */
@property CalendrumCoreRecurrenceLimits *limits __attribute__((swift_name("limits")));

/** Receives the problems; `null` (the default) for none. */
@property id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/** What to do with a `DTSTART` outside the rule; [StartMismatch.Rfc] by default. */
@property CalendrumCoreStartMismatch *startMismatch __attribute__((swift_name("startMismatch")));

/** The UTC offset in force at a local time, for a UTC `UNTIL` against a zoned `DTSTART`; `null` by default. */
@property CalendrumCoreKotlinx_datetimeUtcOffset *(^ _Nullable untilOffset)(CalendrumCoreKotlinx_datetimeLocalDateTime *) __attribute__((swift_name("untilOffset")));
@end


/** The default options and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceOptions.Companion")))
@interface CalendrumCoreRecurrenceOptionsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default options and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRecurrenceOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the default options. */
- (CalendrumCoreRecurrenceOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** Default limits, [StartMismatch.Rfc], no listener and no offset function. */
@property (readonly) CalendrumCoreRecurrenceOptions *Default __attribute__((swift_name("Default")));
@end


/**
 * All the dates a component repeats on: its `DTSTART`, the dates its rules produce and its `RDATE`s, less its
 * exceptions ([RFC 5545 §3.8.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5)).
 *
 * Get one from a component with `recurrenceSet` or [of], or build one with [builder]. [iterator] and [between] produce
 * the dates lazily, in order, in local time: `DTSTART` first (subject to `StartMismatch`), then the union of the
 * `RRULE`s and `RDATE`s with duplicates dropped, minus the dates of `EXDATE` and `EXRULE`, which always win. An
 * `EXDATE` that is a `DATE` removes every occurrence on that day. Dates of another form than `DTSTART` are compared by
 * their local time (`ICAL-R009`). An `RDATE` period contributes its start. Limits end the sequence, they never throw.
 * Immutable.
 *
 * @property start the `DTSTART`.
 * @property rules the `RRULE`s.
 * @property exceptionRules the `EXRULE`s, which [RFC 5545](https://www.rfc-editor.org/rfc/rfc5545) deprecates but old
 *   data still has.
 * @property dates the single dates of `RDATE`.
 * @property periods the periods of `RDATE`; each contributes its start.
 * @property exceptions the `EXDATE` dates.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceSet")))
@interface CalendrumCoreRecurrenceSet : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRecurrenceSetCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the dates from [from] up to, excluding, [to], in order; the range is compared by local time.
 *
 * @param from the first moment of the range.
 * @param to the end of the range, excluded.
 * @param options limits, the handling of a `DTSTART` that no rule produces, and the problem listener.
 */
- (id<CalendrumCoreKotlinSequence>)betweenFrom:(id<CalendrumCoreICalDateTime>)from to:(id<CalendrumCoreICalDateTime>)to options:(CalendrumCoreRecurrenceOptions *)options __attribute__((swift_name("between(from:to:options:)")));

/**
 * Returns `true` when [other] is a set with equal start, rules, exception rules, dates, periods and exceptions.
 */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over all parts. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns an iterator over all the dates of the set, in order. It may be endless; see [isFinite].
 *
 * @param options limits, the handling of a `DTSTART` that no rule produces, and the problem listener.
 */
- (id<CalendrumCoreKotlinIterator>)iteratorOptions:(CalendrumCoreRecurrenceOptions *)options __attribute__((swift_name("iterator(options:)")));

/** Returns the number of rules, dates, periods and exceptions; never a date. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *dates __attribute__((swift_name("dates")));
@property (readonly) NSArray<CalendrumCoreRecur *> *exceptionRules __attribute__((swift_name("exceptionRules")));
@property (readonly) NSArray<id<CalendrumCoreICalDateTime>> *exceptions __attribute__((swift_name("exceptions")));

/**
 * Whether every rule ends (has `COUNT` or `UNTIL`), so that iterating the set terminates without hitting a limit. A
 * set without rules is finite.
 */
@property (readonly) BOOL isFinite __attribute__((swift_name("isFinite")));
@property (readonly) NSArray<CalendrumCoreICalPeriod *> *periods __attribute__((swift_name("periods")));
@property (readonly) NSArray<CalendrumCoreRecur *> *rules __attribute__((swift_name("rules")));
@property (readonly) id<CalendrumCoreICalDateTime> start __attribute__((swift_name("start")));
@end


/** Collects the parts of a [RecurrenceSet] by hand. Not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceSet.Builder")))
@interface CalendrumCoreRecurrenceSetBuilder : CalendrumCoreBase

/** Creates the set. */
- (CalendrumCoreRecurrenceSet *)build __attribute__((swift_name("build()")));

/** Adds a single date, as `RDATE` does. */
- (CalendrumCoreRecurrenceSetBuilder *)dateValue:(id<CalendrumCoreICalDateTime>)value __attribute__((swift_name("date(value:)")));

/** Adds an excluded date, as `EXDATE` does. */
- (CalendrumCoreRecurrenceSetBuilder *)exceptionValue:(id<CalendrumCoreICalDateTime>)value __attribute__((swift_name("exception(value:)")));

/** Adds a period, as `RDATE` does; it contributes its start. */
- (CalendrumCoreRecurrenceSetBuilder *)periodValue:(CalendrumCoreICalPeriod *)value __attribute__((swift_name("period(value:)")));

/** Adds an `RRULE`. */
- (CalendrumCoreRecurrenceSetBuilder *)ruleValue:(CalendrumCoreRecur *)value __attribute__((swift_name("rule(value:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** Creates sets. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurrenceSet.Companion")))
@interface CalendrumCoreRecurrenceSetCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates sets. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRecurrenceSetCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for a set that starts at [start]. */
- (CalendrumCoreRecurrenceSetBuilder *)builderStart:(id<CalendrumCoreICalDateTime>)start __attribute__((swift_name("builder(start:)")));

/**
 * Reads the set of a component (`VEVENT`, `VTODO`, `VJOURNAL` or an observance) from its `DTSTART`, `RRULE`,
 * `EXRULE`, `RDATE` and `EXDATE`.
 *
 * Properties that do not decode are left out; their problems are not reported here. Returns `null` when the
 * component has no `DTSTART` or it does not decode.
 *
 * @param component the component to read.
 */
- (CalendrumCoreRecurrenceSet * _Nullable)ofComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * What to do when `DTSTART` is not one of the dates its own rule produces, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined (`ICAL-R003`).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StartMismatch")))
@interface CalendrumCoreStartMismatch : CalendrumCoreKotlinEnum<CalendrumCoreStartMismatch *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * What to do when `DTSTART` is not one of the dates its own rule produces, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined (`ICAL-R003`).
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreStartMismatch *rfc __attribute__((swift_name("rfc")));
@property (class, readonly) CalendrumCoreStartMismatch *exclude __attribute__((swift_name("exclude")));
@property (class, readonly) CalendrumCoreStartMismatch *include __attribute__((swift_name("include")));
+ (CalendrumCoreKotlinArray<CalendrumCoreStartMismatch *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreStartMismatch *> *entries __attribute__((swift_name("entries")));
@end


/**
 * The fingerprint of one component: its key, a hash of its content and where it is in the input.
 *
 * Two fingerprints with the same key and hash describe components with the same content, apart from the ignored
 * properties. Use [io.github.calendrum.syntax.ICalParser.parseComponent] to parse the component at its offsets.
 *
 * @property key the identity of the component.
 * @property hash a 64-bit xxHash64 of the unfolded lines of the component with names in upper case and the ignored
 * properties left out; values and the order of lines matter, folding does not.
 * @property startOffset the offset of the first byte of the component's `BEGIN` line.
 * @property endOffset the offset just after the component's `END` line.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComponentFingerprint")))
@interface CalendrumCoreComponentFingerprint : CalendrumCoreBase

/** Returns `true` when [other] is a ComponentFingerprint equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t endOffset __attribute__((swift_name("endOffset")));
@property (readonly, getter=hash_) int64_t hash __attribute__((swift_name("hash")));
@property (readonly) CalendrumCoreComponentKey *key __attribute__((swift_name("key")));
@property (readonly) int64_t startOffset __attribute__((swift_name("startOffset")));
@end


/**
 * Identifies a component of a feed between two fingerprint runs.
 *
 * A `VTIMEZONE` is identified by its `TZID`, and any other component with a `UID` by the pair of `UID` and
 * `RECURRENCE-ID`, within its calendar. A component with neither is identified by its name and its position among
 * components of that name ([ordinal]). Keys are equal when all their parts are.
 *
 * @property calendarIndex the 0-based index of the `VCALENDAR` the component is in, or -1 outside any calendar.
 * @property name the canonical component name.
 * @property uid the `UID` of the component, or `null`.
 * @property recurrenceId the `RECURRENCE-ID` text, or `null`.
 * @property tzid the `TZID` of a `VTIMEZONE`, or `null`.
 * @property ordinal the position among components of the same name without identity, or -1 when it has one.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComponentKey")))
@interface CalendrumCoreComponentKey : CalendrumCoreBase

/** Returns `true` when [other] is a ComponentKey equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t calendarIndex __attribute__((swift_name("calendarIndex")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@property (readonly) NSString * _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));
@property (readonly) NSString * _Nullable tzid __attribute__((swift_name("tzid")));
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol CalendrumCoreKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end


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
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComponentStream")))
@interface CalendrumCoreComponentStream : CalendrumCoreBase <CalendrumCoreKotlinIterator>

/** Returns the remaining components as a sequence that can be iterated only once. */
- (id<CalendrumCoreKotlinSequence>)asSequence __attribute__((swift_name("asSequence()")));

/**
 * Returns whether another component follows, reading ahead as far as needed.
 *
 * @throws ICalParseException in strict mode, at the first error; later calls rethrow it.
 */
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));

/**
 * Same as [hasNext]; it exists so that Java callers see the checked [ICalParseException].
 *
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)hasNextOrThrowAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("hasNextOrThrow()"))) __attribute__((swift_error(nonnull_error)));

/**
 * Returns the next component and moves on.
 *
 * @throws kotlin.NoSuchElementException if the stream has ended.
 * @throws ICalParseException in strict mode, at the first error.
 */
- (CalendrumCoreICalComponent *)next __attribute__((swift_name("next()")));

/**
 * Same as [next]; it exists so that Java callers see the checked [ICalParseException].
 *
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreICalComponent * _Nullable)nextOrThrowAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("nextOrThrow()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The 0-based index of the `VCALENDAR` the last returned component belongs to, or -1 before the first or for a
 * component outside any calendar.
 */
@property (readonly) int32_t calendarIndex __attribute__((swift_name("calendarIndex")));

/**
 * The properties of the `VCALENDAR` of the last returned component that were read so far: those before it in the
 * input.
 */
@property (readonly) NSArray<CalendrumCoreICalProperty *> *calendarProperties __attribute__((swift_name("calendarProperties")));

/** The problems found so far, in the order they were found; it grows as the stream is consumed. */
@property (readonly) NSArray<CalendrumCoreProblem *> *problems __attribute__((swift_name("problems")));
@end


/** The kind of event an [ICalReader] reports. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ContentEventType")))
@interface CalendrumCoreContentEventType : CalendrumCoreKotlinEnum<CalendrumCoreContentEventType *>
+ (instancetype)alloc __attribute__((unavailable));

/** The kind of event an [ICalReader] reports. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreContentEventType *begin __attribute__((swift_name("begin")));
@property (class, readonly) CalendrumCoreContentEventType *property __attribute__((swift_name("property")));
@property (class, readonly) CalendrumCoreContentEventType *end __attribute__((swift_name("end")));
+ (CalendrumCoreKotlinArray<CalendrumCoreContentEventType *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreContentEventType *> *entries __attribute__((swift_name("entries")));
@end


/**
 * A named group of properties and components that a selection needs for some purpose, so that it need not list them one
 * by one.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Feature")))
@interface CalendrumCoreFeature : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreFeatureCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** The features of [Selection.Builder.need]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Feature.Companion")))
@interface CalendrumCoreFeatureCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The features of [Selection.Builder.need]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreFeatureCompanion *shared __attribute__((swift_name("shared")));

/** The `VALARM` components, whole. */
@property (readonly) CalendrumCoreFeature *Alarms __attribute__((swift_name("Alarms")));

/**
 * Everything needed to compute occurrences: `DTSTART`, `DTEND`, `DURATION`, `RRULE`, `RDATE`, `EXDATE` and
 * `RECURRENCE-ID`. Time zones are then at least [TimeZoneSelection.Referenced].
 */
@property (readonly) CalendrumCoreFeature *Occurrences __attribute__((swift_name("Occurrences")));

/** The `ATTENDEE` and `ORGANIZER` properties. */
@property (readonly) CalendrumCoreFeature *Participants __attribute__((swift_name("Participants")));
@end


/**
 * The difference between two fingerprint runs of a feed, as returned by [Fingerprints.diff].
 *
 * @property unchangedCount how many components have the same key and hash in both runs.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FingerprintDiff")))
@interface CalendrumCoreFingerprintDiff : CalendrumCoreBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Components that are in the newer run only, with their offsets in the newer input. */
@property (readonly) NSArray<CalendrumCoreComponentFingerprint *> *added __attribute__((swift_name("added")));

/** Components with the same key but another hash, with their offsets in the newer input. */
@property (readonly) NSArray<CalendrumCoreComponentFingerprint *> *changed __attribute__((swift_name("changed")));

/** Components that are in the older run only, with their offsets in the older input. */
@property (readonly) NSArray<CalendrumCoreComponentFingerprint *> *removed __attribute__((swift_name("removed")));
@property (readonly) int32_t unchangedCount __attribute__((swift_name("unchangedCount")));
@end


/**
 * Settings of component fingerprinting: which properties are left out of the hash, and the limits of reading.
 *
 * An immutable value; build one with [builder]. [Default] ignores `DTSTAMP`, which many servers refresh on every export
 * and which would otherwise make every component look changed.
 *
 * @property ignoredProperties the canonical names of the properties that do not take part in the hash.
 * @property limits the limits of reading. What lies beyond them does not take part in any hash, so that a change there is not
 *   seen: for a file from a stranger, set [problemListener] as well.
 * @property problemListener receives the problems of reading, such as a line above the limit (`ICAL-S010`), or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FingerprintOptions")))
@interface CalendrumCoreFingerprintOptions : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreFingerprintOptionsCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSSet<NSString *> *ignoredProperties __attribute__((swift_name("ignoredProperties")));
@property (readonly) CalendrumCoreParseLimits *limits __attribute__((swift_name("limits")));
@property (readonly) id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));
@end


/** Collects [FingerprintOptions] values. Not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FingerprintOptions.Builder")))
@interface CalendrumCoreFingerprintOptionsBuilder : CalendrumCoreBase

/**
 * Creates the options.
 *
 * @throws IllegalArgumentException if a name is not a valid property name.
 */
- (CalendrumCoreFingerprintOptions *)build __attribute__((swift_name("build()")));

/** Sets [ignoredProperties] to [names]. */
- (CalendrumCoreFingerprintOptionsBuilder *)ignoredPropertiesNames:(NSSet<NSString *> *)names __attribute__((swift_name("ignoredProperties(names:)")));

/** Sets [limits] to [value]. */
- (CalendrumCoreFingerprintOptionsBuilder *)limitsValue:(CalendrumCoreParseLimits *)value __attribute__((swift_name("limits(value:)")));

/** Sets [problemListener] to [value]. */
- (CalendrumCoreFingerprintOptionsBuilder *)problemListenerValue:(id<CalendrumCoreProblemListener> _Nullable)value __attribute__((swift_name("problemListener(value:)")));

/** The names of the properties to ignore; `DTSTAMP` by default. */
@property NSSet<NSString *> *ignoredProperties __attribute__((swift_name("ignoredProperties")));

/** The limits of reading; [ParseLimits.Default] by default. */
@property CalendrumCoreParseLimits *limits __attribute__((swift_name("limits")));

/** Receives the problems of reading; `null` by default. */
@property id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));
@end


/** The shared defaults and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FingerprintOptions.Companion")))
@interface CalendrumCoreFingerprintOptionsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The shared defaults and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreFingerprintOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the defaults. */
- (CalendrumCoreFingerprintOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** Ignores only `DTSTAMP`. */
@property (readonly) CalendrumCoreFingerprintOptions *Default __attribute__((swift_name("Default")));
@end


/**
 * The fingerprints of all components of a feed, in input order, computed in one pass over the bytes without parsing
 * values.
 *
 * Store [encodeToString] and compare it with a later run through [diff] to learn which components were added, removed
 * or changed. Hashes depend on the algorithm, so fingerprints of different [algorithmVersion]s cannot be compared. An
 * immutable value.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Fingerprints")))
@interface CalendrumCoreFingerprints : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreFingerprintsCompanion *companion __attribute__((swift_name("companion")));

/**
 * Compares these fingerprints with an older run.
 *
 * @param previous the older fingerprints, for example restored by [decodeFromString].
 * @throws IllegalArgumentException if the two runs have different algorithm versions.
 */
- (CalendrumCoreFingerprintDiff *)diffPrevious:(CalendrumCoreFingerprints *)previous __attribute__((swift_name("diff(previous:)")));

/** Returns a compact, versioned text form that [decodeFromString] reads back.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (NSString *)encodeToString __attribute__((swift_name("encodeToString()")));

/** Returns `true` when [other] is a Fingerprints equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The version of the hashing algorithm; it changes whenever hashes would differ for the same content. */
@property (readonly) int32_t algorithmVersion __attribute__((swift_name("algorithmVersion")));

/** One fingerprint per child of each `VCALENDAR`, plus any component outside a calendar, in input order. */
@property (readonly) NSArray<CalendrumCoreComponentFingerprint *> *entries __attribute__((swift_name("entries")));
@end


/** Restores fingerprints from text. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Fingerprints.Companion")))
@interface CalendrumCoreFingerprintsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Restores fingerprints from text. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreFingerprintsCompanion *shared __attribute__((swift_name("shared")));

/**
 * Reads the text form made by [encodeToString].
 *
 * @throws IllegalArgumentException if the text is malformed or was made by another algorithm version.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreFingerprints * _Nullable)decodeFromStringText:(NSString *)text error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decodeFromString(text:)")));
@end


/**
 * A component of an iCalendar tree, such as `VCALENDAR`, `VEVENT` or `VALARM`: a name, its properties and its nested
 * components, in the order of the source ([RFC 5545 §3.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.6)).
 *
 * The tree is lossless and immutable. A component read from input keeps byte ranges of the source and decodes its name
 * and its children's text on first access; with `ParseOptions.retainSource = false` the lines are copied instead of
 * holding the input. Components built by hand with [builder] or the `ICalComponent(name) { ... }` function have no
 * source and no [span]. To change a tree, build a new one: the typed model's `edit` functions share every unchanged
 * node.
 *
 * [equals] and [hashCode] compare content (name, properties in order, nested components) without recursion, so a very
 * deep tree cannot overflow the stack; the source position and the spelling in the input do not matter. [toString]
 * shows names and counts and never values. A component can be read from any thread.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalComponent")))
@interface CalendrumCoreICalComponent : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreICalComponentCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the directly nested components named [name], ignoring ASCII case, in source order; the list is empty when
 * there are none. Deeper levels are not searched.
 *
 * @param name a component name such as `VEVENT`.
 */
- (NSArray<CalendrumCoreICalComponent *> *)componentsName:(NSString *)name __attribute__((swift_name("components(name:)")));

/**
 * Returns `true` when [other] is a component with the same name, properties and nested components, compared without
 * recursion; position and spelling in the source do not matter.
 */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name, the properties and the nested components, computed without recursion. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns all properties named [name], ignoring ASCII case, in source order; the list is empty when there are none.
 *
 * @param name a property name such as `ATTENDEE`.
 */
- (NSArray<CalendrumCoreICalProperty *> *)propertiesName:(NSString *)name __attribute__((swift_name("properties(name:)")));

/**
 * Returns the first property named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a property name such as `SUMMARY`.
 */
- (CalendrumCoreICalProperty * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns the name and the number of properties and nested components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The directly nested components, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumCoreICalComponent *> *components __attribute__((swift_name("components")));

/**
 * The canonical name in upper case, such as `VEVENT`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/**
 * The properties of this component, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumCoreICalProperty *> *properties __attribute__((swift_name("properties")));

/**
 * Where the component starts in the input and where its `END` line ends, or `null` for a component that was built
 * by hand or edited.
 */
@property (readonly) CalendrumCoreSourceSpan * _Nullable span __attribute__((swift_name("span")));
@end


/**
 * Collects the properties and nested components of a new [ICalComponent].
 *
 * A builder is not thread-safe. Obtain one with [ICalComponent.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalComponent.Builder")))
@interface CalendrumCoreICalComponentBuilder : CalendrumCoreBase

/** Creates the component. The builder can be reused: later changes do not affect components already built. */
- (CalendrumCoreICalComponent *)build __attribute__((swift_name("build()")));

/** Appends [component] after the nested components added so far. */
- (CalendrumCoreICalComponentBuilder *)componentComponent:(CalendrumCoreICalComponent *)component __attribute__((swift_name("component(component:)")));

/**
 * Appends a nested component built by [block].
 *
 * @param name the name of the nested component.
 * @param block fills the builder of the nested component.
 * @throws IllegalArgumentException if [name] is not a valid component name.
 */
- (CalendrumCoreICalComponentBuilder *)componentName:(NSString *)name block:(void (^)(CalendrumCoreICalComponentBuilder *))block __attribute__((swift_name("component(name:block:)")));

/** Appends [property] after the properties added so far. */
- (CalendrumCoreICalComponentBuilder *)propertyProperty:(CalendrumCoreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Appends a property with the given [name] and [value] and no parameters.
 *
 * @throws IllegalArgumentException if [name] is not a valid property name, or [value] contains a control
 *   character other than a tab.
 */
- (CalendrumCoreICalComponentBuilder *)propertyName:(NSString *)name value:(NSString *)value __attribute__((swift_name("property(name:value:)")));
@end


/** Creates builders for [ICalComponent]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalComponent.Companion")))
@interface CalendrumCoreICalComponentCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [ICalComponent]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreICalComponentCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a component named [name].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @throws IllegalArgumentException if [name] is empty or has another character.
 */
- (CalendrumCoreICalComponentBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));
@end


/**
 * A parameter of a property, such as `TZID=Europe/Moscow`: a name and one or more values ([RFC 5545
 * §3.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.2)).
 *
 * Parameters of parsed input decode their name and values on first access. The quotes around a value are removed and a
 * `^'`, `^n` or `^^` escape of [RFC 6868](https://www.rfc-editor.org/rfc/rfc6868) is decoded. Equality compares the
 * name and the values; it ignores spelling and position. A parameter is immutable and can be read from any thread.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalParameter")))
@interface CalendrumCoreICalParameter : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreICalParameterCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a parameter with the same name and values. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name and the values. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the name and the number of values; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The canonical name in upper case, such as `TZID`. */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The first value, or an empty string when there is none. */
@property (readonly) NSString *value __attribute__((swift_name("value")));

/**
 * All values, in order, with quotes removed. The list is never empty for a parameter built by [of]; a parameter
 * read from input without `=` has none.
 */
@property (readonly) NSArray<NSString *> *values __attribute__((swift_name("values")));
@end


/** Creates parameters by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalParameter.Companion")))
@interface CalendrumCoreICalParameterCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates parameters by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreICalParameterCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates a parameter with the given [name] and [values].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @param values at least one value.
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumCoreICalParameter *)ofName:(NSString *)name values:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("of(name:values:)")));
@end


/**
 * Reads iCalendar data with a fixed set of [ParseOptions].
 *
 * Obtain a parser from `ICal.parser()` or `ICal.parser(options)`. A parser is immutable and thread-safe: it holds only
 * its options, and every call to `parse`, `reader` or `components` gets its own state. Per-parse state, such as a
 * stateful [LineInterceptor], is created from the factories in the options for each call.
 *
 * The same input gives the same tree and the same problems in every form: a `ByteArray`, a `String`, a
 * [io.github.calendrum.ByteInput] with any block size, or an `InputStream` on the JVM. Positions in problems are offsets in
 * bytes from the start of the input.
 *
 * In lenient mode (the default) a parser never throws on data. In strict mode it throws [ICalParseException] at the
 * first problem with severity [io.github.calendrum.Severity.Error]. Calls that return a [ParseResult] are `parse`, calls
 * that stream are `reader` and `components`.
 *
 * @property options the options this parser applies.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalParser")))
@interface CalendrumCoreICalParser : CalendrumCoreBase

/**
 * Returns a stream of the children of every `VCALENDAR` that [input] yields, one component at a time, with memory
 * bounded by a block and the longest component. The input is not closed.
 */
- (CalendrumCoreComponentStream *)componentsInput:(id<CalendrumCoreByteInput>)input __attribute__((swift_name("components(input:)")));

/**
 * Returns a stream of the children of every `VCALENDAR` in [bytes], one component at a time.
 *
 * @param bytes the complete content.
 */
- (CalendrumCoreComponentStream *)componentsBytes:(CalendrumCoreKotlinByteArray *)bytes __attribute__((swift_name("components(bytes:)")));

/**
 * Decodes everything [input] yields into objects of [schema], one at a time, with bounded memory.
 *
 * The sequence can be iterated once and reads the input as it goes; a strict parser throws [ICalParseException]
 * from the iterator.
 */
- (id<CalendrumCoreKotlinSequence>)decodeInput:(id<CalendrumCoreByteInput>)input schema:(CalendrumCoreIcsSchema<id> *)schema __attribute__((swift_name("decode(input:schema:)")));

/**
 * Decodes [bytes] straight into objects of [schema], without building a tree.
 *
 * Only the properties the schema names are read. Components that miss a required field are skipped; use
 * [decodeWithProblems] to see why.
 *
 * @param bytes the complete content.
 * @param schema describes the component and its fields.
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSArray<id> * _Nullable)decodeBytes:(CalendrumCoreKotlinByteArray *)bytes schema:(CalendrumCoreIcsSchema<id> *)schema error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decode(bytes:schema:)")));

/**
 * Decodes [bytes] into objects of [schema] and also returns the problems and the number of skipped components.
 *
 * @param bytes the complete content.
 * @param schema describes the component and its fields.
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreSchemaResult<id> * _Nullable)decodeWithProblemsBytes:(CalendrumCoreKotlinByteArray *)bytes schema:(CalendrumCoreIcsSchema<id> *)schema error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decodeWithProblems(bytes:schema:)")));

/**
 * Parses everything [input] yields, reading it block by block.
 *
 * The tree does not keep the input, so it is always a copy of the lines it needs; the memory held while reading is
 * the tree plus a block. For a feed too large to hold as a tree use [components] instead. [input] is read to its
 * end and not closed.
 *
 * @param input the source of the content.
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreParseResult * _Nullable)parseInput:(id<CalendrumCoreByteInput>)input error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(input:)")));

/**
 * Parses the whole [bytes] into a tree.
 *
 * The encoding is detected as described in [ParseOptions.charset]. The result keeps the problems found and, unless
 * the options say otherwise, references [bytes] instead of copying them, so do not change the array while the
 * result is in use.
 *
 * @param bytes the complete content.
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreParseResult * _Nullable)parseBytes:(CalendrumCoreKotlinByteArray *)bytes error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(bytes:)")));

/**
 * Parses [text] into a tree.
 *
 * The text is encoded to UTF-8 first, so no charset detection happens, only a leading U+FEFF is reported as
 * `ICAL-S001`. Positions count bytes of the UTF-8 form.
 *
 * @param text the complete content.
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreParseResult * _Nullable)parseText:(NSString *)text error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(text:)")));

/**
 * Parses [length] bytes of [bytes] starting at [offset]. Offsets in positions are relative to [offset].
 *
 * @param bytes an array that holds the content.
 * @param offset the index of the first byte to parse.
 * @param length the number of bytes to parse.
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreParseResult * _Nullable)parseBytes:(CalendrumCoreKotlinByteArray *)bytes offset:(int32_t)offset length:(int32_t)length error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(bytes:offset:length:)")));

/**
 * Parses only the component that [fingerprint] points at, by its offsets, without reading the rest of [bytes].
 *
 * The result equals the corresponding subtree of a full parse. [bytes] must be the input the fingerprint was
 * computed from.
 *
 * @param bytes the content that was fingerprinted.
 * @param fingerprint an entry of [Fingerprints.entries] or of a [FingerprintDiff].
 * @throws IllegalArgumentException if the fingerprint does not fit [bytes].
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreICalComponent * _Nullable)parseComponentBytes:(CalendrumCoreKotlinByteArray *)bytes fingerprint:(CalendrumCoreComponentFingerprint *)fingerprint error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parseComponent(bytes:fingerprint:)")));

/** Returns a pull cursor over everything [input] yields; the input is read block by block and not closed. */
- (CalendrumCoreICalReader *)readerInput:(id<CalendrumCoreByteInput>)input __attribute__((swift_name("reader(input:)")));

/**
 * Returns a pull cursor over [bytes] that reports events without building a tree.
 *
 * The cursor allocates nothing per event; see [ICalReader] for what each event offers.
 *
 * @param bytes the complete content.
 */
- (CalendrumCoreICalReader *)readerBytes:(CalendrumCoreKotlinByteArray *)bytes __attribute__((swift_name("reader(bytes:)")));

/** Returns a pull cursor over [text], encoded to UTF-8. */
- (CalendrumCoreICalReader *)readerText:(NSString *)text __attribute__((swift_name("reader(text:)")));

/** Returns a pull cursor over [length] bytes of [bytes] starting at [offset]. */
- (CalendrumCoreICalReader *)readerBytes:(CalendrumCoreKotlinByteArray *)bytes offset:(int32_t)offset length:(int32_t)length __attribute__((swift_name("reader(bytes:offset:length:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreParseOptions *options __attribute__((swift_name("options")));
@end


/**
 * A content line of a component: a name, parameters and a value, such as `SUMMARY:Weekly sync` or
 * `DTSTART;TZID=Europe/Moscow:20261001T100000` ([RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1)).
 *
 * The value is the text of the line after the colon, unfolded but otherwise as written: not unescaped and not
 * interpreted. The typed model in `io.github.calendrum.model` decodes it into dates, durations and other types. A property
 * read from input decodes its name, value and parameters on first access, and keeps its original spelling and folding
 * for byte-stable writing; a property built by hand has no [span].
 *
 * Equality compares the name, the parameters and the value. A property is immutable and can be read from any thread.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalProperty")))
@interface CalendrumCoreICalProperty : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreICalPropertyCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a property with the same name, parameters and value. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name, the parameters and the value. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the first parameter named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a parameter name such as `TZID`.
 */
- (CalendrumCoreICalParameter * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/** Returns the name and the number of parameters; never the value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The canonical name in upper case, such as `DTSTART`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The parameters in source order. The list is empty for none. */
@property (readonly) NSArray<CalendrumCoreICalParameter *> *parameters __attribute__((swift_name("parameters")));

/**
 * Where the property is in the input, or `null` for a property that was built by hand. For input that was changed
 * by an interceptor it is the position of the original line.
 */
@property (readonly) CalendrumCoreSourceSpan * _Nullable span __attribute__((swift_name("span")));

/**
 * The value as written after the colon, with folding removed. It is not unescaped; an empty string for no value.
 */
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** Collects the parameters and the value of a new [ICalProperty]. A builder is not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalProperty.Builder")))
@interface CalendrumCoreICalPropertyBuilder : CalendrumCoreBase

/**
 * Creates the property.
 *
 * @throws IllegalArgumentException if the value contains a control character other than a tab.
 */
- (CalendrumCoreICalProperty *)build __attribute__((swift_name("build()")));

/** Appends [parameter] after the parameters added so far. */
- (CalendrumCoreICalPropertyBuilder *)parameterParameter:(CalendrumCoreICalParameter *)parameter __attribute__((swift_name("parameter(parameter:)")));

/**
 * Appends a parameter with the given [name] and [values].
 *
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumCoreICalPropertyBuilder *)parameterName:(NSString *)name values:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/**
 * Sets the value, which is written as is: escape `TEXT` yourself or use the typed model. The default is an
 * empty string.
 */
- (CalendrumCoreICalPropertyBuilder *)valueValue:(NSString *)value __attribute__((swift_name("value(value:)")));
@end


/** Creates properties by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalProperty.Companion")))
@interface CalendrumCoreICalPropertyCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates properties by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreICalPropertyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a property named [name].
 *
 * @param name letters, digits and hyphens only; `BEGIN` and `END` are not allowed.
 * @throws IllegalArgumentException if [name] is invalid.
 */
- (CalendrumCoreICalPropertyBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));

/**
 * Creates a property with no parameters.
 *
 * @param name letters, digits and hyphens only; the stored name is upper case; `BEGIN` and `END` are not
 *   allowed.
 * @param value the value as it is written; a control character other than a tab is not allowed.
 * @throws IllegalArgumentException if [name] or [value] is invalid.
 */
- (CalendrumCoreICalProperty *)ofName:(NSString *)name value:(NSString *)value __attribute__((swift_name("of(name:value:)")));
@end


/**
 * A pull cursor over iCalendar content: it moves from event to event and exposes the current one without building a
 * tree.
 *
 * Create one with `ICal.parser().reader(...)`. [next] moves to the next [ContentEventType]: `Begin` of a component, a
 * `Property` or `End` of a component. The accessors describe the current event. Nothing is allocated per event, and
 * [value] is a view that is valid only until the next call of [next]. At a `Begin` event [readComponent] builds the
 * subtree of that component and leaves the cursor at its `End`.
 *
 * Problems go to the `problemListener` of the options; in strict mode [next] throws at the first error and keeps
 * throwing. A cursor is not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalReader")))
@interface CalendrumCoreICalReader : CalendrumCoreBase

/**
 * Moves to the next event.
 *
 * @return the new event, or `null` at the end of the input.
 * @throws ICalParseException in strict mode, at the first error; later calls rethrow it.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreContentEventType * _Nullable)nextAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("next()"))) __attribute__((swift_error(nonnull_error)));

/**
 * Returns the canonical name of the parameter at [index] of the current property.
 *
 * @param index from 0 until [parameterCount].
 * @throws IllegalStateException if the current event is not a `Property`.
 */
- (NSString *)parameterNameIndex:(int32_t)index __attribute__((swift_name("parameterName(index:)")));

/**
 * Returns the values of the parameter at [index] of the current property, unquoted.
 *
 * @param index from 0 until [parameterCount].
 * @throws IllegalStateException if the current event is not a `Property`.
 */
- (NSArray<NSString *> *)parameterValuesIndex:(int32_t)index __attribute__((swift_name("parameterValues(index:)")));

/**
 * Builds the component of the current `Begin` event, including all it contains, and moves to its `End`.
 *
 * @throws IllegalStateException if the current event is not a `Begin`.
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreICalComponent * _Nullable)readComponentAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("readComponent()")));

/**
 * Returns the position of the current event: the whole line of a `Begin`, `Property` or `End`, or a point for an
 * implicit end.
 *
 * @throws IllegalStateException if there is no current event.
 */
- (CalendrumCoreSourceSpan *)span __attribute__((swift_name("span()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Returns the value of the current property as a new string.
 *
 * @throws IllegalStateException if the current event is not a `Property`.
 */
- (NSString *)valueString __attribute__((swift_name("valueString()")));

/**
 * The 1-based column in bytes where the current event starts on its line.
 *
 * @throws IllegalStateException if there is no current event.
 */
@property (readonly) int32_t column __attribute__((swift_name("column")));

/**
 * The nesting depth of the current event: 1 for a top-level component and its properties, 0 when there is no
 * current event.
 */
@property (readonly) int32_t depth __attribute__((swift_name("depth")));

/**
 * Whether the current event is an `End` the input does not contain: a component closed implicitly (`ICAL-S016`,
 * `ICAL-S018`).
 */
@property (readonly) BOOL isImplicitEnd __attribute__((swift_name("isImplicitEnd")));

/**
 * The 1-based number of the physical line where the current event starts.
 *
 * @throws IllegalStateException if there is no current event.
 */
@property (readonly) int32_t line __attribute__((swift_name("line")));

/**
 * The canonical name of the current component (at `Begin` and `End`) or property (at `Property`).
 *
 * @throws IllegalStateException if there is no current event.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/**
 * The offset in bytes from the start of the input where the current event starts.
 *
 * @throws IllegalStateException if there is no current event.
 */
@property (readonly) int64_t offset __attribute__((swift_name("offset")));

/**
 * The number of parameters of the current property.
 *
 * @throws IllegalStateException if the current event is not a `Property`.
 */
@property (readonly) int32_t parameterCount __attribute__((swift_name("parameterCount")));

/**
 * The value of the current property as a view over a reused buffer; copy it ([valueString]) to keep it past the
 * next [next].
 *
 * @throws IllegalStateException if the current event is not a `Property`.
 */
@property (readonly) id value __attribute__((swift_name("value")));
@end


/**
 * Writes component trees as iCalendar text.
 *
 * Get a writer from `ICal.writer()` or `ICal.writer(options)`. A writer is immutable and thread-safe. With the default
 * options it is byte-stable: a tree read from input is written as it was read, except that line ends become CRLF, and
 * an edited tree gives a minimal difference, because only lines that changed are rewritten. Nodes built by hand are
 * written canonically: lines folded at 75 octets, parameter values quoted when they contain a colon, semicolon or
 * comma, and `^`, `"` and line breaks in parameter values escaped as in [RFC
 * 6868](https://www.rfc-editor.org/rfc/rfc6868).
 *
 * Streams given to a writer are never closed. A component is written completely or the call throws; after a failure the
 * output can hold a partial component.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalWriter")))
@interface CalendrumCoreICalWriter : CalendrumCoreBase

/**
 * Encodes [items] as components of [schema] inside a copy of [calendar] and returns the bytes.
 *
 * @param items the objects to write, one component each.
 * @param schema describes the component and its fields.
 * @param calendar the surrounding calendar; its properties are kept and the components are added.
 * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
 *   defaults are given.
 * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumCoreIcsSchema<id> *)schema calendar:(CalendrumCoreVCalendar *)calendar error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:calendar:)")));

/**
 * Encodes [items] as components of [schema] inside a new `VCALENDAR` set up by [calendar], and returns the bytes.
 *
 * @param items the objects to write, one component each.
 * @param schema describes the component and its fields.
 * @param calendar sets the properties of the surrounding calendar, such as `prodId`.
 * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
 *   defaults are given.
 * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumCoreIcsSchema<id> *)schema error:(NSError * _Nullable * _Nullable)error buildCalendar:(void (^)(CalendrumCoreVCalendarBuilder *))buildCalendar __attribute__((swift_name("encode(items:schema:buildCalendar:)")));

/**
 * Encodes [items] as components of [schema] inside a copy of [calendar] and returns the bytes, generating `UID` and
 * `DTSTAMP` of components that lack them with [defaults].
 *
 * @param items the objects to write, one component each.
 * @param schema describes the component and its fields.
 * @param calendar the surrounding calendar; its properties are kept and the components are added.
 * @param defaults the source of generated `UID` and `DTSTAMP` values.
 * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
 *   defaults are given.
 * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumCoreIcsSchema<id> *)schema calendar:(CalendrumCoreVCalendar *)calendar defaults:(CalendrumCoreBuildDefaults *)defaults error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:calendar:defaults:)")));

/**
 * Encodes [items] as components of [schema] inside a new `VCALENDAR` set up by [calendar], generating `UID` and
 * `DTSTAMP` of components that lack them with [defaults].
 *
 * @param items the objects to write, one component each.
 * @param schema describes the component and its fields.
 * @param calendar sets the properties of the surrounding calendar, such as `prodId`.
 * @param defaults the source of generated `UID` and `DTSTAMP` values.
 * @throws IllegalStateException if the calendar has no `PRODID`, or a component has no `UID` or `DTSTAMP` and no
 *   defaults are given.
 * @throws IllegalArgumentException if a required field of an item is null or a value cannot be written.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumCoreIcsSchema<id> *)schema buildCalendar:(void (^)(CalendrumCoreVCalendarBuilder *))buildCalendar defaults:(CalendrumCoreBuildDefaults *)defaults error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:buildCalendar:defaults:)")));

/**
 * Writes [component] and returns the UTF-8 bytes.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinByteArray * _Nullable)encodeToByteArrayComponent:(CalendrumCoreICalComponent *)component error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToByteArray(component:)")));

/**
 * Writes all [components] one after another and returns the UTF-8 bytes.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinByteArray * _Nullable)encodeToByteArrayComponents:(NSArray<CalendrumCoreICalComponent *> *)components error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToByteArray(components:)")));

/**
 * Writes [component] and returns the text.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSString * _Nullable)encodeToStringComponent:(CalendrumCoreICalComponent *)component error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToString(component:)")));

/**
 * Writes all [components] one after another and returns the text.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSString * _Nullable)encodeToStringComponents:(NSArray<CalendrumCoreICalComponent *> *)components error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToString(components:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Writes [component] to [out] as UTF-8 bytes in blocks; [out] is not closed.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written. What was written before the
 *   error has been passed on to [out].
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponent:(CalendrumCoreICalComponent *)component out:(id<CalendrumCoreByteOutput>)out error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(component:out:)")));

/**
 * Writes [component] to [out]. Nothing is flushed or closed.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written, such as a control character in
 *   a value.
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponent:(CalendrumCoreICalComponent *)component appendable:(id<CalendrumCoreKotlinAppendable>)appendable error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(component:appendable:)")));

/**
 * Writes all [components] to [out] as UTF-8 bytes; [out] is not closed.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponents:(NSArray<CalendrumCoreICalComponent *> *)components out:(id<CalendrumCoreByteOutput>)out error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(components:out:)")));

/**
 * Writes all [components] to [out] one after another.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponents:(NSArray<CalendrumCoreICalComponent *> *)components appendable:(id<CalendrumCoreKotlinAppendable>)appendable error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(components:appendable:)")));
@end


/**
 * The view of one property line that a [LineInterceptor] receives, with functions to change it.
 *
 * The same instance is reused for every line, which is why it is valid only inside [LineInterceptor.intercept]; any
 * access outside that call throws [IllegalStateException]. Reading is cheap and allocation-free; the first change
 * copies the name, the parameters and the value of the line, and later changes work on that copy.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InterceptedLine")))
@interface CalendrumCoreInterceptedLine : CalendrumCoreBase

/** Removes the whole line from the tree or from the output. Later interceptors do not see it. */
- (void)drop __attribute__((swift_name("drop()")));

/**
 * Returns the canonical name of the parameter at [index].
 *
 * @param index from 0 until [parameterCount].
 */
- (NSString *)parameterNameIndex:(int32_t)index __attribute__((swift_name("parameterName(index:)")));

/**
 * Returns the values of the parameter at [index], unquoted.
 *
 * @param index from 0 until [parameterCount].
 */
- (NSArray<NSString *> *)parameterValuesIndex:(int32_t)index __attribute__((swift_name("parameterValues(index:)")));

/** Removes every parameter named [name]; nothing happens when there is none. */
- (void)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/**
 * Changes the name of the property.
 *
 * @throws IllegalArgumentException if [newName] is not a valid property name.
 */
- (void)renameNewName:(NSString *)newName __attribute__((swift_name("rename(newName:)")));

/**
 * Sets the parameter [name] to [values]: the first parameter of that name is replaced in place, later ones are
 * removed, and a new parameter goes last.
 *
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (void)setParameterName:(NSString *)name values:(CalendrumCoreKotlinArray<NSString *> *)values __attribute__((swift_name("setParameter(name:values:)")));

/**
 * Replaces the value, which is written as is.
 *
 * @throws IllegalArgumentException if [newValue] has a control character other than a tab.
 */
- (void)setValueNewValue:(NSString *)newValue __attribute__((swift_name("setValue(newValue:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The canonical name of the component that owns the line, such as `VEVENT`. */
@property (readonly) NSString *componentName __attribute__((swift_name("componentName")));

/** The nesting depth of the component that owns the line: 1 for a top-level component. */
@property (readonly) int32_t depth __attribute__((swift_name("depth")));

/** The canonical name of the property, as changed by [rename] if it was. */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The number of parameters of the line. */
@property (readonly) int32_t parameterCount __attribute__((swift_name("parameterCount")));

/**
 * The value of the line, as changed by [setValue] if it was. It may be a view over a reused buffer, so copy it with
 * `toString()` to keep it.
 */
@property (readonly) id value __attribute__((swift_name("value")));
@end


/**
 * Looks at each property line while it is read or written, and may change or drop it.
 *
 * An interceptor fixes a feed on the fly: nothing is allocated per line until an edit is made, and lines it leaves
 * alone stay byte for byte as they were. Register it with `ParseOptions.Builder.intercept` or
 * `WriteOptions.Builder.intercept`. Interceptors run in the order they were added; each sees the line as the previous
 * one left it. An edited line is written canonically.
 */
__attribute__((swift_name("LineInterceptor")))
@protocol CalendrumCoreLineInterceptor
@required

/**
 * Called once for every property line.
 *
 * The [line] is valid only during this call; do not keep it. The function must not throw for data reasons.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (void)interceptLine:(CalendrumCoreInterceptedLine *)line __attribute__((swift_name("intercept(line:)")));
@end


/**
 * Upper bounds on what a parser accepts, so that hostile or broken input cannot use unlimited memory or time.
 *
 * When a bound is hit, the parser reports a problem and either skips the offending part or stops with a truncated
 * result; see the documentation of each property. Build limits with [builder]; [Default] is generous and fits files you
 * trust, while [ParseOptions.Untrusted] uses much tighter ones. A value of this class is immutable.
 *
 * @property maxLineOctets the longest logical line, after unfolding, in bytes; a longer line is skipped (`ICAL-S010`).
 * @property maxLines the most logical lines; reading stops there (`ICAL-S014`).
 * @property maxProblems the most problems that are reported; later ones are only counted (`ICAL-S013`).
 * @property maxDepth the deepest nesting of components; deeper subtrees are skipped (`ICAL-S022`).
 * @property maxComponents the most components; reading stops there (`ICAL-S023`).
 * @property maxInputOctets the most bytes of input, as given before any transcoding, or [Long.MAX_VALUE] for no limit
 *   (the default). Reading stops before the first logical line that does not end within that many bytes, the byte that
 *   follows its line ending included (`ICAL-S030`), and the result is truncated. A byte array is checked once before it
 *   is read; a stream counts the bytes of each block it reads and reads at most one block past the limit.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseLimits")))
@interface CalendrumCoreParseLimits : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreParseLimitsCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t maxComponents __attribute__((swift_name("maxComponents")));
@property (readonly) int32_t maxDepth __attribute__((swift_name("maxDepth")));
@property (readonly) int64_t maxInputOctets __attribute__((swift_name("maxInputOctets")));
@property (readonly) int32_t maxLineOctets __attribute__((swift_name("maxLineOctets")));
@property (readonly) int32_t maxLines __attribute__((swift_name("maxLines")));
@property (readonly) int32_t maxProblems __attribute__((swift_name("maxProblems")));

/**
 * The time budget of one parse, measured by [ParseOptions.timeSource], or `null` for none.
 *
 * When the budget runs out the parser stops at a line boundary with `ICAL-S029` and a truncated result. A cursor
 * and a component stream count the time of the code between events, too.
 */
@property (readonly) id _Nullable maxTime __attribute__((swift_name("maxTime")));
@end


/**
 * Collects [ParseLimits] values. The properties and the functions of the same name do the same; the functions
 * chain.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseLimits.Builder")))
@interface CalendrumCoreParseLimitsBuilder : CalendrumCoreBase

/**
 * Creates the limits.
 *
 * @throws IllegalArgumentException if a bound is not positive (or, for [maxProblems], negative), or the time is
 *   not positive.
 */
- (CalendrumCoreParseLimits *)build __attribute__((swift_name("build()")));

/** Sets [maxComponents] to [value]. */
- (CalendrumCoreParseLimitsBuilder *)maxComponentsValue:(int32_t)value __attribute__((swift_name("maxComponents(value:)")));

/** Sets [maxDepth] to [value]. */
- (CalendrumCoreParseLimitsBuilder *)maxDepthValue:(int32_t)value __attribute__((swift_name("maxDepth(value:)")));

/** Sets [maxInputOctets] to [value]. */
- (CalendrumCoreParseLimitsBuilder *)maxInputOctetsValue:(int64_t)value __attribute__((swift_name("maxInputOctets(value:)")));

/** Sets [maxLineOctets] to [value]. */
- (CalendrumCoreParseLimitsBuilder *)maxLineOctetsValue:(int32_t)value __attribute__((swift_name("maxLineOctets(value:)")));

/** Sets [maxLines] to [value]. */
- (CalendrumCoreParseLimitsBuilder *)maxLinesValue:(int32_t)value __attribute__((swift_name("maxLines(value:)")));

/** Sets [maxProblems] to [value]. */
- (CalendrumCoreParseLimitsBuilder *)maxProblemsValue:(int32_t)value __attribute__((swift_name("maxProblems(value:)")));

/** Sets [maxTime] to [value]; `null` removes the budget. */
- (CalendrumCoreParseLimitsBuilder *)maxTimeValue:(id _Nullable)value __attribute__((swift_name("maxTime(value:)")));

/** The most components; positive. The default is 1,000,000. */
@property int32_t maxComponents __attribute__((swift_name("maxComponents")));

/** The deepest component nesting; positive. The default is 16. */
@property int32_t maxDepth __attribute__((swift_name("maxDepth")));

/** The most bytes of input; positive. The default, [Long.MAX_VALUE], means no limit. */
@property int64_t maxInputOctets __attribute__((swift_name("maxInputOctets")));

/** The longest logical line in bytes; positive. The default is 8 MiB. */
@property int32_t maxLineOctets __attribute__((swift_name("maxLineOctets")));

/** The most logical lines; positive. The default is 10,000,000. */
@property int32_t maxLines __attribute__((swift_name("maxLines")));

/** The most reported problems; zero or more. The default is 1,000. */
@property int32_t maxProblems __attribute__((swift_name("maxProblems")));

/** The time budget of one parse, or `null` for none (the default); positive. */
@property id _Nullable maxTime __attribute__((swift_name("maxTime")));
@end


/** The default limits and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseLimits.Companion")))
@interface CalendrumCoreParseLimitsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default limits and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreParseLimitsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a new builder that starts from the default limits. */
- (CalendrumCoreParseLimitsBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Generous limits for files you trust: lines up to 8 MiB, 10 million lines, 1,000 reported problems, nesting
 * depth 16, one million components, no limit on the input size and no time budget.
 */
@property (readonly) CalendrumCoreParseLimits *Default __attribute__((swift_name("Default")));
@end


/** How a parser reacts to problems in the input. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseMode")))
@interface CalendrumCoreParseMode : CalendrumCoreKotlinEnum<CalendrumCoreParseMode *>
+ (instancetype)alloc __attribute__((unavailable));

/** How a parser reacts to problems in the input. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreParseMode *lenient __attribute__((swift_name("lenient")));
@property (class, readonly) CalendrumCoreParseMode *strict __attribute__((swift_name("strict")));
+ (CalendrumCoreKotlinArray<CalendrumCoreParseMode *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreParseMode *> *entries __attribute__((swift_name("entries")));
@end


/**
 * Settings of a parser: mode, limits, problem listener, retention of the source, charset hint, interceptors, selection
 * and registry.
 *
 * Build options with [builder] or with the `ICal.parser { ... }` DSL. The class is immutable and can be shared between
 * threads; [Default] is lenient with the default limits, and [Untrusted] is meant for files from strangers.
 *
 * @property mode lenient or strict reading.
 * @property limits the bounds on size, depth and time.
 * @property problemListener receives problems as they are found, or `null`.
 * @property retainSource whether the tree keeps references to the input, which saves copying; see
 *   [Builder.retainSource].
 * @property charset the charset hint for byte input, or `null`.
 * @property interceptors the factories of the line interceptors, applied in order.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseOptions")))
@interface CalendrumCoreParseOptions : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreParseOptionsCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable charset __attribute__((swift_name("charset")));

/**
 * Whether values are decoded while parsing, so that value problems appear in the result. Always true in strict
 * mode.
 */
@property (readonly) BOOL decodeEagerly __attribute__((swift_name("decodeEagerly")));

/**
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
@property (readonly) NSArray<id<CalendrumCoreLineInterceptor> (^)(void)> *interceptors __attribute__((swift_name("interceptors")));
@property (readonly) CalendrumCoreParseLimits *limits __attribute__((swift_name("limits")));
@property (readonly) CalendrumCoreParseMode *mode __attribute__((swift_name("mode")));
@property (readonly) id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/**
 * The registry that tells eager decoding and strict mode what every property is; [ICalRegistry.Default] unless one
 * was set.
 */
@property (readonly) CalendrumCoreICalRegistry *registry __attribute__((swift_name("registry")));
@property (readonly) BOOL retainSource __attribute__((swift_name("retainSource")));

/** Which components and properties are read; [Selection.Full] unless a selection was set. */
@property (readonly) CalendrumCoreSelection *selection __attribute__((swift_name("selection")));

/** The clock used for the time budget; the monotonic clock unless one was set. */
@property (readonly) id<CalendrumCoreKotlinTimeSourceWithComparableMarks> timeSource __attribute__((swift_name("timeSource")));
@end


/**
 * Collects [ParseOptions] values. The properties and the functions of the same name do the same; the functions
 * chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseOptions.Builder")))
@interface CalendrumCoreParseOptionsBuilder : CalendrumCoreBase

/** Creates the options. */
- (CalendrumCoreParseOptions *)build __attribute__((swift_name("build()")));

/** Sets the charset hint; `null` removes it. */
- (CalendrumCoreParseOptionsBuilder *)charsetCharset:(NSString * _Nullable)charset __attribute__((swift_name("charset(charset:)")));

/** Sets [decodeEagerly]. */
- (CalendrumCoreParseOptionsBuilder *)decodeEagerlyEager:(BOOL)eager __attribute__((swift_name("decodeEagerly(eager:)")));

/** Adds a stateless [interceptor]; the same instance serves every parse and must be thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumCoreParseOptionsBuilder *)interceptInterceptor:(id<CalendrumCoreLineInterceptor>)interceptor __attribute__((swift_name("intercept(interceptor:)")));

/**
 * Adds an interceptor created by [factory] for every parse, so that a stateful interceptor gets a fresh
 * instance each time. Interceptors run in the order they were added.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumCoreParseOptionsBuilder *)interceptFactory:(id<CalendrumCoreLineInterceptor> (^)(void))factory __attribute__((swift_name("intercept(factory:)")));

/** Sets [limits]. */
- (CalendrumCoreParseOptionsBuilder *)limitsLimits:(CalendrumCoreParseLimits *)limits __attribute__((swift_name("limits(limits:)")));

/** Sets [mode]. */
- (CalendrumCoreParseOptionsBuilder *)modeMode:(CalendrumCoreParseMode *)mode __attribute__((swift_name("mode(mode:)")));

/** Sets the problem listener; `null` removes it. */
- (CalendrumCoreParseOptionsBuilder *)problemListenerListener:(id<CalendrumCoreProblemListener> _Nullable)listener __attribute__((swift_name("problemListener(listener:)")));

/** Sets [registry]; `null` selects the default registry. */
- (CalendrumCoreParseOptionsBuilder *)registryRegistry:(CalendrumCoreICalRegistry * _Nullable)registry __attribute__((swift_name("registry(registry:)")));

/** Sets [retainSource]. */
- (CalendrumCoreParseOptionsBuilder *)retainSourceRetain:(BOOL)retain __attribute__((swift_name("retainSource(retain:)")));

/** Reads only what [selection] names. The rest is skipped in the lexer; the result is partial. */
- (CalendrumCoreParseOptionsBuilder *)selectSelection:(CalendrumCoreSelection *)selection __attribute__((swift_name("select(selection:)")));

/** Reads only what the selection built by [block] names. */
- (CalendrumCoreParseOptionsBuilder *)selectBlock:(void (^)(CalendrumCoreSelectionBuilder *))block __attribute__((swift_name("select(block:)")));

/** Sets the clock for the time budget. */
- (CalendrumCoreParseOptionsBuilder *)timeSourceSource:(id<CalendrumCoreKotlinTimeSourceWithComparableMarks>)source __attribute__((swift_name("timeSource(source:)")));

/**
 * A hint for the encoding of byte input: a charset name or a whole `Content-Type` value such as `text/calendar;
 * charset=windows-1251`. The hint is checked against the data; a contradiction is reported (`ICAL-S027`) and
 * the data decides. `null` (the default) means no hint.
 */
@property NSString * _Nullable charset __attribute__((swift_name("charset")));

/** Decode every value while parsing and report its problems; false by default. Strict mode implies it. */
@property BOOL decodeEagerly __attribute__((swift_name("decodeEagerly")));

/** The bounds on the input; [ParseLimits.Default] by default. */
@property CalendrumCoreParseLimits *limits __attribute__((swift_name("limits")));

/** Lenient (the default) or strict reading. */
@property CalendrumCoreParseMode *mode __attribute__((swift_name("mode")));

/** Receives each reported problem as it is found; `null` (the default) for none. */
@property id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/** The registry for eager decoding and strict mode; `null` selects [ICalRegistry.Default]. */
@property CalendrumCoreICalRegistry * _Nullable registry __attribute__((swift_name("registry")));

/**
 * Whether the tree keeps references to the input bytes (true, the default) or copies the lines it needs.
 * Retaining is faster and allocates less, but holds the whole input as long as any node lives; copying lets a
 * small selection outlive a big input.
 */
@property BOOL retainSource __attribute__((swift_name("retainSource")));

/** Which components and properties to read; `null` (the default) reads everything. */
@property CalendrumCoreSelection * _Nullable selection __attribute__((swift_name("selection")));

/**
 * The clock used for [ParseLimits.maxTime]; the monotonic clock by default. Set a test clock to make the budget
 * deterministic.
 */
@property id<CalendrumCoreKotlinTimeSourceWithComparableMarks> timeSource __attribute__((swift_name("timeSource")));
@end


/** The shared defaults and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseOptions.Companion")))
@interface CalendrumCoreParseOptionsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The shared defaults and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreParseOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the defaults. */
- (CalendrumCoreParseOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** Lenient mode, the default limits, a retained source and no listener, hint, interceptors or selection. */
@property (readonly) CalendrumCoreParseOptions *Default __attribute__((swift_name("Default")));

/**
 * Options for files from strangers: lenient, but with nesting depth 8, lines up to 64 KiB, 1,000,000 lines,
 * 100,000 components, input up to 64 MiB and a time budget of 2 seconds.
 */
@property (readonly) CalendrumCoreParseOptions *Untrusted __attribute__((swift_name("Untrusted")));
@end


/**
 * The outcome of parsing: the tree, the problems found and facts about the input.
 *
 * A result is immutable. [components] are the top-level components, usually a single `VCALENDAR`. Reading values from
 * the tree is lazy and cached in the tree; use `calendars` of the typed model for wrappers with typed accessors.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ParseResult")))
@interface CalendrumCoreParseResult : CalendrumCoreBase

/**
 * Returns how many problems with [code] were found, including those beyond the reporting limit that are only
 * counted.
 *
 * @param code the code to count.
 */
- (int32_t)problemCountCode:(CalendrumCoreProblemCode *)code __attribute__((swift_name("problemCount(code:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The canonical name of the encoding the input was read in, such as `UTF-8` or `windows-1251`. */
@property (readonly) NSString *charset __attribute__((swift_name("charset")));

/**
 * How sure the library is of [charset], in percent: 100 when the encoding is certain (a byte order mark, UTF-8, a
 * confirmed hint) and lower for a guess by the heuristic.
 */
@property (readonly) int32_t charsetConfidence __attribute__((swift_name("charsetConfidence")));

/** The top-level components in input order. A property outside any component is not part of the tree. */
@property (readonly) NSArray<CalendrumCoreICalComponent *> *components __attribute__((swift_name("components")));

/**
 * Whether the tree is a selection of the input (see [ParseOptions.selection]). A partial tree cannot be written
 * back unless `WriteOptions.allowPartialWrite` is set.
 */
@property (readonly) BOOL isPartial __attribute__((swift_name("isPartial")));

/**
 * Whether parsing stopped before the end of the input because of a limit on lines, components or the input size
 * or of the time budget (`ICAL-S014`, `ICAL-S023`, `ICAL-S030`, `ICAL-S029`). Then [components] is a partial
 * result.
 */
@property (readonly) BOOL isTruncated __attribute__((swift_name("isTruncated")));

/**
 * The reported problems, in the order they were found. There are at most [ParseLimits.maxProblems] of them; see
 * [problemCount] for the real number.
 */
@property (readonly) NSArray<CalendrumCoreProblem *> *problems __attribute__((swift_name("problems")));
@end


/**
 * Tells the lexer which components and properties to read; everything else is skipped by bytes, without being parsed.
 *
 * A selection is the way to pay only for what you use: lines of unwanted properties and whole unwanted components are
 * skipped to the end of their logical line, so they cost almost nothing and produce no problems. `BEGIN` and `END`
 * lines are always read. The resulting tree is marked partial, and writing it throws unless
 * `WriteOptions.allowPartialWrite` is set. Use a preset ([Full], [Schedule], [SyncMetadata], [FreeBusy]) or [builder].
 *
 * @property timeZones which `VTIMEZONE` components are kept.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Selection")))
@interface CalendrumCoreSelection : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreSelectionCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreTimeZoneSelection *timeZones __attribute__((swift_name("timeZones")));
@end


/**
 * Collects what a [Selection] reads. The properties and the functions of the same name do the same. Not
 * thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Selection.Builder")))
@interface CalendrumCoreSelectionBuilder : CalendrumCoreBase

/** Creates the selection. */
- (CalendrumCoreSelection *)build __attribute__((swift_name("build()")));

/**
 * Adds components by name. Names of components that the standard defines and unknown `X-` names are accepted;
 * time zone components are chosen with [timeZones] instead.
 *
 * @throws IllegalArgumentException if a name is invalid, is not a component name, or names a time zone
 *   component.
 */
- (CalendrumCoreSelectionBuilder *)componentsNames:(CalendrumCoreKotlinArray<NSString *> *)names __attribute__((swift_name("components(names:)")));

/** Adds what the [features] need. */
- (CalendrumCoreSelectionBuilder *)needFeatures:(CalendrumCoreKotlinArray<CalendrumCoreFeature *> *)features __attribute__((swift_name("need(features:)")));

/** Adds the properties that the typed [keys] stand for. */
- (CalendrumCoreSelectionBuilder *)propertiesKeys:(CalendrumCoreKotlinArray<CalendrumCorePropertyKey<id> *> *)keys __attribute__((swift_name("properties(keys:)")));

/**
 * Adds properties by name, which are read in every selected component. Unknown names are matched exactly
 * (ignoring ASCII case).
 *
 * @throws IllegalArgumentException if a name is invalid, is not a property name, or is `BEGIN` or `END`.
 */
- (CalendrumCoreSelectionBuilder *)propertiesNames:(CalendrumCoreKotlinArray<NSString *> *)names __attribute__((swift_name("properties(names:)")));

/** Sets [timeZones]. */
- (CalendrumCoreSelectionBuilder *)timeZonesSelection:(CalendrumCoreTimeZoneSelection *)selection __attribute__((swift_name("timeZones(selection:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Which `VTIMEZONE` components to keep; [TimeZoneSelection.All] by default. */
@property CalendrumCoreTimeZoneSelection *timeZones __attribute__((swift_name("timeZones")));
@end


/** The presets and the builder of selections. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Selection.Companion")))
@interface CalendrumCoreSelectionCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The presets and the builder of selections. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreSelectionCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumCoreSelectionBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * What a free/busy view needs: `VFREEBUSY` whole, and `VEVENT` with `UID`, `TRANSP`, `STATUS` and the
 * occurrence properties, with the time zones they use.
 */
@property (readonly) CalendrumCoreSelection *FreeBusy __attribute__((swift_name("FreeBusy")));

/** Reads everything. This is the default of a parser. */
@property (readonly) CalendrumCoreSelection *Full __attribute__((swift_name("Full")));

/**
 * A calendar view of events: `VEVENT` with `UID`, `SUMMARY`, `DESCRIPTION`, `LOCATION`, `STATUS`, `SEQUENCE`,
 * `CREATED`, `LAST-MODIFIED`, the occurrence properties and the participants, `X-WR-TIMEZONE` of the calendar,
 * and the time zones the events use.
 */
@property (readonly) CalendrumCoreSelection *Schedule __attribute__((swift_name("Schedule")));

/**
 * What a sync needs to tell versions apart: every component with `UID`, `SEQUENCE`, `LAST-MODIFIED`, `DTSTAMP`
 * and `RECURRENCE-ID`, and no time zones.
 */
@property (readonly) CalendrumCoreSelection *SyncMetadata __attribute__((swift_name("SyncMetadata")));
@end


/** Which `VTIMEZONE` components a selective parse keeps. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeZoneSelection")))
@interface CalendrumCoreTimeZoneSelection : CalendrumCoreKotlinEnum<CalendrumCoreTimeZoneSelection *>
+ (instancetype)alloc __attribute__((unavailable));

/** Which `VTIMEZONE` components a selective parse keeps. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreTimeZoneSelection *all __attribute__((swift_name("all")));
@property (class, readonly) CalendrumCoreTimeZoneSelection *referenced __attribute__((swift_name("referenced")));
@property (class, readonly) CalendrumCoreTimeZoneSelection *none __attribute__((swift_name("none")));
+ (CalendrumCoreKotlinArray<CalendrumCoreTimeZoneSelection *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreTimeZoneSelection *> *entries __attribute__((swift_name("entries")));
@end


/**
 * Supplies a `VTIMEZONE` component for a time zone identifier, so that a written calendar can be self-contained ([RFC
 * 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 *
 * Used by `WriteOptions.Builder.addMissingTimeZones`. The `calendrum-tzdata` module provides one for every IANA zone.
 */
__attribute__((swift_name("VTimeZoneSource")))
@protocol CalendrumCoreVTimeZoneSource
@required

/**
 * Returns the `VTIMEZONE` for [tzid], or `null` when this source does not know the zone.
 *
 * @param tzid the value of a `TZID` parameter.
 */
- (CalendrumCoreICalComponent * _Nullable)vtimezoneTzid:(NSString *)tzid __attribute__((swift_name("vtimezone(tzid:)")));
@end


/**
 * Settings of a writer: byte stability, the order of time zones, interceptors, partial output and missing time zones.
 *
 * Build options with [builder]; [Default] writes byte-stable. The class is immutable and can be shared between threads.
 *
 * @property byteStable whether lines read from input are written exactly as they were.
 * @property timeZonesFirst whether `VTIMEZONE` components are written before the other children of a `VCALENDAR`.
 * @property interceptors the factories of the line interceptors, applied in order.
 * @property allowPartialWrite whether a tree produced by a selective parse may be written.
 * @property missingTimeZones where a `VTIMEZONE` for a `TZID` that the calendar does not define comes from, or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("WriteOptions")))
@interface CalendrumCoreWriteOptions : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreWriteOptionsCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL allowPartialWrite __attribute__((swift_name("allowPartialWrite")));
@property (readonly) BOOL byteStable __attribute__((swift_name("byteStable")));

/**
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
@property (readonly) NSArray<id<CalendrumCoreLineInterceptor> (^)(void)> *interceptors __attribute__((swift_name("interceptors")));
@property (readonly) id<CalendrumCoreVTimeZoneSource> _Nullable missingTimeZones __attribute__((swift_name("missingTimeZones")));
@property (readonly) BOOL timeZonesFirst __attribute__((swift_name("timeZonesFirst")));
@end


/**
 * Collects [WriteOptions] values. The properties and the functions of the same name do the same; the functions
 * chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("WriteOptions.Builder")))
@interface CalendrumCoreWriteOptionsBuilder : CalendrumCoreBase

/**
 * Sets [missingTimeZones]: when a calendar is written, a `VTIMEZONE` that [source] returns is added for each
 * `TZID` that the calendar uses but does not define, placed before the other children. `null` turns it off.
 */
- (CalendrumCoreWriteOptionsBuilder *)addMissingTimeZonesSource:(id<CalendrumCoreVTimeZoneSource> _Nullable)source __attribute__((swift_name("addMissingTimeZones(source:)")));

/** Sets [allowPartialWrite]. */
- (CalendrumCoreWriteOptionsBuilder *)allowPartialWriteAllow:(BOOL)allow __attribute__((swift_name("allowPartialWrite(allow:)")));

/** Creates the options. */
- (CalendrumCoreWriteOptions *)build __attribute__((swift_name("build()")));

/** Sets [byteStable]. */
- (CalendrumCoreWriteOptionsBuilder *)byteStableByteStable:(BOOL)byteStable __attribute__((swift_name("byteStable(byteStable:)")));

/** Adds a stateless [interceptor]; the same instance serves every write and must be thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumCoreWriteOptionsBuilder *)interceptInterceptor:(id<CalendrumCoreLineInterceptor>)interceptor __attribute__((swift_name("intercept(interceptor:)")));

/**
 * Adds an interceptor created by [factory] for every write, so that a stateful interceptor gets a fresh
 * instance each time.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumCoreWriteOptionsBuilder *)interceptFactory:(id<CalendrumCoreLineInterceptor> (^)(void))factory __attribute__((swift_name("intercept(factory:)")));

/** Sets [timeZonesFirst]. */
- (CalendrumCoreWriteOptionsBuilder *)timeZonesFirstTimeZonesFirst:(BOOL)timeZonesFirst __attribute__((swift_name("timeZonesFirst(timeZonesFirst:)")));

/**
 * Allow writing a tree from a selective parse, which lacks what was not selected; false by default, in which
 * case writing such a tree throws.
 */
@property BOOL allowPartialWrite __attribute__((swift_name("allowPartialWrite")));

/**
 * Write lines that came from input as they were (spelling, quoting, folding), so an unchanged tree comes back
 * byte for byte; true by default. When false every line is written canonically: folded at 75 octets, with
 * parameters quoted as needed. Line ends are always CRLF. Nodes built by hand or edited are always written
 * canonically.
 */
@property BOOL byteStable __attribute__((swift_name("byteStable")));

/**
 * The source of a `VTIMEZONE` for every `TZID` used in a calendar but not defined in it; `null` (the default)
 * adds none.
 */
@property id<CalendrumCoreVTimeZoneSource> _Nullable missingTimeZones __attribute__((swift_name("missingTimeZones")));

/** Write the `VTIMEZONE` children of a `VCALENDAR` before its other children; false by default. */
@property BOOL timeZonesFirst __attribute__((swift_name("timeZonesFirst")));
@end


/** The shared defaults and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("WriteOptions.Companion")))
@interface CalendrumCoreWriteOptionsCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The shared defaults and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreWriteOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the defaults. */
- (CalendrumCoreWriteOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** Byte-stable writing without interceptors, partial output or added time zones. */
@property (readonly) CalendrumCoreWriteOptions *Default __attribute__((swift_name("Default")));
@end


/**
 * A time zone as the library uses it: an identifier and the rules that map instants to UTC offsets and back.
 *
 * Instances come from a [TimeZoneResolver]; they are not tied to the system time zone, which the library never uses on
 * its own. A zone is immutable and thread-safe.
 *
 * Users implement this interface for a resolver of their own. Its set of members is frozen for 1.0: a later release
 * does not add an abstract member that would break such implementations.
 */
__attribute__((swift_name("ICalTimeZone")))
@protocol CalendrumCoreICalTimeZone
@required

/** Returns the UTC offset in force at [instant]. */
- (CalendrumCoreKotlinx_datetimeUtcOffset *)offsetAtInstant:(CalendrumCoreKotlinInstant *)instant __attribute__((swift_name("offsetAt(instant:)")));

/**
 * Returns the instant of the wall-clock time [local] in this zone.
 *
 * A time that the zone skips (a daylight saving gap) is read with the offset before the gap, so it lands after the
 * gap; an ambiguous time (an overlap) takes the first of its two instants, as [RFC 5545
 * §3.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.5) says.
 */
- (CalendrumCoreKotlinInstant *)toInstantLocal:(CalendrumCoreKotlinx_datetimeLocalDateTime *)local __attribute__((swift_name("toInstant(local:)")));

/** Returns the changes of offset from [from] up to, excluding, [to], in time order. */
- (NSArray<CalendrumCoreZoneTransition *> *)transitionsFrom:(CalendrumCoreKotlinInstant *)from to:(CalendrumCoreKotlinInstant *)to __attribute__((swift_name("transitions(from:to:)")));

/** The identifier the zone was requested with, usually the `TZID` of the file. */
@property (readonly) NSString *id __attribute__((swift_name("id")));
@end


/**
 * What a resolver may need besides the identifier: the calendar the `TZID` comes from and where to report problems
 * (`ICAL-Z001` to `ICAL-Z004`).
 *
 * @property calendar the calendar being read, or `null`.
 * @property problemListener receives the problems, or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResolveContext")))
@interface CalendrumCoreResolveContext : CalendrumCoreBase
- (instancetype)initWithCalendar:(CalendrumCoreVCalendar * _Nullable)calendar problemListener:(id<CalendrumCoreProblemListener> _Nullable)problemListener __attribute__((swift_name("init(calendar:problemListener:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreResolveContextCompanion *companion __attribute__((swift_name("companion")));

/** Returns the name of the class. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreVCalendar * _Nullable calendar __attribute__((swift_name("calendar")));
@property (readonly) id<CalendrumCoreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));
@end


/** Shared contexts. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResolveContext.Companion")))
@interface CalendrumCoreResolveContextCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Shared contexts. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreResolveContextCompanion *shared __attribute__((swift_name("shared")));

/** A context without a calendar and a listener. */
@property (readonly) CalendrumCoreResolveContext *Empty __attribute__((swift_name("Empty")));
@end


/**
 * Finds the rules of a time zone by its `TZID`.
 *
 * Resolvers are combined in a chain with [TimeZoneResolvers.chain]. Implementations must be thread-safe and must not
 * use the system time zone.
 */
__attribute__((swift_name("TimeZoneResolver")))
@protocol CalendrumCoreTimeZoneResolver
@required

/**
 * Returns the zone for [tzid], or `null` when this resolver does not know it, so that the next resolver of a chain
 * can try.
 *
 * @param tzid the value of a `TZID` parameter.
 * @param context the calendar being read and the problem listener.
 * @throws kotlinx.datetime.IllegalTimeZoneException if the identifier is unknown and the policy of the chain is
 *   [UnknownZonePolicy.Fail].
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (id<CalendrumCoreICalTimeZone> _Nullable)resolveTzid:(NSString *)tzid context:(CalendrumCoreResolveContext *)context error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("resolve(tzid:context:)"))) __attribute__((swift_error(nonnull_error)));
@end


/**
 * Ready-made [TimeZoneResolver]s and the chain that combines them.
 *
 * The library never asks the operating system for a time zone on its own: `default` resolves from the file and its
 * aliases only, and the IANA zones of the platform or of the `calendrum-tzdata` module join a chain when you add them. An
 * application that does not ask for them does not pay for the platform zone database.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeZoneResolvers")))
@interface CalendrumCoreTimeZoneResolvers : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Ready-made [TimeZoneResolver]s and the chain that combines them.
 *
 * The library never asks the operating system for a time zone on its own: `default` resolves from the file and its
 * aliases only, and the IANA zones of the platform or of the `calendrum-tzdata` module join a chain when you add them. An
 * application that does not ask for them does not pay for the platform zone database.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)timeZoneResolvers __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreTimeZoneResolvers *shared __attribute__((swift_name("shared")));

/**
 * Combines [resolvers] into one that asks them in order and applies [unknown] when none of them knows the `TZID`.
 *
 * @param resolvers the resolvers, tried first to last.
 * @param unknown what to do for a `TZID` no resolver knows.
 */
- (id<CalendrumCoreTimeZoneResolver>)chainResolvers:(CalendrumCoreKotlinArray<id<CalendrumCoreTimeZoneResolver>> *)resolvers unknown:(CalendrumCoreUnknownZonePolicy *)unknown __attribute__((swift_name("chain(resolvers:unknown:)")));

/**
 * Returns the default resolver: the `VTIMEZONE` components of [calendar], then the aliases of the zones in them,
 * and `null` (floating time) for any other `TZID`. Without a calendar it knows nothing.
 *
 * No IANA zone database is consulted, so the result does not depend on the platform. From Java call it as
 * `defaultFor`.
 */
- (id<CalendrumCoreTimeZoneResolver>)defaultCalendar:(CalendrumCoreVCalendar * _Nullable)calendar __attribute__((swift_name("default(calendar:)")));

/**
 * Returns a resolver that compiles the `VTIMEZONE` components of [calendar] and resolves a `TZID` to the rules they
 * describe.
 *
 * A `VTIMEZONE` that cannot be compiled is skipped with `ICAL-Z003`. Compilation happens once per calendar, on
 * first use.
 */
- (id<CalendrumCoreTimeZoneResolver>)vtimezoneCalendar:(CalendrumCoreVCalendar *)calendar __attribute__((swift_name("vtimezone(calendar:)")));

/**
 * A resolver for the names real-world calendars use instead of IANA identifiers: Windows and Outlook names, IANA
 * links and `/mozilla.org/` prefixes. It resolves them through the [iana] resolver and reports `ICAL-Z002`.
 */
@property (readonly) id<CalendrumCoreTimeZoneResolver> aliases __attribute__((swift_name("aliases")));

/**
 * A resolver for IANA zone names such as `Europe/Berlin`, backed by the time zone database of the platform through
 * kotlinx-datetime.
 *
 * It does not know Windows names or aliases; combine it with [aliases]. Results can differ between platforms and
 * database versions; for the same answer everywhere use the resolver of `calendrum-tzdata`.
 */
@property (readonly) id<CalendrumCoreTimeZoneResolver> iana __attribute__((swift_name("iana")));
@end


/** What a chain of resolvers does with a `TZID` that none of them knows (`ICAL-Z001`). */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UnknownZonePolicy")))
@interface CalendrumCoreUnknownZonePolicy : CalendrumCoreKotlinEnum<CalendrumCoreUnknownZonePolicy *>
+ (instancetype)alloc __attribute__((unavailable));

/** What a chain of resolvers does with a `TZID` that none of them knows (`ICAL-Z001`). */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreUnknownZonePolicy *floating __attribute__((swift_name("floating")));
@property (class, readonly) CalendrumCoreUnknownZonePolicy *utc __attribute__((swift_name("utc")));
@property (class, readonly) CalendrumCoreUnknownZonePolicy *fail __attribute__((swift_name("fail")));
+ (CalendrumCoreKotlinArray<CalendrumCoreUnknownZonePolicy *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreUnknownZonePolicy *> *entries __attribute__((swift_name("entries")));
@end


/**
 * A change of the UTC offset of a zone, such as the start or the end of daylight saving time.
 *
 * @property at the instant at which the new offset takes effect.
 * @property offsetBefore the offset just before [at].
 * @property offsetAfter the offset from [at] on.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ZoneTransition")))
@interface CalendrumCoreZoneTransition : CalendrumCoreBase
- (instancetype)initWithAt:(CalendrumCoreKotlinInstant *)at offsetBefore:(CalendrumCoreKotlinx_datetimeUtcOffset *)offsetBefore offsetAfter:(CalendrumCoreKotlinx_datetimeUtcOffset *)offsetAfter __attribute__((swift_name("init(at:offsetBefore:offsetAfter:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a transition at the same instant with the same offsets. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the instant and the offsets. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the instant and the offsets, such as `ZoneTransition(2026-03-29T01:00:00Z, +01:00 -> +02:00)`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinInstant *at __attribute__((swift_name("at")));
@property (readonly) CalendrumCoreKotlinx_datetimeUtcOffset *offsetAfter __attribute__((swift_name("offsetAfter")));
@property (readonly) CalendrumCoreKotlinx_datetimeUtcOffset *offsetBefore __attribute__((swift_name("offsetBefore")));
@end


/**
 * The address of an element of a calendar, such as `VCALENDAR[0]/VEVENT[3]/ATTENDEE[1]@PARTSTAT`: components and
 * properties by name and index among those of the same name, and optionally a parameter.
 *
 * A path of a missing element has the index 0 and points where the element would be. Immutable.
 *
 * @property segments the components and the property from the root, in order.
 * @property parameter the name of the parameter, or `null` when the path ends at a component or property.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ElementPath")))
@interface CalendrumCoreElementPath : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreElementPathCompanion *companion __attribute__((swift_name("companion")));

/** Returns the path of the child with the given [name] and [index] among children with that name. */
- (CalendrumCoreElementPath *)childName:(NSString *)name index:(int32_t)index __attribute__((swift_name("child(name:index:)")));

/** Returns `true` when [other] has the same segments and parameter. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the segments and the parameter. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the path of the parameter named [name] of the element this path ends at. */
- (CalendrumCoreElementPath *)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/** Returns the path as `VCALENDAR[0]/VEVENT[3]/ATTENDEE[1]@PARTSTAT`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable parameter __attribute__((swift_name("parameter")));
@property (readonly) NSArray<CalendrumCoreElementPathSegment *> *segments __attribute__((swift_name("segments")));
@end


/** Creates paths. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ElementPath.Companion")))
@interface CalendrumCoreElementPathCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates paths. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreElementPathCompanion *shared __attribute__((swift_name("shared")));

/** Creates a path from [segments]. */
- (CalendrumCoreElementPath *)ofSegments:(CalendrumCoreKotlinArray<CalendrumCoreElementPathSegment *> *)segments __attribute__((swift_name("of(segments:)")));

/** The empty path above the calendar. */
@property (readonly) CalendrumCoreElementPath *Root __attribute__((swift_name("Root")));
@end


/**
 * One step of an [ElementPath].
 *
 * @property name the name of the component or property.
 * @property index the 0-based index among elements of the same name in the parent.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ElementPath.Segment")))
@interface CalendrumCoreElementPathSegment : CalendrumCoreBase
- (instancetype)initWithName:(NSString *)name index:(int32_t)index __attribute__((swift_name("init(name:index:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same name and index. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name and the index. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the segment as `NAME[index]`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/**
 * The rules a calendar is checked against: which properties each component requires, allows and forbids, which
 * components may be nested where, constraints between properties, an optional iTIP method with its tables, and custom
 * rules.
 *
 * The built-in tables are data (`properties.csv`, `components.csv`, `constraints.csv` and `itip.csv`), not code. A rule
 * set is immutable; change one with [toBuilder] or start from [builder].
 *
 * @property method the iTIP method the rules are for, or `null` for plain RFC 5545 rules.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RuleSet")))
@interface CalendrumCoreRuleSet : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRuleSetCompanion *companion __attribute__((swift_name("companion")));

/** Returns a builder that starts from these rules. */
- (CalendrumCoreRuleSetBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns the iTIP method and the number of custom rules. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreMethod * _Nullable method __attribute__((swift_name("method")));
@end


/** Changes a copy of a [RuleSet]. Not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RuleSet.Builder")))
@interface CalendrumCoreRuleSetBuilder : CalendrumCoreBase

/** Creates the rule set. */
- (CalendrumCoreRuleSet *)build __attribute__((swift_name("build()")));

/**
 * Sets how many times [property] may occur in [component] to [value], replacing the rule that was there.
 *
 * @param component the component name, such as `VEVENT`.
 * @param property the property name, such as `SUMMARY`.
 * @param value how often the property occurs.
 */
- (CalendrumCoreRuleSetBuilder *)cardinalityComponent:(NSString *)component property:(NSString *)property value:(CalendrumCoreCardinality *)value __attribute__((swift_name("cardinality(component:property:value:)")));

/**
 * Forbids [property] in [component]; a use of it is reported as `ICAL-L004`.
 *
 * @param component the component name, such as `VEVENT`.
 * @param property the property name, such as `ATTACH`.
 */
- (CalendrumCoreRuleSetBuilder *)forbidComponent:(NSString *)component property:(NSString *)property __attribute__((swift_name("forbid(component:property:)")));

/** Adds a custom [rule], which runs after the built-in checks on every component. */
- (CalendrumCoreRuleSetBuilder *)ruleRule:(id<CalendrumCoreValidationRule>)rule __attribute__((swift_name("rule(rule:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** The built-in rule sets and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RuleSet.Companion")))
@interface CalendrumCoreRuleSetCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** The built-in rule sets and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRuleSetCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from [Rfc5545]. */
- (CalendrumCoreRuleSetBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns the rules of RFC 5545 plus the table of RFC 5546 for [method]: the calendar must declare that
 * `METHOD`, and which components and properties it may hold depends on the method (`ICAL-L018`, `ICAL-L020`).
 *
 * @param method the iTIP method to check for.
 */
- (CalendrumCoreRuleSet *)itipMethod:(CalendrumCoreMethod *)method __attribute__((swift_name("itip(method:)")));

/** The rules of RFC 5545, without a method. */
@property (readonly) CalendrumCoreRuleSet *Rfc5545 __attribute__((swift_name("Rfc5545")));
@end


/**
 * One problem found by a validation.
 *
 * @property code the code of the `ICAL-L` series.
 * @property severity how serious it is; the severity of the code.
 * @property path the element it refers to.
 * @property span where the element is in the input, or `null` for a calendar that was not parsed.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ValidationIssue")))
@interface CalendrumCoreValidationIssue : CalendrumCoreBase
- (instancetype)initWithCode:(CalendrumCoreProblemCode *)code severity:(CalendrumCoreSeverity *)severity path:(CalendrumCoreElementPath *)path span:(CalendrumCoreSourceSpan * _Nullable)span __attribute__((swift_name("init(code:severity:path:span:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same code, severity, path and span. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the code, the path and the severity. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the code, severity, message and path; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreProblemCode *code __attribute__((swift_name("code")));
@property (readonly) CalendrumCoreElementPath *path __attribute__((swift_name("path")));
@property (readonly) CalendrumCoreSeverity *severity __attribute__((swift_name("severity")));
@property (readonly) CalendrumCoreSourceSpan * _Nullable span __attribute__((swift_name("span")));
@end


/**
 * The result of a validation: the issues found.
 *
 * An immutable list of issues; the calendar is valid when none has the severity [io.github.calendrum.Severity.Error].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ValidationReport")))
@interface CalendrumCoreValidationReport : CalendrumCoreBase

/** Returns the issues with the given [code]. */
- (NSArray<CalendrumCoreValidationIssue *> *)issuesCode:(CalendrumCoreProblemCode *)code __attribute__((swift_name("issues(code:)")));

/** Returns the number of issues and whether the calendar is valid. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Whether there is no issue with the severity `Error`. Warnings and infos do not make a calendar invalid. */
@property (readonly) BOOL isValid __attribute__((swift_name("isValid")));

/** All issues, in the order they were found. */
@property (readonly) NSArray<CalendrumCoreValidationIssue *> *issues __attribute__((swift_name("issues")));
@end


/**
 * A custom check that runs on every component of the calendar, for rules of your own such as house style.
 *
 * Add one with [RuleSet.Builder.rule].
 */
__attribute__((swift_name("ValidationRule")))
@protocol CalendrumCoreValidationRule
@required

/**
 * Checks one component.
 *
 * @param component the component being checked.
 * @param path where the component is in the calendar.
 * @return the issues found; empty when the component is fine.
 */
- (NSArray<CalendrumCoreValidationIssue *> *)validateComponent:(CalendrumCoreICalComponent *)component path:(CalendrumCoreElementPath *)path __attribute__((swift_name("validate(component:path:)")));
@end


/**
 * Checks a calendar against a set of rules and reports what is wrong, without ever throwing.
 *
 * The default rules are those of RFC 5545 ([RuleSet.Rfc5545]); [RuleSet.itip] adds the tables of RFC 5546 for one
 * method, and [RuleSet.builder] makes your own. Validation looks at a parsed calendar and does not change it.
 *
 * @sample io.github.calendrum.samples.validateCalendar
 * @sample io.github.calendrum.samples.validateWithHouseRules
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Validator")))
@interface CalendrumCoreValidator : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Checks a calendar against a set of rules and reports what is wrong, without ever throwing.
 *
 * The default rules are those of RFC 5545 ([RuleSet.Rfc5545]); [RuleSet.itip] adds the tables of RFC 5546 for one
 * method, and [RuleSet.builder] makes your own. Validation looks at a parsed calendar and does not change it.
 *
 * @sample io.github.calendrum.samples.validateCalendar
 * @sample io.github.calendrum.samples.validateWithHouseRules
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)validator __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreValidator *shared __attribute__((swift_name("shared")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Validates [calendar] against [rules] and returns the issues found.
 *
 * Each issue has a code of the `ICAL-L` series, a severity, the path of the offending element and, for a parsed
 * calendar, its position. At most one issue is reported for each code and path. Properties that do not decode are
 * reported as `ICAL-L016`; the details are the `ICAL-V` problems of reading. Time zones are resolved with the
 * default resolver of the calendar, so a `DTEND` in another zone than `DTSTART` is compared correctly, and a
 * comparison that needs an unknown zone is skipped.
 *
 * @param calendar the calendar to check.
 * @param rules the rules; RFC 5545 by default. From Java call `Validator.validate(calendar)` or pass the rules.
 * @sample io.github.calendrum.samples.validateCalendar
 */
- (CalendrumCoreValidationReport *)validateCalendar:(CalendrumCoreVCalendar *)calendar rules:(CalendrumCoreRuleSet *)rules __attribute__((swift_name("validate(calendar:rules:)")));
@end


/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Frequency")))
@interface CalendrumCoreFrequency : CalendrumCoreKotlinEnum<CalendrumCoreFrequency *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreFrequency *secondly __attribute__((swift_name("secondly")));
@property (class, readonly) CalendrumCoreFrequency *minutely __attribute__((swift_name("minutely")));
@property (class, readonly) CalendrumCoreFrequency *hourly __attribute__((swift_name("hourly")));
@property (class, readonly) CalendrumCoreFrequency *daily __attribute__((swift_name("daily")));
@property (class, readonly) CalendrumCoreFrequency *weekly __attribute__((swift_name("weekly")));
@property (class, readonly) CalendrumCoreFrequency *monthly __attribute__((swift_name("monthly")));
@property (class, readonly) CalendrumCoreFrequency *yearly __attribute__((swift_name("yearly")));
+ (CalendrumCoreKotlinArray<CalendrumCoreFrequency *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreFrequency *> *entries __attribute__((swift_name("entries")));
@end


/**
 * A `DATE` or `DATE-TIME` value in one of the four forms the standard allows ([RFC 5545
 * §3.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.4),
 * [§3.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.5)): [Date], [Floating], [Utc] and [Zoned].
 *
 * The set of forms is closed by the standard, so a `when` over it is exhaustive. None of them is an instant by itself
 * except [Utc]; to get one from the others a time zone is needed (see `toInstant` with a resolver). The library never
 * uses the system time zone. All forms are immutable and compare by content, so [Floating] and [Zoned] values with the
 * same fields are equal even if they would be different instants. Years outside 0000 to 9999 cannot be written to
 * iCalendar text.
 */
__attribute__((swift_name("ICalDateTime")))
@protocol CalendrumCoreICalDateTime
@required
@end


/**
 * A calendar date without a time of day: `VALUE=DATE`, such as `20261001`. A date-only start usually means an
 * all-day event.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalDateTimeDate")))
@interface CalendrumCoreICalDateTimeDate : CalendrumCoreBase <CalendrumCoreICalDateTime>

/** Creates the value of [date]. */
- (instancetype)initWithDate:(CalendrumCoreKotlinx_datetimeLocalDate *)date __attribute__((swift_name("init(date:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a date with the same year, month and day. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over year, month and day. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the date in extended ISO 8601 form, such as `2026-10-01`; this is not the iCalendar text form. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The calendar date. */
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDate *date __attribute__((swift_name("date")));
@end


/**
 * A local date and time that is not tied to a time zone: `20261001T100000`. It means "10:00 wherever the reader
 * is".
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalDateTimeFloating")))
@interface CalendrumCoreICalDateTimeFloating : CalendrumCoreBase <CalendrumCoreICalDateTime>

/** Creates the value of [dateTime]. */
- (instancetype)initWithDateTime:(CalendrumCoreKotlinx_datetimeLocalDateTime *)dateTime __attribute__((swift_name("init(dateTime:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a floating value with the same date and time of day. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over date and time of day. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the date and time in extended ISO 8601 form, such as `2026-10-01T10:00`; this is not the iCalendar
 * text form.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The local date and time. */
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDateTime *dateTime __attribute__((swift_name("dateTime")));
@end


/**
 * A date and time in UTC, written with a trailing `Z`: `20261001T070000Z`. The only form that is an instant by
 * itself.
 *
 * @property instant the moment.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalDateTimeUtc")))
@interface CalendrumCoreICalDateTimeUtc : CalendrumCoreBase <CalendrumCoreICalDateTime>
- (instancetype)initWithInstant:(CalendrumCoreKotlinInstant *)instant __attribute__((swift_name("init(instant:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a UTC value of the same instant. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the instant. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the instant in ISO 8601 form, such as `2026-10-01T07:00:00Z`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinInstant *instant __attribute__((swift_name("instant")));
@end


/**
 * A local date and time in the time zone named by the `TZID` parameter:
 * `DTSTART;TZID=Europe/Berlin:20261001T100000`.
 *
 * The name is kept as written; resolving it to rules needs a time zone resolver, since it may name a `VTIMEZONE` of
 * the file, an IANA zone or a vendor alias.
 *
 * @property tzid the `TZID` as written, never a resolved zone.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalDateTimeZoned")))
@interface CalendrumCoreICalDateTimeZoned : CalendrumCoreBase <CalendrumCoreICalDateTime>

/** Creates the value of [dateTime] in the zone named [tzid]. */
- (instancetype)initWithDateTime:(CalendrumCoreKotlinx_datetimeLocalDateTime *)dateTime tzid:(NSString *)tzid __attribute__((swift_name("init(dateTime:tzid:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a zoned value with the same date, time of day and `TZID` text. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over date, time of day and `TZID`. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the date and time followed by the zone name in brackets, such as `2026-10-01T10:00[Europe/Berlin]`.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The local date and time in the named zone. */
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDateTime *dateTime __attribute__((swift_name("dateTime")));
@property (readonly) NSString *tzid __attribute__((swift_name("tzid")));
@end


/**
 * A `DURATION` value: a signed amount of weeks, days, hours, minutes and seconds ([RFC 5545
 * §3.3.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.6)), such as `PT1H30M` or `-P2D`.
 *
 * Days and weeks are nominal: their length depends on the time zone and the date (a day can have 23 or 25 hours), so
 * they cannot become an exact `kotlin.time.Duration`. The value keeps which units it was written with, so reading and writing it
 * again gives the same text. Immutable. Equality compares the sign, the amounts and those units.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalDuration")))
@interface CalendrumCoreICalDuration : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreICalDurationCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a duration with the same sign, amounts and written units. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the sign, the amounts and the written units. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the duration as a kotlinx-datetime period with the sign applied to every unit; weeks become days (7
 * each).
 */
- (CalendrumCoreKotlinx_datetimeDateTimePeriod *)toDateTimePeriod __attribute__((swift_name("toDateTimePeriod()")));

/**
 * Returns the exact length, or `null` when the duration has weeks or days, whose length depends on the time zone.
 */
- (id _Nullable)toDurationOrNull __attribute__((swift_name("toDurationOrNull()")));

/** Returns the duration in iCalendar text form, such as `PT1H30M`. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The number of days, never negative. */
@property (readonly) int32_t days __attribute__((swift_name("days")));

/** The number of hours, never negative. */
@property (readonly) int32_t hours __attribute__((swift_name("hours")));

/** Whether the duration is negative, written with a leading `-`. */
@property (readonly) BOOL isNegative __attribute__((swift_name("isNegative")));

/** The number of minutes, never negative. */
@property (readonly) int32_t minutes __attribute__((swift_name("minutes")));

/** The number of seconds, never negative. */
@property (readonly) int32_t seconds __attribute__((swift_name("seconds")));

/** The number of weeks, never negative; the sign is in [isNegative]. The standard allows weeks only alone. */
@property (readonly) int32_t weeks __attribute__((swift_name("weeks")));
@end


/** Creates durations by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalDuration.Companion")))
@interface CalendrumCoreICalDurationCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates durations by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreICalDurationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates the duration that equals [duration] in hours, minutes and seconds.
 *
 * @throws IllegalArgumentException if [duration] is infinite, has a fraction of a second, or is longer than
 *   `Int.MAX_VALUE` hours.
 */
- (CalendrumCoreICalDuration *)ofDuration:(int64_t)duration __attribute__((swift_name("of(duration:)")));

/**
 * Creates a duration of [days] days; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [days] is `Int.MIN_VALUE`.
 */
- (CalendrumCoreICalDuration *)ofDaysDays:(int32_t)days __attribute__((swift_name("ofDays(days:)")));

/**
 * Creates a duration of [hours] hours; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [hours] is `Int.MIN_VALUE`.
 */
- (CalendrumCoreICalDuration *)ofHoursHours:(int32_t)hours __attribute__((swift_name("ofHours(hours:)")));

/**
 * Creates a duration of [minutes] minutes; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [minutes] is `Int.MIN_VALUE`.
 */
- (CalendrumCoreICalDuration *)ofMinutesMinutes:(int32_t)minutes __attribute__((swift_name("ofMinutes(minutes:)")));

/**
 * Creates a duration of [seconds] seconds; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [seconds] is `Int.MIN_VALUE`.
 */
- (CalendrumCoreICalDuration *)ofSecondsSeconds:(int32_t)seconds __attribute__((swift_name("ofSeconds(seconds:)")));

/**
 * Creates a duration of [weeks] weeks; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [weeks] is `Int.MIN_VALUE`.
 */
- (CalendrumCoreICalDuration *)ofWeeksWeeks:(int32_t)weeks __attribute__((swift_name("ofWeeks(weeks:)")));
@end


/**
 * A `PERIOD` value: a start and either an end or a duration ([RFC 5545
 * §3.3.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.9)), such as `19970101T180000Z/PT5H30M`.
 *
 * Exactly one of [end] and [duration] is set. Immutable.
 *
 * @property start where the period begins; [ICalDateTime.Utc] or a local form.
 * @property end where the period ends, or `null` when the period is given by a [duration].
 * @property duration how long the period lasts, or `null` when it is given by an [end].
 * @throws IllegalArgumentException if both or neither of [end] and [duration] are given.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalPeriod")))
@interface CalendrumCoreICalPeriod : CalendrumCoreBase
- (instancetype)initWithStart:(id<CalendrumCoreICalDateTime>)start end:(id<CalendrumCoreICalDateTime> _Nullable)end duration:(CalendrumCoreICalDuration * _Nullable)duration __attribute__((swift_name("init(start:end:duration:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a period with equal start, end and duration. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the start, the end and the duration. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the start and the end or duration separated by a slash, in the forms of [ICalDateTime.toString] and
 * [ICalDuration.toString].
 */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreICalDuration * _Nullable duration __attribute__((swift_name("duration")));
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable end __attribute__((swift_name("end")));
@property (readonly) id<CalendrumCoreICalDateTime> start __attribute__((swift_name("start")));
@end


/**
 * An entry of `BYMONTH`: a month number, which with `RSCALE` of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529) can
 * be a leap month, written `5L`.
 *
 * @property month the month number, 1 to 12 in the Gregorian calendar.
 * @property isLeap whether this is a leap month; always false in the Gregorian calendar.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MonthNum")))
@interface CalendrumCoreMonthNum : CalendrumCoreBase
- (instancetype)initWithMonth:(int32_t)month isLeap:(BOOL)isLeap __attribute__((swift_name("init(month:isLeap:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same month and leap flag. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the month and the leap flag. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the entry as written in a rule, such as `5` or `5L`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL isLeap __attribute__((swift_name("isLeap")));
@property (readonly) int32_t month __attribute__((swift_name("month")));
@end


/**
 * A recurrence rule: the value of `RRULE` and `EXRULE` ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)), together with `RSCALE` and `SKIP` of [RFC
 * 7529](https://www.rfc-editor.org/rfc/rfc7529).
 *
 * Read one from a property with the typed model, or build one with [builder], which checks the combinations the
 * standard forbids. Rules read from input are accepted leniently (see `ICAL-V017` to `ICAL-V020`), so a rule read from
 * a file may hold combinations the builder would reject. A `Recur` is immutable; expanding it into dates is done by
 * `expand` in the recurrence package.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Recur")))
@interface CalendrumCoreRecur : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreRecurCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a rule with the same parts in the same form. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over all parts. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the rule in iCalendar text form, such as `FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE`; unknown parts are left out.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The `BYDAY` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreWeekdayNum *> *byDay __attribute__((swift_name("byDay")));

/** The `BYHOUR` values, 0 to 23; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreInt *> *byHour __attribute__((swift_name("byHour")));

/** The `BYMINUTE` values, 0 to 59; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreInt *> *byMinute __attribute__((swift_name("byMinute")));

/** The `BYMONTH` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreMonthNum *> *byMonth __attribute__((swift_name("byMonth")));

/** The `BYMONTHDAY` values, 1 to 31 or -31 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreInt *> *byMonthDay __attribute__((swift_name("byMonthDay")));

/** The `BYSECOND` values, 0 to 60; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreInt *> *bySecond __attribute__((swift_name("bySecond")));

/** The `BYSETPOS` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreInt *> *bySetPos __attribute__((swift_name("bySetPos")));

/** The `BYWEEKNO` values, 1 to 53 or -53 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreInt *> *byWeekNo __attribute__((swift_name("byWeekNo")));

/** The `BYYEARDAY` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumCoreInt *> *byYearDay __attribute__((swift_name("byYearDay")));

/** The `COUNT`, or `null` when the rule has none. */
@property (readonly) CalendrumCoreInt * _Nullable count __attribute__((swift_name("count")));

/** The `FREQ` of the rule. */
@property (readonly) CalendrumCoreFrequency *frequency __attribute__((swift_name("frequency")));

/** The `INTERVAL`, 1 when the rule has none. */
@property (readonly) int32_t interval __attribute__((swift_name("interval")));

/**
 * The `RSCALE` calendar name of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529), or `null` for the Gregorian
 * calendar.
 */
@property (readonly) NSString * _Nullable rscale __attribute__((swift_name("rscale")));

/** The `SKIP` rule part, or `null` when the rule has none. */
@property (readonly) CalendrumCoreSkip * _Nullable skip __attribute__((swift_name("skip")));

/** The parts the library does not know, in the order they were written; empty for none. */
@property (readonly) NSArray<CalendrumCoreRecurPart *> *unknownParts __attribute__((swift_name("unknownParts")));

/** The `UNTIL` of the rule: a date, a local date-time or a UTC date-time, or `null` when the rule has none. */
@property (readonly) id<CalendrumCoreICalDateTime> _Nullable until __attribute__((swift_name("until")));

/** The `WKST`, or `null` when the rule has none (Monday is then the default of the standard). */
@property (readonly) CalendrumCoreKotlinx_datetimeDayOfWeek * _Nullable weekStart __attribute__((swift_name("weekStart")));
@end


/**
 * Builds a [Recur] that is correct by construction: the combinations the standard forbids are rejected as they are
 * added. Not thread-safe; obtain one with [Recur.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Recur.Builder")))
@interface CalendrumCoreRecurBuilder : CalendrumCoreBase

/**
 * Creates the rule.
 *
 * @throws IllegalStateException if `BYSETPOS` is used without another `BYxxx` part, or `BYWEEKNO` is combined
 *   with a numbered `BYDAY`.
 */
- (CalendrumCoreRecur *)build __attribute__((swift_name("build()")));

/**
 * Sets `BYDAY` to the given entries, which may carry a number such as the second Tuesday.
 *
 * @throws IllegalArgumentException if no entry is given, a number is used with a frequency other than `MONTHLY`
 *   or `YEARLY`, or a number is 0 or outside -53 to 53.
 */
- (CalendrumCoreRecurBuilder *)byDayDays:(CalendrumCoreKotlinArray<CalendrumCoreWeekdayNum *> *)days __attribute__((swift_name("byDay(days:)")));

/**
 * Sets `BYDAY` to the given days, every such weekday.
 *
 * @throws IllegalArgumentException if no day is given.
 */
- (CalendrumCoreRecurBuilder *)byDayDaysOfWeek:(CalendrumCoreKotlinArray<CalendrumCoreKotlinx_datetimeDayOfWeek *> *)daysOfWeek __attribute__((swift_name("byDay(daysOfWeek:)")));

/**
 * Sets `BYHOUR` to [values], each 0 to 23.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumCoreRecurBuilder *)byHourValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("byHour(values:)")));

/**
 * Sets `BYMINUTE` to [values], each 0 to 59.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumCoreRecurBuilder *)byMinuteValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("byMinute(values:)")));

/**
 * Sets `BYMONTH` to [values], each 1 to 12.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumCoreRecurBuilder *)byMonthValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("byMonth(values:)")));

/**
 * Sets `BYMONTHDAY` to [values], each 1 to 31 or -31 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `WEEKLY`, there is no value or one is out of range.
 */
- (CalendrumCoreRecurBuilder *)byMonthDayValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("byMonthDay(values:)")));

/**
 * Sets `BYSECOND` to [values], each 0 to 60.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumCoreRecurBuilder *)bySecondValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("bySecond(values:)")));

/**
 * Sets `BYSETPOS` to [values], each 1 to 366 or -366 to -1. The rule also needs another `BYxxx` part.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumCoreRecurBuilder *)bySetPosValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("bySetPos(values:)")));

/**
 * Sets `BYWEEKNO` to [values], each 1 to 53 or -53 to -1.
 *
 * @throws IllegalArgumentException if the frequency is not `YEARLY`, there is no value or one is out of range.
 */
- (CalendrumCoreRecurBuilder *)byWeekNoValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("byWeekNo(values:)")));

/**
 * Sets `BYYEARDAY` to [values], each 1 to 366 or -366 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `DAILY`, `WEEKLY` or `MONTHLY`, there is no value or one
 *   is out of range.
 */
- (CalendrumCoreRecurBuilder *)byYearDayValues:(CalendrumCoreKotlinIntArray *)values __attribute__((swift_name("byYearDay(values:)")));

/**
 * Sets `COUNT`, the number of occurrences.
 *
 * @throws IllegalStateException if `UNTIL` is already set.
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumCoreRecurBuilder *)countValue:(int32_t)value __attribute__((swift_name("count(value:)")));

/**
 * Sets `INTERVAL`, the step in units of the frequency.
 *
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumCoreRecurBuilder *)intervalValue:(int32_t)value __attribute__((swift_name("interval(value:)")));

/** Returns the frequency of the rule being built. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Sets `UNTIL`, which is a date, a local date-time or a UTC date-time and must suit the type of `DTSTART`.
 *
 * @throws IllegalStateException if `COUNT` is already set.
 * @throws IllegalArgumentException if [value] is a zoned date-time or its year is outside 0000 to 9999.
 */
- (CalendrumCoreRecurBuilder *)untilValue:(id<CalendrumCoreICalDateTime>)value __attribute__((swift_name("until(value:)")));

/** Sets `WKST`, the day on which the week starts. */
- (CalendrumCoreRecurBuilder *)weekStartDay:(CalendrumCoreKotlinx_datetimeDayOfWeek *)day __attribute__((swift_name("weekStart(day:)")));
@end


/** Creates rule builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Recur.Companion")))
@interface CalendrumCoreRecurCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates rule builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreRecurCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for a rule that repeats with the given [frequency]. */
- (CalendrumCoreRecurBuilder *)builderFrequency:(CalendrumCoreFrequency *)frequency __attribute__((swift_name("builder(frequency:)")));
@end


/**
 * A rule part the library does not know, such as an `X-` part or a part registered after RFC 7529, kept so that the
 * rule is written back unchanged.
 *
 * @property name the part name as written, in upper case.
 * @property value the part value as written.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RecurPart")))
@interface CalendrumCoreRecurPart : CalendrumCoreBase
- (instancetype)initWithName:(NSString *)name value:(NSString *)value __attribute__((swift_name("init(name:value:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same name and value. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name and the value. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the name only, never the value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/**
 * The `SKIP` rule part of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529#section-4.1.1): what to do with an
 * occurrence that falls on a date that does not exist, such as the 31st in a month with 30 days.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Skip")))
@interface CalendrumCoreSkip : CalendrumCoreKotlinEnum<CalendrumCoreSkip *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `SKIP` rule part of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529#section-4.1.1): what to do with an
 * occurrence that falls on a date that does not exist, such as the 31st in a month with 30 days.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreSkip *omit __attribute__((swift_name("omit")));
@property (class, readonly) CalendrumCoreSkip *backward __attribute__((swift_name("backward")));
@property (class, readonly) CalendrumCoreSkip *forward __attribute__((swift_name("forward")));
+ (CalendrumCoreKotlinArray<CalendrumCoreSkip *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreSkip *> *entries __attribute__((swift_name("entries")));
@end


/**
 * An entry of `BYDAY`: a day of the week, optionally preceded by a number, as in `MO`, `2TU` or `-1FR`.
 *
 * The number selects the n-th such day in the month or year (`-1` is the last), and is allowed only with `MONTHLY` and
 * `YEARLY`.
 *
 * @property ordinal the number from 1 to 53 or from -53 to -1, or `null` for every such weekday.
 * @property dayOfWeek the day of the week.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("WeekdayNum")))
@interface CalendrumCoreWeekdayNum : CalendrumCoreBase
- (instancetype)initWithOrdinal:(CalendrumCoreInt * _Nullable)ordinal dayOfWeek:(CalendrumCoreKotlinx_datetimeDayOfWeek *)dayOfWeek __attribute__((swift_name("init(ordinal:dayOfWeek:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same number and day of week. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the number and the day of week. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the entry as written in a rule, such as `MO` or `-1FR`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) CalendrumCoreInt * _Nullable ordinal __attribute__((swift_name("ordinal")));
@end

@interface CalendrumCoreICal (Extensions)

/**
 * Creates a parser from options written in a DSL block.
 *
 * ```
 * val strict = ICal.parser {
 *     mode = ParseMode.Strict
 *     limits = ParseLimits.builder().maxLines(100_000).build()
 * }
 * ```
 *
 * @param configure sets the properties of [ParseOptions.Builder].
 */
- (CalendrumCoreICalParser *)parserConfigure:(void (^)(CalendrumCoreParseOptionsBuilder *))configure __attribute__((swift_name("parser(configure:)")));
@end

@interface CalendrumCorePropertyKey (Extensions)

/**
 * Returns a key that reads the same property as an `Instant` instead of an [ICalDateTime], placing it in time with
 * [zones].
 *
 * Use it with schemas and typed reads: `Properties.DTSTART.instant(zones)`. A value that cannot be placed reads as
 * malformed, with `ICAL-Z001` for an unknown `TZID` or `ICAL-O004` for a floating time or a date without a
 * [floatingZone]. Writing the key produces a UTC value.
 *
 * @param zones resolves the `TZID` of zoned values.
 * @param floatingZone the zone in which floating times and dates are read, or `null` for none.
 */
- (CalendrumCorePropertyKey<CalendrumCoreKotlinInstant *> *)instantZones:(id<CalendrumCoreTimeZoneResolver>)zones floatingZone:(CalendrumCoreKotlinx_datetimeTimeZone * _Nullable)floatingZone __attribute__((swift_name("instant(zones:floatingZone:)")));
@end

@interface CalendrumCoreVCalendar (Extensions)

/**
 * Returns a cursor over the occurrences of this calendar, to ask for the next one, the one after an instant, the ones
 * at an instant or a page. From Java this overload is `Occurrences.cursor`.
 *
 * @param options zones, floating zone, recurrence options, alarms and the problem listener.
 */
- (CalendrumCoreOccurrenceCursor *)occurrencesOptions:(CalendrumCoreOccurrenceOptions *)options __attribute__((swift_name("occurrences(options:)")));

/**
 * Returns the occurrences of this calendar's events, to-dos and journal entries that intersect the time range from
 * [from] up to, excluding, [to], in the order of their start.
 *
 * Recurrence, overrides (`RECURRENCE-ID`, including `RANGE=THISANDFUTURE`), cancelled instances, durations and alarms
 * are applied; the rules are those of [RFC 4791 §9.9](https://www.rfc-editor.org/rfc/rfc4791#section-9.9) for time
 * range queries. The sequence is lazy. Time zones come from [options]: by default the `VTIMEZONE` components of this
 * calendar and their aliases. Floating times and dates need `floatingZone` in the options, otherwise their occurrences
 * are skipped (`ICAL-O004`). The system time zone is never used. From Java this overload is `Occurrences.between`.
 *
 * @param from the start of the range.
 * @param to the end of the range, excluded.
 * @param options zones, floating zone, recurrence options, alarms and the problem listener.
 * @throws IllegalArgumentException if [from] is not before [to].
 * @throws kotlinx.datetime.IllegalTimeZoneException when evaluated, if a zone is unknown and the resolver's policy is
 *   `Fail`.
 */
- (id<CalendrumCoreKotlinSequence>)occurrencesFrom:(CalendrumCoreKotlinInstant *)from to:(CalendrumCoreKotlinInstant *)to options:(CalendrumCoreOccurrenceOptions *)options __attribute__((swift_name("occurrences(from:to:options:)")));
@end

@interface CalendrumCoreVEvent (Extensions)

/**
 * Returns the instant this event ends, from `DTEND` or else from `DTSTART` plus `DURATION`, a day for a date, or the
 * start itself; `null` when the event has no start or it cannot be placed without a zone.
 *
 * @param zones resolves the `TZID` values.
 * @param floatingZone the zone in which floating times and dates are read, or `null` for none.
 * @throws kotlinx.datetime.IllegalTimeZoneException if a zone is unknown and the resolver's policy is `Fail`.
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinInstant * _Nullable)resolvedEndZones:(id<CalendrumCoreTimeZoneResolver>)zones floatingZone:(CalendrumCoreKotlinx_datetimeTimeZone * _Nullable)floatingZone error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("resolvedEnd(zones:floatingZone:)"))) __attribute__((swift_error(nonnull_error)));

/**
 * Returns the instant this event starts, or `null` when it has no start or the start cannot be placed without a zone.
 *
 * @param zones resolves the `TZID` of the start.
 * @param floatingZone the zone in which a floating start or a date is read, or `null` for none.
 * @throws kotlinx.datetime.IllegalTimeZoneException if the zone is unknown and the resolver's policy is `Fail`.
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumCoreKotlinInstant * _Nullable)resolvedStartZones:(id<CalendrumCoreTimeZoneResolver>)zones floatingZone:(CalendrumCoreKotlinx_datetimeTimeZone * _Nullable)floatingZone error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("resolvedStart(zones:floatingZone:)"))) __attribute__((swift_error(nonnull_error)));

/**
 * Returns the version of the event for deciding which copy is newer: its `SEQUENCE` (0 when absent) and its
 * `LAST-MODIFIED`, or `DTSTAMP` when that is missing, if it is in UTC.
 */
- (CalendrumCoreRevision *)revision __attribute__((swift_name("revision()")));

/** Whether the event starts on a date and not at a time of day. */
@property (readonly) BOOL isAllDay __attribute__((swift_name("isAllDay")));

/** Whether the event has `STATUS:CANCELLED`. */
@property (readonly) BOOL isCancelled __attribute__((swift_name("isCancelled")));

/**
 * The recurrence set of this event: `DTSTART` with its `RRULE`, `EXRULE`, `RDATE` and `EXDATE`, or `null` when the
 * event has no usable `DTSTART`.
 */
@property (readonly) CalendrumCoreRecurrenceSet * _Nullable recurrenceSet __attribute__((swift_name("recurrenceSet")));
@end

@interface CalendrumCoreVJournal (Extensions)

/**
 * The recurrence set of this journal entry: `DTSTART` with its `RRULE`, `EXRULE`, `RDATE` and `EXDATE`, or `null` when
 * it has no usable `DTSTART`.
 */
@property (readonly) CalendrumCoreRecurrenceSet * _Nullable recurrenceSet __attribute__((swift_name("recurrenceSet")));
@end

@interface CalendrumCoreVTimeZoneObservance (Extensions)

/**
 * The recurrence set of this time zone observance (`STANDARD` or `DAYLIGHT`): its `DTSTART` with `RRULE` and `RDATE`,
 * or `null` without a usable `DTSTART`.
 */
@property (readonly) CalendrumCoreRecurrenceSet * _Nullable recurrenceSet __attribute__((swift_name("recurrenceSet")));
@end

@interface CalendrumCoreVTodo (Extensions)

/**
 * The recurrence set of this to-do: `DTSTART` with its `RRULE`, `EXRULE`, `RDATE` and `EXDATE`, or `null` when the
 * to-do has no usable `DTSTART`.
 */
@property (readonly) CalendrumCoreRecurrenceSet * _Nullable recurrenceSet __attribute__((swift_name("recurrenceSet")));
@end

@interface CalendrumCoreICalComponent (Extensions)

/**
 * Returns the values of every property of [key], in source order; properties that do not decode are skipped.
 *
 * @param key the typed key, such as `Properties.ATTENDEE`.
 */
- (NSArray<id> *)allKey:(CalendrumCorePropertyKey<id> *)key __attribute__((swift_name("all(key:)")));

/**
 * Decodes the first property of [key] and returns the outcome: the value with the problems that were repaired,
 * [Decoded.Missing], or [Decoded.Malformed] with the error.
 *
 * @param key the typed key, such as `Properties.DTSTART`.
 */
- (id<CalendrumCoreDecoded>)decodeKey:(CalendrumCorePropertyKey<id> *)key __attribute__((swift_name("decode(key:)")));

/**
 * Decodes the first property named [name] into the type the default registry assigns to it; the text for an unknown
 * property.
 *
 * @param name the property name.
 */
- (id<CalendrumCoreDecoded>)decodeAnyName:(NSString *)name __attribute__((swift_name("decodeAny(name:)")));

/**
 * Decodes the first property named [name] into the type that [registry] assigns to it; the text for a property it does
 * not know.
 *
 * @param name the property name.
 * @param registry what every property is.
 */
- (id<CalendrumCoreDecoded>)decodeAnyName:(NSString *)name registry:(CalendrumCoreICalRegistry *)registry __attribute__((swift_name("decodeAny(name:registry:)")));

/**
 * Returns the value of the first property of [key], or `null` when the property is missing or does not decode.
 *
 * Decoding happens on first access and is cached in the property. Use [decode] to learn why a value is `null`.
 *
 * @param key the typed key, such as `Properties.DTSTART`.
 */
- (id _Nullable)getKey:(CalendrumCorePropertyKey<id> *)key __attribute__((swift_name("get(key:)")));
@end

@interface CalendrumCoreICalProperty (Extensions)

/**
 * Returns the value of the parameter of [key], or `null` when the parameter is missing or does not decode.
 *
 * @param key the typed key, such as `Parameters.PARTSTAT`.
 */
- (id _Nullable)getKey:(CalendrumCoreParameterKey<id> *)key __attribute__((swift_name("get(key:)")));
@end

@interface CalendrumCoreParseResult (Extensions)

/**
 * The `VCALENDAR` components of this result as typed [VCalendar] views, in input order; computed once and cached in the
 * result.
 *
 * This is the entry to the typed model: `ICal.parse(bytes).calendars.single()`.
 */
@property (readonly) NSArray<CalendrumCoreVCalendar *> *calendars __attribute__((swift_name("calendars")));
@end

@interface CalendrumCoreRecur (Extensions)

/**
 * Expands this rule lazily into the dates it produces, starting at [start], in local time.
 *
 * The sequence is lazy and can be endless (a rule without `COUNT` or `UNTIL`); take what you need with `take` or
 * `takeWhile`. Each call to `iterator()` starts over. The dates have the form of [start]: dates stay dates, UTC stays
 * UTC and a zoned [start] gives zoned dates with the same `TZID`. No time zone rules are applied: the dates are
 * wall-clock times, and a time that a daylight saving gap skips is a matter for the occurrence layer. Expansion never
 * throws: the limits of [options] end the sequence, and problems (`ICAL-R001` to `ICAL-R009`) go to its problem
 * listener, each at most once per expansion.
 *
 * @param start the `DTSTART` of the series, which is also the first date when the rule produces it.
 * @param options limits, the handling of a `DTSTART` that the rule does not produce, and the problem listener.
 */
- (id<CalendrumCoreKotlinSequence>)expandStart:(id<CalendrumCoreICalDateTime>)start options:(CalendrumCoreRecurrenceOptions *)options __attribute__((swift_name("expand(start:options:)")));

/**
 * Expands this rule lazily like the shorter form, but only the dates in the range from [from] up to, excluding, [to].
 *
 * The skipped dates before [from] still count for `COUNT`. A `null` bound leaves that side open.
 *
 * @param start the `DTSTART` of the series.
 * @param from the first date to produce, or `null` for no lower bound.
 * @param to the end of the range, excluded, or `null` for no upper bound.
 * @param options limits, the handling of a `DTSTART` that the rule does not produce, and the problem listener.
 */
- (id<CalendrumCoreKotlinSequence>)expandStart:(id<CalendrumCoreICalDateTime>)start from:(id<CalendrumCoreICalDateTime> _Nullable)from to:(id<CalendrumCoreICalDateTime> _Nullable)to options:(CalendrumCoreRecurrenceOptions *)options __attribute__((swift_name("expand(start:from:to:options:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface CalendrumCoreKotlinByteArray : CalendrumCoreBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumCoreByte *(^)(CalendrumCoreInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumCoreKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

@interface CalendrumCoreKotlinByteArray (Extensions)

/**
 * Wraps this array as a [ByteInput] that yields its bytes once, from the start.
 *
 * The array is not copied; do not change it while the input is being read.
 */
- (id<CalendrumCoreByteInput>)asByteInput __attribute__((swift_name("asByteInput()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalComponentDslKt")))
@interface CalendrumCoreICalComponentDslKt : CalendrumCoreBase

/**
 * Builds a component with a Kotlin DSL.
 *
 * ```
 * val event = ICalComponent("VEVENT") {
 *     property("UID", "42@example.com")
 *     component("VALARM") { property("ACTION", "DISPLAY") }
 * }
 * ```
 *
 * @param name the component name, letters, digits and hyphens.
 * @param block adds properties and nested components.
 * @throws IllegalArgumentException if [name] or a name inside [block] is invalid.
 */
+ (CalendrumCoreICalComponent *)ICalComponentName:(NSString *)name block:(void (^)(CalendrumCoreICalComponentBuilder *))block __attribute__((swift_name("ICalComponent(name:block:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LastMileKt")))
@interface CalendrumCoreLastMileKt : CalendrumCoreBase

/**
 * Returns the attendees that are people, as opposed to groups, resources and rooms (`CUTYPE=INDIVIDUAL`, the default).
 */
+ (NSArray<CalendrumCoreAttendee *> *)individuals:(NSArray<CalendrumCoreAttendee *> *)receiver __attribute__((swift_name("individuals(_:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeZoneResolversKt")))
@interface CalendrumCoreTimeZoneResolversKt : CalendrumCoreBase

/**
 * Returns the instant this value means, or `null` when it cannot be known without a time zone.
 *
 * A UTC value is its own instant. A zoned value is resolved by [zones]; if that gives no zone, [floatingZone] is used.
 * A floating value and a date are read in [floatingZone]. With no [floatingZone] they give `null`: the library never
 * assumes the system zone. The result keeps the nanoseconds of the value.
 *
 * @param zones resolves the `TZID` of a zoned value.
 * @param floatingZone the zone in which floating times and dates are read, or `null` for none.
 * @throws kotlinx.datetime.IllegalTimeZoneException if `TZID` is unknown and the resolver's policy is
 *   [UnknownZonePolicy.Fail].
 *
 * @note This method converts instances of IllegalTimeZoneException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
+ (CalendrumCoreKotlinInstant * _Nullable)toInstant:(id<CalendrumCoreICalDateTime>)receiver zones:(id<CalendrumCoreTimeZoneResolver>)zones floatingZone:(CalendrumCoreKotlinx_datetimeTimeZone * _Nullable)floatingZone error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("toInstant(_:zones:floatingZone:)"))) __attribute__((swift_error(nonnull_error)));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VCalendarKt")))
@interface CalendrumCoreVCalendarKt : CalendrumCoreBase

/**
 * Builds a calendar with the given `PRODID` and `VERSION:2.0` in a Kotlin DSL; the builder in [block] adds events,
 * to-dos and the rest.
 *
 * `UID` and `DTSTAMP` of every component must be set in [block], since this overload has no defaults.
 *
 * @param prodId the product identifier, such as `-//Example//Planner 1.0//EN`.
 * @param block adds the properties and components.
 * @throws IllegalStateException when a component is built without `UID` or `DTSTAMP`.
 * @sample io.github.calendrum.samples.buildCalendar
 */
+ (CalendrumCoreVCalendar *)VCalendarProdId:(NSString *)prodId block:(void (^)(CalendrumCoreVCalendarBuilder *))block __attribute__((swift_name("VCalendar(prodId:block:)")));

/**
 * Builds a calendar like the shorter overload, but takes the `UID` and `DTSTAMP` of components that lack them from
 * [defaults].
 *
 * There is no global clock or `UID` generator: they are always passed in.
 *
 * @param prodId the product identifier, such as `-//Example//Planner 1.0//EN`.
 * @param defaults the clock and the generator of `UID` values.
 * @param block adds the properties and components.
 * @sample io.github.calendrum.samples.buildCalendar
 */
+ (CalendrumCoreVCalendar *)VCalendarProdId:(NSString *)prodId defaults:(CalendrumCoreBuildDefaults *)defaults block:(void (^)(CalendrumCoreVCalendarBuilder *))block __attribute__((swift_name("VCalendar(prodId:defaults:block:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface CalendrumCoreKotlinArray<T> : CalendrumCoreBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(CalendrumCoreInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<CalendrumCoreKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface CalendrumCoreKotlinEnumCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="2.3")
*/
__attribute__((swift_name("KotlinClock")))
@protocol CalendrumCoreKotlinClock
@required
- (CalendrumCoreKotlinInstant *)now __attribute__((swift_name("now()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/UtcOffsetSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset")))
@interface CalendrumCoreKotlinx_datetimeUtcOffset : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeUtcOffsetCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t totalSeconds __attribute__((swift_name("totalSeconds")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="2.3")
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant")))
@interface CalendrumCoreKotlinInstant : CalendrumCoreBase <CalendrumCoreKotlinComparable>
@property (class, readonly, getter=companion) CalendrumCoreKotlinInstantCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumCoreKotlinInstant *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumCoreKotlinInstant *)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (int64_t)minusOther:(CalendrumCoreKotlinInstant *)other __attribute__((swift_name("minus(other:)")));
- (CalendrumCoreKotlinInstant *)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
- (int64_t)toEpochMilliseconds __attribute__((swift_name("toEpochMilliseconds()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t epochSeconds __attribute__((swift_name("epochSeconds")));
@property (readonly) int32_t nanosecondsOfSecond __attribute__((swift_name("nanosecondsOfSecond")));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface CalendrumCoreKotlinRuntimeException : CalendrumCoreKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalArgumentException")))
@interface CalendrumCoreKotlinIllegalArgumentException : CalendrumCoreKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumCoreKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeIllegalTimeZoneException")))
@interface CalendrumCoreKotlinx_datetimeIllegalTimeZoneException : CalendrumCoreKotlinIllegalArgumentException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString *)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumCoreKotlinThrowable *)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString *)message cause:(CalendrumCoreKotlinThrowable *)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/TimeZoneSerializer))
*/
__attribute__((swift_name("Kotlinx_datetimeTimeZone")))
@interface CalendrumCoreKotlinx_datetimeTimeZone : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeTimeZoneCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumCoreKotlinInstant *)toInstant:(CalendrumCoreKotlinx_datetimeLocalDateTime *)receiver youShallNotPass:(CalendrumCoreKotlinx_datetimeOverloadMarker *)youShallNotPass __attribute__((swift_name("toInstant(_:youShallNotPass:)")));
- (CalendrumCoreKotlinx_datetimeLocalDateTime *)toLocalDateTime:(CalendrumCoreKotlinInstant *)receiver __attribute__((swift_name("toLocalDateTime(_:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/LocalDateTimeSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateTime")))
@interface CalendrumCoreKotlinx_datetimeLocalDateTime : CalendrumCoreBase <CalendrumCoreKotlinComparable>
- (instancetype)initWithDate:(CalendrumCoreKotlinx_datetimeLocalDate *)date time:(CalendrumCoreKotlinx_datetimeLocalTime *)time __attribute__((swift_name("init(date:time:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithYear:(int32_t)year month:(int32_t)month day:(int32_t)day hour:(int32_t)hour minute:(int32_t)minute second:(int32_t)second nanosecond:(int32_t)nanosecond __attribute__((swift_name("init(year:month:day:hour:minute:second:nanosecond:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithYear:(int32_t)year month:(CalendrumCoreKotlinx_datetimeMonth *)month day:(int32_t)day hour:(int32_t)hour minute:(int32_t)minute second:(int32_t)second nanosecond_:(int32_t)nanosecond __attribute__((swift_name("init(year:month:day:hour:minute:second:nanosecond_:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeLocalDateTimeCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumCoreKotlinx_datetimeLocalDateTime *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDate *date __attribute__((swift_name("date")));
@property (readonly) int32_t day __attribute__((swift_name("day")));
@property (readonly) int32_t dayOfMonth __attribute__((swift_name("dayOfMonth"))) __attribute__((deprecated("Use the 'day' property instead")));
@property (readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) int32_t dayOfYear __attribute__((swift_name("dayOfYear")));
@property (readonly) int32_t hour __attribute__((swift_name("hour")));
@property (readonly) int32_t minute __attribute__((swift_name("minute")));
@property (readonly) CalendrumCoreKotlinx_datetimeMonth *month __attribute__((swift_name("month")));
@property (readonly) int32_t monthNumber __attribute__((swift_name("monthNumber"))) __attribute__((deprecated("Use the 'month' property instead")));
@property (readonly) int32_t nanosecond __attribute__((swift_name("nanosecond")));
@property (readonly) int32_t second __attribute__((swift_name("second")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalTime *time __attribute__((swift_name("time")));
@property (readonly) int32_t year __attribute__((swift_name("year")));
@end

__attribute__((swift_name("KotlinSequence")))
@protocol CalendrumCoreKotlinSequence
@required
- (id<CalendrumCoreKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
@end

__attribute__((swift_name("KotlinAppendable")))
@protocol CalendrumCoreKotlinAppendable
@required

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumCoreKotlinAppendable>)appendValue:(unichar)value __attribute__((swift_name("append(value:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumCoreKotlinAppendable>)appendValue_:(id _Nullable)value __attribute__((swift_name("append(value_:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumCoreKotlinAppendable>)appendValue:(id _Nullable)value startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("append(value:startIndex:endIndex:)")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeSource")))
@protocol CalendrumCoreKotlinTimeSource
@required
- (id<CalendrumCoreKotlinTimeMark>)markNow __attribute__((swift_name("markNow()")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeSourceWithComparableMarks")))
@protocol CalendrumCoreKotlinTimeSourceWithComparableMarks <CalendrumCoreKotlinTimeSource>
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/LocalDateSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDate")))
@interface CalendrumCoreKotlinx_datetimeLocalDate : CalendrumCoreBase <CalendrumCoreKotlinComparable>
- (instancetype)initWithYear:(int32_t)year month:(int32_t)month day:(int32_t)day __attribute__((swift_name("init(year:month:day:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithYear:(int32_t)year month:(CalendrumCoreKotlinx_datetimeMonth *)month day_:(int32_t)day __attribute__((swift_name("init(year:month:day_:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeLocalDateCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumCoreKotlinx_datetimeLocalDate *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumCoreKotlinx_datetimeLocalDateRange *)rangeToThat:(CalendrumCoreKotlinx_datetimeLocalDate *)that __attribute__((swift_name("rangeTo(that:)")));
- (CalendrumCoreKotlinx_datetimeLocalDateRange *)rangeUntilThat:(CalendrumCoreKotlinx_datetimeLocalDate *)that __attribute__((swift_name("rangeUntil(that:)")));
- (int64_t)toEpochDays __attribute__((swift_name("toEpochDays()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t day __attribute__((swift_name("day")));
@property (readonly) int32_t dayOfMonth __attribute__((swift_name("dayOfMonth"))) __attribute__((deprecated("Use the 'day' property instead")));
@property (readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) int32_t dayOfYear __attribute__((swift_name("dayOfYear")));
@property (readonly) CalendrumCoreKotlinx_datetimeMonth *month __attribute__((swift_name("month")));
@property (readonly) int32_t monthNumber __attribute__((swift_name("monthNumber"))) __attribute__((deprecated("Use the 'month' property instead")));
@property (readonly) int32_t year __attribute__((swift_name("year")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/DateTimePeriodSerializer))
*/
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod")))
@interface CalendrumCoreKotlinx_datetimeDateTimePeriod : CalendrumCoreBase
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeDateTimePeriodCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t days __attribute__((swift_name("days")));
@property (readonly) int32_t hours __attribute__((swift_name("hours")));
@property (readonly) int32_t minutes __attribute__((swift_name("minutes")));
@property (readonly) int32_t months __attribute__((swift_name("months")));
@property (readonly) int32_t nanoseconds __attribute__((swift_name("nanoseconds")));
@property (readonly) int32_t seconds __attribute__((swift_name("seconds")));
@property (readonly) int32_t years __attribute__((swift_name("years")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeek")))
@interface CalendrumCoreKotlinx_datetimeDayOfWeek : CalendrumCoreKotlinEnum<CalendrumCoreKotlinx_datetimeDayOfWeek *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *monday __attribute__((swift_name("monday")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *tuesday __attribute__((swift_name("tuesday")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *wednesday __attribute__((swift_name("wednesday")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *thursday __attribute__((swift_name("thursday")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *friday __attribute__((swift_name("friday")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *saturday __attribute__((swift_name("saturday")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeDayOfWeek *sunday __attribute__((swift_name("sunday")));
+ (CalendrumCoreKotlinArray<CalendrumCoreKotlinx_datetimeDayOfWeek *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreKotlinx_datetimeDayOfWeek *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntArray")))
@interface CalendrumCoreKotlinIntArray : CalendrumCoreBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumCoreInt *(^)(CalendrumCoreInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int32_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumCoreKotlinIntIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int32_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinByteIterator")))
@interface CalendrumCoreKotlinByteIterator : CalendrumCoreBase <CalendrumCoreKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumCoreByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset.Companion")))
@interface CalendrumCoreKotlinx_datetimeUtcOffsetCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeUtcOffsetCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)FormatBlock:(void (^)(id<CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset>))block __attribute__((swift_name("Format(block:)")));
- (CalendrumCoreKotlinx_datetimeUtcOffset *)parseInput:(id)input format:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) CalendrumCoreKotlinx_datetimeUtcOffset *ZERO __attribute__((swift_name("ZERO")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant.Companion")))
@interface CalendrumCoreKotlinInstantCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinInstantCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumCoreKotlinInstant *)fromEpochMillisecondsEpochMilliseconds:(int64_t)epochMilliseconds __attribute__((swift_name("fromEpochMilliseconds(epochMilliseconds:)")));
- (CalendrumCoreKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment:(int32_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment:)")));
- (CalendrumCoreKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment_:(int64_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment_:)")));
- (CalendrumCoreKotlinInstant *)now __attribute__((swift_name("now()"))) __attribute__((unavailable("Use Clock.System.now() instead")));
- (CalendrumCoreKotlinInstant *)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (CalendrumCoreKotlinInstant * _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@property (readonly) CalendrumCoreKotlinInstant *DISTANT_FUTURE __attribute__((swift_name("DISTANT_FUTURE")));
@property (readonly) CalendrumCoreKotlinInstant *DISTANT_PAST __attribute__((swift_name("DISTANT_PAST")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeTimeZone.Companion")))
@interface CalendrumCoreKotlinx_datetimeTimeZoneCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeTimeZoneCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumCoreKotlinx_datetimeTimeZone *)currentSystemDefault __attribute__((swift_name("currentSystemDefault()")));
- (CalendrumCoreKotlinx_datetimeTimeZone *)ofZoneId:(NSString *)zoneId __attribute__((swift_name("of(zoneId:)")));
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) CalendrumCoreKotlinx_datetimeFixedOffsetTimeZone *UTC __attribute__((swift_name("UTC")));
@property (readonly) NSSet<NSString *> *availableZoneIds __attribute__((swift_name("availableZoneIds")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeOverloadMarker")))
@interface CalendrumCoreKotlinx_datetimeOverloadMarker : CalendrumCoreBase
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/LocalTimeSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalTime")))
@interface CalendrumCoreKotlinx_datetimeLocalTime : CalendrumCoreBase <CalendrumCoreKotlinComparable>
- (instancetype)initWithHour:(int32_t)hour minute:(int32_t)minute second:(int32_t)second nanosecond:(int32_t)nanosecond __attribute__((swift_name("init(hour:minute:second:nanosecond:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeLocalTimeCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumCoreKotlinx_datetimeLocalTime *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (int32_t)toMillisecondOfDay __attribute__((swift_name("toMillisecondOfDay()")));
- (int64_t)toNanosecondOfDay __attribute__((swift_name("toNanosecondOfDay()")));
- (int32_t)toSecondOfDay __attribute__((swift_name("toSecondOfDay()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t hour __attribute__((swift_name("hour")));
@property (readonly) int32_t minute __attribute__((swift_name("minute")));
@property (readonly) int32_t nanosecond __attribute__((swift_name("nanosecond")));
@property (readonly) int32_t second __attribute__((swift_name("second")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonth")))
@interface CalendrumCoreKotlinx_datetimeMonth : CalendrumCoreKotlinEnum<CalendrumCoreKotlinx_datetimeMonth *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *january __attribute__((swift_name("january")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *february __attribute__((swift_name("february")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *march __attribute__((swift_name("march")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *april __attribute__((swift_name("april")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *may __attribute__((swift_name("may")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *june __attribute__((swift_name("june")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *july __attribute__((swift_name("july")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *august __attribute__((swift_name("august")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *september __attribute__((swift_name("september")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *october __attribute__((swift_name("october")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *november __attribute__((swift_name("november")));
@property (class, readonly) CalendrumCoreKotlinx_datetimeMonth *december __attribute__((swift_name("december")));
+ (CalendrumCoreKotlinArray<CalendrumCoreKotlinx_datetimeMonth *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreKotlinx_datetimeMonth *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateTime.Companion")))
@interface CalendrumCoreKotlinx_datetimeLocalDateTimeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeLocalDateTimeCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)FormatBuilder:(void (^)(id<CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithDateTime>))builder __attribute__((swift_name("Format(builder:)")));
- (CalendrumCoreKotlinx_datetimeLocalDateTime *)parseInput:(id)input format:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeMark")))
@protocol CalendrumCoreKotlinTimeMark
@required
- (int64_t)elapsedNow __attribute__((swift_name("elapsedNow()")));
- (BOOL)hasNotPassedNow __attribute__((swift_name("hasNotPassedNow()")));
- (BOOL)hasPassedNow __attribute__((swift_name("hasPassedNow()")));
- (id<CalendrumCoreKotlinTimeMark>)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (id<CalendrumCoreKotlinTimeMark>)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDate.Companion")))
@interface CalendrumCoreKotlinx_datetimeLocalDateCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeLocalDateCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)FormatBlock:(void (^)(id<CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithDate>))block __attribute__((swift_name("Format(block:)")));
- (CalendrumCoreKotlinx_datetimeLocalDate *)fromEpochDaysEpochDays:(int32_t)epochDays __attribute__((swift_name("fromEpochDays(epochDays:)")));
- (CalendrumCoreKotlinx_datetimeLocalDate *)fromEpochDaysEpochDays_:(int64_t)epochDays __attribute__((swift_name("fromEpochDays(epochDays_:)")));
- (CalendrumCoreKotlinx_datetimeLocalDate *)parseInput:(id)input format:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("KotlinIterable")))
@protocol CalendrumCoreKotlinIterable
@required
- (id<CalendrumCoreKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
@end

__attribute__((swift_name("KotlinCollection")))
@protocol CalendrumCoreKotlinCollection <CalendrumCoreKotlinIterable>
@required
- (BOOL)containsElement:(id _Nullable)element __attribute__((swift_name("contains(element:)")));
- (BOOL)containsAllElements:(id)elements __attribute__((swift_name("containsAll(elements:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("Kotlinx_datetimeLocalDateProgression")))
@interface CalendrumCoreKotlinx_datetimeLocalDateProgression : CalendrumCoreBase <CalendrumCoreKotlinCollection>
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeLocalDateProgressionCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)containsElement:(CalendrumCoreKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(element:)")));
- (BOOL)containsAllElements:(id)elements __attribute__((swift_name("containsAll(elements:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (id<CalendrumCoreKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDate *first __attribute__((swift_name("first")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDate *last __attribute__((swift_name("last")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinClosedRange")))
@protocol CalendrumCoreKotlinClosedRange
@required
- (BOOL)containsValue:(id)value __attribute__((swift_name("contains(value:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
@property (readonly) id endInclusive __attribute__((swift_name("endInclusive")));
@property (readonly) id start __attribute__((swift_name("start")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinOpenEndRange")))
@protocol CalendrumCoreKotlinOpenEndRange
@required
- (BOOL)containsValue_:(id)value __attribute__((swift_name("contains(value_:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
@property (readonly) id endExclusive __attribute__((swift_name("endExclusive")));
@property (readonly) id start __attribute__((swift_name("start")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateRange")))
@interface CalendrumCoreKotlinx_datetimeLocalDateRange : CalendrumCoreKotlinx_datetimeLocalDateProgression <CalendrumCoreKotlinClosedRange, CalendrumCoreKotlinOpenEndRange>
- (instancetype)initWithStart:(CalendrumCoreKotlinx_datetimeLocalDate *)start endInclusive:(CalendrumCoreKotlinx_datetimeLocalDate *)endInclusive __attribute__((swift_name("init(start:endInclusive:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeLocalDateRangeCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)containsElement:(CalendrumCoreKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(element:)")));
- (BOOL)containsValue:(CalendrumCoreKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(value:)")));
- (BOOL)containsValue_:(CalendrumCoreKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(value_:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDate *endExclusive __attribute__((swift_name("endExclusive"))) __attribute__((deprecated("This throws an exception if the exclusive end if not inside the platform-specific boundaries for LocalDate. The 'endInclusive' property does not throw and should be preferred.")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDate *endInclusive __attribute__((swift_name("endInclusive")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDate *start __attribute__((swift_name("start")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod.Companion")))
@interface CalendrumCoreKotlinx_datetimeDateTimePeriodCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeDateTimePeriodCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumCoreKotlinx_datetimeDateTimePeriod *)parseText:(NSString *)text __attribute__((swift_name("parse(text:)")));
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("KotlinIntIterator")))
@interface CalendrumCoreKotlinIntIterator : CalendrumCoreBase <CalendrumCoreKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumCoreInt *)next __attribute__((swift_name("next()")));
- (int32_t)nextInt __attribute__((swift_name("nextInt()")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormat")))
@protocol CalendrumCoreKotlinx_datetimeDateTimeFormat
@required
- (NSString *)formatValue:(id _Nullable)value __attribute__((swift_name("format(value:)")));
- (id<CalendrumCoreKotlinAppendable>)formatToAppendable:(id<CalendrumCoreKotlinAppendable>)appendable value:(id _Nullable)value __attribute__((swift_name("formatTo(appendable:value:)")));
- (id _Nullable)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (id _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilder")))
@protocol CalendrumCoreKotlinx_datetimeDateTimeFormatBuilder
@required
- (void)charsValue:(NSString *)value __attribute__((swift_name("chars(value:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithUtcOffset")))
@protocol CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset <CalendrumCoreKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)offsetFormat:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("offset(format:)")));
- (void)offsetHoursPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetHours(padding:)")));
- (void)offsetMinutesOfHourPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetMinutesOfHour(padding:)")));
- (void)offsetSecondsOfMinutePadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetSecondsOfMinute(padding:)")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol CalendrumCoreKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<CalendrumCoreKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol CalendrumCoreKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<CalendrumCoreKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol CalendrumCoreKotlinx_serialization_coreKSerializer <CalendrumCoreKotlinx_serialization_coreSerializationStrategy, CalendrumCoreKotlinx_serialization_coreDeserializationStrategy>
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/FixedOffsetTimeZoneSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeFixedOffsetTimeZone")))
@interface CalendrumCoreKotlinx_datetimeFixedOffsetTimeZone : CalendrumCoreKotlinx_datetimeTimeZone
- (instancetype)initWithOffset:(CalendrumCoreKotlinx_datetimeUtcOffset *)offset __attribute__((swift_name("init(offset:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeFixedOffsetTimeZoneCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) CalendrumCoreKotlinx_datetimeUtcOffset *offset __attribute__((swift_name("offset")));
@property (readonly) int32_t totalSeconds __attribute__((swift_name("totalSeconds"))) __attribute__((deprecated("Use offset.totalSeconds")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalTime.Companion")))
@interface CalendrumCoreKotlinx_datetimeLocalTimeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeLocalTimeCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)FormatBuilder:(void (^)(id<CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithTime>))builder __attribute__((swift_name("Format(builder:)")));
- (CalendrumCoreKotlinx_datetimeLocalTime *)fromMillisecondOfDayMillisecondOfDay:(int32_t)millisecondOfDay __attribute__((swift_name("fromMillisecondOfDay(millisecondOfDay:)")));
- (CalendrumCoreKotlinx_datetimeLocalTime *)fromNanosecondOfDayNanosecondOfDay:(int64_t)nanosecondOfDay __attribute__((swift_name("fromNanosecondOfDay(nanosecondOfDay:)")));
- (CalendrumCoreKotlinx_datetimeLocalTime *)fromSecondOfDaySecondOfDay:(int32_t)secondOfDay __attribute__((swift_name("fromSecondOfDay(secondOfDay:)")));
- (CalendrumCoreKotlinx_datetimeLocalTime *)parseInput:(id)input format:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithYearMonth")))
@protocol CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithYearMonth <CalendrumCoreKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)monthNameNames:(CalendrumCoreKotlinx_datetimeMonthNames *)names __attribute__((swift_name("monthName(names:)")));
- (void)monthNumberPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("monthNumber(padding:)")));
- (void)yearPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("year(padding:)")));
- (void)yearMonthFormat:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("yearMonth(format:)")));
- (void)yearTwoDigitsBaseYear:(int32_t)baseYear __attribute__((swift_name("yearTwoDigits(baseYear:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithDate")))
@protocol CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithDate <CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithYearMonth>
@required
- (void)dateFormat:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("date(format:)")));
- (void)dayPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("day(padding:)")));
- (void)dayOfMonthPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("dayOfMonth(padding:)"))) __attribute__((deprecated("Use 'day' instead")));
- (void)dayOfWeekNames:(CalendrumCoreKotlinx_datetimeDayOfWeekNames *)names __attribute__((swift_name("dayOfWeek(names:)")));
- (void)dayOfYearPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("dayOfYear(padding:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithTime")))
@protocol CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithTime <CalendrumCoreKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)amPmHourPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("amPmHour(padding:)")));
- (void)amPmMarkerAm:(NSString *)am pm:(NSString *)pm __attribute__((swift_name("amPmMarker(am:pm:)")));
- (void)hourPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("hour(padding:)")));
- (void)minutePadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("minute(padding:)")));
- (void)secondPadding:(CalendrumCoreKotlinx_datetimePadding *)padding __attribute__((swift_name("second(padding:)")));
- (void)secondFractionFixedLength:(int32_t)fixedLength __attribute__((swift_name("secondFraction(fixedLength:)")));
- (void)secondFractionMinLength:(int32_t)minLength maxLength:(int32_t)maxLength __attribute__((swift_name("secondFraction(minLength:maxLength:)")));
- (void)timeFormat:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("time(format:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithDateTime")))
@protocol CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithDateTime <CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithDate, CalendrumCoreKotlinx_datetimeDateTimeFormatBuilderWithTime>
@required
- (void)dateTimeFormat:(id<CalendrumCoreKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("dateTime(format:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateProgression.Companion")))
@interface CalendrumCoreKotlinx_datetimeLocalDateProgressionCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeLocalDateProgressionCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateRange.Companion")))
@interface CalendrumCoreKotlinx_datetimeLocalDateRangeCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeLocalDateRangeCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumCoreKotlinx_datetimeLocalDateRange *EMPTY __attribute__((swift_name("EMPTY")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimePadding")))
@interface CalendrumCoreKotlinx_datetimePadding : CalendrumCoreKotlinEnum<CalendrumCoreKotlinx_datetimePadding *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumCoreKotlinx_datetimePadding *none __attribute__((swift_name("none")));
@property (class, readonly) CalendrumCoreKotlinx_datetimePadding *zero __attribute__((swift_name("zero")));
@property (class, readonly) CalendrumCoreKotlinx_datetimePadding *space __attribute__((swift_name("space")));
+ (CalendrumCoreKotlinArray<CalendrumCoreKotlinx_datetimePadding *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumCoreKotlinx_datetimePadding *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol CalendrumCoreKotlinx_serialization_coreEncoder
@required
- (id<CalendrumCoreKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<CalendrumCoreKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<CalendrumCoreKotlinx_serialization_coreEncoder>)encodeInlineDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("encodeInline(descriptor:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNull __attribute__((swift_name("encodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableValueSerializer:(id<CalendrumCoreKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<CalendrumCoreKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) CalendrumCoreKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol CalendrumCoreKotlinx_serialization_coreSerialDescriptor
@required

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSArray<id<CalendrumCoreKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSArray<id<CalendrumCoreKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isInline __attribute__((swift_name("isInline")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) CalendrumCoreKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol CalendrumCoreKotlinx_serialization_coreDecoder
@required
- (id<CalendrumCoreKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<CalendrumCoreKotlinx_serialization_coreDecoder>)decodeInlineDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeInline(descriptor:)")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (CalendrumCoreKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<CalendrumCoreKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<CalendrumCoreKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) CalendrumCoreKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeFixedOffsetTimeZone.Companion")))
@interface CalendrumCoreKotlinx_datetimeFixedOffsetTimeZoneCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeFixedOffsetTimeZoneCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeekNames")))
@interface CalendrumCoreKotlinx_datetimeDayOfWeekNames : CalendrumCoreBase
- (instancetype)initWithNames:(NSArray<NSString *> *)names __attribute__((swift_name("init(names:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMonday:(NSString *)monday tuesday:(NSString *)tuesday wednesday:(NSString *)wednesday thursday:(NSString *)thursday friday:(NSString *)friday saturday:(NSString *)saturday sunday:(NSString *)sunday __attribute__((swift_name("init(monday:tuesday:wednesday:thursday:friday:saturday:sunday:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeDayOfWeekNamesCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonthNames")))
@interface CalendrumCoreKotlinx_datetimeMonthNames : CalendrumCoreBase
- (instancetype)initWithNames:(NSArray<NSString *> *)names __attribute__((swift_name("init(names:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithJanuary:(NSString *)january february:(NSString *)february march:(NSString *)march april:(NSString *)april may:(NSString *)may june:(NSString *)june july:(NSString *)july august:(NSString *)august september:(NSString *)september october:(NSString *)october november:(NSString *)november december:(NSString *)december __attribute__((swift_name("init(january:february:march:april:may:june:july:august:september:october:november:december:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumCoreKotlinx_datetimeMonthNamesCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol CalendrumCoreKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<CalendrumCoreKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumCoreKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumCoreKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) CalendrumCoreKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface CalendrumCoreKotlinx_serialization_coreSerializersModule : CalendrumCoreBase

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)dumpToCollector:(id<CalendrumCoreKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumCoreKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<CalendrumCoreKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<CalendrumCoreKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumCoreKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumCoreKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumCoreKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumCoreKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end

__attribute__((swift_name("KotlinAnnotation")))
@protocol CalendrumCoreKotlinAnnotation
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface CalendrumCoreKotlinx_serialization_coreSerialKind : CalendrumCoreBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol CalendrumCoreKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<CalendrumCoreKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumCoreKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumCoreKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<CalendrumCoreKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) CalendrumCoreKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface CalendrumCoreKotlinNothing : CalendrumCoreBase
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeekNames.Companion")))
@interface CalendrumCoreKotlinx_datetimeDayOfWeekNamesCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeDayOfWeekNamesCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumCoreKotlinx_datetimeDayOfWeekNames *ENGLISH_ABBREVIATED __attribute__((swift_name("ENGLISH_ABBREVIATED")));
@property (readonly) CalendrumCoreKotlinx_datetimeDayOfWeekNames *ENGLISH_FULL __attribute__((swift_name("ENGLISH_FULL")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonthNames.Companion")))
@interface CalendrumCoreKotlinx_datetimeMonthNamesCompanion : CalendrumCoreBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumCoreKotlinx_datetimeMonthNamesCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumCoreKotlinx_datetimeMonthNames *ENGLISH_ABBREVIATED __attribute__((swift_name("ENGLISH_ABBREVIATED")));
@property (readonly) CalendrumCoreKotlinx_datetimeMonthNames *ENGLISH_FULL __attribute__((swift_name("ENGLISH_FULL")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol CalendrumCoreKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<CalendrumCoreKotlinKClass>)kClass provider:(id<CalendrumCoreKotlinx_serialization_coreKSerializer> (^)(NSArray<id<CalendrumCoreKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<CalendrumCoreKotlinKClass>)kClass serializer:(id<CalendrumCoreKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<CalendrumCoreKotlinKClass>)baseClass actualClass:(id<CalendrumCoreKotlinKClass>)actualClass actualSerializer:(id<CalendrumCoreKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<CalendrumCoreKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumCoreKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultDeserializerProvider:)"))) __attribute__((deprecated("Deprecated in favor of function with more precise name: polymorphicDefaultDeserializer")));
- (void)polymorphicDefaultDeserializerBaseClass:(id<CalendrumCoreKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumCoreKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefaultDeserializer(baseClass:defaultDeserializerProvider:)")));
- (void)polymorphicDefaultSerializerBaseClass:(id<CalendrumCoreKotlinKClass>)baseClass defaultSerializerProvider:(id<CalendrumCoreKotlinx_serialization_coreSerializationStrategy> _Nullable (^)(id))defaultSerializerProvider __attribute__((swift_name("polymorphicDefaultSerializer(baseClass:defaultSerializerProvider:)")));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol CalendrumCoreKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol CalendrumCoreKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol CalendrumCoreKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol CalendrumCoreKotlinKClass <CalendrumCoreKotlinKDeclarationContainer, CalendrumCoreKotlinKAnnotatedElement, CalendrumCoreKotlinKClassifier>
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END

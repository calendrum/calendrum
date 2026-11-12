#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class CalendrumOkioCalendrum_coreAlarmAction, CalendrumOkioCalendrum_coreAlarmActionCompanion, CalendrumOkioCalendrum_coreAttendee, CalendrumOkioCalendrum_coreAttendeeBuilder, CalendrumOkioCalendrum_coreAttendeeCompanion, CalendrumOkioCalendrum_coreAttendeeList, CalendrumOkioCalendrum_coreBuildDefaults, CalendrumOkioCalendrum_coreCardinality, CalendrumOkioCalendrum_coreClassification, CalendrumOkioCalendrum_coreClassificationCompanion, CalendrumOkioCalendrum_coreComponentBuilder<B>, CalendrumOkioCalendrum_coreComponentDefinition, CalendrumOkioCalendrum_coreComponentFingerprint, CalendrumOkioCalendrum_coreComponentKey, CalendrumOkioCalendrum_coreComponentList<T>, CalendrumOkioCalendrum_coreComponentStream, CalendrumOkioCalendrum_coreConference, CalendrumOkioCalendrum_coreConferenceFeature, CalendrumOkioCalendrum_coreConferenceFeatureCompanion, CalendrumOkioCalendrum_coreContentEventType, CalendrumOkioCalendrum_coreCuType, CalendrumOkioCalendrum_coreCuTypeCompanion, CalendrumOkioCalendrum_coreDecodeContext, CalendrumOkioCalendrum_coreDecodeContextCompanion, CalendrumOkioCalendrum_coreEncodeContext, CalendrumOkioCalendrum_coreEventStatus, CalendrumOkioCalendrum_coreEventStatusCompanion, CalendrumOkioCalendrum_coreFbType, CalendrumOkioCalendrum_coreFbTypeCompanion, CalendrumOkioCalendrum_coreFeature, CalendrumOkioCalendrum_coreFeatureCompanion, CalendrumOkioCalendrum_coreFreeBusy, CalendrumOkioCalendrum_coreFrequency, CalendrumOkioCalendrum_coreGeo, CalendrumOkioCalendrum_coreICalComponent, CalendrumOkioCalendrum_coreICalComponentBuilder, CalendrumOkioCalendrum_coreICalComponentCompanion, CalendrumOkioCalendrum_coreICalDuration, CalendrumOkioCalendrum_coreICalDurationCompanion, CalendrumOkioCalendrum_coreICalParameter, CalendrumOkioCalendrum_coreICalParameterCompanion, CalendrumOkioCalendrum_coreICalParser, CalendrumOkioCalendrum_coreICalPeriod, CalendrumOkioCalendrum_coreICalProperty, CalendrumOkioCalendrum_coreICalPropertyBuilder, CalendrumOkioCalendrum_coreICalPropertyCompanion, CalendrumOkioCalendrum_coreICalReader, CalendrumOkioCalendrum_coreICalRegistry, CalendrumOkioCalendrum_coreICalRegistryBuilder, CalendrumOkioCalendrum_coreICalRegistryCompanion, CalendrumOkioCalendrum_coreICalWriter, CalendrumOkioCalendrum_coreIcsSchema<T>, CalendrumOkioCalendrum_coreIcsSchemaField<R>, CalendrumOkioCalendrum_coreIcsSchemaValues, CalendrumOkioCalendrum_coreImage, CalendrumOkioCalendrum_coreImageDisplay, CalendrumOkioCalendrum_coreImageDisplayCompanion, CalendrumOkioCalendrum_coreInterceptedLine, CalendrumOkioCalendrum_coreJournalStatus, CalendrumOkioCalendrum_coreJournalStatusCompanion, CalendrumOkioCalendrum_coreLink, CalendrumOkioCalendrum_coreMethod, CalendrumOkioCalendrum_coreMethodCompanion, CalendrumOkioCalendrum_coreMonthNum, CalendrumOkioCalendrum_coreOrganizer, CalendrumOkioCalendrum_coreOrganizerBuilder, CalendrumOkioCalendrum_coreOrganizerCompanion, CalendrumOkioCalendrum_coreParameterDefinition, CalendrumOkioCalendrum_coreParameterKey<T>, CalendrumOkioCalendrum_coreParseLimits, CalendrumOkioCalendrum_coreParseLimitsBuilder, CalendrumOkioCalendrum_coreParseLimitsCompanion, CalendrumOkioCalendrum_coreParseMode, CalendrumOkioCalendrum_coreParseOptions, CalendrumOkioCalendrum_coreParseOptionsBuilder, CalendrumOkioCalendrum_coreParseOptionsCompanion, CalendrumOkioCalendrum_coreParseResult, CalendrumOkioCalendrum_corePartStat, CalendrumOkioCalendrum_corePartStatCompanion, CalendrumOkioCalendrum_coreParticipant, CalendrumOkioCalendrum_coreParticipantBuilder, CalendrumOkioCalendrum_coreParticipantCompanion, CalendrumOkioCalendrum_coreParticipantType, CalendrumOkioCalendrum_coreParticipantTypeCompanion, CalendrumOkioCalendrum_coreProblem, CalendrumOkioCalendrum_coreProblemCode, CalendrumOkioCalendrum_coreProblemCodeCompanion, CalendrumOkioCalendrum_corePropertyDefinition, CalendrumOkioCalendrum_corePropertyKey<T>, CalendrumOkioCalendrum_coreProximity, CalendrumOkioCalendrum_coreProximityCompanion, CalendrumOkioCalendrum_coreRecur, CalendrumOkioCalendrum_coreRecurBuilder, CalendrumOkioCalendrum_coreRecurCompanion, CalendrumOkioCalendrum_coreRecurPart, CalendrumOkioCalendrum_coreRelType, CalendrumOkioCalendrum_coreRelTypeCompanion, CalendrumOkioCalendrum_coreRelatedTo, CalendrumOkioCalendrum_coreRequestStatus, CalendrumOkioCalendrum_coreResourceType, CalendrumOkioCalendrum_coreResourceTypeCompanion, CalendrumOkioCalendrum_coreRole, CalendrumOkioCalendrum_coreRoleCompanion, CalendrumOkioCalendrum_coreSchemaResult<T>, CalendrumOkioCalendrum_coreSelection, CalendrumOkioCalendrum_coreSelectionBuilder, CalendrumOkioCalendrum_coreSelectionCompanion, CalendrumOkioCalendrum_coreSeverity, CalendrumOkioCalendrum_coreSkip, CalendrumOkioCalendrum_coreSourceSpan, CalendrumOkioCalendrum_coreStructuredData, CalendrumOkioCalendrum_coreStyledDescription, CalendrumOkioCalendrum_coreTimeZoneSelection, CalendrumOkioCalendrum_coreTodoStatus, CalendrumOkioCalendrum_coreTodoStatusCompanion, CalendrumOkioCalendrum_coreTransparency, CalendrumOkioCalendrum_coreTransparencyCompanion, CalendrumOkioCalendrum_coreVAlarm, CalendrumOkioCalendrum_coreVAlarmBuilder, CalendrumOkioCalendrum_coreVAlarmCompanion, CalendrumOkioCalendrum_coreVCalendar, CalendrumOkioCalendrum_coreVCalendarBuilder, CalendrumOkioCalendrum_coreVCalendarCompanion, CalendrumOkioCalendrum_coreVEvent, CalendrumOkioCalendrum_coreVEventBuilder, CalendrumOkioCalendrum_coreVEventCompanion, CalendrumOkioCalendrum_coreVFreeBusy, CalendrumOkioCalendrum_coreVFreeBusyBuilder, CalendrumOkioCalendrum_coreVFreeBusyCompanion, CalendrumOkioCalendrum_coreVJournal, CalendrumOkioCalendrum_coreVJournalBuilder, CalendrumOkioCalendrum_coreVJournalCompanion, CalendrumOkioCalendrum_coreVLocation, CalendrumOkioCalendrum_coreVLocationBuilder, CalendrumOkioCalendrum_coreVLocationCompanion, CalendrumOkioCalendrum_coreVResource, CalendrumOkioCalendrum_coreVResourceBuilder, CalendrumOkioCalendrum_coreVResourceCompanion, CalendrumOkioCalendrum_coreVTimeZone, CalendrumOkioCalendrum_coreVTimeZoneBuilder, CalendrumOkioCalendrum_coreVTimeZoneCompanion, CalendrumOkioCalendrum_coreVTimeZoneObservance, CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder, CalendrumOkioCalendrum_coreVTodo, CalendrumOkioCalendrum_coreVTodoBuilder, CalendrumOkioCalendrum_coreVTodoCompanion, CalendrumOkioCalendrum_coreValueType, CalendrumOkioCalendrum_coreValueTypeCompanion, CalendrumOkioCalendrum_coreWeekdayNum, CalendrumOkioKotlinArray<T>, CalendrumOkioKotlinByteArray, CalendrumOkioKotlinByteIterator, CalendrumOkioKotlinEnum<E>, CalendrumOkioKotlinEnumCompanion, CalendrumOkioKotlinException, CalendrumOkioKotlinInstant, CalendrumOkioKotlinInstantCompanion, CalendrumOkioKotlinIntArray, CalendrumOkioKotlinIntIterator, CalendrumOkioKotlinNothing, CalendrumOkioKotlinRuntimeException, CalendrumOkioKotlinThrowable, CalendrumOkioKotlinx_datetimeDateTimePeriod, CalendrumOkioKotlinx_datetimeDateTimePeriodCompanion, CalendrumOkioKotlinx_datetimeDayOfWeek, CalendrumOkioKotlinx_datetimePadding, CalendrumOkioKotlinx_datetimeUtcOffset, CalendrumOkioKotlinx_datetimeUtcOffsetCompanion, CalendrumOkioKotlinx_serialization_coreSerialKind, CalendrumOkioKotlinx_serialization_coreSerializersModule, CalendrumOkioOkioBuffer, CalendrumOkioOkioBufferUnsafeCursor, CalendrumOkioOkioByteString, CalendrumOkioOkioByteStringCompanion, CalendrumOkioOkioTimeout, CalendrumOkioOkioTimeoutCompanion, NSData;

@protocol CalendrumOkioCalendrum_coreAttach, CalendrumOkioCalendrum_coreByteInput, CalendrumOkioCalendrum_coreByteOutput, CalendrumOkioCalendrum_coreDecoded, CalendrumOkioCalendrum_coreICalDateTime, CalendrumOkioCalendrum_coreICalExtension, CalendrumOkioCalendrum_coreLineInterceptor, CalendrumOkioCalendrum_coreParameterCodec, CalendrumOkioCalendrum_coreProblemListener, CalendrumOkioCalendrum_corePropertyCodec, CalendrumOkioCalendrum_coreTrigger, CalendrumOkioCalendrum_coreUidGenerator, CalendrumOkioKotlinAnnotation, CalendrumOkioKotlinAppendable, CalendrumOkioKotlinClock, CalendrumOkioKotlinComparable, CalendrumOkioKotlinIterator, CalendrumOkioKotlinKAnnotatedElement, CalendrumOkioKotlinKClass, CalendrumOkioKotlinKClassifier, CalendrumOkioKotlinKDeclarationContainer, CalendrumOkioKotlinSequence, CalendrumOkioKotlinTimeMark, CalendrumOkioKotlinTimeSource, CalendrumOkioKotlinTimeSourceWithComparableMarks, CalendrumOkioKotlinx_datetimeDateTimeFormat, CalendrumOkioKotlinx_datetimeDateTimeFormatBuilder, CalendrumOkioKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset, CalendrumOkioKotlinx_serialization_coreCompositeDecoder, CalendrumOkioKotlinx_serialization_coreCompositeEncoder, CalendrumOkioKotlinx_serialization_coreDecoder, CalendrumOkioKotlinx_serialization_coreDeserializationStrategy, CalendrumOkioKotlinx_serialization_coreEncoder, CalendrumOkioKotlinx_serialization_coreKSerializer, CalendrumOkioKotlinx_serialization_coreSerialDescriptor, CalendrumOkioKotlinx_serialization_coreSerializationStrategy, CalendrumOkioKotlinx_serialization_coreSerializersModuleCollector, CalendrumOkioOkioBufferedSink, CalendrumOkioOkioBufferedSource, CalendrumOkioOkioCloseable, CalendrumOkioOkioSink, CalendrumOkioOkioSource;

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
@interface CalendrumOkioBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface CalendrumOkioBase (CalendrumOkioBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface CalendrumOkioMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface CalendrumOkioMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorCalendrumOkioKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface CalendrumOkioNumber : NSNumber
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
@interface CalendrumOkioByte : CalendrumOkioNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface CalendrumOkioUByte : CalendrumOkioNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface CalendrumOkioShort : CalendrumOkioNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface CalendrumOkioUShort : CalendrumOkioNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface CalendrumOkioInt : CalendrumOkioNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface CalendrumOkioUInt : CalendrumOkioNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface CalendrumOkioLong : CalendrumOkioNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface CalendrumOkioULong : CalendrumOkioNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface CalendrumOkioFloat : CalendrumOkioNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface CalendrumOkioDouble : CalendrumOkioNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface CalendrumOkioBoolean : CalendrumOkioNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
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
__attribute__((swift_name("Calendrum_coreICalParser")))
@interface CalendrumOkioCalendrum_coreICalParser : CalendrumOkioBase

/**
 * Returns a stream of the children of every `VCALENDAR` that [input] yields, one component at a time, with memory
 * bounded by a block and the longest component. The input is not closed.
 */
- (CalendrumOkioCalendrum_coreComponentStream *)componentsInput:(id<CalendrumOkioCalendrum_coreByteInput>)input __attribute__((swift_name("components(input:)")));

/**
 * Returns a stream of the children of every `VCALENDAR` in [bytes], one component at a time.
 *
 * @param bytes the complete content.
 */
- (CalendrumOkioCalendrum_coreComponentStream *)componentsBytes:(CalendrumOkioKotlinByteArray *)bytes __attribute__((swift_name("components(bytes:)")));

/**
 * Decodes everything [input] yields into objects of [schema], one at a time, with bounded memory.
 *
 * The sequence can be iterated once and reads the input as it goes; a strict parser throws [ICalParseException]
 * from the iterator.
 */
- (id<CalendrumOkioKotlinSequence>)decodeInput:(id<CalendrumOkioCalendrum_coreByteInput>)input schema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema __attribute__((swift_name("decode(input:schema:)")));

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
- (NSArray<id> * _Nullable)decodeBytes:(CalendrumOkioKotlinByteArray *)bytes schema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decode(bytes:schema:)")));

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
- (CalendrumOkioCalendrum_coreSchemaResult<id> * _Nullable)decodeWithProblemsBytes:(CalendrumOkioKotlinByteArray *)bytes schema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decodeWithProblems(bytes:schema:)")));

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
- (CalendrumOkioCalendrum_coreParseResult * _Nullable)parseInput:(id<CalendrumOkioCalendrum_coreByteInput>)input error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(input:)")));

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
- (CalendrumOkioCalendrum_coreParseResult * _Nullable)parseBytes:(CalendrumOkioKotlinByteArray *)bytes error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(bytes:)")));

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
- (CalendrumOkioCalendrum_coreParseResult * _Nullable)parseText:(NSString *)text error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(text:)")));

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
- (CalendrumOkioCalendrum_coreParseResult * _Nullable)parseBytes:(CalendrumOkioKotlinByteArray *)bytes offset:(int32_t)offset length:(int32_t)length error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(bytes:offset:length:)")));

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
- (CalendrumOkioCalendrum_coreICalComponent * _Nullable)parseComponentBytes:(CalendrumOkioKotlinByteArray *)bytes fingerprint:(CalendrumOkioCalendrum_coreComponentFingerprint *)fingerprint error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parseComponent(bytes:fingerprint:)")));

/** Returns a pull cursor over everything [input] yields; the input is read block by block and not closed. */
- (CalendrumOkioCalendrum_coreICalReader *)readerInput:(id<CalendrumOkioCalendrum_coreByteInput>)input __attribute__((swift_name("reader(input:)")));

/**
 * Returns a pull cursor over [bytes] that reports events without building a tree.
 *
 * The cursor allocates nothing per event; see [ICalReader] for what each event offers.
 *
 * @param bytes the complete content.
 */
- (CalendrumOkioCalendrum_coreICalReader *)readerBytes:(CalendrumOkioKotlinByteArray *)bytes __attribute__((swift_name("reader(bytes:)")));

/** Returns a pull cursor over [text], encoded to UTF-8. */
- (CalendrumOkioCalendrum_coreICalReader *)readerText:(NSString *)text __attribute__((swift_name("reader(text:)")));

/** Returns a pull cursor over [length] bytes of [bytes] starting at [offset]. */
- (CalendrumOkioCalendrum_coreICalReader *)readerBytes:(CalendrumOkioKotlinByteArray *)bytes offset:(int32_t)offset length:(int32_t)length __attribute__((swift_name("reader(bytes:offset:length:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreParseOptions *options __attribute__((swift_name("options")));
@end

@interface CalendrumOkioCalendrum_coreICalParser (Extensions)

/**
 * Returns a stream of the children of every `VCALENDAR` that [source] yields, one component at a time; the source is
 * not closed. An Okio `Buffer` of up to 16 MiB is copied into one array and streamed as an array, as in `parse`.
 *
 * @throws IOException if reading fails.
 *
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumOkioCalendrum_coreComponentStream * _Nullable)componentsSource:(id<CalendrumOkioOkioBufferedSource>)source error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("components(source:)")));

/**
 * Parses everything [source] yields, reading it block by block. An Okio `Buffer` of up to 16 MiB, which already holds
 * all its bytes, is copied into one array and parsed as an array, which is faster; while it is copied the bytes are in
 * memory twice, so a larger buffer is read block by block. The result equals that of parsing the same bytes as an
 * array. The source is not closed.
 *
 * @throws IOException if reading fails.
 * @throws ICalParseException in strict mode, at the first error.
 * @sample io.github.calendrum.samples.parseAndWriteOkio
 *
 * @note This method converts instances of IOException, ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumOkioCalendrum_coreParseResult * _Nullable)parseSource:(id<CalendrumOkioOkioBufferedSource>)source error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("parse(source:)")));

/**
 * Returns a pull cursor over everything [source] yields, without building a tree; the source is not closed.
 *
 * @throws IOException if reading fails.
 *
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumOkioCalendrum_coreICalReader * _Nullable)readerSource:(id<CalendrumOkioOkioBufferedSource>)source error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("reader(source:)")));
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
__attribute__((swift_name("Calendrum_coreICalWriter")))
@interface CalendrumOkioCalendrum_coreICalWriter : CalendrumOkioBase

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
- (CalendrumOkioKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema calendar:(CalendrumOkioCalendrum_coreVCalendar *)calendar error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:calendar:)")));

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
- (CalendrumOkioKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema error:(NSError * _Nullable * _Nullable)error buildCalendar:(void (^)(CalendrumOkioCalendrum_coreVCalendarBuilder *))buildCalendar __attribute__((swift_name("encode(items:schema:buildCalendar:)")));

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
- (CalendrumOkioKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema calendar:(CalendrumOkioCalendrum_coreVCalendar *)calendar defaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:calendar:defaults:)")));

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
- (CalendrumOkioKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema buildCalendar:(void (^)(CalendrumOkioCalendrum_coreVCalendarBuilder *))buildCalendar defaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:buildCalendar:defaults:)")));

/**
 * Writes [component] and returns the UTF-8 bytes.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumOkioKotlinByteArray * _Nullable)encodeToByteArrayComponent:(CalendrumOkioCalendrum_coreICalComponent *)component error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToByteArray(component:)")));

/**
 * Writes all [components] one after another and returns the UTF-8 bytes.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumOkioKotlinByteArray * _Nullable)encodeToByteArrayComponents:(NSArray<CalendrumOkioCalendrum_coreICalComponent *> *)components error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToByteArray(components:)")));

/**
 * Writes [component] and returns the text.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSString * _Nullable)encodeToStringComponent:(CalendrumOkioCalendrum_coreICalComponent *)component error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToString(component:)")));

/**
 * Writes all [components] one after another and returns the text.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSString * _Nullable)encodeToStringComponents:(NSArray<CalendrumOkioCalendrum_coreICalComponent *> *)components error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToString(components:)")));

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
- (BOOL)writeComponent:(CalendrumOkioCalendrum_coreICalComponent *)component out:(id<CalendrumOkioCalendrum_coreByteOutput>)out error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(component:out:)")));

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
- (BOOL)writeComponent:(CalendrumOkioCalendrum_coreICalComponent *)component appendable:(id<CalendrumOkioKotlinAppendable>)appendable error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(component:appendable:)")));

/**
 * Writes all [components] to [out] as UTF-8 bytes; [out] is not closed.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponents:(NSArray<CalendrumOkioCalendrum_coreICalComponent *> *)components out:(id<CalendrumOkioCalendrum_coreByteOutput>)out error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(components:out:)")));

/**
 * Writes all [components] to [out] one after another.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponents:(NSArray<CalendrumOkioCalendrum_coreICalComponent *> *)components appendable:(id<CalendrumOkioKotlinAppendable>)appendable error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(components:appendable:)")));
@end

@interface CalendrumOkioCalendrum_coreICalWriter (Extensions)

/**
 * Writes [component] to [sink] as UTF-8; the sink is neither flushed nor closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @sample io.github.calendrum.samples.parseAndWriteOkio
 *
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponent:(CalendrumOkioCalendrum_coreICalComponent *)component sink:(id<CalendrumOkioOkioBufferedSink>)sink error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(component:sink:)")));

/**
 * Writes all [components] to [sink] as UTF-8; the sink is neither flushed nor closed.
 *
 * @throws IOException if writing fails.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 *
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponents:(NSArray<CalendrumOkioCalendrum_coreICalComponent *> *)components sink:(id<CalendrumOkioOkioBufferedSink>)sink error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(components:sink:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OkioKt")))
@interface CalendrumOkioOkioKt : CalendrumOkioBase

/**
 * Adapts this Okio `BufferedSource` to a [ByteInput]; reading is passed to the source, which the library never closes.
 */
+ (id<CalendrumOkioCalendrum_coreByteInput>)asByteInput:(id<CalendrumOkioOkioBufferedSource>)receiver __attribute__((swift_name("asByteInput(_:)")));

/**
 * Adapts this Okio `BufferedSink` to a [ByteOutput]; writing is passed to the sink, which the library neither flushes
 * nor closes.
 */
+ (id<CalendrumOkioCalendrum_coreByteOutput>)asByteOutput:(id<CalendrumOkioOkioBufferedSink>)receiver __attribute__((swift_name("asByteOutput(_:)")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface CalendrumOkioKotlinThrowable : CalendrumOkioBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (CalendrumOkioKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface CalendrumOkioKotlinException : CalendrumOkioKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * Thrown by a parser in strict mode when the input has a problem of severity [Severity.Error].
 *
 * The parser stops at the first error, so [problems] ends with it. In lenient mode parsing never throws on data.
 *
 * The message is the text of the last error in [problems]. It never contains property values.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalParseException")))
@interface CalendrumOkioCalendrum_coreICalParseException : CalendrumOkioKotlinException
- (instancetype)initWithProblems:(NSArray<CalendrumOkioCalendrum_coreProblem *> *)problems __attribute__((swift_name("init(problems:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithCause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));

/** The problems found up to and including the error that stopped the parse, in the order they were found. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreProblem *> *problems __attribute__((swift_name("problems")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol CalendrumOkioKotlinIterator
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
__attribute__((swift_name("Calendrum_coreComponentStream")))
@interface CalendrumOkioCalendrum_coreComponentStream : CalendrumOkioBase <CalendrumOkioKotlinIterator>

/** Returns the remaining components as a sequence that can be iterated only once. */
- (id<CalendrumOkioKotlinSequence>)asSequence __attribute__((swift_name("asSequence()")));

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
- (CalendrumOkioCalendrum_coreICalComponent *)next __attribute__((swift_name("next()")));

/**
 * Same as [next]; it exists so that Java callers see the checked [ICalParseException].
 *
 * @throws ICalParseException in strict mode, at the first error.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumOkioCalendrum_coreICalComponent * _Nullable)nextOrThrowAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("nextOrThrow()")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalProperty *> *calendarProperties __attribute__((swift_name("calendarProperties")));

/** The problems found so far, in the order they were found; it grows as the stream is consumed. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreProblem *> *problems __attribute__((swift_name("problems")));
@end


/**
 * A pull source of bytes, the platform-independent counterpart of an input stream.
 *
 * Adapters for `java.io.InputStream`, kotlinx-io `Source` and Okio `BufferedSource` live in the optional modules. The
 * parser never closes an input; closing is the caller's job.
 */
__attribute__((swift_name("Calendrum_coreByteInput")))
@protocol CalendrumOkioCalendrum_coreByteInput
@required

/**
 * Reads up to [length] bytes into [buffer] starting at [offset].
 *
 * @return the number of bytes read, at least 1, or -1 at the end of the input.
 */
- (int32_t)readBuffer:(CalendrumOkioKotlinByteArray *)buffer offset:(int32_t)offset length:(int32_t)length __attribute__((swift_name("read(buffer:offset:length:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface CalendrumOkioKotlinByteArray : CalendrumOkioBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumOkioByte *(^)(CalendrumOkioInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumOkioKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinSequence")))
@protocol CalendrumOkioKotlinSequence
@required
- (id<CalendrumOkioKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
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
__attribute__((swift_name("Calendrum_coreIcsSchema")))
@interface CalendrumOkioCalendrum_coreIcsSchema<T> : CalendrumOkioBase
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
- (CalendrumOkioCalendrum_coreIcsSchemaField<NSArray<id> *> *)childFieldSchema:(CalendrumOkioCalendrum_coreIcsSchema<id> *)schema get:(NSArray<id> *(^)(T))get __attribute__((swift_name("childField(schema:get:)")));

/**
 * Builds the object from the decoded [values]; read each declared field with `values[field]`.
 *
 * @param values the decoded values of one component.
 */
- (T)createValues:(CalendrumOkioCalendrum_coreIcsSchemaValues *)values __attribute__((swift_name("create(values:)")));

/**
 * Declares a required field that holds the value of the property [key] as it is decoded; [get] reads it back when
 * writing.
 *
 * @param key the property.
 * @param get reads the value from an object when writing.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumOkioCalendrum_coreIcsSchemaField<id> *)fieldKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key get:(id (^)(T))get __attribute__((swift_name("field(key:get:)")));

/**
 * Declares a required field whose value is converted from the decoded property value by [decode].
 *
 * @param key the property.
 * @param get reads the value from an object when writing; it returns the property value.
 * @param decode converts the decoded value into the type of the field.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumOkioCalendrum_coreIcsSchemaField<id> *)fieldKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key get:(id (^)(T))get decode:(id (^)(id))decode __attribute__((swift_name("field(key:get:decode:)")));

/**
 * Declares a field that collects every property [key] into a list; empty when there is none.
 *
 * @param key the property.
 * @param get reads the list from an object when writing; each element becomes a property.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumOkioCalendrum_coreIcsSchemaField<NSArray<id> *> *)listFieldKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key get:(NSArray<id> *(^)(T))get __attribute__((swift_name("listField(key:get:)")));

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
- (CalendrumOkioCalendrum_coreIcsSchemaField<NSArray<id> *> *)listFieldKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key get:(NSArray<id> *(^)(T))get decode:(id _Nullable (^)(id))decode encode:(id (^)(id _Nullable))encode __attribute__((swift_name("listField(key:get:decode:encode:)")));

/**
 * Declares an optional field: `null` when the property is missing or does not decode.
 *
 * @param key the property.
 * @param get reads the value from an object when writing; `null` writes nothing.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumOkioCalendrum_coreIcsSchemaField<id> *)optionalFieldKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key get:(id _Nullable (^)(T))get __attribute__((swift_name("optionalField(key:get:)")));

/**
 * Declares an optional field whose value is converted from the decoded property value by [decode].
 *
 * @param key the property.
 * @param get reads the value from an object when writing; `null` writes nothing.
 * @param decode converts the decoded value into the type of the field.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumOkioCalendrum_coreIcsSchemaField<id> *)optionalFieldKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key get:(id _Nullable (^)(T))get decode:(id (^)(id))decode __attribute__((swift_name("optionalField(key:get:decode:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * The result of decoding into a schema.
 *
 * @property items the objects decoded, in input order.
 * @property problems the problems found, with values never shown.
 * @property skipped the number of components left out because a required field was missing or broken.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSchemaResult")))
@interface CalendrumOkioCalendrum_coreSchemaResult<T> : CalendrumOkioBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<T> *items __attribute__((swift_name("items")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreProblem *> *problems __attribute__((swift_name("problems")));
@property (readonly) int32_t skipped __attribute__((swift_name("skipped")));
@end


/**
 * The outcome of parsing: the tree, the problems found and facts about the input.
 *
 * A result is immutable. [components] are the top-level components, usually a single `VCALENDAR`. Reading values from
 * the tree is lazy and cached in the tree; use `calendars` of the typed model for wrappers with typed accessors.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseResult")))
@interface CalendrumOkioCalendrum_coreParseResult : CalendrumOkioBase

/**
 * Returns how many problems with [code] were found, including those beyond the reporting limit that are only
 * counted.
 *
 * @param code the code to count.
 */
- (int32_t)problemCountCode:(CalendrumOkioCalendrum_coreProblemCode *)code __attribute__((swift_name("problemCount(code:)")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalComponent *> *components __attribute__((swift_name("components")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreProblem *> *problems __attribute__((swift_name("problems")));
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
__attribute__((swift_name("Calendrum_coreICalComponent")))
@interface CalendrumOkioCalendrum_coreICalComponent : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreICalComponentCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the directly nested components named [name], ignoring ASCII case, in source order; the list is empty when
 * there are none. Deeper levels are not searched.
 *
 * @param name a component name such as `VEVENT`.
 */
- (NSArray<CalendrumOkioCalendrum_coreICalComponent *> *)componentsName:(NSString *)name __attribute__((swift_name("components(name:)")));

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
- (NSArray<CalendrumOkioCalendrum_coreICalProperty *> *)propertiesName:(NSString *)name __attribute__((swift_name("properties(name:)")));

/**
 * Returns the first property named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a property name such as `SUMMARY`.
 */
- (CalendrumOkioCalendrum_coreICalProperty * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns the name and the number of properties and nested components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The directly nested components, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalComponent *> *components __attribute__((swift_name("components")));

/**
 * The canonical name in upper case, such as `VEVENT`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/**
 * The properties of this component, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalProperty *> *properties __attribute__((swift_name("properties")));

/**
 * Where the component starts in the input and where its `END` line ends, or `null` for a component that was built
 * by hand or edited.
 */
@property (readonly) CalendrumOkioCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));
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
__attribute__((swift_name("Calendrum_coreComponentFingerprint")))
@interface CalendrumOkioCalendrum_coreComponentFingerprint : CalendrumOkioBase

/** Returns `true` when [other] is a ComponentFingerprint equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t endOffset __attribute__((swift_name("endOffset")));
@property (readonly, getter=hash_) int64_t hash __attribute__((swift_name("hash")));
@property (readonly) CalendrumOkioCalendrum_coreComponentKey *key __attribute__((swift_name("key")));
@property (readonly) int64_t startOffset __attribute__((swift_name("startOffset")));
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
__attribute__((swift_name("Calendrum_coreICalReader")))
@interface CalendrumOkioCalendrum_coreICalReader : CalendrumOkioBase

/**
 * Moves to the next event.
 *
 * @return the new event, or `null` at the end of the input.
 * @throws ICalParseException in strict mode, at the first error; later calls rethrow it.
 *
 * @note This method converts instances of ICalParseException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumOkioCalendrum_coreContentEventType * _Nullable)nextAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("next()"))) __attribute__((swift_error(nonnull_error)));

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
- (CalendrumOkioCalendrum_coreICalComponent * _Nullable)readComponentAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("readComponent()")));

/**
 * Returns the position of the current event: the whole line of a `Begin`, `Property` or `End`, or a point for an
 * implicit end.
 *
 * @throws IllegalStateException if there is no current event.
 */
- (CalendrumOkioCalendrum_coreSourceSpan *)span __attribute__((swift_name("span()")));

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
__attribute__((swift_name("Calendrum_coreParseOptions")))
@interface CalendrumOkioCalendrum_coreParseOptions : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreParseOptionsCompanion *companion __attribute__((swift_name("companion")));

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
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreLineInterceptor> (^)(void)> *interceptors __attribute__((swift_name("interceptors")));
@property (readonly) CalendrumOkioCalendrum_coreParseLimits *limits __attribute__((swift_name("limits")));
@property (readonly) CalendrumOkioCalendrum_coreParseMode *mode __attribute__((swift_name("mode")));
@property (readonly) id<CalendrumOkioCalendrum_coreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/**
 * The registry that tells eager decoding and strict mode what every property is; [ICalRegistry.Default] unless one
 * was set.
 */
@property (readonly) CalendrumOkioCalendrum_coreICalRegistry *registry __attribute__((swift_name("registry")));
@property (readonly) BOOL retainSource __attribute__((swift_name("retainSource")));

/** Which components and properties are read; [Selection.Full] unless a selection was set. */
@property (readonly) CalendrumOkioCalendrum_coreSelection *selection __attribute__((swift_name("selection")));

/** The clock used for the time budget; the monotonic clock unless one was set. */
@property (readonly) id<CalendrumOkioKotlinTimeSourceWithComparableMarks> timeSource __attribute__((swift_name("timeSource")));
@end

__attribute__((swift_name("OkioIOException")))
@interface CalendrumOkioOkioIOException : CalendrumOkioKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@end

__attribute__((swift_name("OkioCloseable")))
@protocol CalendrumOkioOkioCloseable
@required

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)closeAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("close()")));
@end

__attribute__((swift_name("OkioSource")))
@protocol CalendrumOkioOkioSource <CalendrumOkioOkioCloseable>
@required

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (int64_t)readSink:(CalendrumOkioOkioBuffer *)sink byteCount:(int64_t)byteCount error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("read(sink:byteCount:)"))) __attribute__((swift_error(nonnull_error)));
- (CalendrumOkioOkioTimeout *)timeout __attribute__((swift_name("timeout()")));
@end

__attribute__((swift_name("OkioBufferedSource")))
@protocol CalendrumOkioOkioBufferedSource <CalendrumOkioOkioSource>
@required
- (BOOL)exhausted __attribute__((swift_name("exhausted()")));
- (int64_t)indexOfB:(int8_t)b __attribute__((swift_name("indexOf(b:)")));
- (int64_t)indexOfBytes:(CalendrumOkioOkioByteString *)bytes __attribute__((swift_name("indexOf(bytes:)")));
- (int64_t)indexOfB:(int8_t)b fromIndex:(int64_t)fromIndex __attribute__((swift_name("indexOf(b:fromIndex:)")));
- (int64_t)indexOfBytes:(CalendrumOkioOkioByteString *)bytes fromIndex:(int64_t)fromIndex __attribute__((swift_name("indexOf(bytes:fromIndex:)")));
- (int64_t)indexOfB:(int8_t)b fromIndex:(int64_t)fromIndex toIndex:(int64_t)toIndex __attribute__((swift_name("indexOf(b:fromIndex:toIndex:)")));
- (int64_t)indexOfElementTargetBytes:(CalendrumOkioOkioByteString *)targetBytes __attribute__((swift_name("indexOfElement(targetBytes:)")));
- (int64_t)indexOfElementTargetBytes:(CalendrumOkioOkioByteString *)targetBytes fromIndex:(int64_t)fromIndex __attribute__((swift_name("indexOfElement(targetBytes:fromIndex:)")));
- (id<CalendrumOkioOkioBufferedSource>)peek __attribute__((swift_name("peek()")));
- (BOOL)rangeEqualsOffset:(int64_t)offset bytes:(CalendrumOkioOkioByteString *)bytes __attribute__((swift_name("rangeEquals(offset:bytes:)")));
- (BOOL)rangeEqualsOffset:(int64_t)offset bytes:(CalendrumOkioOkioByteString *)bytes bytesOffset:(int32_t)bytesOffset byteCount:(int32_t)byteCount __attribute__((swift_name("rangeEquals(offset:bytes:bytesOffset:byteCount:)")));
- (int32_t)readSink:(CalendrumOkioKotlinByteArray *)sink __attribute__((swift_name("read(sink:)")));
- (int32_t)readSink:(CalendrumOkioKotlinByteArray *)sink offset:(int32_t)offset byteCount:(int32_t)byteCount __attribute__((swift_name("read(sink:offset:byteCount:)")));
- (int64_t)readAllSink:(id<CalendrumOkioOkioSink>)sink __attribute__((swift_name("readAll(sink:)")));
- (int8_t)readByte __attribute__((swift_name("readByte()")));
- (CalendrumOkioKotlinByteArray *)readByteArray __attribute__((swift_name("readByteArray()")));
- (CalendrumOkioKotlinByteArray *)readByteArrayByteCount:(int64_t)byteCount __attribute__((swift_name("readByteArray(byteCount:)")));
- (CalendrumOkioOkioByteString *)readByteString __attribute__((swift_name("readByteString()")));
- (CalendrumOkioOkioByteString *)readByteStringByteCount:(int64_t)byteCount __attribute__((swift_name("readByteString(byteCount:)")));
- (int64_t)readDecimalLong __attribute__((swift_name("readDecimalLong()")));
- (void)readFullySink:(CalendrumOkioKotlinByteArray *)sink __attribute__((swift_name("readFully(sink:)")));
- (void)readFullySink:(CalendrumOkioOkioBuffer *)sink byteCount:(int64_t)byteCount __attribute__((swift_name("readFully(sink:byteCount:)")));
- (int64_t)readHexadecimalUnsignedLong __attribute__((swift_name("readHexadecimalUnsignedLong()")));
- (int32_t)readInt __attribute__((swift_name("readInt()")));
- (int32_t)readIntLe __attribute__((swift_name("readIntLe()")));
- (int64_t)readLong __attribute__((swift_name("readLong()")));
- (int64_t)readLongLe __attribute__((swift_name("readLongLe()")));
- (int16_t)readShort __attribute__((swift_name("readShort()")));
- (int16_t)readShortLe __attribute__((swift_name("readShortLe()")));
- (NSString *)readUtf8 __attribute__((swift_name("readUtf8()")));
- (NSString *)readUtf8ByteCount:(int64_t)byteCount __attribute__((swift_name("readUtf8(byteCount:)")));
- (int32_t)readUtf8CodePoint __attribute__((swift_name("readUtf8CodePoint()")));
- (NSString * _Nullable)readUtf8Line __attribute__((swift_name("readUtf8Line()")));
- (NSString *)readUtf8LineStrict __attribute__((swift_name("readUtf8LineStrict()")));
- (NSString *)readUtf8LineStrictLimit:(int64_t)limit __attribute__((swift_name("readUtf8LineStrict(limit:)")));
- (BOOL)requestByteCount:(int64_t)byteCount __attribute__((swift_name("request(byteCount:)")));
- (void)requireByteCount:(int64_t)byteCount __attribute__((swift_name("require(byteCount:)")));
- (int32_t)selectOptions:(NSArray<CalendrumOkioOkioByteString *> *)options __attribute__((swift_name("select(options:)")));
- (void)skipByteCount:(int64_t)byteCount __attribute__((swift_name("skip(byteCount:)")));
@property (readonly) CalendrumOkioOkioBuffer *buffer __attribute__((swift_name("buffer")));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface CalendrumOkioKotlinRuntimeException : CalendrumOkioKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalArgumentException")))
@interface CalendrumOkioKotlinIllegalArgumentException : CalendrumOkioKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumOkioKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
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
__attribute__((swift_name("Calendrum_coreVCalendar")))
@interface CalendrumOkioCalendrum_coreVCalendar : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVCalendarCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this calendar changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this calendar.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVCalendar *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVCalendarBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VCalendar with an equal VCALENDAR component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this calendar; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this calendar and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

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
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

/**
 * The descriptions of the calendar (`DESCRIPTION` properties,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *descriptions __attribute__((swift_name("descriptions")));

/** The events nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy component nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The images of the calendar (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/** The journal entries nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)), or `null` when it is missing or does
 * not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreMethod * _Nullable method __attribute__((swift_name("method")));

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
@property (readonly) CalendrumOkioCalendrum_coreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar (`SOURCE`, [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVTodo *> *todos __attribute__((swift_name("todos")));

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
 * The common part of the builders of all components: reading and writing properties by key or by name.
 *
 * The typed builders ([VEvent.Builder] and the others) add the accessors of their component. A builder starts empty or
 * from an existing component, shares the nodes it does not touch with it, and is not thread-safe.
 */
__attribute__((swift_name("Calendrum_coreComponentBuilder")))
@interface CalendrumOkioCalendrum_coreComponentBuilder<B> : CalendrumOkioBase

/** Returns the value of the first property of [key], or `null`. */
- (id _Nullable)getKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key __attribute__((swift_name("get(key:)")));

/**
 * Adds [property] as it is.
 *
 * @return this builder.
 */
- (B)propertyProperty:(CalendrumOkioCalendrum_coreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Adds a property of [key] with [value] after the existing ones.
 *
 * @return this builder.
 */
- (B)propertyKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key value:(id)value __attribute__((swift_name("property(key:value:)")));

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
- (B)removeKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key __attribute__((swift_name("remove(key:)")));

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
- (B)setKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key value:(id _Nullable)value __attribute__((swift_name("set(key:value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * Collects the properties and nested components of a new or edited calendar, with a Kotlin DSL and chained calls.
 * Get one from [VCalendar.builder] or [VCalendar.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVCalendar.Builder")))
@interface CalendrumOkioCalendrum_coreVCalendarBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVCalendarBuilder *>

/** Creates the calendar. */
- (CalendrumOkioCalendrum_coreVCalendar *)build __attribute__((swift_name("build()")));

/**
 * Sets [calScale] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)calScaleValue:(NSString * _Nullable)value __attribute__((swift_name("calScale(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)categoriesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds [event] as a event of the calendar. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)eventEvent:(CalendrumOkioCalendrum_coreVEvent *)event __attribute__((swift_name("event(event:)")));

/**
 * Adds a event that [block] sets up.
 *
 * @param block sets up the new event.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)eventBlock:(void (^)(CalendrumOkioCalendrum_coreVEventBuilder *))block __attribute__((swift_name("event(block:)")));

/** Edits the events of the calendar in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)eventsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVEvent *> *))block __attribute__((swift_name("events(block:)")));

/** Edits the free/busy components of the calendar in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)freeBusiesBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVFreeBusy *> *))block __attribute__((swift_name("freeBusies(block:)")));

/** Adds [freeBusy] as a free/busy component of the calendar. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)freeBusyFreeBusy:(CalendrumOkioCalendrum_coreVFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/**
 * Adds a free/busy component that [block] sets up.
 *
 * @param block sets up the new free/busy component.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)freeBusyBlock:(void (^)(CalendrumOkioCalendrum_coreVFreeBusyBuilder *))block __attribute__((swift_name("freeBusy(block:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)imageValue:(CalendrumOkioCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/** Adds [journal] as a journal entry of the calendar. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)journalJournal:(CalendrumOkioCalendrum_coreVJournal *)journal __attribute__((swift_name("journal(journal:)")));

/**
 * Adds a journal entry that [block] sets up.
 *
 * @param block sets up the new journal entry.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)journalBlock:(void (^)(CalendrumOkioCalendrum_coreVJournalBuilder *))block __attribute__((swift_name("journal(block:)")));

/** Edits the journal entries of the calendar in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)journalsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVJournal *> *))block __attribute__((swift_name("journals(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)lastModifiedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/**
 * Sets [method] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)methodValue:(CalendrumOkioCalendrum_coreMethod * _Nullable)value __attribute__((swift_name("method(value:)")));

/** Adds a `NAME` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/**
 * Sets [prodId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)prodIdValue:(NSString * _Nullable)value __attribute__((swift_name("prodId(value:)")));

/**
 * Sets [refreshInterval] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)refreshIntervalValue:(CalendrumOkioCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("refreshInterval(value:)")));

/**
 * Sets [source] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)sourceValue:(NSString * _Nullable)value __attribute__((swift_name("source(value:)")));

/** Adds [timeZone] as a time zone of the calendar. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)timeZoneTimeZone:(CalendrumOkioCalendrum_coreVTimeZone *)timeZone __attribute__((swift_name("timeZone(timeZone:)")));

/**
 * Adds a time zone that [block] sets up.
 *
 * @param block sets up the new time zone.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)timeZoneBlock:(void (^)(CalendrumOkioCalendrum_coreVTimeZoneBuilder *))block __attribute__((swift_name("timeZone(block:)")));

/** Edits the time zones of the calendar in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)timeZonesBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVTimeZone *> *))block __attribute__((swift_name("timeZones(block:)")));

/** Adds [todo] as a to-do of the calendar. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)todoTodo:(CalendrumOkioCalendrum_coreVTodo *)todo __attribute__((swift_name("todo(todo:)")));

/**
 * Adds a to-do that [block] sets up.
 *
 * @param block sets up the new to-do.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)todoBlock:(void (^)(CalendrumOkioCalendrum_coreVTodoBuilder *))block __attribute__((swift_name("todo(block:)")));

/** Edits the to-dos of the calendar in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)todosBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVTodo *> *))block __attribute__((swift_name("todos(block:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * Sets [version] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)versionValue:(NSString * _Nullable)value __attribute__((swift_name("version(value:)")));

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
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy components of the calendar being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVFreeBusy *> *freeBusies __attribute__((swift_name("freeBusies")));

/** The journal entries of the calendar being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar being built (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreMethod * _Nullable method __attribute__((swift_name("method")));

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
@property CalendrumOkioCalendrum_coreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar being built (`SOURCE`,
 * [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)); assigning `null` removes the property.
 */
@property NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones of the calendar being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos of the calendar being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVTodo *> *todos __attribute__((swift_name("todos")));

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


/**
 * What a builder uses when a required property is missing: a clock for `DTSTAMP` and a generator for `UID`.
 *
 * The library never reads the system clock or a global generator by itself.
 *
 * @property clock gives the time for `DTSTAMP`.
 * @property uids makes the `UID`s.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreBuildDefaults")))
@interface CalendrumOkioCalendrum_coreBuildDefaults : CalendrumOkioBase
- (instancetype)initWithClock:(id<CalendrumOkioKotlinClock>)clock uids:(id<CalendrumOkioCalendrum_coreUidGenerator>)uids __attribute__((swift_name("init(clock:uids:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumOkioKotlinClock> clock __attribute__((swift_name("clock")));
@property (readonly) id<CalendrumOkioCalendrum_coreUidGenerator> uids __attribute__((swift_name("uids")));
@end


/**
 * A push sink of bytes, the platform-independent counterpart of an output stream.
 *
 * The writer never flushes or closes an output; both are the caller's job.
 */
__attribute__((swift_name("Calendrum_coreByteOutput")))
@protocol CalendrumOkioCalendrum_coreByteOutput
@required

/** Writes [length] bytes of [buffer] starting at [offset]. */
- (void)writeBuffer:(CalendrumOkioKotlinByteArray *)buffer offset:(int32_t)offset length:(int32_t)length __attribute__((swift_name("write(buffer:offset:length:)")));
@end

__attribute__((swift_name("KotlinAppendable")))
@protocol CalendrumOkioKotlinAppendable
@required

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumOkioKotlinAppendable>)appendValue:(unichar)value __attribute__((swift_name("append(value:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumOkioKotlinAppendable>)appendValue_:(id _Nullable)value __attribute__((swift_name("append(value_:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumOkioKotlinAppendable>)appendValue:(id _Nullable)value startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("append(value:startIndex:endIndex:)")));
@end

__attribute__((swift_name("OkioSink")))
@protocol CalendrumOkioOkioSink <CalendrumOkioOkioCloseable>
@required

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)flushAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("flush()")));
- (CalendrumOkioOkioTimeout *)timeout __attribute__((swift_name("timeout()")));

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeSource:(CalendrumOkioOkioBuffer *)source byteCount:(int64_t)byteCount error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(source:byteCount_:)")));
@end

__attribute__((swift_name("OkioBufferedSink")))
@protocol CalendrumOkioOkioBufferedSink <CalendrumOkioOkioSink>
@required
- (id<CalendrumOkioOkioBufferedSink>)emit __attribute__((swift_name("emit()")));
- (id<CalendrumOkioOkioBufferedSink>)emitCompleteSegments __attribute__((swift_name("emitCompleteSegments()")));
- (id<CalendrumOkioOkioBufferedSink>)writeSource:(CalendrumOkioKotlinByteArray *)source __attribute__((swift_name("write(source:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeByteString:(CalendrumOkioOkioByteString *)byteString __attribute__((swift_name("write(byteString:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeSource:(id<CalendrumOkioOkioSource>)source byteCount:(int64_t)byteCount __attribute__((swift_name("write(source:byteCount:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeSource:(CalendrumOkioKotlinByteArray *)source offset:(int32_t)offset byteCount:(int32_t)byteCount __attribute__((swift_name("write(source:offset:byteCount:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeByteString:(CalendrumOkioOkioByteString *)byteString offset:(int32_t)offset byteCount:(int32_t)byteCount __attribute__((swift_name("write(byteString:offset:byteCount:)")));
- (int64_t)writeAllSource:(id<CalendrumOkioOkioSource>)source __attribute__((swift_name("writeAll(source:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeByteB:(int32_t)b __attribute__((swift_name("writeByte(b:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeDecimalLongV:(int64_t)v __attribute__((swift_name("writeDecimalLong(v:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeHexadecimalUnsignedLongV:(int64_t)v __attribute__((swift_name("writeHexadecimalUnsignedLong(v:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeIntI:(int32_t)i __attribute__((swift_name("writeInt(i:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeIntLeI:(int32_t)i __attribute__((swift_name("writeIntLe(i:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeLongV:(int64_t)v __attribute__((swift_name("writeLong(v:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeLongLeV:(int64_t)v __attribute__((swift_name("writeLongLe(v:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeShortS:(int32_t)s __attribute__((swift_name("writeShort(s:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeShortLeS:(int32_t)s __attribute__((swift_name("writeShortLe(s:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeUtf8String:(NSString *)string __attribute__((swift_name("writeUtf8(string:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeUtf8String:(NSString *)string beginIndex:(int32_t)beginIndex endIndex:(int32_t)endIndex __attribute__((swift_name("writeUtf8(string:beginIndex:endIndex:)")));
- (id<CalendrumOkioOkioBufferedSink>)writeUtf8CodePointCodePoint:(int32_t)codePoint __attribute__((swift_name("writeUtf8CodePoint(codePoint:)")));
@property (readonly) CalendrumOkioOkioBuffer *buffer __attribute__((swift_name("buffer")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface CalendrumOkioKotlinArray<T> : CalendrumOkioBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(CalendrumOkioInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<CalendrumOkioKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end


/**
 * A problem found while reading, decoding or checking iCalendar data: a stable [code], a [severity], a position and a
 * short message.
 *
 * Problems are reported instead of thrown wherever the data can be read at all. The text never contains property or
 * parameter values, so a problem can be logged without leaking user data. See [ProblemCode] for every code.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProblem")))
@interface CalendrumOkioCalendrum_coreProblem : CalendrumOkioBase

/**
 * Returns the code, severity, message and position, such as `ICAL-S003 Warning: bare LF used as a line ending at
 * 3:7 (offset 12)`. It never contains values from the input.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The stable code of the problem, such as `ICAL-S003`. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *code __attribute__((swift_name("code")));

/**
 * A short human-readable text: the fixed message of the code, followed by an explanation after a colon when the
 * code has one (for example the name of a detected charset). It never contains data from the input.
 */
@property (readonly) NSString *message __attribute__((swift_name("message")));

/** The severity of [code]. */
@property (readonly) CalendrumOkioCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));

/**
 * Where in the input the problem was found; a point, so [SourceSpan.startOffset] equals [SourceSpan.endOffset].
 *
 * Nodes built by hand or edited by an interceptor have no position in the original input; see [SourceSpan].
 */
@property (readonly) CalendrumOkioCalendrum_coreSourceSpan *span __attribute__((swift_name("span")));
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
__attribute__((swift_name("Calendrum_coreICalProperty")))
@interface CalendrumOkioCalendrum_coreICalProperty : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreICalPropertyCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a property with the same name, parameters and value. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name, the parameters and the value. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the first parameter named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a parameter name such as `TZID`.
 */
- (CalendrumOkioCalendrum_coreICalParameter * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/** Returns the name and the number of parameters; never the value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The canonical name in upper case, such as `DTSTART`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The parameters in source order. The list is empty for none. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));

/**
 * Where the property is in the input, or `null` for a property that was built by hand. For input that was changed
 * by an interceptor it is the position of the original line.
 */
@property (readonly) CalendrumOkioCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));

/**
 * The value as written after the colon, with folding removed. It is not unescaped; an empty string for no value.
 */
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end

__attribute__((swift_name("KotlinByteIterator")))
@interface CalendrumOkioKotlinByteIterator : CalendrumOkioBase <CalendrumOkioKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumOkioByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end


/** A declared field of a schema, to read its value from [Values] in [IcsSchema.create]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreIcsSchemaField")))
@interface CalendrumOkioCalendrum_coreIcsSchemaField<R> : CalendrumOkioBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** The decoded values of one component, handed to [IcsSchema.create]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreIcsSchemaValues")))
@interface CalendrumOkioCalendrum_coreIcsSchemaValues : CalendrumOkioBase

/**
 * Returns the value of [field]: a malformed or missing optional value as `null`, a list field as a list (empty
 * if there is no property).
 *
 * @param field a field of the schema that decodes these values.
 * @throws IllegalArgumentException if [field] belongs to another schema.
 */
- (id _Nullable)getField:(CalendrumOkioCalendrum_coreIcsSchemaField<id> *)field __attribute__((swift_name("get(field:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
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
__attribute__((swift_name("Calendrum_corePropertyKey")))
@interface CalendrumOkioCalendrum_corePropertyKey<T> : CalendrumOkioBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumOkioCalendrum_corePropertyCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumOkioCalendrum_corePropertyCodec> codec __attribute__((swift_name("codec")));

/** The canonical property name in upper case. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
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
__attribute__((swift_name("Calendrum_coreProblemCode")))
@interface CalendrumOkioCalendrum_coreProblemCode : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreProblemCodeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a code with the same [id]. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the [id]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the [id], such as `ICAL-S003`. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The identifier of the code, for example `ICAL-S003`. */
@property (readonly) NSString *id __attribute__((swift_name("id")));

/** How serious the problems with this code are. */
@property (readonly) CalendrumOkioCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));
@end


/** Creates builders for [ICalComponent]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalComponent.Companion")))
@interface CalendrumOkioCalendrum_coreICalComponentCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [ICalComponent]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreICalComponentCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a component named [name].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @throws IllegalArgumentException if [name] is empty or has another character.
 */
- (CalendrumOkioCalendrum_coreICalComponentBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));
@end


/**
 * A position in the input: byte offsets from its start, a line and a column.
 *
 * Offsets count bytes of the input, from 0; for input that was transcoded they count bytes of the decoded UTF-8 text.
 * The line and the column both start at 1, and the column is counted in bytes from the start of the physical line.
 * Nodes built by hand have no input: their span is all zeros, and a value of a hand-built property has a column of 0.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSourceSpan")))
@interface CalendrumOkioCalendrum_coreSourceSpan : CalendrumOkioBase

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
__attribute__((swift_name("Calendrum_coreComponentKey")))
@interface CalendrumOkioCalendrum_coreComponentKey : CalendrumOkioBase

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

__attribute__((swift_name("KotlinComparable")))
@protocol CalendrumOkioKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface CalendrumOkioKotlinEnum<E> : CalendrumOkioBase <CalendrumOkioKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumOkioKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end


/** The kind of event an [ICalReader] reports. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreContentEventType")))
@interface CalendrumOkioCalendrum_coreContentEventType : CalendrumOkioKotlinEnum<CalendrumOkioCalendrum_coreContentEventType *>
+ (instancetype)alloc __attribute__((unavailable));

/** The kind of event an [ICalReader] reports. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioCalendrum_coreContentEventType *begin __attribute__((swift_name("begin")));
@property (class, readonly) CalendrumOkioCalendrum_coreContentEventType *property __attribute__((swift_name("property")));
@property (class, readonly) CalendrumOkioCalendrum_coreContentEventType *end __attribute__((swift_name("end")));
+ (CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreContentEventType *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioCalendrum_coreContentEventType *> *entries __attribute__((swift_name("entries")));
@end


/** The shared defaults and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseOptions.Companion")))
@interface CalendrumOkioCalendrum_coreParseOptionsCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The shared defaults and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreParseOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the defaults. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** Lenient mode, the default limits, a retained source and no listener, hint, interceptors or selection. */
@property (readonly) CalendrumOkioCalendrum_coreParseOptions *Default __attribute__((swift_name("Default")));

/**
 * Options for files from strangers: lenient, but with nesting depth 8, lines up to 64 KiB, 1,000,000 lines,
 * 100,000 components, input up to 64 MiB and a time budget of 2 seconds.
 */
@property (readonly) CalendrumOkioCalendrum_coreParseOptions *Untrusted __attribute__((swift_name("Untrusted")));
@end


/**
 * Looks at each property line while it is read or written, and may change or drop it.
 *
 * An interceptor fixes a feed on the fly: nothing is allocated per line until an edit is made, and lines it leaves
 * alone stay byte for byte as they were. Register it with `ParseOptions.Builder.intercept` or
 * `WriteOptions.Builder.intercept`. Interceptors run in the order they were added; each sees the line as the previous
 * one left it. An edited line is written canonically.
 */
__attribute__((swift_name("Calendrum_coreLineInterceptor")))
@protocol CalendrumOkioCalendrum_coreLineInterceptor
@required

/**
 * Called once for every property line.
 *
 * The [line] is valid only during this call; do not keep it. The function must not throw for data reasons.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (void)interceptLine:(CalendrumOkioCalendrum_coreInterceptedLine *)line __attribute__((swift_name("intercept(line:)")));
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
__attribute__((swift_name("Calendrum_coreParseLimits")))
@interface CalendrumOkioCalendrum_coreParseLimits : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreParseLimitsCompanion *companion __attribute__((swift_name("companion")));

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


/** How a parser reacts to problems in the input. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseMode")))
@interface CalendrumOkioCalendrum_coreParseMode : CalendrumOkioKotlinEnum<CalendrumOkioCalendrum_coreParseMode *>
+ (instancetype)alloc __attribute__((unavailable));

/** How a parser reacts to problems in the input. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioCalendrum_coreParseMode *lenient __attribute__((swift_name("lenient")));
@property (class, readonly) CalendrumOkioCalendrum_coreParseMode *strict __attribute__((swift_name("strict")));
+ (CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreParseMode *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioCalendrum_coreParseMode *> *entries __attribute__((swift_name("entries")));
@end


/**
 * Receives problems as a reader or an expander finds them.
 *
 * The listener is called on the thread that does the work, in the order the problems are found. It must not throw: an
 * exception thrown by a listener propagates out of the call that was running.
 */
__attribute__((swift_name("Calendrum_coreProblemListener")))
@protocol CalendrumOkioCalendrum_coreProblemListener
@required

/** Called once for every reported problem. */
- (void)onProblemProblem:(CalendrumOkioCalendrum_coreProblem *)problem __attribute__((swift_name("onProblem(problem:)")));
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
__attribute__((swift_name("Calendrum_coreICalRegistry")))
@interface CalendrumOkioCalendrum_coreICalRegistry : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreICalRegistryCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the definition of the component [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumOkioCalendrum_coreComponentDefinition * _Nullable)componentName:(NSString *)name __attribute__((swift_name("component(name:)")));

/**
 * Returns the definition of the parameter [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumOkioCalendrum_coreParameterDefinition * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/**
 * Returns the definition of the property [name], ignoring ASCII case, or `null` when the registry does not know it.
 * When several components define the property differently, the first definition is returned.
 */
- (CalendrumOkioCalendrum_corePropertyDefinition * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalExtension>> *extensions __attribute__((swift_name("extensions")));
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
__attribute__((swift_name("Calendrum_coreSelection")))
@interface CalendrumOkioCalendrum_coreSelection : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreSelectionCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreTimeZoneSelection *timeZones __attribute__((swift_name("timeZones")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeSource")))
@protocol CalendrumOkioKotlinTimeSource
@required
- (id<CalendrumOkioKotlinTimeMark>)markNow __attribute__((swift_name("markNow()")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeSourceWithComparableMarks")))
@protocol CalendrumOkioKotlinTimeSourceWithComparableMarks <CalendrumOkioKotlinTimeSource>
@required
@end

__attribute__((swift_name("OkioByteString")))
@interface CalendrumOkioOkioByteString : CalendrumOkioBase <CalendrumOkioKotlinComparable>
@property (class, readonly, getter=companion) CalendrumOkioOkioByteStringCompanion *companion __attribute__((swift_name("companion")));
- (NSString *)base64 __attribute__((swift_name("base64()")));
- (NSString *)base64Url __attribute__((swift_name("base64Url()")));
- (int32_t)compareToOther:(CalendrumOkioOkioByteString *)other __attribute__((swift_name("compareTo(other:)")));
- (void)doCopyIntoOffset:(int32_t)offset target:(CalendrumOkioKotlinByteArray *)target targetOffset:(int32_t)targetOffset byteCount:(int32_t)byteCount __attribute__((swift_name("doCopyInto(offset:target:targetOffset:byteCount:)")));
- (BOOL)endsWithSuffix:(CalendrumOkioKotlinByteArray *)suffix __attribute__((swift_name("endsWith(suffix:)")));
- (BOOL)endsWithSuffix_:(CalendrumOkioOkioByteString *)suffix __attribute__((swift_name("endsWith(suffix_:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)hex __attribute__((swift_name("hex()")));
- (CalendrumOkioOkioByteString *)hmacSha1Key:(CalendrumOkioOkioByteString *)key __attribute__((swift_name("hmacSha1(key:)")));
- (CalendrumOkioOkioByteString *)hmacSha256Key:(CalendrumOkioOkioByteString *)key __attribute__((swift_name("hmacSha256(key:)")));
- (CalendrumOkioOkioByteString *)hmacSha512Key:(CalendrumOkioOkioByteString *)key __attribute__((swift_name("hmacSha512(key:)")));
- (int32_t)indexOfOther:(CalendrumOkioKotlinByteArray *)other fromIndex:(int32_t)fromIndex __attribute__((swift_name("indexOf(other:fromIndex:)")));
- (int32_t)indexOfOther:(CalendrumOkioOkioByteString *)other fromIndex_:(int32_t)fromIndex __attribute__((swift_name("indexOf(other:fromIndex_:)")));
- (int32_t)lastIndexOfOther:(CalendrumOkioKotlinByteArray *)other fromIndex:(int32_t)fromIndex __attribute__((swift_name("lastIndexOf(other:fromIndex:)")));
- (int32_t)lastIndexOfOther:(CalendrumOkioOkioByteString *)other fromIndex_:(int32_t)fromIndex __attribute__((swift_name("lastIndexOf(other:fromIndex_:)")));
- (CalendrumOkioOkioByteString *)md5 __attribute__((swift_name("md5()")));
- (BOOL)rangeEqualsOffset:(int32_t)offset other:(CalendrumOkioKotlinByteArray *)other otherOffset:(int32_t)otherOffset byteCount:(int32_t)byteCount __attribute__((swift_name("rangeEquals(offset:other:otherOffset:byteCount:)")));
- (BOOL)rangeEqualsOffset:(int32_t)offset other:(CalendrumOkioOkioByteString *)other otherOffset:(int32_t)otherOffset byteCount_:(int32_t)byteCount __attribute__((swift_name("rangeEquals(offset:other:otherOffset:byteCount_:)")));
- (CalendrumOkioOkioByteString *)sha1 __attribute__((swift_name("sha1()")));
- (CalendrumOkioOkioByteString *)sha256 __attribute__((swift_name("sha256()")));
- (CalendrumOkioOkioByteString *)sha512 __attribute__((swift_name("sha512()")));
- (BOOL)startsWithPrefix:(CalendrumOkioKotlinByteArray *)prefix __attribute__((swift_name("startsWith(prefix:)")));
- (BOOL)startsWithPrefix_:(CalendrumOkioOkioByteString *)prefix __attribute__((swift_name("startsWith(prefix_:)")));
- (CalendrumOkioOkioByteString *)substringBeginIndex:(int32_t)beginIndex endIndex:(int32_t)endIndex __attribute__((swift_name("substring(beginIndex:endIndex:)")));
- (CalendrumOkioOkioByteString *)toAsciiLowercase __attribute__((swift_name("toAsciiLowercase()")));
- (CalendrumOkioOkioByteString *)toAsciiUppercase __attribute__((swift_name("toAsciiUppercase()")));
- (CalendrumOkioKotlinByteArray *)toByteArray __attribute__((swift_name("toByteArray()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (NSString *)utf8 __attribute__((swift_name("utf8()")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OkioBuffer")))
@interface CalendrumOkioOkioBuffer : CalendrumOkioBase <CalendrumOkioOkioBufferedSource, CalendrumOkioOkioBufferedSink>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)clear __attribute__((swift_name("clear()")));

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)closeAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("close()")));
- (int64_t)completeSegmentByteCount __attribute__((swift_name("completeSegmentByteCount()")));
- (CalendrumOkioOkioBuffer *)doCopy __attribute__((swift_name("doCopy()")));
- (CalendrumOkioOkioBuffer *)doCopyToOut:(CalendrumOkioOkioBuffer *)out offset:(int64_t)offset __attribute__((swift_name("doCopyTo(out:offset:)")));
- (CalendrumOkioOkioBuffer *)doCopyToOut:(CalendrumOkioOkioBuffer *)out offset:(int64_t)offset byteCount:(int64_t)byteCount __attribute__((swift_name("doCopyTo(out:offset:byteCount:)")));
- (CalendrumOkioOkioBuffer *)emit __attribute__((swift_name("emit()")));
- (CalendrumOkioOkioBuffer *)emitCompleteSegments __attribute__((swift_name("emitCompleteSegments()")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (BOOL)exhausted __attribute__((swift_name("exhausted()")));

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)flushAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("flush()")));
- (int8_t)getPos:(int64_t)pos __attribute__((swift_name("get(pos:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumOkioOkioByteString *)hmacSha1Key:(CalendrumOkioOkioByteString *)key __attribute__((swift_name("hmacSha1(key:)")));
- (CalendrumOkioOkioByteString *)hmacSha256Key:(CalendrumOkioOkioByteString *)key __attribute__((swift_name("hmacSha256(key:)")));
- (CalendrumOkioOkioByteString *)hmacSha512Key:(CalendrumOkioOkioByteString *)key __attribute__((swift_name("hmacSha512(key:)")));
- (int64_t)indexOfB:(int8_t)b __attribute__((swift_name("indexOf(b:)")));
- (int64_t)indexOfBytes:(CalendrumOkioOkioByteString *)bytes __attribute__((swift_name("indexOf(bytes:)")));
- (int64_t)indexOfB:(int8_t)b fromIndex:(int64_t)fromIndex __attribute__((swift_name("indexOf(b:fromIndex:)")));
- (int64_t)indexOfBytes:(CalendrumOkioOkioByteString *)bytes fromIndex:(int64_t)fromIndex __attribute__((swift_name("indexOf(bytes:fromIndex:)")));
- (int64_t)indexOfB:(int8_t)b fromIndex:(int64_t)fromIndex toIndex:(int64_t)toIndex __attribute__((swift_name("indexOf(b:fromIndex:toIndex:)")));
- (int64_t)indexOfElementTargetBytes:(CalendrumOkioOkioByteString *)targetBytes __attribute__((swift_name("indexOfElement(targetBytes:)")));
- (int64_t)indexOfElementTargetBytes:(CalendrumOkioOkioByteString *)targetBytes fromIndex:(int64_t)fromIndex __attribute__((swift_name("indexOfElement(targetBytes:fromIndex:)")));
- (CalendrumOkioOkioByteString *)md5 __attribute__((swift_name("md5()")));
- (id<CalendrumOkioOkioBufferedSource>)peek __attribute__((swift_name("peek()")));
- (BOOL)rangeEqualsOffset:(int64_t)offset bytes:(CalendrumOkioOkioByteString *)bytes __attribute__((swift_name("rangeEquals(offset:bytes:)")));
- (BOOL)rangeEqualsOffset:(int64_t)offset bytes:(CalendrumOkioOkioByteString *)bytes bytesOffset:(int32_t)bytesOffset byteCount:(int32_t)byteCount __attribute__((swift_name("rangeEquals(offset:bytes:bytesOffset:byteCount:)")));
- (int32_t)readSink:(CalendrumOkioKotlinByteArray *)sink __attribute__((swift_name("read(sink:)")));

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (int64_t)readSink:(CalendrumOkioOkioBuffer *)sink byteCount:(int64_t)byteCount error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("read(sink:byteCount:)"))) __attribute__((swift_error(nonnull_error)));
- (int32_t)readSink:(CalendrumOkioKotlinByteArray *)sink offset:(int32_t)offset byteCount:(int32_t)byteCount __attribute__((swift_name("read(sink:offset:byteCount:)")));
- (int64_t)readAllSink:(id<CalendrumOkioOkioSink>)sink __attribute__((swift_name("readAll(sink:)")));
- (CalendrumOkioOkioBufferUnsafeCursor *)readAndWriteUnsafeUnsafeCursor:(CalendrumOkioOkioBufferUnsafeCursor *)unsafeCursor __attribute__((swift_name("readAndWriteUnsafe(unsafeCursor:)")));
- (int8_t)readByte __attribute__((swift_name("readByte()")));
- (CalendrumOkioKotlinByteArray *)readByteArray __attribute__((swift_name("readByteArray()")));
- (CalendrumOkioKotlinByteArray *)readByteArrayByteCount:(int64_t)byteCount __attribute__((swift_name("readByteArray(byteCount:)")));
- (CalendrumOkioOkioByteString *)readByteString __attribute__((swift_name("readByteString()")));
- (CalendrumOkioOkioByteString *)readByteStringByteCount:(int64_t)byteCount __attribute__((swift_name("readByteString(byteCount:)")));
- (int64_t)readDecimalLong __attribute__((swift_name("readDecimalLong()")));
- (void)readFullySink:(CalendrumOkioKotlinByteArray *)sink __attribute__((swift_name("readFully(sink:)")));
- (void)readFullySink:(CalendrumOkioOkioBuffer *)sink byteCount:(int64_t)byteCount __attribute__((swift_name("readFully(sink:byteCount:)")));
- (int64_t)readHexadecimalUnsignedLong __attribute__((swift_name("readHexadecimalUnsignedLong()")));
- (int32_t)readInt __attribute__((swift_name("readInt()")));
- (int32_t)readIntLe __attribute__((swift_name("readIntLe()")));
- (int64_t)readLong __attribute__((swift_name("readLong()")));
- (int64_t)readLongLe __attribute__((swift_name("readLongLe()")));
- (int16_t)readShort __attribute__((swift_name("readShort()")));
- (int16_t)readShortLe __attribute__((swift_name("readShortLe()")));
- (CalendrumOkioOkioBufferUnsafeCursor *)readUnsafeUnsafeCursor:(CalendrumOkioOkioBufferUnsafeCursor *)unsafeCursor __attribute__((swift_name("readUnsafe(unsafeCursor:)")));
- (NSString *)readUtf8 __attribute__((swift_name("readUtf8()")));
- (NSString *)readUtf8ByteCount:(int64_t)byteCount __attribute__((swift_name("readUtf8(byteCount:)")));
- (int32_t)readUtf8CodePoint __attribute__((swift_name("readUtf8CodePoint()")));
- (NSString * _Nullable)readUtf8Line __attribute__((swift_name("readUtf8Line()")));
- (NSString *)readUtf8LineStrict __attribute__((swift_name("readUtf8LineStrict()")));
- (NSString *)readUtf8LineStrictLimit:(int64_t)limit __attribute__((swift_name("readUtf8LineStrict(limit:)")));
- (BOOL)requestByteCount:(int64_t)byteCount __attribute__((swift_name("request(byteCount:)")));
- (void)requireByteCount:(int64_t)byteCount __attribute__((swift_name("require(byteCount:)")));
- (int32_t)selectOptions:(NSArray<CalendrumOkioOkioByteString *> *)options __attribute__((swift_name("select(options:)")));
- (CalendrumOkioOkioByteString *)sha1 __attribute__((swift_name("sha1()")));
- (CalendrumOkioOkioByteString *)sha256 __attribute__((swift_name("sha256()")));
- (CalendrumOkioOkioByteString *)sha512 __attribute__((swift_name("sha512()")));
- (void)skipByteCount:(int64_t)byteCount __attribute__((swift_name("skip(byteCount:)")));
- (CalendrumOkioOkioByteString *)snapshot __attribute__((swift_name("snapshot()")));
- (CalendrumOkioOkioByteString *)snapshotByteCount:(int32_t)byteCount __attribute__((swift_name("snapshot(byteCount:)")));
- (CalendrumOkioOkioTimeout *)timeout __attribute__((swift_name("timeout()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (CalendrumOkioOkioBuffer *)writeSource:(CalendrumOkioKotlinByteArray *)source __attribute__((swift_name("write(source:)")));
- (CalendrumOkioOkioBuffer *)writeByteString:(CalendrumOkioOkioByteString *)byteString __attribute__((swift_name("write(byteString:)")));

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeSource:(CalendrumOkioOkioBuffer *)source byteCount:(int64_t)byteCount error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(source:byteCount_:)")));
- (CalendrumOkioOkioBuffer *)writeSource:(id<CalendrumOkioOkioSource>)source byteCount:(int64_t)byteCount __attribute__((swift_name("write(source:byteCount:)")));
- (CalendrumOkioOkioBuffer *)writeSource:(CalendrumOkioKotlinByteArray *)source offset:(int32_t)offset byteCount:(int32_t)byteCount __attribute__((swift_name("write(source:offset:byteCount:)")));
- (CalendrumOkioOkioBuffer *)writeByteString:(CalendrumOkioOkioByteString *)byteString offset:(int32_t)offset byteCount:(int32_t)byteCount __attribute__((swift_name("write(byteString:offset:byteCount:)")));
- (int64_t)writeAllSource:(id<CalendrumOkioOkioSource>)source __attribute__((swift_name("writeAll(source:)")));
- (CalendrumOkioOkioBuffer *)writeByteB:(int32_t)b __attribute__((swift_name("writeByte(b:)")));
- (CalendrumOkioOkioBuffer *)writeDecimalLongV:(int64_t)v __attribute__((swift_name("writeDecimalLong(v:)")));
- (CalendrumOkioOkioBuffer *)writeHexadecimalUnsignedLongV:(int64_t)v __attribute__((swift_name("writeHexadecimalUnsignedLong(v:)")));
- (CalendrumOkioOkioBuffer *)writeIntI:(int32_t)i __attribute__((swift_name("writeInt(i:)")));
- (CalendrumOkioOkioBuffer *)writeIntLeI:(int32_t)i __attribute__((swift_name("writeIntLe(i:)")));
- (CalendrumOkioOkioBuffer *)writeLongV:(int64_t)v __attribute__((swift_name("writeLong(v:)")));
- (CalendrumOkioOkioBuffer *)writeLongLeV:(int64_t)v __attribute__((swift_name("writeLongLe(v:)")));
- (CalendrumOkioOkioBuffer *)writeShortS:(int32_t)s __attribute__((swift_name("writeShort(s:)")));
- (CalendrumOkioOkioBuffer *)writeShortLeS:(int32_t)s __attribute__((swift_name("writeShortLe(s:)")));
- (CalendrumOkioOkioBuffer *)writeUtf8String:(NSString *)string __attribute__((swift_name("writeUtf8(string:)")));
- (CalendrumOkioOkioBuffer *)writeUtf8String:(NSString *)string beginIndex:(int32_t)beginIndex endIndex:(int32_t)endIndex __attribute__((swift_name("writeUtf8(string:beginIndex:endIndex:)")));
- (CalendrumOkioOkioBuffer *)writeUtf8CodePointCodePoint:(int32_t)codePoint __attribute__((swift_name("writeUtf8CodePoint(codePoint:)")));
@property (readonly) CalendrumOkioOkioBuffer *buffer __attribute__((swift_name("buffer")));
@property (readonly) int64_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("OkioTimeout")))
@interface CalendrumOkioOkioTimeout : CalendrumOkioBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property (class, readonly, getter=companion) CalendrumOkioOkioTimeoutCompanion *companion __attribute__((swift_name("companion")));
@end


/** Creates VCalendar values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVCalendar.Companion")))
@interface CalendrumOkioCalendrum_coreVCalendarCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VCalendar values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVCalendarCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVCalendarBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VCalendar, or returns `null` when it is not a `VCALENDAR` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVCalendar * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreVEvent")))
@interface CalendrumOkioCalendrum_coreVEvent : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVEventCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this event changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this event.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVEvent *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVEventBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VEvent with an equal VEVENT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this event; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this event and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the event, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the event (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the event (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumOkioCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreConference *> *conferences __attribute__((swift_name("conferences")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) CalendrumOkioCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event (`DTEND`, [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The excluded dates of the event (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the event (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the event (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the event (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the event (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/**
 * The location text of the event (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the event, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the event, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the event (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the event (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the event (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the event, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the event (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the event (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@property (readonly) CalendrumOkioCalendrum_coreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

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
__attribute__((swift_name("Calendrum_coreVFreeBusy")))
@interface CalendrumOkioCalendrum_coreVFreeBusy : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVFreeBusyCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this free/busy component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this free/busy component.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVFreeBusy *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVFreeBusyBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VFreeBusy with an equal VFREEBUSY component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this free/busy component; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this free/busy component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attendees of the free/busy component (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The comments of the free/busy component (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The free/busy component nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The links of the free/busy component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the free/busy component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The styled descriptions of the free/busy component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
__attribute__((swift_name("Calendrum_coreImage")))
@interface CalendrumOkioCalendrum_coreImage : CalendrumOkioBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumOkioKotlinByteArray *)bytes formatType:(NSString *)formatType __attribute__((swift_name("init(bytes:formatType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri formatType:(NSString *)formatType __attribute__((swift_name("init(uri:formatType:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the inline content, or `null` when the image is given by a URI. */
- (CalendrumOkioKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a Image equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreImageDisplay *> *display __attribute__((swift_name("display")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the image is inline (base64 encoded in the text form) and not given by a URI. */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable uri __attribute__((swift_name("uri")));
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
__attribute__((swift_name("Calendrum_coreVJournal")))
@interface CalendrumOkioCalendrum_coreVJournal : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVJournalCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this journal entry changed by [block]. Properties and components that [block] does not touch
 * are the same objects as in this journal entry.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVJournal *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVJournalBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VJournal with an equal VJOURNAL component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this journal entry; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this journal entry and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the journal entry (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the journal entry (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumOkioCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The images of the journal entry (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the journal entry (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the journal entry (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The additional dates of the journal entry (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the journal entry (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the journal entry (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreJournalStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the journal entry (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the journal entry (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
__attribute__((swift_name("Calendrum_coreICalDateTime")))
@protocol CalendrumOkioCalendrum_coreICalDateTime
@required
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
__attribute__((swift_name("Calendrum_coreMethod")))
@interface CalendrumOkioCalendrum_coreMethod : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreMethodCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Method with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
__attribute__((swift_name("Calendrum_coreICalDuration")))
@interface CalendrumOkioCalendrum_coreICalDuration : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreICalDurationCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a duration with the same sign, amounts and written units. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the sign, the amounts and the written units. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the duration as a kotlinx-datetime period with the sign applied to every unit; weeks become days (7
 * each).
 */
- (CalendrumOkioKotlinx_datetimeDateTimePeriod *)toDateTimePeriod __attribute__((swift_name("toDateTimePeriod()")));

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
__attribute__((swift_name("Calendrum_coreVTimeZone")))
@interface CalendrumOkioCalendrum_coreVTimeZone : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVTimeZoneCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this time zone changed by [block]. Properties and components that [block] does not touch are
 * the same objects as in this time zone.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVTimeZone *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVTimeZoneBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTimeZone with an equal VTIMEZONE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this time zone; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this time zone and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the time zone (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The `STANDARD` and `DAYLIGHT` observances in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

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
__attribute__((swift_name("Calendrum_coreVTodo")))
@interface CalendrumOkioCalendrum_coreVTodo : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVTodoCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this to-do changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this to-do.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVTodo *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVTodoBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTodo with an equal VTODO component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this to-do; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this to-do and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the to-do (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the to-do (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumOkioCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreConference *> *conferences __attribute__((swift_name("conferences")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The excluded dates of the to-do (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the to-do (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the to-do (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the to-do (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the to-do (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The location nested in the to-do, in source order. */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the to-do (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the to-do (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the to-do (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreTodoStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the to-do (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the to-do (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
 * Collects the properties and nested components of a new or edited event, with a Kotlin DSL and chained calls. Get
 * one from [VEvent.builder] or [VEvent.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVEvent.Builder")))
@interface CalendrumOkioCalendrum_coreVEventBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVEventBuilder *>

/** Adds [alarm] as a alarm of the event. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)alarmAlarm:(CalendrumOkioCalendrum_coreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)alarmBlock:(void (^)(CalendrumOkioCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the event in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)alarmsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)attachAttach:(id<CalendrumOkioCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)attendeeAttendee:(CalendrumOkioCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)attendeesBlock:(void (^)(CalendrumOkioCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the event.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults to generate
 *   them.
 * @throws IllegalStateException if `UNTIL` of the rule does not suit the type of `DTSTART`.
 */
- (CalendrumOkioCalendrum_coreVEvent *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)categoriesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)classificationValue:(CalendrumOkioCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)conferenceValue:(CalendrumOkioCalendrum_coreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)createdValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)durationValue:(CalendrumOkioCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)endValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)exdateDates:(CalendrumOkioKotlinArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)geoValue:(CalendrumOkioCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)imageValue:(CalendrumOkioCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)lastModifiedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the event. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)locationValue:(CalendrumOkioCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)locationBlock:(void (^)(CalendrumOkioCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the event in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)locationsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)organizerValue:(CalendrumOkioCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the event. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)participantValue:(CalendrumOkioCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)participantBlock:(void (^)(CalendrumOkioCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the event in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)participantsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)priorityValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)rdateDates:(CalendrumOkioKotlinArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)rdatePeriods:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)recurrenceIdValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)requestStatusStatus:(CalendrumOkioCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the event. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)resourceComponentValue:(CalendrumOkioCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)resourceComponentBlock:(void (^)(CalendrumOkioCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the event in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)resourceComponentsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)resourcesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)rruleValue:(CalendrumOkioCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)rruleFrequency:(CalendrumOkioCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumOkioCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)sequenceValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)stampValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)startValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)statusValue:(CalendrumOkioCalendrum_coreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)structuredDataValue:(CalendrumOkioCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumOkioCalendrum_coreVEventBuilder *)styledDescriptionValue:(CalendrumOkioCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [transparency] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)transparencyValue:(CalendrumOkioCalendrum_coreTransparency * _Nullable)value __attribute__((swift_name("transparency(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the event being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the event being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property CalendrumOkioCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event being built (`DTEND`,
 * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The geographic position of the event being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the event being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the event being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the event being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the event being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the event being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the event being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the event being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

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
@property CalendrumOkioCalendrum_coreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

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


/**
 * An editable view of the nested components of one kind in a builder, such as the alarms of an event.
 *
 * @property items the components now in the list.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreComponentList")))
@interface CalendrumOkioCalendrum_coreComponentList<T> : CalendrumOkioBase

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
 * Collects the properties and nested components of a new or edited free/busy component, with a Kotlin DSL and
 * chained calls. Get one from [VFreeBusy.builder] or [VFreeBusy.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVFreeBusy.Builder")))
@interface CalendrumOkioCalendrum_coreVFreeBusyBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVFreeBusyBuilder *>

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)attendeeAttendee:(CalendrumOkioCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)attendeesBlock:(void (^)(CalendrumOkioCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the free/busy component.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults.
 */
- (CalendrumOkioCalendrum_coreVFreeBusy *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [contact] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)contactValue:(NSString * _Nullable)value __attribute__((swift_name("contact(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)endValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds [freeBusy] as a free/busy component of the free/busy component. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)freeBusyFreeBusy:(CalendrumOkioCalendrum_coreFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/** Adds [periods] as a free/busy component of the free/busy component. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)freeBusyPeriods:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("freeBusy(periods:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the free/busy component. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)locationValue:(CalendrumOkioCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)locationBlock:(void (^)(CalendrumOkioCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)locationsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)organizerValue:(CalendrumOkioCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the free/busy component. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)participantValue:(CalendrumOkioCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)participantBlock:(void (^)(CalendrumOkioCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)participantsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)requestStatusStatus:(CalendrumOkioCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the free/busy component. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)resourceComponentValue:(CalendrumOkioCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)resourceComponentBlock:(void (^)(CalendrumOkioCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)resourceComponentsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)stampValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)startValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)styledDescriptionValue:(CalendrumOkioCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The locations of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/** The resources of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

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


/**
 * Collects the properties and nested components of a new or edited journal entry, with a Kotlin DSL and chained
 * calls. Get one from [VJournal.builder] or [VJournal.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVJournal.Builder")))
@interface CalendrumOkioCalendrum_coreVJournalBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVJournalBuilder *>

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)attachAttach:(id<CalendrumOkioCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)attendeeAttendee:(CalendrumOkioCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)attendeesBlock:(void (^)(CalendrumOkioCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the journal entry.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumOkioCalendrum_coreVJournal *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)categoriesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)classificationValue:(CalendrumOkioCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)createdValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)exdateDates:(CalendrumOkioKotlinArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)imageValue:(CalendrumOkioCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)lastModifiedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the journal entry. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)locationValue:(CalendrumOkioCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)locationBlock:(void (^)(CalendrumOkioCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the journal entry in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)locationsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)organizerValue:(CalendrumOkioCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the journal entry. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)participantValue:(CalendrumOkioCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)participantBlock:(void (^)(CalendrumOkioCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the journal entry in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)participantsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)rdateDates:(CalendrumOkioKotlinArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)rdatePeriods:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)recurrenceIdValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)requestStatusStatus:(CalendrumOkioCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the journal entry. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)resourceComponentValue:(CalendrumOkioCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)resourceComponentBlock:(void (^)(CalendrumOkioCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the journal entry in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)resourceComponentsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)rruleValue:(CalendrumOkioCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)rruleFrequency:(CalendrumOkioCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumOkioCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)sequenceValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)stampValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)startValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)statusValue:(CalendrumOkioCalendrum_coreJournalStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)structuredDataValue:(CalendrumOkioCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)styledDescriptionValue:(CalendrumOkioCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the journal entry being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The time of the last modification of the journal entry being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the journal entry being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the journal entry being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The recurrence identifier of the journal entry being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the journal entry being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreJournalStatus * _Nullable status __attribute__((swift_name("status")));

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


/**
 * Collects the properties and nested components of a new or edited time zone, with a Kotlin DSL and chained calls.
 * Get one from [VTimeZone.builder] or [VTimeZone.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTimeZone.Builder")))
@interface CalendrumOkioCalendrum_coreVTimeZoneBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVTimeZoneBuilder *>

/** Creates the time zone. */
- (CalendrumOkioCalendrum_coreVTimeZone *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `DAYLIGHT` observance that [block] sets up. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)daylightBlock:(void (^)(CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("daylight(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)lastModifiedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [observance] to the time zone. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)observanceObservance:(CalendrumOkioCalendrum_coreVTimeZoneObservance *)observance __attribute__((swift_name("observance(observance:)")));

/** Edits the observances in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)observancesBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVTimeZoneObservance *> *))block __attribute__((swift_name("observances(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds a `STANDARD` observance that [block] sets up. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)standardBlock:(void (^)(CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("standard(block:)")));

/**
 * Sets [tzUrl] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)tzUrlValue:(NSString * _Nullable)value __attribute__((swift_name("tzUrl(value:)")));

/**
 * Sets [tzid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)tzidValue:(NSString * _Nullable)value __attribute__((swift_name("tzid(value:)")));

/**
 * The time of the last modification of the time zone being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The observances of the time zone being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

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


/**
 * Collects the properties and nested components of a new or edited to-do, with a Kotlin DSL and chained calls. Get
 * one from [VTodo.builder] or [VTodo.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTodo.Builder")))
@interface CalendrumOkioCalendrum_coreVTodoBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVTodoBuilder *>

/** Adds [alarm] as a alarm of the to-do. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)alarmAlarm:(CalendrumOkioCalendrum_coreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)alarmBlock:(void (^)(CalendrumOkioCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the to-do in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)alarmsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)attachAttach:(id<CalendrumOkioCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)attendeeAttendee:(CalendrumOkioCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)attendeesBlock:(void (^)(CalendrumOkioCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the to-do.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumOkioCalendrum_coreVTodo *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)categoriesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)classificationValue:(CalendrumOkioCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/**
 * Sets [completed] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)completedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("completed(value:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)conferenceValue:(CalendrumOkioCalendrum_coreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)createdValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [due] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)dueValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("due(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)durationValue:(CalendrumOkioCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)exdateDates:(CalendrumOkioKotlinArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)geoValue:(CalendrumOkioCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)imageValue:(CalendrumOkioCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)lastModifiedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the to-do. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)locationValue:(CalendrumOkioCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)locationBlock:(void (^)(CalendrumOkioCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the to-do in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)locationsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)organizerValue:(CalendrumOkioCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the to-do. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)participantValue:(CalendrumOkioCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)participantBlock:(void (^)(CalendrumOkioCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the to-do in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)participantsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [percentComplete] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)percentCompleteValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("percentComplete(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)priorityValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)rdateDates:(CalendrumOkioKotlinArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)rdatePeriods:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)recurrenceIdValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)requestStatusStatus:(CalendrumOkioCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the to-do. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)resourceComponentValue:(CalendrumOkioCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)resourceComponentBlock:(void (^)(CalendrumOkioCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the to-do in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)resourceComponentsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)resourcesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)rruleValue:(CalendrumOkioCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)rruleFrequency:(CalendrumOkioCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumOkioCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)sequenceValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)stampValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)startValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)statusValue:(CalendrumOkioCalendrum_coreTodoStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)structuredDataValue:(CalendrumOkioCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)styledDescriptionValue:(CalendrumOkioCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the to-do being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the to-do being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));

/**
 * The creation time of the to-do being built (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do being built (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The geographic position of the to-do being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the to-do being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the to-do being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the to-do being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the to-do being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do being built (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the to-do being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the to-do being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the to-do being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreTodoStatus * _Nullable status __attribute__((swift_name("status")));

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


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="2.3")
*/
__attribute__((swift_name("KotlinClock")))
@protocol CalendrumOkioKotlinClock
@required
- (CalendrumOkioKotlinInstant *)now __attribute__((swift_name("now()")));
@end


/** Makes the `UID` values of new components. There is no global generator: you pass one in [BuildDefaults]. */
__attribute__((swift_name("Calendrum_coreUidGenerator")))
@protocol CalendrumOkioCalendrum_coreUidGenerator
@required

/** Returns a new `UID`; it must be globally unique. */
- (NSString *)next __attribute__((swift_name("next()")));
@end


/** How serious a [Problem] is. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSeverity")))
@interface CalendrumOkioCalendrum_coreSeverity : CalendrumOkioKotlinEnum<CalendrumOkioCalendrum_coreSeverity *>
+ (instancetype)alloc __attribute__((unavailable));

/** How serious a [Problem] is. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioCalendrum_coreSeverity *info __attribute__((swift_name("info")));
@property (class, readonly) CalendrumOkioCalendrum_coreSeverity *warning __attribute__((swift_name("warning")));
@property (class, readonly) CalendrumOkioCalendrum_coreSeverity *error __attribute__((swift_name("error")));
+ (CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreSeverity *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioCalendrum_coreSeverity *> *entries __attribute__((swift_name("entries")));
@end


/** Creates properties by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Companion")))
@interface CalendrumOkioCalendrum_coreICalPropertyCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates properties by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreICalPropertyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a property named [name].
 *
 * @param name letters, digits and hyphens only; `BEGIN` and `END` are not allowed.
 * @throws IllegalArgumentException if [name] is invalid.
 */
- (CalendrumOkioCalendrum_coreICalPropertyBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));

/**
 * Creates a property with no parameters.
 *
 * @param name letters, digits and hyphens only; the stored name is upper case; `BEGIN` and `END` are not
 *   allowed.
 * @param value the value as it is written; a control character other than a tab is not allowed.
 * @throws IllegalArgumentException if [name] or [value] is invalid.
 */
- (CalendrumOkioCalendrum_coreICalProperty *)ofName:(NSString *)name value:(NSString *)value __attribute__((swift_name("of(name:value:)")));
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
__attribute__((swift_name("Calendrum_coreICalParameter")))
@interface CalendrumOkioCalendrum_coreICalParameter : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreICalParameterCompanion *companion __attribute__((swift_name("companion")));

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


/**
 * Reads a property into a value of type [T] and writes a value back; the plug-in point for your own properties.
 *
 * Implementations must be stateless and thread-safe, report trouble as [Decoded.Malformed] instead of throwing, and not
 * put values into problems.
 *
 * @note annotations
 *   kotlin.SubclassOptInRequired(markerClass=[NormalClass(value=io/github/calendrum/ExperimentalICalApi)])
*/
__attribute__((swift_name("Calendrum_corePropertyCodec")))
@protocol CalendrumOkioCalendrum_corePropertyCodec
@required

/**
 * Decodes [property], with problems and strictness as [context] says.
 *
 * @param property the property to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumOkioCalendrum_coreDecoded>)decodeProperty:(CalendrumOkioCalendrum_coreICalProperty *)property context:(CalendrumOkioCalendrum_coreDecodeContext *)context __attribute__((swift_name("decode(property:context:)")));

/**
 * Encodes [value] into a property named by [context].
 *
 * @param value the value to write.
 * @param context carries the property name.
 */
- (CalendrumOkioCalendrum_coreICalProperty *)encodePropertyValue:(id)value context:(CalendrumOkioCalendrum_coreEncodeContext *)context __attribute__((swift_name("encodeProperty(value:context:)")));
@end


/** The constants for every code of the library, and a lookup by identifier. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProblemCode.Companion")))
@interface CalendrumOkioCalendrum_coreProblemCodeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants for every code of the library, and a lookup by identifier. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreProblemCodeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the code with the given [id], ignoring case.
 *
 * For an identifier the library knows this is the constant; for any other a new code is created with the
 * upper-case identifier and the severity [Severity.Warning].
 *
 * @throws IllegalArgumentException if [id] is empty.
 */
- (CalendrumOkioCalendrum_coreProblemCode *)ofId:(NSString *)id __attribute__((swift_name("of(id:)")));

/**
 * `ICAL-O003` (Warning): the `REPEAT` of an alarm is beyond the limit of alarm repetitions. The extra
 * repetitions are dropped.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *AlarmRepeatLimit __attribute__((swift_name("AlarmRepeatLimit")));

/**
 * `ICAL-L022` (Warning): a `TZID` is not an IANA zone but a Windows name, a display name, a link or a vendor
 * prefix. Found by the linter, once per `TZID`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *AliasTimeZoneId __attribute__((swift_name("AliasTimeZoneId")));

/**
 * `ICAL-S002` (Warning): a bare CR is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *BareCr __attribute__((swift_name("BareCr")));

/**
 * `ICAL-S003` (Warning): a bare LF is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *BareLf __attribute__((swift_name("BareLf")));

/** `ICAL-V015` (Warning): a `BINARY` value without base64 padding. The padding is added. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *Base64Padding __attribute__((swift_name("Base64Padding")));

/** `ICAL-V014` (Warning): whitespace inside a `BINARY` value. It is skipped. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *Base64Whitespace __attribute__((swift_name("Base64Whitespace")));

/**
 * `ICAL-S004` (Warning): a blank line, which the format does not allow. The line is skipped. Reported once per
 * input.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *BlankLine __attribute__((swift_name("BlankLine")));

/**
 * `ICAL-S027` (Warning): the charset hint contradicts the data, for example a byte order mark of another
 * encoding or clear UTF-8 data under a single-byte hint. The data decides.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *CharsetHintContradictsData __attribute__((swift_name("CharsetHintContradictsData")));

/**
 * `ICAL-L006` (Error): a component inside a parent that does not allow it, or that the iTIP method forbids.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ComponentNotAllowed __attribute__((swift_name("ComponentNotAllowed")));

/** `ICAL-S020` (Warning): `BEGIN` or `END` has parameters. They are ignored. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ComponentParameters __attribute__((swift_name("ComponentParameters")));

/**
 * `ICAL-S005` (Warning): the input starts with a continuation line, a space or a tab in the first byte. That
 * one character is removed and the line starts the first logical line.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ContinuationAtStart __attribute__((swift_name("ContinuationAtStart")));

/**
 * `ICAL-L026` (Error): a content line holds a control character other than horizontal tab, in its name, a
 * parameter or its value, which [RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1) forbids.
 * Parsing keeps the line as it is and byte-stable writing writes it back, but canonical writing, jCal and the
 * builders refuse it. Found by the validator and the linter; the explanation never quotes the line.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ControlCharacter __attribute__((swift_name("ControlCharacter")));

/**
 * `ICAL-V017` (Warning): a recurrence rule has both `COUNT` and `UNTIL`, which the standard forbids. Both are
 * kept; the position is that of the second.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *CountAndUntil __attribute__((swift_name("CountAndUntil")));

/**
 * `ICAL-V008` (Warning): a `DATE-TIME` value has no time, as in `DTSTART:20260901` without `VALUE=DATE`. It is
 * read as a `DATE`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *DateAsDateTime __attribute__((swift_name("DateAsDateTime")));

/** `ICAL-V007` (Warning): a `DATE` value has a time. The time is dropped. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *DateWithTime __attribute__((swift_name("DateWithTime")));

/** `ICAL-L012` (Error): a `DURATION` with hours, minutes or seconds beside a `DTSTART` of type `DATE`. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *DateWithTimeDuration __attribute__((swift_name("DateWithTimeDuration")));

/**
 * `ICAL-S025` (Warning): the encoding was guessed from the data, by a heuristic or by the placement of zero
 * bytes, and the input is transcoded from there on. The explanation names the encoding and the confidence in
 * percent.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *DetectedCharset __attribute__((swift_name("DetectedCharset")));

/**
 * `ICAL-V010` (Warning): a `DURATION` has hours and seconds but no minutes, such as `PT1H30S`. The missing `0M`
 * is added.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *DurationSkipsMinutes __attribute__((swift_name("DurationSkipsMinutes")));

/**
 * `ICAL-V009` (Warning): a `DURATION` has time units without the `T` designator, such as `P90M`. It is read as
 * `PT90M`, since the standard has no months in durations.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *DurationWithoutTimeDesignator __attribute__((swift_name("DurationWithoutTimeDesignator")));

/**
 * `ICAL-V016` (Warning): an empty list item, as in `a,,b`, or an empty rule part, as in `FREQ=DAILY;`. It is
 * skipped.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *EmptyItem __attribute__((swift_name("EmptyItem")));

/**
 * `ICAL-L025` (Warning): an empty value of a non-text property, such as `DTEND:` or `RRULE:`. Found by the
 * linter.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *EmptyValue __attribute__((swift_name("EmptyValue")));

/**
 * `ICAL-L011` (Error): `DTEND` or `DUE` is not later than `DTSTART`. Not checked when a zone does not resolve.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *EndBeforeStart __attribute__((swift_name("EndBeforeStart")));

/**
 * `ICAL-S016` (Warning): an `END` closes a component that is open deeper in the stack than the top. The
 * components in between are closed implicitly, then the matching one. One problem per `END` line.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *EndClosesNested __attribute__((swift_name("EndClosesNested")));

/** `ICAL-S015` (Warning): an `END` line with no open component. The line is ignored. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *EndWithoutBegin __attribute__((swift_name("EndWithoutBegin")));

/** `ICAL-L008` (Error): properties that exclude each other occur together, such as `DTEND` and `DURATION`. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ExclusiveProperties __attribute__((swift_name("ExclusiveProperties")));

/**
 * `ICAL-V005` (Warning): a date or time is written in the extended ISO 8601 form, with separators. It is read
 * as the basic form; in strict mode it is an error.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ExtendedIsoForm __attribute__((swift_name("ExtendedIsoForm")));

/**
 * `ICAL-O004` (Warning): a floating time, a `DATE` or an unresolved `TZID` was met while no floating zone is
 * set. The occurrence is skipped.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *FloatingWithoutZone __attribute__((swift_name("FloatingWithoutZone")));

/**
 * `ICAL-R009` (Info): an `RDATE` or `EXDATE` is in another form than `DTSTART`, such as UTC against a time
 * zone. They are compared by local time.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *FormMismatch __attribute__((swift_name("FormMismatch")));

/**
 * `ICAL-V006` (Warning): a time has fractions of a second, as written by JavaScript generators. The fraction is
 * dropped.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *FractionalSeconds __attribute__((swift_name("FractionalSeconds")));

/**
 * `ICAL-S030` (Error): the input is longer than [io.github.calendrum.syntax.ParseLimits.maxInputOctets]. Reading
 * stops before the first logical line that does not end within that many bytes, and the result is truncated.
 * Always reported.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *InputTooLarge __attribute__((swift_name("InputTooLarge")));

/**
 * `ICAL-S019` (Warning): the name in `BEGIN` or `END` is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is, and the component is opened and closed by it, ignoring ASCII
 * case.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *InvalidComponentName __attribute__((swift_name("InvalidComponentName")));

/** `ICAL-J001` (Error): the input is not JSON. Nothing is read. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *InvalidJson __attribute__((swift_name("InvalidJson")));

/**
 * `ICAL-S008` (Warning): a property or parameter name is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *InvalidName __attribute__((swift_name("InvalidName")));

/**
 * `ICAL-S006` (Warning): a logical line contains an invalid UTF-8 sequence. Each maximal invalid subsequence is
 * replaced with U+FFFD; the line is not skipped. One problem per line.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *InvalidUtf8 __attribute__((swift_name("InvalidUtf8")));

/**
 * `ICAL-Z003` (Warning): a `VTIMEZONE` has no `TZID`, no observance with `DTSTART` and `TZOFFSETTO`, or more than
 * 200,000 transitions up to its last start (a hostile zone of endless observances). It is skipped, and the next
 * resolver of the chain looks for the `TZID`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *InvalidVTimeZone __attribute__((swift_name("InvalidVTimeZone")));

/**
 * `ICAL-V001` (Error): a value does not conform to the grammar of its type; no value is decoded. In strict mode
 * it follows the warning code of a lenient repair, with the same position.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *InvalidValue __attribute__((swift_name("InvalidValue")));

/**
 * `ICAL-Z005` (Warning): the current rules of the zone cannot be written as one `RRULE` per observance, so the
 * generated `VTIMEZONE` only approximates them. The full mode gives an exact one.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *IrregularTimeZone __attribute__((swift_name("IrregularTimeZone")));

/**
 * `ICAL-J007` (Error): jCal components are nested deeper than `JCalOptions.maxDepth` (16 by default). The
 * deeper component is skipped with everything inside it.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *JCalTooDeep __attribute__((swift_name("JCalTooDeep")));

/**
 * `ICAL-J005` (Warning): a value is not in the form of its type, such as a `date-time` that is not
 * `YYYY-MM-DDTHH:MM:SS`. The value is written as it is.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *JCalValueMismatch __attribute__((swift_name("JCalValueMismatch")));

/**
 * `ICAL-V004` (Warning): a time has the second 60, which the standard allows. The decoder replaces it by 59 or
 * by the next minute, depending on its policy.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *LeapSecond __attribute__((swift_name("LeapSecond")));

/** `ICAL-R008` (Info): `BYSECOND=60` has no meaning in local time. The value is ignored. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *LeapSecondIgnored __attribute__((swift_name("LeapSecondIgnored")));

/**
 * `ICAL-S010` (Error): a logical line is longer than [io.github.calendrum.syntax.ParseLimits.maxLineOctets]. The
 * whole line is skipped and the bytes beyond the limit are not copied.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *LineTooLong __attribute__((swift_name("LineTooLong")));

/**
 * `ICAL-S028` (Warning): the heuristic is less than 90% sure of the encoding, typically for a short text of
 * capital abbreviations where windows-1251 and KOI8-R look alike. The input is read in the chosen encoding; the
 * application may want to ask the user or pass a hint.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *LowCharsetConfidence __attribute__((swift_name("LowCharsetConfidence")));

/**
 * `ICAL-J002` (Error): an element is not a jCal component: not an array of a name, an array of properties and
 * an array of components, or the name is not valid. The component is skipped with its children.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MalformedJCalComponent __attribute__((swift_name("MalformedJCalComponent")));

/**
 * `ICAL-J004` (Warning): a parameter value is not a string or an array of strings, or the `value` parameter
 * appears in the parameter object. The parameter is skipped.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MalformedJCalParameter __attribute__((swift_name("MalformedJCalParameter")));

/**
 * `ICAL-J003` (Error): a property is not an array of a name, an object of parameters, a type and at least one
 * value, or a value has the wrong JSON type. The property is skipped.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MalformedJCalProperty __attribute__((swift_name("MalformedJCalProperty")));

/**
 * `ICAL-S012` (Warning): a parameter is malformed: it has no `=`, a quote inside an unquoted value, or text
 * after a closing quote. A parameter without `=` gets no values; otherwise the value is taken literally up to
 * the next delimiter.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MalformedParameter __attribute__((swift_name("MalformedParameter")));

/**
 * `ICAL-L016` (Error): a value does not decode as its type; the details are an `ICAL-V` problem from reading.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MalformedValue __attribute__((swift_name("MalformedValue")));

/**
 * `ICAL-L018` (Error): the calendar has no `METHOD`, or one that differs from the method of the iTIP rule set
 * in use.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MethodMismatch __attribute__((swift_name("MethodMismatch")));

/**
 * `ICAL-L020` (Error): the iTIP method is not defined for the component, such as `REFRESH` for a `VJOURNAL`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MethodNotDefined __attribute__((swift_name("MethodNotDefined")));

/**
 * `ICAL-S007` (Error): there is no colon outside quotes after the property name and its parameters. The line is
 * skipped.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MissingColon __attribute__((swift_name("MissingColon")));

/**
 * `ICAL-L007` (Error): a required nested component is missing, such as a calendar without components or a
 * `VTIMEZONE` without `STANDARD` or `DAYLIGHT`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MissingComponent __attribute__((swift_name("MissingComponent")));

/**
 * `ICAL-L009` (Error): a property lacks the one it requires, such as `RRULE` without `DTSTART` or `REPEAT`
 * without `DURATION`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MissingDependency __attribute__((swift_name("MissingDependency")));

/** `ICAL-L001` (Error): a required property is missing. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MissingProperty __attribute__((swift_name("MissingProperty")));

/**
 * `ICAL-M001` (Error): a component of a schema lacks a required property, or the first such property is broken.
 * The component is skipped. The explanation names the property, never its value.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MissingRequiredField __attribute__((swift_name("MissingRequiredField")));

/**
 * `ICAL-L017` (Error): a `TZID` has no `VTIMEZONE` with that identifier in the calendar ([RFC 5545
 * §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)). Reported once per `TZID`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *MissingTimeZone __attribute__((swift_name("MissingTimeZone")));

/**
 * `ICAL-V012` (Warning): the offset `-0000`, which [RFC 5545
 * §3.3.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.14) forbids. It is read as `+0000`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *NegativeZeroOffset __attribute__((swift_name("NegativeZeroOffset")));

/**
 * `ICAL-L015` (Error): the `DTSTART` of a `VTIMEZONE` observance is not a local time ([RFC 5545
 * §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *NotLocalTime __attribute__((swift_name("NotLocalTime")));

/**
 * `ICAL-V027` (Warning): a value that must be in UTC is not, as in `DTSTAMP`, `CREATED`, `LAST-MODIFIED` and an
 * absolute `TRIGGER`. It is kept as it is.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *NotUtc __attribute__((swift_name("NotUtc")));

/**
 * `ICAL-O005` (Warning): a component has no `DTSTART` (and a to-do no `DUE` either), so it has no occurrence.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *OccurrenceWithoutStart __attribute__((swift_name("OccurrenceWithoutStart")));

/**
 * `ICAL-V002` (Error): a field is out of range, such as a month, a day, an hour, a UTC offset, an integer
 * beyond 32 bits, or a `BYxxx` part of a recurrence rule. No value is decoded.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *OutOfRange __attribute__((swift_name("OutOfRange")));

/**
 * `ICAL-O001` (Warning): the `RECURRENCE-ID` of an override matches no instance of the master. The override is
 * kept as a single occurrence.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *OverrideWithoutInstance __attribute__((swift_name("OverrideWithoutInstance")));

/**
 * `ICAL-V028` (Warning): a parameter does not apply to the value, such as `TZID` on a `DATE` or `RELATED` on an
 * absolute `TRIGGER`. It is not applied.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ParameterNotAllowed __attribute__((swift_name("ParameterNotAllowed")));

/**
 * `ICAL-V022` (Warning): a `PERIOD` ends no later than it starts, or has a duration that is not positive. It is
 * kept.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *PeriodNotPositive __attribute__((swift_name("PeriodNotPositive")));

/**
 * `ICAL-L004` (Error): a known property is in a component where it does not belong, or that the iTIP method
 * forbids.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *PropertyNotAllowed __attribute__((swift_name("PropertyNotAllowed")));

/**
 * `ICAL-S021` (Warning): a property outside any component, before the first `BEGIN` or between calendars. The
 * property is not part of the tree, but a cursor reports it at depth 0.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *PropertyOutsideComponent __attribute__((swift_name("PropertyOutsideComponent")));

/**
 * `ICAL-V003` (Error): a recurrence rule has no `FREQ` part ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)). No value is decoded.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *RecurWithoutFreq __attribute__((swift_name("RecurWithoutFreq")));

/**
 * `ICAL-R001` (Warning): the number of occurrences reached the limit while the sequence was not over, or a rule
 * with `COUNT` passed as many dates before the start of a range. The sequence ends.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *RecurrenceLimitReached __attribute__((swift_name("RecurrenceLimitReached")));

/**
 * `ICAL-R002` (Warning): too many periods in a row produced no occurrence, as in
 * `FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30`. The sequence ends.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *RecurrenceNoMatch __attribute__((swift_name("RecurrenceNoMatch")));

/**
 * `ICAL-L003` (Warning): a property occurs more than once where the standard says it SHOULD NOT, such as
 * `RRULE`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *RepeatedProperty __attribute__((swift_name("RepeatedProperty")));

/** `ICAL-V018` (Warning): a part of a recurrence rule is repeated. The last one is kept. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *RepeatedRulePart __attribute__((swift_name("RepeatedRulePart")));

/** `ICAL-R004` (Warning): the `RSCALE` of the rule is not `GREGORIAN`. The rule is not expanded. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *RscaleNotSupported __attribute__((swift_name("RscaleNotSupported")));

/**
 * `ICAL-V019` (Warning): a rule part is not allowed in this rule, such as `BYWEEKNO` without `YEARLY`, or
 * `BYSETPOS` without another `BYxxx` part. The part is kept.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *RulePartNotAllowed __attribute__((swift_name("RulePartNotAllowed")));

/**
 * `ICAL-R003` (Warning): `DTSTART` is not an instance of its own rule, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined. What happens
 * is up to the `startMismatch` option of the recurrence options.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *StartNotInRule __attribute__((swift_name("StartNotInRule")));

/**
 * `ICAL-O002` (Info): an override with `RANGE=THISANDFUTURE` was applied to the later instances, on a
 * best-effort basis.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ThisAndFutureApplied __attribute__((swift_name("ThisAndFutureApplied")));

/**
 * `ICAL-S029` (Error): the time budget [io.github.calendrum.syntax.ParseLimits.maxTime] ran out. Reading stops at a
 * line boundary and the result is truncated. Always reported.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TimeBudgetExceeded __attribute__((swift_name("TimeBudgetExceeded")));

/**
 * `ICAL-R007` (Warning): the rule has a time part or a frequency shorter than a day while `DTSTART` is a
 * `DATE`. The time parts are ignored.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TimePartWithDate __attribute__((swift_name("TimePartWithDate")));

/**
 * `ICAL-Z002` (Info): the `TZID` was found through an alias, such as an IANA link, a Windows name or a
 * `/mozilla.org/` prefix.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TimeZoneAlias __attribute__((swift_name("TimeZoneAlias")));

/**
 * `ICAL-Z004` (Warning): the `TZID` was found only by a guess from a city in its name. The zone of the first
 * city mentioned is used.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TimeZoneGuessed __attribute__((swift_name("TimeZoneGuessed")));

/**
 * `ICAL-S022` (Error): components are nested deeper than [io.github.calendrum.syntax.ParseLimits.maxDepth]. The
 * subtree is skipped up to its `END`, with one problem per subtree.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TooDeep __attribute__((swift_name("TooDeep")));

/**
 * `ICAL-S023` (Error): the limit on components was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxComponents]). Open components are closed implicitly, reading stops
 * and the result is truncated. Always reported.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TooManyComponents __attribute__((swift_name("TooManyComponents")));

/**
 * `ICAL-S014` (Error): the limit on logical lines was reached ([io.github.calendrum.syntax.ParseLimits.maxLines]).
 * Reading stops and the result is truncated. Always reported, even beyond the problem limit.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TooManyLines __attribute__((swift_name("TooManyLines")));

/**
 * `ICAL-S013` (Warning): the limit on reported problems was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxProblems]). Further problems are only counted; see
 * [io.github.calendrum.syntax.ParseResult.problemCount].
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TooManyProblems __attribute__((swift_name("TooManyProblems")));

/** `ICAL-L002` (Error): a property occurs more often than allowed. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TooManyProperties __attribute__((swift_name("TooManyProperties")));

/**
 * `ICAL-S024` (Info): the input was transcoded to UTF-8 from the encoding named by a byte order mark or by a
 * charset hint. The explanation names the encoding and where it came from. Positions refer to the decoded text.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TranscodedInput __attribute__((swift_name("TranscodedInput")));

/**
 * `ICAL-V024` (Warning): a `TZID` parameter on a UTC `DATE-TIME`, which the standard forbids. The parameter is
 * not applied and the time is UTC.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *TzidWithUtc __attribute__((swift_name("TzidWithUtc")));

/**
 * `ICAL-S018` (Warning): the input ended while components were open, as in a truncated feed. All of them are
 * closed implicitly. Not reported when reading was stopped by a limit.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnclosedComponents __attribute__((swift_name("UnclosedComponents")));

/**
 * `ICAL-S009` (Warning): a quoted parameter value is not closed before the end of the line. The value is taken
 * literally up to the next comma, semicolon or colon, and parsing goes on.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnclosedQuote __attribute__((swift_name("UnclosedQuote")));

/**
 * `ICAL-L019` (Info): the `ACTION` of an alarm is not `AUDIO`, `DISPLAY` or `EMAIL`; such an alarm is to be
 * ignored.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnknownAlarmAction __attribute__((swift_name("UnknownAlarmAction")));

/**
 * `ICAL-S026` (Warning): the charset hint is not recognized, either an unknown name or an unsupported encoding.
 * The hint is ignored and the data decides.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnknownCharsetHint __attribute__((swift_name("UnknownCharsetHint")));

/**
 * `ICAL-V023` (Warning): a backslash in a `TEXT` value is followed by a character that is not an escape of [RFC
 * 5545 §3.3.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.11), or ends the value. Both characters are
 * kept.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnknownEscape __attribute__((swift_name("UnknownEscape")));

/**
 * `ICAL-J006` (Warning): the type of a property is not a type of RFC 5545 or RFC 7265. No `VALUE` parameter is
 * written and the value is kept as it is.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnknownJCalType __attribute__((swift_name("UnknownJCalType")));

/**
 * `ICAL-L005` (Info): a property known neither to the registry nor to the rule tables, and not an `X-`
 * property.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnknownProperty __attribute__((swift_name("UnknownProperty")));

/**
 * `ICAL-V020` (Warning): an unknown part in a recurrence rule, such as an `X-` name or a future registered
 * part. It is kept, in order, and written after the known ones.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnknownRulePart __attribute__((swift_name("UnknownRulePart")));

/**
 * `ICAL-Z001` (Warning): no resolver of the chain knows the `TZID`. The unknown-zone policy decides: floating
 * time, UTC or an exception.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnknownTimeZone __attribute__((swift_name("UnknownTimeZone")));

/** `ICAL-S017` (Warning): an `END` names a component that is not open. The line is ignored. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnmatchedEnd __attribute__((swift_name("UnmatchedEnd")));

/**
 * `ICAL-S011` (Error): the input is in an encoding the library does not read at all, such as UTF-7, UTF-EBCDIC,
 * SCSU, BOCU-1 or GB18030, recognized by its signature. Nothing is parsed. The explanation names the encoding.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UnsupportedEncoding __attribute__((swift_name("UnsupportedEncoding")));

/**
 * `ICAL-R005` (Warning): the type of `UNTIL` does not match `DTSTART`, such as a `DATE` against a `DATE-TIME`
 * or UTC against local. The two are compared in the closest sensible way.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UntilTypeMismatch __attribute__((swift_name("UntilTypeMismatch")));

/**
 * `ICAL-R006` (Info): `UNTIL` is in UTC while `DTSTART` has a time zone and no `untilOffset` is set, so `UNTIL`
 * is compared with local time.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UntilWithoutZone __attribute__((swift_name("UntilWithoutZone")));

/**
 * `ICAL-L024` (Info): an `ORGANIZER` or `ATTENDEE` with the scheme in upper case, `MAILTO:`. Found by the
 * linter.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UppercaseMailto __attribute__((swift_name("UppercaseMailto")));

/**
 * `ICAL-V021` (Warning): a `URI` or `CAL-ADDRESS` without a scheme, such as an address without `mailto:`. It is
 * kept as it is.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UriWithoutScheme __attribute__((swift_name("UriWithoutScheme")));

/** `ICAL-V011` (Warning): a `UTC-OFFSET` is written with a colon, such as `+03:00`. It is read without it. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *UtcOffsetWithColon __attribute__((swift_name("UtcOffsetWithColon")));

/** `ICAL-S001` (Info): the input starts with a UTF-8 byte order mark. The three bytes are skipped. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *Utf8Bom __attribute__((swift_name("Utf8Bom")));

/**
 * `ICAL-L013` (Error): a value outside the list of the standard, such as a component `STATUS`, `VERSION` or
 * `TRANSP`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ValueNotAllowed __attribute__((swift_name("ValueNotAllowed")));

/**
 * `ICAL-L014` (Error): a number outside its range, such as `PRIORITY` outside 0 to 9 or `PERCENT-COMPLETE`
 * outside 0 to 100.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ValueOutOfRange __attribute__((swift_name("ValueOutOfRange")));

/**
 * `ICAL-L010` (Error): the form of a value differs from that of a related one, such as a `DATE` `DTEND` with a
 * `DATE-TIME` `DTSTART`, or a non-UTC `UNTIL` with a zoned `DTSTART`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ValueTypeMismatch __attribute__((swift_name("ValueTypeMismatch")));

/**
 * `ICAL-V025` (Warning): the `VALUE` parameter names a type this property does not take, or is empty. The
 * default type of the property is read.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ValueTypeNotAllowed __attribute__((swift_name("ValueTypeNotAllowed")));

/**
 * `ICAL-V026` (Warning): a value of a type other than the default is not declared by `VALUE` or `ENCODING`, as
 * in `TRIGGER:19980101T050000Z`. It is read as the value of that type.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *ValueTypeNotDeclared __attribute__((swift_name("ValueTypeNotDeclared")));

/** `ICAL-L023` (Info): `X-WR-CALNAME` without the standard `NAME` of RFC 7986. Found by the linter. */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *VendorCalendarName __attribute__((swift_name("VendorCalendarName")));

/**
 * `ICAL-L021` (Warning): `X-WR-TIMEZONE` beside floating times, which clients understand in different ways.
 * Found by the linter.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *VendorTimeZone __attribute__((swift_name("VendorTimeZone")));

/**
 * `ICAL-V013` (Warning): spaces or tabs around a value or a list item. They are dropped. Not reported for
 * `TEXT` and `BINARY`.
 */
@property (readonly) CalendrumOkioCalendrum_coreProblemCode *Whitespace __attribute__((swift_name("Whitespace")));
@end


/**
 * Collects the properties and nested components of a new [ICalComponent].
 *
 * A builder is not thread-safe. Obtain one with [ICalComponent.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalComponent.Builder")))
@interface CalendrumOkioCalendrum_coreICalComponentBuilder : CalendrumOkioBase

/** Creates the component. The builder can be reused: later changes do not affect components already built. */
- (CalendrumOkioCalendrum_coreICalComponent *)build __attribute__((swift_name("build()")));

/** Appends [component] after the nested components added so far. */
- (CalendrumOkioCalendrum_coreICalComponentBuilder *)componentComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("component(component:)")));

/**
 * Appends a nested component built by [block].
 *
 * @param name the name of the nested component.
 * @param block fills the builder of the nested component.
 * @throws IllegalArgumentException if [name] is not a valid component name.
 */
- (CalendrumOkioCalendrum_coreICalComponentBuilder *)componentName:(NSString *)name block:(void (^)(CalendrumOkioCalendrum_coreICalComponentBuilder *))block __attribute__((swift_name("component(name:block:)")));

/** Appends [property] after the properties added so far. */
- (CalendrumOkioCalendrum_coreICalComponentBuilder *)propertyProperty:(CalendrumOkioCalendrum_coreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Appends a property with the given [name] and [value] and no parameters.
 *
 * @throws IllegalArgumentException if [name] is not a valid property name, or [value] contains a control
 *   character other than a tab.
 */
- (CalendrumOkioCalendrum_coreICalComponentBuilder *)propertyName:(NSString *)name value:(NSString *)value __attribute__((swift_name("property(name:value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface CalendrumOkioKotlinEnumCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end


/**
 * Collects [ParseOptions] values. The properties and the functions of the same name do the same; the functions
 * chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseOptions.Builder")))
@interface CalendrumOkioCalendrum_coreParseOptionsBuilder : CalendrumOkioBase

/** Creates the options. */
- (CalendrumOkioCalendrum_coreParseOptions *)build __attribute__((swift_name("build()")));

/** Sets the charset hint; `null` removes it. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)charsetCharset:(NSString * _Nullable)charset __attribute__((swift_name("charset(charset:)")));

/** Sets [decodeEagerly]. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)decodeEagerlyEager:(BOOL)eager __attribute__((swift_name("decodeEagerly(eager:)")));

/** Adds a stateless [interceptor]; the same instance serves every parse and must be thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)interceptInterceptor:(id<CalendrumOkioCalendrum_coreLineInterceptor>)interceptor __attribute__((swift_name("intercept(interceptor:)")));

/**
 * Adds an interceptor created by [factory] for every parse, so that a stateful interceptor gets a fresh
 * instance each time. Interceptors run in the order they were added.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)interceptFactory:(id<CalendrumOkioCalendrum_coreLineInterceptor> (^)(void))factory __attribute__((swift_name("intercept(factory:)")));

/** Sets [limits]. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)limitsLimits:(CalendrumOkioCalendrum_coreParseLimits *)limits __attribute__((swift_name("limits(limits:)")));

/** Sets [mode]. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)modeMode:(CalendrumOkioCalendrum_coreParseMode *)mode __attribute__((swift_name("mode(mode:)")));

/** Sets the problem listener; `null` removes it. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)problemListenerListener:(id<CalendrumOkioCalendrum_coreProblemListener> _Nullable)listener __attribute__((swift_name("problemListener(listener:)")));

/** Sets [registry]; `null` selects the default registry. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)registryRegistry:(CalendrumOkioCalendrum_coreICalRegistry * _Nullable)registry __attribute__((swift_name("registry(registry:)")));

/** Sets [retainSource]. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)retainSourceRetain:(BOOL)retain __attribute__((swift_name("retainSource(retain:)")));

/** Reads only what [selection] names. The rest is skipped in the lexer; the result is partial. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)selectSelection:(CalendrumOkioCalendrum_coreSelection *)selection __attribute__((swift_name("select(selection:)")));

/** Reads only what the selection built by [block] names. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)selectBlock:(void (^)(CalendrumOkioCalendrum_coreSelectionBuilder *))block __attribute__((swift_name("select(block:)")));

/** Sets the clock for the time budget. */
- (CalendrumOkioCalendrum_coreParseOptionsBuilder *)timeSourceSource:(id<CalendrumOkioKotlinTimeSourceWithComparableMarks>)source __attribute__((swift_name("timeSource(source:)")));

/**
 * A hint for the encoding of byte input: a charset name or a whole `Content-Type` value such as `text/calendar;
 * charset=windows-1251`. The hint is checked against the data; a contradiction is reported (`ICAL-S027`) and
 * the data decides. `null` (the default) means no hint.
 */
@property NSString * _Nullable charset __attribute__((swift_name("charset")));

/** Decode every value while parsing and report its problems; false by default. Strict mode implies it. */
@property BOOL decodeEagerly __attribute__((swift_name("decodeEagerly")));

/** The bounds on the input; [ParseLimits.Default] by default. */
@property CalendrumOkioCalendrum_coreParseLimits *limits __attribute__((swift_name("limits")));

/** Lenient (the default) or strict reading. */
@property CalendrumOkioCalendrum_coreParseMode *mode __attribute__((swift_name("mode")));

/** Receives each reported problem as it is found; `null` (the default) for none. */
@property id<CalendrumOkioCalendrum_coreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/** The registry for eager decoding and strict mode; `null` selects [ICalRegistry.Default]. */
@property CalendrumOkioCalendrum_coreICalRegistry * _Nullable registry __attribute__((swift_name("registry")));

/**
 * Whether the tree keeps references to the input bytes (true, the default) or copies the lines it needs.
 * Retaining is faster and allocates less, but holds the whole input as long as any node lives; copying lets a
 * small selection outlive a big input.
 */
@property BOOL retainSource __attribute__((swift_name("retainSource")));

/** Which components and properties to read; `null` (the default) reads everything. */
@property CalendrumOkioCalendrum_coreSelection * _Nullable selection __attribute__((swift_name("selection")));

/**
 * The clock used for [ParseLimits.maxTime]; the monotonic clock by default. Set a test clock to make the budget
 * deterministic.
 */
@property id<CalendrumOkioKotlinTimeSourceWithComparableMarks> timeSource __attribute__((swift_name("timeSource")));
@end


/**
 * The view of one property line that a [LineInterceptor] receives, with functions to change it.
 *
 * The same instance is reused for every line, which is why it is valid only inside [LineInterceptor.intercept]; any
 * access outside that call throws [IllegalStateException]. Reading is cheap and allocation-free; the first change
 * copies the name, the parameters and the value of the line, and later changes work on that copy.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreInterceptedLine")))
@interface CalendrumOkioCalendrum_coreInterceptedLine : CalendrumOkioBase

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
- (void)setParameterName:(NSString *)name values:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("setParameter(name:values:)")));

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


/** The default limits and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseLimits.Companion")))
@interface CalendrumOkioCalendrum_coreParseLimitsCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default limits and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreParseLimitsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a new builder that starts from the default limits. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Generous limits for files you trust: lines up to 8 MiB, 10 million lines, 1,000 reported problems, nesting
 * depth 16, one million components, no limit on the input size and no time budget.
 */
@property (readonly) CalendrumOkioCalendrum_coreParseLimits *Default __attribute__((swift_name("Default")));
@end


/** The default registry and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalRegistry.Companion")))
@interface CalendrumOkioCalendrum_coreICalRegistryCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default registry and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreICalRegistryCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder; install [Rfc5545] and the other extensions you need.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumOkioCalendrum_coreICalRegistryBuilder *)builder __attribute__((swift_name("builder()")));

/** The registry with RFC 5545, RFC 7986, RFC 9073, RFC 9074 and RFC 9253; created on first use. */
@property (readonly) CalendrumOkioCalendrum_coreICalRegistry *Default __attribute__((swift_name("Default")));
@end


/**
 * What the registry knows about a component. Immutable.
 *
 * @property source the standard or vendor that defines it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreComponentDefinition")))
@interface CalendrumOkioCalendrum_coreComponentDefinition : CalendrumOkioBase
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
 * What the registry knows about a parameter. Immutable.
 *
 * @property key the typed key that decodes the parameter.
 * @property source the standard or vendor that defines it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParameterDefinition")))
@interface CalendrumOkioCalendrum_coreParameterDefinition : CalendrumOkioBase
- (instancetype)initWithKey:(CalendrumOkioCalendrum_coreParameterKey<id> *)key source:(NSString *)source __attribute__((swift_name("init(key:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreParameterKey<id> *key __attribute__((swift_name("key")));

/** The canonical name of the parameter. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
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
__attribute__((swift_name("Calendrum_corePropertyDefinition")))
@interface CalendrumOkioCalendrum_corePropertyDefinition : CalendrumOkioBase
- (instancetype)initWithKey:(CalendrumOkioCalendrum_corePropertyKey<id> *)key defaultType:(CalendrumOkioCalendrum_coreValueType *)defaultType allowedTypes:(NSSet<CalendrumOkioCalendrum_coreValueType *> *)allowedTypes parameters:(NSSet<NSString *> *)parameters cardinality:(NSDictionary<NSString *, CalendrumOkioCalendrum_coreCardinality *> *)cardinality source:(NSString *)source __attribute__((swift_name("init(key:defaultType:allowedTypes:parameters:cardinality:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The value types the property may have, including the default one. */
@property (readonly) NSSet<CalendrumOkioCalendrum_coreValueType *> *allowedTypes __attribute__((swift_name("allowedTypes")));

/** How often the property occurs in each component, by component name. */
@property (readonly) NSDictionary<NSString *, CalendrumOkioCalendrum_coreCardinality *> *cardinality __attribute__((swift_name("cardinality")));
@property (readonly) CalendrumOkioCalendrum_coreValueType *defaultType __attribute__((swift_name("defaultType")));
@property (readonly) CalendrumOkioCalendrum_corePropertyKey<id> *key __attribute__((swift_name("key")));

/** The canonical name of the property. */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The names of the parameters that apply to the property. */
@property (readonly) NSSet<NSString *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@end


/** A set of definitions that can be installed into a registry, such as a standard or a vendor's properties. */
__attribute__((swift_name("Calendrum_coreICalExtension")))
@protocol CalendrumOkioCalendrum_coreICalExtension
@required

/** Adds this extension's definitions to [registry].
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (void)contributeRegistry:(CalendrumOkioCalendrum_coreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));
@end


/** The presets and the builder of selections. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSelection.Companion")))
@interface CalendrumOkioCalendrum_coreSelectionCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The presets and the builder of selections. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreSelectionCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumOkioCalendrum_coreSelectionBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * What a free/busy view needs: `VFREEBUSY` whole, and `VEVENT` with `UID`, `TRANSP`, `STATUS` and the
 * occurrence properties, with the time zones they use.
 */
@property (readonly) CalendrumOkioCalendrum_coreSelection *FreeBusy __attribute__((swift_name("FreeBusy")));

/** Reads everything. This is the default of a parser. */
@property (readonly) CalendrumOkioCalendrum_coreSelection *Full __attribute__((swift_name("Full")));

/**
 * A calendar view of events: `VEVENT` with `UID`, `SUMMARY`, `DESCRIPTION`, `LOCATION`, `STATUS`, `SEQUENCE`,
 * `CREATED`, `LAST-MODIFIED`, the occurrence properties and the participants, `X-WR-TIMEZONE` of the calendar,
 * and the time zones the events use.
 */
@property (readonly) CalendrumOkioCalendrum_coreSelection *Schedule __attribute__((swift_name("Schedule")));

/**
 * What a sync needs to tell versions apart: every component with `UID`, `SEQUENCE`, `LAST-MODIFIED`, `DTSTAMP`
 * and `RECURRENCE-ID`, and no time zones.
 */
@property (readonly) CalendrumOkioCalendrum_coreSelection *SyncMetadata __attribute__((swift_name("SyncMetadata")));
@end


/** Which `VTIMEZONE` components a selective parse keeps. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTimeZoneSelection")))
@interface CalendrumOkioCalendrum_coreTimeZoneSelection : CalendrumOkioKotlinEnum<CalendrumOkioCalendrum_coreTimeZoneSelection *>
+ (instancetype)alloc __attribute__((unavailable));

/** Which `VTIMEZONE` components a selective parse keeps. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioCalendrum_coreTimeZoneSelection *all __attribute__((swift_name("all")));
@property (class, readonly) CalendrumOkioCalendrum_coreTimeZoneSelection *referenced __attribute__((swift_name("referenced")));
@property (class, readonly) CalendrumOkioCalendrum_coreTimeZoneSelection *none __attribute__((swift_name("none")));
+ (CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreTimeZoneSelection *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioCalendrum_coreTimeZoneSelection *> *entries __attribute__((swift_name("entries")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeMark")))
@protocol CalendrumOkioKotlinTimeMark
@required
- (int64_t)elapsedNow __attribute__((swift_name("elapsedNow()")));
- (BOOL)hasNotPassedNow __attribute__((swift_name("hasNotPassedNow()")));
- (BOOL)hasPassedNow __attribute__((swift_name("hasPassedNow()")));
- (id<CalendrumOkioKotlinTimeMark>)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (id<CalendrumOkioKotlinTimeMark>)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OkioByteString.Companion")))
@interface CalendrumOkioOkioByteStringCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioOkioByteStringCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumOkioOkioByteString * _Nullable)decodeBase64:(NSString *)receiver __attribute__((swift_name("decodeBase64(_:)")));
- (CalendrumOkioOkioByteString *)decodeHex:(NSString *)receiver __attribute__((swift_name("decodeHex(_:)")));
- (CalendrumOkioOkioByteString *)encodeUtf8:(NSString *)receiver __attribute__((swift_name("encodeUtf8(_:)")));
- (CalendrumOkioOkioByteString *)ofData:(CalendrumOkioKotlinByteArray *)data __attribute__((swift_name("of(data:)")));
- (CalendrumOkioOkioByteString *)toByteString:(NSData *)receiver __attribute__((swift_name("toByteString(_:)")));
- (CalendrumOkioOkioByteString *)toByteString:(CalendrumOkioKotlinByteArray *)receiver offset:(int32_t)offset byteCount:(int32_t)byteCount __attribute__((swift_name("toByteString(_:offset:byteCount:)")));
@property (readonly) CalendrumOkioOkioByteString *EMPTY __attribute__((swift_name("EMPTY")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OkioBuffer.UnsafeCursor")))
@interface CalendrumOkioOkioBufferUnsafeCursor : CalendrumOkioBase <CalendrumOkioOkioCloseable>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));

/**
 * @note This method converts instances of IOException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)closeAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("close()")));
- (int64_t)expandBufferMinByteCount:(int32_t)minByteCount __attribute__((swift_name("expandBuffer(minByteCount:)")));
- (int32_t)next __attribute__((swift_name("next()")));
- (int64_t)resizeBufferNewSize:(int64_t)newSize __attribute__((swift_name("resizeBuffer(newSize:)")));
- (int32_t)seekOffset:(int64_t)offset __attribute__((swift_name("seek(offset:)")));
@property CalendrumOkioOkioBuffer * _Nullable buffer __attribute__((swift_name("buffer")));
@property CalendrumOkioKotlinByteArray * _Nullable data __attribute__((swift_name("data")));
@property int32_t end __attribute__((swift_name("end")));
@property int64_t offset __attribute__((swift_name("offset")));
@property BOOL readWrite __attribute__((swift_name("readWrite")));
@property int32_t start __attribute__((swift_name("start")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OkioTimeout.Companion")))
@interface CalendrumOkioOkioTimeoutCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioOkioTimeoutCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumOkioOkioTimeout *NONE __attribute__((swift_name("NONE")));
@end


/** Creates VEvent values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVEvent.Companion")))
@interface CalendrumOkioCalendrum_coreVEventCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VEvent values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVEventCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVEventBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VEvent, or returns `null` when it is not a `VEVENT` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVEvent * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreVAlarm")))
@interface CalendrumOkioCalendrum_coreVAlarm : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVAlarmCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this alarm changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this alarm.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVAlarm *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VAlarm with an equal VALARM component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this alarm; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this alarm and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The time the alarm was acknowledged of the alarm (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm (`ACTION`, [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attachments of the alarm (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the alarm (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumOkioCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The links of the alarm (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the alarm, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The number of repetitions of the alarm (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable repeat __attribute__((swift_name("repeat")));

/**
 * The styled descriptions of the alarm (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

/**
 * The globally unique identifier of the alarm (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/**
 * The value of an `ATTACH` property: a document by reference ([Uri]) or inline ([Binary])
 * ([RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)).
 */
__attribute__((swift_name("Calendrum_coreAttach")))
@protocol CalendrumOkioCalendrum_coreAttach
@required

/** The media type of the attachment (`FMTTYPE`), or `null`. */
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
__attribute__((swift_name("Calendrum_coreAttendee")))
@interface CalendrumOkioCalendrum_coreAttendee : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreAttendeeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Attendee equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this attendee, parameters included. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) CalendrumOkioCalendrum_coreCuType *cuType __attribute__((swift_name("cuType")));
@property (readonly) NSArray<NSString *> *delegatedFrom __attribute__((swift_name("delegatedFrom")));
@property (readonly) NSArray<NSString *> *delegatedTo __attribute__((swift_name("delegatedTo")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<NSString *> *member __attribute__((swift_name("member")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumOkioCalendrum_corePartStat *partStat __attribute__((swift_name("partStat")));
@property (readonly) CalendrumOkioCalendrum_coreRole *role __attribute__((swift_name("role")));
@property (readonly) BOOL rsvp __attribute__((swift_name("rsvp")));
@property (readonly) NSString * _Nullable sentBy __attribute__((swift_name("sentBy")));
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
__attribute__((swift_name("Calendrum_coreClassification")))
@interface CalendrumOkioCalendrum_coreClassification : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreClassificationCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Classification with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
__attribute__((swift_name("Calendrum_coreConference")))
@interface CalendrumOkioCalendrum_coreConference : CalendrumOkioBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri features:(NSArray<CalendrumOkioCalendrum_coreConferenceFeature *> *)features label:(NSString * _Nullable)label __attribute__((swift_name("init(uri:features:label:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Conference equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreConferenceFeature *> *features __attribute__((swift_name("features")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *uri __attribute__((swift_name("uri")));
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
__attribute__((swift_name("Calendrum_coreGeo")))
@interface CalendrumOkioCalendrum_coreGeo : CalendrumOkioBase
- (instancetype)initWithLatitude:(double)latitude longitude:(double)longitude __attribute__((swift_name("init(latitude:longitude:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Geo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double latitude __attribute__((swift_name("latitude")));
@property (readonly) double longitude __attribute__((swift_name("longitude")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
__attribute__((swift_name("Calendrum_coreLink")))
@interface CalendrumOkioCalendrum_coreLink : CalendrumOkioBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumOkioCalendrum_coreValueType *)valueType linkRel:(NSString *)linkRel __attribute__((swift_name("init(value:valueType:linkRel:)"))) __attribute__((objc_designated_initializer));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumOkioCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
__attribute__((swift_name("Calendrum_coreVLocation")))
@interface CalendrumOkioCalendrum_coreVLocation : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVLocationCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this location component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this location component.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVLocation *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VLocation with an equal VLOCATION component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this location component; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this location component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the location component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The structured data of the location component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

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
__attribute__((swift_name("Calendrum_coreOrganizer")))
@interface CalendrumOkioCalendrum_coreOrganizer : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreOrganizerCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Organizer equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this organizer, parameters included. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable sentBy __attribute__((swift_name("sentBy")));
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
__attribute__((swift_name("Calendrum_coreParticipant")))
@interface CalendrumOkioCalendrum_coreParticipant : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreParticipantCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this participant component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this participant component.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreParticipant *)editBlock:(void (^)(CalendrumOkioCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a Participant with an equal PARTICIPANT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this participant component; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this participant component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the participant component (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

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
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the participant component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/**
 * The location texts of the participant component (`LOCATION` properties,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *locationTexts __attribute__((swift_name("locationTexts")));

/** The locations nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioInt * _Nullable priority __attribute__((swift_name("priority")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the participant component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the participant component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the participant component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
__attribute__((swift_name("Calendrum_coreRelatedTo")))
@interface CalendrumOkioCalendrum_coreRelatedTo : CalendrumOkioBase
- (instancetype)initWithUid:(NSString *)uid __attribute__((swift_name("init(uid:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumOkioCalendrum_coreRelType *)relType __attribute__((swift_name("init(uid:relType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumOkioCalendrum_coreRelType *)relType gap:(CalendrumOkioCalendrum_coreICalDuration *)gap __attribute__((swift_name("init(uid:relType:gap:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a RelatedTo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreICalDuration * _Nullable gap __attribute__((swift_name("gap")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumOkioCalendrum_coreRelType *relType __attribute__((swift_name("relType")));
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
__attribute__((swift_name("Calendrum_coreRequestStatus")))
@interface CalendrumOkioCalendrum_coreRequestStatus : CalendrumOkioBase
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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
__attribute__((swift_name("Calendrum_coreVResource")))
@interface CalendrumOkioCalendrum_coreVResource : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreVResourceCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this resource component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this resource component.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVResource *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VResource with an equal VRESOURCE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this resource component; unchanged nodes are shared with it. */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this resource component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)toBuilderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the resource component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The resource type of the resource component (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumOkioCalendrum_coreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

/**
 * The structured data of the resource component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The globally unique identifier of the resource component (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));
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
__attribute__((swift_name("Calendrum_coreRecur")))
@interface CalendrumOkioCalendrum_coreRecur : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreRecurCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a rule with the same parts in the same form. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over all parts. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the rule in iCalendar text form, such as `FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE`; unknown parts are left out.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The `BYDAY` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreWeekdayNum *> *byDay __attribute__((swift_name("byDay")));

/** The `BYHOUR` values, 0 to 23; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioInt *> *byHour __attribute__((swift_name("byHour")));

/** The `BYMINUTE` values, 0 to 59; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioInt *> *byMinute __attribute__((swift_name("byMinute")));

/** The `BYMONTH` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreMonthNum *> *byMonth __attribute__((swift_name("byMonth")));

/** The `BYMONTHDAY` values, 1 to 31 or -31 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioInt *> *byMonthDay __attribute__((swift_name("byMonthDay")));

/** The `BYSECOND` values, 0 to 60; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioInt *> *bySecond __attribute__((swift_name("bySecond")));

/** The `BYSETPOS` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioInt *> *bySetPos __attribute__((swift_name("bySetPos")));

/** The `BYWEEKNO` values, 1 to 53 or -53 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioInt *> *byWeekNo __attribute__((swift_name("byWeekNo")));

/** The `BYYEARDAY` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumOkioInt *> *byYearDay __attribute__((swift_name("byYearDay")));

/** The `COUNT`, or `null` when the rule has none. */
@property (readonly) CalendrumOkioInt * _Nullable count __attribute__((swift_name("count")));

/** The `FREQ` of the rule. */
@property (readonly) CalendrumOkioCalendrum_coreFrequency *frequency __attribute__((swift_name("frequency")));

/** The `INTERVAL`, 1 when the rule has none. */
@property (readonly) int32_t interval __attribute__((swift_name("interval")));

/**
 * The `RSCALE` calendar name of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529), or `null` for the Gregorian
 * calendar.
 */
@property (readonly) NSString * _Nullable rscale __attribute__((swift_name("rscale")));

/** The `SKIP` rule part, or `null` when the rule has none. */
@property (readonly) CalendrumOkioCalendrum_coreSkip * _Nullable skip __attribute__((swift_name("skip")));

/** The parts the library does not know, in the order they were written; empty for none. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRecurPart *> *unknownParts __attribute__((swift_name("unknownParts")));

/** The `UNTIL` of the rule: a date, a local date-time or a UTC date-time, or `null` when the rule has none. */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable until __attribute__((swift_name("until")));

/** The `WKST`, or `null` when the rule has none (Monday is then the default of the standard). */
@property (readonly) CalendrumOkioKotlinx_datetimeDayOfWeek * _Nullable weekStart __attribute__((swift_name("weekStart")));
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
__attribute__((swift_name("Calendrum_coreEventStatus")))
@interface CalendrumOkioCalendrum_coreEventStatus : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreEventStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a EventStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
__attribute__((swift_name("Calendrum_coreStructuredData")))
@interface CalendrumOkioCalendrum_coreStructuredData : CalendrumOkioBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumOkioCalendrum_coreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumOkioKotlinByteArray *)bytes formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(bytes:formatType:schema:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumOkioCalendrum_coreValueType *)valueType formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(value:valueType:formatType:schema:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the binary content, or `null` when the data is text or a URI. */
- (CalendrumOkioKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a StructuredData equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the data is binary (base64 encoded in the text form). */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable schema __attribute__((swift_name("schema")));
@property (readonly) NSString * _Nullable text __attribute__((swift_name("text")));
@property (readonly) CalendrumOkioCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
__attribute__((swift_name("Calendrum_coreStyledDescription")))
@interface CalendrumOkioCalendrum_coreStyledDescription : CalendrumOkioBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumOkioCalendrum_coreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumOkioCalendrum_coreValueType *)valueType formatType:(NSString *)formatType __attribute__((swift_name("init(value:valueType:formatType:)"))) __attribute__((objc_designated_initializer));

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
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumOkioCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
__attribute__((swift_name("Calendrum_coreTransparency")))
@interface CalendrumOkioCalendrum_coreTransparency : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreTransparencyCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Transparency with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** Creates VFreeBusy values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVFreeBusy.Companion")))
@interface CalendrumOkioCalendrum_coreVFreeBusyCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VFreeBusy values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVFreeBusyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVFreeBusyBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VFreeBusy, or returns `null` when it is not a `VFREEBUSY` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVFreeBusy * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreFreeBusy")))
@interface CalendrumOkioCalendrum_coreFreeBusy : CalendrumOkioBase
- (instancetype)initWithPeriods:(NSArray<CalendrumOkioCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("init(periods:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithType:(CalendrumOkioCalendrum_coreFbType *)type periods:(NSArray<CalendrumOkioCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("init(type:periods:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a FreeBusy equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalPeriod *> *periods __attribute__((swift_name("periods")));
@property (readonly) CalendrumOkioCalendrum_coreFbType *type __attribute__((swift_name("type")));
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
__attribute__((swift_name("Calendrum_coreImageDisplay")))
@interface CalendrumOkioCalendrum_coreImageDisplay : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreImageDisplayCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ImageDisplay with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** Creates VJournal values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVJournal.Companion")))
@interface CalendrumOkioCalendrum_coreVJournalCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VJournal values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVJournalCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVJournalBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VJournal, or returns `null` when it is not a `VJOURNAL` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVJournal * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreJournalStatus")))
@interface CalendrumOkioCalendrum_coreJournalStatus : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreJournalStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a JournalStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Method and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreMethod.Companion")))
@interface CalendrumOkioCalendrum_coreMethodCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Method and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreMethodCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Method holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreMethod *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ADD`. The organizer adds instances to an existing series. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *Add __attribute__((swift_name("Add")));

/** `CANCEL`. The organizer cancels a component or some of its instances. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *Cancel __attribute__((swift_name("Cancel")));

/** `COUNTER`. An attendee proposes a change. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *Counter __attribute__((swift_name("Counter")));

/** `DECLINECOUNTER`. The organizer declines a proposed change. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *DeclineCounter __attribute__((swift_name("DeclineCounter")));

/** `PUBLISH`. Publishes a component to anyone, without expecting an answer. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *Publish __attribute__((swift_name("Publish")));

/** `REFRESH`. An attendee asks for the latest version. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *Refresh __attribute__((swift_name("Refresh")));

/** `REPLY`. An attendee answers a request. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *Reply __attribute__((swift_name("Reply")));

/** `REQUEST`. Asks for attendance, or updates a component sent before. */
@property (readonly) CalendrumOkioCalendrum_coreMethod *Request __attribute__((swift_name("Request")));
@end


/** Creates durations by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalDuration.Companion")))
@interface CalendrumOkioCalendrum_coreICalDurationCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates durations by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreICalDurationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates the duration that equals [duration] in hours, minutes and seconds.
 *
 * @throws IllegalArgumentException if [duration] is infinite, has a fraction of a second, or is longer than
 *   `Int.MAX_VALUE` hours.
 */
- (CalendrumOkioCalendrum_coreICalDuration *)ofDuration:(int64_t)duration __attribute__((swift_name("of(duration:)")));

/**
 * Creates a duration of [days] days; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [days] is `Int.MIN_VALUE`.
 */
- (CalendrumOkioCalendrum_coreICalDuration *)ofDaysDays:(int32_t)days __attribute__((swift_name("ofDays(days:)")));

/**
 * Creates a duration of [hours] hours; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [hours] is `Int.MIN_VALUE`.
 */
- (CalendrumOkioCalendrum_coreICalDuration *)ofHoursHours:(int32_t)hours __attribute__((swift_name("ofHours(hours:)")));

/**
 * Creates a duration of [minutes] minutes; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [minutes] is `Int.MIN_VALUE`.
 */
- (CalendrumOkioCalendrum_coreICalDuration *)ofMinutesMinutes:(int32_t)minutes __attribute__((swift_name("ofMinutes(minutes:)")));

/**
 * Creates a duration of [seconds] seconds; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [seconds] is `Int.MIN_VALUE`.
 */
- (CalendrumOkioCalendrum_coreICalDuration *)ofSecondsSeconds:(int32_t)seconds __attribute__((swift_name("ofSeconds(seconds:)")));

/**
 * Creates a duration of [weeks] weeks; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [weeks] is `Int.MIN_VALUE`.
 */
- (CalendrumOkioCalendrum_coreICalDuration *)ofWeeksWeeks:(int32_t)weeks __attribute__((swift_name("ofWeeks(weeks:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/DateTimePeriodSerializer))
*/
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod")))
@interface CalendrumOkioKotlinx_datetimeDateTimePeriod : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioKotlinx_datetimeDateTimePeriodCompanion *companion __attribute__((swift_name("companion")));
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


/** Creates VTimeZone values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTimeZone.Companion")))
@interface CalendrumOkioCalendrum_coreVTimeZoneCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTimeZone values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVTimeZoneCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTimeZone, or returns `null` when it is not a `VTIMEZONE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVTimeZone * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreVTimeZone.Observance")))
@interface CalendrumOkioCalendrum_coreVTimeZoneObservance : CalendrumOkioBase

/**
 * Returns a copy of this observance changed by [block]; untouched nodes are shared.
 *
 * @param block changes the builder.
 */
- (CalendrumOkioCalendrum_coreVTimeZoneObservance *)editBlock:(void (^)(CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is an observance of the same kind with an equal component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this observance. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** The comments (`COMMENT`) in source order. */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumOkioCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts (`CONCEPT`, [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) of the
 * observance in source order.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The links (`LINK`, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) of the observance in
 * source order.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) CalendrumOkioKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The UTC offset the observance puts in force (`TZOFFSETTO`,
 * [RFC 5545 §3.8.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumOkioKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The additional start dates (`RDATE`) in source order; a period contributes its start. */
@property (readonly) NSArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The reference identifiers (`REFID`, [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) of
 * the observance in source order.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships (`RELATED-TO`, [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5))
 * of the observance in source order.
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/** The recurrence rule of the observance (`RRULE`), or `null` when it has none or it does not decode. */
@property (readonly) CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The local start (`DTSTART`) of the observance, in the offset before it; `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));
@end


/** Creates VTodo values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTodo.Companion")))
@interface CalendrumOkioCalendrum_coreVTodoCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTodo values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVTodoCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVTodoBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTodo, or returns `null` when it is not a `VTODO` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVTodo * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreTodoStatus")))
@interface CalendrumOkioCalendrum_coreTodoStatus : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreTodoStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a TodoStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/**
 * Collects the properties and nested components of a new or edited alarm, with a Kotlin DSL and chained calls. Get
 * one from [VAlarm.builder] or [VAlarm.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVAlarm.Builder")))
@interface CalendrumOkioCalendrum_coreVAlarmBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVAlarmBuilder *>

/**
 * Sets [acknowledged] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)acknowledgedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("acknowledged(value:)")));

/**
 * Sets [action] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)actionValue:(CalendrumOkioCalendrum_coreAlarmAction * _Nullable)value __attribute__((swift_name("action(value:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)attachAttach:(id<CalendrumOkioCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)attendeeAttendee:(CalendrumOkioCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)attendeesBlock:(void (^)(CalendrumOkioCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Makes this an `AUDIO` alarm: sets `ACTION` and [trigger]; the platform chooses the sound unless you attach
 * one.
 *
 * @param trigger when the alarm fires.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)audioTrigger:(id<CalendrumOkioCalendrum_coreTrigger>)trigger __attribute__((swift_name("audio(trigger:)")));

/** Creates the alarm. */
- (CalendrumOkioCalendrum_coreVAlarm *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Makes this a `DISPLAY` alarm: sets `ACTION`, [trigger] and the `DESCRIPTION` shown to the user.
 *
 * @param trigger when the alarm fires.
 * @param description the text to display.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)displayTrigger:(id<CalendrumOkioCalendrum_coreTrigger>)trigger description:(NSString *)description __attribute__((swift_name("display(trigger:description:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)durationValue:(CalendrumOkioCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Makes this an `EMAIL` alarm: sets `ACTION`, [trigger], the `SUMMARY` that becomes the subject and the
 * `DESCRIPTION` that becomes the body.
 *
 * @param trigger when the alarm fires.
 * @param summary the subject of the mail.
 * @param description the body of the mail.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)emailTrigger:(id<CalendrumOkioCalendrum_coreTrigger>)trigger summary:(NSString *)summary description:(NSString *)description __attribute__((swift_name("email(trigger:summary:description:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the alarm. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)locationValue:(CalendrumOkioCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)locationBlock:(void (^)(CalendrumOkioCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the alarm in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)locationsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [proximity] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)proximityValue:(CalendrumOkioCalendrum_coreProximity * _Nullable)value __attribute__((swift_name("proximity(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [repeat] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)repeatValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("repeat(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)styledDescriptionValue:(CalendrumOkioCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [trigger] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)triggerValue:(id<CalendrumOkioCalendrum_coreTrigger> _Nullable)value __attribute__((swift_name("trigger(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * The time the alarm was acknowledged of the alarm being built (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)); assigning `null` removes the property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm being built (`ACTION`,
 * [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property CalendrumOkioCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/** The locations of the alarm being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm being built (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)); assigning `null` removes the property.
 */
@property CalendrumOkioCalendrum_coreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

/**
 * The number of repetitions of the alarm being built (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable repeat __attribute__((swift_name("repeat")));

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
@property id<CalendrumOkioCalendrum_coreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

/**
 * The globally unique identifier of the alarm being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/**
 * Collects the address and the parameters of an [Attendee]. The properties and the functions of the same name do
 * the same; the functions chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAttendee.Builder")))
@interface CalendrumOkioCalendrum_coreAttendeeBuilder : CalendrumOkioBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the attendee.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumOkioCalendrum_coreAttendee *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [cuType] to [value] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)cuTypeValue:(CalendrumOkioCalendrum_coreCuType *)value __attribute__((swift_name("cuType(value:)")));

/** Sets [delegatedFrom] to [values] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)delegatedFromValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedFrom(values:)")));

/** Sets [delegatedTo] to [values] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)delegatedToValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedTo(values:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/** Sets [member] to [values] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)memberValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("member(values:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)parameterName:(NSString *)name values:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Sets [partStat] to [value] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)partStatValue:(CalendrumOkioCalendrum_corePartStat *)value __attribute__((swift_name("partStat(value:)")));

/** Removes the parameter [name]. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [role] to [value] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)roleValue:(CalendrumOkioCalendrum_coreRole *)value __attribute__((swift_name("role(value:)")));

/** Sets [rsvp] to [value] and returns this builder. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)rsvpValue:(BOOL)value __attribute__((swift_name("rsvp(value:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The calendar address. */
@property NSString *address __attribute__((swift_name("address")));

/** The common name (`CN`); assigning `null` removes the parameter. */
@property NSString * _Nullable cn __attribute__((swift_name("cn")));

/** The calendar user type (`CUTYPE`); [CuType.Individual] when absent. */
@property CalendrumOkioCalendrum_coreCuType *cuType __attribute__((swift_name("cuType")));

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
@property CalendrumOkioCalendrum_corePartStat *partStat __attribute__((swift_name("partStat")));

/** The role (`ROLE`); [Role.ReqParticipant] when absent. */
@property CalendrumOkioCalendrum_coreRole *role __attribute__((swift_name("role")));

/** Whether a reply is requested (`RSVP`); false when absent. */
@property BOOL rsvp __attribute__((swift_name("rsvp")));

/** The address that acts on behalf of the user (`SENT-BY`); assigning `null` removes the parameter. */
@property NSString * _Nullable sentBy __attribute__((swift_name("sentBy")));
@end


/**
 * An editable view of the `ATTENDEE` properties of a builder, addressed by calendar address (compared ignoring ASCII
 * case).
 *
 * @property items the attendees now in the list.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAttendeeList")))
@interface CalendrumOkioCalendrum_coreAttendeeList : CalendrumOkioBase

/** Adds [attendee] after the existing ones. */
- (void)addAttendee:(CalendrumOkioCalendrum_coreAttendee *)attendee __attribute__((swift_name("add(attendee:)")));

/**
 * Adds an attendee with the address [address], set up by [block].
 *
 * @param address the calendar address of the new attendee.
 * @param block sets up the attendee.
 */
- (void)addAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("add(address:block:)")));

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
- (BOOL)replaceAddress:(NSString *)address block:(void (^)(CalendrumOkioCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("replace(address:block:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumOkioCalendrum_coreAttendee *> *items __attribute__((swift_name("items")));
@end


/**
 * Collects the properties and nested components of a new or edited location component, with a Kotlin DSL and
 * chained calls. Get one from [VLocation.builder] or [VLocation.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVLocation.Builder")))
@interface CalendrumOkioCalendrum_coreVLocationBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVLocationBuilder *>

/**
 * Creates the location component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumOkioCalendrum_coreVLocation *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)geoValue:(CalendrumOkioCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [locationTypes] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)locationTypesValue:(NSArray<NSString *> * _Nullable)value __attribute__((swift_name("locationTypes(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)structuredDataValue:(CalendrumOkioCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

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
@property CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

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


/**
 * Collects the address and the parameters of an [Organizer]. The properties and the functions of the same name do
 * the same; the functions chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreOrganizer.Builder")))
@interface CalendrumOkioCalendrum_coreOrganizerBuilder : CalendrumOkioBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the organizer.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumOkioCalendrum_coreOrganizer *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)parameterName:(NSString *)name values:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Removes the parameter [name]. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

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


/**
 * Collects the properties and nested components of a new or edited participant component, with a Kotlin DSL and
 * chained calls. Get one from [Participant.builder] or [Participant.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParticipant.Builder")))
@interface CalendrumOkioCalendrum_coreParticipantBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreParticipantBuilder *>

/** Adds [value] as an `ATTACH` property. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)attachValue:(id<CalendrumOkioCalendrum_coreAttach>)value __attribute__((swift_name("attach(value:)")));

/**
 * Creates the participant.
 *
 * @throws IllegalStateException if `UID` or `PARTICIPANT-TYPE` is missing and the builder has no defaults for
 *   the `UID`.
 */
- (CalendrumOkioCalendrum_coreParticipant *)build __attribute__((swift_name("build()")));

/**
 * Sets [calendarAddress] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)calendarAddressValue:(NSString * _Nullable)value __attribute__((swift_name("calendarAddress(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)categoriesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)createdValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)geoValue:(CalendrumOkioCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)lastModifiedValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the participant component. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)locationValue:(CalendrumOkioCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)locationBlock:(void (^)(CalendrumOkioCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Adds a `LOCATION` property with the text [text]. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)locationTextText:(NSString *)text __attribute__((swift_name("locationText(text:)")));

/** Edits the locations of the participant component in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)locationsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [participantType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)participantTypeValue:(CalendrumOkioCalendrum_coreParticipantType * _Nullable)value __attribute__((swift_name("participantType(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)priorityValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `REQUEST-STATUS` property. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)requestStatusValue:(CalendrumOkioCalendrum_coreRequestStatus *)value __attribute__((swift_name("requestStatus(value:)")));

/** Adds [value] as a resource of the participant component. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)resourceComponentValue:(CalendrumOkioCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)resourceComponentBlock:(void (^)(CalendrumOkioCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the participant component in [block]: add, replace or remove them. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)resourceComponentsBlock:(void (^)(CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)resourcesValues:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)sequenceValue:(CalendrumOkioInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)stampValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)statusValue:(CalendrumOkioCalendrum_coreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)structuredDataValue:(CalendrumOkioCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)styledDescriptionValue:(CalendrumOkioCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

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
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the participant component being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component being built (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)); assigning `null` removes the property.
 */
@property CalendrumOkioCalendrum_coreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable priority __attribute__((swift_name("priority")));

/** The resources of the participant component being built, as an editable list. */
@property (readonly) CalendrumOkioCalendrum_coreComponentList<CalendrumOkioCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The revision sequence number of the participant component being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumOkioCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

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
__attribute__((swift_name("Calendrum_coreICalPeriod")))
@interface CalendrumOkioCalendrum_coreICalPeriod : CalendrumOkioBase
- (instancetype)initWithStart:(id<CalendrumOkioCalendrum_coreICalDateTime>)start end:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)end duration:(CalendrumOkioCalendrum_coreICalDuration * _Nullable)duration __attribute__((swift_name("init(start:end:duration:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a period with equal start, end and duration. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the start, the end and the duration. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the start and the end or duration separated by a slash, in the forms of [ICalDateTime.toString] and
 * [ICalDuration.toString].
 */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));
@property (readonly) id<CalendrumOkioCalendrum_coreICalDateTime> start __attribute__((swift_name("start")));
@end


/**
 * Collects the properties and nested components of a new or edited resource component, with a Kotlin DSL and
 * chained calls. Get one from [VResource.builder] or [VResource.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVResource.Builder")))
@interface CalendrumOkioCalendrum_coreVResourceBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVResourceBuilder *>

/**
 * Creates the resource component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumOkioCalendrum_coreVResource *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)geoValue:(CalendrumOkioCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [resourceType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)resourceTypeValue:(CalendrumOkioCalendrum_coreResourceType * _Nullable)value __attribute__((swift_name("resourceType(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)structuredDataValue:(CalendrumOkioCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

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
@property CalendrumOkioCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The name of the resource component being built (`NAME`,
 * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)); assigning `null` removes the property.
 */
@property NSString * _Nullable name __attribute__((swift_name("name")));

/**
 * The resource type of the resource component being built (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)); assigning `null` removes the property.
 */
@property CalendrumOkioCalendrum_coreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

/**
 * The globally unique identifier of the resource component being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFrequency")))
@interface CalendrumOkioCalendrum_coreFrequency : CalendrumOkioKotlinEnum<CalendrumOkioCalendrum_coreFrequency *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioCalendrum_coreFrequency *secondly __attribute__((swift_name("secondly")));
@property (class, readonly) CalendrumOkioCalendrum_coreFrequency *minutely __attribute__((swift_name("minutely")));
@property (class, readonly) CalendrumOkioCalendrum_coreFrequency *hourly __attribute__((swift_name("hourly")));
@property (class, readonly) CalendrumOkioCalendrum_coreFrequency *daily __attribute__((swift_name("daily")));
@property (class, readonly) CalendrumOkioCalendrum_coreFrequency *weekly __attribute__((swift_name("weekly")));
@property (class, readonly) CalendrumOkioCalendrum_coreFrequency *monthly __attribute__((swift_name("monthly")));
@property (class, readonly) CalendrumOkioCalendrum_coreFrequency *yearly __attribute__((swift_name("yearly")));
+ (CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreFrequency *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioCalendrum_coreFrequency *> *entries __attribute__((swift_name("entries")));
@end


/**
 * Builds a [Recur] that is correct by construction: the combinations the standard forbids are rejected as they are
 * added. Not thread-safe; obtain one with [Recur.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRecur.Builder")))
@interface CalendrumOkioCalendrum_coreRecurBuilder : CalendrumOkioBase

/**
 * Creates the rule.
 *
 * @throws IllegalStateException if `BYSETPOS` is used without another `BYxxx` part, or `BYWEEKNO` is combined
 *   with a numbered `BYDAY`.
 */
- (CalendrumOkioCalendrum_coreRecur *)build __attribute__((swift_name("build()")));

/**
 * Sets `BYDAY` to the given entries, which may carry a number such as the second Tuesday.
 *
 * @throws IllegalArgumentException if no entry is given, a number is used with a frequency other than `MONTHLY`
 *   or `YEARLY`, or a number is 0 or outside -53 to 53.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byDayDays:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreWeekdayNum *> *)days __attribute__((swift_name("byDay(days:)")));

/**
 * Sets `BYDAY` to the given days, every such weekday.
 *
 * @throws IllegalArgumentException if no day is given.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byDayDaysOfWeek:(CalendrumOkioKotlinArray<CalendrumOkioKotlinx_datetimeDayOfWeek *> *)daysOfWeek __attribute__((swift_name("byDay(daysOfWeek:)")));

/**
 * Sets `BYHOUR` to [values], each 0 to 23.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byHourValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("byHour(values:)")));

/**
 * Sets `BYMINUTE` to [values], each 0 to 59.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byMinuteValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("byMinute(values:)")));

/**
 * Sets `BYMONTH` to [values], each 1 to 12.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byMonthValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("byMonth(values:)")));

/**
 * Sets `BYMONTHDAY` to [values], each 1 to 31 or -31 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `WEEKLY`, there is no value or one is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byMonthDayValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("byMonthDay(values:)")));

/**
 * Sets `BYSECOND` to [values], each 0 to 60.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)bySecondValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("bySecond(values:)")));

/**
 * Sets `BYSETPOS` to [values], each 1 to 366 or -366 to -1. The rule also needs another `BYxxx` part.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)bySetPosValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("bySetPos(values:)")));

/**
 * Sets `BYWEEKNO` to [values], each 1 to 53 or -53 to -1.
 *
 * @throws IllegalArgumentException if the frequency is not `YEARLY`, there is no value or one is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byWeekNoValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("byWeekNo(values:)")));

/**
 * Sets `BYYEARDAY` to [values], each 1 to 366 or -366 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `DAILY`, `WEEKLY` or `MONTHLY`, there is no value or one
 *   is out of range.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)byYearDayValues:(CalendrumOkioKotlinIntArray *)values __attribute__((swift_name("byYearDay(values:)")));

/**
 * Sets `COUNT`, the number of occurrences.
 *
 * @throws IllegalStateException if `UNTIL` is already set.
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)countValue:(int32_t)value __attribute__((swift_name("count(value:)")));

/**
 * Sets `INTERVAL`, the step in units of the frequency.
 *
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)intervalValue:(int32_t)value __attribute__((swift_name("interval(value:)")));

/** Returns the frequency of the rule being built. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Sets `UNTIL`, which is a date, a local date-time or a UTC date-time and must suit the type of `DTSTART`.
 *
 * @throws IllegalStateException if `COUNT` is already set.
 * @throws IllegalArgumentException if [value] is a zoned date-time or its year is outside 0000 to 9999.
 */
- (CalendrumOkioCalendrum_coreRecurBuilder *)untilValue:(id<CalendrumOkioCalendrum_coreICalDateTime>)value __attribute__((swift_name("until(value:)")));

/** Sets `WKST`, the day on which the week starts. */
- (CalendrumOkioCalendrum_coreRecurBuilder *)weekStartDay:(CalendrumOkioKotlinx_datetimeDayOfWeek *)day __attribute__((swift_name("weekStart(day:)")));
@end


/**
 * Collects the properties of a `STANDARD` or `DAYLIGHT` observance. Get one from [Standard.builder],
 * [Daylight.builder] or [Observance.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTimeZone.ObservanceBuilder")))
@interface CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder : CalendrumOkioCalendrum_coreComponentBuilder<CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *>

/** Creates the observance: a [Standard] or a [Daylight], as the builder was made for. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservance *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)linkValue:(CalendrumOkioCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds a `TZNAME` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/** Sets [offsetFrom] to [value] and returns this builder; `null` removes the property. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)offsetFromValue:(CalendrumOkioKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetFrom(value:)")));

/** Sets [offsetTo] to [value] and returns this builder; `null` removes the property. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)offsetToValue:(CalendrumOkioKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetTo(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)rdateDates:(CalendrumOkioKotlinArray<id<CalendrumOkioCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)rdatePeriods:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/** Adds [relatedTo] as a `RELATED-TO` property. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)relatedToRelatedTo:(CalendrumOkioCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/** Adds a `RELATED-TO` property naming the component with the given [uid]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Sets [rrule] to [value] and returns this builder; `null` removes the property. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)rruleValue:(CalendrumOkioCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)rruleFrequency:(CalendrumOkioCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumOkioCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/** Sets [start] to [value] and returns this builder; `null` removes the property. */
- (CalendrumOkioCalendrum_coreVTimeZoneObservanceBuilder *)startValue:(id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * The offset before the observance being built (`TZOFFSETFROM`); assigning `null` removes the property.
 */
@property CalendrumOkioKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The offset the observance being built puts in force (`TZOFFSETTO`); assigning `null` removes the
 * property.
 */
@property CalendrumOkioKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The recurrence rule of the observance being built (`RRULE`); assigning `null` removes the property. */
@property CalendrumOkioCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/** The local start (`DTSTART`) of the observance being built; assigning `null` removes the property. */
@property id<CalendrumOkioCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="2.3")
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant")))
@interface CalendrumOkioKotlinInstant : CalendrumOkioBase <CalendrumOkioKotlinComparable>
@property (class, readonly, getter=companion) CalendrumOkioKotlinInstantCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumOkioKotlinInstant *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumOkioKotlinInstant *)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (int64_t)minusOther:(CalendrumOkioKotlinInstant *)other __attribute__((swift_name("minus(other:)")));
- (CalendrumOkioKotlinInstant *)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
- (int64_t)toEpochMilliseconds __attribute__((swift_name("toEpochMilliseconds()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t epochSeconds __attribute__((swift_name("epochSeconds")));
@property (readonly) int32_t nanosecondsOfSecond __attribute__((swift_name("nanosecondsOfSecond")));
@end


/** Collects the parameters and the value of a new [ICalProperty]. A builder is not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Builder")))
@interface CalendrumOkioCalendrum_coreICalPropertyBuilder : CalendrumOkioBase

/**
 * Creates the property.
 *
 * @throws IllegalArgumentException if the value contains a control character other than a tab.
 */
- (CalendrumOkioCalendrum_coreICalProperty *)build __attribute__((swift_name("build()")));

/** Appends [parameter] after the parameters added so far. */
- (CalendrumOkioCalendrum_coreICalPropertyBuilder *)parameterParameter:(CalendrumOkioCalendrum_coreICalParameter *)parameter __attribute__((swift_name("parameter(parameter:)")));

/**
 * Appends a parameter with the given [name] and [values].
 *
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumOkioCalendrum_coreICalPropertyBuilder *)parameterName:(NSString *)name values:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/**
 * Sets the value, which is written as is: escape `TEXT` yourself or use the typed model. The default is an
 * empty string.
 */
- (CalendrumOkioCalendrum_coreICalPropertyBuilder *)valueValue:(NSString *)value __attribute__((swift_name("value(value:)")));
@end


/** Creates parameters by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalParameter.Companion")))
@interface CalendrumOkioCalendrum_coreICalParameterCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates parameters by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreICalParameterCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates a parameter with the given [name] and [values].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @param values at least one value.
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumOkioCalendrum_coreICalParameter *)ofName:(NSString *)name values:(CalendrumOkioKotlinArray<NSString *> *)values __attribute__((swift_name("of(name:values:)")));
@end


/**
 * The result of decoding a property or parameter: a value, nothing, or a malformed one.
 *
 * Reading with `component[key]` gives only the value or `null`; `component.decode(key)` tells which of the three it was
 * and why.
 */
__attribute__((swift_name("Calendrum_coreDecoded")))
@protocol CalendrumOkioCalendrum_coreDecoded
@required
@end


/**
 * How a codec decodes: leniently or strictly, and the factory for problems at the right position.
 *
 * @property isStrict whether a value that is only acceptable by repair is an error.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext")))
@interface CalendrumOkioCalendrum_coreDecodeContext : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreDecodeContextCompanion *companion __attribute__((swift_name("companion")));

/** Creates a problem with [code] positioned [valueOffset] characters into the value of [property]. */
- (CalendrumOkioCalendrum_coreProblem *)problemProperty:(CalendrumOkioCalendrum_coreICalProperty *)property code:(CalendrumOkioCalendrum_coreProblemCode *)code valueOffset:(int32_t)valueOffset __attribute__((swift_name("problem(property:code:valueOffset:)")));

/** Creates a problem with [code] positioned at the value of [parameter] of [property]. */
- (CalendrumOkioCalendrum_coreProblem *)problemProperty:(CalendrumOkioCalendrum_coreICalProperty *)property parameter:(CalendrumOkioCalendrum_coreICalParameter *)parameter code:(CalendrumOkioCalendrum_coreProblemCode *)code __attribute__((swift_name("problem(property:parameter:code:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL isStrict __attribute__((swift_name("isStrict")));
@end


/**
 * What a codec needs to write: the name of the property or parameter.
 *
 * @property name the name to write.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreEncodeContext")))
@interface CalendrumOkioCalendrum_coreEncodeContext : CalendrumOkioBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/**
 * Collects what a [Selection] reads. The properties and the functions of the same name do the same. Not
 * thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSelection.Builder")))
@interface CalendrumOkioCalendrum_coreSelectionBuilder : CalendrumOkioBase

/** Creates the selection. */
- (CalendrumOkioCalendrum_coreSelection *)build __attribute__((swift_name("build()")));

/**
 * Adds components by name. Names of components that the standard defines and unknown `X-` names are accepted;
 * time zone components are chosen with [timeZones] instead.
 *
 * @throws IllegalArgumentException if a name is invalid, is not a component name, or names a time zone
 *   component.
 */
- (CalendrumOkioCalendrum_coreSelectionBuilder *)componentsNames:(CalendrumOkioKotlinArray<NSString *> *)names __attribute__((swift_name("components(names:)")));

/** Adds what the [features] need. */
- (CalendrumOkioCalendrum_coreSelectionBuilder *)needFeatures:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreFeature *> *)features __attribute__((swift_name("need(features:)")));

/** Adds the properties that the typed [keys] stand for. */
- (CalendrumOkioCalendrum_coreSelectionBuilder *)propertiesKeys:(CalendrumOkioKotlinArray<CalendrumOkioCalendrum_corePropertyKey<id> *> *)keys __attribute__((swift_name("properties(keys:)")));

/**
 * Adds properties by name, which are read in every selected component. Unknown names are matched exactly
 * (ignoring ASCII case).
 *
 * @throws IllegalArgumentException if a name is invalid, is not a property name, or is `BEGIN` or `END`.
 */
- (CalendrumOkioCalendrum_coreSelectionBuilder *)propertiesNames:(CalendrumOkioKotlinArray<NSString *> *)names __attribute__((swift_name("properties(names:)")));

/** Sets [timeZones]. */
- (CalendrumOkioCalendrum_coreSelectionBuilder *)timeZonesSelection:(CalendrumOkioCalendrum_coreTimeZoneSelection *)selection __attribute__((swift_name("timeZones(selection:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Which `VTIMEZONE` components to keep; [TimeZoneSelection.All] by default. */
@property CalendrumOkioCalendrum_coreTimeZoneSelection *timeZones __attribute__((swift_name("timeZones")));
@end


/**
 * Collects [ParseLimits] values. The properties and the functions of the same name do the same; the functions
 * chain.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseLimits.Builder")))
@interface CalendrumOkioCalendrum_coreParseLimitsBuilder : CalendrumOkioBase

/**
 * Creates the limits.
 *
 * @throws IllegalArgumentException if a bound is not positive (or, for [maxProblems], negative), or the time is
 *   not positive.
 */
- (CalendrumOkioCalendrum_coreParseLimits *)build __attribute__((swift_name("build()")));

/** Sets [maxComponents] to [value]. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)maxComponentsValue:(int32_t)value __attribute__((swift_name("maxComponents(value:)")));

/** Sets [maxDepth] to [value]. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)maxDepthValue:(int32_t)value __attribute__((swift_name("maxDepth(value:)")));

/** Sets [maxInputOctets] to [value]. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)maxInputOctetsValue:(int64_t)value __attribute__((swift_name("maxInputOctets(value:)")));

/** Sets [maxLineOctets] to [value]. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)maxLineOctetsValue:(int32_t)value __attribute__((swift_name("maxLineOctets(value:)")));

/** Sets [maxLines] to [value]. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)maxLinesValue:(int32_t)value __attribute__((swift_name("maxLines(value:)")));

/** Sets [maxProblems] to [value]. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)maxProblemsValue:(int32_t)value __attribute__((swift_name("maxProblems(value:)")));

/** Sets [maxTime] to [value]; `null` removes the budget. */
- (CalendrumOkioCalendrum_coreParseLimitsBuilder *)maxTimeValue:(id _Nullable)value __attribute__((swift_name("maxTime(value:)")));

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


/** Collects definitions and extensions into an [ICalRegistry]. Not thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalRegistry.Builder")))
@interface CalendrumOkioCalendrum_coreICalRegistryBuilder : CalendrumOkioBase

/** Creates the registry. */
- (CalendrumOkioCalendrum_coreICalRegistry *)build __attribute__((swift_name("build()")));

/** Adds a component definition; the children of a definition for the same name are merged. */
- (CalendrumOkioCalendrum_coreICalRegistryBuilder *)componentDefinition:(CalendrumOkioCalendrum_coreComponentDefinition *)definition __attribute__((swift_name("component(definition:)")));

/**
 * Installs [extension] once: a second install of the same extension does nothing. The extension adds its
 * definitions to this builder.
 */
- (CalendrumOkioCalendrum_coreICalRegistryBuilder *)installExtension:(id<CalendrumOkioCalendrum_coreICalExtension>)extension __attribute__((swift_name("install(extension:)")));

/** Adds a parameter definition, replacing a definition of another key for the same name. */
- (CalendrumOkioCalendrum_coreICalRegistryBuilder *)parameterDefinition:(CalendrumOkioCalendrum_coreParameterDefinition *)definition __attribute__((swift_name("parameter(definition:)")));

/**
 * Adds a property definition. A definition for a key that is already defined is merged into it; one for the
 * same name in other components is kept beside it.
 */
- (CalendrumOkioCalendrum_coreICalRegistryBuilder *)propertyDefinition:(CalendrumOkioCalendrum_corePropertyDefinition *)definition __attribute__((swift_name("property(definition:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
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
__attribute__((swift_name("Calendrum_coreParameterKey")))
@interface CalendrumOkioCalendrum_coreParameterKey<T> : CalendrumOkioBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumOkioCalendrum_coreParameterCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumOkioCalendrum_coreParameterCodec> codec __attribute__((swift_name("codec")));

/** The canonical parameter name in upper case. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
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
__attribute__((swift_name("Calendrum_coreValueType")))
@interface CalendrumOkioCalendrum_coreValueType : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreValueTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ValueType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** How many times a property may occur in a component. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreCardinality")))
@interface CalendrumOkioCalendrum_coreCardinality : CalendrumOkioKotlinEnum<CalendrumOkioCalendrum_coreCardinality *>
+ (instancetype)alloc __attribute__((unavailable));

/** How many times a property may occur in a component. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioCalendrum_coreCardinality *one __attribute__((swift_name("one")));
@property (class, readonly) CalendrumOkioCalendrum_coreCardinality *optional __attribute__((swift_name("optional")));
@property (class, readonly) CalendrumOkioCalendrum_coreCardinality *many __attribute__((swift_name("many")));
@property (class, readonly) CalendrumOkioCalendrum_coreCardinality *oneormore __attribute__((swift_name("oneormore")));
+ (CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreCardinality *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioCalendrum_coreCardinality *> *entries __attribute__((swift_name("entries")));
@end


/** Creates VAlarm values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVAlarm.Companion")))
@interface CalendrumOkioCalendrum_coreVAlarmCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VAlarm values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVAlarmCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVAlarmBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VAlarm, or returns `null` when it is not a `VALARM` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVAlarm * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreAlarmAction")))
@interface CalendrumOkioCalendrum_coreAlarmAction : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreAlarmActionCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a AlarmAction with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
__attribute__((swift_name("Calendrum_coreProximity")))
@interface CalendrumOkioCalendrum_coreProximity : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreProximityCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Proximity with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/**
 * The value of a `TRIGGER` property: when an alarm fires
 * ([RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)).
 *
 * Either a time relative to the start or end of the parent component ([Relative]) or an absolute UTC time ([Absolute]).
 * Immutable.
 */
__attribute__((swift_name("Calendrum_coreTrigger")))
@protocol CalendrumOkioCalendrum_coreTrigger
@required

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumOkioCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/** Creates builders for [Attendee]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAttendee.Companion")))
@interface CalendrumOkioCalendrum_coreAttendeeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Attendee]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreAttendeeCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an attendee with the given calendar [address]. */
- (CalendrumOkioCalendrum_coreAttendeeBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
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
__attribute__((swift_name("Calendrum_coreCuType")))
@interface CalendrumOkioCalendrum_coreCuType : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreCuTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a CuType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
__attribute__((swift_name("Calendrum_corePartStat")))
@interface CalendrumOkioCalendrum_corePartStat : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_corePartStatCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a PartStat with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
__attribute__((swift_name("Calendrum_coreRole")))
@interface CalendrumOkioCalendrum_coreRole : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreRoleCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Role with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of Classification and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreClassification.Companion")))
@interface CalendrumOkioCalendrum_coreClassificationCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Classification and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreClassificationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Classification holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreClassification *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CONFIDENTIAL`. The details are shown only to a limited set of people. */
@property (readonly) CalendrumOkioCalendrum_coreClassification *Confidential __attribute__((swift_name("Confidential")));

/** `PRIVATE`. Only the owner may see the details. */
@property (readonly) CalendrumOkioCalendrum_coreClassification *Private __attribute__((swift_name("Private")));

/** `PUBLIC`. Anyone may see the details; the default. */
@property (readonly) CalendrumOkioCalendrum_coreClassification *Public __attribute__((swift_name("Public")));
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
__attribute__((swift_name("Calendrum_coreConferenceFeature")))
@interface CalendrumOkioCalendrum_coreConferenceFeature : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreConferenceFeatureCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ConferenceFeature with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** Creates VLocation values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVLocation.Companion")))
@interface CalendrumOkioCalendrum_coreVLocationCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VLocation values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVLocationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVLocationBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VLocation, or returns `null` when it is not a `VLOCATION` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVLocation * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/** Creates builders for [Organizer]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreOrganizer.Companion")))
@interface CalendrumOkioCalendrum_coreOrganizerCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Organizer]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreOrganizerCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an organizer with the given calendar [address]. */
- (CalendrumOkioCalendrum_coreOrganizerBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
@end


/** Creates Participant values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParticipant.Companion")))
@interface CalendrumOkioCalendrum_coreParticipantCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates Participant values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreParticipantCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. A `UID` and a participant type are required. */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreParticipantBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a Participant, or returns `null` when it is not a `PARTICIPANT` component. Nothing is
 * copied or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreParticipant * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreParticipantType")))
@interface CalendrumOkioCalendrum_coreParticipantType : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreParticipantTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ParticipantType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
__attribute__((swift_name("Calendrum_coreRelType")))
@interface CalendrumOkioCalendrum_coreRelType : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreRelTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a RelType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** Creates VResource values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVResource.Companion")))
@interface CalendrumOkioCalendrum_coreVResourceCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VResource values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreVResourceCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumOkioCalendrum_coreVResourceBuilder *)builderDefaults:(CalendrumOkioCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VResource, or returns `null` when it is not a `VRESOURCE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumOkioCalendrum_coreVResource * _Nullable)ofComponent:(CalendrumOkioCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
__attribute__((swift_name("Calendrum_coreResourceType")))
@interface CalendrumOkioCalendrum_coreResourceType : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreResourceTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ResourceType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** Creates rule builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRecur.Companion")))
@interface CalendrumOkioCalendrum_coreRecurCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates rule builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreRecurCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for a rule that repeats with the given [frequency]. */
- (CalendrumOkioCalendrum_coreRecurBuilder *)builderFrequency:(CalendrumOkioCalendrum_coreFrequency *)frequency __attribute__((swift_name("builder(frequency:)")));
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
__attribute__((swift_name("Calendrum_coreWeekdayNum")))
@interface CalendrumOkioCalendrum_coreWeekdayNum : CalendrumOkioBase
- (instancetype)initWithOrdinal:(CalendrumOkioInt * _Nullable)ordinal dayOfWeek:(CalendrumOkioKotlinx_datetimeDayOfWeek *)dayOfWeek __attribute__((swift_name("init(ordinal:dayOfWeek:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same number and day of week. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the number and the day of week. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the entry as written in a rule, such as `MO` or `-1FR`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) CalendrumOkioInt * _Nullable ordinal __attribute__((swift_name("ordinal")));
@end


/**
 * An entry of `BYMONTH`: a month number, which with `RSCALE` of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529) can
 * be a leap month, written `5L`.
 *
 * @property month the month number, 1 to 12 in the Gregorian calendar.
 * @property isLeap whether this is a leap month; always false in the Gregorian calendar.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreMonthNum")))
@interface CalendrumOkioCalendrum_coreMonthNum : CalendrumOkioBase
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
 * The `SKIP` rule part of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529#section-4.1.1): what to do with an
 * occurrence that falls on a date that does not exist, such as the 31st in a month with 30 days.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSkip")))
@interface CalendrumOkioCalendrum_coreSkip : CalendrumOkioKotlinEnum<CalendrumOkioCalendrum_coreSkip *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `SKIP` rule part of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529#section-4.1.1): what to do with an
 * occurrence that falls on a date that does not exist, such as the 31st in a month with 30 days.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioCalendrum_coreSkip *omit __attribute__((swift_name("omit")));
@property (class, readonly) CalendrumOkioCalendrum_coreSkip *backward __attribute__((swift_name("backward")));
@property (class, readonly) CalendrumOkioCalendrum_coreSkip *forward __attribute__((swift_name("forward")));
+ (CalendrumOkioKotlinArray<CalendrumOkioCalendrum_coreSkip *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioCalendrum_coreSkip *> *entries __attribute__((swift_name("entries")));
@end


/**
 * A rule part the library does not know, such as an `X-` part or a part registered after RFC 7529, kept so that the
 * rule is written back unchanged.
 *
 * @property name the part name as written, in upper case.
 * @property value the part value as written.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRecurPart")))
@interface CalendrumOkioCalendrum_coreRecurPart : CalendrumOkioBase
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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeek")))
@interface CalendrumOkioKotlinx_datetimeDayOfWeek : CalendrumOkioKotlinEnum<CalendrumOkioKotlinx_datetimeDayOfWeek *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *monday __attribute__((swift_name("monday")));
@property (class, readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *tuesday __attribute__((swift_name("tuesday")));
@property (class, readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *wednesday __attribute__((swift_name("wednesday")));
@property (class, readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *thursday __attribute__((swift_name("thursday")));
@property (class, readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *friday __attribute__((swift_name("friday")));
@property (class, readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *saturday __attribute__((swift_name("saturday")));
@property (class, readonly) CalendrumOkioKotlinx_datetimeDayOfWeek *sunday __attribute__((swift_name("sunday")));
+ (CalendrumOkioKotlinArray<CalendrumOkioKotlinx_datetimeDayOfWeek *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioKotlinx_datetimeDayOfWeek *> *entries __attribute__((swift_name("entries")));
@end


/** The constants of EventStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreEventStatus.Companion")))
@interface CalendrumOkioCalendrum_coreEventStatusCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of EventStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreEventStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new EventStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreEventStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The event is cancelled. */
@property (readonly) CalendrumOkioCalendrum_coreEventStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `CONFIRMED`. The event is confirmed. */
@property (readonly) CalendrumOkioCalendrum_coreEventStatus *Confirmed __attribute__((swift_name("Confirmed")));

/** `TENTATIVE`. The event is tentative. */
@property (readonly) CalendrumOkioCalendrum_coreEventStatus *Tentative __attribute__((swift_name("Tentative")));
@end


/** The constants of Transparency and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTransparency.Companion")))
@interface CalendrumOkioCalendrum_coreTransparencyCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Transparency and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreTransparencyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Transparency holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreTransparency *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `OPAQUE`. The event blocks time; the default. */
@property (readonly) CalendrumOkioCalendrum_coreTransparency *Opaque __attribute__((swift_name("Opaque")));

/** `TRANSPARENT`. The event does not block time. */
@property (readonly) CalendrumOkioCalendrum_coreTransparency *Transparent __attribute__((swift_name("Transparent")));
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
__attribute__((swift_name("Calendrum_coreFbType")))
@interface CalendrumOkioCalendrum_coreFbType : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreFbTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a FbType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of ImageDisplay and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreImageDisplay.Companion")))
@interface CalendrumOkioCalendrum_coreImageDisplayCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ImageDisplay and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreImageDisplayCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ImageDisplay holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreImageDisplay *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BADGE`. A small image shown next to the title. */
@property (readonly) CalendrumOkioCalendrum_coreImageDisplay *Badge __attribute__((swift_name("Badge")));

/** `FULLSIZE`. A full-size version of an image. */
@property (readonly) CalendrumOkioCalendrum_coreImageDisplay *Fullsize __attribute__((swift_name("Fullsize")));

/** `GRAPHIC`. An image shown as a graphic with the content. */
@property (readonly) CalendrumOkioCalendrum_coreImageDisplay *Graphic __attribute__((swift_name("Graphic")));

/** `THUMBNAIL`. A reduced-size version of an image. */
@property (readonly) CalendrumOkioCalendrum_coreImageDisplay *Thumbnail __attribute__((swift_name("Thumbnail")));
@end


/** The constants of JournalStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreJournalStatus.Companion")))
@interface CalendrumOkioCalendrum_coreJournalStatusCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of JournalStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreJournalStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new JournalStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreJournalStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The entry is cancelled. */
@property (readonly) CalendrumOkioCalendrum_coreJournalStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `DRAFT`. The entry is a draft. */
@property (readonly) CalendrumOkioCalendrum_coreJournalStatus *Draft __attribute__((swift_name("Draft")));

/** `FINAL`. The entry is final. */
@property (readonly) CalendrumOkioCalendrum_coreJournalStatus *Final __attribute__((swift_name("Final")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod.Companion")))
@interface CalendrumOkioKotlinx_datetimeDateTimePeriodCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioKotlinx_datetimeDateTimePeriodCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumOkioKotlinx_datetimeDateTimePeriod *)parseText:(NSString *)text __attribute__((swift_name("parse(text:)")));
- (id<CalendrumOkioKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/UtcOffsetSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset")))
@interface CalendrumOkioKotlinx_datetimeUtcOffset : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioKotlinx_datetimeUtcOffsetCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t totalSeconds __attribute__((swift_name("totalSeconds")));
@end


/** The constants of TodoStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTodoStatus.Companion")))
@interface CalendrumOkioCalendrum_coreTodoStatusCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of TodoStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreTodoStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new TodoStatus holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreTodoStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The to-do is cancelled. */
@property (readonly) CalendrumOkioCalendrum_coreTodoStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `COMPLETED`. The to-do is completed. */
@property (readonly) CalendrumOkioCalendrum_coreTodoStatus *Completed __attribute__((swift_name("Completed")));

/** `IN-PROCESS`. The to-do is in progress. */
@property (readonly) CalendrumOkioCalendrum_coreTodoStatus *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The to-do needs action. */
@property (readonly) CalendrumOkioCalendrum_coreTodoStatus *NeedsAction __attribute__((swift_name("NeedsAction")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntArray")))
@interface CalendrumOkioKotlinIntArray : CalendrumOkioBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumOkioInt *(^)(CalendrumOkioInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int32_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumOkioKotlinIntIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int32_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant.Companion")))
@interface CalendrumOkioKotlinInstantCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioKotlinInstantCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumOkioKotlinInstant *)fromEpochMillisecondsEpochMilliseconds:(int64_t)epochMilliseconds __attribute__((swift_name("fromEpochMilliseconds(epochMilliseconds:)")));
- (CalendrumOkioKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment:(int32_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment:)")));
- (CalendrumOkioKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment_:(int64_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment_:)")));
- (CalendrumOkioKotlinInstant *)now __attribute__((swift_name("now()"))) __attribute__((unavailable("Use Clock.System.now() instead")));
- (CalendrumOkioKotlinInstant *)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (CalendrumOkioKotlinInstant * _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@property (readonly) CalendrumOkioKotlinInstant *DISTANT_FUTURE __attribute__((swift_name("DISTANT_FUTURE")));
@property (readonly) CalendrumOkioKotlinInstant *DISTANT_PAST __attribute__((swift_name("DISTANT_PAST")));
@end


/** The two contexts. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext.Companion")))
@interface CalendrumOkioCalendrum_coreDecodeContextCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The two contexts. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreDecodeContextCompanion *shared __attribute__((swift_name("shared")));

/** Reads known quirks of real-world data with a warning and a repair. */
@property (readonly) CalendrumOkioCalendrum_coreDecodeContext *Lenient __attribute__((swift_name("Lenient")));

/** Turns every repair into an error: the value is rejected. */
@property (readonly) CalendrumOkioCalendrum_coreDecodeContext *Strict __attribute__((swift_name("Strict")));
@end


/**
 * A named group of properties and components that a selection needs for some purpose, so that it need not list them one
 * by one.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFeature")))
@interface CalendrumOkioCalendrum_coreFeature : CalendrumOkioBase
@property (class, readonly, getter=companion) CalendrumOkioCalendrum_coreFeatureCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * Reads a parameter into a value of type [T] and writes a value back; the plug-in point for your own parameters.
 *
 * Implementations must be stateless and thread-safe.
 *
 * @note annotations
 *   kotlin.SubclassOptInRequired(markerClass=[NormalClass(value=io/github/calendrum/ExperimentalICalApi)])
*/
__attribute__((swift_name("Calendrum_coreParameterCodec")))
@protocol CalendrumOkioCalendrum_coreParameterCodec
@required

/**
 * Decodes [parameter] of [property], with problems and strictness as [context] says.
 *
 * @param property the property the parameter belongs to.
 * @param parameter the parameter to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumOkioCalendrum_coreDecoded>)decodeProperty:(CalendrumOkioCalendrum_coreICalProperty *)property parameter:(CalendrumOkioCalendrum_coreICalParameter *)parameter context:(CalendrumOkioCalendrum_coreDecodeContext *)context __attribute__((swift_name("decode(property:parameter:context:)")));

/**
 * Encodes [value] into a parameter named by [context].
 *
 * @param value the value to write.
 * @param context carries the parameter name.
 */
- (CalendrumOkioCalendrum_coreICalParameter *)encodeParameterValue:(id)value context:(CalendrumOkioCalendrum_coreEncodeContext *)context __attribute__((swift_name("encodeParameter(value:context:)")));
@end


/** The constants of ValueType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreValueType.Companion")))
@interface CalendrumOkioCalendrum_coreValueTypeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ValueType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreValueTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ValueType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreValueType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BINARY`. Base64 encoded binary data. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Binary __attribute__((swift_name("Binary")));

/** `BOOLEAN`. `TRUE` or `FALSE`. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Boolean __attribute__((swift_name("Boolean")));

/** `CAL-ADDRESS`. A calendar user address. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *CalAddress __attribute__((swift_name("CalAddress")));

/** `DATE`. A calendar date. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Date __attribute__((swift_name("Date")));

/** `DATE-TIME`. A date with a time of day. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *DateTime __attribute__((swift_name("DateTime")));

/** `DURATION`. A length of time. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Duration __attribute__((swift_name("Duration")));

/** `FLOAT`. A decimal number. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Float __attribute__((swift_name("Float")));

/** `INTEGER`. A signed 32-bit integer. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Integer __attribute__((swift_name("Integer")));

/** `PERIOD`. A precise period of time. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Period __attribute__((swift_name("Period")));

/** `RECUR`. A recurrence rule. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Recur __attribute__((swift_name("Recur")));

/** `TEXT`. Human-readable text. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Text __attribute__((swift_name("Text")));

/** `TIME`. A time of day. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Time __attribute__((swift_name("Time")));

/** `UID`. A `UID` reference (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Uid __attribute__((swift_name("Uid")));

/** `URI`. A uniform resource identifier. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *Uri __attribute__((swift_name("Uri")));

/** `UTC-OFFSET`. An offset from UTC. */
@property (readonly) CalendrumOkioCalendrum_coreValueType *UtcOffset __attribute__((swift_name("UtcOffset")));

/** `XML-REFERENCE`. A reference to an XML element (RFC 9073). */
@property (readonly) CalendrumOkioCalendrum_coreValueType *XmlReference __attribute__((swift_name("XmlReference")));
@end


/** The constants of AlarmAction and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAlarmAction.Companion")))
@interface CalendrumOkioCalendrum_coreAlarmActionCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of AlarmAction and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreAlarmActionCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new AlarmAction holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreAlarmAction *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Plays a sound. */
@property (readonly) CalendrumOkioCalendrum_coreAlarmAction *Audio __attribute__((swift_name("Audio")));

/** `DISPLAY`. Shows a message. */
@property (readonly) CalendrumOkioCalendrum_coreAlarmAction *Display __attribute__((swift_name("Display")));

/** `EMAIL`. Sends an e-mail. */
@property (readonly) CalendrumOkioCalendrum_coreAlarmAction *Email __attribute__((swift_name("Email")));
@end


/** The constants of Proximity and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProximity.Companion")))
@interface CalendrumOkioCalendrum_coreProximityCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Proximity and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreProximityCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Proximity holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreProximity *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ARRIVE`. Fires when the user arrives at the location. */
@property (readonly) CalendrumOkioCalendrum_coreProximity *Arrive __attribute__((swift_name("Arrive")));

/** `CONNECT`. Fires when the user's device connects, for example to a car. */
@property (readonly) CalendrumOkioCalendrum_coreProximity *Connect __attribute__((swift_name("Connect")));

/** `DEPART`. Fires when the user leaves the location. */
@property (readonly) CalendrumOkioCalendrum_coreProximity *Depart __attribute__((swift_name("Depart")));

/** `DISCONNECT`. Fires when the user's device disconnects. */
@property (readonly) CalendrumOkioCalendrum_coreProximity *Disconnect __attribute__((swift_name("Disconnect")));
@end


/** The constants of CuType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreCuType.Companion")))
@interface CalendrumOkioCalendrum_coreCuTypeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of CuType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreCuTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new CuType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreCuType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `GROUP`. A group of people. */
@property (readonly) CalendrumOkioCalendrum_coreCuType *Group __attribute__((swift_name("Group")));

/** `INDIVIDUAL`. A person; the default. */
@property (readonly) CalendrumOkioCalendrum_coreCuType *Individual __attribute__((swift_name("Individual")));

/** `RESOURCE`. A resource other than a room, such as a projector. */
@property (readonly) CalendrumOkioCalendrum_coreCuType *Resource __attribute__((swift_name("Resource")));

/** `ROOM`. A room. */
@property (readonly) CalendrumOkioCalendrum_coreCuType *Room __attribute__((swift_name("Room")));

/** `UNKNOWN`. The type is not known. */
@property (readonly) CalendrumOkioCalendrum_coreCuType *Unknown __attribute__((swift_name("Unknown")));
@end


/** The constants of PartStat and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_corePartStat.Companion")))
@interface CalendrumOkioCalendrum_corePartStatCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of PartStat and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_corePartStatCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new PartStat holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_corePartStat *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACCEPTED`. The user will attend. */
@property (readonly) CalendrumOkioCalendrum_corePartStat *Accepted __attribute__((swift_name("Accepted")));

/** `COMPLETED`. The user completed the task (to-dos). */
@property (readonly) CalendrumOkioCalendrum_corePartStat *Completed __attribute__((swift_name("Completed")));

/** `DECLINED`. The user will not attend. */
@property (readonly) CalendrumOkioCalendrum_corePartStat *Declined __attribute__((swift_name("Declined")));

/** `DELEGATED`. The user delegated attendance to someone else. */
@property (readonly) CalendrumOkioCalendrum_corePartStat *Delegated __attribute__((swift_name("Delegated")));

/** `IN-PROCESS`. The user is working on the task (to-dos). */
@property (readonly) CalendrumOkioCalendrum_corePartStat *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The user has not answered yet; the default. */
@property (readonly) CalendrumOkioCalendrum_corePartStat *NeedsAction __attribute__((swift_name("NeedsAction")));

/** `TENTATIVE`. The user may attend. */
@property (readonly) CalendrumOkioCalendrum_corePartStat *Tentative __attribute__((swift_name("Tentative")));
@end


/** The constants of Role and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRole.Companion")))
@interface CalendrumOkioCalendrum_coreRoleCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Role and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreRoleCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Role holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreRole *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHAIR`. The user chairs the event. */
@property (readonly) CalendrumOkioCalendrum_coreRole *Chair __attribute__((swift_name("Chair")));

/** `NON-PARTICIPANT`. Informed only; does not take part. */
@property (readonly) CalendrumOkioCalendrum_coreRole *NonParticipant __attribute__((swift_name("NonParticipant")));

/** `OPT-PARTICIPANT`. An optional participant. */
@property (readonly) CalendrumOkioCalendrum_coreRole *OptParticipant __attribute__((swift_name("OptParticipant")));

/** `REQ-PARTICIPANT`. A required participant; the default. */
@property (readonly) CalendrumOkioCalendrum_coreRole *ReqParticipant __attribute__((swift_name("ReqParticipant")));
@end


/** The constants of ConferenceFeature and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreConferenceFeature.Companion")))
@interface CalendrumOkioCalendrum_coreConferenceFeatureCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ConferenceFeature and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreConferenceFeatureCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ConferenceFeature holding the upper-cased token
 * when it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreConferenceFeature *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Audio conferencing. */
@property (readonly) CalendrumOkioCalendrum_coreConferenceFeature *Audio __attribute__((swift_name("Audio")));

/** `CHAT`. Chat or instant messaging. */
@property (readonly) CalendrumOkioCalendrum_coreConferenceFeature *Chat __attribute__((swift_name("Chat")));

/** `FEED`. A blog or atom feed. */
@property (readonly) CalendrumOkioCalendrum_coreConferenceFeature *Feed __attribute__((swift_name("Feed")));

/** `MODERATOR`. Moderator dial-in. */
@property (readonly) CalendrumOkioCalendrum_coreConferenceFeature *Moderator __attribute__((swift_name("Moderator")));

/** `PHONE`. Phone conferencing. */
@property (readonly) CalendrumOkioCalendrum_coreConferenceFeature *Phone __attribute__((swift_name("Phone")));

/** `SCREEN`. Screen sharing. */
@property (readonly) CalendrumOkioCalendrum_coreConferenceFeature *Screen __attribute__((swift_name("Screen")));

/** `VIDEO`. Video conferencing. */
@property (readonly) CalendrumOkioCalendrum_coreConferenceFeature *Video __attribute__((swift_name("Video")));
@end


/** The constants of ParticipantType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParticipantType.Companion")))
@interface CalendrumOkioCalendrum_coreParticipantTypeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ParticipantType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreParticipantTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ParticipantType holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreParticipantType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACTIVE`. An active participant. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *Active __attribute__((swift_name("Active")));

/** `BOOKING-CONTACT`. A contact for bookings or reservations. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *BookingContact __attribute__((swift_name("BookingContact")));

/** `CONTACT`. A contact for information. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *Contact __attribute__((swift_name("Contact")));

/** `EMERGENCY-CONTACT`. A contact in an emergency. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *EmergencyContact __attribute__((swift_name("EmergencyContact")));

/** `INACTIVE`. An inactive participant. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *Inactive __attribute__((swift_name("Inactive")));

/** `PERFORMER`. A performer, such as a musician. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *Performer __attribute__((swift_name("Performer")));

/** `PLANNER-CONTACT`. A contact for the planning of the activity. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *PlannerContact __attribute__((swift_name("PlannerContact")));

/** `PUBLICITY-CONTACT`. A contact for publicity. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *PublicityContact __attribute__((swift_name("PublicityContact")));

/** `SPEAKER`. A speaker. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *Speaker __attribute__((swift_name("Speaker")));

/** `SPONSOR`. A sponsor of the activity. */
@property (readonly) CalendrumOkioCalendrum_coreParticipantType *Sponsor __attribute__((swift_name("Sponsor")));
@end


/** The constants of RelType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRelType.Companion")))
@interface CalendrumOkioCalendrum_coreRelTypeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of RelType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreRelTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new RelType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreRelType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHILD`. The related component is a child of this one. */
@property (readonly) CalendrumOkioCalendrum_coreRelType *Child __attribute__((swift_name("Child")));

/** `CONCEPT`. The related value is a `CONCEPT` URI, not a `UID` (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *Concept __attribute__((swift_name("Concept")));

/** `DEPENDS-ON`. This component depends on the related one (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *DependsOn __attribute__((swift_name("DependsOn")));

/** `FINISHTOFINISH`. A finish-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *FinishToFinish __attribute__((swift_name("FinishToFinish")));

/** `FINISHTOSTART`. A finish-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *FinishToStart __attribute__((swift_name("FinishToStart")));

/** `FIRST`. The related component is the first of a series of components (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *First __attribute__((swift_name("First")));

/** `NEXT`. The related component is the next of a series of components (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *Next __attribute__((swift_name("Next")));

/** `PARENT`. The related component is the parent of this one; the default. */
@property (readonly) CalendrumOkioCalendrum_coreRelType *Parent __attribute__((swift_name("Parent")));

/** `REFID`. The related value is a `REFID` reference identifier, not a `UID` (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *RefId __attribute__((swift_name("RefId")));

/** `SIBLING`. The related component is a sibling of this one. */
@property (readonly) CalendrumOkioCalendrum_coreRelType *Sibling __attribute__((swift_name("Sibling")));

/** `SNOOZE`. This alarm is a snooze of the related alarm (RFC 9074). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *Snooze __attribute__((swift_name("Snooze")));

/** `STARTTOFINISH`. A start-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *StartToFinish __attribute__((swift_name("StartToFinish")));

/** `STARTTOSTART`. A start-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumOkioCalendrum_coreRelType *StartToStart __attribute__((swift_name("StartToStart")));
@end


/** The constants of ResourceType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreResourceType.Companion")))
@interface CalendrumOkioCalendrum_coreResourceTypeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ResourceType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreResourceTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ResourceType holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreResourceType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `PROJECTOR`. A projector. */
@property (readonly) CalendrumOkioCalendrum_coreResourceType *Projector __attribute__((swift_name("Projector")));

/** `REMOTE-CONFERENCE-AUDIO`. A remote conference audio channel. */
@property (readonly) CalendrumOkioCalendrum_coreResourceType *RemoteConferenceAudio __attribute__((swift_name("RemoteConferenceAudio")));

/** `REMOTE-CONFERENCE-VIDEO`. A remote conference video channel. */
@property (readonly) CalendrumOkioCalendrum_coreResourceType *RemoteConferenceVideo __attribute__((swift_name("RemoteConferenceVideo")));

/** `ROOM`. A room. */
@property (readonly) CalendrumOkioCalendrum_coreResourceType *Room __attribute__((swift_name("Room")));
@end


/** The constants of FbType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFbType.Companion")))
@interface CalendrumOkioCalendrum_coreFbTypeCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of FbType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreFbTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new FbType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumOkioCalendrum_coreFbType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BUSY`. The time is busy; the default. */
@property (readonly) CalendrumOkioCalendrum_coreFbType *Busy __attribute__((swift_name("Busy")));

/** `BUSY-TENTATIVE`. The time is tentatively busy. */
@property (readonly) CalendrumOkioCalendrum_coreFbType *BusyTentative __attribute__((swift_name("BusyTentative")));

/** `BUSY-UNAVAILABLE`. The time is busy and unavailable. */
@property (readonly) CalendrumOkioCalendrum_coreFbType *BusyUnavailable __attribute__((swift_name("BusyUnavailable")));

/** `FREE`. The time is free. */
@property (readonly) CalendrumOkioCalendrum_coreFbType *Free __attribute__((swift_name("Free")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol CalendrumOkioKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<CalendrumOkioKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol CalendrumOkioKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<CalendrumOkioKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol CalendrumOkioKotlinx_serialization_coreKSerializer <CalendrumOkioKotlinx_serialization_coreSerializationStrategy, CalendrumOkioKotlinx_serialization_coreDeserializationStrategy>
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset.Companion")))
@interface CalendrumOkioKotlinx_datetimeUtcOffsetCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioKotlinx_datetimeUtcOffsetCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumOkioKotlinx_datetimeDateTimeFormat>)FormatBlock:(void (^)(id<CalendrumOkioKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset>))block __attribute__((swift_name("Format(block:)")));
- (CalendrumOkioKotlinx_datetimeUtcOffset *)parseInput:(id)input format:(id<CalendrumOkioKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumOkioKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) CalendrumOkioKotlinx_datetimeUtcOffset *ZERO __attribute__((swift_name("ZERO")));
@end

__attribute__((swift_name("KotlinIntIterator")))
@interface CalendrumOkioKotlinIntIterator : CalendrumOkioBase <CalendrumOkioKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumOkioInt *)next __attribute__((swift_name("next()")));
- (int32_t)nextInt __attribute__((swift_name("nextInt()")));
@end


/** The features of [Selection.Builder.need]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFeature.Companion")))
@interface CalendrumOkioCalendrum_coreFeatureCompanion : CalendrumOkioBase
+ (instancetype)alloc __attribute__((unavailable));

/** The features of [Selection.Builder.need]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumOkioCalendrum_coreFeatureCompanion *shared __attribute__((swift_name("shared")));

/** The `VALARM` components, whole. */
@property (readonly) CalendrumOkioCalendrum_coreFeature *Alarms __attribute__((swift_name("Alarms")));

/**
 * Everything needed to compute occurrences: `DTSTART`, `DTEND`, `DURATION`, `RRULE`, `RDATE`, `EXDATE` and
 * `RECURRENCE-ID`. Time zones are then at least [TimeZoneSelection.Referenced].
 */
@property (readonly) CalendrumOkioCalendrum_coreFeature *Occurrences __attribute__((swift_name("Occurrences")));

/** The `ATTENDEE` and `ORGANIZER` properties. */
@property (readonly) CalendrumOkioCalendrum_coreFeature *Participants __attribute__((swift_name("Participants")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol CalendrumOkioKotlinx_serialization_coreEncoder
@required
- (id<CalendrumOkioKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<CalendrumOkioKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<CalendrumOkioKotlinx_serialization_coreEncoder>)encodeInlineDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("encodeInline(descriptor:)")));
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
- (void)encodeNullableSerializableValueSerializer:(id<CalendrumOkioKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<CalendrumOkioKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) CalendrumOkioKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol CalendrumOkioKotlinx_serialization_coreSerialDescriptor
@required

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSArray<id<CalendrumOkioKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));

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
@property (readonly) NSArray<id<CalendrumOkioKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));

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
@property (readonly) CalendrumOkioKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol CalendrumOkioKotlinx_serialization_coreDecoder
@required
- (id<CalendrumOkioKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<CalendrumOkioKotlinx_serialization_coreDecoder>)decodeInlineDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeInline(descriptor:)")));
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
- (CalendrumOkioKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<CalendrumOkioKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<CalendrumOkioKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) CalendrumOkioKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormat")))
@protocol CalendrumOkioKotlinx_datetimeDateTimeFormat
@required
- (NSString *)formatValue:(id _Nullable)value __attribute__((swift_name("format(value:)")));
- (id<CalendrumOkioKotlinAppendable>)formatToAppendable:(id<CalendrumOkioKotlinAppendable>)appendable value:(id _Nullable)value __attribute__((swift_name("formatTo(appendable:value:)")));
- (id _Nullable)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (id _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilder")))
@protocol CalendrumOkioKotlinx_datetimeDateTimeFormatBuilder
@required
- (void)charsValue:(NSString *)value __attribute__((swift_name("chars(value:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithUtcOffset")))
@protocol CalendrumOkioKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset <CalendrumOkioKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)offsetFormat:(id<CalendrumOkioKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("offset(format:)")));
- (void)offsetHoursPadding:(CalendrumOkioKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetHours(padding:)")));
- (void)offsetMinutesOfHourPadding:(CalendrumOkioKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetMinutesOfHour(padding:)")));
- (void)offsetSecondsOfMinutePadding:(CalendrumOkioKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetSecondsOfMinute(padding:)")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol CalendrumOkioKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<CalendrumOkioKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumOkioKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumOkioKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) CalendrumOkioKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface CalendrumOkioKotlinx_serialization_coreSerializersModule : CalendrumOkioBase

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)dumpToCollector:(id<CalendrumOkioKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumOkioKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<CalendrumOkioKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<CalendrumOkioKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumOkioKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumOkioKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumOkioKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumOkioKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end

__attribute__((swift_name("KotlinAnnotation")))
@protocol CalendrumOkioKotlinAnnotation
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface CalendrumOkioKotlinx_serialization_coreSerialKind : CalendrumOkioBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol CalendrumOkioKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<CalendrumOkioKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumOkioKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumOkioKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<CalendrumOkioKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) CalendrumOkioKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface CalendrumOkioKotlinNothing : CalendrumOkioBase
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimePadding")))
@interface CalendrumOkioKotlinx_datetimePadding : CalendrumOkioKotlinEnum<CalendrumOkioKotlinx_datetimePadding *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumOkioKotlinx_datetimePadding *none __attribute__((swift_name("none")));
@property (class, readonly) CalendrumOkioKotlinx_datetimePadding *zero __attribute__((swift_name("zero")));
@property (class, readonly) CalendrumOkioKotlinx_datetimePadding *space __attribute__((swift_name("space")));
+ (CalendrumOkioKotlinArray<CalendrumOkioKotlinx_datetimePadding *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumOkioKotlinx_datetimePadding *> *entries __attribute__((swift_name("entries")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol CalendrumOkioKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<CalendrumOkioKotlinKClass>)kClass provider:(id<CalendrumOkioKotlinx_serialization_coreKSerializer> (^)(NSArray<id<CalendrumOkioKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<CalendrumOkioKotlinKClass>)kClass serializer:(id<CalendrumOkioKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<CalendrumOkioKotlinKClass>)baseClass actualClass:(id<CalendrumOkioKotlinKClass>)actualClass actualSerializer:(id<CalendrumOkioKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<CalendrumOkioKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumOkioKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultDeserializerProvider:)"))) __attribute__((deprecated("Deprecated in favor of function with more precise name: polymorphicDefaultDeserializer")));
- (void)polymorphicDefaultDeserializerBaseClass:(id<CalendrumOkioKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumOkioKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefaultDeserializer(baseClass:defaultDeserializerProvider:)")));
- (void)polymorphicDefaultSerializerBaseClass:(id<CalendrumOkioKotlinKClass>)baseClass defaultSerializerProvider:(id<CalendrumOkioKotlinx_serialization_coreSerializationStrategy> _Nullable (^)(id))defaultSerializerProvider __attribute__((swift_name("polymorphicDefaultSerializer(baseClass:defaultSerializerProvider:)")));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol CalendrumOkioKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol CalendrumOkioKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol CalendrumOkioKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol CalendrumOkioKotlinKClass <CalendrumOkioKotlinKDeclarationContainer, CalendrumOkioKotlinKAnnotatedElement, CalendrumOkioKotlinKClassifier>
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

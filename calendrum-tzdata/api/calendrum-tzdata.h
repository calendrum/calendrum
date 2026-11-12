#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class CalendrumTzdataCalendrum_coreAlarmAction, CalendrumTzdataCalendrum_coreAlarmActionCompanion, CalendrumTzdataCalendrum_coreAttendee, CalendrumTzdataCalendrum_coreAttendeeBuilder, CalendrumTzdataCalendrum_coreAttendeeCompanion, CalendrumTzdataCalendrum_coreAttendeeList, CalendrumTzdataCalendrum_coreBuildDefaults, CalendrumTzdataCalendrum_coreClassification, CalendrumTzdataCalendrum_coreClassificationCompanion, CalendrumTzdataCalendrum_coreComponentBuilder<B>, CalendrumTzdataCalendrum_coreComponentList<T>, CalendrumTzdataCalendrum_coreConference, CalendrumTzdataCalendrum_coreConferenceFeature, CalendrumTzdataCalendrum_coreConferenceFeatureCompanion, CalendrumTzdataCalendrum_coreCuType, CalendrumTzdataCalendrum_coreCuTypeCompanion, CalendrumTzdataCalendrum_coreDecodeContext, CalendrumTzdataCalendrum_coreDecodeContextCompanion, CalendrumTzdataCalendrum_coreEncodeContext, CalendrumTzdataCalendrum_coreEventStatus, CalendrumTzdataCalendrum_coreEventStatusCompanion, CalendrumTzdataCalendrum_coreFbType, CalendrumTzdataCalendrum_coreFbTypeCompanion, CalendrumTzdataCalendrum_coreFreeBusy, CalendrumTzdataCalendrum_coreFrequency, CalendrumTzdataCalendrum_coreGeo, CalendrumTzdataCalendrum_coreICalComponent, CalendrumTzdataCalendrum_coreICalComponentBuilder, CalendrumTzdataCalendrum_coreICalComponentCompanion, CalendrumTzdataCalendrum_coreICalDuration, CalendrumTzdataCalendrum_coreICalDurationCompanion, CalendrumTzdataCalendrum_coreICalParameter, CalendrumTzdataCalendrum_coreICalParameterCompanion, CalendrumTzdataCalendrum_coreICalPeriod, CalendrumTzdataCalendrum_coreICalProperty, CalendrumTzdataCalendrum_coreICalPropertyBuilder, CalendrumTzdataCalendrum_coreICalPropertyCompanion, CalendrumTzdataCalendrum_coreImage, CalendrumTzdataCalendrum_coreImageDisplay, CalendrumTzdataCalendrum_coreImageDisplayCompanion, CalendrumTzdataCalendrum_coreJournalStatus, CalendrumTzdataCalendrum_coreJournalStatusCompanion, CalendrumTzdataCalendrum_coreLink, CalendrumTzdataCalendrum_coreMethod, CalendrumTzdataCalendrum_coreMethodCompanion, CalendrumTzdataCalendrum_coreMonthNum, CalendrumTzdataCalendrum_coreOrganizer, CalendrumTzdataCalendrum_coreOrganizerBuilder, CalendrumTzdataCalendrum_coreOrganizerCompanion, CalendrumTzdataCalendrum_corePartStat, CalendrumTzdataCalendrum_corePartStatCompanion, CalendrumTzdataCalendrum_coreParticipant, CalendrumTzdataCalendrum_coreParticipantBuilder, CalendrumTzdataCalendrum_coreParticipantCompanion, CalendrumTzdataCalendrum_coreParticipantType, CalendrumTzdataCalendrum_coreParticipantTypeCompanion, CalendrumTzdataCalendrum_coreProblem, CalendrumTzdataCalendrum_coreProblemCode, CalendrumTzdataCalendrum_coreProblemCodeCompanion, CalendrumTzdataCalendrum_corePropertyKey<T>, CalendrumTzdataCalendrum_coreProximity, CalendrumTzdataCalendrum_coreProximityCompanion, CalendrumTzdataCalendrum_coreRecur, CalendrumTzdataCalendrum_coreRecurBuilder, CalendrumTzdataCalendrum_coreRecurCompanion, CalendrumTzdataCalendrum_coreRecurPart, CalendrumTzdataCalendrum_coreRelType, CalendrumTzdataCalendrum_coreRelTypeCompanion, CalendrumTzdataCalendrum_coreRelatedTo, CalendrumTzdataCalendrum_coreRequestStatus, CalendrumTzdataCalendrum_coreResolveContext, CalendrumTzdataCalendrum_coreResolveContextCompanion, CalendrumTzdataCalendrum_coreResourceType, CalendrumTzdataCalendrum_coreResourceTypeCompanion, CalendrumTzdataCalendrum_coreRole, CalendrumTzdataCalendrum_coreRoleCompanion, CalendrumTzdataCalendrum_coreSeverity, CalendrumTzdataCalendrum_coreSkip, CalendrumTzdataCalendrum_coreSourceSpan, CalendrumTzdataCalendrum_coreStructuredData, CalendrumTzdataCalendrum_coreStyledDescription, CalendrumTzdataCalendrum_coreTodoStatus, CalendrumTzdataCalendrum_coreTodoStatusCompanion, CalendrumTzdataCalendrum_coreTransparency, CalendrumTzdataCalendrum_coreTransparencyCompanion, CalendrumTzdataCalendrum_coreVAlarm, CalendrumTzdataCalendrum_coreVAlarmBuilder, CalendrumTzdataCalendrum_coreVAlarmCompanion, CalendrumTzdataCalendrum_coreVCalendar, CalendrumTzdataCalendrum_coreVCalendarBuilder, CalendrumTzdataCalendrum_coreVCalendarCompanion, CalendrumTzdataCalendrum_coreVEvent, CalendrumTzdataCalendrum_coreVEventBuilder, CalendrumTzdataCalendrum_coreVEventCompanion, CalendrumTzdataCalendrum_coreVFreeBusy, CalendrumTzdataCalendrum_coreVFreeBusyBuilder, CalendrumTzdataCalendrum_coreVFreeBusyCompanion, CalendrumTzdataCalendrum_coreVJournal, CalendrumTzdataCalendrum_coreVJournalBuilder, CalendrumTzdataCalendrum_coreVJournalCompanion, CalendrumTzdataCalendrum_coreVLocation, CalendrumTzdataCalendrum_coreVLocationBuilder, CalendrumTzdataCalendrum_coreVLocationCompanion, CalendrumTzdataCalendrum_coreVResource, CalendrumTzdataCalendrum_coreVResourceBuilder, CalendrumTzdataCalendrum_coreVResourceCompanion, CalendrumTzdataCalendrum_coreVTimeZone, CalendrumTzdataCalendrum_coreVTimeZoneBuilder, CalendrumTzdataCalendrum_coreVTimeZoneCompanion, CalendrumTzdataCalendrum_coreVTimeZoneObservance, CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder, CalendrumTzdataCalendrum_coreVTodo, CalendrumTzdataCalendrum_coreVTodoBuilder, CalendrumTzdataCalendrum_coreVTodoCompanion, CalendrumTzdataCalendrum_coreValueType, CalendrumTzdataCalendrum_coreValueTypeCompanion, CalendrumTzdataCalendrum_coreWeekdayNum, CalendrumTzdataCalendrum_coreZoneTransition, CalendrumTzdataKotlinArray<T>, CalendrumTzdataKotlinByteArray, CalendrumTzdataKotlinByteIterator, CalendrumTzdataKotlinEnum<E>, CalendrumTzdataKotlinEnumCompanion, CalendrumTzdataKotlinException, CalendrumTzdataKotlinIllegalArgumentException, CalendrumTzdataKotlinInstant, CalendrumTzdataKotlinInstantCompanion, CalendrumTzdataKotlinIntArray, CalendrumTzdataKotlinIntIterator, CalendrumTzdataKotlinNothing, CalendrumTzdataKotlinRuntimeException, CalendrumTzdataKotlinThrowable, CalendrumTzdataKotlinx_datetimeDateTimePeriod, CalendrumTzdataKotlinx_datetimeDateTimePeriodCompanion, CalendrumTzdataKotlinx_datetimeDayOfWeek, CalendrumTzdataKotlinx_datetimeDayOfWeekNames, CalendrumTzdataKotlinx_datetimeDayOfWeekNamesCompanion, CalendrumTzdataKotlinx_datetimeLocalDate, CalendrumTzdataKotlinx_datetimeLocalDateCompanion, CalendrumTzdataKotlinx_datetimeLocalDateProgression, CalendrumTzdataKotlinx_datetimeLocalDateProgressionCompanion, CalendrumTzdataKotlinx_datetimeLocalDateRange, CalendrumTzdataKotlinx_datetimeLocalDateRangeCompanion, CalendrumTzdataKotlinx_datetimeLocalDateTime, CalendrumTzdataKotlinx_datetimeLocalDateTimeCompanion, CalendrumTzdataKotlinx_datetimeLocalTime, CalendrumTzdataKotlinx_datetimeLocalTimeCompanion, CalendrumTzdataKotlinx_datetimeMonth, CalendrumTzdataKotlinx_datetimeMonthNames, CalendrumTzdataKotlinx_datetimeMonthNamesCompanion, CalendrumTzdataKotlinx_datetimePadding, CalendrumTzdataKotlinx_datetimeUtcOffset, CalendrumTzdataKotlinx_datetimeUtcOffsetCompanion, CalendrumTzdataKotlinx_serialization_coreSerialKind, CalendrumTzdataKotlinx_serialization_coreSerializersModule, CalendrumTzdataTzData, CalendrumTzdataVTimeZoneMode;

@protocol CalendrumTzdataCalendrum_coreAttach, CalendrumTzdataCalendrum_coreDecoded, CalendrumTzdataCalendrum_coreICalDateTime, CalendrumTzdataCalendrum_coreICalTimeZone, CalendrumTzdataCalendrum_coreProblemListener, CalendrumTzdataCalendrum_corePropertyCodec, CalendrumTzdataCalendrum_coreTimeZoneResolver, CalendrumTzdataCalendrum_coreTrigger, CalendrumTzdataCalendrum_coreUidGenerator, CalendrumTzdataCalendrum_coreVTimeZoneSource, CalendrumTzdataKotlinAnnotation, CalendrumTzdataKotlinAppendable, CalendrumTzdataKotlinClock, CalendrumTzdataKotlinClosedRange, CalendrumTzdataKotlinCollection, CalendrumTzdataKotlinComparable, CalendrumTzdataKotlinIterable, CalendrumTzdataKotlinIterator, CalendrumTzdataKotlinKAnnotatedElement, CalendrumTzdataKotlinKClass, CalendrumTzdataKotlinKClassifier, CalendrumTzdataKotlinKDeclarationContainer, CalendrumTzdataKotlinOpenEndRange, CalendrumTzdataKotlinx_datetimeDateTimeFormat, CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilder, CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithDate, CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithDateTime, CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithTime, CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset, CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithYearMonth, CalendrumTzdataKotlinx_serialization_coreCompositeDecoder, CalendrumTzdataKotlinx_serialization_coreCompositeEncoder, CalendrumTzdataKotlinx_serialization_coreDecoder, CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy, CalendrumTzdataKotlinx_serialization_coreEncoder, CalendrumTzdataKotlinx_serialization_coreKSerializer, CalendrumTzdataKotlinx_serialization_coreSerialDescriptor, CalendrumTzdataKotlinx_serialization_coreSerializationStrategy, CalendrumTzdataKotlinx_serialization_coreSerializersModuleCollector;

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
@interface CalendrumTzdataBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface CalendrumTzdataBase (CalendrumTzdataBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface CalendrumTzdataMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface CalendrumTzdataMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorCalendrumTzdataKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface CalendrumTzdataNumber : NSNumber
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
@interface CalendrumTzdataByte : CalendrumTzdataNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface CalendrumTzdataUByte : CalendrumTzdataNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface CalendrumTzdataShort : CalendrumTzdataNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface CalendrumTzdataUShort : CalendrumTzdataNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface CalendrumTzdataInt : CalendrumTzdataNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface CalendrumTzdataUInt : CalendrumTzdataNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface CalendrumTzdataLong : CalendrumTzdataNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface CalendrumTzdataULong : CalendrumTzdataNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface CalendrumTzdataFloat : CalendrumTzdataNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface CalendrumTzdataDouble : CalendrumTzdataNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface CalendrumTzdataBoolean : CalendrumTzdataNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end


/**
 * Supplies a `VTIMEZONE` component for a time zone identifier, so that a written calendar can be self-contained ([RFC
 * 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 *
 * Used by `WriteOptions.Builder.addMissingTimeZones`. The `calendrum-tzdata` module provides one for every IANA zone.
 */
__attribute__((swift_name("Calendrum_coreVTimeZoneSource")))
@protocol CalendrumTzdataCalendrum_coreVTimeZoneSource
@required

/**
 * Returns the `VTIMEZONE` for [tzid], or `null` when this source does not know the zone.
 *
 * @param tzid the value of a `TZID` parameter.
 */
- (CalendrumTzdataCalendrum_coreICalComponent * _Nullable)vtimezoneTzid:(NSString *)tzid __attribute__((swift_name("vtimezone(tzid:)")));
@end


/**
 * The IANA time zone database, packed into the library (the pinned release is listed in `NOTICE`): every zone, as a
 * resolver and as a source of `VTIMEZONE` components. It gives the same results on every platform, since it does not
 * use the platform database.
 *
 * Add [resolver] to a chain of time zone resolvers to resolve `TZID`s with it, or pass [TzData] to
 * `WriteOptions.Builder.addMissingTimeZones` to write the `VTIMEZONE` components a calendar needs.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TzData")))
@interface CalendrumTzdataTzData : CalendrumTzdataBase <CalendrumTzdataCalendrum_coreVTimeZoneSource>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The IANA time zone database, packed into the library (the pinned release is listed in `NOTICE`): every zone, as a
 * resolver and as a source of `VTIMEZONE` components. It gives the same results on every platform, since it does not
 * use the platform database.
 *
 * Add [resolver] to a chain of time zone resolvers to resolve `TZID`s with it, or pass [TzData] to
 * `WriteOptions.Builder.addMissingTimeZones` to write the `VTIMEZONE` components a calendar needs.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)tzData __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataTzData *shared __attribute__((swift_name("shared")));

/** Returns the name and the release of the database. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Returns a `VTIMEZONE` component for [tzid], or `null` for an unknown zone: the Outlook-compatible form when the
 * zone follows its current rules with one `RRULE` each, and the full form otherwise.
 *
 * @param tzid an IANA identifier, or an alias the library knows.
 */
- (CalendrumTzdataCalendrum_coreICalComponent * _Nullable)vtimezoneTzid:(NSString *)tzid __attribute__((swift_name("vtimezone(tzid:)")));

/**
 * Returns a `VTIMEZONE` component for [tzid] in the given [mode], or `null` when the zone is unknown.
 *
 * @param tzid an IANA identifier, or an alias the library knows.
 * @param mode how to describe the rules.
 * @param from the first moment the component must cover, or `null` for the whole history; earlier history is left
 *   out in [VTimeZoneMode.Full].
 */
- (CalendrumTzdataCalendrum_coreVTimeZone * _Nullable)vtimezoneTzid:(NSString *)tzid mode:(CalendrumTzdataVTimeZoneMode *)mode from:(CalendrumTzdataKotlinInstant * _Nullable)from __attribute__((swift_name("vtimezone(tzid:mode:from:)")));

/**
 * Returns a `VTIMEZONE` component for [tzid] in the given [mode] and reports `ICAL-Z005` to [problemListener] when
 * [VTimeZoneMode.OutlookCompatible] can only approximate the rules.
 *
 * @param tzid an IANA identifier, or an alias the library knows.
 * @param mode how to describe the rules.
 * @param from the first moment the component must cover, or `null` for the whole history.
 * @param problemListener receives `ICAL-Z005`, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTimeZone * _Nullable)vtimezoneTzid:(NSString *)tzid mode:(CalendrumTzdataVTimeZoneMode *)mode from:(CalendrumTzdataKotlinInstant * _Nullable)from problemListener:(id<CalendrumTzdataCalendrum_coreProblemListener> _Nullable)problemListener __attribute__((swift_name("vtimezone(tzid:mode:from:problemListener:)")));

/**
 * Returns the zone with the identifier [tzid], or `null` when the database has no such zone. Aliases are not
 * resolved here; use [resolver].
 *
 * @param tzid an IANA identifier such as `Europe/Berlin`.
 */
- (id<CalendrumTzdataCalendrum_coreICalTimeZone> _Nullable)zoneTzid:(NSString *)tzid __attribute__((swift_name("zone(tzid:)")));

/**
 * A resolver for IANA zone names, and for the aliases of the library (Windows names and links), backed by this
 * module's tables.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreTimeZoneResolver> resolver __attribute__((swift_name("resolver")));

/** The release of the IANA time zone database in this module, such as `2026b`. */
@property (readonly) NSString *version __attribute__((swift_name("version")));

/** The identifiers of all zones in the database. */
@property (readonly) NSSet<NSString *> *zoneIds __attribute__((swift_name("zoneIds")));
@end

__attribute__((swift_name("KotlinComparable")))
@protocol CalendrumTzdataKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface CalendrumTzdataKotlinEnum<E> : CalendrumTzdataBase <CalendrumTzdataKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end


/** How [TzData.vtimezone] describes a zone. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VTimeZoneMode")))
@interface CalendrumTzdataVTimeZoneMode : CalendrumTzdataKotlinEnum<CalendrumTzdataVTimeZoneMode *>
+ (instancetype)alloc __attribute__((unavailable));

/** How [TzData.vtimezone] describes a zone. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumTzdataVTimeZoneMode *full __attribute__((swift_name("full")));
@property (class, readonly) CalendrumTzdataVTimeZoneMode *outlookcompatible __attribute__((swift_name("outlookcompatible")));
+ (CalendrumTzdataKotlinArray<CalendrumTzdataVTimeZoneMode *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumTzdataVTimeZoneMode *> *entries __attribute__((swift_name("entries")));
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
@interface CalendrumTzdataCalendrum_coreICalComponent : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreICalComponentCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the directly nested components named [name], ignoring ASCII case, in source order; the list is empty when
 * there are none. Deeper levels are not searched.
 *
 * @param name a component name such as `VEVENT`.
 */
- (NSArray<CalendrumTzdataCalendrum_coreICalComponent *> *)componentsName:(NSString *)name __attribute__((swift_name("components(name:)")));

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
- (NSArray<CalendrumTzdataCalendrum_coreICalProperty *> *)propertiesName:(NSString *)name __attribute__((swift_name("properties(name:)")));

/**
 * Returns the first property named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a property name such as `SUMMARY`.
 */
- (CalendrumTzdataCalendrum_coreICalProperty * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns the name and the number of properties and nested components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The directly nested components, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalComponent *> *components __attribute__((swift_name("components")));

/**
 * The canonical name in upper case, such as `VEVENT`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/**
 * The properties of this component, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalProperty *> *properties __attribute__((swift_name("properties")));

/**
 * Where the component starts in the input and where its `END` line ends, or `null` for a component that was built
 * by hand or edited.
 */
@property (readonly) CalendrumTzdataCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));
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
@interface CalendrumTzdataCalendrum_coreVTimeZone : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVTimeZoneCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this time zone changed by [block]. Properties and components that [block] does not touch are
 * the same objects as in this time zone.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVTimeZone *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVTimeZoneBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTimeZone with an equal VTIMEZONE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this time zone; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this time zone and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the time zone (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The `STANDARD` and `DAYLIGHT` observances in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

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
 * @note annotations
 *   kotlin.SinceKotlin(version="2.3")
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant")))
@interface CalendrumTzdataKotlinInstant : CalendrumTzdataBase <CalendrumTzdataKotlinComparable>
@property (class, readonly, getter=companion) CalendrumTzdataKotlinInstantCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumTzdataKotlinInstant *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumTzdataKotlinInstant *)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (int64_t)minusOther:(CalendrumTzdataKotlinInstant *)other __attribute__((swift_name("minus(other:)")));
- (CalendrumTzdataKotlinInstant *)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
- (int64_t)toEpochMilliseconds __attribute__((swift_name("toEpochMilliseconds()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t epochSeconds __attribute__((swift_name("epochSeconds")));
@property (readonly) int32_t nanosecondsOfSecond __attribute__((swift_name("nanosecondsOfSecond")));
@end


/**
 * Receives problems as a reader or an expander finds them.
 *
 * The listener is called on the thread that does the work, in the order the problems are found. It must not throw: an
 * exception thrown by a listener propagates out of the call that was running.
 */
__attribute__((swift_name("Calendrum_coreProblemListener")))
@protocol CalendrumTzdataCalendrum_coreProblemListener
@required

/** Called once for every reported problem. */
- (void)onProblemProblem:(CalendrumTzdataCalendrum_coreProblem *)problem __attribute__((swift_name("onProblem(problem:)")));
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
__attribute__((swift_name("Calendrum_coreICalTimeZone")))
@protocol CalendrumTzdataCalendrum_coreICalTimeZone
@required

/** Returns the UTC offset in force at [instant]. */
- (CalendrumTzdataKotlinx_datetimeUtcOffset *)offsetAtInstant:(CalendrumTzdataKotlinInstant *)instant __attribute__((swift_name("offsetAt(instant:)")));

/**
 * Returns the instant of the wall-clock time [local] in this zone.
 *
 * A time that the zone skips (a daylight saving gap) is read with the offset before the gap, so it lands after the
 * gap; an ambiguous time (an overlap) takes the first of its two instants, as [RFC 5545
 * §3.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.5) says.
 */
- (CalendrumTzdataKotlinInstant *)toInstantLocal:(CalendrumTzdataKotlinx_datetimeLocalDateTime *)local __attribute__((swift_name("toInstant(local:)")));

/** Returns the changes of offset from [from] up to, excluding, [to], in time order. */
- (NSArray<CalendrumTzdataCalendrum_coreZoneTransition *> *)transitionsFrom:(CalendrumTzdataKotlinInstant *)from to:(CalendrumTzdataKotlinInstant *)to __attribute__((swift_name("transitions(from:to:)")));

/** The identifier the zone was requested with, usually the `TZID` of the file. */
@property (readonly) NSString *id __attribute__((swift_name("id")));
@end


/**
 * Finds the rules of a time zone by its `TZID`.
 *
 * Resolvers are combined in a chain with [TimeZoneResolvers.chain]. Implementations must be thread-safe and must not
 * use the system time zone.
 */
__attribute__((swift_name("Calendrum_coreTimeZoneResolver")))
@protocol CalendrumTzdataCalendrum_coreTimeZoneResolver
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
- (id<CalendrumTzdataCalendrum_coreICalTimeZone> _Nullable)resolveTzid:(NSString *)tzid context:(CalendrumTzdataCalendrum_coreResolveContext *)context error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("resolve(tzid:context:)"))) __attribute__((swift_error(nonnull_error)));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface CalendrumTzdataKotlinEnumCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface CalendrumTzdataKotlinArray<T> : CalendrumTzdataBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(CalendrumTzdataInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<CalendrumTzdataKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end


/** Creates builders for [ICalComponent]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalComponent.Companion")))
@interface CalendrumTzdataCalendrum_coreICalComponentCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [ICalComponent]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreICalComponentCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a component named [name].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @throws IllegalArgumentException if [name] is empty or has another character.
 */
- (CalendrumTzdataCalendrum_coreICalComponentBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));
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
@interface CalendrumTzdataCalendrum_coreICalProperty : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreICalPropertyCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a property with the same name, parameters and value. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name, the parameters and the value. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the first parameter named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a parameter name such as `TZID`.
 */
- (CalendrumTzdataCalendrum_coreICalParameter * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/** Returns the name and the number of parameters; never the value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The canonical name in upper case, such as `DTSTART`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The parameters in source order. The list is empty for none. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));

/**
 * Where the property is in the input, or `null` for a property that was built by hand. For input that was changed
 * by an interceptor it is the position of the original line.
 */
@property (readonly) CalendrumTzdataCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));

/**
 * The value as written after the colon, with folding removed. It is not unescaped; an empty string for no value.
 */
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
@interface CalendrumTzdataCalendrum_coreSourceSpan : CalendrumTzdataBase

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


/** Creates VTimeZone values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTimeZone.Companion")))
@interface CalendrumTzdataCalendrum_coreVTimeZoneCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTimeZone values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVTimeZoneCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTimeZone, or returns `null` when it is not a `VTIMEZONE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVTimeZone * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * The common part of the builders of all components: reading and writing properties by key or by name.
 *
 * The typed builders ([VEvent.Builder] and the others) add the accessors of their component. A builder starts empty or
 * from an existing component, shares the nodes it does not touch with it, and is not thread-safe.
 */
__attribute__((swift_name("Calendrum_coreComponentBuilder")))
@interface CalendrumTzdataCalendrum_coreComponentBuilder<B> : CalendrumTzdataBase

/** Returns the value of the first property of [key], or `null`. */
- (id _Nullable)getKey:(CalendrumTzdataCalendrum_corePropertyKey<id> *)key __attribute__((swift_name("get(key:)")));

/**
 * Adds [property] as it is.
 *
 * @return this builder.
 */
- (B)propertyProperty:(CalendrumTzdataCalendrum_coreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Adds a property of [key] with [value] after the existing ones.
 *
 * @return this builder.
 */
- (B)propertyKey:(CalendrumTzdataCalendrum_corePropertyKey<id> *)key value:(id)value __attribute__((swift_name("property(key:value:)")));

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
- (B)removeKey:(CalendrumTzdataCalendrum_corePropertyKey<id> *)key __attribute__((swift_name("remove(key:)")));

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
- (B)setKey:(CalendrumTzdataCalendrum_corePropertyKey<id> *)key value:(id _Nullable)value __attribute__((swift_name("set(key:value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * Collects the properties and nested components of a new or edited time zone, with a Kotlin DSL and chained calls.
 * Get one from [VTimeZone.builder] or [VTimeZone.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTimeZone.Builder")))
@interface CalendrumTzdataCalendrum_coreVTimeZoneBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVTimeZoneBuilder *>

/** Creates the time zone. */
- (CalendrumTzdataCalendrum_coreVTimeZone *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `DAYLIGHT` observance that [block] sets up. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)daylightBlock:(void (^)(CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("daylight(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)lastModifiedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [observance] to the time zone. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)observanceObservance:(CalendrumTzdataCalendrum_coreVTimeZoneObservance *)observance __attribute__((swift_name("observance(observance:)")));

/** Edits the observances in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)observancesBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVTimeZoneObservance *> *))block __attribute__((swift_name("observances(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds a `STANDARD` observance that [block] sets up. */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)standardBlock:(void (^)(CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("standard(block:)")));

/**
 * Sets [tzUrl] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)tzUrlValue:(NSString * _Nullable)value __attribute__((swift_name("tzUrl(value:)")));

/**
 * Sets [tzid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneBuilder *)tzidValue:(NSString * _Nullable)value __attribute__((swift_name("tzid(value:)")));

/**
 * The time of the last modification of the time zone being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The observances of the time zone being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

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
 * What a builder uses when a required property is missing: a clock for `DTSTAMP` and a generator for `UID`.
 *
 * The library never reads the system clock or a global generator by itself.
 *
 * @property clock gives the time for `DTSTAMP`.
 * @property uids makes the `UID`s.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreBuildDefaults")))
@interface CalendrumTzdataCalendrum_coreBuildDefaults : CalendrumTzdataBase
- (instancetype)initWithClock:(id<CalendrumTzdataKotlinClock>)clock uids:(id<CalendrumTzdataCalendrum_coreUidGenerator>)uids __attribute__((swift_name("init(clock:uids:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumTzdataKotlinClock> clock __attribute__((swift_name("clock")));
@property (readonly) id<CalendrumTzdataCalendrum_coreUidGenerator> uids __attribute__((swift_name("uids")));
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
@protocol CalendrumTzdataCalendrum_coreICalDateTime
@required
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
@interface CalendrumTzdataCalendrum_coreLink : CalendrumTzdataBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumTzdataCalendrum_coreValueType *)valueType linkRel:(NSString *)linkRel __attribute__((swift_name("init(value:valueType:linkRel:)"))) __attribute__((objc_designated_initializer));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumTzdataCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
@interface CalendrumTzdataCalendrum_coreVTimeZoneObservance : CalendrumTzdataBase

/**
 * Returns a copy of this observance changed by [block]; untouched nodes are shared.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservance *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is an observance of the same kind with an equal component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this observance. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** The comments (`COMMENT`) in source order. */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts (`CONCEPT`, [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) of the
 * observance in source order.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The links (`LINK`, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) of the observance in
 * source order.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) CalendrumTzdataKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The UTC offset the observance puts in force (`TZOFFSETTO`,
 * [RFC 5545 §3.8.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The additional start dates (`RDATE`) in source order; a period contributes its start. */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The reference identifiers (`REFID`, [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) of
 * the observance in source order.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships (`RELATED-TO`, [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5))
 * of the observance in source order.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/** The recurrence rule of the observance (`RRULE`), or `null` when it has none or it does not decode. */
@property (readonly) CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The local start (`DTSTART`) of the observance, in the offset before it; `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));
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
@interface CalendrumTzdataCalendrum_coreRelatedTo : CalendrumTzdataBase
- (instancetype)initWithUid:(NSString *)uid __attribute__((swift_name("init(uid:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumTzdataCalendrum_coreRelType *)relType __attribute__((swift_name("init(uid:relType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumTzdataCalendrum_coreRelType *)relType gap:(CalendrumTzdataCalendrum_coreICalDuration *)gap __attribute__((swift_name("init(uid:relType:gap:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a RelatedTo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataCalendrum_coreICalDuration * _Nullable gap __attribute__((swift_name("gap")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumTzdataCalendrum_coreRelType *relType __attribute__((swift_name("relType")));
@property (readonly) NSString *uid __attribute__((swift_name("uid")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant.Companion")))
@interface CalendrumTzdataKotlinInstantCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinInstantCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumTzdataKotlinInstant *)fromEpochMillisecondsEpochMilliseconds:(int64_t)epochMilliseconds __attribute__((swift_name("fromEpochMilliseconds(epochMilliseconds:)")));
- (CalendrumTzdataKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment:(int32_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment:)")));
- (CalendrumTzdataKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment_:(int64_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment_:)")));
- (CalendrumTzdataKotlinInstant *)now __attribute__((swift_name("now()"))) __attribute__((unavailable("Use Clock.System.now() instead")));
- (CalendrumTzdataKotlinInstant *)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (CalendrumTzdataKotlinInstant * _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@property (readonly) CalendrumTzdataKotlinInstant *DISTANT_FUTURE __attribute__((swift_name("DISTANT_FUTURE")));
@property (readonly) CalendrumTzdataKotlinInstant *DISTANT_PAST __attribute__((swift_name("DISTANT_PAST")));
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
@interface CalendrumTzdataCalendrum_coreProblem : CalendrumTzdataBase

/**
 * Returns the code, severity, message and position, such as `ICAL-S003 Warning: bare LF used as a line ending at
 * 3:7 (offset 12)`. It never contains values from the input.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The stable code of the problem, such as `ICAL-S003`. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *code __attribute__((swift_name("code")));

/**
 * A short human-readable text: the fixed message of the code, followed by an explanation after a colon when the
 * code has one (for example the name of a detected charset). It never contains data from the input.
 */
@property (readonly) NSString *message __attribute__((swift_name("message")));

/** The severity of [code]. */
@property (readonly) CalendrumTzdataCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));

/**
 * Where in the input the problem was found; a point, so [SourceSpan.startOffset] equals [SourceSpan.endOffset].
 *
 * Nodes built by hand or edited by an interceptor have no position in the original input; see [SourceSpan].
 */
@property (readonly) CalendrumTzdataCalendrum_coreSourceSpan *span __attribute__((swift_name("span")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/UtcOffsetSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset")))
@interface CalendrumTzdataKotlinx_datetimeUtcOffset : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeUtcOffsetCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t totalSeconds __attribute__((swift_name("totalSeconds")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/LocalDateTimeSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateTime")))
@interface CalendrumTzdataKotlinx_datetimeLocalDateTime : CalendrumTzdataBase <CalendrumTzdataKotlinComparable>
- (instancetype)initWithDate:(CalendrumTzdataKotlinx_datetimeLocalDate *)date time:(CalendrumTzdataKotlinx_datetimeLocalTime *)time __attribute__((swift_name("init(date:time:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithYear:(int32_t)year month:(int32_t)month day:(int32_t)day hour:(int32_t)hour minute:(int32_t)minute second:(int32_t)second nanosecond:(int32_t)nanosecond __attribute__((swift_name("init(year:month:day:hour:minute:second:nanosecond:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithYear:(int32_t)year month:(CalendrumTzdataKotlinx_datetimeMonth *)month day:(int32_t)day hour:(int32_t)hour minute:(int32_t)minute second:(int32_t)second nanosecond_:(int32_t)nanosecond __attribute__((swift_name("init(year:month:day:hour:minute:second:nanosecond_:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeLocalDateTimeCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumTzdataKotlinx_datetimeLocalDateTime *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalDate *date __attribute__((swift_name("date")));
@property (readonly) int32_t day __attribute__((swift_name("day")));
@property (readonly) int32_t dayOfMonth __attribute__((swift_name("dayOfMonth"))) __attribute__((deprecated("Use the 'day' property instead")));
@property (readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) int32_t dayOfYear __attribute__((swift_name("dayOfYear")));
@property (readonly) int32_t hour __attribute__((swift_name("hour")));
@property (readonly) int32_t minute __attribute__((swift_name("minute")));
@property (readonly) CalendrumTzdataKotlinx_datetimeMonth *month __attribute__((swift_name("month")));
@property (readonly) int32_t monthNumber __attribute__((swift_name("monthNumber"))) __attribute__((deprecated("Use the 'month' property instead")));
@property (readonly) int32_t nanosecond __attribute__((swift_name("nanosecond")));
@property (readonly) int32_t second __attribute__((swift_name("second")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalTime *time __attribute__((swift_name("time")));
@property (readonly) int32_t year __attribute__((swift_name("year")));
@end


/**
 * A change of the UTC offset of a zone, such as the start or the end of daylight saving time.
 *
 * @property at the instant at which the new offset takes effect.
 * @property offsetBefore the offset just before [at].
 * @property offsetAfter the offset from [at] on.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreZoneTransition")))
@interface CalendrumTzdataCalendrum_coreZoneTransition : CalendrumTzdataBase
- (instancetype)initWithAt:(CalendrumTzdataKotlinInstant *)at offsetBefore:(CalendrumTzdataKotlinx_datetimeUtcOffset *)offsetBefore offsetAfter:(CalendrumTzdataKotlinx_datetimeUtcOffset *)offsetAfter __attribute__((swift_name("init(at:offsetBefore:offsetAfter:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a transition at the same instant with the same offsets. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the instant and the offsets. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the instant and the offsets, such as `ZoneTransition(2026-03-29T01:00:00Z, +01:00 -> +02:00)`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataKotlinInstant *at __attribute__((swift_name("at")));
@property (readonly) CalendrumTzdataKotlinx_datetimeUtcOffset *offsetAfter __attribute__((swift_name("offsetAfter")));
@property (readonly) CalendrumTzdataKotlinx_datetimeUtcOffset *offsetBefore __attribute__((swift_name("offsetBefore")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface CalendrumTzdataKotlinThrowable : CalendrumTzdataBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (CalendrumTzdataKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface CalendrumTzdataKotlinException : CalendrumTzdataKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface CalendrumTzdataKotlinRuntimeException : CalendrumTzdataKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalArgumentException")))
@interface CalendrumTzdataKotlinIllegalArgumentException : CalendrumTzdataKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumTzdataKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeIllegalTimeZoneException")))
@interface CalendrumTzdataKotlinx_datetimeIllegalTimeZoneException : CalendrumTzdataKotlinIllegalArgumentException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString *)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumTzdataKotlinThrowable *)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString *)message cause:(CalendrumTzdataKotlinThrowable *)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * What a resolver may need besides the identifier: the calendar the `TZID` comes from and where to report problems
 * (`ICAL-Z001` to `ICAL-Z004`).
 *
 * @property calendar the calendar being read, or `null`.
 * @property problemListener receives the problems, or `null`.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreResolveContext")))
@interface CalendrumTzdataCalendrum_coreResolveContext : CalendrumTzdataBase
- (instancetype)initWithCalendar:(CalendrumTzdataCalendrum_coreVCalendar * _Nullable)calendar problemListener:(id<CalendrumTzdataCalendrum_coreProblemListener> _Nullable)problemListener __attribute__((swift_name("init(calendar:problemListener:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreResolveContextCompanion *companion __attribute__((swift_name("companion")));

/** Returns the name of the class. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataCalendrum_coreVCalendar * _Nullable calendar __attribute__((swift_name("calendar")));
@property (readonly) id<CalendrumTzdataCalendrum_coreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol CalendrumTzdataKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end


/**
 * Collects the properties and nested components of a new [ICalComponent].
 *
 * A builder is not thread-safe. Obtain one with [ICalComponent.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalComponent.Builder")))
@interface CalendrumTzdataCalendrum_coreICalComponentBuilder : CalendrumTzdataBase

/** Creates the component. The builder can be reused: later changes do not affect components already built. */
- (CalendrumTzdataCalendrum_coreICalComponent *)build __attribute__((swift_name("build()")));

/** Appends [component] after the nested components added so far. */
- (CalendrumTzdataCalendrum_coreICalComponentBuilder *)componentComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("component(component:)")));

/**
 * Appends a nested component built by [block].
 *
 * @param name the name of the nested component.
 * @param block fills the builder of the nested component.
 * @throws IllegalArgumentException if [name] is not a valid component name.
 */
- (CalendrumTzdataCalendrum_coreICalComponentBuilder *)componentName:(NSString *)name block:(void (^)(CalendrumTzdataCalendrum_coreICalComponentBuilder *))block __attribute__((swift_name("component(name:block:)")));

/** Appends [property] after the properties added so far. */
- (CalendrumTzdataCalendrum_coreICalComponentBuilder *)propertyProperty:(CalendrumTzdataCalendrum_coreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Appends a property with the given [name] and [value] and no parameters.
 *
 * @throws IllegalArgumentException if [name] is not a valid property name, or [value] contains a control
 *   character other than a tab.
 */
- (CalendrumTzdataCalendrum_coreICalComponentBuilder *)propertyName:(NSString *)name value:(NSString *)value __attribute__((swift_name("property(name:value:)")));
@end


/** Creates properties by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Companion")))
@interface CalendrumTzdataCalendrum_coreICalPropertyCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates properties by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreICalPropertyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a property named [name].
 *
 * @param name letters, digits and hyphens only; `BEGIN` and `END` are not allowed.
 * @throws IllegalArgumentException if [name] is invalid.
 */
- (CalendrumTzdataCalendrum_coreICalPropertyBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));

/**
 * Creates a property with no parameters.
 *
 * @param name letters, digits and hyphens only; the stored name is upper case; `BEGIN` and `END` are not
 *   allowed.
 * @param value the value as it is written; a control character other than a tab is not allowed.
 * @throws IllegalArgumentException if [name] or [value] is invalid.
 */
- (CalendrumTzdataCalendrum_coreICalProperty *)ofName:(NSString *)name value:(NSString *)value __attribute__((swift_name("of(name:value:)")));
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
@interface CalendrumTzdataCalendrum_coreICalParameter : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreICalParameterCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_corePropertyKey<T> : CalendrumTzdataBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumTzdataCalendrum_corePropertyCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumTzdataCalendrum_corePropertyCodec> codec __attribute__((swift_name("codec")));

/** The canonical property name in upper case. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/**
 * Collects the properties of a `STANDARD` or `DAYLIGHT` observance. Get one from [Standard.builder],
 * [Daylight.builder] or [Observance.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTimeZone.ObservanceBuilder")))
@interface CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *>

/** Creates the observance: a [Standard] or a [Daylight], as the builder was made for. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservance *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds a `TZNAME` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/** Sets [offsetFrom] to [value] and returns this builder; `null` removes the property. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)offsetFromValue:(CalendrumTzdataKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetFrom(value:)")));

/** Sets [offsetTo] to [value] and returns this builder; `null` removes the property. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)offsetToValue:(CalendrumTzdataKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetTo(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)rdateDates:(CalendrumTzdataKotlinArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)rdatePeriods:(CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/** Adds [relatedTo] as a `RELATED-TO` property. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/** Adds a `RELATED-TO` property naming the component with the given [uid]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Sets [rrule] to [value] and returns this builder; `null` removes the property. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)rruleValue:(CalendrumTzdataCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)rruleFrequency:(CalendrumTzdataCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumTzdataCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/** Sets [start] to [value] and returns this builder; `null` removes the property. */
- (CalendrumTzdataCalendrum_coreVTimeZoneObservanceBuilder *)startValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * The offset before the observance being built (`TZOFFSETFROM`); assigning `null` removes the property.
 */
@property CalendrumTzdataKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The offset the observance being built puts in force (`TZOFFSETTO`); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The recurrence rule of the observance being built (`RRULE`); assigning `null` removes the property. */
@property CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/** The local start (`DTSTART`) of the observance being built; assigning `null` removes the property. */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));
@end


/**
 * An editable view of the nested components of one kind in a builder, such as the alarms of an event.
 *
 * @property items the components now in the list.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreComponentList")))
@interface CalendrumTzdataCalendrum_coreComponentList<T> : CalendrumTzdataBase

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
 * @note annotations
 *   kotlin.SinceKotlin(version="2.3")
*/
__attribute__((swift_name("KotlinClock")))
@protocol CalendrumTzdataKotlinClock
@required
- (CalendrumTzdataKotlinInstant *)now __attribute__((swift_name("now()")));
@end


/** Makes the `UID` values of new components. There is no global generator: you pass one in [BuildDefaults]. */
__attribute__((swift_name("Calendrum_coreUidGenerator")))
@protocol CalendrumTzdataCalendrum_coreUidGenerator
@required

/** Returns a new `UID`; it must be globally unique. */
- (NSString *)next __attribute__((swift_name("next()")));
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
@interface CalendrumTzdataCalendrum_coreValueType : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreValueTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ValueType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
@interface CalendrumTzdataCalendrum_coreRecur : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreRecurCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a rule with the same parts in the same form. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over all parts. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the rule in iCalendar text form, such as `FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE`; unknown parts are left out.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The `BYDAY` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreWeekdayNum *> *byDay __attribute__((swift_name("byDay")));

/** The `BYHOUR` values, 0 to 23; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataInt *> *byHour __attribute__((swift_name("byHour")));

/** The `BYMINUTE` values, 0 to 59; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataInt *> *byMinute __attribute__((swift_name("byMinute")));

/** The `BYMONTH` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreMonthNum *> *byMonth __attribute__((swift_name("byMonth")));

/** The `BYMONTHDAY` values, 1 to 31 or -31 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataInt *> *byMonthDay __attribute__((swift_name("byMonthDay")));

/** The `BYSECOND` values, 0 to 60; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataInt *> *bySecond __attribute__((swift_name("bySecond")));

/** The `BYSETPOS` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataInt *> *bySetPos __attribute__((swift_name("bySetPos")));

/** The `BYWEEKNO` values, 1 to 53 or -53 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataInt *> *byWeekNo __attribute__((swift_name("byWeekNo")));

/** The `BYYEARDAY` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumTzdataInt *> *byYearDay __attribute__((swift_name("byYearDay")));

/** The `COUNT`, or `null` when the rule has none. */
@property (readonly) CalendrumTzdataInt * _Nullable count __attribute__((swift_name("count")));

/** The `FREQ` of the rule. */
@property (readonly) CalendrumTzdataCalendrum_coreFrequency *frequency __attribute__((swift_name("frequency")));

/** The `INTERVAL`, 1 when the rule has none. */
@property (readonly) int32_t interval __attribute__((swift_name("interval")));

/**
 * The `RSCALE` calendar name of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529), or `null` for the Gregorian
 * calendar.
 */
@property (readonly) NSString * _Nullable rscale __attribute__((swift_name("rscale")));

/** The `SKIP` rule part, or `null` when the rule has none. */
@property (readonly) CalendrumTzdataCalendrum_coreSkip * _Nullable skip __attribute__((swift_name("skip")));

/** The parts the library does not know, in the order they were written; empty for none. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRecurPart *> *unknownParts __attribute__((swift_name("unknownParts")));

/** The `UNTIL` of the rule: a date, a local date-time or a UTC date-time, or `null` when the rule has none. */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable until __attribute__((swift_name("until")));

/** The `WKST`, or `null` when the rule has none (Monday is then the default of the standard). */
@property (readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek * _Nullable weekStart __attribute__((swift_name("weekStart")));
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
@interface CalendrumTzdataCalendrum_coreRelType : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreRelTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a RelType with the same token. */
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
@interface CalendrumTzdataCalendrum_coreICalDuration : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreICalDurationCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a duration with the same sign, amounts and written units. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the sign, the amounts and the written units. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the duration as a kotlinx-datetime period with the sign applied to every unit; weeks become days (7
 * each).
 */
- (CalendrumTzdataKotlinx_datetimeDateTimePeriod *)toDateTimePeriod __attribute__((swift_name("toDateTimePeriod()")));

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
@interface CalendrumTzdataCalendrum_coreProblemCode : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreProblemCodeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a code with the same [id]. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the [id]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the [id], such as `ICAL-S003`. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The identifier of the code, for example `ICAL-S003`. */
@property (readonly) NSString *id __attribute__((swift_name("id")));

/** How serious the problems with this code are. */
@property (readonly) CalendrumTzdataCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));
@end


/** How serious a [Problem] is. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSeverity")))
@interface CalendrumTzdataCalendrum_coreSeverity : CalendrumTzdataKotlinEnum<CalendrumTzdataCalendrum_coreSeverity *>
+ (instancetype)alloc __attribute__((unavailable));

/** How serious a [Problem] is. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumTzdataCalendrum_coreSeverity *info __attribute__((swift_name("info")));
@property (class, readonly) CalendrumTzdataCalendrum_coreSeverity *warning __attribute__((swift_name("warning")));
@property (class, readonly) CalendrumTzdataCalendrum_coreSeverity *error __attribute__((swift_name("error")));
+ (CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreSeverity *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumTzdataCalendrum_coreSeverity *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset.Companion")))
@interface CalendrumTzdataKotlinx_datetimeUtcOffsetCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeUtcOffsetCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)FormatBlock:(void (^)(id<CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset>))block __attribute__((swift_name("Format(block:)")));
- (CalendrumTzdataKotlinx_datetimeUtcOffset *)parseInput:(id)input format:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) CalendrumTzdataKotlinx_datetimeUtcOffset *ZERO __attribute__((swift_name("ZERO")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/LocalDateSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDate")))
@interface CalendrumTzdataKotlinx_datetimeLocalDate : CalendrumTzdataBase <CalendrumTzdataKotlinComparable>
- (instancetype)initWithYear:(int32_t)year month:(int32_t)month day:(int32_t)day __attribute__((swift_name("init(year:month:day:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithYear:(int32_t)year month:(CalendrumTzdataKotlinx_datetimeMonth *)month day_:(int32_t)day __attribute__((swift_name("init(year:month:day_:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeLocalDateCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumTzdataKotlinx_datetimeLocalDate *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumTzdataKotlinx_datetimeLocalDateRange *)rangeToThat:(CalendrumTzdataKotlinx_datetimeLocalDate *)that __attribute__((swift_name("rangeTo(that:)")));
- (CalendrumTzdataKotlinx_datetimeLocalDateRange *)rangeUntilThat:(CalendrumTzdataKotlinx_datetimeLocalDate *)that __attribute__((swift_name("rangeUntil(that:)")));
- (int64_t)toEpochDays __attribute__((swift_name("toEpochDays()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t day __attribute__((swift_name("day")));
@property (readonly) int32_t dayOfMonth __attribute__((swift_name("dayOfMonth"))) __attribute__((deprecated("Use the 'day' property instead")));
@property (readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) int32_t dayOfYear __attribute__((swift_name("dayOfYear")));
@property (readonly) CalendrumTzdataKotlinx_datetimeMonth *month __attribute__((swift_name("month")));
@property (readonly) int32_t monthNumber __attribute__((swift_name("monthNumber"))) __attribute__((deprecated("Use the 'month' property instead")));
@property (readonly) int32_t year __attribute__((swift_name("year")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/LocalTimeSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalTime")))
@interface CalendrumTzdataKotlinx_datetimeLocalTime : CalendrumTzdataBase <CalendrumTzdataKotlinComparable>
- (instancetype)initWithHour:(int32_t)hour minute:(int32_t)minute second:(int32_t)second nanosecond:(int32_t)nanosecond __attribute__((swift_name("init(hour:minute:second:nanosecond:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeLocalTimeCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumTzdataKotlinx_datetimeLocalTime *)other __attribute__((swift_name("compareTo(other:)")));
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
@interface CalendrumTzdataKotlinx_datetimeMonth : CalendrumTzdataKotlinEnum<CalendrumTzdataKotlinx_datetimeMonth *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *january __attribute__((swift_name("january")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *february __attribute__((swift_name("february")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *march __attribute__((swift_name("march")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *april __attribute__((swift_name("april")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *may __attribute__((swift_name("may")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *june __attribute__((swift_name("june")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *july __attribute__((swift_name("july")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *august __attribute__((swift_name("august")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *september __attribute__((swift_name("september")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *october __attribute__((swift_name("october")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *november __attribute__((swift_name("november")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeMonth *december __attribute__((swift_name("december")));
+ (CalendrumTzdataKotlinArray<CalendrumTzdataKotlinx_datetimeMonth *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumTzdataKotlinx_datetimeMonth *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateTime.Companion")))
@interface CalendrumTzdataKotlinx_datetimeLocalDateTimeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeLocalDateTimeCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)FormatBuilder:(void (^)(id<CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithDateTime>))builder __attribute__((swift_name("Format(builder:)")));
- (CalendrumTzdataKotlinx_datetimeLocalDateTime *)parseInput:(id)input format:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeek")))
@interface CalendrumTzdataKotlinx_datetimeDayOfWeek : CalendrumTzdataKotlinEnum<CalendrumTzdataKotlinx_datetimeDayOfWeek *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *monday __attribute__((swift_name("monday")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *tuesday __attribute__((swift_name("tuesday")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *wednesday __attribute__((swift_name("wednesday")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *thursday __attribute__((swift_name("thursday")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *friday __attribute__((swift_name("friday")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *saturday __attribute__((swift_name("saturday")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *sunday __attribute__((swift_name("sunday")));
+ (CalendrumTzdataKotlinArray<CalendrumTzdataKotlinx_datetimeDayOfWeek *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumTzdataKotlinx_datetimeDayOfWeek *> *entries __attribute__((swift_name("entries")));
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
@interface CalendrumTzdataCalendrum_coreVCalendar : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVCalendarCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this calendar changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this calendar.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVCalendar *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVCalendarBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VCalendar with an equal VCALENDAR component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this calendar; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this calendar and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

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
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

/**
 * The descriptions of the calendar (`DESCRIPTION` properties,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *descriptions __attribute__((swift_name("descriptions")));

/** The events nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy component nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The images of the calendar (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/** The journal entries nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)), or `null` when it is missing or does
 * not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreMethod * _Nullable method __attribute__((swift_name("method")));

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
@property (readonly) CalendrumTzdataCalendrum_coreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar (`SOURCE`, [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVTodo *> *todos __attribute__((swift_name("todos")));

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


/** Shared contexts. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreResolveContext.Companion")))
@interface CalendrumTzdataCalendrum_coreResolveContextCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Shared contexts. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreResolveContextCompanion *shared __attribute__((swift_name("shared")));

/** A context without a calendar and a listener. */
@property (readonly) CalendrumTzdataCalendrum_coreResolveContext *Empty __attribute__((swift_name("Empty")));
@end


/** Collects the parameters and the value of a new [ICalProperty]. A builder is not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Builder")))
@interface CalendrumTzdataCalendrum_coreICalPropertyBuilder : CalendrumTzdataBase

/**
 * Creates the property.
 *
 * @throws IllegalArgumentException if the value contains a control character other than a tab.
 */
- (CalendrumTzdataCalendrum_coreICalProperty *)build __attribute__((swift_name("build()")));

/** Appends [parameter] after the parameters added so far. */
- (CalendrumTzdataCalendrum_coreICalPropertyBuilder *)parameterParameter:(CalendrumTzdataCalendrum_coreICalParameter *)parameter __attribute__((swift_name("parameter(parameter:)")));

/**
 * Appends a parameter with the given [name] and [values].
 *
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumTzdataCalendrum_coreICalPropertyBuilder *)parameterName:(NSString *)name values:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/**
 * Sets the value, which is written as is: escape `TEXT` yourself or use the typed model. The default is an
 * empty string.
 */
- (CalendrumTzdataCalendrum_coreICalPropertyBuilder *)valueValue:(NSString *)value __attribute__((swift_name("value(value:)")));
@end


/** Creates parameters by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalParameter.Companion")))
@interface CalendrumTzdataCalendrum_coreICalParameterCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates parameters by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreICalParameterCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates a parameter with the given [name] and [values].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @param values at least one value.
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumTzdataCalendrum_coreICalParameter *)ofName:(NSString *)name values:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("of(name:values:)")));
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
@protocol CalendrumTzdataCalendrum_corePropertyCodec
@required

/**
 * Decodes [property], with problems and strictness as [context] says.
 *
 * @param property the property to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumTzdataCalendrum_coreDecoded>)decodeProperty:(CalendrumTzdataCalendrum_coreICalProperty *)property context:(CalendrumTzdataCalendrum_coreDecodeContext *)context __attribute__((swift_name("decode(property:context:)")));

/**
 * Encodes [value] into a property named by [context].
 *
 * @param value the value to write.
 * @param context carries the property name.
 */
- (CalendrumTzdataCalendrum_coreICalProperty *)encodePropertyValue:(id)value context:(CalendrumTzdataCalendrum_coreEncodeContext *)context __attribute__((swift_name("encodeProperty(value:context:)")));
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
@interface CalendrumTzdataCalendrum_coreICalPeriod : CalendrumTzdataBase
- (instancetype)initWithStart:(id<CalendrumTzdataCalendrum_coreICalDateTime>)start end:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)end duration:(CalendrumTzdataCalendrum_coreICalDuration * _Nullable)duration __attribute__((swift_name("init(start:end:duration:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a period with equal start, end and duration. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the start, the end and the duration. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the start and the end or duration separated by a slash, in the forms of [ICalDateTime.toString] and
 * [ICalDuration.toString].
 */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> start __attribute__((swift_name("start")));
@end


/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFrequency")))
@interface CalendrumTzdataCalendrum_coreFrequency : CalendrumTzdataKotlinEnum<CalendrumTzdataCalendrum_coreFrequency *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumTzdataCalendrum_coreFrequency *secondly __attribute__((swift_name("secondly")));
@property (class, readonly) CalendrumTzdataCalendrum_coreFrequency *minutely __attribute__((swift_name("minutely")));
@property (class, readonly) CalendrumTzdataCalendrum_coreFrequency *hourly __attribute__((swift_name("hourly")));
@property (class, readonly) CalendrumTzdataCalendrum_coreFrequency *daily __attribute__((swift_name("daily")));
@property (class, readonly) CalendrumTzdataCalendrum_coreFrequency *weekly __attribute__((swift_name("weekly")));
@property (class, readonly) CalendrumTzdataCalendrum_coreFrequency *monthly __attribute__((swift_name("monthly")));
@property (class, readonly) CalendrumTzdataCalendrum_coreFrequency *yearly __attribute__((swift_name("yearly")));
+ (CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreFrequency *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumTzdataCalendrum_coreFrequency *> *entries __attribute__((swift_name("entries")));
@end


/**
 * Builds a [Recur] that is correct by construction: the combinations the standard forbids are rejected as they are
 * added. Not thread-safe; obtain one with [Recur.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRecur.Builder")))
@interface CalendrumTzdataCalendrum_coreRecurBuilder : CalendrumTzdataBase

/**
 * Creates the rule.
 *
 * @throws IllegalStateException if `BYSETPOS` is used without another `BYxxx` part, or `BYWEEKNO` is combined
 *   with a numbered `BYDAY`.
 */
- (CalendrumTzdataCalendrum_coreRecur *)build __attribute__((swift_name("build()")));

/**
 * Sets `BYDAY` to the given entries, which may carry a number such as the second Tuesday.
 *
 * @throws IllegalArgumentException if no entry is given, a number is used with a frequency other than `MONTHLY`
 *   or `YEARLY`, or a number is 0 or outside -53 to 53.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byDayDays:(CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreWeekdayNum *> *)days __attribute__((swift_name("byDay(days:)")));

/**
 * Sets `BYDAY` to the given days, every such weekday.
 *
 * @throws IllegalArgumentException if no day is given.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byDayDaysOfWeek:(CalendrumTzdataKotlinArray<CalendrumTzdataKotlinx_datetimeDayOfWeek *> *)daysOfWeek __attribute__((swift_name("byDay(daysOfWeek:)")));

/**
 * Sets `BYHOUR` to [values], each 0 to 23.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byHourValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("byHour(values:)")));

/**
 * Sets `BYMINUTE` to [values], each 0 to 59.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byMinuteValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("byMinute(values:)")));

/**
 * Sets `BYMONTH` to [values], each 1 to 12.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byMonthValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("byMonth(values:)")));

/**
 * Sets `BYMONTHDAY` to [values], each 1 to 31 or -31 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `WEEKLY`, there is no value or one is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byMonthDayValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("byMonthDay(values:)")));

/**
 * Sets `BYSECOND` to [values], each 0 to 60.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)bySecondValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("bySecond(values:)")));

/**
 * Sets `BYSETPOS` to [values], each 1 to 366 or -366 to -1. The rule also needs another `BYxxx` part.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)bySetPosValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("bySetPos(values:)")));

/**
 * Sets `BYWEEKNO` to [values], each 1 to 53 or -53 to -1.
 *
 * @throws IllegalArgumentException if the frequency is not `YEARLY`, there is no value or one is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byWeekNoValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("byWeekNo(values:)")));

/**
 * Sets `BYYEARDAY` to [values], each 1 to 366 or -366 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `DAILY`, `WEEKLY` or `MONTHLY`, there is no value or one
 *   is out of range.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)byYearDayValues:(CalendrumTzdataKotlinIntArray *)values __attribute__((swift_name("byYearDay(values:)")));

/**
 * Sets `COUNT`, the number of occurrences.
 *
 * @throws IllegalStateException if `UNTIL` is already set.
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)countValue:(int32_t)value __attribute__((swift_name("count(value:)")));

/**
 * Sets `INTERVAL`, the step in units of the frequency.
 *
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)intervalValue:(int32_t)value __attribute__((swift_name("interval(value:)")));

/** Returns the frequency of the rule being built. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Sets `UNTIL`, which is a date, a local date-time or a UTC date-time and must suit the type of `DTSTART`.
 *
 * @throws IllegalStateException if `COUNT` is already set.
 * @throws IllegalArgumentException if [value] is a zoned date-time or its year is outside 0000 to 9999.
 */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)untilValue:(id<CalendrumTzdataCalendrum_coreICalDateTime>)value __attribute__((swift_name("until(value:)")));

/** Sets `WKST`, the day on which the week starts. */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)weekStartDay:(CalendrumTzdataKotlinx_datetimeDayOfWeek *)day __attribute__((swift_name("weekStart(day:)")));
@end


/** The constants of ValueType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreValueType.Companion")))
@interface CalendrumTzdataCalendrum_coreValueTypeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ValueType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreValueTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ValueType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreValueType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BINARY`. Base64 encoded binary data. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Binary __attribute__((swift_name("Binary")));

/** `BOOLEAN`. `TRUE` or `FALSE`. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Boolean __attribute__((swift_name("Boolean")));

/** `CAL-ADDRESS`. A calendar user address. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *CalAddress __attribute__((swift_name("CalAddress")));

/** `DATE`. A calendar date. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Date __attribute__((swift_name("Date")));

/** `DATE-TIME`. A date with a time of day. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *DateTime __attribute__((swift_name("DateTime")));

/** `DURATION`. A length of time. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Duration __attribute__((swift_name("Duration")));

/** `FLOAT`. A decimal number. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Float __attribute__((swift_name("Float")));

/** `INTEGER`. A signed 32-bit integer. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Integer __attribute__((swift_name("Integer")));

/** `PERIOD`. A precise period of time. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Period __attribute__((swift_name("Period")));

/** `RECUR`. A recurrence rule. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Recur __attribute__((swift_name("Recur")));

/** `TEXT`. Human-readable text. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Text __attribute__((swift_name("Text")));

/** `TIME`. A time of day. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Time __attribute__((swift_name("Time")));

/** `UID`. A `UID` reference (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Uid __attribute__((swift_name("Uid")));

/** `URI`. A uniform resource identifier. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *Uri __attribute__((swift_name("Uri")));

/** `UTC-OFFSET`. An offset from UTC. */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *UtcOffset __attribute__((swift_name("UtcOffset")));

/** `XML-REFERENCE`. A reference to an XML element (RFC 9073). */
@property (readonly) CalendrumTzdataCalendrum_coreValueType *XmlReference __attribute__((swift_name("XmlReference")));
@end


/** Creates rule builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRecur.Companion")))
@interface CalendrumTzdataCalendrum_coreRecurCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates rule builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreRecurCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for a rule that repeats with the given [frequency]. */
- (CalendrumTzdataCalendrum_coreRecurBuilder *)builderFrequency:(CalendrumTzdataCalendrum_coreFrequency *)frequency __attribute__((swift_name("builder(frequency:)")));
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
@interface CalendrumTzdataCalendrum_coreWeekdayNum : CalendrumTzdataBase
- (instancetype)initWithOrdinal:(CalendrumTzdataInt * _Nullable)ordinal dayOfWeek:(CalendrumTzdataKotlinx_datetimeDayOfWeek *)dayOfWeek __attribute__((swift_name("init(ordinal:dayOfWeek:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same number and day of week. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the number and the day of week. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the entry as written in a rule, such as `MO` or `-1FR`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) CalendrumTzdataInt * _Nullable ordinal __attribute__((swift_name("ordinal")));
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
@interface CalendrumTzdataCalendrum_coreMonthNum : CalendrumTzdataBase
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
@interface CalendrumTzdataCalendrum_coreSkip : CalendrumTzdataKotlinEnum<CalendrumTzdataCalendrum_coreSkip *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `SKIP` rule part of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529#section-4.1.1): what to do with an
 * occurrence that falls on a date that does not exist, such as the 31st in a month with 30 days.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumTzdataCalendrum_coreSkip *omit __attribute__((swift_name("omit")));
@property (class, readonly) CalendrumTzdataCalendrum_coreSkip *backward __attribute__((swift_name("backward")));
@property (class, readonly) CalendrumTzdataCalendrum_coreSkip *forward __attribute__((swift_name("forward")));
+ (CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreSkip *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumTzdataCalendrum_coreSkip *> *entries __attribute__((swift_name("entries")));
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
@interface CalendrumTzdataCalendrum_coreRecurPart : CalendrumTzdataBase
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


/** The constants of RelType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRelType.Companion")))
@interface CalendrumTzdataCalendrum_coreRelTypeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of RelType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreRelTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new RelType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreRelType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHILD`. The related component is a child of this one. */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *Child __attribute__((swift_name("Child")));

/** `CONCEPT`. The related value is a `CONCEPT` URI, not a `UID` (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *Concept __attribute__((swift_name("Concept")));

/** `DEPENDS-ON`. This component depends on the related one (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *DependsOn __attribute__((swift_name("DependsOn")));

/** `FINISHTOFINISH`. A finish-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *FinishToFinish __attribute__((swift_name("FinishToFinish")));

/** `FINISHTOSTART`. A finish-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *FinishToStart __attribute__((swift_name("FinishToStart")));

/** `FIRST`. The related component is the first of a series of components (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *First __attribute__((swift_name("First")));

/** `NEXT`. The related component is the next of a series of components (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *Next __attribute__((swift_name("Next")));

/** `PARENT`. The related component is the parent of this one; the default. */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *Parent __attribute__((swift_name("Parent")));

/** `REFID`. The related value is a `REFID` reference identifier, not a `UID` (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *RefId __attribute__((swift_name("RefId")));

/** `SIBLING`. The related component is a sibling of this one. */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *Sibling __attribute__((swift_name("Sibling")));

/** `SNOOZE`. This alarm is a snooze of the related alarm (RFC 9074). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *Snooze __attribute__((swift_name("Snooze")));

/** `STARTTOFINISH`. A start-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *StartToFinish __attribute__((swift_name("StartToFinish")));

/** `STARTTOSTART`. A start-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumTzdataCalendrum_coreRelType *StartToStart __attribute__((swift_name("StartToStart")));
@end


/** Creates durations by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalDuration.Companion")))
@interface CalendrumTzdataCalendrum_coreICalDurationCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates durations by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreICalDurationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates the duration that equals [duration] in hours, minutes and seconds.
 *
 * @throws IllegalArgumentException if [duration] is infinite, has a fraction of a second, or is longer than
 *   `Int.MAX_VALUE` hours.
 */
- (CalendrumTzdataCalendrum_coreICalDuration *)ofDuration:(int64_t)duration __attribute__((swift_name("of(duration:)")));

/**
 * Creates a duration of [days] days; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [days] is `Int.MIN_VALUE`.
 */
- (CalendrumTzdataCalendrum_coreICalDuration *)ofDaysDays:(int32_t)days __attribute__((swift_name("ofDays(days:)")));

/**
 * Creates a duration of [hours] hours; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [hours] is `Int.MIN_VALUE`.
 */
- (CalendrumTzdataCalendrum_coreICalDuration *)ofHoursHours:(int32_t)hours __attribute__((swift_name("ofHours(hours:)")));

/**
 * Creates a duration of [minutes] minutes; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [minutes] is `Int.MIN_VALUE`.
 */
- (CalendrumTzdataCalendrum_coreICalDuration *)ofMinutesMinutes:(int32_t)minutes __attribute__((swift_name("ofMinutes(minutes:)")));

/**
 * Creates a duration of [seconds] seconds; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [seconds] is `Int.MIN_VALUE`.
 */
- (CalendrumTzdataCalendrum_coreICalDuration *)ofSecondsSeconds:(int32_t)seconds __attribute__((swift_name("ofSeconds(seconds:)")));

/**
 * Creates a duration of [weeks] weeks; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [weeks] is `Int.MIN_VALUE`.
 */
- (CalendrumTzdataCalendrum_coreICalDuration *)ofWeeksWeeks:(int32_t)weeks __attribute__((swift_name("ofWeeks(weeks:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/DateTimePeriodSerializer))
*/
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod")))
@interface CalendrumTzdataKotlinx_datetimeDateTimePeriod : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeDateTimePeriodCompanion *companion __attribute__((swift_name("companion")));
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


/** The constants for every code of the library, and a lookup by identifier. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProblemCode.Companion")))
@interface CalendrumTzdataCalendrum_coreProblemCodeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants for every code of the library, and a lookup by identifier. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreProblemCodeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the code with the given [id], ignoring case.
 *
 * For an identifier the library knows this is the constant; for any other a new code is created with the
 * upper-case identifier and the severity [Severity.Warning].
 *
 * @throws IllegalArgumentException if [id] is empty.
 */
- (CalendrumTzdataCalendrum_coreProblemCode *)ofId:(NSString *)id __attribute__((swift_name("of(id:)")));

/**
 * `ICAL-O003` (Warning): the `REPEAT` of an alarm is beyond the limit of alarm repetitions. The extra
 * repetitions are dropped.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *AlarmRepeatLimit __attribute__((swift_name("AlarmRepeatLimit")));

/**
 * `ICAL-L022` (Warning): a `TZID` is not an IANA zone but a Windows name, a display name, a link or a vendor
 * prefix. Found by the linter, once per `TZID`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *AliasTimeZoneId __attribute__((swift_name("AliasTimeZoneId")));

/**
 * `ICAL-S002` (Warning): a bare CR is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *BareCr __attribute__((swift_name("BareCr")));

/**
 * `ICAL-S003` (Warning): a bare LF is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *BareLf __attribute__((swift_name("BareLf")));

/** `ICAL-V015` (Warning): a `BINARY` value without base64 padding. The padding is added. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *Base64Padding __attribute__((swift_name("Base64Padding")));

/** `ICAL-V014` (Warning): whitespace inside a `BINARY` value. It is skipped. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *Base64Whitespace __attribute__((swift_name("Base64Whitespace")));

/**
 * `ICAL-S004` (Warning): a blank line, which the format does not allow. The line is skipped. Reported once per
 * input.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *BlankLine __attribute__((swift_name("BlankLine")));

/**
 * `ICAL-S027` (Warning): the charset hint contradicts the data, for example a byte order mark of another
 * encoding or clear UTF-8 data under a single-byte hint. The data decides.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *CharsetHintContradictsData __attribute__((swift_name("CharsetHintContradictsData")));

/**
 * `ICAL-L006` (Error): a component inside a parent that does not allow it, or that the iTIP method forbids.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ComponentNotAllowed __attribute__((swift_name("ComponentNotAllowed")));

/** `ICAL-S020` (Warning): `BEGIN` or `END` has parameters. They are ignored. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ComponentParameters __attribute__((swift_name("ComponentParameters")));

/**
 * `ICAL-S005` (Warning): the input starts with a continuation line, a space or a tab in the first byte. That
 * one character is removed and the line starts the first logical line.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ContinuationAtStart __attribute__((swift_name("ContinuationAtStart")));

/**
 * `ICAL-L026` (Error): a content line holds a control character other than horizontal tab, in its name, a
 * parameter or its value, which [RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1) forbids.
 * Parsing keeps the line as it is and byte-stable writing writes it back, but canonical writing, jCal and the
 * builders refuse it. Found by the validator and the linter; the explanation never quotes the line.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ControlCharacter __attribute__((swift_name("ControlCharacter")));

/**
 * `ICAL-V017` (Warning): a recurrence rule has both `COUNT` and `UNTIL`, which the standard forbids. Both are
 * kept; the position is that of the second.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *CountAndUntil __attribute__((swift_name("CountAndUntil")));

/**
 * `ICAL-V008` (Warning): a `DATE-TIME` value has no time, as in `DTSTART:20260901` without `VALUE=DATE`. It is
 * read as a `DATE`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *DateAsDateTime __attribute__((swift_name("DateAsDateTime")));

/** `ICAL-V007` (Warning): a `DATE` value has a time. The time is dropped. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *DateWithTime __attribute__((swift_name("DateWithTime")));

/** `ICAL-L012` (Error): a `DURATION` with hours, minutes or seconds beside a `DTSTART` of type `DATE`. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *DateWithTimeDuration __attribute__((swift_name("DateWithTimeDuration")));

/**
 * `ICAL-S025` (Warning): the encoding was guessed from the data, by a heuristic or by the placement of zero
 * bytes, and the input is transcoded from there on. The explanation names the encoding and the confidence in
 * percent.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *DetectedCharset __attribute__((swift_name("DetectedCharset")));

/**
 * `ICAL-V010` (Warning): a `DURATION` has hours and seconds but no minutes, such as `PT1H30S`. The missing `0M`
 * is added.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *DurationSkipsMinutes __attribute__((swift_name("DurationSkipsMinutes")));

/**
 * `ICAL-V009` (Warning): a `DURATION` has time units without the `T` designator, such as `P90M`. It is read as
 * `PT90M`, since the standard has no months in durations.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *DurationWithoutTimeDesignator __attribute__((swift_name("DurationWithoutTimeDesignator")));

/**
 * `ICAL-V016` (Warning): an empty list item, as in `a,,b`, or an empty rule part, as in `FREQ=DAILY;`. It is
 * skipped.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *EmptyItem __attribute__((swift_name("EmptyItem")));

/**
 * `ICAL-L025` (Warning): an empty value of a non-text property, such as `DTEND:` or `RRULE:`. Found by the
 * linter.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *EmptyValue __attribute__((swift_name("EmptyValue")));

/**
 * `ICAL-L011` (Error): `DTEND` or `DUE` is not later than `DTSTART`. Not checked when a zone does not resolve.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *EndBeforeStart __attribute__((swift_name("EndBeforeStart")));

/**
 * `ICAL-S016` (Warning): an `END` closes a component that is open deeper in the stack than the top. The
 * components in between are closed implicitly, then the matching one. One problem per `END` line.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *EndClosesNested __attribute__((swift_name("EndClosesNested")));

/** `ICAL-S015` (Warning): an `END` line with no open component. The line is ignored. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *EndWithoutBegin __attribute__((swift_name("EndWithoutBegin")));

/** `ICAL-L008` (Error): properties that exclude each other occur together, such as `DTEND` and `DURATION`. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ExclusiveProperties __attribute__((swift_name("ExclusiveProperties")));

/**
 * `ICAL-V005` (Warning): a date or time is written in the extended ISO 8601 form, with separators. It is read
 * as the basic form; in strict mode it is an error.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ExtendedIsoForm __attribute__((swift_name("ExtendedIsoForm")));

/**
 * `ICAL-O004` (Warning): a floating time, a `DATE` or an unresolved `TZID` was met while no floating zone is
 * set. The occurrence is skipped.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *FloatingWithoutZone __attribute__((swift_name("FloatingWithoutZone")));

/**
 * `ICAL-R009` (Info): an `RDATE` or `EXDATE` is in another form than `DTSTART`, such as UTC against a time
 * zone. They are compared by local time.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *FormMismatch __attribute__((swift_name("FormMismatch")));

/**
 * `ICAL-V006` (Warning): a time has fractions of a second, as written by JavaScript generators. The fraction is
 * dropped.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *FractionalSeconds __attribute__((swift_name("FractionalSeconds")));

/**
 * `ICAL-S030` (Error): the input is longer than [io.github.calendrum.syntax.ParseLimits.maxInputOctets]. Reading
 * stops before the first logical line that does not end within that many bytes, and the result is truncated.
 * Always reported.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *InputTooLarge __attribute__((swift_name("InputTooLarge")));

/**
 * `ICAL-S019` (Warning): the name in `BEGIN` or `END` is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is, and the component is opened and closed by it, ignoring ASCII
 * case.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *InvalidComponentName __attribute__((swift_name("InvalidComponentName")));

/** `ICAL-J001` (Error): the input is not JSON. Nothing is read. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *InvalidJson __attribute__((swift_name("InvalidJson")));

/**
 * `ICAL-S008` (Warning): a property or parameter name is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *InvalidName __attribute__((swift_name("InvalidName")));

/**
 * `ICAL-S006` (Warning): a logical line contains an invalid UTF-8 sequence. Each maximal invalid subsequence is
 * replaced with U+FFFD; the line is not skipped. One problem per line.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *InvalidUtf8 __attribute__((swift_name("InvalidUtf8")));

/**
 * `ICAL-Z003` (Warning): a `VTIMEZONE` has no `TZID`, no observance with `DTSTART` and `TZOFFSETTO`, or more than
 * 200,000 transitions up to its last start (a hostile zone of endless observances). It is skipped, and the next
 * resolver of the chain looks for the `TZID`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *InvalidVTimeZone __attribute__((swift_name("InvalidVTimeZone")));

/**
 * `ICAL-V001` (Error): a value does not conform to the grammar of its type; no value is decoded. In strict mode
 * it follows the warning code of a lenient repair, with the same position.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *InvalidValue __attribute__((swift_name("InvalidValue")));

/**
 * `ICAL-Z005` (Warning): the current rules of the zone cannot be written as one `RRULE` per observance, so the
 * generated `VTIMEZONE` only approximates them. The full mode gives an exact one.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *IrregularTimeZone __attribute__((swift_name("IrregularTimeZone")));

/**
 * `ICAL-J007` (Error): jCal components are nested deeper than `JCalOptions.maxDepth` (16 by default). The
 * deeper component is skipped with everything inside it.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *JCalTooDeep __attribute__((swift_name("JCalTooDeep")));

/**
 * `ICAL-J005` (Warning): a value is not in the form of its type, such as a `date-time` that is not
 * `YYYY-MM-DDTHH:MM:SS`. The value is written as it is.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *JCalValueMismatch __attribute__((swift_name("JCalValueMismatch")));

/**
 * `ICAL-V004` (Warning): a time has the second 60, which the standard allows. The decoder replaces it by 59 or
 * by the next minute, depending on its policy.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *LeapSecond __attribute__((swift_name("LeapSecond")));

/** `ICAL-R008` (Info): `BYSECOND=60` has no meaning in local time. The value is ignored. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *LeapSecondIgnored __attribute__((swift_name("LeapSecondIgnored")));

/**
 * `ICAL-S010` (Error): a logical line is longer than [io.github.calendrum.syntax.ParseLimits.maxLineOctets]. The
 * whole line is skipped and the bytes beyond the limit are not copied.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *LineTooLong __attribute__((swift_name("LineTooLong")));

/**
 * `ICAL-S028` (Warning): the heuristic is less than 90% sure of the encoding, typically for a short text of
 * capital abbreviations where windows-1251 and KOI8-R look alike. The input is read in the chosen encoding; the
 * application may want to ask the user or pass a hint.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *LowCharsetConfidence __attribute__((swift_name("LowCharsetConfidence")));

/**
 * `ICAL-J002` (Error): an element is not a jCal component: not an array of a name, an array of properties and
 * an array of components, or the name is not valid. The component is skipped with its children.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MalformedJCalComponent __attribute__((swift_name("MalformedJCalComponent")));

/**
 * `ICAL-J004` (Warning): a parameter value is not a string or an array of strings, or the `value` parameter
 * appears in the parameter object. The parameter is skipped.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MalformedJCalParameter __attribute__((swift_name("MalformedJCalParameter")));

/**
 * `ICAL-J003` (Error): a property is not an array of a name, an object of parameters, a type and at least one
 * value, or a value has the wrong JSON type. The property is skipped.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MalformedJCalProperty __attribute__((swift_name("MalformedJCalProperty")));

/**
 * `ICAL-S012` (Warning): a parameter is malformed: it has no `=`, a quote inside an unquoted value, or text
 * after a closing quote. A parameter without `=` gets no values; otherwise the value is taken literally up to
 * the next delimiter.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MalformedParameter __attribute__((swift_name("MalformedParameter")));

/**
 * `ICAL-L016` (Error): a value does not decode as its type; the details are an `ICAL-V` problem from reading.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MalformedValue __attribute__((swift_name("MalformedValue")));

/**
 * `ICAL-L018` (Error): the calendar has no `METHOD`, or one that differs from the method of the iTIP rule set
 * in use.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MethodMismatch __attribute__((swift_name("MethodMismatch")));

/**
 * `ICAL-L020` (Error): the iTIP method is not defined for the component, such as `REFRESH` for a `VJOURNAL`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MethodNotDefined __attribute__((swift_name("MethodNotDefined")));

/**
 * `ICAL-S007` (Error): there is no colon outside quotes after the property name and its parameters. The line is
 * skipped.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MissingColon __attribute__((swift_name("MissingColon")));

/**
 * `ICAL-L007` (Error): a required nested component is missing, such as a calendar without components or a
 * `VTIMEZONE` without `STANDARD` or `DAYLIGHT`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MissingComponent __attribute__((swift_name("MissingComponent")));

/**
 * `ICAL-L009` (Error): a property lacks the one it requires, such as `RRULE` without `DTSTART` or `REPEAT`
 * without `DURATION`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MissingDependency __attribute__((swift_name("MissingDependency")));

/** `ICAL-L001` (Error): a required property is missing. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MissingProperty __attribute__((swift_name("MissingProperty")));

/**
 * `ICAL-M001` (Error): a component of a schema lacks a required property, or the first such property is broken.
 * The component is skipped. The explanation names the property, never its value.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MissingRequiredField __attribute__((swift_name("MissingRequiredField")));

/**
 * `ICAL-L017` (Error): a `TZID` has no `VTIMEZONE` with that identifier in the calendar ([RFC 5545
 * §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)). Reported once per `TZID`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *MissingTimeZone __attribute__((swift_name("MissingTimeZone")));

/**
 * `ICAL-V012` (Warning): the offset `-0000`, which [RFC 5545
 * §3.3.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.14) forbids. It is read as `+0000`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *NegativeZeroOffset __attribute__((swift_name("NegativeZeroOffset")));

/**
 * `ICAL-L015` (Error): the `DTSTART` of a `VTIMEZONE` observance is not a local time ([RFC 5545
 * §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *NotLocalTime __attribute__((swift_name("NotLocalTime")));

/**
 * `ICAL-V027` (Warning): a value that must be in UTC is not, as in `DTSTAMP`, `CREATED`, `LAST-MODIFIED` and an
 * absolute `TRIGGER`. It is kept as it is.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *NotUtc __attribute__((swift_name("NotUtc")));

/**
 * `ICAL-O005` (Warning): a component has no `DTSTART` (and a to-do no `DUE` either), so it has no occurrence.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *OccurrenceWithoutStart __attribute__((swift_name("OccurrenceWithoutStart")));

/**
 * `ICAL-V002` (Error): a field is out of range, such as a month, a day, an hour, a UTC offset, an integer
 * beyond 32 bits, or a `BYxxx` part of a recurrence rule. No value is decoded.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *OutOfRange __attribute__((swift_name("OutOfRange")));

/**
 * `ICAL-O001` (Warning): the `RECURRENCE-ID` of an override matches no instance of the master. The override is
 * kept as a single occurrence.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *OverrideWithoutInstance __attribute__((swift_name("OverrideWithoutInstance")));

/**
 * `ICAL-V028` (Warning): a parameter does not apply to the value, such as `TZID` on a `DATE` or `RELATED` on an
 * absolute `TRIGGER`. It is not applied.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ParameterNotAllowed __attribute__((swift_name("ParameterNotAllowed")));

/**
 * `ICAL-V022` (Warning): a `PERIOD` ends no later than it starts, or has a duration that is not positive. It is
 * kept.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *PeriodNotPositive __attribute__((swift_name("PeriodNotPositive")));

/**
 * `ICAL-L004` (Error): a known property is in a component where it does not belong, or that the iTIP method
 * forbids.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *PropertyNotAllowed __attribute__((swift_name("PropertyNotAllowed")));

/**
 * `ICAL-S021` (Warning): a property outside any component, before the first `BEGIN` or between calendars. The
 * property is not part of the tree, but a cursor reports it at depth 0.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *PropertyOutsideComponent __attribute__((swift_name("PropertyOutsideComponent")));

/**
 * `ICAL-V003` (Error): a recurrence rule has no `FREQ` part ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)). No value is decoded.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *RecurWithoutFreq __attribute__((swift_name("RecurWithoutFreq")));

/**
 * `ICAL-R001` (Warning): the number of occurrences reached the limit while the sequence was not over, or a rule
 * with `COUNT` passed as many dates before the start of a range. The sequence ends.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *RecurrenceLimitReached __attribute__((swift_name("RecurrenceLimitReached")));

/**
 * `ICAL-R002` (Warning): too many periods in a row produced no occurrence, as in
 * `FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30`. The sequence ends.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *RecurrenceNoMatch __attribute__((swift_name("RecurrenceNoMatch")));

/**
 * `ICAL-L003` (Warning): a property occurs more than once where the standard says it SHOULD NOT, such as
 * `RRULE`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *RepeatedProperty __attribute__((swift_name("RepeatedProperty")));

/** `ICAL-V018` (Warning): a part of a recurrence rule is repeated. The last one is kept. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *RepeatedRulePart __attribute__((swift_name("RepeatedRulePart")));

/** `ICAL-R004` (Warning): the `RSCALE` of the rule is not `GREGORIAN`. The rule is not expanded. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *RscaleNotSupported __attribute__((swift_name("RscaleNotSupported")));

/**
 * `ICAL-V019` (Warning): a rule part is not allowed in this rule, such as `BYWEEKNO` without `YEARLY`, or
 * `BYSETPOS` without another `BYxxx` part. The part is kept.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *RulePartNotAllowed __attribute__((swift_name("RulePartNotAllowed")));

/**
 * `ICAL-R003` (Warning): `DTSTART` is not an instance of its own rule, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined. What happens
 * is up to the `startMismatch` option of the recurrence options.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *StartNotInRule __attribute__((swift_name("StartNotInRule")));

/**
 * `ICAL-O002` (Info): an override with `RANGE=THISANDFUTURE` was applied to the later instances, on a
 * best-effort basis.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ThisAndFutureApplied __attribute__((swift_name("ThisAndFutureApplied")));

/**
 * `ICAL-S029` (Error): the time budget [io.github.calendrum.syntax.ParseLimits.maxTime] ran out. Reading stops at a
 * line boundary and the result is truncated. Always reported.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TimeBudgetExceeded __attribute__((swift_name("TimeBudgetExceeded")));

/**
 * `ICAL-R007` (Warning): the rule has a time part or a frequency shorter than a day while `DTSTART` is a
 * `DATE`. The time parts are ignored.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TimePartWithDate __attribute__((swift_name("TimePartWithDate")));

/**
 * `ICAL-Z002` (Info): the `TZID` was found through an alias, such as an IANA link, a Windows name or a
 * `/mozilla.org/` prefix.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TimeZoneAlias __attribute__((swift_name("TimeZoneAlias")));

/**
 * `ICAL-Z004` (Warning): the `TZID` was found only by a guess from a city in its name. The zone of the first
 * city mentioned is used.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TimeZoneGuessed __attribute__((swift_name("TimeZoneGuessed")));

/**
 * `ICAL-S022` (Error): components are nested deeper than [io.github.calendrum.syntax.ParseLimits.maxDepth]. The
 * subtree is skipped up to its `END`, with one problem per subtree.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TooDeep __attribute__((swift_name("TooDeep")));

/**
 * `ICAL-S023` (Error): the limit on components was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxComponents]). Open components are closed implicitly, reading stops
 * and the result is truncated. Always reported.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TooManyComponents __attribute__((swift_name("TooManyComponents")));

/**
 * `ICAL-S014` (Error): the limit on logical lines was reached ([io.github.calendrum.syntax.ParseLimits.maxLines]).
 * Reading stops and the result is truncated. Always reported, even beyond the problem limit.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TooManyLines __attribute__((swift_name("TooManyLines")));

/**
 * `ICAL-S013` (Warning): the limit on reported problems was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxProblems]). Further problems are only counted; see
 * [io.github.calendrum.syntax.ParseResult.problemCount].
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TooManyProblems __attribute__((swift_name("TooManyProblems")));

/** `ICAL-L002` (Error): a property occurs more often than allowed. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TooManyProperties __attribute__((swift_name("TooManyProperties")));

/**
 * `ICAL-S024` (Info): the input was transcoded to UTF-8 from the encoding named by a byte order mark or by a
 * charset hint. The explanation names the encoding and where it came from. Positions refer to the decoded text.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TranscodedInput __attribute__((swift_name("TranscodedInput")));

/**
 * `ICAL-V024` (Warning): a `TZID` parameter on a UTC `DATE-TIME`, which the standard forbids. The parameter is
 * not applied and the time is UTC.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *TzidWithUtc __attribute__((swift_name("TzidWithUtc")));

/**
 * `ICAL-S018` (Warning): the input ended while components were open, as in a truncated feed. All of them are
 * closed implicitly. Not reported when reading was stopped by a limit.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnclosedComponents __attribute__((swift_name("UnclosedComponents")));

/**
 * `ICAL-S009` (Warning): a quoted parameter value is not closed before the end of the line. The value is taken
 * literally up to the next comma, semicolon or colon, and parsing goes on.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnclosedQuote __attribute__((swift_name("UnclosedQuote")));

/**
 * `ICAL-L019` (Info): the `ACTION` of an alarm is not `AUDIO`, `DISPLAY` or `EMAIL`; such an alarm is to be
 * ignored.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnknownAlarmAction __attribute__((swift_name("UnknownAlarmAction")));

/**
 * `ICAL-S026` (Warning): the charset hint is not recognized, either an unknown name or an unsupported encoding.
 * The hint is ignored and the data decides.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnknownCharsetHint __attribute__((swift_name("UnknownCharsetHint")));

/**
 * `ICAL-V023` (Warning): a backslash in a `TEXT` value is followed by a character that is not an escape of [RFC
 * 5545 §3.3.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.11), or ends the value. Both characters are
 * kept.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnknownEscape __attribute__((swift_name("UnknownEscape")));

/**
 * `ICAL-J006` (Warning): the type of a property is not a type of RFC 5545 or RFC 7265. No `VALUE` parameter is
 * written and the value is kept as it is.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnknownJCalType __attribute__((swift_name("UnknownJCalType")));

/**
 * `ICAL-L005` (Info): a property known neither to the registry nor to the rule tables, and not an `X-`
 * property.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnknownProperty __attribute__((swift_name("UnknownProperty")));

/**
 * `ICAL-V020` (Warning): an unknown part in a recurrence rule, such as an `X-` name or a future registered
 * part. It is kept, in order, and written after the known ones.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnknownRulePart __attribute__((swift_name("UnknownRulePart")));

/**
 * `ICAL-Z001` (Warning): no resolver of the chain knows the `TZID`. The unknown-zone policy decides: floating
 * time, UTC or an exception.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnknownTimeZone __attribute__((swift_name("UnknownTimeZone")));

/** `ICAL-S017` (Warning): an `END` names a component that is not open. The line is ignored. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnmatchedEnd __attribute__((swift_name("UnmatchedEnd")));

/**
 * `ICAL-S011` (Error): the input is in an encoding the library does not read at all, such as UTF-7, UTF-EBCDIC,
 * SCSU, BOCU-1 or GB18030, recognized by its signature. Nothing is parsed. The explanation names the encoding.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UnsupportedEncoding __attribute__((swift_name("UnsupportedEncoding")));

/**
 * `ICAL-R005` (Warning): the type of `UNTIL` does not match `DTSTART`, such as a `DATE` against a `DATE-TIME`
 * or UTC against local. The two are compared in the closest sensible way.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UntilTypeMismatch __attribute__((swift_name("UntilTypeMismatch")));

/**
 * `ICAL-R006` (Info): `UNTIL` is in UTC while `DTSTART` has a time zone and no `untilOffset` is set, so `UNTIL`
 * is compared with local time.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UntilWithoutZone __attribute__((swift_name("UntilWithoutZone")));

/**
 * `ICAL-L024` (Info): an `ORGANIZER` or `ATTENDEE` with the scheme in upper case, `MAILTO:`. Found by the
 * linter.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UppercaseMailto __attribute__((swift_name("UppercaseMailto")));

/**
 * `ICAL-V021` (Warning): a `URI` or `CAL-ADDRESS` without a scheme, such as an address without `mailto:`. It is
 * kept as it is.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UriWithoutScheme __attribute__((swift_name("UriWithoutScheme")));

/** `ICAL-V011` (Warning): a `UTC-OFFSET` is written with a colon, such as `+03:00`. It is read without it. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *UtcOffsetWithColon __attribute__((swift_name("UtcOffsetWithColon")));

/** `ICAL-S001` (Info): the input starts with a UTF-8 byte order mark. The three bytes are skipped. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *Utf8Bom __attribute__((swift_name("Utf8Bom")));

/**
 * `ICAL-L013` (Error): a value outside the list of the standard, such as a component `STATUS`, `VERSION` or
 * `TRANSP`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ValueNotAllowed __attribute__((swift_name("ValueNotAllowed")));

/**
 * `ICAL-L014` (Error): a number outside its range, such as `PRIORITY` outside 0 to 9 or `PERCENT-COMPLETE`
 * outside 0 to 100.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ValueOutOfRange __attribute__((swift_name("ValueOutOfRange")));

/**
 * `ICAL-L010` (Error): the form of a value differs from that of a related one, such as a `DATE` `DTEND` with a
 * `DATE-TIME` `DTSTART`, or a non-UTC `UNTIL` with a zoned `DTSTART`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ValueTypeMismatch __attribute__((swift_name("ValueTypeMismatch")));

/**
 * `ICAL-V025` (Warning): the `VALUE` parameter names a type this property does not take, or is empty. The
 * default type of the property is read.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ValueTypeNotAllowed __attribute__((swift_name("ValueTypeNotAllowed")));

/**
 * `ICAL-V026` (Warning): a value of a type other than the default is not declared by `VALUE` or `ENCODING`, as
 * in `TRIGGER:19980101T050000Z`. It is read as the value of that type.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *ValueTypeNotDeclared __attribute__((swift_name("ValueTypeNotDeclared")));

/** `ICAL-L023` (Info): `X-WR-CALNAME` without the standard `NAME` of RFC 7986. Found by the linter. */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *VendorCalendarName __attribute__((swift_name("VendorCalendarName")));

/**
 * `ICAL-L021` (Warning): `X-WR-TIMEZONE` beside floating times, which clients understand in different ways.
 * Found by the linter.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *VendorTimeZone __attribute__((swift_name("VendorTimeZone")));

/**
 * `ICAL-V013` (Warning): spaces or tabs around a value or a list item. They are dropped. Not reported for
 * `TEXT` and `BINARY`.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProblemCode *Whitespace __attribute__((swift_name("Whitespace")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormat")))
@protocol CalendrumTzdataKotlinx_datetimeDateTimeFormat
@required
- (NSString *)formatValue:(id _Nullable)value __attribute__((swift_name("format(value:)")));
- (id<CalendrumTzdataKotlinAppendable>)formatToAppendable:(id<CalendrumTzdataKotlinAppendable>)appendable value:(id _Nullable)value __attribute__((swift_name("formatTo(appendable:value:)")));
- (id _Nullable)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (id _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilder")))
@protocol CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilder
@required
- (void)charsValue:(NSString *)value __attribute__((swift_name("chars(value:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithUtcOffset")))
@protocol CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset <CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)offsetFormat:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("offset(format:)")));
- (void)offsetHoursPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetHours(padding:)")));
- (void)offsetMinutesOfHourPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetMinutesOfHour(padding:)")));
- (void)offsetSecondsOfMinutePadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetSecondsOfMinute(padding:)")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol CalendrumTzdataKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<CalendrumTzdataKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<CalendrumTzdataKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol CalendrumTzdataKotlinx_serialization_coreKSerializer <CalendrumTzdataKotlinx_serialization_coreSerializationStrategy, CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy>
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDate.Companion")))
@interface CalendrumTzdataKotlinx_datetimeLocalDateCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeLocalDateCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)FormatBlock:(void (^)(id<CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithDate>))block __attribute__((swift_name("Format(block:)")));
- (CalendrumTzdataKotlinx_datetimeLocalDate *)fromEpochDaysEpochDays:(int32_t)epochDays __attribute__((swift_name("fromEpochDays(epochDays:)")));
- (CalendrumTzdataKotlinx_datetimeLocalDate *)fromEpochDaysEpochDays_:(int64_t)epochDays __attribute__((swift_name("fromEpochDays(epochDays_:)")));
- (CalendrumTzdataKotlinx_datetimeLocalDate *)parseInput:(id)input format:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("KotlinIterable")))
@protocol CalendrumTzdataKotlinIterable
@required
- (id<CalendrumTzdataKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
@end

__attribute__((swift_name("KotlinCollection")))
@protocol CalendrumTzdataKotlinCollection <CalendrumTzdataKotlinIterable>
@required
- (BOOL)containsElement:(id _Nullable)element __attribute__((swift_name("contains(element:)")));
- (BOOL)containsAllElements:(id)elements __attribute__((swift_name("containsAll(elements:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("Kotlinx_datetimeLocalDateProgression")))
@interface CalendrumTzdataKotlinx_datetimeLocalDateProgression : CalendrumTzdataBase <CalendrumTzdataKotlinCollection>
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeLocalDateProgressionCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)containsElement:(CalendrumTzdataKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(element:)")));
- (BOOL)containsAllElements:(id)elements __attribute__((swift_name("containsAll(elements:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (id<CalendrumTzdataKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalDate *first __attribute__((swift_name("first")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalDate *last __attribute__((swift_name("last")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinClosedRange")))
@protocol CalendrumTzdataKotlinClosedRange
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
@protocol CalendrumTzdataKotlinOpenEndRange
@required
- (BOOL)containsValue_:(id)value __attribute__((swift_name("contains(value_:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
@property (readonly) id endExclusive __attribute__((swift_name("endExclusive")));
@property (readonly) id start __attribute__((swift_name("start")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateRange")))
@interface CalendrumTzdataKotlinx_datetimeLocalDateRange : CalendrumTzdataKotlinx_datetimeLocalDateProgression <CalendrumTzdataKotlinClosedRange, CalendrumTzdataKotlinOpenEndRange>
- (instancetype)initWithStart:(CalendrumTzdataKotlinx_datetimeLocalDate *)start endInclusive:(CalendrumTzdataKotlinx_datetimeLocalDate *)endInclusive __attribute__((swift_name("init(start:endInclusive:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeLocalDateRangeCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)containsElement:(CalendrumTzdataKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(element:)")));
- (BOOL)containsValue:(CalendrumTzdataKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(value:)")));
- (BOOL)containsValue_:(CalendrumTzdataKotlinx_datetimeLocalDate *)element __attribute__((swift_name("contains(value_:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalDate *endExclusive __attribute__((swift_name("endExclusive"))) __attribute__((deprecated("This throws an exception if the exclusive end if not inside the platform-specific boundaries for LocalDate. The 'endInclusive' property does not throw and should be preferred.")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalDate *endInclusive __attribute__((swift_name("endInclusive")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalDate *start __attribute__((swift_name("start")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalTime.Companion")))
@interface CalendrumTzdataKotlinx_datetimeLocalTimeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeLocalTimeCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)FormatBuilder:(void (^)(id<CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithTime>))builder __attribute__((swift_name("Format(builder:)")));
- (CalendrumTzdataKotlinx_datetimeLocalTime *)fromMillisecondOfDayMillisecondOfDay:(int32_t)millisecondOfDay __attribute__((swift_name("fromMillisecondOfDay(millisecondOfDay:)")));
- (CalendrumTzdataKotlinx_datetimeLocalTime *)fromNanosecondOfDayNanosecondOfDay:(int64_t)nanosecondOfDay __attribute__((swift_name("fromNanosecondOfDay(nanosecondOfDay:)")));
- (CalendrumTzdataKotlinx_datetimeLocalTime *)fromSecondOfDaySecondOfDay:(int32_t)secondOfDay __attribute__((swift_name("fromSecondOfDay(secondOfDay:)")));
- (CalendrumTzdataKotlinx_datetimeLocalTime *)parseInput:(id)input format:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithYearMonth")))
@protocol CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithYearMonth <CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)monthNameNames:(CalendrumTzdataKotlinx_datetimeMonthNames *)names __attribute__((swift_name("monthName(names:)")));
- (void)monthNumberPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("monthNumber(padding:)")));
- (void)yearPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("year(padding:)")));
- (void)yearMonthFormat:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("yearMonth(format:)")));
- (void)yearTwoDigitsBaseYear:(int32_t)baseYear __attribute__((swift_name("yearTwoDigits(baseYear:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithDate")))
@protocol CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithDate <CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithYearMonth>
@required
- (void)dateFormat:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("date(format:)")));
- (void)dayPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("day(padding:)")));
- (void)dayOfMonthPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("dayOfMonth(padding:)"))) __attribute__((deprecated("Use 'day' instead")));
- (void)dayOfWeekNames:(CalendrumTzdataKotlinx_datetimeDayOfWeekNames *)names __attribute__((swift_name("dayOfWeek(names:)")));
- (void)dayOfYearPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("dayOfYear(padding:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithTime")))
@protocol CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithTime <CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)amPmHourPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("amPmHour(padding:)")));
- (void)amPmMarkerAm:(NSString *)am pm:(NSString *)pm __attribute__((swift_name("amPmMarker(am:pm:)")));
- (void)hourPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("hour(padding:)")));
- (void)minutePadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("minute(padding:)")));
- (void)secondPadding:(CalendrumTzdataKotlinx_datetimePadding *)padding __attribute__((swift_name("second(padding:)")));
- (void)secondFractionFixedLength:(int32_t)fixedLength __attribute__((swift_name("secondFraction(fixedLength:)")));
- (void)secondFractionMinLength:(int32_t)minLength maxLength:(int32_t)maxLength __attribute__((swift_name("secondFraction(minLength:maxLength:)")));
- (void)timeFormat:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("time(format:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithDateTime")))
@protocol CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithDateTime <CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithDate, CalendrumTzdataKotlinx_datetimeDateTimeFormatBuilderWithTime>
@required
- (void)dateTimeFormat:(id<CalendrumTzdataKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("dateTime(format:)")));
@end


/** Creates VCalendar values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVCalendar.Companion")))
@interface CalendrumTzdataCalendrum_coreVCalendarCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VCalendar values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVCalendarCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VCalendar, or returns `null` when it is not a `VCALENDAR` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVCalendar * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/**
 * Collects the properties and nested components of a new or edited calendar, with a Kotlin DSL and chained calls.
 * Get one from [VCalendar.builder] or [VCalendar.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVCalendar.Builder")))
@interface CalendrumTzdataCalendrum_coreVCalendarBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVCalendarBuilder *>

/** Creates the calendar. */
- (CalendrumTzdataCalendrum_coreVCalendar *)build __attribute__((swift_name("build()")));

/**
 * Sets [calScale] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)calScaleValue:(NSString * _Nullable)value __attribute__((swift_name("calScale(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)categoriesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds [event] as a event of the calendar. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)eventEvent:(CalendrumTzdataCalendrum_coreVEvent *)event __attribute__((swift_name("event(event:)")));

/**
 * Adds a event that [block] sets up.
 *
 * @param block sets up the new event.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)eventBlock:(void (^)(CalendrumTzdataCalendrum_coreVEventBuilder *))block __attribute__((swift_name("event(block:)")));

/** Edits the events of the calendar in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)eventsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVEvent *> *))block __attribute__((swift_name("events(block:)")));

/** Edits the free/busy components of the calendar in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)freeBusiesBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVFreeBusy *> *))block __attribute__((swift_name("freeBusies(block:)")));

/** Adds [freeBusy] as a free/busy component of the calendar. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)freeBusyFreeBusy:(CalendrumTzdataCalendrum_coreVFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/**
 * Adds a free/busy component that [block] sets up.
 *
 * @param block sets up the new free/busy component.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)freeBusyBlock:(void (^)(CalendrumTzdataCalendrum_coreVFreeBusyBuilder *))block __attribute__((swift_name("freeBusy(block:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)imageValue:(CalendrumTzdataCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/** Adds [journal] as a journal entry of the calendar. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)journalJournal:(CalendrumTzdataCalendrum_coreVJournal *)journal __attribute__((swift_name("journal(journal:)")));

/**
 * Adds a journal entry that [block] sets up.
 *
 * @param block sets up the new journal entry.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)journalBlock:(void (^)(CalendrumTzdataCalendrum_coreVJournalBuilder *))block __attribute__((swift_name("journal(block:)")));

/** Edits the journal entries of the calendar in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)journalsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVJournal *> *))block __attribute__((swift_name("journals(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)lastModifiedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/**
 * Sets [method] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)methodValue:(CalendrumTzdataCalendrum_coreMethod * _Nullable)value __attribute__((swift_name("method(value:)")));

/** Adds a `NAME` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/**
 * Sets [prodId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)prodIdValue:(NSString * _Nullable)value __attribute__((swift_name("prodId(value:)")));

/**
 * Sets [refreshInterval] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)refreshIntervalValue:(CalendrumTzdataCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("refreshInterval(value:)")));

/**
 * Sets [source] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)sourceValue:(NSString * _Nullable)value __attribute__((swift_name("source(value:)")));

/** Adds [timeZone] as a time zone of the calendar. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)timeZoneTimeZone:(CalendrumTzdataCalendrum_coreVTimeZone *)timeZone __attribute__((swift_name("timeZone(timeZone:)")));

/**
 * Adds a time zone that [block] sets up.
 *
 * @param block sets up the new time zone.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)timeZoneBlock:(void (^)(CalendrumTzdataCalendrum_coreVTimeZoneBuilder *))block __attribute__((swift_name("timeZone(block:)")));

/** Edits the time zones of the calendar in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)timeZonesBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVTimeZone *> *))block __attribute__((swift_name("timeZones(block:)")));

/** Adds [todo] as a to-do of the calendar. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)todoTodo:(CalendrumTzdataCalendrum_coreVTodo *)todo __attribute__((swift_name("todo(todo:)")));

/**
 * Adds a to-do that [block] sets up.
 *
 * @param block sets up the new to-do.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)todoBlock:(void (^)(CalendrumTzdataCalendrum_coreVTodoBuilder *))block __attribute__((swift_name("todo(block:)")));

/** Edits the to-dos of the calendar in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)todosBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVTodo *> *))block __attribute__((swift_name("todos(block:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * Sets [version] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVCalendarBuilder *)versionValue:(NSString * _Nullable)value __attribute__((swift_name("version(value:)")));

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
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy components of the calendar being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVFreeBusy *> *freeBusies __attribute__((swift_name("freeBusies")));

/** The journal entries of the calendar being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar being built (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreMethod * _Nullable method __attribute__((swift_name("method")));

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
@property CalendrumTzdataCalendrum_coreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar being built (`SOURCE`,
 * [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)); assigning `null` removes the property.
 */
@property NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones of the calendar being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos of the calendar being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVTodo *> *todos __attribute__((swift_name("todos")));

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
@interface CalendrumTzdataCalendrum_coreVEvent : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVEventCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this event changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this event.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVEvent *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVEventBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VEvent with an equal VEVENT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this event; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this event and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the event, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the event (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the event (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumTzdataCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreConference *> *conferences __attribute__((swift_name("conferences")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) CalendrumTzdataCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event (`DTEND`, [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The excluded dates of the event (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the event (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the event (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the event (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the event (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/**
 * The location text of the event (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the event, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the event, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the event (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the event (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the event (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the event, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the event (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the event (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@property (readonly) CalendrumTzdataCalendrum_coreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

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
@interface CalendrumTzdataCalendrum_coreVFreeBusy : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVFreeBusyCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this free/busy component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this free/busy component.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusy *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVFreeBusyBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VFreeBusy with an equal VFREEBUSY component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this free/busy component; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this free/busy component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attendees of the free/busy component (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The comments of the free/busy component (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The free/busy component nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The links of the free/busy component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the free/busy component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The styled descriptions of the free/busy component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@interface CalendrumTzdataCalendrum_coreImage : CalendrumTzdataBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumTzdataKotlinByteArray *)bytes formatType:(NSString *)formatType __attribute__((swift_name("init(bytes:formatType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri formatType:(NSString *)formatType __attribute__((swift_name("init(uri:formatType:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the inline content, or `null` when the image is given by a URI. */
- (CalendrumTzdataKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a Image equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreImageDisplay *> *display __attribute__((swift_name("display")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the image is inline (base64 encoded in the text form) and not given by a URI. */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumTzdataCalendrum_coreVJournal : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVJournalCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this journal entry changed by [block]. Properties and components that [block] does not touch
 * are the same objects as in this journal entry.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVJournal *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVJournalBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VJournal with an equal VJOURNAL component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this journal entry; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this journal entry and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the journal entry (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the journal entry (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumTzdataCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The images of the journal entry (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the journal entry (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the journal entry (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The additional dates of the journal entry (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the journal entry (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the journal entry (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreJournalStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the journal entry (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the journal entry (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@interface CalendrumTzdataCalendrum_coreMethod : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreMethodCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Method with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
@interface CalendrumTzdataCalendrum_coreVTodo : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVTodoCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this to-do changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this to-do.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVTodo *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVTodoBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTodo with an equal VTODO component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this to-do; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this to-do and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the to-do (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the to-do (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumTzdataCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreConference *> *conferences __attribute__((swift_name("conferences")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The excluded dates of the to-do (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the to-do (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the to-do (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the to-do (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the to-do (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The location nested in the to-do, in source order. */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the to-do (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the to-do (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the to-do (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreTodoStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the to-do (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the to-do (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
 * The result of decoding a property or parameter: a value, nothing, or a malformed one.
 *
 * Reading with `component[key]` gives only the value or `null`; `component.decode(key)` tells which of the three it was
 * and why.
 */
__attribute__((swift_name("Calendrum_coreDecoded")))
@protocol CalendrumTzdataCalendrum_coreDecoded
@required
@end


/**
 * How a codec decodes: leniently or strictly, and the factory for problems at the right position.
 *
 * @property isStrict whether a value that is only acceptable by repair is an error.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext")))
@interface CalendrumTzdataCalendrum_coreDecodeContext : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreDecodeContextCompanion *companion __attribute__((swift_name("companion")));

/** Creates a problem with [code] positioned [valueOffset] characters into the value of [property]. */
- (CalendrumTzdataCalendrum_coreProblem *)problemProperty:(CalendrumTzdataCalendrum_coreICalProperty *)property code:(CalendrumTzdataCalendrum_coreProblemCode *)code valueOffset:(int32_t)valueOffset __attribute__((swift_name("problem(property:code:valueOffset:)")));

/** Creates a problem with [code] positioned at the value of [parameter] of [property]. */
- (CalendrumTzdataCalendrum_coreProblem *)problemProperty:(CalendrumTzdataCalendrum_coreICalProperty *)property parameter:(CalendrumTzdataCalendrum_coreICalParameter *)parameter code:(CalendrumTzdataCalendrum_coreProblemCode *)code __attribute__((swift_name("problem(property:parameter:code:)")));

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
@interface CalendrumTzdataCalendrum_coreEncodeContext : CalendrumTzdataBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntArray")))
@interface CalendrumTzdataKotlinIntArray : CalendrumTzdataBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumTzdataInt *(^)(CalendrumTzdataInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int32_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumTzdataKotlinIntIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int32_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod.Companion")))
@interface CalendrumTzdataKotlinx_datetimeDateTimePeriodCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeDateTimePeriodCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumTzdataKotlinx_datetimeDateTimePeriod *)parseText:(NSString *)text __attribute__((swift_name("parse(text:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("KotlinAppendable")))
@protocol CalendrumTzdataKotlinAppendable
@required

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumTzdataKotlinAppendable>)appendValue:(unichar)value __attribute__((swift_name("append(value:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumTzdataKotlinAppendable>)appendValue_:(id _Nullable)value __attribute__((swift_name("append(value_:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumTzdataKotlinAppendable>)appendValue:(id _Nullable)value startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("append(value:startIndex:endIndex:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimePadding")))
@interface CalendrumTzdataKotlinx_datetimePadding : CalendrumTzdataKotlinEnum<CalendrumTzdataKotlinx_datetimePadding *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumTzdataKotlinx_datetimePadding *none __attribute__((swift_name("none")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimePadding *zero __attribute__((swift_name("zero")));
@property (class, readonly) CalendrumTzdataKotlinx_datetimePadding *space __attribute__((swift_name("space")));
+ (CalendrumTzdataKotlinArray<CalendrumTzdataKotlinx_datetimePadding *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumTzdataKotlinx_datetimePadding *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol CalendrumTzdataKotlinx_serialization_coreEncoder
@required
- (id<CalendrumTzdataKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreEncoder>)encodeInlineDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("encodeInline(descriptor:)")));
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
- (void)encodeNullableSerializableValueSerializer:(id<CalendrumTzdataKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<CalendrumTzdataKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) CalendrumTzdataKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol CalendrumTzdataKotlinx_serialization_coreSerialDescriptor
@required

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSArray<id<CalendrumTzdataKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));

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
@property (readonly) NSArray<id<CalendrumTzdataKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));

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
@property (readonly) CalendrumTzdataKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol CalendrumTzdataKotlinx_serialization_coreDecoder
@required
- (id<CalendrumTzdataKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<CalendrumTzdataKotlinx_serialization_coreDecoder>)decodeInlineDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeInline(descriptor:)")));
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
- (CalendrumTzdataKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) CalendrumTzdataKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateProgression.Companion")))
@interface CalendrumTzdataKotlinx_datetimeLocalDateProgressionCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeLocalDateProgressionCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDateRange.Companion")))
@interface CalendrumTzdataKotlinx_datetimeLocalDateRangeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeLocalDateRangeCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumTzdataKotlinx_datetimeLocalDateRange *EMPTY __attribute__((swift_name("EMPTY")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeekNames")))
@interface CalendrumTzdataKotlinx_datetimeDayOfWeekNames : CalendrumTzdataBase
- (instancetype)initWithNames:(NSArray<NSString *> *)names __attribute__((swift_name("init(names:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMonday:(NSString *)monday tuesday:(NSString *)tuesday wednesday:(NSString *)wednesday thursday:(NSString *)thursday friday:(NSString *)friday saturday:(NSString *)saturday sunday:(NSString *)sunday __attribute__((swift_name("init(monday:tuesday:wednesday:thursday:friday:saturday:sunday:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeDayOfWeekNamesCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonthNames")))
@interface CalendrumTzdataKotlinx_datetimeMonthNames : CalendrumTzdataBase
- (instancetype)initWithNames:(NSArray<NSString *> *)names __attribute__((swift_name("init(names:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithJanuary:(NSString *)january february:(NSString *)february march:(NSString *)march april:(NSString *)april may:(NSString *)may june:(NSString *)june july:(NSString *)july august:(NSString *)august september:(NSString *)september october:(NSString *)october november:(NSString *)november december:(NSString *)december __attribute__((swift_name("init(january:february:march:april:may:june:july:august:september:october:november:december:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumTzdataKotlinx_datetimeMonthNamesCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));
@end


/**
 * Collects the properties and nested components of a new or edited event, with a Kotlin DSL and chained calls. Get
 * one from [VEvent.builder] or [VEvent.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVEvent.Builder")))
@interface CalendrumTzdataCalendrum_coreVEventBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVEventBuilder *>

/** Adds [alarm] as a alarm of the event. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)alarmAlarm:(CalendrumTzdataCalendrum_coreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)alarmBlock:(void (^)(CalendrumTzdataCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the event in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)alarmsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)attachAttach:(id<CalendrumTzdataCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)attendeeAttendee:(CalendrumTzdataCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)attendeesBlock:(void (^)(CalendrumTzdataCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the event.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults to generate
 *   them.
 * @throws IllegalStateException if `UNTIL` of the rule does not suit the type of `DTSTART`.
 */
- (CalendrumTzdataCalendrum_coreVEvent *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)categoriesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)classificationValue:(CalendrumTzdataCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)conferenceValue:(CalendrumTzdataCalendrum_coreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)createdValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)durationValue:(CalendrumTzdataCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)endValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)exdateDates:(CalendrumTzdataKotlinArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)geoValue:(CalendrumTzdataCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)imageValue:(CalendrumTzdataCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)lastModifiedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the event. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)locationValue:(CalendrumTzdataCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)locationBlock:(void (^)(CalendrumTzdataCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the event in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)locationsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)organizerValue:(CalendrumTzdataCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the event. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)participantValue:(CalendrumTzdataCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)participantBlock:(void (^)(CalendrumTzdataCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the event in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)participantsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)priorityValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)rdateDates:(CalendrumTzdataKotlinArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)rdatePeriods:(CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)recurrenceIdValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)requestStatusStatus:(CalendrumTzdataCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the event. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)resourceComponentValue:(CalendrumTzdataCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)resourceComponentBlock:(void (^)(CalendrumTzdataCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the event in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)resourceComponentsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)resourcesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)rruleValue:(CalendrumTzdataCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)rruleFrequency:(CalendrumTzdataCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumTzdataCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)sequenceValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)stampValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)startValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)statusValue:(CalendrumTzdataCalendrum_coreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)structuredDataValue:(CalendrumTzdataCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)styledDescriptionValue:(CalendrumTzdataCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [transparency] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)transparencyValue:(CalendrumTzdataCalendrum_coreTransparency * _Nullable)value __attribute__((swift_name("transparency(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the event being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the event being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property CalendrumTzdataCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event being built (`DTEND`,
 * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The geographic position of the event being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the event being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the event being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the event being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the event being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the event being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the event being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the event being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

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
@property CalendrumTzdataCalendrum_coreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

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
 * Collects the properties and nested components of a new or edited free/busy component, with a Kotlin DSL and
 * chained calls. Get one from [VFreeBusy.builder] or [VFreeBusy.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVFreeBusy.Builder")))
@interface CalendrumTzdataCalendrum_coreVFreeBusyBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVFreeBusyBuilder *>

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)attendeeAttendee:(CalendrumTzdataCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)attendeesBlock:(void (^)(CalendrumTzdataCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the free/busy component.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusy *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [contact] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)contactValue:(NSString * _Nullable)value __attribute__((swift_name("contact(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)endValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds [freeBusy] as a free/busy component of the free/busy component. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)freeBusyFreeBusy:(CalendrumTzdataCalendrum_coreFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/** Adds [periods] as a free/busy component of the free/busy component. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)freeBusyPeriods:(CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("freeBusy(periods:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the free/busy component. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)locationValue:(CalendrumTzdataCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)locationBlock:(void (^)(CalendrumTzdataCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)locationsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)organizerValue:(CalendrumTzdataCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the free/busy component. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)participantValue:(CalendrumTzdataCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)participantBlock:(void (^)(CalendrumTzdataCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)participantsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)requestStatusStatus:(CalendrumTzdataCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the free/busy component. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)resourceComponentValue:(CalendrumTzdataCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)resourceComponentBlock:(void (^)(CalendrumTzdataCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)resourceComponentsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)stampValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)startValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)styledDescriptionValue:(CalendrumTzdataCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The locations of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/** The resources of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

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
@interface CalendrumTzdataCalendrum_coreVJournalBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVJournalBuilder *>

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)attachAttach:(id<CalendrumTzdataCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)attendeeAttendee:(CalendrumTzdataCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)attendeesBlock:(void (^)(CalendrumTzdataCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the journal entry.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumTzdataCalendrum_coreVJournal *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)categoriesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)classificationValue:(CalendrumTzdataCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)createdValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)exdateDates:(CalendrumTzdataKotlinArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)imageValue:(CalendrumTzdataCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)lastModifiedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the journal entry. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)locationValue:(CalendrumTzdataCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)locationBlock:(void (^)(CalendrumTzdataCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the journal entry in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)locationsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)organizerValue:(CalendrumTzdataCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the journal entry. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)participantValue:(CalendrumTzdataCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)participantBlock:(void (^)(CalendrumTzdataCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the journal entry in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)participantsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)rdateDates:(CalendrumTzdataKotlinArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)rdatePeriods:(CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)recurrenceIdValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)requestStatusStatus:(CalendrumTzdataCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the journal entry. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)resourceComponentValue:(CalendrumTzdataCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)resourceComponentBlock:(void (^)(CalendrumTzdataCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the journal entry in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)resourceComponentsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)rruleValue:(CalendrumTzdataCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)rruleFrequency:(CalendrumTzdataCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumTzdataCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)sequenceValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)stampValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)startValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)statusValue:(CalendrumTzdataCalendrum_coreJournalStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)structuredDataValue:(CalendrumTzdataCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)styledDescriptionValue:(CalendrumTzdataCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the journal entry being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The time of the last modification of the journal entry being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the journal entry being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the journal entry being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The recurrence identifier of the journal entry being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the journal entry being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreJournalStatus * _Nullable status __attribute__((swift_name("status")));

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
 * Collects the properties and nested components of a new or edited to-do, with a Kotlin DSL and chained calls. Get
 * one from [VTodo.builder] or [VTodo.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTodo.Builder")))
@interface CalendrumTzdataCalendrum_coreVTodoBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVTodoBuilder *>

/** Adds [alarm] as a alarm of the to-do. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)alarmAlarm:(CalendrumTzdataCalendrum_coreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)alarmBlock:(void (^)(CalendrumTzdataCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the to-do in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)alarmsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)attachAttach:(id<CalendrumTzdataCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)attendeeAttendee:(CalendrumTzdataCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)attendeesBlock:(void (^)(CalendrumTzdataCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the to-do.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumTzdataCalendrum_coreVTodo *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)categoriesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)classificationValue:(CalendrumTzdataCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/**
 * Sets [completed] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)completedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("completed(value:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)conferenceValue:(CalendrumTzdataCalendrum_coreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)createdValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [due] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)dueValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("due(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)durationValue:(CalendrumTzdataCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)exdateDates:(CalendrumTzdataKotlinArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)geoValue:(CalendrumTzdataCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)imageValue:(CalendrumTzdataCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)lastModifiedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the to-do. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)locationValue:(CalendrumTzdataCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)locationBlock:(void (^)(CalendrumTzdataCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the to-do in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)locationsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)organizerValue:(CalendrumTzdataCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the to-do. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)participantValue:(CalendrumTzdataCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)participantBlock:(void (^)(CalendrumTzdataCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the to-do in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)participantsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [percentComplete] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)percentCompleteValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("percentComplete(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)priorityValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)rdateDates:(CalendrumTzdataKotlinArray<id<CalendrumTzdataCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)rdatePeriods:(CalendrumTzdataKotlinArray<CalendrumTzdataCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)recurrenceIdValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)requestStatusStatus:(CalendrumTzdataCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the to-do. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)resourceComponentValue:(CalendrumTzdataCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)resourceComponentBlock:(void (^)(CalendrumTzdataCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the to-do in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)resourceComponentsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)resourcesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)rruleValue:(CalendrumTzdataCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)rruleFrequency:(CalendrumTzdataCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumTzdataCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)sequenceValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)stampValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)startValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)statusValue:(CalendrumTzdataCalendrum_coreTodoStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)structuredDataValue:(CalendrumTzdataCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)styledDescriptionValue:(CalendrumTzdataCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the to-do being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the to-do being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));

/**
 * The creation time of the to-do being built (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do being built (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The geographic position of the to-do being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the to-do being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the to-do being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the to-do being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the to-do being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do being built (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the to-do being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the to-do being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the to-do being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreTodoStatus * _Nullable status __attribute__((swift_name("status")));

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


/** Creates VEvent values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVEvent.Companion")))
@interface CalendrumTzdataCalendrum_coreVEventCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VEvent values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVEventCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVEventBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VEvent, or returns `null` when it is not a `VEVENT` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVEvent * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumTzdataCalendrum_coreVAlarm : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVAlarmCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this alarm changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this alarm.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVAlarm *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VAlarm with an equal VALARM component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this alarm; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this alarm and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The time the alarm was acknowledged of the alarm (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm (`ACTION`, [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attachments of the alarm (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the alarm (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumTzdataCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The links of the alarm (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the alarm, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The number of repetitions of the alarm (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable repeat __attribute__((swift_name("repeat")));

/**
 * The styled descriptions of the alarm (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

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
@protocol CalendrumTzdataCalendrum_coreAttach
@required

/** The media type of the attachment (`FMTTYPE`), or `null`. */
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumTzdataCalendrum_coreAttendee : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreAttendeeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Attendee equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this attendee, parameters included. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) CalendrumTzdataCalendrum_coreCuType *cuType __attribute__((swift_name("cuType")));
@property (readonly) NSArray<NSString *> *delegatedFrom __attribute__((swift_name("delegatedFrom")));
@property (readonly) NSArray<NSString *> *delegatedTo __attribute__((swift_name("delegatedTo")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<NSString *> *member __attribute__((swift_name("member")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumTzdataCalendrum_corePartStat *partStat __attribute__((swift_name("partStat")));
@property (readonly) CalendrumTzdataCalendrum_coreRole *role __attribute__((swift_name("role")));
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
@interface CalendrumTzdataCalendrum_coreClassification : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreClassificationCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreConference : CalendrumTzdataBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri features:(NSArray<CalendrumTzdataCalendrum_coreConferenceFeature *> *)features label:(NSString * _Nullable)label __attribute__((swift_name("init(uri:features:label:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Conference equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreConferenceFeature *> *features __attribute__((swift_name("features")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumTzdataCalendrum_coreGeo : CalendrumTzdataBase
- (instancetype)initWithLatitude:(double)latitude longitude:(double)longitude __attribute__((swift_name("init(latitude:longitude:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Geo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double latitude __attribute__((swift_name("latitude")));
@property (readonly) double longitude __attribute__((swift_name("longitude")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumTzdataCalendrum_coreVLocation : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVLocationCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this location component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this location component.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVLocation *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VLocation with an equal VLOCATION component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this location component; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this location component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the location component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The structured data of the location component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

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
@interface CalendrumTzdataCalendrum_coreOrganizer : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreOrganizerCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Organizer equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this organizer, parameters included. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumTzdataCalendrum_coreParticipant : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreParticipantCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this participant component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this participant component.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreParticipant *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a Participant with an equal PARTICIPANT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this participant component; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this participant component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the participant component (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumTzdataCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

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
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the participant component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/**
 * The location texts of the participant component (`LOCATION` properties,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *locationTexts __attribute__((swift_name("locationTexts")));

/** The locations nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataInt * _Nullable priority __attribute__((swift_name("priority")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the participant component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the participant component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the participant component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@interface CalendrumTzdataCalendrum_coreRequestStatus : CalendrumTzdataBase
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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumTzdataCalendrum_coreVResource : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreVResourceCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this resource component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this resource component.
 *
 * @param block changes the builder.
 */
- (CalendrumTzdataCalendrum_coreVResource *)editBlock:(void (^)(CalendrumTzdataCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VResource with an equal VRESOURCE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this resource component; unchanged nodes are shared with it. */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this resource component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)toBuilderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumTzdataCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the resource component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The resource type of the resource component (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumTzdataCalendrum_coreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

/**
 * The structured data of the resource component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The globally unique identifier of the resource component (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable uid __attribute__((swift_name("uid")));
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
@interface CalendrumTzdataCalendrum_coreEventStatus : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreEventStatusCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreStructuredData : CalendrumTzdataBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumTzdataCalendrum_coreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumTzdataKotlinByteArray *)bytes formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(bytes:formatType:schema:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumTzdataCalendrum_coreValueType *)valueType formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(value:valueType:formatType:schema:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the binary content, or `null` when the data is text or a URI. */
- (CalendrumTzdataKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a StructuredData equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the data is binary (base64 encoded in the text form). */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable schema __attribute__((swift_name("schema")));
@property (readonly) NSString * _Nullable text __attribute__((swift_name("text")));
@property (readonly) CalendrumTzdataCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
@interface CalendrumTzdataCalendrum_coreStyledDescription : CalendrumTzdataBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumTzdataCalendrum_coreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumTzdataCalendrum_coreValueType *)valueType formatType:(NSString *)formatType __attribute__((swift_name("init(value:valueType:formatType:)"))) __attribute__((objc_designated_initializer));

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
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumTzdataCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
@interface CalendrumTzdataCalendrum_coreTransparency : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreTransparencyCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreVFreeBusyCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VFreeBusy values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVFreeBusyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusyBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VFreeBusy, or returns `null` when it is not a `VFREEBUSY` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVFreeBusy * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumTzdataCalendrum_coreFreeBusy : CalendrumTzdataBase
- (instancetype)initWithPeriods:(NSArray<CalendrumTzdataCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("init(periods:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithType:(CalendrumTzdataCalendrum_coreFbType *)type periods:(NSArray<CalendrumTzdataCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("init(type:periods:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a FreeBusy equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalPeriod *> *periods __attribute__((swift_name("periods")));
@property (readonly) CalendrumTzdataCalendrum_coreFbType *type __attribute__((swift_name("type")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface CalendrumTzdataKotlinByteArray : CalendrumTzdataBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumTzdataByte *(^)(CalendrumTzdataInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumTzdataKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
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
@interface CalendrumTzdataCalendrum_coreImageDisplay : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreImageDisplayCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreVJournalCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VJournal values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVJournalCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVJournalBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VJournal, or returns `null` when it is not a `VJOURNAL` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVJournal * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumTzdataCalendrum_coreJournalStatus : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreJournalStatusCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreMethodCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Method and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreMethodCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Method holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreMethod *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ADD`. The organizer adds instances to an existing series. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *Add __attribute__((swift_name("Add")));

/** `CANCEL`. The organizer cancels a component or some of its instances. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *Cancel __attribute__((swift_name("Cancel")));

/** `COUNTER`. An attendee proposes a change. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *Counter __attribute__((swift_name("Counter")));

/** `DECLINECOUNTER`. The organizer declines a proposed change. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *DeclineCounter __attribute__((swift_name("DeclineCounter")));

/** `PUBLISH`. Publishes a component to anyone, without expecting an answer. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *Publish __attribute__((swift_name("Publish")));

/** `REFRESH`. An attendee asks for the latest version. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *Refresh __attribute__((swift_name("Refresh")));

/** `REPLY`. An attendee answers a request. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *Reply __attribute__((swift_name("Reply")));

/** `REQUEST`. Asks for attendance, or updates a component sent before. */
@property (readonly) CalendrumTzdataCalendrum_coreMethod *Request __attribute__((swift_name("Request")));
@end


/** Creates VTodo values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTodo.Companion")))
@interface CalendrumTzdataCalendrum_coreVTodoCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTodo values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVTodoCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVTodoBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTodo, or returns `null` when it is not a `VTODO` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVTodo * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumTzdataCalendrum_coreTodoStatus : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreTodoStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a TodoStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The two contexts. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext.Companion")))
@interface CalendrumTzdataCalendrum_coreDecodeContextCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The two contexts. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreDecodeContextCompanion *shared __attribute__((swift_name("shared")));

/** Reads known quirks of real-world data with a warning and a repair. */
@property (readonly) CalendrumTzdataCalendrum_coreDecodeContext *Lenient __attribute__((swift_name("Lenient")));

/** Turns every repair into an error: the value is rejected. */
@property (readonly) CalendrumTzdataCalendrum_coreDecodeContext *Strict __attribute__((swift_name("Strict")));
@end

__attribute__((swift_name("KotlinIntIterator")))
@interface CalendrumTzdataKotlinIntIterator : CalendrumTzdataBase <CalendrumTzdataKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumTzdataInt *)next __attribute__((swift_name("next()")));
- (int32_t)nextInt __attribute__((swift_name("nextInt()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol CalendrumTzdataKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumTzdataKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumTzdataKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) CalendrumTzdataKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface CalendrumTzdataKotlinx_serialization_coreSerializersModule : CalendrumTzdataBase

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)dumpToCollector:(id<CalendrumTzdataKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumTzdataKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<CalendrumTzdataKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<CalendrumTzdataKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumTzdataKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumTzdataKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumTzdataKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end

__attribute__((swift_name("KotlinAnnotation")))
@protocol CalendrumTzdataKotlinAnnotation
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface CalendrumTzdataKotlinx_serialization_coreSerialKind : CalendrumTzdataBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol CalendrumTzdataKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<CalendrumTzdataKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<CalendrumTzdataKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) CalendrumTzdataKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface CalendrumTzdataKotlinNothing : CalendrumTzdataBase
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeekNames.Companion")))
@interface CalendrumTzdataKotlinx_datetimeDayOfWeekNamesCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeDayOfWeekNamesCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumTzdataKotlinx_datetimeDayOfWeekNames *ENGLISH_ABBREVIATED __attribute__((swift_name("ENGLISH_ABBREVIATED")));
@property (readonly) CalendrumTzdataKotlinx_datetimeDayOfWeekNames *ENGLISH_FULL __attribute__((swift_name("ENGLISH_FULL")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonthNames.Companion")))
@interface CalendrumTzdataKotlinx_datetimeMonthNamesCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataKotlinx_datetimeMonthNamesCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumTzdataKotlinx_datetimeMonthNames *ENGLISH_ABBREVIATED __attribute__((swift_name("ENGLISH_ABBREVIATED")));
@property (readonly) CalendrumTzdataKotlinx_datetimeMonthNames *ENGLISH_FULL __attribute__((swift_name("ENGLISH_FULL")));
@end


/**
 * Collects the properties and nested components of a new or edited alarm, with a Kotlin DSL and chained calls. Get
 * one from [VAlarm.builder] or [VAlarm.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVAlarm.Builder")))
@interface CalendrumTzdataCalendrum_coreVAlarmBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVAlarmBuilder *>

/**
 * Sets [acknowledged] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)acknowledgedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("acknowledged(value:)")));

/**
 * Sets [action] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)actionValue:(CalendrumTzdataCalendrum_coreAlarmAction * _Nullable)value __attribute__((swift_name("action(value:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)attachAttach:(id<CalendrumTzdataCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)attendeeAttendee:(CalendrumTzdataCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)attendeesBlock:(void (^)(CalendrumTzdataCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Makes this an `AUDIO` alarm: sets `ACTION` and [trigger]; the platform chooses the sound unless you attach
 * one.
 *
 * @param trigger when the alarm fires.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)audioTrigger:(id<CalendrumTzdataCalendrum_coreTrigger>)trigger __attribute__((swift_name("audio(trigger:)")));

/** Creates the alarm. */
- (CalendrumTzdataCalendrum_coreVAlarm *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Makes this a `DISPLAY` alarm: sets `ACTION`, [trigger] and the `DESCRIPTION` shown to the user.
 *
 * @param trigger when the alarm fires.
 * @param description the text to display.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)displayTrigger:(id<CalendrumTzdataCalendrum_coreTrigger>)trigger description:(NSString *)description __attribute__((swift_name("display(trigger:description:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)durationValue:(CalendrumTzdataCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Makes this an `EMAIL` alarm: sets `ACTION`, [trigger], the `SUMMARY` that becomes the subject and the
 * `DESCRIPTION` that becomes the body.
 *
 * @param trigger when the alarm fires.
 * @param summary the subject of the mail.
 * @param description the body of the mail.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)emailTrigger:(id<CalendrumTzdataCalendrum_coreTrigger>)trigger summary:(NSString *)summary description:(NSString *)description __attribute__((swift_name("email(trigger:summary:description:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the alarm. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)locationValue:(CalendrumTzdataCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)locationBlock:(void (^)(CalendrumTzdataCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the alarm in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)locationsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [proximity] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)proximityValue:(CalendrumTzdataCalendrum_coreProximity * _Nullable)value __attribute__((swift_name("proximity(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [repeat] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)repeatValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("repeat(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)styledDescriptionValue:(CalendrumTzdataCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [trigger] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)triggerValue:(id<CalendrumTzdataCalendrum_coreTrigger> _Nullable)value __attribute__((swift_name("trigger(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * The time the alarm was acknowledged of the alarm being built (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)); assigning `null` removes the property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm being built (`ACTION`,
 * [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property CalendrumTzdataCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/** The locations of the alarm being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm being built (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)); assigning `null` removes the property.
 */
@property CalendrumTzdataCalendrum_coreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

/**
 * The number of repetitions of the alarm being built (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable repeat __attribute__((swift_name("repeat")));

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
@property id<CalendrumTzdataCalendrum_coreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

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
@interface CalendrumTzdataCalendrum_coreAttendeeBuilder : CalendrumTzdataBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the attendee.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumTzdataCalendrum_coreAttendee *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [cuType] to [value] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)cuTypeValue:(CalendrumTzdataCalendrum_coreCuType *)value __attribute__((swift_name("cuType(value:)")));

/** Sets [delegatedFrom] to [values] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)delegatedFromValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedFrom(values:)")));

/** Sets [delegatedTo] to [values] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)delegatedToValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedTo(values:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/** Sets [member] to [values] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)memberValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("member(values:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)parameterName:(NSString *)name values:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Sets [partStat] to [value] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)partStatValue:(CalendrumTzdataCalendrum_corePartStat *)value __attribute__((swift_name("partStat(value:)")));

/** Removes the parameter [name]. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [role] to [value] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)roleValue:(CalendrumTzdataCalendrum_coreRole *)value __attribute__((swift_name("role(value:)")));

/** Sets [rsvp] to [value] and returns this builder. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)rsvpValue:(BOOL)value __attribute__((swift_name("rsvp(value:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The calendar address. */
@property NSString *address __attribute__((swift_name("address")));

/** The common name (`CN`); assigning `null` removes the parameter. */
@property NSString * _Nullable cn __attribute__((swift_name("cn")));

/** The calendar user type (`CUTYPE`); [CuType.Individual] when absent. */
@property CalendrumTzdataCalendrum_coreCuType *cuType __attribute__((swift_name("cuType")));

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
@property CalendrumTzdataCalendrum_corePartStat *partStat __attribute__((swift_name("partStat")));

/** The role (`ROLE`); [Role.ReqParticipant] when absent. */
@property CalendrumTzdataCalendrum_coreRole *role __attribute__((swift_name("role")));

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
@interface CalendrumTzdataCalendrum_coreAttendeeList : CalendrumTzdataBase

/** Adds [attendee] after the existing ones. */
- (void)addAttendee:(CalendrumTzdataCalendrum_coreAttendee *)attendee __attribute__((swift_name("add(attendee:)")));

/**
 * Adds an attendee with the address [address], set up by [block].
 *
 * @param address the calendar address of the new attendee.
 * @param block sets up the attendee.
 */
- (void)addAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("add(address:block:)")));

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
- (BOOL)replaceAddress:(NSString *)address block:(void (^)(CalendrumTzdataCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("replace(address:block:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreAttendee *> *items __attribute__((swift_name("items")));
@end


/**
 * Collects the properties and nested components of a new or edited location component, with a Kotlin DSL and
 * chained calls. Get one from [VLocation.builder] or [VLocation.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVLocation.Builder")))
@interface CalendrumTzdataCalendrum_coreVLocationBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVLocationBuilder *>

/**
 * Creates the location component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumTzdataCalendrum_coreVLocation *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)geoValue:(CalendrumTzdataCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [locationTypes] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)locationTypesValue:(NSArray<NSString *> * _Nullable)value __attribute__((swift_name("locationTypes(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)structuredDataValue:(CalendrumTzdataCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

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
@property CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

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
@interface CalendrumTzdataCalendrum_coreOrganizerBuilder : CalendrumTzdataBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the organizer.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumTzdataCalendrum_coreOrganizer *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)parameterName:(NSString *)name values:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Removes the parameter [name]. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

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
@interface CalendrumTzdataCalendrum_coreParticipantBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreParticipantBuilder *>

/** Adds [value] as an `ATTACH` property. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)attachValue:(id<CalendrumTzdataCalendrum_coreAttach>)value __attribute__((swift_name("attach(value:)")));

/**
 * Creates the participant.
 *
 * @throws IllegalStateException if `UID` or `PARTICIPANT-TYPE` is missing and the builder has no defaults for
 *   the `UID`.
 */
- (CalendrumTzdataCalendrum_coreParticipant *)build __attribute__((swift_name("build()")));

/**
 * Sets [calendarAddress] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)calendarAddressValue:(NSString * _Nullable)value __attribute__((swift_name("calendarAddress(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)categoriesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)createdValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)geoValue:(CalendrumTzdataCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)lastModifiedValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the participant component. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)locationValue:(CalendrumTzdataCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)locationBlock:(void (^)(CalendrumTzdataCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Adds a `LOCATION` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)locationTextText:(NSString *)text __attribute__((swift_name("locationText(text:)")));

/** Edits the locations of the participant component in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)locationsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [participantType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)participantTypeValue:(CalendrumTzdataCalendrum_coreParticipantType * _Nullable)value __attribute__((swift_name("participantType(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)priorityValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `REQUEST-STATUS` property. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)requestStatusValue:(CalendrumTzdataCalendrum_coreRequestStatus *)value __attribute__((swift_name("requestStatus(value:)")));

/** Adds [value] as a resource of the participant component. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)resourceComponentValue:(CalendrumTzdataCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)resourceComponentBlock:(void (^)(CalendrumTzdataCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the participant component in [block]: add, replace or remove them. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)resourceComponentsBlock:(void (^)(CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)resourcesValues:(CalendrumTzdataKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)sequenceValue:(CalendrumTzdataInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)stampValue:(id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)statusValue:(CalendrumTzdataCalendrum_coreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)structuredDataValue:(CalendrumTzdataCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)styledDescriptionValue:(CalendrumTzdataCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

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
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the participant component being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component being built (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)); assigning `null` removes the property.
 */
@property CalendrumTzdataCalendrum_coreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable priority __attribute__((swift_name("priority")));

/** The resources of the participant component being built, as an editable list. */
@property (readonly) CalendrumTzdataCalendrum_coreComponentList<CalendrumTzdataCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The revision sequence number of the participant component being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumTzdataCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumTzdataCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

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
 * Collects the properties and nested components of a new or edited resource component, with a Kotlin DSL and
 * chained calls. Get one from [VResource.builder] or [VResource.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVResource.Builder")))
@interface CalendrumTzdataCalendrum_coreVResourceBuilder : CalendrumTzdataCalendrum_coreComponentBuilder<CalendrumTzdataCalendrum_coreVResourceBuilder *>

/**
 * Creates the resource component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumTzdataCalendrum_coreVResource *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)geoValue:(CalendrumTzdataCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)linkValue:(CalendrumTzdataCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)relatedToRelatedTo:(CalendrumTzdataCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [resourceType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)resourceTypeValue:(CalendrumTzdataCalendrum_coreResourceType * _Nullable)value __attribute__((swift_name("resourceType(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)structuredDataValue:(CalendrumTzdataCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

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
@property CalendrumTzdataCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The name of the resource component being built (`NAME`,
 * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)); assigning `null` removes the property.
 */
@property NSString * _Nullable name __attribute__((swift_name("name")));

/**
 * The resource type of the resource component being built (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)); assigning `null` removes the property.
 */
@property CalendrumTzdataCalendrum_coreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

/**
 * The globally unique identifier of the resource component being built (`UID`,
 * [RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable uid __attribute__((swift_name("uid")));
@end


/** Creates VAlarm values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVAlarm.Companion")))
@interface CalendrumTzdataCalendrum_coreVAlarmCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VAlarm values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVAlarmCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVAlarmBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VAlarm, or returns `null` when it is not a `VALARM` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVAlarm * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumTzdataCalendrum_coreAlarmAction : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreAlarmActionCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreProximity : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreProximityCompanion *companion __attribute__((swift_name("companion")));

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
@protocol CalendrumTzdataCalendrum_coreTrigger
@required

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumTzdataCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/** Creates builders for [Attendee]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAttendee.Companion")))
@interface CalendrumTzdataCalendrum_coreAttendeeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Attendee]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreAttendeeCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an attendee with the given calendar [address]. */
- (CalendrumTzdataCalendrum_coreAttendeeBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
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
@interface CalendrumTzdataCalendrum_coreCuType : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreCuTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_corePartStat : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_corePartStatCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreRole : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreRoleCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreClassificationCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Classification and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreClassificationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Classification holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreClassification *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CONFIDENTIAL`. The details are shown only to a limited set of people. */
@property (readonly) CalendrumTzdataCalendrum_coreClassification *Confidential __attribute__((swift_name("Confidential")));

/** `PRIVATE`. Only the owner may see the details. */
@property (readonly) CalendrumTzdataCalendrum_coreClassification *Private __attribute__((swift_name("Private")));

/** `PUBLIC`. Anyone may see the details; the default. */
@property (readonly) CalendrumTzdataCalendrum_coreClassification *Public __attribute__((swift_name("Public")));
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
@interface CalendrumTzdataCalendrum_coreConferenceFeature : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreConferenceFeatureCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumTzdataCalendrum_coreVLocationCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VLocation values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVLocationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVLocationBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VLocation, or returns `null` when it is not a `VLOCATION` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVLocation * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/** Creates builders for [Organizer]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreOrganizer.Companion")))
@interface CalendrumTzdataCalendrum_coreOrganizerCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Organizer]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreOrganizerCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an organizer with the given calendar [address]. */
- (CalendrumTzdataCalendrum_coreOrganizerBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
@end


/** Creates Participant values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParticipant.Companion")))
@interface CalendrumTzdataCalendrum_coreParticipantCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates Participant values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreParticipantCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. A `UID` and a participant type are required. */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreParticipantBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a Participant, or returns `null` when it is not a `PARTICIPANT` component. Nothing is
 * copied or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreParticipant * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumTzdataCalendrum_coreParticipantType : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreParticipantTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ParticipantType with the same token. */
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
@interface CalendrumTzdataCalendrum_coreVResourceCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VResource values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreVResourceCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumTzdataCalendrum_coreVResourceBuilder *)builderDefaults:(CalendrumTzdataCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VResource, or returns `null` when it is not a `VRESOURCE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumTzdataCalendrum_coreVResource * _Nullable)ofComponent:(CalendrumTzdataCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumTzdataCalendrum_coreResourceType : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreResourceTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ResourceType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** The constants of EventStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreEventStatus.Companion")))
@interface CalendrumTzdataCalendrum_coreEventStatusCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of EventStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreEventStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new EventStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreEventStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The event is cancelled. */
@property (readonly) CalendrumTzdataCalendrum_coreEventStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `CONFIRMED`. The event is confirmed. */
@property (readonly) CalendrumTzdataCalendrum_coreEventStatus *Confirmed __attribute__((swift_name("Confirmed")));

/** `TENTATIVE`. The event is tentative. */
@property (readonly) CalendrumTzdataCalendrum_coreEventStatus *Tentative __attribute__((swift_name("Tentative")));
@end


/** The constants of Transparency and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTransparency.Companion")))
@interface CalendrumTzdataCalendrum_coreTransparencyCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Transparency and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreTransparencyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Transparency holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreTransparency *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `OPAQUE`. The event blocks time; the default. */
@property (readonly) CalendrumTzdataCalendrum_coreTransparency *Opaque __attribute__((swift_name("Opaque")));

/** `TRANSPARENT`. The event does not block time. */
@property (readonly) CalendrumTzdataCalendrum_coreTransparency *Transparent __attribute__((swift_name("Transparent")));
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
@interface CalendrumTzdataCalendrum_coreFbType : CalendrumTzdataBase
@property (class, readonly, getter=companion) CalendrumTzdataCalendrum_coreFbTypeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a FbType with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end

__attribute__((swift_name("KotlinByteIterator")))
@interface CalendrumTzdataKotlinByteIterator : CalendrumTzdataBase <CalendrumTzdataKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumTzdataByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end


/** The constants of ImageDisplay and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreImageDisplay.Companion")))
@interface CalendrumTzdataCalendrum_coreImageDisplayCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ImageDisplay and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreImageDisplayCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ImageDisplay holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreImageDisplay *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BADGE`. A small image shown next to the title. */
@property (readonly) CalendrumTzdataCalendrum_coreImageDisplay *Badge __attribute__((swift_name("Badge")));

/** `FULLSIZE`. A full-size version of an image. */
@property (readonly) CalendrumTzdataCalendrum_coreImageDisplay *Fullsize __attribute__((swift_name("Fullsize")));

/** `GRAPHIC`. An image shown as a graphic with the content. */
@property (readonly) CalendrumTzdataCalendrum_coreImageDisplay *Graphic __attribute__((swift_name("Graphic")));

/** `THUMBNAIL`. A reduced-size version of an image. */
@property (readonly) CalendrumTzdataCalendrum_coreImageDisplay *Thumbnail __attribute__((swift_name("Thumbnail")));
@end


/** The constants of JournalStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreJournalStatus.Companion")))
@interface CalendrumTzdataCalendrum_coreJournalStatusCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of JournalStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreJournalStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new JournalStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreJournalStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The entry is cancelled. */
@property (readonly) CalendrumTzdataCalendrum_coreJournalStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `DRAFT`. The entry is a draft. */
@property (readonly) CalendrumTzdataCalendrum_coreJournalStatus *Draft __attribute__((swift_name("Draft")));

/** `FINAL`. The entry is final. */
@property (readonly) CalendrumTzdataCalendrum_coreJournalStatus *Final __attribute__((swift_name("Final")));
@end


/** The constants of TodoStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTodoStatus.Companion")))
@interface CalendrumTzdataCalendrum_coreTodoStatusCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of TodoStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreTodoStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new TodoStatus holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreTodoStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The to-do is cancelled. */
@property (readonly) CalendrumTzdataCalendrum_coreTodoStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `COMPLETED`. The to-do is completed. */
@property (readonly) CalendrumTzdataCalendrum_coreTodoStatus *Completed __attribute__((swift_name("Completed")));

/** `IN-PROCESS`. The to-do is in progress. */
@property (readonly) CalendrumTzdataCalendrum_coreTodoStatus *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The to-do needs action. */
@property (readonly) CalendrumTzdataCalendrum_coreTodoStatus *NeedsAction __attribute__((swift_name("NeedsAction")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol CalendrumTzdataKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<CalendrumTzdataKotlinKClass>)kClass provider:(id<CalendrumTzdataKotlinx_serialization_coreKSerializer> (^)(NSArray<id<CalendrumTzdataKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<CalendrumTzdataKotlinKClass>)kClass serializer:(id<CalendrumTzdataKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<CalendrumTzdataKotlinKClass>)baseClass actualClass:(id<CalendrumTzdataKotlinKClass>)actualClass actualSerializer:(id<CalendrumTzdataKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<CalendrumTzdataKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultDeserializerProvider:)"))) __attribute__((deprecated("Deprecated in favor of function with more precise name: polymorphicDefaultDeserializer")));
- (void)polymorphicDefaultDeserializerBaseClass:(id<CalendrumTzdataKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumTzdataKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefaultDeserializer(baseClass:defaultDeserializerProvider:)")));
- (void)polymorphicDefaultSerializerBaseClass:(id<CalendrumTzdataKotlinKClass>)baseClass defaultSerializerProvider:(id<CalendrumTzdataKotlinx_serialization_coreSerializationStrategy> _Nullable (^)(id))defaultSerializerProvider __attribute__((swift_name("polymorphicDefaultSerializer(baseClass:defaultSerializerProvider:)")));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol CalendrumTzdataKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol CalendrumTzdataKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol CalendrumTzdataKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol CalendrumTzdataKotlinKClass <CalendrumTzdataKotlinKDeclarationContainer, CalendrumTzdataKotlinKAnnotatedElement, CalendrumTzdataKotlinKClassifier>
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end


/** The constants of AlarmAction and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAlarmAction.Companion")))
@interface CalendrumTzdataCalendrum_coreAlarmActionCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of AlarmAction and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreAlarmActionCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new AlarmAction holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreAlarmAction *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Plays a sound. */
@property (readonly) CalendrumTzdataCalendrum_coreAlarmAction *Audio __attribute__((swift_name("Audio")));

/** `DISPLAY`. Shows a message. */
@property (readonly) CalendrumTzdataCalendrum_coreAlarmAction *Display __attribute__((swift_name("Display")));

/** `EMAIL`. Sends an e-mail. */
@property (readonly) CalendrumTzdataCalendrum_coreAlarmAction *Email __attribute__((swift_name("Email")));
@end


/** The constants of Proximity and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProximity.Companion")))
@interface CalendrumTzdataCalendrum_coreProximityCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Proximity and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreProximityCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Proximity holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreProximity *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ARRIVE`. Fires when the user arrives at the location. */
@property (readonly) CalendrumTzdataCalendrum_coreProximity *Arrive __attribute__((swift_name("Arrive")));

/** `CONNECT`. Fires when the user's device connects, for example to a car. */
@property (readonly) CalendrumTzdataCalendrum_coreProximity *Connect __attribute__((swift_name("Connect")));

/** `DEPART`. Fires when the user leaves the location. */
@property (readonly) CalendrumTzdataCalendrum_coreProximity *Depart __attribute__((swift_name("Depart")));

/** `DISCONNECT`. Fires when the user's device disconnects. */
@property (readonly) CalendrumTzdataCalendrum_coreProximity *Disconnect __attribute__((swift_name("Disconnect")));
@end


/** The constants of CuType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreCuType.Companion")))
@interface CalendrumTzdataCalendrum_coreCuTypeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of CuType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreCuTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new CuType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreCuType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `GROUP`. A group of people. */
@property (readonly) CalendrumTzdataCalendrum_coreCuType *Group __attribute__((swift_name("Group")));

/** `INDIVIDUAL`. A person; the default. */
@property (readonly) CalendrumTzdataCalendrum_coreCuType *Individual __attribute__((swift_name("Individual")));

/** `RESOURCE`. A resource other than a room, such as a projector. */
@property (readonly) CalendrumTzdataCalendrum_coreCuType *Resource __attribute__((swift_name("Resource")));

/** `ROOM`. A room. */
@property (readonly) CalendrumTzdataCalendrum_coreCuType *Room __attribute__((swift_name("Room")));

/** `UNKNOWN`. The type is not known. */
@property (readonly) CalendrumTzdataCalendrum_coreCuType *Unknown __attribute__((swift_name("Unknown")));
@end


/** The constants of PartStat and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_corePartStat.Companion")))
@interface CalendrumTzdataCalendrum_corePartStatCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of PartStat and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_corePartStatCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new PartStat holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_corePartStat *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACCEPTED`. The user will attend. */
@property (readonly) CalendrumTzdataCalendrum_corePartStat *Accepted __attribute__((swift_name("Accepted")));

/** `COMPLETED`. The user completed the task (to-dos). */
@property (readonly) CalendrumTzdataCalendrum_corePartStat *Completed __attribute__((swift_name("Completed")));

/** `DECLINED`. The user will not attend. */
@property (readonly) CalendrumTzdataCalendrum_corePartStat *Declined __attribute__((swift_name("Declined")));

/** `DELEGATED`. The user delegated attendance to someone else. */
@property (readonly) CalendrumTzdataCalendrum_corePartStat *Delegated __attribute__((swift_name("Delegated")));

/** `IN-PROCESS`. The user is working on the task (to-dos). */
@property (readonly) CalendrumTzdataCalendrum_corePartStat *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The user has not answered yet; the default. */
@property (readonly) CalendrumTzdataCalendrum_corePartStat *NeedsAction __attribute__((swift_name("NeedsAction")));

/** `TENTATIVE`. The user may attend. */
@property (readonly) CalendrumTzdataCalendrum_corePartStat *Tentative __attribute__((swift_name("Tentative")));
@end


/** The constants of Role and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRole.Companion")))
@interface CalendrumTzdataCalendrum_coreRoleCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Role and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreRoleCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Role holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreRole *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHAIR`. The user chairs the event. */
@property (readonly) CalendrumTzdataCalendrum_coreRole *Chair __attribute__((swift_name("Chair")));

/** `NON-PARTICIPANT`. Informed only; does not take part. */
@property (readonly) CalendrumTzdataCalendrum_coreRole *NonParticipant __attribute__((swift_name("NonParticipant")));

/** `OPT-PARTICIPANT`. An optional participant. */
@property (readonly) CalendrumTzdataCalendrum_coreRole *OptParticipant __attribute__((swift_name("OptParticipant")));

/** `REQ-PARTICIPANT`. A required participant; the default. */
@property (readonly) CalendrumTzdataCalendrum_coreRole *ReqParticipant __attribute__((swift_name("ReqParticipant")));
@end


/** The constants of ConferenceFeature and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreConferenceFeature.Companion")))
@interface CalendrumTzdataCalendrum_coreConferenceFeatureCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ConferenceFeature and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreConferenceFeatureCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ConferenceFeature holding the upper-cased token
 * when it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreConferenceFeature *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Audio conferencing. */
@property (readonly) CalendrumTzdataCalendrum_coreConferenceFeature *Audio __attribute__((swift_name("Audio")));

/** `CHAT`. Chat or instant messaging. */
@property (readonly) CalendrumTzdataCalendrum_coreConferenceFeature *Chat __attribute__((swift_name("Chat")));

/** `FEED`. A blog or atom feed. */
@property (readonly) CalendrumTzdataCalendrum_coreConferenceFeature *Feed __attribute__((swift_name("Feed")));

/** `MODERATOR`. Moderator dial-in. */
@property (readonly) CalendrumTzdataCalendrum_coreConferenceFeature *Moderator __attribute__((swift_name("Moderator")));

/** `PHONE`. Phone conferencing. */
@property (readonly) CalendrumTzdataCalendrum_coreConferenceFeature *Phone __attribute__((swift_name("Phone")));

/** `SCREEN`. Screen sharing. */
@property (readonly) CalendrumTzdataCalendrum_coreConferenceFeature *Screen __attribute__((swift_name("Screen")));

/** `VIDEO`. Video conferencing. */
@property (readonly) CalendrumTzdataCalendrum_coreConferenceFeature *Video __attribute__((swift_name("Video")));
@end


/** The constants of ParticipantType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParticipantType.Companion")))
@interface CalendrumTzdataCalendrum_coreParticipantTypeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ParticipantType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreParticipantTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ParticipantType holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreParticipantType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACTIVE`. An active participant. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *Active __attribute__((swift_name("Active")));

/** `BOOKING-CONTACT`. A contact for bookings or reservations. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *BookingContact __attribute__((swift_name("BookingContact")));

/** `CONTACT`. A contact for information. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *Contact __attribute__((swift_name("Contact")));

/** `EMERGENCY-CONTACT`. A contact in an emergency. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *EmergencyContact __attribute__((swift_name("EmergencyContact")));

/** `INACTIVE`. An inactive participant. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *Inactive __attribute__((swift_name("Inactive")));

/** `PERFORMER`. A performer, such as a musician. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *Performer __attribute__((swift_name("Performer")));

/** `PLANNER-CONTACT`. A contact for the planning of the activity. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *PlannerContact __attribute__((swift_name("PlannerContact")));

/** `PUBLICITY-CONTACT`. A contact for publicity. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *PublicityContact __attribute__((swift_name("PublicityContact")));

/** `SPEAKER`. A speaker. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *Speaker __attribute__((swift_name("Speaker")));

/** `SPONSOR`. A sponsor of the activity. */
@property (readonly) CalendrumTzdataCalendrum_coreParticipantType *Sponsor __attribute__((swift_name("Sponsor")));
@end


/** The constants of ResourceType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreResourceType.Companion")))
@interface CalendrumTzdataCalendrum_coreResourceTypeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ResourceType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreResourceTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ResourceType holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreResourceType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `PROJECTOR`. A projector. */
@property (readonly) CalendrumTzdataCalendrum_coreResourceType *Projector __attribute__((swift_name("Projector")));

/** `REMOTE-CONFERENCE-AUDIO`. A remote conference audio channel. */
@property (readonly) CalendrumTzdataCalendrum_coreResourceType *RemoteConferenceAudio __attribute__((swift_name("RemoteConferenceAudio")));

/** `REMOTE-CONFERENCE-VIDEO`. A remote conference video channel. */
@property (readonly) CalendrumTzdataCalendrum_coreResourceType *RemoteConferenceVideo __attribute__((swift_name("RemoteConferenceVideo")));

/** `ROOM`. A room. */
@property (readonly) CalendrumTzdataCalendrum_coreResourceType *Room __attribute__((swift_name("Room")));
@end


/** The constants of FbType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFbType.Companion")))
@interface CalendrumTzdataCalendrum_coreFbTypeCompanion : CalendrumTzdataBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of FbType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumTzdataCalendrum_coreFbTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new FbType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumTzdataCalendrum_coreFbType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BUSY`. The time is busy; the default. */
@property (readonly) CalendrumTzdataCalendrum_coreFbType *Busy __attribute__((swift_name("Busy")));

/** `BUSY-TENTATIVE`. The time is tentatively busy. */
@property (readonly) CalendrumTzdataCalendrum_coreFbType *BusyTentative __attribute__((swift_name("BusyTentative")));

/** `BUSY-UNAVAILABLE`. The time is busy and unavailable. */
@property (readonly) CalendrumTzdataCalendrum_coreFbType *BusyUnavailable __attribute__((swift_name("BusyUnavailable")));

/** `FREE`. The time is free. */
@property (readonly) CalendrumTzdataCalendrum_coreFbType *Free __attribute__((swift_name("Free")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END

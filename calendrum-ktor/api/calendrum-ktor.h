#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class CalendrumKtorCalendrum_coreAlarmAction, CalendrumKtorCalendrum_coreAlarmActionCompanion, CalendrumKtorCalendrum_coreAttendee, CalendrumKtorCalendrum_coreAttendeeBuilder, CalendrumKtorCalendrum_coreAttendeeCompanion, CalendrumKtorCalendrum_coreAttendeeList, CalendrumKtorCalendrum_coreBuildDefaults, CalendrumKtorCalendrum_coreCardinality, CalendrumKtorCalendrum_coreClassification, CalendrumKtorCalendrum_coreClassificationCompanion, CalendrumKtorCalendrum_coreComponentBuilder<B>, CalendrumKtorCalendrum_coreComponentDefinition, CalendrumKtorCalendrum_coreComponentList<T>, CalendrumKtorCalendrum_coreConference, CalendrumKtorCalendrum_coreConferenceFeature, CalendrumKtorCalendrum_coreConferenceFeatureCompanion, CalendrumKtorCalendrum_coreCuType, CalendrumKtorCalendrum_coreCuTypeCompanion, CalendrumKtorCalendrum_coreDecodeContext, CalendrumKtorCalendrum_coreDecodeContextCompanion, CalendrumKtorCalendrum_coreEncodeContext, CalendrumKtorCalendrum_coreEventStatus, CalendrumKtorCalendrum_coreEventStatusCompanion, CalendrumKtorCalendrum_coreFbType, CalendrumKtorCalendrum_coreFbTypeCompanion, CalendrumKtorCalendrum_coreFeature, CalendrumKtorCalendrum_coreFeatureCompanion, CalendrumKtorCalendrum_coreFreeBusy, CalendrumKtorCalendrum_coreFrequency, CalendrumKtorCalendrum_coreGeo, CalendrumKtorCalendrum_coreICalComponent, CalendrumKtorCalendrum_coreICalComponentBuilder, CalendrumKtorCalendrum_coreICalComponentCompanion, CalendrumKtorCalendrum_coreICalDuration, CalendrumKtorCalendrum_coreICalDurationCompanion, CalendrumKtorCalendrum_coreICalParameter, CalendrumKtorCalendrum_coreICalParameterCompanion, CalendrumKtorCalendrum_coreICalPeriod, CalendrumKtorCalendrum_coreICalProperty, CalendrumKtorCalendrum_coreICalPropertyBuilder, CalendrumKtorCalendrum_coreICalPropertyCompanion, CalendrumKtorCalendrum_coreICalRegistry, CalendrumKtorCalendrum_coreICalRegistryBuilder, CalendrumKtorCalendrum_coreICalRegistryCompanion, CalendrumKtorCalendrum_coreICalWriter, CalendrumKtorCalendrum_coreIcsSchema<T>, CalendrumKtorCalendrum_coreIcsSchemaField<R>, CalendrumKtorCalendrum_coreIcsSchemaValues, CalendrumKtorCalendrum_coreImage, CalendrumKtorCalendrum_coreImageDisplay, CalendrumKtorCalendrum_coreImageDisplayCompanion, CalendrumKtorCalendrum_coreInterceptedLine, CalendrumKtorCalendrum_coreJournalStatus, CalendrumKtorCalendrum_coreJournalStatusCompanion, CalendrumKtorCalendrum_coreLink, CalendrumKtorCalendrum_coreMethod, CalendrumKtorCalendrum_coreMethodCompanion, CalendrumKtorCalendrum_coreMonthNum, CalendrumKtorCalendrum_coreOrganizer, CalendrumKtorCalendrum_coreOrganizerBuilder, CalendrumKtorCalendrum_coreOrganizerCompanion, CalendrumKtorCalendrum_coreParameterDefinition, CalendrumKtorCalendrum_coreParameterKey<T>, CalendrumKtorCalendrum_coreParseLimits, CalendrumKtorCalendrum_coreParseLimitsBuilder, CalendrumKtorCalendrum_coreParseLimitsCompanion, CalendrumKtorCalendrum_coreParseMode, CalendrumKtorCalendrum_coreParseOptions, CalendrumKtorCalendrum_coreParseOptionsBuilder, CalendrumKtorCalendrum_coreParseOptionsCompanion, CalendrumKtorCalendrum_corePartStat, CalendrumKtorCalendrum_corePartStatCompanion, CalendrumKtorCalendrum_coreParticipant, CalendrumKtorCalendrum_coreParticipantBuilder, CalendrumKtorCalendrum_coreParticipantCompanion, CalendrumKtorCalendrum_coreParticipantType, CalendrumKtorCalendrum_coreParticipantTypeCompanion, CalendrumKtorCalendrum_coreProblem, CalendrumKtorCalendrum_coreProblemCode, CalendrumKtorCalendrum_coreProblemCodeCompanion, CalendrumKtorCalendrum_corePropertyDefinition, CalendrumKtorCalendrum_corePropertyKey<T>, CalendrumKtorCalendrum_coreProximity, CalendrumKtorCalendrum_coreProximityCompanion, CalendrumKtorCalendrum_coreRecur, CalendrumKtorCalendrum_coreRecurBuilder, CalendrumKtorCalendrum_coreRecurCompanion, CalendrumKtorCalendrum_coreRecurPart, CalendrumKtorCalendrum_coreRelType, CalendrumKtorCalendrum_coreRelTypeCompanion, CalendrumKtorCalendrum_coreRelatedTo, CalendrumKtorCalendrum_coreRequestStatus, CalendrumKtorCalendrum_coreResourceType, CalendrumKtorCalendrum_coreResourceTypeCompanion, CalendrumKtorCalendrum_coreRole, CalendrumKtorCalendrum_coreRoleCompanion, CalendrumKtorCalendrum_coreSelection, CalendrumKtorCalendrum_coreSelectionBuilder, CalendrumKtorCalendrum_coreSelectionCompanion, CalendrumKtorCalendrum_coreSeverity, CalendrumKtorCalendrum_coreSkip, CalendrumKtorCalendrum_coreSourceSpan, CalendrumKtorCalendrum_coreStructuredData, CalendrumKtorCalendrum_coreStyledDescription, CalendrumKtorCalendrum_coreTimeZoneSelection, CalendrumKtorCalendrum_coreTodoStatus, CalendrumKtorCalendrum_coreTodoStatusCompanion, CalendrumKtorCalendrum_coreTransparency, CalendrumKtorCalendrum_coreTransparencyCompanion, CalendrumKtorCalendrum_coreVAlarm, CalendrumKtorCalendrum_coreVAlarmBuilder, CalendrumKtorCalendrum_coreVAlarmCompanion, CalendrumKtorCalendrum_coreVCalendar, CalendrumKtorCalendrum_coreVCalendarBuilder, CalendrumKtorCalendrum_coreVCalendarCompanion, CalendrumKtorCalendrum_coreVEvent, CalendrumKtorCalendrum_coreVEventBuilder, CalendrumKtorCalendrum_coreVEventCompanion, CalendrumKtorCalendrum_coreVFreeBusy, CalendrumKtorCalendrum_coreVFreeBusyBuilder, CalendrumKtorCalendrum_coreVFreeBusyCompanion, CalendrumKtorCalendrum_coreVJournal, CalendrumKtorCalendrum_coreVJournalBuilder, CalendrumKtorCalendrum_coreVJournalCompanion, CalendrumKtorCalendrum_coreVLocation, CalendrumKtorCalendrum_coreVLocationBuilder, CalendrumKtorCalendrum_coreVLocationCompanion, CalendrumKtorCalendrum_coreVResource, CalendrumKtorCalendrum_coreVResourceBuilder, CalendrumKtorCalendrum_coreVResourceCompanion, CalendrumKtorCalendrum_coreVTimeZone, CalendrumKtorCalendrum_coreVTimeZoneBuilder, CalendrumKtorCalendrum_coreVTimeZoneCompanion, CalendrumKtorCalendrum_coreVTimeZoneObservance, CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder, CalendrumKtorCalendrum_coreVTodo, CalendrumKtorCalendrum_coreVTodoBuilder, CalendrumKtorCalendrum_coreVTodoCompanion, CalendrumKtorCalendrum_coreValueType, CalendrumKtorCalendrum_coreValueTypeCompanion, CalendrumKtorCalendrum_coreWeekdayNum, CalendrumKtorICalContentConverter, CalendrumKtorIcsFeedOptions, CalendrumKtorIcsFeedOptionsBuilder, CalendrumKtorIcsFeedOptionsCompanion, CalendrumKtorKotlinArray<T>, CalendrumKtorKotlinByteArray, CalendrumKtorKotlinByteIterator, CalendrumKtorKotlinEnum<E>, CalendrumKtorKotlinEnumCompanion, CalendrumKtorKotlinException, CalendrumKtorKotlinIllegalStateException, CalendrumKtorKotlinInstant, CalendrumKtorKotlinInstantCompanion, CalendrumKtorKotlinIntArray, CalendrumKtorKotlinIntIterator, CalendrumKtorKotlinKTypeProjection, CalendrumKtorKotlinKTypeProjectionCompanion, CalendrumKtorKotlinKVariance, CalendrumKtorKotlinNothing, CalendrumKtorKotlinRuntimeException, CalendrumKtorKotlinThrowable, CalendrumKtorKotlinUnit, CalendrumKtorKotlinx_datetimeDateTimePeriod, CalendrumKtorKotlinx_datetimeDateTimePeriodCompanion, CalendrumKtorKotlinx_datetimeDayOfWeek, CalendrumKtorKotlinx_datetimePadding, CalendrumKtorKotlinx_datetimeUtcOffset, CalendrumKtorKotlinx_datetimeUtcOffsetCompanion, CalendrumKtorKotlinx_io_coreBuffer, CalendrumKtorKotlinx_serialization_coreSerialKind, CalendrumKtorKotlinx_serialization_coreSerializersModule, CalendrumKtorKtor_eventsEventDefinition<T>, CalendrumKtorKtor_eventsEvents, CalendrumKtorKtor_httpContentType, CalendrumKtorKtor_httpContentTypeCompanion, CalendrumKtorKtor_httpCookie, CalendrumKtorKtor_httpCookieCompanion, CalendrumKtorKtor_httpCookieEncoding, CalendrumKtorKtor_httpHeaderValueParam, CalendrumKtorKtor_httpHeaderValueWithParameters, CalendrumKtorKtor_httpHeaderValueWithParametersCompanion, CalendrumKtorKtor_httpHeadersBuilder, CalendrumKtorKtor_httpHttpMethod, CalendrumKtorKtor_httpHttpMethodCompanion, CalendrumKtorKtor_httpHttpStatusCode, CalendrumKtorKtor_httpHttpStatusCodeCompanion, CalendrumKtorKtor_httpOutgoingContent, CalendrumKtorKtor_httpURLBuilder, CalendrumKtorKtor_httpURLBuilderCompanion, CalendrumKtorKtor_httpURLProtocol, CalendrumKtorKtor_httpURLProtocolCompanion, CalendrumKtorKtor_httpUrl, CalendrumKtorKtor_httpUrlCompanion, CalendrumKtorKtor_httpVersionCheckResult, CalendrumKtorKtor_ioCharset, CalendrumKtorKtor_ioCharsetCompanion, CalendrumKtorKtor_ioCharsetDecoder, CalendrumKtorKtor_ioCharsetEncoder, CalendrumKtorKtor_server_coreApplication, CalendrumKtorKtor_server_coreApplicationCallPipeline, CalendrumKtorKtor_server_coreApplicationCallPipelineApplicationPhase, CalendrumKtorKtor_server_coreApplicationReceivePipeline, CalendrumKtorKtor_server_coreApplicationReceivePipelinePhases, CalendrumKtorKtor_server_coreApplicationSendPipeline, CalendrumKtorKtor_server_coreApplicationSendPipelinePhases, CalendrumKtorKtor_server_coreConnectorType, CalendrumKtorKtor_server_coreConnectorTypeCompanion, CalendrumKtorKtor_server_coreRequestCookies, CalendrumKtorKtor_server_coreResponseCookies, CalendrumKtorKtor_server_coreResponseHeaders, CalendrumKtorKtor_server_coreRouteSelector, CalendrumKtorKtor_server_coreRouteSelectorEvaluation, CalendrumKtorKtor_server_coreRouteSelectorEvaluationCompanion, CalendrumKtorKtor_server_coreRouteSelectorEvaluationFailure, CalendrumKtorKtor_server_coreRoutingNode, CalendrumKtorKtor_server_coreRoutingResolveContext, CalendrumKtorKtor_server_coreRoutingResolveResult, CalendrumKtorKtor_server_coreRoutingResolveResultSuccess, CalendrumKtorKtor_server_coreRoutingResolveTrace, CalendrumKtorKtor_utilsAttributeKey<T>, CalendrumKtorKtor_utilsGMTDate, CalendrumKtorKtor_utilsGMTDateCompanion, CalendrumKtorKtor_utilsLogLevel, CalendrumKtorKtor_utilsMonth, CalendrumKtorKtor_utilsMonthCompanion, CalendrumKtorKtor_utilsPipeline<TSubject, TContext>, CalendrumKtorKtor_utilsPipelinePhase, CalendrumKtorKtor_utilsStringValuesBuilderImpl, CalendrumKtorKtor_utilsTypeInfo, CalendrumKtorKtor_utilsWeekDay, CalendrumKtorKtor_utilsWeekDayCompanion;

@protocol CalendrumKtorCalendrum_coreAttach, CalendrumKtorCalendrum_coreByteOutput, CalendrumKtorCalendrum_coreDecoded, CalendrumKtorCalendrum_coreICalDateTime, CalendrumKtorCalendrum_coreICalExtension, CalendrumKtorCalendrum_coreLineInterceptor, CalendrumKtorCalendrum_coreParameterCodec, CalendrumKtorCalendrum_coreProblemListener, CalendrumKtorCalendrum_corePropertyCodec, CalendrumKtorCalendrum_coreTrigger, CalendrumKtorCalendrum_coreUidGenerator, CalendrumKtorKotlinAnnotation, CalendrumKtorKotlinAppendable, CalendrumKtorKotlinAutoCloseable, CalendrumKtorKotlinClock, CalendrumKtorKotlinComparable, CalendrumKtorKotlinCoroutineContext, CalendrumKtorKotlinCoroutineContextElement, CalendrumKtorKotlinCoroutineContextKey, CalendrumKtorKotlinFunction, CalendrumKtorKotlinIterator, CalendrumKtorKotlinKAnnotatedElement, CalendrumKtorKotlinKClass, CalendrumKtorKotlinKClassifier, CalendrumKtorKotlinKDeclarationContainer, CalendrumKtorKotlinKType, CalendrumKtorKotlinMapEntry, CalendrumKtorKotlinSuspendFunction1, CalendrumKtorKotlinSuspendFunction2, CalendrumKtorKotlinTimeMark, CalendrumKtorKotlinTimeSource, CalendrumKtorKotlinTimeSourceWithComparableMarks, CalendrumKtorKotlinx_coroutines_coreCoroutineScope, CalendrumKtorKotlinx_coroutines_coreDisposableHandle, CalendrumKtorKotlinx_datetimeDateTimeFormat, CalendrumKtorKotlinx_datetimeDateTimeFormatBuilder, CalendrumKtorKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset, CalendrumKtorKotlinx_io_coreRawSink, CalendrumKtorKotlinx_io_coreRawSource, CalendrumKtorKotlinx_io_coreSink, CalendrumKtorKotlinx_io_coreSource, CalendrumKtorKotlinx_serialization_coreCompositeDecoder, CalendrumKtorKotlinx_serialization_coreCompositeEncoder, CalendrumKtorKotlinx_serialization_coreDecoder, CalendrumKtorKotlinx_serialization_coreDeserializationStrategy, CalendrumKtorKotlinx_serialization_coreEncoder, CalendrumKtorKotlinx_serialization_coreKSerializer, CalendrumKtorKotlinx_serialization_coreSerialDescriptor, CalendrumKtorKotlinx_serialization_coreSerializationStrategy, CalendrumKtorKotlinx_serialization_coreSerializersModuleCollector, CalendrumKtorKtor_httpHeaders, CalendrumKtorKtor_httpParameters, CalendrumKtorKtor_httpParametersBuilder, CalendrumKtorKtor_httpRequestConnectionPoint, CalendrumKtorKtor_httpVersion, CalendrumKtorKtor_ioByteReadChannel, CalendrumKtorKtor_serializationConfiguration, CalendrumKtorKtor_serializationContentConverter, CalendrumKtorKtor_server_coreApplicationCall, CalendrumKtorKtor_server_coreApplicationConfig, CalendrumKtorKtor_server_coreApplicationConfigValue, CalendrumKtorKtor_server_coreApplicationEngine, CalendrumKtorKtor_server_coreApplicationEnvironment, CalendrumKtorKtor_server_coreApplicationRequest, CalendrumKtorKtor_server_coreApplicationResponse, CalendrumKtorKtor_server_coreEngineConnectorConfig, CalendrumKtorKtor_server_corePipelineCall, CalendrumKtorKtor_server_corePipelineResponse, CalendrumKtorKtor_server_corePlugin, CalendrumKtorKtor_server_coreResponsePushBuilder, CalendrumKtorKtor_server_coreRoute, CalendrumKtorKtor_utilsAttributes, CalendrumKtorKtor_utilsLogger, CalendrumKtorKtor_utilsStringValues, CalendrumKtorKtor_utilsStringValuesBuilder;

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
@interface CalendrumKtorBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface CalendrumKtorBase (CalendrumKtorBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface CalendrumKtorMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface CalendrumKtorMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorCalendrumKtorKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface CalendrumKtorNumber : NSNumber
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
@interface CalendrumKtorByte : CalendrumKtorNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface CalendrumKtorUByte : CalendrumKtorNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface CalendrumKtorShort : CalendrumKtorNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface CalendrumKtorUShort : CalendrumKtorNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface CalendrumKtorInt : CalendrumKtorNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface CalendrumKtorUInt : CalendrumKtorNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface CalendrumKtorLong : CalendrumKtorNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface CalendrumKtorULong : CalendrumKtorNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface CalendrumKtorFloat : CalendrumKtorNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface CalendrumKtorDouble : CalendrumKtorNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface CalendrumKtorBoolean : CalendrumKtorNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end

__attribute__((swift_name("Ktor_serializationContentConverter")))
@protocol CalendrumKtorKtor_serializationContentConverter
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deserializeCharset:(CalendrumKtorKtor_ioCharset *)charset typeInfo:(CalendrumKtorKtor_utilsTypeInfo *)typeInfo content:(id<CalendrumKtorKtor_ioByteReadChannel>)content completionHandler:(void (^)(id _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("deserialize(charset:typeInfo:content:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)serializeContentType:(CalendrumKtorKtor_httpContentType *)contentType charset:(CalendrumKtorKtor_ioCharset *)charset typeInfo:(CalendrumKtorKtor_utilsTypeInfo *)typeInfo value:(id _Nullable)value completionHandler:(void (^)(CalendrumKtorKtor_httpOutgoingContent * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("serialize(contentType:charset:typeInfo:value:completionHandler:)")));
@end


/**
 * A Ktor content converter for `text/calendar`, for the server and the client `ContentNegotiation`.
 *
 * It writes a [VCalendar], an `ICalComponent`, a `ParseResult` or a list of them, and reads a [VCalendar], a list of
 * calendars, an `ICalComponent`, a list of components or a `ParseResult`, depending on the requested type. When
 * reading, the charset of the content type is used as the charset hint of the parser if the options set none. Register
 * it with `iCalendar()`.
 *
 * @property parseOptions the options of the parser that reads bodies.
 * @property writer the writer that produces bodies.
 * @sample io.github.calendrum.samples.serveAnIcsFeed
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalContentConverter")))
@interface CalendrumKtorICalContentConverter : CalendrumKtorBase <CalendrumKtorKtor_serializationContentConverter>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithParseOptions:(CalendrumKtorCalendrum_coreParseOptions *)parseOptions writer:(CalendrumKtorCalendrum_coreICalWriter *)writer __attribute__((swift_name("init(parseOptions:writer:)"))) __attribute__((objc_designated_initializer));

/**
 * Reads the body as the type [typeInfo] asks for, or returns `null` for a type this converter does not produce.
 *
 * At most `ParseLimits.maxInputOctets` of [parseOptions] plus one bytes of the body are read; a larger body gives a
 * truncated result with `ICAL-S030`, not an exception.
 *
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)deserializeCharset:(CalendrumKtorKtor_ioCharset *)charset typeInfo:(CalendrumKtorKtor_utilsTypeInfo *)typeInfo content:(id<CalendrumKtorKtor_ioByteReadChannel>)content completionHandler:(void (^)(id _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("deserialize(charset:typeInfo:content:completionHandler:)")));

/** Writes [value] as `text/calendar` with the charset UTF-8, or returns `null` when it is not a supported type.
 *
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)serializeContentType:(CalendrumKtorKtor_httpContentType *)contentType charset:(CalendrumKtorKtor_ioCharset *)charset typeInfo:(CalendrumKtorKtor_utilsTypeInfo *)typeInfo value:(id _Nullable)value completionHandler:(void (^)(CalendrumKtorKtor_httpOutgoingContent * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("serialize(contentType:charset:typeInfo:value:completionHandler:)")));

/** Returns the name of the class. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreParseOptions *parseOptions __attribute__((swift_name("parseOptions")));
@property (readonly) CalendrumKtorCalendrum_coreICalWriter *writer __attribute__((swift_name("writer")));
@end


/**
 * Settings of an ICS feed route: the `Cache-Control` header and the writer. Immutable.
 *
 * @property cacheControl the value of the `Cache-Control` header; `no-cache` by default, so clients revalidate with the
 *   `ETag`.
 * @property writer the writer that produces the body.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IcsFeedOptions")))
@interface CalendrumKtorIcsFeedOptions : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorIcsFeedOptionsCompanion *companion __attribute__((swift_name("companion")));

/** Returns the cache control. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *cacheControl __attribute__((swift_name("cacheControl")));
@property (readonly) CalendrumKtorCalendrum_coreICalWriter *writer __attribute__((swift_name("writer")));
@end


/** Collects [IcsFeedOptions] values. Not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IcsFeedOptions.Builder")))
@interface CalendrumKtorIcsFeedOptionsBuilder : CalendrumKtorBase

/** Creates the options. */
- (CalendrumKtorIcsFeedOptions *)build __attribute__((swift_name("build()")));

/** Sets [cacheControl] to [value] and returns this builder. */
- (CalendrumKtorIcsFeedOptionsBuilder *)cacheControlValue:(NSString *)value __attribute__((swift_name("cacheControl(value:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Sets [writer] to [value] and returns this builder. */
- (CalendrumKtorIcsFeedOptionsBuilder *)writerValue:(CalendrumKtorCalendrum_coreICalWriter *)value __attribute__((swift_name("writer(value:)")));

/** The value of the `Cache-Control` header; `no-cache` by default. */
@property NSString *cacheControl __attribute__((swift_name("cacheControl")));

/** The writer that produces the body; the default writer by default. */
@property CalendrumKtorCalendrum_coreICalWriter *writer __attribute__((swift_name("writer")));
@end


/** The default options and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IcsFeedOptions.Companion")))
@interface CalendrumKtorIcsFeedOptionsCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default options and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorIcsFeedOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the default options. */
- (CalendrumKtorIcsFeedOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** `Cache-Control: no-cache` and the default writer. */
@property (readonly) CalendrumKtorIcsFeedOptions *Default __attribute__((swift_name("Default")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalContentConverterKt")))
@interface CalendrumKtorICalContentConverterKt : CalendrumKtorBase

/**
 * Registers an [ICalContentConverter] with the default options for `text/calendar`.
 * @sample io.github.calendrum.samples.serveAnIcsFeed
 */
+ (void)iCalendar:(id<CalendrumKtorKtor_serializationConfiguration>)receiver __attribute__((swift_name("iCalendar(_:)")));

/** Registers [converter] for the content type [contentType]; `text/calendar` by default. */
+ (void)iCalendar:(id<CalendrumKtorKtor_serializationConfiguration>)receiver converter:(CalendrumKtorICalContentConverter *)converter contentType:(CalendrumKtorKtor_httpContentType *)contentType __attribute__((swift_name("iCalendar(_:converter:contentType:)")));

/** The `text/calendar` content type. */
@property (class, readonly) CalendrumKtorKtor_httpContentType *TextCalendar __attribute__((swift_name("TextCalendar")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IcsFeedKt")))
@interface CalendrumKtorIcsFeedKt : CalendrumKtorBase

/**
 * Serves a calendar as an ICS feed at [path] with default options: each `GET` runs [feed], sends the calendar as
 * `text/calendar` with an `ETag`, and answers `304 Not Modified` when `If-None-Match` matches.
 *
 * The `ETag` is a weak tag over the canonical form of the calendar, so a feed whose content did not change keeps its
 * tag.
 *
 * @param path the route path.
 * @param feed produces the calendar for one request.
 * @sample io.github.calendrum.samples.serveAnIcsFeed
 */
+ (id<CalendrumKtorKtor_server_coreRoute>)icsFeed:(id<CalendrumKtorKtor_server_coreRoute>)receiver path:(NSString *)path feed:(id<CalendrumKtorKotlinSuspendFunction1>)feed __attribute__((swift_name("icsFeed(_:path:feed:)")));

/**
 * Serves a calendar as an ICS feed at [path] with the given [options].
 *
 * @param path the route path.
 * @param options the cache header and the writer.
 * @param feed produces the calendar for one request.
 */
+ (id<CalendrumKtorKtor_server_coreRoute>)icsFeed:(id<CalendrumKtorKtor_server_coreRoute>)receiver path:(NSString *)path options:(CalendrumKtorIcsFeedOptions *)options feed:(id<CalendrumKtorKotlinSuspendFunction1>)feed __attribute__((swift_name("icsFeed(_:path:options:feed:)")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface CalendrumKtorKotlinThrowable : CalendrumKtorBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (CalendrumKtorKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface CalendrumKtorKotlinException : CalendrumKtorKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface CalendrumKtorKotlinRuntimeException : CalendrumKtorKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalStateException")))
@interface CalendrumKtorKotlinIllegalStateException : CalendrumKtorKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.4")
*/
__attribute__((swift_name("KotlinCancellationException")))
@interface CalendrumKtorKotlinCancellationException : CalendrumKtorKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("Ktor_ioCharset")))
@interface CalendrumKtorKtor_ioCharset : CalendrumKtorBase
- (instancetype)initWith_name:(NSString *)_name __attribute__((swift_name("init(_name:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_ioCharsetCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumKtorKtor_ioCharsetDecoder *)doNewDecoder __attribute__((swift_name("doNewDecoder()")));
- (CalendrumKtorKtor_ioCharsetEncoder *)doNewEncoder __attribute__((swift_name("doNewEncoder()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsTypeInfo")))
@interface CalendrumKtorKtor_utilsTypeInfo : CalendrumKtorBase
- (instancetype)initWithType:(id<CalendrumKtorKotlinKClass>)type kotlinType:(id<CalendrumKtorKotlinKType> _Nullable)kotlinType __attribute__((swift_name("init(type:kotlinType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithType:(id<CalendrumKtorKotlinKClass>)type reifiedType:(id<CalendrumKtorKotlinKType>)reifiedType kotlinType:(id<CalendrumKtorKotlinKType> _Nullable)kotlinType __attribute__((swift_name("init(type:reifiedType:kotlinType:)"))) __attribute__((objc_designated_initializer)) __attribute__((deprecated("Use constructor without reifiedType parameter.")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorKotlinKType> _Nullable kotlinType __attribute__((swift_name("kotlinType")));
@property (readonly) id<CalendrumKtorKotlinKClass> type __attribute__((swift_name("type")));
@end

__attribute__((swift_name("Ktor_ioByteReadChannel")))
@protocol CalendrumKtorKtor_ioByteReadChannel
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)awaitContentMin:(int32_t)min completionHandler:(void (^)(CalendrumKtorBoolean * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("awaitContent(min:completionHandler:)")));
- (void)cancelCause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("cancel(cause:)")));
@property (readonly) CalendrumKtorKotlinThrowable * _Nullable closedCause __attribute__((swift_name("closedCause")));
@property (readonly) BOOL isClosedForRead __attribute__((swift_name("isClosedForRead")));
@property (readonly) id<CalendrumKtorKotlinx_io_coreSource> readBuffer __attribute__((swift_name("readBuffer")));
@end

__attribute__((swift_name("Ktor_httpHeaderValueWithParameters")))
@interface CalendrumKtorKtor_httpHeaderValueWithParameters : CalendrumKtorBase
- (instancetype)initWithContent:(NSString *)content parameters:(NSArray<CalendrumKtorKtor_httpHeaderValueParam *> *)parameters __attribute__((swift_name("init(content:parameters:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpHeaderValueWithParametersCompanion *companion __attribute__((swift_name("companion")));
- (NSString * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * @note This property has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
@property (readonly) NSString *content __attribute__((swift_name("content")));
@property (readonly) NSArray<CalendrumKtorKtor_httpHeaderValueParam *> *parameters __attribute__((swift_name("parameters")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpContentType")))
@interface CalendrumKtorKtor_httpContentType : CalendrumKtorKtor_httpHeaderValueWithParameters
- (instancetype)initWithContentType:(NSString *)contentType contentSubtype:(NSString *)contentSubtype parameters:(NSArray<CalendrumKtorKtor_httpHeaderValueParam *> *)parameters __attribute__((swift_name("init(contentType:contentSubtype:parameters:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithContent:(NSString *)content parameters:(NSArray<CalendrumKtorKtor_httpHeaderValueParam *> *)parameters __attribute__((swift_name("init(content:parameters:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpContentTypeCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)matchPattern:(CalendrumKtorKtor_httpContentType *)pattern __attribute__((swift_name("match(pattern:)")));
- (BOOL)matchPattern_:(NSString *)pattern __attribute__((swift_name("match(pattern_:)")));
- (CalendrumKtorKtor_httpContentType *)withParameterName:(NSString *)name value:(NSString *)value __attribute__((swift_name("withParameter(name:value:)")));
- (CalendrumKtorKtor_httpContentType *)withoutParameters __attribute__((swift_name("withoutParameters()")));
@property (readonly) NSString *contentSubtype __attribute__((swift_name("contentSubtype")));
@property (readonly) NSString *contentType __attribute__((swift_name("contentType")));
@end

__attribute__((swift_name("Ktor_httpOutgoingContent")))
@interface CalendrumKtorKtor_httpOutgoingContent : CalendrumKtorBase
- (id _Nullable)getPropertyKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key __attribute__((swift_name("getProperty(key:)")));
- (void)setPropertyKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key value:(id _Nullable)value __attribute__((swift_name("setProperty(key:value:)")));
- (id<CalendrumKtorKtor_httpHeaders> _Nullable)trailers __attribute__((swift_name("trailers()")));
@property (readonly) CalendrumKtorLong * _Nullable contentLength __attribute__((swift_name("contentLength")));
@property (readonly) CalendrumKtorKtor_httpContentType * _Nullable contentType __attribute__((swift_name("contentType")));
@property (readonly) id<CalendrumKtorKtor_httpHeaders> headers __attribute__((swift_name("headers")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode * _Nullable status __attribute__((swift_name("status")));
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
@interface CalendrumKtorCalendrum_coreParseOptions : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreParseOptionsCompanion *companion __attribute__((swift_name("companion")));

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
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreLineInterceptor> (^)(void)> *interceptors __attribute__((swift_name("interceptors")));
@property (readonly) CalendrumKtorCalendrum_coreParseLimits *limits __attribute__((swift_name("limits")));
@property (readonly) CalendrumKtorCalendrum_coreParseMode *mode __attribute__((swift_name("mode")));
@property (readonly) id<CalendrumKtorCalendrum_coreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/**
 * The registry that tells eager decoding and strict mode what every property is; [ICalRegistry.Default] unless one
 * was set.
 */
@property (readonly) CalendrumKtorCalendrum_coreICalRegistry *registry __attribute__((swift_name("registry")));
@property (readonly) BOOL retainSource __attribute__((swift_name("retainSource")));

/** Which components and properties are read; [Selection.Full] unless a selection was set. */
@property (readonly) CalendrumKtorCalendrum_coreSelection *selection __attribute__((swift_name("selection")));

/** The clock used for the time budget; the monotonic clock unless one was set. */
@property (readonly) id<CalendrumKtorKotlinTimeSourceWithComparableMarks> timeSource __attribute__((swift_name("timeSource")));
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
@interface CalendrumKtorCalendrum_coreICalWriter : CalendrumKtorBase

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
- (CalendrumKtorKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumKtorCalendrum_coreIcsSchema<id> *)schema calendar:(CalendrumKtorCalendrum_coreVCalendar *)calendar error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:calendar:)")));

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
- (CalendrumKtorKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumKtorCalendrum_coreIcsSchema<id> *)schema error:(NSError * _Nullable * _Nullable)error buildCalendar:(void (^)(CalendrumKtorCalendrum_coreVCalendarBuilder *))buildCalendar __attribute__((swift_name("encode(items:schema:buildCalendar:)")));

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
- (CalendrumKtorKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumKtorCalendrum_coreIcsSchema<id> *)schema calendar:(CalendrumKtorCalendrum_coreVCalendar *)calendar defaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:calendar:defaults:)")));

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
- (CalendrumKtorKotlinByteArray * _Nullable)encodeItems:(NSArray<id> *)items schema:(CalendrumKtorCalendrum_coreIcsSchema<id> *)schema buildCalendar:(void (^)(CalendrumKtorCalendrum_coreVCalendarBuilder *))buildCalendar defaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encode(items:schema:buildCalendar:defaults:)")));

/**
 * Writes [component] and returns the UTF-8 bytes.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumKtorKotlinByteArray * _Nullable)encodeToByteArrayComponent:(CalendrumKtorCalendrum_coreICalComponent *)component error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToByteArray(component:)")));

/**
 * Writes all [components] one after another and returns the UTF-8 bytes.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumKtorKotlinByteArray * _Nullable)encodeToByteArrayComponents:(NSArray<CalendrumKtorCalendrum_coreICalComponent *> *)components error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToByteArray(components:)")));

/**
 * Writes [component] and returns the text.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if the tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSString * _Nullable)encodeToStringComponent:(CalendrumKtorCalendrum_coreICalComponent *)component error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToString(component:)")));

/**
 * Writes all [components] one after another and returns the text.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSString * _Nullable)encodeToStringComponents:(NSArray<CalendrumKtorCalendrum_coreICalComponent *> *)components error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToString(components:)")));

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
- (BOOL)writeComponent:(CalendrumKtorCalendrum_coreICalComponent *)component out:(id<CalendrumKtorCalendrum_coreByteOutput>)out error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(component:out:)")));

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
- (BOOL)writeComponent:(CalendrumKtorCalendrum_coreICalComponent *)component appendable:(id<CalendrumKtorKotlinAppendable>)appendable error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(component:appendable:)")));

/**
 * Writes all [components] to [out] as UTF-8 bytes; [out] is not closed.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponents:(NSArray<CalendrumKtorCalendrum_coreICalComponent *> *)components out:(id<CalendrumKtorCalendrum_coreByteOutput>)out error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(components:out:)")));

/**
 * Writes all [components] to [out] one after another.
 *
 * @throws IllegalArgumentException if a name, value or parameter cannot be written.
 * @throws IllegalStateException if a tree is partial and [WriteOptions.allowPartialWrite] is not set.
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (BOOL)writeComponents:(NSArray<CalendrumKtorCalendrum_coreICalComponent *> *)components appendable:(id<CalendrumKtorKotlinAppendable>)appendable error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("write(components:appendable:)")));
@end

__attribute__((swift_name("Ktor_serializationConfiguration")))
@protocol CalendrumKtorKtor_serializationConfiguration
@required
- (void)registerContentType:(CalendrumKtorKtor_httpContentType *)contentType converter:(id<CalendrumKtorKtor_serializationContentConverter>)converter configuration:(void (^)(id<CalendrumKtorKtor_serializationContentConverter>))configuration __attribute__((swift_name("register(contentType:converter:configuration:)")));
@end

__attribute__((swift_name("Ktor_server_coreRoute")))
@protocol CalendrumKtorKtor_server_coreRoute
@required
- (id<CalendrumKtorKtor_server_coreRoute>)createChildSelector:(CalendrumKtorKtor_server_coreRouteSelector *)selector __attribute__((swift_name("createChild(selector:)")));
- (void)handleBody:(id<CalendrumKtorKotlinSuspendFunction1>)body __attribute__((swift_name("handle(body:)")));
- (id)installPlugin:(id<CalendrumKtorKtor_server_corePlugin>)plugin configure:(void (^)(id))configure __attribute__((swift_name("install(plugin:configure:)")));
- (id)pluginPlugin:(id<CalendrumKtorKtor_server_corePlugin>)plugin __attribute__((swift_name("plugin(plugin:)")));
@property (readonly) id<CalendrumKtorKtor_utilsAttributes> attributes __attribute__((swift_name("attributes")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationEnvironment> environment __attribute__((swift_name("environment")));
@property (readonly) id<CalendrumKtorKtor_server_coreRoute> _Nullable parent __attribute__((swift_name("parent")));
@end

__attribute__((swift_name("KotlinFunction")))
@protocol CalendrumKtorKotlinFunction
@required
@end

__attribute__((swift_name("KotlinSuspendFunction1")))
@protocol CalendrumKtorKotlinSuspendFunction1 <CalendrumKtorKotlinFunction>
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)invokeP1:(id _Nullable)p1 completionHandler:(void (^)(id _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("invoke(p1:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface CalendrumKtorKotlinArray<T> : CalendrumKtorBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(CalendrumKtorInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<CalendrumKtorKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_ioCharset.Companion")))
@interface CalendrumKtorKtor_ioCharsetCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_ioCharsetCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_ioCharset *)forNameName:(NSString *)name __attribute__((swift_name("forName(name:)")));
- (BOOL)isSupportedCharset:(NSString *)charset __attribute__((swift_name("isSupported(charset:)")));
@end

__attribute__((swift_name("Ktor_ioCharsetDecoder")))
@interface CalendrumKtorKtor_ioCharsetDecoder : CalendrumKtorBase
- (instancetype)initWith_charset:(CalendrumKtorKtor_ioCharset *)_charset __attribute__((swift_name("init(_charset:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("Ktor_ioCharsetEncoder")))
@interface CalendrumKtorKtor_ioCharsetEncoder : CalendrumKtorBase
- (instancetype)initWith_charset:(CalendrumKtorKtor_ioCharset *)_charset __attribute__((swift_name("init(_charset:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol CalendrumKtorKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol CalendrumKtorKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol CalendrumKtorKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol CalendrumKtorKotlinKClass <CalendrumKtorKotlinKDeclarationContainer, CalendrumKtorKotlinKAnnotatedElement, CalendrumKtorKotlinKClassifier>
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end

__attribute__((swift_name("KotlinKType")))
@protocol CalendrumKtorKotlinKType
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
@property (readonly) NSArray<CalendrumKtorKotlinKTypeProjection *> *arguments __attribute__((swift_name("arguments")));

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
@property (readonly) id<CalendrumKtorKotlinKClassifier> _Nullable classifier __attribute__((swift_name("classifier")));
@property (readonly) BOOL isMarkedNullable __attribute__((swift_name("isMarkedNullable")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="2.0")
*/
__attribute__((swift_name("KotlinAutoCloseable")))
@protocol CalendrumKtorKotlinAutoCloseable
@required
- (void)close __attribute__((swift_name("close()")));
@end

__attribute__((swift_name("Kotlinx_io_coreRawSource")))
@protocol CalendrumKtorKotlinx_io_coreRawSource <CalendrumKtorKotlinAutoCloseable>
@required
- (int64_t)readAtMostToSink:(CalendrumKtorKotlinx_io_coreBuffer *)sink byteCount:(int64_t)byteCount __attribute__((swift_name("readAtMostTo(sink:byteCount:)")));
@end

__attribute__((swift_name("Kotlinx_io_coreSource")))
@protocol CalendrumKtorKotlinx_io_coreSource <CalendrumKtorKotlinx_io_coreRawSource>
@required
- (BOOL)exhausted __attribute__((swift_name("exhausted()")));
- (id<CalendrumKtorKotlinx_io_coreSource>)peek __attribute__((swift_name("peek()")));
- (int32_t)readAtMostToSink:(CalendrumKtorKotlinByteArray *)sink startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("readAtMostTo(sink:startIndex:endIndex:)")));
- (int8_t)readByte __attribute__((swift_name("readByte()")));
- (int32_t)readInt __attribute__((swift_name("readInt()")));
- (int64_t)readLong __attribute__((swift_name("readLong()")));
- (int16_t)readShort __attribute__((swift_name("readShort()")));
- (void)readToSink:(id<CalendrumKtorKotlinx_io_coreRawSink>)sink byteCount:(int64_t)byteCount __attribute__((swift_name("readTo(sink:byteCount:)")));
- (BOOL)requestByteCount:(int64_t)byteCount __attribute__((swift_name("request(byteCount:)")));
- (void)requireByteCount:(int64_t)byteCount __attribute__((swift_name("require(byteCount:)")));
- (void)skipByteCount:(int64_t)byteCount __attribute__((swift_name("skip(byteCount:)")));
- (int64_t)transferToSink:(id<CalendrumKtorKotlinx_io_coreRawSink>)sink __attribute__((swift_name("transferTo(sink:)")));

/**
 * @note annotations
 *   kotlinx.io.InternalIoApi
*/
@property (readonly) CalendrumKtorKotlinx_io_coreBuffer *buffer __attribute__((swift_name("buffer")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpHeaderValueParam")))
@interface CalendrumKtorKtor_httpHeaderValueParam : CalendrumKtorBase
- (instancetype)initWithName:(NSString *)name value:(NSString *)value __attribute__((swift_name("init(name:value:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithName:(NSString *)name value:(NSString *)value escapeValue:(BOOL)escapeValue __attribute__((swift_name("init(name:value:escapeValue:)"))) __attribute__((objc_designated_initializer));
- (CalendrumKtorKtor_httpHeaderValueParam *)doCopyName:(NSString *)name value:(NSString *)value escapeValue:(BOOL)escapeValue __attribute__((swift_name("doCopy(name:value:escapeValue:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL escapeValue __attribute__((swift_name("escapeValue")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpHeaderValueWithParameters.Companion")))
@interface CalendrumKtorKtor_httpHeaderValueWithParametersCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpHeaderValueWithParametersCompanion *shared __attribute__((swift_name("shared")));
- (id _Nullable)parseValue:(NSString *)value init:(id _Nullable (^)(NSString *, NSArray<CalendrumKtorKtor_httpHeaderValueParam *> *))init __attribute__((swift_name("parse(value:init:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpContentType.Companion")))
@interface CalendrumKtorKtor_httpContentTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpContentTypeCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_httpContentType *)parseValue:(NSString *)value __attribute__((swift_name("parse(value:)")));
@property (readonly) CalendrumKtorKtor_httpContentType *Any __attribute__((swift_name("Any")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsAttributeKey")))
@interface CalendrumKtorKtor_utilsAttributeKey<T> : CalendrumKtorBase

/**
 * @note annotations
 *   kotlin.jvm.JvmOverloads
*/
- (instancetype)initWithName:(NSString *)name type:(CalendrumKtorKtor_utilsTypeInfo *)type __attribute__((swift_name("init(name:type:)"))) __attribute__((objc_designated_initializer));
- (CalendrumKtorKtor_utilsAttributeKey<T> *)doCopyName:(NSString *)name type:(CalendrumKtorKtor_utilsTypeInfo *)type __attribute__((swift_name("doCopy(name:type:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((swift_name("Ktor_utilsStringValues")))
@protocol CalendrumKtorKtor_utilsStringValues
@required
- (BOOL)containsName:(NSString *)name __attribute__((swift_name("contains(name:)")));
- (BOOL)containsName:(NSString *)name value:(NSString *)value __attribute__((swift_name("contains(name:value:)")));
- (NSSet<id<CalendrumKtorKotlinMapEntry>> *)entries __attribute__((swift_name("entries()")));
- (void)forEachBody:(void (^)(NSString *, NSArray<NSString *> *))body __attribute__((swift_name("forEach(body:)")));
- (NSString * _Nullable)getName:(NSString *)name __attribute__((swift_name("get(name:)")));
- (NSArray<NSString *> * _Nullable)getAllName:(NSString *)name __attribute__((swift_name("getAll(name:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (NSSet<NSString *> *)names __attribute__((swift_name("names()")));
@property (readonly) BOOL caseInsensitiveName __attribute__((swift_name("caseInsensitiveName")));
@end

__attribute__((swift_name("Ktor_httpHeaders")))
@protocol CalendrumKtorKtor_httpHeaders <CalendrumKtorKtor_utilsStringValues>
@required
@end

__attribute__((swift_name("KotlinComparable")))
@protocol CalendrumKtorKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpHttpStatusCode")))
@interface CalendrumKtorKtor_httpHttpStatusCode : CalendrumKtorBase <CalendrumKtorKotlinComparable>
- (instancetype)initWithValue:(int32_t)value description:(NSString *)description __attribute__((swift_name("init(value:description:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpHttpStatusCodeCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumKtorKtor_httpHttpStatusCode *)other __attribute__((swift_name("compareTo(other:)")));
- (CalendrumKtorKtor_httpHttpStatusCode *)doCopyValue:(int32_t)value description:(NSString *)description __attribute__((swift_name("doCopy(value:description:)")));
- (CalendrumKtorKtor_httpHttpStatusCode *)descriptionValue:(NSString *)value __attribute__((swift_name("description(value:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *description_ __attribute__((swift_name("description_")));
@property (readonly) int32_t value __attribute__((swift_name("value")));
@end


/** The shared defaults and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseOptions.Companion")))
@interface CalendrumKtorCalendrum_coreParseOptionsCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The shared defaults and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreParseOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the defaults. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** Lenient mode, the default limits, a retained source and no listener, hint, interceptors or selection. */
@property (readonly) CalendrumKtorCalendrum_coreParseOptions *Default __attribute__((swift_name("Default")));

/**
 * Options for files from strangers: lenient, but with nesting depth 8, lines up to 64 KiB, 1,000,000 lines,
 * 100,000 components, input up to 64 MiB and a time budget of 2 seconds.
 */
@property (readonly) CalendrumKtorCalendrum_coreParseOptions *Untrusted __attribute__((swift_name("Untrusted")));
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
@protocol CalendrumKtorCalendrum_coreLineInterceptor
@required

/**
 * Called once for every property line.
 *
 * The [line] is valid only during this call; do not keep it. The function must not throw for data reasons.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (void)interceptLine:(CalendrumKtorCalendrum_coreInterceptedLine *)line __attribute__((swift_name("intercept(line:)")));
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
@interface CalendrumKtorCalendrum_coreParseLimits : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreParseLimitsCompanion *companion __attribute__((swift_name("companion")));

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

__attribute__((swift_name("KotlinEnum")))
@interface CalendrumKtorKotlinEnum<E> : CalendrumKtorBase <CalendrumKtorKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end


/** How a parser reacts to problems in the input. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseMode")))
@interface CalendrumKtorCalendrum_coreParseMode : CalendrumKtorKotlinEnum<CalendrumKtorCalendrum_coreParseMode *>
+ (instancetype)alloc __attribute__((unavailable));

/** How a parser reacts to problems in the input. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorCalendrum_coreParseMode *lenient __attribute__((swift_name("lenient")));
@property (class, readonly) CalendrumKtorCalendrum_coreParseMode *strict __attribute__((swift_name("strict")));
+ (CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreParseMode *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorCalendrum_coreParseMode *> *entries __attribute__((swift_name("entries")));
@end


/**
 * Receives problems as a reader or an expander finds them.
 *
 * The listener is called on the thread that does the work, in the order the problems are found. It must not throw: an
 * exception thrown by a listener propagates out of the call that was running.
 */
__attribute__((swift_name("Calendrum_coreProblemListener")))
@protocol CalendrumKtorCalendrum_coreProblemListener
@required

/** Called once for every reported problem. */
- (void)onProblemProblem:(CalendrumKtorCalendrum_coreProblem *)problem __attribute__((swift_name("onProblem(problem:)")));
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
@interface CalendrumKtorCalendrum_coreICalRegistry : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreICalRegistryCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the definition of the component [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumKtorCalendrum_coreComponentDefinition * _Nullable)componentName:(NSString *)name __attribute__((swift_name("component(name:)")));

/**
 * Returns the definition of the parameter [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumKtorCalendrum_coreParameterDefinition * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/**
 * Returns the definition of the property [name], ignoring ASCII case, or `null` when the registry does not know it.
 * When several components define the property differently, the first definition is returned.
 */
- (CalendrumKtorCalendrum_corePropertyDefinition * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalExtension>> *extensions __attribute__((swift_name("extensions")));
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
@interface CalendrumKtorCalendrum_coreSelection : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreSelectionCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreTimeZoneSelection *timeZones __attribute__((swift_name("timeZones")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeSource")))
@protocol CalendrumKtorKotlinTimeSource
@required
- (id<CalendrumKtorKotlinTimeMark>)markNow __attribute__((swift_name("markNow()")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeSourceWithComparableMarks")))
@protocol CalendrumKtorKotlinTimeSourceWithComparableMarks <CalendrumKtorKotlinTimeSource>
@required
@end

__attribute__((swift_name("KotlinIllegalArgumentException")))
@interface CalendrumKtorKotlinIllegalArgumentException : CalendrumKtorKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumKtorKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinByteArray")))
@interface CalendrumKtorKotlinByteArray : CalendrumKtorBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumKtorByte *(^)(CalendrumKtorInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int8_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumKtorKotlinByteIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int8_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
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
@interface CalendrumKtorCalendrum_coreIcsSchema<T> : CalendrumKtorBase
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
- (CalendrumKtorCalendrum_coreIcsSchemaField<NSArray<id> *> *)childFieldSchema:(CalendrumKtorCalendrum_coreIcsSchema<id> *)schema get:(NSArray<id> *(^)(T))get __attribute__((swift_name("childField(schema:get:)")));

/**
 * Builds the object from the decoded [values]; read each declared field with `values[field]`.
 *
 * @param values the decoded values of one component.
 */
- (T)createValues:(CalendrumKtorCalendrum_coreIcsSchemaValues *)values __attribute__((swift_name("create(values:)")));

/**
 * Declares a required field that holds the value of the property [key] as it is decoded; [get] reads it back when
 * writing.
 *
 * @param key the property.
 * @param get reads the value from an object when writing.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumKtorCalendrum_coreIcsSchemaField<id> *)fieldKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key get:(id (^)(T))get __attribute__((swift_name("field(key:get:)")));

/**
 * Declares a required field whose value is converted from the decoded property value by [decode].
 *
 * @param key the property.
 * @param get reads the value from an object when writing; it returns the property value.
 * @param decode converts the decoded value into the type of the field.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumKtorCalendrum_coreIcsSchemaField<id> *)fieldKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key get:(id (^)(T))get decode:(id (^)(id))decode __attribute__((swift_name("field(key:get:decode:)")));

/**
 * Declares a field that collects every property [key] into a list; empty when there is none.
 *
 * @param key the property.
 * @param get reads the list from an object when writing; each element becomes a property.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumKtorCalendrum_coreIcsSchemaField<NSArray<id> *> *)listFieldKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key get:(NSArray<id> *(^)(T))get __attribute__((swift_name("listField(key:get:)")));

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
- (CalendrumKtorCalendrum_coreIcsSchemaField<NSArray<id> *> *)listFieldKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key get:(NSArray<id> *(^)(T))get decode:(id _Nullable (^)(id))decode encode:(id (^)(id _Nullable))encode __attribute__((swift_name("listField(key:get:decode:encode:)")));

/**
 * Declares an optional field: `null` when the property is missing or does not decode.
 *
 * @param key the property.
 * @param get reads the value from an object when writing; `null` writes nothing.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumKtorCalendrum_coreIcsSchemaField<id> *)optionalFieldKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key get:(id _Nullable (^)(T))get __attribute__((swift_name("optionalField(key:get:)")));

/**
 * Declares an optional field whose value is converted from the decoded property value by [decode].
 *
 * @param key the property.
 * @param get reads the value from an object when writing; `null` writes nothing.
 * @param decode converts the decoded value into the type of the field.
 *
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (CalendrumKtorCalendrum_coreIcsSchemaField<id> *)optionalFieldKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key get:(id _Nullable (^)(T))get decode:(id (^)(id))decode __attribute__((swift_name("optionalField(key:get:decode:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
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
@interface CalendrumKtorCalendrum_coreVCalendar : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVCalendarCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this calendar changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this calendar.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVCalendar *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVCalendarBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VCalendar with an equal VCALENDAR component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this calendar; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this calendar and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

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
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

/**
 * The descriptions of the calendar (`DESCRIPTION` properties,
 * [RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *descriptions __attribute__((swift_name("descriptions")));

/** The events nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy component nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The images of the calendar (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/** The journal entries nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)), or `null` when it is missing or does
 * not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreMethod * _Nullable method __attribute__((swift_name("method")));

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
@property (readonly) CalendrumKtorCalendrum_coreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar (`SOURCE`, [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos nested in the calendar, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVTodo *> *todos __attribute__((swift_name("todos")));

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
@interface CalendrumKtorCalendrum_coreComponentBuilder<B> : CalendrumKtorBase

/** Returns the value of the first property of [key], or `null`. */
- (id _Nullable)getKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key __attribute__((swift_name("get(key:)")));

/**
 * Adds [property] as it is.
 *
 * @return this builder.
 */
- (B)propertyProperty:(CalendrumKtorCalendrum_coreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Adds a property of [key] with [value] after the existing ones.
 *
 * @return this builder.
 */
- (B)propertyKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key value:(id)value __attribute__((swift_name("property(key:value:)")));

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
- (B)removeKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key __attribute__((swift_name("remove(key:)")));

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
- (B)setKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key value:(id _Nullable)value __attribute__((swift_name("set(key:value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * Collects the properties and nested components of a new or edited calendar, with a Kotlin DSL and chained calls.
 * Get one from [VCalendar.builder] or [VCalendar.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVCalendar.Builder")))
@interface CalendrumKtorCalendrum_coreVCalendarBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVCalendarBuilder *>

/** Creates the calendar. */
- (CalendrumKtorCalendrum_coreVCalendar *)build __attribute__((swift_name("build()")));

/**
 * Sets [calScale] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)calScaleValue:(NSString * _Nullable)value __attribute__((swift_name("calScale(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)categoriesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds [event] as a event of the calendar. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)eventEvent:(CalendrumKtorCalendrum_coreVEvent *)event __attribute__((swift_name("event(event:)")));

/**
 * Adds a event that [block] sets up.
 *
 * @param block sets up the new event.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)eventBlock:(void (^)(CalendrumKtorCalendrum_coreVEventBuilder *))block __attribute__((swift_name("event(block:)")));

/** Edits the events of the calendar in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)eventsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVEvent *> *))block __attribute__((swift_name("events(block:)")));

/** Edits the free/busy components of the calendar in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)freeBusiesBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVFreeBusy *> *))block __attribute__((swift_name("freeBusies(block:)")));

/** Adds [freeBusy] as a free/busy component of the calendar. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)freeBusyFreeBusy:(CalendrumKtorCalendrum_coreVFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/**
 * Adds a free/busy component that [block] sets up.
 *
 * @param block sets up the new free/busy component.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)freeBusyBlock:(void (^)(CalendrumKtorCalendrum_coreVFreeBusyBuilder *))block __attribute__((swift_name("freeBusy(block:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)imageValue:(CalendrumKtorCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/** Adds [journal] as a journal entry of the calendar. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)journalJournal:(CalendrumKtorCalendrum_coreVJournal *)journal __attribute__((swift_name("journal(journal:)")));

/**
 * Adds a journal entry that [block] sets up.
 *
 * @param block sets up the new journal entry.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)journalBlock:(void (^)(CalendrumKtorCalendrum_coreVJournalBuilder *))block __attribute__((swift_name("journal(block:)")));

/** Edits the journal entries of the calendar in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)journalsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVJournal *> *))block __attribute__((swift_name("journals(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)lastModifiedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/**
 * Sets [method] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)methodValue:(CalendrumKtorCalendrum_coreMethod * _Nullable)value __attribute__((swift_name("method(value:)")));

/** Adds a `NAME` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/**
 * Sets [prodId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)prodIdValue:(NSString * _Nullable)value __attribute__((swift_name("prodId(value:)")));

/**
 * Sets [refreshInterval] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)refreshIntervalValue:(CalendrumKtorCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("refreshInterval(value:)")));

/**
 * Sets [source] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)sourceValue:(NSString * _Nullable)value __attribute__((swift_name("source(value:)")));

/** Adds [timeZone] as a time zone of the calendar. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)timeZoneTimeZone:(CalendrumKtorCalendrum_coreVTimeZone *)timeZone __attribute__((swift_name("timeZone(timeZone:)")));

/**
 * Adds a time zone that [block] sets up.
 *
 * @param block sets up the new time zone.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)timeZoneBlock:(void (^)(CalendrumKtorCalendrum_coreVTimeZoneBuilder *))block __attribute__((swift_name("timeZone(block:)")));

/** Edits the time zones of the calendar in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)timeZonesBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVTimeZone *> *))block __attribute__((swift_name("timeZones(block:)")));

/** Adds [todo] as a to-do of the calendar. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)todoTodo:(CalendrumKtorCalendrum_coreVTodo *)todo __attribute__((swift_name("todo(todo:)")));

/**
 * Adds a to-do that [block] sets up.
 *
 * @param block sets up the new to-do.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)todoBlock:(void (^)(CalendrumKtorCalendrum_coreVTodoBuilder *))block __attribute__((swift_name("todo(block:)")));

/** Edits the to-dos of the calendar in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)todosBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVTodo *> *))block __attribute__((swift_name("todos(block:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * Sets [version] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)versionValue:(NSString * _Nullable)value __attribute__((swift_name("version(value:)")));

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
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVEvent *> *events __attribute__((swift_name("events")));

/** The free/busy components of the calendar being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVFreeBusy *> *freeBusies __attribute__((swift_name("freeBusies")));

/** The journal entries of the calendar being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVJournal *> *journals __attribute__((swift_name("journals")));

/**
 * The time of the last modification of the calendar being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The iTIP method of the calendar being built (`METHOD`,
 * [RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreMethod * _Nullable method __attribute__((swift_name("method")));

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
@property CalendrumKtorCalendrum_coreICalDuration * _Nullable refreshInterval __attribute__((swift_name("refreshInterval")));

/**
 * The source URI of the calendar being built (`SOURCE`,
 * [RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)); assigning `null` removes the property.
 */
@property NSString * _Nullable source __attribute__((swift_name("source")));

/** The time zones of the calendar being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVTimeZone *> *timeZones __attribute__((swift_name("timeZones")));

/** The to-dos of the calendar being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVTodo *> *todos __attribute__((swift_name("todos")));

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
@interface CalendrumKtorCalendrum_coreBuildDefaults : CalendrumKtorBase
- (instancetype)initWithClock:(id<CalendrumKtorKotlinClock>)clock uids:(id<CalendrumKtorCalendrum_coreUidGenerator>)uids __attribute__((swift_name("init(clock:uids:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorKotlinClock> clock __attribute__((swift_name("clock")));
@property (readonly) id<CalendrumKtorCalendrum_coreUidGenerator> uids __attribute__((swift_name("uids")));
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
@interface CalendrumKtorCalendrum_coreICalComponent : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreICalComponentCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the directly nested components named [name], ignoring ASCII case, in source order; the list is empty when
 * there are none. Deeper levels are not searched.
 *
 * @param name a component name such as `VEVENT`.
 */
- (NSArray<CalendrumKtorCalendrum_coreICalComponent *> *)componentsName:(NSString *)name __attribute__((swift_name("components(name:)")));

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
- (NSArray<CalendrumKtorCalendrum_coreICalProperty *> *)propertiesName:(NSString *)name __attribute__((swift_name("properties(name:)")));

/**
 * Returns the first property named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a property name such as `SUMMARY`.
 */
- (CalendrumKtorCalendrum_coreICalProperty * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns the name and the number of properties and nested components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The directly nested components, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalComponent *> *components __attribute__((swift_name("components")));

/**
 * The canonical name in upper case, such as `VEVENT`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/**
 * The properties of this component, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalProperty *> *properties __attribute__((swift_name("properties")));

/**
 * Where the component starts in the input and where its `END` line ends, or `null` for a component that was built
 * by hand or edited.
 */
@property (readonly) CalendrumKtorCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));
@end


/**
 * A push sink of bytes, the platform-independent counterpart of an output stream.
 *
 * The writer never flushes or closes an output; both are the caller's job.
 */
__attribute__((swift_name("Calendrum_coreByteOutput")))
@protocol CalendrumKtorCalendrum_coreByteOutput
@required

/** Writes [length] bytes of [buffer] starting at [offset]. */
- (void)writeBuffer:(CalendrumKtorKotlinByteArray *)buffer offset:(int32_t)offset length:(int32_t)length __attribute__((swift_name("write(buffer:offset:length:)")));
@end

__attribute__((swift_name("KotlinAppendable")))
@protocol CalendrumKtorKotlinAppendable
@required

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumKtorKotlinAppendable>)appendValue:(unichar)value __attribute__((swift_name("append(value:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumKtorKotlinAppendable>)appendValue_:(id _Nullable)value __attribute__((swift_name("append(value_:)")));

/**
 * @note annotations
 *   kotlin.IgnorableReturnValue
*/
- (id<CalendrumKtorKotlinAppendable>)appendValue:(id _Nullable)value startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("append(value:startIndex:endIndex:)")));
@end

__attribute__((swift_name("Ktor_server_coreRouteSelector")))
@interface CalendrumKtorKtor_server_coreRouteSelector : CalendrumKtorBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)evaluateContext:(CalendrumKtorKtor_server_coreRoutingResolveContext *)context segmentIndex:(int32_t)segmentIndex completionHandler:(void (^)(CalendrumKtorKtor_server_coreRouteSelectorEvaluation * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("evaluate(context:segmentIndex:completionHandler:)")));
@end

__attribute__((swift_name("Ktor_server_corePlugin")))
@protocol CalendrumKtorKtor_server_corePlugin
@required
- (id)installPipeline:(CalendrumKtorKtor_utilsPipeline *)pipeline configure:(void (^)(id))configure __attribute__((swift_name("install(pipeline:configure:)")));
@property (readonly) CalendrumKtorKtor_utilsAttributeKey<id> *key __attribute__((swift_name("key")));
@end

__attribute__((swift_name("Ktor_utilsAttributes")))
@protocol CalendrumKtorKtor_utilsAttributes
@required
- (id)computeIfAbsentKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key block:(id (^)(void))block __attribute__((swift_name("computeIfAbsent(key:block:)")));
- (BOOL)containsKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key __attribute__((swift_name("contains(key:)")));
- (id)getKey_:(CalendrumKtorKtor_utilsAttributeKey<id> *)key __attribute__((swift_name("get(key_:)")));
- (id _Nullable)getOrNullKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key __attribute__((swift_name("getOrNull(key:)")));
- (void)putKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key value:(id)value __attribute__((swift_name("put(key:value:)")));
- (void)removeKey_:(CalendrumKtorKtor_utilsAttributeKey<id> *)key __attribute__((swift_name("remove(key_:)")));
- (id)takeKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key __attribute__((swift_name("take(key:)")));
- (id _Nullable)takeOrNullKey:(CalendrumKtorKtor_utilsAttributeKey<id> *)key __attribute__((swift_name("takeOrNull(key:)")));
@property (readonly) NSArray<CalendrumKtorKtor_utilsAttributeKey<id> *> *allKeys __attribute__((swift_name("allKeys")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationEnvironment")))
@protocol CalendrumKtorKtor_server_coreApplicationEnvironment
@required
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationConfig> config __attribute__((swift_name("config")));
@property (readonly) id<CalendrumKtorKtor_utilsLogger> log __attribute__((swift_name("log")));
@property (readonly) CalendrumKtorKtor_eventsEvents *monitor __attribute__((swift_name("monitor"))) __attribute__((deprecated("Moved to Application")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol CalendrumKtorKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinKTypeProjection")))
@interface CalendrumKtorKotlinKTypeProjection : CalendrumKtorBase
- (instancetype)initWithVariance:(CalendrumKtorKotlinKVariance * _Nullable)variance type:(id<CalendrumKtorKotlinKType> _Nullable)type __attribute__((swift_name("init(variance:type:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKotlinKTypeProjectionCompanion *companion __attribute__((swift_name("companion")));
- (CalendrumKtorKotlinKTypeProjection *)doCopyVariance:(CalendrumKtorKotlinKVariance * _Nullable)variance type:(id<CalendrumKtorKotlinKType> _Nullable)type __attribute__((swift_name("doCopy(variance:type:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorKotlinKType> _Nullable type __attribute__((swift_name("type")));
@property (readonly) CalendrumKtorKotlinKVariance * _Nullable variance __attribute__((swift_name("variance")));
@end

__attribute__((swift_name("Kotlinx_io_coreRawSink")))
@protocol CalendrumKtorKotlinx_io_coreRawSink <CalendrumKtorKotlinAutoCloseable>
@required
- (void)flush __attribute__((swift_name("flush()")));
- (void)writeSource:(CalendrumKtorKotlinx_io_coreBuffer *)source byteCount:(int64_t)byteCount __attribute__((swift_name("write(source:byteCount:)")));
@end

__attribute__((swift_name("Kotlinx_io_coreSink")))
@protocol CalendrumKtorKotlinx_io_coreSink <CalendrumKtorKotlinx_io_coreRawSink>
@required
- (void)emit __attribute__((swift_name("emit()")));

/**
 * @note annotations
 *   kotlinx.io.InternalIoApi
*/
- (void)hintEmit __attribute__((swift_name("hintEmit()")));
- (int64_t)transferFromSource:(id<CalendrumKtorKotlinx_io_coreRawSource>)source __attribute__((swift_name("transferFrom(source:)")));
- (void)writeSource:(id<CalendrumKtorKotlinx_io_coreRawSource>)source byteCount_:(int64_t)byteCount __attribute__((swift_name("write(source:byteCount_:)")));
- (void)writeSource:(CalendrumKtorKotlinByteArray *)source startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("write(source:startIndex:endIndex:)")));
- (void)writeByteByte:(int8_t)byte __attribute__((swift_name("writeByte(byte:)")));
- (void)writeIntInt:(int32_t)int_ __attribute__((swift_name("writeInt(int:)")));
- (void)writeLongLong:(int64_t)long_ __attribute__((swift_name("writeLong(long:)")));
- (void)writeShortShort:(int16_t)short_ __attribute__((swift_name("writeShort(short:)")));

/**
 * @note annotations
 *   kotlinx.io.InternalIoApi
*/
@property (readonly) CalendrumKtorKotlinx_io_coreBuffer *buffer __attribute__((swift_name("buffer")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_io_coreBuffer")))
@interface CalendrumKtorKotlinx_io_coreBuffer : CalendrumKtorBase <CalendrumKtorKotlinx_io_coreSource, CalendrumKtorKotlinx_io_coreSink>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)clear __attribute__((swift_name("clear()")));
- (void)close __attribute__((swift_name("close()")));
- (CalendrumKtorKotlinx_io_coreBuffer *)doCopy __attribute__((swift_name("doCopy()")));
- (void)doCopyToOut:(CalendrumKtorKotlinx_io_coreBuffer *)out startIndex:(int64_t)startIndex endIndex:(int64_t)endIndex __attribute__((swift_name("doCopyTo(out:startIndex:endIndex:)")));
- (void)emit __attribute__((swift_name("emit()")));
- (BOOL)exhausted __attribute__((swift_name("exhausted()")));
- (void)flush __attribute__((swift_name("flush()")));
- (int8_t)getPosition:(int64_t)position __attribute__((swift_name("get(position:)")));

/**
 * @note annotations
 *   kotlinx.io.InternalIoApi
*/
- (void)hintEmit __attribute__((swift_name("hintEmit()")));
- (id<CalendrumKtorKotlinx_io_coreSource>)peek __attribute__((swift_name("peek()")));
- (int64_t)readAtMostToSink:(CalendrumKtorKotlinx_io_coreBuffer *)sink byteCount:(int64_t)byteCount __attribute__((swift_name("readAtMostTo(sink:byteCount:)")));
- (int32_t)readAtMostToSink:(CalendrumKtorKotlinByteArray *)sink startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("readAtMostTo(sink:startIndex:endIndex:)")));
- (int8_t)readByte __attribute__((swift_name("readByte()")));
- (int32_t)readInt __attribute__((swift_name("readInt()")));
- (int64_t)readLong __attribute__((swift_name("readLong()")));
- (int16_t)readShort __attribute__((swift_name("readShort()")));
- (void)readToSink:(id<CalendrumKtorKotlinx_io_coreRawSink>)sink byteCount:(int64_t)byteCount __attribute__((swift_name("readTo(sink:byteCount:)")));
- (BOOL)requestByteCount:(int64_t)byteCount __attribute__((swift_name("request(byteCount:)")));
- (void)requireByteCount:(int64_t)byteCount __attribute__((swift_name("require(byteCount:)")));
- (void)skipByteCount:(int64_t)byteCount __attribute__((swift_name("skip(byteCount:)")));
- (NSString *)description __attribute__((swift_name("description()")));
- (int64_t)transferFromSource:(id<CalendrumKtorKotlinx_io_coreRawSource>)source __attribute__((swift_name("transferFrom(source:)")));
- (int64_t)transferToSink:(id<CalendrumKtorKotlinx_io_coreRawSink>)sink __attribute__((swift_name("transferTo(sink:)")));
- (void)writeSource:(CalendrumKtorKotlinx_io_coreBuffer *)source byteCount:(int64_t)byteCount __attribute__((swift_name("write(source:byteCount:)")));
- (void)writeSource:(id<CalendrumKtorKotlinx_io_coreRawSource>)source byteCount_:(int64_t)byteCount __attribute__((swift_name("write(source:byteCount_:)")));
- (void)writeSource:(CalendrumKtorKotlinByteArray *)source startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("write(source:startIndex:endIndex:)")));
- (void)writeByteByte:(int8_t)byte __attribute__((swift_name("writeByte(byte:)")));
- (void)writeIntInt:(int32_t)int_ __attribute__((swift_name("writeInt(int:)")));
- (void)writeLongLong:(int64_t)long_ __attribute__((swift_name("writeLong(long:)")));
- (void)writeShortShort:(int16_t)short_ __attribute__((swift_name("writeShort(short:)")));

/**
 * @note annotations
 *   kotlinx.io.InternalIoApi
*/
@property (readonly) CalendrumKtorKotlinx_io_coreBuffer *buffer __attribute__((swift_name("buffer")));
@property (readonly) int64_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinMapEntry")))
@protocol CalendrumKtorKotlinMapEntry
@required
@property (readonly) id _Nullable key __attribute__((swift_name("key")));
@property (readonly) id _Nullable value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpHttpStatusCode.Companion")))
@interface CalendrumKtorKtor_httpHttpStatusCodeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpHttpStatusCodeCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_httpHttpStatusCode *)fromValueValue:(int32_t)value __attribute__((swift_name("fromValue(value:)")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Accepted __attribute__((swift_name("Accepted")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *BadGateway __attribute__((swift_name("BadGateway")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *BadRequest __attribute__((swift_name("BadRequest")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Conflict __attribute__((swift_name("Conflict")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Continue __attribute__((swift_name("Continue")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Created __attribute__((swift_name("Created")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *ExpectationFailed __attribute__((swift_name("ExpectationFailed")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *FailedDependency __attribute__((swift_name("FailedDependency")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Forbidden __attribute__((swift_name("Forbidden")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Found __attribute__((swift_name("Found")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *GatewayTimeout __attribute__((swift_name("GatewayTimeout")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Gone __attribute__((swift_name("Gone")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *InsufficientStorage __attribute__((swift_name("InsufficientStorage")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *InternalServerError __attribute__((swift_name("InternalServerError")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *LengthRequired __attribute__((swift_name("LengthRequired")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Locked __attribute__((swift_name("Locked")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *MethodNotAllowed __attribute__((swift_name("MethodNotAllowed")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *MovedPermanently __attribute__((swift_name("MovedPermanently")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *MultiStatus __attribute__((swift_name("MultiStatus")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *MultipleChoices __attribute__((swift_name("MultipleChoices")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *NoContent __attribute__((swift_name("NoContent")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *NonAuthoritativeInformation __attribute__((swift_name("NonAuthoritativeInformation")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *NotAcceptable __attribute__((swift_name("NotAcceptable")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *NotFound __attribute__((swift_name("NotFound")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *NotImplemented __attribute__((swift_name("NotImplemented")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *NotModified __attribute__((swift_name("NotModified")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *OK __attribute__((swift_name("OK")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *PartialContent __attribute__((swift_name("PartialContent")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *PayloadTooLarge __attribute__((swift_name("PayloadTooLarge")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *PaymentRequired __attribute__((swift_name("PaymentRequired")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *PermanentRedirect __attribute__((swift_name("PermanentRedirect")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *PreconditionFailed __attribute__((swift_name("PreconditionFailed")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Processing __attribute__((swift_name("Processing")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *ProxyAuthenticationRequired __attribute__((swift_name("ProxyAuthenticationRequired")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *RequestHeaderFieldTooLarge __attribute__((swift_name("RequestHeaderFieldTooLarge")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *RequestTimeout __attribute__((swift_name("RequestTimeout")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *RequestURITooLong __attribute__((swift_name("RequestURITooLong")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *RequestedRangeNotSatisfiable __attribute__((swift_name("RequestedRangeNotSatisfiable")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *ResetContent __attribute__((swift_name("ResetContent")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *SeeOther __attribute__((swift_name("SeeOther")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *ServiceUnavailable __attribute__((swift_name("ServiceUnavailable")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *SwitchProxy __attribute__((swift_name("SwitchProxy")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *SwitchingProtocols __attribute__((swift_name("SwitchingProtocols")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *TemporaryRedirect __attribute__((swift_name("TemporaryRedirect")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *TooEarly __attribute__((swift_name("TooEarly")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *TooManyRequests __attribute__((swift_name("TooManyRequests")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *Unauthorized __attribute__((swift_name("Unauthorized")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *UnprocessableEntity __attribute__((swift_name("UnprocessableEntity")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *UnsupportedMediaType __attribute__((swift_name("UnsupportedMediaType")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *UpgradeRequired __attribute__((swift_name("UpgradeRequired")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *UseProxy __attribute__((swift_name("UseProxy")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *VariantAlsoNegotiates __attribute__((swift_name("VariantAlsoNegotiates")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *VersionNotSupported __attribute__((swift_name("VersionNotSupported")));
@property (readonly) NSArray<CalendrumKtorKtor_httpHttpStatusCode *> *allStatusCodes __attribute__((swift_name("allStatusCodes")));
@end


/**
 * Collects [ParseOptions] values. The properties and the functions of the same name do the same; the functions
 * chain. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseOptions.Builder")))
@interface CalendrumKtorCalendrum_coreParseOptionsBuilder : CalendrumKtorBase

/** Creates the options. */
- (CalendrumKtorCalendrum_coreParseOptions *)build __attribute__((swift_name("build()")));

/** Sets the charset hint; `null` removes it. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)charsetCharset:(NSString * _Nullable)charset __attribute__((swift_name("charset(charset:)")));

/** Sets [decodeEagerly]. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)decodeEagerlyEager:(BOOL)eager __attribute__((swift_name("decodeEagerly(eager:)")));

/** Adds a stateless [interceptor]; the same instance serves every parse and must be thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)interceptInterceptor:(id<CalendrumKtorCalendrum_coreLineInterceptor>)interceptor __attribute__((swift_name("intercept(interceptor:)")));

/**
 * Adds an interceptor created by [factory] for every parse, so that a stateful interceptor gets a fresh
 * instance each time. Interceptors run in the order they were added.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)interceptFactory:(id<CalendrumKtorCalendrum_coreLineInterceptor> (^)(void))factory __attribute__((swift_name("intercept(factory:)")));

/** Sets [limits]. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)limitsLimits:(CalendrumKtorCalendrum_coreParseLimits *)limits __attribute__((swift_name("limits(limits:)")));

/** Sets [mode]. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)modeMode:(CalendrumKtorCalendrum_coreParseMode *)mode __attribute__((swift_name("mode(mode:)")));

/** Sets the problem listener; `null` removes it. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)problemListenerListener:(id<CalendrumKtorCalendrum_coreProblemListener> _Nullable)listener __attribute__((swift_name("problemListener(listener:)")));

/** Sets [registry]; `null` selects the default registry. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)registryRegistry:(CalendrumKtorCalendrum_coreICalRegistry * _Nullable)registry __attribute__((swift_name("registry(registry:)")));

/** Sets [retainSource]. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)retainSourceRetain:(BOOL)retain __attribute__((swift_name("retainSource(retain:)")));

/** Reads only what [selection] names. The rest is skipped in the lexer; the result is partial. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)selectSelection:(CalendrumKtorCalendrum_coreSelection *)selection __attribute__((swift_name("select(selection:)")));

/** Reads only what the selection built by [block] names. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)selectBlock:(void (^)(CalendrumKtorCalendrum_coreSelectionBuilder *))block __attribute__((swift_name("select(block:)")));

/** Sets the clock for the time budget. */
- (CalendrumKtorCalendrum_coreParseOptionsBuilder *)timeSourceSource:(id<CalendrumKtorKotlinTimeSourceWithComparableMarks>)source __attribute__((swift_name("timeSource(source:)")));

/**
 * A hint for the encoding of byte input: a charset name or a whole `Content-Type` value such as `text/calendar;
 * charset=windows-1251`. The hint is checked against the data; a contradiction is reported (`ICAL-S027`) and
 * the data decides. `null` (the default) means no hint.
 */
@property NSString * _Nullable charset __attribute__((swift_name("charset")));

/** Decode every value while parsing and report its problems; false by default. Strict mode implies it. */
@property BOOL decodeEagerly __attribute__((swift_name("decodeEagerly")));

/** The bounds on the input; [ParseLimits.Default] by default. */
@property CalendrumKtorCalendrum_coreParseLimits *limits __attribute__((swift_name("limits")));

/** Lenient (the default) or strict reading. */
@property CalendrumKtorCalendrum_coreParseMode *mode __attribute__((swift_name("mode")));

/** Receives each reported problem as it is found; `null` (the default) for none. */
@property id<CalendrumKtorCalendrum_coreProblemListener> _Nullable problemListener __attribute__((swift_name("problemListener")));

/** The registry for eager decoding and strict mode; `null` selects [ICalRegistry.Default]. */
@property CalendrumKtorCalendrum_coreICalRegistry * _Nullable registry __attribute__((swift_name("registry")));

/**
 * Whether the tree keeps references to the input bytes (true, the default) or copies the lines it needs.
 * Retaining is faster and allocates less, but holds the whole input as long as any node lives; copying lets a
 * small selection outlive a big input.
 */
@property BOOL retainSource __attribute__((swift_name("retainSource")));

/** Which components and properties to read; `null` (the default) reads everything. */
@property CalendrumKtorCalendrum_coreSelection * _Nullable selection __attribute__((swift_name("selection")));

/**
 * The clock used for [ParseLimits.maxTime]; the monotonic clock by default. Set a test clock to make the budget
 * deterministic.
 */
@property id<CalendrumKtorKotlinTimeSourceWithComparableMarks> timeSource __attribute__((swift_name("timeSource")));
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
@interface CalendrumKtorCalendrum_coreInterceptedLine : CalendrumKtorBase

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
- (void)setParameterName:(NSString *)name values:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("setParameter(name:values:)")));

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
@interface CalendrumKtorCalendrum_coreParseLimitsCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default limits and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreParseLimitsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a new builder that starts from the default limits. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Generous limits for files you trust: lines up to 8 MiB, 10 million lines, 1,000 reported problems, nesting
 * depth 16, one million components, no limit on the input size and no time budget.
 */
@property (readonly) CalendrumKtorCalendrum_coreParseLimits *Default __attribute__((swift_name("Default")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface CalendrumKtorKotlinEnumCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
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
@interface CalendrumKtorCalendrum_coreProblem : CalendrumKtorBase

/**
 * Returns the code, severity, message and position, such as `ICAL-S003 Warning: bare LF used as a line ending at
 * 3:7 (offset 12)`. It never contains values from the input.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The stable code of the problem, such as `ICAL-S003`. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *code __attribute__((swift_name("code")));

/**
 * A short human-readable text: the fixed message of the code, followed by an explanation after a colon when the
 * code has one (for example the name of a detected charset). It never contains data from the input.
 */
@property (readonly) NSString *message __attribute__((swift_name("message")));

/** The severity of [code]. */
@property (readonly) CalendrumKtorCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));

/**
 * Where in the input the problem was found; a point, so [SourceSpan.startOffset] equals [SourceSpan.endOffset].
 *
 * Nodes built by hand or edited by an interceptor have no position in the original input; see [SourceSpan].
 */
@property (readonly) CalendrumKtorCalendrum_coreSourceSpan *span __attribute__((swift_name("span")));
@end


/** The default registry and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalRegistry.Companion")))
@interface CalendrumKtorCalendrum_coreICalRegistryCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default registry and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreICalRegistryCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder; install [Rfc5545] and the other extensions you need.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumKtorCalendrum_coreICalRegistryBuilder *)builder __attribute__((swift_name("builder()")));

/** The registry with RFC 5545, RFC 7986, RFC 9073, RFC 9074 and RFC 9253; created on first use. */
@property (readonly) CalendrumKtorCalendrum_coreICalRegistry *Default __attribute__((swift_name("Default")));
@end


/**
 * What the registry knows about a component. Immutable.
 *
 * @property source the standard or vendor that defines it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreComponentDefinition")))
@interface CalendrumKtorCalendrum_coreComponentDefinition : CalendrumKtorBase
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
@interface CalendrumKtorCalendrum_coreParameterDefinition : CalendrumKtorBase
- (instancetype)initWithKey:(CalendrumKtorCalendrum_coreParameterKey<id> *)key source:(NSString *)source __attribute__((swift_name("init(key:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreParameterKey<id> *key __attribute__((swift_name("key")));

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
@interface CalendrumKtorCalendrum_corePropertyDefinition : CalendrumKtorBase
- (instancetype)initWithKey:(CalendrumKtorCalendrum_corePropertyKey<id> *)key defaultType:(CalendrumKtorCalendrum_coreValueType *)defaultType allowedTypes:(NSSet<CalendrumKtorCalendrum_coreValueType *> *)allowedTypes parameters:(NSSet<NSString *> *)parameters cardinality:(NSDictionary<NSString *, CalendrumKtorCalendrum_coreCardinality *> *)cardinality source:(NSString *)source __attribute__((swift_name("init(key:defaultType:allowedTypes:parameters:cardinality:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The value types the property may have, including the default one. */
@property (readonly) NSSet<CalendrumKtorCalendrum_coreValueType *> *allowedTypes __attribute__((swift_name("allowedTypes")));

/** How often the property occurs in each component, by component name. */
@property (readonly) NSDictionary<NSString *, CalendrumKtorCalendrum_coreCardinality *> *cardinality __attribute__((swift_name("cardinality")));
@property (readonly) CalendrumKtorCalendrum_coreValueType *defaultType __attribute__((swift_name("defaultType")));
@property (readonly) CalendrumKtorCalendrum_corePropertyKey<id> *key __attribute__((swift_name("key")));

/** The canonical name of the property. */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The names of the parameters that apply to the property. */
@property (readonly) NSSet<NSString *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@end


/** A set of definitions that can be installed into a registry, such as a standard or a vendor's properties. */
__attribute__((swift_name("Calendrum_coreICalExtension")))
@protocol CalendrumKtorCalendrum_coreICalExtension
@required

/** Adds this extension's definitions to [registry].
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (void)contributeRegistry:(CalendrumKtorCalendrum_coreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));
@end


/** The presets and the builder of selections. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSelection.Companion")))
@interface CalendrumKtorCalendrum_coreSelectionCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The presets and the builder of selections. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreSelectionCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumKtorCalendrum_coreSelectionBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * What a free/busy view needs: `VFREEBUSY` whole, and `VEVENT` with `UID`, `TRANSP`, `STATUS` and the
 * occurrence properties, with the time zones they use.
 */
@property (readonly) CalendrumKtorCalendrum_coreSelection *FreeBusy __attribute__((swift_name("FreeBusy")));

/** Reads everything. This is the default of a parser. */
@property (readonly) CalendrumKtorCalendrum_coreSelection *Full __attribute__((swift_name("Full")));

/**
 * A calendar view of events: `VEVENT` with `UID`, `SUMMARY`, `DESCRIPTION`, `LOCATION`, `STATUS`, `SEQUENCE`,
 * `CREATED`, `LAST-MODIFIED`, the occurrence properties and the participants, `X-WR-TIMEZONE` of the calendar,
 * and the time zones the events use.
 */
@property (readonly) CalendrumKtorCalendrum_coreSelection *Schedule __attribute__((swift_name("Schedule")));

/**
 * What a sync needs to tell versions apart: every component with `UID`, `SEQUENCE`, `LAST-MODIFIED`, `DTSTAMP`
 * and `RECURRENCE-ID`, and no time zones.
 */
@property (readonly) CalendrumKtorCalendrum_coreSelection *SyncMetadata __attribute__((swift_name("SyncMetadata")));
@end


/** Which `VTIMEZONE` components a selective parse keeps. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTimeZoneSelection")))
@interface CalendrumKtorCalendrum_coreTimeZoneSelection : CalendrumKtorKotlinEnum<CalendrumKtorCalendrum_coreTimeZoneSelection *>
+ (instancetype)alloc __attribute__((unavailable));

/** Which `VTIMEZONE` components a selective parse keeps. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorCalendrum_coreTimeZoneSelection *all __attribute__((swift_name("all")));
@property (class, readonly) CalendrumKtorCalendrum_coreTimeZoneSelection *referenced __attribute__((swift_name("referenced")));
@property (class, readonly) CalendrumKtorCalendrum_coreTimeZoneSelection *none __attribute__((swift_name("none")));
+ (CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreTimeZoneSelection *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorCalendrum_coreTimeZoneSelection *> *entries __attribute__((swift_name("entries")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.9")
*/
__attribute__((swift_name("KotlinTimeMark")))
@protocol CalendrumKtorKotlinTimeMark
@required
- (int64_t)elapsedNow __attribute__((swift_name("elapsedNow()")));
- (BOOL)hasNotPassedNow __attribute__((swift_name("hasNotPassedNow()")));
- (BOOL)hasPassedNow __attribute__((swift_name("hasPassedNow()")));
- (id<CalendrumKtorKotlinTimeMark>)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (id<CalendrumKtorKotlinTimeMark>)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
@end

__attribute__((swift_name("KotlinByteIterator")))
@interface CalendrumKtorKotlinByteIterator : CalendrumKtorBase <CalendrumKtorKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumKtorByte *)next __attribute__((swift_name("next()")));
- (int8_t)nextByte __attribute__((swift_name("nextByte()")));
@end


/** A declared field of a schema, to read its value from [Values] in [IcsSchema.create]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreIcsSchemaField")))
@interface CalendrumKtorCalendrum_coreIcsSchemaField<R> : CalendrumKtorBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** The decoded values of one component, handed to [IcsSchema.create]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreIcsSchemaValues")))
@interface CalendrumKtorCalendrum_coreIcsSchemaValues : CalendrumKtorBase

/**
 * Returns the value of [field]: a malformed or missing optional value as `null`, a list field as a list (empty
 * if there is no property).
 *
 * @param field a field of the schema that decodes these values.
 * @throws IllegalArgumentException if [field] belongs to another schema.
 */
- (id _Nullable)getField:(CalendrumKtorCalendrum_coreIcsSchemaField<id> *)field __attribute__((swift_name("get(field:)")));

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
@interface CalendrumKtorCalendrum_corePropertyKey<T> : CalendrumKtorBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumKtorCalendrum_corePropertyCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorCalendrum_corePropertyCodec> codec __attribute__((swift_name("codec")));

/** The canonical property name in upper case. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/** Creates VCalendar values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVCalendar.Companion")))
@interface CalendrumKtorCalendrum_coreVCalendarCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VCalendar values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVCalendarCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVCalendarBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VCalendar, or returns `null` when it is not a `VCALENDAR` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVCalendar * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreVEvent : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVEventCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this event changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this event.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVEvent *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVEventBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VEvent with an equal VEVENT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this event; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this event and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the event, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the event (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the event (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumKtorCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreConference *> *conferences __attribute__((swift_name("conferences")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) CalendrumKtorCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event (`DTEND`, [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)), or
 * `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The excluded dates of the event (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the event (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the event (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the event (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the event (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/**
 * The location text of the event (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the event, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the event, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the event (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the event (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the event (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the event, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the event (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the event (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@property (readonly) CalendrumKtorCalendrum_coreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

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
@interface CalendrumKtorCalendrum_coreVFreeBusy : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVFreeBusyCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this free/busy component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this free/busy component.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVFreeBusy *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVFreeBusyBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VFreeBusy with an equal VFREEBUSY component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this free/busy component; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this free/busy component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attendees of the free/busy component (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The comments of the free/busy component (`COMMENT` properties,
 * [RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The free/busy component nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreFreeBusy *> *freeBusy __attribute__((swift_name("freeBusy")));

/**
 * The links of the free/busy component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the free/busy component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the free/busy component, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The styled descriptions of the free/busy component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@interface CalendrumKtorCalendrum_coreImage : CalendrumKtorBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumKtorKotlinByteArray *)bytes formatType:(NSString *)formatType __attribute__((swift_name("init(bytes:formatType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri formatType:(NSString *)formatType __attribute__((swift_name("init(uri:formatType:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the inline content, or `null` when the image is given by a URI. */
- (CalendrumKtorKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a Image equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable altRep __attribute__((swift_name("altRep")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreImageDisplay *> *display __attribute__((swift_name("display")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the image is inline (base64 encoded in the text form) and not given by a URI. */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumKtorCalendrum_coreVJournal : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVJournalCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this journal entry changed by [block]. Properties and components that [block] does not touch
 * are the same objects as in this journal entry.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVJournal *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVJournalBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VJournal with an equal VJOURNAL component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this journal entry; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this journal entry and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the journal entry (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the journal entry (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumKtorCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The images of the journal entry (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the journal entry (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the journal entry (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The additional dates of the journal entry (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the journal entry (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the journal entry (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the journal entry, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreJournalStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the journal entry (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the journal entry (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@protocol CalendrumKtorCalendrum_coreICalDateTime
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
@interface CalendrumKtorCalendrum_coreMethod : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreMethodCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreICalDuration : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreICalDurationCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a duration with the same sign, amounts and written units. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the sign, the amounts and the written units. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the duration as a kotlinx-datetime period with the sign applied to every unit; weeks become days (7
 * each).
 */
- (CalendrumKtorKotlinx_datetimeDateTimePeriod *)toDateTimePeriod __attribute__((swift_name("toDateTimePeriod()")));

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
@interface CalendrumKtorCalendrum_coreVTimeZone : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVTimeZoneCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this time zone changed by [block]. Properties and components that [block] does not touch are
 * the same objects as in this time zone.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVTimeZone *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVTimeZoneBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTimeZone with an equal VTIMEZONE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this time zone; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this time zone and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the time zone (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The `STANDARD` and `DAYLIGHT` observances in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

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
@interface CalendrumKtorCalendrum_coreVTodo : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVTodoCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this to-do changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this to-do.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVTodo *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVTodoBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VTodo with an equal VTODO component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this to-do; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this to-do and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The alarms nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attachments of the to-do (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the to-do (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property (readonly) CalendrumKtorCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreConference *> *conferences __attribute__((swift_name("conferences")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The excluded dates of the to-do (`EXDATE` properties,
 * [RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *exdates __attribute__((swift_name("exdates")));

/**
 * The geographic position of the to-do (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The images of the to-do (`IMAGE` properties,
 * [RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)) in source order; ones that do not decode
 * are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreImage *> *images __attribute__((swift_name("images")));

/**
 * The time of the last modification of the to-do (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the to-do (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The location nested in the to-do, in source order. */
@property (readonly) NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The additional dates of the to-do (`RDATE` properties,
 * [RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)) from all properties in source order;
 * ones that do not decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The recurrence identifier of the to-do (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the to-do (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the to-do, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do (`DTSTART`, [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreTodoStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the to-do (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the to-do (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@interface CalendrumKtorCalendrum_coreICalProperty : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreICalPropertyCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a property with the same name, parameters and value. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name, the parameters and the value. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the first parameter named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a parameter name such as `TZID`.
 */
- (CalendrumKtorCalendrum_coreICalParameter * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/** Returns the name and the number of parameters; never the value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The canonical name in upper case, such as `DTSTART`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The parameters in source order. The list is empty for none. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));

/**
 * Where the property is in the input, or `null` for a property that was built by hand. For input that was changed
 * by an interceptor it is the position of the original line.
 */
@property (readonly) CalendrumKtorCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));

/**
 * The value as written after the colon, with folding removed. It is not unescaped; an empty string for no value.
 */
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/**
 * Collects the properties and nested components of a new or edited event, with a Kotlin DSL and chained calls. Get
 * one from [VEvent.builder] or [VEvent.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVEvent.Builder")))
@interface CalendrumKtorCalendrum_coreVEventBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVEventBuilder *>

/** Adds [alarm] as a alarm of the event. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)alarmAlarm:(CalendrumKtorCalendrum_coreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)alarmBlock:(void (^)(CalendrumKtorCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the event in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)alarmsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)attachAttach:(id<CalendrumKtorCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)attendeeAttendee:(CalendrumKtorCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)attendeesBlock:(void (^)(CalendrumKtorCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the event.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults to generate
 *   them.
 * @throws IllegalStateException if `UNTIL` of the rule does not suit the type of `DTSTART`.
 */
- (CalendrumKtorCalendrum_coreVEvent *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)categoriesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)classificationValue:(CalendrumKtorCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)conferenceValue:(CalendrumKtorCalendrum_coreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)createdValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)durationValue:(CalendrumKtorCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)endValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)exdateDates:(CalendrumKtorKotlinArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)geoValue:(CalendrumKtorCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)imageValue:(CalendrumKtorCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)lastModifiedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the event. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)locationValue:(CalendrumKtorCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)locationBlock:(void (^)(CalendrumKtorCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the event in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)locationsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)organizerValue:(CalendrumKtorCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the event. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)participantValue:(CalendrumKtorCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)participantBlock:(void (^)(CalendrumKtorCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the event in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)participantsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)priorityValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)rdateDates:(CalendrumKtorKotlinArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)rdatePeriods:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)recurrenceIdValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)requestStatusStatus:(CalendrumKtorCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the event. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)resourceComponentValue:(CalendrumKtorCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)resourceComponentBlock:(void (^)(CalendrumKtorCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the event in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)resourceComponentsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)resourcesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)rruleValue:(CalendrumKtorCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)rruleFrequency:(CalendrumKtorCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumKtorCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)sequenceValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)stampValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)startValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)statusValue:(CalendrumKtorCalendrum_coreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)structuredDataValue:(CalendrumKtorCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumKtorCalendrum_coreVEventBuilder *)styledDescriptionValue:(CalendrumKtorCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [transparency] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)transparencyValue:(CalendrumKtorCalendrum_coreTransparency * _Nullable)value __attribute__((swift_name("transparency(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the event being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the event being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property CalendrumKtorCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The end of the event being built (`DTEND`,
 * [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/**
 * The geographic position of the event being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the event being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the event being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the event being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the event being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the event being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The priority of the event being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the event being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the event being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the event being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the event being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the event being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the event being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the event being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

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
@property CalendrumKtorCalendrum_coreTransparency * _Nullable transparency __attribute__((swift_name("transparency")));

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
@interface CalendrumKtorCalendrum_coreComponentList<T> : CalendrumKtorBase

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
@interface CalendrumKtorCalendrum_coreVFreeBusyBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVFreeBusyBuilder *>

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)attendeeAttendee:(CalendrumKtorCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)attendeesBlock:(void (^)(CalendrumKtorCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the free/busy component.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults.
 */
- (CalendrumKtorCalendrum_coreVFreeBusy *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [contact] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)contactValue:(NSString * _Nullable)value __attribute__((swift_name("contact(value:)")));

/**
 * Sets [end] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)endValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("end(value:)")));

/** Adds [freeBusy] as a free/busy component of the free/busy component. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)freeBusyFreeBusy:(CalendrumKtorCalendrum_coreFreeBusy *)freeBusy __attribute__((swift_name("freeBusy(freeBusy:)")));

/** Adds [periods] as a free/busy component of the free/busy component. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)freeBusyPeriods:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("freeBusy(periods:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the free/busy component. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)locationValue:(CalendrumKtorCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)locationBlock:(void (^)(CalendrumKtorCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)locationsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)organizerValue:(CalendrumKtorCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the free/busy component. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)participantValue:(CalendrumKtorCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)participantBlock:(void (^)(CalendrumKtorCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)participantsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)requestStatusStatus:(CalendrumKtorCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the free/busy component. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)resourceComponentValue:(CalendrumKtorCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)resourceComponentBlock:(void (^)(CalendrumKtorCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the free/busy component in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)resourceComponentsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)stampValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)startValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)styledDescriptionValue:(CalendrumKtorCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));

/** The locations of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the free/busy component being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/** The resources of the free/busy component being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The time stamp of the free/busy component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the free/busy component being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

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
@interface CalendrumKtorCalendrum_coreVJournalBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVJournalBuilder *>

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)attachAttach:(id<CalendrumKtorCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)attendeeAttendee:(CalendrumKtorCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)attendeesBlock:(void (^)(CalendrumKtorCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the journal entry.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumKtorCalendrum_coreVJournal *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)categoriesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)classificationValue:(CalendrumKtorCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)createdValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/** Adds a `DESCRIPTION` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)descriptionText:(NSString *)text __attribute__((swift_name("description(text:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)exdateDates:(CalendrumKtorKotlinArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)imageValue:(CalendrumKtorCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)lastModifiedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the journal entry. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)locationValue:(CalendrumKtorCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)locationBlock:(void (^)(CalendrumKtorCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the journal entry in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)locationsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)organizerValue:(CalendrumKtorCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the journal entry. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)participantValue:(CalendrumKtorCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)participantBlock:(void (^)(CalendrumKtorCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the journal entry in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)participantsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)rdateDates:(CalendrumKtorKotlinArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)rdatePeriods:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)recurrenceIdValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)requestStatusStatus:(CalendrumKtorCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the journal entry. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)resourceComponentValue:(CalendrumKtorCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)resourceComponentBlock:(void (^)(CalendrumKtorCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the journal entry in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)resourceComponentsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)rruleValue:(CalendrumKtorCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)rruleFrequency:(CalendrumKtorCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumKtorCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)sequenceValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)stampValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)startValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)statusValue:(CalendrumKtorCalendrum_coreJournalStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)structuredDataValue:(CalendrumKtorCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)styledDescriptionValue:(CalendrumKtorCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the journal entry being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

/**
 * The time of the last modification of the journal entry being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the journal entry being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the journal entry being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the journal entry being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The recurrence identifier of the journal entry being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the journal entry being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the journal entry being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the journal entry being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the journal entry being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the journal entry being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the journal entry being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreJournalStatus * _Nullable status __attribute__((swift_name("status")));

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
@interface CalendrumKtorCalendrum_coreVTimeZoneBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVTimeZoneBuilder *>

/** Creates the time zone. */
- (CalendrumKtorCalendrum_coreVTimeZone *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `DAYLIGHT` observance that [block] sets up. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)daylightBlock:(void (^)(CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("daylight(block:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)lastModifiedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [observance] to the time zone. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)observanceObservance:(CalendrumKtorCalendrum_coreVTimeZoneObservance *)observance __attribute__((swift_name("observance(observance:)")));

/** Edits the observances in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)observancesBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVTimeZoneObservance *> *))block __attribute__((swift_name("observances(block:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds a `STANDARD` observance that [block] sets up. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)standardBlock:(void (^)(CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("standard(block:)")));

/**
 * Sets [tzUrl] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)tzUrlValue:(NSString * _Nullable)value __attribute__((swift_name("tzUrl(value:)")));

/**
 * Sets [tzid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)tzidValue:(NSString * _Nullable)value __attribute__((swift_name("tzid(value:)")));

/**
 * The time of the last modification of the time zone being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The observances of the time zone being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVTimeZoneObservance *> *observances __attribute__((swift_name("observances")));

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
@interface CalendrumKtorCalendrum_coreVTodoBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVTodoBuilder *>

/** Adds [alarm] as a alarm of the to-do. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)alarmAlarm:(CalendrumKtorCalendrum_coreVAlarm *)alarm __attribute__((swift_name("alarm(alarm:)")));

/**
 * Adds a alarm that [block] sets up.
 *
 * @param block sets up the new alarm.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)alarmBlock:(void (^)(CalendrumKtorCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("alarm(block:)")));

/** Edits the alarms of the to-do in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)alarmsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVAlarm *> *))block __attribute__((swift_name("alarms(block:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)attachAttach:(id<CalendrumKtorCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)attendeeAttendee:(CalendrumKtorCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)attendeesBlock:(void (^)(CalendrumKtorCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Creates the to-do.
 *
 * @throws IllegalStateException if `UID` or `DTSTAMP` is missing and the builder has no defaults, or if `UNTIL`
 *   of the `RRULE` does not suit the type of `DTSTART`.
 */
- (CalendrumKtorCalendrum_coreVTodo *)build __attribute__((swift_name("build()")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)categoriesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/**
 * Sets [classification] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)classificationValue:(CalendrumKtorCalendrum_coreClassification * _Nullable)value __attribute__((swift_name("classification(value:)")));

/**
 * Sets [color] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)colorValue:(NSString * _Nullable)value __attribute__((swift_name("color(value:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/**
 * Sets [completed] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)completedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("completed(value:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `CONFERENCE` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)conferenceValue:(CalendrumKtorCalendrum_coreConference *)value __attribute__((swift_name("conference(value:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)createdValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [due] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)dueValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("due(value:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)durationValue:(CalendrumKtorCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/** Adds an `EXDATE` property with the given [dates]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)exdateDates:(CalendrumKtorKotlinArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("exdate(dates:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)geoValue:(CalendrumKtorCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as an `IMAGE` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)imageValue:(CalendrumKtorCalendrum_coreImage *)value __attribute__((swift_name("image(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)lastModifiedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the to-do. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)locationValue:(CalendrumKtorCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)locationBlock:(void (^)(CalendrumKtorCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/**
 * Sets [location] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)locationText:(NSString * _Nullable)text __attribute__((swift_name("location(text:)")));

/** Edits the locations of the to-do in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)locationsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [organizer] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)organizerValue:(CalendrumKtorCalendrum_coreOrganizer * _Nullable)value __attribute__((swift_name("organizer(value:)")));

/** Sets the organizer to the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)organizerAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreOrganizerBuilder *))block __attribute__((swift_name("organizer(address:block:)")));

/** Adds [value] as a participant of the to-do. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)participantValue:(CalendrumKtorCalendrum_coreParticipant *)value __attribute__((swift_name("participant(value:)")));

/**
 * Adds a participant that [block] sets up.
 *
 * @param block sets up the new participant.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)participantBlock:(void (^)(CalendrumKtorCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("participant(block:)")));

/** Edits the participants of the to-do in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)participantsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *))block __attribute__((swift_name("participants(block:)")));

/**
 * Sets [percentComplete] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)percentCompleteValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("percentComplete(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)priorityValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)rdateDates:(CalendrumKtorKotlinArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)rdatePeriods:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/**
 * Sets [recurrenceId] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)recurrenceIdValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("recurrenceId(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [status] as a `REQUEST-STATUS` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)requestStatusStatus:(CalendrumKtorCalendrum_coreRequestStatus *)status __attribute__((swift_name("requestStatus(status:)")));

/** Adds [value] as a resource of the to-do. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)resourceComponentValue:(CalendrumKtorCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)resourceComponentBlock:(void (^)(CalendrumKtorCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the to-do in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)resourceComponentsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)resourcesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [rrule] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)rruleValue:(CalendrumKtorCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)rruleFrequency:(CalendrumKtorCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumKtorCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)sequenceValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)stampValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [start] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)startValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)statusValue:(CalendrumKtorCalendrum_coreTodoStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)structuredDataValue:(CalendrumKtorCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)styledDescriptionValue:(CalendrumKtorCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

/** The alarms of the to-do being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVAlarm *> *alarms __attribute__((swift_name("alarms")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

/**
 * The access classification of the to-do being built (`CLASS`,
 * [RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreClassification * _Nullable classification __attribute__((swift_name("classification")));

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
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable completed __attribute__((swift_name("completed")));

/**
 * The creation time of the to-do being built (`CREATED`,
 * [RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable due __attribute__((swift_name("due")));

/**
 * The duration of the to-do being built (`DURATION`,
 * [RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The geographic position of the to-do being built (`GEO`,
 * [RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the to-do being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The location text of the to-do being built (`LOCATION`,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)); assigning `null` removes the
 * property.
 */
@property NSString * _Nullable location __attribute__((swift_name("location")));

/** The locations of the to-do being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The organizer of the to-do being built (`ORGANIZER`,
 * [RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreOrganizer * _Nullable organizer __attribute__((swift_name("organizer")));

/** The participants of the to-do being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreParticipant *> *participants __attribute__((swift_name("participants")));

/**
 * The percentage complete of the to-do being built (`PERCENT-COMPLETE`,
 * [RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable percentComplete __attribute__((swift_name("percentComplete")));

/**
 * The priority of the to-do being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable priority __attribute__((swift_name("priority")));

/**
 * The recurrence identifier of the to-do being built (`RECURRENCE-ID`,
 * [RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable recurrenceId __attribute__((swift_name("recurrenceId")));

/** The resources of the to-do being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The recurrence rule of the to-do being built (`RRULE`,
 * [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The revision sequence number of the to-do being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the to-do being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The start of the to-do being built (`DTSTART`,
 * [RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));

/**
 * The status of the to-do being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreTodoStatus * _Nullable status __attribute__((swift_name("status")));

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
@protocol CalendrumKtorKotlinClock
@required
- (CalendrumKtorKotlinInstant *)now __attribute__((swift_name("now()")));
@end


/** Makes the `UID` values of new components. There is no global generator: you pass one in [BuildDefaults]. */
__attribute__((swift_name("Calendrum_coreUidGenerator")))
@protocol CalendrumKtorCalendrum_coreUidGenerator
@required

/** Returns a new `UID`; it must be globally unique. */
- (NSString *)next __attribute__((swift_name("next()")));
@end


/** Creates builders for [ICalComponent]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalComponent.Companion")))
@interface CalendrumKtorCalendrum_coreICalComponentCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [ICalComponent]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreICalComponentCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a component named [name].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @throws IllegalArgumentException if [name] is empty or has another character.
 */
- (CalendrumKtorCalendrum_coreICalComponentBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));
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
@interface CalendrumKtorCalendrum_coreSourceSpan : CalendrumKtorBase

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

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreRoutingResolveContext")))
@interface CalendrumKtorKtor_server_coreRoutingResolveContext : CalendrumKtorBase
- (instancetype)initWithRouting:(CalendrumKtorKtor_server_coreRoutingNode *)routing call:(id<CalendrumKtorKtor_server_corePipelineCall>)call tracers:(NSArray<CalendrumKtorKotlinUnit *(^)(CalendrumKtorKtor_server_coreRoutingResolveTrace *)> *)tracers __attribute__((swift_name("init(routing:call:tracers:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)resolveWithCompletionHandler:(void (^)(CalendrumKtorKtor_server_coreRoutingResolveResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("resolve(completionHandler:)")));
@property (readonly) id<CalendrumKtorKtor_server_corePipelineCall> call __attribute__((swift_name("call")));
@property (readonly) BOOL hasTrailingSlash __attribute__((swift_name("hasTrailingSlash")));
@property (readonly) CalendrumKtorKtor_server_coreRoutingNode *routing __attribute__((swift_name("routing")));
@property (readonly) NSArray<NSString *> *segments __attribute__((swift_name("segments")));
@end

__attribute__((swift_name("Ktor_server_coreRouteSelectorEvaluation")))
@interface CalendrumKtorKtor_server_coreRouteSelectorEvaluation : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorKtor_server_coreRouteSelectorEvaluationCompanion *companion __attribute__((swift_name("companion")));
@property (readonly) BOOL succeeded __attribute__((swift_name("succeeded")));
@end

__attribute__((swift_name("Ktor_utilsPipeline")))
@interface CalendrumKtorKtor_utilsPipeline<TSubject, TContext> : CalendrumKtorBase
- (instancetype)initWithPhases:(CalendrumKtorKotlinArray<CalendrumKtorKtor_utilsPipelinePhase *> *)phases __attribute__((swift_name("init(phases:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithPhase:(CalendrumKtorKtor_utilsPipelinePhase *)phase interceptors:(NSArray<id<CalendrumKtorKotlinSuspendFunction2>> *)interceptors __attribute__((swift_name("init(phase:interceptors:)"))) __attribute__((objc_designated_initializer));
- (void)addPhasePhase:(CalendrumKtorKtor_utilsPipelinePhase *)phase __attribute__((swift_name("addPhase(phase:)")));
- (void)afterIntercepted __attribute__((swift_name("afterIntercepted()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)executeContext:(TContext)context subject:(TSubject)subject completionHandler:(void (^)(TSubject _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("execute(context:subject:completionHandler:)")));
- (void)insertPhaseAfterReference:(CalendrumKtorKtor_utilsPipelinePhase *)reference phase:(CalendrumKtorKtor_utilsPipelinePhase *)phase __attribute__((swift_name("insertPhaseAfter(reference:phase:)")));
- (void)insertPhaseBeforeReference:(CalendrumKtorKtor_utilsPipelinePhase *)reference phase:(CalendrumKtorKtor_utilsPipelinePhase *)phase __attribute__((swift_name("insertPhaseBefore(reference:phase:)")));
- (void)interceptPhase:(CalendrumKtorKtor_utilsPipelinePhase *)phase block:(id<CalendrumKtorKotlinSuspendFunction2>)block __attribute__((swift_name("intercept(phase:block:)")));
- (NSArray<id<CalendrumKtorKotlinSuspendFunction2>> *)interceptorsForPhasePhase:(CalendrumKtorKtor_utilsPipelinePhase *)phase __attribute__((swift_name("interceptorsForPhase(phase:)")));
- (void)mergeFrom:(CalendrumKtorKtor_utilsPipeline<TSubject, TContext> *)from __attribute__((swift_name("merge(from:)")));
- (void)mergePhasesFrom:(CalendrumKtorKtor_utilsPipeline<TSubject, TContext> *)from __attribute__((swift_name("mergePhases(from:)")));
- (void)resetFromFrom:(CalendrumKtorKtor_utilsPipeline<TSubject, TContext> *)from __attribute__((swift_name("resetFrom(from:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorKtor_utilsAttributes> attributes __attribute__((swift_name("attributes")));
@property (readonly) BOOL developmentMode __attribute__((swift_name("developmentMode")));
@property (readonly, getter=isEmpty_) BOOL isEmpty __attribute__((swift_name("isEmpty")));
@property (readonly) NSArray<CalendrumKtorKtor_utilsPipelinePhase *> *items __attribute__((swift_name("items")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationConfig")))
@protocol CalendrumKtorKtor_server_coreApplicationConfig
@required
- (id<CalendrumKtorKtor_server_coreApplicationConfig>)configPath:(NSString *)path __attribute__((swift_name("config(path:)")));
- (NSArray<id<CalendrumKtorKtor_server_coreApplicationConfig>> *)configListPath:(NSString *)path __attribute__((swift_name("configList(path:)")));
- (NSSet<NSString *> *)keys __attribute__((swift_name("keys()")));
- (id<CalendrumKtorKtor_server_coreApplicationConfigValue>)propertyPath:(NSString *)path __attribute__((swift_name("property(path:)")));
- (id<CalendrumKtorKtor_server_coreApplicationConfigValue> _Nullable)propertyOrNullPath:(NSString *)path __attribute__((swift_name("propertyOrNull(path:)")));
- (NSDictionary<NSString *, id> *)toMap __attribute__((swift_name("toMap()")));
@end

__attribute__((swift_name("Ktor_utilsLogger")))
@protocol CalendrumKtorKtor_utilsLogger
@required
- (void)debugMessage:(NSString *)message __attribute__((swift_name("debug(message:)")));
- (void)debugMessage:(NSString *)message cause:(CalendrumKtorKotlinThrowable *)cause __attribute__((swift_name("debug(message:cause:)")));
- (void)errorMessage:(NSString *)message __attribute__((swift_name("error(message:)")));
- (void)errorMessage:(NSString *)message cause:(CalendrumKtorKotlinThrowable *)cause __attribute__((swift_name("error(message:cause:)")));
- (void)infoMessage:(NSString *)message __attribute__((swift_name("info(message:)")));
- (void)infoMessage:(NSString *)message cause:(CalendrumKtorKotlinThrowable *)cause __attribute__((swift_name("info(message:cause:)")));
- (void)traceMessage:(NSString *)message __attribute__((swift_name("trace(message:)")));
- (void)traceMessage:(NSString *)message cause:(CalendrumKtorKotlinThrowable *)cause __attribute__((swift_name("trace(message:cause:)")));
- (void)warnMessage:(NSString *)message __attribute__((swift_name("warn(message:)")));
- (void)warnMessage:(NSString *)message cause:(CalendrumKtorKotlinThrowable *)cause __attribute__((swift_name("warn(message:cause:)")));
@property (readonly) CalendrumKtorKtor_utilsLogLevel *level __attribute__((swift_name("level")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_eventsEvents")))
@interface CalendrumKtorKtor_eventsEvents : CalendrumKtorBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)raiseDefinition:(CalendrumKtorKtor_eventsEventDefinition<id> *)definition value:(id _Nullable)value __attribute__((swift_name("raise(definition:value:)")));
- (id<CalendrumKtorKotlinx_coroutines_coreDisposableHandle>)subscribeDefinition:(CalendrumKtorKtor_eventsEventDefinition<id> *)definition handler:(void (^)(id _Nullable))handler __attribute__((swift_name("subscribe(definition:handler:)")));
- (void)unsubscribeDefinition:(CalendrumKtorKtor_eventsEventDefinition<id> *)definition handler:(void (^)(id _Nullable))handler __attribute__((swift_name("unsubscribe(definition:handler:)")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinKVariance")))
@interface CalendrumKtorKotlinKVariance : CalendrumKtorKotlinEnum<CalendrumKtorKotlinKVariance *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorKotlinKVariance *invariant __attribute__((swift_name("invariant")));
@property (class, readonly) CalendrumKtorKotlinKVariance *in __attribute__((swift_name("in")));
@property (class, readonly) CalendrumKtorKotlinKVariance *out __attribute__((swift_name("out")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKotlinKVariance *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKotlinKVariance *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinKTypeProjection.Companion")))
@interface CalendrumKtorKotlinKTypeProjectionCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKotlinKTypeProjectionCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKotlinKTypeProjection *)contravariantType:(id<CalendrumKtorKotlinKType>)type __attribute__((swift_name("contravariant(type:)")));
- (CalendrumKtorKotlinKTypeProjection *)covariantType:(id<CalendrumKtorKotlinKType>)type __attribute__((swift_name("covariant(type:)")));
- (CalendrumKtorKotlinKTypeProjection *)invariantType:(id<CalendrumKtorKotlinKType>)type __attribute__((swift_name("invariant(type:)")));
@property (readonly) CalendrumKtorKotlinKTypeProjection *STAR __attribute__((swift_name("STAR")));
@end


/**
 * Collects what a [Selection] reads. The properties and the functions of the same name do the same. Not
 * thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSelection.Builder")))
@interface CalendrumKtorCalendrum_coreSelectionBuilder : CalendrumKtorBase

/** Creates the selection. */
- (CalendrumKtorCalendrum_coreSelection *)build __attribute__((swift_name("build()")));

/**
 * Adds components by name. Names of components that the standard defines and unknown `X-` names are accepted;
 * time zone components are chosen with [timeZones] instead.
 *
 * @throws IllegalArgumentException if a name is invalid, is not a component name, or names a time zone
 *   component.
 */
- (CalendrumKtorCalendrum_coreSelectionBuilder *)componentsNames:(CalendrumKtorKotlinArray<NSString *> *)names __attribute__((swift_name("components(names:)")));

/** Adds what the [features] need. */
- (CalendrumKtorCalendrum_coreSelectionBuilder *)needFeatures:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreFeature *> *)features __attribute__((swift_name("need(features:)")));

/** Adds the properties that the typed [keys] stand for. */
- (CalendrumKtorCalendrum_coreSelectionBuilder *)propertiesKeys:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_corePropertyKey<id> *> *)keys __attribute__((swift_name("properties(keys:)")));

/**
 * Adds properties by name, which are read in every selected component. Unknown names are matched exactly
 * (ignoring ASCII case).
 *
 * @throws IllegalArgumentException if a name is invalid, is not a property name, or is `BEGIN` or `END`.
 */
- (CalendrumKtorCalendrum_coreSelectionBuilder *)propertiesNames:(CalendrumKtorKotlinArray<NSString *> *)names __attribute__((swift_name("properties(names:)")));

/** Sets [timeZones]. */
- (CalendrumKtorCalendrum_coreSelectionBuilder *)timeZonesSelection:(CalendrumKtorCalendrum_coreTimeZoneSelection *)selection __attribute__((swift_name("timeZones(selection:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** Which `VTIMEZONE` components to keep; [TimeZoneSelection.All] by default. */
@property CalendrumKtorCalendrum_coreTimeZoneSelection *timeZones __attribute__((swift_name("timeZones")));
@end


/**
 * Collects [ParseLimits] values. The properties and the functions of the same name do the same; the functions
 * chain.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParseLimits.Builder")))
@interface CalendrumKtorCalendrum_coreParseLimitsBuilder : CalendrumKtorBase

/**
 * Creates the limits.
 *
 * @throws IllegalArgumentException if a bound is not positive (or, for [maxProblems], negative), or the time is
 *   not positive.
 */
- (CalendrumKtorCalendrum_coreParseLimits *)build __attribute__((swift_name("build()")));

/** Sets [maxComponents] to [value]. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)maxComponentsValue:(int32_t)value __attribute__((swift_name("maxComponents(value:)")));

/** Sets [maxDepth] to [value]. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)maxDepthValue:(int32_t)value __attribute__((swift_name("maxDepth(value:)")));

/** Sets [maxInputOctets] to [value]. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)maxInputOctetsValue:(int64_t)value __attribute__((swift_name("maxInputOctets(value:)")));

/** Sets [maxLineOctets] to [value]. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)maxLineOctetsValue:(int32_t)value __attribute__((swift_name("maxLineOctets(value:)")));

/** Sets [maxLines] to [value]. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)maxLinesValue:(int32_t)value __attribute__((swift_name("maxLines(value:)")));

/** Sets [maxProblems] to [value]. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)maxProblemsValue:(int32_t)value __attribute__((swift_name("maxProblems(value:)")));

/** Sets [maxTime] to [value]; `null` removes the budget. */
- (CalendrumKtorCalendrum_coreParseLimitsBuilder *)maxTimeValue:(id _Nullable)value __attribute__((swift_name("maxTime(value:)")));

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
@interface CalendrumKtorCalendrum_coreProblemCode : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreProblemCodeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a code with the same [id]. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the [id]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the [id], such as `ICAL-S003`. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The identifier of the code, for example `ICAL-S003`. */
@property (readonly) NSString *id __attribute__((swift_name("id")));

/** How serious the problems with this code are. */
@property (readonly) CalendrumKtorCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));
@end


/** How serious a [Problem] is. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSeverity")))
@interface CalendrumKtorCalendrum_coreSeverity : CalendrumKtorKotlinEnum<CalendrumKtorCalendrum_coreSeverity *>
+ (instancetype)alloc __attribute__((unavailable));

/** How serious a [Problem] is. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorCalendrum_coreSeverity *info __attribute__((swift_name("info")));
@property (class, readonly) CalendrumKtorCalendrum_coreSeverity *warning __attribute__((swift_name("warning")));
@property (class, readonly) CalendrumKtorCalendrum_coreSeverity *error __attribute__((swift_name("error")));
+ (CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreSeverity *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorCalendrum_coreSeverity *> *entries __attribute__((swift_name("entries")));
@end


/** Collects definitions and extensions into an [ICalRegistry]. Not thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalRegistry.Builder")))
@interface CalendrumKtorCalendrum_coreICalRegistryBuilder : CalendrumKtorBase

/** Creates the registry. */
- (CalendrumKtorCalendrum_coreICalRegistry *)build __attribute__((swift_name("build()")));

/** Adds a component definition; the children of a definition for the same name are merged. */
- (CalendrumKtorCalendrum_coreICalRegistryBuilder *)componentDefinition:(CalendrumKtorCalendrum_coreComponentDefinition *)definition __attribute__((swift_name("component(definition:)")));

/**
 * Installs [extension] once: a second install of the same extension does nothing. The extension adds its
 * definitions to this builder.
 */
- (CalendrumKtorCalendrum_coreICalRegistryBuilder *)installExtension:(id<CalendrumKtorCalendrum_coreICalExtension>)extension __attribute__((swift_name("install(extension:)")));

/** Adds a parameter definition, replacing a definition of another key for the same name. */
- (CalendrumKtorCalendrum_coreICalRegistryBuilder *)parameterDefinition:(CalendrumKtorCalendrum_coreParameterDefinition *)definition __attribute__((swift_name("parameter(definition:)")));

/**
 * Adds a property definition. A definition for a key that is already defined is merged into it; one for the
 * same name in other components is kept beside it.
 */
- (CalendrumKtorCalendrum_coreICalRegistryBuilder *)propertyDefinition:(CalendrumKtorCalendrum_corePropertyDefinition *)definition __attribute__((swift_name("property(definition:)")));

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
@interface CalendrumKtorCalendrum_coreParameterKey<T> : CalendrumKtorBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumKtorCalendrum_coreParameterCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorCalendrum_coreParameterCodec> codec __attribute__((swift_name("codec")));

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
@interface CalendrumKtorCalendrum_coreValueType : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreValueTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreCardinality : CalendrumKtorKotlinEnum<CalendrumKtorCalendrum_coreCardinality *>
+ (instancetype)alloc __attribute__((unavailable));

/** How many times a property may occur in a component. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorCalendrum_coreCardinality *one __attribute__((swift_name("one")));
@property (class, readonly) CalendrumKtorCalendrum_coreCardinality *optional __attribute__((swift_name("optional")));
@property (class, readonly) CalendrumKtorCalendrum_coreCardinality *many __attribute__((swift_name("many")));
@property (class, readonly) CalendrumKtorCalendrum_coreCardinality *oneormore __attribute__((swift_name("oneormore")));
+ (CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreCardinality *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorCalendrum_coreCardinality *> *entries __attribute__((swift_name("entries")));
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
@protocol CalendrumKtorCalendrum_corePropertyCodec
@required

/**
 * Decodes [property], with problems and strictness as [context] says.
 *
 * @param property the property to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumKtorCalendrum_coreDecoded>)decodeProperty:(CalendrumKtorCalendrum_coreICalProperty *)property context:(CalendrumKtorCalendrum_coreDecodeContext *)context __attribute__((swift_name("decode(property:context:)")));

/**
 * Encodes [value] into a property named by [context].
 *
 * @param value the value to write.
 * @param context carries the property name.
 */
- (CalendrumKtorCalendrum_coreICalProperty *)encodePropertyValue:(id)value context:(CalendrumKtorCalendrum_coreEncodeContext *)context __attribute__((swift_name("encodeProperty(value:context:)")));
@end


/** Creates VEvent values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVEvent.Companion")))
@interface CalendrumKtorCalendrum_coreVEventCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VEvent values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVEventCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVEventBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VEvent, or returns `null` when it is not a `VEVENT` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVEvent * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreVAlarm : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVAlarmCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this alarm changed by [block]. Properties and components that [block] does not touch are the
 * same objects as in this alarm.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVAlarm *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVAlarmBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VAlarm with an equal VALARM component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this alarm; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this alarm and takes generated `UID` and `DTSTAMP` values from [defaults]
 * where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The time the alarm was acknowledged of the alarm (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm (`ACTION`, [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)),
 * or `null` when it is missing or does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attachments of the alarm (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

/**
 * The attendees of the alarm (`ATTENDEE` properties,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumKtorCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/**
 * The links of the alarm (`LINK` properties, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2))
 * in source order; ones that do not decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/** The locations nested in the alarm, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The number of repetitions of the alarm (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable repeat __attribute__((swift_name("repeat")));

/**
 * The styled descriptions of the alarm (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

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
@protocol CalendrumKtorCalendrum_coreAttach
@required

/** The media type of the attachment (`FMTTYPE`), or `null`. */
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumKtorCalendrum_coreAttendee : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreAttendeeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Attendee equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this attendee, parameters included. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) CalendrumKtorCalendrum_coreCuType *cuType __attribute__((swift_name("cuType")));
@property (readonly) NSArray<NSString *> *delegatedFrom __attribute__((swift_name("delegatedFrom")));
@property (readonly) NSArray<NSString *> *delegatedTo __attribute__((swift_name("delegatedTo")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<NSString *> *member __attribute__((swift_name("member")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumKtorCalendrum_corePartStat *partStat __attribute__((swift_name("partStat")));
@property (readonly) CalendrumKtorCalendrum_coreRole *role __attribute__((swift_name("role")));
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
@interface CalendrumKtorCalendrum_coreClassification : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreClassificationCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreConference : CalendrumKtorBase
- (instancetype)initWithUri:(NSString *)uri __attribute__((swift_name("init(uri:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUri:(NSString *)uri features:(NSArray<CalendrumKtorCalendrum_coreConferenceFeature *> *)features label:(NSString * _Nullable)label __attribute__((swift_name("init(uri:features:label:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Conference equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreConferenceFeature *> *features __attribute__((swift_name("features")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumKtorCalendrum_coreGeo : CalendrumKtorBase
- (instancetype)initWithLatitude:(double)latitude longitude:(double)longitude __attribute__((swift_name("init(latitude:longitude:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a Geo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double latitude __attribute__((swift_name("latitude")));
@property (readonly) double longitude __attribute__((swift_name("longitude")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumKtorCalendrum_coreLink : CalendrumKtorBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumKtorCalendrum_coreValueType *)valueType linkRel:(NSString *)linkRel __attribute__((swift_name("init(value:valueType:linkRel:)"))) __attribute__((objc_designated_initializer));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumKtorCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
@interface CalendrumKtorCalendrum_coreVLocation : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVLocationCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this location component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this location component.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVLocation *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VLocation with an equal VLOCATION component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this location component; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this location component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the location component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The structured data of the location component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

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
@interface CalendrumKtorCalendrum_coreOrganizer : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreOrganizerCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a Organizer equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this organizer, parameters included. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable cn __attribute__((swift_name("cn")));
@property (readonly) NSString * _Nullable dir __attribute__((swift_name("dir")));
@property (readonly) NSString * _Nullable email __attribute__((swift_name("email")));
@property (readonly) NSString * _Nullable language __attribute__((swift_name("language")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumKtorCalendrum_coreParticipant : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreParticipantCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this participant component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this participant component.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreParticipant *)editBlock:(void (^)(CalendrumKtorCalendrum_coreParticipantBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a Participant with an equal PARTICIPANT component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this participant component; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this participant component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The attachments of the participant component (`ATTACH` properties,
 * [RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreAttach>> *attachments __attribute__((swift_name("attachments")));

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
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property (readonly) CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/**
 * The links of the participant component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/**
 * The location texts of the participant component (`LOCATION` properties,
 * [RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<NSString *> *locationTexts __attribute__((swift_name("locationTexts")));

/** The locations nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorInt * _Nullable priority __attribute__((swift_name("priority")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The request statuses of the participant component (`REQUEST-STATUS` properties,
 * [RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)) in source order; ones that do not
 * decode are skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRequestStatus *> *requestStatuses __attribute__((swift_name("requestStatuses")));

/** The resources nested in the participant component, in source order. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

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
@property (readonly) CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

/**
 * The structured data of the participant component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

/**
 * The styled descriptions of the participant component (`STYLED-DESCRIPTION` properties,
 * [RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStyledDescription *> *styledDescriptions __attribute__((swift_name("styledDescriptions")));

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
@interface CalendrumKtorCalendrum_coreRelatedTo : CalendrumKtorBase
- (instancetype)initWithUid:(NSString *)uid __attribute__((swift_name("init(uid:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumKtorCalendrum_coreRelType *)relType __attribute__((swift_name("init(uid:relType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithUid:(NSString *)uid relType:(CalendrumKtorCalendrum_coreRelType *)relType gap:(CalendrumKtorCalendrum_coreICalDuration *)gap __attribute__((swift_name("init(uid:relType:gap:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a RelatedTo equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreICalDuration * _Nullable gap __attribute__((swift_name("gap")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumKtorCalendrum_coreRelType *relType __attribute__((swift_name("relType")));
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
@interface CalendrumKtorCalendrum_coreRequestStatus : CalendrumKtorBase
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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
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
@interface CalendrumKtorCalendrum_coreVResource : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreVResourceCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns a copy of this resource component changed by [block]. Properties and components that [block] does not
 * touch are the same objects as in this resource component.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVResource *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is a VResource with an equal VRESOURCE component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this resource component; unchanged nodes are shared with it. */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/**
 * Returns a builder that starts from this resource component and takes generated `UID` and `DTSTAMP` values from
 * [defaults] where it creates components.
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)toBuilderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("toBuilder(defaults:)")));

/** Returns the type and the number of properties and components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

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
@property (readonly) CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The links of the resource component (`LINK` properties,
 * [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/**
 * The resource type of the resource component (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)), or `null` when it is missing or does not
 * decode.
 */
@property (readonly) CalendrumKtorCalendrum_coreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

/**
 * The structured data of the resource component (`STRUCTURED-DATA` properties,
 * [RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)) in source order; ones that do not decode are
 * skipped.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreStructuredData *> *structuredData __attribute__((swift_name("structuredData")));

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
@interface CalendrumKtorCalendrum_coreRecur : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreRecurCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a rule with the same parts in the same form. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over all parts. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the rule in iCalendar text form, such as `FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE`; unknown parts are left out.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The `BYDAY` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreWeekdayNum *> *byDay __attribute__((swift_name("byDay")));

/** The `BYHOUR` values, 0 to 23; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorInt *> *byHour __attribute__((swift_name("byHour")));

/** The `BYMINUTE` values, 0 to 59; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorInt *> *byMinute __attribute__((swift_name("byMinute")));

/** The `BYMONTH` entries; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreMonthNum *> *byMonth __attribute__((swift_name("byMonth")));

/** The `BYMONTHDAY` values, 1 to 31 or -31 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorInt *> *byMonthDay __attribute__((swift_name("byMonthDay")));

/** The `BYSECOND` values, 0 to 60; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorInt *> *bySecond __attribute__((swift_name("bySecond")));

/** The `BYSETPOS` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorInt *> *bySetPos __attribute__((swift_name("bySetPos")));

/** The `BYWEEKNO` values, 1 to 53 or -53 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorInt *> *byWeekNo __attribute__((swift_name("byWeekNo")));

/** The `BYYEARDAY` values, 1 to 366 or -366 to -1; empty when the part is absent. */
@property (readonly) NSArray<CalendrumKtorInt *> *byYearDay __attribute__((swift_name("byYearDay")));

/** The `COUNT`, or `null` when the rule has none. */
@property (readonly) CalendrumKtorInt * _Nullable count __attribute__((swift_name("count")));

/** The `FREQ` of the rule. */
@property (readonly) CalendrumKtorCalendrum_coreFrequency *frequency __attribute__((swift_name("frequency")));

/** The `INTERVAL`, 1 when the rule has none. */
@property (readonly) int32_t interval __attribute__((swift_name("interval")));

/**
 * The `RSCALE` calendar name of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529), or `null` for the Gregorian
 * calendar.
 */
@property (readonly) NSString * _Nullable rscale __attribute__((swift_name("rscale")));

/** The `SKIP` rule part, or `null` when the rule has none. */
@property (readonly) CalendrumKtorCalendrum_coreSkip * _Nullable skip __attribute__((swift_name("skip")));

/** The parts the library does not know, in the order they were written; empty for none. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRecurPart *> *unknownParts __attribute__((swift_name("unknownParts")));

/** The `UNTIL` of the rule: a date, a local date-time or a UTC date-time, or `null` when the rule has none. */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable until __attribute__((swift_name("until")));

/** The `WKST`, or `null` when the rule has none (Monday is then the default of the standard). */
@property (readonly) CalendrumKtorKotlinx_datetimeDayOfWeek * _Nullable weekStart __attribute__((swift_name("weekStart")));
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
@interface CalendrumKtorCalendrum_coreEventStatus : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreEventStatusCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreStructuredData : CalendrumKtorBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumKtorCalendrum_coreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithBytes:(CalendrumKtorKotlinByteArray *)bytes formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(bytes:formatType:schema:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumKtorCalendrum_coreValueType *)valueType formatType:(NSString *)formatType schema:(NSString *)schema __attribute__((swift_name("init(value:valueType:formatType:schema:)"))) __attribute__((objc_designated_initializer));

/** Returns a copy of the binary content, or `null` when the data is text or a URI. */
- (CalendrumKtorKotlinByteArray * _Nullable)bytes __attribute__((swift_name("bytes()")));

/** Returns `true` when [other] is a StructuredData equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable formatType __attribute__((swift_name("formatType")));

/** Whether the data is binary (base64 encoded in the text form). */
@property (readonly) BOOL isBinary __attribute__((swift_name("isBinary")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable schema __attribute__((swift_name("schema")));
@property (readonly) NSString * _Nullable text __attribute__((swift_name("text")));
@property (readonly) CalendrumKtorCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
@interface CalendrumKtorCalendrum_coreStyledDescription : CalendrumKtorBase
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumKtorCalendrum_coreValueType *)valueType __attribute__((swift_name("init(value:valueType:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithValue:(NSString *)value valueType:(CalendrumKtorCalendrum_coreValueType *)valueType formatType:(NSString *)formatType __attribute__((swift_name("init(value:valueType:formatType:)"))) __attribute__((objc_designated_initializer));

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
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@property (readonly) CalendrumKtorCalendrum_coreValueType *valueType __attribute__((swift_name("valueType")));
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
@interface CalendrumKtorCalendrum_coreTransparency : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreTransparencyCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreVFreeBusyCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VFreeBusy values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVFreeBusyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVFreeBusyBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VFreeBusy, or returns `null` when it is not a `VFREEBUSY` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVFreeBusy * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreFreeBusy : CalendrumKtorBase
- (instancetype)initWithPeriods:(NSArray<CalendrumKtorCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("init(periods:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithType:(CalendrumKtorCalendrum_coreFbType *)type periods:(NSArray<CalendrumKtorCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("init(type:periods:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a FreeBusy equal to this one, comparing their content. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code that agrees with [equals]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalPeriod *> *periods __attribute__((swift_name("periods")));
@property (readonly) CalendrumKtorCalendrum_coreFbType *type __attribute__((swift_name("type")));
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
@interface CalendrumKtorCalendrum_coreImageDisplay : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreImageDisplayCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a ImageDisplay with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
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
@interface CalendrumKtorCalendrum_coreICalParameter : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreICalParameterCompanion *companion __attribute__((swift_name("companion")));

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


/** Creates VJournal values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVJournal.Companion")))
@interface CalendrumKtorCalendrum_coreVJournalCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VJournal values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVJournalCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVJournalBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VJournal, or returns `null` when it is not a `VJOURNAL` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVJournal * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreJournalStatus : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreJournalStatusCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreMethodCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Method and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreMethodCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Method holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreMethod *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ADD`. The organizer adds instances to an existing series. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *Add __attribute__((swift_name("Add")));

/** `CANCEL`. The organizer cancels a component or some of its instances. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *Cancel __attribute__((swift_name("Cancel")));

/** `COUNTER`. An attendee proposes a change. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *Counter __attribute__((swift_name("Counter")));

/** `DECLINECOUNTER`. The organizer declines a proposed change. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *DeclineCounter __attribute__((swift_name("DeclineCounter")));

/** `PUBLISH`. Publishes a component to anyone, without expecting an answer. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *Publish __attribute__((swift_name("Publish")));

/** `REFRESH`. An attendee asks for the latest version. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *Refresh __attribute__((swift_name("Refresh")));

/** `REPLY`. An attendee answers a request. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *Reply __attribute__((swift_name("Reply")));

/** `REQUEST`. Asks for attendance, or updates a component sent before. */
@property (readonly) CalendrumKtorCalendrum_coreMethod *Request __attribute__((swift_name("Request")));
@end


/** Creates durations by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalDuration.Companion")))
@interface CalendrumKtorCalendrum_coreICalDurationCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates durations by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreICalDurationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates the duration that equals [duration] in hours, minutes and seconds.
 *
 * @throws IllegalArgumentException if [duration] is infinite, has a fraction of a second, or is longer than
 *   `Int.MAX_VALUE` hours.
 */
- (CalendrumKtorCalendrum_coreICalDuration *)ofDuration:(int64_t)duration __attribute__((swift_name("of(duration:)")));

/**
 * Creates a duration of [days] days; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [days] is `Int.MIN_VALUE`.
 */
- (CalendrumKtorCalendrum_coreICalDuration *)ofDaysDays:(int32_t)days __attribute__((swift_name("ofDays(days:)")));

/**
 * Creates a duration of [hours] hours; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [hours] is `Int.MIN_VALUE`.
 */
- (CalendrumKtorCalendrum_coreICalDuration *)ofHoursHours:(int32_t)hours __attribute__((swift_name("ofHours(hours:)")));

/**
 * Creates a duration of [minutes] minutes; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [minutes] is `Int.MIN_VALUE`.
 */
- (CalendrumKtorCalendrum_coreICalDuration *)ofMinutesMinutes:(int32_t)minutes __attribute__((swift_name("ofMinutes(minutes:)")));

/**
 * Creates a duration of [seconds] seconds; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [seconds] is `Int.MIN_VALUE`.
 */
- (CalendrumKtorCalendrum_coreICalDuration *)ofSecondsSeconds:(int32_t)seconds __attribute__((swift_name("ofSeconds(seconds:)")));

/**
 * Creates a duration of [weeks] weeks; negative values give a negative duration.
 *
 * @throws IllegalArgumentException if [weeks] is `Int.MIN_VALUE`.
 */
- (CalendrumKtorCalendrum_coreICalDuration *)ofWeeksWeeks:(int32_t)weeks __attribute__((swift_name("ofWeeks(weeks:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/DateTimePeriodSerializer))
*/
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod")))
@interface CalendrumKtorKotlinx_datetimeDateTimePeriod : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorKotlinx_datetimeDateTimePeriodCompanion *companion __attribute__((swift_name("companion")));
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
@interface CalendrumKtorCalendrum_coreVTimeZoneCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTimeZone values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVTimeZoneCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTimeZone, or returns `null` when it is not a `VTIMEZONE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVTimeZone * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreVTimeZoneObservance : CalendrumKtorBase

/**
 * Returns a copy of this observance changed by [block]; untouched nodes are shared.
 *
 * @param block changes the builder.
 */
- (CalendrumKtorCalendrum_coreVTimeZoneObservance *)editBlock:(void (^)(CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *))block __attribute__((swift_name("edit(block:)")));

/** Returns `true` when [other] is an observance of the same kind with an equal component. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the component. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns a builder that starts from this observance. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)toBuilder __attribute__((swift_name("toBuilder()")));

/** The comments (`COMMENT`) in source order. */
@property (readonly) NSArray<NSString *> *comments __attribute__((swift_name("comments")));
@property (readonly) CalendrumKtorCalendrum_coreICalComponent *component __attribute__((swift_name("component")));

/**
 * The concepts (`CONCEPT`, [RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)) of the
 * observance in source order.
 */
@property (readonly) NSArray<NSString *> *concepts __attribute__((swift_name("concepts")));

/**
 * The links (`LINK`, [RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)) of the observance in
 * source order.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreLink *> *links __attribute__((swift_name("links")));

/**
 * The names of the observance (`TZNAME`,
 * [RFC 5545 §3.8.3.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.2)), such as `CEST`, in source
 * order.
 */
@property (readonly, getter=names_) NSArray<NSString *> *names __attribute__((swift_name("names")));

/**
 * The UTC offset in force before the observance (`TZOFFSETFROM`,
 * [RFC 5545 §3.8.3.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.3)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The UTC offset the observance puts in force (`TZOFFSETTO`,
 * [RFC 5545 §3.8.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.4)), or `null` when it is missing or
 * does not decode.
 */
@property (readonly) CalendrumKtorKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The additional start dates (`RDATE`) in source order; a period contributes its start. */
@property (readonly) NSArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *rdates __attribute__((swift_name("rdates")));

/**
 * The reference identifiers (`REFID`, [RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)) of
 * the observance in source order.
 */
@property (readonly) NSArray<NSString *> *refIds __attribute__((swift_name("refIds")));

/**
 * The relationships (`RELATED-TO`, [RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5))
 * of the observance in source order.
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreRelatedTo *> *relatedTo __attribute__((swift_name("relatedTo")));

/** The recurrence rule of the observance (`RRULE`), or `null` when it has none or it does not decode. */
@property (readonly) CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/**
 * The local start (`DTSTART`) of the observance, in the offset before it; `null` when it is missing or does not
 * decode.
 */
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));
@end


/** Creates VTodo values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTodo.Companion")))
@interface CalendrumKtorCalendrum_coreVTodoCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VTodo values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVTodoCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. `UID` and `DTSTAMP` are required: set them, or give the builder [BuildDefaults] to
 * generate them.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVTodoBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VTodo, or returns `null` when it is not a `VTODO` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVTodo * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreTodoStatus : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreTodoStatusCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a TodoStatus with the same token. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns the hash code of the token. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the token. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/** Creates properties by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Companion")))
@interface CalendrumKtorCalendrum_coreICalPropertyCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates properties by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreICalPropertyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a property named [name].
 *
 * @param name letters, digits and hyphens only; `BEGIN` and `END` are not allowed.
 * @throws IllegalArgumentException if [name] is invalid.
 */
- (CalendrumKtorCalendrum_coreICalPropertyBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));

/**
 * Creates a property with no parameters.
 *
 * @param name letters, digits and hyphens only; the stored name is upper case; `BEGIN` and `END` are not
 *   allowed.
 * @param value the value as it is written; a control character other than a tab is not allowed.
 * @throws IllegalArgumentException if [name] or [value] is invalid.
 */
- (CalendrumKtorCalendrum_coreICalProperty *)ofName:(NSString *)name value:(NSString *)value __attribute__((swift_name("of(name:value:)")));
@end


/**
 * Collects the properties and nested components of a new or edited alarm, with a Kotlin DSL and chained calls. Get
 * one from [VAlarm.builder] or [VAlarm.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVAlarm.Builder")))
@interface CalendrumKtorCalendrum_coreVAlarmBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVAlarmBuilder *>

/**
 * Sets [acknowledged] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)acknowledgedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("acknowledged(value:)")));

/**
 * Sets [action] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)actionValue:(CalendrumKtorCalendrum_coreAlarmAction * _Nullable)value __attribute__((swift_name("action(value:)")));

/** Adds [attach] as an `ATTACH` property. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)attachAttach:(id<CalendrumKtorCalendrum_coreAttach>)attach __attribute__((swift_name("attach(attach:)")));

/** Adds [attendee] as an `ATTENDEE` property. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)attendeeAttendee:(CalendrumKtorCalendrum_coreAttendee *)attendee __attribute__((swift_name("attendee(attendee:)")));

/** Adds an `ATTENDEE` for the address [address], set up by [block]. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)attendeeAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("attendee(address:block:)")));

/** Edits the attendees in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)attendeesBlock:(void (^)(CalendrumKtorCalendrum_coreAttendeeList *))block __attribute__((swift_name("attendees(block:)")));

/**
 * Makes this an `AUDIO` alarm: sets `ACTION` and [trigger]; the platform chooses the sound unless you attach
 * one.
 *
 * @param trigger when the alarm fires.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)audioTrigger:(id<CalendrumKtorCalendrum_coreTrigger>)trigger __attribute__((swift_name("audio(trigger:)")));

/** Creates the alarm. */
- (CalendrumKtorCalendrum_coreVAlarm *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Makes this a `DISPLAY` alarm: sets `ACTION`, [trigger] and the `DESCRIPTION` shown to the user.
 *
 * @param trigger when the alarm fires.
 * @param description the text to display.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)displayTrigger:(id<CalendrumKtorCalendrum_coreTrigger>)trigger description:(NSString *)description __attribute__((swift_name("display(trigger:description:)")));

/**
 * Sets [duration] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)durationValue:(CalendrumKtorCalendrum_coreICalDuration * _Nullable)value __attribute__((swift_name("duration(value:)")));

/**
 * Makes this an `EMAIL` alarm: sets `ACTION`, [trigger], the `SUMMARY` that becomes the subject and the
 * `DESCRIPTION` that becomes the body.
 *
 * @param trigger when the alarm fires.
 * @param summary the subject of the mail.
 * @param description the body of the mail.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)emailTrigger:(id<CalendrumKtorCalendrum_coreTrigger>)trigger summary:(NSString *)summary description:(NSString *)description __attribute__((swift_name("email(trigger:summary:description:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the alarm. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)locationValue:(CalendrumKtorCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)locationBlock:(void (^)(CalendrumKtorCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Edits the locations of the alarm in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)locationsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [proximity] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)proximityValue:(CalendrumKtorCalendrum_coreProximity * _Nullable)value __attribute__((swift_name("proximity(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [repeat] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)repeatValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("repeat(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)styledDescriptionValue:(CalendrumKtorCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [trigger] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)triggerValue:(id<CalendrumKtorCalendrum_coreTrigger> _Nullable)value __attribute__((swift_name("trigger(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * The time the alarm was acknowledged of the alarm being built (`ACKNOWLEDGED`,
 * [RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)); assigning `null` removes the property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable acknowledged __attribute__((swift_name("acknowledged")));

/**
 * The action of the alarm being built (`ACTION`,
 * [RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreAlarmAction * _Nullable action __attribute__((swift_name("action")));

/**
 * The attendees added so far (`ATTENDEE`,
 * [RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *attendees __attribute__((swift_name("attendees")));

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
@property CalendrumKtorCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));

/** The locations of the alarm being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The proximity condition of the alarm being built (`PROXIMITY`,
 * [RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)); assigning `null` removes the property.
 */
@property CalendrumKtorCalendrum_coreProximity * _Nullable proximity __attribute__((swift_name("proximity")));

/**
 * The number of repetitions of the alarm being built (`REPEAT`,
 * [RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable repeat __attribute__((swift_name("repeat")));

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
@property id<CalendrumKtorCalendrum_coreTrigger> _Nullable trigger __attribute__((swift_name("trigger")));

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
@interface CalendrumKtorCalendrum_coreAttendeeBuilder : CalendrumKtorBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the attendee.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumKtorCalendrum_coreAttendee *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [cuType] to [value] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)cuTypeValue:(CalendrumKtorCalendrum_coreCuType *)value __attribute__((swift_name("cuType(value:)")));

/** Sets [delegatedFrom] to [values] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)delegatedFromValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedFrom(values:)")));

/** Sets [delegatedTo] to [values] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)delegatedToValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("delegatedTo(values:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/** Sets [member] to [values] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)memberValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("member(values:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)parameterName:(NSString *)name values:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Sets [partStat] to [value] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)partStatValue:(CalendrumKtorCalendrum_corePartStat *)value __attribute__((swift_name("partStat(value:)")));

/** Removes the parameter [name]. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [role] to [value] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)roleValue:(CalendrumKtorCalendrum_coreRole *)value __attribute__((swift_name("role(value:)")));

/** Sets [rsvp] to [value] and returns this builder. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)rsvpValue:(BOOL)value __attribute__((swift_name("rsvp(value:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The calendar address. */
@property NSString *address __attribute__((swift_name("address")));

/** The common name (`CN`); assigning `null` removes the parameter. */
@property NSString * _Nullable cn __attribute__((swift_name("cn")));

/** The calendar user type (`CUTYPE`); [CuType.Individual] when absent. */
@property CalendrumKtorCalendrum_coreCuType *cuType __attribute__((swift_name("cuType")));

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
@property CalendrumKtorCalendrum_corePartStat *partStat __attribute__((swift_name("partStat")));

/** The role (`ROLE`); [Role.ReqParticipant] when absent. */
@property CalendrumKtorCalendrum_coreRole *role __attribute__((swift_name("role")));

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
@interface CalendrumKtorCalendrum_coreAttendeeList : CalendrumKtorBase

/** Adds [attendee] after the existing ones. */
- (void)addAttendee:(CalendrumKtorCalendrum_coreAttendee *)attendee __attribute__((swift_name("add(attendee:)")));

/**
 * Adds an attendee with the address [address], set up by [block].
 *
 * @param address the calendar address of the new attendee.
 * @param block sets up the attendee.
 */
- (void)addAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("add(address:block:)")));

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
- (BOOL)replaceAddress:(NSString *)address block:(void (^)(CalendrumKtorCalendrum_coreAttendeeBuilder *))block __attribute__((swift_name("replace(address:block:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumKtorCalendrum_coreAttendee *> *items __attribute__((swift_name("items")));
@end


/**
 * Collects the properties and nested components of a new or edited location component, with a Kotlin DSL and
 * chained calls. Get one from [VLocation.builder] or [VLocation.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVLocation.Builder")))
@interface CalendrumKtorCalendrum_coreVLocationBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVLocationBuilder *>

/**
 * Creates the location component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumKtorCalendrum_coreVLocation *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)geoValue:(CalendrumKtorCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [locationTypes] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)locationTypesValue:(NSArray<NSString *> * _Nullable)value __attribute__((swift_name("locationTypes(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)structuredDataValue:(CalendrumKtorCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

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
@property CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

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
@interface CalendrumKtorCalendrum_coreOrganizerBuilder : CalendrumKtorBase

/** Sets [address] to [value] and returns this builder. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)addressValue:(NSString *)value __attribute__((swift_name("address(value:)")));

/**
 * Creates the organizer.
 *
 * @throws IllegalArgumentException if the address is not a valid calendar address.
 */
- (CalendrumKtorCalendrum_coreOrganizer *)build __attribute__((swift_name("build()")));

/** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)cnValue:(NSString * _Nullable)value __attribute__((swift_name("cn(value:)")));

/** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)dirValue:(NSString * _Nullable)value __attribute__((swift_name("dir(value:)")));

/** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)emailValue:(NSString * _Nullable)value __attribute__((swift_name("email(value:)")));

/** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)languageValue:(NSString * _Nullable)value __attribute__((swift_name("language(value:)")));

/**
 * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
 * typed field.
 */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)parameterName:(NSString *)name values:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/** Removes the parameter [name]. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)removeParameterName:(NSString *)name __attribute__((swift_name("removeParameter(name:)")));

/** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)sentByValue:(NSString * _Nullable)value __attribute__((swift_name("sentBy(value:)")));

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
@interface CalendrumKtorCalendrum_coreParticipantBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreParticipantBuilder *>

/** Adds [value] as an `ATTACH` property. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)attachValue:(id<CalendrumKtorCalendrum_coreAttach>)value __attribute__((swift_name("attach(value:)")));

/**
 * Creates the participant.
 *
 * @throws IllegalStateException if `UID` or `PARTICIPANT-TYPE` is missing and the builder has no defaults for
 *   the `UID`.
 */
- (CalendrumKtorCalendrum_coreParticipant *)build __attribute__((swift_name("build()")));

/**
 * Sets [calendarAddress] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)calendarAddressValue:(NSString * _Nullable)value __attribute__((swift_name("calendarAddress(value:)")));

/** Adds a `CATEGORIES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)categoriesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("categories(values:)")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds a `CONTACT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)contactText:(NSString *)text __attribute__((swift_name("contact(text:)")));

/**
 * Sets [created] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)createdValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("created(value:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)geoValue:(CalendrumKtorCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/**
 * Sets [lastModified] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)lastModifiedValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("lastModified(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds [value] as a location of the participant component. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)locationValue:(CalendrumKtorCalendrum_coreVLocation *)value __attribute__((swift_name("location(value:)")));

/**
 * Adds a location that [block] sets up.
 *
 * @param block sets up the new location.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)locationBlock:(void (^)(CalendrumKtorCalendrum_coreVLocationBuilder *))block __attribute__((swift_name("location(block:)")));

/** Adds a `LOCATION` property with the text [text]. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)locationTextText:(NSString *)text __attribute__((swift_name("locationText(text:)")));

/** Edits the locations of the participant component in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)locationsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *))block __attribute__((swift_name("locations(block:)")));

/**
 * Sets [participantType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)participantTypeValue:(CalendrumKtorCalendrum_coreParticipantType * _Nullable)value __attribute__((swift_name("participantType(value:)")));

/**
 * Sets [priority] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)priorityValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("priority(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Adds [value] as a `REQUEST-STATUS` property. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)requestStatusValue:(CalendrumKtorCalendrum_coreRequestStatus *)value __attribute__((swift_name("requestStatus(value:)")));

/** Adds [value] as a resource of the participant component. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)resourceComponentValue:(CalendrumKtorCalendrum_coreVResource *)value __attribute__((swift_name("resourceComponent(value:)")));

/**
 * Adds a resource that [block] sets up.
 *
 * @param block sets up the new resource.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)resourceComponentBlock:(void (^)(CalendrumKtorCalendrum_coreVResourceBuilder *))block __attribute__((swift_name("resourceComponent(block:)")));

/** Edits the resources of the participant component in [block]: add, replace or remove them. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)resourceComponentsBlock:(void (^)(CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *))block __attribute__((swift_name("resourceComponents(block:)")));

/** Adds a `RESOURCES` property with the given [values]. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)resourcesValues:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("resources(values:)")));

/**
 * Sets [sequence] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)sequenceValue:(CalendrumKtorInt * _Nullable)value __attribute__((swift_name("sequence(value:)")));

/**
 * Sets [stamp] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)stampValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("stamp(value:)")));

/**
 * Sets [status] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)statusValue:(CalendrumKtorCalendrum_coreEventStatus * _Nullable)value __attribute__((swift_name("status(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)structuredDataValue:(CalendrumKtorCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/** Adds [value] as a `STYLED-DESCRIPTION` property. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)styledDescriptionValue:(CalendrumKtorCalendrum_coreStyledDescription *)value __attribute__((swift_name("styledDescription(value:)")));

/**
 * Sets [summary] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)summaryValue:(NSString * _Nullable)value __attribute__((swift_name("summary(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

/**
 * Sets [url] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)urlValue:(NSString * _Nullable)value __attribute__((swift_name("url(value:)")));

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
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable created __attribute__((swift_name("created")));

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
@property CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The time of the last modification of the participant component being built (`LAST-MODIFIED`,
 * [RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable lastModified __attribute__((swift_name("lastModified")));

/** The locations of the participant component being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVLocation *> *locations __attribute__((swift_name("locations")));

/**
 * The participant type of the participant component being built (`PARTICIPANT-TYPE`,
 * [RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)); assigning `null` removes the property.
 */
@property CalendrumKtorCalendrum_coreParticipantType * _Nullable participantType __attribute__((swift_name("participantType")));

/**
 * The priority of the participant component being built (`PRIORITY`,
 * [RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable priority __attribute__((swift_name("priority")));

/** The resources of the participant component being built, as an editable list. */
@property (readonly) CalendrumKtorCalendrum_coreComponentList<CalendrumKtorCalendrum_coreVResource *> *resourceComponents __attribute__((swift_name("resourceComponents")));

/**
 * The revision sequence number of the participant component being built (`SEQUENCE`,
 * [RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorInt * _Nullable sequence __attribute__((swift_name("sequence")));

/**
 * The time stamp of the participant component being built (`DTSTAMP`,
 * [RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)); assigning `null` removes the
 * property.
 */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable stamp __attribute__((swift_name("stamp")));

/**
 * The status of the participant component being built (`STATUS`,
 * [RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)); assigning `null` removes the
 * property.
 */
@property CalendrumKtorCalendrum_coreEventStatus * _Nullable status __attribute__((swift_name("status")));

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
@interface CalendrumKtorCalendrum_coreICalPeriod : CalendrumKtorBase
- (instancetype)initWithStart:(id<CalendrumKtorCalendrum_coreICalDateTime>)start end:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)end duration:(CalendrumKtorCalendrum_coreICalDuration * _Nullable)duration __attribute__((swift_name("init(start:end:duration:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] is a period with equal start, end and duration. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the start, the end and the duration. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the start and the end or duration separated by a slash, in the forms of [ICalDateTime.toString] and
 * [ICalDuration.toString].
 */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorCalendrum_coreICalDuration * _Nullable duration __attribute__((swift_name("duration")));
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable end __attribute__((swift_name("end")));
@property (readonly) id<CalendrumKtorCalendrum_coreICalDateTime> start __attribute__((swift_name("start")));
@end


/**
 * Collects the properties and nested components of a new or edited resource component, with a Kotlin DSL and
 * chained calls. Get one from [VResource.builder] or [VResource.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVResource.Builder")))
@interface CalendrumKtorCalendrum_coreVResourceBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVResourceBuilder *>

/**
 * Creates the resource component.
 *
 * @throws IllegalStateException if `UID` is missing and the builder has no defaults.
 */
- (CalendrumKtorCalendrum_coreVResource *)build __attribute__((swift_name("build()")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/**
 * Sets [description] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)descriptionValue:(NSString * _Nullable)value __attribute__((swift_name("description(value:)")));

/**
 * Sets [geo] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)geoValue:(CalendrumKtorCalendrum_coreGeo * _Nullable)value __attribute__((swift_name("geo(value:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/**
 * Sets [name] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)nameValue:(NSString * _Nullable)value __attribute__((swift_name("name(value:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/**
 * Adds a `RELATED-TO` property: for a `String` the `UID` of the related component with the default relation,
 * otherwise the given value.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/**
 * Sets [resourceType] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)resourceTypeValue:(CalendrumKtorCalendrum_coreResourceType * _Nullable)value __attribute__((swift_name("resourceType(value:)")));

/** Adds [value] as a `STRUCTURED-DATA` property. */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)structuredDataValue:(CalendrumKtorCalendrum_coreStructuredData *)value __attribute__((swift_name("structuredData(value:)")));

/**
 * Sets [uid] to [value] and returns this builder; `null` removes the property.
 *
 * @param value the new value, or `null`.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)uidValue:(NSString * _Nullable)value __attribute__((swift_name("uid(value:)")));

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
@property CalendrumKtorCalendrum_coreGeo * _Nullable geo __attribute__((swift_name("geo")));

/**
 * The name of the resource component being built (`NAME`,
 * [RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)); assigning `null` removes the property.
 */
@property NSString * _Nullable name __attribute__((swift_name("name")));

/**
 * The resource type of the resource component being built (`RESOURCE-TYPE`,
 * [RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)); assigning `null` removes the property.
 */
@property CalendrumKtorCalendrum_coreResourceType * _Nullable resourceType __attribute__((swift_name("resourceType")));

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
@interface CalendrumKtorCalendrum_coreFrequency : CalendrumKtorKotlinEnum<CalendrumKtorCalendrum_coreFrequency *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `FREQ` of a recurrence rule: the unit of its repetition ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)).
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorCalendrum_coreFrequency *secondly __attribute__((swift_name("secondly")));
@property (class, readonly) CalendrumKtorCalendrum_coreFrequency *minutely __attribute__((swift_name("minutely")));
@property (class, readonly) CalendrumKtorCalendrum_coreFrequency *hourly __attribute__((swift_name("hourly")));
@property (class, readonly) CalendrumKtorCalendrum_coreFrequency *daily __attribute__((swift_name("daily")));
@property (class, readonly) CalendrumKtorCalendrum_coreFrequency *weekly __attribute__((swift_name("weekly")));
@property (class, readonly) CalendrumKtorCalendrum_coreFrequency *monthly __attribute__((swift_name("monthly")));
@property (class, readonly) CalendrumKtorCalendrum_coreFrequency *yearly __attribute__((swift_name("yearly")));
+ (CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreFrequency *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorCalendrum_coreFrequency *> *entries __attribute__((swift_name("entries")));
@end


/**
 * Builds a [Recur] that is correct by construction: the combinations the standard forbids are rejected as they are
 * added. Not thread-safe; obtain one with [Recur.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRecur.Builder")))
@interface CalendrumKtorCalendrum_coreRecurBuilder : CalendrumKtorBase

/**
 * Creates the rule.
 *
 * @throws IllegalStateException if `BYSETPOS` is used without another `BYxxx` part, or `BYWEEKNO` is combined
 *   with a numbered `BYDAY`.
 */
- (CalendrumKtorCalendrum_coreRecur *)build __attribute__((swift_name("build()")));

/**
 * Sets `BYDAY` to the given entries, which may carry a number such as the second Tuesday.
 *
 * @throws IllegalArgumentException if no entry is given, a number is used with a frequency other than `MONTHLY`
 *   or `YEARLY`, or a number is 0 or outside -53 to 53.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byDayDays:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreWeekdayNum *> *)days __attribute__((swift_name("byDay(days:)")));

/**
 * Sets `BYDAY` to the given days, every such weekday.
 *
 * @throws IllegalArgumentException if no day is given.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byDayDaysOfWeek:(CalendrumKtorKotlinArray<CalendrumKtorKotlinx_datetimeDayOfWeek *> *)daysOfWeek __attribute__((swift_name("byDay(daysOfWeek:)")));

/**
 * Sets `BYHOUR` to [values], each 0 to 23.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byHourValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("byHour(values:)")));

/**
 * Sets `BYMINUTE` to [values], each 0 to 59.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byMinuteValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("byMinute(values:)")));

/**
 * Sets `BYMONTH` to [values], each 1 to 12.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byMonthValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("byMonth(values:)")));

/**
 * Sets `BYMONTHDAY` to [values], each 1 to 31 or -31 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `WEEKLY`, there is no value or one is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byMonthDayValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("byMonthDay(values:)")));

/**
 * Sets `BYSECOND` to [values], each 0 to 60.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)bySecondValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("bySecond(values:)")));

/**
 * Sets `BYSETPOS` to [values], each 1 to 366 or -366 to -1. The rule also needs another `BYxxx` part.
 *
 * @throws IllegalArgumentException if there is no value or one is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)bySetPosValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("bySetPos(values:)")));

/**
 * Sets `BYWEEKNO` to [values], each 1 to 53 or -53 to -1.
 *
 * @throws IllegalArgumentException if the frequency is not `YEARLY`, there is no value or one is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byWeekNoValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("byWeekNo(values:)")));

/**
 * Sets `BYYEARDAY` to [values], each 1 to 366 or -366 to -1.
 *
 * @throws IllegalArgumentException if the frequency is `DAILY`, `WEEKLY` or `MONTHLY`, there is no value or one
 *   is out of range.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)byYearDayValues:(CalendrumKtorKotlinIntArray *)values __attribute__((swift_name("byYearDay(values:)")));

/**
 * Sets `COUNT`, the number of occurrences.
 *
 * @throws IllegalStateException if `UNTIL` is already set.
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)countValue:(int32_t)value __attribute__((swift_name("count(value:)")));

/**
 * Sets `INTERVAL`, the step in units of the frequency.
 *
 * @throws IllegalArgumentException if [value] is less than 1.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)intervalValue:(int32_t)value __attribute__((swift_name("interval(value:)")));

/** Returns the frequency of the rule being built. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Sets `UNTIL`, which is a date, a local date-time or a UTC date-time and must suit the type of `DTSTART`.
 *
 * @throws IllegalStateException if `COUNT` is already set.
 * @throws IllegalArgumentException if [value] is a zoned date-time or its year is outside 0000 to 9999.
 */
- (CalendrumKtorCalendrum_coreRecurBuilder *)untilValue:(id<CalendrumKtorCalendrum_coreICalDateTime>)value __attribute__((swift_name("until(value:)")));

/** Sets `WKST`, the day on which the week starts. */
- (CalendrumKtorCalendrum_coreRecurBuilder *)weekStartDay:(CalendrumKtorKotlinx_datetimeDayOfWeek *)day __attribute__((swift_name("weekStart(day:)")));
@end


/**
 * Collects the properties of a `STANDARD` or `DAYLIGHT` observance. Get one from [Standard.builder],
 * [Daylight.builder] or [Observance.toBuilder]. Not thread-safe.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVTimeZone.ObservanceBuilder")))
@interface CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder : CalendrumKtorCalendrum_coreComponentBuilder<CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *>

/** Creates the observance: a [Standard] or a [Daylight], as the builder was made for. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservance *)build __attribute__((swift_name("build()")));

/** Adds a `COMMENT` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)commentText:(NSString *)text __attribute__((swift_name("comment(text:)")));

/** Adds a `CONCEPT` property with the URI [uri]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)conceptUri:(NSString *)uri __attribute__((swift_name("concept(uri:)")));

/** Adds [value] as a `LINK` property. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)linkValue:(CalendrumKtorCalendrum_coreLink *)value __attribute__((swift_name("link(value:)")));

/** Adds a `TZNAME` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)nameText:(NSString *)text __attribute__((swift_name("name(text:)")));

/** Sets [offsetFrom] to [value] and returns this builder; `null` removes the property. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)offsetFromValue:(CalendrumKtorKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetFrom(value:)")));

/** Sets [offsetTo] to [value] and returns this builder; `null` removes the property. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)offsetToValue:(CalendrumKtorKotlinx_datetimeUtcOffset * _Nullable)value __attribute__((swift_name("offsetTo(value:)")));

/** Adds an `RDATE` property with the given [dates]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)rdateDates:(CalendrumKtorKotlinArray<id<CalendrumKtorCalendrum_coreICalDateTime>> *)dates __attribute__((swift_name("rdate(dates:)")));

/** Adds an `RDATE` property with the given [periods]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)rdatePeriods:(CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreICalPeriod *> *)periods __attribute__((swift_name("rdate(periods:)")));

/** Adds a `REFID` property with the text [text]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)refIdText:(NSString *)text __attribute__((swift_name("refId(text:)")));

/** Adds [relatedTo] as a `RELATED-TO` property. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)relatedToRelatedTo:(CalendrumKtorCalendrum_coreRelatedTo *)relatedTo __attribute__((swift_name("relatedTo(relatedTo:)")));

/** Adds a `RELATED-TO` property naming the component with the given [uid]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)relatedToUid:(NSString *)uid __attribute__((swift_name("relatedTo(uid:)")));

/** Sets [rrule] to [value] and returns this builder; `null` removes the property. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)rruleValue:(CalendrumKtorCalendrum_coreRecur * _Nullable)value __attribute__((swift_name("rrule(value:)")));

/** Sets the recurrence rule to one with the given [frequency], set up by [block]. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)rruleFrequency:(CalendrumKtorCalendrum_coreFrequency *)frequency block:(void (^)(CalendrumKtorCalendrum_coreRecurBuilder *))block __attribute__((swift_name("rrule(frequency:block:)")));

/** Sets [start] to [value] and returns this builder; `null` removes the property. */
- (CalendrumKtorCalendrum_coreVTimeZoneObservanceBuilder *)startValue:(id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable)value __attribute__((swift_name("start(value:)")));

/**
 * The offset before the observance being built (`TZOFFSETFROM`); assigning `null` removes the property.
 */
@property CalendrumKtorKotlinx_datetimeUtcOffset * _Nullable offsetFrom __attribute__((swift_name("offsetFrom")));

/**
 * The offset the observance being built puts in force (`TZOFFSETTO`); assigning `null` removes the
 * property.
 */
@property CalendrumKtorKotlinx_datetimeUtcOffset * _Nullable offsetTo __attribute__((swift_name("offsetTo")));

/** The recurrence rule of the observance being built (`RRULE`); assigning `null` removes the property. */
@property CalendrumKtorCalendrum_coreRecur * _Nullable rrule __attribute__((swift_name("rrule")));

/** The local start (`DTSTART`) of the observance being built; assigning `null` removes the property. */
@property id<CalendrumKtorCalendrum_coreICalDateTime> _Nullable start __attribute__((swift_name("start")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="2.3")
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant")))
@interface CalendrumKtorKotlinInstant : CalendrumKtorBase <CalendrumKtorKotlinComparable>
@property (class, readonly, getter=companion) CalendrumKtorKotlinInstantCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumKtorKotlinInstant *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (CalendrumKtorKotlinInstant *)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (int64_t)minusOther:(CalendrumKtorKotlinInstant *)other __attribute__((swift_name("minus(other:)")));
- (CalendrumKtorKotlinInstant *)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
- (int64_t)toEpochMilliseconds __attribute__((swift_name("toEpochMilliseconds()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t epochSeconds __attribute__((swift_name("epochSeconds")));
@property (readonly) int32_t nanosecondsOfSecond __attribute__((swift_name("nanosecondsOfSecond")));
@end


/**
 * Collects the properties and nested components of a new [ICalComponent].
 *
 * A builder is not thread-safe. Obtain one with [ICalComponent.builder].
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalComponent.Builder")))
@interface CalendrumKtorCalendrum_coreICalComponentBuilder : CalendrumKtorBase

/** Creates the component. The builder can be reused: later changes do not affect components already built. */
- (CalendrumKtorCalendrum_coreICalComponent *)build __attribute__((swift_name("build()")));

/** Appends [component] after the nested components added so far. */
- (CalendrumKtorCalendrum_coreICalComponentBuilder *)componentComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("component(component:)")));

/**
 * Appends a nested component built by [block].
 *
 * @param name the name of the nested component.
 * @param block fills the builder of the nested component.
 * @throws IllegalArgumentException if [name] is not a valid component name.
 */
- (CalendrumKtorCalendrum_coreICalComponentBuilder *)componentName:(NSString *)name block:(void (^)(CalendrumKtorCalendrum_coreICalComponentBuilder *))block __attribute__((swift_name("component(name:block:)")));

/** Appends [property] after the properties added so far. */
- (CalendrumKtorCalendrum_coreICalComponentBuilder *)propertyProperty:(CalendrumKtorCalendrum_coreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Appends a property with the given [name] and [value] and no parameters.
 *
 * @throws IllegalArgumentException if [name] is not a valid property name, or [value] contains a control
 *   character other than a tab.
 */
- (CalendrumKtorCalendrum_coreICalComponentBuilder *)propertyName:(NSString *)name value:(NSString *)value __attribute__((swift_name("property(name:value:)")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationCallPipeline")))
@interface CalendrumKtorKtor_server_coreApplicationCallPipeline : CalendrumKtorKtor_utilsPipeline<CalendrumKtorKotlinUnit *, id<CalendrumKtorKtor_server_corePipelineCall>>
- (instancetype)initWithDevelopmentMode:(BOOL)developmentMode environment:(id<CalendrumKtorKtor_server_coreApplicationEnvironment>)environment __attribute__((swift_name("init(developmentMode:environment:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithPhases:(CalendrumKtorKotlinArray<CalendrumKtorKtor_utilsPipelinePhase *> *)phases __attribute__((swift_name("init(phases:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithPhase:(CalendrumKtorKtor_utilsPipelinePhase *)phase interceptors:(NSArray<id<CalendrumKtorKotlinSuspendFunction2>> *)interceptors __attribute__((swift_name("init(phase:interceptors:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) CalendrumKtorKtor_server_coreApplicationCallPipelineApplicationPhase *companion __attribute__((swift_name("companion")));
@property (readonly) BOOL developmentMode __attribute__((swift_name("developmentMode")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationEnvironment> environment __attribute__((swift_name("environment")));
@property (readonly) CalendrumKtorKtor_server_coreApplicationReceivePipeline *receivePipeline __attribute__((swift_name("receivePipeline")));
@property (readonly) CalendrumKtorKtor_server_coreApplicationSendPipeline *sendPipeline __attribute__((swift_name("sendPipeline")));
@end

__attribute__((swift_name("Ktor_server_coreRoutingNode")))
@interface CalendrumKtorKtor_server_coreRoutingNode : CalendrumKtorKtor_server_coreApplicationCallPipeline <CalendrumKtorKtor_server_coreRoute>
- (instancetype)initWithParent:(CalendrumKtorKtor_server_coreRoutingNode * _Nullable)parent selector:(CalendrumKtorKtor_server_coreRouteSelector *)selector developmentMode:(BOOL)developmentMode environment:(id<CalendrumKtorKtor_server_coreApplicationEnvironment>)environment __attribute__((swift_name("init(parent:selector:developmentMode:environment:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithDevelopmentMode:(BOOL)developmentMode environment:(id<CalendrumKtorKtor_server_coreApplicationEnvironment>)environment __attribute__((swift_name("init(developmentMode:environment:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (void)afterIntercepted __attribute__((swift_name("afterIntercepted()")));
- (CalendrumKtorKtor_server_coreRoutingNode *)createChildSelector:(CalendrumKtorKtor_server_coreRouteSelector *)selector __attribute__((swift_name("createChild(selector:)")));
- (void)handleBody:(id<CalendrumKtorKotlinSuspendFunction1>)body __attribute__((swift_name("handle(body:)")));
- (id)installPlugin:(id<CalendrumKtorKtor_server_corePlugin>)plugin configure:(void (^)(id))configure __attribute__((swift_name("install(plugin:configure:)")));
- (void)invokeBody:(void (^)(CalendrumKtorKtor_server_coreRoutingNode *))body __attribute__((swift_name("invoke(body:)")));
- (id)pluginPlugin:(id<CalendrumKtorKtor_server_corePlugin>)plugin __attribute__((swift_name("plugin(plugin:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumKtorKtor_server_coreRoutingNode *> *children __attribute__((swift_name("children")));
@property (readonly) CalendrumKtorKtor_server_coreRoutingNode * _Nullable parent __attribute__((swift_name("parent")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelector *selector __attribute__((swift_name("selector")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreCoroutineScope")))
@protocol CalendrumKtorKotlinx_coroutines_coreCoroutineScope
@required
@property (readonly) id<CalendrumKtorKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationCall")))
@protocol CalendrumKtorKtor_server_coreApplicationCall <CalendrumKtorKotlinx_coroutines_coreCoroutineScope>
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)receiveNullableTypeInfo:(CalendrumKtorKtor_utilsTypeInfo *)typeInfo completionHandler:(void (^)(id _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("receiveNullable(typeInfo:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)respondMessage:(id _Nullable)message typeInfo:(CalendrumKtorKtor_utilsTypeInfo * _Nullable)typeInfo completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("respond(message:typeInfo:completionHandler:)")));
@property (readonly) CalendrumKtorKtor_server_coreApplication *application __attribute__((swift_name("application")));
@property (readonly) id<CalendrumKtorKtor_utilsAttributes> attributes __attribute__((swift_name("attributes")));
@property (readonly) id<CalendrumKtorKtor_httpParameters> parameters __attribute__((swift_name("parameters")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationRequest> request __attribute__((swift_name("request")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationResponse> response __attribute__((swift_name("response")));
@end

__attribute__((swift_name("Ktor_server_corePipelineCall")))
@protocol CalendrumKtorKtor_server_corePipelineCall <CalendrumKtorKtor_server_coreApplicationCall>
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinUnit")))
@interface CalendrumKtorKotlinUnit : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)unit __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKotlinUnit *shared __attribute__((swift_name("shared")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreRoutingResolveTrace")))
@interface CalendrumKtorKtor_server_coreRoutingResolveTrace : CalendrumKtorBase
- (instancetype)initWithCall:(id<CalendrumKtorKtor_server_corePipelineCall>)call segments:(NSArray<NSString *> *)segments __attribute__((swift_name("init(call:segments:)"))) __attribute__((objc_designated_initializer));
- (void)addCandidateTrait:(NSArray<CalendrumKtorKtor_server_coreRoutingResolveResultSuccess *> *)trait __attribute__((swift_name("addCandidate(trait:)")));
- (void)beginRoute:(CalendrumKtorKtor_server_coreRoutingNode *)route segmentIndex:(int32_t)segmentIndex __attribute__((swift_name("begin(route:segmentIndex:)")));
- (NSString *)buildText __attribute__((swift_name("buildText()")));
- (void)finishRoute:(CalendrumKtorKtor_server_coreRoutingNode *)route segmentIndex:(int32_t)segmentIndex result:(CalendrumKtorKtor_server_coreRoutingResolveResult *)result __attribute__((swift_name("finish(route:segmentIndex:result:)")));
- (void)registerFinalResultResult:(CalendrumKtorKtor_server_coreRoutingResolveResult *)result __attribute__((swift_name("registerFinalResult(result:)")));
- (void)skipRoute:(CalendrumKtorKtor_server_coreRoutingNode *)route segmentIndex:(int32_t)segmentIndex result:(CalendrumKtorKtor_server_coreRoutingResolveResult *)result __attribute__((swift_name("skip(route:segmentIndex:result:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorKtor_server_corePipelineCall> call __attribute__((swift_name("call")));
@property (readonly) NSArray<NSString *> *segments __attribute__((swift_name("segments")));
@end

__attribute__((swift_name("Ktor_server_coreRoutingResolveResult")))
@interface CalendrumKtorKtor_server_coreRoutingResolveResult : CalendrumKtorBase
@property (readonly) id<CalendrumKtorKtor_httpParameters> parameters __attribute__((swift_name("parameters")));
@property (readonly) CalendrumKtorKtor_server_coreRoutingNode *route __attribute__((swift_name("route")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreRouteSelectorEvaluation.Companion")))
@interface CalendrumKtorKtor_server_coreRouteSelectorEvaluationCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_server_coreRouteSelectorEvaluationCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_server_coreRouteSelectorEvaluation *)invokeSucceeded:(BOOL)succeeded quality:(double)quality parameters:(id<CalendrumKtorKtor_httpParameters>)parameters segmentIncrement:(int32_t)segmentIncrement __attribute__((swift_name("invoke(succeeded:quality:parameters:segmentIncrement:)"))) __attribute__((unavailable("Please use RouteSelectorEvaluation.Failure() or RouteSelectorEvaluation.Success() constructors")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluation *Constant __attribute__((swift_name("Constant")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluation *ConstantPath __attribute__((swift_name("ConstantPath")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluationFailure *Failed __attribute__((swift_name("Failed")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluationFailure *FailedMethod __attribute__((swift_name("FailedMethod")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluationFailure *FailedParameter __attribute__((swift_name("FailedParameter")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluationFailure *FailedPath __attribute__((swift_name("FailedPath")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluation *Missing __attribute__((swift_name("Missing")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluation *Transparent __attribute__((swift_name("Transparent")));
@property (readonly) CalendrumKtorKtor_server_coreRouteSelectorEvaluation *WildcardPath __attribute__((swift_name("WildcardPath")));
@property (readonly) double qualityConstant __attribute__((swift_name("qualityConstant")));
@property (readonly) double qualityFailedMethod __attribute__((swift_name("qualityFailedMethod")));
@property (readonly) double qualityFailedParameter __attribute__((swift_name("qualityFailedParameter")));
@property (readonly) double qualityMethodParameter __attribute__((swift_name("qualityMethodParameter")));
@property (readonly) double qualityMissing __attribute__((swift_name("qualityMissing")));
@property (readonly) double qualityParameter __attribute__((swift_name("qualityParameter")));
@property (readonly) double qualityParameterWithPrefixOrSuffix __attribute__((swift_name("qualityParameterWithPrefixOrSuffix")));
@property (readonly) double qualityPathParameter __attribute__((swift_name("qualityPathParameter")));
@property (readonly) double qualityQueryParameter __attribute__((swift_name("qualityQueryParameter")));
@property (readonly) double qualityTailcard __attribute__((swift_name("qualityTailcard")));
@property (readonly) double qualityTransparent __attribute__((swift_name("qualityTransparent")));
@property (readonly) double qualityWildcard __attribute__((swift_name("qualityWildcard")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsPipelinePhase")))
@interface CalendrumKtorKtor_utilsPipelinePhase : CalendrumKtorBase
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((swift_name("KotlinSuspendFunction2")))
@protocol CalendrumKtorKotlinSuspendFunction2 <CalendrumKtorKotlinFunction>
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)invokeP1:(id _Nullable)p1 p2:(id _Nullable)p2 completionHandler:(void (^)(id _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("invoke(p1:p2:completionHandler:)")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationConfigValue")))
@protocol CalendrumKtorKtor_server_coreApplicationConfigValue
@required
- (NSArray<NSString *> *)getList __attribute__((swift_name("getList()")));
- (NSString *)getString __attribute__((swift_name("getString()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsLogLevel")))
@interface CalendrumKtorKtor_utilsLogLevel : CalendrumKtorKotlinEnum<CalendrumKtorKtor_utilsLogLevel *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorKtor_utilsLogLevel *trace __attribute__((swift_name("trace")));
@property (class, readonly) CalendrumKtorKtor_utilsLogLevel *debug __attribute__((swift_name("debug")));
@property (class, readonly) CalendrumKtorKtor_utilsLogLevel *info __attribute__((swift_name("info")));
@property (class, readonly) CalendrumKtorKtor_utilsLogLevel *warn __attribute__((swift_name("warn")));
@property (class, readonly) CalendrumKtorKtor_utilsLogLevel *error __attribute__((swift_name("error")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKtor_utilsLogLevel *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKtor_utilsLogLevel *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((swift_name("Ktor_eventsEventDefinition")))
@interface CalendrumKtorKtor_eventsEventDefinition<T> : CalendrumKtorBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreDisposableHandle")))
@protocol CalendrumKtorKotlinx_coroutines_coreDisposableHandle
@required
- (void)dispose __attribute__((swift_name("dispose()")));
@end


/**
 * A named group of properties and components that a selection needs for some purpose, so that it need not list them one
 * by one.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFeature")))
@interface CalendrumKtorCalendrum_coreFeature : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreFeatureCompanion *companion __attribute__((swift_name("companion")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/** The constants for every code of the library, and a lookup by identifier. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProblemCode.Companion")))
@interface CalendrumKtorCalendrum_coreProblemCodeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants for every code of the library, and a lookup by identifier. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreProblemCodeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the code with the given [id], ignoring case.
 *
 * For an identifier the library knows this is the constant; for any other a new code is created with the
 * upper-case identifier and the severity [Severity.Warning].
 *
 * @throws IllegalArgumentException if [id] is empty.
 */
- (CalendrumKtorCalendrum_coreProblemCode *)ofId:(NSString *)id __attribute__((swift_name("of(id:)")));

/**
 * `ICAL-O003` (Warning): the `REPEAT` of an alarm is beyond the limit of alarm repetitions. The extra
 * repetitions are dropped.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *AlarmRepeatLimit __attribute__((swift_name("AlarmRepeatLimit")));

/**
 * `ICAL-L022` (Warning): a `TZID` is not an IANA zone but a Windows name, a display name, a link or a vendor
 * prefix. Found by the linter, once per `TZID`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *AliasTimeZoneId __attribute__((swift_name("AliasTimeZoneId")));

/**
 * `ICAL-S002` (Warning): a bare CR is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *BareCr __attribute__((swift_name("BareCr")));

/**
 * `ICAL-S003` (Warning): a bare LF is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *BareLf __attribute__((swift_name("BareLf")));

/** `ICAL-V015` (Warning): a `BINARY` value without base64 padding. The padding is added. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *Base64Padding __attribute__((swift_name("Base64Padding")));

/** `ICAL-V014` (Warning): whitespace inside a `BINARY` value. It is skipped. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *Base64Whitespace __attribute__((swift_name("Base64Whitespace")));

/**
 * `ICAL-S004` (Warning): a blank line, which the format does not allow. The line is skipped. Reported once per
 * input.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *BlankLine __attribute__((swift_name("BlankLine")));

/**
 * `ICAL-S027` (Warning): the charset hint contradicts the data, for example a byte order mark of another
 * encoding or clear UTF-8 data under a single-byte hint. The data decides.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *CharsetHintContradictsData __attribute__((swift_name("CharsetHintContradictsData")));

/**
 * `ICAL-L006` (Error): a component inside a parent that does not allow it, or that the iTIP method forbids.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ComponentNotAllowed __attribute__((swift_name("ComponentNotAllowed")));

/** `ICAL-S020` (Warning): `BEGIN` or `END` has parameters. They are ignored. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ComponentParameters __attribute__((swift_name("ComponentParameters")));

/**
 * `ICAL-S005` (Warning): the input starts with a continuation line, a space or a tab in the first byte. That
 * one character is removed and the line starts the first logical line.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ContinuationAtStart __attribute__((swift_name("ContinuationAtStart")));

/**
 * `ICAL-L026` (Error): a content line holds a control character other than horizontal tab, in its name, a
 * parameter or its value, which [RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1) forbids.
 * Parsing keeps the line as it is and byte-stable writing writes it back, but canonical writing, jCal and the
 * builders refuse it. Found by the validator and the linter; the explanation never quotes the line.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ControlCharacter __attribute__((swift_name("ControlCharacter")));

/**
 * `ICAL-V017` (Warning): a recurrence rule has both `COUNT` and `UNTIL`, which the standard forbids. Both are
 * kept; the position is that of the second.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *CountAndUntil __attribute__((swift_name("CountAndUntil")));

/**
 * `ICAL-V008` (Warning): a `DATE-TIME` value has no time, as in `DTSTART:20260901` without `VALUE=DATE`. It is
 * read as a `DATE`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *DateAsDateTime __attribute__((swift_name("DateAsDateTime")));

/** `ICAL-V007` (Warning): a `DATE` value has a time. The time is dropped. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *DateWithTime __attribute__((swift_name("DateWithTime")));

/** `ICAL-L012` (Error): a `DURATION` with hours, minutes or seconds beside a `DTSTART` of type `DATE`. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *DateWithTimeDuration __attribute__((swift_name("DateWithTimeDuration")));

/**
 * `ICAL-S025` (Warning): the encoding was guessed from the data, by a heuristic or by the placement of zero
 * bytes, and the input is transcoded from there on. The explanation names the encoding and the confidence in
 * percent.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *DetectedCharset __attribute__((swift_name("DetectedCharset")));

/**
 * `ICAL-V010` (Warning): a `DURATION` has hours and seconds but no minutes, such as `PT1H30S`. The missing `0M`
 * is added.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *DurationSkipsMinutes __attribute__((swift_name("DurationSkipsMinutes")));

/**
 * `ICAL-V009` (Warning): a `DURATION` has time units without the `T` designator, such as `P90M`. It is read as
 * `PT90M`, since the standard has no months in durations.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *DurationWithoutTimeDesignator __attribute__((swift_name("DurationWithoutTimeDesignator")));

/**
 * `ICAL-V016` (Warning): an empty list item, as in `a,,b`, or an empty rule part, as in `FREQ=DAILY;`. It is
 * skipped.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *EmptyItem __attribute__((swift_name("EmptyItem")));

/**
 * `ICAL-L025` (Warning): an empty value of a non-text property, such as `DTEND:` or `RRULE:`. Found by the
 * linter.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *EmptyValue __attribute__((swift_name("EmptyValue")));

/**
 * `ICAL-L011` (Error): `DTEND` or `DUE` is not later than `DTSTART`. Not checked when a zone does not resolve.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *EndBeforeStart __attribute__((swift_name("EndBeforeStart")));

/**
 * `ICAL-S016` (Warning): an `END` closes a component that is open deeper in the stack than the top. The
 * components in between are closed implicitly, then the matching one. One problem per `END` line.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *EndClosesNested __attribute__((swift_name("EndClosesNested")));

/** `ICAL-S015` (Warning): an `END` line with no open component. The line is ignored. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *EndWithoutBegin __attribute__((swift_name("EndWithoutBegin")));

/** `ICAL-L008` (Error): properties that exclude each other occur together, such as `DTEND` and `DURATION`. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ExclusiveProperties __attribute__((swift_name("ExclusiveProperties")));

/**
 * `ICAL-V005` (Warning): a date or time is written in the extended ISO 8601 form, with separators. It is read
 * as the basic form; in strict mode it is an error.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ExtendedIsoForm __attribute__((swift_name("ExtendedIsoForm")));

/**
 * `ICAL-O004` (Warning): a floating time, a `DATE` or an unresolved `TZID` was met while no floating zone is
 * set. The occurrence is skipped.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *FloatingWithoutZone __attribute__((swift_name("FloatingWithoutZone")));

/**
 * `ICAL-R009` (Info): an `RDATE` or `EXDATE` is in another form than `DTSTART`, such as UTC against a time
 * zone. They are compared by local time.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *FormMismatch __attribute__((swift_name("FormMismatch")));

/**
 * `ICAL-V006` (Warning): a time has fractions of a second, as written by JavaScript generators. The fraction is
 * dropped.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *FractionalSeconds __attribute__((swift_name("FractionalSeconds")));

/**
 * `ICAL-S030` (Error): the input is longer than [io.github.calendrum.syntax.ParseLimits.maxInputOctets]. Reading
 * stops before the first logical line that does not end within that many bytes, and the result is truncated.
 * Always reported.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *InputTooLarge __attribute__((swift_name("InputTooLarge")));

/**
 * `ICAL-S019` (Warning): the name in `BEGIN` or `END` is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is, and the component is opened and closed by it, ignoring ASCII
 * case.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *InvalidComponentName __attribute__((swift_name("InvalidComponentName")));

/** `ICAL-J001` (Error): the input is not JSON. Nothing is read. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *InvalidJson __attribute__((swift_name("InvalidJson")));

/**
 * `ICAL-S008` (Warning): a property or parameter name is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *InvalidName __attribute__((swift_name("InvalidName")));

/**
 * `ICAL-S006` (Warning): a logical line contains an invalid UTF-8 sequence. Each maximal invalid subsequence is
 * replaced with U+FFFD; the line is not skipped. One problem per line.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *InvalidUtf8 __attribute__((swift_name("InvalidUtf8")));

/**
 * `ICAL-Z003` (Warning): a `VTIMEZONE` has no `TZID`, no observance with `DTSTART` and `TZOFFSETTO`, or more than
 * 200,000 transitions up to its last start (a hostile zone of endless observances). It is skipped, and the next
 * resolver of the chain looks for the `TZID`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *InvalidVTimeZone __attribute__((swift_name("InvalidVTimeZone")));

/**
 * `ICAL-V001` (Error): a value does not conform to the grammar of its type; no value is decoded. In strict mode
 * it follows the warning code of a lenient repair, with the same position.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *InvalidValue __attribute__((swift_name("InvalidValue")));

/**
 * `ICAL-Z005` (Warning): the current rules of the zone cannot be written as one `RRULE` per observance, so the
 * generated `VTIMEZONE` only approximates them. The full mode gives an exact one.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *IrregularTimeZone __attribute__((swift_name("IrregularTimeZone")));

/**
 * `ICAL-J007` (Error): jCal components are nested deeper than `JCalOptions.maxDepth` (16 by default). The
 * deeper component is skipped with everything inside it.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *JCalTooDeep __attribute__((swift_name("JCalTooDeep")));

/**
 * `ICAL-J005` (Warning): a value is not in the form of its type, such as a `date-time` that is not
 * `YYYY-MM-DDTHH:MM:SS`. The value is written as it is.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *JCalValueMismatch __attribute__((swift_name("JCalValueMismatch")));

/**
 * `ICAL-V004` (Warning): a time has the second 60, which the standard allows. The decoder replaces it by 59 or
 * by the next minute, depending on its policy.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *LeapSecond __attribute__((swift_name("LeapSecond")));

/** `ICAL-R008` (Info): `BYSECOND=60` has no meaning in local time. The value is ignored. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *LeapSecondIgnored __attribute__((swift_name("LeapSecondIgnored")));

/**
 * `ICAL-S010` (Error): a logical line is longer than [io.github.calendrum.syntax.ParseLimits.maxLineOctets]. The
 * whole line is skipped and the bytes beyond the limit are not copied.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *LineTooLong __attribute__((swift_name("LineTooLong")));

/**
 * `ICAL-S028` (Warning): the heuristic is less than 90% sure of the encoding, typically for a short text of
 * capital abbreviations where windows-1251 and KOI8-R look alike. The input is read in the chosen encoding; the
 * application may want to ask the user or pass a hint.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *LowCharsetConfidence __attribute__((swift_name("LowCharsetConfidence")));

/**
 * `ICAL-J002` (Error): an element is not a jCal component: not an array of a name, an array of properties and
 * an array of components, or the name is not valid. The component is skipped with its children.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MalformedJCalComponent __attribute__((swift_name("MalformedJCalComponent")));

/**
 * `ICAL-J004` (Warning): a parameter value is not a string or an array of strings, or the `value` parameter
 * appears in the parameter object. The parameter is skipped.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MalformedJCalParameter __attribute__((swift_name("MalformedJCalParameter")));

/**
 * `ICAL-J003` (Error): a property is not an array of a name, an object of parameters, a type and at least one
 * value, or a value has the wrong JSON type. The property is skipped.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MalformedJCalProperty __attribute__((swift_name("MalformedJCalProperty")));

/**
 * `ICAL-S012` (Warning): a parameter is malformed: it has no `=`, a quote inside an unquoted value, or text
 * after a closing quote. A parameter without `=` gets no values; otherwise the value is taken literally up to
 * the next delimiter.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MalformedParameter __attribute__((swift_name("MalformedParameter")));

/**
 * `ICAL-L016` (Error): a value does not decode as its type; the details are an `ICAL-V` problem from reading.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MalformedValue __attribute__((swift_name("MalformedValue")));

/**
 * `ICAL-L018` (Error): the calendar has no `METHOD`, or one that differs from the method of the iTIP rule set
 * in use.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MethodMismatch __attribute__((swift_name("MethodMismatch")));

/**
 * `ICAL-L020` (Error): the iTIP method is not defined for the component, such as `REFRESH` for a `VJOURNAL`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MethodNotDefined __attribute__((swift_name("MethodNotDefined")));

/**
 * `ICAL-S007` (Error): there is no colon outside quotes after the property name and its parameters. The line is
 * skipped.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MissingColon __attribute__((swift_name("MissingColon")));

/**
 * `ICAL-L007` (Error): a required nested component is missing, such as a calendar without components or a
 * `VTIMEZONE` without `STANDARD` or `DAYLIGHT`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MissingComponent __attribute__((swift_name("MissingComponent")));

/**
 * `ICAL-L009` (Error): a property lacks the one it requires, such as `RRULE` without `DTSTART` or `REPEAT`
 * without `DURATION`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MissingDependency __attribute__((swift_name("MissingDependency")));

/** `ICAL-L001` (Error): a required property is missing. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MissingProperty __attribute__((swift_name("MissingProperty")));

/**
 * `ICAL-M001` (Error): a component of a schema lacks a required property, or the first such property is broken.
 * The component is skipped. The explanation names the property, never its value.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MissingRequiredField __attribute__((swift_name("MissingRequiredField")));

/**
 * `ICAL-L017` (Error): a `TZID` has no `VTIMEZONE` with that identifier in the calendar ([RFC 5545
 * §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)). Reported once per `TZID`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *MissingTimeZone __attribute__((swift_name("MissingTimeZone")));

/**
 * `ICAL-V012` (Warning): the offset `-0000`, which [RFC 5545
 * §3.3.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.14) forbids. It is read as `+0000`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *NegativeZeroOffset __attribute__((swift_name("NegativeZeroOffset")));

/**
 * `ICAL-L015` (Error): the `DTSTART` of a `VTIMEZONE` observance is not a local time ([RFC 5545
 * §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *NotLocalTime __attribute__((swift_name("NotLocalTime")));

/**
 * `ICAL-V027` (Warning): a value that must be in UTC is not, as in `DTSTAMP`, `CREATED`, `LAST-MODIFIED` and an
 * absolute `TRIGGER`. It is kept as it is.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *NotUtc __attribute__((swift_name("NotUtc")));

/**
 * `ICAL-O005` (Warning): a component has no `DTSTART` (and a to-do no `DUE` either), so it has no occurrence.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *OccurrenceWithoutStart __attribute__((swift_name("OccurrenceWithoutStart")));

/**
 * `ICAL-V002` (Error): a field is out of range, such as a month, a day, an hour, a UTC offset, an integer
 * beyond 32 bits, or a `BYxxx` part of a recurrence rule. No value is decoded.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *OutOfRange __attribute__((swift_name("OutOfRange")));

/**
 * `ICAL-O001` (Warning): the `RECURRENCE-ID` of an override matches no instance of the master. The override is
 * kept as a single occurrence.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *OverrideWithoutInstance __attribute__((swift_name("OverrideWithoutInstance")));

/**
 * `ICAL-V028` (Warning): a parameter does not apply to the value, such as `TZID` on a `DATE` or `RELATED` on an
 * absolute `TRIGGER`. It is not applied.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ParameterNotAllowed __attribute__((swift_name("ParameterNotAllowed")));

/**
 * `ICAL-V022` (Warning): a `PERIOD` ends no later than it starts, or has a duration that is not positive. It is
 * kept.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *PeriodNotPositive __attribute__((swift_name("PeriodNotPositive")));

/**
 * `ICAL-L004` (Error): a known property is in a component where it does not belong, or that the iTIP method
 * forbids.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *PropertyNotAllowed __attribute__((swift_name("PropertyNotAllowed")));

/**
 * `ICAL-S021` (Warning): a property outside any component, before the first `BEGIN` or between calendars. The
 * property is not part of the tree, but a cursor reports it at depth 0.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *PropertyOutsideComponent __attribute__((swift_name("PropertyOutsideComponent")));

/**
 * `ICAL-V003` (Error): a recurrence rule has no `FREQ` part ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)). No value is decoded.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *RecurWithoutFreq __attribute__((swift_name("RecurWithoutFreq")));

/**
 * `ICAL-R001` (Warning): the number of occurrences reached the limit while the sequence was not over, or a rule
 * with `COUNT` passed as many dates before the start of a range. The sequence ends.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *RecurrenceLimitReached __attribute__((swift_name("RecurrenceLimitReached")));

/**
 * `ICAL-R002` (Warning): too many periods in a row produced no occurrence, as in
 * `FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30`. The sequence ends.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *RecurrenceNoMatch __attribute__((swift_name("RecurrenceNoMatch")));

/**
 * `ICAL-L003` (Warning): a property occurs more than once where the standard says it SHOULD NOT, such as
 * `RRULE`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *RepeatedProperty __attribute__((swift_name("RepeatedProperty")));

/** `ICAL-V018` (Warning): a part of a recurrence rule is repeated. The last one is kept. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *RepeatedRulePart __attribute__((swift_name("RepeatedRulePart")));

/** `ICAL-R004` (Warning): the `RSCALE` of the rule is not `GREGORIAN`. The rule is not expanded. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *RscaleNotSupported __attribute__((swift_name("RscaleNotSupported")));

/**
 * `ICAL-V019` (Warning): a rule part is not allowed in this rule, such as `BYWEEKNO` without `YEARLY`, or
 * `BYSETPOS` without another `BYxxx` part. The part is kept.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *RulePartNotAllowed __attribute__((swift_name("RulePartNotAllowed")));

/**
 * `ICAL-R003` (Warning): `DTSTART` is not an instance of its own rule, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined. What happens
 * is up to the `startMismatch` option of the recurrence options.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *StartNotInRule __attribute__((swift_name("StartNotInRule")));

/**
 * `ICAL-O002` (Info): an override with `RANGE=THISANDFUTURE` was applied to the later instances, on a
 * best-effort basis.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ThisAndFutureApplied __attribute__((swift_name("ThisAndFutureApplied")));

/**
 * `ICAL-S029` (Error): the time budget [io.github.calendrum.syntax.ParseLimits.maxTime] ran out. Reading stops at a
 * line boundary and the result is truncated. Always reported.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TimeBudgetExceeded __attribute__((swift_name("TimeBudgetExceeded")));

/**
 * `ICAL-R007` (Warning): the rule has a time part or a frequency shorter than a day while `DTSTART` is a
 * `DATE`. The time parts are ignored.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TimePartWithDate __attribute__((swift_name("TimePartWithDate")));

/**
 * `ICAL-Z002` (Info): the `TZID` was found through an alias, such as an IANA link, a Windows name or a
 * `/mozilla.org/` prefix.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TimeZoneAlias __attribute__((swift_name("TimeZoneAlias")));

/**
 * `ICAL-Z004` (Warning): the `TZID` was found only by a guess from a city in its name. The zone of the first
 * city mentioned is used.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TimeZoneGuessed __attribute__((swift_name("TimeZoneGuessed")));

/**
 * `ICAL-S022` (Error): components are nested deeper than [io.github.calendrum.syntax.ParseLimits.maxDepth]. The
 * subtree is skipped up to its `END`, with one problem per subtree.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TooDeep __attribute__((swift_name("TooDeep")));

/**
 * `ICAL-S023` (Error): the limit on components was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxComponents]). Open components are closed implicitly, reading stops
 * and the result is truncated. Always reported.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TooManyComponents __attribute__((swift_name("TooManyComponents")));

/**
 * `ICAL-S014` (Error): the limit on logical lines was reached ([io.github.calendrum.syntax.ParseLimits.maxLines]).
 * Reading stops and the result is truncated. Always reported, even beyond the problem limit.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TooManyLines __attribute__((swift_name("TooManyLines")));

/**
 * `ICAL-S013` (Warning): the limit on reported problems was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxProblems]). Further problems are only counted; see
 * [io.github.calendrum.syntax.ParseResult.problemCount].
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TooManyProblems __attribute__((swift_name("TooManyProblems")));

/** `ICAL-L002` (Error): a property occurs more often than allowed. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TooManyProperties __attribute__((swift_name("TooManyProperties")));

/**
 * `ICAL-S024` (Info): the input was transcoded to UTF-8 from the encoding named by a byte order mark or by a
 * charset hint. The explanation names the encoding and where it came from. Positions refer to the decoded text.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TranscodedInput __attribute__((swift_name("TranscodedInput")));

/**
 * `ICAL-V024` (Warning): a `TZID` parameter on a UTC `DATE-TIME`, which the standard forbids. The parameter is
 * not applied and the time is UTC.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *TzidWithUtc __attribute__((swift_name("TzidWithUtc")));

/**
 * `ICAL-S018` (Warning): the input ended while components were open, as in a truncated feed. All of them are
 * closed implicitly. Not reported when reading was stopped by a limit.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnclosedComponents __attribute__((swift_name("UnclosedComponents")));

/**
 * `ICAL-S009` (Warning): a quoted parameter value is not closed before the end of the line. The value is taken
 * literally up to the next comma, semicolon or colon, and parsing goes on.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnclosedQuote __attribute__((swift_name("UnclosedQuote")));

/**
 * `ICAL-L019` (Info): the `ACTION` of an alarm is not `AUDIO`, `DISPLAY` or `EMAIL`; such an alarm is to be
 * ignored.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnknownAlarmAction __attribute__((swift_name("UnknownAlarmAction")));

/**
 * `ICAL-S026` (Warning): the charset hint is not recognized, either an unknown name or an unsupported encoding.
 * The hint is ignored and the data decides.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnknownCharsetHint __attribute__((swift_name("UnknownCharsetHint")));

/**
 * `ICAL-V023` (Warning): a backslash in a `TEXT` value is followed by a character that is not an escape of [RFC
 * 5545 §3.3.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.11), or ends the value. Both characters are
 * kept.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnknownEscape __attribute__((swift_name("UnknownEscape")));

/**
 * `ICAL-J006` (Warning): the type of a property is not a type of RFC 5545 or RFC 7265. No `VALUE` parameter is
 * written and the value is kept as it is.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnknownJCalType __attribute__((swift_name("UnknownJCalType")));

/**
 * `ICAL-L005` (Info): a property known neither to the registry nor to the rule tables, and not an `X-`
 * property.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnknownProperty __attribute__((swift_name("UnknownProperty")));

/**
 * `ICAL-V020` (Warning): an unknown part in a recurrence rule, such as an `X-` name or a future registered
 * part. It is kept, in order, and written after the known ones.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnknownRulePart __attribute__((swift_name("UnknownRulePart")));

/**
 * `ICAL-Z001` (Warning): no resolver of the chain knows the `TZID`. The unknown-zone policy decides: floating
 * time, UTC or an exception.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnknownTimeZone __attribute__((swift_name("UnknownTimeZone")));

/** `ICAL-S017` (Warning): an `END` names a component that is not open. The line is ignored. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnmatchedEnd __attribute__((swift_name("UnmatchedEnd")));

/**
 * `ICAL-S011` (Error): the input is in an encoding the library does not read at all, such as UTF-7, UTF-EBCDIC,
 * SCSU, BOCU-1 or GB18030, recognized by its signature. Nothing is parsed. The explanation names the encoding.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UnsupportedEncoding __attribute__((swift_name("UnsupportedEncoding")));

/**
 * `ICAL-R005` (Warning): the type of `UNTIL` does not match `DTSTART`, such as a `DATE` against a `DATE-TIME`
 * or UTC against local. The two are compared in the closest sensible way.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UntilTypeMismatch __attribute__((swift_name("UntilTypeMismatch")));

/**
 * `ICAL-R006` (Info): `UNTIL` is in UTC while `DTSTART` has a time zone and no `untilOffset` is set, so `UNTIL`
 * is compared with local time.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UntilWithoutZone __attribute__((swift_name("UntilWithoutZone")));

/**
 * `ICAL-L024` (Info): an `ORGANIZER` or `ATTENDEE` with the scheme in upper case, `MAILTO:`. Found by the
 * linter.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UppercaseMailto __attribute__((swift_name("UppercaseMailto")));

/**
 * `ICAL-V021` (Warning): a `URI` or `CAL-ADDRESS` without a scheme, such as an address without `mailto:`. It is
 * kept as it is.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UriWithoutScheme __attribute__((swift_name("UriWithoutScheme")));

/** `ICAL-V011` (Warning): a `UTC-OFFSET` is written with a colon, such as `+03:00`. It is read without it. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *UtcOffsetWithColon __attribute__((swift_name("UtcOffsetWithColon")));

/** `ICAL-S001` (Info): the input starts with a UTF-8 byte order mark. The three bytes are skipped. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *Utf8Bom __attribute__((swift_name("Utf8Bom")));

/**
 * `ICAL-L013` (Error): a value outside the list of the standard, such as a component `STATUS`, `VERSION` or
 * `TRANSP`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ValueNotAllowed __attribute__((swift_name("ValueNotAllowed")));

/**
 * `ICAL-L014` (Error): a number outside its range, such as `PRIORITY` outside 0 to 9 or `PERCENT-COMPLETE`
 * outside 0 to 100.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ValueOutOfRange __attribute__((swift_name("ValueOutOfRange")));

/**
 * `ICAL-L010` (Error): the form of a value differs from that of a related one, such as a `DATE` `DTEND` with a
 * `DATE-TIME` `DTSTART`, or a non-UTC `UNTIL` with a zoned `DTSTART`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ValueTypeMismatch __attribute__((swift_name("ValueTypeMismatch")));

/**
 * `ICAL-V025` (Warning): the `VALUE` parameter names a type this property does not take, or is empty. The
 * default type of the property is read.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ValueTypeNotAllowed __attribute__((swift_name("ValueTypeNotAllowed")));

/**
 * `ICAL-V026` (Warning): a value of a type other than the default is not declared by `VALUE` or `ENCODING`, as
 * in `TRIGGER:19980101T050000Z`. It is read as the value of that type.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *ValueTypeNotDeclared __attribute__((swift_name("ValueTypeNotDeclared")));

/** `ICAL-L023` (Info): `X-WR-CALNAME` without the standard `NAME` of RFC 7986. Found by the linter. */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *VendorCalendarName __attribute__((swift_name("VendorCalendarName")));

/**
 * `ICAL-L021` (Warning): `X-WR-TIMEZONE` beside floating times, which clients understand in different ways.
 * Found by the linter.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *VendorTimeZone __attribute__((swift_name("VendorTimeZone")));

/**
 * `ICAL-V013` (Warning): spaces or tabs around a value or a list item. They are dropped. Not reported for
 * `TEXT` and `BINARY`.
 */
@property (readonly) CalendrumKtorCalendrum_coreProblemCode *Whitespace __attribute__((swift_name("Whitespace")));
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
@protocol CalendrumKtorCalendrum_coreParameterCodec
@required

/**
 * Decodes [parameter] of [property], with problems and strictness as [context] says.
 *
 * @param property the property the parameter belongs to.
 * @param parameter the parameter to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumKtorCalendrum_coreDecoded>)decodeProperty:(CalendrumKtorCalendrum_coreICalProperty *)property parameter:(CalendrumKtorCalendrum_coreICalParameter *)parameter context:(CalendrumKtorCalendrum_coreDecodeContext *)context __attribute__((swift_name("decode(property:parameter:context:)")));

/**
 * Encodes [value] into a parameter named by [context].
 *
 * @param value the value to write.
 * @param context carries the parameter name.
 */
- (CalendrumKtorCalendrum_coreICalParameter *)encodeParameterValue:(id)value context:(CalendrumKtorCalendrum_coreEncodeContext *)context __attribute__((swift_name("encodeParameter(value:context:)")));
@end


/** The constants of ValueType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreValueType.Companion")))
@interface CalendrumKtorCalendrum_coreValueTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ValueType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreValueTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ValueType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreValueType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BINARY`. Base64 encoded binary data. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Binary __attribute__((swift_name("Binary")));

/** `BOOLEAN`. `TRUE` or `FALSE`. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Boolean __attribute__((swift_name("Boolean")));

/** `CAL-ADDRESS`. A calendar user address. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *CalAddress __attribute__((swift_name("CalAddress")));

/** `DATE`. A calendar date. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Date __attribute__((swift_name("Date")));

/** `DATE-TIME`. A date with a time of day. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *DateTime __attribute__((swift_name("DateTime")));

/** `DURATION`. A length of time. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Duration __attribute__((swift_name("Duration")));

/** `FLOAT`. A decimal number. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Float __attribute__((swift_name("Float")));

/** `INTEGER`. A signed 32-bit integer. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Integer __attribute__((swift_name("Integer")));

/** `PERIOD`. A precise period of time. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Period __attribute__((swift_name("Period")));

/** `RECUR`. A recurrence rule. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Recur __attribute__((swift_name("Recur")));

/** `TEXT`. Human-readable text. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Text __attribute__((swift_name("Text")));

/** `TIME`. A time of day. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Time __attribute__((swift_name("Time")));

/** `UID`. A `UID` reference (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Uid __attribute__((swift_name("Uid")));

/** `URI`. A uniform resource identifier. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *Uri __attribute__((swift_name("Uri")));

/** `UTC-OFFSET`. An offset from UTC. */
@property (readonly) CalendrumKtorCalendrum_coreValueType *UtcOffset __attribute__((swift_name("UtcOffset")));

/** `XML-REFERENCE`. A reference to an XML element (RFC 9073). */
@property (readonly) CalendrumKtorCalendrum_coreValueType *XmlReference __attribute__((swift_name("XmlReference")));
@end


/**
 * The result of decoding a property or parameter: a value, nothing, or a malformed one.
 *
 * Reading with `component[key]` gives only the value or `null`; `component.decode(key)` tells which of the three it was
 * and why.
 */
__attribute__((swift_name("Calendrum_coreDecoded")))
@protocol CalendrumKtorCalendrum_coreDecoded
@required
@end


/**
 * How a codec decodes: leniently or strictly, and the factory for problems at the right position.
 *
 * @property isStrict whether a value that is only acceptable by repair is an error.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext")))
@interface CalendrumKtorCalendrum_coreDecodeContext : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreDecodeContextCompanion *companion __attribute__((swift_name("companion")));

/** Creates a problem with [code] positioned [valueOffset] characters into the value of [property]. */
- (CalendrumKtorCalendrum_coreProblem *)problemProperty:(CalendrumKtorCalendrum_coreICalProperty *)property code:(CalendrumKtorCalendrum_coreProblemCode *)code valueOffset:(int32_t)valueOffset __attribute__((swift_name("problem(property:code:valueOffset:)")));

/** Creates a problem with [code] positioned at the value of [parameter] of [property]. */
- (CalendrumKtorCalendrum_coreProblem *)problemProperty:(CalendrumKtorCalendrum_coreICalProperty *)property parameter:(CalendrumKtorCalendrum_coreICalParameter *)parameter code:(CalendrumKtorCalendrum_coreProblemCode *)code __attribute__((swift_name("problem(property:parameter:code:)")));

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
@interface CalendrumKtorCalendrum_coreEncodeContext : CalendrumKtorBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/** Creates VAlarm values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreVAlarm.Companion")))
@interface CalendrumKtorCalendrum_coreVAlarmCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VAlarm values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVAlarmCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVAlarmBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VAlarm, or returns `null` when it is not a `VALARM` component. Nothing is copied or
 * validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVAlarm * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreAlarmAction : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreAlarmActionCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreProximity : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreProximityCompanion *companion __attribute__((swift_name("companion")));

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
@protocol CalendrumKtorCalendrum_coreTrigger
@required

/** All parameters of the property as written, including unknown ones. */
@property (readonly) NSArray<CalendrumKtorCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));
@end


/** Creates builders for [Attendee]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAttendee.Companion")))
@interface CalendrumKtorCalendrum_coreAttendeeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Attendee]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreAttendeeCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an attendee with the given calendar [address]. */
- (CalendrumKtorCalendrum_coreAttendeeBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
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
@interface CalendrumKtorCalendrum_coreCuType : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreCuTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_corePartStat : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_corePartStatCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreRole : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreRoleCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreClassificationCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Classification and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreClassificationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Classification holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreClassification *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CONFIDENTIAL`. The details are shown only to a limited set of people. */
@property (readonly) CalendrumKtorCalendrum_coreClassification *Confidential __attribute__((swift_name("Confidential")));

/** `PRIVATE`. Only the owner may see the details. */
@property (readonly) CalendrumKtorCalendrum_coreClassification *Private __attribute__((swift_name("Private")));

/** `PUBLIC`. Anyone may see the details; the default. */
@property (readonly) CalendrumKtorCalendrum_coreClassification *Public __attribute__((swift_name("Public")));
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
@interface CalendrumKtorCalendrum_coreConferenceFeature : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreConferenceFeatureCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreVLocationCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VLocation values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVLocationCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVLocationBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VLocation, or returns `null` when it is not a `VLOCATION` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVLocation * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
@end


/** Creates builders for [Organizer]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreOrganizer.Companion")))
@interface CalendrumKtorCalendrum_coreOrganizerCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [Organizer]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreOrganizerCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for an organizer with the given calendar [address]. */
- (CalendrumKtorCalendrum_coreOrganizerBuilder *)builderAddress:(NSString *)address __attribute__((swift_name("builder(address:)")));
@end


/** Creates Participant values and builders. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParticipant.Companion")))
@interface CalendrumKtorCalendrum_coreParticipantCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates Participant values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreParticipantCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder. A `UID` and a participant type are required. */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreParticipantBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a Participant, or returns `null` when it is not a `PARTICIPANT` component. Nothing is
 * copied or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreParticipant * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreParticipantType : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreParticipantTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreRelType : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreRelTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreVResourceCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates VResource values and builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreVResourceCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns an empty builder. A `UID` is required: set it, or give the builder [BuildDefaults] to generate it.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)builder __attribute__((swift_name("builder()")));

/**
 * Returns an empty builder that generates missing identity properties with [defaults].
 *
 * @param defaults the clock and the `UID` generator.
 */
- (CalendrumKtorCalendrum_coreVResourceBuilder *)builderDefaults:(CalendrumKtorCalendrum_coreBuildDefaults *)defaults __attribute__((swift_name("builder(defaults:)")));

/**
 * Wraps [component] as a VResource, or returns `null` when it is not a `VRESOURCE` component. Nothing is copied
 * or validated.
 *
 * @param component the component to wrap.
 */
- (CalendrumKtorCalendrum_coreVResource * _Nullable)ofComponent:(CalendrumKtorCalendrum_coreICalComponent *)component __attribute__((swift_name("of(component:)")));
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
@interface CalendrumKtorCalendrum_coreResourceType : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreResourceTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreRecurCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates rule builders. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreRecurCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder for a rule that repeats with the given [frequency]. */
- (CalendrumKtorCalendrum_coreRecurBuilder *)builderFrequency:(CalendrumKtorCalendrum_coreFrequency *)frequency __attribute__((swift_name("builder(frequency:)")));
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
@interface CalendrumKtorCalendrum_coreWeekdayNum : CalendrumKtorBase
- (instancetype)initWithOrdinal:(CalendrumKtorInt * _Nullable)ordinal dayOfWeek:(CalendrumKtorKotlinx_datetimeDayOfWeek *)dayOfWeek __attribute__((swift_name("init(ordinal:dayOfWeek:)"))) __attribute__((objc_designated_initializer));

/** Returns `true` when [other] has the same number and day of week. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the number and the day of week. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the entry as written in a rule, such as `MO` or `-1FR`. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) CalendrumKtorInt * _Nullable ordinal __attribute__((swift_name("ordinal")));
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
@interface CalendrumKtorCalendrum_coreMonthNum : CalendrumKtorBase
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
@interface CalendrumKtorCalendrum_coreSkip : CalendrumKtorKotlinEnum<CalendrumKtorCalendrum_coreSkip *>
+ (instancetype)alloc __attribute__((unavailable));

/**
 * The `SKIP` rule part of [RFC 7529](https://www.rfc-editor.org/rfc/rfc7529#section-4.1.1): what to do with an
 * occurrence that falls on a date that does not exist, such as the 31st in a month with 30 days.
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorCalendrum_coreSkip *omit __attribute__((swift_name("omit")));
@property (class, readonly) CalendrumKtorCalendrum_coreSkip *backward __attribute__((swift_name("backward")));
@property (class, readonly) CalendrumKtorCalendrum_coreSkip *forward __attribute__((swift_name("forward")));
+ (CalendrumKtorKotlinArray<CalendrumKtorCalendrum_coreSkip *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorCalendrum_coreSkip *> *entries __attribute__((swift_name("entries")));
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
@interface CalendrumKtorCalendrum_coreRecurPart : CalendrumKtorBase
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
@interface CalendrumKtorKotlinx_datetimeDayOfWeek : CalendrumKtorKotlinEnum<CalendrumKtorKotlinx_datetimeDayOfWeek *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *monday __attribute__((swift_name("monday")));
@property (class, readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *tuesday __attribute__((swift_name("tuesday")));
@property (class, readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *wednesday __attribute__((swift_name("wednesday")));
@property (class, readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *thursday __attribute__((swift_name("thursday")));
@property (class, readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *friday __attribute__((swift_name("friday")));
@property (class, readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *saturday __attribute__((swift_name("saturday")));
@property (class, readonly) CalendrumKtorKotlinx_datetimeDayOfWeek *sunday __attribute__((swift_name("sunday")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKotlinx_datetimeDayOfWeek *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKotlinx_datetimeDayOfWeek *> *entries __attribute__((swift_name("entries")));
@end


/** The constants of EventStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreEventStatus.Companion")))
@interface CalendrumKtorCalendrum_coreEventStatusCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of EventStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreEventStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new EventStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreEventStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The event is cancelled. */
@property (readonly) CalendrumKtorCalendrum_coreEventStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `CONFIRMED`. The event is confirmed. */
@property (readonly) CalendrumKtorCalendrum_coreEventStatus *Confirmed __attribute__((swift_name("Confirmed")));

/** `TENTATIVE`. The event is tentative. */
@property (readonly) CalendrumKtorCalendrum_coreEventStatus *Tentative __attribute__((swift_name("Tentative")));
@end


/** The constants of Transparency and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTransparency.Companion")))
@interface CalendrumKtorCalendrum_coreTransparencyCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Transparency and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreTransparencyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Transparency holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreTransparency *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `OPAQUE`. The event blocks time; the default. */
@property (readonly) CalendrumKtorCalendrum_coreTransparency *Opaque __attribute__((swift_name("Opaque")));

/** `TRANSPARENT`. The event does not block time. */
@property (readonly) CalendrumKtorCalendrum_coreTransparency *Transparent __attribute__((swift_name("Transparent")));
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
@interface CalendrumKtorCalendrum_coreFbType : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorCalendrum_coreFbTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumKtorCalendrum_coreImageDisplayCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ImageDisplay and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreImageDisplayCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ImageDisplay holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreImageDisplay *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BADGE`. A small image shown next to the title. */
@property (readonly) CalendrumKtorCalendrum_coreImageDisplay *Badge __attribute__((swift_name("Badge")));

/** `FULLSIZE`. A full-size version of an image. */
@property (readonly) CalendrumKtorCalendrum_coreImageDisplay *Fullsize __attribute__((swift_name("Fullsize")));

/** `GRAPHIC`. An image shown as a graphic with the content. */
@property (readonly) CalendrumKtorCalendrum_coreImageDisplay *Graphic __attribute__((swift_name("Graphic")));

/** `THUMBNAIL`. A reduced-size version of an image. */
@property (readonly) CalendrumKtorCalendrum_coreImageDisplay *Thumbnail __attribute__((swift_name("Thumbnail")));
@end


/** Creates parameters by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalParameter.Companion")))
@interface CalendrumKtorCalendrum_coreICalParameterCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates parameters by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreICalParameterCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates a parameter with the given [name] and [values].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @param values at least one value.
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumKtorCalendrum_coreICalParameter *)ofName:(NSString *)name values:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("of(name:values:)")));
@end


/** The constants of JournalStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreJournalStatus.Companion")))
@interface CalendrumKtorCalendrum_coreJournalStatusCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of JournalStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreJournalStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new JournalStatus holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreJournalStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The entry is cancelled. */
@property (readonly) CalendrumKtorCalendrum_coreJournalStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `DRAFT`. The entry is a draft. */
@property (readonly) CalendrumKtorCalendrum_coreJournalStatus *Draft __attribute__((swift_name("Draft")));

/** `FINAL`. The entry is final. */
@property (readonly) CalendrumKtorCalendrum_coreJournalStatus *Final __attribute__((swift_name("Final")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDateTimePeriod.Companion")))
@interface CalendrumKtorKotlinx_datetimeDateTimePeriodCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKotlinx_datetimeDateTimePeriodCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKotlinx_datetimeDateTimePeriod *)parseText:(NSString *)text __attribute__((swift_name("parse(text:)")));
- (id<CalendrumKtorKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/UtcOffsetSerializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset")))
@interface CalendrumKtorKotlinx_datetimeUtcOffset : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorKotlinx_datetimeUtcOffsetCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t totalSeconds __attribute__((swift_name("totalSeconds")));
@end


/** The constants of TodoStatus and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreTodoStatus.Companion")))
@interface CalendrumKtorCalendrum_coreTodoStatusCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of TodoStatus and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreTodoStatusCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new TodoStatus holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreTodoStatus *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CANCELLED`. The to-do is cancelled. */
@property (readonly) CalendrumKtorCalendrum_coreTodoStatus *Cancelled __attribute__((swift_name("Cancelled")));

/** `COMPLETED`. The to-do is completed. */
@property (readonly) CalendrumKtorCalendrum_coreTodoStatus *Completed __attribute__((swift_name("Completed")));

/** `IN-PROCESS`. The to-do is in progress. */
@property (readonly) CalendrumKtorCalendrum_coreTodoStatus *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The to-do needs action. */
@property (readonly) CalendrumKtorCalendrum_coreTodoStatus *NeedsAction __attribute__((swift_name("NeedsAction")));
@end


/** Collects the parameters and the value of a new [ICalProperty]. A builder is not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Builder")))
@interface CalendrumKtorCalendrum_coreICalPropertyBuilder : CalendrumKtorBase

/**
 * Creates the property.
 *
 * @throws IllegalArgumentException if the value contains a control character other than a tab.
 */
- (CalendrumKtorCalendrum_coreICalProperty *)build __attribute__((swift_name("build()")));

/** Appends [parameter] after the parameters added so far. */
- (CalendrumKtorCalendrum_coreICalPropertyBuilder *)parameterParameter:(CalendrumKtorCalendrum_coreICalParameter *)parameter __attribute__((swift_name("parameter(parameter:)")));

/**
 * Appends a parameter with the given [name] and [values].
 *
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumKtorCalendrum_coreICalPropertyBuilder *)parameterName:(NSString *)name values:(CalendrumKtorKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/**
 * Sets the value, which is written as is: escape `TEXT` yourself or use the typed model. The default is an
 * empty string.
 */
- (CalendrumKtorCalendrum_coreICalPropertyBuilder *)valueValue:(NSString *)value __attribute__((swift_name("value(value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntArray")))
@interface CalendrumKtorKotlinIntArray : CalendrumKtorBase
+ (instancetype)arrayWithSize:(int32_t)size __attribute__((swift_name("init(size:)")));
+ (instancetype)arrayWithSize:(int32_t)size init:(CalendrumKtorInt *(^)(CalendrumKtorInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (int32_t)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (CalendrumKtorKotlinIntIterator *)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(int32_t)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinInstant.Companion")))
@interface CalendrumKtorKotlinInstantCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKotlinInstantCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKotlinInstant *)fromEpochMillisecondsEpochMilliseconds:(int64_t)epochMilliseconds __attribute__((swift_name("fromEpochMilliseconds(epochMilliseconds:)")));
- (CalendrumKtorKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment:(int32_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment:)")));
- (CalendrumKtorKotlinInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment_:(int64_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment_:)")));
- (CalendrumKtorKotlinInstant *)now __attribute__((swift_name("now()"))) __attribute__((unavailable("Use Clock.System.now() instead")));
- (CalendrumKtorKotlinInstant *)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (CalendrumKtorKotlinInstant * _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@property (readonly) CalendrumKtorKotlinInstant *DISTANT_FUTURE __attribute__((swift_name("DISTANT_FUTURE")));
@property (readonly) CalendrumKtorKotlinInstant *DISTANT_PAST __attribute__((swift_name("DISTANT_PAST")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreApplicationCallPipeline.ApplicationPhase")))
@interface CalendrumKtorKtor_server_coreApplicationCallPipelineApplicationPhase : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)applicationPhase __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_server_coreApplicationCallPipelineApplicationPhase *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Call __attribute__((swift_name("Call")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Fallback __attribute__((swift_name("Fallback")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Features __attribute__((swift_name("Features"))) __attribute__((unavailable("Renamed to Plugins")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Monitoring __attribute__((swift_name("Monitoring")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Plugins __attribute__((swift_name("Plugins")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Setup __attribute__((swift_name("Setup")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationReceivePipeline")))
@interface CalendrumKtorKtor_server_coreApplicationReceivePipeline : CalendrumKtorKtor_utilsPipeline<id, id<CalendrumKtorKtor_server_corePipelineCall>>
- (instancetype)initWithDevelopmentMode:(BOOL)developmentMode __attribute__((swift_name("init(developmentMode:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithPhases:(CalendrumKtorKotlinArray<CalendrumKtorKtor_utilsPipelinePhase *> *)phases __attribute__((swift_name("init(phases:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithPhase:(CalendrumKtorKtor_utilsPipelinePhase *)phase interceptors:(NSArray<id<CalendrumKtorKotlinSuspendFunction2>> *)interceptors __attribute__((swift_name("init(phase:interceptors:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) CalendrumKtorKtor_server_coreApplicationReceivePipelinePhases *companion __attribute__((swift_name("companion")));
@property (readonly) BOOL developmentMode __attribute__((swift_name("developmentMode")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationSendPipeline")))
@interface CalendrumKtorKtor_server_coreApplicationSendPipeline : CalendrumKtorKtor_utilsPipeline<id, id<CalendrumKtorKtor_server_corePipelineCall>>
- (instancetype)initWithDevelopmentMode:(BOOL)developmentMode __attribute__((swift_name("init(developmentMode:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithPhases:(CalendrumKtorKotlinArray<CalendrumKtorKtor_utilsPipelinePhase *> *)phases __attribute__((swift_name("init(phases:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (instancetype)initWithPhase:(CalendrumKtorKtor_utilsPipelinePhase *)phase interceptors:(NSArray<id<CalendrumKtorKotlinSuspendFunction2>> *)interceptors __attribute__((swift_name("init(phase:interceptors:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) CalendrumKtorKtor_server_coreApplicationSendPipelinePhases *companion __attribute__((swift_name("companion")));
@property (readonly) BOOL developmentMode __attribute__((swift_name("developmentMode")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreApplication")))
@interface CalendrumKtorKtor_server_coreApplication : CalendrumKtorKtor_server_coreApplicationCallPipeline <CalendrumKtorKotlinx_coroutines_coreCoroutineScope>
- (instancetype)initWithDevelopmentMode:(BOOL)developmentMode environment:(id<CalendrumKtorKtor_server_coreApplicationEnvironment>)environment __attribute__((swift_name("init(developmentMode:environment:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (void)dispose __attribute__((swift_name("dispose()")));
@property (readonly) id<CalendrumKtorKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationEngine> engine __attribute__((swift_name("engine")));
@property (readonly) CalendrumKtorKtor_eventsEvents *monitor __attribute__((swift_name("monitor")));
@property (readonly) id<CalendrumKtorKotlinCoroutineContext> parentCoroutineContext __attribute__((swift_name("parentCoroutineContext")));
@property NSString *rootPath __attribute__((swift_name("rootPath")));
@end

__attribute__((swift_name("Ktor_httpParameters")))
@protocol CalendrumKtorKtor_httpParameters <CalendrumKtorKtor_utilsStringValues>
@required
@end

__attribute__((swift_name("Ktor_server_coreApplicationRequest")))
@protocol CalendrumKtorKtor_server_coreApplicationRequest
@required
- (id<CalendrumKtorKtor_ioByteReadChannel>)receiveChannel __attribute__((swift_name("receiveChannel()")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationCall> call __attribute__((swift_name("call")));
@property (readonly) CalendrumKtorKtor_server_coreRequestCookies *cookies __attribute__((swift_name("cookies")));
@property (readonly) id<CalendrumKtorKtor_httpHeaders> headers __attribute__((swift_name("headers")));
@property (readonly) id<CalendrumKtorKtor_httpRequestConnectionPoint> local __attribute__((swift_name("local")));
@property (readonly) id<CalendrumKtorKtor_httpParameters> queryParameters __attribute__((swift_name("queryParameters")));
@property (readonly) id<CalendrumKtorKtor_httpParameters> rawQueryParameters __attribute__((swift_name("rawQueryParameters")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationResponse")))
@protocol CalendrumKtorKtor_server_coreApplicationResponse
@required
- (void)pushBuilder:(id<CalendrumKtorKtor_server_coreResponsePushBuilder>)builder __attribute__((swift_name("push(builder:)")));
- (CalendrumKtorKtor_httpHttpStatusCode * _Nullable)status_ __attribute__((swift_name("status()")));
- (void)statusValue:(CalendrumKtorKtor_httpHttpStatusCode *)value __attribute__((swift_name("status(value:)")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationCall> call __attribute__((swift_name("call")));
@property (readonly) CalendrumKtorKtor_server_coreResponseCookies *cookies __attribute__((swift_name("cookies")));
@property (readonly) CalendrumKtorKtor_server_coreResponseHeaders *headers __attribute__((swift_name("headers")));
@property (readonly) BOOL isCommitted __attribute__((swift_name("isCommitted")));
@property (readonly) BOOL isSent __attribute__((swift_name("isSent")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.3")
*/
__attribute__((swift_name("KotlinCoroutineContext")))
@protocol CalendrumKtorKotlinCoroutineContext
@required
- (id _Nullable)foldInitial:(id _Nullable)initial operation:(id _Nullable (^)(id _Nullable, id<CalendrumKtorKotlinCoroutineContextElement>))operation __attribute__((swift_name("fold(initial:operation:)")));
- (id<CalendrumKtorKotlinCoroutineContextElement> _Nullable)getKey__:(id<CalendrumKtorKotlinCoroutineContextKey>)key __attribute__((swift_name("get(key__:)")));
- (id<CalendrumKtorKotlinCoroutineContext>)minusKeyKey:(id<CalendrumKtorKotlinCoroutineContextKey>)key __attribute__((swift_name("minusKey(key:)")));
- (id<CalendrumKtorKotlinCoroutineContext>)plusContext:(id<CalendrumKtorKotlinCoroutineContext>)context __attribute__((swift_name("plus(context:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreRoutingResolveResult.Success")))
@interface CalendrumKtorKtor_server_coreRoutingResolveResultSuccess : CalendrumKtorKtor_server_coreRoutingResolveResult
- (instancetype)initWithRoute:(CalendrumKtorKtor_server_coreRoutingNode *)route parameters:(id<CalendrumKtorKtor_httpParameters>)parameters __attribute__((swift_name("init(route:parameters:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable("This will become internal in future releases.")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumKtorKtor_httpParameters> parameters __attribute__((swift_name("parameters")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreRouteSelectorEvaluation.Failure")))
@interface CalendrumKtorKtor_server_coreRouteSelectorEvaluationFailure : CalendrumKtorKtor_server_coreRouteSelectorEvaluation
- (instancetype)initWithQuality:(double)quality failureStatusCode:(CalendrumKtorKtor_httpHttpStatusCode *)failureStatusCode __attribute__((swift_name("init(quality:failureStatusCode:)"))) __attribute__((objc_designated_initializer));
- (CalendrumKtorKtor_server_coreRouteSelectorEvaluationFailure *)doCopyQuality:(double)quality failureStatusCode:(CalendrumKtorKtor_httpHttpStatusCode *)failureStatusCode __attribute__((swift_name("doCopy(quality:failureStatusCode:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *failureStatusCode __attribute__((swift_name("failureStatusCode")));
@property (readonly) double quality __attribute__((swift_name("quality")));
@end


/** The features of [Selection.Builder.need]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFeature.Companion")))
@interface CalendrumKtorCalendrum_coreFeatureCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The features of [Selection.Builder.need]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreFeatureCompanion *shared __attribute__((swift_name("shared")));

/** The `VALARM` components, whole. */
@property (readonly) CalendrumKtorCalendrum_coreFeature *Alarms __attribute__((swift_name("Alarms")));

/**
 * Everything needed to compute occurrences: `DTSTART`, `DTEND`, `DURATION`, `RRULE`, `RDATE`, `EXDATE` and
 * `RECURRENCE-ID`. Time zones are then at least [TimeZoneSelection.Referenced].
 */
@property (readonly) CalendrumKtorCalendrum_coreFeature *Occurrences __attribute__((swift_name("Occurrences")));

/** The `ATTENDEE` and `ORGANIZER` properties. */
@property (readonly) CalendrumKtorCalendrum_coreFeature *Participants __attribute__((swift_name("Participants")));
@end


/** The two contexts. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext.Companion")))
@interface CalendrumKtorCalendrum_coreDecodeContextCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The two contexts. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreDecodeContextCompanion *shared __attribute__((swift_name("shared")));

/** Reads known quirks of real-world data with a warning and a repair. */
@property (readonly) CalendrumKtorCalendrum_coreDecodeContext *Lenient __attribute__((swift_name("Lenient")));

/** Turns every repair into an error: the value is rejected. */
@property (readonly) CalendrumKtorCalendrum_coreDecodeContext *Strict __attribute__((swift_name("Strict")));
@end


/** The constants of AlarmAction and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreAlarmAction.Companion")))
@interface CalendrumKtorCalendrum_coreAlarmActionCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of AlarmAction and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreAlarmActionCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new AlarmAction holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreAlarmAction *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Plays a sound. */
@property (readonly) CalendrumKtorCalendrum_coreAlarmAction *Audio __attribute__((swift_name("Audio")));

/** `DISPLAY`. Shows a message. */
@property (readonly) CalendrumKtorCalendrum_coreAlarmAction *Display __attribute__((swift_name("Display")));

/** `EMAIL`. Sends an e-mail. */
@property (readonly) CalendrumKtorCalendrum_coreAlarmAction *Email __attribute__((swift_name("Email")));
@end


/** The constants of Proximity and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProximity.Companion")))
@interface CalendrumKtorCalendrum_coreProximityCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Proximity and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreProximityCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Proximity holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreProximity *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ARRIVE`. Fires when the user arrives at the location. */
@property (readonly) CalendrumKtorCalendrum_coreProximity *Arrive __attribute__((swift_name("Arrive")));

/** `CONNECT`. Fires when the user's device connects, for example to a car. */
@property (readonly) CalendrumKtorCalendrum_coreProximity *Connect __attribute__((swift_name("Connect")));

/** `DEPART`. Fires when the user leaves the location. */
@property (readonly) CalendrumKtorCalendrum_coreProximity *Depart __attribute__((swift_name("Depart")));

/** `DISCONNECT`. Fires when the user's device disconnects. */
@property (readonly) CalendrumKtorCalendrum_coreProximity *Disconnect __attribute__((swift_name("Disconnect")));
@end


/** The constants of CuType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreCuType.Companion")))
@interface CalendrumKtorCalendrum_coreCuTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of CuType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreCuTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new CuType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreCuType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `GROUP`. A group of people. */
@property (readonly) CalendrumKtorCalendrum_coreCuType *Group __attribute__((swift_name("Group")));

/** `INDIVIDUAL`. A person; the default. */
@property (readonly) CalendrumKtorCalendrum_coreCuType *Individual __attribute__((swift_name("Individual")));

/** `RESOURCE`. A resource other than a room, such as a projector. */
@property (readonly) CalendrumKtorCalendrum_coreCuType *Resource __attribute__((swift_name("Resource")));

/** `ROOM`. A room. */
@property (readonly) CalendrumKtorCalendrum_coreCuType *Room __attribute__((swift_name("Room")));

/** `UNKNOWN`. The type is not known. */
@property (readonly) CalendrumKtorCalendrum_coreCuType *Unknown __attribute__((swift_name("Unknown")));
@end


/** The constants of PartStat and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_corePartStat.Companion")))
@interface CalendrumKtorCalendrum_corePartStatCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of PartStat and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_corePartStatCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new PartStat holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_corePartStat *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACCEPTED`. The user will attend. */
@property (readonly) CalendrumKtorCalendrum_corePartStat *Accepted __attribute__((swift_name("Accepted")));

/** `COMPLETED`. The user completed the task (to-dos). */
@property (readonly) CalendrumKtorCalendrum_corePartStat *Completed __attribute__((swift_name("Completed")));

/** `DECLINED`. The user will not attend. */
@property (readonly) CalendrumKtorCalendrum_corePartStat *Declined __attribute__((swift_name("Declined")));

/** `DELEGATED`. The user delegated attendance to someone else. */
@property (readonly) CalendrumKtorCalendrum_corePartStat *Delegated __attribute__((swift_name("Delegated")));

/** `IN-PROCESS`. The user is working on the task (to-dos). */
@property (readonly) CalendrumKtorCalendrum_corePartStat *InProcess __attribute__((swift_name("InProcess")));

/** `NEEDS-ACTION`. The user has not answered yet; the default. */
@property (readonly) CalendrumKtorCalendrum_corePartStat *NeedsAction __attribute__((swift_name("NeedsAction")));

/** `TENTATIVE`. The user may attend. */
@property (readonly) CalendrumKtorCalendrum_corePartStat *Tentative __attribute__((swift_name("Tentative")));
@end


/** The constants of Role and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRole.Companion")))
@interface CalendrumKtorCalendrum_coreRoleCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of Role and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreRoleCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new Role holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreRole *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHAIR`. The user chairs the event. */
@property (readonly) CalendrumKtorCalendrum_coreRole *Chair __attribute__((swift_name("Chair")));

/** `NON-PARTICIPANT`. Informed only; does not take part. */
@property (readonly) CalendrumKtorCalendrum_coreRole *NonParticipant __attribute__((swift_name("NonParticipant")));

/** `OPT-PARTICIPANT`. An optional participant. */
@property (readonly) CalendrumKtorCalendrum_coreRole *OptParticipant __attribute__((swift_name("OptParticipant")));

/** `REQ-PARTICIPANT`. A required participant; the default. */
@property (readonly) CalendrumKtorCalendrum_coreRole *ReqParticipant __attribute__((swift_name("ReqParticipant")));
@end


/** The constants of ConferenceFeature and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreConferenceFeature.Companion")))
@interface CalendrumKtorCalendrum_coreConferenceFeatureCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ConferenceFeature and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreConferenceFeatureCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ConferenceFeature holding the upper-cased token
 * when it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreConferenceFeature *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `AUDIO`. Audio conferencing. */
@property (readonly) CalendrumKtorCalendrum_coreConferenceFeature *Audio __attribute__((swift_name("Audio")));

/** `CHAT`. Chat or instant messaging. */
@property (readonly) CalendrumKtorCalendrum_coreConferenceFeature *Chat __attribute__((swift_name("Chat")));

/** `FEED`. A blog or atom feed. */
@property (readonly) CalendrumKtorCalendrum_coreConferenceFeature *Feed __attribute__((swift_name("Feed")));

/** `MODERATOR`. Moderator dial-in. */
@property (readonly) CalendrumKtorCalendrum_coreConferenceFeature *Moderator __attribute__((swift_name("Moderator")));

/** `PHONE`. Phone conferencing. */
@property (readonly) CalendrumKtorCalendrum_coreConferenceFeature *Phone __attribute__((swift_name("Phone")));

/** `SCREEN`. Screen sharing. */
@property (readonly) CalendrumKtorCalendrum_coreConferenceFeature *Screen __attribute__((swift_name("Screen")));

/** `VIDEO`. Video conferencing. */
@property (readonly) CalendrumKtorCalendrum_coreConferenceFeature *Video __attribute__((swift_name("Video")));
@end


/** The constants of ParticipantType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreParticipantType.Companion")))
@interface CalendrumKtorCalendrum_coreParticipantTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ParticipantType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreParticipantTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ParticipantType holding the upper-cased token when
 * it is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreParticipantType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `ACTIVE`. An active participant. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *Active __attribute__((swift_name("Active")));

/** `BOOKING-CONTACT`. A contact for bookings or reservations. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *BookingContact __attribute__((swift_name("BookingContact")));

/** `CONTACT`. A contact for information. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *Contact __attribute__((swift_name("Contact")));

/** `EMERGENCY-CONTACT`. A contact in an emergency. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *EmergencyContact __attribute__((swift_name("EmergencyContact")));

/** `INACTIVE`. An inactive participant. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *Inactive __attribute__((swift_name("Inactive")));

/** `PERFORMER`. A performer, such as a musician. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *Performer __attribute__((swift_name("Performer")));

/** `PLANNER-CONTACT`. A contact for the planning of the activity. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *PlannerContact __attribute__((swift_name("PlannerContact")));

/** `PUBLICITY-CONTACT`. A contact for publicity. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *PublicityContact __attribute__((swift_name("PublicityContact")));

/** `SPEAKER`. A speaker. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *Speaker __attribute__((swift_name("Speaker")));

/** `SPONSOR`. A sponsor of the activity. */
@property (readonly) CalendrumKtorCalendrum_coreParticipantType *Sponsor __attribute__((swift_name("Sponsor")));
@end


/** The constants of RelType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreRelType.Companion")))
@interface CalendrumKtorCalendrum_coreRelTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of RelType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreRelTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new RelType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreRelType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `CHILD`. The related component is a child of this one. */
@property (readonly) CalendrumKtorCalendrum_coreRelType *Child __attribute__((swift_name("Child")));

/** `CONCEPT`. The related value is a `CONCEPT` URI, not a `UID` (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *Concept __attribute__((swift_name("Concept")));

/** `DEPENDS-ON`. This component depends on the related one (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *DependsOn __attribute__((swift_name("DependsOn")));

/** `FINISHTOFINISH`. A finish-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *FinishToFinish __attribute__((swift_name("FinishToFinish")));

/** `FINISHTOSTART`. A finish-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *FinishToStart __attribute__((swift_name("FinishToStart")));

/** `FIRST`. The related component is the first of a series of components (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *First __attribute__((swift_name("First")));

/** `NEXT`. The related component is the next of a series of components (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *Next __attribute__((swift_name("Next")));

/** `PARENT`. The related component is the parent of this one; the default. */
@property (readonly) CalendrumKtorCalendrum_coreRelType *Parent __attribute__((swift_name("Parent")));

/** `REFID`. The related value is a `REFID` reference identifier, not a `UID` (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *RefId __attribute__((swift_name("RefId")));

/** `SIBLING`. The related component is a sibling of this one. */
@property (readonly) CalendrumKtorCalendrum_coreRelType *Sibling __attribute__((swift_name("Sibling")));

/** `SNOOZE`. This alarm is a snooze of the related alarm (RFC 9074). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *Snooze __attribute__((swift_name("Snooze")));

/** `STARTTOFINISH`. A start-to-finish dependency between the two components (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *StartToFinish __attribute__((swift_name("StartToFinish")));

/** `STARTTOSTART`. A start-to-start dependency between the two components (RFC 9253). */
@property (readonly) CalendrumKtorCalendrum_coreRelType *StartToStart __attribute__((swift_name("StartToStart")));
@end


/** The constants of ResourceType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreResourceType.Companion")))
@interface CalendrumKtorCalendrum_coreResourceTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ResourceType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreResourceTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ResourceType holding the upper-cased token when it
 * is not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreResourceType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `PROJECTOR`. A projector. */
@property (readonly) CalendrumKtorCalendrum_coreResourceType *Projector __attribute__((swift_name("Projector")));

/** `REMOTE-CONFERENCE-AUDIO`. A remote conference audio channel. */
@property (readonly) CalendrumKtorCalendrum_coreResourceType *RemoteConferenceAudio __attribute__((swift_name("RemoteConferenceAudio")));

/** `REMOTE-CONFERENCE-VIDEO`. A remote conference video channel. */
@property (readonly) CalendrumKtorCalendrum_coreResourceType *RemoteConferenceVideo __attribute__((swift_name("RemoteConferenceVideo")));

/** `ROOM`. A room. */
@property (readonly) CalendrumKtorCalendrum_coreResourceType *Room __attribute__((swift_name("Room")));
@end


/** The constants of FbType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreFbType.Companion")))
@interface CalendrumKtorCalendrum_coreFbTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of FbType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorCalendrum_coreFbTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new FbType holding the upper-cased token when it is not
 * one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumKtorCalendrum_coreFbType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BUSY`. The time is busy; the default. */
@property (readonly) CalendrumKtorCalendrum_coreFbType *Busy __attribute__((swift_name("Busy")));

/** `BUSY-TENTATIVE`. The time is tentatively busy. */
@property (readonly) CalendrumKtorCalendrum_coreFbType *BusyTentative __attribute__((swift_name("BusyTentative")));

/** `BUSY-UNAVAILABLE`. The time is busy and unavailable. */
@property (readonly) CalendrumKtorCalendrum_coreFbType *BusyUnavailable __attribute__((swift_name("BusyUnavailable")));

/** `FREE`. The time is free. */
@property (readonly) CalendrumKtorCalendrum_coreFbType *Free __attribute__((swift_name("Free")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol CalendrumKtorKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<CalendrumKtorKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol CalendrumKtorKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<CalendrumKtorKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol CalendrumKtorKotlinx_serialization_coreKSerializer <CalendrumKtorKotlinx_serialization_coreSerializationStrategy, CalendrumKtorKotlinx_serialization_coreDeserializationStrategy>
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeUtcOffset.Companion")))
@interface CalendrumKtorKotlinx_datetimeUtcOffsetCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKotlinx_datetimeUtcOffsetCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumKtorKotlinx_datetimeDateTimeFormat>)FormatBlock:(void (^)(id<CalendrumKtorKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset>))block __attribute__((swift_name("Format(block:)")));
- (CalendrumKtorKotlinx_datetimeUtcOffset *)parseInput:(id)input format:(id<CalendrumKtorKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<CalendrumKtorKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) CalendrumKtorKotlinx_datetimeUtcOffset *ZERO __attribute__((swift_name("ZERO")));
@end

__attribute__((swift_name("KotlinIntIterator")))
@interface CalendrumKtorKotlinIntIterator : CalendrumKtorBase <CalendrumKtorKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (CalendrumKtorInt *)next __attribute__((swift_name("next()")));
- (int32_t)nextInt __attribute__((swift_name("nextInt()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreApplicationReceivePipeline.Phases")))
@interface CalendrumKtorKtor_server_coreApplicationReceivePipelinePhases : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)phases __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_server_coreApplicationReceivePipelinePhases *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *After __attribute__((swift_name("After")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Before __attribute__((swift_name("Before")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Transform __attribute__((swift_name("Transform")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreApplicationSendPipeline.Phases")))
@interface CalendrumKtorKtor_server_coreApplicationSendPipelinePhases : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)phases __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_server_coreApplicationSendPipelinePhases *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *After __attribute__((swift_name("After")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Before __attribute__((swift_name("Before")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *ContentEncoding __attribute__((swift_name("ContentEncoding")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Engine __attribute__((swift_name("Engine")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Render __attribute__((swift_name("Render")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *TransferEncoding __attribute__((swift_name("TransferEncoding")));
@property (readonly) CalendrumKtorKtor_utilsPipelinePhase *Transform __attribute__((swift_name("Transform")));
@end

__attribute__((swift_name("Ktor_server_coreApplicationEngine")))
@protocol CalendrumKtorKtor_server_coreApplicationEngine
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)resolvedConnectorsWithCompletionHandler:(void (^)(NSArray<id<CalendrumKtorKtor_server_coreEngineConnectorConfig>> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("resolvedConnectors(completionHandler:)")));
- (id<CalendrumKtorKtor_server_coreApplicationEngine>)startWait:(BOOL)wait __attribute__((swift_name("start(wait:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)startSuspendWait:(BOOL)wait completionHandler:(void (^)(id<CalendrumKtorKtor_server_coreApplicationEngine> _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("startSuspend(wait:completionHandler:)")));
- (void)stopGracePeriodMillis:(int64_t)gracePeriodMillis timeoutMillis:(int64_t)timeoutMillis __attribute__((swift_name("stop(gracePeriodMillis:timeoutMillis:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)stopSuspendGracePeriodMillis:(int64_t)gracePeriodMillis timeoutMillis:(int64_t)timeoutMillis completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("stopSuspend(gracePeriodMillis:timeoutMillis:completionHandler:)")));
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationEnvironment> environment __attribute__((swift_name("environment")));
@end

__attribute__((swift_name("Ktor_server_coreRequestCookies")))
@interface CalendrumKtorKtor_server_coreRequestCookies : CalendrumKtorBase
- (instancetype)initWithRequest:(id<CalendrumKtorKtor_server_coreApplicationRequest>)request __attribute__((swift_name("init(request:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (NSDictionary<NSString *, NSString *> *)fetchCookies __attribute__((swift_name("fetchCookies()")));
- (NSString * _Nullable)getName:(NSString *)name encoding:(CalendrumKtorKtor_httpCookieEncoding *)encoding __attribute__((swift_name("get(name:encoding:)")));
@property (readonly) NSDictionary<NSString *, NSString *> *rawCookies __attribute__((swift_name("rawCookies")));

/**
 * @note This property has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
@property (readonly) id<CalendrumKtorKtor_server_coreApplicationRequest> request __attribute__((swift_name("request")));
@end

__attribute__((swift_name("Ktor_httpRequestConnectionPoint")))
@protocol CalendrumKtorKtor_httpRequestConnectionPoint
@required
@property (readonly) NSString *host __attribute__((swift_name("host"))) __attribute__((unavailable("Use localHost or serverHost instead")));
@property (readonly) NSString *localAddress __attribute__((swift_name("localAddress")));
@property (readonly) NSString *localHost __attribute__((swift_name("localHost")));
@property (readonly) int32_t localPort __attribute__((swift_name("localPort")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *method __attribute__((swift_name("method")));
@property (readonly) int32_t port __attribute__((swift_name("port"))) __attribute__((unavailable("Use localPort or serverPort instead")));
@property (readonly) NSString *remoteAddress __attribute__((swift_name("remoteAddress")));
@property (readonly) NSString *remoteHost __attribute__((swift_name("remoteHost")));
@property (readonly) int32_t remotePort __attribute__((swift_name("remotePort")));
@property (readonly) NSString *scheme __attribute__((swift_name("scheme")));
@property (readonly) NSString *serverHost __attribute__((swift_name("serverHost")));
@property (readonly) int32_t serverPort __attribute__((swift_name("serverPort")));
@property (readonly) NSString *uri __attribute__((swift_name("uri")));
@property (readonly) NSString *version __attribute__((swift_name("version")));
@end

__attribute__((swift_name("Ktor_server_coreResponsePushBuilder")))
@protocol CalendrumKtorKtor_server_coreResponsePushBuilder
@required
@property (readonly) CalendrumKtorKtor_httpHeadersBuilder *headers __attribute__((swift_name("headers")));
@property CalendrumKtorKtor_httpHttpMethod *method __attribute__((swift_name("method")));
@property (readonly) CalendrumKtorKtor_httpURLBuilder *url __attribute__((swift_name("url")));
@property (readonly) NSMutableArray<id<CalendrumKtorKtor_httpVersion>> *versions __attribute__((swift_name("versions")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreResponseCookies")))
@interface CalendrumKtorKtor_server_coreResponseCookies : CalendrumKtorBase
- (instancetype)initWithResponse:(id<CalendrumKtorKtor_server_corePipelineResponse>)response __attribute__((swift_name("init(response:)"))) __attribute__((objc_designated_initializer));
- (void)appendItem:(CalendrumKtorKtor_httpCookie *)item __attribute__((swift_name("append(item:)")));
- (void)appendName:(NSString *)name value:(NSString *)value encoding:(CalendrumKtorKtor_httpCookieEncoding *)encoding maxAge:(CalendrumKtorLong * _Nullable)maxAge expires:(CalendrumKtorKtor_utilsGMTDate * _Nullable)expires domain:(NSString * _Nullable)domain path:(NSString * _Nullable)path secure:(BOOL)secure httpOnly:(BOOL)httpOnly extensions:(NSDictionary<NSString *, id> *)extensions __attribute__((swift_name("append(name:value:encoding:maxAge:expires:domain:path:secure:httpOnly:extensions:)")));
- (void)appendExpiredName:(NSString *)name domain:(NSString * _Nullable)domain path:(NSString * _Nullable)path __attribute__((swift_name("appendExpired(name:domain:path:)"))) __attribute__((unavailable("This method doesn't bypass all flags and extensions so it will be removed in future major release. Please consider using append with expires parameter instead.")));
- (CalendrumKtorKtor_httpCookie * _Nullable)getName:(NSString *)name __attribute__((swift_name("get(name:)")));
@end

__attribute__((swift_name("Ktor_server_coreResponseHeaders")))
@interface CalendrumKtorKtor_server_coreResponseHeaders : CalendrumKtorBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (id<CalendrumKtorKtor_httpHeaders>)allValues __attribute__((swift_name("allValues()")));
- (void)appendName:(NSString *)name value:(NSString *)value safeOnly:(BOOL)safeOnly __attribute__((swift_name("append(name:value:safeOnly:)")));
- (BOOL)containsName:(NSString *)name __attribute__((swift_name("contains(name:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (void)engineAppendHeaderName:(NSString *)name value:(NSString *)value __attribute__((swift_name("engineAppendHeader(name:value:)")));
- (NSString * _Nullable)getName:(NSString *)name __attribute__((swift_name("get(name:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (NSArray<NSString *> *)getEngineHeaderNames __attribute__((swift_name("getEngineHeaderNames()")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (NSArray<NSString *> *)getEngineHeaderValuesName:(NSString *)name __attribute__((swift_name("getEngineHeaderValues(name:)")));
- (NSArray<NSString *> *)valuesName:(NSString *)name __attribute__((swift_name("values(name:)")));

/**
 * @note This property has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
@property (readonly) NSSet<NSString *> *managedByEngineHeaders __attribute__((swift_name("managedByEngineHeaders")));
@end

__attribute__((swift_name("KotlinCoroutineContextElement")))
@protocol CalendrumKtorKotlinCoroutineContextElement <CalendrumKtorKotlinCoroutineContext>
@required
@property (readonly) id<CalendrumKtorKotlinCoroutineContextKey> key __attribute__((swift_name("key")));
@end

__attribute__((swift_name("KotlinCoroutineContextKey")))
@protocol CalendrumKtorKotlinCoroutineContextKey
@required
@end

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol CalendrumKtorKotlinx_serialization_coreEncoder
@required
- (id<CalendrumKtorKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<CalendrumKtorKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<CalendrumKtorKotlinx_serialization_coreEncoder>)encodeInlineDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("encodeInline(descriptor:)")));
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
- (void)encodeNullableSerializableValueSerializer:(id<CalendrumKtorKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<CalendrumKtorKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) CalendrumKtorKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol CalendrumKtorKotlinx_serialization_coreSerialDescriptor
@required

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSArray<id<CalendrumKtorKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));

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
@property (readonly) NSArray<id<CalendrumKtorKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));

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
@property (readonly) CalendrumKtorKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol CalendrumKtorKotlinx_serialization_coreDecoder
@required
- (id<CalendrumKtorKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<CalendrumKtorKotlinx_serialization_coreDecoder>)decodeInlineDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeInline(descriptor:)")));
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
- (CalendrumKtorKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<CalendrumKtorKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<CalendrumKtorKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) CalendrumKtorKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormat")))
@protocol CalendrumKtorKotlinx_datetimeDateTimeFormat
@required
- (NSString *)formatValue:(id _Nullable)value __attribute__((swift_name("format(value:)")));
- (id<CalendrumKtorKotlinAppendable>)formatToAppendable:(id<CalendrumKtorKotlinAppendable>)appendable value:(id _Nullable)value __attribute__((swift_name("formatTo(appendable:value:)")));
- (id _Nullable)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (id _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilder")))
@protocol CalendrumKtorKotlinx_datetimeDateTimeFormatBuilder
@required
- (void)charsValue:(NSString *)value __attribute__((swift_name("chars(value:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithUtcOffset")))
@protocol CalendrumKtorKotlinx_datetimeDateTimeFormatBuilderWithUtcOffset <CalendrumKtorKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)offsetFormat:(id<CalendrumKtorKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("offset(format:)")));
- (void)offsetHoursPadding:(CalendrumKtorKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetHours(padding:)")));
- (void)offsetMinutesOfHourPadding:(CalendrumKtorKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetMinutesOfHour(padding:)")));
- (void)offsetSecondsOfMinutePadding:(CalendrumKtorKotlinx_datetimePadding *)padding __attribute__((swift_name("offsetSecondsOfMinute(padding:)")));
@end

__attribute__((swift_name("Ktor_server_coreEngineConnectorConfig")))
@protocol CalendrumKtorKtor_server_coreEngineConnectorConfig
@required
@property (readonly) NSString *host __attribute__((swift_name("host")));
@property (readonly) int32_t port __attribute__((swift_name("port")));
@property (readonly) CalendrumKtorKtor_server_coreConnectorType *type __attribute__((swift_name("type")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpCookieEncoding")))
@interface CalendrumKtorKtor_httpCookieEncoding : CalendrumKtorKotlinEnum<CalendrumKtorKtor_httpCookieEncoding *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorKtor_httpCookieEncoding *raw __attribute__((swift_name("raw")));
@property (class, readonly) CalendrumKtorKtor_httpCookieEncoding *dquotes __attribute__((swift_name("dquotes")));
@property (class, readonly) CalendrumKtorKtor_httpCookieEncoding *uriEncoding __attribute__((swift_name("uriEncoding")));
@property (class, readonly) CalendrumKtorKtor_httpCookieEncoding *base64Encoding __attribute__((swift_name("base64Encoding")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKtor_httpCookieEncoding *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKtor_httpCookieEncoding *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpHttpMethod")))
@interface CalendrumKtorKtor_httpHttpMethod : CalendrumKtorBase
- (instancetype)initWithValue:(NSString *)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpHttpMethodCompanion *companion __attribute__((swift_name("companion")));
- (CalendrumKtorKtor_httpHttpMethod *)doCopyValue:(NSString *)value __attribute__((swift_name("doCopy(value:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end

__attribute__((swift_name("Ktor_utilsStringValuesBuilder")))
@protocol CalendrumKtorKtor_utilsStringValuesBuilder
@required
- (void)appendName:(NSString *)name value:(NSString *)value __attribute__((swift_name("append(name:value:)")));
- (void)appendAllStringValues:(id<CalendrumKtorKtor_utilsStringValues>)stringValues __attribute__((swift_name("appendAll(stringValues:)")));
- (void)appendAllName:(NSString *)name values:(id)values __attribute__((swift_name("appendAll(name:values:)")));
- (void)appendMissingStringValues:(id<CalendrumKtorKtor_utilsStringValues>)stringValues __attribute__((swift_name("appendMissing(stringValues:)")));
- (void)appendMissingName:(NSString *)name values:(id)values __attribute__((swift_name("appendMissing(name:values:)")));
- (id<CalendrumKtorKtor_utilsStringValues>)build __attribute__((swift_name("build()")));
- (void)clear __attribute__((swift_name("clear()")));
- (BOOL)containsName:(NSString *)name __attribute__((swift_name("contains(name:)")));
- (BOOL)containsName:(NSString *)name value:(NSString *)value __attribute__((swift_name("contains(name:value:)")));
- (NSSet<id<CalendrumKtorKotlinMapEntry>> *)entries __attribute__((swift_name("entries()")));
- (NSString * _Nullable)getName:(NSString *)name __attribute__((swift_name("get(name:)")));
- (NSArray<NSString *> * _Nullable)getAllName:(NSString *)name __attribute__((swift_name("getAll(name:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (NSSet<NSString *> *)names __attribute__((swift_name("names()")));
- (void)removeName_:(NSString *)name __attribute__((swift_name("remove(name_:)")));
- (BOOL)removeName:(NSString *)name value:(NSString *)value __attribute__((swift_name("remove(name:value:)")));
- (void)removeKeysWithNoEntries __attribute__((swift_name("removeKeysWithNoEntries()")));
- (void)setName:(NSString *)name value:(NSString *)value __attribute__((swift_name("set(name:value:)")));
@property (readonly) BOOL caseInsensitiveName __attribute__((swift_name("caseInsensitiveName")));
@end

__attribute__((swift_name("Ktor_utilsStringValuesBuilderImpl")))
@interface CalendrumKtorKtor_utilsStringValuesBuilderImpl : CalendrumKtorBase <CalendrumKtorKtor_utilsStringValuesBuilder>
- (instancetype)initWithCaseInsensitiveName:(BOOL)caseInsensitiveName size:(int32_t)size __attribute__((swift_name("init(caseInsensitiveName:size:)"))) __attribute__((objc_designated_initializer));
- (void)appendName:(NSString *)name value:(NSString *)value __attribute__((swift_name("append(name:value:)")));
- (void)appendAllStringValues:(id<CalendrumKtorKtor_utilsStringValues>)stringValues __attribute__((swift_name("appendAll(stringValues:)")));
- (void)appendAllName:(NSString *)name values:(id)values __attribute__((swift_name("appendAll(name:values:)")));
- (void)appendMissingStringValues:(id<CalendrumKtorKtor_utilsStringValues>)stringValues __attribute__((swift_name("appendMissing(stringValues:)")));
- (void)appendMissingName:(NSString *)name values:(id)values __attribute__((swift_name("appendMissing(name:values:)")));
- (id<CalendrumKtorKtor_utilsStringValues>)build __attribute__((swift_name("build()")));
- (void)clear __attribute__((swift_name("clear()")));
- (BOOL)containsName:(NSString *)name __attribute__((swift_name("contains(name:)")));
- (BOOL)containsName:(NSString *)name value:(NSString *)value __attribute__((swift_name("contains(name:value:)")));
- (NSSet<id<CalendrumKtorKotlinMapEntry>> *)entries __attribute__((swift_name("entries()")));
- (NSString * _Nullable)getName:(NSString *)name __attribute__((swift_name("get(name:)")));
- (NSArray<NSString *> * _Nullable)getAllName:(NSString *)name __attribute__((swift_name("getAll(name:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (NSSet<NSString *> *)names __attribute__((swift_name("names()")));
- (void)removeName_:(NSString *)name __attribute__((swift_name("remove(name_:)")));
- (BOOL)removeName:(NSString *)name value:(NSString *)value __attribute__((swift_name("remove(name:value:)")));
- (void)removeKeysWithNoEntries __attribute__((swift_name("removeKeysWithNoEntries()")));
- (void)setName:(NSString *)name value:(NSString *)value __attribute__((swift_name("set(name:value:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (void)validateNameName:(NSString *)name __attribute__((swift_name("validateName(name:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (void)validateValueValue:(NSString *)value __attribute__((swift_name("validateValue(value:)")));
@property (readonly) BOOL caseInsensitiveName __attribute__((swift_name("caseInsensitiveName")));

/**
 * @note This property has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
@property (readonly) CalendrumKtorMutableDictionary<NSString *, NSMutableArray<NSString *> *> *values __attribute__((swift_name("values")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpHeadersBuilder")))
@interface CalendrumKtorKtor_httpHeadersBuilder : CalendrumKtorKtor_utilsStringValuesBuilderImpl
- (instancetype)initWithSize:(int32_t)size __attribute__((swift_name("init(size:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCaseInsensitiveName:(BOOL)caseInsensitiveName size:(int32_t)size __attribute__((swift_name("init(caseInsensitiveName:size:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
- (id<CalendrumKtorKtor_httpHeaders>)build __attribute__((swift_name("build()")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (void)validateNameName:(NSString *)name __attribute__((swift_name("validateName(name:)")));

/**
 * @note This method has protected visibility in Kotlin source and is intended only for use by subclasses.
*/
- (void)validateValueValue:(NSString *)value __attribute__((swift_name("validateValue(value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpURLBuilder")))
@interface CalendrumKtorKtor_httpURLBuilder : CalendrumKtorBase
- (instancetype)initWithProtocol:(CalendrumKtorKtor_httpURLProtocol * _Nullable)protocol host:(NSString *)host port:(int32_t)port user:(NSString * _Nullable)user password:(NSString * _Nullable)password pathSegments:(NSArray<NSString *> *)pathSegments parameters:(id<CalendrumKtorKtor_httpParameters>)parameters fragment:(NSString *)fragment trailingQuery:(BOOL)trailingQuery __attribute__((swift_name("init(protocol:host:port:user:password:pathSegments:parameters:fragment:trailingQuery:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpURLBuilderCompanion *companion __attribute__((swift_name("companion")));
- (CalendrumKtorKtor_httpUrl *)build __attribute__((swift_name("build()")));
- (NSString *)buildString __attribute__((swift_name("buildString()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString *encodedFragment __attribute__((swift_name("encodedFragment")));
@property id<CalendrumKtorKtor_httpParametersBuilder> encodedParameters __attribute__((swift_name("encodedParameters")));
@property NSString * _Nullable encodedPassword __attribute__((swift_name("encodedPassword")));
@property NSArray<NSString *> *encodedPathSegments __attribute__((swift_name("encodedPathSegments")));
@property NSString * _Nullable encodedUser __attribute__((swift_name("encodedUser")));
@property NSString *fragment __attribute__((swift_name("fragment")));
@property NSString *host __attribute__((swift_name("host")));
@property (readonly) id<CalendrumKtorKtor_httpParametersBuilder> parameters __attribute__((swift_name("parameters")));
@property NSString * _Nullable password __attribute__((swift_name("password")));
@property NSArray<NSString *> *pathSegments __attribute__((swift_name("pathSegments")));
@property int32_t port __attribute__((swift_name("port")));
@property CalendrumKtorKtor_httpURLProtocol *protocol __attribute__((swift_name("protocol")));
@property CalendrumKtorKtor_httpURLProtocol * _Nullable protocolOrNull __attribute__((swift_name("protocolOrNull")));
@property BOOL trailingQuery __attribute__((swift_name("trailingQuery")));
@property NSString * _Nullable user __attribute__((swift_name("user")));
@end

__attribute__((swift_name("Ktor_httpVersion")))
@protocol CalendrumKtorKtor_httpVersion
@required
- (void)appendHeadersToBuilder:(CalendrumKtorKtor_httpHeadersBuilder *)builder __attribute__((swift_name("appendHeadersTo(builder:)")));
- (CalendrumKtorKtor_httpVersionCheckResult *)checkRequestHeaders:(id<CalendrumKtorKtor_httpHeaders>)requestHeaders __attribute__((swift_name("check(requestHeaders:)")));
@end

__attribute__((swift_name("Ktor_server_corePipelineResponse")))
@protocol CalendrumKtorKtor_server_corePipelineResponse <CalendrumKtorKtor_server_coreApplicationResponse>
@required
@property (readonly) CalendrumKtorKtor_server_coreApplicationSendPipeline *pipeline __attribute__((swift_name("pipeline")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpCookie")))
@interface CalendrumKtorKtor_httpCookie : CalendrumKtorBase
- (instancetype)initWithName:(NSString *)name value:(NSString *)value encoding:(CalendrumKtorKtor_httpCookieEncoding *)encoding maxAge:(CalendrumKtorInt * _Nullable)maxAge expires:(CalendrumKtorKtor_utilsGMTDate * _Nullable)expires domain:(NSString * _Nullable)domain path:(NSString * _Nullable)path secure:(BOOL)secure httpOnly:(BOOL)httpOnly extensions:(NSDictionary<NSString *, id> *)extensions __attribute__((swift_name("init(name:value:encoding:maxAge:expires:domain:path:secure:httpOnly:extensions:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpCookieCompanion *companion __attribute__((swift_name("companion")));
- (CalendrumKtorKtor_httpCookie *)doCopyName:(NSString *)name value:(NSString *)value encoding:(CalendrumKtorKtor_httpCookieEncoding *)encoding maxAge:(CalendrumKtorInt * _Nullable)maxAge expires:(CalendrumKtorKtor_utilsGMTDate * _Nullable)expires domain:(NSString * _Nullable)domain path:(NSString * _Nullable)path secure:(BOOL)secure httpOnly:(BOOL)httpOnly extensions:(NSDictionary<NSString *, id> *)extensions __attribute__((swift_name("doCopy(name:value:encoding:maxAge:expires:domain:path:secure:httpOnly:extensions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable domain __attribute__((swift_name("domain")));
@property (readonly) CalendrumKtorKtor_httpCookieEncoding *encoding __attribute__((swift_name("encoding")));
@property (readonly) CalendrumKtorKtor_utilsGMTDate * _Nullable expires __attribute__((swift_name("expires")));
@property (readonly) NSDictionary<NSString *, id> *extensions __attribute__((swift_name("extensions")));
@property (readonly) BOOL httpOnly __attribute__((swift_name("httpOnly")));
@property (readonly) CalendrumKtorInt * _Nullable maxAge __attribute__((swift_name("maxAge")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString * _Nullable path __attribute__((swift_name("path")));
@property (readonly) BOOL secure __attribute__((swift_name("secure")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsGMTDate")))
@interface CalendrumKtorKtor_utilsGMTDate : CalendrumKtorBase <CalendrumKtorKotlinComparable>
- (instancetype)initWithSeconds:(int32_t)seconds minutes:(int32_t)minutes hours:(int32_t)hours dayOfWeek:(CalendrumKtorKtor_utilsWeekDay *)dayOfWeek dayOfMonth:(int32_t)dayOfMonth dayOfYear:(int32_t)dayOfYear month:(CalendrumKtorKtor_utilsMonth *)month year:(int32_t)year timestamp:(int64_t)timestamp __attribute__((swift_name("init(seconds:minutes:hours:dayOfWeek:dayOfMonth:dayOfYear:month:year:timestamp:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_utilsGMTDateCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(CalendrumKtorKtor_utilsGMTDate *)other __attribute__((swift_name("compareTo(other:)")));
- (CalendrumKtorKtor_utilsGMTDate *)doCopy __attribute__((swift_name("doCopy()")));
- (CalendrumKtorKtor_utilsGMTDate *)doCopySeconds:(int32_t)seconds minutes:(int32_t)minutes hours:(int32_t)hours dayOfWeek:(CalendrumKtorKtor_utilsWeekDay *)dayOfWeek dayOfMonth:(int32_t)dayOfMonth dayOfYear:(int32_t)dayOfYear month:(CalendrumKtorKtor_utilsMonth *)month year:(int32_t)year timestamp:(int64_t)timestamp __attribute__((swift_name("doCopy(seconds:minutes:hours:dayOfWeek:dayOfMonth:dayOfYear:month:year:timestamp:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t dayOfMonth __attribute__((swift_name("dayOfMonth")));
@property (readonly) CalendrumKtorKtor_utilsWeekDay *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) int32_t dayOfYear __attribute__((swift_name("dayOfYear")));
@property (readonly) int32_t hours __attribute__((swift_name("hours")));
@property (readonly) int32_t minutes __attribute__((swift_name("minutes")));
@property (readonly) CalendrumKtorKtor_utilsMonth *month __attribute__((swift_name("month")));
@property (readonly) int32_t seconds __attribute__((swift_name("seconds")));
@property (readonly) int64_t timestamp __attribute__((swift_name("timestamp")));
@property (readonly) int32_t year __attribute__((swift_name("year")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol CalendrumKtorKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<CalendrumKtorKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumKtorKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumKtorKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) CalendrumKtorKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface CalendrumKtorKotlinx_serialization_coreSerializersModule : CalendrumKtorBase

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)dumpToCollector:(id<CalendrumKtorKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumKtorKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<CalendrumKtorKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<CalendrumKtorKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumKtorKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumKtorKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumKtorKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumKtorKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end

__attribute__((swift_name("KotlinAnnotation")))
@protocol CalendrumKtorKotlinAnnotation
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface CalendrumKtorKotlinx_serialization_coreSerialKind : CalendrumKtorBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol CalendrumKtorKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<CalendrumKtorKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumKtorKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumKtorKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<CalendrumKtorKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) CalendrumKtorKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface CalendrumKtorKotlinNothing : CalendrumKtorBase
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimePadding")))
@interface CalendrumKtorKotlinx_datetimePadding : CalendrumKtorKotlinEnum<CalendrumKtorKotlinx_datetimePadding *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorKotlinx_datetimePadding *none __attribute__((swift_name("none")));
@property (class, readonly) CalendrumKtorKotlinx_datetimePadding *zero __attribute__((swift_name("zero")));
@property (class, readonly) CalendrumKtorKotlinx_datetimePadding *space __attribute__((swift_name("space")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKotlinx_datetimePadding *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKotlinx_datetimePadding *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreConnectorType")))
@interface CalendrumKtorKtor_server_coreConnectorType : CalendrumKtorBase
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_server_coreConnectorTypeCompanion *companion __attribute__((swift_name("companion")));
- (CalendrumKtorKtor_server_coreConnectorType *)doCopyName:(NSString *)name __attribute__((swift_name("doCopy(name:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpHttpMethod.Companion")))
@interface CalendrumKtorKtor_httpHttpMethodCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpHttpMethodCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_httpHttpMethod *)parseMethod:(NSString *)method __attribute__((swift_name("parse(method:)")));
@property (readonly) NSArray<CalendrumKtorKtor_httpHttpMethod *> *DefaultMethods __attribute__((swift_name("DefaultMethods")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *Delete __attribute__((swift_name("Delete")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *Get __attribute__((swift_name("Get")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *Head __attribute__((swift_name("Head")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *Options __attribute__((swift_name("Options")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *Patch __attribute__((swift_name("Patch")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *Post __attribute__((swift_name("Post")));
@property (readonly) CalendrumKtorKtor_httpHttpMethod *Put __attribute__((swift_name("Put")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpURLProtocol")))
@interface CalendrumKtorKtor_httpURLProtocol : CalendrumKtorBase
- (instancetype)initWithName:(NSString *)name defaultPort:(int32_t)defaultPort __attribute__((swift_name("init(name:defaultPort:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpURLProtocolCompanion *companion __attribute__((swift_name("companion")));
- (CalendrumKtorKtor_httpURLProtocol *)doCopyName:(NSString *)name defaultPort:(int32_t)defaultPort __attribute__((swift_name("doCopy(name:defaultPort:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t defaultPort __attribute__((swift_name("defaultPort")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpURLBuilder.Companion")))
@interface CalendrumKtorKtor_httpURLBuilderCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpURLBuilderCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpUrl")))
@interface CalendrumKtorKtor_httpUrl : CalendrumKtorBase
@property (class, readonly, getter=companion) CalendrumKtorKtor_httpUrlCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *encodedFragment __attribute__((swift_name("encodedFragment")));
@property (readonly) NSString * _Nullable encodedPassword __attribute__((swift_name("encodedPassword")));
@property (readonly) NSString *encodedPath __attribute__((swift_name("encodedPath")));
@property (readonly) NSString *encodedPathAndQuery __attribute__((swift_name("encodedPathAndQuery")));
@property (readonly) NSString *encodedQuery __attribute__((swift_name("encodedQuery")));
@property (readonly) NSString * _Nullable encodedUser __attribute__((swift_name("encodedUser")));
@property (readonly) NSString *fragment __attribute__((swift_name("fragment")));
@property (readonly) NSString *host __attribute__((swift_name("host")));
@property (readonly) id<CalendrumKtorKtor_httpParameters> parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString * _Nullable password __attribute__((swift_name("password")));
@property (readonly) NSArray<NSString *> *pathSegments __attribute__((swift_name("pathSegments"))) __attribute__((deprecated("\n        `pathSegments` is deprecated.\n\n        This property will contain an empty path segment at the beginning for URLs with a hostname,\n        and an empty path segment at the end for the URLs with a trailing slash. If you need to keep this behaviour please\n        use [rawSegments]. If you only need to access the meaningful parts of the path, consider using [segments] instead.\n             \n        Please decide if you need [rawSegments] or [segments] explicitly.\n        ")));
@property (readonly) int32_t port __attribute__((swift_name("port")));
@property (readonly) CalendrumKtorKtor_httpURLProtocol *protocol __attribute__((swift_name("protocol")));
@property (readonly) CalendrumKtorKtor_httpURLProtocol * _Nullable protocolOrNull __attribute__((swift_name("protocolOrNull")));
@property (readonly) NSArray<NSString *> *rawSegments __attribute__((swift_name("rawSegments")));
@property (readonly) NSArray<NSString *> *segments __attribute__((swift_name("segments")));
@property (readonly) int32_t specifiedPort __attribute__((swift_name("specifiedPort")));
@property (readonly) BOOL trailingQuery __attribute__((swift_name("trailingQuery")));
@property (readonly) NSString * _Nullable user __attribute__((swift_name("user")));
@end

__attribute__((swift_name("Ktor_httpParametersBuilder")))
@protocol CalendrumKtorKtor_httpParametersBuilder <CalendrumKtorKtor_utilsStringValuesBuilder>
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpVersionCheckResult")))
@interface CalendrumKtorKtor_httpVersionCheckResult : CalendrumKtorKotlinEnum<CalendrumKtorKtor_httpVersionCheckResult *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumKtorKtor_httpVersionCheckResult *ok __attribute__((swift_name("ok")));
@property (class, readonly) CalendrumKtorKtor_httpVersionCheckResult *notModified __attribute__((swift_name("notModified")));
@property (class, readonly) CalendrumKtorKtor_httpVersionCheckResult *preconditionFailed __attribute__((swift_name("preconditionFailed")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKtor_httpVersionCheckResult *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKtor_httpVersionCheckResult *> *entries __attribute__((swift_name("entries")));
@property (readonly) CalendrumKtorKtor_httpHttpStatusCode *statusCode __attribute__((swift_name("statusCode")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpCookie.Companion")))
@interface CalendrumKtorKtor_httpCookieCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpCookieCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumKtorKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsWeekDay")))
@interface CalendrumKtorKtor_utilsWeekDay : CalendrumKtorKotlinEnum<CalendrumKtorKtor_utilsWeekDay *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) CalendrumKtorKtor_utilsWeekDayCompanion *companion __attribute__((swift_name("companion")));
@property (class, readonly) CalendrumKtorKtor_utilsWeekDay *monday __attribute__((swift_name("monday")));
@property (class, readonly) CalendrumKtorKtor_utilsWeekDay *tuesday __attribute__((swift_name("tuesday")));
@property (class, readonly) CalendrumKtorKtor_utilsWeekDay *wednesday __attribute__((swift_name("wednesday")));
@property (class, readonly) CalendrumKtorKtor_utilsWeekDay *thursday __attribute__((swift_name("thursday")));
@property (class, readonly) CalendrumKtorKtor_utilsWeekDay *friday __attribute__((swift_name("friday")));
@property (class, readonly) CalendrumKtorKtor_utilsWeekDay *saturday __attribute__((swift_name("saturday")));
@property (class, readonly) CalendrumKtorKtor_utilsWeekDay *sunday __attribute__((swift_name("sunday")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKtor_utilsWeekDay *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKtor_utilsWeekDay *> *entries __attribute__((swift_name("entries")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsMonth")))
@interface CalendrumKtorKtor_utilsMonth : CalendrumKtorKotlinEnum<CalendrumKtorKtor_utilsMonth *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) CalendrumKtorKtor_utilsMonthCompanion *companion __attribute__((swift_name("companion")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *january __attribute__((swift_name("january")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *february __attribute__((swift_name("february")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *march __attribute__((swift_name("march")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *april __attribute__((swift_name("april")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *may __attribute__((swift_name("may")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *june __attribute__((swift_name("june")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *july __attribute__((swift_name("july")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *august __attribute__((swift_name("august")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *september __attribute__((swift_name("september")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *october __attribute__((swift_name("october")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *november __attribute__((swift_name("november")));
@property (class, readonly) CalendrumKtorKtor_utilsMonth *december __attribute__((swift_name("december")));
+ (CalendrumKtorKotlinArray<CalendrumKtorKtor_utilsMonth *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumKtorKtor_utilsMonth *> *entries __attribute__((swift_name("entries")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsGMTDate.Companion")))
@interface CalendrumKtorKtor_utilsGMTDateCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_utilsGMTDateCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumKtorKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) CalendrumKtorKtor_utilsGMTDate *START __attribute__((swift_name("START")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol CalendrumKtorKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<CalendrumKtorKotlinKClass>)kClass provider:(id<CalendrumKtorKotlinx_serialization_coreKSerializer> (^)(NSArray<id<CalendrumKtorKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<CalendrumKtorKotlinKClass>)kClass serializer:(id<CalendrumKtorKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<CalendrumKtorKotlinKClass>)baseClass actualClass:(id<CalendrumKtorKotlinKClass>)actualClass actualSerializer:(id<CalendrumKtorKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<CalendrumKtorKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumKtorKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultDeserializerProvider:)"))) __attribute__((deprecated("Deprecated in favor of function with more precise name: polymorphicDefaultDeserializer")));
- (void)polymorphicDefaultDeserializerBaseClass:(id<CalendrumKtorKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumKtorKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefaultDeserializer(baseClass:defaultDeserializerProvider:)")));
- (void)polymorphicDefaultSerializerBaseClass:(id<CalendrumKtorKotlinKClass>)baseClass defaultSerializerProvider:(id<CalendrumKtorKotlinx_serialization_coreSerializationStrategy> _Nullable (^)(id))defaultSerializerProvider __attribute__((swift_name("polymorphicDefaultSerializer(baseClass:defaultSerializerProvider:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_server_coreConnectorType.Companion")))
@interface CalendrumKtorKtor_server_coreConnectorTypeCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_server_coreConnectorTypeCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) CalendrumKtorKtor_server_coreConnectorType *HTTP __attribute__((swift_name("HTTP")));
@property (readonly) CalendrumKtorKtor_server_coreConnectorType *HTTPS __attribute__((swift_name("HTTPS")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpURLProtocol.Companion")))
@interface CalendrumKtorKtor_httpURLProtocolCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpURLProtocolCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_httpURLProtocol *)createOrDefaultName:(NSString *)name __attribute__((swift_name("createOrDefault(name:)")));
@property (readonly) CalendrumKtorKtor_httpURLProtocol *HTTP __attribute__((swift_name("HTTP")));
@property (readonly) CalendrumKtorKtor_httpURLProtocol *HTTPS __attribute__((swift_name("HTTPS")));
@property (readonly) CalendrumKtorKtor_httpURLProtocol *SOCKS __attribute__((swift_name("SOCKS")));
@property (readonly) CalendrumKtorKtor_httpURLProtocol *WS __attribute__((swift_name("WS")));
@property (readonly) CalendrumKtorKtor_httpURLProtocol *WSS __attribute__((swift_name("WSS")));
@property (readonly) NSDictionary<NSString *, CalendrumKtorKtor_httpURLProtocol *> *byName __attribute__((swift_name("byName")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_httpUrl.Companion")))
@interface CalendrumKtorKtor_httpUrlCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_httpUrlCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsWeekDay.Companion")))
@interface CalendrumKtorKtor_utilsWeekDayCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_utilsWeekDayCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_utilsWeekDay *)fromOrdinal:(int32_t)ordinal __attribute__((swift_name("from(ordinal:)")));
- (CalendrumKtorKtor_utilsWeekDay *)fromValue:(NSString *)value __attribute__((swift_name("from(value:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Ktor_utilsMonth.Companion")))
@interface CalendrumKtorKtor_utilsMonthCompanion : CalendrumKtorBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumKtorKtor_utilsMonthCompanion *shared __attribute__((swift_name("shared")));
- (CalendrumKtorKtor_utilsMonth *)fromOrdinal:(int32_t)ordinal __attribute__((swift_name("from(ordinal:)")));
- (CalendrumKtorKtor_utilsMonth *)fromValue:(NSString *)value __attribute__((swift_name("from(value:)")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END

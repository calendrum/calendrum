#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class CalendrumJcalCalendrum_coreCardinality, CalendrumJcalCalendrum_coreComponentDefinition, CalendrumJcalCalendrum_coreDecodeContext, CalendrumJcalCalendrum_coreDecodeContextCompanion, CalendrumJcalCalendrum_coreEncodeContext, CalendrumJcalCalendrum_coreICalComponent, CalendrumJcalCalendrum_coreICalComponentBuilder, CalendrumJcalCalendrum_coreICalComponentCompanion, CalendrumJcalCalendrum_coreICalParameter, CalendrumJcalCalendrum_coreICalParameterCompanion, CalendrumJcalCalendrum_coreICalProperty, CalendrumJcalCalendrum_coreICalPropertyBuilder, CalendrumJcalCalendrum_coreICalPropertyCompanion, CalendrumJcalCalendrum_coreICalRegistry, CalendrumJcalCalendrum_coreICalRegistryBuilder, CalendrumJcalCalendrum_coreICalRegistryCompanion, CalendrumJcalCalendrum_coreParameterDefinition, CalendrumJcalCalendrum_coreParameterKey<T>, CalendrumJcalCalendrum_coreProblem, CalendrumJcalCalendrum_coreProblemCode, CalendrumJcalCalendrum_coreProblemCodeCompanion, CalendrumJcalCalendrum_corePropertyDefinition, CalendrumJcalCalendrum_corePropertyKey<T>, CalendrumJcalCalendrum_coreSeverity, CalendrumJcalCalendrum_coreSourceSpan, CalendrumJcalCalendrum_coreValueType, CalendrumJcalCalendrum_coreValueTypeCompanion, CalendrumJcalICalSerializers, CalendrumJcalJCal, CalendrumJcalJCalOptions, CalendrumJcalJCalOptionsBuilder, CalendrumJcalJCalOptionsCompanion, CalendrumJcalJCalResult, CalendrumJcalKotlinArray<T>, CalendrumJcalKotlinEnum<E>, CalendrumJcalKotlinEnumCompanion, CalendrumJcalKotlinException, CalendrumJcalKotlinNothing, CalendrumJcalKotlinRuntimeException, CalendrumJcalKotlinThrowable, CalendrumJcalKotlinx_serialization_coreSerialKind, CalendrumJcalKotlinx_serialization_coreSerializersModule, CalendrumJcalKotlinx_serialization_jsonJsonElement, CalendrumJcalKotlinx_serialization_jsonJsonElementCompanion;

@protocol CalendrumJcalCalendrum_coreDecoded, CalendrumJcalCalendrum_coreICalExtension, CalendrumJcalCalendrum_coreParameterCodec, CalendrumJcalCalendrum_corePropertyCodec, CalendrumJcalKotlinAnnotation, CalendrumJcalKotlinComparable, CalendrumJcalKotlinIterator, CalendrumJcalKotlinKAnnotatedElement, CalendrumJcalKotlinKClass, CalendrumJcalKotlinKClassifier, CalendrumJcalKotlinKDeclarationContainer, CalendrumJcalKotlinx_serialization_coreCompositeDecoder, CalendrumJcalKotlinx_serialization_coreCompositeEncoder, CalendrumJcalKotlinx_serialization_coreDecoder, CalendrumJcalKotlinx_serialization_coreDeserializationStrategy, CalendrumJcalKotlinx_serialization_coreEncoder, CalendrumJcalKotlinx_serialization_coreKSerializer, CalendrumJcalKotlinx_serialization_coreSerialDescriptor, CalendrumJcalKotlinx_serialization_coreSerializationStrategy, CalendrumJcalKotlinx_serialization_coreSerializersModuleCollector;

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
@interface CalendrumJcalBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface CalendrumJcalBase (CalendrumJcalBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface CalendrumJcalMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface CalendrumJcalMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorCalendrumJcalKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface CalendrumJcalNumber : NSNumber
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
@interface CalendrumJcalByte : CalendrumJcalNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface CalendrumJcalUByte : CalendrumJcalNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface CalendrumJcalShort : CalendrumJcalNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface CalendrumJcalUShort : CalendrumJcalNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface CalendrumJcalInt : CalendrumJcalNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface CalendrumJcalUInt : CalendrumJcalNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface CalendrumJcalLong : CalendrumJcalNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface CalendrumJcalULong : CalendrumJcalNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface CalendrumJcalFloat : CalendrumJcalNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface CalendrumJcalDouble : CalendrumJcalNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface CalendrumJcalBoolean : CalendrumJcalNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end


/**
 * Serializers for kotlinx-serialization that read and write the values of the library as their jCal text, without a
 * compiler plugin or reflection.
 *
 * Use them explicitly: `Json.encodeToString(ICalSerializers.dateTime, start)`. Values are single JSON strings, except
 * [component], which is the jCal array of a component and works only with the JSON format.
 *
 * @sample io.github.calendrum.samples.jcalSerializersForKotlinxSerialization
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ICalSerializers")))
@interface CalendrumJcalICalSerializers : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Serializers for kotlinx-serialization that read and write the values of the library as their jCal text, without a
 * compiler plugin or reflection.
 *
 * Use them explicitly: `Json.encodeToString(ICalSerializers.dateTime, start)`. Values are single JSON strings, except
 * [component], which is the jCal array of a component and works only with the JSON format.
 *
 * @sample io.github.calendrum.samples.jcalSerializersForKotlinxSerialization
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)iCalSerializers __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalICalSerializers *shared __attribute__((swift_name("shared")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * Serializes a component as its jCal array (`["vevent", [...properties], [...components]]`). It works only with the
 * JSON format; encoding throws `IllegalArgumentException` on what [JCal.encodeToString] cannot write (a control
 * character, a parameter without a value), and decoding fails with a `SerializationException` when the input is not
 * a valid jCal component.
 */
@property (readonly) id<CalendrumJcalKotlinx_serialization_coreKSerializer> component __attribute__((swift_name("component")));

/**
 * Serializes a date or date-time as its jCal string: `2026-10-01`, `2026-10-01T10:00:00` (floating) or
 * `2026-10-01T07:00:00Z` (UTC). A zoned value gets its zone in brackets, `2026-10-01T10:00:00[Europe/Berlin]`, so
 * no value is lost (the bracket form is this library's extension; standard jCal puts the zone in the `tzid`
 * parameter).
 */
@property (readonly) id<CalendrumJcalKotlinx_serialization_coreKSerializer> dateTime __attribute__((swift_name("dateTime")));

/** Serializes a duration as its iCalendar text, such as `PT1H30M`. */
@property (readonly) id<CalendrumJcalKotlinx_serialization_coreKSerializer> duration __attribute__((swift_name("duration")));

/**
 * Serializes a period as `start/end` or `start/duration`, with the start and the end in the form of [dateTime].
 */
@property (readonly) id<CalendrumJcalKotlinx_serialization_coreKSerializer> period __attribute__((swift_name("period")));

/** Serializes a recurrence rule as its iCalendar text, such as `FREQ=WEEKLY;COUNT=10`. */
@property (readonly) id<CalendrumJcalKotlinx_serialization_coreKSerializer> recur __attribute__((swift_name("recur")));
@end


/**
 * Converts component trees to and from jCal, the JSON form of iCalendar
 * ([RFC 7265](https://www.rfc-editor.org/rfc/rfc7265)).
 *
 * Values get the JSON types of their iCalendar types, taken from the registry of [JCalOptions]; a value that does not
 * decode as its type is written with the type `unknown` as it is, and a `VALUE` type that jCal does not define, such as
 * `X-FOO`, with that name in lower case. Writing follows canonical writing and throws on what it
 * rejects (a control character, a parameter without a value), so whatever is written reads back. Reading never throws
 * on data: what cannot be read is skipped and reported as a problem of the `ICAL-J` series in [JCalResult.problems].
 *
 * @sample io.github.calendrum.samples.jcalRoundTrip
 * @sample io.github.calendrum.samples.jcalProblemsInsteadOfExceptions
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JCal")))
@interface CalendrumJcalJCal : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/**
 * Converts component trees to and from jCal, the JSON form of iCalendar
 * ([RFC 7265](https://www.rfc-editor.org/rfc/rfc7265)).
 *
 * Values get the JSON types of their iCalendar types, taken from the registry of [JCalOptions]; a value that does not
 * decode as its type is written with the type `unknown` as it is, and a `VALUE` type that jCal does not define, such as
 * `X-FOO`, with that name in lower case. Writing follows canonical writing and throws on what it
 * rejects (a control character, a parameter without a value), so whatever is written reads back. Reading never throws
 * on data: what cannot be read is skipped and reported as a problem of the `ICAL-J` series in [JCalResult.problems].
 *
 * @sample io.github.calendrum.samples.jcalRoundTrip
 * @sample io.github.calendrum.samples.jcalProblemsInsteadOfExceptions
 */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)jCal __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalJCal *shared __attribute__((swift_name("shared")));

/**
 * Reads jCal from an already parsed JSON [element], like [decodeFromString].
 *
 * @param element a JSON array of components, or a single component.
 * @param options the registry that tells the type of every property.
 */
- (CalendrumJcalJCalResult *)decodeFromJsonElementElement:(CalendrumJcalKotlinx_serialization_jsonJsonElement *)element options:(CalendrumJcalJCalOptions *)options __attribute__((swift_name("decodeFromJsonElement(element:options:)")));

/**
 * Reads jCal from [text]. Malformed parts are skipped and reported; input that is not JSON gives `ICAL-J001` and no
 * components, and JSON nested deeper than a jCal of `JCalOptions.maxDepth` components can be (twice the depth and
 * eight more arrays or objects) gives `ICAL-J007` and no components.
 *
 * @param text a JSON array of components, or a single component.
 * @param options the registry that tells the type of every property.
 */
- (CalendrumJcalJCalResult *)decodeFromStringText:(NSString *)text options:(CalendrumJcalJCalOptions *)options __attribute__((swift_name("decodeFromString(text:options:)")));

/**
 * Writes [components] as a jCal `JsonArray` for use with kotlinx-serialization-json.
 *
 * @param components the components to write, usually one `VCALENDAR`.
 * @param options the registry that tells the type of every property.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written, as in [encodeToString].
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (CalendrumJcalKotlinx_serialization_jsonJsonElement * _Nullable)encodeToJsonElementComponents:(NSArray<CalendrumJcalCalendrum_coreICalComponent *> *)components options:(CalendrumJcalJCalOptions *)options error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToJsonElement(components:options:)")));

/**
 * Writes [components] as a jCal JSON array (compact, with no whitespace) and returns the text.
 *
 * @param components the components to write, usually one `VCALENDAR`.
 * @param options the registry that tells the type of every property.
 * @throws IllegalArgumentException if a name, value or parameter cannot be written, as in canonical writing: a name
 *   that is not a token, a control character other than HTAB, or a parameter without a value; or if a `VALUE`
 *   parameter is not one type name. Parsing keeps them, but jCal could not read them back.
 * @sample io.github.calendrum.samples.jcalRoundTrip
 *
 * @note This method converts instances of IllegalArgumentException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (NSString * _Nullable)encodeToStringComponents:(NSArray<CalendrumJcalCalendrum_coreICalComponent *> *)components options:(CalendrumJcalJCalOptions *)options error:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeToString(components:options:)")));

/** Returns the name of the object. */
- (NSString *)description __attribute__((swift_name("description()")));
@end


/**
 * Settings of jCal reading and writing: the registry that tells the type of every property and the deepest nesting
 * that reading accepts. Immutable.
 *
 * @property registry the registry; [ICalRegistry.Default] unless set. Install your own `X-` properties to give them
 *   types.
 * @property maxDepth the deepest nesting of components that reading accepts, the calendar being 1; 16 unless set, as
 *   `ParseLimits.maxDepth` of the text form. A deeper component is skipped with `ICAL-J007`, so hostile JSON nested
 *   thousands of levels deep cannot exhaust the stack.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JCalOptions")))
@interface CalendrumJcalJCalOptions : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalJCalOptionsCompanion *companion __attribute__((swift_name("companion")));

/** Returns the registry and the depth limit. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t maxDepth __attribute__((swift_name("maxDepth")));
@property (readonly) CalendrumJcalCalendrum_coreICalRegistry *registry __attribute__((swift_name("registry")));
@end


/** Collects [JCalOptions] values. Not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JCalOptions.Builder")))
@interface CalendrumJcalJCalOptionsBuilder : CalendrumJcalBase

/**
 * Creates the options.
 *
 * @throws IllegalArgumentException if [maxDepth] is not positive.
 */
- (CalendrumJcalJCalOptions *)build __attribute__((swift_name("build()")));

/** Sets [maxDepth] to [value] and returns this builder. */
- (CalendrumJcalJCalOptionsBuilder *)maxDepthValue:(int32_t)value __attribute__((swift_name("maxDepth(value:)")));

/** Sets [registry] to [value] and returns this builder. */
- (CalendrumJcalJCalOptionsBuilder *)registryValue:(CalendrumJcalCalendrum_coreICalRegistry *)value __attribute__((swift_name("registry(value:)")));

/** Returns the name of the builder. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The deepest nesting of components that reading accepts; positive. The default is 16. */
@property int32_t maxDepth __attribute__((swift_name("maxDepth")));

/** The registry that tells the type of every property; the default registry by default. */
@property CalendrumJcalCalendrum_coreICalRegistry *registry __attribute__((swift_name("registry")));
@end


/** The default options and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JCalOptions.Companion")))
@interface CalendrumJcalJCalOptionsCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default options and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalJCalOptionsCompanion *shared __attribute__((swift_name("shared")));

/** Returns a builder that starts from the default options. */
- (CalendrumJcalJCalOptionsBuilder *)builder __attribute__((swift_name("builder()")));

/** Options with the default registry and a nesting depth of 16. */
@property (readonly) CalendrumJcalJCalOptions *Default __attribute__((swift_name("Default")));
@end


/**
 * The result of reading jCal.
 *
 * @property components the components that could be read, as a lossless tree.
 * @property problems what was skipped or repaired, with `ICAL-J` codes. The text of a problem names the element in JSON
 *   (such as `$[0][1][3]`); there is no source position in JSON text.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JCalResult")))
@interface CalendrumJcalJCalResult : CalendrumJcalBase

/** Returns the number of components and problems. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<CalendrumJcalCalendrum_coreICalComponent *> *components __attribute__((swift_name("components")));
@property (readonly) NSArray<CalendrumJcalCalendrum_coreProblem *> *problems __attribute__((swift_name("problems")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol CalendrumJcalKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<CalendrumJcalKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol CalendrumJcalKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<CalendrumJcalKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol CalendrumJcalKotlinx_serialization_coreKSerializer <CalendrumJcalKotlinx_serialization_coreSerializationStrategy, CalendrumJcalKotlinx_serialization_coreDeserializationStrategy>
@required
@end

__attribute__((swift_name("KotlinThrowable")))
@interface CalendrumJcalKotlinThrowable : CalendrumJcalBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (CalendrumJcalKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumJcalKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface CalendrumJcalKotlinException : CalendrumJcalKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface CalendrumJcalKotlinRuntimeException : CalendrumJcalKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalArgumentException")))
@interface CalendrumJcalKotlinIllegalArgumentException : CalendrumJcalKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(CalendrumJcalKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/serialization/json/JsonElementSerializer))
*/
__attribute__((swift_name("Kotlinx_serialization_jsonJsonElement")))
@interface CalendrumJcalKotlinx_serialization_jsonJsonElement : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalKotlinx_serialization_jsonJsonElementCompanion *companion __attribute__((swift_name("companion")));
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
@interface CalendrumJcalCalendrum_coreICalComponent : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalCalendrum_coreICalComponentCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the directly nested components named [name], ignoring ASCII case, in source order; the list is empty when
 * there are none. Deeper levels are not searched.
 *
 * @param name a component name such as `VEVENT`.
 */
- (NSArray<CalendrumJcalCalendrum_coreICalComponent *> *)componentsName:(NSString *)name __attribute__((swift_name("components(name:)")));

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
- (NSArray<CalendrumJcalCalendrum_coreICalProperty *> *)propertiesName:(NSString *)name __attribute__((swift_name("properties(name:)")));

/**
 * Returns the first property named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a property name such as `SUMMARY`.
 */
- (CalendrumJcalCalendrum_coreICalProperty * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns the name and the number of properties and nested components; never a value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The directly nested components, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumJcalCalendrum_coreICalComponent *> *components __attribute__((swift_name("components")));

/**
 * The canonical name in upper case, such as `VEVENT`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/**
 * The properties of this component, in the order they appear. The list is a read-only view; it is empty for none.
 */
@property (readonly) NSArray<CalendrumJcalCalendrum_coreICalProperty *> *properties __attribute__((swift_name("properties")));

/**
 * Where the component starts in the input and where its `END` line ends, or `null` for a component that was built
 * by hand or edited.
 */
@property (readonly) CalendrumJcalCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));
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
@interface CalendrumJcalCalendrum_coreICalRegistry : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalCalendrum_coreICalRegistryCompanion *companion __attribute__((swift_name("companion")));

/**
 * Returns the definition of the component [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumJcalCalendrum_coreComponentDefinition * _Nullable)componentName:(NSString *)name __attribute__((swift_name("component(name:)")));

/**
 * Returns the definition of the parameter [name], ignoring ASCII case, or `null` when the registry does not know
 * it.
 */
- (CalendrumJcalCalendrum_coreParameterDefinition * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/**
 * Returns the definition of the property [name], ignoring ASCII case, or `null` when the registry does not know it.
 * When several components define the property differently, the first definition is returned.
 */
- (CalendrumJcalCalendrum_corePropertyDefinition * _Nullable)propertyName:(NSString *)name __attribute__((swift_name("property(name:)")));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<CalendrumJcalCalendrum_coreICalExtension>> *extensions __attribute__((swift_name("extensions")));
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
@interface CalendrumJcalCalendrum_coreProblem : CalendrumJcalBase

/**
 * Returns the code, severity, message and position, such as `ICAL-S003 Warning: bare LF used as a line ending at
 * 3:7 (offset 12)`. It never contains values from the input.
 */
- (NSString *)description __attribute__((swift_name("description()")));

/** The stable code of the problem, such as `ICAL-S003`. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *code __attribute__((swift_name("code")));

/**
 * A short human-readable text: the fixed message of the code, followed by an explanation after a colon when the
 * code has one (for example the name of a detected charset). It never contains data from the input.
 */
@property (readonly) NSString *message __attribute__((swift_name("message")));

/** The severity of [code]. */
@property (readonly) CalendrumJcalCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));

/**
 * Where in the input the problem was found; a point, so [SourceSpan.startOffset] equals [SourceSpan.endOffset].
 *
 * Nodes built by hand or edited by an interceptor have no position in the original input; see [SourceSpan].
 */
@property (readonly) CalendrumJcalCalendrum_coreSourceSpan *span __attribute__((swift_name("span")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol CalendrumJcalKotlinx_serialization_coreEncoder
@required
- (id<CalendrumJcalKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<CalendrumJcalKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<CalendrumJcalKotlinx_serialization_coreEncoder>)encodeInlineDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("encodeInline(descriptor:)")));
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
- (void)encodeNullableSerializableValueSerializer:(id<CalendrumJcalKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<CalendrumJcalKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) CalendrumJcalKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol CalendrumJcalKotlinx_serialization_coreSerialDescriptor
@required

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSArray<id<CalendrumJcalKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));

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
@property (readonly) NSArray<id<CalendrumJcalKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));

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
@property (readonly) CalendrumJcalKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol CalendrumJcalKotlinx_serialization_coreDecoder
@required
- (id<CalendrumJcalKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<CalendrumJcalKotlinx_serialization_coreDecoder>)decodeInlineDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeInline(descriptor:)")));
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
- (CalendrumJcalKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<CalendrumJcalKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<CalendrumJcalKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) CalendrumJcalKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface CalendrumJcalKotlinArray<T> : CalendrumJcalBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(CalendrumJcalInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<CalendrumJcalKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_serialization_jsonJsonElement.Companion")))
@interface CalendrumJcalKotlinx_serialization_jsonJsonElementCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalKotlinx_serialization_jsonJsonElementCompanion *shared __attribute__((swift_name("shared")));
- (id<CalendrumJcalKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/** Creates builders for [ICalComponent]. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalComponent.Companion")))
@interface CalendrumJcalCalendrum_coreICalComponentCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates builders for [ICalComponent]. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalCalendrum_coreICalComponentCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a component named [name].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @throws IllegalArgumentException if [name] is empty or has another character.
 */
- (CalendrumJcalCalendrum_coreICalComponentBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));
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
@interface CalendrumJcalCalendrum_coreICalProperty : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalCalendrum_coreICalPropertyCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a property with the same name, parameters and value. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the name, the parameters and the value. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/**
 * Returns the first parameter named [name], ignoring ASCII case, or `null` when there is none.
 *
 * @param name a parameter name such as `TZID`.
 */
- (CalendrumJcalCalendrum_coreICalParameter * _Nullable)parameterName:(NSString *)name __attribute__((swift_name("parameter(name:)")));

/** Returns the name and the number of parameters; never the value. */
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * The canonical name in upper case, such as `DTSTART`; the names the standard defines share one string instance.
 */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The parameters in source order. The list is empty for none. */
@property (readonly) NSArray<CalendrumJcalCalendrum_coreICalParameter *> *parameters __attribute__((swift_name("parameters")));

/**
 * Where the property is in the input, or `null` for a property that was built by hand. For input that was changed
 * by an interceptor it is the position of the original line.
 */
@property (readonly) CalendrumJcalCalendrum_coreSourceSpan * _Nullable span __attribute__((swift_name("span")));

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
@interface CalendrumJcalCalendrum_coreSourceSpan : CalendrumJcalBase

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


/** The default registry and a builder. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalRegistry.Companion")))
@interface CalendrumJcalCalendrum_coreICalRegistryCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** The default registry and a builder. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalCalendrum_coreICalRegistryCompanion *shared __attribute__((swift_name("shared")));

/** Returns an empty builder; install [Rfc5545] and the other extensions you need.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (CalendrumJcalCalendrum_coreICalRegistryBuilder *)builder __attribute__((swift_name("builder()")));

/** The registry with RFC 5545, RFC 7986, RFC 9073, RFC 9074 and RFC 9253; created on first use. */
@property (readonly) CalendrumJcalCalendrum_coreICalRegistry *Default __attribute__((swift_name("Default")));
@end


/**
 * What the registry knows about a component. Immutable.
 *
 * @property source the standard or vendor that defines it.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreComponentDefinition")))
@interface CalendrumJcalCalendrum_coreComponentDefinition : CalendrumJcalBase
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
@interface CalendrumJcalCalendrum_coreParameterDefinition : CalendrumJcalBase
- (instancetype)initWithKey:(CalendrumJcalCalendrum_coreParameterKey<id> *)key source:(NSString *)source __attribute__((swift_name("init(key:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) CalendrumJcalCalendrum_coreParameterKey<id> *key __attribute__((swift_name("key")));

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
@interface CalendrumJcalCalendrum_corePropertyDefinition : CalendrumJcalBase
- (instancetype)initWithKey:(CalendrumJcalCalendrum_corePropertyKey<id> *)key defaultType:(CalendrumJcalCalendrum_coreValueType *)defaultType allowedTypes:(NSSet<CalendrumJcalCalendrum_coreValueType *> *)allowedTypes parameters:(NSSet<NSString *> *)parameters cardinality:(NSDictionary<NSString *, CalendrumJcalCalendrum_coreCardinality *> *)cardinality source:(NSString *)source __attribute__((swift_name("init(key:defaultType:allowedTypes:parameters:cardinality:source:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The value types the property may have, including the default one. */
@property (readonly) NSSet<CalendrumJcalCalendrum_coreValueType *> *allowedTypes __attribute__((swift_name("allowedTypes")));

/** How often the property occurs in each component, by component name. */
@property (readonly) NSDictionary<NSString *, CalendrumJcalCalendrum_coreCardinality *> *cardinality __attribute__((swift_name("cardinality")));
@property (readonly) CalendrumJcalCalendrum_coreValueType *defaultType __attribute__((swift_name("defaultType")));
@property (readonly) CalendrumJcalCalendrum_corePropertyKey<id> *key __attribute__((swift_name("key")));

/** The canonical name of the property. */
@property (readonly) NSString *name __attribute__((swift_name("name")));

/** The names of the parameters that apply to the property. */
@property (readonly) NSSet<NSString *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSString *source __attribute__((swift_name("source")));
@end


/** A set of definitions that can be installed into a registry, such as a standard or a vendor's properties. */
__attribute__((swift_name("Calendrum_coreICalExtension")))
@protocol CalendrumJcalCalendrum_coreICalExtension
@required

/** Adds this extension's definitions to [registry].
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
- (void)contributeRegistry:(CalendrumJcalCalendrum_coreICalRegistryBuilder *)registry __attribute__((swift_name("contribute(registry:)")));
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
@interface CalendrumJcalCalendrum_coreProblemCode : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalCalendrum_coreProblemCodeCompanion *companion __attribute__((swift_name("companion")));

/** Returns `true` when [other] is a code with the same [id]. */
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));

/** Returns a hash code over the [id]. */
- (NSUInteger)hash __attribute__((swift_name("hash()")));

/** Returns the [id], such as `ICAL-S003`. */
- (NSString *)description __attribute__((swift_name("description()")));

/** The identifier of the code, for example `ICAL-S003`. */
@property (readonly) NSString *id __attribute__((swift_name("id")));

/** How serious the problems with this code are. */
@property (readonly) CalendrumJcalCalendrum_coreSeverity *severity __attribute__((swift_name("severity")));
@end

__attribute__((swift_name("KotlinComparable")))
@protocol CalendrumJcalKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface CalendrumJcalKotlinEnum<E> : CalendrumJcalBase <CalendrumJcalKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) CalendrumJcalKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end


/** How serious a [Problem] is. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreSeverity")))
@interface CalendrumJcalCalendrum_coreSeverity : CalendrumJcalKotlinEnum<CalendrumJcalCalendrum_coreSeverity *>
+ (instancetype)alloc __attribute__((unavailable));

/** How serious a [Problem] is. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumJcalCalendrum_coreSeverity *info __attribute__((swift_name("info")));
@property (class, readonly) CalendrumJcalCalendrum_coreSeverity *warning __attribute__((swift_name("warning")));
@property (class, readonly) CalendrumJcalCalendrum_coreSeverity *error __attribute__((swift_name("error")));
+ (CalendrumJcalKotlinArray<CalendrumJcalCalendrum_coreSeverity *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumJcalCalendrum_coreSeverity *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol CalendrumJcalKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<CalendrumJcalKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumJcalKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<CalendrumJcalKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) CalendrumJcalKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface CalendrumJcalKotlinx_serialization_coreSerializersModule : CalendrumJcalBase

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)dumpToCollector:(id<CalendrumJcalKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumJcalKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<CalendrumJcalKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<CalendrumJcalKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumJcalKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumJcalKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<CalendrumJcalKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<CalendrumJcalKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end

__attribute__((swift_name("KotlinAnnotation")))
@protocol CalendrumJcalKotlinAnnotation
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface CalendrumJcalKotlinx_serialization_coreSerialKind : CalendrumJcalBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol CalendrumJcalKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<CalendrumJcalKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumJcalKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<CalendrumJcalKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<CalendrumJcalKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) CalendrumJcalKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface CalendrumJcalKotlinNothing : CalendrumJcalBase
@end

__attribute__((swift_name("KotlinIterator")))
@protocol CalendrumJcalKotlinIterator
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
@interface CalendrumJcalCalendrum_coreICalComponentBuilder : CalendrumJcalBase

/** Creates the component. The builder can be reused: later changes do not affect components already built. */
- (CalendrumJcalCalendrum_coreICalComponent *)build __attribute__((swift_name("build()")));

/** Appends [component] after the nested components added so far. */
- (CalendrumJcalCalendrum_coreICalComponentBuilder *)componentComponent:(CalendrumJcalCalendrum_coreICalComponent *)component __attribute__((swift_name("component(component:)")));

/**
 * Appends a nested component built by [block].
 *
 * @param name the name of the nested component.
 * @param block fills the builder of the nested component.
 * @throws IllegalArgumentException if [name] is not a valid component name.
 */
- (CalendrumJcalCalendrum_coreICalComponentBuilder *)componentName:(NSString *)name block:(void (^)(CalendrumJcalCalendrum_coreICalComponentBuilder *))block __attribute__((swift_name("component(name:block:)")));

/** Appends [property] after the properties added so far. */
- (CalendrumJcalCalendrum_coreICalComponentBuilder *)propertyProperty:(CalendrumJcalCalendrum_coreICalProperty *)property __attribute__((swift_name("property(property:)")));

/**
 * Appends a property with the given [name] and [value] and no parameters.
 *
 * @throws IllegalArgumentException if [name] is not a valid property name, or [value] contains a control
 *   character other than a tab.
 */
- (CalendrumJcalCalendrum_coreICalComponentBuilder *)propertyName:(NSString *)name value:(NSString *)value __attribute__((swift_name("property(name:value:)")));
@end


/** Creates properties by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Companion")))
@interface CalendrumJcalCalendrum_coreICalPropertyCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates properties by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalCalendrum_coreICalPropertyCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns a builder for a property named [name].
 *
 * @param name letters, digits and hyphens only; `BEGIN` and `END` are not allowed.
 * @throws IllegalArgumentException if [name] is invalid.
 */
- (CalendrumJcalCalendrum_coreICalPropertyBuilder *)builderName:(NSString *)name __attribute__((swift_name("builder(name:)")));

/**
 * Creates a property with no parameters.
 *
 * @param name letters, digits and hyphens only; the stored name is upper case; `BEGIN` and `END` are not
 *   allowed.
 * @param value the value as it is written; a control character other than a tab is not allowed.
 * @throws IllegalArgumentException if [name] or [value] is invalid.
 */
- (CalendrumJcalCalendrum_coreICalProperty *)ofName:(NSString *)name value:(NSString *)value __attribute__((swift_name("of(name:value:)")));
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
@interface CalendrumJcalCalendrum_coreICalParameter : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalCalendrum_coreICalParameterCompanion *companion __attribute__((swift_name("companion")));

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


/** Collects definitions and extensions into an [ICalRegistry]. Not thread-safe.
 *
 * @note annotations
 *   io.github.calendrum.ExperimentalICalApi
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalRegistry.Builder")))
@interface CalendrumJcalCalendrum_coreICalRegistryBuilder : CalendrumJcalBase

/** Creates the registry. */
- (CalendrumJcalCalendrum_coreICalRegistry *)build __attribute__((swift_name("build()")));

/** Adds a component definition; the children of a definition for the same name are merged. */
- (CalendrumJcalCalendrum_coreICalRegistryBuilder *)componentDefinition:(CalendrumJcalCalendrum_coreComponentDefinition *)definition __attribute__((swift_name("component(definition:)")));

/**
 * Installs [extension] once: a second install of the same extension does nothing. The extension adds its
 * definitions to this builder.
 */
- (CalendrumJcalCalendrum_coreICalRegistryBuilder *)installExtension:(id<CalendrumJcalCalendrum_coreICalExtension>)extension __attribute__((swift_name("install(extension:)")));

/** Adds a parameter definition, replacing a definition of another key for the same name. */
- (CalendrumJcalCalendrum_coreICalRegistryBuilder *)parameterDefinition:(CalendrumJcalCalendrum_coreParameterDefinition *)definition __attribute__((swift_name("parameter(definition:)")));

/**
 * Adds a property definition. A definition for a key that is already defined is merged into it; one for the
 * same name in other components is kept beside it.
 */
- (CalendrumJcalCalendrum_coreICalRegistryBuilder *)propertyDefinition:(CalendrumJcalCalendrum_corePropertyDefinition *)definition __attribute__((swift_name("property(definition:)")));

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
@interface CalendrumJcalCalendrum_coreParameterKey<T> : CalendrumJcalBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumJcalCalendrum_coreParameterCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumJcalCalendrum_coreParameterCodec> codec __attribute__((swift_name("codec")));

/** The canonical parameter name in upper case. */
@property (readonly) NSString *name __attribute__((swift_name("name")));
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
@interface CalendrumJcalCalendrum_corePropertyKey<T> : CalendrumJcalBase
- (instancetype)initWithName:(NSString *)name codec:(id<CalendrumJcalCalendrum_corePropertyCodec>)codec __attribute__((swift_name("init(name:codec:)"))) __attribute__((objc_designated_initializer));

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<CalendrumJcalCalendrum_corePropertyCodec> codec __attribute__((swift_name("codec")));

/** The canonical property name in upper case. */
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
@interface CalendrumJcalCalendrum_coreValueType : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalCalendrum_coreValueTypeCompanion *companion __attribute__((swift_name("companion")));

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
@interface CalendrumJcalCalendrum_coreCardinality : CalendrumJcalKotlinEnum<CalendrumJcalCalendrum_coreCardinality *>
+ (instancetype)alloc __attribute__((unavailable));

/** How many times a property may occur in a component. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) CalendrumJcalCalendrum_coreCardinality *one __attribute__((swift_name("one")));
@property (class, readonly) CalendrumJcalCalendrum_coreCardinality *optional __attribute__((swift_name("optional")));
@property (class, readonly) CalendrumJcalCalendrum_coreCardinality *many __attribute__((swift_name("many")));
@property (class, readonly) CalendrumJcalCalendrum_coreCardinality *oneormore __attribute__((swift_name("oneormore")));
+ (CalendrumJcalKotlinArray<CalendrumJcalCalendrum_coreCardinality *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<CalendrumJcalCalendrum_coreCardinality *> *entries __attribute__((swift_name("entries")));
@end


/** The constants for every code of the library, and a lookup by identifier. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreProblemCode.Companion")))
@interface CalendrumJcalCalendrum_coreProblemCodeCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants for every code of the library, and a lookup by identifier. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalCalendrum_coreProblemCodeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the code with the given [id], ignoring case.
 *
 * For an identifier the library knows this is the constant; for any other a new code is created with the
 * upper-case identifier and the severity [Severity.Warning].
 *
 * @throws IllegalArgumentException if [id] is empty.
 */
- (CalendrumJcalCalendrum_coreProblemCode *)ofId:(NSString *)id __attribute__((swift_name("of(id:)")));

/**
 * `ICAL-O003` (Warning): the `REPEAT` of an alarm is beyond the limit of alarm repetitions. The extra
 * repetitions are dropped.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *AlarmRepeatLimit __attribute__((swift_name("AlarmRepeatLimit")));

/**
 * `ICAL-L022` (Warning): a `TZID` is not an IANA zone but a Windows name, a display name, a link or a vendor
 * prefix. Found by the linter, once per `TZID`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *AliasTimeZoneId __attribute__((swift_name("AliasTimeZoneId")));

/**
 * `ICAL-S002` (Warning): a bare CR is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *BareCr __attribute__((swift_name("BareCr")));

/**
 * `ICAL-S003` (Warning): a bare LF is used as a line ending. It is accepted as one. Reported once per input;
 * the rest are only counted.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *BareLf __attribute__((swift_name("BareLf")));

/** `ICAL-V015` (Warning): a `BINARY` value without base64 padding. The padding is added. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *Base64Padding __attribute__((swift_name("Base64Padding")));

/** `ICAL-V014` (Warning): whitespace inside a `BINARY` value. It is skipped. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *Base64Whitespace __attribute__((swift_name("Base64Whitespace")));

/**
 * `ICAL-S004` (Warning): a blank line, which the format does not allow. The line is skipped. Reported once per
 * input.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *BlankLine __attribute__((swift_name("BlankLine")));

/**
 * `ICAL-S027` (Warning): the charset hint contradicts the data, for example a byte order mark of another
 * encoding or clear UTF-8 data under a single-byte hint. The data decides.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *CharsetHintContradictsData __attribute__((swift_name("CharsetHintContradictsData")));

/**
 * `ICAL-L006` (Error): a component inside a parent that does not allow it, or that the iTIP method forbids.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ComponentNotAllowed __attribute__((swift_name("ComponentNotAllowed")));

/** `ICAL-S020` (Warning): `BEGIN` or `END` has parameters. They are ignored. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ComponentParameters __attribute__((swift_name("ComponentParameters")));

/**
 * `ICAL-S005` (Warning): the input starts with a continuation line, a space or a tab in the first byte. That
 * one character is removed and the line starts the first logical line.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ContinuationAtStart __attribute__((swift_name("ContinuationAtStart")));

/**
 * `ICAL-L026` (Error): a content line holds a control character other than horizontal tab, in its name, a
 * parameter or its value, which [RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1) forbids.
 * Parsing keeps the line as it is and byte-stable writing writes it back, but canonical writing, jCal and the
 * builders refuse it. Found by the validator and the linter; the explanation never quotes the line.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ControlCharacter __attribute__((swift_name("ControlCharacter")));

/**
 * `ICAL-V017` (Warning): a recurrence rule has both `COUNT` and `UNTIL`, which the standard forbids. Both are
 * kept; the position is that of the second.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *CountAndUntil __attribute__((swift_name("CountAndUntil")));

/**
 * `ICAL-V008` (Warning): a `DATE-TIME` value has no time, as in `DTSTART:20260901` without `VALUE=DATE`. It is
 * read as a `DATE`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *DateAsDateTime __attribute__((swift_name("DateAsDateTime")));

/** `ICAL-V007` (Warning): a `DATE` value has a time. The time is dropped. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *DateWithTime __attribute__((swift_name("DateWithTime")));

/** `ICAL-L012` (Error): a `DURATION` with hours, minutes or seconds beside a `DTSTART` of type `DATE`. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *DateWithTimeDuration __attribute__((swift_name("DateWithTimeDuration")));

/**
 * `ICAL-S025` (Warning): the encoding was guessed from the data, by a heuristic or by the placement of zero
 * bytes, and the input is transcoded from there on. The explanation names the encoding and the confidence in
 * percent.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *DetectedCharset __attribute__((swift_name("DetectedCharset")));

/**
 * `ICAL-V010` (Warning): a `DURATION` has hours and seconds but no minutes, such as `PT1H30S`. The missing `0M`
 * is added.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *DurationSkipsMinutes __attribute__((swift_name("DurationSkipsMinutes")));

/**
 * `ICAL-V009` (Warning): a `DURATION` has time units without the `T` designator, such as `P90M`. It is read as
 * `PT90M`, since the standard has no months in durations.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *DurationWithoutTimeDesignator __attribute__((swift_name("DurationWithoutTimeDesignator")));

/**
 * `ICAL-V016` (Warning): an empty list item, as in `a,,b`, or an empty rule part, as in `FREQ=DAILY;`. It is
 * skipped.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *EmptyItem __attribute__((swift_name("EmptyItem")));

/**
 * `ICAL-L025` (Warning): an empty value of a non-text property, such as `DTEND:` or `RRULE:`. Found by the
 * linter.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *EmptyValue __attribute__((swift_name("EmptyValue")));

/**
 * `ICAL-L011` (Error): `DTEND` or `DUE` is not later than `DTSTART`. Not checked when a zone does not resolve.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *EndBeforeStart __attribute__((swift_name("EndBeforeStart")));

/**
 * `ICAL-S016` (Warning): an `END` closes a component that is open deeper in the stack than the top. The
 * components in between are closed implicitly, then the matching one. One problem per `END` line.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *EndClosesNested __attribute__((swift_name("EndClosesNested")));

/** `ICAL-S015` (Warning): an `END` line with no open component. The line is ignored. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *EndWithoutBegin __attribute__((swift_name("EndWithoutBegin")));

/** `ICAL-L008` (Error): properties that exclude each other occur together, such as `DTEND` and `DURATION`. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ExclusiveProperties __attribute__((swift_name("ExclusiveProperties")));

/**
 * `ICAL-V005` (Warning): a date or time is written in the extended ISO 8601 form, with separators. It is read
 * as the basic form; in strict mode it is an error.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ExtendedIsoForm __attribute__((swift_name("ExtendedIsoForm")));

/**
 * `ICAL-O004` (Warning): a floating time, a `DATE` or an unresolved `TZID` was met while no floating zone is
 * set. The occurrence is skipped.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *FloatingWithoutZone __attribute__((swift_name("FloatingWithoutZone")));

/**
 * `ICAL-R009` (Info): an `RDATE` or `EXDATE` is in another form than `DTSTART`, such as UTC against a time
 * zone. They are compared by local time.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *FormMismatch __attribute__((swift_name("FormMismatch")));

/**
 * `ICAL-V006` (Warning): a time has fractions of a second, as written by JavaScript generators. The fraction is
 * dropped.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *FractionalSeconds __attribute__((swift_name("FractionalSeconds")));

/**
 * `ICAL-S030` (Error): the input is longer than [io.github.calendrum.syntax.ParseLimits.maxInputOctets]. Reading
 * stops before the first logical line that does not end within that many bytes, and the result is truncated.
 * Always reported.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *InputTooLarge __attribute__((swift_name("InputTooLarge")));

/**
 * `ICAL-S019` (Warning): the name in `BEGIN` or `END` is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is, and the component is opened and closed by it, ignoring ASCII
 * case.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *InvalidComponentName __attribute__((swift_name("InvalidComponentName")));

/** `ICAL-J001` (Error): the input is not JSON. Nothing is read. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *InvalidJson __attribute__((swift_name("InvalidJson")));

/**
 * `ICAL-S008` (Warning): a property or parameter name is empty or contains a character other than letters,
 * digits and a hyphen. The name is kept as it is.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *InvalidName __attribute__((swift_name("InvalidName")));

/**
 * `ICAL-S006` (Warning): a logical line contains an invalid UTF-8 sequence. Each maximal invalid subsequence is
 * replaced with U+FFFD; the line is not skipped. One problem per line.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *InvalidUtf8 __attribute__((swift_name("InvalidUtf8")));

/**
 * `ICAL-Z003` (Warning): a `VTIMEZONE` has no `TZID`, no observance with `DTSTART` and `TZOFFSETTO`, or more than
 * 200,000 transitions up to its last start (a hostile zone of endless observances). It is skipped, and the next
 * resolver of the chain looks for the `TZID`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *InvalidVTimeZone __attribute__((swift_name("InvalidVTimeZone")));

/**
 * `ICAL-V001` (Error): a value does not conform to the grammar of its type; no value is decoded. In strict mode
 * it follows the warning code of a lenient repair, with the same position.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *InvalidValue __attribute__((swift_name("InvalidValue")));

/**
 * `ICAL-Z005` (Warning): the current rules of the zone cannot be written as one `RRULE` per observance, so the
 * generated `VTIMEZONE` only approximates them. The full mode gives an exact one.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *IrregularTimeZone __attribute__((swift_name("IrregularTimeZone")));

/**
 * `ICAL-J007` (Error): jCal components are nested deeper than `JCalOptions.maxDepth` (16 by default). The
 * deeper component is skipped with everything inside it.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *JCalTooDeep __attribute__((swift_name("JCalTooDeep")));

/**
 * `ICAL-J005` (Warning): a value is not in the form of its type, such as a `date-time` that is not
 * `YYYY-MM-DDTHH:MM:SS`. The value is written as it is.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *JCalValueMismatch __attribute__((swift_name("JCalValueMismatch")));

/**
 * `ICAL-V004` (Warning): a time has the second 60, which the standard allows. The decoder replaces it by 59 or
 * by the next minute, depending on its policy.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *LeapSecond __attribute__((swift_name("LeapSecond")));

/** `ICAL-R008` (Info): `BYSECOND=60` has no meaning in local time. The value is ignored. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *LeapSecondIgnored __attribute__((swift_name("LeapSecondIgnored")));

/**
 * `ICAL-S010` (Error): a logical line is longer than [io.github.calendrum.syntax.ParseLimits.maxLineOctets]. The
 * whole line is skipped and the bytes beyond the limit are not copied.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *LineTooLong __attribute__((swift_name("LineTooLong")));

/**
 * `ICAL-S028` (Warning): the heuristic is less than 90% sure of the encoding, typically for a short text of
 * capital abbreviations where windows-1251 and KOI8-R look alike. The input is read in the chosen encoding; the
 * application may want to ask the user or pass a hint.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *LowCharsetConfidence __attribute__((swift_name("LowCharsetConfidence")));

/**
 * `ICAL-J002` (Error): an element is not a jCal component: not an array of a name, an array of properties and
 * an array of components, or the name is not valid. The component is skipped with its children.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MalformedJCalComponent __attribute__((swift_name("MalformedJCalComponent")));

/**
 * `ICAL-J004` (Warning): a parameter value is not a string or an array of strings, or the `value` parameter
 * appears in the parameter object. The parameter is skipped.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MalformedJCalParameter __attribute__((swift_name("MalformedJCalParameter")));

/**
 * `ICAL-J003` (Error): a property is not an array of a name, an object of parameters, a type and at least one
 * value, or a value has the wrong JSON type. The property is skipped.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MalformedJCalProperty __attribute__((swift_name("MalformedJCalProperty")));

/**
 * `ICAL-S012` (Warning): a parameter is malformed: it has no `=`, a quote inside an unquoted value, or text
 * after a closing quote. A parameter without `=` gets no values; otherwise the value is taken literally up to
 * the next delimiter.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MalformedParameter __attribute__((swift_name("MalformedParameter")));

/**
 * `ICAL-L016` (Error): a value does not decode as its type; the details are an `ICAL-V` problem from reading.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MalformedValue __attribute__((swift_name("MalformedValue")));

/**
 * `ICAL-L018` (Error): the calendar has no `METHOD`, or one that differs from the method of the iTIP rule set
 * in use.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MethodMismatch __attribute__((swift_name("MethodMismatch")));

/**
 * `ICAL-L020` (Error): the iTIP method is not defined for the component, such as `REFRESH` for a `VJOURNAL`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MethodNotDefined __attribute__((swift_name("MethodNotDefined")));

/**
 * `ICAL-S007` (Error): there is no colon outside quotes after the property name and its parameters. The line is
 * skipped.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MissingColon __attribute__((swift_name("MissingColon")));

/**
 * `ICAL-L007` (Error): a required nested component is missing, such as a calendar without components or a
 * `VTIMEZONE` without `STANDARD` or `DAYLIGHT`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MissingComponent __attribute__((swift_name("MissingComponent")));

/**
 * `ICAL-L009` (Error): a property lacks the one it requires, such as `RRULE` without `DTSTART` or `REPEAT`
 * without `DURATION`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MissingDependency __attribute__((swift_name("MissingDependency")));

/** `ICAL-L001` (Error): a required property is missing. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MissingProperty __attribute__((swift_name("MissingProperty")));

/**
 * `ICAL-M001` (Error): a component of a schema lacks a required property, or the first such property is broken.
 * The component is skipped. The explanation names the property, never its value.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MissingRequiredField __attribute__((swift_name("MissingRequiredField")));

/**
 * `ICAL-L017` (Error): a `TZID` has no `VTIMEZONE` with that identifier in the calendar ([RFC 5545
 * §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)). Reported once per `TZID`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *MissingTimeZone __attribute__((swift_name("MissingTimeZone")));

/**
 * `ICAL-V012` (Warning): the offset `-0000`, which [RFC 5545
 * §3.3.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.14) forbids. It is read as `+0000`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *NegativeZeroOffset __attribute__((swift_name("NegativeZeroOffset")));

/**
 * `ICAL-L015` (Error): the `DTSTART` of a `VTIMEZONE` observance is not a local time ([RFC 5545
 * §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *NotLocalTime __attribute__((swift_name("NotLocalTime")));

/**
 * `ICAL-V027` (Warning): a value that must be in UTC is not, as in `DTSTAMP`, `CREATED`, `LAST-MODIFIED` and an
 * absolute `TRIGGER`. It is kept as it is.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *NotUtc __attribute__((swift_name("NotUtc")));

/**
 * `ICAL-O005` (Warning): a component has no `DTSTART` (and a to-do no `DUE` either), so it has no occurrence.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *OccurrenceWithoutStart __attribute__((swift_name("OccurrenceWithoutStart")));

/**
 * `ICAL-V002` (Error): a field is out of range, such as a month, a day, an hour, a UTC offset, an integer
 * beyond 32 bits, or a `BYxxx` part of a recurrence rule. No value is decoded.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *OutOfRange __attribute__((swift_name("OutOfRange")));

/**
 * `ICAL-O001` (Warning): the `RECURRENCE-ID` of an override matches no instance of the master. The override is
 * kept as a single occurrence.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *OverrideWithoutInstance __attribute__((swift_name("OverrideWithoutInstance")));

/**
 * `ICAL-V028` (Warning): a parameter does not apply to the value, such as `TZID` on a `DATE` or `RELATED` on an
 * absolute `TRIGGER`. It is not applied.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ParameterNotAllowed __attribute__((swift_name("ParameterNotAllowed")));

/**
 * `ICAL-V022` (Warning): a `PERIOD` ends no later than it starts, or has a duration that is not positive. It is
 * kept.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *PeriodNotPositive __attribute__((swift_name("PeriodNotPositive")));

/**
 * `ICAL-L004` (Error): a known property is in a component where it does not belong, or that the iTIP method
 * forbids.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *PropertyNotAllowed __attribute__((swift_name("PropertyNotAllowed")));

/**
 * `ICAL-S021` (Warning): a property outside any component, before the first `BEGIN` or between calendars. The
 * property is not part of the tree, but a cursor reports it at depth 0.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *PropertyOutsideComponent __attribute__((swift_name("PropertyOutsideComponent")));

/**
 * `ICAL-V003` (Error): a recurrence rule has no `FREQ` part ([RFC 5545
 * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)). No value is decoded.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *RecurWithoutFreq __attribute__((swift_name("RecurWithoutFreq")));

/**
 * `ICAL-R001` (Warning): the number of occurrences reached the limit while the sequence was not over, or a rule
 * with `COUNT` passed as many dates before the start of a range. The sequence ends.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *RecurrenceLimitReached __attribute__((swift_name("RecurrenceLimitReached")));

/**
 * `ICAL-R002` (Warning): too many periods in a row produced no occurrence, as in
 * `FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30`. The sequence ends.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *RecurrenceNoMatch __attribute__((swift_name("RecurrenceNoMatch")));

/**
 * `ICAL-L003` (Warning): a property occurs more than once where the standard says it SHOULD NOT, such as
 * `RRULE`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *RepeatedProperty __attribute__((swift_name("RepeatedProperty")));

/** `ICAL-V018` (Warning): a part of a recurrence rule is repeated. The last one is kept. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *RepeatedRulePart __attribute__((swift_name("RepeatedRulePart")));

/** `ICAL-R004` (Warning): the `RSCALE` of the rule is not `GREGORIAN`. The rule is not expanded. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *RscaleNotSupported __attribute__((swift_name("RscaleNotSupported")));

/**
 * `ICAL-V019` (Warning): a rule part is not allowed in this rule, such as `BYWEEKNO` without `YEARLY`, or
 * `BYSETPOS` without another `BYxxx` part. The part is kept.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *RulePartNotAllowed __attribute__((swift_name("RulePartNotAllowed")));

/**
 * `ICAL-R003` (Warning): `DTSTART` is not an instance of its own rule, for which [RFC 5545
 * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined. What happens
 * is up to the `startMismatch` option of the recurrence options.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *StartNotInRule __attribute__((swift_name("StartNotInRule")));

/**
 * `ICAL-O002` (Info): an override with `RANGE=THISANDFUTURE` was applied to the later instances, on a
 * best-effort basis.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ThisAndFutureApplied __attribute__((swift_name("ThisAndFutureApplied")));

/**
 * `ICAL-S029` (Error): the time budget [io.github.calendrum.syntax.ParseLimits.maxTime] ran out. Reading stops at a
 * line boundary and the result is truncated. Always reported.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TimeBudgetExceeded __attribute__((swift_name("TimeBudgetExceeded")));

/**
 * `ICAL-R007` (Warning): the rule has a time part or a frequency shorter than a day while `DTSTART` is a
 * `DATE`. The time parts are ignored.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TimePartWithDate __attribute__((swift_name("TimePartWithDate")));

/**
 * `ICAL-Z002` (Info): the `TZID` was found through an alias, such as an IANA link, a Windows name or a
 * `/mozilla.org/` prefix.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TimeZoneAlias __attribute__((swift_name("TimeZoneAlias")));

/**
 * `ICAL-Z004` (Warning): the `TZID` was found only by a guess from a city in its name. The zone of the first
 * city mentioned is used.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TimeZoneGuessed __attribute__((swift_name("TimeZoneGuessed")));

/**
 * `ICAL-S022` (Error): components are nested deeper than [io.github.calendrum.syntax.ParseLimits.maxDepth]. The
 * subtree is skipped up to its `END`, with one problem per subtree.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TooDeep __attribute__((swift_name("TooDeep")));

/**
 * `ICAL-S023` (Error): the limit on components was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxComponents]). Open components are closed implicitly, reading stops
 * and the result is truncated. Always reported.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TooManyComponents __attribute__((swift_name("TooManyComponents")));

/**
 * `ICAL-S014` (Error): the limit on logical lines was reached ([io.github.calendrum.syntax.ParseLimits.maxLines]).
 * Reading stops and the result is truncated. Always reported, even beyond the problem limit.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TooManyLines __attribute__((swift_name("TooManyLines")));

/**
 * `ICAL-S013` (Warning): the limit on reported problems was reached
 * ([io.github.calendrum.syntax.ParseLimits.maxProblems]). Further problems are only counted; see
 * [io.github.calendrum.syntax.ParseResult.problemCount].
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TooManyProblems __attribute__((swift_name("TooManyProblems")));

/** `ICAL-L002` (Error): a property occurs more often than allowed. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TooManyProperties __attribute__((swift_name("TooManyProperties")));

/**
 * `ICAL-S024` (Info): the input was transcoded to UTF-8 from the encoding named by a byte order mark or by a
 * charset hint. The explanation names the encoding and where it came from. Positions refer to the decoded text.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TranscodedInput __attribute__((swift_name("TranscodedInput")));

/**
 * `ICAL-V024` (Warning): a `TZID` parameter on a UTC `DATE-TIME`, which the standard forbids. The parameter is
 * not applied and the time is UTC.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *TzidWithUtc __attribute__((swift_name("TzidWithUtc")));

/**
 * `ICAL-S018` (Warning): the input ended while components were open, as in a truncated feed. All of them are
 * closed implicitly. Not reported when reading was stopped by a limit.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnclosedComponents __attribute__((swift_name("UnclosedComponents")));

/**
 * `ICAL-S009` (Warning): a quoted parameter value is not closed before the end of the line. The value is taken
 * literally up to the next comma, semicolon or colon, and parsing goes on.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnclosedQuote __attribute__((swift_name("UnclosedQuote")));

/**
 * `ICAL-L019` (Info): the `ACTION` of an alarm is not `AUDIO`, `DISPLAY` or `EMAIL`; such an alarm is to be
 * ignored.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnknownAlarmAction __attribute__((swift_name("UnknownAlarmAction")));

/**
 * `ICAL-S026` (Warning): the charset hint is not recognized, either an unknown name or an unsupported encoding.
 * The hint is ignored and the data decides.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnknownCharsetHint __attribute__((swift_name("UnknownCharsetHint")));

/**
 * `ICAL-V023` (Warning): a backslash in a `TEXT` value is followed by a character that is not an escape of [RFC
 * 5545 §3.3.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.11), or ends the value. Both characters are
 * kept.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnknownEscape __attribute__((swift_name("UnknownEscape")));

/**
 * `ICAL-J006` (Warning): the type of a property is not a type of RFC 5545 or RFC 7265. No `VALUE` parameter is
 * written and the value is kept as it is.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnknownJCalType __attribute__((swift_name("UnknownJCalType")));

/**
 * `ICAL-L005` (Info): a property known neither to the registry nor to the rule tables, and not an `X-`
 * property.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnknownProperty __attribute__((swift_name("UnknownProperty")));

/**
 * `ICAL-V020` (Warning): an unknown part in a recurrence rule, such as an `X-` name or a future registered
 * part. It is kept, in order, and written after the known ones.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnknownRulePart __attribute__((swift_name("UnknownRulePart")));

/**
 * `ICAL-Z001` (Warning): no resolver of the chain knows the `TZID`. The unknown-zone policy decides: floating
 * time, UTC or an exception.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnknownTimeZone __attribute__((swift_name("UnknownTimeZone")));

/** `ICAL-S017` (Warning): an `END` names a component that is not open. The line is ignored. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnmatchedEnd __attribute__((swift_name("UnmatchedEnd")));

/**
 * `ICAL-S011` (Error): the input is in an encoding the library does not read at all, such as UTF-7, UTF-EBCDIC,
 * SCSU, BOCU-1 or GB18030, recognized by its signature. Nothing is parsed. The explanation names the encoding.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UnsupportedEncoding __attribute__((swift_name("UnsupportedEncoding")));

/**
 * `ICAL-R005` (Warning): the type of `UNTIL` does not match `DTSTART`, such as a `DATE` against a `DATE-TIME`
 * or UTC against local. The two are compared in the closest sensible way.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UntilTypeMismatch __attribute__((swift_name("UntilTypeMismatch")));

/**
 * `ICAL-R006` (Info): `UNTIL` is in UTC while `DTSTART` has a time zone and no `untilOffset` is set, so `UNTIL`
 * is compared with local time.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UntilWithoutZone __attribute__((swift_name("UntilWithoutZone")));

/**
 * `ICAL-L024` (Info): an `ORGANIZER` or `ATTENDEE` with the scheme in upper case, `MAILTO:`. Found by the
 * linter.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UppercaseMailto __attribute__((swift_name("UppercaseMailto")));

/**
 * `ICAL-V021` (Warning): a `URI` or `CAL-ADDRESS` without a scheme, such as an address without `mailto:`. It is
 * kept as it is.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UriWithoutScheme __attribute__((swift_name("UriWithoutScheme")));

/** `ICAL-V011` (Warning): a `UTC-OFFSET` is written with a colon, such as `+03:00`. It is read without it. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *UtcOffsetWithColon __attribute__((swift_name("UtcOffsetWithColon")));

/** `ICAL-S001` (Info): the input starts with a UTF-8 byte order mark. The three bytes are skipped. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *Utf8Bom __attribute__((swift_name("Utf8Bom")));

/**
 * `ICAL-L013` (Error): a value outside the list of the standard, such as a component `STATUS`, `VERSION` or
 * `TRANSP`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ValueNotAllowed __attribute__((swift_name("ValueNotAllowed")));

/**
 * `ICAL-L014` (Error): a number outside its range, such as `PRIORITY` outside 0 to 9 or `PERCENT-COMPLETE`
 * outside 0 to 100.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ValueOutOfRange __attribute__((swift_name("ValueOutOfRange")));

/**
 * `ICAL-L010` (Error): the form of a value differs from that of a related one, such as a `DATE` `DTEND` with a
 * `DATE-TIME` `DTSTART`, or a non-UTC `UNTIL` with a zoned `DTSTART`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ValueTypeMismatch __attribute__((swift_name("ValueTypeMismatch")));

/**
 * `ICAL-V025` (Warning): the `VALUE` parameter names a type this property does not take, or is empty. The
 * default type of the property is read.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ValueTypeNotAllowed __attribute__((swift_name("ValueTypeNotAllowed")));

/**
 * `ICAL-V026` (Warning): a value of a type other than the default is not declared by `VALUE` or `ENCODING`, as
 * in `TRIGGER:19980101T050000Z`. It is read as the value of that type.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *ValueTypeNotDeclared __attribute__((swift_name("ValueTypeNotDeclared")));

/** `ICAL-L023` (Info): `X-WR-CALNAME` without the standard `NAME` of RFC 7986. Found by the linter. */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *VendorCalendarName __attribute__((swift_name("VendorCalendarName")));

/**
 * `ICAL-L021` (Warning): `X-WR-TIMEZONE` beside floating times, which clients understand in different ways.
 * Found by the linter.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *VendorTimeZone __attribute__((swift_name("VendorTimeZone")));

/**
 * `ICAL-V013` (Warning): spaces or tabs around a value or a list item. They are dropped. Not reported for
 * `TEXT` and `BINARY`.
 */
@property (readonly) CalendrumJcalCalendrum_coreProblemCode *Whitespace __attribute__((swift_name("Whitespace")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface CalendrumJcalKotlinEnumCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol CalendrumJcalKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<CalendrumJcalKotlinKClass>)kClass provider:(id<CalendrumJcalKotlinx_serialization_coreKSerializer> (^)(NSArray<id<CalendrumJcalKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<CalendrumJcalKotlinKClass>)kClass serializer:(id<CalendrumJcalKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<CalendrumJcalKotlinKClass>)baseClass actualClass:(id<CalendrumJcalKotlinKClass>)actualClass actualSerializer:(id<CalendrumJcalKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<CalendrumJcalKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumJcalKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultDeserializerProvider:)"))) __attribute__((deprecated("Deprecated in favor of function with more precise name: polymorphicDefaultDeserializer")));
- (void)polymorphicDefaultDeserializerBaseClass:(id<CalendrumJcalKotlinKClass>)baseClass defaultDeserializerProvider:(id<CalendrumJcalKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefaultDeserializer(baseClass:defaultDeserializerProvider:)")));
- (void)polymorphicDefaultSerializerBaseClass:(id<CalendrumJcalKotlinKClass>)baseClass defaultSerializerProvider:(id<CalendrumJcalKotlinx_serialization_coreSerializationStrategy> _Nullable (^)(id))defaultSerializerProvider __attribute__((swift_name("polymorphicDefaultSerializer(baseClass:defaultSerializerProvider:)")));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol CalendrumJcalKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol CalendrumJcalKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol CalendrumJcalKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol CalendrumJcalKotlinKClass <CalendrumJcalKotlinKDeclarationContainer, CalendrumJcalKotlinKAnnotatedElement, CalendrumJcalKotlinKClassifier>
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end


/** Collects the parameters and the value of a new [ICalProperty]. A builder is not thread-safe. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalProperty.Builder")))
@interface CalendrumJcalCalendrum_coreICalPropertyBuilder : CalendrumJcalBase

/**
 * Creates the property.
 *
 * @throws IllegalArgumentException if the value contains a control character other than a tab.
 */
- (CalendrumJcalCalendrum_coreICalProperty *)build __attribute__((swift_name("build()")));

/** Appends [parameter] after the parameters added so far. */
- (CalendrumJcalCalendrum_coreICalPropertyBuilder *)parameterParameter:(CalendrumJcalCalendrum_coreICalParameter *)parameter __attribute__((swift_name("parameter(parameter:)")));

/**
 * Appends a parameter with the given [name] and [values].
 *
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumJcalCalendrum_coreICalPropertyBuilder *)parameterName:(NSString *)name values:(CalendrumJcalKotlinArray<NSString *> *)values __attribute__((swift_name("parameter(name:values:)")));

/**
 * Sets the value, which is written as is: escape `TEXT` yourself or use the typed model. The default is an
 * empty string.
 */
- (CalendrumJcalCalendrum_coreICalPropertyBuilder *)valueValue:(NSString *)value __attribute__((swift_name("value(value:)")));
@end


/** Creates parameters by hand. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreICalParameter.Companion")))
@interface CalendrumJcalCalendrum_coreICalParameterCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** Creates parameters by hand. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalCalendrum_coreICalParameterCompanion *shared __attribute__((swift_name("shared")));

/**
 * Creates a parameter with the given [name] and [values].
 *
 * @param name letters, digits and hyphens only; the stored name is upper case.
 * @param values at least one value.
 * @throws IllegalArgumentException if [name] is invalid or no value is given.
 */
- (CalendrumJcalCalendrum_coreICalParameter *)ofName:(NSString *)name values:(CalendrumJcalKotlinArray<NSString *> *)values __attribute__((swift_name("of(name:values:)")));
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
@protocol CalendrumJcalCalendrum_coreParameterCodec
@required

/**
 * Decodes [parameter] of [property], with problems and strictness as [context] says.
 *
 * @param property the property the parameter belongs to.
 * @param parameter the parameter to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumJcalCalendrum_coreDecoded>)decodeProperty:(CalendrumJcalCalendrum_coreICalProperty *)property parameter:(CalendrumJcalCalendrum_coreICalParameter *)parameter context:(CalendrumJcalCalendrum_coreDecodeContext *)context __attribute__((swift_name("decode(property:parameter:context:)")));

/**
 * Encodes [value] into a parameter named by [context].
 *
 * @param value the value to write.
 * @param context carries the parameter name.
 */
- (CalendrumJcalCalendrum_coreICalParameter *)encodeParameterValue:(id)value context:(CalendrumJcalCalendrum_coreEncodeContext *)context __attribute__((swift_name("encodeParameter(value:context:)")));
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
@protocol CalendrumJcalCalendrum_corePropertyCodec
@required

/**
 * Decodes [property], with problems and strictness as [context] says.
 *
 * @param property the property to read.
 * @param context lenient or strict decoding, and a factory for problems with positions.
 */
- (id<CalendrumJcalCalendrum_coreDecoded>)decodeProperty:(CalendrumJcalCalendrum_coreICalProperty *)property context:(CalendrumJcalCalendrum_coreDecodeContext *)context __attribute__((swift_name("decode(property:context:)")));

/**
 * Encodes [value] into a property named by [context].
 *
 * @param value the value to write.
 * @param context carries the property name.
 */
- (CalendrumJcalCalendrum_coreICalProperty *)encodePropertyValue:(id)value context:(CalendrumJcalCalendrum_coreEncodeContext *)context __attribute__((swift_name("encodeProperty(value:context:)")));
@end


/** The constants of ValueType and the lookup by token. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreValueType.Companion")))
@interface CalendrumJcalCalendrum_coreValueTypeCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** The constants of ValueType and the lookup by token. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalCalendrum_coreValueTypeCompanion *shared __attribute__((swift_name("shared")));

/**
 * Returns the constant for [value], ignoring case, or a new ValueType holding the upper-cased token when it is
 * not one the library knows.
 *
 * @param value a token as written in iCalendar text.
 */
- (CalendrumJcalCalendrum_coreValueType *)ofValue:(NSString *)value __attribute__((swift_name("of(value:)")));

/** `BINARY`. Base64 encoded binary data. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Binary __attribute__((swift_name("Binary")));

/** `BOOLEAN`. `TRUE` or `FALSE`. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Boolean __attribute__((swift_name("Boolean")));

/** `CAL-ADDRESS`. A calendar user address. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *CalAddress __attribute__((swift_name("CalAddress")));

/** `DATE`. A calendar date. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Date __attribute__((swift_name("Date")));

/** `DATE-TIME`. A date with a time of day. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *DateTime __attribute__((swift_name("DateTime")));

/** `DURATION`. A length of time. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Duration __attribute__((swift_name("Duration")));

/** `FLOAT`. A decimal number. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Float __attribute__((swift_name("Float")));

/** `INTEGER`. A signed 32-bit integer. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Integer __attribute__((swift_name("Integer")));

/** `PERIOD`. A precise period of time. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Period __attribute__((swift_name("Period")));

/** `RECUR`. A recurrence rule. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Recur __attribute__((swift_name("Recur")));

/** `TEXT`. Human-readable text. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Text __attribute__((swift_name("Text")));

/** `TIME`. A time of day. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Time __attribute__((swift_name("Time")));

/** `UID`. A `UID` reference (RFC 9253). */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Uid __attribute__((swift_name("Uid")));

/** `URI`. A uniform resource identifier. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *Uri __attribute__((swift_name("Uri")));

/** `UTC-OFFSET`. An offset from UTC. */
@property (readonly) CalendrumJcalCalendrum_coreValueType *UtcOffset __attribute__((swift_name("UtcOffset")));

/** `XML-REFERENCE`. A reference to an XML element (RFC 9073). */
@property (readonly) CalendrumJcalCalendrum_coreValueType *XmlReference __attribute__((swift_name("XmlReference")));
@end


/**
 * The result of decoding a property or parameter: a value, nothing, or a malformed one.
 *
 * Reading with `component[key]` gives only the value or `null`; `component.decode(key)` tells which of the three it was
 * and why.
 */
__attribute__((swift_name("Calendrum_coreDecoded")))
@protocol CalendrumJcalCalendrum_coreDecoded
@required
@end


/**
 * How a codec decodes: leniently or strictly, and the factory for problems at the right position.
 *
 * @property isStrict whether a value that is only acceptable by repair is an error.
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext")))
@interface CalendrumJcalCalendrum_coreDecodeContext : CalendrumJcalBase
@property (class, readonly, getter=companion) CalendrumJcalCalendrum_coreDecodeContextCompanion *companion __attribute__((swift_name("companion")));

/** Creates a problem with [code] positioned [valueOffset] characters into the value of [property]. */
- (CalendrumJcalCalendrum_coreProblem *)problemProperty:(CalendrumJcalCalendrum_coreICalProperty *)property code:(CalendrumJcalCalendrum_coreProblemCode *)code valueOffset:(int32_t)valueOffset __attribute__((swift_name("problem(property:code:valueOffset:)")));

/** Creates a problem with [code] positioned at the value of [parameter] of [property]. */
- (CalendrumJcalCalendrum_coreProblem *)problemProperty:(CalendrumJcalCalendrum_coreICalProperty *)property parameter:(CalendrumJcalCalendrum_coreICalParameter *)parameter code:(CalendrumJcalCalendrum_coreProblemCode *)code __attribute__((swift_name("problem(property:parameter:code:)")));

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
@interface CalendrumJcalCalendrum_coreEncodeContext : CalendrumJcalBase

/** Returns a short description for diagnostics; its format is not a contract. */
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end


/** The two contexts. */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Calendrum_coreDecodeContext.Companion")))
@interface CalendrumJcalCalendrum_coreDecodeContextCompanion : CalendrumJcalBase
+ (instancetype)alloc __attribute__((unavailable));

/** The two contexts. */
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) CalendrumJcalCalendrum_coreDecodeContextCompanion *shared __attribute__((swift_name("shared")));

/** Reads known quirks of real-world data with a warning and a repair. */
@property (readonly) CalendrumJcalCalendrum_coreDecodeContext *Lenient __attribute__((swift_name("Lenient")));

/** Turns every repair into an error: the value is rejected. */
@property (readonly) CalendrumJcalCalendrum_coreDecodeContext *Strict __attribute__((swift_name("Strict")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END

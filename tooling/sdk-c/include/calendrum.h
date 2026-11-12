/*
 * The C interface of Calendrum, a library for iCalendar files (RFC 5545).
 *
 * One function runs every operation: a JSON request goes in and a JSON response comes out, as the operations contract
 * describes them. There are no objects to create and no state between calls.
 *
 * Memory: every buffer this library returns comes from its own malloc, and only calendrum_free may release it. On Windows the
 * library and the caller can have different C runtimes, so free() from the caller is a heap corruption there. A buffer can be
 * released on any thread.
 *
 * Threads: every function can be called from any thread, at the same time, without any setup. The first call on a process
 * starts the Kotlin/Native runtime, which keeps its own threads from then on.
 *
 * Do not call this library in a child of fork() when the parent has called it, not even once. The threads of the runtime do
 * not survive a fork. See the guide for what works instead, for the size of the stack, for signals and for unloading.
 *
 * The ABI only grows: functions are added and existing ones do not change. A change to an existing function would be a new
 * major version of calendrum_abi_version.
 */
#ifndef CALENDRUM_H
#define CALENDRUM_H

#include <stddef.h>
#include <stdint.h>

#if defined(_WIN32)
#define CALENDRUM_API __declspec(dllimport)
#else
#define CALENDRUM_API
#endif

/* The version of the ABI this header describes; calendrum_abi_version gives the one of the library. */
#define CALENDRUM_ABI_VERSION 1

/* The response is written. An operation that failed is here too, with "ok": false in the response. */
#define CALENDRUM_OK 0
/* A null pointer where one is required, or a null request with a length that is not zero. Nothing is written. */
#define CALENDRUM_BAD_ARGUMENT 1
/* An internal failure, such as no memory for the response. Nothing is written. */
#define CALENDRUM_INTERNAL_ERROR 2

#ifdef __cplusplus
extern "C" {
#endif

/* Returns the version of the ABI. */
CALENDRUM_API int32_t calendrum_abi_version(void);

/*
 * Writes the versions as a JSON object with the integers "abi" and "contract" and the strings "library" and "sdk".
 *
 * On CALENDRUM_OK, *out_json holds UTF-8 with a final zero that *out_len does not count; release it with calendrum_free. On any
 * other code, *out_json is null and *out_len is 0, for those of the two pointers that are not null themselves.
 */
CALENDRUM_API int32_t calendrum_version(uint8_t **out_json, size_t *out_len);

/*
 * Runs one operation.
 *
 * operation    the name of the operation, in UTF-8 with a final zero, such as "inspect".
 * request      the request, a JSON object in UTF-8. It needs no final zero and may hold one; only request_len bytes are read.
 *              It may be null when request_len is 0, which is not valid JSON, so the response says so.
 * request_len  the length of the request in bytes.
 * out_response on CALENDRUM_OK, the response in UTF-8 with a final zero that *out_len does not count. Release it with
 *              calendrum_free.
 * out_len      on CALENDRUM_OK, the length of the response in bytes.
 *
 * Returns CALENDRUM_OK whenever a response is written, whether "ok" in it is true or false: an unknown operation, a request
 * that is not valid JSON or UTF-8 and a request above the limit are all answered in the response, with their codes. On any other
 * code, *out_response is null and *out_len is 0, for those of the two pointers that are not null themselves.
 *
 * A response is not trusted text: fields marked in the contract hold what the calendar says.
 */
CALENDRUM_API int32_t calendrum_call(const char *operation,
                                     const uint8_t *request, size_t request_len,
                                     uint8_t **out_response, size_t *out_len);

/* Releases a buffer from this library. A null pointer is allowed and does nothing. */
CALENDRUM_API void calendrum_free(uint8_t *data);

#ifdef __cplusplus
}
#endif

#endif

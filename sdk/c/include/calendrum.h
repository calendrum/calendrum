#ifndef CALENDRUM_H
#define CALENDRUM_H

#include <stddef.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

#define ICAL_OK 0
#define ICAL_INVALID_ARGUMENT 1
#define ICAL_INTERNAL_ERROR 2

int32_t ical_abi_version(void);

int32_t ical_version(uint8_t **out_json, size_t *out_len);

int32_t ical_parse(const uint8_t *ics, size_t len,
                   uint8_t **out_jcal, size_t *out_jcal_len,
                   uint8_t **out_problems, size_t *out_problems_len);

int32_t ical_occurrences(const uint8_t *ics, size_t len,
                         const char *from_utc, const char *to_utc, const char *zone,
                         uint8_t **out_json, size_t *out_len);

void ical_free(uint8_t *data);

#ifdef __cplusplus
}
#endif

#endif

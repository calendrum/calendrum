/* What the C tests share: checks, growing buffers, synthetic calendars, the cases of the contract, memory and thread counts. */
#ifndef SUPPORT_H
#define SUPPORT_H

#pragma clang diagnostic ignored "-Wunused-function"

#include <dirent.h>
#include <mach/mach.h>
#include <stdarg.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <time.h>

static int failures = 0;

#define CHECK(condition, ...)                                 \
    do {                                                      \
        if (!(condition)) {                                   \
            failures++;                                       \
            printf("FAILED %s:%d: ", __FILE__, __LINE__);     \
            printf(__VA_ARGS__);                              \
            printf("\n");                                     \
            fflush(stdout);                                   \
        }                                                     \
    } while (0)

static double now_seconds(void) {
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return (double) ts.tv_sec + (double) ts.tv_nsec / 1e9;
}

static double rss_mib(void) {
    mach_task_basic_info_data_t info;
    mach_msg_type_number_t count = MACH_TASK_BASIC_INFO_COUNT;
    if (task_info(mach_task_self(), MACH_TASK_BASIC_INFO, (task_info_t) &info, &count) != KERN_SUCCESS) return -1;
    return (double) info.resident_size / (1024.0 * 1024.0);
}

static int thread_count(void) {
    thread_act_array_t threads;
    mach_msg_type_number_t count = 0;
    if (task_threads(mach_task_self(), &threads, &count) != KERN_SUCCESS) return -1;
    for (mach_msg_type_number_t i = 0; i < count; i++) mach_port_deallocate(mach_task_self(), threads[i]);
    vm_deallocate(mach_task_self(), (vm_address_t) threads, count * sizeof(thread_t));
    return (int) count;
}

typedef struct {
    char *data;
    size_t len;
    size_t capacity;
} buffer;

static void buffer_reserve(buffer *b, size_t extra) {
    if (b->len + extra + 1 <= b->capacity) return;
    size_t capacity = b->capacity ? b->capacity : 1024;
    while (capacity < b->len + extra + 1) capacity *= 2;
    b->data = realloc(b->data, capacity);
    if (!b->data) abort();
    b->capacity = capacity;
}

static void buffer_add(buffer *b, const char *text, size_t len) {
    buffer_reserve(b, len);
    memcpy(b->data + b->len, text, len);
    b->len += len;
    b->data[b->len] = 0;
}

static void buffer_puts(buffer *b, const char *text) { buffer_add(b, text, strlen(text)); }

static void buffer_printf(buffer *b, const char *format, ...) {
    char line[512];
    va_list args;
    va_start(args, format);
    int n = vsnprintf(line, sizeof line, format, args);
    va_end(args);
    buffer_add(b, line, (size_t) n);
}

/* The lines of a calendar inside a JSON string: the only characters that need escaping here are the line endings. */
#define CRLF "\\r\\n"

/* A calendar of `count` weekly events, as the JSON string it goes into a request as, quotes included. */
static void add_feed(buffer *b, int count) {
    buffer_puts(b, "\"BEGIN:VCALENDAR" CRLF "VERSION:2.0" CRLF "PRODID:-//Calendrum//Test//EN" CRLF);
    for (int i = 0; i < count; i++) {
        buffer_puts(b, "BEGIN:VEVENT" CRLF);
        buffer_printf(b, "UID:event-%d@example.com" CRLF "DTSTAMP:20260901T080000Z" CRLF, i);
        buffer_printf(b, "DTSTART;TZID=Europe/Berlin:202609%02dT%02d0000" CRLF, 1 + i % 28, 8 + i % 10);
        buffer_puts(b, "DURATION:PT1H" CRLF "RRULE:FREQ=WEEKLY;COUNT=10" CRLF);
        buffer_printf(b, "SUMMARY:Lecture %d" CRLF "END:VEVENT" CRLF, i);
    }
    buffer_puts(b, "END:VCALENDAR" CRLF "\"");
}

static buffer inspect_request(int events) {
    buffer b = {0};
    buffer_puts(&b, "{\"ics\":");
    add_feed(&b, events);
    buffer_puts(&b, "}");
    return b;
}

static buffer expand_request(int events) {
    buffer b = {0};
    buffer_puts(&b, "{\"ics\":");
    add_feed(&b, events);
    buffer_puts(&b, ",\"from\":\"2026-01-01T00:00:00Z\",\"to\":\"2027-01-01T00:00:00Z\",\"zone\":\"Europe/Berlin\","
                    "\"limit\":100000}");
    return b;
}

static char *read_all(const char *path, size_t *len) {
    FILE *file = fopen(path, "rb");
    if (!file) return NULL;
    fseek(file, 0, SEEK_END);
    size_t size = (size_t) ftell(file);
    fseek(file, 0, SEEK_SET);
    char *data = malloc(size + 1);
    if (!data || fread(data, 1, size, file) != size) abort();
    fclose(file);
    data[size] = 0;
    *len = size;
    return data;
}

typedef struct {
    char operation[64];
    char id[128];
    char *request;
    size_t request_len;
} contract_case;

/* The field "operation" of meta.json, by hand: these are our own files, and there is no JSON parser in C here. */
static int read_operation(const char *meta, char *out, size_t size) {
    const char *key = strstr(meta, "\"operation\"");
    if (!key) return 0;
    const char *open = strchr(key + 11, '"');
    if (!open) return 0;
    const char *close = strchr(open + 1, '"');
    if (!close || (size_t) (close - open) > size) return 0;
    memcpy(out, open + 1, (size_t) (close - open - 1));
    out[close - open - 1] = 0;
    return 1;
}

static contract_case *load_cases(const char *root, int *count) {
    DIR *dir = opendir(root);
    if (!dir) {
        fprintf(stderr, "cannot read %s\n", root);
        exit(2);
    }
    int capacity = 128, n = 0;
    contract_case *cases = malloc(sizeof(contract_case) * (size_t) capacity);
    struct dirent *entry;
    while ((entry = readdir(dir))) {
        char path[2048];
        size_t len;
        snprintf(path, sizeof path, "%s/%s/meta.json", root, entry->d_name);
        char *meta = read_all(path, &len);
        if (!meta) continue;
        if (n == capacity) {
            capacity *= 2;
            cases = realloc(cases, sizeof(contract_case) * (size_t) capacity);
        }
        contract_case *c = &cases[n];
        if (!read_operation(meta, c->operation, sizeof c->operation)) {
            fprintf(stderr, "no operation in %s\n", path);
            exit(2);
        }
        free(meta);
        snprintf(c->id, sizeof c->id, "%s", entry->d_name);
        snprintf(path, sizeof path, "%s/%s/request.json", root, entry->d_name);
        c->request = read_all(path, &c->request_len);
        if (!c->request) {
            fprintf(stderr, "no request in %s\n", path);
            exit(2);
        }
        n++;
    }
    closedir(dir);
    *count = n;
    return cases;
}

#endif

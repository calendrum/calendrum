/* Every line of the ABI, through the real library: arguments, final zero, UTF-8, the limit, memory. */
#include <sys/mman.h>
#include <unistd.h>

#include "calendrum.h"
#include "interpose.h"
#include "support.h"

#define GARBAGE ((uint8_t *) (uintptr_t) 0x1234)

static int32_t call_text(const char *operation, const char *request, uint8_t **out, size_t *len) {
    return calendrum_call(operation, (const uint8_t *) request, strlen(request), out, len);
}

/* Calls and checks that the response has the code, is one JSON envelope and has its final zero. */
static void expect_code(const char *what, const char *operation, const void *request, size_t len, const char *code) {
    uint8_t *out = GARBAGE;
    size_t out_len = 77;
    int32_t result = calendrum_call(operation, request, len, &out, &out_len);
    CHECK(result == CALENDRUM_OK, "%s: code %d", what, result);
    if (result != CALENDRUM_OK) return;
    CHECK(out != NULL && out != GARBAGE, "%s: no response", what);
    CHECK(out[out_len] == 0, "%s: no final zero", what);
    CHECK(strlen((char *) out) == out_len, "%s: the length %zu and the text differ", what, out_len);
    CHECK(strncmp((char *) out, "{\"ops_version\":1,", 17) == 0, "%s: %s", what, out);
    CHECK(strstr((char *) out, code) != NULL, "%s: expected %s in %s", what, code, out);
    calendrum_free(out);
}

static void test_versions(void) {
    CHECK(calendrum_abi_version() == CALENDRUM_ABI_VERSION, "abi %d", calendrum_abi_version());

    uint8_t *json = GARBAGE;
    size_t len = 77;
    CHECK(calendrum_version(&json, &len) == CALENDRUM_OK, "version");
    CHECK(json && json[len] == 0 && strlen((char *) json) == len, "version text");
    CHECK(strstr((char *) json, "\"abi\":1,") != NULL, "%s", json);
    CHECK(strstr((char *) json, "\"contract\":1,") != NULL, "%s", json);
    CHECK(strstr((char *) json, "\"library\":\"") != NULL, "%s", json);
    CHECK(strstr((char *) json, "\"sdk\":\"") != NULL, "%s", json);
    calendrum_free(json);

    json = GARBAGE;
    CHECK(calendrum_version(&json, NULL) == CALENDRUM_BAD_ARGUMENT && json == NULL, "version without out_len");
    len = 77;
    CHECK(calendrum_version(NULL, &len) == CALENDRUM_BAD_ARGUMENT && len == 0, "version without out_json");
    CHECK(calendrum_version(NULL, NULL) == CALENDRUM_BAD_ARGUMENT, "version without both");
}

static void test_null_pointers(void) {
    const uint8_t *request = (const uint8_t *) "{}";
    uint8_t *out = GARBAGE;
    size_t len = 77;
    CHECK(calendrum_call(NULL, request, 2, &out, &len) == CALENDRUM_BAD_ARGUMENT, "no operation");
    CHECK(out == NULL && len == 0, "no operation: the outputs are cleared");

    out = GARBAGE;
    len = 77;
    CHECK(calendrum_call("inspect", NULL, 2, &out, &len) == CALENDRUM_BAD_ARGUMENT, "null request, a length");
    CHECK(out == NULL && len == 0, "null request: the outputs are cleared");

    len = 77;
    CHECK(calendrum_call("inspect", request, 2, NULL, &len) == CALENDRUM_BAD_ARGUMENT, "no out_response");
    CHECK(len == 0, "no out_response: out_len is cleared");

    out = GARBAGE;
    CHECK(calendrum_call("inspect", request, 2, &out, NULL) == CALENDRUM_BAD_ARGUMENT, "no out_len");
    CHECK(out == NULL, "no out_len: out_response is cleared");

    CHECK(calendrum_call(NULL, NULL, 1, NULL, NULL) == CALENDRUM_BAD_ARGUMENT, "all null");

    calendrum_free(NULL);
}

static void test_empty_request(void) {
    expect_code("null request, length 0", "inspect", NULL, 0, "OPS-BAD-ARGUMENT");
    expect_code("a request of length 0", "inspect", "", 0, "OPS-BAD-ARGUMENT");
    expect_code("length 0 of a valid request", "explain", "{\"code\":\"ICAL-S003\"}", 0, "OPS-BAD-ARGUMENT");
}

/* The last bytes of a page with a guard after it: reading one byte past request_len is a crash. */
static void test_reads_only_request_len(void) {
    const char *request = "{\"code\":\"ICAL-S003\"}";
    size_t len = strlen(request);
    size_t page = (size_t) getpagesize();
    char *area = mmap(NULL, page * 2, PROT_READ | PROT_WRITE, MAP_ANON | MAP_PRIVATE, -1, 0);
    CHECK(area != MAP_FAILED, "mmap");
    CHECK(mprotect(area + page, page, PROT_NONE) == 0, "mprotect");
    memcpy(area + page - len, request, len);
    expect_code("no final zero", "explain", area + page - len, len, "\"ICAL-S003\"");
    munmap(area, page * 2);
}

static void test_zero_in_request(void) {
    expect_code("zero after the request", "explain", "{\"code\":\"ICAL-S003\"}\0", 21, "OPS-BAD-ARGUMENT");
    expect_code("zero inside a string", "explain", "{\"code\":\"ICAL\0-S003\"}", 21, "\"ok\":false");
    expect_code("zero first", "explain", "\0{\"code\":\"ICAL-S003\"}", 21, "OPS-BAD-ARGUMENT");
    /* An escaped zero is valid JSON and reaches the operation, and the calendar then has one. */
    expect_code("escaped zero", "inspect", "{\"ics\":\"BEGIN:VCALENDAR\\u0000\"}", 31, "\"ok\":true");
}

static void test_invalid_utf8(void) {
    static const struct {
        const char *what;
        const char *text;
    } requests[] = {
        {"a stray continuation", "{\"code\":\"\x80\"}"},
        {"0xFF", "{\"code\":\"\xFF\"}"},
        {"overlong", "{\"code\":\"\xC0\xAF\"}"},
        {"an encoded surrogate", "{\"code\":\"\xED\xA0\x80\"}"},
        {"above U+10FFFF", "{\"code\":\"\xF4\x90\x80\x80\"}"},
        {"cut at the end", "{\"code\":\"\xE2\x82"},
        {"outside a string", "\xFF\xFE{}"},
    };
    for (size_t i = 0; i < sizeof requests / sizeof requests[0]; i++) {
        expect_code(requests[i].what, "explain", requests[i].text, strlen(requests[i].text), "OPS-BAD-ARGUMENT");
    }
    /* The name is checked by the operation itself, so a request that is fine gets to say the name is wrong. */
    expect_code("a name that is not UTF-8", "expl\xFF\xFE", "{}", 2, "OPS-UNKNOWN-OPERATION");
    expect_code("a name of only 0x80", "\x80", "{}", 2, "OPS-UNKNOWN-OPERATION");
}

static void test_names(void) {
    expect_code("unknown", "nothing", "{}", 2, "OPS-UNKNOWN-OPERATION");
    expect_code("empty", "", "{}", 2, "OPS-UNKNOWN-OPERATION");
    expect_code("upper case", "INSPECT", "{}", 2, "OPS-UNKNOWN-OPERATION");

    size_t size = 1024 * 1024;
    char *long_name = malloc(size + 1);
    memset(long_name, 'a', size);
    long_name[size] = 0;
    expect_code("a megabyte", long_name, "{}", 2, "OPS-UNKNOWN-OPERATION");
    memset(long_name, 0, size);
    memcpy(long_name, "explain", 7);
    expect_code("a good name", long_name, "{\"code\":\"ICAL-S003\"}", 20, "\"ICAL-S003\"");
    free(long_name);

    static const char *operations[] = {"inspect", "validate", "fix", "expand", "rrule_expand", "freebusy", "build_event",
                                       "diff", "convert", "anonymize", "tz_resolve", "explain"};
    for (size_t i = 0; i < sizeof operations / sizeof operations[0]; i++) {
        uint8_t *out = NULL;
        size_t len = 0;
        CHECK(call_text(operations[i], "{}", &out, &len) == CALENDRUM_OK, "%s", operations[i]);
        CHECK(out && strstr((char *) out, "OPS-BAD-ARGUMENT"), "%s: %s", operations[i], out);
        CHECK(out && !strstr((char *) out, "OPS-INTERNAL"), "%s: %s", operations[i], out);
        calendrum_free(out);
    }
}

#define LIMIT ((size_t) 128 * 1024 * 1024)

static void test_limit(void) {
    /* These two are refused from the length alone: the memory behind the pointer is one byte, and it is not touched. */
    char one = '{';
    expect_code("three times the limit and one", "inspect", &one, LIMIT * 3 + 1, "OPS-TOO-LARGE");
    expect_code("SIZE_MAX", "inspect", &one, SIZE_MAX, "OPS-TOO-LARGE");
    expect_code("above 2 GiB", "inspect", &one, (size_t) 3 * 1024 * 1024 * 1024, "OPS-TOO-LARGE");

    /* One character for each byte, one above the limit: refused after counting the bytes where they are. */
    size_t size = LIMIT + 1;
    char *large = malloc(size);
    CHECK(large != NULL, "malloc");
    memset(large, ' ', size);
    large[0] = '{';
    large[size - 1] = '}';
    double before = rss_mib();
    expect_code("one above the limit", "inspect", large, size, "OPS-TOO-LARGE");
    double grown = rss_mib() - before;
    /* The buffer itself is 128 MiB of touched pages; a copy in the memory of Kotlin would be at least as much again. */
    CHECK(grown < 64, "refused above the limit, but RSS grew by %.0f MiB", grown);

    /* Exactly at the limit: goes through, and the operation says what is missing. */
    size = LIMIT;
    large[size - 1] = '}';
    expect_code("at the limit", "inspect", large, size, "OPS-BAD-ARGUMENT");
    free(large);

    /* Three bytes for one character: above the limit in bytes, within it in characters, so not refused by size. */
    size_t units = LIMIT / 2;
    buffer b = {0};
    buffer_reserve(&b, units * 3 + 32);
    buffer_puts(&b, "{\"x\":\"");
    for (size_t i = 0; i < units; i++) memcpy(b.data + b.len + i * 3, "\xE2\x82\xAC", 3);
    b.len += units * 3;
    buffer_puts(&b, "\"}");
    CHECK(b.len > LIMIT, "the request is above the limit in bytes");
    expect_code("many bytes, few characters", "inspect", b.data, b.len, "OPS-BAD-ARGUMENT");
    free(b.data);
}

/* Text of the request never comes back in an error. */
static void test_errors_do_not_quote(void) {
    const char *marker = "MARKER-QUOTED";
    static const struct {
        const char *operation;
        const char *request;
    } calls[] = {
        {"MARKER-QUOTED", "{}"},
        {"explain", "{\"code\":\"MARKER-QUOTED\"}"},
        {"explain", "MARKER-QUOTED"},
        {"explain", "{\"code\":MARKER-QUOTED}"},
        {"tz_resolve", "{\"name\":\"MARKER-QUOTED\"}"},
        {"expand", "{\"ics\":\"\",\"from\":\"MARKER-QUOTED\",\"to\":\"x\"}"},
    };
    for (size_t i = 0; i < sizeof calls / sizeof calls[0]; i++) {
        uint8_t *out = NULL;
        size_t len = 0;
        CHECK(call_text(calls[i].operation, calls[i].request, &out, &len) == CALENDRUM_OK, "call %zu", i);
        CHECK(out && strstr((char *) out, "\"ok\":false"), "call %zu: %s", i, out);
        CHECK(out && !strstr((char *) out, marker), "call %zu quotes the request: %s", i, out);
        calendrum_free(out);
    }
}

/*
 * A response of a size nothing else asks for: the runtime calls malloc as well, and it cannot survive a refusal (see
 * out_of_memory.c).
 */
static void test_no_memory_for_the_response(void) {
    buffer request = inspect_request(2000);
    uint8_t *out = NULL;
    size_t len = 0;
    CHECK(calendrum_call("inspect", (uint8_t *) request.data, request.len, &out, &len) == CALENDRUM_OK, "inspect");
    size_t expected = len;
    calendrum_free(out);

    interpose_refuse_malloc_of(expected + 1);
    out = GARBAGE;
    len = 77;
    int32_t code = calendrum_call("inspect", (uint8_t *) request.data, request.len, &out, &len);
    CHECK(interpose_refused_malloc() == 0, "malloc of the response was not asked for %zu bytes", expected + 1);
    CHECK(code == CALENDRUM_INTERNAL_ERROR, "no memory: code %d", code);
    CHECK(out == NULL && len == 0, "no memory: the outputs are cleared");
    interpose_refuse_malloc_of(0);

    /* And it still works. */
    CHECK(calendrum_call("inspect", (uint8_t *) request.data, request.len, &out, &len) == CALENDRUM_OK, "after a failure");
    CHECK(len == expected, "after a failure: %zu bytes, not %zu", len, expected);
    calendrum_free(out);
    free(request.data);
}

int main(void) {
    test_versions();
    test_null_pointers();
    test_empty_request();
    test_reads_only_request_len();
    test_zero_in_request();
    test_invalid_utf8();
    test_names();
    test_limit();
    test_errors_do_not_quote();
    test_no_memory_for_the_response();
    printf("boundary: %d failed\n", failures);
    return failures ? 1 : 0;
}

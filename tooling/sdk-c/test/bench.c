/*
 * The price of a call for measure.py: the empty operation, inspect and expand of a feed, in milliseconds, as one line of JSON.
 *
 *   bench <feed.ics>
 */
#include "calendrum.h"
#include "support.h"

static double timed(const char *operation, const uint8_t *request, size_t len, int rounds) {
    uint8_t *out = NULL;
    size_t out_len = 0;
    for (int i = 0; i < (rounds / 10 > 5 ? rounds / 10 : 5); i++) {
        calendrum_call(operation, request, len, &out, &out_len);
        calendrum_free(out);
    }
    double start = now_seconds();
    for (int i = 0; i < rounds; i++) {
        if (calendrum_call(operation, request, len, &out, &out_len) != CALENDRUM_OK) exit(3);
        calendrum_free(out);
    }
    return (now_seconds() - start) / rounds * 1e3;
}

/* The calendar as a JSON string: line endings and the characters a feed can have that need an escape. */
static void add_json_string(buffer *b, const char *text, size_t len) {
    buffer_puts(b, "\"");
    for (size_t i = 0; i < len; i++) {
        char c = text[i];
        if (c == '\r') buffer_puts(b, "\\r");
        else if (c == '\n') buffer_puts(b, "\\n");
        else if (c == '"') buffer_puts(b, "\\\"");
        else if (c == '\\') buffer_puts(b, "\\\\");
        else buffer_add(b, &c, 1);
    }
    buffer_puts(b, "\"");
}

int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "usage: bench <feed.ics>\n");
        return 2;
    }
    size_t len;
    char *feed = read_all(argv[1], &len);
    if (!feed) return 2;
    buffer inspect = {0}, expand = {0};
    buffer_puts(&inspect, "{\"ics\":");
    add_json_string(&inspect, feed, len);
    buffer_puts(&inspect, "}");
    buffer_puts(&expand, "{\"ics\":");
    add_json_string(&expand, feed, len);
    buffer_puts(&expand, ",\"from\":\"2026-01-01T00:00:00Z\",\"to\":\"2027-01-01T00:00:00Z\",\"zone\":\"Europe/Berlin\",\"limit\":100000}");

    double explain_ms = timed("explain", (const uint8_t *) "{\"code\":\"ICAL-S003\"}", 20, 20000);
    double inspect_ms = timed("inspect", (uint8_t *) inspect.data, inspect.len, 300);
    double expand_ms = timed("expand", (uint8_t *) expand.data, expand.len, 100);
    printf("{\"explain\":%.6f,\"inspect\":%.6f,\"expand\":%.6f}\n", explain_ms, inspect_ms, expand_ms);
    return 0;
}

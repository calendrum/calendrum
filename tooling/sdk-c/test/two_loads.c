/*
 * The library under two paths in one process, which means two runtimes: two copies of the file, as two packages that each bring
 * their own would have it. Both are called, in turn and at the same time.
 *
 *   two_loads <library> <scratch directory>
 */
#include <dlfcn.h>
#include <pthread.h>

#include "support.h"

typedef int32_t (*call_function)(const char *, const uint8_t *, size_t, uint8_t **, size_t *);
typedef void (*free_function)(uint8_t *);

typedef struct {
    call_function call;
    free_function release;
    void *library;
} loaded;

static buffer request;
static uint8_t *expected;
static size_t expected_len;

static int copy_file(const char *from, const char *to) {
    size_t len;
    char *data = read_all(from, &len);
    if (!data) return 0;
    FILE *file = fopen(to, "wb");
    if (!file) return 0;
    int good = fwrite(data, 1, len, file) == len;
    fclose(file);
    free(data);
    return good;
}

static loaded open_library(const char *path) {
    loaded l = {0};
    l.library = dlopen(path, RTLD_NOW | RTLD_LOCAL);
    if (!l.library) {
        printf("dlopen %s: %s\n", path, dlerror());
        exit(1);
    }
    l.call = (call_function) dlsym(l.library, "calendrum_call");
    l.release = (free_function) dlsym(l.library, "calendrum_free");
    return l;
}

static int same_answer(loaded *l) {
    uint8_t *out = NULL;
    size_t len = 0;
    int good = l->call("expand", (uint8_t *) request.data, request.len, &out, &len) == 0 && len == expected_len &&
               memcmp(out, expected, len) == 0;
    l->release(out);
    return good;
}

static _Atomic int wrong = 0;

static void *loop(void *argument) {
    for (int i = 0; i < 300; i++) {
        if (!same_answer(argument)) wrong++;
    }
    return NULL;
}

int main(int argc, char **argv) {
    if (argc != 3) {
        fprintf(stderr, "usage: two_loads <library> <scratch directory>\n");
        return 2;
    }
    char first[2048], second[2048];
    snprintf(first, sizeof first, "%s/one-libcalendrum.dylib", argv[2]);
    snprintf(second, sizeof second, "%s/two-libcalendrum.dylib", argv[2]);
    CHECK(copy_file(argv[1], first) && copy_file(argv[1], second), "copies");

    request = expand_request(20);
    loaded a = open_library(first);
    loaded b = open_library(second);
    CHECK(a.call != b.call, "both paths gave one image: %p", (void *) a.call);
    printf("calendrum_call: %p and %p, %s\n", (void *) a.call, (void *) b.call, a.call != b.call ? "two images" : "ONE image");

    CHECK(a.call("expand", (uint8_t *) request.data, request.len, &expected, &expected_len) == 0, "first call");
    for (int i = 0; i < 100; i++) {
        CHECK(same_answer(&a), "the first copy, call %d", i);
        CHECK(same_answer(&b), "the second copy, call %d", i);
    }

    pthread_t ids[8];
    for (int i = 0; i < 8; i++) pthread_create(&ids[i], NULL, loop, i % 2 ? (void *) &a : (void *) &b);
    for (int i = 0; i < 8; i++) pthread_join(ids[i], NULL);
    CHECK(wrong == 0, "at the same time: %d differ", (int) wrong);
    printf("8 threads over two copies: %d differ, threads in the process %d\n", (int) wrong, thread_count());
    a.release(expected);
    remove(first);
    remove(second);
    printf("two_loads: %d failed\n", failures);
    return failures ? 1 : 0;
}

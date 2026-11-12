/*
 * The cost of loading: the time from dlopen to the first response, RSS and threads at each step. One process for one measurement.
 *
 *   load_cost <library>
 */
#include <dlfcn.h>
#include <unistd.h>

#include "support.h"

typedef int32_t (*call_function)(const char *, const uint8_t *, size_t, uint8_t **, size_t *);
typedef void (*free_function)(uint8_t *);
typedef int32_t (*abi_function)(void);

int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "usage: load_cost <library>\n");
        return 2;
    }
    double rss_before = rss_mib();
    int threads_before = thread_count();

    double start = now_seconds();
    void *library = dlopen(argv[1], RTLD_NOW | RTLD_LOCAL);
    double opened = now_seconds();
    if (!library) {
        printf("dlopen: %s\n", dlerror());
        return 1;
    }
    double rss_loaded = rss_mib();
    int threads_loaded = thread_count();

    call_function call = (call_function) dlsym(library, "calendrum_call");
    free_function release = (free_function) dlsym(library, "calendrum_free");
    abi_function abi = (abi_function) dlsym(library, "calendrum_abi_version");

    double abi_start = now_seconds();
    abi();
    double abi_end = now_seconds();
    double rss_abi = rss_mib();
    int threads_abi = thread_count();

    uint8_t *out = NULL;
    size_t len = 0;
    double call_start = now_seconds();
    int32_t code = call("explain", (const uint8_t *) "{\"code\":\"ICAL-S003\"}", 20, &out, &len);
    double call_end = now_seconds();
    CHECK(code == 0 && out && strstr((char *) out, "\"ok\":true"), "the first response");
    release(out);
    double rss_called = rss_mib();
    int threads_called = thread_count();

    buffer request = expand_request(200);
    for (int i = 0; i < 20; i++) {
        call("expand", (uint8_t *) request.data, request.len, &out, &len);
        release(out);
    }
    double rss_worked = rss_mib();
    int threads_worked = thread_count();

    /* One line of numbers for the measuring script, then the same in words. */
    printf("numbers %.3f %.3f %.3f %.3f %.1f %.1f %.1f %.1f %.1f %d %d %d %d %d\n", (opened - start) * 1e3, (abi_end - abi_start) * 1e3,
           (call_end - call_start) * 1e3, (call_end - start) * 1e3, rss_before, rss_loaded, rss_abi, rss_called, rss_worked, threads_before,
           threads_loaded, threads_abi, threads_called, threads_worked);
    printf("dlopen %.3f ms, first function %.3f ms, first explain %.3f ms, dlopen to the first response %.3f ms\n",
           (opened - start) * 1e3, (abi_end - abi_start) * 1e3, (call_end - call_start) * 1e3, (call_end - start) * 1e3);
    printf("RSS MiB: before %.1f, loaded %.1f, runtime started %.1f, first response %.1f, after 20 expand %.1f\n", rss_before, rss_loaded,
           rss_abi, rss_called, rss_worked);
    printf("threads: before %d, loaded %d, runtime started %d, first response %d, after 20 expand %d\n", threads_before, threads_loaded,
           threads_abi, threads_called, threads_worked);
    return failures ? 1 : 0;
}

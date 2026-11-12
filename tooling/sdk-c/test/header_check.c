/* The header on its own, and the prototypes as the ABI has them: a different type here is a compile error. */
#include "calendrum.h"

static int32_t (*const abi_version)(void) = calendrum_abi_version;
static int32_t (*const version)(uint8_t **, size_t *) = calendrum_version;
static int32_t (*const call)(const char *, const uint8_t *, size_t, uint8_t **, size_t *) = calendrum_call;
static void (*const release)(uint8_t *) = calendrum_free;

int main(void) {
    uint8_t *json = 0;
    size_t len = 0;
    if (abi_version() != CALENDRUM_ABI_VERSION) return 1;
    if (version(&json, &len) != CALENDRUM_OK || !json) return 2;
    release(json);
    if (call(0, 0, 1, 0, 0) != CALENDRUM_BAD_ARGUMENT) return 3;
    return 0;
}

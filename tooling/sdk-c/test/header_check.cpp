/* The same as header_check.c, as C++: extern "C" has to hold. */
#include "calendrum.h"

static int32_t (*const abi_version)(void) = calendrum_abi_version;
static int32_t (*const version)(uint8_t **, size_t *) = calendrum_version;
static int32_t (*const call)(const char *, const uint8_t *, size_t, uint8_t **, size_t *) = calendrum_call;
static void (*const release)(uint8_t *) = calendrum_free;

int main(void) {
    uint8_t *json = nullptr;
    size_t len = 0;
    if (abi_version() != CALENDRUM_ABI_VERSION) return 1;
    if (version(&json, &len) != CALENDRUM_OK || !json) return 2;
    release(json);
    if (call(nullptr, nullptr, 1, nullptr, nullptr) != CALENDRUM_BAD_ARGUMENT) return 3;
    return 0;
}

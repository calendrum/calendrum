package io.github.calendrum.anonymize

// HMAC-SHA-256 with a tag byte for every use (text, address, UID, link, binary, stream), so equal bytes in different
// roles give unrelated results. A random stream is HMAC(key, stream tag, seed, counter) in blocks of 32 bytes, with the
// seed HMAC(key, tag, value). `token` takes 8 bytes (16 hex digits) and retries with another attempt byte while the hex
// contains a forbidden 4-gram.
internal class Pseudonyms(key: ByteArray, private val forbidden: ForbiddenGrams) {
    private val mac = HmacSha256(key)
    private val digest = ByteArray(Sha256.DIGEST_BYTES)

    fun stream(tag: Int, bytes: ByteArray, from: Int, to: Int): RandomStream {
        val seed = ByteArray(Sha256.DIGEST_BYTES)
        mac.start()
        mac.update(tag)
        mac.update(bytes, from, to)
        mac.finish(seed)
        return RandomStream(this, seed)
    }

    fun token(tag: Int, bytes: ByteArray): String {
        var attempt = 0
        while (true) {
            mac.start()
            mac.update(tag)
            mac.update(attempt)
            mac.update(bytes, 0, bytes.size)
            mac.finish(digest)
            val chars = CharArray(TOKEN_BYTES * 2)
            for (index in 0 until TOKEN_BYTES) {
                val byte = digest[index].toInt() and 0xFF
                chars[index * 2] = HEX[byte ushr 4]
                chars[index * 2 + 1] = HEX[byte and 0xF]
            }
            val token = chars.concatToString()
            attempt++
            if (attempt == MAX_ATTEMPTS || !forbidden.occursIn(token)) return token
        }
    }

    internal fun block(seed: ByteArray, counter: Int, out: ByteArray) {
        mac.start()
        mac.update(TAG_STREAM)
        mac.update(seed, 0, seed.size)
        mac.update(counter ushr 24)
        mac.update(counter ushr 16)
        mac.update(counter ushr 8)
        mac.update(counter)
        mac.finish(out)
    }

    companion object {
        const val TAG_TEXT: Int = 1
        const val TAG_ADDRESS: Int = 2
        const val TAG_UID: Int = 3
        const val TAG_URL: Int = 4
        const val TAG_BINARY: Int = 5
        private const val TAG_STREAM = 6
        const val TAG_ZONE: Int = 7
        private const val TOKEN_BYTES = 8
        private const val MAX_ATTEMPTS = 64
        private val HEX = "0123456789abcdef".toCharArray()
    }
}

internal class RandomStream(private val pseudonyms: Pseudonyms, private val seed: ByteArray) {
    private val block = ByteArray(Sha256.DIGEST_BYTES)
    private var position = block.size
    private var counter = 0

    fun nextByte(): Int {
        if (position == block.size) {
            pseudonyms.block(seed, counter++, block)
            position = 0
        }
        return block[position++].toInt() and 0xFF
    }

    fun nextInt(bound: Int): Int = ((nextByte() shl 8) or nextByte()) % bound
}

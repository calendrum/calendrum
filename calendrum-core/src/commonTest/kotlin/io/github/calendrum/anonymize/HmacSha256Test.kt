package io.github.calendrum.anonymize

import kotlin.test.Test
import kotlin.test.assertEquals

class HmacSha256Test {
    @Test
    fun sha256OfFipsVectors() {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", sha256(""))
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", sha256("abc"))
        assertEquals(
            "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
            sha256("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"),
        )
    }

    @Test
    fun sha256AcrossBlockBoundaries() {
        val message = ByteArray(1000) { (it * 7).toByte() }
        val whole = Sha256()
        whole.update(message, 0, message.size)
        val expected = ByteArray(32).also { whole.digest(it, 0) }
        for (split in listOf(1, 55, 56, 63, 64, 65, 127, 128, 999)) {
            val parts = Sha256()
            parts.update(message, 0, split)
            for (index in split until message.size) parts.update(message[index].toInt())
            val actual = ByteArray(32).also { parts.digest(it, 0) }
            assertEquals(expected.toHex(), actual.toHex(), "split at $split")
        }
    }

    @Test
    fun hmacOfRfc4231Vectors() {
        assertEquals(
            "b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7",
            hmac(ByteArray(20) { 0x0b }, "Hi There".encodeToByteArray()),
        )
        assertEquals(
            "5bdcc146bf60754e6a042426089575c75a003f089d2739839dec58b964ec3843",
            hmac("Jefe".encodeToByteArray(), "what do ya want for nothing?".encodeToByteArray()),
        )
        assertEquals(
            "773ea91e36800e46854db8ebd09181a72959098b3ef8c122d9635514ced565fe",
            hmac(ByteArray(20) { 0xaa.toByte() }, ByteArray(50) { 0xdd.toByte() }),
        )
        assertEquals(
            "60e431591ee0b67f0d8a26aacbf5b77f8e0bc6213728c5140546040f0ee37f54",
            hmac(
                ByteArray(131) {
                    0xaa.toByte()
                },
                "Test Using Larger Than Block-Size Key - Hash Key First".encodeToByteArray(),
            ),
        )
    }

    @Test
    fun hmacIsReusable() {
        val mac = HmacSha256("key".encodeToByteArray())
        val first = ByteArray(32)
        val second = ByteArray(32)
        mac.start()
        mac.update("message".encodeToByteArray(), 0, 7)
        mac.finish(first)
        mac.start()
        mac.update("other".encodeToByteArray(), 0, 5)
        mac.finish(second)
        mac.start()
        mac.update("message".encodeToByteArray(), 0, 7)
        val again = ByteArray(32).also { mac.finish(it) }
        assertEquals(first.toHex(), again.toHex())
        assertEquals(hmac("key".encodeToByteArray(), "other".encodeToByteArray()), second.toHex())
    }

    private fun sha256(text: String): String {
        val sha = Sha256()
        val bytes = text.encodeToByteArray()
        sha.update(bytes, 0, bytes.size)
        return ByteArray(32).also { sha.digest(it, 0) }.toHex()
    }

    private fun hmac(key: ByteArray, message: ByteArray): String {
        val mac = HmacSha256(key)
        mac.start()
        mac.update(message, 0, message.size)
        return ByteArray(32).also { mac.finish(it) }.toHex()
    }
}

internal fun ByteArray.toHex(): String = joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }

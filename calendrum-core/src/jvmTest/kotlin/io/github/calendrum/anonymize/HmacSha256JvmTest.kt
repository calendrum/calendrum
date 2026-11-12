package io.github.calendrum.anonymize

import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals

class HmacSha256JvmTest {
    @Test
    fun matchesTheJdk() {
        val random = Random(5545)
        repeat(500) {
            val key = random.nextBytes(random.nextInt(1, 200))
            val message = random.nextBytes(random.nextInt(0, 1000))
            val mac = HmacSha256(key)
            mac.start()
            var at = 0
            while (at < message.size) {
                val next = minOf(message.size, at + random.nextInt(1, 130))
                mac.update(message, at, next)
                at = next
            }
            val actual = ByteArray(32).also { mac.finish(it) }
            val jdk = Mac.getInstance("HmacSHA256").apply { init(SecretKeySpec(key, "HmacSHA256")) }
            assertContentEquals(jdk.doFinal(message), actual)
            val sha = Sha256()
            sha.update(message, 0, message.size)
            val digest = ByteArray(32).also { sha.digest(it, 0) }
            assertContentEquals(MessageDigest.getInstance("SHA-256").digest(message), digest)
        }
    }
}

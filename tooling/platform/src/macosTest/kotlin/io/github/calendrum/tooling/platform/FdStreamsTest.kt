@file:OptIn(ExperimentalForeignApi::class, ObsoleteWorkersApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.usePinned
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.io.write
import platform.posix.EINTR
import platform.posix.F_SETFL
import platform.posix.O_NONBLOCK
import platform.posix.SIGPIPE
import platform.posix.SIGUSR1
import platform.posix.SIG_IGN
import platform.posix.close
import platform.posix.errno
import platform.posix.fcntl
import platform.posix.pipe
import platform.posix.pthread_kill
import platform.posix.pthread_self
import platform.posix.read
import platform.posix.siginterrupt
import platform.posix.signal
import platform.posix.usleep
import platform.posix.write
import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FdStreamsTest {
    private fun pipePair(): Pair<Int, Int> = memScoped {
        val fds = allocArray<IntVar>(2)
        check(pipe(fds) == 0)
        fds[0] to fds[1]
    }

    @Test
    fun anInterruptedReadIsNotTheEndOfTheInput() {
        val (input, output) = pipePair()
        signal(SIGUSR1, staticCFunction { _: Int -> })
        siginterrupt(SIGUSR1, 1)
        val target = pthread_self()
        val worker = Worker.start()
        val job = worker.execute(TransferMode.SAFE, { Triple(target, output, 0) }) { (thread, fd, _) ->
            usleep(200_000u)
            pthread_kill(thread, SIGUSR1)
            usleep(200_000u)
            pthread_kill(thread, SIGUSR1)
            usleep(200_000u)
            byteArrayOf(42).usePinned { write(fd, it.addressOf(0), 1u.convert()) }
            Unit
        }
        // The setting works: a plain read gets EINTR from the first signal.
        val plain = ByteArray(1).usePinned { read(input, it.addressOf(0), 1u.convert()) }.toInt()
        assertEquals(-1, plain)
        assertEquals(EINTR, errno)
        val sink = Buffer()
        assertEquals(1L, FdSource(input).readAtMostTo(sink, 10))
        assertContentEquals(byteArrayOf(42), sink.readByteArray())
        job.result
        worker.requestTermination().result
        close(input)
        close(output)
    }

    @Test
    fun aNonBlockingInputWaitsForData() {
        val (input, output) = pipePair()
        check(fcntl(input, F_SETFL, O_NONBLOCK) == 0)
        val worker = Worker.start()
        val job = worker.execute(TransferMode.SAFE, { output }) { fd ->
            usleep(200_000u)
            byteArrayOf(7).usePinned { write(fd, it.addressOf(0), 1u.convert()) }
            Unit
        }
        val sink = Buffer()
        assertEquals(1L, FdSource(input).readAtMostTo(sink, 10))
        assertContentEquals(byteArrayOf(7), sink.readByteArray())
        job.result
        worker.requestTermination().result
        close(input)
        close(output)
    }

    @Test
    fun theEndOfAPipeIsTheEndOfTheInput() {
        val (input, output) = pipePair()
        close(output)
        assertEquals(-1L, FdSource(input).readAtMostTo(Buffer(), 10))
        close(input)
    }

    @Test
    fun aShortWriteToANonBlockingPipeIsContinued() {
        val (input, output) = pipePair()
        check(fcntl(output, F_SETFL, O_NONBLOCK) == 0)
        val size = 1024 * 1024
        val bytes = ByteArray(size) { (it % 251).toByte() }
        val worker = Worker.start()
        val job = worker.execute(TransferMode.SAFE, { input to size }) { (fd, total) ->
            val received = ByteArray(total)
            var filled = 0
            while (filled < total) {
                usleep(100u)
                val count = received.usePinned {
                    read(fd, it.addressOf(filled), minOf(4096, total - filled).convert())
                }.toInt()
                if (count <= 0) break
                filled += count
            }
            received.copyOf(filled)
        }
        val source = Buffer()
        source.write(bytes)
        FdSink(output) { error("The pipe is open") }.write(source, size.toLong())
        assertContentEquals(bytes, job.result)
        worker.requestTermination().result
        close(input)
        close(output)
    }

    @Test
    fun aClosedPipeIsReported() {
        val (input, output) = pipePair()
        signal(SIGPIPE, SIG_IGN)
        close(input)
        var closed = false
        val source = Buffer()
        source.write(byteArrayOf(1, 2, 3))
        FdSink(output) { closed = true }.write(source, 3)
        assertTrue(closed)
        close(output)
    }
}

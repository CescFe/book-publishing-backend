package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.time

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class SystemClockTest {
    @Test
    fun `should return current instant`() {
        val clock = SystemClock()
        val before = Instant.now()

        val now = clock.now()

        val after = Instant.now()
        assertTrue(!now.isBefore(before))
        assertTrue(!now.isAfter(after))
    }
}

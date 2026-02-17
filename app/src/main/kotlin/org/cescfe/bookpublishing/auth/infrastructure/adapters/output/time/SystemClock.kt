package org.cescfe.bookpublishing.auth.infrastructure.adapters.output.time

import org.cescfe.bookpublishing.auth.application.port.output.Clock
import java.time.Instant

class SystemClock : Clock {
    override fun now(): Instant = Instant.now()
}
